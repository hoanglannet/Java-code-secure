/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

import com.kms.challenges.rbh.dao.AbstractRabbitHoleDao;
import com.kms.challenges.rbh.dao.ConnectionManager;
import com.kms.challenges.rbh.dao.FileDao;
import com.kms.challenges.rbh.dao.UserDao;
import com.kms.challenges.rbh.model.FileMetadata;
import com.kms.challenges.rbh.model.UploadFile;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.util.SecureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tkhuu.
 */
public class FileDaoImpl extends AbstractRabbitHoleDao implements FileDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDaoImpl.class);
    private UserDao userDao;

    public FileDaoImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<UploadFile> getAllFiles() throws SQLException {
        List<UploadFile> files = new ArrayList<>();
        try (Statement select = ConnectionManager.getConnection().createStatement()) {
            try (ResultSet resultSet = select.executeQuery("select * from files")) {
                while (resultSet.next()) {
                    //get file metadata
                    long file_metadata_id = resultSet.getLong("file_metadata_id");
                    FileMetadata metadata = getFileMetadata(file_metadata_id);
                    //try to get the user
                    User user = null;
                    try (Statement selectUserFiles = ConnectionManager.getConnection()
                                                                      .createStatement();
                         ResultSet userFileResultSet = selectUserFiles.executeQuery(
                                 String.format("select * from user_files where file_id=%s", resultSet.getLong("id")))) {
                        if (userFileResultSet.next()) {
                            user = userDao.getUser(userFileResultSet.getLong("user_id"));
                        }
                    }
                    UploadFile uploadFile = new UploadFile(resultSet.getLong("id"),
                                                           resultSet.getString("file_name"),
                                                           resultSet.getString("upload_note"), metadata, user);
                    files.add(uploadFile);
                }
            }
        }
        return files;

    }

    @Override
    public UploadFile getFile(Long fileId) throws SQLException {
        try (Statement statement = ConnectionManager.getConnection().createStatement()) {
            try (ResultSet resultSet = statement
                    .executeQuery(String.format("select * from files where id=%s", fileId))) {
                if (resultSet.next()) {
                    UploadFile uploadFile = new UploadFile(fileId, resultSet.getString("file_name"),
                                                           resultSet.getString("upload_note"),
                                                           getFileMetadata(resultSet.getLong("file_metadata_id")),
                                                           getUploader(fileId));
                    LOGGER.debug(String.format("Get file by id : ID = %s | Filename = %s | Uploader = %s",
                                               uploadFile.getId(),
                                               uploadFile.getFileName(), uploadFile.getUploader().getEmail()));
                    return uploadFile;
                } else {
                    throw new RuntimeException("File not found");
                }
            }

        }
    }

    @Override
    public User getUploader(Long fileId) throws SQLException {
        try (Statement select = ConnectionManager.getConnection().createStatement()) {
            try (ResultSet resultSet = select
                    .executeQuery(String.format("select * from user_files where file_id=%s", fileId))) {
                if (resultSet.next()) {
                    Long userId = resultSet.getLong("user_id");
                    return userDao.getUser(userId);
                } else {
                    throw new RuntimeException("Uploader not found");
                }
            }

        }
    }

    @Override
    public void deleteFile(UploadFile file) throws SQLException {
        try (Statement delete = ConnectionManager.getConnection().createStatement()) {
            delete.execute(String.format("delete from user_files where file_id=%s", file.getId()));
            delete.execute(String.format("delete from files where id=%s", file.getId()));
            delete.execute(String.format("delete from file_metadatas where id=%s", file.getFileMetadata().getId()));
        }
    }

    @Override
    public FileMetadata getFileMetadata(long file_metadata_id) throws SQLException {
        FileMetadata metadata = null;
        try (Statement selectMetadata = ConnectionManager.getConnection()
                                                         .createStatement();
             ResultSet metadataResultSet = selectMetadata
                     .executeQuery(String.format("select * from file_metadatas where id=%s",
                                                 file_metadata_id))) {
            if (metadataResultSet.next()) {
                metadata = new FileMetadata(metadataResultSet.getLong("id"), metadataResultSet.getString("file_type"),
                                            metadataResultSet.getLong("file_size"));
            }
        }
        return metadata;
    }

    @Override
    public Long addFile(UploadFile uploadFile) throws SQLException {

        Long metadataId = insertMetadata(uploadFile.getFileMetadata());
        Long fileId = null;
        try (PreparedStatement insertFile = ConnectionManager.getConnection()
                                                             .prepareStatement(
                                                                     FileQuery.INSERT_FILE)) {
            insertFile.setString(1, SecureUtils.escape(uploadFile.getFileName()));
            insertFile.setString(2, SecureUtils.escape(uploadFile.getUploadNote()));
            insertFile.setLong(3, metadataId);
            LOGGER.debug(String.format("Input upload file : FileName = %s - Node = %s - MetaId = %s ",
                                       uploadFile.getFileName(), uploadFile.getUploadNote(), metadataId));
            insertFile.execute();
            ResultSet resultSet = insertFile.getGeneratedKeys();
            if (resultSet.next()) {

                fileId = resultSet.getLong(1);
                LOGGER.debug(fileId.toString());
            }
        }
        try (Statement insertFileUser = ConnectionManager.getConnection().createStatement()) {
            insertFileUser.execute(String.format("insert into user_files(user_id,file_id) values(%s,%s)",
                                                 uploadFile.getUploader().getId(), fileId));
        }
        return fileId;
    }

    @Override
    public Long insertMetadata(FileMetadata metadata) throws SQLException {
        try (PreparedStatement insertMetadata = ConnectionManager
                .getConnection().prepareStatement(FileQuery.INSERT_META_FILE)) {
            insertMetadata.setString(1, metadata.getFileType());
            insertMetadata.setLong(2, metadata.getFileSize());
            insertMetadata.execute();
            ResultSet resultSet = insertMetadata.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        }
        return null;
    }

    @Override
    public List<UploadFile> getFileByUserId(Long userId) throws SQLException {
        List<UploadFile> uploadFiles = new ArrayList<>();
        try (Statement select = ConnectionManager.getConnection().createStatement()) {
            ResultSet resultSet = select
                    .executeQuery(String.format("select * from user_files where user_id=%s", userId));
            while (resultSet.next()) {
                Long fileId = resultSet.getLong("file_id");
                uploadFiles.add(getFile(fileId));
            }
        }
        return uploadFiles;
    }

    @Override
    public List<UploadFile> searchByFileName(String fileName) throws SQLException {
        List<UploadFile> uploadFiles = new ArrayList<>();
        try (PreparedStatement select = ConnectionManager.getConnection()
                                                         .prepareStatement(FileQuery.SEARCH_FILE)) {
            select.setString(1, "%%" + SecureUtils.escape(fileName) + "%%");
            ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                Long fileId = resultSet.getLong("id");
                uploadFiles.add(getFile(fileId));
            }
        }
        return uploadFiles;
    }
}
