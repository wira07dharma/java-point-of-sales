<% 
/* 
 * Page Name  		:  payable_view.jsp
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
<%@ page import="com.dimata.posbo.form.arap.FrmAccPayableDetail"%>
<%@ page import="com.dimata.posbo.form.arap.CtrlAccPayableDetail"%>
<%@ page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textGlobalTitle[][] = {
	{
	 "Gudang","Penerimaan Barang","Rekap Hutang","Kembali ke Pencarian","Tambah Pembayaran","Simpan Pembayaran","Kembali Ke Daftar","Rincian Pembayaran", //0-7
	 "Total Pembayaran","Saldo Hutang","Sub Total","Grand Total","Daftar hutang tidak ditemukan!","Daftar pembayaran hutang tidak ditemukan!", //8-13
	 "Hutang","Pembayaran","Rincian Sistem Pembayaran","Cetak Laporan Rekap Hutang","Jumlah pembayaran melebihi saldo hutang!", //14-18
	 "Data belum lengkap!", "Kembali ke Penerimaan"
	},
	{
	 "Warehouse","Receive Goods","AP Summary","Back To Search","Add New Payment","Save Payment","Back To List","Detail Of Payment", //0-7
	 "Total of Payment","AP Balance","Sub Total","Grand Total","Account Payable list not found!","Payment list not found!", //8-13
	 "AP","Payment","Payment System Info", "Print AP Summary Report","AP payment more than AP balance!", //14-18
	 "Incomplete data!", "Back To Receive"
	}
};

public static final String textListHeader[][] = {
	{"Nama Supplier","Nomor Invoice","Keterangan","Tanggal","Total","Pajak","Retur","Pembayaran","Saldo Hutang"},
	{"Supplier Name", "Invoice Number","Remark","Invoice","Total","Tax","Return","Payment","AP Balance"}
};

public static final String textListHeaderDetail[][] = {
	{"Tanggal","Lokasi","Sistem Pembayaran","Mata Uang","Rate","Jumlah dalam Mata Uang","Jumlah"},
	{"Date","Location","Payment System","Currency","Rate","Amount in Currency","Amount"}
};

public static final String textListPaymentInfo[][] = {
	{"Nama Bank","Alamat Bank","Kode Swift","Nama Rekening","Nomor Rekening","Nama pada Kartu","Nomor Kartu","Id Kartu", //0-7
	"Kadarluwarsa","Tempat Pembayaran","Nomor BG/Check","Jatuh Tempo"}, //8-11
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

Vector records = new Vector();
ControlLine ctrLine = new ControlLine();
Control control = new Control();

com.dimata.posbo.entity.search.SrcAccPayable srcAccPayable = new SrcAccPayable();
FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable(request, srcAccPayable);
/*frmSrcAccPayable.requestEntityObject(srcAccPayable);
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
            }*/

if(iCommand != Command.LIST) {
	try{ 
		srcAccPayable = (SrcAccPayable)session.getValue(SessAccPayable.SESS_ACC_PAYABLE); 
		if (srcAccPayable == null)
			srcAccPayable = new SrcAccPayable();			
	}catch(Exception e){ 
		srcAccPayable = new SrcAccPayable();
	}
}
else {
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
//srcAccPayable.setApstatus(vectSt);
//session.putValue(SessAccPayable.SESS_ACC_PAYABLE, srcAccPayable);
  

//vectSize = 	SessAccPayable.countListAP(srcAccPayable);
vectSize = 	SessAccPayable.countListAP(srcAccPayable,oidReceive );

if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)) {
	start = control.actionList(iCommand, start, vectSize, recordToGet);
}

//records = SessAccPayable.getListAP(srcAccPayable, start, recordToGet);
records = SessAccPayable.getListAP(srcAccPayable, start, recordToGet,oidReceive );

