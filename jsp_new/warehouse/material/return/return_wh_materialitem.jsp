<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReturnStockCode"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page import="com.dimata.posbo.form.warehouse.FrmMatReturnItem,
                 com.dimata.posbo.entity.warehouse.MatReturnItem,
                 com.dimata.posbo.entity.masterdata.Material,
				 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.form.warehouse.CtrlMatReturn,
                 com.dimata.posbo.form.warehouse.FrmMatReturn,
                 com.dimata.posbo.entity.warehouse.MatReturn,
                 com.dimata.posbo.form.warehouse.CtrlMatReturnItem,
                 com.dimata.posbo.entity.warehouse.PstMatReturnItem,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.posbo.entity.warehouse.PstMatReturn,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.MatReceiveItem,
                 com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
				 com.dimata.common.entity.payment.CurrencyType,
				 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
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
	{"Retur","Dengan Nota Penerimaan","Edit","Tidak ada item retur"},
	{"Return","With Invoice","Edit","There is no return item"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
	{"Nomor","Lokasi Asal","Tanggal","Supplier","Status","Keterangan","Alasan","Nota Supplier","Mata Uang"},
	{"Number","Location","Date","Supplier","Status","Remark","Reason","Invoice","Currency"}	
};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
	{"No","Sku","Nama Barang","Unit","Harga Beli","Harga Jual","Mata Uang","Qty","Total Retur","Hapus"},//9
	{"No","Code","Goods Name","Unit","Cost","Price","Currency","Qty","Total Return","Delete"}
};

/**
* this method used to maintain poMaterialList
*/
public String drawListRetItem(int language,int iCommand,FrmMatReturnItem frmObject,MatReturnItem objEntity,Vector objectClass,
        long retItemId,int start, long oidLGR,boolean privShowQtyPrice, String approot, int typeOfBusinessDetail) {
    
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
	
    ctrlist.addHeader(textListOrderItem[language][0],"1%");
    ctrlist.addHeader(textListOrderItem[language][1],"15%");
    ctrlist.addHeader(textListOrderItem[language][2],"20%");
    ctrlist.addHeader(textListOrderItem[language][3],"5%");        
    if(privShowQtyPrice){
        ctrlist.addHeader(textListOrderItem[language][4],"10%");
        ctrlist.addHeader(textListOrderItem[language][7],"8%");      
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Berat","8%");
            ctrlist.addHeader("Oks/Batu","8%");
        }
        ctrlist.addHeader(textListOrderItem[language][8],"12%");
    }else{
        ctrlist.addHeader(textListOrderItem[language][7],"8%");
    }
    ctrlist.addHeader(textListOrderItem[language][9],"1%");
	
	
    Vector listError = new Vector(1,1);
    Vector lstData = ctrlist.getData();
    Vector rowx = new Vector(1,1);
    ctrlist.reset();
    ctrlist.setLinkRow(1);
    int index = -1;
    if(start<0)
    {
       start=0;
    }	
	
    for(int i=0; i<objectClass.size(); i++) {
        Vector temp = (Vector)objectClass.get(i);
        MatReturnItem retItem = (MatReturnItem)temp.get(0);
        Material mat = (Material)temp.get(1);
        Unit unit = (Unit)temp.get(2);
        CurrencyType matCurrency = (CurrencyType)temp.get(3);
        rowx = new Vector();
        start = start + 1;
        
        //added by dewok 2018-02-21
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
			 
        if (retItemId == retItem.getOID()) index = i;
        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {
            
            String whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidLGR+
                                 " and "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_MATERIAL_ID]+"="+retItem.getMaterialId();
            double avblQtyToRtn = 0;
            Vector list = PstMatReceiveItem.list(0, 0, whereClause, "");
            if(list != null && list.size() > 0) {
                    MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(0);
                    avblQtyToRtn = matReceiveItem.getResidueQty();
            }
            rowx.add(""+start);
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+retItem.getMaterialId()+
                    "\"><input type=\"text\" size=\"15\" name=\"matCode\"  onKeyDown=\"javascript:keyDownCheck(event)\" value=\""+mat.getSku()+"\" class=\"formElemen\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>
            rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\""+itemName+"\" class=\"hiddenText\" readOnly>");
            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+retItem.getUnitId()+
                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\" readOnly>");
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getCost())+"\" class=\"hiddenText\" readOnly></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+retItem.getCurrencyId()+"\">");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getQty())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(event)\" style=\"text-align:right\"></div>"+
                                 "<input type=\"hidden\" name=\"available_qty_to_return\" value=\""+(retItem.getQty() + avblQtyToRtn)+"\"");
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT]+"\" value=\""+String.format("%,.3f", retItem.getBerat())+"\" class=\"formElemen\" ></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+String.format("%,.0f", retItem.getOngkos())+".00\" class=\"formElemen\" ></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+String.format("%,.0f", retItem.getTotal())+".00\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
                }
            }else{
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getQty())+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(event)\" style=\"text-align:right\"></div>"+
                         "<input type=\"hidden\" name=\"available_qty_to_return\" value=\""+(retItem.getQty() + avblQtyToRtn)+"\""
                         + "<div align=\"right\"><input type=\"hidden\"  size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getTotal())+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>"
                         + "<div align=\"right\"><input type=\"hidden\"  size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+FRMHandler.userFormatStringDecimal(retItem.getCost())+"\" class=\"hiddenText\" readOnly></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\""+retItem.getCurrencyId()+"\">");
            }
            rowx.add("");
        } else {
            rowx.add(""+start+"");
            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(retItem.getOID())+"')\">"+mat.getSku()+"</a>");
            rowx.add(itemName);
            rowx.add(unit.getCode());
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retItem.getCost())+"</div>");
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
                    listError.add(""+listError.size()+". Jumlah serial kode stok produk '"+itemName+"' tidak sama dengan qty terima.");
                }
                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(retItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(retItem.getQty())+"</div>");
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
                "\"><input type=\"text\" size=\"13\" onKeyDown=\"javascript:keyDownCheck(event)\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" onKeyDown=\"javascript:keyDownCheck(event)\" value=\""+""+"\" class=\"formElemen\"> <a href=\"javascript:cmdCheck()\">CHK</a>");
        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+""+
                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");
        if(privShowQtyPrice){
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"hiddenText\" style=\"text-align:right\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\"\">");
            rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(event)\" style=\"text-align:right\"></div>"+
                             "<input type=\"hidden\" name=\"available_qty_to_return\" value=\"\"");
            if(typeOfBusinessDetail == 2){
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"5\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_BERAT]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
                rowx.add("<div align=\"center\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_ONGKOS]+"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right\" ></div>");
            }
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>");
        }else{
            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" onkeyup=\"javascript:cntTotal(event)\" style=\"text-align:right\"></div>"+
                     "<input type=\"hidden\" name=\"available_qty_to_return\" value=\"\""
                     + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" onBlur=\"javascript:cntTotal(event)\" readOnly></div>"
                     + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_COST]+"\" value=\""+""+"\" class=\"hiddenText\" style=\"text-align:right\" ></div><input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID]+"\" value=\"\">");
        }
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

