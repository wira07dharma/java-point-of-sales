<%-- 
    Document   : provinsi
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
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_PROVINCE);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][]
            = {
                {"No", "Nama Provinsi", "Nama Benua", "Alamat", "Telepon", "Fax", "Kontak Person", "E-mail", "Website", "Locasi Induk", "Tipe", "Kontak Perusahaan", "Keterangan", "Tax Persentase", "Service Percentase", "Tax & Service Standart", "Persentasi Distribusi PO (%)", "Nama Perusahaan"},
                {"No", "provinsi Name", "Name", "Address", "Phone", "Fax", "Person Name", "E-mail", "Website", "Parent Location", "Type", "Contact Link", "Description", "Tax Persentase", "Service Percentase", "Tax & Service Default", "Persentasi Distribusi PO (%)"}
            };
    public static final String textListTitleHeader[][]
            = {
                {"Provinsi"},
                {"Province"}
            };

    public String drawList(int language, int iCommand, FrmPropinsi frmObject, Provinsi objEntity, Vector objectClass, long idPropinsi) {
        String result = "";

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("Country", "25%");
        ctrlist.addHeader("Prov.Code", "25%");
        ctrlist.addHeader("Province", "50%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.reset();
        Vector rowx = new Vector(1, 1);
        int index = -1;
        String kdPropinsi = objEntity.getKdProvinsi();

        String idNegara = objEntity.getIdNegara() + "";

        Vector listNegara = new Vector();
        Vector vKey = new Vector();
        Vector vValue = new Vector();
        Hashtable hNegara = new Hashtable();
        try {
            listNegara = PstNegara.list(0, 0, "", "");
            for (int i = 0; i < listNegara.size(); i++) {
                Negara negara = (Negara) listNegara.get(i);
                vKey.add(negara.getOID() + "");
                vValue.add(negara.getNmNegara());
                hNegara.put("" + negara.getOID(), negara.getNmNegara());
            }
        } catch (Exception e) {
            System.out.println("err on drawList:" + e.toString());
        }

        if (objectClass != null && objectClass.size() > 0) {
            for (int i = 0; i < objectClass.size(); i++) {
                Provinsi provinsi = (Provinsi) objectClass.get(i);
                if (idPropinsi == provinsi.getOID()) {
                    index = i;
                }

                rowx = new Vector();
                if ((index == i) && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                    rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPropinsi.FRM_FIELD_ID_NEGARA], "-select-", "" + provinsi.getIdNegara(), vKey, vValue));
                    rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_KD_PROPINSI] + "\" value=\"" + provinsi.getKdProvinsi() + "\" class=\"formElemen\" size=\"10\">");
                    rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_NM_PROPINSI] + "\" value=\"" + provinsi.getNmProvinsi() + "\" class=\"formElemen\" size=\"40\">");
                } else {
                    rowx.add(String.valueOf(hNegara.get("" + provinsi.getIdNegara())));
                    rowx.add(String.valueOf(provinsi.getKdProvinsi()));
                    rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(provinsi.getOID()) + "')\">" + provinsi.getNmProvinsi() + "</a>");
                }
                lstData.add(rowx);
            }
            rowx = new Vector();

            if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPropinsi.FRM_FIELD_ID_NEGARA], "Pilih Negara", idNegara, vKey, vValue));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_KD_PROPINSI] + "\" value=\"" + kdPropinsi + "\" class=\"formElemen\" size=\"10\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_NM_PROPINSI] + "\" value=\"" + objEntity.getNmProvinsi() + "\" class=\"formElemen\" size=\"40\">");
            }
            lstData.add(rowx);
            result = ctrlist.draw();
        } else {
            if (iCommand == Command.ADD) {
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPropinsi.FRM_FIELD_ID_NEGARA], "Pilih Negara", idNegara, vKey, vValue));
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_KD_PROPINSI] + "\" value=\"" + kdPropinsi + "\" class=\"formElemen\" size=\"10\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmPropinsi.FRM_FIELD_NM_PROPINSI] + "\" value=\"" + objEntity.getNmProvinsi() + "\" class=\"formElemen\" size=\"40\">");
                lstData.add(rowx);
                result = ctrlist.draw();
            } else {
                result = "<i>No Province data in database ...</i>";
            }
        }
        return result;
    }
%>


<%// request data from jsp page
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidPropinsi = FRMQueryString.requestLong(request, "hidden_id_propinsi");
    String locationTitle = textListTitleHeader[SESS_LANGUAGE][0];
// variable declaration
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;

// instantiate Provinsi classes
    CtrlProvinsi ctrlProvinsi = new CtrlProvinsi(request);
    ControlLine ctrLine = new ControlLine();

// set text command based on indonesia language
    String strPropinsi = "Province";
    String strSave = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_SAVE, true);
    String strDelete = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_ASK, true);
    String strCancel = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_CANCEL, false);
    String strConfirmDel = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_DELETE, true);
    String strSaveInfo = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_SAVE_SUCCESS, false);
    String strConfirmDelInfo = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_DELETE_SUCCESS, false);
    String strDelQuest = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_DELETE_QUESTION, false);
    String strBack = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_BACK, true);
    String strAdd = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_ADD, true);
    String strPrevCaption = ctrLine.getCommand(SESS_LANGUAGE, strPropinsi, ctrLine.CMD_PREV, false);

