<%-- 
    Document   : prmaterial_edit
    Created on : Feb 5, 2014, 10:25:29 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseOrderItem"%>
<%@page import="com.dimata.posbo.session.purchasing.SessFormatEmailQueenTandoor"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page import="org.apache.poi.hssf.record.SaveRecalcRecord"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseRequestItem"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequest"%>
<%@page import="com.dimata.posbo.form.purchasing.CtrlPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseRequestItem"%>
<%@page import="com.dimata.posbo.form.purchasing.FrmPurchaseRequest"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstDistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
                   com.dimata.printman.RemotePrintMan,
                   com.dimata.printman.DSJ_PrintObj,
                   com.dimata.printman.PrnConfig,
                   com.dimata.printman.PrinterHost,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->

<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_REQUEST);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApproval= userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
%>
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"No","Lokasi","Tanggal","Supplier","Contact","Alamat","Telp.","Terms","Days","Ppn","Ket.","Mata Uang","Gudang","Request Barang","Nomor Revisi","Status","Include","%","Term Of Payment","Rate"},
    {"No","Location","Date","Supplier","Contact","Addres","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Request","Revisi Number","Status","Include","%","Term Of Payment","Rate"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Qty Request","Unit Stok","Hrg Beli Terakhir","Hrg Beli","Diskon Terakhir %",//7
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Qty Terima","Stok","Minimum Stok","Approval","Keterangan","Hapus","Term"},//18
    {"No","Code","Name","Qty Request","Unit Stok","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Qty Receive","Stok","Minimum Stok","Approval",
             "Keterangan","Delete","Term"}
};
/**
* this method used to list all po item
*/

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

public String drawListRequestItem(int language,Vector objectClass,int start,boolean privManageData, long oidPurchaseOrder, int useBorder, String approot, String useForRaditya) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(useBorder);
        ctrlist.addHeader(textListOrderItem[language][0],"3%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"15%");
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        //ctrlist.addHeader(textListOrderItem[language][13],"5%");
        ctrlist.addHeader("Unit Request","3%");
        ctrlist.addHeader("Qty Stock","3%");
        ctrlist.addHeader(textListOrderItem[language][4],"3%");
        ctrlist.addHeader(textListOrderItem[language][16],"3%");
        ctrlist.addHeader(textListOrderItem[language][17],"3%");
        ctrlist.addHeader(textListOrderItem[language][19],"3%");
        ctrlist.addHeader(textListOrderItem[language][18],"3%");
        
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0) {
            start=0;
        }

        for(int i=0; i<objectClass.size(); i++) {
            Vector temp = (Vector)objectClass.get(i);
            PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            
            Unit unitStock = new Unit();
            try{
            unitStock=PstUnit.fetchExc(poItem.getUnitRequestId());
                       }catch(Exception ex){}
            
            rowx = new Vector();
            start = start + 1;

            rowx.add(""+start+"");
            if(privManageData){
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(poItem.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(mat.getSku());
            }

            rowx.add(mat.getName());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            
            rowx.add(unitStock.getCode());
            double valueKonfersi =  PstUnit.getQtyPerBaseUnit(poItem.getUnitRequestId(),poItem.getUnitId());
            rowx.add(""+valueKonfersi*poItem.getQuantity());
            //adding qty rec in po by mirahu 20120427
            //double qtyRec = 0.0;
            //qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseRequest,poItem.getMaterialId());
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyRec)+"</div>");
            // unit
            rowx.add(unit.getCode());
            rowx.add(PstPurchaseOrder.fieldsApprovalType[poItem.getApprovalStatus()]);
            rowx.add(poItem.getNote());
        if(useForRaditya.equals("1")){
            rowx.add(PstPurchaseOrder.purchaseRequestType[poItem.getTermPurchaseRequest()]);
        }else{
            rowx.add(PstPurchaseOrder.fieldsPurchaseRequestType[poItem.getTermPurchaseRequest()]);
        }
            // add by fitra 17-05-2014
            if(privManageData){
                 rowx.add(" <div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
                 rowx.add("");
            }
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item order ...</div>";
    }
    return result;
}


