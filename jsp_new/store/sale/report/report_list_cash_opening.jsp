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
                 com.dimata.pos.entity.masterCashier.*,
                 com.dimata.pos.entity.balance.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeaderFilter[][] = {
	{"NO","Lokasi","Tanggal Opening","Shift","Kasir No.", "Invoice"},
	{"NO","Location","Opening Date","Shift","No. Cashier","Invoice"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","TGL","NOMOR","KONSUMEN","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","HPP","CATATAN"},
	{"NO","DATE","NUMBER","CUSTOMER","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","COGS","REMARK"}
};

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang","Kasir"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type","Cashier"}
};

public String drawList(int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
         for(int i=0; i<objectClass.size(); i++) {
	   Vector temp = (Vector)objectClass.get(i);
           CashCashier cashCashier = (CashCashier)temp.get(0);
           CashMaster cashMaster = (CashMaster)temp.get(1);
           Location location2 = (Location)temp.get(2);
           Shift shift = (Shift)temp.get(3);
           Vector rowx = new Vector();

         rowx.add("<tr><td><table width=\"98%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "<tr><td>" +textListMaterialHeaderFilter[0][0]+"</td>" +
                "<td>" +(start+i+1)+"</td>" +
                "<td>" +textListMaterialHeaderFilter[language][1]+"</td>" +
                "<td>" +location2.getName()+"</td>" +
                "<td>" +textListMaterialHeaderFilter[language][2]+"</td>" +
                "<td>" +Formater.formatDate(cashCashier.getCashDate(),"dd/MM/yyyy HH:mm:ss")+"</td>" +
                "<td>" +textListMaterialHeaderFilter[language][3]+"</td>" +
                "<td>" +shift.getName()+"</td>" +
                "<td>" +textListMaterialHeaderFilter[language][4]+"</td>" +
                "<td>" +cashMaster.getCashierNumber()+"</td></tr>");
      }
   }
    return result;
}
/*public String drawListFilter(JspWriter outObj, int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId) {
    //String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");

        ctrlist.dataFormat("","3%","0","0","center","bottom"); //no
        ctrlist.dataFormat("","7%","0","0","center","bottom"); // lokasi
        ctrlist.dataFormat("","6%","0","0","center","bottom"); // tgl opening
        ctrlist.dataFormat("","6%","0","0","center","bottom"); // shift
	ctrlist.dataFormat("","6%","0","0","center","bottom"); // kasir no.
        ctrlist.dataFormat("","36%","0","0","center","bottom"); // invoice
        ctrlist.dataFormat("","3%","0","0","center","bottom"); //no
        ctrlist.dataFormat("","7%","0","0","center","bottom"); // lokasi
        ctrlist.dataFormat("","6%","0","0","center","bottom"); // tgl opening
        ctrlist.dataFormat("","6%","0","0","center","bottom"); // shift
	

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

	String where = "";
        for(int i=0; i<objectClass.size(); i++) {
	     CashCashier cashCashier = (CashCashier)objectClass.get(i);
            CashMaster cashMaster = new CashMaster();
            try {
                cashMaster = PstCashMaster.fetchExc(srcSaleReport.getCashMasterId());
            }
			catch(Exception e) {
				System.out.println(e);
			}

            Location location = new Location();
            try {
                location = PstLocation.fetchExc(cashMaster.getLocationId());
            }
            catch(Exception e1) {
		System.out.println(e1);
            }

            BillMain billMain = new BillMain();
            try {
                billMain = PstBillMain.fetchExc(cashCashier.getOID());
            }
            catch(Exception e) {
				System.out.println(e);
            }

            rowx = new Vector();
            rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");
            rowx.add("<div align=\"left\">"+location.getName()+"</div>");
            rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");
            rowx.add("<div align=\"left\">"+Formater.formatDate(cashCashier.getCashDate(),"dd/MM/yyyy HH:mm:ss")+"</div>");
            rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");
            rowx.add("<div align=\"left\">"+billMain.getShiftId()+"</div>");
            rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");
            rowx.add("<div align=\"left\">"+cashMaster.getCashierNumber()+"</div>");
            rowx.add("<div align=\"left\"></div>");


            //where = PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+cashCashier.getOID();
            //Vector list = PstBillDetail.list(0,0,where,"");
            //Vector list = SessBilling.listInvoiceDetail(0, 0, where, "");
            //Vector list = PstBillMain.listPerCashier(0, 0,where,"");
            //rowx.add(drawList(outObj, language, list, srcSaleReport, start, currencyId));

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(billMain.getOID()));
        }

        ctrlist.draw(outObj);
    }
    return"";
}


 public String drawList(JspWriter outObj, int language,Vector objectClass,SrcSaleReport srcSaleReport, int start, long currencyId) {

    //String result = "";
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
        ctrlist.dataFormat(textListMaterialHeader[language][2],"7%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][1],"7%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][3],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][11],"7%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][4],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][5],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][6],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][7],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][8],"11%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][9],"10%","0","0","center","bottom");												

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
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
		for(int i=0; i<objectClass.size(); i++)	{
			BillMain billMain = (BillMain)objectClass.get(i);

            ContactList contactlist = new ContactList();
            try	{
                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
            }
			catch(Exception e) {
				System.out.println("Contact not found ...");
			}

            rowx = new Vector();			
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
			rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")+"</div>");
            rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
			rowx.add("<div align=\"left\">"+billMain.getNotes()+"</div>");
			if(currencyId != 0) {
            	bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
				diskon = billMain.getDiscount();
				pajak = billMain.getTaxValue();
				servis = billMain.getServiceValue();
			}
			else {
				bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * billMain.getRate();
				diskon = billMain.getDiscount() * billMain.getRate();
				pajak = billMain.getTaxValue() * billMain.getRate();
				servis = billMain.getServiceValue() * billMain.getRate();
			}
			netto = bruto - diskon + pajak + servis;
			dp = PstPendingOrder.getDpValue(billMain.getOID());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(servis)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(netto)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
            //double totCost = PstBillDetail.getTotalCOGS(billMain.getOID());			
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totCost)+"</div>");

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
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalBruto)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDisc)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalTax)+"</b></div>");								
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalService)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalNetto)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDp)+"</b></div>");		
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
}*/
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

