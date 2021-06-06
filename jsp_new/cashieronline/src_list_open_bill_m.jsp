<%-- 
    Document   : src_list_open_bill_m
    Created on : Aug 13, 2014, 2:57:12 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@ include file = "CheckUserCashierNew.jsp"%>
<%@include file = "checkusercashieronline.jsp" %>
<%
        int appObjCodeKasirInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_INVOICE);
        //int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT);

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
          {"Guest Type", "Date", "all date", "Invoice Number","No Invoice", "NO SR","Sales Person"},
        {"Guest Type", "Date", "all date", "Invoice Number","No Invoice", "NO SR","Sales Person"}
    };
    public static final String textListHeader[][] = {
        {"Code", "Date", "No SR", "Outlet", "Total","Sales Person","Status Document","Currency","Action"},
        {"Code", "Date", "No SR", "Outlet", "Total","Sales Person","Status Document","Currency","Action"}
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
            ctrlist.dataFormat(textListHeader[language][1], "10%", "left", "left");
            ctrlist.dataFormat(textListHeader[language][2], "20%", "left", "left");
            ctrlist.dataFormat(textListHeader[language][3], "10%", "left", "left");
            ctrlist.dataFormat(textListHeader[language][6], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][8], "10%", "center", "left");
            
            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            //ctrlist.setLinkPrefix("javascript:cmdEdit('");
            //ctrlist.setLinkSufix("')");
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
                    AppUser ap = new AppUser();
                    ap = PstAppUser.fetch(billMain.getAppUserId());
                    salesName = ap.getFullName();
