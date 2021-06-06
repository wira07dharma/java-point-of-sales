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
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
boolean privFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
%>
<!-- Jsp Block -->
<%!
static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang ..."},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item ..."}
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
    "Diskon1 %","Diskon2 %","Discount Nominal","Qty Entri","Unit Order","Harga Beli", "Hapus","Bonus"},//18
  {"No","Code","Name","Expired Date","Unit","Price/Stock","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
   "Discount2 %","Disc. Nominal","Qty Entri","Unit Order","Harga Beli", "Delete","Bonus"}
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
public Vector drawListRetItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start, boolean privShowQtyPrice,double exchangeRate, String readOnlyQty, String typeOfBusiness, long oidCurrency, String approot) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"5%");// no
    ctrlist.addHeader(textListOrderItem[language][1],"10%");// SKU
    ctrlist.addHeader(textListOrderItem[language][2],"20%");// NAMA
    if(bEnableExpiredDate){
     ctrlist.addHeader(textListOrderItem[language][3],"7%");
    }
    ctrlist.addHeader(textListOrderItem[language][14],"5%");//Qty Entri
    ctrlist.addHeader(textListOrderItem[language][15],"5%");//Unit Order
    ctrlist.addHeader("Unit Stock","30%");
    ctrlist.addHeader(textListOrderItem[language][8],"5%");//Qty@stock
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][9],"10%");//Total Beli
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
            rowx.add("<input type=\"hidden\" class =\"form-control\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\"class =\"form-control\"  size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add("<input type=\"text\" size=\"20\"  name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                }else{
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit"+mat.getCurrBuyPrice()+"]</a>");
                }
            }else{
                rowx.add("<input type=\"text\" size=\"20\"  name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            }

            if(bEnableExpiredDate){
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate()==null) ? new Date() : recItem.getExpiredDate(), 1, -5, "formElemen", ""));
            }
            //add opie-eyek 20140108 untuk konversi satuan
            if(readOnlyQty=="readonly"){
              rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" "+readOnlyQty+">");
            }else{
              rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");

            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+"\"><input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>" 
                + "<div align=\"center\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"</div>"
                + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" ></div></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>" 
                + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                + "<div align=\"hidden\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"); 
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" ><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<div align=\"right\" class=\"form-control\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"form-control\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\""+readOnlyQty+"></div>" +
                    "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\">");
            
            rowx.add("<div align=\"left\"><input type=\"checkbox\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"1\">Bonus</div>");
            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\" class=\"form-control\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"\" class=\"form-control\" readOnly>"+
                    "<div align=\"right\" class=\"form-control\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
            rowx.add("");
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
            
            if(privShowQtyPrice){  
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>" 
                + "<div align=\"left\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
             
                
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
            
            }else{
                
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</div>");
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
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                "\"><input indextab=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"form-control\" onkeydown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"30\" name=\"matItem\" value=\""+""+"\" class=\"form-control\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        if(bEnableExpiredDate){
                 rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 1, -5, "formElemen", ""));
        }
        //add opie-eyek 20140108 untuk konversi satuan
        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:change(this.value)\"</div>");
        rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>"
            + "<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+""+"\" class=\"hidden\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"</div>"
            + "<div align=\"right\"><input indextab=\"2\" type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"hidden\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
            + "<div align=\"right\"><input indextab=\"3\" type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+""+"\" class=\"hidden\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
            + "<div align=\"right\"><input indextab=\"4\" type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+""+"\" class=\"hidden\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
            + "<div align=\"right\"><input indextab=\"5\" type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+""+"\" class=\"hidden\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
            + "<div align=\"right\"><input indextab=\"6\" type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+""+"\" class=\"hidden\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
        
            
           
        
        if(privShowQtyPrice){   
            
        }else{
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>"
                + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"form-control\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+""+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+""+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+""+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                + "<input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                  "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly>");
        }
        
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");   
        rowx.add("<div align=\"left\"><input indextab=\"7\" type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>" +
                "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\"\">"
        + "<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"hidden\"  name=\"typeBonus\" value=\"0\" onClick=\"javascript:checkBonus()\" ></div>"); 
        if(privShowQtyPrice){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"form-control\" readOnly>"+
                "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
        }
        
          rowx.add("");
        lstData.add(rowx);
      }
    }
    
    list.add(ctrlist.drawBootstrap());
    list.add(listError);
    return list;
}

