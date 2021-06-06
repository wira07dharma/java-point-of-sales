<%@page import="com.dimata.posbo.session.masterdata.TransferDataToOutlet"%>
<%@ page import = "com.dimata.posbo.entity.masterdata.OutletConnection"%>
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
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>

<%@ page import = "com.dimata.posbo.form.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ page import = "com.dimata.pos.entity.masterCashier.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%! public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "location", "cashier", "number of query", "proses", "running"},
        {"No", "location", "cashier", "number of query", "proses", "running"}
    };
    public static final String textListTitleHeader[][] = {
        {"Trasfer Data to Outlet"},
        {"Goods Location"}
    };

    public String drawList(HttpServletRequest request, int language, Vector objectClass, long connId, String approot, int start, int recordToGet)throws Exception {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tabbg");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListHeader[language][0], "3%");
        ctrlist.addHeader(textListHeader[language][1], "8%");
        ctrlist.addHeader(textListHeader[language][2], "8%");
        ctrlist.addHeader(textListHeader[language][3], "8%");
        //ctrlist.addHeader(textListHeader[language][4], "8%");
       // ctrlist.addHeader(textListHeader[language][5], "8%");

        //ctrlist.addHeader(textListHeader[language][5], "10%");
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }
        CashMaster cashmaster = new CashMaster();
        Location location = new Location();
        for (int i = 0; i < objectClass.size(); i++) {
            OutletConnection conn = (OutletConnection) objectClass.get(i);
            Vector rowx = new Vector();
            if (connId == conn.getOID()) {
                index = i;
            }

            start = start + 1;
            String checked="";
            long lx = FRMQueryString.requestLong(request, "data_is_process" + i);
                    if(lx!=0){
                      checked="checked";
                    }
            if(conn.getTypeConnection()==1){
                rowx.add("");
            }else{
                rowx.add("" + start + "<center><input type=\"checkbox\" name=\"data_is_process"+i+"\" value=\""+ conn.getOID() +"\" "+checked+"></center>");
            }
            
            cashmaster = PstCashMaster.fetchExc(conn.getCash_master_id());
            location = PstLocation.fetchExc(cashmaster.getLocationId());
            
            //rowx.add(location.getName() + " kasir " + cashmaster.getCashierNumber());
            rowx.add(cekNull(location.getName()) + "<br>" + TransferDataToOutlet.getStatusText(conn.getCash_master_id())+"<br>"+ TransferDataToOutlet.getAddStatusText(conn.getCash_master_id()));

            rowx.add(cekNull("kasir " + cashmaster.getCashierNumber()));
            PstDataSyncStatus pstdataStatus = new PstDataSyncStatus(0);
            rowx.add("" + pstdataStatus.getCount("id_dbconnection = " + conn.getOID() + " and " + "status=0"));

           // rowx.add(cekNull(conn.getFordate()));
           // rowx.add(cekNull(conn.getDbuser()));
           // rowx.add(cekNull(conn.getDbmaxconn()));

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(conn.getOID()));

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
            int startInternet = FRMQueryString.requestInt(request, "start_connection");
            int startMaterial = FRMQueryString.requestInt(request, "start_material");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidDbConn = FRMQueryString.requestLong(request, FrmConnection.fieldNames[FrmConnection.FRM_FIELD_OID]);

            

            boolean isProses[]= new boolean [PstConnection.getCount("")];
            Vector vecOutletConn = new Vector();
            for(int i=0;i<isProses.length;i++ ){
                    long lx = FRMQueryString.requestLong(request, "data_is_process" + i);
                    if(lx!=0){
                        vecOutletConn.add(new Long(lx));
                    }
            }

            switch(iCommand){
                case Command.START:
                    TransferDataToOutlet.startTransfer(vecOutletConn);
                    break;
                case Command.STOP:
                    for(int i=0;i<(vecOutletConn.size());i++ ){
                            //long oidDbConnec = Long.parseLong("" + vecOutletConn.get(i));
                            OutletConnection outletConn = PstConnection.fetchExc(Long.parseLong("" + vecOutletConn.get(i)));
                            TransferDataToOutlet.stopThread(outletConn.getCash_master_id());
                    }
                    break;
                case Command.LOCK:
                    for(int i=0;i<(vecOutletConn.size());i++ ){
                            //long cashMaster = Long.parseLong("" + vecOutletConn.get(i));
                            OutletConnection outletConn = PstConnection.fetchExc(Long.parseLong("" + vecOutletConn.get(i)));
                            TransferDataToOutlet.pauseThread(outletConn.getCash_master_id());
                    }
                    break;
                case Command.UNLOCK:
                    for(int i=0;i<vecOutletConn.size();i++ ){
                            OutletConnection outletConn = PstConnection.fetchExc(Long.parseLong("" + vecOutletConn.get(i)));
                            TransferDataToOutlet.resumeThread(outletConn.getCash_master_id());
                    }
                    break;
                case Command.DELETE:
                    PstDataSyncSql pstDataSyncSql = new PstDataSyncSql(0);
                    //Vector vecDataSyncDelete = new Vector();
                    //vecDataSyncDelete =  PstDataSyncSql.list(0, 0, PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " not in (select " + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " from " + PstDataSyncStatus.TBL_P2_DATA_SYNC_STATUS + ")", "");
                    //for(int i=0;i<vecDataSyncDelete.size();i++){
                     //   DataSyncSql dataSync = (DataSyncSql) vecDataSyncDelete.get(i);
                      //  pstDataSyncSql.deleteExc(dataSync.getOID());
                    //}
                    PstDataSyncSql.deleteRecExc(PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " not in (select " + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " from " + PstDataSyncStatus.TBL_P2_DATA_SYNC_STATUS + ")");
                    break;
                case Command.RESET:
                    TransferDataToOutlet.ResetDataTransfer(vecOutletConn);
                    break;
                    
                default:;
            }

            //update opie-eyek 20130904
            int queryDelete = 0;//PstDataSyncSql.getCount(PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " not in (select " + PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_DATA_SYNC_ID] + " from " + PstDataSyncStatus.TBL_P2_DATA_SYNC_STATUS + ")" );

            String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
            //String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];
            //FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();
            /* variable declaration */
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";//PstConnection.fieldNames[PstConnection.FLD_TYPE_CONNECTION]+"=0";
            String orderClause = PstConnection.fieldNames[PstConnection.FLD_DBDRIVER] + "," + PstConnection.fieldNames[PstConnection.FLD_DBURL];

            /* ControlLine */
            ControlLine ctrLine = new ControlLine();

            /* Control LOcation */

            CtrlConnection ctrlconn = new CtrlConnection(request);
            FrmConnection frmconn = ctrlconn.getForm();
            iErrCode = ctrlconn.action(iCommand, oidDbConn);
            OutletConnection conn = ctrlconn.getConnection();
            msgString = ctrlconn.getMessage();

            /* get start value for list location */
           

            int vectSize = PstConnection.getCount(whereClause);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                startInternet = ctrlconn.actionList(iCommand, startInternet, vectSize, recordToGet);
            }

            /* get record to display */
            Vector listInternet = new Vector(1, 1);
            listInternet = PstConnection.list(startInternet, recordToGet, whereClause, orderClause);
            if (listInternet.size() < 1 && startInternet > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    startInternet = startInternet - recordToGet;
                } else {
                    startInternet = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST;

                }
                listInternet = PstConnection.list(startInternet, recordToGet, whereClause, orderClause);
            }
