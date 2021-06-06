<%@page import="com.dimata.posbo.session.warehouse.SessMatStockOpname"%>
<%@page import="com.dimata.posbo.form.search.FrmSrcMatStockOpname"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatStockOpname"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>

<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import="com.dimata.posbo.entity.search.SrcStockCard,
         com.dimata.posbo.form.search.FrmSrcStockCard,
         com.dimata.posbo.session.warehouse.SessStockCard,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListGlobal[][] = {
        {"Stok", "Laporan", "Kartu Stok", "Pencarian", "Tidak ada data", "Cetak Kartu Stok", "Harus Diisi"},
        {"Stock", "Report", "Stock Card", "Searching", "No stock available", "Print Stock Card", "Entry Required"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"Lokasi", "Kode/Nama Barang", "Kategori", "Periode", "Urut Berdasar", "Sub Kategori", "Supplier", "Status Dokumen","Tampilkan Perpindahan Stok"},
        {"Location", "Code/Goods Name", "Category", "Period", "Sort By", "Sub Category", "Supplier", "Document Status","Show Transfer Stock"}
    };

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
<%    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int systemName = I_DocType.SYSTEM_MATERIAL;
    int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_LMRR);

    /**
     * get data from 'hidden form'
     */
    String txtKode = FRMQueryString.requestString(request, "txtkode");
    String txtNama = FRMQueryString.requestString(request, "txtnama");
    int typeCheckKartu = FRMQueryString.requestInt(request, "typeCheckKartu");
    long oidMaterialId = FRMQueryString.requestLong(request, FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]);
    long locationIdKoreksi = FRMQueryString.requestLong(request, "locationId");
    Date dateKoreksi = FRMQueryString.requestDate(request, "fromEndDate");
    Date toDate = FRMQueryString.requestDate(request, "toDate");
    int includeWarehouse = FRMQueryString.requestInt(request, "INCLUDE_WAREHOUSE");
    int showTransferHistory = FRMQueryString.requestInt(request, "SHOW_TRANSFER_HISTORY");
    /**
     * update opie-eyek untuk membuka kartu stock dari laporan koreksi stock
     */
    SrcMatStockOpname srcMatStockOpname = null;
    if (oidMaterialId != 0) {
        srcMatStockOpname = (SrcMatStockOpname) session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKCORRECTION);
    }

    int iCommand = FRMQueryString.requestCommand(request);
    int index = FRMQueryString.requestInt(request, "type");

//System.out.println("index : "+index);
    ControlLine ctrLine = new ControlLine();

    SrcStockCard srcStockCard = new SrcStockCard();
    FrmSrcStockCard frmSrcStockCard = new FrmSrcStockCard();
    Material material = new Material();
    try {
        srcStockCard = (SrcStockCard) session.getValue(SessStockCard.SESS_STOCK_CARD);

        srcStockCard.setMaterialId(oidMaterialId);

        if (srcStockCard.getMaterialId() != 0) {
            material = PstMaterial.fetchExc(srcStockCard.getMaterialId());
            txtKode = material.getSku();
            txtNama = material.getName();
        }
    } catch (Exception e) {
        srcStockCard = new SrcStockCard();
    }

    if (srcStockCard == null) {
        srcStockCard = new SrcStockCard();
    }

    try {
        session.removeValue(SessStockCard.SESS_STOCK_CARD);
    } catch (Exception e) {
    }

//update opie-eyek20140404
    if (srcMatStockOpname != null && typeCheckKartu == 1) {
        material = PstMaterial.fetchExc(oidMaterialId);
        srcStockCard.setLocationId(srcMatStockOpname.getLocationId());
        srcStockCard.setStardDate(srcMatStockOpname.getFromDate());
        srcStockCard.setEndDate(srcMatStockOpname.getToDate());
        srcStockCard.setDocStatus(srcMatStockOpname.getDocStatus());
        srcStockCard.setMaterialId(oidMaterialId);
    }

    if (typeCheckKartu == 2) {
        material = PstMaterial.fetchExc(oidMaterialId);
        srcStockCard.setLocationId(locationIdKoreksi);
        srcStockCard.setMaterialId(oidMaterialId);
        //srcStockCard.setEndDate(dateKoreksi);
    }

    if (typeCheckKartu == 3) {
        material = PstMaterial.fetchExc(oidMaterialId);
        srcStockCard.setLocationId(locationIdKoreksi);
        srcStockCard.setMaterialId(oidMaterialId);
        srcStockCard.setEndDate(dateKoreksi);
        srcStockCard.setStardDate(toDate);
    }

    Vector val_locationid1 = new Vector(1, 1);
    Vector key_locationid1 = new Vector(1, 1);
    val_locationid1.add("0");
    key_locationid1.add("All location");
    String locWhereClause = "";
    if (index == 0) {
        locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
    } else {
        locWhereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
    }
    String locOrderBy = PstLocation.fieldNames[PstLocation.FLD_NAME];
