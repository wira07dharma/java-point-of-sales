<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.util.Command,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.posbo.form.warehouse.FrmMatConDispatchItem,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.form.warehouse.FrmMatConDispatch,
                   com.dimata.posbo.form.warehouse.CtrlMatConDispatch,
                   com.dimata.posbo.form.warehouse.CtrlMatConDispatchItem" %>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
				   
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){
	privAdd=false;
	privUpdate=false;
	privDelete=false;
}
%>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Pengiriman Barang"},
	{"Consigment","Goods Transfer"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Consignor"},
	{"Number","Location","Destination","Date","Status","Remark","Consignor Invoice"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Qty","HPP","Harga Jual","Total"},
	{"No","Code","Goods Name","Unit","Qty","Avg.Cost","Retail Price","Total"}
};


public double getPriceCost(Vector list, long oid){
    double cost = 0.00;
    if(list.size()>0){
        for(int k=0;k<list.size();k++){
            MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(k);
            if(matReceiveItem.getMaterialId()==oid){
                cost = matReceiveItem.getCost();
                break;
            }
        }
    }
    return cost;
}
/**
* this method used to maintain dfList
*/
public Vector drawListDfItem(int language,int iCommand,FrmMatConDispatchItem frmObject,
                             MatConDispatchItem objEntity,Vector objectClass,
                             long dfItemId,int start, String invoiceNumber, int tranUsedPriceHpp)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListOrderItem[language][0],"3%");
	ctrlist.addHeader(textListOrderItem[language][1],"10%");
	ctrlist.addHeader(textListOrderItem[language][2],"40%");
	ctrlist.addHeader(textListOrderItem[language][3],"5%");
    if(tranUsedPriceHpp==0)
	    ctrlist.addHeader(textListOrderItem[language][5],"10%");
    else
        ctrlist.addHeader(textListOrderItem[language][6],"10%");

    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    ctrlist.addHeader(textListOrderItem[language][7],"10%");
    //ctrlist.addHeader(textListOrderItem[language][6],"10%");

    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);

	Vector lstData = ctrlist.getData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	ctrlist.setLinkRow(1);
	int index = -1;
	if(start<0)
	   start=0;


    /**
     * get data receive for get price cost
     */
   /* String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoiceNumber+"'";
    Vector list = PstMatReceive.list(0,0,whereClause,"");
    Vector listItem = new Vector(1,1);
    if(list!=null && list.size()>0){
        MatReceive matReceive = (MatReceive)list.get(0);
        whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceive.getOID();
        listItem = PstMatReceiveItem.list(0,0,whereClause,"");
    }*/

    for(int i=0; i<objectClass.size(); i++)
	{
		 Vector temp = (Vector)objectClass.get(i);
		 MatConDispatchItem dfItem = (MatConDispatchItem)temp.get(0);
		 Material mat = (Material)temp.get(1);
		 Unit unit = (Unit)temp.get(2);
		 rowx = new Vector();
		 start = start + 1;

        System.out.println("hpp barang");
       // double cost = getPriceCost(listItem,dfItem.getMaterialId());

		if (dfItemId == dfItem.getOID()) index = i;
		if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
		{
			rowx.add(""+start);
			rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+dfItem.getMaterialId()+
				"\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
			rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\"  readOnly>");
			rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+dfItem.getUnitId()+
				"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\"  readOnly>");
			rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"\" class=\"hiddenText\" readOnly></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal()\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");

            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"hiddenText\" readOnly></div>");

            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"</div>");
            // rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
		}
		else
		{
			rowx.add(""+start+"");
			rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
			rowx.add(mat.getName());
			rowx.add(unit.getCode());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"</div>");

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstMatConDispatchSerialCode.fieldNames[PstMatConDispatchSerialCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                int cnt = PstMatConDispatchSerialCode.getCount(where);
                if(cnt<dfItem.getQty()){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty pengiriman");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");
            }

            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"</div>");
			//rowx.add("<div align=\"right\">"+String.valueOf(dfItem.getQty())+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
		}
		lstData.add(rowx);
	}

	rowx = new Vector();
	if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0))
	{
		rowx.add(""+(start+1));
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
			"\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
		rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
		rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" class=\"hiddenText\" value=\""+""+
			"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
		rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal()\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");

        //rowx.add("<div align=\"right\"></div>");
        //rowx.add("<div align=\"right\"></div>");
		
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
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");
long oidDispatchMaterialItem = FRMQueryString.requestLong(request,"hidden_dispatch_item_id");

long oidReceiveId = FRMQueryString.requestLong(request,"hidden_receive_item_id");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String dfCode = ""; //i_pstDocType.getDocCode(docType);
String dfTitle = "Transfer Barang"; //i_pstDocType.getDocTitle(docType);
String dfItemTitle = dfTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));


