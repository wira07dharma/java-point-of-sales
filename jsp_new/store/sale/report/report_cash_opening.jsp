<%-- 
    Document   : report_cash_opening
    Created on : Mar 20, 2018, 2:21:18 PM
    Author     : Dimata 007
--%>

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

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListMaterialHeaderFilter[][] = {
        {"No", "Lokasi", "Tanggal Opening", "Shift", "Kasir No.", "Invoice", "Tidak ada data transaksi", "TRANSAKSI RETURN ", "TRANSAKSI BATAL", "TRANSAKSI PENJUALAN TUNAI", "TRANSAKSI PENJUALAN KREDIT"},
        {"No", "Location", "Opening Date", "Shift", "No. Cashier", "Invoice", "No transaction data available", "RETURN TRANSACTION", "CANCEL TRANSACTION", "CASH SALES TRANSACTION", "CREDIT SALES TRANSACTION"}
    };

    public static final String textListMaterialHeader[][] = {
        {"NO", "TGL SETTLE", "NOMOR", "KONSUMEN", "SALES BRUTO", "DISC", "TAX", "SERVICE", "SALES NETTO", "DP DEDUCTION", "HPP", "CATATAN", "WAKTU SETTLE", "NOMOR TAGIHAN", "TGL PRINT ", "WAKTU PRINT"},
        {"NO", "DATE SETTLE", "NUMBER", "CUSTOMER", "SALES BRUTO", "DISC", "TAX", "SERVICE", "SALES NETTO", "DP DEDUCTION", "COGS", "REMARK", "TIME SETTLE", "COVER BILL NUMBER", "DATE PRINT", "TIME PRINT"}
    };

    public static final String textListTitleHeader[][] = {
        {"Laporan Rekap Penjualan Harian", "LAPORAN REKAP PENJUALAN PER SHIFT", "Tidak ada data transaksi ..", "Lokasi", "SHIFT", "Laporan", "Cetak Transaksi Harian", "TIPE", "Mata Uang", "Kasir"},
        {"Daily Sales Recapitulation Report", "SALES RECAPITULATION REPORT PER SHIFT", "No transaction data available ..", "LOCATION", "SHIFT", "Laporan", "Print Daily Transaction ", "TYPE", "Currency Type", "Cashier"}
    };

    public static final String textListMaterialHeaderSummaryCash[][] = {
        {"NO.", "RANGKUMAN KAS ", "TRANSAKSI", "MATA UANG", "NILAI", "QTY", "TOTAL", "PENJUALAN"},
        {"NO.", "SUMMARY", "TRANSACTION", "CURRENCY", "AMOUNT", "QTY", "TOTAL", "SALES"}
    };

    public static final String textListMaterialHeaderDetail[][] = {
        {"NO.", "TIPE", "MATA UANG", "RATE", "JUMLAH", "TOTAL"},
        {"NO.", "TYPE", "CURRENCY", "RATE", "SUMMARY", "TOTAL"}
    };

    public static final String textListMaterialHeaderSummaryTransaction[][] = {
        {"No", "RANGKUMAN TRANSAKSI ", "TRANSAKSI", "QTY", "NILAI", "Open Bill", "Batal",
            "Bayar Kredit", "Retur Penjualan", "Tunai", "Kredit", "PENJUALAN"},
        {"No", "SUMMARY TRANSACTION", "TRANSACTION", "QTY", "VALUE", "Open Bill", "Cancel",
            "Pay Credit", "Sales Return", "Cash", "Credit", "SALES"}
    };
%>

