<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.pos.session.sale.SessPaymentBillReturn"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
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
                 com.dimata.pos.entity.masterCashier.*,
                 com.dimata.pos.entity.balance.*,
                 com.dimata.pos.entity.payment.*"%>

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
	{"No","Lokasi","Tanggal Opening","Shift","Kasir No.", "Invoice","Tidak ada data transaksi", "TRANSAKSI RETURN ", "TRANSAKSI BATAL", "TRANSAKSI PENJUALAN TUNAI", "TRANSAKSI PENJUALAN KREDIT"},
	{"No","Location","Opening Date","Shift","No. Cashier","Invoice","No transaction data available","RETURN TRANSACTION","CANCEL TRANSACTION", "CASH SALES TRANSACTION","CREDIT SALES TRANSACTION"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","TGL SETTLE","NOMOR","KONSUMEN","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","HPP","CATATAN","WAKTU SETTLE","NOMOR TAGIHAN","TGL PRINT ","WAKTU PRINT", "SALES"},
	{"NO","DATE SETTLE","NUMBER","CUSTOMER","SALES BRUTO","DISC","TAX","SERVICE","SALES NETTO","DP DEDUCTION","COGS","REMARK","TIME SETTLE", "COVER BILL NUMBER","DATE PRINT","TIME PRINT", "SALES"}
    };

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang","Kasir"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type","Cashier"}
};

public static final String textListMaterialHeaderSummaryCash[][] = {
	{"NO.","RANGKUMAN KAS ", "TRANSAKSI","MATA UANG","NILAI","QTY", "TOTAL", "PENJUALAN"},
	{"NO.","SUMMARY","TRANSACTION","CURRENCY","AMOUNT", "QTY", "TOTAL", "SALES"}
};

public static final String textListMaterialHeaderDetail[][] = {
	{"NO.","TIPE","MATA UANG","RATE","JUMLAH","TOTAL"},
	{"NO.","TYPE","CURRENCY","RATE","SUMMARY","TOTAL"}
};

public static final String textListMaterialHeaderSummaryTransaction[][] = {
	{"No","RANGKUMAN TRANSAKSI ", "TRANSAKSI","QTY","NILAI", "Open Bill", "Batal",
         "Bayar Kredit", "Retur Penjualan", "Tunai", "Kredit","PENJUALAN"},
	{"No","SUMMARY TRANSACTION","TRANSACTION","QTY","VALUE", "Open Bill", "Cancel",
         "Pay Credit", "Sales Return", "Cash", "Credit","SALES"}
};





