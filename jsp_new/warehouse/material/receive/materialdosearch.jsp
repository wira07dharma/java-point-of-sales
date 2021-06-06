<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.session.warehouse.SessDoSearch"%>
<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@ page import="com.dimata.gui.jsp.ControlList,com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
				 com.dimata.common.entity.payment.PstStandartRate,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.posbo.form.masterdata.CtrlMaterial,
                 com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                 com.dimata.posbo.entity.search.SrcMaterial"%>
<%@page contentType="text/html"%>

<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!
public static final String textListGlobal[][] = {
    {"Pencarian Barang","Tidak ada barang"},
    {"Goods Searching","No goods available"}
};

/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] = {
    {"Kategori","Sku","Nama Barang","Semua"},
    {"Category","Material Code","Material Name","All"}
};

/* this constant used to list text of listHeader */
//public static final String textListMaterialHeader[][] = {
   //{"No","Grup","Sku","Nama Barang","Unit","Harga Beli"},
   //{"No","Category","Code","Item","Unit","Cost"}
//};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
   {"No","Grup","Sku","Nama Barang","Unit","Harga Beli","Qty"},
   {"No","Category","Code","Item","Unit","Cost","Qty"}
};

public String drawListMaterial(long currency, double transRate, int language,Vector objectClass,int start) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][6],"10%");

        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        
        int hpp_calculate =Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));
        /** mendapatkan objek hashtable yang berisi list current daily rate */
        Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
        
        for(int i=0; i<objectClass.size(); i++) {
            
            Vector vt = (Vector)objectClass.get(i);
            
            Material material = (Material)vt.get(0);
            
            MaterialStock materialStock = (MaterialStock)vt.get(1);

            //Material material = new Material();
            try {
                    material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
            }
            catch(Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }

            Unit unit = new Unit();
            try {
                    unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            }
            catch(Exception e) {
                    System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
            }

            start = start + 1;
            
            double standardRate = 0;
            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}            
            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            //try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}
            try{
                if(hpp_calculate==1){
                    defaultCost = (material.getAveragePrice() * standardRate) / transRate;
                }else{
                    defaultCost = (material.getCurrBuyPrice() * standardRate) / transRate;
                }
            }   
            catch(Exception exc){
            
            }
            
            Vector rowx = new Vector();
            
            rowx.add(""+start);
            // rowx.add(categ.getName());
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(unit.getCode());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");
            rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(defaultCost)+"</a></div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            
            lstData.add(rowx);
            /*lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode());*/
            /*lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+(defaultCost>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultCost)) : "")+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode());*/
            lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+(defaultCost>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultCost)) : "")+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+/*matCurr.getCode()+ */"','"+materialStock.getQty());
        }
        return ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>";
    }
    return result;
}

