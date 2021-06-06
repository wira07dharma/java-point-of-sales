<%-- 
    Document   : pomaterialitem_cashier
    Created on : Apr 25, 2014, 5:46:43 PM
    Author     : dimata005
--%>

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

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal","Supplier","Contact","Alamat","Telp.","Terms","Days","PPn","Ket.","Mata Uang","Gudang","Order Barang","Include","%","Rate"},
    {"No","Location","Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Order","Include","%","Rate"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Qty Stock","Unit Stock","Hrg Beli Terakhir","Hrg Stock","Diskon Terakhir %",//
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Nilai Tukar"},
    {"No","Code","Name","Qty Stock","Unit","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Exchange Rate"}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand,FrmPurchaseOrderItem frmObject,
        PurchaseOrderItem objEntity,Vector objectClass,long poItemId,int start, double exhangeRate) {
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    
    ctrlist.addHeader("Qty Beli","5%");
    ctrlist.addHeader("Unit Beli","5%");
    ctrlist.addHeader("Harga / Unit Beli","5%");
    
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"3%");
    ctrlist.addHeader(textListOrderItem[language][5],"8%");
    ctrlist.addHeader(textListOrderItem[language][6],"8%");
    ctrlist.addHeader(textListOrderItem[language][7],"5%");
    ctrlist.addHeader(textListOrderItem[language][8],"5%");
    ctrlist.addHeader(textListOrderItem[language][9],"5%");
    ctrlist.addHeader(textListOrderItem[language][10],"8%");
    ctrlist.addHeader(textListOrderItem[language][11],"8%");
    ctrlist.addHeader(textListOrderItem[language][12],"8%");
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0) {
        start=0;
    }
    
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
        
        if (poItemId == poItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            rowx.add(""+start);
            // code
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+poItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+poItem.getQtyRequest()+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
            rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+poItem.getUnitRequestId(), index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getPriceKonv())+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(this.value)\"</div>");
        
            // qty
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"\" class=\"formElemen\" onKeyUp=\"javascript:calculate()\"  style=\"text-align:right\"></div>");
            // unit
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+poItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            // hrg beli
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getPrice()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly></div>");
            // beli
            rowx.add("<input type=\"text\" size=\"10\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exhangeRate)+"\" class=\"formElemenR\">");
            // discon 1
            rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly>");
            // discon
            rowx.add("<input type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount1()/exhangeRate)+"\" class=\"formElemenR\">");
            // discount 2
            rowx.add("<input type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscount2()/exhangeRate)+"\" class=\"formElemenR\">");
            // nilai nominal
            rowx.add("<input type=\"text\" size=\"10\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()/exhangeRate)+"\" class=\"formElemenR\">");
            // tot
            rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()/exhangeRate)+"\" class=\"formElemenR\">");
            // total
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getTotal()/exhangeRate)+"\" class=\"hiddenTextR\" readOnly></div>");
        
        }else{
            
            rowx.add(""+start+"");
            
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(poItem.getOID())+"')\">"+mat.getSku()+"</a>");
            
            rowx.add(mat.getName());
            
            rowx.add("<div align=\"right\">"+poItem.getQtyRequest()+"</div>");
            rowx.add("<div align=\"right\">"+unitKon.getCode()+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getPriceKonv())+"</div>");
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            // unit
            rowx.add(unit.getCode());
            // harga beli
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getPrice()/exhangeRate)+"</div>");
            // harga
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exhangeRate)+"</div>");
            // discount
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount()/exhangeRate)+"</div>");
            // discount 1
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount1()/exhangeRate)+"</div>");
            // discount 2
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount2()/exhangeRate)+"</div>");
            // disc nominal
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()/exhangeRate)+"</div>");
            // curr harga beli
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()/exhangeRate)+"</div>");
            // total
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getTotal()/exhangeRate)+"</div>");
        }
        lstData.add(rowx);
    }
    
    rowx = new Vector();
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)){
        rowx.add(""+(start+1));
        // code
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+
                "\"><input tabindex=\"1\" type=\"text\" size=\"13\" name=\"matCode\" value=\"\" class=\"formElemen\"><a tabindex=\"2\" href=\"javascript:cmdCheck()\">CHK</a>");
        // name
        //rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"\" class=\"hiddenText\" readOnly>");
        rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
        
        //update opie-eyek
        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY_INPUT] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:change(this.value)\"</div>");
        rowx.add("<div align=\"center\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID_KONVERSI], null, ""+0, index_value, index_key,"onChange=\"javascript:showData(this.value)\"","formElemen")+" </div>");
        rowx.add("<div align=\"center\"><input type=\"text\" size=\"7\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_KONVERSI] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:changePriceKonv(this.value)\"</div>");
        
        // qty
        rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate()\"  style=\"text-align:right\"></div>");
        // unit
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>");
        // hrg beli
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>");
        // beli
        rowx.add("<input tabindex=\"4\" type=\"text\" size=\"10\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ORG_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">");
        // discon 1
        rowx.add("<input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT]+"\" value=\"\" class=\"hiddenTextR\" readOnly>");
        // discon
        rowx.add("<input tabindex=\"5\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT1]+"\" value=\"\" class=\"formElemenR\">");
        // discount 2
        rowx.add("<input tabindex=\"6\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT2]+"\" value=\"\" class=\"formElemenR\">");
        // nilai nominal
        rowx.add("<input tabindex=\"7\" type=\"text\" size=\"10\" onKeyUp=\"javascript:calculate()\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_DISCOUNT_NOMINAL]+"\" value=\"\" class=\"formElemenR\">");
        // tot
        rowx.add("<input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURR_BUYING_PRICE]+"\" value=\"\" class=\"formElemenR\">");
        // total
        rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\"\" class=\"hiddenTextR\" readOnly></div>");
        
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
long oidPurchaseOrder = FRMQueryString.requestLong(request,"hidden_material_order_id");
long oidPurchaseOrderItem = FRMQueryString.requestLong(request,"hidden_mat_order_item_id");
double DefaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
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
iErrCode = ctrlPurchaseOrder.action(Command.EDIT,oidPurchaseOrder);
FrmPurchaseOrder frmPurchaseOrder = ctrlPurchaseOrder.getForm();
PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();

