<%-- 
    Document   : print_price_protection
    Created on : Jan 2, 2015, 6:28:55 PM
    Author     : dimata005
--%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.form.warehouse.*,
         com.dimata.posbo.entity.warehouse.*,
         com.dimata.posbo.entity.masterdata.Material,
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
<%
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING, AppObjInfo.OBJ_COSTING);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Lokasi ", "Tanggal", "Status", "Keterangan"},
        {"No", "Location", "Location", "Date", "Status", "Remark"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Kode", "Nama Barang", "Qty", "Amount", "Total Amount", "Total", "Harga Jual", "Barcode"},
        {"No", "Code", "Name", "Unit", "Qty", "Amount", "Total Amount", "Sell Price", "Barcode"}
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
     * this method used to maintain dfList
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

        /**
         * get data receive for get price cost
         */
        String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER] + "='" + invoiceNumber + "'";
        Vector list = PstMatReceive.list(0, 0, whereClause, "");
        Vector listItem = new Vector(1, 1);
        if (list != null && list.size() > 0) {
            MatReceive matReceive = (MatReceive) list.get(0);
            whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + matReceive.getOID();
            listItem = PstMatReceiveItem.list(0, 0, whereClause, "");
        }

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            MatDispatchItem dfItem = (MatDispatchItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            rowx = new Vector();
            start = start + 1;

            double cost = getPriceCost(listItem, dfItem.getMaterialId());

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
     * get approval status for create document 
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_DF);
%>
<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidPriceProtection = FRMQueryString.requestLong(request, "hidden_priceprotection_id");
    //long oidCostingMaterialItem = FRMQueryString.requestLong(request, "hidden_costing_item_id");

    //adding dynamic sign rec by mirahu 20120427
    String signCost1 = PstSystemProperty.getValueByName("PRICE_PROTECTION_1");
    String signCost2 = PstSystemProperty.getValueByName("PRICE_PROTECTION_2");
    String signCost3 = PstSystemProperty.getValueByName("PRICE_PROTECTION_3");
    int iErrCode = FRMMessage.ERR_NONE;
    String dfTitle = "PRICE PROTECTION";
    String dfCode = "";
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
    }
    chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);
    CtrlPriceProtection ctrlPriceProtection = new CtrlPriceProtection(request);

    iErrCode = ctrlPriceProtection.action(Command.EDIT , oidPriceProtection, userName, userId);
    FrmPriceProtection frmpp = ctrlPriceProtection.getForm();
    PriceProtection pp = ctrlPriceProtection.getPriceProtection();

    String whereClouse=PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
    String whereClousex="PPPI."+PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ID] + " = " + oidPriceProtection;
    int vectSizeItem = PstPriceProtectionItem.getCount(whereClouse);
    Vector listMatCostingItem = PstPriceProtectionItem.listInnerJoinMaterial(0,0,whereClousex,PstPriceProtectionItem.fieldNames[PstPriceProtectionItem.FLD_POS_PRICE_PROTECTION_ITEM_ID]);

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
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <tr> 
                <td width="88%" valign="top" align="left" height="56"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td><!-- #BeginEditable "content" --> 
                                <form name="frm_matdispatch" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_priceprotection_id" value="<%=oidPriceProtection%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr> 
                                            <td valign="top" colspan="3">&nbsp; </td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title"><b>DOKUMEN&nbsp;<%=dfTitle.toUpperCase()%></b></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" align="center"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="9%" align="left" nowrap><%=dfCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="35%" align="left"> : <%=pp.getNumberPP()%> </td>
                                                        <td align="center" valign="bottom" width="18%">&nbsp; </td>
                                                        <td width="38%" align="right"><%=textListOrderHeader[SESS_LANGUAGE][2]%> : 
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(pp.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc1.getName()%> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="9%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                        <td width="35%" align="left">: <%=Formater.formatDate(pp.getDateCreated(), "dd MMMM yyyy")%> </td>
                                                        <td align="center" valign="bottom" width="18%"> 
                                                        </td>
                                                        <td width="38%" align="right">&nbsp;
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td valign="top"> 
                                                <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                                                <tr align="center"> 
                                                                    <td width="5%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                    <td width="10%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                    <td width="30%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                    <td width="20%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                    <td width="15%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                    <td width="20%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                                                </tr>
                                                                <tr > 
                                                                <%
                                                                int start=0;
                                                                for(int i=0; i<listMatCostingItem.size(); i++)
                                                                {
                                                                     Vector temp = (Vector)listMatCostingItem.get(i);
                                                                     PriceProtectionItem priceProtectionItem = (PriceProtectionItem)temp.get(0);
                                                                     Material mat = (Material)temp.get(1);
                                                                     start = start + 1;
                                                                %>
                                                                    <td width="5%" class="listgentitle" > <%=start%></td>
                                                                    <td width="10%" class="listgentitle"><%=mat.getSku()%></td>
                                                                    <td width="30%" class="listgentitle"><%=mat.getName()%></td>
                                                                    <td width="20%" class="listgentitle" align="right"><%=priceProtectionItem.getStockOnHand()%></td>
                                                                    <td width="15%" class="listgentitle" align="right"><%=FRMHandler.userFormatStringDecimal(priceProtectionItem.getAmount())%></td>
                                                                    <td width="20%" class="listgentitle" align="right"><%=FRMHandler.userFormatStringDecimal(priceProtectionItem.getTotalAmount())%></td>
                                                                <%     
                                                                }
                                                                %>
                                                                </tr>
                                                                <tr class="listgensell">
                                                                    <td width="70%" colspan="5" align="right" class="listgensell">Total:</td>
                                                                    <td width="10%" align="right" class="listgensell"><b>&nbsp;<%=FRMHandler.userFormatStringDecimal(pp.getTotalAmount())%></b></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=pp.getRemark()%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="40" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                         <%if (signCost1.equals(signCost1) && !signCost1.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost1%>,</td>
                                                        <%} else {%>
                                                        <td width="34%" align="center" nowrap>Mengetahui,</td>
                                                        <%}%>
                                                        <%if (signCost2.equals(signCost2) && !signCost2.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost2%>,</td>
                                                        <%} else {%>
                                                        <td align="center" valign="bottom" width="33%">Pengirim,</td>
                                                        <%}%>
                                                         <%if (signCost3.equals(signCost3) && !signCost3.equals("Not initialized")) {%>
                                                            <td width="34%" align="center" nowrap><%=signCost3%>,</td>
                                                        <%} else {%>
                                                        <td width="33%" align="center">Penerima,</td>
                                                        <%}%>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                        <td height="75" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="34%" align="center" nowrap> (.................................) 
                                                        </td>
                                                        <td align="center" valign="bottom" width="33%"> (.................................) 
                                                        </td>
                                                        <td width="33%" align="center"> (.................................) 
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

