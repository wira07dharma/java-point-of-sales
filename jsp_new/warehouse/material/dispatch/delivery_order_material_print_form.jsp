<%-- 
    Document   : delivery_order_material_print_form
    Created on : Jan 14, 2014, 10:56:51 PM
    Author     : dimata005
--%>


<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.form.contact.CtrlContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.pos.entity.billing.BillDetailCode"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetailCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReceiveStockCode"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReceiveStockCode"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.MemberReg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMemberReg"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.form.billing.FrmBillDetail"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillDetail"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.form.billing.CtrlBillMain"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textMaterialHeader[][] =
{
	{"Number", "Date", "Location", "Destination","Nama Customer","Alamat","Phone","Fax","Kode Pos","Nama Perusahaan","Alamat Perusahaan","Nama PIC","Alamat Rumah","Kota ", " Provisi", "No Handphone", "No Fax Rumah", "Kode Pos Rumah", "Shipping Mobile Phone","Shipping Fax" }, //11
        {"Number", "Date", "Location", "Destination","Customer Name","Address","Telephone","Fax","Postal Code","Company Name","Company Address","Person Name"}
};


/* this constant used to list text of listMaterialItem */
public static final String textListOrderItem[][] =
{
	{"No", "Sku/Barcode", "Item Name", "Unit", "Quantity", "Serial Number"},//17
        {"No", "Sku/Barcode", "Item Name", "Unit", "Quantity", "Serial Number"}
};

/**
* this method used to maintain poMaterialList
*/


%>
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
            int startItem = FRMQueryString.requestInt(request, "start_item");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            int appCommand = FRMQueryString.requestInt(request, "approval_command");
            long oidBilldetail = FRMQueryString.requestLong(request, "hidden_dispatch_id");
            //long oidBillDetailItem = FRMQueryString.requestLong(request, "hidden_dispatch_item_id");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            long oidContactList = FRMQueryString.requestLong(request, "hidden_contact_id");




int typePrint = FRMQueryString.requestInt(request,"type_print_tranfer");

//adding dynamic sign rec by mirahu 20120427


 //adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
     if(useBarcodeorSku.equals("Not initialized")){
       useBarcodeorSku= "0";
     }
      chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);


/**
* initialization of some identifier
*/
int iErrCode = FRMMessage.NONE;
String msgString = "";

/**
* purchasing pr code and title
*/
String recCode = i_pstDocType.getDocCode(docType);
String retTitle = "Sales Order"; //i_pstDocType.getDocTitle(docType);
String recItemTitle = retTitle + " Item";

/**
* process on purchase order main
*/

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            BillMain billMain = ctrlBillMain.getBillMain();

            String whereClauseBill = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
            Vector listBillMain = PstBillMain.list(0, 0, whereClauseBill, "");
            billMain = (BillMain) listBillMain.get(0);

            ContactList contactList =  new ContactList();
            try{
                contactList = PstContactList.fetchExc(billMain.getCustomerId());
            }catch(Exception exc){

            }


/**
* check if document may modified or not
*/
boolean privManageData = true;

            ControlLine ctrLine = new ControlLine();
            CtrlBillDetail CtrlBillDetail = new CtrlBillDetail(request);
            CtrlBillDetail.setLanguage(SESS_LANGUAGE);
            iErrCode = CtrlBillDetail.action(iCommand, oidBillMain, oidBilldetail);
            FrmBillDetail FrmBillDetail = CtrlBillDetail.getForm();
            Billdetail Billdetail = CtrlBillDetail.getBillDetail();
            msgString = CtrlBillDetail.getMessage();

String whereClauseBillMain = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID]+"="+oidBillMain;

// " RMI."+PstBillDetail.fieldNames[PstBillDetail.FLD_RECEIVE_MATERIAL_ITEM_ID]
String orderClauseItem = "";
int vectSizeItem = PstBillDetail.getCount(whereClauseBillMain);
int recordToGetItem = 25;



if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	startItem = CtrlBillDetail.actionList(iCommand,startItem,vectSizeItem,recordToGetItem);
}

//Vector listDispatchItem = PstBillDetail.list(startItem,recordToGetItem,whereClauseItem);
//String where = "";
  Vector listBillDetailItem = PstBillDetail.list(0, 0,whereClauseBillMain, "");
if(listBillDetailItem.size()<1 && startItem>0)
{
	 if(vectSizeItem-recordToGetItem > recordToGetItem)
	 {
		startItem = startItem - recordToGetItem;
	 }
	 else
	 {
		startItem = 0 ;
		iCommand = Command.FIRST;
		prevCommand = Command.FIRST;
	 }
	 listBillDetailItem = PstBillDetail.list(startItem, recordToGetItem, whereClauseBillMain, "");
}


