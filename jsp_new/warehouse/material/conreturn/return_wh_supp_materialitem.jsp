<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstMatConReturnSerialCode" %>
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

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Stok Keluar","Ke Consignor","Tanpa Nota Terima"},
	{"Consigment","Stock Out","To Consignor","Without Goods Receiving"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = 
{
	{"Nomor","Dari Consignee","Tanggal","Ke Consignor","Status","Keterangan","Alasan"},
	{"Number","From Consignee","Date","To Consignor","Status","Remark","Reason"}	
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = 
{
	{"No","Sku","Nama Barang","Unit","HPP","Harga Jual","Mata Uang","Qty","Total"},
	{"No","Code","Name","Unit","Cost","Sell Price","Currency","Qty","Total"}
};

/**
* this method used to maintain poMaterialList
*/
public Vector drawListRetItem(int language,int iCommand,FrmMatConReturnItem frmObject,
                              MatConReturnItem objEntity,Vector objectClass,
                              long retItemId,int start,int tranUsedPriceHpp)
{
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

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
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	   start=0;

	for(int i=0; i<objectClass.size(); i++)	{
			 Vector temp = (Vector)objectClass.get(i);
			 MatConReturnItem retItem = (MatConReturnItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 CurrencyType matCurrency = (CurrencyType)temp.get(3);
			 rowx = new Vector();
			 start = start + 1;
	
			if (retItemId == retItem.getOID()) index = i;
			if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
			{				
				rowx.add(""+start); 
				rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+retItem.getMaterialId()+
					"\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
				rowx.add("<input type=\"text\" size=\"35\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
				rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+retItem.getUnitId()+
					"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
				rowx.add("<div align=\"right\"><input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+Formater.formatNumber(retItem.getCost(),"###")+"\" onKeyUp=\"javascript:cntTotal()\" class=\"formElemen\"></div>");
               // rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
				rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+retItem.getCurrencyId()+
					"\"><input type=\"text\" size=\"5\" name=\"currencyType\" value=\""+matCurrency.getCode()+"\" class=\"hiddenText\" readOnly>");
				rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal()\"></div>");
				rowx.add("<div align=\"right\"><input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+Formater.formatNumber(retItem.getTotal(),"###")+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
			}
			else
			{
				 rowx.add(""+start+"");
				 rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(retItem.getOID())+"')\">"+mat.getSku()+"</a>");
				 rowx.add(mat.getName());
				 rowx.add(unit.getCode());			 
				 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getCost())+"</div>");
                 //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
				 rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");

                if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                    String where = PstMatConReturnSerialCode.fieldNames[PstMatConReturnSerialCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+retItem.getOID();
                    int cnt = PstMatConReturnSerialCode.getCount(where);
                    double recQtyPerBuyUnit = retItem.getQty();
                    double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultSellUnitId());
                    double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                    double max = recQty;

                    if(cnt<max){
                        if(listError.size()==0){
                            listError.add("Silahkan cek :");
                        }
                        listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty return");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(retItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
                }else{
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
                }
				 //rowx.add("<div align=\"right\">"+String.valueOf(retItem.getQty())+"</div>");
				 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"</div>");
			} 
			lstData.add(rowx);
		}

	rowx = new Vector();
	if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) { 
		rowx.add(""+(start+1)); 
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\"><a href=\"javascript:cmdCheck()\">CHK</a>");
		rowx.add("<input type=\"text\" size=\"35\" name=\"matItem\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
		rowx.add("<div align=\"right\"><input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
        //rowx.add("<div align=\"right\">&nbsp;</div>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"5\" name=\"currencyType\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
		rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal()\"></div>");
		rowx.add("<div align=\"right\"><input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
		
		lstData.add(rowx);
	}

    list.add(ctrlist.draw());
	list.add(listError);
	return list;
}

%>


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
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");
long oidReturnMaterialItem = FRMQueryString.requestLong(request,"hidden_return_item_id");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String retCode = ""; //i_pstDocType.getDocCode(docType);
String retTitle = "Retur Barang"; //i_pstDocType.getDocTitle(docType);
String retItemTitle = retTitle + " Item";

/**
* process on purchase order main
*/
CtrlMatConReturn ctrlMatConReturn = new CtrlMatConReturn(request);
iErrCode = ctrlMatConReturn.action(Command.EDIT,oidReturnMaterial);
FrmMatConReturn frmMatConReturn = ctrlMatConReturn.getForm();
MatConReturn ret = ctrlMatConReturn.getMatConReturn();
	
/**
* check if document may modified or not 
*/
boolean privManageData = true; 

