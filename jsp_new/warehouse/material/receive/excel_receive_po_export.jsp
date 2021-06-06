<%-- 
    Document   : excel_receive_po_export
    Created on : Mar 18, 2020, 2:25:03 PM
    Author     : Regen
--%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.ReceiveStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstReceiveStockCode"%>
<%@ page import="com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.qdep.form.FRMMessage,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.location.Location,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.qdep.form.FRMHandler,
         com.dimata.posbo.form.warehouse.CtrlMatReceive,
         com.dimata.posbo.form.warehouse.FrmMatReceive,
         com.dimata.posbo.entity.warehouse.MatReceive,
         com.dimata.posbo.form.warehouse.CtrlMatReceiveItem,
         com.dimata.posbo.form.warehouse.FrmMatReceiveItem,
         com.dimata.posbo.entity.warehouse.MatReceiveItem,
         com.dimata.posbo.entity.warehouse.PstMatReceiveItem,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.posbo.entity.masterdata.Unit,
         com.dimata.posbo.entity.masterdata.PstMaterial,
         com.dimata.posbo.entity.masterdata.Material,
         com.dimata.common.entity.payment.CurrencyType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<%    int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
%>
<!-- Jsp Block -->
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"Nomor", "Lokasi", "Tanggal", "Supplier", "Status", "Keterangan", "Nomor PO", "Nota Supplier", "Ppn", "Include", "%", "Kode BC", "Tanggal BC", "Lokasi Pabean", "Jenis Dokumen"},
        {"No", "Location", "Date", "Supplier", "Status", "Remark", "PO Code", "Supplier Invoice", "Ppn", "Include", "%", "Customs Code", "Date BC", "Pabean Location", "Document Type"}
    };
    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama", "Kadaluarsa", "Unit", "Harga Beli", "Harga Jual", "Mata Uang", "Qty", "Total Beli", "Barcode", "Total Jual", "HS Code"},
        {"No", "Code", "Name", "Expired Date", "Unit", "Cost", "Price", "Currency", "Qty", "Total Cost", "Barcode", "Total Jual", "HS Code"}
    };

    public static int getStrDutyFree() {
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
    int typePrint = FRMQueryString.requestInt(request, "type_print_tranfer");
    String syspropHSCode = PstSystemProperty.getValueByName("SHOW_HS_CODE");

    //adding dynamic sign rec by mirahu 20120427
    String signRec1 = PstSystemProperty.getValueByName("SIGN_RECEIVE_1");
    String signRec2 = PstSystemProperty.getValueByName("SIGN_RECEIVE_2");
    String signRec3 = PstSystemProperty.getValueByName("SIGN_RECEIVE_3");
    String[] sign1 = signRec1.split(",");
    String[] sign2 = signRec2.split(",");
    String[] sign3 = signRec3.split(",");

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
    String recCode = i_pstDocType.getDocCode(docType);
    String retTitle = "Penerimaan Barang"; //i_pstDocType.getDocTitle(docType);
    String recItemTitle = retTitle + " Item";

    /**
     * process on purchase order main
     */
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(Command.EDIT, oidReceiveMaterial);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();

    /**
     * check if document may modified or not
     */
    boolean privManageData = true;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceiveItem ctrlMatReceiveItem = new CtrlMatReceiveItem(request);
    ctrlMatReceiveItem.setLanguage(SESS_LANGUAGE);
    iErrCode = ctrlMatReceiveItem.action(iCommand, oidReceiveMaterialItem, oidReceiveMaterial);
    FrmMatReceiveItem frmMatReceiveItem = ctrlMatReceiveItem.getForm();
    MatReceiveItem recItem = ctrlMatReceiveItem.getMatReceiveItem();
    msgString = ctrlMatReceiveItem.getMessage();

    String whereClauseItem = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
    String orderClauseItem = "";
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);
    int recordToGetItem = 25;

    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    if (iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST) {
        startItem = ctrlMatReceiveItem.actionList(iCommand, startItem, vectSizeItem, recordToGetItem);
    }

