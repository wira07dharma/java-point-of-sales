<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.MatCurrency,
                   com.dimata.posbo.entity.masterdata.PstMaterial,
                   com.dimata.posbo.form.warehouse.CtrlMatConReceive,
                   com.dimata.posbo.form.warehouse.FrmMatConReceive,
                   com.dimata.posbo.entity.warehouse.*" %>
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
<%@ page import="com.dimata.posbo.session.masterdata.SessPosting"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Konsinyasi","Dari Consignee","Pencarian","Daftar","Edit","Tidak ada data penerimaan..."},
	{"Consigment","From Consignee","Search","List","Edit","Receive","No receive data available..."}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = 
{
	{"Nomor","Lokasi Consignee","Tanggal","Terima Dari","Status","Keterangan","Nomor Kirim","Waktu"},
	{"Number","Consignee Location","Date","Receive From","Status","Remark","Dispatch Number","Time"}	
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = 
{
	{"No","Sku","Nama Barang","Kadaluarsa","Unit","HPP","Harga Jual","Mata Uang","Qty","Total"},
	{"No","Code","Name","Expired Date","Unit","Cost","Sell Price","Currency","Qty","Total"}
};

public static final String textListTitleHeader[][] = 
{
	{"Toko","Terima Barang > Dari Gudang","Terima Barang","Tidak ada data penerimaan ..","Penerimaan Barang"},
	{"Store","Goods Receive > from Warehouse","Goods Receive","No receive data available ..","Goods Receive"}
};

/**
* this method used to list all po item
*/
public Vector drawListRecItem(int language,Vector objectClass,int start,boolean privManageData, int tranUsedPriceHpp)
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
		ctrlist.addHeader(textListOrderItem[language][0],"5%");
		ctrlist.addHeader(textListOrderItem[language][1],"10%");
		ctrlist.addHeader(textListOrderItem[language][2],"20%");
		ctrlist.addHeader(textListOrderItem[language][3],"10%");
		ctrlist.addHeader(textListOrderItem[language][4],"5%");
		
		if(tranUsedPriceHpp==0)
			ctrlist.addHeader(textListOrderItem[language][5],"10%");
		else	
        	ctrlist.addHeader(textListOrderItem[language][6],"10%");
		
		//ctrlist.addHeader(textListOrderItem[language][7],"5%");
		ctrlist.addHeader(textListOrderItem[language][8],"5%");
		ctrlist.addHeader(textListOrderItem[language][9],"15%");
	
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;
		if(start<0)
		{
			start=0;
		}	
		
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector temp = (Vector)objectClass.get(i);
			MatConReceiveItem recItem = (MatConReceiveItem)temp.get(0);
			Material mat = (Material)temp.get(1);
			Unit unit = (Unit)temp.get(2);
			//MatCurrency matCurrency = (MatCurrency)temp.get(3);
			rowx = new Vector();
			start = start + 1;
			
			rowx.add(""+start+"");
			if(privManageData)
			{
				rowx.add("<a href=\"javascript:editItem('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
			}
			else
			{
				rowx.add(mat.getSku()); 
			}			 
			
			rowx.add(mat.getName());
			rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");			 
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
			//rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
			//rowx.add("<div align=\"center\">"+matCurrency.getCode()+"</div>");

            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstMatConReceiveSerialCode.fieldNames[PstMatConReceiveSerialCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstMatConReceiveSerialCode.getCount(where);
                if(cnt<recItem.getQty()){
                    if(listError.size()==0){
                        listError.add("Please Check :");
                    }
                    listError.add(""+listError.size()+". jumlah Serial kode stok barang '"+mat.getName()+"' tidak sama dengan jumlah qty penerimaan");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }

			//rowx.add("<div align=\"right\">"+String.valueOf(recItem.getQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"</div>");

			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}else{
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
%>


<%
// get request data from current form
int iCommand = FRMQueryString.requestCommand(request);
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");

if(iCommand==Command.SUBMIT){
    try{
        System.out.println(">>> proses posting doc : "+oidReceiveMaterial);
        SessPosting sessPosting = new SessPosting();
        sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}

// initialization of some identifier
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

// purchasing ret code and title
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

// action process
ControlLine ctrLine = new ControlLine();
CtrlMatConReceive ctrlMatConReceive = new CtrlMatConReceive(request);
iErrCode = ctrlMatConReceive.action(iCommand , oidReceiveMaterial);
FrmMatConReceive frmrec = ctrlMatConReceive.getForm();
MatConReceive rec = ctrlMatConReceive.getMatConReceive();
errMsg = ctrlMatConReceive.getMessage();

// generate code of current currency
String priceCode = "Rp.";

// check if document may modified or not 
boolean privManageData = true; 

// list purchase order item
oidReceiveMaterial = rec.getOID();
int recordToGetItem = 50;
String whereClauseItem = ""+PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatConReceiveItem.getCount(whereClauseItem);
Vector listMatConReceiveItem = PstMatConReceiveItem.list(startItem,recordToGetItem,whereClauseItem);

//Fetch Dispatch Material Info
MatConDispatch df = new MatConDispatch();
try {
	df = PstMatConDispatch.fetchExc(rec.getDispatchMaterialId());
}
catch(Exception e) {
}	

if(iCommand==Command.DELETE && iErrCode==0) {
%>
	<jsp:forward page="receive_store_wh_material_list.jsp"> 
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}

/** get list location */
Vector obj_locationid = new Vector(1,1); 
Vector val_locationid = new Vector(1,1);  
Vector key_locationid = new Vector(1,1); 
Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
for(int d=0;d<vt_loc.size();d++){
	Location loc = (Location)vt_loc.get(d);
	val_locationid.add(String.valueOf(loc.getOID()));
	key_locationid.add(loc.getName());
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
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function posting(){
    var x = confirm("Are you sure do Posting ?");
    if(x){
        document.frm_recmaterial.command.value="<%=Command.SUBMIT%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
        document.frm_recmaterial.submit();
        }
}

function gostock(oid){

}

function compare(){
	var dt = document.frm_recmaterial.<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
	var mn = document.frm_recmaterial.<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
	var yy = document.frm_recmaterial.<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
	var dt = new Date(yy,mn-1,dt);	
	var bool = new Boolean(compareDate(dt));
	return bool;
}

function cmdSave()
{
	<%	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>	
		<%if(privManageData){%>
		document.frm_recmaterial.command.value="<%=Command.SAVE%>"; 
		<%}else{%>
		document.frm_recmaterial.command.value="<%=Command.EDIT%>"; 	
		<%}%>
		document.frm_recmaterial.prev_command.value="<%=prevCommand%>";	
		document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
		var noInv = document.frm_recmaterial.txt_dfnumber.value;	
		if(noInv!=''){
			if(compare()==true)
				document.frm_recmaterial.submit();
		}else{
			alert('No invoice tidak boleh kosong!!!');
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
	document.frm_recmaterial.command.value="<%=Command.ASK%>"; 
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";	
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
} 

function cmdCancel(){
	document.frm_recmaterial.command.value="<%=Command.CANCEL%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";	
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
} 

function cmdConfirmDelete(oid){
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";	
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";		
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp"; 
	document.frm_recmaterial.submit();
}  

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>"; 
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";	
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdSelectDF()
{
	var strvalue  = "dfdosearch.jsp?command=<%=Command.FIRST%>"+
					"&oidFrom="+document.frm_recmaterial.<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_FROM]%>.value+
                    "&oidloc="+document.frm_recmaterial.<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_LOCATION_ID]%>.value
	window.open(strvalue,"material", "height=600,width=700,status=no,toolbar=no,menubar=no,location=no,scrollbars=no");
}

function printForm()
{
	window.open("receive_store_wh_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem()
{
	<%	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>	
		document.frm_recmaterial.command.value="<%=Command.ADD%>"; 
		document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
		if(compareDateForAdd()==true)
			document.frm_recmaterial.submit();
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
	<%	if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED))
		{
	%>	
		document.frm_recmaterial.command.value="<%=Command.EDIT%>"; 
		document.frm_recmaterial.hidden_receive_item_id.value=oid;  
		document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
		document.frm_recmaterial.submit();
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
 function gostock(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.action="rec_df_stockcode.jsp";
    document.frm_recmaterial.submit();
}

function itemList(comm){
	document.frm_recmaterial.command.value=comm; 
	document.frm_recmaterial.prev_command.value=comm;	
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> > <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_recmaterial" method="post" action="">
              <input type="hidden" name="command" value="">
			  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">			  
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">			  			  
              <input type="hidden" name="hidden_receive_item_id" value="">
              <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
              <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_STORE%>">
              <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatConReceive.SOURCE_FROM_DISPATCH%>">
              <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=rec.getDispatchMaterialId()%>">
			  <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_CURRENCY_ID]%>" value="0">
			  <input type="hidden" name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_TRANS_RATE]%>" value="0">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="3"> 
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td> 
                        <td width="54%" align="left"> : <b><%=rec.getRecCode()%></b></td>
                        <td width="13%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="21%"> : 
                          <%=ControlCombo.draw(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_FROM], null, String.valueOf(rec.getReceiveFrom()), val_locationid, key_locationid, "", "formElemen")%>
						</td>  
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td> 
                        <td width="54%" align="left"> : 
                          <%=ControlCombo.draw(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_LOCATION_ID], null, String.valueOf(rec.getLocationId()), val_locationid, key_locationid, "", "formElemen")%>
						</td>
                        <td width="13%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td width="21%"> : <input type="text" readonly name="txt_dfnumber"  value="<%= df.getDispatchCode() %>" class="formElemen" size="25">
                          <%if(listMatConReceiveItem.size()==0){%><a href="javascript:cmdSelectDF()">CHK</a><%}%>
                      </tr>
                      <tr>
                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td> 
                        <td width="54%" align="left"> : <%=ControlDate.drawDateWithStyle(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date() : rec.getReceiveDate(), 1, -5, "formElemen", "")%></td>
                        <td width="13%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="21%" rowspan="3" valign="top"> : <textarea name="<%=FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2"><%=rec.getRemark()%></textarea>
                        </td>
                      </tr>
                      <tr>
                        <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td align="left">: <%=ControlDate.drawTimeSec(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date(): rec.getReceiveDate(),"formElemen")%></td>
                        <td align="right" valign="bottom">&nbsp;</td>
                      </tr>
                      <tr>
                        <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                        <td align="left">: 
						 <%
							Vector obj_status = new Vector(1,1);
							Vector val_status = new Vector(1,1);
							Vector key_status = new Vector(1,1);

							val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
							key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
							
							//add by fitra
						  val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
							if(listMatConReceiveItem.size()>0){
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
						  <%=ControlCombo.draw(FrmMatConReceive.fieldNames[FrmMatConReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"","formElemen")%>
                          <%}%>
                        </td>
                        <td align="right" valign="bottom">&nbsp;</td>
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
                            Vector list = drawListRecItem(SESS_LANGUAGE,listMatConReceiveItem,startItem,privManageData,tranUsedPriceHpp);
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
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%> </span> </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> 
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr> 
                              <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                              <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                <%if(listMatConReceiveItem!=null && listMatConReceiveItem.size()>0){%>
                <tr> 
                  <td colspan="2" valign="top"> </td>
                  <td width="30%" valign="top"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="54%"> 
                          <div align="right"><%="TOTAL  : "+recCode%></div>
                        </td>
                        <td width="5%"> 
                          <div align="right"></div>
                        </td>
                        <td width="41%"> 
                          <div align="right"> 
                            <%
						  String whereItem = ""+PstMatConReceiveItem.fieldNames[PstMatConReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
						  out.println(Formater.formatNumber(PstMatConReceiveItem.getTotal(whereItem),"##,###.00"));
						  %>
                          </div>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
                <tr> 
                  <td colspan="3"><span class="fielderror">
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
                <tr> 
                  <td colspan="3"> 
                    <%
						ctrLine.setLocationImg(approot+"/images");
						
						// set image alternative caption 
						ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
						ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");							
						ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));							
						ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));																					
						
						ctrLine.initDefault();					
						ctrLine.setTableWidth("100%"); 
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
						
						out.println(ctrLine.drawImage(iCommand,iErrCode,errMsg));					
						%>
                  </td>
                </tr>
                <%if(listMatConReceiveItem!=null && listMatConReceiveItem.size()>0){%>
                <tr> 
                  <td colspan="2" valign="top"> 
                    <table width="50%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" alt="Print <%=recTitle%>" border="0"></a></td>
                        <td width="85%" nowrap>&nbsp; <a href="javascript:printForm()" class="command">Print <%=textListGlobal[SESS_LANGUAGE][0]%></a></td>
                          <%
                            if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
                          %>
                          <td width="5%" valign="top"><a href="javascript:posting()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" alt="Posting Receive" border="0"></a></td>
                          <td width="5%" nowrap>&nbsp; <a href="javascript:posting()" class="command">Posting Receive</a></td>
                          <%}%>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%}%>
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
