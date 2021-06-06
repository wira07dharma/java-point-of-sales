<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
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
	{"NO","TGL","NOMOR","KONSUMEN","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","HPP","CATATAN","WAKTU"},
	{"NO","DATE","NUMBER","CUSTOMER","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","COGS","REMARK","WAKTU"}
};

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang","Kasir"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type","Cashier"}
};

public static final String txtGlobal[][]=
{
       {"Laporan Penjualan > Penjualan Per Invoice","Lokasi","Tipe","Kasir","Mata Uang","Semua Shift","Semua Kasir","Semua Lokasi","Tanggal","PENJUALAN CASH","PENJUALAN CREDIT","Daftar Pembayaran"},
       {"Report Sales > Sales Per Invoice","Location","Type","Cashier","Currency","All Shift","All Cashier","All Location","Date","CASH SALES","CREDIT SALES","Payment List"}
};

public String drawList(JspWriter outObj, int language,Vector objectClass,SrcSaleReport srcSaleReport, int start, Vector vectPay, long currencyId, int typePayment,int integrasi,String dataAddress) {

        //String result = "";
        String frmCurrency = AppValue.getValueByKey("FORMAT_LOCAL_CURRENCY");//"#,###";
        String frmCurrencyUsd = AppValue.getValueByKey("FORMAT_EXCHANGE_CURRENCY");
        
	if(objectClass!=null && objectClass.size()>0){   
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");
                ctrlist.setBorder(1);
		
		ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][2],"7%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][1],"7%","0","0","center","bottom");

                ctrlist.dataFormat(textListMaterialHeader[language][12],"7%","0","0","center","bottom");//jam

                ctrlist.dataFormat(textListMaterialHeader[language][3],"15%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][11],"7%","0","0","center","bottom");
                //ctrlist.dataFormat(textListMaterialHeader[language][4],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][8],"11%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][5],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][6],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][7],"10%","0","0","center","bottom");
                //ctrlist.dataFormat(textListMaterialHeader[language][8],"11%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][4],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][9],"10%","0","0","center","bottom");
                

		
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("");
		ctrlist.setLinkSufix("");
		ctrlist.reset();
                


                double bruto = 0;
		double diskon = 0;
		double pajak = 0;
		double servis = 0;
		double netto = 0;
		double dp = 0;
		double totalBruto = 0;
                double totalDisc = 0;
                double totalTax = 0;
                double totalService = 0;						
                double totalNetto = 0;
		double totalDp = 0;
		double totalCost = 0;
                String password = "";
                String username="";
                String dbURL="";
                
                if (integrasi==1){
                    String dataAddressTemp[] = dataAddress.split(";");
                    dbURL = dataAddressTemp[0];
                    username = dataAddressTemp[1];

                    try {
                        password = dataAddressTemp[2];
                    } catch (Exception e) {
                    }
                }

		for(int i=0; i<objectClass.size(); i++)	{
			BillMain billMain = (BillMain)objectClass.get(i);

            ContactList contactlist = new ContactList();
            try	{
                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
            }
			catch(Exception e) {
				//System.out.println("Contact not found ...");
			}

            rowx = new Vector();			
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
			rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")+"</div>");
                        rowx.add("<div align=\"left\">"+Formater.formatTimeLocale(billMain.getBillDate(),"kk:mm:ss")+"</div>");
            rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
			rowx.add("<div align=\"left\">"+billMain.getNotes()+"</div>");
			if(currencyId != 0) {
            	bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
				diskon = billMain.getDiscount();
				pajak = billMain.getTaxValue();
				servis = billMain.getServiceValue();
			}
			else {
                                if(typePayment!=2){
                                    bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * billMain.getRate();
                                    diskon = billMain.getDiscount() * billMain.getRate();
                                    pajak = billMain.getTaxValue() * billMain.getRate();
                                    servis = billMain.getServiceValue() * billMain.getRate();
                                }else{
                                    bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
                                    diskon = billMain.getDiscount();
                                    pajak = billMain.getTaxValue();
                                    servis = billMain.getServiceValue();
                                }

			}
			netto = bruto - diskon + pajak + servis;
			dp = PstPendingOrder.getDpValue(billMain.getOID());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(servis)+"</div>");
            rowx.add("<div align=\"right\">"+Formater.formatNumber(netto, frmCurrency)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
            if (integrasi==1){

                try {
                    Connection conn = DriverManager.getConnection(dbURL, username, password);
                    String sql = "SELECT * FROM tb_piutang WHERE id_piutang = '"+billMain.getOID()+"'";
                    Statement statement = conn.createStatement();
                    ResultSet result = statement.executeQuery(sql);
                    
                    conn.close();
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            
            }

            totalBruto += bruto;
            totalDisc += diskon;
            totalTax += pajak;
            totalService += servis;
            totalNetto += netto;
			totalDp += dp;
			//totalCost = totalCost + totCost;										
			
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(billMain.getOID()));
        }

        rowx = new Vector();
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalBruto, frmCurrencyUsd)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalDisc, frmCurrencyUsd)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalTax, frmCurrencyUsd)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalService, frmCurrencyUsd)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalNetto, frmCurrency)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalDp, frmCurrencyUsd)+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></div>");				
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
        sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"1\">" + sHeader + strContent + sFooter + "</table>";
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
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"1\">" + sHeader + strContent + sFooter + "</table>";
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
		sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"1\">" + sHeader + strContent + sFooter + "</table>";
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