ControlLine ctrLine = new ControlLine();
CtrlPurchaseOrderItem ctrlPurchaseOrderItem = new CtrlPurchaseOrderItem(request);
ctrlPurchaseOrderItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlPurchaseOrderItem.action(iCommand,oidPurchaseOrderItem,oidPurchaseOrder,userName, userId);
FrmPurchaseOrderItem frmPurchaseOrderItem = ctrlPurchaseOrderItem.getForm();
PurchaseOrderItem poItem = ctrlPurchaseOrderItem.getPurchaseOrderItem();
msgString = ctrlPurchaseOrderItem.getMessage();

String whereClauseItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder;
String orderClauseItem = "";
int vectSizeItem = PstPurchaseOrderItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    startItem = ctrlPurchaseOrderItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

whereClauseItem = "POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder;
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
	document.frm_purchaseorder.action="pomaterial_edit.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd(){
	document.frm_purchaseorder.hidden_mat_order_item_id.value="0";
	document.frm_purchaseorder.command.value="<%=Command.ADD%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_purchaseorder.submit();
}

function cmdEdit(oidPurchaseOrderItem)
{
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdAsk(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.ASK%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdSave(){
	document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdConfirmDelete(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdCancel(oidPurchaseOrderItem){
	document.frm_purchaseorder.hidden_mat_order_item_id.value=oidPurchaseOrderItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBack(){
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.start_item.value = 0;
	document.frm_purchaseorder.action="pomaterial_edit.jsp";
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
    var strvalue  = "materialdosearch.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value+
                                    "&mat_vendor="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value+
                                    "&rate="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>.value+
                                    "&currency_id="+document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID]%>.value;
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function calculate(){
    var qty = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup,guiDecimalSymbol);
    var cost = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
    var lastDisc = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_DISCOUNT1]%>.value,guiDigitGroup,guiDecimalSymbol);
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
    if(isNaN(lastDiscNom) || (lastDiscNom==""))
    lastDiscNom = 0.0;

    var totaldiscount = cost * lastDisc / 100;
    var totalMinus = cost - totaldiscount;
    var totaldiscount2 = totalMinus * lastDisc2 / 100;

    var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
    var lastTotal = qty * totalCost;
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_CURR_BUYING_PRICE]%>.value = totalCost;//formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value = lastTotal;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
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
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListPrev(){
	document.frm_purchaseorder.command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListNext(){
	document.frm_purchaseorder.command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListLast(){
	document.frm_purchaseorder.command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.action="pomaterialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBackList(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="pomaterial_list.jsp";
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
<script src="../../../styles/jquery.min.js"></script>
<!-- #BeginEditable "headerscript" -->
<script type="text/javascript">
function change(value){
    
     document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value=value;
     document.frm_purchaseorder.hidden_qty_input.value=value;
     var oidKonversiUnit= document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
     //changePriceKonv();
     showData(oidKonversiUnit);
}         

function changePriceKonv(value){
    var oidUnit = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value;
    var oidUnitKonv = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>.value;
    
    var qtyBeli = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>.value;
    var costBeli = cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.value,guiDigitGroup,guiDecimalSymbol);
    
    var qtyKonv =cleanNumberFloat(document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QUANTITY]%>.value,guiDigitGroup,guiDecimalSymbol);
    
    
    var totalBeli = (qtyBeli*costBeli)/qtyKonv;
    
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_ORG_BUYING_PRICE]%>.value=parseFloat(totalBeli);
    
    var total = qtyKonv*totalBeli;
    
    document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_TOTAL]%>.value=parseFloat(total);
}

function showData(value){
  
   var qtyInput = document.frm_purchaseorder.hidden_qty_input.value;
   var oidUnit = document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>.value;
   var oidKonversiUnit=value;
    
   checkAjax(oidKonversiUnit,oidUnit,qtyInput);
   changePriceKonv();
   document.frm_purchaseorder.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PRICE_KONVERSI]%>.focus();
}

