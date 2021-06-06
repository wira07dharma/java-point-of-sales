<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
                   com.dimata.posbo.entity.masterdata.Material,
                   com.dimata.posbo.entity.masterdata.Unit,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.entity.masterdata.PstMaterial" %>
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
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!//
    static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    static boolean bEnableExpiredDate = (sEnableExpiredDate != null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

    public static final String textListGlobal[][] = {
        {"Penerimaan", "Dari Toko/Gudang", "Pencarian", "Daftar", "Edit", "Tidak ada data penerimaan..."},
        {"Recive", "From Store/Warehouse", "Search", "List", "Edit", "Receive", "No receive data available..."}
    };

    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi", "Tanggal", "Penerimaan Dari", "Status", "Keterangan", "Nomor Kirim"},
                {"Number", "Location", "Date", "Receive From", "Status", "Remark", "Dispatch Number"}
            };


    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama", "Kadaluarsa", "Unit", "HPP", "Harga Jual", "Mata Uang", "Qty", "Total HPP", "Hapus"},
                {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Price", "Currency", "Qty", "Total Cost", "Delete"}
            };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    /**
     * this method used to maintain poMaterialList
     */
    public Vector drawListRetItem(int language, int iCommand, FrmMatReceiveItem frmObject, MatReceiveItem objEntity,
            Vector objectClass, long recItemId, int start, int tranUsedPriceHpp, boolean privShowQtyPrice, boolean privManageData, String approot, int typeOfBusinessDetail) {
        Vector list = new Vector(1, 1);
        Vector listError = new Vector(1, 1);
        String result = "";

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.addHeader(textListOrderItem[language][0], "5%");
        ctrlist.addHeader(textListOrderItem[language][1], "10%");
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        if (bEnableExpiredDate) {
            ctrlist.addHeader(textListOrderItem[language][3], "20%");
        }
        ctrlist.addHeader(textListOrderItem[language][4], "5%");
        ctrlist.addHeader(textListOrderItem[language][8], "5%");//qty
        if(typeOfBusinessDetail == 2){
            ctrlist.addHeader("Berat", "5%");//berat
            ctrlist.addHeader("Ongkos/Batu", "5%");//ongkos
        }
        if (privShowQtyPrice) {
            if (tranUsedPriceHpp == 0) {
                ctrlist.addHeader(textListOrderItem[language][5], "10%");//hpp
            } else {
                ctrlist.addHeader(textListOrderItem[language][6], "10%");//harga jual
            }
        }        
        if (privShowQtyPrice) {
            ctrlist.addHeader(textListOrderItem[language][9], "10%");
        }
        ctrlist.addHeader(textListOrderItem[language][10], "10%");
        
        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            rowx = new Vector();
            start = start + 1;
            
            if(typeOfBusinessDetail == 2){
                mat.setName(SessMaterial.setItemNameForLitama(recItem.getMaterialId()));
            }

            if (recItemId == recItem.getOID()) {
                index = i;
            }
            if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                rowx.add("" + start);
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + recItem.getMaterialId() + "\">"
                        + "<input type=\"text\" size=\"15\" name=\"matCode\" value=\"" + mat.getSku() + "\" class=\"formElemen\"><a href=\"javascript:cmdCheck()\">CHK</a>");
                rowx.add("<input type=\"text\" size=\"20\" name=\"matItem\" value=\"" + mat.getName() + "\" class=\"hiddenText\" readOnly>");
                if (bEnableExpiredDate) {
                    rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], (recItem.getExpiredDate() == null) ? new Date() : recItem.getExpiredDate(), 1, -5, "formElemen", ""));
                }
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + recItem.getUnitId() + "\">"
                        + "<div align=\"center\"><input type=\"text\" style=\"text-align:center\" size=\"5\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"hiddenText\" readOnly></div>");
                if (privShowQtyPrice) {
                    rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal()\"></div>");
                    if(typeOfBusinessDetail == 2){
                        rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + String.format("%.3f", recItem.getBerat()) + "\" class=\"formElemen hiddenText\"></div>");
                        rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getForwarderCost()) + "\" class=\"formElemen hiddenText\"></div>");
                    }
                    rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"hiddenText\" ></div><input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "\">");                    
                    rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
                } else {
                    rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal()\" style=\"text-align:right\"></div>"
                            + "<div align=\"right\"><inputtype=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "\" class=\"hiddenText\" ></div>"
                            + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"" + recItem.getCurrencyId() + "\">"
                            + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
                }
                rowx.add("");
                
            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(recItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                rowx.add(mat.getName());
                if (bEnableExpiredDate) {
                    rowx.add("<div align=\"center\">" + Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy") + "</div>");
                }
                rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");                
                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItem.getOID();
                    int cnt = PstReceiveStockCode.getCount(where);
                    if (cnt < recItem.getQty()) {
                        if (listError.size() == 0) {
                            listError.add("Please Check :");
                        }
                        listError.add("" + listError.size() + ". jumlah Serial kode stok barang '" + mat.getName() + "' tidak sama dengan jumlah qty penerimaan");
                    }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('" + String.valueOf(recItem.getOID()) + "')\">[ST.CD]</a> " + String.valueOf(recItem.getQty()) + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getQty()) + "</div>");
                }
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"right\">" + String.format("%,.3f",recItem.getBerat()) + "</div>");
                    rowx.add("<div align=\"right\">" + String.format("%,.0f",recItem.getForwarderCost()) + ".00</div>");
                }
                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getCost()) + "</div>");
                }
                if (privShowQtyPrice) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(recItem.getTotal()) + "</div>");
                }

                // add by fitra
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(recItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");

            }
            lstData.add(rowx);
        }

        rowx = new Vector();
        if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
            rowx.add("" + (start + 1));
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + ""
                    + "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\"" + "" + "\" class=\"formElemen\"><a href=\"javascript:cmdCheck()\"> CHK</a>");
            rowx.add("<input type=\"text\" size=\"30\" name=\"matItem\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\"> CHK</a>");
            if (bEnableExpiredDate) {
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[frmObject.FRM_FIELD_EXPIRED_DATE], new Date(), 1, -5, "formElemen", ""));
            }
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + ""
                    + "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\"" + "" + "\" class=\"hiddenText\" readOnly>");
            if (privShowQtyPrice) {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal()\" style=\"text-align:right\"></div>");
                if(typeOfBusinessDetail == 2){
                    rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_BERAT] + "\" value=\"" + "\" class=\"formElemen\"></div>");
                    rowx.add("<div align=\"center\"><input type=\"text\" style=\"text-align:right\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_FORWARDER_COST] + "\" value=\"" + "\" class=\"formElemen\"></div>");
                }
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"hiddenText\" ></div><input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\">");                
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
            } else {
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyUp=\"javascript:cntTotal()\" style=\"text-align:right\"></div>"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_COST] + "\" value=\"" + "" + "\" class=\"hiddenText\" ></div><input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_CURRENCY_ID] + "\" value=\"\">"
                        + "<div align=\"right\"><input type=\"hidden\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL] + "\" value=\"" + "" + "\" class=\"hiddenText\" onBlur=\"javascript:cntTotal()\" readOnly></div>");
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
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "hidden_receive_item_id");

    /**
     * initialization of some identifier
     */
    int iErrCode = FRMMessage.NONE;
    String msgString = "";

    /**
     * purchasing pr code and title
     */
    String recCode = "";//i_pstDocType.getDocCode(docType);
    String retTitle = "Penerimaan"; //i_pstDocType.getDocTitle(docType);
    String recItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();

    /**
     * check if document may modified or not
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
    ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReceiveItem.action(iCommand, oidReceiveMaterialItem, oidReceiveMaterial, userName, userId);
    FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
    MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
    msgString = ctrlMatReceiveItem.getMessage();

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
    String orderClauseItem = "";
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
    int recordToGetItem = 10;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatReceiveItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    Vector listMatReceiveItem = PstMatReceiveItem.list(startItem, recordToGetItem, whereClauseItem);
    if (listMatReceiveItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listMatReceiveItem = PstMatReceiveItem.list(startItem, recordToGetItem, whereClauseItem);
    }

//Fetch Dispatch Material Info
    MatDispatch df = new MatDispatch();
    try {
        df = PstMatDispatch.fetchExc(rec.getDispatchMaterialId());
    } catch (Exception e) {
    }

    /**
     * get list location
     */
    Vector obj_locationid = new Vector(1, 1);
    Vector val_locationid = new Vector(1, 1);
    Vector key_locationid = new Vector(1, 1);
    Vector vt_loc = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_NAME]);
    for (int d = 0; d < vt_loc.size(); d++) {
        Location loc = (Location) vt_loc.get(d);
        val_locationid.add(String.valueOf(loc.getOID()));
        key_locationid.add(loc.getName());
    }

