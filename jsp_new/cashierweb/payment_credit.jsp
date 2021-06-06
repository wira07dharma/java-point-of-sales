<%-- whereClause
    Document   : payment_credit
    Created on : Nov 19, 2013, 5:41:22 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.entity.masterCashier.CashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.pos.entity.balance.CashCashier"%>
<%@page import="com.dimata.pos.entity.balance.PstCashCashier"%>
<%@page import="com.dimata.pos.entity.payment.CashCreditPaymentInfo"%>
<%@page import="com.dimata.pos.entity.payment.CreditPaymentMain"%>
<%@page import="com.dimata.pos.form.payment.FrmCashCreditPaymentMain"%>
<%@page import="com.dimata.pos.form.payment.CtrlCashCreditPaymentMain"%>
<%@page import="com.dimata.pos.form.payment.FrmCashCreditPayment"%>
<%@page import="com.dimata.posbo.entity.search.RptArPaymentDetail"%>
<%@page import="com.dimata.posbo.entity.search.RptArPayment"%>
<%@page import="com.dimata.posbo.session.sales.SessAr"%>
<%@page import="com.dimata.posbo.entity.search.RptArInvoice"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%
/*
 * Page Name  		:  payment_credit.jsp
 * Created on 		:  [date] [time] AM/PM
 *
 * @author  		:  [authorName]
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
<%@ page import="com.dimata.common.entity.payment.PaymentSystem"%>
<%@ page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@ page import="com.dimata.common.form.payment.FrmPaymentSystem"%>
<%@ page import="com.dimata.common.entity.payment.PaymentInfo"%>
<%@ page import="com.dimata.common.entity.payment.PstPaymentInfo"%>
<%@ page import="com.dimata.common.form.payment.FrmPaymentInfo"%>
<%@ page import="com.dimata.common.entity.payment.DailyRate"%>
<%@ page import="com.dimata.common.entity.payment.PstDailyRate"%>

<%@ page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@ page import="com.dimata.posbo.session.arap.SessAccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.AccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.PstAccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.AccPayableDetail"%>
<%@ page import="com.dimata.posbo.entity.arap.PstAccPayableDetail"%>
<%@ page import="com.dimata.posbo.form.arap.FrmAccPayable"%>
<%@ page import="com.dimata.posbo.form.arap.CtrlAccPayable"%>
<%@ page import="com.dimata.posbo.form.arap.CtrlAccPayableDetail"%>
<%@ page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@include file = "../main/checkuser.jsp" %>
<%
privAdd = true;
privUpdate =true;
privDelete = true;
privStart = true;
privStop =true;
%>
<%!
public static final String textGlobalTitle[][] = {
	{
	 "Gudang","Penerimaan Barang","Rekap Piutang","Kembali ke Pencarian","Tambah Pembayaran","Simpan Pembayaran","Kembali Ke Daftar","Rincian Pembayaran", //0-7
	 "Total Pembayaran","Saldo Piutang","Sub Total","Grand Total","Daftar Piutang Tidak di Temukan!","Daftar pembayaran Piutang tidak ditemukan!", //8-13
	 "Piutang","Pembayaran","Rincian Sistem Pembayaran","Cetak Laporan Rekap Piutang","Jumlah pembayaran melebihi saldo Piutang!", //14-18
	 "Data belum lengkap!", "Kembali ke Penerimaan"
	},
	{
	 "Warehouse","Receive Goods","AP Summary","Back To Search","Add New Payment","Save Payment","Back To List","Detail Of Payment", //0-7
	 "Total of Payment","AP Balance","Sub Total","Grand Total","Account Receivable list not found!","Payment list not found!", //8-13
	 "AP","Payment","Payment System Info", "Print AP Summary Report","AR payment more than AR balance!", //14-18
	 "Incomplete data!", "Back To Receive"
	}
};

public static final String textListHeader[][] = {
	{"Nama Perusahaan","Nomor Invoice","Keterangan","Tanggal","Total","Pajak","Retur","Pembayaran","Saldo Piutang","Payment"},
	{"Company Name", "Invoice Number","Remark","Invoice","Total","Tax","Return","Payment","AR Balance","Payment"}
};

public static final String textListHeaderDetail[][] = {
	{"Tanggal","Lokasi","Sistem Pembayaran","Mata Uang","Rate","Jumlah dalam Mata Uang","Jumlah","Total Invoice","Sisa"},
	{"Date","Location","Payment System","Currency","Rate","Amount in Currency","Amount","Payent","Balance"}
};

public static final String textListPaymentInfo[][] = {
	{"Nama Bank","Alamat Bank","Kode Swift","Nama Rekening","Nomor Rekening","Nama pada Kartu","Nomor Kartu","Id Kartu", //0-7
	"Kadarluwarsa","Tempat Pembayaran","Nomor BG/Check","Tanggal Pencairan"}, //8-11
	{"Bank Name","Bank Address","Swift Code","Account Name","Account Number","Name on Card","Card Number","Card Id", //0-7
	"Expired Date","Pay Address","BG/Check Number","Due Date"} //8-11
};
%>

<!-- JSP Block -->
<%
int iErrCode = FRMMessage.ERR_NONE;
String errMsg = "";
int recordToGet = 10;
int vectSize = 0;
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
long oidInvoiceSelected = FRMQueryString.requestLong(request, "oid_invoice_selected");
long oidPaymentSelected = FRMQueryString.requestLong(request, "oid_payment_selected");
long oidPaymentDetailSelected = FRMQueryString.requestLong(request, "oid_payment_detail_selected");
//currency
long oidCurrency = FRMQueryString.requestLong(request, "oid_currency_type");
String recCode = FRMQueryString.requestString(request, "code_receive");
long oidReceive = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidPurchaseOrder = FRMQueryString.requestLong(request,"hidden_purchase_order_id");

//tambahan
long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
Date billDate = FRMQueryString.requestDate(request, "biil_date");
String invoiceNumber = FRMQueryString.requestString(request, "inv_number");
long customerId = FRMQueryString.requestLong(request, "cust_id");
String custName = FRMQueryString.requestString(request, "cust_name");
String personName = FRMQueryString.requestString(request, "person_name");
double amountTotalPaid =  FRMQueryString.requestLong(request,FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_AMOUNT]);

CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
FrmBillMain frmBillMain = ctrlBillMain.getForm();
int statusDate = FRMQueryString.requestInt(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_STATUS_DATE]);
Date datefrom = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_FROM]);
Date dateto = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_TO]);

long cashCashierId=FRMQueryString.requestLong(request, FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_CASH_CASHIER_ID]);//"504404544260781250";
long locationId=FRMQueryString.requestLong(request, FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_LOCATION_ID]);//"504404544214899588";
long shiftId=FRMQueryString.requestLong(request, FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SHIFT_ID]);//"504404535341007539";

Vector records = new Vector();
ControlLine ctrLine = new ControlLine();
ControlLine ctrLineAllPayment = new ControlLine();
Control control = new Control();

com.dimata.posbo.entity.search.SrcAccPayable srcAccPayable = new SrcAccPayable();
FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable(request, srcAccPayable);
if(iCommand != Command.LIST) {
	try{
		srcAccPayable = (SrcAccPayable)session.getValue(SessAccPayable.SESS_ACC_PAYABLE);
		if (srcAccPayable == null)
			srcAccPayable = new SrcAccPayable();
	}catch(Exception e){
		srcAccPayable = new SrcAccPayable();
	}
}else {
    frmSrcAccPayable.requestEntityObject(srcAccPayable);
    //vector status
      Vector vectSt = new Vector(1,1);
       String[] strStatus = request.getParameterValues(FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_AP_STATUS]);
       if(strStatus!=null && strStatus.length>0) {
         for(int i=0; i<strStatus.length; i++) {
	   try {
                    vectSt.add(strStatus[i]);
                }
            catch(Exception exc) {
                    System.out.println("err");
                    }
                 }
            }
       srcAccPayable.setApstatus(vectSt);
       session.putValue(SessAccPayable.SESS_ACC_PAYABLE, srcAccPayable);
}

vectSize = 	SessAccPayable.countListAP(srcAccPayable,oidReceive );

if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)) {
	start = control.actionList(iCommand, start, vectSize, recordToGet);
}

String whereClauseBillMain = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = '0' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] +" = '"+customerId+"'";
String orderClauseBillMain = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
BillMain bMain = new BillMain();
bMain.setBillDate(billDate);
bMain.setInvoiceNumber(invoiceNumber);
bMain.setCustomerId(customerId);
bMain.setStatusDate(statusDate);
bMain.setDatefrom(datefrom);
bMain.setDateto(dateto);
bMain.setCustName(custName);
bMain.setPersonName(personName);
records = PstBillMain.listSrc(0, bMain, 0, whereClauseBillMain, orderClauseBillMain);//SessAccPayable.getListAP(srcAccPayable, start, recordToGet,oidReceive );

/** ACTION PROCESS */
CtrlCashCreditPaymentMain ctrlCashCreditPaymentMain = new CtrlCashCreditPaymentMain(request);

