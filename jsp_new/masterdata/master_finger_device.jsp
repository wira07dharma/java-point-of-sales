<%@ page language="java" %>
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command
                   "
%>
<%@page import="com.dimata.common.entity.finger.DeviceFinger" %>
<%@page import="com.dimata.common.entity.finger.PstDeviceFinger"%>
<%@page import="com.dimata.common.form.finger.CtrlDeviceFinger" %>
			    
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_ADMIN , AppObjInfo.G2_ADMIN_USER, AppObjInfo.OBJ_ADMIN_USER_USER); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- JSP Block -->
<%!
public static final String textListTitleHeader[][] ={ 
    {"Nama Device","Serial Number","Kode Verifikasi","Kode Aktivasi","V Key","Alamat Mesin"},
    {"Device Name","Serial Number","Verification Code","Activation Code","V Key","Mac Address"}
};
public static final String textListTitle[][]={
    {"Sistem","Daftar Finger Device ","Tambah Device Baru","Pencarian"},
    {"System","Finger Device List","Add New Device","Search"}   
};


public String drawListDeviceFinger(int language,Vector objectClass,String baseURL){
    
    ControlList ctrlist = new ControlList();
    ctrlist.setAreaWidth("90%");
    ctrlist.setListStyle("listgen");
    ctrlist.setTitleStyle("listgentitle");
    ctrlist.setCellStyle("listgensell");
    ctrlist.setHeaderStyle("listgentitle");
    
    ctrlist.addHeader(textListTitleHeader[language][0],"20%");
    ctrlist.addHeader(textListTitleHeader[language][1],"20%");
    ctrlist.addHeader(textListTitleHeader[language][2],"10%");
    ctrlist.addHeader(textListTitleHeader[language][3],"10%");
    ctrlist.addHeader(textListTitleHeader[language][4],"10%");
    ctrlist.addHeader(textListTitleHeader[language][5],"10%");
    
    ctrlist.setLinkRow(0);
    ctrlist.setLinkSufix("");
    
    Vector lstData = ctrlist.getData();
    Vector lstLinkData 	= ctrlist.getLinkData();

    ctrlist.setLinkPrefix("javascript:cmdEdit('");
    ctrlist.setLinkSufix("')");
    ctrlist.reset();

    for (int i = 0; i < objectClass.size(); i++) {
        DeviceFinger entDeviceFinger = (DeviceFinger)objectClass.get(i);
       
        Vector rowx = new Vector();
        rowx.add(String.valueOf(entDeviceFinger.getDeviceName()));
        rowx.add(String.valueOf(entDeviceFinger.getSn()));
        rowx.add(String.valueOf(entDeviceFinger.getVc()));
        rowx.add(String.valueOf(entDeviceFinger.getAc()));
        rowx.add(String.valueOf(entDeviceFinger.getvKey()));
        rowx.add(String.valueOf(entDeviceFinger.getMacAddress()));
         
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(entDeviceFinger.getOID()));
    }						

    return ctrlist.draw();
}

%>
<%

    /* VARIABLE DECLARATION */
    int recordToGet = 15;
    String order = " " + PstDeviceFinger.fieldNames[PstDeviceFinger.FLD_DEVICE_NAME];
    Vector listDeviceFinger = new Vector(1,1);
    ControlLine ctrLine = new ControlLine();
    String whereClause = "";

    /* GET REQUEST FROM HIDDEN TEXT */
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start"); 
    long deviceOID = FRMQueryString.requestLong(request,"device_oid");
    int listCommand = FRMQueryString.requestInt(request, "list_command");
    String search = FRMQueryString.requestString(request, "search");
    if(listCommand==Command.NONE)
        listCommand = Command.LIST;
   
        
    CtrlDeviceFinger ctrlDeviceFinger = new CtrlDeviceFinger(request);

    int vectSize = PstDeviceFinger.getCount(""); 
    start = ctrlDeviceFinger.actionList(listCommand, start,vectSize,recordToGet);

    listDeviceFinger = PstDeviceFinger.list(start,recordToGet, "" , order);

