<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.MaterialDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
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

<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->

<%!

public static final String textListGlobal[][] = {

    {"Transfer","Edit","Quantity transfer melebihi quantity yang ada"},

    {"Dispatch","Edit","Dispatch quantity more than available quantity"}

};

public static final String textOrderList[][] = {

    {"Cetak", "ke Excel", "ke PDF"},

    {"Print", "to Excel", "to PDF"}

};

/* this constant used to list text of listHeader */

public static final String textListOrderHeader[][] =

{

    {"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Keterangan","Nota Supplier","Etalase Asal","Etalase Tujuan", "Kode BC"},

    {"Number","Location","Destination","Date","Status","Remark","Supplier Invoice","From Etalase","Etalase Destination","Customs Code"}

};



/* this constant used to list text of listMaterialItem */

public static final String textListOrderItem[][] =

{

    {"No","Invoice","Detail Barang","Status","Aksi"},

    {"No","Invoice","Detail Item","Status","Action"}

};

/* this constant used to list text of listMaterialItem */

public static final String textListOrderItemDetail[][] =

{

    {"SKU","Nama Barang","Qty","Harga Satuan","Total Harga"},

    {"SKU","Item Name","Qty","Price per Item","Total Price"}

};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
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

public String drawListInvoices(int language, Vector objectClass,int start) {

    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItem[language][0],"1%");//no
    ctrlist.addHeader(textListOrderItem[language][1],"10%");//invoice
    ctrlist.addHeader(textListOrderItem[language][2],"30%");//detail
    ctrlist.addHeader(textListOrderItem[language][3],"10%");//status
    ctrlist.addHeader(textListOrderItem[language][4],"10%");//aksi
    
	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    ctrlist.reset();
    int index = -1;
    if(start<0){
        start=0;
	}
    for(int i=0; i<objectClass.size(); i++) {
		start++;
		Vector rowx = new Vector(1,1);
		Vector temp = (Vector) objectClass.get(i);
		MatDispatchBill mdb = (MatDispatchBill) temp.get(0);
		BillMain bm = (BillMain) temp.get(1);

		rowx.add("<div align=\"center\">" + start + "</div>"); //nomor
		rowx.add("<div align=\"center\">" + bm.getInvoiceNumber() + "</div>"); //invoice
		Vector billDetails = PstMatDispatchBill.getInvoiceDetail(0, 0, "", "", bm.getOID());
		rowx.add(drawInvoicesDetail(language, billDetails));//detail

		String status = PstMatDispatchBill.statusNames[mdb.getStatus()];
		rowx.add("<div align=\"center\">" + status + "</div>"); //status

		String button = "";
		rowx.add(button); //tombol aksi
		
		lstData.add(rowx);
    }
    return ctrlist.draw();
}

public String drawInvoicesDetail(int language, Vector objectClass) {

    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("100%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListOrderItemDetail[language][0],"10%");//no sku
    ctrlist.addHeader(textListOrderItemDetail[language][1],"10%");//item name
    ctrlist.addHeader(textListOrderItemDetail[language][2],"5%");//qty
    ctrlist.addHeader(textListOrderItemDetail[language][3],"10%");//price
    ctrlist.addHeader(textListOrderItemDetail[language][4],"10%");//total price
    
	ctrlist.setLinkRow(1);
	ctrlist.setLinkSufix("");
    Vector lstData = ctrlist.getData();
    ctrlist.reset();

	Double totalqty = 0.0;
	Double totalprice = 0.0;
	Vector rowx = new Vector(1,1);
    for(int i=0; i<objectClass.size(); i++) {
		rowx = new Vector(1,1);
		Billdetail bd = (Billdetail) objectClass.get(i);

		rowx.add("<div align=\"center\">" + bd.getSku() + "</div>"); //sku
		rowx.add("<div align=\"center\">" + bd.getItemName() + "</div>"); //item name
		rowx.add("<div align=\"center\">" + bd.getQty() + "</div>");//qty
		rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(bd.getItemPrice()) + "</div>");//item price
		rowx.add("<div align=\"center\">" + FRMHandler.userFormatStringDecimal(bd.getTotalPrice()) + "</div>"); //total price
		
		totalqty += bd.getQty();
		totalprice += bd.getTotalPrice();

		lstData.add(rowx);
    }
	rowx = new Vector(1,1);
	rowx.add("");
	rowx.add("<div align=\"right\"> Total </div>");
	rowx.add("<div align=\"center\"> " + totalqty + " </div>");
	rowx.add("<div align=\"center\"> - </div>");
	rowx.add("<div align=\"center\"> " + FRMHandler.userFormatStringDecimal(totalprice) + " </div>");
	lstData.add(rowx);
    return ctrlist.draw();
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
String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE_TRANSFER");
String syspropHPP = PstSystemProperty.getValueByName("SHOW_HPP_TRANSFER");
String syspropTotal = PstSystemProperty.getValueByName("SHOW_TOTAL_TRANSFER");


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


String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID]+"="+oidDispatchMaterial;

