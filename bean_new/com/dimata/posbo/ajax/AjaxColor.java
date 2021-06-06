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

import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.form.masterdata.CtrlColor;
import com.dimata.posbo.form.masterdata.FrmColor;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
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
 * @author Dewa
 */
public class AjaxColor extends HttpServlet {

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

    //BOOLEAN
    private boolean privUpdate = false;
    private boolean privDelete = false;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

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

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        //BOOLEAN
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");

        //OBJECT
        this.jSONObject = new JSONObject();

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
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showForm(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveKondisi(request);
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
                PstColor.fieldNames[PstColor.FLD_COLOR_ID],
                PstColor.fieldNames[PstColor.FLD_COLOR_CODE],
                
             
            };
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

        if (dataFor.equals("list")) {

            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstColor.fieldNames[PstColor.FLD_COLOR_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstColor.fieldNames[PstColor.FLD_COLOR_NAME] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstColor.fieldNames[PstColor.FLD_COLOR_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstColor.fieldNames[PstColor.FLD_COLOR_NAME] + " LIKE '%" + searchTerm + "%')";
                        
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstColor.getCount(whereClause);
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
        Color color = new Color();
        String whereClause = "";
        String order = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("list")) {

            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstColor.fieldNames[PstColor.FLD_COLOR_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstColor.fieldNames[PstColor.FLD_COLOR_NAME] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstColor.fieldNames[PstColor.FLD_COLOR_CODE] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstColor.fieldNames[PstColor.FLD_COLOR_NAME] + " LIKE '%" + searchTerm + "%')";
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("list")) {
            listData = PstColor.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("list")) {
                String buttonAction = "";
                color = (Color) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + color.getColorCode());
                ja.put("" + color.getColorName());
                buttonAction = "<div class=\"text-center\">";
                if (true) {
                    buttonAction += "<button type='button' class='btn btn-sm btn-warning btneditgeneral' data-oid='" + color.getOID() + "' data-for='showform' style=\"\" ><i class='fa fa-pencil'></i> Edit</button> &nbsp;";
                }
                if (true) {
                    buttonAction += "<button class='btn btn-sm btn-danger button-delete' type='button' data-oid='" + color.getOID() + "' data-for='delete' data-command = '" + Command.DELETE + "'>Delete</button>";
                }
                buttonAction += "</div>";
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
        Color color = new Color();
        if (oid != 0) {
            try {
                color = PstColor.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        String returnData = ""
        + "<div class='row'>"
            + "<div class='col-md-12'>"
                + "<div class='form-group'>"
                    + "<label>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmColor.fieldQuestion[FrmColor.FRM_FIELD_COLOR_CODE] + "'></i>"
                    + " Color Code <i style='color:red'>(m*</i></label>"
                    + "<input type='text' class='form-control' name='" + FrmColor.fieldNames[FrmColor.FRM_FIELD_COLOR_CODE] + "' value='" + color.getColorCode()+ "' data-required='required' data-number='false' data-alpha='true' data-special='' data-type='text' data-error-message='" + FrmColor.fieldError[FrmColor.FRM_FIELD_COLOR_CODE] + "' >"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmColor.fieldQuestion[FrmColor.FRM_FIELD_COLOR_NAME] + "'></i>"
                    + " Color Name <i style='color:red'>(m*</i></label>"
                    + "<input type='text' class='form-control' name='" + FrmColor.fieldNames[FrmColor.FRM_FIELD_COLOR_NAME] + "' value='" + color.getColorName() + "' data-required='required' data-number='false' data-alpha='true' data-special='' data-type='text' data-error-message='" + FrmColor.fieldError[FrmColor.FRM_FIELD_COLOR_NAME] + "' >"
                + "</div>"      
            + "</div>"
        + "</div>";

        return returnData;
    }

    public String saveKondisi(HttpServletRequest request) {
        String returnData = "";
        CtrlColor color = new CtrlColor(request);
        color.action(iCommand, oid, oidDelete);
        returnData = color.getMessage();
        return returnData;
    }

    public String deleteAll(HttpServletRequest request) {
        String returnData = "";
        CtrlColor color = new CtrlColor(request);
        color.action(iCommand, oid, oidDelete);
        returnData = color.getMessage();
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
