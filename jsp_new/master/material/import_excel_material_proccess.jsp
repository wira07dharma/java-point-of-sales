<%-- 
    Document   : import_excel_material_proccess
    Created on : Oct 23, 2018, 11:55:25 AM
    Author     : dimata005
--%>

<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.List"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.excel.importexcel.ImportRegister"%>

<%
    int type = 0;
    JspWriter output = pageContext.getOut();
    ImportRegister importDetail = new ImportRegister();
    if (!importDetail.isRunning() && importDetail.getTable().equals("")) {
        importDetail.startUpload(config, request, response, output, type);
    }

    double percentage = 0;
    if (importDetail.getTotalData() != 0 && importDetail.getCurrentProgress() != 0) {
        percentage = importDetail.getCurrentProgress() / importDetail.getTotalData() * 100;
        BigDecimal bd = new BigDecimal(percentage);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        percentage = bd.doubleValue();
    }
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <script type="text/javascript" src="../../styles/jquery-1.4.2.min.js"></script>
        <script src="../../styles/jquery.autocomplete.js"></script>
        <title>Import Excel</title>
        <style>
            table {font-size: 13px}
            table .th {white-space: nowrap}
        </style>
    </head>
    <body>
        <div class="col-md-12">
            <center><h2>Process Import Excel</h2></center>
            <div class="progress">
                <center>
                    <div class="progress-bar" role="progressbar" aria-valuenow="<%=percentage%>" aria-valuemin="0" aria-valuemax="100" style="width:<%=(percentage < 3 ? 3 : percentage)%>%">
                        <%=percentage%>%
                    </div>
                </center>
            </div>

            <% if (!importDetail.getTable().equals("")) {%>
            <center><h4>Import Result</h4></center>
            <div class="pre-scrollable">
                <%=importDetail.getTable()%>
            </div>
            <br>
            <a href="import_excel_material.jsp" class="btn btn-primary">Import Baru</a>
            <a href="<%=approot%>/master/material/srcmaterial.jsp?typemenu=0" class="btn btn-primary">Kembali ke pencarian</a>
            <% } else { %>
            <div class="text-center">Tunggu...</div>
            <% } %>

        </div>
    </body>
    <script>
        jQuery(function () {
        <% if (importDetail.getTable().equals("")) {  %>
            setTimeout(function () {
                window.location.href = window.location.href;
            }, 5 * 1000);
        <% } %>
        });

        <%
            if (!importDetail.isRunning()) {
                ImportRegister.countTotalData = 0;
                ImportRegister.currentProgress = 0;
                ImportRegister.table = "";
                ImportRegister.running = false;
            }
        %>
    </script>
</html>
