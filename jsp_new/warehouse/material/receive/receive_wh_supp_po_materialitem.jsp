<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrderItem"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.warehouse.PstReceiveStockCode,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceive,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    
%>
<!-- Jsp Block -->
<%!
static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang ...","Verifikasi","Verifikasi Sukses","Maksimal Qty adalah"},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item ...","Verification","Verification Success","Maximum Qty is"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor PO","Nota Supplier","Total PPN","Mata Uang","Gudang","Terima Barang","Dengan Faktur Order","Terms","Days","Rate"},
    {"Number","Location","Date","Supplier","Status","Remark","PO Number","Supplier Invoice","Total VAT","Currency","Warehouse","Receive","With Purchase Order","Terms","Days","Rate"}
};


/* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
    //{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost"}
//};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga/Stok","Ongkos Kirim","Mata Uang","Qty@stock","Total Beli","Diskon Terakhir %",//10
    "Diskon1 %","Diskon2 %","Discount Nominal","Qty Entri","Unit Order","Harga Total", "Hapus","Bonus"},//17
  {"No","Code","Name","Expired Date","Unit","Price/Stock","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
   "Discount2 %","Disc. Nominal","Qty Entri","Unit Order","Total Price", "Delete","Bonus"}
};
public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};


/* this constant used to list text of listHeader */
public static final String textListPOItem[][] = {
    {"No","Sku","Nama Barang","Unit Stok","Hrg/Unit Order","Mata Uang","Qty@unitStock","Total Beli","Unit Order"},
    {"No","Code","Name","Unit","Buy Price/Unit Order","Currency","Quantity","Sub Total","Unit Request"}
};
/**
* this method used to maintain poMaterialList
*/
public Vector drawListRetItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start, boolean privShowQtyPrice,double exchangeRate, String readOnlyQty, String typeOfBusiness, long oidCurrency, String approot, String syspropPenerimaanHpp, String useForRaditya) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"5%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"20%");
    if(bEnableExpiredDate){
     ctrlist.addHeader(textListOrderItem[language][3],"7%");
    }
    ctrlist.addHeader(textListOrderItem[language][14],"5%");
    ctrlist.addHeader(textListOrderItem[language][15],"5%");
    ctrlist.addHeader("Unit Stock","8%");
if(useForRaditya.equals("1")){}else{
    if(syspropPenerimaanHpp.equals("1")){
    ctrlist.addHeader("Harga Pokok","9%");}
    if(privShowQtyPrice){
        //ctrlist.addHeader("Harga Beli","5%");
        ctrlist.addHeader(textListOrderItem[language][16],"8%");
        ctrlist.addHeader(textListOrderItem[language][5],"8%");
        ctrlist.addHeader(textListOrderItem[language][11],"5%");
        ctrlist.addHeader(textListOrderItem[language][12],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"8%");
        ctrlist.addHeader(textListOrderItem[language][6],"8%");
    }
}
    ctrlist.addHeader(textListOrderItem[language][8],"9%");
if(useForRaditya.equals("1")){}else{
    ctrlist.addHeader(textListOrderItem[language][18],"9%");
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][9],"10%");
    }
}
    
    ctrlist.addHeader(textListOrderItem[language][17],"10%");
    /**
     * add opie 28-06-2012 untuk konversi
     */
    String whereUnit = "";
    Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
    Vector index_value = new Vector(1,1);
    Vector index_key = new Vector(1,1);
    index_key.add("-");
    index_value.add("0");
    for(int i=0;i<listBuyUnit.size();i++){
        Unit mateUnit = (Unit)listBuyUnit.get(i);
        index_key.add(mateUnit.getCode());
        index_value.add(""+mateUnit.getOID());
    }

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start < 0) {
        start=0;
    }
    
    int priceReadOnly =Integer.parseInt(PstSystemProperty.getValueByName("PRICE_RECEIVING_READONLY"));
    String read="";
    if(priceReadOnly==1){
         read="readonly";
    }


    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        
        rowx = new Vector();
        start = start + 1;
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        if (recItemId == recItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            if(useForRaditya.equals("0")){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            }else{
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" "+read+" class='formElemen changeHarga' onkeyup=\"javascript:cntTotal(this, event)\" ><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\""+read+"  class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" "+read+" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"harga_pokok\" value=\"" + FRMHandler.userFormatStringDecimal(mat.getAveragePrice()) + "\" class=\"\" readOnly></div>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"\" class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                }else{
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit"+mat.getCurrBuyPrice()+"]</a>");
                }
            }else{
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            }

            if(bEnableExpiredDate){
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate()==null) ? new Date() : recItem.getExpiredDate(), 1, -5, "formElemen", ""));
            }
            //add opie-eyek 20140108 untuk konversi satuan
            if(readOnlyQty=="readonly"){
              rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" "+readOnlyQty+">");
            }else{
              rowx.add("<div align=\"center\"><input type='text' size='7' name='"+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"' value='"+recItem.getQtyEntry()+"' class='formElemen changeQtyPo' onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");

                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
if(useForRaditya.equals("0")){
            if(privShowQtyPrice){
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" readonly='readonly' size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" ></div></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" "+read+" class='formElemen changeHarga' onkeyup=\"javascript:cntTotal(this, event)\" ><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" "+read+" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\""+read+"  class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" "+read+" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }

                if(syspropPenerimaanHpp.equals("1")){
                 rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"harga_pokok\" value=\"" + FRMHandler.userFormatStringDecimal(mat.getAveragePrice()) + "\" class=\"\" readOnly></div>");
                }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\""+readOnlyQty+"></div>" +
                    "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\">");
            
            rowx.add("<div align=\"left\"><input type=\"checkbox\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"1\">Bonus</div>");
            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"\" class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
}
if(useForRaditya.equals("1")){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"left\"></div>");
}
        } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            
            //rowx.add(mat.getName());
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add(mat.getName() +"<a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                }else{
                    rowx.add(mat.getName() +"<a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit]</a>");
                }
            }else{
                rowx.add(mat.getName());
            }
            
            if(bEnableExpiredDate){
                rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            rowx.add("<div align=\"center\">"+recItem.getQtyEntry()+"</div>");
            rowx.add("<div align=\"center\">"+untiKonv.getCode()+"</div>");
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
if(useForRaditya.equals("0")){
            if(syspropPenerimaanHpp.equals("1")){
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getAveragePrice()) + "</div>");
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);
                
                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"left\"></div>");
            
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"left\"></div>");
            
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</div>");
            }
}
if(useForRaditya.equals("1")){

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
}

            // add by fitra 17-05-2014
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
        }
        lstData.add(rowx);
    }
    
    rowx = new Vector();
    if(readOnlyQty=="readonly"){
        
    }else{
        if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
        rowx.add(""+(start+1));
        if(useForRaditya.equals("0")){
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                "\"><input indextab=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onkeydown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        }else{
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                "\"><input indextab=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onkeydown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        }
        rowx.add("<input type=\"text\" size=\"13\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        if(bEnableExpiredDate){
                 rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 1, -5, "formElemen", ""));
        }
        //add opie-eyek 20140108 untuk konversi satuan
        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value='' class='formElemen changeQtyPo' onkeyup=\"javascript:change(this.value)\"></div>");
        rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
