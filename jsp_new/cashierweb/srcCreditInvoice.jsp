<%--
    Document   : bill_detail_stockcode.jsp
    Created on : 02 Jul 13, 11:44:26
    Author     : Wiweka
--%>
<%@page import="com.dimata.common.form.payment.FrmPaymentInfo"%>
<%@page import="com.dimata.posbo.form.arap.FrmAccPayable"%>
<%@page import="com.dimata.posbo.form.arap.FrmAccPayableDetail"%>
<%@page import="com.dimata.posbo.form.arap.CtrlAccPayable"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*,
         com.dimata.pos.entity.billing.BillMain,
         com.dimata.pos.entity.billing.PstBillMain,
         com.dimata.pos.form.billing.CtrlBillMain,
         com.dimata.pos.form.billing.FrmBillMain,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.gui.jsp.ControlList,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.jsp.JspInfo,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.session.masterdata.SessDiscountCategory,
         com.dimata.common.entity.payment.*,
         com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<%
        int appObjCodeKasirInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_INVOICE);
        int appObjCodeKasirCreditInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_CREDIT_INVOICE);
        int appObjCodeKasirRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_RETUR);
        int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT);

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalKasirInv = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirInvoice, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirKreditInv = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirCreditInvoice, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirRetur= userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirRetur, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirPay = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));
%>
<%
            boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
          {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Person Name"},
        {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Person Name"}
    };
    public static final String textListHeader[][] = {
        {"Code", "Date", "Customer Name", "Person Name", "Total"},
        {"Code", "Date", "Customer Name", "Person Name", "Total"}
};

public static final String textGlobalTitle[][] = {
	{
	 "Gudang","Penerimaan Barang","Rekap Hutang","Kembali ke Pencarian","Tambah Pembayaran","Simpan Pembayaran","Kembali Ke Daftar","Rincian Pembayaran", //0-7
	 "Total Pembayaran","Saldo Hutang","Sub Total","Grand Total","Daftar hutang tidak ditemukan!","Daftar pembayaran hutang tidak ditemukan!", //8-13
	 "Hutang","Pembayaran","Rincian Sistem Pembayaran","Cetak Laporan Rekap Hutang","Jumlah pembayaran melebihi saldo hutang!", //14-18
	 "Data belum lengkap!", "Kembali ke Penerimaan"
	},
	{
	 "Warehouse","Receive Goods","AP Summary","Back To Search","Add New Payment","Save Payment","Back To List","Detail Of Payment", //0-7
	 "Total of Payment","AP Balance","Sub Total","Grand Total","Account Payable list not found!","Payment list not found!", //8-13
	 "AP","Payment","Payment System Info", "Print AP Summary Report","AP payment more than AP balance!", //14-18
	 "Incomplete data!", "Back To Receive"
	}
};

public static final String textListHeaderDetail[][] = {
	{"Tanggal","Lokasi","Sistem Pembayaran","Mata Uang","Rate","Jumlah dalam Mata Uang","Jumlah"},
	{"Date","Location","Payment System","Currency","Rate","Amount in Currency","Amount"}
};

public static final String textListPaymentInfo[][] = {
	{"Nama Bank","Alamat Bank","Kode Swift","Nama Rekening","Nomor Rekening","Nama pada Kartu","Nomor Kartu","Id Kartu", //0-7
	"Kadarluwarsa","Tempat Pembayaran","Nomor BG/Check","Jatuh Tempo"}, //8-11
	{"Bank Name","Bank Address","Swift Code","Account Name","Account Number","Name on Card","Card Number","Card Id", //0-7
	"Expired Date","Pay Address","BG/Check Number","Due Date"} //8-11
};

/* this method used to list material department */
public String drawList(int language,Vector objectClass,long billMainId, int start)
{
    String result = "";
        if (objectClass != null && objectClass.size() > 0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.dataFormat("No","3%","center","left");
        ctrlist.dataFormat(textListHeader[language][0],"12%","center","left");
        ctrlist.dataFormat(textListHeader[language][1],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][2],"20%","center","left");
        ctrlist.dataFormat(textListHeader[language][3],"10%","center","left");
        ctrlist.dataFormat(textListHeader[language][4],"10%","center","left");

        ctrlist.setLinkRow(1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;

        for(int i=0; i<objectClass.size(); i++)
        {
                //BillMain billMain = (BillMain)objectClass.get(i);
                Vector vt = (Vector) objectClass.get(i);
                BillMain billMain = (BillMain) vt.get(0);
                MemberReg memberReg = (MemberReg) vt.get(1);
                CurrencyType currencyType = (CurrencyType) vt.get(2);
                AppUser appUser = (AppUser) vt.get(3);

                Vector rowx = new Vector();

                if(billMainId == billMain.getOID()){
                  index = i;
                }

                rowx.add("<div align=\"center\">"+(i+start+1)+"</div>");
                rowx.add(billMain.getInvoiceNumber());
                rowx.add("<div align=\"center\">" + Formater.formatDate(billMain.getBillDate(),"dd-MM-yyyy")+"</div>");
                rowx.add("<div align=\"right\">" +memberReg.getCompName()+"</div>");
                rowx.add("<div align=\"right\">" +memberReg.getPersonName()+"</div>");
                rowx.add("<div align=\"right\">" +FRMHandler.userFormatStringDecimal(billMain.getAmount())+"</div>");

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(billMain.getOID()));
        }
        result = ctrlist.draw();
                } else {
                    result = "<div class=\"msginfo\">&nbsp;&nbsp;Data is empty...</div>";
                }
                return result;
            }