//GET SYSTEM PROPERTY FOR INTEGRATION
String integrationDb2 = "";
try{
    integrationDb2 = PstSystemProperty.getValueByName("INTEGRASI_DB2");
}catch(Exception ex){
    integrationDb2 ="";
}

int integration = 0;

if (integrationDb2.length()>0){
    integration=1;
}
    

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);
int vectSize = 0;

SaleReportDocument saleReportDocument = new SaleReportDocument();
try{
	srcSaleReport  = (SrcSaleReport )session.getValue(SaleReportDocument.SALE_REPORT_DOC);
        
}catch(Exception e){
	srcSaleReport  = new SrcSaleReport();
}

if(srcSaleReport ==null){
	srcSaleReport = new SrcSaleReport();
        
}



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
String whereCash="";
String whereCredit="";

if(iSaleReportType==6){
    whereCash = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0";
    whereCredit = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1";
}else{
    where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
}

if(iSaleReportType == 6){
    whereCash = whereCash  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
    whereCredit = whereCredit  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
}

if(iSaleReportType == PstCashPayment.CASH){
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
}else{
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
}

if(srcSaleReport.getShiftId()!=0) {
          where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
          whereCash = whereCash + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
          whereCredit = whereCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

if(srcSaleReport.getLocationId()!=0) {
        where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
         whereCash = whereCash + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
          whereCredit = whereCredit + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}       

if(srcSaleReport.getCurrencyOid()!=0) {
	where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
        whereCash = whereCash + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
        whereCredit = whereCredit + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
}

//+perCashier
if (srcSaleReport.getCashMasterId()!=0) {
          where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
           whereCash = whereCash + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
            whereCredit = whereCredit + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
 }

//vectSize = PstBillMain.getCount(where);
//getCount +PerCashier
  vectSize = PstBillMain.getCountPerCashier(where);
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
	location.setName(txtGlobal[SESS_LANGUAGE][7]);
}

//Vector records = PstBillMain.list(start,recordToGet, where,"");
  //Vector records = PstBillMain.list(0, 0, where, "");
//+List Per Cashier
//add opie 23-06-2012
int paymentDinamis = 0;
paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));

String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

Vector records = new Vector();
Vector recordsCash = new Vector();
Vector recordsCredit = new Vector();

if (paymentDinamis == 2){//menggunakan cashier hanoman
    records=PstBillMain.listPerCashierUsingCashierHanoman(0, 0, where, order);
}else{
    if(iSaleReportType==6){
         recordsCash=PstBillMain.listPerCashier(0, 0, whereCash, order);
         recordsCredit=PstBillMain.listPerCashier(0, 0, whereCredit, order);
    }else{
        records=PstBillMain.listPerCashier(0, 0, where, order);
    }
}

//coba di buka untuk payment dinamis
//opie-eyek 07022013
String strTemp ="";
Vector vResult = new Vector();
Vector vRusltCash = new Vector();




