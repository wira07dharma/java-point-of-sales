<%-- 
    Document   : src_report_sales_return
    Created on : Apr 5, 2018, 3:57:21 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKadar"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesOrder"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.posbo.entity.masterdata.Shift"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstShift"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
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
            
        </style>
    </head>
    <body>
        <div class="col-md-12">
            
            <h4>Laporan Retur Penjualan</h4>
            
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
        <section class="content">   
            <div class="row">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Data Laporan</h3>
                </div>
                
                
                <div class="box-body">
                    <table class="table table-bordered" style="width:100%">
                        <tr>
                            <th style="width: 1%">No.</th>
                            <th>No. Penjualan</th>
                            <th>Tgl Penjualan</th>
                            <th>Tgl Retur</th>
                            <th>SKU</th>
                            <th>Nama Barang</th>
                            <th>No. Retur</th>
                            <th>Kadar</th>
                            <th>Berat Awal</th>
                            <th>Berat Baru</th>
                            <th>Berat Selisih</th>
                            <th>Harga / gr</th>
                            <th>Jumlah</th>
                            <th>Potongan</th>
                            <th>Total Nilai</th>
                            <th>Rate</th>
                            <th>Total (Rp)</th>
                            <th>Kode Baru</th>
                            <th>Keterangan</th>
                        </tr>
                        
                        <%
                            /* String where = "" // alias : bill main > BM , bill detail > BD , material > PM
                                    + "("
                                    + "("
                                    + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_RETUR
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE 
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                    + ")"
                                    + "OR"
                                    + "("
                                    + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_RETUR
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE 
                                    + " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                                    + ")"
                                    + ")"
                                    + "";
                            
                            if (saleType == PstBillMain.TRANS_TYPE_CREDIT) {
                                //where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT;
                            } else if (saleType == PstBillMain.TRANS_TYPE_CASH) {
                                //where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH;
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
                                where += " AND BM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + "=" + idCurrency;
                            } */
                            
                            String where = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE]+"="+PstMatReceive.SOURCE_FROM_BUYBACK;
                            if (startDate.length() > 0 && endDate.length() > 0) {
                                where += " AND DATE(rec." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") BETWEEN '" + startDate + "' AND '" + endDate + "'";
                            }
                            if (idLocation > 0) {
                                where += " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + idLocation;
                            }
                            if (idShift > 0) {
                                where += " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + idShift;
                            }
                            if (idCurrency > 0) {
                                where += " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + "=" + idCurrency;
                            } 
                            
                            String order = ""
                                    + " bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                                    + ", bm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
                                    + "";
                            
                            //Vector listPenjualan = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, where, order);
                            Vector listPenjualan = SessSalesOrder.listBuyBack(0, 0, where, order);
                            session.putValue("listPenjualan", listPenjualan);
                            session.putValue("startDate", startDate);
                            session.putValue("endDate", endDate);
                            double subBeratAwal = 0;
                            double subBeratBaru = 0;
                            double subHarga = 0;
                            double subJumlah = 0;
                            double subPotongan = 0;
                            double subTotalNilai = 0;
                            double subTotal = 0;
                            
                            double grandBeratAwal = 0;
                            double grandBeratBaru = 0;
                            double granndTotal = 0;
                            for (int i = 0; i < listPenjualan.size(); i++) {
                                Vector v = (Vector) listPenjualan.get(i);
                                BillMain bm = (BillMain) v.get(0);
                                MatReceive rc = (MatReceive) v.get(1);
                                MatReceiveItem it = (MatReceiveItem) v.get(2);
                                Material m = (Material) v.get(3);
                                Material nm = (Material) v.get(4);
                                
                                double kadarValue;
				try {
					kadarValue = PstKadar.fetchExc(m.getPosKadar()).getKadar();
				} catch (Exception ex) {
					kadarValue = 0;
				}
                                
                                double jumlah = 0;
                                if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN){
                                    jumlah = rc.getHel();
                                } else {
                                    jumlah = rc.getHel() * it.getBerat();
                                }
                                
                                subBeratAwal += it.getBeratAwal();
                                subBeratBaru += it.getBerat();
                                subHarga += rc.getHel();
                                subJumlah += jumlah;
                                subPotongan += it.getDiscount();
                                subTotalNilai += jumlah-it.getDiscount();
                                subTotal += (jumlah-it.getDiscount())*rc.getTransRate();
                                
                                grandBeratAwal += subBeratAwal;
                                grandBeratBaru += subBeratBaru;
                                granndTotal += subTotal;
                        %>

                        <tr>
                            <td><%=(i+1)%></td>
                            <td><%=bm.getInvoiceNumber()%></td>
                            <td><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                            <td><%=Formater.formatDate(rc.getReceiveDate(), "yyyy/MM/dd") %></td>
                            <td><%=m.getSku()%></td>
                            <td><%=SessMaterial.setItemNameForLitama(m.getOID())%></td>
                            <td><%=rc.getRecCode()%></td>
                            <td><%=String.format("%.2f",kadarValue)%></td>
                            <td><%=String.format("%.3f",it.getBeratAwal())%></td>
                            <td><%=String.format("%.3f",it.getBerat())%></td>
                            <td><%=String.format("%.3f",(it.getBeratAwal()-it.getBerat()))%></td>
                            <td><%=String.format("%,.2f", rc.getHel())%></td>
                            <td><%=String.format("%,.2f", jumlah)%></td>
                            <td><%=String.format("%,.2f", it.getDiscount())%></td>
                            <td><%=String.format("%,.2f", (jumlah-it.getDiscount()))%></td>
                            <td><%=String.format("%.2f",rc.getTransRate())%></td>
                            <td><%=String.format("%,.2f", (jumlah-it.getDiscount())*rc.getTransRate())%></td>
                            <td><%=nm.getBarCode()%></td>
                            <td><%=rc.getRemark()%></td>
                        </tr>
                        
                        <%}%>
                        
                        <%if(listPenjualan.isEmpty()) {%>
                        <tr>
                            <td colspan="19" class="text-center"><b>Tidak ada data retur penjualan</b></td>
                        </tr>
                        <%}%>
                        
                        <tr>
                            <td></td>
                            <td><b>Total :</b></td>
                            <td colspan="6"></td>
                            <td><strong><%=String.format("%.3f",subBeratAwal)%></strong></td>
                            <td><strong><%=String.format("%.3f",subBeratBaru)%></strong></td>
                            <td></td>
                            <td><strong><%=String.format("%,.2f",subHarga)%></strong></td>
                            <td><strong><%=String.format("%,.2f",subJumlah)%></strong></td>
                            <td><strong><%=String.format("%,.2f",subPotongan)%></strong></td>
                            <td><strong><%=String.format("%,.2f",subTotalNilai)%></strong></td>
                            <td></td>
                            <td><strong><%=String.format("%,.2f",subTotal)%></strong></td>
                            <td colspan="2"></td>
                        </tr>
                    </table>
                </div>
                
                <div class="box-footer">
                    <button type="button" id="btnPrint" class="btn btn-sm btn-primary pull-right"><i class="fa fa-print"></i>&nbsp; Cetak</button>
                </div>
            </div>
            </div>
        </section>
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
            window.open("print_out_report_sales_return.jsp","print_out_sales_lost_profit");
        });
        
    </script>

</html>
