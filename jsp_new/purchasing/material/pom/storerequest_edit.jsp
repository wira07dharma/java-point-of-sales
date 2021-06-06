<%-- 
    Document   : storerequest_edit
    Created on : Aug 3, 2014, 4:41:39 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.session.email.SessEmail"%>
<%@page import="com.dimata.posbo.session.purchasing.SessFormatEmailQueenTandoor"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
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
                   com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
                   com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.location.Location,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<% boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));%>
<%!
/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Number SR","Lokasi","Tanggal Buat","Supplier","Contact","Alamat","Telp.","Terms","Days","Ppn","Ket.","Mata Uang","Gudang","Order Barang","Nomor Revisi","Status","Include","%","Rate","Include Print Harga Beli dan Total","Kategori","Tanggal Pengiriman"},
    {"Number SR","Location","Create Date","Supplier","Contact","Address","Phone","Terms","Days","Ppn","Remark","Currency","Warehouse","Purchase Order","Revisi Number","Status","Include","%","Rate","Include Print Purchase Price and Total","Category","Delivery Date"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
    {"No","Sku","Nama","Qty @UnitStock","Unit","Hrg Beli Terakhir","Hrg Beli","Diskon Terakhir %",//8
     "Diskon1 %","Diskon2 %","Discount Nominal","Netto Hrg Beli","Total","Qty Terima","Hapus","Tidak ada item order ..."},
    {"No","Code","Name","Qty","Unit","Last Cost","Cost","last Discount %","Discount1 %",
     "Discount2 %","Disc. Nominal","Netto Buying Price","Total","Qty Receive","Delete","No purchase data was Found"}
};

public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

/**
* this method used to list all po item
*/
public String drawListOrderItem(int language,Vector objectClass,int start,boolean privManageData, long oidPurchaseOrder, double exchangeRate, String approot) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0],"3%");
        ctrlist.addHeader(textListOrderItem[language][1],"10%");
        ctrlist.addHeader(textListOrderItem[language][2],"15%");
        ctrlist.addHeader("Qty Request","5%");
        ctrlist.addHeader("Unit Request","5%");
        
        ctrlist.addHeader(textListOrderItem[language][13],"5%");
        ctrlist.addHeader(textListOrderItem[language][4],"3%");
        
        ctrlist.addHeader(textListOrderItem[language][14],"8%");
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
            PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            MatCurrency matCurrency = (MatCurrency)temp.get(3);
            Unit unitKonv = new Unit();
            try{
                unitKonv=PstUnit.fetchExc(poItem.getUnitRequestId());
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
            rowx.add("<div align=\"center\">"+poItem.getQuantity()+"</div>");
            
            rowx.add("<div align=\"center\">"+unitKonv.getCode()+"</div>");
            
            double qtyRec = 0.0;
            
            qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseOrder,poItem.getMaterialId());
            
            rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(qtyRec)+"</div>");
            
            // unit
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            
            // harga beli
            if(privManageData){
                rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
                rowx.add("");
            }
            lstData.add(rowx);
        }
        result = ctrlist.drawBootstrap();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListOrderItem[language][15]+"</div>";
    }
    return result;
}