<%!
    public String drawList(JspWriter outObj, int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId, int iSaleReportType, int iViewType) {
        String result = "";
        int paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("table table-bordered");
            ctrlist.setTitleStyle("");
            ctrlist.setCellStyle("td1");
            ctrlist.setHeaderStyle("");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("th1");
            ctrlist.setBorder(0);
            ctrlist.dataFormat(textListMaterialHeaderFilter[language][0], "3%", "0", "0", "center", "bottom"); //no
            ctrlist.dataFormat("Data", "97%", "0", "0", "center", "bottom"); // data

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector();

            ctrlist.setLinkPrefix("");
            ctrlist.setLinkSufix("");
            ctrlist.reset();

            String where = "";
            String whereBillMain = "";
            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                CashCashier cashCashier = (CashCashier) temp.get(0);
                CashMaster cashMaster = (CashMaster) temp.get(1);
                Location location2 = (Location) temp.get(2);
                Shift shift = (Shift) temp.get(3);

                rowx = new Vector();
                rowx.add("<div align=\"center\">" + (start + i + 1) + "</div>");
                rowx.add("<div align=\"left\"><b>" + textListMaterialHeaderFilter[language][1] + "</b> : " + location2.getName() + ""
                        + "&nbsp;&nbsp;<b>" + textListMaterialHeaderFilter[language][2] + "</b> : " + Formater.formatDate(cashCashier.getCashDate(), "dd/MM/yyyy HH:mm:ss") + ""
                        + "&nbsp;&nbsp;<b>" + textListMaterialHeaderFilter[language][3] + "</b> : " + shift.getName() + ""
                        + "&nbsp;&nbsp;<b>" + textListMaterialHeaderFilter[language][4] + "</b> : " + cashMaster.getCashierNumber() + ""
                        + "</div>");
                lstData.add(rowx);
                
                rowx = new Vector();
                //Tipe pilihan baru
                where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + cashCashier.getOID();
                if (iSaleReportType == PstBillMain.TRANS_TYPE_CASH) {
                    whereBillMain = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
                } else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT) {
                    whereBillMain = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
                } else if (iSaleReportType == -1) {
                    whereBillMain = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
                }

                //add opie 19-06-2012 agar no invoice transaksi sesuai dengan jam tidak loncat2
                String order = "";
                order = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " ASC, " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
                Vector list = PstBillMain.listPerCashier(0, 0, whereBillMain, order);
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<p><b>" + textListMaterialHeaderFilter[language][9] + "</b></p>" + drawListInvoice(language, list, srcSaleReport, start, currencyId, iViewType));
                lstData.add(rowx);

                if (iSaleReportType == -1) {
                    
                    rowx = new Vector();
                    //Tipe pilihan baru
                    where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + cashCashier.getOID();
                    whereBillMain = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT
                            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";

                    String orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " ASC, " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
                    orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                    Vector listReturn = PstBillMain.listPerCashier(0, 0, whereBillMain, orderReturn);
                    rowx.add("<div align=\"left\"></div>");
                    rowx.add("<p><b>" + textListMaterialHeaderFilter[language][10] + "</b></p>" + drawListInvoice(language, listReturn, srcSaleReport, start, currencyId, iViewType));
                    lstData.add(rowx);
                }

                rowx = new Vector();
                //Tipe pilihan baru
                where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + cashCashier.getOID();
                whereBillMain = where + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=1"
                        + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                        + " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ") "
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE + ")";

                String orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " ASC, " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
                orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                Vector listReturn = PstBillMain.listPerCashier(0, 0, whereBillMain, orderReturn);
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<p><b>" + textListMaterialHeaderFilter[language][7] + "</b></p>" + drawListInvoice(language, listReturn, srcSaleReport, start, currencyId, iViewType));
                lstData.add(rowx);

                rowx = new Vector();
                //Tipe pilihan baru
                where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + cashCashier.getOID();
                whereBillMain = where + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0"
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_DELETED + ")";

                String orderCancel = PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " ASC, " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " ASC";
                orderReturn = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
                Vector listCancel = PstBillMain.listPerCashier(0, 0, whereBillMain, orderCancel);
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<p><b>" + textListMaterialHeaderFilter[language][8] + "</b></p>" + drawListInvoice(language, listCancel, srcSaleReport, start, currencyId, iViewType));
                lstData.add(rowx);
                
                //Transaction summary dipisah
                rowx = new Vector();
                Vector listDetailPayment = new Vector();
                if (paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                    listDetailPayment = PstCashPayment1.listDetailPaymentDinamisWithReturn(0, 0, where);
                    rowx.add("<div align=\"left\"></div>");
                    if (list != null && list.size() > 0) {                        
                        rowx.add("<div class='row'>"
                                + "<div class='col-sm-4'>"+ generateSummaryCash(cashCashier.getOID()) + "</div>"
                                + "<div class='col-sm-4'><p><b>DETAIL PEMBAYARAN<b></p>" + drawListDetailPaymentDinamis(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType) + "</div>"
                                + "<div class='col-sm-4'>" + generateSummaryTransaction2(language, cashCashier.getOID()) + "</div>"
                                + "</div>");
                    } else {
                        rowx.add("<table border=\"\" class=\"table table-bordered\"><tr><td><div align=\"center\">" + textListMaterialHeaderFilter[language][6] + "</div></td></tr></table>");
                    }
                } else {
                    listDetailPayment = PstCashPayment.listDetailPaymentWithReturn(0, 0, where);
                    rowx.add("<div align=\"left\"></div>");
                    if (list != null && list.size() > 0) {
                        rowx.add("<div class='row'>"
                                + "<div class='col-sm-4'>" + generateSummaryCash(cashCashier.getOID()) + "</div>"
                                + "<div class='col-sm-4'><p><b>DETAIL PEMBAYARAN</b></p>" + drawListDetailPayment(language, listDetailPayment, start, currencyId, cashCashier.getOID(), iSaleReportType) + "</div>"
                                + "<div class='col-sm-4'>" + generateSummaryTransaction2(language, cashCashier.getOID()) + "</div>"
                                + "</div>");
                    } else {
                        rowx.add("<table border=\"\" class=\"table table-bordered\"><tr><td><div align=\"center\">" + textListMaterialHeaderFilter[language][6] + "</div></td></tr></table>");
                    }
                }
                lstData.add(rowx);
            }

            ctrlist.draw(outObj);
        }
        return "";
    }

%>

