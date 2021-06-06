<%-- 
    Document   : storerequestitem
    Created on : Aug 3, 2014, 4:40:50 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page import="com.dimata.posbo.session.purchasing.SessFormatEmailQueenTandoor"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
                   com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.form.purchasing.CtrlPurchaseOrderItem,
                   com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.util.Command,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<% boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));%>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal Buat","Supplier","Contact","Alamat","Telp.","Terms","Days","PPn","Ket.","Mata Uang","Gudang","Order Barang","Include","%","Rate","Kategori","Status","Tanggal Pengiriman"},
    {"No","Location","Create Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Order","Include","%","Rate","Category","Status","Delivery Date"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Qty Stock di Gudang","Unit Stock","Hrg Beli Terakhir","Hrg Stock","Diskon Terakhir %",//7
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Nilai Tukar","Qty Request","Unit Request","Harga/Unit Beli","Hapus","Bonus","Qty Stock Sistem"},//18
    {"No","Code","Name","Qty Stock on Hand","Unit","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Exchange Rate","Qty Request","Unit Request","Price/Unit Buying","Delete","Bonus","Qty Stock Sistem"}   
};


public static final String textDelete[][] = {
    {"Apakah Anda yakin akan menghapus item ?","Qty Tidak Boleh Kosong","Unit beli dan Unit Stock berbeda, Tidak ada konversi antara unit tersebut, Silahkan Setting di masterdata unit"},
    {"Are You sure to delete this item? ","Qty Not Allow Empty","Unit Buying and Unit Stock is different. No conversion between these units, please Setting in Masterdata unit"}
};

