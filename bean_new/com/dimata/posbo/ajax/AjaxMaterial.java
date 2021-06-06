/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.db.DBResultSet;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.Merk;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMatMappLocation;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMerk;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class AjaxMaterial extends HttpServlet {

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
    private int supplier = 0;
    private int lokasi = 0;
    
    //ADDITIONAL
    private String form_nomor = "";
    private String form_name = "";
    private String form_category = "";
    private String form_supplier = "";
    private String form_barcode = "";
    private String form_location_stock = "";
    private String start_date = "";
    private String end_date = "";
    private String order_by = "";
    private List<String> form_location_sell = new ArrayList<>();
    

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
        this.lokasi = 0;

        //DOUBLE
        
        //ADDITIONAL FOR FILTER LIST REQUEST PURCHASE ORDER
        this.form_nomor = FRMQueryString.requestString(request, "form_nomor");
        this.form_name = FRMQueryString.requestString(request, "form_nama");
        this.form_category = FRMQueryString.requestString(request, "form_category");
        this.form_barcode = FRMQueryString.requestString(request, "form_barcode");
        this.form_supplier = FRMQueryString.requestString(request, "form_supplier");
        this.order_by = FRMQueryString.requestString(request, "order_by");
        this.start_date = FRMQueryString.requestString(request, "start_date");
        this.end_date = FRMQueryString.requestString(request, "end_date");
        this.form_location_stock = FRMQueryString.requestString(request, "form_location");
        String[] strList = request.getParameterValues("form_location_sell");
        if(strList != null){
                this.form_location_sell = Arrays.asList(strList);
        }
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
    
    //COMMAND NONE============================================================
    public synchronized void commandNone(HttpServletRequest request) {
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
        if (this.dataFor.equals("listMaterial")) {
                String[] cols = {
                    PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                    PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                    PstMaterial.fieldNames[PstMaterial.FLD_BARCODE],
                    PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                    PstMerk.fieldNames[PstMerk.FLD_NAME],
                    PstCategory.fieldNames[PstCategory.FLD_NAME],
                    PstUnit.fieldNames[PstUnit.FLD_NAME],
                    PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE],
                    PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE],
                    PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST],
                    PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST]

                };
                jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listMaterialCatalog")) {
            String[] cols = {
                    PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                    PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                    PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                    PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]
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
        this.supplier = 0;
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
        if (dataFor.equals("listMaterial")) {
            whereClause += "("
                        + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_CURR_BUY_PRICE] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_DEFAULT_COST] + " LIKE '%" + searchTerm + "%'"
                        + " OR MERK." + PstMerk.fieldNames[PstMerk.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR UNIT." + PstUnit.fieldNames[PstUnit.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            if (this.form_nomor != null && this.form_nomor.length() > 0) {
                whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + this.form_nomor + "%'";
            }  
            if (this.form_barcode != null && this.form_barcode.length() > 0) {
                whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + this.form_barcode + "%'";
            } 
            if (this.form_name != null && this.form_name.length() > 0) {
                whereClause += " AND MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + this.form_name + "%'";
            } 
            if (!this.form_supplier.equals("0") && this.form_supplier != null && this.form_supplier.length() > 0) {
                this.supplier = 1;
                whereClause += " AND CON." + PstContactList.fieldNames[PstContactList.FLD_CONTACT_ID] + " LIKE '%" + this.form_supplier + "%'";
            } 
            if (!this.form_category.equals("0") && this.form_category != null && this.form_category.length() > 0) {
                whereClause += " AND CAT." + PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID] + " LIKE '%" + this.form_category + "%'";
            } 

            if ((this.start_date != null && this.start_date.length() > 0) && (this.end_date != null && this.end_date.length() > 0)) {
                whereClause += " AND ("
                        + "(TO_DAYS( MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + ") "
                        + ">= TO_DAYS('" + this.start_date + "')) AND "
                        + "(TO_DAYS( MAT." + PstMaterial.fieldNames[PstMaterial.FLD_UPDATE_DATE] + ") "
                        + "<= TO_DAYS('" + this.end_date + "'))"
                        + ")";
            }
            if (this.form_location_sell != null && this.form_location_sell.size() > 0) {
                this.lokasi = 1;
                whereClause += " AND LOC."+PstMatMappLocation.fieldNames[PstMatMappLocation.FLD_LOCATION_ID]+ " IN(";
                for (int i = 0; i < this.form_location_sell.size(); i++) {
                    if (i > 0) {
                        whereClause += ", ";
                    }
                    whereClause += this.form_location_sell.get(i);
                }
                whereClause += ")";
            }
        } else {
            whereClause += "("
                        + " MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR MAT." + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR CAT." + PstCategory.fieldNames[PstCategory.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + ")";
        }

        String colName = cols[col];
        int total = -1;

        //cek parameter daftar transaksi
        String addSql = "";
        if (dataFor.equals("listMaterial")) {
            total = SessMaterial.getCountMaterial(whereClause, this.lokasi, this.supplier);
        } else {
            total = PstMaterial.getCountList(whereClause);
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
          
        if (dataFor.equals("listMaterial")) {
                if (this.order_by != null) {
                    if(this.order_by.equals("-1")){
                        order = "CAT."+PstCategory.fieldNames[PstCategory.FLD_NAME]+ ", MERK."+PstMerk.fieldNames[PstMerk.FLD_NAME]+ ", MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
                    }else if(this.order_by.equals("0")){
                        order = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU]+ " ASC";
                    }else  if(this.order_by.equals("1")){
                        order = "CAT."+PstCategory.fieldNames[PstCategory.FLD_NAME]+ " ASC";
                    }else  if(this.order_by.equals("2")){
                        order = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME]+ " ASC";
                    }
                }else{
                        order += "" + colName + " " + dir + "";
                }
            }else{
                order += "" + colName + " " + dir + "";
            }

        Vector listData = new Vector(1, 1);
        if (dataFor.equals("listMaterial")) {
            listData = SessMaterial.listMaterial(start,amount,whereClause,order, this.lokasi, this.supplier);
        } else {
            listData = PstMaterial.listJoin(start, amount, whereClause, "");
        }
        String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA");
        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("listMaterial")) {
                Vector data = (Vector) listData.get(i);
                Material mat = (Material) data.get(0);
                Unit unit = (Unit) data.get(1);
                Category cat = (Category) data.get(2);
                Merk merk = (Merk) data.get(3);
                
                String formulaCash = PstSystemProperty.getValueByName("CASH_FORMULA");
                double hargaJual = mat.getDefaultCost();
                if(useForRaditya.equals("1")){
                    if (checkString(formulaCash, "HPP") > -1) {
                      formulaCash = formulaCash.replaceAll("HPP", "" + mat.getAveragePrice()); 
                    }
                    hargaJual = getValue(formulaCash);
                    hargaJual = rounding(-3, hargaJual);
                }
                ja.put("<div class='text-center'>" + (this.start + i + 1) + ".</div>");
                ja.put("" + mat.getSku());
                ja.put("" + mat.getBarCode());
                ja.put("" + mat.getName());
                ja.put("" + merk.getName());
                ja.put("<div class='text-center'>" + cat.getName()+"</div>");
                ja.put("<div class='text-center'>" + unit.getName()+"</div>");
                ja.put("<div class='text-center'>" + String.format("%,.2f",mat.getAveragePrice())+"</div>");
                ja.put("<div class='text-center'>" + String.format("%,.2f",mat.getCurrBuyPrice())+"</div>");
                ja.put("<div class='text-center'>" + String.format("%,.2f",hargaJual)+"</div>");
                ja.put("<div class='text-center'><button type='button' class='btn btn-primary btn-xs edit-material' data-oid='"+mat.getOID()+"'><i class='fa fa-pencil'> </i></button></div>");
                array.put(ja);
            }  else if (this.dataFor.equals("listMaterialCatalog")){
                Vector data = (Vector) listData.get(i);
                Material mat = (Material) data.get(0);
                Category cat = (Category) data.get(1);
                
                ja.put("<div class='text-center'>" + (this.start + i + 1) + ".</div>");
                ja.put("" + mat.getName());
                ja.put("" + mat.getSku());
                ja.put("" + cat.getName());
                ja.put("<button data-oid='"+mat.getOID()+"' data-oid_category='"+mat.getCategoryId()+"' class='btn btn-warning choose'>Pilih</button>");
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
    
    public static double rounding(int scale, double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(scale, RoundingMode.UP);
        return bDecimal.doubleValue();
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





