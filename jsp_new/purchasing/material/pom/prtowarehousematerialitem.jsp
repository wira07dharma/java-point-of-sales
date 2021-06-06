<%-- 
    Document   : prmaterialitem
    Created on : Feb 5, 2014, 10:27:57 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatDispatch"%>
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Asal Barang","Tanggal","Supplier","Contact","Alamat","Telp.","Terms","Days","PPn","Ket.","Mata Uang","Gudang","Store Request","Include","%", "Nomor BC", "Jenis Dokumen","Tujuan Baarang", "Request Transfer"},
    {"No","Order Location","Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Store Request","Include","%", "BC Number", "Document Type","Request Location", "Request Transfer"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Qty","Unit","Hrg Beli Terakhir","Hrg Beli","Diskon Terakhir %",
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Hapus"},
    {"No","Code","Name","Qty","Unit","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Delete"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

public static int getStrDutyFree(){
	String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
	System.out.println("#Duty Free: " + strDutyFree);
	int dutyFree = Integer.parseInt(strDutyFree);
	return dutyFree;
}
/**
* this method used to maintain poMaterialList
*/
public String drawListPoItem(int language,int iCommand,FrmPurchaseRequestItem frmObject,
        PurchaseRequestItem objEntity,Vector objectClass,long poItemId,int start, String approot) {
    String useForRaditya = PstSystemProperty.getValueByName("USE_FOR_RADITYA");
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    ctrlist.addHeader(textListOrderItem[language][0],"3%");
    ctrlist.addHeader(textListOrderItem[language][1],"10%");
    ctrlist.addHeader(textListOrderItem[language][2],"15%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");
    ctrlist.addHeader(textListOrderItem[language][4],"3%");
    if (useForRaditya.equals("0")){
        ctrlist.addHeader("Supplier","5%");
        ctrlist.addHeader("Supplier Name","3%");
        ctrlist.addHeader("Price","5%");
        ctrlist.addHeader("Unit Request","3%");
        ctrlist.addHeader("Total Price","3%");
    }
    
//    ctrlist.addHeader(textListOrderItem[language][4],"3%");
    ctrlist.addHeader(textListOrderItem[language][13],"3%");
    
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0) {
        start=0;
    }

    String whereSupplier = "";
    Vector listMatVendorPrice = PstMatVendorPrice.listFiltter(start, 0, whereSupplier, "");
    Vector supplierVal = new Vector();
    Vector supplierKey = new Vector();
    long firstVendor = 0;
    String firstVendorName ="";
    double totalPrice = 0.0;
    double firstPriceBuying =0.0;
    for(int i = 0; i < listMatVendorPrice.size(); i++){
    MatVendorPrice matVendorPrice = (MatVendorPrice) listMatVendorPrice.get(i);
    if(i==0){
      firstVendor = matVendorPrice.getVendorId();
      firstVendorName = PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId());
    }
    supplierKey.add(""+matVendorPrice.getVendorId());
    supplierVal.add(""+PstMatVendorPrice.getVendorName(matVendorPrice.getVendorId())); 
    }

    Vector untiVal = new Vector();
    Vector unitKey = new Vector();
    if(firstVendor > 0){
    String whereUnit = "";
    Vector listJoinUnit = PstMatVendorPrice.listJoinUnit(start,0, whereUnit , "");
    for(int i=0; i<listJoinUnit.size(); i++)
            {
                MatVendorPrice matVendorPrice = (MatVendorPrice)listJoinUnit.get(i);
                if(i==0){
                            firstPriceBuying=matVendorPrice.getOrgBuyingPrice();
                }
                unitKey.add(""+matVendorPrice.getBuyingUnitId());
                untiVal.add(""+matVendorPrice.getBuyingUnitName());
            } 
    }else{
    //tampilkan unit yang bisa di beli di material tsb
        String whereBaseUnitOrder = "";
        Vector listBaseUnitOrder = PstMaterialUnitOrder.listJoin(0,0,whereBaseUnitOrder,"");
        for(int i=0; i<listBaseUnitOrder.size(); i++){
            MaterialUnitOrder materialUnitOrder = (MaterialUnitOrder)listBaseUnitOrder.get(i);
                unitKey.add(""+materialUnitOrder.getUnitID());
                untiVal.add(""+materialUnitOrder.getUnitKode());
            }
    }
    
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        Unit unitRequest = new Unit();
        try{
           unitRequest = PstUnit.fetchExc(poItem.getUnitRequestId());
        }catch(Exception ex){}
        //MatCurrency matCurrency = (MatCurrency)temp.get(3);
        rowx = new Vector();
        start = start + 1;
        totalPrice = poItem.getQuantity() * poItem.getPriceBuying();
        if (poItemId == poItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) { 
            rowx.add(""+start);
            // code
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+poItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\" readOnly>");
            // qty
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\""+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(this, event)\"  style=\"text-align:right\"></div>");
            // unit
            
