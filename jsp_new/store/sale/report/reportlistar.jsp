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
                 com.dimata.posbo.entity.arap.PstArApItem"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Laporan Penjualan","Rekap Piutang","Lokasi","Tanggal","s/d","Status","Mata Uang","Laporan", // 0-7
	 "Cetak Laporan Rekap Piutang","Cetak Laporan Rekap Piutang Detail","Tidak ada data transaksi ..","Sub Total","Grand Total","Semua Lokasi"}, // 8-12
	{"Sale Report","AR Summary","Location","Date","to","Status","Currency","Report", // 0-7
	 "Print Report AR","Print Report AR Detail","No transaction data available ..","Sub Total","Grand Total","All Location"} // 8-12
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No.","Nama Konsumen","No. Invoice","Tanggal","Total Kredit","Telah Dibayar","Saldo Piutang",
	 "Keterangan","Rincian Item","Diskon","Pajak","Retur"},
	{"No.","Customer Name","Invoice Number","Date","Credit Total","Payment","AR Balance",
	 "Remark","Detail Item","Discount","Tax","Return"}
};

public static final String textListMaterialDetailHeader[][] = {
	{"Kode","Harga","Unit","Qty","Diskon","Total"},
	{"Code","Price","Unit","Qty","Disc.","Total"}
};

public Vector drawList(int language,Vector objectClass,SrcSaleReport srcSaleReport, int start, int recordToGet) {
    
    String result = "";
    Vector listAll = new Vector(1,1);
    Vector listTableHeaderPdf = new Vector(1,1);
    Vector listTableFooterPdf = new Vector(1,1);
    Vector listTableFooterDetailPdf = new Vector(1,1);
    Vector listContentPdf = new Vector(1,1);
    Vector listBodyPdf = new Vector(1,1);
      String frmCurrency = "#,###";
        String frmCurrencyUsd = "#,###.##";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("1");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][1],"20%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][2],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][7],"13%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][3],"8%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][4],"12%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][11],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][5],"12%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][6],"12%","0","0","center","bottom");
        
        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector();
        
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        
        double amount = 0;
        double diskon = 0;
        double pajak = 0;
        double retur = 0;
        double pembayaran = 0;
        double saldo = 0;

        //adding service value
        //by mirahu
        //20111231
        double service = 0;
        
        double subTotalAmount = 0;
        double subTotalRetur = 0;
        double subTotalPembayaran = 0;
        double subTotalSaldo = 0;
        
        double grandTotalAmount = 0;
        double grandTotalRetur = 0;
        double grandTotalPembayaran = 0;
        double grandTotalSaldo = 0;
        
        for(int i=0; i<objectClass.size(); i++)	{
            RptArInvoice arInv = (RptArInvoice) objectClass.get(i);
            if(srcSaleReport.getCurrencyOid() == 0) {
                amount = arInv.getSaleNetto() * arInv.getRate();
                diskon = arInv.getDiscount() * arInv.getRate();
                pajak = arInv.getTax() * arInv.getRate();
                retur = arInv.getTotalReturn() * arInv.getRate();
                pembayaran = arInv.getTotalPay() * arInv.getRate();
                service = arInv.getService() * arInv.getRate();

            } else {
                amount = arInv.getSaleNetto();
                diskon = arInv.getDiscount();
                pajak = arInv.getTax();
                retur = arInv.getTotalReturn();
                pembayaran = arInv.getTotalPay();
                //adding service
                service = arInv.getService();
            }
            
            //amount = (amount - diskon) + pajak;
            amount = (amount - diskon) + pajak + service;
            saldo = amount - pembayaran - retur;
            
            grandTotalAmount += amount;
            grandTotalRetur += retur;
            grandTotalPembayaran += pembayaran;
            grandTotalSaldo += saldo;
            
            if(i>=start && i<start+recordToGet){
                rowx = new Vector();
                rowx.add("<div align=\"center\">"+(i+1)+"</div>");
                rowx.add("<div align=\"left\">"+arInv.getCustName()+"</div>");
                rowx.add("<div align=\"center\">"+arInv.getInvoiceNo()+"</div>");
                rowx.add("<div align=\"left\">"+arInv.getNotes()+"</div>");
                rowx.add("<div align=\"left\">"+Formater.formatDate(arInv.getBillDate(),"dd MMM yyyy")+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(amount)+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retur)+"</div>");
                if(arInv.getTotalPay()>0){
                    rowx.add("<a href=\"javascript:cmdEditPay('"+i+"')\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(pembayaran)+"</div></a>");
                }else{
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pembayaran)+"</div>");
                }
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saldo)+"</div>");
                
                subTotalAmount += amount;
                subTotalRetur += retur;
                subTotalPembayaran += pembayaran;
                subTotalSaldo += saldo;
                
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(arInv.getBillMainId()));
            }
        }
        
        if(subTotalSaldo==grandTotalSaldo){
            rowx = new Vector();
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+textListGlobal[language][12]+" : </b></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(subTotalAmount, frmCurrency)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(subTotalRetur, frmCurrency)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(subTotalPembayaran, frmCurrency)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(subTotalSaldo, frmCurrency)+"</b></div>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(0));
        } else{
            rowx = new Vector();
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+textListGlobal[language][11]+" : </b></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subTotalAmount)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subTotalRetur)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subTotalPembayaran)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(subTotalSaldo)+"</b></div>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(0));
            
            rowx = new Vector();
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+textListGlobal[language][12]+" : </b></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalAmount)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalRetur)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalPembayaran)+"</b></div>");
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalSaldo)+"</b></div>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(0));
        }
        
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][10]+"</div>";
    }
    listAll.add(result);
    return listAll;
}
%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);
int vectSize = 0;

