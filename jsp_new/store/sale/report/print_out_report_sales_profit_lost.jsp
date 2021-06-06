<%-- 
    Document   : print_out_report_sales_profit_lost
    Created on : Apr 5, 2018, 4:25:30 PM
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

<%
    int iCommand = FRMQueryString.requestCommand(request);
    String startDate = FRMQueryString.requestString(request, "FRM_START_DATE");
    String endDate = FRMQueryString.requestString(request, "FRM_END_DATE");
    long idLocation = FRMQueryString.requestLong(request, "FRM_LOCATION");
    long idShift = FRMQueryString.requestLong(request, "FRM_SHIFT");
    long idCurrency = FRMQueryString.requestLong(request, "FRM_CURRENCY_TYPE");
    int saleType = FRMQueryString.requestInt(request, "FRM_SALE_TYPE");
    
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
                border-color: black !important;
                font-size: 11px
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
                <h4><b>Laporan Penjualan Laba / Rugi</b></h4>
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
                <tr>
                    <th>Tgl</th>
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

//                        Sales s = new Sales();
//                        if (bm.getSalesCode().length() > 0 && PstSales.checkOID(Long.valueOf(bm.getSalesCode()))) {
//                            s = PstSales.fetchExc(Long.valueOf(bm.getSalesCode()));
//                        }

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

                        double hargaBeli = m.getAveragePrice();
                        double hargaJual = bd.getTotalPrice()  + bd.getTotalTax();
                        double selisihBeli = hargaJual - hargaBeli;
                        double selisihPasar = hargaJual - hargaPasar;

                        totalQty += bd.getQty();
                        totalBerat += bd.getBerat();
                        totalHargaBeli +=  Math.round(m.getDefaultCost());
                        totalHargaPasar += Math.round(hargaPasar);
                        totalHargaJual += Math.round(bd.getTotalPrice()  + bd.getTotalTax());
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
                
                <%
                    }
                %>
                
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
                   
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                                       
        </div>
    </body>
</html>
