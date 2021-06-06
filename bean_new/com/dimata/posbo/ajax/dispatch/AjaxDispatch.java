/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax.dispatch;

import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.session.admin.SessUserSession;
import com.dimata.qdep.form.FRMQueryString;
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
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Regen
 */
public class AjaxDispatch extends HttpServlet {

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
    private String history = "";
    private String statusDoc = "";
    private String useForRaditya = "";
    private String typeOfCredit = "";
    private String posApiUrl = "";
    private String raditya = "";

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
    private String tglTempo = "";

    private long userId = 0;
    private String userName = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // OBJECT
        this.jSONArray = new JSONArray();
        this.jSONObject = new JSONObject();

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");


        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.posApiUrl = PstSystemProperty.getValueByName("POS_API_URL");

        //BOOLEAN
        this.privAdd = FRMQueryString.requestBoolean(request, "privadd");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privView = FRMQueryString.requestBoolean(request, "privview");
        this.sessLogin = false;

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        this.raditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA");

        //DOUBLE

        //OBJECT
        this.jSONObject = new JSONObject();

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
            this.jSONObject.put("RETURN_ERROR_CODE", "" + this.iErrCode);
            this.jSONObject.put("RETURN_MESSAGE", "" + this.message);

        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

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
    //COMMAND SAVE==============================================================
    public synchronized void commandSave(HttpServletRequest request) {
       
    }

    //COMMAND DELETE==============================================================
    public synchronized void commandDelete(HttpServletRequest request) {
       
    }

    //COMMAND NONE==============================================================
    public synchronized void commandNone(HttpServletRequest request) {
       
    }


    //COMMAND LIST==============================================================
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listDispatch")) {
            String[] cols = {                
                "dispatch." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE],
                "dispatch." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE],
                "location." + PstLocation.fieldNames[PstLocation.FLD_NAME],
                "location." + PstLocation.fieldNames[PstLocation.FLD_NAME]
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
        if (dataFor.equals("listDispatch")) {
             whereClause += "";   
            } 
        

        String colName = cols[col];
        int total = -1;

        //cek parameter daftar transaksi
        String addSql = "";

        if (dataFor.equals("listDispatch")) {
				total = 0;
        }

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor, addSql);
        } catch (Exception ex) {
            printErrorMessage(ex.getMessage());
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor, String addSql) {
        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();

        String whereClause = "";
        String order = "";

        if (datafor.equals("listDispatch")) {
            if (typeOfCredit.equals("1")){
            whereClause += "";    
            } 

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

          Vector listData = new Vector(1, 1);
        if (datafor.equals("listDispatch")) {
                listData = new Vector(1, 1);
           
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listDispatch")) {
                try {
                } catch (Exception e) {
                    printErrorMessage(e.getMessage());
                    message = e.toString();
                }
                ja.put("" + (this.start + i + 1) + ".");
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
        }
        return result;
    }

    public int convertInteger(int scale, double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.HALF_UP);
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
