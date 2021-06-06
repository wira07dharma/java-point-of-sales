

package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.payment.CurrencyType;
import com.dimata.common.entity.payment.PriceType;
import com.dimata.common.entity.payment.PstCurrencyType;
import com.dimata.common.entity.payment.PstPriceType;
import com.dimata.common.entity.payment.PstStandartRate;
import com.dimata.common.entity.payment.StandartRate;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.gui.jsp.ControlDate;
import com.dimata.gui.jsp.ControlList;
import com.dimata.hanoman.entity.masterdata.MasterGroup;
import com.dimata.hanoman.entity.masterdata.MasterType;
import com.dimata.hanoman.entity.masterdata.PstMasterGroup;
import com.dimata.hanoman.entity.masterdata.PstMasterType;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Color;
import com.dimata.posbo.entity.masterdata.Ksg;
import com.dimata.posbo.entity.masterdata.MasterGroupMapping;
import com.dimata.posbo.entity.masterdata.MatMappKsg;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialTypeMapping;
import com.dimata.posbo.entity.masterdata.PstColor;
import com.dimata.posbo.entity.masterdata.PstKsg;
import com.dimata.posbo.entity.masterdata.PstMasterGroupMapping;
import com.dimata.posbo.entity.masterdata.PstMatMappKsg;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstMaterialMappingType;
import com.dimata.posbo.entity.masterdata.PstPriceTypeMapping;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.SubCategory;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PstPurchaseOrder;
import com.dimata.posbo.entity.purchasing.PurchaseOrder;
import com.dimata.posbo.entity.search.SrcReportReceive;
import com.dimata.posbo.entity.warehouse.MatDispatch;
import com.dimata.posbo.entity.warehouse.MatReceive;
import com.dimata.posbo.entity.warehouse.MatReceiveItem;
import com.dimata.posbo.entity.warehouse.MatReturn;
import com.dimata.posbo.entity.warehouse.MatReturnItem;
import com.dimata.posbo.entity.warehouse.PstMatDispatch;
import com.dimata.posbo.entity.warehouse.PstMatReceive;
import com.dimata.posbo.entity.warehouse.PstMatReceiveItem;
import com.dimata.posbo.entity.warehouse.PstMatReturn;
import com.dimata.posbo.entity.warehouse.PstMatReturnItem;
import com.dimata.posbo.entity.warehouse.PstReceiveStockCode;
import com.dimata.posbo.form.search.FrmSrcReportDispatch;
import com.dimata.posbo.form.search.FrmSrcReportReceive;
import com.dimata.posbo.form.warehouse.CtrlMatReceiveItem;
import com.dimata.posbo.form.warehouse.FrmMatReceiveItem;
import com.dimata.posbo.session.masterdata.SessMaterial;
import com.dimata.posbo.session.warehouse.SessMatCostingStokFisik;
import com.dimata.posbo.session.warehouse.SessReportReceive;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.IOException;
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


public class AjaxPenerimaan extends HttpServlet{
    //OBJECT
    private JSONObject jSONObject = new JSONObject();
    private JSONArray jSONArray = new JSONArray();
    
    //STRING
    private String dataFor = "";
    private String oidDelete="";
    private String approot = "";
    private String address  = "";
    private String htmlReturn = "";
    private String msgReturn = "";
  
    //LONG
    private long oidReturn = 0;

    //INT
    private int iCommand = 0;
    private int iErrCode = 0;
    
    static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    static boolean bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;
    
