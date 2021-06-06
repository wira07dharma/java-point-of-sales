<% 
/* 
 * Page Name  		:  buffer_ar_detail_pdf.jsp
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
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
				 com.dimata.posbo.entity.search.RptArInvoice,
				 com.dimata.posbo.session.sales.SessAr,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
				 com.dimata.common.entity.payment.PstCurrencyType,
				 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.pos.entity.billing.*,
				 com.dimata.pos.entity.payment.*,
				 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.entity.arap.ArApMain,
                 com.dimata.posbo.entity.arap.PstArApMain,
                 com.dimata.posbo.entity.arap.ArApItem,
                 com.dimata.posbo.entity.arap.PstArApItem,
				 com.dimata.pos.entity.billing.PstBillDetail,
				 com.dimata.pos.session.billing.SessBilling"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Laporan Rekap Piutang Detail","Lokasi","Tanggal","s/d","Status","Mata Uang"},
	{"AR Summary Detail Report","Location","Date","to","Status","Currency"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No.","Nama Konsumen","No. Invoice","Keterangan","Tanggal","Rincian Item","Total Kredit","Diskon","Pajak","Retur","Telah Dibayar","Saldo Piutang","Pelayanan"},
	{"No.","Customer Name","Invoice Number","Remark","Date","Detail Item","Credit Total","Discount","Tax","Return","Payment","AR Balance","Service"}
};

public static final String textListMaterialDetailHeader[][] = {
	{"Kode","Harga","Unit","Qty","Diskon","Total"},
	{"Code","Price","Unit","Qty","Disc.","Total"}
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
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");

/** Vector untuk keperluan PDF */
Vector listHeaderPdf = new Vector(1,1);
Vector listBodyPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector listContentPdf = new Vector(1,1);
Vector listTableFooterPdf = new Vector(1,1);
Vector listPdf = new Vector(1,1);
Vector compTelpFax = (Vector)companyAddress.get(2);

SrcSaleReport srcSaleReport = new SrcSaleReport();
try{ 
	srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.AR_REPORT_DOC);
	if (srcSaleReport == null)
		srcSaleReport = new SrcSaleReport();			
}catch(Exception e){ 
	srcSaleReport = new SrcSaleReport();
}
session.putValue(SaleReportDocument.AR_REPORT_DOC, srcSaleReport);

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e) {
	location.setName("Semua toko");
}

/** get the list */
Vector records = SessAr.getArInvoice(0, 0, srcSaleReport);  
if(records.size() > 0) {
	session.putValue(SaleReportDocument.AR_PAYMENT_DOC, records);
}

String strCurrencyType = "All";
if(srcSaleReport.getCurrencyOid() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}

/** pdf header */
listHeaderPdf.add(0,(String) companyAddress.get(0)); // nama perusahaan
listHeaderPdf.add(1,(String) companyAddress.get(1)); // alamat
listHeaderPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1)); // no telp/fax
listHeaderPdf.add(3, textListGlobal[SESS_LANGUAGE][0]); //judul laporan
listHeaderPdf.add(4, textListGlobal[SESS_LANGUAGE][1]); // lokasi
listHeaderPdf.add(5, location.getName()); // lokasi value
if(srcSaleReport.getUseDate()==1) {
	listHeaderPdf.add(6, textListGlobal[SESS_LANGUAGE][2]); //tanggal
	listHeaderPdf.add(7, Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")); //tanggal value1
	listHeaderPdf.add(8, textListGlobal[SESS_LANGUAGE][3]); //s/d
	listHeaderPdf.add(9, Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")); // tanggal value2
	listHeaderPdf.add(10, textListGlobal[SESS_LANGUAGE][4]); // status
	listHeaderPdf.add(11, SrcSaleReport.stTransStatus[SESS_LANGUAGE][srcSaleReport.getTransStatus()]); //status value
	listHeaderPdf.add(12, textListGlobal[SESS_LANGUAGE][5]); // mata uang
	listHeaderPdf.add(13, strCurrencyType); // mata uang value
}
else {
	listHeaderPdf.add(6, ""); //tanggal
	listHeaderPdf.add(7, ""); //tanggal value1
	listHeaderPdf.add(8, ""); //s/d
	listHeaderPdf.add(9, ""); //tanggal value2
	listHeaderPdf.add(10, textListGlobal[SESS_LANGUAGE][4]); // status
	listHeaderPdf.add(11, SrcSaleReport.stTransStatus[SESS_LANGUAGE][srcSaleReport.getTransStatus()]); //status value
	listHeaderPdf.add(12, textListGlobal[SESS_LANGUAGE][5]); // mata uang
	listHeaderPdf.add(13, strCurrencyType); // mata uang value
}


/** table header untuk main */
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][8]);
//adding service
//by mirahu
//060112
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][12]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][9]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][10]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][11]);

