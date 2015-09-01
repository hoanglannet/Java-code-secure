/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao;

import com.kms.challenges.rbh.model.User;

import java.sql.SQLException;

/**
 * @author tkhuu.
 */
public interface UserDao {
    User getUser(long userId) throws SQLException;

    User getUserByEmailAndPassword(String email, String password) throws SQLException;

    void addUser(User user) throws SQLException;

}
