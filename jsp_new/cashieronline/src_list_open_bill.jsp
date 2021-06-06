<%-- 
    Document   : src_list_open_bill
    Created on : Jan 27, 2014, 9:35:10 AM
    Author     : dimata005
--%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.pos.form.search.FrmSrcInvoice"%>
<%@page import="com.dimata.pos.session.billing.SessSales"%>
<%@page import="com.dimata.pos.entity.search.SrcInvoice"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
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
        int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT);

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalKasirInv = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirInvoice, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirPay = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));
%>
<%
            boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
          {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Sales Name","Sales Person"},
        {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Sales Name","Sales Person"}
    };
    public static final String textListHeader[][] = {
        {"Code", "Date", "Customer Name", "Sales Name", "Total","Sales Person","Status invoicing","Currency","Action"},
        {"Code", "Date", "Customer Name", "Sales Name", "Total","Sales Person","Status invoicing","Currency","Action"}
    };

    /* this method used to list material department */
    public String drawList(int language, Vector objectClass, long billMainId, int start) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setBorder(1);
            ctrlist.dataFormat("No", "3%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][5], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][1], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][2], "20%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][3], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][7], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][4], "20%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][6], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][8], "10%", "center", "left");
            
            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdView('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            int index = -1;

            for (int i = 0; i < objectClass.size(); i++) {
                //BillMain billMain = (BillMain)objectClass.get(i);
                Vector vt = (Vector) objectClass.get(i);
                BillMain billMain = (BillMain) vt.get(0);
                MemberReg memberReg = (MemberReg) vt.get(1);
                CurrencyType currencyType = (CurrencyType) vt.get(2);
                AppUser appUser = (AppUser) vt.get(3);
                Sales saler = (Sales) vt.get(4);

                Vector rowx = new Vector();

                if (billMainId == billMain.getOID()) {
                    index = i;
                }

                String salesName="";
                try {
                    AppUser ap = new AppUser();
                    ap = PstAppUser.fetch(billMain.getAppUserId());
                    salesName = ap.getFullName();
//                    salesName = PstSales.getNameSales(billMain.getSalesCode());
                } catch (Exception e) {
                    salesName="";
                }

                rowx.add("<div align=\"center\">" + (i + start + 1) + "</div>");
                if(salesName.equals("")){
                     rowx.add("<div align=\"left\">From Outlet</div>");
                }else{
                     rowx.add("<div align=\"left\">" + salesName + "</div>");
                }
                rowx.add("<div align=\"center\">" + Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") + "</div>");
                rowx.add("<div align=\"right\">" + memberReg.getCompName() + "</div>");
                rowx.add("<div align=\"right\">" + saler.getName()+ "</div>");

                if(billMain.getRate()==1){
                    rowx.add("<div align=\"right\">Rp.</div>");
                }else{
                    rowx.add("<div align=\"right\">USD</div>");
                }

                double bruto = 0;
                bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(bruto) + "</div>");
                if(billMain.getStatusInv()==1){
                    rowx.add("<div align=\"center\">On Prosess</div>");
                }else if (billMain.getStatusInv()==2){
                    rowx.add("<div align=\"center\">Done</div>");
                }else{
                    rowx.add("<div align=\"center\">Draft</div>");
                }
                rowx.add("<div align=\"center\"><a href=\"javascript:cmdProses('"+String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv()+"')\">Proses</a></div>");
                
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv());
            }
            result = ctrlist.drawTableNavigation();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Data is empty...</div>";
            
        }
        return result;
    }
%>
<%
            /**
             * get approval status for create document
             */
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
            I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
            I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
            int systemName = I_DocType.SYSTEM_MATERIAL;
            int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>
