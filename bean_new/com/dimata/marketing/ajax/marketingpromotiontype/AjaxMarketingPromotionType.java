/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.marketing.ajax.marketingpromotiontype;

import com.dimata.marketing.entity.marketingpromotiontype.MarketingPromotionType;
import com.dimata.marketing.entity.marketingpromotiontype.PstMarketingPromotionType;
import com.dimata.marketing.form.marketingpromotiontype.CtrlMarketingPromotionType;
import com.dimata.marketing.form.marketingpromotiontype.FrmMarketingPromotionType;
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
public class AjaxMarketingPromotionType extends HttpServlet{
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

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            this.jSONObject.put("FRM_FIELD_RETURN_OID", "" + this.oidReturn);
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }
    
    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("savepromotype")) {
            this.htmlReturn = savePromoType(request);
        }
    }
    
    public String savePromoType(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingPromotionType promotionType = new CtrlMarketingPromotionType(request);
        promotionType.action(iCommand, oid, oidDelete);
        MarketingPromotionType marketingPromotionType = new MarketingPromotionType();
        marketingPromotionType = promotionType.getMarketingPromotionType();
        oidReturn = marketingPromotionType.getOID();
        returnData = promotionType.getMessage();
        return returnData;
    }
    
    public void commandDelete(HttpServletRequest request){
        if (this.dataFor.equals("deletepromotype")){
            this.htmlReturn = deletePromoType(request);
        }
    }
    
    public String deletePromoType(HttpServletRequest request){
        String returnData = "";
        CtrlMarketingPromotionType promotionType = new CtrlMarketingPromotionType(request);
        promotionType.action(iCommand, oid, oidDelete);
        returnData = promotionType.getMessage();
        return returnData;
    }
    
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listpromotype")) {
            String[] cols = {
                PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID],
                PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE],
                PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
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

        if (dataFor.equals("listpromotype")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                    + "AND (" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            } else {
                whereClause += " "
                    + "(" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listpromotype")) {
            total = PstMarketingPromotionType.getCount(whereClause);
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
        MarketingPromotionType promotionType = new MarketingPromotionType();
        
        String whereClause = "";
        String order = "";
        
        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("listpromotype")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                    + "AND (" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + ")";
                } else {
                    whereClause += " "
                    + "(" + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_MARKETING_PROMOTION_TYPE_ID] + " LIKE '%" + searchTerm + "%' "
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE] + " LIKE '%" + searchTerm + "%'"
                    + " OR " + PstMarketingPromotionType.fieldNames[PstMarketingPromotionType.FLD_PROMOTION_TYPE_NAME] + " LIKE '%" + searchTerm + "%'"
                    + ")";
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listpromotype")) {
            listData = PstMarketingPromotionType.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listpromotype")) {
                promotionType = (MarketingPromotionType) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + promotionType.getPromotionTypeName());
                ja.put("" + promotionType.getPromotionType());
                ja.put(" <a class='btn-edit' data-oid='"+promotionType.getOID()+"' href='#'><i class='fa fa-eye'></i></a>"
                        + " | "
                        + "<a data-oid='"+promotionType.getOID()+"' href='#' data-backdrop='static'><i class='fa fa-pencil'></i></a>"
                        + " | "
                        + "<a class='btn-delete' data-oid='"+promotionType.getOID()+"' data-for='deletepromotype' data-command = '" + Command.DELETE + "' href='#'><i class='fa fa-remove'></i></a>");
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }
    
    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showformpromotype")) {
            this.htmlReturn = showFormPromotionType(request);
        }
    }
    
    public String showFormPromotionType(HttpServletRequest request) {
        MarketingPromotionType promotionType = new MarketingPromotionType();
        if (oid != 0) {
            try {
                promotionType = PstMarketingPromotionType.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        
        String returnData = ""
            + "<input type='hidden' name='"+FrmMarketingPromotionType.fieldNames[FrmMarketingPromotionType.FRM_FIELD_MARKETING_PROMOTION_TYPE_ID]+"' value='"+promotionType.getOID()+"'>"
            + "<div class='row'>"
                + "<div class='col-sm-12'>"
                    + "<div class='form-group'>"
                        + "<label>Promotion Type : </label>"
                        + "<select class='form-control input-sm' name='" + FrmMarketingPromotionType.fieldNames[FrmMarketingPromotionType.FRM_FIELD_PROMOTION_TYPE] + "'>"
                            + "<option value='1'>Promotion Type</option>"
                            + "<option value='0'>Discount Promo Type</option>"
                        + "</select>"
                    + "</div>"
                + "</div>"
                + "<div class='col-sm-12'>"
                    + "<div class='form-group'>"
                        + "<label>Promotion Name : </label>"
                        + "<input type='text' class='form-control input-sm promotypename'  name='" + FrmMarketingPromotionType.fieldNames[FrmMarketingPromotionType.FRM_FIELD_PROMOTION_TYPE_NAME] + "' value='"+promotionType.getPromotionTypeName()+"'>"
                    + "</div>"
                + "</div>"
            + "</div>";
        
        return returnData;
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
