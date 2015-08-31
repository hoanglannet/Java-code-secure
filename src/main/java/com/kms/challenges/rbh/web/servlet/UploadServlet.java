/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.servlet;

import com.kms.challenges.rbh.dao.FileDao;
import com.kms.challenges.rbh.dao.impl.FileDaoImpl;
import com.kms.challenges.rbh.dao.impl.UserDaoImpl;
import com.kms.challenges.rbh.model.FileMetadata;
import com.kms.challenges.rbh.model.UploadFile;
import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.model.validation.ValidationError;
import com.kms.challenges.rbh.util.RabbitHolesUtil;
import com.kms.challenges.rbh.util.SecureUtils;
import com.kms.challenges.rbh.web.filter.XsrfFilter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * @author tkhuu.
 */
@WebServlet(name = "upload-servlet", urlPatterns = "/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServlet.class);
    private FileDao fileDao = null;
    public UploadServlet() {
        fileDao = new FileDaoImpl(new UserDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("Start do get upload : ");
        Set<User.ROLE> requireRoles = new HashSet<>();
        requireRoles.add(User.ROLE.ADMIN);
        requireRoles.add(User.ROLE.USER);
        User user = (User) req.getSession().getAttribute("user");
        LOGGER.debug(String.format("Got user login : ID = %s | EMAIL = %s", user.getId(), user.getEmail()));

        if (!RabbitHolesUtil.authenticate(user, requireRoles)) {
            LOGGER.debug("Authenticated!");
            resp.sendRedirect("/login");
        }
        try {
            List<UploadFile> files = fileDao.getFileByUserId(user.getId());
            LOGGER.debug(String.format("Got user files : NumberOfFile = %s", files.size()));
            req.setAttribute("files", files);
            req.setAttribute("tokenHeader", XsrfFilter.TOKEN_HEADER);
            req.setAttribute("token", XsrfFilter.getToken(req.getSession()));
            req.getRequestDispatcher("/jsp/upload.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uploadNote = SecureUtils.escape(req.getParameter("upload_note"));
        Part uploadedFile = req.getPart("file");
        Map<String, ValidationError> errorMap = new HashMap<>();
        if (!SecureUtils.fileValidator(uploadedFile.getName())) {
            errorMap.put("file", new ValidationError("file", "File format is not accepted!"));
            req.setAttribute("validationErrors", errorMap);
            req.getRequestDispatcher("/jsp/upload.jsp").forward(req, resp);
            return;
        }
        if (uploadedFile.getSize() == 0) {
            errorMap.put("file", new ValidationError("file", "Upload file is required"));
            req.setAttribute("validationErrors", errorMap);
            req.getRequestDispatcher("/jsp/upload.jsp").forward(req, resp);
            return;
        }
        User user = (User)
                req.getSession().getAttribute("user");
        File storeFolder = new File(RabbitHolesUtil.getUploadLocation() + user.getId() + "/");
        storeFolder.mkdirs();
        File storeFile = new File(
                RabbitHolesUtil.getUploadLocation() + user.getId() + "/" +
                SecureUtils.escape(uploadedFile.getSubmittedFileName()));
        try (FileOutputStream fileOutputStream = new FileOutputStream(storeFile)) {
            IOUtils.copy(uploadedFile.getInputStream(), fileOutputStream);
        }
        uploadedFile.getSize();
        UploadFile file = new UploadFile(null, SecureUtils.escape(uploadedFile.getSubmittedFileName()),
                                         SecureUtils.escape(uploadNote), new FileMetadata(null,
                                                                                          uploadedFile.getContentType(),
                                                                                          uploadedFile.getSize()),
                                         user);
        try {
            fileDao.addFile(file);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        resp.sendRedirect("/index");
    }
}
