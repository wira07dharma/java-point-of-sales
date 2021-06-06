/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.ajax;

import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.marketing.entity.marketingmanagement.CtrlMarketingManagement;
import com.dimata.marketing.form.marketingpromotion.CtrlMarketingPromotion;
import com.dimata.posbo.entity.marketing.MarketingCatalog;
import com.dimata.posbo.entity.marketing.MarketingCatalogDetail;
import com.dimata.posbo.entity.marketing.MarketingManagement;
import com.dimata.posbo.entity.marketing.PstMarketingCatalog;
import com.dimata.posbo.entity.marketing.PstMarketingCatalogDetail;
import com.dimata.posbo.entity.marketing.PstMarketingManagement;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Sunima
 */
public class AjaxMarketing extends HttpServlet{
    
    // Data Table 
    private String searchTerm;
    private String colName;
    private int colOrder;
    private String dir;
    private int start;
    private int amount;
    
    
    //Object
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    // long 
    long oid = 0;
    private long oidReturn = 0;
    private long oidKatalog = 0;
    
    // String 
    private String dataFor = "";
    private String approot = "";
    private String htmlReturn = "";
    
    // Int
    private int iCommand = 0;
    private int iErrCode = 0;
    
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    
        // Long 
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_LONG");
        this.oidReturn = 0;
        
        // String datafor
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";
        this.oidKatalog = 0;
        
        // Int
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;
        
        // Object
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
        
            case Command.SAVE:
                commandSave(request);
                break;
                
            case Command.LIST:
                commandList(request, response);
                break;
            
                
            case Command.DELETE:
                commandDelete(request);
                break;
                
