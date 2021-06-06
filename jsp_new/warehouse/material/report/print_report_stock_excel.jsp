<%-- 
    Document   : print_report_stock_excel
    Created on : Nov 5, 2019, 9:33:27 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMerk"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportPotitionStock"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportPotitionStock"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportPotitionStock"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ include file ="../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file ="../../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!  public static final String textListGlobal[][] = {
    //0                  1              2           3           4             5
    {"Pencarian", "Lihat", "Tambah", "Stock", "Laporan", "Posisi"},
    {"Search", "View", "Add", "Stock", "Report", "Position"}
  };
  public static final String textListHeader[][] = {
    //0            1        2            3                4            5                     6               7                      8             9           10
    {"Periode", "s/d", "Lokasi", "Kategori", "Merk", "Mata Uang", "Supplier", "Price Type By", "Group By", "View", "Semua"},
    {"Periode", "Until", "Location", "Category", "Merk", "Currency", "Supplier", "Price Type By", "Group By", "View", "All"},};

  public static final String textHeaderTable[][] = {
    //0            1               2                 3                4                  5               6         7            8           9          10         11        12       13      14
    {"No", "Stock ID", "Barcode", "Stock Name", "Brand", "Sub Category", "Stock Awal", "Purch", "P. Rtn", "Penerimaan", "Pengembalian", "Sales", "Exchange", "Adj", "End"},
    {"No", "Stock ID", "Barcode", "Stock Name", "Brand", "Sub Category", "First Stock", "Purch", "P. Rtn", "Receive", "Return", "Sales", "Exchange", "Adj", "End"}

};

%>
<%
  
    String checked="selected";
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int type = FRMQueryString.requestInt(request,"type");
    int includePerpindahan = FRMQueryString.requestInt(request, "PERPINDAHAN");
    int showQtyZero = FRMQueryString.requestInt(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_SHOW_QTY_ZERO]);
    int includeWarehouse = FRMQueryString.requestInt(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_INCLUDE_WAREHOUSE]);
    long oidLocation = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_LOCATION_ID]);
    String tglMulai = FRMQueryString.requestString(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_FROM]);
    String tglAkhir = FRMQueryString.requestString(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_TO]);
    long oidCategory = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_CATEGORY_ID]);
    long oidMerk = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_MERK_ID]);
    long oidCurrency = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_STANDART_ID]);
    long oidSupplier = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_SUPPLIER_ID]);
    int groupBy = FRMQueryString.requestInt(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_GROUP_BY]);
    long oidPriceType = FRMQueryString.requestLong(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_PRICE_TYPE_ID]);
     
    SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
    SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
    FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock(request, srcReportPotitionStock);

   //proses pengambilan userid dilakukan di javainit
    srcReportPotitionStock.setUserId(userId);
    // Date format Search
    if (tglMulai.isEmpty()) {
        tglMulai = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
        
    }
    if (tglAkhir.isEmpty()) {
        tglAkhir = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
    }
    Date nowDate = new Date();
    long oidPeriode = PstPeriode.getPeriodeNow(nowDate);
    Periode p = new Periode();
    try {
        p = PstPeriode.fetchExc(oidPeriode);
      } catch (Exception e) {
      }
    Date periodeStart = p.getStartDate();
    Date periodeEnd = p.getEndDate();
    //Get Periode Sebelumnya
    long oidPeriodeBefore = PstPeriode.getPeriodeBefore(periodeStart);
    Periode periode = new Periode();
    try {
        periode = PstPeriode.fetchExc(oidPeriodeBefore);
      } catch (Exception e) {
      }
    
    Location location = new Location();
    try {
        location = PstLocation.fetchExc(oidLocation);
    } catch (Exception exc){}
   
response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "posisi_stock_"+location.getName().replaceAll(" ", "_")+"-"+tglMulai+"_"+tglAkhir+".xls" ); 
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Export Excel</title>
    <style>

      th {
            text-align: center;
        }
        td {
            text-align: center;
        }

    </style>
    </head>
    <body>
              <form id="prntsrcReportPotitionStock" class="form-horizontal" method="post" action="">
              <input type="hidden" name="command" value="<%=Command.LIST%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="type" value="<%=type%>">
              <input type="hidden" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_USER_ID]%>" value="<%=userId%>">
              <input type="hidden" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_GENERATE_REPORT]%>" value="true">
              <table id="listStock" class="table table-striped table-bordered" style="width:100%" border="1">
              <thead class="headerList">
                <tr>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][0]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][1]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][2]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][3]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][4]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][5]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][6]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][9]%></th>
                  <th style="font-size: 10px;">Transfer In</th>
                  <th style="font-size: 10px;">Transfer Out</th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][10]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][11]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][12]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][13]%></th>
                  <th style="font-size: 10px;"><%=textHeaderTable[SESS_LANGUAGE][14]%></th>
                </tr>
              </thead>
              <tbody>
                  <%
                    String whereData = "";
                    whereData += " mat."+ PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID]+" <> 0";
                    
                    if (oidSupplier != 0) {
                    whereData += " AND con." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " = " + oidSupplier;
                    }
                    if (oidMerk != 0) {
                    whereData = whereData + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_MERK_ID] + " = " + oidMerk;
                    }
                    if (oidCategory != 0) {
                    whereData = whereData + " AND mat." + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + oidCategory;
                    }
                    String order = "";
                    if (groupBy == SrcSaleReport.GROUP_BY_SUPPLIER) {
                        order = order + " con." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " ASC ";
                    } else if (groupBy == SrcSaleReport.GROUP_BY_CATEGORY) {
                        order = order + " cat." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " ASC ";
                    } else {
                        order = order + " mat." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " ASC ";
                    }
                      String loc = "";
                    if (oidLocation > 0){
                        loc = ""+oidLocation;
                        if (includeWarehouse == 1){
                            try {
                                Location locStore = PstLocation.fetchExc(oidLocation);
                                if (locStore.getParentLocationId()>0){
                                    loc+=","+locStore.getParentLocationId();
                                }
                            } catch (Exception exc){}
                        }
                    }
                    ArrayList<String> data = SessReportPotitionStock.drawTableListPosition(whereData, order, periodeStart, periodeEnd, tglMulai, tglAkhir, loc, useForGreenbowl, groupBy, 1, showQtyZero);
                    if (data.size()>0){
                        for (int i = 0; i < data.size(); i++) { 		      
                            %><%=data.get(i)%><%
                        }   
                    }
                %>
              </tbody>  
            </table>
              </form>
    </body>
</html>