//public String drawList(JspWriter outObj, int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId) {
 public String drawList(JspWriter outObj, int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId, int iSaleReportType, int iViewType ) {
        String result = "";
	 int paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
        if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		//ctrlist.setCellStyle("listgentitle");
                ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("");

        ctrlist.dataFormat(textListMaterialHeaderFilter[language][0],"3%","0","0","center","bottom"); //no
        ctrlist.dataFormat("Data","97%","0","0","center","bottom"); // data
       


		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

	String where = "";
        String whereBillMain = "";
        for(int i=0; i<objectClass.size(); i++) {
	   Vector temp = (Vector)objectClass.get(i);
           CashCashier cashCashier = (CashCashier)temp.get(0);
           CashMaster cashMaster = (CashMaster)temp.get(1);
           Location location2 = (Location)temp.get(2);
           Shift shift = (Shift)temp.get(3);

            rowx = new Vector();        
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"center\">"+textListMaterialHeaderFilter[language][1]+":"+location2.getName()+
                     "&nbsp;&nbsp;"+textListMaterialHeaderFilter[language][2]+":"+Formater.formatDate(cashCashier.getCashDate(),"dd/MM/yyyy HH:mm:ss")+
                     "&nbsp;&nbsp;"+textListMaterialHeaderFilter[language][3]+":"+shift.getName()+
                     "&nbsp;&nbsp;"+textListMaterialHeaderFilter[language][4]+":"+cashMaster.getCashierNumber()+
                    "</div>");
            lstData.add(rowx);
            
            rowx = new Vector();
            rowx.add("<div align=\"left\">&nbsp;</div>");
            rowx.add("<div align=\"left\"><b>"+textListMaterialHeaderFilter[language][9]+
                    "</b></div>");
            lstData.add(rowx);
            
            rowx = new Vector();
           //Tipe pilihan baru
            where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+cashCashier.getOID();
            if(iSaleReportType == PstBillMain.TRANS_TYPE_CASH){
	       whereBillMain = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
            }else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT){
             whereBillMain = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
            }else if (iSaleReportType ==-1){
             /*whereBillMain = where + " AND(("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0"+
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+ ")"+
                            " OR("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0"+
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+ ")"+
                            " OR("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0"+
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                            " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_OPEN+ "))";*/
               whereBillMain = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
            }

            //Vector list = PstBillMain.listPerCashier(0, 0,where,"");
            //add opie 19-06-2012 agar no invoice transaksi sesuai dengan jam tidak loncat2
            String order ="";
            order = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+" ASC, "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" ASC";
            Vector list = PstBillMain.listPerCashier(0, 0,whereBillMain,order);
            rowx.add("<div align=\"left\"></div>");
            rowx.add(drawListInvoice(language, list, srcSaleReport, start, currencyId, iViewType));
            lstData.add(rowx);

            //update opie-eyek untuk transaksi kredit update 24012013
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            if (iSaleReportType ==-1){
            rowx = new Vector();
            rowx.add("<div align=\"left\">&nbsp;</div>");
            rowx.add("<div align=\"left\"><b>"+textListMaterialHeaderFilter[language][10]+
                    "</b></div>");
            lstData.add(rowx);

            rowx = new Vector();
           //Tipe pilihan baru
            where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+cashCashier.getOID();
            whereBillMain = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";

            String orderReturn =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+" ASC, "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" ASC";
            orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            Vector listReturn = PstBillMain.listPerCashier(0, 0,whereBillMain,orderReturn);
            rowx.add("<div align=\"left\"></div>");
            rowx.add(drawListInvoice(language, listReturn, srcSaleReport, start, currencyId, iViewType));
            lstData.add(rowx);
            }

             //update opie-eyek untuk transaksi return update 24012013
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            rowx = new Vector();
            rowx.add("<div align=\"left\">&nbsp;</div>");
            rowx.add("<div align=\"left\"><b>"+textListMaterialHeaderFilter[language][7]+
                    "</b></div>");
            lstData.add(rowx);

            rowx = new Vector();
           //Tipe pilihan baru
            where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+cashCashier.getOID();
            whereBillMain = where + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1"+
                                   " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                                   " OR "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT+") "+
                                   " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+ ")";

            String orderReturn =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+" ASC, "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" ASC";
            orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            Vector listReturn = PstBillMain.listPerCashier(0, 0,whereBillMain,orderReturn);
            rowx.add("<div align=\"left\"></div>");
            rowx.add(drawListInvoice(language, listReturn, srcSaleReport, start, currencyId, iViewType));
            lstData.add(rowx);

            //update opie-eyek untuk transaksi batal 24012013
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            rowx = new Vector();
            rowx.add("<div align=\"left\">&nbsp;</div>");
            rowx.add("<div align=\"left\"><b>"+textListMaterialHeaderFilter[language][8]+
                    "</b></div>");
            lstData.add(rowx);

            rowx = new Vector();
           //Tipe pilihan baru
            where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+cashCashier.getOID();
            whereBillMain = where + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0"+
                                   " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH+
                                   " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_DELETED+ ")";

            String orderCancel =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]+" ASC, "+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" ASC";
            orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            Vector listCancel = PstBillMain.listPerCashier(0, 0,whereBillMain,orderCancel);
            rowx.add("<div align=\"left\"></div>");
            rowx.add(drawListInvoice(language, listCancel, srcSaleReport, start, currencyId, iViewType));
            lstData.add(rowx);
            //Transaction summary digabung
            /*rowx = new Vector();
            Vector listTransaction = PstBillMain.listSummaryTranscation(0, 0, where);
            //Vector listDetailPayment = PstCashPayment.listDetailBayar(0, 0, where);
            Vector listDetailPayment = PstCashPayment.listDetailPaymentWithReturn(0, 0, where);
            rowx.add("<div align=\"left\"></div>");
            //rowx.add(generateSummaryCash(cashCashier.getOID()));
            if(list!=null && list.size()>0){
            rowx.add("<table class=\"listgensell\"><tr><td rowspan =\"2\">" +generateSummaryCash(cashCashier.getOID()) + "</td><td><b>DETAIL PEMBAYARAN</b>" +drawListDetailPayment(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType) +
                     "</td></tr><tr><td><b>SUMMARY TRANSACTION</b>" +drawListTransaction(language, listTransaction, currencyId, start, cashCashier.getOID(), iSaleReportType) +
                    "</td></tr></table>");
            }
            else {
               rowx.add("<table class=\"listgensell\"><tr><td><div align=\"center\">"+textListMaterialHeaderFilter[language][6]+"</div></td></tr></table>");
            }*/

            //Transaction summary dipisah
            rowx = new Vector();
            //Vector listTransaction = PstBillMain.listSummaryTranscation(0, 0, where);
            //Vector listDetailPayment = PstCashPayment.listDetailBayar(0, 0, where);
            Vector listDetailPayment = new Vector();
            if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                //Vector listDetailPaymentTransactionReturn = PstCashPayment1.listDetailPaymentDinamisWithReturnTransaction(0, 0, where);
                listDetailPayment = PstCashPayment1.listDetailPaymentDinamisWithReturn(0, 0, where,new Vector());
                rowx.add("<div align=\"left\"></div>");
                if(list!=null && list.size()>0){
                      rowx.add("<table class=\"listgensell\"><tr><td rowspan =\"3\">" +generateSummaryCash(cashCashier.getOID()) + "</td><td><b><color=\"FFFFFF\">DETAIL PEMBAYARAN</b></color>" +drawListDetailPaymentDinamis(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType,where) +
                               "</td><td>" +generateSummaryTransaction2(language, cashCashier.getOID()) +
                               "</td></tr></table>");
                }else {
               rowx.add("<table class=\"listgensell\"><tr><td><div align=\"center\">"+textListMaterialHeaderFilter[language][6]+"</div></td></tr></table>");
                }
            }else{
                listDetailPayment = PstCashPayment.listDetailPaymentWithReturn(0, 0, where);
                rowx.add("<div align=\"left\"></div>");
                if(list!=null && list.size()>0){
                      rowx.add("<table class=\"listgensell\"><tr><td rowspan =\"3\">" +generateSummaryCash(cashCashier.getOID()) + "</td><td><b><color=\"FFFFFF\">DETAIL PEMBAYARAN</b></color>" +drawListDetailPayment(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType) +
                               "</td><td>" +generateSummaryTransaction2(language, cashCashier.getOID()) +
                               "</td></tr></table>");
                }else {
               rowx.add("<table class=\"listgensell\"><tr><td><div align=\"center\">"+textListMaterialHeaderFilter[language][6]+"</div></td></tr></table>");
                }
            }
            lstData.add(rowx);
        }

         ctrlist.draw(outObj);
    }
    return"";
}