if(useForRaditya.equals("1")){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>"
                + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" "+read+" value=\""+""+"\" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" "+read+" value=\""+""+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\""+read+" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                  "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" "+read+" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly>"
                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
}else{        
rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>"); 
}               
if(useForRaditya.equals("1")){}else{
if(syspropPenerimaanHpp.equals("1")){
         rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"harga_pokok\" value=\""+ "" +"\" class=\"hiddenText\" readOnly></div>");
        }
        if(privShowQtyPrice){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" "+read+" value=\""+""+"\" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"></div>");
            rowx.add("<div align=\"right\"><input indextab=\"2\" type=\"text\" readonly='readonly' size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" "+read+" value=\""+""+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">");
            rowx.add("<div align=\"right\"><input indextab=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"right\"><input indextab=\"4\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"right\"><input indextab=\"5\" type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"right\"><input indextab=\"6\" type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
        }else{
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>"
                + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" "+read+" value=\""+""+"\" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" "+read+" value=\""+""+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" "+read+" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\""+read+" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                  "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" "+read+" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly>");
        }

}
        rowx.add("<div align=\"right\"><input indextab=\"7\" type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>" +
                "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\"\">");

if(useForRaditya.equals("0")){
if(privShowQtyPrice){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
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

public Vector drawListRetBonusItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start, boolean privShowQtyPrice,double exchangeRate, String readOnlyQty, String typeOfBusiness, long oidCurrency, String approot, String syspropPenerimaanHpp) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"5%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"20%");
    if(bEnableExpiredDate){
     ctrlist.addHeader(textListOrderItem[language][3],"7%");
    }
    ctrlist.addHeader(textListOrderItem[language][14],"5%");
    ctrlist.addHeader(textListOrderItem[language][15],"5%");
    ctrlist.addHeader("Unit Stock","8%");
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][16],"5%");
        ctrlist.addHeader(textListOrderItem[language][5],"8%");
        ctrlist.addHeader(textListOrderItem[language][11],"5%");
        ctrlist.addHeader(textListOrderItem[language][12],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"8%");
        ctrlist.addHeader(textListOrderItem[language][6],"8%");
    }
    ctrlist.addHeader(textListOrderItem[language][8],"9%");
    ctrlist.addHeader(textListOrderItem[language][18],"9%");
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][9],"10%");
    }
    
    ctrlist.addHeader(textListOrderItem[language][17],"10%");
    /**
     * add opie 28-06-2012 untuk konversi
     */
    String whereUnit = "";
    Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
    Vector index_value = new Vector(1,1);
    Vector index_key = new Vector(1,1);
    index_key.add("-");
    index_value.add("0");
    for(int i=0;i<listBuyUnit.size();i++){
        Unit mateUnit = (Unit)listBuyUnit.get(i);
        index_key.add(mateUnit.getCode());
        index_value.add(""+mateUnit.getOID());
    }

    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start < 0) {
        start=0;
    }
    
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        
        rowx = new Vector();
        start = start + 1;
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        if (recItemId == recItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                }else{
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit"+mat.getCurrBuyPrice()+"]</a>");
                }
            }else{
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            }

            if(bEnableExpiredDate){
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate()==null) ? new Date() : recItem.getExpiredDate(), 1, -5, "formElemen", ""));
            }
            //add opie-eyek 20140108 untuk konversi satuan
            if(readOnlyQty=="readonly"){
              rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" "+readOnlyQty+">");
            }else{
              rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class='formElemen changeQtyPo' onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");

            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" readonly='readonly' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" ></div></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" ><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemenR\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\""+readOnlyQty+"></div>" +
                    "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\">");
            rowx.add("<div align=\"left\"><input type=\"checkbox\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" checked value=\"1\">Bonus</div>");
            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"\" class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
        } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add(mat.getName() +"<a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'>[Edit]</font></a>");
                }else{
                    rowx.add(mat.getName() +"<a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit]</a>");
                }
            }else{
                rowx.add(mat.getName());
            }
            
            if(bEnableExpiredDate){
                rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            rowx.add("<div align=\"center\">"+recItem.getQtyEntry()+"</div>");
            rowx.add("<div align=\"center\">"+untiKonv.getCode()+"</div>");
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);
                
                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"left\">Bonus</div>");
            
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"left\">Bonus</div>");
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</div>");
            }
            // add by fitra 17-05-2014
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
        }
        lstData.add(rowx);
    }
    list.add(ctrlist.draw());
    list.add(listError);
    return list;
}

