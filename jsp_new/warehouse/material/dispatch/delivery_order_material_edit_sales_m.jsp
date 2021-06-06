<%-- 
    Document   : delivery_order_material_edit_sales_m
    Created on : Aug 26, 2014, 3:51:08 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.session.warehouse.SessMatReceive"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.pos.entity.payment.PstCashPayment1"%>
<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@page import="com.dimata.pos.entity.payment.CashPayments1"%>
<%@ page import="com.dimata.pos.entity.billing.Billdetail,
                com.dimata.pos.entity.billing.BillDetailCode,
                com.dimata.pos.entity.billing.PstBillMain,
                com.dimata.pos.entity.billing.BillMain,
                com.dimata.pos.entity.payment.CashPayments,
                com.dimata.pos.entity.payment.CashReturn,
                com.dimata.pos.entity.payment.PstCashPayment,
                com.dimata.pos.form.billing.FrmBillMain,
                com.dimata.pos.form.billing.CtrlBillMain,
                com.dimata.pos.entity.search.SrcInvoice,
                com.dimata.pos.form.search.FrmSrcInvoice,
                com.dimata.pos.session.billing.SessBilling,
                com.dimata.posbo.report.sale.SaleReportDocument,
                com.dimata.posbo.entity.masterdata.MemberReg,
                com.dimata.common.entity.payment.CurrencyType,
                com.dimata.common.entity.payment.PstCurrencyType,java.util.*,
                com.dimata.posbo.entity.masterdata.*,
                com.dimata.qdep.form.FRMMessage,
                com.dimata.qdep.form.FRMQueryString,
                com.dimata.gui.jsp.ControlLine,
                com.dimata.util.Command,
                com.dimata.common.entity.location.Location,
                com.dimata.common.entity.location.PstLocation,
		com.dimata.common.entity.contact.*,
                com.dimata.qdep.form.FRMHandler,
                com.dimata.pos.cashier.DefaultSaleModel,
                com.dimata.pos.session.processdata.SessTransactionData,
		com.dimata.pos.entity.billing.*"%>
<%@ page language = "java" %>

<!DOCTYPE html>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!

public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No. Invoice","Tanggal Invoice","Sales Person","Customer ","Kode Customer ","Lokasi Transaksi","(tidak tercatat)",//0-6
	 "No.","SKU ","Nama Item","Serial No.","Harga ","Disc","Qty","Total ","Total Trans","Total Disc","Tax","Service",//7-18
	 "Net Trans","DP","Total Bayar","Kembali","No.","Tipe Pembayaran","Kurs","Jumlah","Tgl Bayar","(tidak tercatat)","Catatan",//19-29
	 "Mata Uang","Status"},//30
	{"Invoice No. ","Bill Date","Sales Name","Customer Name","Customer Code","Sale Location","(uscpecified)",
	 "No.","Item Code","Item Name","Serial No.","Item Price","Disc","Qty","Total Price","Total Trans","Total Disc","Tax","Service",
	 "Net Trans","Last Payment(DP)","Total Payment","Return","No.","Payment Type","Rate","Pay Amount","Pay Date","(unspecified)","Remark",
	 "Currency","Status"}
};
public static final String textListTitle[][] =
{
	{"Invoice ","NOMOR INVOICE","TANGAAL INVOICE","NAMA CUSTOMER","(tidak tercatat)","Cetak Pengiriman Barang","Posting Stock","Posting Harga Beli"},
	{"Invoice ","INVOICE NUMBER","INVOICE DATE","CUSTOMER NAME","(unspecified)","Cetak Pengiriman Barang","Posting Stock","Posting Harga Beli"}
};

public static final String textListTitleHeader[][] =
{
	{"DETAIL INVOICE ","Invoice ","Tidak ada data transaksi ..","Transaksi Invoice","Semua"},
	{"INVOICE DETAIL","Invoice ","No transaction data available ..","Invoice Transaction ","TYPE","All"}
};

public static final String textPosting[][] = {
    {"Anda yakin melakukan Posting Stok ?","Anda yakin melakukan Posting Harga ?"},
    {"Are You Sure to Posting Stock ? ","Are You Sure to Posting Cost Price?"}
};