public String drawListInvoice(int language,Vector objectClass,SrcSaleReport srcSaleReport, int start, long currencyId, int iViewType) {
   String result = "";
     String frmCurrency = "#,###";
        String frmCurrencyUsd = "#,###.##";
            String useCoverNumber = PstSystemProperty.getValueByName("CASHIER_USING_COVER_NUMBER");
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
        if(useCoverNumber.equals("1")){
            ctrlist.dataFormat(textListMaterialHeader[language][13],"7%","0","0","center","bottom");
        }

        ctrlist.dataFormat(textListMaterialHeader[language][14], "5%", "0", "0", "center", "bottom"); //tanggal pertama di print
        ctrlist.dataFormat(textListMaterialHeader[language][15], "5%", "0", "0", "center", "bottom"); //tanggal pertama di print
        ctrlist.dataFormat(textListMaterialHeader[language][1],"5%","0","0","center","bottom");//tanggal
        ctrlist.dataFormat(textListMaterialHeader[language][12],"5%","0","0","center","bottom");//waktu
        

        ctrlist.dataFormat(textListMaterialHeader[language][3],"9%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][16],"9%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][11],"7%","0","0","center","bottom");
        //ctrlist.dataFormat(textListMaterialHeader[language][4],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][4],"11%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][5],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][6],"10%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][7],"10%","0","0","center","bottom");
        //ctrlist.dataFormat(textListMaterialHeader[language][8],"11%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][8],"10%","0","0","center","bottom");
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

           if(iViewType == 0) {
		for(int i=0; i<objectClass.size(); i++)	{
			BillMain billMain = (BillMain)objectClass.get(i);
      AppUser ap = new AppUser();
      try {
          ap = PstAppUser.fetch(billMain.getAppUserSalesId());
        } catch (Exception e) {
        }

            ContactList contactlist = new ContactList();
            try	{
                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
            }
	    catch(Exception e) {
			System.out.println("Contact not found ...");
             }
        //if(iViewType == 0) {
            rowx = new Vector();			
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
            if(useCoverNumber.equals("1")){
                rowx.add("<div align=\"left\">"+billMain.getCoverNumber()+"</div>");   
            }

            //cek log history
            String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
            Date count = PstLogSysHistory.getDateLog(whereLog);
            if(count!=null){
                rowx.add("<div align=\"left\">"+Formater.formatDate(count,"dd/MM/yyyy")+"</div>");
                rowx.add("<div align=\"left\">"+Formater.formatTimeLocale(count,"kk:mm:ss")+"</div>");
            }else{
                rowx.add("<div align=\"left\">-</div>");
                rowx.add("<div align=\"left\">-</div>");
            }
	    rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")+"</div>");
            rowx.add("<div align=\"left\">"+Formater.formatTimeLocale(billMain.getBillDate(),"kk:mm:ss")+"</div>");
            
            
            rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
            rowx.add("<div align=\"center\">"+ap.getFullName()+"</div>");
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

            if(billMain.getDocType()!=1){ // not return }
            netto = bruto - diskon + pajak + servis ;
            dp = PstPendingOrder.getDpValue(billMain.getOID());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(servis)+"</div>");
            rowx.add("<div align=\"right\">"+Formater.formatNumber(netto, frmCurrency)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
            } else { //return
            //netto = -bruto + diskon - pajak - servis;
            bruto = -bruto;
            diskon=-diskon;
            pajak=-pajak;
            servis= -servis;
            netto = bruto - diskon + pajak + servis ;
            dp = -dp;
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(servis)+"</div>");
            rowx.add("<div align=\"right\">"+Formater.formatNumber(netto, frmCurrency)+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
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
        if(useCoverNumber.equals("1")){
            rowx.add("<div align=\"left\"></div>");
        }
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalBruto)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDisc)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalTax)+"</b></div>");								
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalService)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalNetto, frmCurrency)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDp)+"</b></div>");		
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></div>");				
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));

       result = ctrlist.draw();
        //ctrlist.draw(outObj);
       }
      else if(iViewType == 1) {
		for(int i=0; i<objectClass.size(); i++)	{
			BillMain billMain = (BillMain)objectClass.get(i);

            ContactList contactlist = new ContactList();
            try	{
                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
            }
	    catch(Exception e) {
			System.out.println("Contact not found ...");
             }
        //if(iViewType == 0) {
            //rowx = new Vector();
            //rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            //rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
	    //rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy")+"</div>");
            //rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
	    //rowx.add("<div align=\"left\">"+billMain.getNotes()+"</div>");
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

            if(billMain.getDocType()!=1){ // not return }
			netto = bruto - diskon + pajak + servis ;
			dp = PstPendingOrder.getDpValue(billMain.getOID());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(servis)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(netto)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
            //double totCost = PstBillDetail.getTotalCOGS(billMain.getOID());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totCost)+"</div>");
            } else { //return
                netto = -bruto + diskon - pajak - servis;
                bruto = -bruto;
			dp = -PstPendingOrder.getDpValue(billMain.getOID());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
            //rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(diskon)+")</div>");
            //rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(pajak)+")</div>");
            //rowx.add("<div align=\"right\">("+FRMHandler.userFormatStringDecimal(servis)+")</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(netto)+"</div>");
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dp)+"</div>");
                }
            totalBruto += bruto;
            totalDisc += diskon;
            totalTax += pajak;
            totalService += servis;
            totalNetto += netto;
			totalDp += dp;
			//totalCost = totalCost + totCost;

            //lstData.add(rowx);
            lstLinkData.add(String.valueOf(billMain.getOID()));
        }

        rowx = new Vector();
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        if(useCoverNumber.equals("1")){
            rowx.add("<div align=\"left\"></div>");
        }
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalBruto)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDisc)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalTax)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalService)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalNetto, frmCurrency)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDp)+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></div>");
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));

       result = ctrlist.draw();
        //ctrlist.draw(outObj);
       }
    }
    return result;
   //return"";
}

//Summary Cash
public String generateSummaryCash(long oidCashCashier ){
    String result = "";
    double saldoAwal=0;
    double jualCash=0;
    double returnCash=0;
    double kembalian=0;
    double returnKembalian=0;
    double jualKotor=0;
    double returnSales=0;
    double jualBersih=0;
    double bayarKredit=0;
    double saldoAkhir = 0;
    double uangDilaci=0;
    double selisih =0;
    String where = "";

    
    CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
    String defaultCurrency = currencyType.getCode();

    saldoAwal = PstBalance.getSaldoAwal(PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"="+oidCashCashier);
    jualCash = PstCashPayment.getPembayaranKotorClosing(oidCashCashier);
    //returnCash = PstCashPayment.getPembayaranKotorTransactionReturnClosing(oidCashCashier,"");
    kembalian = PstCashReturn.getReturnSummary(oidCashCashier);
    //returnKembalian = PstCashReturn.getReturnSummaryTransactionReturn(oidCashCashier,"");

    jualKotor = ((jualCash - kembalian)); //+ (returnCash-returnKembalian));
    returnSales = PstBillDetail.getReturSales(oidCashCashier);
    jualBersih = jualKotor - returnSales;
    bayarKredit = PstCashCreditPayment.getPembayaranKredit2(oidCashCashier);
    //bayarKredit = PstCashPayment.getPembayaranKreditClosing(oidCashCashier);
    //bayarKredit = PstCashPayment.getPembayaranKredit(oidCashCashier);

    saldoAkhir = PstBalance.getSaldoAkhir(oidCashCashier);
    uangDilaci = PstBalance.getUangDiLaci(oidCashCashier);
    selisih = uangDilaci - saldoAkhir;

       result = //"<tr><table width=\"50%\" >"
                //"<tr>" +
                "<table width =\"50%\"><tr><td><table width=\"50%\" border=\"1\" bordercolor=\"FFFFFF\" class=\"listgen\"> " +
                "<tr>" +
		"<td width=\"4%\" class=\"listgentitle\" colspan =\"4\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\">SUMMARY CASH</div></td></tr>" +
                "<tr><td class=\"listgentitle\" border=\"1\" bordercolor=\"FFFFFF\">NO</td>" +
		"<td class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">TRANSAKSI</td>" +
                "<td width=\"5%\" class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">CURRENCY</td>" +
                "<td class=\"listgentitle\" border=\"1\" bordercolor=\"FFFFFF\">AMOUNT</td>" +
		"</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">1.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\">Saldo Awal</div></td>" +
                "<td width=\"24%\" class=\"listgensell\" border=\"1\" bordercolor=\"FFFFFF\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(saldoAwal)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">2.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\"><div align=\"left\">Penjualan Kotor</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(jualKotor)+"</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">3.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\"><div align=\"left\">Retur Sales</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">("+FRMHandler.userFormatStringDecimal(returnSales)+")</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">4.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Penjualan Bersih</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(jualBersih)+"</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">5.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Bayar Kredit</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(bayarKredit)+"</div></td>" +
                "</tr>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">6.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Saldo Akhir</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(saldoAkhir)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">7.</td>" +
                "<td width=\"20%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Uang di laci</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(uangDilaci)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">8.</td>" +
                "<td width=\"20%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Selisih</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\" ><div align=\"right\">"+FRMHandler.userFormatStringDecimal(selisih)+"</div></td>" +
                //"</tr></table></tr>" ;
                "</tr>" +
                "</table></td></tr></table>";

     //return "<tr><td><table width=\"100%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" +
     return result;
                //"</table><td></tr>";
}