public Vector drawListRetBonusItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start, boolean privShowQtyPrice,double exchangeRate, String readOnlyQty, String typeOfBusiness, long oidCurrency, String approot) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("form-group");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("form-group");
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
        ctrlist.addHeader("Harga Beli","5%");
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
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"form-control\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
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
              rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" "+readOnlyQty+">");
            }else{
              rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"form-control\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");

            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+"\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonv(this.value,event)\"</div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" ></div></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getPriceKonv())+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonv(this.value,event)\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" ><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+"\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"form-control\" readOnly>"+
                    "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(this, event)\" readOnly></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\""+readOnlyQty+"></div>" +
                    "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\">");
            rowx.add("<div align=\"left\"><input type=\"checkbox\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" checked value=\"1\">Bonus</div>");
            
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"\" class=\"form-control\" readOnly>"+
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
    list.add(ctrlist.drawBootstrap());
    list.add(listError);
    return list;
}

public String drawListMaterial(int language,Vector objectClass,int start, boolean privShowQtyPrice, double exchangeRate,FrmMatReceiveItem frmObject, long oidCurrency, String poCode) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("form-group");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("form-group");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListOrderItem[language][0],"5%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"15%");
        if(bEnableExpiredDate){
         ctrlist.addHeader(textListOrderItem[language][3],"7%");
        }
        ctrlist.addHeader("Qty Order","5%");
        ctrlist.addHeader("Qty Issue","5%");
        ctrlist.addHeader(textListOrderItem[language][14],"5%");
        ctrlist.addHeader(textListOrderItem[language][15],"5%");
        //ctrlist.addHeader("Unit Stock","8%");
        
        if(privShowQtyPrice){
            /*ctrlist.addHeader("Harga Beli","5%");
            ctrlist.addHeader(textListOrderItem[language][5],"8%");
            ctrlist.addHeader(textListOrderItem[language][11],"5%");
            ctrlist.addHeader(textListOrderItem[language][12],"5%");
            ctrlist.addHeader(textListOrderItem[language][13],"8%");
            ctrlist.addHeader(textListOrderItem[language][6],"8%");*/
        }
        
       // ctrlist.addHeader(textListOrderItem[language][8],"9%");
       // ctrlist.addHeader(textListOrderItem[language][18],"9%");
       // if(privShowQtyPrice){
       //     ctrlist.addHeader(textListOrderItem[language][9],"10%");
       // }
        
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

        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            MatCurrency matCurrency = (MatCurrency)temp.get(3);
            Vector rowx = new Vector();
            start = start + 1;
            Unit unitRequest = new Unit();
            double qtyIssue=0.0;
            try{
                unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
                qtyIssue = PstPurchaseOrderItem.getQtyIssue(mat.getOID(), poCode);
            }catch(Exception e){}
            
            double sisa = poItem.getQuantity() - poItem.getResiduQty();
            
            rowx.add(""+(start));
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"_"+i+"\" value=\""+mat.getOID()+""+
                    "\"><input indextab=\"1\" type=\"hidden\" size=\"30\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"form-control\" onkeydown=\"javascript:keyDownCheck(event)\"><div align=\"center\">"+mat.getSku()+"</div>");
            rowx.add("<input type=\"hidden\" size=\"80\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"form-control\" onKeyDown=\"javascript:keyDownCheck(event)\"><div align=\"center\">"+mat.getName()+"</div>"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+poItem.getUnitId()+""+"\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"_"+i+"\" value=\""+poItem.getUnitId()+""+"\">");
            
            if(bEnableExpiredDate){
                     rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE]+"_"+i, new Date(), 1, -5, "formElemen", ""));
            }
            //add opie-eyek 20140108 untuk konversi satuan
            rowx.add(""+poItem.getQtyRequest());
            rowx.add(""+qtyIssue);
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"_"+i+"\" value=\""+qtyIssue+"\" class=\"form-control\" onkeyup=\"javascript:changeAll(this.value,'"+unit.getOID()+"',"+i+")\"></div>");
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>"
                    + "<input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i+"\" value=\""+poItem.getOrgBuyingPrice()+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonvAll(this.value,"+i+")\"></div>"
                    + "<div align=\"right\"><input indextab=\"2\" type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"_"+i+"\" value=\""+poItem.getPrice()+"\" onkeyup=\"javascript:cntTotalAll(this, event,"+i+")\" class=\"form-control\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                    + "<div align=\"right\"><input indextab=\"3\" type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\""+poItem.getDiscount()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>"
                    + "<div align=\"right\"><input indextab=\"4\" type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\""+poItem.getDiscount2()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>"
                    + "<div align=\"right\"><input indextab=\"5\" type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"_"+i+"\" value=\""+poItem.getDiscNominal()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>"
                    + "<div align=\"right\"><input indextab=\"6\" type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"_"+i+"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event,"+i+")\" style=\"text-align:right\"></div>"
                    + "<div align=\"right\"><input indextab=\"7\" type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY]+"_"+i+"\" value=\""+poItem.getQuantity()+"\" class=\"form-control\" onkeyup=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\"></div>" +
                      "<input type=\"hidden\" size=\"15\" name=\"FRM_FIELD_RESIDUE_QTY"+"_"+i+"\" value=\""+sisa+"\">"
                    + "<div align=\"center\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"_"+i+"\" value=\"0\"></div>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost"+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"form-control\" readOnly>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\""+poItem.getPrice()*qtyIssue+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotalAll(this, event,"+i+")\" readOnly></div>");

            if(privShowQtyPrice){

            }else{
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+poItem.getUnitId()+""+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i +"\" value=\""+poItem.getPriceKonv()+"\" class=\"form-control\" onkeyup=\"javascript:changePriceKonvAll(this.value,"+i+")\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"_"+i+"\" value=\""+poItem.getOrgBuyingPrice()+"\" onkeyup=\"javascript:cntTotal(this, event)\" class=\"form-control\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+oidCurrency+""+"\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\""+poItem.getDiscount()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\""+poItem.getDiscount2()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"_"+i+"\" value=\""+poItem.getDiscNominal()+"\" class=\"form-control\" onKeyUp=\"javascript:cntTotalAll(this, event,"+i+")\" style=\"text-align:right\">"
                    + "<input  type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"_"+i+"\" value=\""+""+"\" class=\"form-control\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\">"
                    + "<input type=\"hidden\" size=\"15\" name=\"total_cost"+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"form-control\" readOnly>"+
                      "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\""+poItem.getTotal()+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotalAll(this, event,"+i+")\" readOnly>");
            }
            
            lstData.add(rowx);
        }
        return ctrlist.drawBootstrap();
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
    ctrlMatReceiveItem.actionSaveAllOutlet(request,vectPO,oidReceiveMaterial);
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


