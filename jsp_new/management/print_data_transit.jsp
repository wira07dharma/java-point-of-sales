<%-- 
    Document   : print_data_transit
    Created on : Oct 1, 2019, 4:39:49 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchBill"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    String tglMulai = FRMQueryString.requestString(request, "TGL_MULAI");
    String tglAkhir = FRMQueryString.requestString(request, "TGL_AKHIR");
    long idLokasi = FRMQueryString.requestLong(request, "LOKASI");
    String multiStatus = FRMQueryString.requestString(request, "STATUS");
    String keyword = FRMQueryString.requestString(request, "KEYWORD");
    
    String locationName = "";
    String multiLokasi = "";
    if (idLokasi == 0) {
        locationName = "Semua lokasi";
        Vector<Location> listLocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_DUTY_FREE, PstLocation.fieldNames[PstLocation.FLD_NAME]);
        for (Location l : listLocation) {
            multiLokasi += multiLokasi.isEmpty() ? "" : ", ";
            multiLokasi += l.getOID();
        }
    } else {
        try {
            multiLokasi = "" + idLokasi;
            locationName = PstLocation.fetchExc(idLokasi).getName();
        } catch (Exception e) {
            
        }
    }
    
    String selectedStatus = "";
    if (multiStatus.isEmpty()) {
        selectedStatus = "Semua status";
    } else {
        String[] arrayStatus = multiStatus.split(",");
        for (int i = 0; i < arrayStatus.length; i++) {
            selectedStatus += selectedStatus.isEmpty() ? "" : ", ";
            selectedStatus += PstBillMain.deliveryStatus[Integer.valueOf(arrayStatus[i].trim())];
        }
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
    Vector<BillMain> listBill = PstBillMain.list(0, 0, PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " IN (" + query + ")", PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " DESC ");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bea Cukai Customs</title>
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
        <style>
            .tabel-data, .tabel-data thead tr th, .tabel-data tbody tr td {
                font-size: 12px;
                padding: 5px;
                border-color: grey;
            }

            .tabel-data thead tr th {
                text-align: center;
                white-space: nowrap;
                border-width: thin;
                background-color: #DDD;
                text-transform: uppercase;
            }

            .table_info tr td {
                font-size: 14px;
                padding: 5px;
            }

            .middle-inline {
                text-align: center;
                white-space: nowrap;
            }
        </style>
    </head>
    <body>
        <div class="print" style="margin: 20px">
            <h3 class="text-center">Data Transit Barang</h3>
            <table class="table_info">
                <tr>
                    <td>Tanggal</td>
                    <td>:</td>
                    <td><%=tglMulai%> s/d <%=tglAkhir%></td>
                </tr>
                <tr>
                    <td>Lokasi</td>
                    <td>:</td>
                    <td><%=locationName%></td>
                </tr>
                <tr>
                    <td>Status</td>
                    <td>:</td>
                    <td><%=selectedStatus%></td>
                </tr>
            </table>
            <br>
            <table class="table table-bordered tabel-data">
                <thead>
                    <tr>
                        <th style="width: 1%">No.</th>
                        <th>Details</th>
                        <th>SKU</th>
                        <th>Items</th>
                        <th>Qty</th>
                        <th>Price</th>
                        <th>Disc</th>
                        <th>Total</th>
                        <th style="width: 1%">Status</th>
                    </tr>
                </thead>
                <tbody>
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
                            
                    <tr style="<%=stripped%>; font-weight: bold;">
                        <td class="text-bold" colspan="2">TOTAL</td>
                        <td class="text-bold text-center"><%=String.format("%,.0f", totalQty)%></td>
                        <td class="text-bold text-right" colspan="3"><%=String.format("%,.0f", totalPrice)%></td>
                    </tr>

                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>