public String drawListRequestItemEmail(int language,Vector objectClass,int start,boolean privManageData, long oidPurchaseOrder, int useBorder) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(useBorder);
        ctrlist.addHeader(textListOrderItem[language][0],"3%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"15%");
        ctrlist.addHeader(textListOrderItem[language][14],"3%");
        ctrlist.addHeader(textListOrderItem[language][15],"3%");
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        ctrlist.addHeader(textListOrderItem[language][4],"3%");
        
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0) {
            start=0;
        }

        for(int i=0; i<objectClass.size(); i++) {
            Vector temp = (Vector)objectClass.get(i);
            PurchaseRequestItem poItem = (PurchaseRequestItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            //MatCurrency matCurrency = (MatCurrency)temp.get(3);
            rowx = new Vector();
            
            start = start + 1;
            rowx.add(""+start+"");
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            // price
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getCurrentStock())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getMinimStock())+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            rowx.add(unit.getCode());
            
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada item order ...</div>";
    }
    return result;
}


%>


<!-- Jsp Block -->
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
int prevCommand = FRMQueryString.requestInt(request,"prev_command");
int startItem = FRMQueryString.requestInt(request,"start_item");
int cmdItem = FRMQueryString.requestInt(request,"command_item");
int appCommand = FRMQueryString.requestInt(request,"approval_command");
long oidPurchaseRequest = FRMQueryString.requestLong(request, "hidden_material_request_id");
String textHistory = FRMQueryString.requestString(request, "text_history");
int type = FRMQueryString.requestInt(request, "str_type");
int poUseDirectPrinting = 0;
int includePrintPrice = FRMQueryString.requestInt(request, "includePrinPrice");


// for printing document
if(iCommand==Command.PRINT){
    System.out.println(" JSP 0 ");
    String hostIpIdx ="";
    try{
        String command = request.getParameter("command");
        hostIpIdx = request.getParameter("printeridx");
        System.out.println("Print on "+hostIpIdx + " Command = "+ iCommand);
        if(hostIpIdx!=null){
            PrinterHost prnHost = RemotePrintMan.getPrinterHost(hostIpIdx,";");
            PrnConfig prn = RemotePrintMan.getPrinterConfig(hostIpIdx,";");
            DSJ_PrintObj obj = InternalExternalPrinting.printForm(oidPurchaseRequest,type,companyAddress); //TestPrn.getTestObj();// get object to print !
            obj.setPrnIndex(prn.getPrnIndex());
            RemotePrintMan.printObj(prnHost,obj );
        }
    } catch (Exception exc3){
        System.out.println("Print Exc "+exc3);
    }

    iCommand=Command.EDIT;
}
/**
* initialization of some identifier
*/
String errMsg = "";
String getTmpHistory;
int iErrCode = FRMMessage.ERR_NONE;

/**
* purchasing pr code and title
*/
String poCode = ""; // i_pstDocType.getDocCode(docType);
String poTitle = "PR"; //i_pstDocType.getDocTitle(docType);
String poDocTitle = textListOrderHeader[SESS_LANGUAGE][13]; //i_pstDocType.getDocTitle(docType);
String poItemTitle = poTitle + " Item";
String poTitleBlank = "";


/**
* purchasing pr code and title
*/
String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_PRR));

/**
* action process
*/
LogSysHistory logHistory = new LogSysHistory();

ControlLine ctrLine = new ControlLine();
CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
iErrCode = ctrlPurchaseRequest.action(iCommand , oidPurchaseRequest,userName, userId);
FrmPurchaseRequest frmpo = ctrlPurchaseRequest.getForm();
PurchaseRequest po = ctrlPurchaseRequest.getPurchaseRequest();
errMsg = ctrlPurchaseRequest.getMessage();

/**
* if iCommand = Commmand.ADD ---> Set default rate which value taken from PstCurrencyRate
*/
//double curr = PstCurrencyRate.getLastCurrency();
if(iCommand==Command.ADD) {
    //po.setVendorExchangeRate(curr);
}

