<%-- 
    Document   : reportsalerekaptanggal_form_print_location
    Created on : Nov 14, 2016, 2:31:49 PM
    Author     : Witar
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<!--pos versi 1-->
<!--%@ page import = "com.dimata.cashier.entity.billing.*" %-->
<!--%@ page import = "com.dimata.cashier.entity.payment.*" %-->

<!--pos versi 2-->
<%@ page import = "com.dimata.pos.entity.billing.*" %>
<%@ page import = "com.dimata.pos.entity.payment.*" %>

<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DAILY_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
    {"No","Tanggal","Total","Lokasi"},
    {"No","Date","Total","Location"}
};


public static final String textListHeaderLocation[][] = {
    {"LAPORAN DETAIL REKAP HARIAN PER LOKASI","Laporan Rekap Harian"},
    {"SUMMARY DAILY REPORT PER LOCATION","Summary Daily Report"}
};
%>
<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;

ControlLine ctrLine = new ControlLine();
SrcReportSale srcReportSale = new SrcReportSale();

String whereClause = "";
        
//INT
SessReportSale sessReportSale = new SessReportSale();
FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
frmSrcReportSale.requestEntityObject(srcReportSale);
String dateFrom = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM]+"");
String dateTo = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO]+"");
        
Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
               
srcReportSale.setDateFrom(tglmulai);
srcReportSale.setDateTo(tglberakhir);





        
//CATCH 
int language = FRMQueryString.requestInt(request, "language");
String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID]+"");
String categoryLocation[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID]+"");
String categoryInText="";

String locationInText="";
try {
    for (int a = 0; a<categoryLocation.length;a++){
        if (locationInText.length()>0){
            locationInText = locationInText + "," + categoryLocation[a];
        }else{
            locationInText =  categoryLocation[a];
        }
    }
} catch (Exception e) {
}
        
try {
    for (int a = 0; a<categoryMultiple.length;a++){
        if (categoryInText.length()>0){
            categoryInText = categoryInText + "," + categoryMultiple[a];
        }else{
            categoryInText =  categoryMultiple[a];
        }
    }
} catch (Exception e) {
}
srcReportSale.setCategoryMultiple(categoryInText);
srcReportSale.setLocationMultiple(locationInText);      
//jadikan hastable
Hashtable hashReportRekap = sessReportSale.getReportSaleRekapTanggalPerLocation(srcReportSale);

