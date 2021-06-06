<%-- 
    Document   : history_open_document
    Created on : Oct 12, 2015, 4:06:53 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpname"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatCosting"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReturn"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReturn"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%-- 
    Document   : historypo
    Created on : Nov 18, 2013, 11:32:00 AM
    Author     : yazt
--%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "../main/checkuser.jsp" %>

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
            if (oidLog == logSysHistory.getOID()) {
                index = i;
            }

            rowx.add(""+logSysHistory.getLogLoginName());
            rowx.add(""+logSysHistory.getLogUserAction());
            rowx.add(Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd hh:mm:ss"));
            rowx.add(""+logSysHistory.getLogApplication());
            rowx.add(""+logSysHistory.getLogDetail());

            lstData.add(rowx);
        }
        
       
        
        
         return ctrlist.draw(index);
    }
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
long oidDocHistory = FRMQueryString.requestLong(request, "oidDocHistory");
long oidDocHistoryPrice = FRMQueryString.requestLong(request, "oidDocHistoryPrice");
long oidDocHistoryLocation = FRMQueryString.requestLong(request, "oidDocHistoryLocation");
int oidSelectedDocument = FRMQueryString.requestInt(request, "index_document");
int status_document = FRMQueryString.requestInt(request, "status_document");

LogSysHistory logHistory = new LogSysHistory();
String msgString = "";
long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
Vector listHistoryPurchaseOrder = new Vector(1, 1);
Vector listHistoryPurchaseOrderPrice = new Vector(1, 1);
Vector listHistoryPurchaseOrderLocation = new Vector(1, 1);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 200;
PurchaseOrder purchaseOrder = new PurchaseOrder();
MatReceive matReceive = new MatReceive();
MatReturn matreturn = new MatReturn();
MatDispatch matDf = new MatDispatch();
MatCosting matCos = new MatCosting();
MatStockOpname matStockOpname = new MatStockOpname();
LogSysHistory logSysHistory = new LogSysHistory();
String numberDoc = "";
int status=0;
Date dateLog = new Date();
switch (oidSelectedDocument) {
    case 1://po
        if(oidDocHistory!=0){
            purchaseOrder = PstPurchaseOrder.fetchExc(oidDocHistory);
            numberDoc = purchaseOrder.getPoCode();
            status = purchaseOrder.getPoStatus();
            if(iCommand==Command.SAVE){
                purchaseOrder.setPoStatus(status_document);
                PstPurchaseOrder.updateExc(purchaseOrder);
                
                dateLog = new Date();
                logSysHistory.setLogUserId(userId);
                logSysHistory.setLogLoginName(userName);
                logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                logSysHistory.setLogApplication("Prochain");
                logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                logSysHistory.setLogUpdateDate(dateLog);
                logSysHistory.setLogUserAction(Command.commandString[5]);
                logSysHistory.setLogDocumentNumber(numberDoc);
                logSysHistory.setLogDocumentId(oidDocHistory);
                logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                
                status = purchaseOrder.getPoStatus();
            }
        }
        break;
    case 2://receive from purchase
        if(oidDocHistory!=0){
            matReceive = PstMatReceive.fetchExc(oidDocHistory);
            numberDoc = matReceive.getRecCode();
            status = matReceive.getReceiveStatus();
             if(iCommand==Command.SAVE){
                 matReceive.setReceiveStatus(status_document);
                 PstMatReceive.updateExc(matReceive);
                 
                 dateLog = new Date();
                 logSysHistory.setLogUserId(userId);
                 logSysHistory.setLogLoginName(userName);
                 logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                 logSysHistory.setLogApplication("Prochain");
                 logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                 logSysHistory.setLogUpdateDate(dateLog);
                 logSysHistory.setLogUserAction(Command.commandString[5]);
                 logSysHistory.setLogDocumentNumber(numberDoc);
                 logSysHistory.setLogDocumentId(oidDocHistory);
                 logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                 
                 status = matReceive.getReceiveStatus();
            }
        }
        break;
    case 3://receive from transfer 
        if(oidDocHistory!=0){
            matReceive = PstMatReceive.fetchExc(oidDocHistory);
            numberDoc = matReceive.getRecCode();
            status = matReceive.getReceiveStatus();
            if(iCommand==Command.SAVE){
                 matReceive.setReceiveStatus(status_document);
                 PstMatReceive.updateExc(matReceive);
                 
                 dateLog = new Date();
                 logSysHistory.setLogUserId(userId);
                 logSysHistory.setLogLoginName(userName);
                 logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                 logSysHistory.setLogApplication("Prochain");
                 logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                 logSysHistory.setLogUpdateDate(dateLog);
                 logSysHistory.setLogUserAction(Command.commandString[5]);
                 logSysHistory.setLogDocumentNumber(numberDoc);
                 logSysHistory.setLogDocumentId(oidDocHistory);
                 logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                 
                 status = matReceive.getReceiveStatus();
            }
        }
        break;
    case 5://return  
        if(oidDocHistory!=0){
            matreturn = PstMatReturn.fetchExc(oidDocHistory);
            numberDoc = matreturn.getRetCode();
            status = matreturn.getReturnStatus();
            if(iCommand==Command.SAVE){
                matreturn.setReturnStatus(status_document);
                PstMatReturn.updateExc(matreturn);
                
                 dateLog = new Date();
                 logSysHistory.setLogUserId(userId);
                 logSysHistory.setLogLoginName(userName);
                 logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                 logSysHistory.setLogApplication("Prochain");
                 logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                 logSysHistory.setLogUpdateDate(dateLog);
                 logSysHistory.setLogUserAction(Command.commandString[5]);
                 logSysHistory.setLogDocumentNumber(numberDoc);
                 logSysHistory.setLogDocumentId(oidDocHistory);
                 logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                
                status = matreturn.getReturnStatus();
            }
        }
        break;    
    case 6://transfer
        if(oidDocHistory!=0){
            matDf = PstMatDispatch.fetchExc(oidDocHistory);
            numberDoc = matDf.getDispatchCode();
            status = matDf.getDispatchStatus();
            if(iCommand==Command.SAVE){
                 matDf.setDispatchStatus(status_document);
                 PstMatDispatch.updateExc(matDf);
                 
                 dateLog = new Date();
                 logSysHistory.setLogUserId(userId);
                 logSysHistory.setLogLoginName(userName);
                 logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                 logSysHistory.setLogApplication("Prochain");
                 logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                 logSysHistory.setLogUpdateDate(dateLog);
                 logSysHistory.setLogUserAction(Command.commandString[5]);
                 logSysHistory.setLogDocumentNumber(numberDoc);
                 logSysHistory.setLogDocumentId(oidDocHistory);
                 logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                 
                 status = matDf.getDispatchStatus();
            }
        }
        break;
    case 7://costing
        if(oidDocHistory!=0){
            matCos = PstMatCosting.fetchExc(oidDocHistory);
            numberDoc = matCos.getCostingCode();
            status = matCos.getCostingStatus();
            if(iCommand==Command.SAVE){
                matCos.setCostingStatus(status_document);
                PstMatCosting.updateExc(matCos);
                
                 dateLog = new Date();
                 logSysHistory.setLogUserId(userId);
                 logSysHistory.setLogLoginName(userName);
                 logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                 logSysHistory.setLogApplication("Prochain");
                 logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                 logSysHistory.setLogUpdateDate(dateLog);
                 logSysHistory.setLogUserAction(Command.commandString[5]);
                 logSysHistory.setLogDocumentNumber(numberDoc);
                 logSysHistory.setLogDocumentId(oidDocHistory);
                 logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                 long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                
                status = matCos.getCostingStatus();
            }
        }
        break;   
    case 8://opname
         if(oidDocHistory!=0){
            matStockOpname = PstMatStockOpname.fetchExc(oidDocHistory);
            numberDoc = matStockOpname.getStockOpnameNumber();
            status = matStockOpname.getStockOpnameStatus();
            if(iCommand==Command.SAVE){
                matStockOpname.setStockOpnameStatus(status_document);
                PstMatStockOpname.updateExc(matStockOpname);
                
                dateLog = new Date();
                logSysHistory.setLogUserId(userId);
                logSysHistory.setLogLoginName(userName);
                logSysHistory.setLogDocumentType(I_DocType.documentTypeNames[0][1]);
                logSysHistory.setLogApplication("Prochain");
                logSysHistory.setLogOpenUrl("material/dispatch/costing_material_edit.jsp");
                logSysHistory.setLogUpdateDate(dateLog);
                logSysHistory.setLogUserAction(Command.commandString[5]);
                logSysHistory.setLogDocumentNumber(numberDoc);
                logSysHistory.setLogDocumentId(oidDocHistory);
                logSysHistory.setLogDetail("Update Status document "+I_DocStatus.fieldDocumentStatus[status]+" to be " +I_DocStatus.fieldDocumentStatus[status_document]);
                long oid2 = PstLogSysHistory.insertLog(logSysHistory);
                
                status = matStockOpname.getStockOpnameStatus();
            }
        }
        break;       
    default:
}


