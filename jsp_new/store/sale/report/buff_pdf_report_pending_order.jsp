<%@ page language = "java" %>
<%@ page import="java.util.Hashtable,
		 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
		 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.pos.entity.billing.PstPendingOrder,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.pos.entity.billing.PendingOrder,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
		 com.dimata.common.entity.payment.PstCurrencyType,
		 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_PENDING_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textGlobalTitle[][] = {
	{"Laporan Pending Order","Periode","s/d","Lokasi","Mata Uang","Semua","Cetak Laporan Pending Order"},
	{"Pending Order Report","Period","to","Location","Currency","All","Print Pending Order Report"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","TANGGAL","NOMOR","KONSUMEN","DOWN PAYMENT","JUMLAH","KURS","TOTAL DP"},
	{"NO","DATE","NUMBER","CUSTOMER","DOWN PAYMENT","NOMINAL","RATE","TOTAL DP"}
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
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);

try{ 
	srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC); 
	if (srcSaleReport == null)
		srcSaleReport = new SrcSaleReport();			
}catch(Exception e){ 
	srcSaleReport = new SrcSaleReport();
}

session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);

Vector records = PstPendingOrder.getDataPendingOrder(srcSaleReport, 0, 0);

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e) {
	location.setName(textGlobalTitle[SESS_LANGUAGE][5]+" "+textGlobalTitle[SESS_LANGUAGE][3]);
}

CurrencyType currencyType = new CurrencyType();
try {
	currencyType = PstCurrencyType.fetchExc(srcSaleReport.getCurrencyOid());
}
catch(Exception e) {
	System.out.println(e.toString());
}

Vector listPendingOrder = new Vector(1,1);
double totalDp = 0;

for(int i = 0; i < records.size(); i++) {
	PendingOrder pendingOrder = (PendingOrder)records.get(i);
	
	ContactList contactlist = new ContactList();
	try	{
		contactlist = PstContactList.fetchExc(pendingOrder.getMemberId());
	}
	catch(Exception e) {
		System.out.println("Exc when fetch member : " + e.toString());
	}
	
	Vector rowx = new Vector();
	rowx.add(String.valueOf(i+1)+".");
	rowx.add(Formater.formatDate(pendingOrder.getCreationDate(),"dd/MM/yyyy"));
	rowx.add(pendingOrder.getOrderNumber());
	rowx.add(contactlist.getPersonName());			
	rowx.add(FRMHandler.userFormatStringDecimal(pendingOrder.getDownPayment()/pendingOrder.getRate()));
	rowx.add(FRMHandler.userFormatStringDecimal(pendingOrder.getRate()));
	rowx.add(String.valueOf(pendingOrder.getDownPayment() * pendingOrder.getRate()));
	
	listPendingOrder.add(rowx);
	totalDp += (pendingOrder.getDownPayment() * pendingOrder.getRate());
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);
String periodeLaporan = Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textGlobalTitle[SESS_LANGUAGE][2]+" "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy");

headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textGlobalTitle[SESS_LANGUAGE][0]);
headerPdf.add(4, textGlobalTitle[SESS_LANGUAGE][1]);
headerPdf.add(5, " : "+periodeLaporan);
headerPdf.add(6, textGlobalTitle[SESS_LANGUAGE][3]);
headerPdf.add(7, " : "+location.getName());
headerPdf.add(8, textGlobalTitle[SESS_LANGUAGE][4]);
headerPdf.add(9, " : "+currencyType.getCode());

listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]+"("+currencyType.getCode()+")");
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listPendingOrder);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalDp));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("PENDING_ORDER_REPORT_PDF", pdfContent);

%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.sale.PendingOrderReportPdf?approot=<%=approot%>";
</script>