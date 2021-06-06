<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.gui.jsp.ControlDate
                   
                   " 
%>

<%@page import="com.dimata.common.entity.finger.DeviceFinger" %>
<%@page import="com.dimata.common.entity.finger.PstDeviceFinger"%>
<%@page import="com.dimata.common.form.finger.CtrlDeviceFinger" %>
<%@page import="com.dimata.common.form.finger.FrmDeviceFinger" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_GROUP); %>
<%@ include file = "../main/checkuser.jsp" %>


<%!
public static final String listTextGlobal[][] = {
    {"Daftar Finger Device","Edit","Harus diiisi"},
    {"Finger Device List","Edit","Required"}
};

public static final String listTextTableHeader[][] = {
    {"Nama Device","Serial Number","Kode Verifikasi","Kode Aktivasi","V Key","Alamat Mesin","Simpan","Kembali"},
    {"Device Name","Serial Number","Verification Code","Activation Code","V Key","Mac Address","Save","Back"}
};

%>
<%

/* VARIABLE DECLARATION */ 
ControlLine ctrLine = new ControlLine();

/* GET REQUEST FROM HIDDEN TEXT */
int iCommand = FRMQueryString.requestCommand(request);
long deviceOid = FRMQueryString.requestLong(request,"device_oid");

CtrlDeviceFinger ctrlDeviceFinger = new CtrlDeviceFinger(request);
FrmDeviceFinger frmDeviceFinger = ctrlDeviceFinger.getForm();
DeviceFinger deviceFinger = new DeviceFinger();

int iErrCode = ctrlDeviceFinger.action(iCommand,deviceOid);

if (deviceOid!=0){
    deviceFinger = PstDeviceFinger.fetchExc(deviceOid);
}

if (iCommand==Command.SAVE && iErrCode==0){
    response.sendRedirect("master_finger_device.jsp");
}

%>

<html>
<head>
    <title>Dimata - ProChain POS</title>
    <script language="JavaScript">
       
        <% if(privAdd || privUpdate) {%>
            function cmdSave(){
                document.frmFingerDevice.command.value="<%=Command.SAVE%>";
                document.frmFingerDevice.employee_oid.value="0";
                document.frmFingerDevice.action="finger_device_edit.jsp";
                document.frmFingerDevice.submit();
            }
        <%}%>

        function cmdBack(){
            window.location="master_finger_device.jsp";
        }
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../styles/tab.css" type="text/css">
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
    <%if(menuUsed == MENU_PER_TRANS){%>
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
    <%}else{%>
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
                            <%=listTextGlobal[SESS_LANGUAGE][0]%>
                            > <%=listTextGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                        </td>
                    </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form name="frmFingerDevice" method="post" action="finger_device_edit.jsp">
                            
                            <input type="hidden" name="command" value="<%=Command.SAVE%>">
                            <input type="hidden" name="device_oid" value="<%=deviceOid%>">
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
                                        <input value="<%= deviceFinger.getDeviceName()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_DEVICE_NAME]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][1]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= deviceFinger.getSn()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_SN]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][2]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= deviceFinger.getVc()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_VC]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][3]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= deviceFinger.getAc()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_AC]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][4]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= deviceFinger.getvKey()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_VKEY]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width : 15%"><%= listTextTableHeader[SESS_LANGUAGE][5]%></td>
                                    <td style="width : 2%">:</td>
                                    <td>
                                        <input value="<%= deviceFinger.getMacAddress()%>" type="text" required="required" name="<%= frmDeviceFinger.fieldNames[frmDeviceFinger.FRM_FLD_MAC_ADDRESS]%>" style="width:300px;" /> 
                                    </td>
                                </tr>
                                
                                
                                <tr>
                                    <td style="width : 15%">&nbsp; </td>
                                    <td style="width : 2%"></td>
                                    <td>
                                        <table>
                                            <tr>
                                   
                                                <input type="submit" value="<%= listTextTableHeader[SESS_LANGUAGE][6]%>">
                                                <button style="margin-top: 2px" onclick="cmdBack()" type="button"><%= listTextTableHeader[SESS_LANGUAGE][7]%></button>
                                            </tr>
                                        </table>
                                        
                                    </td>
                                </tr>
                            </table>

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
            <%if(menuUsed == MENU_ICON){%>
                <%@include file="../styletemplate/footer.jsp" %>
            <%}else{%>
                <%@ include file = "../main/footer.jsp" %>
            <%}%>
        <!-- #EndEditable --> 
        </td>
    </tr>
</table>
</body>
</html>
