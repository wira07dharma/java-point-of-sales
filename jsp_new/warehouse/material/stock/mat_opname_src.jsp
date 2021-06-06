<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatStockOpname"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatStockOpname"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.entity.I_PstDocType,
         com.dimata.posbo.entity.search.SrcMatStockOpname,
         com.dimata.posbo.session.warehouse.SessMatStockOpname,
         com.dimata.posbo.form.search.FrmSrcMatStockOpname,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.util.Command,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.entity.masterdata.PstCategory,
         com.dimata.posbo.entity.masterdata.Category,
         com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public static final String textListHeader[][] = {
        {"Lokasi", "Supplier", "Kategori", "Sub Kategori", "Tanggal", "Semua Tanggal", "Dari tanggal", " s/d ", //7
            "Urut Berdasarkan", "Status Document", "Semua Lokasi", "Semua Kategori", "Status", "Item", "Nomor"},//14
        {"Location", "Supplier", "Category", "Sub Category", "Date", "All Date", "From", "To",
            "Sort by", "Document Status", "All Location", "All Category", "Status", "Item", "Nomor"}
    };

    public static final String textListGlobal[][] = {
        {"Stok", "Opname", "Pencarian", "Daftar", "Edit", "Tidak ada data opname"},
        {"Stock", "Opname", "Search", "List", "Edit", "No opname data available"}
    };

    public static final String textListHeaders[][] = {
        {"No", "Nomor", "Tanggal", "Lokasi", "Status", "Keterangan", "Status Item", "Urut Berdasar"},
        {"No", "Number", "Date", "Location", "Status", "Remark", "Status Item", "Sort By"}
    };

    public static final String textListSortBy[][] = {
        {"-", "Nomor", "Tanggal", "Lokasi", "Status", "Status Item"},
        {"-", "Number", "Date", "Location", "Status", "Status Item"}
    };

    public String drawList(Vector objectClass, int start, int language, int docType, I_DocStatus i_status, int iStatusItem, int typeOfBusinessDetail) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListHeaders[language][0], "3%");
            ctrlist.addHeader(textListHeaders[language][1], "15%");
            ctrlist.addHeader(textListHeaders[language][2], "10%");
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Tipe Item Opname", "10%");
            }
            ctrlist.addHeader(textListHeaders[language][3], "25%");
            if (typeOfBusinessDetail == 2) {
                ctrlist.addHeader("Etalase", "10%");
            }
            ctrlist.addHeader(textListHeaders[language][4], "7%");
            ctrlist.addHeader(textListHeaders[language][6], "7%");
            ctrlist.addHeader(textListHeaders[language][5], "40%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();

            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                Vector rowx = new Vector();
                start = start + 1;
                MatStockOpname matStockOpname = (MatStockOpname) vt.get(0);
                Location location = (Location) vt.get(1);

                int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                int getCountSameMaterial = SessMatStockOpname.getCountSameMaterialList(matStockOpname.getOID());

                rowx.add("" + start);
                String str_dt_OpnameDate = "";
                Ksg ksg = new Ksg();
                try {
                    Date dt_OpnameDate = matStockOpname.getStockOpnameDate();
                    if (dt_OpnameDate == null) {
                        dt_OpnameDate = new Date();
                    }
                    str_dt_OpnameDate = Formater.formatDate(dt_OpnameDate, "dd-MM-yyyy");
                    ksg = PstKsg.fetchExc(matStockOpname.getEtalaseId());
                } catch (Exception e) {
                    str_dt_OpnameDate = "";
                }

                rowx.add(matStockOpname.getStockOpnameNumber());
                rowx.add(str_dt_OpnameDate);
                if (typeOfBusinessDetail == 2) {
                    if (matStockOpname.getOpnameItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {                        
                        rowx.add(""+Material.MATERIAL_TYPE_TITLE[matStockOpname.getOpnameItemType()]);
                    } else {
                        rowx.add("Emas / Berlian");
                    }                    
                }
                rowx.add(location.getName());
                if (typeOfBusinessDetail == 2) {
                    rowx.add(""+ksg.getCode());
                }
                rowx.add(i_status.getDocStatusName(docType, matStockOpname.getStockOpnameStatus()));

                        //status opname item
                //int getCounter = SessMatStockOpname.getCountCounterList(matStockOpname.getOID());
                if (getCounter > 1 || getCountSameMaterial > 1) {
                    rowx.add("Double");
                } else {
                    rowx.add("Clean");
                }

                rowx.add(matStockOpname.getRemark());

                lstData.add(rowx);

                lstLinkData.add(String.valueOf(matStockOpname.getOID()));
            }

            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListGlobal[language][5] + "</div>";
        }
        return result;
    }

    public boolean getTruedFalse(Vector vect, int index) {
        for (int i = 0; i < vect.size(); i++) {
            int iStatus = Integer.parseInt((String) vect.get(i));
            if (iStatus == index) {
                return true;
            }
        }
        return false;
    }

