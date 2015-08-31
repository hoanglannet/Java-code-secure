/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.FileDao;
import com.kms.challenges.rbh.dao.impl.FileDaoImpl;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.util.RabbitHolesUtil;
import com.kms.challenges.rbh.web.filter.XsrfFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tkhuu.
 */
@WebServlet(name = "index-servlet", urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    private FileDao dao;
    public IndexServlet() {
        dao = new FileDaoImpl(new UserDaoImpl());
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<User.ROLE> requireRoles = new HashSet<>();
        requireRoles.add(User.ROLE.ADMIN);
        requireRoles.add(User.ROLE.USER);
        if (!RabbitHolesUtil.authenticate((User) req.getSession().getAttribute("user"), requireRoles)) {
            resp.sendRedirect("/login");
        }
        try {
            req.setAttribute("files", dao.getAllFiles());
            req.setAttribute("tokenHeader", XsrfFilter.TOKEN_HEADER);
            req.setAttribute("token", XsrfFilter.getToken(req.getSession()));
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/jsp/index.jsp").forward(req, resp);
    }
}
