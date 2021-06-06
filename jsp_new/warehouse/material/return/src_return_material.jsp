<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReturn"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%
    /* 
     * Page Name  		:  src_return_material.jsp
     * Created on 		:  Selasa, 2 Agustus 2007 3:04 PM 
     * 
     * @author  		:  gwawan
     * @version  		:  -
     */

    /**
     * *****************************************************************
     * Page Description : page ini merupakan gabungan dari page : -
     * srcreturn_wh_supp_material.jsp - srcreturn_wh_material.jsp.jsp Imput
     * Parameters : [input parameter ...] Output : [output ...] 
 ******************************************************************
     */
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Retur", "Pencarian", "Daftar", "Retur dengan Nota Penerimaan", "Retur tanpa Nota Penerimaan"},
        {"Retur", "Searching", "List", "Return with Receipt", "Return without Receipt"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Kode", "Suplier", "Lokasi", "Tanggal", "Status", "Urut Berdasar", "Semua Tanggal", "Dari", "s/d", "Semua"},
        {"Code", "Supplier", "Location", "Date", "Status", "Sort By", "All Date", "From", "to", "All"}
    };

    public static final String textListMaterialHeader[][] = {
        {"No", "Kode", "Tanggal", "Nota Penerimaan", "Suplier", "Status", "Keterangan", "Status PO", "Lokasi", "Kode Bea Cukai"},
        {"No", "Code", "Date", "Receipt", "Supplier", "Status", "Remark", "PO Status", "Location", "Customs Code"}
    };

    public static final String textOrderBy[][] = {
        {"Kode", "Suplier", "Tanggal", "Status"},
        {"Code", "Suplier", "Date", "Status"}
    };

    public static final String textStatusPO[][] = {
        {"Dengan PO", "Tanpa PO"},
        {"With PO", "Without PO"}
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
            ctrlist.addHeader(textListMaterialHeader[language][0], "3%"); // nomor
            ctrlist.addHeader(textListMaterialHeader[language][1], "15%"); // kode
            ctrlist.addHeader(textListMaterialHeader[language][2], "12%"); // tanggal
			int dutyFree = SessMatReturn.getStrDutyFree();
			if(dutyFree == 1){
				ctrlist.addHeader(textListMaterialHeader[language][8], "8%"); // lokasi
				ctrlist.addHeader(textListMaterialHeader[language][9], "8%"); // cukai
			}
            ctrlist.addHeader(textListMaterialHeader[language][3], "15%"); //nota
            ctrlist.addHeader(textListMaterialHeader[language][4], "10%"); //suplier
            ctrlist.addHeader(textListMaterialHeader[language][5], "10%"); //status
            ctrlist.addHeader(textListMaterialHeader[language][6], "15%"); //keterangan
            ctrlist.addHeader(textListMaterialHeader[language][7], "10%"); //status po
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
                MatReturn ret = (MatReturn) vt.get(0);
                ContactList contact = (ContactList) vt.get(1);
                MatReceive matReceive = (MatReceive) vt.get(2);
				
                String cntName = contact.getCompName();
                if (cntName.length() == 0) {
                    cntName = String.valueOf(contact.getPersonName() + " " + contact.getPersonLastname());
                }

                String str_dt_RetDate = "";
                try {
                    Date dt_RetDate = ret.getReturnDate();
                    if (dt_RetDate == null) {
                        dt_RetDate = new Date();
                    }
                    str_dt_RetDate = Formater.formatDate(dt_RetDate, "dd-MM-yyyy");
                } catch (Exception e) {
                    str_dt_RetDate = "";
                }

                Vector rowx = new Vector();
                rowx.add("" + (start + 1 + i));
                rowx.add(ret.getRetCode());
                rowx.add(str_dt_RetDate);
				if(dutyFree == 1){
					Location location = (Location) vt.get(3);
					rowx.add(location.getName());
					rowx.add("");
				}
                rowx.add(matReceive.getInvoiceSupplier());
                rowx.add(cntName);
                rowx.add(i_status.getDocStatusName(docType, ret.getReturnStatus()));
                rowx.add(ret.getRemark());

                MatReturn entMatReturn = new MatReturn();
                try {
                    entMatReturn = PstMatReturn.fetchExc(ret.getOID());
                } catch (Exception ex) {

                }

                String statusPO = "";
                if (entMatReturn.getPurchaseOrderId() != 0) {
                    statusPO = textStatusPO[language][0];
                } else {
                    statusPO = textStatusPO[language][1];
                }
                rowx.add(statusPO);
                lstData.add(rowx);
                lstLinkData.add(String.valueOf(ret.getOID()) + "', '" + matReceive.getOID());
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data retur ...</div>";
        }
        return result;
    }

    public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            if (language == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                result = textListHeader[language][index] + " " + prefiks;
            } else {
                result = prefiks + " " + textListHeader[language][index];
            }
        } else {
            result = textListHeader[language][index];
        }
        return result;
    }

    public String getJspTitle2(int index, int language, String prefiks, boolean addBody) {
        String result = "";
        if (addBody) {
            if (language == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {
                result = textOrderBy[language][index] + " " + prefiks;
            } else {
                result = prefiks + " " + textOrderBy[language][index];
            }
        } else {
            result = textOrderBy[language][index];
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
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_ROMR);
    boolean privManageData = true;%>


<%
    /**
     * get data from 'hidden form'
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int PurchaseID = FRMQueryString.requestInt(request, "PurchaseID");
    /**
     * declaration of some identifier
     */
    String retCode = ""; //i_pstDocType.getDocCode(docType);
    String retTitle = "Retur Ke Supplier"; //i_pstDocType.getDocTitle(docType);
    String retItemTitle = retTitle + " Item";

    /**
     * ControlLine
     */
    ControlLine ctrLine = new ControlLine();

    long oidMatReturn = FRMQueryString.requestLong(request, "hidden_return_id");

    int iErrCode = FRMMessage.ERR_NONE;
    String msgStr = "";

    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 100;
    int vectSize = 0;
    String whereClause = "";

    /**
     * instantiate some object used in this page
     */
    CtrlMatReturn ctrlMatReturn = new CtrlMatReturn(request);
    SrcMatReturn srcMatReturn = new SrcMatReturn();
    SessMatReturn sessMatReturn = new SessMatReturn();
    FrmSrcMatReturn frmSrcMatReturn = new FrmSrcMatReturn(request, srcMatReturn);
    String sOidNumber = FRMQueryString.requestString(request, frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNNUMBER]);
    int oidDate = FRMQueryString.requestInt(request, frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNDATESTATUS]);
    int oidSortBy = FRMQueryString.requestInt(request, frmSrcMatReturn.fieldNames[frmSrcMatReturn.FRM_FIELD_RETURNSORTBY]);
    long oidLocFrom = FRMQueryString.requestLong(request, FrmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_LOCATION_FROM]);
    String sVendor = FRMQueryString.requestString(request, frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_VENDORNAME]);

    if (oidDate == 1 || sOidNumber != "" || oidSortBy != 0 || sVendor != "" || oidLocFrom != 0 && iCommand != Command.FIRST) {
        frmSrcMatReturn.requestEntityObject(srcMatReturn);
    }

    Vector vectSt = new Vector(1, 1);
    String[] strStatus = request.getParameterValues(FrmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNSTATUS]);
    if (strStatus != null && strStatus.length > 0) {
        for (int i = 0; i < strStatus.length; i++) {
            try {
                vectSt.add(strStatus[i]);
            } catch (Exception exc) {
                System.out.println("err");
            }
        }
    }
    srcMatReturn.setReturnstatus(vectSt);

    if (PurchaseID == -1) {
        srcMatReturn.setPurchaseOrderId(PurchaseID);
    }
    if (PurchaseID == -2) {
        srcMatReturn.setPurchaseOrderId(PurchaseID);
    }

    String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

    vectSize = sessMatReturn.getCountSearch(srcMatReturn, whereClausex);
    if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
        start = ctrlMatReturn.actionList(iCommand, start, vectSize, recordToGet);
    }
    Vector records = sessMatReturn.searchMatReturn(srcMatReturn, start, recordToGet, whereClausex);

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdAddRcv() {
                document.frm_retmaterial.command.value = "<%=Command.ADD%>";
                document.frm_retmaterial.approval_command.value = "<%=Command.SAVE%>";
                document.frm_retmaterial.add_type.value = "<%=ADD_TYPE_SEARCH%>";
                document.frm_retmaterial.action = "return_wh_material_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_retmaterial.submit();
            }

            function cmdAdd() {
                document.frm_retmaterial.command.value = "<%=Command.ADD%>";
                document.frm_retmaterial.approval_command.value = "<%=Command.SAVE%>";
                document.frm_retmaterial.add_type.value = "<%=ADD_TYPE_SEARCH%>";
                document.frm_retmaterial.action = "return_wh_supp_material_edit.jsp";
                if (compareDateForAdd() == true)
                    document.frm_retmaterial.submit();
            }

            function cmdSearch() {
                document.frm_retmaterial.command.value = "<%=Command.LIST%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
            }




            function cmdEdit(idRtn, idRcv) {
                document.frm_retmaterial.hidden_return_id.value = idRtn;
                document.frm_retmaterial.start.value = 0;
                document.frm_retmaterial.approval_command.value = "<%=Command.APPROVE%>";
                document.frm_retmaterial.command.value = "<%=Command.EDIT%>";
                if (parseInt(idRcv) != 0) {
                    document.frm_retmaterial.action = "return_wh_material_edit.jsp";
                }
                else {
                    document.frm_retmaterial.action = "return_wh_supp_material_edit.jsp";
                }
                document.frm_retmaterial.submit();
            }

            function cmdListFirst() {
                document.frm_retmaterial.command.value = "<%=Command.FIRST%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
            }

            function cmdListPrev() {
                document.frm_retmaterial.command.value = "<%=Command.PREV%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
            }

            function cmdListNext() {
                document.frm_retmaterial.command.value = "<%=Command.NEXT%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
            }

            function cmdListLast() {
                document.frm_retmaterial.command.value = "<%=Command.LAST%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
            }

            function cmdBack() {
                document.frm_retmaterial.command.value = "<%=Command.BACK%>";
                document.frm_retmaterial.action = "src_return_material.jsp";
                document.frm_retmaterial.submit();
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
                                &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>
                                <!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_retmaterial" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="add_type" value="">			
                                    <input type="hidden" name="approval_command">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="hidden_return_id" value="<%=oidMatReturn%>">
                                    <input type="hidden" name="PurchaseID" value="<%=PurchaseID%>">		
                                    <input type="hidden" name="<%=frmSrcMatReturn.fieldNames[frmSrcMatReturn.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                                    <table width="100%" border="0">
                                        <tr>
                                            <td valign="top" colspan="2">
                                                <hr size="1">
                                            </td>
                                        </tr>
                                        <tr>

                                        </tr>
                                        <td> 
                                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                <tr> 
                                                    <td height="21" width="12%"><%=getJspTitle(0, SESS_LANGUAGE, retCode, true)%></td>
                                                    <td height="21" width="1%">:</td>
                                                    <td height="21" width="87%">&nbsp; 
                                                        <input type="text" name="<%=frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNNUMBER]%>"  value="<%= srcMatReturn.getReturnnumber()%>" class="formElemen" size="20">
                                                    </td>
                                                </tr>
                                                <tr> 
                                                    <td height="21" width="12%"><%=getJspTitle(1, SESS_LANGUAGE, retCode, true)%></td>
                                                    <td height="21" width="1%">:</td>
                                                    <td height="21" width="87%">&nbsp; 
                                                        <input type="text" name="<%=frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_VENDORNAME]%>"  value="<%= srcMatReturn.getVendorname()%>" class="formElemen" size="30">
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td valign="top" align="left"><%=getJspTitle(2, SESS_LANGUAGE, "", true)%></td>
                                                    <td valign="top" align="left">:</td>
                                                    <td valign="top" align="left">&nbsp; 
                                                        <%
                                                            Vector val_locationid = new Vector(1, 1);
                                                            Vector key_locationid = new Vector(1, 1);
                                                                                      //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                            //Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                            //add opie-eyek
                                                            //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                            whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                                    + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                                            whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                            Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                                            val_locationid.add("0");
                                                            key_locationid.add(textListHeader[SESS_LANGUAGE][9] + " " + textListHeader[SESS_LANGUAGE][2]);
                                                            for (int d = 0; d < vt_loc.size(); d++) {
                                                                Location loc = (Location) vt_loc.get(d);
                                                                val_locationid.add("" + loc.getOID() + "");
                                                                key_locationid.add(loc.getName());
                                                            }
                                                            String select_locationid = "" + srcMatReturn.getLocationFrom(); //selected on combo box
%>
                                                        <%=ControlCombo.draw(FrmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_LOCATION_FROM], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%></td>
                                                </tr>
                                            </table>
                                        </td> 
                                        <td>
                                            <table width="85%" border="0" cellspacing="1" cellpadding="1">  
                                                <tr> 
                                                    <td height="43" rowspan="2" valign="top" width="12%" align="left"><%=getJspTitle(3, SESS_LANGUAGE, retCode, true)%></td>
                                                    <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                                                    <td height="21" width="87%" valign="top" align="left"> 
                                                        <input type="radio" name="<%=frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNDATESTATUS]%>" <%if (srcMatReturn.getReturndatestatus() == 0) {%>checked<%}%> value="0">
                                                        <%=textListHeader[SESS_LANGUAGE][6]%></td>
                                                </tr>
                                                <tr align="left"> 
                                                    <td height="21" width="87%" valign="top"> 
                                                        <input type="radio" name="<%=frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNDATESTATUS]%>" <%if (srcMatReturn.getReturndatestatus() == 1) {%>checked<%}%> value="1">
                                                        <%=textListHeader[SESS_LANGUAGE][7]%> <%=ControlDate.drawDate(frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNFROMDATE], srcMatReturn.getReturnfromdate(), "formElemen", 1, -5)%>
                                                        <%=textListHeader[SESS_LANGUAGE][8]%>
                                                        <%=ControlDate.drawDate(frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNTODATE], srcMatReturn.getReturntodate(), "formElemen", 1, -5)%> </td>
                                                </tr>
                                                <tr> 
                                                    <td height="21" valign="top" width="12%" align="left"><%=getJspTitle(4, SESS_LANGUAGE, retCode, true)%></td>
                                                    <td height="21" valign="top" width="1%" align="left">:</td>
                                                    <td height="21" width="87%" valign="top" align="left"> 
                                                        <%
                                                            Vector vectResult = i_status.getDocStatusFor(docType);
                                                            for (int i = 0; i < vectResult.size(); i++) {
                                                                if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL) || (i == I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED)) {
                                                                    Vector vetTemp = (Vector) vectResult.get(i);
                                                                    int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                                                    String strPrStatus = String.valueOf(vetTemp.get(1));
                                                        %>
                                                        <input type="checkbox" class="formElemen" name="<%=frmSrcMatReturn.fieldNames[FrmSrcMatReturn.FRM_FIELD_RETURNSTATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcMatReturn.getReturnstatus(), indexPrStatus)) {%>checked<%}%>>
                                                        <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                                        <%
                                                                }
                                                            }
                                                        %>
                                                    </td>
                                                </tr>
                                                <tr> 
                                                    <td height="19" valign="top" width="12%" align="left"><%=getJspTitle(5, SESS_LANGUAGE, retCode, false)%></td>
                                                    <td height="19" valign="top" width="1%" align="left">:</td>
                                                    <td height="19" width="87%" valign="top" align="left"> 
                                                        <%
                                                            Vector key_sort = new Vector(1, 1);
                                                            Vector val_sort = new Vector(1, 1);
                                                            for (int i = 0; i < textOrderBy[0].length; i++) {
                                                                key_sort.add("" + i);
                                                                val_sort.add("" + getJspTitle2(i, SESS_LANGUAGE, retCode, true));
                                                            }
                                                            String select_sort = "" + srcMatReturn.getReturnsortby();
                                                            out.println("&nbsp;" + ControlCombo.draw(frmSrcMatReturn.fieldNames[frmSrcMatReturn.FRM_FIELD_RETURNSORTBY], null, select_sort, key_sort, val_sort, "", "formElemen"));
                                                        %>
                                                    </td>
                                                </tr>

                                            </table>
                                        </td>
                                    </table>
                                </form>
                                <!-- #EndEditable --></td> 
                        </tr> 
                        <tr align="left" valign="top"> 
                            <td height="18" valign="top" colspan="3"> 					                             
                                <table width="80%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <!--td nowrap width="1%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10', '', '<%=approot%>/images/BtnSearchOn.jpg', 1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0], ctrLine.CMD_SEARCH, true)%>"></a></td-->
                                        <td class=""><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][0], ctrLine.CMD_SEARCH, true)%></a></td>

                                        <%if (privAdd && privManageData) {%>
                                        <!--td nowrap width="4%"><a href="javascript:cmdAddRcv()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][3], ctrLine.CMD_ADD, true)%>"></a></td-->
                                        <td class=""><a class="btn btn-primary" href="javascript:cmdAddRcv()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][3], ctrLine.CMD_ADD, true)%></a></td>
                                        <!--td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][4], ctrLine.CMD_ADD, true)%>"></a></td-->
                                        <td class=""><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][4], ctrLine.CMD_ADD, true)%></a></td>
                                            <%}%>
                                    </tr>
                                </table>

                            </td>
                        </tr>
                        <tr align="left" valign="top">
                            <td height="18" valign="top" colspan="3"> 
                                <table width="50%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td nowrap width="1%">&nbsp;</td>
                                        <td class="command" nowrap width="26%">&nbsp;</td>

                                        <% if (privAdd) {%>
                                        <td nowrap width="4%">&nbsp;</td>
                                        <td class="command" nowrap width="31%">&nbsp;</td>
                                        <td nowrap width="4%">&nbsp;</td>
                                        <td class="command" nowrap width="31%">&nbsp;</td>
                                        <%}%>
                                    </tr>
                                </table>
                            </td>
                        </tr> 
                        <%if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>	 
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
                                <table width="80%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <%if (privAdd && privManageData) {%>
                                        <!--td nowrap width="4%"><a href="javascript:cmdAddRcv()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][3], ctrLine.CMD_ADD, true)%>"></a></td-->
                                        <td class="command" nowrap width="31%"><a class="btn btn-primary" href="javascript:cmdAddRcv()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][3], ctrLine.CMD_ADD, true)%></a></td>
                                        <!--td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][4], ctrLine.CMD_ADD, true)%>"></a></td-->
                                        <td class="command" nowrap width="31%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][4], ctrLine.CMD_ADD, true)%></a></td>
                                            <%}%>
                                    </tr>
                                </table>

                            </td>
                        </tr>	

                        <%}%>



                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../../../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../../../main/footer.jsp" %>
                    <%}%>

                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
