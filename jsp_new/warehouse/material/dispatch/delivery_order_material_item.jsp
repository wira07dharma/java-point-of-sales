<%-- 
    Document   : delivery_order_material_item
    Created on : Nov 26, 2013, 11:06:14 AM
    Author     : dimata005
--%>

<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

                   com.dimata.common.entity.location.Location,

                   com.dimata.common.entity.location.PstLocation,

                   com.dimata.gui.jsp.ControlList,

                   com.dimata.util.Command,

                   com.dimata.qdep.form.FRMHandler,

                   com.dimata.qdep.form.FRMMessage,

                   com.dimata.qdep.form.FRMQueryString,

                   com.dimata.qdep.entity.I_PstDocType,

                   com.dimata.gui.jsp.ControlLine,

                   com.dimata.gui.jsp.ControlCombo,

                   com.dimata.gui.jsp.ControlDate,

                   com.dimata.posbo.form.warehouse.FrmMatDispatchItem,

                   com.dimata.posbo.entity.masterdata.Unit,

                   com.dimata.posbo.entity.masterdata.Material,

                   com.dimata.posbo.entity.masterdata.PstMaterial,

                   com.dimata.posbo.entity.warehouse.*,

                   com.dimata.posbo.form.warehouse.FrmMatDispatch,

                   com.dimata.posbo.form.warehouse.CtrlMatDispatch,

                   com.dimata.posbo.form.warehouse.CtrlMatDispatchItem,

                   com.dimata.posbo.entity.masterdata.MaterialStock,

                   com.dimata.posbo.entity.masterdata.PstMaterialStock" %>

<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>



<%@ include file = "../../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>

<%@ include file = "../../../main/checkuser.jsp" %>



<%

/*if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){

	privAdd=false;

	privUpdate=false;

	privDelete=false;

}*/

%>



<!-- Jsp Block -->

<%!

public static final String textListGlobal[][] = {

    {"Transfer","Edit","Quantity transfer melebihi quantity yang ada"},

    {"Dispatch","Edit","Dispatch quantity more than available quantity"}

};



/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] =

{

    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier"},

    {"Number","Location","Destination","Date","Status","Remark","Supplier Invoice"}

};



/* this constant used to list text of listMaterialItem */

public static final String textListOrderItem[][] =

{

    {"No","Sku","Nama Barang","Unit","Qty","HPP","Harga Jual","Total","Gondola"},

    {"No","Code","Goods Name","Unit","Qty","Avg.Cost","Retail Price","Total","Brand"}

};





public double getPriceCost(Vector list, long oid){

    double cost = 0.00;

    if(list.size()>0){

        for(int k=0;k<list.size();k++){

            MatReceiveItem matReceiveItem = (MatReceiveItem)list.get(k);

            if(matReceiveItem.getMaterialId()==oid){

                cost = matReceiveItem.getCost();

                break;

            }

        }

    }

    return cost;

}



/**

 * this method used to maintain dfList

 */