public String drawListOrderBonusItem(int language,Vector objectClass,int start,boolean privManageData, long oidPurchaseOrder, double exchangeRate, String approot) {
    String result = "";
    if(objectClass!=null && objectClass.size()>0) {
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
        ctrlist.addHeader(textListOrderItem[language][3],"5%");
        ctrlist.addHeader(textListOrderItem[language][13],"5%");
        ctrlist.addHeader(textListOrderItem[language][4],"3%");
        ctrlist.addHeader(textListOrderItem[language][5],"8%");
        ctrlist.addHeader(textListOrderItem[language][6],"8%");
        ctrlist.addHeader(textListOrderItem[language][7],"5%");
        ctrlist.addHeader(textListOrderItem[language][8],"5%");
        ctrlist.addHeader(textListOrderItem[language][9],"5%");
        ctrlist.addHeader(textListOrderItem[language][10],"8%");
        ctrlist.addHeader(textListOrderItem[language][11],"8%");
        ctrlist.addHeader(textListOrderItem[language][12],"8%");
        ctrlist.addHeader(textListOrderItem[language][14],"8%");
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
            PurchaseOrderItem poItem = (PurchaseOrderItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            MatCurrency matCurrency = (MatCurrency)temp.get(3);
            Unit unitKonv = new Unit();
            try{
                unitKonv=PstUnit.fetchExc(poItem.getUnitRequestId());
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
            rowx.add(""+poItem.getQtyRequest());
            rowx.add(unitKonv.getCode());
            
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getQuantity())+"</div>");
            //adding qty rec in po by mirahu 20120427
            double qtyRec = 0.0;
            qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseOrder,poItem.getMaterialId());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyRec)+"</div>");
            // unit
            rowx.add(unit.getCode());
            // harga beli
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getPrice()/exchangeRate)+"</div>");
            // harga
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()/exchangeRate)+"</div>");
            // discount
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount()/exchangeRate)+"</div>");
            // discount 1
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount1()/exchangeRate)+"</div>");
            // discount 2
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscount2()/exchangeRate)+"</div>");
            // disc nominal
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()/exchangeRate)+"</div>");
            // curr harga beli
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()/exchangeRate)+"</div>");
            // total
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(poItem.getTotal()/exchangeRate)+"</div>");
            // delete
            // add by fitra 17-05-2014
             if(privManageData){
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(poItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                       }else{
                 rowx.add("Delete");
                       }
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListOrderItem[language][15]+"</div>";
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
long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");
String textHistory = FRMQueryString.requestString(request, "text_history");
int type = FRMQueryString.requestInt(request, "str_type");
int poUseDirectPrinting = 0;
int includePrintPrice = FRMQueryString.requestInt(request, "includePrinPrice");


// for printing document
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
String poTitle = "SR"; //i_pstDocType.getDocTitle(docType);
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
CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
iErrCode = ctrlPurchaseOrder.actionSR(iCommand , oidPurchaseOrder,userName, userId);
FrmPurchaseOrder frmpo = ctrlPurchaseOrder.getForm();
PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();
errMsg = ctrlPurchaseOrder.getMessage();

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
if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED) {
    documentClosed = true;
}

/**
 * check if document may modified or not
 */
boolean privManageData = true;
if(po.getPoStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}
/**
 * list purchase order item
 */
//textHistory = logHistory.getHistory();
oidPurchaseOrder = po.getOID();
int recordToGetItem = 10;

String whereClauseItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                         " AND "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=0";
String orderClauseItem = "";
int vectSizeItem = PstPurchaseOrderItem.getCount(whereClauseItem);
whereClauseItem = "POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                  " AND "+"POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=0";  
Vector listPurchaseOrderItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseItem);

//bonus item
String whereClauseBonusItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                              " AND "+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=1";
int vectSizeBonusItem = PstPurchaseOrderItem.getCount(whereClauseBonusItem);
whereClauseBonusItem = "POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID]+"="+oidPurchaseOrder+
                       " AND "+"POI."+PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS]+"=1";
Vector listPurchaseOrderBonusItem = PstPurchaseOrderItem.list(startItem,recordToGetItem,whereClauseBonusItem);


double defaultPpn = 0;
defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

String enableInput="";
if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED||po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED ||po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL){
    enableInput="readonly";
}

//Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
				 " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
				 " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
				 " != "+PstContactList.DELETE;
Vector vt_supp = PstContactList.listContactByClassTypeWarehouse(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);


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


<!-- FUNGSI UNTUK MENAMPILKAN HISTORY DATA PURCHASE ORDER -->
<%
String msgString = "";
long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
Vector listDistributionPO = new Vector(1, 1);
//Vector listGetLastData = new Vector(1, 1);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 0;
String whereClausePurchaseOrder =" dpo."+PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID] + "="+oidPurchaseOrder;
listDistributionPO= PstDistributionPurchaseOrder.getListWithLocationName(0, 0, whereClausePurchaseOrder, "");
%>
<%!
    public String drawList(Vector objectClass, long oidLog) {

       ControlList ctrlist = new ControlList(); //membuat new class ControlList

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader("No", "10%");
        ctrlist.addHeader("Location", "10%");
        ctrlist.addHeader("Persentase Distribution(%)", "10%");
        ctrlist.setLinkRow(0);

        Vector lstData = ctrlist.getData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset();

        int index = -1;
        int noUrut=0;
        for (int i = 0; i < objectClass.size(); i++) {
            DistributionPurchaseOrder distributionPurchaseOrder = (DistributionPurchaseOrder) objectClass.get(i);
            Vector rowx = new Vector();
            if (oidLog == distributionPurchaseOrder.getOID()) {
                index = i;
            }
            noUrut=noUrut+1;
            rowx.add(""+noUrut);
            rowx.add(""+distributionPurchaseOrder.getLocationName());
            rowx.add(""+distributionPurchaseOrder.getQty());
            lstData.add(rowx);
        }
         return ctrlist.draw(index);
    }
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