public String drawListMaterial(int language,Vector objectClass,int start, boolean privShowQtyPrice, double exchangeRate,FrmMatReceiveItem frmObject, long oidCurrency, String useForRadtya) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListOrderItem[language][0],"5%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"20%");
        if(bEnableExpiredDate){
         ctrlist.addHeader(textListOrderItem[language][3],"7%");
        }
        ctrlist.addHeader(textListOrderItem[language][14],"5%");
        ctrlist.addHeader(textListOrderItem[language][15],"5%");
        ctrlist.addHeader("Unit Stock","8%");

  if(useForRadtya.equals("0")){
        if(privShowQtyPrice){
            ctrlist.addHeader(textListOrderItem[language][16],"5%");
            ctrlist.addHeader(textListOrderItem[language][5],"8%");
            ctrlist.addHeader(textListOrderItem[language][11],"5%");
            ctrlist.addHeader(textListOrderItem[language][12],"5%");
            ctrlist.addHeader(textListOrderItem[language][13],"8%");
            ctrlist.addHeader(textListOrderItem[language][6],"8%");
        }
}
        ctrlist.addHeader(textListOrderItem[language][8],"9%");
  if(useForRadtya.equals("0")){
         ctrlist.addHeader(textListOrderItem[language][18],"9%");
        if(privShowQtyPrice){
            ctrlist.addHeader(textListOrderItem[language][9],"10%");
        }
    }
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        
        if(start<0) start = 0;
        
        /**
         * add opie 28-06-2012 untuk konversi
         */
        String whereUnit = "";
        Vector listBuyUnit = PstUnit.list(0,1000,whereUnit,"");
        Vector index_value = new Vector(1,1);
        Vector index_key = new Vector(1,1);
        index_key.add("-");
        index_value.add("0");
        for(int i=0;i<listBuyUnit.size();i++){
            Unit mateUnit = (Unit)listBuyUnit.get(i);
            index_key.add(mateUnit.getCode());
            index_value.add(""+mateUnit.getOID());
        }
        
        int priceReadOnly =Integer.parseInt(PstSystemProperty.getValueByName("PRICE_RECEIVING_READONLY"));
        String read="";
        if(priceReadOnly==1){
             read="readonly";
        }


        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            MatCurrency matCurrency = (MatCurrency)temp.get(3);
            Vector rowx = new Vector();
            start = start + 1;
            Unit unitRequest = new Unit();
            try{
            unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
                                  }catch(Exception e){}
            
            double sisa = poItem.getQuantity() - poItem.getResiduQty();
            
            rowx.add(""+(start));
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"_"+i+"\" value=\""+mat.getOID()+""+
                    "\"><input indextab=\"1\" type=\"text\" size=\"30\" name=\"matCode\" value=\""+mat.getSku()+"\" "+read+" class=\"formElemen\" onkeydown=\"javascript:keyDownCheck(event)\">");
            rowx.add("<input type=\"text\" size=\"30\" name=\"matItem\" value=\""+mat.getName()+"\" "+read+" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">");
            if(bEnableExpiredDate){
                     rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE]+"_"+i, new Date(), 1, -5, "formElemen", ""));
            }
            //add opie-eyek 20140108 untuk konversi satuan
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"_"+i+"\" value=\""+poItem.getQtyRequest()+"\" class='formElemen' onkeyup=\"javascript:changeAll(this.value,'"+unit.getOID()+"',"+i+")\"></div>");
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"_"+i, null, ""+poItem.getUnitRequestId(), index_value, index_key,"onChange=\"javascript:showDataAll(this.value,'"+unit.getOID()+"',"+i+")\"","formElemen")+" </div>");
if(useForRadtya.equals("1")){
            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+poItem.getUnitId()+""+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i +"\" value=\""+poItem.getPriceKonv()+"\" "+read+" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonvAll(this.value,"+i+")\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"_"+i+"\" value=\""+poItem.getOrgBuyingPrice()+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\""+poItem.getDiscount()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\""+poItem.getDiscount2()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"_"+i+"\" value=\""+poItem.getDiscNominal()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"_"+i+"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\"total_cost"+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" readOnly>"+
                      "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotalAll(this, event,"+i+")\" readOnly>");
        }
    }else{
            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+poItem.getUnitId()+""+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
        }
}
  if(useForRadtya.equals("0")){
            if(privShowQtyPrice){
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i+"\" value=\""+poItem.getPriceKonv()+"\" "+read+" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonvAll(this.value,"+i+")\"></div>");
                rowx.add("<div align=\"right\"><input indextab=\"2\" type=\"text\" size=\"15\" readonly='readonly' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"_"+i+"\" value=\""+poItem.getOrgBuyingPrice()+"\" onkeyup=\"javascript:cntTotalAll(this, event,"+i+")\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">");
                rowx.add("<div align=\"right\"><input indextab=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\""+poItem.getDiscount()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input indextab=\"4\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\""+poItem.getDiscount2()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input indextab=\"5\" type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"_"+i+"\" value=\""+poItem.getDiscNominal()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input indextab=\"6\" type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"_"+i+"\" value=\""+""+"\" "+read+" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event,"+i+")\" style=\"text-align:right\"></div>");
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+poItem.getUnitId()+""+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i +"\" value=\""+poItem.getPriceKonv()+"\" "+read+" class=\"formElemen changeHarga\" onkeyup=\"javascript:changePriceKonvAll(this.value,"+i+")\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"_"+i+"\" value=\""+poItem.getOrgBuyingPrice()+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"formElemen\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\""+poItem.getDiscount()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\""+poItem.getDiscount2()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"_"+i+"\" value=\""+poItem.getDiscNominal()+"\" "+read+" class=\"formElemen\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"_"+i+"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\"total_cost"+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" readOnly>"+
                      "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotalAll(this, event,"+i+")\" readOnly>");
            }
}
            rowx.add("<div align=\"right\"><input indextab=\"7\" type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY]+"_"+i+"\" value=\""+poItem.getQuantity()+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>" +
                    "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY"+"_"+i+"\" value=\""+sisa+"\">");
            
  if(useForRadtya.equals("0")){
            if(poItem.getBonus()==1) {
                rowx.add("<div align=\"center\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"_"+i+"\" value=\"1\"><input type=\"checkbox\"  name=\"chekBonus\" checked value=\"1\"> Bonus </div>");
            }else{
                 rowx.add("<div align=\"center\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"_"+i+"\" value=\"0\"><input type=\"checkbox\"  name=\"chekBonus\" value=\"1\"> Bonus </div>");
            }
            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost"+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotalAll(this, event,"+i+")\" readOnly></div>");
            }
}
            lstData.add(rowx);
        }
        return ctrlist.draw();
    }
    else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>";
    }
    return result;
}

%>


<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReceiveMaterial = FRMQueryString.requestLong(request,"hidden_receive_id");
long oidReceiveMaterialItem = FRMQueryString.requestLong(request,"hidden_receive_item_id");
String syspropPenerimaanHpp = PstSystemProperty.getValueByName("SHOW_PENERIMAAN_HPP");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* process on purchase order main
*/
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(Command.EDIT,oidReceiveMaterial);
FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();

/**
* check if document may modified or not
*/
boolean privManageData = true;

ControlLine ctrLine = new ControlLine();
CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatReceiveItem.action(iCommand,oidReceiveMaterialItem,oidReceiveMaterial, userName, userId);
FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
msgString = ctrlMatReceiveItem.getMessage();

String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                         " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
String orderClauseItem = "";
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
int recordToGetItem = 1000;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST){
    startItem = ctrlMatReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */
if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {
    iCommand = Command.ADD;
    /** agar form add item tampil pada list paling akhir */
    startItem = ctrlMatReceiveItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);
}

Vector listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
if(listMatReceiveItem.size()<1 && startItem>0) {
    if(vectSizeItem-recordToGetItem > recordToGetItem) {
        startItem = startItem - recordToGetItem;
    } else {
        startItem = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
}


String whereClauseBonusItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                              " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=1";
Vector listMatReceiveBonusItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseBonusItem);

PurchaseOrder po = new PurchaseOrder();
try {
    po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
} catch(Exception xxx){
}

Vector vectPO = new Vector();
if(iCommand == Command.ADDALL || iCommand==Command.SAVEALL){
    String whereClause = " POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] +
                " = " + rec.getPurchaseOrderId()+ " AND POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_RESIDU_QTY]+" < POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_QUANTITY];
    vectPO = PstPurchaseOrderItem.listPO(0,0,whereClause); 
}
if(iCommand==Command.SAVEALL){
    ctrlMatReceiveItem.actionSaveAll(request,vectPO,oidReceiveMaterial);
    listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
    listMatReceiveBonusItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseBonusItem);
    iCommand = Command.ADD;
}

String readonlyQty="";
if(typeOfBusiness.equals("3") && privFinal==true){
    readonlyQty="readonly";
}

// add by fitra 17-05-2014
if(iCommand==Command.SAVE && iErrCode == 0) {
        iCommand = Command.ADD;
        oidReceiveMaterialItem=0;
}

