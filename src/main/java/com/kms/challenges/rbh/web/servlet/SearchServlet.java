/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.FileDao;
import com.kms.challenges.rbh.dao.impl.FileDaoImpl;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.UploadFile;
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
import java.util.List;
import java.util.Set;

/**
 * @author tkhuu.
 */
@WebServlet(name = "search-servlet", urlPatterns = "/search")
public class SearchServlet extends HttpServlet {
    private FileDao fileDao;
    public SearchServlet() {
        fileDao = new FileDaoImpl(new UserDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<User.ROLE> reqRoles = new HashSet<>();
        reqRoles.add(User.ROLE.USER);
        reqRoles.add(User.ROLE.ADMIN);
        if (!RabbitHolesUtil.authenticate((User) req.getSession().getAttribute("user"), reqRoles)) {
            resp.sendRedirect("/login");
        }
        String searchText = req.getParameter("searchText");
        try {
            List<UploadFile> fileList = fileDao.searchByFileName(searchText);
            req.setAttribute("files", fileList);
            req.setAttribute("tokenHeader", XsrfFilter.TOKEN_HEADER);
            req.setAttribute("token", XsrfFilter.getToken(req.getSession()));
            req.getRequestDispatcher("/jsp/search.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}
