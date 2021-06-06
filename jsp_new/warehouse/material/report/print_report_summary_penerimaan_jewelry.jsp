<%-- 
    Document   : print_report_summary_penerimaan_jewelry.jsp
    Created on : Jan 11, 2018, 3:01:21 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.session.warehouse.SessReportReceive"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.common.entity.location.PstLocation"%>
<%@page import="com.dimata.qdep.entity.I_DocStatus"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcReportReceive"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="java.util.Date"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%//
    String dateFromCatch = FRMQueryString.requestString(request, "" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM] + "");
    String dateToCatch = FRMQueryString.requestString(request, "" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO] + "");
    String locationMultiple[] = request.getParameterValues("" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID] + "");
    String currencyMultiple[] = request.getParameterValues("" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID] + "");
    String tipePenerimaan = FRMQueryString.requestString(request, "FRM_TIPE_PENERIMAAN");

    String where = " WHERE";
    where += " (rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT + ""
			+ " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL 
				+ " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_DISPATCH
				+ " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_DISPATCH_UNIT + 
				")";

    Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
    Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");

    String newDateFrom = Formater.formatDate(dateFrom, "yyyy-MM-dd");
    String newDateTo = Formater.formatDate(dateTo, "yyyy-MM-dd");

    String newLongDateFrom = Formater.formatDate(dateFrom, "dd/MMM/yyyy");
    String newLongDateTo = Formater.formatDate(dateTo, "dd/MMM/yyyy");

    //set query periode
    String periode = "";
    if (dateFrom != null && dateTo != null) {
        int compareDate = dateFrom.compareTo(dateTo);
        if (compareDate == 0) {
            periode = "" + newLongDateFrom;
            where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") = '" + newDateFrom + "'";
        } else {
            periode = "Dari " + newLongDateFrom + " sampai " + newLongDateTo;
            where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") BETWEEN '" + newDateFrom + "' AND '" + newDateTo + "'";
        }
    } else if (dateFrom != null) {
        periode = "Dari " + newLongDateFrom + " sampai akhir";
        where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") >= '" + newDateFrom + "'";
    } else if (dateTo != null) {
        periode = "Dari awal sampai " + newLongDateTo;
        where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") <= '" + newDateTo + "'";
    } else {
        periode = "Semua tanggal";
    }

    //set query location
    String locationInText = "";
    String locationShow = "";
    try {
        for (int i = 0; i < locationMultiple.length; i++) {
            if (!locationMultiple[i].equals("0")) {
                if (locationInText.length() > 0) {
                    locationInText += "," + locationMultiple[i];
                    Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                    locationShow += ", " + l.getName();
                } else {
                    locationInText = locationMultiple[i];
                    Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                    locationShow += "" + l.getName();
                }
            }
        }
        if (locationInText.length() > 0) {
            where += " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " IN (" + locationInText + ")";
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
        locationShow = "Semua lokasi";
    }

    //set query currency
    String currencyInText = "";
    String currencyShow = "";
    try {
        for (int i = 0; i < currencyMultiple.length; i++) {
            if (currencyMultiple[i].equals("0")) {
                currencyShow = "Semua mata uang";
                break;
            }
            if (currencyInText.length() > 0) {
                currencyInText += "," + currencyMultiple[i];
                CurrencyType ct = PstCurrencyType.fetchExc(Long.parseLong(currencyMultiple[i]));
                currencyShow += ", " + ct.getCode();
            } else {
                currencyInText = currencyMultiple[i];
                CurrencyType ct = PstCurrencyType.fetchExc(Long.parseLong(currencyMultiple[i]));
                currencyShow += "" + ct.getCode();
            }
        }
        if (currencyInText.length() > 0) {
            where += " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID] + " IN (" + currencyInText + ")";
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    //set query penerimaan
    String penerimaanShow = "";
    if (tipePenerimaan.equals("" + Material.MATERIAL_TYPE_EMAS)) {
        penerimaanShow = "" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS];
    } else if (tipePenerimaan.equals("" + Material.MATERIAL_TYPE_BERLIAN)) {
        penerimaanShow = "" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN];
    } else {
        penerimaanShow = "Semua tipe penerimaan";
    }

    Vector<Company> company = PstCompany.list(0, 0, "", "");

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <style>            
            .table_warna {border-color: black;}
            .table_warna th {
                border-top-color: black !important;
                border-bottom-color: black !important;
                border-left-color: black !important;
                border-right-color: black !important;
                border-top-width: thin !important;
                border-bottom-width: thin !important;
            }
            .table_warna td {
                border-bottom-color: black !important;
                border-left-color: black !important;
                border-right-color: black !important;
            }
            .header_center th {text-align: center}
            .header_emas {
                background-color: #e2e2e2;
            }
            .header_berlian {
                background-color: #e2e2e2;
            }
        </style>
    </head>
    <body>
        <div style="margin: 10px">
            <br>
            <div>
                <img style="width: 100px" src="../../../images/litama.jpeg">
                <span style="font-size: 24px; font-family: TimesNewRomans">
                    <b><%=company.get(0).getCompanyName().toUpperCase()%></b>
                    <small><i>Gallery</i></small>
                </span>
            </div>
            <div>
                <h4><b>Laporan Pembelian Detail</b></h4>
            </div>
            <div style="font-size: 12px;">
                <span>Laporan Pembelian Harian : <%=periode%></span>
                <span class="pull-right">Tanggal dicetak : <%=Formater.formatDate(new Date(), "dd/MMM/yyyy")%></span>
            </div>

            <hr style="border-width: medium; border-color: black; margin-top: 5px">

            <table class="table table-bordered table_warna" style="width: 100%; font-size: 8px;">    

                <%
                    double grandQty = 0;
                    double grandBerat = 0;
                    double grandTotal = 0;
                    int recordData = 0;

                    if (tipePenerimaan.equals("") || tipePenerimaan.equals("" + Material.MATERIAL_TYPE_EMAS)) {
                        String whereTipe = " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE] + " = '" + Material.MATERIAL_TYPE_EMAS + "'";
                        Vector listEmas = SessReportReceive.getReportReceiveSummaryJewelry(where + whereTipe);
                        double totalQty = 0;
                        double totalBerat = 0;
                        double totalTotal = 0;
                %>

                <thead>
                    <tr><th colspan="14" class="header_emas"><%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]%></th></tr>
                    <tr class="header_center">
                        <th>No.</th>
                        <th>Tanggal</th>
                        <th>Invoice</th>
                        <th>Kode Penerimaan</th>
                        <th>Qty</th>
                        <th>Supplier</th>
                        <th>Berat</th>
                        <th>HEL/Rate</th>
                        <th>Ongkos</th>
                        <th>Rate</th>
                        <th>Currency</th>
                        <th>Nilai Tukar / Rate</th>
                        <th>Total</th>
                        <th>Catatan</th>
                    </tr>
                </thead>
                <tbody>

                    <%
                        for (int i = 0; i < listEmas.size(); i++) {
                            recordData += 1;
                            int no = i + 1;
                            Vector v = (Vector) listEmas.get(i);
                            MatReceive mr = (MatReceive) v.get(0);
                            MatReceiveItem mri = (MatReceiveItem) v.get(1);
                            ContactList supplier = new ContactList();
                            CurrencyType currency = new CurrencyType();
                            try {
                                supplier = PstContactList.fetchExc(mr.getSupplierId());
                                currency = PstCurrencyType.fetchExc(mr.getCurrencyId());
                            } catch (Exception e) {
                            }
                            totalQty += mri.getQty();
                            totalBerat += mr.getBerat();
                            totalTotal += mri.getTotal();
                    %>

                    <tr>
                        <td><%=no%></td>
                        <td><%=Formater.formatDate(mr.getReceiveDate(), "dd MMM yyyy")%></td>
                        <td><%=mr.getInvoiceSupplier()%></td>
                        <td><%=mr.getRecCode()%></td>
                        <td class='text-right'><%=Formater.formatNumber(mri.getQty(), "#")%></td>
                        <td><%=supplier.getCompName()%></td>
                        <td class='text-right'><%=String.format("%,.3f", mr.getBerat())%></td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getHel())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mri.getForwarderCost())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getTransRate())%>.00</td>
                        <td><%=currency.getCode()%></td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getNilaiTukar())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mri.getTotal())%>.00</td>
                        <td><%=mr.getRemark()%></td>
                    </tr>

                    <%
                        }
                    %>

                    <%
                        if (listEmas.isEmpty()) {
                    %>

                    <tr><td colspan="14" style="background-color: " class="text-center"><b>Tidak ada data penerimaan <%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]%></b></td></tr>

                    <%
                    } else {
                    %>

                    <tr>
                        <td colspan="4"><b>Sub Total</b></td>
                        <td class="text-right"><b><%=Formater.formatNumber(totalQty, "#")%></b></td>
                        <td></td>
                        <td class="text-right"><b><%=String.format("%,.3f", totalBerat)%></b></td>
                        <td colspan="5"></td>
                        <td class="text-right"><b><%=String.format("%,.0f", totalTotal)%>.00</b></td>
                        <td></td>
                    </tr>                

                    <%
                        }
                    %>

                </tbody>

                <%
                        grandQty += totalQty;
                        grandBerat += totalBerat;
                        grandTotal += totalTotal;
                    }
                %>

                <%
                    if (tipePenerimaan.equals("") || tipePenerimaan.equals("" + Material.MATERIAL_TYPE_BERLIAN)) {
                        String whereTipe = " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE] + " = '" + Material.MATERIAL_TYPE_BERLIAN + "'";
                        Vector listBerlian = SessReportReceive.getReportReceiveSummaryJewelry(where + whereTipe);
                        double totalQty = 0;
                        double totalBerat = 0;
                        double totalTotal = 0;
                %>

                <thead>
                    <tr><th colspan="14"  class="header_berlian"><%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]%></th></tr>
                    <tr class="header_center">
                        <th>No.</th>
                        <th>Tanggal</th>
                        <th>Invoice</th>
                        <th>Kode Penerimaan</th>
                        <th>Qty</th>
                        <th>Supplier</th>
                        <th>Berat</th>
                        <th>HEL/Rate</th>
                        <th>Ongkos</th>
                        <th>Rate</th>
                        <th>Currency</th>
                        <th>Nilai Tukar / Rate</th>
                        <th>Total</th>
                        <th>Catatan</th>
                    </tr>
                </thead>
                <tbody>

                    <%
                        for (int i = 0; i < listBerlian.size(); i++) {
                            recordData += 1;
                            int no = i + 1;
                            Vector v = (Vector) listBerlian.get(i);
                            MatReceive mr = (MatReceive) v.get(0);
                            MatReceiveItem mri = (MatReceiveItem) v.get(1);
                            ContactList supplier = new ContactList();
                            CurrencyType currency = new CurrencyType();
                            try {
                                supplier = PstContactList.fetchExc(mr.getSupplierId());
                                currency = PstCurrencyType.fetchExc(mr.getCurrencyId());
                            } catch (Exception e) {
                            }
                            totalQty += mri.getQty();
                            totalBerat += mr.getBerat();
                            totalTotal += mri.getTotal();
                    %>

                    <tr>
                        <td><%=no%></td>
                        <td><%=Formater.formatDate(mr.getReceiveDate(), "dd MMM yyyy")%></td>
                        <td><%=mr.getInvoiceSupplier()%></td>
                        <td><%=mr.getRecCode()%></td>
                        <td class='text-right'><%=Formater.formatNumber(mri.getQty(), "#")%></td>
                        <td><%=supplier.getCompName()%></td>
                        <td class='text-right'><%=String.format("%,.3f", mr.getBerat())%></td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getHel())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mri.getForwarderCost())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getTransRate())%>.00</td>
                        <td><%=currency.getCode()%></td>
                        <td class='text-right'><%=String.format("%,.0f", mr.getNilaiTukar())%>.00</td>
                        <td class='text-right'><%=String.format("%,.0f", mri.getTotal())%>.00</td>
                        <td><%=mr.getRemark()%></td>
                    </tr>

                    <%
                        }
                    %>

                    <%
                        if (listBerlian.isEmpty()) {
                    %>

                    <tr><td colspan="14" style="background-color: " class="text-center"><b>Tidak ada data penerimaan <%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]%></b></td></tr>

                    <%
                    } else {
                    %>

                    <tr>
                        <td colspan="4"><b>Sub Total</b></td>
                        <td class="text-right"><b><%=Formater.formatNumber(totalQty, "#")%></b></td>
                        <td></td>
                        <td class="text-right"><b><%=String.format("%,.3f", totalBerat)%></b></td>
                        <td colspan="5"></td>
                        <td class="text-right"><b><%=String.format("%,.0f", totalTotal)%>.00</b></td>
                        <td></td>
                    </tr>                

                    <%
                        }
                    %>

                </tbody>

                <%
                        grandQty += totalQty;
                        grandBerat += totalBerat;
                        grandTotal += totalTotal;
                    }
                %>

                <%
                    if (recordData > 0) {
                %>

                <tfoot>
                    <tr>
                        <td style='background-color: #e2e2e2' colspan='4'><b>Grand Total</b></td>
                        <td style='background-color: #e2e2e2' class='text-right'><b><%=Formater.formatNumber(grandQty, "#")%></b></td>
                        <td style='background-color: #e2e2e2'></td>
                        <td style='background-color: #e2e2e2' class='text-right'><b><%=String.format("%,.3f", grandBerat)%></b></td>
                        <td style='background-color: #e2e2e2' colspan='5'></td>
                        <td style='background-color: #e2e2e2' class='text-right'><b><%=String.format("%,.0f", grandTotal)%>.00</b></td>
                        <td style='background-color: #e2e2e2'></td>
                    </tr>
                </tfoot>

                <%
                    }
                %>

            </table>

                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 0; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                
        </div>
    </body>
</html>