//            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID]+"\" value=\""+poItem.getUnitId()+
//                    "\"><input type=\"text\" size=\"5\" name=\"matUnitReq\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");

            //kode supplier
            if (useForRaditya.equals("0")){
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+poItem.getUnitId()+"\">"
                        + "<input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID],"formElemen", null, ""+poItem.getSupplierId(), supplierKey, supplierVal, ""));
                rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\""+poItem.getSupplierName()+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\""+poItem.getPriceBuying()+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", unitKey, untiVal, "")+"</div>");
            
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"' value=\""+poItem.getTotalPrice()+"\" readOnly style=\"text-align:right\"></div>");
            } else {
                rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+poItem.getUnitId()+"\">"
                            + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID]+"\" value=\""+poItem.getSupplierId()+"\">"
                            + "<input type=\"hidden\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\""+poItem.getSupplierName()+"\">"
                            + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\""+poItem.getPriceBuying()+"\">"
                            + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID]+"\" value=\"\">"
                            + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"' value=\""+poItem.getTotalPrice()+"\">"
                            + "<input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            }

            rowx.add("");
            
        }else{
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(poItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(mat.getName());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            // unit
            rowx.add(""+unit.getCode());
            //rowx.add(""+poItem.getUnitRequestId());
            // add by fitra 17-05-2014
            if (useForRaditya.equals("0")){
                rowx.add(""+poItem.getSupplierName());//nama supplier
                rowx.add(""+poItem.getSupplierName());//nama supplier
                rowx.add("<div align=\"right\">"+Formater.formatNumber(poItem.getPriceBuying(), "###,###")+"</div>"); //price
                rowx.add(""+unitRequest.getCode());//unit order
                rowx.add("<div align=\"right\">"+Formater.formatNumber(totalPrice,"###,###")+"</div>"); //price
            }
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><li class=\"fa fa-trash\"></li></a></div>");
            
        } 
        lstData.add(rowx);
    }

    rowx = new Vector();
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)){
        rowx.add(""+(start+1));
        // code
        rowx.add("<input type=\"hidden\" onKeyUp=\"javascript:cntTotal(event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+
                "\"><input tabindex=\"1\" type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\"\" class=\"formElemen\"><a tabindex=\"2\" href=\"javascript:cmdCheck()\">CHK</a>");
        // name
        //rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"\" class=\"hiddenText\" readOnly>");
        rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\" ><a href=\"javascript:cmdCheck()\">CHK</a>");
        // qty
        rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QUANTITY] +"\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(this, event)\"  style=\"text-align:right\"></div>");
        // unit
        if (useForRaditya.equals("0")){
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+
                        "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>");
            rowx.add(ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID],"formElemen", null, "", supplierKey, supplierVal, ""));
            rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"15\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\""+firstVendorName+"\" class=\"formElemen\" style=\"text-align:left\"></div>");
            rowx.add("<div align=\"left\"><input tabindex=\"3\" type=\"text\" size=\"8\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\""+firstPriceBuying+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
            rowx.add("<div align=\"center\" id=\"posts\">"+ControlCombo.draw(frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID],"formElemen", null, "", unitKey, untiVal, "")+"</div>");
            rowx.add("<div align=\"left\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE]+"' value=\""+totalPrice+"\" readOnly style=\"text-align:right\"></div>");
        } else {
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\"\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_ID]+"\" value=\"\">"
                    + "<input type=\"hidden\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME]+"' name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_SUPPLIER_NAME] +"\" value=\"\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_PRICE_BUYING] +"\" id='FRM_FIELD_PRICE_BUYING' value=\"0\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID] +"\" id='"+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID]+"' value=\"0\">"
                    + "<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] +"\" value=\"\">"
                    + "<input type=\"text\" size=\"5\" name=\"matUnit\" value=\"\" class=\"hiddenText\" readOnly>");
        }
        
