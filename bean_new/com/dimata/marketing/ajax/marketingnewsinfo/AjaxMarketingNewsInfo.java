/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.marketing.ajax.marketingnewsinfo;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.marketing.entity.marketingnewsinfo.PstMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.CtrlMarketingNewsInfo;
import com.dimata.marketing.form.marketingnewsinfo.FrmMarketingNewsInfo;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 * @author Witar
 */
public class AjaxMarketingNewsInfo 
extends HttpServlet{
    
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

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    public static final String textListHeader[][] = {
        {"Berita dan Informasi","Judul","Tgl Mulai","Tgl Berakhir","Keterangan","Plih Gambar","Pilih Lokasi","Lokasi"},
        {"News and Info","Title","Valid Start", "Valid End","Description","Select Picture","Select Location","Location"}
    };
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //LONG
        this.oid = FRMQueryString.requestLong(request, "FRM_FIELD_OID");
        this.oidReturn = 0;
        
         //STRING
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.oidDelete = FRMQueryString.requestString(request, "FRM_FIELD_OID_DELETE");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.htmlReturn = "";

        //INT
        this.iCommand = FRMQueryString.requestCommand(request);
        this.iErrCode = 0;

        //OBJECT
        this.jSONObject = new JSONObject();
        
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
            this.jSONObject.put("FRM_FIELD_DATE_START", this.dateStart);
            this.jSONObject.put("FRM_FIELD_DATE_END", this.dateEnd);
        } catch (JSONException jSONException) {
            jSONException.printStackTrace();
        }

        response.getWriter().print(this.jSONObject);
    }
    
    public void commandList(HttpServletRequest request, HttpServletResponse response) {
        if (this.dataFor.equals("list")) {
            String[] cols = {
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_PROMO_ID],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END],
                PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION]
            };
            jSONObject = listDataTables(request, response, cols, this.dataFor, this.jSONObject);
        }
    }
    
    public void commandNone(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = showForm(request);
        }else if (this.dataFor.equals("upload")){
            this.htmlReturn = showFormUpload(request);
        }
    }
    public void commandSave(HttpServletRequest request) {
        if (this.dataFor.equals("showform")) {
            this.htmlReturn = saveMarketingNews(request);
        }else if (this.dataFor.equals("upload")){
            this.htmlReturn = saveUploadImage(request);
        }
    }
    
    public void commandDelete(HttpServletRequest request){
        if (this.dataFor.equals("delete")){
            this.htmlReturn = deleteMarketingNews(request);
        }
    }
    
    public String saveMarketingNews(HttpServletRequest request) {
        String returnData = "";
        CtrlMarketingNewsInfo ctrlMarketingNewsInfo = new CtrlMarketingNewsInfo(request);
        ctrlMarketingNewsInfo.action(iCommand, oid, oidDelete);
        returnData = ctrlMarketingNewsInfo.getMessage();
        return returnData;
    }
    
    public String deleteMarketingNews(HttpServletRequest request){
        String returnData = "";
        CtrlMarketingNewsInfo ctrlMarketingNewsInfo = new CtrlMarketingNewsInfo(request);
        ctrlMarketingNewsInfo.action(iCommand, oid, oidDelete);
        returnData = ctrlMarketingNewsInfo.getMessage();
        return returnData;
    }
    
    public String showFormUpload (HttpServletRequest request){
        String returnData = "";
        int language = FRMQueryString.requestInt(request,"FRM_FIELD_LANGUAGE");
        returnData+= ""
        + "<input type='hidden' name='"+FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_PROMO_ID]+"' value='"+oid+"'>"
        + "<input type='file' id='filename' name='filename' style='width:0px;'>"
        + "<input type='hidden' name='basename' id='basename' value=''>"
            + "<div class='row'>"
                + "<div class='col-md-12'>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][5]+"</label>"
                        + "<div class='input-group'>" 
                            + "<input class='form-control clickSearchImage fakeinput' type='text'>" 
                            +"<div class='input-group-btn'>" 
                                + " <button type='button' class='btn btn-default clickSearchImage'>&nbsp;<i class='fa fa-file-photo-o'></i>&nbsp;</button>" 
                            + "</div>" 
                        + "</div>"
                    + "</div>"
                    + "<div class='form-group'>"
                        + "<div class='input-group'>" 
                            + "<div id='imgPrev'>"
                            + "</div>"
                        + "</div>"
                    + "</div>"
                + "</div>"
            + "</div>";
        
        return returnData;
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
                whereClause += ""
                    + "AND (mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                    + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] +" LIKE '%"+searchTerm+"%'"
                    + ")";
            } else {
                whereClause += " "
                    + "(mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                    + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                    + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] +" LIKE '%"+searchTerm+"%'"
                    + ")";
            }
        }

        String colName = cols[col];
        int total = -1;

        if (dataFor.equals("list")) {
            total = PstMarketingNewsInfo.getCountJoinLocation(whereClause);
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
        MarketingNewsInfo marketingNewsInfo = new MarketingNewsInfo();
        String whereClause = "";
        String order = "";
        boolean privUpdate = FRMQueryString.requestBoolean(request, "privUpdate");
        String appRoot = FRMQueryString.requestString(request, "FRM_FLD_APP_ROOT");

        if (this.searchTerm == null) {
            whereClause += "";
        } else {
            if (datafor.equals("list")) {
                if (whereClause.length() > 0) {
                    whereClause += ""
                        + "AND (mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] +" LIKE '%"+searchTerm+"%'"
                        + ")";
                } else {
                    whereClause += " "
                        + "(mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_TITLE] + " LIKE '%" + searchTerm + "%' "
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START] + " LIKE '%" + searchTerm + "%'"
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END] + " LIKE '%" + searchTerm + "%'"
                        + " OR mni." + PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_DESCRIPTION] + " LIKE '%" + searchTerm + "%'"
                        + " OR loc." + PstLocation.fieldNames[PstLocation.FLD_NAME] +" LIKE '%"+searchTerm+"%'"
                        + ")";
                }
            }
        }

        if (this.colOrder >= 0) {
            order += "" + colName + " " + dir + "";
        }

        Vector listData = new Vector(1, 1);
        if (datafor.equals("list")) {
            listData = PstMarketingNewsInfo.listJoinLocation(start, amount, whereClause, order);
        }

        for (int i = 0; i <= listData.size() - 1; i++) {
            JSONArray ja = new JSONArray();
            if (datafor.equals("list")) {
                marketingNewsInfo = (MarketingNewsInfo) listData.get(i);
                ja.put("" + (this.start + i + 1));
                ja.put("" + marketingNewsInfo.getTitle());
                ja.put("" + Formater.formatDate(marketingNewsInfo.getValidStart(),"dd/MM/yyyy"));
                ja.put("" + Formater.formatDate(marketingNewsInfo.getValidEnd(),"dd/MM/yyyy"));
                ja.put("" + marketingNewsInfo.getDescription());
                ja.put("<img style='width:100px;' src='"+appRoot+"/imgupload/marketing/"+marketingNewsInfo.getOID()+".jpg'>");
                ja.put("" + marketingNewsInfo.getLocationName());
                ja.put(""
                    + "<button type='button' class='btn btn-warning btneditgeneral' data-oid='" + marketingNewsInfo.getOID() + "' data-for='showform'><i class='fa fa-pencil'></i> Edit</button> &nbsp;"
                    + "<button class='btn btn-danger button-delete' type='button' data-oid='" + marketingNewsInfo.getOID() + "' data-for='delete' data-command = '" + Command.DELETE + "'>Delete</button> &nbsp;"
                    + "<button class='btn btn-info button-upload' type='button' data-oid='" + marketingNewsInfo.getOID() + "' data-for='upload' data-command = '" + Command.NONE + "'>Upload Image</button> "
                    + "");

                array.put(ja);
            }
        }

        totalAfterFilter = total;

        try {
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("iUrlToGetPicture","imgupload/marketing/");
            result.put("aaData", array);
        } catch (Exception e) {

        }

        return result;
    }
    
    public String showForm(HttpServletRequest request) {
        MarketingNewsInfo marketingNewsInfo = new MarketingNewsInfo();
        if (oid != 0) {
            try {
                marketingNewsInfo = PstMarketingNewsInfo.fetchExc(oid);
            } catch (Exception e) {
            }
        }
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        
        Vector val_locationid = new Vector(1,1);
        Vector key_locationid = new Vector(1,1); 
        Vector vt_loc = PstLocation.list(0, 0, "", "");
        
        val_locationid.add("0");
        key_locationid.add(""+textListHeader[language][6]+"");
        
        for(int d=0;d<vt_loc.size();d++){
            Location loc = (Location)vt_loc.get(d);
            val_locationid.add(""+loc.getOID()+"");
            key_locationid.add(loc.getName());
        }
        
        
        String returnData = ""
            + "<input type='hidden' name='"+FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_PROMO_ID]+"' value='"+marketingNewsInfo.getOID()+"'>"
            + "<div class='row'>"
                + "<div class='col-md-12'>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][1]+"</label>"
                        + "<input type='text' class='form-control ' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_TITLE] + "' value='" + marketingNewsInfo.getTitle()+ "'>"
                    + "</div>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][2]+"</label>"
                        + "<input type='text' class='form-control  dates' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_START] + "' value='" + Formater.formatDate(marketingNewsInfo.getValidStart(),"dd/MM/yyyy")+ "'>"
                    + "</div>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][3]+"</label>"
                        + "<input type='text' class='form-control  dates' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_END] + "' value='" + Formater.formatDate(marketingNewsInfo.getValidEnd(),"dd/MM/yyyy")+ "'>"
                    + "</div>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][4]+"</label>"
                        + " <textarea class='form-control' rows='5' required name='" + FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_DESCRIPTION] + "'>"+marketingNewsInfo.getDescription()+"</textarea>"
                    + "</div>"
                    + "<div class='form-group'>"
                        + "<label>"+textListHeader[language][7]+"</label>"
                        + " "+ControlCombo.draw(FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_LOCATION_ID], null, ""+marketingNewsInfo.getLocationId()+"", val_locationid, key_locationid, "", "form-control")+""
                    + "</div>"
                + "</div>"
            + "</div>";

        return returnData;
    }
    
    public String saveUploadImage(HttpServletRequest request){
        String returnData= "";
        
        String appRoot = FRMQueryString.requestString(request, "FRM_FIELD_APP_ROOT");
        String basename = FRMQueryString.requestString(request, "basename");
        long oidNews = oid;
        String realPath =FRMQueryString.requestString(request, "FRM_FIELD_REAL_PATH");
        
        basename = basename.replaceAll("data:image/png;base64,","");
        basename = basename.replaceAll("data:image/jpg;base64,","");
        basename = basename.replaceAll("data:image/jpeg;base64,","");
        String namaImage =""+ oidNews +".jpg"; 
       
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
    }
    
}
