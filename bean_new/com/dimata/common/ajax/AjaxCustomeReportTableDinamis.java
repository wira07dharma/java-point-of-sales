/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.PaymentSystem;
import com.dimata.common.entity.payment.PstPaymentSystem;
import com.dimata.common.session.customreport.SessCustomReport;
import com.dimata.common.session.report.SessCustomeReport;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.marketing.entity.marketingnewsinfo.PstMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.CtrlMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.FrmMarketingNewsInfo;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.entity.payment.CashPayments;
import com.dimata.pos.entity.payment.PstCashReturn;
import com.dimata.posbo.entity.masterdata.TableRoom;
import com.dimata.posbo.session.report.SessSalesSummaryReport;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;
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
public class AjaxCustomeReportTableDinamis
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
    private long nilaiCombo = 0;
    private long locationId=0;
    
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
        this.nilaiCombo = FRMQueryString.requestLong(request, "FRM_FIELD_COMBO");
        this.locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
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
        int isCustomeReport = SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
        if(isCustomeReport==1){
            if (this.dataFor.equals("getColumn")) {
                 //cek berdasarkan id yang di kirim
                 String query = SessCustomeReport.getQueryReport(this.nilaiCombo,"","");
                 String queryWhere = SessCustomeReport.getQueryReportWhere(this.nilaiCombo,"","");
                 listData = SessCustomReport.listCustome(query,"","");  
            }
            //map table colom
            Vector objectColomn = (Vector) listData.get(0);
            for (int i = 0; i <= objectColomn.size() - 1; i++) {
                if (this.dataFor.equals("getColumn")) {
                    String column = (String) objectColomn.get(i);
                    html += "<th>" + column + "</th>";
                }
            }
        }else{
            html = getColomReportFull();
        }
        return html;
    }
    
    public String getColomReportFull() {
        //Cari cash cashier
        String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0);
        String whereCashPayment = ""
            + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";
        Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");
        String html=""
            + "<th>No</th>"
            + "<th>Paper Bill</th>"
            + "<th>System Bill</th>"
            + "<th>Tyoe</th>"
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
            + "<th>Total Sales</th>"
            + "<th>Credit</th>";
            for (int i = 0; i<listPaymentSystem.size();i++){
                PaymentSystem paymentsystem = (PaymentSystem)listPaymentSystem.get(i);
                html += "<th>"+paymentsystem.getPaymentSystem()+"</th>";
            }
            html += "<th>Compliment</th>";
            html += "<th>Remark</th>";
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
        int isCustomeReport = SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
         int total = -1;
        if(isCustomeReport==1){
            if (dataFor.equals("list")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND (mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += " "
                            + "(mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            }
            String colName = cols[col];
            if (dataFor.equals("list")) {
                if (this.nilaiCombo == 1) {
                    total = 0;
                } else if (this.nilaiCombo == 4) {
                    total = 0;
                } else if (this.nilaiCombo == 3) {
                    total = 0;
                }
            }
        }else{
            String colName = cols[col];
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
                    + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 )) AND ("+cashCashierID+") "
                        + " ORDER BY "+" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";

                total = PstBillMain.countListSummaryTranscationReportHarian(0, 0, whereBillMainCash,"");
            }
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
        
        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("list")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND (mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += " "
                            + "(mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                            + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                            + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }
        
        //proses list data
        int isCustomeReport = SessCustomeReport.getIsCustome(this.nilaiCombo,"","");
        if(isCustomeReport==1){
            Vector listData = new Vector(1, 1);
            if (datafor.equals("list")) {
                if (this.nilaiCombo == 1) {
                    listData = SessCustomReport.listCustome("SELECT * FROM pos_unit", "", "");
                } else if (this.nilaiCombo == 4) {
                    listData = SessCustomReport.listCustome("SELECT * FROM pos_room", "", "");
                } else if (this.nilaiCombo == 3) {
                    listData = SessCustomReport.listCustome("SELECT * FROM aiso_aktiva_location", "", "");
                }
            }
            //map table data
            Vector objectValue = (Vector) listData.get(1);
            for (int i = 0; i <= objectValue.size() - 1; i++) {
                JSONArray ja = new JSONArray();
                if (datafor.equals("list")) {
                    Vector oValue = (Vector) objectValue.get(i);
                    for (int k = 0; k < oValue.size(); k++) {
                        String value = (String) oValue.get(k);
                        ja.put("" + value);
                    }
                    array.put(ja);
                }
            }
        }else{
            String pattern = "###,###.##";
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            String cashCashierID = PstBillMain.listCashCashierPerDay(this.dateStart,  this.dateEnd,0," cash_master.LOCATION_ID='"+this.locationId+"'");
            String whereCashPayment = ""+ " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND ("+cashCashierID+")";
            Vector listPaymentSystem = PstPaymentSystem.listDistinctCashPaymentJoinPaymentSystem(0, 0, whereCashPayment, "");
            
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
            
            
            String whereKredit="" + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+this.locationId+" AND "
                + " (cbm."+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"=0 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"=1 AND "
                + " cbm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"= 1 ) AND ("+cashCashierID+") ";
            
            String orderBy=" cbm."+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER]+" ASC ";
            Vector listBillMaincash = PstBillMain.listSummaryTranscationReportHarian(start, amount, whereBillMainCash,orderBy);

            //UNTUK TRANSAKSI YANG DIBAYAR
                double totalSumFood=0;
                double totalSumBeverage=0;
                double totalSumOther=0;
                double totalSumDiscount=0;
                double totalSumService=0;
                double totalSumTax=0;
                double totalSumSub=0;
                double totalSumKredit=0;
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
