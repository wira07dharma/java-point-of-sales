<%@ page import="com.dimata.posbo.entity.warehouse.MatConReturnItem,
                 com.dimata.posbo.entity.masterdata.Material,
				 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.warehouse.MatConReturn,
                 com.dimata.posbo.form.warehouse.FrmMatConReturn,
                 com.dimata.posbo.form.warehouse.CtrlMatConReturn,
                 com.dimata.posbo.entity.warehouse.PstMatConReturnItem,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.posbo.entity.warehouse.PstMatConReturn,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
				 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Stok Keluar","Dengan Nota Terima"},
	{"Consigment","Out Stock","With Goods Receiving"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Dari Consignee","Tanggal","Ke Consignor","Status","Keterangan","Alasan","Nota Supplier","Waktu"},
	{"Number","From Consignee","Date","To Consignor","Status","Remark","Reason","Invoice","Time"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Harga Beli","Harga Jual","Mata Uang","Qty","Total Beli"},
	{"No","Code","Name","Unit","Cost","Price","Currency","Qty","Total Cost"}
};

/**
* this method used to list all po item
*/
public String drawListOrderItem(int language,Vector objectClass,int start,boolean privManageData) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"10%");
		ctrlist.addHeader(textListOrderItem[language][2],"25%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		ctrlist.addHeader(textListOrderItem[language][4],"10%");
        //ctrlist.addHeader(textListOrderItem[language][5],"10%");
		ctrlist.addHeader(textListOrderItem[language][6],"7%");
		ctrlist.addHeader(textListOrderItem[language][7],"8%");
		ctrlist.addHeader(textListOrderItem[language][8],"12%");

		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)	{
			start=0;
		}

		for(int i=0; i<objectClass.size(); i++) {
			 Vector temp = (Vector)objectClass.get(i);
			 MatConReturnItem retItem = (MatConReturnItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 CurrencyType matCurrency = (CurrencyType)temp.get(3);
			 rowx = new Vector();
			 start = start + 1;

			 rowx.add(""+start+"");
			 if(privManageData) {
				 rowx.add("<a href=\"javascript:editItem('"+String.valueOf(retItem.getOID())+"')\">"+mat.getSku()+"</a>");
			 }
			 else {
				 rowx.add(mat.getSku());
			 }

			 rowx.add(mat.getName());
			 rowx.add(unit.getCode());
			 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getCost())+"</div>");
             //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
			 rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");
			 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
			 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"</div>");

			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item return...</div>";
	}
	return result;
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_ROMR);
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
long oidReturnMaterial = FRMQueryString.requestLong(request, "hidden_return_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* purchasing ret code and title
*/
String retCode = "";//i_pstDocType.getDocCode(docType);
String retTitle = "Retur Barang"; //i_pstDocType.getDocTitle(docType);
String retItemTitle = retTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatConReturn ctrlMatConReturn = new CtrlMatConReturn(request);
iErrCode = ctrlMatConReturn.action(iCommand , oidReturnMaterial);
FrmMatConReturn frmret = ctrlMatConReturn.getForm();
MatConReturn ret = ctrlMatConReturn.getMatConReturn();
errMsg = ctrlMatConReturn.getMessage();

/**
* generate code of current currency
*/
String priceCode = "Rp.";
/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = true;

/**
* list purchase order item
*/
oidReturnMaterial = ret.getOID();
int recordToGetItem = 10;
String whereClauseItem = ""+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatConReturnItem.getCount(whereClauseItem);
Vector listMatConReturnItem = PstMatConReturnItem.list(startItem,recordToGetItem,whereClauseItem);


if(iCommand==Command.DELETE && iErrCode==0){
%>
	<jsp:forward page="return_material_list.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
%>
<!-- End of Jsp Block -->

<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cHide(){
	document.all.CHIDE.style.display = "none";
}

function cDisplay(){
	document.all.CHIDE.style.display = "";
}

function cmdEdit(oid){
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function compare(){
	var dt = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE]%>_dy.value;
	var mn = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE]%>_mn.value;
	var yy = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);	
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave() {
<%
	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))	{
%>
		var invNo = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
		if(invNo!='') {
			document.frm_retmaterial.command.value="<%=Command.SAVE%>";
			document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
			document.frm_retmaterial.action="return_wh_material_edit.jsp";
			if(compare()==true)
				document.frm_retmaterial.submit();
		}
		else {
			alert("Nomor invoice supplier harus diisi");
		}
<%
	}
	else {
%>
	alert("Document sudah posted !!!");
<%
	}
