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
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang","Cetak Penerimaan Barang","Informasi Forwarder Harus Diisi","Posting Stock","Posting Harga Beli"},
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item","Print Goods Receive","Forwarder Information Require","Posting Stock","Posting Cost Price"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor PO","Nomor Invoice","PPN","Waktu","Mata Uang","Sub Total","Grand Total","Include","%","Terms","Days","Rate"},//16
    {"Number","Location","Date","Supplier","Status","Remark","PO Number","Supplier Invoice","PPN","Time","Currency","Sub Total","Grand Total","Include","%","Terms","Days","Rate"}
};

/* this constant used to list text of listMaterialItem */
//public static final String textListOrderItem[][] = {
    //{"No","Sku","Nama","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli"},
   // {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Sub Total Cost"}
//};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",//10
    "Diskon1 %","Diskon2 %","Discount Nominal","Hapus"},// 14
   {"No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
    "Discount2 %","Disc. Nominal","Delete"}
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
public Vector drawListRecItem(int language,Vector objectClass,int start,boolean privManageData, boolean privShowQtyPrice, double exchangeRate, int statusDoc, String approot){
    
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
        ctrlist.addHeader(textListOrderItem[language][2],"20%");//Nama Barang
        if(bEnableExpiredDate){
            ctrlist.addHeader(textListOrderItem[language][3],"7%");//Kadaluarsa
        }
        ctrlist.addHeader(textListOrderItem[language][4],"5%");//Unit
        if(privShowQtyPrice){
            //ctrlist.addHeader(textListOrderItem[language][5],"8%");//Harga Beli
            //ctrlist.addHeader(textListOrderItem[language][11],"5%");//Diskon1
            //ctrlist.addHeader(textListOrderItem[language][12],"5%");//"Diskon2 %
            //ctrlist.addHeader(textListOrderItem[language][13],"8%");//Discount Nominal
            //ctrlist.addHeader(textListOrderItem[language][6],"8%");//Ongkos Kirim
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
            if((privManageData || privShowQtyPrice)&&statusDoc!=I_DocStatus.DOCUMENT_STATUS_CLOSED){
                rowx.add("<a href=\"javascript:editItem('"+String.valueOf(recItem.getOID())+"')\">"+mat.getSku()+"</a>");
            }else{
                rowx.add(mat.getSku());
            }
            
            rowx.add(mat.getName());
            if(bEnableExpiredDate){
                rowx.add("<div align=\"center\">"+Formater.formatDate(recItem.getExpiredDate(), "dd-MM-yyyy")+"</div>");
            }
            if(privShowQtyPrice){
            rowx.add("<div align=\"left\">"+unit.getCode()+"</div>"
                + "<div align=\"right\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getCost())+"</div>"
                + "<div align=\"right\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount())+"</div>"
                + "<div align=\"right\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscount2())+"</div>"
                + "<div align=\"right\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getDiscNominal())+"</div>"
                + "<div align=\"right\" class=\"hidden\">"+FRMHandler.userFormatStringDecimal(recItem.getForwarderCost())+"</div>");
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
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
            }
            if(privShowQtyPrice){
                 rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal((recItem.getTotal()+totalForwarderCost))+"</div>");
            }
            // add by fitra 17-05-2014
             if(privManageData){
            rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('"+String.valueOf(recItem.getOID())+"')\"><img src="+approot+"/images/x3.png align=\"center\" ></a></div>");
                       }else{
                 rowx.add("");
                       }
            
            
            lstData.add(rowx);
        }
        result = ctrlist.drawBootstrap(); 
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
//typer reail
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
//maka statusnya final = posting value
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

 Vector listItem = new Vector();
try{
    listItem = drawListRecItem(SESS_LANGUAGE,listMatReceiveItem,startItem,privManageData,privShowQtyPrice,po.getExchangeRate(),rec.getReceiveStatus(), approot);
    listError = (Vector)listItem.get(1);
}catch(Exception e){
    System.out.println(e);
}


Vector listBonus = drawListRecItem(SESS_LANGUAGE,listMatReceiveBonusItem,startItem,privManageData,privShowQtyPrice,po.getExchangeRate(),rec.getReceiveStatus(), approot);
listErrorBonus = (Vector)listBonus.get(1);