public static final String textFinalDocument[][] = {
    {"Apakah Anda Yakin Memfinalkan Document ini?","Qty Tidak Boleh Kosong","Unit beli dan Unit Stock berbeda, Tidak ada konversi antara unit tersebut, Silahkan Setting di masterdata unit"},
    {"Are You Sure to Final This Data? ","Qty Not Allow Empty","Unit Buying and Unit Stock is different. No conversion between these units, please Setting in Masterdata unit"}
};
/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand,FrmPurchaseOrderItem frmObject,
        PurchaseOrderItem objEntity,Vector objectClass,long poItemId,int start, double exhangeRate,  String approot,Vector objectMaterial, long locationID) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItem[language][0],"3%");//No
    ctrlist.addHeader(textListOrderItem[language][1],"10%");//SKU
    ctrlist.addHeader(textListOrderItem[language][2],"20%");//Nama
    ctrlist.addHeader(textListOrderItem[language][4],"5%"); //unit
    ctrlist.addHeader(textListOrderItem[language][19],"15%");//qty stock current
    ctrlist.addHeader(textListOrderItem[language][3],"15%");//qty stock gudang
    ctrlist.addHeader(textListOrderItem[language][14],"15%");//qty request
    ctrlist.addHeader(textListOrderItem[language][17],"7%");//delete
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0) {
        start=0;
    }
     Periode maPeriode = PstPeriode.getPeriodeRunning();
    //add unit
    Vector listBuyUnit = PstUnit.list(0,1000,"","");
    Vector index_value = new Vector(1,1);
    Vector index_key = new Vector(1,1);
    index_key.add("-");
    index_value.add("0");
    for(int i=0;i<listBuyUnit.size();i++){
        Unit mateUnit = (Unit)listBuyUnit.get(i);
        index_key.add(mateUnit.getCode());
        index_value.add(""+mateUnit.getOID());
    }
    
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        Unit unitKon= new Unit();
        try{
            unitKon=PstUnit.fetchExc(poItem.getUnitRequestId());
        }catch(Exception ex){}
        rowx = new Vector();
        start = start + 1;
        String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+mat.getOID()+
                       " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+maPeriode.getOID();
        Vector listStock = new Vector();
        listStock = PstMaterialStock.list(0,0,where + " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationID,"");
        MaterialStock materialStock = new MaterialStock();

        if(listStock!=null && listStock.size()>0){
              materialStock = (MaterialStock)listStock.get(0);
        }
            
        if (poItemId == poItem.getOID()) index = i;
        
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            
            rowx.add(""+start);
            // code
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+poItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\"> <a href=\"javascript:cmdCheck()\">CHK</a>"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            rowx.add("<input type=\"text\" size=\"20\" name=\"unitItem\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            
            rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
            
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQtyInputStock())+"</div>");
            rowx.add("<div align=\"center\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY_STOCK]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQtyInputStock())+"\" class=\"formElemen\" onkeyup=\"javascript:calculate(this,event)\"  style=\"text-align:center\"><br>"+unit.getCode()+"</div>");
             
             // qty
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(this,event)\"  style=\"text-align:right\"></div>"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+poItem.getUnitId()+
                    "\"><input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getPrice()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly></div>"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exhangeRate)+"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly>"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount1()/exhangeRate)+"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount2()/exhangeRate)+"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()/exhangeRate)+"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()/exhangeRate)+"\" class=\"formElemenR\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getTotal()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly></div>");
            
            // bonus
           rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"\" value=\""+poItem.getUnitId()+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"></div><div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"</div>");
            
            
        }else{
            
            rowx.add(""+start+"");
            
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(poItem.getOID())+"')\">"+mat.getSku()+"</a>");
            
            rowx.add(mat.getName());
            rowx.add(unit.getCode());
            rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
            rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(poItem.getQtyInputStock())+"</div>");
            rowx.add("<div align=\"center\">"+poItem.getQuantity()+"</div>");
            
            // add by fitra 17-05-2014
            rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
           
        }
        lstData.add(rowx);
    }
    
    rowx = new Vector();
    if(objectClass.size()==0 && (iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) ){
        int stratInput=0;
        if(objectMaterial.size()>0){
            
            long standartId = Long.parseLong(com.dimata.system.entity.PstSystemProperty.getValueByName("ID_STANDART_RATE"));
                Vector customer = PstMemberReg.listFromLocationId(0, 0, PstMemberReg.fieldNames[PstMemberReg.FLD_LOCATION_ID]+"='"+locationID+"'", "");
                MemberReg member = new MemberReg();
                long priceTypeId=0;
                if(customer.size()>0){
                    for (int i = 0; i < customer.size(); i++) {
                        member = (MemberReg) customer.get(0);
                        try{
                            MemberGroup memberGroup= PstMemberGroup.fetchExc(member.getMemberGroupId());
                            priceTypeId=memberGroup.getPriceTypeId();
                        }catch(Exception ex){
                        }

                    }
            }
            
            for(int i=0; i<objectMaterial.size(); i++) {
                rowx = new Vector();
                stratInput=stratInput+1;
                Vector temp = (Vector)objectMaterial.get(i);

                Material mat = (Material)temp.get(0);
                Unit unit = (Unit)temp.get(2);

                String where = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+mat.getOID()+
                                                          " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_PERIODE_ID]+"="+maPeriode.getOID();
                Vector listStock = new Vector();
                listStock = PstMaterialStock.list(0,0,where + " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationID,"");
                MaterialStock materialStock = new MaterialStock();

                if(listStock!=null && listStock.size()>0){
                      materialStock = (MaterialStock)listStock.get(0);
                }
                
                //mencari price harga
                double price = PstPriceTypeMapping.getPrice(mat.getOID(), standartId, priceTypeId);

                rowx.add(""+(stratInput));
                // code
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"_"+i+"\" value=\""+mat.getOID()+
                        "\"><div align=\"left\">"+mat.getSku()+"</div><input tabindex=\"1\" type=\"hidden\" size=\"13\" name=\"matCode_"+i+"\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a tabindex=\"2\" href=\"javascript:cmdCheck()\"></a>");

                // name
                rowx.add("<div align=\"left\">"+mat.getName()+"</div><input type=\"hidden\" size=\"40\" name=\"matItem_"+i+"\" value=\""+mat.getName()+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"><a href=\"javascript:cmdCheck()\"></a>");
                //unit
                rowx.add("<div align=\"left\">"+unit.getCode()+"</div><input type=\"hidden\" size=\"40\" name=\"unitItem_"+i+"\" value=\""+unit.getCode()+""+"\" class=\"formElemen\"  id=\"txt_unit\">");
                rowx.add("<div align=\"center\">"+materialStock.getQty()+"</div>");

                // qty stock current
                rowx.add("<div align=\"center\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY_STOCK]+"_"+i +"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate(this,event)\"  style=\"text-align:center\"><br>"+unit.getCode()+"</div>");

                //update opie-eyek
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"4\" style=\"text-align:center\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"_"+i +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value,event)\">"+unit.getCode()+"</div>"
                        + "<input type=\"hidden\"  name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BONUS]+"_"+i+"\" value=\"0\">"
                        + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"_"+i+"\" value=\""+unit.getOID()+"\"><input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>"
                        + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"_"+i+"\" value=\""+price+"\" class=\"hiddenTextR\" readOnly>"
                        + "<input tabindex=\"4\" type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"_"+i+"\" value=\""+price+"\" class=\"formElemenR\">"
                        + "<input tabindex=\"5\" type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"_"+i+"\" value=\"\" class=\"formElemenR\">"
                        + "<input tabindex=\"5\" type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"_"+i+"\" value=\"\" class=\"formElemenR\">"
                        + "<input tabindex=\"6\" type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"_"+i+"\" value=\"\" class=\"formElemenR\">"
                        + "<input tabindex=\"7\" type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"_"+i+"\" value=\"\" class=\"formElemenR\">"
                        + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"_"+i+"\" value=\"\" class=\"formElemenR\">"
                        + "<input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"_"+i+"\" value=\"\" class=\"hiddenTextR\" readOnly>");

                rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"_"+i +"\" value=\""+unit.getOID()+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"></div><div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"_"+i +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"</div>");

                lstData.add(rowx); 

            }
        }else{
            
            rowx.add(""+(start+1));
            
            
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+
                    "\"><input tabindex=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">");

            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"><a href=\"javascript:cmdCheck()\">CHK</a>");

            rowx.add("<input type=\"text\" readonly='readonly' size=\"20\" name=\"unitItem\" value=\""+""+"\" class=\"formElemen\"  id=\"txt_unit\">");

            // qty stock curent
            rowx.add("<div align=\"center\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY_STOCK] +"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate(this,event)\"  style=\"text-align:right\"></div>");
            
             // qty
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"\" value=\"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(this,event)\"  style=\"text-align:right\"></div>"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\"\"><input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\"\" class=\"hiddenTextR\" readOnly>"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>"
                    + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"\" value=\"\" class=\"formElemenR\">");

            // bonus
           rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"></div><div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"</div>");

           lstData.add(rowx);
            
        }
        
    }else{
        if(iCommand!=Command.EDIT){
            rowx.add(""+(start+1));
           
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+
                    "\"><input tabindex=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\">");

            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            
            rowx.add("<input type=\"text\" readonly='readonly' size=\"20\" name=\"unitItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\">");

            // qty stock curent
            rowx.add("<div align=\"center\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY_STOCK] +"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate(this,event)\"  style=\"text-align:right\"></div>");
            
             // qty
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"\" value=\"\" class=\"formElemen\" onKeyUp=\"javascript:calculate(this,event)\"  style=\"text-align:right\"></div>"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\"\"><input type=\"hidden\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\"\" class=\"hiddenTextR\" readOnly>"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"4\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" onKeyUp=\"javascript:calculate(this,event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"\" value=\"\" class=\"formElemenR\">"
                    + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">"
                    + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>"
                    + "<input type=\"hidden\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT]+"\" value=\"\" class=\"formElemenR\">");

            // bonus
           rowx.add("<div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI]+"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"></div><div align=\"center\"><input type=\"hidden\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI]+"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(event)\"</div>");

           lstData.add(rowx);
      }
    }
    
    return ctrlist.drawBootstrap();
}

