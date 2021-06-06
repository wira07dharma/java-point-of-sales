<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.harisma.entity.masterdata.Position"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate,
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


<%!
public static final String listTextGlobal[][] = {
    {"Daftar Pegawai","Edit","Harus diiisi"},
    {"Employee List","Edit","Required"}
};

public static final String listTextTableHeader[][] = {
    {"Nama Lengkap","Alamat","Telepon","Jenis Kelamin","Tempat Lahir","Tanggal Lahir","Agama","Pilih","Simpan","Kembali","Posisi","Perusahaan"},
    {"Full Name","Address","Phone","Sex","Birth Place","Birth Date","Religion","Select","Save","Back","Position","Company"}
};

public static final String textTitleSex [][]={
    {"Perempuan","Laki-laki"},
    {"Female","Male"}
};

%>
<%

/* VARIABLE DECLARATION */ 
ControlLine ctrLine = new ControlLine();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
long employeeOid = FRMQueryString.requestLong(request,"employee_oid");

CtrlEmployee ctrlEmployee = new CtrlEmployee(request);
FrmEmployee frmEmployee = ctrlEmployee.getForm();
Employee employee = new Employee();

Religion religion;
int iErrCode = ctrlEmployee.action(iCommand,employeeOid);

// Get Religion List
Vector listReligion = PstReligion.list(0, 0,"","");
Vector listPosition = PstPosition.list(0, 0,"","");
AppUser appUser = userSession.getAppUser();
String whereCompany = "";

if (appUser.getCompanyId() != 0){
	whereCompany = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]+"="+appUser.getCompanyId();
}

Vector listCompany = PstCompany.list(0, 0, whereCompany, "");

if (employeeOid!=0){
    employee = PstEmployee.fetchExc(employeeOid);
}

if (iCommand==Command.SAVE && iErrCode==0){
    response.sendRedirect("employee.jsp");
}

%>

<html>
<head>
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
        function cmdCancel(){
            //document.frmAppGroup.group_oid.value=oid;
            document.frmAppGroup.command.value="<%=Command.EDIT%>";
            document.frmAppGroup.action="groupedit.jsp";
            document.frmAppGroup.submit();
        }

        <% if(privAdd || privUpdate) {%>
            function cmdSave(){
                document.frmEmployee.command.value="<%=Command.SAVE%>";
                document.frmEmployee.employee_oid.value="0";
                document.frmEmployee.action="employee_edit.jsp";
                document.frmEmployee.submit();
            }
        <%}%>

        function cmdBack(){
            window.location="employee.jsp";
        }
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
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
    <%}else{%>
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
                            <%=listTextGlobal[SESS_LANGUAGE][0]%>
                            > <%=listTextGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                        </td>
                    </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form name="frmEmployee" method="post" action="">
                            
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input type="hidden" name="employee_oid" value="<%=employeeOid%>">
                            <table width="100%" cellspacing="0" cellpadding="0">
                                <tr> 
                                    <td colspan="2" class="listtitle">
                                        <hr size="1">
                                        
                                    </td>
                                </tr>
                            </table>
                            <table style="width: 100%">
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][0]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= employee.getFullName()%>" type="text" required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_FULL_NAME]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][1]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <textarea required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_ADDRESS]%>" style="width: 400px; height: 90px;"><%= employee.getAddress()%></textarea> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][2]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= employee.getPhone()%>"  type="text" required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_PHONE]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][3]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <%
                                        if (employee.getSex()==1){
                                        %>
                                            <input checked="checked" required="required" type="radio" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_SEX]%>" value="1"><%= textTitleSex[SESS_LANGUAGE][1]%><br>
                                            <input required="required" type="radio" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_SEX]%>" value="0"><%= textTitleSex[SESS_LANGUAGE][0]%><br>
                                        <%}else{%>
                                            <input required="required" type="radio" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_SEX]%>" value="1"><%= textTitleSex[SESS_LANGUAGE][1]%><br>
                                            <input checked="checked" required="required" type="radio" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_SEX]%>" value="0"><%= textTitleSex[SESS_LANGUAGE][0]%><br>
                                        <%}%>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][4]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= employee.getBirthPlace()%>" type="text" required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_BIRTH_PLACE]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][5]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <%=ControlDate.drawDate(frmEmployee.fieldNames[frmEmployee.FRM_FIELD_BIRTH_DATE], employee.getBirthDate(),"formElemen",  0, -60)%> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][6]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <select required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_RELIGION_ID]%>"> 
                                            <option value="">-- <%= listTextTableHeader[SESS_LANGUAGE][7]%> <%= listTextTableHeader[SESS_LANGUAGE][6]%> --</option>
                                            <%
                                            for (int i=0;i<listReligion.size();i++){
                                                religion = (Religion)listReligion.get(i);
                                                if (employee.getReligionId()==religion.getOID()){
                                                    out.println("<option selected='selected' value='"+religion.getOID()+"'>"+religion.getReligion()+"</option>");
                                                }else{
                                                    out.println("<option value='"+religion.getOID()+"'>"+religion.getReligion()+"</option>");
                                                }
                                                
                                            }    
                                            %>
                                        </select>
                                    </td>
                                </tr>
								<tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][10]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <select required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_POSITION_ID]%>"> 
                                            <option value="">-- <%= listTextTableHeader[SESS_LANGUAGE][7]%> <%= listTextTableHeader[SESS_LANGUAGE][10]%> --</option>
                                            <%
                                            for (int i=0;i<listPosition.size();i++){
                                                Position p = (Position)listPosition.get(i);
                                                if (employee.getPositionId()==p.getOID()){
                                                    out.println("<option selected='selected' value='"+p.getOID()+"'>"+p.getPosition()+"</option>");
                                                }else{
                                                    out.println("<option value='"+p.getOID()+"'>"+p.getPosition()+"</option>");
                                                }
                                                
                                            }    
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][11]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <select required="required" name="<%= frmEmployee.fieldNames[frmEmployee.FRM_FIELD_COMPANY_ID]%>"> 
                                            <%
                                            for (int i=0;i<listCompany.size();i++){
                                                Company c = (Company)listCompany.get(i);
                                                if (employee.getCompanyId()==c.getOID()){
                                                    out.println("<option selected='selected' value='"+c.getOID()+"'>"+c.getCompanyName()+"</option>");
                                                }else{
                                                    out.println("<option value='"+c.getOID()+"'>"+c.getCompanyName()+"</option>");
                                                }
                                                
                                            }    
                                            %>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%">&nbsp; </td>
                                    <td style="width : 2%"></td>
                                    <td>
                                        <table>
                                            <tr>
                                                <%if((iCommand == Command.ADD && privAdd) || (iCommand == Command.EDIT && privUpdate)){%>
                                                <input type="submit" class="btn btn-lg btn-primary" value="<%= listTextTableHeader[SESS_LANGUAGE][8]%>">
                                                <%}%>
                                                <button style="margin-top: 2px" onclick="cmdBack()" class="btn btn-lg btn-primary" type="button"><i class="fa fa-arrow-left"></i> <%= listTextTableHeader[SESS_LANGUAGE][9]%></button>
                                            </tr>
                                        </table>
                                        
                                    </td>
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
            <%if(menuUsed == MENU_ICON){%>
                <%@include file="../../styletemplate/footer.jsp" %>
            <%}else{%>
                <%@ include file = "../../main/footer.jsp" %>
            <%}%>
        <!-- #EndEditable --> 
        </td>
    </tr>
</table>
</body>
</html>
