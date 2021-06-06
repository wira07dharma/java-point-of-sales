<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstDispatchStockCode,
                   com.dimata.posbo.entity.warehouse.PstReturnStockCode" %>
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
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Stok Keluar","Ke Consignor","Tanpa Nota Terima"},
	{"Consigment","Stock Out","To Consignor","Without Goods Receiving"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Dari Consignee","Tanggal","Ke Consignor","Status","Keterangan","Alasan","Waktu"},
	{"No","From Consignee","Date","To Consignor","Status","Remark","Reason","Time"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama","Unit","HPP","Harga Jual","Mata Uang","Qty","Total"},
	{"No","Code","Name","Unit","Cost","Sell Price","Currency","Qty","Total"}
};

/**
* this method used to list all po item
*/
public Vector drawListOrderItem(int language, Vector objectClass, int start, boolean privManageData, int tranUsedPriceHpp)
{
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);
	Vector result = new Vector(1,1);
	String strResult = "";
	double totQty = 0;
	double totBuy = 0;
	if(objectClass!=null && objectClass.size()>0)
	{
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
		if(tranUsedPriceHpp==0)
			ctrlist.addHeader(textListOrderItem[language][4],"10%");
        else
			ctrlist.addHeader(textListOrderItem[language][5],"10%");
		
		ctrlist.addHeader(textListOrderItem[language][6],"7%");
		ctrlist.addHeader(textListOrderItem[language][7],"8%");
		ctrlist.addHeader(textListOrderItem[language][8],"12%");

		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
		{
			start=0;
		}

		for(int i=0; i<objectClass.size(); i++){
		 	 rowx = new Vector();

			 Vector temp = (Vector)objectClass.get(i);
			 MatConReturnItem retItem = (MatConReturnItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 CurrencyType currencyType = (CurrencyType)temp.get(3);

			 start = start + 1;
			 totQty = totQty + retItem.getQty();
			 totBuy = totBuy + retItem.getTotal();

			 rowx.add(""+start+"");
			 if(privManageData)
			 {
				 rowx.add("<a href=\"javascript:editItem('"+String.valueOf(retItem.getOID())+"')\">"+mat.getSku()+"</a>");
			 }
			 else
			 {
				 rowx.add(mat.getSku());
			 }

			 rowx.add(mat.getName());
			 rowx.add(unit.getCode());
			 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getCost())+"</div>");
             //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
			 rowx.add("<div align=\"center\">"+currencyType.getCode()+"</div>");

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+retItem.getOID();
                int cnt = PstReturnStockCode.getCount(where);
                double recQtyPerBuyUnit = retItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultSellUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;

                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty retur");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(retItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
            }
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"</div>");

		lstData.add(rowx);
		}
		strResult = ctrlist.draw();
	}else{
		strResult = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data item barang retur ...</div>";
	}

	result.add(strResult);
	result.add(new Double(totQty));
	result.add(new Double(totBuy));

    list.add(result);
	list.add(listError);
	return list;
}


