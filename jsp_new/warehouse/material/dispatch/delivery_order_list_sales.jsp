<%-- 
    Document   : delivery_order_list_sales
    Created on : Dec 9, 2013, 10:34:00 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
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
                 com.dimata.pos.entity.balance.PstCashCashier,
                 com.dimata.pos.entity.masterCashier.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","TGL","NOMOR","KONSUMEN","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","HPP","CATATAN"},
	{"NO","DATE","NUMBER","CUSTOMER","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","COGS","REMARK"}
};

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang","Kasir"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type","Cashier"}
};

public String drawList(JspWriter outObj, int language,Vector objectClass,SrcSaleReport srcSaleReport, int start, Vector vectPay, long currencyId, int typePayment) {

    //String result = "";
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
                ctrlist.dataFormat(textListMaterialHeader[language][2],"20%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][1],"20%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][3],"20%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][11],"20%","0","0","center","bottom");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++)	{
			BillMain billMain = (BillMain)objectClass.get(i);

                        ContactList contactlist = new ContactList();
                        try
                        {
                            contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                        }
                        catch(Exception e) {
                                //System.out.println("Contact not found ...");
                        }

                        rowx = new Vector();
                        rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
                        rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
                        rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")+"</div>");
                        rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
                        rowx.add("<div align=\"left\">"+billMain.getNotes()+"</div>");
                        lstData.add(rowx);
                        lstLinkData.add(String.valueOf(billMain.getOID()));
                        
        }

        rowx = new Vector();
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));

       ctrlist.draw(outObj);
    }
    return"";
}


public String getTotalPayment(Vector vListPayment) {
    String sResult = "";
    if(vListPayment!=null && vListPayment.size()>0){
        Vector vCurrType = PstCurrencyType.list(0,0,"","");
        Hashtable hashCurrType = new Hashtable();
		if(vCurrType!=null && vCurrType.size()>0) {
			int iCurrTypeCount = vCurrType.size();
			for(int i=0; i<iCurrTypeCount; i++)	{
				CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
				hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
			}
		}

		String sHeader = generateHeader("");
        String strContent = "";

        double dTotalPayment = 0;
        int iListPaymentCount = vListPayment.size();
        for(int i=0; i<iListPaymentCount; i++){
            Vector vObj = (Vector) vListPayment.get(i);
            if(vObj!=null && vObj.size()>0){
                double dPaymentPerType = 0;
                String strPaymentTypeName = "";
                for(int j=0; j<vObj.size(); j++){
                    CashPayments objCashPayment = (CashPayments) vObj.get(j);

                    String strPaymentType = PstCashPayment.paymentType[objCashPayment.getPaymentType()];
                    String strNumber = String.valueOf(j+1);
                    String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
                    String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()));
                    String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
                    String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount() * objCashPayment.getRate());
                    dPaymentPerType = dPaymentPerType + (objCashPayment.getAmount() * objCashPayment.getRate());

                    if(j==0) {
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


public String drawListPayment(Vector vListPayment) {
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
					CashPayments objCashPayment = (CashPayments) vObj.get(j);

					String strPaymentType = PstCashPayment.paymentType[objCashPayment.getPaymentType()];
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()));
					String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount() * objCashPayment.getRate());
					dPaymentPerType = dPaymentPerType + (objCashPayment.getAmount() * objCashPayment.getRate());

					if(j==0){
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

		CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
		String defaultCurrency = currencyType.getCode();
		String sHeader = generateHeader(defaultCurrency);
		String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
	}

	return sResult;
}


