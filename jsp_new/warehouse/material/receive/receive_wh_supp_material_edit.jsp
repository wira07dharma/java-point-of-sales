<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
    com.dimata.posbo.entity.masterdata.*,
    com.dimata.posbo.form.warehouse.CtrlMatReceive,
    com.dimata.posbo.form.warehouse.FrmMatReceive,
    com.dimata.posbo.entity.warehouse.*,
    com.dimata.posbo.form.warehouse.*,
    com.dimata.posbo.session.warehouse.*" %>
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
<%@ page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@ page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@ page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
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
boolean privFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
%>
<%!
static String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
static boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;


public static final String textListGlobal[][] = {
    {"Penerimaan","Dari Pembelian","Pencarian","Daftar","Edit","Dengan PO","Tanpa PO","Tidak ada item penerimaan barang","Cetak Penerimaan Barang","Posting Stock","Posting Harga Beli","Item Baru","Semua"}, //10
    {"Receive","From Purchase","Search","List","Edit","With PO","Without PO","There is no goods receive item","Print Goods Receive","Posting Stock","Posting Cost Price","New Item","All"}
};

/* this constant used to list text of listHeader */
public static final String textListOrderHeader[][] = {
    {"Nomor","Lokasi","Tanggal","Supplier","Status","Keterangan","Nomor Invoice","Ppn","Waktu","Mata Uang","Sub Total","Grand Total","Include", "%","Terms","Days","Rate"},
    {"Number","Location","Date","Supplier","Status","Remark","Supplier Invoice","VAT","Time","Currency","Sub Total","Grand Total","Include", "%","Terms","Days","Rate"}
};

/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] = {
   {"No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",
    "Diskon1 %","Diskon2 %","Discount Nominal","Hapus"},
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

public static final String textState[][] = {
    {"Tunggu","Simpan","Kategori"},
    {"Wait","Save","Category"}
};


public static final String textDelete[][] = {
    {"Apakah Anda Yakin Akan Menghapus Data ?"},
    {"Are You Sure to Delete This Data? "}
};

public static final String textListState[][] ={
    {"Tunggu","Konfirmasi","Ya","Tidak","Silahkan selesaikan proses yang sedang berlangsung!","Kolom masih kosong","Barang Tidak Ditemukan","Tutup"},
    {"Wait","Confirmation","yes","No","Please finish the curent process","This input required","Material cant found","Close"}
};

public static final String textPurchaseOrderHeader[][] = {
    {"Bulan,Tahun Order","Simpan Semua Item","Batal","Kembali Ke Daftar Penerimaan"},
    {"PO Month and Year","Save All Item","Cancel","Back to Receiving List"}
};


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
int iCommandPosting = FRMQueryString.requestInt(request,"iCommandPosting");


int includePpn = FRMQueryString.requestInt(request, "include_ppn");
double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

//adding currency id
long oidCurrencyRec = FRMQueryString.requestLong(request, "hidden_currency_id");
/**
* initialization of some identifier
*/
String errMsg = "";
int iErrCode = FRMMessage.ERR_NONE;
/**
* purchasing ret code and title
*/
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];//"Terima Barang";//i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";
PurchaseOrder po = new PurchaseOrder();
/**
* action process
*/
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
iErrCode = ctrlMatReceive.action(iCommand , oidReceiveMaterial, userName, userId);
FrmMatReceive frmrec = ctrlMatReceive.getForm();
MatReceive rec = ctrlMatReceive.getMatReceive();
errMsg = ctrlMatReceive.getMessage();

String priceCode = "Rp.";

boolean privManageData = true;
if(rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
    //enableInput="readonly";
}
String readonlyQty="";
if(typeOfBusiness.equals("3") && privFinal==true){
    readonlyQty="readonly";
}

if (rec.getPurchaseOrderId()!=0){
    try{
        po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
    }catch(Exception ex){
        po = new PurchaseOrder();
    }
}

%>
<!-- End of Jsp Block -->

<html>
<head>
<title>Dimata - ProChain POS</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>

<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
<link type="text/css" rel="stylesheet" href="../../../styles/daterangepicker.css">
<style>
    .hasErr{
        border-color: #843534;
        box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075) inset, 0px 0px 6px #CE8483;   
    }
    .hasErr:focus{
        border-color: #843534;
        box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.075) inset, 0px 0px 6px #CE8483;   
    }       
