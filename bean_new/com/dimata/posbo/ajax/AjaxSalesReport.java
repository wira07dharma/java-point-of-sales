/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.gui.jsp.ControlList;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.search.SrcReportSale;
import com.dimata.posbo.form.search.FrmSrcReportSale;
import com.dimata.posbo.session.warehouse.SessReportSale;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AjaxSalesReport extends HttpServlet{
    
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //STRING
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
  
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    //Array list Table Header
    public static final String textListHeader[][] = {
        {"LAPORAN REKAP HARIAN","Laporan Rekap Harian"},
        {"SUMMARY DAILY REPORT","Summary Daily Report"}
    };
    
    public static final String textListHeaderLocation[][] = {
        {"LAPORAN DETAIL REKAP HARIAN PER LOCATION","Laporan Rekap Harian"},
        {"SUMMARY DAILY REPORT PER LOCATION","Summary Daily Report"}
    };
    
    public static final String textListMaterialHeader[][] ={ 
	{"No","Tanggal","Total HPP", "Total Disc","Total Jual","Total Profit","Total Tax","Total Service","RATA-RATA","Rata-rata Harian"},
	{"No","Date","Total Cost","Total Disc","Total Sale","Total Profit","Total Tax","Total Service","AVERAGE","Average per Day"}
    };
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.iCommand = FRMQueryString.requestCommand(request);       
        this.iErrCode = 0;        
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
	    case Command.SAVE :
		//commandSave(request);
	    break;
                
            case Command.LIST :
                commandList(request);
            break;
		
	    case Command.DELETE :
                //commandDelete(request);
            break;
   
	    default : commandNone(request);
	}
        
        try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
        
        response.getWriter().print(this.jSONObject);
        
    }
    
    public void commandNone(HttpServletRequest request){
	if(this.dataFor.equals("getComboCategory")){
	    this.htmlReturn = getComboCategory(request);
	}
    }
    
    public void commandList(HttpServletRequest request){
        if (this.dataFor.equals("getListReportTable")){
            this.htmlReturn = getListReportTable(request);
        }else if(this.dataFor.equals("getListReportTableOld")){
            this.htmlReturn = getListReportTableOld(request);
         }else if(this.dataFor.equals("getListReportTableLocation")){
            this.htmlReturn = getListReportTablePerLocation(request);
        }
    }
    
    
    
    private String getComboCategory(HttpServletRequest request){
        String htmlReturn="";
        
        //STRING
        String where="";
        
        //VECTOR
        Vector val_categoryid = new Vector(1,1);
        Vector key_categoryid = new Vector(1,1);
        
        
        //CATCH VARIABEL
        long oidLocation = FRMQueryString.requestLong(request, "FRM_FLD_LOCATION_ID");
        
        //if (oidLocation!=0){
        //    where += ""
        //    + " "+PstCategory.fieldNames[PstCategory.FLD_LOCATION_ID]+"='"+oidLocation+"'";
        //}
        
        val_categoryid.add("0");
        key_categoryid.add("All Category");
        
        Vector listCategory = PstCategory.list(0, 0, where, "");
        for (int i=0; i<listCategory.size();i++ ){
            Category entCategory = (Category) listCategory.get(i);
            val_categoryid.add(""+entCategory.getOID()+"");
            key_categoryid.add(""+entCategory.getName()+"");
        }
        
        htmlReturn += ""+ControlCombo.draw(FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID], null, "", val_categoryid, key_categoryid, " multiple='multiple' ", "form-control selects2")+"";
        
        return htmlReturn;
    }
    
    private String getListReportTable(HttpServletRequest request){
        String htmlReturn = "";
        
        //STRING
        String whereClause = "";
        
        //INT
        int iErrCode = FRMMessage.ERR_NONE;
        
        SrcReportSale srcReportSale = new SrcReportSale();
        SessReportSale sessReportSale = new SessReportSale();
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
        
        frmSrcReportSale.requestEntityObject(srcReportSale);
        String dateFrom = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM]+"");
        String dateTo = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO]+"");
        
        Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
        Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
               
        srcReportSale.setDateFrom(tglmulai);
        srcReportSale.setDateTo(tglberakhir);
        
        //CATCH 
        int language = FRMQueryString.requestInt(request, "language");
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
               
        srcReportSale.setCategoryMultiple(categoryInText);
        srcReportSale.setLocationMultiple(locationInText);
        Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
        
        

        htmlReturn += ""
        + "<div class='col-md-12'>"                                    
            + "<div class='box box-primary'>" 
                + "<div class='box-header'>" 
                    + "<h3 class='box-title'>"+textListHeader[language][1]+"</h3>" 
                    + "<div class='box-tools pull-right'>" 
                        + "<button class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>" 
                    + "</div>" 
                + "</div>" 
                + "<div class='box-body' >"
                    + "<table class='table table-striped table-bordered'>"
                        + "<thead>"
                            + "<tr>"
                                + "<td><b>"+textListMaterialHeader[language][0]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][1]+"</b></td>"
                                
                                + "<td><b>"+textListMaterialHeader[language][4]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][9]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][2]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][5]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][6]+"</b></td>"
                                + "<td><b>"+textListMaterialHeader[language][7]+"</b></td>"
                            + "</tr>"
                        + "</thead>"
                        + "<tbody>";
                        if (records.size()>0){
                            double totalJual = 0.00;
                            double totalCost = 0.00;
                            double totalPotongan = 0.00;
                            double totalProfit = 0.00;
                            double totalTaxVal = 0.00;
                            double totalServiceVal = 0.00;
                            double totalRata= 0;
                            double rata=0;
                            double totalRataRata=0;
                            for (int i = 0; i<records.size();i++){
                                Vector rowx = new Vector();				
                                Vector vt = (Vector)records.get(i);
                                String tanggal = (String)vt.get(0);
                                Double totalRekap = (Double)vt.get(1);
                                Double totalHPP = (Double)vt.get(2);
                                Double totalDisc = (Double)vt.get(3);
                                
                                if (i==0){
                                    totalRata = totalRekap.doubleValue();
                                }else{
                                    totalRata = rata+ totalRekap.doubleValue();
                                }
                                

                                Double totalTax = (Double)vt.get(4);
                                Double totalService = (Double)vt.get(5);
                                totalJual += totalRekap.doubleValue();
                                rata = totalJual/(i+1);
                                htmlReturn += ""
                                + "<tr>"
                                    + "<td>"+(i+1)+"</td>"
                                    + "<td>"+tanggal+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rata)+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue())+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalTax.doubleValue())+"</td>"
                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalService.doubleValue())+"</td>"
                                + "</tr>";
                                
                                totalRataRata = 0;
                                totalCost += totalHPP.doubleValue();
                                totalPotongan += totalDisc.doubleValue();
                                totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
                                totalTaxVal += totalTax.doubleValue();
                                totalServiceVal += totalService.doubleValue();
                            }
                            htmlReturn += ""
                            + "<tr>"
                                + "<td colspan='2'><b>TOTAL</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual)+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata)+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit)+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal)+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal)+"</b></td>"
                            + "</tr>"
                            + "<tr>"
                                + "<td colspan='2'><b>"+textListMaterialHeader[language][8]+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual/records.size())+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata/records.size())+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost/records.size())+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit/records.size())+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal/records.size())+"</b></td>"
                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal/records.size())+"</b></td>"
                                    
                            + "</tr>";
                        }else{
                            htmlReturn += ""
                            + "<tr><td colspan='7'><center>No Data</center></td></tr>";
                        }
                        htmlReturn += ""
                        + "</tbody>"
                    + "</table>" 
                + "</div>" 
                +"<div class='box-footer'> &nbsp;"
                    + "&nbsp;<button style='margin-left:10px;' id='btnPrint' type='button' class='btn btn-primary pull-right'><i class='fa fa-print'></i> Print</button>&nbsp;" 
                    + "&nbsp;<button type='button' id='btnExportExcel' class='btn btn-success pull-right'><i class='fa fa-cloud-upload'></i> Export Excel</button>&nbsp;" 
                +"</div>" 
            +"</div>" 
        +"</div>";
        
        return htmlReturn;
    }
    
    
    private String getListReportTablePerLocation(HttpServletRequest request){
        String htmlReturn = "";
        
        //STRING
        String whereClause = "";
        
        //INT
        int iErrCode = FRMMessage.ERR_NONE;
        
        SrcReportSale srcReportSale = new SrcReportSale();
        SessReportSale sessReportSale = new SessReportSale();
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
        
        frmSrcReportSale.requestEntityObject(srcReportSale);
        String dateFrom = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM]+"");
        String dateTo = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO]+"");
        
        Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
        Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
               
        srcReportSale.setDateFrom(tglmulai);
        srcReportSale.setDateTo(tglberakhir);
        
        //CATCH 
        int language = FRMQueryString.requestInt(request, "language");
        String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID]+"");
        String categoryLocation[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID]+"");
        String categoryInText="";
        String lokasiInText="";
        
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
            for (int a = 0; a<categoryLocation.length;a++){
                if (lokasiInText.length()>0){
                    lokasiInText = lokasiInText + "," + categoryLocation[a];
                }else{
                    lokasiInText =  categoryLocation[a];
                }
            }
        } catch (Exception e) {
        }
        
               
        srcReportSale.setCategoryMultiple(categoryInText);
        srcReportSale.setLocationMultiple(lokasiInText);
        
        //jadikan hastable
       Hashtable hashReportRekap = sessReportSale.getReportSaleRekapTanggalPerLocation(srcReportSale);
        
       //Vector location  = PstLocation.listLocationStore(0, 0, "", ""+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+" ASC ");
       
        htmlReturn += ""
        + "<div class='col-md-12'>"                                    
            + "<div class='box box-primary'>" 
                + "<div class='box-header'>" 
                    + "<h3 class='box-title'>"+textListHeaderLocation[language][1]+"</h3>" 
                    + "<div class='box-tools pull-right'>" 
                        + "<button class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>" 
                    + "</div>" 
                + "</div>" 
                + "<div class='box-body' >"
                   
                    + "<table class='table table-striped table-bordered'>"
                        + "<thead>"
                                + "<tr>"
                                    + "<td></td>"//no
                                    + "<td></td>";//tanggal
                                    //lokasi
                                    for (int i = 0; i<categoryLocation.length;i++){
                                        long locationOid = Long.parseLong(categoryLocation[i]);
                                        Location locHeader = new Location();
                                        try {
                                            locHeader = PstLocation.fetchExc(locationOid);
                                        } catch (Exception e) {
                                        }
                                        htmlReturn += "<td colspan=\"2\"><center><b>"+locHeader.getName()+"</b></center></td>";//no
                                    }
                                    htmlReturn += "<td colspan=\"2\"><center><b>Total</b></center></td>";//no
                                    htmlReturn += "<td colspan=\"2\"><center><b>Gross Profit</b></center></td>";//no
                                    
                                htmlReturn += "</tr>"   
                                 + "<tr>"
                                    + "<td><b>"+textListMaterialHeader[language][0]+"</b></td>"//no
                                    + "<td><b>"+textListMaterialHeader[language][1]+"</b></td>";//tanggal
                                
                                    //lokasi
                                    for (int i = 0; i<categoryLocation.length;i++){
                                        htmlReturn += "<td><center><b>Sales</b></center></td>";//no
                                        htmlReturn += "<td><center><b>CoGs<b></center></td>";//no
                                    }
                                    
                                    htmlReturn += "<td><center><b>Sales</b></center></td>";//no
                                    htmlReturn += "<td><center><b>CoGs<b></center></td>";//no
                                    
                                    htmlReturn += "<td><center><b>Rp.</b></center></td>";//no
                                    htmlReturn += "<td><center><b>%<b></center></td>";//no    
                                    
                                htmlReturn += "</tr>"         
                        + "</thead>"
                        + "<tbody>";
                        if(hashReportRekap!=null && hashReportRekap.size()>0 ){
                            double totalJual = 0.00;
                            double totalCost = 0.00;
                            double totalPotongan = 0.00;
                            double totalProfit = 0.00;
                            double totalTaxVal = 0.00;
                            double totalServiceVal = 0.00;
                            double totalRata= 0;
                            double rata=0;
                            double totalRataRata=0;
                            
                            try {
                                long oneDayMilSec = 86400000;
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                //Date startDate = srcReportSale.getDateFrom().getTime();//sdf.parse("2012-02-15");
                                //Date endDate = srcReportSale.getDateTo();//sdf.parse("2012-03-15");
                                long startDateMilSec = srcReportSale.getDateFrom().getTime();//startDate.getTime();
                                long endDateMilSec = srcReportSale.getDateTo().getTime();
                                int nourut=0;
                                double rekapPerDate=0.0;
                                double hppPerDate=0.0;
                                double profitPerDate=0.0;
                                for(long d=startDateMilSec; d<=endDateMilSec; d=d+oneDayMilSec){
                                    nourut=nourut+1;
                                    rekapPerDate=0.0;
                                    hppPerDate=0.0;
                                    profitPerDate=0.0;
                                    Date dateNow = new Date(d);
                                    Formater.formatDate(dateNow, "dd-MM-yyyy");
                                    htmlReturn += ""
                                               + "<tr>"
                                               + "<td>"+nourut+"</td>"
                                               + "<td>"+Formater.formatDate(dateNow, "dd-MM-yyyy")+"</td>";        
                                                for (int k = 0; k<categoryLocation.length;k++){
                                                    long locationOid = Long.parseLong(categoryLocation[k]);
                                                    Location locHeader =  new Location();
                                                    try {
                                                        locHeader = PstLocation.fetchExc(locationOid);
                                                    } catch (Exception e) {
                                                    }
                                                    Vector vt = (Vector) hashReportRekap.get(""+locHeader.getOID()+"-"+Formater.formatDate(dateNow, "dd-MM-yyyy"));
                                                    
                                                    if(vt!=null && vt.size()>0){
                                                        Double totalRekap = (Double)vt.get(1);
                                                        Double totalHPP = (Double)vt.get(2);

                                                        hppPerDate=hppPerDate+totalHPP;
                                                        rekapPerDate=rekapPerDate+(totalRekap.doubleValue());
                                                        profitPerDate = profitPerDate + (totalRekap.doubleValue() - totalHPP.doubleValue());

                                                        totalJual = totalJual+(totalRekap.doubleValue());
                                                        totalCost = totalCost+totalHPP;
                                                        totalProfit = totalProfit+(totalRekap.doubleValue()  - totalHPP.doubleValue());

                                                        htmlReturn += "<td>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</td>";//no
                                                        htmlReturn += "<td>"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</td>";//no
                                                    }else{
                                                        htmlReturn += "<td>"+FRMHandler.userFormatStringDecimal(0)+"</td>";//no
                                                        htmlReturn += "<td>"+FRMHandler.userFormatStringDecimal(0)+"</td>";//no
                                                    }
                                                    
                                                }  
                                                
                                                htmlReturn += "<td><center>"+FRMHandler.userFormatStringDecimal(rekapPerDate)+"</center></td>";//no
                                                htmlReturn += "<td><center>"+FRMHandler.userFormatStringDecimal(hppPerDate)+"</center></td>";//no
                                                htmlReturn += "<td><center>"+FRMHandler.userFormatStringDecimal(profitPerDate)+"</center></td>";//no
                                                htmlReturn += "<td><center>"+FRMHandler.userFormatStringDecimal((profitPerDate/hppPerDate)*100)+"%</center></td>";//no    
                                    
                                }
                                
                                htmlReturn += "<tr>"
                                    + "<td></td>"//no
                                    + "<td><b>Total</b></td>";//tanggal
                                    //lokasi
                                     for (int i = 0; i<categoryLocation.length;i++){
                                        htmlReturn += "<td><center></center></td>";//no
                                        htmlReturn += "<td><center></center></td>";//no
                                    }
                                    
                                    htmlReturn += "<td><center><b>"+FRMHandler.userFormatStringDecimal(totalJual)+"</b></center></td>";//no
                                    htmlReturn += "<td><center><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></center></td>";//no
                                    
                                    htmlReturn += "<td><center><b>"+FRMHandler.userFormatStringDecimal(totalProfit)+"</b></center></td>";//no
                                    htmlReturn += "<td><center><b>"+FRMHandler.userFormatStringDecimal((totalProfit/totalCost)*100)+"%</b></center></td>";//no    
                                    
                                htmlReturn += "</tr>" ; 
                                
                            }catch (Exception ex) {
                            }
//                            for (int i = 0; i<hashReportRekap.size();i++){
//                                
//                                
//                                for (int k = 0; k<location.size();k++){
//                                    htmlReturn += "<td>HPP</td>";//no
//                                    htmlReturn += "<td>SALES</td>";//no
//                                }   
//                                Vector rowx = new Vector();				
//                                Vector vt = (Vector)records.get(i);
//                                String tanggal = (String)vt.get(0);
//                                Double totalRekap = (Double)vt.get(1);
//                                Double totalHPP = (Double)vt.get(2);
//                                Double totalDisc = (Double)vt.get(3);
//                                
//                                if (i==0){
//                                    totalRata = totalRekap.doubleValue();
//                                }else{
//                                    totalRata = rata+ totalRekap.doubleValue();
//                                }
//                                
//
//                                Double totalTax = (Double)vt.get(4);
//                                Double totalService = (Double)vt.get(5);
//                                totalJual += totalRekap.doubleValue();
//                                rata = totalJual/(i+1);
//                                //htmlReturn += ""
////                                + "<tr>"
////                                    + "<td>"+(i+1)+"</td>"
////                                    + "<td>"+tanggal+"</td>"
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</td>"//total jual
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rata)+"</td>"//
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</td>"//total hpp
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue())+"</td>"//total profite
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalTax.doubleValue())+"</td>"
////                                    + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(totalService.doubleValue())+"</td>"
////                                + "</tr>";
//                                htmlReturn += ""
//                                            + "<td>"+(i+1)+"</td>"
//                                            + "<td>"+tanggal+"</td>";
//                                        for (int k = 0; k<location.size();k++){
//                                                htmlReturn += "<td>HPP</td>";//no
//                                                htmlReturn += "<td>SALES</td>";//no
//                                            }
//                                
//                                totalRataRata = 0;
//                                totalCost += totalHPP.doubleValue();
//                                totalPotongan += totalDisc.doubleValue();
//                                totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
//                                totalTaxVal += totalTax.doubleValue();
//                                totalServiceVal += totalService.doubleValue();
                          //  }
