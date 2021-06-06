<%@ page language = "java" %>

<!-- package java -->

<%@ page import = "java.util.*,

         com.dimata.posbo.form.warehouse.*,

         com.dimata.posbo.entity.warehouse.*,

         com.dimata.posbo.entity.masterdata.Material,

         com.dimata.posbo.session.masterdata.SessMaterial,

         com.dimata.posbo.entity.masterdata.Unit" %>

<!-- package dimata -->

<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->

<%@ page import = "com.dimata.gui.jsp.*" %>

<%@ page import = "com.dimata.qdep.entity.*" %>

<%@ page import = "com.dimata.qdep.form.*" %>

<!--package common -->

<%@ page import = "com.dimata.common.entity.location.*" %>

<%@ page import = "com.dimata.common.entity.contact.*" %>

<!--package material -->

<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.Material" %>



<%@ include file = "../../../main/javainit.jsp" %>

<%// int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING); %>
<%
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>

<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi Asal", "Lokasi Tujuan", "Tanggal", "Status", "Keterangan"},
                {"No", "Location", "Destination", "Date", "Status", "Remark"}

            };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"NO", "KODE", "NAMA BARANG", "UNIT", "QTY", "COST", "TOTAL", "HARGA JUAL", "BARCODE"},
                {"NO", "CODE", "DESCRIPTION", "UNIT", "QTY", "COST", "TOTAL", "SELL PRICE", "BARCODE"}

            };

    public double getPriceCost(Vector list, long oid) {

        double cost = 0.00;

        if (list.size() > 0) {

            for (int k = 0; k < list.size(); k++) {

                MatReceiveItem matReceiveItem = (MatReceiveItem) list.get(k);

                if (matReceiveItem.getMaterialId() == oid) {

                    cost = matReceiveItem.getCost();

                    break;

                }

            }

        }

        return cost;

    }

    /**
     *
     * this method used to maintain dfList
     *
     */
    public String drawListDfItem(int language, int iCommand,
            FrmMatDispatchItem frmObject, MatDispatchItem objEntity,
            Vector objectClass, long dfItemId, int start, String invoiceNumber, int tranUsedPriceHpp) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");

        ctrlist.setListStyle("listgen");

        ctrlist.setTitleStyle("listgentitle");

        ctrlist.setCellStyle("listgensell");

        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader(textListOrderItem[language][0], "3%");

        ctrlist.addHeader(textListOrderItem[language][1], "15%");

        ctrlist.addHeader(textListOrderItem[language][2], "20%");

        ctrlist.addHeader(textListOrderItem[language][3], "5%");

        ctrlist.addHeader(textListOrderItem[language][4], "5%");

        if (tranUsedPriceHpp == 0) {
            ctrlist.addHeader(textListOrderItem[language][5], "10%");
        } else {
            ctrlist.addHeader(textListOrderItem[language][7], "10%");
        }

        ctrlist.addHeader(textListOrderItem[language][6], "10%");

        Vector lstData = ctrlist.getData();

        Vector rowx = new Vector(1, 1);

        ctrlist.reset();

        ctrlist.setLinkRow(1);

        int index = -1;

        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {

            Vector temp = (Vector) objectClass.get(i);

            MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);

            Material mat = (Material) temp.get(1);

            Unit unit = (Unit) temp.get(2);

            rowx = new Vector();

            start = start + 1;

            double cost = 0; //getPriceCost(listItem,dfItem.getMaterialId());

            rowx.add("<div align=\"center\">" + start + "</div>");

            rowx.add(mat.getSku());

            rowx.add(mat.getName());

            rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");

            rowx.add("<div align=\"center\">" + String.valueOf(dfItem.getQty()) + "</div>");

            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(cost) + "</div>");

            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()) + "</div>");

            lstData.add(rowx);

        }

        return ctrlist.draw();

    }


%>

<%
    /**
     *
     * get approval status for create document *
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();

    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();

    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();

    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);

%>

<%    /**
     *
     * get request data from current form
     *
     */
    int iCommand = FRMQueryString.requestCommand(request);

    int startItem = FRMQueryString.requestInt(request, "start_item");

    int prevCommand = FRMQueryString.requestInt(request, "prev_command");

    int appCommand = FRMQueryString.requestInt(request, "approval_command");

    long oidDispatchMaterial = FRMQueryString.requestLong(request, "hidden_dispatch_id");

    long oidDispatchMaterialItem = FRMQueryString.requestLong(request, "hidden_dispatch_item_id");

    int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");