// add by fitra 17-05-2014
if(iCommand==Command.DELETE && iErrCode==0) {
%>
    <jsp:forward page="m_src_receive_material.jsp">
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
<meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
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
<!-- #EndEditable -->
        
        

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->

<SCRIPT language=JavaScript>
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function cmdEdit(oid){
    document.frm_recmaterial.command.value="<%=Command.EDIT%>";
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
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
                        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
                     if(conf){
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.iCommandPosting.value="<%=Command.REPOSTING%>";
                        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else{
                        document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
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
                        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
                        if(compare()==true)
                                    document.frm_recmaterial.submit();
                     }
                }else{
                    document.frm_recmaterial.command.value="<%=Command.SAVE%>";
                    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
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
        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
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
    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oid){
    document.frm_recmaterial.command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.hidden_receive_item_id.value=oid;
    document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
    document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
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
    document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
    document.frm_recmaterial.submit();
}

function cmdBack(){
    <% if(oidReceiveMaterial != 0 && vctForwarderInfo.size() == 0 && totalForwarderCost > 0) { %>
        alert("<%=textListGlobal[SESS_LANGUAGE][9]%>");
            
    <% } else { %>
        document.frm_recmaterial.command.value="<%=Command.FIRST%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="m_src_receive_material.jsp";
        document.frm_recmaterial.submit();
    <% } %>
}

function cmdSelectPO(){
    var mydate = new Date();
    var strvalue  = "m_podosearch.jsp?command=<%=Command.FIRST%>"+
                    "&oidVendor="+document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>.value+
                    "&po_code="+document.frm_recmaterial.txt_ponumber.value;
    window.open(strvalue,"material", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
}

function printForm(){
    window.open("receive_wh_supp_po_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function addForwarderInfo() {
    <%
    if ((rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT)) {
    %>
        document.frm_recmaterial.command.value="<%=Command.EDIT%>";
        document.frm_recmaterial.action="m_forwarder_info_po.jsp";
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
        document.frm_recmaterial.action="m_forwarder_info_po.jsp";
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
		document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
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
		document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
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
        document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
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
    document.frm_recmaterial.action="m_receive_wh_supp_po_materialitem.jsp";
    document.frm_recmaterial.submit();
}

//add opie-eyek 20131205 untuk posting
function PostingStock() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
    if(conf){
        document.frm_recmaterial.command.value="<%=Command.POSTING%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
        document.frm_recmaterial.submit();
        }
}

//add opie-eyek 20131205 untuk posting
function PostingCostPrice() {
    var conf = confirm("<%=textPosting[SESS_LANGUAGE][1]%>");
    if(conf){
        document.frm_recmaterial.command.value="<%=Command.REPOSTING%>";
        document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
        document.frm_recmaterial.action="m_receive_wh_supp_po_material_edit.jsp";
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
                        Dashboard
                        <small><%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][5]%> &gt; <%=textListGlobal[SESS_LANGUAGE][4]%></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frm_recmaterial" method="post" action="">
                          
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
                        <!--form hidden -->
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="box-body">
                                <div class="row"> 
                                        <div class="col-md-3">
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][0]%></label><br />
                                                <b><%=rec.getRecCode()%></b>
                                            </div>
                                             
                                             
                                                    
                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></label><br />
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
                                                        <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, " tabindex=\"1\"", "form-control")%> </td>
                                               </div>
                                                        
                                               <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></label><br />
                                                    <%=ControlDate.drawDateWithBootstrap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date() : rec.getReceiveDate(), 1, -5,"form-control-date", "")%>
                                                </div>  
                                                 
                                                <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][9]%></label><br />
                                                    <%=ControlDate.drawTimeSec(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate()==null) ? new Date(): rec.getReceiveDate(),"form-control-date")%>
                                                </div>
                                                
                                                <div class="form-group">
                                                     <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][4]%></label><br />
                                                     
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
                                                       <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS],null,select_status,val_status,key_status,"","form-control")%> 
                                                          &nbsp;
                                                          <%if(privShowQtyPrice){%>
                                                          <div>
                                                           <% if (rec.getReceiveStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT){%>
                                                               <%=textListForwarderInfo[SESS_LANGUAGE][7]%>
                                                           <%}%>
                                                          </div>
                                                          <%}%>
                                                          
                                                      <% } %> 
                                                 </div>
                                                
                                        </div>
                                        <!-- End Of col-md-3 -->    
                                            
                                        <div class="col-md-5">
                                            
                                            <div class="form-group">   
                                                 <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][3]%></label><br />
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
                                                    <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"tabindex=\"2\"","form-control")%> </td>
                                                    <!-- adding term payment -->
                                                    <!-- by Mirahu 20120302 -->
                                                     <!-- update opie-eyek show kan price untuk user yang mempunyai hak akses untuk view price saja -->
                                             </div>
                                             
                                             <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][6]%></label><br />
                                                   <input type="text" name="txt_ponumber"  value="<%= po.getPoCode() %>" class="form-control" size="15" tabindex="2" onKeyPress="javascript:fnTrapKD(event)">
                                                    <%if(listMatReceiveItem.size()==0){%><a href="javascript:cmdSelectPO()">CHK</a><%}%>
                                             </div>
                                            
                                            
                                            <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][7]%></label><br />
                                                 
                                                    <input type="text"  class="form-control" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  size="20" style="text-align:right" tabindex="3">
                                            </div>
                                            
                                            <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][10]%></label><br />
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
                                                         out.println(ControlCombo.drawBoostrap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"form-control", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\""));

                                                        %>
                                                        <%--<%=ControlCombo.draw("CURRENCY_CODE","formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>
                                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+rec.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\"")%>--%>
                                                        &nbsp;&nbsp;
                                                      <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][17]%></label><br />  
                                                      <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>" type="text" class="form-control" size="10" value="<%=rec.getTransRate()%>" >
                                           </div>
                                                    

                                        </div>
                                         <!-- End Of col-md-5 -->   
                                         
                                        <div class="col-md-4">
                                            
                                             <%if(privShowQtyPrice){%>       
                                              <div class="form-group">
                                                  <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][15]%></label><br />
                                                  <%
                                                        Vector val_terms = new Vector(1,1);
                                                        Vector key_terms = new Vector(1,1);
                                                        for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                                            val_terms.add(String.valueOf(d));
                                                            key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                                        }
                                                        String select_terms = ""+rec.getTermOfPayment();
                                                    %>
                                                    <%=ControlCombo.drawBootsratap(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"","formElemen")%> </td>
                                                    <%}else{ %>
                                                         <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>" type="hidden" class="form-control" style="text-align:right" size="5" value="<%=rec.getTermOfPayment()%>"></td>
                                                    <%}%>
                                                  
                                              </div>
                                                    
                                             <%if(privShowQtyPrice){%>
                                            <div class="form-group">
                                                <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][16]%></label><br />
                                                <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="form-control" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                    
                                                <%}else{ %>
                                                     <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" class="form-control" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"></td>
                                                <%}%>

                                            </div>
                                                
                                           <div class="form-group">
                                                    <label for="exampleInputEmail1"><%=textListOrderHeader[SESS_LANGUAGE][5]%></label><br />
                                                    <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="2" tabindex="4"><%=rec.getRemark()%></textarea>
                                           </div>     
                                                
                                                    

                                        </div>  
                                        <!-- End Of col-md-4 -->   
                                </div>
                              <!-- End Of Row --> 
                                <div class="row">
                                     <%=listItem.get(0)%>
                                    
                                </div>
                                     
                                
                               <div class="row">      
                                        <div class="col-md-12">
                                              <%if(oidReceiveMaterial!=0){%> 
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


                                               <%
                                              for(int k=0;k<listError.size();k++){
                                                  if(k==0)
                                                      out.println("<img src='../../../images/DOTreddotANI.gif'>Sorry You Can't Final This Document Because <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                  else
                                                      out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                              }
                                              %>



                                                <%
                                                for(int k=0;k<listErrorBonus.size();k++){
                                                    if(k==0)
                                                        out.println("<img src='../../../images/DOTreddotANI.gif'>Sorry You Can't Final This Document Because <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                                    else
                                                        out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listErrorBonus.get(k)+"<br>");
                                                }
                                                %>

                                           </div>
                               </div>
                               <!-- end of row -->                 
                                                
                               <br />
                               <div class="row"> 
                                   
                                   <div class="col-md-12">
                                                    <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT && listMatReceiveItem.size()==0) { 
                                                            if(typeOfBusiness.equals("3") && privApprovalFinal==true) {%>
                                                             <%}else{%>


                                                    <%--<button  onclick="javascript:addItem()" type="submit" class="btn btn-primary">Tambah Item</button>--%>
                                                    <button type="submit" onclick="javascript:addAllItem()" class="btn btn-primary pull-right" >Tambah Semua Item</button>     

                                                        <%}%>  
                                                    <% } else if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT){ 
                                                           if(typeOfBusiness.equals("3") && privApprovalFinal==true){%>
                                                           <%}else{%>
                                                            <button  onclick="javascript:addItem()" type="submit" class="btn btn-primary">Tambah Item</button>


                                                    <%}%>
                                                 <% } %>



                                             <%}%>       

                                        </div>
                                </div>
                                <!-- end of row -->               
                                <br />             
                                             
                               
                               <!-- end of row -->
                               
                               <% if(listMatReceiveItem != null && listMatReceiveItem.size() > 0) { %>
                                <div class="row">
                                    
                                    <div class="col-md-4">
                                        
                                               <%if(privShowQtyPrice){%>

                                               <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][5]%></label>
                                               <%
                                               if(vctForwarderInfo.size() > 0) {
                                               Vector temp = (Vector)vctForwarderInfo.get(0);
                                               ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                                               ContactList contactList = (ContactList)temp.get(1);
                                               %>


                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][0]%></label><br />
                                                   <a href="javascript:editForwarderInfo()"><%=forwarderInfo.getDocNumber()%></a>
                                               </div>

                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][1]%></label><br />
                                                   <%=ControlCombo.drawBootsratap(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_CONTACT_ID],null,String.valueOf(forwarderInfo.getContactId()),val_supplier,key_supplier,"disabled=\"true\"","form-control")%>


                                               </div>    

                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][2]%></label><br />
                                                   <%=ControlDate.drawDateWithStyle(FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_DOC_DATE], (forwarderInfo.getDocDate()==null) ? new Date() : forwarderInfo.getDocDate(), 0, -1, "form-control-date", "disabled=\"true\"")%>

                                               </div> 


                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][3]%></label><br />
                                                   <strong><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></strong>
                                               </div>


                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][4]%></label><br />
                                                   <textarea name="<%=FrmForwarderInfo.fieldNames[FrmForwarderInfo.FRM_FIELD_NOTES]%>" class="formElemen" wrap="VIRTUAL" rows="2" cols="27" disabled="true"><%=forwarderInfo.getNotes()%></textarea>
                                               </div>
                                               <% } else { %>
                                               <div class="form-group">
                                                   <label for="exampleInputEmail1"><%=textListForwarderInfo[SESS_LANGUAGE][6]%></label>





                                               </div>


                                           <% if(rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) { %>

                                           <div class="form-group">

                                               <button  onclick="javascript:addForwarderInfo()" type="submit" class="btn btn-primary">Tambah Informasi Forwader</button>
                                           </div>  

                                           <% } else { %>

                                             &nbsp;
                                          <% } %>   
                                         <% } %>   


                                      

                                        <%}%> 
                                        
                                    </div>
                                  <!-- end of col-md-4-->  
                                    <div class="col-md-4">
                                        
                                                  <%if(privShowQtyPrice){%>
                                                  <% if(rec.getTermOfPayment()== 0){ %>
                                                             <div class="form-group">
                                                                <label for="exampleInputEmail1"><%=textListDetailPayment[SESS_LANGUAGE][1]%> :</label> 
                                                                <%@ include file = "m_receive_payable_list.jsp" %> 
                                                                <button  onclick="javascript:addReceivePayable('<%=oidReceiveMaterial%>','<%=oidPurchaseOrder%>')" type="submit" class="btn btn-primary">Tambah Pembayaran</button>                  
                                                             </div>  
                                                    <%} %>
                                                <%} %>  

                                       
                                        
                                    </div>
                                      <!-- end of col-md-4-->  
                                      <%--  
                                    <div class="col-md-4">
                                                
                                            
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

                                                   
                                     </div>
                                      --%>
                                     <!-- end of col-md-4-->  
                                </div>
			   <!-- end of row --> 
                           <%}%> 
                           
                        <div class="row">  
                                    <div class="col-md-7">
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
                                    </div>
                       
                        
                        
                          <%if(listMatReceiveItem!=null && listMatReceiveItem.size()>0){%>
                               
                                   
                                        
                                        <div class="col-md-5">
                                            <button  onclick="javascript:printForm('<%=oidReceiveMaterial%>')" type="button" class="btn btn-primary pull-right">Print Form</button>
                                        </div>
                                        
                      </div>
                         <!-- end of row -->           
                          <%}%>
                           
                           
                       </div>
                      </div>     
                    </form>
                    <script language="javascript">
                            document.frm_recmaterial.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID]%>.focus();
                    </script>      
                          
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
       <!-- add new calendar event modal -->


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
</html>
