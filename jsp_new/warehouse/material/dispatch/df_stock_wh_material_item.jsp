<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

                   com.dimata.common.entity.location.Location,

                   com.dimata.common.entity.location.PstLocation,

                   com.dimata.gui.jsp.ControlList,

                   com.dimata.util.Command,

                   com.dimata.qdep.form.FRMHandler,

                   com.dimata.qdep.form.FRMMessage,

                   com.dimata.qdep.form.FRMQueryString,

                   com.dimata.qdep.entity.I_PstDocType,

                   com.dimata.gui.jsp.ControlLine,

                   com.dimata.gui.jsp.ControlCombo,

                   com.dimata.gui.jsp.ControlDate,

                   com.dimata.posbo.form.warehouse.FrmMatDispatchItem,

                   com.dimata.posbo.entity.masterdata.Unit,

                   com.dimata.posbo.entity.masterdata.Material,

                   com.dimata.posbo.entity.masterdata.PstMaterial,

                   com.dimata.posbo.entity.warehouse.*,

                   com.dimata.posbo.form.warehouse.FrmMatDispatch,

                   com.dimata.posbo.form.warehouse.CtrlMatDispatch,

                   com.dimata.posbo.form.warehouse.CtrlMatDispatchItem,

                   com.dimata.posbo.entity.masterdata.MaterialStock,

                   com.dimata.posbo.entity.masterdata.PstMaterialStock" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>

				   

<%@ include file = "../../../main/javainit.jsp" %>

<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
    int appObjCodePayment = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_PAYMENT);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodePayment, AppObjInfo.COMMAND_FINAL));
%>
<!-- Jsp Block -->

<%!

public static final String textListGlobal[][] = {

    {"Transfer","Edit","Quantity transfer melebihi quantity yang ada"},

    {"Dispatch","Edit","Dispatch quantity more than available quantity"}

};



/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] =

{

    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Etalase Asal","Etalase Tujuan"},

    {"Number","Location","Destination","Date","Status","Remark","Supplier Invoice","From Etalase","Etalase Destination"}

};



/* this constant used to list text of listMaterialItem */

public static final String textListOrderItem[][] =

{

    {"No","Sku","Nama Barang","Unit","Qty","HPP","Harga Jual","Total","Gondola","Hapus","Berat Awal", //10
    "Berat Akhir","Berat Selisih"},//12

    {"No","Code","Goods Name","Unit","Qty","Avg.Cost","Retail Price","Total","Brand","Delete","Last Weight",//10
    "Current Weight","Weight Difference"}//12

};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};



public double getPriceCost(Vector list, long oid){

    double cost = 0.00;

    if(list.size()>0){

        for(int k=0;k<list.size();k++){

            MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(k);

            if(matReceiveItem.getMaterialId()==oid){

                cost = matReceiveItem.getCost();

                break;

            }

        }

    }

    return cost;

}



/**

 * this method used to maintain dfList

 */

