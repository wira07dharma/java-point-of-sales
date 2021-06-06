<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN_REPORT, AppObjInfo.OBJ_SUPPLIER_RETURN_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data", "LAPORAN RETUR BARANG KE SUPLIER PER SUPLIER", "Periode", "Dari", "s/d", "Lokasi", "Suplier", 
	 "Merk", "Kategori", "Semua", "Laporan Retur", "Cetak Laporan Retur"},
	{"No data available", "GOODS RETURN TO SUPPLIER REPORT BY SUPPLIER", "Period", "From", "to", "Location", "Supplier",
	 "Merk", "Category", "All", "Return Report", "Print Return Report"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY","SATUAN","HARGA BELI","TOTAL RETUR"},
	{"NO","SKU","GOODS NAME","QTY","UNIT","BUYING PRICE","TOTAL RETURN"}
};

public Vector drawList(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][2],"31%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][3],"13%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][5],"13%","0","0");
		ctrlist.addHeader(textListMaterialHeader[language][6],"13%","0","0");
		
		Vector lstData = ctrlist.getData();
		double totalNilaiHpp = 0;
		
		for(int i=0; i<objectClass.size(); i++) {				
			Vector vt = (Vector)objectClass.get(i);
			MatReturnItem matReturnItem = (MatReturnItem)vt.get(0);
			Material material = (Material)vt.get(1);
			Unit objUnit = (Unit)vt.get(2);
			MatReturn objMatReturn = (MatReturn)vt.get(5);
			
			Vector rowx = new Vector();
			rowx.add(String.valueOf(i+1)+".");
			rowx.add(material.getSku());
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReturnItem.getQty())+"</div>");
			rowx.add("<div align=\"right\">"+objUnit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReturnItem.getCost() * objMatReturn.getTransRate())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matReturnItem.getTotal() * objMatReturn.getTransRate())+"</div>");
			
			totalNilaiHpp += (matReturnItem.getTotal() * objMatReturn.getTransRate());
			
			lstData.add(rowx);
		}
		
		result.add(ctrlist.draw());
		result.add(FRMHandler.userFormatStringDecimal(totalNilaiHpp));
	}
	else{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>");
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

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcReportReturn srcReportReturn = new SrcReportReturn();
SessReportReturn sessReportReturn = new SessReportReturn();
FrmSrcReportReturn frmSrcReportReturn = new FrmSrcReportReturn(request, srcReportReturn);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	 try { 
		srcReportReturn = (SrcReportReturn)session.getValue(SessReportReturn.SESS_SRC_REPORT_RETURN); 
		if (srcReportReturn == null) srcReportReturn = new SrcReportReturn();
	 }
	 catch(Exception e) { 
		srcReportReturn = new SrcReportReturn();
	 }
}
else {
	 frmSrcReportReturn.requestEntityObject(srcReportReturn);
	 //karena tidak memakai parameter REASON, maka property ini di-set -1        
	 srcReportReturn.setReturnReason(-1);
	 session.putValue(SessReportReturn.SESS_SRC_REPORT_RETURN, srcReportReturn);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector records = sessReportReturn.getReportReturnTotal(srcReportReturn);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
	//start = ctrlReportReturn.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
*/

Location location = new Location();
if (srcReportReturn.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportReturn.getLocationId());
	}
	catch(Exception exx) {
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][9]+" "+textListGlobal[SESS_LANGUAGE][5]);
}

ContactList contactList = new ContactList();
if (srcReportReturn.getSupplierId() != 0) {
	try	{
		contactList = PstContactList.fetchExc(srcReportReturn.getSupplierId());
	}
	catch(Exception exx) {
	}
}

Vector vctDrawList = drawList(SESS_LANGUAGE, records);
String strDrawList = (String)vctDrawList.get(0);
String strTotalHpp = (String)vctDrawList.get(1);
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack() {
	document.frm_reportreturn.command.value="<%=Command.BACK%>";
	document.frm_reportreturn.action="src_reportreturn.jsp";
	document.frm_reportreturn.submit();
}

function printForm() {
	window.open("buff_pdf_report_return_supp.jsp","reportreturbysupplier");
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportreturn" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" align="center" valign="middle" colspan="2"><h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%" valign="middle"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong></td>
                  <td width="90%" valign="middle"><strong>:</strong> <%=Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][4]%> <%=Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy")%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td valign="middle"><strong><%=textListGlobal[SESS_LANGUAGE][5]%></strong></td>
                  <td><strong>: </strong><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td valign="middle"><strong><%=textListGlobal[SESS_LANGUAGE][6]%></strong></td>
                  <td valign="middle"><strong>: </strong><%=contactList.getCompName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td align="left" valign="middle" colspan="2"><%=strDrawList%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td colspan="2">&nbsp;</td>
                </tr>
				<% if(records.size() > 0) { %>
                <tr align="left" valign="top"> 
                  <td height="22" align="right" valign="middle" colspan="3"> <h4>GRAND 
                    <%=textListMaterialHeader[SESS_LANGUAGE][6]%> :&nbsp;&nbsp;&nbsp;<%=strTotalHpp%></h4></td>
                </tr>
				<% } %>
                <tr align="left" valign="top"> 
                  <td height="18" align="left" valign="top" colspan="2"> <table width="100%" border="0">
                      <tr> 
                        <td width="85%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][10],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][10],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
						</td>
                        <td width="15%"> <%if(records!=null && records.size()>0){%> <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][11]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                            </tr>
                          </table>
                          <%}else{%> &nbsp; <%}%>
						</td>
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