public Vector drawListDfItem(int language,int iCommand,FrmMatDispatchItem frmObject,

        MatDispatchItem objEntity,Vector objectClass,

        long dfItemId,int start, String invoiceNumber, int tranUsedPriceHpp, long locationId, int brandInUse) {

    ControlList ctrlist = new ControlList();

    ctrlist.setAreaWidth("100%");

    ctrlist.setListStyle("listgen");

    ctrlist.setTitleStyle("listgentitle");

    ctrlist.setCellStyle("listgensell");

    ctrlist.setHeaderStyle("listgentitle");

    ctrlist.addHeader(textListOrderItem[language][0],"3%");

    ctrlist.addHeader(textListOrderItem[language][1],"10%");

	if(brandInUse==1){

		ctrlist.addHeader(textListOrderItem[language][8],"10%");

	}

    ctrlist.addHeader(textListOrderItem[language][2],"40%");

    ctrlist.addHeader(textListOrderItem[language][3],"5%");

    if(tranUsedPriceHpp==0)

        ctrlist.addHeader(textListOrderItem[language][5],"10%");

    else

        ctrlist.addHeader(textListOrderItem[language][6],"10%");



    ctrlist.addHeader(textListOrderItem[language][4],"5%");

    ctrlist.addHeader(textListOrderItem[language][7],"10%");

    //ctrlist.addHeader(textListOrderItem[language][6],"10%");



    Vector list = new Vector(1,1);

    Vector listError = new Vector(1,1);



    Vector lstData = ctrlist.getData();

    Vector rowx = new Vector(1,1);

    ctrlist.reset();

    ctrlist.setLinkRow(1);

    int index = -1;

    if(start<0)

        start=0;





    /**

     * get data receive for get price cost

     */

   /* String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+invoiceNumber+"'";

    Vector list = PstMatReceive.list(0,0,whereClause,"");

    Vector listItem = new Vector(1,1);

    if(list!=null && list.size()>0){

        MatReceive matReceive = (MatReceive)list.get(0);

        whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+matReceive.getOID();

        listItem = PstMatReceiveItem.list(0,0,whereClause,"");

    }*/



    for(int i=0; i<objectClass.size(); i++) {

        Vector temp = (Vector)objectClass.get(i);

        MatDispatchItem dfItem = (MatDispatchItem)temp.get(0);

        Material mat = (Material)temp.get(1);

        Unit unit = (Unit)temp.get(2);

        rowx = new Vector();

        start = start + 1;



        //System.out.println("hpp barang");

        // double cost = getPriceCost(listItem,dfItem.getMaterialId());



        if (dfItemId == dfItem.getOID()) index = i;

        if(index==i && (iCommand==Command.EDIT || iCommand==Command.ASK)) {

            //mencari stok yang tersedia

            String whereClause = PstMaterialStock.fieldNames[PstMaterialStock.FLD_MATERIAL_UNIT_ID]+"="+dfItem.getMaterialId();

            whereClause += " AND "+PstMaterialStock.fieldNames[PstMaterialStock.FLD_LOCATION_ID]+"="+locationId;

            Vector vMaterialStock = PstMaterialStock.list(0, 0, whereClause, "");

            MaterialStock objMaterialStock = new MaterialStock();

            if(vMaterialStock.size() > 0) {

             objMaterialStock = (MaterialStock)vMaterialStock.get(0);

            }



            rowx.add(""+start);

            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+dfItem.getMaterialId()+

                    "\"><input type=\"text\" size=\"15\" name=\"matCode\" value=\""+mat.getSku()+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">"); // <a href=\"javascript:cmdCheck()\">CHK</a>

			if(brandInUse==1){

				rowx.add("<input type=\"text\" size=\"15\" name=\"matbrand\" value=\""+mat.getGondolaCode()+"\" class=\"hiddenText\"  readOnly>");

			}

            rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+mat.getName()+"\" class=\"hiddenText\"  readOnly>");

            rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" value=\""+dfItem.getUnitId()+

                    "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+unit.getCode()+"\" class=\"hiddenText\"  readOnly>");

            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"\" class=\"hiddenText\" readOnly></div>");

            rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" style=\"text-align:right\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"\" class=\"formElemen\" style=\"text-align:right\">"+

                    "<input type=\"hidden\" name=\"avbl_qty\" value=\""+objMaterialStock.getQty()+"\"></div>");



            rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"\" class=\"hiddenText\" readOnly></div>");



            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"</div>");

            // rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");

        } else {

            rowx.add(""+start+"");

            rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(dfItem.getOID())+"')\">"+mat.getSku()+"</a>");

			if(brandInUse==1){

				rowx.add(mat.getGondolaCode());

			}

            rowx.add(mat.getName());

            rowx.add(unit.getCode());

            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHpp())+"</div>");



            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){

                String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID]+"="+dfItem.getOID();

                int cnt = PstDispatchStockCode.getCount(where);

                if(cnt<dfItem.getQty()){

                    if(listError.size()==0){

                        listError.add("Silahkan cek :");

                    }

                    listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty pengiriman");

                }

                rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(dfItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");

            }else{

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getQty())+"</div>");

            }



            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfItem.getHppTotal())+"</div>");

            //rowx.add("<div align=\"right\">"+String.valueOf(dfItem.getQty())+"</div>");

            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");

        }

        lstData.add(rowx);

    }



    rowx = new Vector();

    String styleQty="";

    if(iCommand==Command.ADD || (iCommand==Command.SAVE && frmObject.errorSize()>0)) {

        if(iCommand==Command.ADD){

           styleQty="display:none";

        }

        rowx.add(""+(start+1));

        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID]+"\" value=\""+""+

                "\"><input type=\"text\" size=\"13\" name=\"matCode\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");

		if(brandInUse==1){

			rowx.add("<input type=\"text\" size=\"15\" name=\"matbrand\" value=\"\" class=\"hiddenText\"  readOnly>");

		}
        rowx.add("<input type=\"text\" size=\"50\" name=\"matItem\" value=\""+""+"\" class=\"formElemen\" onKeyUp=\"javascript:keyDownCheck(event)\">&nbsp;<a href=\"javascript:cmdCheck()\">CHK</a>");

        rowx.add("<input type=\"hidden\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID]+"\" class=\"hiddenText\" value=\""+""+

                "\"><input type=\"text\" size=\"5\" name=\"matUnit\" value=\""+""+"\" class=\"hiddenText\" readOnly>");

        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");

        rowx.add("<div align=\"right\"><input type=\"text\" size=\"5\" onkeyup=\"javascript:cntTotal(this, event)\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_QTY] +"\" value=\""+""+"\" class=\"formElemen\" style=\"text-align:right;"+styleQty+"\"   >"+

                "<input type=\"hidden\" name=\"avbl_qty\" value=\"0\"></div>");

        rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\""+frmObject.fieldNames[frmObject.FRM_FIELD_HPP_TOTAL]+"\" value=\""+""+"\" class=\"hiddenText\" readOnly></div>");



        //rowx.add("<div align=\"right\"></div>");

        //rowx.add("<div align=\"right\"></div>");



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