public String drawListMaterialAutoSave(long currency, double transRate, int language,Vector objectClass,int start, long locationId) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        //ctrlist.addHeader(textListMaterialHeader[language][1],"20%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"35%");
        ctrlist.addHeader(textListMaterialHeader[language][4],"5%");
        ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][6],"10%");

        ctrlist.setLinkRow(2);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEditAuto('");
        if (objectClass.size() == 1){
            ctrlist.setLinkSufix("')\" class=\"autoClick");
        } else {
            ctrlist.setLinkSufix("')");
        }
        
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        
        int hpp_calculate =Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));
        /** mendapatkan objek hashtable yang berisi list current daily rate */
        Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
        
        for(int i=0; i<objectClass.size(); i++) {
            
            Vector vt = (Vector)objectClass.get(i);
            
            Material material = (Material)vt.get(0);
            
            MaterialStock materialStock = (MaterialStock)vt.get(1);

            //Material material = new Material();
            try {
                    material = PstMaterial.fetchExc(materialStock.getMaterialUnitId());
            }
            catch(Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }

            Unit unit = new Unit();
            try {
                    unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
            }
            catch(Exception e) {
                    System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
            }

            String whereMatMapKsg = "KSG."+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"="+locationId+" AND "
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

            Location l = new Location();
            if (ksg.getLocationId() != 0) {
                try {
                    l = PstLocation.fetchExc(ksg.getLocationId());
                } catch (Exception exc){}
            }

            Color color = new Color();
            try {
                color = PstColor.fetchExc(material.getPosColor());
            } catch (Exception exc){
                System.out.println("Exc when PstColor.fetchExc() : " + exc.toString());
            }

            Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, 
                    PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");

            String selectedMap = "";
            if (listGroupMapping.size()>0){
                for (int x=0;x< listGroupMapping.size();x++){
                    MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                    MasterGroup masterGroup = new MasterGroup();
                    try {
                        masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                    } catch (Exception exc){}
                    long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(),material.getOID());
                    selectedMap += ","+String.valueOf(oidMapping);
                }
                if (selectedMap.length() > 0){
                    selectedMap = selectedMap.substring(1);
                }
            }
            start = start + 1;
            
            double standardRate = 0;
            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}            
            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            //try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}
            try{
                if(hpp_calculate==1){
                    defaultCost = (material.getAveragePrice() * standardRate) / transRate;
                }else{
                    defaultCost = (material.getCurrBuyPrice() * standardRate) / transRate;
                }
            }   
            catch(Exception exc){
            
            }
            
            Vector rowx = new Vector();
            
            rowx.add(""+start);
            // rowx.add(categ.getName());
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(unit.getCode());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"</div>");
            rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(defaultCost)+"</a></div>"
                    + (selectedMap.length()>0 ? "<input type='hidden' id='typeMapping' value='"+selectedMap+"'>" : ""));
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");

            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            
            lstData.add(rowx);
            /*lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice())+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode());*/
            /*lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+(defaultCost>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultCost)) : "")+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+matCurr.getCode());*/
            lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                    "','"+(defaultCost>0.00 ? (""+FRMHandler.userFormatStringDecimal(defaultCost)) : "")+"','"+unit.getOID()+//material.getDefaultStockUnitId()+
                    "','"+material.getDefaultCostCurrencyId()+"','"+/*matCurr.getCode()+ */"','"+materialStock.getQty()+
                    "','"+ksg.getOID()+"','"+color.getOID());
        }
        return ctrlist.draw();
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>";
    }
    return result;
}


public String drawListAllMaterial(int language,Vector objectClass,int start, boolean sts, double curr,double transRate){
    String result = "";
    int hpp_calculate =Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));
    Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
        
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
        ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][2],"40%");
        ctrlist.addHeader(textListMaterialHeader[language][3],"10%");

        ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
        ctrlist.addHeader(textListMaterialHeader[language][6],"10%");

        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        
        for(int i=0; i<objectClass.size(); i++){
            Vector vt = (Vector)objectClass.get(i);
            Material material = (Material)vt.get(0);
            Category categ = (Category)vt.get(1);
            Unit unit = (Unit)vt.get(2);
            MatCurrency matCurr = (MatCurrency)vt.get(3);
            MatVendorPrice matVendorPrice = (MatVendorPrice)vt.get(4);
            start = start + 1;
            

            try {
                    material = PstMaterial.fetchExc(material.getOID());
            }
            catch(Exception e) {
                    System.out.println("Exc when PstMaterial.fetchExc() : " + e.toString());
            }

            Vector rowx = new Vector();
            double exchangeRateLastReceive=1;
            String orderLastReceiveBy = PstMatReceive.fieldNames[PstMatReceive.FLD_RECEIVE_DATE]+" DESC ";
            Vector listLastReceive = PstMatReceiveItem.listLastReceive(material.getOID(),0,1,"",orderLastReceiveBy);
             for(int r=0; r<listLastReceive.size(); r++){
                  MatReceiveItem matreceiveitem = (MatReceiveItem)listLastReceive.get(r);//new MatReceiveItem();
                  if(curr==1){
                      if(matreceiveitem.getExchangeRate()==0){
                           exchangeRateLastReceive=1;
                      }else{
                           exchangeRateLastReceive=matreceiveitem.getExchangeRate();
                      }   
                  }  
            }



            double standardRate = 0;
            try{standardRate = Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));}catch(Exception exc){;}            
            double defaultCost = 0; //mendapatkan besaran default cost dalam mata uang transaksi
            //try{defaultCost = (material.getDefaultCost() * standardRate) / transRate;}catch(Exception exc){;}
            try{
                if(hpp_calculate==1){
                    defaultCost = (material.getAveragePrice() * standardRate) / transRate;
                }else{
                    defaultCost = (material.getCurrBuyPrice() * standardRate) / transRate;
                }
            }   
            catch(Exception exc){
            
            }

            rowx.add(""+start);
            rowx.add(material.getSku());
            rowx.add(material.getName());
            rowx.add(unit.getCode());

            //rowx.add(unit.getCode());
            //rowx.add(unit.getCode());
            rowx.add("<div align=\"right\"><a href=\"javascript:cmdEditItem('"+material.getOID()+"')\">"+FRMHandler.userFormatStringDecimal(defaultCost)+"</a></div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(0)+"</div>");
            
            lstData.add(rowx);
            
            String name = "";
            name = material.getName();
            name = name.replace('\"','`');
            name = name.replace('\'','`');
            if(sts){
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal((matVendorPrice.getOrgBuyingPrice()*exchangeRateLastReceive))+"','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getCurrBuyingPrice()*exchangeRateLastReceive));
            }else{
                lstLinkData.add(material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+
                        "','"+FRMHandler.userFormatStringDecimal(material.getDefaultCost()*exchangeRateLastReceive)+"','"+unit.getOID()+
                        "','"+material.getDefaultCostCurrencyId()+"','"+FRMHandler.userFormatStringDecimal(matVendorPrice.getLastDiscount())+"','"+FRMHandler.userFormatStringDecimal(material.getDefaultCost()*exchangeRateLastReceive));
            }
        }
        //return ctrlist.drawTableNavigation();
         return ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data ...</div>";
    }
    return result;
}
%>

