<%-- 
    Document   : m_open_shift
    Created on : Aug 13, 2014, 2:29:26 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.payment.StandartRate"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.pos.entity.masterCashier.CashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.admin.AppUser"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.masterdata.Shift"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstShift"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Time"%>
<%@page import="com.dimata.pos.entity.balance.CashCashier"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.entity.balance.PstCashCashier"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.pos.form.balance.CtrlCashCashier"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.pos.form.balance.FrmCashCashier"%>
<%@page import="com.dimata.pos.entity.balance.OpeningCashCashier"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<!DOCTYPE html>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "CheckUserCashierNew.jsp"%>
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
        return ctrlist.drawBootstrap();
    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidCashCashier = FRMQueryString.requestLong(request, "hidden_cash_cashier_id");
    int typeListCashier = FRMQueryString.requestInt(request, "typeListCashier");
    int typeCashier = FRMQueryString.requestInt(request, "typeCashier");

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
    Vector vctCashCashier=new Vector(1,1);
    long oidCashCashierOnline=0;
    long oidCashierShifId=0;
    String whereClosingBalance=""+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+"="+userId+" AND "+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=1";
    vctCashCashier=PstCashCashier.list(0,0,whereClosingBalance,"");
    if ((vctCashCashier!=null)&&(vctCashCashier.size()>0)){
            cashCashier = (CashCashier)vctCashCashier.get(0);
            oidCashCashierOnline=cashCashier.getOID();
            oidCashierShifId=cashCashier.getShiftId();
    }

    if ((vctCashCashier!=null)&&(vctCashCashier.size()>0)){
             if(typeListCashier==0){//sales order
                response.sendRedirect("cashier_lyt.jsp?typeCashier="+typeCashier);
             }else if(typeListCashier==1){//invoicing sales order
                response.sendRedirect("src_list_open_bill_m.jsp");
             }else if(typeListCashier==2){//return kredit from invoicing
                response.sendRedirect("src_list_retun_bill.jsp?trans_type=2");
             }else if(typeListCashier==3){//kasir payment credit
                response.sendRedirect("srcCustomerInvoice.jsp");
             }else if(typeListCashier==4){//kasir return cash
                response.sendRedirect("srcRetur.jsp?command=23&trans_type=2");
             }else if(typeListCashier==5){//salesinvoice void
                response.sendRedirect("src_list_void_bill.jsp");
             }else if(typeListCashier==6){
                response.sendRedirect("search_po_outlet.jsp");
             }else if(typeListCashier==7){//invoicing sales order
                response.sendRedirect("src_list_issue_bill_m.jsp");
             }
    }
    
