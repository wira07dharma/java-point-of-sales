<%-- 
    Document   : country
    Created on : Jan 24, 2014, 3:45:09 PM
    Author     : Fitra
--%>



<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_COUNTRY);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][]
            = {
                {"No", "Nama Negara", "Nama Benua", "Alamat", "Telepon", "Fax", "Kontak Person", "E-mail", "Website", "Locasi Induk", "Tipe", "Kontak Perusahaan", "Keterangan", "Tax Persentase", "Service Percentase", "Tax & Service Standart", "Persentasi Distribusi PO (%)", "Nama Perusahaan"},
                {"No", "Country Name", "Continent", "Address", "Phone", "Fax", "Person Name", "E-mail", "Website", "Parent Location", "Type", "Contact Link", "Description", "Tax Persentase", "Service Percentase", "Tax & Service Default", "Persentasi Distribusi PO (%)"}
            };
    public static final String textListTitleHeader[][]
            = {
                {"Negara"},
                {"Country"}
            };

    public String drawList(int language, Vector objectClass, long negaraId, String approot, int start, int recordToGet) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("50%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "2%");
        ctrlist.addHeader(textListHeader[language][1], "10%");
        ctrlist.addHeader(textListHeader[language][2], "10%");

        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector lstLinkDataa = ctrlist.getLinkData();
        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Negara negara = (Negara) objectClass.get(i);

            Vector rowx = new Vector();
            if (negaraId == negara.getOID()) {
                index = i;
            }

            start = start + 1;
            rowx.add("" + start);

            rowx.add("<a href=\"javascript:cmdEdit('" + negara.getOID() + "')\">" + negara.getNmNegara() + "</a>");

            rowx.add(cekNull(negara.getBenua()));

                //update by fitra
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(negara.getOID()));

        }
        return ctrlist.draw();
    }

    public String cekNull(String val) {
        if (val == null) {
            val = "";
        }
        return val;
    }
%>

<%    /* get data from request form */
    int iCommand = FRMQueryString.requestCommand(request);
    int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
    int startNegara = FRMQueryString.requestInt(request, "start_negara");
    int startMaterial = FRMQueryString.requestInt(request, "start_material");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidNegara = FRMQueryString.requestLong(request, "hidden_negara_id");
    long oidMinimum = FRMQueryString.requestLong(request, "hidden_mat_minimum_id");
    String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
//String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];

    /* variable declaration */
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA];

    /* ControlLine */
    ControlLine ctrLine = new ControlLine();

    /* Control LOcation */
    CtrlNegara ctrlNegara = new CtrlNegara(request);
    FrmNegara frmNegara = ctrlNegara.getForm();
    iErrCode = ctrlNegara.action(iCommand, oidNegara);
    Negara negara = ctrlNegara.getNegara();
    msgString = ctrlNegara.getMessage();

    /* get start value for list location */
    if (iCommand == Command.SAVE && iErrCode == FRMMessage.NONE) {
        startNegara = PstNegara.findLimitStart(negara.getOID(), recordToGet, whereClause);
    }
//update by fitra
    int vectSize = PstNegara.getCount(whereClause);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startNegara = ctrlNegara.actionList(iCommand, startNegara, vectSize, recordToGet);
    }

    /* get record to display */
    Vector listNegara = new Vector(1, 1);

