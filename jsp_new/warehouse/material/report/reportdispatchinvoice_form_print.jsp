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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER_REPORT, AppObjInfo.OBJ_TRANSFER_REPORT_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListPrintHeader[][] ={
    {"LAPORAN TRANSFER BARANG PER INVOICE"," s/d ","Lokasi Asal","Lokasi Tujuan","Mengetahui","Pengirim","Penerima","Semua Lokasi"},
    {"GOODS TRANSFER REPORT BY INVOICE"," to ","Source","Destination","Check by","Send by","Receive by","All Location"}
};
%>

<%
/**
 * set value vector for stock report print
 * biar tidak load data lagi
 */
SrcReportDispatch srcReportDispatch = new SrcReportDispatch();
Vector hasil = new Vector(1,1);
try {
    Vector vtSess = (Vector)session.getValue("SESS_MAT_REPORT_DISPATCH_INVOICE");
    srcReportDispatch = (SrcReportDispatch)vtSess.get(0);
    hasil = (Vector)vtSess.get(1);
} catch(Exception e){}
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
            <form name="frm_reportdispatch" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="approval_command">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="center">
                        <td width="25%">&nbsp;</td>
                        <td width="50%"><b><%=textListPrintHeader[SESS_LANGUAGE][0]%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                      <tr align="center">
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b><%=Formater.formatDate(srcReportDispatch.getDateFrom(), "dd-MM-yyyy")%> <%= textListPrintHeader[SESS_LANGUAGE][1] + Formater.formatDate(srcReportDispatch.getDateTo(), "dd-MM-yyyy")%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
		<%
                        Location lokasi = new Location();
                        if (srcReportDispatch.getLocationId() != 0) {
                            try{
                                lokasi = PstLocation.fetchExc(srcReportDispatch.getLocationId());
                            }catch(Exception exx){}
                        } else {
                            lokasi.setName(textListPrintHeader[SESS_LANGUAGE][7]);
                        }
                        
                        Location loc1 = new Location();
                        if (srcReportDispatch.getDispatchTo() != 0) {
                            try {
                                loc1 = PstLocation.fetchExc(srcReportDispatch.getDispatchTo());
                            }catch(Exception exx){}
                        } else {
                            loc1.setName(textListPrintHeader[SESS_LANGUAGE][7]);
                        }
                %>
                <tr>
                  <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td class="listgensell" valign="middle">&nbsp;<%= textListPrintHeader[SESS_LANGUAGE][2]%></td>
                        <td class="listgensell" valign="middle">:</td>
                        <td class="listgensell" valign="middle"><%=lokasi.getName().toUpperCase()%></td>
                      </tr>
                      <tr> 
                        <td width="14%" class="listgensell" valign="middle" > &nbsp;<%= textListPrintHeader[SESS_LANGUAGE][3]%></td>
                        <td width="1%" class="listgensell" valign="middle">: </td>
                        <td width="85%" class="listgensell" valign="middle"><%=loc1.getName().toUpperCase()%></td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="0">
                  <%
                  for (int i=0; i<hasil.size();i++) {
                            out.println((String)hasil.get(i));
                  }
                  %>
                  </td>
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
