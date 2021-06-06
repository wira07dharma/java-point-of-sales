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
<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>


<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "Kode", "Nama", "Lokasi", "Keterangan", "Tipe Ruangan", "Status", "Kapasitas", "Kapasitas Unit", "Indeks"},
        {"No", "Code", "Name", "Location", "Description", "Room Class", "Status", "Capacity", "Capacity Unit", "Index"}
    };
    public static final String textListTitleHeader[][] = {
        {"Bar"},
        {"Bar"}
    };

    public String drawList(int language, Vector objectClass, long roomId, String approot, int start, int recordToGet) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("80%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "3%");
        ctrlist.addHeader(textListHeader[language][1], "5%");
        ctrlist.addHeader(textListHeader[language][2], "10%");
        ctrlist.addHeader(textListHeader[language][3], "15%");
        ctrlist.addHeader(textListHeader[language][6], "5%");
        ctrlist.addHeader(textListHeader[language][7], "5%");
        ctrlist.addHeader(textListHeader[language][8], "10%");
        ctrlist.addHeader(textListHeader[language][9], "5%");



        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Room room = (Room) objectClass.get(i);

            Vector rowx = new Vector();
            if (roomId == room.getOID()) {
                index = i;
            }

            start = start + 1;
            rowx.add("" + start);
            rowx.add("<a href=\"javascript:cmdEdit('" + room.getOID() + "')\">" + room.getCode() + "</a>");
            rowx.add(cekNull(room.getName()));

            Location location = new Location();
            try {
                location = PstLocation.fetchExc(room.getLocationId());
            } catch (Exception e) {
                System.out.println("Location not found ...");
            }
            rowx.add(cekNull(location.getName()));
            rowx.add("" + room.getStatus());
            rowx.add("" + room.getCapacity());
            rowx.add("" + room.getCapacityUnit());
            rowx.add("" + room.getShowIndex());
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(room.getOID()));
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

<%
    /* get data from request form */
    int iCommand = FRMQueryString.requestCommand(request);
    int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
    int startRoom = FRMQueryString.requestInt(request, "start_room");
    int startMaterial = FRMQueryString.requestInt(request, "start_material");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidRoom = FRMQueryString.requestLong(request, "hidden_room_id");
    long oidMinimum = FRMQueryString.requestLong(request, "hidden_mat_minimum_id");
    String roomTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