<%!    public String drawListInvoice(int language, Vector objectClass, SrcSaleReport srcSaleReport, int start, long currencyId, int iViewType) {
        String result = "";
        String frmCurrency = "#,###";
        String frmCurrencyUsd = "#,###.##";
        String useCoverNumber = PstSystemProperty.getValueByName("CASHIER_USING_COVER_NUMBER");
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("table table-bordered");
            ctrlist.setTitleStyle("");
            ctrlist.setCellStyle("td2");
            ctrlist.setHeaderStyle("");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("th2");
            ctrlist.setBorder(0);
            ctrlist.dataFormat(textListMaterialHeader[language][0], "3%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][2], "7%", "0", "0", "center", "bottom");
            if (useCoverNumber.equals("1")) {
                ctrlist.dataFormat(textListMaterialHeader[language][13], "7%", "0", "0", "center", "bottom");
            }
            ctrlist.dataFormat(textListMaterialHeader[language][14], "5%", "0", "0", "center", "bottom"); //tanggal pertama di print
            ctrlist.dataFormat(textListMaterialHeader[language][15], "5%", "0", "0", "center", "bottom"); //tanggal pertama di print

            ctrlist.dataFormat(textListMaterialHeader[language][1], "5%", "0", "0", "center", "bottom");//tanggal
            ctrlist.dataFormat(textListMaterialHeader[language][12], "5%", "0", "0", "center", "bottom");//waktu

            ctrlist.dataFormat(textListMaterialHeader[language][3], "9%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][11], "7%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][8], "11%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][5], "10%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][6], "10%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][7], "10%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][4], "10%", "0", "0", "center", "bottom");
            ctrlist.dataFormat(textListMaterialHeader[language][9], "10%", "0", "0", "center", "bottom");

            ctrlist.setLinkRow(1);
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

            if (iViewType == 0) {
                for (int i = 0; i < objectClass.size(); i++) {
                    BillMain billMain = (BillMain) objectClass.get(i);

                    ContactList contactlist = new ContactList();
                    try {
                        contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                    } catch (Exception e) {
                        System.out.println("Contact not found ...");
                    }
                    rowx = new Vector();
                    rowx.add("<div align=\"center\">" + (start + i + 1) + "</div>");
                    rowx.add("<div align=\"left\">" + billMain.getInvoiceNumber() + "</div>");
                    if (useCoverNumber.equals("1")) {
                        rowx.add("<div align=\"left\">" + billMain.getCoverNumber() + "</div>");
                    }
                    //cek log history
                    String whereLog = PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "='" + billMain.getOID() + "' "
                            + " AND " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + "='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                    Date count = PstLogSysHistory.getDateLog(whereLog);
                    if (count != null) {
                        rowx.add("<div align=\"left\">" + Formater.formatDate(count, "dd/MM/yyyy") + "</div>");
                        rowx.add("<div align=\"left\">" + Formater.formatTimeLocale(count, "kk:mm:ss") + "</div>");
                    } else {
                        rowx.add("<div align=\"left\">-</div>");
                        rowx.add("<div align=\"left\">-</div>");
                    }
                    rowx.add("<div align=\"left\">" + Formater.formatDate(billMain.getBillDate(), "dd/MM/yyyy") + "</div>");
                    rowx.add("<div align=\"left\">" + Formater.formatTimeLocale(billMain.getBillDate(), "kk:mm:ss") + "</div>");

                    rowx.add("<div align=\"left\">" + contactlist.getPersonName() + "</div>");
                    rowx.add("<div align=\"left\">" + billMain.getNotes() + "</div>");
                    if (currencyId != 0) {
                        bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID());
                        diskon = billMain.getDiscount();
                        pajak = billMain.getTaxValue();
                        servis = billMain.getServiceValue();
                    } else {
                        bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID()) * billMain.getRate();
                        diskon = billMain.getDiscount() * billMain.getRate();
                        pajak = billMain.getTaxValue() * billMain.getRate();
                        servis = billMain.getServiceValue() * billMain.getRate();
                    }

                    if (billMain.getDocType() != 1) { // not return }
                        netto = bruto - diskon + pajak + servis;
                        dp = PstPendingOrder.getDpValue(billMain.getOID());
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", bruto) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", diskon) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", pajak) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", servis) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", netto) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", dp) + ".00</div>");
                    } else { //return
                        bruto = -bruto;
                        diskon = -diskon;
                        pajak = -pajak;
                        servis = -servis;
                        netto = bruto - diskon + pajak + servis;
                        dp = -dp;
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", bruto) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", diskon) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", pajak) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", servis) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", netto) + ".00</div>");
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", dp) + ".00</div>");
                    }

                    totalBruto += bruto;
                    totalDisc += diskon;
                    totalTax += pajak;
                    totalService += servis;
                    totalNetto += netto;
                    totalDp += dp;

                    lstData.add(rowx);
                }

                rowx = new Vector();
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                if (useCoverNumber.equals("1")) {
                    rowx.add("<div align=\"left\"></div>");
                }
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalBruto) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalDisc) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalTax) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalService) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalNetto) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalDp) + ".00</b></div>");
                lstData.add(rowx);

                result = ctrlist.draw();
            } else if (iViewType == 1) {
                for (int i = 0; i < objectClass.size(); i++) {
                    BillMain billMain = (BillMain) objectClass.get(i);

                    ContactList contactlist = new ContactList();
                    try {
                        contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                    } catch (Exception e) {
                        System.out.println("Contact not found ...");
                    }
                    if (currencyId != 0) {
                        bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID());
                        diskon = billMain.getDiscount();
                        pajak = billMain.getTaxValue();
                        servis = billMain.getServiceValue();
                    } else {
                        bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID()) * billMain.getRate();
                        diskon = billMain.getDiscount() * billMain.getRate();
                        pajak = billMain.getTaxValue() * billMain.getRate();
                        servis = billMain.getServiceValue() * billMain.getRate();
                    }

                    if (billMain.getDocType() != 1) { // not return }
                        netto = bruto - diskon + pajak + servis;
                        dp = PstPendingOrder.getDpValue(billMain.getOID());
                    } else { //return
                        netto = -bruto + diskon - pajak - servis;
                        bruto = -bruto;
                        dp = -PstPendingOrder.getDpValue(billMain.getOID());
                    }
                    totalBruto += bruto;
                    totalDisc += diskon;
                    totalTax += pajak;
                    totalService += servis;
                    totalNetto += netto;
                    totalDp += dp;
                    lstLinkData.add(String.valueOf(billMain.getOID()));
                }

                rowx = new Vector();
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                if (useCoverNumber.equals("1")) {
                    rowx.add("<div align=\"left\"></div>");
                }
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalBruto) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalDisc) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalTax) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalService) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalNetto) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", totalDp) + ".00</b></div>");
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(0));

                result = ctrlist.draw();
            }
        } else {
            result = "Tidak ada data";
        }
        return result;
    }

%>

<%!//Summary Cash
    public String generateSummaryCash(long oidCashCashier) {
        String result = "";
        double saldoAwal = 0;
        double jualCash = 0;
        double kembalian = 0;
        double jualKotor = 0;
        double returnSales = 0;
        double jualBersih = 0;
        double bayarKredit = 0;
        double saldoAkhir = 0;
        double uangDilaci = 0;
        double selisih = 0;
        String where = "";

        CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
        String defaultCurrency = currencyType.getCode();

        saldoAwal = PstBalance.getSaldoAwal(PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "=" + oidCashCashier);
        jualCash = PstCashPayment.getPembayaranKotorClosing(oidCashCashier);
        kembalian = PstCashReturn.getReturnSummary(oidCashCashier);
        jualKotor = jualCash - kembalian;
        returnSales = PstBillDetail.getReturSales(oidCashCashier);
        jualBersih = jualKotor - returnSales;
        bayarKredit = PstCashCreditPayment.getPembayaranKredit2(oidCashCashier);

        saldoAkhir = PstBalance.getSaldoAkhir(oidCashCashier);
        uangDilaci = PstBalance.getUangDiLaci(oidCashCashier);
        selisih = uangDilaci - saldoAkhir;

        result
                = "<p><b>SUMMARY CASH</b></p>"
                + "<table width =\"100%\"><tr><td><table width=\"50%\" border=\"1\"  class=\"table table-bordered\"> "
                + "<tr>"
                //+ "<td width=\"4%\" class=\"\" colspan =\"4\" border=\"1\" ><div align=\"left\"><b>SUMMARY CASH</b></div></td></tr>"
                + "<tr><td class=\"\" border=\"1\" >NO</td>"
                + "<td class=\"\"border=\"1\" >TRANSAKSI</td>"
                + "<td width=\"5%\" class=\"\"border=\"1\" >CURRENCY</td>"
                + "<td class=\"\" border=\"1\" >AMOUNT</td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\"border=\"1\" >1.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\" border=\"1\" ><div align=\"left\">Saldo Awal</div></td>"
                + "<td width=\"24%\" class=\"\" border=\"1\" >" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\" border=\"1\" ><div align=\"right\">" + String.format("%,.0f", saldoAwal) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">2.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">Penjualan Kotor</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", jualKotor) + ".00</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">3.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">Retur Sales</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">(" + String.format("%,.0f", returnSales) + ".00)</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">4.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Penjualan Bersih</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", jualBersih) + ".00</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">5.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Bayar Kredit</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", bayarKredit) + ".00</div></td>"
                + "</tr>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">6.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Saldo Akhir</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", saldoAkhir) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">7.</td>"
                + "<td width=\"20%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Uang di laci</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", uangDilaci) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">8.</td>"
                + "<td width=\"20%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Selisih</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\" ><div align=\"right\">" + String.format("%,.0f", selisih) + ".00</div></td>"
                + "</tr>"
                + "</table></td></tr></table>";

        return result;
    }
