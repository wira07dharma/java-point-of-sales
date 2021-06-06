<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.warehouse.*,
                   com.dimata.posbo.session.warehouse.*,
                   com.dimata.posbo.entity.arap.*,
                   com.dimata.posbo.form.arap.*" %>
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


public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang","Cetak Penerimaan Barang"},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item","Print Goods Receive"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nota Supplier","Ppn (%)","Waktu","Mata Uang","Sub Total","Grand Total"},
    {"Number","Location","Date","Supplier","Status","Remark","Supplier Invoice","VAT","Time","Currency","Sub Total","Grand Total"}
};

/* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
    //{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %"}
//};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",
    "Diskon1 %","Diskon2 %","Discount Nominal"},
   {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
    "Discount2 %","Disc. Nominal"}
};

/** this constan used to list text of forwarder information */
public static final String textListForwarderInfo[][] = {
    {"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!","Sebelum Status Final Pastikan Informasi Forwarder diisi jika ada !"},
    {"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!","Prior to final status, make sure forwarder information is entered if required !"}
};

/** This constan used to list text of payment terms information */
public static final String textListPaymentTerms[][] = {
    {"Nomor", "Tanggal", "Sistem Pembayaran", "Mata Uang", "Jumlah","Rate","Catatan","Jadwal Pembayaran", "Tidak Ada Jadwal Pembayaran!"},
    {"Number", "Date", "Payment System", "Currency","Amount","Rate","Note", "Payment Terms", "No Payment Terms!","Prior to final status"}
};
/** End of List TextPaymentTerms


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
        //ctrlist.addHeader(textListOrderItem[language][10],"5%");
        ctrlist.addHeader(textListOrderItem[language][11],"5%");
        ctrlist.addHeader(textListOrderItem[language][12],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"8%");
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
            if(privManageData) {
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(mat.getSku());
            }
            
            rowx.add(mat.getName());
            if(bEnableExpiredDate){
             rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
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
int recordToGetItem = 10;
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

/** get List PaymentTerms */
Vector vctPaymentTerms = new Vector(1,1);
String whereClausePayTerms = " TERMS." +PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_RECEIVE_MATERIAL_ID]+
                     " = "+oidReceiveMaterial;
String ordPaymentTerms = " TERMS." +PstPaymentTerms.fieldNames[PstPaymentTerms.FLD_DUE_DATE];
vctPaymentTerms = PstPaymentTerms.listPaymentTerms(0, recordToGetItem, whereClausePayTerms, ordPaymentTerms);
/** end of List PaymentTerms */

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

var windowSupplier;
function addSupplier(){
    windowSupplier=window.open("../../../master/contact/contact_company_edit.jsp?command=<%=Command.ADD%>&source_link=receive_wh_supp_material_edit.jsp","add_supplier", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { windowSupplier.focus();}
}

function  setSupplierOnLGR(textIn, supOID){
    var oOption = self.opener.document.createElement("OPTION");
    oOption.text=textIn;
    oOption.value="supOID";
    document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID.add(oOption);
    document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID.value = "504404432982825708";
    changeFocus(self.opener.document.forms.frm_recmaterial.FRM_FIELD_SUPPLIER_ID);
}





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
    document.frm_recmaterial.action="receive_material_list.jsp";
    document.frm_recmaterial.submit();
}