//Vector vt_loc1 = PstLocation.list(0,0,locWhereClause,locOrderBy);
//Vector vt_loc1 = PstLocation.list(0,0,"",locOrderBy);
    String whereLocViewReport = PstDataCustom.whereLocReportViewStock(userId, "user_view_sale_stock_report_location");
    Vector vt_loc1 = PstLocation.list(0, 0, whereLocViewReport, locOrderBy);
    for (int d = 0; d < vt_loc1.size(); d++) {
        Location loc = (Location) vt_loc1.get(d);
        val_locationid1.add("" + loc.getOID() + "");
        key_locationid1.add(loc.getName());
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">

            function cmdSearch() {
            <% String msg = textListHeader[SESS_LANGUAGE][1] + " " + textListGlobal[SESS_LANGUAGE][6];%>
                if (document.frmsrcstockcard.txtkode.value != "" || document.frmsrcstockcard.txtnama.value != "") {
                    document.frmsrcstockcard.command.value = "<%=Command.LIST%>";
                    document.frmsrcstockcard.action = "stockcard_list.jsp";
                    document.frmsrcstockcard.submit();
                }
                else {
                    alert("<%=msg%>!");
                }
            }

            function cekbrg() {
                window.open("matstockcardsearch.jsp?mat_code='" + document.frmsrcstockcard.txtkode.value + "'" + "&txt_materialname='" + document.frmsrcstockcard.txtnama.value + "'", "srhmatcard", "height=500,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            
            function cmdSwitchLocation(){
                var location = document.frmsrcstockcard.<%=frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_LOCATION_ID]%>.value;
                if (location === "0") {
                    document.getElementById("showInclude").style.display="none";
                } else {
                    document.getElementById("showInclude").style.display="inline";
                }
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
                                <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmsrcstockcard" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="type" value="<%=index%>">
                                    <input type="hidden" name="<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_MATERIAL_ID]%>" value="<%=srcStockCard.getMaterialId()%>">
                                    <table width="100%" border="0">
                                        <tr>
                                            <td colspan="2">
                                                <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                    <tr>
                                                        <td height="21" colspan=3""><hr size="1"></td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="" width="15%" align="left"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                                        <td valign="" width="1%" align="left">:</td>
                                                        <td width="84%" valign="" align="left"><%=ControlDate.drawDate(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_START_DATE], srcStockCard.getStardDate(), "formElemen", 1, -5)%> s/d <%=	ControlDate.drawDate(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_END_DATE], srcStockCard.getEndDate(), "formElemen", 1, -5)%></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="15%"><%=textListHeader[SESS_LANGUAGE][0]%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="21" width="84%">
                                                            <%=ControlCombo.draw(frmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_LOCATION_ID], null, "" + srcStockCard.getLocationId(), val_locationid1, key_locationid1, "onChange='javascript:cmdSwitchLocation()'", "formElemen")%>
                                                            <%
                                                                String checkWarehouse = "";
                                                                if (iCommand == Command.BACK && includeWarehouse == 1) {
                                                                    checkWarehouse = "checked";
                                                                }
                                                            %>
                                                            <div id="showInclude" style="display: none">
                                                                <input type="checkbox" <%= checkWarehouse %> name="INCLUDE_WAREHOUSE" value="1"> Include warehouse
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="" align="left"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                                        <td valign="" align="left">:</td>
                                                        <td valign="" align="left">
                                                            <input name="txtkode" type="text" value="<%=txtKode%>" class="formElemen" id="txtkode" size="20"> /
                                                            <input name="txtnama" type="text" class="formElemen" id="txtnama" value="<%=txtNama%>" size="40">
                                                            <a href="javascript:cekbrg()">CHK</a> *
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="" width="15%" align="left"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                                        <td height="21" valign="" width="1%" align="left">:</td>
                                                        <td height="31" width="84%" valign="" align="left">
                                                            <input type="checkbox" class="formElemen" name="<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_DOC_STATUS]%>" value="<%=(I_DocStatus.DOCUMENT_STATUS_APPROVED)%>"onKeyDown="">

                                                            <%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     
                                                            <%
                                                                Vector vectResult = i_status.getDocStatusFor(docType);
                                                                for (int i = 0; i < vectResult.size(); i++) {
                                                                    if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL) || (i == I_DocStatus.DOCUMENT_STATUS_APPROVED)) {
                                                                        Vector vetTemp = (Vector) vectResult.get(i);
                                                                        int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));
                                                                        String strPrStatus = String.valueOf(vetTemp.get(1));
                                                            %>
                                                            <input type="checkbox" class="formElemen" name="<%=FrmSrcStockCard.fieldNames[FrmSrcStockCard.FRM_FIELD_DOC_STATUS]%>" value="<%=(indexPrStatus)%>" <%if (getTruedFalse(srcStockCard.getDocStatus(), indexPrStatus)) {%>checked<%}%> onKeyDown="">
                                                            <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                                                            <%
                                                                    }
                                                                }
                                                            %>

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" width="15%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                                        <td height="21" width="1%">:</td>
                                                        <td height="31" width="84%">
                                                            <%
                                                                String checkedTransferHistory = "checked";
                                                                if (iCommand == Command.BACK && showTransferHistory == 0) {
                                                                    checkedTransferHistory = "";
                                                                }
                                                            %>
                                                            <input type="checkbox" <%= checkedTransferHistory %> class="formElemen" name="SHOW_TRANSFER_HISTORY" value="1">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td height="21" valign="" width="15%" align="left">&nbsp;</td>
                                                        <td height="21" valign="" width="1%" align="left">&nbsp;</td>
                                                        <td height="50" width="84%" valign="" align="left">
                                                            <a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, textListGlobal[SESS_LANGUAGE][2], ctrLine.CMD_SEARCH, true)%></a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </form>
                                <!-- #EndEditable -->
                            </td> 
                        </tr> 
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
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>
        <script language=JavaScript>
            cmdSwitchLocation();
        </script>
    </body>
    <!-- #EndTemplate -->
</html>
