/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by khoahoang on 8/31/2015.
 */
@WebFilter(filterName = "SecureHeaderFilter", urlPatterns = "*")
public class SecureHeaderFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) resp;
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");
        httpResponse.setHeader("X-Frame-Options", "deny");
        httpResponse.setHeader("Content-Security-Policy", " child-src 'none'; " +
                                                          "frame-src 'none';" +
                                                          "script-src 'self'; ");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
