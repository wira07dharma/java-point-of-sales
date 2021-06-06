<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterialMappingType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstColor"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstKsg"%>
<%@page import="com.dimata.posbo.entity.masterdata.Color"%>
<%@page import="com.dimata.posbo.entity.masterdata.Ksg"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstPriceTypeMapping"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterGroup"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterGroup"%>
<%@page import="com.dimata.posbo.entity.masterdata.MasterGroupMapping"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMasterGroupMapping"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReceiveStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReceiveStockCode"%>
<%@ page import="com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.gui.jsp.ControlList,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.session.masterdata.SessMaterial,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.common.entity.payment.CurrencyType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%    
    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
    
%>

<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][]
            = {
                {"Nomor", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Nota Supplier", "Ppn", "Sub Total", "Include", "%","Kode BC", "Lokasi Pabean", "Tanggal BC", "Jenis Dokumen"},
                {"No", "Location", "Date", "Supplier", "Status", "Remark", "Supplier Invoice", "VAT", "Sub Total", "Include", "%","Customs Code", "Pabean Location", "Date BC", "Document Type"}
            };


    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
       {
           "No","Sku","Nama Barang","Kadaluarsa","Unit","Harga Beli","Ongkos Kirim","Mata Uang","Qty","Total Beli","Diskon Terakhir %",//10
           "Diskon1 %","Diskon2 %","Discount Nominal","Hapus","Barcode","Berat (gr)","Keterangan", "Sorting", "Warna","Etalase", "HS Code" //20
       },
       {
           "No","Code","Name","Expired Date","Unit","Cost","Delivery Cost","Currency","Qty","Total Cost","last Discount %","Discount1 %",
           "Discount2 %","Disc. Nominal","Delete","Barcode","Weight","Remark","Sorting","Color", "Etalase", "HS Code"
       }
    };

	public static int getStrDutyFree(){
		String strDutyFree = PstSystemProperty.getValueByName("ENABLE_DUTY_FREE");
		System.out.println("#Duty Free: " + strDutyFree);
		int dutyFree = Integer.parseInt(strDutyFree);
		return dutyFree;
	}

%>


<%
    /**
     * get approval status for create document
     */
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_LMRR);
%>