/**
* process on df main
*/
CtrlMatConDispatch ctrlMatConDispatch = new CtrlMatConDispatch(request);
iErrCode = ctrlMatConDispatch.action(Command.EDIT,oidDispatchMaterial);
FrmMatConDispatch frmMatConDispatch = ctrlMatConDispatch.getForm();
MatConDispatch df = ctrlMatConDispatch.getMatConDispatch();

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not
*/
boolean privManageData = true;

ControlLine ctrLine = new ControlLine();
CtrlMatConDispatchItem ctrlMatConDispatchItem = new CtrlMatConDispatchItem(request);
ctrlMatConDispatchItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatConDispatchItem.action(iCommand,oidDispatchMaterialItem,oidDispatchMaterial);
FrmMatConDispatchItem frmMatConDispatchItem = ctrlMatConDispatchItem.getForm();
MatConDispatchItem dfItem = ctrlMatConDispatchItem.getMatConDispatchItem();
msgString = ctrlMatConDispatchItem.getMessage();

int comm = iCommand;
if(iErrCode!=0 && dfItem.getOID()!=0 && iCommand==Command.SAVE){
	comm = Command.EDIT;
}else if(iErrCode!=0 && dfItem.getOID()==0 && iCommand==Command.SAVE){
	comm = Command.ADD;
}

String whereClauseItem = PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatConDispatchItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatConDispatchItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

Vector listMatConDispatchItem = PstMatConDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);
if(listMatConDispatchItem.size()<1 && startItem>0)
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
	 listMatConDispatchItem = PstMatConDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);
}
%>

<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.hidden_dispatch_id.value=oid;
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_dispatch_item_id.value=oid;
    document.frm_matdispatch.action="df_stockcode.jsp";
    document.frm_matdispatch.submit();
}

function cmdAdd()
{
	document.frm_matdispatch.hidden_dispatch_item_id.value="0";
	document.frm_matdispatch.command.value="<%=Command.ADD%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	if(compareDateForAdd()==true)
		document.frm_matdispatch.submit();
}

