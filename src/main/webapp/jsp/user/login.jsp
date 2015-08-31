<%--
  ~ Copyright (c) 2015 Kms-technology.com
  --%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="rbt" uri="http://rbh.kms.challenges.com/customtags"%>
<t:layout>
    <jsp:attribute name="header"><fmt:message key="label.user.login.page.title"/></jsp:attribute>
    <jsp:body>
        <rbt:authorize accessMode="ANONYMOUS">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2><fmt:message key="label.login.form.title"/></h2>
                    <c:if test="${loginSuccess!=null&&!loginSuccess}">
                        <div class="alert alert-danger alert-dismissable">
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                            <fmt:message key="text.login.page.login.failed.error"/>
                        </div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/login" method="POST" role="form">

                        <div class="row">
                            <div id="form-group-email" class="form-group col-lg-4">
                                <label class="control-label" for="user-email"><fmt:message key="label.user.email"/>:</label>
                                <input id="user-email" name="email" type="text" class="form-control"/>
                                <t:error  id="email_error" fieldName="email" cssClass="help-block"/>
                            </div>
                        </div>

                        <div class="row">
                            <div id="form-group-password" class="form-group col-lg-4">
                                <label class="control-label" for="user-password"><fmt:message key="label.user.password"/>:</label>
                                <input id="user-password" name="password" type="password" class="form-control"/>
                                <t:error  id="password_error" fieldName="password" cssClass="help-block"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-lg-4">
                                <button type="submit" class="btn btn-default"><fmt:message key="label.user.login.submit.button"/></button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </rbt:authorize>
        <rbt:authorize accessMode="AUTHORIZED">
            <p><fmt:message key="text.login.page.authenticated.user.help"/></p>
        </rbt:authorize>
    </jsp:body>
</t:layout>