/**

 * get request data from current form

 */

int iCommand = FRMQueryString.requestCommand(request);

int startItem = FRMQueryString.requestInt(request,"start_item");

int prevCommand = FRMQueryString.requestInt(request,"prev_command");

int appCommand = FRMQueryString.requestInt(request,"approval_command");

long oidDispatchMaterial = FRMQueryString.requestLong(request,"hidden_dispatch_id");

long oidDispatchMaterialItem = FRMQueryString.requestLong(request,"hidden_dispatch_item_id");
long oidReceiveMaterialItem = FRMQueryString.requestLong(request,"hidden_receive_id");

long timemls = System.currentTimeMillis();

long oidReceiveId = FRMQueryString.requestLong(request,"hidden_receive_item_id");



/**

 * initialization of some identifier

 */

int iErrCode = FRMMessage.NONE;

String msgString = "";



/**

 * purchasing pr code and title

 */

String dfCode = ""; //i_pstDocType.getDocCode(docType);

String dfTitle = "Transfer Barang"; //i_pstDocType.getDocTitle(docType);

String dfItemTitle = dfTitle + " Item";



/**

 * purchasing pr code and title

 */

String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_DF));





/**

 * process on df main

 */

CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

iErrCode = ctrlMatDispatch.action(Command.EDIT,oidDispatchMaterial);

FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

MatDispatch df = ctrlMatDispatch.getMatDispatch();



/**

 * check if document already closed or not

 */

boolean documentClosed = false;

if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED) {

    documentClosed = true;

}



/**

 * check if document may modified or not

 */

boolean privManageData = true;



ControlLine ctrLine = new ControlLine();

CtrlMatDispatchItem ctrlMatDispatchItem = new CtrlMatDispatchItem(request);

ctrlMatDispatchItem.setLanguage(SESS_LANGUAGE);

//by dyas
//tambah userName dan userId
iErrCode = ctrlMatDispatchItem.action(iCommand,oidDispatchMaterialItem,oidDispatchMaterial, userName, userId);

FrmMatDispatchItem frmMatDispatchItem = ctrlMatDispatchItem.getForm();

MatDispatchItem dfItem = ctrlMatDispatchItem.getMatDispatchItem();

msgString = ctrlMatDispatchItem.getMessage();



/*int comm = iCommand;

if(iErrCode!=0 && dfItem.getOID()!=0 && iCommand==Command.SAVE){

    comm = Command.EDIT;

}else if(iErrCode!=0 && dfItem.getOID()==0 && iCommand==Command.SAVE){

    comm = Command.ADD;

}*/



