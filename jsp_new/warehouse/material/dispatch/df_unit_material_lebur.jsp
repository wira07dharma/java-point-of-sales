<%-- 
    Document   : df_unit_material_lebur
    Created on : Jan 17, 2018, 11:42:36 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ page import= "com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.session.warehouse.SessMatDispatch,
         com.dimata.posbo.entity.search.SrcMatDispatch,
         com.dimata.posbo.form.warehouse.FrmMatDispatch,
         com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Produk Dilebur", "Pencarian"},
        {"Dispatch", "Searching"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Urut Berdasar", "Semua", "Dari", " s/d ", "Lokasi"},
        {"Number", "From Location", "To Location", "Date", "Status", "Sort By", "All", "From", " to ", "Location"}
    };

    public static final String textListGlobals[][] = {
        {"Produk Dilebur", "Daftar"},
        {"Dispatch", "List"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"No", "Nomor", "Tanggal", "Lokasi Asal", "Lokasi Tujuan", "Status","Keterangan"},
        {"No", "Number", "Date", "Dispatch From", "Dispatch To", "Status","Remark"}
    };

    public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "15%");
            ctrlist.addHeader("Tipe Item Lebur", "10%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "20%");
            ctrlist.addHeader(textListMaterialHeader[language][4], "20%");
            ctrlist.addHeader(textListMaterialHeader[language][5], "10%");
            ctrlist.addHeader(textListMaterialHeader[language][6], "10%");
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
                Vector rowx = new Vector();
                Vector vt = (Vector) objectClass.get(i);
                MatDispatch df = (MatDispatch) vt.get(0);
                Location loc1 = (Location) vt.get(1);
                Location loc2 = new Location();
                MatDispatch newDf = new MatDispatch();
                try {
                    loc2 = PstLocation.fetchExc(df.getDispatchTo());
                    newDf = PstMatDispatch.fetchExc(df.getOID());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                start += 1;
                rowx.add("" + start);
                rowx.add(df.getDispatchCode());
                if(newDf.getDispatchItemType() == Material.MATERIAL_TYPE_EMAS_LANTAKAN) {
                    rowx.add("" + Material.MATERIAL_TYPE_TITLE[newDf.getDispatchItemType()]);
                } else {
                    rowx.add("Emas / Berlian");
                }
                //rowx.add("" + Material.MATERIAL_TYPE_TITLE[newDf.getDispatchItemType()]);
                rowx.add(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"));
                rowx.add(loc1.getName());
                rowx.add(loc2.getName());
                rowx.add(i_status.getDocStatusName(docType, df.getDispatchStatus()));
                rowx.add(df.getRemark());
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(+df.getOID()));
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data pengiriman ...</div>";
        }
        return result;
    }

    public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            result = textListHeader[language][index];
        } else {
            result = textListHeader[language][index];
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

<%    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_DF);
    boolean privManageData = true;%>