%>


<%
/**
* get approval status for create document
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_POR);
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidPurchaseOrder = FRMQueryString.requestLong(request,"hidden_material_order_id");
long oidPurchaseOrderItem = FRMQueryString.requestLong(request,"hidden_mat_order_item_id");
double DefaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
//add by fitra
String materialname = FRMQueryString.requestString(request,"txt_materialname");

int statusDocument = FRMQueryString.requestInt(request, FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_STATUS]);
/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String poCode = ""; //i_pstDocType.getDocCode(docType);
String poTitle = "Order Pembelian"; //i_pstDocType.getDocTitle(docType);
String poItemTitle = poTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRR));


/**
* process on purchase order main
*/
CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
iErrCode = ctrlPurchaseOrder.actionSR(Command.EDIT,oidPurchaseOrder);
iErrCode = ctrlPurchaseOrder.actionSR(Command.UPDATE,oidPurchaseOrder);
FrmPurchaseOrder frmPurchaseOrder = ctrlPurchaseOrder.getForm();
PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();

ControlLine ctrLine = new ControlLine();
CtrlPurchaseOrderItem ctrlPurchaseOrderItem = new CtrlPurchaseOrderItem(request);
ctrlPurchaseOrderItem.setLanguage(SESS_LANGUAGE);


iErrCode = ctrlPurchaseOrderItem.actionSR(iCommand,oidPurchaseOrderItem,oidPurchaseOrder,userName, userId);

//out.println(""+ctrlPurchaseOrderItem.getMessage()+"");

String whereMaterial ="";
Material srcMaterial = new Material();
String orderMaterial="MAT."+PstMaterial.fieldNames[PstMaterial.FLD_NAME];
if(po.getCategoryId()!=0){
   srcMaterial.setCategoryId(po.getCategoryId());
}

ContactList contactListx = new ContactList();
try{
    PstContactList pstContactList = new PstContactList();
    contactListx = pstContactList.fetchExc(po.getSupplierId());
}catch(Exception e){
    System.out.println("Err when fetch Supplier : "+e.toString());
}

