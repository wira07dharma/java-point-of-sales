<%@page import= "com.dimata.posbo.session.masterdata.*"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>

<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_POSTING);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%! public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "Lokasi","Periode","s/d"},
        {"No", "Location","Periode","To"}
    };
    public static final String textListTitleHeader[][] = {
        {"Reposting","Stok"},
        {"Reposting","Stock"}
    };
    
 
    public static final String textTitleHeader[][] = {
    {"Pencarian Barang","Kode","Nama","Supplier","Kategori","Tipe","Urut Berdasar","Cari Barang",
             "Tambah Barang Baru","Tipe Barang","Merk","Tampilkan Gambar","Refresh Catalog","Group","Semua Group","Semua","-","No","Barcode"},
    {"Goods Searching","Code","Name","Supplier","Group","Tipe","Sort By","Goods Search",
             "Add New Goods","Goods Type","Category","Show Picture","Refresh Catalog","Group","All Group","All","-","No","Barcode"}
};
    
    
    public String drawList(HttpServletRequest request, int language, Vector objectClass, long connId, String approot, int start, int recordToGet)throws Exception {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("70%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textTitleHeader[language][17], "3%");
        ctrlist.addHeader(textTitleHeader[language][1], "8%");
        ctrlist.addHeader(textTitleHeader[language][18], "20%");
        ctrlist.addHeader(textTitleHeader[language][2], "40%");
        
        
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Material material = (Material) objectClass.get(i);            
            Vector rowx = new Vector();
            

            start = start + 1;
            
            rowx.add("" + start );
            rowx.add("" + material.getSku());
            rowx.add(""+ material.getBarCode());

            rowx.add(""+ material.getName());

            lstData.add(rowx);
            //lstLinkData.add(String.valueOf(loc.getOID()));

        }

        return ctrlist.draw();
    }


    
    public String drawCalendarJS(String id){
        String str="";
        str+="<script language=\"javascript\">" +
                "var cal_obj2 = null;\n"+
                "var format = '%j-%m-%Y';\n"+

             "function show_calendar_"+id+"(el) {\n"+
                "if (cal_obj2) return;\n"+
                "var text_field = document.getElementById(\""+id+"\");\n"+
                "cal_obj2 = new RichCalendar();\n"+
                "cal_obj2.start_week_day = 0;\n"+
                "cal_obj2.show_time = false;\n"+
                "cal_obj2.language = 'en';\n"+
                "cal_obj2.user_onchange_handler = cal2_on_change_"+id+";\n"+
                "cal_obj2.user_onclose_handler = cal2_on_close_"+id+";\n"+
                "cal_obj2.user_onautoclose_handler = cal2_on_autoclose_"+id+";\n"+
                "cal_obj2.parse_date(text_field.value, format);\n"+
                "cal_obj2.show_at_element(text_field, \"adj_left-bottom\");\n"+
                //cal_obj2.change_skin('alt');
                "}\n"+

            // user defined onchange handler
            "function cal2_on_change_"+id+"(cal, object_code) {\n"+
                "if (object_code == 'day') {\n"+
                    "if(cal.get_formatted_date('%j').length <= 1){\n" +
                    "format='0%j-%m-%Y';\n" +
                    "}else{\n" +
                    "format='%j-%m-%Y';\n" +
                    "}\n" +
                    "document.getElementById(\""+id+"\").value = cal.get_formatted_date(format);\n"+
                    "cal.hide();\n"+
                    "cal_obj2 = null;\n"+
                "}\n"+
            "}\n"+

            // user defined onclose handler (used in pop-up mode - when auto_close is true)
            "function cal2_on_close_"+id+"(cal) {\n"+
                "if (window.confirm('Are you sure to close the calendar?')) {\n"+
                    "cal.hide();\n"+
                    "cal_obj2 = null;\n"+
                "}\n"+
            "}\n"+

            // user defined onautoclose handler
            "function cal2_on_autoclose_"+id+"(cal) {\n"+
                "cal_obj2 = null;\n"+
            "}\n" +
            "</script>\n";
        return str;
    }


    public String cekNull(String val) {
        if (val == null) {
            val = "";
        }
        return val;
    }
%>