%>

<%! //Summary Transaction Terpisah

    public String generateSummaryTransaction2(int language, long oidCashCashier) {
        String result = "";
        double openBill = 0;
        double cancel = 0;
        double bayarKredit = 0;
        double returnSales = 0;
        double cash = 0;
        double creditSales = 0;
        double totalSummary = 0;
        String where = "";

        double openBillQty = 0;
        double cancelQty = 0;
        double bayarKreditQty = 0;
        double returnSalesQty = 0;
        double cashQty = 0;
        double creditSalesQty = 0;
        double totalQty = 0;

        double openBillTransaksi = 0;
        double cancelTransaksi = 0;
        double bayarKreditTransaksi = 0;
        double returnSalesTransaksi = 0;
        double cashTransaksi = 0;
        double creditSalesTransaksi = 0;
        double totalTransaksi = 0;

        CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
        String defaultCurrency = currencyType.getCode();

        //summary
        openBill = PstBillMain.getSummaryOpenBill(oidCashCashier);
        cancel = PstBillMain.getSummaryCancel(oidCashCashier);
        bayarKredit = PstCashCreditPayment.getSummaryBayarCredit(oidCashCashier);
        returnSales = PstBillMain.getSummaryReturn(oidCashCashier);
        cash = PstBillMain.getSummaryCash(oidCashCashier);
        creditSales = PstBillMain.getSummarySalesCredit(oidCashCashier);
        totalSummary = cash + creditSales;

        //Qty
        openBillQty = PstBillMain.getCountQtySummaryOpenBill(oidCashCashier);
        cancelQty = PstBillMain.getCountQtySummaryCancel(oidCashCashier);
        bayarKreditQty = PstCreditPaymentMain.getCountQtySummaryBayarCredit(oidCashCashier);
        returnSalesQty = PstBillMain.getCountQtySummaryReturn(oidCashCashier);
        cashQty = PstBillMain.getCountQtySummaryCash(oidCashCashier);
        creditSalesQty = PstBillMain.getCountQtySummarySalesCredit(oidCashCashier);
        totalQty = cashQty + creditSalesQty;

        //transaksi
        openBillTransaksi = PstBillMain.getCountTransSummaryOpenBill(oidCashCashier);
        cancelTransaksi = PstBillMain.getCountTransSummaryCancel(oidCashCashier);
        bayarKreditTransaksi = PstCreditPaymentMain.getCountTransSummaryBayarCredit(oidCashCashier);
        returnSalesTransaksi = PstBillMain.getCountTransSummaryReturn(oidCashCashier);
        cashTransaksi = PstBillMain.getCountTransSummaryCash(oidCashCashier);
        creditSalesTransaksi = PstBillMain.getCountTransSummarySalesCredit(oidCashCashier);
        totalTransaksi = cashTransaksi + creditSalesTransaksi;

        result
                = "<p><b>" + textListMaterialHeaderSummaryTransaction[language][1] + "</b></p>"
                + "<table width =\"100%\"><tr><td><table width=\"50%\" border=\"1\" bordercolor=\"FFFFFF\" class=\"table table-bordered\"> "
                + "<tr>"
                //+ "<td width=\"4%\" class=\"\" colspan =\"4\"><div align=\"left\"><b>" + textListMaterialHeaderSummaryTransaction[language][1] + "</b></div></td></tr>"
                + "<tr>"
                + "<td class=\"\">" + textListMaterialHeaderSummaryTransaction[language][2] + "</td>"
                + "<td width=\"5%\" class=\"\">" + textListMaterialHeaderSummaryTransaction[language][2] + "</td>"
                + "<td width=\"5%\" class=\"\">" + textListMaterialHeaderSummaryTransaction[language][3] + "</td>"
                + "<td class=\"\">" + textListMaterialHeaderSummaryTransaction[language][4] + "</td>"
                + "</tr>";
        if (openBill > 0 && openBillQty > 0) {

            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][5] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + openBillTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + openBillQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", openBill) + ".00</div></td>"
                    + "</tr>";
        }
        if (cancel > 0 && cancelQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][6] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cancelTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cancelQty + "</div></td>"
                    + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", cancel) + ".00</div></td>"
                    + "</tr>";
        }
        if (bayarKredit > 0 && bayarKreditQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][7] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + bayarKreditTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + bayarKreditQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", bayarKredit) + ".00</div></td>"
                    + "</tr>";
        }
        if (returnSales > 0 && returnSalesQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][8] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + returnSalesTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + returnSalesQty + "</div></td>"
                    + "<td width=\"20%\"class=\"\"><div align=\"right\">(" + String.format("%,.0f", returnSales) + ".00)</div></td>"
                    + "</tr>";
        }
        result = result + "<tr>"
                + "<td width=\"5%\" class=\"\" colspan =\"4\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][11] + "</div></td>";

        if (cash > 0 && cashQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"right\">" + textListMaterialHeaderSummaryTransaction[language][9] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cashTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cashQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", cash) + ".00</div></td>"
                    + "</tr>";
        }
        if (creditSales > 0 && creditSalesQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"right\">" + textListMaterialHeaderSummaryTransaction[language][10] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + creditSalesTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + creditSalesQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", creditSales) + ".00</div></td>"
                    + "</tr>";
        }
        result = result + "<trborder=\"1\" bordercolor=\"FFFFFF\">"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><b>TOTAL" + textListMaterialHeaderSummaryTransaction[language][11] + " </b></td>"
                + "<td width=\"24%\" class=\"\"><div align=\"right\"><b>" + totalTransaksi + "<b></div></td>"
                + "<td width=\"24%\" class=\"\"><div align=\"right\"><b>" + totalQty + "<b></div></td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\"><b>" + String.format("%,.0f", totalSummary) + ".00</b></div></td>"
                + "</tr>"
                + "</tr>"
                + "</table></td></tr></table>";
        return result;
    }

%>

