<%@ page language="java" %>
<%@ page import = "java.util.*,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
         com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
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
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.posbo.session.admin.SessUserSession,
         java.util.Vector,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.pos.entity.balance.*" %>
<%@ page import = "com.dimata.pos.form.balance.*" %>


<%@ include file = "../main/javainit.jsp" %>
<!-- JSP Block -->
<%!    //final static int CMD_NONE =0;
    final static int CMD_APPROVAL = 1;
%>




        <%!    /* this constant used to list text of listHeader */
            public static final String textMaterialHeader[][] = {
                {"username", "password", "Total"},
                {"username", "password", "Total"},};
            public static final String textListHeader[][] = {
                {"No", "User", "Supervisor", "Open at", "Location", "Currency", "Amount", "Exch.Rate", "Subtotal"},
                {"No", "User", "Supervisor", "Open at", "Location", "Currency", "Amount", "Exch.Rate", "Subtotal"}
            };
            /* this method used to list material master cashier */

            public String drawList(int language, int iCommand, FrmCashCashier frmObject, CashCashier objEntity, Vector objectClass, long unitId, int start) {
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
                    CashCashier cashCashier = (CashCashier) objectClass.get(i);
                    rowx = new Vector();
                    if (unitId == cashCashier.getOID()) {
                        index = i;
                    }

                    start = start + 1;
                    if (index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                        rowx.add("" + start);
                        rowx.add("<input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[FrmCashCashier.FRM_FIELD_APP_USER_ID] + "\" value=\"" + cashCashier.getAppUserId() + "\" class=\"formElemen\">");
                        rowx.add("<input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[FrmCashCashier.FRM_FIELD_APP_USER_ID] + "\" value=\"" + cashCashier.getCashMasterId() + "\" class=\"formElemen\">");
                        Date dt = null;
                        try {
                            dt = cashCashier.getOpenDate();
                            if (dt == null) {
                                dt = new Date();
                            }

                        } catch (Exception e) {
                            dt = new Date();
                        }
                        rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmCashCashier.FRM_FIELD_OPEN_DATE], dt, "formElemen", 24, 1, 0));
                        rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmCashCashier.FRM_FIELD_OPEN_DATE], dt, "formElemen", 24, 1, 0));
                        
                    } else {
                        rowx.add("" + start);
                        rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(cashCashier.getOID()) + "')\"><div align=\"left\">" + cashCashier.getAppUserId() + "</div></a>");
                        rowx.add(cashCashier.getCashMasterId());
                        rowx.add(Formater.formatDate(cashCashier.getOpenDate(), "HH:mm"));
                        rowx.add(Formater.formatDate(cashCashier.getOpenDate(), "HH:mm"));
                    }
                    lstData.add(rowx);
                }

                rowx = new Vector();
                if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
                    Date dt = new Date();
                    rowx.add("" + (start + 1));
                    rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[FrmCashCashier.FRM_FIELD_APP_USER_ID] + "\" value=\"" + objEntity.getAppUserId() + "\" class=\"formElemen\">");
                    rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[FrmCashCashier.FRM_FIELD_CASH_MASTER_ID] + "\" value=\"" + objEntity.getCashMasterId() + "\" class=\"formElemen\">");
                    rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmCashCashier.FRM_FIELD_OPEN_DATE], dt, "formElemen"));
                    rowx.add(ControlDate.drawTime(frmObject.fieldNames[FrmCashCashier.FRM_FIELD_OPEN_DATE], dt, "formElemen"));
                    
                    lstData.add(rowx);
                }

                return ctrlist.draw();
            }

        %>
        <%
                    int iCommand = FRMQueryString.requestCommand(request);
                    int start = FRMQueryString.requestInt(request, "start");
                    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
                    long oidCashCashier = FRMQueryString.requestLong(request, "hidden_cashier_id");
//String cashierTitle = textListTitleHeader[SESS_LANGUAGE][1];

