<%@ page import="com.dimata.pos.form.billing.FrmBillMain,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.pos.entity.search.SrcInvoice,
                 com.dimata.pos.form.search.FrmSrcInvoice,
                 com.dimata.pos.session.billing.SessBilling,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.posbo.entity.masterdata.MemberReg,
                 java.util.*,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.pos.cashier.DefaultSaleModel,
                 com.dimata.pos.session.processdata.SessTransactionData,
                 com.dimata.pos.entity.payment.*,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.pos.entity.billing.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.common.entity.payment.CurrencyType,
				 com.dimata.posbo.session.sales.SessAr"%>
<%@ page language = "java" %>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No. Invoice","Tanggal Invoice","Sales Person","Customer ","Kode Customer ","Lokasi Transaksi","(tidak tercatat)","No.","SKU ","Nama Item","Serial No.","Harga ","Disc","Qty","Total ","Total Trans","Total Disc","Tax","Service","Net Trans","DP","Total Bayar","Kembali","No.","Tipe Pembayaran","Kurs","Jumlah","Tgl Bayar","(tidak tercatat)"},
	{"Invoice No. ","Bill Date","Sales Name","Customer Name","Customer Code","Sale Location","(uscpecified)","No.","Item Code","Item Name","Serial No.","Item Price","Disc","Qty","Total Price","Total Trans","Total Disc","Tax","Service","Net Trans","Last Payment(DP)","Total Payment","Return","No.","Payment Type","Rate","Pay Amount","Pay Date","(unspecified)"}
};
public static final String textListTitle[][] =
{
	{"Pembayaran Piutang ","NOMOR INVOICE","TANGAAL INVOICE","NAMA CUSTOMER","(tidak tercatat)"},
	{"AR Payments","INVOICE NUMBER","INVOICE DATE","CUSTOMER NAME","(unspecified)"}
};

public static final String textListTitleHeader[][] =
{
	{"DETAIL INVOICE ","Invoice ","Tidak ada data transaksi ..","Transaksi Invoice","Semua"},
	{"INVOICE DETAIL","Invoice ","No transaction data available ..","Invoice Transaction ","TYPE","All"}
};

public String drawListCreditPayment(Vector vListPayment) 
{
	String sResult = "";
	if(vListPayment!=null && vListPayment.size()>0)
	{
		// get list currency type
		Vector vCurrType = PstCurrencyType.list(0,0,"","");
		Hashtable hashCurrType = new Hashtable();
		if(vCurrType!=null && vCurrType.size()>0)
		{	
			int iCurrTypeCount = vCurrType.size();
			for(int i=0; i<iCurrTypeCount; i++)
			{
				CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
				hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
			}
		}
		
		
		String sHeader = generateHeader();
		String strContent = "";
		
		double dTotalPayment = 0;
		int iListPaymentCount = vListPayment.size();
		for(int i=0; i<iListPaymentCount; i++)
		{
			Vector vObj = (Vector) vListPayment.get(i);
			if(vObj!=null && vObj.size()>0)
			{
				double dPaymentPerType = 0;
				String strPaymentTypeName = "";
				for(int j=0; j<vObj.size(); j++)
				{
					CashCreditPayments objCashCreditPayments = (CashCreditPayments) vObj.get(j);
					
					String strPaymentType = PstCashPayment.paymentType[objCashCreditPayments.getPaymentType()];
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashCreditPayments.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal((objCashCreditPayments.getAmount()));
					String strRate = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getAmount() * objCashCreditPayments.getRate());
					dPaymentPerType = dPaymentPerType + (objCashCreditPayments.getAmount() * objCashCreditPayments.getRate());
					
					if(j==0)
					{
						strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
						strPaymentTypeName = strPaymentType.toUpperCase();
					}					
					
					strContent = strContent + generateContent(strNumber, strCurrency, strAmount, strRate, strTotal);
				}
				
				strContent = strContent + generateItemFooter("TOTAL "+strPaymentTypeName, FRMHandler.userFormatStringDecimal(dPaymentPerType));
				strContent = strContent + generateBlankSpace();
				dTotalPayment = dTotalPayment + dPaymentPerType;
			}			
		}
		
		String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));		
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
	}
	
	return sResult;
}

public String generateHeader()
{
	return "<tr>" + 
			  "<td width=\"4%\" class=\"listgentitle\">NO</td>" + 
			  "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
			  "<td width=\"26%\" class=\"listgentitle\">TOTAL</td>" +
			"</tr>";
}

