/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.util.RabbitHolesUtil;
import com.kms.challenges.rbh.util.SecureUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author tkhuu.
 */
@WebServlet(name = "download-servlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Download allow all user to download so let save sql select here
        String fileName = SecureUtils.filterFileName(req.getParameter("fileName"));
        Long userId = Long.parseLong(req.getParameter("userId"));
        User user = (User) req.getSession().getAttribute("user");
        if (!user.getId().equals(userId)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getOutputStream().println("You don't have permission to download this file!");
            return;
        }

        File file = new File(RabbitHolesUtil.properties.get("upload.location") + userId.toString() + "/" + fileName);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            resp.setHeader("Content-disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, resp.getOutputStream());
        }

    }
}
