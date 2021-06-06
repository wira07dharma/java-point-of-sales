<%-- 
    Document   : print_out_report_invoice_detail
    Created on : Apr 6, 2018, 11:28:11 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.payment.*"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesOrder"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.posbo.report.sale.SaleReportDocument,
         com.dimata.util.Command,
         com.dimata.posbo.form.search.FrmSrcSaleReport,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.pos.form.billing.CtrlBillMain,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.pos.entity.billing.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.pos.session.billing.SessBilling"%>

<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
    public static final String textListTitleHeader[][] = {
        {"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER ","Tidak ada data transaksi ..","Lokasi","Shift","Laporan","Cetak Laporan Penjualan Invoice Detail","Tipe Penjualan","Tanggal","s/d","Mata Uang"},
        {"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","Location","Shift","Report","Print Invoice Detail Sale Report","Type","Date","to","Currency Type"}
    };
    
    public static final String textListGlobal[][] = {
	{"Laporan Penjualan", "Laporan Penjualan Invoice Detail","Semua Location",
            "Penjualan Cash","Penjualan Credit","Semua Penjualan","Penjualan Biasa", "Penjualan Konsinyasi"},
	{"Sale Report", "Invoice Detail Sale Report","All Location",
            "Cash Sales"," Credit Sales","All Sales", "Normal Sale", "Consignment Sale"}
    };
%>