public Vector drawListBillingItem(int SESS_LANGUAGE,Vector objectClass,int start,boolean privManageData){
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
        
        ctrlist.addHeader(textListMaterialHeader[SESS_LANGUAGE][7],"3%");
        ctrlist.addHeader(textListMaterialHeader[SESS_LANGUAGE][8],"20%");
        ctrlist.addHeader(textListMaterialHeader[SESS_LANGUAGE][9],"20%");
        ctrlist.addHeader(textListMaterialHeader[SESS_LANGUAGE][13],"20%");

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1,1);
        ctrlist.reset();
        int index = -1;
        if(start<0)
        start=0;

        for(int i=0; i<objectClass.size(); i++){
            Billdetail objBillDetail = new Billdetail();
            Material mat = new Material();
            try{
                objBillDetail = (Billdetail) objectClass.get(i);
                mat = PstMaterial.fetchExc(objBillDetail.getMaterialId());
            }catch(Exception ex){
                System.out.print(ex);
                objBillDetail = new Billdetail();
            }
            
            BillDetailCode objBillDetailCode = PstBillDetailCode.getBillDetailCode(objBillDetail.getOID());
            rowx = new Vector();
            start = start + 1;

            rowx.add(""+start+"");
            rowx.add("<div align=\"left\">"+objBillDetail.getSku()+"</div>");
            rowx.add("<div align=\"left\">"+objBillDetail.getItemName()+"</div>");

             if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+"="+objBillDetail.getOID();
                int cnt = PstBillDetailCode.getCount(where);
                double max = objBillDetail.getQty();

                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok produk '"+mat.getName()+"' tidak sama dengan qty terima.");
                }

               rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(objBillDetail.getOID())+"')\">[ST.CD]</a> "+String.valueOf(objBillDetail.getQty())+"</div>");

            }else{
                rowx.add("<div align=\"center\">"+FRMHandler.userFormatStringDecimal(objBillDetail.getQty())+"</div>");
            }
            lstData.add(rowx);
        }
        result = ctrlist.drawBootstrap();
    }else{
        result = "<div class=\"msginfo\">xx</div>";
    }

    list.add(result);
    list.add(listError);
    
    return list;
}

%>


<%
int iCommand = FRMQueryString.requestCommand(request);
long oidBillMainOid = FRMQueryString.requestLong(request, "hidden_billmain_id");
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
int statusDoc= FRMQueryString.requestInt(request, "statusDoc");

int start = FRMQueryString.requestInt(request, "start");
int recordToGet = FRMQueryString.requestInt(request, "recordToGet");
int iCommandPosting = FRMQueryString.requestInt(request,"iCommandPosting");

//update status
if(iCommand==Command.SAVE){
    PstBillMain.updateStatusBillMain(oidBillMainOid, statusDoc);
}



ControlLine ctrLine = new ControlLine();
int iErrCode = FRMMessage.ERR_NONE;
String msgString = "";

BillMain billMain = new BillMain();
try {
	billMain = PstBillMain.fetchExc(oidBillMainOid);
}
catch(Exception e) {
	System.out.println("Exc when get bill detail : " + e.toString());
}

boolean privManageData = true;
if(billMain.getBillStatus()!= I_DocStatus.DOCUMENT_STATUS_DRAFT){
    privManageData=false;
}


String sWhereClause = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + " = " + oidBillMainOid;
Vector records = PstBillDetail.list(start, recordToGet, sWhereClause, "");

String strCurrencyType = "";
if(billMain.getCurrencyId() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+billMain.getCurrencyId();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}

Vector listError = new Vector();
Vector list = drawListBillingItem(SESS_LANGUAGE,records,0,privManageData);
listError = (Vector)list.get(1);

String notePo="";
long oidLocationReq=0;
long oidPurchaseID=0;
Location locationRequest = new Location();
try{
        String[] smartPhonesSplits = billMain.getNotes().split("\\;");
        notePo=smartPhonesSplits[0];
        oidLocationReq=Long.parseLong(smartPhonesSplits[1]);
        oidPurchaseID=Long.parseLong(smartPhonesSplits[2]);
}catch(Exception ex){}

if(oidLocationReq!=0){
   locationRequest= PstLocation.fetchExc(oidLocationReq);
}

