/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.webservice;

import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.marketing.entity.marketingnewsinfo.MarketingNewsInfo;
import com.dimata.marketing.entity.marketingnewsinfo.PstMarketingNewsInfo;
import com.dimata.marketing.entity.marketingpromotion.MarketingPromotion;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebServices extends HttpServlet{
    
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
        this.iErrCode = 0;        
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
	    
            case Command.LIST :
                commandList(request);
            break;
                
             case Command.SAVE :
                commandSave(request,response);
            break;

	    default : commandNone(request,response);
	}
        
        try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(this.jSONObject);
    }
    
    public void commandNone(HttpServletRequest request,HttpServletResponse response){
	
    }
    
    public void commandSave(HttpServletRequest request,HttpServletResponse response){
        
    }
    
    public void commandList(HttpServletRequest request){
        if(dataFor.equals("showPromotion")){
            getListPromotion(request);
        }
    }
    
    public void getListPromotion(HttpServletRequest request){
        //WHERE CLAUSE
        String whereClause = "";
        String dateStart = FRMQueryString.requestString(request, "startDate");
        String dateEnd = FRMQueryString.requestString(request, "endDate");
        int showPage = FRMQueryString.requestInt(request, "showPage");
        int limit = FRMQueryString.requestInt(request, "limit");
        int currPage = showPage;
        if(showPage > 0){
            showPage-=1;
        }else{
            currPage+=1;
        }
        long locationId = FRMQueryString.requestLong(request, "location");
        String filterDate = FRMQueryString.requestString(request, "filterDate");
        
        if(dateStart.length() == 0){
            dateStart = Formater.formatDate(new Date(),"yyyy-MM-dd");
        }
        if(dateEnd.length() == 0){
            dateEnd = Formater.formatDate(new Date(), "yyyy-MM-dd");
        }
        
        if(filterDate.length() == 0){
            filterDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
        }
        whereClause+=""
        + "("
            + ""+PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_LOCATION_ID]+"='"+locationId+"'"
            + "OR "+PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_LOCATION_ID]+"='0'"
        + ") "
        + "AND "+PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_END]+">='"+filterDate+"' "
        + "AND "+PstMarketingNewsInfo.fieldNames[PstMarketingNewsInfo.FLD_VALID_START]+"<='"+filterDate+"' ";
        //GET TOTAL RECORD
        int totalRecord = PstMarketingNewsInfo.getCount(whereClause);
        //JUMLAH PAGE
        int jumlahPage = (totalRecord+limit-1)/limit;
        try {
            jSONArray = new JSONArray();
            Vector listData = PstMarketingNewsInfo.list(showPage, limit, whereClause, "");
            if(listData.size() > 0){
                for(int i = 0; i < listData.size(); i++){
                    MarketingNewsInfo marketingNewsInfo = (MarketingNewsInfo) listData.get(i);
                    JSONArray data = new JSONArray();
                    data.put(""+marketingNewsInfo.getOID());
                    data.put(""+marketingNewsInfo.getTitle());
                    data.put(""+Formater.formatDate(marketingNewsInfo.getValidStart(),"yyyy-MM-dd"));
                    data.put(""+Formater.formatDate(marketingNewsInfo.getValidEnd(),"yyyy-MM-dd"));
                    data.put(""+marketingNewsInfo.getDescription());
                    jSONArray.put(data);
                }
            }
            jSONObject.put("totalRecord", ""+totalRecord);
            jSONObject.put("jumlahPage", ""+jumlahPage);
            jSONObject.put("dateFilter",filterDate);
            jSONObject.put("curpage", currPage);
            jSONObject.put("totalData", totalRecord);
            jSONObject.put("data", jSONArray);
            
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
        processRequest(request, response);
    }
    
}
