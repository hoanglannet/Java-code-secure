/*
 * Copyright (c) 2015 Kms-technology.com
 */

package com.kms.challenges.rbh.web.tag;

import com.kms.challenges.rbh.model.User;
import com.kms.challenges.rbh.web.filter.XsrfFilter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * @author tkhuu.
 */
public class TokenTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        if (!isAnonymous()) {
            JspWriter writer = getJspContext().getOut();
            String csrfHeader = XsrfFilter.TOKEN_HEADER;
            String token = getJspContext().getAttribute(csrfHeader, PageContext.SESSION_SCOPE).toString();
            writer.write(String.format("<input type='hidden' name='%s' value='%s'/>", csrfHeader, token));

        }
    }

    private boolean isAnonymous() throws JspException, IOException {
        User user = (User) getJspContext().getAttribute("user", PageContext.SESSION_SCOPE);
        return (user == null || user.getRole() == User.ROLE.ANNONYMOUS);
    }


}
