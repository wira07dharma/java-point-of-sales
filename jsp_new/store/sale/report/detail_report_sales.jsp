<%-- 
    Document   : detail_report_sales
    Created on : Mar 11, 2020, 1:46:57 PM
    Author     : Regen
--%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
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
    
    
    startMonth = "01 " + startMonth;
    try {
        Date date1=new SimpleDateFormat("dd MMM yyyy").parse(startMonth);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        endMonth = calendar.getActualMaximum(Calendar.DATE)+" " + endMonth;
    } catch (Exception exc){
        endMonth = "31 " + endMonth;
    }
    
    Vector dataa = new Vector();
    dataa.add(arrLocation);
    dataa.add(startMonth);
    dataa.add(dateStartReport);
    dataa.add(endMonth);
    dataa.add(dateEndReport);
    dataa.add(jenisLaporan);
    dataa.add(typeTransaksi);
    dataa.add(arrCategory);
    session.putValue("DATA_PARAM", dataa);
    
    Vector<Location> listLocation = PstLocation.list(0, 0, "", "");
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
                    window.open("excel_report_sales_detail.jsp", "REPORT_SALES");
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
                                            <th>Invoice</th>
                                            <th>Sku</th>
                                            <th>Nama Barang</th>
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
                                            double totalDisc = 0;
                                            double totalCost = 0;
                                            
                                            double subTotal =0;
                                            double subTotalDisc =0;
                                            double subTotalCost =0;
                                            Vector listData = SaleReportDocument.listCashSalesBulananDetail(0, 0, whereClause, "BM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]+",BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]);
                                            if (!listData.isEmpty()) {
                                                String currDate = "";
                                                String currBill = "";
                                                int no = 0;
                                                for (int i = 0; i < listData.size(); i++) {
                                                    Vector temp = (Vector) listData.get(i);
                                                    BillMain bm = (BillMain) temp.get(0);
                                                    Billdetail det = (Billdetail) temp.get(1);
                                                    String date = Formater.formatDate(bm.getBillDate(), "MMMM yyyy");
                                                    String billNumber = bm.getInvoiceNumber();
                                                    String number = "";
                                                    double amount = Math.round(det.getTotalAmount());
                                                    double disc = Math.round(det.getDisc());
                                                    double cost = Math.round(det.getCost());
                                                    String start = "01 " + date;
                                                    String end = "31 " + date;

                                                    
                                                    if (!billNumber.equals(currBill)){
                                                        currBill = billNumber;
                                                    } else {
                                                        billNumber = "";
                                                    }
                                                    if (!date.equals(currDate)){
                                                        if (currDate.length()>0){
                                                            %>
                                                            <tr>
                                                                <td class="text-right" colspan="5"><b>Sub Total</b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotalDisc, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotal - subTotalDisc, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotalCost, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber((subTotal - subTotalDisc) - subTotalCost, "###,###.##")%>
                                                                    (<%=Math.round((((subTotal - subTotalDisc) - subTotalCost) / (subTotal - subTotalDisc) * 100))%>%)
                                                                    </b></td>
                                                            </tr>
                                                            <%
                                                            subTotal =0;
                                                            subTotalDisc =0;
                                                            subTotalCost =0;
                                                        }
                                                        currDate = date;
                                                        no++;
                                                        number = ""+no;
                                                    } else {
                                                        date = "";
                                                    }

                                                    subTotal += amount;
                                                    subTotalDisc +=disc;
                                                    subTotalCost +=cost;
                                                    Date dateS = Formater.formatDate(start, "dd MMMM yyyy");
                                                    Date dateE = Formater.formatDate(end, "dd MMMM yyyy");

                                                    String Start1 = Formater.formatDate(dateS, "yyyy-MM-dd");
                                                    String End1 = Formater.formatDate(dateE, "yyyy-MM-dd");
                                                    total += amount;
                                                    totalDisc += disc;
                                                    totalCost += cost;
                                        %>
                                        <tr>
                                            <td class="text-center"><%=number%></td>
                                            <td class="text-center"><%=date%></td>
                                            <td class="text-left"><%=billNumber%></td>
                                            <td class="text-left"><%=det.getSku()%></td>
                                            <td class="text-left"><%=det.getItemName()%></td>
                                            <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(disc, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(amount - disc, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(cost, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber((amount - disc) - cost, "###,###.##")%></td>
                                        </tr>
                                        <% }
                                        } else {
                                        %>
                                        <tr> 
                                            <td colspan="6"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-right" colspan="5"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal - subTotalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalCost, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((subTotal - subTotalDisc) - subTotalCost, "###,###.##")%>
                                                (<%=Math.round((((subTotal - subTotalDisc) - subTotalCost) / (subTotal - subTotalDisc) * 100))%>%)
                                                </b></td>
                                        </tr>
                                        <tr>
                                            <td colspan="5" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total - totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalCost, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((total - totalDisc) - totalCost, "###,###.##")%>
                                                (<%=Math.round((((total - totalDisc) - totalCost) / (total - totalDisc) * 100))%>%)
                                                </b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Bulan - Tahun</th>
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
                                            double totalPk = 0;
                                            double totalKredit = 0;
                                            double totalHpp = 0;
                                            double totalGross = 0;
                                            double subTotal = 0;
                                            double subTotalDp = 0;
                                            double subTotalKredit = 0;
                                            double subTotalHpp = 0;
                                            double subTotalGross = 0;
                                            
                                            int jmlPk = 0;
                                            int jmlPkSub = 0;
                                            int qtyBarang = 0;
                                            int qtyBarangSub = 0;
                                            
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
                                            String url = apiUrl + "/credit/pinjaman/laporan-penjualan-bulanan-detail/" + dateStart1 + "/" + dateEnd1 + "/" + inLocation+"/"+inCategory;
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
                                                    String currDate = "";
                                                    String currPK = "";
                                                    String currHari = "";
                                                    
                                                    
                                                    int no = 0;
                                                    int rowspan = 1;
                                                    double hppTotal = 0;
                                                    for (int x = 0; x < arr.length(); x++) {
                                                        JSONObject data = arr.getJSONObject(x);
                                                        double amount = Math.round(data.optDouble("AMOUNT"));
                                                        double dp = Math.round(data.optDouble("DP"));
                                                        double pk = Math.round(data.optDouble("JUMLAH_PK"));
                                                        double hpp = Math.round(data.optDouble("HPP"));
                                                        double gross = Math.round(data.optDouble("GROSS"));
                                                        double kredit = Math.round(amount - dp);
                                                        String tgl = data.optString("TANGGAL");
                                                        String noPk = data.optString("NO_KREDIT", "");
                                                        String number = "";
                                                        Date date = Formater.formatDate(tgl, "yyyy-MM-dd");
                                                        String newDate = Formater.formatDate(date, "MMMM yyyy");
                                                        
                                                        
                                                        
                                                        if (!tgl.equals(currHari)){
                                                            currHari = tgl;
                                                        } else {
                                                            tgl = "";
                                                        }
                                                        if (!newDate.equals(currDate)){
                                                            if (currDate.length()>0){
                                                                %>
                                                                <tr>
                                                                    <td class="text-right" colspan="2"><b>Sub Total</b></td>
                                                                    <td class="text-right"><b><%=jmlPkSub%></b></td>
                                                                    <td class="text-right">&nbsp;</td>
                                                                    <td class="text-right">&nbsp;</td>
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
                                                                subTotalHpp = 0;
                                                                subTotalKredit = 0;
                                                                jmlPkSub = 0;
                                                                qtyBarangSub = 0;
                                                            }
                                                            no++;
                                                            currDate = newDate;
                                                            number = ""+no;
                                                        } else {
                                                            newDate = "";
                                                        }
                                                        hppTotal += hpp;
                                                        if (!noPk.equals(currPK)){
                                                            currPK = noPk;
                                                            jmlPk++;
                                                            jmlPkSub++;
                                                            %>
                                                                <tr>
                                                                    <td class="text-center"><%=number%></td>
                                                                    <td class="text-center"><%=newDate%></td>
                                                                    <td class="text-left"><%=data.optString("NO_KREDIT","")%></td>
                                                                    <td class="text-left"><%=data.optString("SKU","")%></td>
                                                                    <td class="text-left"><%=data.optString("NAME","")%></td>
                                                                    <td class="text-left"><%=data.optString("QTY","0")%></td>
                                                                    <td class="text-right" style="vertical-align: middle"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(0, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(dp, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(kredit, "###,###.##")%></td>
                                                                    <td class="text-right" rowspan="<%=data.optString("ROW","1")%>" style="vertical-align: middle"><%=Formater.formatNumber(gross, "###,###.##")%></td>
                                                                </tr>
                                        <%
                                                        subTotal += amount;
                                                        subTotalDp += dp;
                                                        subTotalKredit += kredit;
                                                        subTotalHpp += hpp;
                                                        subTotalGross += gross;
                                                        
                                                        total += amount;
                                                        totalDp += dp;
                                                        totalPk += pk;
                                                        totalKredit += kredit;
                                                        totalHpp += hpp;
                                                        totalGross += gross;
                                                        hppTotal = 0;
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
                                                        
                                                        String noKredit = "";
                                                        String Start1 = Formater.formatDate(date, "yyyy-MM-dd");
                                                        String End1 = Formater.formatDate(date, "yyyy-MM-dd");

                                                        
                                        }
                                        } else {
                                        %>
                                        <tr> 
                                            <td colspan="6"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-right" colspan="2"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=jmlPkSub%></b></td>
                                            <td class="text-right">&nbsp;</td>
                                            <td class="text-right">&nbsp;</td>
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
                                            <td colspan="2" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=jmlPk%></b></td>
                                            <td class="text-right">&nbsp;</td>
                                            <td class="text-right">&nbsp;</td>
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
                                </table>
                                <%}
                                } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                                <%

                                    Date dateStart = Formater.formatDate(startMonth, "dd MMMM yyyy");
                                    Date dateEnd = Formater.formatDate(endMonth, "dd MMMM yyyy");

                                    String dateStart1 = Formater.formatDate(dateStart, "yyyy-MM-dd");
                                    String dateEnd1 = Formater.formatDate(dateEnd, "yyyy-MM-dd");

                                %>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>Cabang</th>
                                                <%                                                    for (Location loca : listLocation) {
                                                %>
                                            <th colspan="2"><%=loca.getName()%></th>
                                                <%}%>
                                            <th rowspan="2">Total</th>
                                        </tr>
                                        <tr>
                                            <th>TGL</th>
                                                <%
                                                    for (Location loca : listLocation) {
                                                %>
                                            <th>Cash</th>
                                            <th>Credit</th>
                                                <%}%>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            String whereClause = "" + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                    + " AND ("
                                                    + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                                                    + ">= TO_DAYS('" + dateStart1 + "')) AND "
                                                    + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                                                    + "<= TO_DAYS('" + dateEnd1 + "'))"
                                                    + ")";

                                            Vector listData = PstBillMain.list(0, 0, whereClause + " GROUP BY DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ")", "");
                                            for (int i = 0; i < listData.size(); i++) {
                                                BillMain bm = (BillMain) listData.get(i);

                                        %>
                                        <tr>
                                            <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "dd MMMM yyyy")%></td>
                                            <%
                                                int total1 = 0;
                                                int total2 = 0;
                                                long locaId = 0;
                                                for (Location loca : listLocation) {
                                                    locaId = loca.getOID();
                                                    whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                            + " AND "
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE] + ") "
                                                            + "= TO_DAYS('" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "'))"
                                                            + "";
                                                    if (locaId != 0) {
                                                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                                                + "";
                                                    }
                                                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCash, "###,###.##")%></td>
                                            <%
                                                total1 += dataCash;
                                                String url = apiUrl + "/credit/pinjaman/laporan-penjualan-global/" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "/" + locaId;
                                                System.out.println("REST API URL : " + url);
                                                JSONObject obj = WebServices.getAPI("", url);
                                                double dataCredit = 0;
                                                try {
                                                    dataCredit = obj.getInt("DATA");
                                                } catch (Exception e) {
                                                    System.out.println("Problem is : " + e.getMessage());
                                                    dataCredit = 0;
                                                }
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCredit, "###,###.##")%></td>
                                            <% total2 += dataCredit; %>
                                            <%}%>
                                            <td class="text-center"><%=Formater.formatNumber(total1 + total2, "###,###.##")%></td>
                                            <%
                                                fTotal = total1 + total2;
                                                gtotal += fTotal;
                                            %>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-center">GTOTAL</td>
                                            <%
                                                long locaId = 0;
                                                for (Location loca : listLocation) {
                                                    locaId = loca.getOID();
                                                    whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                            + " AND ("
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                            + ">= TO_DAYS('" + dateStart1 + "')) AND "
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                            + "<= TO_DAYS('" + dateEnd1 + "'))"
                                                            + ")";
                                                    if (locaId != 0) {
                                                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                                                + "";
                                                    }
                                                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCash, "###,###.##")%></td>
                                            <%
                                                Date dateStart3 = Formater.formatDate(startMonth, "dd MMMM yyyy"); 
                                                Date dateEnd3 = Formater.formatDate(endMonth, "dd MMMM yyyy"); 

                                                String dateStart2 = Formater.formatDate(dateStart3, "yyyy-MM-dd");
                                                String dateEnd2 = Formater.formatDate(dateEnd3, "yyyy-MM-dd");
                                                String url = apiUrl + "/credit/pinjaman/laporan-penjualan-gtotal/" + dateStart2 + "/" + dateEnd2 + "/" + locaId;
                                                System.out.println("REST API URL : " + url);
                                                JSONObject obj = WebServices.getAPI("", url);
                                                int dataCredit = 0;
                                                try {
                                                    dataCredit = obj.getInt("DATA");
                                                } catch (Exception e) {
                                                    System.out.println("Problem is : " + e.getMessage());
                                                    dataCredit = 0;
                                                }
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCredit, "###,###.##")%></td>
                                            <%}%>
                                            <td class="text-center"><%=Formater.formatNumber(gtotal, "###,###.##")%></td>
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
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Tanggal</th>
                                            <th>Hari</th>
                                            <th>Invoice Number</th>
                                            <th>SKU</th>
                                            <th>Nama Barang</th>
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
                                            double totalDisc = 0;
                                            double totalGross = 0;
                                            double perTotalHpp = 0;
                                            double perTotalGross = 0;

                                            double subTotal =0;
                                            double subTotalHpp = 0;
                                            double subTotalDisc = 0;
                                            double subTotalGross = 0;
                                            Vector listData = SaleReportDocument.listCashSalesDetail(0, 0, whereClause, "BM."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_DATE]+",BM."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]);
                                            if (!listData.isEmpty()) {
                                                String currDate = "";
                                                String currBill = "";
                                                String currHari = "";
                                                int no = 0;
                                                for (int i = 0; i < listData.size(); i++) {
                                                    Vector temp = (Vector) listData.get(i);
                                                    BillMain bm = (BillMain) temp.get(0);
                                                    Billdetail det = (Billdetail) temp.get(1);
                                                    String date = Formater.formatDate(bm.getBillDate(), "dd-MM-yyyy");
                                                    String hari = bm.getEventName();
                                                    String billNumber = bm.getInvoiceNumber();
                                                    String number = "";
                                                    
                                                    double amount = Math.round(det.getItemPrice());
                                                    double hpp = Math.round(det.getCost());
                                                    double disc = Math.round(det.getDisc());
                                                    double gross = Math.round(det.getTotalAmount());
                                                    total += amount;
                                                    totalHpp += hpp;
                                                    totalDisc += disc;
                                                    totalGross += gross;
                                                    
                                                    if (!hari.equals(currHari)){
                                                        currHari = hari;
                                                    } else {
                                                        hari = "";
                                                    }
                                                    if (!billNumber.equals(currBill)){
                                                        currBill = billNumber;
                                                    } else {
                                                        billNumber = "";
                                                    }
                                                    if (!date.equals(currDate)){
                                                        if (currDate.length()>0){
                                                            %>
                                                            <tr>
                                                                <td class="text-right" colspan="6"><b>Sub Total</b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotalDisc, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotal - subTotalDisc, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                                                <td class="text-right"><b><%=Formater.formatNumber((subTotal - subTotalDisc) - subTotalHpp, "###,###.##")%> (<%=Math.round((((subTotal - subTotalDisc) - subTotalHpp) / (subTotal - subTotalDisc) * 100))%>%)</b></td>
                                                            </tr>
                                                            <%
                                                            subTotal =0;
                                                            subTotalHpp = 0;
                                                            subTotalGross = 0;
                                                            subTotalDisc = 0;
                                                        }
                                                        currDate = date;
                                                        no++;
                                                        number = ""+no;
                                                    } else {
                                                        date = "";
                                                    }

                                                    subTotal += amount;
                                                    subTotalHpp += hpp;
                                                    subTotalGross += gross;
                                                    subTotalDisc += disc;
                                                    
                                                    perTotalHpp = totalHpp / total * 100;
                                                    perTotalGross = totalGross / total * 100;
                                                    Date dateS = Formater.formatDate(date, "dd-MM-yyyy");
                                        %>
                                        <tr>
                                            <td class="text-center"><%=number%></td>
                                            <td class="text-center"><%=date%></td>
                                            <td class="text-center"><%=hari%></td>
                                            <td class="text-left"><%=billNumber%></td>
                                            <td class="text-left"><%=det.getSku()%></td>
                                            <td class="text-left"><%=det.getItemName()%></td>
                                            <td class="text-right"><%=Formater.formatNumber(amount, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(disc, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(amount - disc, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(hpp, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber((amount - disc) - hpp, "###,###.##")%></td>
                                        </tr>
                                        <% }
                                        } else {
                                        %>
                                        <tr> 
                                            <td colspan="10"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-right" colspan="6"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal - subTotalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((subTotal - subTotalDisc) - subTotalHpp, "###,###.##")%> (<%=Math.round((((subTotal - subTotalDisc) - subTotalHpp) / (subTotal - subTotalDisc) * 100))%>%)</b></td>
                                        </tr>
                                        <tr>
                                            <td colspan="6" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total - totalDisc, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber((total - totalDisc) - totalHpp, "###,###.##")%> (<%=Math.round((((total - totalDisc) - totalHpp) / (total - totalDisc) * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                </table>
                                <%} else if (typeTransaksi == SrcSaleReport.TYPE_CREDIT) {%>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Date</th>
                                            <th>Nama Hari</th>
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
                                            String url = apiUrl + "/credit/pinjaman/laporan-penjualan-tanggal-detail/" + dateStartReport + "/" + dateEndReport + "/" + inLocation+"/"+inCategory;
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
                                                    String currDate = "";
                                                    String currPK = "";
                                                    String currHari = "";
                                                    
                                                    
                                                    int no = 0;
                                                    int rowspan = 1;
                                                    for (int x = 0; x < arr.length(); x++) {
                                                        JSONObject data = arr.getJSONObject(x);
                                                        Date date = Formater.formatDate(data.optString("TANGGAL"), "yyyy-MM-dd");
                                                        String newDate = Formater.formatDate(date, "dd MMMM yyyy");
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
                                                        if (!newDate.equals(currDate)){
                                                            if (currDate.length()>0){
                                                                %>
                                                                <tr>
                                                                    <td class="text-right" colspan="3"><b>Sub Total</b></td>
                                                                    <td class="text-right"><b><%=jmlPkSub%></b></td>
                                                                    <td class="text-right">&nbsp;</td>
                                                                    <td class="text-right">&nbsp;</td>
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
                                                            currDate = newDate;
                                                            number = ""+no;
                                                        } else {
                                                            newDate = "";
                                                        }
                                                        if (!noPk.equals(currPK)){
                                                            currPK = noPk;
                                                            jmlPk++;
                                                            jmlPkSub++;
                                                            %>
                                                                <tr>
                                                                    <td class="text-center"><%=number%></td>
                                                                    <td class="text-center"><%=newDate%></td>
                                                                    <td class="text-center"><%=hari%></td>
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
                                            <td class="text-right" colspan="3"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=jmlPkSub%></b></td>
                                            <td class="text-right">&nbsp;</td>
                                            <td class="text-right">&nbsp;</td>
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
                                            <td colspan="3" class="text-right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=jmlPk%></b></td>
                                            <td class="text-right">&nbsp;</td>
                                            <td class="text-right">&nbsp;</td>
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
                                </table>
                                <%}
                                } else if (typeTransaksi == SrcSaleReport.TYPE_CASH_CREDIT) {%>
                                <table class="table table-striped table-bordered" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>Cabang</th>
                                                <%
                                                    for (Location loca : listLocation) {

                                                %>
                                            <th colspan="2"><%=loca.getName()%></th>
                                                <%}%>
                                            <th rowspan="2">Total</th>
                                        </tr>
                                        <tr>
                                            <th>TGL</th>
                                                <%
                                                    for (Location loca : listLocation) {
                                                %>
                                            <th>Cash</th>
                                            <th>Credit</th>
                                                <%}%>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            String whereClause = "" + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                    + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                    + " AND ("
                                                    + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                    + ">= TO_DAYS('" + startDate + "')) AND "
                                                    + "(TO_DAYS(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                    + "<= TO_DAYS('" + endDate + "'))"
                                                    + ")";

                                            Vector listData = PstBillMain.list(0, 0, whereClause + " GROUP BY DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ")", "");
                                            for (int i = 0; i < listData.size(); i++) {
                                                BillMain bm = (BillMain) listData.get(i);

                                        %>
                                        <tr>
                                            <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "dd MMMM yyyy")%></td>
                                            <%
                                                int total1 = 0;
                                                int total2 = 0;
                                                long locaId = 0;
                                                for (Location loca : listLocation) {
                                                    locaId = loca.getOID();
                                                    whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                            + " AND "
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                            + "= TO_DAYS('" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "'))"
                                                            + "";
                                                    if (locaId != 0) {
                                                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                                                + "";
                                                    }
                                                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCash, "###,###.##")%></td>
                                            <% total1 += dataCash;
                                                String url = apiUrl + "/credit/pinjaman/laporan-penjualan-global/" + Formater.formatDate(bm.getBillDate(), "yyyy-MM-dd") + "/" + locaId;
                                                System.out.println("REST API URL : " + url);
                                                JSONObject obj = WebServices.getAPI("", url);
                                                int dataCredit = 0;
                                                try {
                                                    dataCredit = obj.getInt("DATA");
                                                } catch (Exception e) {
                                                    System.out.println("Problem is : " + e.getMessage());
                                                    dataCredit = 0;
                                                }
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCredit, "###,###.##")%></td>
                                            <% total2 += dataCredit; %>
                                            <%}%>
                                            <td class="text-center"><%=Formater.formatNumber(total1 + total2, "###,###.##")%></td>
                                            <%
                                                fTotal = total1 + total2;
                                                gtotal += fTotal;
                                            %>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-center">GTOTAL</td>
                                            <%
                                                long locaId = 0;
                                                for (Location loca : listLocation) {
                                                    locaId = loca.getOID();
                                                    whereClause = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (4, 5)"
                                                            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_AMOUNT] + " <> 0"
                                                            + " AND ("
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                            + ">= TO_DAYS('" + startDate + "')) AND "
                                                            + "(TO_DAYS( BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") "
                                                            + "<= TO_DAYS('" + endDate + "'))"
                                                            + ")";
                                                    if (locaId != 0) {
                                                        whereClause += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + locaId
                                                                + "";
                                                    }
                                                    double dataCash = SaleReportDocument.listGlobalSale(0, 0, whereClause, "");
                                            %>
                                            <td class="text-center"><<%=Formater.formatNumber(dataCash, "###,###.##")%></td>
                                            <%

                                                String url = apiUrl + "/credit/pinjaman/laporan-penjualan-gtotal/" + dateStartReport + "/" + dateEndReport + "/" + locaId;
                                                System.out.println("REST API URL : " + url);
                                                JSONObject obj = WebServices.getAPI("", url);
                                                int dataCredit = 0;
                                                try {
                                                    dataCredit = obj.getInt("DATA");
                                                } catch (Exception e) {
                                                    System.out.println("Problem is : " + e.getMessage());
                                                    dataCredit = 0;
                                                }
                                            %>
                                            <td class="text-center"><%=Formater.formatNumber(dataCredit, "###,###.##")%></td>
                                            <%}%>
                                            <td class="text-center"><%=Formater.formatNumber(gtotal, "###,###.##")%></td>
                                        </tr>
                                    </tbody>
                                </table>

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
