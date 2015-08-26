package com.kms.challenges.rbh.web.filter;

import com.kms.challenges.rbh.dao.RabbitHolesDao;
import com.kms.challenges.rbh.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author tkhuu.
 * Filter that add security information before processing request
 */
@WebFilter(filterName = "security-filter",urlPatterns = "*")
public class SecurityFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        //we only have 1 admin, great great admin
        //check if request have admin flag in cookie
        for (Cookie cookie : httpServletRequest.getCookies()) {
            if ("admin".equals(cookie.getName())) {
                // our great admin have come back welcomback, prepare the admin user for him
                try {
                    httpServletRequest.getSession().setAttribute("user", RabbitHolesDao.getInstance().getAdminUser());
                } catch (SQLException e) {
                    throw new ServletException(e);
                }
            }
        }
        if (user == null) {
            httpServletRequest.getSession().setAttribute("user",User.getNewAnonymousUser());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