<!-- JSP Block -->
<%
String materialcode = FRMQueryString.requestString(request,"mat_code");
long materialgroup = FRMQueryString.requestLong(request,"txt_materialgroup");
String materialname = FRMQueryString.requestString(request,"txt_materialname");
long locationId = FRMQueryString.requestLong(request,"location_id");
long oidVendor = FRMQueryString.requestLong(request,"mat_vendor");
long currencyId = FRMQueryString.requestLong(request,"currency_id");
double transRate = FRMQueryString.requestDouble(request,"trans_rate");
int start = FRMQueryString.requestInt(request, "start");
int iCommand = FRMQueryString.requestCommand(request);
long oidMatReceive = FRMQueryString.requestLong(request, "rec_id");
int recordToGet = 15;
//show all good
int showAllGoods = FRMQueryString.requestInt(request, "show_all_good");
String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
String syspropAutoSave = PstSystemProperty.getValueByName("AUTO_SAVE_RECEIVING");
String supplierName = "";
try{
    PstContactList pstContactList = new PstContactList();
    ContactList contactList = pstContactList.fetchExc(oidVendor);
    supplierName = contactList.getCompName();
}catch(Exception e){
    System.out.println("Err fetch ContactList");
}

MatReceive matRec = new MatReceive();
try {
    matRec = PstMatReceive.fetchExc(oidMatReceive);
} catch (Exception exc){
    
}

//data from penerimaan litama
int itemMaterialType = FRMQueryString.requestInt(request, "material_type");
String whereAdd = "";
if (typeOfBusinessDetail == 2) {
    whereAdd = " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_MATERIAL_JENIS_TYPE] + " = '" + itemMaterialType + "'"
            + " AND MT." + PstMaterial.fieldNames[PstMaterial.FLD_GONDOLA_CODE] + " IN ("
            + " SELECT " + PstKsg.fieldNames[PstKsg.FLD_KSG_ID] + " FROM " + PstKsg.TBL_MAT_KSG 
            + " WHERE " + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " = '" + locationId + "'"
            + ")";
}

/**
* instantiate material object that handle searching parameter
*/
String orderBy = "MAT."+PstMaterial.fieldNames[PstMaterial.FLD_SKU];
Material objMaterial = new Material();
objMaterial.setCategoryId(materialgroup);
objMaterial.setSku(materialcode);
objMaterial.setBarCode(materialcode);
objMaterial.setName(materialname);
objMaterial.setDefaultCostCurrencyId(currencyId);

SrcMaterial objSrcMaterial = new SrcMaterial();
objSrcMaterial.setLocationId(locationId);
objSrcMaterial.setCategoryId(materialgroup);
objSrcMaterial.setMatcode(materialcode);
objSrcMaterial.setMatname(materialname);
//objSrcMaterial.setBarCode(materialcode);




/**
* get amount/count of material's list
*/
//int vectSize = PstMaterial.getCountMaterialItem(0,objMaterial);
int vectSize = 0;
//int vectSize = PstMaterialStock.countMaterialStockCurrPeriodAllBarcode(objSrcMaterial);