String orderClauseItem = "";

int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);
int invoiceSize = PstMatDispatchBill.getCount("");

int recordToGetItem = 10;

if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {

    startItem = ctrlMatDispatchItem.actionList(iCommand,startItem,invoiceSize,recordToGetItem);

}

/** kondisi ini untuk manampilakn form tambah item, setelah proses simpan item */

if(iCommand == Command.ADD || (iCommand==Command.SAVE && iErrCode == 0)) {

    iCommand = Command.ADD;

    oidDispatchMaterialItem = 0;

    /** agar form add item tampil pada list paling akhir */

    //startItem = ctrlMatDispatchItem.actionList(Command.LAST,startItem,vectSizeItem,recordToGetItem);

}

String order=" DFI."+PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
if (typeOfBusinessDetail == 2) {
    order = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU]+",7) ASC";
}
Vector listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial, order);

Vector listInvoices = PstMatDispatchBill.getListInvoices(startItem, recordToGetItem, "", "");

if(listInvoices.size()<1 && startItem>0) {

    if(invoiceSize-recordToGetItem > recordToGetItem) {

        startItem = startItem - recordToGetItem;

    } else {

        startItem = 0 ;

        iCommand = Command.FIRST;

        prevCommand = Command.FIRST;

    }

	listInvoices = PstMatDispatchBill.getListInvoices(startItem, recordToGetItem, "", "");

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

    document.frm_bcdispatch.command.value=comm;

    document.frm_bcdispatch.hidden_dispatch_id.value=oid;

    document.frm_bcdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_bcdispatch.submit();

}

function gostock(oid){

document.frm_bcdispatch.command.value="<%=Command.EDIT%>";

document.frm_bcdispatch.hidden_dispatch_item_id.value=oid;

document.frm_bcdispatch.action="df_bccode.jsp";

document.frm_bcdispatch.submit();

}



function cmdAdd()

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value="0";

    document.frm_bcdispatch.command.value="<%=Command.ADD%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    if(compareDateForAdd()==true)

            document.frm_bcdispatch.submit();

}

function cmdAddItemReceive()

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value="0";

    document.frm_bcdispatch.command.value="<%=Command.ADD%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.action="df_bc_material_receive_search.jsp";

    if(compareDateForAdd()==true)

            document.frm_bcdispatch.submit();

}




function cmdEdit(oidDispatchMaterialItem)

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_bcdispatch.command.value="<%=Command.EDIT%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdAsk(oidDispatchMaterialItem)

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_bcdispatch.command.value="<%=Command.ASK%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}

function printForm()

{

	var typePrint = document.frm_bcdispatch.type_print_tranfer.value;

    //window.open("<%=approot%>/servlet/com.dimata.posbo.report.TransferPrintPDF?hidden_dispatch_id=<%=oidDispatchMaterial%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&brandinuse=<%=brandInUse%>");

    window.open("df_bc_wh_material_print_form.jsp?hidden_dispatch_id=<%=oidDispatchMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"&timemls=<%=timemls%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");

}

function cmdSave(){

    var avblQty = parseFloat(document.frm_bcdispatch.avbl_qty.value);

    var transferQty = parseFloat(document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value);



<%!

/** variabel untuk menentukkan apakah stok bernilai 0 (nol) ditampilkan/tidak */

static String showStockNol = PstSystemProperty.getValueByName("SHOW_STOCK_NOL");

static boolean calculateQtyNull =( showStockNol==null || showStockNol.equalsIgnoreCase("Not initialized") || showStockNol.equals("NO") || showStockNol.equalsIgnoreCase("0")) ? true : false;



%>

    if(transferQty<=0.000){

           alert("Quantity < 0");

        } else {

            <% if(calculateQtyNull) { %>

                document.frm_bcdispatch.command.value="<%=Command.SAVE%>";

                document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

                <%if(typeOfBusinessDetail == 2){%>
                    var etalaseAwal = document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_ID]%>.value;
                    var etalaseBaru = document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_GONDOLA_TO_ID]%>.value;
                    document.frm_bcdispatch.submit();
                    
                <%} else {%>
                    document.frm_bcdispatch.submit();
                <%}%>

            <%} else { %>

            if(transferQty <= avblQty) {

                document.frm_bcdispatch.command.value="<%=Command.SAVE%>";

                document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

                document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";
                
                document.frm_bcdispatch.submit();

            }

            else {

                alert("<%=textListGlobal[SESS_LANGUAGE][2]%> ("+avblQty+" "+document.frm_bcdispatch.matUnit.value+")");

            }

            <%}%>

    }

}



