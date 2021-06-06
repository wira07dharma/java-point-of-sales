<%--
    Document   : paymentTypeCredit.jsp
    Created on : 02 Jul 13, 11:44:26
    Author     : Wiweka
--%>
<%@ page language = "java" %>
<%@ page import="java.util.*"%>
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.common.entity.payment.*"%>
<%@ page import="com.dimata.common.form.payment.*"%>

<%@ page import= "com.dimata.pos.entity.billing.BillMain,
         com.dimata.pos.entity.billing.PstBillMain,
         com.dimata.pos.form.billing.CtrlBillMain,
         com.dimata.pos.form.billing.FrmBillMain,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.jsp.JspInfo,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.session.masterdata.SessDiscountCategory" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import=" com.dimata.pos.entity.payment.*" %>
<%@ page import="com.dimata.pos.form.payment.*" %>
<%@ page import="com.dimata.common.entity.payment.*"%>
<%@ page import="com.dimata.common.form.payment.*"%>
<%@ page import="com.dimata.pos.entity.balance.*" %>
<%@ page import="com.dimata.pos.form.balance.*"%>
<%@ page import="com.dimata.pos.entity.masterCashier.*"%>
<%@ page import="com.dimata.pos.form.masterCashier.*"%>

<%@ include file = "../main/javainit.jsp" %>
<!--% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATEGORY);%>
<!--%@ include file = "../main/checkuser.jsp" %>
<%
            boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "Type", "Currancy", "Rate", "Amount", "Total"},
        {"No", "Type", "Currancy", "Rate", "Amount", "Total"}
    };

    public String drawList(int language, int iCommand, FrmCashCreditPaymentMain frmObject, CreditPaymentMain objEntity, Vector objectClass, long oidPaymentMain, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "4%");
        ctrlist.addHeader(textListHeader[language][1], "20%");
        ctrlist.addHeader(textListHeader[language][2], "8%");
        ctrlist.addHeader(textListHeader[language][3], "8%");
        ctrlist.addHeader(textListHeader[language][4], "10%");
        ctrlist.addHeader(textListHeader[language][5], "10%");

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
            Vector temp = (Vector) objectClass.get(i);
            CreditPaymentMain creditPaymentMain = (CreditPaymentMain) temp.get(0);
            CashCreditPaymentsDinamis cashCreditPaymentsDinamis = (CashCreditPaymentsDinamis) temp.get(1);
            CashCreditPaymentInfo cashCreditPaymentInfo = (CashCreditPaymentInfo) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);
            rowx = new Vector();

            double totalAmount = cashCreditPaymentsDinamis.getAmount() * cashCreditPaymentsDinamis.getRate();

            String type="";
            Vector list = PstPaymentSystem.list(0, 0, PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_SYSTEM_ID] + "='"+cashCreditPaymentsDinamis.getPaymentType()+"'", "");
            
            if (list != null && list.size() > 0) {
                PaymentSystem paySystem = (PaymentSystem) list.get(0);
                type = paySystem.getPaymentSystem();
            }

            start = start + 1;

            rowx.add("" + start);
            rowx.add("<div align=\"left\">" + type + "</div>");
            rowx.add("<div align=\"left\">" + currencyType.getCode() + "</div>");
            rowx.add("<div align=\"left\">" + cashCreditPaymentsDinamis.getRate() + "</div>");
            rowx.add("<div align=\"left\">" + FRMHandler.userFormatStringDecimal(cashCreditPaymentsDinamis.getAmount()) + "</div>");
            rowx.add("<div align=\"left\">" + FRMHandler.userFormatStringDecimal(totalAmount) + "</div>");

            lstData.add(rowx);
        }
        return ctrlist.draw();
    }
