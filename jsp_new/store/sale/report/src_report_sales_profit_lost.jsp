<%-- 
    Document   : src_report_sales_profit_lost
    Created on : Apr 5, 2018, 1:59:43 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.common.entity.payment.*"%>
<%@page import="com.dimata.posbo.session.masterdata.*"%>
<%@page import="com.dimata.pos.entity.balance.*"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.pos.entity.billing.*"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesOrder"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//DATA FORM PENCARIAN
    int iCommand = FRMQueryString.requestCommand(request);
    String startDate = FRMQueryString.requestString(request, "FRM_START_DATE");
    String endDate = FRMQueryString.requestString(request, "FRM_END_DATE");
    long idLocation = FRMQueryString.requestLong(request, "FRM_LOCATION");
    long idShift = FRMQueryString.requestLong(request, "FRM_SHIFT");
    long idCurrency = FRMQueryString.requestLong(request, "FRM_CURRENCY_TYPE");
    int saleType = FRMQueryString.requestInt(request, "FRM_SALE_TYPE");

    if (iCommand == Command.NONE) {
        startDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
        endDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
        saleType = SrcSaleReport.TYPE_ALL_SALES;
    }
    
    String where = "" // alias : bill main > BM , bill detail > BD , material > PM
			+ " BM." + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + " != 1 "
			+ " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " != " + Material.MATERIAL_TYPE_EMAS_LANTAKAN
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
            + "";

    if (saleType == PstBillMain.TRANS_TYPE_CREDIT) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT;
    } else if (saleType == PstBillMain.TRANS_TYPE_CASH) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH;
    }
    if (startDate.length() > 0 && endDate.length() > 0) {
        where += " AND DATE(BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + startDate + "' AND '" + endDate + "'";
    }
    if (idLocation > 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + idLocation;
    }
    if (idShift > 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + idShift;
    }
    if (idCurrency > 0) {
        where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + idCurrency;
    }

    String order = ""
            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
            + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            + "";

    Vector listPenjualan = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, where, order);
    
    //==========================================================================

    //set value form input lokasi
    Vector val_locationid = new Vector(1, 1);
    Vector key_locationid = new Vector(1, 1);

    String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location", "");
    Vector vt_loc = PstLocation.listLocationStore(0, 0, whereLocViewReport, PstLocation.fieldNames[PstLocation.FLD_NAME]);

    boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location", "0");
    if (checkDataAllLocReportView) {
        val_locationid.add("0");
        key_locationid.add("Semua Lokasi");
    }

    for (int d = 0; d < vt_loc.size(); d++) {
        Location loc = (Location) vt_loc.get(d);
        val_locationid.add("" + loc.getOID() + "");
        key_locationid.add(loc.getName());
    }

    //set value form input tipe
    Vector val_saletype = new Vector(1, 1);
    Vector key_saletype = new Vector(1, 1);

    val_saletype.add("-1");
    key_saletype.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_ALL_SALES]);

    val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
    key_saletype.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CASH]);

    val_saletype.add("" + PstBillMain.TRANS_TYPE_CREDIT);
    key_saletype.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CREDIT]);
    
    //set value form input shift
    Vector val_shiftid = new Vector(1, 1);
    Vector key_shiftid = new Vector(1, 1);
    Vector vt_shift = PstShift.list(0, 0, "", PstShift.fieldNames[PstShift.FLD_NAME]);
    val_shiftid.add("0");
    key_shiftid.add("Semua Shift");
    for (int d = 0; d < vt_shift.size(); d++) {
        Shift shift = (Shift) vt_shift.get(d);
        val_shiftid.add("" + shift.getOID() + "");
        key_shiftid.add(shift.getName());
    }

    //set value form input currency
    Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
    Vector vectCurrVal = new Vector(1, 1);
    Vector vectCurrKey = new Vector(1, 1);
    vectCurrVal.add("0");
    vectCurrKey.add("Semua Mata Uang");    
    for (int i = 0; i < listCurr.size(); i++) {
        CurrencyType currencyType = (CurrencyType) listCurr.get(i);
        vectCurrKey.add(currencyType.getCode());
        vectCurrVal.add("" + currencyType.getOID());
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <style>
            
            body {background-color: #eeeeee}
            
            .box-header.with-border {border-bottom-color: lightgray}
            .box-footer {border-top-color: lightgray}
            
            .form {margin-top: 10px}
            
            .table {font-size: 12px}
            .table th {background-color: lightgray}
            
            .datetimepicker th {font-size: 14px}
            .datetimepicker td {font-size: 14px}
            
            .tabel-data th {text-align: center}
            
        </style>
    </head>
    <body>
        <div class="col-md-12">
            
            <h4>Laporan Penjualan Laba/Rugi</h4>
            
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>
                
                <div class="box-body">
                    <form id="formSearch" class="form form-horizontal" method="post" action="?command=<%=Command.LIST %>">
                        
                        <div class="form-group">
                            <label class="control-label col-sm-1">Tanggal</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal Awal" class="form-control input-sm datepick" name="FRM_START_DATE" value="<%=startDate%>">
                                </div>
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal Akhir" class="form-control input-sm datepick" name="FRM_END_DATE" value="<%=endDate%>">
                                </div>
                            </div>
                            
                            <label class="control-label col-sm-1">Tipe</label>
                            <div class="col-sm-2">
                                <%=ControlCombo.draw("FRM_SALE_TYPE", null, "" + saleType, val_saletype, key_saletype, "", "form-control input-sm")%>
                            </div>
                            
                            <label class="control-label col-sm-1">Currency</label>
                            <div class="col-sm-2">
                                <%=ControlCombo.draw("FRM_CURRENCY_TYPE", null, "" + idCurrency, vectCurrVal, vectCurrKey, "", "form-control input-sm")%>
                            </div>
                        </div>
                        
                        <div class="form-group">
                            <label class="control-label col-sm-1">Lokasi</label>
                            <div class="col-sm-4">
                                <%=ControlCombo.draw("FRM_LOCATION", null, "" + idLocation, val_locationid, key_locationid, "", "form-control input-sm")%>
                            </div>                            
                            
                            <label class="control-label col-sm-1">Shift</label>
                            <div class="col-sm-2">
                                <%=ControlCombo.draw("FRM_SHIFT", null, "" + idShift, val_shiftid, key_shiftid, "", "form-control input-sm")%></td>
                            </div>
                        </div>
                        
                    </form>
                </div>
                
                <div class="box-footer">
                    <button type="button" id="btnSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                </div>
            </div>
                            
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Hasil Pencarian</h3>
                </div>
                
                <div style="overflow-x:auto;">
                    <table class="table table-bordered tabel-data">
                        <tr>
                            <th>Tanggal</th>
                            <th>No. Faktur</th>
                            <th>Kode Barang</th>
                            <th>Nama Barang</th>
                            <th>Sales Person</th>
                            <th>Sales Manager</th>
                            <th>Qty</th>
                            <th>Berat</th>
                            <th>Kadar</th>
                            <th>Harga Beli</th>
                            <th>Harga Pasar</th>
                            <th>Harga Jual</th>
                            <th>Selisih Beli</th>
                            <th>Selisih Pasar</th>                            
                        </tr>
                        
                        <%                            
                            double totalQty = 0;
                            double totalBerat = 0;
                            double totalHargaBeli = 0;
                            double totalHargaPasar = 0;
                            double totalHargaJual = 0;
                            double totalSelisihBeli = 0;
                            double totalSelisihPasar = 0;
                            
                            for (int i = 0; i < listPenjualan.size(); i++) {
                                Vector v = (Vector) listPenjualan.get(i);
                                BillMain bm = (BillMain) v.get(0);
                                Billdetail bd = (Billdetail) v.get(1);
                                Material m = (Material) v.get(2);  
                                AppUser ap = new AppUser();
                                try {
                                    ap = PstAppUser.fetch(bm.getAppUserSalesId());
                                  } catch (Exception e) {
                                  }

                                Kadar k = new Kadar();
                                String kadarInfo = "";
                                if (m.getPosKadar() != 0 && PstKadar.checkOID(m.getPosKadar())) {
                                    k = PstKadar.fetchExc(m.getPosKadar());
                                    kadarInfo = "" + k.getKadar() + "%";
                                }
                                
//                                Sales s = new Sales();
//                                if (bm.getSalesCode().length() > 0 && PstSales.checkOID(Long.valueOf(bm.getSalesCode()))) {
//                                    s = PstSales.fetchExc(Long.valueOf(bm.getSalesCode()));
//                                }
                                
                                String manager = "";
                                if (bm.getCashCashierId() > 0 && PstCashCashier.checkOID(bm.getCashCashierId())) {
                                    CashCashier cashier = PstCashCashier.fetchExc(bm.getCashCashierId());
                                    manager = cashier.getSpvName();
                                }
                                
                                BillMain bmOrder = new BillMain();
                                if (bm.getParentSalesOrderId() > 0 && PstBillMain.checkOID(bm.getParentSalesOrderId())) {
                                    bmOrder = PstBillMain.fetchExc(bm.getParentSalesOrderId());
                                }
                                
                                String itemName = SessMaterial.setItemNameForLitama(m.getOID());
                                
                                double hargaPasar = 0;
                                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                                    
                                    Vector<EmasLantakan> listEmasLantakan = PstEmasLantakan.list(0, 0, "" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF] + " = 0", "");                                
                                    if (!listEmasLantakan.isEmpty()) {
                                        double hargaTengah = listEmasLantakan.get(0).getHargaTengah();
                                        double kadar = k.getKadar();
                                        double berat = bd.getBerat();
                                        hargaPasar = hargaTengah * (kadar/100) * berat;
                                    }

                                } else if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                    double hargaBeliBerlian = m.getAveragePrice();
                                    double rateUsd = PstStandartRate.getStandardRate();                                    
                                    double ratePasarBerlian = 0;
                                    Vector<RatePasarBerlian> listRatePasarBerlian = PstRatePasarBerlian.list(0, 0, "", "");
                                    if (!listRatePasarBerlian.isEmpty()) {
                                        ratePasarBerlian = listRatePasarBerlian.get(0).getRate();
                                    }
                                    hargaPasar = hargaBeliBerlian / rateUsd * ratePasarBerlian;
                                }
                                
                                double hargaBeli = m.getDefaultCost();
                                double hargaJual = bd.getTotalPrice() + bd.getTotalTax();
                                double selisihBeli = hargaJual - hargaBeli;
                                double selisihPasar = hargaJual - hargaPasar;
                                
                                totalQty += bd.getQty();
                                totalBerat += bd.getBerat();
                                totalHargaBeli += Math.round(m.getDefaultCost());
                                totalHargaPasar += Math.round(hargaPasar);
                                totalHargaJual += Math.round(bd.getTotalPrice() + bd.getTotalTax());
                                totalSelisihBeli += Math.round(selisihBeli);
                                totalSelisihPasar += Math.round(selisihPasar);
                                
                        %>
                        
                        <tr>
                            <td><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                            <td><%=bm.getInvoiceNumber() %></td>
                            <td><%=m.getSku() %></td>
                            <td><%=itemName %></td>
                            <td><%=ap.getFullName()%></td>
                            <td><%=manager %></td>
                            <td class="text-center"><%=String.format("%,.0f", bd.getQty()) %></td>
                            <td class="text-right"><%=String.format("%,.3f", bd.getBerat()) %></td>
                            <td><%=kadarInfo %></td>
                            <td class="text-right"><%=String.format("%,.0f", hargaBeli) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", hargaPasar)%>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", hargaJual) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", selisihBeli) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", selisihPasar) %>.00</td>                            
                        </tr>
                        
                        <%}%>
                        
                        <%if(listPenjualan.isEmpty()) {%>
                        <tr>
                            <td colspan="19" class="text-center"><b>Tidak ada data penjualan</b></td>
                        </tr>
                        <%} else {%>
                        
                        <tr>
                            <td class="text-right" colspan="6"><b>TOTAL :</b></td>
                            <td class="text-center"><b><%=String.format("%,.0f", totalQty) %></b></td>
                            <td class="text-right"><b><%=String.format("%,.3f", totalBerat) %></b></td>
                            <td></td>
                            <td class="text-right"><b><%=String.format("%,.0f", totalHargaBeli) %>.00</b></td>
                            <td class="text-right"><b><%=String.format("%,.0f", totalHargaPasar) %>.00</b></td>
                            <td class="text-right"><b><%=String.format("%,.0f", totalHargaJual) %>.00</b></td>
                            <td class="text-right"><b><%=String.format("%,.0f", totalSelisihBeli) %>.00</b></td>
                            <td class="text-right"><b><%=String.format("%,.0f", totalSelisihPasar) %>.00</b></td>
                        </tr>
                        
                        <%}%>
                        
                    </table>
                    </div>
                </div>
                
                <%if(!listPenjualan.isEmpty()) {%>                        
                <div class="box-footer">
                    <button type="button" id="btnPrint" class="btn btn-sm btn-primary pull-right"><i class="fa fa-print"></i>&nbsp; Cetak</button>
                </div>
                <%}%>
                
            </div>
        </div>        
    </body>
            
    <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
    <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
    <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
    <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script>
        
        $('.datepick').datetimepicker({
            autoclose: true,
            todayBtn: true,
            format: 'yyyy-mm-dd',
            minView: 2
        });
        
        $('#btnSearch').click(function() {
            $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
            $('#formSearch').submit();
        });
        
        $('#btnPrint').click(function() {
            var data = $('#formSearch').serialize();
            window.open("print_out_report_sales_profit_lost.jsp?"+data,"print_out_sales_lost_profit");
        });
        
    </script>

</html>