<%!    public String generateGrandTotSummaryCash(String whereCashCashier, String whereCashCashierBm, String whereCashierCb, String whereCpm) {
        String result = "";
        double saldoAwal = 0;
        double jualCash = 0;
        double kembalian = 0;
        double jualKotor = 0;
        double returnSales = 0;
        double jualBersih = 0;
        double bayarKredit = 0;
        double saldoAkhir = 0;
        double uangDilaci = 0;
        double selisih = 0;
        String where = "";

        CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
        String defaultCurrency = currencyType.getCode();

        saldoAwal = PstBalance.getSaldoAwal(whereCashCashier);
        jualCash = PstCashPayment.getPembayaranKotorClosing(0, whereCashCashierBm);
        kembalian = PstCashReturn.getReturnSummary(0, whereCashCashierBm);
        jualKotor = jualCash - kembalian;
        returnSales = PstBillDetail.getReturSales(0, whereCashCashierBm);
        jualBersih = jualKotor - returnSales;
        bayarKredit = PstCashCreditPayment.getPembayaranKredit2(0, whereCpm);

        saldoAkhir = PstBalance.getSaldoAkhir(0, whereCashierCb);
        uangDilaci = PstBalance.getUangDiLaci(0, whereCashierCb);
        selisih = uangDilaci - saldoAkhir;

        result = "<p><b>GRAND TOTAL SUMMARY CASH</b></p>"
                + "<table width =\"100%\"><tr><td><table width=\"50%\" border=\"1\" bordercolor=\"FFFFFF\" class=\"table table-bordered\"> "
                + "<tr>"
                //+ "<td width=\"4%\" class=\"\" colspan =\"4\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\"><b>GRAND TOTAL SUMMARY CASH</b></div></td></tr>"
                + "<tr><td class=\"\" border=\"1\" bordercolor=\"FFFFFF\">NO</td>"
                + "<td class=\"\"border=\"1\" bordercolor=\"FFFFFF\">TRANSAKSI</td>"
                + "<td width=\"5%\" class=\"\"border=\"1\" bordercolor=\"FFFFFF\">CURRENCY</td>"
                + "<td class=\"\" border=\"1\" bordercolor=\"FFFFFF\">AMOUNT</td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\"border=\"1\" bordercolor=\"FFFFFF\">1.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"left\">Saldo Awal</div></td>"
                + "<td width=\"24%\" class=\"\" border=\"1\" bordercolor=\"FFFFFF\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\" border=\"1\" bordercolor=\"FFFFFF\"><div align=\"right\">" + String.format("%,.0f", saldoAwal) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">2.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">Penjualan Kotor</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", jualKotor) + ".00</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">3.</td>"
                + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">Retur Sales</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">(" + String.format("%,.0f", returnSales) + ".00)</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">4.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Penjualan Bersih</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", jualBersih) + ".00</div></td>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">5.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Bayar Kredit</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", bayarKredit) + ".00</div></td>"
                + "</tr>"
                + "</tr>"
                + "<tr>"
                + "<td width=\"5%\" class=\"\">6.</td>"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Saldo Akhir</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", saldoAkhir) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">7.</td>"
                + "<td width=\"20%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Uang di laci</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", uangDilaci) + ".00</div></td>"
                + "</tr>"
                + "<td width=\"5%\" class=\"\">8.</td>"
                + "<td width=\"20%\" class=\"\"nowrap width=\"95\"><div align=\"left\">Selisih</div></td>"
                + "<td width=\"24%\" class=\"\">" + defaultCurrency + "</td>"
                + "<td width=\"20%\"class=\"\" ><div align=\"right\">" + String.format("%,.0f", selisih) + ".00</div></td>"
                + "</tr>"
                + "</table></td></tr></table>";
        return result;
    }

%>