%>
<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
        <!-- bootstrap 3.0.2 -->
        <link href="../styles/bootstrap3.1/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <!-- font Awesome -->
        <link href="../styles/bootstrap3.1/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <!-- Ionicons -->
        <link href="../styles/bootstrap3.1/css/ionicons.min.css" rel="stylesheet" type="text/css" />
        <!-- Morris chart -->
        <link href="../styles/bootstrap3.1/css/morris/morris.css" rel="stylesheet" type="text/css" />
        <!-- jvectormap -->
        <link href="../styles/bootstrap3.1/css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
        <!-- fullCalendar -->
        <link href="../styles/bootstrap3.1/css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
        <!-- Daterange picker -->
        <link href="../styles/bootstrap3.1/css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
        <!-- bootstrap wysihtml5 - text editor -->
        <link href="../styles/bootstrap3.1/css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" />
        <!-- Theme style -->
        <link href="../styles/bootstrap3.1/css/AdminLTE.css" rel="stylesheet" type="text/css" />

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
        
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
            document.frmcashcashier.action = "m_open_shift.jsp";
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
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdAsk(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.ASK%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdConfirmDelete(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.DELETE%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdSave()
        {
            document.frmcashcashier.command.value="<%=Command.SAVE%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdEdit(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.EDIT%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdCancel(oidCashCashier)
        {
            document.frmcashcashier.hidden_merk_id.value=oidCashCashier;
            document.frmcashcashier.command.value="<%=Command.EDIT%>";
            document.frmcashcashier.prev_command.value="<%=prevCommand%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdBack()
        {
            document.frmcashcashier.command.value="<%=Command.BACK%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListFirst()
        {
            document.frmcashcashier.command.value="<%=Command.FIRST%>";
            document.frmcashcashier.prev_command.value="<%=Command.FIRST%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListPrev()
        {
            document.frmcashcashier.command.value="<%=Command.PREV%>";
            document.frmcashcashier.prev_command.value="<%=Command.PREV%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListNext()
        {
            document.frmcashcashier.command.value="<%=Command.NEXT%>";
            document.frmcashcashier.prev_command.value="<%=Command.NEXT%>";
            document.frmcashcashier.action="m_open_shift.jsp";
            document.frmcashcashier.submit();
        }

        function cmdListLast()
        {
            document.frmcashcashier.command.value="<%=Command.LAST%>";
            document.frmcashcashier.prev_command.value="<%=Command.LAST%>";
            document.frmcashcashier.action="m_open_shift.jsp";
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
    
    </head>
    <body class="skin-blue">
        <%@ include file = "../header_mobile.jsp" %> 
        <div class="wrapper row-offcanvas row-offcanvas-left">
            
            <!-- Left side column. contains the logo and sidebar -->
            <%@ include file = "../menu_left_mobile.jsp" %> 

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmcashcashier" method ="post" action=""  role="form">
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
                        <input type="hidden" name="typeListCashier" value="<%=typeListCashier%>">
                        <input type="hidden" name="typeCashier" value="<%=typeCashier%>">
                        <input type="hidden" name="FRM_FIELD_DISC_TYPE">
                        <input type="hidden" name="cmd" value="<%=CMD_APPROVAL%>">
                        <div class="box-body">
                            <%
                            try {
                            %>
                             <%=drawList(SESS_LANGUAGE, iCommand, frmCashCashier, openingCashCashier, listCashCashier, oidCashCashier, start)%>
                              <%
                            } catch (Exception exc) {
                            }%>
                        </div>
                        <div class="box-body">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr align="left" valign="top">
                                    <td height="22" valign="middle" colspan="3">
                                        <fieldset >
                                            <legend> Location </legend>
                                            <table id="location">
                                                <tr>
                                                    <td >Opening Location</td>
                                                    <td ></td>
                                                    <td >No.
                                                    </td>
                                                    <td ></td>
                                                </tr>
                                                <tr>
                                                    <td width="20%">
                                                        <%
                                                        long selectedLocId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_LOCATION]);
                                                        String whereClausex = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                                               " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";
                                                        whereClausex += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                                        Vector listLoc = PstLocation.listLocationCreateDocument(0, 1, whereClausex, "");
                                                        Vector vectLocVal = new Vector(1, 1);
                                                        Vector vectLocKey = new Vector(1, 1);
                                                        vectLocKey.add("-none-");
                                                        vectLocVal.add("");
                                                        for (int i = 0; i < listLoc.size(); i++) {
                                                             Location location = (Location) listLoc.get(i);
                                                             vectLocKey.add(location.getName());
                                                             vectLocVal.add("" + location.getOID());
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
                                                <tr>
                                                    <td>
                                                        <%
                                                         if (iCommand == Command.GOTO) {
                                                           if (cashierUserOpeningClosing==1) {
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
                                                                    <%
                                                                        long selectedCurrencyId = FRMQueryString.requestLong(request, frmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID]);
                                                                        double tmpAmount = 0;
                                                                        Vector vectCurrVal = new Vector(1, 1);
                                                                        Vector vectCurrKey = new Vector(1, 1);
                                                                        String select_Cur = "0";
                                                                    %>
                                                                    <td><%
                                                                        Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
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
                                                                        
                                                                        if (selectedCurrencyId != 0) {
                                                                            select_Cur = "" + selectedCurrencyId;
                                                                            Vector listAmount = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " = '" + selectedCurrencyId + "'", "");
                                                                            if (listAmount.size() > 0) {
                                                                                StandartRate standartRate = (StandartRate) listAmount.get(0);
                                                                                tmpAmount = standartRate.getSellingRate();
                                                                            }
                                                                        }
                                                                        double amount1=0;
                                                                        if(amountRp==0){
                                                                            amount1=tmpAmount;
                                                                        }else{
                                                                            amount1=amountRp;
                                                                        }
                                                                        %>
                                                                        <%=ControlCombo.draw(FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CURRENCY_ID], "formElemen", null, select_Cur, vectCurrVal, vectCurrKey, "onChange=\"javascript:cmdChangeCurrency()\"")%>
                                                                    </td>
                                                                    <td><input type="text" name="<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_AMOUNT1]%>" value="<%=amount1%>"></td>
                                                                    <td><input type="text" name="<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_RUPIAH]%>" value="<%=rupiah == 0 ? rupiah : rupiah%>" onkeyup="javascript:cntTotal(this, event)"></td>
                                                                    <td><input type="text" name="<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_SUBTOTAL1]%>" value="<%=subtotal1 == 0 ? subtotal1 : subtotal1%>"></td>
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
                                                                    <td></td>
                                                                    <td></td>
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
                                        </fieldset>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </form>
                </section><!-- /.content -->
                
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->


        <!-- jQuery 2.0.2 -->
        <script src="../styles/bootstrap3.1/js/jquery.min.js"></script>
        <!-- jQuery UI 1.10.3 -->
        <script src="../styles/bootstrap3.1/js/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
        <!-- Bootstrap -->
        <script src="../styles/bootstrap3.1/js/bootstrap.min.js" type="text/javascript"></script>
        <!-- Morris.js charts -->
        <script src="../styles/bootstrap3.1/js/raphael-min.js"></script>
        <script src="../styles/bootstrap3.1/js/plugins/morris/morris.min.js" type="text/javascript"></script>
        <!-- Sparkline -->
        <script src="../styles/bootstrap3.1/js/plugins/sparkline/jquery.sparkline.min.js" type="text/javascript"></script>
        <!-- jvectormap -->
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" type="text/javascript"></script>
        <script src="../styles/bootstrap3.1/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js" type="text/javascript"></script>
        <!-- fullCalendar -->
        <script src="../styles/bootstrap3.1/js/plugins/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
        <!-- jQuery Knob Chart -->
        <script src="../styles/bootstrap3.1/js/plugins/jqueryKnob/jquery.knob.js" type="text/javascript"></script>
        <!-- daterangepicker -->
        <script src="../styles/bootstrap3.1/js/plugins/daterangepicker/daterangepicker.js" type="text/javascript"></script>
        <!-- Bootstrap WYSIHTML5 -->
        <script src="../styles/bootstrap3.1/js/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" type="text/javascript"></script>
        <!-- iCheck -->
        <script src="../styles/bootstrap3.1/js/plugins/iCheck/icheck.min.js" type="text/javascript"></script>

        <!-- AdminLTE App -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/app.js" type="text/javascript"></script>
        
        <!-- AdminLTE dashboard demo (This is only for demo purposes) -->
        <script src="../styles/bootstrap3.1/js/AdminLTE/dashboard.js" type="text/javascript"></script>        

    </body>
</html>
