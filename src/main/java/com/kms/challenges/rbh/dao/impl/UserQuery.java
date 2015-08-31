/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

public class UserQuery {
    public static final String INSERT_USER = "insert into " +
                                             "user_accounts(email,first_name,last_name,password,role) " +
                                             "values(?,?,?,?,?)";
    public static final String GET_USER = "select * from user_accounts where email=? and password=?";
    public static final String SELECT_USER = "select * from user_accounts where id=?";
}

