<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment"%>
<%@page import="com.dimata.common.entity.payment.PaymentSystem"%>
<%@page import="com.dimata.posbo.entity.masterdata.Shift"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstShift"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.balance.PstCashCashier"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.pos.entity.masterCashier.CashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcSaleReport"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
    public static final String textListTitleHeader[][] = {
        {"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang","Kasir"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type","Cashier"}
    };
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    
    //DEKLARASI VARIABEL
    Location location = new Location();
    String cashier = "All Cashier";
    String strCurrencyType = "All";
    
    SrcSaleReport srcSaleReport = new SrcSaleReport();
    FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
    frmSrcSaleReport.requestEntity(srcSaleReport);
    
    String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
    String endDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 23:59:59");
    
    //LOCATION REPORT
    if(srcSaleReport.getLocationId()!=0) {
        try{
            location = PstLocation.fetchExc(srcSaleReport.getLocationId());
        }catch(Exception e){
            location.setName("Semua toko");
        }
    }
    
    //CASHIER   
    if(srcSaleReport.getCashMasterId() != 0) {
        String whereClause = PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"="+srcSaleReport.getCashMasterId();
        Vector listCashier = PstCashMaster.list(0, 0, whereClause, "");
        CashMaster cashMaster = (CashMaster)listCashier.get(0);
        cashier = ""+cashMaster.getCashierNumber();
    }
    
    //CURRENCY 
    if(srcSaleReport.getCurrencyOid() != 0) {
        String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
        Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
        CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
        strCurrencyType = currencyType.getCode();
    }
    
    
    //-----------------------------REPORT QUERY--------------------------------
    String whereRevenue = " 1=1 ";
    BillMain entBillMain = new BillMain();
    double totalRevenue =0;
    //REVENUE
    
    whereRevenue += ""
        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+">= '"+startDate+"'"
        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+"<= '"+endDate+"'";
    
    if (location.getOID()!=0){
        whereRevenue += ""
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"='"+location.getOID()+"'";
    }
    
    if(srcSaleReport.getCashMasterId() != 0) {
        whereRevenue += ""
            + " AND cm."+PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID]+"='"+srcSaleReport.getCashMasterId()+"'";
    }
    
    if(srcSaleReport.getCurrencyOid() != 0) {
        whereRevenue += ""
            + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"='"+srcSaleReport.getCurrencyOid()+"'";
    }
    
    Vector listRevenue = PstBillMain.listForSummary(0, 0, whereRevenue, "");
    if (listRevenue.size()>0){
        entBillMain = (BillMain) listRevenue.get(0);
    }
    
    totalRevenue = entBillMain.getTaxValue()+entBillMain.getAmount()+entBillMain.getServiceValue()-entBillMain.getDiscount()-0;
    
    //SHIFT REPORT
    Vector listShift = PstShift.listAll();
    
    //PAYMENT REPORT
    Vector listPayment = PstBillMain.listPaymentForSummary(0, 0, whereRevenue, "");
    
    //GET COST REPORT
    double costRevenue = PstBillMain.getSumCostForSummary(whereRevenue);
    
    //CATEGORY REPORT 
    Vector listCategoryMenu = PstBillMain.listMaterialForSummary(0,0,whereRevenue,"");
    
    //GET REVENUE DINE IN
    String whereRevDineIn = whereRevenue +
        " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_ROOM_ID]+"<>0 ";
    Vector listRevDineIn = PstBillMain.listForSummary(0,0,whereRevDineIn,"");
    int countDineIn = PstBillMain.getCountForSummary(whereRevDineIn);
    
    //GET REVENUE TAKE AWAY
    String whereRevTakeAway = whereRevenue +
        " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_ROOM_ID]+"=0 "
        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"=''";
    Vector listRevTakeAway = PstBillMain.listForSummary(0,0,whereRevTakeAway,"");
    int countRevTakeAway = PstBillMain.getCountForSummary(whereRevTakeAway);
    
    //GET REVENUE DELIVERY ORDER
    String whereRevDelOrder = whereRevenue +
        " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_ROOM_ID]+"=0 "
        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"<>''";
    Vector listRevDelOrder = PstBillMain.listForSummary(0,0,whereRevDelOrder,"");
    int countRevDelOrder = PstBillMain.getCountForSummary(whereRevDelOrder);
    
    //GET ITEM VOID
    Vector listVoidItem = PstBillMain.listVoidMaterialForSummary(0,0,whereRevenue,"");
    
    //GET DISCOUNT GROUP BY PCT
    Vector listDiscForSummary = PstBillMain.listDiscForSummary(0,0,whereRevenue,"");
    
    String whereOpenBill = ""
        + " "+whereRevenue+" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 "
        + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0"
        + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"=1";
    //GET OPEN BILL REVENUE
    Vector listSummaryOpenBill = PstBillMain.listSummaryOpenBill(0,0,whereOpenBill);
    
    
    //PAYMENT REPORT RETURN
    String whereReturnRev = ""
        + " "+whereRevenue+" AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=1 ";
    Vector listPaymentReturn = PstBillMain.listPaymentForSummary(0, 0, whereReturnRev, "");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%if(menuUsed == MENU_ICON){%>
            <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.min.css">
        <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.js"></script>
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                </td>
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
                          <td height="20" class="mainheader"> 
                            &nbsp;Laporan Cashier Sales Summary
                          </td>
                        </tr>
                    </table>
                </td>   
            </tr>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" cellspacing="0" cellpadding="3">
                        <tr align="left" valign="top"> 
                            <td height="14" valign="middle" colspan="3" class="command"> 
                                <b><%=textListTitleHeader[SESS_LANGUAGE][3]%> : <%=location.getName()%></b> 
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td height="14" valign="middle" colspan="3" class="command"> 
                                <b>Tanggal : <%=startDate%>  s/d <%=endDate%></b> 
                            </td>
                        </tr>
                        <tr align="left"> 
                            <td height="22" valign="middle">
                                <b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=cashier%></b>
                            </td>
                        </tr>
                        <tr align="left" valign="top">
                            <td height="22" valign="middle" >
                                <b><%=textListTitleHeader[SESS_LANGUAGE][9]%> : <%=strCurrencyType%></b>
                            </td>
                        </tr>
                        <tr align="left" valign="top"> 
                            <td height="22" valign="middle" ></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <div class="row" style="margin-right: 0px; margin-left: 0px;">
                        <div class="col-md-12">
                            <table class="table table-striped table-bordered table-hover"  width='100%'>
                            <thead>
                                <tr>
                                    <td height='39'><strong><center>Item Name</center></strong></td>
                                    <td><strong><center>Count</center></strong></td>
                                    <td><strong><center>Percentage</center></strong></td>
                                    <td><strong><center>Value</center></strong></td>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="4" width='33%'><strong>Revenue</strong></td>                                 
                                </tr> 
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber((entBillMain.getAmount()),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Discount</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(entBillMain.getDiscount(),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Net  Sales</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber((entBillMain.getAmount()-entBillMain.getDiscount()),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Service Charge</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(entBillMain.getServiceValue(),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Tax 1</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(entBillMain.getTaxValue(),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Rounding</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">0</td>
                                </tr>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><b><%= Formater.formatNumber((entBillMain.getTaxValue()+entBillMain.getAmount()+entBillMain.getServiceValue()-entBillMain.getDiscount()-0),"###,###,##0")%></b></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Shift Report</strong></td>                                
                                </tr> 
                                <%
                                double totalRevShift = 0;
                                double totalRevShiftPct = 0;
                                int totalBillShift = 0;
                                for (int i = 0; i<listShift.size();i++){
                                    Shift matShift = new Shift();
                                    matShift = (Shift)listShift.get(i);
                                    
                                    //Get Revenue By This Shift
                                    String whereRevShift = ""
                                        + " "+ whereRevenue + ""
                                        + " AND cc."+PstCashCashier.fieldNames[PstCashCashier.FLD_SHIFT_ID]+"='"+matShift.getOID()+"'";
                                    
                                    Vector listRevByShift = PstBillMain.listForSummary(0, 0, whereRevShift, "");
                                    BillMain enBillMain2 = new BillMain();
                                    double totalRevByShift = 0;
                                    if (listRevByShift.size()>0){
                                        enBillMain2 = (BillMain)listRevByShift.get(0);
                                    }
                                    totalRevByShift = enBillMain2.getTaxValue()+enBillMain2.getAmount()+enBillMain2.getServiceValue()-enBillMain2.getDiscount()-0;
                                    totalRevShift = totalRevShift + totalRevByShift;
                                            
                                    //count bill this shif
                                    int totalBillByShift = PstBillMain.getCountForSummary(whereRevShift);
                                    totalBillShift = totalBillShift + totalBillByShift;
                                    
                                    //Percentage this shif revenue to total revenue
                                    double totalRevByShiftPct = totalRevByShift/totalRevenue * 100;
                                    totalRevShiftPct = totalRevShiftPct+ totalRevByShiftPct;
                                    
                                    out.println("<tr>");
                                    out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;"+matShift.getName()+"</td>");
                                    out.println("<td style='text-align:right'>"+totalBillByShift+"</td>");
                                    out.println("<td style='text-align:right'>"+totalRevByShiftPct+" %</td>");
                                    out.println("<td style='text-align:right'>"+Formater.formatNumber(totalRevByShift, "###,###,##0")+"</td>");
                                    out.println("</tr>");
                                }
                                %>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style='text-align:right'><b><%= totalBillShift%></b></td>
                                    <td style='text-align:right'><b><%= totalRevShiftPct%> %</b></td>
                                    <td style='text-align:right'><b><%= Formater.formatNumber(totalRevShift,"###,###,##0")%></b></td>
                                </tr>  
                                <tr>
                                    <td colspan="4"><strong>No Sales</strong></td>                                  
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Discount</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Net Sales</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Service Charge</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Tax 1</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Rounding</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>No Sales Payment</strong></td>                                    
                                </tr>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td><strong>Payment</strong></td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <%
                                double totalPaymentPayment = 0;
                                double totalReturnPayment = 0;
                                double totalNetPayment = 0;
                                double totalPercentPayment = 0;
                                int totalBillPayment =0;
                                        
                                totalPaymentPayment = PstBillMain.getSumCashPaymentForSummary(whereRevenue);
                                totalReturnPayment = PstBillMain.getSumCashReturnPaymentForSummary(whereRevenue);
                                totalNetPayment = totalPaymentPayment -totalReturnPayment;
                                
                                for (int j = 0; j<listPayment.size();j++){
                                    PaymentSystem paymentSystem = new PaymentSystem();
                                    paymentSystem = (PaymentSystem) listPayment.get(j);
                                    
                                    //GET COUNT BILL BY THIS PAYMENT
                                    String wherePayment = "" +
                                        whereRevenue + "AND cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+"='"+paymentSystem.getOID()+"'" ;
                                    
                                    int totalBillByPayment = PstBillMain.getCountForSummary(wherePayment);
                                    totalBillPayment = totalBillPayment+totalBillByPayment;
                                    
                                    double totalPaymentByPayment = 0;
                                    double totalReturnByPayment =0;
                                    double totalNetPaymentByPayment = 0;
                                    
                                    totalPaymentByPayment = PstBillMain.getSumCashPaymentForSummary(wherePayment);
                                    
                                    //jika payment adalah cash, maka dihitung kembaliannya
                                    if (paymentSystem.getPaymentType()==1){
                                        totalReturnByPayment = PstBillMain.getSumCashReturnPaymentForSummary(whereRevenue);
                                    }
                                    
                                    totalNetPaymentByPayment = totalPaymentByPayment-totalReturnByPayment;
                                    
                                    //Menghitung persentase payment ini dari total payment
                                    double pctPaymentByPayment = totalNetPaymentByPayment/totalNetPayment*100;
                                    totalPercentPayment = totalPercentPayment +  pctPaymentByPayment;  
                                    
                                    out.println("<tr>");
                                    out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;"+paymentSystem.getPaymentSystem()+"</td>");
                                    out.println("<td style='text-align:right'>"+totalBillByPayment+"</td>");
                                    out.println("<td style='text-align:right'>"+pctPaymentByPayment+" %</td>");
                                    out.println("<td style='text-align:right'>"+Formater.formatNumber(totalNetPaymentByPayment,"###,###,##0")+"</td>");
                                    out.println("</tr>");
                                }
                                %>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style='text-align:right'><strong><%= totalBillPayment%></strong></td>
                                    <td style='text-align:right'><strong><%= totalPercentPayment%> %</strong></td>
                                    <td style='text-align:right'><strong><%= Formater.formatNumber(totalNetPayment,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Profit and Loss</strong></td>                                    
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Subtotal</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber((entBillMain.getAmount()),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Discount</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(entBillMain.getDiscount(),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Net  Sales</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber((entBillMain.getAmount()-entBillMain.getDiscount()),"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Total Cost</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(costRevenue,"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td><strong>Profit/Loss</strong></td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right">-</td>
                                    <td style="text-align:right"><b><%= Formater.formatNumber((entBillMain.getAmount()-entBillMain.getDiscount()-costRevenue),"###,###,##0")%></b></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Category</strong></td>                                  
                                </tr>
                                <%
                                //Mendapatkan total category menu general
                                Vector listCategorySumGeneral = PstBillMain.listMaterialCategoryForSummary(0,0,whereRevenue,"");
                                Billdetail billDetailGeneral = new Billdetail();
                                double percentageCategoryGeneral = 0;
                                if (listCategorySumGeneral.size()>0){
                                    billDetailGeneral = (Billdetail)listCategorySumGeneral.get(0);
                                }
                                for (int i = 0; i<listCategoryMenu.size();i++){                                   
                                    Category category = new Category();
                                    category = (Category)listCategoryMenu.get(i); 
                                    
                                    //WHERE CATEGORY
                                    String whereCategory = "" +
                                        whereRevenue + "AND pm."+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"='"+category.getOID()+"'" ;
                                    
                                    Billdetail billdetail = new Billdetail();
                                    Vector listCategorySum = PstBillMain.listMaterialCategoryForSummary(0,0,whereCategory,"");
                                    if (listCategorySum.size()>0){
                                        billdetail = (Billdetail) listCategorySum.get(0);
                                    }
                                    
                                    //get percentage 
                                    double percentageCategoryMenu = billdetail.getTotalPrice() / billDetailGeneral.getTotalPrice() *100;
                                    percentageCategoryGeneral = percentageCategoryGeneral  +percentageCategoryMenu;
                                    
                                    out.println("<tr>");
                                    out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;"+category.getName()+"</td>");
                                    out.println("<td style='text-align:right'>"+billdetail.getQty()+"</td>");
                                    out.println("<td style='text-align:right'>"+percentageCategoryMenu+" %</td>");
                                    out.println("<td style='text-align:right'>"+Formater.formatNumber(billdetail.getTotalPrice(),"###,###,##0")+"</td>");
                                    out.println("</tr>");
                                }
                                %>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style='text-align:right'><strong><%= billDetailGeneral.getQty()%></strong></td>
                                    <td style='text-align:right'><strong><%=percentageCategoryGeneral%> %</strong></td>
                                    <td style='text-align:right'><strong><%= Formater.formatNumber(billDetailGeneral.getTotalPrice(),"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Discount</strong></td>                                  
                                </tr>
                                <%
                                //DISCOUNT PROCESSING 
                                int countDiscRev = 0;
                                double totalDIscountRev =  0;
                                for (int i = 0; i<listDiscForSummary.size();i++){
                                    BillMain  entBillMainDisc = new BillMain();
                                    entBillMainDisc = (BillMain) listDiscForSummary.get(i);
                                    
                                    if (entBillMainDisc.getDiscPct()>0){
                                        
                                        countDiscRev = countDiscRev + entBillMainDisc.getPaxNumber();
                                        totalDIscountRev = totalDIscountRev +entBillMainDisc.getDiscount();
                                        
                                        out.println("<tr>");
                                        out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;BILL DISC "+entBillMainDisc.getDiscPct()+" %</td>");
                                        out.println("<td style='text-align:right'>"+entBillMainDisc.getPaxNumber()+"</td>");
                                        out.println("<td style='text-align:right'>-</td>");
                                        out.println("<td style='text-align:right'>"+Formater.formatNumber(entBillMainDisc.getDiscount(),"###,###,##0")+"</td>");
                                        out.println("</tr>");
                                    }
                                    
                                }
                                %>
                                
                                <tr>
                                    <td ><strong>Total</strong></td>
                                    <td style='text-align:right'><strong><%= countDiscRev%></strong></td>
                                    <td style='text-align:right'><strong>-</strong></td>
                                    <td style='text-align:right'><strong><%= Formater.formatNumber(totalDIscountRev,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Void Item</strong></td>                                   
                                </tr>
                                <%
                                //VOID ITEM PROCESSING
                                double totalQtyVoid = 0;
                                double totalAmountVoid = 0;
                                for(int i = 0; i<listVoidItem.size();i++){
                                    Billdetail entBillDetailVoid = new Billdetail();
                                    entBillDetailVoid =(Billdetail)listVoidItem.get(i);                                    
                                    Material material = new Material();
                                    material = PstMaterial.fetchExc(entBillDetailVoid.getMaterialId());
                                    String materialNames = "";
                                    try{
                                        String materialName []= material.getFullName().split(";");
                                        materialNames = materialName[1];
                                    }catch(Exception ex){
                                        materialNames = material.getFullName();
                                    }
                                    
                                    totalQtyVoid = totalQtyVoid + entBillDetailVoid.getQty();
                                    totalAmountVoid = totalAmountVoid+ entBillDetailVoid.getTotalPrice();
                                    out.println("<tr>");
                                    out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;"+materialNames+"</td>");
                                    out.println("<td style='text-align:right'>"+entBillDetailVoid.getQty()+"</td>");
                                    out.println("<td style='text-align:right'>-</td>");
                                    out.println("<td style='text-align:right'>"+Formater.formatNumber(entBillDetailVoid.getTotalPrice(),"###,###,##0")+"</td>");
                                    out.println("</tr>");
                                }
                                %>
                                
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style='text-align:right'><strong><%=totalQtyVoid%></strong></td>
                                    <td style='text-align:right'><strong>-</strong></td>
                                    <td style='text-align:right'><strong><%=Formater.formatNumber(totalAmountVoid,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Return Bill</strong></td>                                    
                                </tr>
                                <%
                                double totalPaymentPaymentRe = 0;
                                double totalReturnPaymentRe = 0;
                                double totalNetPaymentRe = 0;
                                double totalPercentPaymentRe = 0;
                                int totalBillPaymentRe =0;
                                        
                                totalPaymentPaymentRe = PstBillMain.getSumCashPaymentForSummary(whereReturnRev);
                                totalReturnPaymentRe = PstBillMain.getSumCashReturnPaymentForSummary(whereReturnRev);
                                totalNetPaymentRe = totalPaymentPaymentRe -totalReturnPaymentRe;
                                
                                for (int j = 0; j<listPaymentReturn.size();j++){
                                    PaymentSystem paymentSystem = new PaymentSystem();
                                    paymentSystem = (PaymentSystem) listPaymentReturn.get(j);
                                    
                                    //GET COUNT BILL BY THIS PAYMENT
                                    String wherePayment = "" +
                                        whereReturnRev + "AND cp."+PstCashPayment.fieldNames[PstCashPayment.FLD_PAY_TYPE]+"='"+paymentSystem.getOID()+"'" ;
                                    
                                    int totalBillByPaymentRe = PstBillMain.getCountForSummary(wherePayment);
                                    totalBillPaymentRe = totalBillPaymentRe+totalBillByPaymentRe;
                                    
                                    double totalPaymentByPaymentRe = 0;
                                    double totalReturnByPaymentRe =0;
                                    double totalNetPaymentByPaymentRe = 0;
                                    
                                    totalPaymentByPaymentRe = PstBillMain.getSumCashPaymentForSummary(wherePayment);
                                    
                                    //jika payment adalah cash, maka dihitung kembaliannya
                                    if (paymentSystem.getPaymentType()==1){
                                        totalReturnByPaymentRe = PstBillMain.getSumCashReturnPaymentForSummary(whereReturnRev);
                                    }
                                    
                                    totalNetPaymentByPaymentRe = totalPaymentByPaymentRe-totalReturnByPaymentRe;
                                    
                                    //Menghitung persentase payment ini dari total payment
                                    double pctPaymentByPaymentRe = totalNetPaymentByPaymentRe/totalNetPaymentRe*100;
                                    totalPercentPaymentRe = totalPercentPaymentRe +  pctPaymentByPaymentRe;  
                                    
                                    out.println("<tr>");
                                    out.println("<td>&nbsp;&nbsp;&nbsp;&nbsp;"+paymentSystem.getPaymentSystem()+"</td>");
                                    out.println("<td style='text-align:right'>"+totalBillByPaymentRe+"</td>");
                                    out.println("<td style='text-align:right'>-</td>");
                                    out.println("<td style='text-align:right'>"+Formater.formatNumber(totalNetPaymentByPaymentRe,"###,###,##0")+"</td>");
                                    out.println("</tr>");
                                }
                                %>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style='text-align:right'><strong><%= totalBillPaymentRe%></strong></td>
                                    <td style='text-align:right'><strong>-</strong></td>
                                    <td style='text-align:right'><strong><%= Formater.formatNumber(totalNetPaymentRe,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Sales Type</strong></td>                                  
                                </tr>
                                <%
                                    //SALES TYPE PROCESS
                                    //TOTAL = DINE IN + TAKE AWAY + DELIVERY ORDER
                                    BillMain billMainTotaly = new BillMain();
                                    if (listRevenue.size()>0){
                                        billMainTotaly = (BillMain) listRevenue.get(0);
                                    }
                                    double totalBillMainTot = billMainTotaly.getTaxValue()+billMainTotaly.getAmount()+billMainTotaly.getServiceValue()-billMainTotaly.getDiscount()-0;
                                
                                    //DINE IN
                                    BillMain billMainDineIn = new BillMain();
                                    if (listRevDineIn.size()>0){
                                        billMainDineIn = (BillMain) listRevDineIn.get(0);
                                    }
                                    double totalBillMainDineIn = billMainDineIn.getTaxValue()+billMainDineIn.getAmount()+billMainDineIn.getServiceValue()-billMainDineIn.getDiscount()-0;
                                    double pctDineIn = totalBillMainDineIn/totalBillMainTot *100;
                                                                       
                                    //TAKE AWAY
                                    BillMain billMainTakeAway = new BillMain();
                                    if (listRevTakeAway.size()>0){
                                        billMainTakeAway = (BillMain) listRevTakeAway.get(0);
                                    }
                                    double totalBillMainTakeAway = billMainTakeAway.getTaxValue()+billMainTakeAway.getAmount()+billMainTakeAway.getServiceValue()-billMainTakeAway.getDiscount()-0;
                                    double pctTakeAway = totalBillMainTakeAway/totalBillMainTot *100;
                                                                       
                                    //DELIVERY ORRDER
                                    BillMain billMainDeOrder = new BillMain();
                                    if (listRevDelOrder.size()>0){
                                        billMainDeOrder = (BillMain) listRevDelOrder.get(0);
                                    }
                                    double totalBillMainDeOrder= billMainDeOrder.getTaxValue()+billMainDeOrder.getAmount()+billMainDeOrder.getServiceValue()-billMainDeOrder.getDiscount()-0;
                                    double pctDeOrder = totalBillMainDeOrder/totalBillMainTot *100;
                                    
                                    //TOTAL PERCENTAGE
                                    double pctTotal = pctDineIn+pctTakeAway+pctDeOrder;
                                    
                                    //TOTAL COUNT 
                                    int countTotal = countDineIn+countRevTakeAway+countRevDelOrder;
                                    
                                %>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Dine In</td>
                                    <td style="text-align:right"><%=countDineIn%></td>
                                    <td style="text-align:right"><%=pctDineIn%> %</td>
                                    <td style="text-align:right"><%= Formater.formatNumber(totalBillMainDineIn,"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Take Away</td>
                                    <td style="text-align:right"><%=countRevTakeAway%></td>
                                    <td style="text-align:right"><%=pctTakeAway%></td>
                                    <td style="text-align:right"><%= Formater.formatNumber(totalBillMainTakeAway,"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td>&nbsp;&nbsp;&nbsp;&nbsp;Delivery</td>
                                    <td style="text-align:right"><%=countRevDelOrder%></td>
                                    <td style="text-align:right"><%=pctDeOrder%></td>
                                    <td style="text-align:right"><%= Formater.formatNumber(totalBillMainDeOrder,"###,###,##0")%></td>
                                </tr>
                                <tr>
                                    <td><strong>Total</strong></td>
                                    <td style="text-align:right"><strong><%=countTotal%></strong></td>
                                    <td style="text-align:right"><%= pctTotal%></td>
                                    <td style="text-align:right"><strong><%= Formater.formatNumber(totalBillMainTot,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><strong>Outstanding</strong></td>                                   
                                </tr>     
                                <%
                                    BillMain billMainOpenBill = new BillMain();
                                    if (listSummaryOpenBill.size()>0){
                                        billMainOpenBill = (BillMain) listSummaryOpenBill.get(0);
                                    }
                                %>
                                <tr>
                                    <td><strong>Total </strong></td>
                                    <td style="text-align:right"><b><%= billMainOpenBill.getPaxNumber()%></b></td>
                                    <td style="text-align:right"><b>-</b></td>
                                    <td style="text-align:right"><b><%= Formater.formatNumber((billMainOpenBill.getAmount() +billMainOpenBill.getTaxValue() + billMainOpenBill.getServiceValue() - billMainOpenBill.getDiscount()),"###,###,##0")%></b></td>
                                </tr>
                                <tr>
                                    <td colspan="4"><center><strong>STATISTIC</strong></center></td>
                                </tr>
                                <%
                                    //STATICTIC PROCESSING
                                    int totalGuestBillMain = billMainTotaly.getPaxNumber();
                                    double avAmountPerInvoice = totalBillMainTot/countTotal;
                                    double avAmountPerGuest = totalBillMainTot/totalGuestBillMain;
                                %>
                                <tr>
                                    <td colspan="3"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Total Sales</strong></td>                                   
                                    <td style="text-align:right"><strong><%= Formater.formatNumber(totalBillMainTot,"###,###,##0")%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="3"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Total Guest</strong></td>
                                    <td style="text-align:right"><strong><%=totalGuestBillMain%></strong></td>                                 
                                </tr>
                                <tr>
                                    <td colspan="3"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Average  amount per guest</strong></td>
                                    <td style="text-align:right"><strong><%= Formater.formatNumber(avAmountPerGuest,"###,###,##0")%></strong></td>                                  
                                </tr>
                                <tr>
                                    <td colspan="3"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Total Invoice</strong></td>
                                    <td style="text-align:right"><strong><%=countTotal%></strong></td>
                                </tr>
                                <tr>
                                    <td colspan="3"><strong>&nbsp;&nbsp;&nbsp;&nbsp;Average  amount per invoice </strong></td>
                                    <td style="text-align:right"><strong><%= Formater.formatNumber(avAmountPerInvoice,"###,###,##0")%></strong></td>                               
                                </tr>
                            </tbody>
                        </table>
                    
                        </div>
                    </div>
                    <div class="row" style="margin-right: 0px; margin-left: 0px;">
                        <div class="col-md-12">
                            <form method="POST" action="report_cashier_sales_sum_xls.jsp">
                            <input type="hidden" value="<%=srcSaleReport.getLocationId()%>" name="<%= FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID]%>">
                            <input type="hidden" value="<%=Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd")%>" name="<%= FrmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE] %>">
                            <input type="hidden" value="<%=srcSaleReport.getCashMasterId()%>" name="<%= FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CASH_MASTER_ID]%>">
                            <input type="hidden" value="<%=srcSaleReport.getCurrencyOid()%>" name="<%= FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CURRENCY]%>">
                            <button type="submit" class="btn btn-primary pull-left">Export Excel</button>
                            </form>
                        </div>
                    </div>
                    <br></br>
                </td>
            </tr>
        </table>
    </body>
</html>