<%!    public String generateGrandSummaryTransaction2(int language, String whereCashCashier, String whereCashCashierBm, String whereCashierCb, String whereCpm) {
        String result = "";
        double openBill = 0;
        double cancel = 0;
        double bayarKredit = 0;
        double returnSales = 0;
        double cash = 0;
        double creditSales = 0;
        double totalSummary = 0;
        String where = "";

        double openBillQty = 0;
        double cancelQty = 0;
        double bayarKreditQty = 0;
        double returnSalesQty = 0;
        double cashQty = 0;
        double creditSalesQty = 0;
        double totalQty = 0;

        double openBillTransaksi = 0;
        double cancelTransaksi = 0;
        double bayarKreditTransaksi = 0;
        double returnSalesTransaksi = 0;
        double cashTransaksi = 0;
        double creditSalesTransaksi = 0;
        double totalTransaksi = 0;

        CurrencyType currencyType = PstCurrencyType.getDefaultCurrencyType();
        String defaultCurrency = currencyType.getCode();

        //summary
        openBill = PstBillMain.getSummaryOpenBill(0, whereCashCashierBm);
        cancel = PstBillMain.getSummaryCancel(0, whereCashCashierBm);
        bayarKredit = PstCashCreditPayment.getSummaryBayarCredit(0, whereCpm);
        returnSales = PstBillMain.getSummaryReturn(0, whereCashCashierBm);
        cash = PstBillMain.getSummaryCash(0, whereCashCashierBm);
        creditSales = PstBillMain.getSummarySalesCredit(0, whereCashCashierBm);
        totalSummary = cash + creditSales;

        //Qty
        openBillQty = PstBillMain.getCountQtySummaryOpenBill(0, whereCashCashierBm);
        cancelQty = PstBillMain.getCountQtySummaryCancel(0, whereCashCashierBm);
        bayarKreditQty = PstCreditPaymentMain.getCountQtySummaryBayarCredit(0, whereCpm);
        returnSalesQty = PstBillMain.getCountQtySummaryReturn(0, whereCashCashierBm);
        cashQty = PstBillMain.getCountQtySummaryCash(0, whereCashCashierBm);
        creditSalesQty = PstBillMain.getCountQtySummarySalesCredit(0, whereCashCashierBm);
        totalQty = cashQty + creditSalesQty;

        //transaksi
        openBillTransaksi = PstBillMain.getCountTransSummaryOpenBill(0, whereCashCashierBm);
        cancelTransaksi = PstBillMain.getCountTransSummaryCancel(0, whereCashCashierBm);
        bayarKreditTransaksi = PstCreditPaymentMain.getCountTransSummaryBayarCredit(0, whereCpm);
        returnSalesTransaksi = PstBillMain.getCountTransSummaryReturn(0, whereCashCashierBm);
        cashTransaksi = PstBillMain.getCountTransSummaryCash(0, whereCashCashierBm);
        creditSalesTransaksi = PstBillMain.getCountTransSummarySalesCredit(0, whereCashCashierBm);
        totalTransaksi = cashTransaksi + creditSalesTransaksi;

        result = "<p><b>" + textListMaterialHeaderSummaryTransaction[language][1] + "</b><p>"
                + "<table width =\"100%\"><tr><td><table width=\"50%\" border=\"1\" bordercolor=\"FFFFFF\" class=\"table table-bordered\"> "
                + "<tr>"
                //+ "<td width=\"4%\" class=\"\" colspan =\"4\"><div align=\"left\"><b>" + textListMaterialHeaderSummaryTransaction[language][1] + "</b></div></td></tr>"
                + "<tr>"
                + "<td class=\"\">" + textListMaterialHeaderSummaryTransaction[language][2] + "</td>"
                + "<td width=\"5%\" class=\"\">" + textListMaterialHeaderSummaryTransaction[language][2] + "</td>"
                + "<td width=\"5%\" class=\"\">" + textListMaterialHeaderSummaryTransaction[language][3] + "</td>"
                + "<td class=\"\">" + textListMaterialHeaderSummaryTransaction[language][4] + "</td>"
                + "</tr>";
        if (openBill > 0 && openBillQty > 0) {

            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][5] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + openBillTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + openBillQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", openBill) + ".00</div></td>"
                    + "</tr>";
        }
        if (cancel > 0 && cancelQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][6] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cancelTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cancelQty + "</div></td>"
                    + "<td width=\"20%\"class=\"\"><div align=\"right\">" + String.format("%,.0f", cancel) + ".00</div></td>"
                    + "</tr>";
        }
        if (bayarKredit > 0 && bayarKreditQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\" nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][7] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + bayarKreditTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + bayarKreditQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", bayarKredit) + ".00</div></td>"
                    + "</tr>";
        }
        if (returnSales > 0 && returnSalesQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][8] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + returnSalesTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + returnSalesQty + "</div></td>"
                    + "<td width=\"20%\"class=\"\"><div align=\"right\">(" + String.format("%,.0f", returnSales) + ".00)</div></td>"
                    + "</tr>";
        }
        result = result + "<tr>"
                + "<td width=\"5%\" class=\"\" colspan =\"4\"><div align=\"left\">" + textListMaterialHeaderSummaryTransaction[language][11] + "</div></td>";

        if (cash > 0 && cashQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"right\">" + textListMaterialHeaderSummaryTransaction[language][9] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cashTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + cashQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", cash) + ".00</div></td>"
                    + "</tr>";
        }
        if (creditSales > 0 && creditSalesQty > 0) {
            result = result + "<tr border=\"1\" bordercolor=\"FFFFFF\">"
                    + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><div align=\"right\">" + textListMaterialHeaderSummaryTransaction[language][10] + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + creditSalesTransaksi + "</div></td>"
                    + "<td width=\"24%\" class=\"\"><div align=\"right\">" + creditSalesQty + "</div></td>"
                    + "<td width=\"20%\" class=\"\"><div align=\"right\">" + String.format("%,.0f", creditSales) + ".00</div></td>"
                    + "</tr>";
        }
        result = result + "<trborder=\"1\" bordercolor=\"FFFFFF\">"
                + "<td width=\"10%\" class=\"\"nowrap width=\"95\"><b>TOTAL" + textListMaterialHeaderSummaryTransaction[language][11] + " </b></td>"
                + "<td width=\"24%\" class=\"\"><div align=\"right\"><b>" + totalTransaksi + "<b></div></td>"
                + "<td width=\"24%\" class=\"\"><div align=\"right\"><b>" + totalQty + "<b></div></td>"
                + "<td width=\"20%\" class=\"\"><div align=\"right\"><b>" + String.format("%,.0f", totalSummary) + ".00</b></div></td>"
                + "</tr>"
                + "</tr>"
                + "</table></td></tr></table>";
        return result;
    }
%>

<%!//ListPayment

    public String drawListDetailPayment(int language, Vector objectClass, int start, long currencyId, long oidCashCashier, int iSaleReportType) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("table table-bordered");
            ctrlist.setTitleStyle("");
            ctrlist.setCellStyle("");
            ctrlist.setHeaderStyle("");

            ctrlist.dataFormat(textListMaterialHeaderDetail[language][0], "3%", "0", "0", "center", "bottom"); //no
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][1], "7%", "0", "0", "center", "bottom"); //tipe
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][2], "8%", "0", "0", "center", "bottom"); //currency
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][3], "15%", "0", "0", "center", "bottom"); //jumlah
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][4], "15%", "0", "0", "center", "bottom"); //total
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][5], "15%", "0", "0", "center", "bottom");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();

            if (start < 0) {
                start = 0;
            }
            String where = "";
            String whereSaleType = "";
            double rate = 0;
            double total = 0.0;
            double totalPayment = 0.0;
            double totalReturn = 0.0;
            double totalAll = 0.0;

            where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + oidCashCashier;
            if (iSaleReportType == PstBillMain.TRANS_TYPE_CASH) {
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
            } else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT) {
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
            } else if (iSaleReportType == -1) {
                where = where;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                CashPayments cashPayment = (CashPayments) temp.get(0);
                CurrencyType currencyType = (CurrencyType) temp.get(1);
                CashReturn cashReturn = (CashReturn) temp.get(2);

                double PaymentReturn = 0;

                //update opie 18-06-2012k detail pembayaran - return
                double amountPaymentAll = 0.0;
                if (cashPayment.getPaymentType() != 6) {
                    //amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount();
                    if (cashPayment.getPaymentType() == 0) {
                        PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier, 0);
                    } //cash
                    else if (cashPayment.getPaymentType() == 1) {
                        PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier, 1);
                    } //kartu kredit
                    else if (cashPayment.getPaymentType() == 2) {
                        PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier, 2);
                    } //check
                    else if (cashPayment.getPaymentType() == 3) {
                        PaymentReturn = PstCashPayment.getPembayaranReturn(oidCashCashier, 3);
                    }//debit
                    amountPaymentAll = cashPayment.getAmount() - cashReturn.getAmount() - PaymentReturn;
                } else {
                    amountPaymentAll = cashPayment.getAmount();
                }

                total += amountPaymentAll;

                rowx = new Vector();
                rowx.add("<div align=\"left\">" + (start + i + 1) + "</div>");
                rowx.add("<div align=\"right\">" + PstCashPayment.paymentType[cashPayment.getPaymentType()]);
                rowx.add(currencyType.getCode());
                rowx.add("<div align=\"right\">" + cashPayment.getRate());
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", amountPaymentAll) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", amountPaymentAll) + ".00</b></div>");
                lstData.add(rowx);

            }
            rowx = new Vector();
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
            rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", total) + ".00</b></div>");
            lstData.add(rowx);
            result = ctrlist.draw();
        }
        return result;
    }