//        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_REQUEST_ID]+"\" value=\""+
//                        "\"><input type=\"text\" size=\"5\" name=\"matUnitReq\" value=\"\" class=\"hiddenText\" readOnly>");
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
iErrCode = ctrlPurchaseRequest.action(Command.EDIT,oidPurchaseRequest,"",0);
FrmPurchaseRequest frmPurchaseRequest = ctrlPurchaseRequest.getForm();
PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();




ControlLine ctrLine = new ControlLine();
CtrlPurchaseRequestItem ctrlPurchaseRequestItem = new CtrlPurchaseRequestItem(request);
ctrlPurchaseRequestItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlPurchaseRequestItem.action(iCommand,oidPurchaseRequestItem,oidPurchaseRequest,userName, userId);
FrmPurchaseRequestItem frmPurchaseRequestItem = ctrlPurchaseRequestItem.getForm();
PurchaseRequestItem poItem = ctrlPurchaseRequestItem.getPurchaseRequestItem();
msgString = ctrlPurchaseRequestItem.getMessage();

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
if(iCommand==Command.SAVE && iErrCode == 0) {
	iCommand = Command.ADD;
}

// add by fitra 17-05-2014
if(iCommand==Command.DELETE && iErrCode==0) {
%>
    <jsp:forward page="prtowarehousematerial_edit.jsp">
    <jsp:param name="hidden_mat_request_item_id" value="<%=oidPurchaseRequestItem%>"/>
    <jsp:param name="command" value="<%=Command.EDIT%>" />	
    </jsp:forward>

<%
}
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>
    <link rel="stylesheet" type="text/css" href="../../../styles/prochain.css"/>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

<!--window.location = "#go";-->


//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm){
	document.frm_purchaseorder.command.value=comm;
	document.frm_purchaseorder.hidden_material_request_id.value=oid;
	document.frm_purchaseorder.action="prtowarehousematerial_edit.jsp";
	document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd(){
	document.frm_purchaseorder.hidden_mat_request_item_id.value="0";
	document.frm_purchaseorder.command.value="<%=Command.ADD%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_purchaseorder.submit();
}

