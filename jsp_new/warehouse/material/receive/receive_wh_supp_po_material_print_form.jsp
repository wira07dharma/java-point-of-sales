<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReceiveStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReceiveStockCode"%>
<%@ page import="com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.common.entity.payment.CurrencyType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Nomor PO", "Nota Supplier", "Ppn", "Include", "%", "Kode BC", "Tanggal BC", "Lokasi Pabean", "Jenis Dokumen"},
        {"No", "Location", "Date", "Supplier", "Status", "Remark", "PO Code", "Supplier Invoice", "Ppn", "Include", "%", "Customs Code", "Date BC", "Pabean Location", "Document Type"}
    };
    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama", "Kadaluarsa", "Unit", "Harga Beli", "Harga Jual", "Mata Uang", "Qty", "Total Beli", "Barcode","Total Jual", "HS Code"},
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Price", "Currency", "Qty", "Total Cost", "Barcode","Total Jual", "HS Code"}
    };
	public static int getStrDutyFree(){
		String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
		System.out.println("#Duty Free: " + strDutyFree);
		int dutyFree = Integer.parseInt(strDutyFree);
		return dutyFree;
	}
%>
<%
    /**
     * get approval status for create document 
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>
<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "hidden_receive_item_id");
    int typePrint = FRMQueryString.requestInt(request,"type_print_tranfer");
    String syspropHSCode = PstSystemProperty.getValueByName("SHOW_HS_CODE");
    
    //adding dynamic sign rec by mirahu 20120427
    String signRec1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
    String signRec2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
    String signRec3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
    String[] sign1 = signRec1.split(",");
    String[] sign2 = signRec2.split(",");
    String[] sign3 = signRec3.split(",");

    //adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
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
    String retTitle = "Penerimaan Barang"; //i_pstDocType.getDocTitle(docType);
    String recItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();

    /**
     * check if document may modified or not 
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
    ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReceiveItem.action(iCommand, oidReceiveMaterialItem, oidReceiveMaterial);
    FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
    MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
    msgString = ctrlMatReceiveItem.getMessage();

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
    String orderClauseItem = "";
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
    int recordToGetItem = 25;

    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatReceiveItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

//Vector listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
    Vector listMatReceiveItem = PstMatReceiveItem.list(0, 0, whereClauseItem);
    if (listMatReceiveItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listMatReceiveItem = PstMatReceiveItem.list(startItem, recordToGetItem, whereClauseItem);
    }

    PurchaseOrder po = new PurchaseOrder();
    try {
        po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
    } catch (Exception xxx) {
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
                document.frm_recmaterial.hidden_receive_id.value=oid;
                document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                document.frm_recmaterial.submit();
            }
            //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


            //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
            function cmdAdd()
            {
                document.frm_recmaterial.hidden_receive_item_id.value="0";
                document.frm_recmaterial.command.value="<%=Command.ADD%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdEdit(oidReceiveMaterialItem)
            {
                document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
                document.frm_recmaterial.command.value="<%=Command.EDIT%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdAsk(oidReceiveMaterialItem){
                document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
                document.frm_recmaterial.command.value="<%=Command.ASK%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdSave()
            {
                document.frm_recmaterial.command.value="<%=Command.SAVE%>"; 
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdConfirmDelete(oidReceiveMaterialItem){
                document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
                document.frm_recmaterial.command.value="<%=Command.DELETE%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.approval_command.value="<%=Command.DELETE%>";	
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdCancel(oidReceiveMaterialItem){
                document.frm_recmaterial.hidden_receive_item_id.value=oidReceiveMaterialItem;
                document.frm_recmaterial.command.value="<%=Command.EDIT%>";
                document.frm_recmaterial.prev_command.value="<%=prevCommand%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdBack(){
                document.frm_recmaterial.command.value="<%=Command.EDIT%>";
                document.frm_recmaterial.action="receive_wh_supp_po_material_edit.jsp";
                document.frm_recmaterial.submit();
            }

            function sumPrice()
            {
            }		

            function cmdCheck()
            {
                var strvalue  = "materialpodosearch.jsp?command=<%=Command.FIRST%>"+
                    "&mat_code="+document.frm_recmaterial.matCode.value+
                    "&oidPurchaseOrder=<%=rec.getPurchaseOrderId()%>";
                window.open(strvalue,"material", "height=500,width=750,status=yes,toolbar=no,menubar=yes,location=no,scrollbars=yes");
                //alert("Sorry, under construction!!!");
            }

            function cntTotal()
            {
                var qty = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.value,guiDigitGroup);
                var price = cleanNumberInt(document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_COST]%>.value,guiDigitGroup);
                if(!(isNaN(qty)) && (qty != '0'))
                {
                    var amount = parseFloat(price) * qty;
                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_TOTAL]%>.value = amount;					 
                }
                else
                {
                    document.frm_recmaterial.<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_QTY]%>.focus();
                }
            }

            function cmdListFirst(){
                document.frm_recmaterial.command.value="<%=Command.FIRST%>";
                document.frm_recmaterial.prev_command.value="<%=Command.FIRST%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListPrev(){
                document.frm_recmaterial.command.value="<%=Command.PREV%>";
                document.frm_recmaterial.prev_command.value="<%=Command.PREV%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListNext(){
                document.frm_recmaterial.command.value="<%=Command.NEXT%>";
                document.frm_recmaterial.prev_command.value="<%=Command.NEXT%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdListLast(){
                document.frm_recmaterial.command.value="<%=Command.LAST%>";
                document.frm_recmaterial.prev_command.value="<%=Command.LAST%>";
                document.frm_recmaterial.action="receive_wh_supp_po_materialitem.jsp";
                document.frm_recmaterial.submit();
            }

            function cmdBackList(){
                document.frm_recmaterial.command.value="<%=Command.FIRST%>";
                document.frm_recmaterial.action="receive_wh_supp_po_material_list.jsp";
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
        <style>
                img {
                    height: 25%;
                    margin: 20px 10px 10px 20px;
                }
            td.title {
                margin-bottom: 10px !important;
                padding-bottom: 20px !important;
            }
            td.top-3 {
                width: 12%;
            }
            td.top-2 {
                width: 18%;
                padding-left: 25px;
            }
            td.top-1 {
                width: 15%;
                padding-left: 20px;
            }
            td.top-isi-1 {
                width: 21%;
            }
            td.top-isi-2 {
                width: 15%;
            }
            td.top-isi-3 {
                width: 20%;
            }
        </style>
        
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">
        <!-- #EndEditable -->
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr>
                <td width="88%" valign="top" align="left" height="56">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frm_recmaterial" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
                                    <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                      <tr align="center">
                                        <td colspan="3">
                                          <table width="100%" border="0" cellpadding="1">
                                            <tr>
                                              <td class="" align="center" ><img src="../../../images/company_pdf.jpg"></td>
                                            </tr>
                                            <tr>
                                              <td class="title" align="center"><b><%=retTitle.toUpperCase()%></b></td>
                                            </tr>
                                          </table>
                                        </td>
                                      </tr>
                                      <tr><td>
                                      <table width="100%" border="0" cellpadding="3">
                                      <tr>
                                          <td class="top-1"><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                          <td  class="top-isi-1"> : <%=rec.getRecCode()%></td>
                                          <% if (getStrDutyFree() == 1) {%>
                                          <td  class="top-2"><%= textListOrderHeader[SESS_LANGUAGE][11]%></td>
                                          <td   class="top-isi-2"> : <%= rec.getNomorBc()%></td>
                                          <%}%>
                                          <td  class="top-3"><%=textListOrderHeader[SESS_LANGUAGE][3]%> </td>
                                                <%
                                                  ContactList cnt = new ContactList();
                                                  try {
                                                    cnt = PstContactList.fetchExc(rec.getSupplierId());
                                                  } catch (Exception e) {
                                                  }

                                                  String cntName = cnt.getCompName();
                                                  if (cntName.length() == 0) {
                                                    cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                  }
                                                %>
                                          <td   class="top-isi-3"> :  <%=cntName%> </td>
                                      </tr>
                                      <tr>
                                          <td class="top-1"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                <%
                                                  Location loc = new Location();
                                                  try {
                                                    loc = PstLocation.fetchExc(rec.getLocationId());
                                                  } catch (Exception e) {
                                                  }
                                                %>
                                          <td  class="top-isi-1"> : <%=loc.getName()%></td>
                                          <% if (getStrDutyFree() == 1) {%>
                                          <td  class="top-2"><%=textListOrderHeader[SESS_LANGUAGE][14]%></td>
                                          <td   class="top-isi-2"> : <%=rec.getJenisDokumen()%></td>
                                          <%}%>
                                          <td  class="top-3"><%=textListOrderHeader[SESS_LANGUAGE][6]%> </td>
                                          <td   class="top-isi-3"> :  <%= po.getPoCode()%>  </td>
                                      </tr>
                                      <tr>
                                          <td class="top-1"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                          <td  class="top-isi-1"> : <%=Formater.formatDate(rec.getReceiveDate(), "dd MMMM yyyy")%></td>
                                          <% if (getStrDutyFree() == 1) {%>
                                          <td  class="top-2"><%=textListOrderHeader[SESS_LANGUAGE][12]%> </td>
                                          <td   class="top-isi-2"> : <%= Formater.formatDate(rec.getTglBc(), "dd MMMM yyyy")%></td>
                                          <%}%>
                                          <td  class="top-3"><%=textListOrderHeader[SESS_LANGUAGE][7]%> </td>
                                          <td   class="top-isi-3"> :  <%= rec.getInvoiceSupplier()%> </td>
                                      </tr>
                                      <tr>
                                          <% if (getStrDutyFree() == 1) {%>
                                          <td class="top-1"><%=textListOrderHeader[SESS_LANGUAGE][13]%></td>
                                              <%
                                                try {
                                                  loc = PstLocation.fetchExc(rec.getLocationPabean());
                                                } catch (Exception e) {
                                                }
                                              %>
                                          <td  class="top-isi-1"> : <%=loc.getName()%></td>
                                          <%}%>
                                      </tr>
                                      </table></td>
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
                                                          <td class="formElemen" width="4%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                          <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][10]%></td>
                                                          <% } else {%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                          <% }%>
                                                          <% if (syspropHSCode.equals("1")) {%> 
                                                          <td width="" class="formElemen"><%=textListOrderItem[SESS_LANGUAGE][12]%></td>
                                                          <% } %>
                                                          <td class="formElemen"  width="20%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                          <td  class="formElemen" width="8%"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                          <%if (privShowQtyPrice) {%>
                                                          <%if (typePrint == 0) { //cost%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                                          <%} else if (typePrint == 1) { //sell%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                          <%} else { //both%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                          <%}%>
                                                          <%}%>
                                                          <td class="formElemen" width="8%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                          <%if (privShowQtyPrice) {%>
                                                          <%if (typePrint == 0) { //cost%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                          <%} else if (typePrint == 1) { //sell%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][11]%></td>
                                                          <%} else { //both%>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                          <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][11]%></td>
                                                          <%}%>

                                                          <%} %>
                                                        </tr>
                                                        <%
                                                          int start = 0;
                                                          double totalSellPrice = 0.0;
                                                          for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                                            Vector temp = (Vector) listMatReceiveItem.get(i);
                                                            MatReceiveItem recItemx = (MatReceiveItem) temp.get(0);
                                                            Material mat = (Material) temp.get(1);
                                                            Unit unit = (Unit) temp.get(2);
                                                            CurrencyType currencyType = (CurrencyType) temp.get(3);
                                                            String listStockCode = "";
                                                            mat.setOID(recItemx.getMaterialId());
                                                            if (typePrint == 1) {
                                                              double priceSales = SessMaterial.getPriceSale(mat);
                                                              recItemx.setCost(priceSales);
                                                              recItemx.setTotal(priceSales * recItemx.getQty());
                                                              totalSellPrice = totalSellPrice + recItemx.getTotal();
                                                            }
                                                            Category cat = new Category();
                                                            try {
                                                                cat = PstCategory.fetchExc(mat.getCategoryId());
                                                              } catch (Exception e) {
                                                              }
                                                                    
                                                            double sellPrice = 0;
                                                            double totalSalesPrice = 0;
                                                            if (typePrint == 2) {
                                                              sellPrice = SessMaterial.getPriceSale(mat);
                                                              totalSalesPrice = sellPrice * recItemx.getQty();
                                                              totalSellPrice = totalSellPrice + totalSalesPrice;
                                                            }

                                                            if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                                                              String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItemx.getOID();
                                                              Vector cntStockCode = PstReceiveStockCode.list(0, 0, where, "");
                                                              for (int s = 0; s < cntStockCode.size(); s++) {
                                                                ReceiveStockCode materialStockCode = (ReceiveStockCode) cntStockCode.get(s);
                                                                if (s == 0) {
                                                                  listStockCode = listStockCode + "<br>&nbsp;SN : " + materialStockCode.getStockCode();
                                                                } else {
                                                                  listStockCode = listStockCode + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + materialStockCode.getStockCode();
                                                                }

                                                              }
                                                            }
                                                            start = start + 1;

                                                        %>
                                                        <tr >
                                                          <td width="4%" height="21" align="center" class="formElemen">&nbsp;<%=start%></td>
                                                          <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                          <td  class="formElemen" width="10%">&nbsp;<%=mat.getSku()%></td>
                                                          <td  class="formElemen" width="10%">&nbsp;<%=mat.getBarCode()%></td>
                                                          <% } else {%>
                                                          <td  class="formElemen" width="10%">&nbsp;<%=mat.getSku()%></td>
                                                          <% }%>
                                                          <% if (syspropHSCode.equals("1")) {%>
                                                          <td  class="formElemen" width="10%">&nbsp;<%=cat.getCode()%></td>
                                                          <% } %>
                                                          <td class="formElemen" width="20%">&nbsp;<%=mat.getName()%><%=listStockCode%></td>
                                                          <td class="formElemen" width="8%">&nbsp;<%=unit.getCode()%></td>
                                                          <%if (privShowQtyPrice) {%>
                                                          <%if (typePrint == 0) { //cost%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                                                          <%} else if (typePrint == 1) { //sell%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                                                          <%} else { //both%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(sellPrice)%></td>
                                                          <%}%>
                                                          <%}%>
                                                          <td class="formElemen" width="8%" align="right">&nbsp;<%=recItemx.getQty()%></td>
                                                          <%if (privShowQtyPrice) {%>
                                                          <%if (typePrint == 0) { //cost%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getTotal())%></td>
                                                          <%} else if (typePrint == 1) { //sell%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getTotal())%></td>
                                                          <%} else { //both%>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getTotal())%></td>
                                                          <td class="formElemen" width="10%" align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(totalSalesPrice)%></td>
                                                          <%}%>
                                                          <%} %>
                                                        </tr>
                                                        <%}%>
                                                      </table>
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                            <%if (privShowQtyPrice) {%>
                                            <tr> 
                                              <td width="64%" rowspan="2" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=rec.getRemark()%> </td>
                                              <td width="36%" valign="top">
                                                <table width="100%" border="0">
                                                  <%
                                                    String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
                                                    double total = PstMatReceiveItem.getTotal(whereItem);
                                                    // double ppn = total * rec.getTotalPpn() / 100;
                                                    double ppn = rec.getTotalPpn();
                                                    if (ppn == 0) {
                                                      ppn = defaultPpn;
                                                    }
                                                    double totalBeliWithPPN = (total * (ppn / 100)) + total;
                                                    double valuePpn = 0.0;
                                                    if (rec.getIncludePpn() == 1) {
                                                      valuePpn = total - (total / 1.1);
                                                    } else if (rec.getIncludePpn() == 0) {
                                                      valuePpn = total * (ppn / 100);
                                                    }
                                                  %>
                                                  <tr>
                                                    <td width="56%">
                                                      <div align="right">Sub Total</div>
                                                    </td>
                                                    <td width="5%">
                                                      <div align="right">:</div>
                                                    </td>
                                                    <td width="30%">
                                                      <%if (typePrint == 0) { //cost%>
                                                      <div align="right"><%=FRMHandler.userFormatStringDecimal(total)%> </div>
                                                      <%} else if (typePrint == 1) { //sell%>
                                                      <div align="right"><%=FRMHandler.userFormatStringDecimal(totalSellPrice)%> </div>
                                                      <%} else { //both%>
                                                      <div align="right"><%=FRMHandler.userFormatStringDecimal(total)%> </div>
                                                      <%}%>
                                                    </td>
                                                  </tr>
                                                  <tr>
                                                    <td width="56%"><div align="right"><input type="checkbox" disabled=\"true\" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if (rec.getIncludePpn() == 1) {%>checked<%}%>><%=textListOrderHeader[SESS_LANGUAGE][9]%>&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][8]%>
                                                        <input type="text" disabled=\"true\"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%if (ppn != 0.0) {%><%=FRMHandler.userFormatStringDecimal(ppn)%><%} else {%><%=FRMHandler.userFormatStringDecimal(defaultPpn)%><%}%>"  size="5" style="text-align:right">&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][10]%></div>
                                                    </td>
                                                    <td width="5%">
                                                      <div align="right">:</div>
                                                    </td>
                                                    <td width="30%">
                                                      <div align="right"><b><%=FRMHandler.userFormatStringDecimal(valuePpn)%></b></div>
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>

                                            <%if (listMatReceiveItem != null && listMatReceiveItem.size() > 0) {%>
                                            <tr> 
                                              <td width="36%" valign="top"> 
                                                <table width="100%" border="0">
                                                  <tr> 
                                                    <td width="56%">
                                                      <div align="right"><%="Total"%></div>
                                                    </td>
                                                    <td width="5%"> 
                                                      <div align="right">:</div>
                                                    </td>
                                                    <td width="30%">
                                                      <div align="right"> 
                                                        <% if (rec.getIncludePpn() == 1) {%>

                                                        <%if (typePrint == 0) { //cost%>
                                                        <%=FRMHandler.userFormatStringDecimal(total)%>
                                                        <%} else if (typePrint == 1) { //sell%>
                                                        <%=FRMHandler.userFormatStringDecimal(totalSellPrice)%>
                                                        <%} else { //both%>
                                                        <%=FRMHandler.userFormatStringDecimal(total)%>
                                                        <%}%>
                                                        <%} else {%>
                                                        <%if (typePrint == 0) { //cost%>
                                                        <%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%>
                                                        <%} else if (typePrint == 1) { //sell%>
                                                        <%=FRMHandler.userFormatStringDecimal(totalSellPrice)%>
                                                        <%} else { //both%>
                                                        <%=FRMHandler.userFormatStringDecimal(total)%>
                                                        <%}%>
                                                        <%}%>
                                                      </div>
                                                    </td>
                                                  </tr>
                                                </table>
                                              </td>
                                            </tr>
                                            <%}%>
                                            <%} %>
                                          </table>
                                        </td>
                                      </tr>
                                    </table>			  
                                    <table width="100%">			  
                                      <tr align="left" valign="top"> 
                                        <td height="40" valign="middle" colspan="3"></td>
                                      </tr>
                                      <tr>
                                        <%if (signRec1.equals(signRec1) && !signRec1.equals("Not initialized")) {%>     
                                        <td width="34%" align="center" nowrap><%=sign1[0]%></td>
                                        <%} else {%>
                                        <td width="34%" align="center" nowrap>Diterima Oleh,</td>
                                        <% } %>
                                        <%if (signRec2.equals(signRec2) && !signRec2.equals("Not initialized")) {%>
                                        <td width="34%" align="center" nowrap><%=sign2[0]%></td>
                                        <%} else {%>
                                        <td align="center" valign="bottom" width="33%">Dikirim Oleh,</td>
                                        <% } %>
                                        <%if (signRec3.equals(signRec3) && !signRec3.equals("Not initialized")) {%>
                                        <td width="33%" align="center"><%=sign3[0]%></td>
                                        <%} else {%>
                                        <td width="33%" align="center">Mengetahui,</td>
                                        <% }%>
                                      </tr>
                                      <tr align="left" valign="top"> 
                                        <td height="75" valign="middle" colspan="3"></td>
                                      </tr>
                                      <tr>
                                        <td width="34%" align="center" nowrap>
                                          (.................................)
                                        </td>
                                        <td align="center" valign="bottom" width="33%">
                                          (.................................)
                                        </td>
                                        <td width="33%" align="center">
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

