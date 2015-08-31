/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by khoahoang on 8/30/2015.
 */
@WebFilter(filterName = "XsrfFilter", urlPatterns = "*")
public class XsrfFilter implements Filter {
    public static final String TOKEN_HEADER = "_csrf";

    private static final Logger LOGGER = LoggerFactory.getLogger(XsrfFilter.class);

    public static String getToken(HttpSession session) {
        return session.getAttribute(XsrfFilter.TOKEN_HEADER).toString();
    }

    public static String nextToken() {
        return UUID.randomUUID().toString();
    }

    public static boolean isFormSubmission(HttpServletRequest httpRequest) {
        return "POST".equals(httpRequest.getMethod());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                                     ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String clientToken = req.getParameter(XsrfFilter.TOKEN_HEADER);
        String serverToken = req.getSession().getAttribute(XsrfFilter.TOKEN_HEADER).toString();
        LOGGER.debug(String.format("Request URL --> %s", req.getRequestURI()));
        LOGGER.debug(String.format("--> Client Token : %s | Server Token : %s", clientToken, serverToken));

        if ((isFormSubmission(req) && (clientToken == null || !clientToken.equals(serverToken)))) {
            httpResponse.setStatus(403);
            httpResponse.getOutputStream().println("Don't hack me!");
            return;
        }


        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
