<%--
    Document   : df_stock_order_print
    Created on : 18 Jul 13, 9:34:14
    Author     : Wiweka
--%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java"  %>
<%@ page import = " java.util.* ,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
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
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.pos.entity.payment.CashPayments1,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*,
         com.dimata.posbo.entity.admin.*,
         com.dimata.posbo.form.warehouse.*"%>
<%@ include file = "../main/javainit.jsp" %>
<%
            int appObjCodeSalesOrder = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_ORDER);
            int appObjCodeSalesRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_RETUR);
            int appObjCodeSalesInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_INVOICE);

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
            boolean privApprovalSalesOrder = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesOrder, AppObjInfo.COMMAND_VIEW));
            boolean privApprovalSalesRetur = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesRetur, AppObjInfo.COMMAND_VIEW));
            boolean privApprovalSalesInvoice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesInvoice, AppObjInfo.COMMAND_VIEW));
%>
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Number", "Date", "Location", "Destination","Nama Customer","Alamat","Phone","Fax","Kode Pos","Nama Perusahaan","Alamat Perusahaan","Nama PIC"}, //11
        {"Number", "Date", "Location", "Destination","Customer Name","Address","Telephone","Fax","Postal Code","Company Name","Company Address","Person Name"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Unit", "Quantity", "Price"},//17
        {"No", "Sku/Barcode", "Item Name", "Unit", "Quantity", "Price"}
    };
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
            int iCommand = FRMQueryString.requestCommand(request);
            int startItem = FRMQueryString.requestInt(request, "start_item");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            int appCommand = FRMQueryString.requestInt(request, "approval_command");
            long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");
            long oidDispatchMaterialItem = FRMQueryString.requestLong(request, "hidden_dispatch_item_id");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            
            int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");

            int iErrCode = FRMMessage.NONE;
            String msgString = "";

            String recCode = i_pstDocType.getDocCode(docType);
            String retTitle = "Sales Order"; //i_pstDocType.getDocTitle(docType);
            String recItemTitle = retTitle + " Item";

            CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
            iErrCode = ctrlMatDispatch.action(Command.EDIT, oidDispatchMaterial);
            FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();
            MatDispatch dispatch = ctrlMatDispatch.getMatDispatch();



            String whereClause = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_MATERIAL_ID] + " ='" + oidDispatchMaterial + "'";
            Vector listMatDispatch = PstMatDispatch.list(0, 0, whereClause, "");
            dispatch = (MatDispatch) listMatDispatch.get(0);

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            BillMain billMain = ctrlBillMain.getBillMain();

            String whereClauseBill = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
            Vector listBillMain = PstBillMain.list(0, 0, whereClauseBill, "");
            billMain = (BillMain) listBillMain.get(0);


            /**
             * check if document may modified or not
             */
            boolean privManageData = true;

            ControlLine ctrLine = new ControlLine();
            CtrlMatDispatchItem ctrlMatDispatchItem = new CtrlMatDispatchItem(request);
            ctrlMatDispatchItem.setLanguage(SESS_LANGUAGE);
            iErrCode = ctrlMatDispatchItem.action(iCommand, oidDispatchMaterialItem, oidDispatchMaterial);
            FrmMatDispatchItem frmMatDispatchItem = ctrlMatDispatchItem.getForm();
            MatDispatchItem matDispatchItem = ctrlMatDispatchItem.getMatDispatchItem();
            msgString = ctrlMatDispatchItem.getMessage();

            String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidDispatchMaterial;
            String orderClauseItem = "";
            int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);
            int recordToGetItem = 25;

            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                startItem = ctrlMatDispatchItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
            }

            Vector listDispatchItem = PstMatDispatchItem.list(0, 0, oidDispatchMaterial, "");
            if (listDispatchItem.size() < 1 && startItem > 0) {
                if (vectSizeItem - recordToGetItem > recordToGetItem) {
                    startItem = startItem - recordToGetItem;
                } else {
                    startItem = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listDispatchItem = PstMatDispatchItem.list(startItem, recordToGetItem, whereClauseItem, "");
            }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script language="JavaScript">
            <!--
            //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------
            function main(oid,comm)
            {
                document.frmprintsalesorder.command.value=comm;
                document.frmprintsalesorder.hidden_receive_id.value=oid;
                document.frmprintsalesorder.action="outlet_cashier.jsp";
                document.frmprintsalesorder.submit();
            }


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
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../styles/print.css" type="text/css">
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
                                    <input type="hidden" name="<%=FrmMatDispatchItem.fieldNames[FrmMatDispatchItem.FRM_FIELD_DISPATCH_MATERIAL_ID]%>" value="<%=oidDispatchMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr align="center">
                                            <td colspan="3" class="title" align="center">
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr align="left" class="listgensell">
                                                        <td colspan="4">
                                                            <table width="100%" border="0" cellpadding="1">
                                                                <tr>
                                                                    <td class="title" align="left" width="15%"><img src="../images/company.jpg"></td>
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
                                                            <table id="table" align="center" width="98%" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=(dispatch.getDispatchCode())%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=Formater.formatDate(dispatch.getDispatchDate(), "dd MMM yyyy")%></td>
                                                                            </tr>
                                                                             <%

                                                                                Vector listCust = PstMemberReg.list(0, 0, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " ='" + billMain.getCustomerId() + "'", "");
                                                                                MemberReg memberReg = (MemberReg) listCust.get(0);
                                                                            %>
                                                                             <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(memberReg.getCompName() == "") ? "-" : memberReg.getCompName()%></td>
                                                                            </tr>
                                                                            <%
                                                                                String address = "";
                                                                                if(memberReg.getBussAddress() != ""){
                                                                                    address = memberReg.getBussAddress();
                                                                                }else{
                                                                                    address = memberReg.getHomeAddr();
                                                                                }
                                                                            %>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][10]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(address == "") ? "-" : address%></td>
                                                                            </tr>

                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(memberReg.getPersonName() == "") ? "-" : memberReg.getPersonName()%></td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    <td></td>
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
                                                                                        for (int i = 0; i < listDispatchItem.size(); i++) {
                                                                                            Vector temp = (Vector) listDispatchItem.get(i);
                                                                                            matDispatchItem = (MatDispatchItem) temp.get(0);
                                                                                            Material mat = (Material) temp.get(1);
                                                                                            Unit unit = (Unit) temp.get(2);
                                                                                            start = start + 1;
                                                                            %>
                                                                            <tr>
                                                                                <td width="3%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                                <td width="8%" class="listgensell">&nbsp;<%=mat.getName()%></td>
                                                                                <td width="15%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=matDispatchItem.getQty()%></td>
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
                                                    <%if ((listDispatchItem != null) && (listDispatchItem.size() > 0)) {%>
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

