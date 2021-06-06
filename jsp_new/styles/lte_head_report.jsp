<%-- 
    Document   : lte_head_report
    Created on : Sep 6, 2017, 6:45:12 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN);%>
<%@ include file = "./../main/checkuser.jsp" %>
<% String root = approot; %>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<% // Tell the browser to be responsive to screen width %>
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<% /* - - - - - - - - - - - - - - - - Styles - - - - - - - - - - - - - - - */ %>
<% // Bootstrap 3.3.6 %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
<% // Font Awesome %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
<% // Ionicons %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
<% // Theme style %>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
<% /* AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. */%>
<link rel="stylesheet" href="<%=root%>/styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
 <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->