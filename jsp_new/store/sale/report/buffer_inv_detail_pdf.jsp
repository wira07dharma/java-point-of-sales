<%@ page language="java" %>
<!-- package java -->
<%@ page import="java.util.*"%>
<!-- package qdep -->
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.qdep.entity.*"%>
<!-- package project -->
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcSaleReport"%>
<%@ page import="com.dimata.pos.session.billing.SessBilling"%>
<%@ page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@ page import="com.dimata.pos.entity.billing.BillMain"%>
<%@ page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@ page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@ page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@ page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@ page import="com.dimata.common.entity.location.Location"%>
<%@ page import="com.dimata.common.entity.location.PstLocation"%>
<%@ page import="com.dimata.common.entity.contact.ContactList"%>
<%@ page import="com.dimata.common.entity.contact.PstContactList"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstSales"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>


<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DETAIL_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Laporan Penjualan", "Laporan Penjualan Invoice Detail"},
	{"Sale Report", "Invoice Detail Sale Report"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No","Tanggal","Nomor","Konsumen","Sales Bruto","Diskon","Pajak","Pelayanan","Netto Inv.","DP Deduction","HPP","Kasir","Detail Item","Keterangan"},
	{"No","Date","Number","Customer","Sales Bruto","Discount","Tax","Service","Netto Inv.","DP Deduction","Cogs","Sales","Item Detail","Remark"}
};

public static final String textListMaterialDetailHeader[][] = {
	{"Kode","Nama Produk","Harga","Diskon","Qty","Unit","Harga Total"},
	{"Code","Product Name","Price","Discount","Qty","Unit","Total Price"}
};


public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER ","Tidak ada data transaksi ..","Lokasi","Shift","Laporan","Cetak Laporan Penjualan Invoice Detail","Tipe","Tanggal","s/d","Mata Uang"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","Location","Shift","Report","Print Invoice Detail Sale Report","Type","Date","to","Currency Type"}
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
Vector listFooterPdf = new Vector(1,1);
Vector listPdf = new Vector(1,1);
Vector compTelpFax = (Vector)companyAddress.get(2);

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

/** tanggal */
String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");

String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
        
 if(iSaleReportType == -1 ) {
     where = where  +" AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 )";
 }else{
     where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
 }    
			

/** tipe penjualan */
if(iSaleReportType == PstCashPayment.CASH) {
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
}
else {
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
}

/** shift */
if(srcSaleReport.getShiftId()!=0) {
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

/** lokasi */
if(srcSaleReport.getLocationId()!=0) {
	where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}

/** currency */
if(srcSaleReport.getCurrencyOid()!=0) {
	where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
}

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e) {
	location.setName("Semua toko");
}

/** get the list */
Vector records = PstBillMain.list(0, 0, where, "");

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
listHeaderPdf.add(3, textListGlobal[SESS_LANGUAGE][1]); //judul laporan
listHeaderPdf.add(4, textListTitleHeader[SESS_LANGUAGE][3]+" : "+location.getName()); // lokasi
listHeaderPdf.add(5, textListTitleHeader[SESS_LANGUAGE][8]+" : "+Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textListTitleHeader[SESS_LANGUAGE][9]+" " +Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")); //tanggal
listHeaderPdf.add(6, textListTitleHeader[SESS_LANGUAGE][7]+" : "+((iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : "Penjualan Kredit")); // tipe penjualan
listHeaderPdf.add(7, textListTitleHeader[SESS_LANGUAGE][10]+" : "+strCurrencyType);

/** table header untuk main */
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][11]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][13]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][12]);
//listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][8]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);
//add service 
//by mirahu 27122011
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);
//end of adding service
//listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][8]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);

/** table header untuk detail item */
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialDetailHeader[SESS_LANGUAGE][6]);

/** proses mengambil data */
double amountBruto = 0;
double diskon = 0;
double pajak = 0;
double amountNetto = 0;
double totalAmountBruto = 0;
double totalDiskon = 0;
double totalPajak = 0;
double totalAmountNetto = 0;
//adding service
double service = 0;
double totalService = 0;

for(int i=0; i<records.size(); i++) {
	try {
		BillMain billMain = (BillMain)records.get(i);
		ContactList contactlist = new ContactList();
		
		try {
			contactlist = PstContactList.fetchExc(billMain.getCustomerId());
		}
		catch(Exception e) {
			System.out.println("Contact not found ...");
		}
		
		if(srcSaleReport.getCurrencyOid() != 0) {
			amountBruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
			diskon = billMain.getDiscount();
			pajak = billMain.getTaxValue();
                        //adding service
                        service = billMain.getServiceValue();
		}
		else {
			amountBruto = (PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID())) * billMain.getRate();
			diskon = billMain.getDiscount() * billMain.getRate();
			pajak = billMain.getTaxValue() * billMain.getRate();
                        //adding service
                        service = billMain.getServiceValue() * billMain.getRate();
		}
		//amountNetto = amountBruto - diskon + pajak;//(amountBruto - billMain.getDiscount()) + billMain.getTaxValue();
		amountNetto = amountBruto - diskon + pajak + service;
		/** pengisian vector listContentPdf untuk kepreluan report dalam PDF */
		Vector rowxPdf = new Vector(1,1);
		rowxPdf.add(String.valueOf(billMain.getOID())); //0
		rowxPdf.add(billMain.getInvoiceNumber()); //1
		rowxPdf.add(Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")); //2
		//rowxPdf.add(PstSales.getNameSales(billMain.getSalesCode())); //3
                rowxPdf.add(PstAppUser.getNameCashier(billMain.getAppUserId())); //3
		rowxPdf.add(billMain.getNotes()); //4
		rowxPdf.add(String.valueOf(amountBruto)); //5
		rowxPdf.add(String.valueOf(diskon)); //6
		rowxPdf.add(String.valueOf(pajak)); //7
                rowxPdf.add(String.valueOf(service)); //8
		rowxPdf.add(String.valueOf(amountNetto)); //9
		rowxPdf.add(String.valueOf(billMain.getRate())); //10
		rowxPdf.add(String.valueOf(srcSaleReport.getCurrencyOid())); //11
		listContentPdf.add(rowxPdf);
		
		totalAmountBruto += amountBruto;
		totalDiskon += diskon;
		totalPajak += pajak;
                //adding service value
                totalService += service;
		totalAmountNetto += amountNetto;
	}
	catch(Exception e) {
		System.out.println("Error >>>"+e.toString());
	}
}
/** end proses ambil data */

/** footer */
listFooterPdf.add(FRMHandler.userFormatStringDecimal(totalAmountBruto));
listFooterPdf.add(FRMHandler.userFormatStringDecimal(totalDiskon));
listFooterPdf.add(FRMHandler.userFormatStringDecimal(totalPajak));
//adding service
listFooterPdf.add(FRMHandler.userFormatStringDecimal(totalService));
listFooterPdf.add(FRMHandler.userFormatStringDecimal(totalAmountNetto));

listBodyPdf.add(listTableHeaderPdf);
listBodyPdf.add(listContentPdf);
listBodyPdf.add(listFooterPdf);

listPdf.add(listHeaderPdf);
listPdf.add(listBodyPdf);
session.putValue("SESS_SALE_INVOICE_DETAIL_PDF", listPdf);
%>
<script language="JavaScript">
document.location="<%=approot%>/servlet/com.dimata.posbo.report.sale.SaleInvoiceDetailReportPdf?approot=<%=approot%>";
</script>