public String generateGrandTotSummaryCash(String whereCashCashier, String whereCashCashierBm, String whereCashierCb, String whereCpm){
    String result = "";
    double saldoAwal=0;
    double jualCash=0;
    double kembalian=0;
    double jualKotor=0;
    double returnSales=0;
    double jualBersih=0;
    double bayarKredit=0;
    double saldoAkhir = 0;
    double uangDilaci=0;
    double selisih =0;
    String where = "";

    
    CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
    String defaultCurrency = currencyType.getCode();

    saldoAwal = PstBalance.getSaldoAwal(whereCashCashier);
    jualCash = PstCashPayment.getPembayaranKotorClosing(0, whereCashCashierBm);
    kembalian = PstCashReturn.getReturnSummary(0, whereCashCashierBm);
    jualKotor = jualCash - kembalian;
    returnSales = PstBillDetail.getReturSales(0,whereCashCashierBm);
    jualBersih = jualKotor - returnSales;
    bayarKredit = PstCashCreditPayment.getPembayaranKredit2(0, whereCpm);

    saldoAkhir = PstBalance.getSaldoAkhir(0, whereCashierCb);
    uangDilaci = PstBalance.getUangDiLaci(0, whereCashierCb);
    selisih = uangDilaci - saldoAkhir;

       result = //"<tr><table width=\"50%\" >"
                //"<tr>" +
                "<table width =\"50%\"><tr><td><table width=\"50%\" border=\"1\" bordercolor=\"FFFFFF\" class=\"listgen\"> " +
                "<tr>" +
		"<td width=\"4%\" class=\"listgentitle\" colspan =\"4\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\">GRAND TOTAL SUMMARY CASH</div></td></tr>" +
                "<tr><td class=\"listgentitle\" border=\"1\" bordercolor=\"FFFFFF\">NO</td>" +
		"<td class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">TRANSAKSI</td>" +
                "<td width=\"5%\" class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">CURRENCY</td>" +
                "<td class=\"listgentitle\" border=\"1\" bordercolor=\"FFFFFF\">AMOUNT</td>" +
		"</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\"border=\"1\" bordercolor=\"FFFFFF\">1.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\">Saldo Awal</div></td>" +
                "<td width=\"24%\" class=\"listgensell\" border=\"1\" bordercolor=\"FFFFFF\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(saldoAwal)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">2.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\"><div align=\"left\">Penjualan Kotor</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(jualKotor)+"</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">3.</td>" +
                "<td width=\"10%\" class=\"listgentitle\" nowrap width=\"95\"><div align=\"left\">Retur Sales</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">("+FRMHandler.userFormatStringDecimal(returnSales)+")</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">4.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Penjualan Bersih</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(jualBersih)+"</div></td>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">5.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Bayar Kredit</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(bayarKredit)+"</div></td>" +
                "</tr>" +
                "</tr>" +
                "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\">6.</td>" +
                "<td width=\"10%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Saldo Akhir</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(saldoAkhir)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">7.</td>" +
                "<td width=\"20%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Uang di laci</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(uangDilaci)+"</div></td>" +
                "</tr>" +
                "<td width=\"5%\" class=\"listgentitle\">8.</td>" +
                "<td width=\"20%\" class=\"listgentitle\"nowrap width=\"95\"><div align=\"left\">Selisih</div></td>" +
                "<td width=\"24%\" class=\"listgensell\">"+defaultCurrency+"</td>" +
                "<td width=\"20%\"class=\"listgensell\" ><div align=\"right\">"+FRMHandler.userFormatStringDecimal(selisih)+"</div></td>" +
                //"</tr></table></tr>" ;
                "</tr>" +
                "</table></td></tr></table>";
     return result;
}

