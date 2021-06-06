
<%@page import= "com.dimata.posbo.form.arap.FrmAccPayable"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*, java.sql.Time, 
         com.dimata.posbo.form.admin.service.FrmBackUpService" %>
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
<%@ page import = "com.dimata.pos.form.billing.*" %>
<%@ page import = "com.dimata.pos.entity.billing.*" %>
<%@ include file = "../main/javainit.jsp" %>
<%@ include file = "CheckUserCashier.jsp"%>

<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_SALES_INVOICE);%>

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

            lstData.add(rowx);
        }
        return ctrlist.draw();
    }
%>
<%
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidCashCashier = FRMQueryString.requestLong(request, "hidden_cash_cashier_id");

            /*variable declaration*/
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "";

            CtrlCashCashier ctrlCashCashier = new CtrlCashCashier(request);
            ControlLine ctrLine = new ControlLine();
            Vector listCashCashier = new Vector(1, 1);


            /* end switch*/
            FrmCashCashier frmCashCashier = ctrlCashCashier.getForm();
            //iErrCode = ctrlCashCashier.action(iCommand,oidCashCashier);
            double amountRp = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]);
            double rupiah = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]);
            double subtotal1 = FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]);
            //double finalTotal=FRMQueryString.requestDouble(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_BALANCE_VALUE]);

            /*count list All CashCashier*/
            int vectSize = PstCashCashier.getCount(whereClause);

            /*switch list CashCashier*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlCashCashier.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/
            OpeningCashCashier openingCashCashier = ctrlCashCashier.getOpeningCashCashier();
            CashCashier cashCashier = ctrlCashCashier.getCashCashier();
            msgString = ctrlCashCashier.getMessage();

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

            //Mencari Shift Id
            long shiftId = 0;
            long shiftIdTmp = 0;
            long shiftIdTmpElse = 0;
            String namaShift = "";
            String time = "";
            Time timeShift;

            String timeNow = Formater.formatDate(new Date(), "HH:mm:ss");
            Time timeshiftNow = Time.valueOf(timeNow);

            Vector listShift = PstShift.list(0, 0, "", PstShift.fieldNames[PstShift.FLD_END_TIME] + " DESC ");
            for (int i = 0; i < listShift.size(); i++) {
                Shift shift = (Shift) listShift.get(i);
                time = Formater.formatDate(shift.getStartTime(), "HH:mm:ss");
                timeShift = Time.valueOf(time);
                if (timeshiftNow.after(timeShift) || timeshiftNow.equals(timeShift)) {
                    shiftIdTmp = shift.getOID();
                    namaShift = shift.getName();
                    i = listShift.size();
                } else {
                    shiftIdTmpElse = shift.getOID();
                    namaShift = shift.getName();
                }
            }
            if (shiftIdTmp != 0) {
                shiftId = shiftIdTmp;
            } else {
                shiftId = shiftIdTmpElse;
            }

            int iCmd = Integer.parseInt((request.getParameter("cmd") == null) ? "0" : request.getParameter("cmd"));
            int doapproval = 0;
            int appLanguage = 0;
            int approvalStatus = 0;
            long supervisorId = 0;

            if (iCommand == Command.SAVE) {
                 String loginID = FRMQueryString.requestString(request, "login_id");
                String passwd = FRMQueryString.requestString(request, "pass_wd");

                Vector listSupervisor = PstAppUser.listFullObj(0, 0, PstAppUser.fieldNames[PstAppUser.FLD_USER_GROUP_NEW] + " ='1'", "");
                for (int i = 0; i < listSupervisor.size(); i++) {
                    AppUser user = (AppUser) listSupervisor.get(0);
                    loginID = user.getLoginId();
                    passwd = user.getPassword();
                }               

                AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
                if (user == null) {
                    approvalStatus = 2;
                } else {
                    if (user.getUserGroupNew() == 1) {
                        supervisorId = user.getOID();
                        approvalStatus = 1;
                        iErrCode = ctrlCashCashier.action(iCommand, oidCashCashier, request, supervisorId);
                        oidCashCashier = cashCashier.getOID();
                    } else {
                        approvalStatus = 2;
                    }
                }
            }


            if ((iCmd == CMD_APPROVAL) && (approvalStatus == 1) && iErrCode == FRMMessage.NONE) {

                switch (userGroupNewStatus) {
                    case PstAppUser.GROUP_CASHIER:
                        response.sendRedirect("cashier_lyt.jsp?");
                        break;
                    case PstAppUser.GROUP_SALES_ADMIN:
                        response.sendRedirect("srcsalesordecashier.jsp");
                        break;
                    default:
                }
            }
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
    <!-- #BeginEditable "doctitle" -->
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
            
        //change kasir based on Location
        function cntTotal(element, evt)
        {
            var rupiah = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]%>.value,guiDigitGroup,'');
            var amount1 = cleanNumberFloat(document.frmcashcashier.<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>.value,guiDigitGroup,'');

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
            document.frmcashcashier.action = "open_shift.jsp";
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
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdAsk(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.ASK%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdConfirmDelete(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.DELETE%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdSave()
        {
            document.frmcashcashier.command.value="<%=Command.SAVE%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdEdit(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.EDIT%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdCancel(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.EDIT%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdBack()
        {
            document.frmcashcashier.command.value="<%=Command.BACK%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListFirst()
        {
            document.frmcashcashier.command.value="<%=Command.FIRST%>";
            document.frmcashcashier.prev_command.value="<%=Command.FIRST%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListPrev()
        {
            document.frmcashcashier.command.value="<%=Command.PREV%>";
            document.frmcashcashier.prev_command.value="<%=Command.PREV%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListNext()
        {
            document.frmcashcashier.command.value="<%=Command.NEXT%>";
            document.frmcashcashier.prev_command.value="<%=Command.NEXT%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListLast()
        {
            document.frmcashcashier.command.value="<%=Command.LAST%>";
            document.frmcashcashier.prev_command.value="<%=Command.LAST%>";
            document.frmcashcashier.action="open_shift.jsp";
            document.frmcashcashier.submit();
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
      onpageshow="if (event.persisted) noBack();" onunload="">
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
                        <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Cashier - Open CashCashier <!-- #EndEditable --></td>
                    </tr>
                    <tr>
                        <td><!-- #BeginEditable "content" -->
                            <form name="frmcashcashier" method ="post" action="">
                                <input type="hidden" name="command" value="<%=iCommand%>">
                                <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                <input type="hidden" name="start" value="<%=start%>">
                                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                <input type="hidden" name="hidden_cash_cashier_id" value="<%=oidCashCashier%>">
                                <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_APP_USER_ID]%>" value="<%=userId%>">
                                <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SPV_OID]%>" value="<%=supervisorId%>">
                                <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                                <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_TYPE]%>" value="0">
                                <input type="hidden" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SPV_CLOSE_OID]%>" value="1">
                                <input type="hidden" name="FRM_FIELD_DISC_TYPE">
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
                                                <tr align="left" valign="top">
                                                    <td height="22" valign="middle" colspan="3">
                                                        <fieldset >
                                                            <legend> Location </legend>
                                                            <table id="location">
                                                                <tr>
                                                                    <td >Opening Location</td>
                                                                    <td ></td>
                                                                    <td ><%if (userGroupNewStatus == PstAppUser.GROUP_CASHIER) {%>
                                                                        No. Cashier
                                                                        <%} else {%>
                                                                        No. Sales Admin
                                                                        <%}%>
                                                                    </td>
                                                                    <td ></td>

                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"><%

                                                                                long selectedLocId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_LOCATION]);

                                                                                String whereTypeLocation = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = '1'";
                                                                                Vector listLoc = PstLocation.list(0, 0,whereTypeLocation, "");
                                                                                Vector vectLocVal = new Vector(1, 1);
                                                                                Vector vectLocKey = new Vector(1, 1);
                                                                                for (int i = 0; i <= listLoc.size(); i++) {
                                                                                    if (i == 0) {
                                                                                        vectLocKey.add("-none-");
                                                                                        vectLocVal.add("");
                                                                                    } else {
                                                                                        Location location = (Location) listLoc.get(i - 1);
                                                                                        vectLocKey.add(location.getName());
                                                                                        vectLocVal.add("" + location.getOID());
                                                                                    }
                                                                                }
                                                                                String select_Loc = "0";
                                                                                if (selectedLocId != 0) {
                                                                                    select_Loc = "" + selectedLocId;
                                                                                }
                                                                        %>
                                                                        <%=ControlCombo.draw(frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_LOCATION], "formElemen", null, select_Loc, vectLocVal, vectLocKey, "onChange=\"javascript:cmdChangeLocation()\"")%></td>
                                                                    <td width="5%"></td>
                                                                    <td width="60%"><%
                                                                                long selectedCashMasterId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CASH_MASTER_ID]);
                                                                                String where = "";
                                                                                Vector vectMasterVal = new Vector(1, 1);
                                                                                Vector vectMasterKey = new Vector(1, 1);
                                                                                if (selectedLocId != 0) {
                                                                                    where = PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID] + " = '" + selectedLocId + "'";
                                                                                    Vector listcashMaster = PstCashMaster.listCashierNumber(0, 0, where, "");
                                                                                    for (int i = 0; i <= listcashMaster.size(); i++) {
                                                                                        if (i == 0) {
                                                                                            vectMasterKey.add("-none-");
                                                                                            vectMasterVal.add("");
                                                                                        } else {
                                                                                            CashMaster cashMaster = (CashMaster) listcashMaster.get(i - 1);
                                                                                            vectMasterKey.add("" + cashMaster.getCashierNumber());
                                                                                            vectMasterVal.add("" + cashMaster.getOID());
                                                                                        }

                                                                                    }
                                                                                } else {
                                                                                    vectMasterKey.add("-none-");
                                                                                    vectMasterVal.add("");
                                                                                }
                                                                                String select_Cash_Master = "0";
                                                                                if (selectedCashMasterId != 0) {
                                                                                    select_Cash_Master = "" + selectedCashMasterId;
                                                                                }
                                                                        %>
                                                                        <%=ControlCombo.draw(frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CASH_MASTER_ID], "formElemen", null, select_Cash_Master, vectMasterVal, vectMasterKey, "onChange=\"javascript:cmdChangeCashMasterId()\"")%></td>
                                                                    <td width="40%"></td>

                                                                </tr>
                                                                <tr>
                                                                    <td width="20%"></td>
                                                                    <td width="5%"></td>
                                                                    <td width="60%"></td>
                                                                    <td width="40%"></td>

                                                                </tr>
                                                            </table>
                                                        </fieldset>
                                                    </td>
                                                </tr>
                                                <tr >
                                                    <td height="22" valign="middle" colspan="3"></td>
                                                </tr>

                                                <tr>
                                                    <td>
                                                        <%
                                                                    if (iCommand == Command.GOTO) {
                                                                        if (userGroupNewStatus != PstAppUser.GROUP_CASHIER && userGroupNewStatus != PstAppUser.GROUP_SALES_ADMIN) {
                                                        %>
                                                        <table width="800" cellspacing="1" cellpadding="1">
                                                            <tr>
                                                                <td colspan="2" height="22"> Opening Balance</td>
                                                                <td></td>
                                                                <td></td>
                                                                <td></td>
                                                            </tr>

                                                            <tr>
                                                                <td width="6%" class="listgentitle">No.</td>
                                                                <td width="6%" class="listgentitle">Currency</td>
                                                                <td width="25%" class="listgentitle">Exch.Rate</td>
                                                                <td width="63%" class="listgentitle">Amount</td>
                                                                <td width="63%" class="listgentitle"> Subtotal</td>
                                                            </tr>
                                                            <tr>
                                                                <td>1</td>
                                                                <td><%
                                                                                                                                                long selectedCurrencyId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID]);
                                                                                                                                                double tmpAmount = 0;
                                                                                                                                                Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                                                                                                                Vector vectCurrVal = new Vector(1, 1);
                                                                                                                                                Vector vectCurrKey = new Vector(1, 1);
                                                                                                                                                for (int i = 0; i <= listCurr.size(); i++) {
                                                                                                                                                    if (i == 0) {
                                                                                                                                                        vectCurrKey.add("-none-");
                                                                                                                                                        vectCurrVal.add("");
                                                                                                                                                    } else {
                                                                                                                                                        CurrencyType currencyType = (CurrencyType) listCurr.get(i - 1);
                                                                                                                                                        vectCurrKey.add(currencyType.getCode());
                                                                                                                                                        vectCurrVal.add("" + currencyType.getOID());
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                String select_Cur = "0";
                                                                                                                                                if (selectedCurrencyId != 0) {
                                                                                                                                                    select_Cur = "" + selectedCurrencyId;
                                                                                                                                                    Vector listAmount = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + selectedCurrencyId + "'", "");
                                                                                                                                                    if (listAmount.size() > 0) {
                                                                                                                                                        StandartRate standartRate = (StandartRate) listAmount.get(0);
                                                                                                                                                        tmpAmount = standartRate.getSellingRate();
                                                                                                                                                    }
                                                                                                                                                }



                                                                    %>
                                                                    <%=ControlCombo.draw(frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID], "formElemen", null, select_Cur, vectCurrVal, vectCurrKey, "onChange=\"javascript:cmdChangeCurrency()\"")%>
                                                                </td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>" value="<%=amountRp == 0 ? tmpAmount : amountRp%>"></td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]%>" value="<%=rupiah == 0 ? "" : rupiah%>" onkeyup="javascript:cntTotal(this, event)"></td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>" value="<%=subtotal1 == 0 ? "" : subtotal1%>"></td>
                                                            </tr>
                                                            <tr>
                                                                <td>2</td>
                                                                <td><%
                                                                                                                                                String whereCur = "";
                                                                                                                                                long currencyId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID2]);
                                                                                                                                                double tmpAmount2 = 0;
                                                                                                                                                if (selectedCurrencyId != 0) {
                                                                                                                                                    whereCur = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + " = " + PstCurrencyType.INCLUDE + " AND " + PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID] + " != '" + selectedCurrencyId + "'";
                                                                                                                                                }
                                                                                                                                                Vector listCurr2 = PstCurrencyType.list(0, 0, whereCur, "");
                                                                                                                                                Vector vectCurrVal2 = new Vector(1, 1);
                                                                                                                                                Vector vectCurrKey2 = new Vector(1, 1);
                                                                                                                                                for (int i = 0; i <= listCurr2.size(); i++) {
                                                                                                                                                    if (i == 0) {
                                                                                                                                                        vectCurrKey2.add("-none-");
                                                                                                                                                        vectCurrVal2.add("");
                                                                                                                                                    } else {
                                                                                                                                                        CurrencyType currencyType = (CurrencyType) listCurr2.get(i - 1);
                                                                                                                                                        vectCurrKey2.add(currencyType.getCode());
                                                                                                                                                        vectCurrVal2.add("" + currencyType.getOID());
                                                                                                                                                    }

                                                                                                                                                }
                                                                                                                                                String select_Cur2 = "";
                                                                                                                                                select_Cur2 = "" + currencyId;

                                                                                                                                                Vector listAmount = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + currencyId + "'", "");
                                                                                                                                                if (listAmount.size() > 0) {
                                                                                                                                                    StandartRate standartRate = (StandartRate) listAmount.get(0);
                                                                                                                                                    tmpAmount2 = standartRate.getSellingRate();
                                                                                                                                                }



                                                                    %>
                                                                    <!--%=ControlCombo.draw("hidden_currency_id", "formElemen", null, a, vectCurrVal2, vectCurrKey2, "")%-->
                                                                    <%=ControlCombo.draw(frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID2], "formElemen", null, select_Cur2, vectCurrVal2, vectCurrKey2, "onChange=\"javascript:cmdChangeCurrency()\"")%>
                                                                </td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT2]%>" value="<%= tmpAmount2%>"></td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_USD]%>" value="" onkeyup="javascript:cntTotal2(this, event)"></td>
                                                                <td><input type="text" name="<%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL2]%>" value=""></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                                <td>&nbsp;</td>
                                                                <td>&nbsp;</td>
                                                                <td><!--div align="right"><!--%=textMaterialHeader[SESS_LANGUAGE][2]%></div--></td>
                                                                <td><!--input type="text" name="<!--%=frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_BALANCE_VALUE]%>" value=""--></td>
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
                                                        <!--fieldset>
                                                            <legend>Approve by supervisor : </legend>
                                                            <br>
                                                            <p><--%=textMaterialHeader[SESS_LANGUAGE][0]%>
                                                                <input type="text" name="login_id" size="20" onKeyDown="javascript:keybrdPress(this, event)">
                                                                <--%=textMaterialHeader[SESS_LANGUAGE][1]%>
                                                                <input type="password" name="pass_wd" size="15" onKeyPress="javascript:keybrdPress(this, event)">
                                                            </p>
                                                            <p>
                                                                <label>
                                                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                        <tr>
                                                                            <td ></td>
                                                                            <td ><input id="aLogin" type="button" name="Button" value="Submit" onClick="javascript:cmdApproval()" class="formElemen">

                                                                            </td>
                                                                            <td >&nbsp;</td>
                                                                            <td ></td>
                                                                        </tr>
                                                                    </table>
                                                                </label>
                                                            </p>

                                                        </fieldset-->
                                                        <input id="aLogin" type="button" name="Button" value="Submit" onClick="javascript:cmdApproval()" class="formElemen">
                                                        <% }%>
                                                        <%if ((iCmd == CMD_APPROVAL) && (approvalStatus > 1)) {%>
                                                        <font class="errfont" color="#FF0000"><%if (appLanguage == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {%>nama atau password salah<%} else {%>
                                                            username or password wrong, try again...
                                                            <%}%></font>
                                                            <%}%>
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
        <tr>
            <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                <%@ include file = "../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
        </tr>
    </table>
</body>

<!-- #EndTemplate --></html>
