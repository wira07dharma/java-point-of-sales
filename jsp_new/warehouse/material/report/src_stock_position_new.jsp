<%-- 
    Document   : src_stock_position
    Created on : Oct 29, 2019, 4:03:34 PM
    Author     : Regen
--%>

<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstSubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.gui.jsp.ControlCheckBox"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportPotitionStock"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.posbo.entity.masterdata.Merk"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMerk"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportPotitionStock"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@ page import="com.dimata.posbo.session.warehouse.SessReportPotitionStock,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlLine" %>
<%@ include file ="../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file ="../../../main/checkuser.jsp" %>

<%!  public static final String textListGlobal[][] = {
    //0                  1              2           3           4             5             6
    {"Pencarian", "Lihat", "Tambah", "Stock", "Laporan", "Posisi", "Laporan Stock Posisi"},
    {"Search", "View", "Add", "Stock", "Report", "Position", "Report Stock Position"}
  };
  public static final String textListHeader[][] = {
    //0            1        2            3                4            5                     6               7                      8             9           10
    {"Periode", "s/d", "Lokasi", "Kategori", "Merk", "Mata Uang", "Supplier", "Price Type By", "Group By", "View", "Semua"},
    {"Periode", "Until", "Location", "Category", "Merk", "Currency", "Supplier", "Price Type By", "Group By", "View", "All"},};

  public static final String textHeaderTable[][] = {
    //0            1               2                 3                4                  5               6         7            8           9          10         11        12       13      14
    {"No", "Stock ID", "Barcode", "Stock Name", "Brand", "Sub Category", "Stock Awal", "Pengiriman", "P. Rtn", "Penerimaan", "Pengembalian", "Sales", "Exchange", "Adj", "End"},
    {"No", "Stock ID", "Barcode", "Stock Name", "Brand", "Sub Category", "First Stock", "Pengiriman", "P. Rtn", "Receive", "Return", "Sales", "Exchange", "Adj", "End"}

};