iErrCode = ctrlCashCreditPaymentMain.action(iCommand, oidPaymentSelected, oidInvoiceSelected,0);
FrmCashCreditPaymentMain frmCashCreditPaymentMain = ctrlCashCreditPaymentMain.getForm();
CreditPaymentMain ap = ctrlCashCreditPaymentMain.getCreditPaymentMain();
errMsg = ctrlCashCreditPaymentMain.getMessage();

CashCreditPaymentInfo cashCreditPaymentInfo = ctrlCashCreditPaymentMain.getCashCreditPaymentInfo();
Vector listPaymentSystem = PstPaymentSystem.list(0, 0, "", "");
Vector listCurrencyType = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE, "");

Vector vBankInfo = new Vector();
Vector vCardInfo = new Vector();
Vector vCheckBgInfo = new Vector();
Vector vDueDateInfo = new Vector();

if(listPaymentSystem != null && listPaymentSystem.size() > 0){
    PaymentSystem paymentSystem = new PaymentSystem();
    for(int i=0; i< listPaymentSystem.size(); i++){
        paymentSystem = (PaymentSystem)listPaymentSystem.get(i);
        if(paymentSystem.isBankInfoOut()){
            vBankInfo.add(paymentSystem.getOID()+"");
        } else if(paymentSystem.isCardInfo()){
            vCardInfo.add(paymentSystem.getOID()+"");
        } else if(paymentSystem.isCheckBGInfo()){
            vCheckBgInfo.add(paymentSystem.getOID()+"");
        }

        if(paymentSystem.isDueDateInfo()){
            vDueDateInfo.add(paymentSystem.getOID() + "");
        }
    }
}

/** Untuk mendapatkan besarnya daily rate per satuan default (:currency rate = 1) */
/**
 * Yang dibagi dengan variabel dailyRate yaitu:
 * dr pada function javascript:calculate(), totalPayment, apPayment dan amount
 * Ini berfungsi untuk mengasilkan amount yang sesuai dengan search key yang diminta (Rp atau USD)
 */
long oidCurrencyPayable = 0;
String oidX="504404261456952182";
oidCurrencyPayable = srcAccPayable.getCurrencyId();
if(oidCurrencyPayable == 0){
    oidCurrencyPayable = Long.parseLong(oidX);
}
//String whereClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
String whereClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+oidCurrencyPayable;
String orderClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
Vector listDailyRate2 = PstDailyRate.list(0, 0, whereClause2, orderClause2);
DailyRate dr2 = (DailyRate)listDailyRate2.get(0);
double dailyRate = dr2.getSellingRate();


/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);

Vector compTelpFax = (Vector)companyAddress.get(2);
headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));

