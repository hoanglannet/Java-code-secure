/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

/**
 * Created by khoahoang on 8/30/2015.
 */
public class FileQuery {
    public static final String INSERT_FILE = "insert into files(file_name,upload_note,file_metadata_id) values(?,?,?)";
    public static final String INSERT_META_FILE = "insert into file_metadatas (file_type,file_size) values(?,?)";
    public static final String SEARCH_FILE = "select * from files where file_name like ?";
    public static final String GET_USER_FILE = "select * from user_files where user_id = ?";
    public static final String GET_META = "select * from file_metadatas where id = ?";
    public static final String DELETE_USER_FILE = "delete from user_files where file_id = ?";
    public static final String DELETE_FILE = "delete from files where id = ?";
    public static final String DELETE_FILE_META = "delete from file_metadatas where id = ?";
    public static final String INSERT_USER_FILE = "insert into user_files(user_id,file_id) values(?,?)";
    public static final String GET_UPLOADER = "select * from user_files where file_id = ?";
    public static final String GET_FILE = "select * from files where id = ?";
}

