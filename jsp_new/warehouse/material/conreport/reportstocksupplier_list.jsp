<%@ page import="com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportConStock,
                 com.dimata.posbo.form.search.FrmSrcReportStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
int DATA_NULL = 0;
int DATA_PRINT = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data ...", "LAPORAN STOK BARANG", "Dari", "s/d", "Periode", "Lokasi", "Kategori", "Consignor", "Semua","Merk"},
	{"Goods item not found...", "GOODS STOCK REPORT", "From", "to", "Periode", "Location", "Category", "Consignor", "All","Merk"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HPP","NILAI STOCK"},
	{"NO","CODE","NAME","QTY", "UNIT", "COST","TOTAL COST","COGS","STOCK VALUE"}
};

public Vector drawList2(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("99%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"31%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"13%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][7],"13%");
        ctrlist.addHeader(textListMaterialHeader[language][8],"13%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		
		double totalNilaiStock = 0;
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);			
			MaterialConStock materialStock = (MaterialConStock)vt.get(0);
                        Material material = (Material)vt.get(1);			
			Unit unit = (Unit)vt.get(4);
			

			Vector rowx = new Vector();
			rowx.add(String.valueOf(i+1)+".");
			rowx.add(material.getSku());
			rowx.add(material.getName());
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice() * materialStock.getQty())+"</div>");
			
			totalNilaiStock += (material.getAveragePrice() * materialStock.getQty());
			
			lstData.add(rowx);
		}
		
		result.add(ctrlist.draw());
		result.add(FRMHandler.userFormatStringDecimal(totalNilaiStock));
	}
	else{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>");
		result.add("");
	}
	return result;
}

%>

<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");

ControlLine ctrLine = new ControlLine();
SrcReportStock srcReportStock = new SrcReportStock();
SessReportConStock sessReportStock = new SessReportConStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);

if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
	 try{ 
		srcReportStock = (SrcReportStock)session.getValue(SessReportConStock.SESS_SRC_REPORT_STOCK); 
		if (srcReportStock == null) srcReportStock = new SrcReportStock();
	 }catch(Exception e){ 
		srcReportStock = new SrcReportStock();
	 }
}
else {
	 frmSrcReportStock.requestEntityObject(srcReportStock);
	 session.putValue(SessReportConStock.SESS_SRC_REPORT_STOCK, srcReportStock);
}

boolean calculateQtyNull = true;
srcReportStock.setTypeConsig(PstMaterial.CATALOG_TYPE_CONSIGMENT);
Vector listStockPerSupplier = sessReportStock.getReportStock(srcReportStock); // getReportStock

Vector vct = drawList2(SESS_LANGUAGE, listStockPerSupplier);
String strDrawlist = (String)vct.get(0);
String strNilaiStock = (String)vct.get(1);

Location location = new Location();
if (srcReportStock.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportStock.getLocationId());
	}
	catch(Exception e) {
		System.out.println("Exc when get lokasi");
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][5]);
}

Periode periode = new Periode();
try {
	periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
}
catch(Exception e) {
	System.out.println("Exc when get periode");
}

ContactList contactList = new ContactList();
try{
	contactList = PstContactList.fetchExc(srcReportStock.getSupplierId());
        if(contactList.getOID()==0)
            contactList.setCompName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][7]);

}catch(Exception e){
    contactList.setCompName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][7]);
    System.out.println("Exc when get periode");
}

Category category = new Category();
try{
	category = PstCategory.fetchExc(srcReportStock.getCategoryId());
        if(category.getOID()==0)
            category.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][6]);
}catch(Exception e){
    category.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][6]);
    System.out.println("Exc when get kategory");
}

Merk merk = new Merk();
try{
	merk = PstMerk.fetchExc(srcReportStock.getMerkId());
        if(merk.getOID()==0)
            merk.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][9]);

}catch(Exception e){
    merk.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][9]);
    System.out.println("Exc when get merk");
}

%> 
<!-- End of Jsp Block -->

<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack() {
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportstocksupplier.jsp";
	document.frm_reportstock.submit();
} 

function printForm() {
	window.open("buff_pdf_liststockreportsuplier.jsp", "stock_report_by_supplier")
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
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">
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
            Konsinyasi > Laporan Stok Barang<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
				  	<h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h3>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3"> 
                    <b><%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%> : </b><%=periode.getPeriodeName()%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%> : </b><%=location.getName()%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%> : </b><%=contactList.getCompName()%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%> : </b><%=category.getName()%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=(textListGlobal[SESS_LANGUAGE][9]).toUpperCase()%> : </b><%=merk.getName()%>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" align="center" colspan="3"><%=strDrawlist%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" align="right" valign="middle" colspan="3">
				  	<h4></font><b>TOTAL <%=textListMaterialHeader[SESS_LANGUAGE][8]%> : &nbsp;&nbsp;&nbsp; <%=strNilaiStock%></b></h3>
				  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="55%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Laporan Stok",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td nowrap width="2%">&nbsp;</td>
                              <td class="command" nowrap width="92%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Laporan Stok",ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                        <td width="20%"> 
						<%
						if(listStockPerSupplier.size() != 0) {
						%>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" >Print Laporan Stock</a></td>
                            </tr>
                        </table>
						<%
						} else {
						%>
						  &nbsp;
						<%
						}
						%>						  
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --> 
</html>