public Vector drawListDfItem(int language,int iCommand,FrmMatDispatchItem frmObject,MatDispatchItem objEntity,
        Vector objectClass,long dfItemId,int start, String invoiceNumber, int tranUsedPriceHpp, long locationId,
        int brandInUse,boolean privShowQtyPrice, String approot, int typeOfBusinessDetail, 
        int dispatchItemType, long locationTo, String syspropEtalase, String syspropTotal, String syspropHPP, boolean privPayment) {
    String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA");
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItem[language][0],"1%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"10%");//sku
    if(brandInUse==1){ 
        if(typeOfBusinessDetail != 2 && syspropEtalase.equals("1")){
            ctrlist.addHeader(textListOrderItem[language][8],"10%");//gondola
        }
    }
    ctrlist.addHeader(textListOrderItem[language][2],"15%");//nama barang    
    if (privShowQtyPrice) {
        ctrlist.addHeader(textListOrderItem[language][4], "5%");//qty
    }
    if(typeOfBusinessDetail == 2){
        ctrlist.addHeader("Etalase Asal","5%");//etalase awal
        ctrlist.addHeader("Etalase Tujuan","5%");//etalase akhir
    }
    if(typeOfBusinessDetail != 2){
        ctrlist.addHeader(textListOrderItem[language][3],"5%");//unit
    }
    if (typeOfBusinessDetail == 2) {
        ctrlist.addHeader(textListOrderItem[language][10], "5%");//b.awal
        ctrlist.addHeader(textListOrderItem[language][11], "5%");//b.akhir
        ctrlist.addHeader(textListOrderItem[language][12], "5%");//b.selisih
    }
    if (privShowQtyPrice) {
        if (tranUsedPriceHpp == 0) {
            if (typeOfBusinessDetail == 2) {
                if (dispatchItemType == Material.MATERIAL_TYPE_EMAS || dispatchItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                    ctrlist.addHeader("HE Awal", "10%");//harga emas awal
                    ctrlist.addHeader("HE Akhir", "10%");//harga emas akhir (hpp)
                } else if (dispatchItemType == Material.MATERIAL_TYPE_BERLIAN) {
                    ctrlist.addHeader("Harga Beli Awal", "10%");//harga pokok awal
                    ctrlist.addHeader("Harga Beli Akhir", "10%");//harga pokok akhir (hpp)
                }
            } else {
            if(syspropHPP.equals("1")){
                ctrlist.addHeader(textListOrderItem[language][5], "10%");//hpp
            }            
            }            
        } else {

            ctrlist.addHeader(textListOrderItem[language][6], "10%");//harga jual
        }
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Oks/Batu Awal", "10%");//ongkos awal
            ctrlist.addHeader("Oks/Batu Akhir", "10%");//ongkos akhir
            if (dispatchItemType == Material.MATERIAL_TYPE_EMAS || dispatchItemType == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                ctrlist.addHeader("Total HP Awal", "10%");//total awal
                ctrlist.addHeader("Total HP Akhir", "10%");//total akhir
            } else if (dispatchItemType == Material.MATERIAL_TYPE_BERLIAN) {
                ctrlist.addHeader("HP Awal", "10%");//total awal
                ctrlist.addHeader("HP Akhir", "10%");//total akhir
            }            
        } else if (privPayment){
            ctrlist.addHeader(textListOrderItem[language][7], "10%");//total
        }
    }
    ctrlist.addHeader(textListOrderItem[language][9], "1%");//hapus

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0)
        start=0;
    
    String whereKsg = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " = '" + locationId + "'";
    Vector listKsg = PstKsg.list(0,0,whereKsg,PstKsg.fieldNames[PstKsg.FLD_CODE]);
    Vector vectKsgVal = new Vector(1,1);
    Vector vectKsgKey = new Vector(1,1);
    for(int i=0; i<listKsg.size(); i++){
        Ksg matKsg = (Ksg)listKsg.get(i);
        Location l = new Location();
        try {
            l = PstLocation.fetchExc(matKsg.getLocationId());
        } catch (Exception e) {
            
        }
        vectKsgKey.add(""+l.getCode()+" - "+matKsg.getCode());
        vectKsgVal.add(""+matKsg.getOID());
    }
    
    String whereKsgTo = "" + PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID] + " = '" + locationTo + "'";
    Vector listKsgTo = PstKsg.list(0,0,whereKsgTo,PstKsg.fieldNames[PstKsg.FLD_CODE]);
    Vector vectKsgValTo = new Vector(1,1);
    Vector vectKsgKeyTo = new Vector(1,1);
    for(int i=0; i<listKsgTo.size(); i++){
        Ksg matKsg = (Ksg)listKsgTo.get(i);
        Location l = new Location();
        try {
            l = PstLocation.fetchExc(matKsg.getLocationId());
        } catch (Exception e) {
            
        }
        vectKsgKeyTo.add(""+l.getCode()+" - "+matKsg.getCode());
        vectKsgValTo.add(""+matKsg.getOID());
    }
                                
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MatDispatchItem dfItem = (MatDispatchItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        rowx = new Vector();
        //added by dewok 2018 for jewelry
        MaterialDetail md = new MaterialDetail();
        Vector<MaterialDetail> listMatDetail = PstMaterialDetail.list(0, 0, "" + PstMaterialDetail.fieldNames[PstMaterialDetail.FLD_MATERIAL_ID] + "='"+dfItem.getMaterialId()+"'", "");
        Ksg ksg = new Ksg();
        Ksg ksgTo = new Ksg();
        Location locLast = new Location();
        Location locNew = new Location();
        try {
            ksg = PstKsg.fetchExc(dfItem.getGondolaId());
            ksgTo = PstKsg.fetchExc(dfItem.getGondolaToId());
            locLast = PstLocation.fetchExc(ksg.getLocationId());
            locNew = PstLocation.fetchExc(ksgTo.getLocationId());
            if (!listMatDetail.isEmpty()) {
                md = PstMaterialDetail.fetchExc(listMatDetail.get(0).getOID());
            }
        } catch (Exception e) {
            
        }     
        if(typeOfBusinessDetail == 2){
            mat.setName(SessMaterial.setItemNameForLitama(dfItem.getMaterialId()));
        }
        
        start = start + 1;
        if (dfItemId == dfItem.getOID()) index = i;

        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            //mencari stok yang tersedia
            String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfItem.getMaterialId();
            whereClause += " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;
            Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");
            MaterialStock objMaterialStock = new MaterialStock();
            if(vMaterialStock.size() > 0) {
             objMaterialStock = (MaterialStock)vMaterialStock.get(0);
            }
            rowx.add(""+start);
            if(useForRaditya.equals("0")){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+dfItem.getMaterialId()+"\">"
                        + "<input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+dfItem.getMaterialId()+"\">"
                        + "<input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"\" class=\"formElemen hiddenText\" readOnly></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>"
                        + "<div align=\"right\"><input type=\"hidden\" id=\"\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_AWAL]+"\" value=\""+String.format("%,.2f",dfItem.getHppTotalAwal())+"\" class=\"formElemen hiddenText\" readOnly></div>"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            
            }
            
            if(brandInUse==1){
                if(typeOfBusinessDetail != 2 && syspropEtalase.equals("1")){
                    rowx.add("<input type=\"text\" size=\"15\" name=\"matbrand\" value=\""+mat.getGondolaCode()+"\" class=\"hiddenText\"  readOnly>");
                }
            }
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\"  readOnly>");            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"
                        + "<input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"
                        + "<input type=\"hidden\" name=\"avbl_qty\" value=\""+objMaterialStock.getQty()+"\"></div>");
            } else {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"
                        + "<input type=\"hidden\" name=\"avbl_qty\" value=\""+objMaterialStock.getQty()+"\"></div>");
            }
            if(typeOfBusinessDetail == 2){
                rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID],"formElemen", null, ""+dfItem.getGondolaId(), vectKsgVal, vectKsgKey, ""));
                rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_TO_ID],"formElemen", null, ""+dfItem.getGondolaToId(), vectKsgValTo, vectKsgKeyTo, ""));
            }
            if(typeOfBusinessDetail != 2){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+dfItem.getUnitId()+"\">"
                        + "<input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\"  readOnly>");
            }
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_LAST]+"\" value=\""+String.format("%,.3f",dfItem.getBeratLast())+"\" class=\"formElemen hiddenText berat_last\" readOnly></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_CURRENT]+"\" value=\""+String.format("%,.3f",dfItem.getBeratCurrent())+"\" class=\"formElemen countSelisih berat_current\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_SELISIH]+"\" value=\""+String.format("%,.3f",dfItem.getBeratSelisih())+"\" class=\"formElemen hiddenText berat_selisih\" readOnly></div>");                
            }
            if(privShowQtyPrice){
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"hargaEmasAwal\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_AWAL]+"\" value=\""+String.format("%,.2f",dfItem.getHppAwal())+"\" class=\"formElemen hiddenText\"readOnly></div>");//he.awal
                }
            if(syspropHPP.equals("1")){
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"hargaEmasAkhir\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+String.format("%,.2f",dfItem.getHpp())+"\" class=\"formElemen countTotalHp\"></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"hargaEmasAkhir\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"\" class=\"formElemen countTotalHp\"></div>");
                }
            }
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"ongkosAwal\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS_AWAL]+"\" value=\""+String.format("%,.2f",dfItem.getOngkosAwal())+"\" class=\"hiddenText\" readOnly></div>");//ongkos awal
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"ongkos\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+String.format("%,.2f",dfItem.getOngkos())+"\" class=\"formElemen countTotalHp\"></div>");//ongkos
                }
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_AWAL]+"\" value=\""+String.format("%,.2f",dfItem.getHppTotalAwal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"totalHp\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+String.format("%,.2f",dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                } else if (privPayment){
                    if(useForRaditya.equals("0")){
                        rowx.add("<div align=\"right\"><input type=\"text\" id=\"totalHp\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"text\" id=\"totalHp\" size=\"10\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                    }
                }
            }else{
                rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                if (privPayment){
                    if(useForRaditya.equals("0")){
                        rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                    } else {
                        rowx.add("<div align=\"right\"><input type=\"hidden\" id=\"totalHp\" size=\"10\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"formElemen hiddenText\" readOnly></div>");
                    }
                }
            }
            rowx.add("");
       } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");
            if(brandInUse==1){
                if(typeOfBusinessDetail != 2 && syspropEtalase.equals("1")){
                    rowx.add(""+mat.getGondolaCode());
                }
            }
            rowx.add(mat.getName());            
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfItem.getOID();
                int cnt = PstDispatchStockCode.getCount(where);
                if(cnt<dfItem.getQty()){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty pengiriman");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");                
            }
            if(typeOfBusinessDetail == 2){
                rowx.add(locLast.getCode()+" - "+ksg.getCode());
                rowx.add(locNew.getCode()+" - "+ksgTo.getCode());
            }
            if(typeOfBusinessDetail != 2){
                rowx.add(unit.getCode());
            }            
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\">"+String.format("%,.3f",dfItem.getBeratLast())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.3f",dfItem.getBeratCurrent())+"</div>");
                rowx.add("<div align=\"right\">"+String.format("%,.3f",dfItem.getBeratSelisih())+"</div>");
            }
            if(privShowQtyPrice){
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getHppAwal())+"</div>");//he.awal
                }
            if(syspropHPP.equals("1")){
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getHpp())+"</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"</div>");
                }
                }
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getOngkosAwal())+"</div>");//ongkos awal
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getOngkos())+"</div>");//ongkos akhir
                }
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getHppTotalAwal())+"</div>");
                    rowx.add("<div align=\"right\">"+String.format("%,.2f",dfItem.getHppTotal())+"</div>");
                } else if (privPayment){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"</div>");
                }
            }
            // add by fitra 17-05-2014
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(dfItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
        }
        lstData.add(rowx);
    }
    rowx = new Vector();
    String styleQty="";
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {        
        if(iCommand==Command.ADD){
           styleQty="display:none"; 
        }
        rowx.add(""+(start+1));
        if(useForRaditya.equals("0")){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                 "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");

        }else{
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                 "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\"\" class=\"formElemen hiddenText\" readOnly></div>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\"\" class=\"formElemen hiddenText\" readOnly></div>"
                    + "<div align=\"right\"><input type=\"hidden\" id=\"\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_AWAL]+"\" value=\"\" class=\"formElemen hiddenText\" readOnly></div>"); // <a href=\"javascript:cmdCheck()\">CHK</a>

        }
        if(brandInUse==1){
            if(typeOfBusinessDetail != 2 && syspropEtalase.equals("1")){
                rowx.add("<input type=\"text\" size=\"15\" name=\"matbrand\" value=\"\" class=\"hiddenText\"  readOnly>");
            }
        }
        rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\" >&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");
        if(privShowQtyPrice){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"
                    +"<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        } else {
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"
                    +"<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");
        }
        if(typeOfBusinessDetail == 2){
            rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_ID],"formElemen", null, "", vectKsgVal, vectKsgKey, ""));
            rowx.add("" + ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_GONDOLA_TO_ID],"formElemen", null, "", vectKsgValTo, vectKsgKeyTo, ""));
        } 
        if(typeOfBusinessDetail != 2){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" class=\"hiddenText\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        }
        if(typeOfBusinessDetail == 2){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_LAST]+"\" value=\""+""+"\" class=\"formElemen hiddenText berat_last\" readOnly></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_CURRENT]+"\" value=\""+""+"\" class=\"formElemen countSelisih berat_current\"></div>");
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT_SELISIH]+"\" value=\""+""+"\" class=\"formElemen hiddenText berat_selisih\" readOnly></div>");
        }
        if(privShowQtyPrice){
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"right\"><input type=\"text\" id=\"hargaEmasAwal\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_AWAL]+"\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");//he.awal
            }
        if(syspropHPP.equals("1")){
            rowx.add("<div align=\"right\"><input type=\"text\" id=\"hargaEmasAkhir\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"formElemen countTotalHp\"></div>");   
        }
            if(typeOfBusinessDetail == 2){                
                rowx.add("<div align=\"right\"><input type=\"text\" id=\"ongkosAwal\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS_AWAL]+"\" value=\""+""+"\" class=\"formElemen countTotalHp\"></div>");//ongkos awal
                rowx.add("<div align=\"right\"><input type=\"text\" id=\"ongkos\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+""+"\" class=\"formElemen countTotalHp\"></div>");//ongkos
                rowx.add("<div align=\"right\"><input type=\"text\" id=\"\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL_AWAL]+"\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");//hpp total awal
            }
            if (privPayment){
                if(useForRaditya.equals("0")){
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"totalHp\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" id=\"totalHp\" size=\"10\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");
                }
                
            }
        }else{            
            rowx.add("<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");
            if (privPayment){
                if(useForRaditya.equals("0")){
                    rowx.add("<div align=\"right\"><input type=\"hidden\" id=\"totalHp\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"hidden\" id=\"totalHp\" size=\"10\" value=\""+""+"\" class=\"formElemen hiddenText\" readOnly></div>");
                }
                
            }   
        }
        
        rowx.add("");
        lstData.add(rowx);
    }
    list.add(ctrlist.draw());
    list.add(listError);
    return list;
}
%>