String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			" AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;

if(iSaleReportType == PstCashPayment.CASH){
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
}else{
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
}

if(srcSaleReport.getShiftId()!=0) {
	//where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
          where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

if(srcSaleReport.getLocationId()!=0) {
	//where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
        where = where + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}       

if(srcSaleReport.getCurrencyOid()!=0) {
	where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
}

//+perCashier
if (srcSaleReport.getCashMasterId()!=0) {
        //where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+srcSaleReport.getCashMasterId();
          where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
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
	location.setName("Semua toko");
}

//Vector records = PstBillMain.list(start,recordToGet, where,"");
  //Vector records = PstBillMain.list(0, 0, where, "");
//list berdasarkan open dan closing
Vector recordList = PstCashCashier.listCashOpening(start, recordToGet, where, "");
//+List Per Cashier
 Vector records = PstBillMain.listPerCashier(0, 0, where, "");

Vector vResult = new Vector();
//if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
   // PstCashPayment objPstCashPayment = new PstCashPayment();
    //vResult = objPstCashPayment.getListPayment(srcSaleReport,PstBillMain.TYPE_INVOICE, iSaleReportType);
    //String strTemp = drawListPayment(vResult);
//}

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
	document.frm_reportsale.action="invoice_edit.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportsale.jsp";
	document.frm_reportsale.submit();
	//history.back();
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
            &nbsp;Laporan Penjualan > Penjualan Per Cash Opening<!-- #EndEditable --></td>
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
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b>Tipe : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : "Penjualan Kredit"%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][9]%> : <%=cashier%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, recordList, start, srcSaleReport, srcSaleReport.getCurrencyOid())%></td>
                </tr>
                
				<%
			        //if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
					//String strTemp = drawListPayment(vResult);
				%>
				<tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b>Daftar Pembayaran</b></td>
                </tr>							
                <tr align="left" valign="top"> 
                  <!--<td height="22" valign="middle" colspan="3"><%=//strTemp%></td>-->
                </tr>							
				<%
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