function printForm() {
	var typePrint = document.frm_recmaterial.type_print_tranfer.value;
    window.open("receive_wh_supp_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function addForwarderInfo() {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.action="forwarder_info.jsp";
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

/* Fungsi add Payment Terms
 * By Mirahu (16 Februari 2011)
 */

function addPaymentTerms(oidReceiveMaterial) {
    <%
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
        var strvalue  = "<%=approot%>/warehouse/material/receive/payment_terms.jsp?command=<%=Command.ADD%>"+
                        "&hidden_receive_id="+oidReceiveMaterial;

    winSrcMaterial = window.open(strvalue,"receivepaymentterms", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function editForwarderInfo(){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.action="forwarder_info.jsp";
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
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
          	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][6]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
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
			  <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
              <input type="hidden" name="hidden_receive_item_id" value="">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="3">&nbsp;
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td width="5%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="25%" align="left">: <b><%=rec.getRecCode()%></b></td>
                        <td width="9%"> <%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="32%" valign="bottom"> :
                          <%
                            Vector val_supplier = new Vector(1,1);
                            Vector key_supplier = new Vector(1,1);
                            String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                             " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                             " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                             " != "+PstContactList.DELETE;
                            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                            
                            for(int d=0; d<vt_supp.size(); d++) {
                                ContactList cnt = (ContactList)vt_supp.get(d);
                                String cntName = cnt.getCompName();
                                if(cntName.length()==0){
                                    cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                }
                                val_supplier.add(String.valueOf(cnt.getOID()));
                                key_supplier.add(cntName);
                            }
                            String select_supplier = ""+rec.getSupplierId();
                          %>
                          <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"tabindex=\"2\"","formElemen")%> 
                          <a href="javascript:addSupplier();">Add New</a>
                        </td>
                        <td width="10%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="19%" rowspan="4" align="right" valign="top"><textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2" tabindex="6"><%=rec.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td width="5%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="25%" align="left"> :
                          <%
                            Vector obj_locationid = new Vector(1,1);
                            Vector val_locationid = new Vector(1,1);
                            Vector key_locationid = new Vector(1,1);
                            String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                            whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                            Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                            
                            for(int d=0;d<vt_loc.size();d++){
                                Location loc = (Location)vt_loc.get(d);
                                val_locationid.add(""+loc.getOID()+"");
                                key_locationid.add(loc.getName());
                            }
                            String select_locationid = ""+rec.getLocationId(); //selected on combo box
                          %>
                          <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "tabindex=\"1\"", "formElemen")%>
                        </td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>:
                          <input type="text" class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" tabindex="3">
                          
                        </td>
                      <td width="10%" align="right">                        </tr>
                      <tr>
                        <td width="5%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="25%" align="left">: <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date() : rec.getReceiveDate(), 1, -5, "formElemen", "")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td>:
                        <% 
                          Vector obj_status = new Vector(1,1);
                          Vector val_status = new Vector(1,1);
                          Vector key_status = new Vector(1,1);
                          
                          val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                          key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
						  
						  //add by fitra
						  val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                          if(listMatReceiveItem.size()>0){
                              val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                              key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                          }
                          String select_status = ""+rec.getReceiveStatus();
                          if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                              out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                          }else if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                              out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                          }else{
                              out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"tabindex=\"4\"","formElemen"));
                          }
                        %>
                        &nbsp;<div>
                                        <% if (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){%>
                                       <%=textListForwarderInfo[SESS_LANGUAGE][7]%>
                                       <%}%></div>
                        </td>
                        <td width="10%" align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="5%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                        <td width="25%" align="left">: <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date(): rec.getReceiveDate(), "formElemen")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                        <td>:
                          <%
                            Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                            Vector vectCurrVal = new Vector(1,1);
                            Vector vectCurrKey = new Vector(1,1);
                            for(int i=0; i<listCurr.size(); i++){
                                CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                vectCurrKey.add(currencyType.getCode());
                                vectCurrVal.add(""+currencyType.getOID());
                            }
                            if((vectSizeItem == 0) || (rec.getCurrencyId()==0) ) {
                                out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "tabindex=\"5\""));
                            }
                            else {
                               out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]+"_sel","formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled"));
                               out.println("<input type=\"hidden\" name=\""+FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]+"\" value=\""+rec.getCurrencyId()+"\">");
                            }
                          %>
                         </td>
                        <td width="10%" align="right"></td>
                      </tr>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="right"></td>
                        <td align="right" valign="top">
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
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                              <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                              <% } %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top" colspan="3">&nbsp;</td>
                </tr>
                <!-- List of Payment Terms -->
                 <% if(oidReceiveMaterial!=0 || iCommand == Command.SAVE) { %>
                <tr>
                  <td colspan="2" valign="top">
                    <table width="100%" border="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3" class="mainheader"><%=textListPaymentTerms[SESS_LANGUAGE][7]%> :</td>
                      </tr>
                       <%
                        if(vctPaymentTerms!=null && vctPaymentTerms.size() > 0) {
                       %>
                       <tr align="left" valign="top">
                        <td width="1%" height="22" valign="middle" colspan="1">&nbsp;</td>
                        <td width="99%" height="22" valign="middle" colspan="2">
                          <table width="80%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <td height="22" width="5%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][0]%></td>
                              <td height="22" width="15%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][1]%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][2]%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][3]%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][5]%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][4]%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgentitle"><%=textListPaymentTerms[SESS_LANGUAGE][6]%></td>
                            </tr>
                          <%
                          for(int k=0;k<vctPaymentTerms.size();k++){
                              PaymentTerms paymentTerms = (PaymentTerms)vctPaymentTerms.get(k);

                               PaymentSystem paymentSystem = new PaymentSystem();
                                try {
                                        paymentSystem = PstPaymentSystem.fetchExc(paymentTerms.getPaymentSystemId());
                                }
                                catch(Exception e) {
                                    System.out.println("Payment System not found ...");
                                }

                               CurrencyType currencyType = new CurrencyType();
                                try {
                                        currencyType = PstCurrencyType.fetchExc(paymentTerms.getCurrencyTypeId());
                                }
                                catch(Exception e) {
                                    System.out.println("Currency Type not found ...");
                                }


                            //Vector temps = (Vector)vctPaymentTerms.get(0);
                            //PaymentTerms paymentTerms = (PaymentTerms)temps.get(0);
                            //PaymentSystem paymentSystem = (PaymentSystem)temps.get(1);
                            //CurrencyType currencyType = (CurrencyType)temps.get(2);

                            
                          %>
                            <tr>
                              <td height="22" width="5%" align="center" nowrap class="listgensell">1</td>
                              <td height="22" width="15%" align="center" nowrap class="listgensell"><%=Formater.formatDate(paymentTerms.getDueDate(),"dd-MM-yyyy")%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgensell"><%=paymentSystem.getPaymentSystem()%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgensell"><%=currencyType.getCode()%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgensell"><%=paymentTerms.getRate()%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgensell"><%=paymentTerms.getAmount()%></td>
                              <td height="22" width="10%" align="center" nowrap class="listgensell"><%=paymentTerms.getNote()%></td>
                            </tr>
                          <% }%>
                          </table>
                        </td>
                      </tr>

                       
                       <% } else { %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3" class="comment">&nbsp;&nbsp;<%=textListPaymentTerms[SESS_LANGUAGE][8]%></td>
                      </tr>
                       <% } %>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                          <table width="80%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                              <td width="6%"><a href="javascript:addPaymentTerms('<%=oidReceiveMaterial%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListPaymentTerms[SESS_LANGUAGE][7],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addPaymentTerms('<%=oidReceiveMaterial%>')"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListPaymentTerms[SESS_LANGUAGE][7],ctrLine.CMD_ADD,true)%></a></td>
                              <% } %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <% } %>
                <tr>
                  <td valign="top" colspan="3">&nbsp;</td>
                </tr>
                <!-- End of Payment Terms -->

                <% if(oidReceiveMaterial!=0 || listMatReceiveItem.size() > 0) { %>
                <tr>
                  <td colspan="2" valign="top">
                    <table width="100%" border="0">
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
                          <table width="80%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][0]%></td>
                              <td height="22" width="2%">:</td>
                              <td height="22" width="68%">
                                <a href="javascript:editForwarderInfo()"><%=forwarderInfo.getDocNumber()%></a>
                              </td>
                            </tr>
                            <tr>
                              <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][1]%></td>
                              <td height="22" width="2%">:</td>
                              <td height="22" width="68%">
                                <%=ControlCombo.draw(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"disabled=\"true\"","formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][2]%></td>
                              <td height="22" width="2%">:</td>
                              <td height="22" width="68%">
                                <%=ControlDate.drawDateWithStyle(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE], (forwarderInfo.getDocDate()==null) ? new Date() : forwarderInfo.getDocDate(), 0, -1, "formElemen", "disabled=\"true\"")%>
                              </td>
                            </tr>
                            <tr>
                              <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][3]%></td>
                              <td height="22" width="2%">:</td>
                              <td height="22" width="68%">
                                <strong><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></strong>
                              </td>
                            </tr>
                            <tr>
                              <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][4]%></td>
                              <td height="22" width="2%">:</td>
                              <td height="22" width="68%">
                                <textarea name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]%>" class="formElemen" wrap="VIRTUAL" rows="2" cols="27" disabled="true"><%=forwarderInfo.getNotes()%></textarea>
                              </td>
                            </tr>
                          </table>
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
                    </table>
                  </td>
                  <td width="30%" valign="top">
                    <table width="100%" border="0">
                      <%
                      String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                      double totalBeli = PstMatReceiveItem.getTotal(whereItem);
                      double ppn = rec.getTotalPpn();
                      double totalBeliWithPPN = (totalBeli * (rec.getTotalPpn() / 100)) + totalBeli;
                      %>
                      <tr> 
                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%> <%=textListOrderItem[SESS_LANGUAGE][5]%></div></td>
                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b></div></td>
                      </tr>
                      <tr> 
                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][7]%></div></td>
                        <td width="44%">
                          <div align="right">
                            <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%=FRMHandler.userFormatStringDecimal(ppn)%>"  size="15" style="text-align:right">
                          </div>
                        </td>
                      </tr>
                      <tr> 
                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%></div></td>
                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%></b></div></td>
                      </tr>
                      <tr> 
                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%> <%=textListOrderItem[SESS_LANGUAGE][6]%></div></td>
                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></b></div></td>
                      </tr>
                      <tr> 
                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][11]%></div></td>
                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)%></b></div></td>
                      </tr>
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
                        <td>&nbsp;</td>
                        <td><select name="type_print_tranfer">
                          <option value="0">Price Cost</option>
                          <option value="1">Sell Price</option>
                        </select></td>
                      </tr>
                      <tr>
                        <td width="80%">
                          <%
                          ctrLine.setLocationImg(approot+"/images");
                          
                          // set image alternative caption
                          ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");
                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));
                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));
                          
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
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="5%" valign="top"><a href="javascript:printForm('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][8]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidReceiveMaterial%>')" class="command" > <%=textListGlobal[SESS_LANGUAGE][8]%> </a></td>
                            </tr>
                          </table>
                          <%}%>                        </td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