</style>
<script src="../../../styles/jquery.min.js"></script>
<script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="../../../styles/moment.js"></script>
<script type="text/javascript" src="../../../styles/daterangepicker.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        var base ="<%=approot%>";
        var language ="<%= SESS_LANGUAGE%>";
        var caption1="<%=textPurchaseOrderHeader[SESS_LANGUAGE][1]%>";
        var caption2="<%=textPurchaseOrderHeader[SESS_LANGUAGE][2]%>";
        
        function cleanNumberInt(strNum, digitGroup) {
            if (!strNum) return strNum;
            strNum = replace(strNum, digitGroup, '', 0);
            return strNum;
        }
        
        function ajaxReceives(url,data,type,append,title){
            $.ajax({
                url : ""+url+"",
                data: ""+data+"",
                type : ""+type+"",
                async : false,
                cache: false,
                success : function(data) {   
                    $(''+append+'').html(data);
                    onSuccess(data,title);
                },
                error : function(data){

                }
            }).done(function(data){
                onDone(data,title,append);    
            });
        }

        function onSuccess(data,title){
            if (title=="clickCurrency"){ 
               $('#transRate').val(data);
            }else if (title=="saveButtonClick"){
                $('#saveButton').html('<%=textState[SESS_LANGUAGE][1]%>').removeAttr('disabled');
                if (data!=""){
                    $('#hidden_receive_id').val(data);
                    getCodeReceive();
                }                
            }else if (title=="checkShowData"){
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").val(data);
                var qty =$("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").val();
                var tempVal = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>").val();
                var value =cleanNumberInt(tempVal,",");
                if(isNaN(value)){
                    value=0;                   
                }
                var total = parseFloat(value)*qty;
                if(isNaN(total)){
                    total=0;
                }
        
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>").val(parseFloat(total));       
                $("#total_cost").val(parseFloat(total));
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>").focus();
            }else if (title=="getAddAllItem"){
                $('#dynamicButtonContainer').html("<button type='button' class='btn btn-primary' id='saveAllItem'><i class='glyphicon glyphicon-ok'></i> "+caption1+"</button> &nbsp <button type='button' class='btn btn-danger' id='cancelAddAllItem'><i class='glyphicon glyphicon-refresh'></i> "+caption2+"</button>");
            }
        }

        function onDone(data,title,append){
            if (title==="getListCurrency"){
                clickCurrency();
            }else if(title==="getListError"){
                $('#listErrSize').val(data);
                getListStatus();
            }else if (title==="getSaveButton"){
                saveButtonClick();
            }else if (title==="saveButtonClick"){
                getListError();
                getListStatus();
                getListReceiveItem();
                getListReceiveItemBonus();
            }else if (title==="getListReceiveItem"){
                changeQty();
                changeShowData();
                loadListItem();
                fieldCost();
                checkBonus();
                expiredDate();
                saveReceiveItem();
                deleteItem();
                editItem();
                getForwaderInfo();
                getListPayment();
                getSummaryReceive();
                getButtonAddAll();
                dateForwarder();
                changeQty2();
                changePriceKonvEvent();
                changePriceKonvEvent2();
                cntTotalAllEvent();
                loadListItemPo();
                saveReceiveItemPo();
                checksBoxs();
                editItemPo();
                changeUnitIdKonv();
                matCodes();
                editItems();
            }else if (title==="getListReceiveItemBonus"){
                deleteItem();
                editItem();
                getListStatus();
                formAddKeyboard();
                getButtonAddAll();
                editItemPo();
                editItems();
            }else if (title==="getListItemModal"){
                selectModalItems1Click();
                selectModalItems2Click();
            }else if (title==="saveReceiveItem"){
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
                setTimeout(function(){
                     $('#matCode').focus();
                }, 500);
            }else if (title==="btnDeleteItemConfirmation"){
                $('#deleteReceiveConfirmation').modal('hide');
                $('#btnDeleteItemConfirmation').removeAttr('disabled').html('<%=textListState[SESS_LANGUAGE][2]%>');
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
                
            }else if (title==="editItem"){
                expiredDate();
                changeQty();
                fieldCost();
                checkBonus();
                saveEditReceiveItem();
                loadListItem();
            }else if (title==="saveEditReceiveItem"){
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
            }else if (title==="getForwaderInfo"){
                addForwader();
                deleteForwarder();
            }else if (title==="getForwaderControl"){
                dateForwarder();
                btnSaveForwarder();
            }else if (title==="btnSaveForwarder"){
                getForwaderInfo();
            }else if (title==="deleteForwarder"){
                getForwaderInfo();
            }else if (title==="getListPayment"){
                addPayments();
                editPayments();
            }else if (title==="getSummaryReceive"){
                calcPpn();
            }else if (title==="getTotalByOidReceiveMaterial"){
                if (data!=="0"){
                   $('#FRM_FIELD_LOCATION_ID').attr('readonly','readonly');
                   $('#FRM_FIELD_SUPPLIER_ID').attr('readonly','readonly');
                   $('#FRM_FIELD_INVOICE_SUPPLIER').attr('readonly','readonly');
                   $('#FRM_FIELD_CURRENCY_ID').attr('readonly','readonly');
                   $('#transRate').attr('readonly','readonly'); 
                   $('#datetime').attr('readonly','readonly'); 
                }else{
                   $('#FRM_FIELD_LOCATION_ID').removeAttr('readonly');
                   $('#FRM_FIELD_SUPPLIER_ID').removeAttr('readonly');
                   $('#FRM_FIELD_INVOICE_SUPPLIER').removeAttr('readonly');
                   $('#datetime').removeAttr('readonly');
                }
            }else if (title==="getListStatus"){
                getTotalByOidReceiveMaterial();
            }else if (title==="getItemBySkuOrName"){
                if (data!==""){
                    apendDataSearch(data);
                }
            }else if (title==="getDeleteButton"){
                deleteButtonClick();
            }else if (title==="btnDeleteReceiveConfirmation"){
                window.location = 'search_receive_material.jsp';
            }else if (title==="getListPoBySuplier"){
                selectPo();
            }else if (title==="getButtonAddAll"){
                addAllItem();
            }else if (title==="getAddAllItem"){
                cancelAddAllItem();
                saveAllItem();
                dateForwarder();
                changeQty2();
                changePriceKonvEvent();
                changePriceKonvEvent2();
                cntTotalAllEvent();
                checksBoxs();
            }else if (title==="saveAllItem"){
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
            }else if (title==="cntTotal2"){
                prosesAfterTotal2(data,append);
            }else if (title==="loadListItemPoModal"){
                selectItemByPo();
            }else if (title==="saveReceiveItemPo"){              
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
            }else if (title==="editItemPo"){
                dateForwarder();
                changeQty2();
                changePriceKonvEvent();
                changePriceKonvEvent2();
                cntTotalAllEvent();
                loadListItemPo();
                saveReceiveItemPo();
                checksBoxs();
                saveEditReceiveItemPo();
                changeUnitIdKonv();
                matCodes();
            }else if (title==="saveEditReceiveItemPo"){
                getListError();
                getCodeReceive();
                getSaveButton();
                getDeleteButton();
                getListReceiveItem();
                getListReceiveItemBonus();
                getListCurrency();
                getListStatus();
            }else if (title==="getPoBySkuOrName"){
                if (data!=""){
                    apendFromGetPo(data);
                    
                }
            }
        }
        
        function getListReceiveItem(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var oidCurrencyRec =$("#hidden_currency_id").val();
            var privManageData = $("#privManageData").val();
            var privShowQtyPrice = $("#privShowQtyPrice").val();
            var receiveStatus = $("#receiveStatus").val();
            var bEnableExpiredDate = $("#bEnableExpiredDate").val();
            var command = "<%=Command.LIST%>";
            var loadType = "getListReceiveItem";
            var privAdd = $('#privAdd').val();
            var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterial="+oidReceiveMaterial+"&oidCurrencyRec="+oidCurrencyRec+"&privManageData="+privManageData+"&privShowQtyPrice="+privShowQtyPrice+"&receiveStatus="+receiveStatus+"&bEnableExpiredDate="+bEnableExpiredDate+"&privAdd="+privAdd+"";
            ajaxReceives(url,data,"POST","#dynamicContainer","getListReceiveItem");
            
        }
        
        function getListReceiveItemBonus(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var oidCurrencyRec =$("#hidden_currency_id").val();
            var privManageData = $("#privManageData").val();
            var privShowQtyPrice = $("#privShowQtyPrice").val();
            var receiveStatus = $("#receiveStatus").val();
            var bEnableExpiredDate = $("#bEnableExpiredDate").val();
            var command = "<%=Command.LIST%>";
            var loadType = "getListReceiveItemBonus";
            var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterial="+oidReceiveMaterial+"&oidCurrencyRec="+oidCurrencyRec+"&privManageData="+privManageData+"&privShowQtyPrice="+privShowQtyPrice+"&receiveStatus="+receiveStatus+"&bEnableExpiredDate="+bEnableExpiredDate+"";
            ajaxReceives(url,data,"POST","#dynamicContainerBonus","getListReceiveItemBonus");
        }
        
        function getListError(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var data = "command=<%=Command.LIST%>&loadType=getListError&oidReceiveMaterial="+oidReceiveMaterial+"";
            ajaxReceives(url,data,"POST","","getListError");
        }
        
        function getListStatus(){
            var url = ""+base+"/AjaxReceives";
            var data = $('#frm_recmaterial').serialize();
            $('#command').val(<%=Command.LIST%>);
            data = data + "&loadType=getListStatus";
            ajaxReceives(url,data,"POST","#dynamicStatus","getListStatus");
        }
        
        function getListCurrency(){
            var url = ""+base+"/AjaxReceives";
            var data = $('#frm_recmaterial').serialize();
            $('#command').val(<%=Command.LIST%>);
            data = data + "&loadType=getListCurrency";
            ajaxReceives(url,data,"POST","#dynamicCurency","getListCurrency");
            
        }
        
        function clickCurrency(){
            $('.clickCurrency').change(function(){
                var value = $(this).val();
                var url = ""+base+"/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE="+value+"&typeCheckCurrency=1"
                var data="";
                ajaxReceives(url,data,"POST","","clickCurrency");
            });
        }
        
        function getSaveButton(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var privAdd= $('#privAdd').val();
            var privUpdate= $('#privUpdate').val();
            var privApprovalFinal= $('#privApprovalFinal').val();
            var privApprovalApprove= $('#privApprovalApprove').val();
            var typeOfBusiness = $('#typeOfBusiness').val();
            var data = "command=<%=Command.GET%>&loadType=getSaveButton&oidReceiveMaterial="+oidReceiveMaterial+"&privAdd="+privAdd+"&privUpdate="+privUpdate+"&privApprovalFinal="+privApprovalFinal+"&privApprovalApprove="+privApprovalApprove+"&typeOfBusiness="+typeOfBusiness+"";
          
            ajaxReceives(url,data,"POST","#saveButtonPlace","getSaveButton");
        }
        
        function getDeleteButton(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var privManageData = $('#privManageData').val();
            var privApprovalFinal= $('#privApprovalFinal').val();
            var privDelete = $('#privDelete').val();         
            var typeOfBusiness = $('#typeOfBusiness').val();
            var data = "command=<%=Command.GET%>&loadType=getDeleteButton&oidReceiveMaterial="+oidReceiveMaterial+"&privManageData="+privManageData+"&privDelete="+privDelete+"&privApprovalFinal="+privApprovalFinal+"&typeOfBusiness="+typeOfBusiness+"";
          
            ajaxReceives(url,data,"POST","#deleteButtonPlace","getDeleteButton");
        }
        
        function validate(selectorTag){
            var errValidate = false;
            var errorIndex = -1;
            var idError = "";
            $(''+selectorTag+' .required').each(function(i){
                var id = this.id;
                var value = $(this).val();
                $(this).parent().find('input').removeClass('hasErr');
                $(this).parent().find('button').removeClass('hasErr');
                if (value.length==0 || value ==0){
                    errValidate = true;
                    $(this).parent().find('input').addClass('hasErr');
                    $(this).parent().find('button').addClass('hasErr');                     
                    if (errorIndex==-1){
                        errorIndex = i;
                        idError = $(this).attr('id');
                    }
                }                 
            });
            setTimeout(function(){
                $('#'+idError+'').focus();
            }, 300);
           return errValidate;
        }
        
        function saveButtonClick(){
            $('#saveButton').click(function(){
                if (validate("#frm_recmaterial")==false){
                    var msgNo=-1;
                    var statusDoc = $("#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>").val();
                    var typeOfBusiness = $('#typeOfBusiness').val();
                    if (typeOfBusiness==="3"){
                        if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" && <%=privApprovalFinal%>==false){
                            $("#command").val("<%=Command.SAVE%>");
                            $("#iCommandPosting").val("<%= Command.POSTING%>");
                            msgNo=0;
                        }else if (statusDoc==="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                            $("#command").val("<%=Command.SAVE%>");
                            $("#iCommandPosting").val("<%= Command.REPOSTING%>"); 
                            msgNo=1;
                        }else{
                            $("#command").val("<%=Command.SAVE%>");
                        }                      
                    }else{
                        if(statusDoc==="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){ 
                            $("#command").val("<%=Command.SAVE%>");
                            $("#iCommandPosting").val("<%= Command.POSTING%>"); 
                            msgNo=0;
                        }else{
                            $("#command").val("<%=Command.SAVE%>");
                        }
                    }
                    
                    //proses confirmasi
                    var dateTime = $('#datetime').val();
                    var temp = dateTime.split(" ");
                    $('#receiveDate').val(temp[0]);
                    $('#receiveTime').val(temp[1]);
                    
                    if (msgNo>=0){
                        var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                        if(conf){
                            $("#saveButton").attr({"disabled":true}).html("<%=textState[SESS_LANGUAGE][0]%>");                         
                            var url = ""+base+"/AjaxReceives";
                            var data = $('#frm_recmaterial').serialize();
                            data = data + "&loadType=saveMasterReceiving";
                            ajaxReceives(url,data,"POST","","saveButtonClick");
                        }
                    }else{
                        $("#saveButton").attr({"disabled":true}).html("<%=textState[SESS_LANGUAGE][0]%>");  
                        var url = ""+base+"/AjaxReceives";
                        var data = $('#frm_recmaterial').serialize();
                        data = data + "&loadType=saveMasterReceiving";
                        ajaxReceives(url,data,"POST","","saveButtonClick");
                    }
                }
            });
        }
        
        function deleteButtonClick(){
            $('#deleteButton').click(function(){
                $('#deleteReceivesConfirmation').modal('show');
            });
            
        }
        
        function getCodeReceive(){
            var url = ""+base+"/AjaxReceives";
            var oidReceiveMaterial = $("#hidden_receive_id").val();
            var data = "command=<%=Command.GET%>&loadType=getCodeReceive&oidReceiveMaterial="+oidReceiveMaterial+"";
            ajaxReceives(url,data,"POST","#oidRecPlaces","getCodeReceive");
        }
                      
        function checkShowData(oidKonversiUnit,oidUnit,qtyInput){
            var url = ""+base+"/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidKonversiUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>="+qtyInput+"";
            var data = "";
            ajaxReceives(url,data,"POST","","checkShowData");
            
        }
        
        function showData(value){
            var qtyInput = $('#formAdd #hidden_qty_input').val();
            var oidUnit = $("#formAdd #<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>").val();
            var oidKonversiUnit=value;
            checkShowData(oidKonversiUnit,oidUnit,qtyInput);
        }
        
        function change(value){
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").val(value);
            $("#hidden_qty_input").val(value);
        }
        
        function change2(value,i){
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val(value);
            //$("#hidden_qty_input").val(value);
        }
        
        function changeQty(){
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>").keyup(function(){
                var value = $(this).val();
                change(value);
                cntTotal(); 
            });
        }
        
        function changeQty2(){
            $(".changeQty").keyup(function(){
                var id = $(this).attr('id');
                var temp = id.split('-');
                var i = temp[1];                
                cntTotal2(i); 
            });
        }
        
        function changeShowData(){
            $("#formAdd #<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>").change(function(){
                var value = $(this).val();
                showData(value);
            });
        }
        
        function loadListItem(){
            $('.loadListItem').click(function(){
                getListItemModal();
                $('#modalReport').modal('show');
            });
        }
        
        function getListItemModal(){
            var oidReceive = $('#hidden_receive_id').val();
            var check = 0;
            if ($('#showAllGood').is(":checked")){
              check=1;
            }
            var sku = $('#sku').val(); 
            var materialName = $('#namaBarang').val();
            var matGroupId = $('#matGroupId').val(); 
            var lang = language;
            
            var url = ""+base+"/AjaxReceives";
            var data = "command=<%=Command.LIST%>&loadType=getListItemModal&oidReceive="+oidReceive+"&check="+check+"&sku="+sku+"&materialName="+materialName+"&matGroupId="+matGroupId+"&language="+language+"";
            
            ajaxReceives(url,data,"POST","#modal-body-low","getListItemModal");
        }
        
        function groupItemModalChange(){
            $('#matGroupId').change(function(){
                getListItemModal();
            });
        }
        
        function skuModalChange(){
            $('#sku').keyup(function(){
                getListItemModal();   
            });
        }
        
        function namaBarangChange(){
            $('#namaBarang').keyup(function(){
                getListItemModal(); 
            });
        }
        
        function searchModal(){
            $('.searchModal').click(function(){
                getListItemModal();
            });
        }
        
        function showAllGood(){
            $('#showAllGood').click(function(){
                getListItemModal();
            });
        }
        
        function apendSelectItem(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty){
            var privShowQtyPrice=$('#privShowQtyPrice').val();
            
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>').val(matOid);
            $('#matCode').val(matCode);
            $('#txt_materialname').val(matItem);
            $('#matUnit').val(matUnit);
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>').val(matPrice);
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>').val(matUnitId);
            
            $('#modalReport').modal('hide');
            
            if (privShowQtyPrice==true){
               $('#matCode').focus(); 
            }else{
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>').focus();
            }
        }
        
        function selectModalItems1Click(){
            $('.selectModalItems1').click(function(){
                var matOid = $(this).data('oid');
                var matCode = $(this).data('sku');
                var matItem = $(this).data('materialname');
                var matUnit = $(this).data('matUnit');
                var matPrice = $(this).data('buyingexchange');
                var matUnitId = $(this).data('unitoid');
                var matCurrencyId = $(this).data('defaultcostcurrency');
                var matCurrCode = $(this).data('getlastdisc');
                var stockQty = $(this).data('currbuyinglastreceive');
                
                apendSelectItem(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty);
            });
        }
        
        function selectModalItems2Click(){
            $('.selectModalItems2').click(function(){
                var matOid = $(this).data('oid');
                var matCode = $(this).data('sku');
                var matItem = $(this).data('materialname');
                var matUnit = $(this).data('unitcode');
                var matPrice = $(this).data('cost');
                var matUnitId = $(this).data('unitoid');
                var matCurrencyId = $(this).data('defaultcurrency');
                var matCurrCode = "0";
                var stockQty = $(this).data('stokqty');
                
                apendSelectItem(matOid,matCode,matItem,matUnit,matPrice,matUnitId,matCurrencyId,matCurrCode,stockQty);
            });
        }
        
        function cntTotal() {
            
            var qty = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").val(),",");
            var price = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>").val(),",");
            var forwarder_cost = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>").val(),",");
            var lastDisc = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>").val(),",",".");
            var lastDisc2 = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>").val(),",",".");
            var lastDiscNom = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>").val(),",",".");
         
            if(qty<0.0000){
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").val(0);
            }

            if(price==""){ 
                price = 0; 
            }
            if(forwarder_cost == ""){ 
                forwarder_cost = 0; 
            }
            if(isNaN(lastDisc) || (lastDisc=="")){
                lastDisc = 0.0;
            }
            if(isNaN(lastDisc2) || (lastDisc2=="")){
                lastDisc2 = 0.0;
            }
            if(isNaN(lastDiscNom) || (lastDiscNom=="")){
                lastDiscNom = 0.0;
            }

            var totaldiscount = price * lastDisc / 100;
            var totalMinus = price - totaldiscount;
            var totaldiscount2 = totalMinus * lastDisc2 / 100;
            var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;


            if(!(isNaN(qty)) && (qty != '0')) {               
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>").val(parseFloat(totalCost) * qty);
                $("#total_cost").val((parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty);
            }else{
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>").focus();
            }   
        }
        
        function cntTotal2(i) {           
            var inputqty = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-"+i+"").val();
            var oidUnitkonv = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>_"+i+"").val();
            var oidUnit = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>-"+i+"").val();
            var price = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",",".");
            var url = "<%=approot%>/servlet/com.dimata.posbo.ajax.CheckKonversiUnit?<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>="+oidUnitkonv+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>="+oidUnit+"&<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>="+inputqty+""
            var data = "";           
            ajaxReceives(url,data,"POST",i,"cntTotal2");           
        }
        
        function changePriceKonvAll(i){
            var qty = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val();
            var cost = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>-"+i+"").val(),",",".");
            var total = cost/qty;
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(parseFloat(total));
            var qtyx = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val();
            var costx = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",",".");
            var totalx = costx*qtyx;   
            $("#total_cost-"+i+"").val(parseFloat(totalx));
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-"+i+"").val(parseFloat(totalx));
        }
        
        function changePriceKonvAll2(i){
            var qty = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val();
            var cost = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",",".");
            var total = cost*qty;
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>-"+i+"").val(parseFloat(total));
            
            var qtyx = $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val();
            var costx = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",",".");
            var totalx = costx*qtyx;
            $("#total_cost-"+i+"").val(parseFloat(totalx));
            $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-"+i+"").val(parseFloat(totalx));
        }
        
        function cntTotalAll(i){
            var qty = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val(),",");
            var price = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",");
            var forwarder_cost = cleanNumberInt($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_FORWARDER_COST]%>-"+i+"").val(),",");
                    
            var lastDisc = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT]%>-"+i+"").val(),",",".");
            var lastDisc2 = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISCOUNT2]%>-"+i+"").val(),",",".");
            var lastDiscNom = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_DISC_NOMINAL]%>-"+i+"").val(),",",".");

            if(price==""){ 
                price = 0; 
            }
            if(forwarder_cost == "") { 
                forwarder_cost = 0; 
            }

            if(isNaN(lastDisc) || (lastDisc=="")){
                lastDisc = 0.0;
            }
            if(isNaN(lastDisc2) || (lastDisc2=="")){
                lastDisc2 = 0.0;
            }
            if(isNaN(lastDiscNom) || (lastDiscNom=="")){
                lastDiscNom = 0.0;
            }

            var totaldiscount = price * lastDisc / 100;
            var totalMinus = price - totaldiscount;
            var totaldiscount2 = totalMinus * lastDisc2 / 100;
            var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
            
            alert(totalCost);

            if(!(isNaN(qty)) && (qty != '0')) {
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-"+i+"").val(parseFloat(totalCost) * qty);
                $("#total_cost-"+i+"").val((parseFloat(totalCost) + parseFloat(forwarder_cost)) * qty);
            }else {
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").focus();
            }
        }
        
        function cntTotalAllEvent(){
            $('.cntTotalAllEvent').keyup(function(){
                var id = $(this).attr('id');
                var temp = id.split("-");
                var i = temp[1];
                cntTotalAll(i);
            });
        }
        
        function changePriceKonvEvent(){
            $('.changePriceKonvEvent').keyup(function(){
                var id = $(this).attr('id');
                var temp = id.split("-");
                var i = temp[1];
                changePriceKonvAll(i);
                //cntTotal2(i);
            });
        }
        
        function changePriceKonvEvent2(){
            $('.changePriceKonvEvent2').keyup(function(){
                var id = $(this).attr('id');
                var temp = id.split("-");
                var i = temp[1];
                changePriceKonvAll2(i);
                //cntTotal2(i);
            });
        }
        
        function prosesAfterTotal2(data,i){
            var sisa = parseFloat($("#FRM_FIELD_RESIDUE_QTY-"+i+"").val());
            var price = cleanNumberFloat($("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(),",",".");
            if(data<=sisa){                  
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-"+i+"").val(data);
                var total = price*data;
                var totalstock = total/data;                           
                if(isNaN(total)){
                    total=0;
                }
                if(isNan(totalstock)){
                    totalstock=0;
                }                            
                $("#total_cost-"+i+"").val(parseFloat(total));
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-"+i+"").val(parseFloat(total));                           
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-"+i+"").val(parseFloat(totalstock));
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>").focus();

            }else{
                alert("Input : "+data+"Residu : "+sisa);
                alert("Maaf qty yang di input melebihi qty order");
                $("#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-"+i+"").val(sisa);

            }
        }
        
        function fieldCost(){
            $('.fieldCost').keyup(function(){
               cntTotal(); 
            });
        }
        
        function checkBonus(){
            $('#typeBonus').click(function(){
                var check = 0;
                if ($('#typeBonus').is(":checked")){
                    check=1;   
                }else{
                    check=0;
                }
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>').val(check);
            });
            
        }
        
        function saveReceiveItem(){
            $('.saveReceiveItem').click(function(){
                var url = ""+base+"/AjaxReceives";
                var data = $('#formAdd').serialize();
                var command = "<%=Command.SAVE%>";
                var loadType= "saveReceiveItem";
                var oidReceive = $('#hidden_receive_id').val();               
                var userName = $("userName").val();
                var userId = $("userId").val();
                data = data + "&oidReceive="+oidReceive+"&loadType="+loadType+"&command="+command+"&userName="+userName+"&userId="+userId+"";               
                ajaxReceives(url,data,"POST","","saveReceiveItem");
            });
        }
        
        function expiredDate(){
            $("#FRM_FIELD_EXPIRED_DATE").daterangepicker({
                "showDropdowns": true,
                "singleDatePicker": true,
                "timePicker": false,
                "timePicker24Hour": true,
                "timePickerSeconds": true,
                "autoApply": true,
                "dateLimit": {
                    "days": 7
                },
                "locale": {
                    "format": "DD/MM/YYYY",
                    "separator": " - ",
                    "applyLabel": "Use",
                    "cancelLabel": "Cancel",
                    "fromLabel": "From",
                    "toLabel": "To",
                    "customRangeLabel": "Custom",
                    "daysOfWeek": [
                        "Su",
                        "Mo",
                        "Tu",
                        "We",
                        "Th",
                        "Fr",
                        "Sa"
                    ],
                    "monthNames": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "firstDay": 1
                },
                "autoUpdateInput": true

            }, function(start, end, label) {
              console.log('New date range selected: ' + start.format('YYYY-MM-DD hh:mm:ss') + ' to ' + end.format('YYYY-MM-DD hh:mm:ss') + ' (predefined range: ' + label + ')');
            });
        }
        
        function deleteItem(){
            $('.deleteItem').click(function(){
                var oid = $(this).data('recitem');
                $('#oidDetails').val(oid);
                $('#deleteReceiveConfirmation').modal('show');
            });
        }
        
        function btnDeleteItemConfirmation(){
            $('#btnDeleteItemConfirmation').click(function(){
                var oidReceiveMaterialItem = $("#oidDetails").val();
                var oidReceiveMaterial = $("#hidden_receive_id").val();
                var url = ""+base+"/AjaxReceives";   
                var userName = $("userName").val();
                var userId = $("userId").val();
                var data = "command=<%=Command.DELETE%>&loadType=deleteReceiveItem&oidReceiveMaterial="+oidReceiveMaterial+"&oidReceiveMaterialItem="+oidReceiveMaterialItem+"&userName="+userName+"&userId="+userId+"";
                $(this).attr('disabled','true').html("<%=textListState[SESS_LANGUAGE][0]%>");
                ajaxReceives(url,data,"POST","","btnDeleteItemConfirmation");
            });
        }
        
        function editItem(){
            $('.editItem').click(function(){
                var bonus = 0;
                var affect="";
                var id = $(this).attr('id');
                var temp = id.split("-");
                
                if ($(this).hasClass('bonus')){
                    bonus=1;
                    affect = "#receiveBonus-"+temp[1]+"";
                }else{
                    bonus=0;
                    affect = "#receive-"+temp[1]+"";
                }
                $('#addForm').remove();
                $('.editItem').remove();
                $('.deleteItem').remove();
                
                var oidReceiveMaterialItem = $(this).data('recitem');
                var oidReceiveMaterial = $("#hidden_receive_id").val();
                var orderData = temp[1];
                var privManageData = $("#privManageData").val();
                var privShowQtyPrice = $("#privShowQtyPrice").val();
                var receiveStatus = $("#receiveStatus").val();
                var bEnableExpiredDate = $("#bEnableExpiredDate").val();
                var lang = language;
                var readonlyQty = $('#readonlyQty').val();
                
                var url = ""+base+"/AjaxReceives"; 
                var command = "<%=Command.GET%>";
                var loadType = "showEditControl";
                
                var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterialItem="+oidReceiveMaterialItem+"&oidReceiveMaterial="+oidReceiveMaterial+"&orderData="+orderData+"&privManageData="+privManageData+"&privShowQtyPrice="+privShowQtyPrice+"&receiveStatus="+receiveStatus+"&bEnableExpiredDate="+bEnableExpiredDate+"&lang="+lang+"&readonlyQty="+readonlyQty+"&bonus="+bonus+"";
               
                ajaxReceives(url,data,"POST",affect,"editItem");
            });
        }
        
        function saveEditReceiveItem(){
            $('.saveEditReceiveItem').click(function(){
                var data="";
                var url = ""+base+"/AjaxReceives";
                if ($(this).hasClass("bonus")){
                    data = $('#formAddBonus').serialize();
                }else{
                    data = $('#formAdd').serialize(); 
                }
                
                var command = "<%=Command.SAVE%>";
                var loadType= "saveEditReceiveItem";
                var oidReceive = $('#hidden_receive_id').val(); 
                var userName = $("userName").val();
                var userId = $("userId").val();
                data = data + "&oidReceive="+oidReceive+"&loadType="+loadType+"&command="+command+"&userName="+userName+"&userId="+userId+"";               
                ajaxReceives(url,data,"POST","","saveEditReceiveItem");
            });       
        }
        
        function getForwaderInfo(){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getForwaderInfo";
            var oidReceive = $('#hidden_receive_id').val(); 
            
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&lang="+language+"";
            ajaxReceives(url,data,"POST","#dynamicLeft","getForwaderInfo");
            
        }
        
        function addForwader(){
            $("#addForwader").click(function(){
                getForwaderControl();
                $('#showModalForwader').modal('show');
            });
        }
        
        function getForwaderControl(){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getForwaderControl";
            var oidReceive = $('#hidden_receive_id').val(); 
            var lang = ""+language+"";
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&lang="+lang+"";
            ajaxReceives(url,data,"POST","#modalForwaderDynamic","getForwaderControl");
            
        }
        
        function dateForwarder(){
            $(".datesPic").daterangepicker({
                "showDropdowns": true,
                "singleDatePicker": true,
                "timePicker": false,
                "timePicker24Hour": true,
                "timePickerSeconds": true,
                "autoApply": true,
                "dateLimit": {
                    "days": 7
                },
                "locale": {
                    "format": "DD/MM/YYYY",
                    "separator": " - ",
                    "applyLabel": "Use",
                    "cancelLabel": "Cancel",
                    "fromLabel": "From",
                    "toLabel": "To",
                    "customRangeLabel": "Custom",
                    "daysOfWeek": [
                        "Su",
                        "Mo",
                        "Tu",
                        "We",
                        "Th",
                        "Fr",
                        "Sa"
                    ],
                    "monthNames": [
                        "January",
                        "February",
                        "March",
                        "April",
                        "May",
                        "June",
                        "July",
                        "August",
                        "September",
                        "October",
                        "November",
                        "December"
                    ],
                    "firstDay": 1
                },
                "autoUpdateInput": true

            }, function(start, end, label) {
              console.log('New date range selected: ' + start.format('YYYY-MM-DD hh:mm:ss') + ' to ' + end.format('YYYY-MM-DD hh:mm:ss') + ' (predefined range: ' + label + ')');
            });
        }
        
        function btnSaveForwarder(){
            $('#btnSaveForwarder').click(function(){                
                if (validate("#forwarderForm")== false){
                    var url = ""+base+"/AjaxReceives"; 
                    var command = "<%=Command.SAVE%>";
                    var loadType = "saveForwarder";
                    var data = $("#forwarderForm").serialize();
                    data = data + "&command="+command+"&loadType="+loadType+"";
                    ajaxReceives(url,data,"POST","","btnSaveForwarder");
                }
            });
        }
        
        function deleteForwarder(){
            $('#deleteForwarder').click(function(){
                $('#deleteForwarderConfirmation').modal('show');
            });
        }
        
        function btnDeleteForwardConfirmation(){
            $("#btnDeleteForwardConfirmation").click(function(){
                var url = ""+base+"/AjaxReceives"; 
                var command = "<%=Command.DELETE%>";
                var loadType = "deleteForwarder";
                var oidReceive = $('#hidden_receive_id').val(); 
                var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"";
                ajaxReceives(url,data,"POST","","deleteForwarder"); 
            });
        }
        
        function getListPayment(){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getListPayment";
            var oidReceive = $('#hidden_receive_id').val(); 
            
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&lang="+language+"";
            ajaxReceives(url,data,"POST","#dynamicCenter","getListPayment");
        }
        
        function addPayments(){
            $('#addPayments').click(function(){
                var oidReceive = $('#hidden_receive_id').val(); 
                $('#oid_invoice_selected').val(oidReceive);
                $('#commands').val(<%=Command.ADD%>);
                $('#frm_recmaterial2').attr('action', '../../../arap/payable/payable_view.jsp');
                $('#frm_recmaterial2').submit();
            });
            
        }
        
        function editPayments(){
            $('.editPayments').click(function(){              
                var oidPayment=$(this).data('oidpayment');
                var oidPaymentDetail=$(this).data('oidpaymentdetail');
                var oidCurrency=$(this).data('oidcurrency');
                var codeRec=$(this).data('coderec');
                var oidMatReceive1=$(this).data('oidmatreceive1');
                var oidPurchaseorder2=$(this).data('oidpurchaseorder2');
                
                $('#commands').val(<%=Command.EDIT%>);               
                $('#oid_payment_selected').val(oidPayment);
                $('#oid_payment_detail_selected').val(oidPaymentDetail);
                $('#oid_currency_type').val(oidCurrency);
                $('#code_receive').val(codeRec);
                $('#hidden_receive_id').val(oidMatReceive1);
                $('#oid_invoice_selected').val(oidMatReceive1);
                $('#hidden_purchase_order_id').val(oidPurchaseorder2);               
                $('#frm_recmaterial2').attr('action', '../../../arap/payable/payable_view.jsp');
                $('#frm_recmaterial2').submit();        
            });
        }
        
        function getSummaryReceive(){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getSummaryReceive";
            var oidReceive = $('#hidden_receive_id').val(); 
            
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&lang="+language+"";
            ajaxReceives(url,data,"POST","#dynamicRight","getSummaryReceive");
        }
        
        function calcPpn(){
            $("#includePPN").click(function(){
                var check = 0;
                if ($('#includePPN').is(":checked")){
                    check=1;   
                }else{
                    check=0;
                }
                $('#includePpn').val(check);               
            });
            
            $('#valuePPN').change(function(){
                var value = $(this).val();
                $('#totalPpn').val(value);
            });
        }
        
        function getTotalByOidReceiveMaterial(){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getTotalByOidReceiveMaterial";
            var oidReceive = $('#hidden_receive_id').val(); 
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"";
            ajaxReceives(url,data,"POST","","getTotalByOidReceiveMaterial");
        }
        
        function getItemBySkuOrName(search){
            var url = ""+base+"/AjaxReceives"; 
            var command = "<%=Command.GET%>";
            var loadType = "getItemBySkuOrName";
            var oidReceive = $('#hidden_receive_id').val(); 
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&search="+search+"";
            ajaxReceives(url,data,"POST","","getItemBySkuOrName");
        }
        
        function apendDataSearch(data){
            var privShowQtyPrice=$('#privShowQtyPrice').val();
            var tempData = data.split("==");
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>').val(tempData[0]);
            $('#matCode').val(tempData[1]);
            $('#txt_materialname').val(tempData[2]);
            $('#matUnit').val(tempData[3]);
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>').val(tempData[4]);
            $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>').val(tempData[5]);
            
            if (privShowQtyPrice==true){
               $('#matCode').focus(); 
            }else{
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>').focus();
            }
        }
        
        function formAddKeyboard(){
            $('#matCode').keydown(function(e){
                if (e.keyCode==13){
                    var value = $(this).val();
                    if (value.length>0){
                        getItemBySkuOrName(value);
                    }
                }else if (e.keyCode==113){
                    $('.loadListItem').trigger('click');
                }else if (e.keyCode==38){
                    //$('.listOpname .firstFocus').focus();
                    //$('.listOpname .lastFocus').focus();
                }else if (e.keyCode==39){
                    //$('#materialInput').focus();
                }
            });
            
            $('#FRM_FIELD_QTY_INPUT').keydown(function(e){
                if (e.keyCode==13){
                    var value = $(this).val();
                    if (value.length>0){
                        $('#FRM_FIELD_UNIT_ID_KONVERSI').focus();
                    }
                }
            });
            
            $('#FRM_FIELD_UNIT_ID_KONVERSI').keydown(function(e){
                if (e.keyCode==13){
                    var value = $(this).val();
                    if (value.length>0){
                        $('#FRM_FIELD_COST').focus();
                    }
                }
            });
            
            $('#FRM_FIELD_COST').keydown(function(e){
                if (e.keyCode==13){
                    var value = $(this).val();
                    if (value.length>0){
                        $('#FRM_FIELD_DISCOUNT').focus();
                    }
                }
            });
            
            $('#FRM_FIELD_DISCOUNT').keydown(function(e){
                if (e.keyCode==13){
                    $('#FRM_FIELD_DISCOUNT2').focus();   
                }
            });
            
            $('#FRM_FIELD_DISCOUNT2').keydown(function(e){
                if (e.keyCode==13){
                    $('#FRM_FIELD_DISC_NOMINAL').focus();                   
                }
            });
            
            $('#FRM_FIELD_DISC_NOMINAL').keydown(function(e){
                if (e.keyCode==13){
                    $('#FRM_FIELD_FORWARDER_COST').focus();                  
                }
            });
            
            $('#FRM_FIELD_FORWARDER_COST').keydown(function(e){
                if (e.keyCode==13){
                    $('#FRM_FIELD_QTY').focus();                  
                }
            });
            
            $('#FRM_FIELD_QTY').keydown(function(e){
                if (e.keyCode==13){
                    $('.saveReceiveItem').focus();                   
                }
            });
            
            
            
        }
        
        function btnDeleteReceiveConfirmation(){
            $('#btnDeleteReceiveConfirmation').click(function(){
                var url = ""+base+"/AjaxReceives";
                var command = "<%=Command.DELETE%>";
                var loadType= "deleteReceiving";
                var oidReceive = $('#hidden_receive_id').val();               
                var userName = $("userName").val();
                var userId = $("userId").val();
                var  data = "&oidReceive="+oidReceive+"&loadType="+loadType+"&command="+command+"&userName="+userName+"&userId="+userId+"";               
                ajaxReceives(url,data,"POST","","btnDeleteReceiveConfirmation");
            });
        }
        
        function hidePo(){
            $('#checkBoxPo').prop('checked', false);
            $('#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>').val("");
            $('#noPo').val(""); 
            $('#searchPO').attr('disabled','disabled');
            $('#POControl').hide();
        }
        
        function showPo(){
            var values = $('#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>').val();
            if (values.length==0){
                $('#checkBoxPo').prop('checked', false);
                $('#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>').val("");
                $('#noPo').val(""); 
                $('#searchPO').attr('disabled','disabled');
                $('#POControl').show();
            }else{
                $('#checkBoxPo').prop('checked', true);
                $('#searchPO').removeAttr('disabled');
                $('#POControl').show();
            }
            //$('#checkBoxPo').prop('checked', false);
            //$('#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>').val("");
            //$('#noPo').val(""); 
            //$('#searchPO').attr('disabled','disabled');
            //$('#POControl').show();
        }
        
        function changeSuplier(){
            $('#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>').change(function(){
                checkChangeSuplier();
            });
        }
        
        function checkChangeSuplier(){
            
            var value = $("#<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID]%>").val();             

            if (value==0){
                hidePo();
            }else{
                showPo();
            }
          
        }
        
        function checkBoxPo(){
            $("#checkBoxPo").click(function(){
                if ($('#checkBoxPo').is(":checked")){
                    $('#searchPO').removeAttr('disabled');
                }else{
                    $('#searchPO').attr('disabled','disabled');
                }
            });
        }
        
        function getListPoBySuplier(){
            var url = ""+base+"/AjaxReceives";
            var command = "<%=Command.LIST%>";
            var loadType= "getListPoBySuplier";
            var oidVendor= $('#FRM_FIELD_SUPPLIER_ID').val();
            var monthOfPO = $('#month_combo').val();
            var yearOfPO = $('#year_combo').val();          
            var data = "command="+command+"&loadType="+loadType+"&oidVendor="+oidVendor+"&monthOfPO="+monthOfPO+"&yearOfPO="+yearOfPO+"";               
            ajaxReceives(url,data,"POST","#purchaseOrderListPlace","getListPoBySuplier");
            
        }
        
        function searchPo(){
            $('#searchPO').click(function(){
                getListPoBySuplier();
                $('#purchaseOrderList').modal('show');
            });
        }
        
        function month_combo(){
            $('#month_combo').change(function(){
                getListPoBySuplier();
            });
        }
        
        function year_combo(){
            $('#year_combo').change(function(){
                getListPoBySuplier();
            });
        }
        
        function selectPo(){
            $('.selectPo').click(function(){
                var poCode = $(this).data('pocode');
                var pooid = $(this).data('pooid');
                
                $('#noPo').val(poCode);
                $('#FRM_FIELD_PURCHASE_ORDER_ID').val(pooid);
                $('#purchaseOrderList').modal('hide');
            });
        }
        
        function getButtonAddAll(){
            var url = ""+base+"/AjaxReceives";
            var command = "<%=Command.GET%>";
            var loadType= "getButtonAddAll";
            var oidReceiving = $('#hidden_receive_id').val();               
            var data = "command="+command+"&loadType="+loadType+"&oidReceiving="+oidReceiving+"&language="+language+"";               
            //alert(data);
            ajaxReceives(url,data,"POST","#dynamicButtonContainer","getButtonAddAll");
        }
        
        function getAddAllItem(){
            var url = ""+base+"/AjaxReceives";
            var oidReceive = $("#hidden_receive_id").val();          
            var privShowQtyPrice = $("#privShowQtyPrice").val();
            var bEnableExpiredDate = $("#bEnableExpiredDate").val();
            var command = "<%=Command.GET%>";
            var loadType = "getAddAllItem";
            var data = "command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"&privShowQtyPrice="+privShowQtyPrice+"&bEnableExpiredDate="+bEnableExpiredDate+"";
            ajaxReceives(url,data,"POST","#dynamicContainer","getAddAllItem");
        }
        
        function addAllItem(){
            $('#addAllItem').click(function(){
                getAddAllItem();
            });
        }
        
        function cancelAddAllItem(){
            $('#cancelAddAllItem').click(function(){
                getListReceiveItem();
                getListReceiveItemBonus();
            });
        }
        
        function saveAllItem(){
            $('#saveAllItem').click(function(){
                var url = ""+base+"/AjaxReceives";
                var oidReceive = $("#hidden_receive_id").val(); 
                var loadType = "saveAllItem";
                var command = "<%=Command.SAVE%>";
                var data = $('#formAdd').serialize();
                data = data + "&command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"";
                ajaxReceives(url,data,"POST","","saveAllItem");
            });
        }
        
        function loadListItemPoModal(){          
            var url = ""+base+"/AjaxReceives";
            var oidReceive = $("#hidden_receive_id").val();
            var lang = language;
            var privShowQtyPrice = $("#privShowQtyPrice").val();
            var command = "<%=Command.LIST%>";
            var loadType = "getListItemModalPo";
            var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterial="+oidReceive+"&privShowQtyPrice="+privShowQtyPrice+"&lang="+lang+"";
            ajaxReceives(url,data,"POST","#dynamicModalPoItem","loadListItemPoModal");
        }
        
        function loadListItemPo(){
            $('.loadListItemPo').click(function(){
                loadListItemPoModal();
                $('#modalListPoItem').modal('show');
            });
        }
        
        function selectItemByPo(){
            $('.selectItemByPo').click(function(){
               
                var materialId= $(this).data('materialid');
                var matSku = $(this).data('matsku');
                var materialName = $(this).data('materialname');
                var unitCode = $(this).data('unitcode');
                var orgBuyingRate = $(this).data('orgbuyingrate');
                var unitOid = $(this).data('unitoid'); 
                var getQtyResidu = $(this).data('getqtyresidu');
                var currencyId = $(this).data('currencyid');
                var curencyCode = $(this).data('curencycode');
                var totaly = $(this).data('totaly');
                var unitRequestId = $(this).data('unitrequestid');
                var priceKonv = $(this).data('pricekonv');
                var bonus = $(this).data('bonus');
                
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>-0').val(materialId);
                $('#matCode').val(matSku);
                $('#matItem').val(materialName);
                $('#matUnit').val(unitCode);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>-0').val(unitOid);	
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>-0').val(currencyId);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-0').val(orgBuyingRate);		
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-0').val(getQtyResidu);
                $('#FRM_FIELD_RESIDUE_QTY-0').val(getQtyResidu);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-0').val(totaly);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>_0').val(unitRequestId);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>-0').val(priceKonv);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>-0').val(bonus);
                if (bonus==1){
                    $('#checksBoxs-0').prop('checked',true);
                }else{
                    $('#checksBoxs-0').prop('checked',false);
                }
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-0').val(getQtyResidu);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-0').focus();
                $('#total_cost-0').val(priceKonv);
                $('#modalListPoItem').modal('hide');

            });
        }
        
        function saveReceiveItemPo(){
            $('.saveReceiveItemPo').click(function(){
                
                var url = ""+base+"/AjaxReceives";
                var oidReceive = $("#hidden_receive_id").val(); 
                var loadType = "saveReceiveItemPo";
                var command = "<%=Command.SAVE%>";
                var data = $('#formAdd').serialize();
                data = data + "&command="+command+"&loadType="+loadType+"&oidReceive="+oidReceive+"";
                ajaxReceives(url,data,"POST","","saveReceiveItemPo"); 
            });
            
        }
        
        function checksBoxs(){
            $('.checksBoxs').click(function(){
                var id = $(this).attr('id');
                var temp = id.split("-");
                var ids = temp[1];
                var check= 0;
                if ($('.checksBoxs').is(":checked")){
                    check=1;   
                }else{
                    check=0;
                }
                $('#FRM_FIELD_BONUS-'+ids+'').val(check);
            });
        }
        
        function editItemPo(){
            $('.editItemPo').click(function(){
                var affect="";
                var id = $(this).attr('id');
                var temp = id.split("-");
                var bonus = $(this).data('bonus');
                
                if (bonus==1){
                    affect = "#receiveBonusPo-"+temp[1]+"";
                }else{
                    affect = "#receivePo-"+temp[1]+"";
                }
                $('#addForm').remove();
                $('.editItemPo').remove();
                $('.deleteItem').remove();
                
                var oidReceiveMaterialItem = $(this).data('recitem');
                var oidReceiveMaterial = $("#hidden_receive_id").val();
                var orderData = temp[1];
                var privManageData = $("#privManageData").val();
                var privShowQtyPrice = $("#privShowQtyPrice").val();
                var receiveStatus = $("#receiveStatus").val();
                var bEnableExpiredDate = $("#bEnableExpiredDate").val();
                var lang = language;
                var readonlyQty = $('#readonlyQty').val();
                
                var url = ""+base+"/AjaxReceives"; 
                var command = "<%=Command.GET%>";
                var loadType = "showEditControlPo";
                
                var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterialItem="+oidReceiveMaterialItem+"&oidReceiveMaterial="+oidReceiveMaterial+"&orderData="+orderData+"&privManageData="+privManageData+"&privShowQtyPrice="+privShowQtyPrice+"&receiveStatus="+receiveStatus+"&bEnableExpiredDate="+bEnableExpiredDate+"&lang="+lang+"&readonlyQty="+readonlyQty+"&bonus="+bonus+"";
               
                ajaxReceives(url,data,"POST",affect,"editItemPo");
            });
          
        }
        
        function saveEditReceiveItemPo(){
            $('.saveEditReceiveItemPo').click(function(){
                var bonus = $(this).data('bonus');
                var data = "";
                if (bonus=="1"){
                    data = $('#formAddBonus').serialize();
                }else{
                    data = $('#formAdd').serialize();
                }
                var url = ""+base+"/AjaxReceives"; 
                var command = "<%=Command.SAVE%>";
                var loadType = "saveEditReceiveItemPo";
                var userName = $('#userName').val();
                var userId = $('#userId').val();
                var oidReceiveMaterial = $("#hidden_receive_id").val(); 
                var FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID = $("#FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID-0").val();
                var FRM_FIELD_MATERIAL_ID = $('#FRM_FIELD_MATERIAL_ID-0').val();
                var FRM_FIELD_EXPIRED_DATE = $('#FRM_FIELD_EXPIRED_DATE-0').val();
                var FRM_FIELD_QTY_INPUT = $('#FRM_FIELD_QTY_INPUT-0').val();
                var FRM_FIELD_UNIT_ID_KONVERSI =  $('#FRM_FIELD_UNIT_ID_KONVERSI_0').val();
                var FRM_FIELD_UNIT_ID = $('#FRM_FIELD_UNIT_ID-0').val();
                var FRM_FIELD_PRICE_KONVERSI = $('#FRM_FIELD_PRICE_KONVERSI-0').val();
                var FRM_FIELD_COST= $('#FRM_FIELD_COST-0').val();
                var FRM_FIELD_CURRENCY_ID = $('#FRM_FIELD_CURRENCY_ID').val();
                var FRM_FIELD_DISCOUNT= $('#FRM_FIELD_DISCOUNT-0').val();
                var FRM_FIELD_DISCOUNT2= $('#FRM_FIELD_DISCOUNT2-0').val();
                var FRM_FIELD_DISC_NOMINAL = $('#FRM_FIELD_DISC_NOMINAL-0').val();
                var FRM_FIELD_FORWARDER_COST = $('#FRM_FIELD_FORWARDER_COST-0').val();
                var FRM_FIELD_QTY = $('#FRM_FIELD_QTY-0').val();
                var FRM_FIELD_RESIDUE_QTY = $('#FRM_FIELD_RESIDUE_QTY-0').val();
                var FRM_FIELD_BONUS = $('#FRM_FIELD_BONUS-0').val();
                var FRM_FIELD_TOTAL = $('#FRM_FIELD_TOTAL-0').val();
                data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterial="+oidReceiveMaterial+"&userName="+userName+"&userId="+userId+"&FRM_FIELD_MATERIAL_ID="+FRM_FIELD_MATERIAL_ID+"&FRM_FIELD_EXPIRED_DATE="+FRM_FIELD_EXPIRED_DATE+"&FRM_FIELD_QTY_INPUT="+FRM_FIELD_QTY_INPUT+"&FRM_FIELD_UNIT_ID_KONVERSI="+FRM_FIELD_UNIT_ID_KONVERSI+"&FRM_FIELD_UNIT_ID="+FRM_FIELD_UNIT_ID+"&FRM_FIELD_PRICE_KONVERSI="+FRM_FIELD_PRICE_KONVERSI+"&FRM_FIELD_COST="+FRM_FIELD_COST+"&FRM_FIELD_CURRENCY_ID="+FRM_FIELD_CURRENCY_ID+"&FRM_FIELD_DISCOUNT="+FRM_FIELD_DISCOUNT+"&FRM_FIELD_DISCOUNT2="+FRM_FIELD_DISCOUNT2+"&FRM_FIELD_DISC_NOMINAL="+FRM_FIELD_DISC_NOMINAL+"&FRM_FIELD_FORWARDER_COST="+FRM_FIELD_FORWARDER_COST+"&FRM_FIELD_QTY="+FRM_FIELD_QTY+"&FRM_FIELD_RESIDUE_QTY="+FRM_FIELD_RESIDUE_QTY+"&FRM_FIELD_BONUS="+FRM_FIELD_BONUS+"&FRM_FIELD_TOTAL="+FRM_FIELD_TOTAL+"&FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID="+FRM_FIELD_RECEIVE_MATERIAL_ITEM_ID+"";
                
                ajaxReceives(url,data,"POST","","saveEditReceiveItemPo");
            });
        }
        
        function changeUnitIdKonv(){
            $('.changeUnitIdKonv').change(function(){
                var id = $(this).attr('id');
                var temp = id.split("_");
                var ids = temp[5];
                cntTotal2(ids);
            });
        }
        
        function matCodes(){
            $('.matCodes').keydown(function(e){
                if (e.keyCode==13){
                    var value = $(this).val();
                    if (value.length>0){
                        getPoBySkuOrName(value);
                    }
                }
            });
        }
        
        function getPoBySkuOrName(value){
            var url = ""+base+"/AjaxReceives";
            var oidReceive = $("#hidden_receive_id").val();
            var lang = language;
            var privShowQtyPrice = $("#privShowQtyPrice").val();
            var command = "<%=Command.LIST%>";
            var loadType = "getListItemModalPoBySkuName";
            var data = "command="+command+"&loadType="+loadType+"&oidReceiveMaterial="+oidReceive+"&privShowQtyPrice="+privShowQtyPrice+"&search="+value+"";
            ajaxReceives(url,data,"POST","","getPoBySkuOrName");
        }
        
        function apendFromGetPo(data){
            if (data!=""){
                var temp = data.split(":");
                
                var materialId= temp[0];
                var matSku = temp[1];
                var materialName = temp[2];
                var unitCode = temp[3];
                var orgBuyingRate = temp[4];
                var unitOid = temp[5]; 
                var getQtyResidu = temp[6];
                var currencyId = temp[7];
                var curencyCode = temp[8];
                var totaly = temp[9];
                var unitRequestId = temp[10];
                var priceKonv = temp[11];
                var bonus = temp[12];
                
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_MATERIAL_ID]%>-0').val(materialId);
                $('#matCode').val(matSku);
                $('#matItem').val(materialName);
                $('#matUnit').val(unitCode);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID]%>-0').val(unitOid);	
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_CURRENCY_ID]%>-0').val(currencyId);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>-0').val(orgBuyingRate);		
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>-0').val(getQtyResidu);
                $('#FRM_FIELD_RESIDUE_QTY-0').val(getQtyResidu);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>-0').val(totaly);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_UNIT_ID_KONVERSI]%>_0').val(unitRequestId);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_PRICE_KONVERSI]%>-0').val(priceKonv);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_BONUS]%>-0').val(bonus);
                if (bonus==1){
                    $('#checksBoxs-0').prop('checked',true);
                }else{
                    $('#checksBoxs-0').prop('checked',false);
                }
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-0').val(getQtyResidu);
                $('#total_cost-0').val(priceKonv);
                $('#<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY_INPUT]%>-0').focus();
            }
        }
        
        function editItems(){
            $('.editItems').click(function(){
                var oid = $(this).data('oid');
                var name = $(this).data('name');
                var code = $(this).data('code');

                var strvalue  = "<%=approot%>/master/material/material_main.jsp?command=<%=Command.EDIT%>"+
                    "&hidden_material_id="+oid+
                    "&mat_code="+code+
                     "&txt_materialname="+name;
                    window.open(strvalue,"material", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                     
            });
        }
        
        function general(){
            $("#btnback").click(function(){
                window.location="search_receive_material.jsp";
            });
            
            $("#btnHistoryTable").click(function(){
               var oidReceive = $("#hidden_receive_id").val();
               var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                "&oidDocHistory="+oidReceive+"";
                window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
 
            });
            
            $("#btnPrintReceiving").click(function(){
                var typePrint = $("#type_print_tranfer").val();
                var oidReceive = $("#hidden_receive_id").val();
                window.open("receive_wh_supp_material_print_form.jsp?hidden_receive_id="+oidReceive+"&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");                
            });
            
            $("#btnPrintBarcode").click(function(){
                var oidReceive = $("#hidden_receive_id").val();
                var con = confirm("Print Barcode ? ");
                if (con ==true){
                    window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcode?hidden_receive_id="+oidReceive+"&command=<%=Command.EDIT%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    
                } 
            });  
            
            $('#addNewSupplier').click(function(){
                var windowSupplier=window.open("../../../master/contact/contact_company_edit.jsp?command=<%=Command.ADD%>&source_link=receive_wh_supp_material_edit.jsp","add_supplier", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                if (window.focus) { 
                    windowSupplier.focus()
                ;}
            });
            
            
        }
        
        //
        year_combo();
        month_combo();
        searchPo();
        general();
        checkChangeSuplier();
        checkBoxPo();
        changeSuplier();
        btnDeleteReceiveConfirmation();
        btnDeleteForwardConfirmation();
        btnDeleteItemConfirmation();
        showAllGood();
        searchModal();
        namaBarangChange();
        skuModalChange();
        groupItemModalChange();
        getListError();
        getCodeReceive();
        getSaveButton();
        getDeleteButton();
        getListReceiveItem();
        getListReceiveItemBonus();
        getListCurrency();
        getListStatus();
        $('#datetime').daterangepicker({
            "showDropdowns": true,
            "singleDatePicker": true,
            "timePicker": true,
            "timePicker24Hour": true,
            "timePickerSeconds": true,
            "autoApply": true,
            "dateLimit": {
                "days": 7
            },
            "locale": {
                "format": "MM/DD/YYYY HH:mm:ss",
                "separator": " - ",
                "applyLabel": "Use",
                "cancelLabel": "Cancel",
                "fromLabel": "From",
                "toLabel": "To",
                "customRangeLabel": "Custom",
                "daysOfWeek": [
                    "Su",
                    "Mo",
                    "Tu",
                    "We",
                    "Th",
                    "Fr",
                    "Sa"
                ],
                "monthNames": [
                    "January",
                    "February",
                    "March",
                    "April",
                    "May",
                    "June",
                    "July",
                    "August",
                    "September",
                    "October",
                    "November",
                    "December"
                ],
                "firstDay": 1
            },
            "autoUpdateInput": true

        }, function(start, end, label) {
          console.log('New date range selected: ' + start.format('YYYY-MM-DD hh:mm:ss') + ' to ' + end.format('YYYY-MM-DD hh:mm:ss') + ' (predefined range: ' + label + ')');
        });
    });
