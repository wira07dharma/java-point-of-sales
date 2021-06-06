<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*,
         com.dimata.posbo.entity.masterdata.Category,
         com.dimata.posbo.entity.masterdata.PstCategory,
         com.dimata.posbo.form.masterdata.CtrlCategory,
         com.dimata.posbo.form.masterdata.FrmCategory,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.gui.jsp.ControlList,
         com.dimata.posbo.jsp.JspInfo,
         com.dimata.common.entity.payment.DiscountType,
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.posbo.entity.masterdata.PstDiscountMapping,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.common.entity.payment.PstDiscountType,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.posbo.session.masterdata.SessDiscountCategory" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CATEGORY);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%    boolean privEditPrice = true;
%>

<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListHeader[][]
            = {
                {"Kode", "Nama", "Harga Poin", "Keterangan", "Lokasi Produksi", "Parent Id", "Status", "Category", "Type Category", "Show Category", "Kenaikan Harga(%)"},
                {"Code", "Name", "Point Price", "Description", "Production Location", "Parent Id", "Status", "Category", "Type Category", "Tampilkan Category", "Increase Price(%)"}
            };

    /* this method used to list material department */
    public String drawList(int language, Vector objectClass, long departmentId, int start, String typeOfBusiness, int typeCategory) {
        int mappingProduksi = Integer.parseInt(PstSystemProperty.getValueByName("MAPPING_PRINT_PRODUKSI"));
        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.dataFormat("No", "5%", "center", "left");
        ctrlist.dataFormat(textListHeader[language][0], "10%", "center", "left");
        ctrlist.dataFormat(textListHeader[language][1], "15%", "center", "left");
        ctrlist.dataFormat(textListHeader[language][2], "10%", "center", "left");
        ctrlist.dataFormat(textListHeader[language][10], "10%", "center", "left");
        ctrlist.dataFormat(textListHeader[language][3], "10%", "center", "left");
        if (typeOfBusiness.equals("2")) {
            ctrlist.dataFormat(textListHeader[language][4], "10%", "center", "left");
            ctrlist.dataFormat(textListHeader[language][7], "10%", "center", "left");
            if (mappingProduksi == 2) {
                ctrlist.dataFormat(textListHeader[language][6], "10%", "center", "left");
            }
        }

        ctrlist.setLinkRow(1); 
        ctrlist.setLinkSufix("");//??
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        String where = "" + PstCategory.fieldNames[PstCategory.FLD_TYPE_CATEGORY] + "='" + typeCategory + "'";
        //Vector masterCatAcak = PstCategory.list(0,0,""+where,PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID]);
        Vector masterCatAcak = PstCategory.list(0, 0, "" + where, PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID] + "," + PstCategory.fieldNames[PstCategory.FLD_CODE]);
        //Category newCategory = new Category();
        //add opie-eyek 20130821
        Vector materGroup = PstCategory.structureList(masterCatAcak);
        if (materGroup != null && materGroup.size() > 0) {
            String parent = "";
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
                        parent = parent + "&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
                    levelParent.add(mGroup.getOID());

                } else {
                    levelParent.removeAllElements();
                    levelParent.add(mGroup.getOID());
                    parent = "";
                }
                //disini
                Vector rowx = new Vector();
                rowx.add("" + (i + start + 1));
                rowx.add(parent + mGroup.getCode());
                //check jumlah material group ini
                int countMaterial = PstMaterial.getCount("" + PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + "='" + mGroup.getOID() + "'");
                rowx.add(parent + mGroup.getName() + "  (" + countMaterial + ")");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mGroup.getPointPrice()) + "</div>");
                rowx.add("" +mGroup.getKenaikanHarga());
                rowx.add(mGroup.getDescription());
                //adding production location by Mirahu 20120511
                if (typeOfBusiness.equals("2")) { 
                    Location location = new Location();
                    try {
                        location = PstLocation.fetchExc(mGroup.getLocationId());
                    } catch (Exception e) {
                        // System.out.println("Location not found ...");
                    }
                    rowx.add(location.getName());

                    rowx.add("" + PstCategory.category[mGroup.getTypeCategory()]);

                    if (mappingProduksi == 2) {
                        rowx.add("<a href=\"javascript:addCategory('" + mGroup.getOID() + "')\" >mapping produksi</a>");
                    }

                }

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(mGroup.getOID()));
            }
 
        }
        return ctrlist.drawMe(index);
    }

