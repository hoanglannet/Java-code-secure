<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="rb" uri="http://rbh.kms.challenges.com/customtags" %>
<%--
  ~ Copyright (c) 2015 Kms-technology.com
  --%>

<t:layout>
    <jsp:attribute name="header"><fmt:message key="rabbitholes.homepage.title"/></jsp:attribute>
    <jsp:body>
        <div class="row col-lg-8 col-lg-offset-2">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3>Welcome to the world of files!!!</h3>
                </div>
                <div class="panel-body">


                    <div>
                        <c:choose>
                            <c:when test="${fn:length(files) eq 0}">
                                <span>It's a bit cold here as no file is available at the moment!</span>
                                <span>Please <a href="/upload">upload</a> your file!</span>
                            </c:when>
                            <c:otherwise>
                                <div class="row col-lg-12">
                                    <form action="/search" method="GET">
                                        <div class="row">
                                            <div id="form-group-file" class="form-group col-lg-10">
                                                <input type="text" name="searchText" class="form-control form-inline"
                                                       placeholder="Enter your keywords">
                                            </div>
                                            <button class="btn btn-default col-lg-2"><i
                                                    class="glyphicon glyphicon-search"></i> Search File
                                            </button>
                                        </div>
                                    </form>
                                </div>

                                <br/>

                                <div>
                                    <table class="table table-hover">
                                        <thead>
                                        <tr>
                                            <th class="col-lg-4">Uploader</th>
                                            <th class="col-lg-4">File Name</th>
                                            <th class="col-lg-6">File Note</th>
                                            <th class="col-lg-2"></th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach var="file" items="${files}">
                                            <tr>
                                                <td><strong><i
                                                        class="glyphicon glyphicon-user"></i> ${file.uploader.firstName}
                                                </strong></td>
                                                <td><i class="glyphicon glyphicon-file"></i>
                                        <span> <a
                                                href="/download?fileName=${file.fileName}&userId=${file.uploader.id}">${file.fileName}</a></span>
                                                </td>
                                                <td>
                                                    <span>${file.uploadNote}</span></td>
                                                <td>

                                                    <rb:authenticate access="${user.role}" principleName="ADMIN">
                                                        <form action="/delete" method="POST">
                                                            <input type="hidden" name="${tokenHeader}"
                                                                   value="${token}">
                                                            <input type="hidden" name="fileId" value="${file.id}"/>
                                                            <button class="btn btn-danger" type="submit"><i
                                                                    class="glyphicon glyphicon-remove"></i> Delete
                                                            </button>
                                                        </form>
                                                    </rb:authenticate>

                                                </td>
                                            </tr>

                                        </c:forEach>
                                        </tbody>
                                    </table>


                                </div>

                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>