/** ACTION PROCESS */
CtrlAccPayable ctrlAccPayable = new CtrlAccPayable(request);
iErrCode = ctrlAccPayable.action(iCommand, oidPaymentSelected, oidPaymentDetailSelected);
FrmAccPayable frmAccPayable = ctrlAccPayable.getForm();
AccPayable ap = ctrlAccPayable.getAccPayable();
errMsg = ctrlAccPayable.getMessage();

PaymentInfo paymentInfo = ctrlAccPayable.getPaymentInfo();
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
oidCurrencyPayable = srcAccPayable.getCurrencyId();
if(oidCurrencyPayable == 0){
    oidCurrencyPayable = oidCurrency;
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

%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>

function changePaymentSystem() {
    var infoSts = false;
    var dueDateSts = false

    var paymentId = document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_PAYMENT_SYSTEM_ID]%>.value;
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
	var currency = document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_CURRENCY_TYPE_ID]%>.value;
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
				document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_RATE]%>.value = '<%=FRMHandler.userFormatStringDecimal(dr.getSellingRate())%>';
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
	var rate = cleanNumberFloat(document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_RATE]%>.value, guiDigitGroup, guiDecimalSymbol);
	var amount = cleanNumberFloat(document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_AMOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);
	
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
	document.frap.action="payable_search.jsp";
	document.frap.submit();
}

