<%-- 
    Document   : pdf_report_sales
    Created on : Mar 16, 2020, 11:55:32 AM
    Author     : Regen
--%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public static final String textListGlobal[][] = {
        {"LAPORAN PENJUALAN", "Periode", " s/d ", "Lokasi", "Semua", "PER BULAN", "PER TANGGAL", "Transaksi"},
        {"SALES REPORT", "Period", " to ", "Location", "All", "BY MONTH", "BY DATE", "Transaction"}
    };

    /* this constant used to list text of listHeader */
    public static final String headerByMonth[][] = {
        {"NO", "BULAN - TAHUN", "JUMLAH PELANGGAN", "TOTAL PENJUALAN", "TOTAL DP", "TOTAL KREDIT"},
        {"NO", "MONTH - YEAR", "TOTAL CUSTOMER", "TOTAL SALES", "TOTAL DP", "TOTAL CREDIT"}
    };

    public static final String headerByDate[][] = {
        {"NO", "TANGGAL", "NAMA HARI", "QTY PK", "AMOUNT", "DP", "PIUTANG", "HPP", "GROSS PROFIT"},
        {"NO", "DATE", "DAY NAME", "QTY PK", "AMOUNT", "DP", "ACCOUNTS RECEIVABLE", "HPP", "GROSS PROFIT"}
    };

%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
    </head>

    <body bgcolor="#FFFFFF" text="#000000">
        Loading ... 
        <script language="JavaScript">
            window.focus();
        </script>
    </body>
