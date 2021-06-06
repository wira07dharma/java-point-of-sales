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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textGlobalTitle[][] = {
	{"LAPORAN PENERIMAAN BARANG PER SUPPLIER", "Dari", "s/d", "Lokasi", "Supplier","Semua"},
	{"GOODS RECEIVE REPORT BY SUPPLIER", "From", "to", "Location", "Supplier","All"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HARGA JUAL","TOTAL JUAL"},
	{"NO","SKU","NAME","QTY", "UNIT", "COST","TOTAL COST","PRICE","TOTAL PRICE"}
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
frmSrcReportReceive.requestEntityObject(srcReportReceive);

try{ 
	srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE); 
	if (srcReportReceive == null)
		srcReportReceive = new SrcReportReceive();			
}catch(Exception e){ 
	srcReportReceive = new SrcReportReceive();
}

session.putValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE, srcReportReceive);

Vector records = SessReportReceive.getReportReceiveTotal(srcReportReceive);

Location location = new Location();
if (srcReportReceive.getLocationId() != 0) {
try {
	location = PstLocation.fetchExc(srcReportReceive.getLocationId());
}
catch(Exception e) {
	System.out.println("Exc when fetch Location : " + e.toString());
}
} else {
    location.setName(textGlobalTitle[SESS_LANGUAGE][5]+" "+textGlobalTitle[SESS_LANGUAGE][3]);
}

ContactList contactList = new ContactList();
if (srcReportReceive.getSupplierId() != 0) {
	try	{
		contactList = PstContactList.fetchExc(srcReportReceive.getSupplierId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}
else {
    contactList.setCompName(textGlobalTitle[SESS_LANGUAGE][5]+" "+textGlobalTitle[SESS_LANGUAGE][4]);
}

Vector listMatReceive = new Vector(1,1);
double totalBeli = 0;

for(int i = 0; i < records.size(); i++) {
	Vector vt = (Vector)records.get(i);
	MatReceiveItem matReceiveItem = (MatReceiveItem)vt.get(0);
	Material material = (Material)vt.get(1);
	Unit unit = (Unit)vt.get(2); 
	Category category = (Category)vt.get(3);
	SubCategory subCategory = (SubCategory)vt.get(4);
	MatReceive matReceive = (MatReceive)vt.get(5);
	
	Vector temp = new Vector(1,1);
	temp.add(String.valueOf(i+1));
	temp.add(material.getSku());
	temp.add(material.getName());
	temp.add(FRMHandler.userFormatStringDecimal(matReceiveItem.getQty()));
	temp.add(unit.getCode());
	temp.add(FRMHandler.userFormatStringDecimal(matReceiveItem.getCost() * matReceive.getTransRate()));
	temp.add(String.valueOf(matReceiveItem.getCost() * matReceiveItem.getQty() * matReceive.getTransRate()));
	
	listMatReceive.add(temp);
	
	totalBeli += matReceiveItem.getCost() * matReceiveItem.getQty() * matReceive.getTransRate();
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);
String periodeLaporan = textGlobalTitle[SESS_LANGUAGE][1]+" "+Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")+" "+textGlobalTitle[SESS_LANGUAGE][2]+" "+Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy");

headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textGlobalTitle[SESS_LANGUAGE][0]);
headerPdf.add(4, periodeLaporan);
headerPdf.add(5, textGlobalTitle[SESS_LANGUAGE][3]+" : "+location.getName());
headerPdf.add(6, textGlobalTitle[SESS_LANGUAGE][4]+" : "+contactList.getCompName());

listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listMatReceive);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalBeli));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("REPORT_RECEIVE_PDF", pdfContent);
int view=1;
%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.receive.ReportReceivePdf?approot=<%=approot%>&view=<%=view%>";
</script>