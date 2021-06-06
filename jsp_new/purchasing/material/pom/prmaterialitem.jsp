<%-- 
    Document   : prmaterialitem
    Created on : Feb 5, 2014, 10:27:57 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page import="com.dimata.posbo.session.purchasing.SessFormatEmailQueenTandoor"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.location.Location,
                   com.dimata.common.entity.location.PstLocation,
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
<% 
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_REQUEST);
int  appObjCodeMasterdata = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATALOG);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApproval= userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
boolean privShowStock = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeMasterdata, AppObjInfo.COMMAND_VIEW_STOCK));
int privShowStok = 0;
if (privShowStock==true){
    privShowStok=1;
}
%>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal","Supplier","Contact","Alamat","Telp.","Terms","Days","PPn","Ket.","Mata Uang","Gudang","Request Barang","Include","%","Status","Rate"},
    {"No","Location","Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Request","Include","%","Status","Rate"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Supplier","Name Supplier","Unit Order","Qty Request","Price",//6
     "Unit Stock","Total Price","Discount Nominal","Netto Hrg Beli","Total","Hapus","Terms"},//14
    {"No","Code","Name","Supplier"," Name Supplier","Unit Order","Qty Request","Price",
     "Unit Stock","Total Price","Discount Nominal","Netto Price Buying","Total","Delete","Terms"}
     
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand,FrmPurchaseRequestItem frmObject,
        PurchaseRequestItem objEntity,Vector objectClass,long poItemId,int start,long matOid, String matCode,String matItem, String matUnit, long matUnitId, int typeTerm, long oidNewSupplier,String approot, String useForRaditya
        ) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"3%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"10%");//sku
    ctrlist.addHeader(textListOrderItem[language][2],"15%");//nama
    
    ctrlist.addHeader(textListOrderItem[language][3],"5%");//kode supplier
if(useForRaditya.equals("1")){}else{
    ctrlist.addHeader(textListOrderItem[language][4],"10%");//nama supplier
}
    ctrlist.addHeader(textListOrderItem[language][5],"5%");//unit order

if(useForRaditya.equals("0")){
    ctrlist.addHeader(textListOrderItem[language][7],"10%");//price
}
    ctrlist.addHeader(textListOrderItem[language][6],"10%");//qty order
if(useForRaditya.equals("0")){
    ctrlist.addHeader(textListOrderItem[language][9],"10%");//total price
}
    ctrlist.addHeader(textListOrderItem[language][8],"5%");//unit stock
    ctrlist.addHeader(textListOrderItem[language][14],"5%");//terms
    ctrlist.addHeader(textListOrderItem[language][13],"5%");
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0) {
        start=0;
    }
    
    //cek vendor yang ada berdasarkan oidmaterial
    String whereClause = ""+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+matOid;
    Vector listMatVendorPrice = PstMatVendorPrice.listFiltter(start,0, "" , PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+" ASC");
    Vector vectUnitVal = new Vector(1,1);
    Vector vectUnitKey = new Vector(1,1);
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
        vectUnitKey.add(""+matVendorPrice.getVendorId());
        vectUnitVal.add(""+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId()));
    }
    
//    if(typeTerm!=2){
//        vectUnitKey.add(""+oidNewSupplier);
//        vectUnitVal.add("New Supplier");
//    }
    
    //cek unit yang bisa dibeli di vendor tsb
    Vector vectUnitSupVal = new Vector(1,1);
    Vector vectUnitSupKey = new Vector(1,1);
    if(firstVendor>0){
        String whereClauseUnitSupp = " MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+matOid+""+
                                     " AND MV."+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"="+firstVendor;
        Vector listJoinUnit = PstMatVendorPrice.listJoinUnit(start,0, whereClauseUnitSupp , "");
         for(int i=0; i<listJoinUnit.size(); i++)
            {
                MatVendorPrice matVendorPrice = (MatVendorPrice)listJoinUnit.get(i);
                if(i==0){
                            firstPriceBuying=matVendorPrice.getOrgBuyingPrice();
                }
                vectUnitSupKey.add(""+matVendorPrice.getBuyingUnitId());
                vectUnitSupVal.add(""+matVendorPrice.getBuyingUnitName());
            } 
    }else{
    //tampilkan unit yang bisa di beli di material tsb
        String whereBaseUnitOrder = "PMU."+PstMaterialUnitOrder.fieldNames[PstMaterialUnitOrder.FLD_MATERIAL_ID] + " ='"+matOid+"'";
        Vector listBaseUnitOrder = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
        for(int i=0; i<listBaseUnitOrder.size(); i++){
            MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listBaseUnitOrder.get(i);
                vectUnitSupKey.add(""+materialUnitOrder.getUnitID());
                vectUnitSupVal.add(""+materialUnitOrder.getUnitKode());
            }
    }
    
    Vector val_terms = new Vector(1,1);
    Vector key_terms = new Vector(1,1);
