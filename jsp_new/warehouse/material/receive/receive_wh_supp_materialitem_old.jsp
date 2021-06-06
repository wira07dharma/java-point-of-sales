<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstReceiveStockCode,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.posbo.entity.warehouse.MatReceiveItem,
                   com.dimata.posbo.entity.warehouse.PstMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.warehouse.MatReceive,
                   com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
                   com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>
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
<%!

static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;


public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang ..."},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item ..."}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal","Supplier","Status","Keterangan","Nota Supplier","Total PPN","Mata Uang","Terms","Days","Rate"},
    {"No","Location","Date","Supplier","Status","Remark","Supplier Invoice","Total VAT","Currency","Terms","Days","Rate"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",//10
    "Diskon1 %","Diskon2 %","Discount Nominal","Qty Entri","Unit Konversi","Hapus","Bonus"},//17
   {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
    "Discount2 %","Disc. Nominal","Qty Entri","Unit Konversi","Delete","Bonus"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

//public static final String textListOrderItem[][] = {
   //{"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
   //{"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost"}
//};

/**
* this method used to maintain poMaterialList
*/
public Vector drawListRetItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start,boolean privShowQtyPrice,String readOnlyQty, String approot) {
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
        ctrlist.addHeader(textListOrderItem[language][3],"8%");
    }
    
    ctrlist.addHeader(textListOrderItem[language][14],"5%");
    ctrlist.addHeader(textListOrderItem[language][15],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][5],"7%");
        ctrlist.addHeader(textListOrderItem[language][11],"5%");
        ctrlist.addHeader(textListOrderItem[language][12],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"7%");
        ctrlist.addHeader(textListOrderItem[language][6],"7%");
        ctrlist.addHeader(textListOrderItem[language][8],"7%");//qty
        ctrlist.addHeader(textListOrderItem[language][17],"7%");
        ctrlist.addHeader(textListOrderItem[language][9],"7%");
    }else{
         ctrlist.addHeader(textListOrderItem[language][8],"9%");
         ctrlist.addHeader(textListOrderItem[language][17],"7%");
    }
        ctrlist.addHeader(textListOrderItem[language][16],"9%");
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
    if(start<0) {
        start=0;
    }

    for(int i=0; i<objectClass.size(); i++)	{
        Vector temp = (Vector)objectClass.get(i);
        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        CurrencyType currencyType = (CurrencyType)temp.get(3);
        rowx = new Vector();
        start = start + 1;
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        if (recItemId == recItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            //update opie-eyek 20130812
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                }else{
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit]</a>");
                }
            }else{
                 rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            }
            
            if(bEnableExpiredDate){
            rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate()==null) ? new Date() : recItem.getExpiredDate(), 5, -5, "formElemen", ""));
            }

            //add opie-eyek 20140108 untuk konversi satuan
            if(readOnlyQty=="readonly"){
                 rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" "+readOnlyQty+">");
            }else{
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null,""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+recItem.getCurrencyId()+"><div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                
                if(readOnlyQty=="readonly"){
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" "+readOnlyQty+" ></div>");
                }else{
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                }
                
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"hiddenText\" readOnly></div>"+
                        "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\">");
            }else{
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"hiddenText\" readOnly></div>"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\">"
                        + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+recItem.getCurrencyId()+"><div align=\"right\">"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
            }
            rowx.add("<div align=\"right\"></div>");//hapus
        } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            //update opie-eyek 20130812
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
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED) {
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
                rowx.add("<div align=\"right\"></div>");//bonus
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                 
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                 rowx.add("<div align=\"right\"></div>");//bonus
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</div>");
            }
            // add by fitra 17-05-2014
            rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            
        }
        lstData.add(rowx);
    }

    rowx = new Vector();
    if(readOnlyQty=="readonly"){
        
    }else{
        if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {
            rowx.add(""+(start+1));
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                    "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            rowx.add("<input type=\"text\" size=\"30\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            if(bEnableExpiredDate){
            rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 5, -5, "formElemen", ""));
            }

            //add opie-eyek 20140108 untuk konversi satuan
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" </div>");
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");

            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");

            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\"\">" +
                    "<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
                
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"+
                        "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                 
            }else{
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\"\">"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+""+"\" class=\"hiddenText\" readOnly>"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" >Bonus</div>");
            }
            rowx.add("");
            lstData.add(rowx);
        }
    }

    list.add(ctrlist.draw());
    list.add(listError);
    return list;
}


