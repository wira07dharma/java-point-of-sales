<% 
/* 
 * Page Name  		:  sys_info.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
 
<%@ page language="java" %>
<!-- package java -->
<!-- package qdep -->
<%@ page import="com.dimata.qdep.system.I_SystemInfo"%>
<!-- package project -->

<%@ include file = "main/javainit.jsp" %>

<!-- JSP Block -->
<%
if(isLoggedIn==false) {
%>
<script language="javascript">
	window.location="index.html";
</script>
<%
}

String sic = (request.getParameter("ic")==null) ? "0" : request.getParameter("ic");
int infCode = 0;
String msgAccess = "";
try {
	infCode = Integer.parseInt(sic);
}
catch(Exception e) { 
	infCode = 0;
}
%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="styles/tab.css" type="text/css">

<%if(menuUsed == MENU_ICON){%>
    <link href="stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
 <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
		  	<font color="#FF8080" face="Century Gothic"><big><strong><div align="center">System Information</div></strong></big></font>
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="form1" method="post" action="">
              <input type="hidden" name="ic">
              <table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%">
                <tr>                   
                  <td width="88%" valign="top" align="center">&nbsp;</td>
				</tr>
				<tr>                   
                  <td width="88%" valign="top" align="center" class="comment"> 
                    <% 
					switch(infCode) {
						case I_SystemInfo.DATA_LOCKED : 
							msgAccess  = I_SystemInfo.textInfo[infCode];
						break;
			
						case I_SystemInfo.HAVE_NOPRIV : 
							msgAccess  = I_SystemInfo.textInfo[infCode];
						break;
			
						case I_SystemInfo.NOT_LOGIN : 
							msgAccess  = I_SystemInfo.textInfo[infCode];
						break;
			
						default:
						%>                   
							<script language="javascript">
								window.location="<%= approot %>/index.html"
							</script>
						<%																
					}
					%>
                    <%=msgAccess%> 
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
