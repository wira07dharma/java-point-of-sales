<%-- 
    Document   : historypo
    Created on : Nov 18, 2013, 11:32:00 AM
    Author     : yazt
--%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "checkuser.jsp" %>

<!-- Jsp Block -->
<%!
    public static final int ADD_TYPE_LIST = 1;
    
    public String drawList(Vector objectClass   ,  long oidLog) {

       ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader("Login Name", "10%");
        ctrlist.addHeader("User Action", "10%");
        ctrlist.addHeader("Update Date", "10%");
        ctrlist.addHeader("Application", "10%");
        ctrlist.addHeader("Detail", "50%");
        ctrlist.setLinkRow(0);

        Vector lstData = ctrlist.getData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset();

        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
            LogSysHistory logSysHistory = (LogSysHistory) objectClass.get(i);
            Vector rowx = new Vector();
            if (logSysHistory.getLogDetail().indexOf("Type Harga") > -1){
                continue;
            }
            if (oidLog == logSysHistory.getOID()) {
                index = i;
            }

            rowx.add(""+logSysHistory.getLogLoginName());
            rowx.add(""+logSysHistory.getLogUserAction());
            rowx.add(Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd hh:mm:ss"));
            rowx.add(""+logSysHistory.getLogApplication());
            if (logSysHistory.getLogDocumentType().equals("HPP")){
                try {
                    JSONObject json = new JSONObject(logSysHistory.getLogDetail());
                    String log = " <b> Dokumen Penerimaan : "+logSysHistory.getLogDocumentNumber()+"</b>"
                                + "<br> Harga Beli Penerimaan : "+json.optString("HARGA_BELI","-")
                                +" <br> Qty Penerimaan : "+json.optString("QTY_PENERIMAAN","-")
                                +" <br> Hpp Awal : "+json.optString("HPP_AWAL","-")
                                +" <br> Qty Awal : "+json.optString("QTY_AWAL","-")
                                +" <br> Formula : "+json.optString("RUMUS_HPP","-")
                                +" <br> Hpp Sekarang : "+json.optString("HPP","-");
                    rowx.add(""+log);
                } catch (Exception exc){
                    rowx.add(""+logSysHistory.getLogDetail());
                }
            } else if (logSysHistory.getLogDocumentType().equals("HPP Return")){
                try {
                    JSONObject json = new JSONObject(logSysHistory.getLogDetail());
                    String log = " <b> Dokumen Return : "+logSysHistory.getLogDocumentNumber()+"</b>"
                                + "<br> Harga Beli Return : "+json.optString("HARGA_BELI","-")
                                +" <br> Qty Return : "+json.optString("QTY_RETURN","-")
                                +" <br> Hpp Awal : "+json.optString("HPP_AWAL","-")
                                +" <br> Qty Awal : "+json.optString("QTY_AWAL","-")
                                +" <br> Formula : "+json.optString("RUMUS_HPP","-")
                                +" <br> Hpp Sekarang : "+json.optString("HPP","-");
                    rowx.add(""+log);
                } catch (Exception exc){
                    rowx.add(""+logSysHistory.getLogDetail());
                }
            } else {
                rowx.add(""+logSysHistory.getLogDetail());
            }

            lstData.add(rowx);
        }
        
       
        
        
         return ctrlist.draw(index);
    }
%>

<%
int startItem = FRMQueryString.requestInt(request,"start_item");
long oidDocHistory = FRMQueryString.requestLong(request, "oidDocHistory");
long oidDocHistoryPrice = FRMQueryString.requestLong(request, "oidDocHistoryPrice");
long oidDocHistoryLocation = FRMQueryString.requestLong(request, "oidDocHistoryLocation");
LogSysHistory logHistory = new LogSysHistory();

String msgString = "";

long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
Vector listHistoryPurchaseOrder = new Vector(1, 1);
Vector listHistoryPurchaseOrderPrice = new Vector(1, 1);
Vector listHistoryPurchaseOrderLocation = new Vector(1, 1);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 200;




// update by Fitra 06-05-2014
if ( oidDocHistory != 0){
String whereClauseHistory =PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "="+oidDocHistory;
String orderClauseHistory = "" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " ASC ";
listHistoryPurchaseOrder = PstLogSysHistory.listPurchaseOrder(start, recordToGet, whereClauseHistory, orderClauseHistory);   
}