function cmdAdd(){
                document.frm_purchaseorder.target = "";
                document.frm_purchaseorder.command.value="<%=Command.VIEW%>";
                //document.frm_purchaseorder.hidden_material_order_id.value=0;
                document.frm_purchaseorder.action="storerequest_edit.jsp";
                document.frm_purchaseorder.submit();
            }

function cmdEdit(oid){
    document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="storerequest_edit.jsp";
    document.frm_purchaseorder.submit();
}






function compare(){
    var dt = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_dy.value;
    var mn = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_mn.value;
    var yy = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_yr.value;
    var dt = new Date(yy,mn-1,dt);
    var bool = new Boolean(compareDate(dt));
    return bool;
}

function cmdSave(){
    document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="storerequest_edit.jsp";
    //if(compare()==true)
        document.frm_purchaseorder.submit();
}

function cmdAsk(oid){
    document.frm_purchaseorder.command.value="<%=Command.ASK%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="storerequest_edit.jsp";
    document.frm_purchaseorder.submit();
}

function cmdCancel(){
    document.frm_purchaseorder.command.value="<%=Command.CANCEL%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="storerequest_edit.jsp";
    document.frm_purchaseorder.submit();
}


function cmdDelete(oid){
    var msg;
    msg= "Apakah Anda yakin menghapus dokumen Store Request ?" ;
    var agree=confirm(msg);
    if (agree){
        document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
        document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
        document.frm_purchaseorder.approval_command.value="<%=Command.DELETE%>";
        document.frm_purchaseorder.action="storerequest_edit.jsp";
        document.frm_purchaseorder.submit();
    }
}

function cmdConfirmDelete(oid){
    document.frm_purchaseorder.command.value="<%=Command.DELETE%>";
    document.frm_purchaseorder.hidden_mat_order_item_id.value=oid;
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


function cmdAlert(){
    document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="storerequest_edit.jsp";
    document.frm_purchaseorder.submit();
}



function cmdBack(){
    document.frm_purchaseorder.command.value="<%=Command.FIRST%>";
    document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
    document.frm_purchaseorder.action="srcstorerequest.jsp";
    document.frm_purchaseorder.submit();
}

function printDirectForm()
{
	document.frm_purchaseorder.command.value="<%=Command.PRINT%>";
	document.frm_purchaseorder.prev_command.value="<%=prevCommand%>";
	document.frm_purchaseorder.action="storerequest_edit.jsp";
	document.frm_purchaseorder.submit();
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
         document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>.value=data;
         //document.frm_purchaseorder.exchangeRate.value=data;
    }
});
}

function changeTermOfService(value){
    var termOfService=value;
    var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
    if(termOfService==1){
      //alert("ini adalah payment kredit")  
      checkAjaxDaysTermOfService(currId);
    }else{
        document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>.value=0;
    }
    //checkAjax(oidCurrencyId);
}

function checkAjaxDaysTermOfService(currId){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.DaysTermOfService?oid_contact="+currId,
    type : "POST",
    async : false,
    success : function(data) {
         document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>.value=data;
    }
});
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderPrintPDF?hidden_material_order_id=<%=oidPurchaseOrder%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPoCode()%>");
}

function printOutForm(){
    var include =0;
    var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderNoShippingPrintPDF?hidden_material_order_id=<%=oidPurchaseOrder%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPoCode()%>&showprintprice="+include+"&typeRequest=1");
}

