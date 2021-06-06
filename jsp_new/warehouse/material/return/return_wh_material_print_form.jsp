<%@page import="com.dimata.posbo.entity.warehouse.ReturnStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReturnStockCode"%>
<%@
page import="com.dimata.posbo.form.warehouse.FrmMatReturnItem,
     com.dimata.posbo.entity.warehouse.MatReturnItem,
     com.dimata.gui.jsp.ControlList,
     com.dimata.posbo.entity.masterdata.*,
     com.dimata.qdep.form.FRMHandler,
     com.dimata.qdep.entity.I_PstDocType,
     com.dimata.qdep.form.FRMQueryString,
     com.dimata.qdep.form.FRMMessage,
     com.dimata.posbo.form.warehouse.CtrlMatReturnItem,
     com.dimata.gui.jsp.ControlLine,
     com.dimata.posbo.entity.warehouse.MatReturn,
     com.dimata.posbo.form.warehouse.FrmMatReturn,
     com.dimata.util.Command,
     com.dimata.posbo.form.warehouse.CtrlMatReturn,
     com.dimata.posbo.entity.warehouse.PstMatReturnItem,
     com.dimata.common.entity.contact.PstContactList,
     com.dimata.common.entity.contact.ContactList,
     com.dimata.common.entity.location.PstLocation,
     com.dimata.common.entity.location.Location,
     com.dimata.common.entity.payment.CurrencyType"
     %>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Pengirim", "Mengetahui", "Penerima"},
                {"No", "Location", "Date", "Supplier", "Status", "Remark", "Sent By", "Approve By", "Receice By"}
            };


    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][]
            = {
                {"No", "Sku", "Nama Barang", "Unit", "Harga Beli", "Harga Jual", "Mata Uang", "Qty", "Total Beli", "Barcode"},
                {"No", "Code", "Name", "Unit", "Cost", "Price", "Currency", "Qty", "Total Beli", "Barcode"}
            };

    /**
     * this method used to maintain poMaterialList
     */
    public String drawListRetItem(int language, int iCommand, FrmMatReturnItem frmObject, MatReturnItem objEntity, Vector objectClass, long retItemId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        String showHpp = PstSystemProperty.getValueByName("SHOW_HPP_RETURN");
        ctrlist.addHeader(textListOrderItem[language][0], "3%");
        ctrlist.addHeader(textListOrderItem[language][1], "15%");
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");

        if (showHpp.equals("1")) {
            ctrlist.addHeader(textListOrderItem[language][4], "10%");
        }

        ctrlist.addHeader(textListOrderItem[language][5], "10%");
        ctrlist.addHeader(textListOrderItem[language][7], "5%");
        ctrlist.addHeader(textListOrderItem[language][8], "5%");
        ctrlist.addHeader(textListOrderItem[language][9], "15%");

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
            MatReturnItem retItem = (MatReturnItem) temp.get(0);
            Material mat = (Material) temp.get(1);
            Unit unit = (Unit) temp.get(2);
            CurrencyType currencyType = (CurrencyType) temp.get(3);
            rowx = new Vector();
            start = start + 1;
            rowx.add("<div align=\"center\">" + start + "</div>");
            rowx.add(mat.getSku());
            rowx.add(mat.getName());
            rowx.add("<div align=\"center\">" + unit.getCode() + "</div>");
            if (showHpp.equals("1")) {
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(retItem.getCost()) + "</div>");
            }
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()) + "</div>");
            rowx.add("<div align=\"center\">" + currencyType.getCode() + "</div>");
            rowx.add("<div align=\"center\">" + String.valueOf(retItem.getQty()) + "</div>");
            rowx.add("<div align=\"right\">" + Formater.formatNumber(retItem.getTotal(), "##,###.00") + "</div>");
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
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_ROMR);
%>
<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReturnMaterial = FRMQueryString.requestLong(request, "hidden_return_id");
    long oidReturnMaterialItem = FRMQueryString.requestLong(request, "hidden_return_item_id");

//adding dynamic sign ret by mirahu 20120427
    String signRet1 = PstSystemProperty.getValueByName("SIGN_RETURN_1");
    String signRet2 = PstSystemProperty.getValueByName("SIGN_RETURN_2");
    String signRet3 = PstSystemProperty.getValueByName("SIGN_RETURN_3");
    String showHpp = PstSystemProperty.getValueByName("SHOW_HPP_RETURN");

//adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
    }
    chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);
    /**
     * initialization of some identifier
     */
    int iErrCode = FRMMessage.NONE;
    String msgString = "";

    /**
     * purchasing pr code and title
     */
    String retCode = "";//i_pstDocType.getDocCode(docType);
    String retTitle = "Retur ke Supplier"; //i_pstDocType.getDocTitle(docType);
    String retItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReturn ctrlMatReturn = new CtrlMatReturn(request);
    iErrCode = ctrlMatReturn.action(Command.EDIT, oidReturnMaterial);
    FrmMatReturn frmMatReturn = ctrlMatReturn.getForm();
    MatReturn ret = ctrlMatReturn.getMatReturn();

    /**
     * check if document already closed or not
     */
    boolean documentClosed = false;
    if (ret.getReturnStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
        documentClosed = true;
    }

    /**
     * check if document may modified or not
     */
    ControlLine ctrLine = new ControlLine();
    CtrlMatReturnItem ctrlMatReturnItem = new CtrlMatReturnItem(request);
    ctrlMatReturnItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReturnItem.action(iCommand, oidReturnMaterialItem, oidReturnMaterial);
    FrmMatReturnItem frmMatReturnItem = ctrlMatReturnItem.getForm();
    MatReturnItem retItem = ctrlMatReturnItem.getMatReturnItem();
    msgString = ctrlMatReturnItem.getMessage();

    String whereClauseItem = PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial;
    String orderClauseItem = "";
    int vectSizeItem = PstMatReturnItem.getCount(whereClauseItem);
    int recordToGetItem = 25;

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatReturnItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

    Vector listMatReturnItem = PstMatReturnItem.list(startItem, recordToGetItem, whereClauseItem);
    if (listMatReturnItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listMatReturnItem = PstMatReturnItem.list(startItem, recordToGetItem, whereClauseItem);
    }