//Summary Transaction Terpisah
public String generateSummaryTransaction2(int language,long oidCashCashier ){
    String result = "";
    double openBill=0;
    double cancel=0;
    double bayarKredit=0;
    double returnSales=0;
    double cash=0;
    double creditSales=0;
    double totalSummary=0;
    String where = "";

    double openBillQty=0;
    double cancelQty=0;
    double bayarKreditQty=0;
    double returnSalesQty=0;
    double cashQty=0;
    double creditSalesQty=0;
    double totalQty=0;

    double openBillTransaksi=0;
    double cancelTransaksi=0;
    double bayarKreditTransaksi=0;
    double returnSalesTransaksi=0;
    double cashTransaksi=0;
    double creditSalesTransaksi=0;
    double totalTransaksi=0;



    CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
    String defaultCurrency = currencyType.getCode();

    //summary
    openBill = PstBillMain.getSummaryOpenBill(oidCashCashier);
    cancel = PstBillMain.getSummaryCancel(oidCashCashier);
    bayarKredit = PstCashCreditPayment.getSummaryBayarCredit(oidCashCashier);
    returnSales = PstBillMain.getSummaryReturn(oidCashCashier);
    cash = PstBillMain.getSummaryCash(oidCashCashier);
    creditSales = PstBillMain.getSummarySalesCredit(oidCashCashier);
    totalSummary = cash + creditSales - returnSales;

    //Qty
    openBillQty = PstBillMain.getCountQtySummaryOpenBill(oidCashCashier);
    cancelQty = PstBillMain.getCountQtySummaryCancel(oidCashCashier);
    bayarKreditQty = PstCreditPaymentMain.getCountQtySummaryBayarCredit(oidCashCashier);
    returnSalesQty = PstBillMain.getCountQtySummaryReturn(oidCashCashier);
    cashQty = PstBillMain.getCountQtySummaryCash(oidCashCashier);
    creditSalesQty = PstBillMain.getCountQtySummarySalesCredit(oidCashCashier);
    totalQty = cashQty + creditSalesQty;

    //transaksi
    openBillTransaksi= PstBillMain.getCountTransSummaryOpenBill(oidCashCashier);
    cancelTransaksi=PstBillMain.getCountTransSummaryCancel(oidCashCashier);
    bayarKreditTransaksi=PstCreditPaymentMain.getCountTransSummaryBayarCredit(oidCashCashier);
    returnSalesTransaksi=PstBillMain.getCountTransSummaryReturn(oidCashCashier);
    cashTransaksi=PstBillMain.getCountTransSummaryCash(oidCashCashier);
    creditSalesTransaksi=PstBillMain.getCountTransSummarySalesCredit(oidCashCashier);
    totalTransaksi= cashTransaksi+creditSalesTransaksi;

       result = //"<tr><table width=\"50%\" >"
                //"<tr>" +
                "<table width =\"50%\"><tr><td><table width=\"50%\" border=\"0\" bordercolor=\"FFFFFF\" class=\"listgen\"> " +
                "<tr>" +
		"<td width=\"4%\" class=\"listgentitle\" colspan =\"4\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][1]+"</div></td></tr>" +
                "<tr>" +
                //"<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][0]+"</td>" +
		"<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][2]+"</td>" +
                "<td width=\"5%\" class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][2]+"</td>" +
                "<td width=\"5%\" class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][3]+"</td>" +
                "<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][4]+"</td>" +
		"</tr>";
       if(openBill>0 && openBillQty>0){

        result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">1.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][5]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+openBillTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+openBillQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(openBill)+"</div></td>" +
                "</tr>";
       }
       if(cancel>0 && cancelQty>0){
         result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">2.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][6]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cancelTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cancelQty+"</div></td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(cancel)+"</div></td>" +
                "</tr>";
       }
       if(bayarKredit>0 && bayarKreditQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">3.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][7]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+bayarKreditTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+bayarKreditQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(bayarKredit)+"</div></td>" +
                "</tr>";
       }
       if(returnSales>0 && returnSalesQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">4.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][8]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+returnSalesTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+returnSalesQty+"</div></td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">("+FRMHandler.userFormatStringDecimal(returnSales)+")</div></td>" +
                "</tr>";
       }
       result = result + "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\" colspan =\"4\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][11]+"</div></td>";

       if(cash>0 && cashQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">5.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"right\">"+textListMaterialHeaderSummaryTransaction[language][9]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cashTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cashQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(cash)+"</div></td>" +
                "</tr>";
       }
       if(creditSales>0 && creditSalesQty>0){
          result = result +"<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">6.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"right\">"+textListMaterialHeaderSummaryTransaction[language][10]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+creditSalesTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+creditSalesQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(creditSales)+"</div></td>" +
                "</tr>";
       }
       result = result +"<trborder=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\"></td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><b>TOTAL"+textListMaterialHeaderSummaryTransaction[language][11]+" </b></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\"><b>"+totalTransaksi+"<b></div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\"><b>"+totalQty+"<b></div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalSummary)+"</b></div></td>" +
                "</tr>" +
                //"</tr></table></tr>" ;
                "</tr>" +
                "</table></td></tr></table>";
     return result;
}

