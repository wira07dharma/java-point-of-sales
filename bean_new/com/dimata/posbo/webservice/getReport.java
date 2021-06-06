/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import static com.dimata.common.ajax.AjaxCustomReportTable.VIEW_FULL_REPORT;
import static com.dimata.common.ajax.AjaxCustomReportTable.VIEW_SUMMARY_SALES_REPORT;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.session.report.SessCustomeReport;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.posbo.entity.masterdata.PstShift;
import com.dimata.posbo.entity.masterdata.Shift;
import com.dimata.posbo.entity.masterdata.TableRoom;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Formater;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class getReport extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();

    //STRING
    private String dataFor = "";
    private String isCustomeReport = "0";
    private String oidDelete = "";
    private String approot = "";
    private String address = "";
    private String htmlReturn = "";
    private String searchTerm = "";
    private String dateStart = "";
    private String dateEnd = "";
    private String locationId = "";
    private String locationCode = "";
    private String recordtoget = "";
    private String paper_bill = "";
    private String system_bill = "";
    private String unikkode = "";

    private int amount = 0;
    private String colName = "";
    private String dir = "";
    private int start = 0;
    private int colOrder = 0;
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int language = 0;

    public static final String textListOrderHeader[][] = {
        {"No", "Lokasi", "Nama Item", "Jml", "Unit Stok", "Jml Sekarang", "Kode", "Jumlah Request", "Jumlah Kirim"},
        {"No", "Location", "Item Name", "Qty", "Stock Unit", "Curent Qty", "Code", "Request Qty", "Send Request"}
    };

    public static final String textListButton[][] = {
        {"Simpan", "Hapus", "Ubah", "Selanjutnya", "Kembali", "Pencarian"},
        {"Save", "Delete", "Update", "Next", "Back", "Search"}
    };

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.iCommand = FRMQueryString.requestCommand(request);

        this.isCustomeReport = FRMQueryString.requestString(request, "type_of_report");
        this.dateStart = FRMQueryString.requestString(request, "date_start");
        this.dateEnd = FRMQueryString.requestString(request, "date_end");
        this.locationCode = FRMQueryString.requestString(request, "location");
        this.recordtoget = FRMQueryString.requestString(request, "recordtoget");
        this.paper_bill = FRMQueryString.requestString(request, "paper_bill");
        this.system_bill = FRMQueryString.requestString(request, "system_bill");
        this.system_bill = FRMQueryString.requestString(request, "system_bill");
        this.unikkode = FRMQueryString.requestString(request, "unikkode");

        if (!this.locationCode.equals("")) {
            Vector clocation = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_CODE] + " like '%" + this.locationCode + "%'", "");
            if (clocation.size() > 0) {
                Location loc = (Location) clocation.get(0);
                this.locationId = "" + loc.getOID();
            }
        }

        if (!this.recordtoget.equals("")) {
            try {
                amount = Integer.parseInt(this.recordtoget);
            } catch (Exception ex) {
                amount = 0;
            }
        }
        if (this.unikkode.equals("webserviceprochain")) {
            commandList(request);
        }else{
            try {
                this.jSONObject.put("status", "NOT_OK");
                this.jSONObject.put("recordtoget", this.recordtoget);
                this.jSONObject.put("dateStart", this.dateStart);
                this.jSONObject.put("dateEnd", this.dateEnd);
                this.jSONObject.put("message", "kode unik salah");
                this.jSONObject.put("result", jSONArray);
            } catch (Exception ex) {
            }
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONObject);
    }

    public void commandList(HttpServletRequest request) {
        getListReport(request);
    }

    public void getListReport(HttpServletRequest request) {
        String cashCashierID = "";
        String whereBillMainCash = "";
        Vector listBillMaincash = new Vector();
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat printDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        int typeReport = Integer.parseInt(this.isCustomeReport);
        switch (typeReport) {
            case VIEW_SUMMARY_SALES_REPORT:
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart, this.dateEnd, 0, " cash_master.LOCATION_ID='" + this.locationId + "'");
                whereBillMainCash += ""
                        + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] + "=" + this.locationId + " AND "
                        + " ((cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 AND "
                        + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=0 AND "
                        + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 0 )";

                whereBillMainCash += ""
                        + " OR (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=0 AND "
                        + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=1 AND "
                        + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "= 1 )) AND (" + cashCashierID + ") ";

                if (!this.system_bill.equals("")) {
                    whereBillMainCash = whereBillMainCash + " AND " + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NO] + "='" + this.system_bill + "'";
                }

                if (!this.paper_bill.equals("")) {
                    whereBillMainCash = whereBillMainCash + " AND " + " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + "='" + this.paper_bill + "'";
                }

                String orderBy = " cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " ASC ";
                listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash, orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                int nomor = 0;
                try {
                    jSONArray = new JSONArray();
                    if (listBillMaincash.size() > 0) {
                        for (int j = 0; j < listBillMaincash.size(); j++) {
                            Vector tempData = (Vector) listBillMaincash.get(j);
                            BillMain billMain = (BillMain) tempData.get(0);
                            TableRoom tableRoom = (TableRoom) tempData.get(2);
                            JSONObject jSONObjectx = new JSONObject();
                            nomor = nomor + 1;
                            jSONObjectx.put("no", "" + nomor);
                            jSONObjectx.put("paper_bill", "" + billMain.getCoverNumber());
                            jSONObjectx.put("system_bill", "" + billMain.getInvoiceNumber());
                            if (billMain.getTableId() != 0) {
                                jSONObjectx.put("type_transaction", "Dine In");
                            } else if (billMain.getTableId() == 0 && !billMain.getShippingAddress().equals("")) {
                                jSONObjectx.put("type_transaction", "Delivery");
                            } else if (billMain.getTableId() == 0 && billMain.getShippingAddress().equals("")) {
                                jSONObjectx.put("type_transaction", "Take a Way");
                            } else {
                                jSONObjectx.put("type_transaction", "-");
                            }
                            if (tableRoom.getTableNumber() == null) {
                                jSONObjectx.put("table", "-");
                            } else {
                                jSONObjectx.put("table", "" + tableRoom.getTableNumber());
                            }
                            jSONObjectx.put("guest", "" + billMain.getCustName());
                            String whereLog = PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "='" + billMain.getOID() + "' "
                                    + " AND " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + "='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                            Date count = PstLogSysHistory.getDateLog(whereLog);
                            if (count != null) {
                                jSONObjectx.put("tgl_print", "" + Formater.formatDate(count, "yyyy-MM-dd"));
                                jSONObjectx.put("waktu_print", "" + Formater.formatTimeLocale(count, "kk:mm:ss"));
                            } else {
                                jSONObjectx.put("tgl_print", "-");
                                jSONObjectx.put("waktu_print", "-");
                            }
                            jSONObjectx.put("tgl_settle", "" + printDateFormat.format(billMain.getBillDate()));
                            jSONObjectx.put("waktu_settle", "" + printFormat.format(billMain.getBillDate()));
                            jSONObjectx.put("pax", "" + billMain.getPaxNumber());

                            double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                            jSONObjectx.put("food", "" + decimalFormat.format(totalFood));
                            double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                            jSONObjectx.put("beverage", "" + decimalFormat.format(totalBeverage));
                            double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                            jSONObjectx.put("other", "" + decimalFormat.format(totalOther));
                            jSONObjectx.put("net_sales", "" + decimalFormat.format(totalFood + totalBeverage + totalOther));
                            jSONObjectx.put("tax", "" + decimalFormat.format(billMain.getTaxValue()));
                            jSONObjectx.put("servie", "" + decimalFormat.format(billMain.getServiceValue()));
                            jSONObjectx.put("discount", "" + decimalFormat.format(billMain.getDiscount()));
                            jSONObjectx.put("total_sales", "" + decimalFormat.format(billMain.getTaxValue() + totalFood + totalBeverage + totalOther - billMain.getDiscount() + billMain.getServiceValue()));
                            double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(), 0, " ps.PAYMENT_TYPE=1 ");
                            jSONObjectx.put("cash", "" + decimalFormat.format(totalPaymentCash));
                            if (billMain.getDocType() == 0 && billMain.getTransctionType() == 1 && billMain.getTransactionStatus() == 1) {
                                jSONObjectx.put("credit", "" + decimalFormat.format(billMain.getTaxValue() + totalFood + totalBeverage + totalOther - billMain.getDiscount() + billMain.getServiceValue()));
                            } else {
                                jSONObjectx.put("credit", "");
                            }
                            double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(), 0, " ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                            jSONObjectx.put("c_card", "" + decimalFormat.format(totalPaymentCreditCard));
                            double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(), 0, " ps.PAYMENT_TYPE=2");
                            jSONObjectx.put("d_card", "" + decimalFormat.format(totalPaymentDebitCard));
                            double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(), 0, " ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                            jSONObjectx.put("bg", "" + decimalFormat.format(totalPaymentBG));
                            double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(), 0, " ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                            jSONObjectx.put("cek", "" + decimalFormat.format(totalPaymentCek));
                            jSONObjectx.put("foc", "0");
                            jSONObjectx.put("remark", "" + billMain.getNotes());
                            jSONArray.put(jSONObjectx);
                        }
                    }
                    try {
                        this.jSONObject.put("status", "OK");
                        this.jSONObject.put("recordtoget", this.recordtoget);
                        this.jSONObject.put("dateStart", this.dateStart);
                        this.jSONObject.put("dateEnd", this.dateEnd);
                        this.jSONObject.put("message", "data berhasil di dapat");
                        this.jSONObject.put("result", jSONArray);
                    } catch (Exception ex) {
                    }
                } catch (Exception e) {
                }
                break;
                
            default:
                try {
                    this.jSONObject.put("status", "NOT_OK");
                    this.jSONObject.put("recordtoget", this.recordtoget);
                    this.jSONObject.put("dateStart", this.dateStart);
                    this.jSONObject.put("dateEnd", this.dateEnd);
                    this.jSONObject.put("message", "tidak ada jenis report");
                    this.jSONObject.put("result", jSONArray);
                } catch (Exception ex) {
                }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