%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title
><script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
    document.frm_recmaterial.command.value=comm;
    document.frm_recmaterial.hidden_receive_id.value=oid;
    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd(){
    document.frm_recmaterial.hidden_receive_item_id.value="0";
    document.frm_recmaterial.command.value="<%=Command.ADD%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    if(compareDateForAdd()==true)
            document.frm_recmaterial.submit();
}

function cmdEdit(oidReceiveMaterialItem){
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdAsk(oidReceiveMaterialItem){
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.ASK%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdSave() {
    var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
    var residueQty = document.frm_recmaterial.FRM_FIELD_RESIDUE_QTY.value;

    if(parseFloat(qty) <= parseFloat(residueQty)) {
        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
        document.frm_recmaterial.submit();
    }
    else {
       alert("Quantity more than residue quantity!");
    }
}

function change(value){
     document.frm_recmaterial.hidden_qty_input.value=value
     var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
     showData(oidUnit);
     var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
     //alert("ss"+qty);
     var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
     var total = cost*qty;
     document.frm_recmaterial.total_cost.value=parseFloat(total);
     document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value=parseFloat(total);

}
function changePriceKonv(value, e){
    var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
    var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.value,guiDigitGroup,guiDecimalSymbol);
    var total = cost/qty;
    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value=parseFloat(total);
    var qtyx = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
    var costx = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
    var totalx = costx*qtyx;
    document.frm_recmaterial.total_cost.value=parseFloat(totalx);
    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value=parseFloat(totalx);
    
    if (e.keyCode == 13) {
         document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    }
   
}

function cmdConfirmDelete(oidReceiveMaterialItem){
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

// add by fitra 17-05-2014
function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid) ;
else
return cmdEdit(oid);
}