</html>
<%    String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
    Location loc = new Location();

    Vector list = new Vector();
    list = (Vector) session.getValue("DATA_PARAM");
    String location = (String) String.valueOf(list.get(0));
    String startMonth = (String) list.get(1);
    String startDate = (String) list.get(2);
    String endMonth = (String) list.get(3);
    String endDate = (String) list.get(4);
    String jLaporan = (String) String.valueOf(list.get(5));
    String tTransaksi = (String) String.valueOf(list.get(6));

    int typeTransaksi = Integer.parseInt(tTransaksi);
    long oidLocation = Long.parseLong(location);
    int jenisLaporan = Integer.parseInt(jLaporan);

    String transaksi = SrcSaleReport.reportType[SESS_LANGUAGE][typeTransaksi];
    String nameLokasi = "";
    String whereClause = "";

    try {
        if (oidLocation != 0) {
            loc = PstLocation.fetchExc(oidLocation);
            nameLokasi = loc.getName();
        } else {
            nameLokasi = "All Location";
        }

    } catch (Exception e) {
    }
    startMonth = "01 " + startMonth;
    endMonth = "31 " + endMonth;
    Date dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
    Date dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");
    String dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
    String dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");
    String tanggalAwal = "";
    String tanggalAkhir = "";
    JSONArray arr = new JSONArray();

    double total = 0;
    double totalDp = 0;
    double totalPiutang = 0;
    double totalHpp = 0;
    double totalPk = 0;
    double totalKredit = 0;
    double totalGross = 0;

    double perTotalDp = 0;
    double perTotalPiutang = 0;
    double perTotalHpp = 0;
    double perTotalGross = 0;

    double amount = 0;
    double dp = 0;
    double pk = 0;
    double hpp = 0;
    double kredit = 0;
    double piutang = 0;
    double gross = 0;

    Vector<Location> listLocation = PstLocation.list(0, 0, "", "");
    int gtotal = 0;
    int fTotal = 0;

    Vector header = new Vector();
    Vector body = new Vector();
    Vector footer = new Vector();
    Vector listTable = new Vector();
    Vector listHTable = new Vector();
    Vector content = new Vector();
    Vector listData = new Vector();
    Vector listReport = new Vector();
    String url = "";
    JSONObject obj = new JSONObject();
    if (jenisLaporan == 1) {
        tanggalAwal = dateStart1;
        tanggalAkhir = dateEnd1;
    } else if (jenisLaporan == 2) {
        tanggalAwal = startDate;
        tanggalAkhir = endDate;
    }
    whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
            + " AND ("
            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
            + ">= TO_DAYS('" + tanggalAwal + " 00:00:00')) AND "
            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
            + "<= TO_DAYS('" + tanggalAkhir + " 23:59:59'))"
            + ")";

    if (oidLocation != 0) {
        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + oidLocation
                + "";
    }

    if (jenisLaporan == 1) {
        if (typeTransaksi == SrcSaleReport.TYPE_CASH) {
            listData = SaleReportDocument.listCashSalesBulanan(0, 0, whereClause, "");
            for (int i = 0; i < listData.size(); i++) {
                BillMain bm = (BillMain) listData.get(i);
                Vector row = new Vector();
                String date = Formater.formatDate(bm.getBillDate(), "MMMM yyyy");
                amount = Math.round(bm.getAmount());
                total += amount;

                row.add(String.valueOf(i + 1) + ".");
                row.add(date);
                row.add(Formater.formatNumber(amount, "###,###.##"));
                listReport.add(row);
            }

            header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][5]);
            header.add(textListGlobal[SESS_LANGUAGE][1]);
            header.add(": " + startMonth + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endMonth);
            header.add(textListGlobal[SESS_LANGUAGE][7]);
            header.add(": " + transaksi);

            listTable.add(headerByMonth[SESS_LANGUAGE][0]);
            listTable.add(headerByMonth[SESS_LANGUAGE][1]);
            listTable.add(headerByMonth[SESS_LANGUAGE][3]);

            body.add(listTable);
            body.add(listReport);
            footer.add(Formater.formatNumber(total, "###,###.##"));

            content.add(header);
            content.add(body);
            content.add(footer);

        } else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {

            url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan/" + tanggalAwal + "/" + tanggalAkhir + "/" + oidLocation;
            obj = WebServices.getAPI("", url);
            arr = obj.getJSONArray("DATA");
            if (arr.length() > 0) {
                for (int x = 0; x < arr.length(); x++) {
                    JSONObject data = arr.getJSONObject(x);
                    Vector row = new Vector();
                    amount = Math.round(data.optDouble("AMOUNT"));
                    dp = Math.round(data.optDouble("DP"));
                    pk = Math.round(data.optDouble("JUMLAH_PK"));
                    kredit = Math.round(amount - dp);
                    String tgl = data.optString("TANGGAL");
                    Date date = Formater.formatDate(tgl, "yyyy-MM-dd");
                    String newDate = Formater.formatDate(date, "MMMM yyyy");
                    total += amount;
                    totalDp += dp;
                    totalPk += pk;
                    totalKredit += kredit;

                    row.add(String.valueOf(x + 1) + ".");
                    row.add(newDate);
                    row.add("" + pk);
                    row.add(Formater.formatNumber(amount, "###,###.##"));
                    row.add(Formater.formatNumber(dp, "###,###.##"));
                    row.add(Formater.formatNumber(kredit, "###,###.##"));
                    listReport.add(row);
                }

                header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][5]);
                header.add(textListGlobal[SESS_LANGUAGE][1]);
                header.add(": " + startMonth + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endMonth);
                header.add(textListGlobal[SESS_LANGUAGE][7]);
                header.add(": " + transaksi);

                listTable.add(headerByMonth[SESS_LANGUAGE][0]);
                listTable.add(headerByMonth[SESS_LANGUAGE][1]);
                listTable.add(headerByMonth[SESS_LANGUAGE][2]);
                listTable.add(headerByMonth[SESS_LANGUAGE][3]);
                listTable.add(headerByMonth[SESS_LANGUAGE][4]);
                listTable.add(headerByMonth[SESS_LANGUAGE][5]);

                body.add(listTable);
                body.add(listReport);
                footer.add(Formater.formatNumber(totalPk, "###,###.##"));
                footer.add(Formater.formatNumber(total, "###,###.##"));
                footer.add(Formater.formatNumber(totalDp, "###,###.##"));
                footer.add(Formater.formatNumber(totalKredit, "###,###.##"));

                content.add(header);
                content.add(body);
                content.add(footer);
            }

        } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {

            header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][5]);
            header.add(textListGlobal[SESS_LANGUAGE][1]);
            header.add(": " + startMonth + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endMonth);
            header.add(textListGlobal[SESS_LANGUAGE][7]);
            header.add(": " + transaksi);

//            List Header 1
            listTable.add("CABANG");
            for (Location loca : listLocation) {
                listTable.add("" + loca.getName());
            }
            listTable.add("TOTAL");