//adding payment Dinamis
//by mirahu 20120416
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
				//String strPaymentTypeName = "";
				for(int j=0; j<vObj.size(); j++){
					CashPayments1 objCashPayment = (CashPayments1) vObj.get(j);

					String strPaymentType = PstPaymentSystem.getTypePayment(objCashPayment.getPaymentType());
					String strNumber = String.valueOf(j+1);
					String strCurrency = String.valueOf(hashCurrType.get(""+objCashPayment.getCurrencyId()));
					String strAmount = FRMHandler.userFormatStringDecimal( (objCashPayment.getAmount()));
					String strRate = FRMHandler.userFormatStringDecimal(objCashPayment.getRate());
					String strTotal = FRMHandler.userFormatStringDecimal(objCashPayment.getAmount() * objCashPayment.getRate());
					dPaymentPerType = dPaymentPerType + (objCashPayment.getAmount() * objCashPayment.getRate());

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


public String generateHeader(String defaultCurrency){
	return "<tr>" +
			  "<td width=\"4%\" class=\"listgentitle\">NO</td>" +
			  "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
			  "<td width=\"26%\" class=\"listgentitle\">TOTAL ("+defaultCurrency+")</td>" +
			"</tr>";
}

public String generateItemHeader(String strItemHeader){
	return "<tr valign=\"top\">" +
			  "<td class=\"listgensell\" colspan=\"5\"><b>"+strItemHeader+"</b></td>" +
			"</tr>";
}

public String generateContent(String strNum, String strCurrency, String strAmount, String strRate, String strTotal){
	return "<tr valign=\"top\">" +
			  "<td width=\"4%\" class=\"listgensell\" align=\"center\">"+strNum+"</td>" +
			  "<td width=\"24%\" class=\"listgensell\">"+strCurrency+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strAmount+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strRate+"</td>" +
			  "<td width=\"26%\" class=\"listgensell\" align=\"right\">"+strTotal+"</td>" +
			"</tr>";
}

public String generateItemFooter(String strItemFooter, String strValue){
	return "<tr valign=\"top\">" +
			  "<td class=\"listgensell\" colspan=\"4\"><b>"+strItemFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateFooter(String strFooter, String strValue){
	return "<tr valign=\"top\">" +
			  "<td class=\"listgensell\" colspan=\"4\" ><b>"+strFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateBlankSpace(){
	return "<tr valign=\"top\">"  +
			  "<td class=\"listgensell\" colspan=\"5\">&nbsp;</td>" +
			"</tr>";
}
%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
int start = FRMQueryString.requestInt(request, "start");
long billMainID = FRMQueryString.requestLong(request,"hidden_billmain_id");
int recordToGet = 100;

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
	if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK) {
		srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
	}
}
catch(Exception e) {
}

String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");
String where="";
String whereCashCredit="";
String whereCredit="";

if(iSaleReportType==6){
      whereCashCredit = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND ("+
                        "("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0"+
                        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+
                        " ) "+
                        " OR "+
                        " ("+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1"+
                        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_OPEN+
                        ")) ";
}

if(srcSaleReport.getShiftId()!=0) {
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

if(srcSaleReport.getLocationId()!=0) {
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}

if(srcSaleReport.getCurrencyOid()!=0) {
      whereCashCredit = whereCashCredit + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
}

//+perCashier
if (srcSaleReport.getCashMasterId()!=0) {
      whereCashCredit = whereCashCredit + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
 }

vectSize = PstBillMain.getCountPerCashier(whereCashCredit);
if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST){
	try	{
		start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
		if (srcSaleReport== null){
			srcSaleReport= new SrcSaleReport();
		}
	}
	catch(Exception e){
		srcSaleReport = new SrcSaleReport();
	}
}
else{
	session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
}

Location location = new Location();
try{
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e){
	location.setName("Semua toko");
}

//Vector records = PstBillMain.list(start,recordToGet, where,"");
//Vector records = PstBillMain.list(0, 0, where, "");
//+List Per Cashier
//add opie 23-06-2012
int paymentDinamis = 0;
paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));

String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

Vector recordsCash = new Vector();
//Vector recordsCredit = new Vector();


if(iSaleReportType==6){
 recordsCash=PstBillMain.listPerCashier(0, 0, whereCashCredit, order);
 //recordsCredit=PstBillMain.listPerCashier(0, 0, whereCredit, order);
}

//coba di buka untuk payment dinamis
//opie-eyek 07022013
//String strTemp ="";
//Vector vResult = new Vector();
Vector vRusltCash = new Vector();

/*String strCurrencyType = "All";
if(srcSaleReport.getCurrencyOid() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}

String cashier = "All Cashier";
if(srcSaleReport.getCashMasterId() != 0) {
        String whereClause = PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
        Vector listCashier = PstCashMaster.list(0, 0, whereClause, "");
        CashMaster cashMaster = (CashMaster)listCashier.get(0);
        cashier = ""+cashMaster.getCashierNumber();

}*/

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdEdit(oid){
    document.frm_reportsale.hidden_billmain_id.value=oid;
    document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="delivery_order_material_edit_sales.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="delivery_order_list_sales.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="delivery_order_list_sales.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="delivery_order_list_sales.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="delivery_order_list_sales.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_delivery_order_sales.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
}
function printExcel(){
    window.open("<%=approot%>/servlet/com.dimata.printman.SalePerInvoiceExcel?sale_type=<%=iSaleReportType%>&command=<%=Command.LIST%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");

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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Pengiriman Barang > Cari Pengiriman Barang<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="<%=billMainID%>">
              <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" align="center">&nbsp;</td>
                </tr>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" class="command">
                    <b><%=textListTitleHeader[SESS_LANGUAGE][3]%> : <%=location.getName()%> </b> </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" class="command">
                    <b>Tanggal : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>
                <%if(iSaleReportType==6){%>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">&nbsp;</td>
                    </tr>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><b>PENGIRIMAN BARANG</b></td>
                    </tr>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawList(out, SESS_LANGUAGE,recordsCash,srcSaleReport, start, vRusltCash, srcSaleReport.getCurrencyOid(),paymentDinamis)%></td>
                    </tr>
                    <%--
                     <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><b>PENJUALAN KREDIT</b></td>
                    </tr>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawList(out, SESS_LANGUAGE,recordsCredit,srcSaleReport, start, vResult, srcSaleReport.getCurrencyOid(),paymentDinamis)%></td>
                    </tr>
                    --%>
                <%}%>
                <tr align="left" valign="top">
                  <td height="8" align="left" colspan="3" class="command"> <span class="command">
                    <%
                        ctrLine.setLocationImg(approot+"/images");
                        ctrLine.initDefault();
                    %>
                    </span>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3">
                    <table width="100%" border="0">
                      <tr>
                        <td width="61%">
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="95%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="39%">&nbsp;</td>
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