//cek verification, apakah menggunakan sidik jari atau tidak
String verificationType = PstSystemProperty.getValueByName("PROCHAIN_LOGIN_TYPE");
//String verificationType = "0";

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title
        ><script language="JavaScript">
            //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function main(oid, comm){
            document.frm_recmaterial.command.value = comm;
            document.frm_recmaterial.hidden_receive_id.value = oid;
            document.frm_recmaterial.action = "receive_wh_supp_po_material_edit.jsp";
            document.frm_recmaterial.submit();
            }
            //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


            //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
            function cmdAdd(){
            document.frm_recmaterial.hidden_receive_item_id.value = "0";
            document.frm_recmaterial.command.value = "<%=Command.ADD%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            if (compareDateForAdd() == true)
                    document.frm_recmaterial.submit();
            }

            function cmdEdit(oidReceiveMaterialItem){
            document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
            document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            document.frm_recmaterial.submit();
            }

            function cmdAsk(oidReceiveMaterialItem){
            document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
            document.frm_recmaterial.command.value = "<%=Command.ASK%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            document.frm_recmaterial.submit();
            }

            function cmdSave() {
            var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
            var residueQty = document.frm_recmaterial.FRM_FIELD_RESIDUE_QTY.value;
            if (parseFloat(qty) <= parseFloat(residueQty)) {
            document.frm_recmaterial.command.value = "<%=Command.SAVE%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            document.frm_recmaterial.submit();
            }
            else {
            alert("Quantity more than residue quantity!");
            }
            }

            function change(value){
            document.frm_recmaterial.hidden_qty_input.value = value
                    var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
            showData(oidUnit);
            var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
            var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup, guiDecimalSymbol);
            var total = cost * qty;
            //alert("qty "+qty);
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.value = parseFloat(total);
            document.frm_recmaterial.total_cost.value = parseFloat(total);
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(total);
            }

            function changePriceKonv(value, e){
            var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
            var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.value, guiDigitGroup, guiDecimalSymbol);
            var total = cost / qty;
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = parseFloat(total);
            var qtyx = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
            var costx = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup, guiDecimalSymbol);
            var totalx = costx * qtyx;
            document.frm_recmaterial.total_cost.value = parseFloat(totalx);
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(totalx);
            if (e.keyCode == 13) {
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
            }

            }

            function cmdConfirmDelete(oidReceiveMaterialItem){
            document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
            document.frm_recmaterial.command.value = "<%=Command.DELETE%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.approval_command.value = "<%=Command.DELETE%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            document.frm_recmaterial.submit();
            }

            // add by fitra 17-05-2014
            function cmdNewDelete(oid){
            var msg;
            msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
            var agree = confirm(msg);
            if (agree)
                    return cmdConfirmDelete(oid);
            else
                    return cmdEdit(oid);
            }

            function cmdCancel(oidReceiveMaterialItem){
            document.frm_recmaterial.hidden_receive_item_id.value = oidReceiveMaterialItem;
            document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
            document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
            document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
            document.frm_recmaterial.submit();
            }

            function cmdHargaJual(oidMaterial) {
            var strvalue = "<%=approot%>/master/material/material_main.jsp?command=<%=Command.EDIT%>" +
                        "&hidden_material_id=" + oidMaterial +
                        "&mat_code=" + document.frm_recmaterial.matCode.value +
                        "&txt_materialname=" + document.frm_recmaterial.matItem.value;
                winSrcMaterial = window.open(strvalue, "material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                if (window.focus) { winSrcMaterial.focus(); }
                }

                function cmdBack(){
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.start_item.value = 0;
                document.frm_recmaterial.action = "receive_wh_supp_po_material_edit.jsp";
                document.frm_recmaterial.submit();
                }

                function SaveAll(){
                document.frm_recmaterial.command.value = "<%=Command.SAVEALL%>";
                document.frm_recmaterial.prev_command.value = "<%=prevCommand%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
                }

                function cmdCheck(){
                var strvalue = "materialpodosearch.jsp?command=<%=Command.FIRST%>" +
                        "&mat_code=" + document.frm_recmaterial.matCode.value +
                        "&oidPurchaseOrder=<%=rec.getPurchaseOrderId()%>";
                window.open(strvalue, "material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                }

                function keyDownCheck(e){
                if (e.keyCode == 13) {
                //document.all.aSearch.focus();
                cmdCheck();
                }
                }


                function changeFocus(element){
                if (element.name == "matCode") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>") {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                }
                else if (element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>") {
                cmdSave();
                }
                else {
                cmdSave();
                }
                }

                function cntTotal(element, evt){
                var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value, guiDigitGroup);
                var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup);
                var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value, guiDigitGroup);
                var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value, guiDigitGroup, guiDecimalSymbol);
                var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value, guiDigitGroup, guiDecimalSymbol);
                var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value, guiDigitGroup, guiDecimalSymbol);
                if (price == "") { price = 0; }
                if (forwarder_cost == "") { forwarder_cost = 0; }

                if (isNaN(lastDisc) || (lastDisc == ""))
                        lastDisc = 0.0;
                if (isNaN(lastDisc2) || (lastDisc2 == ""))
                        lastDisc2 = 0.0;
                if (isNaN(lastDiscNom) || (lastDiscNom == ""))
                        lastDiscNom = 0.0;
                var totaldiscount = price * lastDisc / 100;
                var totalMinus = price - totaldiscount;
                var totaldiscount2 = totalMinus * lastDisc2 / 100;
                var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                if (!(isNaN(qty)) && (qty != '0')) {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(totalCost) * qty;
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.value = parseFloat(totalCost) * qty;
                document.frm_recmaterial.total_cost.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
                }
                else {
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                }

                if (evt.keyCode == 13) {
                changeFocus(element);
                }
                }



                function cmdListFirst(){
                document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.prev_command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
                }

                function cmdListPrev(){
                document.frm_recmaterial.command.value = "<%=Command.PREV%>";
                document.frm_recmaterial.prev_command.value = "<%=Command.PREV%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
                }

                function cmdListNext(){
                document.frm_recmaterial.command.value = "<%=Command.NEXT%>";
                document.frm_recmaterial.prev_command.value = "<%=Command.NEXT%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
                }

                function cmdListLast(){
                document.frm_recmaterial.command.value = "<%=Command.LAST%>";
                document.frm_recmaterial.prev_command.value = "<%=Command.LAST%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
                }

                function cmdBackList(){
                document.frm_recmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_recmaterial.action = "receive_wh_supp_po_material_list.jsp";
                document.frm_recmaterial.submit();
                }

                function gostock(oid){
                document.frm_recmaterial.command.value = "<%=Command.EDIT%>";
                document.frm_recmaterial.rec_type.value = 1;
                document.frm_recmaterial.type_doc.value = 1;
                document.frm_recmaterial.hidden_receive_item_id.value = oid;
                document.frm_recmaterial.action = "rec_wh_stockcode.jsp";
                document.frm_recmaterial.submit();
                }


                function checkBonus(){
                //var checkBook =document.frm_recmaterial.checkbox.value;
                //alert("hekkkii");
                if (document.frm_recmaterial.typeBonus.checked){
                //alert("hekkkii");
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>.value = "1";
                }
                }

                //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


                //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
                function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr; for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
                }

                function MM_preloadImages() { //v3.0
                var d = document; if (d.images){ if (!d.MM_p) d.MM_p = new Array();
                var i, j = d.MM_p.length, a = MM_preloadImages.arguments; for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0){ d.MM_p[j] = new Image; d.MM_p[j++].src = a[i]; }}
                }

                function MM_findObj(n, d) { //v4.0
                var p, i, x; if (!d) d = document; if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                d = parent.frames[n.substring(p + 1)].document; n = n.substring(0, p); }
                if (!(x = d[n]) && d.all) x = d.all[n]; for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++) x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById) x = document.getElementById(n); return x;
                }

                function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments; document.MM_sr = new Array; for (i = 0; i < (a.length - 2); i += 3)
                        if ((x = MM_findObj(a[i])) != null){document.MM_sr[j++] = x; if (!x.oSrc) x.oSrc = x.src; x.src = a[i + 2]; }
                }
                //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

        <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
        <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
        <script type="text/javascript">

                var interval = 0;
                var verificationType = <%=verificationType%>;
                //untuk rekap
                var data = 0;
                var i = 0;
                var rePrice = 0;
                function showData(value){
                var qtyInput = document.frm_recmaterial.hidden_qty_input.value;
                var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value;
                var oidKonversiUnit = value;
                //alert("showdata");
                checkAjax(oidKonversiUnit, oidUnit, qtyInput);
                }

                function disabled(){

                $("#frm_recmaterial input[name='<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>']").attr('readonly', 'true');
                }



                function ajaxUser(url, data, type, appendTo, another, optional, optional2){
                $.ajax({
                url : "" + url + "",
                        data: "" + data + "",
                        type : "" + type + "",
                        async : false,
                        cache: false,
                        success : function(data) {
                        //alert(data);
                        $('' + appendTo + '').html(data);
                        },
                        error : function(data){
                        alert('error');
                        }
                }).done(function(data){
                if (another == "checkStatusUser"){
                if (data == 1){

                clearInterval(interval);
                //$('#modalReport').modal({backdrop: 'static', keyboard: false});
                alert("<%= textListGlobal[SESS_LANGUAGE][9]%>");
                //lanjutkan kode disini
                rekapitulasi(data, i, rePrice);
                //close modal
                $('#modalVerifikasi').modal('hide');
                }
                }
                });
                }

                function rekapitulasi(data, i, price){

                $("#frm_recmaterial input[name='<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_" + i + "']").val(data);
                var total = price * data;
                var totalstock = total / data;
                var total2 = parseFloat(total);
                var totalStock = parseFloat(totalstock)

                        $("#frm_recmaterial input[name='total_cost_" + i + "']").val(total);
                $("#frm_recmaterial input[name='<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_" + i + "']").val(total);
                $("#frm_recmaterial input[name='<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_" + i + "']").val(totalStock);
                $("#frm_recmaterial input[name='<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>']").focus();
                }

                function keyUpFunction(){
                var url = "<%=approot%>/AjaxUser";
                var loginId = $('#searchVerification').val();
                var data = "command=<%=Command.ASSIGN%>&login=" + loginId + "&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>";
                ajaxUser(url, data, "POST", "#dynamicPlace", "checkUser", "", "");
                }

                function checkStatusUser(userId){
                var url = "<%=approot%>/AjaxUser";
                var data = "command=<%=Command.SEARCH%>&loginId=" + userId + "";
                ajaxUser(url, data, "POST", "", "checkStatusUser", "", "");
                }

                function searchVerificationKeyUp(){
                keyUpFunction();
                }

                function fingerClick(){
                var loginId = $('#searchVerification').val();
                interval = setInterval(function() {
                checkStatusUser(loginId);
                }, 5000);
                }

                function checkAjax(oidKonversiUnit, oidUnit, qtyInput){
                $.ajax({
                url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>=" + oidKonversiUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>=" + oidUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>=" + qtyInput + "",
                        type : "POST",
                        async : false,
                        success : function(data) {
                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = data;
                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
                        }
                });
                }



                function changeAll(valuex, oidKonv, countx){
                //alert("aaa");
                if (valuex != 0){
                //alert("1");
                switch (countx){
            <%for(int i=0; i<vectPO.size(); i++){%>
                case <%=""+i%>:
                        //alert("2");         
                        var inputqty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>_<%=""+i%>.value;
                            var oidUnitkonv = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>_<%=""+i%>.value;
                                var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>_<%=""+i%>.value;
                                    var price = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                        $.ajax({
                                        url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>=" + oidUnitkonv + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>=" + oidUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>=" + inputqty + "",
                                                type : "POST",
                                                async : false,
                                                success : function(data) {
                                                var sisa = parseFloat(document.frm_recmaterial.FRM_FIELD_RESIDUE_QTY_<%=""+i%>.value);
                                                if (data <= sisa){
                                                //jika verifikasi 
                                                //muncul verifikasi sidik jari  
                                                //alert("3");
                                                if (verificationType == "1"){
                                                $('#searchVerification').val('');
                                                keyUpFunction();
                                                $('#modalVerifikasi').modal({
                                                backdrop: 'static',
                                                        keyboard: false
                                                });
                                                data = data;
                                                i = i;
                                                rePrice = price;
                                                } else{
                                                //alert("4 "+price+"/data "+data);  
                                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value = data;
                                                            //alert("5");
                                                            var total = price * data;
                                                            var totalstock = total / data;
                                                            /*if(isNaN(total)){
                                                             total=0;
                                                             }
                                                             if(isNan(totalstock)){
                                                             totalstock=0;
                                                             }*/

                                                            //alert("total" + total);

                                                            document.frm_recmaterial.total_cost_<%=""+i%>.value = parseFloat(total);
                                                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value = parseFloat(total);
                                                                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value = parseFloat(totalstock);
                                                                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>_<%=""+i%>.value = parseFloat(total);
                                                                                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.focus();
                                                                                                }

                                                                                                } else{
                                                                                                alert("Input : " + data + "Residu : " + sisa)
                                                                                                        alert("maaf qty yang di input melebihi qty order");
                                                                                                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>_<%=""+i%>.value = "0";
                                                                                                            }

                                                                                                            }
                                                                                                    });
                                                                                                    break;
            <%}%>
                                                                                                    }
                                                                                                    }
                                                                                                    }






                                                                                                    function cntTotalAll(element, evt, countx){
                                                                                                    switch (countx){
            <%for(int i=0; i<vectPO.size(); i++){%>
                                                                                                    case <%=""+i%>:
                                                                                                            var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value, guiDigitGroup);
                                                                                                                var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value, guiDigitGroup);
                                                                                                                    var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>_<%=""+i%>.value, guiDigitGroup);
                                                                                                                        var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                            var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                                var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                                    if (price == "") { price = 0; }
                                                                                                                                    if (forwarder_cost == "") { forwarder_cost = 0; }

                                                                                                                                    if (isNaN(lastDisc) || (lastDisc == ""))
                                                                                                                                            lastDisc = 0.0;
                                                                                                                                    if (isNaN(lastDisc2) || (lastDisc2 == ""))
                                                                                                                                            lastDisc2 = 0.0;
                                                                                                                                    if (isNaN(lastDiscNom) || (lastDiscNom == ""))
                                                                                                                                            lastDiscNom = 0.0;
                                                                                                                                    var totaldiscount = price * lastDisc / 100;
                                                                                                                                    var totalMinus = price - totaldiscount;
                                                                                                                                    var totaldiscount2 = totalMinus * lastDisc2 / 100;
                                                                                                                                    var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                                                                                                                                    if (!(isNaN(qty)) && (qty != '0')) {
                                                                                                                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value = parseFloat(totalCost) * qty;
                                                                                                                                        document.frm_recmaterial.total_cost_<%=""+i%>.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
                                                                                                                                        } else {
                                                                                                                                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.focus();
                                                                                                                                            }
                                                                                                                                            break;
            <%}%>
                                                                                                                                            }

                                                                                                                                            }


                                                                                                                                            function changePriceKonvAll(value, countx){
                                                                                                                                            switch (countx){
            <%for(int i=0; i<vectPO.size(); i++){%>
                                                                                                                                            case <%=""+i%>:
                                                                                                                                                    var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                                                                                                                                                        var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                                                            var total = cost / qty;
                                                                                                                                                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value = parseFloat(total);
                                                                                                                                                                var qtyx = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                                                                                                                                                                    var costx = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                                                                        var totalx = costx * qtyx;
                                                                                                                                                                        document.frm_recmaterial.total_cost_<%=""+i%>.value = parseFloat(totalx);
                                                                                                                                                                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value = parseFloat(totalx);
                                                                                                                                                                            break;
            <%}%>
                                                                                                                                                                            }
                                                                                                                                                                            }



                                                                                                                                                                            function showDataAll(oidKonv, value, countx){
                                                                                                                                                                            changeAll(oidKonv, value, countx);
                                                                                                                                                                            }

                                                                                                                                                                            $(document).ready(function(){
                                                                                                                                                                            var interval = 0;
                                                                                                                                                                            var tipeVerifikasi = <%=verificationType%>;
                                                                                                                                                                            var expectedQty = 0;
                                                                                                                                                                            var normalQty = $('.changeQtyPo').val();
                                                                                                                                                                            if (tipeVerifikasi == "1"){
                                                                                                                                                                            $('.changeHarga').prop('readonly', true);
                                                                                                                                                                            $('.changeHarga').click(function(){
                                                                                                                                                                            $('#searchVerificationPrice').val('');
                                                                                                                                                                            checkUser2();
                                                                                                                                                                            $('#modalVerifikasiPrice').modal({
                                                                                                                                                                            backdrop: 'static',
                                                                                                                                                                                    keyboard: false
                                                                                                                                                                            });
                                                                                                                                                                            });
                                                                                                                                                                            $('.changeHarga').blur(function(){
                                                                                                                                                                            $('.changeHarga').prop('readonly', true);
                                                                                                                                                                            });
                                                                                                                                                                            }

                                                                                                                                                                            function ajaxUser2(url, data, type, appendTo, another, optional, optional2){
                                                                                                                                                                            $.ajax({
                                                                                                                                                                            url : "" + url + "",
                                                                                                                                                                                    data: "" + data + "",
                                                                                                                                                                                    type : "" + type + "",
                                                                                                                                                                                    async : false,
                                                                                                                                                                                    cache: false,
                                                                                                                                                                                    success : function(data) {
                                                                                                                                                                                    //alert(data);
                                                                                                                                                                                    $('' + appendTo + '').html(data);
                                                                                                                                                                                    },
                                                                                                                                                                                    error : function(data){
                                                                                                                                                                                    alert('error');
                                                                                                                                                                                    }
                                                                                                                                                                            }).done(function(data){

                                                                                                                                                                            if (another == "checkStatusUser2"){
                                                                                                                                                                            if (data == 1){
                                                                                                                                                                            clearInterval(interval);
                                                                                                                                                                            alert("<%= textListGlobal[SESS_LANGUAGE][9]%>");
                                                                                                                                                                            //alert(data);
                                                                                                                                                                            $('#modalVerifikasiPrice').modal('hide');
                                                                                                                                                                            $('.changeHarga').removeAttr('readonly');
                                                                                                                                                                            $('.changeHarga').focus();
                                                                                                                                                                            }
                                                                                                                                                                            } else if (another == "checkUser2"){
                                                                                                                                                                            clickFinger2();
                                                                                                                                                                            } else if (another == "checkUser3"){
                                                                                                                                                                            clickFinger3();
                                                                                                                                                                            } else if (another == "checkStatusUser3"){
                                                                                                                                                                            if (data == 1){
                                                                                                                                                                            clearInterval(interval);
                                                                                                                                                                            alert("<%= textListGlobal[SESS_LANGUAGE][9]%>");
                                                                                                                                                                            //alert(data);
                                                                                                                                                                            $('#modalVerifikasiQty').modal('hide');
                                                                                                                                                                            $('.changeQtyPo').val(expectedQty);
                                                                                                                                                                            }
                                                                                                                                                                            }
                                                                                                                                                                            });
                                                                                                                                                                            }

                                                                                                                                                                            function checkUser2(){
                                                                                                                                                                            var url = "<%=approot%>/AjaxUser";
                                                                                                                                                                            var loginId = $('#searchVerificationPrice').val();
                                                                                                                                                                            var data = "command=<%=Command.ASSIGN%>&login=" + loginId + "&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>&func='2'";
                                                                                                                                                                            ajaxUser2(url, data, "POST", "#dynamicPlacePrice", "checkUser2", "", "");
                                                                                                                                                                            }

                                                                                                                                                                            function checkUser3(){
                                                                                                                                                                            var url = "<%=approot%>/AjaxUser";
                                                                                                                                                                            var loginId = $('#searchVerificationQty').val();
                                                                                                                                                                            var data = "command=<%=Command.ASSIGN%>&login=" + loginId + "&language=<%= SESS_LANGUAGE%>&base=<%= baseURL%>&func='2'";
                                                                                                                                                                            ajaxUser2(url, data, "POST", "#dynamicPlaceQty", "checkUser3", "", "");
                                                                                                                                                                            }

                                                                                                                                                                            function checkStatusUser2(userId){
                                                                                                                                                                            var url = "<%=approot%>/AjaxUser";
                                                                                                                                                                            var data = "command=<%=Command.SEARCH%>&loginId=" + userId + "";
                                                                                                                                                                            ajaxUser2(url, data, "POST", "", "checkStatusUser2", "", "");
                                                                                                                                                                            }

                                                                                                                                                                            function checkStatusUser3(userId){
                                                                                                                                                                            var url = "<%=approot%>/AjaxUser";
                                                                                                                                                                            var data = "command=<%=Command.SEARCH%>&loginId=" + userId + "";
                                                                                                                                                                            //alert(data);
                                                                                                                                                                            ajaxUser2(url, data, "POST", "", "checkStatusUser3", "", "");
                                                                                                                                                                            }

                                                                                                                                                                            function clickFinger2(){
                                                                                                                                                                            $('.loginFinger').click(function(){
                                                                                                                                                                            var loginId = $('#searchVerificationPrice').val();
                                                                                                                                                                            interval = setInterval(function() {
                                                                                                                                                                            checkStatusUser2(loginId);
                                                                                                                                                                            }, 5000);
                                                                                                                                                                            });
                                                                                                                                                                            }

                                                                                                                                                                            function clickFinger3(){
                                                                                                                                                                            $('.loginFinger2').click(function(){
                                                                                                                                                                            var loginId = $('#searchVerificationQty').val();
                                                                                                                                                                            //alert(loginId);
                                                                                                                                                                            interval = setInterval(function() {
                                                                                                                                                                            checkStatusUser3(loginId);
                                                                                                                                                                            }, 5000);
                                                                                                                                                                            });
                                                                                                                                                                            }

                                                                                                                                                                            $('#searchVerificationPrice').keyup(function(){
                                                                                                                                                                            checkUser2();
                                                                                                                                                                            });
                                                                                                                                                                            $('#searchVerificationQty').keyup(function(){
                                                                                                                                                                            checkUser3();
                                                                                                                                                                            });
                                                                                                                                                                            $('.changeQtyPo').keyup(function(){
                                                                                                                                                                            var qty = 0;
                                                                                                                                                                            var normalQty2 = $('#qty_help').val();
                                                                                                                                                                            if (normalQty2 == 0){
                                                                                                                                                                            qty = normalQty;
                                                                                                                                                                            } else{
                                                                                                                                                                            qty = normalQty2;
                                                                                                                                                                            }
                                                                                                                                                                            expectedQty = $(this).val();
                                                                                                                                                                            var inputqty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>.value;
                                                                                                                                                                            var oidUnitkonv = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
                                                                                                                                                                            var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value;
                                                                                                                                                                            var price = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value, guiDigitGroup, guiDecimalSymbol);
                                                                                                                                                                            $.ajax({
                                                                                                                                                                            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>=" + oidUnitkonv + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>=" + oidUnit + "&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>=" + inputqty + "",
                                                                                                                                                                                    type : "POST",
                                                                                                                                                                                    async : false,
                                                                                                                                                                                    success : function(data) {
                                                                                                                                                                                    var sisa = parseFloat(document.frm_recmaterial.FRM_FIELD_RESIDUE_QTY.value);
                                                                                                                                                                                    if (data <= sisa){
                                                                                                                                                                                    if (verificationType == "1"){
                                                                                                                                                                                    $('.changeQtyPo').val(qty);
                                                                                                                                                                                    checkUser3();
                                                                                                                                                                                    $('#searchVerificationQty').val('');
                                                                                                                                                                                    $('#modalVerifikasiQty').modal('show');
                                                                                                                                                                                    }
                                                                                                                                                                                    } else{
                                                                                                                                                                                    //alert("test");
                                                                                                                                                                                    alert("Input : " + data + "Residu : " + sisa)
                                                                                                                                                                                            alert("maaf qty yang di input melebihi qty order");
                                                                                                                                                                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.value = parseFloat(qty * price);
                                                                                                                                                                                    document.frm_recmaterial.total_cost.value = parseFloat(qty * price);
                                                                                                                                                                                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>.value = qty;
                                                                                                                                                                                    }

                                                                                                                                                                                    }
                                                                                                                                                                            });
                                                                                                                                                                            });
                                                                                                                                                                            });
        </script>

    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
            <tr>
                <td  ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%}else{%>
            <tr bgcolor="#FFFFFF">
                <td  ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][5]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>		  
                                <!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" -->
                                <form id="frm_recmaterial" name="frm_recmaterial" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="rec_type" value="">
                                    <input type="hidden" name="type_doc" value="">
                                    <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
                                    <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="hidden_qty_unit" value="">
                                    <input type="hidden" name="hidden_qty_input" value="">
                                    <input type="hidden" name="qty_help" id="qty_help" value="0">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr>
                                            <td valign="top" colspan="3"><table width="100%"  border="0" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td width="26%" valign="top">&nbsp;</td>
                                                    </tr>

                                                    <tr>
                                                        <td width="8%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="22%">: <b><%=rec.getRecCode()%></b></td>
                                                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                        <td width="26%">:
                                                            <%
                                                            Vector obj_supplier = new Vector(1,1);
                                                            Vector val_supplier = new Vector(1,1);
                                                            Vector key_supplier = new Vector(1,1);
                                                            String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                                    " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                                    " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                                    " != "+PstContactList.DELETE;
                                                            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                            //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                            for(int d=0; d<vt_supp.size(); d++){
                                                                ContactList cnt = (ContactList)vt_supp.get(d);
                                                                String cntName = cnt.getCompName();
                                                                if(cntName.length()==0){
                                                                    cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                                                }
                                                                val_supplier.add(String.valueOf(cnt.getOID()));
                                                                key_supplier.add(cntName);
                                                            }
                                                            String select_supplier = ""+rec.getSupplierId();
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"disabled=\"true\"","formElemen")%> </td>
                                                        <!-- adding term payment -->
                                                        <!-- by Mirahu 20120302 -->
                                                        <%if(privShowQtyPrice){%>
                                                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][13]%></td>
                                                        <td width="26%" align="left" valign="top">:
                                                            <%
                                                                  Vector val_terms = new Vector(1,1);
                                                                  Vector key_terms = new Vector(1,1);
                                                                  for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                                                      val_terms.add(String.valueOf(d));
                                                                        key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                                                   }
                                                                       String select_terms = ""+rec.getTermOfPayment();
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"disabled=\"true\"","formElemen")%> </td>
                                                            <%}else{ %>
                                                    <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=rec.getTermOfPayment()%>"></td>
                                                    <%}%>
                                                    </tr>

                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], rec.getReceiveDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                        <td>:
                                                            <input type="text" name="txt_ponumber"  value="<%= po.getPoCode() %>" class="formElemen" size="20" disabled="true"></td>
                                                        <!-- adding credit time -->
                                                        <!-- by Mirahu 20120302 -->
                                                        <%if(privShowQtyPrice){%>
                                                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][14]%></td>
                                                        <td width="26%" align="left" valign="top">:
                                                            <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>" readOnly></td>
                                                            <%}else{ %>
                                                    <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                                    <%}%>
                                                    </tr>
                                                    <tr>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                        <td>:
                                                            <%
                                                            Vector obj_locationid = new Vector(1,1);
                                                            Vector val_locationid = new Vector(1,1);
                                                            Vector key_locationid = new Vector(1,1);
                                                            String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                            whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                            Vector vt_loc = PstLocation.list(0, 0, whereClause, "");
                                                            for(int d=0;d<vt_loc.size();d++){
                                                                Location loc = (Location)vt_loc.get(d);
                                                                val_locationid.add(""+loc.getOID()+"");
                                                                key_locationid.add(loc.getName());
                                                            }
                                                            String select_locationid = ""+rec.getLocationId(); //selected on combo box
                                                            %>
                                                            <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%></td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                                                        <td>:
                                                            <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" readonly></td>
                                                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                        <td width="26%" rowspan="4" valign="top">: 
                                                            <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2" tabindex="4"  disabled="true"><%=rec.getRemark()%></textarea></td> 
                                                        <!--td>&nbsp;</td-->
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                                                        <td>:
                                                            <%
                                                            Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                                                            Vector vectCurrVal = new Vector(1,1);
                                                            Vector vectCurrKey = new Vector(1,1);
                                                            vectCurrKey.add(" ");
                                                            vectCurrVal.add("0");
                                                            for(int i=0; i<listCurr.size(); i++){
                                                                CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                                                vectCurrKey.add(currencyType.getCode());
                                                                vectCurrVal.add(""+currencyType.getOID());
                                                            }
                                                            %>
                                                            <%=ControlCombo.draw("CURRENCY_CODE","formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled")%>
                                                            &nbsp;&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][15]%>&nbsp;&nbsp;
                                                            <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="formElemen" size="10" value="<%=rec.getTransRate()%>" disabled>
                                                        </td>
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
                                                                <%Vector listError = new Vector(1,1);%>
                                                                <%if(iCommand==Command.ADDALL){%>
                                                                <tr align="left" valign="top">
                                                                    <%
                                                                      try {
                                                                    %>
                                                                    <td  valign="middle" colspan="3">
                                                                        <%=drawListMaterial(SESS_LANGUAGE,vectPO,0,privShowQtyPrice,1,frmMatReceiveItem,rec.getCurrencyId(), useForRaditya) %>
                                                                        <%
                                                                        %>
                                                                    </td>
                                                                    <%
                                                                    } catch(Exception e)	{
                                                                        System.out.println(e);
                                                                    }
                                                                    %>
                                                                </tr>
                                                                <%}else{
                                                                %>

                                                                <tr align="left" valign="top">
                                                                    <%
                                                                      try {
                                                                    %>
                                                                    <td  valign="middle" colspan="3">
                                                                        <%
                                                                        Vector list = drawListRetItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,po.getExchangeRate(),readonlyQty,typeOfBusiness,rec.getCurrencyId(), approot, syspropPenerimaanHpp, useForRaditya);
                                                                        out.println(""+list.get(0));
                                                                        listError = (Vector)list.get(1);
                                                                        %>
                                                                    </td>
                                                                    <%
                                                                    } catch(Exception e)	{
                                                                        System.out.println(e);
                                                                    }
                                                                    %>
                                                                </tr>
                                                                <tr align="left" valign="top">
                                                                    <td  valign="middle" colspan="3">
                                                                        &nbsp;
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                if(useForRaditya.equals("1")){}else{
                                                                %>
                                                                <tr align="left" valign="top">
                                                                    <td  valign="middle" colspan="3">Bonus Item
                                                                    </td>
                                                                </tr>
                                                                <tr align="left" valign="top">
                                                                    <%
                                                                     try {
                                                                    %>
                                                                    <td  valign="middle" colspan="3">
                                                                        <%
                                                                        Vector list = drawListRetBonusItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveBonusItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,po.getExchangeRate(),readonlyQty,typeOfBusiness,rec.getCurrencyId(), approot, syspropPenerimaanHpp);
                                                                        out.println(""+list.get(0));
                                                                        listError = (Vector)list.get(1);
                                                                        %>
                                                                    </td>
                                                                    <%
                                                                    } catch(Exception e)	{
                                                                        System.out.println(e);
                                                                    }
                                                                    %>
                                                                </tr>
                                                                <%if(privShowQtyPrice){%>
                                                                <tr align="left" valign="top">
                                                                    <td  colspan="3" valign="middle">
                                                                        <%
                                                                            out.println("&nbsp;&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'><blink><b>[edit]</b></blink></font>&nbsp; : <b>Edit Harga Jual (Jika Harga Beli + PPN di Master Data Lebih Kecil dari Harga Beli di Dokument Penerimaan Ini)</b><br>");
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                                <%}}
                                if(useForRaditya.equals("1")){}else{%>  
                                                                <tr align="left" valign="top">
                                                                    <td  align="left" colspan="3" class="command">
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
                                                                    </td>
                                                                </tr>
                                                                <%}%>
                                                                <tr align="left" valign="top">
                                                                    <td  align="left" colspan="3" class="errfont">
                                                                        <%
                                                                        for(int k=0;k<listError.size();k++){
                                                                            if(k==0)
                                                                                out.println(listError.get(k)+"<br>");
                                                                            else
                                                                                out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                                        }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                                <tr align="left" valign="top">
                                                                    <td  valign="middle" colspan="3">
                                                                        <%
                                                                        ctrLine.setLocationImg(approot+"/images");
                                
                                                                        // set image alternative caption
                                                                        ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ADD,true));
                                                                        ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_SAVE,true));
                                                                        ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true)+" List");
                                                                        ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ASK,true));
                                                                        ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_CANCEL,false));
                                
                                                                        ctrLine.initDefault();
                                                                        ctrLine.setTableWidth("65%");
                                                                        String scomDel = "javascript:cmdAsk('"+oidReceiveMaterialItem+"')";
                                                                        String sconDelCom = "javascript:cmdConfirmDelete('"+oidReceiveMaterialItem+"')";
                                                                        String scancel = "javascript:cmdEdit('"+oidReceiveMaterialItem+"')";
                                                                        ctrLine.setCommandStyle("command");
                                                                        ctrLine.setColCommStyle("command");
                                
                                                                        // set command caption
                                                                        ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ADD,true));
                                                                        ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_SAVE,true));
                                                                        ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true)+" List");
                                                                        ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ASK,true));
                                                                        ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_DELETE,true));
                                                                        ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_CANCEL,false));
                                
                                
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
                                                                        <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                            <tr>
                                                                                <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                                                                <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                                                                <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_ADD,true)%></a></td>
                                                                                    <% } %>
                                                                                <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnBackOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                                                                <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true)%></a></td>
                                                                            </tr>
                                                                        </table>
                                                                        <%
                                                                        } else{
                                                                            if(iCommand!=Command.ADDALL){
                                                                                out.println(strDrawImage);
                                                                            }else{%>
                                                                        <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                            <tr>
                                                                                <%if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                                <td width="6%"><a href="javascript:SaveAll()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," All Item",ctrLine.CMD_SAVE,true)%>"></a></td>
                                                                                <td width="47%"><a href="javascript:SaveAll()"><%=ctrLine.getCommand(SESS_LANGUAGE," All Item",ctrLine.CMD_SAVE,true)%></a></td>
                                                                                    <%}%>
                                                                                <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnBackOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                                                                <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Item",ctrLine.CMD_BACK,true)%></a></td> 
                                                                            </tr>  
                                                                        </table>
                                                                        <%
                                                                        }
                                                                     }
                                                                        %>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <script language="javascript">
                                    document.frm_recmaterial.matCode.focus();
                                </script>
                                <!-- #EndEditable --></td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" > <!-- #BeginEditable "footer" -->
                    <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%}else{%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>

                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        <!-- CODE UNTUK MODAL BOOTSTRAP-->
        <style>
            .finger{
                width:20%; 
                height:auto;
                padding : 2%;
                float:left;
            }
            .finger_spot{
                width:100%;
                height: 80px;
                background-color :#e5e5e5;
                border : thin solid #c5c5c5;
                font-size: 14px;
                font-family:calibri;
                text-align:center;
                color :#FFF;
                border-radius: 3px;
            }

            .green{
                background-color : #5CB85C;
                border : thin solid #4CAE4C;
            }
        </style>
        <div id="modalVerifikasi" class="modal fade" tabindex="-1">
            <div id="modalVerifikasi" class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"  aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][8]%></h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <input onkeyup="javascript:searchVerificationKeyUp()" type="text" class="form-control" id="searchVerification" placeholder="Input user..."/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div id="dynamicPlace"></div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger">Close</button>
                    </div>
                </div>
            </div>

        </div>
        <div id="modalVerifikasiPrice" class="modal fade" tabindex="-1">
            <div id=""  class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" data-dismiss="modal" class="close"  aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][8]%></h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <input type="text" class="form-control" id="searchVerificationPrice" placeholder="Input user..."/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div id="dynamicPlacePrice"></div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
                    </div>
                </div>
            </div>

        </div>
        <div id="modalVerifikasiQty" class="modal fade" tabindex="-1">
            <div id=""  class="modal-dialog modal-sm">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" data-dismiss="modal" class="close"  aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][8]%></h4>
                    </div>
                    <div class="modal-body" id="modal-body">
                        <div class="row">
                            <div class="col-md-12">
                                <input type="text" class="form-control" id="searchVerificationQty" placeholder="Input user..."/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div id="dynamicPlaceQty"></div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
                    </div>
                </div>
            </div>

        </div>
    </body>
    <!-- #EndTemplate --></html>