<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidReceiveMaterialItem = FRMQueryString.requestLong(request, "hidden_receive_item_id");
    int printExcel = FRMQueryString.requestInt(request, "printExcel");

    //adding dynamic sign rec by mirahu 20120427
    String signRec1 = com.dimata.system.entity.PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
    String signRec2 = com.dimata.system.entity.PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
    String signRec3 = com.dimata.system.entity.PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
    String[] sign1 = signRec1.split(",");
    String[] sign2 = signRec2.split(",");
    String[] sign3 = signRec3.split(",");
    
    
    boolean privApprovalApprove = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    String sEnableExpiredDate = PstSystemProperty.getValueByName("ENABLE_EXPIRED_DATE");
    boolean bEnableExpiredDate = (sEnableExpiredDate!=null && sEnableExpiredDate.equalsIgnoreCase("YES")) ? true : false;
    String syspropDiscount1 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_1");
    String syspropDiscount2 = PstSystemProperty.getValueByName("SHOW_DISCOUNT_2");
    String syspropDiscountNominal = PstSystemProperty.getValueByName("SHOW_DISCOUNT_NOMINAL");
    String syspropOngkosKirim = PstSystemProperty.getValueByName("SHOW_ONGKOS_KIRIM");
    String syspropBonus = PstSystemProperty.getValueByName("SHOW_BONUS");
    String syspropHargaBeli = PstSystemProperty.getValueByName("SHOW_HARGA_BELI");
    String syspropRecTypeDefault = PstSystemProperty.getValueByName("DEFAULT_RECEIVE_TYPE");
    String syspropColor = PstSystemProperty.getValueByName("SHOW_COLOR");
    String syspropEtalase = PstSystemProperty.getValueByName("SHOW_ETALASE");
    String syspropTotalBeli = PstSystemProperty.getValueByName("SHOW_TOTAL_BELI");
    String syspropHargaJual = PstSystemProperty.getValueByName("SHOW_HARGA_JUAL");
    String syspropHSCode = PstSystemProperty.getValueByName("SHOW_HS_CODE");
    String syspropKeterangan = PstSystemProperty.getValueByName("SHOW_KETERANGAN");

    //adding useBarcode or sku by mirahu 20120426
    int chooseTypeViewSkuOrBcd = 0;
    String useBarcodeorSku = PstSystemProperty.getValueByName("USE_BARCODE_OR_SKU_IN_REPORT");
    if (useBarcodeorSku.equals("Not initialized")) {
        useBarcodeorSku = "0";
    }
    chooseTypeViewSkuOrBcd = Integer.parseInt(useBarcodeorSku);

    String retTitle = "Laporan Penerimaan Detail";

    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    int iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    MatReceive rec = ctrlMatReceive.getMatReceive();
    Location recLoc = new Location();
    ContactList recSupp = new ContactList();
    try {
        recLoc = PstLocation.fetchExc(rec.getLocationId());
        recSupp = PstContactList.fetchExc(rec.getSupplierId());
    } catch (Exception e) {
        
    }

    CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
    ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReceiveItem.action(iCommand, oidReceiveMaterialItem, oidReceiveMaterial);
    String msgString = ctrlMatReceiveItem.getMessage();

    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
    String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    Vector listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClauseItem, orderClauseItem);

    if (printExcel == 1) {
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment; filename=" + "penerimaan_item.xls");
    }
