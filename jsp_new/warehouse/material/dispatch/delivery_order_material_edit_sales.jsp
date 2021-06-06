<%-- 
    Document   : delivery_order_material_edit_sales
    Created on : Dec 9, 2013, 10:35:14 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
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
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
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
            rowx.add("<div align=\"right\">"+objBillDetail.getSku()+"</div>");
            rowx.add("<div align=\"right\">"+objBillDetail.getItemName()+"</div>");

             if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+"="+objBillDetail.getOID();
                int cnt = PstBillDetailCode.getCount(where);
                double max = objBillDetail.getQty();
                /*double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;*/

                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                    listError.add(""+listError.size()+". Jumlah serial kode stok produk '"+mat.getName()+"' tidak sama dengan qty terima.");
                }

               rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(objBillDetail.getOID())+"')\">[ST.CD]</a> "+String.valueOf(objBillDetail.getQty())+"</div>");

            }else{
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objBillDetail.getQty())+"</div>");
            }
            lstData.add(rowx);
        }
        result = ctrlist.draw();
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
AppUser ap = new AppUser();
try {
	billMain = PstBillMain.fetchExc(oidBillMainOid);
   ap = PstAppUser.fetch(billMain.getAppUserId());
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


/**
add opie-eyek 20131205 untuk posting stock
*/
if(iCommandPosting==Command.POSTING){
    try{
        SessPosting sessPosting = new SessPosting();
        sessPosting.postedSalesTranscaction(oidBillMainOid);
        billMain.setBillStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
        iCommand = Command.EDIT;
    }catch(Exception e){
        iCommand = Command.EDIT;
    }
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdBack()
{
	document.frminvoice.command.value="<%=Command.BACK%>";
	document.frminvoice.action="delivery_order_list_sales.jsp";
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
            document.frminvoice.action="delivery_order_material_edit_sales.jsp";
            document.frminvoice.submit();
         }
    }else{
        document.frminvoice.command.value="<%=Command.SAVE%>";
        document.frminvoice.hidden_billmain_id.value="<%=oidBillMainOid%>";
        document.frminvoice.action="delivery_order_material_edit_sales.jsp";
        document.frminvoice.submit();
    }
}

