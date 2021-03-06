/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.UserDao;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.LoginForm;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.model.validation.ValidationError;
import com.kms.challenges.rbh.model.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tkhuu.
 */
@WebServlet(name = "login-servlet",urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServlet.class.getCanonicalName());
    private UserDao dao;
    public  LoginServlet() {
        dao = new UserDaoImpl();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("Login page initialize");
        getServletContext().getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, ValidationError> errorMap = new HashMap<>();
        LoginForm form=null;
        try {
            form = Validator.parseToBeanAndValidate(LoginForm.class, req.getParameterMap(), errorMap);
        } catch (IllegalAccessException|InstantiationException e) {
            throw new ServletException(e);
        }
        req.setAttribute("validationErrors", errorMap);
        try {
            User user = dao.getUserByEmailAndPassword(form.getEmail(), form.getPassword());
            if (user != null) {
                // Renew session when login for prevent session hijacking
                LOGGER.debug("Old Session --> " + req.getSession().getId());
                req.getSession().invalidate();
                LOGGER.debug("New Session --> " + req.getSession().getId());

                req.getSession().setAttribute("user", user);

                resp.sendRedirect("/");
            } else {
                req.setAttribute("loginSuccess", false);
                getServletContext().getRequestDispatcher("/jsp/user/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}