if(useForRaditya.equals("1")){
    for(int d=0; d<PstPurchaseOrder.purchaseRequestType.length; d++){
        val_terms.add(String.valueOf(d));
        key_terms.add(PstPurchaseOrder.purchaseRequestType[d]);
    } 
}else{
    for(int d=0; d<PstPurchaseOrder.fieldsPurchaseRequestType.length; d++){
        val_terms.add(String.valueOf(d));
        key_terms.add(PstPurchaseOrder.fieldsPurchaseRequestType[d]);
    } 
}
    
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        Unit unitRequest = new Unit();
        ContactList con = new ContactList();
        try{
            if(poItem.getUnitRequestId() != 0){
           unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
            }
            if(poItem.getSupplierId() != 0){
           con = PstContactList.fetchExc(poItem.getSupplierId());
            }

        }catch(Exception ex){}
        
        rowx = new Vector();
        start = start + 1;

        if (poItemId == poItem.getOID()) index = i;
        
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            
            rowx.add(""+(start+1));
            // code
            rowx.add("<input type=\"hidden\" data-status='edit' id='matOid' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+poItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" id='matCodex' name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen matCode\"><a href='#' class='showCheckMaterial'>CHK</a>");
            
            // name
            rowx.add("<input type=\"text\" size=\"20\" id='matItem' name=\"matItem\" value=\""+mat.getName()+"\" class='formElemen matCode'><a class='showCheckMaterial' href='#'>CHK</a>");

            //kode supplier
            rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID],"formElemen select2", null, ""+poItem.getSupplierId(), vectUnitKey, vectUnitVal, "onChange=\"javascript:changeContractPrice(this.value,'"+matOid+"')\""));

if(useForRaditya.equals("1")){
            rowx.add("<div align=\"center\" id=\"posts\"><input tabindex=\"3\" type=\"hidden\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\"\" class=\"formElemen\" style=\"text-align:right\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");
}else{
            rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\""+poItem.getSupplierName()+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");
}

            //rowx.add("<div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, ""+unitRequest.getOID(), vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");
            //rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID]+"\" value=\""+poItem.getUnitRequestId()+"\"><div align=\"center\"><input type=\"text\" size=\"5\" name=\"matUnit\" id='matUnit' value=\""+PstUnit.getKodeUnitByHash(poItem.getUnitRequestId())+"\" readOnly>");
            
if(useForRaditya.equals("0")){
            rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\""+poItem.getPriceBuying()+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate(event)\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
} else {
            rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"hidden\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\"0\" class=\"formElemen\" style=\"text-align:right\"></div><input tabindex=\"3\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate(event)\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
}
            //input qty
            

if(useForRaditya.equals("0")){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"' value=\""+poItem.getTotalPrice()+"\" readOnly style=\"text-align:right\"></div>");
}
            // unit
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+poItem.getUnitId()+"\"><div align=\"center\"><input type=\"text\" size=\"5\" name=\"matUnit\" id ='matUnit' value=\""+PstUnit.getKodeUnitByHash(poItem.getUnitId())+"\" readOnly>");
            
            rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_TERMS],"formElemen", null, ""+poItem.getTermPurchaseRequest(),val_terms , key_terms,""));

            rowx.add("");
        }else{
            
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(poItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(mat.getName());
            
            //kode supplier
            rowx.add(""+con.getCompName());//nama supplier
            
            if(useForRaditya.equals("1")){}else{
            rowx.add(""+con.getCompName());//nama supplier
            }
            rowx.add(""+unitRequest.getCode());//unit order

if(useForRaditya.equals("0")){
            rowx.add("<div align=\"right\">"+Formater.formatNumber(poItem.getPriceBuying(), "###,###")+"</div>"); //price
}
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            
if(useForRaditya.equals("0")){
            rowx.add("<div align=\"right\">"+Formater.formatNumber(poItem.getTotalPrice(),"###,###")+"</div>"); //price
}
            
            // unit
            rowx.add(unit.getCode());
            
        if(useForRaditya.equals("1")){
            rowx.add(PstPurchaseOrder.purchaseRequestType[poItem.getTermPurchaseRequest()]);
        }else{
            rowx.add(PstPurchaseOrder.fieldsPurchaseRequestType[poItem.getTermPurchaseRequest()]);
        }
            // add by fitra 17-05-2014
            rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
         
            
        }
        lstData.add(rowx);
    }

    rowx = new Vector();
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)){
        
        rowx.add(""+(start+1));
        // code
        rowx.add("<input id='matOid' data-status='add' type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+matOid+"\"><input tabindex=\"1\" type=\"text\" size=\"13\" id='matCodex'   name=\"matCode\" value=\""+matCode+"\" class='formElemen matCode'><a tabindex=\"2\" class='showCheckMaterial' href='#'>CHK</a>");
        
        // name
        
        rowx.add("<input type=\"text\" size=\"20\"  id='matItem' name=\"matItem\" value=\""+matItem+"\" class='formElemen matCode' ><a href='#' class='showCheckMaterial'>CHK</a>");
        
        //kode supplier
        rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID],"formElemen select2", null, "", vectUnitKey, vectUnitVal, "onChange=\"javascript:changeContractPrice(this.value,'"+matOid+"')\""));

if(useForRaditya.equals("1")){
        rowx.add("<input tabindex=\"3\" type=\"hidden\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\"\" class=\"formElemen\" style=\"text-align:left\"><div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");
}else{
        rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\""+firstVendorName+"\" class=\"formElemen\" style=\"text-align:left\"></div>");
        rowx.add("<div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", vectUnitSupKey, vectUnitSupVal, "onChange=\"javascript:changeChargeUnit(this.value)\"")+"</div>");
}
        
if(useForRaditya.equals("0")){
        rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\""+firstPriceBuying+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
        rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate(event)\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\"\" class=\"formElemen\" style=\"text-align:right\"></div>");
} else {
        rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"hidden\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\"0\" class=\"formElemen\" style=\"text-align:right\"><input tabindex=\"3\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate(event)\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\"\" class=\"formElemen\" style=\"text-align:right\"></div>");
}
        //input qty
        
        