//bonus item
public Vector drawListRetBonusItem(int language,int iCommand,FrmMatReceiveItem frmObject,MatReceiveItem objEntity,Vector objectClass,long recItemId,int start,boolean privShowQtyPrice,String readOnlyQty, String approot) {
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
    ctrlist.addHeader(textListOrderItem[language][3],"8%");
    }
    ctrlist.addHeader(textListOrderItem[language][14],"5%");
    ctrlist.addHeader(textListOrderItem[language][15],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"5%");
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][5],"7%");
        ctrlist.addHeader(textListOrderItem[language][11],"5%");
        ctrlist.addHeader(textListOrderItem[language][12],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"7%");
        ctrlist.addHeader(textListOrderItem[language][6],"7%");
        ctrlist.addHeader(textListOrderItem[language][8],"7%");//qty
        ctrlist.addHeader(textListOrderItem[language][17],"7%");
        ctrlist.addHeader(textListOrderItem[language][9],"7%");
    }else{
         ctrlist.addHeader(textListOrderItem[language][8],"9%");
         ctrlist.addHeader(textListOrderItem[language][17],"7%");
    }
        ctrlist.addHeader(textListOrderItem[language][16],"9%");
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
    if(start<0) {
        start=0;
    }

    for(int i=0; i<objectClass.size(); i++)	{
        Vector temp = (Vector)objectClass.get(i);
        MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        CurrencyType currencyType = (CurrencyType)temp.get(3);
        rowx = new Vector();
        start = start + 1;
        double totalForwarderCost = recItem.getForwarderCost() * recItem.getQty();
        Unit untiKonv = new Unit();
        try{
            untiKonv = PstUnit.fetchExc(recItem.getUnitKonversi());
        }catch(Exception ex){

        }
        if (recItemId == recItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+recItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            //update opie-eyek 20130812
            if(privShowQtyPrice){
                if(mat.getCurrBuyPrice()<recItem.getCost()){
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><img src='../../../images/DOTreddotANI.gif'><blink><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\"><font color='#FF0000'>[Edit]</font></a></blink>");
                }else{
                    rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly><a href=\"javascript:cmdHargaJual('"+String.valueOf(recItem.getMaterialId())+"')\">&nbsp;&nbsp;[Edit]</a>");
                }
            }else{
                 rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            }
            
            if(bEnableExpiredDate){
            rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate()==null) ? new Date() : recItem.getExpiredDate(), 5, -5, "formElemen", ""));
            }

            //add opie-eyek 20140108 untuk konversi satuan
            if(readOnlyQty=="readonly"){
                 rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\" "+readOnlyQty+"></div>"
                      + "<input type=\"text\" size=\"7\" name=\"ccc\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" "+readOnlyQty+">");
            }else{
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+recItem.getQtyEntry()+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
            }
            
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null,""+untiKonv.getOID(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+recItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            if(privShowQtyPrice){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+recItem.getCurrencyId()+"><div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                
                if(readOnlyQty=="readonly"){
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" "+readOnlyQty+" ></div>");
                }else{
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                }
                
                //rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"1\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" checked>Bonus</div>");
                
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"hiddenText\" readOnly></div>"+
                        "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\">");
            }else{
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"total_cost\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"\" class=\"hiddenText\" readOnly></div>"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getTotal())+"\">"
                        + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+recItem.getCurrencyId()+"><div align=\"right\">"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISC_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"1\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" checked>Bonus</div>");
                
            }
            rowx.add("<div align=\"right\"></div>");//hapus
        } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            //update opie-eyek 20130812
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
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED) {
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
                rowx.add("<div align=\"left\"><input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"\" value=\"0\"> <input type=\"checkbox\"  name=\"typeBonus\" value=\"1\" onClick=\"javascript:checkBonus()\" checked>Bonus</div>");
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                 
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                rowx.add("<div align=\"right\">Bonus</div>");//bonus
            }
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getTotal()+totalForwarderCost)+"</div>");
            }
            // add by fitra 17-05-2014
            rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            
        }
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
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";
FRMHandler.deFormatStringDecimal(",");
/**
* purchasing pr code and title
*/
String recCode = "";//i_pstDocType.getDocCode(docType);
String retTitle = "Penerimaan Barang";//i_pstDocType.getDocTitle(docType);
String recItemTitle = retTitle + " Item";

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
iErrCode = ctrlMatReceiveItem.action(iCommand,oidReceiveMaterialItem,oidReceiveMaterial,userName, userId);
FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
msgString = ctrlMatReceiveItem.getMessage();
String materialname = FRMQueryString.requestString(request,"txt_materialname");


