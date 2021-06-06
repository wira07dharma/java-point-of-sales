
package com.dimata.common.ajax;

import com.dimata.common.entity.location.Location;
import com.dimata.common.entity.location.PstLocation;
import com.dimata.common.entity.system.AppValue;
import com.dimata.common.entity.system.PstSystemProperty;
import com.dimata.gui.jsp.ControlList;
import com.dimata.posbo.entity.masterdata.Category;
import com.dimata.posbo.entity.masterdata.MatVendorPrice;
import com.dimata.posbo.entity.masterdata.Material;
import com.dimata.posbo.entity.masterdata.MaterialUnitOrder;
import com.dimata.posbo.entity.masterdata.PstCategory;
import com.dimata.posbo.entity.masterdata.PstMatVendorPrice;
import com.dimata.posbo.entity.masterdata.PstMaterialUnitOrder;
import com.dimata.posbo.entity.masterdata.Unit;
import com.dimata.posbo.entity.purchasing.PurchaseRequest;
import com.dimata.posbo.entity.search.SrcMinimumStock;
import com.dimata.posbo.form.purchasing.CtrlPurchaseRequest;
import com.dimata.posbo.form.purchasing.FrmPurchaseRequest;
import com.dimata.posbo.session.warehouse.SessMinimumStock;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.util.Command;
import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Witar
 */

public class AjaxPurchaseOrder extends HttpServlet{
    
    public static final String textMaterialHeader[][] = {
        {"Kategori","Sku","Nama Barang","Semua Barang"},
        {"Category","Code","Material Name","All Goods"}
    };
    
    public static final String textListMaterialHeader[][] = {
        {"NO","SKU","NAMA BARANG","UNIT","STOK","MINIMUM STOK"},
        {"NO","CODE","NAME PRODUCT","UNIT","CURRENT STOCK","MINIMUM STOCK"}
    };
    
    private String drawListMaterial2(int currency,int language,Vector objectClass,int start, boolean sts, boolean privShowStock){
        String result ="";
        result += "<table style='width :100%;' class='table table-striped'>";
        result += "<thead>";
        result += "<tr>";
        result += "<th>"+textListMaterialHeader[language][0]+"</th>";
        result += "<th>"+textListMaterialHeader[language][1]+"</th>";
        result += "<th>"+textListMaterialHeader[language][2]+"</th>";
        result += "<th>"+textListMaterialHeader[language][3]+"</th>";
        if (privShowStock){
            result += "<th>"+textListMaterialHeader[language][4]+"</th>";
            result += "<th>"+textListMaterialHeader[language][5]+"</th>";
        }
        result += "</tr>";
        result += "</thead>";
        result += "<tbody>";
        for(int i=0; i<objectClass.size(); i++){
            Vector vtitem = (Vector)objectClass.get(i);
            Material material = (Material)vtitem.get(0);
            Vector vMin = (Vector)vtitem.get(1);
            Vector vStock = (Vector)vtitem.get(2);
            Unit unit = (Unit)vtitem.get(3);

            start = start + 1;
            
            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            
            result += "<tr class='selectMaterial' data-oid ='"+material.getOID()+"' data-materialcurrency='"+material.getDefaultCostCurrencyId()+"' data-sku ='"+material.getSku()+"' data-name='"+name+"' data-unitcode ='"+unit.getCode()+"' data-unitoid='"+unit.getOID()+"'  style='cursor:pointer;'>";
            result += "<td>"+start+"</td>";
            result += "<td>"+material.getSku()+"</td>";
            result += "<td>"+material.getName()+"</td>";
            result += "<td>"+unit.getCode()+"</td>";
            if(privShowStock){
                for(int k=0; k<vStock.size(); k++){
                    double qtyStock = Double.parseDouble((String)vStock.get(k));
                    double qtyMin = Double.parseDouble((String)vMin.get(k));
                    result += "<td>"+qtyStock+"</td>";
                    result += "<td>"+qtyMin+"</td>";
               }
           }
           
            result += "</tr>";
        }
        
        result += "</tbody>";
        result += "</table>";
       
           
        return result;
    }
    private String savePurchaseOrder(HttpServletRequest request){
        
        int iErrCode = FRMMessage.NONE;
        int iCommand = FRMQueryString.requestCommand(request);
        
        int startItem = FRMQueryString.requestInt(request,"start_item");
        int prevCommand = FRMQueryString.requestInt(request,"prev_command");
        int appCommand = FRMQueryString.requestInt(request,"approval_command");
        long oidPurchaseRequest = FRMQueryString.requestLong(request,"hidden_material_request_id");
        long oidPurchaseRequestItem = FRMQueryString.requestLong(request,"hidden_mat_request_item_id");
        double DefaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
        long locationId = FRMQueryString.requestLong(request,"location_id");
        
        CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
        iErrCode = ctrlPurchaseRequest.action(Command.SAVE,oidPurchaseRequest,"",0);
        FrmPurchaseRequest frmPurchaseRequest = ctrlPurchaseRequest.getForm();
        PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();
        
        String prCode= po.getPrCode();
        
        return prCode;
    }
    private String checkMaterialItem(HttpServletRequest request){
        String result ="";
        int sessLanguage = FRMQueryString.requestInt(request,"sessLanguage");
        long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
        String materialcode = FRMQueryString.requestString(request,"mat_code");
        String materialname = FRMQueryString.requestString(request,"txt_materialname");
        
        result += "<div class=\"modal-header\">";
        result += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>";
        result += "<div class=\"modal-title\" id=\"modal-title\">DAFTAR BARANG</div>";
        result += "</div>";
        result += "<div class=\"modal-body\" id=\"modal-body\">";
        result += "<div class=\"row\">";
        result += "<div class='col-md-3'><label>"+textMaterialHeader[sessLanguage][0]+"</label></div>";
        result += "<div class='col-md-9'><select id=\"txt_materialgroup\"  name=\"txt_materialgroup\" class=\"form-control\">";
        result += "<option value=\"-1\">Semua Category</option>";
        Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);   
        Vector materGroup = PstCategory.structureList(masterCatAcak) ;
        Vector vectGroupVal = new Vector(1,1);
        Vector vectGroupKey = new Vector(1,1);
        