//add opie-eyek 20131205 untuk posting
function PostingStock() {
    var conf = confirm("Are you sure do Posting Stock ?");
    if(conf){
        document.frminvoice.command.value="<%=Command.POSTING%>";
        document.frminvoice.action="delivery_order_material_edit_sales.jsp";
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
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Pengiriman Barang > Cari Pengiriman Barang<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frminvoice" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="recordToGet" value="<%=recordToGet%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="<%=oidBillMainOid%>">
              <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">
              <input type="hidden" name="hidden_cash_bill_detail_id" value="">
              <input type="hidden" name="iCommandPosting" value="">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td height="127" valign="top">
                    <table width="100%" border="0" cellspacing="1">
                      <tr>
                        <td align="left" valign="top">
                           <table border="0" cellspacing="1">
                            <tr>
                           <td>
                            <table width="100%" border='0'>
                                <tr>
                                  <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][0]%></td>
                                  <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" >
                                    <div align="left"><%=billMain.getInvoiceNumber()%></div></td>
                                </tr>
                                <tr>
                                  <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][1]%></td>
                                  <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" ><%=Formater.formatDate(billMain.getBillDate(),"dd-MMM-yyyy : HH-mm-SS")%></td>
                                </tr>
                                <tr>
                                    <td align="left"><%=textListMaterialHeader[SESS_LANGUAGE][31]%></td>
                                    <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" >
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
                                        <%=ControlCombo.draw("statusDoc",null,select_status,val_status,key_status,"","formElemen")%>
                                        </td>
                                    <% } %>
                              </tr>
                         </table>
                           </td>
                           <td width='10%'>&nbsp;</td>
                           <td>
                            <table width="100%" border='0'>
                            <tr>
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][3]%></td>
                              <td align="left" valign="top" > : </td>
                              <%
                                    String custName = "";
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
                                    }
                              %>
                              <td align="left" valign="top" ><%=custName%></td>
                            </tr>
                            <tr>
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][4]%></td>
                              <td align="left" valign="top" > : </td>
                              <td align="left" valign="top" ><%=custCode%></td>
                            </tr>
                            <tr>
                              <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][5]%></td>
                              <td align="left" valign="top" > : </td>
                              <%
                                String locationname = "";
                                Location location = new Location();
                                try
								{
                                   location = PstLocation.fetchExc(billMain.getLocationId());
                                   locationname = location.getName();
                                }
								catch(Exception e)
								{
                                   locationname = textListMaterialHeader[SESS_LANGUAGE][6];
                                }
                              %>
                              <td align="left" valign="top" ><%=locationname %></td>
                            </tr>
                            </table>
                           </td>
                           <td width='10%'>&nbsp;</td>
                           <td valign="top">
                                <table width="100%" border="0">
                                  <tr>
                                    <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][2]%></td>
                                    <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" ><%=ap.getFullName() %></td>
                                  </tr>
                                                              <tr>
                                    <td align="left" valign="top" ><%=textListMaterialHeader[SESS_LANGUAGE][29]%></td>
                                    <td align="left" valign="top" > : </td>
                                    <td align="left" valign="top" ><%=billMain.getNotes()%></td>
                                  </tr>
                                </table>
                           </td>
                           </tr>
                          </table></td>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                         <td colspan="2" align="left" valign="top"><%=list.get(0)%></td>
                      </tr>
                      <tr align="left" valign="top">
                        <td height="22" colspan="3" valign="middle" class="errfont">
                            <span class="command">
                              <%
                              for(int k=0;k<listError.size();k++){
                                  if(k==0)
                                      out.println("<img src='../../../images/DOTreddotANI.gif'>Document Tidak Bisa di Finalkan, Silahkan Isi Serial Number Terlebih Dahulu <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                                  else
                                      out.println("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+listError.get(k)+"<br>");
                              }
                              %>
                              </span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top"> <table width="100%" border="0">
                      <tr>
                        <td width="61%"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          </table>
                              <%
                                    ctrLine.setLocationImg(approot+"/images");
                                    ctrLine.initDefault(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0]);
                                    ctrLine.setTableWidth("100%");
                                    ctrLine.setSaveImageAlt("Save");
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_BACK,true)+" List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_ASK,true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE,textListTitle[SESS_LANGUAGE][0],ctrLine.CMD_CANCEL,false));

                                    ctrLine.setSaveCaption("Save");

                                    if(privDelete && privManageData){
                                        ctrLine.setConfirmDelCommand("");
                                        ctrLine.setDeleteCommand("");
                                        ctrLine.setEditCommand("");
                                    }else{
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                    }

                                    if(listError.size()>0){
                                        ctrLine.setSaveCaption("");
                                    }

                                    if(privAdd==false){
                                        ctrLine.setAddCaption("");
                                    }

                                    %>
                          <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%>
                        </td>
                      </tr>
                    </table>
                     <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                      <%-- add opie-eyek 20131205 untuk posting perdocument --%>
                     <% if(billMain.getBillStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT || billMain.getBillStatus() == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED) { %>
                          <td width="5%" valign="top"><a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListTitle[SESS_LANGUAGE][5]%>"></a></td>
                          <td width="95%" nowrap>&nbsp; <a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')" class="command" > <%=textListTitle[SESS_LANGUAGE][5]%> </a></td>
                      <%}else if (billMain.getBillStatus()== I_DocStatus.DOCUMENT_STATUS_FINAL){%>
                          <td width="5%" valign="top"><a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListTitle[SESS_LANGUAGE][5]%>"></a></td>
                          <td width="45%" nowrap>&nbsp; <a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')" class="command" > <%=textListTitle[SESS_LANGUAGE][5]%> </a></td>
                          <td width="5%" valign="top"><a href="javascript:PostingStock('<%=oidBillMainOid%>')"><img src="<%=approot%>/images/update_data.jpg" width="31" height="27" border="0" alt="<%=textListTitle[SESS_LANGUAGE][6]%>"></a></td>
                          <td width="45%" nowrap>&nbsp; <a href="javascript:PostingStock('<%=oidBillMainOid%>')" class="command" > <%=textListTitle[SESS_LANGUAGE][6]%> </a></td>
                      <%}else{%>
                          <td width="5%" valign="top"><a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListTitle[SESS_LANGUAGE][5]%>"></a></td>
                          <td width="45%" nowrap>&nbsp; <a href="javascript:printDeliveryOrder('<%=oidBillMainOid%>')" class="command" > <%=textListTitle[SESS_LANGUAGE][5]%> </a></td>
                      <%}%>
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

