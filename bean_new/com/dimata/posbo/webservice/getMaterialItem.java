/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.webservice.inquery.InqueryMaterialItem;
import com.dimata.posbo.webservice.inquery.MaterialForApi;
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
public class getMaterialItem extends HttpServlet {

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
    private String oidDelete = "";
    private String approot = "";
    private String address = "";
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
        {"No", "Lokasi", "Nama Item", "Jml", "Unit Stok", "Jml Sekarang", "Kode", "Jumlah Request", "Jumlah Kirim"},
        {"No", "Location", "Item Name", "Qty", "Stock Unit", "Curent Qty", "Code", "Request Qty", "Send Request"}
    };

    public static final String textListButton[][] = {
        {"Simpan", "Hapus", "Ubah", "Selanjutnya", "Kembali", "Pencarian"},
        {"Save", "Delete", "Update", "Next", "Back", "Search"}
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

    public void commandNone(HttpServletRequest request, HttpServletResponse response) {

    }

    public void commandSave(HttpServletRequest request, HttpServletResponse response) {

    }

    public void commandList(HttpServletRequest request) {
        getListPromotion(request);
    }

    public void getListPromotion(HttpServletRequest request) {
        //WHERE CLAUSE
        String whereClause = "";
        String locationId = FRMQueryString.requestString(request, "LOCATION_ID");
        String priceTypeId = FRMQueryString.requestString(request, "PRICE_TYPE_ID");
        String standartRateId = FRMQueryString.requestString(request, "STANDART_RATE_ID");
        
        boolean checkOk = true;
//        if(locationId.equals("")){
//            checkOk = false;
//        }
//        
//        if(standartRateId.equals("")){
//            checkOk = false;
//        }
//        
//        if(priceTypeId.equals("")){
//            checkOk = false;
//        }
        
        if(checkOk){
            try {
                jSONArray = new JSONArray();
                whereClause = " pl.LOCATION_ID='"+locationId+"' AND pp.PRICE_TYPE_ID='"+priceTypeId+"' " +
                              " AND pp.STANDART_RATE_ID='"+standartRateId+"'";
                Vector listData = InqueryMaterialItem.listMaterialApi(0, 0,"", "");
                if (listData.size() > 0) {
                    for (int i = 0; i < listData.size(); i++) {
                        MaterialForApi materialForApi = (MaterialForApi) listData.get(i);
                        JSONObject jSONObjectArray = new JSONObject();
                        jSONObjectArray.put("LOCATION_ID", materialForApi.getLocationId());
                        jSONObjectArray.put("MATERIAL_ID", materialForApi.getMaterialId());
                        jSONObjectArray.put("NAME", materialForApi.getName());
                        jSONObjectArray.put("UNIT_ID", materialForApi.getUnitId());
                        jSONObjectArray.put("PRICE", materialForApi.getPrice());
                        jSONObjectArray.put("PRICE_TYPE_ID", materialForApi.getPriceTypeId());
                        jSONObjectArray.put("STANDART_RATE_ID", materialForApi.getStandartRateId());
                        jSONArray.put(jSONObjectArray);
                    }
                }
            } catch (Exception e) {
            }
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
        //processRequest(request, response);
    }
}
