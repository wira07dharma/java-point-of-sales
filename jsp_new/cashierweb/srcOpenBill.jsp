<%--
    Document   : srcOpenBill.jsp
    Created on : 02 Jul 13, 11:44:26
    Author     : Wiweka
--%>
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
          {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Person Name","Sales Person"},
        {"Guest Type", "Date", "all date", "Invoice Number","Customer Name", "Person Name","Sales Person"}
    };
    public static final String textListHeader[][] = {
        {"Code", "Date", "Customer Name", "Person Name", "Total","Sales Person","Status Document","Currency"},
        {"Code", "Date", "Customer Name", "Person Name", "Total","Sales Person","Status Document","Currency"}
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
            ctrlist.dataFormat("No", "3%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][5], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][1], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][2], "20%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][3], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][7], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][4], "20%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][6], "10%", "center", "left");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
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

                Vector rowx = new Vector();

                if (billMainId == billMain.getOID()) {
                    index = i;
                }              

                String salesName="";
                try {
//                    salesName = PstSales.getNameSales(billMain.getSalesCode());
                AppUser ap = new AppUser();
                ap = PstAppUser.fetch(billMain.getAppUserId());
                salesName = ap.getFullName();
                } catch (Exception e) {
                    salesName="";
                }

                rowx.add("<div align=\"center\">" + (i + start + 1) + "</div>");
                if(salesName.equals("")){
                     rowx.add("<div align=\"left\">From Invoicing</div>");
                }else{
                     rowx.add("<div align=\"left\">" + salesName + "</div>");
                }
                rowx.add("<div align=\"center\">" + Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") + "</div>");
                rowx.add("<div align=\"right\">" + memberReg.getCompName() + "</div>");
                rowx.add("<div align=\"right\">" + memberReg.getPersonName() + "</div>");
                
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
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            Date billDate = FRMQueryString.requestDate(request, "biil_date");
            String invoiceNumber = FRMQueryString.requestString(request, "inv_number");
            long customerId = FRMQueryString.requestLong(request, "cust_id");
            String custName = FRMQueryString.requestString(request, "cust_name");
            String personName = FRMQueryString.requestString(request, "person_name");

            int recordToGet = 20;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;

            String whereClause = "";
            String orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

            //jika mendapatkan hak akses kasir
            if(privApprovalKasirPay){
                whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0'"
                             + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0'"
                             + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "='1'"
                             + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " != '0'";
            }else {//hak akses invoicing
                whereClause =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '0'";
            }

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

            // get record to display
            orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
            int vectSize = PstBillMain.getCount(start, bMain, recordToGet, whereClause, orderClause);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }

            listOpenBill = PstBillMain.listSrc(start, bMain, recordToGet, whereClause, orderClause);

            // handle condition if size of record to display=0 and start>0 after delete
            if (listOpenBill.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listOpenBill = PstBillMain.listSrc(start, bMain, recordToGet, whereClause, orderClause);
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
                document.frmopenbill.prev_command.value="<%=prevCommand%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }



            function cmdEdit(oidBillMain,SalesName,StatusInvoice){
                //alert(SalesName);
                if(StatusInvoice==2){
                    alert("Invoice Sudah Done, Tidak bisa di Proses Lagi")
                }else{
                     self.close();
                    self.opener.document.forms.frmcashier.command.value = <%=Command.ADD%>;
                    self.opener.document.forms.frmcashier.hidden_bill_main_id.value = oidBillMain;
                    self.opener.document.forms.frmcashier.nota_type.value=<%=PstBillMain.OPEN_BILL%>;
                    self.opener.document.forms.frmcashier.SalesName.value = SalesName;

                    self.opener.document.forms.frmcashier.submit();
                }
            }


            function cmdListFirst()
            {
                document.frmopenbill.command.value="<%=Command.FIRST%>";
                document.frmopenbill.prev_command.value="<%=Command.FIRST%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListPrev()
            {
                document.frmopenbill.command.value="<%=Command.PREV%>";
                document.frmopenbill.prev_command.value="<%=Command.PREV%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListNext()
            {
                document.frmopenbill.command.value="<%=Command.NEXT%>";
                document.frmopenbill.prev_command.value="<%=Command.NEXT%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }

            function cmdListLast()
            {
                document.frmopenbill.command.value="<%=Command.LAST%>";
                document.frmopenbill.prev_command.value="<%=Command.LAST%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }
            function cmdSearch(){
                document.frmopenbill.start.value="0";
                document.frmopenbill.command.value="<%=Command.LIST%>";
                document.frmopenbill.action="srcOpenBill.jsp";
                document.frmopenbill.submit();
            }

            function modifKey(frmObj, event,value){
               //alert(event.keyCode);
                if(event.keyCode == 13) { //enter
                  cmdSearch();
                }else if (event.keyCode==112){ //F1=Open Sales Order
                        strvalue  = "srcOpenBill.jsp";
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
        </style>
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab_cashierweb.css" type="text/css">
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

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onkeydown="modifKey(this, event, this.value)">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td height="25" ID="TOPTITLE"></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <!-- #EndEditable --> </td>
            </tr>
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
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="8" valign="middle" colspan="3">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                        <td height="22" width="87%"> :
                                                            <%

                                                                        long selectedCustomerId = FRMQueryString.requestLong(request, "custId");
                                                                        Vector listCustomer = PstMemberGroup.list(0, 0, "", "");
                                                                        Vector vectCustomerVal = new Vector(1, 1);
                                                                        Vector vectCustomerKey = new Vector(1, 1);
                                                                        for (int i = 0; i <= listCustomer.size(); i++) {
                                                                            if (i == 0) {
                                                                                vectCustomerKey.add("-none-");
                                                                                vectCustomerVal.add("");
                                                                            } else {
                                                                                MemberGroup memberGroup = (MemberGroup) listCustomer.get(i - 1);
                                                                                vectCustomerKey.add(memberGroup.getName());
                                                                                vectCustomerVal.add("" + memberGroup.getOID());
                                                                            }
                                                                        }
                                                                        String select_Customer = "0";

                                                                        if (selectedCustomerId != 0) {
                                                                            select_Customer = "" + selectedCustomerId;
                                                                        }




                                                            %>
                                                            <%=ControlCombo.draw("cust_id", "formElemen", null, select_Customer, vectCustomerVal, vectCustomerKey, "")%>
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <%--
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="inv_number" size="30" value="<%=invoiceNumber%>" class="formElemen">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    --%>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="cust_name" size="30" value="<%=custName%>" class="formElemen" onkeydown="modifKey(this, event, this.value)">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="person_name" size="30" value="<%=personName%>" class="formElemen" onkeydown="modifKey(this, event, this.value)">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                        <td height="22" width="87%"> :
                                                           <input type="radio" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_STATUS_DATE]%>"
                                                                   <%if (billMain.getStatusDate() == 0) {%>checked<%}%> value="0"> <%=textMaterialHeader[SESS_LANGUAGE][2]%>
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"></td>
                                                        <td height="22" width="87%">  &nbsp;
                                                          <input type="radio" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_STATUS_DATE]%>"
                                                                   <%if (billMain.getStatusDate() == 1) {%>checked<%}%> value="1">
                                                           from <%=ControlDate.drawDateWithStyle(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_FROM], billMain.getDatefrom()==null? new Date() : billMain.getDatefrom() , 1, -5,"formElemen","")%>
                                                            &nbsp;to&nbsp; <%=ControlDate.drawDateWithStyle(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_TO], billMain.getDateto()==null? new Date() : billMain.getDateto(), 1, -5, "formElemen","")%>
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%">&nbsp;</td>
                                                        <td height="22" width="87%">
                                                            <input type="button" name="Button" value="Search" onClick="javascript:cmdSearch()" class="formElemen">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%=drawList(SESS_LANGUAGE, listOpenBill, oidBillMain, start)%> </td>
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
        </table>
    </body>
    <!-- #EndTemplate --></html>