%>

<%!//update opie-eyek untuk payment dinamis 10022013
    public String drawListDetailPaymentDinamis(int language, Vector objectClass, int start, long currencyId, long oidCashCashier, int iSaleReportType) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("table table-bordered");
            ctrlist.setTitleStyle("");
            ctrlist.setCellStyle("");
            ctrlist.setHeaderStyle("");
            ctrlist.setBorder(0);
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][0], "3%", "0", "0", "center", "bottom"); //no
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][1], "7%", "0", "0", "center", "bottom"); //tipe
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][2], "8%", "0", "0", "center", "bottom"); //currency
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][3], "15%", "0", "0", "center", "bottom"); //jumlah
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][4], "15%", "0", "0", "center", "bottom"); //total
            ctrlist.dataFormat(textListMaterialHeaderDetail[language][5], "15%", "0", "0", "center", "bottom");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            Vector rowx = new Vector();

            if (start < 0) {
                start = 0;
            }
            String where = "";
            String whereSaleType = "";
            double rate = 0;
            double total = 0.0;
            double totalPayment = 0.0;
            double totalReturn = 0.0;
            double totalAll = 0.0;

            where = " CBM. " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + "=" + oidCashCashier;
            if (iSaleReportType == PstBillMain.TRANS_TYPE_CASH) {
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
            } else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT) {
                where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT
                        + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "= 0";
            } else if (iSaleReportType == -1) {
                where = where;
            }

            Vector vCurrType = PstCurrencyType.list(0, 0, "", "");
            Hashtable hashCurrType = new Hashtable();
            if (vCurrType != null && vCurrType.size() > 0) {
                int iCurrTypeCount = vCurrType.size();
                for (int k = 0; k < iCurrTypeCount; k++) {
                    CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(k);
                    hashCurrType.put("" + objCurrencyType.getOID(), objCurrencyType.getName() + "(" + objCurrencyType.getCode() + ")");
                }
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                CashPayments1 cashPayment = (CashPayments1) temp.get(0);
                CurrencyType currencyType = (CurrencyType) temp.get(1);
                CashReturn cashReturn = (CashReturn) temp.get(2);

                String strPaymentType = PstPaymentSystem.getTypePayment(cashPayment.getPaymentType());

                double PaymentReturn = 0;

                //update opie 18-06-2012k detail pembayaran - return
                double amountPaymentAll = 0.0;
                double dPaymentPerType = 0.0;
                double amounPayment = 0.0;
                String strNumber = String.valueOf(i + 1);
                String strCurrency = String.valueOf(hashCurrType.get("" + cashPayment.getCurrencyId()));
                String strAmount = FRMHandler.userFormatStringDecimal((cashPayment.getAmount()));
                String strRate = FRMHandler.userFormatStringDecimal(cashPayment.getRate());
                String strTotal = FRMHandler.userFormatStringDecimal(cashPayment.getAmount() * cashPayment.getRate());
                dPaymentPerType = ((cashPayment.getAmount() * cashPayment.getRate()));
                amounPayment = dPaymentPerType - cashReturn.getAmount();
                total = total + amounPayment;
                rowx = new Vector();

                rowx.add("<div align=\"left\">" + (start + i + 1) + "</div>");
                rowx.add("<div align=\"right\">" + strPaymentType);
                rowx.add(currencyType.getCode());
                rowx.add("<div align=\"right\">" + cashPayment.getRate());
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", amounPayment) + ".00</b></div>");
                rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", amounPayment) + ".00</b></div>");
                lstData.add(rowx);

            }
            rowx = new Vector();
            rowx.add("<div align=\"left\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"right\"></div>");
            rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
            rowx.add("<div align=\"right\"><b>" + String.format("%,.0f", total) + ".00</b></div>");
            lstData.add(rowx);
            result = ctrlist.draw();
        }
        return result;
    }