</script>
</head> 

<body style="background-color:beige">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
    <tr>
        <td height="25" ID="TOPTITLE">
          <%@ include file = "../../../main/header.jsp" %>
        </td>
    </tr>
    <tr>
        <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../../../main/mnmain.jsp" %>
        </td>
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
            <form id="frm_recmaterial" name="frm_recmaterial" method="post" action="">
            <input type="hidden" id="command" name="command" value="">
            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
            <input type="hidden" name="start_item" value="<%=startItem%>">
            <input type="hidden" name="rec_type" value="">
            <input type="hidden" name="type_doc" value="">
            <input type="hidden" name="command_item" value="<%=cmdItem%>">
            <input type="hidden" name="approval_command" value="<%=appCommand%>">
            <input type="hidden" id="hidden_receive_id" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
            <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
            <input type="hidden" name="hidden_receive_item_id" value="">
            <input type="hidden" id ="hidden_currency_id" name="hidden_currency_id" value="<%=rec.getCurrencyId()%>">
            <input type="hidden" name="hidden_currency_id" value="<%=oidCurrencyRec%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
            <input type="hidden" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatReceive.SOURCE_FROM_SUPPLIER%>">
            <input type="hidden" id="iCommandPosting" name="iCommandPosting" value="">
            <input type="hidden" id="receiveDate" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>" value="<%=rec.getReceiveDate()%>">
            <input type="hidden" id="receiveTime" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_TIME" value="">
            <input type="hidden" id="privManageData" name="privManageData"  value="<%=privManageData%>">
            <input type="hidden" id ="privShowQtyPrice" name="privShowQtyPrice" value="<%=privShowQtyPrice%>">  
            <input type="hidden" id ="receiveStatus" name="receiveStatus" value="<%=rec.getReceiveStatus()%>">
            <input type="hidden" id="bEnableExpiredDate" name="bEnableExpiredDate" value="<%=bEnableExpiredDate%>">
            <input type="hidden" id="typeOfBusiness" name="typeOfBusiness" value="<%= typeOfBusiness%>">
            <input type="hidden" id="userId" name="userId" value="<%=userId%>">
            <input type="hidden" id="userName" name="userName" value="<%=userName%>">
            <input type="hidden" id="privAdd" name="privAdd" value="<%=privAdd%>">
            <input type="hidden" id="privUpdate" name="privUpdate" value="<%=privUpdate%>">
            <input type="hidden" id="privApprovalFinal" name="privApprovalFinal" value="<%=privApprovalFinal%>" >
            <input type="hidden" id="privApprovalApprove" name="privApprovalApprove" value="<%=privApprovalApprove%>">
            <input type="hidden" id="privDelete" name="privDelete" value="<%=privDelete%>">
            <input type="hidden" id="listErrSize" name="listErrSize" >
            <input type="hidden" id="readonlyQty" name="readonlyQty" value="<%=readonlyQty%>">
            <input type="hidden" id="includePpn" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>" value="<%=rec.getIncludePpn()%>">
            <input type="hidden" id="totalPpn" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%=rec.getTotalPpn()%>">
           
            <br>
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12">
                    <ol class="breadcrumb" style="border: thin solid rgb(221, 221, 221)">
                        <li><%=textListGlobal[SESS_LANGUAGE][0]%></li>
                        <li><%=textListGlobal[SESS_LANGUAGE][1]%></li>
                        <li class="active"><%=textListGlobal[SESS_LANGUAGE][6]%></li>
                        <li class="active"><%=textListGlobal[SESS_LANGUAGE][4]%></li>
                    </ol>
                </div>
            </div>
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12">                  
                    <div class="panel panel-default">
                        <div class="panel-heading" id="oidRecPlaces">
                            &nbsp;
                        </div>
                        <div class="panel-body" id="dynamiontrol">
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][1]%></label></div>
                                    <div class="col-md-9">
                                        <%
                                        Vector obj_locationid = new Vector(1,1);
                                        Vector val_locationid = new Vector(1,1);
                                        Vector key_locationid = new Vector(1,1);
                           
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
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "form-control")%>  
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][8]%></label></div>
                                    <div class="col-md-9">                                        
                                        <div class="input-group"> 
                                            <%
                                                String dateString = null;
                                                SimpleDateFormat sdfr = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                                                
                                                try{
                                                    dateString = sdfr.format(rec.getReceiveDate());
                                                }catch (Exception ex ){
                                                    System.out.println(ex);
                                                }
                                            %>
                                            <input type="text" id="datetime" value="<%= dateString%>"  class="form-control" aria-describedby="sizing-addon1">
                                            <span class="input-group-addon" id="sizing-addon1"><i class="glyphicon glyphicon-calendar"></i></span>
                                        </div>    
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][3]%></label></div>
                                    <div class="col-md-9"> 
                                        <div class="input-group"> 
                                        <%
                                        Vector val_supplier = new Vector(1,1);
                                        Vector key_supplier = new Vector(1,1);
                                        String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                         " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                                         " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                                         " != "+PstContactList.DELETE;
                                        Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                        val_supplier.add("0");
                                        key_supplier.add("");
                                        for(int d=0; d<vt_supp.size(); d++) {
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
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","form-control")%>   
                                        <span id="addNewSupplier" class="input-group-addon btn btn-info" id="sizing-addon1"><i class="glyphicon glyphicon-plus"></i></span>
                                        </div>   
                                    </div>
                                </div>
                                <div id="POControl" class="row" style="margin-top:5px; display:none">
                                    <div class="col-md-3"><label>&nbsp;</label></div>
                                    <div class="col-md-9">
                                        <div class="input-group">
                                            <span class="input-group-addon">
                                                <input id="checkBoxPo" type="checkbox" aria-label="...">
                                                PO
                                            </span>
                                            <input type="hidden" value="<%= rec.getPurchaseOrderId()%>" id="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_PURCHASE_ORDER_ID]%>">
                                            <input readonly value="<%= po.getPoCode()%>" id="noPo" type="text" class="form-control" aria-label="...">
                                            <span class="input-group-btn">
                                                <button disabled="disabled" id="searchPO" class="btn btn-default" type="button">
                                                    <i class="glyphicon glyphicon-search"></i>&nbsp;
                                                </button>
                                            </span>
                                        </div><!-- /input-group -->
                                        
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][6]%></label></div>
                                    <div class="col-md-9">
                                        <input type="text" class="form-control required" id="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>" value="<%=rec.getInvoiceSupplier()%>"  style="text-align:right">
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][4]%></label></div>
                                    <div class="col-md-9" id="dynamicStatus">
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][9]%></label></div>
                                    <div id="dynamicCurency"></div>
                                </div> 
                                    
                            </div>
                            <div class="col-md-4">
                                <div class="row">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][14]%></label></div>
                                    <div class="col-md-9">
                                        <%
                                        Vector val_terms = new Vector(1,1);
                                        Vector key_terms = new Vector(1,1);
                                        for(int d=0; d<PstMatReceive.fieldsPaymentType.length; d++){
                                            val_terms.add(String.valueOf(d));
                                            key_terms.add(PstMatReceive.fieldsPaymentType[d]);
                                        }
                                        String select_terms = ""+rec.getTermOfPayment();
                                        %>
                                        <%=ControlCombo.draw(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TERM_OF_PAYMENT],null,select_terms,val_terms,key_terms,"","form-control")%>

                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][15]%></label></div>
                                    <div class="col-md-9">
                                        <input name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="text" class="form-control" style="text-align:right" size="5" value="<%=rec.getCreditTime()%>"> 
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"><label><%=textListOrderHeader[SESS_LANGUAGE][5]%></label></div>
                                    <div class="col-md-9">
                                        <textarea name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_REMARK]%>" class="form-control" wrap="VIRTUAL" rows="2" tabindex="6"><%=rec.getRemark()%></textarea>
                                    </div>
                                </div>
                                <div class="row" style="margin-top:5px;">
                                    <div class="col-md-3"></div>
                                    <div class="col-md-9">
                                        <span id="saveButtonPlace"></span>
                                        <span id="deleteButtonPlace"></span>
                                    </div>
                                </div>    
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </form>
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">&nbsp;</div>
                        <div class="panel-body">
                            <form name='formAdd' id='formAdd'>
                                <div id="dynamicContainer"></div>
                                <div id="dynamicButtonContainer"></div>
                            </form>
                        </div>
                    </div>
                </div>
            </div> 
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-12" >
                    <div class="panel panel-default">
                        <div class="panel-heading">Bonus Item</div>
                        <div class="panel-body">
                            <form name='formAddBonus' id='formAddBonus'>
                                <div id="dynamicContainerBonus"></div>  
                            </form>
                        </div>
                    </div>
                </div>
            </div> 
            <div class="row" style="margin-right:0px; margin-left:0px;">
                <div class="col-md-3" id="dynamicLeft"></div>
                <div class="col-md-6" id="dynamicCenter"></div>
                <div class="col-md-3" id="dynamicRight"></div>
            </div> 
            <div class="row" style="margin-right:0px; margin-left:0px;margin-bottom:10px; ">
                <div class="col-md-12" >
                    <button type="button" style="margin-right:5px;" class="btn btn-primary pull-left" id="btnback"><i class="glyphicon glyphicon-chevron-left"></i> <%=textPurchaseOrderHeader[SESS_LANGUAGE][3]%></button> 
                    <button type="button" style="margin-right:5px;" class="btn btn-primary pull-left" id="btnHistoryTable"><i class="glyphicon glyphicon-retweet"></i> View History Table</button>
                    <%if(privShowQtyPrice){%>
                    <select id="type_print_tranfer" name="type_print_tranfer" class="form-control pull-left" style="width:200px; margin-right:5px;">
                        <option value="0">Price Cost</option>
                        <option value="1">Sell Price</option>
                    </select>
                    <%}else{%>
                    <input id="type_print_tranfer" type="hidden" name="type_print_tranfer" value="0">
                    <%}%>
                    <button type="button" class="btn btn-primary pull-left" id="btnPrintReceiving" style="margin-right:5px;"><i class="glyphicon glyphicon-print" ></i> <%=textListGlobal[SESS_LANGUAGE][8]%></button>
                    <button type="button" class="btn btn-primary pull-left" id="btnPrintBarcode"><i class="glyphicon glyphicon-barcode"></i> Cetak Barcode</button>
                </div>
            </div> 
        </td> 
    </tr> 