if(useForRaditya.equals("0")){
        rowx.add("<div align=\"left\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"' value=\"\" readOnly style=\"text-align:right\"></div>");
}
        // unit
        rowx.add("<input type=\"hidden\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+matUnitId+"\"><div align=\"center\"><input type=\"text\" size=\"5\" id='matUnit' name=\"matUnit\" value=\""+matUnit+"\" readOnly>");
        
        rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_TERMS],"formElemen", null, "",val_terms , key_terms, ""));
        
        rowx.add("");  
        
        lstData.add(rowx);
    }

    return ctrlist.draw();
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
long oidPurchaseRequest = FRMQueryString.requestLong(request,"hidden_material_request_id");
long oidPurchaseRequestItem = FRMQueryString.requestLong(request,"hidden_mat_request_item_id");
double DefaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
long locationId = FRMQueryString.requestLong(request,"location_id");

long matOid= FRMQueryString.requestLong(request,FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]);
String matCode=FRMQueryString.requestString(request,"matCode");
String matItem=FRMQueryString.requestString(request,"matItem");
String matUnit=FRMQueryString.requestString(request,"matUnit");
long matUnitId=FRMQueryString.requestLong(request,FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]);
int includePrintPrice = FRMQueryString.requestInt(request, "includePrinPrice");
/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String poCode = ""; //i_pstDocType.getDocCode(docType);
String poTitle = "Request Pembelian"; //i_pstDocType.getDocTitle(docType);
String poItemTitle = poTitle + " Item";

/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRR));


/**
* process on purchase order main
*/

CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
iErrCode = ctrlPurchaseRequest.action(Command.SAVE,oidPurchaseRequest,"",0);
FrmPurchaseRequest frmPurchaseRequest = ctrlPurchaseRequest.getForm();
PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();
int start = FRMQueryString.requestInt(request, "start");
ControlLine ctrLine = new ControlLine();
CtrlPurchaseRequestItem ctrlPurchaseRequestItem = new CtrlPurchaseRequestItem(request);
ctrlPurchaseRequestItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlPurchaseRequestItem.action(iCommand,oidPurchaseRequestItem,oidPurchaseRequest,userName, userId);
FrmPurchaseRequestItem frmPurchaseRequestItem = ctrlPurchaseRequestItem.getForm();
PurchaseRequestItem poItem = ctrlPurchaseRequestItem.getPurchaseRequestItem();
msgString = ctrlPurchaseRequestItem.getMessage();
//add by fitra
String materialname = FRMQueryString.requestString(request,"txt_materialname");
//oidPurchaseRequestItem = po.getOID();

String whereClauseItem = PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;
String orderClauseItem = "";
int vectSizeItem = PstPurchaseRequestItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    startItem = ctrlPurchaseRequestItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

whereClauseItem = "POI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;

Vector listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem,recordToGetItem,whereClauseItem);

if(listPurchaseRequestItem.size()<1 && startItem>0) {
    if(vectSizeItem-recordToGetItem > recordToGetItem) {
        startItem = startItem - recordToGetItem;
    } else {
        startItem = 0 ;
        iCommand = Command.FIRST;
        prevCommand = Command.FIRST;
    }
    listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem,recordToGetItem,"","");
}

String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
        " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
        " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
        " != "+PstContactList.DELETE;
Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);

boolean documentClosed = false;
if(po.getPrStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT) {
    documentClosed = true;
}

/** kondisi ini untuk manampilakn form tambah item. posisi pada baris program paling bawah */
int inputNew=0;
if(iCommand==Command.SAVE && iErrCode == 0) {
        matCode="";
        matItem="";
        matUnit="";
        matUnitId=0;
        inputNew=1;
        oidPurchaseRequestItem=0;
	iCommand = Command.ADD;
        
}

//untuk mapping new supplier
long oidNewSupplier =Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));

String enableInput="";
if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED||po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
    enableInput="enable";
}


//sent email if document to be approve
String contentEmailItem = "";
int sentNotif =Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
String toEmail =PstSystemProperty.getValueByName("POS_EMAIL_TO");
String urlOnline =PstSystemProperty.getValueByName("POS_URL_ONLINE");
int recordToGetItemEmail = 0;
String hasilEmail="";
if(sentNotif==1){
    if(po.getPrStatus()== I_DocStatus.DOCUMENT_STATUS_FINAL && iCommand==Command.SAVE){
        Vector listPurchaseRequestItemEmail = new Vector();
        listPurchaseRequestItemEmail = PstPurchaseRequestItem.listWithStokNMinStock(startItem,recordToGetItemEmail,whereClauseItem,po.getLocationId());
        Location lokasiPO = new Location();
        try{
            lokasiPO = PstLocation.fetchExc(po.getLocationId());
        }catch(Exception ex){
        }
        String AksesOnsite ="Akses Onsite Aplication :&nbsp; <a href=\"http://localhost:8080/"+approot+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=1&deviceuse=1\">Onsite Aplication</a><br>";
        String AksesOnline ="Akses Online Aplication :&nbsp; <a href=\""+urlOnline+"/login_new.jsp?nodocument="+po.getOID()+"&typeView=2&deviceuse=1\">Online Aplication</a>";
        contentEmailItem= SessFormatEmailQueenTandoor.getContentEmailPR(listPurchaseRequestItemEmail, po, AksesOnsite,AksesOnline,lokasiPO);
        SessEmail sessEmail = new SessEmail();
        hasilEmail = sessEmail.sendEamil(toEmail, "Purchase Request Approval - "+lokasiPO.getName()+" - "+po.getPrCode(), contentEmailItem, "");
    }
}

// add by fitra 17-05-2014
if(iCommand==Command.DELETE && iErrCode==0) {
%>
    <jsp:forward page="prmaterial_edit.jsp">
    <jsp:param name="hidden_mat_request_item_id" value="<%=oidPurchaseRequestItem%>"/>
    <jsp:param name="command" value="<%=Command.EDIT%>" />	
    </jsp:forward>

<%
}
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script src="../../../styles/bootstrap/js/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
<link rel="stylesheet" href="../../../styles/AdminLTE-2.3.11/plugins/select2/select2.min.css" type="text/css">
<script language="JavaScript">