//Mencari Shift Id, cascashier id, location id
//long shiftId = 0;
//long cashcashierId = 0;
//long cashmasterId = 0;
//long locationId = 0;
//double taxValue = 0.00;
//double taxPct = 0.00;
//int taxCode = 0;
long cashmasterId=0;
Vector listCashCashier = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] + " ='" + userId + "' AND " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " = '1'", "");
if(listCashCashier.size()>0){
  for (int i = 0; i < listCashCashier.size(); i++) {
    CashCashier cashCashier = (CashCashier) listCashCashier.get(i);
    cashCashierId = cashCashier.getOID();
    shiftId = cashCashier.getShiftId();
    cashmasterId = cashCashier.getCashMasterId();
  }
}else{
    
}

Vector listLocation = PstCashMaster.list(0, 0, PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] + " ='" + cashmasterId + "'", "");
for (int i = 0; i < listLocation.size(); i++) {
    CashMaster cashMaster = (CashMaster) listLocation.get(i);
    locationId = cashMaster.getLocationId();
}

if(iCommand==Command.UPDATE){
    //HttpServletRequest req, long oidCustomer, double amountTotal, long oidCashCashier, long shiftID
    int xxx = ctrlCashCreditPaymentMain.requestPayment(request, customerId,amountTotalPaid,cashCashierId,shiftId,locationId,userId);
}

%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>

function changePaymentSystem() {
    var infoSts = false;
    var dueDateSts = false

    var paymentId = document.frap.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>.value;
    <%
        for(int i=0; i<vBankInfo.size(); i++){
    %>
            if(paymentId == '<%=vBankInfo.get(i)%>'){
                infoSts = true;
                document.all.paymentSystem.style.display = '';
                setDisplayInfo('', '', '', '', '', 'none', 'none', 'none', 'none', 'none', 'none')
            }
    <%
        }
    %>

    <%
    for(int i=0; i<vCardInfo.size(); i++){
    %>
            if(paymentId == '<%=vCardInfo.get(i)%>'){
                infoSts = true;
                document.all.paymentSystem.style.display = '';
                setDisplayInfo('', 'none', 'none', 'none', 'none', '', '', '', '', '', 'none')
            }
    <%
    }
    %>

    <%
    for(int i=0; i<vCheckBgInfo.size(); i++){
    %>
            if(paymentId == '<%=vCheckBgInfo.get(i)%>'){
                infoSts = true;
                document.all.paymentSystem.style.display = '';
                setDisplayInfo('', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', '')
            }
    <%
    }
    %>

    <%
    for(int i=0; i<vDueDateInfo.size(); i++){
    %>
            if(paymentId == '<%=vDueDateInfo.get(i)%>'){
                dueDateSts = true;
                document.all.paymentSystem.style.display = '';
                document.all.dueDate.style.display = '';
            }
    <%
    }
    %>

    if(!infoSts){
        if(!dueDateSts){
            document.all.paymentSystem.style.display = 'none';
        }
        setDisplayInfo('none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none')
    }
    if(!dueDateSts){
        document.all.dueDate.style.display = 'none';
    }

}

function setDisplayInfo(ibankName, ibankAdd, iSwiftCode, iAccName, iAccNum, iNameOnCard, iCardNum,
            iCardId, iExpDate, iPayAdd, iBGCheckNum){
    document.all.bankName.style.display = ibankName;
    document.all.bankAddress.style.display = ibankAdd;
    document.all.swiftCode.style.display = iSwiftCode;
    document.all.accountName.style.display = iAccName;
    document.all.accountNumber.style.display = iAccNum;
    document.all.nameOnCard.style.display = iNameOnCard;
    document.all.cardNumber.style.display = iCardNum;
    document.all.cardId.style.display = iCardId;
    document.all.expiredDate.style.display = iExpDate;
    document.all.payAddress.style.display = iPayAdd;
    document.all.bgCheckNumber.style.display = iBGCheckNum;
}

function changeDailyRate() {
	var currency = document.frap.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CURRENCY_ID]%>.value;
	if(isNaN(currency) || currency == null) {
		currency = 0;
	}
	switch(currency) {
		<%
		for(int i=0; i<listCurrencyType.size(); i++) {
			CurrencyType currType = (CurrencyType)listCurrencyType.get(i);
		%>
			case "<%=currType.getOID()%>":
				<%
				String whereClause = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+currType.getOID();
				String orderClause = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
				Vector listDailyRate = PstDailyRate.list(0, 0, whereClause, orderClause);
				DailyRate dr = (DailyRate)listDailyRate.get(0);
				%>
				document.frap.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]%>.value = '<%=FRMHandler.userFormatStringDecimal(dr.getSellingRate())%>';
				break;
		<%
		}
		%>
			default:
				break;
	}
	calculate();
}

function calculate() {
	var rate = cleanNumberFloat(document.frap.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]%>.value, guiDigitGroup, guiDecimalSymbol);
	var amount = cleanNumberFloat(document.frap.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_AMOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);

	if(isNaN(rate) || rate == "") {
		rate = 0;
	}

	if(isNaN(amount) || amount == "") {
		amount = 0;
	}
	var dr = parseFloat('<%=dailyRate%>');
	var totalAmount = (amount * rate)/dr;
	document.frap.amount.value = formatFloat(totalAmount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
}

function printForm() {
	window.open("buffer_ap_pdf.jsp","form_ap");
}

<!--
function hideObjectForMarketing(){
}

function hideObjectForWarehouse(){
}

function hideObjectForProduction(){
}

function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}
function hideObjectForSystem(){
}

function cmdBackToSearch(){
	document.frap.command.value="<%=Command.BACK%>";
	document.frap.action="srcCustomerInvoice.jsp";
	document.frap.submit();
}

