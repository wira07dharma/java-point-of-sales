<%@page import="com.dimata.posbo.entity.masterdata.PstDataSyncStatus"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstConnection"%>
<%@page import="com.dimata.posbo.entity.masterdata.OutletConnection"%>
<%@page import="com.dimata.pos.entity.masterCashier.CashMaster"%>
<%@page import="com.dimata.pos.entity.masterCashier.PstCashMaster"%>
<%@page import="com.dimata.posbo.entity.masterdata.DataSyncSql"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstDataSyncSql"%>
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
<%@ page import = "com.dimata.common.form.location.*" %>
<%@ page import = "com.dimata.posbo.session.masterdata.*" %>
<%@ page import = "com.dimata.common.form.location.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_LOCATION);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%! public static String formatNumberList = "#,###";
    public static String formatNumberEdit = "##.###";

    /* this constant used to list text of listHeader */
    public static final String textListHeader[][] = {
        {"No", "Location", "Cashier", "Number of Query", "Proses","Running"},
        {"No", "ip", "user_name", "pasword","port","location_id","Database name"}
    };
    public static final String textListTitleHeader[][] = {
        {"Data Sync to outlet"},
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

        //ctrlist.addHeader(textListHeader[language][6], "10%");
        //ctrlist.addHeader(textListHeader[language][5], "10%");
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();

        ctrlist.reset();

        int index = -1;
        if (start < 0) {
            start = 0;
        }
        Vector vecDbConn = new Vector(1,1);
        CashMaster cashmaster  = new CashMaster();
        //PstCashMaster pstcash_master = new PstCashMaster(0);
        vecDbConn = PstConnection.listAll();
       

        for (int i = 0; i < vecDbConn.size(); i++) {
            OutletConnection dbConn = ( OutletConnection) vecDbConn.get(i) ;
            Vector rowx = new Vector();
            //if (internetId == datasync.getOID()) {
            //    index = i;
           // }

            start = start + 1;
           

           //rowx.add("<center><input type=\"checkbox\" name=\"data_is_process"+i+"\" value=\"1\"></center>");
          rowx.add("" + start + "<center><input type=\"checkbox\" name=\"data_is_process"+i+"\" value=\""+ dbConn.getOID() +"\"></center>");
         

            dbConn = (OutletConnection) vecDbConn.get(i);
            cashmaster=PstCashMaster.fetchExc(dbConn.getCash_master_id());
            Location lokasi = new Location();
            lokasi= PstLocation.fetchExc(cashmaster.getLocationId());

            rowx.add(lokasi.getName());
            rowx.add("kasir " + cashmaster.getCashierNumber());

            PstDataSyncStatus pstdataStatus = new PstDataSyncStatus(0);
            rowx.add("" + pstdataStatus.getCount("id_dbconnection = " + dbConn.getOID() + " and " + "status=0"));
            lstData.add(rowx);
         // lstLinkData.add(String.valueOf(internet.getOID()));
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
                    //TransferDataToOutlet.stopThread(cashMasterId);
                    break;
                case Command.CLOSE:
                    break;
                case Command.ACTIVATE:
                    break;
                default:;
            }

            String locationTitle = textListTitleHeader[SESS_LANGUAGE][0]; //com.dimata.common.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.common.jsp.JspInfo.LOCATION];
            //String minimumTitle = com.dimata.material.jsp.JspInfo.txtMaterialInfo[SESS_LANGUAGE][com.dimata.material.jsp.JspInfo.MATERIAL_MINIMUM];
            //FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();
            /* variable declaration */
            int recordToGet = 20;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "";//PstDataSyncSql.fieldNames[PstDataSyncSql.FLD_QUERY] + "," + PstInternet.fieldNames[PstInternet.FLD_USER_NAME];

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

            int vectSize = PstConnection.getCount(whereClause);
            if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                startInternet = ctrlInternet.actionList(iCommand, startInternet, vectSize, recordToGet);
            }

            /* get record to display */
            Vector listInternet = new Vector(1, 1);
            listInternet = PstDataSyncSql.list(startInternet, recordToGet, whereClause, orderClause);
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
                            /*------------- start location function ---------------*/
                function cmdStart()
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value="0";
                    document.frminternet.command.value="<%=Command.START%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }
                function cmdAsk(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.ASK%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdConfirmDelete(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.DELETE%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdSave()
                {
                    document.frminternet.command.value="<%=Command.SAVE%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdEdit(oidInternet)
                {
                    document.frminternet.<%=frmInternet.fieldNames[FrmInternet.FRM_FIELD_OID]%>.value=oidInternet;
                    document.frminternet.command.value="<%=Command.EDIT%>";
                    document.frminternet.prev_command.value="<%=prevCommand%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }
               
                function cmdBack()
                {
                    document.frminternet.command.value="<%=Command.BACK%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdListFirst()
                {
                    document.frminternet.command.value="<%=Command.FIRST%>";
                    document.frminternet.prev_command.value="<%=Command.FIRST%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdListPrev()
                {
                    document.frminternet.command.value="<%=Command.PREV%>";
                    document.frminternet.prev_command.value="<%=Command.PREV%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdListNext()
                {
                    document.frminternet.command.value="<%=Command.NEXT%>";
                    document.frminternet.prev_command.value="<%=Command.NEXT%>";
                    document.frminternet.action="data_sync.jsp";
                    document.frminternet.submit();
                }

                function cmdListLast()
                {
                    document.frminternet.command.value="<%=Command.LAST%>";
                    document.frminternet.prev_command.value="<%=Command.LAST%>";
                    document.frminternet.action="data_sync.jsp";
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
                                                    <%
                                                                if (iCommand == Command.LIST || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST
                                                                        || iCommand == Command.NONE
                                                                        || iCommand == Command.BACK
                                                                        || (iCommand == Command.SAVE && iErrCode == 0) || (iCommand == Command.DELETE && iErrCode == 0)) {
                                                    %>
                                                    <tr align="left" valign="top">
                                                        <td>
                                                            <%if (((iCommand != Command.ADD) && (iCommand != Command.STOP) && (iCommand != Command.CLOSE)) || (iCommand == Command.ACTIVATE ) ) {%>
                                                            <table cellpadding="0" cellspacing="0" border="0">
                                                                <tr>
                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="4"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="4" valign="middle" colspan="3" width="172"><a href="javascript:cmdStart()" class="command">Execute</a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="4"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdStart()" class="command">Stop </a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="24"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdStart()" class="command">Pause </a></td>

                                                                    <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td width="24"><a href="javascript:cmdStart()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Execute data"></a></td>
                                                                    <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                                    <td height="22" valign="middle" colspan="3" width="172"><a href="javascript:cmdStart()" class="command">Resume </a></td>
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
                                                            if ((iCommand == Command.START)
                                                                    || (iCommand == Command.EDIT)
                                                                    || (iCommand == Command.ASK)
                                                                    || ((iCommand == Command.SAVE) && iErrCode > 0) || (iCommand == Command.DELETE && iErrCode > 0)) {
                                                %>
                                                <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                    
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