if(showAllGoods==1){
    vectSize = PstMaterial.getCountMaterialItem(0,objMaterial);
}else{
    vectSize = SessDoSearch.countMaterialStockCurrPeriodAll(objSrcMaterial, whereAdd);
}
/**
* generate start/mailstone for displaying data
*/
CtrlMaterial ctrlMaterial = new CtrlMaterial(request);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
	start = ctrlMaterial.actionList(iCommand, start, vectSize, recordToGet);
} 

Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, 
        PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
 
/**
* get material list will displayed in this page
*/ 
//Vector vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);
int automaticSelected = 1;//Integer.valueOf(AppValue.getValueByKey("AUTOMATIC_SELECTED"));

Vector vect = new Vector();

if(showAllGoods==1){
    vect = PstMaterial.getListMaterialItem(0,objMaterial,start,recordToGet, orderBy);
}else{
    vect = SessDoSearch.listMaterialStockCurrPeriodAll(objSrcMaterial, start, recordToGet, "", whereAdd);
}

/** kondisi ini untuk menangani jika menemukan satu buah item saja, sehingga langsung di teruskan ke halaman item */
if(vectSize == 1 && automaticSelected==0) {
    
    Vector vt = (Vector)vect.get(0);
    
    Material material = (Material)vt.get(0);

    Unit unit = new Unit();
    try {
            unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
    }
    catch(Exception e) {
            System.out.println("Exc when PstUnit.fetchExc() : " + e.toString());
    }
    MatCurrency matCurr = new MatCurrency();
    try{
        matCurr =PstMatCurrency.fetchExc(material.getDefaultCostCurrencyId());
    }
    catch(Exception e) {
            System.out.println("Exc when PstMatCurrency.fetchExc() : " + e.toString());
    }

    /** mendapatkan objek hashtable yang berisi list current daily rate */
    Hashtable objActiveStandardRate = PstStandartRate.getActiveStandardRate();
    int hpp_calculate =Integer.parseInt(PstSystemProperty.getValueByName("HPP_CALCULATION"));
    double standardRate = 1.0d;
    try{
        standardRate =Double.parseDouble((String)objActiveStandardRate.get(""+material.getDefaultCostCurrencyId()));
        }catch(Exception exc){
             System.out.println("materialdosearch.jsp"+exc.toString());
            }
    double defaultCost = 0.0d;
    try{
       //defaultCost = (material.getDefaultCost() * standardRate) / transRate; //mendapatkan besaran default cost dalam mata uang transaksi
        if(hpp_calculate==1){
            defaultCost = (material.getAveragePrice() * standardRate) / transRate;
        }else{
            defaultCost = (material.getCurrBuyPrice() * standardRate) / transRate;
        }
        
    } catch(Exception exc){
         System.out.println("materialdosearch.jsp"+exc.toString());
    }
    
    String name = "";
    name = material.getName();
    name = name.replace('\"','`');
    name = name.replace('\'','`');
    
    /*String link = "'"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+"','"+
            FRMHandler.userFormatStringDecimal(defaultCost)+"','"+unit.getOID()+"','"+
            material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"'";*/
     /* String link = "'"+material.getOID()+"','"+material.getSku()+"','"+name+"','"+unit.getCode()+"','"+
            FRMHandler.userFormatStringDecimal(defaultCost)+"','"+unit.getOID()+"','"+
            material.getDefaultCostCurrencyId()+"','"+matCurr.getCode()+"','"+materialStock.getQty()+"'";*/
    %>
    <script language="JavaScript">
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = "<%=material.getOID()%>";
       self.opener.document.forms.frm_recmaterial.matCode.value = "<%=material.getSku()%>";
       self.opener.document.forms.frm_recmaterial.matItem.value = "<%=name%>";
       self.opener.document.forms.frm_recmaterial.matUnit.value = "<%=unit.getCode()%>";
       <% if (syspropHargaBeli.equals("1")){ %>
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = "<%=FRMHandler.userFormatStringDecimal(defaultCost)%>";
       <% } %>
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = "<%=unit.getOID()%>";
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = "<%=material.getDefaultCostCurrencyId()%>";
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = "0";
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
       self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
       self.close();
    </script>
    <%
}




%>
<!-- End of JSP Block -->
<html>
<head>
<title>Dimata - ProChain POS</title>
<script language="JavaScript">


function addNewItem(){
    document.frmvendorsearch.command.value="<%=Command.ADD%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
    }