function cmdListFirst(){
	document.frap.command.value="<%=Command.FIRST%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}

function cmdListPrev(){
	document.frap.command.value="<%=Command.PREV%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}

function cmdListNext(){
	document.frap.command.value="<%=Command.NEXT%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}

function cmdListLast(){
	document.frap.command.value="<%=Command.LAST%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdListPayment(oid){
	document.frap.command.value="<%=Command.SUBMIT%>";
	document.frap.oid_invoice_selected.value=oid;
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdEditPayment(oidPayment, oidPaymentDetail){
	document.frap.command.value="<%=Command.EDIT%>";
	document.frap.oid_payment_selected.value=oidPayment;
	document.frap.oid_payment_detail_selected.value=oidPaymentDetail;
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdAdd(){
	document.frap.command.value="<%=Command.ADD%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdEdit(oid){
	document.frap.command.value="<%=Command.EDIT%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdSave(){
	var amount = parseFloat(cleanNumberFloat(document.frap.amount.value, guiDigitGroup, guiDecimalSymbol));
	var ap_saldo = parseFloat(cleanNumberFloat(document.frap.ap_saldo.value, guiDigitGroup, guiDecimalSymbol));
	var iCommand = <%=iCommand%>;
	
	if(amount > 0) {
		if((amount <= ap_saldo && iCommand == <%=Command.ADD%>) || (amount <= (ap_saldo + amount) && iCommand == <%=Command.EDIT%>)) {
			document.frap.command.value="<%=Command.SAVE%>";
			document.frap.action="payable_view.jsp";
			document.frap.submit();
		}
		else {
			alert("<%=textGlobalTitle[SESS_LANGUAGE][18]%>");
		}
	}
	else {
		alert("<%=textGlobalTitle[SESS_LANGUAGE][19]%>");
	}
}
function cmdDelete(){
	document.frap.command.value="<%=Command.ASK%>";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdConfirmDelete(oid){
	document.frap.command.value="<%=Command.DELETE%>";
	document.frap.FRM_ACC_PAYABLE_ID.value=oid;
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdBack(){
	document.frap.command.value="<%=Command.BACK%>";
	document.frap.oid_invoice_selected.value="0";
	document.frap.oid_payment_selected.value="0";
	document.frap.action="payable_view.jsp";
	document.frap.submit();
}
function cmdAsk(oid){
	document.frap.command.value="<%=Command.ASK%>";
	document.frap.oid_payment_selected.value=oid;
	document.frap.action="payable_view.jsp";
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
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textGlobalTitle[SESS_LANGUAGE][0]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][1]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][2]%>
			<!-- #EndEditable --></td>
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
            <table width="100%" border="0" cellspacing="1" cellpadding="1">                           
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
                              <td align="center" width="3%"><strong>No</strong></td>
                              <td align="center" width="15%"><strong><%=textListHeader[SESS_LANGUAGE][0]%></strong></td>
                              <td align="center" width="11%"><strong><%=textListHeader[SESS_LANGUAGE][1]%></strong></td>
                              <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][2]%></strong></td>
                              <td align="center" width="8%"><strong><%=textListHeader[SESS_LANGUAGE][3]%></strong></td>
							  <td align="center" width="11%"><strong><%=textListHeader[SESS_LANGUAGE][4]%></strong></td>
							  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][5]%></strong></td>
							  <td align="center" width="10%"><strong><%=textListHeader[SESS_LANGUAGE][6]%></strong></td>
							  <td align="center" width="11%"><strong><%=textListHeader[SESS_LANGUAGE][7]%></strong></td>
							  <td align="center" width="11%"><strong><%=textListHeader[SESS_LANGUAGE][8]%></strong></td>
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
							double totalAmount = 0;
							double totalTax = 0;
							double totalRetur = 0;
							double totalPayment = 0;
							double totalApSaldo = 0;
							
							//Vector grandTotal = SessAccPayable.getListAP(srcAccPayable, 0, 0);
                                                        Vector grandTotal = SessAccPayable.getListAP(srcAccPayable, 0, 0, oidReceive);
							for(int m=0; m<grandTotal.size(); m++) {
								Vector temp3 = (Vector)grandTotal.get(m);
								totalAmount += Double.parseDouble((String)temp3.get(5));
								totalTax += Double.parseDouble((String)temp3.get(6));
							}
							//totalRetur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, 0L);
							//totalPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, 0))/dailyRate;
                                                        totalRetur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, oidReceive);
							totalPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, oidReceive))/dailyRate;
							totalApSaldo = (totalAmount + totalTax) - totalPayment - totalRetur;
							
							for(int i=0; i<records.size(); i++) {
								Vector temp = (Vector)records.get(i);
								long oidInvoice = Long.parseLong((String)temp.get(0));
								double total = Double.parseDouble((String)temp.get(5));
								double tax = Double.parseDouble((String)temp.get(6));
								//double retur = SessMatReturn.getTotalReturnByReceive(oidInvoice);
                                                                double retur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, oidInvoice);
								double apPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, oidInvoice))/dailyRate;
								double apSaldo = (total + tax) - apPayment - retur;
								
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
								<td><%=(String)temp.get(1)%></td>
								<td><a href="javascript:cmdListPayment('<%=oidInvoice%>')"><%=(String)temp.get(2)%></a></td>
								<td><%=(String)temp.get(3)%></td>
								<td><%=Formater.formatDate((Date)temp.get(4), "dd-MM-yyyy")%></td>
								<td align="right"><%=FRMHandler.userFormatStringDecimal(total)%></td>
								<td align="right"><%=FRMHandler.userFormatStringDecimal(tax)%></td>
								<td align="right"><%=FRMHandler.userFormatStringDecimal(retur)%></td>
								<td align="right"><%=FRMHandler.userFormatStringDecimal(apPayment)%></td>
								<td align="right"><%=FRMHandler.userFormatStringDecimal(apSaldo)%></td>
							</tr>
							<%	if(oidInvoice == oidInvoiceSelected) { /*kondisi ini untuk menampilkan detail dari sebuah payment*/ %>
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
                            <tr  class="listgensell"> 
                              <td colspan="2" class="comment" align="center"><%=textGlobalTitle[SESS_LANGUAGE][7]%></td>
                              <td colspan="8"> 
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
								  Vector apDetail = SessAccPayable.getListApDetail(oidInvoice);
								  if(apDetail.size() > 0) { //kondisi untuk menampilkan list detail dari payment
								  	for(int j=0; j<apDetail.size(); j++) {
								  		Vector temp2 = (Vector)apDetail.get(j);
										AccPayable accPayable = (AccPayable)temp2.get(0);
										PaymentSystem paymentSystem = (PaymentSystem)temp2.get(1);
										CurrencyType currencyType = (CurrencyType)temp2.get(2);
										AccPayableDetail accPayableDetail = (AccPayableDetail)temp2.get(3);
										
										long oidPayment = accPayable.getOID();
										long oidPaymentDetail = accPayableDetail.getOID();
										double amount = (accPayableDetail.getRate()*accPayableDetail.getAmount())/dailyRate;
										
										if((iCommand == Command.EDIT || iCommand == Command.ASK ) && oidPayment == oidPaymentSelected) {
								  %>
								  <tr class="listgensell">
									<td>
									   <%=ControlDate.drawDate(FrmAccPayable.fieldNames[FrmAccPayable.FRM_PAYMENT_DATE], accPayable.getPaymentDate(), "formElemen", 1,-5) %>
									   <input type="hidden" name="<%=FrmAccPayable.fieldNames[FrmAccPayable.FRM_ACC_PAYABLE_ID]%>" value="<%=oidPayment%>">
									   <input type="hidden" name="<%=FrmAccPayable.fieldNames[FrmAccPayable.FRM_RECEIVE_MATERIAL_ID]%>" value="<%=oidInvoice%>">
									</td>
									<td>
									   <%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_PAYMENT_SYSTEM_ID], null, ""+paymentSystem.getOID(), listPaySysVal, listPaySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%>
									   <input type="hidden" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_ACC_PAYABLE_ID]%>" value="<%=oidInvoice%>">
									   <input type="hidden" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_ACC_PAYABLE_DETAIL_ID]%>" value="<%=accPayableDetail.getOID()%>">
									</td>
									<td><%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_CURRENCY_TYPE_ID], null, ""+currencyType.getOID(), listCurrTypeVal, listCurrTypeKey, "onchange=\"changeDailyRate();\"", "formElemen")%></td>
									<td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_RATE]%>"  value="<%=FRMHandler.userFormatStringDecimal(accPayableDetail.getRate())%>" class="formElemen" size="14"></td>
									<td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_AMOUNT]%>"  value="<%=FRMHandler.userFormatStringDecimal(accPayableDetail.getAmount())%>" class="formElemen" size="15" onKeyUp="javascript:calculate()"></td>
									<td align="right"><input type="text" name="amount"  value="<%=FRMHandler.userFormatStringDecimal(amount)%>" class="formElemen" size="15" disabled></td>
								  </tr>
								  <%	} else { %>
                                  <tr class="listgensell">
								  	<td><a href="javascript:cmdEditPayment('<%=oidPayment%>', '<%=oidPaymentDetail%>')"><%=Formater.formatDate(accPayable.getPaymentDate(), "dd-MM-yyyy")%></a></td>
									<td><%=paymentSystem.getPaymentSystem()%></td>
									<td align="center"><%=currencyType.getCode()%></td>
									<td align="right"><%=FRMHandler.userFormatStringDecimal(accPayableDetail.getRate())%></td>
									<td align="right"><%=FRMHandler.userFormatStringDecimal(accPayableDetail.getAmount())%></td>
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
								  <% } /*end if untuk kondisi menampilkan list detail dari payment*/%>
								  
								  <% if(iCommand == Command.ADD) { %>
                                  <tr class="listgensell">
								  	<td>
									   <%=ControlDate.drawDate(FrmAccPayable.fieldNames[FrmAccPayable.FRM_PAYMENT_DATE], new Date(), "formElemen", 1,-5) %>
									   <input type="hidden" name="<%=FrmAccPayable.fieldNames[FrmAccPayable.FRM_RECEIVE_MATERIAL_ID]%>" value="<%=oidInvoice%>">
									</td>
									<td><%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_PAYMENT_SYSTEM_ID], null, "", listPaySysVal, listPaySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%></td>
									<td><%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_CURRENCY_TYPE_ID], null, "", listCurrTypeVal, listCurrTypeKey, "onchange=\"changeDailyRate();\"", "formElemen")%></td>
									<td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_RATE]%>"  value="" class="formElemen" size="14"></td>
									<td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_AMOUNT]%>"  value="" class="formElemen" size="15" onKeyUp="javascript:calculate()"></td>
									<td align="right"><input type="text" name="amount"  value="" class="formElemen" size="15" disabled></td>
								  </tr>
								   <% } /*end if ADD*/%>
								  <!-- !!! disini juga dipanggil fungsi change payment, yg akan menmapilkan form payment dari setiap payment system -->
                                  <!--%@ include file = "payment_info.jsp" menggunakan metode seperti ini, id tidak dikenal!!!%--> 
								  <tr id="paymentSystem" style="YES; display: none;"  class="listgensell"> 
									<td colspan="1" class="comment" align="center"> 
									  <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PURCH_PAYMENT_ID]%>" value="<%=paymentInfo.getlPurchPaymentId()%>">
									  <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAYMENT_INFO_ID]%>" value="<%=paymentInfo.getOID()%>">
									  <%=textGlobalTitle[SESS_LANGUAGE][16]%>
									</td>
									<td colspan="5"  class="listgensell"> 
									  <table>
										<tr id="bankName" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][0]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_NAME]%>" size="50" value="<%=paymentInfo.getStBankName()%>"></td>
										</tr>
										<tr id="bankAddress" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][1]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_ADDRESS]%>" size="60" value="<%=paymentInfo.getStBankAddress()%>"></td>
										</tr>
										<tr id="swiftCode" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][2]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_SWIFT_CODE]%>" size="40" value="<%=paymentInfo.getStSwiftCade()%>"></td>
										</tr>
										<tr id="accountName" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][3]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NAME]%>" size="50" value="<%=paymentInfo.getStAccountName()%>"></td>
										</tr>
										<tr id="accountNumber" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][4]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NUMBER]%>" size="40" value="<%=paymentInfo.getStAccountNumber()%>"></td>
										</tr>
										<tr id="nameOnCard" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][5]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_NAME_ON_CARD]%>" size="50" value="<%=paymentInfo.getStNameOnCard()%>"></td>
										</tr>
										<tr id="cardNumber" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][6]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_NUMBER]%>" size="40" value="<%=paymentInfo.getStCardNumber()%>"></td>
										</tr>
										<tr id="cardId" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][7]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_ID]%>" size="40" value="<%=paymentInfo.getStCardId()%>"></td>
										</tr>
										<tr id="expiredDate" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][8]%></td>
										  <td>:</td>
										  <td><%=ControlDate.drawDateMY(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_EXPIRED_DATE], paymentInfo.getDtExpiredDate() == null ? new Date(): paymentInfo.getDtExpiredDate(),"","formElemen",0,5)%></td>
										</tr>
										<tr id="payAddress" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][9]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAY_ADDRESS]%>" size="60" value="<%=paymentInfo.getStPaymentAddress()%>"></td>
										</tr>
										<tr id="bgCheckNumber" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][10]%></td>
										  <td>:</td>
										  <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CHECK_BG_NUMBER]%>" size="40" value="<%=paymentInfo.getStCheckBGNumber()%>"></td>
										</tr>
										<tr id="dueDate" style="YES; display: none;"> 
										  <td><%=textListPaymentInfo[SESS_LANGUAGE][11]%></td>
										  <td>:</td>
										  <td><%=ControlDate.drawDate(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_DUE_DATE], paymentInfo.getDueDate() == null ? new Date(): paymentInfo.getDueDate(),"formElemen", 0, 0)%></td>
										</tr>
									  </table>
									</td>
								  </tr>
								  <tr  class="listgensell"> 
                                    <td colspan="5" align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][8]%></strong></td>
                                    <td width="19%" align="right"><strong><%=FRMHandler.userFormatStringDecimal(apPayment)%></strong></td>
                                  </tr>
                                  <tr  class="listgensell"> 
                                    <td colspan="5" align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][9]%></strong></td>
                                    <td width="19%" align="right"><strong><%=FRMHandler.userFormatStringDecimal(apSaldo)%></strong>
									  <input type="hidden" name="ap_saldo" value="<%=apSaldo%>">
									</td>
                                  </tr>
                                  <tr  class="listgensell">
								  	<td colspan="6"> 
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<% if(iCommand == Command.SUBMIT) { %>
										  <tr>
											<td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                			<td width="25"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image2611','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=textGlobalTitle[SESS_LANGUAGE][4]%>" name="Image2611" width="24" height="24" border="0" id="Image2611"></a></td>
                                			<td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
											<td width="735" class="command"><a href="javascript:cmdAdd()"><strong><%=textGlobalTitle[SESS_LANGUAGE][4]%></strong></a></td>
											</tr>
										  <% } %>
										  <tr>
										    <td colspan="4">
												<%
												ctrLine.setLocationImg(approot+"/images");
						
												// set image alternative caption
												ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_SAVE,true));
												ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLine.CMD_BACK,true)+" List");
												ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_ASK,true));
												ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_CANCEL,false));
						
												ctrLine.initDefault();
												ctrLine.setTableWidth("100%");
												String scomDel = "javascript:cmdAsk('"+oidPaymentSelected+"')";
												String sconDelCom = "javascript:cmdConfirmDelete('"+oidPaymentSelected+"')";
												String scancel = "javascript:cmdBack()";
												ctrLine.setCommandStyle("command");
												ctrLine.setColCommStyle("command");
						
												// set command caption
												ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_ADD,true));
												ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_SAVE,true));
												ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][14],ctrLine.CMD_BACK,true)+" List");
												ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_ASK,true));
												ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_DELETE,true));
												ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textGlobalTitle[SESS_LANGUAGE][15],ctrLine.CMD_CANCEL,false));
						
												if(privDelete){
													ctrLine.setConfirmDelCommand(sconDelCom);
													ctrLine.setDeleteCommand(scomDel);
													ctrLine.setEditCommand(scancel);
												}else{
													ctrLine.setConfirmDelCaption("");
													ctrLine.setDeleteCaption("");
													ctrLine.setEditCaption("");
												}
						
												if(privAdd==false && privUpdate==false){
													ctrLine.setSaveCaption("");
												}
												
												if(iCommand != Command.EDIT){
													ctrLine.setDeleteCaption("");
												}
						
												if(privAdd==false){
													ctrLine.setAddCaption("");
												}
						
												if(iCommand==Command.SAVE && frmAccPayable.errorSize()==0){
													iCommand=Command.EDIT;
													ctrLine.setSaveCaption("");
												}
												%> <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
										    </td>
										  </tr>
										</table>
									</td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
							<%
								} /*end if()*/
							} /*end for*/
							%>
                            <tr  class="listgensell"> 
                              <td colspan="5"><div align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][10]%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalAmount)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalTax)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalRetur)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalPayment)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(subTotalApSaldo)%></strong></div></td>
                            </tr>
                            <tr  class="listgensell"> 
                              <td colspan="5"><div align="right"><strong><%=textGlobalTitle[SESS_LANGUAGE][11]%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalAmount)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalTax)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalRetur)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalPayment)%></strong></div></td>
							  <td colspan="1"><div align="right"><strong><%=FRMHandler.userFormatStringDecimal(totalApSaldo)%></strong></div></td>
							</tr>
                          </table>						  
						</td>
                      </tr>
					  <tr>
					  	<td>
						<span class="command"> 
						<%
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
						%> 
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
								<% //if(records.size() > 0) { %> 
                                                                <% if(records.size() > 0 && oidReceive== 0) { %>  
								<td width="31" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" ></a></td>
								<td width="195" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textGlobalTitle[SESS_LANGUAGE][17]%></a></td>
								<% } %>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
