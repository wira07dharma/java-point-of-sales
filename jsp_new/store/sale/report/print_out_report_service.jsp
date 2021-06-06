<%-- 
    Document   : print_out_report_service
    Created on : Apr 5, 2018, 4:50:47 PM
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

<%//
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
                <h4><b>Laporan Service</b></h4>
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
                    <th>Tanggal</th>
                    <th>No. Bill</th>
                    <th>Customer</th>
                    <th>Nama Barang</th>
                    <th>Berl.</th>
                    <th>Keterangan</th>
                    <th>Payment</th>
                    <th>Sal. Pers.</th>
					<th>Berat</th>
					<th>Harga @</th>
					<th>Jumlah</th>
                    <th>Ongkos</th>
                    <th>Disc.</th>
                    <th>PPN</th>
                    <th>Total</th>
                    <th>Add. Charge</th>
                    <th>Total Payment</th>
                    <th>Cur.</th>
                    <th>Rate</th>
                    <th>Total (Rp)</th>
                </tr>
                
                <%                            
					double totalBerat = 0.0;
					double totalPrice = 0.0;
					double totalCost = 0.0;
					double totalSusutan = 0.0;
					double totalDisc = 0.0;
					double totalTax = 0.0;
					double totalAddCharge = 0.0;
					double totalAll = 0.0;
					double totalAllPayment = 0.0;
					String curr = "";
					double rate = 0.0;
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
//                        Sales s = new Sales();
//                        if (bm.getSalesCode().length() > 0 && PstSales.checkOID(Long.valueOf(bm.getSalesCode()))) {
//                            s = PstSales.fetchExc(Long.valueOf(bm.getSalesCode()));
//                        }
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
                        String itemName = SessMaterial.setItemNameForLitama(m.getOID());                                
                        //check if berlian
                        String statusBerlian = "N";
                        if (m.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                            statusBerlian = "Y";
                        }
                        //get paymenttype
                        String paymentType = SessSalesOrder.getPaymentType(bm.getOID());
                        //count total
                        double total = bd.getCost() - bd.getDisc() + bd.getTotalTax();
                        double addCharge = SessSalesOrder.getAddCharge(bm.getOID());
                        double totalPayment = SessSalesOrder.getPayment(bm.getOID());
						
						totalBerat = totalBerat + bd.getBerat();
						totalPrice = totalPrice +  bd.getItemPrice();
						totalCost = totalCost + bd.getCost();
						totalSusutan = totalSusutan + bd.getSusutanPrice();
						totalDisc = totalDisc + bd.getDisc();
						totalTax = totalTax + bd.getTotalTax();
						totalAddCharge = totalAddCharge + addCharge;
						totalAll = totalAll + total;
						totalAllPayment = totalAllPayment + totalPayment;

                %>
                        
                <tr>
                    <td><%=Formater.formatDate(bm.getBillDate(), "yyyy/MM/dd") %></td>
                    <td><%=bm.getInvoiceNo() %></td>
                    <td><%=bm.getGuestName()%></td>
                    <td><%=itemName %></td>
                    <td class="text-center"><%=statusBerlian %></td>
                    <td><%=bm.getNotes() %></td>
                    <td><%=paymentType %></td>
                    <td><%=ap.getFullName()%></td>
					<td class="text-right"><%=String.format("%,.3f", bd.getBerat()) %></td>
					<td class="text-right"><%=String.format("%,.0f", bd.getItemPrice()) %>.00</td>
					<td class="text-right"><%=String.format("%,.0f", (bd.getBerat() * bd.getItemPrice())) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", bd.getCost()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", bd.getDisc()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", bd.getTotalTax()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", (bd.getBerat() * bd.getItemPrice()) + total) %>.00</td>
                    <td class="text-right"><%=addCharge %></td>
                    <td class="text-right"><%=String.format("%,.0f", totalPayment) %>.00</td>
                    <td class="text-center"><%=ct.getCode() %></td>
                    <td class="text-right"><%=String.format("%,.0f", bm.getRate()) %>.00</td>
                    <td class="text-right"><%=String.format("%,.0f", (bd.getBerat() * bd.getItemPrice()) + total + addCharge) %>.00</td>
                </tr>
                
                <%}%>
                
                <%if(listService.isEmpty()) {%>
                <tr>
                    <td colspan="19" class="text-center"><b>Tidak ada data servis</b></td>
                </tr>
                <%} else {%>
				<tr>
                    <td colspan="8" class="text-right"><b>Total</b></td>
                    <td class="text-right"><b><%=String.format("%,.3f", totalBerat) %></b></td>
					<td class="text-right"><b><%=String.format("%,.0f", totalPrice) %>.00</b></td>
					<td class="text-right"><b><%=String.format("%,.0f", (totalBerat * totalPrice)) %>.00</b></td>
                    <td class="text-right"><b><%=String.format("%,.0f", totalCost) %>.00</b></td>
                    <td class="text-right"><b><%=String.format("%,.0f", totalDisc) %>.00</b></td>
                    <td class="text-right"><b><%=String.format("%,.0f", totalTax) %>.00</b></td>
                    <td class="text-right"><b><%=String.format("%,.0f", (totalBerat * totalPrice) + totalAll) %>.00</b></td>
                    <td class="text-right"><b><%=totalAddCharge %></b></td>
                    <td class="text-right"><b><%=String.format("%,.0f", totalAllPayment) %>.00</b></td>
                    <td class="text-center"></td>
                    <td class="text-right"></td>
                    <td class="text-right"><b><%=String.format("%,.0f", (totalBerat * totalPrice) + totalAll + totalAddCharge) %>.00</b></td>
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