%>

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            double payment = FRMQueryString.requestDouble(request, "payment");
            double allPayment = FRMQueryString.requestDouble(request, "all_payment");
            double balance = FRMQueryString.requestDouble(request, "balance");
            double paid = FRMQueryString.requestDouble(request, "paid");
            int paidFor = FRMQueryString.requestInt(request, "paid_for");
            double totalDeposit = FRMQueryString.requestDouble(request, "total_deposit");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long idBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            double amount = FRMQueryString.requestDouble(request, "amount");
            double pay = FRMQueryString.requestDouble(request, "pay");
            long oidPaymentMain = FRMQueryString.requestLong(request, "hidden_payment_main_id");

            long cashCashierId = FRMQueryString.requestLong(request, "cash_cashier_id");
            long locationId = FRMQueryString.requestLong(request, "location_id");
            long appUser = FRMQueryString.requestLong(request, "app_user_id");
            long shiftId = FRMQueryString.requestLong(request, "shift_id");
            long payType = FRMQueryString.requestLong(request, "pay_type");

            int transStatus = 0;

            /*variable declaration*/
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "";
            Vector listCreditPaymet = new Vector(1, 1);
            int idNotaType = PstBillMain.OPEN_CREDIT_INVOICE;

            if(cashCashierId == 0){
            Vector listCashCashier = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] + " ='" + appUser + "' AND " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " = '1'", "");
            for (int i = 0; i < listCashCashier.size(); i++) {
                CashCashier cashCashier = (CashCashier) listCashCashier.get(i);
                cashCashierId = cashCashier.getOID();
            }
            }

            /** Untuk Credit Payment **/
            CtrlCashCreditPaymentMain ctrlCashCreditPaymentMain = new CtrlCashCreditPaymentMain(request);
            ctrlCashCreditPaymentMain.setLanguage(SESS_LANGUAGE);

            iErrCode = ctrlCashCreditPaymentMain.action(iCommand, oidPaymentMain, idBillMain, amount);

            FrmCashCreditPaymentMain frmCashCreditPaymentMain = ctrlCashCreditPaymentMain.getForm();
            CreditPaymentMain creditPaymentMain = ctrlCashCreditPaymentMain.getCreditPaymentMain();
            msgString = ctrlCashCreditPaymentMain.getMessage();

            whereClause = "CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'";
            int vectSize = PstCreditPaymentMain.getCountPayment(whereClause);

            listCreditPaymet = PstCreditPaymentMain.listCreditPayment(start, recordToGet, whereClause, orderClause);
            pay = PstCreditPaymentMain.getSumPayment(whereClause);

            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (listCreditPaymet.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;   //go to Command.PREV
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                listCreditPaymet = PstCreditPaymentMain.listCreditPayment(start, recordToGet, whereClause, orderClause);
            }


            Vector paySysKey = new Vector(1, 1);
            Vector paySysValue = new Vector(1, 1);
            Vector vPaymentSystem = PstPaymentSystem.listAll();

            if (vPaymentSystem != null && vPaymentSystem.size() > 0) {
                for (int p = 0; p < vPaymentSystem.size(); p++) {
                    PaymentSystem ps = (PaymentSystem) vPaymentSystem.get(p);
                    paySysKey.add("" + ps.getPaymentSystem());
                    paySysValue.add("" + ps.getOID());
                }
            }

            Vector vBankInfo = new Vector();
            Vector vCardInfo = new Vector();
            Vector vCheckBgInfo = new Vector();
            Vector vDueDateInfo = new Vector();

            if (vPaymentSystem != null && vPaymentSystem.size() > 0) {
                PaymentSystem paymentSystem = new PaymentSystem();
                for (int i = 0; i < vPaymentSystem.size(); i++) {
                    paymentSystem = (PaymentSystem) vPaymentSystem.get(i);
                    if (paymentSystem.isBankInfoOut()) {
                        vBankInfo.add(paymentSystem.getOID() + "");
                    } else if (paymentSystem.isCardInfo()) {
                        vCardInfo.add(paymentSystem.getOID() + "");
                    } else if (paymentSystem.isCheckBGInfo()) {
                        vCheckBgInfo.add(paymentSystem.getOID() + "");
                    }
                    if (paymentSystem.isDueDateInfo()) {
                        vDueDateInfo.add(paymentSystem.getOID() + "");
                    }
                }
            }
