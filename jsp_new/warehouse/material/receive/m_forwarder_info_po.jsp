<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.warehouse.*,
                   com.dimata.posbo.session.warehouse.*,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder" %>
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
public static final String textListGlobal[][] = {
	{"Tidak Item Terima!","Grand Total","Sub Total"},
	{"No Receive Item!","Grand Total","Sub Total"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor PO","Nota Supplier","Ppn (%)","Waktu","Sub Total","Mata Uang"},
	{"Number","Location","Date","Supplier","Status","Remark","PO Number","Supplier Invoice","VAT","Time","Sub Total","Currency"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
	{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost"}
};

/** this constan used to list text of forwarder information */
public static final String textListForwarderInfo[][] = {
	{"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!"},
	{"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!"}
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
		ctrlist.addHeader(textListOrderItem[language][3],"8%");
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
			if(privManageData)
			{
				rowx.add("<a href=\"javascript:editItem('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
			}else{
				rowx.add(mat.getSku());
			}

			rowx.add(mat.getName());
			rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
			rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");

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
			rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</div>");

			lstData.add(rowx);
		}
		result = ctrlist.drawBootstrap();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item terima...</div>";
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
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(Command.EDIT , oidReceiveMaterial);
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
* list material receive item
*/
oidReceiveMaterial = rec.getOID();
int recordToGetItem = 20;
String whereClauseItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
Vector listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);

//Fetch Purchase Order Info
PurchaseOrder po = new PurchaseOrder();
try {
	po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
}
catch(Exception e) {
}

/** get forwarder info */
Vector vctForwarderInfo = new Vector(1,1);
try {
	if(oidReceiveMaterial != 0) {
		vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceiveMaterial);
	}
}
catch(Exception e) {
}

Vector temp = new Vector(1,1);
ForwarderInfo forwarderInfo = new ForwarderInfo();
ContactList contactList = new ContactList();
if(vctForwarderInfo.size() > 0) {
	temp = (Vector)vctForwarderInfo.get(0);
	forwarderInfo = (ForwarderInfo)temp.get(0);
	contactList = (ContactList)temp.get(1);
	oidForwarderInfo = forwarderInfo.getOID();
}
/** end get forwarder info */

/** action untuk forwarder info*/
CtrlForwarderInfo ctrlForwarderInfo = new CtrlForwarderInfo(request);
iErrCode = ctrlForwarderInfo.action(iCommand , oidForwarderInfo);
FrmForwarderInfo frmForwarderInfo = ctrlForwarderInfo.getForm();
errMsg = ctrlForwarderInfo.getMessage();
ForwarderInfo fi = ctrlForwarderInfo.getForwarderInfo();
oidForwarderInfo = fi.getOID();

/** get total biaya forwarder */
double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceiveMaterial);


if((iCommand == Command.SAVE) || (iCommand==Command.DELETE && iErrCode==0)) {
%>
	<jsp:forward page="m_receive_wh_supp_po_material_edit.jsp">
	<jsp:param name="command" value="<%=Command.EDIT%>"/>
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
	document.frm_recmaterial.action="m_receive_wh_supp_material_edit.jsp";
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

function gostock(oid) {
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.action="rec_wh_stockcode.jsp";
    document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function editItem(oid) {
	<%
	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
	%>
		document.frm_recmaterial.command.value="<%=Command.EDIT%>";
		document.frm_recmaterial.hidden_receive_item_id.value=oid;
		document.frm_recmaterial.action="m_receive_wh_supp_materialitem.jsp";
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
	document.frm_recmaterial.action="m_receive_wh_supp_materialitem.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//-------------------------- START JAVASCRIPT FUNCTION FOR SHIPPING INFO -----------------------
function cmdSave() {
	document.frm_recmaterial.command.value="<%=Command.SAVE%>";
	document.frm_recmaterial.action="m_forwarder_info_po.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function cmdAsk(oid) {
	<%
	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
	%>
		document.frm_recmaterial.command.value="<%=Command.ASK%>";
		document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
		document.frm_recmaterial.action="m_forwarder_info_po.jsp";
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

function cmdConfirmDelete(oid){
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.action="m_forwarder_info_po.jsp";
	document.frm_recmaterial.submit();
}

function cmdCancel() {
	document.frm_recmaterial.command.value="<%=Command.CANCEL%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="m_receive_wh_supp_material_edit.jsp";
	document.frm_recmaterial.submit();
}
//-------------------------- END JAVASCRIPT FUNCTION FOR SHIPPING INFO ------------------------


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


<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
        

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

<body class="skin-blue">
        <%@ include file = "../../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>   Gudang &gt; Terima Barang &gt; Tanpa PO<!-- #EndEditable --></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frm_recmaterial" method="post" action="">
                     <input type="hidden" name="command" value="">
                     <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                     <input type="hidden" name="start_item" value="<%=startItem%>">
                     <input type="hidden" name="command_item" value="<%=cmdItem%>">
                     <input type="hidden" name="approval_command" value="<%=appCommand%>">
                     <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                     <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
                     <input type="hidden" name="hidden_receive_item_id" value="">
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                    <div class="row">
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label><br />

                                                    <b><%=rec.getRecCode()%></b>
                                                
                                                </div>
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label><br />

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
                                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%> </td>
                        
                                                
                                                </div>
                                                    
                                                
                                                    
                                                    
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label><br />

                                                     <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date() : rec.getReceiveDate(), 1, -5,"form-control-date", "")%>
                                                </div>
                                                
                                                   
                                                    
                                                    
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][9]%></label><br />

                                                    <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date(): rec.getReceiveDate(),"form-control-date")%>
                                                </div>
                                                
                                                
                                                
                                                      
                                                      
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label><br />

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
						%>
						<%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"","form-control")%> </td>
						<% } %>
                                                
                                                </div>
                                                    
                                                    
                                                     
                                                    
                                                    
                                                
                                            </div>
                                            <!-- End of col-md-4 -->
                                            <div class="col-md-4">
                                                
                                                
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label><br />

                                                  
                                                    <%
							Vector obj_supplier = new Vector(1,1);
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);

                                                                String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                                                 " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                                                 " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                                                 " != "+PstContactList.DELETE;
                                                                Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                                //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
                                                                //String whClauseSupp = " CLS." + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + "=" + PstContactClass.FLD_CLASS_VENDOR;
                                                                //String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE];
                                                                //Vector vt_supp = PstContactList.listContactByClassType(0,0,whClauseSupp,ordBySupp);
                                                                for(int d=0; d<vt_supp.size(); d++){
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
                                                              <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","form-control")%>
                                                
                                                </div>
                                                              
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label><br />

                                                    <input type="text" readonly name="txt_ponumber"  value="<%= po.getPoCode() %>" class="form-control" size="15">
                                                    <%if(listMatReceiveItem.size()==0){%><a href="javascript:cmdSelectPO()">CHK</a><%}%>
                                                
                                                </div>
                                                    
                                                <div class="form-group">
                                                     <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label><br />
                                                     <input type="text"  class="form-control" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right">
                                                
                                                </div>
                                                     
                                                     
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][11]%></label><br />
                                                    
                                                    <%
                                                        Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                                                        Vector vectCurrVal = new Vector(1,1);
                                                        Vector vectCurrKey = new Vector(1,1);
                                                        vectCurrKey.add(" ");
                                                        vectCurrVal.add("0");
                                                        for(int i=0; i<listCurr.size(); i++){
                                                            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                                            vectCurrKey.add(currencyType.getCode());
                                                            vectCurrVal.add(""+currencyType.getOID());
                                                        }
                                                      %>
                                                      <%=ControlCombo.draw("CURRENCY_CODE","form-control", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled")%>
                                                    
                                                </div>              

                                            </div>
                                              <!-- End of col-md-4 -->
                                            <div class="col-md-4">
                                                
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label><br />

                                                    <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="2"><%=rec.getRemark()%></textarea>
                                                
                                                </div>

                                            </div>
                                            <!-- End of col-md-4 -->  
                                    </div>
                                <!-- End of row -->
                                    <div class="row">
                                        
                                        <div class="col-md-12">
                                            
                                             <%
                                                    Vector list = drawListRecItem(SESS_LANGUAGE,listMatReceiveItem,startItem,privManageData);
                                                    out.println(""+list.get(0));
                                                    Vector listError = (Vector)list.get(1);
                                             %>
                                            
                                        </div>
                                    </div>
                                    <!-- End of row -->
                                    
                                    
                                    <div class="row">
                                        
                                        <div class="col-md-12">
                                            
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
                                            
                                        </div>
                                                    
                                        <div class="col-md-12">
                                            
                                            <%
						for(int k=0;k<listError.size();k++){
							if(k==0)
								out.println(listError.get(k)+"<br>");
							else
								out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
						}
					   %>
                                            
                                        </div>            
                                          
                                        

                                    </div>
                                    <!-- End of row -->
                                    
                                    <br />
                                    <div class="row">
                                        
                                        <div class="col-md-3">
                                            
                                            <label for="exampleInputEmail1"><b><%=textListForwarderInfo[SESS_LANGUAGE][5]%> &nbsp; :</b></label><br /><br />
                                            
                                            <div class="form-group"> 
                                             <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][0]%></label><br />
                                            
                                            
                                            
                                            <strong><%=forwarderInfo.getDocNumber()%></strong>
                                            
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FORWARDER_INFO_ID]%>" value="<%=forwarderInfo.getOID()%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_RECEIVE_ID]%>" value="<%=oidReceiveMaterial%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FI_LOCATION_ID]%>" value="<%=rec.getLocationId()%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_NUMBER]%>" value="<%=forwarderInfo.getDocNumber()%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FI_CURRENCY_ID]%>" value="<%=rec.getCurrencyId()%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FI_STATUS]%>" value="<%=rec.getReceiveStatus()%>">
                                            <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_FI_TRANS_RATE]%>" value="<%=rec.getTransRate()%>">
                                           </div>  
                                              <div class="form-group">  
                                                    <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][1]%></label><br />
                                                    <%=ControlCombo.drawBootsratap(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"","form-control")%> <br />
                                              </div>
                                             
                                              <div class="form-group">  
                                                    <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][2]%></label><br />
                                                    <%=ControlDate.drawDateWithBootstrap(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE], (forwarderInfo.getDocDate()==null) ? new Date() : forwarderInfo.getDocDate(), 0, -1, "form-control-date", "")%><br />
                                              </div>
                                             
                                              <div class="form-group">  
                                                    <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][3]%></label><br />
                                                    <strong><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></strong>
                                                    <input type="hidden" name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_TOTAL_COST]%>" value="<%=totalForwarderCost%>"><br />
                                              </div>
                                             
                                              <div class="form-group">  
                                                    <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][4]%></label><br />
                                                    <textarea name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]%>" class="form-control" wrap="VIRTUAL" rows="2" cols="27"><%=forwarderInfo.getNotes()%></textarea><br />
                                              </div>
                                          </div>
                                        <!-- End of col-md-5 -->  
                                        
                                        <div class="col-md-6">
                                            
                                            
                                        </div>
                                        
                                        
                                         <div class="col-md-3" >
                                            <br />
                                            <br />
                                             <%
                                                String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
						double totalBeli = PstMatReceiveItem.getTotal(whereItem);
						double ppn = rec.getTotalPpn();
						double totalBeliWithPPN = (totalBeli * (rec.getTotalPpn() / 100)) + totalBeli;
                                             %>
                                             
                                           <div class="form-group">   
                                                <label for="exampleInputEmail1"><%=textListGlobal[SESS_LANGUAGE][2]%> <%=textListOrderItem[SESS_LANGUAGE][5]%></label><br />   
                                                <b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b><br />
                                           </div>
                                         
                                           <div class="form-group">   
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][8]%></label><br /> 
                                                <input type="text"  class="form-control" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%=FRMHandler.userFormatStringDecimal(ppn)%>"  size="15" style="text-align:left"><br /> 
                                           </div>
                                          
                                           <div class="form-group">   
                                                <label for="exampleInputEmail1"><%=textListGlobal[SESS_LANGUAGE][2]%></label><br /> 
                                                <b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%></b><br />
                                           </div>
                                           
                                            <div class="form-group">   
                                                <label for="exampleInputEmail1"><%=textListGlobal[SESS_LANGUAGE][2]%></label><br /> 
                                                <b><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></b>
                                           </div>
                                           
                                            <div class="form-group">   
                                                <label for="exampleInputEmail1"><%=textListGlobal[SESS_LANGUAGE][1]%></label><br /> 
                                                <b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)%></b>
                                            </div>
                                           
                                        </div>   
                                       <!-- End of col-md-3 -->    
                                        
                                            
                                    </div>
                                    <!-- End of row -->
                                    
                                  
                                    
                                    
                                    
                                    
                                    <div class="box-footer">
                                        
                                         <div class="col-md-3" >    
                                            <button  onclick="javascript:cmdSave()" type="submit" class="btn btn-primary">Simpan</button>
                                            <button type="submit" onclick="javascript:cmdAsk()" class="btn btn-primary" >Hapus</button>
                                            <button type="submit" onclick="javascript:cmdBack()" class="btn btn-primary" >Kembali</button>
                                         </div>   
                                    </div>
                                
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>       

    </body>
</html>