//                    salesName = PstSales.getNameSales(billMain.getSalesCode());
                } catch (Exception e) {
                    salesName="";
                }

                rowx.add("<div align=\"left\">" + (i + start + 1) + "</div>");
                rowx.add("<div align=\"left\">" + Formater.formatDate(billMain.getBillDate(), "dd-MM-yyyy") + "</div>");
                
                String notePo="";
                try{
                    String[] smartPhonesSplits = billMain.getNotes().split("\\;");
                    notePo=smartPhonesSplits[0];
                }catch(Exception ex){
                    billMain.getInvoiceNumber();
                }
                
                rowx.add("<div align=\"left\">" +notePo+ "</div>");
                rowx.add("<div align=\"left\">" + memberReg.getPersonName() + "</div>");

                if(billMain.getStatusInv()==1){
                    rowx.add("<div align=\"center\">On Prosess</div>");
                }else if (billMain.getStatusInv()==2){
                    rowx.add("<div align=\"center\">Done</div>");
                }else{
                    rowx.add("<div align=\"center\">Draft</div>");
                }
                rowx.add(""
                    + "<div align='center'>"
                        + "<button type=\"button\" onclick=\"javascript:cmdEdit('"+String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv()+"') \" class='btn btn-success '>Print </button> &nbsp;"
                        + "<button type=\"button\" onclick=\"javascript:cmdProses('"+String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv()+"','2')\" class=\"btn btn-primary\">Proses</button>"
                        //+ //"<a href=\"javascript:cmdProses('"+String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv()+"','2')\">Proses</a>"
                    + "</div>"
                + "");
                
                lstData.add(rowx);
                //lstLinkData.add(String.valueOf(billMain.getOID())+"','"+salesName+"','"+billMain.getStatusInv());
            }
            result = ctrlist.drawBootstrap();
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
    String invoiceNumber = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_NUMBER]);  
    String notes = FRMQueryString.requestString(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]);  
    long location   =   FRMQueryString.requestLong(request, FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID] );   
    int statusInv =   FRMQueryString.requestInt(request, "FRM_FIELD_STATUS");   
    //FRM_FIELD_STATUS_DATE
    long customerId = FRMQueryString.requestLong(request, "cust_id");
    String custName = FRMQueryString.requestString(request, "cust_name");
    String personName = FRMQueryString.requestString(request, "person_name");
    BillMain bMain = new BillMain();
    int recordToGet = 20;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;

    String whereClause = "";
    String orderClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];

    //jika mendapatkan hak akses kasir
    /*if(privApprovalKasirPay){
        whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "='1'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " != '0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"='"+statusInv+"'";
        
    }else {//hak akses invoicing
        whereClause =PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " = '0'"+
                     " AND cm."+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"!=2";
    }*/
    whereClause = PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"='0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"='0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "='1'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_INVOICE_NUMBER] + " != '0'"
                     + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_STATUS_INVOICING]+"='"+statusInv+"'";

  
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

    
    bMain.setBillDate(billDate);
    bMain.setInvoiceNumber(invoiceNumber);
    bMain.setCustomerId(customerId);
    bMain.setStatusDate(statusDate);
    bMain.setDatefrom(datefrom);
    bMain.setDateto(dateto);
    bMain.setCustName(custName);
    bMain.setPersonName(personName);
    bMain.setNotes(notes);
    bMain.setLocationId(location); 
    // get record to display
    
    if (location !=0 ) { 
        whereClause = whereClause + "AND cm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +  
                " = '" + location+"'";
    }else{
        whereClause = whereClause + "AND cm."+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID] +  
                " = '0'";
    }
    
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

            function cmdAsk(oidBillMain)
            {
                document.frmopenbill.hidden_bill_main_id.value=oidBillMain;
                document.frmopenbill.command.value="<%=Command.ASK%>";
                document.frmopenbill.prev_command.value="<%=prevCommand%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }

            function cmdEdit(oidBillMain,SalesName,StatusInvoice){
                    strvalue  = "../cashieronline/outlet_cashier_m.jsp?FRM_FIELD_TYPE_SALES_ORDER=0&oidBillMain="+oidBillMain+"&commandDetail=1&command=3&notasalestype=0";
                    window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=yes,menubar=yes,location=no,scrollbars=yes");
            }
            
            function cmdProses(oidBillMain,SalesName,StatusInvoice,typeBill){
                if(StatusInvoice==2){
                    alert("Invoice Sudah Done, Tidak bisa di Proses Lagi");
                }else{
                    document.frmopenbill.command.value="<%=Command.ADD%>";
                    document.frmopenbill.hidden_bill_main_id.value=oidBillMain;
                    document.frmopenbill.nota_type.value="<%=PstBillMain.OPEN_BILL%>";
                    document.frmopenbill.SalesName.value=SalesName;
                    document.frmopenbill.ApprovalKasirPay.value="1";
                    document.frmopenbill.typeCashier.value="1";
                    document.frmopenbill.typeBill.value=typeBill;
                    document.frmopenbill.typeListBill.value="0";
                    document.frmopenbill.action="cashier_lyt_m.jsp";
                    document.frmopenbill.submit();
                    
                }
            }

            function cmdListFirst()
            {
                document.frmopenbill.command.value="<%=Command.FIRST%>";
                document.frmopenbill.prev_command.value="<%=Command.FIRST%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }

            function cmdListPrev()
            {
                document.frmopenbill.command.value="<%=Command.PREV%>";
                document.frmopenbill.prev_command.value="<%=Command.PREV%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }

            function cmdListNext()
            {
                document.frmopenbill.command.value="<%=Command.NEXT%>";
                document.frmopenbill.prev_command.value="<%=Command.NEXT%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }

            function cmdListLast()
            {
                document.frmopenbill.command.value="<%=Command.LAST%>";
                document.frmopenbill.prev_command.value="<%=Command.LAST%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }
            function cmdSearch(){
                document.frmopenbill.start.value="0";
                document.frmopenbill.command.value="<%=Command.LIST%>";
                document.frmopenbill.action="src_list_open_bill_m.jsp";
                document.frmopenbill.submit();
            }

            function modifKey(frmObj, event,value){
                if(event.keyCode == 13) { //enter
                  cmdSearch();
                }else if (event.keyCode==112){ //F1=Open Sales Order
                        strvalue  = "src_list_open_bill_m.jsp";
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
                        Search  Issue Store Request
                        <small></small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">
                    <form name="frmopenbill" method ="post" action=""  role="form">
                        <!--form hidden -->
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                        <input type="hidden" name="SalesName" value="">
                        <input type="hidden" name="nota_type" value="">
                        <input type="hidden" name="typeCashier" value="">
                        <input type="hidden" name="ApprovalKasirPay" value="">
                        <input type="hidden" name="typeBill" value="">
                        <input type="hidden" name="typeListBill" value="">
                        
                        <!--body-->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-5">
                                    
                                     <div class="form-group">
                                         <label for="exampleInputEmail1"><%=textMaterialHeader[SESS_LANGUAGE][4]%></label>
                                            <%
                                                    long selectedLocationId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[frmBillMain.FRM_FIELD_LOCATION_ID]);
                                                    Vector val_locationid = new Vector(1,1);
                                                    Vector key_locationid = new Vector(1,1);
                                                    //Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                     String whereClausex = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                                                       " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";
                                                    whereClausex += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                                    Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClausex, "");
                                                    val_locationid.add("0");
                                                    key_locationid.add("Select Location");
                                                    for(int d=0;d<vt_loc.size();d++){
                                                            Location loc = (Location)vt_loc.get(d);
                                                            val_locationid.add(""+loc.getOID()+"");
                                                            key_locationid.add(loc.getName());
                                                    }
                                                    
                                                    String select_loc = "0";
                                                    if (selectedLocationId!=0){
                                                        select_loc= ""+selectedLocationId;

                                                    }
                                            %>
                                            <%=ControlCombo.drawBootsratap(frmBillMain.fieldNames[frmBillMain.FRM_FIELD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, ""+location, "form-control")%>
                                     </div>
                                     
                                     
                                     
                                     <div class="form-group">
                                         <label for="exampleInputEmail1"><%=textMaterialHeader[SESS_LANGUAGE][5]%></label>
                                         <input type="text" class="form-control"  placeholder="No Store Request" name=<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>  value="<%=notes%>">
                                     </div>
                                    
                                </div>
                                <div class="col-md-7">
                                     <div class="form-group">
                                            <label for="exampleInputEmail1">Tanggal</label><br>
                                            <input type="radio" class="formElemen" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_STATUS_DATE]%>" <%if(bMain.getStatusDate() == 0){%>checked<%}%>  value="0">
                                            <%=textMaterialHeader[SESS_LANGUAGE][2]%>
                                     </div>
                                     <div class="form-group">
                                        <input type="radio" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_STATUS_DATE]%>" <%if(bMain.getStatusDate() == 1){%>checked<%}%> value="1" class="minimal-red">
                                        from
                                        <%=ControlDate.drawDateWithBootstrap(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_FROM], bMain.getStatusDate() == 0? new Date() : bMain.getDatefrom() , 1, -5,"form-control-date","")%>
                                        &nbsp;to&nbsp;<%=ControlDate.drawDateWithBootstrap(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DATE_TO], bMain.getStatusDate() == 0? new Date() : bMain.getDateto(), 1, -5, "form-control-date","")%>
                                    </div>
                                     
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="box-footer">
                                        <button  onclick="javascript:cmdSearch()" type="submit" class="btn btn-primary">Search</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="row">&nbsp;</div>
                                </div>
                            </div>
                            <div class="row">
                                 <div class="col-md-12">
                                    <%=drawList(SESS_LANGUAGE, listOpenBill, oidBillMain, start)%>
                                </div>
                            </div>
                            <div class="box-footer clearfix">
                                     <% 
                                        ctrLine.setLocationImg(approot+"/images");
                                        ctrLine.initDefault();
                                        String strList = ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet);
                                        if(strList.length()>0){
                                        %>
                                        <%=strList%>
                             <%}%>
                            </div>
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