String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
if (DESIGN_MATERIAL_FOR == 1) { 
    srcMaterial.setUseSellLocation(contactListx.getLocationId());
}else{
    srcMaterial.setUseSellLocation(0);
}

Vector listMaterial = PstMaterial.getListMaterialItemWithStockAndMinStock(0, srcMaterial, 0, 0, orderMaterial);
if(iCommand==Command.SAVEALL){
    ctrlPurchaseOrderItem.actionSaveAll(request,listMaterial,po.getOID(),po.getCurrencyId(),po.getCategoryId(), po.getLocationId());
    iCommand = Command.EDIT;
}


FrmPurchaseOrderItem frmPurchaseOrderItem = ctrlPurchaseOrderItem.getForm();
PurchaseOrderItem poItem = ctrlPurchaseOrderItem.getPurchaseOrderItem();
msgString = ctrlPurchaseOrderItem.getMessage();



String whereClauseItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                         " AND "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=0";
int vectSizeItem = PstPurchaseOrderItem.getCount(whereClauseItem);

int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    startItem = ctrlPurchaseOrderItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

whereClauseItem = "POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                  " AND "+"POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=0";
Vector listPurchaseOrderItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem);
if(listPurchaseOrderItem.size()<1 && startItem>0) {
    if(vectSizeItem-recordToGetItem > recordToGetItem) {
        startItem = startItem - recordToGetItem;
    } else {
        startItem = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    listPurchaseOrderItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem);
}

//bonus_list
String whereClauseBonusItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                              " AND "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=1";
int vectSizeBonusItem = PstPurchaseOrderItem.getCount(whereClauseBonusItem);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    startItem = ctrlPurchaseOrderItem.actionList(iCommand,startItem,vectSizeBonusItem,recordToGetItem);
}
whereClauseItem = "POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                  " AND "+"POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=1";
Vector listPurchaseOrderBonusItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem);
if(listPurchaseOrderBonusItem.size()<1 && startItem>0) {
    if(vectSizeBonusItem-recordToGetItem > recordToGetItem) {
        startItem = startItem - recordToGetItem;
    } else {
        startItem = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    listPurchaseOrderBonusItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem);
}


String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
        " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
        " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
        " != "+PstContactList.DELETE;
Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);

boolean documentClosed = false;
if(po.getPoStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT) {
    documentClosed = true;
}

/** kondisi ini untuk manampilakn form tambah item. posisi pada baris program paling bawah */
if(iCommand==Command.SAVE && iErrCode == 0) {
	iCommand = Command.ADD;
}

String enableInput="";
if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED||po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED ||po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
    enableInput="readonly";
}



//sent email if document to be approve
String contentEmailItem = "";
int sentNotif =Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
String toEmail =PstSystemProperty.getValueByName("POS_EMAIL_TO");
String urlOnline =PstSystemProperty.getValueByName("POS_URL_ONLINE");
int recordToGetItemEmail = 0;
String hasilEmail="";
if(sentNotif==1){
    if(po.getPoStatus()== I_DocStatus.DOCUMENT_STATUS_FINAL && iCommand==Command.SAVE){
        Location lokasiPO = new Location();
        
        try{
            lokasiPO = PstLocation.fetchExc(po.getLocationId());
        }catch(Exception ex){
        }
        
        String AksesOnsite ="";//Akses Onsite Aplication :&nbsp; <a href=\"http://localhost:8080/"+approot+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=1&deviceuse=1\">Onsite Aplication</a><br>";
        String AksesOnline ="";//Akses Online Aplication :&nbsp; <a href=\""+urlOnline+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=2&deviceuse=1\">Online Aplication</a>";
        contentEmailItem= SessFormatEmailQueenTandoor.getContentEmailSR(listPurchaseOrderItem, po, AksesOnsite,AksesOnline,lokasiPO);
        SessEmail sessEmail = new SessEmail();
        hasilEmail = sessEmail.sendEamil(toEmail, "Store Request - "+lokasiPO.getName()+" - "+po.getPoCode(), contentEmailItem, "");
    }
}


%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

<!--window.location = "#go";-->


//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
	document.frm_purchaseorder.command.value=comm;
	document.frm_purchaseorder.hidden_material_order_id.value=oid;
	document.frm_purchaseorder.action="storerequest_edit.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd(){
	document.frm_purchaseorder.hidden_mat_order_item_id.value="0";
	document.frm_purchaseorder.command.value="<%=Command.ADD%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_purchaseorder.submit();
}