//iCommand=Command.NONE;
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmconn.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK)) {%>
                //window.location="#go";
            <%}%>

                /*------------- start location function ---------------*/
                function cmdStart()
                {
                    document.frmconn.<%=frmconn.fieldNames[FrmConnection.FRM_FIELD_OID]%>.value="0";
                    document.frmconn.command.value="<%=Command.START%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();

                }

                function refreshStatusSynch(){
                    document.frmconn.<%=frmconn.fieldNames[FrmConnection.FRM_FIELD_OID]%>.value="0";
                    document.frmconn.command.value="<%=Command.NONE%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }


                function cmdStop()
                {
                    document.frmconn.command.value="<%=Command.STOP%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdPause()
                {
                    document.frmconn.command.value="<%=Command.LOCK%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdResume()
                {
                    document.frmconn.command.value="<%=Command.UNLOCK%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdEdit(oidInternet)
                {
                    document.frmconn.<%=frmconn.fieldNames[FrmConnection.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frmconn.command.value="<%=Command.EDIT%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdCancel(oidInternet)
                {
                    document.frmconn.hidden_location_id.value=oidInternet;
                    document.frmconn.command.value="<%=Command.EDIT%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdBack()
                {
                    document.frmconn.command.value="<%=Command.BACK%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdListFirst()
                {
                    document.frmconn.command.value="<%=Command.FIRST%>";
                    document.frmconn.prev_command.value="<%=Command.FIRST%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdListPrev()
                {
                    document.frmconn.command.value="<%=Command.PREV%>";
                    document.frmconn.prev_command.value="<%=Command.PREV%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdListNext()
                {
                    document.frmconn.command.value="<%=Command.NEXT%>";
                    document.frmconn.prev_command.value="<%=Command.NEXT%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                }

                function cmdListLast()
                {
                    document.frmconn.command.value="<%=Command.LAST%>";
                    document.frmconn.prev_command.value="<%=Command.LAST%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();

                }
                function cmdDelete()
                {   if(confirm("yakin akan menghapus log data yang sudah ditransfer")){
                    document.frmconn.command.value="<%=Command.DELETE%>";
                    document.frmconn.prev_command.value="<%=Command.DELETE%>";
                    document.frmconn.action="transfer_data.jsp";
                    document.frmconn.submit();
                    }
                }

                function cmdResetQuery()
                {
                    if(confirm("Yakin akan menghapus data query ini?")){
                        document.frmconn.command.value="<%=Command.RESET%>";
                        document.frmconn.prev_command.value="<%=Command.RESET%>";
                        document.frmconn.action="transfer_data.jsp";
                        document.frmconn.submit();
                    }
                }


                /*------------- end location function ---------------*/


                /*------------- start vendor price function -----------------*/
                function addMinimumQty()
                {
                    document.frmconn.command.value="<%=Command.ADD%>";
                    document.frmconn.prev_command.value="<%=prevCommand%>";
                    document.frmconn.action="minimum_qty.jsp";
                    document.frmconn.submit();
                }

                function editMinimumQty(locationId,minimumOid)
                {
                    document.frmconn.hidden_location_id.value=locationId;
                    document.frmconn.hidden_mat_minimum_id.value=minimumOid;
                    document.frmconn.command.value="<%=Command.EDIT%>";
                    document.frmconn.action="minimum_qty.jsp";
                    document.frmconn.submit();
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
        <%if(menuUsed == MENU_ICON){%>
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
            <%if(menuUsed == MENU_PER_TRANS){%>
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
                                Masterdata &gt; <%=locationTitle%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmconn" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                    <input type="hidden" name="start_connection" value="<%=startInternet%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="<%=frmconn.fieldNames[FrmConnection.FRM_FIELD_OID]%>" value="<%=conn.getOID()%>">

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
                                                        <td height="22" valign="middle" colspan="3"> <%= drawList(request, SESS_LANGUAGE, listInternet, oidDbConn, approot, startInternet, recordToGet)%> </td>
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
                                                                        || iCommand == Command.START
                                                                        || iCommand == Command.STOP
                                                                        || iCommand == Command.LOCK
                                                                        || iCommand == Command.UNLOCK
                                                                        || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {
                                                    %>
                                                    <tr align="left" valign="top">
                                                        <td><br>
                                                            <%if (((iCommand != Command.ADD) && (iCommand != Command.EDIT) && (iCommand != Command.ASK)) || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {%>
                                                            <table cellpadding="0" cellspacing="0" border="0">
                                                                <tr>
                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <!--td width="4"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td-->
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="4" valign="middle" colspan="3" width="172"><a class="btn btn-primary" href="javascript:cmdStart()" class="command"><i class="fa fa-play"></i> Execute</a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <!--td width="4"><a href="javascript:cmdStop()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="stop"></a></td-->
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="4" valign="middle" colspan="3" width="172"><a class="btn btn-primary" href="javascript:cmdStop()" class="command"><i class="fa fa-stop"></i> Stop</a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <!--td width="24"><a href="javascript:cmdPause()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td-->
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a class="btn btn-primary" href="javascript:cmdPause()" class="command"><i class="fa fa-pause"></i> Pause </a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <!--td width="24"><a href="javascript:cmdResume()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td-->
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a class="btn btn-primary" href="javascript:cmdResume()" class="command"><i class="fa fa-play"></i> Resume </a></td>
                                                                    
                                                                </tr>
                                                            </table>
                                                                    <br>
                                                                    <table cellpadding="0" cellspacing="0" border="0">
                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <!--td width="24"><a href="javascript:cmdResetQuery()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td-->
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a class="btn btn-primary" href="javascript:cmdResetQuery()" class="command"><i class="fa fa-refresh"></i> Reset data </a></td>
                                                                    
                                                            </table>
                                                                <% if(queryDelete > 0){%>

                                                                        <table cellpadding="0" cellspacing="0" border="0">
                                                                            <tr>
                                                                                <td> Ada  <%=queryDelete%>  query yang bisa dihapus</td>
                                                                            </tr>
                                                                        </table>
                                                                        <table cellpadding="0" cellspacing="0" border="0">
                                                                            <tr>
                                                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                <td width="24"><a href="javascript:cmdDelete()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                                <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                                <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdDelete()" class="command">Hapus data sql</a></td>
                                                                            </tr>
                                                                        </table>
                                                                <%}%>
                                                                    
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
                                                                       String scomDel = "javascript:cmdAsk('" + oidDbConn + "')";
                                                                       String sconDelCom = "javascript:cmdConfirmDelete('" + oidDbConn + "')";
                                                                       String scancel = "javascript:cmdEdit('" + oidDbConn + "')";
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
                     <%if(menuUsed == MENU_ICON){%>
                        <%@include file="../../styletemplate/footer.jsp" %>

                    <%}else{%>
                        <%@ include file = "../../main/footer.jsp" %>
                    <%}%>

                    <!-- #EndEditable --> </td>
            </tr>
        </table>
                    <script language="JavaScript">
// CREDITS:
 // Automatic Page Refresher by Peter Gehrig and Urs Dudli www.24fun.com
 // Permission given to use the script provided that this notice remains as is.
 // Additional scripts can be found at http://www.hypergurl.com
// Configure refresh interval (in seconds)
var refreshinterval=10
// Shall the coundown be displayed inside your status bar? Say "yes"  or "no" below: var displaycountdown="yes"
// Do not edit the code below

var starttime;
var nowtime;
var reloadseconds=0;
var secondssinceloaded=0;
function starttime() {
starttime=new Date() ;
starttime=starttime.getTime() ;
countdown() ;
}

function countdown() {
nowtime= new Date() ;
nowtime=nowtime.getTime() ;
secondssinceloaded=(nowtime-starttime)/1000 ;
reloadseconds=Math.round(refreshinterval-secondssinceloaded) ;
  if (refreshinterval>=secondssinceloaded) {
       var timer=setTimeout("countdown()",1000) ;
       if (displaycountdown=="yes") {
       window.status="Page refreshing in "+reloadseconds+ " seconds" ;
         }
    } else {
      clearTimeout(timer);
      refreshStatusSynch();//window.location.reload(true) ;
     }
}
window.onload=starttime ;
</script>
    </body>
    <!-- #EndTemplate --></html>
