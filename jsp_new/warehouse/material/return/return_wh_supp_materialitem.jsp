<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseOrder"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.PstReturnStockCode" %>
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
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Retur","Tanpa Nota Penerimaan","Edit","Qty retur melebihi stok yang ada!"},
	{"Return","Without Receipt","Edit","Qty return more than available stock!"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Alasan","Waktu","Mata Uang"},
	{"Number","Location","Date","Supplier","Status","Remark","Reason","Time","Currency"}	
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{
            "No","Sku","Nama Barang","Unit","HPP","Harga Beli","Mata Uang","Qty","Total", "Hapus","Barcode", //10
            "Warna","Ukuran"//12
        },
	{
            "No","Code","Goods Name","Unit","COGS","Buying Price","Currency","Qty","Total","Delete","Barcode",
            "Color","Size"
        }
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

/**
* this method used to maintain poMaterialList
*/
public Vector drawListRetItem(int language,int iCommand,FrmMatReturnItem frmObject, MatReturnItem objEntity,Vector objectClass,
                              long retItemId,int start,int tranUsedPriceHpp,boolean privShowQtyPrice, String approot, int typeOfBusinessDetail)
{
    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);

    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");

    boolean useForGreenbowl = 1 == Integer.valueOf(PstSystemProperty.getValueByName("USE_FOR_GREENBOWL"));
    
    ctrlist.addHeader(textListOrderItem[language][0],"1%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"15%");//sku
    ctrlist.addHeader(textListOrderItem[language][2],"20%");//nama barang
    if (useForGreenbowl) {
        ctrlist.addHeader(textListOrderItem[language][10],"5%");//barcode
        ctrlist.addHeader(textListOrderItem[language][11],"5%");//barcode
        ctrlist.addHeader(textListOrderItem[language][12],"5%");//barcode
    }
    ctrlist.addHeader(textListOrderItem[language][3],"5%");//unit
    
    if(privShowQtyPrice){
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Harga Beli / HE","10%");
        } else {
            if(tranUsedPriceHpp==0){
                ctrlist.addHeader(textListOrderItem[language][4],"10%");//hpp
            }else{
                if (useForGreenbowl) {
                    ctrlist.addHeader("Harga","10%");//harga jual
                } else {
                    ctrlist.addHeader(textListOrderItem[language][5],"10%");//harga beli
                }
            }
        }
        ctrlist.addHeader(textListOrderItem[language][7],"8%");//qty
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Berat","8%");
            ctrlist.addHeader("Oks/Batu","8%");
        }
        ctrlist.addHeader(textListOrderItem[language][8],"12%");//total
    }else{
        ctrlist.addHeader(textListOrderItem[language][7],"8%");//qty
    }
    ctrlist.addHeader(textListOrderItem[language][9],"1%");//hapus

    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0)
       start=0;

    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MatReturnItem retItem = (MatReturnItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        CurrencyType matCurrency = (CurrencyType)temp.get(3);
        rowx = new Vector();
        start = start + 1;
        
        //added by dewok 2018
        Material newmat = new Material();
        Category category = new Category();
        Color color = new Color();
        try {
            newmat = PstMaterial.fetchExc(retItem.getMaterialId());
            category = PstCategory.fetchExc(newmat.getCategoryId());
            color = PstColor.fetchExc(newmat.getPosColor());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String itemName = "" + newmat.getName();
        if(typeOfBusinessDetail == 2){
            if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_EMAS) {
                itemName = "" + category.getName() + " " + color.getColorName() + " " + newmat.getName();
            } else if (newmat.getMaterialJenisType() == Material.MATERIAL_TYPE_BERLIAN) {
                itemName = "" + category.getName() + " " + color.getColorName() + " Berlian " + newmat.getName();
            }
        }
        
        //added by dewok 20190114 for greenbowl
        MasterType masterTypeSize = new MasterType();
        if (useForGreenbowl) {
            try {
                long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, newmat.getOID());
                masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (retItemId == retItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK))
        {
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+retItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+itemName+"\" class=\"hiddenText\" readOnly>");
            if (useForGreenbowl) {
                rowx.add(""+newmat.getBarCode());
                rowx.add(""+color.getColorName());
                rowx.add(""+masterTypeSize.getMasterName());
            }
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+retItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            if(privShowQtyPrice){
                if (useForGreenbowl) {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HARGA_JUAL]+"\" value=\""+Formater.formatNumber(retItem.getHargaJual(),"###")+"\" onKeyUp=\"javascript:cntTotal(event)\" class=\"formElemen\" style=\"text-align:right\"></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+Formater.formatNumber(retItem.getCost(),"###")+"\" onKeyUp=\"javascript:cntTotal(event)\" class=\"formElemen\" style=\"text-align:right\"></div>");
                }
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal(event)\"></div>");
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT]+"\" value=\""+String.format("%,.3f", retItem.getBerat())+"\" class=\"formElemen\" style=\"text-align:right\"></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+String.format("%,.0f", retItem.getOngkos())+".00\" class=\"formElemen\" style=\"text-align:right\"></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+String.format("%,.0f", retItem.getTotal())+".00\" class=\"hiddenText\" style=\"text-align:right\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+Formater.formatNumber(retItem.getTotal(),"###")+"\" class=\"hiddenText\" style=\"text-align:right\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
                }
            }else{
                 rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getQty())+"\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal(event)\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal(event)\"></div>"
                         + "<div align=\"right\"><input type=\"hidden\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+Formater.formatNumber(retItem.getCost(),"###")+"\" onKeyUp=\"javascript:cntTotal(event)\" class=\"formElemen\"></div>"
                         + "<div align=\"right\"><input type=\"hidden\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+Formater.formatNumber(retItem.getTotal(),"###")+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
            }
            rowx.add("");
        }else{
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(retItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(itemName);
            if (useForGreenbowl) {
                rowx.add(""+newmat.getBarCode());
                rowx.add(""+color.getColorName());
                rowx.add(""+masterTypeSize.getMasterName());
            }
            rowx.add(unit.getCode());
            if(privShowQtyPrice){
               if (useForGreenbowl) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getHargaJual())+"</div>");
               } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getCost())+"</div>");
               }
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+retItem.getOID();
                int cnt = PstReturnStockCode.getCount(where);
                double recQtyPerBuyUnit = retItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;

                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty return");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(retItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getQty())+"</div>");
            }
            if(privShowQtyPrice){
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">"+String.format("%,.3f",retItem.getBerat())+"</div>");
                    rowx.add("<div align=\"right\">"+String.format("%,.0f",retItem.getOngkos())+".00</div>");
                    rowx.add("<div align=\"right\">"+String.format("%,.0f",retItem.getTotal())+".00</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"</div>");
                }
            }
                     
            // add by fitra 17-05-2014   
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(retItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
        }
        lstData.add(rowx);
    }

    rowx = new Vector();
    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) { 
        rowx.add(""+(start+1)); 
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+
                "\"><input type=\"text\" onKeyDown=\"javascript:keyDownCheck(event)\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"20\" onKeyDown=\"javascript:keyDownCheck(event)\" id=\"txt_materialname\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        if (useForGreenbowl) {
            rowx.add("<input type=\"text\" size=\"10\" id=\"scanBarcode\" name=\"matBarcode\" value=\"\" class=\"formElemen \">");
            rowx.add("<input type=\"text\" size=\"10\" id=\"\" name=\"matColor\" value=\"\" class=\"formElemen hiddenText\">");
            rowx.add("<input type=\"text\" size=\"10\" id=\"\" name=\"matSize\" value=\"\" class=\"formElemen hiddenText\">");
        }
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        if(privShowQtyPrice){
            if (useForGreenbowl) {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HARGA_JUAL]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
            } else {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal(event)\"></div>");
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" style=\"text-align:right\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
        }else{
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" onKeyUp=\"javascript:cntTotal(event)\"></div>"
                    + "<div align=\"right\"><input  type=\"hidden\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>"
                    + "<div align=\"right\"><input  type=\"hidden\" size=\"20\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
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
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_ROMR);
%>

