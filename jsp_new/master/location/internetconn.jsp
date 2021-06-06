<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package prochain -->
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>

<%@ page import = "com.dimata.pos.entity.masterCashier.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!    public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "IP", "User Name", "Password", "Port","Location ","Database name"},
        {"No", "ip", "user_name", "pasword","port","location_id","Database name"}
    };
    public static final String textListTitleHeader[][] = {
        {"Internet Connection"},
        {"Goods Location"}
    };

    public String drawList(int language, Vector objectClass, long internetId, String approot, int start, int recordToGet)throws Exception {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "3%");
        ctrlist.addHeader(textListHeader[language][1], "12%");
        ctrlist.addHeader(textListHeader[language][2], "15%");
        ctrlist.addHeader(textListHeader[language][3], "20%");
        ctrlist.addHeader(textListHeader[language][4], "10%");
        ctrlist.addHeader(textListHeader[language][5], "10%");
        ctrlist.addHeader(textListHeader[language][6], "10%");

        //ctrlist.addHeader(textListHeader[language][5], "10%");
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            internetconn internet = (internetconn) objectClass.get(i);
            Vector rowx = new Vector();
            if (internetId == internet.getOID()) {
                index = i;
            }


            start = start + 1;
            rowx.add("" + start);
            rowx.add("<a href=\"javascript:cmdEdit('" + internet.getOID() + "')\">" + internet.getIp() + "</a>");//untuk membuat link
            rowx.add(cekNull(internet.getUser_name()));
            rowx.add(cekNull(internet.getPasword()));
            rowx.add(cekNull(internet.getPort()));
            rowx.add(cekNull(internet.getCash_master_id().toString()));
            rowx.add(cekNull(internet.getDatabase_name()));
            //Location location = new Location();

            //location = PstLocation.fetchExc(503000);
            //rowx.add(cekNull(location.getName()));
            //CashMaster cashMaster = internet.getCash_master_id();
            // rowx.add(cekNull());
            //rowx.add(PstInternet.fieldTypes);
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(internet.getOID()));
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
            //int cmdMinimum = FRMQueryString.requestInt(request, "command_minimum");
            int startInternet = FRMQueryString.requestInt(request, "starsdfgsdt_internet");
            int startMaterial = FRMQueryString.requestInt(request, "stargsdt_matesdfrial");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidInternet = FRMQueryString.requestLong(request, FrmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]);

            String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
            //String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];
            //FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();
            /* variable declaration */
            int recordToGet = 20;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = PstInternet.fieldNames[PstInternet.FLD_IP] + "," + PstInternet.fieldNames[PstInternet.FLD_USER_NAME];

            /* ControlLine */
            ControlLine ctrLine = new ControlLine();

            /* Control LOcation */
            CtrlInternet ctrlInternet = new CtrlInternet(request);
            FrmInternet frmInternet = ctrlInternet.getForm();
            iErrCode = ctrlInternet.action(iCommand, oidInternet);
            internetconn internet = ctrlInternet.getInternet();
            msgString = ctrlInternet.getMessage();

            /* get start value for list location */
            if (iCommand == Command.SAVE && iErrCode == FRMMessage.NONE) {
                startInternet = PstInternet.findLimitStart(internet.getOID(), recordToGet, whereClause);
            }

            int vectSize = PstInternet.getCount(whereClause);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                startInternet = ctrlInternet.actionList(iCommand, startInternet, vectSize, recordToGet);
            }

            /* get record to display */
            Vector listInternet = new Vector(1, 1);
            listInternet = PstInternet.list(startInternet, recordToGet, whereClause, orderClause);
            if (listInternet.size() < 1 && startInternet > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    startInternet = startInternet - recordToGet;
                } else {
                    startInternet = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listInternet = PstInternet.list(startInternet, recordToGet, whereClause, orderClause);
            }

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmInternet.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                //window.location="#go";
            <%}%>

                /*------------- start location function ---------------*/
                function cmdAdd()
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value="0";
                    document.frminternet.command.value="<%=Command.ADD%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();


                }

                function cmdAsk(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.ASK%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdConfirmDelete(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.DELETE%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdSave()
                {
                    document.frminternet.command.value="<%=Command.SAVE%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdEdit(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.EDIT%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdCancel(oidInternet)
                {
                    document.frminternet.hidden_location_id.value=oidInternet;
                    document.frminternet.command.value="<%=Command.EDIT%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdBack()
                {
                    document.frminternet.command.value="<%=Command.BACK%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdListFirst()
                {
                    document.frminternet.command.value="<%=Command.FIRST%>";
                    document.frminternet.prev_command.value="<%=Command.FIRST%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdListPrev()
                {
                    document.frminternet.command.value="<%=Command.PREV%>";
                    document.frminternet.prev_command.value="<%=Command.PREV%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdListNext()
                {
                    document.frminternet.command.value="<%=Command.NEXT%>";
                    document.frminternet.prev_command.value="<%=Command.NEXT%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();
                }

                function cmdListLast()
                {
                    document.frminternet.command.value="<%=Command.LAST%>";
                    document.frminternet.prev_command.value="<%=Command.LAST%>";
                    document.frminternet.action="internetconn.jsp";
                    document.frminternet.submit();

                }


                /*------------- end location function ---------------*/


                /*------------- start vendor price function -----------------*/
                function addMinimumQty()
                {
                    document.frminternet.command.value="<%=Command.ADD%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="minimum_qty.jsp";
                    document.frminternet.submit();
                }

                function editMinimumQty(locationId,minimumOid)
                {
                    document.frminternet.hidden_location_id.value=locationId;
                    document.frminternet.hidden_mat_minimum_id.value=minimumOid;
                    document.frminternet.command.value="<%=Command.EDIT%>";
                    document.frminternet.action="minimum_qty.jsp";
                    document.frminternet.submit();
                }
                /*------------- end vendor price function -----------------*/


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
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                Masterdata &gt; <%=locationTitle%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frminternet" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start_Internet" value="<%=startInternet%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>" value="<%=internet.getOID()%>">

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
                                                        <td height="22" valign="middle" colspan="3"> <%= drawList(SESS_LANGUAGE, listInternet, oidInternet, approot, startInternet, recordToGet)%> </td>
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
                                                                            out.println(ctrLine.drawImageListLimit(cmd, vectSize, startInternet, recordToGet));
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
                                                                    <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%>"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdAdd()" class="command"><%=ctrLine.getCommand(SESS_LANGUAGE, locationTitle, ctrLine.CMD_ADD, true)%></a></td>
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
                                                        <td colspan="2" class="comment" height="30"><u><%=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Editorn " + locationTitle : locationTitle + " Editor"%></u></td>
                                                    </tr>
                                                    <tr>
                                                        <td height="100%" width="100%" colspan="2">
                                                            <table border="0" cellspacing="1" cellpadding="1" width="100%">

                                                                <tr align="left">
                                                                    <td width="12%"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <input type="text" name="<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_IP]%>"  value="<%= internet.getIp()%>" class="formElemen" size="20">
                                                                        * <%= frmInternet.getErrorMsg(FrmInternet.FRM_FIELD_IP)%> </td>
                                                                </tr>
                                                                <tr align="left">
                                                                    <td width="12%"><%=textListHeader[SESS_LANGUAGE][6]%></td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <input type="text" name="<%=frmInternet.fieldNames[FrmInternet.FRM_DATABASE_NAME]%>"  value="<%= internet.getDatabase_name()%>" class="formElemen" size="20">
                                                                        * <%= frmInternet.getErrorMsg(FrmInternet.FRM_DATABASE_NAME)%> </td>
                                                                </tr>

                                                                <tr align="left">
                                                                    <td width="12%"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <input type="text" name="<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_USER_NAME]%>"  value="<%= internet.getUser_name()%>" class="formElemen" size="40">
                                                                        * <%= frmInternet.getErrorMsg(FrmInternet.FRM_FIELD_USER_NAME)%></td>
                                                                </tr>
                                                                <tr align="left">
                                                                    <td valign="top" width="12%"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <textarea name="<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_PASWORD]%>" class="formElemen" cols="39" rows="2" wrap="VIRTUAL"><%= internet.getPasword()%></textarea>
                                                                        * <%= frmInternet.getErrorMsg(FrmInternet.FRM_FIELD_PASWORD)%> </td>
                                                                </tr>
                                                                <tr align="left">
                                                                    <td width="12%"><%=textListHeader[SESS_LANGUAGE][4]%></td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <input type="text" name="<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_PORT]%>"  value="<%= internet.getPort()%>" class="formElemen" size="20">
                                                                        * <%= frmInternet.getErrorMsg(FrmInternet.FRM_FIELD_USER_NAME)%></td>

                                                                </tr>
                                                                <tr align="left">
                                                                    <td width="12%">Lokasi kasir </td>
                                                                    <td width="1%" valign="top">:</td>
                                                                    <td width="87%" valign="top">
                                                                        <%

                                                                            String where = "";
                                                                            // if (selectedLocationId!=0) {
                                                                              //  where = PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]+"='" +selectedLocationId+"'";
                                                                            //}
                                                                            Vector val_cashierid = new Vector(1,1);
                                                                            Vector key_cashierid = new Vector(1,1);
                                                                            Location location = new Location();
                                                                            //CashCashier cashCashier = new CashCashier();
                                                                            Vector vt_cashier = PstCashMaster.list(0,0,where, PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]);
                                                                            val_cashierid.add("0");
                                                                            key_cashierid.add("Semua Cashier");
                                                                            for(int d=0;d<vt_cashier.size();d++){
                                                                                    CashMaster cashMaster = (CashMaster)vt_cashier.get(d);
                                                                                    val_cashierid.add(""+cashMaster.getOID()+"");
                                                                                    location = PstLocation.fetchExc(cashMaster.getLocationId());
                                                                                    //cashCashier = PstCashCashier.fetchExc(cashMaster.getOID());
                                                                                    //key_cashierid.add(location.getCode());
                                                                                    //key_cashierid.add(""+cashMaster.getCashierNumber());
                                                                                    key_cashierid.add(location.getName()+" kasir "+""+ cashMaster.getCashierNumber());
                                                                            }
                                                                            //String select_cashier = "0";
                                                                        %>
                                                                     <%=ControlCombo.draw(frmInternet.fieldNames[frmInternet.FRM_CASH_MASTER_ID], null,""+val_cashierid, val_cashierid, key_cashierid, "", "formElemen")%></td>

                                                                   </tr>

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
                                                                       String scomDel = "javascript:cmdAsk('" + oidInternet + "')";
                                                                       String sconDelCom = "javascript:cmdConfirmDelete('" + oidInternet + "')";
                                                                       String scancel = "javascript:cmdEdit('" + oidInternet + "')";
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
                    <%@ include file = "../../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
