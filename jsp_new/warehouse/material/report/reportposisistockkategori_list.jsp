<%@ page import="com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportStock,
                 com.dimata.posbo.form.search.FrmSrcReportStock,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
				 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.Material,
				 com.dimata.posbo.entity.masterdata.MaterialStock,
				 com.dimata.posbo.entity.masterdata.Unit,
				 com.dimata.posbo.entity.masterdata.Merk,
				 com.dimata.posbo.entity.masterdata.PstMerk,
				 com.dimata.posbo.entity.masterdata.Category,
				 com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Tidak ada data", "LAPORAN POSISI STOK PER KATEGORI", "Dari", "s/d", "Periode", "Lokasi", "Merk", "Kategori", "Semua",
	 "Laporan Posisi Stock", "Cetak Laporan Posisi Stock"},
	{"No data available", "STOCK POSITION REPORT BY CATEGORY", "From", "to", "Periode", "Location", "Merk", "Category", "All",
	 "Stock Potition Report", "Print Stock Potition Report"}
};

public static final String textListTitleTable[][] = {
	{"NO","SKU","NAMA BARANG","HPP","UNIT","QUANTITY","AWAL","OPNAME","TERIMA","TRANSFER","RETUR","JUAL","SALDO","NILAI STOK"},
	{"NO","SKU","GOODS NAME","COGS","UNIT","QUANTITY","BEGINNING","RECEIVE","TRANSFER","RETURN","SALE","SALDO","STOCK VALUE"}
};

public Vector drawList(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("99%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListTitleTable[language][0],"3%","2","0");
		ctrlist.addHeader(textListTitleTable[language][1],"10%","2","0");
		ctrlist.addHeader(textListTitleTable[language][2],"16%","2","0");
		ctrlist.addHeader(textListTitleTable[language][3],"7%","2","0");
		ctrlist.addHeader(textListTitleTable[language][4],"5%","2","0");
		ctrlist.addHeader(textListTitleTable[language][5],"0%","0","7");
		ctrlist.addHeader(textListTitleTable[language][6],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][7],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][8],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][9],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][10],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][11],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][12],"7%","0","0");
		ctrlist.addHeader(textListTitleTable[language][13],"11%","2","0");
		
		Vector lstData = ctrlist.getData();
		double totalNilaiStock = 0;
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);			
			Material material = (Material)vt.get(0);
			MaterialStock materialStock = (MaterialStock)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			
			Vector rowx = new Vector();
			rowx.add(String.valueOf(i+1)+".");
			rowx.add(material.getSku());
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getOpnameQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyIn())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyOut())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyMin())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getSaleQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getClosingQty())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice() * materialStock.getClosingQty())+"</div>");
			
			totalNilaiStock += (material.getAveragePrice() * materialStock.getClosingQty());
			
			lstData.add(rowx);
		}
		
		result.add(ctrlist.drawList());
		result.add(FRMHandler.userFormatStringDecimal(totalNilaiStock));
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
int type = FRMQueryString.requestInt(request, "type");

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
SrcReportStock srcReportStock = new SrcReportStock();
SessReportStock sessReportStock = new SessReportStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	 try{
		srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK); 
		if (srcReportStock == null) srcReportStock = new SrcReportStock();
	 }catch(Exception e){
		srcReportStock = new SrcReportStock();
	 }
}else{
	 frmSrcReportStock.requestEntityObject(srcReportStock);
	 session.putValue(SessReportStock.SESS_SRC_REPORT_STOCK, srcReportStock);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector vctReportStock = sessReportStock.reportPosisiStock(false,SessReportStock.TYPE_REPORT_POSISI_CATEGORY,srcReportStock,SESS_LANGUAGE,false,0,0); 
frmSrcReportStock.requestEntityObject(srcReportStock);
session.putValue(SessReportStock.SESS_SRC_REPORT_STOCK, srcReportStock);

/** 3 baris perintah dibawah ini digunakan, jika veactor "records" tidak berisi tag-tag HTML */
Vector vctDrawList = drawList(SESS_LANGUAGE, vctReportStock);
String strDrawList = (String)vctDrawList.get(0);
String strNilaiStock = (String)vctDrawList.get(1);


Location location = new Location();
if (srcReportStock.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportStock.getLocationId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][5]);
}

Merk merk = new Merk();
if(srcReportStock.getMerkId() != 0) {
	try {
		merk = PstMerk.fetchExc(srcReportStock.getMerkId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}
else {
	merk.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][6]);
}

Category category = new Category();
if(srcReportStock.getCategoryId() != 0) {
	try {
		category = PstCategory.fetchExc(srcReportStock.getCategoryId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}

/** ada data untuk proses print PDF ke dalam vector */
Vector vctPDF = new Vector(1,1);
vctPDF.add(0, vctReportStock);
vctPDF.add(1, strNilaiStock);
session.putValue("SESS_STOCK_POTITION_REPORT", vctPDF);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack() {
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportposisistockkategori.jsp";
	document.frm_reportstock.submit();
}

function printForm() {
	window.open("buff_pdf_stockpotitionreport.jsp?report_title=<%=textListGlobal[SESS_LANGUAGE][1]%>","form_stockpotitionreport");
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="type" value="<%=type%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
				<tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
				  	<h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h4>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
				  <td width="89%" height="14" valign="middle" class="command">
					<%=Formater.formatDate(srcReportStock.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][3]%> <%=Formater.formatDate(srcReportStock.getDateTo(), "dd-MM-yyyy")%>
				  </td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=merk.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%></b></td>
				  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
				  <td width="89%" height="14" valign="middle" class="command"><%=category.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td align="center" valign="middle" colspan="3"><%=strDrawList%></td>
                </tr>
				<% if(vctReportStock.size() != 0) {%>
                <tr align="left" valign="top"> 
                  <td align="center" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td align="right" valign="middle" colspan="3">
				  	<h4></font><b>TOTAL <%=textListTitleTable[SESS_LANGUAGE][13]%> : &nbsp;&nbsp;&nbsp; <%=strNilaiStock%></b></h3>
				  </td>
                </tr>
				<% } %>
				<tr align="left" valign="top"> 
                  <td align="center" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="55%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td nowrap width="2%">&nbsp;</td>
                              <td class="command" nowrap width="92%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                        <td width="20%">
						<% if(vctReportStock.size() != 0) { %>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][10]%></a></td>
                            </tr>
                          </table>
						<% } %></td>
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
<!-- #EndTemplate --></html>