%>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
    int start = FRMQueryString.requestInt(request, "start");
    long billMainID = FRMQueryString.requestLong(request, "hidden_billmain_id");
    long cashCashierId = FRMQueryString.requestLong(request, "hidden_cash_cashier_Id");
    int recordToGet = 100;
    int iViewType = FRMQueryString.requestInt(request, "view_type");
    int viewReport = FRMQueryString.requestInt(request, "view_report");
    iViewType = viewReport;

    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    ControlLine ctrLine = new ControlLine();
    SrcSaleReport srcSaleReport = new SrcSaleReport();
    FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
    frmSrcSaleReport.requestEntity(srcSaleReport);
    int vectSize = 0;
    int vectSize1 = 0;

    try {
        srcSaleReport = (SrcSaleReport) session.getValue(SaleReportDocument.SALE_REPORT_DOC);
    } catch (Exception e) {
    }

    String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd 00:00:00");
    String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd 23:59:59");

    String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'"
            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE
            + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + iSaleReportType;

    if (iSaleReportType == PstCashPayment.CASH) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE;
    } else {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "!=" + PstBillMain.TRANS_STATUS_DELETED;
    }

    if (srcSaleReport.getShiftId() != 0) {
        //where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
        where = where + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
    }

    if (srcSaleReport.getLocationId() != 0) {
        //where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
        where = where + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
    }

    if (srcSaleReport.getCurrencyOid() != 0) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + "=" + srcSaleReport.getCurrencyOid();
    }

    if (srcSaleReport.getCashMasterId() != 0) {
        //where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID]+"="+srcSaleReport.getCashMasterId();
        where = where + " AND MSTR. " + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] + "=" + srcSaleReport.getCashMasterId();
    }

    String whereOpening = PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
    if (srcSaleReport.getShiftId() != 0) {
        whereOpening = whereOpening + " AND CSH." + PstCashCashier.fieldNames[PstCashCashier.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
    }

    if (srcSaleReport.getLocationId() != 0) {
        whereOpening = whereOpening + " AND MSTR." + PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
    }

    if (srcSaleReport.getCashMasterId() != 0) {
        whereOpening = whereOpening + " AND MSTR." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] + "=" + srcSaleReport.getCashMasterId();
    }

    String orderOpening = " LOC." + PstLocation.fieldNames[PstLocation.FLD_CODE];
    orderOpening = orderOpening + " ," + PstCashCashier.fieldNames[PstCashCashier.FLD_OPEN_DATE];
    orderOpening = orderOpening + " ,MSTR." + PstCashMaster.fieldNames[PstCashMaster.FLD_CASHIER_NUMBER];

    vectSize1 = PstCashCashier.getCountPerCashOpening(whereOpening);

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        try {
            start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            if (srcSaleReport == null) {
                srcSaleReport = new SrcSaleReport();
            }
        } catch (Exception e) {
            srcSaleReport = new SrcSaleReport();
        }
    } else {
        session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
    }

    Location location = new Location();
    try {
        location = PstLocation.fetchExc(srcSaleReport.getLocationId());
    } catch (Exception e) {
        location.setName("Semua toko");
    }

    Vector recordList = PstCashCashier.listCashOpening(0, 0, whereOpening, orderOpening);

    String strCurrencyType = "Semua mata uang";
    if (srcSaleReport.getCurrencyOid() != 0) {
        //Get currency code
        String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + "=" + srcSaleReport.getCurrencyOid();
        Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
        CurrencyType currencyType = (CurrencyType) listCurrencyType.get(0);
        strCurrencyType = currencyType.getCode();
    }

    String cashier = "Semua kasir";
    if (srcSaleReport.getCashMasterId() != 0) {
        String whereClause = PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] + "=" + srcSaleReport.getCashMasterId();
        Vector listCashier = PstCashMaster.list(0, 0, whereClause, "");
        CashMaster cashMaster = (CashMaster) listCashier.get(0);
        cashier = "" + cashMaster.getCashierNumber();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            table {font-size: 11px}            

            .tabel_header {width: 100%}
            .tabel_header td {padding-bottom: 10px}

            .th1,.td1 {border-color: black !important;}                        
        </style>
    </head>
    <body>
        <div style="margin: 10px">

            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>

            <div>
                <h4><b>Laporan Penjualan</b></h4>
            </div>

            <table class="tabel_header">
                <tr>
                    <td style="width: 8%"><%=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                    <td>: <%=location.getName()%></td>
                </tr>
                <tr> 
                    <td>Tanggal</td>
                    <td>: <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr>
                    <td>Tipe</td>
                    <td>:
                        <%if (iSaleReportType == PstBillMain.TRANS_TYPE_CASH) {%>
                        <%= "Penjualan cash"%>
                        <% } else if (iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT) {%>
                        <%= "Penjualan kredit"%>
                        <% } else if (iSaleReportType == -1) {%>
                        <%= "Semua tipe penjualan"%>
                        <%}%>
                    </td>
                </tr>
                <tr> 
                    <td><%=textListTitleHeader[SESS_LANGUAGE][8]%></td>
                    <td>: <%=strCurrencyType%></td>
                </tr>
                <tr>
                    <td><%=textListTitleHeader[SESS_LANGUAGE][9]%></td>
                    <td>: <%=cashier%></b></td>
                    <td class="text-right">Tanggal Dicetak : <%=Formater.formatDate(new Date(), "dd/MM/yyyy")%></td>
                </tr>
            </table>

            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <form name="frm_reportsale" method="post" action="">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="add_type" value="">
                <input type="hidden" name="start" value="<%= start%>">
                <input type="hidden" name="approval_command">
                <input type="hidden" name="hidden_billmain_id" value="<%=billMainID%>">
                <input type="hidden" name="hidden_cash_cashier_Id" value="<%=cashCashierId%>">
                <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">	

                <div>
                    <%=drawList(out, SESS_LANGUAGE, recordList, start, srcSaleReport, srcSaleReport.getCurrencyOid(), iSaleReportType, iViewType)%>
                </div>
                <%
                    Vector recordListOnlyGetSales = PstCashCashier.listCashOpeningOnlyGetSales(0, 0, whereOpening, orderOpening);

                    if (recordListOnlyGetSales.size() > 0) {

                        Vector listDetailPayment = new Vector();
                        int paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));

                        CashCashier cashCashier = new CashCashier();
                        String whereCashCashier = "";
                        String whereCashCashier2 = "";
                        String whereCashCashier3 = "";
                        String whereCashCashier4 = "";
                        for (int i = 0; i < recordListOnlyGetSales.size(); i++) {
                            Vector temp = (Vector) recordListOnlyGetSales.get(i);
                            cashCashier = (CashCashier) temp.get(0);
                            if (i == 0) {
                                whereCashCashier = "" + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier2 = "CBM." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier3 = "CB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier4 = "CPM." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                            } else {
                                whereCashCashier = whereCashCashier + " OR " + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier2 = whereCashCashier2 + " OR CBM." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier3 = whereCashCashier3 + " OR CB." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                                whereCashCashier4 = whereCashCashier4 + " OR CPM." + PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + "='" + cashCashier.getOID() + "'";
                            }
                        }
                        String whereTo = " ( " + whereCashCashier + " ) ";
                        String whereBm = " ( " + whereCashCashier2 + " )";
                        String whereCb = " ( " + whereCashCashier3 + " )";
                        String whereCpm = " ( " + whereCashCashier4 + " )";

                        if (paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
                            where = where + " AND " + whereBm;
                            listDetailPayment = PstCashPayment1.listDetailPaymentDinamisWithReturn(0, 0, whereBm);
                        } else {
                            where = where + " AND " + whereBm;
                            listDetailPayment = PstCashPayment.listDetailPaymentWithReturn(0, 0, whereBm);
                        }

                %>
                
                <hr style="border-width: thin; border-color: black; margin-top: 5px">
                
                <div class="row">
                    <table>
                        <tr>
                            <td>
                                <div class="col-sm-4"><%=generateGrandTotSummaryCash(whereTo, whereBm, whereCb, whereCpm)%></div>
                                <div class="col-sm-4">
                                    <p><b>DETAIL GRAND TOTAL PEMBAYARAN</b></p>
                                    <%=drawListDetailPaymentDinamis(SESS_LANGUAGE, listDetailPayment, start, srcSaleReport.getCurrencyOid(), cashCashier.getOID(), iSaleReportType)%>
                                </div>
                                <div class="col-sm-4"><%=generateGrandSummaryTransaction2(SESS_LANGUAGE, whereTo, whereBm, whereCb, whereCpm)%></div>
                            </td>
                        </tr>
                    </table>                    
                </div>
                <%}%> 

            </form>

            <hr style="border-width: thin; border-color: black; margin-top: 5px">

                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
        </div>
    </body>
</html>