/**
* handle current search data session
*/
try {
	if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK || iCommand==Command.SAVE){
		srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.AR_REPORT_DOC);
		if(iCommand==Command.BACK) {
			iCommand=Command.LIST;
		}
	}
}
catch(Exception e) {
}

vectSize = SessAr.getArInvoiceCount(srcSaleReport);
if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST ) {
	try {
		start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
		if (srcSaleReport== null) {
			srcSaleReport= new SrcSaleReport();
		}
	}
	catch(Exception e) {
		srcSaleReport = new SrcSaleReport();
	}
}
else {
	session.putValue(SaleReportDocument.AR_REPORT_DOC, srcSaleReport);
}

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e) {
	location.setName(textListGlobal[SESS_LANGUAGE][13]);
}

Vector records = SessAr.getArInvoice(0, 0, srcSaleReport);  
if(vectSize>0) {
	session.putValue(SaleReportDocument.AR_PAYMENT_DOC, records);
}

//Vector listHeaderPdf = new Vector(1,1);
//Vector listBodyPdf = new Vector(1,1);
//Vector listPdf = new Vector(1,1);
Vector list = drawList(SESS_LANGUAGE,records,srcSaleReport, start, recordToGet);
String str = "";

    // ini proses pembuatan ar ke aiso
    if(iCommand==Command.SAVE){
        for(int i=0; i<records.size(); i++)        {
            RptArInvoice arInv = (RptArInvoice) records.get(i);
            ArApMain arap = new ArApMain();

            //arap.setVoucherNo(arInv.getInvoiceNo());
            arap.setVoucherDate(arInv.getBillDate());
            arap = PstArApMain.createOrderNomor(arap);
            arap.setNumberOfPayment(1);
            arap.setContactId(arInv.getMemberId());
            arap.setIdPerkiraanLawan(504404308087025317L);
            arap.setIdCurrency(504404261456952182L);
            arap.setIdPerkiraan(504404266605909977L);
            arap.setRate(1.0);
            arap.setAmount(arInv.getBalance());
            arap.setNotaNo(arInv.getInvoiceNo());
            arap.setNotaDate(arInv.getBillDate());
            arap.setDescription("Piutang generate dari BO : "+arInv.getInvoiceNo());
            arap.setArApMainStatus(0);
            arap.setArApType(PstArApMain.TYPE_AR);
            arap.setArApDocStatus(PstArApMain.STATUS_OPEN);
            try{
                long oid = PstArApMain.insertExc(arap);
                PstArApMain.updateSynchOID(oid,arInv.getBillMainId());

                ArApItem arApItem = new ArApItem();
                arApItem.setAngsuran(arap.getAmount());
                arApItem.setArApMainId(arInv.getBillMainId());
                arApItem.setDueDate(arInv.getBillDate());
                arApItem.setDescription("Piutang generate dari BO : "+arInv.getInvoiceNo());
                arApItem.setArApItemStatus(0);
                arApItem.setLeftToPay(arap.getAmount());
                arApItem.setCurrencyId(arap.getIdCurrency());
                arApItem.setRate(arap.getRate());
                arApItem.setSellingAktivaId(0);
                arApItem.setReceiveAktivaId(0);

                PstArApItem.insertExc(arApItem);
            }catch(Exception e){}
        }
    } // end proses pembuatan ar ke aiso
	