%>
}

function cmdAsk(oid){
	document.frm_retmaterial.command.value="<%=Command.ASK%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdCancel(){
	document.frm_retmaterial.command.value="<%=Command.CANCEL%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdConfirmDelete(oid){
	document.frm_retmaterial.command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.approval_command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdBack(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_material_list.jsp";
	document.frm_retmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------

function findInvoice() {
	var suppId = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_SUPPLIER_ID]%>.value;
	var suppNota = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
	window.open("return_wh_material_receive.jsp?supplier_id="+suppId+"&supplier_nota="+suppNota,"return_invoice_supplier","scrollbars=yes,height=500,width=700,status=yes,toolbar=no,menubar=no,location=no");
}

function addItem() {
<%
	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
%>
		document.frm_retmaterial.command.value="<%=Command.ADD%>";
		document.frm_retmaterial.action="return_wh_materialitem.jsp";
		if(compareDateForAdd()==true)
			document.frm_retmaterial.submit();
<%
	}
	else {
%>
		alert("Document has been posted !!!");
<%
	}
%>
}

function editItem(oid) {
<%
	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
%>
		document.frm_retmaterial.command.value="<%=Command.EDIT%>";
		document.frm_retmaterial.hidden_return_item_id.value=oid;
		document.frm_retmaterial.action="return_wh_materialitem.jsp";
		document.frm_retmaterial.submit();
<%
	}
	else {
%>
		alert("Document has been posted !!!");
<%
	}
%>
}

