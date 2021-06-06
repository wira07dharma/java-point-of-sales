<%-- 
    Document   : invoice_edit_open_bill
    Created on : Aug 1, 2014, 10:21:34 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment1"%>
<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@page import="com.dimata.pos.entity.payment.CashPayments1"%>
<%@ page import="com.dimata.pos.entity.billing.Billdetail,
                com.dimata.pos.entity.billing.BillDetailCode,
                com.dimata.pos.entity.billing.PstBillMain,
                com.dimata.pos.entity.billing.BillMain,
                com.dimata.pos.entity.payment.CashPayments,
                com.dimata.pos.entity.payment.CashReturn,
                com.dimata.pos.entity.payment.PstCashPayment,
                com.dimata.pos.form.billing.FrmBillMain,
                com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.pos.entity.search.SrcInvoice,
                com.dimata.pos.form.search.FrmSrcInvoice,
                com.dimata.pos.session.billing.SessBilling,
                com.dimata.posbo.report.sale.SaleReportDocument,
                com.dimata.posbo.entity.masterdata.MemberReg,
                com.dimata.common.entity.payment.CurrencyType,
                com.dimata.common.entity.payment.PstCurrencyType,
                java.util.*,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
				 com.dimata.common.entity.contact.*,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.pos.cashier.DefaultSaleModel,
                 com.dimata.pos.session.processdata.SessTransactionData,
				 com.dimata.pos.entity.billing.*"%>
<%@ page language = "java" %>
<!-- package java -->   

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No. Invoice","Tanggal Invoice","Sales Person","Customer ","Kode Customer ","Lokasi Transaksi","(tidak tercatat)",//0-6
	 "No.","SKU ","Nama Item","Serial No.","Harga ","Disc","Qty","Total ","Total Trans","Total Disc","Tax","Service",//7-18
	 "Net Trans","DP","Total Bayar","Kembali","No.","Tipe Pembayaran","Kurs","Jumlah","Tgl Bayar","(tidak tercatat)","Catatan",//19-29
	 "Mata Uang"},//30
	{"Invoice No. ","Bill Date","Sales Name","Customer Name","Customer Code","Sale Location","(uscpecified)",
	 "No.","Item Code","Item Name","Serial No.","Item Price","Disc","Qty","Total Price","Total Trans","Total Disc","Tax","Service",
	 "Net Trans","Last Payment(DP)","Total Payment","Return","No.","Payment Type","Rate","Pay Amount","Pay Date","(unspecified)","Remark",
	 "Currency"}
};
public static final String textListTitle[][] = 
{
	{"Invoice ","NOMOR INVOICE","TANGAAL INVOICE","NAMA CUSTOMER","(tidak tercatat)"},
	{"Invoice ","INVOICE NUMBER","INVOICE DATE","CUSTOMER NAME","(unspecified)"}
};

public static final String textListTitleHeader[][] = 
{
	{"DETAIL INVOICE ","Invoice ","Tidak ada data transaksi ..","Transaksi Invoice","Semua"},
	{"INVOICE DETAIL","Invoice ","No transaction data available ..","Invoice Transaction ","TYPE","All"}
};

public String drawListPayment(Vector vListPayment) 
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
					CashPayments objCashPayment = (CashPayments) vObj.get(j);
					
					String strPaymentType = PstCashPayment.paymentType[objCashPayment.getPaymentType()];
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()));
					String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount() * objCashPayment.getRate());					
					dPaymentPerType = dPaymentPerType + (objCashPayment.getAmount() * objCashPayment.getRate());
					
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
		
		CurrencyType defaultCurrency = PstCurrencyType.getDefaultCurrencyType();
		String sHeader = generateHeader(defaultCurrency.getCode());
		String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));		
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
	}
	
	return sResult;
}

public String drawListPaymentDynamic(Vector vListPayment) {
	String sResult = "";
	if(vListPayment!=null && vListPayment.size()>0){
		// get list currency type
		Vector vCurrType = PstCurrencyType.list(0,0,"","");
		Hashtable hashCurrType = new Hashtable();
		if(vCurrType!=null && vCurrType.size()>0){
			int iCurrTypeCount = vCurrType.size();
			for(int i=0; i<iCurrTypeCount; i++){
				CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
				hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
			}
		}

		String strContent = "";

		double dTotalPayment = 0;
		int iListPaymentCount = vListPayment.size();
		for(int i=0; i<iListPaymentCount; i++){
			Vector vObj = (Vector) vListPayment.get(i);
			if(vObj!=null && vObj.size()>0){
				double dPaymentPerType = 0;
				String strPaymentTypeName = "";
				for(int j=0; j<vObj.size(); j++){
					CashPayments1 objCashPayment = (CashPayments1) vObj.get(j);

					String strPaymentType = PstPaymentSystem.getTypePayment(objCashPayment.getPaymentType());
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()));
					String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount() * objCashPayment.getRate());
					dPaymentPerType = dPaymentPerType + (objCashPayment.getAmount() * objCashPayment.getRate());

					/*if(j==0){
						strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
						strPaymentTypeName = strPaymentType.toUpperCase();
					}*/
                                        strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
					strContent = strContent + generateContent(strNumber, strCurrency, strAmount, strRate, strTotal);
				}

				strContent = strContent + generateItemFooter("TOTAL "+"", FRMHandler.userFormatStringDecimal(dPaymentPerType));
				strContent = strContent + generateBlankSpace();
				dTotalPayment = dTotalPayment + dPaymentPerType;
			}
		}

		CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
		String defaultCurrency = currencyType.getCode();
		String sHeader = generateHeader(defaultCurrency);
		String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
	}

	return sResult;
}