//------------------------
            CashCreditPaymentInfo cashCreditPaymentInfo = new CashCreditPaymentInfo();
            CashCreditPaymentsDinamis cashCreditPaymentsDinamis = new CashCreditPaymentsDinamis();

            double creditCard = FRMQueryString.requestDouble(request, "credit_card");
            double cash = FRMQueryString.requestDouble(request, "cash");
            double cheque = FRMQueryString.requestDouble(request, "cheque");
            double debitCard = FRMQueryString.requestDouble(request, "debit_card");
            double other = FRMQueryString.requestDouble(request, "other_payment");

            String whereCredit = "PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " = '1' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " = '0' and CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'";
            String whereCheque = "PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " = '1' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " = '0' and CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'";
            String whereDebit = "PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " = '2' and CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'";
            String whereCash = "PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " = '0' AND PS." + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " = '1' and CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + idBillMain + "'";
            String whereOther = "PS." +PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO]+" = '0' AND PS."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO]+" = '0' AND PS."+PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE]+" = '0' and CPM."+PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID]+" = '"+idBillMain+"'";

            creditCard = PstCreditPaymentMain.getSumSystemPayment(whereCredit);
            cash = PstCreditPaymentMain.getSumSystemPayment(whereCash);
            cheque = PstCreditPaymentMain.getSumSystemPayment(whereCheque);
            debitCard = PstCreditPaymentMain.getSumSystemPayment(whereDebit);
            other = PstCashPayment1.getSumSystemPayment(whereOther); 