function cmdConfirmDelete(oidDispatchMaterialItem)

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_bcdispatch.command.value="<%=Command.DELETE%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.approval_command.value="<%=Command.DELETE%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}

// add by fitra
function cmdNewDelete(oid){
var msg;
msg= "<%=textDelete[SESS_LANGUAGE][0]%>" ;
var agree=confirm(msg);
if (agree)
return cmdConfirmDelete(oid) ;
else
return cmdEdit(oid);
}



function cmdCancel(oidDispatchMaterialItem)

{

    document.frm_bcdispatch.hidden_dispatch_item_id.value=oidDispatchMaterialItem;

    document.frm_bcdispatch.command.value="<%=Command.EDIT%>";

    document.frm_bcdispatch.prev_command.value="<%=prevCommand%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdBack()

{

    document.frm_bcdispatch.command.value="<%=Command.EDIT%>";

    document.frm_bcdispatch.start_item.value = 0;

    document.frm_bcdispatch.action="df_bc_wh_material_edit.jsp";

    document.frm_bcdispatch.submit();

}



var winSrcMaterial;

function cmdCheck() {
		var strvalue  = "dfbcdosearch.jsp?command=<%=Command.FIRST%>"+
			"&location_id=<%=df.getLocationId()%>"+
			"&location_id_penerima=<%=df.getDispatchTo()%>"+
			"&transfer_date=<%=df.getDispatchDate()%>"+
			"&hidden_dispatch_id=<%=oidDispatchMaterial%>"+
			"&mat_code=" +
			"&txt_materialname=";
		winSrcMaterial = window.open(strvalue,"Invoice Search", "height=660,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
		if (window.focus) { 
			winSrcMaterial.focus();
			}
}



function keyDownCheck(e){

    var trap = document.frm_bcdispatch.trap.value;
    if (e.keyCode == 13 && trap==0) {
    document.frm_bcdispatch.trap.value="1";
    }


    // add By fitra
    if (e.keyCode == 13 && trap == "0" && document.frm_bcdispatch.matItem.value == "" ){
        document.frm_bcdispatch.trap.value="0";
        cmdCheck();
   }
    if (e.keyCode == 13 && trap=="1") {
      document.frm_bcdispatch.trap.value="0";
      cmdCheck();
    }
   if (e.keyCode == 27) {
       //alert("sa");
       document.frm_bcdispatch.matCode.focus();
       document.frm_bcdispatch.txt_materialname.value="";
    }


}



function changeFocus(element){

    if(element.name == "matCode") {

        document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();

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
    
    var qty = cleanNumberInt(document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
    var price = cleanNumberFloat(document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP]%>.value,guiDigitGroup,',');
    if(qty<0.0000){
          document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.value=0;
          return;
        }
        
    if(price==""){
            price = 0;
    }
    
    if(!(isNaN(qty)) && (qty != '0'))
    {
        var amount = price * qty;
        if(isNaN(amount)){
            amount = "0";
        }
        document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_HPP_TOTAL]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);
    }else{
        document.frm_bcdispatch.<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_QTY]%>.focus();
    }

    if (evt.keyCode == 13) {
        changeFocus(element);
    }
    
}



function cmdListFirst()

