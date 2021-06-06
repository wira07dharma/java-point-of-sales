/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.db.DBException;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialStock;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialStock;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatDispatchItem;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatCosting;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatDispatchItem;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Ed
 */
public class AjaxBahanBaku extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
    private JSONArray arrayPromo = new JSONArray();
    private JSONArray arrayItem = new JSONArray();

    //LONG
    private long oid = 0;
    private long oidBaru = 0;
    private long oidPromo = 0;
    private long oidPromoBaru = 0;

    //STRING
    private String oidReturn = "";
    private String dataFor = "";
    private String dataFor2 = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String oidLocation = "";
    private String oidMember = "";
    private String oidPrice = "";

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    // ANALYSIS
    private double subjectSalesPrice = 0;
    private double subjectPurchasePrice = 0;
    private int subjectQuantity = 0;
    private int subjectTarget = 0;

    private int objectQuantity = 0;
    private int objectQuantityAll = 0;
    private int objectTarget = 0;
    private double objectRegularPrice = 0;
    private double objectCost = 0;
    private double objectPromotionPrice = 0;

    private double profitNonPromo = 0;
    private double profitPromo = 0;
    private double valuePromo = 0;
    private double percentPromo = 0;
    private double totalProfitNonPromo = 0;
    private double totalMarginPromo = 0;
    private double totalValuePromo = 0;

    // SEARCH TERM
    private String searchTagline = "";
    private String searchReason = "";
    private String searchItem = "";
    private String searchLocation = "";
    private String searchMember = "";
    private String searchPrice = "";
    private String searchSubcomb = "";
    private String searchObcomb = "";

    private long locationId = 0;

    private int status = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidPromo = FRMQueryString.requestLong(request, "FRM_FLD_PROMO_OID");
        this.locationId = FRMQueryString.requestLong(request, "FRM_FLD_LOCATION_ID");
        this.oidReturn = "";
        this.oidBaru = 0;
        this.oidPromoBaru = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.dataFor2 = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR2");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.oidLocation = FRMQueryString.requestString(request, "FRM_FLD_LOCATION_ID");
        this.oidMember = FRMQueryString.requestString(request, "FRM_FLD_MEMBER_TYPE_ID");
        this.oidPrice = FRMQueryString.requestString(request, "FRM_FLD_PRICE_TYPE_ID");
        this.htmlReturn = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        this.status = 0;

        //OBJECT
        this.jSONObject = new JSONObject();
        this.jSONArray = new JSONArray();
        this.arrayPromo = new JSONArray();
        this.arrayItem = new JSONArray();

        //SEARCH TERM
        this.searchTagline = FRMQueryString.requestString(request, "term-tagline");
        this.searchReason = FRMQueryString.requestString(request, "term-reason");
        this.searchItem = FRMQueryString.requestString(request, "term-item");
        this.searchLocation = FRMQueryString.requestString(request, "term-location");
        this.searchMember = FRMQueryString.requestString(request, "term-member");
        this.searchPrice = FRMQueryString.requestString(request, "term-price");
        this.searchSubcomb = FRMQueryString.requestString(request, "term-subcomb");
        this.searchObcomb = FRMQueryString.requestString(request, "term-obcomb");

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.RESET:
                //commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }
        try {
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_FIELD_OID_BARU", "" + this.oidBaru);
            this.jSONObject.put("FRM_FIELD_PROMO", this.arrayPromo);
            this.jSONObject.put("FRM_FIELD_COMBO", this.jSONArray);
            this.jSONObject.put("FRM_FIELD_ITEM", this.arrayItem);

            this.jSONObject.put("FRM_FIELD_STATUS", this.status);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }

    private void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listevent")) {
            String[] cols = {
                "" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID],
                "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                "" + PstUnit.fieldNames[PstUnit.FLD_CODE],
                "" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        } else if (this.dataFor.equals("listevent2")) {
            String[] cols = {
                "" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID],
                "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                "" + PstUnit.fieldNames[PstUnit.FLD_CODE],
                "" + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY]
            };
            jSONObject = listDataTables2(request, response, cols, this.dataFor, this.jSONObject);
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

        if (dataFor.equals("listevent")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = 1";
            } else {
                whereClause += " "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //+ " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //+ " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = 1";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listevent")) {
            total = PstMaterial.getCount(whereClause);
        }

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject listDataTables2(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
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

        if (dataFor.equals("listevent2")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += " "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //+ " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //+ " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listevent2")) {
            total = PstMaterial.getCount(whereClause);
        }

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData2(total, request, dataFor);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor) {

        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        Material material = new Material();
        Unit unit = new Unit();
        MaterialStock materialStock = new MaterialStock();

        String whereClause = "";
        String order = "";

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("listevent")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //                            + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //                            + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND pm." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = 1 "
                        + "AND pmr." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE] + " = 3 "
                        + "AND pmr." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + locationId;
            } else {
                whereClause += " "
                        + "(pm." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR pm." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR pm." + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //                                + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //                                + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")"
                        + " AND pm." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE] + " = 1 "
                        + "AND pmr." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_TYPE] + " = 3 "
                        + "AND pmr." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " = " + locationId;
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listevent")) {
            listData = PstMaterial.listMaterial(start, amount, whereClause, order);
        }

        Vector vUnit = new Vector();
        Vector vStock = new Vector();
        Vector vmri = new Vector();

        MatReceiveItem mri = new MatReceiveItem();

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            MatReceive mr = new MatReceive();
            Location l = new Location();
            if (datafor.equals("listevent")) {
                material = (Material) listData.get(i);
                vUnit = PstUnit.list(0, 0, PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + " = "
                        + material.getDefaultStockUnitId(), null);
                unit = (Unit) vUnit.get(0);

                vmri = PstMatReceiveItem.list(0, 0, PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID] + " = "
                        + material.getOID(), null);
                mri = (MatReceiveItem) vmri.get(0);
                try {
                    mr = PstMatReceive.fetchExc(mri.getReceiveMaterialId());
                    l = PstLocation.fetchExc(mr.getLocationId());
                } catch (DBException ex) {
                    Logger.getLogger(AjaxBahanBaku.class.getName()).log(Level.SEVERE, null, ex);
                }

                vStock = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + " = "
                        + material.getOID() + " AND " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID] + " = " + mr.getLocationId(), null);
                materialStock = (MaterialStock) vStock.get(0);
                ja.put("" + (this.start + i + 1));
                ja.put("" + material.getSku());
                ja.put("<a href=\"#\" type=\"button\" data-stok =\"" + materialStock.getQty() + "\" data-lokasi =\"" + l.getName() + "\" data-oid=\"" + material.getOID() + "\" data-nama=\"" + material.getName() + "\" data-harga=\"" + material.getAveragePrice() + "\" data-qty=\"" + materialStock.getQty() + "\" data-unit=\"" + unit.getCode() + "\" data-unitid=\"" + unit.getOID() + "\" class=\"source\">" + material.getName() + "</a>");
                ja.put("" + unit.getCode());
                ja.put("" + materialStock.getQty());
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            //result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }

    public JSONObject getData2(int total, HttpServletRequest request, String datafor) {

        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        Material material = new Material();
        Unit unit = new Unit();
        MaterialStock materialStock = new MaterialStock();

        String whereClause = "";
        String order = "";

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("listevent2")) {
            if (whereClause.length() > 0) {
                whereClause += ""
                        + "AND "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //                            + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //                            + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                whereClause += " "
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        //                                + " OR " + PstUnit.fieldNames[PstUnit.FLD_CODE] + " LIKE '%" + searchTerm + "%'"
                        //                                + " OR " + PstMaterialStock.fieldNames[PstMaterialStock.FLD_QTY] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listevent2")) {
            listData = PstMaterial.list(start, amount, whereClause, order);
        }

        Vector vUnit = new Vector();
        Vector vStock = new Vector();
        Vector vmri = new Vector();

        MatReceiveItem mri = new MatReceiveItem();

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            MatReceive mr = new MatReceive();
            Location l = new Location();
            if (datafor.equals("listevent2")) {
                material = (Material) listData.get(i);
                vUnit = PstUnit.list(0, 0, PstUnit.fieldNames[PstUnit.FLD_UNIT_ID] + " = "
                        + material.getDefaultStockUnitId(), null);
                unit = (Unit) vUnit.get(0);

//                vmri = PstMatReceiveItem.list(0, 0, PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+" = "
//                        +material.getOID(), null);
//                mri = (MatReceiveItem) vmri.get(i);
//                try {
//                    mr = PstMatReceive.fetchExc(mri.getReceiveMaterialId());
//                    l = PstLocation.fetchExc(mr.getLocationId());
//                } catch (DBException ex) {
//                    Logger.getLogger(AjaxBahanBaku.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                vStock = PstMaterialStock.list(0, 0, PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID] + " = "
//                        + material.getOID()+" AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+" = "+mr.getLocationId(), null);
//                materialStock = (MaterialStock) vStock.get(0);
                ja.put("" + (this.start + i + 1));
                ja.put("" + material.getSku());
                ja.put("<a href=\"#\" type=\"button\" data-stok =\"" + materialStock.getQty() + "\" data-lokasi =\"" + l.getName() + "\" data-oid=\"" + material.getOID() + "\" data-nama=\"" + material.getName() + "\" data-harga=\"" + material.getAveragePrice() + "\" data-qty=\"" + materialStock.getQty() + "\" data-unit=\"" + unit.getCode() + "\" data-unitid=\"" + unit.getOID() + "\" class=\"source\">" + material.getName() + "</a>");
                ja.put("" + unit.getCode());
                ja.put("" + material.getAveragePrice());
                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            //result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
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

    private void commandSave(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void commandDelete(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("checkStatus")) {
            Vector vPembiayaan = PstMatCosting.list(0, 0, PstMatCosting.fieldNames[PstMatCosting.FLD_DOCUMENT_ID] + " = "
                    + this.oid + " AND "
                    + PstMatCosting.fieldNames[PstMatCosting.FLD_COSTING_STATUS] + " <> 5 ", null);
            if (vPembiayaan.size() > 0) {
                this.status = 1;
            } else {
                this.status = 0;
            }
        }
    }

}
