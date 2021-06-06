<%-- 
    Document   : cashier_print
    Created on : 18 Jul 13, 9:34:14
    Author     : Wiweka
--%>
<%@page import= "com.dimata.posbo.entity.admin.PstAppUser"%>
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
         com.dimata.common.entity.payment.*"%>
<%@ include file = "../main/javainit.jsp" %>
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeaderMain[][] = {
        {"", "CASH INVOICE", "RETUR", "GIFT", "SALES ORDER", "CREDIT INVOICE"},
        {"", "CASH INVOICE", "RETUR", "GIFT", "SALES ORDER", "CREDIT INVOICE"}
    };
    public static final String textMaterialHeader[][] = {
        {"Nota Type", "Number", "Customer Type", "Payment", "Bookeeping Currency", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Bayar", "Kembali", "Fax", "Diskon",
            "Tax", "Company", "Address","Notes"}, //11
        {"Nota Type", "Number", "Customer Type", "Payment", "Bookeeping Curreny", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Pay", "Change", "Fax", "Discount",
            "Tax", "Company", "Address","Notes"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",//8
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"},//8
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"}
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
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            long oidBillDetail = FRMQueryString.requestLong(request, "hidden_bill_detail_id");
            int idNotaType = FRMQueryString.requestInt(request, "nota_type");

            double cash = FRMQueryString.requestDouble(request, "cash");
            double creditCard = FRMQueryString.requestDouble(request, "credit_card");
            double debitCard = FRMQueryString.requestDouble(request, "debit_card");
            double chaque = FRMQueryString.requestDouble(request, "cheque");
            double other = FRMQueryString.requestDouble(request, "other");

            int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");

            int iErrCode = FRMMessage.NONE;
            String msgString = "";

            String recCode = i_pstDocType.getDocCode(docType);
            String retTitle = "Sales Order"; //i_pstDocType.getDocTitle(docType);
            String recItemTitle = retTitle + " Item";

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            iErrCode = ctrlBillMain.action(Command.EDIT, oidBillMain, 0);
            FrmBillMain frmBillMain = ctrlBillMain.getForm();
            BillMain billMain = ctrlBillMain.getBillMain();

            String whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
            Vector listBillMain = PstBillMain.list(0, 0, whereClause, "");
            billMain = (BillMain) listBillMain.get(0);

            /**
             * check if document may modified or not
             */
            boolean privManageData = true;

            ControlLine ctrLine = new ControlLine();
            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            ctrlBillDetail.setLanguage(SESS_LANGUAGE);
            iErrCode = ctrlBillDetail.action(iCommand, oidBillDetail, oidBillMain);
            FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();
            Billdetail billdetail = ctrlBillDetail.getBillDetail();
            msgString = ctrlBillDetail.getMessage();

            String whereClauseItem = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;
            String orderClauseItem = "";
            int vectSizeItem = PstBillDetail.getCount(whereClauseItem);
            int recordToGetItem = 25;

            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                startItem = ctrlBillDetail.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
            }

            Vector listSalesOrder = PstBillDetail.listMat(0, 0, whereClauseItem, "");
            if (listSalesOrder.size() < 1 && startItem > 0) {
                if (vectSizeItem - recordToGetItem > recordToGetItem) {
                    startItem = startItem - recordToGetItem;
                } else {
                    startItem = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listSalesOrder = PstBillDetail.listMat(startItem, recordToGetItem, whereClauseItem, "");
            }

            //Mencari paymet
            double totalPayment = 0;
            long oidCashCreditMain = 0;

            int transTypeInvoice = 0;
            if (idNotaType == PstBillMain.OPEN_ALL_INVOICE) {
                transTypeInvoice = billMain.getTransctionType();
            }
            String whereClausePay = "";
            if (idNotaType == PstBillMain.OPEN_BILL || (idNotaType == PstBillMain.OPEN_ALL_INVOICE && transTypeInvoice == PstBillMain.TRANS_TYPE_CASH)) {
                whereClausePay = "CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'";
                totalPayment = PstCashPayment1.getSumPayment(whereClausePay);
            } else if (idNotaType == PstBillMain.OPEN_CREDIT_INVOICE || (idNotaType == PstBillMain.OPEN_ALL_INVOICE && transTypeInvoice == PstBillMain.TRANS_TYPE_CREDIT)) {
                whereClausePay = "CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'";
                totalPayment = PstCreditPaymentMain.getSumPayment(whereClausePay);
            }

            //Mencari ReturPayment
            double cashretur = 0;
            Vector listReturPayment = PstCashReturn.list(0, 0, PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + " ='" + oidBillMain + "'", "");
            CashReturn cashReturn = (CashReturn) listReturPayment.get(0);
            cashretur = cashReturn.getAmount();


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
                document.frmprintsalesorder.action="cashier_lyt.jsp";
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
                                    <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">
                                    <input type="hidden" name="nota_type" value="<%=idNotaType%>">
                                    <input type="hidden" name="<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=oidBillMain%>">
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
                                                                    <td class="title" align="center" width="70%">
                                                                        <%if(idNotaType == PstBillMain.OPEN_ALL_INVOICE){
                                                                            if(transTypeInvoice == PstBillMain.TRANS_TYPE_CASH){%>
                                                                            <b>&nbsp;<%=textMaterialHeaderMain[SESS_LANGUAGE][1].toUpperCase()%></b>
                                                                            <%}else{%>
                                                                            <b>&nbsp;<%=textMaterialHeaderMain[SESS_LANGUAGE][5].toUpperCase()%></b>
                                                                                <%}
                                                                        }else{%>
                                                                        <b>&nbsp;<%=textMaterialHeaderMain[SESS_LANGUAGE][idNotaType].toUpperCase()%></b>
                                                                        <%}%>

                                                                    </td>
                                                                    <td width="15%"></td>
                                                                </tr>
                                                            </table>
                                                        </td>
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
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=(billMain.getInvoiceNumber())%></td>
                                                                            </tr>
                                                                            <tr>

                                                                                <!--%         String custType = "";
                                                                                            Vector listCust = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + billMain.getCustomerId() + "'", "");
                                                                                            if(listCust.size()>0) {
                                                                                                MemberGroup memberGroup = (MemberGroup) listCust.get(0);
                                                                                                custType = memberGroup.getName();
                                                                                             }
                                                                                %-->
                                                                                <%
                                                                                            String custType = "";
                                                                                            int memberTipe = 0;
                                                                                            Vector listCodeCustomerTmp = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='1'", "");
                                                                                            MemberGroup memberGroupCodeTmp = (MemberGroup) listCodeCustomerTmp.get(0);
                                                                                            if (billMain.getCustomerId() == memberGroupCodeTmp.getOID()) {
                                                                                                custType = memberGroupCodeTmp.getName();
                                                                                                memberTipe = 1;
                                                                                            } else {
                                                                                                listCodeCustomerTmp = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='3'", "");
                                                                                                MemberGroup memberGroup = (MemberGroup) listCodeCustomerTmp.get(0);
                                                                                                custType = memberGroup.getName();
                                                                                            }
                                                                                %>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=custType%></td>

                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=PstBillMain.payType[billMain.getTransctionType()]%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <%          String salesName = "";
                                                                                            Vector listSales = PstAppUser.listFullObj(0, 0, PstAppUser.fieldNames[PstAppUser.FLD_USER_ID] + " ='" + billMain.getAppUserId() + "'", "");
                                                                                            if (listSales.size() > 0) {
                                                                                                AppUser appUser = (AppUser) listSales.get(0);
                                                                                                salesName = appUser.getFullName();
                                                                                            }
                                                                                %>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][15]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><%=salesName%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%">Rp. at 9.600/USD</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <%          String currType = "";
                                                                                            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + " ='" + billMain.getCurrencyId() + "'", "");
                                                                                            if (listCurr.size() > 0) {
                                                                                                CurrencyType currencyType = (CurrencyType) listCurr.get(0);
                                                                                                currType = currencyType.getCode();
                                                                                            }
                                                                                %>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="55%"><label><%=currType%> at 9650/USD </label></td>
                                                                            </tr>
                                                                        </table></td>
                                                                    <td></td>
                                                                    <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2" >
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][6]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=Formater.formatDate(billMain.getBillDate(), "dd MMM yyyy")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][7]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%">
                                                                                    <%=billMain.getShippingAddress()%> , <%=billMain.getShippingCity()%> , <%=billMain.getShippingProvince()%>
                                                                                </td>
                                                                            </tr>

                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingPhoneNumber()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][22]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingFax()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=billMain.getShippingZipCode()%></td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    <td></td>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <%if (memberTipe != 1) {%>
                                                                        <table width="100%" border="0" cellspacing="2" cellpadding="2" >
                                                                            <%

                                                                                Vector listCust = new Vector();
                                                                                MemberReg memberReg = new MemberReg();
                                                                                if(billMain.getCustomerId()!=0){
                                                                                    listCust = PstMemberReg.list(0, 0, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " ='" + billMain.getCustomerId() + "'", "");
                                                                                    memberReg = (MemberReg) listCust.get(0);
                                                                                }
                                                                                /*double totalKredit = PstBillMain.getTotalKredit(memberReg.getOID());
                                                                                double totalRetur = PstBillMain.getReturnKredit(memberReg.getOID());
                                                                                double outstanding = totalKredit - totalRetur;
                                                                                double available = memberReg.getMemberCreditLimit() - outstanding;*/
                                                                            %>

                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][25]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(memberReg.getCompName() == "") ? "-" : memberReg.getCompName()%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][26]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(memberReg.getBussAddress() == "") ? "-" : memberReg.getBussAddress()%></td>
                                                                            </tr>

                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"><%=(memberReg.getPersonName() == "") ? "-" : memberReg.getPersonName()%></td>
                                                                            </tr>
                                                                            <%--
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][12]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"  align="right"><%=FRMHandler.userFormatStringDecimal(memberReg.getMemberCreditLimit())%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][13]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%"  align="right"><%=(outstanding == 0) ? 0 : FRMHandler.userFormatStringDecimal(outstanding)%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="30%"><%=textMaterialHeader[SESS_LANGUAGE][14]%></td>
                                                                                <td width="5%">:</td>
                                                                                <td width="65%" align="right"><%=(available == 0) ? 0 : FRMHandler.userFormatStringDecimal(available)%></td>
                                                                            </tr>
                                                                            --%>
                                                                        </table>
                                                                        <%}%>
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
                                                                                <td width="4%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][17]%></td>
                                                                                <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                                <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                                                                                <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                                <td width="8%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                                                <td width="8%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                                                <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][10]%></td>
                                                                                <td width="15%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][11]%></td>
                                                                            </tr>
                                                                            <%
                                                                                        int start = 0;
                                                                                        for (int i = 0; i < listSalesOrder.size(); i++) {
                                                                                            Vector temp = (Vector) listSalesOrder.get(i);
                                                                                            billdetail = (Billdetail) temp.get(0);
                                                                                            Unit unit = (Unit) temp.get(1);
                                                                                            start = start + 1;

                                                                                            double qty = PstBillDetail.getCountStock(billdetail.getBillMainId(), billdetail.getMaterialId());
                                                                                            double disc1 = (billdetail.getItemPrice()) * (billdetail.getDisc1()) / 100;
                                                                                            double discPrice1 = (billdetail.getItemPrice()) - disc1;
                                                                                            double disc2 = discPrice1 * (billdetail.getDisc2()) / 100;
                                                                                            double discPrice2 = discPrice1 - disc2;
                                                                                            double netPrice = discPrice2 - billdetail.getDisc();
                                                                                            double totalDisc = (billdetail.getItemPrice()) - netPrice;
                                                                                            double totalPrice = qty * netPrice;
                                                                            %>
                                                                            <tr>
                                                                                <td width="3%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                                <td width="8%" class="listgensell">&nbsp;<%=billdetail.getSku()%></td>
                                                                                <td width="15%" class="listgensell">&nbsp;<%=billdetail.getItemName()%></td>
                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=qty%></td>
                                                                                <td width="4%" align="center" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                                <td width="5%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(billdetail.getItemPrice())%></td>
                                                                                <td width="5%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(billdetail.getDisc1())%></td>
                                                                                <td width="5%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(billdetail.getDisc2())%></td>
                                                                                <td width="8%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(billdetail.getDisc())%></td>
                                                                                <td width="8%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(billdetail.getTotalDisc())%></td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(totalPrice)%></td>
                                                                                <td width="15%" align="right" class="listgensell">&nbsp;<%=billdetail.getNote()%></td>
                                                                            </tr>
                                                                            <%}%>

                                                                            <tr>
                                                                                <td width="6%" colspan="11" align="center" class="listgensell">
                                                                                    <table width="20%" align="right">
                                                                                        <tr>
                                                                                            <td height="5%" colspan="2">
                                                                                                <b>TOTAL</b>
                                                                                            </td>

                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%">
                                                                                                <b>DISCOUNT</b>
                                                                                            </td>
                                                                                            <td height="5%">
                                                                                                <%if (billMain.getDiscType() == PstBillMain.DISC_TYPE_PCT) {%>
                                                                                                <b><%=(billMain.getDiscPct())%> %</b>
                                                                                                <%} else {%>
                                                                                                <b>- %</b>
                                                                                                <%}%>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%">
                                                                                                <b>TAX</b>
                                                                                            </td>
                                                                                            <td height="5%">
                                                                                                <b><%=(billMain.getTaxPercentage() == 0) ? "-" : (billMain.getTaxPercentage())%> %</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" colspan="2">
                                                                                                <b>GRAND TOTAL</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" colspan="2">
                                                                                                <b>PAYMENT</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%if (cash != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%"></td>
                                                                                            <td  height="5%">
                                                                                                <b>CASH</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (creditCard != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%"></td>
                                                                                            <td height="5%">
                                                                                                <b>CREDIT CARD</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (debitCard != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%"></td>
                                                                                            <td height="5%">
                                                                                                <b>DEBIT CARD</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (chaque != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%"></td>
                                                                                            <td height="5%">
                                                                                                <b>CAHQUE</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (other != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%"></td>
                                                                                            <td height="5%">
                                                                                                <b>OTHER PAYMENT</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}%>

                                                                                        <tr>
                                                                                            <td height="5%" colspan="2">
                                                                                                <b>CHANGE</b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                                <td width="10%" align="right" class="listgensell">
                                                                                    <table width="30%" align="right">
                                                                                        <%
                                                                                                    double amounttotal = PstBillDetail.getTotalAmount(oidBillMain);
                                                                                                    double discValue =0;
                                                                                                    if(billMain.getDiscType() == PstBillMain.DISC_TYPE_PCT){
                                                                                                        discValue = amounttotal * (billMain.getDiscPct()) / 100;
                                                                                                    }else{
                                                                                                        discValue = billMain.getDiscount();
                                                                                                    }
                                                                                                    double amountAftDisc = amounttotal - discValue;
                                                                                                    double taxValue = amountAftDisc * (billMain.getTaxPercentage()) / 100;
                                                                                                    double amountAftTax = amountAftDisc + taxValue; 
                                                                                        %>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=(amounttotal == 0) ? "0.00" : FRMHandler.userFormatStringDecimal(amounttotal)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <%if (billMain.getDiscType() == PstBillMain.DISC_TYPE_PCT) {%>
                                                                                                <b><%=(discValue == 0) ? "0.00" : FRMHandler.userFormatStringDecimal(discValue)%> </b>
                                                                                                <%} else {%>
                                                                                                <b><%=(billMain.getDiscount() == 0) ? "0.00" : FRMHandler.userFormatStringDecimal(billMain.getDiscount())%></b>
                                                                                                <%}%>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=(taxValue == 0) ? "0.00" : FRMHandler.userFormatStringDecimal(taxValue)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(amountAftTax)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=totalPayment == 0 ? "0.00" : FRMHandler.userFormatStringDecimal(totalPayment)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%if (cash != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(cash)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (creditCard != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(creditCard)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (debitCard != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(debitCard)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (chaque != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(chaque)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}
                                                                                                    if (other != 0) {%>
                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=FRMHandler.userFormatStringDecimal(other)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%}%>

                                                                                        <tr>
                                                                                            <td height="5%" align="right">
                                                                                                <b><%=cashretur == 0 ? "0.00" : FRMHandler.userFormatStringDecimal(cashretur)%></b>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                                                             <table width="100%">
                                                    <tr>
                                                        <td align="left"><%=textMaterialHeader[SESS_LANGUAGE][27]%></td>
                                                        <td align="center">:</td>
                                                        <td align="left"><%=billMain.getNotes()%></td>
                                                    </tr>
                                                </table>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top" rowspan="2"></td>
                                                        <td width="35%" valign="top">

                                                        </td>
                                                    </tr>
                                                    <%if ((listSalesOrder != null) && (listSalesOrder.size() > 0)) {%>
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
                                            <td width="25%" align="center">Customer,</td>
                                            <td width="25%" align="center">Sales,</td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="75" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="25%" align="center" nowrap>
                                                (.................................)
                                            </td>
                                            <td width="25%" align="center">
                                                ( <%=salesName%> )
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

