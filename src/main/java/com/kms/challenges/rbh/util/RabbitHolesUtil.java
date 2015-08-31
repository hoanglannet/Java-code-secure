/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.util;

import com.kms.challenges.rbh.model.UploadFile;
import com.kms.challenges.rbh.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author tkhuu.
 */
public class RabbitHolesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitHolesUtil.class);

    public static Properties properties;
    static {
        properties = new Properties();
        try {
            properties.load(RabbitHolesUtil.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Can't load application.properties",e);
        }
    }

    public static String getUploadLocation() {
        return properties.getProperty("upload.location");
    }

    public static String getFileLocation(UploadFile file) {
        return getUploadLocation() + file.getUploader().getId() + "/" + file.getFileName();
    }

    public static boolean authenticate(User user, Set<User.ROLE> requireRole) {
        LOGGER.debug(String.format("Ask for authetication : User = %s  | Role = %s", user.getEmail(), user.getRole()));
        return user != null && requireRole.contains(user.getRole());
    }


}