//                            htmlReturn += ""
//                            + "<tr>"
//                                + "<td colspan='2'><b>TOTAL</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal)+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal)+"</b></td>"
//                            + "</tr>"
//                            + "<tr>"
//                                + "<td colspan='2'><b>"+textListMaterialHeader[language][8]+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalJual/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalRataRata/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalCost/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalProfit/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalTaxVal/records.size())+"</b></td>"
//                                + "<td style='text-align:right;'><b>"+FRMHandler.userFormatStringDecimal(totalServiceVal/records.size())+"</b></td>"
//                                    
//                            + "</tr>";
                        }else{
                            htmlReturn += ""
                            + "<tr><td colspan='7'><center>No Data</center></td></tr>";
                        }
                        htmlReturn += ""
                        + "</tbody>"
                    + "</table>" 
                + "</div>" 
                +"<div class='box-footer'> &nbsp;"
                    + "&nbsp;<button style='margin-left:10px;' id='btnprintperlocation' type='button' class='btn btn-primary pull-right'><i class='fa fa-print'></i> Print</button>&nbsp;" 
                    + "&nbsp;<button type='button' id='exportExcelPerLocation' class='btn btn-success pull-right'><i class='fa fa-cloud-upload'></i> Export Excel</button>&nbsp;" 
                +"</div>" 
            +"</div>" 
        +"</div>";
        
        return htmlReturn;
    }
    
    private String getListReportTableOld(HttpServletRequest request){
         String htmlReturn = "";
        
        //STRING
        String whereClause = "";
        
        //INT
        int iErrCode = FRMMessage.ERR_NONE;
        
        SrcReportSale srcReportSale = new SrcReportSale();
        SessReportSale sessReportSale = new SessReportSale();
        FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);
        
        frmSrcReportSale.requestEntityObject(srcReportSale);
        
        HttpSession session = request.getSession();
        
        
        /*try {
            srcReportSale = (SrcReportSale) session.getValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
            if (srcReportSale == null) {
                srcReportSale = new SrcReportSale();
            }
        } catch (Exception e) {
            srcReportSale = new SrcReportSale();
        }*/
        
        

        //CATCH 
        int language = FRMQueryString.requestInt(request, "language");
        String categoryMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID]+"");
        String dateFrom = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM]+"");
        String dateTo = FRMQueryString.requestString(request, ""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO]+"");
        
        String categoryInText="";
        
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
        
        String locationMultiple[] = request.getParameterValues(""+FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID]+"");
        String locationInText="";
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
        
        Date tglmulai = Formater.formatDate(dateFrom, "MM/dd/yyyy");
        Date tglberakhir = Formater.formatDate(dateTo, "MM/dd/yyyy");
        
        srcReportSale.setLocationMultiple(locationInText);
        srcReportSale.setCategoryMultiple(categoryInText);
        srcReportSale.setDateFrom(tglmulai);
        srcReportSale.setDateTo(tglberakhir);
        
        Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
        Vector hasil = drawList(language,records);
        Vector report = new Vector(1,1);
        report.add(srcReportSale);
        report.add(hasil);
        try {
            session.putValue("SESS_MAT_SALE_REPORT_REKAP", report);
        } catch (Exception e) {
        }

        for(int k=0;k<hasil.size();k++){
           htmlReturn += (hasil.get(k));
        }

        return htmlReturn;
    }
    
    private String getListReportTableOldPerLocation(HttpServletRequest request){
        String htmlReturn = "";
        
        return htmlReturn;
    }
    
    public Vector drawList(int language,Vector objectClass){
	       Vector result = new Vector();
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("80%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("0");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "3%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "10%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");

            // tax service
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");
            ctrlist.dataFormat("", "15%", "0", "0", "right", "bottom");
            ctrlist.dataFormat("", "1%", "0", "0", "center", "middle");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            double totalJual = 0.00;
            double totalCost = 0.00;
            double totalPotongan = 0.00;
            double totalProfit = 0.00;

            double totalTaxVal = 0.00;
            double totalServiceVal = 0.00;

            boolean firstRow = true;
            int baris = 2;
            for (int i = 0; i < objectClass.size(); i++) {
                Vector rowx = new Vector();
                Vector vt = (Vector) objectClass.get(i);
                String tanggal = (String) vt.get(0);
                Double totalRekap = (Double) vt.get(1);
                Double totalHPP = (Double) vt.get(2);
                Double totalDisc = (Double) vt.get(3);

                Double totalTax = (Double) vt.get(4);
                Double totalService = (Double) vt.get(5);

                if (firstRow == true) {
                    firstRow = false;
                    //Draw Header
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                }
                rowx.add("|");
                rowx.add("" + (i + 1));
                rowx.add("<div align=\"center\">" + "|" + "</div>");
                rowx.add("<div align=\"center\">" + tanggal + "</div>");
                rowx.add("<div align=\"center\">" + "|" + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalRekap.doubleValue()) + "</div>"); // 
                rowx.add("<div align=\"center\">" + "|" + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalHPP.doubleValue()) + "</div>");
                rowx.add("<div align=\"center\">" + "|" + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue()) + "</div>");
                rowx.add("<div align=\"center\">" + "|" + "</div>");

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalTax.doubleValue()) + "</div>");
                rowx.add("<div align=\"right\">" + "|" + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalService.doubleValue()) + "</div>");
                rowx.add("<div align=\"right\">" + "|" + "</div>");

                lstData.add(rowx);

                totalJual += totalRekap.doubleValue();
                totalCost += totalHPP.doubleValue();
                totalPotongan += totalDisc.doubleValue();
                totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
                totalTaxVal += totalTax.doubleValue();
                totalServiceVal += totalService.doubleValue();

                baris += 1;

                if (baris == 78) {
                    //Add line
                    lstData.add(drawLineHorizontal());
                    //Add header for next page and restart counting baris
                    lstData.add(drawLineHorizontal());
                    baris = 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                }

                lstLinkData.add("");
            }
            lstData.add(drawLineHorizontal());
            //Add TOTAL
            Vector rowx = new Vector();
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\">" + "TOTAL" + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalJual) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalProfit) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");

            // total tax service
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalTaxVal) + "</div>");// 
            rowx.add("<div align=\"right\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalServiceVal) + "</div>");// 
            rowx.add("<div align=\"right\">" + "|" + "</div>");

            lstData.add(rowx);
            
            //Add RATA
           /*
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\">" + "RATA-RATA" + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalJual/objectClass.size()) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCost/objectClass.size()) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalProfit/objectClass.size()) + "</div>");
            rowx.add("<div align=\"center\">" + "|" + "</div>");

            // RATA tax service
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalTaxVal/objectClass.size()) + "</div>");// 
            rowx.add("<div align=\"right\">" + "|" + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalServiceVal/objectClass.size()) + "</div>");// 
            rowx.add("<div align=\"right\">" + "|" + "</div>");

            lstData.add(rowx);
            */
            lstData.add(drawLineTotal());
            result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
        } else {
            result.add("<div class=\"msginfo\">&nbsp;&nbsp;No data available ...</div>");
        }
        return result;
    }
    
    public Vector drawLineHorizontal() {
        Vector rowx = new Vector();
        //Add Under line
        rowx.add("-");
        rowx.add("-------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        rowx.add("---------------------------------------");
        rowx.add("<div align=\"right\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"right\">" + "-" + "</div>");
        return rowx;
    }

    public Vector drawHeader(int language) {
        Vector rowx = new Vector();
        //Add Header
        rowx.add("|");
        rowx.add(textListMaterialHeader[language][0]);
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"center\">" + textListMaterialHeader[language][1] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][4] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][2] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][5] + "</div>");
        rowx.add("<div align=\"center\">" + "|" + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][6] + "</div>");
        rowx.add("<div align=\"right\">" + "|" + "</div>");
        rowx.add("<div align=\"right\">" + textListMaterialHeader[language][7] + "</div>");
        rowx.add("<div align=\"right\">" + "|" + "</div>");
        return rowx;
    }

    public Vector drawLineTotal() {
        Vector rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        // tax service
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("---------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        return rowx;
    }

    public Vector drawLineSingleSpot() {
        Vector rowx = new Vector();
        rowx.add("-");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");

        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        return rowx;
    }

    public Vector drawLineTotalSide() {
        Vector rowx = new Vector();
        rowx.add("|");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"center\">" + "-" + "</div>");

        // tax service
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"right\">" + "|" + "</div>");
        rowx.add("--------------------------------------");
        rowx.add("<div align=\"right\">" + "|" + "</div>");

        return rowx;
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
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
}