function cmdEdit(oidPurchaseRequestItem)
{
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdAsk(oidPurchaseRequestItem){
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.ASK%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdSave(){
	document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdConfirmDelete(oidPurchaseRequestItem){
	document.frm_purchaseorder.hidden_mat_request_item_id.value=oidPurchaseRequestItem;
	document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
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
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBack(){
	document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
	document.frm_purchaseorder.start_item.value = 0;
	document.frm_purchaseorder.action="prtowarehousematerial_edit.jsp";
	document.frm_purchaseorder.submit();
}

function sumPrice()
{
    
}

function cmdCheck(){
    var strvalue  = "materialrequesttowarehousesearch.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&location_id=<%=po.getLocationId()%>"+
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value;
    window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}




function cmdListFirst(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}


function keyDownCheck(e){
    var trap = document.frm_purchaseorder.trap.value;
    if (e.keyCode == 13 && trap==0) {
    document.frm_purchaseorder.trap.value="1";
    }


    
    // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_purchaseorder.matItem.value == "" ){
        document.frm_purchaseorder.trap.value="0";
        cmdCheck();
   }
    
    if (e.keyCode == 13 && trap=="1") {
      document.frm_purchaseorder.trap.value="0";
      cmdCheck();
    }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_purchaseorder.matCode.focus();
       document.frm_purchaseorder.txt_materialname.value="";
    }

}

function calculate(evenClick){
   //formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    if (evenClick.keyCode == 13) {
        cmdSave();
   }
    
}

function cntTotal(element, evt)
    {

        var qty = document.frm_purchaseorder.FRM_FIELD_QUANTITY.value;
        var price = document.frm_purchaseorder.FRM_FIELD_PRICE_BUYING.value;
        
        if (qty < 0.0000) {
            document.frm_purchaseorder.FRM_FIELD_QUANTITY.value = 0;
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
            document.frm_purchaseorder.FRM_FIELD_TOTAL_PRICE.value = amount;
        } else {
            document.frm_purchaseorder.FRM_FIELD_QUANTITY.focus();
        }

        if (evt.keyCode == 13) {
            changeFocus(element);
        }

    }

function cmdListPrev(){
	document.frm_purchaseorder.command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.PREV%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListNext(){
	document.frm_purchaseorder.command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.NEXT%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdListLast(){
	document.frm_purchaseorder.command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.prev_command.value="<%=Command.LAST%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
	document.frm_purchaseorder.submit();
}

function cmdBackList(){
	document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
	document.frm_purchaseorder.action="prtowarehousematerialitem.jsp";
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%if(menuUsed == MENU_ICON){%><link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" /><%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
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
<style>
  .listgentitle {
    font-size: 11px;
    font-style: normal;
    font-weight: bold;
    color: #FFFFFF;
    background-color: #3e85c3 !important;
    text-align: center;
    border: 1px solid #fff !important;
}.listgensell {
    color: #000000;
    background-color: #ffffff !important;
    border: 1px solid #3e85c3 !important;    
    text-align: center;
}.hiddenText {
    color: #000000;
    background-color: #e4e4e4;
    font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
    font-size: 11px;
    width: 100%;
    border: none !important;
    border-radius: 0 !important;
    height: 100%;
}

.form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
        cursor: not-allowed;
        background-color: #fff !important;
        opacity: 1;
      }
select {
    height: 30px !important;
    border: 1px solid #ccc !important;
    width: 100%;
}
textarea.formElemen {
    border-radius: 0px;
    width: 100%;
    border: 1px solid #ccc;
}
.row {
    margin-bottom: 10px;
}
label.control-label.col-sm-4.padd {
    padding: 10px 0px;
}

.col-sm-8 {
    margin-bottom: 10px;
}
label.control-label.col-sm-4 {
    padding-top: 10px;
}
select.tanggal {
    width: 30.9%;
}
input#selectNomor {
    border: 1px solid #ccc;
}
table.listarea {
    width: 95%;
    margin-left: 10px;
}
input {
    border: 1px solid #ccc !important;
    border-radius: 0 !important;
}
input.formElemen {
    margin-right: 10px !important;
    width: 80%;
}
table.table {
    margin-left: 15px;
}
</style>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<section class="content-header">
      <%if(getStrDutyFree() == 1){%>
      <h1><%=textListOrderHeader[SESS_LANGUAGE][13]%>
        <small> Item</small></h1>
        <%}else{%>
      <h1><%=textListOrderHeader[SESS_LANGUAGE][19]%>
        <small> Item</small></h1>
        <%}%>
<ol class="ol">
  <li>
    <a>
      <li class="active">Transfer</li>
    </a>
  </li>
</ol>
</section>
<p class="border"></p>
<div class="container-pos">
  <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if (menuUsed == MENU_PER_TRANS) {%>
    <tr> <td height="25" ID="TOPTITLE"><%@ include file = "../../../main/header.jsp" %></td> </tr>
    <tr> <td height="20" ID="MAINMENU"><%@ include file = "../../../main/mnmain.jsp" %></td></tr>
      <%} else {%>
    <tr bgcolor="#FFFFFF"><td height="10" ID="MAINMENU"><%@include file="../../../styletemplate/template_header.jsp" %></td> </tr><%}%>
    <tr>
      <td valign="top" align="left">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td><!-- #BeginEditable "content" -->
              <form name="frm_purchaseorder" method ="post" action="">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="start_item" value="<%=startItem%>">
                <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
                <input type="hidden" name="hidden_mat_request_item_id" value="<%=oidPurchaseRequestItem%>">
                <input type="hidden" name="<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PURCHASE_REQUEST_ID]%>" value="<%=oidPurchaseRequest%>">
                <input type="hidden" name="<%=FrmPurchaseRequestItem.fieldNames[FrmPurchaseRequestItem.FRM_FIELD_PURCHASE_REQUEST_ITEM_ID]%>" value="<%=oidPurchaseRequestItem%>">
                <input type="hidden" name="approval_command" value="<%=appCommand%>">
                <input type="hidden" name="location_id" value="<%=po.getLocationId()%>">
                <input type="hidden" name="trap" value="">
                <table width="100%" cellpadding="1" cellspacing="0">
                  <div class="row">
                    <div class="col-sm-12">
                      <!--nomor-->
                      <div class="col-sm-4">
                        <div class="form-group">
                          <label class="control-label col-sm-4" for="selectNomor"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label>
                          <div class="col-sm-8">
                            <input id="selectNomor" class="form-control" type="text" name="" value="<%=po.getPrCode()%>" readonly="">
                          </div>
                        </div>

                        <div class="form-group">
                          <label class="control-label col-sm-4" for="tanggal"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label>
                          <div class="col-sm-8">
                            <%=ControlDate.drawDate(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE], po.getPurchRequestDate(), "tanggal", 1, -5)%>  &nbsp;
                          </div>
                        </div>
                      </div>
                      <div class="col-sm-4">
                        <div class="form-group">
                          <label class="control-label col-sm-4" for="lokasi-asal"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label>
                          <div class="col-sm-8">
                            <%
                              Vector val_locationid = new Vector(1, 1);
                              Vector key_locationid = new Vector(1, 1);
                              String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                              whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                              Vector vt_loc = PstLocation.list(0, 0, whereClause, "");
                              for (int d = 0; d < vt_loc.size(); d++) {
                                Location loc = (Location) vt_loc.get(d);
                                val_locationid.add("" + loc.getOID() + "");
                                key_locationid.add(loc.getName());
                              }
                              String select_locationid = "" + po.getLocationId(); //selected on combo box
%>
                            <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                          </div>
                        </div>
                        <div class="form-group">
                          <label class="control-label col-sm-4 padd" for="lokasi-tujuan"><%=textListOrderHeader[SESS_LANGUAGE][18]%></label>
                          <div class="col-sm-8">
                            <%
                              Vector val_locationidSup = new Vector(1, 1);
                              Vector key_locationidSup = new Vector(1, 1);

                              //add opie-eyek
                              //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                              //Vector vt_loc_sup = PstLocation.list(0, 0, "", "");
                              for (int d = 0; d < vt_loc.size(); d++) {
                                Location supplierFromWarehouse = (Location) vt_loc.get(d);
                                val_locationidSup.add("" + supplierFromWarehouse.getOID() + "");
                                key_locationidSup.add(supplierFromWarehouse.getName());
                              }

                              String select_locationid_sup = "" + po.getWarehouseSupplierId(); //selected on combo box
%>
                            <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_WAREHOUSE_SUPP_ID], null, select_locationid_sup, val_locationidSup, key_locationidSup, "", "formElemen")%>
                          </div>
                        </div>
                      </div>
                      <!--Nomor BC-->
                      <div class="col-sm-4">
                        <div class="form-group">
                          <label class="control-label col-sm-4" for="textarea"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label>
                          <div class="col-sm-8">
                            <textarea id="textarea" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=po.getRemark()%></textarea>
                          </div>
                        </div> 
                      </div> 
                    </div>
                  </div>
                  <tr>
                    <td valign="top" ><a name="go"></a> <table width="100%" cellpadding="1" cellspacing="1">
                        <tr>
                          <td colspan="3" > <table class="table" width="100%" border="0" cellspacing="0" cellpadding="0" >
                              <tr align="left" valign="top">
                                <%try {
                                %>
                                <td height="22" valign="middle"> <%= drawListPoItem(SESS_LANGUAGE, iCommand, frmPurchaseRequestItem, poItem, listPurchaseRequestItem, oidPurchaseRequestItem, startItem, approot)%> </td>
                                <%
                                  } catch (Exception e) {
                                    System.out.println(e);
                                  }
                                %>
                              </tr>
                              <tr align="left" valign="top">
                                <td height="8" align="left" class="command"> <span class="command">
                                    <%
                                      int cmd = 0;
                                      if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                                        cmd = iCommand;
                                      } else {
                                        if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                          cmd = Command.FIRST;
                                        } else {
                                          cmd = prevCommand;
                                        }
                                      }
                                      ctrLine.setLocationImg(approot + "/images");
                                      ctrLine.initDefault();
                                      out.println(ctrLine.drawImageListLimit(cmd, vectSizeItem, startItem, recordToGetItem));
                                    %>
                                  </span> </td>
                              </tr>
                              <tr align="left" valign="top">
                                <td height="22" valign="middle"> <%
                                  ctrLine.setLocationImg(approot + "/images");

                                  // set image alternative caption
                                  ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true));
                                  ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_SAVE, true));
                                  ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                  ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ASK, true));
                                  ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_CANCEL, false));

                                  ctrLine.initDefault();
                                  ctrLine.setTableWidth("65%");
                                  String scomDel = "javascript:cmdAsk('" + oidPurchaseRequestItem + "')";
                                  String sconDelCom = "javascript:cmdConfirmDelete('" + oidPurchaseRequestItem + "')";
                                  String scancel = "javascript:cmdEdit('" + oidPurchaseRequestItem + "')";
                                  ctrLine.setCommandStyle("command");
                                  ctrLine.setColCommStyle("command");

                                  // set command caption
                                  ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true));
                                  ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_SAVE, true));
                                  ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                  ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ASK, true));
                                  ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_DELETE, true));
                                  ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_CANCEL, false));

                                  if (privDelete) {
                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                    ctrLine.setDeleteCommand(scomDel);
                                    ctrLine.setEditCommand(scancel);
                                  } else {
                                    ctrLine.setConfirmDelCaption("");
                                    ctrLine.setDeleteCaption("");
                                    ctrLine.setEditCaption("");
                                  }

                                  if (privAdd == false && privUpdate == false) {
                                    ctrLine.setSaveCaption("");
                                  }

                                  if (privAdd == false) {
                                    ctrLine.setAddCaption("");
                                  }

                                  if (documentClosed) {
                                    ctrLine.setSaveCaption("");
                                    //ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteCaption("");
                                    ctrLine.setConfirmDelCaption("");
                                    ctrLine.setCancelCaption("");
                                  }

                                  String strDrawImage = ctrLine.drawImage(iCommand, iErrCode, msgString);
                                  if ((iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) && strDrawImage.length() == 0) {
                                  %> <table width="21%" border="0" cellspacing="2" cellpadding="0">
                                    <tr>
                                      <% if (po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                      <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                      <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                        <% }%>
                                      <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true)%>"></a></td>
                                      <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_BACK, true)%></a></td>
                                    </tr>
                                  </table>
                                  <%
                                    } else {
                                      out.println(strDrawImage);
                                    }
                                  %> </td>
                              </tr>
                            </table></td>
                        </tr>
                        <%if (listPurchaseRequestItem != null && listPurchaseRequestItem.size() > 0) {%>
                        <tr>
                          <td colspan="3"></td>
                        </tr>
                        <tr>
                          <td colspan="3">&nbsp;</td>
                        </tr>
                        <%}%>
                        <tr>
                          <td colspan="3"> </td>
                        </tr>
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
        <%if (menuUsed == MENU_ICON) {%>
        <%@include file="../../../styletemplate/footer.jsp" %>
        <%} else {%>
        <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
        <!-- #EndEditable --> </td>
    </tr>
  </table>
</div>
</body>
<!-- #EndTemplate -->
<script language="JavaScript">
    <% if(iCommand == Command.ADD ) { %>
        document.frm_purchaseorder.matItem.focus();
    <% } %>
</script>


<script language="JavaScript">
     <!--// add By Fitra-->
     var trap = document.frm_purchaseorder.trap.value;       
     document.frm_purchaseorder.trap.value="0";
     document.frm_purchaseorder.txt_materialname.focus();
</script>
<%--autocomplate add by fitora --%>
<script>
	jQuery(function(){
		$("#txt_materialname").autocomplete("list.jsp");
	});
</script>
</html>