// variable declaration
                    int recordToGet = 10;
                    String msgString = "";
                    int iErrCode = FRMMessage.NONE;
                    String whereClause = "";
                    String orderClause = "";

                    CtrlCashCashier ctrlCashCashier = new CtrlCashCashier(request);
                    ControlLine ctrLine = new ControlLine();
                    Vector listCashCashier = new Vector(1, 1);

                    iErrCode = ctrlCashCashier.action(iCommand, oidCashCashier);
                    FrmCashCashier frmCashCashier = ctrlCashCashier.getForm();


                    int vectSize = PstCashCashier.getCount(whereClause);
                    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                        start = ctrlCashCashier.actionList(iCommand, start, vectSize, recordToGet);
                    }

                    CashCashier cashCashier = ctrlCashCashier.getCashCashier();
                    msgString = ctrlCashCashier.getMessage();

                    listCashCashier = PstCashCashier.list(start, recordToGet, whereClause, orderClause);

                    if (listCashCashier.size() < 1 && start > 0) {
                        if (vectSize - recordToGet > recordToGet) {
                            start = start - recordToGet;
                        } else {
                            start = 0;
                            iCommand = Command.FIRST;
                            prevCommand = Command.FIRST;
                        }
                        listCashCashier = PstCashCashier.list(start, recordToGet, whereClause, orderClause);
                    }
        %>
        <%
                    //int iCommand = Integer.parseInt((request.getParameter("command") == null) ? "0" : request.getParameter("command"));
                    int doapproval = 0;  // SessUserSession.DO_LOGIN_OK ;
                    int appLanguage = 0;
                    int approvalStatus = 0;
                    if (iCommand == CMD_APPROVAL) {
                        String loginID = FRMQueryString.requestString(request, "login_id");
                        String passwd = FRMQueryString.requestString(request, "pass_wd");

                        AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
                        if (user.getUserGroupNew() == 1) {
                            approvalStatus = 1;
                        }
                    }
        %>
        <%
                    if ((iCommand == CMD_APPROVAL) && (approvalStatus == 1)) {
                        //RemotePrintMan.reloadPrintersOnAllHost();
                        response.sendRedirect("cashier_lyt.jsp");
                    }
        %>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata ProChain - Open CashCashier </title>
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
        <script language="JavaScript">
            function cmdApproval(){
                document.frmApproval.action = "open_shift.jsp";
                document.frmApproval.submit();
            }

            function fnTrapUserName(){
                if(event.keyCode == 13){
                    document.frmApproval.pass_wd.focus();
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

            //<!--
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
                //-->
        </script>
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
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Cashier - Open CashCashier <!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmApprofal" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="command" value="<%=CMD_APPROVAL%>">
                                    <table width="100%" cellspacing="1" cellpadding="1">
                                        <tr>
                                            <td>List of Opened CashCashier </td>
                                        </tr>
                                        <%
							try{
							%>
                                        <tr>
                                            <td><%=drawList(SESS_LANGUAGE, iCommand, frmCashCashier, cashCashier, listCashCashier, oidCashCashier, start)%></td>
                                        </tr>
                                        <%
						  }catch(Exception exc){
						  }%>
                                        <tr>
                                            <td>&nbsp;</td>
                                        </tr>

                                        <tr>
                                            <td><table width="800" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td width="6%" class="listgentitle">No.<%=(Formater.formatDate(new Date(), "dd-MM-yyyy"))%></td>
                                                        <td width="6%" class="listgentitle">Currency</td>
                                                        <td width="25%" class="listgentitle">Amount</td>
                                                        <td width="63%" class="listgentitle">Exch.Rate</td>
                                                        <td width="63%" class="listgentitle"> Subtotal </td>
                                                    </tr>
                                                    <tr>
                                                        <td>1</td>
                                                        <td><label>
                                                                <select name="select">
                                                                    <option>Rp.</option>
                                                                    <option>USD</option>
                                                                </select>
                                                            </label></td>
                                                        <td><label>
                                                                <input type="text" name="textfield">
                                                            </label></td>
                                                        <td><input type="text" name="textfield2"></td>
                                                        <td><input type="text" name="textfield3"></td>
                                                    </tr>
                                                    <tr>
                                                        <td>2</td>
                                                        <td><label>
                                                                <select name="select">
                                                                    <option>Rp.</option>
                                                                    <option>USD</option>
                                                                </select>
                                                            </label></td>
                                                        <td><label>
                                                                <input type="text" name="textfield">
                                                            </label></td>
                                                        <td><input type="text" name="textfield2"></td>
                                                        <td><input type="text" name="textfield3"></td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td><div align="right"><%=textMaterialHeader[SESS_LANGUAGE][2]%></div></td>
                                                        <td><input type="text" name="textfield32"></td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td>&nbsp;</td>
                                                        <td><div align="right"><%=textMaterialHeader[SESS_LANGUAGE][2]%></div></td>
                                                        <td><input type="text" name="textfield32"></td>
                                                    </tr>
                                                </table>
                                                <fieldset>
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
                                                                    <td ><a href="javascript:cmdApproval()" onKeyDown="javascript:keybrdPress(this)"><span id="aLogin" >SUBMIT</span></a></td>
                                                                    <td >&nbsp;</td>
                                                                    <td ></td>
                                                                </tr>
                                                            </table>
                                                        </label>
                                                    </p>
                                                </fieldset>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <%if ((iCommand == CMD_APPROVAL) && (approvalStatus != 1)) {%>
                                                <font class="errfont" color="#FF0000"><%if (appLanguage == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {%>nama atau password salah<%} else {%>
                                                    username or password wrong, try again...
                                                    <%}%></font>
                                                    <%}%>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <p>&nbsp;</p>
                                <form name="form1" method="post" action="">
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
