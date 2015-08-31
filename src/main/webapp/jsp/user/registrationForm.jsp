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
    <jsp:attribute name="header"><fmt:message key="label.user.registration.page.title"/></jsp:attribute>
    <jsp:body>
        <rbt:authorize accessMode="ANONYMOUS">
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="${pageContext.request.contextPath}/register" commandName="user" method="POST" enctype="utf8" role="form">
                        <input type="hidden" name="${tokenHeader}"
                               value="${token}">
                        <div class="row">
                            <div id="form-group-firstName" class="form-group col-lg-4">
                                <label class="control-label" for="user-firstName"><fmt:message key="label.user.firstName"/>:</label>
                                <input id="user-firstName" name="first_name" cssClass="form-control" value="${registrationForm.getFirstName()}"/>
                                <t:error id="error-firstName" fieldName="first_name" cssClass="help-block"/>
                            </div>
                        </div>
                        <div class="row">
                            <div id="form-group-lastName" class="form-group col-lg-4">
                                <label class="control-label" for="user-lastName"><fmt:message key="label.user.lastName"/>:</label>
                                <input id="user-lastName" name="last_name" cssClass="form-control" value="${registrationForm.getLastName()}"/>
                                <t:error id="error-lastName" fieldName="last_name" cssClass="help-block"/>
                            </div>
                        </div>
                        <div class="row">
                            <div id="form-group-email" class="form-group col-lg-4">
                                <label class="control-label" for="user-email"><fmt:message key="label.user.email"/>:</label>
                                <input id="user-email" name="email" cssClass="form-control" value="${registrationForm.getEmail()}"/>
                                <t:error id="error-email" fieldName="email" cssClass="help-block"/>
                            </div>
                        </div>
                        <div class="row">
                            <div id="form-group-password" class="form-group col-lg-4">
                                <label class="control-label" for="user-password"><fmt:message key="label.user.password"/>:</label>
                                <input type="password" id="user-password" name="password" cssClass="form-control" />
                                <t:error id="error-password" fieldName="password" cssClass="help-block"/>
                            </div>
                        </div>
                        <div class="row">
                            <div id="form-group-passwordVerification" class="form-group col-lg-4">
                                <label class="control-label" for="user-passwordVerification"><fmt:message key="label.user.passwordVerification"/>:</label>
                                <input type="password" id="user-passwordVerification" name="password_confirm" cssClass="form-control"/>
                                <t:error id="error-passwordVerification" fieldName="passwordVerification" cssClass="help-block"/>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-default"><fmt:message key="label.user.registration.submit.button"/></button>
                    </form>
                </div>
            </div>
        </rbt:authorize>
        <rbt:authorize accessMode="AUTHORIZED">
            <p><fmt:message key="text.registration.page.authenticated.user.help"/></p>
        </rbt:authorize>
    </jsp:body>
</t:layout>