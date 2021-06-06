/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice.masterdata;

import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import java.io.BufferedReader;
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
 * @author Dimata IT Solutions
 */
public class MaterialService extends HttpServlet {

    private JSONObject jSONObject = new JSONObject();
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String jsonString = buffer.toString();

        JSONObject jsonData;
        
        try {
            jsonData = new JSONObject(jsonString);
            String keyword = jsonData.getString("keyword");
            String oids = jsonData.optString("oids", "");
            int limitStart = jsonData.getInt("limitStart");
            int recordToGet = jsonData.getInt("recordToGet");
            String order = jsonData.optString("order", "");
            long priceTypeId= jsonData.optLong("priceTypeId", 0);
            long standardRateId = jsonData.optLong("standardRateId", 0);
            
            if(oids.length()>0){
                
            } else {
                String whereClause = PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + keyword + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " LIKE '%" + keyword + "%'"
                    + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + keyword + "%'";
                Vector listData = PstMaterial.list(limitStart, recordToGet, whereClause, order);
                int count = PstMaterial.getCount(whereClause);
                JSONArray jSONArray = new JSONArray();
                if (listData.size()>0){
                     for (int i = 0; i < listData.size(); i++) {
                         Material material = (Material) listData.get(i);
                         JSONObject objData = PstMaterial.fetchJSON(material.getOID());
                         JSONObject objPrice = PstPriceTypeMapping.fetchJSON(priceTypeId, material.getOID(), standardRateId);
                         objData.put(PstPriceTypeMapping.TBL_POS_PRICE_TYPE_MAPPING, objPrice);
                         jSONArray.put(objData);
                     }
                     jSONObject.put("COUNT", count);
                }
                jSONObject.put("DATA", jSONArray);
            }
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(this.jSONObject);
        } catch (JSONException err) {

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
