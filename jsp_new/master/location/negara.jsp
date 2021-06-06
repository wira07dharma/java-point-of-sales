<%-- 
    Document   : negara
    Created on : Nov 15, 2011, 3:14:41 PM
    Author     : Wiweka
--%>



<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package HRIS -->
<%@ page import = "com.dimata.common.entity.masterdata.*" %>
<%@ page import = "com.dimata.common.form.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
            /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!    public String drawList(int iCommand, FrmNegara frmObject, Negara objEntity, Vector objectClass, long idNegara) {
        String result = "";

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("40%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("Continent", "30%");
        ctrlist.addHeader("Country Name", "70%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.reset();
        Vector rowx = new Vector(1, 1);
        int index = -1;
        String kdNegara = objEntity.getBenua();

        if (objectClass != null && objectClass.size() > 0) {
            for (int i = 0; i < objectClass.size(); i++) {
                Negara negara = (Negara) objectClass.get(i);
                if (idNegara == negara.getOID()) {
                    index = i;
                }

                rowx = new Vector();
                if ((index == i) && (iCommand == Command.EDIT || iCommand == Command.ASK)) {
                    rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_BENUA] + "\" value=\"" + negara.getBenua() + "\" class=\"formElemen\" size=\"10\">");
                    rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_NM_NEGARA] + "\" value=\"" + negara.getNmNegara() + "\" class=\"formElemen\" size=\"40\">");
                } else {
                    rowx.add(String.valueOf(negara.getBenua()));
                    rowx.add("<a href=\"javascript:cmdEdit('" + String.valueOf(negara.getOID()) + "')\">" + negara.getNmNegara() + "</a>");
                }
                lstData.add(rowx);
            }
            rowx = new Vector();

            if (iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)) {
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_BENUA] + "\" value=\"" + kdNegara + "\" class=\"formElemen\" size=\"10\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_NM_NEGARA] + "\" value=\"" + objEntity.getNmNegara() + "\" class=\"formElemen\" size=\"40\">");
            }
            lstData.add(rowx);
            result = ctrlist.draw();
        } else {
            if (iCommand == Command.ADD) {
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_BENUA] + "\" value=\"" + kdNegara + "\" class=\"formElemen\" size=\"10\">");
                rowx.add("<input type=\"text\" name=\"" + frmObject.fieldNames[FrmNegara.FRM_FIELD_NM_NEGARA] + "\" value=\"" + objEntity.getNmNegara() + "\" class=\"formElemen\" size=\"40\">");
                lstData.add(rowx);
                result = ctrlist.draw();
            } else {
                result = "<i>No country list on database ...</i>";
            }
        }
        return result;
    }
%>


<%
// request data from jsp page
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidNegara = FRMQueryString.requestLong(request, "hidden_id_negara");

// variable declaration
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;

// instantiate Negara classes
            CtrlNegara ctrlNegara = new CtrlNegara(request);
            ControlLine ctrLine = new ControlLine();

// set text command based on indonesia language
            String strNegara = "Country";
            String strSave = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_SAVE, true);
            String strDelete = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_ASK, true);
            String strCancel = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_CANCEL, false);
            String strConfirmDel = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_DELETE, true);
            String strSaveInfo = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_SAVE_SUCCESS, false);
            String strConfirmDelInfo = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_DELETE_SUCCESS, false);
            String strDelQuest = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_DELETE_QUESTION, false);
            String strBack = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_BACK, true);
            String strAdd = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_ADD, true);
            String strPrevCaption = ctrLine.getCommand(SESS_LANGUAGE, strNegara, ctrLine.CMD_PREV, false);

// action on object negara defend on command entered
            iErrCode = ctrlNegara.action(iCommand, oidNegara);
            FrmNegara frmNegara = ctrlNegara.getForm();
            Negara negara = ctrlNegara.getNegara();
            msgString = ctrlNegara.getMessage();

// get records to display
            String whereClause = "";
            String orderClause = PstNegara.fieldNames[PstNegara.FLD_BENUA];

            int vectSize = PstNegara.getCount(whereClause);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                start = ctrlNegara.actionList(iCommand, start, vectSize, recordToGet);
            }

            Vector listNegara = PstNegara.list(start, recordToGet, whereClause, orderClause);
            if (listNegara.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listNegara = PstNegara.list(start, recordToGet, whereClause, orderClause);
            }
%>
<%
            int idx = FRMQueryString.requestInt(request, "idx");
%>

