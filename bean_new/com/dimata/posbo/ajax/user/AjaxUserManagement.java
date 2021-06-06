/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax.user;

import com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit;
import com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit;
import com.dimata.common.entity.custom.DataCustom;
import com.dimata.common.entity.custom.PstDataCustom;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.hanoman.entity.masterdata.Contact;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstContact;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.form.billing.FrmBillDetail;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Company;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem;
import com.dimata.posbo.entity.purchasing.PurchaseRequestItem;
import com.dimata.posbo.form.purchasing.CtrlPurchaseRequest;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.sedana.entity.kredit.Pinjaman;
import com.dimata.sedana.entity.kredit.PstPinjaman;
import com.dimata.sedana.entity.kredit.PstTypeKredit;
import com.dimata.services.WebServices;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author WiraDharma
 */
public class AjaxUserManagement extends HttpServlet {

    //DATATABLES
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;

    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    private JSONArray jsonArrayPrintValue = new JSONArray();

    private String dataFor = "";
    private String approot = "";
    private String htmlReturn = "";
    private String message = "";
    private String apiUrl = "";
    private String sedanaAppUrl = "";
    private String hrApiUrl = "";

    private long userId = 0;
    private String userName = "";
    private long oid = 0;

    private boolean sessLogin = false;

    private int iCommand = 0;
    private int iErrCode = 0;
    private int sessLanguage = 0;

    private String start_date = "";
    private String end_date = "";
    private String petugas_delivery = "";
    private String noBillConvert = "";
    private String userStatus = "";

    private NumberFormat numberFormat = NumberFormat.getInstance(new Locale("ID", "id"));

    DataCustom dc = new DataCustom();
    Vector user = new Vector();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        this.jsonArrayPrintValue = new JSONArray();
        this.jSONArray = new JSONArray();
        this.jSONObject = new JSONObject();

        this.hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL");
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
        this.sedanaAppUrl = PstSystemProperty.getValueByName("SEDANA_APP_URL");
        this.htmlReturn = "";
        this.message = "";

        this.userId = FRMQueryString.requestLong(request, "SEND_USER_ID");
        this.userName = FRMQueryString.requestString(request, "SEND_USER_NAME");
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.start_date = FRMQueryString.requestString(request, "start_date");
        this.end_date = FRMQueryString.requestString(request, "end_date");
        this.petugas_delivery = FRMQueryString.requestString(request, "petugas_delivery");
        this.userStatus = FRMQueryString.requestString(request, "STATUS_USER");

        this.sessLogin = false;

        AppUser au = new AppUser();
        iCommand = FRMQueryString.requestCommand(request);

        try {
            au = PstAppUser.fetch(this.userId);
        } catch (Exception e) {
        }
        String where = PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = 'user_create_document_location' " + " AND " + PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID] + " = " + this.userId;
        this.user = PstDataCustom.list(0, 0, where, "");
        for (int i = 0; i < this.user.size(); i++) {
            dc = (DataCustom) this.user.get(i);
        }

        //CHECK USER LOGIN SESSION
        HttpSession session = request.getSession();
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
        if (userSession != null) {
            if (userSession.isLoggedIn()) {
                this.sessLogin = true;
            }
        }

        if (this.sessLogin == true) {
            switch (this.iCommand) {
                case Command.SAVE:
                    commandSave(request, response);
                    break;

                case Command.DELETE:
                    //commandDelete(request);
                    break;

                case Command.LIST:
                    commandList(request, response);
                    break;

                default:
                    commandNone(request);
                    break;
            }
        } else {
            this.iErrCode = 1;
            this.message = "Sesi login Anda telah berakhir. Silakan login ulang untuk melanjutkan.";
        }

        try {

            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("RETURN_PRINT_VALUE", this.jsonArrayPrintValue);
            this.jSONObject.put("RETURN_DATA_ARRAY", this.jSONArray);
            this.jSONObject.put("RETURN_SESSION_LOGIN", this.sessLogin);
            this.jSONObject.put("RETURN_ERROR_CODE", "" + this.iErrCode);
            this.jSONObject.put("RETURN_MESSAGE", "" + this.message);

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("getPrintKwitansi")) {
            getPrintKwitansi(request);
        }
    }

    public void getPrintKwitansi(HttpServletRequest request) {
        String data = "";
        try {
            String url = this.apiUrl + "/document/document/kwitansi-pembayaran/" + this.oid;
            JSONObject res = WebServices.getAPI("", url);
            data = res.getString("DATA");
            System.out.println("URL Document : " + url);
        } catch (Exception e) {
            data = "";
        }
        this.htmlReturn = data;
    }

//    METHOD COMMAND SAVE ======================================================================================================>
    public void commandSave(HttpServletRequest request, HttpServletResponse response) {
//        if (this.dataFor.equals("assignPetugasDelivery")) {
//            assignPetugas(request);
//        }
    }

