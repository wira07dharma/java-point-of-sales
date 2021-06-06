<%-- 
    Document   : excel_omzet_report
    Created on : 29 Jul 20, 16:01:49
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
    response.setHeader("Content-Disposition", "attachment; filename=" + "["+namaDate+"]-"+nameLokasi.replaceAll(" ", "_")+"-Omzet.xls");

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
                    <div id="penjualanBulanan">
                        <table border="1" style="font-size: 14px; width: 100% !important">
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
                                    JSONObject objCash = SaleReportDocument.objReportOmzet(startDate,endDate,inLocation);
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
                                    String url = apiUrl + "/report/omzet-web/" + startDate + "/" + endDate + "/" + inLocation;
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
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