<%
/**
* get request data from current form
*/
int iCommand = FRMQueryString.requestCommand(request);
int startItem = FRMQueryString.requestInt(request,"start_item");
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidReturnMaterial = FRMQueryString.requestLong(request,"hidden_return_id");
long oidReturnMaterialItem = FRMQueryString.requestLong(request,"hidden_return_item_id");
int focusBarcode = FRMQueryString.requestInt(request,"focus_barcode");

//boolean useForGreenbowl = 1 == Integer.valueOf(PstSystemProperty.getValueByName("USE_FOR_GREENBOWL"));

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String retCode = ""; //i_pstDocType.getDocCode(docType);
String retTitle = "Retur Barang"; //i_pstDocType.getDocTitle(docType);
String retItemTitle = retTitle + " Item";

/**
* process on purchase order main
*/
CtrlMatReturn ctrlMatReturn = new CtrlMatReturn(request);
iErrCode = ctrlMatReturn.action(Command.EDIT,oidReturnMaterial);
FrmMatReturn frmMatReturn = ctrlMatReturn.getForm();
MatReturn ret = ctrlMatReturn.getMatReturn();
	
/**
* check if document may modified or not 
*/
boolean privManageData = true; 
CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
ControlLine ctrLine = new ControlLine();
CtrlMatReturnItem ctrlMatReturnItem = new CtrlMatReturnItem(request);
ctrlMatReturnItem.setLanguage(SESS_LANGUAGE);
iErrCode = ctrlMatReturnItem.action(iCommand,oidReturnMaterialItem,oidReturnMaterial);
FrmMatReturnItem frmMatReturnItem = ctrlMatReturnItem.getForm();
MatReturnItem retItem = ctrlMatReturnItem.getMatReturnItem();
msgString = ctrlMatReturnItem.getMessage();
PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();

