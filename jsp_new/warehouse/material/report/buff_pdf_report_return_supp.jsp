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
public static final String textGlobalTitle[][] = {
	{"LAPORAN RETUR BARANG KE SUPLIER PER SUPLIER", "Periode", "Dari", "s/d", "Lokasi", "Suplier", "Semua"},
	{"GOODS RETURN TO SUPPLIER REPORT BY SUPPLIER", "Period", "From", "to", "Location", "Supplier", "All"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY","SATUAN","HARGA BELI","TOTAL RETUR"},
	{"NO","SKU","GOODS NAME","QTY","UNIT","BUYING PRICE","TOTAL RETURN"}
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
SrcReportReturn srcReportReturn = new SrcReportReturn();
FrmSrcReportReturn frmSrcReportReturn = new FrmSrcReportReturn(request, srcReportReturn);
SessReportReturn sessReportReturn = new SessReportReturn();


try{ 
	srcReportReturn = (SrcReportReturn)session.getValue(SessReportReturn.SESS_SRC_REPORT_RETURN); 
	if (srcReportReturn == null)
		srcReportReturn = new SrcReportReturn();			
}catch(Exception e){ 
	srcReportReturn = new SrcReportReturn();
}

session.putValue(SessReportReturn.SESS_SRC_REPORT_RETURN, srcReportReturn);

Vector records = SessReportReturn.getReportReturnTotal(srcReportReturn);

Location location = new Location();
if(srcReportReturn.getLocationId() != 0) {
	try {
		location = PstLocation.fetchExc(srcReportReturn.getLocationId());
	}
	catch(Exception e) {
		System.out.println("Exc when fetch Location : " + e.toString());
	}
}
else {
	location.setName(textGlobalTitle[SESS_LANGUAGE][6]+" "+textGlobalTitle[SESS_LANGUAGE][4]);
}

ContactList contactList = new ContactList();
if (srcReportReturn.getSupplierId() != 0) {
	try	{
		contactList = PstContactList.fetchExc(srcReportReturn.getSupplierId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}

Vector listMatReturn = new Vector(1,1);
double totalReturn = 0;

for(int i = 0; i < records.size(); i++) {
	Vector vt = (Vector)records.get(i);
	MatReturnItem matReturnItem = (MatReturnItem)vt.get(0);
	Material material = (Material)vt.get(1);
	Unit objUnit = (Unit)vt.get(2);
	MatReturn objMatReturn = (MatReturn)vt.get(5);
	
	Vector rowx = new Vector();
	rowx.add(String.valueOf(i+1)+".");
	rowx.add(material.getSku());
	rowx.add(material.getName());
	rowx.add(FRMHandler.userFormatStringDecimal(matReturnItem.getQty()));
	rowx.add(objUnit.getCode());
	rowx.add(FRMHandler.userFormatStringDecimal(matReturnItem.getCost() * objMatReturn.getTransRate()));
	rowx.add(String.valueOf(matReturnItem.getTotal() * objMatReturn.getTransRate()));
	listMatReturn.add(rowx);
	
	totalReturn += (matReturnItem.getTotal() * objMatReturn.getTransRate());
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);
String periodeLaporan = Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")+" "+textGlobalTitle[SESS_LANGUAGE][3]+" "+Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy");

headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textGlobalTitle[SESS_LANGUAGE][0]);
headerPdf.add(4, textGlobalTitle[SESS_LANGUAGE][1]);
headerPdf.add(5, " : "+periodeLaporan);
headerPdf.add(6, textGlobalTitle[SESS_LANGUAGE][4]);
headerPdf.add(7, " : "+location.getName());
headerPdf.add(8, textGlobalTitle[SESS_LANGUAGE][5]);
headerPdf.add(9, " : "+contactList.getCompName());

listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listMatReturn);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalReturn));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("REPORT_RETURN_PDF", pdfContent);

%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.retur.ReportReturnPdf?approot=<%=approot%>";
</script>