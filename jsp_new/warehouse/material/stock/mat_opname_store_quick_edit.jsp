<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstSourceStockCode" %>
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada item opname","Cetak Opname"},
	{"Stock","Opname","Search","List","Edit","There is no opname item","Print Opname"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"No","Lokasi","Tanggal","Time","Status","Supplier","Kategori","Sub Kategori","Keterangan","Status Dokumen"},
	{"No","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","Document Status"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{"No","Sku","Nama","Unit","Kategori","Sub Kategori","Qty Opname"},
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname"}
};

/**
* this method used to list all stock opname item
*/
public Vector drawListOpnameItem(int language,Vector objectClass,int start) {
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListOrderItem[language][0],"3%");
		ctrlist.addHeader(textListOrderItem[language][1],"15%");
		ctrlist.addHeader(textListOrderItem[language][2],"20%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		ctrlist.addHeader(textListOrderItem[language][4],"15%");
		ctrlist.addHeader(textListOrderItem[language][6],"5%");

		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start < 0)	{
			start=0;
		}

		for(int i=0; i<objectClass.size(); i++)	{
			 Vector temp = (Vector)objectClass.get(i);
			 MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 Category cat = (Category)temp.get(3);
			 SubCategory scat = (SubCategory)temp.get(4);
			 rowx = new Vector();
			 start = start + 1;

			 rowx.add(""+start+"");
			 rowx.add("<a href=\"javascript:editItem('"+String.valueOf(soItem.getOID())+"')\">"+mat.getSku()+"</a>");
			 rowx.add(mat.getName());
			 rowx.add(unit.getCode());
			 rowx.add(cat.getName());
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]+"="+soItem.getOID();
                int cnt = PstSourceStockCode.getCount(where);
                if(cnt<soItem.getQtyOpname()){
                    if(listError.size()==0){
                        listError.add("Please Check :");
                    }
                    listError.add(""+listError.size()+". Stock code Item "+mat.getName()+" not balance with qty opname item");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(soItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(soItem.getQtyOpname())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+String.valueOf(soItem.getQtyOpname())+"</div>");
            }
			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_OPN);
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
long oidStockOpname = FRMQueryString.requestLong(request, "hidden_opname_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String soCode = ""; //i_pstDocType.getDocCode(docType);
String opnTitle = "Opname"; //i_pstDocType.getDocTitle(docType);
String soItemTitle = opnTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
iErrCode = ctrlMatStockOpname.action(iCommand , oidStockOpname);
FrmMatStockOpname frmso = ctrlMatStockOpname.getForm();
MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
errMsg = ctrlMatStockOpname.getMessage();

/**
* Document status
*/
String docStatusName = i_status.getDocStatusName(so.getStockOpnameStatus());

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";

/**
* check if document may modified or not
*/
boolean privManageData = true;

/**
* list purchase order item
*/
oidStockOpname = so.getOID();
int recordToGetItem = 50;
int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(startItem,recordToGetItem,oidStockOpname);

if(iCommand==Command.DELETE && iErrCode==0) {
%>
    <jsp:forward page="mat_opname_store_quick_list.jsp">
        <jsp:param name="command" value="<%=Command.FIRST%>"/>
    </jsp:forward>
<%
}

/** */
Vector val_locationid = new Vector(1,1);
Vector key_locationid = new Vector(1,1);
//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
Vector vt_loc = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_CODE]);
for(int d=0;d<vt_loc.size();d++) {
	Location loc = (Location)vt_loc.get(d);
	val_locationid.add(""+loc.getOID()+"");
	key_locationid.add(loc.getName());
}
String select_locationid = ""+so.getLocationId(); //selected on combo box

/** */
Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
Vector vectGroupVal = new Vector(1,1);
Vector vectGroupKey = new Vector(1,1);
vectGroupVal.add("Semua Kategori..");
vectGroupKey.add("0");
if(materGroup!=null && materGroup.size()>0) {
  for(int i=0; i<materGroup.size(); i++) {
	  Category mGroup = (Category)materGroup.get(i);
	  vectGroupVal.add(mGroup.getName());
	  vectGroupKey.add(""+mGroup.getOID());
  }
}
else {
  vectGroupVal.add("-");
  vectGroupKey.add("0");
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_store_quick_edit.jsp";
	document.frm_matopname.submit();
}

function stockcode(oid){
    document.frm_matopname.hidden_opname_item_id.value=oid;
	document.frm_matopname.command.value="<%=Command.EDIT%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="op_stockcode.jsp";
	document.frm_matopname.submit();
}

function compare(){
	var dt = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_dy.value;
	var mn = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_mn.value;
	var yy = document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		var textSubKategori = ""; //document.frm_matopname.txt_subcategory.value;
		if (textSubKategori == "")
		{
			document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value = "0";
		}
		document.frm_matopname.command.value="<%=Command.SAVE%>";
		document.frm_matopname.prev_command.value="<%=prevCommand%>";
		document.frm_matopname.action="mat_opname_store_quick_edit.jsp";
		if(compare()==true)
			document.frm_matopname.submit();
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
	document.frm_matopname.command.value="<%=Command.ASK%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_store_quick_edit.jsp";
	document.frm_matopname.submit();
}

