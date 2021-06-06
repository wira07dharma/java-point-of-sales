package com.dimata.posbo.ajax;

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactClass;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.common.entity.logger.LogSysHistory;
import com.dimata.common.entity.logger.PstLogSysHistory;
import com.dimata.gui.jsp.ControlCombo;
import com.dimata.harisma.entity.masterdata.PstMarital;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMaterial;
import com.dimata.posbo.entity.masterdata.PstUnit;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.warehouse.MatStockOpname;
import com.dimata.posbo.entity.warehouse.MatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstMatStockOpname;
import com.dimata.posbo.entity.warehouse.PstMatStockOpnameItem;
import com.dimata.posbo.entity.warehouse.PstSourceStockCode;
import com.dimata.posbo.form.warehouse.CtrlMatStockOpname;
import com.dimata.posbo.form.warehouse.CtrlMatStockOpnameItem;
import com.dimata.posbo.form.warehouse.FrmMatStockOpname;
import com.dimata.posbo.form.warehouse.FrmMatStockOpnameItem;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class AjaxOpname 
extends HttpServlet{
    
    public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname","Cetak Opname","Material dengan barcode yang sama atau mirip lebih dari 1","Verifikasi","Verifikasi berhasil","Tutup","Simpan"},
	{"Stock","Opname","Search","List","Edit","No opname data available","Print Opname","Material with same barcode more than 1","Verification", "Verifivation success","Close","Save"}
    };
    
    public static final String textListOrderItem[][] = {
	{"No","Sku","Nama Barang","Unit","Kategori","Sub Kategori","Qty Opname","Aksi","Hapus"},//7
	{"No","Code","Name","Unit","Category","Sub Category","Qty Opname","Action","Delete"}
    };
    
    public static final String textListOrderHeader[][] = {
        {"Nomor","Lokasi","Tanggal","Waktu","Status","Supplier","Kategori","Sub Kategori","Keterangan","Semua"},
        {"Number","Location","Date","Jam","Status","Supplier","Category","Sub Category","Remark","All"}
    };
    
    public static final String textListOrderHeader2[][] = {
        {"Nama Login","Aksi","Tanggal","Aplikasi","Detil"},
        {"Login Name","User Action","Date","Application","Detail"}
    };
    
    public static final String textListMaterialHeader[][] ={
	{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
	{"No","Category","Code","Item","Unit","Cost"}
    };
    
    public static final String textListState[][] ={
	{"Tunggu"},
	{"Wait"}
    };
    private String docTypeClassName;
    
    private String listProccess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
        
        if (loadType.equals("getListOpname")){
            result = getListOpname(request);
        }else if (loadType.equals("getListItemModal")){
            result = getListItemModal(request);
        }else if (loadType.equals("editItem")){
            result = getEditItem(request);
        }else if (loadType.equals("getListControlSuplier")){
            result = getListControlSuplier(request);
        }else if (loadType.equals("getListControlCategory")){
            result = getListControlCategory(request);
        }else if (loadType.equals("getListControlStatus")){
            result = getListControlStatus(request);
        }else if (loadType.equals("getDeleteButton")){
            result = getDeleteButton(request);
        }else if (loadType.equals("getSaveButton")){
            result = getSaveButton(request);
        }else if (loadType.equals("getMatItemBySKU")){
            result = getMatItemBySKU(request);
        }else if (loadType.equals("getListHistory")){
            result = getHistoryOpname(request);
        }else if (loadType.equals("getPrintButton")){
            result = getPrintButton(request);
        }
        
        return result;
    }
    
    private String saveProcess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
               
        if (loadType.equals("saveMasterOpname")){
           result = saveMasterOpname(request); 
        }else if (loadType.equals("saveItemList")){
           result = saveItemList(request);
        }else if (loadType.equals("saveEditItem")){
           result = saveEditItem(request);
        }
        return result;
    }
    
    private String deleteProcess(HttpServletRequest request){
        String result ="";
        String loadType = FRMQueryString.requestString(request, "loadType");
        
        if (loadType.equals("deleteItemList")){
            result = deleteItemList(request);
        }else if (loadType.equals("deleteOpname")){
            result = deleteOpname(request);
        }
        
        return result;
    }
    
    private String getProcess(HttpServletRequest request){
        String result ="";
        String loadType= FRMQueryString.requestString(request, "loadType");
        
        if (loadType.equals("getCodeOpname")){
            result = getCodeOpname(request);
        }
        
        return result;
    }
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.LIST :
                result = listProccess(request);
                response.getWriter().write(result);
                break;
            case Command.GET:
                result = getCodeOpname(request);
                response.getWriter().write(result);
                break;
            case Command.SAVE:
                result = saveProcess(request);
                response.getWriter().write(result);
                break;
            case Command.DELETE:
                result = deleteProcess(request);
                response.getWriter().write(result);
                break;            
            default:

        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
        
        switch (command) {
            case Command.SEARCH :
                JSONArray resultJson = new JSONArray();
                resultJson = getListItemAutoName(request);
                response.setContentType("application/json");
                response.setHeader("Cache-Control", "no-store");
                PrintWriter out = response.getWriter();
                out.print(resultJson);
            break;
            default:     
        }
    }
    
    private String getListOpname(HttpServletRequest request){
        String result="";
        MatStockOpname entMatStockOpname= new MatStockOpname();
        long oIdOpname = FRMQueryString.requestLong(request, "idOpname");
        
        try {
            entMatStockOpname = PstMatStockOpname.fetchExc(oIdOpname);
        } catch (Exception e) {
        }
        
        int language = FRMQueryString.requestInt(request, "lang");
        int editableList = FRMQueryString.requestInt(request, "editableList");
        int i = 0;
        int no = 0;
        
        FrmMatStockOpnameItem frmObject = new FrmMatStockOpnameItem();
        
        if (oIdOpname==0){
            result +=""
            + "<table style='width:100%;'>"
                + "<tr>"
                    + "<td style='background-color: beige;padding: 6px;text-align: center;font-weight: bold;'>"
                        + "<b>"+textListGlobal[language][5]+"</b>"
                    + "</td>"
                + "</tr>"
            + "</table>";
        }else{          
            Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(0,0,oIdOpname); 
            result += ""
            + "<table class='table table-hover table-striped table-bordered' width='100%'>"
                + "<thead style='font-size:13px;'>"
                    + "<tr>"
                        + "<th style='width:5%'>"+textListOrderItem[language][0]+"</th>"  
                        + "<th style='width:20%'>"+textListOrderItem[language][1]+"</th>" 
                        + "<th style='width:30%'>"+textListOrderItem[language][2]+"</th>" 
                        + "<th style='width:10%'>"+textListOrderItem[language][3]+"</th>" 
                        + "<th style='width:10%'>"+textListOrderItem[language][6]+"</th>" 
                        + "<th style='width:10%'>"+textListOrderItem[language][7]+"</th>"
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                    if (listMatStockOpnameItem.size()<=0){
                        result += "<tr>";
                        result += "<td colspan='6'><center><b>"+textListGlobal[language][5]+"</b></center></td>";
                        result += "</tr>";
                    }else{
                        for(i=0;i<listMatStockOpnameItem.size();i++){
                            Vector tempData = (Vector)listMatStockOpnameItem.get(i);
                            MatStockOpnameItem matStockOpnameItem = (MatStockOpnameItem)tempData.get(0);
                            Material mat = (Material) tempData.get(1);
                            Unit unit = (Unit) tempData.get(2);
                            String addClass="";
                            if (i==0){
                                addClass="firstFocus";
                            }else if(i==(listMatStockOpnameItem.size()-1)){
                                addClass="lastFocus";
                            }
                            no = i + 1;
                            result += ""
                            + "<tr class='listOpname' id='listOpname-"+i+"'>"
                                + "<td>"+no+"</td>"
                                + "<td><input type='text' value='"+mat.getSku()+"' class='form-control table-control "+addClass+"' id='input-"+i+"' readonly='readonly'></td>"
                                + "<td>"+mat.getName()+"</td>"
                                + "<td>"+unit.getCode()+"</td>"
                                + "<td style='text-align:right;'>"+matStockOpnameItem.getQtyOpname()+"</td>"
                                + "<td>";
                                if (oIdOpname==0 || entMatStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT ){
                                    result += "<button id='"+i+"' class='btn btn-warning editItem' type='button' style='height: 34px;'";
                                    result += " data-oidopname='"+oIdOpname+"' data-matoidinput='"+mat.getOID()+"'";
                                    result += " data-unitoidinput='"+matStockOpnameItem.getUnitId()+"' data-skuinput='"+mat.getSku()+"' ";
                                    result += " data-materialinput='"+mat.getName()+"' data-unitinput='"+unit.getCode()+"'";
                                    result += " data-qtyinput='"+matStockOpnameItem.getQtyOpname()+"'";
                                    result += " data-oiditemopname='"+matStockOpnameItem.getOID()+"'>";
                                    result += "<i class='glyphicon glyphicon-pencil'></i> [e]";
                                    result += "</button>";
                                    result += "&nbsp;";
                                    result += "<button id='del-"+i+"' data-oidopnameitem='"+matStockOpnameItem.getOID()+"' class='btn btn-danger deleteItem' type='button' style='height: 34px;'>"
                                        + "<i class='glyphicon glyphicon-remove'></i> [d]"
                                    + "</button>";
                                }
                                result +=""
                                + "</td>"
                            + "</tr>";
                            
                        }
                    }
                    no = i + 1;
            if (oIdOpname==0 || entMatStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT ){       
            result +=""                  
                    + "<tr id='addForm'>"                      
                        + "<td>"+no+"</td>"
                        + "<td>"
                            + "<div class='input-group' >"
                                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_STOCK_OPNAME_ID]+"' value='"+oIdOpname+"' type='hidden'>"
                                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"' id='matOidInput' type='hidden'>"
                                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"' id='unitOidInput' type='hidden'>"
                                + "<input autocomplete='off'  name='skuInput' id='skuInput' type='text' class='form-control required add' placeholder='SKU....(F2)'>"
                                + "<ul id='myTypeHead' class='dropdown-menu'></ul>"
                                + "<span class='input-group-btn'>"
                                    + "<button class='btn btn-default loadListItem' type='button' style='height: 34px;'>"
                                        + "<i class='glyphicon glyphicon-search'></i>"
                                    + "</button>"
                                + "</span>"
                            + "</div>"
                        + "</td>"
                        + "<td>"  
                            + "<input readonly='readonly' name='materialInput' id='materialInput' type='text' class='form-control required add'>"                       
                        + "</td>"
                        + "<td><input name='unitInput' id='unitInput' readonly='readonly' type='text' class='form-control'></td>"
                        + "<td><input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME]+"' id='qtyInput' type='number' class='form-control add'></td>"
                        + "<td>"
                            + "<button class='btn btn-primary saveItem' type='button' style='height: 34px;'>"
                                + "<i class='glyphicon glyphicon-floppy-disk'></i> "
                                + ""+textListGlobal[language][11]+""
                            + "</button>"
                        + "</td>"
                    + "</tr>" ;
            }
            result  += "</tbody>"
            + "</table>";

           
        }
        
        return result;
    }
    
    private String saveMasterOpname(HttpServletRequest request){
        String result="";
        int iErrCode = FRMMessage.ERR_NONE;
        long oidOpnameNew=0;
        
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        int command = FRMQueryString.requestInt(request, "command");
        long oIdOpname = FRMQueryString.requestLong(request, "hidden_opname_id");
        
        CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
        iErrCode = ctrlMatStockOpname.action(command , oIdOpname, userName, userId);
        
        if (iErrCode==0){
            //MatStockOpname so = ctrlMatStockOpname.getMatStockOpname();
            oidOpnameNew=ctrlMatStockOpname.getNewOid();
        }
                
        result += String.valueOf(oidOpnameNew);
        
        return result;
    }
    
    private String getCodeOpname(HttpServletRequest request){
        String result ="";
        long oidOpname = FRMQueryString.requestLong(request, "oidOpname");
        
        MatStockOpname so = new MatStockOpname();
        try {
            so = PstMatStockOpname.fetchExc(oidOpname);
        } catch (Exception e) {
        }
        result = so.getStockOpnameNumber();
        return result;
    }
    
    private String getListItemModal(HttpServletRequest request){
        String result ="";
        MatStockOpname matStockOpname;
        Vector  vect = new Vector();
        
        // REQUEST DATA
        long oidOpname = FRMQueryString.requestLong(request, "oidOpname");
        String sku = FRMQueryString.requestString(request, "sku");
        String namaBarang = FRMQueryString.requestString(request, "namaBarang");
        int language = FRMQueryString.requestInt(request, "lang");
        int searchType = FRMQueryString.requestInt(request, "searchType");
        
        try {
           matStockOpname = PstMatStockOpname.fetchExc(oidOpname); 
        } catch (Exception e) {
            matStockOpname = new MatStockOpname();
        }
        
        long materialGroup = matStockOpname.getCategoryId();
        long materialSubCategory = matStockOpname.getSubCategoryId();
        long materialSupplier = matStockOpname.getSupplierId();
        long oidLocation = matStockOpname.getLocationId();
        
        String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
        Material objMaterial = new Material();
        objMaterial.setCategoryId(materialGroup);
        objMaterial.setSubCategoryId(materialSubCategory);
        objMaterial.setSku(sku);
        objMaterial.setName(namaBarang);
        
        if(searchType==1){   
            vect = PstMaterial.getListMaterialOpnameAll(materialSupplier, objMaterial, 0, 15, orderBy, oidLocation, oidOpname);
        }else if(searchType==2){    
            vect = PstMaterial.getListMaterialOpnameAllNew(materialSupplier, objMaterial, 0, 15, orderBy, oidLocation, oidOpname);    
        } else {
            vect = PstMaterial.getListMaterialOpname(materialSupplier, objMaterial, 0, 15, orderBy, oidLocation, oidOpname);
        }
        
        result +=""
        + "<table class='table table-hover table-striped table-bordered' width='100%'>"
            + "<thead>"
                + "<tr>"
                    + "<td style='width:5%'>"+textListMaterialHeader[language][0]+"</td>"  
                    + "<td style='width:20%'>"+textListMaterialHeader[language][2]+"</td>" 
                    + "<td style='width:30%'>"+textListMaterialHeader[language][3]+"</td>" 
                    + "<td style='width:10%'>"+textListMaterialHeader[language][4]+"</td>"                       
                + "</tr>"
            + "</thead>"
            + "<tbody>";
        if (vect.size()>0){
            for (int i = 0 ; i<vect.size();i++){
                Material material = (Material)vect.get(i);
                int no = 0;
                no= i +1;
                String unitName = "";
                String unitCode = "";
                String addClass="";
                
                if(i==0){
                    addClass="firstFocus";
                }else if (i==vect.size()){
                    addClass="lastFocus";
                }
                
                try{
                    Unit unit = PstUnit.fetchExc(material.getDefaultStockUnitId());				
                    unitName = unit.getName();
                    unitCode = unit.getCode();
                }catch(Exception e){}
                
                result +=""
                + "<tr style='cursor:pointer;'  data-oidmaterial='"+material.getOID()+"' data-materialname='"+material.getName()+"' data-sku='"+material.getSku()+"' data-unitid='"+material.getDefaultStockUnitId()+"' data-unit='"+unitName+"'>"
                    + "<td>"+no+"</td>"
                    + "<td><input id='textual-"+i+"' type='text' data-oidmaterial='"+material.getOID()+"' data-materialname='"+material.getName()+"' data-sku='"+material.getSku()+"' data-unitid='"+material.getDefaultStockUnitId()+"' data-unit='"+unitName+"' class='form-control selectMaterial textual "+addClass+"' readonly='readonly' value='"+material.getSku()+"' style='cursor:pointer' class='selectMaterial'></td>"
                    + "<td>"+material.getName()+"</td>"
                    + "<td>"+unitName+"</td>"
                + "</tr>";
            }  
        }else{
            result +=""
            + "<tr>"
                + "<td colspan='4'><center>No Data</center></td>"
            + "</tr>";
        }
        result +=""
            + "</tbody>"
        + "</table>";
        return result;
    }
    
    private String saveItemList(HttpServletRequest request){
        String result ="";
        
        long oidOpname = FRMQueryString.requestLong(request, "oidOpname");
        int lang = FRMQueryString.requestInt(request, "lang");
        int iErrCode = 0;
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        
        CtrlMatStockOpnameItem ctrlMatStockOpnameItem = new CtrlMatStockOpnameItem(request);
        ctrlMatStockOpnameItem.setLanguage(lang);
        iErrCode = ctrlMatStockOpnameItem.action(Command.SAVE,0,oidOpname,userName,userId);
        
        if (iErrCode==0){
            result += "0";
        }else{
            result +="1";
        }
        
        return result;
    }
    
    private String deleteItemList(HttpServletRequest request){
        String result ="";
        int iErrCode = 0;
        long oidOpnameItem = FRMQueryString.requestLong(request, "oidOpnameItem");
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        
        CtrlMatStockOpnameItem ctrlMatStockOpnameItem = new CtrlMatStockOpnameItem(request);
        iErrCode = ctrlMatStockOpnameItem.action(Command.DELETE,oidOpnameItem,0,userName,userId);
        
        if (iErrCode==0){
            result += "0";
        }else{
            result +="1";
        }
        
        return result;
    }
    
    private String getEditItem(HttpServletRequest request){
        String result ="";
        FrmMatStockOpnameItem frmObject = new FrmMatStockOpnameItem();        
        long oIdOpname =FRMQueryString.requestLong(request, "oIdOpname");
        long matOidInput =FRMQueryString.requestLong(request, "matOidInput");
        long unitOidInput =FRMQueryString.requestLong(request, "unitOidInput");
        long oidItemOpname =FRMQueryString.requestLong(request, "oidItemOpname");
        String qtyInput=FRMQueryString.requestString(request, "qtyInput");
        String unitInput = FRMQueryString.requestString(request, "unitInput");
        String skuInput = FRMQueryString.requestString(request, "skuInput");
        String materialInput = FRMQueryString.requestString(request, "materialInput");
        int i = FRMQueryString.requestInt(request, "i");
        int language = FRMQueryString.requestInt(request, "lang");
        int no = i+1;
        result += ""
        + "<td>"+no+"</td>"
        + "<td>"
            + "<div class='input-group' >"
                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_STOCK_OPNAME_ITEM_ID]+"' value='"+oidItemOpname+"' type='hidden'>"
                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_STOCK_OPNAME_ID]+"' value='"+oIdOpname+"' type='hidden'>"
                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"' value='"+matOidInput+"' id='matOidInput' type='hidden'>"
                + "<input name='"+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"' value='"+unitOidInput+"' id='unitOidInput' type='hidden'>"
                + "<input value='"+skuInput+"' name='skuInput' id='skuInput' type='text' class='form-control required' placeholder='SKU....(F2)'>"
                + "<span class='input-group-btn'>"
                    + "<button class='btn btn-default loadListItem' type='button' style='height: 34px;'>"
                        + "<i class='glyphicon glyphicon-search'></i>"
                    + "</button>"
                + "</span>"
            + "</div>"
        + "</td>"
        + "<td>"
            + "<div class='input-group'>"
                + "<input value='"+materialInput+"' name='materialInput' id='materialInput' type='text' class='form-control required' placeholder='"+textListOrderItem[language][2]+"....(F2'>"
                + "<span class='input-group-btn'>"
                    + "<button class='btn btn-default loadListItem' type='button' style='height: 34px;'>"
                        + "<i class='glyphicon glyphicon-search'></i>"
                    + "</button>"
                + "</span>"
            + "</div>"
        + "</td>"
        + "<td><input value='"+unitInput+"' name='unitInput' id='unitInput' readonly='readonly' type='text' class='form-control'></td>"
        + "<td><input value='"+qtyInput+"' name='"+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_OPNAME]+"' id='qtyInput' type='number' class='form-control'></td>"
        + "<td>"
            + "<button class='btn btn-primary saveEditItem' type='button' style='height: 34px;'>"
                + "<i class='glyphicon glyphicon-floppy-disk'></i> "
                + ""+textListGlobal[language][11]+""
            + "</button>"
        + "</td>";        
        return result;
    }
    
    private String saveEditItem(HttpServletRequest request){
        String result="";
        FrmMatStockOpnameItem frmObject = new FrmMatStockOpnameItem();

        long oidOpname = FRMQueryString.requestLong(request, ""+frmObject.fieldNames[frmObject.FRM_FIELD_STOCK_OPNAME_ID]+"");
        long oidItemOpname = FRMQueryString.requestLong(request, ""+frmObject.fieldNames[frmObject.FRM_FIELD_STOCK_OPNAME_ITEM_ID]+"");
        int lang = FRMQueryString.requestInt(request, "lang");
        int iErrCode = 0;
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
        
        CtrlMatStockOpnameItem ctrlMatStockOpnameItem = new CtrlMatStockOpnameItem(request);
        ctrlMatStockOpnameItem.setLanguage(lang);
        iErrCode = ctrlMatStockOpnameItem.action(Command.SAVE,oidItemOpname,oidOpname,userName,userId);
        
        if (iErrCode==0){
            result += "0";
        }else{
            result +="1";
        }
        
        return result;
        
    }
    
    private String getListControlSuplier(HttpServletRequest request){
        String result ="";
        long oidStockOpname =0;
        
        oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
        MatStockOpname matStockOpname = new MatStockOpname();
        
        try {
            matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
        } catch (Exception e) {
        }
        
        int lang = FRMQueryString.requestInt(request, "lang");
        if(vectSizeItem!=0) {
            try {
                if(matStockOpname.getSupplierId()!=0) {
                    ContactList contactList = PstContactList.fetchExc(matStockOpname.getSupplierId());
                    result += "<input type='text' class='form-control' readonly='readonly' value='"+contactList.getCompName()+"'>";
                }else{
                    result += "<input type='text' class='form-control' readonly='readonly' value='"+textListOrderHeader[lang][9]+" "+textListOrderHeader[lang][5]+"'>";

                }
            }catch(Exception e){
            }
            result +="<input type='hidden' name='"+FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID]+"' value='"+matStockOpname.getSupplierId()+"'>";
        }else{
            Vector val_supplier = new Vector(1,1);
            Vector key_supplier = new Vector(1,1);

            String wh_supp = ""
                + " "+PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+""
                + " ="+PstContactClass.CONTACT_TYPE_SUPPLIER+""
                + " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS] + ""
                + " != "+PstContactList.DELETE;
            String ordBySupp =  " CONT." + PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+"";
            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,ordBySupp);

            key_supplier.add(textListOrderHeader[lang][9]+" "+textListOrderHeader[lang][5]);
            val_supplier.add("0");
            for(int d=0; d<vt_supp.size(); d++)
            {
                ContactList cnt = (ContactList)vt_supp.get(d);
                String cntName = cnt.getCompName();
                if(cntName.length()==0)
                {
                    cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                }
                val_supplier.add(String.valueOf(cnt.getOID()));
                key_supplier.add(cntName);
            }
            String select_supplier = ""+matStockOpname.getSupplierId();            
            result += ""+ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","form-control");
        }
        return result;
    }
    
    private String getListControlCategory(HttpServletRequest request){
        String result ="";
        long oidStockOpname =0;
        
        oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
        MatStockOpname matStockOpname = new MatStockOpname();
        
        try {
            matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
        } catch (Exception e) {
        }
        
        if(vectSizeItem!=0){
            try{
                if(matStockOpname.getCategoryId()!=0 && matStockOpname.getCategoryId()!=-1){
                    System.out.println("===>>>>>>>>> proses xxxxx ");
                    Category category = PstCategory.fetchExc(matStockOpname.getCategoryId());
                    result += "<input type='text' class='form-control' readonly='readonly' value='"+category.getName()+"'>";
                }else{
                    result += "<input type='text' class='form-control' readonly='readonly' value='"+textListOrderHeader[lang][9]+" "+textListOrderHeader[lang][6]+"'>";
                }
            }catch(Exception e){}
            result += "<input type='hidden' name='"+FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]+"' value='"+matStockOpname.getCategoryId()+"'>";
        }else{
            result += "<select id='"+FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]+"'  name='"+FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_CATEGORY_ID]+"' class='form-control'>";
            result += "<option value='-1'>Semua Category</option>";
                               
            Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
            Vector materGroup = PstCategory.structureList(masterCatAcak) ;
            Vector vectGroupVal = new Vector(1,1);
            Vector vectGroupKey = new Vector(1,1);
            if(materGroup!=null && materGroup.size()>0) {
                String parent="";                      
                Vector<Long> levelParent = new Vector<Long>();
                for(int i=0; i<materGroup.size(); i++) {
                    Category mGroup = (Category)materGroup.get(i);
                    String select="";
                    if(mGroup.getOID()==matStockOpname.getCategoryId()){
                        select="selected";
                    }
                    if(mGroup.getCatParentId()!=0){
                        for(int lv=levelParent.size()-1; lv > -1; lv--){
                            long oidLevel=levelParent.get(lv);
                            if(oidLevel==mGroup.getCatParentId()){
                                break;
                            }else{
                                levelParent.remove(lv);
                            }
                        }
                        parent="";
                        for(int lv=0; lv<levelParent.size(); lv++){
                           parent=parent+"&nbsp;&nbsp;";
                        }
                        levelParent.add(mGroup.getOID());

                    }else{
                        levelParent.removeAllElements();
                        levelParent.add(mGroup.getOID());
                        parent="";
                    }
                                           
                    result += "<option value='"+mGroup.getOID()+"' "+select+">"+parent+""+mGroup.getName()+"</option>";                           
                }
            }else{
                vectGroupVal.add("Tidak Ada Category");
                vectGroupKey.add("-1");
            }
                                            
            result +="</select>";
        }
        
                                
        return result;
    }
    
    private String getListControlStatus(HttpServletRequest request){
        String result="";
        long oidStockOpname =0;
        
        oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int vectSizeItem = PstMatStockOpnameItem.getCount(PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID] + " = " + oidStockOpname);
        MatStockOpname matStockOpname = new MatStockOpname();
        
        try {
            matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
        } catch (Exception e) {
        }
        
        int lang = FRMQueryString.requestInt(request, "lang");
        
        Vector obj_status = new Vector(1,1);
        Vector val_status = new Vector(1,1);
        Vector key_status = new Vector(1,1);

        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);						  						  
        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
        
        Vector listMatStockOpnameItem = PstMatStockOpnameItem.list(0,0,oidStockOpname);
        Vector listError = getListError(listMatStockOpnameItem);
        
        if(vectSizeItem!=0 && listError.size()==0){
            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
        }
        
        String select_status = ""+matStockOpname.getStockOpnameStatus();
        if(matStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
            result += "<input type='text' readonly class='form-control' value='"+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]+"'>";
        }else if(matStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
            result += "<input type='text' readonly class='form-control' value='"+I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]+"'>";
        }else{
            result += "" +ControlCombo.draw(FrmMatStockOpname.fieldNames[FrmMatStockOpname.FRM_FIELD_STOCK_OPNAME_STATUS],null,select_status,val_status,key_status,"tabindex=\"4\"","form-control");
        }
        
        return result;
    }
    
    private Vector getListError(Vector objectClass){
        Vector listError = new Vector(1,1);
        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            MatStockOpnameItem soItem = (MatStockOpnameItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstSourceStockCode.fieldNames[PstSourceStockCode.FLD_SOURCE_ID]+"="+soItem.getOID();
                int cnt = PstSourceStockCode.getCount(where);
                if(cnt<soItem.getQtyOpname()){
                    if(listError.size()==0){
                        listError.add("Pesan Kesalahan: ");
                    }
                    listError.add(""+listError.size()+". Jumlah Serial kode barang '"+mat.getName()+"' tidak sama dengan jumlah qty opname");
                }                
             }
        }
        return listError;
    }
    
    private String getDeleteButton(HttpServletRequest request){
        String result ="";
        MatStockOpname matStockOpname = new MatStockOpname();
        
        Boolean privDelete = FRMQueryString.requestBoolean(request, "privDelete");
        Long oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        try {
            matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
        } catch (Exception e) {
        }
        
        if(matStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED || privDelete==false){
            result += "";
        }else{
            if (oidStockOpname!=0){
                result += ""
                + "<button style='width:49%' class='btn btn-danger pull-right' style='padding: 5px; width: 75px;' type='button' id='deleteOpname'>"
                    + ""+textListOrderItem[lang][8]+""
                + "</button>";
            
            }
        }
        return result ;
    }
    
    private String getSaveButton(HttpServletRequest request){
        String result ="";
        MatStockOpname matStockOpname = new MatStockOpname();
        
        Boolean privAdd = FRMQueryString.requestBoolean(request, "privAdd");
        long oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        try {
            matStockOpname = PstMatStockOpname.fetchExc(oidStockOpname);
        } catch (Exception e) {
        }
        
        if(matStockOpname.getStockOpnameStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED || privAdd==false){
            result += "";
        }else{
            
            result += ""
            + "<button style='width:49%' class='btn btn-primary' style='padding: 5px; width: 75px;' type='button' id='saveOpname'>"
                + ""+textListGlobal[lang][11]+""
            + "</button>";
        }

        return result;
    }
    
    private String deleteOpname(HttpServletRequest request){
        String result ="";
        int iError = 0;
        
        long oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        String userName = FRMQueryString.requestString(request, "userName");
        long userId = FRMQueryString.requestLong(request, "userId");
                
        CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
        iError= ctrlMatStockOpname.action(Command.DELETE, oidStockOpname,  userName,  userId);
        
        if (iError==0){
            result = "0";
        }else{
            result = "1";
        }
        
        return result;
    }
    
    private String getMatItemBySKU(HttpServletRequest request){
        String result ="";
        
        MatStockOpname matStockOpname;
        Unit unit = new Unit();
        Vector  vect = new Vector();
        
        // REQUEST DATA
        long oidOpname = FRMQueryString.requestLong(request, "oidOpname");
        String sku = FRMQueryString.requestString(request, "sku");      
        int language = FRMQueryString.requestInt(request, "lang");
        
        try {
           matStockOpname = PstMatStockOpname.fetchExc(oidOpname); 
        } catch (Exception e) {
            matStockOpname = new MatStockOpname();
        }
        
        long materialGroup = matStockOpname.getCategoryId();
        long materialSubCategory = matStockOpname.getSubCategoryId();
        long materialSupplier = matStockOpname.getSupplierId();
        long oidLocation = matStockOpname.getLocationId();
        
        String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
        Material objMaterial = new Material();
        objMaterial.setCategoryId(materialGroup);
        objMaterial.setSubCategoryId(materialSubCategory);
        objMaterial.setSku(sku);
        objMaterial.setName(sku);
        objMaterial.setBarCode(sku);
       
        vect = PstMaterial.getListMaterialOpname2(materialSupplier, objMaterial, 0, 15, orderBy, oidLocation, oidOpname);
        
        if (vect.size()>0){
             Material material = (Material)vect.get(0);
             try {
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            } catch (Exception e) {
            }
            
             result += ""+material.getOID()+"="+material.getDefaultStockUnitId()+"="+material.getName()+"="+unit.getCode()+"";
        }else {
            result += "0=0=0=0";
        }
        
        return result;
    }
    
    private JSONArray getListItemAutoName(HttpServletRequest request){
        
        JSONArray resultArr = new JSONArray();
        MatStockOpname matStockOpname;
        Unit unit = new Unit();
        Vector  vect = new Vector();
        
        //REQUEST 
        String term = FRMQueryString.requestString(request, "search");
        long oidOpname = FRMQueryString.requestLong(request, "oidopname");     
        
        try {
           matStockOpname = PstMatStockOpname.fetchExc(oidOpname); 
        } catch (Exception e) {
            matStockOpname = new MatStockOpname();
        }
        
        long materialGroup = matStockOpname.getCategoryId();
        long materialSubCategory = matStockOpname.getSubCategoryId();
        long materialSupplier = matStockOpname.getSupplierId();
        long oidLocation = matStockOpname.getLocationId();
        
        String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
        Material objMaterial = new Material();
        objMaterial.setCategoryId(materialGroup);
        objMaterial.setSubCategoryId(materialSubCategory);
        objMaterial.setName(term);
        objMaterial.setSku(term);
        objMaterial.setBarCode(term);
        
        vect = PstMaterial.getListMaterialOpname2(materialSupplier, objMaterial, 0, 15, orderBy, oidLocation, oidOpname);
        if(vect!=null && vect.size()>0 ){
	    for(int i=0;i< vect.size();i++){
		Material material = (Material)vect.get(i);
                try {
                    unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                } catch (Exception e) {
                }
		try{
                    JSONObject result = new JSONObject();
		    result.put("data_value", material.getName());
		    result.put("data_key", ""+material.getOID()+"="+material.getName()+"="+material.getSku()+"="+unit.getOID()+"="+unit.getCode()+"");
		    resultArr.put(result);
		    
		}catch(JSONException jSONException){
		    jSONException.printStackTrace();
		}

	    }
	}
        return resultArr;
    }
    
    private String getHistoryOpname(HttpServletRequest request){
        String result ="";
        
        long oidDocHistory = FRMQueryString.requestLong(request, "oidDocHistory");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        String whereClauseHistory =""
            +" "+PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + "="+oidDocHistory+"";
        String orderClauseHistory = ""
            +" "+ PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_ID] + " ASC ";
        Vector listHistoryPurchaseOrder = PstLogSysHistory.listPurchaseOrder(0, 0, whereClauseHistory, orderClauseHistory);   

        result += ""
            + "<table class='table table-hover table-striped table-bordered'>"
                + "<thead>"
                    + "<tr style='font-size: 13px;'>"
                        + "<th><b>"+textListOrderHeader2[lang][0]+"</b></th>"
                        + "<th><b>"+textListOrderHeader2[lang][1]+"</b></th>"
                        + "<th><b>"+textListOrderHeader2[lang][2]+"</b></th>"
                        + "<th><b>"+textListOrderHeader2[lang][3]+"</b></th>"
                        + "<th><b>"+textListOrderHeader2[lang][4]+"</b></th>"
                    + "</tr>"
                + "</thead>"
                + "<tbody>";
                
        if (listHistoryPurchaseOrder.size()>0){
            for (int i = 0; i<listHistoryPurchaseOrder.size();i++){
                LogSysHistory logSysHistory = (LogSysHistory) listHistoryPurchaseOrder.get(i);
                result += ""
                    + "<tr>"
                        + "<td>"+logSysHistory.getLogLoginName()+"</td>"
                        + "<td>"+logSysHistory.getLogUserAction()+"</td>"
                        + "<td>"+Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy-MM-dd hh:mm:ss")+"</td>"
                        + "<td>"+logSysHistory.getLogApplication()+"</td>"
                        + "<td>"+logSysHistory.getLogDetail()+"</td>"
                    + "</tr>";
            }
            
        }else{
            result += ""
                + "<tr>"
                    + "<td colspan='5'><center><b>No Data...</b></center></td>"
                + "</tr>";
        }
        
        result += ""
                + "</tbody>"
            + "</table>";
        
        return result;
    }
    
    private String getPrintButton(HttpServletRequest request){
        String result ="";
        MatStockOpname matStockOpname = new MatStockOpname();
        
        
        long oidStockOpname = FRMQueryString.requestLong(request, "idOpname");
        int lang = FRMQueryString.requestInt(request, "lang");
        
        String whereClause = ""
            + " "+PstMatStockOpnameItem.fieldNames[PstMatStockOpnameItem.FLD_STOCK_OPNAME_ID]+"="+oidStockOpname+"";
        int count = PstMatStockOpnameItem.getCount(whereClause);
        if (count>0){
            result += ""
                + "<button id='printReport' class='btn btn-success pull-right' type='button' id='btnHistory'>"
                    + "<i class='glyphicon glyphicon-print'></i> Print" 
                + "</button>";
        }
        return result;
    }
}
