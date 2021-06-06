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
<%@ page import = "com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT_BY_RECEIPT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListGlobal[][] = {
	{"Tidak ada data penerimaan barang", "LAPORAN PENERIMAAN BARANG DARI SUPLIER PER INVOICE", "Periode", "s/d", "Lokasi", "Mata Uang","Semua"},
	{"Receiving goods item not found", "GOODS RECEIVE FROM SUPPLIER REPORT BY INVOICE", "Period", "to", "Location", "Currency","All"}
};
%>

<%
SrcReportReceive srcReportReceive = new SrcReportReceive();
Vector hasil = new Vector(1,1);

try{
    Vector vtsess = (Vector)session.getValue("SESS_MAT_RECEIVE_REPORT_INVOICE");
    
    srcReportReceive = (SrcReportReceive)vtsess.get(0);
    hasil = (Vector)vtsess.get(1);
    
}catch(Exception e){
    System.out.println("ERR > "+e.toString());
}


CurrencyType objCurrencyType = new CurrencyType();
if(srcReportReceive.getCurrencyId() != 0) {
    try {
        objCurrencyType = PstCurrencyType.fetchExc(srcReportReceive.getCurrencyId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    objCurrencyType.setCode("-");
}
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
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">             
              <input type="hidden" name="approval_command">
				
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b><%=textListGlobal[SESS_LANGUAGE][1]%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b><%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%> <%= " s/d " + Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                    </table></td>
                </tr>
                <%
                        if (srcReportReceive.getLocationId() != 0) {
                            try {
                                Location lokasi = PstLocation.fetchExc(srcReportReceive.getLocationId());
                %>
                <tr align="left" valign="top"> 
                  <td class="listgensell" valign="middle" >&nbsp;<%=textListGlobal[SESS_LANGUAGE][4]%> : <%=lokasi.getName().toUpperCase()%></td>
                </tr>
                <%
                            } catch(Exception exx) {
                            }
                        }
                       else {
                %>
                
                <!-- adding all location by mirahu 20120504 -->
                <tr align="left" valign="top"> 
                  <td class="listgensell" valign="middle" >&nbsp;<%=textListGlobal[SESS_LANGUAGE][4]%> : <%=textListGlobal[SESS_LANGUAGE][6]%>&nbsp;<%=textListGlobal[SESS_LANGUAGE][4]%></td>
                </tr>
                <% } %>
                <tr align="left" valign="top"> 
                  <td class="listgensell" valign="middle" >&nbsp;<%=textListGlobal[SESS_LANGUAGE][5]%> : <%=objCurrencyType.getCode()%></td>
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
                <tr>
                    <td valign="top" colspan="3">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                        <tr align="left" valign="top"> 
                          <td height="22" valign="middle" colspan="4"></td>
                        </tr>
                        <tr align="left" valign="top"> 
                          <td height="40" valign="middle" colspan="4"></td>
                        </tr>
                        <tr> 
                          <td width="25%" align="left" nowrap>EDP/INVENTORY</td>  
                          <td width="25%" align="left" nowrap></td>
                          <td align="25" valign="left" nowrap></td>
                          <td width="25%" align="left" nowrap>OWNER</td>
                        </tr>
                        <tr align="left" valign="top"> 
                          <td height="75" valign="middle" colspan="4"></td>
                        </tr>
                        <tr> 
                          <td width="25%" align="left" nowrap> (.................................) 
                          </td>  
                          <td width="25%" align="left" nowrap>
                          </td>
                          <td align="25%" valign="left" nowrap>
                          </td>
                          <td width="25%" align="left" nowrap> (.................................) 
                          </td>
                        </tr>
                         <tr align="left" valign="top"> 
                          <td height="22" valign="middle" colspan="4"></td>
                        </tr>
                        </table>

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
