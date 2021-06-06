<%@ page import="com.dimata.gui.jsp.ControlList,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.entity.warehouse.MatReceiveItem,
                 com.dimata.posbo.entity.warehouse.MatReceive,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.form.search.FrmSrcReportReceive,
                 com.dimata.posbo.session.warehouse.SessReportReceive,
                 com.dimata.posbo.entity.search.SrcReportReceive,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT_BY_RECEIPT); %>
<%@ include file = "../../../main/checkuser.jsp" %>




<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String listTextGlobal[][] = {
	{"LAPORAN PENERIMAAN BARANG ANTAR LOKASI PER INVOICE","Penerimaan","Dari Toko/Gudang","Laporan Per Invoice","Pencarian",
	 "Cetak Laporan Penerimaan"," Tidak ada data penerimaan barang...","Laporan Penerimaan"},
	{"GOODS RECEIVE BETWEEN LOCATION REPORT BY RECEIPT","Receive","From Store/Warehouse","By Receipt Report","Search",
	 "Print Receive Report","There is no goods receive data ...","Receive Report"}
};

public static final String listTextTitle[][] = {
	{"Periode"," s/d ", "Lokasi"},
	{"Period"," to ","Location"}
};

public static final String listTextHeaderMain[][] = {
	{"No.","Nota","Tanggal","Keterangan","Rincian Item","Total Beli"},
	{"No.","Receipt","Date","Remark","Detail Item","Total Buying"}
};

public static final String listTextHeaderItem[][] = {
	{"SKU","Nama Barang","Qty","Satuan","Harga Beli","Total Beli"},
	{"SKU","Goods Name","Qty","Unit","Buying Price","Total Buying"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"NO","SKU","NAMA BARANG","QTY","BELI","TOTAL BELI","HARGA JUAL","TOTAL JUAL"},
	{"NO","CODE","NAME","QTY","COST","TOTAL COST","PRICE","TOTAL PRICE"}
};

public String drawListItem(int language, Vector objectClass) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(listTextHeaderItem[language][0],"15%");
		ctrlist.addHeader(listTextHeaderItem[language][1],"30%");
		ctrlist.addHeader(listTextHeaderItem[language][2],"12%");
		ctrlist.addHeader(listTextHeaderItem[language][3],"10%");
		ctrlist.addHeader(listTextHeaderItem[language][4],"15%");
		ctrlist.addHeader(listTextHeaderItem[language][5],"18%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double grandTotalBeli = 0;
		double grandTotalHpp = 0;
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceiveItem objMatReceiveItem = (MatReceiveItem)vt.get(0);
			Material objMaterial = (Material)vt.get(1);
			Unit objUnit = (Unit)vt.get(2);
			Category objCategory = (Category)vt.get(3);
			
			Vector rowx = new Vector();
			rowx.add(objMaterial.getSku());
			rowx.add(objMaterial.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReceiveItem.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+objUnit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReceiveItem.getCost())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReceiveItem.getTotal())+"</div>");
			
			lstData.add(rowx);
		}
		
		result = (ctrlist.draw());
	}
	return result;
}

public Vector drawList(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(listTextHeaderMain[language][0],"3%");
		ctrlist.addHeader(listTextHeaderMain[language][1],"15%");
		ctrlist.addHeader(listTextHeaderMain[language][2],"8%");
		ctrlist.addHeader(listTextHeaderMain[language][3],"10%");
		ctrlist.addHeader(listTextHeaderMain[language][4],"54%");
		ctrlist.addHeader(listTextHeaderMain[language][5],"10%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double grandTotalBeli = 0;
		double grandTotalHpp = 0;
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive objMatReceive = (MatReceive)vt.get(0);
			Location objLocation = (Location)vt.get(1);
			String totalRcv = (String)vt.get(2);
			
			/** get list item */
			Vector recordsItem = SessReportReceive.getReportReceiveInternalItem(objMatReceive.getOID());
			
			Vector rowx = new Vector();
			rowx.add(String.valueOf(i+1)+".");
			rowx.add(objMatReceive.getRecCode());
			rowx.add("<div align=\"center\">"+Formater.formatDate(objMatReceive.getReceiveDate(), "dd-MM-yyyy")+"</div>");
			rowx.add(objMatReceive.getRemark());
			rowx.add(drawListItem(language, recordsItem));
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(Double.parseDouble(totalRcv))+"</div>");
			
			grandTotalBeli += Double.parseDouble(totalRcv);
			
			lstData.add(rowx);
		}
		
		result.add(ctrlist.draw());
		result.add(FRMHandler.userFormatStringDecimal(grandTotalBeli));
	}
	else{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+listTextGlobal[language][6]+"</div>");
		result.add("");
	}
	return result;
}
%>

