<%-- 
    Document   : bea_cukai_customs
    Created on : Oct 1, 2019, 9:11:51 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchBill"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    String tglMulai = FRMQueryString.requestString(request, "TGL_MULAI");
    String tglAkhir = FRMQueryString.requestString(request, "TGL_AKHIR");
    long idLokasi = FRMQueryString.requestLong(request, "LOKASI");
    String[] status = FRMQueryString.requestStringValues(request, "STATUS");
    String keyword = FRMQueryString.requestString(request, "KEYWORD");
    long billId = FRMQueryString.requestLong(request, "BILL_MAIN_ID");
    int deliveryStatus = FRMQueryString.requestInt(request, "DELIVERY_STATUS");
    int record = FRMQueryString.requestInt(request, "RECORD");
    
    if (command == Command.NONE) {
        record = 10;
    }
    
    //UPDATE DELIVERY STATUS
    String msg = "";
    if (command == Command.SAVE && billId != 0) {
        try {
            BillMain billMain = PstBillMain.fetchExc(billId);
            billMain.setStatus(deliveryStatus);
            PstBillMain.updateExc(billMain);
            msg = "";
        } catch (Exception e) {
            msg = e.getMessage();
        }
        command = Command.LIST;
    }
    
    //==========================================================================
    //SET QUERY SEARCH
    if (tglMulai.isEmpty()) {
        tglMulai = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
    }
    if (tglAkhir.isEmpty()) {
        tglAkhir = Formater.formatDate(new java.util.Date(), "yyyy-MM-dd");
    }

    String multiLokasi = "";
    String multiStatus = "";
    Vector listLoc = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_DUTY_FREE, PstLocation.fieldNames[PstLocation.FLD_NAME]);
    Vector<BillMain> listBill = new Vector();
    
    if (command == Command.LIST) {
        if (idLokasi == 0) {
            for (int d = 0; d < listLoc.size(); d++) {
                Location loc = (Location) listLoc.get(d);
                multiLokasi += multiLokasi.isEmpty() ? "" : ",";
                multiLokasi += loc.getOID();
            }
        } else {
            multiLokasi = "" + idLokasi;
        }
        
        if (status != null) {
            String a = Arrays.toString(status);
            multiStatus = a.substring(1, a.length() - 1);
        }
        
        String query = ""
                + " SELECT bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                + " FROM " + PstBillMain.TBL_CASH_BILL_MAIN + " AS bm "
                + " INNER JOIN " + PstMatDispatchBill.TBL_MATDISPATCHBILL + " AS db "
                + " ON db." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_CASH_BILL_MAIN_ID]
                + " = bm." + PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]
                + " INNER JOIN " + PstMatDispatch.TBL_DISPATCH + " AS dm "
                + " ON dm." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID]
                + " = db." + PstMatDispatchBill.fieldNames[PstMatDispatchBill.FLD_DISPATCH_MATERIAL_ITEM_ID]
                + " INNER JOIN " + PstContactList.TBL_CONTACT_LIST + " AS cl "
                + " ON cl." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID]
                + " = bm." + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID]
                + " WHERE ("
                + " ("
                + " bm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CASH
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                + ") OR ("
                + " bm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = " + PstBillMain.TRANS_STATUS_CLOSE
                + " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + " = " + PstBillMain.INVOICING_ON_PROSES
                + " )"
                + " )"
                + " AND dm." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + " BETWEEN '" + tglMulai + " 00:00:00' AND '" + tglAkhir + " 23:59:59'"
                + "";
        if (!multiStatus.isEmpty()) {
            query += " AND bm." + PstBillMain.fieldNames[PstBillMain.FLD_STATUS] + " IN (" + multiStatus + ")";
        }
        if (!multiLokasi.isEmpty()) {
            query += " AND dm." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " IN (" + multiLokasi + ")";
        }
        if (!keyword.isEmpty()) {
            query += " AND ("
                    + " bm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + keyword + "%'"
                    + " OR bm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + " LIKE '%" + keyword + "%'"
                    + " OR cl." + PstContactList.fieldNames[PstContactList.FLD_PASSPORT_NO] + " LIKE '%" + keyword + "%'"
                    + ")";
        }
        listBill = PstBillMain.list(0, record, PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " IN (" + query + ")", PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " DESC ");
    }
    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bea Cukai Customs</title>
        <%@ include file = "../styles/plugin_component.jsp" %>
        <style>
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
            
        </style>
        <script>
            function print() {
                window.open("print_data_transit.jsp?TGL_MULAI=<%=tglMulai%>&TGL_AKHIR=<%=tglAkhir%>&LOKASI=<%=idLokasi%>&STATUS=<%=multiStatus%>&KEYWORD=<%=keyword%>","printout");
            }
        </script>
    </head>
    <body>
        <section class="content-header">
            <h1>Update Status Barang</h1>
            <ol class="breadcrumb">
                <li>Bea Cukai Customs</li>
                <li>Transit</li>
                <li>Update Status Barang</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h4 class="box-title">Pencarian</h4>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="formSearch" class="form-horizontal">
                                <input type="hidden" id="command" name="command" value="<%=Command.LIST%>">
                                <input type="hidden" id="billId" name="BILL_MAIN_ID" value="">
                                <input type="hidden" id="deliveryStatus" name="DELIVERY_STATUS" value="">

                                <div class="form-group">
                                    <label class="col-sm-1">Tanggal</label>
                                    <div class="col-sm-4">
                                        <div class="input-group">
                                            <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="TGL_MULAI" value="<%=tglMulai%>">
                                            <span class="input-group-addon">s/d</span>
                                            <input type="text" autocomplete="off" class="form-control input-sm datePicker" name="TGL_AKHIR" value="<%=tglAkhir%>">
                                        </div>
                                    </div>
                                    <label class="col-sm-1">Keyword</label>
                                    <div class="col-sm-4">
                                        <input type="text" placeholder="Invoice number / Passport number" class="form-control input-sm" name="KEYWORD" value="<%=keyword%>">
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-sm-1">Lokasi</label>
                                    <div class="col-sm-4">
                                        <select class="form-control input-sm" name="LOKASI">
                                            <option value="0">- Pilih Lokasi -</option>
                                            <%
                                                for (int d = 0; d < listLoc.size(); d++) {
                                                    Location loc = (Location) listLoc.get(d);
                                            %>
                                            <option <%=(loc.getOID() == idLokasi ? "selected" : "")%> value="<%=loc.getOID() %>"><%=loc.getName() %></option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                    <label class="col-sm-1">Status</label>
                                    <div class="col-sm-6">
                                        <% for (int i = 0; i < PstBillMain.deliveryStatus.length; i++) {%>
                                        <label class="checkbox-inline"><input type="checkbox" <%=(multiStatus.contains("" + i) ? "checked" : "")%> name="STATUS" value="<%=i%>"><%=PstBillMain.deliveryStatus[i] %></label>
                                        <% } %>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="bg-info text-center"><%=msg%></div>
                </div>
                <div class="box-footer">
                    <div class="form-inline">
                        <button type="submit" form="formSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> View Data</button>
                        <select form="formSearch" class="form-control input-sm" name="RECORD">
                            <option <%=(record == 10 ? "selected":"")%> value="10">10</option>
                            <option <%=(record == 20 ? "selected":"")%> value="20">20</option>
                            <option <%=(record == 50 ? "selected":"")%> value="50">50</option>
                        </select>
                    </div>
                </div>
            </div>

            <%if(command == Command.LIST) {%>
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h4 class="box-title">Data Transit Barang</h4>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table class="table table-bordered tabel-data">
                            <tr class="bg-primary">
                                <th style="width: 1%">No.</th>
                                <th>Details</th>
                                <th>SKU</th>
                                <th>Items</th>
                                <th>Qty</th>
                                <th>Price</th>
                                <th>Disc</th>
                                <th>Total</th>
                                <th style="width: 1%">Status</th>
                                <th style="width: 1%">Update Status</th>
                            </tr>

                            <%
                                int rowspan = 0;
                                String stripped = "";
                                int no = 0;
                                for (BillMain bm : listBill) {
                                    no++;
                                    ContactList member = new ContactList();
                                    try {
                                        if (bm.getCustomerId() != 0) {
                                            member = PstContactList.fetchExc(bm.getCustomerId());
                                        }
                                    } catch (Exception e) {
                                        
                                    }
                                    
                                    String passportNumber = "";
                                    if (member.getPassportNo() == null || member.getPassportNo().equals("")) {
                                        passportNumber = "-";
                                    } else {
                                        passportNumber = member.getPassportNo();
                                    }
                                    String flightNumber = "";
                                    if (bm.getFlightNumber() == null || bm.getFlightNumber().equals("")) {
                                        flightNumber = "-";
                                    } else {
                                        flightNumber = bm.getFlightNumber();
                                    }
                                    String flightDate = "";
                                    if (bm.getShippingDate() == null) {
                                        flightDate = "-";
                                    } else {
                                        flightDate = Formater.formatDate(bm.getShippingDate(), "yyyy-MM-dd HH:mm:ss");
                                    }
                                    
                                    //GET BILL DETAIL
                                    Vector<Billdetail> listDetail = PstBillDetail.list(0, 0, PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + bm.getOID(), "");
                                    rowspan = listDetail.isEmpty() ? 2 : listDetail.size() + 1;
                                    double totalQty = 0;
                                    double totalPrice = 0;
                                    stripped = no % 2 > 0 ? ";background-color: #f1f1f1;" : "";
                                    
                                    String button = "";
                                    if (bm.getStatus() == PstBillMain.DELIVERY_STATUS_IN_TRANSIT) {
                                        button += "<button type='button' class='btn btn-xs btn-info btn_status' data-id='" + bm.getOID() + "' data-status='" + PstBillMain.DELIVERY_STATUS_RECEIVED + "'>Receive</button>";
                                    } else if (bm.getStatus() == PstBillMain.DELIVERY_STATUS_RECEIVED) {
                                        button += "<button type='button' class='btn btn-xs btn-success btn_status' data-id='" + bm.getOID() + "' data-status='" + PstBillMain.DELIVERY_STATUS_TAKEN + "'>Taken</button>";
                                        button += "&nbsp;";
                                        button += "<button type='button' class='btn btn-xs btn-danger btn_status' data-id='" + bm.getOID() + "' data-status='" + PstBillMain.DELIVERY_STATUS_NOT_TAKEN + "'>Not Taken</button>";
                                    }
                            %>
                            
                            <tr style="<%=stripped%>">
                                <td rowspan="<%=rowspan%>"><%=no%>.</td>
                                <td rowspan="<%=rowspan%>">
                                    <div class="text-bold"><%=bm.getInvoiceNumber() %></div>
                                    <div>PASS : <%=passportNumber %></div>
                                    <div>FN : <%=flightNumber %></div>
                                    <div>FD : <%=flightDate %></div>
                                </td>
                                <%
                                    if (!listDetail.isEmpty()) {
                                        Billdetail bd = listDetail.get(0);
                                        Material m = new Material();
                                        try {
                                            m = PstMaterial.fetchExc(bd.getMaterialId());
                                        } catch (Exception e) {
                                        }
                                        double diskon = bd.getDisc();
                                        double total = bd.getQty() * (bd.getItemPrice() - bd.getDisc());
                                        totalQty += bd.getQty();
                                        totalPrice += total;
                                %>
                                <td><%=m.getSku() %></td>
                                <td><%=m.getName() %></td>
                                <td class="text-center"><%=String.format("%,.0f", bd.getQty()) %></td>
                                <td class="text-right"><%=String.format("%,.0f", bd.getItemPrice()) %></td>
                                <td class="text-right"><%=String.format("%,.0f", diskon) %></td>
                                <td class="text-right"><%=String.format("%,.0f", total) %></td>
                                <%
                                    }
                                %>
                                <td class="middle-inline" style="vertical-align: middle" rowspan="<%=rowspan%>"><%=PstBillMain.deliveryStatus[bm.getStatus()] %></td>
                                <td class="middle-inline" style="vertical-align: middle" rowspan="<%=rowspan%>">
                                    <%=button%>
                                </td>
                            </tr>
                            
                            <%
                                    for (int i = 1; i < listDetail.size(); i++) {
                                        Billdetail bd = listDetail.get(i);
                                        Material m = new Material();
                                        try {
                                            m = PstMaterial.fetchExc(bd.getMaterialId());
                                        } catch (Exception e) {
                                        }
                                        double diskon = bd.getDisc();
                                        double total = bd.getQty() * (bd.getItemPrice() - bd.getDisc());
                                        totalQty += bd.getQty();
                                        totalPrice += total;
                            %>
                            
                            <tr style="<%=stripped%>">
                                <td><%=m.getSku() %></td>
                                <td><%=m.getName() %></td>
                                <td class="text-center"><%=String.format("%,.0f", bd.getQty()) %></td>
                                <td class="text-right"><%=String.format("%,.0f", bd.getItemPrice()) %></td>
                                <td class="text-right"><%=String.format("%,.0f", diskon) %></td>
                                <td class="text-right"><%=String.format("%,.0f", total) %></td>
                            </tr>
                            
                            <%
                                    }
                            %>
                            
                            <tr style="<%=stripped%>">
                                <td class="text-bold" colspan="2">TOTAL</td>
                                <td class="text-bold text-center"><%=String.format("%,.0f", totalQty)%></td>
                                <td class="text-bold text-right" colspan="3"><%=String.format("%,.0f", totalPrice)%></td>
                            </tr>

                            <%
                                }
                            %>
                            
                            <%if (listBill.isEmpty()) {%>
                            <tr>
                                <td colspan="10" class="text-center label-default">Tidak ada data</td>
                            </tr>
                            <%}%>
                        </table>
                    </div>
                    <%if(!listBill.isEmpty()){%>
                    <p></p>
                    <button type="button" onclick="print()" class="btn btn-sm btn-primary pull-right">Cetak Laporan</button>
                    <%}%>
                </div>
            </div>
            <%}%>
        </section>
    </body>
    <script>
        $('.datePicker').datetimepicker({
            format: "yyyy-mm-dd",
            todayBtn: true,
            autoclose: true,
            minView: 2
        });
        
        $('.btn_status').click(function() {
            if (confirm("Yakin akan mengubah status ?")) {
                $('#command').val("<%=Command.SAVE%>");
                $('#billId').val($(this).data('id'));
                $('#deliveryStatus').val($(this).data('status'));
                $('#formSearch').submit();
            }
        });
    </script>
</html>