%>

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            Date billDate = FRMQueryString.requestDate(request, "biil_date");
            String invoiceNumber = FRMQueryString.requestString(request, "inv_number");
            long customerId = FRMQueryString.requestLong(request, "cust_id");
            String custName = FRMQueryString.requestString(request, "cust_name");
            String personName = FRMQueryString.requestString(request, "person_name");

            int recordToGet = 15;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = '0' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '1' AND " + PstBillMain.fieldNames[PstBillMain.FLD_CUSTOMER_ID] +" = '"+customerId+"'";
            String orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            ControlLine ctrLine = new ControlLine();
            Vector listCreditInvoice = new Vector(1, 1);

            iErrCode = ctrlBillMain.action(iCommand, oidBillMain, 0);
            FrmBillMain frmBillMain = ctrlBillMain.getForm();
            int statusDate = FRMQueryString.requestInt(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_STATUS_DATE]);
            Date datefrom = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_FROM]);
            Date dateto = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_TO]);

// count list All MatDepartment
            int vectSize = PstBillMain.getCount(whereClause);

            BillMain billMain = ctrlBillMain.getBillMain();
            msgString = ctrlBillMain.getMessage();

            BillMain bMain = new BillMain();
            bMain.setBillDate(billDate);
            bMain.setInvoiceNumber(invoiceNumber);
            bMain.setCustomerId(customerId);
            bMain.setStatusDate(statusDate);
            bMain.setDatefrom(datefrom);
            bMain.setDateto(dateto);
            bMain.setCustName(custName);
            bMain.setPersonName(personName);

            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }

// get record to display
            orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            listCreditInvoice = PstBillMain.listSrc(start, bMain, recordToGet, whereClause, orderClause);

// handle condition if size of record to display=0 and start>0 after delete
            if (listCreditInvoice.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listCreditInvoice = PstBillMain.listSrc(start, bMain, recordToGet, whereClause, orderClause);
            }


          //payment
            CtrlAccPayable ctrlAccPayable = new CtrlAccPayable(request);
            PaymentInfo paymentInfo = ctrlAccPayable.getPaymentInfo();
            Vector listPaymentSystem = PstPaymentSystem.list(0, 0, "", "");
            Vector listCurrencyType = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE, "");

            Vector vBankInfo = new Vector();
            Vector vCardInfo = new Vector();
            Vector vCheckBgInfo = new Vector();
            Vector vDueDateInfo = new Vector();

            if(listPaymentSystem != null && listPaymentSystem.size() > 0){
                PaymentSystem paymentSystem = new PaymentSystem();
                for(int i=0; i< listPaymentSystem.size(); i++){
                    paymentSystem = (PaymentSystem)listPaymentSystem.get(i);
                    if(paymentSystem.isBankInfoOut()){
                        vBankInfo.add(paymentSystem.getOID()+"");
                    } else if(paymentSystem.isCardInfo()){
                        vCardInfo.add(paymentSystem.getOID()+"");
                    } else if(paymentSystem.isCheckBGInfo()){
                        vCheckBgInfo.add(paymentSystem.getOID()+"");
                    }

                    if(paymentSystem.isDueDateInfo()){
                        vDueDateInfo.add(paymentSystem.getOID() + "");
                    }
                }
            }