/**
* generate code of current currency
*/
String priceCode = "Rp.";
/*if(po.getPriceCurrency()==PstCurrencyRate.RATE_CODE_USD)
{
	priceCode = "US$.";
}*/

/**
* check if document already closed or not
*/
boolean documentClosed = false;
if(po.getPrStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT && po.getPrStatus()!=I_DocStatus.DOCUMENT_STATUS_FINAL && po.getPrStatus()!=I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED) {
    documentClosed = true;
}

/**
 * check if document may modified or not
 */
boolean privManageData = true;
if(po.getPrStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}

String enableInput="";
if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED||po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
    enableInput="enable";
}

/**
 * list purchase order item
 */
//textHistory = logHistory.getHistory();
oidPurchaseRequest = po.getOID();
int recordToGetItem = 10;
int recordToGetItemEmail = 0;
String whereClauseItem = PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;
String orderClauseItem = "";
int vectSizeItem = PstPurchaseRequestItem.getCount(whereClauseItem);
whereClauseItem = "POI."+PstPurchaseRequestItem.fieldNames[PstPurchaseRequestItem.FLD_PURCHASE_REQUEST_ID]+"="+oidPurchaseRequest;
Vector listPurchaseRequestItem = new Vector();

listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem,recordToGetItemEmail,whereClauseItem);


//sent email if document to be approve
String contentEmailItem = "";
int sentNotif =Integer.parseInt(PstSystemProperty.getValueByName("POS_EMAIL_NOTIFICATION"));
String toEmail =PstSystemProperty.getValueByName("POS_EMAIL_TO");
String urlOnline =PstSystemProperty.getValueByName("POS_URL_ONLINE");
String hasilEmail="";
if(sentNotif==1){
    if(po.getPrStatus()== I_DocStatus.DOCUMENT_STATUS_FINAL && iCommand==Command.SAVE && iErrCode==FRMMessage.ERR_NONE){
        Vector listPurchaseRequestItemEmail = new Vector();
        listPurchaseRequestItemEmail = PstPurchaseRequestItem.listWithStokNMinStock(startItem,recordToGetItemEmail,whereClauseItem,po.getLocationId());
        Location lokasiPO = new Location();
        try{
            lokasiPO = PstLocation.fetchExc(po.getLocationId());
        }catch(Exception ex){
        }
        String AksesOnsite ="Akses Onsite Aplication :&nbsp; <a href=\"http://localhost:8080/"+approot+"/login.jsp?nodocument="+po.getOID()+"&typeView=1&deviceuse=1\">Onsite Aplication</a><br>";
        String AksesOnline ="Akses Online Aplication :&nbsp; <a href=\""+urlOnline+"/login.jsp?nodocument="+po.getOID()+"&typeView=2&deviceuse=1\">Online Aplication</a>";
        contentEmailItem= SessFormatEmailQueenTandoor.getContentEmailPR(listPurchaseRequestItemEmail, po, AksesOnsite,AksesOnline,lokasiPO);
        SessEmail sessEmail = new SessEmail();
        hasilEmail = sessEmail.sendEamil(toEmail, "Purchase Request Approval - "+lokasiPO.getName()+" - "+po.getPrCode(), contentEmailItem, "");
    }
}

listPurchaseRequestItem = PstPurchaseRequestItem.list(startItem,recordToGetItem,whereClauseItem);

double defaultPpn = 0;
defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
// add by fitra 17-05-2014
if(iCommand==Command.DELETE && iErrCode==0){
%>
	<jsp:forward page="srcprmaterial.jsp">
	<jsp:param name="command" value="<%=Command.FIRST%>"/>
	</jsp:forward>
<%
}
%>

<!-- FUNGSI UNTUK MENAMPILKAN HISTORY DATA PURCHASE REQUEST -->
<%
String msgString = "";
long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
Vector listDistributionPO = new Vector(1, 1);
//Vector listGetLastData = new Vector(1, 1);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 0;
//String whereClausePurchaseRequest =" dpo."+PstDistributionPurchaseRequest.fieldNames[PstDistributionPurchaseRequest.FLD_PURCHASE_REQUEST_ID] + "="+oidPurchaseRequest;
//listDistributionPO= PstDistributionPurchaseRequest.getListWithLocationName(0, 0, whereClausePurchaseRequest, "");
%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