%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <% if (iCommand == Command.SAVE) {%>
                self.opener.document
            <% }%>

                function cmdPrint() {

                    window.open("cashier_invoice_print.jsp?credit_card=<%=creditCard%>&debit_card=<%=debitCard%>&cheque=<%=cheque%>&cash=<%=cash%>&other=<%=other%>&hidden_bill_main_id=<%=idBillMain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","multiplecreditpayment","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                    self.opener.document.forms.frmcashier.commandTmp.value = <%=Command.RESET%>;
                    self.opener.document.forms.frmcashier.submit();
                    self.close();
                }

                function cmdAsk(){
                    document.frmpaymenttype.command.value="<%=Command.ASK%>";
                    document.frmpaymenttype.prev_command.value="<%=prevCommand%>";
                    document.frmpaymenttype.action="paymentTypeCredit.jsp";
                    document.frmpaymenttype.submit();
                }

                function cmdConfirmDelete(){
                    document.frmpaymenttype.command.value="<%=Command.DELETE%>";
                    document.frmpaymenttype.prev_command.value="<%=prevCommand%>";
                    document.frmpaymenttype.action="paymentTypeCredit.jsp";
                    document.frmpaymenttype.submit();
                }

                function cmdSave(){
                    document.frmpaymenttype.command.value="<%=Command.SAVE%>";
                    document.frmpaymenttype.prev_command.value="<%=prevCommand%>";
                    document.frmpaymenttype.action="paymentTypeCredit.jsp";
                    document.frmpaymenttype.submit();
                }



                function cmdEdit(){
                    document.frmpaymenttype.command.value="<%=Command.EDIT%>";
                    document.frmpaymenttype.prev_command.value="<%=prevCommand%>";
                    document.frmpaymenttype.action="paymentTypeCredit.jsp";
                    document.frmpaymenttype.submit();
                }

                function cmdCancel(){
                    document.frmpaymenttype.command.value="<%=Command.EDIT%>";
                    document.frmpaymenttype.prev_command.value="<%=prevCommand%>";
                    document.frmpaymenttype.action="paymentTypeCredit.jsp";
                    document.frmpaymenttype.submit();
                }

                function cmdSavePayment() {
                    var amount = document.frmpaymenttype.<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value;
                        alert(amount);
                    if(amount <= 0 || amount == ""){
                        alert("data can not empty !");
                        document.frmpaymenttype.<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.focus();
                    }else{
                        document.frmpaymenttype.command.value="<%=Command.SAVE%>";
                        document.frmpaymenttype.action="paymentTypeCredit.jsp";
                        document.frmpaymenttype.submit();
                    }
                }


                function keybrdPressTotal(frmObj, event) {
                    if(event.keyCode == 13) {
                        switch(frmObj.name) {
                            case '<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>':
                                document.all.ButtonSavePayment.focus();
                                break;
                            default:
                                break;
                            }
                        }
                    }


                function changePaymentSystem() {
                
                    var F = document.frmpaymenttype;
                    var infoSts = false;
                    var dueDateSts = false;
                    var paymentId = F.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>.value;

            <%
                        for (int i = 0; i < vBankInfo.size(); i++) {
            %>
                    if(paymentId == '<%=vBankInfo.get(i)%>'){
                        infoSts = true;
                        document.all.infoTr.style.display = '';
                        setDisplayInfo('', '', '', '', '', 'none', 'none', 'none', 'none', 'none', 'none');
                    }
            <%
                        }
                        for (int i = 0; i < vCardInfo.size(); i++) {
            %>
                    if(paymentId == '<%=vCardInfo.get(i)%>'){
                        
                        infoSts = true;
                        document.all.infoTr.style.display = '';
                        setDisplayInfo('', '', '', '', '', '', '', '', '', '', 'none')
                    }
            <%
                        }
                        for (int i = 0; i < vCheckBgInfo.size(); i++) {
            %>
                    if(paymentId == '<%=vCheckBgInfo.get(i)%>'){
                        infoSts = true;
                        document.all.infoTr.style.display = '';
                        setDisplayInfo('', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none')
                    }
            <%}

                        for (int i = 0; i < vDueDateInfo.size(); i++) {
            %>
                    if(paymentId == '<%=vDueDateInfo.get(i)%>'){
                        dueDateSts = true;
                        document.all.infoTr.style.display = '';
                        document.all.dueDate.style.display = '';
                    }
            <%
                        }

            %>
                    if(!infoSts){
                        if(!dueDateSts){
                            document.all.infoTr.style.display = 'none';
                        }
                        setDisplayInfo('none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none')
                    }
                    if(!dueDateSts){
                        document.all.dueDate.style.display = 'none';
                    }
                }

                function setDisplayInfo(ibankName, ibankAdd, iSwiftCode, iAccName, iAccNum, iNameOnCard, iCardNum,
                iCardId, iExpDate, iPayAdd, iBGCheckNum){
                    document.all.bankName.style.display = ibankName;
                    document.all.bankAddress.style.display = ibankAdd;
                    document.all.swiftCode.style.display = iSwiftCode;
                    document.all.accountName.style.display = iAccName;
                    document.all.accountNumber.style.display = iAccNum;
                    document.all.nameOnCard.style.display = iNameOnCard;
                    document.all.cardNumber.style.display = iCardNum;
                    document.all.cardId.style.display = iCardId;
                    document.all.expiredDate.style.display = iExpDate;
                    document.all.payAddress.style.display = iPayAdd;
                    document.all.bgCheckNumber.style.display = iBGCheckNum;
                }

                function fillAmount(){
                    var amountTmp = cleanNumberFloat(document.frmpaymenttype.amount.value,guiDigitGroup,'');
                    var payTmp = cleanNumberFloat(document.frmpaymenttype.pay.value,guiDigitGroup,'');

                    if(isNaN(amountTmp) || (amountTmp==""))
                        amountTmp = 0.0;
                    if(isNaN(payTmp) || (payTmp==""))
                        payTmp = 0.0;

                    var payNext = amountTmp - payTmp;

                    document.frmpaymenttype.<%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = formatFloat(payNext, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                }

                function cmdChangeCurrency(){
                    document.frmpaymenttype.submit();
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
        <style type="text/css">
            <!--
            .style1 {
                color: #009900;
                font-weight: bold;
            }
            .style3 {font-size: 24px}
            .style4 {
                font-size: 26px;
                text-align: right;
                background-color: #fcfded;

            }
            .style5 {
                font-size: 26px;
                text-align: right;
            }
            -->
        </style>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
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
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmpaymenttype" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_payment_main_id" value="<%=oidPaymentMain%>">
                                    <input type="hidden" name="hidden_bill_main_id" value="<%=idBillMain%>">
                                    <input type="hidden" name="amount" value="<%=amount%>">
                                    <input type="hidden" name="pay" value="<%=pay%>">
                                    <input type="hidden" name="credit_card" value="<%=creditCard%>">
                                    <input type="hidden" name="cheque" value="<%=cheque%>">
                                    <input type="hidden" name="debit_card" value="<%=debitCard%>">
                                    <input type="hidden" name="cash" value="<%=cash%>">

                                    <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                                    <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_APPUSER_ID]%>" value="<%=appUser%>">
                                    <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
                                    <input type="text" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashCashierId%>">
                                    <!--input type="hidden" name="<!--%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>" value="<!--%=payType%>"-->
                                    
                                    <input type="hidden" name="cash_cashier_id" value="<%=cashCashierId%>">
                                    <input type="hidden" name="shift_id" value="<%=shiftId%>">
                                    <input type="hidden" name="app_user_id" value="<%=appUser%>">
                                    <input type="hidden" name="location_id" value="<%=locationId%>">
                                    <input type="hidden" name="pay_type" value="<%=payType%>">

                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>
                                        <tr align="right" valign="top">
                                            <td>&nbsp;</td>
                                            <td><span class="style5">Rp.</span></td>
                                            <td><span class="style5"><%=FRMHandler.userFormatStringDecimal(amount)%></span></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                        </tr>

                                        <tr align="left" valign="top">
                                            <td colspan="3">
                                                <fieldset>
                                                    <legend><span class="style1">Payment Info</span></legend>
                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                        <tr align="left" valign="top">
                                                            <td>

                                                                <%try {%>
                                                                <table width="60%" border="0" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td colspan="2"><b><%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE], null, "" + cashCreditPaymentsDinamis.getPaymentType(), paySysValue, paySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%></b></td>
                                                                        <td></td>
                                                                    </tr>
                                                                    <%
                                                                        if (iCommand == Command.SAVE && creditPaymentMain.getOID() != 0) {
                                                                            Vector v = PstPaymentInfo.getListPaymentInfo(creditPaymentMain.getOID());
                                                                            if (v != null && v.size() > 0) {
                                                                                cashCreditPaymentInfo = (CashCreditPaymentInfo) v.get(0);
                                                                            }
                                                                        }
                                                                    %>
                                                                    <tr id="infoTr" style="YES; display: none;">
                                                                        <td colspan="2">
                                                                            <div align="left">
                                                                                <table>
                                                                                    <tr id="bankName" style="YES; display: none;">
                                                                                        <td nowrap>Credit Card Type</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="<%=FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_CC_NAME]%>" size="30" value="<%=cashCreditPaymentInfo.getCcName()%>" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="bankAddress" style="YES; display: none;">
                                                                                        <td nowrap>Bank Address</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <textarea name="" cols="30" rows="2"  class="formElemen"></textarea>                    </td>
                                                                                    </tr>
                                                                                    <tr id="swiftCode" style="YES; display: none;">
                                                                                        <td nowrap>Swift Code</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="" size="30" value="" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="accountName" style="YES; display: none;">
                                                                                        <td nowrap>Account Name</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="" size="30" value="" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="accountNumber" style="YES; display: none;">
                                                                                        <td nowrap>Account Number</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="" size="30" value="" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="nameOnCard" style="YES; display: none;">
                                                                                        <td nowrap>Name on Card</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="<%=FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_HOLDER_NAME]%>" size="30" value="<%=cashCreditPaymentInfo.getHolderName()%>" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="cardNumber" style="YES; display: none;">
                                                                                        <td nowrap>Card Number</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="<%=FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_CC_NUMBER]%>" size="30" value="<%=cashCreditPaymentInfo.getCcNumber()%>" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="cardId" style="YES; display: none;">
                                                                                        <td nowrap>Holder Name</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="<%=FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_HOLDER_NAME]%>" size="30" value="" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="expiredDate" style="YES; display: none;">
                                                                                        <td nowrap>Expired Date</td>
                                                                                        <td>:</td>
                                                                                        <td><%=ControlDate.drawDateMY(FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_EXPIRED_DATE], cashCreditPaymentInfo.getExpiredDate() == null ? new Date() : cashCreditPaymentInfo.getExpiredDate(), "", "formElemen", 0, 5)%></td>
                                                                                    </tr>
                                                                                    <tr id="payAddress" style="YES; display: none;">
                                                                                        <td nowrap>Address</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <textarea name=" cols="30" rows="2"  class="formElemen"></textarea>                    </td>
                                                                                    </tr>
                                                                                    <tr id="bgCheckNumber" style="YES; display: none;">
                                                                                        <td nowrap>BG/Check Number</td>
                                                                                        <td>:</td>
                                                                                        <td>
                                                                                            <input type="text" name="" size="30" value="" class="formElemen">                    </td>
                                                                                    </tr>
                                                                                    <tr id="dueDate" style="YES; display: none;">
                                                                                        <td nowrap>Due Date</td>
                                                                                        <td>:</td>
                                                                                        <td><%=ControlDate.drawDate(FrmCashCreditPaymentInfo.fieldNames[FrmCashCreditPaymentInfo.FRM_FIELD_CHEQUE_DUE_DATE], cashCreditPaymentInfo.getChequeDueDate() == null ? new Date() : cashCreditPaymentInfo.getChequeDueDate(), "formElemen", 0, 0)%></td>
                                                                                    </tr>
                                                                                </table>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td width="51%">&nbsp;</td>
                                                                        <td width="49%">&nbsp;</td>
                                                                    </tr>
                                                                </table>
                                                                <%} catch (Exception e) {
                                                                                System.out.println(e.toString());
                                                                            }%>

                                                            </td>
                                                            <td>
                                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                    <tr>
                                                                        <td><%
                                                                                    long selectedCurrencyId = FRMQueryString.requestLong(request, FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CURRENCY_ID]);
                                                                                    double tmpAmount = 0;
                                                                                    Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                                                    Vector vectCurrVal = new Vector(1, 1);
                                                                                    Vector vectCurrKey = new Vector(1, 1);
                                                                                    for (int i = 0; i < listCurr.size(); i++) {

                                                                                        CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                                                                        vectCurrKey.add(currencyType.getCode());
                                                                                        vectCurrVal.add("" + currencyType.getOID());

                                                                                    }
                                                                                    String select_Cur = "0";
                                                                                    if (selectedCurrencyId != 0) {
                                                                                        select_Cur = "" + selectedCurrencyId;
                                                                                        Vector listAmount = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + selectedCurrencyId + "'", "");
                                                                                        for (int i = 0; i < listAmount.size(); i++) {
                                                                                            StandartRate standartRate = (StandartRate) listAmount.get(i);
                                                                                            tmpAmount = standartRate.getSellingRate();
                                                                                        }
                                                                                    }



                                                                            %>
                                                                            <%=ControlCombo.draw(FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_CURRENCY_ID], "formElemen", null, select_Cur, vectCurrVal, vectCurrKey, "onChange=\"javascript:cmdChangeCurrency()\"")%>
                                                                            <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]%>" value="<%=tmpAmount%>">
                                                                        </td>
                                                                        <td><input type="text" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>" value="" onfocus="javascript:fillAmount()" onkeypress="javascript:keybrdPressTotal(this, event)"></td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <%if (pay < amount) {%>
                                                                <input type="button" name="ButtonSavePayment" value="Save Payment" onClick="javascript:cmdSavePayment()" class="styleButton">
                                                                <!--a href="javascript:cmdSavePayment()" onKeyDown="javascript:keybrdPress(this)"><span >SAVE</span></a-->
                                                                <%}%>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                        <tr>
                                                            <td>&nbsp</td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                        <tr>
                                                            <td>&nbsp</td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                        <%
                                                                    try {
                                                        %>
                                                        <tr >
                                                            <td colspan="2"> <%=drawList(SESS_LANGUAGE, iCommand, frmCashCreditPaymentMain, creditPaymentMain, listCreditPaymet, oidPaymentMain, start)%> </td>
                                                        </tr>
                                                        <%
                                                                    } catch (Exception exc) {
                                                                    }%>
                                                        <tr>
                                                            <td>
                                                                <%if (pay >= amount) {%>
                                                                <input type="button" name="ButtonPrint" value="Print" onClick="javascript:cmdPrint()" class="styleButton">
                                                                <%}%>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                    </table>
                                                </fieldset>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
                                            <td>&nbsp;</td>
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
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