%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
                function changePaymentSystem() {
                var infoSts = false;
                var dueDateSts = false

                var paymentId = document.frap.<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_PAYMENT_SYSTEM_ID]%>.value;
                <%
                    for(int i=0; i<vBankInfo.size(); i++){
                %>
                        if(paymentId == '<%=vBankInfo.get(i)%>'){
                            infoSts = true;
                            document.all.paymentSystem.style.display = '';
                            setDisplayInfo('', '', '', '', '', 'none', 'none', 'none', 'none', 'none', 'none')
                        }
                <%
                    }
                %>

                <%
                for(int i=0; i<vCardInfo.size(); i++){
                %>
                        if(paymentId == '<%=vCardInfo.get(i)%>'){
                            infoSts = true;
                            document.all.paymentSystem.style.display = '';
                            setDisplayInfo('', 'none', 'none', 'none', 'none', '', '', '', '', '', 'none')
                        }
                <%
                }
                %>

                <%
                for(int i=0; i<vCheckBgInfo.size(); i++){
                %>
                        if(paymentId == '<%=vCheckBgInfo.get(i)%>'){
                            infoSts = true;
                            document.all.paymentSystem.style.display = '';
                            setDisplayInfo('', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', 'none', '')
                        }
                <%
                }
                %>

                <%
                for(int i=0; i<vDueDateInfo.size(); i++){
                %>
                        if(paymentId == '<%=vDueDateInfo.get(i)%>'){
                            dueDateSts = true;
                            document.all.paymentSystem.style.display = '';
                            document.all.dueDate.style.display = '';
                        }
                <%
                }
                %>

                if(!infoSts){
                    if(!dueDateSts){
                        document.all.paymentSystem.style.display = 'none';
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





            function cmdAsk(oidBillMain)
            {
                document.frmsrccreditinvoice.hidden_bill_main_id.value=oidBillMain;
                document.frmsrccreditinvoice.command.value="<%=Command.ASK%>";
                document.frmsrccreditinvoice.prev_command.value="<%=prevCommand%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
            }



            function cmdEdit(oidBillMain){
                self.close();
                self.opener.document.forms.frmcashier.hidden_bill_main_id.value = oidBillMain;
                self.opener.document.forms.frmcashier.nota_type.value=<%=PstBillMain.OPEN_CREDIT_INVOICE%>;
                if(<%=privApprovalKasirKreditInv%>){
                    self.opener.document.forms.frmcashier.commandDetail.value = <%=Command.ADD%>;
                }
                self.opener.document.forms.frmcashier.submit();
            }


            function cmdListFirst()
            {
                document.frmsrccreditinvoice.command.value="<%=Command.FIRST%>";
                document.frmsrccreditinvoice.prev_command.value="<%=Command.FIRST%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
            }

            function cmdListPrev()
            {
                document.frmsrccreditinvoice.command.value="<%=Command.PREV%>";
                document.frmsrccreditinvoice.prev_command.value="<%=Command.PREV%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
            }

            function cmdListNext()
            {
                document.frmsrccreditinvoice.command.value="<%=Command.NEXT%>";
                document.frmsrccreditinvoice.prev_command.value="<%=Command.NEXT%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
            }

            function cmdListLast()
            {
                document.frmsrccreditinvoice.command.value="<%=Command.LAST%>";
                document.frmsrccreditinvoice.prev_command.value="<%=Command.LAST%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
            }

            function cmdSearch(){
                document.frmsrccreditinvoice.start.value="0";
                document.frmsrccreditinvoice.command.value="<%=Command.LIST%>";
                document.frmsrccreditinvoice.action="srcCreditInvoice.jsp";
                document.frmsrccreditinvoice.submit();
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
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                    <form name="frmsrccreditinvoice" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="cust_id" value="<%=customerId%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
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
                                                        <td height="14" valign="middle" colspan="3" class="comment"></td>
                                                    </tr>

                                                    <%--input payment --%>
                                                    <%

							Vector listPaySysKey = new Vector(1,1);
							Vector listPaySysVal = new Vector(1,1);
							for(int k=0; k<listPaymentSystem.size(); k++) {
								PaymentSystem paymentSystem = (PaymentSystem)listPaymentSystem.get(k);
								listPaySysKey.add(paymentSystem.getPaymentSystem());
								listPaySysVal.add(String.valueOf(paymentSystem.getOID()));
							}

							Vector listCurrTypeKey = new Vector(1,1);
							Vector listCurrTypeVal = new Vector(1,1);
							for(int l=0; l<listCurrencyType.size(); l++) {
								CurrencyType currencyType = (CurrencyType)listCurrencyType.get(l);
								listCurrTypeKey.add(currencyType.getCode());
								listCurrTypeVal.add(String.valueOf(currencyType.getOID()));
							}

							%>
                                                    <tr align="left" valign="top">
                                                        <td>
                                                               <%=ControlDate.drawDate(FrmAccPayable.fieldNames[FrmAccPayable.FRM_PAYMENT_DATE], new Date(), "formElemen", 1,-5) %>
                                                               <input type="hidden" name="<%=FrmAccPayable.fieldNames[FrmAccPayable.FRM_RECEIVE_MATERIAL_ID]%>" value="<%=oidBillMain%>">
                                                            </td>
                                                            <td><%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_PAYMENT_SYSTEM_ID], null, "", listPaySysVal, listPaySysKey, "onchange=\"changePaymentSystem();\"", "formElemen")%></td>
                                                            <td><%=ControlCombo.draw(FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_CURRENCY_TYPE_ID], null, "", listCurrTypeVal, listCurrTypeKey, "onchange=\"changeDailyRate();\"", "formElemen")%></td>
                                                            <td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_RATE]%>"  value="" class="formElemen" size="14"></td>
                                                            <td align="right"><input type="text" name="<%=FrmAccPayableDetail.fieldNames[FrmAccPayableDetail.FRM_AMOUNT]%>"  value="" class="formElemen" size="15" onKeyUp="javascript:calculate()"></td>
                                                            <td align="right"><input type="text" name="amount"  value="" class="formElemen" size="15" disabled></td>

                                                    </tr>
                                                    <tr id="paymentSystem" style="YES; display: none;"  class="listgensell">
                                                    <td colspan="1" class="comment" align="center">
                                                      <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PURCH_PAYMENT_ID]%>" value="<%=paymentInfo.getlPurchPaymentId()%>">
                                                      <input type="hidden" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAYMENT_INFO_ID]%>" value="<%=paymentInfo.getOID()%>">
                                                      <%=textGlobalTitle[SESS_LANGUAGE][16]%>
                                                    </td>
                                                    <td colspan="5"  class="listgensell">
                                                      <table>
                                                            <tr id="bankName" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][0]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_NAME]%>" size="50" value="<%=paymentInfo.getStBankName()%>"></td>
                                                            </tr>
                                                            <tr id="bankAddress" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][1]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_BANK_ADDRESS]%>" size="60" value="<%=paymentInfo.getStBankAddress()%>"></td>
                                                            </tr>
                                                            <tr id="swiftCode" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][2]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_SWIFT_CODE]%>" size="40" value="<%=paymentInfo.getStSwiftCade()%>"></td>
                                                            </tr>
                                                            <tr id="accountName" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][3]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NAME]%>" size="50" value="<%=paymentInfo.getStAccountName()%>"></td>
                                                            </tr>
                                                            <tr id="accountNumber" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][4]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_ACCOUNT_NUMBER]%>" size="40" value="<%=paymentInfo.getStAccountNumber()%>"></td>
                                                            </tr>
                                                            <tr id="nameOnCard" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][5]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_NAME_ON_CARD]%>" size="50" value="<%=paymentInfo.getStNameOnCard()%>"></td>
                                                            </tr>
                                                            <tr id="cardNumber" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][6]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_NUMBER]%>" size="40" value="<%=paymentInfo.getStCardNumber()%>"></td>
                                                            </tr>
                                                            <tr id="cardId" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][7]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CARD_ID]%>" size="40" value="<%=paymentInfo.getStCardId()%>"></td>
                                                            </tr>
                                                            <tr id="expiredDate" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][8]%></td>
                                                              <td>:</td>
                                                              <td><%=ControlDate.drawDateMY(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_EXPIRED_DATE], paymentInfo.getDtExpiredDate() == null ? new Date(): paymentInfo.getDtExpiredDate(),"","formElemen",0,5)%></td>
                                                            </tr>
                                                            <tr id="payAddress" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][9]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_PAY_ADDRESS]%>" size="60" value="<%=paymentInfo.getStPaymentAddress()%>"></td>
                                                            </tr>
                                                            <tr id="bgCheckNumber" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][10]%></td>
                                                              <td>:</td>
                                                              <td><input type="text" name="<%=FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_CHECK_BG_NUMBER]%>" size="40" value="<%=paymentInfo.getStCheckBGNumber()%>"></td>
                                                            </tr>
                                                            <tr id="dueDate" style="YES; display: none;">
                                                              <td><%=textListPaymentInfo[SESS_LANGUAGE][11]%></td>
                                                              <td>:</td>
                                                              <td><%=ControlDate.drawDate(FrmPaymentInfo.fieldNames[FrmPaymentInfo.FRM_FIELD_DUE_DATE], paymentInfo.getDueDate() == null ? new Date(): paymentInfo.getDueDate(),"formElemen", 0, 0)%></td>
                                                            </tr>
                                                      </table>
                                                    </td>
                                                </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE, listCreditInvoice, oidBillMain, start)%> </td>
                                                    </tr>

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <span class="command">
                                                                <%
                                                                            ControlLine ctrlLine = new ControlLine();
                                                                %>
                                                                <%
                                                                            ctrlLine.setLocationImg(approot + "/images");
                                                                            ctrlLine.initDefault();
                                                                %>
                                                                <%=ctrlLine.drawImageListLimit(iCommand, vectSize, start, recordToGet)%> </span>

                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">

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
