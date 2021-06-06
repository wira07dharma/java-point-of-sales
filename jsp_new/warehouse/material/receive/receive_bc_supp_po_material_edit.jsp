<%@page import="com.dimata.qdep.entity.I_ApplicationType"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.util.Command,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.common.entity.location.Location,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.session.warehouse.SessForwarderInfo,
                   com.dimata.posbo.form.warehouse.FrmForwarderInfo,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privApprovalApprove = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!

static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;

public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang","Cetak Penerimaan Barang","Informasi Forwarder Harus Diisi","Posting Stock","Posting Harga Beli","Cetak Barcode"},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item","Print Goods Receive","Forwarder Information Require","Posting Stock","Posting Cost Price","Print Barcode"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor PO","Nomor Invoice","PPN","Waktu","Mata Uang","Sub Total","Grand Total","Include","%","Terms","Days","Rate", "Kode BC"},//18
    {"Number","Location","Date","Supplier","Status","Remark","PO Number","Supplier Invoice","PPN","Time","Currency","Sub Total","Grand Total","Include","%","Terms","Days","Rate", "Customs Code"}
};

/* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
   // {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Sub Total Cost"}
//};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",//10
    "Diskon1 %","Diskon2 %","Discount Nominal","Hapus","Barcode"},// 14
   {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
    "Discount2 %","Disc. Nominal","Delete","Barcode"}
};

/** this constan used to list text of forwarder information */
public static final String textListForwarderInfo[][] = {
    {"Nomor", "Nama Perusahaan", "Tanggal", "Total Biaya", "Keterangan", "Informasi Forwarder", "Tidak Ada Informasi Forwarder!","Sebelum Status Final Pastikan Informasi Forwarder diisi jika ada !"},
    {"Number", "Company Name", "Date", "Total Cost", "Remark", "Forwarder Information", "No Forwarder Information!","Prior to final status, make sure forwarder information is entered if required !"}
};

public static final String textListDetailPayment[][] = {
    {"Nomor","Rincian Pembayaran","Pembayaran"},
    {"Number","Detail Of Payment","Payment"}
};

public static final String textPosting[][] = {
    {"Anda yakin melakukan Posting Stok ?","Anda yakin melakukan Posting Harga ?"},
    {"Are You Sure to Posting Stock ? ","Are You Sure to Posting Cost Price?"}
};
public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};


/**
* this method used to list all receive item
*/
public Vector drawListRecItem(int language,Vector objectClass,int start,boolean privManageData, boolean privShowQtyPrice, double exchangeRate, int select_status, String approot, String typeOfBusiness){
    
    Vector list = new Vector(1,1);
    Vector listError = new Vector(1,1);
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0],"5%");//No
        ctrlist.addHeader(textListOrderItem[language][1],"10%");//Sku
        if(typeOfBusiness.equals("0")){
             ctrlist.addHeader(textListOrderItem[language][15],"10%");////barcode
        }
        ctrlist.addHeader(textListOrderItem[language][2],"20%");//Nama Barang
        if(bEnableExpiredDate){
            ctrlist.addHeader(textListOrderItem[language][3],"7%");//Kadaluarsa
        }
        ctrlist.addHeader(textListOrderItem[language][4],"5%");//Unit
        if(privShowQtyPrice){
            ctrlist.addHeader(textListOrderItem[language][5],"8%");//Harga Beli
            ctrlist.addHeader(textListOrderItem[language][11],"5%");//Diskon1
            ctrlist.addHeader(textListOrderItem[language][12],"5%");//"Diskon2 %
            ctrlist.addHeader(textListOrderItem[language][13],"8%");//Discount Nominal
            ctrlist.addHeader(textListOrderItem[language][6],"8%");//Ongkos Kirim
        }
        ctrlist.addHeader(textListOrderItem[language][8],"8%");//Qty

        if(privShowQtyPrice){
            ctrlist.addHeader(textListOrderItem[language][9],"10%");//Total Beli
        }
        ctrlist.addHeader(textListOrderItem[language][14],"10%");//Delete Beli

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0)
            start=0;
        
        for(int i=0; i<objectClass.size(); i++){
            Vector temp = (Vector)objectClass.get(i);
            MatReceiveItem recItem = (MatReceiveItem)temp.get(0);
            Material mat = (Material)temp.get(1);
            Unit unit = (Unit)temp.get(2);
            rowx = new Vector();
            start = start + 1;
            double totalForwarderCost = (recItem.getForwarderCost() * recItem.getQty());
            
            rowx.add(""+start+"");
            if((privManageData || privShowQtyPrice) && select_status!=I_DocStatus.DOCUMENT_STATUS_CLOSED){
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(mat.getSku());
            }
            if(typeOfBusiness.equals("0")){
                rowx.add(mat.getBarCode());
            }
            rowx.add(mat.getName());
            if(bEnableExpiredDate){
                rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
            if(privShowQtyPrice){
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
            }
            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);
                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok produk '"+mat.getName()+"' tidak sama dengan qty terima.");
                }
                
               rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+String.valueOf(recItem.getQty())+"</div>");

            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }
            if(privShowQtyPrice){
                 rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</div>");
            }
            // add by fitra 17-05-2014
            if(privManageData && (select_status!=5 || select_status!=2 || select_status!=7 || select_status!=1 || select_status!=2) ){
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
            }else{
                rowx.add("");
            }
            
            
            lstData.add(rowx);
        }
        result = ctrlist.draw();
    }else{
        result = "<div class=\"msginfo\">"+textListGlobal[language][7]+"</div>";
    }
    
    list.add(result);
    list.add(listError);
    return list;
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
int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL,I_DocType.MAT_DOC_TYPE_LMRR);
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
long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
long oidForwarderInfo = FRMQueryString.requestLong(request, "hidden_forwarder_id");
String strPoNumber = FRMQueryString.requestString(request, "txt_ponumber");
int iCommandPosting = FRMQueryString.requestInt(request,"iCommandPosting");
//adding oidpurchaseorder
//by mirahu 01032012