String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;

String orderClauseItem = "";

int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);

int recordToGetItem = 10;



if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {

    startItem = ctrlMatDispatchItem.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);

}



/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */

if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {

    iCommand = Command.ADD;

    oidDispatchMaterialItem = 0;

    /** agar form add item tampil pada list paling akhir */

    startItem = ctrlMatDispatchItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);

}

String order=" DFI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
Vector listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial, order);

if(listMatDispatchItem.size()<1 && startItem>0) {

    if(vectSizeItem-recordToGetItem > recordToGetItem) {

        startItem = startItem - recordToGetItem;

    } else {

        startItem = 0 ;

        iCommand = Command.FIRST;

        prevCommand = Command.FIRST;

    }

    listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);

}



%>



<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->

<head>

<!-- #BeginEditable "doctitle" -->

<title>Dimata - ProChain POS</title>

<script language="JavaScript">

<!--

//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

function main(oid,comm){

    document.frm_matdispatch.command.value=comm;

    document.frm_matdispatch.hidden_dispatch_id.value=oid;

    document.frm_matdispatch.action="delivery_order_material_edit.jsp";

    document.frm_matdispatch.submit();

}



function gostock(oid){

document.frm_matdispatch.command.value="<%=Command.EDIT%>";

document.frm_matdispatch.hidden_dispatch_item_id.value=oid;

document.frm_matdispatch.action="df_stockcode.jsp";

document.frm_matdispatch.submit();

}



function cmdAdd()