function cmdCancel(oidReceiveMaterialItem){
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdHargaJual(oidMaterial) {
    var strvalue  = "<%=approot%>/master/material/material_main.jsp?command=<%=Command.EDIT%>"+
                    "&hidden_material_id="+oidMaterial+
                    "&mat_code="+document.frm_recmaterial.matCode.value+
                     "&txt_materialname="+document.frm_recmaterial.matItem.value;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function cmdBack(){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.start_item.value = 0;
    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
    //document.frm_recmaterial.action="m_src_receive_material.jsp";
    document.frm_recmaterial.submit();
}

function SaveAll(){
        document.frm_recmaterial.command.value="<%=Command.SAVEALL%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
        document.frm_recmaterial.submit();
}

function cmdCheck(){
    var strvalue  = "materialpodosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+document.frm_recmaterial.matCode.value+
                    "&oidPurchaseOrder=<%=rec.getPurchaseOrderId()%>";
    window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function keyDownCheck(e){
    if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdCheck();
    }
}


function changeFocus(element){
    if(element.name == "matCode") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
    }
    else if(element.name == "<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>") {
        cmdSave();
    }
    else {
        cmdSave();
    }
}

function cntTotal(element, evt){
   var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
    var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
    var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value,guiDigitGroup);
    var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value,guiDigitGroup,guiDecimalSymbol);

    if(price=="") { price = 0; }
    if(forwarder_cost == "") { forwarder_cost = 0; }

    if(isNaN(lastDisc) || (lastDisc==""))
        lastDisc = 0.0;
    if(isNaN(lastDisc2) || (lastDisc2==""))
        lastDisc2 = 0.0;
    if(isNaN(lastDiscNom) || (lastDiscNom==""))
        lastDiscNom = 0.0;

    var totaldiscount = price * lastDisc / 100;
    var totalMinus = price - totaldiscount;
    var totaldiscount2 = totalMinus * lastDisc2 / 100;
    var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
    //var lastTotal = qty * totalCost;

    if(!(isNaN(qty)) && (qty != '0')) {
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
    document.frm_recmaterial.command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListPrev(){
    document.frm_recmaterial.command.value="<%=Command.PREV%>";
    document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListNext(){
    document.frm_recmaterial.command.value="<%=Command.NEXT%>";
    document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListLast(){
    document.frm_recmaterial.command.value="<%=Command.LAST%>";
    document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdBackList(){
    document.frm_recmaterial.command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.action="receive_wh_supp_po_material_list.jsp";
    document.frm_recmaterial.submit();
}

function gostock(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.rec_type.value = 1;
    document.frm_recmaterial.type_doc.value = 1;
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.action="rec_wh_stockcode.jsp";
    document.frm_recmaterial.submit();
}


function checkBonus(){
    //var checkBook =document.frm_recmaterial.checkbox.value;
    //alert("hekkkii");
    if(document.frm_recmaterial.typeBonus.checked){
            //alert("hekkkii");
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>.value="1";
    }
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../../../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../../../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../../../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../../../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <!--link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css"-- />
        <!-- Daterange picker -->
        <!--link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" /-->
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
        
        

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
<!-- #EndEditable -->
<script src="../../../styles/jquery.min.js"></script>
<script type="text/javascript">
function showData(value){

   var qtyInput = document.frm_recmaterial.hidden_qty_input.value;
   var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value;
   var oidKonversiUnit=value;
   //alert("showdata");
   checkAjax(oidKonversiUnit,oidUnit,qtyInput);
}

function checkAjax(oidKonversiUnit,oidUnit, qtyInput){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"",
    type : "POST",
    async : false,
    success : function(data) {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value=data;
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
    }
});
}

function changeAll(valuex,oidKonv,countx){
if(valuex!=0){
     switch(countx){
         <%for(int i=0; i<vectPO.size(); i++){%>
             case <%=""+i%>:
                var inputqty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>_<%=""+i%>.value;
                var oidUnitkonv = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>_<%=""+i%>.value;
                var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>_<%=""+i%>.value;
                var price = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                
                $.ajax({
                    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidUnitkonv+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>="+inputqty+"",
                    type : "POST",
                    async : false,
                    success : function(data) {
                        var sisa = parseFloat(document.frm_recmaterial.FRM_FIELD_RESIDUE_QTY_<%=""+i%>.value);
                        if(data<=sisa){
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value=data;
                            
                            var total = price*data;
                            
                            var totalstock = total/data;
                            
                            if(isNaN(value)){
                                total=0;
                            }
                            if(isNan(value)){
                                totalstock=0;
                            }
                            
                            document.frm_recmaterial.total_cost_<%=""+i%>.value=parseFloat(total);
                            
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value=parseFloat(total);
                            
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value=parseFloat(totalstock);
                            //alert(totalstock);
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.focus();

                        }else{
                            alert("Input : "+data+"Residu : "+sisa)
                            alert("maaf qty yang di input melebihi qty order");
                            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>_<%=""+i%>.value="0";
                            
                        }
                        
                    }
                });
                
             break;
        <%}%>
    }     
  }
}



function cntTotalAll(element, evt,countx){
     switch(countx){
         <%for(int i=0; i<vectPO.size(); i++){%>
             case <%=""+i%>:
                    
                    var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value,guiDigitGroup);
                    var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value,guiDigitGroup);
                    var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>_<%=""+i%>.value,guiDigitGroup);
                    
                    var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                    var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                    var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                    
                    if(price=="") { price = 0; }
                    if(forwarder_cost == "") { forwarder_cost = 0; }

                    if(isNaN(lastDisc) || (lastDisc==""))
                        lastDisc = 0.0;
                    if(isNaN(lastDisc2) || (lastDisc2==""))
                        lastDisc2 = 0.0;
                    if(isNaN(lastDiscNom) || (lastDiscNom==""))
                        lastDiscNom = 0.0;

                    var totaldiscount = price * lastDisc / 100;
                    var totalMinus = price - totaldiscount;
                    var totaldiscount2 = totalMinus * lastDisc2 / 100;
                    var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;

                    if(!(isNaN(qty)) && (qty != '0')) {
                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value = parseFloat(totalCost) * qty;
                        document.frm_recmaterial.total_cost_<%=""+i%>.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
                    }else {
                        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.focus();
                    }
             break;
        <%}%>
    } 
    
}