/**int comm = iCommand;
if(iErrCode!=0 && retItem.getOID()!=0 && iCommand==Command.SAVE){
	comm = Command.EDIT;
}else if(iErrCode!=0 && retItem.getOID()==0 && iCommand==Command.SAVE){
	comm = Command.ADD;
}**/



String whereClauseItem = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatReturnItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatReturnItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
} 

Vector listMatReturnItem = PstMatReturnItem.list(startItem,recordToGetItem,whereClauseItem);

//added by dewok 20190116, list for total qty item
Vector listMatReturnItemAll = PstMatReturnItem.list(0,0,whereClauseItem);

if(listMatReturnItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;   
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST; 
	 }
	 listMatReturnItem = PstMatReturnItem.list(startItem,recordToGetItem,whereClauseItem);
}

// add by fitra
if(iCommand==Command.SAVE && iErrCode == 0) {
	iCommand = Command.ADD;
        oidReturnMaterialItem=0;
        
}

// add by fitra 17-05-2014

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm) {
	document.frm_retmaterial.command.value=comm;
	document.frm_retmaterial.hidden_return_id.value=oid;
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function gostock(oid) {
    document.frm_retmaterial.command.value="<%=Command.EDIT%>";
    document.frm_retmaterial.hidden_return_item_id.value=oid;
    document.frm_retmaterial.action="return_wh_stockcode.jsp";
    document.frm_retmaterial.submit();
}

function cmdAdd() {
	document.frm_retmaterial.hidden_return_item_id.value="0";
	document.frm_retmaterial.command.value="<%=Command.ADD%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_retmaterial.submit();
}

function cmdEdit(oidReturnMaterialItem) {
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}



function cmdCheck(){
    var strvalue  = "materialrequestsearch.jsp?command=<%=Command.FIRST%>"+
                                    "&mat_code="+document.frm_purchaseorder.matCode.value+
                                    "&location_id=<%=po.getLocationId()%>"+
                                    "&mat_vendor=<%=po.getSupplierId()%>"+ 
                                    "&txt_materialname="+document.frm_purchaseorder.matItem.value;
    window.open(strvalue,"material", "height=600,width=1000,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function keyDownCheck(e){
    var trap = document.frm_retmaterial.trap.value;
   
    
   if (e.keyCode == 13 && trap==0) {
    document.frm_retmaterial.trap.value="1";
  
   }
   
    // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_retmaterial.matItem.value == "" ){
        document.frm_retmaterial.trap.value="0";
        cmdCheck();
   }
   if (e.keyCode == 13 && trap==1) {
       document.frm_retmaterial.trap.value="0";
       cmdCheck();
}
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_retmaterial.txt_materialname.value="";
   } 
}