%>
<!-- End of JSP Block -->
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <%if(menuUsed == MENU_ICON){%>
        <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
    <%}%>
    <link rel="stylesheet" href="../styles/main.css" type="text/css">
    <link rel="stylesheet" href="../styles/tab.css" type="text/css">
    <script language="JavaScript">
       
        function addNew(){
            document.frmFingerDevice.device_oid.value="0";
            document.frmFingerDevice.list_command.value="<%=listCommand%>";
            document.frmFingerDevice.command.value="<%=Command.ADD%>";
            document.frmFingerDevice.action="finger_device_edit.jsp";
            document.frmFingerDevice.submit();
        }
        
        function cmdListFirst(){
            document.frmFingerDevice.command.value="<%=Command.FIRST%>";
            document.frmFingerDevice.list_command.value="<%=Command.FIRST%>";
            document.frmFingerDevice.action="master_finger_device.jsp";
            document.frmFingerDevice.submit();
        }

        function cmdListPrev(){
            document.frmFingerDevice.command.value="<%=Command.PREV%>";
            document.frmFingerDevice.list_command.value="<%=Command.PREV%>";
            document.frmFingerDevice.action="master_finger_device.jsp";
            document.frmFingerDevice.submit();
        }

        function cmdListNext(){
            document.frmFingerDevice.command.value="<%=Command.NEXT%>";
            document.frmFingerDevice.list_command.value="<%=Command.NEXT%>";
            document.frmFingerDevice.action="master_finger_device.jsp";
            document.frmFingerDevice.submit();
        }

        function cmdListLast(){
            document.frmFingerDevice.command.value="<%=Command.LAST%>";
            document.frmFingerDevice.list_command.value="<%=Command.LAST%>";
            document.frmFingerDevice.action="master_finger_device.jsp";
            document.frmFingerDevice.submit();
        }
        
        function cmdEdit(oid){
            document.frmFingerDevice.device_oid.value=oid;
            document.frmFingerDevice.command.value="<%=Command.EDIT%>";
            document.frmFingerDevice.list_command.value="<%=listCommand%>";
            document.frmFingerDevice.action="finger_device_edit.jsp";
            document.frmFingerDevice.submit();
        }
        
        
    </script>
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
                            <%=textListTitle[SESS_LANGUAGE][0]%>
                            > <%=textListTitle[SESS_LANGUAGE][1]%><!-- #EndEditable -->
                        </td>
                    </tr>
                <tr> 
                    <td><!-- #BeginEditable "content" --> 
                        <form name="frmFingerDevice" method="post" action="">
                            <input type="hidden" name="command" value="">
                            <input type="hidden" name="device_oid" value="<%=deviceOID%>">
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
                            <% if ((listDeviceFinger!=null)&&(listDeviceFinger.size()>0)){ %>
                            <%=drawListDeviceFinger(SESS_LANGUAGE, listDeviceFinger,baseURL)%> 
                            <%}%>
                            <table width="100%">
                                <tr> 
                                    <td colspan="2"> 
                                        <span class="command"> 
                                            <% 
                                             int cmd = 0;
                                             if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
                                                (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                    cmd =iCommand; 
                                             else{
                                                if(iCommand == Command.NONE)
                                                    cmd = Command.FIRST;
                                             } 
                                             ctrLine.setLocationImg(approot+"/images");
                                             ctrLine.initDefault();						   
                                            %>
                                            <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                        </span>				  
                                    </td>
                                </tr>
                                <% if (privAdd){%>
                                <tr valign="middle"> 
                                    <td colspan="2" class="command"> 
                                        <table width="15%" border="0" cellspacing="2" cellpadding="2">
                                            <tr> 
                                                <td width="20%">
                                                    <a href="javascript:addNew()"><img name="Image10011" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Tambah Baru User"></a>
                                                </td>
                                                <td nowrap width="80%"><a href="javascript:addNew()" class="command">
                                                    <%=textListTitle[SESS_LANGUAGE][2]%></a>
                                                </td>
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
