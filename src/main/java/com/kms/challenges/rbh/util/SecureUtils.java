/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by khoahoang on 8/30/2015.
 */
public class SecureUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecureUtils.class);

    public static String escape(String input) {

        String[] identify = new String[]{"&", "<", ">", "\"", "'", "/", ";"};
        String[] replace = new String[]{"&amp;", "&lt;", "&gt;", "&quot;",
                                        "&#x27;", "&#x2F;", " "};
        return StringUtils.replaceEach(input, identify, replace);
    }

    public static String filterFileName(String input) {

        String[] identify = new String[]{"/", "\\", ".."};
        String[] replace = new String[]{"", "", ""};
        return StringUtils.replaceEach(input, identify, replace);
    }

    public static boolean fileValidator(String input) {
        Pattern pattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|rar|zip|doc|txt))$)");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}