String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                         " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";

String orderClauseItem = " RMI."+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    startItem = ctrlMatReceiveItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}


/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */
if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {
    iCommand = Command.ADD;
    /** agar form add item tampil pada list paling akhir */
    startItem = ctrlMatReceiveItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);
}

if(startItem<0)
  startItem=0;
if(recordToGetItem<0)
  recordToGetItem=10;

Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem,recordToGetItem,whereClauseItem, orderClauseItem);//PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
if(listMatReceiveItem.size()<1 && startItem>0) {
    if(vectSizeItem-recordToGetItem > recordToGetItem) {
        startItem = startItem - recordToGetItem;
    } else {
        startItem = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    //listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
if(startItem<0)
  startItem=0;
if(recordToGetItem<0)
  recordToGetItem=10;
    listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem,recordToGetItem,whereClauseItem, orderClauseItem);
}

String whereClauseBonusItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                         " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=1";
Vector listMatReceiveBonusItem = PstMatReceiveItem.listVectorRecItemComplete(startItem,0,whereClauseBonusItem, orderClauseItem);

String readonlyQty="";
if(typeOfBusiness.equals("3") && privFinal==true){
    readonlyQty="readonly";
}


if(iCommand==Command.SAVE && iErrCode == 0) {
		iCommand = Command.ADD;
        oidReceiveMaterialItem=0;
}

// add by fitra 17-05-2014

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
    document.frm_recmaterial.command.value=comm;
    document.frm_recmaterial.hidden_receive_id.value=oid;
    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
    document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------

function cmdEditHargaJual2(oid)
{
    document.frm_recmaterial.hidden_material_id.value=oid;
    document.frm_recmaterial.start.value=0;
    document.frm_recmaterial.approval_command.value="<%=Command.APPROVE%>";
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.action="<%=approot%>/master/material/material_main.jsp";
    document.frm_recmaterial.submit();
}

function cmdEditHargaJual(oid){
    document.frmvendorsearch.command.value="<%=Command.EDIT%>";
    document.frmvendorsearch.action="<%=approot%>/master/material/material_main.jsp";
    document.frmvendorsearch.submit();
}

function cmdAdd() {
    document.frm_recmaterial.hidden_receive_item_id.value="0";
    document.frm_recmaterial.command.value="<%=Command.ADD%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    if(compareDateForAdd()==true)
            document.frm_recmaterial.submit();
}

function cmdSelectItem(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty){
   document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>.value = matOid;
   document.forms.frm_recmaterial.matCode.value = matCode;
   document.forms.frm_recmaterial.matItem.value = matItem;
   document.forms.frm_recmaterial.matUnit.value = matUnit;
   document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value = matPrice;
   document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value = matUnitId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>.value = matCurrencyId;
    //self.opener.document.forms.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value = stockQty;
    //self.opener.document.forms.frm_recmaterial.matCurrency.value = matCurrCode;
    <%if(privShowQtyPrice){%>
      changeFocus(document.forms.frm_recmaterial.matCode);
    <%}else{%>
         changeFocus(document.forms.frm_recmaterial.matQty);
    <%}%>
 
}

