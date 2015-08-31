<%--
  ~ Copyright (c) 2015 Kms-technology.com
  --%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<t:layout>
    <jsp:attribute name="header"><fmt:message key="rabbitholes.homepage.title"/></jsp:attribute>
    <jsp:body>
        <div class="row col-lg-5 col-lg-offset-3">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3>Share your file to the world</h3>
                </div>

                <div class="panel-body">
                    <form action="/upload" method="POST" enctype="multipart/form-data">
                        <div class="row">
                            <input type="hidden" name="${tokenHeader}"
                                   value="${token}">

                            <div id="form-group-file" class="form-group col-lg-4">
                                <label class="control-label" for="file"><fmt:message key="label.vault.file"/>:</label>
                                <input class="form-control form-inline" id="file" name="file" type="file"/>
                                <t:error fieldName="file" cssClass="help-block"/>
                            </div>
                        </div>

                        <div class="row">
                            <div id="form-group-desc" class="form-group col-lg-4">
                                <label class="control-label" for="upload_node"><fmt:message
                                        key="label.vault.description"/>:</label>
                                <input id="upload_node" name="upload_note" type="text" class="form-control"
                                       placeholder="Enter file note.."/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group col-lg-4">
                                <button type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-ok"></i>
                                    <fmt:message
                                            key="label.vault.submit.button"/></button>
                            </div>
                        </div>
                    </form>
                    <br/>

                    <div>
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th class="col-lg-2">Uploader</th>
                                <th class="col-lg-4">File Name</th>
                                <th class="col-lg-4">File Note</th>
                                <th class="col-lg-2"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="file" items="${files}">
                                <form action="/delete" method="POST">

                                    <tr>
                                        <td><span> <i
                                                class="glyphicon glyphicon-user"></i> ${file.uploader.firstName}</span>
                                            <span></span></td>
                                        <td>
                                        <span> <i class="glyphicon glyphicon-file"></i> <a
                                                href="/download?fileName=${file.fileName}&userId=${file.uploader.id}">${file.fileName}</a></span>
                                        </td>
                                        <td>
                                            <span>${file.uploadNote}</span></td>
                                        <td>
                                            <input type="hidden" name="${tokenHeader}"
                                                   value="${token}">
                                            <input type="hidden" name="fileId" value="${file.id}"/>
                                            <button class="btn btn-danger" type="submit"><i
                                                    class="glyphicon glyphicon-remove"></i> Delete
                                            </button>
                                        </td>
                                    </tr>
                                </form>
                            </c:forEach>
                            </tbody>
                        </table>


                    </div>
                </div>

            </div>
        </div>
    </jsp:body>
</t:layout>