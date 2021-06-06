<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*, java.sql.Time,
         com.dimata.posbo.form.admin.service.FrmBackUpService,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*"%>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.pos.entity.balance.*" %>
<%@ page import = "com.dimata.pos.form.balance.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.pos.entity.masterCashier.*" %>
<%@ include file = "../main/javainit.jsp" %>
<!--% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_SHIFT);%-->
<!--%@ include file = "../main/checkuser.jsp" %-->



<!-- Jsp Block -->

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            double tmpAmount = FRMQueryString.requestDouble(request, "tmp_amount");
            long oidCashCashier = FRMQueryString.requestLong(request, "hidden_cash_cashier_id");
            double saldoawalRp = FRMQueryString.requestDouble(request, "saldoawalRP");
            double saldoawalUsd = FRMQueryString.requestDouble(request, "saldoawalUSD");


            /*mencari saldo awal Rupiah*/
            Vector listSaldoAwalRp = PstBalance.list(0, 0, PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND " + PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID] + " = '1'", "");
            for (int i = 0; i < listSaldoAwalRp.size(); i++) {
                Balance balanceRp = (Balance) listSaldoAwalRp.get(i);
                saldoawalRp = balanceRp.getBalanceValue();
            }

            /*mencari saldo awal USD*/
            Vector listSaldoAwalUsd = PstBalance.list(0, 0, PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND " + PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID] + " = '2'", "");
            for (int i = 0; i < listSaldoAwalUsd.size(); i++) {
                Balance balanceUsd = (Balance) listSaldoAwalUsd.get(0);
                saldoawalUsd = balanceUsd.getBalanceValue();
            }

            /*variable declaration*/
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "";

            String whereClauseRp = "";
            String whereClauseUsd = "";

            /*Mencari total transaksi cash*/
            whereClauseRp = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0";
            whereClauseUsd = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 2 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0";
            double cashRp = PstCashCashier.getTotalCashTrans(whereClauseRp);
            double cashUsd = PstCashCashier.getTotalCashTrans(whereClauseUsd);

            double changeCashRp = PstCashCashier.getChange(whereClauseRp);
            double changeCashUsd = PstCashCashier.getChange(whereClauseUsd);

            double totalCashRp = cashRp - changeCashRp;
            double totalCashUsd = cashUsd - changeCashUsd;

            /*Mencari total transaksi credit*/
            whereClauseRp = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 1 AND CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 ";
            whereClauseUsd = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 2 AND CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1 ";
            double creditRp = PstCashCashier.getTotalCreditTrans(whereClauseRp);
            double creditUsd = PstCashCashier.getTotalCreditTrans(whereClauseUsd);

            double changeCreditRp = PstCashCashier.getChange(whereClauseRp);
            double changeCreditUsd = PstCashCashier.getChange(whereClauseUsd);

            double totalCreditRp = creditRp - changeCreditRp;
            double totalCreditUsd = creditUsd - changeCreditUsd;

            /*data header closing cashier*/
            String cashierName = "";
            Date openDate = null;
            Date closeDate = null;
            String namaLocation = "";
            String namaShift = "";
            Vector listDataMain = PstCashCashier.listDataClosingCashier(0, 0, " CC." + PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " = '" + oidCashCashier + "'", "");
            for (int i = 0; i < listDataMain.size(); i++) {
                Vector temp = (Vector) listDataMain.get(i);
                CashCashier cashCashierMain = (CashCashier) temp.get(0);
                AppUser appUser = (AppUser) temp.get(1);
                Location location = (Location) temp.get(2);
                Shift shift = (Shift) temp.get(3);

                cashierName = appUser.getFullName();
                //openDate = cashCashierMain.getOpenDate();
                //closeDate = cashCashierMain.getCloseDate();
                namaLocation = location.getName();
                namaShift = shift.getName();

            }

            Vector listCashCashier = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_CASH_CASHIER_ID] + " = '" + oidCashCashier + "'", "");
            for (int i = 0; i < listCashCashier.size(); i++) {
                CashCashier cashCashier = (CashCashier) listCashCashier.get(i);
                openDate = cashCashier.getOpenDate();
                closeDate = cashCashier.getCloseDate();
            }



            CtrlCloseCashCashier ctrlCloseCashCashier = new CtrlCloseCashCashier(request);
            ControlLine ctrLine = new ControlLine();

            FrmCashCashier frmCashCashier = ctrlCloseCashCashier.getForm();

            int vectSize = PstCashCashier.getCount(whereClause);

            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlCloseCashCashier.actionList(iCommand, start, vectSize, recordToGet);
            }

            OpeningCashCashier openingCashCashier = ctrlCloseCashCashier.getOpeningCashCashier();
            CashCashier cashCashier = ctrlCloseCashCashier.getCashCashier();
            msgString = ctrlCloseCashCashier.getMessage();

            long payType = 0;
            Vector listPayType = PstPaymentSystem.list(0, 0, PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " ='1' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " ='0' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " ='0'", "");
            for (int i = 0; i < listPayType.size(); i++) {
                PaymentSystem paymentSystem = (PaymentSystem) listPayType.get(i);
                payType = paymentSystem.getOID();
            }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <style type="text/css">
            <!--
            .styleFont{
                font-size: 26px;
                text-align: center;
                font-weight: bold;
            }
            .styleButton{
                color: #000099;
                font-weight: bold;
            }
            -->
        </style>
        <script language="JavaScript">
            //-------------- script control line -------------------
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
        </script>
        <SCRIPT type="text/javascript">
                window.history.forward();
                function noBack() { window.history.forward(); }
        </SCRIPT>

        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <!-- #EndEditable -->
    </head>

    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" ></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmcashcashier" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_cash_cashier_id" value="<%=oidCashCashier%>">
                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_TYPE]%>" value="1">

                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3" class="styleFont"> Closing Balance</td>
                                                    </tr>                                                    
                                                    <tr >
                                                        <td height="22" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr >
                                                        <td height="22" valign="middle" colspan="3" align="center">
                                                            <table width="800" cellspacing="1" cellpadding="1" >
                                                                <tr>
                                                                    <td width="50%">
                                                                        <table widht="100%"cellspacing="0" cellpadding="0" >
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Cashier Name</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=cashierName%></b>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Date</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=Formater.formatDate(closeDate, "dd MMM yyyy")%></b>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Location</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=namaLocation%></b>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    <td width="50%">
                                                                        <table widht="100%"cellspacing="0" cellpadding="0" >
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Start time</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=Formater.formatTimeLocale(openDate)%></b>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Closing Time</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=Formater.formatTimeLocale(closeDate)%></b>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td height="15" width="40%" align="left">
                                                                                    <b>Shift</b>
                                                                                </td>
                                                                                <td height="15" width="5%" align="center">
                                                                                    <b>:</b>
                                                                                </td>
                                                                                <td height="15" width="55%" align="left">
                                                                                    <b><%=namaShift%></b>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr >
                                                        <td height="20" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="3" align="center">
                                                            <table width="800" border="1" cellspacing="1" cellpadding="1" >
                                                                <tr>
                                                                    <td>

                                                                        <table width="800" border="1" cellspacing="0" cellpadding="0" >
                                                                            <tr>
                                                                                <td align="center" width="40%" ><b>Summary Payment</b></td>
                                                                                <td align="center" width="30%" ><b>Rp.</b></td>
                                                                                <td align="center" width="30%" ><b>USD</b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td  align="left"><b>OPENING VALUE</b></td>
                                                                                <td  align="right"><b><%=(saldoawalRp != 0) ? FRMHandler.userFormatStringDecimal(saldoawalRp) : "0"%></b></td>
                                                                                <td  align="right"><b><%=(saldoawalUsd != 0) ? FRMHandler.userFormatStringDecimal(saldoawalUsd) : "0"%></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>PAYMENT</b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>CASH INVOICE</b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <%
                                                                                        double amountCash = 0;

                                                                                        String whereClausePayCash = "CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 0 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = 0";
                                                                                        Vector listPayCash = PstCashCashier.listCashClosing(0, 0, whereClausePayCash, "");
                                                                                        for (int i = 0; i < listPayCash.size(); i++) {
                                                                                            Vector temp = (Vector) listPayCash.get(i);
                                                                                            PaymentSystem paymentSystem = (PaymentSystem) temp.get(0);
                                                                                            CashPayments1 cashPayments1 = (CashPayments1) temp.get(1);
                                                                                            CashCreditCard cashCreditCard = (CashCreditCard) temp.get(2);

                                                                                            if (cashPayments1.getPaymentType() == payType) {
                                                                                                if (cashPayments1.getCurrencyId() == 1) {
                                                                                                    amountCash = (cashCreditCard.getAmount()) - changeCashRp;
                                                                                                } else {
                                                                                                    amountCash = (cashCreditCard.getAmount()) - changeCashUsd;
                                                                                                }
                                                                                            } else {
                                                                                                amountCash = cashCreditCard.getAmount();
                                                                                            }
                                                                            %>
                                                                            <tr>
                                                                                <td align="left">- <%=paymentSystem.getPaymentSystem()%></td>
                                                                                <td align="right"><%=(cashPayments1.getCurrencyId() == 1) ? FRMHandler.userFormatStringDecimal(amountCash) : "0"%></td>
                                                                                <td align="right"><%=(cashPayments1.getCurrencyId() == 2) ? FRMHandler.userFormatStringDecimal(amountCash) : "0"%></td>
                                                                            </tr>
                                                                            <%}%>
                                                                            <tr>
                                                                                <td colspan="3"><hr></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="right"><b>SUB TOTAL</b></td>
                                                                                <td align="right"><b><%=(totalCashRp != 0) ? FRMHandler.userFormatStringDecimal(totalCashRp) : "0"%></b></td>
                                                                                <td align="right"><b><%=(totalCashUsd != 0) ? FRMHandler.userFormatStringDecimal(totalCashUsd) : "0"%></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>CREDIT INVOICE</b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <%
                                                                                        double amountCredit = 0;
                                                                                        String whereClausePayCredit = "CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = 1";
                                                                                        Vector listPayCredit = PstCashCashier.listCreditClosing(0, 0, whereClausePayCredit, "");
                                                                                        for (int i = 0; i < listPayCredit.size(); i++) {
                                                                                            Vector temp = (Vector) listPayCredit.get(i);
                                                                                            PaymentSystem paymentSystem = (PaymentSystem) temp.get(0);
                                                                                            CashCreditPaymentsDinamis cashCreditPaymentsDinamis = (CashCreditPaymentsDinamis) temp.get(1);
                                                                                            CashCreditPaymentInfo cashCreditPaymentInfo = (CashCreditPaymentInfo) temp.get(2);

                                                                                            if (cashCreditPaymentsDinamis.getPaymentType() == payType) {
                                                                                                if (cashCreditPaymentsDinamis.getCurrencyId() == 1) {
                                                                                                    amountCredit = (cashCreditPaymentInfo.getAmount()) - changeCreditRp;
                                                                                                } else {
                                                                                                    amountCredit = (cashCreditPaymentInfo.getAmount()) - changeCreditUsd;
                                                                                                }
                                                                                            } else {
                                                                                                amountCredit = cashCreditPaymentInfo.getAmount();
                                                                                            }

                                                                            %>
                                                                            <tr>
                                                                                <td align="left">- <%=paymentSystem.getPaymentSystem()%></td>
                                                                                <td align="right"><%=(cashCreditPaymentsDinamis.getCurrencyId() == 1) ? FRMHandler.userFormatStringDecimal(amountCredit) : "0"%></td>
                                                                                <td align="right"><%=(cashCreditPaymentsDinamis.getCurrencyId() == 2) ? FRMHandler.userFormatStringDecimal(amountCredit) : "0"%></td>
                                                                            </tr>
                                                                            <%}%>
                                                                            <tr>
                                                                                <td colspan="3"><hr></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="right"><b>SUB TOTAL</b></td>
                                                                                <td align="right"><b><%=(totalCreditRp != 0) ? FRMHandler.userFormatStringDecimal(totalCreditRp) : "0"%></b></td>
                                                                                <td align="right"><b><%=(totalCreditUsd != 0) ? FRMHandler.userFormatStringDecimal(totalCreditUsd) : "0"%></b></td>
                                                                            </tr>
                                                                            <%
                                                                                        String whereClauseReturRp = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 1 ";
                                                                                        String whereClauseReturUsd = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 2 ";
                                                                                        double returRp = PstCashCashier.getReturSales(whereClauseReturRp);
                                                                                        double returUsd = PstCashCashier.getReturSales(whereClauseReturUsd);
                                                                            %>
                                                                            <tr>
                                                                                <td align="left"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                                <td align="right"><b></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>RETUR SALES</b></td>
                                                                                <td align="right"><%=(returRp != 0) ? FRMHandler.userFormatStringDecimal(returRp) : "0"%></td>
                                                                                <td align="right"><%=(returUsd != 0) ? FRMHandler.userFormatStringDecimal(returUsd) : "0"%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"></td>
                                                                                <td align="right"></td>
                                                                                <td align="right"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td colspan="3"><hr></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"></td>
                                                                                <td align="right"></td>
                                                                                <td align="right"></td>
                                                                            </tr>
                                                                            <%
                                                                                        double totalFinalRp = saldoawalRp + totalCashRp + totalCreditRp - returRp;
                                                                                        double totalFinalUsd = saldoawalUsd + totalCashUsd + totalCreditUsd - returUsd;
                                                                                        double closingValueRp = 0;
                                                                                        double closingValueUsd = 0;

                                                                                        Vector listClosingValueRp = PstBalance.list(0, 0, PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND " + PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID] + " = '1' AND " + PstBalance.fieldNames[PstBalance.FLD_BALANCE_TYPE] + " = '1'", "");
                                                                                        for (int i = 0; i < listClosingValueRp.size(); i++) {
                                                                                            Balance balanceRp = (Balance) listClosingValueRp.get(0);
                                                                                            closingValueRp = balanceRp.getShouldValue();
                                                                                        }

                                                                                        Vector listClosingValueUsd = PstBalance.list(0, 0, PstBalance.fieldNames[PstBalance.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND " + PstBalance.fieldNames[PstBalance.FLD_CURRENCY_ID] + " = '2' AND " + PstBalance.fieldNames[PstBalance.FLD_BALANCE_TYPE] + " = '1'", "");
                                                                                        for (int i = 0; i < listClosingValueUsd.size(); i++) {
                                                                                            Balance balanceUsd = (Balance) listClosingValueUsd.get(0);
                                                                                            closingValueUsd = balanceUsd.getShouldValue();
                                                                                        }

                                                                                        double balanceRp = totalFinalRp - closingValueRp;
                                                                                        double balanceUsd = totalFinalUsd - closingValueUsd;

                                                                            %>
                                                                            <tr>
                                                                                <td align="left"><b>TOTAL</b></td>
                                                                                <td align="right"><b><%=(totalFinalRp != 0) ? FRMHandler.userFormatStringDecimal(totalFinalRp) : "0"%></b></td>
                                                                                <td align="right"><b><%=(totalFinalUsd != 0) ? FRMHandler.userFormatStringDecimal(totalFinalUsd) : "0"%></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>UANG DI LACI</b></td>
                                                                                <td align="right"><b><%=(closingValueRp != 0) ? FRMHandler.userFormatStringDecimal(closingValueRp) : "0"%></b></td>
                                                                                <td align="right"><b><%=(closingValueUsd != 0) ? FRMHandler.userFormatStringDecimal(closingValueUsd) : "0"%></b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td align="left"><b>BALANCE</b></td>
                                                                                <td align="right"><b><%=(balanceRp != 0) ? FRMHandler.userFormatStringDecimal(balanceRp) : "0"%></b></td>
                                                                                <td align="right"><b><%=(balanceUsd != 0) ? FRMHandler.userFormatStringDecimal(balanceUsd) : "0"%></b></td>
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

                                    </table>
                                </form>

                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>

        </table>
    </body>
    <!-- #EndTemplate --></html>
