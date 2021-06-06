<% 
/* 
 * Page Name  		:  buffer_ap_summary_pdf.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  gwawan 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language="java" %>
<!-- package java -->
<%@ page import="java.util.*"%>
<!-- package qdep -->
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.qdep.entity.*"%>
<!-- package project -->
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.DailyRate"%>
<%@ page import="com.dimata.common.entity.payment.PstDailyRate"%>

<%@ page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@ page import="com.dimata.posbo.session.arap.SessAccPayable"%>
<%@ page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static final String textGlobalTitle[][] = {
	{"LAPORAN REKAP HUTANG", "Tanggal","Tipe Mata Uang","Tanggal Cetak"},
	{"AP SUMMARY REPORT","Date","Currency Type","Print Date"}
};

public static final String textListHeader[][] = {
	{"Nama Supplier","Nomor Invoice","Keterangan","Tgl. Invoice","Rincian Item","Total","Diskon","Pajak","Pembayaran","Saldo Hutang"},
	{"Supplier Name", "Invoice Number","Remark","Invoice Date","Detail Item","Total","Discount","Tax","Payment","AP Balance"}
};

public static final String textListItemHeader[][] = {
	{"Kode","Unit","Harga","Qty","Total"},
	{"Code","Unit","Price","Qty","Total"}
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

SrcAccPayable srcAccPayable = new SrcAccPayable();
FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable(request, srcAccPayable);
frmSrcAccPayable.requestEntityObject(srcAccPayable);

try{ 
	srcAccPayable = (SrcAccPayable)session.getValue(SessAccPayable.SESS_ACC_PAYABLE); 
	if (srcAccPayable == null)
		srcAccPayable = new SrcAccPayable();			
}catch(Exception e){ 
	srcAccPayable = new SrcAccPayable();
}

session.putValue(SessAccPayable.SESS_ACC_PAYABLE, srcAccPayable);

/** Untuk mendapatkan besarnya daily rate dalam satuan Rp */
/**
 * Yang dibagi dengan variabel dailyRate yaitu: 
 * dr pada function javascript:calculate(), totalPayment, apPayment dan amount
 * Ini berfungsi untuk mengasilkan amount yang sesuai dengan search key yang diminta (Rp atau USD)
 */
String whereClause = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
String orderClause = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
Vector listDailyRate = PstDailyRate.list(0, 0, whereClause, orderClause);
DailyRate dr = (DailyRate)listDailyRate.get(0);
double dailyRate = dr.getSellingRate();

whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);

//Vector records = SessAccPayable.getListAPWithDetail(srcAccPayable, 0, 0);
//Vector records = SessAccPayable.getListAP(srcAccPayable, 0, 0);
Vector records = SessAccPayable.getListAP(srcAccPayable, 0, 0, 0);
Vector listAp = new Vector(1,1);
double amount = 0;
double tax = 0;
double apPayment = 0;
double apSaldo = 0;
double totalAmount = 0;
double totalTax = 0;
double totalPayment = 0;
double totalSaldo = 0;

double itemPrice = 0;
double itemQty = 0;
double itemTotalPrice = 0;

long oidInvoice = 0;
String whereClause2 = "";

for(int i=0; i<records.size(); i++) {
	Vector temp = (Vector)records.get(i);
	
	oidInvoice = Long.parseLong((String)temp.get(0));
	amount = Double.parseDouble((String)temp.get(5));
	tax = Double.parseDouble((String)temp.get(6));
	apPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, Long.parseLong((String)temp.get(0))))/dailyRate;
	apSaldo = (amount + tax) - apPayment;
	
	Vector temp2 = new Vector(1,1);
	temp2.add(String.valueOf(oidInvoice)); //0 OID Invoice
	temp2.add((String)temp.get(1)); //1 Nama Supplier
	temp2.add((String)temp.get(2)); //2 Kode Invoice
	temp2.add((String)temp.get(3)); //3 Remark
	temp2.add(Formater.formatDate((Date)temp.get(4), "dd-MM-yyyy")); //4 Tanggal Invoice
	temp2.add(String.valueOf(amount)); //5 Total
	temp2.add(String.valueOf(tax)); //6 Tax
	temp2.add(String.valueOf(apPayment)); //7 Payment
	temp2.add(String.valueOf(apSaldo)); //8 Saldo AP
	
	Vector listMatReceiveItem = new Vector(1,1);
	whereClause2 = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidInvoice;
    listMatReceiveItem = PstMatReceiveItem.list(0 , 0, whereClause2);
	temp2.add(listMatReceiveItem);
	
	listAp.add(temp2);
	
	totalAmount += amount;
	totalTax += tax;
	totalPayment += apPayment;
	totalSaldo += apSaldo;
}

Vector compTelpFax = (Vector)companyAddress.get(2);
headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textGlobalTitle[SESS_LANGUAGE][0]);
headerPdf.add(4, "");//textGlobalTitle[SESS_LANGUAGE][1]+" : "+(srcAccPayable.getInvoiceDateStatus()==1?(Formater.formatDate(srcAccPayable.getStartDate(), "dd-MM-yyyy")+" - "+Formater.formatDate(srcAccPayable.getEndDate(), "dd-MM-yyyy")):" - "));
headerPdf.add(5, textGlobalTitle[SESS_LANGUAGE][2]+" : "+currencyType.getCode());
headerPdf.add(6, textGlobalTitle[SESS_LANGUAGE][3]+" : "+Formater.formatDate(new Date(), "dd-MM-yyyy"));

listTableHeaderPdf.add("No"); //0
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][0]); //1
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][1]); //2
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][2]); //3
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][3]); //4
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][4]); //5
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][5]); //6
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][7]); //7
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][8]); //8
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][9]); //9
listTableHeaderPdf.add(textListItemHeader[SESS_LANGUAGE][0]); //10
listTableHeaderPdf.add(textListItemHeader[SESS_LANGUAGE][1]); //11
listTableHeaderPdf.add(textListItemHeader[SESS_LANGUAGE][2]); //12
listTableHeaderPdf.add(textListItemHeader[SESS_LANGUAGE][3]); //13
listTableHeaderPdf.add(textListItemHeader[SESS_LANGUAGE][4]); //14

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listAp);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalAmount));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalTax));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalPayment));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalSaldo));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("REPORT_AP_PDF", pdfContent);

%>
<script language="JavaScript">
document.location="<%=approot%>/servlet/com.dimata.posbo.report.arap.ApInvoiceWithItemReportPdf?approot=<%=approot%>";
</script>