%>
<html>
    <!-- #BeginTemplate "/Templates/print.dwt" --> 
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
                                <form name="frm_retmaterial" method ="post" action="">
                                    <input type="hidden" name="command" value="<%=iCommand%>">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="hidden_return_id" value="<%=oidReturnMaterial%>">
                                    <input type="hidden" name="hidden_return_item_id" value="<%=oidReturnMaterialItem%>">
                                    <input type="hidden" name="<%=FrmMatReturnItem.fieldNames[FrmMatReturnItem.FRM_FIELD_RETURN_MATERIAL_ID]%>" value="<%=oidReturnMaterial%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <table width="100%" cellpadding="1" cellspacing="0">
                                        <tr> 
                                            <td valign="top" colspan="3">&nbsp; </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3" class="title" align="center"><img src="../../../images/company_pdf.jpg" width="120" ></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title" align="center"><b>DOKUMEN&nbsp;<%=retTitle.toUpperCase()%></b></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title" valign="top" align="center"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="12%" align="left" nowrap><%=retCode%> <%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="39%" align="left"> : <%=ret.getRetCode()%> </td>
                                                        <td align="left" valign="bottom" width="4%"> 
                                                        <td align="right" valign="bottom" width="45%"><%=textListOrderHeader[SESS_LANGUAGE][3]%> : 
                                                            <%
                                                                ContactList cnt = new ContactList();
                                                                try {
                                                                    cnt = PstContactList.fetchExc(ret.getSupplierId());
                                                                } catch (Exception e) {
                                                                }
                                                                String cntName = cnt.getCompName();
                                                                if (cntName.length() == 0) {
                                                                    cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                                }
                                                            %>
                                                            <%=cntName%> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td width="39%" align="left">: <%=Formater.formatDate(ret.getReturnDate(), "dd MMMM yyyy")%></td>
                                                        <td align="left" valign="top" width="4%"> 
                                                            <%//=strComboStatus%>
                                                        </td>
                                                        <td align="right" valign="top" width="45%"><%=textListOrderHeader[SESS_LANGUAGE][1]%>: 
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(ret.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc1.getName()%></td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr> 
                                            <td valign="top"> 
                                                <table width="100%" cellpadding="1" cellspacing="1">
                                                    <tr> 
                                                        <td colspan="2"> 
                                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                                                                <tr align="left" valign="top"> 
                                                                    <td height="22" valign="middle" colspan="3"> 
                                                                        <table width="100%" border="1" cellspacing="0" cellpadding="0">
                                                                            <tr align="center">
                                                                                <td width="5%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                                <%if (false) {%>
                                                                                <td width="11%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                                                <td width="25%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                                <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                                <% } else {%>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                <td width="11%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                                <td width="11%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                                                <%} else {%>
                                                                                <td width="11%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                                <%}%>
                                                                                <td width="25%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                                <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                                <% if (privShowQtyPrice) {%>
                                                                                <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                                <td width="9%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                                <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                                                <%} else {%>
                                                                                <td width="9%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%
                                                                                int start = 0;
                                                                                for (int i = 0; i < listMatReturnItem.size(); i++) {
                                                                                    Vector temp = (Vector) listMatReturnItem.get(i);
                                                                                    MatReturnItem recItemx = (MatReturnItem) temp.get(0);
                                                                                    Material mat = (Material) temp.get(1);
                                                                                    Unit unit = (Unit) temp.get(2);
                                                                                    CurrencyType currencyType = (CurrencyType) temp.get(3);
                                                                                    String listStockCode = "";
                                                                                    if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                                                                                        String where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID] + "=" + recItemx.getOID();
                                                                                        Vector cntStockCode = PstReturnStockCode.list(0, 0, where, "");

                                                                                        for (int s = 0; s < cntStockCode.size(); s++) {
                                                                                            ReturnStockCode returnStockCode = (ReturnStockCode) cntStockCode.get(s);
                                                                                            if (s == 0) {
                                                                                                listStockCode = listStockCode + "<br>&nbsp;SN : " + returnStockCode.getStockCode();
                                                                                            } else {
                                                                                                listStockCode = listStockCode + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + returnStockCode.getStockCode();
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                    start = start + 1;

                                                                            %>
                                                                            <tr> 
                                                                                <td width="5%" align="center">&nbsp;<%=start%></td>

                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                <td width="11%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <td width="11%" class="listgensell">&nbsp;<%=mat.getBarCode()%></td>
                                                                                <%} else {%>
                                                                                <td width="11%" class="listgensell">&nbsp;<%=mat.getSku()%></td>
                                                                                <%}%>

                                                                                <td width="25%" class="listgensell">&nbsp;<%=mat.getName()%> <%=listStockCode%></td>
                                                                                <td width="10%" class="listgensell">&nbsp;<%=unit.getCode()%></td>
                                                                                <% if (privShowQtyPrice) {%>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                                                                                <td width="9%" align="right" class="listgensell">&nbsp;<%=recItemx.getQty()%></td>
                                                                                <td width="10%" align="right" class="listgensell">&nbsp;<%=FRMHandler.userFormatStringDecimal(recItemx.getTotal())%></td>
                                                                                <%} else {%>
                                                                                <td width="9%" align="right" class="listgensell">&nbsp;<%=recItemx.getQty()%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%}%>
                                                                            <%}%>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <% if (privShowQtyPrice) {%>
                                                    <%if (listMatReturnItem != null && listMatReturnItem.size() > 0) {%>
                                                    <tr>
                                                        <td valign="top"><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=ret.getRemark()%> </td>
                                                        <td width="27%" valign="top">

                                                            <%
                                                                if (showHpp.equals("1")) {
                                                            %>
                                                            <table width="100%" border="0">
                                                                <tr>
                                                                    <td width="44%">
                                                                        <div align="right"><%="TOTAL : " + retCode%></div>
                                                                    </td>
                                                                    <td width="15%">
                                                                        <div align="right"></div>
                                                                    </td>
                                                                    <td width="41%">
                                                                        <div align="right">
                                                                            <%
                                                                                String whereItem = "" + PstMatReturnItem.fieldNames[PstMatReturnItem.FLD_RETURN_MATERIAL_ID] + "=" + oidReturnMaterial;
                                                                                out.println(Formater.formatNumber(PstMatReturnItem.getTotal(whereItem), "##,###.00"));
                                                                            %>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            <%}%>    
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="100%">			  
                                        <tr align="left" valign="top"> 
                                            <td height="40" valign="middle" colspan="3"></td>
                                        </tr>
                                        <!--                      <tr>
                                                                
                                                          <td width="34%" align="center" nowrap><%=textListOrderHeader[SESS_LANGUAGE][6]%>,</td>
                                                                
                                                          <td align="center" valign="bottom" width="33%"><%=textListOrderHeader[SESS_LANGUAGE][7]%>,</td>
                                                                
                                                          <td width="33%" align="center"><%=textListOrderHeader[SESS_LANGUAGE][8]%>,</td> 
                                                              </tr>-->

                                        <tr>
                                            <%if (signRet1.equals(signRet1) && !signRet1.equals("Not initialized")) {%>  
                                            <td width="34%" align="center" nowrap><%=signRet1%></td>
                                            <%} else {%>  
                                            <td width="34%" align="center" nowrap>Pengirim,</td>
                                            <%}%>
                                            <%if (signRet2.equals(signRet2) && !signRet2.equals("Not initialized")) {%>  
                                            <td width="34%" align="center" nowrap><%=signRet2%></td>
                                            <%} else {%> 
                                            <td align="center" valign="bottom" width="33%">Mengetahui,</td>
                                            <%}%>
                                            <%if (signRet3.equals(signRet3) && !signRet3.equals("Not initialized")) {%>  
                                            <td width="34%" align="center" nowrap><%=signRet3%></td>
                                            <%} else {%> 
                                            <td width="33%" align="center">Penerima,</td> 
                                            <%}%>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                            <td height="75" valign="middle" colspan="3"></td>
                                        </tr>
                                        <tr>
                                            <td width="34%" align="center" nowrap>
                                                (.................................)
                                            </td>
                                            <td align="center" valign="bottom" width="33%">
                                                (.................................)
                                            </td>
                                            <td width="33%" align="center">
                                                (.................................)
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
    <!-- #EndTemplate -->
</html>