/** untuk keperluan report ar detail */
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][5]);

/** proses mengambil data */
double amount = 0;
double diskon = 0;
double pajak = 0;
double retur = 0;
double payment = 0;
double saldo = 0;
//adding service
double service = 0;

double totalAmount = 0;
double totalDiskon = 0;
double totalPajak = 0;
double totalRetur = 0;
double totalPayment = 0;
double totalSaldo = 0;
//adding service
double totalService = 0;

String where = "";

for(int i=0; i<records.size(); i++) {
	RptArInvoice arInv = (RptArInvoice)records.get(i);
	if(srcSaleReport.getCurrencyOid() == 0) {
		amount = arInv.getSaleNetto() * arInv.getRate();
		diskon = arInv.getDiscount() * arInv.getRate();
		pajak = arInv.getTax() * arInv.getRate();
                //adding service
                service = arInv.getService() * arInv.getRate();
                retur = arInv.getTotalReturn() * arInv.getRate();
		payment = arInv.getTotalPay() * arInv.getRate();
                
	}
	else {
		amount = arInv.getSaleNetto();
		diskon = arInv.getDiscount();
		pajak = arInv.getTax();
                //adding service
                service = arInv.getService();
                retur = arInv.getTotalReturn();
		payment = arInv.getTotalPay();
                
	}
	//saldo = amount - diskon + pajak - payment;
        saldo = amount - diskon + pajak + service - payment;
	
	totalAmount += amount;
	totalDiskon += diskon;
	totalPajak += pajak;
        totalRetur += retur;
	totalPayment += payment;
	totalSaldo += saldo;
        //adding service
        totalService += service;
	
	Vector rowPdf = new Vector();
	rowPdf.add(String.valueOf(arInv.getBillMainId())); //0
	rowPdf.add(arInv.getCustName()); //1
	rowPdf.add(arInv.getInvoiceNo()); //2
	rowPdf.add(arInv.getNotes()); //3
	rowPdf.add(Formater.formatDate(arInv.getBillDate(),"dd MMM yyyy")); //4
	rowPdf.add(String.valueOf(amount)); //5
	rowPdf.add(String.valueOf(diskon)); //6
	rowPdf.add(String.valueOf(pajak)); //7
        rowPdf.add(String.valueOf(service)); //8
        rowPdf.add(String.valueOf(payment)); //9
	rowPdf.add(String.valueOf(saldo)); //10
	rowPdf.add(String.valueOf(srcSaleReport.getCurrencyOid())); //11 currency type
	rowPdf.add(String.valueOf(arInv.getRate())); //12 rate
        rowPdf.add(String.valueOf(retur)); //13
	//rowPdf.add(String.valueOf(payment)); //8
	//rowPdf.add(String.valueOf(saldo)); //9
	//rowPdf.add(String.valueOf(srcSaleReport.getCurrencyOid())); //10 currency type
	//rowPdf.add(String.valueOf(arInv.getRate())); //11 rate
        //rowPdf.add(String.valueOf(retur)); //12
	
	/** proses pencarian detail dari invoice */
	where = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+arInv.getBillMainId();
	Vector listInvoiceDetail = SessBilling.listInvoiceDetail(0, 0, where, "");
	rowPdf.add(listInvoiceDetail);
	
	listContentPdf.add(rowPdf);
}
/** end proses ambil data */

/** footer */
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalAmount));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalDiskon));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalPajak));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalService));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalRetur));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalPayment));
listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(totalSaldo));

listBodyPdf.add(listTableHeaderPdf);
listBodyPdf.add(listContentPdf);
listBodyPdf.add(listTableFooterPdf);

listPdf.add(listHeaderPdf);
listPdf.add(listBodyPdf);
session.putValue(SaleReportDocument.AR_INVOICE_PDF, listPdf);
%>
<script language="JavaScript">
document.location="<%=approot%>/servlet/com.dimata.posbo.report.arap.ArInvoiceDetailReportPdf?approot=<%=approot%>";
</script>