function cmdAsk(oidReturnMaterialItem) {
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.ASK%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdSave(){
	var qty = parseFloat(cleanNumberFloat(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol));
	var residu_qty = parseFloat(cleanNumberFloat(document.frm_retmaterial.residu_qty.value,guiDigitGroup,guiDecimalSymbol));
	//var hasilResiduQty = residu_qty - qty;
	if(qty > residu_qty) {
		//alert("<%=textListGlobal[SESS_LANGUAGE][3]%>");
                //For Yashoda
                document.frm_retmaterial.command.value="<%=Command.SAVE%>";
		document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
		document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
		document.frm_retmaterial.submit();
	}
	else {
		document.frm_retmaterial.command.value="<%=Command.SAVE%>"; 
		document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
		document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
		document.frm_retmaterial.submit();
	}
}

function cmdConfirmDelete(oidReturnMaterialItem) {
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
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

function cmdCancel(oidReturnMaterialItem) {
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBack(){
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.start_item.value = 0;
	document.frm_retmaterial.action="return_wh_supp_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdCheck() {    
	var strvalue  = "materialdosearch_without_receipt.jsp?command=<%=Command.FIRST%>"+
                    "&location_id=<%=ret.getLocationId()%>"+
					"&currency_id=<%=ret.getCurrencyId()%>"+
					"&trans_rate=<%=ret.getTransRate()%>"+
                                        "&mat_vendor=<%=ret.getSupplierId()%>"+
                                        "&show_all_good=0"+ 
					"&material_code="+document.frm_retmaterial.matCode.value+
                                        "&material_name="+document.frm_retmaterial.matItem.value+
                                        "&show_stok_Nol=0";//updated by dewok 2018 b'coz rida minta default allow qty = 0 (tidak auto checked)
	window.open(strvalue,"material", "height=500,width=700,left=300,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function cntTotal(evenClick) {
	var qty = cleanNumberFloat(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
	var price = 0;
        if (<%= useForGreenbowl %>) {
            price = cleanNumberFloat(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_HARGA_JUAL]%>.value,guiDigitGroup,guiDecimalSymbol);
        } else {
            price = cleanNumberFloat(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value,guiDigitGroup,guiDecimalSymbol);
        }
        if(qty<0.0000){

          document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value=0;

          return;

        }
	
	if(!(isNaN(qty)) && (qty != '0')) {
		var amount = parseFloat(price) * qty;
		document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);					 
	}
	else {
		document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();
	}
        
         if (evenClick.keyCode == 13) {
        cmdSave();
}
}

function cmdListFirst() {
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListPrev() {
	document.frm_retmaterial.command.value="<%=Command.PREV%>";
	document.frm_retmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListNext() {
	document.frm_retmaterial.command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListLast() {
	document.frm_retmaterial.command.value="<%=Command.LAST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_retmaterial.action="return_wh_supp_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBackList() {
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_supp_material_list.jsp";
	document.frm_retmaterial.submit();
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
<%--autocomplate addd by fitra--%>
<script type="text/javascript" src="../../../styles/jquery-1.4.2.min.js"></script>
<script src="../../../styles/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="../../../styles/style.css" />
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

    <%if (menuUsed == MENU_PER_TRANS) {%>
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
    <%} else {%>
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
                        &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
                </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form name="frm_retmaterial" method ="post" action="">
                            <input type="hidden" name="command" value="<%=iCommand%>">
                            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                            <input type="hidden" name="start_item" value="<%=startItem%>">
                            <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">
                            <input type="hidden" name="hidden_return_item_id" value="<%=oidReturnMaterialItem%>">
                            <input type="hidden" name="<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_RETURN_MATERIAL_ID]%>" value="<%=oidReturnMaterial%>">
                            <input type="hidden" name="approval_command" value="<%=appCommand%>">
                            <input type="hidden" name="trap" value="">

                            <input type="hidden" name="residu_qty">
                            <table width="100%" cellpadding="1" cellspacing="0">
                                <tr> 
                                    <td colspan="3"> 
                                        <hr size="1">
                                    </td>
                                </tr>
                                <tr align="center"> 
                                    <td colspan="3" class="title"> 
                                        <table width="100%" border="0" cellpadding="1">
                                            <tr>
                                                <td width="10%" height="23" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td> 
                                                <td width="27%" align="left"> : <b><%=ret.getRetCode()%></b></td>
                                                <td width="10%" valign=""><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                <td width="28%" valign="">:
                                                    <%
                                                        Vector obj_supplier = new Vector(1, 1);
                                                        Vector val_supplier = new Vector(1, 1);
                                                        Vector key_supplier = new Vector(1, 1);
                                                        //Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                        String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                                                                + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                                                                + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                                                                + " != " + PstContactList.DELETE;
                                                        Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

                                                        for (int d = 0; d < vt_supp.size(); d++) {
                                                            ContactList cnt = (ContactList) vt_supp.get(d);
                                                            String cntName = cnt.getCompName();
                                                            if (cntName.length() == 0) {
                                                                cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                            }
                                                            val_supplier.add(String.valueOf(cnt.getOID()));
                                                            key_supplier.add(cnt.getContactCode() + " - " + cntName);
                                                        }
                                                        String select_supplier = "" + ret.getSupplierId();
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_SUPPLIER_ID], null, select_supplier, val_supplier, key_supplier, "disabled=\"true\"", "formElemen")%> 
                                                </td>
                                                <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][6]%>
                                                </td>
                                                <td>:</td>
                                                <td width="15%" align="left"><%=PstMatReturn.strReturnReasonList[SESS_LANGUAGE][ret.getReturnReason()]%> 
                                                </td>						
                                            </tr>
                                            <tr>
                                                <td align="left"> <%=textListOrderHeader[SESS_LANGUAGE][1]%></td> 
                                                <td align="left"> : 
                                                    <%
                                                        Vector obj_locationid = new Vector(1, 1);
                                                        Vector val_locationid = new Vector(1, 1);
                                                        Vector key_locationid = new Vector(1, 1);
                                                        //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                        Vector vt_loc = PstLocation.list(0, 0, "", "");
                                                        for (int d = 0; d < vt_loc.size(); d++) {
                                                            Location loc = (Location) vt_loc.get(d);
                                                            val_locationid.add("" + loc.getOID() + "");
                                                            key_locationid.add(loc.getName());
                                                        }
                                                        String select_locationid = "" + ret.getLocationId(); //selected on combo box
%>
                                                    <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%> 
                                                </td>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                <td>:
                                                    <%
                                                        Vector obj_status = new Vector(1, 1);
                                                        Vector val_status = new Vector(1, 1);
                                                        Vector key_status = new Vector(1, 1);

                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                        //add by fitra
                                                        if (listMatReturnItem.size() > 0) {
                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);

                                                        }
                                                        if (listMatReturnItem.size() > 0) {
                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                        }
                                                        String select_status = "" + ret.getReturnStatus();
                                                        if (ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                        } else if (ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                        } else {
                                                    %>
                                                    <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_STATUS], null, select_status, val_status, key_status, "disabled", "formElemen")%> 
                                                    <%}%>
                                                </td>
                                                <td align="left"> <%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                <td>:</td>
                                                <td align="left" valign="top" rowspan="3"><textarea name="<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3" disabled="true"><%=ret.getRemark()%></textarea></td>
                                            </tr>
                                            <tr>
                                                <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td> 
                                                <td align="left">: <%=ControlDate.drawDateWithStyle(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate() == null) ? new Date() : ret.getReturnDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>
                                                <td><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                                                <td>:
                                                    <%
                                                        Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                        Vector vectCurrVal = new Vector(1, 1);
                                                        Vector vectCurrKey = new Vector(1, 1);
                                                        for (int i = 0; i < listCurr.size(); i++) {
                                                            CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                                            vectCurrKey.add(currencyType.getCode());
                                                            vectCurrVal.add("" + currencyType.getOID());
                                                        }
                                                        out.println(ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_CURRENCY_ID], "formElemen", null, "" + ret.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled"));
                                                    %>
                                                </td>
                                                <td align="right">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                                                <td align="left">: <%=ControlDate.drawTimeSec(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate() == null) ? new Date() : ret.getReturnDate(), "formElemen")%></td>
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
                                                            Vector listError = new Vector();
                                                            try {
                                                            %>
                                                            <td height="22" valign="middle" colspan="3">                           <%
                                                                //Vector list = drawListRetItem(SESS_LANGUAGE, comm, frmMatReturnItem, retItem, listMatReturnItem, oidReturnMaterialItem, startItem, tranUsedPriceHpp);
                                                                Vector list = drawListRetItem(SESS_LANGUAGE, iCommand, frmMatReturnItem, retItem, listMatReturnItem, oidReturnMaterialItem, startItem, tranUsedPriceHpp, privShowQtyPrice, approot, typeOfBusinessDetail);
                                                                out.println("" + list.get(0));
                                                                listError = (Vector) list.get(1);
                                                                %>
                                                            </td>
                                                            <%
                                                                } catch (Exception e) {
                                                                    System.out.println(e);
                                                                }
                                                            %>
                                                        </tr>
                                                        <tr align="left" valign="top"> 
                                                            <td height="8" align="left" colspan="3" class="command"> 
                                                                <span class="command"> 
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
                                                                </span>
                                                            </td>
                                                        </tr>
                                                        <tr align="left" valign="top">
                                                            <td height="22" valign="middle" colspan="3"><span class="errfont">
                                                                    <%
                                                                        for (int k = 0; k < listError.size(); k++) {
                                                                            if (k == 0) {
                                                                                out.println(listError.get(k) + "<br>");
                                                                            } else {
                                                                                out.println("&nbsp;&nbsp;&nbsp;" + listError.get(k) + "<br>");
                                                                            }
                                                                        }
                                                                    %>
                                                                </span></td>
                                                        </tr>
                                                        <tr align="left" valign="top"> 
                                                            <td height="22" valign="middle" colspan="3"> 
                                                                <%
                                                                    ctrLine.setLocationImg(approot + "/images");

                                                                    // set image alternative caption 
                                                                    ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ADD, true));
                                                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_SAVE, true));
                                                                    ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ASK, true));
                                                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_CANCEL, false));

                                                                    ctrLine.initDefault();
                                                                    ctrLine.setTableWidth("65%");
                                                                    String scomDel = "javascript:cmdAsk('" + oidReturnMaterialItem + "')";
                                                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidReturnMaterialItem + "')";
                                                                    String scancel = "javascript:cmdEdit('" + oidReturnMaterialItem + "')";
                                                                    ctrLine.setCommandStyle("command");
                                                                    ctrLine.setColCommStyle("command");

                                                                    // set command caption 
                                                                    ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ADD, true));
                                                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_SAVE, true));
                                                                    ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ASK, true));
                                                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_DELETE, true));
                                                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_CANCEL, false));

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

                                                                    String strDrawImage = ctrLine.drawImage(iCommand, iErrCode, msgString);
                                                                    if ((iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) && strDrawImage.length() == 0) {
                                                                %>
                                                                <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                    <tr> 
                                                                        <% if (ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                                                        <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                        <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                            <% }%>
                                                                        <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true)%>"></a></td>
                                                                        <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE, retCode + " Item", ctrLine.CMD_BACK, true)%></a></td></tr>
                                                                </table>
                                                                <%
                                                                    } else {
                                                                        out.println(strDrawImage);
                                                                    }
                                                                %>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <%if (listMatReturnItem != null && listMatReturnItem.size() > 0) {%>
                                            <tr> 
                                                <td valign="top">&nbsp; </td>
                                                <td width="27%" valign="top">
                                                    <table width="100%" border="0">
                                                        <tr>
                                                            <td width="44%"> <div align="right"><strong><%="TOTAL QTY : " + retCode%></strong></div></td>
                                                            <td width="15%"> <div align="right"></div></td>
                                                            <%
                                                                double totQty = 0;
                                                                for (int i=0; i < listMatReturnItemAll.size(); i++ ) {
                                                                    Vector v = (Vector) listMatReturnItemAll.get(i);
                                                                    MatReturnItem mri = (MatReturnItem) v.get(0);
                                                                    totQty += mri.getQty();
                                                                }
                                                            %>
                                                            <td width="41%"> <div align="right"><strong><%= totQty %></strong></div></td>
                                                        </tr>
                                                        <tr> 
                                                            <td width="44%"> <div align="right"><strong><%="TOTAL : " + retCode%></strong></div></td>
                                                            <td width="15%"> <div align="right"></div></td>
                                                            <td width="41%">
                                                                <div align="right">
                                                                    <strong> 
                                                                        <%
                                                                            String whereItem = "" + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial;
                                                                            out.println(Formater.formatNumber(PstMatReturnItem.getTotal(whereItem), "##,###.00"));
                                                                        %>
                                                                    </strong>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <%}%>
                                            <tr> 
                                                <td colspan="3">&nbsp;</td>
                                            </tr>
                                        </table>
                                    </td>
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
</body>