//String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];
	AppUser appUser = userSession.getAppUser();
    /* variable declaration */
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = PstRoom.fieldNames[PstRoom.FLD_CODE] + "," + PstRoom.fieldNames[PstRoom.FLD_NAME];

    /* ControlLine */
    ControlLine ctrLine = new ControlLine();

    /* Control LOcation */
    CtrlRoom ctrlRoom = new CtrlRoom(request);
    FrmRoom frmRoom = ctrlRoom.getForm();
    iErrCode = ctrlRoom.action(iCommand, oidRoom, request);
    Room room = ctrlRoom.getRoom();
    msgString = ctrlRoom.getMessage();

    /* get start value for list location */
    if (iCommand == Command.SAVE && iErrCode == FRMMessage.NONE) {
        startRoom = PstRoom.findLimitStart(room.getOID(), recordToGet, whereClause);
    }

    int vectSize = PstRoom.getCount(whereClause);
    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startRoom = ctrlRoom.actionList(iCommand, startRoom, vectSize, recordToGet);
    }

    /* get record to display */
    Vector listRoom = new Vector(1, 1);
    listRoom = PstRoom.list(startRoom, recordToGet, whereClause, orderClause);
    if (listRoom.size() < 1 && startRoom > 0) {
        if (vectSize - recordToGet > recordToGet) {
            startRoom = startRoom - recordToGet;
        } else {
            startRoom = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listRoom = PstRoom.list(startRoom, recordToGet, whereClause, orderClause);
    }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmRoom.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                //window.location="#go";
            <%}%>

                /*------------- start location function ---------------*/
                function cmdAdd()
                {
                    document.frmroom.hidden_room_id.value="0";
                    document.frmroom.command.value="<%=Command.ADD%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdAsk(oidRoom)
                {
                    document.frmroom.hidden_room_id.value=oidRoom;
                    document.frmroom.command.value="<%=Command.ASK%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdConfirmDelete(oidRoom)
                {
                    document.frmroom.hidden_room_id.value=oidRoom;
                    document.frmroom.command.value="<%=Command.DELETE%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdSave()
                {
                    document.frmroom.command.value="<%=Command.SAVE%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdEdit(oidRoom)
                {
                    document.frmroom.hidden_room_id.value=oidRoom;
                    document.frmroom.command.value="<%=Command.EDIT%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdCancel(oidRoom)
                {
                    document.frmroom.hidden_room_id.value=oidRoom;
                    document.frmroom.command.value="<%=Command.EDIT%>";
                    document.frmroom.prev_command.value="<%=prevCommand%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdBack()
                {
                    document.frmroom.command.value="<%=Command.BACK%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdListFirst()
                {
                    document.frmroom.command.value="<%=Command.FIRST%>";
                    document.frmroom.prev_command.value="<%=Command.FIRST%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdListPrev()
                {
                    document.frmroom.command.value="<%=Command.PREV%>";
                    document.frmroom.prev_command.value="<%=Command.PREV%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdListNext()
                {
                    document.frmroom.command.value="<%=Command.NEXT%>";
                    document.frmroom.prev_command.value="<%=Command.NEXT%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }

                function cmdListLast()
                {
                    document.frmroom.command.value="<%=Command.LAST%>";
                    document.frmroom.prev_command.value="<%=Command.LAST%>";
                    document.frmroom.action="room.jsp";
                    document.frmroom.submit();
                }


                /*------------- end location function ---------------*/



                //-------------- script control line -------------------
                function MM_swapImgRestore() { //v3.0
                    var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
                }

                function MM_preloadImages() { //v3.0
                    var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                        var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                            if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                    }

                    function MM_findObj(n, d) { //v4.0
                        var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                            d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                        if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                        for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                        if(!x && document.getElementById) x=document.getElementById(n); return x;
                    }

                    function MM_swapImage() { //v3.0
                        var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                            if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                    }

        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>

        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" --> 
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "headerscript" --> 
        <SCRIPT language=JavaScript>
                function hideObjectForMarketing(){    
                } 
	 
                function hideObjectForWarehouse(){ 
                }
	
                function hideObjectForProduction(){
                }
	
                function hideObjectForPurchasing(){
                }

                function hideObjectForAccounting(){
                }

                function hideObjectForHRD(){
                }

                function hideObjectForGallery(){
                }

                function hideObjectForMasterData(){
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
                                Masterdata &gt; <%=roomTitle%><!-- #EndEditable --></td>
                        </tr>
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frmroom" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start_room" value="<%=startRoom%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="hidden_room_id" value="<%=oidRoom%>">			  
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
                                                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar " + roomTitle : roomTitle + " List"%></u></td>                      
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE, listRoom, oidRoom, approot, startRoom, recordToGet)%> </td>
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
                                                        out.println(ctrLine.drawImageListLimit(cmd, vectSize, startRoom, recordToGet));
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
                                                        <!--td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_ADD, true)%>"></a></td-->
                                                        <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                        <br>
                                                        <td valign="middle" colspan="3" ><a class="btn btn-primary" href="javascript:cmdAdd()" class="command"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_ADD, true)%></a></td>

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
                                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editor " + roomTitle : roomTitle + " Editor"%></u></td>
                        </tr>
                        <tr> 
                            <td height="100%" width="100%" colspan="2"> 
                                <table border="0" cellspacing="1" cellpadding="1" width="100%">
                                    <tr align="left"> 
                                        <td width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top"> 
                                            <input type="text" name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_CODE]%>"  value="<%= room.getCode()%>" class="formElemen" size="20">
                                            * <%= frmRoom.getErrorMsg(FrmLocation.FRM_FIELD_CODE)%> 
                                        </td>
                                    </tr>
                                    <tr align="left"> 
                                        <td width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top"> 
                                            <input type="text" name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_NAME]%>"  value="<%=room.getName()%>" class="formElemen" size="40">
                                            * <%= frmRoom.getErrorMsg(FrmLocation.FRM_FIELD_NAME)%>
                                        </td>
                                    </tr>

                                    <tr align="left">
                                        <td width="12%" valign="top">&nbsp;<%=textListHeader[SESS_LANGUAGE][3]%></td>
                                        <td width="1%" valign="top">:
                                        <td width="87%" valign="top">
                                            <%
                                                Vector val_locationid = new Vector(1, 1);
                                                Vector key_locationid = new Vector(1, 1);
												
												String whereLocation = "";

												if (appUser.getCompanyId() != 0){
													whereLocation = PstLocation.fieldNames[PstLocation.FLD_COMPANY_ID]+"="+appUser.getCompanyId();
												}
                                                Vector vt_loc = PstLocation.list(0, 0, whereLocation, PstLocation.fieldNames[PstLocation.FLD_CODE]);

                                                for (int d = 0; d < vt_loc.size(); d++) {
                                                    Location loc = (Location) vt_loc.get(d);
                                                    val_locationid.add("" + loc.getOID() + "");
                                                    key_locationid.add(loc.getName());
                                                }


                                            %>
                                            <%=ControlCombo.draw(frmRoom.fieldNames[FrmRoom.FRM_FIELD_LOCATION_ID], "formElemen", null, "" + room.getLocationId(), val_locationid, key_locationid, null)%></td>
                                    </tr>
                                    <tr align="left">
                                        <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top">

                                            <select name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_STATUS]%>"  value="<%=room.getStatus()%>" class="formElemen">
                                                <%
                                                    for (int s = 0; s < Room.ROOM_STATUS_TYPE.length; s++) {
                                                        if (s == room.getStatus()) {
                                                %>
                                                <option selected value="<%=Room.ROOM_STATUS_TYPE[s]%>"><%=Room.ROOM_STATUS_TITLE[s]%></option>
                                                <%
                                                } else {
                                                %>
                                                <option value="<%=Room.ROOM_STATUS_TYPE[s]%>"><%=Room.ROOM_STATUS_TITLE[s]%></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr align="left">
                                        <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top">
                                            <input type="text" name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_CAPACITY]%>"  value="<%=room.getCapacity()%>" class="formElemen" size="40">
                                        </td>
                                    </tr>
                                    <tr align="left">
                                        <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][8]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top">
                                            <input type="text" name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_CAPACITY_UNIT]%>"  value="<%=room.getCapacityUnit()%>" class="formElemen" size="40">
                                        </td>
                                    </tr>
                                    <tr align="left">
                                        <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][9]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top">
                                            <input type="text" name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_SHOW_INDEX]%>"  value="<%=room.getShowIndex()%>" class="formElemen" size="40">
                                        </td>
                                    </tr>
                                    <tr align="left"> 
                                        <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                        <td width="1%" valign="top">:</td>
                                        <td width="87%" valign="top"> 
                                            <textarea name="<%=frmRoom.fieldNames[FrmRoom.FRM_FIELD_DESCRIPTION]%>" class="formElemen" cols="39" rows="2" wrap="VIRTUAL"><%= room.getDescription()%></textarea>
                                        </td>
                                    </tr>

                                </table>
                            </td>
                        </tr>
                        <tr align="left" valign="top" > 
                            <td colspan="2" class="command"> 
                                <%
                                    ctrLine.setLocationImg(approot + "/images");

                                    // set image alternative caption
                                    ctrLine.setSaveImageAlt(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_SAVE, true));
                                    ctrLine.setBackImageAlt(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_BACK, true) + " List");
                                    ctrLine.setDeleteImageAlt(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_ASK, true));
                                    ctrLine.setEditImageAlt(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_CANCEL, false));

                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("100%");
                                    String scomDel = "javascript:cmdAsk('" + oidRoom + "')";
                                    String sconDelCom = "javascript:cmdConfirmDelete('" + oidRoom + "')";
                                    String scancel = "javascript:cmdEdit('" + oidRoom + "')";
                                    ctrLine.setCommandStyle("command");
                                    ctrLine.setColCommStyle("command");

                                    // set command caption
                                    ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_SAVE, true));
                                    ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_BACK, true) + " List");
                                    ctrLine.setDeleteCaption(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_ASK, true));
                                    ctrLine.setConfirmDelCaption(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_DELETE, true));
                                    ctrLine.setCancelCaption(ctrLine.getCommand(SESS_LANGUAGE, roomTitle, ctrLine.CMD_CANCEL, false));


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
                                <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%></td>
                        </tr>
                    </table>
                    <%}%>

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