<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcReportReceive srcReportReceive = new SrcReportReceive();
SessReportReceive sessReportReceive = new SessReportReceive();
FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	 try { 
		srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE); 
		if (srcReportReceive == null) srcReportReceive = new SrcReportReceive();
	 }
	 catch(Exception e) { 
		srcReportReceive = new SrcReportReceive();
	 }
}
else {
	 frmSrcReportReceive.requestEntityObject(srcReportReceive);
	 session.putValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE, srcReportReceive);
}

/**
* get vectSize, start and data to be display in this page
*/
//Vector records = sessReportReceive.getReportReceiveInternal(srcReportReceive);
//vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
	//start = ctrlReportReceive.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
*/

Vector records = sessReportReceive.getReportReceiveInternalMain(srcReportReceive);
Vector vctDrawList = drawList(SESS_LANGUAGE,records);
String strDrawlist = (String)vctDrawList.get(0);
String strGrandTotalBeli = (String)vctDrawList.get(1);

Location location = new Location();
if (srcReportReceive.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportReceive.getLocationId());
	}
	catch(Exception e) {
	}
}

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdEdit(oid) {
}

function cmdListFirst() {
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportreceiveinternalinvoice_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev() {
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportreceiveinternalinvoice_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext() {
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportreceiveinternalinvoice_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast() {
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportreceiveinternalinvoice_list.jsp";
	document.frm_reportsale.submit();
}

function cmdBack() {
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportreceiveinternalinvoice.jsp";
	document.frm_reportsale.submit();
}

function printForm() {
	//window.open("reportreceiveinternalinvoice_form_print.jsp","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	window.open("buff_pdf_report_receive_internal_inv.jsp", "RECEIVE_INTERNAL_DETAIL_REPORT_PDF");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
				  	<h4><strong><%=listTextGlobal[SESS_LANGUAGE][0]%></strong></h3>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td class="command" width="9%"><strong><%=(listTextTitle[SESS_LANGUAGE][0]).toUpperCase()%></strong></td>
				  <td class="command" width="1%"><strong>:</strong></td>
				  <td class="command" width="90%"><%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%><%=listTextTitle[SESS_LANGUAGE][1]%><%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td class="command"><strong><%=(listTextTitle[SESS_LANGUAGE][2]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=strDrawlist%></td>
                </tr>
				<% if(records.size() != 0) { //kondisi ada data %>
				<tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command">&nbsp;</td>
                </tr>
				<tr align="left" valign="top">
                  <td colspan="3">
				  	<table width="100%" cellspacing="0" cellpadding="3">
					  <tr align="right" valign="top"> 
					    <td width="85%" align="right"><strong>GRAND <%=listTextHeaderMain[SESS_LANGUAGE][5].toUpperCase()%></strong></td>
						<td width="1%" align="right"><strong>:</strong></td>
						<td width="14%" align="right"><strong><%=strGrandTotalBeli%></strong></td>
					  </tr>
					</table>
				  </td>
                </tr>
				<% } //kondisi ada data %>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%">
						  <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="90%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][7],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
						</td>
						<% if(records.size() != 0) { //kondisi ada data %>
                        <td width="20%">
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="95%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm()" class="command"><i class="fa fa-print">&nbsp;<%=listTextGlobal[SESS_LANGUAGE][5]%></i></a></td>
                            </tr>
                          </table>
						</td>
						<% } //kondisi ada data%>
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
