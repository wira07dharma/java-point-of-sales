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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"LAPORAN PENERIMAAN BARANG ANTAR LOKASI","Periode"," s/d ", "Lokasi","Semua"},
	{"GOODS RECEIVE BETWEEN LOCATION REPORT","Period"," to ","Location","All"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","QTY","SATUAN","HARGA BELI","TOTAL BELI","HPP","TOTAL HPP"},
    {"NO","SKU","GOODS NAME","QTY","UNIT","BUYING PRICE","TOTAL BUYING","COGS","TOTAL COGS"}
};
%>

<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head> 

<body bgcolor="#FFFFFF" text="#000000">
    Loading ... 
    <script language="JavaScript">
        window.focus();
    </script>
</body>
</html>

<!-- JSP Block -->
<%
/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector bodyPdf = new Vector(1,1);
Vector footerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);

/** start fetch data */
SrcReportReceive srcReportReceive = new SrcReportReceive();
FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);

try{ 
	srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE); 
	if (srcReportReceive == null)
		srcReportReceive = new SrcReportReceive();			
}catch(Exception e){ 
	srcReportReceive = new SrcReportReceive();
}

session.putValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE, srcReportReceive);

Vector records = SessReportReceive.getReportReceiveInternalTotal(srcReportReceive);

Location location = new Location();
if (srcReportReceive.getLocationId() != 0) {
try {
	location = PstLocation.fetchExc(srcReportReceive.getLocationId());
}
catch(Exception e) {
	System.out.println("Exc when fetch Location : " + e.toString());
}
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][4]+" "+textListGlobal[SESS_LANGUAGE][3]);
}

Vector listMatReceive = new Vector(1,1);
double grandTotalBeli = 0;
double grandTotalHpp = 0;

for(int i = 0; i < records.size(); i++) {
	Vector vt = (Vector)records.get(i);
	MatReceiveItem objMatReceiveItem = (MatReceiveItem)vt.get(0);
	Material objMaterial = (Material)vt.get(1);
	Unit objUnit = (Unit)vt.get(2);
	//Category objCategory = new Category;//(Category)vt.get(3);
	//SubCategory objSubCategory = (SubCategory)vt.get(4);
	
	Vector rowx = new Vector();
	rowx.add(String.valueOf(i+1)+".");
	rowx.add(objMaterial.getSku());
	rowx.add(objMaterial.getName());
	rowx.add(FRMHandler.userFormatStringDecimal(objMatReceiveItem.getQty()));
	rowx.add(objUnit.getCode());
	rowx.add(FRMHandler.userFormatStringDecimal(objMatReceiveItem.getCost()));
	rowx.add(String.valueOf(objMatReceiveItem.getTotal()));
	rowx.add(FRMHandler.userFormatStringDecimal(objMaterial.getAveragePrice()));
	rowx.add(String.valueOf(objMatReceiveItem.getQty() * objMaterial.getAveragePrice()));
	listMatReceive.add(rowx);
	
	grandTotalBeli += objMatReceiveItem.getTotal();
	grandTotalHpp += (objMatReceiveItem.getQty() * objMaterial.getAveragePrice());
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);
String periodeLaporan = Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")+" "+textListGlobal[SESS_LANGUAGE][2]+" "+Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy");

headerPdf.add(0, (String) companyAddress.get(0));
headerPdf.add(1, (String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textListGlobal[SESS_LANGUAGE][0]);
headerPdf.add(4, (textListGlobal[SESS_LANGUAGE][1]).toUpperCase()); //periode
headerPdf.add(5, " : "+periodeLaporan);
headerPdf.add(6, (textListGlobal[SESS_LANGUAGE][3]).toUpperCase()); //location
headerPdf.add(7, " : "+location.getName().toUpperCase());
headerPdf.add(8, ""); //category or supplier
headerPdf.add(9, "");

listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][8]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listMatReceive);

footerPdf.add(FRMHandler.userFormatStringDecimal(grandTotalBeli));
footerPdf.add(FRMHandler.userFormatStringDecimal(grandTotalHpp));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("RECEIVE_INTERNAL_REPORT_PDF", pdfContent);

%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.receive.ReceiveInternalReportPdf?approot=<%=approot%>";
</script>