// add by fitra 17-05-2014
    if (iCommand == Command.SAVE && iErrCode == 0) {
        iCommand = Command.ADD;
        oidReceiveMaterialItem = 0;
    }
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm)
{
	document.frm_recmaterial.command.value=comm;
	document.frm_recmaterial.hidden_receive_id.value=oid;
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd()
{
	document.frm_recmaterial.hidden_receive_item_id.value="0";
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	if(compareDateForAdd()==true)
		document.frm_recmaterial.submit();
}






function cmdEdit(oidReceiveMaterialItem)
{
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdAsk(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.ASK%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdSave()
{
	document.frm_recmaterial.command.value="<%=Command.SAVE%>"; 
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";	
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
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
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function sumPrice()
{
}		

function cmdCheck()
{
	var strvalue  = "materialdfdosearch.jsp?command=<%=Command.FIRST%>"+
	//var strvalue  = "materialdosearch_recfromdfwh.jsp?command=<%=Command.FIRST%>"+
					"&mat_code="+document.frm_recmaterial.matCode.value+
					"&oidDispatch="+document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>.value+
					"&mat_receive_oid=<%=rec.getOID()%>";										
	window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=no");
}

function keyDownCheck(e){
   if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdCheck();
   }
}

function cntTotal(e)
{
	var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
	var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
	//var qty = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value;
	//var price = document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value;
	if(!(isNaN(qty)) && (qty != '0'))
	{
		var amount = parseFloat(price) * qty;
		document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = amount;					 
	}
	else
	{
		document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
	}
        
        if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        //cmdAdd();
        cmdSave();
}

        
        
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="receive_store_wh_materialitem.jsp";
	document.frm_recmaterial.submit();
}

function cmdBackList(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function gostock(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.type_doc.value = 0;
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
                            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> > <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%> <!-- #EndEditable --></td>
                    </tr>
                    <tr> 
                        <td><!-- #BeginEditable "content" --> 
                            <form name="frm_recmaterial" method ="post" action="">
                                <input type="hidden" name="command" value="<%=iCommand%>">
                                <input type="hidden" name="prev_command" value="<%=prevCommand%>">			  
                                <input type="hidden" name="start_item" value="<%=startItem%>">
                                <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                                <input type="hidden" name="type_doc" value="">
                                <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
                                <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                                <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=rec.getDispatchMaterialId()%>">
                                <table width="100%" cellpadding="1" cellspacing="0">
                                    <tr align="center"> 
                                        <td colspan="3" class="title"> 
                                            <table width="100%" border="0" cellpadding="1">
                                                <tr>
                                                    <td align="left">&nbsp;</td>
                                                    <td align="left">&nbsp;</td>
                                                    <td align="left">&nbsp;</td>
                                                    <td align="left">&nbsp;</td>
                                                </tr>
                                                <tr>
                                                    <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td> 
                                                    <td width="20%" align="left"> : <b><%=rec.getRecCode()%></b></td>

                                                    <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                    <td width="20%"> : <input type="text" name="txt_dfnumber"  value="<%= df.getDispatchCode()%>" class="formElemen" size="25" disabled="true">

                                                    <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                    <td width="1%">:</td>
                                                    <td width="20%" rowspan="2" valign=""> 
                                                        <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="3" style="width: 200px" disabled="true"><%=rec.getRemark()%></textarea>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td> 
                                                    <td width="20%" align="left"> : 
                                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, String.valueOf(rec.getLocationId()), val_locationid, key_locationid, "disabled=\"true\"", "formElemen")%>
                                                    </td>

                                                    <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                    <td width="20%"> : 
                                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_FROM], null, String.valueOf(rec.getReceiveFrom()), val_locationid, key_locationid, "", "formElemen")%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td width="10%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td> 
                                                    <td width="20%" align="left"> : <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], rec.getReceiveDate(), 1, -5, "formElemen", "disabled=\"true\"")%></td>

                                                </tr>
                                                <tr>
                                                    <td align="left">&nbsp;</td>
                                                    <td align="left">&nbsp;</td>
                                                    <td align="center">&nbsp;</td>
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
                                                                    Vector listError = new Vector(1, 1);
                                                                    try {
                                                                %>
                                                                <td height="22" valign="middle" colspan="3"> <%
                                                                    Vector list = drawListRetItem(SESS_LANGUAGE, iCommand, frmMatReceiveItem, recItem, listMatReceiveItem, oidReceiveMaterialItem, startItem, tranUsedPriceHpp, privShowQtyPrice, privManageData, approot, typeOfBusinessDetail);
                                                                    out.println(list.get(0));
                                                                    listError = (Vector) list.get(1);
                                                                    %> </td>
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
                                                                    </span> </td>
                                                            </tr>
                                                            <tr align="left" valign="top"> 
                                                                <td height="22" valign="middle" colspan="3"> 
                                                                    <%
                                                                        ctrLine.setLocationImg(approot + "/images");

                                                                        // set image alternative caption 
                                                                        ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true));
                                                                        ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_SAVE, true));
                                                                        ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                        ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ASK, true));
                                                                        ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_CANCEL, false));

                                                                        ctrLine.initDefault();
                                                                        ctrLine.setTableWidth("65%");
                                                                        String scomDel = "javascript:cmdAsk('" + oidReceiveMaterialItem + "')";
                                                                        String sconDelCom = "javascript:cmdConfirmDelete('" + oidReceiveMaterialItem + "')";
                                                                        String scancel = "javascript:cmdEdit('" + oidReceiveMaterialItem + "')";
                                                                        ctrLine.setCommandStyle("command");
                                                                        ctrLine.setColCommStyle("command");

                                                                        // set command caption 
                                                                        ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true));
                                                                        ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_SAVE, true));
                                                                        ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_BACK, true) + " List");
                                                                        ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ASK, true));
                                                                        ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_DELETE, true));
                                                                        ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_CANCEL, false));

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
                                                                            <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                            <td width="94%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, recCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                        </tr>
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
                                                <tr> 
                                                    <td colspan="3">&nbsp;</td>
                                                </tr>
                                                <%if (listMatReceiveItem != null && listMatReceiveItem.size() > 0) {%>
                                                <%if (privShowQtyPrice) {%>
                                                <tr>
                                                    <td colspan="2" valign="top">&nbsp; </td>
                                                    <td width="27%" valign="top">
                                                        <table width="100%" border="0">
                                                            <tr>
                                                                <td width="44%">
                                                                    <div align="right"><%="TOTAL : " + recCode%></div>
                                                                </td>
                                                                <td width="15%">
                                                                    <div align="right"></div>
                                                                </td>
                                                                <td width="41%">
                                                                    <div align="right">
                                                                        <%
                                                                            String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
                                                                            out.println(FRMHandler.userFormatStringDecimal(PstMatReceiveItem.getTotal(whereItem)));
                                                                        %>
                                                                    </div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <%}%>
                                                <tr>
                                                    <td colspan="3"><span class="fielderror">
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
                                                <%}%>
                                                <tr> 
                                                    <td colspan="3"> 
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
            <% if (iCommand == Command.ADD) { %>
        document.frm_recmaterial.matItem.focus();
            <% } %>
        </script>


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

<!-- #EndTemplate -->
</html>