</table>
<!-- DELETE RECEIVE CONFIRMATION-->
          
<div id="modalReport" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][2]%></h4>
                
            </div>
            <div class="modal-body" id="modal-body">
                <div id="modal-body-up">
                    
                    <div class="row">                        
                        <div class="col-md-3"><%=textState[SESS_LANGUAGE][2]%></div>
                        <div class="col-md-9">
                            <select id="matGroupId" name="matGroupId" class="form-control">
                            <option value="0">Semua Category</option>
                            <%
                            Vector masterCatAcak = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);                   
                            String checked="selected";
                            Vector materGroup = PstCategory.structureList(masterCatAcak) ;
                            Vector vectGroupVal = new Vector(1,1);
                            Vector vectGroupKey = new Vector(1,1);

                            if(materGroup!=null && materGroup.size()>0) {
                                String parent="";
                                Vector<Long> levelParent = new Vector<Long>();
                                for(int i=0; i<materGroup.size(); i++) {
                                    Category mGroup = (Category)materGroup.get(i);
                                    if(mGroup.getCatParentId()!=0){
                                        for(int lv=levelParent.size()-1; lv > -1; lv--){
                                            long oidLevel=levelParent.get(lv);
                                            if(oidLevel==mGroup.getCatParentId()){
                                                break;
                                            }else{
                                                levelParent.remove(lv);
                                            }
                                        }
                                        parent="";
                                        for(int lv=0; lv<levelParent.size(); lv++){
                                            parent=parent+"&nbsp;&nbsp;";
                                        }
                                        levelParent.add(mGroup.getOID());

                                    }else{
                                        levelParent.removeAllElements();
                                        levelParent.add(mGroup.getOID());
                                        parent="";
                                    }
                                    out.println("<option value='"+mGroup.getOID()+"'>"+parent+" "+mGroup.getName()+"</option>");
                                }
                            }
                            %>
                            </select>
                        </div>
                        
                    </div>
                    <div class="row" style="margin-bottom:3px;margin-top:4px;">                        
                        <div class="col-md-3"><%= textListOrderItem[SESS_LANGUAGE][1]%></div>
                        <div class="col-md-9"><input id="sku" type='text' class='form-control' placeholder='<%= textListOrderItem[SESS_LANGUAGE][1]%>'></div>
                    </div>
                    
                    <div class="row" style="margin-bottom:3px;">
                        <div class="col-md-3"><%=textListOrderItem[SESS_LANGUAGE][2]%></div>
                        <div class="col-md-9"><input id="namaBarang" type='text' class='form-control' placeholder='<%=textListOrderItem[SESS_LANGUAGE][2]%>'></div>
                    </div>
                    <div class="row" style="margin-bottom:3px;">
                        <div class="col-md-3">                            
                            
                        </div>
                        <div class="col-md-9">                            
                            <label>
                                <input type="checkbox" id="showAllGood" name="showAllGood" value="1">
                                <%=textListGlobal[SESS_LANGUAGE][12]%>
                            </label>
                            <button style="margin-left:5px;" type="button"  class="btn btn-default pull-right searchModal">
                                <i class="glyphicon glyphicon-search"></i> <%=textListGlobal[SESS_LANGUAGE][2]%>
                            </button>
                            <button style="margin-left:5px;" type="button"  class="btn btn-default pull-right addNewModal">
                                <i class="glyphicon glyphicon-plus"></i> <%=textListGlobal[SESS_LANGUAGE][11] %>
                            </button> 
                        </div>
                        
                    </div>
                </div>
                <div id="modal-body-low"></div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- DELETE ITEM CONFIRMATION-->
