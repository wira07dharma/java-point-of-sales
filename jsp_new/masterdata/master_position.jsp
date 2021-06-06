<%-- 
    Document   : master_position
    Created on : Dec 13, 2017, 2:51:50 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.form.FrmPosition"%>
<%@page import="com.dimata.harisma.form.CtrlPosition"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@page import="com.dimata.harisma.entity.masterdata.Position"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlAssignDiscount"%>
<%@page import="com.dimata.posbo.entity.masterdata.AssignDiscount"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstAssignDiscount"%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
         com.dimata.gui.jsp.ControlList,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.util.Command,
         com.dimata.harisma.entity.employee.Employee,
         com.dimata.harisma.entity.employee.PstEmployee,
         com.dimata.harisma.form.CtrlEmployee,
         com.dimata.harisma.form.FrmEmployee,
         com.dimata.harisma.entity.masterdata.Religion,
         com.dimata.harisma.entity.masterdata.PstReligion
         "
         %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_POSITION); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!    public static final String textListTitleHeader[][] = {
        {"Nama Posisi", "Keterangan", "Persentase Insentif", "Level"},
        {"Position Name", "Description", "Insentive Percentage", "Level"}
    };
    public static final String textListTitle[][] = {
        {"Sistem", "Daftar Posisi", "Tambah Posisi Baru", "Pencarian"},
        {"System", "Position List", "Add New Position", "Search"}
    };

    public String drawListPosition(int language, Vector objectClass, String baseURL) {

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("70%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListTitleHeader[language][0], "30");
        ctrlist.addHeader(textListTitleHeader[language][1], "30%");
        ctrlist.addHeader(textListTitleHeader[language][2], "30%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();

        for (int i = 0; i < objectClass.size(); i++) {
            Position entPosition = (Position) objectClass.get(i);

            Vector rowx = new Vector();
            rowx.add(String.valueOf(entPosition.getPosition()));
            rowx.add(String.valueOf(entPosition.getDescription()));
            rowx.add(String.valueOf(entPosition.getPersentaseInsentif()));

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(entPosition.getOID()));
        }

        return ctrlist.draw();
    }

%>

<%    /* VARIABLE DECLARATION */
    int recordToGet = 10;
    String order = " " + PstPosition.fieldNames[PstPosition.FLD_POSITION];
    Vector listPosition = new Vector(1, 1);
    ControlLine ctrLine = new ControlLine();
    String whereClause = "";

    /* GET REQUEST FROM HIDDEN TEXT */
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    long positionOID = FRMQueryString.requestLong(request, "position_oid");
    int listCommand = FRMQueryString.requestInt(request, "list_command");
    String search = FRMQueryString.requestString(request, "search");
    if (listCommand == Command.NONE) {
        listCommand = Command.LIST;
    }

    Position position = new Position();
    if (positionOID != 0) {
        try {
            position = PstPosition.fetchExc(positionOID);
        } catch (Exception e) {

        }
    }

    if (!search.equals("")) {
        whereClause = " " + PstPosition.fieldNames[PstPosition.FLD_POSITION] + " like '%" + search + "%'";
        whereClause += " or " + PstPosition.fieldNames[PstPosition.FLD_DESCRIPTION] + " like '%" + search + "%'";
        whereClause += " or " + PstPosition.fieldNames[PstPosition.FLD_PERSENTASE_INSENTIF] + " like '%" + search + "%'";
    }

    CtrlPosition ctrPosition = new CtrlPosition(request);
    int iErrCode = ctrPosition.action(iCommand, positionOID);

    int vectSize = PstPosition.getCount(whereClause);
    start = ctrPosition.actionList(listCommand, start, vectSize, recordToGet);

    listPosition = PstPosition.list(start, recordToGet, whereClause, order);

%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../styles/main.css" type="text/css">
        <script language="JavaScript">

            function addNew() {
                document.frmPosition.position_oid.value = "0";
                document.frmPosition.list_command.value = "<%=listCommand%>";
                document.frmPosition.command.value = "<%=Command.ADD%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdListFirst() {
                document.frmPosition.command.value = "<%=Command.FIRST%>";
                document.frmPosition.list_command.value = "<%=Command.FIRST%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdListPrev() {
                document.frmPosition.command.value = "<%=Command.PREV%>";
                document.frmPosition.list_command.value = "<%=Command.PREV%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdListNext() {
                document.frmPosition.command.value = "<%=Command.NEXT%>";
                document.frmPosition.list_command.value = "<%=Command.NEXT%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdListLast() {
                document.frmPosition.command.value = "<%=Command.LAST%>";
                document.frmPosition.list_command.value = "<%=Command.LAST%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdSearch() {
                document.frmPosition.command.value = "<%=Command.NONE%>";
                document.frmPosition.list_command.value = "<%=listCommand%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }
            function cmdEdit(oid) {
                document.frmPosition.position_oid.value = oid;
                document.frmPosition.command.value = "<%=Command.EDIT%>";
                document.frmPosition.list_command.value = "<%=listCommand%>";
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdBack() {
                window.location = "master_position.jsp";
            }

            function cmdSave(oid) {
                document.frmPosition.command.value = "<%=Command.SAVE%>";
                document.frmPosition.position_oid.value = oid;
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

            function cmdDelete(oid) {
                document.frmPosition.command.value = "<%=Command.DELETE%>";
                document.frmPosition.position_oid.value = oid;
                document.frmPosition.action = "master_position.jsp";
                document.frmPosition.submit();
            }

        </script>
    </head> 
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> 
                    <%@ include file = "../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU">
                    <%@ include file = "../main/mnmain.jsp" %>
                </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                <%=textListTitle[SESS_LANGUAGE][0]%>
                                > <%=textListTitle[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                            </td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmPosition" method="post" action="">
                                    <table style="width:100%">
                                        <tr>
                                            <td style="width:20%">
                                                <input type="text" name="search" value="<%=search%>" placeholder="<%=textListTitle[SESS_LANGUAGE][3]%>..." style="width: 100%;">

                                            </td>
                                            <td>
                                                <!--a href="javascript:cmdSearch()"><img name="Image10011" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24"></a-->
                                                <a class="btn btn-primary" href="javascript:cmdSearch()" class="command"><i class="fa fa-search"></i> </a>
                                            </td>
                                        </tr>
                                    </table>

                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="position_oid" value="<%=positionOID%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="list_command" value="<%=listCommand%>">
                                    <table width="100%" cellspacing="0" cellpadding="0">
                                        <tr> 
                                            <td colspan="2" class="listtitle">
                                                <hr size="1">

                                            </td>
                                        </tr>
                                    </table>
                                    <% if ((listPosition != null) && (listPosition.size() > 0)) {%>
                                    <%=drawListPosition(SESS_LANGUAGE, listPosition, baseURL)%> 
                                    <%}%>                                    
                                    <table width="100%">
                                        <tr> 
                                            <td colspan="2"> 
                                                <span class="command"> 
                                                    <%
                                                        int cmd = 0;
                                                        if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                                                || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                                            cmd = iCommand;
                                                        } else {
                                                            if (iCommand == Command.NONE) {
                                                                cmd = Command.FIRST;
                                                            }
                                                        }
                                                        ctrLine.setLocationImg(approot + "/images");
                                                        ctrLine.initDefault();
                                                    %>
                                                    <%=ctrLine.drawMeListLimitBootstrap(cmd, vectSize, start, recordToGet, "cmdListFirst", "cmdListPrev", "cmdListNext", "cmdListLast", "left")%>
                                                </span>				  
                                            </td>
                                        </tr>
                                        <% if (privAdd) {%>
                                        <tr valign="middle"> 
                                            <td colspan="2" class="command"> 
                                                <table width="15%" border="0" cellspacing="2" cellpadding="2">
                                                    <tr> 
                                                        <!--td width="20%">
                                                            <a href="javascript:addNew()"><img name="Image10011" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Tambah Baru User"></a>
                                                        </td>
                                                        <td nowrap width="80%"><a href="javascript:addNew()" class="command">
                                                        <%=textListTitle[SESS_LANGUAGE][2]%></a>
                                                </td-->
                                                        <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:addNew()" class="btn-primary btn"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, "", ctrLine.CMD_ADD, true)%></a></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%}%>
                                        <tr> 
                                            <td width="13%">&nbsp;</td>
                                            <td width="87%">&nbsp;</td>
                                        </tr>
                                    </table>
                                    <%if (iCommand == Command.ADD || iCommand == Command.EDIT) {%>
                                    <hr style="border-color: lightgray">
                                    <table width="40%">
                                        <tr>
                                            <td><label><%= textListTitleHeader[SESS_LANGUAGE][0]%></label></td>
                                            <td>:</td>
                                            <td><input type="text" name="<%= FrmPosition.fieldNames[FrmPosition.FRM_FIELD_POSITION]%>" value="<%=position.getPosition()%>"></td>
                                        </tr>
                                        <tr>
                                            <td><label><%= textListTitleHeader[SESS_LANGUAGE][1]%></label></td>
                                            <td>:</td>
                                            <td><input type="text" name="<%= FrmPosition.fieldNames[FrmPosition.FRM_FIELD_DESCRIPTION]%>" value="<%=position.getDescription()%>"></td>
                                        </tr>
                                        <tr>
                                            <td><label><%= textListTitleHeader[SESS_LANGUAGE][2]%></label></td>
                                            <td>:</td>
                                            <td><input type="text" name="<%= FrmPosition.fieldNames[FrmPosition.FRM_FIELD_PERSENTASE_INSENTIF]%>" value="<%=position.getPersentaseInsentif()%>"></td>
                                        </tr>
                                        <tr>
                                            <td><label><%= textListTitleHeader[SESS_LANGUAGE][3]%></label></td>
                                            <td>:</td>
                                            <%
                                                Vector levelKey = new Vector(1,1);
                                                Vector levelValue = new Vector(1,1);
                                                for(int idx=0; idx < PstPosition.strPositionLevelNames.length;idx++){																							
                                                     levelKey.add(PstPosition.strPositionLevelNames[idx]);
                                                    levelValue.add(""+idx);														
                                                }
                                            %>
                                            <td><%=ControlCombo.draw(FrmPosition.fieldNames[FrmPosition.FRM_FIELD_POSITION_LEVEL], "formElemen", null, ""+position.getPositionLevel(), levelValue, levelKey)%></td>
                                        </tr>
                                    </table>
                                    <br><br><br>
                                    <table width="50%">
                                        <tr>
                                            <td>
                                                <%if((iCommand == Command.ADD && privAdd) || (iCommand == Command.EDIT && privUpdate)){%>
                                                <button type="button" class="btn-primary btn btn-sm" onclick="cmdSave('<%=positionOID%>')" style="margin-left: 0px;">Simpan</button>
                                                <%}%>
                                                <button type="button" class="btn-primary btn btn-sm" onclick="cmdBack()">Kembali</button>
                                                <% if (positionOID != 0) {%>
                                                <%if(privDelete){%>
                                                <button type="button" class="btn-danger btn btn-sm" onclick="cmdDelete('<%=positionOID%>')">Hapus</button>
                                                <%}%>
                                                <%}%>
                                            </td>
                                        </tr>                                    
                                    </table>
                                    <%}%>
                                </form>

                                <link rel="stylesheet" href="../css/default.css" type="text/css">
                                <!-- #EndEditable -->
                            </td> 
                        </tr> 
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if (menuUsed == MENU_ICON) {%>
                    <%@include file="../styletemplate/footer.jsp" %>
                    <%} else {%>
                    <%@ include file = "../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>

        <link type="text/css" rel="stylesheet" href="../styles/bootstrap3.1/css/bootstrap.css">
        <script type="text/javascript" src="../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../styles/bootstrap3.1/js/bootstrap.min.js"></script>

    </body>
</html>
