<%-- 
    Document   : excel_sales_marketing
    Created on : 13 Jul 20, 16:35:26
    Author     : gndiw
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
<%@ include file = "../../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT);%>
<%@ include file = "../../../../main/checkuser.jsp" %>
<!DOCTYPE html>
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
    int jenisReport = (Integer) list.get(8);

    int typeTransaksi = Integer.parseInt(tTransaksi);
    int jenisLaporan = Integer.parseInt(jLaporan);
    String startMonthCashCredit = startMonth;
    String endMonthCashCredit = endMonth;
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
    
    int gtotal = 0;
    int fTotal = 0;

    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "["+namaDate+"]-"+nameLokasi.replaceAll(" ", "_")+"-Top_Sales_Marketing.xls");

%>
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
        <div class="box-body">
            <div class="row">
                <div class="col-md-12">
                    <h3 class="text-center">TOP SALE PER MARKETING <%=(jenisReport == 0 ? "SUMMARY" : "DETAIL")%></h3>
                    <br>
                    <label class="col-md-2">Periode</label>
                    <label>: <%=startDate%></label>
                    <label> s/d </label>
                    <label><%=endDate%></label>
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
                        <% if (jenisReport == 0) { %>
                                <table border="1" style="font-size: 14px; width: 100% !important">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Marketing</th>
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
                                            total = 0;
                                            totalDp = 0;
                                            totalPiutang = 0;
                                            totalHpp = 0;
                                            totalGross = 0;
                                            double totalQty = 0;
                                            
                                            perTotalDp = 0;
                                            perTotalPiutang = 0;
                                            perTotalHpp = 0;
                                            perTotalGross = 0;
                                            
                                            arr = new JSONArray();
                                            JSONArray arrChild = new JSONArray();
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            url = apiUrl + "/credit/pinjaman/top-sale-marketing/" + startDate + "/" + endDate + "/" + inLocation+"/"+inCategory;
                                            System.out.println("REST API URL : " + url);
                                            obj = WebServices.getAPI("", url);
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
                                                        totalQty += data.optDouble("JUMLAH_PK");
                                                        
                                                        perTotalHpp = Math.round(totalHpp / total * 100.0);
                                                        perTotalDp = Math.round(totalDp / total * 100.0);
                                                        perTotalPiutang = Math.round(totalPiutang / total * 100.0);
                                                        perTotalGross = Math.round(totalGross / total * 100.0);

                                        %>
                                        <tr>
                                            <td class="text-center"><%=x + 1%></td>
                                            <td class="text-center" style="text-align: left"><%=data.optString("FULL_NAME")%></td>
                                            <td class="text-center"><%=data.optDouble("JUMLAH_PK")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(0, "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("DP"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("PIUTANG"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("HPP"), "###,###.##")%></td>
                                            <td class="text-right"><%=Formater.formatNumber(data.optDouble("AMOUNT") - data.optDouble("HPP"), "###,###.##")%></td>
                                        </tr>
                                        <% } %>
                                        <tr>
                                            <td colspan="2" class="text-right" style="text-align: right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalQty, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalPiutang, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(total - totalHpp, "###,###.##")%> (<%=Math.round(((total - totalHpp) / total * 100))%>%)</b></td>
                                        </tr>
                                        <%} else {
                                        %>
                                        <tr> 
                                            <td colspan="9"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}
                                            }%>
                                        
                                    </tbody>
                                </table>
                            <% } else { %>
                                <table border="1" style="font-size: 14px; width: 100% !important">
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
                                            total = 0;
                                            totalDp = 0;
                                            totalPiutang = 0;
                                            totalHpp = 0;
                                            totalGross = 0;
                                            totalKredit = 0;
                                            double subTotalKredit = 0;
                                            perTotalDp = 0;
                                            perTotalPiutang = 0;
                                            perTotalHpp = 0;
                                            perTotalGross = 0;

                                            double subTotal = 0;
                                            double subTotalDp = 0;
                                            double subTotalPiutang = 0;
                                            double subTotalHpp = 0;
                                            double subTotalGross = 0;

                                            int jmlPk = 0;
                                            int jmlPkSub = 0;
                                            int qtyBarang = 0;
                                            int qtyBarangSub = 0;

                                            arr = new JSONArray();
                                            JSONArray arrChild = new JSONArray();
                                            if (inLocation.length()==0){
                                                inLocation = "0";
                                            }
                                            if (inCategory.length()==0){
                                                inCategory = "0";
                                            }
                                            url = apiUrl + "/credit/pinjaman/top-sale-marketing-detail/" + startDate + "/" + endDate + "/" + inLocation+"/"+inCategory;
                                            System.out.println("REST API URL : " + url);
                                            obj = WebServices.getAPI("", url);
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
                                                        amount = Math.round(data.optDouble("AMOUNT"));
                                                        dp = Math.round(data.optDouble("DP"));
                                                        piutang = Math.round(data.optDouble("PIUTANG"));
                                                        hpp = Math.round(data.optDouble("HPP"));
                                                        gross = Math.round(data.optDouble("GROSS"));
                                                        kredit = Math.round(amount - dp);



                                                        if (!hari.equals(currHari)){
                                                            currHari = hari;
                                                        } else {
                                                            hari = "";
                                                        }
                                                        if (!newName.equals(currName)){
                                                            if (currName.length()>0){
                                                                %>
                                                                <tr>
                                                                    <td class="text-right" colspan="5" style="text-align: right"><b>Sub Total</b></td>
                                                                    <td class="text-right"><b><%=qtyBarangSub%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalDp, "###,###.##")%></b></td>
                                                                    <td class="text-right"><b><%=Formater.formatNumber(subTotalKredit, "###,###.##")%></b></td>
                                                                    <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(subTotalGross, "###,###.##")%> (<%=Math.round(((subTotalGross) / subTotal * 100))%>%)</b></td>
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
                                                                    <td class="text-left" style="text-align: left"><%=newName%></td>
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
                                            <td colspan="13"> Data Tidak Ditemukan</td>
                                        </tr>
                                        <%}%>
                                        <tr>
                                            <td class="text-right" colspan="5" style="text-align: right"><b>Sub Total</b></td>
                                            <td class="text-right"><b><%=qtyBarangSub%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotal, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(subTotalKredit, "###,###.##")%></b></td>
                                            <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(subTotalGross, "###,###.##")%> (<%=Math.round((subTotalGross / subTotal * 100))%>%)</b></td>
                                        </tr>
                                        <tr>
                                            <td colspan="5" class="text-right" style="text-align: right"><b>Grand Total</b></td>
                                            <td class="text-right"><b><%=qtyBarang%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalHpp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(0, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(total, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalDp, "###,###.##")%></b></td>
                                            <td class="text-right"><b><%=Formater.formatNumber(totalKredit, "###,###.##")%></b></td>
                                            <td class="text-right" style="text-align: right"><b><%=Formater.formatNumber(totalGross, "###,###.##")%> (<%=Math.round((totalGross / total * 100))%>%)</b></td>
                                        </tr>
                                    </tbody>
                                    <% } %>
                                </table>
                            <% } %>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

