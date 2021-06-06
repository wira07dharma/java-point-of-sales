/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstPaymentSystem;
import com.dimata.common.session.report.SessCustomeReport;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.marketing.entity.marketingnewsinfo.PstMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.CtrlMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.FrmMarketingNewsInfo;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.CashCreditCard;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.PstMemberReg;
import com.dimata.posbo.entity.masterdata.TableRoom;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.entity.warehouse.MatCosting;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.posbo.report.sale.SaleReportItem;
import com.dimata.posbo.session.report.SessSalesSummaryReport;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Witar
 */
public class AjaxCustomReportTable
        extends HttpServlet {

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    private JSONArray arrayColom = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String dateStart = "";
    private String dateEnd = "";
    private String message = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int nilaiCombo = 0;
    private long locationId=0;
    private int buttonFilterRange = 0;
    private String textinput="";
    
    public static final int VIEW_FULL_REPORT=-11;
    public static final int VIEW_SUMMARY_SALES_REPORT=1;
    public static final int VIEW_SALES_F_AND_B_REPORT=2;
    public static final int VIEW_SALES_REPORT_BY_CASH=3;
    public static final int VIEW_SALES_REPORT_BY_CREDIT_CARD=4;
    public static final int VIEW_SALES_REPORT_BY_CL=5;
    public static final int VIEW_SALES_REPORT_BY_DILIVERY=6;
    public static final int VIEW_SALES_REPORT_BY_TAKE_AWAY=7;
    public static final int VIEW_SALES_REPORT_ACCUMULATIVE_BY_MONTHLY=8;
    public static final int VIEW_SALES_REPORT_ACCUMULATIVE_BY_YEARLY=9;
    public static final int VIEW_SALES_REPORT_BY_TAX=10;
    public static final int VIEW_SALES_REPORT_BY_SERVICE=11;
    public static final int VIEW_SALES_REPORT_BY_STAFF=12;
    public static final int VIEW_SALES_REPORT_BY_ITEM=13;
    public static final int VIEW_SALES_REPORT_BY_VOID=14;
    public static final int VIEW_SALES_REPORT_BY_ERROR=15;
    public static final int VIEW_SALES_REPORT_BY_CANCEL=16;
    public static final int VIEW_SALES_REPORT_BY_COMPLIMENT=17;
    public static final int VIEW_SALES_REPORT_BY_DISCOUNT=18;
    
    public static final int BUTTON_FILTER_TODAY=1;
    public static final int BUTTON_FILTER_YESTERDAY=2;
    public static final int BUTTON_FILTER_THIS_WEEK=3;
    public static final int BUTTON_FILTER_THIS_MONTH=4;
    public static final int BUTTON_FILTER_LAST_MONTH=5;
    public static final int BUTTON_FILTER_MINUS=6;
    public static final int BUTTON_FILTER_PLUS=7;
    
    public static final String textListHeader[][] = {
        {"Berita dan Informasi", "Judul", "Tgl Mulai", "Tgl Berakhir", "Keterangan", "Plih Gambar", "Pilih Lokasi", "Lokasi"},
        {"News and Info", "Title", "Valid Start", "Valid End", "Description", "Select Picture", "Select Location", "Location"}
    };

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidReturn = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";
        this.dateStart = FRMQueryString.requestString(request, "FRM_FIELD_DATE_START");
        this.dateEnd = FRMQueryString.requestString(request, "FRM_FIELD_DATE_END");

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        this.nilaiCombo = FRMQueryString.requestInt(request, "FRM_FIELD_COMBO");
        this.locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        this.buttonFilterRange = FRMQueryString.requestInt(request, "button_filter_range");
        this.textinput = FRMQueryString.requestString(request, "textinput");
        
        DateFormat formaterx = new SimpleDateFormat("yyyy-MM-dd");  
        Date startDate = new Date();
        Date endDate = new Date();
        
        if(!textinput.equals("")){
            try{
                textinput = textinput.trim();
            }catch(Exception ex){}
        }
        
        switch (this.buttonFilterRange) {
            case BUTTON_FILTER_TODAY:
                startDate = new Date();
                try {
                    this.dateStart = Formater.formatDate(startDate,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(startDate,"yyyy-MM-dd");          
                } catch (Exception ex) {
                    
                }
                break;
            case BUTTON_FILTER_YESTERDAY:
                startDate = new Date();
                startDate.setDate(startDate.getDate()-1);
                try {
                    this.dateStart = Formater.formatDate(startDate,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(startDate,"yyyy-MM-dd");          
                } catch (Exception ex) {
                }
                break;
            case BUTTON_FILTER_THIS_WEEK:
                startDate = new Date();
                endDate = new Date();
                Date startDateN = getWeekStart(startDate, 7);
                Date endDateN = getWeekEnd(endDate, 7);
                try {
                    this.dateStart = Formater.formatDate(startDateN,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(endDateN,"yyyy-MM-dd");          
                } catch (Exception ex) {
                }
                break;
            case BUTTON_FILTER_THIS_MONTH:
                Calendar beginCalendar = Calendar.getInstance();
                Date startMTD = beginCalendar.getTime();
                startMTD.setDate(1);
                
                int dayOfMonth = beginCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                Date endMTD = beginCalendar.getTime();
                endMTD.setDate(dayOfMonth);
                try {
                    this.dateStart = Formater.formatDate(startMTD,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(endMTD,"yyyy-MM-dd");          
                } catch (Exception ex) {
                }
                break;
                
            case BUTTON_FILTER_LAST_MONTH:
                
                Calendar beginCalendarx = Calendar.getInstance();
                Date startMTDx = beginCalendarx.getTime();
                startMTDx.setMonth(startMTDx.getMonth()-1);
                startMTDx.setDate(1);
                
                beginCalendarx.setTime(startMTDx);
                int dayOfMonthx = beginCalendarx.getActualMaximum(Calendar.DAY_OF_MONTH);
                Date endMTDx = beginCalendarx.getTime();
                endMTDx.setDate(dayOfMonthx);
                try {
                    this.dateStart = Formater.formatDate(startMTDx,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(endMTDx,"yyyy-MM-dd");          
                } catch (Exception ex) {
                }
                
                break;
            
            case BUTTON_FILTER_PLUS:
                startDate = new Date();
                try {
                    Calendar beginCalendary = Calendar.getInstance();
                    beginCalendary.setTime(formaterx.parse(this.dateStart));
                    Date startMTDz = beginCalendary.getTime();
                    startMTDz.setDate(startMTDz.getDate()+1);
                    
                    this.dateStart = Formater.formatDate(startMTDz,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(startMTDz,"yyyy-MM-dd");          
                } catch (Exception ex) {
                    
                }
                break;
                
            case BUTTON_FILTER_MINUS:
                startDate = new Date();
                try {
                    Calendar beginCalendaro = Calendar.getInstance();
                    beginCalendaro.setTime(formaterx.parse(this.dateStart));
                    Date startMTDo = beginCalendaro.getTime();
                    startMTDo.setDate(startMTDo.getDate()-1);
                    
                    this.dateStart = Formater.formatDate(startMTDo,"yyyy-MM-dd"); 
                    this.dateEnd = Formater.formatDate(startMTDo,"yyyy-MM-dd");          
                } catch (Exception ex) {
                    
                }
                break;    
                
            default:
        }
        
        //OBJECT
        this.jSONObject = new JSONObject();

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.RESET:
                //commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }
        try {

            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
            this.jSONObject.put("FRM_FIELD_MESSAGE", this.message);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("list")) {
            String[] cols = {
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showForm(request);
        } else if (this.dataFor.equals("upload")) {
            this.htmlReturn = showFormUpload(request);
        } else if (this.dataFor.equals("getColumn")) {
            this.htmlReturn = getDataTableColumn(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveMarketingNews(request);
        } else if (this.dataFor.equals("upload")) {
            this.htmlReturn = saveUploadImage(request);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("delete")) {
            this.htmlReturn = deleteMarketingNews(request);
        }
    }

    public String saveMarketingNews(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingNewsInfo ctrlMarketingNewsInfo = new CtrlMarketingNewsInfo(request);
        ctrlMarketingNewsInfo.action(iCommand, oid, oidDelete);
        returnData = ctrlMarketingNewsInfo.getMessage();
        return returnData;
    }

    public String deleteMarketingNews(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingNewsInfo ctrlMarketingNewsInfo = new CtrlMarketingNewsInfo(request);
        ctrlMarketingNewsInfo.action(iCommand, oid, oidDelete);
        returnData = ctrlMarketingNewsInfo.getMessage();
        return returnData;
    }

    public String showFormUpload(HttpServletRequest request) {
        String returnData = "";
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        returnData += ""
                + "<input type='hidden' name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_PROMO_ID] + "' value='" + oid + "'>"
                + "<input type='file' id='filename' name='filename' style='width:0px;'>"
                + "<input type='hidden' name='basename' id='basename' value=''>"
                + "<div class='row'>"
                + "<div class='col-md-12'>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][5] + "</label>"
                + "<div class='input-group'>"
                + "<input class='form-control clickSearchImage fakeinput' type='text'>"
                + "<div class='input-group-btn'>"
                + " <button type='button' class='btn btn-default clickSearchImage'>&nbsp;<i class='fa fa-file-photo-o'></i>&nbsp;</button>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                + "<div class='input-group'>"
                + "<div id='imgPrev'>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</div>";

        return returnData;
    }

    public String getDataTableColumn(HttpServletRequest request) {
        String html = "";
        Vector listData = new Vector(1, 1);
        int isCustomeReport = this.nilaiCombo;//SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
        html = getColomReportFull(isCustomeReport);
        return html;
    }
    
     public String getColomReportFull(int typeReport) {
        String html="";
         String cashCashierID ="";
        switch (typeReport) {
            case VIEW_FULL_REPORT:
                    cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";
                    Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");
                    html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>guest</th>"
                        + "<th>Time</th>"
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Credit</th>";
                        for (int i = 0; i<listPaymentSystem.size();i++){
                            PaymentSystem paymentsystem = (PaymentSystem)listPaymentSystem.get(i);
                            html += "<th>"+paymentsystem.getPaymentSystem()+"</th>";
                        }
                        html += "<th>Compliment</th>";
                        html += "<th>Remark</th>";
                    break;
                
                case VIEW_SUMMARY_SALES_REPORT:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Company</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"
                        + "<th>Remark</th>";
                    break;
                    
                case VIEW_SALES_F_AND_B_REPORT:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Net Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"
                        + "<th>Remark</th>";
                    break;  
                case VIEW_SALES_REPORT_BY_CASH:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Cash_Receive</th>"
                        + "<th>Cash_Return</th>"
                        + "<th>Cash_Deposit_1</th>"
                        + "<th>Cash_Deposit_1</th>"
                        + "<th>Cash_Deposit_2</th>"     
                        + "<th>Cash_Deposit_2</th>"
                        + "<th>Cash_Deposit_3</th>"
                        + "<th>Cash Deposit_3</th>"     
                        + "<th>Remark</th>";
                    break; 
                case VIEW_SALES_REPORT_BY_CREDIT_CARD:
                     html=""
                        + "<th width='100%'>No</th>"
                        + "<th width='100%'>Paper_Bill</th>"
                        + "<th width='100%'>System_Bill</th>"
                        + "<th width='100%'>Type_Transaction</th>"
                        + "<th width='100%'>Table</th>"
                        + "<th width='100%'>Guest</th>"
                        + "<th width='100%'>Tgl_Print</th>"
                        + "<th width='100%'>Waktu_Print</th>" 
                        + "<th width='100%'>Tgl_Settle</th>"
                        + "<th width='100%'>Waktu_Settle</th>"      
                        + "<th width='100%'>Pax</th>"
                        + "<th width='100%'>Food</th>"
                        + "<th width='100%'>Beverage</th>"
                        + "<th width='100%'>Other</th>"
                        + "<th width='100%'>Net_Sales</th>"
                        + "<th width='100%'>Tax</th>"
                        + "<th width='100%'>Servie</th>"
                        + "<th width='100%'>Discount</th>"
                        + "<th width='100%'>Total_Sales</th>"
                        + "<th width='100%'>Credit_Card</th>"     
                        + "<th width='100%'>Type_Of_Credit_Card</th>"
                        + "<th width='100%'>Card_Number</th>"
                        + "<th width='100%'>Name_On_Card </th>"
                        + "<th width='100%'>Bank_Name</th>"
                        + "<th width='100%'>Expired_Card</th>"   
                        + "<th width='100%'>Remark</th>";
                    break;    
                case VIEW_SALES_REPORT_BY_CL:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Credit</th>"     
                        + "<th>Company Name</th>"
                        + "<th>Remark</th>";
                    break;
                
                case VIEW_SALES_REPORT_BY_DILIVERY:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"
                        + "<th>Delivery_Boy</th>"     
                        + "<th>Delivery_Address</th>"     
                        + "<th>Dilivery_Receive</th>"     
                        + "<th>Remark</th>";
                    break;  
                case VIEW_SALES_REPORT_BY_TAKE_AWAY:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"  
                        + "<th>Remark</th>";
                    break; 
                case VIEW_SALES_REPORT_ACCUMULATIVE_BY_MONTHLY:
                     html=""
                        + "<th>No</th>"
                        + "<th>Date</th>"     
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"  
                        + "<th>Remark</th>";
                    break;     
                case VIEW_SALES_REPORT_ACCUMULATIVE_BY_YEARLY:
                     html=""
                        + "<th>No</th>"
                        + "<th>Month</th>"     
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"  
                        + "<th>Remark</th>";
                    break;
                
                case VIEW_SALES_REPORT_BY_TAX:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Discount</th>"     
                        + "<th>Remark</th>";
                    break;
                
                case VIEW_SALES_REPORT_BY_SERVICE:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Remark</th>";
                    break;
                    
                case VIEW_SALES_REPORT_BY_STAFF:
                     html=""
                        + "<th>No</th>"     
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"
                        + "<th>Waiter</th>" 
                        + "<th>Cashier</th>"      
                        + "<th>Remark</th>";
                    break;   
                    
                case VIEW_SALES_REPORT_BY_ITEM:
                     html=""
                        + "<th>No</th>"
                        + "<th>SKU</th>"     
                        + "<th>Barcode</th>"
                        + "<th>Nama</th>"
                        + "<th>Jumlah</th>"
                        + "<th>Total_Jual</th>"
                        + "<th>Total_Beli</th>";
                    break;
                    
                case VIEW_SALES_REPORT_BY_VOID:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Item_Name</th>"     
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"     
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Void_By</th>"
                        + "<th>Authorize_By</th>"     
                        + "<th>Remark</th>";
                    break;   
                
                case VIEW_SALES_REPORT_BY_ERROR:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Item_Name</th>"     
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"     
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Error_By</th>"
                        + "<th>Authorize_By</th>"     
                        + "<th>Remark</th>";
                    break;    
                    
                case VIEW_SALES_REPORT_BY_COMPLIMENT:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total_Sales</th>"
                        + "<th>Remark</th>";
                    break;
                    
                case VIEW_SALES_REPORT_BY_DISCOUNT:
                     html=""
                        + "<th>No</th>"
                        + "<th>Paper_Bill</th>"
                        + "<th>System_Bill</th>"
                        + "<th>Type_Transaction</th>"
                        + "<th>Table</th>"
                        + "<th>Guest</th>"
                        + "<th>Tgl_Print</th>"
                        + "<th>Waktu_Print</th>" 
                        + "<th>Tgl_Settle</th>"
                        + "<th>Waktu_Settle</th>"      
                        + "<th>Pax</th>"
                        + "<th>Food</th>"
                        + "<th>Beverage</th>"
                        + "<th>Other</th>"
                        + "<th>Net_Sales</th>"
                        + "<th>Tax</th>"
                        + "<th>Servie</th>"
                        + "<th>Discount</th>"
                        + "<th>Total Sales</th>"
                        + "<th>Cash</th>"     
                        + "<th>Credit</th>"
                        + "<th>C.Card</th>"
                        + "<th>D.Card</th>"
                        + "<th>BG</th>"
                        + "<th>CEK</th>"     
                        + "<th>FOC</th>"
                        + "<th>Remark</th>";
                    break;    
            default:
                throw new AssertionError();
        }
        
        return html;
    }
    
    
    public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "sSearch");
        int amount = 10;
        int start = 0;
        int col = 0;
        String dir = "asc";
        String sStart = request.getParameter("iDisplayStart");
        String sAmount = request.getParameter("iDisplayLength");
        String sCol = request.getParameter("iSortCol_0");
        String sdir = request.getParameter("sSortDir_0");

        if (sStart != null) {
            start = Integer.parseInt(sStart);
            if (start < 0) {
                start = 0;
            }
        }
        if (sAmount != null) {
            amount = Integer.parseInt(sAmount);
            if (amount < 10) {
                amount = 0;
            }
        }
        if (sCol != null) {
            col = Integer.parseInt(sCol);
            if (col < 0) {
                col = 0;
            }
        }
        if (sdir != null) {
            if (!sdir.equals("asc")) {
                dir = "desc";
            }
        }

        String whereClause = "";
        int isCustomeReport =this.nilaiCombo;// SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
        int total = -1;
        String colName = "";
        switch (isCustomeReport) {
            case VIEW_FULL_REPORT:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + "AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ")";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;
            case VIEW_SUMMARY_SALES_REPORT:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";

                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;   
            
            case VIEW_SALES_F_AND_B_REPORT:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";

                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                        //    + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                     if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break; 
                
             case VIEW_SALES_REPORT_BY_CASH:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + ") AND ("+cashCashierID+") AND ps.PAYMENT_TYPE=1 ";
                    //    + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;   
                
              case VIEW_SALES_REPORT_BY_CREDIT_CARD:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + ") AND ("+cashCashierID+") AND ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    //+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break; 
                
               case VIEW_SALES_REPORT_BY_CL:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"=1 )";

                    whereBillMainCash += ""
                        + ") AND ("+cashCashierID+")";
                    //    + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;  
            
            case VIEW_SALES_REPORT_BY_DILIVERY:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";

                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                        + " AND ("+cashCashierID+") "
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=0"
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"!=''";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;     
                
            case VIEW_SALES_REPORT_BY_TAKE_AWAY:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";

                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                        + " AND ("+cashCashierID+") "
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=0"
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"=''";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;
            
            case VIEW_SALES_REPORT_ACCUMULATIVE_BY_MONTHLY:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    //String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart, this.dateEnd,1,"cash_master.LOCATION_ID='"+this.locationId+"'");
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " (("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";
                    whereBillMainCash += ""
                        + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                        + " AND ("+cashCashierID+") GROUP BY CASH_CASHIER_ID ";
                    Vector listBillMain = PstBillMain.listSummaryTranscationGroupByDate(0, 0, whereBillMainCash);
                    if(listBillMain.size()>0){
                        total = listBillMain.size();
                    }
                }
                break;   
                
            case VIEW_SALES_REPORT_ACCUMULATIVE_BY_YEARLY:
                colName = cols[col];
                if (dataFor.equals("list")) {

                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();

                    try {
                        beginCalendar.setTime(formater.parse(this.dateStart));
                        finishCalendar.setTime(formater.parse( this.dateEnd));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int urut=0;
                    while (beginCalendar.before(finishCalendar)) {
                        urut=urut+1;
                        beginCalendar.add(Calendar.MONTH, 1);
                    }
                    total = urut;
                }
                break;
            
            case VIEW_SALES_REPORT_BY_TAX:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                        //    + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break; 
                
            case VIEW_SALES_REPORT_BY_SERVICE:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";

                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                         //   + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break; 
            
            case VIEW_SALES_REPORT_BY_STAFF:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";
                    
                        //    + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " LIKE '%" + textinput + "%'"    
                            + ") ";
                    }
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;  
            
            case VIEW_SALES_REPORT_BY_ITEM:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    SrcSaleReport srcSaleReport = new SrcSaleReport();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar beginCalendar = Calendar.getInstance();
                    Calendar finishCalendar = Calendar.getInstance();

                    try {
                        beginCalendar.setTime(formater.parse(this.dateStart));
                        finishCalendar.setTime(formater.parse( this.dateEnd));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    srcSaleReport.setDateFrom(beginCalendar.getTime());
                    srcSaleReport.setDateTo(finishCalendar.getTime());
                    srcSaleReport.setTransType(6);
                    srcSaleReport.setGroupBy(SrcSaleReport.GROUP_BY_ITEM);
                    //Vector records = SaleReportDocument.getList(srcSaleReport,0,100,SaleReportDocument.LOG_MODE_CONSOLE);
                    //total = records.size();
                }
                break;
                
            case VIEW_SALES_REPORT_BY_VOID:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ("+cashCashierID+") ";
                    total = PstBillMain.getCountReportVoid(0, 0, whereBillMainCash);
                }
                break;
            
            case VIEW_SALES_REPORT_BY_ERROR:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ("+cashCashierID+") ";
                    total = PstBillMain.getCountReportError(0, 0, whereBillMainCash);
                }
                break;
                
            case VIEW_SALES_REPORT_BY_COMPLIMENT:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String whereCosting = " "
                        + " "+PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]+"="+locationId+""
                        + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") >= '"+this.dateStart+"'"
                        + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") <= '"+this.dateEnd+"'";
                    total = PstMatCosting.getCount(whereCosting);
                }
                break;       
                
            case VIEW_SALES_REPORT_BY_DISCOUNT:
                colName = cols[col];
                if (dataFor.equals("list")) {
                    String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
                  
                    //BILL NOT PAID
                    String whereBillMainCash = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                    whereBillMainCash += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") "
                        + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]+"!=0 ";
                    
                    if(!textinput.equals("")){
                        whereBillMainCash += ""
                            + " AND (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                            + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                            + ") ";
                    }
                    
                    whereBillMainCash += ""+ " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                    
                       // + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";

                    total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
                }
                break;   
                
            default:
                throw new AssertionError();
        }
            

        this.amount = amount;
        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor) {

        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        MarketingNewsInfo marketingNewsInfo = new MarketingNewsInfo();
        String whereClause = "";
        String order = "";
        boolean privUpdate = FRMQueryString.requestBoolean(request, "privUpdate");
        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");
        SimpleDateFormat printFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat printDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int isCustomeReport = this.nilaiCombo;//SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
        String whereBillMainCash = "";
        
        
        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("list")) {
                if(!textinput.equals("")){
                    if(isCustomeReport==VIEW_SALES_REPORT_BY_STAFF){
                   
                    whereBillMainCash += ""
                                + "  (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                                + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                                + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_SALES_CODE] + " LIKE '%" + textinput + "%'"    
                                + ") AND ";
                    }else{
                        if(isCustomeReport==VIEW_FULL_REPORT || isCustomeReport==VIEW_SUMMARY_SALES_REPORT || isCustomeReport==VIEW_SALES_F_AND_B_REPORT
                                || isCustomeReport==VIEW_SALES_REPORT_BY_CASH || isCustomeReport==VIEW_SALES_REPORT_BY_CREDIT_CARD ||
                                isCustomeReport==VIEW_SALES_REPORT_BY_CL || isCustomeReport==VIEW_SALES_REPORT_BY_DILIVERY || isCustomeReport==VIEW_SALES_REPORT_BY_TAKE_AWAY ){
                            
                                whereBillMainCash += ""
                                    + " (cbm." + PstBillMain.fieldNames[PstBillMain.FLD_COVER_NUMBER] + " LIKE '%" + textinput + "%' "
                                    + " OR cbm." + PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " LIKE '%" + textinput + "%'"
                                    + ") AND ";
                                
                        }
                    }
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }
        
        //proses list data
        
        String pattern = "###,###.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String cashCashierID ="";
        String cashCashierID2 ="";
        String whereCashPayment ="";
        switch (isCustomeReport) {
            case VIEW_FULL_REPORT:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";
                Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");

                //BILL NOT PAID
                 whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";


                String whereKredit="" + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 ) AND ("+cashCashierID+") ";

                String orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                Vector listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                    double totalSumPax=0;
                    double totalSumFood=0;
                    double totalSumBeverage=0;
                    double totalSumOther=0;
                    double totalNetSale=0;
                    double totalSumDiscount=0;
                    double totalSumService=0;
                    double totalSumTax=0;
                    double totalSumSub=0;
                    double totalSumKredit=0;
                    
                    double subTotalCash=0;
                    double subTotalCCredit=0;
                    double subTotalDCredit=0;
                    double subTotalBG=0;
                    double subTotalCek=0;
                    double subTotalFoc=0;
                    double subTotalCashReceive=0;
                    double subTotalCashReturn=0;
                    
                    String whereBill="";
                    String whereBillReturn="";
                    String whereBillKembali="";
                    String whereBillReturnKembalian="";
                    int urutretunrn=0;
                    int urut=0;
                    int nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        JSONArray ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood-totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage-totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther-totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax-billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService-billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount-billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub-(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        //KREDIT
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                        }else{
                            ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        }

                        //PAYMENT SYSTEM
                        for (int a = 0; a<listPaymentSystem.size();a++){
                            PaymentSystem paymentSystems = (PaymentSystem)listPaymentSystem.get(a);
                            double totalPayment = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),paymentSystems.getOID());
                            ja.put(""+decimalFormat.format(totalPayment));//result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalPayment)+"</td>";
                        }
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        //urutretunrn=urutretunrn+1;

                        array.put(ja);
                    }

                    SessSalesSummaryReport sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    double pax = 0;
                    double service=0;
                    double tax=0;
                    double disc=0;

                    JSONArray ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getPax())+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getFood())+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getBeverage())+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getOther())+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getNetSales())+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getTax())+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getService())+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getDiscount())+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getTotalSales())+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //kredit
                    ja.put("<b>"+decimalFormat.format(sessSalesSummaryReport.getKreditSales())+"</b>");//result += "<td style='text-align: right; color:red'>0</td>";
                    //PAYMENT SYSTEM
                    for (int a = 0; a<listPaymentSystem.size();a++){
                        PaymentSystem paymentSystems = (PaymentSystem)listPaymentSystem.get(a);
                        double totalPayment = SessCustomeReport.getTotalPaymentByCashierId(cashCashierID,paymentSystems.getOID());
                        ja.put("<b>"+decimalFormat.format(totalPayment)+"</b>");
                    }
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    array.put(ja);  
                break;
            
            case VIEW_SUMMARY_SALES_REPORT:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");

                //BILL NOT PAID
                 whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        if (billMain.getCustomerId() > 0) {
                            try {
                                ja.put(""+PstMemberReg.fetchExc(billMain.getCustomerId()).getCompName());//7
                            } catch (Exception e) {
                                ja.put("");//7
                            }
                        } else {
                            ja.put("-");//7
                        }
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        //CL
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    array.put(ja);  
                break;
                
            case VIEW_SALES_F_AND_B_REPORT:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                 whereBillMainCash +=  ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOtherNotCount = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        double totalOther = 0;//SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        //ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        if(totalPaymentCash>0){
                            totalPaymentCash=totalPaymentCash-totalOtherNotCount;
                        }
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            double kreditval= billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue();
                            totalSumKredit=totalSumKredit+(kreditval-totalOtherNotCount);
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        if(totalPaymentCreditCard>0){
                            totalPaymentCreditCard=totalPaymentCreditCard-totalOtherNotCount;
                        }
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        if(totalPaymentDebitCard>0){
                            totalPaymentDebitCard=totalPaymentDebitCard-totalOtherNotCount;
                        }
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        if(totalPaymentBG>0){
                            totalPaymentBG=totalPaymentBG-totalOtherNotCount;
                        }
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        if(totalPaymentCek>0){
                            totalPaymentCek=totalPaymentCek-totalOtherNotCount;
                        }
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    //ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    array.put(ja);  
                break; 
                
            case VIEW_SALES_REPORT_BY_CASH:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
               
                //BILL NOT PAID
                 whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " ) AND ("+cashCashierID+") AND ps.PAYMENT_TYPE=1 ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     subTotalCashReceive=0;
                     subTotalCashReturn=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        AppUser ap = new AppUser();
                        
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        double totalPaymentCashReceive = SessCustomeReport.getTotalPaymentReceive(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCashReceive=subTotalCashReceive+totalPaymentCashReceive;
                        ja.put(""+decimalFormat.format(totalPaymentCashReceive));
                        
                        double totalPaymentCashReturn = SessCustomeReport.getTotalPaymentReturn(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCashReturn=subTotalCashReturn+totalPaymentCashReturn;
                        ja.put(""+decimalFormat.format(totalPaymentCashReturn));
                        
                        ja.put("");
                        ja.put("");
                        ja.put("" );    
                        ja.put("");
                        ja.put("");
                        ja.put("" );
                        
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCashReceive)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCashReturn)+"</b>");
                    ja.put("");
                    ja.put("");
                    ja.put("" );    
                    ja.put("");
                    ja.put("");
                    ja.put("" );
                    ja.put("");
                    array.put(ja);  
                break;
            
             case VIEW_SALES_REPORT_BY_CREDIT_CARD:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash +=  ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " ) AND ("+cashCashierID+") AND ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     subTotalCashReceive=0;
                     subTotalCashReturn=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put("<th width='100%'>"+nomor+"</th>");//1
                        ja.put("<th width='100%'>"+billMain.getCoverNumber()+"</th>");//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put("<th width='100%'>"+billMain.getInvoiceNumber()+"</th>");//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("<th width='100%'>Dine In"+"</th>");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("<th width='100%'>Delivery"+"</th>");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("<th width='100%'>Take a Way"+"</th>");//4
                        }else{
                            ja.put("<th width='100%'>-"+"</th>");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("<th width='100%'>-"+"</th>");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put("<th width='100%'>"+tableRoom.getTableNumber()+"</th>");//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put("<th width='100%'>"+Formater.formatDate(count,"yyyy-MM-dd")+""+"</th>");
                            ja.put("<th width='100%'>"+Formater.formatTimeLocale(count,"kk:mm:ss")+""+"</th>");
                        }else{
                            ja.put("<th width='100%'>-"+"</th>");
                            ja.put("<th width='100%'>-"+"</th>");
                        }
                        ja.put("<th width='100%'>"+printDateFormat.format(billMain.getBillDate())+"</th>");
                        ja.put("<th width='100%'>"+printFormat.format(billMain.getBillDate())+"</th>");//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        
                        ja.put("<th width='100%'>"+billMain.getPaxNumber()+"</th>");//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put("<th width='100%'>"+decimalFormat.format(totalFood)+"</th>");//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put("<th width='100%'>"+decimalFormat.format(totalBeverage)+"</th>");//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put("<th width='100%'>"+decimalFormat.format(totalOther)+"</th>");//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put("<th width='100%'>"+decimalFormat.format(totalFood+totalBeverage+totalOther)+"</th>");//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put("<th width='100%'>"+decimalFormat.format(billMain.getTaxValue())+"</th>");//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put("<th width='100%'>"+decimalFormat.format(billMain.getServiceValue())+"</th>");//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put("<th width='100%'>"+decimalFormat.format(billMain.getDiscount())+"</th>");//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put("<th width='100%'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</th>");//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put("<th width='100%'>"+decimalFormat.format(totalPaymentCreditCard)+"</th>");
                        Vector vDetailOfCredit = SessCustomeReport.getDetailCreditCard(billMain.getOID(),0," AND ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        String typeOfCredit="";
                        String cardNumber="";
                        String nameOnCard="";
                        String bankName="";
                        Date expiredDate=new Date();
                        
                        if(vDetailOfCredit.size()>0){
                            CashCreditCard cashCreditCard = (CashCreditCard) vDetailOfCredit.get(0);
                            typeOfCredit = cashCreditCard.getTypeOfCard();
                            cardNumber = cashCreditCard.getCcNumber();
                            nameOnCard = cashCreditCard.getCcName();
                            bankName = cashCreditCard.getDebitBankName();
                            expiredDate = cashCreditCard.getExpiredDate();
                        }
                        
                        ja.put("<th width='100%'>"+typeOfCredit+"</th>");
                        ja.put("<th width='100%'>"+cardNumber+"</th>");
                        ja.put("<th width='100%'>"+nameOnCard+"</th>");
                        ja.put("<th width='100%'>"+bankName+"</th>");
                        ja.put("<th width='100%'>"+Formater.formatDate(expiredDate,"dd/MM/yyyy")+""+"</th>");
                        
                        ja.put("<th width='100%'>"+billMain.getNotes()+"</th>");//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("<th width='100%'>"+""+"</th>");//+nomor);//1
                    ja.put("<th width='100%'>"+""+"</th>");//+billMain.getCoverNumber());//
                    ja.put("<th width='100%'>"+""+"</th>");//+billMain.getInvoiceNumber());//
                    ja.put("<th width='100%'>"+""+"</th>");//4
                    ja.put("<th width='100%'>"+""+"</th>");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("<th width='100%'>"+""+"</th>");//+billMain.getCustName());//6
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+"<b>TOTAL</b>"+"</th>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumPax)+"</b>"+"</th>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumFood)+"</b>"+"</th>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumBeverage)+"</b>"+"</th>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumOther)+"</b>"+"</th>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalNetSale)+"</b>"+"</th>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumTax)+"</b>"+"</th>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumService)+"</b>"+"</th>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumDiscount)+"</b>"+"</th>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(totalSumSub)+"</b>"+"</th>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<th width='100%'>"+"<b>"+decimalFormat.format(subTotalCCredit)+"</b>"+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    ja.put("<th width='100%'>"+""+"</th>");
                    array.put(ja);  
                break;    
                
            case VIEW_SALES_REPORT_BY_CL:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");

                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"=1 )";

                whereBillMainCash += ""
                    + " ) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     subTotalCashReceive=0;
                     subTotalCashReturn=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        subTotalCCredit=subTotalCCredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        String compName="";
                        try{
                            ContactList contact = PstContactList.fetchExc(billMain.getCustomerId());
                            compName = contact.getCompName();
                        }catch(Exception ex){
                        }
                        ja.put(""+compName);
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("");
                    ja.put("");
                    array.put(ja);  
                break;   
                
             case VIEW_SALES_REPORT_BY_DILIVERY:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                    + " AND ("+cashCashierID+") "
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=0"
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"!=''";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        //CL
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        //compliment
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""); //pengirim
                        ja.put(""+billMain.getShippingAddress());//alamat penerima
                        ja.put(""); //yang diterima
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put(""); //pengirim
                    ja.put("");//alamat penerima
                    ja.put(""); //yang diterima
                    ja.put("");
                    array.put(ja);  
                break;    
            
            case VIEW_SALES_REPORT_BY_TAKE_AWAY:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                    + " AND ("+cashCashierID+") "
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TABLE_ID]+"=0"
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_SHIPPING_ADDRESS]+"=''";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        //CL
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        //compliment
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    array.put(ja);  
                break;    
                
            case VIEW_SALES_REPORT_ACCUMULATIVE_BY_MONTHLY:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart, this.dateEnd,1,"cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " (("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                    + " AND ("+cashCashierID+") GROUP BY CASH_CASHIER_ID ";
                 
                 listBillMaincash =  PstBillMain.listSummaryTranscationGroupByDate(0, 0, whereBillMainCash);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        BillMain billMain = (BillMain)listBillMaincash.get(j);
                        nomor=nomor+1;
                        cashCashierID2 = PstBillMain.listCashCashierPerDay(Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd"),Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd"),0," cash_master.LOCATION_ID='"+this.locationId+"'");
                        String whereBillMainCash2 = ""
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                        + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                        whereBillMainCash2 += ""
                        + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                        + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                        + " AND ("+cashCashierID2+") ";
                
                        ja.put(""+nomor);//1
                        ja.put(""+Formater.formatDate(billMain.getBillDate(),"yyyy-MM-dd")+"");
                        
                        ja.put(""+billMain.getPaxNumber());//3result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(0, 0, whereBillMainCash2);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//4result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(0, 1,whereBillMainCash2);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//5result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(0, 3, whereBillMainCash2);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//6result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//7
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//8result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        double totPendapatan = billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue();
                        totalSumSub=totalSumSub+(totPendapatan);
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=1 AND ("+cashCashierID2+")");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 AND ("+cashCashierID2+")");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=2 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=2 AND ("+cashCashierID2+")");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")", " ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        
                        double cashPayment = totalPaymentCash+totalPaymentCreditCard+totalPaymentDebitCard+totalPaymentBG+totalPaymentCek;
                        double creditCL= totPendapatan - cashPayment;
                        totalSumKredit = totalSumKredit+creditCL;
                        
                        ja.put(""+decimalFormat.format(totalPaymentCash));//12
                        
                        ja.put(""+decimalFormat.format(creditCL));//12 CL
                        
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));//14
                        
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));//15
                        
                        ja.put(""+decimalFormat.format(totalPaymentBG));//16
                        
                        ja.put(""+decimalFormat.format(totalPaymentCek));//17
                        //compliment
                        ja.put("0");//18result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//19result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("<b>TOTAL</b>");//2//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//3//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//4//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//5+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//6+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//7+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//8+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//9+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//10+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//11+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");//12
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");//13
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");//14
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");//15
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");//16
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");//17
                    //compliment
                    ja.put("<b>0</b>");//18
                    ja.put("");//19
                    array.put(ja);  
                break; 
                
            case VIEW_SALES_REPORT_ACCUMULATIVE_BY_YEARLY:
                totalSumPax=0;
                totalSumFood=0;
                totalSumBeverage=0;
                totalSumOther=0;
                totalNetSale=0;
                totalSumDiscount=0;
                totalSumService=0;
                totalSumTax=0;
                totalSumSub=0;
                totalSumKredit=0;

                subTotalCash=0;
                subTotalCCredit=0;
                subTotalDCredit=0;
                subTotalBG=0;
                subTotalCek=0;
                subTotalFoc=0;

                whereBill="";
                whereBillReturn="";
                whereBillKembali="";
                whereBillReturnKembalian="";
                urutretunrn=0;
                urut=0;
                nomor=start;
                
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat formater2 = new SimpleDateFormat("MMM-yyyy");
                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();

                    try {
                        beginCalendar.setTime(formater.parse(this.dateStart));
                        finishCalendar.setTime(formater.parse( this.dateEnd));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                
                    while (beginCalendar.before(finishCalendar)) {
                         Date startMTD = beginCalendar.getTime();
                         startMTD.setDate(1);
                         
                         int dayOfMonth = beginCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                         Date endMTD = beginCalendar.getTime();
                         endMTD.setDate(dayOfMonth);
                        
                         String sDateStart = formater.format(startMTD);
                         String sDateEnd = formater.format(endMTD);
                         String date = formater2.format(beginCalendar.getTime()).toUpperCase();
                         
                         cashCashierID = PstBillMain.listCashCashierPerDay(sDateStart,sDateEnd,1,"cash_master.LOCATION_ID='"+this.locationId+"'"); 
                         //BILL NOT PAID
                         whereBillMainCash += ""
                            + " "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                            + " (("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                            + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                            + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";
                        whereBillMainCash += ""
                            + " OR ("+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                            + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                            + " "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                            + " AND ("+cashCashierID+") ";
                        
                        cashCashierID2 = PstBillMain.listCashCashierPerDay(sDateStart,sDateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                        String whereBillMainCash2 = ""
                            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                            + " (( cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";
                        whereBillMainCash2 += ""
                            + " OR ( cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) "
                            + " AND ("+cashCashierID2+") ";

                         listBillMaincash =  PstBillMain.listSummaryTranscationGroupByDate(0, 0, whereBillMainCash);

                        //UNTUK TRANSAKSI YANG DIBAYAR
                            for (int j=0;j<listBillMaincash.size();j++){
                                ja = new JSONArray();
                                BillMain billMain = (BillMain)listBillMaincash.get(j);
                                nomor=nomor+1;
                                
                                ja.put(""+nomor);//1
                                ja.put(""+date+"");

                                ja.put(""+billMain.getPaxNumber());//3result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                                totalSumPax=totalSumPax+billMain.getPaxNumber();
                                //FOOD
                                double totalFood = SessCustomeReport.getTotalByCategory(0, 0, whereBillMainCash2);
                                totalSumFood=totalSumFood+totalFood;
                                ja.put(""+decimalFormat.format(totalFood));//4result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                                //BEVERAGE

                                double totalBeverage = SessCustomeReport.getTotalByCategory(0, 1,whereBillMainCash2);
                                totalSumBeverage=totalSumBeverage+totalBeverage;
                                ja.put(""+decimalFormat.format(totalBeverage));//5result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                                //OTHER
                                double totalOther = SessCustomeReport.getTotalByCategory(0, 3,whereBillMainCash2);
                                totalSumOther=totalSumOther+totalOther;
                                ja.put(""+decimalFormat.format(totalOther));//6result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                                ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//7
                                totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                                ja.put(""+decimalFormat.format(billMain.getTaxValue()));//8result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                                totalSumTax=totalSumTax+billMain.getTaxValue();

                                ja.put(""+decimalFormat.format(billMain.getServiceValue()));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                                totalSumService=totalSumService+billMain.getServiceValue();

                                ja.put(""+decimalFormat.format(billMain.getDiscount()));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                                totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                                ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                                double totPendapatan = billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue();
                                totalSumSub=totalSumSub+(totPendapatan);

                                //PAYMENT SYSTEM
                                double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=1 AND ("+cashCashierID2+")");
                                subTotalCash=subTotalCash+totalPaymentCash;
                                double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 AND ("+cashCashierID2+")");
                                subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                                double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=2 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=2 AND ("+cashCashierID2+")");
                                subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                                double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")", " ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")");
                                subTotalBG=subTotalBG+totalPaymentBG;
                                double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(0,0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")", " ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1 AND ("+cashCashierID2+")");
                                subTotalCek=subTotalCek+totalPaymentCek;

                                double cashPayment = totalPaymentCash+totalPaymentCreditCard+totalPaymentDebitCard+totalPaymentBG+totalPaymentCek;
                                double creditCL= totPendapatan - cashPayment;
                                totalSumKredit = totalSumKredit+creditCL;

                                ja.put(""+decimalFormat.format(totalPaymentCash));//12

                                ja.put(""+decimalFormat.format(creditCL));//12 CL

                                ja.put(""+decimalFormat.format(totalPaymentCreditCard));//14

                                ja.put(""+decimalFormat.format(totalPaymentDebitCard));//15

                                ja.put(""+decimalFormat.format(totalPaymentBG));//16

                                ja.put(""+decimalFormat.format(totalPaymentCek));//17
                                //compliment
                                ja.put("0");//18result += "<td style='text-align: right; color:red'>0</td>";
                                ja.put(""+billMain.getNotes());//19result += "<td style='text-align: right; color:red'>0</td>";
                                array.put(ja);
                            }
                        beginCalendar.add(Calendar.MONTH, 1);
                    }
                    
                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("<b>TOTAL</b>");//2//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//3//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//4//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//5+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//6+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//7+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//8+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//9+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//10+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//11+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");//12
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");//13
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");//14
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");//15
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");//16
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");//17
                    //compliment
                    ja.put("<b>0</b>");//18
                    ja.put("");//19
                    array.put(ja);  
                break;
                
            case VIEW_SALES_REPORT_BY_TAX:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    ja.put("");
                    array.put(ja);  
                break;
            
            case VIEW_SALES_REPORT_BY_SERVICE:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
               
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    
                    ja.put("");
                    array.put(ja);  
                break;
                
            case VIEW_SALES_REPORT_BY_STAFF:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        AppUser ap = new AppUser();
                        ap = PstAppUser.fetch(billMain.getAppUserSalesId());
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        //CL
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        
                        ja.put(""+ap.getFullName()); // staff name
                        whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION]+"='Verification' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        String cashierName = PstLogSysHistory.getLogLoginName(whereLog);
                        ja.put(""+cashierName);
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    array.put(ja);  
                break;    
                
             case VIEW_SALES_REPORT_BY_ITEM:
                    SrcSaleReport srcSaleReport = new SrcSaleReport();
                    DateFormat formaterx = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar beginCalendarx = Calendar.getInstance();
                    Calendar finishCalendarx = Calendar.getInstance();

                    try {
                        beginCalendarx.setTime(formaterx.parse(this.dateStart));
                        finishCalendarx.setTime(formaterx.parse( this.dateEnd));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    srcSaleReport.setDateFrom(beginCalendarx.getTime());
                    srcSaleReport.setDateTo(finishCalendarx.getTime());
                    srcSaleReport.setTransType(6);
                    srcSaleReport.setGroupBy(SrcSaleReport.GROUP_BY_ITEM);
                    srcSaleReport.setSortBy(SrcSaleReport.SORT_BY_TOTAL_QTY);
                    Vector records = SaleReportDocument.getList(srcSaleReport,start, amount,SaleReportDocument.LOG_MODE_CONSOLE);
                    int noUrut=0;
                    double totQty=0;
                    double totSold=0;
                    double totCost=0;
                    for (int i = 0; i < records.size(); i++) {
                        SaleReportItem saleItem = (SaleReportItem) records.get(i);
                        noUrut=noUrut+1;
                        ja = new JSONArray();
                        ja.put(""+noUrut);//1
                        ja.put(""+saleItem.getItemCode());//2
                        ja.put(""+saleItem.getItemBarcode());//3
                        ja.put(""+saleItem.getItemName());//4
                        ja.put(""+saleItem.getTotalQty());//5
                        ja.put(""+decimalFormat.format(saleItem.getTotalSold()));//6
                        ja.put(""+decimalFormat.format(saleItem.getTotalCost()));//7
                        totQty=totQty+saleItem.getTotalQty();
                        totSold=totSold+saleItem.getTotalSold();
                        totCost=totCost+saleItem.getTotalCost();
                        array.put(ja);
                    }
                    ja = new JSONArray();
                    ja.put("");//1
                    ja.put("");//2
                    ja.put("");//3
                    ja.put("<b>Total</b>");//4
                    ja.put("<b>"+totQty+"</b>");//5
                    ja.put("<b>"+decimalFormat.format(totSold)+"</b>");//6
                    ja.put("<b>"+decimalFormat.format(totCost)+"</b>");//7
                    array.put(ja);
                    total = amount;
                break;
                
            case VIEW_SALES_REPORT_BY_VOID:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ("+cashCashierID+") ";
                 
                 listBillMaincash = PstBillMain.getListReportVoid(start, amount, whereBillMainCash);

                //UNTUK TRANSAKSI YANG DIBAYARWW
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        //Billdetail billMain = (Billdetail)listBillMaincash.get(j);
                        BillMain billMain =(BillMain) listBillMaincash.get(j);
                        AppUser ap = new AppUser();
                        ap = PstAppUser.fetch(billMain.getAppUserSalesId());
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put("");//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";
                        ja.put(""+billMain.getItemName());
                        
                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(billMain.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+billMain.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategoryVoid(0, 0, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategoryVoid(0, 1, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategoryVoid(0, 3, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(0));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+0;

                        ja.put(""+decimalFormat.format(0));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+0;

                        ja.put(""+decimalFormat.format(0));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+0;

                        ja.put(""+decimalFormat.format(0+totalFood+totalBeverage+totalOther-0+0));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(0+totalFood+totalBeverage+totalOther-0+0);
                        
                        ja.put(""+ap.getFullName());//result += "<td style='text-align: right; color:red'>0</td>";
//                        ja.put(""+billMain.getSalesCode());//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put("");//result += "<td style='text-align: right; color:red'>0</td>";
                        
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    array.put(ja);  
                break;    
                
            case VIEW_SALES_REPORT_BY_ERROR:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
                
                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ("+cashCashierID+") ";
                 
                 listBillMaincash = PstBillMain.getListReportError(start, amount, whereBillMainCash);

                //UNTUK TRANSAKSI YANG DIBAYARWW
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        //Billdetail billMain = (Billdetail)listBillMaincash.get(j);
                        BillMain billMain =(BillMain) listBillMaincash.get(j);
                        AppUser ap = new AppUser();
                        ap = PstAppUser.fetch(billMain.getAppUserSalesId());
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put("");//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";
                        ja.put(""+billMain.getItemName());
                        
                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(billMain.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+billMain.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategoryVoid(0, 0, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategoryVoid(0, 1, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategoryVoid(0, 3, "cbd.CASH_BILL_DETAIL_ID='"+billMain.getCashBillDetailId()+"'");
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(0));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+0;

                        ja.put(""+decimalFormat.format(0));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+0;

                        ja.put(""+decimalFormat.format(0));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+0;

                        ja.put(""+decimalFormat.format(0+totalFood+totalBeverage+totalOther-0+0));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(0+totalFood+totalBeverage+totalOther-0+0);
                        
                        ja.put(""+ap.getFullName());//result += "<td style='text-align: right; color:red'>0</td>";
//                        ja.put(""+billMain.getSalesCode());//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNoteItem());//result += "<td style='text-align: right; color:red'>0</td>";
                        
                        ja.put(""+billMain.getCancelationNote());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    array.put(ja);  
                break;
            
            case VIEW_SALES_REPORT_BY_COMPLIMENT:
                    
                    Location location = new Location();
                    try {
                        location = PstLocation.fetchExc(locationId);
                    } catch (Exception e) {
                    }
                    String whereCosting = " "
                        + " "+PstMatCosting.fieldNames[PstMatCosting.FLD_LOCATION_ID]+"="+locationId+""
                        + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") >= '"+this.dateStart+"'"
                        + " AND DATE("+PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_DATE]+") <= '"+this.dateEnd+"'";
                    
                    Vector listCosting = PstMatCosting.list(start, amount, whereCosting, "");
                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listCosting.size();j++){
                        ja = new JSONArray();
                        MatCosting matCosting = (MatCosting)listCosting.get(j);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put("");//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+matCosting.getCostingCode());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        ja.put("-");//4
                        ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        ja.put("-");//4
                        ja.put("-");
                        ja.put("-");
                        ja.put(""+printDateFormat.format(matCosting.getCostingDate()));
                        ja.put(""+printFormat.format(matCosting.getCostingDate()));
                        
                        ja.put("0");//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+0;
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategoryCompliment(matCosting.getOID(), 0,"", location.getStandarRateId(), location.getPriceTypeId());
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategoryCompliment(matCosting.getOID(), 1,"", location.getStandarRateId(), location.getPriceTypeId());
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategoryCompliment(matCosting.getOID(), 3,"", location.getStandarRateId(), location.getPriceTypeId());
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(0));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+0;

                        ja.put(""+decimalFormat.format(0));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+0;

                        ja.put(""+decimalFormat.format(0));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+0;

                        ja.put(""+decimalFormat.format(0));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(0);
                        
                        ja.put(""+matCosting.getRemark());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                   
                    ja.put("");
                    array.put(ja);  
                break;    
            
            case VIEW_SALES_REPORT_BY_DISCOUNT:
                
                cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");

                //BILL NOT PAID
                whereBillMainCash += ""
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                    + " ((cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 0 )";

                whereBillMainCash += ""
                    + " OR (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") "
                    + " AND cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DISCOUNT]+"!=0 ";

                 orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
                 listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

                //UNTUK TRANSAKSI YANG DIBAYAR
                     totalSumPax=0;
                     totalSumFood=0;
                     totalSumBeverage=0;
                     totalSumOther=0;
                     totalNetSale=0;
                     totalSumDiscount=0;
                     totalSumService=0;
                     totalSumTax=0;
                     totalSumSub=0;
                     totalSumKredit=0;
                     
                     subTotalCash=0;
                     subTotalCCredit=0;
                     subTotalDCredit=0;
                     subTotalBG=0;
                     subTotalCek=0;
                     subTotalFoc=0;
                     
                     whereBill="";
                     whereBillReturn="";
                     whereBillKembali="";
                     whereBillReturnKembalian="";
                     urutretunrn=0;
                     urut=0;
                     nomor=start;
                    for (int j=0;j<listBillMaincash.size();j++){
                        ja = new JSONArray();
                        Vector tempData = (Vector)listBillMaincash.get(j);
                        BillMain billMain = (BillMain)tempData.get(0);
                        CashPayments cashPayments = (CashPayments)tempData.get(1);
                        TableRoom tableRoom = (TableRoom)tempData.get(2);
                        PaymentSystem paymentSystem =(PaymentSystem)tempData.get(3);
                        nomor=nomor+1;
                        ja.put(""+nomor);//1
                        ja.put(""+billMain.getCoverNumber());//2result += "<td style='text-align: center; color:red'>"+nomor+"</td>";
                        ja.put(""+billMain.getInvoiceNumber());//3result += "<td style='text-align: center; color:red'>"+billMain.getInvoiceNumber()+"</td>";

                        if(billMain.getTableId()!=0){
                            ja.put("Dine In");//4
                        }else if (billMain.getTableId()==0 && !billMain.getShippingAddress().equals("")){
                            ja.put("Delivery");//4
                        }else if (billMain.getTableId()==0 && billMain.getShippingAddress().equals("")){   
                            ja.put("Take a Way");//4
                        }else{
                            ja.put("-");//4
                        }

                        if(tableRoom.getTableNumber()==null){
                             ja.put("-");//5result += "<td style='text-align: center; color:red'>-</td>";
                        }else{
                            ja.put(""+tableRoom.getTableNumber());//5result += "<td style='text-align: center; color:red'>"+tableRoom.getTableNumber()+"</td>";
                        }
                        ja.put(""+billMain.getCustName());//6
                        
                        String whereLog= PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID]+"='"+billMain.getOID()+"' "
                        + " AND "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL]+"='Print out bill' ORDER BY LOG_UPDATE_DATE ASC LIMIT 0,1";
                        Date count = PstLogSysHistory.getDateLog(whereLog);
                        if(count!=null){
                            ja.put(""+Formater.formatDate(count,"yyyy-MM-dd")+"");
                            ja.put(""+Formater.formatTimeLocale(count,"kk:mm:ss")+"");
                        }else{
                            ja.put("-");
                            ja.put("-");
                        }
                        ja.put(""+printDateFormat.format(billMain.getBillDate()));
                        ja.put(""+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                        
                        ja.put(""+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                        totalSumPax=totalSumPax+billMain.getPaxNumber();
                        //FOOD
                        double totalFood = SessCustomeReport.getTotalByCategory(billMain.getOID(), 0);
                        totalSumFood=totalSumFood+totalFood;
                        ja.put(""+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                        //BEVERAGE

                        double totalBeverage = SessCustomeReport.getTotalByCategory(billMain.getOID(), 1);
                        totalSumBeverage=totalSumBeverage+totalBeverage;
                        ja.put(""+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                        //OTHER
                        double totalOther = SessCustomeReport.getTotalByCategory(billMain.getOID(), 3);
                        totalSumOther=totalSumOther+totalOther;
                        ja.put(""+decimalFormat.format(totalOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                        ja.put(""+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                        totalNetSale=totalNetSale+(totalFood+totalBeverage+totalOther);

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                        totalSumTax=totalSumTax+billMain.getTaxValue();

                        ja.put(""+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                        totalSumService=totalSumService+billMain.getServiceValue();

                        ja.put(""+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                        totalSumDiscount=totalSumDiscount+billMain.getDiscount();

                        ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                        totalSumSub=totalSumSub+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        
                        //PAYMENT SYSTEM
                        double totalPaymentCash = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=1 ");
                        subTotalCash=subTotalCash+totalPaymentCash;
                        ja.put(""+decimalFormat.format(totalPaymentCash));
                        
                        //CL
                        if(billMain.getDocType()==0 && billMain.getTransctionType()==1 && billMain.getTransactionStatus()==1){
                            ja.put(""+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));
                            totalSumKredit=totalSumKredit+(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue());
                        }else{
                             ja.put("0");
                        }
                        
                        double totalPaymentCreditCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.CARD_INFO=1 ");
                        subTotalCCredit=subTotalCCredit+totalPaymentCreditCard;
                        ja.put(""+decimalFormat.format(totalPaymentCreditCard));
                        
                        double totalPaymentDebitCard = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=2");
                        subTotalDCredit=subTotalDCredit+totalPaymentDebitCard;
                        ja.put(""+decimalFormat.format(totalPaymentDebitCard));
                        
                        double totalPaymentBG = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.PAYMENT_TYPE=0 AND ps.BANK_INFO_OUT=1");
                        subTotalBG=subTotalBG+totalPaymentBG;
                        ja.put(""+decimalFormat.format(totalPaymentBG));
                        
                        double totalPaymentCek = SessCustomeReport.getTotalPaymentByPaymentSystem(billMain.getOID(),0," ps.CHECK_BG_INFO=0 AND ps.BANK_INFO_OUT=1");
                        subTotalCek=subTotalCek+totalPaymentCek;
                        ja.put(""+decimalFormat.format(totalPaymentCek));
                        
                        ja.put("0");//result += "<td style='text-align: right; color:red'>0</td>";
                        ja.put(""+billMain.getNotes());//result += "<td style='text-align: right; color:red'>0</td>";
                        array.put(ja);
                    }

                    //sessSalesSummaryReport = PstBillMain.sumListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"",whereKredit);
                    pax = 0;
                    service=0;
                    tax=0;
                    disc=0;

                    ja = new JSONArray();
                    ja.put("");//+nomor);//1
                    ja.put("");//+billMain.getCoverNumber());//
                    ja.put("");//+billMain.getInvoiceNumber());//
                    ja.put("");//4
                    ja.put("");//5result += "<td style='text-align: center; color:red'>-</td>";
                    ja.put("");//+billMain.getCustName());//6
                    ja.put("");
                    ja.put("");
                    ja.put("");
                    ja.put("<b>TOTAL</b>");//+printFormat.format(billMain.getBillDate()));//71result += "<td style='text-align: center; color:red'>"+printFormat.format(billMain.getBillDate())+"</td>";
                    ja.put("<b>"+decimalFormat.format(totalSumPax)+"</b>");//+billMain.getPaxNumber());//8result += "<td style='text-align: right; color:red'>"+billMain.getPaxNumber()+"</td>";
                    //FOOD
                    ja.put("<b>"+decimalFormat.format(totalSumFood)+"</b>");//+decimalFormat.format(totalFood));//9result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalFood)+"</td>";
                    //BEVERAGE
                    ja.put("<b>"+decimalFormat.format(totalSumBeverage)+"</b>");//+decimalFormat.format(totalBeverage));//10result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalBeverage)+"</td>";
                    //OTHER
                    ja.put("<b>"+decimalFormat.format(totalSumOther)+"</b>");//+decimalFormat.format(totalSumOther));//11result += "<td style='text-align: right; color:red'>"+decimalFormat.format(totalOther)+"</td>";
                    //net sales
                    ja.put("<b>"+decimalFormat.format(totalNetSale)+"</b>");//+decimalFormat.format(totalFood+totalBeverage+totalOther));//12
                    //tax
                    ja.put("<b>"+decimalFormat.format(totalSumTax)+"</b>");//+decimalFormat.format(billMain.getTaxValue()));//13result += "<td style='text-align: right; color:red '>"+decimalFormat.format(billMain.getTaxValue())+"</td>";
                    //service
                    ja.put("<b>"+decimalFormat.format(totalSumService)+"</b>");//+decimalFormat.format(billMain.getServiceValue()));//14result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getServiceValue())+"</td>";
                    //discount
                    ja.put("<b>"+decimalFormat.format(totalSumDiscount)+"</b>");//+decimalFormat.format(billMain.getDiscount()));//15result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getDiscount())+"</td>";
                    //total sales
                    ja.put("<b>"+decimalFormat.format(totalSumSub)+"</b>");//+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue()));//16result += "<td style='text-align: right; color:red'>"+decimalFormat.format(billMain.getTaxValue()+totalFood+totalBeverage+totalOther-billMain.getDiscount()+billMain.getServiceValue())+"</td>";
                    //PAYMENT SYSTEM
                    ja.put("<b>"+decimalFormat.format(subTotalCash)+"</b>");
                    ja.put("<b>"+decimalFormat.format(totalSumKredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalDCredit)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalBG)+"</b>");
                    ja.put("<b>"+decimalFormat.format(subTotalCek)+"</b>");
                    //compliment
                    ja.put("<b>0</b>");
                    ja.put("");
                    array.put(ja);  
                break;
                
            default:
                throw new AssertionError();
        }
            
        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("iUrlToGetPicture", "imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }

    public String showForm(HttpServletRequest request) {
        MarketingNewsInfo marketingNewsInfo = new MarketingNewsInfo();
        if (oid != 0) {
            try {
                marketingNewsInfo = PstMarketingNewsInfo.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");

        Vector val_locationid = new Vector(1, 1);
        Vector key_locationid = new Vector(1, 1);
        Vector vt_loc = PstLocation.list(0, 0, "", "");

        val_locationid.add("0");
        key_locationid.add("" + textListHeader[language][6] + "");

        for (int d = 0; d < vt_loc.size(); d++) {
            Location loc = (Location) vt_loc.get(d);
            val_locationid.add("" + loc.getOID() + "");
            key_locationid.add(loc.getName());
        }

        String returnData = ""
                + "<input type='hidden' name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_PROMO_ID] + "' value='" + marketingNewsInfo.getOID() + "'>"
                + "<div class='row'>"
                + "<div class='col-md-12'>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][1] + "</label>"
                + "<input type='text' class='form-control ' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_TITLE] + "' value='" + marketingNewsInfo.getTitle() + "'>"
                + "</div>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][2] + "</label>"
                + "<input type='text' class='form-control  dates' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_START] + "' value='" + Formater.formatDate(marketingNewsInfo.getValidStart(), "dd/MM/yyyy") + "'>"
                + "</div>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][3] + "</label>"
                + "<input type='text' class='form-control  dates' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_END] + "' value='" + Formater.formatDate(marketingNewsInfo.getValidEnd(), "dd/MM/yyyy") + "'>"
                + "</div>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][4] + "</label>"
                + " <textarea class='form-control' rows='5' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_DESCRIPTION] + "'>" + marketingNewsInfo.getDescription() + "</textarea>"
                + "</div>"
                + "<div class='form-group'>"
                + "<label>" + textListHeader[language][7] + "</label>"
                + " " + ControlCombo.draw(FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_LOCATION_ID], null, "" + marketingNewsInfo.getLocationId() + "", val_locationid, key_locationid, "", "form-control") + ""
                + "</div>"
                + "</div>"
                + "</div>";

        return returnData;
    }

    public String saveUploadImage(HttpServletRequest request) {
        String returnData = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FIELD_APP_ROOT");
        String basename = FRMQueryString.requestString(request, "basename");
        long oidNews = oid;
        String realPath = FRMQueryString.requestString(request, "FRM_FIELD_REAL_PATH");

        basename = basename.replaceAll("data:image/png;base64,", "");
        basename = basename.replaceAll("data:image/jpg;base64,", "");
        basename = basename.replaceAll("data:image/jpeg;base64,", "");
        String namaImage = "" + oidNews + ".jpg";

        BufferedImage newImg;
        newImg = decodeToImage(basename);

        String url = "";

        try {
            ImageIO.write(newImg, "jpg", new File("" + getServletContext().getRealPath("imgupload/marketing") + "/" + namaImage + ""));
        } catch (Exception e) {
        }

        return returnData;
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public static Date getWeekStart(Date date, int weekStart)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int loop = (calendar.get(Calendar.DAY_OF_WEEK) - 1);
        for (int d = 0; d < loop; d++)
        {
            calendar.add(Calendar.DATE, -1);
        }
        //startOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getWeekEnd(Date date, int weekStart)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int loop = 7 - calendar.get(Calendar.DAY_OF_WEEK);
        for (int d = 0; d < loop; d++)
        {
            calendar.add(Calendar.DATE, 1);
        }
        //endOfDay(calendar);
        return calendar.getTime();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
