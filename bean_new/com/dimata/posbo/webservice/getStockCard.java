/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.session.warehouse.SessMatCostingStokFisik;
import com.dimata.qdep.form.FRMQueryString;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
public class getStockCard extends HttpServlet {

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
    private long oidLocation = 0;

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
       this.oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION");

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
        //String id = FRMQueryString.requestString(request, "LOGIN_ID");
        //String password = FRMQueryString.requestString(request, "PASSWORD");
        try {
            jSONArray = new JSONArray();

            Location loc = new Location();
            try {
                loc = PstLocation.fetchExc(oidLocation);
            } catch (Exception exc){}
            
            Vector listItem = PstMaterial.listShopingCartUseLocation(0,0,"", "", ""+loc.getStandarRateId(), ""+loc.getPriceTypeId(), 0, oidLocation);
            if (listItem.size() > 0) {
                for (int i = 0; i < listItem.size(); i++) {
                    Vector temp = (Vector) listItem.get(i);
                    if(temp.size() != 0){
                        Material material = (Material) temp.get(0);
                        PriceTypeMapping priceTypeMapping = (PriceTypeMapping)temp.get(1);
                        try {
                    
                        long matStockId = PstMaterialStock.fetchByMaterialLocation(Long.valueOf(material.getMaterialId()), oidLocation);
						double qtyStockStockCard = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(Long.valueOf(material.getMaterialId()), oidLocation, new Date(), 0);

                        JSONObject jSONObjectArray = new JSONObject();
                        //jSONObjectArray.put(PstLocation.fieldNames[PstLocation.FLD_NAME], location.getName());
						MaterialStock materialStock = PstMaterialStock.fetchExc(matStockId);
                        jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_STOCK_ID], materialStock.getOID());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID], materialStock.getPeriodeId());
                        jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID], materialStock.getMaterialUnitId());
                        jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID], materialStock.getLocationId());
                        jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY], materialStock.getQty());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MIN], materialStock.getQtyMin());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_MAX], materialStock.getQtyMax());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_QTY], materialStock.getOpeningQty());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_QTY], materialStock.getClosingQty());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_IN], materialStock.getQtyIn());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY_OUT], materialStock.getQtyOut());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_QT_OPTIMUM], materialStock.getQtyOptimum());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT], materialStock.getBerat());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_IN], materialStock.getBeratIn());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_BERAT_OUT], materialStock.getBeratOut());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_OPENING_BERAT], materialStock.getOpeningBerat());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_CLOSING_BERAT], materialStock.getClosingBerat());
						jSONObjectArray.put(PstMaterialStock.fieldNames[PstMaterialStock.FLD_UPDATE_DATE], materialStock.getUpdateDate());
                        jSONArray.put(jSONObjectArray);
                        } catch (Exception exc){
                            
                        }
                    }
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
        //processRequest(request, response);
    }
}
