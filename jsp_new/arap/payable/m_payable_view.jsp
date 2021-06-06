<%@page import="com.dimata.common.entity.system.AppValue"%>
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


<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %> 
<%@ include file = "../../main/checkuser.jsp" %>

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
long hidden_oid_pp =FRMQueryString.requestLong(request,"hidden_oid_pp"); 

long oidPPIntegrasi=0;
try{
   oidPPIntegrasi = Long.parseLong(AppValue.getValueByKey("OID_PP_INTEGRATION"));
}catch(Exception ex){
    oidPPIntegrasi=0;
}

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
iErrCode = ctrlAccPayable.action(iCommand, oidPaymentSelected, oidPaymentDetailSelected,hidden_oid_pp);
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
<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
<SCRIPT language=JavaScript>

function changePaymentSystem(oidSupplier) {
    //alert("hello");
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
    //alert("cepot"+<%=oidPPIntegrasi%>+"sdasa"+paymentId);
    if(<%=oidPPIntegrasi%>==paymentId){
        var oidVendor=oidSupplier;
        //alert("haaaa");
        cmdOpenPP(oidVendor);
    }else{
        document.frap.hidden_oid_pp.value="0";
    }

}


function cmdOpenPP(oidSupplier){
    var strvalue  = "search_payment_pp.jsp?supplierId="+oidSupplier+"";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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

<body class="skin-blue">
        <%@ include file = "header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar --> 
            <%@ include file = "menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small><%=textGlobalTitle[SESS_LANGUAGE][0]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][1]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][2]%></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frap" method="post" action="">
              
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="oid_invoice_selected" value="<%=oidInvoiceSelected%>">
                        <input type="hidden" name="oid_payment_selected" value="<%=oidPaymentSelected%>">
                        <input type="hidden" name="oid_payment_detail_selected" value="<%=oidPaymentDetailSelected%>">
                        <input type="hidden" name="oid_currency_type" value="<%=oidCurrency%>">
                        <input type="hidden" name="hidden_receive_id" value="<%=oidReceive%>">
                        <input type="hidden" name="hidden_purchase_order_id" value="<%=oidPurchaseOrder%>">
                        <input type="hidden" name="hidden_oid_pp" value="<%=hidden_oid_pp%>">
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                    <div class="row">
                                            <div class="col-md-12">
                                                
                                                

                                            </div>
                                           
                                    </div>
                                    <div class="row">

                                    </div>
                                    <div class="box-footer">
                                            <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                            <button type="submit" onclick="javascript:cmdAdd()" class="btn btn-primary pull-right" >Add New</button>
                                    </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>