function cmdEdit(oidDispatchMaterialItem)
{
	document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdAsk(oidDispatchMaterialItem)
{
	document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.ASK%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdSave(){
	document.frm_matdispatch.command.value="<%=Command.SAVE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oidDispatchMaterialItem)
{
	document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdCancel(oidDispatchMaterialItem)
{
	document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack()
{
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.start_item.value = 0;
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function sumPrice()
{
}

function cntTotal()
{
	var qty = cleanNumberInt(document.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
	var price = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_HPP]%>.value,guiDigitGroup,',');
    //alert("price : "+price);
	if(price==""){
		price = 0;
	}
    /*
	var qty = document.frm_recmaterial.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_QTY]%>.value;
	var price = document.frm_recmaterial.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_HPP]%>.value;
    */
	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = price * qty;
        //alert("amount : "+amount);
        if(isNaN(amount)){
            amount = "0";
        }
		document.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
	}
	else
	{
		document.frm_matdispatch.<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_QTY]%>.focus();
	}
}

function cmdCheck()
{
	var strvalue  = "dfwhdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&location_id=<%=df.getLocationId()%>"+
					"&hidden_dispatch_id=<%=oidDispatchMaterial%>"+
					"&mat_code="+document.frm_matdispatch.matCode.value;
	window.open(strvalue,"material", "height=500,width=700,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cmdListFirst()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListPrev()
{
	document.frm_matdispatch.command.value="<%=Command.PREV%>";
	document.frm_matdispatch.prev_command.value="<%=Command.PREV%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListNext()
{
	document.frm_matdispatch.command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.prev_command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdListLast()
{
	document.frm_matdispatch.command.value="<%=Command.LAST%>";
	document.frm_matdispatch.prev_command.value="<%=Command.LAST%>";
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

function cmdBackList()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_matdispatch" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
			  <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveId%>">
			  <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">
              <input type="hidden" name="<%=FrmMatConDispatchItem.fieldNames[FrmMatConDispatchItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" class="title">
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td width="17%" align="right" valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="25%">: <b>
                          <%
						  if(df.getDispatchCode()!="" && iErrCode==0)
						  {
							out.println(df.getDispatchCode());
						  }
						  else
						  {
						  	out.println("");
						  }
						  %>
                        </b></td>
                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="31%">:
                            <%
							Vector obj_locationid = new Vector(1,1);
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
                                //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
							Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
							for(int d=0;d<vt_loc.size();d++)
							{
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+df.getLocationId(); //selected on combo box
						  %>
                            <%=ControlCombo.draw(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>
                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="17%" rowspan="3" align="right" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate()==null) ? new Date() : df.getDispatchDate(), 1, -5, "formElemen", "")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td>:
                            <%
							Vector obj_locationid1 = new Vector(1,1);
							Vector val_locationid1 = new Vector(1,1);
							Vector key_locationid1 = new Vector(1,1);
							String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
							String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
							Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
							for(int d = 0; d < vt_loc1.size(); d++)
							{
								Location loc1 = (Location)vt_loc1.get(d);
								val_locationid1.add(""+loc1.getOID()+"");
								key_locationid1.add(loc1.getName());
							}
							String select_locationid1 = ""+df.getDispatchTo(); //selected on combo box
						  %>
                            <%=ControlCombo.draw(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
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
                                    Vector listError = new Vector(1,1);
							  try
								{
								%>
                              <td height="22" valign="middle" colspan="3"> <%

							  Vector list = drawListDfItem(SESS_LANGUAGE,comm,frmMatConDispatchItem, dfItem,listMatConDispatchItem,oidDispatchMaterialItem,startItem,df.getInvoiceSupplier(),tranUsedPriceHpp);
                                        out.println(""+list.get(0));
                                        listError = (Vector)list.get(1);
							  %> </td>
                              <%
								}
								catch(Exception e)
								{
									System.out.println(e);
								}
								%>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="8" align="left" colspan="3" class="command">
                                <span class="command">
                                <%
								int cmd = 0;
								if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST){
									cmd =iCommand;
								}else{
								    if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
								    else
										cmd =prevCommand;
							    }
                                ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));
								%>
                                </span> </td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="22" colspan="3" valign="middle" class="errfont"><%
				  	for(int k=0;k<listError.size();k++){
						if(k==0)
							out.println(listError.get(k)+"<br>");
						else
							out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
					}
				  %></td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="22" colspan="3" valign="middle">
                                <%
								ctrLine.setLocationImg(approot+"/images");

								// set image alternative caption
								ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));

								ctrLine.initDefault();
								ctrLine.setTableWidth("65%");
								String scomDel = "javascript:cmdAsk('"+oidDispatchMaterialItem+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidDispatchMaterialItem+"')";
								String scancel = "javascript:cmdEdit('"+oidDispatchMaterialItem+"')";
								ctrLine.setCommandStyle("command");
								ctrLine.setColCommStyle("command");

								// set command caption
								ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));
								ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_DELETE,true));
								ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));


								if (privDelete){
									ctrLine.setConfirmDelCommand(sconDelCom);
									ctrLine.setDeleteCommand(scomDel);
									ctrLine.setEditCommand(scancel);
								}else{
									ctrLine.setConfirmDelCaption("");
									ctrLine.setDeleteCaption("");
									ctrLine.setEditCaption("");
								}

								if(privAdd == false  && privUpdate == false){
									ctrLine.setSaveCaption("");
								}

								if (privAdd == false){
									ctrLine.setAddCaption("");
								}

								String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
								if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
								%>
                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                    <% if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
									<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
									<% } %>
									<td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
                                  </tr>
                                </table>
                              <%
								}else{
									out.println(strDrawImage);
								}
								%>                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="3">
                        </td>
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