public String generateGrandSummaryTransaction2(int language,String whereCashCashier, String whereCashCashierBm, String whereCashierCb, String whereCpm){
    String result = "";
    double openBill=0;
    double cancel=0;
    double bayarKredit=0;
    double returnSales=0;
    double cash=0;
    double creditSales=0;
    double totalSummary=0;
    String where = "";

    double openBillQty=0;
    double cancelQty=0;
    double bayarKreditQty=0;
    double returnSalesQty=0;
    double cashQty=0;
    double creditSalesQty=0;
    double totalQty=0;

    double openBillTransaksi=0;
    double cancelTransaksi=0;
    double bayarKreditTransaksi=0;
    double returnSalesTransaksi=0;
    double cashTransaksi=0;
    double creditSalesTransaksi=0;
    double totalTransaksi=0;

    CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
    String defaultCurrency = currencyType.getCode();

    //summary
    openBill = PstBillMain.getSummaryOpenBill(0,whereCashCashierBm);
    cancel = PstBillMain.getSummaryCancel(0,whereCashCashierBm);
    bayarKredit = PstCashCreditPayment.getSummaryBayarCredit(0,whereCpm);
    returnSales = PstBillMain.getSummaryReturn(0,whereCashCashierBm);
    cash = PstBillMain.getSummaryCash(0,whereCashCashierBm);
    creditSales = PstBillMain.getSummarySalesCredit(0,whereCashCashierBm);
    totalSummary = cash + creditSales-returnSales;

    //Qty
    openBillQty = PstBillMain.getCountQtySummaryOpenBill(0,whereCashCashierBm);
    cancelQty = PstBillMain.getCountQtySummaryCancel(0,whereCashCashierBm);
    bayarKreditQty = PstCreditPaymentMain.getCountQtySummaryBayarCredit(0, whereCpm);
    returnSalesQty = PstBillMain.getCountQtySummaryReturn(0,whereCashCashierBm);
    cashQty = PstBillMain.getCountQtySummaryCash(0,whereCashCashierBm);
    creditSalesQty = PstBillMain.getCountQtySummarySalesCredit(0,whereCashCashierBm);
    totalQty = cashQty + creditSalesQty;

    //transaksi
    openBillTransaksi= PstBillMain.getCountTransSummaryOpenBill(0,whereCashCashierBm);
    cancelTransaksi=PstBillMain.getCountTransSummaryCancel(0,whereCashCashierBm);
    bayarKreditTransaksi=PstCreditPaymentMain.getCountTransSummaryBayarCredit(0,whereCpm);
    returnSalesTransaksi=PstBillMain.getCountTransSummaryReturn(0,whereCashCashierBm);
    cashTransaksi=PstBillMain.getCountTransSummaryCash(0,whereCashCashierBm);
    creditSalesTransaksi=PstBillMain.getCountTransSummarySalesCredit(0,whereCashCashierBm);
    totalTransaksi= cashTransaksi+creditSalesTransaksi;


    
 result = //"<tr><table width=\"50%\" >"
                //"<tr>" +
                "<table width =\"50%\"><tr><td><table width=\"50%\" border=\"0\" bordercolor=\"FFFFFF\" class=\"listgen\"> " +
                "<tr>" +
		"<td width=\"4%\" class=\"listgentitle\" colspan =\"4\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][1]+"</div></td></tr>" +
                "<tr>" +
                //"<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][0]+"</td>" +
		"<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][2]+"</td>" +
                "<td width=\"5%\" class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][2]+"</td>" +
                "<td width=\"5%\" class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][3]+"</td>" +
                "<td class=\"listgentitle\">"+textListMaterialHeaderSummaryTransaction[language][4]+"</td>" +
		"</tr>";
       if(openBill>0 && openBillQty>0){

        result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">1.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][5]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+openBillTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+openBillQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(openBill)+"</div></td>" +
                "</tr>";
       }
       if(cancel>0 && cancelQty>0){
         result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">2.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][6]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cancelTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cancelQty+"</div></td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(cancel)+"</div></td>" +
                "</tr>";
       }
       if(bayarKredit>0 && bayarKreditQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">3.</td>" +
                "<td width=\"10%\" class=\"listgensell\" nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][7]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+bayarKreditTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+bayarKreditQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(bayarKredit)+"</div></td>" +
                "</tr>";
       }
       if(returnSales>0 && returnSalesQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">4.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][8]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+returnSalesTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+returnSalesQty+"</div></td>" +
                "<td width=\"20%\"class=\"listgensell\"><div align=\"right\">("+FRMHandler.userFormatStringDecimal(returnSales)+")</div></td>" +
                "</tr>";
       }
       result = result + "<tr>" +
                "<td width=\"5%\" class=\"listgentitle\" colspan =\"4\"><div align=\"left\">"+textListMaterialHeaderSummaryTransaction[language][11]+"</div></td>";

       if(cash>0 && cashQty>0){
          result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">5.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"right\">"+textListMaterialHeaderSummaryTransaction[language][9]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cashTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+cashQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(cash)+"</div></td>" +
                "</tr>";
       }
       if(creditSales>0 && creditSalesQty>0){
          result = result +"<tr border=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\">6.</td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><div align=\"right\">"+textListMaterialHeaderSummaryTransaction[language][10]+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+creditSalesTransaksi+"</div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\">"+creditSalesQty+"</div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\">"+FRMHandler.userFormatStringDecimal(creditSales)+"</div></td>" +
                "</tr>";
       }
       result = result +"<trborder=\"1\" bordercolor=\"FFFFFF\">" +
                //"<td width=\"5%\" class=\"listgentitle\"></td>" +
                "<td width=\"10%\" class=\"listgensell\"nowrap width=\"95\"><b>TOTAL"+textListMaterialHeaderSummaryTransaction[language][11]+" </b></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\"><b>"+totalTransaksi+"<b></div></td>" +
                "<td width=\"24%\" class=\"listgensell\"><div align=\"right\"><b>"+totalQty+"<b></div></td>" +
                "<td width=\"20%\" class=\"listgensell\"><div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalSummary)+"</b></div></td>" +
                "</tr>" +
                //"</tr></table></tr>" ;
                "</tr>" +
                "</table></td></tr></table>";
     return result;
}
//ListPayment
//(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType)
public String drawListDetailPayment(int language, Vector objectClass, int start, long currencyId, long oidCashCashier, int iSaleReportType) {

  String result = "";
    if(objectClass!=null && objectClass.size()>0) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");

    ctrlist.dataFormat(textListMaterialHeaderDetail[language][0],"3%","0","0","center","bottom"); //no
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][1],"7%","0","0","center","bottom"); //tipe
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][2],"8%","0","0","center","bottom"); //currency
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][3],"15%","0","0","center","bottom"); //jumlah
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][4],"15%","0","0","center","bottom"); //total
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][5],"15%","0","0","center","bottom");

    ctrlist.setLinkRow(-1);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData = ctrlist.getLinkData();
    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset();
    Vector rowx = new Vector();
    //int index = -1;

    if(start<0) start = 0;
    String where = "";
    String whereSaleType = "";
    double rate = 0;
    double total = 0.0;
    double totalPayment = 0.0;
    double totalReturn = 0.0;
    double totalAll= 0.0;

    where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+oidCashCashier;
     if(iSaleReportType == PstBillMain.TRANS_TYPE_CASH){
	            where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
                 //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH;
                 //}else if (iSaleReportType == PstCashPayment.CARD){
               }else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT){
                  //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_OPEN;
                 //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANS_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT;
	        //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
               where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
             }
             else if (iSaleReportType ==-1){
               where = where;
              //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"!= 1";
             }

    

    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        CashPayments cashPayment = (CashPayments)temp.get(0);
        CurrencyType currencyType = (CurrencyType)temp.get(1);
        //BillMain billMain = (BillMain)temp.get(2);
        CashReturn cashReturn = (CashReturn)temp.get(2);

         //CashReturn cashReturn = new CashReturn();
       
         
	 
        
       // where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+oidCashCashier;
        //add opie 18-06-2012
        double PaymentReturn=0;
        
        //update opie 18-06-2012k detail pembayaran - return
        double amountPaymentAll = 0.0;
        if(cashPayment.getPaymentType()!= 6){
            //amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount();
            if(cashPayment.getPaymentType()==0){
                 PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier,0);
            }  //cash
            else if(cashPayment.getPaymentType()==1) {
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,1);
            } //kartu kredit
            else if (cashPayment.getPaymentType()==2){
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,2);
            } //check
            else if (cashPayment.getPaymentType()==3) {
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,3);
            }//debit
            amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount() - PaymentReturn;
       }
        else {
         amountPaymentAll = cashPayment.getAmount();
       }

        //totalPayment = PstCashPayment.getSumListDetailBayar(where);
        //totalReturn = PstCashReturn.getDetailPaymentReturn(where);
        //total = totalPayment - totalReturn;
        total+=amountPaymentAll;

        rowx = new Vector();

        //start = start + 1;

        //rowx.add(""+start+"");
        rowx.add("<div align=\"left\">"+(start+i+1)+"</div>");
        rowx.add("<div align=\"right\">" +PstCashPayment.paymentType[cashPayment.getPaymentType()]);
        rowx.add(currencyType.getCode());
        rowx.add("<div align=\"right\">" +cashPayment.getRate());
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(amountPaymentAll)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(amountPaymentAll)+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(cashPayment.getAmount())+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(cashPayment.getAmount())+"</b></div>");
        //totalAll = +amountPaymentAll;

        lstData.add(rowx);

        

     }
        rowx = new Vector();
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalAll)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total)+"</b></div>");
        lstData.add(rowx);
        result = ctrlist.draw();
    }
    return result;
}