function changePriceKonvAll(value,countx){
    switch(countx){
         <%for(int i=0; i<vectPO.size(); i++){%>
             case <%=""+i%>:
                var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                var cost = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                var total = cost/qty;
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value=parseFloat(total);
                var qtyx = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>_<%=""+i%>.value;
                var costx = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>_<%=""+i%>.value,guiDigitGroup,guiDecimalSymbol);
                var totalx = costx*qtyx;
                document.frm_recmaterial.total_cost_<%=""+i%>.value=parseFloat(totalx);
                document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>_<%=""+i%>.value=parseFloat(totalx);
  
                break;
        <%}%>
    } 
}



function showDataAll(oidKonv,value,countx){
     changeAll(oidKonv,value,countx);
}


</script>
<!-- #BeginEditable "headerscript" -->
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
<!-- #EndEditable --> 
</head> 

<body class="skin-blue">
        <%@ include file = "../../../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../../../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small><%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][5]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>		  </small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frm_recmaterial" method ="post" action="">
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
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row">
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"> <%=textListOrderHeader[SESS_LANGUAGE][0]%> </label><br />
                                                
                                                <b><%=rec.getRecCode()%></b>
                                                
                                            </div>     
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label><br />
                                                <%=ControlDate.drawDateWithBootstrap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], rec.getReceiveDate(), 1, -5, "form-control-date", "disabled=\"true\"")%>
                                                
                                            </div> 
                                           <div class="form-group">
                                                    
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label><br />    
                                               
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
                                                    <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "form-control")%>

                                           </div>  
                                           
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][9]%></label><br />   
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
                                                    <%=ControlCombo.drawBoostrap("CURRENCY_CODE","formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled")%>
                                                &nbsp;&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][15]%>&nbsp;&nbsp;
                                                <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="formElemen" size="10" value="<%=rec.getTransRate()%>" disabled class="form-control">
                                            </div>
                                        </div>
                                    <!-- end of col-md-3-->
                                        <div class="col-md-5">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label><br />
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
                                                    <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"disabled=\"true\"","form-control")%> 
                                            </div>
                                                    <!-- adding term payment -->
                                                    <!-- by Mirahu 20120302 -->
                                            
                                            <div class="form-group">        
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label><br />

                                                <input type="text" name="txt_ponumber"  value="<%= po.getPoCode() %>" class="form-control" size="20" disabled="true">        
                                            </div>
                                            
                                            
                                            <div class="form-group">     
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label><br />  
                                                <input type="text"  class="form-control" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" readonly>
                                            </div>
                                            
                                        </div>
                                    <!-- end of col-md-5-->
                                        <div class="col-md-4">
                                             
                                            <div class="form-group">       
                                                     <%if(privShowQtyPrice){%>
                                                         <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][13]%></label><br />
                                                       
                                                         <%
                                                               Vector val_terms = new Vector(1,1);
                                                               Vector key_terms = new Vector(1,1);
                                                               for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                                                   val_terms.add(String.valueOf(d));
                                                                     key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                                                }
                                                                    String select_terms = ""+rec.getTermOfPayment();
                                                          %>
                                                          <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"disabled=\"true\"","form-control")%> </td>
                                                    <%} else { %>
                                                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>" type="hidden" class="form-control" style="text-align:right" size="5" value="<%=rec.getTermOfPayment()%>"></td>
                                                    <%}%>

                                            </div>
                                            <div class="form-group">   
                                            <%if(privShowQtyPrice){%>
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][14]%></label><br />
                                                <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="form-control" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>" readOnly></td>
                                                
                                                 <%}else{ %>
                                                <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="form-control" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td> 
                                            <%}%> 
                                            </div>    
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label><br /> 
                                                <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="2" tabindex="4"  disabled="true"><%=rec.getRemark()%></textarea>        
                                            </div>       

                                        </div>
                                    <!-- end of col-md-4-->
                                    
                                    
                                </div>
                               <!-- end of row-->
                               
                               
                                
                                <div class="row">
                                    <div class="col-md-12">
                                     <%Vector listError = new Vector(1,1);%>
                                     
                                     <%if(iCommand==Command.ADDALL){%>
                                           
                                                <%
                                                  try {
                                                      %>
                                                      <
                                                          <%=drawListMaterial(SESS_LANGUAGE,vectPO,0,privShowQtyPrice,1,frmMatReceiveItem,rec.getCurrencyId(),po.getPoCode())%>
                                                      <%
                                                      %>
                                                  
                                                      <%
                                                      } catch(Exception e)	{
                                                          System.out.println(e);
                                                      }
                                                        %>
                                          
                                         <%}else{%>
                                    </div>
                                    
                                    <div class="col-md-12">
                                       <%
                                          try {
                                              %><td height="22" valign="middle" colspan="3">
                                              <%
                                              Vector list = drawListRetItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,po.getExchangeRate(),readonlyQty,typeOfBusiness,rec.getCurrencyId(), approot);
                                              out.println(""+list.get(0));
                                              listError = (Vector)list.get(1);
                                              %>

                                              <%
                                              } catch(Exception e)	{
                                                  System.out.println(e);
                                              }
                                            %>
                                    </div>
                                   <%--      
                                        <div class="col-md-12">
                                            Bonus Item
                                        </div>  

                                        <div class="col-md-12">
                                            <%
                                          try {
                                              %>
                                              <td height="22" valign="middle" colspan="3">
                                              <%
                                              Vector list = drawListRetBonusItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveBonusItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,po.getExchangeRate(),readonlyQty,typeOfBusiness,rec.getCurrencyId(), approot);
                                              out.println(""+list.get(0));
                                              listError = (Vector)list.get(1);
                                              %>
                                              </td>
                                              <%
                                              } catch(Exception e)	{
                                                  System.out.println(e);
                                              }
                                              %>
                                        </div>
                                    --%>  
                                    
                                    <%if(privShowQtyPrice){%>
                                    <div class="col-md-12">
                                        
                                        <%
                                            out.println("&nbsp;&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'><blink><b>[edit]</b></blink></font>&nbsp; : <b>Edit Harga Jual (Jika Harga Beli + PPN di Master Data Lebih Kecil dari Harga Beli di Dokument Penerimaan Ini)</b><br>");
                                        %>
                                    </div>
                                         <%}%>
                                    <%}%>   
                                    
                                     <div class="col-md-12">
                                         
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
                                       
                                    </div>
                                        
                                    <div class="col-md-12">
                                        
                                        <%
                                        for(int k=0;k<listError.size();k++){
                                            if(k==0)
                                                out.println(listError.get(k)+"<br>");
                                            else
                                                out.println("&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                        }
                                        %>
                                    </div>
                                    
                                    
                                    
                                       
                                    <%String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
                                    if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                    %>
                                    
                                    <div class="col-md-12">
                                    
                                         <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                            
                                             <button  onclick="javascript:cmdAdd()" type="submit" class="btn btn-primary">Tambah</button>
                                          <% } %>
                                          
                                             <button  onclick="javascript:cmdBack()" type="submit" class="btn btn-primary">Kembali</button>
                                             
                                    </div>         
                                          <%
                                             } else {
                                                if(iCommand!=Command.ADDALL){
                                                    out.println(strDrawImage);
                                                }else{%>
                                    <div class="col-md-12">
                                        <%if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                            <button  onclick="javascript:SaveAll()" type="submit" class="btn btn-primary">Save All</button>
                                        
                                        <%}%>
                                            <button  onclick="javascript:cmdBack()" type="submit" class="btn btn-primary">Kembali</button>
                                    </div>
                                            <%
                                         }
                                      }
                                      %>  
                                   
                                </div>
                                    
			   </div>
                        </div>
                    </form>
                    <script language="javascript">
                        document.frm_recmaterial.matCode.focus();
                    </script>                  
                                      
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
       <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../../../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../../../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../../../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../../../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../../../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../../../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../../../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../../../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../../../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../../../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../../../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../../../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>