%>
<%
    String checked="selected";
    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    int type = FRMQueryString.requestInt(request,"type");
    int includeWarehouse = FRMQueryString.requestInt(request, FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_INCLUDE_WAREHOUSE]);
    int includePerpindahan = FRMQueryString.requestInt(request, "PERPINDAHAN");
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
    Date periodeSB = periode.getStartDate();
    Date periodeEB = periode.getEndDate();
    //Category
    Vector caList = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_NAME]);
    Vector masterCat = PstCategory.structureList(caList);
    Material mate = new Material();
    //Location
    String whereLocation = PstDataCustom.whereLocReportViewStock(userId, "user_view_sale_stock_report_location");
    Vector location = PstLocation.list(0, 0, whereLocation, PstLocation.fieldNames[PstLocation.FLD_NAME]);
    //Merk
    Vector merk = PstMerk.list(0, 0, "", PstMerk.fieldNames[PstMerk.FLD_NAME]);
    Merk merkStock = new Merk();
    //Mata Uang
    Vector listCurrency = PstStandartRate.listCurrStandard(0);
    
    //Supplier
    String whereSupplier = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER+ " AND " +PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                       " != " +PstContactList.DELETE;
    Vector listSupplier = PstContactList.listContactByClassType(0,0,whereSupplier,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

    //List Price
    String orderPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX];
    Vector listPrice = PstPriceType.list(0, 0, "", orderPrice);
    String multiPrice = "";
    for(int i=0;i<listPrice.size();i++){
      PriceType prType = (PriceType)listPrice.get(i);
      multiPrice +=multiPrice.isEmpty() ? "" : ",";
      multiPrice +=prType.getOID();
   }
    
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../../../styles/plugin_component.jsp" %>
    <title>Dimata - ProChain POS</title>
    <script>
          function cmdSearch() {
              document.frmsrcReportPotitionStock.command.value="<%=Command.LIST%>";
              document.frmsrcReportPotitionStock.action="src_stock_position.jsp";
              document.frmsrcReportPotitionStock.submit();
          }
            function printFormExcel() {
              
                window.open("print_report_stock_excel.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][6]%>&FRM_FIELD_SUPPLIER_ID=<%=oidSupplier %>&FRM_FIELD_CATEGORY_ID=<%=oidCategory %>&FRM_FIELD_GROUP_BY=<%=groupBy %>&FRM_FIELD_DATE_FROM=<%=tglMulai %>&FRM_FIELD_DATE_TO=<%=tglAkhir %>&PERPINDAHAN=<%=includePerpindahan%>&FRM_FIELD_LOCATION_ID=<%=oidLocation%>&FRM_FIELD_INCLUDE_WAREHOUSE=<%=includeWarehouse%>","prntsrcReportPotitionStock");
            }
      
    </script>
    <style>
      span.input-group-addon {
        padding: 5px;
      }
      button.btn.btn-sm.btn-danger.pull-right {
        margin-left: 10px;
        width: 10%;
      }
      button.btn.btn-sm.btn-success.pull-right {
        margin-left: 10px;
        width: 10%;
      }
      input.form-control.input-sm.datePicker {
        padding: 5px;
      }
      body {background-color: #EEE}

      .box .box-header, .box .box-footer {border-color: lightgray}

      .datetimepicker th {font-size: 14px}
      .datetimepicker td {font-size: 12px}

      .tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
          font-size: 12px;
          padding: 5px;
          border-color: #DDD;
      }

      .tabel-data tbody tr th {
          text-align: center;
          white-space: nowrap;
          text-transform: uppercase;
      }

      .form-group label {padding-top: 5px}
      .table, form {margin-bottom: 0px}
      .middle-inline {text-align: center; white-space: nowrap}
      th {
            text-align: center;
            font-size: 12px;
        }
        thead.headerList {
            background-color: #3c8dbc;
            color: #fff;
            text-align: center;
        }
        td {
            font-size: 14px;
            text-align: center;
            border: 1px solid #428bca !important;
        }
        table.dataTable thead > tr > th {
            padding-right: 25px !important;
        }
        th.sorting {
            padding: 0px !important;
            padding-bottom: 7px !important;
            padding-left: 5px !important;
        }
    </style>
  </head>
  <body>
    <section class="content-header">
      <h1><%=textListGlobal[SESS_LANGUAGE][4]%><small> <%=textListGlobal[SESS_LANGUAGE][5]%> <%=textListGlobal[SESS_LANGUAGE][3]%></small> </h1>
      <ol class="breadcrumb">
        <li><%=textListGlobal[SESS_LANGUAGE][3]%></li>
        <li><%=textListGlobal[SESS_LANGUAGE][4]%></li>
        <li><%=textListGlobal[SESS_LANGUAGE][0]%></li>
      </ol>
    </section>
    <section class="content">
      <div class="box box-primary">
        <div class="box-header with-border">
          <label class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][0]%></label>
          <a class="box-title pull-right" style="margin-right: 15px;font-size: 15px;margin-top: 7px;">Periode Stock : <%=p.getStartDate() %> s/d <%=p.getEndDate() %> </a>
        </div>
        <div class="box-body">
          <div class="row">
            <div class="col-sm-12">
              <form id="frmsrcReportPotitionStock" class="form-horizontal" method="post" action="">
              <input type="hidden" name="command" value="<%=Command.LIST%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="type" value="<%=type%>">
              <input type="hidden" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_USER_ID]%>" value="<%=userId%>">
              <input type="hidden" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_GENERATE_REPORT]%>" value="true">
              <input type="hidden" name="INCLUDE_WAREHOUSE" value="<%=includeWarehouse %>">
              <input type="hidden" name="LOCATION_ID" value="<%=oidLocation %>">
              <input type="hidden" name="START_DATE" value="<%=tglMulai %>">
              <input type="hidden" name="END_DATE" value="<%=tglAkhir %>">
              <input type="hidden" name="CATEGORY" value="<%=oidCategory %>">
              <input type="hidden" name="MERK" value="<%=oidMerk %>">
              <input type="hidden" name="STANDART_ID" value="<%=oidCurrency %>">
              <input type="hidden" name="CONTACT_ID" value="<%=oidSupplier %>">
              <input type="hidden" name="GROUP_BY_CATEGORY" value="<%=groupBy %>">
              <input type="hidden" name="PRICE_TYPE" value="<%=oidPriceType %>">
              
                <!--LEFT SIDE-->
                <div class="col-sm-4">
                  <div class="form-group">
                    <label class="col-sm-3"><%=textListHeader[SESS_LANGUAGE][0]%></label>
                    <div class="col-sm-9">
                      <div class="input-group">
                        <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_FROM] %>" value="<%=tglMulai%>">
                        <span class="input-group-addon"><%=textListHeader[SESS_LANGUAGE][1]%></span>
                        <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_DATE_TO] %>" value="<%=tglAkhir %>">
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-3"><%=textListHeader[SESS_LANGUAGE][3]%></label>
                    <div class="col-sm-9">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_CATEGORY_ID]%>">
                        <option value="0">- Semua Kategori -</option>
                        <%
                        for(int d = 0; d < masterCat.size(); d++ ){
                        Category cat = (Category)masterCat.get(d);
                        %>
                        <option <%=cat.getOID()  == oidCategory ? "selected" : ""%> value="<%=cat.getOID() %>"><%=cat.getName() %></option>
                        <%
                        }
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-3"><%=textListHeader[SESS_LANGUAGE][2]%></label>
                    <div class="col-sm-9">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_LOCATION_ID] %>">
                        <option value="0">- Semua Lokasi -</option>
                        <%
                        for(int d = 0; d < location.size(); d++ ){
                        Location loc = (Location)location.get(d);
                        %>
                        <option <%=loc.getOID() == oidLocation ? "selected" : ""%> value="<%=loc.getOID() %>"><%=loc.getName() %></option>
                        <%
                        }
                        %>
                      </select>
                            <%
                                String checkWarehouse = "";
                                if (iCommand == Command.LIST && includeWarehouse == 1) {
                                    checkWarehouse = "checked";
                                }
                            %>
                      <input type="checkbox" <%= includeWarehouse == 1 ? checkWarehouse : "" %> value="1" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_INCLUDE_WAREHOUSE] %>"> Include Warehouse
                    </div>
                  </div>
                </div>

                <!--CENTER SIDE-->
                <div class="col-md-4">
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][6]%></label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_SUPPLIER_ID] %>">
                        <option value="0">- Semua Supplier -</option>
                        <%
                        for(int d = 0; d < listSupplier.size(); d++ ){
                        ContactList con = (ContactList)listSupplier.get(d);
                        %>
                        <option <%=con.getOID() == oidSupplier ? "selected" : ""%> value="<%=con.getOID() %>"><%=con.getCompName()%></option>
                        <%
                        }
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][5]%></label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_STANDART_ID] %>">
                        <%
                        for(int d = 0; d < listCurrency.size(); d++ ){
                        Vector temp = (Vector)listCurrency.get(d);
                        CurrencyType curr = (CurrencyType) temp.get(0);
                        StandartRate standart = (StandartRate)temp.get(1);
                        %>
                        <option <%=standart.getOID() == oidCurrency ? "selected" : ""%> value="<%=standart.getOID() %>"><%=curr.getCode()%></option>
                        <%
                        }
                        %>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][4]%></label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_MERK_ID] %>">
                        <option value="0">- Semua Merk -</option>
                        <%
                        for(int d = 0; d < merk.size(); d++ ){
                        merkStock = (Merk)merk.get(d);
                        %>
                        <option <%=merkStock.getOID() == oidMerk ? "selected" : ""%> value="<%=merkStock.getOID() %>"><%=merkStock.getName() %></option>
                        <%
                        }
                        %>
                      </select>
                        <%
                          if(useForGreenbowl.equals("1")){
                            String checkPerpindahan = "";
                            if (iCommand == Command.BACK && includePerpindahan == 1) {
                                checkWarehouse = "checked";
                            }
                        %>
                      <input type="checkbox" <%= includePerpindahan == 1 ? checkPerpindahan : "" %> value="1" name="PERPINDAHAN"> Tampilkan Perpindahan
                      <%}%>
                    </div>
                  </div>
                </div>

                <!--RIGHT SIDE-->
                <div class="col-md-4">
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][9]%></label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="View">
                        <option value="0">- Detail -</option>
                        <option value="1">- Summary -</option>
                      </select>
                    </div>
                  </div>
                  <div class="form-group">
                    <label class="col-sm-4"><%=textListHeader[SESS_LANGUAGE][8]%></label>
                    <div class="col-sm-8">
                      <select class="form-control input-sm" name="<%=FrmSrcReportPotitionStock.fieldNames[FrmSrcReportPotitionStock.FRM_FIELD_GROUP_BY] %>">
                        <option <%=(groupBy == SrcSaleReport.GROUP_BY_CATEGORY ? "selected" : "")%> value="<%=SrcSaleReport.GROUP_BY_CATEGORY %>"><%=SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_CATEGORY] %></option>
                        <option <%=(groupBy == SrcSaleReport.GROUP_BY_SUPPLIER ? "selected" : "")%> value="<%=SrcSaleReport.GROUP_BY_SUPPLIER %>"><%=SrcSaleReport.groupMethod[SESS_LANGUAGE][SrcSaleReport.GROUP_BY_SUPPLIER] %></option>
                      </select>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="bg-info text-center"></div>
        </div>
        <div class="box-footer">
          <div class="form-inline">
            <button type="submit" form="frmsrcReportPotitionStock" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> View Data</button>
          </div>
        </div>
      </div>
        <% if (iCommand == Command.LIST) { %>
      <div class="box box-primary">
        <div class="box-header with-border">
          <label class="box-title pull-left">Data <%=textListGlobal[SESS_LANGUAGE][5]%> <%=textListGlobal[SESS_LANGUAGE][3]%></label>
          <!--<button type="button" onclick="print()" class="btn btn-sm btn-danger pull-right">Export Pdf</button>-->
            <a  href="javascript:printFormExcel()"  class="btn btn-sm btn-success pull-right">Export Excel</a>
        </div>
        <div class="box-body">
          <div class="table-responsive">
            <table id="listStock" class="table table-striped table-bordered table-small" style="width:100%;">
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
                    ArrayList<String> data = SessReportPotitionStock.drawTableListPositionStok(whereData, order, periodeStart, periodeEnd, tglMulai, tglAkhir, loc, useForGreenbowl, groupBy, 0);
                    if (data.size()>0){
                        for (int i = 0; i < data.size(); i++) { 		      
                            %><%=data.get(i)%><%
                        }   
                    }
                %>
                            
              </tbody>  
            </table>
          </div>   
          <p></p>
        <div class="box-footer">
        </div>
          <!--<button type="button" onclick="print()" class="btn btn-sm btn-primary pull-right">Cetak Laporan</button>-->
        </div>
      </div>
    <%}%>
    </section>
    <script>
      $(document).ready(function () {
//        $('#listStock').DataTable(); 
        $('.datePicker').datetimepicker({
            format: "yyyy-mm-dd",
            todayBtn: true,
            autoclose: true,
            minView: 2
        });
      });
    </script>
  </body> 
</html>