<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_retmaterial.matItem.focus();
    <% } %>
</script>

<script language="JavaScript">
               // add By Fitra
                var trap = document.frm_retmaterial.trap.value;       
                document.frm_retmaterial.trap.value="0";

         document.frmvendorsearch.txt_materialname.focus();
</script>
<%--autocomplate add by fitra--%>
<script>
        jQuery(function(){
                $("#txt_materialname").autocomplete("list.jsp");
                
                                $('#scanBarcode').focus();
                
                $('#scanBarcode').keydown(function(e){
                    if (e.keyCode === 13){
                        addItemReturn($(this).val());
                        $('#scanBarcode').val("");
                    }
                });
                
                function addItemReturn(barcode) {
                    var dataSend = {
                        "command"           : "<%= Command.SAVE%>",
                        "FRM_FIELD_DATA_FOR": "saveReturnItem",
                        "BARCODE"           : barcode,
                        "OID_MAT_RETURN"    : "<%= oidReturnMaterial %>",
                        "OID_LOCATION"      : "<%= ret.getLocationId()%>"
                    };
                    $.ajax({
                        type    : "POST",
                        url	: "<%= approot%>/AjaxPenerimaan",
                        data    : dataSend,
                        chace   : false,
                        dataType : "json",
                        success : function(data){
                            var error = data.FRM_FIELD_ERROR_NUMBER;
                            if (error == 0) {
                                window.location = "return_wh_supp_materialitem.jsp?command=<%= Command.ADD%>&approval_command=<%= appCommand%>&hidden_return_id=<%= oidReturnMaterial%>&focus_barcode=1";
                            } else {
                                alert(data.FRM_FIELD_RETURN_MESSAGE);
                            }
                        }
                    });
                }
        });
</script>


<!-- #EndTemplate -->

</html>
