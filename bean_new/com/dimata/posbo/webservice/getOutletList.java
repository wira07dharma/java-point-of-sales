/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.admin.AppUser;
import com.dimata.posbo.entity.admin.PstAppUser;
import com.dimata.posbo.webservice.inquery.InqOutletListApi;
import com.dimata.qdep.form.FRMQueryString;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author dimata005
 */
public class getOutletList extends HttpServlet {
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
        //String id = FRMQueryString.requestString(request, "LOGIN_ID");
        //String password = FRMQueryString.requestString(request, "PASSWORD");
        try {
            jSONArray = new JSONArray();
           
            //whereClause = PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+id+"' AND "+PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD]+"='"+password+"'";
            Vector listData = InqOutletListApi.list(0,0, "","");
            if(listData.size() > 0){
                for(int i = 0; i < listData.size(); i++){
                Location location = (Location) listData.get(i);
                JSONObject jSONObjectArray = new JSONObject();
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID], location.getOID());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_NAME], location.getName());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_CONTACT_ID], location.getContactId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_DESCRIPTION], location.getDescription());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_CODE], location.getCode());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_ADDRESS], location.getAddress());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TELEPHONE], location.getTelephone());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_FAX], location.getFax());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_PERSON], location.getPerson());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_EMAIL], location.getEmail());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TYPE], location.getType());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_PARENT_LOCATION_ID], location.getParentLocationId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_WEBSITE], location.getWebsite());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_LOC_INDEX], location.getLocIndex());

                // ini tambahan prochain add opie 13-06-2012
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_PERCENT], location.getServicePersen());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TAX_PERCENT], location.getTaxPersen());

                // ini hanya untuk di gunakan oleh hanoman
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID], location.getDepartmentId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_USED_VAL], location.getTypeBase());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL], location.getServiceValue());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE], location.getTaxValue());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_SERVICE_VAL_USD], location.getServiceValueUsd());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TAX_VALUE_USD], location.getTaxValueUsd());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_REPORT_GROUP], location.getReportGroup());

                //ini untuk prohchain add opie 13-06-2012
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_TAX_SVC_DEFAULT], location.getTaxSvcDefault());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_PERSENTASE_DISTRIBUTION_PURCHASE_ORDER], location.getPersentaseDistributionPurchaseOrder());
                  //add fitra 29-01-2014
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID], location.getCompanyId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_PRICE_TYPE_ID], location.getPriceTypeId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_STANDART_RATE_ID], location.getStandarRateId());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_USED_TABLE], location.getUseTable());
                
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_ACCOUNTING_EMAIL],location.getAcountingEmail());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_LOCATION_IP], location.getLocationIp());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_SISTEM_ADDRESS_HISTORY_OUTLET], location.getSistemAddressHistoryOutlet());
                
                // added by dewok++ 2017-03-21
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_OPENING_TIME], location.getOpeningTime());
                jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_CLOSING_TIME], location.getClosingTime());
                jSONArray.put(jSONObjectArray);
                }
            }
        } catch (Exception e) {
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       // processRequest(request, response);
    }
}
