<%-- 
    Document   : srcInvoice
    Created on : 31 Jul 13, 10:16:51
    Author     : Wiweka
--%>
<%@page import="com.dimata.pos.entity.payment.CreditPaymentMain"%>
<%@page import="com.dimata.pos.entity.payment.PstCreditPaymentMain"%>
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
         com.dimata.common.entity.payment.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../main/javainit.jsp" %>
<!--% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATEGORY); %>
<!--%@ include file = "../main/checkuser.jsp" %>
<%
            boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!    /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Guest Type", "Date", "all date", "Invoice Number", "Customer Name", "Person Name"},
        {"Guest Type", "Date", "all date", "Invoice Number", "Customer Name", "Person Name"}
    };
    public static final String textListHeader[][] = {
        {"Code", "Date", "Customer Name", "Person Name", "Total"},
        {"Code", "Date", "Customer Name", "Person Name", "Total"}
    };

    /* this method used to list material department */
    public String drawList(int language, Vector objectClass, long billMainId, int start) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("60%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.dataFormat("No", "3%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][0], "12%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][1], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][2], "20%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][3], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][4], "10%", "center", "left");

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

                rowx.add("<div align=\"center\">" + (i + start + 1) + "</div>");
                rowx.add(billMain.getInvoiceNumber());
                rowx.add("<div align=\"center\">" + Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") + "</div>");
                rowx.add("<div align=\"right\">" + memberReg.getCompName() + "</div>");
                rowx.add("<div align=\"right\">" + memberReg.getPersonName() + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billMain.getAmount()) + "</div>");

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
            String whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + " = '0' AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '0' OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + " = '1')";
            String orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            ControlLine ctrLine = new ControlLine();
            Vector listOpenBill = new Vector(1, 1);

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
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
            }



            function cmdEdit(oidBillMain){
                self.close();
                self.opener.document.forms.frmcashier.hidden_bill_main_id.value = oidBillMain;
                self.opener.document.forms.frmcashier.nota_type.value=<%=PstBillMain.OPEN_ALL_INVOICE%>;
                self.opener.document.forms.frmcashier.submit();

            }


            function cmdListFirst()
            {
                document.frmopenbill.command.value="<%=Command.FIRST%>";
                document.frmopenbill.prev_command.value="<%=Command.FIRST%>";
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
            }

            function cmdListPrev()
            {
                document.frmopenbill.command.value="<%=Command.PREV%>";
                document.frmopenbill.prev_command.value="<%=Command.PREV%>";
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
            }

            function cmdListNext()
            {
                document.frmopenbill.command.value="<%=Command.NEXT%>";
                document.frmopenbill.prev_command.value="<%=Command.NEXT%>";
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
            }

            function cmdListLast()
            {
                document.frmopenbill.command.value="<%=Command.LAST%>";
                document.frmopenbill.prev_command.value="<%=Command.LAST%>";
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
            }
            function cmdSearch(){
                document.frmopenbill.start.value="0";
                document.frmopenbill.command.value="<%=Command.LIST%>";
                document.frmopenbill.action="srcInvoice.jsp";
                document.frmopenbill.submit();
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
                                                            <!--input type="text" name="cust_id" size="15" value="<!--%=customerId%>" class="formElemen"-->
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="inv_number" size="30" value="<%=invoiceNumber%>" class="formElemen">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="cust_name" size="30" value="<%=custName%>" class="formElemen">
                                                        </td>
                                                        <td height="22"></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="22" width="13%"><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                        <td height="22" width="87%"> :
                                                            <input type="text" name="person_name" size="30" value="<%=personName%>" class="formElemen">
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
                                                            from <%=ControlDate.drawDateWithStyle(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_FROM], billMain.getDatefrom() == null ? new Date() : billMain.getDatefrom(), 1, -5, "formElemen", "")%>
                                                            &nbsp;to&nbsp; <%=ControlDate.drawDateWithStyle(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_TO], billMain.getDateto() == null ? new Date() : billMain.getDateto(), 1, -5, "formElemen", "")%>
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
                                <!-- #EndEditable --></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>