%>

<!-- Jsp Block -->
<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
//int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_OPN);
//implement status
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_LMRR);
%>

<%
    String opnameCode = i_pstDocType.getDocCode(docType);
    String opnameTitle = "Opname"; //i_pstDocType.getDocTitle(docType);
    String opnameTitleHarian = "Opname Harian"; //i_pstDocType.getDocTitle(docType);

    ControlLine ctrLine = new ControlLine();
    CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
    long oidMatStockOpname = FRMQueryString.requestLong(request, "hidden_mat_stock_opname_id");
    int iStatusItem = FRMQueryString.requestInt(request, "status_item");
    
    //added by dewok 2018 for emas lantakan
    int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");

    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 20;
    int vectSize = 0;
    String whereClause = "";

    SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
    FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname(request, srcMatStockOpname);

    int oidNumber = FRMQueryString.requestInt(request, frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_PRMNUMBER]);
    int oidDate = FRMQueryString.requestInt(request, frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS_DATE]);
    long oidLoc = FRMQueryString.requestLong(request, frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_LOCATION_ID]);
    int sortby = FRMQueryString.requestInt(request, frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_SORTBY]);

//long oidLocFrom = FRMQueryString.requestLong(request, frmSrcReportDispatch.fieldNames[frmSrcReportDispatch.FRM_FIELD_DISPATCH_TO]);
//Vector oidCost = FRMQueryString.requestLongs(request, frmSrcReportDispatch.fieldNames[frmSrcReportDispatch.FRM_FIELD_COSTING_ID]);  
    if (oidDate == 1 || oidLoc != 0 || oidNumber != 0 || sortby != 0 || lantakan == 1) {
        frmSrcMatStockOpname.requestEntityObject(srcMatStockOpname);
    }
    Vector vectSt = new Vector(1, 1);
    String[] strStatus = request.getParameterValues(FrmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]);
    if (strStatus != null && strStatus.length > 0) {
        for (int i = 0; i < strStatus.length; i++) {
            try {
                vectSt.add(strStatus[i]);
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
    }
    srcMatStockOpname.setDocStatus(vectSt);

    SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();
    session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatStockOpname);

    try {
        Vector listreq = (Vector) session.getValue("BARCODE_RECEIVE");
        if (listreq != null && listreq.size() > 0) {
            String strcode = "";
            for (int k = 0; k < listreq.size(); k++) {
                MatStockOpname matStockOpname = (MatStockOpname) listreq.get(k);
                if (strcode.length() > 0) {
                    strcode = strcode + "," + matStockOpname.getStockOpnameNumber();
                } else {
                    strcode = matStockOpname.getStockOpnameNumber();
                }
            }
            srcMatStockOpname.setOpnameNumber(strcode);
        }
    } catch (Exception e) {
    }

    vectSize = sessMatStockOpname.getCountSearch(srcMatStockOpname);
    ctrlMatStockOpname.action(iCommand, oidMatStockOpname);
    if ((iCommand == Command.FIRST) || (iCommand == Command.NEXT) || (iCommand == Command.PREV) || (iCommand == Command.LAST) || (iCommand == Command.LIST)) {
        start = ctrlMatStockOpname.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector records = sessMatStockOpname.searchMatStockOpname(srcMatStockOpname, start, recordToGet);

    try {
        session.removeValue("SESSION_OPNAME_SELECTED_ITEM");
    } catch (Exception e) {
    }
//String opnameCode = i_pstDocType.getDocCode(docType);
/*
     try{
     srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
     }
     catch(Exception e){
     srcMatStockOpname = new SrcMatStockOpname();
     //srcMatStockOpname.setStatus(-1);
     }

     if(srcMatStockOpname == null){
     srcMatStockOpname = new SrcMatStockOpname();
     //srcMatStockOpname.setStatus(-1);
     }

     try {
     session.removeValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
     }
     catch(Exception e){
     }
     */
    /**
     * get location list
     */
    Vector locationid_value = new Vector(1, 1);
    Vector locationid_key = new Vector(1, 1);
//String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
//Vector vectLocation = PstLocation.list(0,0,whereClause,PstLocation.fieldNames[PstLocation.FLD_CODE]);
//add opie-eyek
    //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
    whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
            + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

    whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

    Vector vectLocation = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
    locationid_value.add("0");
    locationid_key.add(textListHeader[SESS_LANGUAGE][10]);
    if (vectLocation != null && vectLocation.size() > 0) {
        for (int b = 0; b < vectLocation.size(); b++) {
            Location location = (Location) vectLocation.get(b);
            locationid_value.add("" + location.getOID());
            locationid_key.add(location.getName());
        }
    }
    String selectValue = "" + srcMatStockOpname.getLocationId();

    /**
     * get category list
     */
    /*Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
     Vector vectGroupVal = new Vector(1,1);
     Vector vectGroupKey = new Vector(1,1);
     vectGroupVal.add(textListHeader[SESS_LANGUAGE][11]);
     vectGroupKey.add("0");																	  	
     if(materGroup!=null && materGroup.size()>0) {
     for(int i=0; i<materGroup.size(); i++) {
     Category mGroup = (Category)materGroup.get(i);
     vectGroupVal.add(mGroup.getName());
     vectGroupKey.add(""+mGroup.getOID());																	  	
     }
     }*/
//MENGECEK SISTEM PROPERTI, APAKAH TEMPLATE UNTUK MELAKUKAN PENERIMAAN, BERBASIS BOOTSTRAP ATAU TIDAK
    int prochainMenuBootstrap = Integer.parseInt(PstSystemProperty.getValueByName("PROCHAIN_MENU_BOOTSTRAP"));

    Vector sortby_value = new Vector(1, 1);
    Vector sortby_key = new Vector(1, 1);

    for (int i = 0; i < 5; i++) {
        sortby_value.add("" + i);
        sortby_key.add("" + textListSortBy[SESS_LANGUAGE][i] + "");
    }

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            //<!--
        function cmdAdd() {
                document.frmsrcmatstockopname.command.value = "<%=Command.ADD%>";
            <%if (prochainMenuBootstrap == 0) {%>
                document.frmsrcmatstockopname.action = "mat_opname_edit_new.jsp";
            <%} else {%>
                document.frmsrcmatstockopname.action = "mat_opname_edit.jsp";
            <%}%>
                document.frmsrcmatstockopname.approval_command.value = "<%=Command.SAVE%>";
                if (compareDateForAdd() == true)
                    document.frmsrcmatstockopname.submit();
            }

            function cmdAddHarian() {
                document.frmsrcmatstockopname.command.value = "<%=Command.ADD%>";
                document.frmsrcmatstockopname.action = "mat_opname_edit_new.jsp?typeharian=1";
                document.frmsrcmatstockopname.approval_command.value = "<%=Command.SAVE%>";
                if (compareDateForAdd() == true)
                    document.frmsrcmatstockopname.submit();
            }

            function cmdSearch() {
                document.frmsrcmatstockopname.command.value = "<%=Command.LIST%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }




            function cmdEdit(oid) {
                document.frmsrcmatstockopname.command.value = "<%=Command.EDIT%>";
                document.frmsrcmatstockopname.hidden_opname_id.value = oid;
                document.frmsrcmatstockopname.approval_command.value = "<%=Command.APPROVE%>";
            <%if (prochainMenuBootstrap == 0) {%>
                document.frmsrcmatstockopname.action = "mat_opname_edit_new.jsp";
            <%} else {%>
                document.frmsrcmatstockopname.action = "mat_opname_edit.jsp";
            <%}%>
                document.frmsrcmatstockopname.submit();
            }

            function cmdListFirst() {
                document.frmsrcmatstockopname.command.value = "<%=Command.FIRST%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }

            function cmdListPrev() {
                document.frmsrcmatstockopname.command.value = "<%=Command.PREV%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }

            function cmdListNext() {
                document.frmsrcmatstockopname.command.value = "<%=Command.NEXT%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }

            function cmdListLast() {
                document.frmsrcmatstockopname.command.value = "<%=Command.LAST%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }

            function cmdBack() {
                document.frmsrcmatstockopname.command.value = "<%=Command.BACK%>";
                document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                document.frmsrcmatstockopname.submit();
            }


            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                    x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d = document;
                if (d.images) {
                    if (!d.MM_p)
                        d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                    for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
                }
            }

            function MM_findObj(n, d) { //v4.0
                var p, i, x;
                if (!d)
                    d = document;
                if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                    d = parent.frames[n.substring(p + 1)].document;
                    n = n.substring(0, p);
                }
                if (!(x = d[n]) && d.all)
                    x = d.all[n];
                for (i = 0; !x && i < d.forms.length; i++)
                    x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++)
                    x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
            }

            function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments;
                document.MM_sr = new Array;
                for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
            }
            
            //added by dewok for get etalase jewelry
            function getEtalase() {
                if ("<%=typeOfBusinessDetail%>" == 2) {
                    //document.frmsrcmatstockopname.command.value = "<%=Command.NONE%>";
                    document.frmsrcmatstockopname.action = "mat_opname_src.jsp";
                    document.frmsrcmatstockopname.submit();
                }
            }
            //-->
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
            function hideObjectForMarketing() {
            }

            function hideObjectForWarehouse() {
            }

            function hideObjectForProduction() {
            }

            function hideObjectForPurchasing() {
            }

            function hideObjectForAccounting() {
            }

            function hideObjectForHRD() {
            }

            function hideObjectForGallery() {
            }

            function hideObjectForMasterData() {
            }

        </SCRIPT>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>
                                <%
                                    if(typeOfBusinessDetail == 2) {
                                        if (lantakan == 1) {
                                            out.print(" : Emas Lantakan");
                                        }else{
                                            out.print(" : Emas & Berlian");
                                        }
                                    }
                                %>
                                <!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmsrcmatstockopname" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="approval_command" value="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="hidden_opname_id" value="<%=oidMatStockOpname%>">
                                    <input type="hidden" name="status_item" value="<%=iStatusItem%>">
                                    <!--<input type="hidden" name="<%//=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]%>" value="-1">-->
                                    <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">
                                    <%
                                        if (typeOfBusinessDetail == 2) {
                                            if (lantakan == 1) {
                                            %>
                                                <input type="hidden" name="<%=frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_OPNAME_ITEM_TYPE]%>" value="<%=Material.MATERIAL_TYPE_EMAS_LANTAKAN%>">
                                            <%
                                            }
                                        }
                                    %>                                    
                                    
                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                        <tr> 
                                            <td colspan="3" align="left" class="title"><hr size="1"></td>
                                        </tr>
                                        <tr>
                                            <td height="21" width="9%"><%=textListHeader[SESS_LANGUAGE][14]%> </td>
                                            <td height="21" width="1%">:</td>
                                            <td height="21" width="90%">
                                                <input type="text" name="<%=frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_PRMNUMBER]%>"  value="<%= srcMatStockOpname.getOpnameNumber()%>" class="formElemen" size="20">
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][0]%> </td>
                                            <td height="21" width="1%" valign="" align="left">:</td>
                                            <td height="21" width="88%" valign="" align="left">
                                                <%= ControlCombo.draw(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_LOCATION_ID], null, selectValue, locationid_value, locationid_key, "onChange=javascript:getEtalase()", "formElemen")%>
                                            </td>
                                        </tr>
                                        <%if(typeOfBusinessDetail == 2){%>
                                        <tr>
                                            <td>Etalase</td>
                                            <td>:</td>
                                            <%
                                                Vector val_etalase = new Vector(1,1);
                                                Vector key_etalase = new Vector(1,1);
                                                Vector listEtalase = PstKsg.list(0, 0, ""+PstKsg.fieldNames[PstKsg.FLD_LOCATION_ID]+"='"+selectValue+"'", ""+PstKsg.fieldNames[PstKsg.FLD_CODE]);
                                                for (int i=0; i<listEtalase.size(); i++) {
                                                    Ksg ksg = (Ksg) listEtalase.get(i);
                                                    Location loc = PstLocation.fetchExc(ksg.getLocationId());
                                                    val_etalase.add(""+ksg.getOID());
                                                    key_etalase.add(""+loc.getCode()+" - "+ksg.getCode());
                                                }
                                            %>
                                            <td>
                                                <%=ControlCombo.draw(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_ETALASE_ID], null, ""+srcMatStockOpname.getEtalaseId(), val_etalase, key_etalase, "", "formElemen")%>
                                            </td>
                                        </tr>
                                        <%}%>
                                        <tr> 
                                            <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][2]%> </td>
                                            <td height="21" width="1%" valign="" align="left">:</td>
                                            <td height="21" width="88%" valign="" align="left"> 

                                                <%--<%=(ControlCombo.draw(frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcMatStockOpname.getLocationId(), vectGroupKey, vectGroupVal, null))%>
                                                --%>
                                                <select  name="txt_materialgroup" class="formElemen">
                                                    <option value="-1">Semua Category</option>
                                                    <%
                                                        Vector masterCatAcak = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
                                                     //Category newCategory = new Category();
                                                        //add opie-eyek 20130821
                                                        String checked = "selected";
                                                        Vector materGroup = PstCategory.structureList(masterCatAcak);
                                                        Vector vectGroupVal = new Vector(1, 1);
                                                        Vector vectGroupKey = new Vector(1, 1);
                                                        if (materGroup != null && materGroup.size() > 0) {
                                                            String parent = "";
                                                            // Vector<Category> resultTotal= new Vector();
                                                            Vector<Long> levelParent = new Vector<Long>();
                                                            for (int i = 0; i < materGroup.size(); i++) {
                                                                Category mGroup = (Category) materGroup.get(i);
                                                                if (mGroup.getCatParentId() != 0) {
                                                                    for (int lv = levelParent.size() - 1; lv > -1; lv--) {
                                                                        long oidLevel = levelParent.get(lv);
                                                                        if (oidLevel == mGroup.getCatParentId()) {
                                                                            break;
                                                                        } else {
                                                                            levelParent.remove(lv);
                                                                        }
                                                                    }
                                                                    parent = "";
                                                                    for (int lv = 0; lv < levelParent.size(); lv++) {
                                                                        parent = parent + "&nbsp;&nbsp;";
                                                                    }
                                                                    levelParent.add(mGroup.getOID());

                                                                } else {
                                                                    levelParent.removeAllElements();
                                                                    levelParent.add(mGroup.getOID());
                                                                    parent = "";
                                                                }
                                                    %>
                                                    <option value="<%=mGroup.getOID()%>" <%=mGroup.getOID() == srcMatStockOpname.getLocationId() ? checked : ""%> ><%=parent%><%=mGroup.getName()%></option>
                                                    <%
                                                            }
                                                        } else {
                                                            vectGroupVal.add("Tidak Ada Category");
                                                            vectGroupKey.add("-1");
                                                        }
                                                    %>
                                                </select>
                                            </td>
                                        </tr>


                                        <tr> 
                                            <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][4]%> </td>
                                            <td height="21" width="1%" valign="" align="left">:</td>
                                            <td height="21" width="88%" valign="" align="left"> 
                                                <input type="radio" class="formElemen" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS_DATE]%>" <%if (srcMatStockOpname.getStatusDate() == 0) {%>checked<%}%> value="0">
                                                <%=textListHeader[SESS_LANGUAGE][5]%></td>
                                        </tr>
                                        <tr> 
                                            <td height="21" width="11%"> 
                                                <div align="right"></div>
                                            </td>
                                            <td height="21" width="1%" valign="" align="left">&nbsp;</td>
                                            <td height="21" width="88%" valign="" align="left"> 
                                                <input type="radio" class="formElemen" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS_DATE]%>" <%if (srcMatStockOpname.getStatusDate() == 1) {%>checked<%}%> value="1">
                                                <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_FROM_DATE], srcMatStockOpname.getFromDate(), 1, -5, "formElemen", "")%> 
                                                <%=textListHeader[SESS_LANGUAGE][7]%> <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_TO_DATE], srcMatStockOpname.getToDate(), 1, -5, "formElemen", "")%> </td>
                                        </tr>

                                        <!-- OpnameStatus -->
                                        <tr>
                                            <td height="21" valign="" width="11%" align="left"><%=textListHeader[SESS_LANGUAGE][12]%></td>
                                            <td height="21" valign="" width="1%" align="left">:</td>
                                            <td height="21" width="88%" valign="" align="left">
                                                <%
                                                    Vector vectResult = i_status.getDocStatusFor(docType);
                                                    for (int i = 0; i < vectResult.size(); i++) {
                                                        if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL)) {
                                                            Vector vetTemp = (Vector) vectResult.get(i);
                                                            int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                                            String strPrStatus = String.valueOf(vetTemp.get(1));
                                                %>
                                                <input type="checkbox" class="formElemen" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatStockOpname.getDocStatus(), indexPrStatus)) {%>checked<%}%> onKeyDown="javascript:fnTrapKD()">
                                                <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                <%
                                                        }
                                                    }
                                                %>
                                            </td>
                                        </tr>
                                        <!-- end of status -->
                                        <!-- Sort By-->
                                        <tr> 
                                            <td height="21" width="11%"> <%=textListHeaders[SESS_LANGUAGE][7]%> </td>
                                            <td height="21" width="1%" valign="" align="left">:</td>
                                            <td height="21" width="88%" valign="" align="left">                      
                                                <%= ControlCombo.draw(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_SORTBY], null, "" + srcMatStockOpname.getSortBy(), sortby_value, sortby_key, "", "formElemen")%>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td height="21" valign="top" width="11%" align="left">&nbsp;</td>
                                            <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                                            <td height="21" width="88%" valign="top" align="left">&nbsp;</td>
                                        </tr>
                                        <tr> 
                                            <td height="21" valign="top" width="11%" align="left">&nbsp;</td>
                                            <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                                            <td height="21" width="88%" valign="top" align="left"> 
                                                <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr> 
                                                        <!--td nowrap width="5%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101', '', '<%=approot%>/images/BtnSearchOn.jpg', 1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_SEARCH, true)%>"></a></td-->
                                                        <td class="command" nowrap width=""><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_SEARCH, true)%></a></td>
                                                            <%
                                                                if (privAdd) {
                                                            %>
                                                        <!--td nowrap width="5%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_ADD, true)%>"></a></td-->
                                                        <td class="command" nowrap width=""><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                        <!--td nowrap width="5%"><a href="javascript:cmdAddHarian()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitleHarian, ctrLine.CMD_ADD, true)%>"></a></td-->
                                                        <td class="command" nowrap width=""><a class="btn btn-primary" href="javascript:cmdAddHarian()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitleHarian, ctrLine.CMD_ADD, true)%></a></td>
                                                            <%
                                                                }
                                                            %>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>	   
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr> 
                                            <td> 
                                                <hr size="1" noshade>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td><%=drawList(records, start, SESS_LANGUAGE, docType, i_status, iStatusItem, typeOfBusinessDetail)%></td>
                                        </tr>
                                        <%
                                            ctrLine.setLocationImg(approot + "/images");
                                            ctrLine.initDefault();
                                            String strList = ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet);
                                            if (strList.length() > 0) {
                                        %>				
                                        <tr> 
                                            <td><%=strList%></td>
                                        </tr>
                                        <%}%>
                                        <tr> 				
                                            <td> 
                                                <table width="52%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr> 
                                                        <%//if(privAdd && privManageData){%>
                                                        <!--td nowrap width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_ADD, true)%>"></a></td-->
                                                        <td class="command" nowrap width="48%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, opnameTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                            <%//}%>
                                                        <td nowrap width="6%"></td>
                                                        <td class="command" nowrap width="40%"></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <%}%>
                                    <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                        <tr>
                                            <td colspan = "2">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan = "2">&nbsp;</td>
                                        </tr>
                                        <tr>
                                            <td colspan = "2" ><b>Aturan Opname dan Posting :</b></td>
                                        </tr>
                                        <tr>
                                            <td>1.</td>
                                            <td>Semua dokumen transaksi stock (penerimaan, sale, dsb.) harus sudah diposting sebelum tanggal opname</td>
                                        </tr>
                                        <tr>
                                            <td>2.</td>
                                            <td>Sebelum hari opname, Tanggal dan jam opname harus didaftarkan di sistem prochain dengan membuat 
                                                dokumen opname  status draft dengan mengisi dengan lengkap data-data dokumen opname tanpa opname item.
                                                <br>Tujuannya : untuk menghindari dokumen transaksi setelah tgl dan jam opname bisa diposting sebelum 
                                                dokumen opname real dientry ke sistem.</td>
                                        </tr>
                                        <tr>
                                            <td>3.</td>
                                            <td>Dokumen opname yang tidak dipakai harus dihapus dari sistem.</td>
                                        </tr>
                                        <tr>
                                            <td>4.</td>
                                            <td>Setelah dokumen opname dientry dan diposting maka dokumen transaksi lainnya baru bisa diposting.
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
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
