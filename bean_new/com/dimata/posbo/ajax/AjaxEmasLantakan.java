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

import com.dimata.posbo.entity.masterdata.EmasLantakan;
import com.dimata.posbo.entity.masterdata.PstEmasLantakan;
import com.dimata.posbo.form.masterdata.CtrlEmasLantakan;
import com.dimata.posbo.form.masterdata.FrmEmasLantakan;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.posbo.entity.masterdata.MaterialNilaiTukarEmas;
import com.dimata.posbo.entity.masterdata.PstMaterialNilaiTukarEmas;
import com.dimata.posbo.session.emaslantakan.EmasLantakanManager;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxEmasLantakan extends HttpServlet {

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
        if (this.dataFor.equals("showhistory")) {
            this.htmlReturn = showFormLog(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveEmasLantakan(request);
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
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE],
                PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF]
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
                whereClause += "AND (" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE] + " LIKE '%" + searchTerm + "%')"
                        + "";
            } else {
                whereClause += " (" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE] + " LIKE '%" + searchTerm + "%')"
                        + "";
            }
        } else if (dataFor.equals("listhistory")) {
            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstEmasLantakan.getCount(whereClause);
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
        EmasLantakan emasLantakan = new EmasLantakan();
        LogSysHistory logSysHistory = new LogSysHistory();
        String whereClause = "";
        String order = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (dataFor.equals("list")) {
            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE] + " LIKE '%" + searchTerm + "%')"
                        + "";
            } else {
                whereClause += " (" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_BELI] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_JUAL] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_HARGA_TENGAH] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_END_DATE] + " LIKE '%" + searchTerm + "%')"
                        + "";
            }
        } else if(dataFor.equals("listhistory")) {
            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_USER_ACTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_APPLICATION] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DETAIL] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_LOGIN_NAME] + " LIKE '%" + searchTerm + "%' "
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
            listData = PstEmasLantakan.list(start, amount, whereClause, order);
        } else if(datafor.equals("listhistory")) {
            listData = PstLogSysHistory.listPurchaseOrder(start, amount, whereClause + " AND " + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "=" + oid, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            String statusAktif[] = {"Tidak aktif", "Aktif"};
            if (datafor.equals("list")) {
                emasLantakan = (EmasLantakan) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + FRMHandler.userFormatStringDecimal(emasLantakan.getHargaBeli()));
                ja.put("" + FRMHandler.userFormatStringDecimal(emasLantakan.getHargaJual()));
                ja.put("" + FRMHandler.userFormatStringDecimal(emasLantakan.getHargaTengah()));
                ja.put("" + Formater.formatDate(emasLantakan.getStartDate(), "yyyy-MM-dd"));
                ja.put("" + Formater.formatDate(emasLantakan.getEndDate(), "yyyy-MM-dd"));
                ja.put("" + statusAktif[emasLantakan.getStatusAktif()]);
                String buttonAction = ""
                    + "<div class=\"text-center\">"
                        //+ "<button type='button' class='btn btn-sm btn-warning btneditgeneral' data-oid='" + emasLantakan.getOID() + "' data-for='showform'><i class='fa fa-pencil'></i> Edit</button>"
                        //+ "&nbsp;"
                        + "<a href='#' class='btnhistory' data-oid='" + emasLantakan.getOID() + "' data-for='showhistory'><i class='fa fa-history'></i> History</a>"
                    + "</div>";
                ja.put(buttonAction);
                array.put(ja);
            } else if(datafor.equals("listhistory")) {
                logSysHistory = (LogSysHistory) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + logSysHistory.getLogLoginName());
                ja.put("" + logSysHistory.getLogUserAction());
                ja.put("" + Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
                ja.put("" + logSysHistory.getLogApplication());
                ja.put("" + logSysHistory.getLogDetail().replaceAll(";", "<br>"));
                String buttonAction = ""
                    + "<div class=\"btn-group\">"
                        + "<button type='button' class='btn btn-warning btneditgeneral' data-oid='" + emasLantakan.getOID() + "' data-for='showform'><i class='fa fa-pencil'></i> Edit</button>"
                        + "<button type='button' class='btn btn-primary btneditgeneral' data-oid='" + emasLantakan.getOID() + "' data-for='showhistory'><i class='fa fa-history'></i> History</button>"
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
        EmasLantakan emasLantakan = new EmasLantakan();
        if (oid != 0) {
            try {
                emasLantakan = PstEmasLantakan.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        
        String returnData = ""
                + "<div class='form-group'>"
                    + "<label class='col-sm-4'>"
                        + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmEmasLantakan.fieldQuestion[FrmEmasLantakan.FRM_FIELD_HARGA_BELI] + "'></i>"
                        + " Harga Beli<i style='color:red'>*</i>"
                    + "</label>"
                    + "<div class='col-sm-8'>"
                        + "<input type='text' class='form-control input-sm' name='" + FrmEmasLantakan.fieldNames[FrmEmasLantakan.FRM_FIELD_HARGA_BELI] + "' value='" + emasLantakan.getHargaBeli() + "' data-required='required' data-number='false' data-alpha='true' data-special='' data-type='number' data-error-message='" + FrmEmasLantakan.fieldError[FrmEmasLantakan.FRM_FIELD_HARGA_BELI] + "' >"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-4'>"
                        + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmEmasLantakan.fieldQuestion[FrmEmasLantakan.FRM_FIELD_HARGA_JUAL] + "'></i>"
                        + " Harga Jual<i style='color:red'>*</i>"
                    + "</label>"
                    + "<div class='col-sm-8'>"
                        + "<input type='text' class='form-control input-sm' name='" + FrmEmasLantakan.fieldNames[FrmEmasLantakan.FRM_FIELD_HARGA_JUAL] + "' value='" + emasLantakan.getHargaJual() + "' data-required='required' data-number='true' data-alpha='true' data-special='true' data-type='number' data-error-message='" + FrmEmasLantakan.fieldError[FrmEmasLantakan.FRM_FIELD_HARGA_JUAL] + "' >"
                    + "</div>"      
                + "</div>"      
                + "<div class='form-group'>"
                    + "<label class='col-sm-4'>"
                        + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmEmasLantakan.fieldQuestion[FrmEmasLantakan.FRM_FIELD_HARGA_TENGAH] + "'></i>"
                        + " Harga Tengah<i style='color:red'>*</i>"
                    + "</label>"
                    + "<div class='col-sm-8'>"
                        + "<input type='text' class='form-control input-sm' name='" + FrmEmasLantakan.fieldNames[FrmEmasLantakan.FRM_FIELD_HARGA_TENGAH] + "' value='" + emasLantakan.getHargaTengah() + "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmEmasLantakan.fieldError[FrmEmasLantakan.FRM_FIELD_HARGA_TENGAH] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-4'>"
                        + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmEmasLantakan.fieldQuestion[FrmEmasLantakan.FRM_FIELD_START_DATE] + "'></i>"
                        + " Tanggal Awal<i style='color:red'>*</i>"
                    + "</label>"
                    + "<div class='col-sm-8'>"
                        + "<input type='text' class='form-control input-sm dates' name='" + FrmEmasLantakan.fieldNames[FrmEmasLantakan.FRM_FIELD_START_DATE] + "' value='" + Formater.formatDate(emasLantakan.getStartDate(), "yyyy-MM-dd") + "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmEmasLantakan.fieldError[FrmEmasLantakan.FRM_FIELD_START_DATE] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-4'>"
                        + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmEmasLantakan.fieldQuestion[FrmEmasLantakan.FRM_FIELD_END_DATE] + "'></i>"
                        + " Tanggal Akhir<i style='color:red'>*</i>"
                    + "</label>"
                    + "<div class='col-sm-8'>"
                        + "<input type='text' class='form-control input-sm dates' name='" + FrmEmasLantakan.fieldNames[FrmEmasLantakan.FRM_FIELD_END_DATE] + "' value='" + Formater.formatDate(emasLantakan.getEndDate(), "yyyy-MM-dd") + "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmEmasLantakan.fieldError[FrmEmasLantakan.FRM_FIELD_END_DATE] + "'>"
                    + "</div>"
                + "</div>"
                + "<p><i style='color:red'>* = (wajib diisi)</i></p>";

        return returnData;
    }

    public String saveEmasLantakan(HttpServletRequest request) {
        String returnData = "";
        CtrlEmasLantakan ctrlEmasLantakan = new CtrlEmasLantakan(request);
        ctrlEmasLantakan.action(iCommand, oid, userId, userName);
        EmasLantakan emasLantakan = ctrlEmasLantakan.getEmasLantakan();
        long newEmasLantakan = emasLantakan.getOID();
        //update data emas lantakan lama menjadi non aktif
        int code = PstEmasLantakan.updateStatusEmasLantakan(newEmasLantakan);
        //---
        //EmasLantakanManager emasLantakanManager = new EmasLantakanManager();
        //emasLantakanManager.startMonitor();
        //---
        returnData = ctrlEmasLantakan.getMessage();
        return returnData;
    }

    public String deleteAll(HttpServletRequest request) {
        String returnData = "";
        CtrlEmasLantakan kadar = new CtrlEmasLantakan(request);
        kadar.action(iCommand, oid, userId, userName);
        returnData = kadar.getMessage();
        return returnData;
    }

    // get HARGA_JUAL from "pos_material_emas_lantakan" table
    public void getHargaJual(HttpServletRequest request) {
        EmasLantakan emasLantakan = new EmasLantakan();
        try {
            emasLantakan = PstEmasLantakan.getEmasLantakan();
        } catch (Exception ex) {
        }

        double materialPrice = 0;
        long kadarOID = FRMQueryString.requestLong(request, "FRM_KADAR_ID");
        long colorOID = FRMQueryString.requestLong(request, "FRM_COLOR_ID");

        Vector listMaterialNilaiTukarEmas = PstMaterialNilaiTukarEmas.list(0, 1, PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " = " + kadarOID + ""
                + " AND " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID] + " = " + colorOID, null);

        if (!listMaterialNilaiTukarEmas.isEmpty()) {
            MaterialNilaiTukarEmas materialNilaiTukarEmas = (MaterialNilaiTukarEmas) listMaterialNilaiTukarEmas.get(0);
            materialPrice = emasLantakan.getHargaJual() * materialNilaiTukarEmas.getLokal() * 0.01;
            this.hargaJual = materialPrice;
        } else {

        }
    }

    public String showhistory(HttpServletRequest request) {
        String html = "";
        html += ""
                + "<table class='table table-bordered'>"
                + "<tr>"
                + "<th>No.</th>"
                + "<th>Harga Beli</th>"
                + "<th>Harga Jual</th>"
                + "<th>Harga Tengah</th>"
                + "<th>Tanggal Awal</th>"
                + "<th>Tanggal Selesai</th>"
                + "</tr>";
        Vector listEmasLantakanNonAktif = PstEmasLantakan.list(0, 0, "" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_EMAS_LANTAKAN_ID] + " <> " + this.oid, "" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_START_DATE] + " DESC");
        for (int i = 0; i < listEmasLantakanNonAktif.size(); i++) {
            EmasLantakan el = (EmasLantakan) listEmasLantakanNonAktif.get(i);
            html += ""
                    + "<tr>"
                    + "<td>" + (i + 1) + "</td>"
                    + "<td>" + String.format("%,.0f", el.getHargaBeli()) + "</td>"
                    + "<td>" + String.format("%,.0f", el.getHargaJual()) + "</td>"
                    + "<td>" + String.format("%,.0f", el.getHargaTengah()) + "</td>"
                    + "<td>" + Formater.formatDate(el.getStartDate(), "yyyy-MM-dd") + "</td>"
                    + "<td>" + Formater.formatDate(el.getEndDate(), "yyyy-MM-dd") + "</td>"
                    + "</tr>";
        }
        html += ""
                + "</table>"
                + "";
        return html;
    }
    
    public String showFormLog(HttpServletRequest request) {
        LogSysHistory logSysHistory = new LogSysHistory();
        Vector listData = PstLogSysHistory.listPurchaseOrder(0, 0, PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + " = " + oid, PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " DESC ");;
        String returnData = ""
                + "<div class='row'>"
                + "<div class='col-md-12'>"
                + "<table class='table table-bordered table-striped table-info'>"
                + "<thead class='thead-inverse'>"
                + "<tr>"
                + "<th>No.</th>"
                + "<th>Login Name</th>"
                + "<th>User Action</th>"
                + "<th>Update Date</th>"
                + "<th>Application</th>"
                + "<th>Detail</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>";
        for (int i = 0; i < listData.size(); i++) {
            logSysHistory = (LogSysHistory) listData.get(i);
            returnData += "<tr>"
                    + "<td>" + (i + 1) + "</td>"
                    + "<td>" + logSysHistory.getLogLoginName() + "</td>"
                    + "<td>" + logSysHistory.getLogUserAction() + "</td>"
                    + "<td>" + Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd hh:mm:ss") + "</td>"
                    + "<td>" + logSysHistory.getLogApplication() + "</td>"
                    + "<td>" + logSysHistory.getLogDetail() + "</td>"
                    + "</tr>";
        }

        returnData += "</tbody>"
                + "</table>"
                + "</div>"
                + "</div>"
                + "</div>";

        return returnData;
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