// update by Fitra 06-05-2014
if ( oidDocHistory != 0){
    String whereClauseHistory =PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "="+oidDocHistory;
    String orderClauseHistory = "" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " ASC ";
    listHistoryPurchaseOrder = PstLogSysHistory.listPurchaseOrder(start, recordToGet, whereClauseHistory, orderClauseHistory);   
}
%>

<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
    
function cmdSave(){
	document.frm_ordermaterial.command.value="<%=Command.SAVE%>";
	document.frm_ordermaterial.action="history_open_document.jsp";
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
                                <input type="hidden" name="index_document" value="<%=oidSelectedDocument%>">
                                <input type="hidden" name="oidDocHistory" value="<%=oidDocHistory%>">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="10%">No Document</td>
                                            <td width="2%">:</td>
                                            <td width="88%">
                                                <b><%=numberDoc%></b>
                                            </td>
                                        </tr> 
                                        
                                        <tr>
                                            <td width="10%">Status</td>
                                            <td width="2%">:</td>
                                            <td width="88%">
                                                <%
                                                    Vector obj_status = new Vector(1,1);
                                                    Vector val_status = new Vector(1,1);
                                                    Vector key_status = new Vector(1,1);

                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                    //add by fitra
                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CLOSED));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                    String select_status = ""+status;
                                                    if(status==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]+" (Document sudah terposting di Accounting, silahkan hapus jurnal terlebih dahulu)");
                                                    }else{
                                                        out.println(ControlCombo.draw("status_document",null,select_status,val_status,key_status,"tabindex=\"4\"","formElemen"));
                                                    }
                                                  %>
                                            </td>
                                        </tr> 
                                        <%if(status!=I_DocStatus.DOCUMENT_STATUS_POSTED){%>
                                            <tr>
                                               <td colspan="2"> 
                                                 <td width="90%">
                                                      <table width="47%" border="0" cellspacing="0" cellpadding="0">
                                                         <tr>
                                                           <td nowrap width="4%"><a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Save"></a></td>
                                                           <td class="command" nowrap width="50%"><a href="javascript:cmdSave()">Save</a></td>
                                                         </tr>
                                                   </table>
                                                 </td>
                                             </tr>  
                                         <%}%>
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