function cmdListFirst(){
	document.frap.command.value="<%=Command.FIRST%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdListPrev(){
	document.frap.command.value="<%=Command.PREV%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdListNext(){
	document.frap.command.value="<%=Command.NEXT%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdListLast(){
	document.frap.command.value="<%=Command.LAST%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdListPayment(oid){
	document.frap.command.value="<%=Command.SUBMIT%>";
	document.frap.oid_invoice_selected.value=oid;
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdEditPayment(oidPayment, oidPaymentDetail){
	document.frap.command.value="<%=Command.EDIT%>";
	document.frap.oid_payment_selected.value=oidPayment;
	document.frap.oid_payment_detail_selected.value=oidPaymentDetail;
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdAdd(){
        // alert("askal");
	document.frap.command.value="<%=Command.ADD%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdAddAllPayment(){
	document.frap.command.value="<%=Command.ADDALL%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdEdit(oid){
	document.frap.command.value="<%=Command.EDIT%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdSave(){
        //var ap_saldo = parseFloat(cleanNumberFloat(document.frap.ap_saldo.value, guiDigitGroup, guiDecimalSymbol));
	/*var amount = parseFloat(cleanNumberFloat(document.frap.amount.value, guiDigitGroup, guiDecimalSymbol));
	var iCommand = <%=iCommand%>;
        alert(amount);*/
        //alert(ap_saldo);
	/*if(amount > 0) {
		if((amount <= ap_saldo && iCommand == <%=Command.ADD%>) || (amount <= (ap_saldo + amount) && iCommand == <%=Command.EDIT%>)) {
			document.frap.command.value="<%=Command.SAVE%>";
			document.frap.action="payment_credit.jsp";
			document.frap.submit();
		}
		else {
			alert("<%=textGlobalTitle[SESS_LANGUAGE][18]%>");
		}
	}
	else {
		alert("<%=textGlobalTitle[SESS_LANGUAGE][19]%>");
	}*/
    var amountPaid = parseFloat(cleanNumberFloat(document.frap.amount.value, guiDigitGroup, guiDecimalSymbol));
    if(amountPaid>0){
       document.frap.command.value="<%=Command.UPDATE%>";
       document.frap.action="payment_credit.jsp";
       document.frap.submit();
    }else{
       alert("Maaf, Pembayaran Belum di Inputkan")
    }
      
}

function checkPaid(checkFungsi,amountSaldoPiutang){
    var amount = parseFloat(cleanNumberFloat(amountSaldoPiutang, guiDigitGroup, guiDecimalSymbol));
    //alert("Bayar "+amount);
    if(checkFungsi.checked == false){
        var answer = confirm("Yakin Membatalkan Pembayaran Invoice Ini?")
        if (answer){
           var amountPaid = parseFloat(cleanNumberFloat(document.frap.paymentPaid.value, guiDigitGroup, guiDecimalSymbol));
           var xx =amountPaid-amount;
           var invoicex =formatFloat(xx, '', guiDigitGroup, guiDecimalSymbol, decPlace);
           var amountPaidx = parseFloat(cleanNumberFloat(document.frap.amount.value, guiDigitGroup, guiDecimalSymbol));
           var balance = amountPaidx-xx;
           var balancex = formatFloat(balance, '', guiDigitGroup, guiDecimalSymbol, decPlace);
           
           document.frap.paymentPaid.value=invoicex;
           document.frap.balance.value=balancex;
        }
    }else{
        //disini di check apakah nilai payment nya di isi apa enggak
        var amountPaid = parseFloat(cleanNumberFloat(document.frap.amount.value, guiDigitGroup, guiDecimalSymbol));
        var totalPayment =parseFloat(cleanNumberFloat(document.frap.paymentPaid.value, guiDigitGroup, guiDecimalSymbol));
        if(totalPayment>0){
             var invoice = totalPayment+amount;
             var balance = amountPaid-invoice;
             if(balance>0){
                 var balancex =formatFloat(balance, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                 var invoicex =formatFloat(invoice, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                 document.frap.paymentPaid.value=invoicex;
                 document.frap.balance.value=balancex;
             }else{
                 var answer = confirm("Maaf, Pembayaran Kurang. Tetap Proses Pembayaran Invoice Ini, Invoice akan di bayar sebagai sisa?");
                 if (answer){
                     var balancex =formatFloat(balance, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                     var invoicex =formatFloat(amountPaid, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                     document.frap.paymentPaid.value=invoicex;
                     document.frap.balance.value="0.00";
                 }
             }
        }else{
            //alert("y");
            if(amountPaid>0){
                 var balance = amountPaid-amount;
                 var balancex =formatFloat(balance, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                 var invoicex = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                 document.frap.paymentPaid.value=invoicex;
                 document.frap.balance.value=balancex;
            }else{
                alert("Maaf, Pembayaran Belum di Inputkan")
                checkFungsi.checked=false;
            }
        }
    }
}

function cmdDelete(){
	document.frap.command.value="<%=Command.ASK%>";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdConfirmDelete(oid){
	document.frap.command.value="<%=Command.DELETE%>";
	document.frap.FRM_ACC_PAYABLE_ID.value=oid;
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdBack(){
	document.frap.command.value="<%=Command.BACK%>";
	document.frap.oid_invoice_selected.value="0";
	document.frap.oid_payment_selected.value="0";
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}
function cmdAsk(oid){
	document.frap.command.value="<%=Command.ASK%>";
	document.frap.oid_payment_selected.value=oid;
	document.frap.action="payment_credit.jsp";
	document.frap.submit();
}

function cmdBackToReceive(oid,oidPurchase){
	document.frap.command.value="<%=Command.EDIT%>";
        document.frap.hidden_receive_id.value=oid;
        <%if(oidPurchaseOrder !=0){%>
         document.frap.action="../../warehouse/material/receive/receive_wh_supp_po_material_edit.jsp";
         <%}else {%>
	document.frap.action="../../warehouse/material/receive/receive_wh_supp_material_edit.jsp";
        <%}%>
	document.frap.submit();
}


function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


function setstock(prodOID,wareHouse0ID){
  	window.open("set_stock.jsp","",'status=yes, scrollbars,width=650,height=550');
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader">Payment</td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frap" method="post" action="">
            <input type="hidden" name="command" value="<%=iCommand%>">
            <input type="hidden" name="start" value="<%=start%>">
            <input type="hidden" name="oid_invoice_selected" value="<%=oidInvoiceSelected%>">
            <input type="hidden" name="oid_payment_selected" value="<%=oidPaymentSelected%>">
            <input type="hidden" name="oid_payment_detail_selected" value="<%=oidPaymentDetailSelected%>">
            <input type="hidden" name="oid_currency_type" value="<%=oidCurrency%>">
            <input type="hidden" name="hidden_receive_id" value="<%=oidReceive%>">
            <input type="hidden" name="hidden_purchase_order_id" value="<%=oidPurchaseOrder%>">
            <input type="hidden" name="cust_id" value="<%=customerId%>">
            <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
           
            <input type="hidden" name="<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashCashierId%>">
            <input type="hidden" name="<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_BILL_MAIN_ID]%>" value="<%=oidBillMain%>">
            <input type="hidden" name="<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
            <input type="hidden" name="<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
            <!--input type="hidden" name="<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SALES_CODE]%>" value="<//%=salesCode%>"-->
            <table width="100%" border="0" cellspacing="1" cellpadding="1">
               <%
                Vector listPaySysKey = new Vector(1,1);
                Vector listPaySysVal = new Vector(1,1);
                for(int k=0; k<listPaymentSystem.size(); k++) {
                        PaymentSystem paymentSystem = (PaymentSystem)listPaymentSystem.get(k);
                        listPaySysKey.add(paymentSystem.getPaymentSystem());
                        listPaySysVal.add(String.valueOf(paymentSystem.getOID()));
                }

                Vector listCurrTypeKey = new Vector(1,1);
                Vector listCurrTypeVal = new Vector(1,1);
                for(int l=0; l<listCurrencyType.size(); l++) {
                        CurrencyType currencyType = (CurrencyType)listCurrencyType.get(l);
                        listCurrTypeKey.add(currencyType.getCode());
                        listCurrTypeVal.add(String.valueOf(currencyType.getOID()));
                }

                %>
               <tr>
                  <td valign="middle" colspan="2">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                      <tr>
                        <td height="8" class="comment">All Payment</td>
                      </tr>
                      <tr>
                        <td width="100%">
                          <table width="100%"  border="0" class="listgen" cellspacing="1" cellpadding="1">
                              <% if(iCommand == Command.ADDALL) { %>
                                  <tr class="listgentitle" align="center">
                                    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][0]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][2]%></strong></td>
                                    <td width="10%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][3]%></strong></td>
                                    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][4]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][5]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][6]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][7]%></strong></td>
                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][8]%></strong></td>
                                 </tr>
                                 <tr class="listgensell">
                                        <td><%=ControlDate.drawDate(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_DATETIME], new Date(), "formElemen", 1,-5) %>
                                           <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CREDIT_MAIN_ID]%>" value="">
                                        </td>
                                        <td><%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE], null, "", listPaySysVal, listPaySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%></td>
                                        <td><%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CURRENCY_ID], null, "", listCurrTypeVal, listCurrTypeKey, "onchange=\"changeDailyRate();\"", "formElemen")%></td>
                                        <td align="right"><input type="text" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]%>"  value="" class="formElemen" size="14"></td>
                                        <td align="right"><input type="text" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_AMOUNT]%>"  value="" class="formElemen" size="15" onKeyUp="javascript:calculate()"></td>
                                        <td align="right"><input type="text" name="amount"  value="" class="formElemen" size="15" disabled></td>
                                        <td align="right"><input type="text" name="paymentPaid"  value="" class="formElemen" size="15" disabled></td>
                                        <td align="right"><input type="text" name="balance"  value="" class="formElemen" size="15" disabled></td>
                                 </tr>
                              <%}%>
                              <tr id="paymentSystem" style="YES; display: none;"  class="listgensell">
                                 <td valign="middle" colspan="8">
                                     <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                         <tr>
                                            <td colspan="1" class="comment" align="center">
                                              <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PURCH_PAYMENT_ID]%>" value="<%=cashCreditPaymentInfo.getPaymentId()%>">
                                              <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAYMENT_INFO_ID]%>" value="<%=cashCreditPaymentInfo.getOID()%>">
                                              <%=textGlobalTitle[SESS_LANGUAGE][16]%>
                                            </td>
                                            <td colspan="5"  class="listgensell">
                                              <table>
                                                    <tr id="bankName" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][0]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_NAME]%>" size="50" value="<%=cashCreditPaymentInfo.getDebitBankName()%>"></td>
                                                    </tr>
                                                    <tr id="bankAddress" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][1]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_ADDRESS]%>" size="60" value="<%=cashCreditPaymentInfo.getStBankAddress()%>"></td>
                                                    </tr>
                                                    <tr id="swiftCode" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][2]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_SWIFT_CODE]%>" size="40" value="<%=cashCreditPaymentInfo.getStSwiftCade()%>"></td>
                                                    </tr>
                                                    <tr id="accountName" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][3]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NAME]%>" size="50" value="<%=cashCreditPaymentInfo.getChequeAccountName()%>"></td>
                                                    </tr>
                                                    <tr id="accountNumber" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][4]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NUMBER]%>" size="40" value="<%=cashCreditPaymentInfo.getStAccountNumber()%>"></td>
                                                    </tr>
                                                    <tr id="nameOnCard" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][5]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_NAME_ON_CARD]%>" size="50" value="<%=cashCreditPaymentInfo.getStNameOnCard()%>"></td>
                                                    </tr>
                                                    <tr id="cardNumber" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][6]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_NUMBER]%>" size="40" value="<%=cashCreditPaymentInfo.getStCardNumber()%>"></td>
                                                    </tr>
                                                    <tr id="cardId" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][7]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_ID]%>" size="40" value="<%=cashCreditPaymentInfo.getStCardId()%>"></td>
                                                    </tr>
                                                    <tr id="expiredDate" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][8]%></td>
                                                      <td>:</td>
                                                      <td><%=ControlDate.drawDateMY(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_EXPIRED_DATE], cashCreditPaymentInfo.getDtExpiredDate() == null ? new Date(): cashCreditPaymentInfo.getDtExpiredDate(),"","formElemen",0,5)%></td>
                                                    </tr>
                                                    <tr id="payAddress" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][9]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAY_ADDRESS]%>" size="60" value="<%=cashCreditPaymentInfo.getStPaymentAddress()%>"></td>
                                                    </tr>
                                                    <tr id="bgCheckNumber" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][10]%></td>
                                                      <td>:</td>
                                                      <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CHECK_BG_NUMBER]%>" size="40" value="<%=cashCreditPaymentInfo.getStCheckBGNumber()%>"></td>
                                                    </tr>
                                                    <tr id="dueDate" style="YES; display: none;">
                                                      <td><%=textListPaymentInfo[SESS_LANGUAGE][11]%></td>
                                                      <td>:</td>
                                                      <td><%=ControlDate.drawDate(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_DUE_DATE], cashCreditPaymentInfo.getDueDate() == null ? new Date(): cashCreditPaymentInfo.getDueDate(),"formElemen", 0, 0)%></td>
                                                    </tr>
                                              </table>
                                            </td>
                                     </tr>
                                 </table>
                                </td>
                              </tr>
                             <tr class="listgensell">
                                <td colspan="8">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <% if(iCommand != Command.ADDALL) { %>
                                      <tr>
                                            <td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td width="25"><a href="javascript:cmdAddAllPayment()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image2611','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=textGlobalTitle[SESS_LANGUAGE][4]%>" name="Image2611" width="24" height="24" border="0" id="Image2611"></a></td>
                                            <td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td width="735" class="command"><a href="javascript:cmdAddAllPayment()"><strong><%=textGlobalTitle[SESS_LANGUAGE][4]%></strong></a></td>
                                      </tr>
                                      <% } %>
                                      <tr>
                                        <td colspan="4">
                                                    <%
                                                    ctrLineAllPayment.setLocationImg(approot+"/images");

                                                    // set image alternative caption
                                                    ctrLineAllPayment.setSaveImageAlt(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_SAVE,true));
                                                    ctrLineAllPayment.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLineAllPayment.CMD_BACK,true) : ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLineAllPayment.CMD_BACK,true)+" List");
                                                    ctrLineAllPayment.setDeleteImageAlt(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_ASK,true));
                                                    ctrLineAllPayment.setEditImageAlt(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_CANCEL,false));

                                                    ctrLineAllPayment.initDefault();
                                                    ctrLineAllPayment.setTableWidth("100%");
                                                    String scomDel = "javascript:cmdAsk('"+oidPaymentSelected+"')";
                                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidPaymentSelected+"')";
                                                    String scancel = "javascript:cmdBack()";
                                                    ctrLineAllPayment.setCommandStyle("command");
                                                    ctrLineAllPayment.setColCommStyle("command");

                                                    // set command caption
                                                    ctrLineAllPayment.setAddCaption(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_ADD,true));
                                                    ctrLineAllPayment.setSaveCaption(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_SAVE,true));
                                                    ctrLineAllPayment.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLineAllPayment.CMD_BACK,true) : ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLineAllPayment.CMD_BACK,true)+" List");
                                                    ctrLineAllPayment.setDeleteCaption(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_ASK,true));
                                                    ctrLineAllPayment.setConfirmDelCaption(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_DELETE,true));
                                                    ctrLineAllPayment.setCancelCaption(ctrLineAllPayment.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLineAllPayment.CMD_CANCEL,false));

                                                    if(privDelete){
                                                            ctrLineAllPayment.setConfirmDelCommand(sconDelCom);
                                                            ctrLineAllPayment.setDeleteCommand(scomDel);
                                                            ctrLineAllPayment.setEditCommand(scancel);
                                                    }else{
                                                            ctrLineAllPayment.setConfirmDelCaption("");
                                                            ctrLineAllPayment.setDeleteCaption("");
                                                            ctrLineAllPayment.setEditCaption("");
                                                    }

                                                    if(privAdd==false && privUpdate==false){
                                                            ctrLineAllPayment.setSaveCaption("");
                                                    }

                                                    if(iCommand != Command.EDIT || iCommand == Command.ADD){
                                                            ctrLineAllPayment.setDeleteCaption("");
                                                    }

                                                    if(privAdd==false || iCommand == Command.ADD){
                                                            ctrLineAllPayment.setAddCaption("");
                                                    }

                                                    if(iCommand==Command.SAVE && frmCashCreditPaymentMain.errorSize()==0 || iCommand == Command.ADD){
                                                            ctrLineAllPayment.setSaveCaption("");
                                                            ctrLineAllPayment.setBackCaption("");
                                                    }
                                                    %> <%=ctrLineAllPayment.drawImage(iCommand,iErrCode,errMsg)%>
                                        </td>
                                      </tr>
                                    </table>
                                </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
               </tr>
              <tr>
                  <td valign="middle" colspan="2">
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                      <tr>
                        <td height="8" class="comment">&nbsp;</td>
                      </tr>
					  <% if(records.size() > 0) { /*tampilan ini untuk kondisi list yang dicari not empty*/ %>
                                          <tr>
                                            <td width="100%">
                                              <table width="100%"  border="0" class="listgen" cellspacing="1" cellpadding="1">
                                                <tr class="listgentitle">
                                                  <td align="center" width="2%"><strong>No</strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][0]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][1]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][2]%></strong></td>
                                                  <td align="center" width="8%"><strong><%=textListHeader[SESS_LANGUAGE][3]%></strong></td>
                                                  <td align="center" width="8%"><strong>MATA UANG</strong></td>
                                                  <td align="center" width="8%"><strong>RATE</strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][4]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][5]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][6]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][7]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][8]%></strong></td>
                                                  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][9]%></strong></td>
                                                  <%
                                                  listTableHeaderPdf.add("No");
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][0]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][1]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][2]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][3]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][4]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][5]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][6]);
                                                  listTableHeaderPdf.add(textListHeader[SESS_LANGUAGE][7]);

                                                  pdfContent.add(headerPdf);
                                                  pdfContent.add(listTableHeaderPdf);
                                                  pdfContent.add(srcAccPayable);
                                                                              %>
                                                </tr>
                                                                            <%
                                                                            double subTotalAmount = 0;
                                                                            double subTotalTax = 0;
                                                                            double subTotalRetur = 0;
                                                                            double subTotalPayment = 0;
                                                                            double subTotalApSaldo = 0;

                                                                            /*double totalAmount = 0;
                                                                            double totalTax = 0;
                                                                            double totalRetur = 0;
                                                                            double totalPayment = 0;
                                                                            double totalApSaldo = 0;*/
                                                                            
                                                                           /* Vector grandTotal = SessAccPayable.getListAP(srcAccPayable, 0, 0, oidReceive);
                                                                            for(int m=0; m<grandTotal.size(); m++) {
                                                                                    Vector temp3 = (Vector)grandTotal.get(m);
                                                                                    totalAmount += Double.parseDouble((String)temp3.get(5));
                                                                                    totalTax += Double.parseDouble((String)temp3.get(6));
                                                                            }
                                                                            
                                                                            totalRetur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, oidReceive);
                                                                            totalPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, oidReceive))/dailyRate;
                                                                            totalApSaldo = (totalAmount + totalTax) - totalPayment - totalRetur;*/

                                                                            for(int i=0; i<records.size(); i++) {
                                                                                    Vector vt = (Vector) records.get(i);
                                                                                    BillMain billMain = (BillMain) vt.get(0);
                                                                                    MemberReg memberReg = (MemberReg) vt.get(1);
                                                                                    CurrencyType currencyTypeX = (CurrencyType) vt.get(2);
                                                                                    AppUser appUser = (AppUser) vt.get(3);

                                                                                    long oidInvoice = billMain.getOID();//Long.parseLong((String)temp.get(0));
                                                                                    double total = (billMain.getAmount()*billMain.getRate())+(billMain.getServiceValue()*billMain.getRate())-(billMain.getDiscount()*billMain.getRate());//Double.parseDouble((String)temp.get(5));
                                                                                    double tax = billMain.getTaxValue()* billMain.getRate();
                                                                                    double retur = SessAr.setReturnFromInvoiceId(billMain.getOID());
                                                                                    double apPayment = SessAr.getSumPaymentForArInvoice(billMain.getOID());
                                                                                    double apSaldo = total+tax-retur-apPayment;//(total + tax) - apPayment - retur;

                                                                                    subTotalAmount += total;
                                                                                    subTotalTax += tax;
                                                                                    subTotalRetur += retur;
                                                                                    subTotalPayment += apPayment;
                                                                                    subTotalApSaldo += apSaldo;

                                                                                    /* untuk membedakan css yang digunakan oleh selected record */
                                                                                    String recordCss = "listgensell";
                                                                                    if(oidInvoice == oidInvoiceSelected) {
                                                                                            recordCss = "listgenselledit";
                                                                                    }
                                                                            %>
                                                                            <tr class="<%=recordCss%>">
                                                                                    <td align="center"><%=(start+i+1)%></td>
                                                                                    <td><%=memberReg.getCompName()%></td>
                                                                                    <td><a href="javascript:cmdListPayment('<%=String.valueOf(billMain.getOID())%>')"><%=billMain.getInvoiceNumber()%></a></td>
                                                                                    <td><%=memberReg.getPersonName()%></td>
                                                                                    <td><%=Formater.formatDate(billMain.getBillDate(),"dd-MM-yyyy")%></td>
                                                                                    <td><%=billMain.getRate()==1?"RP":"USD"%></td>
                                                                                    <td><%=billMain.getRate()%></td>
                                                                                    <td align="right"><%=FRMHandler.userFormatStringDecimal(total)%></td>
                                                                                    <td align="right"><%=FRMHandler.userFormatStringDecimal(tax)%></td>
                                                                                    <td align="right"><%=FRMHandler.userFormatStringDecimal(retur)%></td>
                                                                                    <td align="right"><%=FRMHandler.userFormatStringDecimal(apPayment)%></td>
                                                                                    <td align="right"><%=FRMHandler.userFormatStringDecimal(apSaldo)%></td>
                                                                                    <td align="center"><input type="checkbox" name="invoice_<%=billMain.getOID()%>" value="1" onclick="checkPaid(this,'<%=apSaldo%>')">Paid</td>
                                                                            </tr>
                                                                            <%	if(oidInvoice == oidInvoiceSelected) { /*kondisi ini untuk menampilkan detail dari sebuah payment*/ %>
                                                                            <tr  class="listgensell">
                                                                              <td colspan="2" class="comment" align="center"><%=textGlobalTitle[SESS_LANGUAGE][7]%></td>
                                                                              <td colspan="9">
                                                                                <table width="100%" border="0"  class="listgen" cellspacing="1" cellpadding="1">
                                                                                  <tr class="listgentitle" align="center">
                                                                                    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][0]%></strong></td>
                                                                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][2]%></strong></td>
                                                                                    <td width="10%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][3]%></strong></td>
                                                                                    <td width="15%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][4]%></strong></td>
                                                                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][5]%></strong></td>
                                                                                    <td width="20%"><strong><%=textListHeaderDetail[SESS_LANGUAGE][6]%></strong></td>
                                                                                 </tr>
                                                                                      <%
                                                                                      //menampilkan list payment
                                                                                      RptArInvoice temp = new RptArInvoice();
                                                                                      temp.setBillMainId(oidInvoice);
                                                                                      Vector apDetail = SessAr.getPaymentForArInvoice(temp);
                                                                                      if(apDetail.size() > 0) { //kondisi untuk menampilkan list detail dari payment
                                                                                            for(int j=0; j<apDetail.size(); j++) {
                                                                                                    RptArPaymentDetail arPayment = (RptArPaymentDetail) apDetail.get(j);
                                                                                                    PaymentSystem paymentSystem = new PaymentSystem();//(PaymentSystem)apDetail.get(1);
                                                                                                    /*AccPayableDetail accPayableDetail = new AccPayableDetail();
 *                                                                                                  AccPayable accPayable = new AccPayable();*/
                                                                                                    CurrencyType currencyType = new CurrencyType();
                                                                                                   

                                                                                                    long oidPayment = arPayment.getCreditPaymentMainId();//accPayable.getOID();
                                                                                                    long oidPaymentDetail = arPayment.getOID();//accPayableDetail.getOID();
                                                                                                    double amount = arPayment.getTotalPay();//(accPayableDetail.getRate()*accPayableDetail.getAmount())/dailyRate;

                                                                                                    if((iCommand == Command.EDIT || iCommand == Command.ASK ) && oidPayment == oidPaymentSelected) {
                                                                                      %>
                                                                                      <tr class="listgensell">
                                                                                            <td>
                                                                                               <%=ControlDate.drawDate(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_DATETIME], arPayment.getPayDate(), "formElemen", 1,-5) %>
                                                                                               <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>" value="<%=oidPayment%>">
                                                                                               <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CREDIT_MAIN_ID]%>" value="<%=oidInvoice%>">
                                                                                            </td>
                                                                                            <td>
                                                                                               <%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE], null, ""+paymentSystem.getOID(), listPaySysVal, listPaySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%>
                                                                                               <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CREDIT_MAIN_ID]%>" value="<%=oidInvoice%>">
                                                                                               <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CASH_CREDIT_PAYMENT_ID]%>" value="<%=arPayment. getCreditPaymentMainId() %>">
                                                                                            </td>
                                                                                            <td><%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CURRENCY_ID], null, ""+currencyType.getOID(), listCurrTypeVal, listCurrTypeKey, "onchange=\"changeDailyRate();\"", "formElemen")%></td>
                                                                                            <td align="right"><input type="text" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]%>"  value="<%=FRMHandler.userFormatStringDecimal(arPayment.getRate())%>" class="formElemen" size="14"></td>
                                                                                            <td align="right"><input type="text" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_AMOUNT]%>"  value="<%=FRMHandler.userFormatStringDecimal(arPayment.getTotalPay())%>" class="formElemen" size="15" onKeyUp="javascript:calculate()"></td>
                                                                                            <td align="right"><input type="text" name="amount"  value="<%=FRMHandler.userFormatStringDecimal(amount)%>" class="formElemen" size="15" disabled></td>
                                                                                      </tr>
                                                                                      <%	} else { %>
                                                                                        <tr class="listgensell">
                                                                                            <td><a href="javascript:cmdEditPayment('<%=oidPayment%>', '<%=oidPaymentDetail%>')"><%=Formater.formatDate(arPayment.getPayDate(), "dd-MM-yyyy")%></a></td>
                                                                                            <td><%=arPayment.getPaymentName()%></td>
                                                                                            <td align="center"><%=arPayment.getCurrCode()%></td>
                                                                                            <td align="right"><%=FRMHandler.userFormatStringDecimal(arPayment.getRate())%></td>
                                                                                            <td align="right"><%=FRMHandler.userFormatStringDecimal(arPayment.getTotalPay())%></td>
                                                                                            <td align="right"><%=FRMHandler.userFormatStringDecimal(amount)%></td>
                                                                                        </tr>
                                                                                      <%
                                                                                                    }
                                                                                            } /*end for*/
                                                                                      } else {
                                                                                      %>
                                                                                      <tr  class="listgensell">
                                                                                            <td colspan="6" class="comment" align="center"><%=textGlobalTitle[SESS_LANGUAGE][13]%></td>
                                                                                          </tr>
                                                                                      <% }%>

                                                                                </table>
                                                                              </td>
                                                                            </tr>
                                                                            <%
                                                                                    } /*end if()*/
                                                                            } /*end for*/
                                                                            %>
                                                                        <tr  class="listgensell">
                                                                              <td colspan="7"><div align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][10]%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalAmount)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalTax)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalRetur)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalPayment)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalApSaldo)%></strong></div></td>
                                                                              <td colspan="1"><div align="right">&nbsp;</div></td>
                                                                        </tr>
                                                                        <%--
                                                                        <tr  class="listgensell">
                                                                              <td colspan="5"><div align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][11]%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalAmount)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalTax)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalRetur)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalPayment)%></strong></div></td>
                                                                              <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalApSaldo)%></strong></div></td>
                                                                              <td colspan="1"><div align="right">&nbsp;</div></td>
                                                                        </tr>
                                                                        --%>
                                              </table>
                                             </td>
                                          </tr>
					  <tr>
					  	<td>
						<span class="command">
                                                    <%--
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
						%>--%>
						</span>
						</td>
					  </tr>
					  <% } else { /*tampilan ini untuk kondisi list yang dicari empty*/ %>
                                              <tr>
                                                <td height="8" width="10">&nbsp;</td>
                                              </tr>
                                              <tr>
                                                <td height="2" width="100%" class="comment"><strong><%=textGlobalTitle[SESS_LANGUAGE][12]%></strong></td>
                                              </tr>
					  <% } %>
					  <tr>
                        <td height="8" width="10">&nbsp;</td>
                      </tr>
                    </table>
					  <table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						  <td nowrap align="left" class="command">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
                              <tr>
                                <td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"> </td>
                                <% if(oidReceive !=0) {%>
                                  <td width="25"><a href="javascript:cmdBackToReceive('<%=oidReceive%>','<%=oidPurchaseOrder%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back To Search Stock Opname"></a></td>
                                  <td width="5"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
				  <td height="22" valign="middle" colspan="3"><a href="javascript:cmdBackToReceive('<%=oidReceive%>','<%=oidPurchaseOrder%>')" class="command"><%=textGlobalTitle[SESS_LANGUAGE][20]%></a></td>
                                <%}else {%>
                                <td width="25"><a href="javascript:cmdBackToSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back To Search Stock Opname"></a></td>
                                <td width="5"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
				<td height="22" valign="middle" colspan="3"><a href="javascript:cmdBackToSearch()" class="command"><%=textGlobalTitle[SESS_LANGUAGE][3]%></a></td>
                                <%}%>
                                <%--
                                <% if(records.size() > 0 && oidReceive== 0) { %>
                                <td width="31" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" ></a></td>
                                <td width="195" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textGlobalTitle[SESS_LANGUAGE][17]%></a></td>
                                <% } %>
                                --%>
                              </tr>
                            </table></td>
						</tr>
                    </table>
                </td>
              </tr>
            </table>
			</form>
			<script language="JavaScript">
                            changeDailyRate();
			</script>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
      <%@ include file = "../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