%>
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <% if (printExcel == 0) { %>
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <link rel="stylesheet" href="../../../styles/print.css" type="text/css">
        <% } %>
        <style>
          img {
                margin: 20px 10px 10px 10px;
            }
            td.title {
                margin-bottom: 10px !important;
                padding-bottom: 20px !important;
            }
        </style>
    </head>
    <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">           
        <!-- #BeginEditable "content" --> 
        <form name="frm_recmaterial" method ="post" action="">
            <input type="hidden" name="command" value="<%=iCommand%>">
            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
            <input type="hidden" name="start_item" value="<%=startItem%>">
            <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
            <input type="hidden" name="hidden_receive_item_id" value="<%=oidReceiveMaterialItem%>">
            <input type="hidden" name="<%=FrmMatReceiveItem.fieldNames[FrmMatReceiveItem.FRM_FIELD_RECEIVE_MATERIAL_ID]%>" value="<%=oidReceiveMaterial%>">
            <input type="hidden" name="approval_command" value="<%=appCommand%>">

            <table width="100%" border="0" cellpadding="1">
              <% if (printExcel == 0) {%> 
              <tr>
                <td align="center" width="100%"><img width='100px' src="../../../images/yourcompany.jpg"></td>
              </tr> 
              <tr>
                <td class="title" align="center" width="100%"><b><%=retTitle.toUpperCase()%></b></td>
              </tr> 
              <% } else {%>
              <tr> 
                <td class="title" align="center" width="" colspan="10"><b><%=retTitle.toUpperCase()%></b></td>
              </tr>
              <% } %>
            </table>

            <table width="100%" border="0" cellpadding="3">
              <% if (printExcel == 0) {%>
              <tr>
                <td>Kode Penerimaan</td>
                <td> : <%= rec.getRecCode()%></td>
                <%if (getStrDutyFree() == 1) {%>
                <td><%= textListOrderHeader[SESS_LANGUAGE][11]%></td><!-- Kode BC / Bea Cukai -->
                <td>: <%=rec.getNomorBc()%></td>
                <% }%>
                <td>Lokasi</td>
                <td> : <%= recLoc.getName()%></td>
              </tr>
              <tr>
                <td>Tanggal Penerimaan</td>
                <td> : <%= rec.getReceiveDate()%></td>       
                <%if (getStrDutyFree() == 1) {%>
                <td><%= textListOrderHeader[SESS_LANGUAGE][13]%></td><!-- Tanggal BC / Bea Cukai -->
                <td>: <%=rec.getTglBc()%></td>
                <% }%>
                <td>Supplier</td>
                <td> : <%= recSupp.getCompName()%></td>
              </tr>
              <tr>
                <td>Nomor Invoice</td>
                <td> : <%= rec.getInvoiceSupplier()%></td>       
                <%if (getStrDutyFree() == 1) {%>
                <td><%= textListOrderHeader[SESS_LANGUAGE][14]%></td><!-- Tanggal BC / Bea Cukai -->
                <td>: <%=rec.getJenisDokumen()%></td>
                <td><%= textListOrderHeader[SESS_LANGUAGE][12]%></td><!-- Lokasi Pabean-->
                <%
                  Location loc = new Location();
                  try {
                    loc = PstLocation.fetchExc(rec.getLocationPabean());
                  } catch (Exception e) {
                  }
                %>
                <td> : <%=loc.getName()%></td>
                <% }%>

              </tr>
              <% } else {%>
              <tr>
                <td colspan="10">Kode Penerimaan : <%= rec.getRecCode()%></td>
              </tr>
              <tr>
                <td colspan="10">Tanggal Penerimaan : <%= rec.getReceiveDate()%></td>
              </tr>
              <tr>
                <td colspan="10">Lokasi : <%= recLoc.getName()%></td>
              </tr>
              <tr>
                <td colspan="10">Nomor Invoice : <%= rec.getInvoiceSupplier()%></td>
              </tr>
              <tr>
                <td colspan="10">Supplier : <%= recSupp.getCompName()%></td>
              </tr>
              <%if (getStrDutyFree() == 1) {%>
              <tr>
                <td colspan="10"><%= textListOrderHeader[SESS_LANGUAGE][11]%> : <%=rec.getNomorBc() %></td>
              </tr>
              <% }%>
              <tr>
                <%if (getStrDutyFree() == 1) {%>
                <%
                  Location loc = new Location();
                  try {
                    loc = PstLocation.fetchExc(rec.getLocationPabean());
                  } catch (Exception e) {
                  }
                %>
                <td><%= textListOrderHeader[SESS_LANGUAGE][12]%>  : <%=loc.getName()%></td><%}%>
              </tr>
              <tr>
                <%if (getStrDutyFree() == 1) {%>
                <td><%= textListOrderHeader[SESS_LANGUAGE][13]%></td><!-- Tanggal BC / Bea Cukai -->
                <td>: <%=rec.getTglBc()%></td>
                <% }%>
              </tr>
              <tr>
                <%if (getStrDutyFree() == 1) {%>
                <td><%= textListOrderHeader[SESS_LANGUAGE][14]%></td><!-- Jenis Dokumen BC / Bea Cukai -->
                <td>: <%=rec.getJenisDokumen()%></td>
                <% }%>
              </tr>
              <% }%>
            </table>

            <br>

            <table width="100%" cellpadding="1" cellspacing="1">
              <tr>
                <td colspan="2" >

                  <table width="100%" class="listgen" border="1" cellspacing="0" cellpadding="5">
                    <tr align="center">
                      <td width="1%" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                      <% if (typeOfBusiness.equals("0")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][15]%></td>
                      <% }%>
                      <% if (syspropHSCode.equals("1") && getStrDutyFree() == 1) {%> 
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][21]%></td>
                      <% } %>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                      <% if (bEnableExpiredDate) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][3]%></td>
                      <% } %>
                      <% if (typeOfBusinessDetail != 2) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                      <% } %>

                      <% if (privShowQtyPrice) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                      <% if (useForRaditya.equals("0") && syspropHargaBeli.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                      <% } else { %>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][5]%></td>
                      <% } %>
                      <% if (syspropDiscount1.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][11]%></td>
                      <% } %>
                      <% if (syspropDiscount2.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][12]%></td>
                      <% } %>
                      <% if (syspropDiscountNominal.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][13]%></td>
                      <% } %>
                      <% if (syspropOngkosKirim.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][6]%></td>
                      <% } %>
                      <% if (useForRaditya.equals("0") && syspropTotalBeli.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                      <% } %>
                      <% if (syspropHargaJual.equals("1")) { %>
                      <td width="" class="listgentitle">Harga Jual</td>
                      <% } %>
                      <% } else {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                      <% } %>
                      <% if (syspropEtalase.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][20]%></td>
                      <% } %>
                      <% if (syspropColor.equals("1")) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][19]%></td>
                      <% } %>
                      <!--printExcel == 0 || -->
                      <% if (syspropKeterangan.equals("1") && printExcel == 0) {%>
                      <td width="" class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][17]%></td>
                      <% } %>
                      <%
                        Vector listGroupMapping = PstMasterGroupMapping.list(0, 0, PstMasterGroupMapping.fieldNames[PstMasterGroupMapping.FLD_MODUL] + " = " + PstMasterGroupMapping.MODUL_RECEIVING, "");
                        if (listGroupMapping.size() > 0) {
                          for (int i = 0; i < listGroupMapping.size(); i++) {
                            MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(i);
                            MasterGroup masterGroup = new MasterGroup();
                            try {
                              masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                            } catch (Exception exc) {
                            }
                            out.print("<td width='' class='listgentitle'>" + masterGroup.getNamaGroup() + "</td>");
                          }
                        }
                      %>
                    </tr>
                    <%
                      int start = 0;
                      double totalharga = 0.0;
                      double totalSellPrice = 0.0;
                      double totalQty = 0.0;

                      for (int i = 0; i < listMatReceiveItem.size(); i++) {
                        start = start + 1;
                        Vector temp = (Vector) listMatReceiveItem.get(i);
                        MatReceiveItem recItemx = (MatReceiveItem) temp.get(0);
                        Material mat = (Material) temp.get(1);
                        Unit unit = (Unit) temp.get(2);
                        Ksg ksg = new Ksg();
                        Color color = new Color();
                        Category cat = new Category();
                        try {
                            cat = PstCategory.fetchExc(mat.getCategoryId());
                          } catch (Exception e) {
                          }
                        try {
                          if (PstKsg.checkOID(recItemx.getGondolaId())) {
                            ksg = PstKsg.fetchExc(recItemx.getGondolaId());
                          }
                          if (PstColor.checkOID(recItemx.getColorId())) {
                            color = PstColor.fetchExc(recItemx.getColorId());
                          }
                        } catch (Exception exc) {

                        }
                        double totalForwarderCost = recItemx.getForwarderCost() * recItemx.getQty();
                        double sellPrice = PstPriceTypeMapping.getSellPrice(recItemx.getMaterialId(), PstPriceTypeMapping.getOidStandartRate(), PstPriceTypeMapping.getOidPriceType());
                        totalQty += recItemx.getQty();
                        totalSellPrice += recItemx.getCost();
                    %>
                    <tr>
                      <td align="center" class="listgensell"><%=start%></td>
                      <td class="listgensell"><%=mat.getSku()%></td>
                      <% if (typeOfBusiness.equals("0")) {%>
                      <td class="listgentitle"><%= mat.getBarCode().toString()%>&nbsp;</td>
                      <% }%>
                      <% if (syspropHSCode.equals("1") && getStrDutyFree() == 1) {%>
                      <td align="center" class="listgentitle"><%=cat.getCode()%></td>
                      <% } %>
                      <td class="listgentitle"><%= mat.getName()%></td>
                      <% if (bEnableExpiredDate) {%>
                      <td class="listgentitle"><%= Formater.formatDate(recItemx.getExpiredDate(), "dd-MM-yyyy")%></td>
                      <% } %>
                      <% if (typeOfBusinessDetail != 2) {%>
                      <td align="center" class="listgentitle"><%= unit.getCode()%></td>
                      <% } %>

                      <% if (privShowQtyPrice) {%>
                      <td align="center" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getQty())%></td>
                      <% if (useForRaditya.equals("0") && syspropHargaBeli.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                      <% } else { %>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getCost())%></td>
                      <% } %>
                      <% if (syspropDiscount1.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getDiscount())%></td>
                      <% } %>
                      <% if (syspropDiscount2.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getDiscount2())%></td>
                      <% } %>
                      <% if (syspropDiscountNominal.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getDiscNominal())%></td>
                      <% } %>
                      <% if (syspropOngkosKirim.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getForwarderCost())%></td>
                      <% } %>
                      <%
                        if (useForRaditya.equals("0") && syspropTotalBeli.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(recItemx.getTotal() + totalForwarderCost)%></td>
                      <% } %>
                      <% if (syspropHargaJual.equals("1")) {%>
                      <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(sellPrice)%></td>
                      <% } %>

                      <% } else {%>
                      <td class="listgentitle"><%=textListOrderItem[SESS_LANGUAGE][9]%></td>
                      <% } %>
                      <% if (syspropEtalase.equals("1")) {%>
                      <td class="listgentitle"><%= ksg.getName()%></td>
                      <% } %>
                      <% if (syspropColor.equals("1")) {%>
                      <td class="listgentitle"><%= color.getColorName()%></td>
                      <% } %>
                      <!--printExcel == 0 ||--> 
                      <% if (syspropKeterangan.equals("1") && printExcel == 0) {%>
                      <td class="listgentitle"><%= recItemx.getRemark()%></td>
                      <% } %>
                      <%
                        if (listGroupMapping.size() > 0) {
                          for (int x = 0; x < listGroupMapping.size(); x++) {
                            MasterGroupMapping masterGroupMap = (MasterGroupMapping) listGroupMapping.get(x);
                            MasterGroup masterGroup = new MasterGroup();
                            try {
                              masterGroup = PstMasterGroup.fetchExc(masterGroupMap.getGroupId());
                            } catch (Exception exc) {
                            }

                            Vector vValue = new Vector(1, 1);
                            Vector vKey = new Vector(1, 1);
                            vValue.add("0");
                            vKey.add("-");
                            Vector listType = PstMasterType.list(0, 0, PstMasterType.fieldNames[PstMasterType.FLD_TYPE_GROUP] + "=" + masterGroup.getTypeGroup(), PstMasterType.fieldNames[PstMasterType.FLD_MASTER_NAME]);

                            long oidMapping = PstMaterialMappingType.getSelectedTypeId(masterGroup.getTypeGroup(), recItemx.getOID());
                            MasterType masterType = new MasterType();
                            String typeName = "";
                            try {
                              masterType = PstMasterType.fetchExc(oidMapping);
                              typeName = masterType.getMasterName();
                            } catch (Exception exc) {
                              typeName = "-";
                            }
                            out.print("<td class='listgentitle'>" + typeName + "</td>");
                          }
                        }
                      %>
                    </tr>
                    <%
                      }
                    %>
                    <tr>
                      <%if(getStrDutyFree() == 1){ %>
                      <td align="center" colspan="6"><b>TOTAL </b></td>
                      <%}else{%>
                      <td align="center" colspan="5"><b>TOTAL </b></td>
                      <%}%>
                      <td align="center" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(totalQty)%></td>
                      <% if (privShowQtyPrice){ %>
                        <td align="right" class="listgentitle"><%= FRMHandler.userFormatStringDecimal(totalSellPrice)%></td>
                      <% } %>
                      <% if (useForRaditya.equals("0") && printExcel == 0) { %>
                      <td colspan="4"></td>
                      <% } else if(useForRaditya.equals("0")){ %>
                      <td colspan="3"></td>
                      <% }%>
                    </tr>
                  </table>

                </td>
              </tr>
              <tr>
                <td><%=textListOrderHeader[SESS_LANGUAGE][5]%> : <%=rec.getRemark()%> </td>
              </tr>
              <tr>
                <td>User Cetak : <%= userName%></td>
              </tr>
              <tr>
                <td>Tanggal Cetak : <%=Formater.formatDate(new Date(), "dd MMMM yyyy")%></td>
              </tr>
              <%
                if (privShowQtyPrice && false) {
              %>
              <tr>
                <td width="35%" valign="top">
                  <table width="100%" border="0">
                    <%//
                      double ppn = rec.getTotalPpn();
                      if (ppn == 0) {
                        ppn = defaultPpn;
                      }
                      double totalBeliWithPPN = (totalharga * (ppn / 100)) + totalharga;
                      double valuePpn = 0.0;
                      if (rec.getIncludePpn() == 1) {
                        valuePpn = totalharga - (totalharga / 1.1);
                      } else if (rec.getIncludePpn() == 0) {
                        valuePpn = totalharga * (ppn / 100);
                      }
                    %>
                    <tr class="listgensell">
                      <td width="56%">
                        <div align="right"><input type="checkbox" disabled=\"true\" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if (rec.getIncludePpn() == 1) {%>checked<%}%>><%=textListOrderHeader[SESS_LANGUAGE][9]%><%=textListOrderHeader[SESS_LANGUAGE][7]%>
                          <input type="text" disabled=\"true\"  class="formElemen" name="<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TOTAL_PPN]%>" value="<%if (ppn != 0.0) {%><%=FRMHandler.userFormatStringDecimal(ppn)%><%} else {%><%=FRMHandler.userFormatStringDecimal(defaultPpn)%><%}%>"  size="5" style="text-align:right"><%=textListOrderHeader[SESS_LANGUAGE][10]%></div>
                      </td>
                      <td width="30%">
                        <div align="right"><b><%=FRMHandler.userFormatStringDecimal(valuePpn)%></b></div>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <%if ((listMatReceiveItem != null) && (listMatReceiveItem.size() > 0)) {%>
              <tr> 
                <td width="27%" valign="top"> 
                  <table width="100%" border="0">
                    <tr class="listgensell"> 
                      <td width="44%"> 
                        <div align="right"><%="Total"%></div>
                      </td>
                      <td width="15%"> 
                        <div align="right"></div>
                      </td>
                      <td width="41%"> 
                        <div align="right">
                          <% if (rec.getIncludePpn() == 1) {%>
                          <%=FRMHandler.userFormatStringDecimal(totalharga)%>

                          <%} else {%>
                          <%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%>
                          <%}%>
                        </div>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <%}%>
              <%
                }
              %>
            </table>

            <table width="100%">			  
              <tr align="left" valign="top"> 
                <td height="40" valign="middle" colspan="3"></td>
              </tr>
              <tr>
                <td width="34%" align="center"><%= sign1[0] %></td>
                <td width="33%" align="center"><%= sign2[0] %></td>
                <td width="33%" align="center"><%= sign3[0] %></td>
              </tr>
              <tr align="left" valign="top"> 
                <td height="75" valign="middle" colspan="3"></td>
              </tr>
              <tr>
                <td width="33%" align="center">( <%= sign1[1] %> )</td>
                <td width="33%" align="center">( <%= sign2[1] %> )</td>
                <td width="33%" align="center">( <%= sign3[1] %> )</td>
              </tr>
            </table>			  
        </form>
        <!-- #EndEditable -->
    </body>
    <!-- #EndTemplate -->
</html>
