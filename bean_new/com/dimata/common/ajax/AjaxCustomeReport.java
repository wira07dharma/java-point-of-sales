/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.ajax;

import com.dimata.common.session.chart.SessGenerateChart;
import com.dimata.posbo.entity.search.SrcReportSale;
import com.dimata.posbo.entity.search.SrcSaleReport;
import com.dimata.posbo.form.search.FrmSrcReportSale;
import com.dimata.posbo.report.sale.SaleReportDocument;
import com.dimata.posbo.report.sale.SaleReportItem;
import com.dimata.posbo.session.warehouse.SessReportSale;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * @author dimata005
 */
public class AjaxCustomeReport extends HttpServlet {

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
    
    //LONG
    private long oid = 0;
    private long oidReturn = 0;
    
    //STRING
    private String dataFor = "";
    private String oidDelete = "";
    private String approot = "";
    private String htmlReturn = "";
    
    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    ArrayList<String> listCategories = new ArrayList<String>();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");	
        String chartType = FRMQueryString.requestString(request, "FRM_FIELD_CHART_TYPE");
        String dataChart = FRMQueryString.requestString(request, "FRM_FIELD_CHART_DATA_FOR");
        String returnHTML = "";
        String returnChart = "";
        String whereClause = "";
	
	//INT
	this.iCommand = FRMQueryString.requestCommand(request);
        
        //JSONOBJECT
	JSONObject colors = new JSONObject();
	JSONArray colorArrays = new JSONArray();
	try{
	    colors.put("orange", "#ED7D31");
	    colors.put("blue","#5B9BD5");
	    colors.put("yellow","#FFC000");
	    colors.put("gray","#A5A5A5");
	    colors.put("green","#70AD47");
	    colors.put("blackGray","#7F7F7F");
	    colorArrays.put("#ED7D31");
	    colorArrays.put("#5B9BD5");
	    colorArrays.put("#FFC000");
	    colorArrays.put("#A5A5A5");
	    colorArrays.put("#70AD47");
	    colorArrays.put("#7F7F7F");
	}catch(Exception ex){
	}
        
        JSONArray seriesDatas = new JSONArray();
	JSONObject seriesData = new JSONObject();
	JSONObject chart = new JSONObject();
	JSONObject chartData = new JSONObject();
	JSONArray chartDatas = new JSONArray();
	JSONArray categories = new JSONArray();
	JSONObject categoriesTitle = new JSONObject();
	JSONObject chartOptions = new JSONObject();
	JSONObject titleChart = new JSONObject();
	JSONObject subtitleChart = new JSONObject();
	JSONObject xAxisChart = new JSONObject();
	JSONObject yAxisChart = new JSONObject();
	JSONObject tooltipChart = new JSONObject();
	JSONObject plotOptions = new JSONObject();
        
        //ARRAYLIST DATA
	ArrayList<Double> datas = new ArrayList<Double>();
        
        //generate
        SessGenerateChart sessGenerateChart = new SessGenerateChart();
        