%>
<!DOCTYPE html>
<%@ page contentType="application/x-msexcel" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata-Prochain</title>
        <style>
            #report, #report th, #report td {
                border: 1px solid black;
            }
            #report{
                border-collapse: collapse;
            }
            @media print {
                .nonprint{
                    visibility: hidden;
                }
            }
        </style>
    </head>
    <body>
        <table style='width:100%;'>
            <tr>
                <td colspan="3"><center><h3><%= textListHeaderLocation[language][0] %></h3></center></td>
            </tr>
            <tr>
                <td style="width:10%;"><%= textListMaterialHeader[language][1]%></td>
                <td style="width:5%;">:</td>
                <td><%= Formater.formatDate(tglmulai, "dd/MM/yyyy")%>-<%= Formater.formatDate(tglberakhir, "dd/MM/yyyy")%></td>
            </tr>
            <tr>
                <%
                    String namaLokasi = "";
                    if (srcReportSale.getLocationMultiple().length()>0){
                        try{
                            String[] parts = srcReportSale.getLocationMultiple().split(",");
                            for (int i = 0; i<parts.length;i++){
                                Location entLocation = new Location();
                                long oidLocation = Long.parseLong(parts[i]);
                                entLocation = PstLocation.fetchExc(oidLocation);

                                if (namaLokasi.length()>0){
                                    namaLokasi += ","+entLocation.getName()+"";
                                }else{
                                    namaLokasi = entLocation.getName();
                                }    
                            }
                        }catch(Exception ex){
                            namaLokasi="";
                        }
                    }else if (srcReportSale.getLocationId() != 0){
                        Location entLocation = new Location();
                        try{
                            entLocation = PstLocation.fetchExc(srcReportSale.getLocationId());
                            namaLokasi = entLocation.getName();
                        }catch(Exception ex){
                            namaLokasi="";
                        }

                    }

                    if (namaLokasi.length()>0){
                        String html = "";

                        html += ""
                        + "<tr align='left' valign='top'>"
                            + "<td>"+textListMaterialHeader[language][3]+"</td>"
                            + "<td>:</td>"
                            + "<td>"+namaLokasi+"</td>"
                        + "</tr>";

                        out.println(html);
                    }
                %>
            </tr>
        </table>
        <br>
        <table id='report' style='width:100%;' class='table table-striped table-bordered'>
            
                <%
                    String htmlReturn = "";
                    htmlReturn ="<tr>";
                    htmlReturn += "<td></td>";
                    htmlReturn += "<td></td>";
                    for (int i = 0; i<categoryLocation.length;i++){
                        long locationOid = Long.parseLong(categoryLocation[i]);
                        Location locHeader = new Location();
                        try {
                            locHeader = PstLocation.fetchExc(locationOid);
                        } catch (Exception e) {
                        }
                        htmlReturn += "<td colspan=\"2\"><center><b>"+locHeader.getName()+"</b></center></td>";//no 
                    }
                    htmlReturn += "<td colspan=\"2\"><center><b>Total</b></center></td>";//no
                    htmlReturn += "<td colspan=\"2\"><center><b>Gross Profit</b></center></td>";//no
                    
                    htmlReturn += ""
                    + "</tr>"   
                    + "<tr>"
                        + "<td><b>"+textListMaterialHeader[language][0]+"</b></td>"//no
                        + "<td><b>"+textListMaterialHeader[language][1]+"</b></td>";//tanggal
                        for (int i = 0; i<categoryLocation.length;i++){
                            htmlReturn += "<td><center><b>Sales</b></center></td>";//no
                            htmlReturn += "<td><center><b>CoGs<b></center></td>";//no
                        }                                  
                        htmlReturn += "<td><center><b>Sales</b></center></td>";//no
                        htmlReturn += "<td><center><b>CoGs<b></center></td>";//no
                                    
                        htmlReturn += "<td><center><b>Rp.</b></center></td>";//no
                        htmlReturn += "<td><center><b>%<b></center></td>";//no    
                                    
                    htmlReturn += ""
                    + "</tr>";
                    if(hashReportRekap!=null && hashReportRekap.size()>0 ){
                            double totalJual = 0.00;
                            double totalCost = 0.00;
                            double totalPotongan = 0.00;
                            double totalProfit = 0.00;
                            double totalTaxVal = 0.00;
                            double totalServiceVal = 0.00;
                            double totalRata= 0;
                            double rata=0;
                            double totalRataRata=0;
                            
                            try {
                                long oneDayMilSec = 86400000;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //Date startDate = srcReportSale.getDateFrom().getTime();//sdf.parse("2012-02-15");
                                //Date endDate = srcReportSale.getDateTo();//sdf.parse("2012-03-15");
                                long startDateMilSec = srcReportSale.getDateFrom().getTime();//startDate.getTime();
                                long endDateMilSec = srcReportSale.getDateTo().getTime();
                                int nourut=0;
                                double rekapPerDate=0.0;
                                double hppPerDate=0.0;
                                double profitPerDate=0.0;
                                for(long d=startDateMilSec; d<=endDateMilSec; d=d+oneDayMilSec){
                                    nourut=nourut+1;
                                    rekapPerDate=0.0;
                                    hppPerDate=0.0;
                                    profitPerDate=0.0;
                                    Date dateNow = new Date(d);
                                    Formater.formatDate(dateNow, "dd-MM-yyyy");
                                    htmlReturn += ""
                                               + "<tr>"
                                               + "<td>"+nourut+"</td>"
                                               + "<td>"+Formater.formatDate(dateNow, "dd-MM-yyyy")+"</td>";        
                                                for (int k = 0; k<categoryLocation.length;k++){
                                                    long locationOid = Long.parseLong(categoryLocation[k]);
                                                    Location locHeader =  new Location();
                                                    try {
                                                        locHeader = PstLocation.fetchExc(locationOid);
                                                    } catch (Exception e) {
                                                    }
                                                    Vector vt = (Vector) hashReportRekap.get(""+locHeader.getOID()+"-"+Formater.formatDate(dateNow, "dd-MM-yyyy"));
                                                    
                                                    if(vt!=null && vt.size()>0){
                                                        Double totalRekap = (Double)vt.get(1);
                                                        Double totalHPP = (Double)vt.get(2);

                                                        hppPerDate=hppPerDate+totalHPP;
                                                        rekapPerDate=rekapPerDate+(totalRekap.doubleValue());
                                                        profitPerDate = profitPerDate + (totalRekap.doubleValue() - totalHPP.doubleValue());

                                                        totalJual = totalJual+(totalRekap.doubleValue());
                                                        totalCost = totalCost+totalHPP;
                                                        totalProfit = totalProfit+(totalRekap.doubleValue()  - totalHPP.doubleValue());

                                                        htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</td>";//no
                                                        htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</td>";//no
                                                    }else{
                                                        htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(0)+"</td>";//no
                                                        htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(0)+"</td>";//no
                                                    }
                                                    
                                                }  
                                                
                                                htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rekapPerDate)+"</td>";//no
                                                htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(hppPerDate)+"";//no
                                                htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(profitPerDate)+"";//no
                                                htmlReturn += "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal((profitPerDate/hppPerDate)*100)+"%</td>";//no    
                                    
                                }
                                
                                htmlReturn += "<tr>"
                                    + "<td></td>"//no
                                    + "<td><b>Total</b></td>";//tanggal
                                    //lokasi
                                     for (int i = 0; i<categoryLocation.length;i++){
                                        htmlReturn += "<td><center></center></td>";//no
                                        htmlReturn += "<td><center></center></td>";//no
                                    }
                                    
                                    htmlReturn += "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual)+"</b></center></td>";//no
                                    htmlReturn += "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></center></td>";//no
                                    
                                    htmlReturn += "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit)+"</b></center></td>";//no
                                    htmlReturn += "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal((totalProfit/totalCost)*100)+"%</b></center></td>";//no    
                                    
                                htmlReturn += "</tr>" ; 
                                
                            }catch (Exception ex) {
                            }