//adding dynamic sign rec by mirahu 20120427
    String signDf1 = PstSystemProperty.getValueByName("SIGN_TRANSFER_1");
    String signDf2 = PstSystemProperty.getValueByName("SIGN_TRANSFER_2");
    String signDf3 = PstSystemProperty.getValueByName("SIGN_TRANSFER_3");
    String[] sign1 = signDf1.split(",");
    String[] sign2 = signDf2.split(",");
    String[] sign3 = signDf3.split(",");

//adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
    }
    chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);

    /**
     *
     * initialization of some identifier
     *
     */
    int iErrCode = FRMMessage.NONE;

    String msgString = "";

    /**
     *
     * purchasing pr code and title
     *
     */
    String dfCode = ""; //i_pstDocType.getDocCode(docType);

    String dfTitle = "Dispatch Barang"; //i_pstDocType.getDocTitle(docType);

    String dfItemTitle = dfTitle + " Item";

    /**
     *
     * purchasing pr code and title
     *
     */
    String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF));

    /**
     *
     * process on df main
     *
     */
    CtrlMatDispatch ctrlMatDispatch = new CtrlMatDispatch(request);

    iErrCode = ctrlMatDispatch.action(Command.EDIT, oidDispatchMaterial);

    FrmMatDispatch frmMatDispatch = ctrlMatDispatch.getForm();

    MatDispatch dispatch = ctrlMatDispatch.getMatDispatch();

    /**
     *
     * check if document may modified or not      *
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();

    CtrlMatDispatchItem ctrlMatDispatchItem = new CtrlMatDispatchItem(request);

    ctrlMatDispatchItem.setLanguage(SESS_LANGUAGE);

    iErrCode = ctrlMatDispatchItem.action(iCommand, oidDispatchMaterialItem, oidDispatchMaterial);

    FrmMatDispatchItem frmMatDispatchItem = ctrlMatDispatchItem.getForm();

    MatDispatchItem dispatchItem = ctrlMatDispatchItem.getMatDispatchItem();

    msgString = ctrlMatDispatchItem.getMessage();

    String whereClauseItem = PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ID] + "=" + oidDispatchMaterial;

    String orderClauseItem = "";

    int vectSizeItem = PstMatDispatchItem.getCount(whereClauseItem);

    int recordToGetItem = 25;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {

        startItem = ctrlMatDispatchItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);

    }

//Update by Mirah
    String order = " DFI." + PstMatDispatchItem.fieldNames[PstMatDispatchItem.FLD_DISPATCH_MATERIAL_ITEM_ID];
//Vector listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial);
//Vector listMatDispatchItem = PstMatDispatchItem.list(startItem,recordToGetItem,oidDispatchMaterial, order);
    Vector listMatDispatchItem = PstMatDispatchItem.list(0, 0, oidDispatchMaterial, order);

    if (listMatDispatchItem.size() < 1 && startItem > 0) {

        if (vectSizeItem - recordToGetItem > recordToGetItem) {

            startItem = startItem - recordToGetItem;

        } else {

            startItem = 0;

            iCommand = Command.FIRST;

            prevCommand = Command.FIRST;

        }

        listMatDispatchItem = PstMatDispatchItem.list(startItem, recordToGetItem, oidDispatchMaterial);

    }

%>

<html><!-- #BeginTemplate "/Templates/print.dwt" -->

    <head>

        <!-- #BeginEditable "doctitle" --> 

        <title>Dimata - ProChain POS</title>

        <!-- #EndEditable -->

        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

        <!-- #BeginEditable "stylestab" --> 

        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">

        <!-- #EndEditable -->

    </head>  

    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    

        <table width="100%" border="0" cellspacing="0" cellpadding="0" >

            <tr>  

                <td width="88%" valign="top" align="left" height="56"> 

                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  

                        <tr> 

                            <td><!-- #BeginEditable "content" --> 

                                <form name="frm_matdispatch" method ="post" action="">

                                    <input type="hidden" name="command" value="<%=iCommand%>">

                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                                    <input type="hidden" name="start_item" value="<%=startItem%>">

                                    <input type="hidden" name="hidden_dispatch_id" value="<%=oidDispatchMaterial%>">

                                    <input type="hidden" name="hidden_dispatch_item_id" value="<%=oidDispatchMaterialItem%>">

                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">

                                    <table width="100%" cellpadding="1" cellspacing="0">

                                        <tr> 

                                            <td valign="top" colspan="3">&nbsp; </td>

                                        </tr>

                                        <tr align="center"> 

                                            <td colspan="3" class="title"><table width="100%" border="0" cellpadding="1">

                                                    <tr>
                                                        <td class="title" align="center" width="100%" ><img style="width: 100px; margin: 0 auto;" src="../../../images/company_pdf.jpg" alt="cd"></td>

                                                    </tr>
                                                    <tr>
                                                        <td class="title" align="center" width="100%"><b>&nbsp;<%=dfTitle.toUpperCase()%></b></td>
                                                    </tr>

                                                </table>                    <b>&nbsp;</b></td>

                                        </tr>

                                        <tr align="center"> 

                                            <td colspan="3" align="center"> 

                                                <table width="100%" border="0" cellpadding="1">

                                                    <tr> 

                                                        <td width="9%" align="left" nowrap><%=dfCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>

                                                        <td width="35%" align="left"> : <%=dispatch.getDispatchCode()%> </td>

                                                        <td align="center" valign="bottom" width="18%">&nbsp; </td>

                                                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : 

                                                            <%

                                                                Location loc1 = new Location();

                                                                try {

                                                                    loc1 = PstLocation.fetchExc(dispatch.getDispatchTo());

                                                                } catch (Exception e) {
                                                                }

                                                            %>

                                                            <%=loc1.getName()%> 

                                                    </tr>

                                                    <tr> 

                                                        <td width="9%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>

                                                        <td width="35%" align="left">: <%=Formater.formatDate(dispatch.getDispatchDate(), "dd MMMM yyyy")%> </td>

                                                        <td align="center" valign="bottom" width="18%"> 

                                                            <%//=strComboStatus%>

                                                        </td>

                                                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][1]%> : 

                                                            <%

                                                                Location loc2 = new Location();

                                                                try {

                                                                    loc2 = PstLocation.fetchExc(dispatch.getLocationId());

                                                                } catch (Exception e) {
                                                                }

                                                            %>

                                                            <%=loc2.getName()%> 

                                                    </tr>

                                                </table>

                                            </td>

                                        </tr>

                                        <tr> 

                                            <td valign="top"> 

                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >

                                                    <tr align="left" valign="top">

                                                        <td height="22" valign="middle" colspan="3">

                                                            <table width="100%" border="1" cellpadding="1" cellspacing="1" bordercolor="#000000" bgcolor="#FFFFFF">
                                                                <tr align="center"> 
                                                                    <td width="5%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                    <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                    <td width="10%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                                    <% } else {%>
                                                                    <td width="10%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                    <% }%>
                                                                    <td width="30%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                    <td width="10%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                    <td width="5%" bgcolor="#FFFFFF" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                </tr>
                                                                <%
                                                                    int start = 0;
                                                                    /**
                                                                     * get data receive for get price cost
                                                                     */
                                                                    String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + "='" + dispatch.getInvoiceSupplier() + "'";
                                                                    Vector list = PstMatReceive.list(0, 0, whereClause, "");
                                                                    Vector listItem = new Vector(1, 1);
                                                                    if (list != null && list.size() > 0) {
                                                                        MatReceive matReceive = (MatReceive) list.get(0);
                                                                        whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
                                                                        listItem = PstMatReceiveItem.list(0, 0, whereClause, "");
                                                                    }
                                                                    double total = 0;
                                                                    double totalQty = 0;
                                                                    for (int i = 0; i < listMatDispatchItem.size(); i++) {
                                                                        Vector temp = (Vector) listMatDispatchItem.get(i);
                                                                        MatDispatchItem dispatchItemx = (MatDispatchItem) temp.get(0);
                                                                        Material mat = (Material) temp.get(1);
                                                                        System.out.println("dispatchItemx.getMaterialId() : " + dispatchItemx.getMaterialId());
                                                                        mat.setOID(dispatchItemx.getMaterialId());
                                                                        Unit unit = (Unit) temp.get(2);

                                                                        String listStockCode = "";
                                                                        if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                                                                            String where = PstDispatchStockCode.fieldNames[PstDispatchStockCode.FLD_DISPATCH_MATERIAL_ITEM_ID] + "=" + dispatchItemx.getOID();
                                                                            Vector cntStockCode = PstDispatchStockCode.list(0, 0, where, "");
                                                                            for (int s = 0; s < cntStockCode.size(); s++) {
                                                                                DispatchStockCode dispatchStockCode = (DispatchStockCode) cntStockCode.get(s);
                                                                                if (s == 0) {
                                                                                    listStockCode = listStockCode + "<br>&nbsp;SN : " + dispatchStockCode.getStockCode();
                                                                                } else {
                                                                                    listStockCode = listStockCode + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + dispatchStockCode.getStockCode();
                                                                                }

                                                                            }
                                                                        }

                                                                        start = start + 1;
                                                                        if (typePrint == 1) {
                                                                            double priceSales = SessMaterial.getPriceSale(mat);
                                                                            dispatchItemx.setHpp(priceSales);
                                                                        } else if (typePrint == 2) {
                                                                            dispatchItemx.setHpp(0);
                                                                        }
                                                                        total += dispatchItemx.getHppTotal();
                                                                        totalQty += dispatchItemx.getQty();
                                                                %>
                                                                <tr bgcolor="#FFFFFF" class="listgensell">
                                                                    <td width="5%" align="center" class="listgensell">&nbsp;<%=start%></td>
                                                                    <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                    <td width="10%" class="listgensell">&nbsp;<%=mat.getBarCode()%></td>
                                                                    <% } else {%>
                                                                    <td width="10%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                    <% }%>
                                                                    <td width="30%" class="listgensell">&nbsp;<%=mat.getName()%><%=listStockCode%></td>
                                                                    <td width="10%" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                    <td width="5%" align="right" class="listgensell">&nbsp;<%=dispatchItemx.getQty()%></td>
                                                                </tr>
                                                                <%}%>
                                                                <tr bgcolor="#FFFFFF" class="listgensell">
                                                                    <% if (privShowQtyPrice) {%>
                                                                    <td colspan="4" align="right" class="listgensell"><strong>GRAND TOTAL </strong></td>
                                                                    <td width="5%" align="right" class="listgensell"><b>&nbsp;<%=FRMHandler.userFormatStringDecimal(totalQty)%></b></td>
                                                                    <%}%>
                                                                </tr>

                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=dispatch.getRemark()%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="40" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                        <%if (signDf1.equals(signDf1) && !signDf1.equals("Not initialized")) {%>
                                                        <td width="34%" align="center" nowrap><%=sign1[0]%>,</td>
                                                        <%} else {%>
                                                        <td width="34%" align="center" nowrap>Mengetahui,</td>
                                                        <%}%>
                                                        <%if (signDf2.equals(signDf2) && !signDf2.equals("Not initialized")) {%>
                                                        <td width="34%" align="center" nowrap><%=sign2[0]%>,</td>
                                                        <%} else {%>
                                                        <td align="center" valign="bottom" width="33%">Pengirim,</td>
                                                        <%}%>
                                                        <%if (signDf3.equals(signDf3) && !signDf3.equals("Not initialized")) {%>
                                                        <td width="34%" align="center" nowrap><%=sign3[0]%>,</td>
                                                        <%} else {%>
                                                        <td width="33%" align="center">Penerima,</td>
                                                        <%}%>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="75" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 

                                                        <td width="34%" align="center" nowrap> (<%=sign1[1] %>) 

                                                        </td>

                                                        <td align="center" valign="bottom" width="33%"> (<%=sign2[1] %>) 

                                                        </td>

                                                        <td width="33%" align="center"> (<%=sign3[1] %>) 

                                                        </td>

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

        </table>

    </body>

    <!-- #EndTemplate --></html>



