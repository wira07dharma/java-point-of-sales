<%-- 
    Document   : import_excel_item_proccess
    Created on : Oct 21, 2019, 3:31:47 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.util.Command"%>
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
    long recId = FRMQueryString.requestLong(request, "receive_material_id");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
		<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
		<script src="../../../styles/jquery.autocomplete.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <%
			
            JspWriter output = pageContext.getOut();
            ImportRegister importDetail = new ImportRegister();
			if (!importDetail.isRunning() && importDetail.getTable().equals("")){
				importDetail.startUploadReceiveItem(config, request, response, output, type);
			} 
			double percentage = 0;
			if (importDetail.getTotalData() !=0 && importDetail.getCurrentProgress() != 0){
				percentage = importDetail.getCurrentProgress() / importDetail.getTotalData() * 100;
				BigDecimal bd = new BigDecimal(percentage);
				bd = bd.setScale(2, RoundingMode.HALF_UP);
				percentage = bd.doubleValue();
			}
			
        %>
		<div class="container">
			<center><h1>Process Import Excel</h1></center>
			<div class="progress">
				<center><div class="progress-bar" role="progressbar" aria-valuenow="<%=percentage%>" aria-valuemin="0" aria-valuemax="100" style="width:<%=(percentage < 3 ? 3 : percentage)%>%">
					<%=percentage%>%
				</div>
				</center>
			</div>
			
			<% if (!importDetail.getTable().equals("")){  %>
			<center><h1>Import Result</h1></center>
			<div class="pre-scrollable">
				<%=importDetail.getTable()%>
			</div>
			
			<% } %>
			
		</div>
    </body>
	<script>
	jQuery(function(){
		<% if (importDetail.getTable().equals("")){  %>
		setTimeout(function () { 
			window.location.href = window.location.href;
		  }, 5 * 1000);
		<% } %>
	});
	<%
		
	if (!importDetail.isRunning()){
		ImportRegister.countTotalData = 0;
		ImportRegister.currentProgress = 0;
		ImportRegister.table = "";
		ImportRegister.running = false;
	}	
	%>
        window.opener.location = "receive_wh_supp_material_edit_old.jsp?command=<%=Command.EDIT%>&hidden_receive_id=<%=recId%>";
</script>
</html>