function checkAjax(oidKonversiUnit,oidUnit, qtyInput){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"",
    type : "POST",
    async : false,
    success : function(data) {
        //alert(data);
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
            <form name="frm_purchaseorder" method ="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
              <input type="hidden" name="hidden_mat_order_item_id" value="<%=oidPurchaseOrderItem%>">
              <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PURCHASE_ORDER_ID]%>" value="<%=oidPurchaseOrder%>">
              <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_PURCHASE_ORDER_ITEM_ID]%>" value="<%=oidPurchaseOrderItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_qty_input" value="">
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr align="center">
                  <td colspan="3" valign="top" class="title">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="37%" align="left" valign="top" class="comment">&nbsp;</td>
                        <td width="38%" align="center" valign="bottom">&nbsp;</td>
                        <td width="25%" valign="top"></tr>
                      <tr>
                        <td align="left" valign="top">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                        <td valign="top"></tr>
                      <tr>
                        <td align="left" valign="top">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td width="26%"><%=poCode+" "+textListOrderHeader[SESS_LANGUAGE][0]%></td>
                              <td width="74%">: <b><%=po.getPoCode()%></b></td>
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
				<%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                              <td>: <%=ControlDate.drawDateWithStyle(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE], (po.getPurchDate()==null) ? new Date() : po.getPurchDate(), 1, -5, "formElemen", "")%></td>
                            </tr>
                            <tr>
                              <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][11]%></td>
                              <td>:
                                <%
                                    Vector listCurr = PstCurrencyType.list(0,0,"","");
                                    Vector vectCurrVal = new Vector(1,1);
                                    Vector vectCurrKey = new Vector(1,1);
                                    for(int i=0; i<listCurr.size(); i++){
                                            CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                            vectCurrKey.add(currencyType.getCode());
                                            vectCurrVal.add(""+currencyType.getOID());
                                    }
                                   // double resultKonversi = PstDailyRate.getCurrentDailyRateSales(po.getCurrencyId());
                                 %>
                                 <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+po.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                              &nbsp;&nbsp;
                              <%=textListOrderHeader[SESS_LANGUAGE][16]%>&nbsp;&nbsp;
                               <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>" type="text" class="formElemen" size="10" value="<%=po.getExchangeRate()%>" readonly >
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td valign="top"><table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td width="21%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                              <td width="79%">:
                                <%
                                    Vector val_supplier = new Vector(1,1);
                                    Vector key_supplier = new Vector(1,1);
                                    //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
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
                                <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"onChange=\"javascript:changeVendor()\"","formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                              <td>:<input name="hid_contact" disable="true" type="text" readonly class="hiddenTextMain" size="30"></td>
                            </tr>
                            <tr>
                              <td><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                              <td>:<input name="hid_addres" disable="true" type="text" readonly class="hiddenTextMain" size="40"></td>
                            </tr>
                            <tr>
                              <td><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                              <td>:<input name="hid_phone" disable="true" type="text" readonly class="hiddenTextMain" size="30"></td>
                            </tr>
                          </table></td>
                        <td valign="top">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td width="24%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                              <td width="76%">
                                <%
                                    Vector val_terms = new Vector(1,1);
                                    Vector key_terms = new Vector(1,1);
                                    for(int d=0; d<PstPurchaseOrder.fieldsPaymentType.length; d++){
                                            val_terms.add(String.valueOf(d));
                                            key_terms.add(PstPurchaseOrder.fieldsPaymentType[d]);
                                    }
                                    String select_terms = ""+po.getTermOfPayment();
                                %>
                                <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"","formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                              <td><input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>" type="text" class="formElemen" size="5" value="<%=po.getCreditTime()%>"></td>
                            </tr>
                            <tr>
                              <td valign="top"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                              <td><textarea name="textarea" cols="20" rows="3" wrap="VIRTUAL" class="formElemen"><%=po.getRemark()%></textarea></td>
                            </tr>
                          </table>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td valign="top" ><a name="go"></a> <table width="100%" cellpadding="1" cellspacing="1">
                      <tr>
                        <td colspan="3" > <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top">
                              <%try	{
								%>
                              <td height="22" valign="middle"> <%= drawListPoItem(SESS_LANGUAGE,iCommand,frmPurchaseOrderItem, poItem,listPurchaseOrderItem,oidPurchaseOrderItem,startItem,po.getExchangeRate())%> </td>
                              <%
								} catch(Exception e) {
									System.out.println(e);
								}
							  %>
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
                              <td height="22" valign="middle"> <%
								ctrLine.setLocationImg(approot+"/images");

								// set image alternative caption
								ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ASK,true));
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_CANCEL,false));

								ctrLine.initDefault();
								ctrLine.setTableWidth("65%");
								String scomDel = "javascript:cmdAsk('"+oidPurchaseOrderItem+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidPurchaseOrderItem+"')";
								String scancel = "javascript:cmdEdit('"+oidPurchaseOrderItem+"')";
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
                                      //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                                      ctrLine.setDeleteCaption("");
                                      ctrLine.setConfirmDelCaption("");
                                      ctrLine.setCancelCaption("");
                                  }

                                String  strDrawImage = ctrLine.drawImage(iCommand,iErrCode,msgString);
								if((iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) && strDrawImage.length()==0){
								%> <table width="21%" border="0" cellspacing="2" cellpadding="0">
                                  <tr>
                                    <% if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
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
                          </table></td>
                      </tr>
                      <%if(listPurchaseOrderItem!=null && listPurchaseOrderItem.size()>0){%>
                      <tr>
                        <td colspan="2" valign="top">&nbsp; </td>
                        <td width="30%" valign="top"> <table width="100%" border="0">
                            <tr>
                              <td align="right"><%="SUB TOTAL :"%></td>
                              <td>&nbsp;</td>
                              <td><div align="right">
                                  <%
									  String whereItem = ""+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder;
									  //out.println(Formater.formatNumber(PstPurchaseOrderItem.getTotal(whereItem),"##,###.00"));
									  double total = PstPurchaseOrderItem.getTotal(whereItem)/po.getExchangeRate();
									  //out.println(FRMHandler.userFormatStringDecimal(total));
                                                                          double lastPpn = po.getPpn()/po.getExchangeRate();
                                                                          if(lastPpn == 0){
                                                                                lastPpn  = DefaultPpn/po.getExchangeRate();
                                                                          }
									  //double ppn = total * po.getPpn() / 100;
                                                                          double ppn = total * lastPpn / 100;
									  ppn = total + ppn;

                                                                          //include or not include
                                                                         double valuePpn = 0.0;
                                                                        if(po.getIncludePpn()== 1){
                                                                        valuePpn =total - (total /1.1);
                                                                         }
                                                                        else if(po.getIncludePpn()== 0){
                                                                        valuePpn = total * lastPpn / 100;;
                                                                        }
                                  %>
                                </div></td>
                            </tr>
                            <tr>
                              <td width="44%" align="right"> <div align="right"><input type="checkbox" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if(po.getIncludePpn()==1){%>checked<%}%> ><%=textListOrderHeader[SESS_LANGUAGE][14]%>&nbsp;
                                      <%=textListOrderHeader[SESS_LANGUAGE][9]%><input type="text"  class="formElemen" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PPN]%>" value="<%if(ppn != 0.0){%><%=FRMHandler.userFormatStringDecimal(lastPpn)%><%}else {%><%=FRMHandler.userFormatStringDecimal(DefaultPpn)%><%}%>"  size="5" style="text-align:right" readonly class="hiddenTextMain">&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][15]%> :</div></td>
                              <td width="5%" align="left"> <div align="right"></div></td>
                              <td width="51%"><div align="right">
                                  <!--<input name="<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PPN]%>" type="text" class="formElemenR" value="<%//=FRMHandler.userFormatStringDecimal(po.getPpn())%>" size="5" readonly >-->
                                      <%=FRMHandler.userFormatStringDecimal(valuePpn)%>
                                </div></td>
                            </tr>
                            <tr>
                              <td align="right">TOTAL : </td>
                              <td>&nbsp;</td>
                              <% if (po.getIncludePpn()==1) { %>
                        <td><div align="right"><b><%=FRMHandler.userFormatStringDecimal(total)%></b></div></td>
                        <%}
                         else {%>
                          <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(ppn)%></b></div></td>
                       <%}%>
                              
                            </tr>
                          </table></td>
                      </tr>
                      <%if(listPurchaseOrderItem!=null && listPurchaseOrderItem.size()>0){%>
                      <tr>
                        <td colspan="3"></td>
                      </tr>
                      <tr>
                        <td colspan="3">&nbsp;</td>
                      </tr>
                      <%}%>
                      <%}%>
                      <tr>
                        <td colspan="3"> <%
							/*ctrLine.setLocationImg(approot+"/images");

							// set image alternative caption
							ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
							ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

							ctrLine.initDefault();
							ctrLine.setTableWidth("80%");
							String scomDelMat = "javascript:main('"+oidPurchaseOrder+"','"+Command.ASK+"')";
							String sconDelComMat = "javascript:main('"+oidPurchaseOrder+"','"+Command.DELETE+"')";
							String scancelMat = "javascript:cmdEdit('"+oidPurchaseOrder+"','"+Command.EDIT+"')";
							ctrLine.setSaveCommand("javascript:main('"+oidPurchaseOrder+"','"+Command.SAVE+"')");
							ctrLine.setBackCommand("javascript:cmdBackList()");
							ctrLine.setCommandStyle("command");
							ctrLine.setColCommStyle("command");

							// set command caption
							ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
							ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
							ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
							ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_DELETE,true));
							ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

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

							if(iCommand==Command.SAVE && frmPurchaseOrder.errorSize()==0){
								iCommand=Command.EDIT;
							}

							if(documentClosed){
								ctrLine.setSaveCaption("");
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
								ctrLine.setDeleteCaption("");
								ctrLine.setConfirmDelCaption("");
								ctrLine.setCancelCaption("");
							}*/
							%> </td>
                      </tr>
                    </table></td>
                </tr>
              </table>
            </form>
			  <script language="JavaScript">
					changeVendor();
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
<!-- #EndTemplate -->
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_purchaseorder.matCode.focus();
    <% } %>
</script>
</html>

