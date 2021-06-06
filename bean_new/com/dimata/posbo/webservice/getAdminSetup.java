/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.Date;
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
 * @author dimata005
 */
public class getAdminSetup extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //STRING
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
    private String searchTerm = "";
    private int amount = 0;
    private String colName = "";
    private String dir = "";
    private int start = 0;
    private int colOrder = 0;
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    private int language = 0; 
    
    public static final String textListOrderHeader[][] = {
        {"No","Lokasi","Nama Item","Jml","Unit Stok","Jml Sekarang","Kode","Jumlah Request","Jumlah Kirim"},
        {"No","Location","Item Name","Qty","Stock Unit","Curent Qty","Code","Request Qty","Send Request"}
    };
    
    public static final String textListButton[][] = {
        {"Simpan","Hapus","Ubah","Selanjutnya","Kembali","Pencarian"},
        {"Save","Delete","Update","Next","Back","Search"}
    };
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.iCommand = FRMQueryString.requestCommand(request); 
        try {
            this.language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        } catch (Exception e) {
        }
        
        commandList(request);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONArray);
    }
    
    public void commandNone(HttpServletRequest request,HttpServletResponse response){
	
    }
    
    public void commandSave(HttpServletRequest request,HttpServletResponse response){
        
    }
    
    public void commandList(HttpServletRequest request){
            getListPromotion(request);
    }
    
    public void getListPromotion(HttpServletRequest request){
        //WHERE CLAUSE
        String whereClause = "";
        String id = FRMQueryString.requestString(request, "LOGIN_ID");
        String password = FRMQueryString.requestString(request, "PASSWORD");
        try {
            jSONArray = new JSONArray();
            whereClause = PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+id+"' AND "+PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD]+"='"+password+"' AND "+PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW]+"='4'";
            Vector listData = PstAppUser.listPartObj(0, 0, whereClause, "");
            if(listData.size() > 0){
                for(int i = 0; i < listData.size(); i++){
                    AppUser marketingNewsInfo = (AppUser) listData.get(i);
                    JSONObject jSONObjectArray = new JSONObject();
                    jSONObjectArray.put("USER_GROUP",marketingNewsInfo.getUserGroupNew());
                    jSONObjectArray.put("USER_ID",marketingNewsInfo.getOID());
                    jSONObjectArray.put("LOGIN_ID",marketingNewsInfo.getLoginId());
                    jSONObjectArray.put("PASSWORD",marketingNewsInfo.getPassword());
                    jSONObjectArray.put("FULL_NAME",marketingNewsInfo.getFullName());
                    jSONObjectArray.put("EMAIL",marketingNewsInfo.getEmail());
                    jSONObjectArray.put("DESCRIPTION",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("REG_DATE",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("UPDATE_DATE",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("USER_STATUS",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("LAST_LOGIN_DATE",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("LAST_LOGIN_IP",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("EMPLOYEE_ID",marketingNewsInfo.getEmployeeId());
                    jSONObjectArray.put("START_TIME",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("END_TIME",marketingNewsInfo.getDescription());
                    jSONObjectArray.put("FINGER_DATA",marketingNewsInfo.getDescription());
                    jSONArray.put(jSONObjectArray);
                }
            }
        } catch (Exception e) {
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        //System.out.print("Can't Use");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
}