function cmdEdit(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty){
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
    self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
    self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = stockQty;
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
    
    <%if(typeOfBusinessDetail == 2){%>
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = 1;
    <%} else {%>
        
    <%}%>
        
    <%if(privShowQtyPrice){%>
        self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matCode);        
    <%}else{%>
        self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matQty);         
    <%}%>  
    
    self.close();
}

function cmdEditAuto(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty,etalaseId,colorId){
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
    self.opener.document.forms.frm_recmaterial.matCode.value = matCode;
    self.opener.document.forms.frm_recmaterial.matItem.value = matItem;
    self.opener.document.forms.frm_recmaterial.matUnit.value = matUnit;
    <% if (syspropHargaBeli.equals("1")){ %>
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
    <% } %>
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = stockQty;
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
    
    <%if(typeOfBusinessDetail == 2){%>
        self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = 1;
    <%} else {%>
        
    <%}%>
        
    <%if(privShowQtyPrice){%>
        self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matCode);        
    <%}else{%>
        self.opener.changeFocus(self.opener.document.forms.frm_recmaterial.matQty);         
    <%}%>
    <% if (syspropColor.equals("1")){ %>
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_GONDOLA_ID]%>.value = etalaseId;
    <% } %>    
    <% if (syspropEtalase.equals("1")){ %>
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COLOR_ID]%>.value = colorId;
    <% } %>     
    var map =document.getElementById("typeMapping").value;
    var maps = map.split(",");
    if (maps.length > 0){
        var i;
        for (i = 0; i < maps.length; i++) {
            self.opener.document.getElementById("MASTER_GROUP"+(i+1)).value = maps[i];
        } 
    }
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value = matUnitId;
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>.value ="1";
    self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value ="1";
    self.opener.document.forms.frm_recmaterial.command.value=<%=Command.SAVE%>;
    self.opener.document.forms.frm_recmaterial.submit();
    self.close();
}

function cmdEditItem(oid)
{
    document.frmvendorsearch.hidden_material_id.value=oid;
    document.frmvendorsearch.start.value=0;
    document.frmvendorsearch.approval_command.value="<%=Command.APPROVE%>";
    document.frmvendorsearch.command.value="<%=Command.EDIT%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
}

function cmdListFirst(){
    document.frmvendorsearch.command.value="<%=Command.FIRST%>";
    document.frmvendorsearch.action="materialdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListPrev(){
    document.frmvendorsearch.command.value="<%=Command.PREV%>";
    document.frmvendorsearch.action="materialdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListNext(){
    document.frmvendorsearch.command.value="<%=Command.NEXT%>";
    document.frmvendorsearch.action="materialdosearch.jsp";
    document.frmvendorsearch.submit();
}

function cmdListLast(){
    document.frmvendorsearch.command.value="<%=Command.LAST%>";
    document.frmvendorsearch.action="materialdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function cmdSearch(){
    document.frmvendorsearch.command.value="<%=Command.LIST%>";
    document.frmvendorsearch.start.value = '0';
    document.frmvendorsearch.action="materialdosearch.jsp";
    document.frmvendorsearch.submit();
}	

function clear(){
    document.frmvendorsearch.txt_materialcode.value="";
}

function fnTrapKD(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSearch();
    }
}

function keyDownCheck(e){
    //activeTable();
   var trap = document.frmvendorsearch.trap.value;
   
   if (e.keyCode == 13 && trap==0) {
    document.frmvendorsearch.trap.value="1";
   }
   if (e.keyCode == 13 && trap==1) {
       document.frmvendorsearch.trap.value="0";
       cmdSearch();
       
   }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frmvendorsearch.txt_materialname.value="";
   }
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">


<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

<SCRIPT language=JavaScript>
function hideObjectForMarketing(){    
} 
	 
function hideObjectForWarehouse(){ 
}
	
function hideObjectForProduction(){
}
	
function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