{

    document.frm_matdispatch.hidden_dispatch_item_id.value="0";

    document.frm_matdispatch.command.value="<%=Command.ADD%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}

function cmdAddItemReceive()

{

    document.frm_matdispatch.hidden_dispatch_item_id.value="0";

    document.frm_matdispatch.command.value="<%=Command.ADD%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="df_stock_material_receive_search.jsp";

    if(compareDateForAdd()==true)

            document.frm_matdispatch.submit();

}




function cmdEdit(oidDispatchMaterialItem)

{

    document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdAsk(oidDispatchMaterialItem)

{

    document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_matdispatch.command.value="<%=Command.ASK%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}

function printForm()

{

	var typePrint = document.frm_matdispatch.type_print_tranfer.value;

    //window.open("<%=approot%>/servlet/com.dimata.posbo.report.TransferPrintPDF?hidden_dispatch_id=<%=oidDispatchMaterial%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&brandinuse=<%=brandInUse%>");

    window.open("df_stock_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"&timemls=<%=timemls%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");

}

function cmdSave(){

    var avblQty = parseFloat(document.frm_matdispatch.avbl_qty.value);

    var transferQty = parseFloat(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value);



<%!

/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */

static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");

static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;



%>

    if(transferQty<=0.000){

           alert("Quantity < 0");

        } else {

            <% if(calculateQtyNull) { %>

                document.frm_matdispatch.command.value="<%=Command.SAVE%>";

                document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_matdispatch.action="delivery_order_material_item.jsp";

                document.frm_matdispatch.submit();

            <%} else { %>

            if(transferQty <= avblQty) {

                document.frm_matdispatch.command.value="<%=Command.SAVE%>";

                document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_matdispatch.action="delivery_order_material_item.jsp";

                document.frm_matdispatch.submit();

            }

            else {

                alert("<%=textListGlobal[SESS_LANGUAGE][2]%> ("+avblQty+" "+document.frm_matdispatch.matUnit.value+")");

            }

            <%}%>

    }

}



function cmdConfirmDelete(oidDispatchMaterialItem)

{

    document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_matdispatch.command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.approval_command.value="<%=Command.DELETE%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdCancel(oidDispatchMaterialItem)

{

    document.frm_matdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdBack()

{

    document.frm_matdispatch.command.value="<%=Command.EDIT%>";

    document.frm_matdispatch.start_item.value = 0;

    document.frm_matdispatch.action="delivery_order_material_edit.jsp";

    document.frm_matdispatch.submit();

}



var winSrcMaterial;

function cmdCheck()

{

    var strvalue  = "dfwhdosearch.jsp?command=<%=Command.FIRST%>"+
        "&location_id=<%=df.getLocationId()%>"+
        "&hidden_dispatch_id=<%=oidDispatchMaterial%>"+
        "&mat_code="+document.frm_matdispatch.matCode.value +
        "&txt_materialname="+document.frm_matdispatch.matItem.value;
    winSrcMaterial = window.open(strvalue,"Stock_Material", "height=500,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
    if (window.focus) { winSrcMaterial.focus();}

}



function keyDownCheck(e){

   if (e.keyCode == 13) {

        //document.all.aSearch.focus();

        cmdCheck();

   }

}



function changeFocus(element){

    if(element.name == "matCode") {

        document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();

    }

    else if(element.name == "<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>") {

        cmdSave();

    }

    else {

        cmdSave();

    }

}



function cntTotal(element, evt)

{

    var qty = cleanNumberInt(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);

    var price = cleanNumberFloat(document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value,guiDigitGroup,',');



    if(qty<0.0000){

          document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value=0;

          return;

        }



    if(price==""){

            price = 0;

    }

    /*

    var qty = document.frm_recmaterial.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value;

    var price = document.frm_recmaterial.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value;

    */

    if(!(isNaN(qty)) && (qty != '0'))

    {

        var amount = price * qty;

        if(isNaN(amount)){

            amount = "0";

        }

        document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);

    }

    else

    {

        document.frm_matdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();

    }



    if (evt.keyCode == 13) {

        changeFocus(element);

    }

}



function cmdListFirst()

{

    document.frm_matdispatch.command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.prev_command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListPrev()

{

    document.frm_matdispatch.command.value="<%=Command.PREV%>";

    document.frm_matdispatch.prev_command.value="<%=Command.PREV%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListNext()

{

    document.frm_matdispatch.command.value="<%=Command.NEXT%>";

    document.frm_matdispatch.prev_command.value="<%=Command.NEXT%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdListLast()

{

    document.frm_matdispatch.command.value="<%=Command.LAST%>";

    document.frm_matdispatch.prev_command.value="<%=Command.LAST%>";

    document.frm_matdispatch.action="delivery_order_material_item.jsp";

    document.frm_matdispatch.submit();

}



function cmdBackList()

{

    document.frm_matdispatch.command.value="<%=Command.FIRST%>";

    document.frm_matdispatch.action="df_stock_wh_material_list.jsp";

    document.frm_matdispatch.submit();

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



function MM_findObj(n, d) { //v4.01

    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);

    if(!x && d.getElementById) x=d.getElementById(n); return x;

}

//-->

</script>

<!-- #EndEditable -->

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<!-- #BeginEditable "styles" -->

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

  <tr>

    <td valign="top" align="left">

      <table width="100%" border="0" cellspacing="0" cellpadding="0">

        <tr>

          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->

            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>

        </tr>

        <tr>

          <td><!-- #BeginEditable "content" -->

            <form name="frm_matdispatch" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveId%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterialItem%>">
              <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">
              <input type="hidden" name="<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">

              <input type="hidden" name="approval_command" value="<%=appCommand%>">

              <table width="100%" cellpadding="1" cellspacing="0">

                <tr align="center">

                  <td colspan="3">

                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="25%">: <b>

                          <%

                          if(df.getDispatchCode()!="" && iErrCode==0) {

                                out.println(df.getDispatchCode());

                          } else {

                                out.println("");

                          }

                          %>

                        </b></td>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="31%">:

                        <%

                        Vector obj_locationid = new Vector(1,1);

                        Vector val_locationid = new Vector(1,1);

                        Vector key_locationid = new Vector(1,1);

                        //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE

                        Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_NAME]);

                        for(int d=0;d<vt_loc.size();d++) {

                            Location loc = (Location)vt_loc.get(d);

                            val_locationid.add(""+loc.getOID()+"");

                            key_locationid.add(loc.getName());

                        }

                        String select_locationid = ""+df.getLocationId(); //selected on combo box

			%>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%> </td>

                        <td width="9%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>

                        <td width="17%" rowspan="3" align="right" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>

                      </tr>

                      <tr>

                        <td><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                        <td>: <%=ControlDate.drawDateWithStyle(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_DATE], (df.getDispatchDate()==null) ? new Date() : df.getDispatchDate(), 1, -5, "formElemen", "")%></td>

                        <td><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>

                        <td>:

                        <%

                        Vector obj_locationid1 = new Vector(1,1);

                        Vector val_locationid1 = new Vector(1,1);

                        Vector key_locationid1 = new Vector(1,1);

                        String locWhClause = ""; //PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;

                        String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);

                        Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);

                        for(int d = 0; d < vt_loc1.size(); d++) {

                            Location loc1 = (Location)vt_loc1.get(d);

                            val_locationid1.add(""+loc1.getOID()+"");

                            key_locationid1.add(loc1.getName());

                        }

                        String select_locationid1 = ""+df.getDispatchTo(); //selected on combo box

                        %>

                        <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_TO], null, select_locationid1, val_locationid1, key_locationid1, "", "formElemen")%> </td>

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

                        <td colspan="3" >

                          <table width="100%" border="0" cellspacing="0" cellpadding="0" >

                            <tr align="left" valign="top">

                              <%

                              Vector listError = new Vector(1,1);

                              try {

                              %>

                              <td height="22" valign="middle" colspan="3">

                              <%

                              Vector list = drawListDfItem(SESS_LANGUAGE,iCommand,frmMatDispatchItem, dfItem,listMatDispatchItem,oidDispatchMaterialItem,startItem,df.getInvoiceSupplier(),tranUsedPriceHpp,df.getLocationId(),brandInUse);

                              out.println(""+list.get(0));

                              listError = (Vector)list.get(1);

                              %>

                              </td>

                              <%

                              } catch(Exception e) {

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

                              %></td>

                            </tr>

                            <tr align="left" valign="top">

                              <td height="22" colspan="3" valign="middle">

                                <%

                                ctrLine.setLocationImg(approot+"/images");



                                // set image alternative caption

                                ctrLine.setAddNewImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));

                                ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));

                                ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");

                                ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));

                                ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));



                                ctrLine.initDefault();

                                ctrLine.setTableWidth("65%");

                                String scomDel = "javascript:cmdAsk('"+oidDispatchMaterialItem+"')";

                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidDispatchMaterialItem+"')";

                                String scancel = "javascript:cmdEdit('"+oidDispatchMaterialItem+"')";

                                ctrLine.setCommandStyle("command");

                                ctrLine.setColCommStyle("command");



                                // set command caption

                                ctrLine.setAddCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true));

                                ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_SAVE,true));

                                ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)+" List");

                                ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ASK,true));

                                ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_DELETE,true));

                                ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_CANCEL,false));





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

                                <table width="70%" border="0" cellspacing="2" cellpadding="0">

                                  <tr>

                                    <% if(df.getDispatchStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>

                                    <td width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>

                                    <td width="20%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                                    <td width="6%"><a href="javascript:cmdAddItemReceive()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_ADD,true)%>"></a></td>

                                    <td width="20%"><a href="javascript:cmdAddItemReceive()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item Receive",ctrLine.CMD_ADD,true)%></a></td>

                                    <% } %>

                                    <td width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%>"></a></td>

                                    <td width="47%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfCode+" Item",ctrLine.CMD_BACK,true)%></a></td>



                                     <td><select name="type_print_tranfer">

                                          <option value="0">Price Cost</option>

                                          <option value="1">Sell Price</option>

                                                                  <option value="2">No Price</option>

                                        </select>

                                    </td>


                                  </tr>
                                  <tr>
                                      <td width="6%"></td>

                                      <td width="47%"></td>

                                      <td width="6%" valign="top"><a href="javascript:printForm('<%=oidDispatchMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="Cetak Transfer"></a></td>

                                      <td width="47%" nowrap>&nbsp; <a href="javascript:printForm('<%=oidDispatchMaterial%>')" class="command" >Cetak Transfer</a></td>

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

                        <td colspan="3">

                        </td>

                      </tr>

                    </table>

                  </td>

                </tr>

              </table>

            </form>

            <script language="JavaScript">

                document.frm_matdispatch.matCode.focus();

            </script>

            <!-- #EndEditable --></td>

        </tr>

      </table>

    </td>

  </tr>

  <tr>

    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->

      <%@ include file = "../../../main/footer.jsp" %>

      <!-- #EndEditable --> </td>

  </tr>

</table>

</body>

<!-- #EndTemplate --></html>




