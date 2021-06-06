<%-- 
    Document   : src_report_service
    Created on : Apr 5, 2018, 3:55:58 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.pos.entity.payment.*"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.pos.entity.billing.*"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesOrder"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.payment.*"%>
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
    
    //get data
    String where = "" // alias : bill main > BM , bill detail > BD , material > PM
            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE          
            + " AND ("
            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH
            + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
            + ")"
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
            + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES            
            + "";

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
    
    where += " AND ("
            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + " = 1" 
            + " OR BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " IN ("
            + " SELECT " + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]
            + " FROM " + PstBillDetail.TBL_CASH_BILL_DETAIL
            + " WHERE " + PstBillDetail.fieldNames[PstBillDetail.FLD_MATERIAL_TYPE] + " = " + PstMaterial.MAT_TYPE_SERVICE
            + ")"
            + ")";
        
    String order = ""
            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
            + ", BM." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
            + "";

    Vector listService = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, where, order);
    
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
            
            .tabel_data th {text-align: center}
            
        </style>
    </head>
    <body>
        <div class="col-md-12">
            
            <h4>Laporan Service</h4>
            
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
                            <%--
                            <label class="control-label col-sm-1">Tipe</label>
                            <div class="col-sm-2">
                                <%=ControlCombo.draw("FRM_SALE_TYPE", null, "" + saleType, val_saletype, key_saletype, "", "form-control input-sm")%>
                            </div>
                            --%>
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
                
                <div class="box-body">
                    <div style="overflow-x:auto;">
                    <table class="table table-bordered tabel_data">
                        <tr>
                            <th>Tanggal</th>
                            <th>No. Bill</th>
                            <th>Customer</th>
                            <th>Nama Barang</th>
                            <th>Berl.</th>
                            <th>Payment</th>
                            <th>Sal. Pers.</th>
							<th>Berat</th>
							<th>Harga @</th>
							<th>Jumlah</th>
                            <th>Ongkos</th>
                            <th>Susutan</th>
                            <th>Potongan</th>
                            <th>Pajak</th>
                            <th>Uang Muka</th>
                            <th>Additional Charge</th>
                            <th>Total</th>
                            <th>Keterangan</th>
                        </tr>
                        
                        <%
							double totalBerat = 0.0;
							double totalPrice = 0.0;
							double totalCost = 0.0;
							double totalSusutan = 0.0;
							double totalDisc = 0.0;
							double totalTax = 0.0;
							double totalDp = 0.0;
							double totalAddCharge = 0.0;
							double totalAll = 0.0;
                            for (int i = 0; i < listService.size(); i++) {
                                Vector v = (Vector) listService.get(i);
                                BillMain bm = (BillMain) v.get(0);
                                Billdetail bd = (Billdetail) v.get(1);
                                Material m = (Material) v.get(2);
                                AppUser ap = new AppUser();
                                try {
                                    ap = PstAppUser.fetch(bm.getAppUserSalesId());
                                  } catch (Exception e) {
                                  }
                                //get ulang bill detail                                
                                if (PstBillDetail.checkOID(bd.getOID())) {
                                    bd = PstBillDetail.fetchExc(bd.getOID());
                                }
                                //get ulang material                                
                                if (PstMaterial.checkOID(m.getOID())) {
                                    m = PstMaterial.fetchExc(m.getOID());
                                }
                                //get sales person
//                                Sales s = new Sales();
//                                if (bm.getSalesCode().length() > 0 && PstSales.checkOID(Long.valueOf(bm.getSalesCode()))) {
//                                    s = PstSales.fetchExc(Long.valueOf(bm.getSalesCode()));
//                                }
                                //get order number                                
                                String orderNumber = "-";
                                if (bm.getParentSalesOrderId() > 0 && PstBillMain.checkOID(bm.getParentSalesOrderId())) {
                                    BillMain bmOrder = PstBillMain.fetchExc(bm.getParentSalesOrderId());
                                    orderNumber = bmOrder.getInvoiceNo();
                                }
                                //get currency type
                                CurrencyType ct = new CurrencyType();
                                if (bm.getCurrencyId() > 0 && PstCurrencyType.checkOID(bm.getCurrencyId())) {
                                    ct = PstCurrencyType.fetchExc(bm.getCurrencyId());
                                }
                                //get item name
                                String itemName = m.getName(); /*SessMaterial.setItemNameForLitama(m.getOID());*/                                
                                //check if berlian
                                String statusBerlian = "N";
                                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                                    statusBerlian = "Y";
                                }
                                //get paymenttype
                                String paymentType = SessSalesOrder.getPaymentType(bm.getOID());
                                //count total
                                double dp = SessSalesOrder.getDownPayment(bm.getOID());
                                double total = (((bd.getItemPrice() * bd.getBerat()) + bd.getSusutanPrice() + bd.getCost() - bd.getDisc()) - dp) + bd.getTotalTax(); /*bd.getCost() - bd.getDisc() + bd.getTotalTax();*/
                                double addCharge = SessSalesOrder.getAddCharge(bm.getOID());
                                double totalPayment = SessSalesOrder.getPayment(bm.getOID());
                                
								totalBerat = totalBerat + bd.getBerat();
								totalPrice = totalPrice +  bd.getItemPrice();
								totalCost = totalCost + bd.getCost();
								totalSusutan = totalSusutan + bd.getSusutanPrice();
								totalDisc = totalDisc + bd.getDisc();
								totalTax = totalTax + bd.getTotalTax();
								totalDp = totalDp + dp;
								totalAddCharge = totalAddCharge + addCharge;
								totalAll = totalAll + total;

                        %>
                        
                        <tr>
                            <td><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                            <td><%=bm.getInvoiceNo() %></td>
                            <td><%=bm.getGuestName()%></td>
                            <td><%=itemName %></td>
                            <td class="text-center"><%=statusBerlian %></td>
                            <td><%=paymentType %></td>
                            <td><%=ap.getFullName()%></td>
							<td class="text-right"><%=String.format("%,.3f", bd.getBerat()) %></td>
							<td class="text-right"><%=String.format("%,.0f", bd.getItemPrice()) %>.00</td>
							<td class="text-right"><%=String.format("%,.0f", (bd.getBerat() * bd.getItemPrice())) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", bd.getCost()) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", bd.getSusutanPrice()) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", bd.getDisc()) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", bd.getTotalTax()) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", dp) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", addCharge) %>.00</td>
                            <td class="text-right"><%=String.format("%,.0f", total) %>.00</td>
                            <td><%=bd.getNote()%></td>
                        </tr>
                        
                        <%}%>
                        
                        <%if(listService.isEmpty()) {%>
                        <tr>
                            <td colspan="19" class="text-center"><b>Tidak ada data servis</b></td>
                        </tr>
                        <%} else {%>
							<tr>
								<td colspan="7" class="text-right"><b>Total</b></td>
								<td class="text-right"><b><%=String.format("%,.3f", totalBerat) %></b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalPrice) %>.00</td>
								<td class="text-right"><b><%=String.format("%,.0f", (totalBerat * totalPrice)) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalCost) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalSusutan) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalDisc) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalTax) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalDp) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalAddCharge) %>.00</b></td>
								<td class="text-right"><b><%=String.format("%,.0f", totalAll) %>.00</b></td>
								<td></td>
							</tr>
						<%}%>
                    </table>
                    </div>
                </div>
                
                <%if(!listService.isEmpty()) {%>
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
            window.open("print_out_report_service.jsp?"+data,"print_out_sales_lost_profit");
        });
        
    </script>

</html>
