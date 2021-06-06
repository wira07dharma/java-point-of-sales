/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.masterdata.MaterialDetail;
import com.dimata.posbo.entity.masterdata.PstMaterialDetail;
import com.dimata.posbo.entity.masterdata.PstRateJualBerlian;
import com.dimata.posbo.entity.masterdata.RateJualBerlian;
import com.dimata.posbo.form.masterdata.CtrlRateJualBerlian;
import com.dimata.posbo.form.masterdata.FrmRateJualBerlian;
import com.dimata.qdep.db.DBException;

import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dewa
 */
public class AjaxRateJualBerlian extends HttpServlet {

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
    private long userId;
    private String userName = "";

    //BOOLEAN
    private boolean privUpdate = false;
    private boolean privDelete = false;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    //DOUBLE
    private double hargaJual = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidReturn = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.userId = FRMQueryString.requestLong(request, "FRM_FIELD_USER_ID");
        this.userName = FRMQueryString.requestString(request, "FRM_FIELD_USER_NAME");

        this.htmlReturn = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        //BOOLEAN
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");

        //OBJECT
        this.jSONObject = new JSONObject();

        //DOUBLE
        this.hargaJual = 0;

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.DELETEALL:
                commandDeleteAll(request);
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
            this.jSONObject.put("RETURN_HARGA_JUAL", "" + this.hargaJual);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showForm(request);
        }
        if (this.dataFor.equals("getHargaJual")) {
            getHargaJual(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveRateJual(request);
        }
    }

    public void commandDeleteAll(HttpServletRequest request) {
        if (this.dataFor.equals("deleteAll")) {
            this.htmlReturn = deleteAll(request);
        }
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("list")) {
            String[] cols = {
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE],
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE],
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME],
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE],
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION],                
                PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listhistory")) {
            String[] cols = {
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID],
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME],
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION],
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE],
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION],
                PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL],};
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("delete")) {
            this.htmlReturn = deleteAll(request);
        }
    }

    public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "sSearch");
        int amount = 10;
        int start = 0;
        int col = 0;
        String dir = "asc";
        String sStart = FRMQueryString.requestString(request, "iDisplayStart");
        String sAmount = FRMQueryString.requestString(request, "iDisplayLength");
        String sCol = FRMQueryString.requestString(request, "iSortCol_0");
        String sdir = FRMQueryString.requestString(request, "sSortDir_0");

        if (sStart != null) {
            start = Integer.parseInt(sStart);
            if (start < 0) {
                start = 0;
            }
        }
        if (sAmount != null) {
            amount = Integer.parseInt(sAmount);
            if (amount < 10) {
                amount = 10;
            }
        }
        if (sCol != null) {
            col = Integer.parseInt(sCol);
            if (col < 0) {
                col = 0;
            }
        }
        if (sdir != null) {
            if (!sdir.equals("desc")) {
                dir = "desc";
            }
        }

        String whereClause = "";

        if (dataFor.equals("list")) {
            if (whereClause.length() > 0) {
                whereClause += "AND "
                        + " (" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE] + " LIKE '%" + searchTerm + "%')";
            }
        } else if (dataFor.equals("listhistory")) {
            if (whereClause.length() > 0) {
                whereClause += "AND "
                        + " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstRateJualBerlian.getCount(whereClause);
        } else if (dataFor.equals("listhistory")) {
            total = PstLogSysHistory.getCount(whereClause + " AND " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "=" + oid);
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
        RateJualBerlian rateJualBerlian = new RateJualBerlian();
        LogSysHistory logSysHistory = new LogSysHistory();
        String whereClause = "";
        String order = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (dataFor.equals("list")) {
            if (whereClause.length() > 0) {
                whereClause += "AND"
                        + " (" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_UPDATE_DATE] + " LIKE '%" + searchTerm + "%')";
            }
        } else if (dataFor.equals("listhistory")) {
            if (whereClause.length() > 0) {
                whereClause += "AND"
                        + " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("list")) {
            listData = PstRateJualBerlian.list(start, amount, whereClause, order);
        } else if (datafor.equals("listhistory")) {
            listData = PstLogSysHistory.listPurchaseOrder(start, amount, whereClause + " AND " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "=" + oid, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            String aStatusAktif[] = {"Tidak aktif", "Aktif"};
            if (datafor.equals("list")) {
                rateJualBerlian = (RateJualBerlian) listData.get(i);
                ja.put("" + (this.start + i + 1));
                //ja.put("" + rateJualBerlian.getCode());
                //ja.put("" + rateJualBerlian.getName());                
                ja.put("<div class='text-right'>" + String.format("%,.2f", rateJualBerlian.getRate()) + "</div>");
                ja.put("" + rateJualBerlian.getDescription());
                ja.put("" + Formater.formatDate(rateJualBerlian.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                ja.put("" + aStatusAktif[rateJualBerlian.getStatusAktif()]);
                array.put(ja);
            } else if (datafor.equals("listhistory")) {
                logSysHistory = (LogSysHistory) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + logSysHistory.getLogLoginName());
                ja.put("" + logSysHistory.getLogUserAction());
                ja.put("" + Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                ja.put("" + logSysHistory.getLogApplication());
                ja.put("" + logSysHistory.getLogDetail().replaceAll(";", "<br>"));
                String buttonAction = ""
                        + "<div class=\"btn-group\">"
                        + "<button type='button' class='btn btn-warning btneditgeneral' data-oid='" + rateJualBerlian.getOID() + "' data-for='showform'><i class='fa fa-pencil'></i> Edit</button>"
                        + "<button type='button' class='btn btn-primary btneditgeneral' data-oid='" + rateJualBerlian.getOID() + "' data-for='showhistory'><i class='fa fa-history'></i> History</button>"
                        + "</div>";
                ja.put(buttonAction);
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }

    public String showForm(HttpServletRequest request) {
        RateJualBerlian rateJualBerlian = new RateJualBerlian();
        if (oid != 0) {
            try {
                rateJualBerlian = PstRateJualBerlian.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        
        String returnData = ""
                + "<div class='row'>"
                    + "<div class='col-md-12'>"
                
//                        + "<div class='form-group'>"
//                            + "<label class='col-sm-4'>"
//                                + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmRateJualBerlian.fieldQuestion[FrmRateJualBerlian.FRM_FIELD_CODE] + "'></i>"
//                                + " Kode <i style='color:red'>*</i>"
//                            + "</label>"
//                            + "<div class='col-md-8'>"
//                                + "<input type='text' class='form-control' name='" + FrmRateJualBerlian.fieldNames[FrmRateJualBerlian.FRM_FIELD_CODE] + "' value='" + rateJualBerlian.getCode()+ "' data-required='required' data-number='false' data-alpha='true' data-special='' data-type='number' data-error-message='" + FrmRateJualBerlian.fieldError[FrmRateJualBerlian.FRM_FIELD_CODE] + "' >"
//                            + "</div>"
//                        + "</div>"
//                
//                        + "<div class='form-group'>"
//                            + "<label class='col-sm-4'>"
//                                + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmRateJualBerlian.fieldQuestion[FrmRateJualBerlian.FRM_FIELD_NAME] + "'></i>"
//                                + " Nama <i style='color:red'>*</i>"
//                            + "</label>"
//                            + "<div class='col-md-8'>"
//                                + "<input type='text' class='form-control' name='" + FrmRateJualBerlian.fieldNames[FrmRateJualBerlian.FRM_FIELD_NAME] + "' value='" + rateJualBerlian.getName()+ "' data-required='required' data-number='true' data-alpha='true' data-special='true' data-type='number' data-error-message='" + FrmRateJualBerlian.fieldError[FrmRateJualBerlian.FRM_FIELD_NAME] + "' >"
//                            + "</div>"
//                        + "</div>"
                
                        + "<div class='form-group'>"
                            + "<label class='col-sm-4'>"
                                + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmRateJualBerlian.fieldQuestion[FrmRateJualBerlian.FRM_FIELD_RATE] + "'></i>"
                                + " Rate <i style='color:red'>*</i>"
                            + "</label>"
                            + "<div class='col-md-8'>"
                                + "<input type='text' class='form-control date' name='" + FrmRateJualBerlian.fieldNames[FrmRateJualBerlian.FRM_FIELD_RATE] + "' value='" + rateJualBerlian.getRate()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmRateJualBerlian.fieldError[FrmRateJualBerlian.FRM_FIELD_RATE] + "'>"
                            + "</div>"
                        + "</div>"
                
                        + "<div class='form-group'>"
                            + "<label class='col-sm-4'>"
                                + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmRateJualBerlian.fieldQuestion[FrmRateJualBerlian.FRM_FIELD_DESCRIPTION] + "'></i>"
                                + " Deskripsi <i style='color:red'>*</i>"
                            + "</label>"
                            + "<div class='col-md-8'>"
                                + "<input type='text' class='form-control' name='" + FrmRateJualBerlian.fieldNames[FrmRateJualBerlian.FRM_FIELD_DESCRIPTION] + "' value='" + rateJualBerlian.getDescription()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmRateJualBerlian.fieldError[FrmRateJualBerlian.FRM_FIELD_DESCRIPTION] + "'>"
                            + "</div>"
                        + "</div>"
                
                        + "<p><i style='color:red'>* = (wajib diisi)</i></p>"
                
                    + "</div>"
                + "</div>";

        return returnData;
    }

    public String saveRateJual(HttpServletRequest request) {
        String returnData = "";
        CtrlRateJualBerlian ctrlRateJualBerlian = new CtrlRateJualBerlian(request);
        ctrlRateJualBerlian.action(iCommand, oid, userId, userName);
        RateJualBerlian rateJualBerlian = ctrlRateJualBerlian.getRateJualBerlian();
        long newRateJual = rateJualBerlian.getOID();
        //update status rate jual lama jadi non aktif
        if (newRateJual > 0) {
            Vector listRateJual = PstRateJualBerlian.list(0, 0, "" + PstRateJualBerlian.fieldNames[PstRateJualBerlian.FLD_RATE_JUAL_BERLIAN_ID] + " <> " + newRateJual, "");
            for (int i = 0; i < listRateJual.size(); i++) {
                try {
                    RateJualBerlian rjb = (RateJualBerlian) listRateJual.get(i);
                    rjb.setStatusAktif(0);
                    PstRateJualBerlian.updateExc(rjb);
                } catch (DBException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        //update all material detail rate value with the latest
        //update harga jual
        updateAllItemRate(rateJualBerlian.getRate());
        returnData = ctrlRateJualBerlian.getMessage();
        return returnData;
    }

    public void updateAllItemRate(double newRateJual) {
        Vector listMatDetail = PstMaterialDetail.list(0, 0, "", "");
        for (int i = 0; i < listMatDetail.size(); i++) {
            MaterialDetail md = (MaterialDetail) listMatDetail.get(i);
            double faktorJual = md.getFaktorJual();
            md.setRate(newRateJual);
            md.setHargaJual(faktorJual * newRateJual);
            try {
                PstMaterialDetail.updateExc(md);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String deleteAll(HttpServletRequest request) {
        String returnData = "";
        CtrlRateJualBerlian ctrlRateJualBerlian = new CtrlRateJualBerlian(request);
        ctrlRateJualBerlian.action(iCommand, oid, userId, userName);
        returnData = ctrlRateJualBerlian.getMessage();
        return returnData;
    }

    // get HARGA_JUAL from "pos_material_emas_lantakan" table
    public void getHargaJual(HttpServletRequest request) {
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
    }// </editor-fold>

}