public String generateItemHeader(String strItemHeader)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"5\"><b>"+strItemHeader+"</b></td>" +
			"</tr>";
}

public String generateContent(String strNum, String strCurrency, String strAmount, String strRate, String strTotal)
{
	return "<tr valign=\"top\">" + 
			  "<td width=\"4%\" class=\"listgensell\" align=\"center\">"+strNum+"</td>" + 
			  "<td width=\"24%\" class=\"listgensell\">"+strCurrency+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strAmount+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strRate+"</td>" +
			  "<td width=\"26%\" class=\"listgensell\" align=\"right\">"+strTotal+"</td>" +
			"</tr>";			
}

public String generateItemFooter(String strItemFooter, String strValue)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\"><b>"+strItemFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateFooter(String strFooter, String strValue)

{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\" ><b>"+strFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateBlankSpace()
{
	return "<tr valign=\"top\">"  +
			  "<td class=\"listgensell\" colspan=\"5\">&nbsp;</td>" +
			"</tr>";
}
%>

<%
int iErrCode = FRMMessage.ERR_NONE;
int iCommand = FRMQueryString.requestCommand(request);
long oidGroup = FRMQueryString.requestLong(request, "hidden_group_id");
int start = FRMQueryString.requestInt(request, "start");
int indeks = FRMQueryString.requestInt(request, "indeks");
int recordToGet = FRMQueryString.requestInt(request, "recordToGet");
int prev_start = FRMQueryString.requestInt(request, "prev_start");

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
BillMain billMain = new BillMain();
ContactList contactList = new ContactList();
CreditPaymentMain creditPaymentMain = new CreditPaymentMain();

double totTrans = 0;
double totBayar =0;
double totTerbayar = 0;
//adding tax & service
//by mirahu 27032012
double tax = 0;
double service = 0;
double bruto = 0;


if(iCommand==Command.EDIT || iCommand==Command.ASK || iCommand==Command.DELETE){
   try
   {
        creditPaymentMain = PstCreditPaymentMain.fetchExc(oidGroup);
		if(creditPaymentMain.getBillMainId()>0)
		{
            billMain = PstBillMain.fetchExc(creditPaymentMain.getBillMainId());
            contactList = PstContactList.fetchExc(billMain.getCustomerId());
            //totTrans = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
            totTrans = SessAr.getTotalPriceNetto(billMain.getOID());
	    totTerbayar = PstCashCreditPayment.getTotalCreditPaymentNotInPayNow(creditPaymentMain.getBillMainId(),oidGroup);
            totBayar = PstCashCreditPayment.getTotalCreditPayment(creditPaymentMain.getOID());
            tax = billMain.getTaxValue();
            service = billMain.getServiceValue();
            bruto = totTrans + tax + service;
		}
    }
	catch(Exception e)
	{
        e.printStackTrace();
    }
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdBack()
{
	document.frminvoice.command.value="<%=Command.BACK%>";
	document.frminvoice.action="reportar_list_payment.jsp";
	document.frminvoice.submit();
	//history.back();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.material.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
            &nbsp;Laporan Penjualan > Rekap Piutang<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frminvoice" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="recordToGet" value="<%=recordToGet%>">
              <input type="hidden" name="indeks" value="<%=indeks%>">
              <input type="hidden" name="hidden_group_id" value="<%=oidGroup%>">
              <input type="hidden" name="prev_start" value="<%=prev_start%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="top"> 
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td width="53%"> 
                          <table width="60%" border="0" cellspacing="1" class="listgen">
                            <tr> 
                              <td colspan="6" align="left" valign="top" class="listgentitle">DETAIL 
                                PELUNASAN PIUTANG</td>
                            </tr>
                            <tr> 
                              <td width="24%" align="left" valign="top" class="listgensell"><b><font color="#000000">Nomor 
                                Pembayaran </font></b></td>
                              <td width="3%" align="center" valign="top" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000"><%=creditPaymentMain.getInvoiceNumber()%></font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Netto 
                                Transaksi</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(totTrans)%></font></td>
                            </tr>
                            <!-- adding tax, service, bruto -->
                            <tr> 
                              <td width="24%" align="left" valign="top" class="listgensell"><b><font color="#000000">Tanggal 
                                Transaksi</font></b></td>
                              <td width="3%" align="center" valign="top" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000"><%=Formater.formatDate(creditPaymentMain.getBillDate(),"dd/MM/yyyy")%></font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Tax</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(tax)%></font></td>
                            </tr>
                            <tr> 
                              <td width="24%" align="left" valign="top" class="listgensell"><b><font color="#000000">Nama 
                                Member</font></b></td>
                              <td width="3%" align="center" valign="top" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000"><%=contactList.getPersonName()%></font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Service</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(service)%></font></td>
                            </tr>
                             <tr> 
                              <td width="24%" align="left" valign="top" class="listgensell"><b><font color="#000000">Invoice 
                                Referensi</font></b></td>
                              <td width="3%" align="center" valign="top" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000"><%=billMain.getInvoiceNumber()%></font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Bruto Transaksi</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(bruto)%></font></td>
                            </tr>
                            <!-- end of adding tax,service,bruto -->
                            <tr> 
                              <td align="left" valign="top" width="24%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000">&nbsp;</font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Sudah 
                                Dibayar</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(totTerbayar)%></font></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" width="24%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000">&nbsp;</font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Pembayaran</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <td align="right" valign="top" width="17%" class="listgensell"><b><font color="#000000"><%=FRMHandler.userFormatStringDecimal(totBayar)%></font></b></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" width="24%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">&nbsp;</font></b></td>
                              <td align="left" valign="top" width="33%" class="listgensell"><font color="#000000">&nbsp;</font></td>
                              <td align="left" valign="top" width="20%" class="listgensell"><b><font color="#000000">Saldo 
                                Akhir</font></b></td>
                              <td align="center" valign="top" width="3%" class="listgensell"><b><font color="#000000">:</font></b></td>
                              <!--td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%//=FRMHandler.userFormatStringDecimal(totTrans - (totTerbayar + totBayar))%></font></td>-->
                              <td align="right" valign="top" width="17%" class="listgensell"><font color="#000000"><%=FRMHandler.userFormatStringDecimal(bruto - (totTerbayar + totBayar))%></font></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
				
				<%
				if(creditPaymentMain.getOID()!=0)
				{
					PstCreditPaymentMain objPstCreditPaymentMain = new PstCreditPaymentMain();
					Vector vResult = objPstCreditPaymentMain.getListCreditPayment(oidGroup);
					String strTemp = drawListCreditPayment(vResult);	
				%>
				<tr align="left" valign="top"> 
                  <td height="22" valign="middle">&nbsp;</td>
                </tr>															
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle"><b>Detail Pembayaran</b></td>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle"><%=strTemp%></td>
                </tr>							
				<%
				}
				%>
				
                <tr align="left" valign="top"> 
                  <td height="18" valign="top"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
					ctrLine.setTableWidth("100%");
					ctrLine.setSaveImageAlt("");
					ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
					ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

					String scomDel = "javascript:cmdAsk('"+oidGroup+"')";
					String sconDelCom = "javascript:cmdConfirmDelete('"+oidGroup+"')";
					String scancel = "javascript:cmdEdit('"+oidGroup+"')";

					ctrLine.setCommandStyle("command");
					ctrLine.setColCommStyle("command");

					// set command caption
					ctrLine.setAddCaption("");
					ctrLine.setSaveCaption("");
					ctrLine.setDeleteCaption("");
					ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
					if(billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_DELETED||billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
						ctrLine.setDeleteCaption("");
					}else{
						ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
					}

					ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_DELETE,true));
					ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

					if (privDelete){
						ctrLine.setConfirmDelCommand(sconDelCom);
						if(billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_DELETED||billMain.getTransactionStatus()==PstBillMain.TRANS_STATUS_OPEN){
							ctrLine.setDeleteCommand("");
						}else{
							ctrLine.setDeleteCommand(scomDel);
						}
						ctrLine.setEditCommand(scancel);
					}else{
						ctrLine.setConfirmDelCaption("");
						ctrLine.setDeleteCaption("");
						ctrLine.setEditCaption("");
					}

					ctrLine.setDeleteCaption("");
					if(privAdd == false  && privUpdate == false){
						ctrLine.setSaveCaption("");
					}
					ctrLine.setDeleteCaption("");
					if (privAdd == false){
						ctrLine.setAddCaption("");
					}
					%>
                    <%= ctrLine.drawImage(iCommand, iErrCode, "")%>
                    
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
    <td colspan="2" height="20"> 
        <!-- #BeginEditable "footer" -->
         <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
        <!-- #EndEditable -->
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