<%
/**
 * get approval status for create document
 */
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
/**
 * get request data from current form
 */
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");

int appCommand = FRMQueryString.requestInt(request,"approval_command");

long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");

long oidDispatchMaterialItem = FRMQueryString.requestLong(request,"hidden_dispatch_item_id");
long oidReceiveMaterialItem = FRMQueryString.requestLong(request,"hidden_receive_id");

long timemls = System.currentTimeMillis();

long oidReceiveId = FRMQueryString.requestLong(request,"hidden_receive_item_id");
String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE_TRANSFER");
String syspropHPP = PstSystemProperty.getValueByName("SHOW_HPP_TRANSFER");
String syspropTotal = PstSystemProperty.getValueByName("SHOW_TOTAL_TRANSFER");


/**

 * initialization of some identifier

 */

int iErrCode = FRMMessage.NONE;

String msgString = "";



/**

 * purchasing pr code and title

 */

String dfCode = ""; //i_pstDocType.getDocCode(docType);

String dfTitle = "Transfer Barang"; //i_pstDocType.getDocTitle(docType);

String dfItemTitle = dfTitle + " Item";



/**

 * purchasing pr code and title

 */

String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));





/**

 * process on df main

 */

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

iErrCode = ctrlMatDispatch.action(Command.EDIT,oidDispatchMaterial);

FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

MatDispatch df = ctrlMatDispatch.getMatDispatch();



/**

 * check if document already closed or not

 */

boolean documentClosed = false;

if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED) {

    documentClosed = true;

}



/**

 * check if document may modified or not

 */

boolean privManageData = true;



ControlLine ctrLine = new ControlLine();

CtrlMatDispatchItem ctrlMatDispatchItem = new CtrlMatDispatchItem(request);

ctrlMatDispatchItem.setLanguage(SESS_LANGUAGE);

//by dyas
//tambah userName dan userId
iErrCode = ctrlMatDispatchItem.action(iCommand,oidDispatchMaterialItem,oidDispatchMaterial, userName, userId);

FrmMatDispatchItem frmMatDispatchItem = ctrlMatDispatchItem.getForm();

MatDispatchItem dfItem = ctrlMatDispatchItem.getMatDispatchItem();

msgString = ctrlMatDispatchItem.getMessage();


String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;

String orderClauseItem = "";

int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);

int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {

    startItem = ctrlMatDispatchItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);

}

/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */

if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {

    iCommand = Command.ADD;

    oidDispatchMaterialItem = 0;

    /** agar form add item tampil pada list paling akhir */

    startItem = ctrlMatDispatchItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);

}

String order=" DFI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
if (typeOfBusinessDetail == 2) {
    order = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
}
Vector listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial, order);

