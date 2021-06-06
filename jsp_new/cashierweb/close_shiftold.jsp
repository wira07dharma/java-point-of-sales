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
<%!    //final static int CMD_NONE =0;
    final static int CMD_APPROVAL = 1;
%>
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"username", "password", "Total"},
        {"username", "password", "Total"},};
    public static final String textListHeader[][] = {
        {"No", "User", "Supervisor", "Open at", "Location", "Currency", "Exch.Rate", "Amount", "Subtotal"},
        {"No", "User", "Supervisor", "Open at", "Location", "Currency", "Exch.Rate", "Amount", "Subtotal"}
    };

    /* this method used to list material unit */
    public String drawList(int language, int iCommand, FrmCashCashier frmObject, OpeningCashCashier objEntity, Vector objectClass, long cashCashierId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "5%");
        ctrlist.addHeader(textListHeader[language][1], "20%");
        ctrlist.addHeader(textListHeader[language][2], "20%");
        ctrlist.addHeader(textListHeader[language][3], "20%");
        ctrlist.addHeader(textListHeader[language][4], "20%");

        ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            OpeningCashCashier openingCashCashier = (OpeningCashCashier) objectClass.get(i);
            rowx = new Vector();
            if (cashCashierId == openingCashCashier.getOID()) {
                index = i;
            }

            start = start + 1;

            rowx.add("" + start);
            rowx.add("<div align=\"left\">" + openingCashCashier.getNameUser() + "</div>");
            rowx.add("<div align=\"left\">" + openingCashCashier.getNameSupervisor() + "</div>");
            rowx.add(Formater.formatDate(openingCashCashier.getOpenDate(), "yyyy-MM-dd HH:mm"));
            rowx.add("<div align=\"left\">" + openingCashCashier.getLocation() + "</div>");
            //}
            lstData.add(rowx);
        }

        return ctrlist.draw();
    }