function printFormHtml() {
    var include =0;
    var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
    if(includeprint){
           include=1;
    }
    window.open("po_material_print_form.jsp?hidden_material_order_id=<%=oidPurchaseOrder%>&command=<%=Command.EDIT%>&showprintprice="+include+"&typeRequest=1","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function klik(){
    var bool = new Boolean();
    <%
    if(listPurchaseOrderItem.size()>0){
        %>
        alert("Supplier tidak bisa di ubah\nKarena sudah ada data item");
    bool = true;
    <%}else{%>
    bool = false;
    <%}%>
    return bool;
}

function changeVendor(){
    //alert(document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>);
    //alert(document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value);
    if(!klik()){
        var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
        switch(currId){
        <%
            if(vt_supp!=null && vt_supp.size()>0){
                for(int i=0; i<vt_supp.size(); i++){
                ContactList contactList = (ContactList)vt_supp.get(i);
        %>
            case "<%=contactList.getOID()%>" :
                    document.frm_purchaseorder.hid_contact.value = "<%=contactList.getPersonName().length()>0 ? contactList.getPersonName() : "-"%>";
                    document.frm_purchaseorder.hid_addres.value = "<%=contactList.getBussAddress().length()>0 ? contactList.getBussAddress() : "-"%>";
                    document.frm_purchaseorder.hid_phone.value =  "<%=contactList.getTelpNr().length()>0 ? contactList.getTelpNr() : "-"%>";
            break;
        <%}}%>
            default :
            break;
        }
    }else{
        document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value = "<%=po.getSupplierId()%>";
    }
}

function changeVendorFisrt(){
    //alert(document.frm_purchaseorder.<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>);
    //alert(document.frm_purchaseorder.<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value);
    var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
    switch(currId){
    <%
        if(vt_supp!=null && vt_supp.size()>0){
            for(int i=0; i<vt_supp.size(); i++){
            ContactList contactList = (ContactList)vt_supp.get(i);
    %>
        case "<%=contactList.getOID()%>" :
            document.frm_purchaseorder.hid_contact.value = "<%=contactList.getPersonName().length()>0 ? contactList.getPersonName() : "-"%>";
            document.frm_purchaseorder.hid_addres.value = "<%=contactList.getBussAddress().length()>0 ? contactList.getBussAddress() : "-"%>";
            document.frm_purchaseorder.hid_phone.value = "<%=contactList.getTelpNr().length()>0 ? contactList.getTelpNr() : "-"%>";
        break;
    <%}}%>
        default :
        break;
    }
}

//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem(){
    document.frm_purchaseorder.command.value="<%=Command.ADD%>";
    var size = document.frm_purchaseorder.signForAdd.value;
    if (size>0){
        document.frm_purchaseorder.action="storerequestitem_old.jsp";
    }else{
        document.frm_purchaseorder.action="storerequestitem.jsp";
    }
    if(compareDateForAdd()==true)
        document.frm_purchaseorder.submit();
}

function editItem(oid){
    document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
    document.frm_purchaseorder.hidden_mat_order_item_id.value=oid;
    document.frm_purchaseorder.action="storerequestitem_old.jsp";
    document.frm_purchaseorder.submit();
}