//            List Header 2
            listHTable.add("TGL");
            for (Location loca : listLocation) {
                listHTable.add("Cash");
                listHTable.add("Credit");
            }
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                    + " AND ("
                    + "(TO_DAYS( " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                    + ">= TO_DAYS('" + tanggalAwal + "')) AND "
                    + "(TO_DAYS( " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                    + "<= TO_DAYS('" + tanggalAkhir + "'))"
                    + ")";
            listData = PstBillMain.list(0, 0, whereClause + " GROUP BY DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")", "");
            for (int i = 0; i < listData.size(); i++) {
                BillMain bm = (BillMain) listData.get(i);
                Vector row = new Vector();
                row.add(Formater.formatDate(bm.getBillDate(), "dd MMMM yyyy"));

                int total1 = 0;
                int total2 = 0;
                long locaId = 0;
                for (Location loca : listLocation) {
                    locaId = loca.getOID();
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                            + " AND "
                            + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                            + "= TO_DAYS('" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "'))"
                            + "";
                    if (locaId != 0) {
                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                + "";
                    }
                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                    row.add("" + dataCash);

                    total1 += dataCash;
                    url = apiUrl + "/credit/pinjaman/laporan-penjualan-global/" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "/" + locaId;
                    System.out.println("REST API URL : " + url);
                    obj = WebServices.getAPI("", url);
                    int dataCredit = 0;
                    try {
                        dataCredit = obj.getInt("DATA");
                    } catch (Exception e) {
                        System.out.println("Problem is : " + e.getMessage());
                        dataCredit = 0;
                    }
                    row.add("" + dataCredit);
                    total2 += dataCredit;
                }
                row.add("" + total1 + total2);
                listReport.add(row);

                fTotal = total1 + total2;
                gtotal += fTotal;
            }
            body.add(listTable);
            body.add(listHTable);
            body.add(listReport);

            long locaId = 0;
            footer.add("GTOTAL");
            for (Location loca : listLocation) {
                locaId = loca.getOID();
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                        + " AND ("
                        + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + ">= TO_DAYS('" + tanggalAwal + "')) AND "
                        + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + "<= TO_DAYS('" + tanggalAkhir + "'))"
                        + ")";
                if (locaId != 0) {
                    whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                            + "";
                }
                double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                footer.add("" + dataCash);
                url = apiUrl + "/credit/pinjaman/laporan-penjualan-gtotal/" + tanggalAwal + "/" + tanggalAkhir + "/" + locaId;
                System.out.println("REST API URL : " + url);
                obj = WebServices.getAPI("", url);
                int dataCredit = 0;
                try {
                    dataCredit = obj.getInt("DATA");
                } catch (Exception e) {
                    System.out.println("Problem is : " + e.getMessage());
                    dataCredit = 0;
                }
                footer.add("" + dataCredit);
            }
            footer.add("" + gtotal);

            content.add(header);
            content.add(body);
            content.add(footer);
        }
    } else if (jenisLaporan == 2) {
        if (typeTransaksi == SrcSaleReport.TYPE_CASH) {
            listData = SaleReportDocument.listCashSales(0, 0, whereClause, "");
            for (int i = 0; i < listData.size(); i++) {
                Vector row = new Vector();
                BillMain bm = (BillMain) listData.get(i);
                String date = Formater.formatDate(bm.getBillDate(), "dd-MM-yyyy");
                amount = Math.round(bm.getAmount());
                hpp = Math.round(bm.getServiceValue());
                gross = Math.round(bm.getPaidAmount());
                total += amount;
                totalHpp += hpp;
                totalGross += gross;

                perTotalHpp = totalHpp / total * 100;
                perTotalGross = totalGross / total * 100;

                row.add(String.valueOf(i + 1) + ".");
                row.add(date);
                row.add(bm.getEventName());
                row.add(Formater.formatNumber(amount, "###,###.##"));
                row.add(Formater.formatNumber(hpp, "###,###.##"));
                row.add(Formater.formatNumber(gross, "###,###.##"));
                listReport.add(row);
            }

            header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][6]);
            header.add(textListGlobal[SESS_LANGUAGE][1]);
            header.add(": " + startDate + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endDate);
            header.add(textListGlobal[SESS_LANGUAGE][7]);
            header.add(": " + transaksi);

            listTable.add(headerByDate[SESS_LANGUAGE][0]);
            listTable.add(headerByDate[SESS_LANGUAGE][1]);
            listTable.add(headerByDate[SESS_LANGUAGE][2]);
            listTable.add(headerByDate[SESS_LANGUAGE][4]);
            listTable.add(headerByDate[SESS_LANGUAGE][7]);
            listTable.add(headerByDate[SESS_LANGUAGE][8]);

            body.add(listTable);
            body.add(listReport);
            footer.add("" + Formater.formatNumber(total, "###,###.##"));
            footer.add("" + Formater.formatNumber(totalHpp, "###,###.##"));
            footer.add("" + Formater.formatNumber(totalGross, "###,###.##"));
            footer.add("" + Math.round(perTotalHpp));
            footer.add("" + Math.round(perTotalGross));

            content.add(header);
            content.add(body);
            content.add(footer);

        } else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {

            url = apiUrl + "/credit/pinjaman/laporan-penjualan-tanggal/" + tanggalAwal + "/" + tanggalAkhir + "/" + oidLocation;
            obj = WebServices.getAPI("", url);
            arr = obj.getJSONArray("DATA");
            if (arr.length() > 0) {
                for (int x = 0; x < arr.length(); x++) {
                    JSONObject data = arr.getJSONObject(x);
                    Vector row = new Vector();
                    Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                    String newDate = Formater.formatDate(date, "dd MMMM yyyy");
                    amount = Math.round(data.optDouble("AMOUNT"));
                    dp = Math.round(data.optDouble("DP"));
                    piutang = Math.round(data.optDouble("PIUTANG"));
                    hpp = Math.round(data.optDouble("HPP"));
                    gross = Math.round(data.optDouble("GROSS"));
                    pk = Math.round(data.optDouble("JUMLAH_PK"));
                    total += amount;
                    totalDp += dp;
                    totalPiutang += piutang;
                    totalHpp += hpp;
                    totalGross += gross;

                    perTotalHpp = Math.round(totalHpp / total * 100.0);
                    perTotalDp = Math.round(totalDp / total * 100.0);
                    perTotalPiutang = Math.round(totalPiutang / total * 100.0);
                    perTotalGross = Math.round(totalGross / total * 100.0);

                    row.add(String.valueOf(x + 1) + ".");
                    row.add(newDate);
                    row.add(data.optString("HARI"));
                    row.add("" + pk);
                    row.add(Formater.formatNumber(data.optDouble("AMOUNT"), "###,###.##"));
                    row.add(Formater.formatNumber(data.optDouble("DP"), "###,###.##"));
                    row.add(Formater.formatNumber(data.optDouble("PIUTANG"), "###,###.##"));
                    row.add(Formater.formatNumber(data.optDouble("HPP"), "###,###.##"));
                    row.add(Formater.formatNumber(data.optDouble("GROSS"), "###,###.##"));
                    listReport.add(row);
                }

                header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][6]);
                header.add(textListGlobal[SESS_LANGUAGE][1]);
                header.add(": " + startDate + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endDate);
                header.add(textListGlobal[SESS_LANGUAGE][7]);
                header.add(": " + transaksi);

                listTable.add(headerByDate[SESS_LANGUAGE][0]);
                listTable.add(headerByDate[SESS_LANGUAGE][1]);
                listTable.add(headerByDate[SESS_LANGUAGE][2]);
                listTable.add(headerByDate[SESS_LANGUAGE][3]);
                listTable.add(headerByDate[SESS_LANGUAGE][4]);
                listTable.add(headerByDate[SESS_LANGUAGE][5]);
                listTable.add(headerByDate[SESS_LANGUAGE][6]);
                listTable.add(headerByDate[SESS_LANGUAGE][7]);
                listTable.add(headerByDate[SESS_LANGUAGE][8]);

                body.add(listTable);
                body.add(listReport);

                footer.add("" + Formater.formatNumber(total, "###,###.##"));
                footer.add("" + Formater.formatNumber(totalDp, "###,###.##"));
                footer.add("" + Formater.formatNumber(totalPiutang, "###,###.##"));
                footer.add("" + Formater.formatNumber(totalHpp, "###,###.##"));
                footer.add("" + Formater.formatNumber(totalGross, "###,###.##"));
                footer.add("" + Math.round(perTotalDp));
                footer.add("" + Math.round(perTotalPiutang));
                footer.add("" + Math.round(perTotalHpp));
                footer.add("" + Math.round(perTotalGross));

                content.add(header);
                content.add(body);
                content.add(footer);
            }
        } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {

            header.add(textListGlobal[SESS_LANGUAGE][0] + " " + textListGlobal[SESS_LANGUAGE][6]);
            header.add(textListGlobal[SESS_LANGUAGE][1]);
            header.add(": " + startDate + " " + textListGlobal[SESS_LANGUAGE][2] + " " + endDate);
            header.add(textListGlobal[SESS_LANGUAGE][7]);
            header.add(": " + transaksi);

//            List Header 1
            listTable.add("CABANG");
            for (Location loca : listLocation) {
                listTable.add("" + loca.getName());
            }
            listTable.add("TOTAL");

//            List Header 2
            listHTable.add("TGL");
            for (Location loca : listLocation) {
                listHTable.add("Cash");
                listHTable.add("Credit");
            }
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                    + " AND ("
                    + "(TO_DAYS( " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                    + ">= TO_DAYS('" + tanggalAwal + "')) AND "
                    + "(TO_DAYS( " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                    + "<= TO_DAYS('" + tanggalAkhir + "'))"
                    + ")";
            listData = PstBillMain.list(0, 0, whereClause + " GROUP BY DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")", "");
            for (int i = 0; i < listData.size(); i++) {
                BillMain bm = (BillMain) listData.get(i);
                Vector row = new Vector();
                row.add(Formater.formatDate(bm.getBillDate(), "dd MMMM yyyy"));

                int total1 = 0;
                int total2 = 0;
                long locaId = 0;
                for (Location loca : listLocation) {
                    locaId = loca.getOID();
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                            + " AND "
                            + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                            + "= TO_DAYS('" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "'))"
                            + "";
                    if (locaId != 0) {
                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                + "";
                    }
                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                    total1 += dataCash;
                    row.add("" + dataCash);

                    url = apiUrl + "/credit/pinjaman/laporan-penjualan-global/" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "/" + locaId;
                    System.out.println("REST API URL : " + url);
                    obj = WebServices.getAPI("", url);
                    int dataCredit = 0;
                    try {
                        dataCredit = obj.getInt("DATA");
                    } catch (Exception e) {
                        System.out.println("Problem is : " + e.getMessage());
                        dataCredit = 0;
                    }
                    total2 += dataCredit;
                    row.add("" + dataCredit);
                }
                fTotal = 0;
                fTotal = total1 + total2;
                row.add("" + fTotal);
                listReport.add(row);

                gtotal += fTotal;
            }
            body.add(listTable);
            body.add(listHTable);
            body.add(listReport);

            footer.add("GTOTAL");

            long locaId = 0;
            for (Location loca : listLocation) {
                locaId = loca.getOID();
            whereClause = " " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                        + " AND ("
                        + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + ">= TO_DAYS('" + tanggalAwal + "')) AND "
                        + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                        + "<= TO_DAYS('" + tanggalAkhir + "'))"
                        + ")";
                if (locaId != 0) {
                    whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                            + "";
                }
                double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                footer.add("" + dataCash);
                url = apiUrl + "/credit/pinjaman/laporan-penjualan-gtotal/" + tanggalAwal + "/" + tanggalAkhir + "/" + locaId;
                System.out.println("REST API URL : " + url);
                obj = WebServices.getAPI("", url);
                int dataCredit = 0;
                try {
                    dataCredit = obj.getInt("DATA");
                } catch (Exception e) {
                    System.out.println("Problem is : " + e.getMessage());
                    dataCredit = 0;
                }
                footer.add("" + dataCredit);
            }
            footer.add("" + gtotal);

            content.add(header);
            content.add(body);
            content.add(footer);
        }
    }

    session.putValue("REPORT_SALES_BY_MONTH_CASH", content);
%>
<script language="JavaScript">
    document.location = "<%=approot%>/ReportSales?approot=<%=approot%>&jenis_laporan=<%=jenisLaporan%>&type_transaksi=<%=typeTransaksi%>&location_id=<%=oidLocation%>";
</script>
