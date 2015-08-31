/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.listener;

import com.kms.challenges.rbh.dao.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author tkhuu.
 */
@WebListener
public class RabbitHoleServletContextListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RabbitHoleServletContextListener.class.getCanonicalName());
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("Servlet Context initalized, ");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOGGER.info("Servlet context destroyed");
        ConnectionManager.shutdown();
    }
}
