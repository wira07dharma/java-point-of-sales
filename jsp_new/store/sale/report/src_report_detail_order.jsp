<%-- 
    Document   : src_report_detail_order
    Created on : Apr 5, 2018, 3:52:52 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.pos.entity.payment.*"%>
<%@page import="com.dimata.pos.entity.billing.*"%>
<%@page import="com.dimata.common.entity.contact.*"%>
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

<%!
    public String getTanggalSelesai(long idItem) {
        String tgl = "-";
        String whereStatusBatal = ""
                + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " > 0"
                + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + idItem
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_FINISH
                + "";
        Vector listInvoice = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, whereStatusBatal, null);
        for (int v = 0; v < listInvoice.size(); v++) {
            Vector vec = (Vector) listInvoice.get(v);
            BillMain b = (BillMain) vec.get(0);
            tgl = b.getBillDate() == null ? "-" : "" + Formater.formatDate(b.getBillDate(), "yyyy/MM/dd");
        }
        return tgl;
    }
%>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    String startDate = FRMQueryString.requestString(request, "FRM_START_DATE");
    String endDate = FRMQueryString.requestString(request, "FRM_END_DATE");
    long idLocation = FRMQueryString.requestLong(request, "FRM_LOCATION");
    long idShift = FRMQueryString.requestLong(request, "FRM_SHIFT");
    long idCurrency = FRMQueryString.requestLong(request, "FRM_CURRENCY_TYPE");
    int view = FRMQueryString.requestInt(request, "FRM_VIEW");

    if (iCommand == Command.NONE) {
        startDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
        endDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
    }

    Vector listPesanan = new Vector();
    if (iCommand == Command.LIST) {
        //get data
        String where = ""                                  
                + "((" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0       
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT //1
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED //2
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES //1       
				+ " ) OR "
				+ "(" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0       
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT //1
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_OPEN //1
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_FINISH //2       
				+ " ) OR "
				+ "(" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0       
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT //1
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE //0
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_FINISH //2       
				+ " ) OR "
				+ "(" + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE //0       
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH //0
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_DELETED //2
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES //1       
				+ " ))"
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " = 0"                                    
                + "";

        if (startDate.length() > 0 && endDate.length() > 0) {
            where += " AND DATE(" + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + ") BETWEEN '" + startDate + "' AND '" + endDate + "'";
        }
        if (idLocation > 0) {
            where += " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + " = " + idLocation;
        }
        if (idShift > 0) {
            where += " AND " + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + " = " + idShift;
        }
        if (idCurrency > 0) {
            where += " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = " + idCurrency;
        }

        where += " GROUP BY " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID];

        String order = ""
                + " " + PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]
                + ", " + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO]
                + "";

        listPesanan = PstBillMain.list(0, 0, where, order);
    }
    
    //=============================DATA FORM PENCARIAN==========================
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
    
    String selected = "";
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
            
            .long_text {overflow: hidden; white-space: nowrap; text-overflow: ellipsis;}
            
        </style>
    </head>
    <body>
        <div class="col-md-12">
            
            <h4>Laporan Pemesanan</h4>
            
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
                            
                            <label class="control-label col-sm-1">Currency</label>
                            <div class="col-sm-2">
                                <%=ControlCombo.draw("FRM_CURRENCY_TYPE", null, "" + idCurrency, vectCurrVal, vectCurrKey, "", "form-control input-sm")%>
                            </div>
                                                        
                            <label class="control-label col-sm-1">View</label>
                            <div class="col-sm-2">
                                <select class="form-control input-sm" name="FRM_VIEW">
                                    <%selected = ""; if(view==1){selected="selected";}%>
                                    <option <%=selected%> value="1">Summary</option>
                                    <%selected = ""; if(view==2){selected="selected";}%>
                                    <option <%=selected%> value="2">Detail</option>
                                </select>
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
                         
            <%if (iCommand == Command.LIST) {%>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">Hasil Pencarian</h3>
                </div>
                
                <div class="box-body">
                    <div style="overflow-x:auto;">
                        <table class="table table-bordered tabel_data">
                            <%if(view==1){%>
                            <tr>                            
                                <th>No. Order</th>
                                <th>Tgl Order</th>
                                <th>Tgl Selesai</th>
                                <th>Cust.</th>
                                <th>Addr.</th>
                                <th>Phone</th>
                                <th>Nilai Pesanan</th>
                                <th>DP</th>
                                <th>Add. Charge</th>
                                <th>DP + Charge</th>
                                <th>Sisa</th>
                                <th>Cur.</th>
                                <th>Rate</th>
                                <th>DP (Rp)</th>
                                <th>Keterangan</th>                            
                            </tr>
                            <%} else if(view==2) {%>
                            <tr>
                                <th>Tgl Order</th>
                                <th>Tgl Selesai</th>
                                <th>Cust.</th>
                                <th>Addr.</th>
                                <th>Phone</th>
                                <th>Kode Barang</th>
                                <th>Nama Barang</th>
                                <th>Berat</th>
                                <th>Kadar</th>
                                <th>Warna</th>
                                <th>Qty</th>
                                <th>Status Selesai</th>
                                <th>Status Batal</th>
                                <th style="width: 15%">Keterangan</th>                            
                            </tr>
                            <%}%>

                            <%
                                double totalDp = 0;
                                for (int i = 0; i < listPesanan.size(); i++) {
                                    BillMain bm = (BillMain) listPesanan.get(i);
                                    ContactList customer = new ContactList();
                                    if (bm.getCustomerId() > 0) {
                                        customer = PstContactList.fetchExc(bm.getCustomerId());
                                    }

                                    if (view == 1) {

                                        String tglSelesai = "" + Formater.formatDate(bm.getDateTermOfPayment(), "yyyy/MM/dd");
                                        /*
                                        Vector<Billdetail> dataDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + bm.getOID(), "");                                
                                        for (Billdetail bd : dataDetail) {
                                            tglSelesai = getTanggalSelesai(bd.getMaterialId());
                                            if (tglSelesai.equals("-")) {break;}
                                        }
                                        */
                                        //------------------------------------------
                                        CurrencyType ct = new CurrencyType();
                                        if (bm.getCurrencyId() > 0 && PstCurrencyType.checkOID(bm.getCurrencyId())) {
                                            ct = PstCurrencyType.fetchExc(bm.getCurrencyId());
                                        }
                                        //------------------------------------------
                                        double nilaiPesanan = SessSalesOrder.getTotalBillOrder(bm.getOID());
                                        double dp = SessSalesOrder.getDownPayment(bm.getOID());
                                        totalDp += dp;
                                        double addCharge = SessSalesOrder.getAddCharge(bm.getOID());
										boolean isCanceled = SessSalesOrder.isCanceled(bm.getOID());
										if (bm.getDocType() == PstBillMain.TYPE_INVOICE && bm.getTransctionType() == PstBillMain.TRANS_TYPE_CASH
												&& bm.getTransactionStatus() == PstBillMain.TRANS_STATUS_DELETED && bm.getStatusInv() == PstBillMain.INVOICING_ON_PROSES){
											isCanceled = true;
										}
                            %>

                            <tr>
                                <td><%=bm.getInvoiceNo() %></td>
                                <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                                <td class="text-center"><%=tglSelesai %></td>
                                <td><%=customer.getPersonName()%></td>
                                <td class="long_text" style="max-width: 100px" title="<%=customer.getHomeAddr() %>"><%=customer.getHomeAddr() %></td>
                                <td><%=customer.getHomeTelp() %> / <%=customer.getHomeMobilePhone() %></td>
                                <td class="text-right"><%=String.format("%,.0f", nilaiPesanan) %>.00</td>
                                <td class="text-right"><%=String.format("%,.0f", dp) %>.00</td>
                                <td class="text-right"><%=String.format("%,.0f", addCharge) %>.00</td>
                                <td class="text-right"><%=String.format("%,.0f", dp + addCharge) %>.00</td>
                                <td class="text-right"><%=String.format("%,.0f", nilaiPesanan - dp) %>.00</td>
                                <td class="text-center"><%=ct.getCode() %></td>
                                <td class="text-right"><%=String.format("%,.0f", bm.getRate()) %>.00</td>
                                <td class="text-right"><%=String.format("%,.0f", dp) %>.00</td>
                                <td><%=(isCanceled ? "Canceled": bm.getNotes() )%></td>
                            </tr>

                            <%
                                    } else if (view == 2) {
                            %>

                            <tr>
                                <td colspan="14" style="background-color: #eeeeee"><b>No. Order : <%=bm.getInvoiceNo() %></b></td>                            
                            </tr>

                            <%
                                Vector<Billdetail> listPesananDetail = PstBillDetail.list(0, 0, "" + PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + bm.getOID(), "");
                                double totalBerat = 0;
                                int totalQty = 0;                            
                                for (Billdetail detailOrder : listPesananDetail) {

                                    totalQty += detailOrder.getQty();
                                    totalBerat += detailOrder.getBerat();
                                    //----------------------------------------------
                                    long idItem = detailOrder.getMaterialId();
                                    Material item = new Material();
                                    if (idItem != 0 && PstMaterial.checkOID(idItem)) {
                                        item = PstMaterial.fetchExc(idItem);
                                    }
                                    //----------------------------------------------
                                    Kadar k = new Kadar();
                                    String kadarInfo = "";
                                    if (PstKadar.checkOID(item.getPosKadar())) {
                                        k = PstKadar.fetchExc(item.getPosKadar());
                                        kadarInfo =  k.getKadar() + "%";
                                    }
                                    //----------------------------------------------
                                    Color c = new Color();
                                    String warna = "";
                                    if (PstColor.checkOID(item.getPosColor())) {
                                        c = PstColor.fetchExc(item.getPosColor());
                                        warna = c.getColorName();
                                    }
                                    //----------------------------------------------
                                    String tglSelesai = Formater.formatDate(bm.getDateTermOfPayment(), "yyyy/MM/dd");
                                    String statusSelesai = "N";
                                    String statusBatal = "N";
                                    //----------------------------------------------
                                    String whereStatusBatal = ""
                                            + " BM." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " > 0"
                                            + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + item.getOID();
                                    Vector listInvoice = SessSalesOrder.listJoinBillMainDetailMaterial(0, 0, whereStatusBatal, null);
                                    for (int v = 0; v < listInvoice.size(); v++) {
                                        Vector vec = (Vector) listInvoice.get(v);
                                        BillMain b = (BillMain) vec.get(0);
                                        statusSelesai = b.getStatusInv() == PstBillMain.INVOICING_FINISH ? "Y" : "N";
                                        statusBatal = b.getBillStatus() == I_DocStatus.DOCUMENT_STATUS_CANCELLED ? "Y" : "N";
                                    }
                                    //----------------------------------------------
                            %>

                            <tr>
                                <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                                <td class="text-center"><%=tglSelesai %></td>
                                <td><%=customer.getPersonName()%></td>
                                <td class="long_text" style="max-width: 100px" title="<%=customer.getHomeAddr() %>"><%=customer.getHomeAddr() %></td>
                                <td><%=customer.getHomeTelp() %> / <%=customer.getHomeMobilePhone() %></td>
                                <td><%=item.getSku() %></td>
                                <td><%=item.getName() %></td>
                                <td class="text-right"><%=String.format("%,.3f", detailOrder.getBerat()) %></td>
                                <td><%=kadarInfo %></td>
                                <td><%=warna %></td>
                                <td class="text-center"><%=String.format("%,.0f", detailOrder.getQty()) %></td>
                                <td class="text-center"><%=statusSelesai %></td>
                                <td class="text-center"><%=statusBatal %></td>
                                <td><%=detailOrder.getNote() %></td>
                            </tr>

                            <%
                                }
                            %>

                            <tr>
                                <td colspan="7"></td>                        
                                <td class="text-right"><b><%=String.format("%,.3f", totalBerat) %></b></td>                        
                                <td colspan="2"></td>
                                <td class="text-center"><b><%=totalQty %></b></td>
                                <td colspan="3"></td>
                            </tr>

                            <%
                                    }
                            %>

                            <%
                                }
                            %>

                            <%if(listPesanan.isEmpty()) {%>
                            <tr>
                                <td colspan="19" class="text-center"><b>Tidak ada data pemesanan</b></td>
                            </tr>
                            <%} else {%>

                            <%if (view == 1) {%>
                            <tr>
                                <td colspan="14" class="text-right"><b><%=String.format("%,.0f", totalDp) %>.00</b></td>
                                <td></td>
                            </tr>
                            <%}%>

                            <%}%>

                        </table>
                    </div>
                </div>
                
                <%if(!listPesanan.isEmpty()) {%>
                <div class="box-footer">
                    <button type="button" id="btnPrint" class="btn btn-sm btn-primary pull-right"><i class="fa fa-print"></i>&nbsp; Cetak</button>
                </div>
                <%}%>
            </div>
            <%}%>
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
            window.open("print_out_report_detail_order.jsp?"+data,"print_out_sales_lost_profit");
        });
        
    </script>

</html>