function cmdEdit(oidReceiveMaterialItem) {
     <%
    //update opie-eyek 20130812
    if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
    %>
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
     <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function cmdAsk(oidReceiveMaterialItem) {
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.ASK%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdSave() {
    document.frm_recmaterial.command.value="<%=Command.SAVE%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function keyDownCheck(e){
    
   //if (e.keyCode == 13 || trap ==1){
     //  cmdCheck();
       //}
   
   var trap = document.frm_recmaterial.trap.value;
    if (e.keyCode == 13 && trap==0) {
    document.frm_recmaterial.trap.value="1";
    }
    
    // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_recmaterial.matItem.value == "" ){
        document.frm_recmaterial.trap.value="0";
        cmdCheck();
    }
    
   if (e.keyCode == 13 && trap=="1") {
      document.frm_recmaterial.trap.value="0";
      cmdCheck();
    }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_recmaterial.txt_materialname.value="";
    }
   
}


function cmdConfirmDelete(oidReceiveMaterialItem) {
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
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

function cmdCancel(oidReceiveMaterialItem) {
    document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdBack() {
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.start_item.value = 0;
    document.frm_recmaterial.action="receive_wh_supp_material_edit.jsp";
    document.frm_recmaterial.submit();
}


function sumPrice() {
}

var winSrcMaterial;
function cmdCheck() {
    var strvalue  = "materialdosearch.jsp?command=<%=Command.FIRST%>"+
                    "&location_id=<%=rec.getLocationId()%>"+
                    "&mat_code="+document.frm_recmaterial.matCode.value+
                     "&txt_materialname="+document.frm_recmaterial.matItem.value+
                    "&mat_vendor="+document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>.value+
                    "&currency_id=<%=rec.getCurrencyId()%>"+
                    "&trans_rate=<%=rec.getTransRate()%>";
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}

function cmdHargaJual(oidMaterial) {
    var strvalue  = "<%=approot%>/master/material/material_main.jsp?command=<%=Command.EDIT%>"+
                    "&hidden_material_id="+oidMaterial+
                    "&mat_code="+document.frm_recmaterial.matCode.value+
                     "&txt_materialname="+document.frm_recmaterial.matItem.value;
    winSrcMaterial = window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}
}


function keyDownCheck1(e){
   if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdCheck();
   }
}

function changeFocus(element){
    if(element.name == "matCode") {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>.focus();
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
    }else if(element.name == "matQty"){
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
    }else {
        cmdSave();
    }
}

function cntTotal(element, evt) {
    var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
    var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
    var forwarder_cost = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>.value,guiDigitGroup);
    var lastDisc = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc2 = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDiscNom = cleanNumberFloat(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>.value,guiDigitGroup,guiDecimalSymbol);

    if(qty<0.0000){

          document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value=0;

          return;

        }


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
        //document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(price) * qty;
        //document.frm_recmaterial.total_cost.value = (parseFloat(price) + parseFloat(forwarder_cost)) * qty;
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(totalCost) * qty;
        document.frm_recmaterial.total_cost.value = (parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty;
    }
    else {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
    }

    if (evt.keyCode == 13) {
        changeFocus(element);
    }
}

function change(value){
     document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value=value;
     document.frm_recmaterial.hidden_qty_input.value=value;
     
}