<!--window.location = "#go";-->


//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -------------ss--------



function main(oid,comm){
	document.frm_purchaseorder.command.value=comm;
	document.frm_purchaseorder.hidden_material_request_id.value=oid;
	document.frm_purchaseorder.action="prmaterial_edit.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd(){
	document.frm_purchaseorder.hidden_mat_request_item_id.value="0";
	document.frm_purchaseorder.command.value="<%=Command.ADD%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_purchaseorder.submit();
}

function cmdEdit(oidPurchaseRequestItem)
{
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdAsk(oidPurchaseRequestItem){
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.ASK%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdSave(){
	document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}


function cmdDelete(oidPurchaseRequestItem){
    
    
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
        
}

function cmdConfirmDelete(oidPurchaseRequestItem){
    
    
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
        
}

// add by fitra 17-05-2014
function cmdNewDelete(oid){
    var msg;
    msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
    var agree=confirm(msg);
    if (agree)
        return cmdConfirmDelete(oid);
    else
        return cmdEdit(oid);
}

function cmdCancel(oidPurchaseRequestItem){
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBack(){
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.start_item.value = 0;
	document.frm_purchaseorder.action="prmaterial_edit.jsp";
	document.frm_purchaseorder.submit();
}

function sumPrice()
{
}

function cmdCheck(){
    var strvalue  = "materialrequestsearch.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&location_id=<%=po.getLocationId()%>"+
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value;
    window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function changeContractPrice(vendorId, idmaterial){
    if(vendorId==<%=oidNewSupplier%>){
        //alert("ndak ada suppliernya");
        //alert("unit id "+unitRequestId);
        //alert("test");
        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_NAME]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PRICE_BUYING]%>.value="";
        checkAjaxUnitRequestStandart(vendorId,idmaterial);
        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_NAME]%>.focus();
    }else{
        //with ajax
       //alert("unit id "+unitRequestId);
       //alert("test 2");
       checkAjaxUnitRequest(vendorId,idmaterial);
       var unitRequestId = document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_UNIT_REQUEST_ID]%>.value;
       checkAjaxPrice(unitRequestId,vendorId);
       checkAjaxNameVendor(unitRequestId,vendorId);
       document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();

    }
   }

function changeChargeUnit(value){
    var vendorId = document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_ID]%>.value;
    checkAjaxPrice(value,vendorId);
    document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
}

 function checkAjaxPrice(unitId,vendorId){
            //var nameInput="priceContract"+materialId;
            //alert("ccc ");
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=1&unitId="+unitId+"&vendorId="+vendorId+"",
            type : "POST",
            async : false,
            success : function(data) {
                //alert("Harga : "+data);
                document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PRICE_BUYING]%>.value=data;
                document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
            }
        });
}

 function checkAjaxNameVendor(unitId,vendorId){
            //var nameInput="priceContract"+materialId;
            //alert("ccc ");
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=4&unitId="+unitId+"&vendorId="+vendorId+"",
            type : "POST",
            async : false,
            success : function(data) {
                //alert("Harga : "+data);
                document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_NAME]%>.value=data;
                document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
            }
        });
}


 function checkAjaxUnitRequest(vendorId,idmaterial){
            //alert("hellp");
            var vendorId=0;
            var idMaterial = 0;
            vendorId = $('#FRM_FIELD_SUPPLIER_ID').val();
            var idMaterial = $('#matOid').val();
            $("#posts").html("");
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=2&vendorId="+vendorId+"&material_id="+idMaterial+"",
            type : "POST",
            async : false,
            cache: false,
            success : function(data) {
                    //alert(data);
                    content=data;
                    $(content).appendTo("#posts");
                    document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
            }
        });
}

function checkAjaxUnitRequestStandart(vendorId,idmaterial){
            var vendorId=0;
            var idMaterial = 0;
            var matUnit = $('#matUnit').val();
            var matUnitId = $('#FRM_FIELD_UNIT_ID').val();
            vendorId = $('#FRM_FIELD_SUPPLIER_ID').val();
            var idMaterial = $('#matOid').val();
            $("#posts").html("");
            $.ajax({
                url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckPriceContract?typeCheck=3&vendorId="+vendorId+"&material_id="+idMaterial+"&matUnit="+matUnit+"&matUnitId="+matUnitId+"",
                type : "POST",
                async : false,
                cache: false,
                success : function(data) {
                        //alert(data);
                        content=data;
                        $(content).appendTo("#posts");
                        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
                         $('#FRM_FIELD_SUPPLIER_NAME').focus();
                }
            });
           
            
}