</SCRIPT>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader" colspan="2"><%=textListGlobal[SESS_LANGUAGE][0]%></td>
        </tr>
        <tr><%@include file="../../../styletemplate/template_header_empty.jsp" %>
          <td colspan="2">
            <hr size="1">
          </td>
        </tr>
        <tr>
          <td>
          <%
            try{
          %>
            <form name="frmvendorsearch" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="location_id" value="<%=locationId%>">
              <input type="hidden" name="mat_vendor" value="<%=oidVendor%>">
              <input type="hidden" name="currency_id" value="<%=currencyId%>">
              <input type="hidden" name="trans_rate" value="<%=transRate%>">
              <input type="hidden" name="source_link" value="materialdosearch.jsp">
              <input type="hidden" name="hidden_material_id" value="0">
              <input type="hidden" name="approval_command" value="0">
              <input type="hidden" name="trap" value="">
              <input type="hidden" name="material_type" value="<%=itemMaterialType%>">

              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <tr>
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                  <td width="87%"> 
                      <%--
                  <%
                  Vector category = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                  Vector vectGroupVal = new Vector(1,1);
                  Vector vectGroupKey = new Vector(1,1);
                  vectGroupVal.add(textMaterialHeader[SESS_LANGUAGE][3]+" "+textMaterialHeader[SESS_LANGUAGE][0]);
                  vectGroupKey.add("-1");
                  if(category!=null && category.size()>0) {
                      for(int i=0; i<category.size(); i++) {
                          Category mGroup = (Category)category.get(i);
                          vectGroupVal.add(mGroup.getName());
                          vectGroupKey.add(""+mGroup.getOID());
                      }
                  } else {
                      vectGroupVal.add("No Category Available");
                      vectGroupKey.add("0");
                  }
                  //out.println(ControlCombo.draw("txt_materialgroup","formElemen", null, ""+materialgroup, vectGroupKey, vectGroupVal, null));
                  %>
                  <%=ControlCombo.draw("txt_materialgroup", null, ""+materialgroup, vectGroupKey, vectGroupVal, " onkeydown=\"javascript:fnTrapKD(event)\"", "formElemen")%>
                  --%>
                   <select  name="txt_materialgroup" class="formElemen">
                   <option value="0">Semua Category</option>
                   <%
                    Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                    //Category newCategory = new Category();
                    //add opie-eyek 20130821
                    String checked="selected";
                    Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    if(materGroup!=null && materGroup.size()>0) {
                        String parent="";
                       // Vector<Category> resultTotal= new Vector();
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
                            %>
                                <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID()==objSrcMaterial.getCategoryId()?checked:""%> ><%=parent%><%=mGroup.getName()%></option>
                            <%
                        }
                    } else {
                        vectGroupVal.add("Tidak Ada Category");
                        vectGroupKey.add("-1");
                    }
                  %>
                  </select>
                  <input type="checkbox" name="show_all_good" value="1" <% if( showAllGoods==1){%>checked<%}%> >
                  <%=textMaterialHeader[SESS_LANGUAGE][3]%>
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                  <td width="87%"> 
                    <input type="text" name="mat_code" size="15" value="<%=materialcode%>" class="formElemen" onKeyDown="javascript:fnTrapKD(event)">
                  </td>
                </tr>
                <tr> 
                  <td width="13%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                  <td width="87%"> 
                    <input type="text" name="txt_materialname" size="30" value="<%=materialname%>" class="formElemen" onKeyDown="javascript:keyDownCheck(event)" id="txt_materialname">
                  </td>
                </tr>
                <tr> 
                  <td width="13%">&nbsp;</td>
                  <td width="87%"> 
                    <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                    <input type="button" name="Button2" value="Add New Item" onClick="javascript:addNewItem()" class="formElemen">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2">
                      <%if(showAllGoods==1){%>
                            <%=drawListAllMaterial(SESS_LANGUAGE,vect,start,true,1,transRate)%>
                      <%}else if (syspropAutoSave.equals("1")){%>
                            <%=drawListMaterialAutoSave(currencyId, transRate, SESS_LANGUAGE, vect, start, matRec.getLocationId())%>
                      <%} else {%>
                            <%=drawListMaterial(currencyId, transRate, SESS_LANGUAGE, vect, start)%>
                      <% } %>
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"><span class="command"> 
                    <%
                    ControlLine ctrlLine= new ControlLine();
                    %>
                    <%
                    ctrlLine.setLocationImg(approot+"/images");
                    ctrlLine.initDefault();
                    %>
                    <%=ctrlLine.drawImageListLimit(iCommand ,vectSize,start,recordToGet)%> </span></td>
                </tr>
              </table>
            </form>
            <%
            }catch(Exception e){
                System.out.println("terr "+e.toString());
            }
            %>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
<script language="JavaScript">
	document.frmvendorsearch.txt_materialgroup.focus();
</script>
<% if (syspropAutoSave.equals("1") && vect.size() == 1) {%> 
<script type="text/javascript">
$(function(){
     
    window.location.href = $('.autoClick').attr('href');
});
</script>
<% } %> 
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
        
       

</script>
</html>
