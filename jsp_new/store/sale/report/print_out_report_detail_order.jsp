<%-- 
    Document   : print_out_report_detail_order
    Created on : Apr 5, 2018, 4:49:45 PM
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
        String whereStatusBatal = " BM." + PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID] + " > 0"
                + " AND PM." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " = " + idItem
                + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = " + PstBillMain.TYPE_INVOICE
                //+ " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = " + PstBillMain.TRANS_TYPE_CREDIT
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
    int saleType = FRMQueryString.requestInt(request, "FRM_SALE_TYPE");
    int view = FRMQueryString.requestInt(request, "FRM_VIEW");
    
    String tanggal = "";
    if (startDate.equals(endDate)) {
        tanggal = startDate;
    } else {
        tanggal = startDate + " s/d " + endDate;
    }
    
    String location = "Semua lokasi";
    if (idLocation > 0 && PstLocation.checkOID(idLocation)) {
        try{
            Location l = PstLocation.fetchExc(idLocation);
            location = l.getName();
        } catch (Exception e) {
            
        }
    }
    
    String shift = "Semua shift";
    if (idShift > 0 && PstShift.checkOID(idShift)) {
        Shift s = PstShift.fetchExc(idShift);
        shift = s.getName();
    }
    
    String currency = "Semua mata uang";
    if (idCurrency > 0 && PstCurrencyType.checkOID(idCurrency)) {
        CurrencyType currencyType = PstCurrencyType.fetchExc(idCurrency);
        currency = currencyType.getCode();
    }
    
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

    Vector listPesanan = PstBillMain.list(0, 0, where, order);
    
%>

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>            
            .tabel_header {width: 100%; font-size: 12px}
            .tabel_header td {padding-bottom: 10px}

            .tabel_data {
                font-size: 11px;
                border-color: black !important
            }
            
            .tabel_data th {
                text-align: center;
                border-color: black !important;                
                border-bottom-width: thin !important;
                padding: 4px 8px !important;
            }
            .tabel_data td {
                border-color: black !important;
                padding: 4px 8px !important
            }
        </style>
    </head>
    <body>
        <div style="margin: 10px">
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
            </div>
            
            <div>
                <h4><b>Laporan Pemesanan <%= (view == 1) ? "Summary" : "Detail" %></b></h4>
            </div>
            
            <table class="tabel_header">
                <tr>
                    <td style="width: 5%">Tanggal</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=tanggal %></td>
                    
                    <td style="width: 5%">Lokasi</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=location %></td>
                    
                    <td style="width: 5%">Shift</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=shift %></td>
                    
                    <td style="width: 5%">Currency</td>
                    <td style="width: 1%">:</td>
                    <td style="width: 20%"><%=currency %></td>                    
                </tr>                
            </table>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px">
            
            <table class="table table-bordered tabel_data">
                <%if(view==1){%>
                <tr>                            
                    <th>No. Order</th>
                    <th>Tgl Order</th>
                    <th>Tgl Selesai</th>
                    <th>Cust.</th>
                    <th style="width: 15%">Addr.</th>
                    <th>Phone</th>
                    <th>Nilai Pesanan</th>
                    <th>DP</th>
                    <th>Add. Charge</th>
                    <th>DP + Charge</th>
                    <th>Sisa</th>
                    <th>Cur.</th>
                    <th>Rate</th>
                    <th>DP (Rp)</th>
                    <th style="max-width: 15%">Keterangan</th>                            
                </tr>
                <%} else if(view==2) {%>
                <tr>
                    <th>Tgl Order</th>
                    <th>Tgl Selesai</th>
                    <th>Cust.</th>
                    <th style="width: 15%">Addr.</th>
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
                %>

                <tr>
                    <td><%=bm.getInvoiceNo() %></td>
                    <td class="text-center"><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                    <td class="text-center"><%=tglSelesai %></td>
                    <td><%=customer.getPersonName()%></td>
                    <td><%=customer.getHomeAddr() %></td>
                    <td><%=customer.getHomeTelp() %> / <%=customer.getHomeMobilePhone() %></td>
                    <td class="text-right"><%=String.format("%,.0f", nilaiPesanan) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", dp) %>.00</td>
                    <td class="text-right"><%=addCharge %></td>
                    <td class="text-right"><%=String.format("%,.0f", dp + addCharge) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", nilaiPesanan - dp) %>.00</td>
                    <td class="text-center"><%=ct.getCode() %></td>
                    <td class="text-right"><%=String.format("%,.0f", bm.getRate()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", dp) %>.00</td>
                    <td><%=bm.getNotes() %></td>
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
                        String tglSelesai = "" + Formater.formatDate(bm.getDateTermOfPayment(), "yyyy/MM/dd");
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
                    <td><%=customer.getHomeAddr() %></td>
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
                
                <%if (view == 1) {%>
                <tr>
                    <td colspan="14" class="text-right"><b><%=String.format("%,.0f", totalDp) %>.00</b></td>
                    <td></td>
                </tr>
                <%}%>

                <%if(listPesanan.isEmpty()) {%>
                <tr>
                    <td colspan="19" class="text-center"><b>Tidak ada data pemesanan</b></td>
                </tr>
                <%}%>
                
            </table>
                    
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                        
        </div>
    </body>
</html>
