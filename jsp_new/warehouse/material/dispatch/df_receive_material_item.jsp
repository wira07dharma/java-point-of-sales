<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.warehouse.*,
                   com.dimata.posbo.session.warehouse.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;


/*public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang","Cetak Penerimaan Barang"},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item","Print Goods Receive"}
};*/

public static final String textListGlobal[][] = {
    {"Tambah Item","Dari Penerimaan","Ke Transfer","Daftar","Item","Penerimaan","Proses transfer tidak dapat dilakukan pada lokasi yang sama"},
    {"Add Item", "From Receive","For Transfer","List","Item","Receive","There Transfer cant'be proceed in same location"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Waktu"},
    {"Number","From Location","Destination","Date","Status","Remark","Supplier Invoice","Time"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
    {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost"}
};

/** this constan used to list text of forwarder information */
public static final String textListForwarderInfo[][] = {
    {"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!","Sebelum Status Final Pastikan Informasi Forwarder diisi jika ada !"},
    {"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!","Prior to final status, make sure forwarder information is entered if required !"}
};


/**
* this method used to list all po item
*/
public Vector drawListRecItem(int language,Vector objectClass,int start,boolean privManageData){
    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0],"5%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"20%");
        if(bEnableExpiredDate){
        ctrlist.addHeader(textListOrderItem[language][3],"8%");
        }
        ctrlist.addHeader(textListOrderItem[language][4],"5%");
        ctrlist.addHeader(textListOrderItem[language][5],"8%");
        ctrlist.addHeader(textListOrderItem[language][6],"8%");
        //ctrlist.addHeader(textListOrderItem[language][7],"5%");
        ctrlist.addHeader(textListOrderItem[language][8],"9%");
        ctrlist.addHeader(textListOrderItem[language][9],"10%");

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0){
            start=0;
        }

        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            rowx = new Vector();
            start = start + 1;
            double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();

            rowx.add(""+start+"");
  
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            if(bEnableExpiredDate){
             rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
            //rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);

                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</div>");

            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">"+textListGlobal[language][7]+"</div>";
    }
    list.add(result);
    list.add(listError);
    return list;
}


%>


<!-- Jsp Block -->
<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidForwarderInfo = FRMQueryString.requestLong(request, "hidden_forwarder_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* purchasing ret code and title
*/
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];//"Terima Barang";//i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
MatDispatch df =new MatDispatch();
if(oidDispatchMaterial !=0){
try{
  df = PstMatDispatch.fetchExc(oidDispatchMaterial);
}
catch(Exception e){
  System.out.println(e);
}
}

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(iCommand , oidReceiveMaterial);
FrmMatReceive frmrec = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();
errMsg = ctrlMatReceive.getMessage();

/**
* generate code of current currency
*/
String priceCode = "Rp.";

/**
* check if document may modified or not
*/
boolean privManageData = true;

/**
* list purchase order item
*/
oidReceiveMaterial = rec.getOID();
int recordToGetItem = 30;
String whereClauseItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
String orderClauseItem = " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
Vector listMatReceiveItem = new Vector();

if(rec!=null && rec.getOID()!=0){
listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem,recordToGetItem,whereClauseItem, orderClauseItem);
}

/** get forwarder info */
Vector vctForwarderInfo = new Vector(1,1);
try {
    if(oidReceiveMaterial != 0) {
        vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceiveMaterial);
    }
} catch(Exception e) {
}

/** get total biaya forwarder */
double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceiveMaterial);

if(iCommand==Command.DELETE && iErrCode==0){
%>
	<jsp:forward page="receive_material_list.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function compare(){
    var dt = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
    var mn = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
    var yy = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
    var dt = new Date(yy,mn-1,dt);
    var bool = new Boolean(compareDate(dt));
    return bool;
}