long oidPurchaseOrder = FRMQueryString.requestLong(request,"hidden_purchase_order_id");
double termPayment = FRMQueryString.requestDouble(request, "term_payment");

/**
 * initialization of some identifier
 */
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;

double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

//adding currency id
long oidCurrencyRec = FRMQueryString.requestLong(request, "hidden_currency_id");

/**
 * purchasing ret code and title
 */
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];
String recItemTitle = recTitle + " Item";

/**
 * action process
 */
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);

if(strPoNumber.length()==0 && oidReceiveMaterial==0){
    iCommand = Command.ADD;
}
iErrCode = ctrlMatReceive.action(iCommand , oidReceiveMaterial, userName, userId);

FrmMatReceive frmrec = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();
errMsg = ctrlMatReceive.getMessage();


//proses posting stock
int typeOfBussiness = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS"));
if(typeOfBussiness==I_ApplicationType.APPLICATION_DISTRIBUTIONS){
   SessPosting sessPosting = new SessPosting();
   switch (iCommandPosting) {
        case Command.POSTING:
                if(!privApprovalFinal){
                    boolean isOKP =  sessPosting.postedQtyReceiveOnlyDoc(oidReceiveMaterial);
                    if(isOKP){
                       rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_APPROVED);     
                    }
                }
            break;
         case Command.REPOSTING:
                boolean isOK = sessPosting.postedValueReceiveOnlyDoc(oidReceiveMaterial);
                if(isOK){
                   rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);     
                }
            break;

        default:
           // break;
   }
}else{
    //type kecuali retail, klo final langsung posting
    if(iCommandPosting==Command.POSTING){
          SessPosting sessPosting = new SessPosting();
          boolean isOK = sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
          if(isOK){
                   rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);     
          }
    }
}


//Fetch Purchase Order Info
PurchaseOrder po = new PurchaseOrder();
if(rec!=null && rec.getOID()!=0){
try {
    po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
    strPoNumber = po.getPoCode();
} catch(Exception e) {
}
}

if(strPoNumber.length()==0 || iErrCode!=0){
    frmrec.addError(1,"Nomor Order tidak boleh kosong");
}

/**
 * generate code of current currency
 */
String priceCode = "Rp.";
Vector listError = new Vector();
Vector listErrorBonus = new Vector();

/**
 * list receive item
 */
oidReceiveMaterial = rec.getOID();
oidPurchaseOrder = rec.getPurchaseOrderId();
int recordToGetItem = 10;
String whereClauseItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                          " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=0";
int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
Vector listMatReceiveItem = new Vector();
if(rec!=null && rec.getOID()!=0){
    listMatReceiveItem=PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
}


String whereClauseBonusItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial+
                              " AND "+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS]+"=1";
Vector listMatReceiveBonusItem = new Vector();
if(rec!=null && rec.getOID()!=0){
    listMatReceiveBonusItem=PstMatReceiveItem.list(startItem,0,whereClauseBonusItem);
}


/** get forwarder info */
Vector vctForwarderInfo = new Vector(1,1);
if(rec!=null && rec.getOID()!=0){
try {
    if(oidReceiveMaterial != 0) {
        vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceiveMaterial);
    }
} catch(Exception e) {
}
}

/** get total biaya forwarder */
double totalForwarderCost = (SessForwarderInfo.getTotalCost(oidReceiveMaterial));

/**
 * check if document may modified or not
 */
boolean privManageData = true;
if(rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}
Vector list = drawListRecItem(SESS_LANGUAGE,listMatReceiveItem,startItem,privManageData,privShowQtyPrice,po.getExchangeRate(),rec.getReceiveStatus(), approot,typeOfBusiness);
listError = (Vector)list.get(1);



Vector listBonus = drawListRecItem(SESS_LANGUAGE,listMatReceiveBonusItem,startItem,privManageData,privShowQtyPrice,po.getExchangeRate(),rec.getReceiveStatus(), approot,typeOfBusiness);
listErrorBonus = (Vector)listBonus.get(1);

// add by fitra 17-05-2014
if(iCommand==Command.DELETE && iErrCode==0) {
%>
    <jsp:forward page="src_receive_material.jsp">
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
    </jsp:forward>
<%
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->  
<title>Dimata - ProChain POS</title
><!-- #EndEditable -->
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
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}


