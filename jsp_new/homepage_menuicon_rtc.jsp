<%-- 
    Document   : homepage_menuicon_rtc
    Created on : Jan 22, 2014, 3:47:00 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.aplikasi.entity.uploadpicture.PictureBackground"%>
<%@page import="com.dimata.aplikasi.entity.uploadpicture.PstPictureBackground"%>
<%@page import="com.dimata.aplikasi.form.mastertemplate.CtrlTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="java.util.ResourceBundle.Control"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.aplikasi.form.mastertemplate.FrmTempDinamis"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN);
%>
<%@ include file = "main/checkuser.jsp" %>
<!DOCTYPE html>
<%
    String url= request.getParameter("menu");
    if(url!=null && url.length()>0){
        boolean cek = true;
    }
%>
<html>
<head>
<title>Dimata - ProChain POS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="styles/main.css" type="text/css">
<link rel="stylesheet" href="styles/tab.css" type="text/css">
<style type="text/css">
    .contentBgPicturex{
        position:relative;
        width:100%;
        min-height: 400px;
        background-attachment: local;
        /*background-image:url(stylesheets/images/logo.png);*/
        background-repeat: no-repeat;
        background-position: right bottom;
    }

</style>
</head>
<body  text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="contentBgPicturex">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FFFFFF">
  <tr>
    <td height="20" ID="MAINMENU">
         <%@include file="styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <tr>
    <td height="10" >
      <%@include file="menu_icon_rtc.jsp" %>
    </td>
  </tr>
  <tr>
    <td >&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" height="20">
      <%@include file="styletemplate/footer.jsp" %>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
