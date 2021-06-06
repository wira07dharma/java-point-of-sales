<%-- 
    Document   : custome_report_excel
    Created on : Dec 13, 2017, 3:10:32 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<!DOCTYPE html>
<%
    int combo = FRMQueryString.requestInt(request, "combo");
    String startDate = FRMQueryString.requestString(request, "dateStart");
    String startEnd = FRMQueryString.requestString(request, "dateEnd");
    long FRM_FIELD_LOCATION_ID = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
    
    response.setContentType("application/x-msexcel"); 
    response.setHeader("Content-Disposition","attachment; filename=custome_report_excel.xls" ); 
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Custome Report Excel</title>
    </head>
    <body>
        
    </body>
</html>