public String generateHeader(String defaultCurrency)
{
	return "<tr>" + 
			  "<td width=\"4%\" class=\"listgentitle\">NO</td>" + 
			  "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
			  "<td width=\"26%\" class=\"listgentitle\">TOTAL ("+defaultCurrency+")</td>" +
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
int iCommand = FRMQueryString.requestCommand(request);
long oidBillMainOid = FRMQueryString.requestLong(request, "hidden_billmain_id");
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = FRMQueryString.requestInt(request, "recordToGet");

ControlLine ctrLine = new ControlLine();
int iErrCode = FRMMessage.ERR_NONE;
String msgString = "";

BillMain billMain = new BillMain();
AppUser ap = new AppUser();
try {
	billMain = PstBillMain.fetchExc(oidBillMainOid);
   ap = PstAppUser.fetch(billMain.getAppUserId());
}
catch(Exception e) {
	System.out.println("Exc when get bill detail : " + e.toString());
}

String sWhereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oidBillMainOid;
Vector records = PstBillDetail.list(start, recordToGet, sWhereClause, "");

String strCurrencyType = "";
if(billMain.getCurrencyId() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+billMain.getCurrencyId();
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

function cmdBack()
{
	document.frminvoice.command.value="<%=Command.BACK%>";
	document.frminvoice.action="reportlistsale.jsp";
	document.frminvoice.submit();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
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

<%@include file="../../../styletemplate/template_header_empty.jsp" %>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;Laporan Pending Order > Pending Order Detail<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frminvoice" method="post" action=""> 
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="recordToGet" value="<%=recordToGet%>">              
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="<%=oidBillMainOid%>">
              <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">			  			  
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="127" valign="top"> 
                    <table width="100%" border="0" cellspacing="1">
                      <tr> 
                        <td align="left" valign="top">
                           <table border="0" cellspacing="1">
                            <tr>
                           <td>
                            <table width="100%" border='0'>
                                  <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][0]%></td>
                              <td align="left" valign="top" > : </td>
                                <td align="left" valign="top" > 
                                <div align="left"><%=billMain.getInvoiceNumber()%></div></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                              <td align="left" valign="top" > : </td>
                                <td align="left" valign="top" ><%=Formater.formatDate(billMain.getBillDate(),"dd-MMM-yyyy : HH-mm-SS")%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][30]%></td>
                              <td align="left" valign="top" > : </td>
                              <td align="left" valign="top" ><%=strCurrencyType%></td>
                            </tr>
                            </table>
                           </td>
                           <td width='10%'>&nbsp;</td>
                           <td>
                            <table width="100%" border='0'>
                                  <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][3]%></td>
                              <td align="left" valign="top" > : </td>
							  <%
								String custName = "";
								String custCode = "";
								ContactList contactlist = new ContactList();
								try
								{
									contactlist = PstContactList.fetchExc(billMain.getCustomerId());
									custName = contactlist.getPersonName();
									custCode = contactlist.getContactCode();
								}
								catch(Exception e)
								{
									System.out.println("Contact not found ...");
								}							  							  
							  %>
                              <td align="left" valign="top" ><%=custName%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][4]%></td>
                              <td align="left" valign="top" > : </td>
                              <td align="left" valign="top" ><%=custCode%></td>
                            </tr>
                            <tr> 
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][5]%></td>
                              <td align="left" valign="top" > : </td>
                              <%
                                String locationname = "";
                                Location location = new Location();
                                try
								{
                                   location = PstLocation.fetchExc(billMain.getLocationId());
                                   locationname = location.getName(); 
                                }
								catch(Exception e)
								{
                                   locationname = textListMaterialHeader[SESS_LANGUAGE][6];
                                }
                              %>
                              <td align="left" valign="top" ><%=locationname %></td>
                            </tr>
                            </table>
                           </td>
                           <td width='10%'>&nbsp;</td>
						   <td valign="top">
						   	<table width="100%" border="0">
                              <tr> 
                                <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][2]%></td>
                                <td align="left" valign="top" > : </td>
                                <td align="left" valign="top" ><%=ap.getFullName() %></td>
                              </tr>
							  <tr> 
                                <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][29]%></td>
                                <td align="left" valign="top" > : </td>
                                <td align="left" valign="top" ><%=billMain.getNotes()%></td>
                              </tr>
							</table>
						   </td>
                           </tr>
                          </table></td>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td colspan="2" align="left" valign="top">
                            
                          <table border="0" cellspacing="1"  width='100%'>
                            <tr class="listgentitle"> 
                              <td width="2%"><b><%=textListMaterialHeader[SESS_LANGUAGE][7]%></b></td>
                              <td width="16%"><b><%=textListMaterialHeader[SESS_LANGUAGE][8]%></b></td>
                              <td width="27%"><b><%=textListMaterialHeader[SESS_LANGUAGE][9]%></b></td>
                              <td width="9%"><b><%=textListMaterialHeader[SESS_LANGUAGE][10]%></b></td>
                              <td width="8%"><b><%=textListMaterialHeader[SESS_LANGUAGE][11]%></b></td>
                              <td width="7%"><b><%=textListMaterialHeader[SESS_LANGUAGE][12]%></b></td>
                              <td width="10%"><b><%=textListMaterialHeader[SESS_LANGUAGE][13]%></b></td>
                              <td width="12%"><b><%=textListMaterialHeader[SESS_LANGUAGE][14]%></b></td>
                            </tr>
                            <%
                            double returBruto = 0;
                            if(records!=null && records.size()>0)
                            {
                                    int maxDetail = records.size();								
                                    for(int i=0; i<maxDetail; i++)
                                    {
                                            Billdetail objBillDetail = new Billdetail();
                                            try{
                                                    objBillDetail = (Billdetail) records.get(i);
                                            }catch(Exception ex){
                                                System.out.print(ex);
                                                objBillDetail = new Billdetail();
                                            }
                                            //Billdetail objBillDetail = (Billdetail) records.get(i);
                                            BillDetailCode objBillDetailCode = PstBillDetailCode.getBillDetailCode(objBillDetail.getOID());
                                            returBruto = returBruto + objBillDetail.getTotalPrice();
                            %>
                            <tr class="listgensell"> 
                              <td>
                                <div align="center"><%=i+1%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetail.getSku()%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetail.getItemName()%></div>
                              </td>
                              <td>
                                <div align="left"><%=objBillDetailCode.getStockCode()%></div>
                              </td>
                              <td> 
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getItemPrice())%></div>
                              </td>
                              <td> 
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getDisc())%></div>
                              </td>
                              <td>
                                <div align="right"><%=objBillDetail.getQty()%></div>
                              </td>
                              <td>
                                <div align="right"><%=FRMHandler.userFormatStringDecimal(objBillDetail.getTotalPrice())%></div>
                              </td>
                            </tr>
                            <%
                                }
							}
                            %>
                            <tr > 
                              <td colspan="6" rowspan="5">&nbsp;</td>
                              <td align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][15]%></b></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(returBruto)%></td>
                            </tr>
                            <tr > 
                              <td align="right"class="listgensell" ><b><%=textListMaterialHeader[SESS_LANGUAGE][16]%></b></td>
                              <td align="right"class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getDiscount())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][17]%></b></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getTaxValue())%></td>
                            </tr>
                            <tr > 
                              <td height="21" align="right" class="listgensell"><b><%=textListMaterialHeader[SESS_LANGUAGE][18]%></b></td>
                              <td align="right" class="listgensell"><%=FRMHandler.userFormatStringDecimal(billMain.getServiceValue())%></td>
                            </tr>
                            <tr > 
                              <td align="right" class="listgensell" ><b><%=textListMaterialHeader[SESS_LANGUAGE][19]%></b></td>
                              <%
							  double netTrans = returBruto - billMain.getDiscount() + billMain.getTaxValue() + billMain.getServiceValue(); 
							  %>
                              <td align="right" class="listgensell"><b><%=FRMHandler.userFormatStringDecimal(netTrans)%></b></td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <%
                int paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
                if(oidBillMainOid!=0 && iSaleReportType == PstCashPayment.CASH)
                {
                        String strTemp = "";
                        PstCashPayment objPstCashPayment = new PstCashPayment();
                        PstCashPayment1 objPstCashPaymentDinamis = new PstCashPayment1();
                        Vector vResult = new Vector();
                        if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                              vResult = objPstCashPaymentDinamis.getListPaymentDinamis(oidBillMainOid);
                             strTemp = drawListPaymentDynamic(vResult); 
                        }else{
                            vResult = objPstCashPayment.getListPayment(oidBillMainOid);
                            strTemp = drawListPayment(vResult);
                        }
                %>				
		<tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>															
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b>Daftar Pembayaran</b></td>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=strTemp%></td>
                </tr>							
				<%
				}
				%>
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