// action on object provinsi defend on command entered
    iErrCode = ctrlProvinsi.action(iCommand, oidPropinsi);
    FrmPropinsi frmPropinsi = ctrlProvinsi.getForm();
    Provinsi provinsi = ctrlProvinsi.getPropinsi();
    msgString = ctrlProvinsi.getMessage();

// get records to display
    String whereClause = "";
    String orderClause = " n." + PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + ", p." + PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI];
    //String orderClause ="";

    int vectSize = PstProvinsi.getCount(whereClause);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        start = ctrlProvinsi.actionList(iCommand, start, vectSize, recordToGet);
    }

    Vector listPropinsi = PstProvinsi.listJoinPro(start, recordToGet, whereClause, orderClause);
    if (listPropinsi.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listPropinsi = PstProvinsi.listJoinPro(start, recordToGet, whereClause, orderClause);
    }
%>
<%
    int idx = FRMQueryString.requestInt(request, "idx");
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmPropinsi.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
            //window.location="#go";
            <%}%>

            /*------------- start location function ---------------*/
            function cmdAdd() {
                document.frmmaster.hidden_id_propinsi.value = "0";
                document.frmmaster.command.value = "<%=Command.ADD%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdAsk(oidPropinsi) {
                document.frmmaster.hidden_id_propinsi.value = oidPropinsi;
                document.frmmaster.command.value = "<%=Command.ASK%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdConfirmDelete(oidPropinsi) {
                document.frmmaster.hidden_id_propinsi.value = oidPropinsi;
                document.frmmaster.command.value = "<%=Command.DELETE%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdSave() {
                document.frmmaster.command.value = "<%=Command.SAVE%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdEdit(oidPropinsi) {
                document.frmmaster.hidden_id_propinsi.value = oidPropinsi;
                document.frmmaster.command.value = "<%=Command.EDIT%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdCancel(oidPropinsi) {
                document.frmmaster.hidden_id_propinsi.value = oidPropinsi;
                document.frmmaster.command.value = "<%=Command.EDIT%>";
                document.frmmaster.prev_command.value = "<%=prevCommand%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function cmdBack() {
                document.frmmaster.command.value = "<%=Command.BACK%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function awal() {
                document.frmmaster.command.value = "<%=Command.FIRST%>";
                document.frmmaster.prev_command.value = "<%=Command.FIRST%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function mundur() {
                document.frmmaster.command.value = "<%=Command.PREV%>";
                document.frmmaster.prev_command.value = "<%=Command.PREV%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function maju() {
                document.frmmaster.command.value = "<%=Command.NEXT%>";
                document.frmmaster.prev_command.value = "<%=Command.NEXT%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
            }

            function akhir() {
                document.frmmaster.command.value = "<%=Command.LAST%>";
                document.frmmaster.prev_command.value = "<%=Command.LAST%>";
                document.frmmaster.action = "provinsi.jsp";
                document.frmmaster.submit();
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
                                <form name="frmmaster" method="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_id_propinsi" value="<%=oidPropinsi%>">		  
                                    <input type="hidden" name="<%=FrmPropinsi.FRM_NAME_PROPINSI%>" method="POST" action="">
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
                                            <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE, iCommand, frmPropinsi, provinsi, listPropinsi, oidPropinsi)%> </td>
                                        </tr>
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
                                                        out.println(ctrLine.drawMeListLimit(cmd, vectSize, start, recordToGet, "awal", "mundur", "maju", "akhir", "left"));
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

                                <%if (iCommand == Command.NONE || iCommand == Command.BACK || iCommand == Command.FIRST
                                            || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>
                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                    <tr>
                                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor " + locationTitle : locationTitle + " Editor"%></u></td>
                        </tr>
                        <tr> 
                            <td height="100%" width="100%" colspan="2"> 
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                        <!--td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td-->
                                        <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                        <!--td height="22" valign="middle" colspan="3"><a href="javascript:cmdAdd()" class="command"> <%=strAdd%></a></td-->
                                        <%if(privAdd){%>
                                        <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdAdd()" class="btn-primary btn-lg"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%></a></td>
                                        <%}%>
                                    </tr>
                                </table>
                                <%}%>
                            </td>
                        </tr>
                        <tr align="left" valign="top" > 
                            <td colspan="2" class="command"> 
                                <%
                                    ctrLine.setTableWidth("80%");
                                    String scomDel = "javascript:cmdAsk('" + oidPropinsi + "')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidPropinsi + "')";
                                    String scancel = "javascript:cmdEdit('" + oidPropinsi + "')";
                                    ctrLine.setCommandStyle("command");

                                    ctrLine.setBackCaption("" + strBack + "");
                                    ctrLine.setDeleteCaption("" + strDelete + "");
                                    ctrLine.setSaveCaption("" + strSave + "");
                                    ctrLine.setAddCaption("" + strAdd + "");

                                    ctrLine.setCancelCaption(strCancel);
                                    ctrLine.setConfirmDelCaption(strConfirmDel);
                                    ctrLine.setSavedInfo(strSaveInfo);
                                    ctrLine.setConfrmDelInfo(strConfirmDelInfo);
                                    ctrLine.setDeleteQuestion(strDelQuest);
                                    ctrLine.setPrevCaption(strPrevCaption);

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