%>
<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            double tmpAmount = FRMQueryString.requestDouble(request, "tmp_amount");
            long oidCashCashier = FRMQueryString.requestLong(request, "hidden_cash_cashier_id");
            double saldoawalRp = FRMQueryString.requestDouble(request, "saldoawalRP");
            double saldoawalUsd = FRMQueryString.requestDouble(request, "saldoawalUSD");

            Vector listCashier = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] + " = " + userId + " AND " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " = '1'", "");
            for (int i = 0; i < listCashier.size(); i++) {
                CashCashier cashCashier = (CashCashier) listCashier.get(i);
                oidCashCashier = cashCashier.getOID();
            }

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

            /**Nilai Should Value*/
            double shouldValueRp = totalCashRp + totalCreditRp;
            double shouldValueUsd = totalCashUsd + totalCreditUsd;

            CtrlCloseCashCashier ctrlCloseCashCashier = new CtrlCloseCashCashier(request);
            ControlLine ctrLine = new ControlLine();
            Vector listCashCashier = new Vector(1, 1);

            /* end switch*/
            FrmCashCashier frmCashCashier = ctrlCloseCashCashier.getForm();
            double amountRp = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]);
            double rupiah = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]);
            double subtotal1 = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]);

            //iErrCode = ctrlCloseCashCashier.action(iCommand,oidCashCashier);

            /*count list All CashCashier*/
            int vectSize = PstCashCashier.getCount(whereClause);

            /*switch list CashCashier*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlCloseCashCashier.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/

            OpeningCashCashier openingCashCashier = ctrlCloseCashCashier.getOpeningCashCashier();
            CashCashier cashCashier = ctrlCloseCashCashier.getCashCashier();
            msgString = ctrlCloseCashCashier.getMessage();

            /* get record to display */
            listCashCashier = PstCashCashier.listOpeningCashier(start, recordToGet, whereClause, orderClause);

            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (listCashCashier.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;   //go to Command.PREV
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                listCashCashier = PstCashCashier.listOpeningCashier(start, recordToGet, whereClause, orderClause);
            }

            long payType = 0;
            Vector listPayType = PstPaymentSystem.list(0, 0, PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " ='1' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " ='0' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " ='0'", "");
            for (int i = 0; i < listPayType.size(); i++) {
                PaymentSystem paymentSystem = (PaymentSystem) listPayType.get(i);
                payType = paymentSystem.getOID();
            }

            int iCmd = Integer.parseInt((request.getParameter("cmd") == null) ? "0" : request.getParameter("cmd"));
            int doapproval = 0;
            int appLanguage = 0;
            int approvalStatus = 0;
            long supervisorId = 0;

            if (iCommand == Command.SAVE) {
                String loginID = FRMQueryString.requestString(request, "login_id");
                String passwd = FRMQueryString.requestString(request, "pass_wd");

                AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
                if (user == null) {
                    approvalStatus = 2;
                } else {
                    if (user.getUserGroupNew() == 1) {
                        supervisorId = user.getOID();
                        approvalStatus = 1;
                        /*switch statement */
                        iErrCode = ctrlCloseCashCashier.action(iCommand, oidCashCashier, request, supervisorId);

                    } else {
                        approvalStatus = 2;
                    }
                }
            }

            //Mencari Shift ID
            String tanggal = Formater.formatDate(new Date(), "HH:mm:ss");
            if ((iCmd == CMD_APPROVAL) && (approvalStatus == 1) && userGroupNewStatus == PstAppUser.GROUP_SALES_ADMIN) {
                response.sendRedirect("../logout.jsp");
            } else if ((iCmd == CMD_APPROVAL) && (approvalStatus == 1) && userGroupNewStatus == PstAppUser.GROUP_CASHIER) {
                iCommand = Command.DETAIL;
            }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <style type="text/css">
            <!--
            .styleFont{
                font-size: 18px;
                font-weight: bold;
            }
            .styleButton{
                color: #000099;
                font-weight: bold;
            }
            -->
        </style>
        <script language="JavaScript">
            function cmdPrintClosingValue(){
                window.open("close_shift_print.jsp?hidden_cash_cashier_id=<%=oidCashCashier%>","closingprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }

            function cmdOpenCashier(){
                document.frmcashcashier.action="open_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cntTotal(element, evt)
            {
                var rupiah = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]%>.value,guiDigitGroup,',');
                var amount1 = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>.value,guiDigitGroup,',');

                if(rupiah==""){
                    rupiah = 0;
                }
                if(amount1==""){
                    amount1 = 0;
                }

                var amount = rupiah * amount1;
                if(isNaN(amount)){
                    amount = "0";
                }
                document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);

            }

            function cntTotal2(element, evt)
            {
                var usd = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_USD]%>.value,guiDigitGroup,'');
                var amount2 = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT2]%>.value,guiDigitGroup,'');


                if(usd==""){
                    usd = 0;
                }
                if(amount2==""){
                    amount2 = 0;
                }

                var amount = usd * amount2;
                if(isNaN(amount)){
                    amount = "0";
                }


                document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL2]%>.value = formatFloat(amount, '', guiDigitGroup, guiDecimalSymbol, decPlace);

            }


            function cmdChangeLocation(){
                document.frmcashcashier.submit();

            }

            function cmdChangeCurrency(){
                document.frmcashcashier.submit();
            }

            function cmdChangeCashMasterId(){
                document.frmcashcashier.command.value="<%=Command.GOTO%>";
                document.frmcashcashier.submit();
            }

            function cmdApproval(){
                document.frmcashcashier.command.value="<%=Command.SAVE%>";
                document.frmcashcashier.action = "close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function fnTrapUserName(){
                if(event.keyCode == 13){
                    document.frmcashcashier.pass_wd.focus();
                }
            }

            function fnTrapPasswd(){
                if(event.keyCode == 13){
                    document.all.aLogin.focus();
                    cmdApproval();
                }
            }

            function keybrdPress(frmObj, event) {
                if(event.keyCode == 13) {
                    switch(frmObj.name) {
                        case 'login_id':
                            document.all.pass_wd.focus();
                            break;
                        case 'pass_wd':
                            document.all.aLogin.focus();
                            cmdApproval();
                            break;
                        case 'app_language':
                            document.all.aLogin.focus();
                            cmdApproval();
                            break;
                        default:
                            break;
                    }
                }
            }

            function cmdAdd()
            {
                document.frmcashcashier.hidden_merk_id.value="0";
                document.frmcashcashier.command.value="<%=Command.ADD%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdAsk(oidCashCashier)
            {
                document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
                document.frmcashcashier.command.value="<%=Command.ASK%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdConfirmDelete(oidCashCashier)
            {
                document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
                document.frmcashcashier.command.value="<%=Command.DELETE%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdSave()
            {
                document.frmcashcashier.command.value="<%=Command.SAVE%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdEdit(oidCashCashier)
            {
                document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
                document.frmcashcashier.command.value="<%=Command.EDIT%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdCancel(oidCashCashier)
            {
                document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
                document.frmcashcashier.command.value="<%=Command.EDIT%>";
                document.frmcashcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdBack()
            {
                document.frmcashcashier.command.value="<%=Command.BACK%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdListFirst()
            {
                document.frmcashcashier.command.value="<%=Command.FIRST%>";
                document.frmcashcashier.prev_command.value="<%=Command.FIRST%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdListPrev()
            {
                document.frmcashcashier.command.value="<%=Command.PREV%>";
                document.frmcashcashier.prev_command.value="<%=Command.PREV%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdListNext()
            {
                document.frmcashcashier.command.value="<%=Command.NEXT%>";
                document.frmcashcashier.prev_command.value="<%=Command.NEXT%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdListLast()
            {
                document.frmcashcashier.command.value="<%=Command.LAST%>";
                document.frmcashcashier.prev_command.value="<%=Command.LAST%>";
                document.frmcashcashier.action="close_shift.jsp";
                document.frmcashcashier.submit();
            }

            function cmdCalculateRp(){
                var costSaldoAwal = cleanNumberFloat(document.frmcashcashier.saldoawalRP.value,guiDigitGroup,'');
                var costCash = cleanNumberFloat(document.frmcashcashier.cashRP.value,guiDigitGroup,'');
                var costCredit = cleanNumberFloat(document.frmcashcashier.ccRP.value,guiDigitGroup,'');
                var costdebit = cleanNumberFloat(document.frmcashcashier.dcRP.value,guiDigitGroup,'');
                var costCheque = cleanNumberFloat(document.frmcashcashier.chequeRP.value,guiDigitGroup,'');
                var refund = cleanNumberFloat(document.frmcashcashier.rfRP.value,guiDigitGroup,'');

                if(isNaN(costSaldoAwal) || (costSaldoAwal==""))
                    costSaldoAwal = 0.0;
                if(isNaN(costCash) || (costCash==""))
                    costCash = 0.0;
                if(isNaN(costCredit) || (costCredit==""))
                    costCredit = 0.0;
                if(isNaN(costdebit) || (costdebit==""))
                    costdebit = 0.0;
                if(isNaN(costCheque) || (costCheque==""))
                    costCheque = 0.0;
                if(isNaN(refund) || (refund==""))
                    refund = 0.0;

                var total = parseFloat(costSaldoAwal)+ parseFloat(costCash)+parseFloat(costCredit)+parseFloat(costdebit)+parseFloat(costCheque)-parseFloat(refund);

                document.frmcashcashier.totalRpView.value = total;
                document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>.value = total;
            }

            function cmdCalculateUsd(){
                var costSaldoAwal = cleanNumberFloat(document.frmcashcashier.saldoawalUSD.value,guiDigitGroup,'');
                var costCash = cleanNumberFloat(document.frmcashcashier.cashUSD.value,guiDigitGroup,'');
                var costCredit = cleanNumberFloat(document.frmcashcashier.ccUSD.value,guiDigitGroup,'');
                var costdebit = cleanNumberFloat(document.frmcashcashier.dcUSD.value,guiDigitGroup,'');
                var costCheque = cleanNumberFloat(document.frmcashcashier.chequeUSD.value,guiDigitGroup,'');
                var refund = cleanNumberFloat(document.frmcashcashier.rfUSD.value,guiDigitGroup,'');

                if(isNaN(costSaldoAwal) || (costSaldoAwal==""))
                    costSaldoAwal = 0.0;
                if(isNaN(costCash) || (costCash==""))
                    costCash = 0.0;
                if(isNaN(costCredit) || (costCredit==""))
                    costCredit = 0.0;
                if(isNaN(costdebit) || (costdebit==""))
                    costdebit = 0.0;
                if(isNaN(costCheque) || (costCheque==""))
                    costCheque = 0.0;
                if(isNaN(refund) || (refund==""))
                    refund = 0.0;

                var total = parseFloat(costSaldoAwal)+ parseFloat(costCash)+parseFloat(costCredit)+parseFloat(costdebit)+parseFloat(costCheque)-parseFloat(refund);

                document.frmcashcashier.totalUsdView.value = total;
                document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL2]%>.value = total;
            }



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

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="noBack();"
          onpageshow="if (event.persisted) noBack();" onunload="" >
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#E1CA9F">
                        <tr>
                            <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
                        </tr>
                        <tr>
                            <td nowrap>
                                <div align="center"> <a href="../logout.jsp">  Logout </a> </div>
                            </td>
                        </tr>
                        <tr>
                            <td height="1" nowrap bgcolor="#A73701"><img src="home_files/spacer.gif" width=1 height=1></td>
                        </tr>
                    </table>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%if (userGroupNewStatus == PstAppUser.GROUP_CASHIER) {%>
                                Cashier - Open CashCashier
                                <%}else{%>
                                Sales Order
                                <%}%>
                                 <!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmcashcashier" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_cash_cashier_id" value="<%=oidCashCashier%>">
                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SPV_CLOSE_OID]%>" value="<%=supervisorId%>">
                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_TYPE]%>" value="1">
                                    <input type="hidden" name="cmd" value="<%=CMD_APPROVAL%>">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="8" valign="middle" colspan="3">
                                                            <hr size="1">
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="14" valign="middle" colspan="3" class="comment">List of Opened CashCashier</td>
                                                    </tr>
                                                    <%
                                                                try {
                                                    %>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE, iCommand, frmCashCashier, openingCashCashier, listCashCashier, oidCashCashier, start)%> </td>
                                                    </tr>
                                                    <%
                                                                } catch (Exception exc) {
                                                                }%>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> </td>
                                                    </tr>
                                                    <tr >
                                                        <td height="22" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    
                                                    <tr align="left" valign="top">
                                                        <td height="22" align="left" colspan="3" class="styleFont">
                                                       <%    if (userGroupNewStatus == PstAppUser.GROUP_CASHIER) {%>
                                                            Closing Balance
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                    <% if (iCommand != Command.DETAIL) {%>
                                                    <tr>
                                                        <td>
                                                            <%
                                                                 if (userGroupNewStatus == PstAppUser.GROUP_CASHIER) {%>
                                                                 <table  width="800" border="1" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td>
                                                                        <table width="800" cellspacing="1" cellpadding="1">
                                                                            <tr>
                                                                                <td width="40%" bgcolor="#ffcc98" align="center"><b>Summary Payment</b></td>
                                                                                <td width="15%" bgcolor="#ffcc98" align="center"><b>Rp.</b></td>
                                                                                <td width="15%" bgcolor="#ffcc98" align="center"><b>USD</b></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">SALDO AWAL</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="saldoawalRP" value="<%=(saldoawalRp != 0) ? (saldoawalRp):"0"%>" onkeyup="javascript:cmdCalculateRp()" readonly></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="saldoawalUSD" value="<%=(saldoawalUsd != 0) ? (saldoawalUsd):"0"%>" onkeyup="javascript:cmdCalculateUsd()" readonly></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">CASH</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="cashRP" value="" onkeyup="javascript:cmdCalculateRp()"></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="cashUSD" value="" onkeyup="javascript:cmdCalculateUsd()"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">CREDIT CARD AMOUNT</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="ccRP" value="" onkeyup="javascript:cmdCalculateRp()"></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="ccUSD" value="" onkeyup="javascript:cmdCalculateUsd()"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">DEBIT CARD AMOUNT</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="dcRP" value="" onkeyup="javascript:cmdCalculateRp()"></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="dcUSD" value="" onkeyup="javascript:cmdCalculateUsd()"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">CHEQUE AMOUNT</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="chequeRP" value="" onkeyup="javascript:cmdCalculateRp()"></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="chequeUSD" value="" onkeyup="javascript:cmdCalculateUsd()"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left">RETUR SALES</td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="rfRP" value="" onkeyup="javascript:cmdCalculateRp()"></td>
                                                                                <td bgcolor="#fbe6d0" align="center"><input align="right" type="text" name="rfUSD" value="" onkeyup="javascript:cmdCalculateUsd()"></td>
                                                                            </tr>
                                                                            <tr bgcolor="#ffcc98">
                                                                                <td colspan="3" bgcolor="#ffcc98">
                                                                                    <hr>
                                                                                </td>
                                                                            </tr>
                                                                            <tr bgcolor="#ffcc98">

                                                                            <tr>
                                                                                <td bgcolor="#ffcc98" align="left"><b>TOTAL CLOSING AMOUNT</b></td>
                                                                                <td bgcolor="#fbe6d0" align="center">
                                                                                    <input type="text" align="right" name="totalRpView" value="">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>" value="">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>" value="<%=shouldValueRp%>">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID]%>" value="1">
                                                                                </td>
                                                                                <td bgcolor="#fbe6d0" align="center">
                                                                                    <input type="text"align="right"  name="totalUsdView" value="">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL2]%>" value="">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT2]%>" value="<%=shouldValueUsd%>">
                                                                                    <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID2]%>" value="2">
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>


                                                            <%} else {
                                                                                                                                 int defaultCurrId = 1;
                                                                                                                                 double rate = 0;
                                                                                                                                 Vector listRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + defaultCurrId + "'", "");
                                                                                                                                 if (listRate.size() > 0) {
                                                                                                                                     StandartRate standartRate = (StandartRate) listRate.get(0);
                                                                                                                                     rate = standartRate.getSellingRate();
                                                                                                                                 }
                                                            %>
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID]%>" value="<%=defaultCurrId%>">
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>" value="<%=rate%>">
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>" value="0">
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT2]%>" value="0">
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_USD]%>" value="0" >
                                                            <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL2]%>" value="0">

                                                            <%}%>

                                                            <fieldset style="margin-top: 22">
                                                                <legend>Approve by supervisor : </legend>
                                                                <br>
                                                                <p><%=textMaterialHeader[SESS_LANGUAGE][0]%>
                                                                    <input type="text" name="login_id" size="20" onKeyDown="javascript:keybrdPress(this, event)">
                                                                    <%=textMaterialHeader[SESS_LANGUAGE][1]%>
                                                                    <input type="password" name="pass_wd" size="15" onKeyPress="javascript:keybrdPress(this, event)">
                                                                </p>
                                                                <p>
                                                                    <label>
                                                                        <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                            <tr>
                                                                                <td ></td>
                                                                                <td ><input id="aLogin" type="button" name="Button" value="Submit" onClick="javascript:cmdApproval()" class="styleButton">
                                                                                </td>
                                                                                <td >&nbsp;</td>
                                                                                <td ></td>
                                                                            </tr>
                                                                        </table>
                                                                    </label>
                                                                </p>

                                                            </fieldset>
                                                            <%if ((iCmd == CMD_APPROVAL) && (approvalStatus > 1)) {%>
                                                            <font class="errfont" color="#FF0000"><%if (appLanguage == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {%>nama atau password salah<%} else {%>
                                                                username or password wrong, try again...
                                                                <%}%></font>
                                                                <%}%>
                                                        </td>
                                                    </tr>
                                                    <%} else {%>
                                                    <tr>
                                                        <td >
                                                            <table width="800" cellspacing="1" cellpadding="1" >
                                                                <tr>
                                                                    <td align="center" width="40%" bgcolor="#ffcc98"><b>Supervisor Confirmation</b></td>
                                                                    <td align="center" width="30%" bgcolor="#ffcc98"><b>Rp.</b></td>
                                                                    <td align="center" width="30%" bgcolor="#ffcc98"><b>USD</b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>OPENING VALUE</b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><b><%=(saldoawalRp != 0) ? FRMHandler.userFormatStringDecimal(saldoawalRp) : "0"%></b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><b><%=(saldoawalUsd != 0) ? FRMHandler.userFormatStringDecimal(saldoawalUsd) : "0"%></b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#fbe6d0" align="left"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>PAYMENT</b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>CASH INVOICE</b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
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
                                                                    <td bgcolor="#ffcc98" align="left">- <%=paymentSystem.getPaymentSystem()%></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(cashPayments1.getCurrencyId() == 1) ? FRMHandler.userFormatStringDecimal(amountCash) : "0"%></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(cashPayments1.getCurrencyId() == 2) ? FRMHandler.userFormatStringDecimal(amountCash) : "0"%></td>
                                                                </tr>
                                                                <%}%>                                                                
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" colspan="3"><hr></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="right"><b>SUB TOTAL</b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalCashRp != 0) ? FRMHandler.userFormatStringDecimal(totalCashRp) : "0"%></b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalCashUsd != 0) ? FRMHandler.userFormatStringDecimal(totalCashUsd) : "0"%></b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td  bgcolor="#fbe6d0" align="left"></td>
                                                                    <td  bgcolor="#fbe6d0" align="right"></td>
                                                                    <td  bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>CREDIT INVOICE</b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <%
                                                                     double amountCredit = 0;
                                                                     String whereClausePayCredit = "CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + "  AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " != 1";
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
                                                                    <td bgcolor="#ffcc98" align="left">- <%=paymentSystem.getPaymentSystem()%></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(cashCreditPaymentsDinamis.getCurrencyId() == 1) ? FRMHandler.userFormatStringDecimal(amountCredit) : "0"%></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(cashCreditPaymentsDinamis.getCurrencyId() == 2) ? FRMHandler.userFormatStringDecimal(amountCredit) : "0"%></td>
                                                                </tr>
                                                                <%}%>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" colspan="3"><hr></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="right"><b>SUB TOTAL</b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalCreditRp != 0) ? FRMHandler.userFormatStringDecimal(totalCreditRp) : "0"%></b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalCreditUsd != 0) ? FRMHandler.userFormatStringDecimal(totalCreditUsd) : "0"%></b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td  bgcolor="#fbe6d0" align="left"></td>
                                                                    <td  bgcolor="#fbe6d0" align="right"></td>
                                                                    <td  bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <%
                                                                     String whereClauseReturRp = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 1 ";
                                                                     String whereClauseReturUsd = " CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CASH_CASHIER_ID] + " = " + oidCashCashier + " AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = 1 AND CBM." + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID] + " = 2 ";
                                                                     double returRp = PstCashCashier.getReturSales(whereClauseReturRp);
                                                                     double returUsd = PstCashCashier.getReturSales(whereClauseReturUsd);
                                                                %>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>RETUR SALES</b></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(returRp != 0) ? FRMHandler.userFormatStringDecimal(returRp) : "0"%></td>
                                                                    <td bgcolor="#fbe6d0" align="right"><%=(returUsd != 0) ? FRMHandler.userFormatStringDecimal(returUsd) : "0"%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#fbe6d0" align="left"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" colspan="3"><hr></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#fbe6d0" align="left"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
                                                                    <td bgcolor="#fbe6d0" align="right"></td>
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
                                                                    <td bgcolor="#ffcc98" align="left"><b>TOTAL</b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalFinalRp != 0) ? FRMHandler.userFormatStringDecimal(totalFinalRp) : "0"%></b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(totalFinalUsd != 0) ? FRMHandler.userFormatStringDecimal(totalFinalUsd) : "0"%></b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>UANG DI LACI</b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(closingValueRp != 0) ? FRMHandler.userFormatStringDecimal(closingValueRp) : "0"%></b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(closingValueUsd != 0) ? FRMHandler.userFormatStringDecimal(closingValueUsd) : "0"%></b></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#ffcc98" align="left"><b>BALANCE</b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(balanceRp != 0) ? FRMHandler.userFormatStringDecimal(balanceRp) : "0"%></b></td>
                                                                    <td bgcolor="#ffcc98" align="right"><b><%=(balanceUsd != 0) ? FRMHandler.userFormatStringDecimal(balanceUsd) : "0"%></b></td>
                                                                </tr>

                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr >
                                                        <td height="22" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="3">
                                                            <table width="50%" border="0" cellpadding="0" cellspacing="0" align="left">
                                                                <tr>
                                                                    <td>
                                                                        <input type="button" name="Button" value="Print Closing Value" onClick="javascript:cmdPrintClosingValue()"  class="styleButton">
                                                                    </td>
                                                                    <td>
                                                                        <input type="button" name="Button" value="Opening Cashier" onClick="javascript:cmdOpenCashier()"  class="styleButton">
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
                                </form>

                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
