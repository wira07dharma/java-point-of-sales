/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.ajax.employee;

import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.PstReligion;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.services.WebServices;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * @author Regen
 */
public class AjaxPegawaiOfficer extends HttpServlet {

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
    private long dataOid = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String message = "";
    private String history = "";
   
    //BOOLEAN
    private boolean privAdd = false;
    private boolean privUpdate = false;
    private boolean privDelete = false;
    private boolean privView = false;
    private boolean sessLogin = false;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int sessLanguage = 0;
    

    //DOUBLE

    //DATE

    private long userId = 0;
    private String userName = "";
    AppUser au = new AppUser();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // OBJECT
        this.jSONArray = new JSONArray();
        this.jSONObject = new JSONObject();

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.dataOid = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";
        this.message = "";
        this.history = "";

        //BOOLEAN
        this.privAdd = FRMQueryString.requestBoolean(request, "privadd");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privView = FRMQueryString.requestBoolean(request, "privview");
        this.sessLogin = true;

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        this.sessLanguage = FRMQueryString.requestInt(request, "SESS_LANGUAGE");

        //DOUBLE

        //OBJECT
        this.jSONObject = new JSONObject();

        this.userId = FRMQueryString.requestLong(request, "SEND_USER_ID");
        this.userName = FRMQueryString.requestString(request, "SEND_USER_NAME");
        try {
            au = PstAppUser.fetch(this.userId);
        } catch (Exception e) {
        }
        //CHECK USER LOGIN SESSION
//        HttpSession session = request.getSession();
//        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
//        if (userSession != null) {
//            if (userSession.isLoggedIn()) {
//                this.sessLogin = true;
//            }
//        }

        if (this.sessLogin == true) {
            switch (this.iCommand) {
                case Command.SAVE:
                    commandSave(request);
                    break;

                case Command.DELETE:
                    commandDelete(request);
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
            this.jSONObject.put("RETURN_DATA_ARRAY", this.jSONArray);
            this.jSONObject.put("RETURN_SESSION_LOGIN", this.sessLogin);
            this.jSONObject.put("RETURN_ERROR_CODE", "" + this.iErrCode);
            this.jSONObject.put("RETURN_MESSAGE", "" + this.message);
            this.jSONObject.put("RETURN_DATA_OID", "" + this.dataOid);

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    //COMMAND SAVE==============================================================
    public synchronized void commandSave(HttpServletRequest request) {
//        if (this.dataFor.equals("saveCompany")) {
//            saveCompany(request);
//        } 
    }

    
    public synchronized void saveNegara(HttpServletRequest request) {
        try {


        } catch (Exception e) {
            printErrorMessage(e.getMessage());
            this.message += e.getMessage();
            this.iErrCode += 1;
        }
    }
    
    
    //COMMAND DELETE============================================================
    public synchronized void commandDelete(HttpServletRequest request) {
//        if (this.dataFor.equals("deleteCompany")) {
//            deleteCompany(request, this.oid);
//        }
    }


    public synchronized void deleteKota(HttpServletRequest request, long oid) {
        try {

        } catch (Exception e) {
            printErrorMessage(e.getMessage());
            this.message += e.getMessage();
            this.iErrCode += 1;
        }
    }
    


    //COMMAND LIST==============================================================
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listPegawai")) {
                String[] cols = {
                    PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME],
                    PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME],
                    PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS],
                    PstEmployee.fieldNames[PstEmployee.FLD_PHONE],
                    PstEmployee.fieldNames[PstEmployee.FLD_SEX],
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]

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
        if (dataFor.equals("listPegawai")) {
            whereClause += "("
            + " " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + searchTerm + "%'"
            + " OR " + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS] + " LIKE '%" + searchTerm + "%'"
            + " OR " + PstEmployee.fieldNames[PstEmployee.FLD_PHONE] + " LIKE '%" + searchTerm + "%'"
            + " OR " + PstEmployee.fieldNames[PstEmployee.FLD_SEX] + " LIKE '%" + searchTerm + "%'"
            + " OR " + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE] + " LIKE '%" + searchTerm + "%'"
            + ")"
            + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = '0'"
            + "";
        } 

        String colName = cols[col];
        int total = -1;

        //cek parameter daftar transaksi
        String addSql = "";
        if (dataFor.equals("listPegawai")) {
        String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL"); 
            JSONArray array = new JSONArray();
            JSONObject tempObj = new JSONObject();
            try {
                String tempUrl = hrApiUrl + "/employee/employee-count/";
                String param = "whereClause=" + WebServices.encodeUrl(whereClause);
                tempObj = WebServices.getAPIWithParam("", tempUrl, param);
                int err = tempObj.optInt("ERROR", 0);
                total = tempObj.getInt("COUNT");
                
              } catch (Exception e) {
              }

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
        JSONArray arrayList = new JSONArray();
        
        String order = "";
          

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        JSONArray listPegawai = new JSONArray();
        if (dataFor.equals("listPegawai")) {
            String hrApiUrl = PstSystemProperty.getValueByName("HARISMA_URL"); 
            JSONObject tempObj = new JSONObject();
            try {
                String tempUrl = hrApiUrl + "/employee/employee-user";
                String param = "limitStart=" + WebServices.encodeUrl(""+start) + "&recordToGet=" + WebServices.encodeUrl(""+amount)
                       + "&whereClause=" + WebServices.encodeUrl(whereClause) + "&order=" + WebServices.encodeUrl(""+order);
                  tempObj = WebServices.getAPIWithParam("", tempUrl, param);
                int err = tempObj.optInt("ERROR", 0);

                if (err == 0) {
                    arrayList = tempObj.getJSONArray("DATA");
                }
                listPegawai = arrayList;
              } catch (Exception e) {
              }
        } 
        if (datafor.equals("listPegawai")) {
            for (int i = 0; i < listPegawai.length(); i++) {
            JSONObject empObj = listPegawai.optJSONObject(i);
            JSONArray ja = new JSONArray();
            String gender = "";
            String sex = ""+empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_SEX], "-");
            if(sex.equals("0")){
                gender = "Laki - Laki";
            }else{
                gender = "Perempuan";
            }
            ja.put("<div class='text-center'>" + (this.start + i + 1) + ".</div>");
            ja.put("" + empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], "-"));
            ja.put("" + empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], "-"));
            ja.put("<div class='text-center'>" + empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], "-")+"</div>");
            ja.put("<div class='text-center'>" + gender+"</div>");
            ja.put("" + empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], "-"));
            ja.put("<div class='text-center'><button type='button' class='btn btn-primary employeeSelect' "
                    + "data-nama='"+empObj.optString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME])+"' "
                    + "data-oid='"+empObj.optLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID])+"'"
                    + ">Pilih</button></div>");
            array.put(ja);
            }
        } else {
        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
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

    //COMMAND NONE==============================================================
    public synchronized void commandNone(HttpServletRequest request) {
//        if (this.dataFor.equals("getDataJenisKredit")) {
//            getDataJenisKredit(request);
//        } 
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

//    public void getPrintOutTransaksiPembayaran(HttpServletRequest request) {
//        getPrintOutAngsuranKredit(request, this.oid);
//    }

    public int convertInteger(int scale, double val) {
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.UP);
        return bDecimal.intValue();
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