<!-- JSP Block -->
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Harisma - Master Country List</title>

        <script language="JavaScript">
            function cmdAdd(){                                
                document.frmmaster.hidden_id_negara.value="0";
                //alert(1);
                document.frmmaster.command.value="<%=Command.ADD%>";
                //alert(2);
                //document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                //alert(3);
                document.frmmaster.submit();
            }

            function cmdAsk(oidNegara){
                document.frmmaster.hidden_id_negara.value=oidNegara;
                document.frmmaster.command.value="<%=Command.ASK%>";
                document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function cmdConfirmDelete(oidNegara){
                document.frmmaster.hidden_id_negara.value=oidNegara;
                document.frmmaster.command.value="<%=Command.DELETE%>";
                document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function cmdSave(){
                document.frmmaster.command.value="<%=Command.SAVE%>";
                document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function cmdEdit(oidNegara){
                document.frmmaster.hidden_id_negara.value=oidNegara;
                document.frmmaster.command.value="<%=Command.EDIT%>";
                document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function cmdCancel(oidNegara){
                document.frmmaster.hidden_id_negara.value=oidNegara;
                document.frmmaster.command.value="<%=Command.EDIT%>";
                document.frmmaster.prev_command.value="<%=prevCommand%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function cmdBack(){
                document.frmmaster.command.value="<%=Command.BACK%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function awal(){
                document.frmmaster.command.value="<%=Command.FIRST%>";
                document.frmmaster.prev_command.value="<%=Command.FIRST%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function mundur(){
                document.frmmaster.command.value="<%=Command.PREV%>";
                document.frmmaster.prev_command.value="<%=Command.PREV%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function maju(){
                document.frmmaster.command.value="<%=Command.NEXT%>";
                document.frmmaster.prev_command.value="<%=Command.NEXT%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }

            function akhir(){
                document.frmmaster.command.value="<%=Command.LAST%>";
                document.frmmaster.prev_command.value="<%=Command.LAST%>";
                document.frmmaster.action="negara.jsp";
                document.frmmaster.submit();
            }
        </script>
        <!-- #EndEditable -->
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
            function hideObjectForEmployee(){
            }

            function hideObjectForLockers(){
            }

            function hideObjectForCanteen(){
            }

            function hideObjectForClinic(){
            }

            function hideObjectForMasterdata(){
            }

        </SCRIPT>
        <!-- #EndEditable -->
        <!-- #EndEditable -->
    </head>

    <body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
             <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
            <tr>
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">
                    <!-- #BeginEditable "header" -->
                      <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable -->
                </td>
            </tr>
            <tr>
                <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" -->
                   <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td  bgcolor="#9BC1FF" height="10" valign="middle">

                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td align="left"><img src="<%=approot%>/images/harismaMenuLeft1.jpg" width="8" height="8"></td>
                            <td align="center" background="<%=approot%>/images/harismaMenuLine1.jpg" width="100%"><img src="<%=approot%>/images/harismaMenuLine1.jpg" width="8" height="8"></td>
                            <td align="right"><img src="<%=approot%>/images/harismaMenuRight1.jpg" width="8" height="8"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%}%>
            <tr>
                <td width="88%" valign="top" align="left">
                    <table width="100%" border="0" cellspacing="3" cellpadding="2">
                        <tr>
                            <td width="100%">
                                <table  width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td height="20">
                                            <font color="#FF6600" face="Arial"><strong>
                                                    <!-- #BeginEditable "contenttitle" -->
                                                    Master Data &gt; Country<!-- #EndEditable -->
                                                </strong></font>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td  style="background-color:<%=bgColorContent%>; ">
                                                        <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                                            <tr>
                                                                <td valign="top">
                                                                    <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                                                                        <tr>
                                                                            <td valign="top">
                                                                                <!-- #BeginEditable "content" -->
                                                                                <form name="frmmaster" method="post" action="">

                                                                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                                                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                                                                    <input type="hidden" name="start" value="<%=start%>">
                                                                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                                                                    <input type="hidden" name="hidden_id_negara" value="<%=oidNegara%>">
                                                                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                                        <tr>
                                                                                            <td height="14" valign="middle" colspan="3" class="listtitle">Country List</td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td><%= drawList(iCommand, frmNegara, negara, listNegara, oidNegara)%></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
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
                                                                                                            out.println(ctrLine.drawMeListLimit(cmd, vectSize, start, recordToGet, "First", "Back", "Forward", "End", "Left"));
                                                                                                %>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td height="20">
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td>
                                                                                                <%if (iCommand == Command.NONE || iCommand == Command.BACK || iCommand == Command.FIRST
                                                                                                                                || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {%>
                                                                                                <table cellpadding="0" cellspacing="0" border="0">
                                                                                                    <tr>
                                                                                                        <td>&nbsp;</td>
                                                                                                    </tr>
                                                                                                    <tr>
                                                                                                        <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                                        <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                                                                        <td width="2"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                                        <td height="22" valign="middle" colspan="3"><a href="javascript:cmdAdd()" class="command"> <%=strAdd%></a></td>

                                                                                                    </tr>
                                                                                                </table>
                                                                                                <%}%>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <tr align="left" valign="top" >
                                                                                            <td colspan="3" class="command">
                                                                                                <%
                                                                                                            ctrLine.setTableWidth("80%");
                                                                                                            String scomDel = "javascript:cmdAsk('" + oidNegara + "')";
                                                                                                            String sconDelCom = "javascript:cmdConfirmDelete('" + oidNegara + "')";
                                                                                                            String scancel = "javascript:cmdEdit('" + oidNegara + "')";
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

                                                                                                            if (privAdd == false && privUpdate == false) {
                                                                                                                ctrLine.setSaveCaption("");
                                                                                                            }

                                                                                                            if (privAdd == false) {
                                                                                                                ctrLine.setAddCaption("");
                                                                                                            }
                                                                                                %>
                                                                                                <%= ctrLine.draw(iCommand, iErrCode, msgString)%> </td>
                                                                                        </tr>
                                                                                    </table>

                                                                                </form>
                                                                                <!-- #EndEditable -->
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
            <tr>
                            <td valign="bottom">
                               <!-- untuk footer -->
                              <%@include file="../../styletemplate/footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
        <%@ include file = "../../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
        </table>
    </body>
    <!-- #BeginEditable "script" -->
    <script language="JavaScript">
        //var oBody = document.body;
        //var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
    <!-- #EndEditable -->
    <!-- #EndTemplate --></html>

