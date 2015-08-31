/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.UserDao;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.RegistrationForm;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.model.validation.ValidationError;
import com.kms.challenges.rbh.model.validation.Validator;
import com.kms.challenges.rbh.util.SecureUtils;
import com.kms.challenges.rbh.web.filter.XsrfFilter;

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
@WebServlet(name = "registration-servlet", urlPatterns = "register")
public class RegistrationServlet extends HttpServlet {
    private UserDao dao;
    public RegistrationServlet() {
        dao = new UserDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tokenHeader", XsrfFilter.TOKEN_HEADER);
        req.setAttribute("token", XsrfFilter.getToken(req.getSession()));
        getServletContext().getRequestDispatcher("/jsp/user/registrationForm.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, ValidationError> errorMap = new HashMap<>();
        try {
            RegistrationForm form = Validator
                    .parseToBeanAndValidate(RegistrationForm.class, req.getParameterMap(), errorMap);
            if (!errorMap.isEmpty()) {
                req.setAttribute("validationErrors", errorMap);
                req.setAttribute("registrationForm", form);
                getServletContext().getRequestDispatcher("/jsp/user/registrationForm.jsp").forward(req, resp);
                return;
            }
            try {
                dao.addUser(
                        new User(null, SecureUtils.escape(form.getEmail()), SecureUtils.escape(form.getFirstName()),
                                 SecureUtils.escape(form.getLastName()), form.getPassword(), User.ROLE.USER));
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect("/login");
    }

}
