/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.FileDao;
import com.kms.challenges.rbh.dao.impl.FileDaoImpl;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.UploadFile;
import com.kms.challenges.rbh.util.RabbitHolesUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author tkhuu.
 */
@WebServlet(name = "delete-servlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    private FileDao fileDao;
    public DeleteServlet() {
        fileDao = new FileDaoImpl(new UserDaoImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileIdString = req.getParameter("fileId");
        if (fileIdString != null) {
            Long fileId = Long.parseLong(fileIdString);
            try {
                UploadFile file = fileDao.getFile(fileId);
                if (file == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                fileDao.deleteFile(file);
                File onDiskFile = new File(RabbitHolesUtil.getFileLocation(file));
                if (onDiskFile.exists()) {
                    onDiskFile.delete();
                }
                resp.sendRedirect("/index");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