function itemList(comm){
    document.frm_purchaseorder.command.value=comm;
    document.frm_purchaseorder.prev_command.value=comm;
    document.frm_purchaseorder.action="storerequestitem.jsp";
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
    document.frm_purchaseorder.command.value=comm;
    document.frm_purchaseorder.prev_command.value=comm;
    document.frm_purchaseorder.action="pomaterialdelivery.jsp";
    document.frm_purchaseorder.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
function paymentList(comm){
    document.frm_purchaseorder.command.value=comm;
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

function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidPurchaseOrder%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function viewDistributionLocation() {
    var strvalue ="po_distribution_location.jsp?command=<%=Command.ADD%>"+
                     "&hidden_oidPurchaseOrder=<%=oidPurchaseOrder%>";
    window.open(strvalue,"material", "height=250,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<script src="../../../styles/jquery.min.js"></script>

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
<script>
    $(document).ready(function(){
        var poId = $("#hidden_material_order_id").val();
        //alert(poId);
        var typeDocument = $("#FRM_FIELD_PO_STATUS").val();
        if (typeDocument=="0"){
            if (poId=="0"){
                $("#addItem").hide();
            }else{
                $("#addItem").show();
            }
            
        }else{
            $("#addItem").hide();
        }
        $("#FRM_FIELD_PO_STATUS").change(function(){
            var typeDocument = $("#FRM_FIELD_PO_STATUS").val();
            var poId = $("#hidden_material_order_id").val();
            if (typeDocument=="0"){
                if (poId=="0"){
                    $("#addItem").hide();
                }else{
                    $("#addItem").show();
                }
            }else{
                $("#addItem").hide();
            }
        });
    });
</script>
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
                    <%=strCompany%>
                    <small>Control panel</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-dashboard"></i>Search Store Request</a></li>
                    <li class="active">Edit Store Request</li>
                </ol>
            </section>

            <!-- Main content -->
            <section class="content">
                <div class="row">
                    <div class="col-md-12">
                        <div class="box box-primary">
                            <div class="box-header">
                                <h3 class="box-title">Edit Store Request > <%=(po.getPoCode().length()==0 ? "<b>- Otomatis -</b>" : "<b>"+po.getPoCode()+"</b>")%></h3>
                            </div>
                            <form role="form" name="frm_purchaseorder" method="post" action="">
                                  <input type="hidden" name="command" value="">
                                  <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                  <input type="hidden" name="start_item" value="<%=startItem%>">
                                  <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                  <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                  <input type="hidden" id="hidden_material_order_id" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
                                  <input type="hidden" name="hidden_mat_order_item_id" value="">
                                  <input type="hidden" name="hidden_order_deliver_sch_id" value="">
                                  <input type="hidden" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_CODE]%>" value="<%=po.getPoCode()%>">
                                  <input type="hidden" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                                  <div class="box-body">
                                      <div class="row">
                                            <div class="col-md-4">
                                                 <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label><br>
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
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][21]%></label><br>
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
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][20]%></label><br>
                                                    <%
                                                        Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                        Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                                                    %>
                                                    <%=ControlCombo.drawParentComboBoxBootsratap(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+po.getCategoryId(), null, materGroup)%>
                                                 </div>
                                                 <div class="form-group">
                                                     <div class="form-group">
                                                        <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][15]%></label><br>
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
                                              <input type='hidden' id="signForAdd" name="signForAdd" value="<%= listPurchaseOrderItem.size()%>">
                                              <%=drawListOrderItem(SESS_LANGUAGE,listPurchaseOrderItem,startItem,privManageData,oidPurchaseOrder,po.getExchangeRate(),approot)%>
                                          </div>
                                      </div>
                                      <div class="row">
                                          <br></br>
                                      </div>   
                                      <div class="row">
                                      <%
                                       if(privAdd==true && (po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT || po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)){
                                       %>     
                                       <div class="col-md-12">
                                           <div class="form-group">
                                            <button id="addItem" class="btn btn-success pull-left" onclick="javascript:cmdSave()" type="button" > Add Item </button>  &nbsp;
                                            <button class="btn btn-primary pull-left" style="margin-left: 5px;" onclick="javascript:cmdSave()" type="button" > Save </button>  
                                            <%if(po.getOID()!=0){%>
                                                <button class="btn btn-danger pull-left" style="margin-left: 5px;" onclick="javascript:cmdDelete()" type="button" > Delete </button>  
                                            <%}%>
                                            <button class="btn btn-warning pull-left" style="margin-left: 5px;" onclick="javascript:cmdBack()" type="button" > Back To List </button>
                                           </div>
                                       </div>
                                      <%}
                                      %>
                                      </div>  
                                      <div class="row">
                                          <br></br><br></br>
                                      </div>
                                      <div class="row">
                                        <div class="col-md-12">
                                           <div class="form-group">
                                                <input name="includePrinPrice" type="hidden" value="1">
                                                <button class="btn btn-success pull-right" onclick="javascript:printFormHtml()" type="button" ><i class="fa fa-download"></i> Generate HTML </button>  
                                                <button class="btn btn-primary pull-right" style="margin-right: 5px;" onclick="javascript:printOutForm()" type="button" ><i class="fa fa-download"></i> Generate PDF </button>
                                           </div>
                                        </div>
                                      </div>
                                  </div>
                            </form>
                        </div>
                    </div>
                </div>  
            </section><!-- /.content -->

        </aside><!-- /.right-side -->
    </div><!-- ./wrapper -->
  
<script language="JavaScript">
    changeVendorFisrt();
    document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID]%>.focus();
  </script>
  <script language="JavaScript">   
    <% 
    // add by fitra 10-5-2014
    if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%> 
       addItem();
    <% } %>
 </script>
 <script language="JavaScript">   
    <% 
    // add by fitra 10-5-2014
    if(iCommand==Command.DELETE && iErrCode==0){%> 
        cmdBack()
    <% } %>
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

<!-- #EndTemplate --></html>