    public static final String textListHeader[][] ={ 
	{"No","Tanggal","Invoice","PO No","Kode Penerimaan","Suplier","Keterangan","Total Qty","Total Beli","Total HPP","Total Harga Jual","Margin","Tidak ada data tersedia"},
	{"No","Date","Invoice","No PO","Receiving Code","Supplier","Description","Total Qty","TOtal Purchase,Total HPP","Total Sales Price","Margin","No Data Available"}
    };
    
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Kadaluarsa", "Unit", "Harga Beli", "Ongkos Kirim", "Mata Uang", "Qty", "Total Beli", "Diskon Terakhir %",//10
            "Diskon1 %", "Diskon2 %", "Discount Nominal", "Qty Entri", "Unit Konversi", "Hapus", "Bonus", "Berat (gr)", "Keterangan", "Sorting",
            "Warna","Etalase"},//19
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Delivery Cost", "Currency", "Qty", "Total Cost", "last Discount %",
            "Discount1 %", "Discount2 %", "Disc. Nominal", "Qty Entri", "Unit Konversi", "Delete", "Bonus", "Weight", "Remark", "Sorting",
            "Color", "Etalase"}
    };
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        this.dataFor = FRMQueryString.requestString(request, "FRM_FIELD_DATA_FOR");
        this.approot = FRMQueryString.requestString(request, "FRM_FIELD_APPROOT");
        this.iCommand = FRMQueryString.requestCommand(request);       
        this.iErrCode = 0;        
        this.jSONObject = new JSONObject();
        
        switch(this.iCommand){
	    case Command.SAVE :
		commandSave(request);
	    break;
                
            case Command.LIST :
                //commandList(request);
            break;
		
	    case Command.DELETE :
                //commandDelete(request);
            break;
   
	    default : commandNone(request);
	}
        
        try{
	    
	    this.jSONObject.put("FRM_FIELD_HTML", this.htmlReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_OID", this.oidReturn);
	    this.jSONObject.put("FRM_FIELD_RETURN_MESSAGE", this.msgReturn);
	    this.jSONObject.put("FRM_FIELD_ERROR_NUMBER", ""+this.iErrCode);
	}catch(JSONException jSONException){
	    jSONException.printStackTrace();
	}
        
        response.getWriter().println(this.jSONObject);
        
    }
    
    public void commandNone(HttpServletRequest request){
	if(this.dataFor.equals("getSummaryReportPerInvoice")){
           int typeFor = FRMQueryString.requestInt(request, "typeFor");
           if (typeFor==0){
                this.htmlReturn = getSummaryReportPerInvoice(request);
           }else{
                this.htmlReturn = getSummaryReportPerInvoice2(request);
           }
        } else if(this.dataFor.equals("getReportBisnisDetail2")){
            this.htmlReturn = getSummaryReportPenerimaanJewelry(request);
        } else if(this.dataFor.equals("getEtalaseByLocation")){
            this.htmlReturn = getEtalaseByLocation(request);
        } else if (this.dataFor.equals("getReportBisnisLeburDetail2")){
			this.htmlReturn = getSummaryReportPenerimaanJewelryLebur(request);
		}
    }
    
    public void commandSave(HttpServletRequest request){
        if (this.dataFor.equals("saveReceiveItem")) {
            saveReceiveItem(request);
            this.htmlReturn = listMaterialItem(request);
        } else if (this.dataFor.equals("saveReturnItem")) {
            saveReturnItem(request);
        }
    }
    
    private String getSummaryReportPerInvoice(HttpServletRequest request){
        String htmlReturn="";
        String dateFromCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
        String dateToCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");
        
        Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
        Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");
        /*
        Date dateFrom = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
        Date dateTo = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");*/
        /*
        long locationId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID]+"");
        long currencyId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID]+"");
        long supplierId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_SUPPLIER_ID]+"");
        */
        int language = FRMQueryString.requestInt(request, "language");
        SrcReportReceive srcReportReceive = new SrcReportReceive();
        SessReportReceive sessReportReceive = new SessReportReceive();
        FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);
        frmSrcReportReceive.requestEntityObject(srcReportReceive);
        
        
        String priceTypeMultiple[] = request.getParameterValues(""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID]+"");
        String priceTypeInText="";
        try {
            for (int a = 0; a<priceTypeMultiple.length;a++){
                if (priceTypeInText.length()>0){
                    priceTypeInText = priceTypeInText + "," + priceTypeMultiple[a];
                }else{
                    priceTypeInText =  priceTypeMultiple[a];
                }
            }
        } catch (Exception e) {
        }
        
        String locationMultiple [] = request.getParameterValues(""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID]+"");
        String locationInText="";
        try {
            for (int b = 0; b<locationMultiple.length;b++){
                if (!locationMultiple[b].equals("0")){
                    if (locationInText.length()>0){
                        locationInText = locationInText + "," + locationMultiple[b];
                    }else{
                        locationInText =  locationMultiple[b];
                    }
                }
            }
        } catch (Exception e) {
        }
        srcReportReceive.setMultiLocation(locationInText);
        srcReportReceive.setMultiPriceType(priceTypeInText);
        srcReportReceive.setDateFrom(dateFrom);
        srcReportReceive.setDateTo(dateTo);
        Vector records = sessReportReceive.getReportReceiveSummary(srcReportReceive);
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0); 
        
        Vector listTypeHrga =  new Vector (); 
        if(priceTypeInText!=null && priceTypeInText.length()>0) {
            String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+priceTypeInText+")";
            listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
        }
        
        int jumlahTambahanKolom = 0;
        
        htmlReturn += ""
            + "<div  class='row' style='margin-left:0px; margin-right:0px; margin-top:10px;'>"
                + "<div class='col-md-12'>"
                    + "<div  class='box box-primary'>"
                        + "<div class='box-header'>"
                            + "<h3 class='box-title'></h3>"
                            + "<div class='box-tools pull-right'>" 
                                + " <button class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>" 
                            + "</div>"
                        + "</div>"
                        + "<div class='box-body'>"
                            + "<table class='table table-striped table-bordered table-hover'>"
                                + "<tr>"
                                    + "<th>"+textListHeader[language][0]+"</th>"
                                    + "<th>"+textListHeader[language][1]+"</th>"
                                    + "<th>"+textListHeader[language][2]+"</th>"
                                    + "<th>"+textListHeader[language][3]+"</th>"
                                    + "<th>"+textListHeader[language][4]+"</th>"
                                    + "<th>"+textListHeader[language][5]+"</th>"
                                    + "<th>"+textListHeader[language][6]+"</th>"
                                    + "<th>"+textListHeader[language][7]+"</th>"
                                    + "<th>"+textListHeader[language][8]+"</th>"
                                    + "<th>"+textListHeader[language][9]+"</th>";
                                    if(listTypeHrga != null && listTypeHrga.size()>0){
                                    for(int i = 0; i<listTypeHrga.size();i++){
                                        for(int j=0;j<listCurrStandardX.size();j++){
                                            Vector temp = (Vector)listCurrStandardX.get(j);
                                            CurrencyType curr = (CurrencyType)temp.get(0);
                                            PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                                            //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                                            //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                                            htmlReturn += ""
                                                + "<th>"+textListHeader[language][10]+"</th>"
                                                + "<th>"+textListHeader[language][11]+"</th>";
                                            jumlahTambahanKolom += 2;
                                        }
                                    }
                                }
                                htmlReturn += ""
                                + "</tr>";
                                long noBill = 0;
                                boolean firstRow = true;
                                double totalRec = 0.00;
                                double totalPrice = 0.00;
                                double totalQty = 0;
                                double subTotalRec = 0.00;

                                double subTotalBeli = 0.00;
                                double subTotalQty = 0;
                                double subTotalHPP=0;
                                double subTotalHargaJual=0;

                                int baris = 0;
                                int countTrue = 0;

                                double subTotalJual = 0.00;
                                double totalJual = 0.00;
                                double totalLastJual = 0.00;
                                double[] arrSubTotalHargaJual = new double[listTypeHrga.size()];
                                
                                int noColspan = 10 +jumlahTambahanKolom;
                                if (records.size()>0){
                                    for(int i=0; i<records.size(); i++) {
                                        int counter = i + 1;                                
                                        Vector vt = (Vector)records.get(i);
                                        MatReceive rec = (MatReceive)vt.get(0);
                                        MatReceiveItem rmi = (MatReceiveItem)vt.get(1);
                                        Material mat = (Material)vt.get(2);
                                        ContactList cnt = (ContactList)vt.get(3);

                                        String nomorPo="Tanpa PO";
                                        try{
                                            if(rec.getPurchaseOrderId()!=0){
                                                PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
                                                nomorPo = purchaseOrder.getPoCode();
                                            }
                                        }catch(Exception ex){
                                        }

                                        htmlReturn += ""
                                        + "<tr>"
                                            + "<td>"+counter+"</td>"
                                            + "<td>"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</td>"
                                            + "<td>"+rec.getInvoiceSupplier()+"</td>"
                                            + "<td>"+nomorPo+"</td>"
                                            + "<td>"+rec.getRecCode()+"</td>"
                                            + "<td>"+cnt.getCompName()+"</td>"
                                            + "<td>"+rec.getRemark()+"</td>"
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getQty())+"</td>"
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getTotal())+"</td>"
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</td>";

                                            subTotalQty += rmi.getQty();
                                            subTotalBeli += rmi.getTotal();
                                            subTotalHPP += mat.getDefaultPrice();
                                            if(listTypeHrga != null && listTypeHrga.size()>0){
                                                for(int a = 0; a<listTypeHrga.size();a++){
                                                    PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                                    for(int j=0;j<listCurrStandardX.size();j++){
                                                        Vector temp = (Vector)listCurrStandardX.get(j);
                                                        CurrencyType curr = (CurrencyType)temp.get(0);
                                                        StandartRate objStandartRate = PstStandartRate.getActiveStandardRate(curr.getOID());
                                                        double total = SessReportReceive.getTotalPriceReceiveByMemberAndReceiveId(rec.getOID(), objStandartRate.getOID(), pricetype.getOID());
                                                        //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                                                        //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                                                        double margin = ((total-mat.getDefaultPrice())/mat.getDefaultPrice())*100;
                                                        htmlReturn += ""
                                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(total)+"</td>"
                                                            + "<td style='text-align:right;'>"+margin+"</td>";
                                                        
                                                        arrSubTotalHargaJual[a] += total;
                                                    }
                                                }
                                            }
                                        htmlReturn += ""
                                        + "</tr>";
                                        
                                    }
                                    htmlReturn += ""
                                    + "<tr style='font-weight:bold'>"
                                        + "<td colspan='7'><center>TOTAL</center></td>"
                                        + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</td>"
                                        + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalBeli)+"</td>"
                                        + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalHPP)+"</td>";
                                        if(listTypeHrga != null && listTypeHrga.size()>0){
                                            for(int a = 0; a<listTypeHrga.size();a++){
                                                PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                                for(int j=0;j<listCurrStandardX.size();j++){

                                                    htmlReturn += ""
                                                        + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(arrSubTotalHargaJual[a])+"</td>"
                                                        + "<td style='text-align:right;'></td>";                                                      
                                                }
                                            }
                                        }
                                    htmlReturn += ""
                                    + "</tr>";
                                }else{
                                    htmlReturn += "<tr><td colspan='"+noColspan+"'><center>"+textListHeader[language][12]+"</center></td></tr>";
                                }
                                
                                htmlReturn += ""
                            + "</table>"
                        + "</div>"
                        + "<div class='box-footer'>" ;
                            if (records.size()>0){
                                htmlReturn += "<button id='btnPrint' class='btn btn-primary pull-right'>Print Report</button>";
                            }
                           
                            htmlReturn += ""
                        + "</div>"
                    + "</div>"
                + " </div>"
            + "</div>";
        
        return htmlReturn;
    }
    
    private String getSummaryReportPerInvoice2(HttpServletRequest request){
        String htmlReturn="";
        String dateFromCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
        String dateToCatch = FRMQueryString.requestString(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");
        
        Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
        Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");
        /*
        Date dateFrom = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]+"");
        Date dateTo = FRMQueryString.requestDate(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]+"");*/
        /*
        long locationId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID]+"");
        long currencyId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID]+"");
        long supplierId = FRMQueryString.requestLong(request, ""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_SUPPLIER_ID]+"");
        */
        int language = FRMQueryString.requestInt(request, "language");
        SrcReportReceive srcReportReceive = new SrcReportReceive();
        SessReportReceive sessReportReceive = new SessReportReceive();
        FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);
        frmSrcReportReceive.requestEntityObject(srcReportReceive);
        
        String priceTypeMultiple[] = request.getParameterValues(""+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID]+"");
        String priceTypeInText="";
        try {
            for (int a = 0; a<priceTypeMultiple.length;a++){
                if (priceTypeInText.length()>0){
                    priceTypeInText = priceTypeInText + "," + priceTypeMultiple[a];
                }else{
                    priceTypeInText =  priceTypeMultiple[a];
                }
            }
        } catch (Exception e) {
        }
        
        srcReportReceive.setMultiPriceType(priceTypeInText);
        srcReportReceive.setDateFrom(dateFrom);
        srcReportReceive.setDateTo(dateTo);
        Vector records = sessReportReceive.getReportReceiveSummary(srcReportReceive);
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0); 
        
        Vector listTypeHrga =  new Vector (); 
        if(priceTypeInText!=null && priceTypeInText.length()>0) {
            String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+priceTypeInText+")";
            listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
        }
        
        int jumlahTambahanKolom = 0;
        
        htmlReturn += "" 
            + "<table class='table table-striped table-bordered table-hover'>"
                + "<tr>"
                    + "<th>"+textListHeader[language][0]+"</th>"
                    + "<th>"+textListHeader[language][1]+"</th>"
                    + "<th>"+textListHeader[language][2]+"</th>"
                    + "<th>"+textListHeader[language][3]+"</th>"
                    + "<th>"+textListHeader[language][4]+"</th>"
                    + "<th>"+textListHeader[language][5]+"</th>"
                    + "<th>"+textListHeader[language][6]+"</th>"
                    + "<th>"+textListHeader[language][7]+"</th>"
                    + "<th>"+textListHeader[language][8]+"</th>"
                    + "<th>"+textListHeader[language][9]+"</th>";
                    if(listTypeHrga != null && listTypeHrga.size()>0){
                    for(int i = 0; i<listTypeHrga.size();i++){
                        for(int j=0;j<listCurrStandardX.size();j++){
                            Vector temp = (Vector)listCurrStandardX.get(j);
                            CurrencyType curr = (CurrencyType)temp.get(0);
                            PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                            //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                            //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                            htmlReturn += ""
                                + "<th>"+textListHeader[language][10]+"</th>"
                                + "<th>"+textListHeader[language][11]+"</th>";
                            jumlahTambahanKolom += 2;
                        }
                    }
                }
                htmlReturn += ""
                + "</tr>";
                long noBill = 0;
                boolean firstRow = true;
                double totalRec = 0.00;
                double totalPrice = 0.00;
                double totalQty = 0;
                double subTotalRec = 0.00;

                double subTotalBeli = 0.00;
                double subTotalQty = 0;
                double subTotalHPP=0;
                double subTotalHargaJual=0;

                int baris = 0;
                int countTrue = 0;

                double subTotalJual = 0.00;
                double totalJual = 0.00;
                double totalLastJual = 0.00;
                double[] arrSubTotalHargaJual = new double[listTypeHrga.size()];

                int noColspan = 10 +jumlahTambahanKolom;
                if (records.size()>0){
                    for(int i=0; i<records.size(); i++) {
                        int counter = i + 1;                                
                        Vector vt = (Vector)records.get(i);
                        MatReceive rec = (MatReceive)vt.get(0);
                        MatReceiveItem rmi = (MatReceiveItem)vt.get(1);
                        Material mat = (Material)vt.get(2);
                        ContactList cnt = (ContactList)vt.get(3);

                        String nomorPo="Tanpa PO";
                        try{
                            if(rec.getPurchaseOrderId()!=0){
                                PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
                                nomorPo = purchaseOrder.getPoCode();
                            }
                        }catch(Exception ex){
                        }

                        htmlReturn += ""
                        + "<tr>"
                            + "<td>"+counter+"</td>"
                            + "<td>"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</td>"
                            + "<td>"+rec.getInvoiceSupplier()+"</td>"
                            + "<td>"+nomorPo+"</td>"
                            + "<td>"+rec.getRecCode()+"</td>"
                            + "<td>"+cnt.getCompName()+"</td>"
                            + "<td>"+rec.getRemark()+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getQty())+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(rmi.getTotal())+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</td>";

                            subTotalQty += rmi.getQty();
                            subTotalBeli += rmi.getTotal();
                            subTotalHPP += mat.getDefaultPrice();
                            if(listTypeHrga != null && listTypeHrga.size()>0){
                                for(int a = 0; a<listTypeHrga.size();a++){
                                    PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                    for(int j=0;j<listCurrStandardX.size();j++){
                                        Vector temp = (Vector)listCurrStandardX.get(j);
                                        CurrencyType curr = (CurrencyType)temp.get(0);
                                        StandartRate objStandartRate = PstStandartRate.getActiveStandardRate(curr.getOID());
                                        double total = SessReportReceive.getTotalPriceReceiveByMemberAndReceiveId(rec.getOID(), objStandartRate.getOID(), pricetype.getOID());
                                        //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                                        //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                                        double margin = ((total-mat.getDefaultPrice())/mat.getDefaultPrice())*100;
                                        htmlReturn += ""
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(total)+"</td>"
                                            + "<td style='text-align:right;'>"+margin+"</td>";

                                        arrSubTotalHargaJual[a] += total;
                                    }
                                }
                            }
                        htmlReturn += ""
                        + "</tr>"
                        + "<tr style='font-weight:bold'>"
                            + "<td colspan='7'><center>TOTAL</center></td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalBeli)+"</td>"
                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(subTotalHPP)+"</td>";
                            if(listTypeHrga != null && listTypeHrga.size()>0){
                                for(int a = 0; a<listTypeHrga.size();a++){
                                    PriceType pricetype = (PriceType)listTypeHrga.get(a); 
                                    for(int j=0;j<listCurrStandardX.size();j++){

                                        htmlReturn += ""
                                            + "<td style='text-align:right;'>"+FRMHandler.userFormatStringDecimal(arrSubTotalHargaJual[a])+"</td>"
                                            + "<td style='text-align:right;'></td>";                                                      
                                    }
                                }
                            }
                        htmlReturn += ""
                        + "</tr>";
                    }
                }else{
                    htmlReturn += "<tr><td colspan='"+noColspan+"'><center>"+textListHeader[language][12]+"</center></td></tr>";
                }

                htmlReturn += ""
            + "</table>";
                        
        
        return htmlReturn;
    }
    
    private String getSummaryReportPenerimaanJewelry(HttpServletRequest request) {
        String html = "";
        String where = " WHERE";
        where += " (rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT + ""
                + " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL 
				+ " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_DISPATCH
				+ " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_SOURCE] + " != " + PstMatReceive.SOURCE_FROM_DISPATCH_UNIT + 
				")";

        //get date
        String dateFromCatch = FRMQueryString.requestString(request, "" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM] + "");
        String dateToCatch = FRMQueryString.requestString(request, "" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO] + "");
        String periode = "";
        
        Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
        Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");

        String newDateFrom = Formater.formatDate(dateFrom, "yyyy-MM-dd");
        String newDateTo = Formater.formatDate(dateTo, "yyyy-MM-dd");

        String newLongDateFrom = Formater.formatDate(dateFrom, "dd/MMM/yyyy");
        String newLongDateTo = Formater.formatDate(dateTo, "dd/MMM/yyyy");

        if (dateFrom != null && dateTo != null) {
            int compareDate = dateFrom.compareTo(dateTo);
            if (compareDate == 0) {
                periode = "" + newLongDateFrom;
                where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") = '" + newDateFrom + "'";
            } else {
                periode = "Dari " + newLongDateFrom + " sampai " + newLongDateTo;
                where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") BETWEEN '" + newDateFrom + "' AND '" + newDateTo + "'";
            }
        } else if (dateFrom != null) {
            periode = "Dari " + newLongDateFrom + " sampai akhir";
            where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") >= '" + newDateFrom + "'";
        } else if (dateTo != null) {
            periode = "Dari awal sampai " + newLongDateTo;
            where += " AND DATE(rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE] + ") <= '" + newDateTo + "'";
        } else {
            periode = "Semua tanggal";
        }

        //get location
        String locationMultiple[] = request.getParameterValues("" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID] + "");
        String locationInText = "";
        String locationShow = "";
        try {
            for (int i = 0; i < locationMultiple.length; i++) {
                if (!locationMultiple[i].equals("0")) {
                    if (locationInText.length() > 0) {
                        locationInText += "," + locationMultiple[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                        locationShow += ", " + l.getName();
                    } else {
                        locationInText = locationMultiple[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                        locationShow += "" + l.getName();
                    }
                }
            }
            if (locationInText.length() > 0) {
                where += " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_LOCATION_ID] + " IN (" + locationInText + ")";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            locationShow = "Semua lokasi";
        }

        //get currency
        String currencyMultiple[] = request.getParameterValues("" + FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID] + "");
        String currencyInText = "";
        String currencyShow = "";
        try {
            for (int i = 0; i < currencyMultiple.length; i++) {
                if (currencyMultiple[i].equals("0")) {
                    currencyShow = "Semua mata uang";
                    break;
                }
                if (currencyInText.length() > 0) {
                    currencyInText += "," + currencyMultiple[i];
                    CurrencyType ct = PstCurrencyType.fetchExc(Long.parseLong(currencyMultiple[i]));
                    currencyShow += ", " + ct.getCode();
                } else {
                    currencyInText = currencyMultiple[i];
                    CurrencyType ct = PstCurrencyType.fetchExc(Long.parseLong(currencyMultiple[i]));
                    currencyShow += "" + ct.getCode();
                }
            }
            if (currencyInText.length() > 0) {
                where += " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_CURRENCY_ID] + " IN (" + currencyInText + ")";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //get tipe penerimaan
        String tipePenerimaan = FRMQueryString.requestString(request, "FRM_TIPE_PENERIMAAN");
        String penerimaanShow = "";
        if (tipePenerimaan.equals("" + Material.MATERIAL_TYPE_EMAS)) {
            penerimaanShow = "" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS];
        } else if (tipePenerimaan.equals("" + Material.MATERIAL_TYPE_BERLIAN)) {
            penerimaanShow = "" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN];
        } else {
            penerimaanShow = "Semua tipe penerimaan";
        }

        html += ""
                + " <div class='col-md-12'>"
                + "     <div class='box box-primary'>"
                + "         <div class='box-header with-border text-center' style='border-bottom-color: lightgray'>"
                + "             <h3 class='box-title'>Laporan Summary Penerimaan</h3>"
                + "         </div>"
                + "         <div class='box-body'>"
                + "             <div class='col-sm-2'>"
                + "                 <p>Periode</p>"
                + "                 <p>Lokasi</p>"
                + "                 <p>Mata Uang</p>"
                + "                 <p>Tipe Penerimaan</p>"
                + "             </div>"
                + "             <div class='col-sm-10'>"
                + "                 <p>: &nbsp; " + periode + "</p>"
                + "                 <p>: &nbsp; " + locationShow + "</p>"
                + "                 <p>: &nbsp; " + currencyShow + "</p>"
                + "                 <p>: &nbsp; " + penerimaanShow + "</p>"
                + "             </div>"
                + "             <table class='table table-bordered' style='font-size: 14px'>"
                + "";

        double grandQty = 0;
        double grandBerat = 0;
        double grandTotal = 0;
        int recordData = 0;

        if (tipePenerimaan.equals("") || tipePenerimaan.equals("" + Material.MATERIAL_TYPE_EMAS)) {
            String whereTipe = " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE] + " = '" + Material.MATERIAL_TYPE_EMAS + "'";
            Vector listEmas = SessReportReceive.getReportReceiveSummaryJewelry(where + whereTipe);
            double totalQty = 0;
            double totalBerat = 0;
            double totalTotal = 0;
            html += ""
                    + " <tr><th colspan='14' style=\"background-color: #e2e2e2\">" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS] + "</th></tr>"
                    + " <tr>"
                    + "     <th>No.</th>"
                    + "     <th>Tanggal</th>"
                    + "     <th>Invoice</th>"
                    + "     <th>Kode Penerimaan</th>"
                    + "     <th>Qty</th>"
                    + "     <th>Supplier</th>"
                    + "     <th>Berat</th>"
                    + "     <th>HEL/Rate</th>"
                    + "     <th>Ongkos</th>"
                    + "     <th>Rate</th>"
                    + "     <th>Currency</th>"
                    + "     <th>Nilai Tukar / Rate</th>"
                    + "     <th>Total</th>"
                    + "     <th>Catatan</th>"
                    + " </tr>";

            for (int i = 0; i < listEmas.size(); i++) {
                recordData += 1;
                int no = i + 1;
                Vector v = (Vector) listEmas.get(i);
                MatReceive mr = (MatReceive) v.get(0);
                MatReceiveItem mri = (MatReceiveItem) v.get(1);
                ContactList supplier = new ContactList();
                CurrencyType currency = new CurrencyType();
                try {
                    supplier = PstContactList.fetchExc(mr.getSupplierId());
                    currency = PstCurrencyType.fetchExc(mr.getCurrencyId());
                } catch (Exception e) {
                }
                totalQty += mri.getQty();
                totalBerat += mr.getBerat();
                totalTotal += mri.getTotal();
                html += ""
                        + " <tr>"
                        + "     <td>" + no + "</td>"
                        + "     <td>" + Formater.formatDate(mr.getReceiveDate(), "dd MMM yyyy") + "</td>"
                        + "     <td>" + mr.getInvoiceSupplier() + "</td>"
                        + "     <td>" + mr.getRecCode() + "</td>"
                        + "     <td class='text-right'>" + Formater.formatNumber(mri.getQty(), "#") + "</td>"
                        + "     <td>" + supplier.getCompName()+ "</td>"
                        + "     <td class='text-right'>" + String.format("%,.3f", mr.getBerat()) + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getHel()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getForwarderCost()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getTransRate()) + ".00</td>"
                        + "     <td>" + currency.getCode() + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getNilaiTukar()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getTotal()) + ".00</td>"
                        + "     <td>" + mr.getRemark() + "</td>"
                        + " </tr>"
                        + "";
            }

            if (listEmas.isEmpty()) {
                html += ""
                        + "<tr><td colspan='14' style='background-color: ' class='text-center'><b>Tidak ada data penerimaan " + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS] + "</b></td></tr>"
                        + "";
            } else {
                html += ""
                        + " <tr>"
                        + "     <td colspan='4'><b>Sub Total</b></td>"
                        + "     <td class='text-right'><b>" + Formater.formatNumber(totalQty, "#") + "</b></td>"
                        + "     <td></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.3f", totalBerat) + "</b></td>"
                        + "     <td colspan='5'></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.0f", totalTotal) + ".00</b></td>"
                        + "     <td></td>"
                        + " </tr>"
                        + "";
            }

            grandQty += totalQty;
            grandBerat += totalBerat;
            grandTotal += totalTotal;
        }

        if (tipePenerimaan.equals("") || tipePenerimaan.equals("" + Material.MATERIAL_TYPE_BERLIAN)) {
            String whereTipe = " AND rm." + PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_ITEM_TYPE] + " = '" + Material.MATERIAL_TYPE_BERLIAN + "'";
            Vector listBerlian = SessReportReceive.getReportReceiveSummaryJewelry(where + whereTipe);
            double totalQty = 0;
            double totalBerat = 0;
            double totalTotal = 0;
            html += ""
                    + " <tr><th colspan='14' style=\"background-color: #e2e2e2\">" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN] + "</th></tr>"
                    + " <tr>"
                    + "     <th>No.</th>"
                    + "     <th>Tanggal</th>"
                    + "     <th>Invoice</th>"
                    + "     <th>Kode Penerimaan</th>"
                    + "     <th>Qty</th>"
                    + "     <th>Supplier</th>"
                    + "     <th>Berat</th>"
                    + "     <th>HEL/Rate</th>"
                    + "     <th>Ongkos</th>"
                    + "     <th>Rate</th>"
                    + "     <th>Currency</th>"
                    + "     <th>Nilai Tukar / Rate</th>"
                    + "     <th>Total</th>"
                    + "     <th>Catatan</th>"
                    + " </tr>";

            for (int i = 0; i < listBerlian.size(); i++) {
                recordData += 1;
                int no = i + 1;
                Vector v = (Vector) listBerlian.get(i);
                MatReceive mr = (MatReceive) v.get(0);
                MatReceiveItem mri = (MatReceiveItem) v.get(1);
                ContactList supplier = new ContactList();
                CurrencyType currency = new CurrencyType();
                try {
                    supplier = PstContactList.fetchExc(mr.getSupplierId());
                    currency = PstCurrencyType.fetchExc(mr.getCurrencyId());
                } catch (Exception e) {
                }
                totalQty += mri.getQty();
                totalBerat += mr.getBerat();
                totalTotal += mri.getTotal();
                html += ""
                        + " <tr>"
                        + "     <td>" + no + "</td>"
                        + "     <td>" + Formater.formatDate(mr.getReceiveDate(), "dd MMM yyyy") + "</td>"
                        + "     <td>" + mr.getInvoiceSupplier() + "</td>"
                        + "     <td>" + mr.getRecCode() + "</td>"
                        + "     <td class='text-right'>" + Formater.formatNumber(mri.getQty(), "#") + "</td>"
                        + "     <td>" + supplier.getCompName()+ "</td>"
                        + "     <td class='text-right'>" + String.format("%,.3f", mr.getBerat()) + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getHel()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getForwarderCost()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getTransRate()) + ".00</td>"
                        + "     <td>" + currency.getCode() + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mr.getNilaiTukar()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getTotal()) + ".00</td>"
                        + "     <td>" + mr.getRemark() + "</td>"
                        + " </tr>"
                        + "";
            }

            if (listBerlian.isEmpty()) {
                html += ""
                        + "<tr><td colspan='14' style='background-color: ' class='text-center'><b>Tidak ada data penerimaan " + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN] + "</b></td></tr>"
                        + "";
            } else {
                html += ""
                        + " <tr>"
                        + "     <td colspan='4'><b>Sub Total</b></td>"
                        + "     <td class='text-right'><b>" + Formater.formatNumber(totalQty, "#") + "</b></td>"
                        + "     <td></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.3f", totalBerat) + "</b></td>"
                        + "     <td colspan='5'></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.0f", totalTotal) + ".00</b></td>"
                        + "     <td></td>"
                        + " </tr>"
                        + "";
            }

            grandQty += totalQty;
            grandBerat += totalBerat;
            grandTotal += totalTotal;
        }

        if (recordData > 0) {
            html += ""
                    + " <tr>"
                    + "     <td style='background-color: #e2e2e2' colspan='4'><b>Grand Total</b></td>"
                    + "     <td style='background-color: #e2e2e2' class='text-right'><b>" + Formater.formatNumber(grandQty, "#") + "</b></td>"
                    + "     <td style='background-color: #e2e2e2'></td>"
                    + "     <td style='background-color: #e2e2e2' class='text-right'><b>" + String.format("%,.3f", grandBerat) + "</b></td>"
                    + "     <td style='background-color: #e2e2e2' colspan='5'></td>"
                    + "     <td style='background-color: #e2e2e2' class='text-right'><b>" + String.format("%,.0f", grandTotal) + ".00</b></td>"
                    + "     <td style='background-color: #e2e2e2'></td>"
                    + " </tr>"
                    + "";
        }

        html += ""
                + "             </table>"
                + "         </div>";
        if (recordData > 0) {
            html += ""
                    + "     <div class='box-footer'>"
                    + "         <button type='button' id='btnprintjewelry' class='btn btn-sm btn-primary pull-right'><i class='fa fa-print'></i> &nbsp; Cetak Laporan</button>"
                    + "     </div>";
        }
        html += ""
                + "     </div>"
                + " </div>"
                + "";

        return html;
    }
	
	private String getSummaryReportPenerimaanJewelryLebur(HttpServletRequest request) {
        String html = "";
        String where = " WHERE";
        where += " (dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_DRAFT + ""
                + " AND dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS] + " != " + I_DocStatus.DOCUMENT_STATUS_FINAL + ")";

        //get date
        String dateFromCatch = FRMQueryString.requestString(request, "" + FrmSrcReportDispatch.fieldNames[FrmSrcReportDispatch.FRM_FIELD_DATE_FROM] + "");
        String dateToCatch = FRMQueryString.requestString(request, "" + FrmSrcReportDispatch.fieldNames[FrmSrcReportDispatch.FRM_FIELD_DATE_TO] + "");
		String dispatchCode = FRMQueryString.requestString(request, "DISPATCH_CODE");
        String periode = "";
        
        Date dateFrom = Formater.formatDate(dateFromCatch, "MM/dd/yyyy");
        Date dateTo = Formater.formatDate(dateToCatch, "MM/dd/yyyy");

        String newDateFrom = Formater.formatDate(dateFrom, "yyyy-MM-dd");
        String newDateTo = Formater.formatDate(dateTo, "yyyy-MM-dd");

        String newLongDateFrom = Formater.formatDate(dateFrom, "dd/MMM/yyyy");
        String newLongDateTo = Formater.formatDate(dateTo, "dd/MMM/yyyy");

        if (dateFrom != null && dateTo != null) {
            int compareDate = dateFrom.compareTo(dateTo);
            if (compareDate == 0) {
                periode = "" + newLongDateFrom;
                where += " AND DATE(dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + ") = '" + newDateFrom + "'";
            } else {
                periode = "Dari " + newLongDateFrom + " sampai " + newLongDateTo;
                where += " AND DATE(dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + ") BETWEEN '" + newDateFrom + "' AND '" + newDateTo + "'";
            }
        } else if (dateFrom != null) {
            periode = "Dari " + newLongDateFrom + " sampai akhir";
            where += " AND DATE(dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + ") >= '" + newDateFrom + "'";
        } else if (dateTo != null) {
            periode = "Dari awal sampai " + newLongDateTo;
            where += " AND DATE(dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE] + ") <= '" + newDateTo + "'";
        } else {
            periode = "Semua tanggal";
        }

        //get location
        String locationMultiple[] = request.getParameterValues("" + FrmSrcReportDispatch.fieldNames[FrmSrcReportDispatch.FRM_FIELD_LOCATION_ID] + "");
        String locationInText = "";
        String locationShow = "";
        try {
            for (int i = 0; i < locationMultiple.length; i++) {
                if (!locationMultiple[i].equals("0")) {
                    if (locationInText.length() > 0) {
                        locationInText += "," + locationMultiple[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                        locationShow += ", " + l.getName();
                    } else {
                        locationInText = locationMultiple[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultiple[i]));
                        locationShow += "" + l.getName();
                    }
                }
            }
            if (locationInText.length() > 0) {
                where += " AND dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID] + " IN (" + locationInText + ")";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            locationShow = "Semua lokasi";
        }
		
		//get location to
        String locationMultipleTo[] = request.getParameterValues("" + FrmSrcReportDispatch.fieldNames[FrmSrcReportDispatch.FRM_FIELD_DISPATCH_TO] + "");
        String locationInTextTo = "";
        String locationShowTo = "";
        try {
            for (int i = 0; i < locationMultipleTo.length; i++) {
                if (!locationMultipleTo[i].equals("0")) {
                    if (locationInTextTo.length() > 0) {
                        locationInTextTo += "," + locationMultipleTo[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultipleTo[i]));
                        locationShow += ", " + l.getName();
                    } else {
                        locationInTextTo = locationMultipleTo[i];
                        Location l = PstLocation.fetchExc(Long.parseLong(locationMultipleTo[i]));
                        locationShow += "" + l.getName();
                    }
                }
            }
            if (locationInTextTo.length() > 0) {
                where += " AND dp." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO] + " IN (" + locationInTextTo + ")";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            locationShowTo = "Semua lokasi";
        }
		
		if (!dispatchCode.equals("")){
			where += " AND dp."+PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]+" LIKE '%"+dispatchCode+"%'";
		}

        
        html += ""
                + " <div class='col-md-12'>"
                + "     <div class='box box-primary'>"
                + "         <div class='box-header with-border text-center' style='border-bottom-color: lightgray'>"
                + "             <h3 class='box-title'>Laporan Summary Penerimaan</h3>"
                + "         </div>"
                + "         <div class='box-body'>"
                + "             <div class='col-sm-2'>"
                + "                 <p>Periode</p>"
                + "                 <p>Lokasi Asal</p>"
                + "                 <p>Lokasi Tujuan</p>"
                + "             </div>"
                + "             <div class='col-sm-10'>"
                + "                 <p>: &nbsp; " + periode + "</p>"
                + "                 <p>: &nbsp; " + locationShow + "</p>"
                + "                 <p>: &nbsp; " + locationShowTo + "</p>"
                + "             </div>"
                + "             <table class='table table-bordered' style='font-size: 14px'>"
                + "";

        double grandQty = 0;
        double grandBerat = 0;
        double grandTotal = 0;
        int recordData = 0;

            Vector listEmas = SessReportReceive.getReportReceiveSummaryJewelryLebur(where);
            double totalQty = 0;
            double totalBerat = 0;
            double totalTotal = 0;
            html += ""
                    + " <tr><th colspan='14' style=\"background-color: #e2e2e2\">" + Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS] + "</th></tr>"
                    + " <tr>"
                    + "     <th>No.</th>"
                    + "     <th>Tanggal</th>"
                    + "     <th>Kode Transfer Lebur</th>"
                    + "     <th>Qty</th>"
                    + "     <th>Berat</th>"
                    + "     <th>HEL/Rate</th>"
                    + "     <th>Ongkos</th>"
                    + "     <th>Total</th>"
                    + "     <th>Catatan</th>"
                    + " </tr>";

            for (int i = 0; i < listEmas.size(); i++) {
                recordData += 1;
                int no = i + 1;
                Vector v = (Vector) listEmas.get(i);
                MatDispatch mr = (MatDispatch) v.get(0);
                MatReceiveItem mri = (MatReceiveItem) v.get(1);
                
                totalQty += mri.getQty();
                totalBerat += mri.getBerat();
                totalTotal += mri.getTotal();
                html += ""
                        + " <tr>"
                        + "     <td>" + no + "</td>"
                        + "     <td>" + Formater.formatDate(mr.getDispatchDate(), "dd MMM yyyy") + "</td>"
						+ "     <td>" + mr.getDispatchCode()+ "</td>"
                        + "     <td class='text-right'>" + Formater.formatNumber(mri.getQty(), "#") + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.3f", mri.getBerat()) + "</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getCost()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getForwarderCost()) + ".00</td>"
                        + "     <td class='text-right'>" + String.format("%,.0f", mri.getTotal()) + ".00</td>"
                        + "     <td>" + mr.getRemark() + "</td>"
                        + " </tr>"
                        + "";
            }

            if (listEmas.isEmpty()) {
                html += ""
                        + "<tr><td colspan='14' style='background-color: ' class='text-center'><b>Tidak ada data penerimaan transfer lebur</b></td></tr>"
                        + "";
            } else {
                html += ""
                        + " <tr>"
                        + "     <td colspan='3'><b>Grand Total</b></td>"
                        + "     <td class='text-right'><b>" + Formater.formatNumber(totalQty, "#") + "</b></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.3f", totalBerat) + "</b></td>"
                        + "     <td colspan='2'></td>"
                        + "     <td class='text-right'><b>" + String.format("%,.0f", totalTotal) + ".00</b></td>"
                        + "     <td></td>"
                        + " </tr>"
                        + "";
            }

            grandQty += totalQty;
            grandBerat += totalBerat;
            grandTotal += totalTotal;

        html += ""
                + "             </table>"
                + "         </div>";
        if (recordData > 0) {
            html += ""
                    + "     <div class='box-footer'>"
                    + "         <button type='button' id='btnprintjewelry' class='btn btn-sm btn-primary pull-right'><i class='fa fa-print'></i> &nbsp; Cetak Laporan</button>"
                    + "     </div>";
        }
        html += ""
                + "     </div>"
                + " </div>"
                + "";

        return html;
    }

    private String getEtalaseByLocation(HttpServletRequest request) {
        String htmlOption = "";
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_LOCATION_ID");
        String useFor = FRMQueryString.requestString(request, "USE_FOR");
        
        String where = "";
        String order = ""+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+","+PstKsg.fieldNames[PstKsg.FLD_CODE];
        
        if (locationId != 0) {            
            where = ""+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"='"+locationId+"'";                        
        } 
        
        if (useFor.equals("stock_report_global")) {
            htmlOption += "<option value=''>All...</option>";
        }
        
        Vector listEtalase = PstKsg.list(0, 0, ""+where, ""+order);
        for (int i=0; i<listEtalase.size(); i++) {
            Ksg ksg = (Ksg) listEtalase.get(i);
            try {
                Location loc = PstLocation.fetchExc(ksg.getLocationId());
                if (useFor.equals("stock_report_global")) {
                    htmlOption += "<option value='"+ksg.getCode()+"'>"+loc.getCode()+" - "+ksg.getCode()+"</option>";
                } else {
                    htmlOption += "<option value='"+ksg.getOID()+"'>"+loc.getCode()+" - "+ksg.getCode()+"</option>";
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return htmlOption;
    }
    
    private void saveReceiveItem(HttpServletRequest request){
        String barcode = FRMQueryString.requestString(request, "BARCODE");
        long oidMatReceive = FRMQueryString.requestLong(request, "MATERIAL_RECEIVE_ID");
        String msg = "";
        this.msgReturn = "";
        this.iErrCode = 0;
        try {
            Material material = PstMaterial.fetchBySkuBarcode(barcode);
            MatReceive rec =  PstMatReceive.fetchExc(oidMatReceive);
            boolean isExist = PstMatReceiveItem.materialExist(material.getOID(), oidMatReceive);
            if (material.getOID() != 0){
                if (isExist){
                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem = PstMatReceiveItem.getObjectReceiveItem(rec.getInvoiceSupplier(), 0, material.getOID());
                    if (recItem.getOID() != 0){
                        recItem.setQty(recItem.getQty()+1);
                        recItem.setQtyEntry(recItem.getQtyEntry()+1);
                        try {
                            PstMatReceiveItem.updateExc(recItem);
                         } catch (Exception exc){

                        }
                    } else {

                    }
                } else {
                    String whereMatMapKsg = "KSG."+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"="+rec.getLocationId()+" AND "
                                        + "MAP."+PstMatMappKsg.fieldNames[PstMatMappKsg.FLD_MATERIAL_ID]+"="+material.getOID();
                    Vector listMatMapKsg = PstMatMappKsg.listJoinKsg(0, 0, whereMatMapKsg, "");

                    Ksg ksg = new Ksg();
                    if (listMatMapKsg.size()>0){
                        try {
                            MatMappKsg matMapKsg = (MatMappKsg) listMatMapKsg.get(0);
                            ksg = PstKsg.fetchExc(matMapKsg.getKsgId());
                        } catch (Exception exc){

                        }
                    }
                    MatReceiveItem recItem = new MatReceiveItem();
                    recItem.setReceiveMaterialId(oidMatReceive);
                    recItem.setMaterialId(material.getOID());
                    recItem.setUnitId(material.getDefaultStockUnitId());
                    recItem.setUnitKonversi(material.getDefaultStockUnitId());
                    recItem.setCost(material.getAveragePrice());
                    recItem.setCurrencyId(material.getDefaultCostCurrencyId());
                    recItem.setQty(1);
                    recItem.setTotal(recItem.getQty() * recItem.getCost());
                    recItem.setResidueQty(1);
                    recItem.setQtyEntry(1);
                    recItem.setColorId(material.getPosColor());
                    recItem.setGondolaId(ksg.getOID());
                    long oidMatRecItem = PstMatReceiveItem.insertExc(recItem);
                    
                    Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, 
                            PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
                    if (listGroupMapping.size()>0){
                        for (int x=0;x< listGroupMapping.size();x++){
                            MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                            MasterGroup masterGroup = new MasterGroup();
                            try {
                                masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                            } catch (Exception exc){}
                            long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(),material.getOID());
                            MaterialTypeMapping map = new MaterialTypeMapping();
                            map.setMaterialId(oidMatRecItem);
                            map.setTypeId(oidMapping);
                            map.setNote("");
                            try {
                                PstMaterialMappingType.insertExc(map);
                            } catch (Exception exc){

                            }
                        }
                    }
                }
            } else {
                //added by dewok 2019-01-14
                this.msgReturn = "Item tidak terdaftar";
                this.iErrCode = 1;
            }
        } catch (Exception exc){
            msg = "No Item Found";
        }
    }
    
    private void saveReturnItem(HttpServletRequest request) {
        this.msgReturn = "";
        String barcode = FRMQueryString.requestString(request, "BARCODE");
        long oidMatReturn = FRMQueryString.requestLong(request, "OID_MAT_RETURN");
        long oidLocation = FRMQueryString.requestLong(request, "OID_LOCATION");
        //cek barcode
        Vector<Material> listMaterial = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_BARCODE] + " = '" + barcode + "'", null);
        if (listMaterial.isEmpty()) {
            this.msgReturn = "Item tidak terdaftar";
            this.iErrCode += 1;
            return;
        }
        if (listMaterial.size() > 1) {
            this.msgReturn = "Terdapat " + listMaterial.size() + " item dengan barcode yang sama";
            this.iErrCode += 1;
            return;
        }
        
        try {
            MatReturn mr = PstMatReturn.fetchExc(oidMatReturn);
            Material m = listMaterial.get(0);
            
            //cek qty stock
            double qtyStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(m.getOID(), oidLocation, new Date(), 0);
            if (qtyStock <= 0) {
                this.msgReturn = "Sisa stok untuk item " + m.getName()+ " adalah " + qtyStock;
                this.iErrCode += 1;
                return;
            }
            //cek apakah item sudah ada di dokumen return
            String where = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + " = " + mr.getOID()
                    + " AND " + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_MATERIAL_ID] + " = " + m.getOID();
            Vector<MatReturnItem> listItemReturn = PstMatReturnItem.list(0, 0, where, null);
            if (listItemReturn.isEmpty()) {
                //input baru
                double hargaJual = PstPriceTypeMapping.getSellPrice(m.getOID(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                MatReturnItem mri = new MatReturnItem();
                mri.setReturnMaterialId(mr.getOID());
                mri.setMaterialId(m.getOID());
                mri.setUnitId(m.getDefaultStockUnitId());
                mri.setQty(1);
                mri.setHargaJual(hargaJual);
                mri.setTotal(hargaJual);
                mri.setResidueQty(1);
                PstMatReturnItem.insertExc(mri);
            } else {
                MatReturnItem mri = listItemReturn.get(0);
                //compare qty stock dengan qty return yg sudah disimpan agar tidak menyimpan return lebih dari qty stock
                if (mri.getQty() < qtyStock) {
                    //update qty +1
                    mri.setQty(mri.getQty() + 1);
                    mri.setTotal(mri.getTotal() + mri.getHargaJual());
                    mri.setResidueQty(mri.getResidueQty() + 1);
                    PstMatReturnItem.updateExc(mri);
                } else {
                    this.msgReturn = "Sisa stok untuk item " + m.getName()+ " adalah " + (qtyStock - mri.getQty());
                    this.iErrCode += 1;
                    return;
                }
            }
        } catch (Exception e) {
            this.msgReturn = e.getMessage();
            this.iErrCode += 1;
        }
    }
    
    private String listMaterialItem(HttpServletRequest request){
        long oidMatReceive = FRMQueryString.requestLong(request, "MATERIAL_RECEIVE_ID");
        int startItem = FRMQueryString.requestInt(request, "START");
        int record = FRMQueryString.requestInt(request, "RECORD");
        boolean privShowQty = FRMQueryString.requestBoolean(request, "PRIV_SHOW_QTY");
        String readonlyQty = "";
        String syspropDiscount1 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_1");
        String syspropDiscount2 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_2");
        String syspropDiscountNominal = PstSystemProperty.getValueByName("SHOW_DISCOUNT_NOMINAL");
        String syspropOngkosKirim = PstSystemProperty.getValueByName("SHOW_ONGKOS_KIRIM");
        String syspropBonus = PstSystemProperty.getValueByName("SHOW_BONUS");
        String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
        String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
        String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
        String syspropTotalBeli = PstSystemProperty.getValueByName("SHOW_TOTAL_BELI");
        String syspropAutoSave = PstSystemProperty.getValueByName("AUTO_SAVE_RECEIVING");
        int SESS_LANGUAGE = FRMQueryString.requestInt(request, "SESS_LANGUAGE");
        long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "OID_REC_MAT_ITEM");
        boolean privShowQtyPrice = FRMQueryString.requestBoolean(request, "PRIV_SHOW_QTY_PRICE");
        int typeOfBusinessDetail = FRMQueryString.requestInt(request, "TYPE_OFF_BUSINESS");
        String currCode = FRMQueryString.requestString(request, "CURR_CODE");
        int recType = FRMQueryString.requestInt(request, "REC_TYPE");
        this.iCommand = Command.ADD;
        
        CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
        FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
        MatReceiveItem recItem = null;
        MatReceive rec =  new MatReceive();
        try{
            rec = PstMatReceive.fetchExc(oidMatReceive);
        } catch (Exception exc){}
        
        String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidMatReceive
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=0";
        String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
        
        Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem, record, whereClauseItem, orderClauseItem);
        
        Vector list = drawListRetItem(SESS_LANGUAGE, iCommand, frmMatReceiveItem, recItem, listMatReceiveItem, oidReceiveMaterialItem, startItem, privShowQtyPrice, readonlyQty, approot, syspropDiscount1, syspropDiscount2, syspropDiscountNominal, syspropOngkosKirim, syspropBonus, syspropHargaBeli, typeOfBusinessDetail, rec.getReceiveItemType(), currCode, rec.getReceiveSource(), syspropEtalase, syspropColor, recType, syspropTotalBeli, rec.getLocationId(), syspropAutoSave);
        return ""+list.get(0);
    }
    
    public Vector drawListRetItem(int language, int iCommand, FrmMatReceiveItem frmObject, 
            MatReceiveItem objEntity, Vector objectClass, long recItemId, int start, 
            boolean privShowQtyPrice, String readOnlyQty, String approot, String syspropDiscount1, 
            String syspropDiscount2, String syspropDiscountNominal, String syspropOngkosKirim, 
            String syspropBonus, String syspropHargaBeli, int typeOfBusinessDetail, int recItemType, 
            String currCode, int recSource, String syspropColor, String syspropEtalase, 
            int recType, String syspropTotalBeli, long locationId, String syspropAutoSave) {

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");        
        ctrlist.addHeader(textListOrderItem[language][0], "1%");//no
        ctrlist.addHeader(textListOrderItem[language][1], "17%");//sku
        ctrlist.addHeader(textListOrderItem[language][2], "20%");//nama brg
        if (bEnableExpiredDate) {
            ctrlist.addHeader(textListOrderItem[language][3], "8%");
        }
        if (typeOfBusinessDetail != 2) {
        ctrlist.addHeader(textListOrderItem[language][14], "5%");//qty entry
        ctrlist.addHeader(textListOrderItem[language][15], "5%");//unit konv
        }
        if (privShowQtyPrice) {
            ctrlist.addHeader(textListOrderItem[language][8], "7%");//qty            
            ctrlist.addHeader(textListOrderItem[language][4], "5%");//unit                 
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader(textListOrderItem[language][18], "5%");//berat
            }
            if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))){
                    if (typeOfBusinessDetail == 2) {
                        if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Harga Emas (" + currCode + ")", "7%");
                        } else {
                            ctrlist.addHeader(textListOrderItem[language][5] + " (" + currCode + ")", "7%");
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][5], "7%");
                    }
                }
            }
            if (syspropDiscount1.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][11], "5%");//discount 1
            }
            if (syspropDiscount2.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][12], "5%");//discount 2
            }
            if (syspropDiscountNominal.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][13], "7%");//discount nominal
            }
            if (syspropOngkosKirim.equals("1")) {
                if (typeOfBusinessDetail == 2) {                    
                    ctrlist.addHeader("Oks/Batu (" + currCode + ")", "7%");//ongkos kirim
                } else {
                    ctrlist.addHeader(textListOrderItem[language][6], "7%");//ongkos kirim
                }
            }
            if (syspropBonus.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][17], "7%");//bonus
            }
            if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))){
                    if (typeOfBusinessDetail == 2) {
                        if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                            ctrlist.addHeader("Harga Pokok (Rp)", "7%");
                        } else if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                            ctrlist.addHeader("Total HP (Rp)", "7%");//Total HP
                        }
                    } else {
                        ctrlist.addHeader(textListOrderItem[language][9], "7%");
                    }
                }
            }
        } else {
            ctrlist.addHeader(textListOrderItem[language][8], "9%");
            if (syspropDiscount1.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][11], "9%");
            }
            if (syspropDiscount2.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][12], "9%");
            }
            if (syspropDiscountNominal.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][13], "9%");
            }
            if (syspropOngkosKirim.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][6], "9%");
            }
            ctrlist.addHeader(textListOrderItem[language][9], "9%");
            if (syspropBonus.equals("1")) {
                ctrlist.addHeader(textListOrderItem[language][17], "7%");//bonus
            }
        }
        if (syspropEtalase.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][22], "7%");//bonus
        }
        if (syspropColor.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][21], "7%");//bonus
        }
        ctrlist.addHeader(textListOrderItem[language][19], "7%");//keterangan
        if(typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK){
            ctrlist.addHeader(textListOrderItem[language][20],"5%");//sorting
        }
        Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, 
                                                PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
        if (listGroupMapping.size()>0){
             for (int i=0;i< listGroupMapping.size();i++){
                MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(i);
                MasterGroup masterGroup = new MasterGroup();
                try {
                    masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                } catch (Exception exc){}
                ctrlist.addHeader(masterGroup.getNamaGroup(), "7%");//
            }
        }
        ctrlist.addHeader(textListOrderItem[language][16], "1%");//hapus
        /**
         * add opie 28-06-2012 untuk konversi
         */
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0, 1000, whereUnit, "");
        Vector index_value = new Vector(1, 1);
        Vector index_key = new Vector(1, 1);
        index_key.add("-");
        index_value.add("0");
        for (int i = 0; i < listBuyUnit.size(); i++) {
            Unit mateUnit = (Unit) listBuyUnit.get(i);
            index_key.add(mateUnit.getCode());
            index_value.add("" + mateUnit.getOID());
        }

        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }
        Vector vWarna = PstColor.listAll();
        Vector warna_Val = new Vector(1,1);
        Vector warna_Key = new Vector(1,1);
        //warna_Val.add("0");
        //warna_Key.add("-");    
        for(int i=0; i<vWarna.size(); i++){
            Color color = (Color) vWarna.get(i);
            warna_Val.add(""+color.getOID());
            warna_Key.add(""+color.getColorCode()+" - "+color.getColorName());
        }

        Vector listKsg = PstKsg.list(0,0,PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"="+locationId,PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+","+PstKsg.fieldNames[PstKsg.FLD_CODE]);
        Vector vectKsgVal = new Vector(1,1);
        Vector vectKsgKey = new Vector(1,1);
        //vectKsgKey.add("Semua");
        //vectKsgVal.add("0");
        for(int i=0; i<listKsg.size(); i++){                                            
            Ksg matKsg = (Ksg)listKsg.get(i);
            Location l = new Location();
            if (matKsg.getLocationId() != 0) {
                try {
                    l = PstLocation.fetchExc(matKsg.getLocationId());
                } catch (Exception exc){}
            }
            vectKsgKey.add(matKsg.getCode()+" - "+l.getCode());
            vectKsgVal.add(""+matKsg.getOID());
        }

        int priceReadOnly = Integer.parseInt(PstSystemProperty.getValueByName("PRICE_RECEIVING_READONLY"));
        String read = "";
        if (priceReadOnly == 1) {
            read = "readonly";
        }
        
        //added by dewok 20190114, sum total qty entry
        double totalQtyEntry = 0;
        
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);

            totalQtyEntry += recItem.getQtyEntry();
                    
            //added by dewok 2018 for jewelry
            String itemName = SessMaterial.setItemNameForLitama(recItem.getMaterialId());

            rowx = new Vector();
            start = start + 1;
            double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
            Unit untiKonv = new Unit();
            try {
                untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
            } catch (Exception ex) {

            }
            Ksg ksg = new Ksg();
            try {
                ksg = PstKsg.fetchExc(recItem.getGondolaId());
            } catch (Exception exc){

            }
            
            Color color = new Color();
            try {
                color = PstColor.fetchExc(recItem.getColorId());
            } catch (Exception exc){

            }

            if (recItemId == recItem.getOID()) {
                index = i;
            }
            if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                rowx.add("" + start);
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + recItem.getMaterialId()
                        + "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\"" + mat.getSku() + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
                //update opie-eyek 20130812
                if (privShowQtyPrice) {
                    if (mat.getCurrBuyPrice() < recItem.getCost()) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + itemName + "\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                        } else {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                        }
                    } else {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + itemName + "\" class=\"hiddenText\" readOnly> <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">[Edit]</a>");
                        } else {
                            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;[Edit]</a>");
                        }
                    }
                } else {
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly>");
                }

                if (bEnableExpiredDate) {
                    rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate() == null) ? new Date() : recItem.getExpiredDate(), 5, -5, "formElemen", ""));
                }

                //add opie-eyek 20140108 untuk konversi satuan
                if (typeOfBusinessDetail != 2) {
                    if (readOnlyQty == "readonly") {
                        rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" " + readOnlyQty + "></div>"
                            + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" " + readOnlyQty + ">");
                    } else {
                        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + recItem.getQtyEntry() + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
                    }                
                    rowx.add("<div align=\"center\">" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, "" + untiKonv.getOID(), index_value, index_key, "onChange=\"javascript:showData(this.value)\"", "formElemen") + " </div>");
                }
                
                if (privShowQtyPrice) {
                    if (readOnlyQty == "readonly") {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" " + readOnlyQty + " ></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }                    
                        rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + recItem.getUnitId()
                            + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"hiddenText\" readOnly>");
                    
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\"><input type=\"text\" onKeyUp=\"javascript:cntTotal(this, event)\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + recItem.getBerat() + "\" class=\"formElemen\" style=\"text-align:right\"></div>");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))){
                            if (typeOfBusinessDetail == 2) {
                                if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value='" + String.format("%,.0f",recItem.getCost()) + "' class=\"formElemen hiddenText\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                } else if (recItemType == Material.MATERIAL_TYPE_BERLIAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value='" + String.format("%,.0f",recItem.getCost()) + "' class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                }
                            } else {
                                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                            }
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "\" class=\"formElemen\" " + read + "  onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))){
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"total_cost\" value=\"" + String.format("%,.0f", recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>"
                                        + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                            } else {
                                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>"
                                        + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                            }
                        }
                    }
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "\" class=\"hiddenText\" readOnly></div>");
                    }
                    rowx.add("<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\">");
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "><div align=\"right\">");
                    rowx.add("<input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID], null, "" + recItem.getGondolaId(),  vectKsgVal, vectKsgKey,""));
                }
                if (syspropColor.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_COLOR_ID], null, "" + recItem.getColorId(),  warna_Val, warna_Key,""));
                }
                rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + recItem.getRemark() + "\" class=\"formElemen\">");
                if(typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK){
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_PREV_MATERIAL_ID] + "\" value=\"" + recItem.getPrevMaterialId()
                        + "\"> "+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SORTING_STATUS], null, "" + recItem.getSortStatus(),  PstMatReceiveItem.getSortingVal(), PstMatReceiveItem.getSortingKey(),""));
                }
                if (listGroupMapping.size()>0){
                     for (int x=0;x< listGroupMapping.size();x++){
                        MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                        MasterGroup masterGroup = new MasterGroup();
                        try {
                            masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                        } catch (Exception exc){}
                        Vector vValue = new Vector(1,1);
                        Vector vKey = new Vector(1,1);

                        vValue.add("0");
                        vKey.add("-");

                        Vector listType = PstMasterType.list(0, 0, 
                                PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                        long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(),recItem.getOID());
                        if (listType.size() > 0){
                            for (int n=0; n < listType.size(); n++){
                                MasterType masterType = (MasterType) listType.get(n);
                                vValue.add(""+masterType.getOID());
                                vKey.add(masterType.getMasterName());
                            }
                        }
                        rowx.add("<input type=\"hidden\" name=\"groupSize\" value=\"" + listGroupMapping.size()
                        + "\"> "+ControlCombo.draw("MASTER_GROUP"+masterGroup.getTypeGroup(), null, "" + oidMapping,  vValue, vKey,""));
                    }
                }
                rowx.add("<div align=\"right\"></div>");//hapus
            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                //update opie-eyek 20130812
                if (privShowQtyPrice) {
                    if (mat.getCurrBuyPrice() < recItem.getCost()) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add(itemName + " <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\"><img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                        } else {
                            rowx.add(mat.getName() + "<a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                        }
                    } else {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add(itemName + " <a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">[Edit]</a>");
                        } else {
                            rowx.add(mat.getName() + "<a href=\"javascript:cmdHargaJual('" + String.valueOf(recItem.getMaterialId()) + "')\">&nbsp;&nbsp;[Edit]</a>");
                        }
                    }
                } else {
                    rowx.add(mat.getName());
                }

                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }
                if (typeOfBusinessDetail != 2) {                    
                    rowx.add("<div align=\"center\">" + recItem.getQtyEntry() + "</div>");
                    rowx.add("<div align=\"center\">" + untiKonv.getCode() + "</div>");
                }                

                if (privShowQtyPrice) {
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getQty()) + "</div>");
                    } else {
						
						if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
							String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItem.getOID();
							int cnt = PstReceiveStockCode.getCount(where);
							double recQtyPerBuyUnit = recItem.getQty();
							double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
							double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
							double max = recQty;

							if (cnt < max) {
								if (listError.size() == 0) {
									listError.add("Silahkan cek :");
								}
								listError.add("" + listError.size() + ". Jumlah serial kode stok " + mat.getName() + " tidak sama dengan qty terima");
							}
							rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(recItem.getOID()) + "')\">[ST.CD]</a> " + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");

						} else {
						
							rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
						}
                    }
                    
                    rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
                    
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<div align=\"right\">" + String.format("%,.3f", recItem.getBerat()) + "</div>");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))){
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getCost()) + "</div>");
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                            }
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount()) + "</div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscount2()) + "</div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getDiscNominal()) + "</div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        if (typeOfBusinessDetail == 2) {
                            rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getForwarderCost()) + "</div>");
                        } else {
                            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "</div>");
                        }
                    }
                }
                
				if (syspropBonus.equals("1")) {
					rowx.add("<div align=\"right\"></div>");//bonus
				}
                if (privShowQtyPrice) {
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))){
                            if (typeOfBusinessDetail == 2) {
                                rowx.add("<div align=\"right\">" + String.format("%,.0f", recItem.getTotal() + totalForwarderCost) + "</div>");
                            } else {
                                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal() + totalForwarderCost) + "</div>");
                            }
                        }
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add("<div align=\"right\">"+ksg.getName()+"</div>");
                }
                if (syspropColor.equals("1")) {
                    rowx.add("<div align=\"right\">"+color.getColorName()+"</div>");
                }
                rowx.add("<div align=\"\">" + recItem.getRemark() + "</div>");
                if(typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK){
                    rowx.add("<div align=\"right\">"+PstMatReceiveItem.sortingKey[recItem.getSortStatus()]+"</div>");
                }
                if (listGroupMapping.size()>0){
                     for (int x=0;x< listGroupMapping.size();x++){
                        MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                        MasterGroup masterGroup = new MasterGroup();
                        try {
                            masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                        } catch (Exception exc){}
                        Vector vValue = new Vector(1,1);
                        Vector vKey = new Vector(1,1);

                        vValue.add("0");
                        vKey.add("-");

                        Vector listType = PstMasterType.list(0, 0, 
                                PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                        long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(),recItem.getOID());
                        MasterType masterType = new MasterType();
                        String typeName = "";
                        try {
                            masterType = PstMasterType.fetchExc(oidMapping);
                            typeName = masterType.getMasterName();
                        } catch (Exception exc){
                            typeName = "-";
                        }
                        rowx.add("<div align=\"left\">"+typeName+"</div>");
                    }
                }
                // add by fitra 17-05-2014
                rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><img src=\"" + approot + "/images/x3.png\" align=\"center\" ></a></div>");

            }
            lstData.add(rowx);
        }
        
        //added by dewok 20190114, sum total qty entry
        try {
            this.jSONObject.put("FRM_FIELD_TOTAL_QTY_ENTRY", ""+totalQtyEntry);
        } catch (JSONException ex) {
            Logger.getLogger(AjaxPenerimaan.class.getName()).log(Level.SEVERE, null, ex);
        }

        rowx = new Vector();

        if (readOnlyQty == "readonly") {

        } else {
            if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
                rowx.add("" + (start + 1));
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + ""
                        + "\"><input type=\"text\" id='barcode' size=\"15\" name=\"matCode\" value=\"" + "" + "\" class=\"formElemen\" "+(!syspropAutoSave.equals("1") ? "onKeyDown=\"javascript:keyDownCheck(event)\"" : "" )+"> <a href=\"javascript:cmdCheck()\">CHK</a>");
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
                if (bEnableExpiredDate) {
                    rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 5, -5, "formElemen", ""));
                }
                //add opie-eyek 20140108 untuk konversi satuan
                if (typeOfBusinessDetail != 2) {
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] + "\" value=\"" + "" + "\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" </div>");
                    rowx.add("<div align=\"center\">" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, "" + 0, index_value, index_key, "onChange=\"javascript:showData(this.value)\"", "formElemen") + " </div>");
                }
                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + ""
                            + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + "" + "\" class=\"hiddenText\" readOnly>");
                    
                    if (typeOfBusinessDetail == 2) {
                        rowx.add("<input type=\"text\" onKeyUp=\"javascript:cntTotal(this, event)\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + "\" class=\"formElemen\" style=\"text-align:right\">");
                    }
                    if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals("2") || syspropHargaBeli.equals("3")) {
                        if (syspropHargaBeli.equals("1") || syspropHargaBeli.equals(String.valueOf(recType))){
                            if (typeOfBusinessDetail == 2) {
                                if (recItemType == Material.MATERIAL_TYPE_EMAS || recItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen hiddenText\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                } else {
                                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                                }
                            } else {
                                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\"><div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                            }
                        }
                    }
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                    if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals("2") || syspropTotalBeli.equals("3")) {
                        if (syspropTotalBeli.equals("1") || syspropTotalBeli.equals(String.valueOf(recType))){
                            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"total_cost\" value=\"" + "" + "\" " + read + " onKeyUp=\"javascript:cntTotal3(this, event)\">"
                                    + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" readOnly></div>");
                        }
                    }
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                            + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\">"
                            + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    if (syspropDiscount1.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscount2.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropDiscountNominal.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    if (syspropOngkosKirim.equals("1")) {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + "" + "\" class=\"formElemen\" " + read + " onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                    }
                    rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\"" + "" + "\" " + read + " onKeyUp=\"javascript:cntTotal3(this, event)\">"
                            + "<input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" readOnly></div>");
                    if (syspropBonus.equals("1")) {
                        rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BONUS] + "\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                    }
                }
                if (syspropEtalase.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID], null, "",  vectKsgVal, vectKsgKey,""));
                }
                if (syspropColor.equals("1")) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_COLOR_ID], null, "",  warna_Val, warna_Key,""));
                }
                rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_REMARK] + "\" value=\"" + "\" class=\"formElemen\">");
                if(typeOfBusinessDetail == 2 && recSource == PstMatReceive.SOURCE_FROM_BUYBACK){
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_PREV_MATERIAL_ID] + "\" value=\"0 \"> "+
                            ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SORTING_STATUS], null, null,  PstMatReceiveItem.getSortingVal(), PstMatReceiveItem.getSortingKey(),""));
                }
                if (listGroupMapping.size()>0){
                     for (int x=0;x< listGroupMapping.size();x++){
                        MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                        MasterGroup masterGroup = new MasterGroup();
                        try {
                            masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                        } catch (Exception exc){}
                        Vector vValue = new Vector(1,1);
                        Vector vKey = new Vector(1,1);

                        vValue.add("0");
                        vKey.add("-");

                        Vector listType = PstMasterType.list(0, 0, 
                                PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP]+"="+masterGroup.getTypeGroup(),
                                PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                        if (listType.size() > 0){
                            for (int n=0; n < listType.size(); n++){
                                MasterType masterType = (MasterType) listType.get(n);
                                vValue.add(""+masterType.getOID());
                                vKey.add(masterType.getMasterName());
                            }
                        }
                        rowx.add("<input type=\"hidden\" name=\"groupSize\" value=\"" + listGroupMapping.size()
                        + "\"> "+ControlCombo.draw("MASTER_GROUP"+masterGroup.getTypeGroup(), null, null,  vValue, vKey,""));
                    }
                }
                rowx.add("");
                lstData.add(rowx);
            }
        }

        list.add(ctrlist.draw());
        list.add(listError);
        return list;
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