%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidMatDepartment = FRMQueryString.requestLong(request, "hidden_department_id");

    int source = FRMQueryString.requestInt(request, "source");

    String departmentTitle = kategoriName;//JspInfo.txtMaterialInfo[SESS_LANGUAGE][JspInfo.MATERIAL_DEPARTMENT];

    int recordToGet = 15;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    /**
     * Special case if code is caracter of numeric (1,2,3,4,5, ...) dengan "+1"
     * artinya String akan diubah secara implisit menjadi Numerik
     */
    String orderClause = "(" + PstCategory.fieldNames[PstCategory.FLD_CODE] + "+1)";

    CtrlCategory ctrlMatCategory = new CtrlCategory(request);
    ControlLine ctrLine = new ControlLine();
    Vector listMatCategory = new Vector(1, 1);

    iErrCode = ctrlMatCategory.action(iCommand, oidMatDepartment);
    FrmCategory frmMatDepartment = ctrlMatCategory.getForm();

// count list All MatDepartment
    int vectSize = 0;//PstCategory.getCount(whereClause);

    Category matCategory = ctrlMatCategory.getCategory();
    msgString = ctrlMatCategory.getMessage();

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        start = ctrlMatCategory.actionList(iCommand, start, vectSize, recordToGet);
    }

// get record to display
    orderClause = PstCategory.fieldNames[PstCategory.FLD_NAME];
//listMatCategory = PstCategory.list(start,recordToGet,whereClause,orderClause);

