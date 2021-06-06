<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN_REPORT , AppObjInfo.OBJ_SUMMARY_SUPPLIER_RETURN_REPORT_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
%>
<%
/**
	set value vector for stock report print
	biar tidak load data lagi
*/
SrcReportReturn srcReportReturn = new SrcReportReturn();
Vector hasil = new Vector(1,1);

try
{
	Vector vtSess = (Vector)session.getValue("SESS_MAT_RETURN_REPORT_INVOICE_REKAP");
	
	srcReportReturn = (SrcReportReturn)vtSess.get(0);
	hasil = (Vector)vtSess.get(1);
	
}
catch(Exception e){}


%>
<!-- End of Jsp Block -->
<html>
<!-- #BeginTemplate "/Templates/print.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../../../styles/print.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportreturn" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="approval_command">
				
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b>LAPORAN REKAP RETUR 
                          KE SUPPLIER PER INVOICE</b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b><%=Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")%> <%= " s/d " + Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy")%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                    </table></td>
                </tr>
                <%
					Location lokasi = new Location();
					if (srcReportReturn.getLocationId() != 0)
					{
						try
						{
							lokasi = PstLocation.fetchExc(srcReportReturn.getLocationId());
						%>
                <%
							}
							catch(Exception exx)
							{
							}
						}
					%>
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="7%" class="listgensell" valign="middle" > <b>&nbsp;LOKASI</b></td>
                        <td width="1%" class="listgensell" valign="middle"> <b>:</b> 
                        </td>
                        <td width="92%" class="listgensell" valign="middle"> <b><%=lokasi.getName().toUpperCase()%> </b> </td>
                      </tr>
                    </table></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td colspan="0" align="left" valign="middle"> <%
						  	for (int i=0; i<hasil.size();i++)
						  	{
						  		out.println((String)hasil.get(i));
							}
						%> </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>
