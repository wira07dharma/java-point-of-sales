<%-- 
    Document   : import_item_excel
    Created on : Oct 19, 2019, 9:29:46 AM
    Author     : IanRizky
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%@ page import="com.dimata.gui.jsp.*"%>
<!DOCTYPE html>
<%
	int iCommand = FRMQueryString.requestCommand(request);
	long recId = FRMQueryString.requestLong(request, "receive_material_id");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Import Data</title>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
        <script src="../../../styles/jquery.autocomplete.js"></script>
        <link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
        <%@ include file = "../../../styles/lte_head.jsp" %>
        <style>
            body {font-family: Arial;}

            /* Style the tab */
            .tab {
                overflow: hidden;
                border: 1px solid #ccc;
                background-color: #f1f1f1;
            }

            /* Style the buttons inside the tab */
            .tab button {
                background-color: inherit;
                float: left;
                border: none;
                outline: none;
                cursor: pointer;
                padding: 14px 16px;
                transition: 0.3s;
                font-size: 17px;
            }

            /* Change background color of buttons on hover */
            .tab button:hover {
                background-color: #ddd;
            }

            /* Create an active/current tablink class */
            .tab button.active {
                background-color: #ccc;
            }

            /* Style the tab content */
            .tabcontent {
                display: none;
                padding: 6px 12px;
                border: 1px solid #ccc;
                border-top: none;
            }
        </style>
        <script language="JavaScript">

            function cmdAdd() {
                document.form1.submit();
            }
        </script>
    </head>
    <body class="flexbox" style="background-color: rgb(245, 247, 255) !important;">
        <section class="content-header">
            <h1>
              Import Item
              <small></small>
            </h1>
            <ol class="breadcrumb">
              <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
              <li class="active">Import Item</li>
            </ol>
        </section>
        <div class="content">
            <div>    
                <form name="form1" method="post" action="import_excel_item_proccess.jsp?receive_material_id=<%=recId%>" enctype="multipart/form-data">
					<input type="hidden" name="receive_material_id" value="<%=recId%>">
					<table width="100%" border="0" cellspacing="2" cellpadding="2">
						<tr align="left">
                            <td height="21" valign="" width="13%">Template</td>
                            <td height="21" width="2%" valign="">:</td>
                            <td height="21" width="85%" valign="">
                                <select name="template" class="formElemen">
									<option value="1">1</option>
									<option value="2">2</option>
								</select>
                            </td>
                        </tr>
					</table>
					<div>&nbsp;</div>
                    <input type="file" name="file">
                    <input type="submit" name="Submit" value="Submit">
                </form>
                <div>&nbsp;</div>
<!--                <div><a href="javascript:cmdDownload()">Download file example</a></div>-->
            </div>
        </div>
    </body>
</html>
