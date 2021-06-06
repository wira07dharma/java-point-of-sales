/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.posbo.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.pos.entity.billing.BillMain;
import com.dimata.pos.entity.billing.Billdetail;
import com.dimata.pos.entity.billing.PstBillDetail;
import com.dimata.pos.entity.billing.PstBillMain;
import com.dimata.pos.form.billing.CtrlBillDetail;
import com.dimata.pos.form.billing.CtrlBillMain;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.MappingProductLocationStoreRequest;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMappingProductLocationStoreRequest;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PurchaseOrderItem;
import com.dimata.posbo.form.masterdata.CtrlMappingProductLocationStoreRequest;
import com.dimata.posbo.form.purchasing.CtrlPurchaseOrderItem;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AjaxMappingItemStore extends HttpServlet{
    
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
        
        response.getWriter().print(this.jSONObject);
    }
    
    public void commandNone(HttpServletRequest request,HttpServletResponse response){
	if (dataFor.equals("showTabByLocation")){
            this.htmlReturn = showTabByLocation(request);
        }else if (dataFor.equals("showMappingForm")){
            this.htmlReturn = showMappingForm(request,response);
        }else if (dataFor.equals("showRecapitulation")){
            this.htmlReturn = showRecapitulation(request,response);
        }
    }
    
    public void commandSave(HttpServletRequest request,HttpServletResponse response){
        if (dataFor.equals("saveItemMapping")){
            this.htmlReturn =  saveItemMapping(request);
        }else if (dataFor.equals("saveAllItem")){
            this.htmlReturn = saveAllItem(request,response);
        }else if (dataFor.equals("saveIssue")){
            this.htmlReturn = saveIssue(request);
        }
    }
    
    public void commandList(HttpServletRequest request){
        if (dataFor.equals("listItemMapping")){
            this.htmlReturn =  listItemMapping(request);
        }else if (dataFor.equals("showAllItem")){
            this.htmlReturn= showAllItemOrder(request);
        }
    }
    
    private String listItemMapping(HttpServletRequest request){
        String htmlReturn = "";
        String whereClause, whereClauseMaterial= "";
        
        long locationOid = FRMQueryString.requestLong(request, "FRM_FIELD_OID_LOCATION");
        long categoryId = FRMQueryString.requestLong(request, "FRM_FIELD_CATEGORYID");
        
        Location entLocation = new Location();
        try {
            entLocation = PstLocation.fetchExc(locationOid);
        } catch (Exception e) {
        }
        
        whereClause =""
        + " "+PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]+"='"+entLocation.getCompanyId()+"'"
        + " AND "+PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]+"<>'0'";
        
        
        
        
        Vector listLocationMapping = PstLocation.list(0, 0, whereClause, "");
        
        
        whereClauseMaterial = ""
        + " "+PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_TYPE]+"='0'";
        
        if (categoryId>0){
            whereClauseMaterial += ""
                + " AND ("+PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID]+"="+categoryId+"";
            
            String where = ""
                + " "+ PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]+"='"+categoryId+"' "
                + " OR "+ PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"='"+categoryId+"' ";
            
            Vector masterCatAcak = PstCategory.list(0,0,where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
            
            if(masterCatAcak.size()>1){
                for(int i=0; i<masterCatAcak.size(); i++) {
                    Category mGroup = (Category)masterCatAcak.get(i);
                    whereClauseMaterial += " "
                        + "OR " + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] +
                        " = " + mGroup.getOID();
                }
            }
            whereClauseMaterial += ")";
        }
        
        Vector listMaterial = PstMaterial.list(0, 0, whereClauseMaterial,"");
        

        htmlReturn= ""
            + "<div  class='row' style='margin-left:0px; margin-right:0px; margin-top:10px;'>"
                + "<div class='col-md-12'>"
                    + "<div id='grafik' >"
                        + "<div  class='box box-primary'>"
                            + "<div class='box-header'>"
                                + "<h3 class='box-title'></h3>"
                                + "<div class='box-tools pull-right'>"
                                    + "<button class='btn btn-box-tool' data-widget='collapse'><i class='fa fa-minus'></i></button>"
                                + "</div>"
                            + "</div>"
                            + "<div class='box-body'>"
                                + "<table class='table table-bordered table-stripted'>"
                                    + "<thead>"
                                        + "<tr>"
                                            + "<td>ITEM | LOKASI</td>";
                                            for (int i = 0; i<listLocationMapping.size();i++){
                                                Location entLocationTemp = new Location();
                                                entLocationTemp = (Location)listLocationMapping.get(i);
                                                htmlReturn+= ""
                                                    + "<td>"+entLocationTemp.getName()+"</td>";
                                            }                                           
                                    htmlReturn += ""
                                        + "</tr>"
                                    + "</thead>"
                                    + "<tbody>";
                                        for (int a= 0; a<listMaterial.size();a++){
                                            String nameMaterial ="";
                                            Material material = new Material();
                                            material = (Material)listMaterial.get(a); 
                                            
                                            try {
                                                String arrName [] = material.getName().split(";");
                                                nameMaterial = arrName[0];
                                            } catch (Exception e) {
                                                nameMaterial = material.getName();
                                            }
                                            htmlReturn += ""
                                            + "<tr>"
                                                + "<td>"+nameMaterial+"</td>";
                                            for (int b = 0; b<listLocationMapping.size();b++){
                                                Location entLocationTemp = new Location();
                                                entLocationTemp = (Location)listLocationMapping.get(b);
                                                
                                                String whereGetCount = ""
                                                    + " "+PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_LOCATION_ID]+"='"+entLocationTemp.getOID()+"'"
                                                    + " AND "+PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_MATERIAL_ID]+"='"+material.getOID()+"'";
                                                
                                                int count = PstMappingProductLocationStoreRequest.getCount(whereGetCount);
                                                String check = "";
                                                if (count>0){
                                                    check = "checked";
                                                }
                                                
                                                htmlReturn += ""
                                                + "<td>"
                                                    + "<input "+check+" type='checkbox' class='checkValue' value='"+material.getOID()+"-"+entLocationTemp.getOID()+"'>"
                                                + "</td>";
                                            }
                                            htmlReturn += ""
                                            + "</tr>";
                                        }
                                        
                                       
                                    htmlReturn += ""
                                    + "</tbody>"
                                + "</table>"
                                + "<br>"
                                + "<br>";
                                if (listMaterial.size()>0 || listLocationMapping.size()>0){
                                    htmlReturn += ""
                                    + "<button type='button' class='btn btn-primary pull-right' id='saveMapping'>Save Mapping</buttton>";

                                }
                                
                            htmlReturn += ""
                            + "</div>"
                            + "<div class='box-footer'>"
                                + "&nbsp;"
                            + "</div>"
                        + "</div>"
                    + "</div>"
                + "</div>"
            + "</div>";
        
                     
        return htmlReturn;
    }
    
    private String saveItemMapping(HttpServletRequest request){
        String result ="";
        
        String value = FRMQueryString.requestString(request, "FRM_FIELD_OID_ITEM_LOCATION");
        long oidMainLocation = FRMQueryString.requestLong(request, "FRM_FIELD_MAIN_LOCATION");
        
        CtrlMappingProductLocationStoreRequest ctrlMappingProductLocationStoreRequest = new CtrlMappingProductLocationStoreRequest(request);
        int iError = ctrlMappingProductLocationStoreRequest.action2(Command.SAVEALL, 0, value, oidMainLocation);
        
        result = String.valueOf(iError);
        return result;
    }
    
    private String showTabByLocation(HttpServletRequest request){
        String htmlReturn="";
        
        long locationOid = FRMQueryString.requestLong(request, "FRM_FIELD_OID_LOCATION");
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_OID_LOCATION");
        long poId = FRMQueryString.requestLong(request, "FRM_FIELD_PO_ID");
        
        Location entLocation = new Location();
        try {
            entLocation = PstLocation.fetchExc(locationOid);
        } catch (Exception e) {
        }
        
        String whereLocation = ""
            + " "+PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]+"='"+entLocation.getCompanyId()+"'"
            + " AND "+PstLocation.fieldNames[PstLocation.FLD_DEPARTMENT_ID]+"<>'0'";
        
        Vector listLocation = PstLocation.list(0, 0, whereLocation, "");
        
        htmlReturn += ""
            + " <div class='nav-tabs-custom'>"
                + " <ul class='nav nav-tabs'>";
                    long oidActiveTab = 0;
                    for (int i = 0; i<listLocation.size();i++){   
                        String active = "";
                        Location entLocationTemp = new Location();
                        entLocationTemp = (Location)listLocation.get(i);
                        if (i ==0){
                            active= "active";
                            oidActiveTab = entLocationTemp.getOID();
                        }
                        htmlReturn += ""
                        + "<li data-oidlocation='"+entLocationTemp.getOID()+"' class=' tabhead "+active+"'><a href='#tabContent' data-toggle='tab'>"+entLocationTemp.getName()+"</a></li>";
                    }
                htmlReturn += ""
                + " </ul>"
                + "<input type='hidden' id='FRM_FIELD_OID_ACTIVE_TAB' value='"+oidActiveTab+"'>"
                + "<input type='hidden' id='FRM_FIELD_SIZE_LOCATION' value='"+listLocation.size()+"'>"
                + "<div class='tab-content'>"
                    + "<div class='tab-pane active' id='tabContent'>"
                        
                    + "</div>"
                + "</div>"               
            + " </div>";
            if (listLocation.size()>0){
                htmlReturn += "<button id='nextAfterFill' data-poid='"+poId+"' data-locationid='"+locationOid+"' type='button' class='btn btn-primary pull-right'>"+textListButton[language][3]+"</button>";
            }
            
        
        
        
        return htmlReturn;
    }
    
    private boolean chekValueExist (Hashtable<String,Integer> hashTemp, String key){
        boolean returnValue = false;
        
        try {
            returnValue = hashTemp.containsKey(key);
        } catch (Exception e) {
            returnValue = false;
        }
        
        return returnValue;
    }
    
    private String showMappingForm(HttpServletRequest request,HttpServletResponse response){
        String htmlReturn = "";
        
        long locationId = FRMQueryString.requestLong(request, "FRM_FIELD_OID_LOCATION");
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        String valueTemp = FRMQueryString.requestString(request, "FRM_FIELD_VALUE_TEMP");
        Cookie requestCookies[]=request.getCookies();
        
        String whereMapping = ""
            + " mp."+PstMappingProductLocationStoreRequest.fieldNames[PstMappingProductLocationStoreRequest.FLD_LOCATION_ID]+"='"+locationId+"'";
        
        Vector listItemMapping = PstMappingProductLocationStoreRequest.listJoinMaterial(0, 0, whereMapping, "");
        
        Hashtable<String,Integer> hashTemp=new Hashtable<String,Integer>();  
        
        //COOKIES DI TEMPATKAN PADA HASH TABLE
        for(Cookie c : requestCookies){
            String temp = c.getValue();
            String valueForHash="";
            boolean validCookie = false;
            try {
                String newTemp[] = temp.split("=/");
                String valueTemporary = newTemp[1];
                if (valueTemporary.equals("dimata")){
                    valueForHash = newTemp[0];
                    validCookie = true;
                }
            } catch (Exception e) {
                validCookie = false;
            }
            if (validCookie==true){
                hashTemp.put(""+c.getName()+"", Integer.parseInt(valueForHash));
            }
            
        }
        
        //MODIFY DATA HASH TABLE DENGAN DATA YANG DI VALUE TEMP
        if (valueTemp.length()>0){
            String temp [] = valueTemp.split(";");
            for (int a=0; a<temp.length;a++){
                String tempData = temp[a];
                String tempDataStr[] = tempData.split("=");
                String key = tempDataStr[0];
                int value = Integer.parseInt(tempDataStr[1]);
                
                //CHECK DULU APAKAH KEY SUDAH ADA PADA HASH TABLE ATAU BELUM
                boolean check = chekValueExist(hashTemp, key);
                if (check==true){
                    //jika sudah ada, maka modify value dengan value yang baru
                    hashTemp.put(""+key+"", value);
                }else{
                    //jika tidak ada, maka input value yang baru
                    hashTemp.put(""+key+"", value);
                }
                
            }
        }
        
        htmlReturn = ""
            + " <table class='table table-striped table-bordered'>"
                + "<thead>"
                    + "<tr>"
                        + "<td>"+textListOrderHeader[language][0]+"</td>"
                        + "<td>"+textListOrderHeader[language][2]+"</td>"
                        + "<td>"+textListOrderHeader[language][3]+"</td>"
                        + "<td>"+textListOrderHeader[language][4]+"</td>"
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                    for (int j = 0; j<listItemMapping.size();j++){
                        int no = j + 1;
                        String nameMaterial="";
                        MappingProductLocationStoreRequest mappingProductLocationStoreRequest = new MappingProductLocationStoreRequest();
                        mappingProductLocationStoreRequest = (MappingProductLocationStoreRequest)listItemMapping.get(j);
                        try {
                            String arrName [] = mappingProductLocationStoreRequest.getItemName().split(";");
                            nameMaterial = arrName[0];
                        } catch (Exception e) {
                            nameMaterial = mappingProductLocationStoreRequest.getItemName();
                        }
                        
                        int writeValue = 0;
                        //check
                        String keyToCheck = ""+locationId+"-"+mappingProductLocationStoreRequest.getMaterialId()+"";
                        if (chekValueExist(hashTemp, keyToCheck)==true){
                            writeValue= hashTemp.get(keyToCheck);
                        }else{
                            writeValue =0;
                        }
                        
                        htmlReturn += ""
                        + "<tr>"
                            + "<td>"+no+"</td>"
                            + "<td>"+nameMaterial+"</td>"
                            + "<td><input value='"+writeValue+"' data-key='"+locationId+"-"+mappingProductLocationStoreRequest.getMaterialId()+"' type='number' class='form-control itemqty' data-oidmaterial='"+mappingProductLocationStoreRequest.getMaterialId()+"' ></td>"
                            + "<td>"+mappingProductLocationStoreRequest.getUnitCode()+"</td>"
                        + "</tr>";
                    }
                htmlReturn += ""
                + "</tbody>"
            + " </table>";
        
        //HASH TABLE DIJADIKAN COOKIES KEMBALI
        Enumeration en=hashTemp.keys();
        while (en.hasMoreElements()) {
            //System.out.println(en.nextElement());
            String hashKey = (String)en.nextElement();
            int hashValue = hashTemp.get(hashKey);
            Cookie temp =new Cookie(""+hashKey+"",""+hashValue+"=/dimata");
            temp.setComment("dimataprivatearea");
            response.addCookie(temp);
        }
        
        hashTemp.clear();
        //KEMUDIAN HASH TABLE DI HAPUS
        
        return htmlReturn;
    }
    
    private boolean chekValueExistHash (Hashtable<Long,Double> hashTemp, long key){
        boolean returnValue = false;
        
        try {
            returnValue = hashTemp.containsKey(key);
        } catch (Exception e) {
            returnValue = false;
        }
        
        return returnValue;
    }
    
    private String showRecapitulation(HttpServletRequest request,HttpServletResponse response){
        String htmlReturn="";
        
        long oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_OID_LOCATION");
        long poId = FRMQueryString.requestLong(request, "FRM_FIELD_PO_ID");
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        Cookie requestCookies[]=request.getCookies();
        
        Hashtable<Long,Double> hashTemp=new Hashtable<Long,Double>(); 
        
        //COOKIES DI TEMPATKAN PADA HASH TABLE
        for(Cookie c : requestCookies){
            String temp = c.getValue();
            String valueForHash="";
            boolean validCookie = false;
            try {
                String newTemp[] = temp.split("=/");
                String valueTemporary = newTemp[1];
                if (valueTemporary.equals("dimata")){
                    valueForHash = newTemp[0];
                    validCookie = true;
                }
            } catch (Exception e) {
                validCookie = false;
            }
            if (validCookie==true){
                String cookieName = c.getName();
                //DIPECAH SEHINGGA DIDAPAT MATERIAL ID NYA 
                String tempCookie[] = cookieName.split("-");
                long materialId = Long.parseLong(tempCookie[1]);
                
                //CEK APAKAH DATA MATERIAL TERSEBUT SUDAH ADA DI HASH TABLE
                boolean isExist = chekValueExistHash(hashTemp,materialId);
                
                if (isExist==false){
                    //JIKA TIDAK ADA MAKA LANGSUNG DITAMBAHKAN KE HASH TABLE
                    hashTemp.put(materialId, Double.valueOf(valueForHash));
                }else{
                    //JIKA ADA MAKA DIAMBIL NILA HASH TABLE YANG SUDAH ADA
                    //DITAMBAHKAN DENGAN NILAI YANG BARU
                    double oldValue = hashTemp.get(materialId);
                    double addValue = Double.valueOf(valueForHash);
                    double newValue = oldValue+addValue;
                    hashTemp.put(materialId, newValue);
                }
  
            }
            
        }
        
        
        htmlReturn += ""
            + "<table class='table table-bordered table-striped'>"
                + "<thead>"
                    + "<tr>"
                        + "<td>"+textListOrderHeader[language][0]+"</td>"
                        + "<td>"+textListOrderHeader[language][2]+"</td>"
                        + "<td style='text-align:right'>"+textListOrderHeader[language][3]+"</td>"
                        + "<td>"+textListOrderHeader[language][5]+"</td>"
                        + "<td>"+textListOrderHeader[language][4]+"</td>"
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                //HASH TABLE DI BAWA KE BENTUK TABLE
                int num = 1;
                Enumeration en=hashTemp.keys();
                while (en.hasMoreElements()) {
                    long hashKey = (Long)en.nextElement();
                    double hashValue = hashTemp.get(hashKey);
                    Material entMaterial = new Material();
                    Unit entUnit = new Unit();
                    try {
                        entMaterial = PstMaterial.fetchExc(hashKey);
                    } catch (Exception e) {
                    }
                    
                    String nameMaterial="";
                    try {
                        String arrName [] = entMaterial.getName().split(";");
                        nameMaterial = arrName[0];
                    } catch (Exception e) {
                        nameMaterial = entMaterial.getName();
                    }
                    
                    try {
                        entUnit = PstUnit.fetchExc(entMaterial.getDefaultStockUnitId());
                    } catch (Exception e) {
                    }
                    
                    
                    
                    
                    htmlReturn += ""
                    + "<tr>"
                        + "<td>"+num+"</td>"
                        + "<td>"+nameMaterial+"</td>"
                        + "<td style='text-align:right;'>"+hashValue+"</td>"
                        + "<td><input type='number' class='form-control curentQty' data-unitid='"+entUnit.getOID()+"' data-qtyrequest='"+hashValue+"' data-oidmaterial='"+entMaterial.getOID()+"'></td>"
                        + "<td>"+entUnit.getCode()+"</td>"
                    + "</tr>";
                    num +=1;
                }
                htmlReturn += ""
                + "</tbody>"
            + "</table>"
            + "<br>"
            + "<button style='margin-left:5px;' type='button' id='btnSaveAllItem' class='btn btn-primary pull-right'>"+textListButton[language][0]+"</button>"
            + "<button  type='button' id='btnBack' class='btn btn-danger pull-right'>"+textListButton[language][4]+"</button>";
            
        
        return htmlReturn;
    }
    
    private String saveAllItem(HttpServletRequest request,HttpServletResponse response){
        String result="";
        
        long oidLocation = FRMQueryString.requestLong(request, "FRM_FIELD_OID_LOCATION");
        long poId = FRMQueryString.requestLong(request, "FRM_FIELD_PO_ID");
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        long currencyId = FRMQueryString.requestLong(request, "FRM_FIELD_ID_CURRENCY");
        String currentValue = FRMQueryString.requestString(request, "FRM_FIELD_VALUE_CURRENT");
        long userOid = FRMQueryString.requestLong(request, "FRM_FIELD_USER_ID");
        String userName = FRMQueryString.requestString(request, "FRM_FIELD_USER_NAME");
        
        Cookie requestCookies[]=request.getCookies();
        
        Hashtable<Long,PurchaseOrderItem> hashTemp=new Hashtable<Long,PurchaseOrderItem>(); 
        
        //CURRENT QTY DIMASUKKAN KE HASH TABLE
        String explode [] = currentValue.split(";");
        for (int i = 0; i<explode.length;i++){
            PurchaseOrderItem entPurchaseOrderItem = new PurchaseOrderItem();
            String explodeSec[] = explode[i].split("-");
            entPurchaseOrderItem.setPurchaseOrderId(poId);
            entPurchaseOrderItem.setMaterialId(Long.parseLong(explodeSec[0]));
            entPurchaseOrderItem.setUnitId(Long.parseLong(explodeSec[3]));
            entPurchaseOrderItem.setCurrencyId(currencyId);
            entPurchaseOrderItem.setQuantity(Double.valueOf(explodeSec[1]));
            entPurchaseOrderItem.setUnitRequestId(Long.parseLong(explodeSec[3]));
            entPurchaseOrderItem.setQtyRequest(Double.valueOf(explodeSec[1]));
            entPurchaseOrderItem.setQtyInputStock(Double.valueOf(explodeSec[2]));
            
            hashTemp.put(Long.parseLong(explodeSec[0]),entPurchaseOrderItem);
            
            
        }
        
        //PROSES SIMPAN KE PO ITEM
        if (hashTemp.size()>0){
            CtrlPurchaseOrderItem ctrlPurchaseOrderItem = new CtrlPurchaseOrderItem(request);
            int iError = ctrlPurchaseOrderItem.actionUsingHash(Command.SAVE, poId, hashTemp, userName, userOid);
            
            result = String.valueOf(iError);
            
            if (iError==0){
                //HAPUS COOKIES
                for(Cookie c : requestCookies){
                    String temp = c.getValue();
                    String name = c.getName();
                    String valueForHash="";
                    boolean validCookie = false;
                    try {
                        String newTemp[] = temp.split("=/");
                        String valueTemporary = newTemp[1];
                        if (valueTemporary.equals("dimata")){
                            valueForHash = newTemp[0];
                            validCookie = true;
                        }
                    } catch (Exception e) {
                        validCookie = false;
                    }
                    if (validCookie==true){
                        Cookie tempCookie =new Cookie(""+name+"",null);
                        tempCookie.setMaxAge(0);
                        response.addCookie(tempCookie);
                    }

                }
            }
            
        }

        return result;
    }
    
    private String showAllItemOrder(HttpServletRequest request){
        String htmlReturn = "";
        
        long oidBillMain = FRMQueryString.requestLong(request, "FRM_FIELD_BILL_MAIN_ID");
        int language = FRMQueryString.requestInt(request, "FRM_FIELD_LANGUAGE");
        
        String whereClauseX = ""
            + " "+ PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
        Vector lisBillDetailIssue = PstBillDetail.listMat(0, 0,whereClauseX, "");
                      
        htmlReturn += ""
        
        + "<div class='col-xs-6'></div>"
        + "<div class='col-xs-6'>"
            + "<div class='input-group input-group-sm'>"
                + "<input class='form-control' type='text' id='findBarcode'>"
                + "<span class='input-group-btn'>"
                    + "<button id='findBarcodeButton' class='btn btn-info btn-flat' type='button'>"+textListButton[language][5]+"</button>"
                + "</span>"
            + "</div>"
        + "</div>"
        + "<div class='col-xs-12'>" 
            + "<table style='margin-top:10px;' class='table table-bordered table-striped' >"
                + "<thead>"
                    + "<tr>"
                        + "<th>"+textListOrderHeader[language][0]+"</th>"
                        + "<th>"+textListOrderHeader[language][6]+"</th>"
                        + "<th>"+textListOrderHeader[language][2]+"</th>"
                        + "<th>"+textListOrderHeader[language][7]+"</th>"
                        + "<th>"+textListOrderHeader[language][4]+"</th>"
                        + "<th>"+textListOrderHeader[language][8]+"</th>"
                    + "</tr>"
                + "</thead>"
                + "<tbody>";

                if (lisBillDetailIssue.size()>0){
                    for (int i = 0; i<lisBillDetailIssue.size();i++){
                        int num = i +1;
                        Vector vBill = (Vector) lisBillDetailIssue.get(i); 
                        Billdetail billdetail =(Billdetail)vBill.get(0); 
                        Unit unit = new Unit();
                        try {
                            unit = PstUnit.fetchExc(billdetail.getUnitId());     
                        }catch(Exception e) {
                            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
                        }
                        Material material= new Material();
                        try {
                            material = PstMaterial.fetchExc(billdetail.getMaterialId());     
                        } catch(Exception e) {
                            System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
                        }

                        String nameMaterial="";
                        try {
                            String arrName [] = material.getName().split(";");
                            nameMaterial = arrName[0];
                        } catch (Exception e) {
                            nameMaterial = material.getName();
                        }
                        
                        String fokus="";
                        
                        if (i==0){
                            fokus = "first";
                        }else if (i==(lisBillDetailIssue.size()-1)){
                            fokus = "last";
                        }

                        htmlReturn+= ""
                        + "<tr>"
                            + "<td>"+num+"</td>"
                            + "<td>"+material.getSku()+"</td>"
                            + "<td>"+nameMaterial+"</td>"
                            + "<td style='text-align:right'>"+billdetail.getQtyRequestSo()+"</td>"
                            + "<td>"+unit.getCode()+"</td>"
                            + "<td><input min='0' id='qty-"+num+"' type='number' data-qtyrequest='"+billdetail.getQtyRequestSo()+"' class='form-control "+fokus+" qtysend "+material.getBarCode()+"' data-barcode='"+material.getBarCode()+"' data-material='"+material.getOID()+"' data-oid='"+billdetail.getOID()+"'></td>"
                        + "</tr>";
                    }
                }

                htmlReturn+= ""
                + "</tbody>"
            + "</table>"
        + "</div>";
        
        if (lisBillDetailIssue.size()>0){
            htmlReturn +=""
                + "<button type='button' id='printOrder' class='btn btn-danger pull-right'>Invoice</button>"
                + "<button style='margin-right:10px;' type='button' id='saveOrder' class='btn btn-primary pull-right'>"+textListButton[language][0]+"</button>"
                
                + "";
        }
            
            
        
        return htmlReturn;
    }
    
    private String saveIssue(HttpServletRequest request){
        String resultHtml = "";
        
        long cashCashier = FRMQueryString.requestLong(request, "FRM_FIELD_CASH_CASHIER_ID");
        long oidBillMain = FRMQueryString.requestLong(request, "FRM_FIELD_BILL_MAIN_ID");
        String values = FRMQueryString.requestString(request, "FRM_FIELD_VALUES");
        
        int iError = 0;
        //UPDATE BILL DETAIL
        
        if (values.length()>0){
            String temp [] = values.split(";");
            
            for (int i = 0; i<temp.length;i++){
                String tempValue []= temp[i].split(":");
                long oidBillDetail = Long.parseLong(tempValue[0]);
                double qty = Double.valueOf(tempValue[1]);
                
                Billdetail entBillDetail = new Billdetail();
                try {
                    entBillDetail = PstBillDetail.fetchExc(oidBillDetail);
                } catch (Exception e) {
                }
                entBillDetail.setQty(qty);
                if (entBillDetail.getOID()!=0){
                    CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
                    if (iError==0){
                        ctrlBillDetail.action(Command.SAVE, entBillDetail);
                    }
                }
   
            }
            if (iError==0){
                
                //SET BILL MAIN 
                
                BillMain entBillMain = new BillMain();
                try {
                    entBillMain = PstBillMain.fetchExc(oidBillMain);
                } catch (Exception e) {
                }

                entBillMain.setCashCashierId(cashCashier);
                entBillMain.setDiscType(3);
                String oldInvoiceNumber =entBillMain.getInvoiceNo();
                oldInvoiceNumber = oldInvoiceNumber.replace("X", "");
                entBillMain.setInvoiceNo(oldInvoiceNumber);
                entBillMain.setInvoiceNumber(oldInvoiceNumber);
                entBillMain.setTransctionType(1);
                entBillMain.setStatusInv(0);
                entBillMain.setDateTermOfPayment(new Date());
                
                CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
                
                iError = ctrlBillMain.action(Command.SAVE, entBillMain);
                
            }
            
            
        }
        
        resultHtml = String.valueOf(iError);

        return resultHtml;
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
