<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.common.entity.payment.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%
int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN);
int  appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%
boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan"},
        {"No", "Location", "Date", "Supplier", "Status", "Remark"}
    };


    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama Barang", "Unit", "HPP", "Harga Jual", "Mata Uang", "Qty", "Total", "Barcode"},
        {"No", "Code", "Name", "Unit", "Cost", "Sell Price", "Currency", "Qty", "Total", "Barcode"}
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

    if(showHpp.equals("1")){
        ctrlist.addHeader(textListOrderItem[language][4], "10%"); 
    }
        //ctrlist.addHeader(textListOrderItem[language][5],"10%");
        ctrlist.addHeader(textListOrderItem[language][6], "5%");
        ctrlist.addHeader(textListOrderItem[language][7], "5%");
        ctrlist.addHeader(textListOrderItem[language][8], "15%");

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

    if(showHpp.equals("1")){
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(retItem.getCost()) + "</div>");
    }
            //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
            rowx.add("<div align=\"center\">" + currencyType.getCode() + "</div>");
            rowx.add("<div align=\"center\">" + String.valueOf(retItem.getQty()) + "</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(retItem.getTotal()) + "</div>");
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
    int recordtoget = FRMQueryString.requestInt(request, "recordtoget");
//adding dynamic sign ret by mirahu 20120430
    String signRet1 = PstSystemProperty.getValueByName("SIGN_RETURN_1");
    String signRet2 = PstSystemProperty.getValueByName("SIGN_RETURN_2");
    String signRet3 = PstSystemProperty.getValueByName("SIGN_RETURN_3");
    String showHpp = PstSystemProperty.getValueByName("SHOW_HPP_RETURN");
	
    String nameSignRet1 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_1");
    String nameSignRet2 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_2");
    String nameSignRet3 = PstSystemProperty.getValueByName("NAME_SIGN_RETURN_3");

    //adding useBarcode or sku by mirahu 20120430
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
    String retCode = i_pstDocType.getDocCode(docType);
    String retTitle = i_pstDocType.getDocTitle(docType);
    String retItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReturn ctrlMatReturn = new CtrlMatReturn(request);
    iErrCode = ctrlMatReturn.action(Command.EDIT, oidReturnMaterial);
    FrmMatReturn frmMatReturn = ctrlMatReturn.getForm();
    MatReturn ret = ctrlMatReturn.getMatReturn();


    /**
     * check if document may modified or not 
     */
    boolean privManageData = true;

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
    int recordToGetItem =0;

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
	
	AppUser appUser = userSession.getAppUser();
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
                                            <td valign="top" colspan="3"><center><img src="../../../images/company_pdf.jpg" width="120"> </center></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title"><b>RETURN FAKTUR </b></td>
                                        </tr>
                                        <tr align="center"> 
                                            <td colspan="3" class="title" valign="top"> 
                                                <table width="100%" border="0" cellpadding="1">
                                                    <tr> 
                                                        <td width="12%" align="left" nowrap><%=textListOrderHeader[SESS_LANGUAGE][0]%></td>
                                                        <td width="28%" align="left"> : <%=ret.getRetCode()%> </td>
                                                        <td align="left" valign="bottom" width="29%"><%=textListOrderHeader[SESS_LANGUAGE][3]%> : 
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
                                                        <td align="left" valign="bottom" width="31%"> 
                                                    </tr>
                                                    <tr> 
                                                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                        <td width="28%" align="left">: 
                                                            <%
                                                                Location loc1 = new Location();
                                                                try {
                                                                    loc1 = PstLocation.fetchExc(ret.getLocationId());
                                                                } catch (Exception e) {
                                                                }
                                                            %>
                                                            <%=loc1.getName()%> </td>
                                                        <td align="left" valign="top" rowspan="2" width="29%"><%//=strComboStatus%></td>
                                                        <td align="left" valign="bottom" rowspan="2" width="31%">&nbsp; 
                                                        </td>
                                                    </tr>
                                                    <tr> 
                                                        <td width="12%" align="left"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                        <td width="28%" align="left">: <%=Formater.formatDate(ret.getReturnDate(), "dd MMMM yyyy")%></td>
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
                                                                    <td height="22" valign="middle" colspan="3"><table width="100%"  border="1" cellspacing="0" cellpadding="0">
                                                                            <tr align="center" class="listgentitle">
                                                                                <td width="7%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                    <td width="15%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                                    <td width="15%"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                                                                                <% } else {%>
                                                                                    <td width="15%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                                                                                <% } %>
                                                                                <td width="32%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                                                                                <td width="10%"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                                                                                <% if(privShowQtyPrice){%>
                                                                                    <%if (tranUsedPriceHpp == 0) {%>
                                                                                    <td width="14%"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                                                                                    <%} else {%>
                                                                                    <td width="14%"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                                                                                    <%}%>
                                                                                    <td width="7%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                                    <td width="15%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                                                                                <%}else{%>
                                                                                    <td width="7%"><%=textListOrderItem[SESS_LANGUAGE][7]%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%
                                                                                int stCount = startItem;
                                                                                for (int i = 0; i < listMatReturnItem.size(); i++) {
                                                                                    Vector temp = (Vector) listMatReturnItem.get(i);
                                                                                    MatReturnItem retItemx = (MatReturnItem) temp.get(0);
                                                                                    Material mat = (Material) temp.get(1);
                                                                                    Unit unit = (Unit) temp.get(2);
                                                                                    CurrencyType currencyType = (CurrencyType) temp.get(3);
                                                                                     String listStockCode= "";
                                                                                     if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                                                                                     String where = PstReturnStockCode.fieldNames[PstReturnStockCode.FLD_RETURN_MATERIAL_ITEM_ID]+"="+retItemx.getOID();
                                                                                     Vector cntStockCode = PstReturnStockCode.list(0,0,where,"");
                                                                                    
                                                                                     for (int s = 0; s < cntStockCode.size(); s++) {
                                                                                        ReturnStockCode returnStockCode = (ReturnStockCode) cntStockCode.get(s);
                                                                                        if(s==0){
                                                                                            listStockCode=listStockCode+"<br>&nbsp;SN : "+returnStockCode.getStockCode();
                                                                                        }else{
                                                                                             listStockCode=listStockCode+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+returnStockCode.getStockCode();
                                                                                        }

                                                                                      }
                                                                                    }
                                                                                    stCount = stCount + 1;
                                                                            %>
                                                                            <tr class="listgensell">
                                                                                <td align="center">&nbsp;<%=stCount%></td>
                                                                                <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                                                                                    <td>&nbsp;<%=mat.getSku()%></td>
                                                                                    <td>&nbsp;<%=mat.getBarCode()%></td>
                                                                                <% } else {%>
                                                                                <td>&nbsp;<%=mat.getSku()%></td>
                                                                                <% }%>
                                                                                <td>&nbsp;<%=mat.getName()%><%=listStockCode%></td>
                                                                                <td align="center">&nbsp;<%=unit.getCode()%></td>
                                                                                <% if(privShowQtyPrice){%>
                                                                                    <td align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(retItemx.getHargaJual())%></td>
                                                                                    <td align="right">&nbsp;<%=retItemx.getQty()%></td>
                                                                                    <td align="right">&nbsp;<%=FRMHandler.userFormatStringDecimal(retItemx.getTotal())%></td>
                                                                                <%}else{%>
                                                                                    <td align="right">&nbsp;<%=retItemx.getQty()%></td>
                                                                                <%}%>
                                                                            </tr>
                                                                            <%}%>
                                                                        </table></td>
                                                                </tr>
                                                                <tr align="left" valign="top"> 
                                                                    <%try {
                                                                    %>
                                                                    <td height="22" valign="middle" colspan="3"> <%--= drawListRetItem(SESS_LANGUAGE,iCommand,frmMatReturnItem, retItem,listMatReturnItem,oidReturnMaterialItem,startItem)--%> </td>
                                                                    <%                                  } catch (Exception e) {
                                                                            System.out.println(e);
                                                                        }
                                                                    %>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <% if(privShowQtyPrice){%>
                                                    <%if (listMatReturnItem != null && listMatReturnItem.size() > 0) {%>
                                                    <tr> 
                                                        <td valign="top">&nbsp; </td>
                                                        <td width="27%" valign="top"> 
                                                            <table width="100%" border="0">
                                                                <tr>
                                                                    <td width="44%"> <div align="right"><div><%="TOTAL QTY : " + retCode%></div></div></td>
                                                                    <td width="15%"> <div align="right"></div></td>
                                                                    <td width="41%"> <div align="right">
                                                                            <div>
                                                                                <%
                                                                                    //added by dewok 20190116, list for total qty item
                                                                                    double tot = 0;
                                                                                    Vector listMatReturnItemAll = PstMatReturnItem.list(0, 0, whereClauseItem);
                                                                                    for (int i=0; i < listMatReturnItemAll.size(); i++ ) {
                                                                                        Vector v = (Vector) listMatReturnItemAll.get(i);
                                                                                        MatReturnItem mri = (MatReturnItem) v.get(0);
                                                                                        tot += mri.getQty();
                                                                                    }
                                                                                    out.print(tot);
                                                                                %>
                                                                            </div>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                                <%
                                                                if(showHpp.equals("1")){
                                                                %>
                                                                <tr> 
                                                                    <td width="44%"> 
                                                                        <div align="right"><%="TOTAL : "%></div>
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
                                                                <%
                                                                }
                                                                %>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    <tr> 
                                                        <td colspan="2" valign="top"><%=textListOrderHeader[SESS_LANGUAGE][5]%> 
                                                            : <%=ret.getRemark()%></td>
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
											<%if (nameSignRet1.equals(nameSignRet1) && !nameSignRet1.equals("Not initialized") && !nameSignRet1.equals("0")) {%>
											<td width="34%" align="center" nowrap>( <u><%=nameSignRet1%></u> )</td>
											<%} else {%>  
                                            <td width="34%" align="center" nowrap>
                                                ( <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> )
                                            </td>
											<%} if (nameSignRet2.equals(nameSignRet2) && !nameSignRet2.equals("Not initialized") && !nameSignRet2.equals("0")) {%>
											<td width="34%" align="center" nowrap>( <u><%=nameSignRet2%></u> )</td>
											<%} else {%>  
                                            <td align="center" valign="bottom" width="33%">
                                               ( <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> )
                                            </td>
											<%} if (nameSignRet3.equals(nameSignRet3) && !nameSignRet3.equals("Not initialized") && !nameSignRet3.equals("0")) {%>
											<td width="34%" align="center" nowrap>( <u><%=nameSignRet3%></u> )</td>
											<%} else {%>  
                                            <td width="33%" align="center">
                                                ( <u>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
													&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u> )
                                            </td> 
											<% } %>
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