        if(materGroup!=null && materGroup.size()>0) {
            String parent="";
            Vector<Long> levelParent = new Vector<Long>();
            for(int i=0; i<materGroup.size(); i++) {
                Category mGroup = (Category)materGroup.get(i);
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
                String check="";
                if(materialgroup==mGroup.getOID()){
                    check="selected";
                }
                            
                result += "<option value='"+mGroup.getOID()+"' "+check+">"+parent+""+mGroup.getName()+"</option>";
                            
            }               
        }else{
            vectGroupVal.add("Tidak Ada Category");
            vectGroupKey.add("-1");         
        }
        
        result += "</select>";
        result += "</div>";
        result += "</div>";
        result += "<div class='row'>";
        result += "<div class='col-md-3'><label>"+textMaterialHeader[sessLanguage][1]+"</label></div>";
        result += "<div class='col-md-9'><input type=\"text\" name='mat_code' id='mat_code2'  value='"+materialcode+"' class='form-control formSearch'></div>";
        result += "</div>";
        result += "<div class='row'>";
        result += "<div class='col-md-3'><label>"+textMaterialHeader[sessLanguage][2]+"</label></div>";
        result += "<div class='col-md-9'><input type=\"text\"  name='txt_materialname'  value='"+materialname+"' class='form-control formSearch' id=\"txt_materialname2\"></div>";
        result += "</div>";
        result += "<div class='row'>";
        result += "<div class='col-md-3'>&nbsp</div>";
        result += "<div class='col-md-9'><button type='button' id='addNewItem' class='btn btn-primary pull-right'>Add New Item</button></div>";
        result += "</div>";
        result += "</div>";
        
                
                
            
        return result;
    }   
    private String loadMaterialItem(HttpServletRequest request){
        String result ="";
        long locationId = FRMQueryString.requestLong(request,"location_id");
        long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
        String materialcode = FRMQueryString.requestString(request,"mat_code");
        String materialname = FRMQueryString.requestString(request,"txt_materialname");
        long oidCurrency = FRMQueryString.requestLong(request,"currency_id");
        int sessLanguage = FRMQueryString.requestInt(request,"sessLanguage");
        int privShowStok = FRMQueryString.requestInt(request, "privShowStok");
        int currType = FRMQueryString.requestInt(request,"curr_type");
        boolean privShowStock = false;
        if (privShowStok==1){
            privShowStock = true;
        }
        boolean sts = true;
        
        Material objMaterial = new Material();
        objMaterial.setCategoryId(materialgroup);
        objMaterial.setSku(materialcode);
        objMaterial.setName(materialname);
        objMaterial.setDefaultCostCurrencyId(oidCurrency);
        objMaterial.setBarCode(materialcode);
        objMaterial.setMaterialType(0);
        
        SrcMinimumStock srcMinimumStock = new SrcMinimumStock();
        Location location = new Location();
        try{
            location=PstLocation.fetchExc(locationId);
            srcMinimumStock.setvLocation(location);
            srcMinimumStock.setCategoryId(materialgroup);
        }catch(Exception ex){

        }
        
        Vector vect = SessMinimumStock.getSearchMaterialListMinimumStock(srcMinimumStock,0,10,objMaterial);
        
        result += "<div class=\"modal-body\" id=\"modal-body\">";
        result += "<div class='row'>";
        result += "<div class='col-md-12'>";
        result += ""+drawListMaterial2(currType,sessLanguage,vect,0,sts,privShowStock)+"";
        result += "</div></div>";
        result += "</div>";
        result += "<div class=\"modal-footer\">";
        result += "<button style=\"float: right; \" type=\"button\" data-dismiss=\"modal\" class=\"btn btn-danger\">Close</button>";
        result += "</div>";
                               
                            
        
        return result;
    }
    private String loadAddMaterialForm(HttpServletRequest request){
        String result ="";
        
        String root = FRMQueryString.requestString(request,"root");
        
        result += "<div class=\"modal-header\">";
        result += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>";
        result += "<div class=\"modal-title\" id=\"modal-title\">EDITOR BARANG</div>";
        result += "</div>";
        result += "<div class=\"modal-body\" id=\"modal-body\">";
        result += "<div class=\"row\">";
        result += "<div class='col-md-12'>";
        result += "<iframe src='"+root+"/master/material/material_main.jsp?command=2&ext=1' style='width: 100%;height: 500px;border: medium none;'></iframe>";
        result += "</div>";
        result += "</div>";
        result += "</div>";
        result += "<div class=\"modal-footer\">";
        result += "<button style=\"float: right; \" type=\"button\" data-dismiss=\"modal\" class=\"btn btn-danger\">Close</button>";
        result += "<button style=\"float: right; margin-right:5px;\" id='btnBackFromAdd' type=\"button\"  class=\"btn btn-default\">Back</button>";
        result += "</div>";
        result += "";
        result += "";
        result += "";
        result += "";
        return result;
    }
    private String cekVendorByIdMaterial(HttpServletRequest request){
        String result = "";
        long matOid= FRMQueryString.requestLong(request, "oid");
        int tOfPayment = FRMQueryString.requestInt(request, "tOfPayment");
        long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));
        
        String whereClause = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+matOid;
        Vector listMatVendorPrice = PstMatVendorPrice.listFiltter(0,0, "" , "");
     
        long firstVendor=0;
        String firstVendorName="";
        double firstPriceBuying=0.0;
        
        for(int i=0; i<listMatVendorPrice.size(); i++)
        {
            MatVendorPrice matVendorPrice = (MatVendorPrice)listMatVendorPrice.get(i);
            if(i==0){
                firstVendor=matVendorPrice.getVendorId();
                firstVendorName=PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
            }
            
            result += "<option class='"+firstVendor+";"+firstVendorName+"' value='"+matVendorPrice.getVendorId()+"'>"+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId())+"</option>";
            
        }
        
