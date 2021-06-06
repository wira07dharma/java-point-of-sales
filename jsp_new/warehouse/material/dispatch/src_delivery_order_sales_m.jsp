<%-- 
    Document   : src_delivery_order_sales_m
    Created on : Aug 26, 2014, 10:44:33 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@page import="com.dimata.pos.entity.payment.CashPayments1"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@page import="com.dimata.pos.entity.payment.CashPayments"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.pos.entity.masterCashier.CashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.posbo.entity.masterdata.Shift"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstShift"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcSaleReport"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>

<!DOCTYPE html>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!

public static final String textListHeader[][] =
{
	{"Lokasi","Shift","Kasir","Supplier","Kategori","Merk","Tanggal","Urut Berdasar","Kode Sales","Sub Kategori","Tipe Transaksi","Dikelompokkan berdasar"},
	{"Location","Shift","Cashier","Supplier","Category","Mark","Sale Date","Sort By","Sales Code","Sub Category","Transaction Type","Grouped by"}
};

public static final String textListTitleHeader[][] =
{
	{"Laporan Transaksi","Laporan Penjualan Harian","Pencarian","Cari Laporan","print excel"},
	{"Transaction Report","Daily Sale Report","Searching","Report Search","print excel"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){
			result = textListHeader[language][index] + " " + prefiks;
		}
		else
		{
			result = prefiks + " " + textListHeader[language][index];
		}
	}
	else
	{
		result = textListHeader[language][index];
	}
	return result;
}

%>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","TGL","NOMOR INVOICE ","KONSUMEN","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","HPP"," NOMOR STORE REQUEST","STATUS"},
	{"NO","DATE","INVOICE NUMBER","CUSTOMER","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","COGS","STORE REQUEST NUMBER","STATUS"}
};

public static final String textListTitleHeaderList[][] = {
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
                ctrlist.dataFormat(textListMaterialHeader[language][12],"20%","0","0","center","bottom");
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
                        rowx.add("<div align=\"left\">"+I_DocStatus.fieldDocumentStatus[billMain.getBillStatus()]+"</div>");
                        
                        String notePo="";
                        try{
                            String[] smartPhonesSplits = billMain.getNotes().split("\\;");
                            notePo=smartPhonesSplits[0];
                        }catch(Exception ex){
                            notePo=billMain.getNotes();
                        }
                        rowx.add("<div align=\"left\">"+notePo+"</div>");
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

       ctrlist.drawBootstrap(outObj);
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
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);
int iSaleReportType = 6;

CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int start = FRMQueryString.requestInt(request, "start");
long billMainID = FRMQueryString.requestLong(request,"hidden_billmain_id");


long status = FRMQueryString.requestLong(request,"status");


int recordToGet = 100;

SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport); 

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
//frmSrcSaleReport.requestEntity(srcSaleReport);
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


int oidDate = FRMQueryString.requestInt(request, frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_USE_DATE] );
String  invoiceNumber    =   FRMQueryString.requestString(request,FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_INVOICE_NUMBER] );
String  notes           =   FRMQueryString.requestString(request,  FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_NOTE]); 
long locationn   =   FRMQueryString.requestLong(request, FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID] );    
frmSrcSaleReport.requestEntity(srcSaleReport);
Vector vectSt = new Vector(1,1);
String[] strStatus = request.getParameterValues(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_STATUS]);
if(strStatus!=null && strStatus.length>0){
     for(int i=0; i<strStatus.length; i++){
            try{
                    vectSt.add(strStatus[i]);  
            }catch(Exception exc){  
                    System.out.println("err");
            }
     }
} 
srcSaleReport.setStatus(vectSt);
String startDate ="";    
String endDate = "";

if(srcSaleReport.getDateFrom() == null){  
    srcSaleReport.setDateFrom(new Date());
    srcSaleReport.setDateTo(new Date());
}
if(srcSaleReport.getUseDate() !=0){   
 startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");    
 endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");  
}
String where="";
String whereCashCredit="";
String whereCredit="";

if(iSaleReportType==6){
      whereCashCredit = 
                        "(("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0"+ 
                        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+") "+
                        " OR "+
                        " ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1"+
                        " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_OPEN+") )";
}

if(srcSaleReport.getUseDate() !=0){   
       whereCashCredit =  whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+ " BETWEEN '"+startDate+"' AND '"+endDate+"'";
        
}

if(srcSaleReport.getShiftId()!=0) {
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

if(srcSaleReport.getStatus()!=null && srcSaleReport.getStatus().size() > 0 ) {  
    //whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
    for (int b = 0; b < srcSaleReport.getStatus().size() ; b++) {
        if (whereCashCredit.length() != 0) {
            whereCashCredit = whereCashCredit + " OR " + "(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " =" + srcSaleReport.getStatus().get(b) + ")";
        } else {
            whereCashCredit = "(CBM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_STATUS] + " =" + srcSaleReport.getStatus().get(b) + ")";
        }
    }
}

if(invoiceNumber !="") {
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+" LIKE '%"+invoiceNumber+"%'";
}

if(srcSaleReport.getNote()!="") { 
      String str = srcSaleReport.getNote();
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_NOTES]+" LIKE '%"+ str+"%'";
}

if(locationn !=0) {
      whereCashCredit = whereCashCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+locationn;
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
	location.setName("Semua Outlet");
}

//add opie 23-06-2012
int paymentDinamis = 0;
paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));

String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

Vector recordsCash = new Vector();
//Vector recordsCredit = new Vector();


if(iSaleReportType==6){
    
 recordsCash=PstBillMain.listPerCashier(0, 0, whereCashCredit, order);

}

Vector vRusltCash = new Vector();