            default:
                commandNone(request);
                
        }
        try{
            this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
            this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
            this.jSONObject.put("FRM_OID_RETURN_KATALOG", this.oidKatalog);
        }catch(JSONException jSONException){
            jSONException.printStackTrace();
        }
        
        response.getWriter().print(this.jSONObject);
    }
    
    public void commandNone(HttpServletRequest request){
        
    }
    
    public void commandSave(HttpServletRequest request){
        if(this.dataFor.equals("saveadd")) {
            saveForm(request);
        }else if(this.dataFor.equals("saveKatalogItem")){
            this.htmlReturn = saveKatalogDetail(request);
        }else if(this.dataFor.equals("saveKatalog")){
            saveKatalog(request);
        }
    }
    
    public void commandDelete(HttpServletRequest request){
        if(this.dataFor.equals("delete")){
            this.htmlReturn = delete(request);
        }else if(this.dataFor.equals("deleteItemCatalog")){
            deleteItemCatalog(request);
        }else if(this.dataFor.equals("deleteCatalog")){
            deleteCatalog(request);
        }
    }

    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("list")) {
            String[] cols = {
                PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID],
                PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID],
                PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_MATERIAL_CATEGORY_ID],
                PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_MATERIAL_ID]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }
    
       public JSONObject listDataTables(HttpServletRequest request, HttpServletResponse response, String[] cols, String dataFor, JSONObject result) {
        this.searchTerm = FRMQueryString.requestString(request, "SEND_OID_KATALOG");
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
                whereClause += "AND (" + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID] + " LIKE '%" + searchTerm + "%')";
                        
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstMarketingCatalogDetail.getCount(whereClause);
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
        MarketingCatalogDetail marketingCatalogDetail = new MarketingCatalogDetail();
        String whereClause = "";
        String order = "";

        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else if (datafor.equals("list")) {

            if (whereClause.length() > 0) {
                whereClause += "AND (" + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID] + " LIKE '%" + searchTerm + "%')";
            } else {
                whereClause += " (" + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_DETAIL_ID] + " LIKE '%" + searchTerm + "%' "
                        + " OR " + PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID] + " LIKE '%" + searchTerm + "%')";
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("list")) {
            listData = PstMarketingCatalogDetail.list(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("list")) {
                String buttonAction = "";
                marketingCatalogDetail = (MarketingCatalogDetail) listData.get(i);
                String itemName ="";
                String sku = "";
                String kategoryName = "";
                long oidMaterial = marketingCatalogDetail.getMarketing_material_id();
                long oidCategory = marketingCatalogDetail.getMarketing_material_category_id();
                try{
                    Material material = new Material();
                    material = PstMaterial.fetchExc(oidMaterial);
                    itemName = material.getName();
                    sku = material.getSku();
                }catch(Exception e){}
                try{
                    Category category = new Category();
                    category= PstCategory.fetchExc(oidCategory);
                    kategoryName = category.getName();
                }catch(Exception e){}
                
                ja.put("" + (this.start + i + 1));
                ja.put("" +itemName);
                ja.put("" + sku);
                ja.put("" + kategoryName);
                buttonAction = "<div class=\"text-center\">";
                if (true) {
                    buttonAction += "<button type='button' class='btn btn-sm btn-warning btn_edit_item' data-oid='" +oidMaterial +"'><i class='fa fa-pencil'></i> Edit</button> &nbsp;";
                }
                if (true) {
                    buttonAction += "<button class='btn btn-sm btn-danger btn_delete_item' type='button' data-oid='" + marketingCatalogDetail.getOID() + "'>Delete</button>";
                }
                buttonAction += "</div>";
                ja.put(buttonAction);

                array.put(ja);
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
    
    public String saveKatalogDetail(HttpServletRequest request){
        long oidMaterial = FRMQueryString.requestLong(request, "MATERIAL_OID");
        long oidCategory = FRMQueryString.requestLong(request, "CATEGORY_OID");
        String oidCatalog  = FRMQueryString.requestString(request, "OID_CATALOG");
        if(Long.parseLong(oidCatalog) > 0){
            try{
            MarketingCatalogDetail marketingCatalogDetail = new MarketingCatalogDetail();
            PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail();
            
            marketingCatalogDetail.setMarketing_catalog_id(Long.parseLong(oidCatalog));
            marketingCatalogDetail.setMarketing_material_id(oidMaterial);
            marketingCatalogDetail.setMarketing_material_category_id(oidCategory);
            pstMarketingCatalogDetail.insertExc(marketingCatalogDetail);
        }catch(Exception e){
        }
        }else{
         oidCatalog = Long.toString(saveKatalog(request));
        try{
            MarketingCatalogDetail marketingCatalogDetail = new MarketingCatalogDetail();
            PstMarketingCatalogDetail pstMarketingCatalogDetail = new PstMarketingCatalogDetail();
            
            marketingCatalogDetail.setMarketing_catalog_id(Long.parseLong(oidCatalog));
            marketingCatalogDetail.setMarketing_material_id(oidMaterial);
            marketingCatalogDetail.setMarketing_material_category_id(oidCategory);
            pstMarketingCatalogDetail.insertExc(marketingCatalogDetail);
        }catch(Exception e){
        }
        }
        
        return oidCatalog;
    }
    
    public long saveKatalog(HttpServletRequest request){
       long oidCatalog = FRMQueryString.requestLong(request, "OID_CATALOG");
        String catalogTitle = FRMQueryString.requestString(request, "FRM_CATALOG_TITLE");
        Date startDate = Formater.formatDate(FRMQueryString.requestString(request, "FRM_START_DATE"), "yyyy-MM-dd");
        Date endDate = Formater.formatDate(FRMQueryString.requestString(request, "FRM_END_DATE"), "yyyy-MM-dd");
        int Status = FRMQueryString.requestInt(request, "FRM_STATUS");
        
        long oidReturn = 0;
        
        if(oidCatalog > 0){
            try{
            MarketingCatalog marketingCatalog = new MarketingCatalog();
            PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog();
            try{
                marketingCatalog = pstMarketingCatalog.fetchExc(oidCatalog);
            }catch(Exception e){}
                    
            marketingCatalog.setMarketing_katalog_title(catalogTitle);
            marketingCatalog.setMarketing_katalog_start_date(startDate);
            marketingCatalog.setMarketing_katalog_end_date(endDate);
            marketingCatalog.setMarketing_katalog_status(Status);
            pstMarketingCatalog.updateExc(marketingCatalog);
            
        }catch(Exception e){
        }
        }else{
            try{
            MarketingCatalog marketingCatalog = new MarketingCatalog();
            PstMarketingCatalog pstMarketingCatalog = new PstMarketingCatalog();
            
            marketingCatalog.setMarketing_katalog_title(catalogTitle);
            marketingCatalog.setMarketing_katalog_start_date(startDate);
            marketingCatalog.setMarketing_katalog_end_date(endDate);
            marketingCatalog.setMarketing_katalog_status(Status);
            pstMarketingCatalog.insertExc(marketingCatalog);
            oidReturn = marketingCatalog.getOID();
            try{
                
            }catch(Exception e){
            }
        }catch(Exception e){
        }
        }
       return oidReturn; 
    }
    
    public void deleteCatalog(HttpServletRequest request){
        long oid = FRMQueryString.requestLong(request, "OID_CATALOG");
        Vector listDelete = PstMarketingCatalogDetail.list(0, 0, PstMarketingCatalogDetail.fieldNames[PstMarketingCatalogDetail.FLD_MARKETING_KATALOG_ID]+"="+oid, "");
        for(int i = 0; i < listDelete.size(); i++){
            try{
            MarketingCatalogDetail marketingCatalogDetail = (MarketingCatalogDetail) listDelete.get(i);
            long oidDelete = marketingCatalogDetail.getOID();
            PstMarketingCatalogDetail.deleteExc(oidDelete);
            }catch(Exception e){
            }
            
        }
        try{
        
        PstMarketingCatalog.deleteExc(oid);
        }catch(Exception e){
        }
    }
    
    public void deleteItemCatalog(HttpServletRequest request){
        long oidItemCatalog = FRMQueryString.requestLong(request, "ITEM_CATALOG_OID");
        try{
            PstMarketingCatalogDetail.deleteExc(oidItemCatalog);
        }catch(Exception e){
        }
    }
    
    public int saveForm(HttpServletRequest request){
        String marketingTitle = FRMQueryString.requestString(request, "FRM_MARKETING_MANAGEMENT_TITLE");
        int marketingStatus = FRMQueryString.requestInt(request, "FRM_MARKETING_MANAGEMENT_STATUS");
        long Oid = FRMQueryString.requestLong(request, "OID");
        int cmdView = FRMQueryString.requestInt(request, "CMD_VIEW");
        String marketingDescription = FRMQueryString.requestString(request, "FRM_MARKETING_MANAGEMENT_DESCRIPTION");
        String sdate = FRMQueryString.requestString(request, "FRM_MARKETING_MANAGEMENT_START_DATE");
        String edate = FRMQueryString.requestString(request, "FRM_MARKETING_MANAGEMENT_END_DATE");
        String marketingNote = FRMQueryString.requestString(request, "FRM_MARKETING_MANAGEMENT_NOTE");
        Date startDate = Formater.formatDate(sdate, "yyyy-MM-dd");
        Date endDate = Formater.formatDate(edate, "yyyy-MM-dd");
        Date allDate = new Date();
        
        if(Oid != 0){
        
        try{
            MarketingManagement entMarketingManagement = new MarketingManagement();
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement();
            
            entMarketingManagement = PstMarketingManagement.fetchExc(Oid);
            saveUploadImage(request, Oid);
            
            entMarketingManagement.setMarketing_title(marketingTitle);
            entMarketingManagement.setMarketing_description(marketingDescription);
            entMarketingManagement.setStart_date(startDate);
            entMarketingManagement.setEnd_date(endDate);
            entMarketingManagement.setMarketing_status(marketingStatus);
            entMarketingManagement.setEdited_date(allDate);
            if(cmdView == Command.VIEW){
            entMarketingManagement.setChecked_date(allDate);
            }
            entMarketingManagement.setMarketing_note(marketingNote);
            pstMarketingManagement.updateExc(entMarketingManagement);
        }catch(Exception e){
            
        }
        }else{
            try{
            MarketingManagement entMarketingManagement = new MarketingManagement();
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement();
            
            entMarketingManagement.setMarketing_title(marketingTitle);
            entMarketingManagement.setMarketing_description(marketingDescription);
            entMarketingManagement.setStart_date(startDate);
            entMarketingManagement.setEnd_date(endDate);
            entMarketingManagement.setMarketing_status(marketingStatus);
            entMarketingManagement.setCreate_date(allDate);
            pstMarketingManagement.insertExc(entMarketingManagement);
            
            try{
                long OidNew = entMarketingManagement.getOID();
                saveUploadImage(request, OidNew);
            }catch(Exception e){
            
            }
        }catch(Exception e){
        }
        }
        return 1;
        
    }
    
    public String delete(HttpServletRequest request){
        String returnData = "";
        long Oid = FRMQueryString.requestLong(request, "OID");
        try{
            MarketingManagement entMarketingManagement = new MarketingManagement();
            PstMarketingManagement pstMarketingManagement = new PstMarketingManagement();
            
            entMarketingManagement = PstMarketingManagement.fetchExc(Oid);
            pstMarketingManagement.deleteExc(entMarketingManagement);
        }catch(Exception e){
        }
        return returnData;
    }
    
    public String saveUploadImage(HttpServletRequest request, long Oid){
        String returnData= "";
        String basename = FRMQueryString.requestString(request, "basename");
        String realPath =FRMQueryString.requestString(request, "FRM_FIELD_REAL_PATH");
        
        basename = basename.replaceAll("data:image/png;base64,","");
        basename = basename.replaceAll("data:image/jpg;base64,","");
        basename = basename.replaceAll("data:image/jpeg;base64,","");
        String namaImage =""+Oid+".jpg"; 
       
        BufferedImage newImg;
        newImg = decodeToImage(basename);
        
        String url = "";
        
        try {
            ImageIO.write(newImg, "jpg",new File(""+getServletContext().getRealPath("imgupload/marketing")+"/"+namaImage+""));
        } catch (Exception e) {
        }
        
        
        return returnData;
    }
    
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
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