function cmdEdit(oidPurchaseOrderItem)
{
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdAsk(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.ASK%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}





function cmdSave(){
    var finalDocument = document.frm_purchaseorder.FRM_FIELD_PO_STATUS.value;
    //alert("ss "+finalDocument);
    if(finalDocument==2){
        var msg= "<%=textFinalDocument[SESS_LANGUAGE][0]%>" ;
        var agree=confirm(msg);
        if (agree){
            document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
            document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
            document.frm_purchaseorder.action="storerequestitem.jsp";
            document.frm_purchaseorder.submit();
        }
     }else{
            //Pengecekan 
            
            document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
            document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
            document.frm_purchaseorder.action="storerequestitem.jsp";
            document.frm_purchaseorder.submit();
     
     }
	
        
}

function cmdSaveAll(){
        document.frm_purchaseorder.command.value="<%=Command.SAVEALL%>";
        document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
        document.frm_purchaseorder.action="storerequestitem.jsp";
        document.frm_purchaseorder.submit();
}

function cmdCheck(){
    //alert("hello");
    var strvalue  = "materialrequestsearch.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&location_id=<%=po.getLocationId()%>"+
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value;
    window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function keyDownCheck(e){
   if (e.keyCode == 13) {
        cmdCheck();
   }
}

function cmdConfirmDelete(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
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

function cmdCancel(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBack(){
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.start_item.value = 0;
	document.frm_purchaseorder.action="storerequest_edit.jsp";
	document.frm_purchaseorder.submit();
}

function changeVendor(){
    var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
	switch(currId){
	<%
        if(vt_supp!=null && vt_supp.size()>0){
            for(int i=0; i<vt_supp.size(); i++){
            ContactList contactList = (ContactList)vt_supp.get(i);
	%>
		case "<%=contactList.getOID()%>" :
				document.frm_purchaseorder.hid_contact.value = "<%=contactList.getPersonName()%>";
                document.frm_purchaseorder.hid_addres.value = "<%=contactList.getBussAddress()%>";
                document.frm_purchaseorder.hid_phone.value = "<%=contactList.getTelpNr()%>";
            break;
	<%}}%>
        default :
        break;
	}
}

function sumPrice()
{
}

function cmdCheck(){
    var strvalue  = "materialdosearchstorerequest.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value+
                                    "&mat_vendor="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value+
                                    "&rate="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>.value+
                                    "&locationID="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID]%>.value+
                                    "&currency_id="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID]%>.value;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function calculate(element, e){
    var qty = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup,guiDecimalSymbol);
    var cost = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc1 = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc2 = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT2]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDiscNom = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT_NOMINAL]%>.value,guiDigitGroup,guiDecimalSymbol);

     if(qty<0.0000){

          document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value=0;

          return;

        }
        
    if(isNaN(cost) || (cost==""))
        cost = 0.0;
    if(isNaN(lastDisc) || (lastDisc==""))
        lastDisc = 0.0;
    if(isNaN(lastDisc2) || (lastDisc2==""))
        lastDisc2 = 0.0;
    if(isNaN(lastDisc1) || (lastDisc1==""))
        lastDisc1 = 0.0;
    
    if(isNaN(lastDiscNom) || (lastDiscNom==""))
    lastDiscNom = 0.0;

    var totaldiscount = cost * lastDisc / 100;
    var totalMinus = cost - totaldiscount;
    var totaldiscount1 = totalMinus * lastDisc1 / 100;
    
    var totalMinus1 = totalMinus - totaldiscount1;
    var totaldiscount2 = totalMinus1 * lastDisc2 / 100;
   

    var totalCost = (totalMinus - totaldiscount1 - totaldiscount2) - lastDiscNom;
    var lastTotal = qty * totalCost;
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;//formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value = lastTotal;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    
    
     if (e.keyCode == 13) {
        // alert("hello");
        changeFocus(element);
    }
}


function changeFocus(element){
    //alert("helo"+element);
    if(element.name == "<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>") {
         document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.focus();
    }
    else if(element.name == "<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>") {
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT2]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT2]%>.focus();
    }
    else if(element.name == "<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT2]%>") {
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT_NOMINAL]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT_NOMINAL]%>.focus();
    }
    else if(element.name == "<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>") {
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.value="0";
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
    }
    else if(element.name == "<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT_NOMINAL]%>") {
        //document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURR_BUYING_PRICE]%>.focus();
        cmdSave();
    }
}

function cntTotal()
{
	/*var qty = cleanNumberInt(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup);
	var price = cleanNumberInt(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value,guiDigitGroup);
	*/
	var qty = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value;
	var price = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value;

	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = parseFloat(price) * qty;
		document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value = amount;
	}
	else
	{
		document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.focus();
	}
}