/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String retCode = "";//i_pstDocType.getDocCode(docType);
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
* check if document already closed or not
*/
boolean documentClosed = false;
if(ret.getReturnStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED)
{
	documentClosed = true;
}

/**
* check if document may modified or not 
*/
boolean privManageData = true; 

ControlLine ctrLine = new ControlLine();
CtrlMatReturnItem ctrlMatReturnItem = new CtrlMatReturnItem(request);
ctrlMatReturnItem.setLanguage(SESS_LANGUAGE);

//by dyas - 20131125
//tambah variabel userName dan userId
iErrCode = ctrlMatReturnItem.action(iCommand,oidReturnMaterialItem,oidReturnMaterial, userName, userId);
FrmMatReturnItem frmMatReturnItem = ctrlMatReturnItem.getForm();
MatReturnItem retItem = ctrlMatReturnItem.getMatReturnItem();
msgString = ctrlMatReturnItem.getMessage();

int comm = iCommand;
if(iErrCode!=0 && retItem.getOID()!=0 && iCommand==Command.SAVE){
	comm = Command.EDIT;
}else if(iErrCode!=0 && retItem.getOID()==0 && iCommand==Command.SAVE){
	comm = Command.ADD;
}

String whereClauseItem = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
String orderClauseItem = "";
int vectSizeItem = PstMatReturnItem.getCount(whereClauseItem);
int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = ctrlMatReturnItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
} 

Vector listMatReturnItem = PstMatReturnItem.list(startItem,recordToGetItem,whereClauseItem);
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

