<%@ page language="java" %>

<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.session.masterdata.SessClosing" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %> 
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN, AppObjInfo.G2_ADMIN_SYSTEM, AppObjInfo.OBJ_ADMIN_SYSTEM_CLOSING_PERIOD); %>
<%@ include file = "../../main/checkuser.jsp" %>




<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] =
{
	{"PROSES TUTUP PERIODE GAGAL !!!", "Daftar dokumen yang gagal di-posting :", "Dokumen Penerimaan", "Dokumen Retur", "Dokumen Transfer", "Dokumen Penjualan"},
	{"CLOSING PERIOD PROCESS FAIL !!!", "List of un posted document :", "Receiving Document", "Returning Document", "Dispatching Document", "Sales Document"}
};
%>

<%
Vector vSessionDate = new Vector(1,1);
try
{
	vSessionDate = (Vector) session.getValue("MONTHLY_CLOSING");
}
catch(Exception e)
{
	System.out.println("Exc when manage SESSION : " + e.toString());
}

SessClosing objSessClosing = new SessClosing();
int iClosingStatus = objSessClosing.CLOSING_OK;
String sClosingMessage = "";
Vector vUnPostedLGRDoc = new Vector(1,1);
Vector vUnPostedReturnDoc = new Vector(1,1);
Vector vUnPostedDFDoc = new Vector(1,1);
Vector vUnPostedSalesDoc = new Vector(1,1);
if(vSessionDate!=null && vSessionDate.size()>5)
{
	iClosingStatus = Integer.parseInt(String.valueOf(vSessionDate.get(0)));
	sClosingMessage = String.valueOf(vSessionDate.get(1));
	vUnPostedLGRDoc = (Vector) vSessionDate.get(2);
	vUnPostedReturnDoc = (Vector) vSessionDate.get(3);
	vUnPostedDFDoc = (Vector) vSessionDate.get(4);
	vUnPostedSalesDoc = (Vector) vSessionDate.get(5);				
}
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "../../Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
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
      <%@include file="../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <table width="100%" border="0">
              <tr> 
                <td align="center" colspan="4">&nbsp;</td>
              </tr>
              <tr> 
                <td align="center" colspan="4"><font size="4"><%=textListTitleHeader[SESS_LANGUAGE][0]%></font></td>
              </tr>
              <tr align="center"> 
                <td colspan="4">&nbsp;</td>
              </tr>
              <tr align="center"> 
                <td colspan="4"><b><%=sClosingMessage%></b></td>
              </tr>
			  
			  <%
			  if(iClosingStatus == objSessClosing.ERR_UNPOSTED_DOC)
			  {
			  %>
              <tr align="center"> 
                <td colspan="4"> 
                  <table width="100%" border="0">
                    <%
				    if(vUnPostedLGRDoc!=null && vUnPostedLGRDoc.size()>0)
				    {
					  //String strUnPostedLGRDoc = "<a href=\"#\">" + (textListTitleHeader[SESS_LANGUAGE][2]) + "</a>" + " (" + vUnPostedLGRDoc.size() + ")";
					  String strUnPostedLGRDoc = (textListTitleHeader[SESS_LANGUAGE][2]) + " (" + vUnPostedLGRDoc.size() + ")";				
				    %>
                    <tr> 
                      <td align="center"><%=strUnPostedLGRDoc%></td>
                    </tr>
                    <%
				    }

				    if(vUnPostedReturnDoc!=null && vUnPostedReturnDoc.size()>0)
				    {
					  //String strUnPostedReturnDoc = "<a href=\"#\">" + (textListTitleHeader[SESS_LANGUAGE][3]) + "</a>" + " (" + vUnPostedReturnDoc.size() + ")";
					  String strUnPostedReturnDoc = (textListTitleHeader[SESS_LANGUAGE][3]) + " (" + vUnPostedReturnDoc.size() + ")";				
				    %>
                    <tr> 
                      <td align="center"><%=strUnPostedReturnDoc%></td>
                    </tr>
                    <%
				    }

				    if(vUnPostedDFDoc!=null && vUnPostedDFDoc.size()>0)
				    {
					  //String strUnPostedDFDoc = "<a href=\"#\">" + (textListTitleHeader[SESS_LANGUAGE][4]) + "</a>" + " (" + vUnPostedDFDoc.size() + ")";
					  String strUnPostedDFDoc = (textListTitleHeader[SESS_LANGUAGE][4]) + " (" + vUnPostedDFDoc.size() + ")";				
				    %>
                    <tr> 
                      <td align="center"><%=strUnPostedDFDoc%></td>
                    </tr>
                    <%
				    }

				    if(vUnPostedSalesDoc!=null && vUnPostedSalesDoc.size()>0)
				    {
					  //String strUnPostedSalesDoc = "<a href=\"#\">" + (textListTitleHeader[SESS_LANGUAGE][5]) + "</a>" + " (" + vUnPostedSalesDoc.size() + ")";
					  String strUnPostedSalesDoc = (textListTitleHeader[SESS_LANGUAGE][5]) + " (" + vUnPostedSalesDoc.size() + ")";				
				    %>
                    <tr> 
                      <td align="center"><%=strUnPostedSalesDoc%></td>
                    </tr>
                    <%
				    }
				    %>
                  </table>
                </td>
              </tr>
			  <%
			  }
			  %>
              <tr align="center"> 
                <td colspan="4">&nbsp;</td>
              </tr>
            </table>
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
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
