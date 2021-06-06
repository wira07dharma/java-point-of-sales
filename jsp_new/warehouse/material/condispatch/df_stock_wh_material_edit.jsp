 <%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.form.warehouse.CtrlMatConDispatch,
                   com.dimata.posbo.form.warehouse.FrmMatConDispatch,
                   com.dimata.posbo.entity.warehouse.*" %>
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

<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Pengiriman Barang"},
	{"Consigment","Goods Transfer"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] =
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Consignor","Waktu"},
	{"Number","From Location","Destination","Date","Status","Remark","Consignor Invoice","Time"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No","Sku","Nama Barang","Unit","Qty","HPP","Harga Jual","Total"},
	{"No","Code","Goods Name","Unit","Qty","Avg. Cost","Price","Total"}};

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
* this method used to list all po item
*/
public Vector drawListOrderItem(int language,Vector objectClass,int start, String invoiceNumber,int tranUsedPriceHpp)
{
    Vector list = new Vector(1,1);
	Vector listError = new Vector(1,1);
	String result = "";
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
		ctrlist.addHeader(textListOrderItem[language][2],"40%");
		ctrlist.addHeader(textListOrderItem[language][3],"5%");
		if(tranUsedPriceHpp==0)
			ctrlist.addHeader(textListOrderItem[language][5],"10%");
		else
			ctrlist.addHeader(textListOrderItem[language][6],"10%");
			
        ctrlist.addHeader(textListOrderItem[language][4],"5%");
        ctrlist.addHeader(textListOrderItem[language][7],"10%");

        //ctrlist.addHeader(textListOrderItem[language][6],"10%");

		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
			start=0;

        /**
         * get data receive for get price cost
         */
        /*String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoiceNumber+"'";
        Vector list = PstMatReceive.list(0,0,whereClause,"");
        Vector listItem = new Vector(1,1);
        if(list!=null && list.size()>0){
            MatReceive matReceive = (MatReceive)list.get(0);
            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceive.getOID();
            listItem = PstMatReceiveItem.list(0,0,whereClause,"");
        }*/

		for(int i=0; i<objectClass.size(); i++){
			 Vector temp = (Vector)objectClass.get(i);
			 MatConDispatchItem dfItem = (MatConDispatchItem)temp.get(0);
			 Material mat = (Material)temp.get(1);
			 Unit unit = (Unit)temp.get(2);
			 rowx = new Vector();
			 start = start + 1;

            //double cost = getPriceCost(listItem,dfItem.getMaterialId());

			rowx.add(""+start+"");
			rowx.add("<a href=\"javascript:editItem('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
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
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");

			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item transfer barang ...</div>";
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF);
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
long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");

/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

/**
* dispatch code and title
*/
String dfCode = "";//i_pstDocType.getDocCode(docType);
String dfTitle = "Transfer Barang";//i_pstDocType.getDocTitle(docType);
String dfItemTitle = dfTitle + " Item";

/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatConDispatch ctrlMatConDispatch = new CtrlMatConDispatch(request);
iErrCode = ctrlMatConDispatch.action(iCommand , oidDispatchMaterial);
FrmMatConDispatch frmdf = ctrlMatConDispatch.getForm();
MatConDispatch df = ctrlMatConDispatch.getMatConDispatch();
errMsg = ctrlMatConDispatch.getMessage();

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
String priceCode = "Rp.";

/**
* list purchase order item
*/
oidDispatchMaterial = df.getOID();
int recordToGetItem = 10;
int vectSizeItem = PstMatConDispatchItem.getCount(PstMatConDispatchItem.fieldNames[PstMatConDispatchItem.FLD_DISPATCH_MATERIAL_ID] + " = " + oidDispatchMaterial);
Vector listMatConDispatchItem = PstMatConDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);


if(iCommand==Command.DELETE && iErrCode==0)
{
%>
	<jsp:forward page="df_stock_wh_material_list.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
    double total = PstMatConDispatchItem.getTotalTransfer(oidDispatchMaterial);
%>
<!-- End of Jsp Block -->

<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function gostock(oid){
    document.frm_matdispatch.command.value="<%=Command.EDIT%>";
    document.frm_matdispatch.hidden_dispatch_item_id.value=oid;
    document.frm_matdispatch.action="df_stockcode.jsp";
    document.frm_matdispatch.submit();
}

function compare(){
	var dt = document.frm_matdispatch.<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE]%>_dy.value;
	var mn = document.frm_matdispatch.<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE]%>_mn.value;
	var yy = document.frm_matdispatch.<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave()
{
	<%	if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
		//var invSupp = document.frm_matdispatch.<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
		//if(invSupp!=''){
			document.frm_matdispatch.command.value="<%=Command.SAVE%>";
			document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
			document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
			if(compare()==true)
				document.frm_matdispatch.submit();
		//}else{
		//	alert("Nota supplier harus diisi");
		//}
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
	<%	if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
    {
	%>
	document.frm_matdispatch.command.value="<%=Command.ASK%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
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
	document.frm_matdispatch.command.value="<%=Command.CANCEL%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdConfirmDelete(oid){
	document.frm_matdispatch.command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack(){
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.prev_command.value="<%=prevCommand%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
}

function printForm()
{
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.TransferConPrintPDF?hidden_dispatch_id=<%=oidDispatchMaterial%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>");
    //window.open("df_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function findInvoice()
{
	window.open("df_wh_material_receive.jsp","invoice_supplier","scrollbars=yes,height=500,width=700,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED)
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.ADD%>";
		document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
		if(compareDateForAdd()==true)
			document.frm_matdispatch.submit();
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
	<%	if ((df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (df.getDispatchStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>
		document.frm_matdispatch.command.value="<%=Command.EDIT%>";
		document.frm_matdispatch.hidden_dispatch_item_id.value=oid;
		document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
		document.frm_matdispatch.submit();
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
	document.frm_matdispatch.command.value=comm;
	document.frm_matdispatch.prev_command.value=comm;
	document.frm_matdispatch.action="df_stock_wh_material_item.jsp";
	document.frm_matdispatch.submit();
}

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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</script>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>','<%=approot%>')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td height="25" ID="TOPTITLE">      <%@ include file = "../../../main/header.jsp" %>      </td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU">      <%@ include file = "../../../main/mnmain.jsp" %>      </td>
  </tr>
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader">&nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%></td>
        </tr>
        <tr>
          <td>            
		  <form name="frm_matdispatch" method="post" action="">
 <%
try
{
%>
              <input type="hidden" name="command" value="">
		      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="">
              <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE%>">
              <input type="hidden" name="<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%=df.getDispatchCode()%>">
              <input type="hidden" name="<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatConDispatch.FLD_TYPE_DISPATCH_LOCATION_WAREHOUSE%>">
              <table width="100%" border="0">
                <tr>
                  <td colspan="3">
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
						  %></b></td>
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
                        <td width="17%" rowspan="3" align="right" valign="top"><textarea name="<%=FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>
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
                        <td><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td>: <%=ControlDate.drawTimeSec(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate()==null) ? new Date(): df.getDispatchDate(),"formElemen")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td>: <%
                        Vector obj_status = new Vector(1,1);
                        Vector val_status = new Vector(1,1);
                        Vector key_status = new Vector(1,1);

                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
						
						//add by fitra
						  val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                        if(listMatConDispatchItem.size()>0){
                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                        }
                        String select_status = ""+df.getDispatchStatus();

                        if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                        }else if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                        }else{
						  %>
                                <%=ControlCombo.draw(FrmMatConDispatch.fieldNames[FrmMatConDispatch.FRM_FIELD_DISPATCH_STATUS],null,select_status,val_status,key_status,"","formElemen")%>
                                <%}%>
                                </td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                </td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%
						Vector list = drawListOrderItem(SESS_LANGUAGE,listMatConDispatchItem,startItem, df.getInvoiceSupplier(),tranUsedPriceHpp);
						out.println(""+list.get(0));
						Vector listError = (Vector)list.get(1);
						%></td>
                      </tr>
                      <%if(oidDispatchMaterial!=0){%>
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
                        <td colspan="3" align="right" valign="top"><span class="bullettitle2">Total :&nbsp;&nbsp;&nbsp;&nbsp;</span> <strong><%=FRMHandler.userFormatStringDecimal(total)%></strong></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" colspan="3" valign="middle" class="errfont"><%
				  	for(int k=0;k<listError.size();k++){
						if(k==0)
							out.println(listError.get(k)+"<br>");
						else
							out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
					}
				  %>                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                            <%
                                if(privAdd==true){
                            %>
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <% if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
							  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
							  <% } %>
                            </tr>
                          </table>
                            <%}%>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">&nbsp;</td>
                  <td width="30%">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="2" valign="top">&nbsp;</td>
                  <td width="30%">&nbsp;</td>
                </tr>
                <tr>
                  <td colspan="3">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td width="77%">
                          <%
					ctrLine.setLocationImg(approot+"/images");

					// set image alternative caption
					ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_CANCEL,false));

					ctrLine.initDefault();
					ctrLine.setTableWidth("80%");
					String scomDel = "javascript:cmdAsk('"+oidDispatchMaterial+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidDispatchMaterial+"')";
					String scancel = "javascript:cmdEdit('"+oidDispatchMaterial+"')";
					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");

					// set command caption
					ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SAVE,true));
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ASK,true));
					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_CANCEL,false));

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

					if(iCommand==Command.SAVE && frmdf.errorSize()==0){
						iCommand=Command.EDIT;
					}

					%>
                          <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%> </td>
                        <td width="23%">
                          <%if(listMatConDispatchItem!=null && listMatConDispatchItem.size()>0){%>
					      <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td width="5%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" >Print
                                <%=dfTitle%></a></td>
                            </tr>
                          </table>
					      <%}%>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
  <%
}
catch(Exception e)
{
	System.out.println(e);
}
%>
            </form></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20">      <%@ include file = "../../../main/footer.jsp" %>      </td>
  </tr>
</table>
</body>
</html>