<div id="deleteReceiveConfirmation" class="modal nonprint">
    <div class="modal-dialog modal-sm" style="max-width:300px;">
        <div class="modal-content">
            <div class="modal-header">
                <input type="hidden" id="oidDetails" value="0">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div class="modal-body">
                <%=textDelete[SESS_LANGUAGE][0]%>
            </div>
            <div class="modal-footer">               
              <button id="btnDeleteItemConfirmation" class="btn btn-primary" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-ok"></i> <%=textListState[SESS_LANGUAGE][2]%></button>
              <button id="btnDeleteItemClose" class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][3]%></button>
            </div>
        </div>
    </div>
</div>
<div id="deleteForwarderConfirmation" class="modal nonprint">
    <div class="modal-dialog modal-sm" style="max-width:300px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div class="modal-body">
                <%=textDelete[SESS_LANGUAGE][0]%>
            </div>
            <div class="modal-footer">               
              <button id="btnDeleteForwardConfirmation" class="btn btn-primary" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-ok"></i> <%=textListState[SESS_LANGUAGE][2]%></button>
              <button  class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][3]%></button>
            </div>
        </div>
    </div>
</div>
<!-- DELETE RECEIVE CONFIRMATION-->
<div id="deleteReceivesConfirmation" class="modal nonprint">
    <div class="modal-dialog modal-sm" style="max-width:300px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div class="modal-body">
                <%=textDelete[SESS_LANGUAGE][0]%>
            </div>
            <div class="modal-footer">               
              <button id="btnDeleteReceiveConfirmation" class="btn btn-primary" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-ok"></i> <%=textListState[SESS_LANGUAGE][2]%></button>
              <button  class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][3]%></button>
            </div>
        </div>
    </div>
