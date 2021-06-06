<%-- 
    Document   : salesorder_edit
    Created on : 10 Jul 13, 6:51:04
    Author     : Wiweka
--%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = " java.util.* ,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.util.Command,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.payment.PstCurrencyType,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.common.entity.payment.CurrencyType,
         com.dimata.pos.form.billing.*,
         com.dimata.pos.entity.billing.*,
         com.dimata.posbo.entity.masterdata.*,
         com.dimata.posbo.form.masterdata.*,
         com.dimata.pos.form.balance.*,
         com.dimata.pos.entity.balance.*,
         com.dimata.pos.entity.payment.CashPayments1,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*,
         com.dimata.pos.entity.search.*,
         com.dimata.pos.form.search.*,
         com.dimata.pos.session.billing.*"%>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.memberReg.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ include file = "../main/javainit.jsp" %>
<!--% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER);%>
<!--%@ include file = "../main/checkuser.jsp" %>

<%!    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;
    public static final String textMainHeader[][] = {
        {"List", "Sales Order"},
        {"List", "Sales Order"}
    };

    /* this constant used to list text of listHeader */
    public static final String textListMaterialHeader[][] = {
        {"No", "Kode", "Tanggal", "Nama Customer", "Alamat", "Telepon/Fax", "Email", "Mata Uang", "Total", "Paid", "Sales Person", "Status", "detail"},
        {"No", "Code", "Date", "Customer Name", "Address", "Telepon/Fax", "Email", "Currency", "Total", "Paid", "Sales Person", "Status", "detail"}
    };

    public String drawList(int language, Vector objectClass, int start, int docType, I_DocStatus i_status) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("80%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListMaterialHeader[language][0], "3%");
            ctrlist.addHeader(textListMaterialHeader[language][1], "14%");
            ctrlist.addHeader(textListMaterialHeader[language][2], "7%");
            ctrlist.addHeader(textListMaterialHeader[language][3], "20%");
            ctrlist.addHeader(textListMaterialHeader[language][7], "7%");
            ctrlist.addHeader(textListMaterialHeader[language][8], "10%");
            //ctrlist.addHeader(textListMaterialHeader[language][9], "10%");
            //ctrlist.addHeader(textListMaterialHeader[language][10], "25%");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector vt = (Vector) objectClass.get(i);
                BillMain billMain = (BillMain) vt.get(0);
                MemberReg memberReg = (MemberReg) vt.get(1);
                CurrencyType currencyType = (CurrencyType) vt.get(2);
                AppUser appUser = (AppUser) vt.get(3);

                String cntName = memberReg.getCompName();
                if (cntName.length() == 0) {
                    cntName = String.valueOf(memberReg.getPersonName());
                }
                start = start + 1;

                Vector rowx = new Vector();
                rowx.add("" + start);//
                rowx.add(billMain.getInvoiceNumber());//

                String str_dt_BillDate = "";
                try {
                    Date dt_BillDate = billMain.getBillDate();
                    if (dt_BillDate == null) {
                        dt_BillDate = new Date();
                    }
                    str_dt_BillDate = Formater.formatDate(dt_BillDate, "dd-MM-yyyy");
                } catch (Exception e) {
                    str_dt_BillDate = "";
                }

                rowx.add(str_dt_BillDate);//               
                rowx.add(cntName);//

                CurrencyType currType = new CurrencyType();
                try {
                    currType = PstCurrencyType.fetchExc(billMain.getCurrencyId());
                } catch (Exception e) {
                }
                rowx.add(currType.getCode());//
                rowx.add(FRMHandler.userFormatStringDecimal(billMain.getAmount()));
                //rowx.add(FRMHandler.userFormatStringDecimal(billMain.getPaidAmount()));

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(billMain.getOID()));
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data sales order...</div>";
        }
        return result;
    }
%>

<%
            /**
             * get approval status for create document
             */
            I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
            I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
            I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
            int systemName = I_DocType.SYSTEM_MATERIAL;
            int docType = i_pstDocType.composeDocumentType(systemName, I_DocType.MAT_DOC_TYPE_POR);
%>