// handle condition if size of record to display=0 and start>0 after delete
    if (listMatCategory.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
    }

    long oidCurr = 0;
    try {
        oidCurr = Long.parseLong((String) com.dimata.system.entity.PstSystemProperty.getValueByName("OID_CURR_FOR_PRICE_SALE"));
    } catch (Exception e) {
        oidCurr = 0;
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdAdd()
            {
                document.frmcategory.hidden_department_id.value = "0";
                document.frmcategory.command.value = "<%=Command.ADD%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdAsk(oidMatDepartment)
            {
                document.frmcategory.hidden_department_id.value = oidMatDepartment;
                document.frmcategory.command.value = "<%=Command.ASK%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdConfirmDelete(oidMatDepartment)
            {
                document.frmcategory.hidden_department_id.value = oidMatDepartment;
                document.frmcategory.command.value = "<%=Command.DELETE%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }
            function cmdSave()
            {
                document.frmcategory.command.value = "<%=Command.SAVE%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdEdit(oidMatDepartment)
            {
                document.frmcategory.hidden_department_id.value = oidMatDepartment;
                document.frmcategory.command.value = "<%=Command.EDIT%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdCancel(oidMatDepartment)
            {
                document.frmcategory.hidden_department_id.value = oidMatDepartment;
                document.frmcategory.command.value = "<%=Command.EDIT%>";
                document.frmcategory.prev_command.value = "<%=prevCommand%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdBack()
            {
                document.frmcategory.command.value = "<%=Command.BACK%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdListFirst()
            {
                document.frmcategory.command.value = "<%=Command.FIRST%>";
                document.frmcategory.prev_command.value = "<%=Command.FIRST%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdListPrev()
            {
                document.frmcategory.command.value = "<%=Command.PREV%>";
                document.frmcategory.prev_command.value = "<%=Command.PREV%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdListNext()
            {
                document.frmcategory.command.value = "<%=Command.NEXT%>";
                document.frmcategory.prev_command.value = "<%=Command.NEXT%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdListLast()
            {
                document.frmcategory.command.value = "<%=Command.LAST%>";
                document.frmcategory.prev_command.value = "<%=Command.LAST%>";
                document.frmcategory.action = "matcategory.jsp";
                document.frmcategory.submit();
            }

            function cmdClose() {
                self.opener.document.forms.frmmaterial.submit();
                self.close();
            }

            function addCategory(oidmapping) {
                //alert("aaa");
                var strvalue = "mapping_kitchen_produksi.jsp?command=<%=Command.ADD%>&oidmapping=" + oidmapping + "&source=1";
                winSrcMaterial = window.open(strvalue, "PopupWindow", "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
            }


            function cmdExport() {
                var strvalue = "matcategory_excel.jsp";
                winSrcMaterial = window.open(strvalue, "searchtipeharga", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                if (window.focus) {
                    winSrcMaterial.focus();
                }
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
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
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
        <style type="text/css">
            .listheader { COLOR: #FFFFFF; background-color:<%=tableHeader%>; FONT-SIZE: 10px; FONT-WEIGHT: bold; TEXT-ALIGN: center}
            .listgensell {  color: #000000; background-color:<%=tableCell%>}
            .listgensell {  color: #000000; background-color:<%=tableCell%>}
            .tabcontent {  background-color: <%=tableCell%>}
            .table_cell {  background-color: <%=tableCell%>}
            .listgentitle {  font-size: 11px; font-style: normal; font-weight: bold; color: #FFFFFF; background-color: <%=tableHeader%>; text-align: center}
        </style>
        <!-- #EndEditable --> 
    </head> 

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
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
            <%} else {%>
            <%if (source == 0) {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                Masterdata &gt; <%=departmentTitle%><!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmcategory" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_department_id" value="<%=oidMatDepartment%>">
                                    <input type="hidden" name="source" value="<%=source%>">
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
                                                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + departmentTitle : departmentTitle + " List"%></u></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3"> 
                                                <%=drawList(SESS_LANGUAGE, listMatCategory, oidMatDepartment, start, typeOfBusiness, 0)%> 
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="14" valign="middle" colspan="3" class="comment">&nbsp;</td>
                                        </tr>
                                        <%  if (typeOfBusiness.equals("2")) {%>
                                        <tr align="left" valign="top">
                                            <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + departmentTitle + " Sales " : departmentTitle + " Sales List"%></u></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3"> 
                                                <%=drawList(SESS_LANGUAGE, listMatCategory, oidMatDepartment, start, typeOfBusiness, 1)%> 
                                            </td>
                                        </tr>
                                        <%}%>
                                        <tr align="left" valign="top">
                                            <td height="8" align="left" colspan="3" class="command">
                                                <span class="command">
                                                    <%
                                                        int cmd = 0;
                                                        if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                                                || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                            cmd = iCommand;
                                                        } else {
                                                            if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                cmd = Command.FIRST;
                                                            } else {
                                                                cmd = prevCommand;
                                                            }
                                                        }
                                                    %>
                                                    <%
                                                        ctrLine.setLocationImg(approot + "/images");
                                                        ctrLine.initDefault();
                                                    %>
                                                    <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> </span> </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <%if (iCommand != Command.ADD && iCommand != Command.EDIT && iCommand != Command.ASK && iErrCode == FRMMessage.NONE) {%>
                                                <table width="17%" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <%if (privAdd) {%>
                                                            <!--td width="18%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_ADD, true)%>"></a></td-->
                                                        <td nowrap width="82%"><a class="btn btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                        <%}%>
                                                    </tr>
                                                    <tr>
                                                        <%if (source == 1) {%>
                                                            <!--td width="18%"><a href="javascript:cmdClose()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK_SEARCH, true)%>"></a></td-->
                                                        <td nowrap width="82%"><a class="btn btn-primary" href="javascript:cmdClose()" class="command"><i class="fa fa-close"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                                        <%}%>
                                                    </tr>
                                                </table>
                                                <%}%>			
                                            </td>
                                        </tr>
                                    </table>
                            </td>
                        </tr>

                        <tr align="left" valign="top">
                            <td height="8" valign="middle" colspan="3">
                                <%
                                    if ((iCommand == Command.ADD)
                                            || (iCommand == Command.EDIT)
                                            || (iCommand == Command.ASK)
                                            || ((iCommand == Command.SAVE || iCommand == Command.DELETE) && (iErrCode > 0))) {
                                %>
                                <table width="100%" border="0" cellspacing="2" cellpadding="0">
                                    <tr align="left">
                                        <td colspan="10" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor " + departmentTitle : departmentTitle + " Editor"%></u></td>
                        <tr align="left">
                            <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][0]%></td>
                            <td width="1%">:
                            <td colspan="8" width="90%">
                                <input type="text" name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_CODE]%>"  value="<%= matCategory.getCode()%>" class="formElemen" size="10">
                                * <%=frmMatDepartment.getErrorMsg(frmMatDepartment.FRM_FIELD_CODE)%>
                        <tr align="left">
                            <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][1]%></td>
                            <td width="1%">:
                            <td colspan="8" width="90%">
                                <input type="text" name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_NAME]%>"  value="<%= matCategory.getName()%>" class="formElemen" size="30">
                        <tr align="left">
                            <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][2]%></td>
                            <td width="1%">:
                            <td colspan="8" width="90%">
                                <input type="text" name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_PRICE_POINT]%>"  value="<%= FRMHandler.userFormatStringDecimal(matCategory.getPointPrice())%>" class="formElemen" style="text-align:right" size="10"><tr align="left">
                            <td width="9%">&nbsp;<%=textListHeader[SESS_LANGUAGE][10]%></td>
                            <td width="1%">:
                            <td colspan="8" width="90%">
                                <input type="text" name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_KENAIKAN_HARGA]%>"  value="<%=matCategory.getKenaikanHarga() %>" class="formElemen" style="text-align:right" size="10">
                        <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][3]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                                <textarea name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_DESCRIPTION]%>" cols="30" rows="5" class="formElemen"><%= matCategory.getDescription()%></textarea>
                                <!-- Adding production location by mirahu 20120511 -->
                        <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][5]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                                <select id="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_PARENT_ID]%>"  name="<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_PARENT_ID]%>" class="formElemen">
                                    <option value="0">-No Parent-</option>
                                    <%
                                        Vector masterCatAcak = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_CAT_PARENT_ID] + "," + PstCategory.fieldNames[PstCategory.FLD_CODE]);
                                        Vector materGroup = PstCategory.structureList(masterCatAcak);
                                        Vector vectGroupVal = new Vector(1, 1);
                                        Vector vectGroupKey = new Vector(1, 1);
                                        if (materGroup != null && materGroup.size() > 0) {
                                            String parent = "";
                                            //Vector<Category> resultTotal= new Vector();
                                            //add opie-eyek 20130821
                                            Vector<Long> levelParent = new Vector<Long>();
                                            for (int i = 0; i < materGroup.size(); i++) {
                                                Category mGroup = (Category) materGroup.get(i);
                                                String select = "";
                                                if (mGroup.getOID() == matCategory.getCatParentId()) {
                                                    select = "selected";
                                                }
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
                                    <option value="<%=mGroup.getOID()%>" <%=select%>><%=parent%><%=mGroup.getName()%></option>
                                    <%
                                            }
                                        } else {
                                            vectGroupVal.add("Tidak Ada Category");
                                            vectGroupKey.add("0");
                                        }
                                    %>
                                </select>

                                <%  if (typeOfBusiness.equals("2")) {%>
                                <%--
                                <tr align="left">
                                <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][4]%></td>
                                <td width="1%" valign="top">:
                                <td colspan="8" width="90%">
                                  <%
                                                                Vector val_locationid = new Vector(1,1);
                                                                Vector key_locationid = new Vector(1,1);
                                                                Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                                                val_locationid.add("0");
                                                                key_locationid.add("Semua Lokasi");
                                                                for(int d=0;d<vt_loc.size();d++){
                                                                        Location loc = (Location)vt_loc.get(d);
                                                                        val_locationid.add(""+loc.getOID()+"");
                                                                        key_locationid.add(loc.getName());
                                                                }
                                                                String select_loc = "0";

                                                    %>
                              <%=ControlCombo.draw(frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_LOCATION_ID],"formElemen", null, ""+matCategory.getLocationId(), val_locationid, key_locationid, null)%></td>
                          </tr>
                          
                          
                           <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][6]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                              <%
                                //adding combo box 
                                Vector val_merkstatus = new Vector(1,1);
                                Vector key_merkstatus = new Vector(1,1);

                                val_merkstatus.add("0");
                                key_merkstatus.add("Tidak ditampilkan di outlet");

                                val_merkstatus.add("1");
                                key_merkstatus.add("Ditampilkan dioutlet");
                              %>
                              <%=ControlCombo.draw(frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_STATUS],"formElemen", null, ""+matCategory.getStatus(), val_merkstatus, key_merkstatus, null)%></td>
                          </tr>
                                --%>
                        <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][7]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                                <%
                                    //adding combo box 
                                    Vector val_category = new Vector(1, 1);
                                    Vector key_category = new Vector(1, 1);

                                    val_category.add("0");
                                    key_category.add("Food");

                                    val_category.add("1");
                                    key_category.add("Beverage");

                                    val_category.add("2");
                                    key_category.add("Other");

                                %>
                                <%=ControlCombo.draw(frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_CATEGORY], "formElemen", null, "" + matCategory.getCategoryId(), val_category, key_category, null)%></td>
                        </tr>
                        <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][8]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                                <%
                                    //adding combo box 
                                    Vector val_type_category = new Vector(1, 1);
                                    Vector key_type_category = new Vector(1, 1);

                                    val_type_category.add("0");
                                    key_type_category.add("Inventory Items");

                                    val_type_category.add("1");
                                    key_type_category.add("Sales items");

                                %>
                                <%=ControlCombo.draw(frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_TYPE_CATEGORY], "formElemen", null, "" + matCategory.getTypeCategory(), val_type_category, key_type_category, null)%></td>
                        </tr>

                        <%}%>
                        <tr align="left">
                            <td width="9%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][9]%></td>
                            <td width="1%" valign="top">:
                            <td colspan="8" width="90%">
                                <%
                                    //adding combo box 
                                    Vector val_show_category = new Vector(1, 1);
                                    Vector key_show_category = new Vector(1, 1);

                                    val_show_category.add("0");
                                    key_show_category.add("Dont Show");

                                    val_show_category.add("1");
                                    key_show_category.add("Show");

                                %>
                                <%=ControlCombo.draw(frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_STATUS], "formElemen", null, "" + matCategory.getStatus(), val_show_category, key_show_category, null)%></td>
                        </tr>
                        <tr align="left" >
                            <td colspan="10" class="command" valign="top">                      
                        </tr>
                        <tr align="left" >
                            <td colspan="10" class="command" valign="top">                      
                        </tr>
                        <tr align="left" >
                            <td colspan="10" class="command" valign="top">
                                <%
                                    ctrLine.setLocationImg(approot + "/images");

                                    // set image alternative caption
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_SAVE, true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK, true) + " List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_ASK, true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_CANCEL, false));

                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("80%");
                                    String scomDel = "javascript:cmdAsk('" + oidMatDepartment + "')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidMatDepartment + "')";
                                    String scancel = "javascript:cmdEdit('" + oidMatDepartment + "')";
                                    ctrLine.setCommandStyle("command");
                                    ctrLine.setColCommStyle("command");

                                    // set command caption
                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_SAVE, true));
                                    ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_BACK, true) + " List");
                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_ASK, true));
                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_DELETE, true));
                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, departmentTitle, ctrLine.CMD_CANCEL, false));

                                    if (privDelete) {
                                        ctrLine.setConfirmDelCommand(sconDelCom);
                                        ctrLine.setDeleteCommand(scomDel);
                                        ctrLine.setEditCommand(scancel);
                                    } else {
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                    }

                                    if (iCommand == Command.EDIT && privUpdate == false) {
                                        ctrLine.setSaveCaption("");
                                    }

                                    if (privAdd == false) {
                                        ctrLine.setAddCaption("");
                                    }
                                    //ctrLine.setAddCaption("");
                                %>
                                <%
                                    if ((iCommand == Command.ADD)
                                            || (iCommand == Command.EDIT)
                                            || (iCommand == Command.ASK)
                                            || ((iCommand == Command.SAVE || iCommand == Command.DELETE) && iErrCode > 0)) {
                                        out.println(ctrLine.drawImage(iCommand, iErrCode, msgString));
                                    }
                                %>
                        </tr>

                    </table>
                    <script language="javascript">
                        document.frmcategory.<%=frmMatDepartment.fieldNames[FrmCategory.FRM_FIELD_CODE]%>.focus();
                    </script>
                    <%}%>
                </td>
            </tr>
            <tr align="right">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <br>
                <tr> 
                  <!--td nowrap width="80%" align="right"><a href="javascript:cmdExport()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Barang"></a></td-->
                    <td class="command" nowrap width="80%"><a class="btn btn-primary" href="javascript:cmdExport()"><i class="fa fa-file"></i> Export Excel</a></td>    
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
        <%if (menuUsed == MENU_ICON) {%>
        <%@include file="../../styletemplate/footer.jsp" %>

        <%} else {%>
        <%@ include file = "../../main/footer.jsp" %>
        <%}%>
        <!-- #EndEditable --> </td>
</tr>
</table>
</body>
<!-- #EndTemplate --></html>