function cmdAdd(){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.target = "";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.VIEW%>";
    //document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_material_request_id.value=0;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function cmdEdit(oid){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.EDIT%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function compare(){
    var dt = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_dy.value;
    var mn = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_mn.value;
    var yy = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE]%>_yr.value;
    var dt = new Date(yy,mn-1,dt);
    var bool = new Boolean(compareDate(dt));
    return bool;
}

function cmdSave(){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.SAVE%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    if(compare()==true)
        document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}



function keyDownCheck(e){
   if (e.keyCode == 13) {
        cmdCheck();
   }
}

function cmdAsk(oid){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.ASK%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}


function cmdDelete(oid){

   document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.DELETE%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.approval_command.value="<%=Command.DELETE%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
        
}


function cmdCancel(){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.CANCEL%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function cmdConfirmDelete(oid){

    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.DELETE%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_mat_request_item_id.value=oid;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.approval_command.value="<%=Command.DELETE%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterialitem.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
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

function cmdBack(){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.FIRST%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="search_purchase_material.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function printDirectForm()
{
	document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.PRINT%>";
	document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value="<%=prevCommand%>";
	document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterial_edit.jsp";
	document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function changeCurrency(value){
    var oidCurrencyId=value;
    checkAjax(oidCurrencyId);
}

function checkAjax(oidCurrencyId){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE="+oidCurrencyId+"&typeCheckCurrency=1",
    type : "POST",
    async : false,
    success : function(data) {
         document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_EXCHANGE_RATE]%>.value=data;
         //document.frm_purchaseorder.exchangeRate.value=data;
    }
});
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderPrintPDF?hidden_material_request_id=<%=oidPurchaseRequest%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPrCode()%>");
}

function printOutForm(){
    var include =0;
    var includeprint = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderNoShippingPrintPDF?hidden_material_request_id=<%=oidPurchaseRequest%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPrCode()%>&showprintprice="+include);
}

function printFormHtml() {
    var include =0;
    var includeprint = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("pr_material_print_form.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice="+include,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}



function printResentEmail() {
    var include =0;
    var includeprint = document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("pr_resent_email.jsp?hidden_material_request_id=<%=oidPurchaseRequest%>&command=<%=Command.EDIT%>&showprintprice="+include,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}




function klik(){
    var bool = new Boolean();
    <%
    if(listPurchaseRequestItem.size()>0){
        %>
        alert("Supplier tidak bisa di ubah\nKarena sudah ada data item");
    bool = true;
    <%}else{%>
    bool = false;
    <%}%>
    return bool;
}


//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem(){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.ADD%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterialitem.jsp";
    if(compareDateForAdd()==true)
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function editItem(oid){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value="<%=Command.EDIT%>";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.hidden_mat_request_item_id.value=oid;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>.value=oid;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterialitem.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}





function itemList(comm){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value=comm;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.prev_command.value=comm;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="prmaterialitem.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
}

function changeStatus() {
    var xxx =document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS]%>.selectedIndex;
    if (xxx == '2'){
        if (confirm('Yakin menyimpan Purchase Request?')) {
            // Save it!
            cmdSave();
        } else {
            // Do nothing!
        }
    }
    /*
      var bnd = document.form1.band.value
      bnd = bnd.toUpperCase()
      if (bnd == "OASIS") {
           document.form1.songs[4].selected = "1"
      }*/
 }
 
//------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
function paymentList(comm){
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.command.value=comm;
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.action="ordermaterialpayment.jsp";
    document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.submit();
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

function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidPurchaseRequest%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function viewDistributionLocation() {
    var strvalue ="po_distribution_location.jsp?command=<%=Command.ADD%>"+
                     "&hidden_oidPurchaseRequest=<%=oidPurchaseRequest%>";
    window.open(strvalue,"material", "height=250,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
            &nbsp;<%=textListOrderHeader[SESS_LANGUAGE][12]%> &gt; <%=textListOrderHeader[SESS_LANGUAGE][13]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="prev_command" value="<%=prevCommand%>">
              <input type="hidden" name="start_item" value="<%=startItem%>">
              <input type="hidden" name="command_item" value="<%=cmdItem%>">
              <input type="hidden" name="approval_command" value="<%=appCommand%>">
              <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
              <input type="hidden" name="hidden_mat_request_item_id" value="">
              <input type="hidden" name="hidden_order_deliver_sch_id" value="">
              <input type="hidden" name="includePrinPrice" value="">
              <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_CODE]%>" value="<%=po.getPrCode()%>">
              <input type="hidden" name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REQUEST_SOURCE]%>" value="<%=PurchaseRequest.TYPE_SOURCE_PURCHASE %>">
              <input type="hidden" name="<%=FrmPurchaseOrderItem.fieldNames[FrmPurchaseOrderItem.FRM_FIELD_MATERIAL_ID]%>" value="">
              <table width="100%" border="0">
                <tr valign="top">
                  <td colspan="3">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td width="30%" align="left" valign="top">&nbsp;</td>
                        <td align="center" valign="bottom" width="40%">&nbsp;</td>
                        <td width="30%" align="right"></td>
                      </tr>
                      <tr>
                        <td align="left" valign="top" width="30%">&nbsp;</td>
                        <td align="center" width="40%">&nbsp;</td>
                        <td align="right" class="comment" width="30%"></td>
                      </tr>
                      <tr>
                        <td align="left" valign="top" width="37%">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                            <tr>
                              <td width="28%">
                              <%
                                out.println(poCode+" "+textListOrderHeader[SESS_LANGUAGE][0]);
                              %>
                              </td>
                              <td width="3%">:</td>
                              <td width="69%"><%=(po.getPrCode().length()==0 ? "<b>- Otomatis -</b>" : "<b>"+po.getPrCode()+"</b>")%> </td>
                            </tr>
                            <tr>
                              <td width="28%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                              <td width="3%">:</td>
                              <td width="69%">
                                <%
                                  Vector val_locationid = new Vector(1,1);
                                  Vector key_locationid = new Vector(1,1);

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
                                <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td height="20" width="28%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                              <td width="3%">:</td>
                              <td width="69%"><%=ControlDate.drawDateWithStyle(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PURCH_REQUEST_DATE], (po.getPurchRequestDate()==null) ? new Date() : po.getPurchRequestDate(), 0, -1, "formElemen", "")%></td>
                            </tr>
                            <tr>
                              <td width="20%"><%=textListOrderHeader[SESS_LANGUAGE][15]%></td>
                              <td width="3%">:</td>
                              <td width="69%">
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
                                    <%=ControlCombo.draw(FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_PR_STATUS],null,select_status,val_status,key_status,"onChange=\"changeStatus()\" id=\"statusName\"","formElemen")%> </td>
                                    <%
                                        }catch(Exception ex){
                                            System.out.print("xxxx "+ex);
                                      }
                                     } %>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="38%" align="center" valign="top">
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
                              <%=textListOrderHeader[SESS_LANGUAGE][19]%>&nbsp;&nbsp;
                               <input name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_EXCHANGE_RATE]%>" type="text" class="formElemen" size="10" value="<%=po.getExhangeRate()!=0?po.getExhangeRate():resultKonversi%>" <%=enableInput%>>
                              </td>
                            </tr>
                            
                            <tr>
                              <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                              <td width="2%">:</td>
                              <td width="79%">
                                 <textarea name="<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_REMARK]%>" cols="25" rows="4" wrap="VIRTUAL" class="formElemen"><%=po.getRemark()%></textarea>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td align="right" valign="top" class="" width="25%">
                          <table width="100%" border="0" cellspacing="1" cellpadding="1">
                          </table>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr>
                  <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3"><%=drawListRequestItem(SESS_LANGUAGE,listPurchaseRequestItem,startItem,privManageData,oidPurchaseRequest,0,approot, useForRaditya)%></td>
                      </tr>
                      <tr align="left" valign="top">
                          <td height="22" valign="middle" colspan="3"><%= hasilEmail %></td>
                      </tr>
                      <%if(oidPurchaseRequest!=0){%>
                      <tr align="left" valign="top">
                        <td height="8" align="left" colspan="3" class="command">
                          <span class="command">
                          <%
                          if(cmdItem!=Command.FIRST && cmdItem!=Command.PREV && cmdItem!=Command.NEXT && cmdItem!=Command.LAST){
                              cmdItem = Command.FIRST;
                          }
                          ctrLine.setLocationImg(approot+"/images");
                          ctrLine.initDefault();
                          ctrLine.setImageListName(approot+"/images","item");

                          ctrLine.setListFirstCommand("javascript:itemList('"+Command.FIRST+"')");
                          ctrLine.setListNextCommand("javascript:itemList('"+Command.NEXT+"')");
                          ctrLine.setListPrevCommand("javascript:itemList('"+Command.PREV+"')");
                          ctrLine.setListLastCommand("javascript:itemList('"+Command.LAST+"')");
                          %>
                          <%=ctrLine.drawImageListLimit(cmdItem,vectSizeItem,startItem,recordToGetItem)%>
                          </span>
                        </td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" valign="middle" colspan="3">
                        <%
                            if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT ){
                        %>
                          <table width="50%" border="0" cellspacing="2" cellpadding="0">
                            <tr>
                              <td width="94%"><a href="javascript:addItem()" class="btn btn-primary" style="color: white"><%=ctrLine.getCommand(SESS_LANGUAGE,poCode+" Item",ctrLine.CMD_ADD,true)%></a></td>
                            </tr>
                          </table>
                        <%}%>
                        </td>
                      </tr>
                      <%}%>
                    </table>
                  </td>
                </tr>
                      <tr>
                        <td width="55%">
                        <%
                        ctrLine.setLocationImg(approot+"/images");

                        // set image alternative caption
                        ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
                        ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                        ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
                        ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

                        ctrLine.initDefault();
                        ctrLine.setTableWidth("100%");
                        String scomDel = "javascript:cmdAsk('"+oidPurchaseRequest+"')";
                        String sconDelCom = "javascript:cmdDelete('"+oidPurchaseRequest+"')";
                        String scancel = "javascript:cmdEdit('"+oidPurchaseRequest+"')";
                        ctrLine.setCommandStyle("command");
                        ctrLine.setColCommStyle("command");

                        // set command caption
                        ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
                        ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                        ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
                        ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_DELETE,true));
                        ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

                        if(privDelete && privManageData){
                            ctrLine.setConfirmDelCommand(sconDelCom);
                            ctrLine.setDeleteCommand(scomDel);
                            ctrLine.setEditCommand(scancel);
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
                        
                        if(po.getPrStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                            ctrLine.setAddCaption("");
                        }

                        if(iCommand==Command.SAVE && frmpo.errorSize()==0){
                            //iCommand=Command.EDIT;
                        }

                        if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL ) {
                            ctrLine.setDeleteCaption("");
                            ctrLine.setSaveCaption("");
                        }


                        if(documentClosed){
                            ctrLine.setSaveCaption("");
                            ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                            ctrLine.setDeleteCaption("");
                            ctrLine.setConfirmDelCaption("");
                            ctrLine.setCancelCaption("");
                        }
			%>
			<%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
                        </td>
                        <td width="45%">
			 <%if(poUseDirectPrinting==1){%>
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                              <td colspan="2" valign="top">
                              <%
                              Vector valtype = new Vector(1,1);
                              Vector keytype = new Vector(1,1);
                              valtype.add(String.valueOf(0));
                              keytype.add("INTERNAL");
                              valtype.add(String.valueOf(1));
                              keytype.add("EXTERNAL");
                              String selecttype = ""+type;
                              %> <%=ControlCombo.draw("str_type",null,selecttype,valtype,keytype,"","formElemen")%>
                              </td>
                            </tr>
                            <tr>
                              <td width="5%">
                              <%
                              Vector hostLst = null;
                              try{
                                  hostLst = RemotePrintMan.getHostList();
                                  System.out.println(" JSP 1 1");
                              }catch(Exception exc){
                                  System.out.println("HostLst:  "+exc);
                              }
                              %>
                                <select name="printeridx">
                              <%
                              Vector prnLst = null;
                              PrinterHost host = null;
                              if(hostLst!=null){
                                  for(int h = 0; h< hostLst.size();h++){
                                      try{
                                          host = (PrinterHost )hostLst.get(h);
                                          if(host!=null)
                                              prnLst = host.getListOfPrinters(false);//getPrinterListWithStatus(host);
                                          if(prnLst!=null){
                                              for(int i = 0; i< prnLst.size();i++){
                                                  try{
                                                      PrnConfig prnConf= (PrnConfig) prnLst.get(i);
                                                      out.print(" <option value='"+ host.getHostIP()+";"+prnConf.getPrnIndex()+"'> ");
                                                      out.println(prnConf.getPrnName()+" on "+host.getHostName()+" ("+prnConf.getPrnPort()+")");
                                                      out.print("f</option>");
                                                  } catch (Exception exc){out.println("ERROR "+ exc);}
                                              }
                                          }
                                      } catch (Exception exc1){out.println("ERROR" + exc1);}
                                  }
                              }
                              %>
                                </select>
                              </td>
                              <% if(useForRaditya.equals("1")){
                              if(po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED){ %>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printDirectForm()" class="command" >
                                <%if(hostLst.size()>0 && hostLst!=null){%> CETAK PR<%}%></a>
                              </td>
                              <%}else{}}else{%>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printDirectForm()" class="command" >
                                <%if(hostLst.size()>0 && hostLst!=null){%> CETAK PR<%}%></a>
                              </td>
                              <%}%>
                            </tr>
                          <%}else{%>
                             <tr>
                              <td width="95%" nowrap align="left">&nbsp;
                                  <%
                                   includePrintPrice= 0;
                                  %>
                              </td>
                            </tr>
                            <tr>
                              <td width="5%" valign="top" align="right"> 
                                  &nbsp;
                                  
                              <%
                                  if(useForRaditya.equals("0")) {
                                  if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL) {
                              %>
                                <a class="command btn btn-primary" style="color: white" href="javascript:printResentEmail()" >Resent Email</a>
                              <%}
                                }%>
                              </td>
                              <td width="95%" nowrap align="left"> &nbsp;

                              <% if(useForRaditya.equals("1")){
                              if(po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL || po.getPrStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED){ %>
                                <a class="printReport command btn btn-primary" style="color: white" href="#" >Print HTML Mode</a>
                              <%}else{}}else{%>
                                <a class="printReport command btn btn-primary" style="color: white" href="#" >Print HTML Mode</a>
                              <%}%>
                              </td>
                            </tr>
                          </table>
			  <%}%>
                        </td>
                      </tr>
                      <tr>
                          <td>
                            <centre> <a href="javascript:viewHistoryTable()">TABEL HISTORY</a></centre>
                          </td>
                      </tr>
                      <!--==================================================================================================== -->
                    </table>
                  </td>
                </tr>
              </table>
            </form>
           </td>
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
	  <script language="JavaScript">
            changeVendorFisrt();
            document.<%=FrmPurchaseRequest.FRM_NAME_PURCHASE_REQUEST%>.<%=FrmPurchaseRequest.fieldNames[FrmPurchaseRequest.FRM_FIELD_LOCATION_ID]%>.focus();
	  </script>
      <!-- #EndEditable --> 
      
      <script language="JavaScript">
           <%// add by fitra 10-5-2014
          if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.ADD){%>
                  cmdSave();
          <% } %>
          <%
          // add by fitra 10-5-2014
          if(po.getPrStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>
                  addItem();
            <% } %>
      </script>
    </td>
  </tr>
 <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
 <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
 <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
 <script type="text/javascript">
        $(document).ready(function(){
            $('.printReport').click(function(){
                $('#modalReport').modal('show');
            });
        }); 
 </script>
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
</table>
</body>

<!-- #EndTemplate --></html>





