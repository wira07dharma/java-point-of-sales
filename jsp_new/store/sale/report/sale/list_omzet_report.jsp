<%-- 
    Document   : list_omzet_report
    Created on : 25 Jul 20, 11:35:47
    Author     : gndiw
--%>

<%@page import="com.dimata.posbo.report.sale.SaleReportDocument"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONObject"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ include file = "../../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../../main/checkuser.jsp" %>
<%    int iCommand = FRMQueryString.requestCommand(request);
    String[] arrLocation = FRMQueryString.requestStringValues(request, "form_location");
    String[] arrCategory = FRMQueryString.requestStringValues(request, "form_category");
    String startMonth = FRMQueryString.requestString(request, "start_month");
    String endMonth = FRMQueryString.requestString(request, "end_month");
    String startDate = FRMQueryString.requestString(request, "start_date");
    String endDate = FRMQueryString.requestString(request, "end_date");
    String invoiceOn = FRMQueryString.requestString(request, "invoice_filter");
    String kreditOn = FRMQueryString.requestString(request, "kredit_filter");
    int jenisReport = FRMQueryString.requestInt(request, "jenis_report");
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
    dataa.add(jenisReport);
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
        <%@include file="../../../../styles/plugin_component.jsp" %>
        <style>
        </style>
        <script language="javascript">
            $(document).ready(function () {

                $('#btn-kembali').click(function () {
                    window.location = "<%=approot%>/store/sale/report/sale/src_omzet_report.jsp";
                });

                $('#print-pdf').click(function () {
                    window.open("pdf_report_sales.jsp", "REPORT_SALES");
                });

                $('#export-excel').click(function () {
                    window.open("excel_omzet_report.jsp", "REPORT_SALES");
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
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <h3 class="text-center">Laporan Omzet Cash dan Kredit</h3>
                            <br>
                            <label class="col-md-2">Periode</label>
                            <label>: <%=startDate%></label>
                            <label> s/d </label>
                            <label><%=endDate%></label>
                            <br>
                            <label class="col-md-2">Location</label>
                            <label>: <%=nameLokasi%></label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div id="penjualanHarian">
                            <% if (jenisReport == 0) { %>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Kode Cabang</th>
                                            <th>Nama Cabang</th>
                                            <th>Total Omzet</th>
                                            <th>Total HPP</th>
                                            <th>Gross Profit</th>
                                            <th>% Gross Profit</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td colspan="7"><b>CASH</b></td>
                                        </tr>
                                        <%
                                            double grandTotal = 0;
                                            double grandHpp = 0;
                                            
                                            double totalCash = 0;
                                            double totalHppCash = 0;
                                            JSONArray arrCash = new JSONArray();
                                            JSONObject objCash = SaleReportDocument.objReportOmzet(dateStartReport,dateEndReport,inLocation);
                                            boolean lanjutCash = true;
                                            if (objCash.length() > 0) {
                                                try {
                                                    arrCash = objCash.getJSONArray("DATA");
                                                } catch (Exception e) {
                                                    System.out.println("Problem is : " + e.getMessage());
                                                    lanjutCash = false;
                                                }
                                                if (lanjutCash) {
                                                    for (int x = 0; x < arrCash.length(); x++) {
                                                        JSONObject data = arrCash.getJSONObject(x);
                                                        Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                                                        String newDate = Formater.formatDate(date, "dd MMMM yyyy");
                                                        double omzet = Math.round(data.optDouble("OMZET"));
                                                        double hpp = data.optDouble("HPP",0);
                                                        totalCash += omzet;
                                                        totalHppCash += hpp;
                                                        grandTotal += omzet;
                                                        grandHpp += hpp;
                                        %>
                                        <tr>
                                            <td class="text-left"><%=x + 1%></td>
                                            <td class="text-left"><%=data.optString("CODE")%></td>
                                            <td class="text-left"><%=data.optString("NAME")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("OMZET"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(omzet-hpp, "###,###.##")%></td>
                                            <td class="text-right"><%=Math.round(((omzet-hpp) / omzet * 100))%>%</td>
                                        </tr>
                                        <% } %>
                                        <tr>
                                            <td colspan="3" class="text-right"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCash, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHppCash, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCash-totalHppCash, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Math.round(((totalCash-totalHppCash) / totalCash * 100))%>%</b></td>
                                        </tr>
                                        <%} else {
                                        %>
                                        <tr> 
                                            <td colspan="7"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}
                                            } else {%>
                                        <tr> 
                                            <td colspan="7"> Data Tidak Ditemukan</td>
                                        </tr>    
                                        <%}%>
                                        <tr>
                                            <td colspan="7"><b>Penjualan Dengan Sistem Kemudahan/Kredit</b></td>
                                        </tr>
                                        <%
                                            double totalCredit = 0;
                                            double totalHppCredit = 0;
                                            JSONArray arr = new JSONArray();
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            String url = apiUrl + "/report/omzet-web/" + dateStartReport + "/" + dateEndReport + "/" + inLocation;
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
                                                        double omzet = Math.round(data.optDouble("OMZET"));
                                                        double hpp = data.optDouble("HPP",0);
                                                        totalCredit += omzet;
                                                        totalHppCredit += hpp;
                                                        grandTotal += omzet;
                                                        grandHpp += hpp;

                                        %>
                                        <tr>
                                            <td class="text-left"><%=x + 1%></td>
                                            <td class="text-left"><%=data.optString("CODE")%></td>
                                            <td class="text-left"><%=data.optString("NAME")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("OMZET"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(omzet-hpp, "###,###.##")%></td>
                                            <td class="text-right"><b><%=Math.round(((omzet-hpp) / omzet * 100))%>%</b></td>
                                        </tr>
                                        <% } %>
                                        <tr>
                                            <td colspan="3" class="text-right"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHppCredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCredit-totalHppCredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Math.round(((totalCredit-totalHppCredit) / totalCredit * 100))%>%</b></td>
                                        </tr>
                                        <%} else {
                                        %>
                                        <tr> 
                                            <td colspan="7"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}
                                            } else {%>
                                        <tr> 
                                            <td colspan="7"> Data Tidak Ditemukan</td>
                                        </tr>    
                                        <%}%>
                                        <tr>
                                            <td colspan="3" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(grandTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(grandHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(grandTotal-grandHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Math.round(((grandTotal-grandHpp) / grandTotal * 100))%>%</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                            <% } else { %>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Marketing</th>
                                            <th>No PK</th>
                                            <th>SKU</th>
                                            <th>Nama Barang</th>
                                            <th>Qty</th>
                                            <th>Hpp</th>
                                            <th>Sales Bruto</th>
                                            <th>Disc</th>
                                            <th>Sales Neto</th>
                                            <th>DP</th>
                                            <th>Piutang</th>
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
                                            double totalKredit = 0;
                                            double subTotalKredit = 0;
                                            double perTotalDp = 0;
                                            double perTotalPiutang = 0;
                                            double perTotalHpp = 0;
                                            double perTotalGross = 0;

                                            double subTotal = 0;
                                            double subTotalDp = 0;
                                            double subTotalPiutang = 0;
                                            double subTotalHpp = 0;
                                            double subTotalGross = 0;

                                            int jmlPk = 0;
                                            int jmlPkSub = 0;
                                            int qtyBarang = 0;
                                            int qtyBarangSub = 0;

                                            JSONArray arr = new JSONArray();
                                            JSONArray arrChild = new JSONArray();
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            String url = apiUrl + "/credit/pinjaman/top-sale-marketing-detail/" + dateStartReport + "/" + dateEndReport + "/" + inLocation+"/"+inCategory;
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
                                                    String currName = "";
                                                    String currPK = "";
                                                    String currHari = "";


                                                    int no = 0;
                                                    int rowspan = 1;
                                                    for (int x = 0; x < arr.length(); x++) {
                                                        JSONObject data = arr.getJSONObject(x);
                                                        Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                                                        String newName = data.optString("FULL_NAME","");
                                                        String noPk = data.optString("NO_KREDIT", "");
                                                        String hari = data.optString("HARI", "");
                                                        String number = "";
                                                        double amount = Math.round(data.optDouble("AMOUNT"));
                                                        double dp = Math.round(data.optDouble("DP"));
                                                        double piutang = Math.round(data.optDouble("PIUTANG"));
                                                        double hpp = Math.round(data.optDouble("HPP"));
                                                        double gross = Math.round(data.optDouble("GROSS"));
                                                        double kredit = Math.round(amount - dp);



                                                        if (!hari.equals(currHari)){
                                                            currHari = hari;
                                                        } else {
                                                            hari = "";
                                                        }
                                                        if (!newName.equals(currName)){
                                                            if (currName.length()>0){
                                                                %>
                                                                <tr>
                                                                    <td class="text-right" colspan="5"><b>Sub Total</b></td>
                                                                    <td class="text-right"><b><%=qtyBarangSub%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalDp, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalKredit, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalGross, "###,###.##")%> (<%=Math.round(((subTotalGross) / subTotal * 100))%>%)</b></td>
                                                                </tr>
                                                                <%
                                                                subTotal = 0;
                                                                subTotalDp = 0;
                                                                subTotalPiutang = 0;
                                                                subTotalHpp = 0;
                                                                subTotalGross = 0;
                                                                subTotalKredit = 0;
                                                                qtyBarangSub = 0;
                                                            }
                                                            no++;
                                                            currName = newName;
                                                            number = ""+no;
                                                        } else {
                                                            newName = "";
                                                        }
                                                        if (!noPk.equals(currPK)){
                                                            currPK = noPk;
                                                            jmlPk++;
                                                            jmlPkSub++;
                                                            %>
                                                                <tr>
                                                                    <td class="text-center"><%=number%></td>
                                                                    <td class="text-left"><%=newName%></td>
                                                                    <td class="text-left"><%=data.optString("NO_KREDIT","")%></td>
                                                                    <td class="text-left"><%=data.optString("SKU","")%></td>
                                                                    <td class="text-left"><%=data.optString("NAME","")%></td>
                                                                    <td class="text-left"><%=data.optString("QTY","")%></td>
                                                                    <td class="text-right" style="vertical-align: middle"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(0, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(dp, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(kredit, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(gross, "###,###.##")%></td>
                                                                </tr>
                                        <%
                                                        total += amount;
                                                        totalDp += dp;
                                                        totalPiutang += piutang;
                                                        totalHpp += hpp;
                                                        totalGross += gross;
                                                        totalKredit += kredit;

                                                        perTotalHpp = Math.round(totalHpp / total * 100.0);
                                                        perTotalDp = Math.round(totalDp / total * 100.0);
                                                        perTotalPiutang = Math.round(totalPiutang / total * 100.0);
                                                        perTotalGross = Math.round(totalGross / total * 100.0);

                                                        subTotal += amount;
                                                        subTotalDp += dp;
                                                        subTotalPiutang += piutang;
                                                        subTotalHpp += hpp;
                                                        subTotalGross += gross;
                                                        subTotalKredit += kredit;
                                                        } else {
                                                            rowspan++;
                                                            subTotalHpp += hpp;
                                                            totalHpp += hpp;
                                                            %>
                                                                <tr>
                                                                    <td class="text-center"></td>
                                                                    <td class="text-center"></td>
                                                                    <td class="text-left"></td>
                                                                    <td class="text-left"><%=data.optString("SKU","")%></td>
                                                                    <td class="text-left"><%=data.optString("NAME","")%></td>
                                                                    <td class="text-left"><%=data.optString("QTY","")%></td>
                                                                    <td class="text-right" style="vertical-align: middle"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                                                </tr>
                                                            <%
                                                        }

                                                        qtyBarangSub += data.optInt("QTY",0);
                                                        qtyBarang += data.optInt("QTY",0);

                                         }
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
                                            <td class="text-right" colspan="5"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=qtyBarangSub%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalKredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalGross, "###,###.##")%> (<%=Math.round((subTotalGross / subTotal * 100))%>%)</b></td>
                                        </tr>
                                        <tr>
                                            <td colspan="5" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=qtyBarang%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalKredit, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalGross, "###,###.##")%> (<%=Math.round((totalGross / total * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                    <% } %>
                                </table>
                            <% } %>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-footer">

                </div>
            </div>
        </section>

    </body>
</html>