function printFormHtml() {
    var include =0;
    var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("pr_material_print_form.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice="+include,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printResentEmail() {
    var include =0;
    var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("pr_resent_email.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice="+include,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}


function cmdListFirst(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListPrev(){
	document.frm_purchaseorder.command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListNext(){
	document.frm_purchaseorder.command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListLast(){
	document.frm_purchaseorder.command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.action="prmaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBackList(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="prmaterial_list.jsp";
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

// add by fitra
function cmdSearch(){
    document.frm_purchaseorder.start.value="0";
    document.frm_purchaseorder.command.value="<%=Command.LIST%>";
    document.frm_purchaseorder.action="materialrequestsearch.jsp";
    document.frm_purchaseorder.submit();
}

function changeLocation(){
   //cmdSave();
}

function changeDate(){
    cmdSave();
}

// update by fitra




//------------------------- END JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
function paymentList(comm){
	document.frm_purchaseorder.command.value= comm;
	document.frm_purchaseorder.prev_command.value= comm;
	document.frm_purchaseorder.action="ordermaterialpayment.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------


function calculate(evenClick){
    var qty = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup,guiDecimalSymbol);
    var cost = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PRICE_BUYING]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastTotal = qty * cost;
    document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_TOTAL_PRICE]%>.value = lastTotal;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    if (evenClick.keyCode == 13) {
        //cmdSave();
}

}

function changeStatus() {
    var xxx =document.frm_purchaseorder.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS]%>.selectedIndex;
        if (xxx == '2'){
            if (confirm('Yakin Untuk menyimpan and kirim email ?')) {
                // Save it!
                cmdSave();
            } else {
                // Do nothing!
            }
      }
 }

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


<%--autocomplate add by fitra--%>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
<link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
<script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function(){
        var statusDoc = "";
        statusDoc = $('#matOid').data('status');
       
        function ajaxPurchaseOrder(url,data,type,appendTo,another,optional,optional2){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {
                    $(''+appendTo+'').html(data);
                    if (optional=="showModal"){
                        if (another=="cekMaterial"){
                           
                            $('#modalCheck').modal({
                                backdrop: 'static',
                                keyboard: false
                            });
                        }
                    }
                },
                error : function(data){
                    alert('error');
                }
            }).done(function(){
                 if (another=="cekMaterial"){
                     loadCheck("","","");
                     formSearchKeyUp();
                     materialGroupChange();
                     addNewMaterial();
                 }else if(another=="loadMaterial"){
                     selectMaterial();
                 }else if(another=="addNewMaterial"){
                     backFromAdd()
                 }else if(another=="loadVendor"){
                     getNameVendor();
                     loadUnitMaterial();
                     
                 }else if(another=="loadUnitMaterial"){
                     setPrice();
                 }
            });

        }
        
        function keyDownCheck(e){
            var trap = $('#trap').val(); 
            var matItem = $('#matItem').val();
            var code = e.which;
           
            if (code == 13 && trap==0){
                $('#trap').val('1');
            }
            if (code== 13 && trap == "0" && matItem == "" ){
                $('#trap').val('0');
                check();
            }
            if (code == 13 && trap=="1") {
                $('#trap').val('0');
                check();
            }
            if (code == 27) {
                $('#matItem').val("");
            }

        }
        
        
        function check(){
            var matCode = $('#matCodex').val();
            var matItem = $('#matItem').val();
            var data = "command=<%=Command.FIRST%>&mat_code="+matCode+"&location_id=<%=po.getLocationId()%>&txt_materialname="+matItem+"&sessLanguage=<%= SESS_LANGUAGE%>";
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            
            ajaxPurchaseOrder(""+url+"",data,"POST","#dynamicModalHeader","cekMaterial","showModal","");
        }
        
        function loadCheck(matCode,materialName,materialGroup){
            var mat_code2="";
            var txt_materialname2 = "";
            
            mat_code2 = $('#mat_code2').val();
            txt_materialname2 =$('#txt_materialname2').val();
            
            var data = "command=<%=Command.NEXT%>&mat_code="+mat_code2+"&location_id=<%=po.getLocationId()%>&txt_materialname="+txt_materialname2+"&sessLanguage=<%= SESS_LANGUAGE%>&privShowStock=<%= privShowStok%>&txt_materialgroup="+materialGroup+"";
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            
            ajaxPurchaseOrder(""+url+"",data,"POST","#dynamicModalContent","loadMaterial","","");
        }
        
        function selectMaterial(){
            $('.selectMaterial').click(function(){
                var materialOid= $(this).data('oid');
                var materialCurrency = $(this).data('materialcurrency');
                var materialSku = $(this).data('sku');
               
                var name = $(this).data('name');
                var unitCode = $(this).data('unitcode');
                var unitOid = $(this).data('unitoid');
                
                $('#matCodex').val(materialSku);
                $('#matOid').val(materialOid);
                $('#matItem').val(name);
                $('#matUnit').val(unitCode);
                $('#<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>').val(unitOid);
                $('#<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>').focus();
                
                $('#modalCheck').modal('hide');
                loadVendorByOid(materialOid);
             });
        }
        
        if (statusDoc=="edit"){
            var matOid= $('#matOid').val();
            loadVendorByOid(matOid);
        }
        
        function formSearchKeyUp(){
            $('.formSearch').keyup(function(){
                loadCheck("","","");
             
            });
        }
        
        function materialGroupChange(){
            $('#txt_materialgroup').change(function(){
                loadCheck("","","");
            });
        }
        
        function addNewMaterial(){
            $('#addNewItem').click(function(){
                var data = "command=<%=Command.ADD%>&root=<%=approot%>";
                var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
                
                $('#dynamicModalHeader').html('');
                $('#dynamicModalContent').html('<img src="../../../imgstyle/loading.gif">');
                ajaxPurchaseOrder(""+url+"",data,"POST","#dynamicModalContent","addNewMaterial","","");
            });
        }
        
        function backFromAdd(){
            $('#btnBackFromAdd').click(function(){
                check();
            });
        }
        
        function loadVendorByOid(oid){
            var data = "command=<%=Command.GET%>&oid="+oid+"&tOfPayment=<%= po.getTermOfPayment()%>";
            
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            ajaxPurchaseOrder(""+url+"",data,"POST","#FRM_FIELD_SUPPLIER_ID","loadVendor","","");
        }
        
        function loadUnitMaterial(){
            var firstVendor = $('#FRM_FIELD_SUPPLIER_ID option:selected').attr('class');
            var arrFirstVendor = firstVendor.split(";");
            firstVendor = arrFirstVendor[0];
            
            var matOid= $('#matOid').val();
            var unitId = $('#FRM_FIELD_UNIT_ID').val();
            var unit = $('#matUnit').val();
            
            var data = "command=<%=Command.ASK%>&matOid="+matOid+"&firstVendor="+firstVendor+"&unitId="+unitId+"&unit="+unit+"";
            //alert(data);
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            
            ajaxPurchaseOrder(""+url+"",data,"POST","#FRM_FIELD_UNIT_REQUEST_ID","loadUnitMaterial","","");
        }
        
        function getNameVendor(){
            var suplierId = $( "#FRM_FIELD_SUPPLIER_ID option:selected" ).val();
            if (suplierId==<%=oidNewSupplier%>){
                $('#FRM_FIELD_SUPPLIER_NAME').val('');
                $('#FRM_FIELD_SUPPLIER_NAME').focus();
            }else{
               var suplierName = $( "#FRM_FIELD_SUPPLIER_ID option:selected" ).text();
                $('#FRM_FIELD_SUPPLIER_NAME').val(suplierName); 
            }
            
        }
        
        function setPrice(){
            var price = $('#FRM_FIELD_UNIT_REQUEST_ID option:selected').attr('class');
            var oldPrice = $("#FRM_FIELD_PRICE_BUYING").val();
            if (oldPrice>0){
                price = oldPrice;
            }
            //alert(price);
            //price = setPriceFormat(price);
            //alert(price);
            $('#FRM_FIELD_PRICE_BUYING').val(price);
            var suplierName = $( "#FRM_FIELD_SUPPLIER_NAME" ).val();
            
            if (suplierName.length>0){
               $('#FRM_FIELD_QUANTITY').focus(); 
               
            }
            
        }
        
        function saveDetil(){
            $('#frm_purchaseorder #command').val(<%=Command.SAVE%>);
            $('#frm_purchaseorder #prev_command').val(<%=prevCommand%>);
            $("#frm_purchaseorder").attr('action', 'prmaterialitem.jsp');
 
            $("#frm_purchaseorder").submit();
        }
        
        $('#FRM_FIELD_LOCATION_ID').change(function(){
            $('#command').val(<%= Command.SAVE%>);
            $('#prev_command').val(<%=prevCommand%>);
            var data = $('#frm_purchaseorder').serialize();
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            ajaxPurchaseOrder(""+url+"",data,"POST","#prCode","","","");
        });
        
        $('.changeDatePr').change(function(){
            $('#command').val(<%= Command.SAVE%>);
            $('#prev_command').val(<%=prevCommand%>);
            var data = $('#frm_purchaseorder').serialize();
            var url = "<%=approot%>/servlet/com.dimata.common.ajax.AjaxPurchaseOrder";
            ajaxPurchaseOrder(""+url+"",data,"POST","#prCode","","","");
        });
        
        $( ".matCode" ).on( "keydown", function( event ) {
            keyDownCheck(event);
        });
        
        $('#FRM_FIELD_SUPPLIER_NAME').on( "keydown", function( event ){
            var code = event.which;
            var suplierName = $('#FRM_FIELD_SUPPLIER_NAME').val();
            if (code==13 && suplierName.length>0){
                $('#FRM_FIELD_PRICE_BUYING').focus();
            }
        });
        
        
        $('.showCheckMaterial').click(function(){
            check();
        });
        

        
        $('#FRM_FIELD_PRICE_BUYING').keyup(function(){
            var qty = $('#FRM_FIELD_QUANTITY').val();
            var price = $('#FRM_FIELD_PRICE_BUYING').val();
            if (qty==""){
                qty=0;
            }
            if (price==""){
                price=0;
            }
            
            var totalPrice= qty * price;
            $('#FRM_FIELD_TOTAL_PRICE').val(totalPrice);
        });
        
        $('#FRM_FIELD_PRICE_BUYING').on( "keydown", function( e ){
            var price = $('#FRM_FIELD_PRICE_BUYING').val();
            code = e.which;
            if (code==13){
                if (price.length>0 && price>0){
                    $('#FRM_FIELD_QUANTITY').focus();
                }
            }
          
        });
        
        
        $('#FRM_FIELD_QUANTITY').on( "keydown", function( e ){
            var price = $('#FRM_FIELD_PRICE_BUYING').val();
            var qty = $('#FRM_FIELD_QUANTITY').val();
           
            code = e.which;
            if (code==13){
                if (qty.length>0 && qty>0){
                    var totalPrice= qty * price;
                   
                    $('#FRM_FIELD_TOTAL_PRICE').val(totalPrice);
                    saveDetil();
                }
            }
        });
        
    });
</script>
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
            <%=textListOrderHeader[SESS_LANGUAGE][12]%> &gt; <%=textListOrderHeader[SESS_LANGUAGE][13]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form id="frm_purchaseorder" name="frm_purchaseorder" method ="post" action="">
              <input type="hidden" id="command" name="command" value="<%=iCommand%>">
              <input type="hidden" id="prev_command" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
              <input type="hidden" name="hidden_mat_request_item_id" value="<%=oidPurchaseRequestItem%>">
              <input type="hidden" name="<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PURCHASE_REQUEST_ID]%>" value="<%=oidPurchaseRequest%>">
              <input type="hidden" name="<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PURCHASE_REQUEST_ITEM_ID]%>" value="<%=oidPurchaseRequestItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="location_id" value="<%=po.getLocationId()%>">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" id="trap" name="trap" value="">
              <input type="hidden" name="includePrinPrice" value="">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" valign="top" class="title">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top"></tr>
                      <tr>
                        <td align="left" valign="top">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td width="26%"><%=poCode+" "+textListOrderHeader[SESS_LANGUAGE][0]%></td>
                              <td width="74%"><span style="float:left;margin-right:3px;">:</span> &nbsp; <b><div style="float:left" id="prCode"><%=po.getPrCode()%></div></b></td>
                            </tr>
                            <tr>
                              <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                              <td>:
                                <%
                                    Vector val_locationid = new Vector(1,1);
                                    Vector key_locationid = new Vector(1,1);
                                    String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                    whereClause += " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                    Vector vt_loc = PstLocation.list(0, 0, whereClause, "");
                                    for(int d=0;d<vt_loc.size();d++){
                                            Location loc = (Location)vt_loc.get(d);
                                            val_locationid.add(""+loc.getOID()+"");
                                            key_locationid.add(loc.getName());
                                    }
                                    String select_locationid = ""+po.getLocationId(); //selected on combo box
                                %>
				<%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "onChange=\"javascript:changeLocation()\"", "formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                              <td>: <%=ControlDate.drawDateWithStyle(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE], (po.getPurchRequestDate()==null) ? new Date() : po.getPurchRequestDate(), 1, -5, "formElemen changeDatePr",  "")%></td>
                            </tr>
                            <%--
                            <tr>
                              <td height="20" width="28%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                              <td width="3%">:
                                  <%
                                Vector val_terms = new Vector(1,1);
                                Vector key_terms = new Vector(1,1);
                                for(int d=0; d<PstPurchaseOrder.fieldsPurchaseRequestType.length; d++){
                                    val_terms.add(String.valueOf(d));
                                    key_terms.add(PstPurchaseOrder.fieldsPurchaseRequestType[d]);
                                }
                                String select_terms = ""+po.getTermOfPayment();
				%>
                                <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"","formElemen")%>
                                  
                              </td>
                            </tr>
                            --%>
                            <tr>
                              <td width="20%"><%=textListOrderHeader[SESS_LANGUAGE][16]%></td>
                              <td width="3%">:
                                <%
                                    Vector obj_status = new Vector(1,1);
                                    Vector val_status = new Vector(1,1);
                                    Vector key_status = new Vector(1,1);

                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
									
                                    //add by fitra
                                    if((listPurchaseRequestItem!=null) && (listPurchaseRequestItem.size()>0)){
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    }
                                    
                                    // update opie-eyek 19022013
                                    // user bisa memfinalkan purchase request  jika  :
                                    // 1. punya approve document pr = true
                                    // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                    boolean locationAssign=false;
                                    locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",po.getLocationId());
                                    
                                    if((listPurchaseRequestItem!=null) && (listPurchaseRequestItem.size()>0) && locationAssign==true && privApproval==true ){
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    }
                                    
                                    String select_status = ""+po.getPrStatus();
                                    if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                    }else if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                    }else if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    }else{
                                      try {

                                    %>
                                    <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS],null,select_status,val_status,key_status,"onChange=\"changeStatus()\"","formElemen")%> </td>
                                    <%
                                        }catch(Exception ex){
                                            System.out.print("xxxx "+ex);
                                      }
                                     } %>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td valign="top">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][11]%></td>
                              <td>:</td>
                              <td width="20%">
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
                                if(oidPurchaseRequest!=0){
                                    oidCurrency=po.getCurrencyId();
                                }
                                double resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrency);
                              %>
                              <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+po.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\"")%>
                              &nbsp;&nbsp;
                              <%=textListOrderHeader[SESS_LANGUAGE][17]%>&nbsp;&nbsp;
                               <input name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_EXCHANGE_RATE]%>" type="text" class="formElemen" size="10" value="<%=po.getExhangeRate()!=0?po.getExhangeRate():resultKonversi%>" <%=enableInput%>>
                              </td>
                            </tr>
                            <%-- 
                           <tr>
                              <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][16]%></td>
                              <td width="2%">:</td>
                              <td width="79%">
                                <%
                                    Vector obj_status = new Vector(1,1);
                                    Vector val_status = new Vector(1,1);
                                    Vector key_status = new Vector(1,1);

                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
									
                                    //add by fitra
                                    if((listPurchaseRequestItem!=null) && (listPurchaseRequestItem.size()>0)){
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                    }
                                    
                                    // update opie-eyek 19022013
                                    // user bisa memfinalkan purchase request  jika  :
                                    // 1. punya approve document pr = true
                                    // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                    boolean locationAssign=false;
                                    locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",po.getLocationId());
                                    
                                    if((listPurchaseRequestItem!=null) && (listPurchaseRequestItem.size()>0) && locationAssign==true && privApproval==true ){
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    }
                                    
                                    String select_status = ""+po.getPrStatus();
                                    if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                    }else if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                    }else if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    }else{
                                      try {

                                    %>
                                    <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS],null,select_status,val_status,key_status,"onChange=\"changeStatus()\"","formElemen")%> </td>
                                    <%
                                        }catch(Exception ex){
                                            System.out.print("xxxx "+ex);
                                      }
                                     } %>
                              </td>
                            </tr>
                            --%>
                            <tr>
                              <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                              <td width="2%">:</td>
                              <td width="79%">
                                 <textarea name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REMARK]%>" cols="25" rows="4" wrap="VIRTUAL" class="formElemen"><%=po.getRemark()%></textarea>
                              </td>
                            </tr>
                            
                          </table>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top" ><a name="go"></a> 
                    <table width="100%" cellpadding="1" cellspacing="1">
                      <tr>
                        <td colspan="3" > <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top">
                              <%try	
                              {
                                %>
                                  <td height="22" valign="middle"> <%= drawListPoItem(SESS_LANGUAGE,iCommand,frmPurchaseRequestItem, poItem,listPurchaseRequestItem,oidPurchaseRequestItem,startItem,matOid,matCode,matItem,matUnit,matUnitId,po.getTermOfPayment(), oidNewSupplier, approot, useForRaditya)%> </td>  
                                  <%
                                } catch(Exception e) {
                                        System.out.println(e);
                                }
                              %>
                            </tr>
                             <tr align="left" valign="top">
                                 <td height="22" valign="middle"> <%= hasilEmail %> </td>  
                            </tr>
                            <tr align="left" valign="top">
                              <td height="8" align="left" class="command"> <span class="command">
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
                              <td height="22" valign="middle"> 
                                  <%
                                    ctrLine.setLocationImg(approot+"/images");

                                    // set image alternative caption
                                    ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true));
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ASK,true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_CANCEL,false));

                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("65%");
                                    String scomDel = "javascript:cmdAsk('"+oidPurchaseRequestItem+"')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('"+oidPurchaseRequestItem+"')";
                                    String scancel = "javascript:cmdEdit('"+oidPurchaseRequestItem+"')";
                                    ctrLine.setCommandStyle("command");
                                    ctrLine.setColCommStyle("command");

                                    // set command caption
                                    ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true));
                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ASK,true));
                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_DELETE,true));
                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_CANCEL,false));

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

                                      if(documentClosed){
                                          ctrLine.setSaveCaption("");
                                          ctrLine.setDeleteCaption("");
                                          ctrLine.setConfirmDelCaption("");
                                          ctrLine.setCancelCaption("");
                                      }

                                    String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
                                    if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
                                    %> 
                                  <table width="21%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                    <% if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
									<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
									<% } %>
									<td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
                                  </tr>
                                </table>
                                <%
								}else{
									out.println(strDrawImage);
								}
								%> </td>
                            </tr>
                          </table>
                      </td>
                      </tr>
                              <% if(useForRaditya.equals("1")){
                              if(po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED){ %><tr>
                                    <td width="25%" nowrap align="left"> 
                                          <a class="printReport command btn btn-primary" style="color: white" href="#" class="" >Print HTML Mode</a>
                                    </td>
                                </tr>
                              <%}else{%>
                                <tr>
                                    <td width="75%" valign="top" align="right">&nbsp;
                                        <%if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL ) {%>
                                          <a href="javascript:printResentEmail()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a>
                                          &nbsp;<a href="javascript:printResentEmail()" class="command" >Resent Email</a>
                                        <%}%>
                                    </td>
                                    <td width="25%" nowrap align="left"> 
                                        <%if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL ) {%>
                                          <a href="javascript:printResentEmail()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a>
                                          &nbsp;<a href="javascript:printResentEmail()" class="command" >Resent Email</a> |
                                        <%}%>
                                        &nbsp;
                                    </td>
                                </tr>
                              <%  }}else{%>
                                <tr>
                                    <td width="75%" valign="top" align="right">&nbsp;
                                        <%if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL ) {%>
                                          <a href="javascript:printResentEmail()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a>
                                          &nbsp;<a href="javascript:printResentEmail()" class="command" >Resent Email</a>
                                        <%}%>
                                    </td>
                                    <td width="25%" nowrap align="left"> 
                                        <%if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL ) {%>
                                          <a href="javascript:printResentEmail()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a>
                                          &nbsp;<a href="javascript:printResentEmail()" class="command" >Resent Email</a> |
                                        <%}%>
                                        &nbsp;
                                          <a class="printReport command btn btn-primary" style="color: white" href="#" class="" >Print HTML Mode</a>
                                    </td>
                                </tr>
                              <%}%>
                    </table></td>
                </tr>
              </table>
            </form>
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
      
