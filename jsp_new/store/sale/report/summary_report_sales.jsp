<%-- 
    Document   : detail_report_sales
    Created on : Mar 11, 2020, 1:46:57 PM
    Author     : Regen
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.common.entity.contact.PstContactClassAssign"%>
<%@page import="com.dimata.common.entity.contact.ContactClassAssign"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatReceive"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatReceive"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    int iCommand = FRMQueryString.requestCommand(request);
    String[] arrLocation = FRMQueryString.requestStringValues(request, "form_location");
    String[] arrCategory = FRMQueryString.requestStringValues(request, "form_category");
    String startMonth = FRMQueryString.requestString(request, "start_month");
    String endMonth = FRMQueryString.requestString(request, "end_month");
    String startDate = FRMQueryString.requestString(request, "start_date");
    String endDate = FRMQueryString.requestString(request, "end_date");
    String invoiceOn = FRMQueryString.requestString(request, "invoice_filter");
    String kreditOn = FRMQueryString.requestString(request, "kredit_filter");
    System.out.println("Kredit On || Invoice On <=> " + kreditOn + " || " + invoiceOn);
    int jenisLaporan = FRMQueryString.requestInt(request, "form_jenis_laporan");
    int typeTransaksi = FRMQueryString.requestInt(request, "form_type_transaksi");
    String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
    
    String nameLokasi = "";
    String nameKategori = "";
    boolean kreditView = false;
    boolean invoiceView = false;

    if (invoiceOn.equals("on")) {
        invoiceView = true;
    }
    if (kreditOn.equals("on")) {
        kreditView = true;
    }

    
    
    String dateStartReport = startDate;
    String dateEndReport = endDate;

    startDate = startDate + " 00:00:00";
    endDate = endDate + " 23:59:59";

    System.out.println("Kredit View || Invoice View <=> " + kreditView + " || " + invoiceView);
    Vector dataa = new Vector();
    
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
    String startMonthCashCredit = startMonth;
    String endMonthCashCredit = endMonth;
    startMonth = "01 " + startMonth;
    try {
        Date date1=new SimpleDateFormat("dd MMM yyyy").parse(startMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        endMonth = calendar.getActualMaximum(Calendar.DATE)+" " + endMonth;
    } catch (Exception exc){
        endMonth = "31 " + endMonth;
    }
    dataa.add(arrLocation);
    dataa.add(startMonth);
    dataa.add(dateStartReport);
    dataa.add(endMonth);
    dataa.add(dateEndReport);
    dataa.add(jenisLaporan);
    dataa.add(typeTransaksi);
    dataa.add(arrCategory);
    session.putValue("DATA_PARAM", dataa);
    String whereLocation = "";
    if (inLocation.length()>0){
        whereLocation = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " IN ("+inLocation+")";
    }
    Vector<Location> listLocation = PstLocation.list(0, 0, whereLocation, "");
    int gtotal = 0;
    int fTotal = 0;

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%@include file="../../../styles/plugin_component.jsp" %>
        <style>
        </style>
        <script language="javascript">
            $(document).ready(function () {

                $('#btn-kembali').click(function () {
                    window.location = "<%=approot%>/store/sale/report/list_report_sales.jsp";
                });

                $('#print-pdf').click(function () {
                    window.open("pdf_report_sales.jsp", "REPORT_SALES");
                });

                $('#export-excel').click(function () {
                    window.open("excel_report_sales.jsp", "REPORT_SALES");
                });

            });
        </script>
        <style>
            thead {
                background-color: #fbb100;
                color: white;
            }
        </style>
    </head>
    <body>
        <section class="content-header">
            <h3>Laporan<small> List</small> </h3>
            <ol class="breadcrumb">
                <li>Laporan</li>
                <li>Penjualan</li>
            </ol>
        </section>
        <section class="content">       
            <div class="box box-outline box-prochain">
                <div class="box-header with-border">
                    <div class="pull-right">
                        <!--<button type="button" class="btn btn-default" id="export-Excel"><i class="fa fa-file-archive-o"> </i> Export Excel</button>-->
<!--                        <button type="button" class="btn btn-default" id="print-pdf"><i class="fa fa-print"> </i> Print PDF</button>-->
                        <button type="button" class="btn btn-success" id="export-excel"><i class="fa fa-file"> </i> Export Excel</button>
                        <button type="button" class="btn btn-primary" id="btn-kembali"><i class="fa fa-refresh"> </i> Kembali</button>
                    </div>
                </div>
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
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
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
                                            Date dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                                            Date dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");

                                            String dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                                            String dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");

                                            String whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                    + " AND ("
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + ">= TO_DAYS('" + dateStart1 + "')) AND "
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + "<= TO_DAYS('" + dateEnd1 + "'))"
                                                    + ")";

                                            if (inLocation.length()>0) {
                                                whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN ("+ inLocation+ ")";
                                            }
                                            if (inCategory.length()>0){
                                                whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + inCategory+ ")";
                                            }
                                            double total = 0;
                                            double totalHpp = 0;
                                            double totalDisc = 0;
                                            Vector listData = SaleReportDocument.listCashSalesBulanan(0, 0, whereClause, "");
                                            if (!listData.isEmpty()) {
                                                for (int i = 0; i < listData.size(); i++) {
                                                    BillMain bm = (BillMain) listData.get(i);
                                                    String date = Formater.formatDate(bm.getBillDate(), "MMMM yyyy");
                                                    double amount = Math.round(bm.getAmount());
                                                    double disc = Math.round(bm.getDiscount());
                                                    double hpp = Math.round(bm.getServiceValue());
                                                    String start = "01 " + date;
                                                    String end = "31 " + date;

                                                    Date dateS = Formater.formatDate(start, "dd MMMM yyyy");
                                                    Date dateE = Formater.formatDate(end, "dd MMMM yyyy");

                                                    String Start1 = Formater.formatDate(dateS, "yyyy-MM-dd");
                                                    String End1 = Formater.formatDate(dateE, "yyyy-MM-dd");
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
                                            <td colspan="2" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total-totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((total-totalDisc) - totalHpp, "###,###.##")%> (<%=Math.round((((total-totalDisc) - totalHpp) / (total-totalDisc) * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
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
                                            double total = 0;
                                            double totalDp = 0;
                                            double totalPk = 0;
                                            double totalKredit = 0;
                                            double totalCost = 0;
                                            double totalGross = 0;
                                            JSONArray arr = new JSONArray();
                                            JSONArray arrChild = new JSONArray();
                                            Date dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                                            Date dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");
                                            String dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                                            String dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            String url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan/" + dateStart1 + "/" + dateEnd1 + "/" + inLocation+"/"+inCategory;
                                            JSONObject obj = WebServices.getAPI("", url);
                                            boolean lanjut = true;
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
                                                        double amount = Math.round(data.optDouble("AMOUNT"));
                                                        double dp = Math.round(data.optDouble("DP"));
                                                        double pk = Math.round(data.optDouble("JUMLAH_PK"));
                                                        double cost = Math.round(data.optDouble("HPP"));
                                                        double kredit = Math.round(amount - dp);
                                                        String tgl = data.optString("TANGGAL");
                                                        Date date = Formater.formatDate(tgl, "yyyy-MM-dd");
                                                        String newDate = Formater.formatDate(date, "MMMM yyyy");
                                                        total += amount;
                                                        totalDp += dp;
                                                        totalPk += pk;
                                                        totalKredit += kredit;
                                                        totalCost += cost;
                                                        String noKredit = "";
                                                        String Start1 = Formater.formatDate(date, "yyyy-MM-dd");
                                                        String End1 = Formater.formatDate(date, "yyyy-MM-dd");
                                        %>
                                        <tr>
                                            <td class="text-center"><%=x + 1%></td>
                                            <td class="text-center"><%=newDate%></td>
                                            <td class="text-left"><%=data.optDouble("JUMLAH_PK")%></td>
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
                                            <td colspan="2" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-left"><b><%=Formater.formatNumber(totalPk, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalKredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCost, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total - totalCost, "###,###.##")%> (<%=Math.round(((total - totalCost) / total * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%}
                                } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                                <%

                                    Date dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                                    Date dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");

                                    String dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                                    String dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");

                                %>
                                <div style="overflow-x:auto;">
                                    <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
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
                                                DateFormat formater = new SimpleDateFormat("MMM yyyy");
                                                
                                                Calendar beginMonth = Calendar.getInstance();
                                                Calendar finishMonth = Calendar.getInstance();
                                                
                                                try {
                                                    beginMonth.setTime(formater.parse(startMonthCashCredit));
                                                    finishMonth.setTime(formater.parse(endMonthCashCredit));
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
                                                      String category = "";
                                                      if (inCategory.length()==0){
                                                        category = "0";
                                                    } else {
                                                       category = inCategory;   
                                                      }
                                                      for (Location loca : listLocation) {
                                                          long locaId = loca.getOID();
                                                          String whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
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
                                                          Vector listData = SaleReportDocument.listCashSalesBulanan(0, 0, whereClause, "");
                                                          double cash = 0;
                                                          if (!listData.isEmpty()) {
                                                              for (int i = 0; i < listData.size(); i++) {
                                                                BillMain bm = (BillMain) listData.get(i);
                                                                double amount = Math.round(bm.getAmount());
                                                                double disc = Math.round(bm.getDiscount());
                                                                double hpp = Math.round(bm.getServiceValue());
                                                                cash = amount - disc;
                                                              }
                                                          }
                                                          
                                                          String url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan/" + monthStart + "/" + monthEnd + "/" + locaId+"/"+category;
                                                          JSONObject obj = WebServices.getAPI("", url);
                                                          JSONArray arr = new JSONArray();
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
                                                                        double amount = Math.round(data.optDouble("AMOUNT"));
                                                                        double dp = Math.round(data.optDouble("DP"));
                                                                        double pk = Math.round(data.optDouble("JUMLAH_PK"));
                                                                        double cost = Math.round(data.optDouble("HPP"));
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
                                </div>
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
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                                <%
                                                    if (invoiceView) {
                                                %>
                                            <th>No Invoice</th>
                                                <%
                                                    }
                                                %>
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
                                            String whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                    + " AND ("
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + ">= TO_DAYS('" + startDate + "')) AND "
                                                    + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                    + "<= TO_DAYS('" + endDate + "'))"
                                                    + ")";

                                            if (inLocation.length()>0) {
                                                whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " IN ("+ inLocation+ ")";
                                            }
                                            if (inCategory.length()>0){
                                                whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " IN (" + inCategory+ ")";
                                            }
                                            double total = 0;
                                            double totalHpp = 0;
                                            double totalGross = 0;
                                            double totalDisc = 0;
                                            double perTotalHpp = 0;
                                            double perTotalGross = 0;

                                            Vector listData = SaleReportDocument.listCashSales(0, 0, whereClause, "");
                                            if (!listData.isEmpty()) {
                                                for (int i = 0; i < listData.size(); i++) {
                                                    BillMain bm = (BillMain) listData.get(i);
                                                    String date = Formater.formatDate(bm.getBillDate(), "dd-MM-yyyy");
                                                    double amount = Math.round(bm.getAmount());
                                                    double disc= Math.round(bm.getDiscount());
                                                    double hpp = Math.round(bm.getServiceValue());
                                                    double gross = Math.round(bm.getPaidAmount());
                                                    total += amount;
                                                    totalHpp += hpp;
                                                    totalGross += gross;
                                                    totalDisc += disc;

                                                    perTotalHpp = totalHpp / (total - disc) * 100;
                                                    perTotalGross = totalGross / total * 100;
                                                    Date dateS = Formater.formatDate(date, "dd-MM-yyyy");

                                                    String Start1 = Formater.formatDate(dateS, "yyyy-MM-dd");
                                                    String whereInvoice = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                            + " AND ("
                                                            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                            + ">= TO_DAYS('" + Start1 + "')) AND "
                                                            + "(TO_DAYS( CP." + PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_DATETIME] + ") "
                                                            + "<= TO_DAYS('" + Start1 + "'))"
                                                            + ")";
                                                    Vector<BillMain> dataInvoice = SaleReportDocument.listInvoiceReport(0, 0, whereInvoice, "");
                                                    System.out.println("dataInvoice || whereInvoice :" + dataInvoice + " || " + whereInvoice);
                                                    String invoice = "";
                                                    int startCount = 0;
                                                    for (BillMain bim : dataInvoice) {
                                                        startCount++;
                                                        if (startCount != 1) {
                                                            invoice += ", \n<br/>";
//                                                            invoice += ", ";
                                                        }
                                                        invoice += bim.getInvoiceNo();
                                                    }
                                        %>
                                        <tr>
                                            <td class="text-center"><%=i + 1%></td>
                                            <%
                                                if (invoiceView) {
                                            %>
                                            <td class="text-center"><%=invoice%></td>
                                            <%
                                                }
                                            %>
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
                                            <%
                                                if (invoiceView) {
                                            %>
                                            <td colspan="7"> Data Tidak Ditemukan</td>
                                            <%
                                            } else {
                                            %>
                                            <td colspan="6"> Data Tidak Ditemukan</td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <%
                                                if (invoiceView) {
                                            %>
                                            <td colspan="4" class="text-right"><b>Grand Total</b></td>
                                            <%
                                            } else {
                                            %>
                                            <td colspan="3" class="text-right"><b>Grand Total</b></td>
                                            <%
                                                }
                                            %>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total-totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((total-totalDisc) - totalHpp, "###,###.##")%> (<%=Math.round((((total-totalDisc) - totalHpp) / (total-totalDisc) * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                                <%
                                                    if (kreditView) {
                                                %>
                                            <th>No Kredit</th>
                                                <%
                                                    }
                                                %>
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
                                            double total = 0;
                                            double totalDp = 0;
                                            double totalPiutang = 0;
                                            double totalHpp = 0;
                                            double totalGross = 0;
                                            double totalQty = 0;
                                            
                                            double perTotalDp = 0;
                                            double perTotalPiutang = 0;
                                            double perTotalHpp = 0;
                                            double perTotalGross = 0;
                                            
                                            JSONArray arr = new JSONArray();
                                            JSONArray arrChild = new JSONArray();
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            String url = apiUrl + "/credit/pinjaman/laporan-penjualan-tanggal/" + dateStartReport + "/" + dateEndReport + "/" + inLocation+"/"+inCategory;
                                            System.out.println("REST API URL : " + url);
                                            JSONObject obj = WebServices.getAPI("", url);
                                            boolean lanjut = true;
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
                                                        Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                                                        String newDate = Formater.formatDate(date, "dd MMMM yyyy");
                                                        double amount = Math.round(data.optDouble("AMOUNT"));
                                                        double dp = Math.round(data.optDouble("DP"));
                                                        double piutang = Math.round(data.optDouble("PIUTANG"));
                                                        double hpp = Math.round(data.optDouble("HPP"));
                                                        double gross = Math.round(data.optDouble("GROSS"));
                                                        total += amount;
                                                        totalDp += dp;
                                                        totalPiutang += piutang;
                                                        totalHpp += hpp;
                                                        totalGross += gross;
                                                        totalQty += data.optDouble("JUMLAH_PK");
                                                        
                                                        perTotalHpp = Math.round(totalHpp / total * 100.0);
                                                        perTotalDp = Math.round(totalDp / total * 100.0);
                                                        perTotalPiutang = Math.round(totalPiutang / total * 100.0);
                                                        perTotalGross = Math.round(totalGross / total * 100.0);

                                                        String noKredit = "";
                                                        String Start1 = Formater.formatDate(date, "yyyy-MM-dd");
                                                        String End1 = Formater.formatDate(date, "yyyy-MM-dd");
                                                        String urll = apiUrl + "/credit/pinjaman/kredit-penjualan-tanggal/" + Start1 + "/" + End1 + "/" + inLocation;
                                                        JSONObject objj = WebServices.getAPI("", urll);
                                                        System.out.println("objj : " + objj);
                                                        if (obj.length() > 0) {
                                                            try {
                                                                arrChild = objj.getJSONArray("DATA");
                                                            } catch (Exception e) {
                                                                System.out.println("Problem is : " + e.getMessage());
                                                            }
                                                        }
                                                        int startCount = 0;
                                                        for (int xx = 0; xx < arrChild.length(); xx++) {
                                                            JSONObject daataa = arrChild.getJSONObject(xx);
                                                            String dataInvoice = daataa.optString("BILL_NUMBER");
                                                            String dataKredit = daataa.optString("NO_KREDIT");
                                                            startCount++;
                                                            if (startCount != 1) {
                                                                noKredit += ", \n<br/>";
                                                            }
                                                            noKredit += dataKredit;
                                                        }
                                        %>
                                        <tr>
                                            <td class="text-center"><%=x + 1%></td>
                                            <%
                                                if (kreditView) {
                                            %>
                                            <td class="text-center"><%=noKredit%></td>
                                            <%
                                                }
                                            %>
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
                                            <%
                                                if (kreditView) {
                                            %>
                                            <td colspan="10"> Data Tidak Ditemukan</td>
                                            <%
                                            } else {
                                            %>
                                            <td colspan="9"> Data Tidak Ditemukan</td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <%
                                                if (kreditView) {
                                            %>
                                            <td colspan="4" class="text-right"><b>Grand Total</b></td>
                                            <%
                                            } else {
                                            %>
                                            <td colspan="3" class="text-right"><b>Grand Total</b></td>
                                            <%
                                                }
                                            %>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalQty, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalPiutang, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total - totalHpp, "###,###.##")%> (<%=Math.round(((total - totalHpp) / total * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%}
                                } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                                <div style="overflow-x:auto;">
                                    <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
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
                                                
                                                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                
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
                                                           JSONObject obj = new JSONObject();
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
                                </div>
                                <%}%>
                            </div>
                        </div>
                    </div>
                </div>
                <%}%>
                <div class="box-footer">

                </div>
            </div>
        </section>

    </body>
</html>