<%//
    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    int iCommand = FRMQueryString.requestCommand(request);
    int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
    int itemType = FRMQueryString.requestInt(request, "item_type");
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 0;

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    SrcSaleReport srcSaleReport = new SrcSaleReport();
    FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
    frmSrcSaleReport.requestEntity(srcSaleReport);
    int vectSize = 0;

    /**
     * handle current search data session
     */
    try {
        srcSaleReport = (SrcSaleReport) session.getValue(SaleReportDocument.SALE_REPORT_DOC);
    } catch (Exception e) {
    }

    srcSaleReport.setTransType(iSaleReportType);
    String startDate = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd 00:00:00");
    String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd 23:59:59");

    /**
     * tanggal transaksi
     */
    String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE] + " BETWEEN '" + startDate + "' AND '" + endDate + "'";

    if (iSaleReportType == -1) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE
				+ " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "!=" + PstBillMain.INVOICING_FINISH
                + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH
                + " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")"
                + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                + " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")";
    } else if (iSaleReportType == PstCashPayment.CASH) {
         where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
		where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "!=" + PstBillMain.INVOICING_FINISH;
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE;
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH;
    } else {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE;
		where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING] + "!=" + PstBillMain.INVOICING_FINISH;
        where = where + " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE
                + " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")";
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT;
    }

    /**
     * shift
     */
    if (srcSaleReport.getShiftId() != 0) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID] + "=" + srcSaleReport.getShiftId();
    }
    Shift shift = new Shift();
    try {
        shift = PstShift.fetchExc(srcSaleReport.getShiftId());
    } catch (Exception e) {
        shift.setName("Semua Shift");
    }
    /**
     * lokasi
     */
    if (srcSaleReport.getLocationId() != 0) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + srcSaleReport.getLocationId();
    }
    Location location = new Location();
    try {
        location = PstLocation.fetchExc(srcSaleReport.getLocationId());
    } catch (Exception e) {
        location.setName("Semua Lokasi");
    }
    /**
     * currency
     */
    if (srcSaleReport.getCurrencyOid() != 0) {
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + "=" + srcSaleReport.getCurrencyOid();
    }
    
    if (typeOfBusinessDetail==2){
        where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_IS_SERVICE] + "!= 1";
    }
    
    String strCurrencyType = "Semua Mata Uang";
    if (srcSaleReport.getCurrencyOid() != 0) {
        //Get currency code
        String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + "=" + srcSaleReport.getCurrencyOid();
        Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
        CurrencyType currencyType = (CurrencyType) listCurrencyType.get(0);
        strCurrencyType = currencyType.getCode();
    }
    
    vectSize = PstBillMain.getCount(where);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        try {
            start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            if (srcSaleReport == null) {
                srcSaleReport = new SrcSaleReport();
            }
        } catch (Exception e) {
            srcSaleReport = new SrcSaleReport();
        }
    } else {
        session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
    }

    int paymentDinamis = 0;
    paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
    
    String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
    Vector records = PstBillMain.list(start, recordToGet, where, order);

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

            .tabel_data {border-color: black !important; font-size: 12px}            
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
            
            .sum_total td {font-weight: bold}
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
                <h4><b>Laporan Penjualan Invoice Detail</b></h4>
            </div>
            
            <table class="tabel_header">
                <tr> 
                    <td style="width: 5%"><%=textListTitleHeader[SESS_LANGUAGE][3]%></td>
                    <td>:</td>
                    <td style="width: 25%"><%=location.getName()%></td>
                    
                    <td style="width: 10%"><%=textListTitleHeader[SESS_LANGUAGE][7]%></td>
                    <td>:</td>
                    <td><%=(iSaleReportType == PstBillMain.TRANS_TYPE_CASH) ? textListGlobal[SESS_LANGUAGE][3] : iSaleReportType == PstBillMain.TRANS_TYPE_CREDIT ? textListGlobal[SESS_LANGUAGE][4] : textListGlobal[SESS_LANGUAGE][5]%>
                        -  <%=(itemType == 0 ? textListGlobal[SESS_LANGUAGE][6] : textListGlobal[SESS_LANGUAGE][7])%>
                    </td>
                    
                    <td style="width: 10%"><%=textListTitleHeader[SESS_LANGUAGE][8]%></td>
                    <td>:</td>
                    <td><%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> <%=textListTitleHeader[SESS_LANGUAGE][9]%> <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr> 
                    <td><%=textListTitleHeader[SESS_LANGUAGE][4]%></td>
                    <td>:</td>
                    <td><%=shift.getName()%></td>
                
                    <td><%=textListTitleHeader[SESS_LANGUAGE][10]%></td>
                    <td>:</td>
                    <td><%=strCurrencyType%></b></td>
                
                    <td>Tanggal dicetak</td>
                    <td>:</td>
                    <td><%=Formater.formatDate(new Date(), "dd MMMM yyyy")%></td>
                </tr>
            </table>
            
            <hr style="border-width: medium; border-color: black; margin-top: 5px;width: 110%">
            
            <table class="table table-bordered tabel_data">
                <tr>
                    <th>No.</th>
                    <th>Tgl</th>
                    <th>No. Faktur</th>
                    <th>Kode Barang</th>
                    <th style="white-space: nowrap">Nama Barang</th>
                    <th>PRS</th>
                    <th>Qty</th>
                    <th>Berat</th>
                    <th>Kadar</th>
                    <th>Harga Jual</th>
                    <th>Ongkos</th>
					<th>Susutan</th>
                    <th>Diskon</th>
                    <th>PPN</th>
                    <th>DP</th>
                    <th>ADD</th>
                    <th>Payment</th>
                    <th>Type</th>
                    <th>No. Order</th>
                    <th>Keterangan</th>
                </tr>
                
                <%
                    double totalQty = 0;
                    double totalBerat = 0;
                    double totalHargaJual = 0;
                    double totalOngkos = 0;
					double totalSusutan = 0.0;
                    double totalDiskon = 0;
                    double totalPPN = 0;
                    double totalDP = 0;
                    double totalAddCharge = 0;
                    double totalPayment = 0;
                    int no=0;
                    for (int i = 0; i < records.size(); i++) {
                        BillMain billMain = (BillMain) records.get(i);
                        AppUser ap = new AppUser();
                        try {
                            ap = PstAppUser.fetch(billMain.getAppUserSalesId());
                          } catch (Exception e) {
                          }
                        
                        String where2 = " BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID()
                            + " AND PM."+PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+" = " + itemType
                            + " AND BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+" > 0";
                            Vector list = SessBilling.listInvoiceDetail(0, 0, where2, "");
                        
                        if (list.size() > 0) {
                            Vector temp = (Vector) list.get(0);
                            Billdetail billDetail = (Billdetail) temp.get(0);
                            Unit unit = (Unit) temp.get(1);
                            Material material = (Material) temp.get(2);

                            Billdetail bd = new Billdetail();
                            Material m = new Material();
                            Sales s = new Sales();
                            Kadar k = new Kadar();
                            String kadarInfo = "";
							boolean dpLebihBesar = false;
							if (billMain.getParentSalesOrderId() > 0) {
								double amountBillMain = SessSalesOrder.getTotalBillOrder(billMain.getParentSalesOrderId());
								double amountDP = SessSalesOrder.getFirstDownPayment(billMain.getParentSalesOrderId());
								dpLebihBesar = amountDP > (amountBillMain / 2);
							}
                            try {
                                if (billDetail.getOID() != 0 && PstBillDetail.checkOID(billDetail.getOID())) {
                                    bd = PstBillDetail.fetchExc(billDetail.getOID());
                                }
                                if (bd.getMaterialId() != 0 && PstMaterial.checkOID(bd.getMaterialId())) {
                                    m = PstMaterial.fetchExc(bd.getMaterialId());
                                }
								
								if (material.getBarCode().equals("")){
									continue;
								}
								no = no+1;
//                                if (billMain.getSalesCode().length() > 0 && PstSales.checkOID(Long.valueOf(billMain.getSalesCode()))) {
//                                    s = PstSales.fetchExc(Long.valueOf(billMain.getSalesCode()));
//                                }
                                if (m.getPosKadar() > 0 && PstKadar.checkOID(m.getPosKadar())) {
                                    k = PstKadar.fetchExc(m.getPosKadar());
                                    kadarInfo = ""+k.getKadar();
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }

                            String itemName = SessMaterial.setItemNameForLitama(m.getOID());
                            //DP
                            double dp = SessSalesOrder.getDownPayment(billMain.getOID());
                            //ADD CHARGE
                            double addCharge = SessSalesOrder.getAddCharge(billMain.getOID());
                            //PAYMENT
                            //double payment = SessSalesOrder.getPayment(billMain.getOID());
							double payment = 0;
							Vector<CashPayments1> listPayment = PstCashPayment1.list(0, 0, PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID] + " = " + billMain.getOID(), "");
							for (CashPayments1 cp : listPayment) {
								payment += cp.getAmount();
							}
							
                            String paymentType = SessSalesOrder.getPaymentType(billMain.getOID());                            
                            //ORDER NUMBER
                            String orderNumber = "";
                            Vector<BillMain> listOrder = PstBillMain.list(0, 0, ""+PstBillMain.fieldNames[PstBillMain.FLD_PARENT_SALES_ORDER_ID]+"="+billMain.getOID(), "");
                            if (!listOrder.isEmpty()) {
                                orderNumber = listOrder.get(0).getInvoiceNo();
                            }

                            double hargaJual = 0;
							if (dpLebihBesar){
								hargaJual = m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS ? bd.getItemPrice() * bd.getBerat() : bd.getItemPrice();
							} else {
								hargaJual = m.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS ? bd.getLatestItemPrice()* bd.getBerat() : bd.getItemPrice();
							}

                            totalQty += bd.getQty();
                            totalBerat += bd.getBerat()+bd.getAdditionalWeight();
                            totalHargaJual += hargaJual;
                            totalOngkos += bd.getCost();
							totalSusutan += bd.getSusutanPrice();
                            totalDiskon += (bd.getDisc() + billMain.getDiscount());
                            totalPPN += billMain.getTaxValue();
                            totalDP += dp;
                            totalAddCharge += addCharge;
                            totalPayment += payment;
                %>
                
                <tr>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-center" style="vertical-align: middle"><%=(no)%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" style="vertical-align: middle"><%=Formater.formatDate(billMain.getBillDate(),"yyyy/MM/dd")%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" style="vertical-align: middle"><%=billMain.getInvoiceNumber()%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" style="vertical-align: middle"><%=material.getBarCode()%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" style="white-space: nowrap; vertical-align: middle"><%=itemName%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" style="vertical-align: middle"><%=ap.getFullName()%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-center" style="vertical-align: middle"><%=String.format("%,.0f", bd.getQty())%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.3f", bd.getBerat()+bd.getAdditionalWeight())%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=kadarInfo %></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", hargaJual)%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", bd.getCost())%></td>
					<td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", bd.getSusutanPrice())%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", (bd.getDisc() + billMain.getDiscount()))%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", billMain.getTaxValue())%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", dp)%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>" class="text-right" style="vertical-align: middle"><%=String.format("%,.2f", addCharge)%></td>
					<% if (listPayment.size()>0) { 
						CashPayments1 cp = (CashPayments1) listPayment.get(0);
					%>
						<td class="text-right"><%=String.format("%,.2f", cp.getAmount())%></td>
						<% 
							try {
								PaymentSystem ps = PstPaymentSystem.fetchExc(cp.getPaymentType());
								%><td><%=ps.getPaymentSystem()%></td><%
							} catch (Exception exc){}
						%>
						
					<% } %>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>"><%=orderNumber%></td>
                    <td rowspan="<%=listPayment.size() > 1 ? listPayment.size() : 1%>"><%=billMain.getNotes()%></td>
                </tr>
                
                <%
					if (listPayment.size()>1){
						%><tr> <%
						for (int p = 1; p < listPayment.size(); p++){
							CashPayments1 cp = (CashPayments1) listPayment.get(p);
							%>
								<td class="text-right"><%=String.format("%,.2f", cp.getAmount())%></td>
								<% 
									try {
										PaymentSystem ps = PstPaymentSystem.fetchExc(cp.getPaymentType());
										%><td><%=ps.getPaymentSystem()%></td><%
									} catch (Exception exc){}
								%>
					<%
						}
					%></tr><%		
					}
					
                        }
                    }
                %>
                
                <tr class="sum_total">
                    <td colspan="6" style="text-align: right">Total :</td>
                    <td class="text-center"><%=String.format("%,.0f", totalQty)%></td>
                    <td class="text-right"><%=String.format("%,.3f", totalBerat)%></td>
					<td></td>
                    <td class="text-right"><%=String.format("%,.2f", totalHargaJual)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalOngkos)%></td>
					<td class="text-right"><%=String.format("%,.2f", totalSusutan)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalDiskon)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalPPN)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalDP)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalAddCharge)%></td>
                    <td class="text-right"><%=String.format("%,.2f", totalPayment)%></td>
                    <td colspan="4"></td>
                </tr>
            </table>
                      
                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                             
        </div>
    </body>
</html>