//        if(tOfPayment!=2){
//            result += "<option class='"+firstVendor+";"+firstVendorName+"' value='"+oidNewSupplier+"'>New Supplier</option>";
//        }
        
        return result;
    }
    private String loadUnitMaterial(HttpServletRequest request){
        String result ="";
        double firstPriceBuying=0.0;
        
        //cek unit yang bisa dibeli di vendor
        Vector vectUnitSupVal = new Vector(1,1);
        Vector vectUnitSupKey = new Vector(1,1);
        
        long firstVendor = FRMQueryString.requestLong(request, "firstVendor");
        long matOid = FRMQueryString.requestLong(request, "matOid");
        long unitId = FRMQueryString.requestLong(request,"unitId");
        String unit = FRMQueryString.requestString(request,"unit");
        
        if(firstVendor!=0){
            String whereClauseUnitSupp = " MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+matOid+""+
                                         " AND MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"="+firstVendor;
            Vector listJoinUnit = PstMatVendorPrice.listJoinUnit(0,0, whereClauseUnitSupp , "");
            for(int i=0; i<listJoinUnit.size(); i++)
                {
                    MatVendorPrice matVendorPrice = (MatVendorPrice)listJoinUnit.get(i);
                    if(i==0){
                        firstPriceBuying=matVendorPrice.getOrgBuyingPrice();
                    }
                    result += "<option class='"+firstPriceBuying+"' value='"+matVendorPrice.getBuyingUnitId()+"'>"+matVendorPrice.getBuyingUnitName()+"</option>";
                    
                } 
        }else{
        //tampilkan unit yang bisa di beli di material tsb
            String whereBaseUnitOrder = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID] + " ='"+matOid+"'";
            Vector listBaseUnitOrder = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
            if (listBaseUnitOrder.size()>0){
                for(int i=0; i<listBaseUnitOrder.size(); i++){
                    MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listBaseUnitOrder.get(i);
                    vectUnitSupKey.add(""+materialUnitOrder.getUnitID());
                    vectUnitSupVal.add(""+materialUnitOrder.getUnitKode());
                    result += "<option class='0' value='"+materialUnitOrder.getUnitID()+"'>"+materialUnitOrder.getUnitKode()+"</option>";
                }
            }else{
                result += "<option class='0' value='"+unitId+"'>"+unit+"</option>";
            }
            
        }
        return result;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String result ="";
        int command = FRMQueryString.requestInt(request,"command");
        response.setContentType("text/html;charset=UTF-8");
       
        switch (command) {
            case Command.SAVE :
                result= savePurchaseOrder(request);
                response.getWriter().write(result);
                break;
            case Command.FIRST :
                result = checkMaterialItem(request);
                response.getWriter().write(result);
                break;
            case Command.NEXT :
                result = loadMaterialItem(request);
                response.getWriter().write(result);
                break;
            case Command.ADD:
                result = loadAddMaterialForm(request);
                response.getWriter().write(result);
                break;
            case Command.GET:
                result = cekVendorByIdMaterial(request);
                response.getWriter().write(result);
                break;
            case Command.ASK:
                result = loadUnitMaterial(request);
                response.getWriter().write(result);
            default:

        }
    }
}