        if (iCommand==Command.NONE){
            if (dataChart.equals("chartPlace")){
                try {                   
                    //
                    SrcReportSale srcReportSale = new SrcReportSale();
                    SessReportSale sessReportSale = new SessReportSale();
                    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
        
                    frmSrcReportSale.requestEntityObject(srcReportSale);
                    String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID]+"");
                    String locationMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID]+"");
                    String categoryInText="";
                    String locationInText="";
                    try {
                        for (int a = 0; a<categoryMultiple.length;a++){
                            if (categoryInText.length()>0){
                                categoryInText = categoryInText + "," + categoryMultiple[a];
                            }else{
                                categoryInText =  categoryMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }
                    
                    try {
                        for (int a = 0; a<locationMultiple.length;a++){
                            if (locationInText.length()>0){
                                locationInText = locationInText + "," + locationMultiple[a];
                            }else{
                                locationInText =  locationMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }
                    
                    
                    //String dateFrom ="2017-09-01";
                    //String dateTo = "2017-09-01";

                    //Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
                    //Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
                    Date dateNow = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateNow);
                    int month = calendar.get(Calendar.MONTH);
                    int lastDate = calendar.getActualMaximum(Calendar.DATE);
                    int firstDate = calendar.getActualMinimum(Calendar.DATE);
                    
                    Date tglmulai = new Date();
                    Date tglberakhir =new Date();
                    tglmulai.setMonth(dateNow.getMonth());
                    tglmulai.setDate(firstDate);
                    
                    tglberakhir.setMonth(dateNow.getMonth());
                    tglberakhir.setDate(lastDate);
                    
                    srcReportSale.setDateFrom(tglmulai);
                    srcReportSale.setDateTo(tglberakhir);


                    srcReportSale.setCategoryMultiple(categoryInText);
                    srcReportSale.setLocationMultiple(locationInText);
                    Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
                    
                    String categoryName [] = {"Total Jual","Total HPP","Total Profit","Total Tax","Total Service","Rata-Rata Harian"};
        
                    //CATCH 
                    int language = FRMQueryString.requestInt(request, "language");
                    
                    //MENAMBAHKAN TANGGAL
                    listCategories.clear();
                    for (int k = 0; k<records.size();k++){
                        Vector rowx = new Vector();				
                        Vector vt = (Vector)records.get(k);
                        String tanggal = (String)vt.get(0);                                            
			listCategories.add(tanggal);
                    }
                    
                    //GENERATE CHART
                    sessGenerateChart = new SessGenerateChart();
                    sessGenerateChart.generateLineChartMonth(chartType, dataChart, "", "", "", chart, listCategories, "");
                    
                    //DATA OBJECT
		    JSONArray pencapaianObject = new JSONArray();
		    JSONArray targetObject = new JSONArray();
		    JSONArray selisihObject = new JSONArray();
		    ArrayList<Object> dataObjectPencapaians = new ArrayList<Object>();
		    ArrayList<Object> dataObjectTargets = new ArrayList<Object>();
                    
                    double totalRata= 0;
                    double rata=0;
                    
                    Vector rataVector = new Vector(1,1);
                    
                    if(records.size() != 0){
                        for (int i = 0; i<categoryName.length;i++){
                            int select = i +1;
                            try{
                                seriesData = new JSONObject();
				pencapaianObject = new JSONArray();
				targetObject = new JSONArray();
				selisihObject = new JSONArray();
				seriesData.put("name", ""+categoryName[i]);                               
                                seriesData.put("color", ""+colorArrays.get(i));
                                
                                for (int j = 0; j<records.size();j++){
                                    double totalValue =0;
                                    if (i!=5){
                                        Vector rowx = new Vector();				
                                        Vector vt = (Vector)records.get(j);                                   
                                        totalValue = (Double)vt.get(select);
                                    }
                                    
                                    
                                    if (i==0){
                                        totalRata = totalRata + totalValue;
                                        rata = totalRata/(j+1);
                                        rataVector.add(""+rata);
                                    }
                                    if (i==5){
                                        Double temp = Double.parseDouble((String)rataVector.get(j));
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(temp,"#.##")));
                                    }else{
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(totalValue,"#.##")));
                                    }
                                    
                                }
                                
                                seriesData.put("data", pencapaianObject);
				seriesDatas.put(seriesData);
                                
                            }catch(Exception ex){

                            }
                        }
                        
                    }
                    
                }catch(Exception e){}
                
            }else if(dataChart.equals("chartPlaceDua")){
                
                   try { 
                    SrcReportSale srcReportSale = new SrcReportSale();
                    SessReportSale sessReportSale = new SessReportSale();
                    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
        
                    frmSrcReportSale.requestEntityObject(srcReportSale);
                    String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID]+"");
                    String locationMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID]+"");
                    String categoryInText="";
                    String locationInText="";
                    try {
                        for (int a = 0; a<categoryMultiple.length;a++){
                            if (categoryInText.length()>0){
                                categoryInText = categoryInText + "," + categoryMultiple[a];
                            }else{
                                categoryInText =  categoryMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }
                    
                    try {
                        for (int a = 0; a<locationMultiple.length;a++){
                            if (locationInText.length()>0){
                                locationInText = locationInText + "," + locationMultiple[a];
                            }else{
                                locationInText =  locationMultiple[a];
                            }
                        }
                    } catch (Exception e) {
                    }
                    
                    
                    //String dateFrom ="2017-09-01";
                    //String dateTo = "2017-09-01";

                    //Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
                    //Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
                    Date dateNow = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateNow);
                    int month = calendar.get(Calendar.MONTH);
                    int lastDate = calendar.getActualMaximum(Calendar.DATE);
                    int firstDate = calendar.getActualMinimum(Calendar.DATE);
                    
                    Date tglmulai = new Date();
                    Date tglberakhir =new Date();
                    tglmulai.setMonth(tglmulai.getMonth()-1);
                    tglmulai.setDate(firstDate);
                    
                    tglberakhir.setMonth(tglberakhir.getMonth()-1);
                    tglberakhir.setDate(lastDate);
                    
                    srcReportSale.setDateFrom(tglmulai);
                    srcReportSale.setDateTo(tglberakhir);

                    srcReportSale.setCategoryMultiple(categoryInText);
                    srcReportSale.setLocationMultiple(locationInText);
                    Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
                    
                    String categoryName [] = {"Total Jual","Total HPP","Total Profit","Total Tax","Total Service","Rata-Rata Harian"};
        
                    //CATCH 
                    int language = FRMQueryString.requestInt(request, "language");
                    
                    //MENAMBAHKAN TANGGAL
                    listCategories.clear();
                    for (int k = 0; k<records.size();k++){
                        Vector rowx = new Vector();				
                        Vector vt = (Vector)records.get(k);
                        String tanggal = (String)vt.get(0);                                            
			listCategories.add(tanggal);
                    }
                    
                    //GENERATE CHART
                    sessGenerateChart = new SessGenerateChart();
                    sessGenerateChart.generateLineChartMonth(chartType, dataChart, "", "", "", chart, listCategories, "");
                    
                    //DATA OBJECT
		    JSONArray pencapaianObject = new JSONArray();
		    JSONArray targetObject = new JSONArray();
		    JSONArray selisihObject = new JSONArray();
		    ArrayList<Object> dataObjectPencapaians = new ArrayList<Object>();
		    ArrayList<Object> dataObjectTargets = new ArrayList<Object>();
                    
                    double totalRata= 0;
                    double rata=0;
                    
                    Vector rataVector = new Vector(1,1);
                    
                    if(records.size() != 0){
                        for (int i = 0; i<categoryName.length;i++){
                            int select = i +1;
                            try{
                                seriesData = new JSONObject();
				pencapaianObject = new JSONArray();
				targetObject = new JSONArray();
				selisihObject = new JSONArray();
				seriesData.put("name", ""+categoryName[i]);                               
                                seriesData.put("color", ""+colorArrays.get(i));
                                
                                for (int j = 0; j<records.size();j++){
                                    double totalValue =0;
                                    if (i!=5){
                                        Vector rowx = new Vector();				
                                        Vector vt = (Vector)records.get(j);                                   
                                        totalValue = (Double)vt.get(select);
                                    }
                                    
                                    
                                    if (i==0){
                                        totalRata = totalRata + totalValue;
                                        rata = totalRata/(j+1);
                                        rataVector.add(""+rata);
                                    }
                                    if (i==5){
                                        Double temp = Double.parseDouble((String)rataVector.get(j));
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(temp,"#.##")));
                                    }else{
                                        pencapaianObject.put(Double.parseDouble(Formater.formatNumber(totalValue,"#.##")));
                                    }
                                    
                                }
                                
                                seriesData.put("data", pencapaianObject);
				seriesDatas.put(seriesData);
                                
                            }catch(Exception ex){

                            }
                        }
                        
                    }
                    
                }catch(Exception e){}
                
            }else if(dataChart.equals("chartPlaceItem")){
                
                //String categoryName [] = {"Africa", "America", "Asia", "Europe", "Oceania"};
                int multiLanguageName = Integer.parseInt((String)com.dimata.system.entity.PstSystemProperty.getValueIntByName("NAME_MATERIAL_MULTI_LANGUAGE"));
        
                SrcSaleReport srcSaleReport = new SrcSaleReport();
                Date dateNow = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateNow);
                int month = calendar.get(Calendar.MONTH);
                int lastDate = calendar.getActualMaximum(Calendar.DATE);
                int firstDate = calendar.getActualMinimum(Calendar.DATE);

                Date tglmulai = new Date();
                Date tglberakhir =new Date();
                tglmulai.setMonth(tglmulai.getMonth());
                tglmulai.setDate(firstDate);

                tglberakhir.setMonth(tglberakhir.getMonth());
                tglberakhir.setDate(lastDate);

                srcSaleReport.setDateFrom(tglmulai);
                srcSaleReport.setDateTo(tglberakhir);
                srcSaleReport.setViewReport(1);
                srcSaleReport.setSortBy(10);
                srcSaleReport.setTransType(6);
                Vector records = SaleReportDocument.getList(srcSaleReport,0,0,SaleReportDocument.LOG_MODE_CONSOLE);
                //MENAMBAHKAN TANGGAL
                listCategories.clear();
                //GENERATE CHART    
                int count = 0;
                if(records.size()>0){
                    if(records.size()>10){
                        count=10;
                    }else{
                        count=records.size();
                    }
                    for (int i = 0; i<count;i++){
                        SaleReportItem saleItem = (SaleReportItem) records.get(i);
                         
                            String[] smartPhonesSplits = saleItem.getItemName().split("\\;");
                            if(multiLanguageName==1){
                                listCategories.add(""+smartPhonesSplits[0]);
                            }else{
                                 listCategories.add(""+saleItem.getItemName());
                            }
                    }
                }
                
                sessGenerateChart = new SessGenerateChart();
                sessGenerateChart.generateBarChart(chartType, dataChart, "", "", "", chart, listCategories, "");
                
                //DATA OBJECT
                JSONArray pencapaianObject = new JSONArray();
                JSONArray targetObject = new JSONArray();
                JSONArray selisihObject = new JSONArray();
                ArrayList<Object> dataObjectPencapaians = new ArrayList<Object>();
                ArrayList<Object> dataObjectTargets = new ArrayList<Object>();
                
                seriesData = new JSONObject();
                pencapaianObject = new JSONArray();
                try{
                    seriesData.put("name", "Item");                               
                    seriesData.put("color", "#ED7D31");
                    if(records.size()>0){
                        for (int i = 0; i<count;i++){
                         SaleReportItem saleItem = (SaleReportItem) records.get(i);
                         pencapaianObject.put(saleItem.getTotalQty());
                        }
                    }
                    seriesData.put("data", pencapaianObject);
                    seriesDatas.put(seriesData);
                }catch(Exception ex){}
               
            }
            
            try{
                chart.put("series", seriesDatas);
            }catch(Exception ex){

            }
            
            JSONObject jSONObject = new JSONObject();
	
            try{
                jSONObject.put("CHART_DATA", chart);
                jSONObject.put("HTML_DATA",returnHTML);
            }catch(JSONException ex){
                returnChart = "{'CHART_DATA':'"+ex.toString()+"'}";
            }

            response.getWriter().println(jSONObject);
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