function cmdListFirst(){
    document.frm_recmaterial.command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListPrev(){
    document.frm_recmaterial.command.value="<%=Command.PREV%>";
    document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListNext(){
    document.frm_recmaterial.command.value="<%=Command.NEXT%>";
    document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdListLast(){
    document.frm_recmaterial.command.value="<%=Command.LAST%>";
    document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
    document.frm_recmaterial.action="receive_wh_supp_materialitem.jsp";
    document.frm_recmaterial.submit();
}

function cmdBackList(){
    document.frm_recmaterial.command.value="<%=Command.FIRST%>";
    document.frm_recmaterial.action="receive_wh_supp_material_list.jsp";
    document.frm_recmaterial.submit();
}

function gostock(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.rec_type.value = 2;
    document.frm_recmaterial.type_doc.value = 2;
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
//    document.frm_recmaterial.action="rec_df_stockcode.jsp";
    document.frm_recmaterial.action="rec_wh_stockcode.jsp";
    document.frm_recmaterial.submit();
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

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
<script src="../../../styles/jquery.min.js"></script>
<script type="text/javascript">
function showData(value){

   var qtyInput = document.frm_recmaterial.hidden_qty_input.value;
   var oidUnit = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>.value;
   var oidKonversiUnit=value;
    
   checkAjax(oidKonversiUnit,oidUnit,qtyInput);
}

function checkAjax(oidKonversiUnit,oidUnit, qtyInput){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"",
    type : "POST",
    async : false,
    success : function(data) {
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value=data;
        var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
        var value =cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
        if(isNaN(value)){
            value=0;
            //alert("nan");
        }
        var total = parseFloat(value)*qty;
        if(isNaN(total)){
            total=0;
        }
        //alert("hh "+ value);
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = parseFloat(total);
        //alert("sss: "+total+"value: "+value+"qty: "+qty);
        document.frm_recmaterial.total_cost.value=parseFloat(total);
        document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.focus();
    }
});
}

function checkBonus(){
    //var checkBook =document.frm_recmaterial.checkbox.value;
    //alert("hekkkii");
    if(document.frm_recmaterial.typeBonus.checked){
            //alert("hekkkii");
            document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>.value="1";
    }
}

</script>
<!-- #EndEditable -->
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


<%--autocomplate add By fitra--%>
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
          	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][6]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_recmaterial" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="rec_type" value="">
              <input type="hidden" name="type_doc" value="">
              <input type="hidden" name="matQty" value="">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_receive_item_id" value="<%=( (iCommand==Command.ADD || iCommand==Command.SAVE ) ? 0 : oidReceiveMaterialItem)%>">
              <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_qty_unit" value="">
              <input type="hidden" name="hidden_qty_input" value="">
                <input type="hidden" name="trap" value="">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr>
                  <td valign="top" colspan="3">&nbsp;
                  </td>
                </tr>
                <tr align="center">
                  <td colspan="3" class="title">
                    <table width="100%" border="0" cellpadding="1">
                      <tr>
                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%" align="left">
                          : <b><%=rec.getRecCode()%></b>
                        </td>
                        <td width="11%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="27%" valign="bottom">  :
                          <%
                            Vector obj_supplier = new Vector(1,1);
                            Vector val_supplier = new Vector(1,1);
                            Vector key_supplier = new Vector(1,1);
                            //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
                            String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                    " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                    " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                    " != "+PstContactList.DELETE;
                            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

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
                        <!--<td width="9%" align="right"><%//=textListOrderHeader[SESS_LANGUAGE][5]%>
                        <td width="18%" rowspan="4" align="right" valign="top">
                          <textarea name="<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3" disabled="true"><%//=rec.getRemark()%></textarea>
                        </td>-->
                        <td width="9%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                        <td width="1%">:</td>
                        <td width="18%" align="left" valign="top">
                            <%
                                Vector val_terms = new Vector(1,1);
                                Vector key_terms = new Vector(1,1);
                                for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                    val_terms.add(String.valueOf(d));
                                    key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                }
                                String select_terms = ""+rec.getTermOfPayment();
                            %>
                                <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"disabled=\"true\"","formElemen")%>
                        </td>

                      </tr>
                      <tr>
                        <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td width="27%" align="left"> : <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], rec.getReceiveDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>:
                          <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" readOnly>
                        </td>
                        <!--<td width="9%" align="right"></td>-->
                        <td width="9%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                        <td width="1%">:</td>
                        <td width="18%" align="left" valign="top">
                            <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>" readOnly>
                        </td>
                      </tr>
                      <tr>
                        <td width="8%" align="left"  valign="top"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td width="27%" align="left"  valign="top">:
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
                        <td valign="top"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                        <td valign="top">:
                          <%
                            Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                            Vector vectCurrVal = new Vector(1,1);
                            Vector vectCurrKey = new Vector(1,1);
                            for(int i=0; i<listCurr.size(); i++){
                                CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                vectCurrKey.add(currencyType.getCode());
                                vectCurrVal.add(""+currencyType.getOID());
                            }
                          %>
                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                         &nbsp;&nbsp;
                          <%=textListOrderHeader[SESS_LANGUAGE][11]%>&nbsp;&nbsp;
                          <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="formElemen" size="10" value="<%=rec.getTransRate()%>" >
                        </td>
                        <!--<td width="9%" align="right">&nbsp;</td>-->
                        <td width="9%" align="right" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="1%" valign="top">:</td>
                        <td width="18%" align="left" valign="bottom">
                            <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3" disabled="true"><%=rec.getRemark()%></textarea>
                        </td>
                      </tr>
                      <!--<tr>
                        <td align="left">&nbsp;</td>
                        <td align="left">&nbsp;</td>
                        <td align="center" valign="bottom">&nbsp;</td>
                        <td align="center" valign="bottom">&nbsp;</td>
                        <td align="right">&nbsp;</td>
                      </tr>-->
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
                                Vector list = drawListRetItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,readonlyQty, approot);
                                out.println(""+list.get(0));
                                listError = (Vector)list.get(1);
                            %>
                               </td>
                            <%
                            }
                            catch(Exception e) {
                                System.out.println(e);
                            }
                            %>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="22" valign="middle" colspan="3"></td>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="22" valign="middle" colspan="3">Bonus Item</td>
                            </tr>
                            <%Vector listErrorBonus = new Vector(1,1);%>
                            <tr align="left" valign="top">
                             <%
                                try {
                                %>
                                   <td height="22" valign="middle" colspan="3">
                                <%
                                    Vector listBonus = drawListRetBonusItem(SESS_LANGUAGE,iCommand,frmMatReceiveItem, recItem,listMatReceiveBonusItem,oidReceiveMaterialItem,startItem,privShowQtyPrice,readonlyQty, approot);
                                    out.println(""+listBonus.get(0));
                                    listErrorBonus = (Vector)listBonus.get(1);
                                %>
                                   </td>
                                <%
                                }
                                catch(Exception e) {
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
                              %>
                              </td>
                            </tr>
                            <tr align="left" valign="top">
                              <td height="22" colspan="3" valign="middle" class="errfont">
                              <%
                              for(int k=0;k<listErrorBonus.size();k++){
                                    if(k==0)
                                        out.println(listErrorBonus.get(k)+"<br>");
                                    else
                                        out.println("&nbsp;&nbsp;&nbsp;"+listErrorBonus.get(k)+"<br>");
                              }
                              %>
                              </td>
                            </tr>
                            <%if(privShowQtyPrice){%>
                                <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle">
                                        <%
                                            out.println("&nbsp;&nbsp;&nbsp;<img src='../../../images/DOTreddotANI.gif'><font color='#FF0000'><blink><b>[edit]</b></blink></font>&nbsp; : <b>Edit Harga Jual (Jika Harga Beli + PPN di Master Data Lebih Kecil dari Harga Beli di Dokument Penerimaan Ini)</b><br>");
                                        %>
                                    </td>
                                </tr>
                            <%}%>
                            <tr align="left" valign="top">
                              <td height="22" valign="middle" colspan="3">
                                <%
                                ctrLine.setLocationImg(approot+"/images");

                                // set image alternative caption
                                ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true));
                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_SAVE,true));
                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ASK,true));
                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_CANCEL,false));

                                ctrLine.initDefault();
                                ctrLine.setTableWidth("65%");
                                String scomDel = "javascript:cmdAsk('"+oidReceiveMaterialItem+"')";
                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidReceiveMaterialItem+"')";
                                String scancel = "javascript:cmdEdit('"+oidReceiveMaterialItem+"')";
                                ctrLine.setCommandStyle("command");
                                ctrLine.setColCommStyle("command");


                                // set command caption
                                ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true));
                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_SAVE,true));
                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ASK,true));
                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_DELETE,true));
                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_CANCEL,false));


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

                                String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);%>
                                
                                <%if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                %>
                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                  <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                    <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                                  <% } %>
                                    <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="25%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
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
                        <td colspan="3">&nbsp;</td>
                      </tr>
                      <tr>
                        <td colspan="2" valign="top">&nbsp;</td>
                        <td width="30%">
                          <table width="100%" border="0">
                            <tr>
                               <td width="44%"><div align="right"><%//=textListOrderHeader[SESS_LANGUAGE][7]%></div></td>
                               <td width="15%"><div align="right"></div></td>
                               <td width="41%"><div align="right">
                                 <!--<input type="text"  class="formElemen" name="<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%//=rec.getTotalPpn()%>"  size="15" style="text-align:right" readOnly>-->
                                 </div>
                               </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                      <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                      <tr>
                        <td colspan="2" valign="top">&nbsp; </td>
                        <td width="27%" valign="top">
                          <table width="100%" border="0">
                              <tr>
                                <td width="44%">
                                  <div align="right"><%//="TOTAL : "+recCode%></div>
                                </td>
                                <td width="15%">
                                  <div align="right"></div>
                                </td>
                                <td width="41%">
                                  <div align="right">
                                  <%
                                  String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                                  //out.println(Formater.formatNumber(PstMatReceiveItem.getTotal(whereItem),"##,###.00"));
                                  %>
                                  </div>
                                </td>
                              </tr>
                          </table>
                        </td>
                      </tr>
                      <tr>
                        <td colspan="3">&nbsp;</td>
                      </tr>
                      <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                      <tr>
                        <td colspan="3"></td>
                      </tr>
                      <tr>
                        <td colspan="3">&nbsp;</td>
                      </tr>
                      <%}%>
                      <%}%>
                      <tr>
                        <td colspan="3">
                          <%
                          /**ctrLine.setLocationImg(approot+"/images");

                          // set image alternative caption
                          ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_SAVE,true));
                          ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
                          ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_ASK,true));
                          ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_CANCEL,false));

                          ctrLine.initDefault();
                          ctrLine.setTableWidth("80%");
                          String scomDelMat = "javascript:main('"+oidReceiveMaterial+"','"+Command.ASK+"')";
                          String sconDelComMat = "javascript:main('"+oidReceiveMaterial+"','"+Command.DELETE+"')";
                          String scancelMat = "javascript:cmdEdit('"+oidReceiveMaterial+"','"+Command.EDIT+"')";
                          ctrLine.setSaveCommand("javascript:main('"+oidReceiveMaterial+"','"+Command.SAVE+"')");
                          ctrLine.setBackCommand("javascript:cmdBackList()");
                          ctrLine.setCommandStyle("command");
                          ctrLine.setColCommStyle("command");

                          // set command caption
                          ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_SAVE,true));
                          ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
                          ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_ASK,true));
                          ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_DELETE,true));
                          ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_CANCEL,false));

                          if(privDelete && privManageData){
                              ctrLine.setConfirmDelCommand(sconDelComMat);
                              ctrLine.setDeleteCommand(scomDelMat);
                              ctrLine.setEditCommand(scancelMat);
                          }else{
                              ctrLine.setConfirmDelCaption("");
                              ctrLine.setDeleteCaption("");
                              ctrLine.setEditCaption("");
                          }

                          if(privAdd==false && privUpdate==false){
                              ctrLine.setSaveCaption("");
                          }

                          if(privAdd==false){
                              ctrLine.setAddCaption("");
                          }

                          if(iCommand==Command.SAVE && frmMatReceive.errorSize()==0){
                              iCommand=Command.EDIT;
                          }

                          if(documentClosed){
                              ctrLine.setSaveCaption("");
                              ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retTitle,ctrLine.CMD_BACK,true)+" List");
                              ctrLine.setDeleteCaption("");
                              ctrLine.setConfirmDelCaption("");
                              ctrLine.setCancelCaption("");
                          }*/
                          %>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
            <script language="JavaScript">
                    
                    
                      // add By Fitra
                var trap = document.frm_recmaterial.trap.value;       
                document.frm_recmaterial.trap.value="0";

         
                document.frm_recmaterial.matItem.focus();
                
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
         document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate add by fitra--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>
<!-- #EndTemplate --></html>