function itemList(comm) {
	document.frm_retmaterial.command.value=comm;
	document.frm_retmaterial.prev_command.value=comm;
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function printForm() {
	window.open("return_wh_material_print_form.jsp?hidden_return_id=<%=oidReturnMaterial%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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

function MM_findObj(n, d) { //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSaveOn.jpg')">
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
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_retmaterial" method="post" action="">
<%
try {
%>
              <input type="hidden" name="command" value="">
			  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">
              <input type="hidden" name="hidden_return_item_id" value="">
              <input type="hidden" name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RET_CODE]%>" value="<%=ret.getRetCode()%>">
              <input type="hidden" name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <table width="100%" border="0">
                <tr>
                  <td colspan="2"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td align="right">&nbsp;</td>
                    </tr>
                    <tr>
                      <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                      <td width="27%">: <b>
                        <%
						  if(ret.getRetCode()!="" && iErrCode==0) {
							out.println(ret.getRetCode());
						  }
						  else {
						  	out.println("");
						  }
						  %></b></td>
                      <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                      <td width="28%">: 
                        <%
							Vector obj_supplier = new Vector(1,1);
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);
                              String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                               " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                               " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                               " != "+PstContactList.DELETE;
							Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
							//Vector vt_supp = PstContactList.list(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
							for(int d=0; d<vt_supp.size(); d++){
								ContactList cnt = (ContactList)vt_supp.get(d);
								String cntName = cnt.getCompName();
								if(cntName.length()==0){
									cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
								}
								val_supplier.add(String.valueOf(cnt.getOID()));
								key_supplier.add(cntName);
							}
							String select_supplier = ""+ret.getSupplierId();
						  %>
                        <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%> </td>
                      <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                      <td width="15%" align="right">
                        <%
						  	Vector asuOID = new Vector();
						  	Vector asuName = new Vector();
							for (int i=0;i<PstMatConReturn.strReturnReasonList[SESS_LANGUAGE].length;i++){
								asuOID.add(""+i);
								asuName.add(PstMatConReturn.strReturnReasonList[SESS_LANGUAGE][i]);
							}
						  %>
                        <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_REASON], null, ""+ret.getReturnReason(), asuOID, asuName, "", "formElemen")%></td>
                    </tr>
                    <tr>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                      <td>: 
                        <%
							Vector obj_locationid = new Vector(1,1);
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,"");
							Vector vt_loc = PstLocation.list(0,0,"","");
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+ret.getLocationId(); //selected on combo box
						  %>
                        <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%></td>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                      <td>: 
                        <input type="text"  class="formElemen" name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=ret.getInvoiceSupplier()%>"  size="20" style="text-align:right">
						* <a href="javascript:findInvoice()">
						<%if(listMatConReturnItem.size()==0){%>
							CHK
						<%}%>
						</a>
						<input type="hidden" name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=ret.getReceiveMaterialId()%>">
						<input type="hidden" name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_PURCHASE_ORDER_ID]%>" value="<%=ret.getPurchaseOrderId()%>">
					  </td>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                      <td rowspan="3" align="right" valign="top"><textarea name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3"><%=ret.getRemark()%></textarea></td>
                    </tr>
                    <tr>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                      <td>: <%=ControlDate.drawDateWithStyle(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date() : ret.getReturnDate(), 1, -5, "formElemen", "")%></td>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                      <td>: <%
                        Vector obj_status = new Vector(1,1);
                        Vector val_status = new Vector(1,1);
                        Vector key_status = new Vector(1,1);

                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
						
						
						//add by fitra
						  if(listMatConReturnItem.size()>0){
						  val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
						}			
                        if(listMatConReturnItem.size()>0){
                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                        }
                        String select_status = ""+ret.getReturnStatus();
                        if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                        }else if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                        }else{
						  %>
                                <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_STATUS],null,select_status,val_status,key_status,"","formElemen")%>
                                <%}%>
                                </td>
                      <td>&nbsp;</td>
                    </tr>
                    <tr>
                      <td><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                      <td>: <%=ControlDate.drawTimeSec(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date(): ret.getReturnDate(),"formElemen")%></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      </tr>
                  </table></td>
                </tr>
                <tr>
                  <td colspan="2"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawListOrderItem(SESS_LANGUAGE,listMatConReturnItem,startItem,privManageData)%></td>
                      </tr>
                      <%if(oidReturnMaterial!=0){%>
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
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"> <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
							  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
							  <% } %>
                            </tr>
                          </table></td>
                      </tr>
                      <%}%>
                    </table></td>
                </tr>
                <%//if(oidReturnMaterial!=0){%>
                <%if(listMatConReturnItem!=null && listMatConReturnItem.size()>0){%>
                <tr>
                  <td valign="top"> </td>
                  <td width="30%" valign="top">
				    <table width="100%" border="0">
                      <tr> 
                        <td width="44%"> <div align="right"><strong><%="TOTAL : "+retCode%></strong></div></td>
                        <td width="15%"> <div align="right"></div></td>
                        <td width="41%"> <div align="right"><strong> 
                          <%
						  String whereItem = ""+PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
						  out.println(Formater.formatNumber(PstMatConReturnItem.getTotal(whereItem),"##,###.00"));
						  %>
                          </strong></div>
						</td>
                      </tr>
                    </table>
				  </td>
                </tr>
                <%}%>
                <tr>
                  <td colspan="2"> <table width="100%" border="0">
                      <tr>
                        <td width="70%"><div id="idcommand"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td>
                                <%
								ctrLine.setLocationImg(approot+"/images");
			
								// set image alternative caption
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_ASK,true));
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_CANCEL,false));
			
								ctrLine.initDefault();
								ctrLine.setTableWidth("80%");
								String scomDel = "javascript:cmdAsk('"+oidReturnMaterial+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidReturnMaterial+"')";
								String scancel = "javascript:cmdEdit('"+oidReturnMaterial+"')";
								ctrLine.setCommandStyle("command");
								ctrLine.setColCommStyle("command");
			
								// set command caption
								ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_SAVE,true));
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_ASK,true));
								ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_DELETE,true));
								ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_CANCEL,false));
			
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
			
								if(iCommand==Command.SAVE && frmret.errorSize()==0){
									iCommand=Command.EDIT;
								}
			
								if(documentClosed){
									ctrLine.setSaveCaption("");
									ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
									ctrLine.setDeleteCaption("");
									ctrLine.setConfirmDelCaption("");
									ctrLine.setCancelCaption("");
								}
								%>
                                <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
                              </td>
                            </tr>
                          </table>
						  </div>
						  <div id="idcommandback"></div>
                        </td>
                        <td width="30%"><%if(listMatConReturnItem.size()>0){%> <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="5%" valign="top"><a href="javascript:printForm('<%=oidReturnMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidReturnMaterial%>')" class="command" >Print
                                <%=retTitle%> </a></td>
                            </tr>
                          </table><%}%></td>
                      </tr>
                    </table></td>
                </tr>
              </table>
<%
}
catch(Exception e) {
	System.out.println(e);
}
%>
            </form>
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
<!-- #EndTemplate -->
</html>