%>
<html>
<!-- #BeginTemplate "/Templates/print.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
//------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
function main(oid,comm)
{
	document.frm_recmaterial.command.value=comm;
	document.frm_recmaterial.hidden_dispatch_id.value=oid;
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
function cmdAdd()
{
	document.frm_recmaterial.hidden_dispatch_id.value="0";
	document.frm_recmaterial.command.value="<%=Command.ADD%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdEdit(oidReceiveMaterialItem)
{
	document.frm_recmaterial.hidden_dispatch_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdAsk(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_dispatch_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.ASK%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdSave()
{
	document.frm_recmaterial.command.value="<%=Command.SAVE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdConfirmDelete(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_dispatch_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdCancel(oidReceiveMaterialItem){
	document.frm_recmaterial.hidden_dispatch_id.value=oidReceiveMaterialItem;
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function sumPrice()
{
}




function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}

function cmdBackList(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="outlet_cashier.jsp";
	document.frm_recmaterial.submit();
}
//------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function MM_swapImgRestore() { //v3.0
	var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr>
                <td width="88%" valign="top" align="left" height="56">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmprintsalesorder" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                                    <input type="hidden" name="hidden_dispatch_id" value="<%=oidBilldetail%>">

                                    <input type="hidden" name="<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_DETAIL_ID]%>" value="<%=oidBilldetail%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr align="center">
                                            <td colspan="3" class="title" align="center">
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr align="left" class="listgensell">
                                                        <td colspan="4">
                                                            <table width="100%" border="0" cellpadding="1">
                                                                <tr>
                                                                    <td class="title" align="left" width="15%"><img src="../../../images/company.jpg"></td>
                                                                    <td class="title" align="center" width="70%"><b>&nbsp;DELIVERY ORDER</b></td>
                                                                    <td width="15%"></td>
                                                                </tr>
                                                            </table>                                                        </td>
                                                    </tr>
                                                    <tr align="center" class="listgensell">
                                                        <td colspan="4" nowrap><b>&nbsp;</b></td>
                                                    </tr>
                                                    <tr align="center" class="listgensell">
                                                        <td colspan="4" nowrap>
                                                            <table id="table" border ="0" align="center" width="98%" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%">    <%=billMain.getInvoiceNumber()%>   </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                                <td width="5%">: </td>
                                                                                <td width="55%">  <%=Formater.formatDate(billMain.getBillDate(),"dd-MM-yyyy")%> </td>
                                                                            </tr>
                                                                             <%
                                                                                Vector listCust = new Vector();
                                                                                MemberReg memberReg = new MemberReg();
                                                                                if(billMain.getCustomerId()!=0){
                                                                                    listCust = PstMemberReg.list(0, 0, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " ='" + billMain.getCustomerId() + "'", "");
                                                                                    memberReg = (MemberReg) listCust.get(0);
                                                                                }
                                                                            %>
                                                                             <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                                <td width="5%">:         </td>
                                                                                <td width="65%">  <%=(memberReg.getCompName() == "") ? "-" : memberReg.getCompName()%> </td>
                                                                            </tr>
                                                                            <%
                                                                                String address = "";
                                                                                if(memberReg.getBussAddress() != ""){
                                                                                    address = memberReg.getBussAddress();
                                                                                }else{
                                                                                    address = memberReg.getHomeAddr();
                                                                                }
                                                                            %>

                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][10]%></td>
                                                                                <td width="5%">:   </td>
                                                                                <td width="65%">   <%=(address == "") ? "-" : address%>   </td>


                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                                <td width="3%">:   </td>
                                                                                <td width="65%">  <%=(memberReg.getPersonName() == "") ? "-" : memberReg.getPersonName()%>  </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    <td></td>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2" >

                                                                            <tr>
                                                                                <td width="40%"><%=textMaterialHeader[SESS_LANGUAGE][12]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="45%"><%=contactList.getHomeAddr()%>, <%=contactList.getHomeTown()%>, <%=contactList.getHomeProvince()%> </td>
                                                                            </tr>



                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][15]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=contactList.getTelpMobile()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][16]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=contactList.getHomeFax()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][17]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=contactList.getPostalCodeHome()%></td>
                                                                            </tr>


                                                                        </table>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2" >
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=Formater.formatDate(billMain.getBillDate(), "dd MMM yyyy")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingAddress()%> , <%=billMain.getShippingCity()%> , <%=billMain.getShippingProvince()%></td>
                                                                            </tr>

                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][6]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingPhoneNumber()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][7]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingFax()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingZipCode()%></td>
                                                                            </tr>


                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>

                                                            </table>
                                                        </td>
                                                    </tr>

                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <table width="100%" cellpadding="1" cellspacing="1">
                                                    <tr>
                                                        <td colspan="2" >
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                <tr align="left" valign="top">
                                                                    <td height="22" valign="middle" colspan="3">
                                                                        <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                                                            <tr align="center">
                                                                                <td width="3%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                                <td width="8%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                                <td width="15%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                                <td width="4%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                                <td width="4%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>

                                                                            </tr>
                                                                            <%
                                                                                        int start = 0;
                                                                                        for (int i = 0; i < listBillDetailItem.size(); i++) {
                                                                                               Billdetail billdetail = (Billdetail) listBillDetailItem.get(i);
                                                                                               Material mat = new Material();
                                                                                               Unit unit = new Unit();
                                                                                            try{
                                                                                                billdetail = (Billdetail) listBillDetailItem.get(i);
                                                                                                mat = PstMaterial.fetchExc(billdetail.getMaterialId());
                                                                                                unit = PstUnit.fetchExc(billdetail.getUnitId());
                                                                                            }catch(Exception ex){
                                                                                                System.out.print(ex);
                                                                                                billdetail = new Billdetail();
                                                                                            }
                                                                                            String listStockCode= "";
                                                                                            if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                                                                                             String where = PstBillDetailCode.fieldNames[PstBillDetailCode.FLD_SALE_ITEM_ID]+"="+billdetail.getOID();
                                                                                             Vector cntStockCode = PstBillDetailCode.list(0,0,where,"");

                                                                                               int loop = 0;
                                                                                             for (int s = 0; s < cntStockCode.size(); s++) {


                                                                                                BillDetailCode materialStockCode = (BillDetailCode) cntStockCode.get(s);
                                                                                                if(s==0 )
                                                                                                {







                                                                                                      loop = loop + 1;
                                                                                                    listStockCode=listStockCode+"<br>&nbsp;SN : "+materialStockCode.getStockCode();



                                                                                                }

                                                                                                else  {

                                                                                                    if (loop == 4 ){




                                                                                                         loop = 1;

                                                                                                        listStockCode=listStockCode+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+materialStockCode.getStockCode();

                                                                                                     } else{
                                                                                                        loop = loop + 1;
                                                                                                         listStockCode=listStockCode+",&nbsp;&nbsp;&nbsp;"+materialStockCode.getStockCode();

                                                                                                     }

                                                                                                    }

                                                                                             }
                                                                                            }

                                                                                            start = start + 1;

                                                                            %>

                                                                             <tr>
                                                                                <td width="3%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                                 <td width="15%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <td width="8%" class="listgensell">&nbsp;<%=mat.getName()%> &nbsp;<%=listStockCode%></td>

                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=billdetail.getQty()%></td>

                                                                            </tr>
                                                                            <%}%>

                                                                            <tr>
                                                                                <td width="6%" colspan="4" align="center" class="listgensell">
                                                                                    <table width="20%" align="right">
                                                                                        <tr>
                                                                                            <td height="5%" colspan="2">
                                                                                                <b>TOTAL</b>
                                                                                            </td>

                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                                <td width="10%" align="right" class="listgensell">
                                                                                    <table width="30%" align="right">

                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top" rowspan="2"></td>
                                                        <td width="35%" valign="top">

                                                        </td>
                                                    </tr>
                                                    <%if ((listBillDetailItem != null) && (listBillDetailItem.size() > 0)) {%>
                                                    <tr>
                                                        <td width="27%" valign="top">
                                                            <table width="100%" border="0">
                                                                <tr class="listgensell">
                                                                    <td width="44%">
                                                                        <div align="right"><%//="Total"%></div>
                                                                    </td>
                                                                    <td width="15%">
                                                                        <div align="right"></div>
                                                                    </td>
                                                                    <td width="41%">
                                                                        <div align="right">
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%">
                                        <tr align="left" valign="top">
                                            <td height="40" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center">Mengetahui,</td>
                                            <td width="25%" align="center">Pengirim,</td>
                                            <td width="25%" align="center">Penerima,</td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="75" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center" nowrap>
                                                (.................................)
                                            </td>
                                            <td width="25%" align="center">
                                                (.................................)
                                            </td>
                                            <td width="25%" align="center">
                                                (.................................)
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate -->
</html>