function cmdSave() {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        var invNo = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
            if(invNo!=''){
                    document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
                    if(compare()==true)
                            document.frm_recmaterial.submit();
            }else{
                    alert("Nomor invoice supplier harus diisi");
        }
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function cmdAsk(oid) {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.ASK%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function gostock(oid) {
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.action="rec_wh_stockcode.jsp";
    document.frm_recmaterial.submit();
}

function cmdCancel() {
    document.frm_recmaterial.command.value="<%=Command.CANCEL%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oid) {
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
    document.frm_recmaterial.submit();
}
function cmdBack() {
    document.frm_recmaterial.command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="df_stock_material_receive_transfer_list.jsp";
    document.frm_recmaterial.submit();
}


//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem() {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.ADD%>";
        document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
        if(compareDateForAdd()==true)
            document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function editItem(oid) {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.hidden_receive_item_id.value=oid;
        document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}
function itemList(comm) {
    document.frm_recmaterial.command.value=comm;
    document.frm_recmaterial.prev_command.value=comm;
    document.frm_recmaterial.action="df_receive_material_item.jsp";
    document.frm_recmaterial.submit();
}

function addToTransfer(oid) {
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.action="df_stock_wh_material_edit.jsp";
    document.frm_recmaterial.submit();
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
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

</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
  
<%if(menuUsed == MENU_PER_TRANS){%>
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
          	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_recmaterial" method="post" action="">
            <%
            try {
            %>
              <input type="hidden" name="command" value="">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
			  <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
              <input type="hidden" name="hidden_receive_item_id" value="">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="3">&nbsp;
                    <hr size="1">
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="25%">:
                           <%=df.getDispatchCode()%>
                        </td>
                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="29%">:
                            <%
                             Location srcLoc = new Location();
                             try{
                                 srcLoc = PstLocation.fetchExc(df.getLocationId());


                                 }catch(Exception exc){
                                     srcLoc=new Location();
                                }
                            %>
                           <%=srcLoc.getName()%>

                        </td>
                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
						  <td width="21%">:
                          <%=df.getRemark()%>
                      </tr>
                        <tr>
                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                           <td width="25%">:
                             <%=(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"))%>
                          </td>
                       <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                           <td width="25%">:
                            <%
                            srcLoc = PstLocation.fetchExc(df.getDispatchTo());
                            %>
                            <%=srcLoc.getName()%>
                        </td>
                       <tr>
                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                           <td width="25%">:
                               <%=(Formater.formatDate(df.getDispatchDate(), "HH:mm"))%>
                           <td width ="8%"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                           <td width="25%">:
                               <%if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                               } else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                               }else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                }else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED)
                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);%>
                       </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                        <%
                            Vector list = drawListRecItem(SESS_LANGUAGE,listMatReceiveItem,startItem,privManageData);
                            out.println(""+list.get(0));
                            Vector listError = (Vector)list.get(1);
                        %>
                        </td>
                      </tr>
                      <%if(oidReceiveMaterial!=0){%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
                            if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST){
                                cmdItem = Command.FIRST;
                            }
                            ctrLine.setLocationImg(approot+"/images");
                            ctrLine.initDefault();
                            ctrLine.setImageListName(approot+"/images","item");

                            ctrLine.setListFirstCommand("javascript:itemList('"+Command.FIRST+"')");
                            ctrLine.setListNextCommand("javascript:itemList('"+Command.NEXT+"')");
                            ctrLine.setListPrevCommand("javascript:itemList('"+Command.PREV+"')");
                            ctrLine.setListLastCommand("javascript:itemList('"+Command.LAST+"')");
                          %>
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%>
                          </span>
                        </td>
                      </tr>
                      <% if(listError!=null && listError.size()>0)
                          {%>
                      <tr align="left" valign="top">
                        <td height="22" colspan="3" valign="middle" class="errfont">
                          <%
                            for(int k=0;k<listError.size();k++){
                              if(k==0)
                                  out.println(listError.get(k)+"<br>");
                              else
                                  out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                            }
			  %>
                        </td>
                      </tr><%}%>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                
                <% if(oidReceiveMaterial!=0 && listMatReceiveItem.size() > 0) { %>
                <tr>
                  <td colspan="2" valign="top">
                    <!--table width="100%" border="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3" class="mainheader"><%=textListForwarderInfo[SESS_LANGUAGE][5]%> :</td>
                      </tr>
                      <%
                        if(vctForwarderInfo.size() > 0) {
                            Vector temp = (Vector)vctForwarderInfo.get(0);
                            ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                            ContactList contactList = (ContactList)temp.get(1);
                      %>
                      <tr align="left" valign="top">
                        <td width="1%" height="22" valign="middle" colspan="1">&nbsp;</td>
                        <td width="99%" height="22" valign="middle" colspan="2">

                        </td>
                      </tr>
                      <% } else { %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3" class="comment">&nbsp;&nbsp;<%=textListForwarderInfo[SESS_LANGUAGE][6]%></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                          <table width="80%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                              <td width="6%"><a href="javascript:addForwarderInfo()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListForwarderInfo[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addForwarderInfo()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListForwarderInfo[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
                              <% } %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <% } %>
                    </table-->
                  </td>
                  <td width="30%" valign="top">
                    <table width="100%" border="0">
                      <%
                      String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                      double totalBeli = PstMatReceiveItem.getTotal(whereItem);
                      double ppn = rec.getTotalPpn();
                      double totalBeliWithPPN = (totalBeli * (rec.getTotalPpn() / 100)) + totalBeli;
                      %>
                    </table>
                  </td>
                </tr>
                <% } %>
                <tr>
                  <td valign="top" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0">
                      <tr>
                          <td nowrap width="6%"><a href="javascript:addToTransfer()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnNewOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnNewOn.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td class="command" width="20%"><a href="javascript:addToTransfer()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_ADD,true)%></a></td>
                          <td nowrap width="6%"><a href="javascript:cmdBack()()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBack.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_BACK,true)%>"></a></td>
                              <td class="command" width="20%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_BACK,true)%></a></td>
                        <td width="80%">
                          <%
                          //ctrLine.setLocationImg(approot+"/images");

                          // set image alternative caption
                          /*ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");
                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));
                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));
                          ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD, true));

                          ctrLine.initDefault();
                          ctrLine.setTableWidth("80%");
                          String scomDel = "javascript:cmdAsk('"+oidReceiveMaterial+"')";
                          String sconDelCom = "javascript:cmdConfirmDelete('"+oidReceiveMaterial+"')";
                          String scancel = "javascript:cmdEdit('"+oidReceiveMaterial+"')";
                          ctrLine.setCommandStyle("command");
                          ctrLine.setColCommStyle("command");

                          // set command caption
                          ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
                          ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");
                          ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));
                          ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_DELETE,true));
                          ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));
                          ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD, true));

                          if(privDelete && privManageData){
                              ctrLine.setConfirmDelCommand(sconDelCom);
                              ctrLine.setDeleteCommand(scomDel);
                              ctrLine.setEditCommand(scancel);
                          }else{
                              ctrLine.setConfirmDelCaption("");
                              ctrLine.setDeleteCaption("");
                              ctrLine.setEditCaption("");
                          }

                          if(privAdd==false && privUpdate==false){
                              ctrLine.setSaveCaption("");
                          }

                          if(privAdd==false){
                              ctrLine.setAddCaption("");
                          }

                          if(iCommand==Command.SAVE && frmrec.errorSize()==0){
                              iCommand=Command.EDIT;
                          }
			  %>
                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%></td>
                        <td width="20%">
                          <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>

                          <%}*/%>
                          
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            <%
            }
            catch(Exception e) {
                System.out.println(e);
            }
            %>
            </form>
            <script language="JavaScript">
                document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.focus();
            </script>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %> 
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
