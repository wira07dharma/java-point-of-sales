/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;


/**
 *
 * @author dimata005
 */
import com.dimata.common.entity.location.*;
import com.dimata.common.entity.logger.*;
import com.dimata.posbo.entity.masterdata.*;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.form.masterdata.*;
import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.posbo.report.sale.SaleReportItem;
import com.dimata.posbo.session.warehouse.SessMatCostingStokFisik;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * @author Dewa
 */
public class AjaxNilaiTukarEmas extends HttpServlet {

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
    private long oidReturn = 0;

    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    private String dateStart = "";
    private String dateEnd = "";
    private String username = "";
    private String userid = "";            

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
        this.oidReturn = 0;

        //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.username = FRMQueryString.requestString(request, "FRM_FIELD_USER_NAME");
        this.userid = FRMQueryString.requestString(request, "FRM_FIELD_USER_ID");
        this.htmlReturn = "";

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
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);

    }

    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showForm(request);
        } else if (this.dataFor.equals("showformLog")) {
            this.htmlReturn = showFormLog(request);
        }
    }

    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveNilaiTukarEmas(request);
        }
    }

    public void commandDeleteAll(HttpServletRequest request) {
        if (this.dataFor.equals("deleteAll")) {
            this.htmlReturn = deleteAll(request);
        }
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("list")) {
            String[] cols = {
                PstMaterialNilaiTukarEmas .fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_COLOR_ID],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_LOKAL],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_LOKAL_RUSAK],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_ASING],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_RETURN_ASING_RUSAK],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_PESANAN],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR],
                PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_TARIF_RETUR_DIATAS_SETAHUN]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
        
        if (this.dataFor.equals("listItemCostSales")) {
            String[] cols = {
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_NAME],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU],
                PstMaterial.fieldNames[PstMaterial.FLD_SKU]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }

    public void commandDelete(HttpServletRequest request) {
        if (this.dataFor.equals("delete")) {
            this.htmlReturn = deleteAll(request);
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

        if (dataFor.equals("list")) {

            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR " + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%')";
            }
        }

        if (dataFor.equals("listItemCostSales")) {
            
            if (whereClause.length() > 0) {
                whereClause += "AND"
                        + " (" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%')";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstMaterialNilaiTukarEmas.getCount(whereClause);
        }

        if (dataFor.equals("listItemCostSales")) {
            String searchSku = FRMQueryString.requestString(request, "where_sku");
            String searchName = FRMQueryString.requestString(request, "where_name");
            String whereAdd = "";
            
            if (searchSku.length() > 0) {
                whereAdd += "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + searchSku + "%'";
            }

            if (searchName.length() > 0) {
                if (searchSku.length() > 0) {
                    whereAdd += " OR ";
                }
                whereAdd += "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchName + "%'";
            }
            
            if (whereAdd.length() > 0 && searchTerm.length() == 0) {
                whereClause += " AND " + whereAdd;
            }
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

    public JSONObject getData(int total, HttpServletRequest request, String datafor) {

        int totalAfterFilter = total;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        MaterialNilaiTukarEmas materialNilaiTukarEmas = new MaterialNilaiTukarEmas();
        String whereClause = "";
        String order = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("list")) {

            if (whereClause.length() > 0) {
                whereClause += "AND (pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%'"
                        + " OR pos_material_nilai_tukar_emas." + PstMaterialNilaiTukarEmas.fieldNames[PstMaterialNilaiTukarEmas.FLD_KADAR_ID] + " LIKE '%" + searchTerm + "%')";
            }
        } else if (datafor.equals("listItemCostSales")) {

            if (whereClause.length() > 0) {
                whereClause += "AND"
                        + " (" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += ""
                        + " (" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchTerm + "%')";
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("list")) {
            listData = PstMaterialNilaiTukarEmas.list(start, amount, whereClause, order);
        }
        
        if (datafor.equals("listItemCostSales")) {
            String searchSku = FRMQueryString.requestString(request, "where_sku");
            String searchName = FRMQueryString.requestString(request, "where_name");
            String whereAdd = "";
            
            if (searchSku.length() > 0) {
                whereAdd += "" + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " LIKE '" + searchSku + "%'";
            }

            if (searchName.length() > 0) {
                if (searchSku.length() > 0) {
                    whereAdd += " OR ";
                }
                whereAdd += "" + PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + searchName + "%'";
            }
            
            if (whereAdd.length() > 0 && searchTerm.length() == 0) {
                whereClause += " AND " + whereAdd;
            }
            listData = PstMaterial.list(start, amount, whereClause, order);
        }
        /*
        <th>No.</th>
        <th>Kadar</th>
        <th>Warna</th>
        <th>Nilai Tukar <br> Jual</th>
        <th>Nilai Tukar <br> Beli</th>
        <th>Harga Jual</th>
        <th>Harga Beli</th>
        <th>Nilai Tukar Return</th>
        <th>Harga Return</th>
        <th>Action</th>
        */
        
        Vector vMasLantakan = PstEmasLantakan.list(0, 1, "" + PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF] + "=1", "");
        EmasLantakan emasLantakan = new EmasLantakan();
        try{
            if (vMasLantakan.size() > 0) {
                emasLantakan = (EmasLantakan) vMasLantakan.get(0);
            }
        }catch(Exception ex){
        }
        
        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("list")) {
                String buttonAction = "";
                materialNilaiTukarEmas = (MaterialNilaiTukarEmas) listData.get(i);
                Kadar kadar = new Kadar();
                Color color = new Color();
                try {
                    if (PstKadar.checkOID(materialNilaiTukarEmas.getKadarId())) {
                        kadar = PstKadar.fetchExc(materialNilaiTukarEmas.getKadarId());
                    }
                    if (PstColor.checkOID(materialNilaiTukarEmas.getColorId())) {
                        color = PstColor.fetchExc(materialNilaiTukarEmas.getColorId());
                    }
                } catch (DBException ex) {
                    Logger.getLogger(AjaxNilaiTukarEmas.class.getName()).log(Level.SEVERE, null, ex);
                }
                ja.put("" + (this.start + i + 1));
                ja.put("" + kadar.getKadar());
                ja.put("" + color.getColorName());
                ja.put("" + materialNilaiTukarEmas.getLokal());
                ja.put("" + materialNilaiTukarEmas.getReturnLokal());
                ja.put("" + String.format("%,.0f",(materialNilaiTukarEmas.getLokal()/100)*emasLantakan.getHargaJual())+".00");
                ja.put("" + String.format("%,.0f",(materialNilaiTukarEmas.getReturnLokal()/100)*emasLantakan.getHargaBeli())+".00");
                ja.put("" + materialNilaiTukarEmas.getReturnLokalRusak());
                ja.put("" + materialNilaiTukarEmas.getAsing());
                ja.put("" + materialNilaiTukarEmas.getReturnAsing());
                ja.put("" + materialNilaiTukarEmas.getReturnAsingRusak());
                ja.put("" + materialNilaiTukarEmas.getPesanan());
                ja.put("" + materialNilaiTukarEmas.getTarifRetur());
                ja.put("" + materialNilaiTukarEmas.getTarifReturDiatasSetahun());
                
                //------------- old --------------------
//                ja.put("" + FRMHandler.userFormatStringDecimal(materialNilaiTukarEmas.getNilaiTukarJual())+" %");
//                ja.put("" + FRMHandler.userFormatStringDecimal(materialNilaiTukarEmas.getNilaiTukarBeli())+" %");
//                ja.put(""+FRMHandler.userFormatStringDecimal((materialNilaiTukarEmas.getNilaiTukarJual()/100)*emasLantakan.getHargaJual())); //harga jual
//                ja.put(""+FRMHandler.userFormatStringDecimal((materialNilaiTukarEmas.getNilaiTukarBeli()/100)*emasLantakan.getHargaBeli())); //harga beli
//                ja.put(""+FRMHandler.userFormatStringDecimal(materialNilaiTukarEmas.getNilaiTukarJualReturn())+" %");
//                ja.put(""+FRMHandler.userFormatStringDecimal(materialNilaiTukarEmas.getNilaiTukarBeliReturn())+" %");
//                //double hargabeli = (materialNilaiTukarEmas.getNilaiTukarBeli()/100)*emasLantakan.getHargaBeli();
//                double masLantakan = emasLantakan.getHargaBeli()*0.02;
//                double duadua = masLantakan*materialNilaiTukarEmas.getNilaiTukarBeli()/100;
//                double nilaiReturn = emasLantakan.getHargaBeli()- duadua;
//                ja.put(""+ FRMHandler.userFormatStringDecimal(nilaiReturn)); //harga beli
                buttonAction = "<div class=\"text-center\">";
                if (true) {
                    buttonAction += "<button type='button' class='btn btn-sm btn-warning btneditgeneral' data-oid='" + materialNilaiTukarEmas.getOID() + "' data-for='showform' style=\"right:10px\" ><i class='fa fa-pencil'></i></button>";
                    buttonAction += "<button type='button' class='btn btn-sm btn-info btneditgeneral' data-oid='" + materialNilaiTukarEmas.getOID() + "' data-for='showformLog' style=\"right:10px\" ><i class='fa fa-history'></i></button>";
                }
                buttonAction += "</div>";
                ja.put(buttonAction);
                array.put(ja);
            }
            
            if (datafor.equals("listItemCostSales")) {
                CalcCogsMain calcCogsMain = new CalcCogsMain();
                Material m = (Material) listData.get(i);
                Unit u = new Unit();
                
                double harga = 0;
                double stokAwal = 0;
                double sisaStok = 0;
                double jumlah = 0;
                double subTotal = 0;
                double nilaiProduksi = 0;

                long oidCalcCogsMain = FRMQueryString.requestLong(request, "hidden_calc_cogs_main_id");
                int showModalItem = FRMQueryString.requestInt(request, "modal_item");

                try {
                    if (m.getDefaultStockUnitId() > 0 && PstUnit.checkOID(m.getDefaultStockUnitId())) {
                        u = PstUnit.fetchExc(m.getDefaultStockUnitId());
                    }
                    if (oidCalcCogsMain > 0 && PstCalcCogsMain.checkOID(oidCalcCogsMain)) {
                        calcCogsMain = PstCalcCogsMain.fetchExc(oidCalcCogsMain);
                    }

                    if (showModalItem == 1) {
                        //get data lokasi biaya
                        Vector<CalcCogsLocation> listLocationCost = PstCalcCogsLocation.list(0, 0, ""
                                + "" + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + oidCalcCogsMain
                                + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_COST, "");

                        String lokasiCosting = "";
                        for (int loc = 0; loc < listLocationCost.size(); loc++) {
                            long idLocation = listLocationCost.get(loc).getLocationId();
                            if (loc > 0) {
                                lokasiCosting += ",";
                            }
                            lokasiCosting += "" + idLocation;
                        }
                        
                        //get data jika menggunakan semua lokasi                        
                        if (listLocationCost.isEmpty()) {                            
                            Vector listAllLocation = PstLocation.list(0, 0, "", "");
                            for (int l = 0; l < listAllLocation.size(); l++) {
                                Location loc = (Location) listAllLocation.get(l);
                                long idLocation = loc.getOID();
                                if (l > 0) {
                                    lokasiCosting += ",";
                                }
                                lokasiCosting += "" + idLocation;
                            }
                        }
                        
                        //get data qty item biaya dan total biaya
                        Vector listDataBiaya = SessMatCostingStokFisik.getTotalCosting(m.getOID(), lokasiCosting, calcCogsMain.getCostDateStart(), calcCogsMain.getCostDateEnd());
                        for (int j = 0; j < listDataBiaya.size(); j++) {
                            ArrayList<Double> aData = (ArrayList) listDataBiaya.get(j);
                            jumlah += aData.get(0);
                            subTotal += aData.get(1);
                        }
                        
                    } else if (showModalItem == 2) {
                        //kurangi tgl start sales 1 detik untuk mencari data stok awal
                        Date startSales = calcCogsMain.getSalesDateStart();
                        Calendar c = Calendar.getInstance();
                        if (startSales != null) {
                            c.setTime(startSales);
                            c.add(Calendar.SECOND, -1);
                        }
                        Date beforeSales = c.getTime();

                        //tambah tgl end sales 1 detik untuk mencari data sisa stok
                        Date endSales = calcCogsMain.getSalesDateEnd();
                        if (endSales != null) {
                            c.setTime(endSales);
                            c.add(Calendar.SECOND, 1);
                        }
                        Date afterSales = c.getTime();

                        //get data lokasi jual
                        Vector<CalcCogsLocation> listLocationSales = PstCalcCogsLocation.list(0, 0, ""
                                + "" + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_CALC_COGS_MAIN_ID] + " = " + oidCalcCogsMain
                                + " AND " + PstCalcCogsLocation.fieldNames[PstCalcCogsLocation.FLD_LOCATION_CALC_COGS_TYPE] + " = " + PstCalcCogsLocation.CALC_COGS_LOC_TYPE_SALES, "");

                        String lokasiSales = "";
                        for (int loc = 0; loc < listLocationSales.size(); loc++) {
                            long idLocation = listLocationSales.get(loc).getLocationId();
                            if (loc > 0) {
                                lokasiSales += ",";
                            }
                            lokasiSales += "" + idLocation;

                            //get data stok awal stok akhir
                            stokAwal += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, beforeSales, 0);
                            sisaStok += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, afterSales, 0);

                            //get data qty item jual dan total penjualan
                            SrcSaleReport srcSaleReport = new SrcSaleReport();
                            srcSaleReport.setDateFrom(calcCogsMain.getSalesDateStart());
                            srcSaleReport.setDateTo(calcCogsMain.getSalesDateEnd());
                            srcSaleReport.setItemId(m.getOID());
                            srcSaleReport.setLocationId(idLocation);
                            srcSaleReport.setTransType(SrcSaleReport.TYPE_CASH_CREDIT);
                            //srcSaleReport.setViewReport(SrcSaleReport.VIEW_BY_DETAIL);
                            Vector records = SaleReportDocument.getList(srcSaleReport, 0, 0, SaleReportDocument.LOG_MODE_CONSOLE);
                            for (int item = 0; item < records.size(); item++) {
                                SaleReportItem saleItem = (SaleReportItem) records.get(item);
                                jumlah += saleItem.getTotalQty();
                                subTotal += saleItem.getTotalSold();
                            }
                        }
                        //get data jika menggunakan semua lokasi
                        if (listLocationSales.isEmpty()) {
                            Vector listAllLocation = PstLocation.list(0, 0, "", "");
                            for (int l = 0; l < listAllLocation.size(); l++) {
                                Location loc = (Location) listAllLocation.get(l);
                                long idLocation = loc.getOID();
                                //get data stok awal stok akhir
                                stokAwal += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, beforeSales, 0);
                                sisaStok += SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), idLocation, afterSales, 0);

                                //get data qty item jual dan total penjualan
                                SrcSaleReport srcSaleReport = new SrcSaleReport();
                                srcSaleReport.setDateFrom(calcCogsMain.getSalesDateStart());
                                srcSaleReport.setDateTo(calcCogsMain.getSalesDateEnd());
                                srcSaleReport.setItemId(m.getOID());
                                srcSaleReport.setLocationId(idLocation);
                                srcSaleReport.setTransType(SrcSaleReport.TYPE_CASH_CREDIT);
                                //srcSaleReport.setViewReport(SrcSaleReport.VIEW_BY_DETAIL);
                                Vector records = SaleReportDocument.getList(srcSaleReport, 0, 0, SaleReportDocument.LOG_MODE_CONSOLE);
                                for (int item = 0; item < records.size(); item++) {
                                    SaleReportItem saleItem = (SaleReportItem) records.get(item);
                                    jumlah += saleItem.getTotalQty();
                                    subTotal += saleItem.getTotalSold();
                                }
                            }
                        }
                    }
                    
                    harga = subTotal / jumlah;
                    if (subTotal == 0 && jumlah == 0) {
                        harga = 0;
                    }
                    nilaiProduksi = (sisaStok + jumlah - stokAwal) * harga;
                    String link = "";
                    if (showModalItem == 1) {
                        link = "'" + m.getOID() + "','" + m.getSku() + "','" + m.getName() + "','" + u.getOID() + "','" + u.getCode() + "','" + String.format("%,.0f", harga) + "','" + String.format("%,.0f", jumlah) + "','" + String.format("%,.0f", subTotal) + "'";
                    } else if (showModalItem == 2) {
                        link = "'" + m.getOID() + "','" + m.getSku() + "','" + m.getName() + "','" + u.getOID() + "','" + u.getCode() + "','" + String.format("%,.0f", harga) + "','" + String.format("%,.0f", jumlah) + "','" + String.format("%,.0f", subTotal) + "','" + String.format("%,.0f", stokAwal) + "','" + String.format("%,.0f", sisaStok) + "','" + String.format("%,.0f", nilaiProduksi) + "'";
                    }

                    ja.put("" + (this.start + i + 1));
                    ja.put("<a href=\"javascript:selectItem(" + link + ")\">" + m.getSku() + "</a>");
                    ja.put("<a href=\"javascript:selectItem(" + link + ")\">" + m.getName() + "</a>");
                    ja.put("" + u.getCode());
                    
                    if (showModalItem == 2) {
                        ja.put("" + String.format("%,.0f", stokAwal));
                    }
                    
                    ja.put("" + String.format("%,.0f", harga));
                    ja.put("" + String.format("%,.0f", jumlah));
                    
                    if (showModalItem == 2) {
                        ja.put("" + String.format("%,.0f", sisaStok));
                    }
                    
                    ja.put("" + String.format("%,.0f", subTotal));
                    
                    if (showModalItem == 2) {
                        ja.put("" + String.format("%,.0f", nilaiProduksi));
                    }
                    array.put(ja);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
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

    public String showForm(HttpServletRequest request) {
        MaterialNilaiTukarEmas materialNilaiTukarEmas = new MaterialNilaiTukarEmas();
        if (oid != 0) {
            try {
                materialNilaiTukarEmas = PstMaterialNilaiTukarEmas.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        
        Vector val_kadarid =  new Vector();
        Vector key_kadarid = new Vector();
        Vector vKadar = PstKadar.listAll();
        for (int i=0; i<vKadar.size();i++ ){
            Kadar entKadar = (Kadar) vKadar.get(i);
            val_kadarid.add(""+entKadar.getOID()+"");
            key_kadarid.add(""+entKadar.getKodeKadar()+"");
        }
        
        Vector vWarna = PstColor.listAll();
        
        String returnData = ""
        + "<div class='row'>"
            + "<div class='col-md-12'>"
                
                /////////////////////////////////KADAR//////////////////////////
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_KADAR_ID] + "'></i>"
                    + " Kadar <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>";
                    if (vKadar.size() > 0){
                        returnData +=  "<select id='" +FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_KADAR_ID]+"'class='form-control select2' style='width: 100%' data-required='required' data-type='combobox' data-error-message='" +FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_KADAR_ID]+ "' name='"+ FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_KADAR_ID]+ "'>";
                        
                        for (int i = 0; i < vKadar.size(); i++) {
                            Kadar kadar = (Kadar) vKadar.get(i);
                            
                            if (kadar.getOID() == materialNilaiTukarEmas.getKadarId()){
                                //EDIT
                                returnData += ""
                                        + "<option value='"+ kadar.getOID()+"' selected='selected'> "
                                        + kadar.getKadar()
                                        + "</option>";
                            } else {
                                //ADD
                                returnData += ""
                                        + "<option value='"+ kadar.getOID()+"'> "
                                        + kadar.getKadar()
                                        + "</option>";
                            }
                        }
                        returnData+= "</select>";
                    } else {
                        returnData += "<p> Data Kadar tidak tersedia! </p>";
                    }
                returnData+=  "</div>"
                        + "</div>"
                //////////////////////////END KADAR/////////////////////////////
              
                /////////////////////////////WARNA/////////////////////////////////
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_KADAR_ID] + "'></i>"
                    + " Warna <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>";
                    if (vWarna.size() > 0){
                        returnData +=  "<select id='" +FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_COLOR_ID]+"'class='form-control select2' style='width: 100%' data-required='required' data-type='combobox' data-error-message='" +FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_COLOR_ID]+ "' name='"+ FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_COLOR_ID]+ "'>";
                        
                        for (int i = 0; i < vWarna.size(); i++) {
                            Color color = (Color) vWarna.get(i);
                            
                            if (color.getOID() == materialNilaiTukarEmas.getColorId()){
                                //EDIT
                                returnData += ""
                                        + "<option value='"+ color.getOID()+"' selected='selected'> "
                                        + color.getColorName()
                                        + "</option>";
                            } else {
                                //ADD
                                returnData += ""
                                        + "<option value='"+ color.getOID()+"'> "
                                        + color.getColorName()
                                        + "</option>";
                            }
                        }
                        returnData+= "</select>";
                    } else {
                        returnData += "<p> Data Warna tidak tersedia! </p>";
                    }
                returnData+=  "</div>"
                        + "</div>"
                ///////////////////////END WARNA/////////////////////////////////
                        
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_LOKAL] + "'></i>"
                    + " Lokal <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_LOKAL] + "' value='" + materialNilaiTukarEmas.getLokal()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_LOKAL] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_LOKAL] + "'></i>"
                    + " Return Lokal <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_LOKAL] + "' value='" + materialNilaiTukarEmas.getReturnLokal()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas. FRM_FIELD_RETURN_LOKAL] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_LOKAL_RUSAK] + "'></i>"
                    + " Return Lokal Rusak <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_LOKAL_RUSAK] + "' value='" + materialNilaiTukarEmas.getReturnLokalRusak()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_LOKAL_RUSAK] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_ASING] + "'></i>"
                    + " Asing <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_ASING] + "' value='" + materialNilaiTukarEmas.getAsing()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_ASING] + "'>"
                    + "</div>"                
                + "</div>"                
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING] + "'></i>"
                    + " Return Asing <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING] + "' value='" + materialNilaiTukarEmas.getReturnAsing()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING_RUSAK] + "'></i>"
                    + " Return Asing Rusak <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING_RUSAK] + "' value='" + materialNilaiTukarEmas.getReturnAsingRusak()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_RETURN_ASING_RUSAK] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_PESANAN] + "'></i>"
                    + " Pesanan <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_PESANAN] + "' value='" + materialNilaiTukarEmas.getPesanan()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_PESANAN] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR] + "'></i>"
                    + " Tarif Retur <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR] + "' value='" + materialNilaiTukarEmas.getTarifRetur()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR] + "'>"
                    + "</div>"
                + "</div>"
                + "<div class='form-group'>"
                    + "<label class='col-sm-5'>"
                    + "<i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right' title='" + FrmMaterialNilaiTukarEmas.fieldQuestion[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN] + "'></i>"
                    + " Tarif Retur > 1thn <i style='color:red'>(m*</i></label>"
                    + "<div class='col-sm-7'>"
                        + "<input type='text' class='form-control' name='" + FrmMaterialNilaiTukarEmas.fieldNames[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN] + "' value='" + materialNilaiTukarEmas.getTarifReturDiatasSetahun()+ "' data-required='required' data-number='true' data-alpha='false' data-special='' data-type='text' data-error-message='" + FrmMaterialNilaiTukarEmas.fieldError[FrmMaterialNilaiTukarEmas.FRM_FIELD_TARIF_RETUR_DIATAS_SETAHUN] + "'>"
                    + "</div>"
                + "</div>"
            + "</div>"
        + "</div>";
        return returnData;
    }
    
    /////////////////FORM LOG HISTORY////////////////////////////////////
    public String showFormLog (HttpServletRequest request){
        LogSysHistory logSysHistory = new LogSysHistory();
        MaterialNilaiTukarEmas materialNilaiTukarEmas = new MaterialNilaiTukarEmas();
        Vector listData = PstLogSysHistory.listPurchaseOrder(0, 0, PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] +" = "+ oid, PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " DESC ");;
        String returnData = ""
        + "<div class='row'>"
            + "<div class='col-md-12'>"
                
                + "<table class='table table-bordered table-striped table-info'>"
                    + "<thead class='thead-inverse'>"
                        + "<tr>"
                            + "<th>No.</th>"
                            + "<th>Login Name</th>"
                            + "<th>User Action</th>"
                            + "<th>Update Date</th>"
                            + "<th>Application</th>"
                            + "<th>Detail</th>"
                        + "</tr>"
                    + "</thead>"
                + "<tbody>";
                                for (int i = 0; i < listData.size(); i++) {
                                logSysHistory = (LogSysHistory)listData.get(i);
                                    returnData += "<tr>"
                                            + "<td>"+(i + 1) +"</td>"
                                            + "<td>"+ logSysHistory.getLogLoginName() +"</td>"
                                            + "<td>"+ logSysHistory.getLogUserAction() +"</td>"
                                            + "<td>"+ Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd hh:mm:ss")+"</td>"
                                            + "<td>"+ logSysHistory.getLogApplication()+"</td>"
                                            + "<td>"+ logSysHistory.getLogDetail()+"</td>"
                                            + "</tr>";
                               }
                
                returnData += "</tbody>"
                                                        
                 + "</table>"
        

                + "</div>"
            + "</div>"
        + "</div>";
        
        
            return returnData;
    }

    public String saveNilaiTukarEmas(HttpServletRequest request) {
        String returnData = "";
        CtrlMaterialNilaiTukarEmas ctrlMaterialNilaiTukarEmas = new CtrlMaterialNilaiTukarEmas(request);
        try {
            ctrlMaterialNilaiTukarEmas.action(iCommand, oid, Long.parseLong(userid), username);
        } catch (Exception ex) {
        }
        returnData = ctrlMaterialNilaiTukarEmas.getMessage();
        return returnData;
    }

    public String deleteAll(HttpServletRequest request) {
        String returnData = "";
        CtrlMaterialNilaiTukarEmas ctrlMaterialNilaiTukarEmas = new CtrlMaterialNilaiTukarEmas(request);
        try {
            ctrlMaterialNilaiTukarEmas.action(iCommand, oid);
        } catch (Exception ex) {
            Logger.getLogger(AjaxNilaiTukarEmas.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnData = ctrlMaterialNilaiTukarEmas.getMessage();
        return returnData;
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
