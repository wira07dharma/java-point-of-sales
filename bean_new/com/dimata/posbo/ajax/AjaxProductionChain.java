/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.posbo.entity.masterdata.ChainAddCost;
import com.dimata.posbo.entity.masterdata.ChainMain;
import com.dimata.posbo.entity.masterdata.ChainMainMaterial;
import com.dimata.posbo.entity.masterdata.ChainPeriod;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstChainAddCost;
import com.dimata.posbo.entity.masterdata.PstChainMain;
import com.dimata.posbo.entity.masterdata.PstChainMainMaterial;
import com.dimata.posbo.entity.masterdata.PstChainPeriod;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.Production;
import com.dimata.posbo.entity.warehouse.ProductionCost;
import com.dimata.posbo.entity.warehouse.ProductionGroup;
import com.dimata.posbo.entity.warehouse.ProductionProduct;
import com.dimata.posbo.entity.warehouse.PstProduction;
import com.dimata.posbo.entity.warehouse.PstProductionCost;
import com.dimata.posbo.entity.warehouse.PstProductionGroup;
import com.dimata.posbo.entity.warehouse.PstProductionProduct;
import com.dimata.posbo.form.masterdata.FrmChainAddCost;
import com.dimata.posbo.form.masterdata.FrmChainMainMaterial;
import com.dimata.posbo.form.warehouse.CtrlProductionCost;
import com.dimata.posbo.form.warehouse.CtrlProductionProduct;
import com.dimata.posbo.form.warehouse.FrmProductionCost;
import com.dimata.posbo.form.warehouse.FrmProductionGroup;
import com.dimata.posbo.form.warehouse.FrmProductionProduct;
import com.dimata.posbo.session.masterdata.SessPosting;
import com.dimata.posbo.session.warehouse.SessProduction;
import com.dimata.qdep.db.DBException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import org.json.JSONException;

/**
 *
 * @author IanRizky
 */
public class AjaxProductionChain extends HttpServlet {

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
    private long userid = 0;
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String dateStart = "";
    private String dateEnd = "";
    private String username = "";
    private String message = "";

    //BOOLEAN
    private boolean privUpdate = false;
    private boolean privDelete = false;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.userid = FRMQueryString.requestLong(request, "FRM_FIELD_USER_ID");
        this.oidReturn = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.username = FRMQueryString.requestString(request, "FRM_FIELD_USER_NAME");
        this.dateStart = FRMQueryString.requestString(request, "FRM_FIELD_DATE_START");
        this.dateEnd = FRMQueryString.requestString(request, "FRM_FIELD_DATE_END");
        this.htmlReturn = "";
        this.message = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        //BOOLEAN
        this.privDelete = FRMQueryString.requestBoolean(request, "privdelete");
        this.privUpdate = FRMQueryString.requestBoolean(request, "privupdate");

        //OBJECT
        this.jSONObject = new JSONObject();

        switch (this.iCommand) {
            case Command.SAVE:
                commandSave(request);
                break;

            case Command.LIST:
                commandList(request, response);
                break;

            case Command.DELETEALL:
                commandDeleteAll(request);
                break;

            case Command.DELETE:
                commandDelete(request);
                break;

            default:
                commandNone(request);
        }
        try {

            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", "" + this.oidReturn);
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
            this.jSONObject.put("ERROR_NUMBER_RETURN", this.iErrCode);
            this.jSONObject.put("MESSAGE_RETURN", this.message);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("productRow")) {
            this.htmlReturn = getProductRow(request);
        } else if (this.dataFor.equals("costRow")) {
            this.htmlReturn = getCostRow(request);
        } else if (this.dataFor.equals("getOptionTemplate")) {
            this.htmlReturn = getOptionTemplate(request);
        } else if (this.dataFor.equals("getOptionPeriod")) {
            this.htmlReturn = getOptionPeriod(request);
        } else if (this.dataFor.equals("getDataOptionGroup")) {
            getDataOptionGroup(request);
        } else if (this.dataFor.equals("setEndDatePeriod")) {
            setEndDatePeriod(request);
        } else if (this.dataFor.equals("getConfirmList")) {
            getConfirmList(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("saveProductionGroup")) {
            saveProductionGroup(request);
        } else if (this.dataFor.equals("saveProductionCost")) {
            saveProductionCost(request);
        } else if (this.dataFor.equals("saveProductionProduct")) {
            saveProductionProduct(request);
        }

    }