// add by Fitra 06-05-2014
if ( oidDocHistoryPrice != 0){
String whereClauseHistoryPrice =PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "="+oidDocHistoryPrice + " AND " +
                                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE 'Type Harga%'";
String orderClauseHistoryPrice = "" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " ASC ";
listHistoryPurchaseOrder = PstLogSysHistory.listPurchaseOrder(start, recordToGet, whereClauseHistoryPrice, orderClauseHistoryPrice);   

}




if ( oidDocHistoryLocation != 0){
String whereClauseHistoryLocation =PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "="+oidDocHistoryLocation+ " AND " + 
                                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE 'Location%'";
String orderClauseHistoryLocation = "" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " ASC ";
listHistoryPurchaseOrder= PstLogSysHistory.listPurchaseOrder(start, recordToGet, whereClauseHistoryLocation, orderClauseHistoryLocation);    
}

%>

<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
	document.frm_ordermaterial.start.value=0;
	document.frm_ordermaterial.approval_command.value="<%=Command.SAVE%>";
	document.frm_ordermaterial.command.value="<%=Command.ADD%>";
	document.frm_ordermaterial.add_type.value="<%=ADD_TYPE_LIST%>";
	document.frm_ordermaterial.action="pomaterial_edit.jsp";
	if(compareDateForAdd()==true)
		document.frm_ordermaterial.submit();
}

function cmdEdit(oid){
	document.frm_ordermaterial.hidden_material_order_id.value=oid;
	document.frm_ordermaterial.start.value=0;
	document.frm_ordermaterial.approval_command.value="<%=Command.APPROVE%>";
	document.frm_ordermaterial.command.value="<%=Command.EDIT%>";
	document.frm_ordermaterial.action="pomaterial_edit.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListFirst(){
	document.frm_ordermaterial.command.value="<%=Command.FIRST%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListPrev(){
	document.frm_ordermaterial.command.value="<%=Command.PREV%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListNext(){
	document.frm_ordermaterial.command.value="<%=Command.NEXT%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdListLast(){
	document.frm_ordermaterial.command.value="<%=Command.LAST%>";
	document.frm_ordermaterial.action="pomaterial_list.jsp";
	document.frm_ordermaterial.submit();
}

function cmdBack(){
	document.frm_ordermaterial.command.value="<%=Command.BACK%>";
	document.frm_ordermaterial.action="srcpomaterial.jsp";
	document.frm_ordermaterial.submit();
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
<!--
function hideObjectForMarketing(){
}

function hideObjectForWarehouse(){
}

function hideObjectForProduction(){
}

function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC">
        <%if(menuUsed == MENU_PER_TRANS){%>
          <tr>
            <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
              <%@ include file = "../main/header.jsp" %>
              <!-- #EndEditable --></td>
          </tr>
          <tr>
            <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
              <%@ include file = "../main/mnmain.jsp" %>
              <!-- #EndEditable --> </td>
          </tr>
          <%}else{%>
           <tr bgcolor="#FFFFFF">
            <td height="10" ID="MAINMENU">
              <%@include file="../styletemplate/template_header_empty.jsp" %>
            </td>
          </tr>
          <%}%>
        <tr>
            <td valign="top" align="left">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td height="20" class="mainheader">Document History</td>
                    </tr>
                    <tr>
                        <td><!-- #BeginEditable "content" -->
                            <form name="frm_ordermaterial" method="post" action="">
                                <input type="hidden" name="command" value="">
                                <input type="hidden" name="add_type" value="">
                                <input type="hidden" name="start" value="">
                                <input type="hidden" name="hidden_material_order_id" value="">
                                <input type="hidden" name="approval_command">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <%=
                                                drawList(listHistoryPurchaseOrder, oidLog)
                                                %>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="18" valign="top" colspan="3">
                                                <table width="44%" border="0" cellspacing="0" cellpadding="0">
                                                </table>
                                            </td>
                                        </tr>
                                  </table>
                            </form>
                            <!-- #EndEditable -->
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                 <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../styletemplate/footer.jsp" %>
                <%}else{%>
                    <%@ include file = "../main/footer.jsp" %>
                <%}%>
                <!-- #EndEditable --> 
            </td>
        </tr>
    </table>
</body>
<!-- #EndTemplate --></html>