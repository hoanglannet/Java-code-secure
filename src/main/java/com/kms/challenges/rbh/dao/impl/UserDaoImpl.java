/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.dao.impl;

import com.kms.challenges.rbh.dao.ConnectionManager;
import com.kms.challenges.rbh.dao.UserDao;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.util.SecureUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author tkhuu.
 */
public class UserDaoImpl extends AbstractMethodError implements UserDao {
    @Override
    public User getUser(long userId) throws SQLException {
        try (Statement select = ConnectionManager.getConnection()
                                                 .createStatement()) {
            try (ResultSet resultSet = select
                    .executeQuery(String.format("select * from user_accounts "
                                                + "where id=%s", userId))) {
                if (resultSet.next()) {
                    return convertResultSetToUser(resultSet);
                }
                return null;
            }
        }
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws SQLException {
        try (PreparedStatement getUser = ConnectionManager
                .getConnection().prepareStatement(Query.GET_USER)) {

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

    @Override
    public User getAdminUser() throws SQLException {
        try (Statement select = ConnectionManager.getConnection()
                                                 .createStatement()) {
            try (ResultSet resultSet = select
                    .executeQuery(
                            String.format("select * from user_accounts where "
                                          + "role='%s'", User.ROLE.ADMIN))) {
                User user = null;

                if (resultSet.next()) {
                    user = convertResultSetToUser(resultSet);
                }
                return user;
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
        try (PreparedStatement insert = ConnectionManager.getConnection()
                                                         .prepareStatement
                                                                 (Query.INSERT_USER)) {
            insert.setString(1, SecureUtils.escape(user.getEmail()));
            insert.setString(2, SecureUtils.escape(user.getFirstName()));
            insert.setString(3, SecureUtils.escape(user.getLastName()));
            insert.setString(4, user.getPassword());
            insert.setString(5, user.getRole().name());

            insert.execute();
        }
    }

    class Query {
        public static final String INSERT_USER = "insert into user_accounts"
                                                 + "(email,first_name,"
                                                 + "last_name,password,role) "
                                                 + "values(?,?,?,?,?)";

        public static final String GET_USER = "select * from user_accounts "
                                              + "where email=? and "
                                              + "password=?";
    }
}