if(listMatDispatchItem.size()<1 && startItem>0) {

    if(vectSizeItem-recordToGetItem > recordToGetItem) {

        startItem = startItem - recordToGetItem;

    } else {

        startItem = 0 ;

        iCommand = Command.FIRST;

        prevCommand = Command.FIRST;

    }

    listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);

}



%>



<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->

<head>

<!-- #BeginEditable "doctitle" -->

<title>Dimata - ProChain POS</title>

<script language="JavaScript">

<!--

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

    function main(oid, comm) {

        document.frm_matdispatch.command.value = comm;

        document.frm_matdispatch.hidden_dispatch_id.value = oid;

        document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

        document.frm_matdispatch.submit();

    }



    function gostock(oid) {

        document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

        document.frm_matdispatch.hidden_dispatch_item_id.value = oid;

        document.frm_matdispatch.action = "df_stockcode.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdAdd()

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = "0";

        document.frm_matdispatch.command.value = "<%=Command.ADD%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        if (compareDateForAdd() == true)
            document.frm_matdispatch.submit();

    }

    function cmdAddItemReceive()

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = "0";

        document.frm_matdispatch.command.value = "<%=Command.ADD%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.action = "df_stock_material_receive_search.jsp";

        if (compareDateForAdd() == true)
            document.frm_matdispatch.submit();

    }




    function cmdEdit(oidDispatchMaterialItem)

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = oidDispatchMaterialItem;

        document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdAsk(oidDispatchMaterialItem)

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = oidDispatchMaterialItem;

        document.frm_matdispatch.command.value = "<%=Command.ASK%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }

    function printForm()

    {

        var typePrint = document.frm_matdispatch.type_print_tranfer.value;

        //window.open("<%=approot%>/servlet/com.dimata.posbo.report.TransferPrintPDF?hidden_dispatch_id=<%=oidDispatchMaterial%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&brandinuse=<%=brandInUse%>");

        window.open("df_stock_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer=" + typePrint + "&timemls=<%=timemls%>", "pireport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");

    }

    function cmdSave() {

        var avblQty = parseFloat(document.frm_matdispatch.avbl_qty.value);

        var transferQty = parseFloat(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value);



<%!

/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */

static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");

static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;



%>

        if (transferQty <= 0.000) {

            alert("Quantity < 0");

        } else {

            <% if(calculateQtyNull) { %>

            document.frm_matdispatch.command.value = "<%=Command.SAVE%>";

            document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

            document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                <%if(typeOfBusinessDetail == 2){%>
            var etalaseAwal = document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_ID]%>.value;
            var etalaseBaru = document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_TO_ID]%>.value;
            document.frm_matdispatch.submit();

                <%} else {%>
            document.frm_matdispatch.submit();
                <%}%>

            <%} else { %>

            if (transferQty <= avblQty) {

                document.frm_matdispatch.command.value = "<%=Command.SAVE%>";

                document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

                document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

                document.frm_matdispatch.submit();

            } else {

                alert("<%=textListGlobal[SESS_LANGUAGE][2]%> (" + avblQty + " " + document.frm_matdispatch.matUnit.value + ")");

            }

            <%}%>

        }

    }



    function cmdConfirmDelete(oidDispatchMaterialItem)

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = oidDispatchMaterialItem;

        document.frm_matdispatch.command.value = "<%=Command.DELETE%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.approval_command.value = "<%=Command.DELETE%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }

// add by fitra
    function cmdNewDelete(oid) {
        var msg;
        msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
        var agree = confirm(msg);
        if (agree)
            return cmdConfirmDelete(oid);
        else
            return cmdEdit(oid);
    }



    function cmdCancel(oidDispatchMaterialItem)

    {

        document.frm_matdispatch.hidden_dispatch_item_id.value = oidDispatchMaterialItem;

        document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

        document.frm_matdispatch.prev_command.value = "<%=prevCommand%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdBack()

    {

        document.frm_matdispatch.command.value = "<%=Command.EDIT%>";

        document.frm_matdispatch.start_item.value = 0;

        document.frm_matdispatch.action = "df_stock_wh_material_edit.jsp";

        document.frm_matdispatch.submit();

    }



    var winSrcMaterial;

    function cmdCheck() {
    <%if(typeOfBusinessDetail == 2){%>
        var strvalue = "dfwhdosearch.jsp?command=<%=Command.FIRST%>" +
                "&location_id=<%=df.getLocationId()%>" +
                "&location_id_penerima=<%=df.getDispatchTo()%>" +
                "&transfer_date=<%=df.getDispatchDate()%>" +
                "&hidden_dispatch_id=<%=oidDispatchMaterial%>" +
                "&mat_code=" + document.frm_matdispatch.matCode.value +
                "&dispatch_item_type=" + document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE]%>.value +
                "&etalase_id=" + document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_ID]%>.value +
                "&txt_materialname=" + document.frm_matdispatch.matItem.value;
        winSrcMaterial = window.open(strvalue, "Stock_Material", "height=500,width=800,left=300,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
        if (window.focus) {
            winSrcMaterial.focus();
        }

    <%} else {%>

        var strvalue = "dfwhdosearch.jsp?command=<%=Command.FIRST%>" +
                "&location_id=<%=df.getLocationId()%>" +
                "&location_id_penerima=<%=df.getDispatchTo()%>" +
                "&transfer_date=<%=df.getDispatchDate()%>" +
                "&hidden_dispatch_id=<%=oidDispatchMaterial%>" +
                "&mat_code=" + document.frm_matdispatch.matCode.value +
                "&txt_materialname=" + document.frm_matdispatch.matItem.value;
        winSrcMaterial = window.open(strvalue, "Stock_Material", "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
        if (window.focus) {
            winSrcMaterial.focus();
        }
    <%}%>
    }



    function keyDownCheck(e) {

        var trap = document.frm_matdispatch.trap.value;
        if (e.keyCode == 13 && trap == 0) {
            document.frm_matdispatch.trap.value = "1";
        }


        // add By fitra
        if (e.keyCode == 13 && trap == "0" && document.frm_matdispatch.matItem.value == "") {
            document.frm_matdispatch.trap.value = "0";
            cmdCheck();
        }
        if (e.keyCode == 13 && trap == "1") {
            document.frm_matdispatch.trap.value = "0";
            cmdCheck();
        }
        if (e.keyCode == 27) {
            //alert("sa");
            document.frm_matdispatch.matCode.focus();
            document.frm_matdispatch.txt_materialname.value = "";
        }


    }



    function changeFocus(element) {

        if (element.name == "matCode") {

            document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();

        } else if (element.name == "<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>") {

            cmdSave();

        } else {

            cmdSave();

        }

    }



    function cntTotal(element, evt)
    {

        var qty = cleanNumberInt(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
        var price = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value, guiDigitGroup, ',');
        if (qty < 0.0000) {
            document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value = 0;
            return;
        }

        if (price == "") {
            price = 0;
        }

        if (!(isNaN(qty)) && (qty != '0'))
        {
            var amount = price * qty;
            if (isNaN(amount)) {
                amount = "0";
            }
            document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
            document.getElementById("totalHp").value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace); 
        } else {
            document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();
        }

        if (evt.keyCode == 13) {
            changeFocus(element);
        }

    }



    function cmdListFirst()

    {

        document.frm_matdispatch.command.value = "<%=Command.FIRST%>";

        document.frm_matdispatch.prev_command.value = "<%=Command.FIRST%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdListPrev()

    {

        document.frm_matdispatch.command.value = "<%=Command.PREV%>";

        document.frm_matdispatch.prev_command.value = "<%=Command.PREV%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdListNext()

    {

        document.frm_matdispatch.command.value = "<%=Command.NEXT%>";

        document.frm_matdispatch.prev_command.value = "<%=Command.NEXT%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdListLast()

    {

        document.frm_matdispatch.command.value = "<%=Command.LAST%>";

        document.frm_matdispatch.prev_command.value = "<%=Command.LAST%>";

        document.frm_matdispatch.action = "df_stock_wh_material_item.jsp";

        document.frm_matdispatch.submit();

    }



    function cmdBackList()

    {

        document.frm_matdispatch.command.value = "<%=Command.FIRST%>";

        document.frm_matdispatch.action = "df_stock_wh_material_list.jsp";

        document.frm_matdispatch.submit();

    }

//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------







//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

    function MM_swapImgRestore() { //v3.0

        var i, x, a = document.MM_sr;
        for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
            x.src = x.oSrc;

    }



    function MM_preloadImages() { //v3.0

        var d = document;
        if (d.images) {
            if (!d.MM_p)
                d.MM_p = new Array();

            var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
            for (i = 0; i < a.length; i++)
                if (a[i].indexOf("#") != 0) {
                    d.MM_p[j] = new Image;
                    d.MM_p[j++].src = a[i];
                }
        }

    }



    function MM_swapImage() { //v3.0

        var i, j = 0, x, a = MM_swapImage.arguments;
        document.MM_sr = new Array;
        for (i = 0; i < (a.length - 2); i += 3)
            if ((x = MM_findObj(a[i])) != null) {
                document.MM_sr[j++] = x;
                if (!x.oSrc)
                    x.oSrc = x.src;
                x.src = a[i + 2];
            }

    }

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------



    function MM_findObj(n, d) { //v4.01

        var p, i, x;
        if (!d)
            d = document;
        if ((p = n.indexOf("?")) > 0 && parent.frames.length) {

            d = parent.frames[n.substring(p + 1)].document;
            n = n.substring(0, p);
        }

        if (!(x = d[n]) && d.all)
            x = d.all[n];
        for (i = 0; !x && i < d.forms.length; i++)
            x = d.forms[i][n];

        for (i = 0; !x && d.layers && i < d.layers.length; i++)
            x = MM_findObj(n, d.layers[i].document);

        if (!x && d.getElementById)
            x = d.getElementById(n);
        return x;

    }

//-->

</script>

<!-- #EndEditable -->

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../../styles/main.css" type="text/css">

<!-- #EndEditable -->

<!-- #BeginEditable "stylestab" -->

<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

<!-- #EndEditable -->

<!-- #BeginEditable "headerscript" -->

<SCRIPT language=JavaScript>

    function hideObjectForMarketing() {

    }



    function hideObjectForWarehouse() {

    }



    function hideObjectForProduction() {

    }



    function hideObjectForPurchasing() {

    }



    function hideObjectForAccounting() {

    }



    function hideObjectForHRD() {

    }



    function hideObjectForGallery() {

    }



    function hideObjectForMasterData() {

    }



</SCRIPT>

<!-- #EndEditable --> 


<%--autocomplate add by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />

</head> 



<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    

<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>

  <tr> 

    <td valign="top" align="left"> 

      <table width="100%" border="0" cellspacing="0" cellpadding="0">  

        <tr> 

          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->

            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>

        </tr>

        <tr> 

          <td><!-- #BeginEditable "content" -->

            <form name="frm_matdispatch" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveId%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterialItem%>">
              <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">
              <input type="hidden" name="<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">

              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="trap" value="">

              <table width="100%" cellpadding="1" cellspacing="0">

                <tr align="center">

                  <td colspan="3">

                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="7%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="20%">: <b>

                          <%

                          if(df.getDispatchCode()!="" && iErrCode==0) {

                                out.println(df.getDispatchCode());

                          } else {

                                out.println("");

                          }

                          %>

                        </b></td>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="20%">:

                        <%

                        Vector obj_locationid = new Vector(1,1);

                        Vector val_locationid = new Vector(1,1);

                        Vector key_locationid = new Vector(1,1);

                        //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE

                        Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);

                        for(int d=0;d<vt_loc.size();d++) {

                            Location loc = (Location)vt_loc.get(d);

                            val_locationid.add(""+loc.getOID()+"");

                            key_locationid.add(loc.getName());

                        }

                        String select_locationid = ""+df.getLocationId(); //selected on combo box

			%>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="1%">:</td>
                        <td width="20%" rowspan="1" align="" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>

                      </tr>

                      <tr>

                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate()==null) ? new Date() : df.getDispatchDate(), 1, -5, "formElemen", "")%></td>

                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>

                        <td>:

                        <%

                        Vector obj_locationid1 = new Vector(1,1);

                        Vector val_locationid1 = new Vector(1,1);

                        Vector key_locationid1 = new Vector(1,1);

                        String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;

                        String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);

                        Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);

                        for(int d = 0; d < vt_loc1.size(); d++) {

                            Location loc1 = (Location)vt_loc1.get(d);

                            val_locationid1.add(""+loc1.getOID()+"");

                            key_locationid1.add(loc1.getName());

                        }

                        String select_locationid1 = ""+df.getDispatchTo(); //selected on combo box

                        %>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>

                        <%if(typeOfBusinessDetail == 2){%>
                        <td>Tipe Item Transfer</td>
                        <td>:</td>
                        <td>
                            <%
                                Vector val_itemType = new Vector(1,1);
                                Vector key_itemType = new Vector(1,1);
                                for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                    if (main_material == Material.MATERIAL_TYPE_GENERAL) {continue;}
                                    key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[main_material]);
                                    val_itemType.add("" + main_material);
                                }
                            %>
                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE],null,""+df.getDispatchItemType(),val_itemType,key_itemType,"disabled","formElemen")%>
                        </td>
                        <%}%>

                      </tr>

                      <tr>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

                      </tr>

                    </table>

                   </td>

                </tr>

                <tr>

                  <td valign="top">

                    <table width="100%" cellpadding="1" cellspacing="1">

                      <tr>

                        <td colspan="3" >

                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >

                            <tr align="left" valign="top">

                              <%

                              Vector listError = new Vector(1,1);

                              try {

                              %>

                              <td height="22" valign="middle" colspan="3">

                              <%

                              Vector list = drawListDfItem(SESS_LANGUAGE,iCommand,frmMatDispatchItem, dfItem,listMatDispatchItem,oidDispatchMaterialItem,startItem,df.getInvoiceSupplier(),tranUsedPriceHpp,df.getLocationId(),brandInUse,privShowQtyPrice, approot, typeOfBusinessDetail, df.getDispatchItemType(), df.getDispatchTo(), syspropEtalase, syspropTotal, syspropHPP, privPayment);

                              out.println(""+list.get(0));

                              listError = (Vector)list.get(1);

                              %>

                              </td>

                              <%

                              } catch(Exception e) {

                                  System.out.println(e);

                              }

                              %>

                            </tr>

                            <tr align="left" valign="top">

                              <td height="8" align="left" colspan="3" class="command">

                                <span class="command">

                                <%

                                int cmd = 0;

                                if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand == Command.NEXT || iCommand==Command.LAST){

                                    cmd =iCommand;

                                }else{

                                    if(iCommand == Command.NONE || prevCommand == Command.NONE)

                                        cmd = Command.FIRST;

                                    else

                                        cmd =prevCommand;

                                }

                                ctrLine.setLocationImg(approot+"/images");

                                ctrLine.initDefault();

                                out.println(ctrLine.drawImageListLimit(cmd,vectSizeItem,startItem,recordToGetItem));

                                %>

                                </span> </td>

                            </tr>

                            <tr align="left" valign="top">

                              <td height="22" colspan="3" valign="middle" class="errfont">

                              <%

                              for(int k=0;k<listError.size();k++){

                                    if(k==0)

                                        out.println(listError.get(k)+"<br>");

                                    else

                                        out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");

                              }

                              %></td>

                            </tr>

                            <tr align="left" valign="top">

                              <td height="22" colspan="3" valign="middle">

                                <%

                                ctrLine.setLocationImg(approot+"/images");

                                

                                // set image alternative caption

                                ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));

                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));

                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");

                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));

                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));

                                

                                ctrLine.initDefault();

                                ctrLine.setTableWidth("65%");

                                String scomDel = "javascript:cmdAsk('"+oidDispatchMaterialItem+"')";

                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidDispatchMaterialItem+"')";

                                String scancel = "javascript:cmdEdit('"+oidDispatchMaterialItem+"')";

                                ctrLine.setCommandStyle("command");

                                ctrLine.setColCommStyle("command");

                                

                                // set command caption

                                ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));

                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));

                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");

                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));

                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_DELETE,true));

                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));

                                

                                

                                if (privDelete){

                                    ctrLine.setConfirmDelCommand(sconDelCom);

                                    ctrLine.setDeleteCommand(scomDel);

                                    ctrLine.setEditCommand(scancel);

                                }else{

                                    ctrLine.setConfirmDelCaption("");

                                    ctrLine.setDeleteCaption("");

                                    ctrLine.setEditCaption("");

                                }

                                

                                if(privAdd == false  && privUpdate == false){

                                    ctrLine.setSaveCaption("");

                                }

                                

                                if (privAdd == false){

                                    ctrLine.setAddCaption("");

                                }

                                

                                String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);

                                if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){

				%>

                                <table width="70%" border="0" cellspacing="2" cellpadding="0">

                                  <tr>

                                    <% if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>

                                    <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>

                                    <td width="20%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                                    <td width="6%"><a href="javascript:cmdAddItemReceive()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>

                                    <td width="20%"><a href="javascript:cmdAddItemReceive()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item Receive",ctrLine.CMD_ADD,true)%></a></td>

                                    <% } %>

                                    <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnBackOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>

                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%></a></td>



                                     <td><select name="type_print_tranfer">

                                          <option value="0">Price Cost</option>

                                          <option value="1">Sell Price</option>

                                                                  <option value="2">No Price</option>

                                        </select>

                                    </td>


                                  </tr>
                                  <tr>
                                      <td width="6%"></td>

                                      <td width="47%"></td>

                                      <td width="6%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="Cetak Transfer"></a></td>

                                      <td width="47%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" >Cetak Transfer</a></td>

                                  </tr>
                                </table>

                              <%

                                }else{

                                    out.println(strDrawImage);

                                }

                              %>

                              </td>
                              
                            </tr>
                             
                          </table>

                        </td>

                      </tr>

                      <tr>

                        <td colspan="3">

                        </td>

                      </tr>

                    </table>

                  </td>

                </tr>
                
              </table>

            </form>

            <script language="JavaScript">
                document.frm_matdispatch.matCode.focus();

            </script>

            <!-- #EndEditable --></td> 

        </tr> 

      </table>

    </td>

  </tr>

  <tr> 

    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->

      <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>

      <!-- #EndEditable --> </td>

  </tr>