// add by fitra 17-05-2014
if(iCommand==Command.SAVE && iErrCode == 0) {
	iCommand = Command.ADD;
        comm = iCommand;
        oidReturnMaterialItem=0;
}
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function main(oid,comm)
{
	document.frm_retmaterial.command.value=comm;
	document.frm_retmaterial.hidden_return_id.value=oid;
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function cmdAdd(){
	document.frm_retmaterial.hidden_return_item_id.value="0";
	document.frm_retmaterial.command.value="<%=Command.ADD%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_retmaterial.submit();
}

function cmdEdit(oidReturnMaterialItem)
{
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdAsk(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.ASK%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdSave(){
	var qtyRtn = document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value;
	var qtyAvb = document.frm_retmaterial.available_qty_to_return.value;
	//alert(parseFloat(qtyRtn)+" <= "+parseFloat(qtyAvb));
	if(parseFloat(qtyRtn) <= parseFloat(qtyAvb)) {
		document.frm_retmaterial.command.value="<%=Command.SAVE%>"; 
		document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
		document.frm_retmaterial.action="return_wh_materialitem.jsp";
		document.frm_retmaterial.submit();
	}
	else {
		alert("Qty return more than qty avaiable for return!")
		document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();
	}
}

function addItem(){
    document.frm_retmaterial.command.value="<%=Command.ADD%>";
    document.frm_retmaterial.action="return_wh_materialitem.jsp";
    document.frm_retmaterial.submit();
}

function cmdConfirmDelete(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.DELETE%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
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

function cmdCancel(oidReturnMaterialItem){
	document.frm_retmaterial.hidden_return_item_id.value=oidReturnMaterialItem;
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBack(){
	document.frm_retmaterial.command.value="<%=Command.EDIT%>";
	document.frm_retmaterial.start_item.value = 0;
	document.frm_retmaterial.action="return_wh_material_edit.jsp";
	document.frm_retmaterial.submit();
}

function gostock(oid) {
    document.frm_retmaterial.command.value="<%=Command.EDIT%>";
    document.frm_retmaterial.hidden_return_item_id.value=oid;
    document.frm_retmaterial.action="return_wh_stockcode.jsp";
    document.frm_retmaterial.submit();
}

function sumPrice(){
}		

function cmdCheck(){
	var strvalue  = "materialdosearch_with_receipt.jsp?command=<%=Command.FIRST%>"+
					"&location_id="+document.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_LOCATION_ID]%>.value+
					"&mat_code="+document.frm_retmaterial.matCode.value+
					"&mat_vendor="+document.frm_retmaterial.<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_SUPPLIER_ID]%>.value+
					"&invoice_supplier=<%=ret.getInvoiceSupplier()%>"+
					"&hidden_return_id=<%=oidReturnMaterial%>"+"&rec_mat_id="+document.frm_retmaterial.rec_mat_id.value;					
	window.open(strvalue,"material", "height=500,width=700,left=300,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}


function keyDownCheck(e){
   if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdCheck();
   }
}

function cntTotal(e){
        
	var qty = cleanNumberInt(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
	var price = cleanNumberInt(document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_COST]%>.value,guiDigitGroup);

        if(qty<0.0000){

          document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.value=0;

          return;

        }
       
	if(!(isNaN(qty)) && (qty != "0")){
		var amount = parseFloat(price) * qty;
		document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_TOTAL]%>.value = amount;					 
	}else{
		document.frm_retmaterial.<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_QTY]%>.focus();
	}
        
        if (e.keyCode == 13) {
            cmdSave();
            //addItem();
        }
}

function cmdListFirst(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListPrev(){
	document.frm_retmaterial.command.value="<%=Command.PREV%>";
	document.frm_retmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListNext(){
	document.frm_retmaterial.command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdListLast(){
	document.frm_retmaterial.command.value="<%=Command.LAST%>";
	document.frm_retmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_retmaterial.action="return_wh_materialitem.jsp";
	document.frm_retmaterial.submit();
}

function cmdBackList(){
	document.frm_retmaterial.command.value="<%=Command.FIRST%>";
	document.frm_retmaterial.action="return_wh_material_list.jsp";
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>
		  <!-- #EndEditable --></td>
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
              <input type="hidden" name="rec_mat_id" value="<%=ret.getReceiveMaterialId()%>">
              
              <table width="100%" cellpadding="1" cellspacing="0">
                <tr> 
                  <td colspan="3"> 
                    <hr size="1">
                  </td>
                </tr>
				<tr align="center"> 
                  <td colspan="3" class="title"> 
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                        <td width="27%">: <b>
                          <%=ret.getRetCode()%>
                        </b></td>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                        <td width="28%">:
                            <%
							Vector obj_supplier = new Vector(1,1);
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);
                              String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                               " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                               " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                               " != "+PstContactList.DELETE;
							Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
							//Vector vt_supp = PstContactList.list(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
							for(int d=0; d<vt_supp.size(); d++){
								ContactList cnt = (ContactList)vt_supp.get(d);
								String cntName = cnt.getCompName();
								if(cntName.length()==0){
									cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
								}
								val_supplier.add(String.valueOf(cnt.getOID()));
								key_supplier.add("" + cnt.getContactCode() + " - " + cntName);
							}
							String select_supplier = ""+ret.getSupplierId();
						  %>
                            <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%> </td>
                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                        <td>:</td>
                        <td width="15%" align="">
                          <%
						  	Vector asuOID = new Vector();
						  	Vector asuName = new Vector();
							for (int i=0;i<PstMatReturn.strReturnReasonList[SESS_LANGUAGE].length;i++) {
								asuOID.add(""+i);
								asuName.add(PstMatReturn.strReturnReasonList[SESS_LANGUAGE][i]);
							}
						  %>
                          <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_REASON], null, ""+ret.getReturnReason(), asuOID, asuName, "", "formElemen")%></td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_RETURN_DATE], (ret.getReturnDate()==null) ? new Date() : ret.getReturnDate(), 1, -5, "formElemen", "")%></td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                        <td>:
                            <input type="text"  readonly class="formElemen" name="<%=FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_INVOICE_SUPPLIER]%>2" value="<%=ret.getInvoiceSupplier()%>"  size="20" style="text-align:right"> * 
							<a href="javascript:findInvoice()"></a>
						</td>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td>:</td>
                        <td rowspan="3" align="" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3"><%=ret.getRemark()%></textarea></td>
                      </tr>
                      <tr>
                        <td><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                        <td>:
                            <%
							Vector obj_locationid = new Vector(1,1);
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,"");
							Vector vt_loc = PstLocation.list(0,0,"","");
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+ret.getLocationId(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmMatReturn.fieldNames[FrmMatReturn.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                        </td>
						<td><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                      	<td>:
                          <%
                            Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                            Vector vectCurrVal = new Vector(1,1);
                            Vector vectCurrKey = new Vector(1,1);
                            for(int i=0; i<listCurr.size(); i++){
                                CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                vectCurrKey.add(currencyType.getCode());
                                vectCurrVal.add(""+currencyType.getOID());
                            }
							out.println(ControlCombo.draw("CURRENCY_CODE","formElemen", null, ""+ret.getCurrencyId(), vectCurrVal, vectCurrKey, "disabled"));
                          %>
                        </td>
                        <td>&nbsp;</td>
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
                        <td colspan="2" > <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr align="left" valign="top"> 
                              <%try{%>
                              <td height="22" valign="middle" colspan="3">
                                  <%= drawListRetItem(SESS_LANGUAGE,comm,frmMatReturnItem, retItem,listMatReturnItem,oidReturnMaterialItem,startItem,ret.getReceiveMaterialId(),privShowQtyPrice,approot, typeOfBusinessDetail)%>
                              </td>
                              <%}catch(Exception e) {
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
                              <td height="22" valign="middle" colspan="3"> <%
								ctrLine.setLocationImg(approot+"/images");
								
								// set image alternative caption 
								ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)+" List");							
								ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ASK,true));							
								ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_CANCEL,false));																					
								
								ctrLine.initDefault();
								ctrLine.setTableWidth("65%");
								String scomDel = "javascript:cmdAsk('"+oidReturnMaterialItem+"')";
								String sconDelCom = "javascript:cmdConfirmDelete('"+oidReturnMaterialItem+"')";
								String scancel = "javascript:cmdEdit('"+oidReturnMaterialItem+"')";								
								ctrLine.setCommandStyle("command");
								ctrLine.setColCommStyle("command");
								
								// set command caption 
								ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true));
								ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_SAVE,true));
								ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)+" List");							
								ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ASK,true));							
								ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_DELETE,true));														
								ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_CANCEL,false));																					
								

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
								%> <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                  <tr> 
                                    <% if(ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
									<td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
									<% } %>
									<td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>
                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,retCode+" Item",ctrLine.CMD_BACK,true)%></a></td>
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
                      <% if(privShowQtyPrice){%>
                          <%if(listMatReturnItem!=null && listMatReturnItem.size()>0){%>
                          <tr>
                            <td valign="top">&nbsp; </td>
                            <td width="27%" valign="top">
                                                      <table width="100%" border="0">
                                                              <tr>
                                                                    <td width="44%"> <div align="right"><strong><%="TOTAL : "+retCode%></strong></div></td>
                                                                    <td width="15%"> <div align="right"></div></td>
                                                                    <td width="41%"> <div align="right"><strong>
                                                                      <%
                                                                      String whereItem = ""+PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID]+"="+oidReturnMaterial;
                                                                      out.println(Formater.formatNumber(PstMatReturnItem.getTotal(whereItem),"##,###.00"));
                                                                      %>
                                                                      </strong></div>
                                                                    </td>
                                                              </tr>
                              </table>
                                                    </td>
                          </tr>
                          <%}%>
                      <%}%>
                      <tr> 
                        <td colspan="2">
						</td>
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
      
<script language="JavaScript">
    <% if(iCommand == Command.ADD) { %>
        document.frm_retmaterial.matItem.focus();
    <% } %>
</script>

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
<!-- #EndTemplate --></html>

