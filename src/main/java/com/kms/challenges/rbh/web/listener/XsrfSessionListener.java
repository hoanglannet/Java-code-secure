/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.listener;

/**
 * Created by khoahoang on 8/30/2015.
 */

import com.kms.challenges.rbh.web.filter.XsrfFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener()
public class XsrfSessionListener implements HttpSessionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(XsrfSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {

        HttpSession session = sessionEvent.getSession();
        session.setAttribute(XsrfFilter.TOKEN_HEADER, XsrfFilter.nextToken());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

    }

}