ControlLine ctrLine = new ControlLine();
CtrlMatConReturnItem ctrlMatConReturnItem = new CtrlMatConReturnItem(request);
ctrlMatConReturnItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatConReturnItem.action(iCommand,oidReturnMaterialItem,oidReturnMaterial);
FrmMatConReturnItem frmMatConReturnItem = ctrlMatConReturnItem.getForm();
MatConReturnItem retItem = ctrlMatConReturnItem.getMatConReturnItem();
msgString = ctrlMatConReturnItem.getMessage();

int comm = iCommand;
if(iErrCode!=0 && retItem.getOID()!=0 && iCommand==Command.SAVE){
	comm = Command.EDIT;
}else if(iErrCode!=0 && retItem.getOID()==0 && iCommand==Command.SAVE){
	comm = Command.ADD;
}

String whereClauseItem = PstMatConReturnItem.fieldNames[PstMatConReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatConReturnItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatConReturnItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
} 

Vector listMatConReturnItem = PstMatConReturnItem.list(startItem,recordToGetItem,whereClauseItem);
if(listMatConReturnItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;   
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST; 
	 }
	 listMatConReturnItem = PstMatConReturnItem.list(startItem,recordToGetItem,whereClauseItem);
}
%>

<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm)
{
	document.frm_retmaterial.command.value=comm;
	document.frm_retmaterial.hidden_return_id.value=oid;
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function gostock(oid){
    document.frm_retmaterial.command.value="<%=Command.EDIT%>";
    document.frm_retmaterial.hidden_return_item_id.value=oid;
    document.frm_retmaterial.action="ret_stockcode.jsp";
    document.frm_retmaterial.submit();
}

function cmdAdd(){
	document.frm_retmaterial.hidden_return_item_id.value="0";
	document.frm_retmaterial.command.value="<%=Command.ADD%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_retmaterial.submit();
}

function cmdEdit(oidReturnMaterialItem)
{
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdAsk(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.ASK%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdSave(){
	document.frm_retmaterial.command.value="<%=Command.SAVE%>"; 
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdConfirmDelete(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdCancel(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBack(){
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.start_item.value = 0;
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdCheck()
{
	var strvalue  = "material_store_dosearch.jsp?command=<%=Command.FIRST%>"+	
					"&hidden_return_id=<%=ret.getOID()%>"+
                    "&hidden_location_id=<%=ret.getLocationId()%>"+
					"&invoice_supplier=''"+
					"&mat_code="+document.frm_retmaterial.matCode.value+
					"&mat_vendor="+document.frm_retmaterial.<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_SUPPLIER_ID]%>.value;
	window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cntTotal()
{
	var qty = cleanNumberFloat(document.frm_retmaterial.<%=FrmMatConReturnItem.fieldNames[FrmMatConReturnItem.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
	var price = cleanNumberFloat(document.frm_retmaterial.<%=FrmMatConReturnItem.fieldNames[FrmMatConReturnItem.FRM_FIELD_COST]%>.value,guiDigitGroup,guiDecimalSymbol);		
	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = parseFloat(price) * qty;
		document.frm_retmaterial.<%=FrmMatConReturnItem.fieldNames[FrmMatConReturnItem.FRM_FIELD_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);					 
	}
	else
	{
		document.frm_retmaterial.<%=FrmMatConReturnItem.fieldNames[FrmMatConReturnItem.FRM_FIELD_QTY]%>.focus();
	}
}

function cmdListFirst(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListPrev(){
	document.frm_retmaterial.command.value="<%=Command.PREV%>";
	document.frm_retmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListNext(){
	document.frm_retmaterial.command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListLast(){
	document.frm_retmaterial.command.value="<%=Command.LAST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBackList(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_supp_material_list.jsp";
	document.frm_retmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function MM_swapImgRestore() 
{ //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() 
{ //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) 
{ //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() 
{ //v3.0
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
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg','<%=approot%>/images/BtnSaveOn.jpg')">
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
            <form name="frm_retmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">
              <input type="hidden" name="hidden_return_item_id" value="<%=oidReturnMaterialItem%>">
              <input type="hidden" name="<%=FrmMatConReturnItem.fieldNames[FrmMatConReturnItem.FRM_FIELD_RETURN_MATERIAL_ID]%>" value="<%=oidReturnMaterial%>">
			  <input type="hidden" name="<%//=frmSrcMatConReturn.fieldNames[frmSrcMatConReturn.FRM_FIELD_LOCATION_TYPE]%>" value="<%//=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center"> 
                  <td colspan="3" class="title"> 
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td valign="bottom">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td align="right">&nbsp;</td>
                        <td align="right">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td> 
                        <td width="28%" align="left"> 
                          : <b><%=ret.getRetCode()%></b>
                        </td>
                        <td width="8%" valign="bottom">&nbsp;</td>
                        <td width="16%" valign="top">&nbsp; </td>
                        <td width="17%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][3]%>: </td>
                        <td width="20%" align="right"><% 
							Vector obj_supplier = new Vector(1,1);  
							Vector val_supplier = new Vector(1,1); 
							Vector key_supplier = new Vector(1,1); 
							//Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
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
                        <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"disabled=\"true\"","formElemen")%> </td>						
                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td> 
                        <td width="28%" align="left">  : <%=ControlDate.drawDateWithStyle(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date() : ret.getReturnDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp; 
                        </td>
                        <td width="17%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][6]%> :
                      <td width="20%" align="right" valign="top"><%=PstMatConReturn.strReturnReasonList[SESS_LANGUAGE][ret.getReturnReason()]%>                      </tr>
                      <tr>
                        <td width="11%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td> 
                        <td width="28%" align="left">: 
                          <% 
							Vector obj_locationid = new Vector(1,1); 
							Vector val_locationid = new Vector(1,1); 
							Vector key_locationid = new Vector(1,1); 
							//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
							Vector vt_loc = PstLocation.list(0,0,"","");
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+ret.getLocationId(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td width="17%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : </td>
                      <td width="20%" rowspan="2" align="right" valign="top"><textarea name="<%=FrmMatConReturn.fieldNames[FrmMatConReturn.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2" disabled="true"><%=ret.getRemark()%></textarea>                      </tr>
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="right">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td valign="top"> 
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr> 
                        <td colspan="3" > 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top"> 
                              <%
								Vector listError = new Vector();
							    try {
							  %>
                              <td height="22" valign="middle" colspan="3">
							   <%
                                    Vector list = drawListRetItem(SESS_LANGUAGE,comm,frmMatConReturnItem, retItem,listMatConReturnItem,oidReturnMaterialItem,startItem,tranUsedPriceHpp);
                                    out.println(""+list.get(0));
                                    listError = (Vector)list.get(1);
                               %>
							  </td>
                                <%
								} catch(Exception e) {
									System.out.println(e);
								}
								%>
                            </tr>
                            <tr align="left" valign="top"> 
                              <td height="8" align="left" colspan="3" class="command"> 
                                <span class="command"> 
                                <% 
								int cmd = 0;
								if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST)	{
									cmd =iCommand; 
								}
								else {
								    if(iCommand == Command.NONE || prevCommand == Command.NONE)	{
										cmd = Command.FIRST;
									}	
								    else {	
										cmd =prevCommand; 
									}	
							    }
                                ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));
								%>
                                </span>
							  </td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="22" valign="middle" colspan="3"><span class="errfont">
                                <%
									for(int k=0;k<listError.size();k++) {
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
                                <%
								ctrLine.setLocationImg(approot+"/images");
								
								// set image alternative caption 
								ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)+" List");							
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ASK,true));							
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_CANCEL,false));																					
								
								ctrLine.initDefault();
								ctrLine.setTableWidth("65%");
								String scomDel = "javascript:cmdAsk('"+oidReturnMaterialItem+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidReturnMaterialItem+"')";
								String scancel = "javascript:cmdEdit('"+oidReturnMaterialItem+"')";								
								ctrLine.setCommandStyle("command");
								ctrLine.setColCommStyle("command");
								
								// set command caption 
								ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)+" List");							
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ASK,true));							
								ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_DELETE,true));														
								ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_CANCEL,false));																					
								

								if (privDelete)	{
									ctrLine.setConfirmDelCommand(sconDelCom);
									ctrLine.setDeleteCommand(scomDel);
									ctrLine.setEditCommand(scancel);
								}
								else { 
									ctrLine.setConfirmDelCaption("");
									ctrLine.setDeleteCaption("");
									ctrLine.setEditCaption("");
								}

								if(privAdd == false  && privUpdate == false) {
									ctrLine.setSaveCaption("");
								}

								if (privAdd == false) {
									ctrLine.setAddCaption("");
								}
								
								String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
								if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0)	{
								%>
                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr> 
                                    <% if(ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
									<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
									<% } %>
									<td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)%></a></td></tr>
                                </table>
                                <%	
								} else {
									out.println(strDrawImage);
								}
								%>
                              </td>
                            </tr>
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
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --> 
</html>