String strCurrencyType = "All";
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

}

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%if(menuUsed == MENU_ICON){%>
        
    <link href="../../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<style>
    .listgen{
       
    }
</style>
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
    </head>
    <body onload="window.print()" style="background-color: #FFF">
        <table width="100%"  cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> &nbsp;<%=txtGlobal[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
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
                    <b><%=txtGlobal[SESS_LANGUAGE][8]%> : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>				
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=txtGlobal[SESS_LANGUAGE][2]%> : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : iSaleReportType==PstBillMain.TRANS_TYPE_CREDIT ?  "Penjualan Kredit" : "Penjualan Cash Dan Kredit"%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][9]%> : <%=cashier%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3">
                     
                  </td>
                  <td height="22" valign="middle" colspan="3">
                      
                  </td>
                </tr><input type="hidden" id="address" value="<%=integrationDb2 %>">
                <%if(iSaleReportType==6){%>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><b><%=txtGlobal[SESS_LANGUAGE][9]%></b></td>
                    </tr>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawList(out, SESS_LANGUAGE,recordsCash,srcSaleReport, start, vRusltCash, srcSaleReport.getCurrencyOid(),paymentDinamis,integration,integrationDb2)%></td>
                    </tr>
                     <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><b><%=txtGlobal[SESS_LANGUAGE][10]%></b></td>
                    </tr>
                    <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawList(out, SESS_LANGUAGE,recordsCredit,srcSaleReport, start, vResult, srcSaleReport.getCurrencyOid(),paymentDinamis,integration,integrationDb2)%></td>
                    </tr>
                <%}else{%>
                   <tr align="left" valign="top">
                       <td height="22" valign="middle" colspan="3"><%=drawList(out, SESS_LANGUAGE,records,srcSaleReport, start, vResult, srcSaleReport.getCurrencyOid(),paymentDinamis,integration,integrationDb2)%></td>
                   </tr>
                <%}%>
                
                   <!-- for payment dynamic 20120417 -->
                   <%
                      // if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {  
				//if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
					//String strTemp = drawListPaymentDynamic(vResult);
				%>
				<tr align="left" valign="top"> 
                  <!--td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b>Daftar Pembayaran</b></td>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%//=strTemp%></td>
                </tr-->							
				<%
				//}
                        // } else {
				%>
                
				<%
                                
				if((records!=null && records.size()>0 || recordsCash!=null && recordsCash.size()>0) && (iSaleReportType == PstCashPayment.CASH || iSaleReportType == 6)){
					//String strTemp = drawListPayment(vResult);
				%>
				<tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top">
                  <%
                  if (paymentDinamis != 2){
                  %>
                     <td height="22" valign="middle" colspan="3"><b><%=txtGlobal[SESS_LANGUAGE][11]%></b></td>
                  <%
                  }
                  %>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">
                      <%
                        if (paymentDinamis != 2){
                                if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                                    if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
                                        PstCashPayment1 objPstCashPayment1 = new PstCashPayment1();
                                        vResult = objPstCashPayment1.getListPaymentDinamis(srcSaleReport,PstBillMain.TYPE_INVOICE, iSaleReportType);
                                        strTemp = drawListPaymentDynamic(vResult);

                                    }
                                    if(iSaleReportType == 6){
                                        PstCashPayment1 objPstCashPayment1 = new PstCashPayment1();
                                        vRusltCash = objPstCashPayment1.getListPaymentDinamis(srcSaleReport,PstBillMain.TYPE_INVOICE, 0);
                                        strTemp = drawListPaymentDynamic(vRusltCash);
                                    }
                                } else {
                                    //Vector vResult = new Vector();
                                    if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
                                        PstCashPayment objPstCashPayment = new PstCashPayment();
                                        vResult = objPstCashPayment.getListPayment(srcSaleReport,PstBillMain.TYPE_INVOICE, iSaleReportType);
                                         strTemp = drawListPayment(vResult);
                                    }
                                }
                            }
                      %>
                      <%=strTemp%>
                  </td>
                </tr>							
                <%
                }

                if(iSaleReportType == 6){
                %>
		<tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top">
                  <%
                  if (paymentDinamis != 2){
                  %>
                     <td height="22" valign="middle" colspan="3"><b><%=txtGlobal[SESS_LANGUAGE][11]%></b></td>
                  <%
                  }
                  %>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"></td>
                </tr>
				<%
				}
                            //}
				%>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					//out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              
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
    </body>
</html>