    public void commandDeleteAll(HttpServletRequest request) {

    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("listItemCostSales")) {
            String[] cols = {
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                PstMaterial.fieldNames[PstMaterial.FLD_BUY_UNIT_ID],
                PstMaterial.fieldNames[PstMaterial.FLD_AVERAGE_PRICE],};
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("deleteProductionGroup")) {
            deleteProductionGroup(request);
        } else if (this.dataFor.equals("deleteProductionCost")) {
            deleteProductionCost(request);
        } else if (this.dataFor.equals("deleteProductionProduct")) {
            deleteProductionProduct(request);
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

        if (dataFor.equals("listItemCostSales")) {
            if (searchTerm.length() > 0) {
                whereClause += ""
                        + "(" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%'"
                        + ")";
            } else {
                String searchSku = FRMQueryString.requestString(request, "where_sku");
                String searchName = FRMQueryString.requestString(request, "where_name");
                if (searchSku.length() > 0) {
                    whereClause += (whereClause.length() > 0) ? " OR " : "";
                    whereClause += "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchSku + "%'";
                }

                if (searchName.length() > 0) {
                    whereClause += (whereClause.length() > 0) ? " OR " : "";
                    whereClause += "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchName + "%'";
                }
            }

            whereClause += (whereClause.length() > 0) ? " AND " : "";
            whereClause += PstMaterial.fieldNames[PstMaterial.FLD_PROCESS_STATUS] + " <> " + PstMaterial.DELETE
                    + " AND " + PstMaterial.fieldNames[PstMaterial.FLD_EDIT_MATERIAL] + " <> " + PstMaterial.EDIT_NON_AKTIVE;
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("listItemCostSales")) {
            total = PstMaterial.getCount(whereClause);
        }

        this.amount = amount;

        this.colName = colName;
        this.dir = dir;
        this.start = start;
        this.colOrder = col;

        try {
            result = getData(total, request, dataFor, whereClause);
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return result;
    }

    public JSONObject getData(int total, HttpServletRequest request, String datafor, String whereClause) {
        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        String order = "";

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("listItemCostSales")) {
            listData = PstMaterial.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            try {
                JSONArray ja = new JSONArray();
                if (datafor.equals("listItemCostSales")) {
                    long locationId = FRMQueryString.requestLong(request, "location_id");

                    Material m = (Material) listData.get(i);
                    Unit u = new Unit();
                    if (m.getBuyUnitId() != 0) {
                        u = PstUnit.fetchExc(m.getBuyUnitId());
                    }
                    double salesValue = 0;
                    if (locationId != 0) {
                        Location l = PstLocation.fetchExc(locationId);
                        salesValue = PstPriceTypeMapping.getSellPrice(m.getOID(), l.getStandarRateId(), l.getPriceTypeId());
                    }
                    String link = "'" + m.getOID() + "','" + m.getSku() + "','" + m.getName() + "','" + u.getOID() + "','" + m.getAveragePrice() + "','" + salesValue + "'";
                    ja.put("" + (this.start + i + 1));
                    ja.put("<a href=\"javascript:selectItem(" + link + ")\">" + m.getSku() + "</a>");
                    ja.put("<a href=\"javascript:selectItem(" + link + ")\">" + m.getName() + "</a>");
                    ja.put("" + u.getCode());
                    ja.put("<div class='text-right'>" + String.format("%,.0f", m.getAveragePrice()) + "</div>");
                    ja.put("<div class='text-right'>" + String.format("%,.0f", salesValue) + "</div>");
                    array.put(ja);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }

    private static String getProductRow(HttpServletRequest request) {
        long oidPeriod = FRMQueryString.requestLong(request, "PERIOD_ID");
        long oidPrevPeriod = FRMQueryString.requestLong(request, "PREV_PERIOD_ID");
        double prevCost = PstChainAddCost.sumTotalCostPeriod(oidPrevPeriod);
        long oidProduct = FRMQueryString.requestLong(request, "PRODUCT_ID");

        ChainMainMaterial product = new ChainMainMaterial();
        Material material = new Material();
        String btnDelete = "";
        if (oidProduct != 0) {
            try {
                product = PstChainMainMaterial.fetchExc(oidProduct);
                btnDelete = "<a class='text-red btnDeleteProduct' style='cursor: pointer' data-period_id='" + product.getChainPeriodId() + "'><i class='fa fa-remove'></i></a>";
                if (product.getMaterialId() != 0) {
                    material = PstMaterial.fetchExc(product.getMaterialId());
                }
            } catch (DBException | com.dimata.posbo.db.DBException e) {
                System.out.println(e.getMessage());
            }
        }
        double totalCostPct = 0;
        double result = 0;
        double subTotalCost = 0;
        double subTotalSales = product.getStockQty() * product.getSalesValue();

        if (product.getMaterialType() != PstChainMainMaterial.TYPE_REFERENCED_COST) {
            totalCostPct = product.getCostPct();
        }

        if (product.getMaterialType() == PstChainMainMaterial.TYPE_RESULT) {
            double referencedCostValue = PstChainMainMaterial.sumTotalCostPeriodSpecificCostType(product.getChainPeriodId(), "" + PstChainMainMaterial.TYPE_REFERENCED_COST);
            result = (product.getStockQty() * product.getCostValue()) + referencedCostValue;
        }

        if (product.getMaterialType() == PstChainMainMaterial.TYPE_RESULT_NEXT_COST) {
            subTotalCost = product.getStockQty() * product.getCostValue();
        }

        String optionMatType = "";
        for (int x = 0; x < PstChainMainMaterial.typeStr.length; x++) {
            if (x == PstChainMainMaterial.TYPE_REFERENCED_COST) {
                continue;
            }
            optionMatType += "<option " + (product.getMaterialType() == x ? "selected" : "") + " value='" + x + "'>" + PstChainMainMaterial.typeStr[x] + "</option>";
        }

        String optionCostType = "";
        for (int x = 0; x < PstChainMainMaterial.COST_TYPE_TITLE.length; x++) {
            if (x == PstChainMainMaterial.COST_TYPE_REFERENCED) {
                continue;
            }
            optionCostType += "<option " + (product.getCostType() == x ? "selected" : "") + " value='" + x + "'>" + PstChainMainMaterial.COST_TYPE_TITLE[x] + "</option>";
        }

        String html = ""
                + "<td class='text-center' style='vertical-align: middle'>" + btnDelete
                + "     <input type='hidden' name='hidden_item_product_id' value='" + product.getOID() + "'>"
                + "</td>"
                + "<td>"
                + "     <div class='input-group'>"
                + "         <input type='hidden' id='cost' value='" + String.format("%.0f", prevCost) + "'>"
                + "         <input type='hidden' name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_CHAIN_PERIOD_ID] + "' value='" + oidPeriod + "'>"
                + "         <input type='hidden' id='product_item' name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_MATERIAL_ID] + "' value='" + product.getMaterialId() + "'>"
                + "         <input type='text' id='product_sku' class='form-control input-sm' name='PRODUCT_SKU' value='" + material.getSku() + "'>"
                + "         <span class='input-group-addon btn btn-primary btn_src_product'><i class='fa fa-search'></i></span>"
                + "     </div>"
                + "</td>"
                + "<td>"
                + "     <div class='input-group'>"
                + "         <input type='text' id='product_name' class='form-control input-sm' name='PRODUCT_NAME' value='" + material.getName() + "'>"
                + "         <span class='input-group-addon btn btn-primary btn_src_product'><i class='fa fa-search'></i></span>"
                + "     </div>"
                + "</td>"
                + "<td>"
                + "     <select name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_MATERIAL_TYPE] + "' id='mat_type' class='form-control input-sm'>" + optionMatType + "</select>"
                + "</td>"
                + "<td>"
                + "     <select name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_COST_TYPE] + "' id='cost_type' class='form-control input-sm'>" + optionCostType + "</select>"
                + "</td>"
                + "<td><input type='text' " + (product.getCostType() == PstChainMainMaterial.COST_TYPE_AUTO ? "readonly" : "") + " name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_COST_PCT] + "' id='product_pct' class='form-control input-sm text-right' value='" + String.format("%.2f", totalCostPct) + "'></td>"
                + "<td><input type='text' readonly name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_PERIOD_DISTRIBUTION] + "' id='product_distribution' class='form-control input-sm text-right' value='" + product.getPeriodDistribution() + "'></td>"
                + "<td><input type='text' name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_STOCK_QTY] + "' id='product_qty' class='form-control input-sm text-right hitungCostValue' value='" + product.getStockQty() + "'></td>"
                + "<td><input type='text' readonly='readonly' name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_COST_VALUE] + "' id='product_cost_value' class='form-control input-sm text-right' value='" + String.format("%.2f", product.getCostValue()) + "'></td>"
                + "<td><input type='text' readonly='' class='form-control input-sm text-right' value='" + String.format("%,.2f", result) + "'></td>"
                + "<td><input type='text' readonly='' class='form-control input-sm text-right' value='" + String.format("%,.2f", subTotalCost) + "'></td>"
                + "<td><input type='text' name='" + FrmChainMainMaterial.fieldNames[FrmChainMainMaterial.FRM_FIELD_SALES_VALUE] + "' id='product_sold_qty' class='form-control input-sm text-right hitungCostValue' value='" + String.format("%.2f", product.getSalesValue()) + "'></td>"
                + "<td><input type='text' readonly='' id='product_subtotal_sales' class='form-control input-sm text-right' value='" + String.format("%,.2f", subTotalSales) + "'></td>"
                + "";
        return html;
    }

    private static String getCostRow(HttpServletRequest request) {
        long oidPeriod = FRMQueryString.requestLong(request, "PERIOD_ID");
        long oidCost = FRMQueryString.requestLong(request, "COST_ID");

        ChainAddCost cost = new ChainAddCost();
        Material material = new Material();
        String btnDelete = "";
        if (oidCost != 0) {
            try {
                cost = PstChainAddCost.fetchExc(oidCost);
                btnDelete = "<a class='btn-xs text-red btnDeleteCost' style='cursor: pointer' data-period_id='" + cost.getChainPeriodId() + "'><i class='fa fa-remove'></i></a>";
                if (cost.getMaterialId() != 0) {
                    material = PstMaterial.fetchExc(cost.getMaterialId());
                }
            } catch (DBException | com.dimata.posbo.db.DBException e) {
                System.out.println(e.getMessage());
            }
        }

        String html = ""
                + "<td class='text-cnter' style='vertical-align: middle'>" + btnDelete
                + "     <input type='hidden' name='hidden_item_cost_id' value='" + cost.getOID() + "'>"
                + "</td>"
                + "<td>"
                + "     <div class='input-group'>"
                + "         <input type='hidden' name='" + FrmChainAddCost.fieldNames[FrmChainAddCost.FRM_FIELD_CHAIN_PERIOD_ID] + "' value='" + oidPeriod + "'>"
                + "         <input type='hidden' id='cost_item' name='" + FrmChainAddCost.fieldNames[FrmChainAddCost.FRM_FIELD_MATERIAL_ID] + "' value='" + cost.getMaterialId() + "'>"
                + "         <input type='text' id='cost_sku' class='form-control input-sm' name='COST_SKU' value='" + material.getSku() + "'>"
                + "         <span class='input-group-addon btn btn-primary btn_src_cost'><i class='fa fa-search'></i></span>"
                + "     </div>"
                + "</td>"
                + "<td>"
                + "     <div class='input-group'>"
                + "         <input type='text' id='cost_name' class='form-control input-sm' name='COST_NAME' value='" + material.getName() + "'>"
                + "         <span class='input-group-addon btn btn-primary btn_src_cost'><i class='fa fa-search'></i></span>"
                + "     </div>"
                + "</td>"
                + "<td><input type='text' readonly class='form-control input-sm' value='" + PstChainAddCost.COST_TYPE_TITLE[PstChainAddCost.COST_TYPE_GENERAL] + "'></td>"
                + "<td><input type='text' name='" + FrmChainAddCost.fieldNames[FrmChainAddCost.FRM_FIELD_STOCK_QTY] + "' id='cost_qty' class='form-control input-sm text-right hitungCostValueCost' value='" + cost.getStockQty() + "'></td>"
                + "<td><input type='text' name='" + FrmChainAddCost.fieldNames[FrmChainAddCost.FRM_FIELD_STOCK_VALUE] + "' id='cost_value' class='form-control input-sm text-right hitungCostValueCost' value='" + cost.getStockValue() + "'></td>"
                + "<td><input type='text' readonly='' id='cost_subtotal' class='form-control input-sm text-right' value='" + String.format("%,.2f", cost.getStockQty() * cost.getStockValue()) + "'></td>"
                + "";
        return html;
    }

    public String getOptionTemplate(HttpServletRequest request) {
        String html = "";
        html += "<option value='0'>- Pilih Template -</option>";
        String batchNumber = FRMQueryString.requestString(request, "batch_number");
        if (batchNumber.isEmpty()) {
            //get all template
            Vector<ChainMain> listTemplate = PstChainMain.list(0, 0, "", "" + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE]);
            for (ChainMain cm : listTemplate) {
                html += "<option value='" + cm.getOID() + "'>" + cm.getChainTitle() + "</option>";
            }
        } else {
            //cari chain main id
            Vector<ProductionGroup> listGroup = PstProductionGroup.list(0, 1, PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + batchNumber + "'", "");
            if (!listGroup.isEmpty()) {
                try {
                    ChainMain chainMain = PstChainMain.fetchExc(PstChainPeriod.fetchExc(listGroup.get(0).getChainPeriodId()).getChainMainId());
                    html += "<option selected value='" + chainMain.getOID() + "'>" + chainMain.getChainTitle() + "</option>";
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return html;
    }

    public String getOptionPeriod(HttpServletRequest request) {
        String html = "";
        html += "<option value='0'>- Pilih Periode -</option>";
        long chainMainId = FRMQueryString.requestLong(request, "chain_main_id");
        String batchNumber = FRMQueryString.requestString(request, "batch_number");

        int nextGroupIndex = 0;
        if (!batchNumber.isEmpty()) {
            //CARI INDEX PERIODE SELANJUTNYA
            Vector<ProductionGroup> listGroup = PstProductionGroup.list(0, 1, PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + batchNumber + "'", PstProductionGroup.fieldNames[PstProductionGroup.FLD_INDEX] + " DESC ");
            if (!listGroup.isEmpty()) {
                nextGroupIndex = listGroup.get(0).getIndex() + 1;
            }
        }
        
        boolean nextPeriod = true;
        Vector<ChainPeriod> listPeriod = PstChainPeriod.list(0, 0, PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + chainMainId, PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]);
        for (ChainPeriod period : listPeriod) {
            if (batchNumber.isEmpty()) {
                String selected = nextPeriod ? "selected" : "";
                html += "<option " + selected + " value='" + period.getOID() + "' data-index='" + period.getIndex() + "'>" + period.getTitle() + "</option>";
                if (nextPeriod) {
                    nextGroupIndex = period.getIndex();
                }
                nextPeriod = false;
            } else {
                //cek apakah periode sudah ada di batch yg sama
                int count = PstProductionGroup.getCount(PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + batchNumber + "'" + " AND " + PstProductionGroup.fieldNames[PstProductionGroup.FLD_CHAIN_PERIOD_ID] + " = " + period.getOID());
                if (count > 0) {
                    html += "<option disabled value='" + period.getOID() + "' data-index='" + period.getIndex() + "'>" + period.getTitle() + "</option>";
                } else {
                    String selected = (nextGroupIndex == period.getIndex()) ? "selected" : "";
                    html += "<option " + selected + " value='" + period.getOID() + "' data-index='" + period.getIndex() + "'>" + period.getTitle() + "</option>";
                }
            }
        }

        try {
            this.jSONObject.put("NEXT_INDEX", nextGroupIndex == 0 ? 1 : nextGroupIndex);
        } catch (Exception e) {
        }
        return html;
    }

    public void getDataOptionGroup(HttpServletRequest request) {
        long groupId = FRMQueryString.requestLong(request, "group_id");
        try {
            if (groupId == 0) {
                this.dateStart = "";
                this.dateEnd = "";
                this.jSONObject.put("GROUP_INDEX", 0);

                //set option batch
                String optionBatch = "<option value=''>Batch Baru</option>";
                optionBatch += SessProduction.listOptionBatchNumber("");
                this.jSONObject.put("OPTION_BATCH", optionBatch);

                //set option template
                Vector<ChainMain> listTemplate = PstChainMain.list(0, 0, "", "" + PstChainMain.fieldNames[PstChainMain.FLD_CHAIN_TITLE]);
                String optionTemplate = "<option value='0'>- Pilih Template -</option>";
                for (ChainMain cm : listTemplate) {
                    optionTemplate += "<option value='" + cm.getOID() + "'>" + cm.getChainTitle() + "</option>";
                }
                this.jSONObject.put("OPTION_TEMPLATE", optionTemplate);

                //set option period
                String optionPeriod = "<option value='0'>- Pilih Periode -</option>";
                this.jSONObject.put("OPTION_PERIOD", optionPeriod);

            } else {
                ProductionGroup group = PstProductionGroup.fetchExc(groupId);
                this.dateStart = group.getDateStart() == null ? "" : Formater.formatDate(group.getDateStart(), "yyyy-MM-dd HH:mm:ss");
                this.dateEnd = group.getDateEnd() == null ? "" : Formater.formatDate(group.getDateEnd(), "yyyy-MM-dd HH:mm:ss");
                this.jSONObject.put("GROUP_INDEX", group.getIndex());

                //set option batch
                String optionBatch = SessProduction.listOptionBatchNumber(group.getBatchNumber());
                this.jSONObject.put("OPTION_BATCH", optionBatch);

                //set option template
                String optionTemplate = "";
                Vector<ProductionGroup> listGroup = PstProductionGroup.list(0, 1, PstProductionGroup.fieldNames[PstProductionGroup.FLD_BATCH_NUMBER] + " = '" + group.getBatchNumber() + "'", "");
                ChainMain chainMain = new ChainMain();
                if (!listGroup.isEmpty()) {
                    try {
                        chainMain = PstChainMain.fetchExc(PstChainPeriod.fetchExc(listGroup.get(0).getChainPeriodId()).getChainMainId());
                        optionTemplate += "<option selected value='" + chainMain.getOID() + "'>" + chainMain.getChainTitle() + "</option>";
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                this.jSONObject.put("OPTION_TEMPLATE", optionTemplate);

                //set option period
                String optionPeriod = "";
                Vector<ChainPeriod> listPeriod = PstChainPeriod.list(0, 0, PstChainPeriod.fieldNames[PstChainPeriod.FLD_CHAIN_MAIN_ID] + " = " + chainMain.getOID(), PstChainPeriod.fieldNames[PstChainPeriod.FLD_INDEX]);
                for (ChainPeriod period : listPeriod) {
                    if (group.getChainPeriodId() == period.getOID()) {
                        optionPeriod += "<option selected value='" + period.getOID() + "'>" + period.getTitle() + "</option>";
                        break;
                    }
                }
                this.jSONObject.put("OPTION_PERIOD", optionPeriod);
            }

        } catch (DBException | JSONException e) {
            this.iErrCode += 1;
            this.message += e.getMessage();
            System.out.println(e.getMessage());
        }
    }

    public synchronized void saveProductionGroup(HttpServletRequest request) {
        try {
            //GET DATA
            ProductionGroup pg = new ProductionGroup();
            FrmProductionGroup fpg = new FrmProductionGroup(request, pg);
            fpg.requestEntityObject(pg);

            //CHECK VALIDATION
            if (pg.getProductionId() == 0) {
                this.iErrCode += 1;
                this.message += "ID production kosong !";
                return;
            }
            if (pg.getChainPeriodId() == 0) {
                this.iErrCode += 1;
                this.message += "ID periode kosong !";
                return;
            }

            if (pg.getBatchNumber().isEmpty()) {
                //GENERATE BATCH NUMBER
                pg.setBatchNumber(SessProduction.generateBatchNumber(pg));
            } else {
                //GET PARENT GROUP ID
                pg.setProductionGroupParentId(SessProduction.getParentGroupId(pg));
            }

            //SAVE GROUP
            if (pg.getOID() == 0) {
                long groupOid = PstProductionGroup.insertExc(pg);
                //SAVE COST
                Vector<ChainAddCost> listCostTemplate = PstChainAddCost.list(0, 0, PstChainAddCost.fieldNames[PstChainAddCost.FLD_CHAIN_PERIOD_ID] + " = " + pg.getChainPeriodId()
                        + " AND " + PstChainAddCost.fieldNames[PstChainAddCost.FLD_COST_TYPE] + " = " + PstChainAddCost.COST_TYPE_GENERAL, "");
                for (ChainAddCost cac : listCostTemplate) {
                    ProductionCost pc = new ProductionCost();
                    pc.setProductionGroupId(groupOid);
                    pc.setMaterialId(cac.getMaterialId());
                    pc.setStockQty(0);
                    pc.setStockValue(PstMaterial.fetchExc(cac.getMaterialId()).getAveragePrice());
                    pc.setCostType(cac.getCostType());
                    pc.setProductDistributionId(0);
                    PstProductionCost.insertExc(pc);
                }

                //cek apakah ada item distribusi di periode sebelumnya
                ArrayList<HashMap> listProductDistribution = SessProduction.getAllItemDistributionLastBatch(groupOid);
                for (HashMap map : listProductDistribution) {
                    try {
                        ProductionCost pc = new ProductionCost();
                        pc.setProductionGroupId(groupOid);
                        pc.setMaterialId(Long.valueOf("" + map.get("MATERIAL_ID")));
                        pc.setStockQty(Integer.valueOf("" + map.get("QTY")));
                        pc.setStockValue(Double.valueOf("" + map.get("VALUE")));
                        pc.setCostType(PstProductionCost.COST_TYPE_REFERENCED);
                        pc.setProductDistributionId(Long.valueOf("" + map.get("PRODUCT_ID")));
                        PstProductionCost.insertExc(pc);
                    } catch (Exception e) {
                    }
                }

                //SAVE PRODUCT
                Vector<ChainMainMaterial> listProductTemplate = PstChainMainMaterial.list(0, 0, PstChainMainMaterial.fieldNames[PstChainMainMaterial.FLD_CHAIN_PERIOD_ID] + " = " + pg.getChainPeriodId(), "");
                for (ChainMainMaterial cmm : listProductTemplate) {
                    ProductionProduct pp = new ProductionProduct();
                    pp.setProductionGroupId(groupOid);
                    pp.setMaterialId(cmm.getMaterialId());
                    pp.setMaterialType(cmm.getMaterialType());
                    pp.setCostType(cmm.getCostType());
                    pp.setCostPct(cmm.getCostPct());
                    pp.setPeriodDistribution(cmm.getPeriodDistribution());
                    pp.setStockQty(0);
                    pp.setCostValue(0);
                    pp.setSalesValue(getSalePrice(groupOid, cmm.getMaterialId()));
                    PstProductionProduct.insertExc(pp);
                }

            } else {
                PstProductionGroup.updateExc(pg);
            }

        } catch (DBException | com.dimata.posbo.db.DBException e) {
            this.iErrCode += 1;
            this.message += e.getMessage();
            System.out.println(e.getMessage());
        }
    }

    public double getSalePrice(long groupId, long materialId) {
        try {
            ProductionGroup pg = PstProductionGroup.fetchExc(groupId);
            ChainPeriod cp = PstChainPeriod.fetchExc(pg.getChainPeriodId());
            ChainMain cm = PstChainMain.fetchExc(cp.getChainMainId());
            Location l = PstLocation.fetchExc(cm.getChainLocation());
            return PstPriceTypeMapping.getPrice(materialId, l.getStandarRateId(), l.getPriceTypeId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public synchronized void saveProductionCost(HttpServletRequest request) {
        long costOid = FRMQueryString.requestLong(request, FrmProductionCost.fieldNames[FrmProductionCost.FRM_FIELD_PRODUCTION_COST_ID]);
        CtrlProductionCost cpc = new CtrlProductionCost(request);
        this.iErrCode = cpc.action(this.iCommand, costOid, this.userid, this.username);
        this.message = cpc.getMessage();
    }

    public synchronized void saveProductionProduct(HttpServletRequest request) {
        long productOid = FRMQueryString.requestLong(request, FrmProductionProduct.fieldNames[FrmProductionProduct.FRM_FIELD_PRODUCTION_PRODUCT_ID]);
        CtrlProductionProduct cpp = new CtrlProductionProduct(request);
        this.iErrCode = cpp.action(this.iCommand, productOid, this.userid, this.username);
        this.message = cpp.getMessage();
    }

    public synchronized void deleteProductionGroup(HttpServletRequest request) {
        //DELETE COST
        Vector<ProductionCost> listCost = PstProductionCost.list(0, 0, PstProductionCost.fieldNames[PstProductionCost.FLD_PRODUCTION_GROUP_ID] + " = " + this.oid, "");
        for (ProductionCost pc : listCost) {
            CtrlProductionCost cpc = new CtrlProductionCost(request);
            this.iErrCode += cpc.action(Command.DELETE, pc.getOID(), this.userid, this.username);
            this.message += cpc.getMessage() + " ";
        }

        //DELETE PRODUCT
        Vector<ProductionProduct> listProduct = PstProductionProduct.list(0, 0, PstProductionProduct.fieldNames[PstProductionProduct.FLD_PRODUCTION_GROUP_ID] + " = " + this.oid, "");
        for (ProductionProduct pp : listProduct) {
            CtrlProductionProduct cpp = new CtrlProductionProduct(request);
            this.iErrCode += cpp.action(Command.DELETE, pp.getOID(), this.userid, this.username);
            this.message += cpp.getMessage() + " ";
        }

        //DELETE GROUP
        try {
            PstProductionGroup.deleteExc(this.oid);
        } catch (Exception e) {
            this.iErrCode += 1;
            this.message += e.getMessage();
            System.out.println(e.getMessage());
        }
    }

    public synchronized void deleteProductionCost(HttpServletRequest request) {
        CtrlProductionCost cpc = new CtrlProductionCost(request);
        this.iErrCode = cpc.action(this.iCommand, this.oid, this.userid, this.username);
        this.message = cpc.getMessage();
    }

    public synchronized void deleteProductionProduct(HttpServletRequest request) {
        CtrlProductionProduct cpp = new CtrlProductionProduct(request);
        this.iErrCode = cpp.action(this.iCommand, this.oid, this.userid, this.username);
        this.message = cpp.getMessage();
    }

    public void setEndDatePeriod(HttpServletRequest request) {
        try {
            if (this.oid == 0) {
                return;
            }
            ChainPeriod period = PstChainPeriod.fetchExc(this.oid);
            long rangeMilliseconds = period.getDuration();
            long days = 0;
            long hours = 0;
            long minutes = 0;
            long seconds = 0;

            try {
                days = rangeMilliseconds / (24 * 60 * 60 * 1000);
                long modDays = rangeMilliseconds % (24 * 60 * 60 * 1000);
                if (modDays > 0) {
                    hours = modDays / (60 * 60 * 1000);
                    long modHours = modDays % (60 * 60 * 1000);
                    if (modHours > 0) {
                        minutes = modHours / (60 * 1000);
                        long modMinutes = modHours % (60 * 1000);
                        if (modMinutes > 0) {
                            seconds = modMinutes / (1000);
                        }
                    }
                }
            } catch (Exception e) {
            }

            Date startDate = Formater.formatDate(this.dateStart, "yyyy-MM-dd HH:mm:ss");
            if (startDate == null) {
                return;
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DAY_OF_MONTH, Integer.valueOf("" + days));
            cal.add(Calendar.HOUR, Integer.valueOf("" + hours));
            cal.add(Calendar.MINUTE, Integer.valueOf("" + minutes));
            cal.add(Calendar.SECOND, Integer.valueOf("" + seconds));
            Date endDate = cal.getTime();
            this.dateEnd = Formater.formatDate(endDate, "yyyy-MM-dd HH:mm:ss");
        } catch (DBException | NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void getConfirmList(HttpServletRequest request) {
        String html = "";
        try {
            if (this.oid != 0) {
                Production p = PstProduction.fetchExc(this.oid);
                ArrayList list = SessProduction.getItemProduct(this.oid);
                for (int i = 0; i < list.size(); i++) {
                    try {
                        JSONObject object = (JSONObject) list.get(i);
                        double currentStock = SessPosting.sumQtyStockAllLocation(object.getLong("MATERIAL_ID"));
                        double averagePrice = object.getDouble("AVERAGE_PRICE");
                        double qtyProduct = object.getDouble("QTY");
                        double costProduct = object.getDouble("COST");
                        double newStock = object.getDouble("QTY") + currentStock;
                        double newAveragePrice = ((currentStock * averagePrice) + (qtyProduct * costProduct)) / (currentStock + qtyProduct);
                        html += ""
                                + "<tr>"
                                + "     <td>" + (i + 1) + ".</td>"
                                + "     <td>" + object.getString("SKU") + "</td>"
                                + "     <td>" + object.getString("NAME") + "</td>"
                                + "     <td class='text-center'>" + SessProduction.getDocReceiveNotClosed(object.getLong("MATERIAL_ID"), p.getProductionDate()) + " Document</td>"
                                + "     <td class='text-center'>" + SessProduction.getDocProductionNotClosed(object.getLong("MATERIAL_ID"), p.getProductionDate()) + " Document</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", qtyProduct) + "</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", costProduct) + "</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", currentStock) + "</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", averagePrice) + "</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", newStock) + "</td>"
                                + "     <td class='text-center'>" + String.format("%,.0f", newAveragePrice) + "</td>"
                                + "</tr>";
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        if (html.isEmpty()) {
            html = "<tr><td colspan='11' class='text-center text-bold'>Tidak ada item produksi</td></tr>";
        }
        this.htmlReturn = html;
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
