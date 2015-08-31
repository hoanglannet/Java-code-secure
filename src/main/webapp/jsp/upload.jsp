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
        <div class="panel panel-default">
            <div class="panel-body">
                <h3>Share your file to the world!!!</h3>
                <div>
                    <c:forEach var="file" items="${files}">
                        <form action="/delete" method="POST">
                            <p>
                                <span>${file.uploader.firstName}</span> <span>: </span>
                                <span><a
                                        href="/download?fileName=${file.fileName}&userId=${file.uploader.id}">${file.fileName}</a></span>
                                <span>${file.uploadNote}</span>
                                <input type="hidden" name="${tokenHeader}"
                                       value="${token}">
                                <input type="hidden" name="fileId" value="${file.id}"/>
                                <button type="submit">Delete</button>
                            </p>
                        </form>
                    </c:forEach>
                </div>
                <form action="/upload" method="POST" enctype="multipart/form-data">
                    <div class="row">
                        <input type="hidden" name="${tokenHeader}"
                               value="${token}">
                        <div id="form-group-file" class="form-group col-lg-4">
                            <label class="control-label" for="file"><fmt:message key="label.vault.file"/>:</label>
                            <input id="file" name="file" type="file" class="form-inline"/>
                            <t:error fieldName="file" cssClass="help-block"/>
                        </div>
                    </div>

                    <div class="row">
                        <div id="form-group-desc" class="form-group col-lg-4">
                            <label class="control-label" for="upload_node"><fmt:message key="label.vault.description"/>:</label>
                            <input id="upload_node" name="upload_note" type="text" class="form-control"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group col-lg-4">
                            <button type="submit" class="btn btn-default"><fmt:message key="label.vault.submit.button"/></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </jsp:body>
</t:layout>