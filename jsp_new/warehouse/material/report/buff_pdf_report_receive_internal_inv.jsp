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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT_BY_RECEIPT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"LAPORAN PENERIMAAN BARANG ANTAR LOKASI PER INVOICE","Periode"," s/d ", "Lokasi"},
	{"GOODS RECEIVE BETWEEN LOCATION REPORT BY RECEIPT","Period"," to ","Location"}
};

public static final String listTextHeaderMain[][] = {
	{"No.","Nota","Tanggal","Keterangan","Rincian Item","Total Beli"},
	{"No.","Receipt","Date","Remark","Detail Item","Total Buying"}
};

public static final String listTextHeaderItem[][] = {
	{"SKU","Nama Barang","Qty","Satuan","Harga Beli","Total Beli"},
	{"SKU","Goods Name","Qty","Unit","Buying Price","Total Buying"}
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

Vector records = SessReportReceive.getReportReceiveInternalMain(srcReportReceive);

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcReportReceive.getLocationId());
}
catch(Exception e) {
	System.out.println("Exc when fetch Location : " + e.toString());
}

Vector listMatReceive = new Vector(1,1);
double grandTotalBeli = 0;

for(int i = 0; i < records.size(); i++) {
	Vector vt = (Vector)records.get(i);
	MatReceive objMatReceive = (MatReceive)vt.get(0);
	Location objLocation = (Location)vt.get(1);
	String totalRcv = (String)vt.get(2);
	
	/** get list item */
	Vector recordsItem = SessReportReceive.getReportReceiveInternalItem(objMatReceive.getOID());
	
	Vector rowx = new Vector();
	rowx.add(String.valueOf(i+1)+".");
	rowx.add(objMatReceive.getRecCode());
	rowx.add(Formater.formatDate(objMatReceive.getReceiveDate(), "dd-MM-yyyy"));
	rowx.add(objMatReceive.getRemark());
	rowx.add(recordsItem);
	rowx.add(FRMHandler.userFormatStringDecimal(Double.parseDouble(totalRcv)));
	listMatReceive.add(rowx);
	
	grandTotalBeli += Double.parseDouble(totalRcv);
	
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

listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(listTextHeaderMain[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(listTextHeaderItem[SESS_LANGUAGE][5]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listMatReceive);

footerPdf.add(FRMHandler.userFormatStringDecimal(grandTotalBeli));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("RECEIVE_INTERNAL_INVOICE_REPORT_PDF", pdfContent);

%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.receive.ReceiveInternalDetailReportPdf?approot=<%=approot%>";
</script>