<%-- 
    Document   : excel_report_sales
    Created on : Mar 18, 2020, 9:40:59 AM
    Author     : Regen
--%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.util.Formater"%>
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
<%    String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
    
    Vector list = new Vector();
    list = (Vector) session.getValue("DATA_PARAM");
    String[] arrLocation = (String[]) list.get(0);
    String startMonth = (String) list.get(1);
    String startDate = (String) list.get(2);
    String endMonth = (String) list.get(3);
    String endDate = (String) list.get(4);
    String jLaporan = (String) String.valueOf(list.get(5));
    String tTransaksi = (String) String.valueOf(list.get(6));
    String[] arrCategory = (String[]) list.get(7);

    int typeTransaksi = Integer.parseInt(tTransaksi);
    int jenisLaporan = Integer.parseInt(jLaporan);
    String startMonthCashCredit = startMonth;
    String endMonthCashCredit = endMonth;
    String transaksi = SrcSaleReport.reportType[SESS_LANGUAGE][typeTransaksi];
    String nameLokasi = "";
    String nameKategori = "";
    String whereClause = "";

    String inLocation = "";
    String inCategory = "";
    try {
        if (arrLocation != null) {
            //loc = PstLocation.fetchExc(oidLocation);
            for (int i = 0; i < arrLocation.length; i++){
                try {
                    long oid = Long.valueOf(arrLocation[i]);
                    Location loc = PstLocation.fetchExc(oid);
                    if (inLocation.length()>0){
                        inLocation += ","+oid;
                        nameLokasi += ","+loc.getName();
                    } else {
                        inLocation += oid;
                        nameLokasi += loc.getName();
                    }
                } catch (Exception exc){}
            }
            
        } else {
            nameLokasi = "Semua Lokasi";
        }
        
        if (arrCategory != null) {
            //loc = PstLocation.fetchExc(oidLocation);
            for (int i = 0; i < arrCategory.length; i++){
                try {
                    long oid = Long.valueOf(arrCategory[i]);
                    Category cat = PstCategory.fetchExc(oid);
                    if (inCategory.length()>0){
                        inCategory += ","+oid;
                        nameKategori += ","+cat.getName();
                    } else {
                        inCategory += oid;
                        nameKategori += cat.getName();
                    }
                } catch (Exception exc){}
            }
            
        } else {
            nameKategori = "Semua Category";
        }

    } catch (Exception e) {
    }