<%

    String dfCode = "";
    String dfTitle = textListGlobal[SESS_LANGUAGE][0];
    String dfItemTitle = dfTitle + " Item";

    /**
     * get request data from current form
     */
    long oidMatDispatch = FRMQueryString.requestLong(request, "hidden_dispatch_id");
    long oidMatDispatchReceive = FRMQueryString.requestLong(request, "hidden_dispatch_receive_id");
    int lantakan = FRMQueryString.requestInt(request, "emas_lantakan");
    String whereAdd = "";
    if (lantakan == 1) {
        whereAdd = " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_ITEM_TYPE] + " = '" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
    } else {
        whereAdd = " AND DF." + PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_ITEM_TYPE] + " != '" + Material.MATERIAL_TYPE_EMAS_LANTAKAN + "'";
    }
    /**
     * initialitation some variable
     */
    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 20;
    int vectSize = 0;
    String whereClause = "";

    /**
     * instantiate some object used in this page
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);
    SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
    SessMatDispatch sessMatDispatch = new SessMatDispatch();
    FrmSrcMatDispatch frmSrcMatDispatch = new FrmSrcMatDispatch(request, srcMatDispatch);
    frmSrcMatDispatch.requestEntityObject(srcMatDispatch);
    session.putValue(SessMatDispatch.SESS_SRC_MATDISPATCH, srcMatDispatch);

    try {
        Vector listreq = (Vector) session.getValue("BARCODE_DISPATCH");
        if (listreq != null && listreq.size() > 0) {
            String strcode = "";
            for (int k = 0; k < listreq.size(); k++) {
                MatDispatch matDispatch = (MatDispatch) listreq.get(k);
                if (strcode.length() > 0) {
                    strcode = strcode + "," + matDispatch.getDispatchCode();
                } else {
                    strcode = matDispatch.getDispatchCode();
                }
            }
            srcMatDispatch.setDispatchCode(strcode);
            srcMatDispatch.setIgnoreDate(true);
        }
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    vectSize = sessMatDispatch.getCountMatDispatchReceive(srcMatDispatch, whereAdd);
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMatDispatch.actionList(iCommand, start, vectSize, recordToGet);
    }
    Vector records = sessMatDispatch.listMatDispatchReceive(srcMatDispatch, start, recordToGet, whereAdd);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>

        <script language="JavaScript">
            function cmdSearch()
            {
                document.frm_matdispatch.command.value = "<%=Command.LIST%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdAdd()
            {
                document.frm_matdispatch.start.value = 0;
                document.frm_matdispatch.approval_command.value = "<%=Command.SAVE%>";
                document.frm_matdispatch.command.value = "<%=Command.ADD%>";
                document.frm_matdispatch.add_type.value = "<%=ADD_TYPE_LIST%>";
                document.frm_matdispatch.action = "df_unit_material_lebur_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_matdispatch.submit();
            }

            function cmdEdit(oid)
            {
                document.frm_matdispatch.hidden_dispatch_id.value = oid;
                document.frm_matdispatch.start.value = 0;
                document.frm_matdispatch.approval_command.value = "<%=Command.APPROVE%>";
                document.frm_matdispatch.command.value = "<%=Command.EDIT%>";
                document.frm_matdispatch.action = "df_unit_material_lebur_edit.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListFirst()
            {
                document.frm_matdispatch.command.value = "<%=Command.FIRST%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListPrev()
            {
                document.frm_matdispatch.command.value = "<%=Command.PREV%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListNext()
            {
                document.frm_matdispatch.command.value = "<%=Command.NEXT%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdListLast()
            {
                document.frm_matdispatch.command.value = "<%=Command.LAST%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            function cmdBack()
            {
                document.frm_matdispatch.command.value = "<%=Command.BACK%>";
                document.frm_matdispatch.action = "df_unit_material_lebur.jsp";
                document.frm_matdispatch.submit();
            }

            //-------------- script control line -------------------
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
        </script>

        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE">
                    <%@ include file = "../../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU">
                    <%@ include file = "../../../main/mnmain.jsp" %>
                </td>
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
                            <td height="20" class="mainheader">
                                &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>
                                <%if(lantakan == 1) {out.print(" : Emas Lantakan");}else{out.print(" : Emas & Berlian");}%>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <form name="frm_matdispatch" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="add_type" value="">
                                    <input type="hidden" name="approval_command">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="hidden_dispatch_id" value="<%=oidMatDispatch%>">
                                    <input type="hidden" name="hidden_dispatch_receive_id" value="<%=oidMatDispatchReceive%>">
                                    <input type="hidden" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstMatDispatch.FLD_TYPE_TRANSFER_LEBUR%>">
                                    <input type="hidden" name="emas_lantakan" value="<%=lantakan%>">
                                    
                                    <table width="100%" border="0">
                                        <tr>
                                            <td valign="top" colspan="2">
                                                <hr size="1">
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2">
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(0, SESS_LANGUAGE, dfCode, true)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">&nbsp;
                                                            <input type="text" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_CODE]%>"  value="<%= srcMatDispatch.getDispatchCode()%>" class="formElemen" size="20">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(1, SESS_LANGUAGE, dfCode, true)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">&nbsp;
                                                            <%
                                                                Vector obj_locationid = new Vector(1, 1);
                                                                Vector val_locationid = new Vector(1, 1);
                                                                Vector key_locationid = new Vector(1, 1);
                                                                Vector vt_loc = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                                val_locationid.add("0");
                                                                key_locationid.add(getJspTitle(6, SESS_LANGUAGE, dfCode, true) + " " + getJspTitle(9, SESS_LANGUAGE, dfCode, true));
                                                                for (int d = 0; d < vt_loc.size(); d++) {
                                                                    Location loc = (Location) vt_loc.get(d);
                                                                    val_locationid.add("" + loc.getOID() + "");
                                                                    key_locationid.add(loc.getName());
                                                                }
                                                            %>
                                                            <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_FROM], null, "", val_locationid, key_locationid, "", "formElemen")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(2, SESS_LANGUAGE, dfCode, true)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">&nbsp;
                                                            <%
                                                                Vector obj_locationid1 = new Vector(1, 1);
                                                                Vector val_locationid1 = new Vector(1, 1);
                                                                Vector key_locationid1 = new Vector(1, 1);
                                                                String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                                String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                                Vector vt_loc1 = PstLocation.list(0, 0, locWhClause, locOrderBy);
                                                                val_locationid1.add("0");
                                                                key_locationid1.add(getJspTitle(6, SESS_LANGUAGE, dfCode, true) + " " + getJspTitle(9, SESS_LANGUAGE, dfCode, true));
                                                                for (int d = 0; d < vt_loc1.size(); d++) {
                                                                    Location loc1 = (Location) vt_loc1.get(d);
                                                                    val_locationid1.add("" + loc1.getOID() + "");
                                                                    key_locationid1.add(loc1.getName());
                                                                }
                                                            %>
                                                            <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_TO], null, "", val_locationid1, key_locationid1, "", "formElemen")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(3, SESS_LANGUAGE, dfCode, true)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">
                                                            <input type="radio" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="1" <%if (srcMatDispatch.getIgnoreDate()) {%>checked<%}%>>
                                                            <%=getJspTitle(6, SESS_LANGUAGE, dfCode, true)%> <%=getJspTitle(3, SESS_LANGUAGE, dfCode, true)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"></td>
                                                        <td height="21" width="1%"></td>
                                                        <td height="21" width="83%">
                                                            <input type="radio" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="0" <%if (!srcMatDispatch.getIgnoreDate()) {%>checked<%}%>>
                                                            <%=getJspTitle(7, SESS_LANGUAGE, dfCode, true)%>
                                                            <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_FROM], (srcMatDispatch.getDispatchDateFrom() == null) ? new Date() : srcMatDispatch.getDispatchDateFrom(), 1, -5, "formElemen", "")%>
                                                            <%=getJspTitle(8, SESS_LANGUAGE, dfCode, true)%>
                                                            <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_TO], (srcMatDispatch.getDispatchDateTo() == null) ? new Date() : srcMatDispatch.getDispatchDateTo(), 1, -5, "formElemen", "")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(4, SESS_LANGUAGE, dfCode, true)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">
                                                            &nbsp;
                                                            <%
                                                                Vector val_status = new Vector(1, 1); //hidden values that will be deliver on request (oids)
                                                                Vector key_status = new Vector(1, 1); //texts that displayed on combo box
                                                                val_status.add("-1");
                                                                key_status.add("All");

                                                                Vector vectResult = i_status.getDocStatusFor(docType);
                                                                for (int i = 0; i < vectResult.size(); i++) {
                                                                    if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL)) {
                                                                        Vector vetTemp = (Vector) vectResult.get(i);
                                                                        int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                                                        String strPrStatus = String.valueOf(vetTemp.get(1));

                                                                        val_status.add("" + indexPrStatus);
                                                                        key_status.add(strPrStatus);
                                                                    }
                                                                }
                                                                String select_status = (iCommand == Command.NONE) ? "-1" : "" + srcMatDispatch.getStatus(); //selected on combo box*/
                                                            %>
                                                            <%=ControlCombo.draw(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS], null, select_status, val_status, key_status, "", "formElemen")%><%=frmSrcMatDispatch.getErrorMsg(FrmSrcMatDispatch.FRM_FIELD_STATUS)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="16%"><%=getJspTitle(5, SESS_LANGUAGE, dfCode, false)%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="83%">&nbsp;
                                                            <%
                                                                Vector key_sort = new Vector(1, 1);
                                                                Vector val_sort = new Vector(1, 1);
                                                                for (int i = 0; i < textListHeader[0].length - 1; i++) {
                                                                    key_sort.add("" + i);
                                                                    val_sort.add("" + getJspTitle(i, SESS_LANGUAGE, dfCode, true));
                                                                }
                                                                String select_sort = "" + srcMatDispatch.getSortBy();
                                                                out.println(ControlCombo.draw(frmSrcMatDispatch.fieldNames[frmSrcMatDispatch.FRM_FIELD_SORT_BY], null, select_sort, key_sort, val_sort, "", "formElemen"));
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                                                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                                                        <td height="21" width="83%" valign="top" align="left">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                                                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                                                        <td height="21" width="83%" valign="top" align="left">
                                                            <table width="57%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                    <%--<td nowrap width="6%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10', '', '<%=approot%>/images/BtnSearchOn.jpg', 1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SEARCH, true)%>"></a></td>--%>
                                                                    <td class="command" nowrap width="44%"><a class="btn btn-primary" href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_SEARCH, true)%></a></td>
                                                                        <% if (privAdd) {%>
                                                                    <%--<td nowrap width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ADD, true)%>"></a></td>--%>
                                                                    <td class="command" nowrap width="43%"><a class="btn btn-primary" href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                                        <%}%>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                            </td>
                        </tr>

                        <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>     
                        <table width="100%" cellspacing="0" cellpadding="3">
                            <tr>
                                <td valign="top" colspan="2"> 
                                    <hr size="1">
                                </td>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, i_status)%></td>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="8" align="left" colspan="3" class="command">
                                    <span class="command">
                                        <%
                                            ctrLine.setLocationImg(approot + "/images");
                                            ctrLine.initDefault();
                                            out.println(ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet));
                                        %>
                                    </span>
                                </td>
                            </tr>
                            <tr align="left" valign="top">
                                <td height="18" valign="top" colspan="3">
                                    <table width="52%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <%if (privAdd) {%>
                                            <td class="command" nowrap width="42%"><a class="btn btn-primary" href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, dfTitle, ctrLine.CMD_ADD, true)%></a></td>
                                            <%}%>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>

                        <%}%>

                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20">
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>
                </td>
            </tr>
        </table>
    </body>
</html>