<%
            String poCode = "PO";
            String poTitle = textMainHeader[SESS_LANGUAGE][1];
            String poItemTitle = poTitle + " Item";
            String poTitleBlank = "";

            long oidBillMain = FRMQueryString.requestLong(request, "oidbillmaintmp");

            int iErrCode = FRMMessage.ERR_NONE;
            String msgStr = "";
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int recordToGet = 20;
            int vectSize = 0;
            String whereClause = "";

            ControlLine ctrLine = new ControlLine();
            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            SrcInvoice srcInvoice = new SrcInvoice();
            SessSales sessSales = new SessSales();
            FrmSrcInvoice frmSrcInvoice = new FrmSrcInvoice(request, srcInvoice);

            if (iCommand == Command.BACK || iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
                try {
                    srcInvoice = (SrcInvoice) session.getValue(SessSales.SESS_SRC_ORDERMATERIAL);
                    if (srcInvoice == null) {
                        srcInvoice = new SrcInvoice();
                    }
                } catch (Exception e) {
                    System.out.println(" Session null : " + e);
                    srcInvoice = new SrcInvoice();
                }
            } else {
                frmSrcInvoice.requestEntityObject(srcInvoice);
                Vector vectSt = new Vector(1, 1);
                String[] strStatus = request.getParameterValues(FrmSrcInvoice.fieldNames[FrmSrcInvoice.FRM_FLD_PRMSTATUS]);
                if (strStatus != null && strStatus.length > 0) {
                    for (int i = 0; i < strStatus.length; i++) {
                        try {
                            vectSt.add(strStatus[i]);
                        } catch (Exception exc) {
                            System.out.println("err");
                        }
                    }
                }
                srcInvoice.setPrmstatus(vectSt);
                session.putValue(SessSales.SESS_SRC_ORDERMATERIAL, srcInvoice);
            }

            vectSize = sessSales.getCount(srcInvoice, docType);
            if (iCommand == Command.FIRST || iCommand == Command.NEXT || iCommand == Command.PREV || iCommand == Command.LAST || iCommand == Command.LIST) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }
            Vector records = sessSales.searchSalesOrder(srcInvoice, docType, start, recordToGet);
%>


<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
            function cmdAdd(){
                document.frmsalesorderlist.start.value=0;
                document.frmsalesorderlist.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>.value = <%=PstBillMain.DISC_TYPE_PCT%>;
                document.frmsalesorderlist.approval_command.value="<%=Command.SAVE%>";
                document.frmsalesorderlist.command.value="<%=Command.ADD%>";
                document.frmsalesorderlist.add_type.value="<%=ADD_TYPE_LIST%>";
                document.frmsalesorderlist.action="outlet_cashier.jsp";
                if(compareDateForAdd()==true)
                    document.frmsalesorderlist.submit();
            }

            function cmdEdit(oid){
                document.frmsalesorderlist.start.value=0;                
                document.frmsalesorderlist.oidbillmaintmp.value = oid;                
                document.frmsalesorderlist.approval_command.value="<%=Command.APPROVE%>";                
                document.frmsalesorderlist.command.value="<%=Command.EDIT%>";
                document.frmsalesorderlist.action="outlet_cashier.jsp";
                
                document.frmsalesorderlist.submit();
                
            }

            function cmdListFirst(){
                document.frmsalesorderlist.command.value="<%=Command.FIRST%>";
                document.frmsalesorderlist.action="salesorder_list.jsp";
                document.frmsalesorderlist.submit();
            }

            function cmdListPrev(){
                document.frmsalesorderlist.command.value="<%=Command.PREV%>";
                document.frmsalesorderlist.action="salesorder_list.jsp";
                document.frmsalesorderlist.submit();
            }

            function cmdListNext(){
                document.frmsalesorderlist.command.value="<%=Command.NEXT%>";
                document.frmsalesorderlist.action="salesorder_list.jsp";
                document.frmsalesorderlist.submit();
            }

            function cmdListLast(){
                document.frmsalesorderlist.command.value="<%=Command.LAST%>";
                document.frmsalesorderlist.action="salesorder_list.jsp";
                document.frmsalesorderlist.submit();
            }

            function cmdBack(){
                document.frmsalesorderlist.command.value="<%=Command.BACK%>";
                document.frmsalesorderlist.action="srcsalesordecashier.jsp";
                document.frmsalesorderlist.submit();
            }

            //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
            function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                        if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
                }

                function MM_swapImage() { //v3.0
                    var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                        if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
                }
                //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
        </script>
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
                <!--
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

                function MM_findObj(n, d) { //v4.01
                    var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
                        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
                    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
                    for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
                    if(!x && d.getElementById) x=d.getElementById(n); return x;
                }
                //-->
        </SCRIPT>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg','<%=approot%>/images/BtnBackOn.jpg')">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                &nbsp;<%=textMainHeader[SESS_LANGUAGE][0]%> &gt; <%=textMainHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frmsalesorderlist" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="add_type" value="">
                                    <input type="hidden" name="start" value="<%=start%>">
                                    <input type="hidden" name="oidbillmaintmp" value="<%=oidBillMain%>">
                                    <input type="hidden" name="approval_command">
                                    <table width="100%" cellspacing="0" cellpadding="3">
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, docType, i_status)%></td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="8" align="left" colspan="3" class="command">
                                                <span class="command">
                                                    <%
                                                                ctrLine.setLocationImg(approot + "/images");
                                                                ctrLine.initDefault();
                                                                out.println(ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet));
                                                    %>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="18" valign="top" colspan="3">
                                                <table width="44%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                        <!--%if (privAdd) {%-->
                                                        <td nowrap width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ADD, true)%>"></a></td>
                                                        <td class="command" nowrap width="37%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_ADD, true)%></a></td>
                                                        <!--%}%-->
                                                        <td nowrap width="2%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK_SEARCH, true)%>"></a></td>
                                                        <td class="command" nowrap width="55%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK_SEARCH, true)%></a></td>
                                                    </tr>
                                                </table>
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
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <!-- #EndTemplate --></html>
