/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

/**
 * Created by khoahoang on 8/30/2015.
 */
public class FileQuery {
    public static final String INSERT_FILE = "insert into files(file_name,"
                                             + "upload_note,file_metadata_id)"
                                             + " values(?,?,?)";
    public static final String INSERT_META_FILE = "insert into file_metadatas"
                                                  + "(file_type,file_size) "
                                                  + "values(?,?)";
    public static final String SEARCH_FILE = "select * from files where "
                                             + "file_name like ?";
}

