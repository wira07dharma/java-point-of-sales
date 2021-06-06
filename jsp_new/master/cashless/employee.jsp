
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

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_EMPLOYEE); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!    public static final String textListTitleHeader[][] = {
        {"Nama Lengkap", "Alamat", "Telepon", "Jenis Kelamin", "Tempat Lahir", "Tgl Lahir", "Agama", "Sidik Jari", "Mapping Discount","Posisi"},
        {"Full Name", "Address", "Phone", "Sex", "Birth Place", "Birth Date", "Religion", "Finger Data", "Mapping Discount","Position"}
    };
    public static final String textListTitle[][] = {
        {"Sistem", "Daftar Pegawai", "Tambah Pegawai Baru", "Pencarian"},
        {"System", "Employee List", "Add New Employee", "Search"}
    };
    public static final String textTitleSex[][] = {
        {"Perempuan", "Laki-laki"},
        {"Female", "Male"}
    };

    public String drawListEmployee(int language, Vector objectClass, String baseURL) {

        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListTitleHeader[language][0], "15%");
        ctrlist.addHeader(textListTitleHeader[language][1], "15%");
        ctrlist.addHeader(textListTitleHeader[language][2], "10%");
        ctrlist.addHeader(textListTitleHeader[language][3], "10%");
        ctrlist.addHeader(textListTitleHeader[language][4], "10%");
        ctrlist.addHeader(textListTitleHeader[language][5], "10%");
        ctrlist.addHeader(textListTitleHeader[language][6], "10%");
        ctrlist.addHeader(textListTitleHeader[language][9], "10%");
        ctrlist.addHeader("#", "10%");
        ctrlist.addHeader(textListTitleHeader[language][8], "10%");

        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");

        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();

        for (int i = 0; i < objectClass.size(); i++) {
            Employee entEmployee = (Employee) objectClass.get(i);
            Religion entReligion = new Religion();
            Position entPosition = new Position();
            // get employee max discount
            double maxDiscAmount = 0;
            long maxDiscOid = 0;
            Vector maxDiscount = PstAssignDiscount.list(0, 0, "" + PstAssignDiscount.fieldNames[PstAssignDiscount.FLD_EMPLOYEE_ID] + " = '" + entEmployee.getOID() + "'", "");
            if (!maxDiscount.isEmpty()) {
                AssignDiscount ad = (AssignDiscount) maxDiscount.get(0);
                maxDiscAmount = ad.getMaxDisc();
                maxDiscOid = ad.getOID();
            }

            //get employee Religion 
            try {
                entReligion = PstReligion.fetchExc(entEmployee.getReligionId());
                entPosition = PstPosition.fetchExc(entEmployee.getPositionId());
            } catch (Exception e) {
                System.out.print(e);
            }

            Vector rowx = new Vector();
            rowx.add(String.valueOf(entEmployee.getFullName()));
            rowx.add(String.valueOf(entEmployee.getAddress()));
            rowx.add(String.valueOf(entEmployee.getPhone()));
            rowx.add(String.valueOf(textTitleSex[language][entEmployee.getSex()]));
            rowx.add(String.valueOf(entEmployee.getBirthPlace()));
            rowx.add(String.valueOf(entEmployee.getBirthDate()));
            rowx.add(String.valueOf(entReligion.getReligion()));
            rowx.add(String.valueOf(entPosition.getPosition()));
            rowx.add(String.valueOf("<a href=\"javascript:cmdDetil('" + entEmployee.getOID() + "')\">" + textListTitleHeader[language][7] + "</a>"));
            rowx.add(String.valueOf("<a href=\"javascript:cmdEditMaxDisc('" + entEmployee.getOID() + "','" + maxDiscOid + "','" + maxDiscAmount + "')\">" + maxDiscAmount + "</a>"));

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(entEmployee.getOID()));
        }

        return ctrlist.draw();
    }

%>
<%    /* VARIABLE DECLARATION */
    int recordToGet = 15;
    String order = " " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];
    Vector listEmployee = new Vector(1, 1);
    ControlLine ctrLine = new ControlLine();
	AppUser appUser = userSession.getAppUser();
    String whereClause = "";
	
	if (appUser.getCompanyId() != 0){
		whereClause	= PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] +" = "+appUser.getCompanyId();
	} 

    /* GET REQUEST FROM HIDDEN TEXT */
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    long employeeOID = FRMQueryString.requestLong(request, "employee_oid");
    int listCommand = FRMQueryString.requestInt(request, "list_command");
    String search = FRMQueryString.requestString(request, "search");
    if (listCommand == Command.NONE) {
        listCommand = Command.LIST;
    }

    if (!search.equals("")) {
		if (whereClause.length()>0){
			whereClause += " AND ";
		}
        whereClause += "  (" + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " like '%" + search + "%'";
        whereClause += " or " + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS] + " like '%" + search + "%'";
        whereClause += " or " + PstEmployee.fieldNames[PstEmployee.FLD_PHONE] + " like '%" + search + "%'";
        whereClause += " or " + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE] + " like '%" + search + "%'";
        whereClause += " or " + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " like '%" + search + "%')";
    }

    CtrlEmployee ctrEmployee = new CtrlEmployee(request);

    int vectSize = PstEmployee.getCount(whereClause);
    start = ctrEmployee.actionList(listCommand, start, vectSize, recordToGet);

    listEmployee = PstEmployee.list(start, recordToGet, whereClause, order);

    CtrlAssignDiscount ctrlAssignDiscount = new CtrlAssignDiscount(request);
    long oidAssignDiscount = FRMQueryString.requestLong(request, "assigndisc_oid");
    long oidEmployee = FRMQueryString.requestLong(request, "FRM_FIELD_EMPLOYEE_ID");
    int iErrCode = ctrlAssignDiscount.action(iCommand, oidAssignDiscount, oidEmployee);

