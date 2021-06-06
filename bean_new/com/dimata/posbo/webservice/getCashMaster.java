/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.webservice;


import com.dimata.common.entity.loationsetting.LocationSetting;
import com.dimata.common.entity.loationsetting.PstLocationSetting;
import com.dimata.pos.entity.masterCashier.CashMaster;
import com.dimata.pos.entity.masterCashier.PstCashMaster;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_CABANG;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_CASHIER_NUMBER;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_LOCATION_ID;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_PRICE_TYPE;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_SERVICE;
import static com.dimata.pos.entity.masterCashier.PstCashMaster.FLD_TAX;
import com.dimata.posbo.db.DBHandler;
import com.dimata.posbo.entity.masterdata.PstShift;
import com.dimata.posbo.entity.masterdata.Shift;
import com.dimata.qdep.form.FRMQueryString;
import java.io.IOException;
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
public class getCashMaster extends HttpServlet {

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
        //String id = FRMQueryString.requestString(request, "LOGIN_ID");
        //String password = FRMQueryString.requestString(request, "PASSWORD");
        try {
            jSONArray = new JSONArray();

            //whereClause = PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]+"='"+id+"' AND "+PstAppUser.fieldNames[PstAppUser.FLD_PASSWORD]+"='"+password+"'";
            Vector listData = PstCashMaster.list(0, 0, "", "");
            if (listData.size() > 0) {
                for (int i = 0; i < listData.size(); i++) {
                    CashMaster cashMaster = (CashMaster) listData.get(i);
                    JSONObject jSONObjectArray = new JSONObject();
                    
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID],cashMaster.getOID());
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID],cashMaster.getLocationId());
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_CASHIER_NUMBER],cashMaster.getCashierNumber());
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_TAX],cashMaster.getCashTax());
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_SERVICE],cashMaster.getCashService());            
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_PRICE_TYPE],cashMaster.getPriceType());   
                    jSONObjectArray.put(PstCashMaster.fieldNames[PstCashMaster.FLD_CABANG],cashMaster.getCabang());
                    
//                    jSONObjectArray.put(PstShift.fieldNames[PstShift.FLD_NAME], matShift.getName());
//                    jSONObjectArray.put(PstShift.fieldNames[PstShift.FLD_START_TIME], matShift.getStartTime());
//                    jSONObjectArray.put(PstShift.fieldNames[PstShift.FLD_END_TIME], matShift.getEndTime());
//                    jSONObjectArray.put(PstShift.fieldNames[PstShift.FLD_REMARK], matShift.getRemark());
//                    
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
        //processRequest(request, response);
    }
}