// utk menampilkan total qty dan harga beli
public String drawListTotal(double qtyItem, double totalBuyRetItem)
{
	String result = "<table class=\"listgen\" width=\"100%\" border=\"0\" cellspacing=\"1\">"+
						"<tr>"+
							"<td width=\"62%\" class=\"listgensell\">"+
								"<div align=\"center\">TOTAL</div>"+
							"</td>"+
							"<td width=\"8%\" class=\"listgensell\">"+
								"<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyItem)+"</div>"+
							"</td>"+
							"<td width=\"12%\" class=\"listgensell\">"+
								"<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalBuyRetItem)+"</div>"+
							"</td>"+
						"</tr>"+
					"</table>";
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
I_DocStatus i_docstatus = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
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
String retCode = ""; //i_pstDocType.getDocCode(docType);
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
function cmdEdit(oid){
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function gostock(oid){
    document.frm_retmaterial.command.value="<%=Command.EDIT%>";
    document.frm_retmaterial.hidden_return_item_id.value=oid;
    document.frm_retmaterial.action="ret_stockcode.jsp";
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

function cmdSave()
{
	<%	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
		document.frm_retmaterial.command.value="<%=Command.SAVE%>";
		document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
		document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
		if(compare()==true)
		{
			document.frm_retmaterial.submit();
		}
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function cmdAsk(oid){
	<%	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
    {
	%>
        document.frm_retmaterial.command.value="<%=Command.ASK%>";
        document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
		if(compare()==true)
		{
			document.frm_retmaterial.submit();
		}
	<%
    }
    else
    {
	%>
	alert("Document has been posted !!!");
	<%
    }
	%>
}

function cmdCancel(){
	document.frm_retmaterial.command.value="<%=Command.CANCEL%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdConfirmDelete(oid){
	document.frm_retmaterial.command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.approval_command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdBack(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_material_list.jsp";
	document.frm_retmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


function findInvoice()
{
	var suppId = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_SUPPLIER_ID]%>.value;
	var locId = document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_LOCATION_ID]%>.value
	window.open("return_store_supp_material_receive.jsp?supplier_id="+suppId+"&location_id="+locId,"return_invoice_supplier","scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");
}


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
		document.frm_retmaterial.command.value="<%=Command.ADD%>";
		document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
		if(compareDateForAdd()==true)
			document.frm_retmaterial.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function editItem(oid)
{
	<%	if ((ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (ret.getReturnStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
		document.frm_retmaterial.command.value="<%=Command.EDIT%>";
		document.frm_retmaterial.hidden_return_item_id.value=oid;
		document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
		document.frm_retmaterial.submit();
	<%
		}
		else
		{
	%>
	alert("Document has been posted !!!");
	<%
		}
	%>
}

function itemList(comm){
	document.frm_retmaterial.command.value=comm;
	document.frm_retmaterial.prev_command.value=comm;
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function printForm(){
	window.open("return_store_supp_material_print_form.jsp?hidden_return_id=<%=oidReturnMaterial%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
		  &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%> <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_retmaterial" method="post" action="">
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
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="right">&nbsp;</td>
                        <td align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%" align="left">
                          : <b><%=ret.getRetCode()%></b>
                        </td>
                        <td width="7%">&nbsp;</td>
                        <td width="23%">&nbsp;  </td>
                        <td width="12%" align="right"> <%=textListOrderHeader[SESS_LANGUAGE][3]%> : </td>
                        <td width="20%" align="right"><%
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);
                              String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                               " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                               " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                               " != "+PstContactList.DELETE;
                              Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

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
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="27%" align="left"> :
                          <%
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
							Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+ret.getLocationId(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;
                        </td>
                        <td width="12%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][6]%> :                           <td width="20%" align="right" valign="top"><%
						  	Vector asuOID = new Vector();
						  	Vector asuName = new Vector();
							for (int i=0;i<PstMatConReturn.strReturnReasonList[SESS_LANGUAGE].length;i++)
							{
								asuOID.add(""+i);
								asuName.add(PstMatConReturn.strReturnReasonList[SESS_LANGUAGE][i]);
							}
						  %>
                          <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_REASON], null, ""+ret.getReturnReason(), asuOID, asuName, "", "formElemen")%>                        </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="27%" align="left">: <%=ControlDate.drawDateWithStyle(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date() : ret.getReturnDate(), 1, -5, "formElemen", "")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td width="12%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : </td>
                      <td width="20%" rowspan="3" align="right" valign="top"><textarea name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2"><%=ret.getRemark()%></textarea>                      </tr>
                      <tr>
                        <td align="left" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td align="left" valign="top">: <%=ControlDate.drawTimeSec(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date(): ret.getReturnDate(),"formElemen")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td align="left" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td align="left" valign="top">: <%
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
                        <td>&nbsp;</td>
                        <td align="right">&nbsp;</td>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
						<%
						Vector result = drawListOrderItem(SESS_LANGUAGE,listMatConReturnItem,startItem,privManageData,tranUsedPriceHpp);
                        Vector vectResult = (Vector)result.get(0);
                        Vector listError = (Vector)result.get(1);
						String strResult = String.valueOf(vectResult.get(0));
						double totQty = Double.parseDouble(String.valueOf(vectResult.get(1)));
						double totBuy = Double.parseDouble(String.valueOf(vectResult.get(2)));

						out.println(strResult);
						%>
						</td>
                      </tr>
                      <%if(oidReturnMaterial!=0){%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
						  if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST) {
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
                        <td height="22" valign="middle" colspan="3"><span class="errfont">
                          <%
							for(int k=0;k<listError.size();k++){
								if(k==0)
									out.println(listError.get(k)+"<br>");
								else
									out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
							}
				  		  %>
                        </span></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
							  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
							  <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
							  <% } %>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
				<%if(listMatConReturnItem!=null && listMatConReturnItem.size()>0){%>
				  <tr> 
					<td valign="top">&nbsp; </td>
					<td width="27%" valign="top">
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
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                    <tr>
                      <td width="68%"><%
					ctrLine.setLocationImg(approot+"/images");

					// set image alternative caption
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_CANCEL,false));

					ctrLine.initDefault();
					ctrLine.setTableWidth("100%");
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

					if(privDelete && privManageData)
					{
						ctrLine.setConfirmDelCommand(sconDelCom);
						ctrLine.setDeleteCommand(scomDel);
						ctrLine.setEditCommand(scancel);
					}
					else{

						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					if(privAdd==false && privUpdate==false)
					{
						ctrLine.setSaveCaption("");
					}

					if(privAdd==false)
					{
						ctrLine.setAddCaption("");
					}

					if(iCommand==Command.SAVE && frmret.errorSize()==0)
					{
						iCommand=Command.EDIT;
					}
					out.println(ctrLine.drawImage(iCommand,iErrCode,errMsg));
					%></td>
                      <td width="32%"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td width="5%" valign="top"><a href="javascript:printForm('<%=oidReturnMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" alt="Print <%=retTitle%>" border="0"></a></td>
                          <td width="95%" nowrap>&nbsp;<a href="javascript:printForm('<%=oidReturnMaterial%>')" class="command">Print <%=retTitle%></a></td>
                        </tr>
                      </table></td>
                    </tr>
                  </table></td>
                </tr>
              </table>
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
