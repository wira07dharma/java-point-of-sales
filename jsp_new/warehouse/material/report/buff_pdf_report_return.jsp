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
	{"LAPORAN RETUR BARANG KE SUPLIER", "Periode", "Dari", "s/d", "Lokasi", "Suplier", "Semua"},
	{"GOODS RETURN TO SUPPLIER REPORT", "Period", "From", "to", "Location", "Supplier", "All"}
};

public static final String textListMaterialHeaderMain[][] = {
	{"NO","NOTA","TANGGAL","SUPLIER","ITEM","TOTAL RETUR"},
	{"NO","INVOICE","DATE","SUPPLIER","ITEM","TOTAL RETURN"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY","SATUAN","HARGA BELI","TOTAL BELI"},
	{"NO","SKU","GOODS NAME","QTY","UNIT","BUYING PRICE","TOTAL COST"}
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
	srcReportReturn = (SrcReportReturn)session.getValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE); 
	if (srcReportReturn == null)
		srcReportReturn = new SrcReportReturn();			
}catch(Exception e){ 
	srcReportReturn = new SrcReportReturn();
}

session.putValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE, srcReportReturn);

Vector records = sessReportReturn.getReportReturn(srcReportReturn);

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


int nomor = 1;
double grandTotalRetur = 0;
Vector listMatReturn = new Vector(1,1);

for(int i=0; i<records.size(); i++)	{
	Vector rowx = new Vector();				
	Vector objVector = (Vector)records.get(i);
	MatReturn objMatReturn = (MatReturn)objVector.get(0);
	MatReturnItem objMatReturnItem = (MatReturnItem)objVector.get(1);
	Material objMaterial = (Material)objVector.get(2);
	Unit objUnit = (Unit)objVector.get(3);
	ContactList objContactList = (ContactList)objVector.get(5);
	
	grandTotalRetur += (objMatReturnItem.getQty() * objMatReturnItem.getCost() * objMatReturn.getTransRate());
	
	rowx.add(String.valueOf(objMatReturn.getOID())); //0
	rowx.add(objMatReturn.getRetCode()); //1
	rowx.add(Formater.formatDate(objMatReturn.getReturnDate(), "dd-MM-yyyy")); //2
	rowx.add(objContactList.getCompName()); //3
	rowx.add(objMaterial.getSku()); //4
	rowx.add(objMaterial.getName()); //5
	rowx.add(objUnit.getCode()); //6
	rowx.add(String.valueOf(objMatReturnItem.getQty())); //7
	rowx.add(String.valueOf(objMatReturnItem.getCost())); //8
	rowx.add(String.valueOf(objMatReturn.getTransRate())); //9
	
	listMatReturn.add(rowx);
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);
String periodeLaporan = Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")+" "+textGlobalTitle[SESS_LANGUAGE][3]+" "+Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy");

headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textGlobalTitle[SESS_LANGUAGE][0]);
headerPdf.add(4, textGlobalTitle[SESS_LANGUAGE][1]);
headerPdf.add(5, periodeLaporan);
headerPdf.add(6, textGlobalTitle[SESS_LANGUAGE][4]);
headerPdf.add(7, location.getName());
headerPdf.add(8, "");
headerPdf.add(9, "");

/** header main */
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeaderMain[SESS_LANGUAGE][5]);

/** header item */
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);

footerPdf.add(FRMHandler.userFormatStringDecimal(grandTotalRetur));

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listMatReturn);
bodyPdf.add(footerPdf);

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);

session.putValue("REPORT_RETURN_DETAIL_PDF", pdfContent);

%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.retur.ReturnDetailReportPdf?approot=<%=approot%>";
</script>