//    METHOD COMMAND LIST ======================================================================================================>
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listUserAll")) {
            String[] cols = {
                PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID],
                PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID],
                PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME],
                PstLocation.fieldNames[PstLocation.FLD_NAME],
                PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS],
                PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "sSearch");
        int amount = 10;
        int start = 0;
        int col = 0;
        String addSql = "";
        String dir = "asc";
        String apiUrl = PstSystemProperty.getValueByName("SEDANA_URL");
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

        try {
        } catch (Exception exc) {

        }

        String whereClause = "";
        if (dataFor.equals("listUserAll")) {
            whereClause += "("
                    + " ap." + PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID] + " LIKE '%" + searchTerm + "%'"
                    + " OR ap." + PstAppUser.fieldNames[PstAppUser.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
                    + " OR ap." + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] + " LIKE '%" + searchTerm + "%'"
                    + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                    + ")";
            
            if (this.userStatus.equals("on")) {
                whereClause += " AND ap." + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] + " <> '3'";
            }
            if (this.userStatus.equals("off")) {
                whereClause += " AND ap." + PstAppUser.fieldNames[PstAppUser.FLD_USER_STATUS] + " = '" + AppUser.STATUS_NON_AKTIF + "'";
            }
        } 

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listUserAll")) {
            whereClause += " AND dc." + PstDataCustom.fieldNames[PstDataCustom.FLD_DATA_NAME] + " = 'user_assign_single_location'";
            total = PstAppUser.countUserManagement(whereClause);
        } 

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor, addSql, whereClause);
        } catch (Exception ex) {
            printErrorMessage(ex.getMessage());
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor, String addSql, String whereClause) {
        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        String order = "";

        boolean isLoggedIn = false;
        int userGroupNewStatus = -1;
        String userName = "";
        long userId = 0;
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60 * 2);
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
        try {
            if (userSession == null) {
                userSession = new SessUserSession();
            } else {
                if (userSession.isLoggedIn() == true) {
                    isLoggedIn = true;
                    AppUser appUser = userSession.getAppUser();
                    userGroupNewStatus = appUser.getUserGroupNew();
                    userName = appUser.getLoginId();
                    userId = appUser.getOID();
                }
            }
        } catch (Exception exc) {
            //System.out.println(" >>> Exception during check login");
        }

        if (this.colOrder >= 0) {
                order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        JSONArray jArr = new JSONArray();
        if (datafor.equals("listUserAll")) {
            listData = PstAppUser.listUserManagement(start, amount, whereClause, order);
        } 
        
        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listUserAll")) {
                Vector data = (Vector) listData.get(i);
                AppUser ap = (AppUser) data.get(0);
                Location loc = (Location) data.get(1);

                ja.put("<div class='text-center'>" + (this.start + i + 1) + ".</div>");
                ja.put("" + ap.getLoginId() + "");
                ja.put("" + ap.getFullName()+ "");
                ja.put("<div class='text-center'>" + loc.getName()+ "</div>");
                ja.put("<div class='text-center'>" + ap.getStatusTxt(ap.getUserStatus())+ "</div>");
                String button = "<div class='text-center'>"
                        + "<button type='button' title='Detail' "
                        + "class='btn btn-xs btn-warning detail-user-btn' "
                        + "data-oid='"+ap.getOID()+"'> "
                        + "<i class='fa fa-pencil'></i>"
                        + "</button>";
                ja.put(button);
                array.put(ja);
            } 
        }
        totalAfterFilter = total;
        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
        } catch (Exception e) {
            printErrorMessage(e.getMessage());
        }
        return result;
    }

    public static int checkString(String strObject, String toCheck) {
        if (toCheck == null || strObject == null) {
            return -1;
        }
        if (strObject.startsWith("=")) {
            strObject = strObject.substring(1);
        }

        String[] parts = strObject.split(" ");
        if (parts.length > 0) {
            for (int i = 0; i < parts.length; i++) {
                String p = parts[i];
                if (toCheck.trim().equalsIgnoreCase(p.trim())) {
                    return i;
                };
            }
        }
        return -1;
    }

    public static double getValue(String formula) {
        DBResultSet dbrs = null;
        double compValueX = 0;
        try {
            String sql = "SELECT (" + formula + ")";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                compValueX = rs.getDouble(1);
            }

            rs.close();
            return compValueX;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public int convertInteger(int scale, double val) {
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.HALF_UP);
        return bDecimal.intValue();
    }

    public void printErrorMessage(String errorMessage) {
        System.out.println("");
        System.out.println("========================================>>> WARNING <<<========================================");
        System.out.println("");
        System.out.println("MESSAGE : " + errorMessage);
        System.out.println("");
        System.out.println("========================================<<< * * * * >>>========================================");
        System.out.println("");
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








