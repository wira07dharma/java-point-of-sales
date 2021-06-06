<% 
/* 
 * Page Name  		:  buffer_ap_pdf.jsp
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
<%@ page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static final String textGlobalTitle[][] = {
	{"LAPORAN REKAP HUTANG", "Tanggal","Tipe Mata Uang","Tanggal Cetak"},
	{"AP SUMMARY REPORT","Date","Currency Type","Print Date"}
};

public static final String textListHeader[][] = {
	{"Nama Supplier","Nomor Invoice","Keterangan","Tgl. Invoice","Total","Pajak","Retur","Pembayaran","Saldo Hutang"},
	{"Supplier Name", "Invoice Number","Remark","Invoice Date","Total","Tax","Return","Payment","AP Balance"}
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

//currency
long oidCurrency = FRMQueryString.requestLong(request, "oid_currency_type");
String recCode = FRMQueryString.requestString(request, "code_receive");
long oidReceive = FRMQueryString.requestLong(request, "hidden_receive_id");

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
//get oidCurrency from receive material
//by mirahu 28022012
long oidCurrencyPayable = 0;
oidCurrencyPayable = srcAccPayable.getCurrencyId();
if(oidCurrencyPayable == 0){
    oidCurrencyPayable = oidCurrency;
}

//String whereClause = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
String whereClause = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+oidCurrencyPayable;
String orderClause = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
Vector listDailyRate = PstDailyRate.list(0, 0, whereClause, orderClause);
DailyRate dr = (DailyRate)listDailyRate.get(0);
double dailyRate = dr	.getSellingRate();

whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);

//Vector records = SessAccPayable.getListAP(srcAccPayable, 0, 0);
Vector records = SessAccPayable.getListAP(srcAccPayable, 0, 0,oidReceive);
Vector listAp = new Vector(1,1);
double amount = 0;
double tax = 0;
double retur = 0;
double apPayment = 0;
double apSaldo = 0;
double totalAmount = 0;
double totalTax = 0;
double totalRetur = 0;
double totalPayment = 0;
double totalSaldo = 0;

//for(int j=0; j<7; j++) { //hanya untuk tes report!
for(int i=0; i<records.size(); i++) {
	Vector temp = (Vector)records.get(i);
	amount = Double.parseDouble((String)temp.get(5));
	tax = Double.parseDouble((String)temp.get(6));
	//retur = SessMatReturn.getTotalReturnByReceive(Long.parseLong((String)temp.get(0)));
        retur = SessMatReturn.getTotalReturnByReceive(srcAccPayable,Long.parseLong((String)temp.get(0)));
	apPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, Long.parseLong((String)temp.get(0))))/dailyRate;
	apSaldo = (amount + tax) - apPayment -retur;
	
	Vector temp2 = new Vector(1,1);
	temp2.add(String.valueOf(i+1));
	temp2.add((String)temp.get(1));
	temp2.add((String)temp.get(2));
	temp2.add((String)temp.get(3));
	temp2.add(Formater.formatDate((Date)temp.get(4), "dd-MM-yyyy"));
	temp2.add(String.valueOf(amount));
	temp2.add(String.valueOf(tax));
	temp2.add(String.valueOf(retur));
	temp2.add(String.valueOf(apPayment));
	temp2.add(String.valueOf(apSaldo));
	
	listAp.add(temp2);
	
	totalAmount += amount;
	totalTax += tax;
	totalRetur += retur;
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

listTableHeaderPdf.add("No");
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][7]);
listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][8]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listAp);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalAmount));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalTax));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalRetur));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalPayment));
footerPdf.add(FRMHandler.userFormatStringDecimal(totalSaldo));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("REPORT_AP_PDF", pdfContent);

%>
<script language="JavaScript">
document.location="<%=approot%>/servlet/com.dimata.posbo.report.arap.ApInvoiceReportPdf?approot=<%=approot%>";
</script>


