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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preStatement = connection.prepareStatement("select * from files")) {
                try (ResultSet filesResultSet = preStatement.executeQuery()) {
                    while (filesResultSet.next()) {
                        long fileID = filesResultSet.getLong("id");
                        long file_metadata_id = filesResultSet.getLong("file_metadata_id");
                        FileMetadata metadata = getFileMetadata(file_metadata_id);
                        User user = getUploader(fileID);
                        UploadFile uploadFile = new UploadFile(fileID, filesResultSet.getString("file_name"),
                                                               filesResultSet.getString("upload_note"),
                                                               metadata, user);
                        files.add(uploadFile);
                    }
                }

            }
        }
        LOGGER.debug(String.format("Get all files  --> There are %s files.", files.size()));
        return files;
    }


    @Override
    public UploadFile getFile(Long fileId) throws SQLException {
        LOGGER.debug("Request file : " + fileId);
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement getFile = connection.prepareStatement(FileQuery.GET_FILE)) {
                getFile.setLong(1, fileId);
                try (ResultSet resultSet = getFile.executeQuery()) {
                    if (resultSet.next()) {
                        UploadFile uploadFile = new UploadFile(fileId, resultSet.getString("file_name"),
                                                               resultSet.getString("upload_note"),
                                                               getFileMetadata(resultSet.getLong("file_metadata_id")),
                                                               getUploader(fileId));
                        return uploadFile;
                    } else {
                        throw new RuntimeException("File not found");
                    }
                }

            }

        }
    }

    @Override
    public User getUploader(Long fileId) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement getUploader = connection.prepareStatement(FileQuery.GET_UPLOADER)) {
                getUploader.setLong(1, fileId);
                try (ResultSet resultSet = getUploader.executeQuery()) {
                    if (resultSet.next()) {
                        Long userId = resultSet.getLong("user_id");
                        LOGGER.debug(String.format("Got uploader of file %s: UserId = %s ", fileId, userId));
                        return userDao.getUser(userId);
                    } else {
                        throw new RuntimeException("Uploader not found");
                    }
                }

            }

        }
    }

    @Override
    public void deleteFile(UploadFile file) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            LOGGER.debug(String.format("Delete user file %s ", file.getId()));
            try (PreparedStatement deleteUserFile = connection.prepareStatement(FileQuery.DELETE_USER_FILE)) {
                deleteUserFile.setLong(1, file.getId());
                deleteUserFile.execute();
            }
            LOGGER.debug(String.format("Delete file %s ", file.getId()));
            try (PreparedStatement deleteFile = connection.prepareStatement(FileQuery.DELETE_FILE)) {
                deleteFile.setLong(1, file.getId());
                deleteFile.execute();
            }

            LOGGER.debug(String.format("Delete meta file %s ", file.getId()));
            try (PreparedStatement deleteMetaFile = connection.prepareStatement(FileQuery.DELETE_FILE_META)) {
                deleteMetaFile.setLong(1, file.getId());
                deleteMetaFile.execute();
            }

        }
    }

    @Override
    public FileMetadata getFileMetadata(long file_metadata_id) throws SQLException {
        FileMetadata metadata = null;
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement getMeta = connection.prepareStatement(FileQuery.GET_META)) {
                getMeta.setLong(1, file_metadata_id);
                try (ResultSet resultSet = getMeta.executeQuery()) {
                    if (resultSet.next()) {
                        metadata = new FileMetadata(resultSet.getLong("id"), resultSet.getString("file_type"),
                                                    resultSet.getLong("file_size"));
                    }
                }

            }
        }
        LOGGER.debug(String.format("Get meta file: ID = %s | type = %s | file_size = %s ",
                                   metadata.getId(), metadata.getFileType(), metadata.getFileSize()));
        return metadata;
    }

    @Override
    public Long addFile(UploadFile uploadFile) throws SQLException {
        Long metadataId = insertMetadata(uploadFile.getFileMetadata());
        Long fileId = null;
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement addFile = connection.prepareStatement(FileQuery.INSERT_FILE)) {
                addFile.setString(1, uploadFile.getFileName());
                addFile.setString(2, uploadFile.getUploadNote());
                addFile.setLong(3, metadataId);
                addFile.execute();

                try (ResultSet resultSet = addFile.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        fileId = resultSet.getLong(1);
                    }
                }
            }
        }
        LOGGER.debug(String.format("Added new file: Name = %s | Note = %s ",
                                   uploadFile.getFileName(), uploadFile.getUploadNote()));

        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement insertUserFile = connection.prepareStatement(FileQuery.INSERT_USER_FILE)) {
                insertUserFile.setLong(1, uploadFile.getUploader().getId());
                insertUserFile.setLong(2, fileId);
                insertUserFile.execute();
            }
        }
        LOGGER.debug(String.format("Added new meta file of " + uploadFile.getFileName()));
        return fileId;
    }

    @Override
    public Long insertMetadata(FileMetadata metadata) throws SQLException {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement insertMeta = connection.prepareStatement(FileQuery.INSERT_META_FILE)) {
                insertMeta.setString(1, metadata.getFileType());
                insertMeta.setLong(2, metadata.getFileSize());
                insertMeta.execute();
                try (ResultSet resultSet = insertMeta.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        return resultSet.getLong(1);
                    }
                }
            }
        }
        return null;
    }


    @Override
    public List<UploadFile> getFileByUserId(Long userId) throws SQLException {
        List<UploadFile> uploadFiles = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preStatement = connection.prepareStatement(FileQuery.GET_USER_FILE)) {
                preStatement.setLong(1, userId);
                try (ResultSet resultSet = preStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Long fileId = resultSet.getLong("file_id");
                        uploadFiles.add(getFile(fileId));
                    }
                }
            }
        }
        LOGGER.debug(String.format("Get all files by user id = %s | There are %s files ",
                                   userId, uploadFiles.size()));
        return uploadFiles;
    }

    @Override
    public List<UploadFile> searchByFileName(String fileName) throws SQLException {
        List<UploadFile> uploadFiles = new ArrayList<>();
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preStatement = connection.prepareStatement(FileQuery.SEARCH_FILE)) {
                preStatement.setString(1, "%%%" + fileName + "%%");
                try (ResultSet resultSet = preStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Long fileId = resultSet.getLong("id");
                        uploadFiles.add(getFile(fileId));
                    }
                }
            }

        }
        LOGGER.debug(String.format("Search file by string :%s | Results = %s ",
                                   fileName, uploadFiles.size()));
        return uploadFiles;
    }
}