//Vector compTelpFax = (Vector)companyAddress.get(2);
//listHeaderPdf.add(0,(String) companyAddress.get(0));
//listHeaderPdf.add(1,(String) companyAddress.get(1));
//listHeaderPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));

try{
	str = (String)list.get(0);
	//listBodyPdf= (Vector)list.get(1);
} catch(Exception e) {
	System.out.println(e.toString());
}

String strCurrencyType = "All";
if(srcSaleReport.getCurrencyOid() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdEdit(oid) {
    document.frm_reportsale.hidden_billmain_id.value=oid;	
    document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="invoicear_edit.jsp";
	document.frm_reportsale.submit();
}

function cmdAr() {
    //document.frm_reportsale.hidden_billmain_id.value=oid;
    document.frm_reportsale.command.value="<%=Command.SAVE%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
}

function cmdEditPay(idx) {
	document.frm_reportsale.indeks.value=idx;
    document.frm_reportsale.command.value="<%=Command.LIST%>";
	document.frm_reportsale.action="reportar_list_payment.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportar.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("buffer_ar_pdf.jsp?sale_type="+<%=iSaleReportType%>+"","form_rpt_ar");
	//window.open("<%=approot%>/servlet/com.dimata.posbo.report.sale.ArInvoiceReportPdf");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printDetailForm(){
    window.open("buffer_ar_detail_pdf.jsp?sale_type="+<%=iSaleReportType%>+"","form_rpt_ar_detail");
	//window.open("<%=approot%>/servlet/com.dimata.posbo.report.sale.ArInvoiceDetailReportPdf");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
	var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
	var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
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

</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
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
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>

  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
		    &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_billmain_id" value="">
              <input type="hidden" name="indeks" value="">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=textListGlobal[SESS_LANGUAGE][2]%> : <%=location.getName()%> </b> </td>
					<!--%
					listHeaderPdf.add(3,"Laporan Rekap Piutang");
					listHeaderPdf.add(4,textListGlobal[SESS_LANGUAGE][2]+ " : " + location.getName());
					%-->
                </tr>
				<%if(srcSaleReport.getUseDate()==1) {
				%>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=textListGlobal[SESS_LANGUAGE][3]%> : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%>&nbsp;<%=textListGlobal[SESS_LANGUAGE][4]%>&nbsp;<%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
					<!--%
					listHeaderPdf.add(5,textListGlobal[SESS_LANGUAGE][3]+ " : "+Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy") + " " + textListGlobal[SESS_LANGUAGE][4] + " " +  Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
					%-->
                </tr>
				<%
				}
				/*else{
					listHeaderPdf.add(5,"");
				}*/
				%>				
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=textListGlobal[SESS_LANGUAGE][5]%> : <%=SrcSaleReport.stTransStatus[SESS_LANGUAGE][srcSaleReport.getTransStatus()]%></b></td>
				  <!--%
					listHeaderPdf.add(6,"Status : " + SrcSaleReport.stTransStatus[SESS_LANGUAGE][srcSaleReport.getTransStatus()]);
				  %-->
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListGlobal[SESS_LANGUAGE][6]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=str%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>															
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				    %>
                  </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="55%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="95%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][7],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="45%"><table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <% if(vectSize > 0) { %>
							<!--%
							try {
								listPdf.add(listHeaderPdf);
								listPdf.add(listBodyPdf);
								session.putValue(SaleReportDocument.AR_INVOICE_PDF,listPdf);
							} catch(Exception e){
								e.printStackTrace(); 
							}
							%-->
                            <tr> 
                              <td width="50%" nowrap>&nbsp; 
                                <a class="btn-primary btn-lg" href="javascript:printForm()" class="command" ><i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][8]%></i></a>	
                              </td>
                              <td width="40%" nowrap>&nbsp; 
				<a class="btn-primary btn-lg" href="javascript:printDetailForm()" class="command" ><i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][9]%></i></a>
                              </td>
							</tr>
							<% } %>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
       <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