function cmdListFirst(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdSearch(){
    document.frm_purchaseorder.start.value="0";
    document.frm_purchaseorder.command.value="<%=Command.LIST%>";
    document.frm_purchaseorder.action="materialdosearchstorerequest.jsp";
    document.frm_purchaseorder.submit();
}

function keyDownCheck(e)  {
    
   
   
   var trap = document.frm_purchaseorder.trap.value;
   
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_purchaseorder.trap.value="1";
  
   }
   
   
   if (e.keyCode == 13 && trap == "0" && document.frm_purchaseorder.matItem.value == "" ){
        document.frm_purchaseorder.trap.value="0";
        cmdCheck();
    }
   
   if (e.keyCode == 13 && trap==1) {
       document.frm_purchaseorder.trap.value="0";
       cmdCheck();
  }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_purchaseorder.txt_materialname.value="";
   } 
}

function cmdListPrev(){
	document.frm_purchaseorder.command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListNext(){
	document.frm_purchaseorder.command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListLast(){
	document.frm_purchaseorder.command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.action="storerequestitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBackList(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="storerequest_list.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------
function addDelivery(){
	document.frm_purchaseorder.command.value="<%=Command.ADD%>";
	document.frm_purchaseorder.action="pomaterialdelivery.jsp";
	document.frm_purchaseorder.submit();
}

function editDelivery(oid){
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.hidden_order_deliver_sch_id.value = oid;
	document.frm_purchaseorder.action="pomaterialdelivery.jsp";
	document.frm_purchaseorder.submit();
}

function deliveryList(comm){
	document.frm_purchaseorder.command.value= comm;
	document.frm_purchaseorder.prev_command.value= comm;
	document.frm_purchaseorder.action="pomaterialdelivery.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
function paymentList(comm){
	document.frm_purchaseorder.command.value= comm;
	document.frm_purchaseorder.prev_command.value= comm;
	document.frm_purchaseorder.action="ordermaterialpayment.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------


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
</script>
<!-- #EndEditable -->
<meta charset="UTF-8">
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
<script src="../../../styles/jquery.min.js"></script>
<!-- #BeginEditable "headerscript" -->
<script type="text/javascript">
function change(value,e){
     document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value=value;
     document.frm_purchaseorder.hidden_qty_input.value=value;
     var oidKonversiUnit= document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
     var qtyCreate = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value;

     if (e.keyCode == 13) {
        showData(oidKonversiUnit,e);
     }
}         

function changePriceKonv(e){

    var oidUnit = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value;
    var oidUnitKonv = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
    
    var qtyBeli = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>.value;
    var costBeli = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.value,guiDigitGroup,guiDecimalSymbol);
    
    var qtyKonv =cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup,guiDecimalSymbol);
    
    
    var totalBeli = (qtyBeli*costBeli)/qtyKonv;
    
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value=parseFloat(totalBeli);
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURR_BUYING_PRICE]%>.value=parseFloat(totalBeli);
    
    var total = qtyKonv*totalBeli;
    
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value=parseFloat(total);
    
    if (e.keyCode == 13) {
        //alert("cilukba");
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT]%>.focus();
     }

}

function showData(value,e){
  
   var qtyInput = document.frm_purchaseorder.hidden_qty_input.value;
   var oidUnit = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value;
   var oidKonversiUnit=value;
    
   checkAjax(oidKonversiUnit,oidUnit,qtyInput);
   changePriceKonv(e);
   
    if (e.keyCode == 13) {
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
   }
   document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
}

function checkAjax(oidKonversiUnit,oidUnit, qtyInput){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"",
    type : "POST",
    async : false,
    success : function(data) {
        
        var xQtyInput=cleanNumberInt(data,guiDigitGroup);
        //alert(data+" "+xQtyInput);
        if(oidKonversiUnit!=oidUnit && xQtyInput==data){
               // alert ("You chosee different unit, Please check masterdata unit, ");
                alert ("<%=textDelete[SESS_LANGUAGE][2]%>");
        }
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value=data;
        var qty = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value;
        var value =cleanNumberInt(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE]%>.value,guiDigitGroup);
        var total = parseFloat(value)*qty;
        //alert("hh "+ value);
        document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value = parseFloat(total);
        //document.frm_purchaseorder.total_cost.value=parseFloat(total);
    }
});

}
</script>
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