%>
<!-- End of JSP Block -->
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <script language="JavaScript">

            function addNew() {
                document.frmEmployee.employee_oid.value = "0";
                document.frmEmployee.list_command.value = "<%=listCommand%>";
                document.frmEmployee.command.value = "<%=Command.ADD%>";
                document.frmEmployee.action = "employee_edit.jsp";
                document.frmEmployee.submit();
            }

            function cmdListFirst() {
                document.frmEmployee.command.value = "<%=Command.FIRST%>";
                document.frmEmployee.list_command.value = "<%=Command.FIRST%>";
                document.frmEmployee.action = "employee.jsp";
                document.frmEmployee.submit();
            }

            function cmdListPrev() {
                document.frmEmployee.command.value = "<%=Command.PREV%>";
                document.frmEmployee.list_command.value = "<%=Command.PREV%>";
                document.frmEmployee.action = "employee.jsp";
                document.frmEmployee.submit();
            }

            function cmdListNext() {
                document.frmEmployee.command.value = "<%=Command.NEXT%>";
                document.frmEmployee.list_command.value = "<%=Command.NEXT%>";
                document.frmEmployee.action = "employee.jsp";
                document.frmEmployee.submit();
            }

            function cmdListLast() {
                document.frmEmployee.command.value = "<%=Command.LAST%>";
                document.frmEmployee.list_command.value = "<%=Command.LAST%>";
                document.frmEmployee.action = "employee.jsp";
                document.frmEmployee.submit();
            }

            function cmdSearch() {
                document.frmEmployee.command.value = "<%=Command.NONE%>";
                document.frmEmployee.list_command.value = "<%=listCommand%>";
                document.frmEmployee.action = "employee.jsp";
                document.frmEmployee.submit();
            }
            function cmdEdit(oid) {
                document.frmEmployee.employee_oid.value = oid;
                document.frmEmployee.command.value = "<%=Command.EDIT%>";
                document.frmEmployee.list_command.value = "<%=listCommand%>";
                document.frmEmployee.action = "employee_edit.jsp";
                document.frmEmployee.submit();
            }

            function cmdDetil(oid) {
                document.frmEmployee.employee_oid.value = oid;
                document.frmEmployee.command.value = "<%=Command.LIST%>";
                document.frmEmployee.action = "master_finger_data.jsp";
                document.frmEmployee.submit();
            }

            function cmdEditMaxDisc(oidEmp, oidAss, maxDiscAmount) {
                document.formmaxdisc.FRM_FIELD_EMPLOYEE_ID.value = oidEmp;
                document.formmaxdisc.assigndisc_oid.value = oidAss;
                document.formmaxdisc.FRM_FIELD_MAX_DISC.value = maxDiscAmount;
                $('#modalmaxdiscount').modal({
                    backdrop: 'static'
                });
                $('#modalmaxdiscount').modal('show');
                $('#btn-savemaxdisc').click(function () {
                    document.formmaxdisc.command.value = "<%=Command.SAVE%>";
                    document.formmaxdisc.action = "employee.jsp";
                    document.formmaxdisc.submit();
                });
            }
        </script>
    </head> 
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> 
                    <%@ include file = "../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU">
                    <%@ include file = "../../main/mnmain.jsp" %>
                </td>
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
                                <%=textListTitle[SESS_LANGUAGE][0]%>
                                > <%=textListTitle[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                            </td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmEmployee" method="post" action="">
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
                                    <input type="hidden" name="employee_oid" value="<%=employeeOID%>">
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
                                    <% if ((listEmployee != null) && (listEmployee.size() > 0)) {%>
                                    <%=drawListEmployee(SESS_LANGUAGE, listEmployee, baseURL)%> 
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
                                </form>

                                <link rel="stylesheet" href="../../css/default.css" type="text/css">
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
                    <!-- #EndEditable --> 
                </td>
            </tr>
        </table>

        <link type="text/css" rel="stylesheet" href="../../styles/bootstrap3.1/css/bootstrap.css">
        <script type="text/javascript" src="../../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap3.1/js/bootstrap.min.js"></script>

        <div class="modal fade" id="modalmaxdiscount" role="dialog">
            <div class="modal-dialog" style="width: 30%">
                <form name="formmaxdisc" method="post" action="">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set Maximum Discount</h4>
                        </div>

                        <div class="modal-body">
                            <div>
                                <input type="hidden" name="command" value="">
                                <input type="hidden" name="FRM_FIELD_EMPLOYEE_ID" value="">
                                <input type="hidden" name="assigndisc_oid" value="">
                                <input type="text" required="" placeholder="Maximum Discount Amount (%)" class="form-control input-sm" name="FRM_FIELD_MAX_DISC" id="FRM_FIELD_MAX_DISC">                                
                            </div>
                        </div>

                        <div class="modal-footer" style="padding: 15px">
                            <button type="submit" id="btn-savemaxdisc" class="btn btn-primary">Save</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
