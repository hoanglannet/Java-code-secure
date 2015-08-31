<%--
  ~ Copyright (c) 2015 Kms-technology.com
  --%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rb" uri="http://rbh.kms.challenges.com/customtags" %>
<t:layout>
    <jsp:attribute name="header"><spring:message code="rabbitholes.homepage.title"/></jsp:attribute>
    <jsp:body>
        <div class="panel panel-default">
            <div class="panel-body">
                <h3>Welcome to the world of files!!!</h3>

                <div>
                    <div>
                        <form action="/search" method="GET">
                            <div class="row">
                                <div id="form-group-file" class="form-group col-lg-4">
                                    <label>Search: </label>
                                    <input type="text" name="searchText" class="form-control form-inline"
                                           placeholder="Enter your keywords">
                                    <input type="submit" value="Search" class="btn btn-default">
                                </div>
                            </div>
                        </form>
                    </div>
                    <c:choose>
                        <c:when test="${fn:length(files) eq 0}">
                            <span>No result!</span>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="file" items="${files}">
                                <p>
                                    <span>${file.uploader.firstName}</span> <span>: </span>
                                    <span><a href="/download?fileName=${file.fileName}&userId=${file.uploader.id}">${file.fileName}</a></span>
                                    <span>${file.uploadNote}</span>
                                </p>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>


                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>