function cmdCancel(){
	document.frm_matopname.command.value="<%=Command.CANCEL%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_store_quick_edit.jsp";
	document.frm_matopname.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matopname.command.value="<%=Command.DELETE%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.approval_command.value="<%=Command.DELETE%>";
	document.frm_matopname.action="mat_opname_store_quick_edit.jsp";
	document.frm_matopname.submit();
}

function cmdBack(){
	document.frm_matopname.command.value="<%=Command.FIRST%>";
	document.frm_matopname.prev_command.value="<%=prevCommand%>";
	document.frm_matopname.action="mat_opname_store_quick_list.jsp";
	document.frm_matopname.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frm_matopname.txt_subcategory.value+
			"&oidCategory="+document.frm_matopname.<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]%>.value,"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function printForm()
{
	window.open("mat_opname_store_print_form.jsp?hidden_opname_id=<%=oidStockOpname%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matopname.command.value="<%=Command.LIST%>";
		document.frm_matopname.action="add_item.jsp"; //mat_opname_store_quick_item.jsp";
		if(compareDateForAdd()==true)
			document.frm_matopname.submit();
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
	<%	if (so.getStockOpnameStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matopname.command.value="<%=Command.EDIT%>";
		document.frm_matopname.hidden_opname_item_id.value=oid;
		document.frm_matopname.action="mat_opname_store_quick_item.jsp";
		document.frm_matopname.submit();
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
	document.frm_matopname.command.value=comm;
	document.frm_matopname.prev_command.value=comm;
	document.frm_matopname.action="mat_opname_store_quick_item.jsp";
	document.frm_matopname.submit();
}

function gostock(oid){
    document.frm_matopname.command.value="<%=Command.EDIT%>";
    document.frm_matopname.hidden_opname_item_id.value=oid;
    document.frm_matopname.action="op_stockcode.jsp";
    document.frm_matopname.submit();
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
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matopname" method="post" action="">
              <input type="hidden" name="command" value="">
			  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_opname_id" value="<%=oidStockOpname%>">
              <input type="hidden" name="hidden_opname_item_id" value="">
              <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_NUMBER]%>" value="<%=so.getStockOpnameNumber()%>">
              <input type="hidden" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>" value="<%=so.getSubCategoryId()%>">
              <table width="100%" border="0">
                <tr>
                  <td valign="top" colspan="3">
                    <hr size="1">
                  </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
						<td width="10%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
						<td width="20%" align="left" valign="bottom">: <%=so.getStockOpnameNumber()%></td>
                        <td width="10%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
						<td width="20%" align="left" valign="bottom">: <%=ControlDate.drawDateWithStyle(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_DATE], (so.getStockOpnameDate()==null) ? new Date() : so.getStockOpnameDate(), 1, -5, "formElemen", "")%></td>                        
                        <td width="10%" align="left" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
						<td width="25%" align="left" valign="top">: <input type="text"  class="formElemen" name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_TIME]%>" value="<%=so.getStockOpnameTime()%>"  size="10" style="text-align:right"></td>
					  </tr>
					  <tr>
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
						<td width="25%" align="left">: <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%></td>
                        <td width="10%" align="left" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
						<td width="20%" align="left" valign="bottom">: <%=docStatusName%></td>
						<td width="10%" align="left" valign="bottom" rowspan="2"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
						<td width="20%" align="left" valign="bottom" rowspan="2">: <textarea name="<%=FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2"><%=so.getRemark()%></textarea></td>
					  </tr>
					  <tr>
                        <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
						<td width="25%" align="left">: <%=ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+so.getCategoryId(), vectGroupKey, vectGroupVal, null)%></td>
                        <td width="10%" align="left" valign="top">&nbsp;</td>
						<td width="25%" align="left" valign="top">&nbsp;</td>
					  </tr>
					  <tr>
					  	<td colspan="4">&nbsp;</td>
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
							Vector list = drawListOpnameItem(SESS_LANGUAGE,listMatStockOpnameItem,startItem);
							out.println(""+list.get(0));
							Vector listError = (Vector)list.get(1);
						%>
						</td>
                      </tr>
                      <% if(oidStockOpname!=0) { %>
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
                          <table width="23%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,opnTitle+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,opnTitle+" Item",ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="2" width="80%">
					<%
					ctrLine.setLocationImg(approot+"/images");

					// set image alternative caption
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

					ctrLine.initDefault();
					ctrLine.setTableWidth("80%");
					String scomDel = "javascript:cmdAsk('"+oidStockOpname+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidStockOpname+"')";
					String scancel = "javascript:cmdEdit('"+oidStockOpname+"')";
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");

					// set command caption
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_ASK,true));
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,opnTitle,ctrLine.CMD_CANCEL,false));

					if(privDelete){
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

					if(iCommand==Command.SAVE && frmso.errorSize()==0){
						iCommand=Command.EDIT;
					}

					out.println(ctrLine.drawImage(iCommand,iErrCode,errMsg));
					%>
				  </td>
				  <td colspan="1" valign="top" width="20%">
                    <%if(listMatStockOpnameItem!=null && listMatStockOpnameItem.size()>0){%>
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="5%" valign="top"><a href="javascript:printForm('<%=oidStockOpname%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" alt="Print <%=opnTitle%>" border="0"></a></td>
                        <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidStockOpname%>')" class="command"><%=textListGlobal[SESS_LANGUAGE][6]%></a></td>
                      </tr>
                    </table>
					<%}%>
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
<!-- #EndTemplate --></html>
