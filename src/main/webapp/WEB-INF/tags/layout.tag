<%--
  ~ Copyright (c) 2015 Kms-technology.com
  --%>

<!DOCTYPE html>
<%@ tag pageEncoding="UTF-8" %>
<%@ attribute name="header" fragment="true"  %>
<%@ attribute name="footer" fragment="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="rbt" uri="http://rbh.kms.challenges.com/customtags" %>
<fmt:setBundle basename="i18n.message" scope="request"/>
<html>
<head>
    <title><fmt:message key="rabbitholes.normal.title"/> -
        <jsp:invoke fragment="header"/>
    </title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/bootstrap-theme.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vendor/jquery-2.0.3.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/vendor/bootstrap.js"></script>
</head>
<body>
<div class="page">
    <div class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                <span class="sr-only"><fmt:message key="label.navigation.toggle.navigation.button"/></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <span class="navbar-brand"><fmt:message key="rabbitholes.brand"/></span>
        </div>
        <div class="collapse navbar-collapse navbar-ex1-collapse">
            <ul class="nav navbar-nav navbar-left">
                <rbt:authorize accessMode="AUTHORIZED">
                    <li><a href="/index"><fmt:message key="label.navigation.home.link"/></a></li>
                </rbt:authorize>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <rbt:authorize accessMode="ANONYMOUS">
                    <li><a href="${pageContext.request.contextPath}/login"><fmt:message
                            key="label.user.login.title"/></a></li>
                    <li><a href="${pageContext.request.contextPath}/register"><fmt:message
                            key="label.navigation.registration.link"/></a></li>
                </rbt:authorize>
                <rbt:authorize accessMode="AUTHORIZED">
                    <li>
                        <form action="${pageContext.request.contextPath}/logout" method="POST">
                            <input type="hidden" name="${tokenHeader}"
                                   value="${token}">
                            <fmt:message key="label.homepage.title"/> ${user.firstName} ${user.lastName}

                            <a href="/upload">Upload</a>
                            <button type="submit" class="btn btn-default navbar-btn">
                                <fmt:message key="label.navigation.logout.link"/>
                            </button>
                        </form>
                    </li>
                </rbt:authorize>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <div id="content">
        <jsp:doBody/>
    </div>
</div>
</body>
</html>