</table>

</body>
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
    //document.frm_matdispatch.matItem.focus();
    document.frm_matdispatch.matCode.focus();
    <% } %>
</script>


<script language="JavaScript">

    // add By Fitra
    var trap = document.frm_matdispatch.trap.value;
    document.frm_matdispatch.trap.value = "0";
    //document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate add by fitora --%>
<script>
    jQuery(function () {
        $("#txt_materialname").autocomplete("list.jsp");
        $('.countSelisih').keyup(function () {
            var itemType = document.frm_matdispatch.<%=FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE]%>.value;
            var last = cleanNumberInt($('.berat_last').val(), guiDigitGroup);
            var current = cleanNumberInt($('.berat_current').val(), guiDigitGroup);
            $('.berat_selisih').val(+current - +last);
            if (itemType === "<%=Material.MATERIAL_TYPE_EMAS%>" || itemType === "<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>") {
                var hargaEmasAwal = cleanNumberInt($('#hargaEmasAwal').val(), guiDigitGroup);
                var hargaEmasAkhir = +hargaEmasAwal / +last * +current;
                $('#hargaEmasAkhir').val(hargaEmasAkhir);
                var ongkos = cleanNumberInt($('#ongkos').val(), guiDigitGroup);
                var totalHp = +hargaEmasAkhir + +ongkos;
                $('#totalHp').val(totalHp);
            }
        });

        $('.countTotalHp').keyup(function () {
            var hargaEmasAkhir = cleanNumberInt($('#hargaEmasAkhir').val(), guiDigitGroup);
            var ongkos = cleanNumberInt($('#ongkos').val(), guiDigitGroup);
            var totalHp = +hargaEmasAkhir + +ongkos;
            $('#totalHp').val(totalHp);
        });
    });
</script>

<!-- #EndTemplate --></html>