{

    document.frm_bcdispatch.command.value="<%=Command.FIRST%>";

    document.frm_bcdispatch.prev_command.value="<%=Command.FIRST%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdListPrev()

{

    document.frm_bcdispatch.command.value="<%=Command.PREV%>";

    document.frm_bcdispatch.prev_command.value="<%=Command.PREV%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdListNext()

{

    document.frm_bcdispatch.command.value="<%=Command.NEXT%>";

    document.frm_bcdispatch.prev_command.value="<%=Command.NEXT%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdListLast()

{

    document.frm_bcdispatch.command.value="<%=Command.LAST%>";

    document.frm_bcdispatch.prev_command.value="<%=Command.LAST%>";

    document.frm_bcdispatch.action="df_bc_wh_material_item.jsp";

    document.frm_bcdispatch.submit();

}



function cmdBackList()

{

    document.frm_bcdispatch.command.value="<%=Command.FIRST%>";

    document.frm_bcdispatch.action="df_bc_wh_material_list.jsp";

    document.frm_bcdispatch.submit();

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

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">


<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/plugins/ionicons-2.0.1/css/ionicons.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
<link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css">
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

            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>

        </tr>

        <tr> 

          <td><!-- #BeginEditable "content" -->

            <form name="frm_bcdispatch" method ="post" action="">

              <input type="hidden" name="command" value="<%=iCommand%>">

              <input type="hidden" name="prev_command" value="<%=prevCommand%>">

              <input type="hidden" name="start_item" value="<%=startItem%>">

              <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveId%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterialItem%>">
              <input type="hidden" name="hidden_dispatch_id"value="<%=oidDispatchMaterial%>">
              <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">
              <input type="hidden" name="<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">

              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="trap" value="">

              <table width="100%" cellpadding="1" cellspacing="0">

                <tr align="center">

                  <td colspan="3">

                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">

                      <tr>

                        <td width="7%"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                        <td width="20%">: <b>

                          <%

                          if(df.getDispatchCode()!="" && iErrCode==0) {

                                out.println(df.getDispatchCode());

                          } else {

                                out.println("");

                          }

                          %>

                        </b></td>

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>

                        <td width="20%">:

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

                        <td width="10%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                        <td width="1%">:</td>
                        <td width="20%" rowspan="1" align="" valign="top"><textarea name="textarea" class="formElemen" wrap="VIRTUAL" rows="3"><%=df.getRemark()%></textarea></td>

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

                        <%if(typeOfBusinessDetail == 2){%>
                        <td>Tipe Item Transfer</td>
                        <td>:</td>
                        <td>
                            <%
                                Vector val_itemType = new Vector(1,1);
                                Vector key_itemType = new Vector(1,1);
                                for (int main_material = 0; main_material < Material.MATERIAL_TYPE_TITLE.length; main_material++) {
                                    if (main_material == Material.MATERIAL_TYPE_GENERAL) {continue;}
                                    key_itemType.add("" + Material.MATERIAL_TYPE_TITLE[main_material]);
                                    val_itemType.add("" + main_material);
                                }
                            %>
                            <%=ControlCombo.draw(FrmMatDispatch.fieldNames[FrmMatDispatch.FRM_FIELD_DISPATCH_ITEM_TYPE],null,""+df.getDispatchItemType(),val_itemType,key_itemType,"disabled","formElemen")%>
                        </td>
                        <%}%>

                      </tr>

					  <tr>

                        <td><%= textListOrderHeader[SESS_LANGUAGE][9] %></td>

                        <td>: 
							E01
						</td>

                        <td>&nbsp;</td>

                        <td>&nbsp;</td>

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
						  <td colspan="100%" height="50">
							  <a href="javascript:cmdCheck()" class="btn btn-primary">Tambah Baru</a>
						  </td>

				      </tr>
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
									out.println(drawListInvoices(SESS_LANGUAGE, listInvoices,startItem));
								%>
                              </td>
								<%
									} catch(Exception e) {
										System.out.println(e);
									}
								%>
                            </tr>
							<tr>
								<td>&nbsp;</td>									
							</tr>
                            <tr align="left" valign="center">
								<td height="20" width="10%" align="left" class="command">
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
											out.println(ctrLine.drawImageListLimit(cmd, invoiceSize, startItem, recordToGetItem));
										%>
									</span> 
								</td>
								<td valign="top">
									<div class="row">
										<div class="col-md-12">
											<a id="printExcelReportBtn" class="btn btn-primary">
												<i class="fa fa-print"></i>
												<span>
													<% out.print(textOrderList[SESS_LANGUAGE][0] + " " + textOrderList[SESS_LANGUAGE][1]);%>									
												</span>
											</a>
											<a id="printReportBtn" class="btn btn-warning">
												<i class="fa fa-file"></i>
												<span>
													<% out.print(textOrderList[SESS_LANGUAGE][0] + " " + textOrderList[SESS_LANGUAGE][2]);%>									
												</span>
											</a>
										</div>
									</div>
								</td>
                            </tr>

                            <tr align="left" valign="top">
                              <td height="22" colspan="3" valign="middle" class="errfont">
								  <%
									  for (int k = 0; k < listError.size(); k++) {
										  if (k == 0) {
											  out.println(listError.get(k) + "<br>");
										  } else {
											  out.println("&nbsp;&nbsp;&nbsp;" + listError.get(k) + "<br>");
										  }
									  }
								  %>
							  </td>
							  
                            </tr>
                             
                          </table>

                        </td>

                      </tr>

                      <tr>

						  <td>
						  </td>

                      </tr>

                    </table>

                  </td>

                </tr>
                
              </table>

            
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
		
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/plugins/jQueryUI/jquery-ui.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
<script src="../../../styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>
<!--<script src="js/CustomFunction.js"></script>-->
<script>$.widget.bridge('uibutton', $.ui.button);</script>

<script>
	$(function(){
		console.log("Dokumen is redi bois");
		
		$('#printReportBtn').click(function(){
			console.log("Print ke PDF BOIS");
			window.open("<%=approot%>/TransferBCPrintPDF?hidden_material_order_id=648519915973081668&approot=/dProchain_20161124&sess_language=0&/GBOB-1909-PO-015&showprintprice=0");
		});
	});									
							
</script>

</body>
</html>