<%
            /* get data from request form */
            
            int iCommand = FRMQueryString.requestCommand(request);
            int startPosting = FRMQueryString.requestInt(request, "start_posting");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidDbConn = FRMQueryString.requestLong(request, FrmConnection.fieldNames[FrmConnection.FRM_FIELD_OID]);
            Date dateFrom = FRMQueryString.requestDate(request, FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM]);
            Date dateTo = FRMQueryString.requestDate(request, FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO]);
            long oidLocation = FRMQueryString.requestLong(request, FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_LOCATIONID]); 
           //Date dateFrom = FRMQueryString.requestDate(request, "hidden_date_from");
            //Date dateTo = FRMQueryString.requestDate(request, "hidden_date_to");
            //long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
            
            //System.out.println(""+dateFrom);
            //System.out.println(""+dateTo);
            System.out.println(""+oidLocation);
            
            

            
            long jml =0;

            String msgString = "";
            
            SrcMaterialRepostingStock srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            //SessMaterial sessMaterial = new SessMaterial();
            SessMaterialReposting sessMaterialReposting = new SessMaterialReposting();
            FrmSrcMaterialRepostingStock frmSrcMaterialRepostingStock = new FrmSrcMaterialRepostingStock(request,srcMaterialRepostingStock);
            
            try {
                    //srcMaterialRepostingStock = (SrcMaterialRepostingStock) session.getValue(SessMaterial.SESS_SRC_MATERIAL);
                    srcMaterialRepostingStock = (SrcMaterialRepostingStock) session.getValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING);
                    srcMaterialRepostingStock.setDateFrom(dateFrom);
                    srcMaterialRepostingStock.setDateTo(dateTo);
                    srcMaterialRepostingStock.setLocationId(oidLocation);
    
                if (srcMaterialRepostingStock == null) {
                 srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            }
            } catch (Exception e) {
                //out.println(textListTitleHeader[SESS_LANGUAGE][3]);
                srcMaterialRepostingStock = new SrcMaterialRepostingStock();
            }
            //session.putValue(SessMaterial.SESS_SRC_MATERIAL, srcMaterialRepostingStock);
            session.putValue(SessMaterialReposting.SESS_SRC_MATERIAL_REPOSTING, srcMaterialRepostingStock);
            long categoryId = srcMaterialRepostingStock.getCategoryId();
            long locationId = srcMaterialRepostingStock.getLocationId();
            Date dateStart = srcMaterialRepostingStock.getDateFrom();
            Date dateEnd = srcMaterialRepostingStock.getDateTo();
            System.out.println("Category Id =" +categoryId);
            System.out.println("Location Id =" +locationId);
            System.out.println("Date Start = "+dateStart);
            System.out.println("Date End =" +dateEnd);
            
           /** boolean isProses[]= new boolean [sessMaterial.getCountSearchRepostingStok(srcMaterialRepostingStock)];
            Vector vecMaterial = new Vector();
            for(int i=0;i<isProses.length;i++ ){
                    long lx = FRMQueryString.requestLong(request, "data_is_process" + i);
                    if(lx!=0){
                        vecMaterial.add(lx);
                    }
            }**/
            
            //Vector vecMaterial = sessMaterial.searchMaterialRepostingStock(srcMaterialRepostingStock,0,0);
            Vector vecMaterial = sessMaterialReposting.searchMaterialRepostingStock(srcMaterialRepostingStock,0,0);
            int vecMaterialSize = SessMaterial.getCountSearchRepostingStok(srcMaterialRepostingStock);
            
            
            switch(iCommand){
                case Command.START:
                    SessReposting.startTransfer(vecMaterial, srcMaterialRepostingStock);
                    break;
                case Command.STOP:
                    //for(int i=0;i<(vecMaterial.size());i++ ){
                            //long oidDbConnec = Long.parseLong("" + vecOutletConn.get(i));
                            //Material mat = PstMaterial.fetchExc(Long.parseLong("" + vecMaterial.get(i)));
                            //SessReposting.stopThread(mat.getOID());
                            SessReposting.stopThread(srcMaterialRepostingStock.getLocationId());
                   // }
                    break;
                case Command.LOCK:
                   // for(int i=0;i<(vecMaterial.size());i++ ){
                            //long cashMaster = Long.parseLong("" + vecOutletConn.get(i));
                            //Material mat = PstMaterial.fetchExc(Long.parseLong("" + vecMaterial.get(i)));
                            //SessReposting.pauseThread(mat.getOID());
                            SessReposting.stopThread(srcMaterialRepostingStock.getLocationId());
                    //}
                    break;
                case Command.UNLOCK:
                   // for(int i=0;i<vecMaterial.size();i++ ){
                            //Material mat = PstMaterial.fetchExc(Long.parseLong("" + vecMaterial.get(i)));
                            //SessReposting.resumeThread(mat.getOID());
                              SessReposting.resumeThread(srcMaterialRepostingStock.getLocationId());
         
                   // }
                    break;
               
                default:;
            }

            String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; 
            /* variable declaration */
            int recordToGet = 20;
            
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = PstLocation.fieldNames[PstLocation.FLD_CODE];

            /* ControlLine */
            ControlLine ctrLine = new ControlLine();
            
            
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            var displaycountdown="no";
            

                /*------------- start location function ---------------*/
                function cmdStart()
                {
                    
                    document.frmProcessReposting.command.value="<%=Command.START%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();

                }

                function refreshPostingProgress(){

                    document.frmProcessReposting.command.value="<%=Command.REFRESH%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdStop()
                {
                    document.frmProcessReposting.command.value="<%=Command.STOP%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    //document.frmProcessReposting.action="proses_reposting.jsp";
                    //document.frmProcessReposting.action="material_list_reposting_stock.jsp";
                    document.frmProcessReposting.action="srcmaterial_reposting_stock.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdPause()
                {
                    document.frmProcessReposting.command.value="<%=Command.LOCK%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdResume()
                {
                    document.frmProcessReposting.command.value="<%=Command.UNLOCK%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdEdit(oidInternet)
                {
                    
                    document.frmProcessReposting.command.value="<%=Command.EDIT%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdCancel(oidInternet)
                {
                    document.frmProcessReposting.hidden_location_id.value=oidInternet;
                    document.frmProcessReposting.command.value="<%=Command.EDIT%>";
                    document.frmProcessReposting.prev_command.value="<%=prevCommand%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdBack()
                {
                    document.frmProcessReposting.command.value="<%=Command.BACK%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdListFirst()
                {
                    document.frmProcessReposting.command.value="<%=Command.FIRST%>";
                    document.frmProcessReposting.prev_command.value="<%=Command.FIRST%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdListPrev()
                {
                    document.frmProcessReposting.command.value="<%=Command.PREV%>";
                    document.frmProcessReposting.prev_command.value="<%=Command.PREV%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdListNext()
                {
                    document.frmProcessReposting.command.value="<%=Command.NEXT%>";
                    document.frmProcessReposting.prev_command.value="<%=Command.NEXT%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();
                }

                function cmdListLast()
                {
                    document.frmProcessReposting.command.value="<%=Command.LAST%>";
                    document.frmProcessReposting.prev_command.value="<%=Command.LAST%>";
                    document.frmProcessReposting.action="proses_reposting.jsp";
                    document.frmProcessReposting.submit();

                }


                /*------------- end location function ---------------*/

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
        <%if(menuUsed == MENU_ICON){%>
            <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="stylesheet" type="text/css" href="../../resources/rich_calendar/rich_calendar.css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->

        <!--<link rel="stylesheet" href="../../resources/style.css" type="text/css"/>-->

        <link rel="stylesheet" type="text/css" href="../../resources/rich_calendar/rich_calendar.css">
	<script type="text/javascript" src="../../resources/rich_calendar/rich_calendar.js"></script>

	<script type="text/javascript" src="../../resources/rich_calendar/rc_lang_en.js"></script>
	<script type="text/javascript" src="../../resources/rich_calendar/rc_lang_ru.js"></script>
	<script type="text/javascript" src="../../resources/domready/domready.js"></script>

         <script type="text/javascript" src="../../resources/iMask/mootools.js"></script>
         <script type="text/javascript" src="../../resources/iMask/moodalbox.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shCore.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shBrushXml.js"></script>
         <script type="text/javascript" src="../../resources/iMask/shBrushJScript.js"></script>
         <script type="text/javascript" src="../../resources/iMask/imask-full.js"></script>



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

          //::::::::::::::::::::::::::::::::::::::::function for calendar

            var format = '%dd-%m-%Y';
            var cal_obj2_start = null;
            var cal_obj2_end = null;

            // show calendar
            function show_cal_start(el) {

                if (cal_obj2_start) return;

                var text_field = document.getElementById("text_field");

                cal_obj2_start = new RichCalendar();
                cal_obj2_start.start_week_day = 0;
                cal_obj2_start.show_time = false;
                cal_obj2_start.language = 'en';
                cal_obj2_start.user_onchange_handler = cal2_on_change_start;
                cal_obj2_start.user_onclose_handler = cal2_on_close_start;
                cal_obj2_start.user_onautoclose_handler = cal2_on_autoclose_start;

                cal_obj2_start.parse_date(text_field.value, format);

                cal_obj2_start.show_at_element(text_field, "adj_left-bottom");
                //cal_obj2_start.change_skin('alt');

            }
            function show_cal_end(el) {

                if (cal_obj2_end) return;

                var text_field = document.getElementById("text_field1");

                cal_obj2_end = new RichCalendar();
                cal_obj2_end.start_week_day = 0;
                cal_obj2_end.show_time = false;
                cal_obj2_end.language = 'en';
                cal_obj2_end.user_onchange_handler = cal2_on_change_end;
                cal_obj2_end.user_onclose_handler = cal2_on_close_end;
                cal_obj2_end.user_onautoclose_handler = cal2_on_autoclose_end;

                cal_obj2_end.parse_date(text_field.value, format);

                cal_obj2_end.show_at_element(text_field, "adj_left-bottom");
                //cal_obj2_start.change_skin('alt');

            }

            // user defined onchange handler
            function cal2_on_change_start(cal, object_code) {
                if (object_code == 'day') {
                    document.getElementById("text_field").value = cal.get_formatted_date(format);
                    cal.hide();
                    cal_obj2_start = null;
                }
            }
            function cal2_on_change_end(cal, object_code) {
                if (object_code == 'day') {
                    document.getElementById("text_field1").value = cal.get_formatted_date(format);
                    cal.hide();
                    cal_obj2_end = null;
                }
            }

            // user defined onclose handler (used in pop-up mode - when auto_close is true)
            function cal2_on_close_start(cal) {
                if (window.confirm('Are you sure to close the calendar?')) {
                    cal.hide();
                    cal_obj2_start = null;
                }
            }
            function cal2_on_close_end(cal) {
                if (window.confirm('Are you sure to close the calendar?')) {
                    cal.hide();
                    cal_obj2_end = null;
                }
            }

            // user defined onautoclose handler
            function cal2_on_autoclose_start(cal) {
                cal_obj2_start = null;
            }
            function cal2_on_autoclose_end(cal) {
                cal_obj2_end = null;
            }



 </SCRIPT>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

        <%if(menuUsed == MENU_PER_TRANS){%>
          <tr>
            <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
              <%@ include file = "../../main/header.jsp" %>
              <!-- #EndEditable --></td>
          </tr>
          <tr>
            <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
              <%@ include file = "../../main/mnmain.jsp" %>
              <!-- #EndEditable --> </td>
          </tr>
          <%}else{%>
           <tr bgcolor="#FFFFFF">
            <td height="10" ID="MAINMENU">
              <%@include file="../../styletemplate/template_header.jsp" %>
            </td>
          </tr>
          <%}%>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListTitleHeader[SESS_LANGUAGE][1]%> &gt; <%=locationTitle%>&nbsp;<%=textListTitleHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmProcessReposting" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="start_connection" value="<%=startPosting%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_FROM]%>" value="<%=Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "dd-MM-yyyy", "dd-mm-yyyy")%>">
                                    <input type="hidden" name="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_DATE_TO]%>" value="<%=Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "dd-MM-yyyy", "dd-mm-yyyy")%>">
                                    <input type="hidden" name="<%=FrmSrcMaterialRepostingStock.fieldNames[FrmSrcMaterialRepostingStock.FRM_FIELD_LOCATIONID]%>" value="<%=srcMaterialRepostingStock.getLocationId()%>">

                                   

                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top">
                                            <td height="8"  colspan="3">
                                                <table width="100%" border="0" cellspacing="3" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + locationTitle : locationTitle + " List"%></u></td>
                                                    </tr>
                                                    
                                                     <tr>
                                                        <td><strong><%=textListHeader[SESS_LANGUAGE][2]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        <td>
                                                            <%=Formater.formatDate(srcMaterialRepostingStock.getDateFrom(), "dd-MM-yyyy", "dd-mm-yyyy")
                                                            //Formater.formatDate(dateFrom, "dd-MM-yyyy")
                                                            
                                                            %>
                                                            <%=textListHeader[SESS_LANGUAGE][3]%>
                                                            <%=Formater.formatDate(srcMaterialRepostingStock.getDateTo(), "dd-MM-yyyy", "dd-mm-yyyy")
                                                              //Formater.formatDate(dateTo, "dd-MM-yyyy")
                                                              
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="15%"><b><%=textListHeader[SESS_LANGUAGE][1]%></b></td>
                                                        <td width="1%"><b>:</b></td>
                                                        <%
                                                          Location objLocation = new Location();
                                                          if (oidLocation != 0) {
                                                          try	{
                                                                    objLocation = PstLocation.fetchExc(srcMaterialRepostingStock.getLocationId());
                                                                } catch(Exception e) {
                                                        }
                                                        }
                                                        %>
                                                        <td width="74%"><%=objLocation.getName().toUpperCase()%></td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <% String matCode = "";
                                                           String matName = "";
                                                           String supplierName = "";
                                                           String categoryName = "";
                                                        %>
                                                        
                                                        <td><strong><%=textTitleHeader[SESS_LANGUAGE][1]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        <td>
                                                            <%
                                                            if (srcMaterialRepostingStock.getMatcode()!= "" && srcMaterialRepostingStock.getMatcode()!=null) {
                                                                try {
                                                                   matCode=srcMaterialRepostingStock.getMatcode(); 
                                                                }
                                                                catch(Exception e) {
                                                                }
                                                            }
                                                             else {
                                                                matCode="-";
                                                             }
                                                            %>
                                                            
                                                           <%=matCode.toUpperCase()%> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><strong><%=textTitleHeader[SESS_LANGUAGE][2]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        <td>
                                                            <%
                                                            if (srcMaterialRepostingStock.getMatname()!= "" && srcMaterialRepostingStock.getMatname()!=null) {
                                                                try {
                                                                   matName=srcMaterialRepostingStock.getMatname(); 
                                                                }
                                                                catch(Exception e) {
                                                                }
                                                            }
                                                             else {
                                                                matName="-";
                                                             }
                                                            %>
                                                            <%=matName.toUpperCase()%> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><strong><%=textTitleHeader[SESS_LANGUAGE][3]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        
                                                         <%
                                                            if (srcMaterialRepostingStock.getSupplierId()>0) {
                                                                try {
                                                                    ContactList contactList = new ContactList();
                                                                    try{
                                                                        contactList = PstContactList.fetchExc(srcMaterialRepostingStock.getSupplierId());
                                                                    }catch(Exception e){
                                                                        System.out.println("Exc when get periode");
                                                                     }
                                                                    
                                                                   supplierName=contactList.getCompName().toUpperCase(); 
                                                                }
                                                                catch(Exception e) {
                                                                }
                                                            }
                                                             else {
                                                                supplierName="-";
                                                             }
                                                            %>
                                                        <%
                                                        
                                                        %>
                                                        <td>
                                                            <%=supplierName%> 
                                                            
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td><strong><%=textTitleHeader[SESS_LANGUAGE][4]%></strong></td>
                                                        <td><strong>:</strong></td>
                                                        <%
                                                         if (srcMaterialRepostingStock.getCategoryId()>  0) {
                                                                try {
                                                                 Category category = new Category();
                                                                    try {
                                                                        category = PstCategory.fetchExc(srcMaterialRepostingStock.getCategoryId());
                                                                    }
                                                                    catch(Exception exx) {
                                                                        System.out.println("Exc when get kategori"+ exx);
                                                                    }
                                                                    
                                                                   categoryName=category.getName(); 
                                                                }
                                                                catch(Exception e) {
                                                                }
                                                            }
                                                             else {
                                                                categoryName="-";
                                                             }
                                                        %>
                                                        <%
                                                        
                                                        %>
                                                        <td>
                                                           <%=categoryName.toUpperCase()%>  
                                                        </td>
                                                    </tr>
                                                    <tr></tr>
                                                    <tr></tr>
                                                   <%
                                                          RepostingProgress repostingProgress = SessReposting.getRepostingProgress(srcMaterialRepostingStock.getLocationId());
                                                            double persentase =0;
                                                            //int a,b;
                                                            double a,b;

                                                            if (repostingProgress.getSumMatRePosting() != 0){
                                                                a= Double.parseDouble(repostingProgress.getSumMatRePostingDone()+ "D");
                                                                b= Double.parseDouble(repostingProgress.getSumMatRePosting()+ "D");
                                                                    persentase=(a/b) * 100;
                                                            }
                                                            else{
                                                                    persentase = 0;
                                                                    repostingProgress.setSumMatRePosting(repostingProgress.getSumMatRePostingDone()*300);
                                                            } 
                                                        %>
                                                    
                                                        <tr>
                                                            <table width="70%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr align="left" valign="top" colspan ="2">
                                                                    <td>Note Status Reposting : <%= SessReposting.getStatusText(srcMaterialRepostingStock.getLocationId())%>&nbsp;&nbsp;
                                                                    </td>     
                                                                </tr>
                                                                <tr align="left" valign="top" colspan ="2">
                                                                        <%//=repostingProgress.getNoteSumMatReposting()%>
                                                                </tr>
                                                                
                                                                <tr>
                                                                     <td><strong>Reposting Progress : <%=repostingProgress.getSumMatRePostingDone()%> dari <%=repostingProgress.getSumMatRePosting()%> Material</strong>
                                                                     </td>   
                                                                </tr>
                                                                 <tr>
                                                                     <td>
                                                                    <% int repostingProgressIn=1;
                                                                    if(repostingProgress.getSumMatRePosting()!=0){
                                                                            
                                                                     }%>
                                                                    <img alt=""  src="../../images/loading.gif" height="20" bg ="#0000FF" width="<%=(repostingProgress.getSumMatRePosting()==0?"":(""+repostingProgress.getSumMatRePostingDone()*300/repostingProgress.getSumMatRePosting())) %>" > <%=(repostingProgress.getSumMatRePostingDone()==0?"":(""+repostingProgress.getSumMatRePostingDone()*100/repostingProgress.getSumMatRePosting())) %>&nbsp; % 
                                                                        <BR>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td><strong> <%=repostingProgress.getNoteUpdateStock()%></strong>
                                                                     </td>
                                                                </tr>
                                                           </table>
                                                          </tr>
                                                          <tr>
                                                              <td>
                                                                  List Reposting Failed :
                                                              </td>
                                                              
                                                          </tr>
                                                                      <% 
                                                                    Vector listGagal = new Vector(1,1);
                                                                    listGagal = repostingProgress.getMatReposting();
                                                                    if( listGagal!=null && listGagal.size()>0){
                                                                        
                                                                    }
                                                                    
                                                                    %>
                                                                    
                                                                    
                                                                    <tr align="left" valign="top">
                                                                        <td height="22" valign="middle" colspan="3"> <%= drawList(request, SESS_LANGUAGE, listGagal, oidDbConn, approot, startPosting, recordToGet)%> </td>
                                                                    </tr>
                                                       
                                                        
                                                        <tr>
                                                            
                                                            <td> 
                                                                
                                                            </td>
                                                        </tr>
                                                      

                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%//= drawList(request, SESS_LANGUAGE, listLocation, oidDbConn, approot, startPosting, recordToGet)%> </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="3" class="command">
                                                            <span class="command">
                                                                <%
                                                                            int cmd = 0;
                                                                            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                                                                                cmd = iCommand;
                                                                            } else {
                                                                                if (iCommand == Command.REFRESH || prevCommand == Command.REFRESH) {
                                                                                    cmd = Command.FIRST;
                                                                                } else {
                                                                                    cmd = prevCommand;
                                                                                }
                                                                            }
                                                                            ctrLine.setLocationImg(approot + "/images");
                                                                            ctrLine.initDefault();
                                                                           
                                                                %>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                    <%
                                                                if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST
                                                                        || iCommand == Command.NONE
                                                                        || iCommand == Command.REFRESH
                                                                        || iCommand == Command.BACK
                                                                        || iCommand == Command.START
                                                                        || iCommand == Command.STOP
                                                                        || iCommand == Command.LOCK
                                                                        ||  iCommand == Command.UNLOCK
                                                                        || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {
                                                    %>
                                                    <tr align="left" valign="top">
                                                        <td>
                                                            <%if (((iCommand != Command.ADD) && (iCommand != Command.EDIT) && (iCommand != Command.ASK)) || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {%>
                                                            <table cellpadding="0" cellspacing="0" border="0">
                                                                <tr>
                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="4"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="4" valign="middle" colspan="3" width="172"><a href="javascript:cmdStart()" class="command">Execute</a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="4"><a href="javascript:cmdStop()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBackOn.jpg" width="24" height="24" alt="stop"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="4" valign="middle" colspan="3" width="172"><a href="javascript:cmdStop()" class="command">Stop</a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="24"><a href="javascript:cmdPause()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Pause"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdPause()" class="command">Pause </a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="24"><a href="javascript:cmdResume()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Resume"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdResume()" class="command">Resume </a></td>
                                                                </tr>
                                                            </table>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>

                                        <tr align="left" valign="top">
                                            <td height="8" valign="middle" colspan="3">

                                                <%
                                                            if ((iCommand == Command.ADD)
                                                                    || (iCommand == Command.EDIT)
                                                                    || (iCommand == Command.ASK)
                                                                    || ((iCommand == Command.SAVE) && iErrCode > 0) || (iCommand == Command.DELETE && iErrCode > 0)) {
                                                %>
                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                    <tr>
                                                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editorn " + locationTitle : locationTitle + " Editor"%></u></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="100%" width="100%" colspan="2">

                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top" >
                                                        <td colspan="2" class="command">
                                                            <%
                                                                       ctrLine.setLocationImg(approot + "/images");
                                                                       // set image alternative caption
                                                                       ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_SAVE, true));
                                                                       ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) + " List");
                                                                       ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ASK, true));
                                                                       ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_CANCEL, false));
                                                                       ctrLine.initDefault();
                                                                       ctrLine.setTableWidth("100%");
                                                                       String scomDel = "javascript:cmdAsk('" + oidDbConn + "')";
                                                                       String sconDelCom = "javascript:cmdConfirmDelete('" + oidDbConn + "')";
                                                                       String scancel = "javascript:cmdEdit('" + oidDbConn + "')";
                                                                       ctrLine.setCommandStyle("command");
                                                                       ctrLine.setColCommStyle("command");
                                                                       // set command caption
                                                                      ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_SAVE, true));
                                                                      ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_BACK, true) + " List");
                                                                      ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ASK, true));
                                                                      ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_DELETE, true));
                                                                      ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_CANCEL, false));


                                                                      if (privDelete) {
                                                                          ctrLine.setConfirmDelCommand(sconDelCom);
                                                                          ctrLine.setDeleteCommand(scomDel);
                                                                          ctrLine.setEditCommand(scancel);
                                                                      } else {
                                                                          ctrLine.setConfirmDelCaption("");
                                                                          ctrLine.setDeleteCaption("");
                                                                          ctrLine.setEditCaption("");
                                                                      }

                                                                      if (privAdd == false && privUpdate == false) {
                                                                          ctrLine.setSaveCaption("");
                                                                      }

                                                                      if (privAdd == false) {
                                                                          ctrLine.setAddCaption("");
                                                                      }
                                                            %>
                                                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                                                    </tr>
                                                </table>
                                                <%}%>

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
                     <%if(menuUsed == MENU_ICON){%>
                        <%@include file="../../styletemplate/footer.jsp" %>
                    <%}else{%>
                        <%@ include file = "../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable -->
                </td>
            </tr>
        </table>  
                    
      <script language="JavaScript">
// CREDITS:
 // Automatic Page Refresher by Peter Gehrig and Urs Dudli www.24fun.com
 // Permission given to use the script provided that this notice remains as is.
 // Additional scripts can be found at http://www.hypergurl.com
// Configure refresh interval (in seconds)
var refreshinterval=5
// Shall the coundown be displayed inside your status bar? Say "yes"  or "no" below: var displaycountdown="yes"
// Do not edit the code below

var starttime;
var nowtime;
var reloadseconds=0;
var secondssinceloaded=0;
function starttime() {
starttime=new Date() ;
starttime=starttime.getTime() ;
countdown() ;
}

function countdown() {
nowtime= new Date() ;
nowtime=nowtime.getTime() ;
secondssinceloaded=(nowtime-starttime)/1000 ;
reloadseconds=Math.round(refreshinterval-secondssinceloaded) ;
  if (refreshinterval>=secondssinceloaded) {
       var timer=setTimeout("countdown()",1000) ;
       if (displaycountdown=="yes") {
       window.status="Page refreshing in "+reloadseconds+ " seconds" ;
         }
    } else {
      clearTimeout(timer);
      refreshPostingProgress();//window.location.reload(true) ;
     }
}
window.onload=starttime ;
</script>
    </body>
    <!-- #EndTemplate --></html>