<%
    Date today = new Date();  
    String klik = FRMQueryString.requestString(request, "klik");

            String poCode = "PO";
            String poTitle = "Sales Order";
            String poItemTitle = poTitle + " Item";
            String poTitleBlank = "";

            long oidBillMain = FRMQueryString.requestLong(request, "oidbillmaintmp");
            String salesCode = FRMQueryString.requestString(request, "FRM_FIELD_SALES_CODE");

            int iErrCode = FRMMessage.ERR_NONE;
            String msgStr = "";
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int recordToGet = 20;
            int vectSize = 0;
            String whereClause = "";
            

    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    Date billDate = FRMQueryString.requestDate(request, "biil_date");
    String invoiceNumber = FRMQueryString.requestString(request, "inv_number");
    long customerId = FRMQueryString.requestLong(request, "cust_id");
    String custName = FRMQueryString.requestString(request, "cust_name");
    String personName = FRMQueryString.requestString(request, "person_name");
 
    
    
     
    CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
    ControlLine ctrLine = new ControlLine();
    Vector listOpenBill = new Vector(1, 1);

    iErrCode = ctrlBillMain.action(iCommand, oidBillMain, 0);
    FrmBillMain frmBillMain = ctrlBillMain.getForm();

    int statusDate = FRMQueryString.requestInt(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_STATUS_DATE]);
    Date datefrom = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_FROM]);
    Date dateto = FRMQueryString.requestDate(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_DATE_TO]);

    // count list All MatDepartment

    BillMain billMain = ctrlBillMain.getBillMain();


   
            SrcInvoice srcInvoice = new SrcInvoice();

            SessSales sessSales = new SessSales();
            

    
            FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice(request, srcInvoice);///
            frmSrcInvoice.requestEntityObject(srcInvoice);
                
            if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                try {
                    srcInvoice = (SrcInvoice) session.getValue(SessSales.SESS_SRC_ORDERMATERIAL);
                    if (srcInvoice == null) {
                        srcInvoice = new SrcInvoice();
                    }
                } catch (Exception e) {
                    System.out.println(" Session null : " + e);
                    srcInvoice = new SrcInvoice();
                }
            } else {
                try {
                    srcInvoice = (SrcInvoice) session.getValue(SessSales.SESS_SRC_ORDERMATERIAL);
                } catch (Exception e) {
                    srcInvoice = new SrcInvoice();
                }

                if (srcInvoice == null) {
                    srcInvoice = new SrcInvoice();
                }

                try {
                    session.removeValue(SessSales.SESS_SRC_ORDERMATERIAL);
                } catch (Exception e) {
                }

                frmSrcInvoice.requestEntityObject(srcInvoice);
                Vector vectStCurr = new Vector(1, 1);

                String[] strStatusCurr = request.getParameterValues(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CURRENCY_ID]);
                if (strStatusCurr != null && strStatusCurr.length > 0) {
                    for (int i = 0; i < strStatusCurr.length; i++) {
                        try {
                            vectStCurr.add(strStatusCurr[i]);
                        } catch (Exception exc) {
                            System.out.println("err");
                        }
                    }
                }
                srcInvoice.setPrmstatusCurr(vectStCurr);
                session.putValue(SessSales.SESS_SRC_ORDERMATERIAL, srcInvoice);
            }

            vectSize = sessSales.getCount(srcInvoice, docType);
            if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }

            //Vector records = sessSales.searchSalesOrder(srcInvoice, docType, start, recordToGet);
            Vector records = sessSales.getList(srcInvoice, docType);
            // mchen
            if (srcInvoice.getInvoiceDate()==null){
                srcInvoice.setInvoiceDate(today);
                srcInvoice.setInvoiceDateTo(today);
            }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">

            function cmdAsk(oidBillMain)
            {
                document.frmopenbill.hidden_bill_main_id.value=oidBillMain;
                document.frmopenbill.command.value="<%=Command.ASK%>";
                document.frmopenbill.prev_command.value="";
                document.frmopenbill.action="src_list_open_bill.jsp";
                document.frmopenbill.submit();
            }

            function cmdView(oidBillMain,SalesName,StatusInvoice){
                    strvalue  = "outlet_cashier_admin.jsp?FRM_FIELD_TYPE_SALES_ORDER=0&oidbillmaintmp="+oidBillMain+"&commandDetail=1&command=3&notasalestype=0";
                    window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            
            function cmdProses(oidBillMain,SalesName,StatusInvoice){
                if(StatusInvoice==2){
                    alert("Invoice Sudah Done, Tidak bisa di Proses Lagi");
                }else{
                    document.frmopenbill.command.value="<%=Command.ADD%>";
                    document.frmopenbill.hidden_bill_main_id.value=oidBillMain;
                    document.frmopenbill.nota_type.value="<%=PstBillMain.OPEN_BILL%>";
                    document.frmopenbill.SalesName.value=SalesName;
                    document.frmopenbill.ApprovalKasirPay.value="1";
                    document.frmopenbill.typeCashier.value="1";
                    document.frmopenbill.action="cashier_lyt.jsp";
                    document.frmopenbill.submit();
                    
                }
            }


            function cmdListFirst()
            {
                document.frmopenbill.command.value="<%=Command.FIRST%>";
                document.frmopenbill.prev_command.value="<%=Command.FIRST%>";
                document.frmopenbill.action="src_list_open_bill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListPrev()
            {
                document.frmopenbill.command.value="<%=Command.PREV%>";
                document.frmopenbill.prev_command.value="<%=Command.PREV%>";
                document.frmopenbill.action="src_list_open_bill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListNext()
            {
                document.frmopenbill.command.value="<%=Command.NEXT%>";
                document.frmopenbill.prev_command.value="<%=Command.NEXT%>";
                document.frmopenbill.action="src_list_open_bill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListLast()
            {
                document.frmopenbill.command.value="<%=Command.LAST%>";
                document.frmopenbill.prev_command.value="<%=Command.LAST%>";
                document.frmopenbill.action="src_list_open_bill.jsp";
                document.frmopenbill.submit();
            }
            function cmdSearch(){
                //mchen
                if (document.getElementById("draff").checked == true)
                    document.frmopenbill.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DRAFF]%>.value="1";
                if (document.getElementById("onprocess").checked == true)
                    document.frmopenbill.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_ONPROCESS]%>.value="1";
                if (document.getElementById("done").checked == true)
                    document.frmopenbill.<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DONE]%>.value="1";
                document.frmopenbill.start.value="0";
                document.frmopenbill.command.value="<%=Command.LIST%>";
                document.frmopenbill.action="src_list_open_bill.jsp";
               
                document.frmopenbill.submit();
            }
            
            function cmdTambahInvoice(){
                var base_url = window.location.origin;
                window.open("open_shift.jsp?typeListCashier=0&typeCashier=1","_self");
              
            }

            function modifKey(frmObj, event,value){
                if(event.keyCode == 13) { //enter
                  cmdSearch();
                }else if (event.keyCode==112){ //F1=Open Sales Order
                        strvalue  = "src_list_open_bill.jsp";
                        window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                }else if (event.keyCode==40){
                    switch(frmObj.name) {
                        case 'cust_name':
                                document.frmopenbill.person_name .focus();
                            break;
                        case 'person_name':
                                activeTable();
                            break;
                                // document.frmopenbill.person_name .focus();
                        default:
                            break;
                    }
                }else if (event.keyCode==38){
                   switch(frmObj.name) {
                        case 'person_name':
                                document.frmopenbill.cust_name .focus();
                            break;
                        default:
                            break;
                    }
                }else if (event.keyCode==27 ){
                    switch(frmObj.name) {
                        case 'cust_name':
                               self.close();
                            break;
                        case 'person_name':
                                document.frmopenbill.cust_name .focus();
                            break;
                        default:
                            document.frmopenbill.cust_name.focus();
                            break;
                    }
                }
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
        <script src="../styles/jquery.js"></script>
        <script src="../styles/jquery.table_navigation.js"></script>
        <script type="text/javascript">
                    function activeTable(){
                         jQuery.tableNavigation();
                    }
        </script>
        <style type="text/css">
                    table {border-collapse: collapse;}
                    th, td, tr {margin: 0; padding: 0.25em 0.5em; font-size:16px;}
                    /* This "tr.selected" style is the only rule you need for yourself. It highlights the selected table row. */
                    tr.selected {background-color: #87CEEB; color: white;}
                    /* Not necessary but makes the links in selected rows white to... */
                    tr.selected a {color: white;}
                    a{font-size:16px;}
                    #btn {padding: 3px 5px; color: #fff; background-color: #333; border:1px solid #000; border-radius: 3px;}
                    #btn:hover {background-color:#CC3300; border: 1px solid #800000; }
        </style>
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab_cashierweb.css" type="text/css">
        <!-- #EndEditable -->
        <%if(menuUsed == MENU_ICON){%>
            <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
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

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onkeydown="modifKey(this, event, this.value)">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
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
              <%}else{%>
               <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                  <%@include file="../styletemplate/template_header.jsp" %>
                </td>
              </tr>
              <%}%>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">

                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmopenbill" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                                    <input type="hidden" name="SalesName" value="">
                                    <input type="hidden" name="nota_type" value="">
                                    <input type="hidden" name="typeCashier" value="">
                                    <input type="hidden" name="ApprovalKasirPay" value="">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="8" valign="middle" colspan="3">
                                                        </td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_CUSTOMER_NAME]%>" size="30" value="<%=custName%>" class="formElemen" onkeydown="modifKey(this, event, this.value)">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_SALES_PERSON]%>" size="30" value="<%=personName%>" class="formElemen" onkeydown="modifKey(this, event, this.value)">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%">Status invoicing</td>
                                                        <td height="22" width="87%"> :
                                                            <%
                                                                if(srcInvoice.getStatusDraff() != 0){
                                                            %>
                                                            <input type="checkbox" id="draff" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DRAFF]%>" value="0" checked="checked" class="formElemen" />Draff&nbsp;
                                                            <%
                                                                } else {
                                                            %>
                                                                <input type="checkbox" id="draff" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DRAFF]%>" value="0" class="formElemen" />Draff&nbsp;
                                                            <%
                                                                }
                                                            %>
                                                            <%
                                                                if(srcInvoice.getStatusOnProcess()!= 0){
                                                            %>
                                                            <input type="checkbox" id="onprocess" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_ONPROCESS]%>" value="0" checked="checked" class="formElemen" />On process&nbsp;
                                                            <%
                                                                } else {
                                                            %>
                                                                <input type="checkbox" id="onprocess" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_ONPROCESS]%>" value="0" class="formElemen" />On process&nbsp;
                                                            <%
                                                                }
                                                            %>
                                                            <%
                                                                if(srcInvoice.getStatusDone()!= 0){
                                                            %>
                                                                <input type="checkbox" id="done" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DONE]%>" value="0" checked="checked" class="formElemen" />Done&nbsp;
                                                            <%
                                                                } else {
                                                            %>
                                                                <input type="checkbox" id="done" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_DONE]%>" value="0" class="formElemen" />Done&nbsp;
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                        <td height="22" width="87%"> :                                                            
                                                            <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" 
                                                                   <%if (srcInvoice.getStatusDate() == 0) {%>checked<%}%> value="0" >
                                                            <%=textMaterialHeader[SESS_LANGUAGE][2]%>
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"></td>
                                                        <td height="22" width="87%">  &nbsp;
                                                           
                                                            
                                                            <input type="radio" name="<%=frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_STATUS_DATE]%>" 
                                                                   <%if (srcInvoice.getStatusDate() == 1) {%>checked<%}%> value="1">
                                        From<%=ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE], srcInvoice.getInvoiceDate(), "formElemen", 1, -5)%>  &nbsp;To&nbsp; 
                                        <%=ControlDate.drawDate(frmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_INVOICE_DATE_TO], srcInvoice.getInvoiceDateTo(), "formElemen", 1, -5)%>
                                                            
                                                           
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%">&nbsp;</td>
                                                        <td height="22" width="87%">
                                                            <input type="button" id="btn" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">&nbsp;
                                                            <input type="button" id="btn" name="Button" value="Tambah Invoicing" onClick="javascript:cmdTambahInvoice()" class="formElemen">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> 
                                                             
                                                            <%
                                                                if(iCommand == Command.LIST){
                                                            %>
                                                                <%=drawList(SESS_LANGUAGE, records, oidBillMain, start)%>
                                                            <%
                                                                }
                                                            %>
                                                        </td>
                                                    </tr>

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <span class="command">
                                                                <%
                                                                if(iCommand == Command.LIST){
                                                                    ControlLine ctrlLine = new ControlLine();
                                                                %>
                                                                <%
                                                                    ctrlLine.setLocationImg(approot + "/images");
                                                                    ctrlLine.initDefault();
                                                                %>
                                                                <%=ctrlLine.drawImageListLimit(iCommand, vectSize, start, recordToGet)%>
                                                                <%
                                                                }
                                                                %>
                                                            </span>

                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>

                                    </table>
                                </form>
                                 <script language="JavaScript">
                                    document.frmopenbill.cust_name.focus();
                                </script>
                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td height="10"> <!-- #BeginEditable "footer" -->
                    <%if(menuUsed == MENU_ICON){%>
                        <%@include file="../styletemplate/footer.jsp" %>
                    <%}else{%>
                        <%@ include file = "../main/footer.jsp" %>
                    <%}%>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