//update opie-eyek untuk payment dinamis 10022013
public String drawListDetailPaymentDinamis(int language, Vector objectClass, int start, long currencyId, long oidCashCashier, int iSaleReportType, String whereReturnBill) {

  String result = "";
    if(objectClass!=null && objectClass.size()>0) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");

    ctrlist.dataFormat(textListMaterialHeaderDetail[language][0],"3%","0","0","center","bottom"); //no
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][1],"7%","0","0","center","bottom"); //tipe
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][2],"8%","0","0","center","bottom"); //currency
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][3],"15%","0","0","center","bottom"); //jumlah
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][4],"15%","0","0","center","bottom"); //total
    ctrlist.dataFormat(textListMaterialHeaderDetail[language][5],"15%","0","0","center","bottom");

    ctrlist.setLinkRow(-1);
    ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    Vector lstLinkData = ctrlist.getLinkData();
    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset();
    Vector rowx = new Vector();
    //int index = -1;

    if(start<0) start = 0;
    String where = "";
    String whereSaleType = "";
    double rate = 0;
    double total = 0.0;
    double totalPayment = 0.0;
    double totalReturn = 0.0;
    double totalAll= 0.0;

    where = " CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+oidCashCashier;
     if(iSaleReportType == PstBillMain.TRANS_TYPE_CASH){
	            where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
               }else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT){
               where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
             }
             else if (iSaleReportType ==-1){
               where = where;
             }

          Vector vCurrType = PstCurrencyType.list(0,0,"","");
		Hashtable hashCurrType = new Hashtable();
		if(vCurrType!=null && vCurrType.size()>0){
			int iCurrTypeCount = vCurrType.size();
			for(int k=0; k<iCurrTypeCount; k++){
				CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(k);
				hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
			}
		}
         

    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        CashPayments1 cashPayment = (CashPayments1) temp.get(0);
        CurrencyType currencyType = (CurrencyType)temp.get(1);
        CashReturn cashReturn = (CashReturn)temp.get(2);

        String strPaymentType = PstPaymentSystem.getTypePayment(cashPayment.getPaymentType());
        
        //add opie 18-06-2012
        double PaymentReturn=0;
        double paymentCashBillReturn = SessPaymentBillReturn.getPaymentDinamisWithReturnTransaction(0,0,whereReturnBill+" AND CP.PAY_TYPE='"+cashPayment.getPaymentType()+"' ");
        //update opie 18-06-2012k detail pembayaran - return
        double amountPaymentAll = 0.0;
        double dPaymentPerType  = 0.0;
        double amounPayment=0.0;
        //String strPaymentType = PstPaymentSystem.getTypePayment(objCashPayment.getPaymentType());
        String strNumber = String.valueOf(i+1);
        String strCurrency = String.valueOf(hashCurrType.get(""+cashPayment.getCurrencyId()));
        String strAmount = FRMHandler.userFormatStringDecimal( (cashPayment.getAmount()));
        String strRate = FRMHandler.userFormatStringDecimal(cashPayment.getRate());
        String strTotal = FRMHandler.userFormatStringDecimal(cashPayment.getAmount() * cashPayment.getRate());
        dPaymentPerType = ((cashPayment.getAmount() * cashPayment.getRate()));
        amounPayment= dPaymentPerType-cashReturn.getAmount()-paymentCashBillReturn;
        total=total+amounPayment;
        /* if(cashPayment.getPaymentType()!= 6){
            //amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount();
            if(cashPayment.getPaymentType()==0){
                 PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier,0);
            }  //cash
            else if(cashPayment.getPaymentType()==1) {
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,1);
            } //kartu kredit
            else if (cashPayment.getPaymentType()==2){
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,2);
            } //check
            else if (cashPayment.getPaymentType()==3) {
                 PaymentReturn= PstCashPayment.getPembayaranReturn(oidCashCashier,3);
            }//debit
            amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount() - PaymentReturn;
       }
        else {
         amountPaymentAll = cashPayment.getAmount();
       }*/

       // totalPayment = PstCashPayment.getSumListDetailBayar(where);
       // totalReturn = PstCashReturn.getDetailPaymentReturn(where);
        //dPaymentPerType=dPaymentPerType-totalReturn;
        //total = amountPaymentAll - totalReturn;
        //total+=amountPaymentAll;

        rowx = new Vector();

        //start = start + 1;

        //rowx.add(""+start+"");
        rowx.add("<div align=\"left\">"+(start+i+1)+"</div>");
        rowx.add("<div align=\"right\">" +strPaymentType);
        rowx.add(currencyType.getCode());
        rowx.add("<div align=\"right\">" +cashPayment.getRate());
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(amounPayment)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(amounPayment)+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(cashPayment.getAmount())+"</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(cashPayment.getAmount())+"</b></div>");
        //totalAll = +amountPaymentAll;

        lstData.add(rowx);



     }
        rowx = new Vector();
        rowx.add("<div align=\"left\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        //rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalAll)+"</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total)+"</b></div>");
        lstData.add(rowx);
        result = ctrlist.draw();
    }
    return result;
}