<script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
<script type="text/javascript" src="../../../styles/AdminLTE-2.3.11/plugins/select2/select2.full.min.js"></script>
</body>
      <script>
          $(document).ready(function(){
        $('.printReport').click(function(){
          $('#modalReport').modal('show');
      });          
});
      </script>
<!-- #EndTemplate -->
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        var code = document.frm_purchaseorder.matCode.value;
        var codeItem = document.frm_purchaseorder.matItem.value;
        // add by Fitra
         document.frm_purchaseorder.trap.value="0";
         var trap = document.frm_purchaseorder.trap.value;
         
         
        if(code==""&&codeItem==""){
            document.frm_purchaseorder.matCode.focus();
        }else{
             document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_QUANTITY]%>.focus();
        }
            
    <% } %>
    
    <% if(inputNew == 1) { %>
        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_SUPPLIER_NAME]%>.value="";
        document.frm_purchaseorder.<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PRICE_BUYING]%>.value="";
    <%}%>
        
</script>

<script language="JavaScript">
         document.frmvendorsearch.matCode.focus();
</script>
<%--autocomplate add by fitora --%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
 
      $(document).ready(function () {
  $('.select2').select2({placeholder: "Semua"});
  $('.selectAll').select2({placeholder: "Semua"});
  });
</script>

<script language="JavaScript">
   <%// add by fitra 10-5-2014
  if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED || po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){%>
          cmdBack();
  <% } %>
</script>
<!--skrip untuk modal bootstrap -->


<div id="modalCheck" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg" style="max-width: 1000px; width: 80%;">
        <div class="modal-content">
            <div id="dynamicModalHeader"></div>
            <div id="dynamicModalContent"></div>
        </div>
    </div>
</div>

<div id="modalReport" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg" style="max-width: 1000px; width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title">Report</h4>
            </div>
            <div class="modal-body" id="modal-body">
                <iframe style="width:100%; height:500px;border:none;" src="pr_material_print_form.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice=0"></iframe>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
            </div>
        </div>
    </div>
</div>

</html>


