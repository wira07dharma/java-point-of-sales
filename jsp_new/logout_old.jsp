<%@ page language="java" %>
<%@ include file = "main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<!--%@ include file = "main/checkuser.jsp" %-->


<%!
public static final String textListTitleHeader[][] = {
	{"EXIT","LOGIN"},
	{"EXIT","LOGIN"}
};
%>                                                   

<%
try{
	if(userSession.isLoggedIn()==true){
		userSession.printAppUser();
		userSession.doLogout(); 
		session.removeValue(SessUserSession.HTTP_SESSION_NAME);
		session.removeValue("APPLICATION_LANGUAGE");
	}
        if(userSession!=null){
            session.removeValue(SessUserSession.HTTP_SESSION_SALES);
        }
}catch (Exception exc){
  System.out.println(" ==> Exception during logout user");
}

/** cek integrasi */
int INTEGRASI_POS = 0;
int INTEGRASI_HANOMAN = 1;

int pos_integ = 0;
try {
	String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
	pos_integ = Integer.parseInt(designMat);
}
catch (Exception e) {
	pos_integ = 0;
}


int login_type = 0;
try {
	String designMat = String.valueOf(PstSystemProperty.getValueByName("PROCHAIN_LOGIN_TYPE"));
	login_type = Integer.parseInt(designMat);
}
catch (Exception e) {
	login_type = 0;
}

%>

<html>
<head>
<title>Dimata - ProChain POS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="styles/default.css" type="text/css">
<script language="JavaScript">
	function closeWindow(){
		window.close();
	}
</script>
</head>
<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<table width="100%" border="0" cellspacing="0" cellpadding="1" height="100%" align="center">
  <tr>       
    <td align="center">  
      <table width="40%" cellspacing="2" cellpadding="2" align="center">
        <tr> 
          <td width="68%" align="center">&nbsp;</td> 
        </tr>
        <tr>
          <td width="68%" height="20"> 
            <% if(pos_integ == INTEGRASI_POS) { %>
			<div align="center">
              <p>&nbsp;</p>
              <p>
				  <span class="biglogintitle"><font size="5"><b><font color="#0000CC" size="6">DIMATA&reg;</font></b></font></span>
				  <font size="6" color="#0000CC"><b>PROCHAIN</b></font><br>
				  <span class="smalllogintitle"><font color="#0000CC" size="2">- RETAIL BUSINESS MANAGEMENT SYSTEM -</font></span>
			  </p>
              <p>&nbsp;</p>
            </div>
			<% } else if(pos_integ == INTEGRASI_HANOMAN) { %>
			<div align="center">
              <p>&nbsp;</p>
              <p>
				  <span class="biglogintitle"><font size="5"><b><font color="#0000CC" size="6">HANOMAN</font></b></font></span> <br>
				  <span class="smalllogintitle"><font color="#0000CC" size="2">- PURCHASING &amp; INVENTORY SYSTEM -</font></span>
			  </p>
              <p>&nbsp;</p>
            </div>
			<% } %>
		  </td>
        </tr>
        <tr> 
          <td width="68%" align="center">&nbsp;</td>
        </tr>
        <tr> 
          <td width="68%" height="40" align="center"> <a href="javascript:closeWindow()">EXIT</a> | 
                <%if(login_type==1){%>
                    <a href="login_finger.jsp">LOGIN </a>
                <%}else{%>
                        <a href="login_new.jsp">LOGIN </a>
                <%}%>
             
          </td>
        </tr>
      </table>
    </td> 
  </tr>
</table>
</body>
</html>