%>
<html>
    <head>
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
        <link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="../../../styles/bootstrap3.1/libs/html5shiv.js"></script>
          <script src="../../../styles/bootstrap3.1/libs/respond.min.js"></script>
        <![endif]-->
        
        <script language="JavaScript">

        function cmdSearch(){
                document.frmsrcreportsale.command.value="<%=Command.LIST%>";
                document.frmsrcreportsale.action="src_delivery_order_sales_m.jsp";
                document.frmsrcreportsale.submit();
        }


        //change kasir based on Location
        function cmdChangeLocation(){
           document.frmsrcreportsale.submit();
        }

        </script>
        <script language="JavaScript">
            <!--

            function cmdEdit(oid){
                document.frmsrcreportsale.hidden_billmain_id.value=oid;
                document.frmsrcreportsale.command.value="<%=Command.EDIT%>";
                    document.frmsrcreportsale.action="delivery_order_material_edit_sales_m.jsp";
                    document.frmsrcreportsale.submit();
            }

            function cmdListFirst(){
                    document.frmsrcreportsale.command.value="<%=Command.FIRST%>";
                    document.frmsrcreportsale.action="delivery_order_list_sales.jsp";
                    document.frmsrcreportsale.submit();
            }

            function cmdListPrev(){
                    document.frmsrcreportsale.command.value="<%=Command.PREV%>";
                    document.frmsrcreportsale.action="delivery_order_list_sales.jsp";
                    document.frmsrcreportsale.submit();
            }

            function cmdListNext(){
                    document.frmsrcreportsale.command.value="<%=Command.NEXT%>";
                    document.frmsrcreportsale.action="delivery_order_list_sales.jsp";
                    document.frmsrcreportsale.submit();
            }

            function cmdListLast(){
                    document.frmsrcreportsale.command.value="<%=Command.LAST%>";
                    document.frmsrcreportsale.action="delivery_order_list_sales.jsp";
                    document.frmsrcreportsale.submit();
            }

            function cmdBack(){
                    document.frmsrcreportsale.command.value="<%=Command.BACK%>";
                    document.frmsrcreportsale.action="src_delivery_order_sales.jsp";
                    document.frmsrcreportsale.submit();
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
    </head>
    <body class="skin-blue">
        <%@ include file = "../../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Delivery Order</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmsrcreportsale" method ="post" action=""  role="form">
                        <!--form hidden -->
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="add_type" value="">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="approval_command">
                        <input type="hidden" name="hidden_billmain_id" value="<%=billMainID%>">
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-md-5">
                                        <div class="form-group">
                                         <label for="exampleInputEmail1">NOMOR INVOICE</label> 
                                         <input type="text" class="form-control"  placeholder="No Invoice" name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_INVOICE_NUMBER] %>"  value="<%=invoiceNumber%>">
                                       </div>
                                       <div class="form-group">
                                         <label for="exampleInputEmail1">NOMOR STORE REQUEST</label>
                                         <input type="text" class="form-control"  placeholder="No Store Request" name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_NOTE] %>"  value="<%=srcSaleReport.getNote()%>">
                                       </div>
                                     </div>
                                     <div class="col-md-7">
                                        <div class="form-group">
                                            <label for="exampleInputEmail1">Lokasi</label>
                                            <%
                                                    long selectedLocationId = FRMQueryString.requestLong(request, frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID]);
                                                    Vector val_locationid = new Vector(1,1);
                                                    Vector key_locationid = new Vector(1,1);
                                                    Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                    val_locationid.add("0");
                                                    key_locationid.add("Semua Toko");
                                                    for(int d=0;d<vt_loc.size();d++){
                                                            Location loc = (Location)vt_loc.get(d);
                                                            val_locationid.add(""+loc.getOID()+"");
                                                            key_locationid.add(loc.getName());
                                                    }
                                                    String select_loc = "0";
                                                    if (selectedLocationId!=0){
                                                        select_loc= ""+selectedLocationId;

                                                    }
                                            %>
                                            <%=ControlCombo.drawBootsratap(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, "", "form-control-date-medium")%>
                                         </div>
                                         <div class="form-group">
                                            <label for="exampleInputEmail1">Tanggal</label><br>
                                            <input type="radio" class="formElemen" name="<%=frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_USE_DATE]%>" <%if(srcSaleReport.getUseDate()==0){%>checked<%}%> value="0">
                                            Semua Tanggal
                                         </div>
                                         <div class="form-group">
                                            <input type="radio" class="formElemen" name="<%=frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_USE_DATE] %>" <%if(srcSaleReport.getUseDate()==1){%>checked<%}%> value="1">
                                            Dari
                                            <%=ControlDate.drawDateWithBootstrap(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReport.getDateFrom(),"form-control-date",4,-8)%> 
                                            s/d <%=ControlDate.drawDateWithBootstrap(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TO_DATE],srcSaleReport.getDateTo(),"form-control-date",4,-8)%>
                                        </div>
                                    </div>
                                  </div>
                                </div>
                                <div class="box-footer">
                                        <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                </div>
                                        <br>
                                <div class="row">
                                    <div class="col-md-12">
                                            <%=drawList(out, SESS_LANGUAGE,recordsCash,srcSaleReport, start, vRusltCash, srcSaleReport.getCurrencyOid(),paymentDinamis)%>
                                    </div>
                                </div>
                                <div class="box-footer clearfix">
                                     <% 
                                            ctrLine.setLocationImg(approot+"/images");
                                            ctrLine.initDefault();
                                            String strList = ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet);
                                            if(strList.length()>0){
                                            %>
                                            <%=strList%>
                                            <%}%>
                                </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../../styles/bootstrap3.1/libs/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../../styles/bootstrap3.1/libs/raphael-min.js"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>