<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />


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
<link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
<!-- Daterange picker -->
<link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<!-- bootstrap wysihtml5 - text editor -->
<link href="../../../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
<!-- Theme style -->
<link href="../../../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />
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
                    <small>Control panel</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-dashboard"></i>Edit Store Request</a></li>
                    <li class="active">Add Item Store Request</li>
                </ol>
            </section>

            <!-- Main content -->
            <section class="content">
                <div class="row">
                    <div class="col-md-12">
                        <div class="box box-primary">
                            <div class="box-header">
                                <h3 class="box-title">Add Item Store Request > <%=(po.getPoCode().length()==0 ? "<b>- Otomatis -</b>" : "<b>"+po.getPoCode()+"</b>")%></h3>
                            </div>
                            <form role="form" name="frm_purchaseorder" method="post" action="">
                              <input type="hidden" name="command" value="<%=iCommand%>">
                              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                              <input type="hidden" name="start_item" value="<%=startItem%>">
                              <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
                              <input type="hidden" name="hidden_mat_order_item_id" value="<%=oidPurchaseOrderItem%>">
                              <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PURCHASE_ORDER_ID]%>" value="<%=oidPurchaseOrder%>">
                              <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PURCHASE_ORDER_ITEM_ID]%>" value="<%=oidPurchaseOrderItem%>">
                              <input type="hidden" name="approval_command" value="<%=appCommand%>">
                              <input type="hidden" name="hidden_qty_input" value="">
                              <input type="hidden" name="source_link2" value="materialrequestsearch.jsp">
                              <input type="hidden" name="trap" value="">
                              <input type="hidden" name="hidden_oid_pp" value="">
                              <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID]%>" type="hidden" class="formElemen" size="10" value="<%=po.getCurrencyId()%>" readonly >
                              <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>" type="hidden" class="formElemen" size="10" value="<%=po.getExchangeRate()%>" readonly >               
                              <div class="box-body">
                                  <div class="row">
                                            <div class="col-md-4">
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label><br>
                                                     <%
                                                      Vector val_locationid = new Vector(1,1);
                                                      Vector key_locationid = new Vector(1,1);
                                                      /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                      whereClause += " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                      Vector vt_loc = PstLocation.list(0, 0, whereClause, "");*/
                                                      //add opie-eyek
                                                      //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                      String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                                           " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

                                                      whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                      Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                                      for(int d=0;d<vt_loc.size();d++){
                                                          Location loc = (Location)vt_loc.get(d);
                                                          val_locationid.add(""+loc.getOID()+"");
                                                          key_locationid.add(loc.getName());
                                                      }
                                                      String select_locationid = ""+po.getLocationId(); //selected on combo box
                                                    %>
                                                    <%=ControlCombo.drawBootsratap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%>
                                                 </div>
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label><br>
                                                    <%
                                                      Vector val_supplier = new Vector(1,1);
                                                      Vector key_supplier = new Vector(1,1);
                                                      if(vt_supp!=null && vt_supp.size()>0){
                                                          for(int d=0; d<vt_supp.size(); d++){
                                                              ContactList cnt = (ContactList)vt_supp.get(d);
                                                              String cntName = cnt.getCompName();
                                                              if(cntName.length()==0){
                                                                  cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                                              }

                                                              if (cntName.compareToIgnoreCase("'") >= 0) {
                                                                  cntName = cntName.replace('\'','`');
                                                              }

                                                              val_supplier.add(String.valueOf(cnt.getOID()));
                                                              key_supplier.add(cntName);
                                                          }
                                                      }
                                                      String select_supplier = ""+po.getSupplierId();
                                                    %>
                                                    <%=ControlCombo.drawBootsratap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"onClick=\"javascript:klik()\" onChange=\"javascript:changeVendor()\"","form-control")%>
                                                 </div>
                                           </div>
                                           <div class="col-md-4">
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label><br>
                                                    <%=ControlDate.drawDateWithBootstrap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE], (po.getPurchDate()==null) ? new Date() : po.getPurchDate(), 0, -1, "form-control-date", "")%>
                                                 </div>
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][19]%></label><br>
                                                    <%
                                                    Date dateRequest = new Date();
                                                    if(po.getPurchDate()!=null){
                                                        int lengthCredit = po.getCreditTime();  
                                                        dateRequest = (Date) po.getPurchDate();
                                                        dateRequest.setDate(dateRequest.getDate()+lengthCredit);
                                                    }
                                                    %>
                                                    <%=ControlDate.drawDateWithBootstrap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE_REQUEST], (po.getPurchDate()==null) ? new Date() : dateRequest, 0, -1, "form-control-date", "")%>
                                                 </div>
                                           </div>
                                           <div class="col-md-4">
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][17]%></label><br>
                                                    <%
                                                        Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                        //add opie-eyek 20130821
                                                        Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                                      %>
                                                     <%=ControlCombo.drawParentComboBoxBootsratap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CATEGORY_ID],"form-control", null, ""+po.getCategoryId(), null, materGroup)%>
                                                 </div>
                                                 <div class="form-group">
                                                     <div class="form-group">
                                                        <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][18]%></label><br>
                                                        <%Vector obj_status = new Vector(1,1);
                                                        Vector val_status = new Vector(1,1);
                                                        Vector key_status = new Vector(1,1);

                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                        //add by fitra
                                                        if((listPurchaseOrderItem!=null) && (listPurchaseOrderItem.size()>0)){
                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                        }

                                                        // update opie-eyek 19022013
                                                        // user bisa memfinalkan purchase request  jika  :
                                                        // 1. punya approve document pr = true
                                                        // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                                        boolean locationAssign=false;
                                                        locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",po.getLocationId());
                                                        if((listPurchaseOrderItem!=null) && (listPurchaseOrderItem.size()>0) &&(locationAssign==true) && (privApproval==true)){
                                                            if(!typeOfBusiness.equals("3")){
                                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_APPROVED));
                                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                                                            }
                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                        }

                                                        String select_status = ""+po.getPoStatus();
                                                        if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                        }else if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                        }else if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]); 
                                                        }else{
                                                        %>
                                                        <%=ControlCombo.drawBootsratap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_STATUS],null,select_status,val_status,key_status,"","form-control")%> </td>
                                                        <% } %>
                                                     </div>
                                                    <%
                                                      long oidCurrency=0;
                                                      Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                                                        Vector vectCurrVal = new Vector(1,1);
                                                        Vector vectCurrKey = new Vector(1,1);
                                                        for(int i=0; i<listCurr.size(); i++){
                                                            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                                            if(i==0){
                                                                oidCurrency=currencyType.getOID();
                                                            }
                                                            vectCurrKey.add(currencyType.getCode());
                                                            vectCurrVal.add(""+currencyType.getOID());
                                                        }
                                                        //mencari rate yang berjalan
                                                        if(oidPurchaseOrder!=0){
                                                            oidCurrency=po.getCurrencyId();
                                                        }
                                                        double resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrency);
                                                      %>
                                                      &nbsp;&nbsp;
                                                      <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID]%>" type="hidden" class="formElemen" size="10" value="<%=oidCurrency%>">
                                                      <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>" type="hidden" class="formElemen" size="10" value="<%=po.getExchangeRate()!=0?po.getExchangeRate():resultKonversi%>" <%=enableInput%>>
                                                      <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CODE_REVISI]%>" type="hidden" class="formElemen" size="10" value="<%=po.getCodeRevisi()%>" >
                                                 </div>
                                                 
                                                 <div class="form-group">
                                                    <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_TERM_OF_PAYMENT]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="1">
                                                    <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=po.getCreditTime()%>">
                                                    <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_REMARK]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=po.getCreditTime()%>">
                                                 </div>
                                                                                                      
                                           </div>
                                      </div>
                                      <div class="row">
                                          <div class="col-md-12">
                                              <%= drawListPoItem(SESS_LANGUAGE,iCommand,frmPurchaseOrderItem, poItem,listPurchaseOrderItem,oidPurchaseOrderItem,startItem,po.getExchangeRate(),approot,listMaterial,po.getLocationId())%> 
                                          </div>
                                      </div>
                                        <% if(iErrCode!=0){ %>
                                        <div class="row">   
                                            <div class="col-md-12">
                                                <div class="alert alert-warning"><center><%= msgString%></center</div>
                                            </div>
                                        </div>
                                        <%}%>
                                      <div class="row">
                                          <div class="col-md-12">
                                              <div class="form-group">
                                              <% if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && listPurchaseOrderItem.size() == 0) { %>
                                                     <% if(listMaterial.size() >  0) { %>
                                                        <button class="btn btn-primary pull-left" style="margin-right: 5px;" onclick="javascript:cmdSaveAll()" ><i class="fa fa-check-square"></i> Save All</button>
                                                    <% }else{ %>
                                                        <button class="btn btn-primary pull-left" style="margin-right: 5px;" onclick="javascript:cmdSave()" ><i class="fa fa-save"></i> Save</button>
                                                    <%}%>
                                              <% }else{%>
                                                    <button class="btn btn-primary pull-left" style="margin-right: 5px;" onclick="javascript:cmdSave()" ><i class="fa fa-save"></i> Save</button>
                                              <% } %>
                                                    <button class="btn btn-primary pull-left" style="margin-right: 5px;" onclick="javascript:cmdBack()" ><i class="fa fa-chevron-left"></i> Back</button>
                                               </div>
                                          </div>
                                      </div> 
                                  </div>
                              </div>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </aside><!-- /.right-side -->
    </div><!-- ./wrapper -->
<!-- #EndTemplate -->
<script language="JavaScript">
            changeVendor();
</script>
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_purchaseorder.matItem.focus();
    <% } %>
</script>
<script language="JavaScript">
     // add By Fitra
     var trap = document.frm_purchaseorder.trap.value;       
     document.frm_purchaseorder.trap.value="0";
     document.frmvendorsearch.txt_materialname.focus();
</script>

<%--autocomplate--%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>
                               
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