/*public String getTotalPayment(Vector vListPayment) {
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
long cashCashierId = FRMQueryString.requestLong(request, "hidden_cash_cashier_Id");
int recordToGet = 100;
int iViewType = FRMQueryString.requestInt(request, "view_type");

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);
int vectSize = 0;
int vectSize1 = 0;

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

//where for filter perCashOpening
String whereOpening = PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'";
//String whereOpening = PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
			//" AND CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE+
			//" AND CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+iSaleReportType;
  //if(iSaleReportType == PstBillMain.TRANS_TYPE_CASH){
	       //whereOpening = whereOpening  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE +
                      // " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CASH +
                      // " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
             //}else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT){
             //whereOpening = whereOpening  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED +
                      // " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT +
                      // " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"= 0";
          // }
          // else if (iSaleReportType ==-1){
             //where = where;
              // whereOpening = whereOpening  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"!= 1";
          // }

//if(iSaleReportType == PstCashPayment.CASH){
	//whereOpening = whereOpening  + " AND CBM. "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
//}else{
	//whereOpening = whereOpening  + " AND CBM."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!="+PstBillMain.TRANS_STATUS_DELETED;
//}


if(srcSaleReport.getShiftId()!=0) {
          whereOpening = whereOpening + " AND CSH."+PstCashCashier.fieldNames[PstCashCashier.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

if(srcSaleReport.getLocationId()!=0) {
        whereOpening = whereOpening + " AND MSTR."+PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}

//+perCashier
if (srcSaleReport.getCashMasterId()!=0) {
          whereOpening = whereOpening + " AND MSTR." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
 }

//order for filter perCashOpening
//String orderOpening = PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE];
  String orderOpening = " LOC."+PstLocation.fieldNames[PstLocation.FLD_CODE];

//if(srcSaleReport.getLocationId()!=0) {
        //orderOpening = orderOpening + " ,LOC."+PstLocation.fieldNames[PstLocation.FLD_CODE];
        orderOpening = orderOpening + " ,"+PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE];
//}

//+perCashier
//if (srcSaleReport.getCashMasterId()!=0) {
          orderOpening = orderOpening + " ,MSTR." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASHIER_NUMBER];
 //}

//vectSize = PstBillMain.getCount(where);
//getCout perCashOpening
  vectSize1 = PstCashCashier.getCountPerCashOpening(whereOpening);
//getCount +PerCashier
  //vectSize = PstBillMain.getCountPerCashier(where);
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
Vector recordList = PstCashCashier.listCashOpening(0, 0, whereOpening, orderOpening);
//+List Per Cashier
 //Vector records = PstBillMain.listPerCashier(0, 0, where, "");

//Vector vResult = new Vector();
//if(records!=null && records.size()>0 && iSaleReportType == PstCashPayment.CASH){
    //PstCashPayment objPstCashPayment = new PstCashPayment();
    //vResult = objPstCashPayment.getListPayment(srcSaleReport,PstBillMain.TYPE_INVOICE, iSaleReportType);
   // String strTemp = drawListPayment(vResult);
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
	document.frm_reportsale.action="invoice_edit_1.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="report_list_cash_opening_1.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="report_list_cash_opening_1.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="report_list_cash_opening_1.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="report_list_cash_opening_1.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_report_cash_opening.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}
function cmdExportExcel(){
    window.open("report_list_cash_opening_1_excel.jsp");
}

function cmdExportHtml(typeView){
    if ("<%=typeOfBusinessDetail%>" === "2") {
        window.open("report_cash_opening.jsp?view_report="+typeView+"&sale_type="+<%=iSaleReportType%>+"");
    } else {
        window.open("report_list_cash_opening_1_html.jsp?view_report="+typeView+"&sale_type="+<%=iSaleReportType%>+"");
    }
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
              <input type="hidden" name="hidden_cash_cashier_Id" value="<%=cashCashierId%>">
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
                 
                    <!--<td height="22" valign="middle" colspan="3"><b>Tipe : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : "Penjualan Kredit"%></b></td>-->
                    <td height="22" valign="middle" colspan="3"><b>Tipe :
                            
                    <%if(iSaleReportType==PstBillMain.TRANS_TYPE_CASH){%>
                         <%= "Penjualan Cash"%>
                     <% } else if (iSaleReportType==PstBillMain.TRANS_TYPE_CREDIT) { %>
                         <%= "Penjualan Kredit"%>
                     <% } else if (iSaleReportType==-1){ %>
                         <%= "Semua Tipe Penjualan"%>
                     <%}%>
                    </b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][9]%> : <%=cashier%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=drawList(out,SESS_LANGUAGE, recordList, start, srcSaleReport, srcSaleReport.getCurrencyOid(), iSaleReportType, iViewType)%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                    <%
                    Vector recordListOnlyGetSales = PstCashCashier.listCashOpeningOnlyGetSales(0, 0, whereOpening, orderOpening);    
                        
                    if(recordListOnlyGetSales.size()>0){
                        
                        Vector listDetailPayment = new Vector();
                        int paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));

                        CashCashier cashCashier = new CashCashier();
                        String whereCashCashier="";
                        String whereCashCashier2="";
                        String whereCashCashier3="";
                        String whereCashCashier4="";
                        String whereCashCashier5="";
                        for(int i=0; i<recordListOnlyGetSales.size(); i++) {
                                Vector temp = (Vector)recordListOnlyGetSales.get(i);
                                cashCashier = (CashCashier)temp.get(0);
                                if(i==0){
                                    whereCashCashier= ""+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier2="CBM."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier3="CB."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier4="CPM."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier5="CP."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                }else{
                                    whereCashCashier=whereCashCashier+" OR "+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier2=whereCashCashier2+" OR CBM."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier3=whereCashCashier3+" OR CB."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier4=whereCashCashier4+" OR CPM."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                    whereCashCashier5=whereCashCashier5+" OR CP."+PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID]+"='"+cashCashier.getOID()+"'";
                                }
                        }
                        String whereTo=" ( "+whereCashCashier+" ) ";
                        String whereBm=" ( "+whereCashCashier2+" )";
                        String whereCb=" ( "+whereCashCashier3+" )";
                        String whereCpm = " ( "+whereCashCashier4+" )";
                        String whereCp = " ( "+whereCashCashier5+" )";

                        if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                            where = where+" AND "+whereBm;
                            listDetailPayment = PstCashPayment1.listDetailPaymentDinamisWithReturn(0, 0, whereBm);
                        }else{
                            where = where+" AND "+whereBm;
                            listDetailPayment = PstCashPayment.listDetailPaymentWithReturn(0, 0, whereBm);
                        }

                    %>
                        <td height="22" valign="middle" colspan="3">
                                <table class="listgensell">
                                    <tr>
                                        <td rowspan ="3"><%=generateGrandTotSummaryCash(whereTo , whereBm, whereCb,whereCpm)%></td>
                                        <td><b>DETAIL GRAND TOTAL PEMBAYARAN</b>
                                            <%=drawListDetailPaymentDinamis(SESS_LANGUAGE, listDetailPayment, start, srcSaleReport.getCurrencyOid(), cashCashier.getOID(), iSaleReportType,""+whereBm)%>
                                        </td>
                                        <td><%=generateGrandSummaryTransaction2(SESS_LANGUAGE, whereTo , whereBm, whereCb,whereCpm)%></td>
                                    </tr>
                                </table>
                        </td>
                    <%}%> 
                </tr>							
                <tr align="left" valign="top"> 
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
                <script type="text/javascript">
                    
                </script>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="45%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                              <td nowrap width="5%"><a href="javascript:cmdExportExcel()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnPrint.gif" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="45%"><a href="javascript:cmdExportExcel()">Export Excel</a></td>
                              <td><input type="button" value="Print Report" onclick="cmdExportHtml(<%= iViewType%>)"></td>
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