/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.ajax;

import com.dimata.common.entity.email.PstSettingTemplateInfo;
import com.dimata.common.entity.email.SettingTemplateInfo;
import com.dimata.common.form.email.CtrlSettingTemplateInfo;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class AjaxSettingTemplateInfo extends HttpServlet {

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

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String message = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //OBJECT
        this.jSONObject = new JSONObject();
        this.jSONArray = new JSONArray();

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";
        this.message = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;

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
                //commandDelete(request);
                break;

            default:
            //commandNone(request);
        }

        try {
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("RETURN_ERROR", this.iErrCode);
            this.jSONObject.put("RETURN_MESSAGE", this.message);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("saveTemplate")) {
            saveTemplate(request);
        }
    }

    public void saveTemplate(HttpServletRequest request) {
        CtrlSettingTemplateInfo csti = new CtrlSettingTemplateInfo(request);
        this.iErrCode = csti.action(this.iCommand, this.oid);
        this.message = csti.getMessage();
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listTemplate")) {
            String[] cols = {
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_ID],
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT],
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_CONTENT_HTML],
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START],
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END],
                "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_ID],};
            this.jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
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
            if (!sdir.equals("asc")) {
                dir = "desc";
            }
        }

        String whereClause = "";

        if (dataFor.equals("listTemplate")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "("
                        + "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += ""
                        + "("
                        + "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listTemplate")) {
            total = PstSettingTemplateInfo.getCount(whereClause);
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
        SettingTemplateInfo settingTemplateInfo = new SettingTemplateInfo();

        String whereClause = "";
        String order = "";

        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("listTemplate")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                            + "AND "
                            + "("
                            + "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT] + " LIKE '%" + searchTerm + "%'"
                            + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                } else {
                    whereClause += ""
                            + "("
                            + "" + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_SUBJECT] + " LIKE '%" + searchTerm + "%'"
                            + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_START] + " LIKE '%" + searchTerm + "%'"
                            + " OR " + PstSettingTemplateInfo.fieldNames[PstSettingTemplateInfo.FLD_TEMPLATE_INFO_DATE_END] + " LIKE '%" + searchTerm + "%'"
                            + ")";
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listTemplate")) {
            listData = PstSettingTemplateInfo.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listTemplate")) {
                settingTemplateInfo = (SettingTemplateInfo) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + settingTemplateInfo.getTemplateInfoSubject());
                //ja.put("" + settingTemplateInfo.getTemplateInfoContentHtml());//.replaceAll("\\&lt.*?&gt",""));
                ja.put("" + Formater.formatDate(settingTemplateInfo.getTemplateInfoDateStart(), "dd MMM yyyy"));
                ja.put("" + Formater.formatDate(settingTemplateInfo.getTemplateInfoDateEnd(), "dd MMM yyyy"));
                ja.put("<div class='text-center'><button type='button' class='btn btn-sm btn-warning edit_template' data-oid='" + settingTemplateInfo.getOID() + "'><i class='fa fa-pencil'></i></button><div>");
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            //result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
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