function compare(){
    var dt = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
    var mn = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
    var yy = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
    var dt = new Date(yy,mn-1,dt);
    var bool = new Boolean(compareDate(dt));
    return bool;
}

function cmdSave() {
    <%
    if((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)){
    %>
        var kodePo = document.frm_recmaterial.txt_ponumber.value;
        var notaSupp = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
        if(kodePo!='' && notaSupp!=''){
            var statusDoc = document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>.value;
            
            <%if(typeOfBusiness.equals("3")){%>
                if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" && <%=privApprovalFinal%>==false){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                     if(conf){
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.iCommandPosting.value="<%=Command.POSTING%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                     if(conf){
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.iCommandPosting.value="<%=Command.REPOSTING%>";
                        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else{
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                }   
            <% }else {%>
                if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                     if(conf){
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.iCommandPosting.value="<%=Command.POSTING%>";
                        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else{
                    document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                    document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                    if(compare()==true)
                                document.frm_recmaterial.submit();
                }
            <%}%>
        }else{
            alert("Kode PO dan Nota Supplier tidak boleh kosong");
        }
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function cmdAsk(oid){
    <%
    if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
    %>
        document.frm_recmaterial.command.value="<%=Command.ASK%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
        alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function cmdCancel(){
    document.frm_recmaterial.command.value="<%=Command.CANCEL%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oid){
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
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

function cmdDelete(oid){
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function cmdBack(){
    <% if(oidReceiveMaterial != 0 && vctForwarderInfo.size() == 0 && totalForwarderCost > 0) { %>
        alert("<%=textListGlobal[SESS_LANGUAGE][9]%>");
            
    <% } else { %>
        document.frm_recmaterial.command.value="<%=Command.FIRST%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="src_receive_material.jsp";
        document.frm_recmaterial.submit();
    <% } %>
}

function cmdSelectPO(){
    var mydate = new Date();
    var strvalue  = "podosearch.jsp?command=<%=Command.FIRST%>"+
                    "&oidVendor="+document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>.value+
                    "&po_code="+document.frm_recmaterial.txt_ponumber.value;
    window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function printForm(){
    var typePrint = document.frm_recmaterial.type_print_tranfer.value;
    window.open("receive_wh_supp_po_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printBarcode(){
    var con = confirm("Print Barcode ? ");
    if (con ==true)
    {
    window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcode?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    }
}

function addForwarderInfo() {
    <%
    if ((rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.action="forwarder_info_po.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function editForwarderInfo(){
    <%
    if ((rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.action="forwarder_info_po.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
            alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function fnTrapKD(e){
   if (e.keyCode == 13) {
        //document.all.aSearch.focus();
        cmdSelectPO();
   }
}

function addReceivePayable(oid) {
    
        document.frm_recmaterial.command.value="<%=Command.ADD%>";
        document.frm_recmaterial.oid_invoice_selected.value=oid;
        document.frm_recmaterial.action="../../../arap/payable/payable_view.jsp";
        document.frm_recmaterial.submit();
}


//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function addItem() {
	<%
	if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
	%>
		document.frm_recmaterial.command.value="<%=Command.ADD%>";
		document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
		if(compareDateForAdd()==true)
			document.frm_recmaterial.submit();
	<%
	}
	else {
	%>
		alert("Document has been posted !!!");
	<%
	}
	%>
}

function addAllItem() {
	<%
	if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) {
	%>
		document.frm_recmaterial.command.value="<%=Command.ADDALL%>";
		document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
		if(compareDateForAdd()==true)
			document.frm_recmaterial.submit();
	<%
	}
	else {
	%>
		alert("Document has been posted !!!");
	<%
	}
	%>
}

function editItem(oid) {
    <%
    if ((rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT)||privShowQtyPrice) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.hidden_receive_item_id.value=oid;
        document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
        document.frm_recmaterial.submit();
    <%
    }
    else {
    %>
        alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
    <%
    }
    %>
}

function gostock(oid) {
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.rec_type.value = 1;
    document.frm_recmaterial.type_doc.value = 1;
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.action="rec_wh_stockcode.jsp";
    document.frm_recmaterial.submit();
}

function itemList(comm) {
    document.frm_recmaterial.command.value=comm;
    document.frm_recmaterial.prev_command.value=comm;
    document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

//add opie-eyek 20131205 untuk posting
function PostingStock() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
    if(conf){
        document.frm_recmaterial.command.value="<%=Command.POSTING%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
        document.frm_recmaterial.submit();
        }
}

//add opie-eyek 20131205 untuk posting
function PostingCostPrice() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
    if(conf){
        document.frm_recmaterial.command.value="<%=Command.REPOSTING%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
        document.frm_recmaterial.submit();
        }
}

function changeCurrency(value){
    //alert("asa"+value);
    var oidCurrencyId=value;
    checkAjax(oidCurrencyId);
}

function checkAjax(oidCurrencyId){
    $.ajax({
    url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE="+oidCurrencyId+"&typeCheckCurrency=1",
    type : "POST",
    async : false,
    success : function(data) {
         document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value=data;
         //document.frm_purchaseorder.exchangeRate.value=data;
    }
});
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

function viewHistoryTable() {
    var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                     "&oidDocHistory=<%=oidReceiveMaterial%>";
    window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}
//-->

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
 <script src="../../../styles/jquery.min.js"></script>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#FCFDEC" >
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
          	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][5]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%>		  
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0"  bgcolor="#FCFDEC" >
              <tr>
                 <td width="88%" valign="top" align="left">
                   <table width="100%" border="0" cellspacing="0" cellpadding="0">
                     <tr>
                       <td>
                         <form name="frm_recmaterial" method="post" action="">
                          <%
                          try {
                          %>
                          <input type="hidden" name="command" value="">
                          <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                          <input type="hidden" name="start_item" value="<%=startItem%>">
                          <input type="hidden" name="rec_type" value="">
                          <input type="hidden" name="type_doc" value="">
                          <input type="hidden" name="command_item" value="<%=cmdItem%>">
                          <input type="hidden" name="approval_command" value="<%=appCommand%>">
                          <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                          <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
                          <input type="hidden" name="hidden_receive_item_id" value="">
                          <input type="hidden" name="hidden_currency_id" value="<%=rec.getCurrencyId()%>">
                          <input type="hidden" name="hidden_currency_id" value="<%=oidCurrencyRec%>">
                          <input type="hidden" name="hidden_purchase_order_id" value="<%=oidPurchaseOrder%>">
                          <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
                          <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                          <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER_PO%>">
                          <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>" value="<%=rec.getPurchaseOrderId()%>">
                          <%--<input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID]%>" value="<%=rec.getCurrencyId()%>">--%>
                          
                          <input type="hidden" name="iCommandPosting" value="">
                          
                          <% if(oidReceiveMaterial == 0) { %>
                            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="">
                          <% } %>
                          <table width="100%" border="0">
                            <tr>
                              <td valign="top" colspan="3">&nbsp;</td>
                            </tr>
                            <tr>
                              <td colspan="3">
                                <table width="100%" border="0" cellpadding="1">
                                  <tr>
                                    <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                    <td width="21%" align="left">: <b><%=rec.getRecCode()%></b></td>
                                    <td width="9%" valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                    <td width="29%" valign="bottom">  :
                                    <%
                                    Vector obj_supplier = new Vector(1,1);
                                    Vector val_supplier = new Vector(1,1);
                                    Vector key_supplier = new Vector(1,1);
                                    
                                    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                            " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                            " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                            " != "+PstContactList.DELETE;
                                    Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                    for(int d=0; d<vt_supp.size(); d++){
                                        ContactList cnt = (ContactList)vt_supp.get(d);
                                        String cntName = cnt.getCompName();
                                        if(cntName.length()==0){
                                            cntName = cnt.getPersonName()+" "+cnt.getPersonLastname();
                                        }
                                        val_supplier.add(String.valueOf(cnt.getOID()));
                                        key_supplier.add(cntName);
                                    }
                                    String select_supplier = ""+rec.getSupplierId();
                                    %>
                                    <input type="text" id="suplierName">
                                        <%--<img style='cursor:pointer' id='btnOpenSearchSuplier' name="Image200" src="/D-Prochain_V3_2015/images/BtnSearch.jpg" alt="Tambah   Item" border="0" height="24" width="24">--%>
                                        <button type="button" class="btn-primary btn" id="btnOpenSearchSuplier"> <i class="fa fa-search"> </i></button>
                                    <div style='display:none'>
                                    <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"tabindex=\"2\"","formElemen suplierId")%> </td>
                                    </div>
                                    
                                    <!-- adding term payment -->
                                    <!-- by Mirahu 20120302 -->
                                     <!-- update opie-eyek show kan price untuk user yang mempunyai hak akses untuk view price saja -->
                                    <%if(privShowQtyPrice){%>
                                    <td width="7%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][15]%></td>
                                    <td width="26%" align="left" valign="top">:
                                    <%
                                        Vector val_terms = new Vector(1,1);
                                        Vector key_terms = new Vector(1,1);
                                        for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                            val_terms.add(String.valueOf(d));
                                            key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                        }
                                        String select_terms = ""+rec.getTermOfPayment();
                                    %>
                                    <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"","formElemen")%> </td>
                                    <%}else{ %>
                                         <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=rec.getTermOfPayment()%>"></td>
                                    <%}%>
                                  </tr>
                                  <tr>
                                    <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                    <td width="21%" align="left">:
                                    <%
                                    Vector obj_locationid = new Vector(1,1);
                                    Vector val_locationid = new Vector(1,1);
                                    Vector key_locationid = new Vector(1,1);

                                    /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                    whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                    Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);*/
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
                                    String select_locationid = ""+rec.getLocationId(); //selected on combo box
                                    %>
                                    <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, " tabindex=\"1\"", "formElemen")%> </td>
                                    <td valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                    <td>:
                                      <input type="text" name="txt_ponumber"  value="<%= po.getPoCode() %>" class="formElemen" size="20" tabindex="2" onKeyPress="javascript:fnTrapKD(event)">
                                      <%if(listMatReceiveItem.size()==0){%><a href="javascript:cmdSelectPO()">CHK</a><%}%>
                                    </td>
                                     <!-- adding credit time -->
                                    <!-- by Mirahu 20120302 -->
                                     <!-- update opie-eyek show kan price untuk user yang mempunyai hak akses untuk view price saja -->
                                    <%if(privShowQtyPrice){%>
                                        <td width="7%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][16]%></td>
                                        <td width="26%" align="left" valign="top">:
                                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                        <!--td align="right"></td-->
                                    <%}else{ %>
                                         <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="formElemen" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                    <%}%>
                                  </tr>
                                  <tr>
                                    <td width="8%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                    <td width="21%" align="left">: <%=ControlDate.drawDateWithStyle(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date() : rec.getReceiveDate(), 1, -5,"formElemen", "")%></td>
                                    <td valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                                    <td>:
                                      <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" tabindex="3">
                                    </td>
                                    <td width="7%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                    <td width="26%" rowspan="4" valign="top">: <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="formElemen" wrap="VIRTUAL" rows="2" tabindex="4"><%=rec.getRemark()%></textarea></td> 
                                    <!--td align="right"></td-->
                                  </tr>
                                  <tr>
                                    <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][9]%></td>
                                    <td align="left">: <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date(): rec.getReceiveDate(),"formElemen")%></td>
                                    <td valign="bottom"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                                    <td>:
                                    <%
                                    Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
                                    Vector vectCurrVal = new Vector(1,1);
                                    Vector vectCurrKey = new Vector(1,1);
                                    vectCurrKey.add(" ");
                                    vectCurrVal.add("0");
                                    for(int i=0; i<listCurr.size(); i++){
                                        CurrencyType currencyType = (CurrencyType)listCurr.get(i);
                                        vectCurrKey.add(currencyType.getCode());
                                        vectCurrVal.add(""+currencyType.getOID());
                                    }
                                     out.println(ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\""));

                                    %>
                                    <%--<%=ControlCombo.draw("CURRENCY_CODE","formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                                    <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\"")%>--%>
                                    &nbsp;&nbsp;
                                    <%=textListOrderHeader[SESS_LANGUAGE][17]%>&nbsp;&nbsp;
                                       <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="formElemen" size="10" value="<%=rec.getTransRate()%>" >
                                    </td>
                                    <td align="right"></td>
                                  </tr>
                                  <tr>
                                    <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                    <td align="left">:
                                    <%
                                    Vector obj_status = new Vector(1,1);
                                    Vector val_status = new Vector(1,1);
                                    Vector key_status = new Vector(1,1);
                                    
                                    if(typeOfBusiness.equals("3")){
                                       if(rec.getReceiveStatus()!=I_DocStatus.DOCUMENT_STATUS_APPROVED) {
                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                            if(listMatReceiveItem.size()>0){
                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                            }
                                       } 
                                    }else{
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                        //appObjCode
                                        if(listMatReceiveItem.size()>0){
                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                        }
                                    }
                                    // update opie-eyek 19022013
                                    // user bisa memfinalkan purchase request  jika  :
                                    // 1. punya approve document pr = true
                                    // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                    boolean locationAssign=false;
                                    locationAssign  = PstDataCustom.checkDataCustom(userId, "user_location_map",po.getLocationId());
                                    
                                    if(listMatReceiveItem.size()>0 && privApprovalApprove==true){
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_APPROVED));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                                    }
                                    
                                    if(listMatReceiveItem.size()>0 && privApprovalFinal==true && locationAssign==true){ 
                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                    }
                                    
                                    String select_status = ""+rec.getReceiveStatus();
                                    if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                    }else if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                        out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                    }else{
                                    %>
                                    <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"","formElemen")%> 
                                       &nbsp;
                                       <%if(privShowQtyPrice){%>
                                       <div>
                                        <% if (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){%>
                                            <%=textListForwarderInfo[SESS_LANGUAGE][7]%>
                                        <%}%>
                                       </div>
                                       <%}%>
                                    </td>
                                    <% } %> 
                                    <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][18]%></td>
									  <td>: 
										  BC001
									  </td>
                                    <td align="right"></td>
                                    <td valign="top"></td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                            <tr>
                              <td colspan="3">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                  <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle" class="errfont"><%=list.get(0)%>
                                    </td>
                                  </tr>
                                  <%if(oidReceiveMaterial!=0){%>
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
                                    <td height="22" colspan="3" valign="middle" class="errfont">
                                    </td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle" class="errfont">Bonus Item
                                    </td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle" class="errfont"><%=listBonus.get(0)%>
                                    </td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle" class="errfont"><span class="command">
                                      <%
                                      for(int k=0;k<listError.size();k++){
                                          if(k==0)
                                              out.println("<img src='../../../images/DOTreddotANI.gif'>Maaf, dokument tidak bisa di finalkan karena <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                          else
                                              out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                      }
                                      %>
                                      </span></td>
                                  </tr>
                                   <tr align="left" valign="top">
                                    <td height="22" colspan="3" valign="middle" class="errfont"><span class="command">
                                      <%
                                      for(int k=0;k<listErrorBonus.size();k++){
                                          if(k==0)
                                              out.println("<img src='../../../images/DOTreddotANI.gif'>Maaf, dokument tidak bisa di finalkan karena <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                          else
                                              out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listErrorBonus.get(k)+"<br>");
                                      }
                                      %>
                                      </span></td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3">
                                      <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && listMatReceiveItem.size()==0) { 
                                          if(typeOfBusiness.equals("3") && privApprovalFinal==true) {%>
                                           
                                          <%}else{%>
                                             <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                <tr>
                                                  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                                  <td width="44%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%></a></td>

                                                  <td width="6%"><a href="javascript:addAllItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," All Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                                  <td width="44%"><a href="javascript:addAllItem()"><%=ctrLine.getCommand(SESS_LANGUAGE," All Item",ctrLine.CMD_ADD,true)%></a></td>
                                                </tr>
                                             </table>
                                          <%}%>
                                      <% } else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){ 
                                          if(typeOfBusiness.equals("3") && privApprovalFinal==true){%>
                                          <%}else{%>
                                            <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                <tr>
                                                  <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%>"></a></td>
                                                  <td width="44%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE," Item",ctrLine.CMD_ADD,true)%></a></td>
                                                </tr>
                                            </table>
                                          <%}%>
                                      <% } %>
                                    </td>
                                  </tr>
                                  <%}%>
                                </table>
                              </td>
                            </tr>
                            <% if(listMatReceiveItem != null && listMatReceiveItem.size() > 0) { %>
                            <tr>
                              <td colspan="3">&nbsp;</td>
                            </tr>
                            <tr>
                            <%if(privShowQtyPrice){%>
			    <td width="50%"  valign="top">
                              <!--<td colspan="2" valign="top">-->
                                <table width="90%" border="0">
                                  <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3" class="mainheader"><%=textListForwarderInfo[SESS_LANGUAGE][5]%> :</td>
                                  </tr>
                                  <%
                                  if(vctForwarderInfo.size() > 0) {
                                        Vector temp = (Vector)vctForwarderInfo.get(0);
                                        ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                                        ContactList contactList = (ContactList)temp.get(1);
                                  %>
                                  <tr align="left" valign="top">
                                    <td width="1%" height="22" valign="middle" colspan="1">&nbsp;</td>
                                    <td width="99%" height="22" valign="middle" colspan="2">
                                      <table width="80%" border="0" cellspacing="2" cellpadding="0">
                                        <tr>
                                          <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][0]%></td>
                                          <td height="22" width="2%">:</td>
                                          <td height="22" width="68%">
                                            <a href="javascript:editForwarderInfo()"><%=forwarderInfo.getDocNumber()%></a>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][1]%></td>
                                          <td height="22" width="2%">:</td>
                                          <td height="22" width="68%">
                                                <%=ControlCombo.draw(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"disabled=\"true\"","formElemen")%>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][2]%></td>
                                          <td height="22" width="2%">:</td>
                                          <td height="22" width="68%">
                                                <%=ControlDate.drawDateWithStyle(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE], (forwarderInfo.getDocDate()==null) ? new Date() : forwarderInfo.getDocDate(), 0, -1, "formElemen", "disabled=\"true\"")%>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][3]%></td>
                                          <td height="22" width="2%">:</td>
                                          <td height="22" width="68%">
                                                <strong><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></strong>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td height="22" width="30%"><%=textListForwarderInfo[SESS_LANGUAGE][4]%></td>
                                          <td height="22" width="2%">:</td>
                                          <td height="22" width="68%">
                                                <textarea name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]%>" class="formElemen" wrap="VIRTUAL" rows="2" cols="27" disabled="true"><%=forwarderInfo.getNotes()%></textarea>
                                          </td>
                                        </tr>
                                      </table>
                                    </td>
                                  </tr>
                                  <% } else { %>
                                  <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3" class="comment">&nbsp;&nbsp;<%=textListForwarderInfo[SESS_LANGUAGE][6]%></td>
                                  </tr>
                                  <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3">
                                      <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>
                                      <table width="80%" border="0" cellspacing="2" cellpadding="0">
                                        <tr>
                                          <td width="6%"><a href="javascript:addForwarderInfo()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListForwarderInfo[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%>"></a></td>
                                          <td width="94%"><a href="javascript:addForwarderInfo()"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListForwarderInfo[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
                                        </tr>
                                      </table>
                                      <% } else { %>
                                      &nbsp;
                                      <% } %>
                                    </td>
                                  </tr>
                                  <% } %>
                                </table>
                              </td>
                            <%}%>
                            <!-- include receive_payable_list -->
                            <!-- by Mirahu -->
                            <!-- 20120301 -->
                            <!-- update opie-eyek show kan price untuk user yang mempunyai hak akses untuk view price saja -->
                            <%if(privShowQtyPrice){%>
                              <% if(rec.getTermOfPayment()== 0){ %>
                                <td width="18%" valign="top">
                                  <table width="100%" border="0">
                                      <tr>
                                        <td width="56%" class="mainheader"><div align="left"><%=textListDetailPayment[SESS_LANGUAGE][1]%> :</div></td>
                                      </tr>
                                        <tr>
                                        <td width="56%">
                                             <%@ include file = "receive_payable_list.jsp" %>
                                        </td>
                                        </tr>
                                        <tr>
                                          <td width="6%" valign="top"><a href="javascript:addReceivePayable('<%=oidReceiveMaterial%>','<%=oidPurchaseOrder%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListDetailPayment[SESS_LANGUAGE][2],ctrLine.CMD_ADD,true)%>"></a>
                                            &nbsp;&nbsp;<a href="javascript:addReceivePayable('<%=oidReceiveMaterial%>','<%=oidPurchaseOrder%>')"><%=ctrLine.getCommand(SESS_LANGUAGE,recCode+textListDetailPayment[SESS_LANGUAGE][2],ctrLine.CMD_ADD,true)%></a></td>
                                        </tr>
                                </table>
                                </td>
                                <%} %>
                             <%} %>
                              <%--jika mempunyai privilage untuk view harga dan qty penerimaan --%>
                               <!-- update opie-eyek show kan price untuk user yang mempunyai hak akses untuk view price saja -->
                              <%if(privShowQtyPrice){%>
                                 <td width="34%" valign="top">
                                    <table width="100%" border="0">
                                      <%
                                      String whereItem = ""+PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidReceiveMaterial;
                                      double totalBeli = (PstMatReceiveItem.getTotal(whereItem));
                                      double ppn = (rec.getTotalPpn());
                                      if(ppn == 0){
                                            ppn  = defaultPpn;
                                      }
                                      int includePpn = rec.getIncludePpn();
                                      
                                      double totalBeliWithPPN = ((totalBeli * (rec.getTotalPpn() / 100)) + totalBeli);
                                      //inlude ppn
                                      double valuePpn = 0.0;
                                      if(rec.getIncludePpn()== 1){
                                        valuePpn =(totalBeli - (totalBeli /1.1));
                                      }
                                      else if(rec.getIncludePpn()== 0){
                                        valuePpn = (totalBeli * (rec.getTotalPpn() / 100));
                                      }
                                      %>
                                      <tr>
                                        <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][11]%> <%=textListOrderItem[SESS_LANGUAGE][5]%></div></td>
                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b></div></td>
                                      </tr>
                                      <tr>
                                          <td width="56%"><div align="right"><input type="checkbox" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if(includePpn==1){%>checked<%}%>><%=textListOrderHeader[SESS_LANGUAGE][13]%>&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][8]%>
                                              <!--<input type="text"  class="formElemen" name="<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%//=FRMHandler.userFormatStringDecimal(ppn)%>"  size="4" style="text-align:right">&nbsp;<%//=textListOrderHeader[SESS_LANGUAGE][14]%></div></td>-->
                                                  <input type="text"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%if(ppn != 0.0){%><%=FRMHandler.userFormatStringDecimal(ppn)%><%}else {%><%=FRMHandler.userFormatStringDecimal(defaultPpn)%><%}%>"  size="5" style="text-align:right">&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][14]%></div></td>
                                        <td width="44%">
                                          <div align="right">
                                            <!--<input type="text"  class="formElemen" name="<%//=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%//=FRMHandler.userFormatStringDecimal(ppn)%>"  size="15" style="text-align:right">-->
                                              <b><%=FRMHandler.userFormatStringDecimal(valuePpn)%></b>
                                          </div>
                                        </td>
                                      </tr>
                                      <tr>
                                         <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][11]%></div></td>
                                         <% if (rec.getIncludePpn()==1) { %>
                                         <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli)%></b></div></td>
                                         <%}
                                           else {%>
                                         <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%></b></div></td>
                                         <%}%>
                                      </tr>
                                      <tr>
                                         <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][11]%> <%=textListOrderItem[SESS_LANGUAGE][6]%></div></td>
                                         <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></b></div></td>
                                      </tr>
                                      <tr>
                                         <td width="56%"><div align="right"><%=textListOrderHeader[SESS_LANGUAGE][12]%></div></td>
                                         <% if (rec.getIncludePpn()==1) { %>
                                          <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeli+totalForwarderCost)%></b></div></td>
                                        <%}
                                            else {  %>
                                         <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)%></b></div></td>
                                         <%}%>
                                      </tr>
                                    </table>
                                  </td>
                              <%}%>
                            </tr>
                            <tr>
                              <td colspan="3">&nbsp;</td>
                            </tr>
                            <%}%>
                            <tr>
                              <td colspan="3">
                                <table width="100%" border="0">
                                  <%-- update opie-eyek untuk privilage gudang tidak bisa bisa melihat nominal price--%>
                                <%if(privShowQtyPrice){%>
                                     <tr>
                                       <td>&nbsp;</td>
                                       <td><select name="type_print_tranfer">
                                         <option value="0">Price Cost</option>
                                         <option value="1">Sell Price</option>
                                         <option value="2">Cost & Sell Price</option>
                                       </select></td>
                                     </tr>
                                 <%}else{%>
                                   <input type="hidden" name="type_print_tranfer" value="0">
                                 <%}%>  
                                  <tr>
                                    <td width="70%">
                                    <%
                                    //recTitle = "Terima Barang";
                                    ctrLine.setLocationImg(approot+"/images");
                                    
                                    // set image alternative caption
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));
                                    
                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("80%");
                                    String scomDel = "javascript:cmdAsk('"+oidReceiveMaterial+"')";
                                    String sconDelCom = "javascript:cmdDelete('"+oidReceiveMaterial+"')";
                                    String scancel = "javascript:cmdEdit('"+oidReceiveMaterial+"')";
                                    ctrLine.setCommandStyle("command");
                                    ctrLine.setColCommStyle("command");
                                    
                                    // set command caption
                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_SAVE,true));
                                    ctrLine.setBackCaption(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ASK,true));
                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_DELETE,true));
                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_CANCEL,false));
                                    
                                    if(privDelete && privManageData){
                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);
                                    }else{
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                    }
                                    
                                    if(typeOfBusiness.equals("3")){
                                        if(privApprovalFinal==true && (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)){
                                            ctrLine.setSaveCaption("");
                                            ctrLine.setDeleteCaption("");
                                        }
                                        if(privApprovalApprove==true && rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_APPROVED && privApprovalFinal==false){
                                            ctrLine.setSaveCaption("");
                                        }
                                    }
                                    
                                    if((privAdd==false && privUpdate==false) || listError.size()>0){
                                         ctrLine.setSaveCaption("");
                                    }
                                    
                                    if(privAdd==false || rec.getReceiveStatus()!=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                        ctrLine.setAddCaption("");
                                    }
                                    
                                    if(iCommand==Command.SAVE && frmrec.errorSize()==0){
                                        //iCommand=Command.EDIT;
                                    }

                                    if(rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_FINAL || rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                          ctrLine.setAddCaption("");
                                          ctrLine.setSaveCaption("");
                                    }

                                    %>
                                    <%=ctrLine.drawImage(iCommand,iErrCode,errMsg)%>
                                    </td>
                                    <td width="30%">
                                      <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                                      <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                            <tr>
                                              <td width="5%" valign="top"><a href="javascript:printForm('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][8]%>"></a></td>
                                              <td width="45%" nowrap>&nbsp;<a href="javascript:printForm('<%=oidReceiveMaterial%>')" class="command"> <%=textListGlobal[SESS_LANGUAGE][8]%> </a></td>
                                              <%if(!typeOfBusiness.equals("0")){%>
                                                <td width="5%" valign="top"><a href="javascript:printBarcode('<%=oidReceiveMaterial%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][8]%>"></a></td>
                                                <td width="45%" nowrap>&nbsp;<a href="javascript:printBarcode('<%=oidReceiveMaterial%>')" class="command"> <%=textListGlobal[SESS_LANGUAGE][12]%> </a></td>
                                            
                                              <%}%>
                                            </tr>
                                      </table>
                                      <%}%>
                                    </td>
                                  </tr>
                                  <tr>
                                      <td>&nbsp;</td>
                                  </tr>
                                   <tr>
                                      <td> <centre> <a href="javascript:viewHistoryTable()">VIEW TABEL HISTORY</a></centre></td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>                          
                          <%
                          } catch(Exception e) {
                              System.out.println(e);
                          }
                          %>
                         </form>
                         <script language="javascript">
                            document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.focus();
                         </script>
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
 <!-- CODE UNTUK MODAL BOOTSTRAP-->
 <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
 <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
 <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
 <script type="text/javascript">
    $(document).ready(function(){
         
        function ajaxReceive(url,data,type,appendTo,another,optional,optional2){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {
                    $(''+appendTo+'').html(data);
                    if (another=="getSearchTool"){
                        $('#modalSuplier').modal('show');
                    }
                },
                error : function(data){
                    alert('error');
                }
            }).done(function(){
                if (another=="getSearchTool"){
                    getListSuplier();
                    searchKeyUp();
                }else
                if (another=="getListSuplier"){
                    selectSuplier();
                }
            });

        }
        
        function getSearchTool(){
            var url = "<%=approot%>/AjaxReceive";
            var data = "command=<%=Command.SEARCH%>&language=<%= SESS_LANGUAGE%>";
            ajaxReceive(url,data,"POST",".bodyUp","getSearchTool","","");
        }
        
        function getListSuplier(){
            var url ="<%=approot%>/AjaxReceive";
            var typeText = $('#searchSuplier').val();
            var data = "command=<%=Command.LIST%>&language=<%= SESS_LANGUAGE%>&typeText="+typeText+"";
            ajaxReceive(url,data,"POST",".bodyDown","getListSuplier","","");
        }
        
        function searchKeyUp(){
            $('#searchSuplier').keyup(function(){
                getListSuplier();
            });
        }
        
        function selectSuplier(){
            $('.selectSuplier').click(function(){
                var id = $(this).data('oidsuplier');
                var suplierName = $(this).data('namasuplier');
                
                //set input text
                $('#suplierName').val(suplierName);
                //set select 
                $(".suplierId").val(id);
                $('#modalSuplier').modal('hide');
            });
        }
        
        function copyValue(){
            var value = $(".suplierId option:selected").text();
            $('#suplierName').val(value);
        }
        
        $('#btnOpenSearchSuplier').click(function(){
            getSearchTool();
        });
        
        copyValue();
        
        
    }); 
 </script>
  <div id="modalSuplier" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][3]%></h4>
            </div>
            <div class="modal-body" id="modal-body">
                <div class="bodyUp">
                      
                </div>
                <div class="bodyDown"></div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
            </div>
        </div>
    </div>
</div>
</body>
<!-- #EndTemplate --></html>