//    startMonth = "01 " + startMonth;
//    endMonth = "31 " + endMonth;
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
    double totalCost = 0;
    double totalGross = 0;
    double totalDisc = 0;

    double perTotalDp = 0;
    double perTotalPiutang = 0;
    double perTotalHpp = 0;
    double perTotalGross = 0;

    double amount = 0;
    double dp = 0;
    double pk = 0;
    double cost = 0;
    double hpp = 0;
    double kredit = 0;
    double piutang = 0;
    double gross = 0;
    double disc = 0;

    Vector listData = new Vector();
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

    String whereLocation = "";
    if (inLocation.length()>0){
        whereLocation = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" IN ("+inLocation+")";
    }
    
    Vector<Location> listLocation = PstLocation.list(0, 0, whereLocation, "");
    
    if (inLocation.length()>0) {
        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN ("+ inLocation+ ")";
    }
    if (inCategory.length()>0){
        whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + inCategory+ ")";
    }
    
    String location = "";
    if (inLocation.length()==0){
        location = "0";
    } else {
        location = inLocation;
    }
    String category = "";
    if (inCategory.length()==0){
        category = "0";
    } else {
        category = inCategory;
    }
    
    String namaDate = "";
    if (jenisLaporan == 1){
        if (Formater.formatDate(dateStart,"dd-MM-yyyy").equals(Formater.formatDate(dateEnd,"dd-MM-yyyy"))){
            namaDate = Formater.formatDate(dateStart,"dd-MM-yyyy");
        } else {
            namaDate = Formater.formatDate(dateStart,"dd-MM-yyyy")+"_"+Formater.formatDate(dateEnd,"dd-MM-yyyy");
        }
    } else {
        Calendar beginDate = Calendar.getInstance();
        Calendar finishDate = Calendar.getInstance();
        try {
            DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
            beginDate.setTime(formater.parse(startDate));
            finishDate.setTime(formater.parse(endDate));
        } catch (Exception exc){}
        
        if (Formater.formatDate(beginDate.getTime(),"dd-MM-yyyy").equals(Formater.formatDate(finishDate.getTime(),"dd-MM-yyyy"))){
            namaDate = Formater.formatDate(beginDate.getTime(),"dd-MM-yyyy");
        } else {
            namaDate = Formater.formatDate(beginDate.getTime(),"dd-MM-yyyy")+"_"+Formater.formatDate(finishDate.getTime(),"dd-MM-yyyy");
        }
        
    }
    String jenisTr = "";
    switch (typeTransaksi){
        case SrcSaleReport.TYPE_CASH:
            jenisTr = "Cash";
            break;
        case SrcSaleReport.TYPE_CREDIT:
            jenisTr = "Kredit";
            break;
        case SrcSaleReport.TYPE_CASH_CREDIT:
            jenisTr = "Cash_Kredit";
        break;
    }
    
    int gtotal = 0;
    int fTotal = 0;

    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "["+namaDate+"]-"+nameLokasi.replaceAll(" ", "_")+"-"+jenisTr+"-Sales_Report_Summary.xls");

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Export Excel</title>
        <style>
            .table-bordered{
                border: 2px solid #000000;
            }
            .text-center{
                text-align: center;
            }
            .table-bordered > thead > tr > th, 
            .table-bordered > tbody > tr > th, 
            .table-bordered > tfoot > tr > th, 
            .table-bordered > thead > tr > td, 
            .table-bordered > tbody > tr > td, 
            .table-bordered > tfoot > tr > td {
                border: 1px solid #f4f4f4;
            }
            table {
                border-collapse: collapse;
                border-spacing: 0;
            }
        </style>
    </head>
    <body>
        <%if (jenisLaporan == 1) {%>
        <div class="box-body">
            <div class="row">
                <div class="col-md-12">
                    <h3 class="text-center">LAPORAN PENJUALAN PER BULAN</h3>
                    <br>
                    <label class="col-md-2">Periode</label>
                    <label>: <%=startMonth%></label>
                    <label> s/d </label>
                    <label><%=endMonth%></label>
                    <br>
                    <label class="col-md-2">Transaksi</label>
                    <label>: <%=SrcSaleReport.reportType[SESS_LANGUAGE][typeTransaksi]%></label>
                    <br>
                    <label class="col-md-2">Location</label>
                    <label>: <%=nameLokasi%></label>
                    <br>
                    <label class="col-md-2">Category</label>
                    <label>: <%=nameKategori%></label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div id="penjualanBulanan">
                        <%if (typeTransaksi == SrcSaleReport.TYPE_CASH) { %>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important" border="1">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Bulan</th>
                                    <th>Sales Bruto</th>
                                    <th>Disc</th>
                                    <th>Sales Neto</th>
                                    <th>Hpp</th>
                                    <th>Gross Profit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    listData = SaleReportDocument.listCashSalesBulanan(0, 0, whereClause, "");
                                    if (!listData.isEmpty()) {
                                        for (int i = 0; i < listData.size(); i++) {
                                            BillMain bm = (BillMain) listData.get(i);
                                            String date = Formater.formatDate(bm.getBillDate(), "MMMM yyyy");
                                            amount = Math.round(bm.getAmount());
                                            disc = Math.round(bm.getDiscount());
                                            hpp = Math.round(bm.getServiceValue());
                                            total += amount;
                                            totalHpp += hpp;
                                            totalDisc += disc;
                                %>
                                <tr>
                                    <td class="text-center"><%=i + 1%></td>
                                    <td class="text-center"><%=date%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(disc, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount-disc, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber((amount-disc) - hpp, "###,###.##")%></td>
                                </tr>
                                <% }
                                } else {
                                %>
                                <tr> 
                                    <td colspan="6"> Data Tidak Ditemukan</td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td colspan="2" class="text-center" style="text-align: right"><b>Grand Total</b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total-totalDisc, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber((total-totalDisc) - totalHpp, "###,###.##")%> (<%=Math.round((((total-totalDisc) - totalHpp) / (total-totalDisc) * 100))%>%)</b></td>
                                </tr>
                            </tbody>
                        </table>
                        <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important"  border="1">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Bulan - Tahun</th>
                                    <th>Jumlah PK</th>
                                    <th>Sales Bruto</th>
                                    <th>Disc</th>
                                    <th>Sales Neto</th>
                                    <th>DP</th>
                                    <th>Piutang</th>
                                    <th>Hpp</th>
                                    <th>Gross Profit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                                    dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");
                                    dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                                    dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");
                                    url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan/" + tanggalAwal + "/" + tanggalAkhir + "/" + location+"/"+category;
                                    System.out.println("Url Export Excel : " + url);
                                    obj = WebServices.getAPI("", url);
                                    if (obj.length() > 0) {
                                        arr = obj.getJSONArray("DATA");
                                    }
                                    if (arr.length() > 0) {
                                        for (int x = 0; x < arr.length(); x++) {
                                            JSONObject data = arr.getJSONObject(x);
                                            amount = Math.round(data.optDouble("AMOUNT"));
                                            dp = Math.round(data.optDouble("DP"));
                                            pk = Math.round(data.optDouble("JUMLAH_PK"));
                                            cost = Math.round(data.optDouble("HPP"));
                                            kredit = Math.round(amount - dp);
                                            String tgl = data.optString("TANGGAL");
                                            Date date = Formater.formatDate(tgl, "yyyy-MM-dd");
                                            String newDate = Formater.formatDate(date, "MMMM yyyy");
                                            total += amount;
                                            totalDp += dp;
                                            totalPk += pk;
                                            totalKredit += kredit;
                                            totalCost += cost;

                                %>
                                <tr>
                                    <td class="text-center"><%=x + 1%></td>
                                    <td class="text-center"><%=newDate%></td>
                                    <td class="text-center"><%=data.optDouble("JUMLAH_PK")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(0, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(dp, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(kredit, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(cost, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount - cost, "###,###.##")%></td>
                                </tr>
                                <% }
                                } else {
                                %>
                                <tr> 
                                    <td colspan="10"> Data Tidak Ditemukan</td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td colspan="2" class="text-center" style="text-align: right"><b>Grand Total</b></td>
                                    <td class="text-left"><b><%=Formater.formatNumber(totalPk, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(totalKredit, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(totalCost, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(total - totalCost, "###,###.##")%> (<%=Math.round(((total - totalCost) / total * 100))%>%)</b></td>
                                </tr>
                            </tbody>
                        </table>
                        <%} else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                        <%

                            dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                            dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");

                            dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                            dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");

                        %>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important" border="1">
                            <thead>
                                <tr>
                                    <th>Cabang</th>
                                        <% for (Location loca : listLocation) {
                                        %>
                                    <th colspan="2"><%=loca.getName()%></th>
                                        <%}%>
                                    <th colspan="3">G-Total</th>
                                </tr>
                                <tr>
                                    <th>TGL</th>
                                        <%
                                            for (Location loca : listLocation) {
                                        %>
                                    <th>Cash</th>
                                    <th>Credit</th>
                                        <%}%>
                                    <th>Cash</th>
                                    <th>Credit</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    DateFormat formater = new SimpleDateFormat("dd MMM yyyy");

                                    Calendar beginMonth = Calendar.getInstance();
                                    Calendar finishMonth = Calendar.getInstance();

                                    try {
                                        beginMonth.setTime(formater.parse(startMonthCashCredit));
                                        finishMonth.setTime(formater.parse(endMonthCashCredit));
                                        finishMonth.set(Calendar.DAY_OF_MONTH, 1);
                                    } catch (Exception exc){}
                                    JSONObject objTotal = new JSONObject();
                                    double granCash = 0;
                                    double grandCredit = 0;
                                    while (beginMonth.before(finishMonth) || beginMonth.equals(finishMonth)) {
                                          String month1 = ""+(beginMonth.get(Calendar.MONTH)+1);
                                          String day1 = ""+beginMonth.getActualMinimum(Calendar.DATE);
                                          String day2 = ""+beginMonth.getActualMaximum(Calendar.DATE);
                                          if (month1.length()==1){
                                              month1 = "0"+month1;
                                          }
                                          if (day1.length()==1){
                                              day1 = "0"+day1;
                                          }
                                          if (day2.length()==1){
                                              day2 = "0"+day1;
                                          }
                                          String monthStart = beginMonth.get(Calendar.YEAR) + "-"+month1+"-"+day1;
                                          String monthEnd = beginMonth.get(Calendar.YEAR) + "-"+month1+"-"+day2;
                                          double totalCash = 0;
                                          double totalCredit = 0;
                                            %>
                                            <tr>
                                                <td><%=Formater.formatDate(beginMonth.getTime(), "MMMM yyyy")%></td>
                                            <%
                                          for (Location loca : listLocation) {
                                              long locaId = loca.getOID();
                                              whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                    + " AND ("
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + ">= TO_DAYS('" + monthStart + "')) AND "
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + "<= TO_DAYS('" + monthEnd + "'))"
                                                    + ")";

                                                  if (inCategory.length()>0){
                                                        whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + inCategory+ ")";
                                                    }

                                              whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = "+ locaId;
                                              listData = SaleReportDocument.listCashSalesBulanan(0, 0, whereClause, "");
                                              double cash = 0;
                                              if (!listData.isEmpty()) {
                                                  for (int i = 0; i < listData.size(); i++) {
                                                    BillMain bm = (BillMain) listData.get(i);
                                                    amount = Math.round(bm.getAmount());
                                                    disc = Math.round(bm.getDiscount());
                                                    hpp = Math.round(bm.getServiceValue());
                                                    cash = amount - disc;
                                                  }
                                              }

                                              url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan/" + monthStart + "/" + monthEnd + "/" + locaId+"/"+category;
                                              obj = WebServices.getAPI("", url);
                                              arr = new JSONArray();
                                              boolean lanjut = true;
                                              double credit = 0;
                                              if (obj.length() > 0) {
                                                    try {
                                                        arr = obj.getJSONArray("DATA");
                                                    } catch (Exception e) {
                                                        System.out.println("Problem is : " + e.getMessage());
                                                        lanjut = false;
                                                    }
                                                    if (lanjut) {
                                                        for (int x = 0; x < arr.length(); x++) {
                                                            JSONObject data = arr.getJSONObject(x);
                                                            amount = Math.round(data.optDouble("AMOUNT"));
                                                            dp = Math.round(data.optDouble("DP"));
                                                            pk = Math.round(data.optDouble("JUMLAH_PK"));
                                                            cost = Math.round(data.optDouble("HPP"));
                                                            credit = amount;
                                                        }
                                                    }
                                              }
                                              try {
                                                  double currCash = objTotal.optDouble(locaId+"cash",0);
                                                  double currCredit = objTotal.optDouble(locaId+"credit",0);
                                                  objTotal.put(locaId+"cash", currCash+cash);
                                                  objTotal.put(locaId+"credit", currCredit+credit);
                                              } catch (Exception exc){

                                              }
                                              totalCash += cash;
                                              totalCredit+= credit;
                                              %>
                                              <td class="text-right"><%=Formater.formatNumber(cash, "###,###.##")%></td>
                                              <td class="text-right"><%=Formater.formatNumber(credit, "###,###.##")%></td>
                                              <%
                                          }
                                          %>
                                            <td class="text-right"><%=Formater.formatNumber(totalCash, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(totalCredit, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(totalCash+totalCredit, "###,###.##")%></td>
                                            </tr>
                                            <%
                                            granCash += totalCash;
                                          grandCredit += totalCredit;
                                          beginMonth.add(Calendar.MONTH, 1);

                                    }
                                %>
                                <tr>
                                    <td class="text-right"><b>G-Total</b></td>
                                    <%
                                        for (Location loca : listLocation) {
                                    %>
                                        <td class="text-right"><b><%=Formater.formatNumber(objTotal.optDouble(loca.getOID()+"cash",0), "###,###.##")%></b></td>
                                        <td class="text-right"><b><%=Formater.formatNumber(objTotal.optDouble(loca.getOID()+"credit",0), "###,###.##")%></b></td>
                                    <%}%>
                                    <td class="text-right"><b><%=Formater.formatNumber(granCash, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(grandCredit, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(granCash+grandCredit, "###,###.##")%></b></td>
                                </tr>
                            </tbody>
                        </table>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
        <%} else {%>
        <div class="box-body">
            <div class="row">
                <div class="col-md-12">
                    <h3 class="text-center">LAPORAN PENJUALAN PER TANGGAL</h3>
                    <br>
                    <label class="col-md-2">Periode</label>
                    <label>: <%=startDate%></label>
                    <label> s/d </label>
                    <label><%=endDate%></label>
                    <br>
                    <label class="col-md-2">Transaksi</label>
                    <label>: <%=SrcSaleReport.reportType[SESS_LANGUAGE][typeTransaksi]%></label>
                    <br>
                    <label class="col-md-2">Location</label>
                    <label>: <%=nameLokasi%></label>
                    <br>
                    <label class="col-md-2">Category</label>
                    <label>: <%=nameKategori%></label>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div id="penjualanHarian">
                        <%if (typeTransaksi == SrcSaleReport.TYPE_CASH) { %>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important"  border="1">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Date</th>
                                    <th>Nama Hari</th>
                                    <th>Sales Bruto</th>
                                    <th>Disc</th>
                                    <th>Sales Neto</th>
                                    <th>Hpp</th>
                                    <th>Gross Profit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    listData = SaleReportDocument.listCashSales(0, 0, whereClause, "");
                                    if (!listData.isEmpty()) {
                                        for (int i = 0; i < listData.size(); i++) {
                                            BillMain bm = (BillMain) listData.get(i);
                                            String date = Formater.formatDate(bm.getBillDate(), "dd-MM-yyyy");
                                            amount = Math.round(bm.getAmount());
                                            hpp = Math.round(bm.getServiceValue());
                                            gross = Math.round(bm.getPaidAmount());
                                            disc= Math.round(bm.getDiscount());
                                            total += amount;
                                            totalHpp += hpp;
                                            totalGross += gross;
                                            totalDisc += disc;

                                            perTotalHpp = totalHpp / total * 100;
                                            perTotalGross = totalGross / total * 100;
                                %>
                                <tr>
                                    <td class="text-center"><%=i + 1%></td>
                                    <td class="text-center"><%=date%></td>
                                    <td class="text-center"><%=bm.getEventName()%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(disc, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(amount-disc, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber((amount-disc) - hpp, "###,###.##")%></td>
                                </tr>
                                <% }
                                } else {
                                %>
                                <tr> 
                                    <td colspan="6"> Data Tidak Ditemukan</td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td colspan="3" class="text-center">Grand Total</td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total-totalDisc, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber((total-totalDisc) - totalHpp, "###,###.##")%> (<%=Math.round((((total-totalDisc) - totalHpp) / (total-totalDisc) * 100))%>%)</b></td>
                                </tr>
                            </tbody>
                        </table>
                        <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important"  border="1">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Date</th>
                                    <th>Nama Hari</th>
                                    <th>Qty PK</th>
                                    <th>Sales Bruto</th>
                                    <th>Disc</th>
                                    <th>Sales Neto</th>
                                    <th>DP</th>
                                    <th>Piutang</th>
                                    <th>Hpp</th>
                                    <th>Gross Profit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%

                                    url = apiUrl + "/credit/pinjaman/laporan-penjualan-tanggal/" + tanggalAwal + "/" + tanggalAkhir + "/" + location+"/"+category;
                                    obj = WebServices.getAPI("", url);
                                    
                                    if (obj.length() > 0) {
                                        arr = obj.getJSONArray("DATA");
                                    }
                                    double totalPK = 0;
                                    if (arr.length() > 0) {
                                        for (int x = 0; x < arr.length(); x++) {
                                            JSONObject data = arr.getJSONObject(x);
                                            Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                                            String newDate = Formater.formatDate(date, "dd MMMM yyyy");
                                            amount = Math.round(data.optDouble("AMOUNT"));
                                            dp = Math.round(data.optDouble("DP"));
                                            piutang = Math.round(data.optDouble("PIUTANG"));
                                            hpp = Math.round(data.optDouble("HPP"));
                                            gross = Math.round(data.optDouble("GROSS"));
                                            total += amount;
                                            totalDp += dp;
                                            totalPiutang += piutang;
                                            totalHpp += hpp;
                                            totalGross += gross;
                                            totalPK += data.optDouble("JUMLAH_PK");
                                            perTotalHpp = Math.round(totalHpp / total * 100.0);
                                            perTotalDp = Math.round(totalDp / total * 100.0);
                                            perTotalPiutang = Math.round(totalPiutang / total * 100.0);
                                            perTotalGross = Math.round(totalGross / total * 100.0);
                                %>
                                <tr>
                                    <td class="text-center"><%=x + 1%></td>
                                    <td class="text-center"><%=newDate%></td>
                                    <td class="text-center"><%=data.optString("HARI")%></td>
                                    <td class="text-center"><%=data.optDouble("JUMLAH_PK")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT"), "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(0, "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT"), "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("DP"), "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("PIUTANG"), "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("HPP"), "###,###.##")%></td>
                                    <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT") - data.optDouble("HPP"), "###,###.##")%></td>
                                </tr>
                                <% }
                                } else {
                                %>
                                <tr> 
                                    <td colspan="11"> Data Tidak Ditemukan</td>
                                </tr>
                                <%}%>
                                <tr>
                                    <td colspan="3" class="text-center" style="text-align: right"><b>Grand Total</b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalPK, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalPiutang, "###,###.##")%></b></td>
                                    <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(total - totalHpp, "###,###.##")%> (<%=Math.round(((total - totalHpp) / total * 100))%>%)</b></td>
                                </tr>
                            </tbody>
                        </table>
                        <%} else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                        <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important" border="1">
                            <thead>
                                <tr>
                                    <th>Cabang</th>
                                        <%
                                            for (Location loca : listLocation) {

                                        %>
                                    <th colspan="2"><%=loca.getName()%></th>
                                        <%}%>
                                    <th colspan="3">G-Total</th>
                                </tr>
                                <tr>
                                    <th>TGL</th>
                                        <%
                                            for (Location loca : listLocation) {
                                        %>
                                    <th>Cash</th>
                                    <th>Credit</th>
                                        <%}%>
                                    <th>Cash</th>
                                    <th>Credit</th>
                                    <th>Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%

                                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

                                    Calendar beginDate = Calendar.getInstance();
                                    Calendar finishDate = Calendar.getInstance();
                                    try {
                                        beginDate.setTime(formater.parse(startDate));
                                        finishDate.setTime(formater.parse(endDate));
                                    } catch (Exception exc){}

                                    JSONObject objTotal = new JSONObject();
                                    double granCash = 0;
                                    double grandCredit = 0;

                                    String whereClauseAllLocation = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                            + " AND ("
                                            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                            + ">= TO_DAYS('" + Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd") + " 00:00:00')) AND "
                                            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                            + "<= TO_DAYS('" + Formater.formatDate(finishDate.getTime(), "yyyy-MM-dd") + " 23:59:59'))"
                                            + ")";
                                    if (inCategory.length()>0){
                                        whereClauseAllLocation += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + inCategory+ ")";
                                    }
                                    if (inLocation.length()>0) {
                                        whereClauseAllLocation += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN ("+ inLocation+ ")";
                                    }
                                    JSONObject objCash = SaleReportDocument.listCashSalesLocation(0, 0, whereClauseAllLocation, "",inLocation);

                                    location = "";
                                    if (inLocation.length()==0){
                                        location = "0";
                                    } else {
                                        location = inLocation;
                                    }
                                    category = "";
                                      if (inCategory.length()==0){
                                        category = "0";
                                    } else {
                                        category = inCategory;   
                                    }

                                    String urlAll = apiUrl + "/credit/pinjaman/laporan-penjualan-tanggal-location/" + Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd") + "/" + Formater.formatDate(finishDate.getTime(), "yyyy-MM-dd") + "/" + location+"/"+category;
                                    JSONObject objCredit = WebServices.getAPI("", urlAll);


                                    while (beginDate.before(finishDate)) {
                                        double totalCash = 0;
                                        double totalCredit = 0;
                                        if (objCash.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")) != null || objCredit.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")) != null){
                                            %>
                                            <tr>
                                            <td><%=Formater.formatDate(beginDate.getTime(), "dd-MM-yyyy")%></td>
                                           <%
                                            for (Location loca : listLocation) {
                                               String key = loca.getName().replaceAll(" ", "_");
                                               double cash = 0;
                                               obj = new JSONObject();
                                               if (objCash.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")) != null){
                                                   obj = objCash.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")).getJSONObject(0);
                                                   cash = obj.optDouble(key,0);
                                               }
                                               double credit = 0;
                                               if (objCredit.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")) != null){
                                                   obj = objCredit.optJSONArray(Formater.formatDate(beginDate.getTime(), "yyyy-MM-dd")).getJSONObject(0);
                                                   credit = obj.optDouble(key,0);
                                               }
                                               try {
                                                   double currCash = objTotal.optDouble(loca.getOID()+"cash",0);
                                                   double currCredit = objTotal.optDouble(loca.getOID()+"credit",0);
                                                   objTotal.put(loca.getOID()+"cash", currCash+cash);
                                                   objTotal.put(loca.getOID()+"credit", currCredit+credit);
                                               } catch (Exception exc){

                                               }
                                               totalCash += cash;
                                               totalCredit+= credit;
                                               %>
                                              <td class="text-right"><%=Formater.formatNumber(cash, "###,###.##")%></td>
                                              <td class="text-right"><%=Formater.formatNumber(credit, "###,###.##")%></td>
                                              <%
                                           }
                                           %>
                                            <td class="text-right"><%=Formater.formatNumber(totalCash, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(totalCredit, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(totalCash+totalCredit, "###,###.##")%></td>
                                        </tr>
                                            <%
                                        }
                                        granCash += totalCash;
                                        grandCredit += totalCredit;
                                        beginDate.add(Calendar.DATE, 1);
                                    }
                                    %>
                                    <tr>
                                        <td class="text-right"><b>G-Total</b></td>
                                        <%
                                            for (Location loca : listLocation) {
                                        %>
                                            <td class="text-right"><b><%=Formater.formatNumber(objTotal.optDouble(loca.getOID()+"cash",0), "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(objTotal.optDouble(loca.getOID()+"credit",0), "###,###.##")%></b></td>
                                        <%}%>
                                        <td class="text-right"><b><%=Formater.formatNumber(granCash, "###,###.##")%></b></td>
                                        <td class="text-right"><b><%=Formater.formatNumber(grandCredit, "###,###.##")%></b></td>
                                        <td class="text-right"><b><%=Formater.formatNumber(granCash+grandCredit, "###,###.##")%></b></td>
                                    </tr>
                            </tbody>
                        </table>
                        <%}%>
                    </div>
                </div>
            </div>
        </div>
        <%}%>
    </body>
</html>