/**
add opie-eyek 20131205 untuk posting stock
*/
if(iCommandPosting==Command.POSTING){
    try{
        SessPosting sessPosting = new SessPosting();
        sessPosting.postedSalesTranscaction(oidBillMainOid);
        billMain.setBillStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
        
        //buat dokument penerimaan dengan po
        //SO : QTR-GS-1409-SR-005;504404550369384680;504404568693474589
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        try{
            purchaseOrder=PstPurchaseOrder.fetchExc(oidPurchaseID);
        }catch(Exception ex){
        
        }
        Date rDate = new Date();
        MatReceive matReceive = new MatReceive(); 
        matReceive.setLocationId(oidLocationReq);
        matReceive.setInvoiceSupplier(notePo);
        matReceive.setSupplierId(purchaseOrder.getSupplierId());
        matReceive.setCreditTime(purchaseOrder.getTermOfPayment());
        matReceive.setTransRate(purchaseOrder.getExchangeRate());
        matReceive.setReceiveStatus(0);
        matReceive.setLocationType(0);
        matReceive.setReceiveDate(rDate);
        matReceive.setReceiveSource(1);
        matReceive.setPurchaseOrderId(oidPurchaseID);
        matReceive.setRecCodeCnt(SessMatReceive.getIntCode(matReceive, rDate, 0, 0, 0, true));
        matReceive.setRecCode(SessMatReceive.getCodeReceive(matReceive));
        matReceive.setCurrencyId(purchaseOrder.getCurrencyId());
        matReceive.setTermOfPayment(1);
        
        long oid = PstMatReceive.insertExc(matReceive);
        
        iCommand = Command.EDIT;
        
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}
%>
<html>
    <head>
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
        <link href="../../../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../../../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
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
        
        <script language="JavaScript">
            <!--

            function cmdBack()
            {
                    document.frminvoice.command.value="<%=Command.BACK%>";
                    document.frminvoice.action="src_delivery_order_sales_m.jsp";
                    document.frminvoice.submit();
            }

            function printForm(){
                window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
            }

            function gostock(oid) {
                document.frminvoice.command.value="<%=Command.EDIT%>";
                document.frminvoice.hidden_cash_bill_detail_id.value=oid;
                document.frminvoice.action="delivery_order_stockcode_sale.jsp";
                document.frminvoice.submit();
            }

            function cmdSave(){
                var statusDoc = document.frminvoice.statusDoc.value;
                if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                     var conf = confirm("<%=textPosting[SESS_LANGUAGE][0]%>");
                     if(conf){
                        document.frminvoice.command.value="<%=Command.SAVE%>";
                        document.frminvoice.iCommandPosting.value="<%=Command.POSTING%>";
                        document.frminvoice.hidden_billmain_id.value="<%=oidBillMainOid%>";
                        document.frminvoice.action="delivery_order_material_edit_sales_m.jsp";
                        document.frminvoice.submit();
                     }
                }else{
                    document.frminvoice.command.value="<%=Command.SAVE%>";
                    document.frminvoice.hidden_billmain_id.value="<%=oidBillMainOid%>";
                    document.frminvoice.action="delivery_order_material_edit_sales_m.jsp";
                    document.frminvoice.submit();
                }
            }

            //add opie-eyek 20131205 untuk posting
            function PostingStock() {
                var conf = confirm("Are you sure do Posting Stock ?");
                if(conf){
                    document.frminvoice.command.value="<%=Command.POSTING%>";
                    document.frminvoice.action="delivery_order_material_edit_sales_m.jsp";
                    document.frminvoice.submit();
                    }
            }

            //add opie-eyek 20131205 untuk posting
            function PostingCostPrice() {
                var conf = confirm("Are you sure do Posting Cost Price?");
                if(conf){
                    document.frminvoice.command.value="<%=Command.REPOSTING%>";
                    document.frminvoice.action="delivery_order_material_edit_sales.jsp";
                    document.frminvoice.submit();
                    }
            }
            
            function cmdPrinInvoice(){
                window.open("<%=approot%>/cashieronline/print_invoice.jsp?oidBillMain=<%=oidBillMainOid%>","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=yes,menubar=yes,location=no");
            }
            
            function printDeliveryOrder()
            {
                var typePrint = 0;//document.frm_matdispatch.type_print_tranfer.value;
                window.open("delivery_order_material_print_form.jsp?command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"&timemls=1&hidden_bill_main_id=<%=oidBillMainOid%>","pireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }
            //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

            //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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

            //-->
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
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frminvoice" method ="post" action=""  role="form">
                        <!--form hidden -->
                          <input type="hidden" name="command" value="">
                          <input type="hidden" name="start" value="<%=start%>">
                          <input type="hidden" name="recordToGet" value="<%=recordToGet%>">
                          <input type="hidden" name="approval_command">
                          <input type="hidden" name="hidden_billmain_id" value="<%=oidBillMainOid%>">
                          <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">
                          <input type="hidden" name="hidden_cash_bill_detail_id" value="">
                          <input type="hidden" name="iCommandPosting" value="">
                        <!--body-->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-12">
                                    <!-- title row -->
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <h2 class="page-header">
                                                <i class="fa fa-globe"></i><%=userName%>
                                                <small class="pull-right">Date: <%=Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy : HH-mm-SS")%></small>
                                            </h2>                            
                                        </div><!-- /.col -->
                                    </div>
                                    
                                    <!-- info row -->
                                    <div class="row invoice-info">
                                        <div class="col-sm-4 invoice-col">
                                            From
                                            <address>
                                                <%
                                                    Location location = new Location();
                                                    try{
                                                       location = PstLocation.fetchExc(billMain.getLocationId());
                                                   }catch(Exception ex){}
                                                %>
                                                <strong><%=location.getName()%></strong><br>
                                                Phone: <%=location.getTelephone()%><br/>
                                                Email: <%=location.getEmail()%>
                                            </address>
                                        </div><!-- /.col -->
                                         <%
                                               /* String custName = "";
                                                String custCode = "";
                                                ContactList contactlist = new ContactList();
                                                try
                                                {
                                                        contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                                                        custName = contactlist.getPersonName();
                                                        custCode = contactlist.getContactCode();
                                                }
                                                catch(Exception e)
                                                {
                                                        //System.out.println("Contact not found ...");
                                                }*/
                                          %>
                                        <div class="col-sm-4 invoice-col">
                                            To
                                            <address>
                                               <strong><%=locationRequest.getName()%></strong><br>
                                                Phone: <%=locationRequest.getTelephone()%><br/>
                                                Email: <%=locationRequest.getEmail()%>
                                            </address>
                                        </div><!-- /.col -->
                                        <div class="col-sm-4 invoice-col">
                                             <b>Invoice #</b><%=billMain.getInvoiceNumber()%>
                                             <address>
                                             <b>SR #</b><%=notePo%><br/>
                                             <b><%=textListMaterialHeader[SESS_LANGUAGE][31]%> : </b>
                                             <%
                                                Vector obj_status = new Vector(1,1);
                                                Vector val_status = new Vector(1,1);
                                                Vector key_status = new Vector(1,1);

                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                //add by fitra
                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                if(listError.size()==0){
                                                    val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                    key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                }
                                                String select_status = ""+billMain.getBillStatus();
                                                if(billMain.getBillStatus()==I_DocStatus.DOCUMENT_STATUS_CLOSED){
                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                }else if(billMain.getBillStatus()==I_DocStatus.DOCUMENT_STATUS_POSTED){
                                                    out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                }else{
                                                %>
                                                    <%=ControlCombo.draw("statusDoc",null,select_status,val_status,key_status,"","form-control-date-medium")%>
                                                <% } %>
                                            </address>
                                        </div><!-- /.col -->
                                    </div><!-- /.row -->
                                </div>
                                <div class="col-xs-12">
                                    <%=list.get(0)%> 
                                </div>
                                <div class="col-xs-12">
                                     <div class="box-footer">
                                            <%if(billMain.getBillStatus()!=I_DocStatus.DOCUMENT_STATUS_CLOSED){%>
                                                <button class="btn btn-danger pull-left" style="margin-right: 5px;" onclick="javascript:cmdSave()" type="button"><i class="fa fa-download"></i> Save</button>
                                            <%}%>
                                            <button class="btn btn-warning pull-left" style="margin-right: 5px;" onclick="javascript:cmdBack()" type="button"><i class="fa fa-download"></i> Back</button>
                                    
                                      <%if(oidBillMainOid!=0){%>
                                        <button class="btn btn-primary pull-right" style="margin-right: 5px;" onclick="javascript:cmdPrinInvoice()" type="button"><i class="fa fa-download"></i>Invoice</button>
                                     <%}%>
                                     </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

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
