/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

import com.kms.challenges.rbh.dao.ConnectionManager;
import com.kms.challenges.rbh.dao.UserDao;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.util.SecureUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author tkhuu.
 */
public class UserDaoImpl extends AbstractMethodError implements UserDao {
    @Override
    public User getUser(long userId) throws SQLException {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement selectUser = con.prepareStatement(UserQuery.SELECT_USER)) {
                selectUser.setLong(1, userId);
                try (ResultSet resultSet = selectUser.executeQuery()) {
                    if (resultSet.next()) {
                        return convertResultSetToUser(resultSet);
                    }
                    return null;
                }
            }
        }

    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement getUser = con.prepareStatement(UserQuery.GET_USER)) {

                getUser.setString(1, SecureUtils.escape(email));
                getUser.setString(2, password);

                try (ResultSet resultSet = getUser.executeQuery()) {
                    User user = null;
                    if (resultSet.next()) {
                        user = convertResultSetToUser(resultSet);
                    }
                    return user;
                }
            }
        }


    }

    private User convertResultSetToUser(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("id"),
                resultSet.getString("email"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"), null,
                User.ROLE.valueOf(resultSet.getString("role")));
    }


    @Override
    public void addUser(User user) throws SQLException {
        try (Connection con = ConnectionManager.getConnection()) {
            try (PreparedStatement insert = con.prepareStatement(UserQuery.INSERT_USER)) {
                insert.setString(1, SecureUtils.escape(user.getEmail()));
                insert.setString(2, SecureUtils.escape(user.getFirstName()));
                insert.setString(3, SecureUtils.escape(user.getLastName()));
                insert.setString(4, user.getPassword());
                insert.setString(5, user.getRole().name());
                insert.execute();
            }
        }

    }


}