//Vector listMatReceiveItem = PstMatReceiveItem.list(startItem,recordToGetItem,whereClauseItem);
    Vector listMatReceiveItem = PstMatReceiveItem.list(0, 0, whereClauseItem);
    if (listMatReceiveItem.size() < 1 && startItem > 0) {
        if (vectSizeItem - recordToGetItem > recordToGetItem) {
            startItem = startItem - recordToGetItem;
        } else {
            startItem = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST;
        }
        listMatReceiveItem = PstMatReceiveItem.list(startItem, recordToGetItem, whereClauseItem);
    }

    PurchaseOrder po = new PurchaseOrder();
    try {
        po = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
    } catch (Exception xxx) {
    }
        
    response.setContentType("application/x-msexcel");
    response.setHeader("Content-Disposition", "attachment; filename=" + "Receive Purchase With PO.xls");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .table-bordered{
                border: 2px solid #000000;
            }
            .text-center{
                text-align: center;
            }
            .table-bordered > thead > tr > th, 
            .table-bordered > tbody > tr > th, 
            .table-bordered > tfoot > tr > th, 
            .table-bordered > thead > tr > td, 
            .table-bordered > tbody > tr > td, 
            .table-bordered > tfoot > tr > td {
                border: 1px solid #f4f4f4;
            }
            table {
                border-collapse: collapse;
                border-spacing: 0;
            }
        </style>
    </head>
    <body>
        <div class="row">
            <div class="col-md-12">
                <h3 class="text-center" colspan="7">PENERIMAAN BARANG</h3>
                <table style="font-size: 10px; width: 100% !important" border="0">
                    <tbody>
                        <tr>
                            <td>Nomor</td>
                            <td> : <%=rec.getRecCode()%></td>
                            <td></td>
                            <td>Supplier</td>
                            <%
                                ContactList cnt = new ContactList();
                                try {
                                    cnt = PstContactList.fetchExc(rec.getSupplierId());
                                } catch (Exception e) {
                                }

                                String cntName = cnt.getCompName();
                                if (cntName.length() == 0) {
                                    cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                }
                            %>
                            <td> : <%=cntName%></td>
                        </tr>
                        <tr>
                            <td>Lokasi</td>

                            <%
                                Location loc = new Location();
                                try {
                                    loc = PstLocation.fetchExc(rec.getLocationId());
                                } catch (Exception e) {
                                }
                            %>
                            <td> : <%=loc.getName()%></td>
                            <td></td>
                            <td>Nomor PO</td>
                            <td> : <%= po.getPoCode()%></td>
                        </tr>
                        <tr>
                            <td>Tanggal</td>
                            <td> : <%=Formater.formatDate(rec.getReceiveDate(), "dd MMMM yyyy")%></td>
                            <td></td>
                            <td>Nota Supplier</td>
                            <td> : <%= rec.getInvoiceSupplier()%></td>
                        </tr>
                        <% if (getStrDutyFree() == 1) {%>
                        <tr>
                            <td>Nomor BC</td>
                            <td> : <%= rec.getNomorBc()%></td>
                            <td></td>
                            <td>Jenis Dokumen</td>
                            <td> : <%= rec.getJenisDokumen()%></td>
                        </tr>
                        <tr>
                            <td>Tanggal BC</td>
                            <td> : <%= Formater.formatDate(rec.getTglBc(), "dd MMMM yyyy")%></td>
                            <td></td>
                            <td>Lokasi Pabean</td>
                            <%
                                try {
                                    loc = PstLocation.fetchExc(rec.getLocationPabean());
                                } catch (Exception e) {
                                }
                            %>
                            <td> : <%=loc.getName()%></td>
                        </tr>
                        <%}%>

                    </tbody>
                </table>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table style="font-size: 14px; width: 100% !important" border="1">
                    <thead>
                        <tr align="center">
                            <td class="formElemen" width="4%"><%=textListOrderItem[SESS_LANGUAGE][0]%></td>
                            <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                            <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                            <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][10]%></td>
                            <% } else {%>
                            <td class="formElemen" width="10%"><%=textListOrderItem[SESS_LANGUAGE][1]%></td>
                            <% }%>
                            <% if (syspropHSCode.equals("1")) {%> 
                            <td width="" class="formElemen"><%=textListOrderItem[SESS_LANGUAGE][12]%></td>
                            <% }%>
                            <td class="formElemen"  width="20%"><%=textListOrderItem[SESS_LANGUAGE][2]%></td>
                            <td  class="formElemen" width="8%"><%=textListOrderItem[SESS_LANGUAGE][4]%></td>
                            <td class="formElemen" width="8%"><%=textListOrderItem[SESS_LANGUAGE][8]%></td>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            int start = 0;
                            double totalSellPrice = 0.0;
                            for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                Vector temp = (Vector) listMatReceiveItem.get(i);
                                MatReceiveItem recItemx = (MatReceiveItem) temp.get(0);
                                Material mat = (Material) temp.get(1);
                                Unit unit = (Unit) temp.get(2);
                                CurrencyType currencyType = (CurrencyType) temp.get(3);
                                String listStockCode = "";
                                mat.setOID(recItemx.getMaterialId());
                                if (typePrint == 1) {
                                    double priceSales = SessMaterial.getPriceSale(mat);
                                    recItemx.setCost(priceSales);
                                    recItemx.setTotal(priceSales * recItemx.getQty());
                                    totalSellPrice = totalSellPrice + recItemx.getTotal();
                                }
                                Category cat = new Category();
                                try {
                                    cat = PstCategory.fetchExc(mat.getCategoryId());
                                } catch (Exception e) {
                                }

                                double sellPrice = 0;
                                double totalSalesPrice = 0;
                                if (typePrint == 2) {
                                    sellPrice = SessMaterial.getPriceSale(mat);
                                    totalSalesPrice = sellPrice * recItemx.getQty();
                                    totalSellPrice = totalSellPrice + totalSalesPrice;
                                }

                                if (mat.getRequiredSerialNumber() == PstMaterial.REQUIRED) {
                                    String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID] + "=" + recItemx.getOID();
                                    Vector cntStockCode = PstReceiveStockCode.list(0, 0, where, "");
                                    for (int s = 0; s < cntStockCode.size(); s++) {
                                        ReceiveStockCode materialStockCode = (ReceiveStockCode) cntStockCode.get(s);
                                        if (s == 0) {
                                            listStockCode = listStockCode + "<br>&nbsp;SN : " + materialStockCode.getStockCode();
                                        } else {
                                            listStockCode = listStockCode + "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + materialStockCode.getStockCode();
                                        }

                                    }
                                }
                                start = start + 1;

                        %>
                        <tr >
                            <td width="4%" height="21" align="center" class="formElemen">&nbsp;<%=start%></td>
                            <%if (chooseTypeViewSkuOrBcd == PstMaterial.USE_BARCODE) {%>
                            <td  class="formElemen" width="10%">&nbsp;<%=mat.getSku()%></td>
                            <td  class="formElemen" width="10%">&nbsp;<%=mat.getBarCode()%></td>
                            <% } else {%>
                            <td  class="formElemen" width="10%">&nbsp;<%=mat.getSku()%></td>
                            <% }%>
                            <% if (syspropHSCode.equals("1")) {%>
                            <td  class="formElemen" width="10%">&nbsp;<%=cat.getCode()%></td>
                            <% }%>
                            <td class="formElemen" width="20%">&nbsp;<%=mat.getName()%><%=listStockCode%></td>
                            <td class="formElemen" align="center" width="8%">&nbsp;<%=unit.getCode()%></td>
                            <td class="formElemen" width="8%" align="center">&nbsp;<%=recItemx.getQty()%></td>
                        </tr>
                        <%}%>
                    </tbody>
                </table>
            </div>
        </div>
          <div class="row">
            <div class="col-md-12">
            <table style="font-size: 10px; width: 100% !important" border="0">
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr align="center">
                            <td><label><%=sign1[0] %></label></td>
                            <td></td>
                            <td><label><%=sign2[0] %></label></td>
                            <td></td>
                            <td><label><%=sign3[0] %></label></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label>(_________________________)</label></td>
                            <td></td>
                            <td><label>(_________________________)</label></td>
                            <td></td>
                            <td><label>(_________________________)</label></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
