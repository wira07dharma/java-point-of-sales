<%-- 
    Document   : prochain_home
    Created on : Jun 3, 2014, 9:55:54 AM
    Author     : sangtel6
--%>

<%@ page language="java" %>
<%@ include file = "main/javainit.jsp" %>

<html>
<head>
<title>Prochain
</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<!--update opie-eyek for menu 20140412 -->
<frameset rows="120,0,*" frameborder="NO" border="0" framespacing="0"> 
  <frameset rows="0,*" frameborder="NO" border="0" framespacing="0"> 
    <frame name="logoFrame" id="logoFrame" scrolling="NO" noresize>
    <%-- disini ubah gunakan if --%>    
    <frame name="leftFrame" id="leftFrame" scrolling="NO" noresize src="styletemplate/template_header_frame.jsp?SESS_LANGUAGE=<%=SESS_LANGUAGE%>">
  </frameset>
    <frameset rows="5,*"  frameborder="NO" border="0" framespacing="0" > 
  	<frame name="spaceTopFrame" id="spaceTopFrame" scrolling="NO" noresize>
    <frame name="spaceFrame" id="spaceFrame" scrolling="NO" noresize src="space.jsp" >  
  </frameset>	
  
  <frameset rows="5,*" frameborder="NO" border="0" framespacing="0" cols="*"> 
    <frame name="topFrame" id="topFrame" scrolling="YES">
    <frame name="mainFrame" id="mainFrame" scrolling="YES" noresize src="<%=approot%>/homepage_menuicon.jsp?menu=home">
  </frameset>
</frameset>
<noframes> 
<body bgcolor="#FFFFFF" text="#000000">
</body>
</noframes> 
</html>