//                            for (int i = 0; i<hashReportRekap.size();i++){
//                                
//                                
//                                for (int k = 0; k<location.size();k++){
//                                    htmlReturn += "<td>HPP</td>";//no
//                                    htmlReturn += "<td>SALES</td>";//no
//                                }   
//                                Vector rowx = new Vector();				
//                                Vector vt = (Vector)records.get(i);
//                                String tanggal = (String)vt.get(0);
//                                Double totalRekap = (Double)vt.get(1);
//                                Double totalHPP = (Double)vt.get(2);
//                                Double totalDisc = (Double)vt.get(3);
//                                
//                                if (i==0){
//                                    totalRata = totalRekap.doubleValue();
//                                }else{
//                                    totalRata = rata+ totalRekap.doubleValue();
//                                }
//                                
//
//                                Double totalTax = (Double)vt.get(4);
//                                Double totalService = (Double)vt.get(5);
//                                totalJual += totalRekap.doubleValue();
//                                rata = totalJual/(i+1);
//                                //htmlReturn += ""
////                                + "<tr>"
////                                    + "<td>"+(i+1)+"</td>"
////                                    + "<td>"+tanggal+"</td>"
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</td>"//total jual
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rata)+"</td>"//
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</td>"//total hpp
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue())+"</td>"//total profite
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalTax.doubleValue())+"</td>"
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalService.doubleValue())+"</td>"
////                                + "</tr>";
//                                htmlReturn += ""
//                                            + "<td>"+(i+1)+"</td>"
//                                            + "<td>"+tanggal+"</td>";
//                                        for (int k = 0; k<location.size();k++){
//                                                htmlReturn += "<td>HPP</td>";//no
//                                                htmlReturn += "<td>SALES</td>";//no
//                                            }
//                                
//                                totalRataRata = 0;
//                                totalCost += totalHPP.doubleValue();
//                                totalPotongan += totalDisc.doubleValue();
//                                totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
//                                totalTaxVal += totalTax.doubleValue();
//                                totalServiceVal += totalService.doubleValue();
                          //  }
//                            htmlReturn += ""
//                            + "<tr>"
//                                + "<td colspan='2'><b>TOTAL</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal)+"</b></td>"
//                            + "</tr>"
//                            + "<tr>"
//                                + "<td colspan='2'><b>"+textListMaterialHeader[language][8]+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal/records.size())+"</b></td>"
//                                    
//                            + "</tr>";
                        }else{
                            htmlReturn += ""
                            + "<tr><td colspan='7'><center>No Data</center></td></tr>";
                        }
                    
                        out.println(htmlReturn);
                %>
          
        </table>
        <br>
        <button type='button' class='nonPrint' onclick='window.print()'>Print</button>
    </body>
</html>