//update by fitra
    listNegara = PstNegara.list(startNegara, recordToGet, whereClause, orderClause);
    if (listNegara.size() < 1 && startNegara > 0) {
        if (vectSize - recordToGet > recordToGet) {
            startNegara = startNegara - recordToGet;
        } else {
            startNegara = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        //update by fitra
        listNegara = PstNegara.list(startNegara, recordToGet, whereClause, orderClause);
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmNegara.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
            //window.location="#go";
            <%}%>

            /*------------- start location function ---------------*/
            function cmdAdd()
            {
                document.frmnegara.hidden_negara_id.value = "0";
                document.frmnegara.command.value = "<%=Command.ADD%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdAsk(oidNegara)
            {
                document.frmnegara.hidden_negara_id.value = oidNegara;
                document.frmnegara.command.value = "<%=Command.ASK%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdConfirmDelete(oidNegara)
            {
                document.frmnegara.hidden_negara_id.value = oidNegara;
                document.frmnegara.command.value = "<%=Command.DELETE%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdSave()
            {
                document.frmnegara.command.value = "<%=Command.SAVE%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdEdit(oidNegara)
            {
                document.frmnegara.hidden_negara_id.value = oidNegara;
                document.frmnegara.command.value = "<%=Command.EDIT%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdCancel(oidNegara)
            {
                document.frmnegara.hidden_negara_id.value = oidNegara;
                document.frmnegara.command.value = "<%=Command.EDIT%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdBack()
            {
                document.frmnegara.command.value = "<%=Command.BACK%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdListFirst()
            {
                document.frmnegara.command.value = "<%=Command.FIRST%>";
                document.frmnegara.prev_command.value = "<%=Command.FIRST%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdListPrev()
            {
                document.frmnegara.command.value = "<%=Command.PREV%>";
                document.frmnegara.prev_command.value = "<%=Command.PREV%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdListNext()
            {
                document.frmnegara.command.value = "<%=Command.NEXT%>";
                document.frmnegara.prev_command.value = "<%=Command.NEXT%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function cmdListLast()
            {
                document.frmnegara.command.value = "<%=Command.LAST%>";
                document.frmnegara.prev_command.value = "<%=Command.LAST%>";
                document.frmnegara.action = "country.jsp";
                document.frmnegara.submit();
            }

            function srcContact()
            {
                window.open("contact_search.jsp?command=<%=Command.FIRST%>&contact_name=" + document.frmnegara.contact_name.value, "group", "height=400,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            /*------------- end location function ---------------*/


            /*------------- start vendor price function -----------------*/
            function addMinimumQty()
            {
                document.frmnegara.command.value = "<%=Command.ADD%>";
                document.frmnegara.prev_command.value = "<%=prevCommand%>";
                document.frmnegara.action = "minimum_qty.jsp";
                document.frmnegara.submit();
            }

            function editMinimumQty(negaraId, minimumOid)
            {
                document.frmnegara.hidden_negara_id.value = negaraId;
                document.frmnegara.hidden_mat_minimum_id.value = minimumOid;
                document.frmnegara.command.value = "<%=Command.EDIT%>";
                document.frmnegara.action = "minimum_qty.jsp";
                document.frmnegara.submit();
            }
            /*------------- end vendor price function -----------------*/


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
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <link rel="StyleSheet" href="../../styles/font-awesome/font-awesome.css" type="text/css" >
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 

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
                    <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
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
                                Masterdata &gt; <%=locationTitle%><!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmnegara" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start_negara" value="<%=startNegara%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_negara_id" value="<%=oidNegara%>">
                                    <input type="hidden" name="hidden_mat_minimum_id" value="<%=oidMinimum%>">			  
                                    <input type="hidden" name="<%=FrmNegara.FRM_NAME_NEGARA%>" method="POST" action="">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                            <td height="8" valign="middle" colspan="3"> 
                                                <hr size="1">
                                            </td>

                                        </tr>                							  
                                        <tr align="left" valign="top"> 
                                            <td height="8"  colspan="3"> 
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top"> 
                                                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + locationTitle : locationTitle + " List"%></u></td>                      
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE, listNegara, oidNegara, approot, startNegara, recordToGet)%> </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="8" align="left" colspan="3" class="command"> 
                                                <span class="command"> 
                                                    <%
                                                        int cmd = 0;
                                                        if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                                                            cmd = iCommand;
                                                        } else {
                                                            if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                                                cmd = Command.FIRST;
                                                            } else {
                                                                cmd = prevCommand;
                                                            }
                                                        }
                                                        ctrLine.setLocationImg(approot + "/images");
                                                        ctrLine.initDefault();
                                                        out.println(ctrLine.drawMeListLimitBootstrap(cmd, vectSize, startNegara, recordToGet, "cmdListFirst", "cmdListPrev", "cmdListNext", "cmdListLast", "left"));
                                                    %>
                                                </span>
                                            </td>
                                        </tr>
                                        <%
                                            if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST
                                                    || iCommand == Command.NONE
                                                    || iCommand == Command.BACK
                                                    || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {
                                        %>					  
                                        <tr align="left" valign="top"> 
                                            <td> 
                                                <%if (((iCommand != Command.ADD) && (iCommand != Command.EDIT) && (iCommand != Command.ASK)) || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {%>
                                                <table cellpadding="0" cellspacing="0" border="0">
                                                    <tr> 
                                                        <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                        <!--<td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%>"></a></td>
                                                        <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>-->
                                                        <%if(privAdd){%>
                                                        <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdAdd()" class="btn-primary btn-lg"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                        <%}%>
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
                                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor " + locationTitle : locationTitle + " Editor"%></u></td>
                        </tr>
                        <tr> 
                            <td height="100%" width="100%" colspan="2"> 
                                <table border="0" cellspacing="1" cellpadding="1" width="100%">
                                    <tr align="left"> 
                                        <td width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top"> 
                                            <input required="required" type="text" name="<%=frmNegara.fieldNames[FrmNegara.FRM_FIELD_NM_NEGARA]%>"  value="<%= negara.getNmNegara()%>" class="formElemen" size="20">
                                            * <%= frmNegara.getErrorMsg(FrmNegara.FRM_FIELD_NM_NEGARA)%> </td>
                                    </tr>

                                    <tr align="left"> 
                                        <td width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top"> 
                                            <input type="text" required="required" name="<%=frmNegara.fieldNames[FrmNegara.FRM_FIELD_BENUA]%>"  value="<%= negara.getBenua()%>" class="formElemen" size="20">
                                            <%= frmNegara.getErrorMsg(FrmNegara.FRM_FIELD_BENUA)%> </td>
                                    </tr>



                                    <%-- finish eyek 13-06-2012--%>

                                </table>
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
                                    String scomDel = "javascript:cmdAsk('" + oidNegara + "')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidNegara + "')";
                                    String scancel = "javascript:cmdEdit('" + oidNegara + "')";
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

                                    if (iCommand == Command.EDIT && privUpdate == false) {
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
    <!-- #EndEditable -->
</td> 
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