</div>
<!-- SHOW MODAL FORWADER-->
<div id="showModalForwader" class="modal nonprint">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <input type="hidden" id="oidDetails" value="0">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b><%=textListState[SESS_LANGUAGE][1]%></b></h4>
            </div>
            <div id="modalForwaderDynamic">
                
            </div>
            
        </div>
    </div>
</div>
<!-- DELETE PURCHASE ORDER-->   
<div id="purchaseOrderList" class="modal nonprint">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">              
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="AUTHORIZE_TITLE"><b>Purchase Order</b></h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4"><%=textPurchaseOrderHeader[SESS_LANGUAGE][0]%></div>
                    <div class="col-md-2">
                        <% 
                            Vector obj_monthid = new Vector(1,1); 
                            Vector val_monthid = new Vector(1,1); 
                            Vector key_monthid = new Vector(1,1); 
                            Date jani = new Date();
                            int bulan_awal = jani.getMonth()+1;
                            for(int d = 1;d < 13; d++)
                            {
                                val_monthid.add(""+d);
                                key_monthid.add(""+d);
                            }
                            String select_monthid = "";
                           
                            select_monthid = ""+bulan_awal; 
                            	
                        %>
                        <%=ControlCombo.draw("month_combo", null, select_monthid, val_monthid, key_monthid, "", "form-control")%>
                    </div>
                    <div class="col-md-6">
                        <% 
                            Vector obj_yearid = new Vector(1,1); 
                            Vector val_yearid = new Vector(1,1); 
                            Vector key_yearid = new Vector(1,1); 
                            Date sekarang = new Date();
                            int tahun_awal = sekarang.getYear() + 1900 -2;
                            for(int d = 0;d < 5; d++)
                            {
                                val_yearid.add(""+(d+tahun_awal));
                                key_yearid.add(""+(d+tahun_awal));
                            }
                            String select_yearid = "";                          
                            select_yearid = ""+(String)key_yearid.get(2);                         	
                      %>
                      <%=ControlCombo.draw("year_combo", null, select_yearid, val_yearid, key_yearid, "", "form-control")%>
				
                    </div>
                </div>
                <div class="row" style="margin-top:10px;" id="purchaseOrderListPlace">
                    
                </div>
            </div>
            <div class="modal-footer">               
              <button class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][7]%></button>
            </div>
        </div>
    </div>
</div>  
<div id="modalListPoItem" class="modal nonprint">
    <div class="modal-dialog modal-lg" style="width:800px;">
        <div class="modal-content">
            <div id="dynamicModalPoItem"></div>
            <div class="modal-footer">               
              <button class="btn btn-danger" type="button" data-dismiss="modal"><i class="glyphicon glyphicon-remove"></i> <%=textListState[SESS_LANGUAGE][7]%></button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
