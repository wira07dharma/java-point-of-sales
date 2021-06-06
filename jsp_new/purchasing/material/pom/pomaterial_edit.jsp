<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstDistributionPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.DistributionPurchaseOrder"%>
<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.posbo.form.purchasing.CtrlPurchaseOrder,
         com.dimata.posbo.form.purchasing.FrmPurchaseOrder,
         com.dimata.posbo.entity.purchasing.PurchaseOrder,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrderItem,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
         com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
         com.dimata.posbo.entity.purchasing.PurchaseOrderItem,
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
         com.dimata.common.entity.payment.CurrencyType" %>
<!-- package dimata -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER);%>
<%@ include file = "../../../main/checkuser.jsp" %>
<%    boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privClosed = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_CLOSED));
%>
<%!
    /* this constant used to list text of listHeader */
    public static final String textListOrderHeader[][] = {
        {"No", "Lokasi", "Tanggal", "Supplier", "Contact", "Alamat", "Telp.", "Terms", "Days", "Ppn", "Ket.", "Mata Uang", "Gudang", "Order Barang", "Nomor Revisi", "Status", "Include", "%", "Rate", "Include Print Harga Beli dan Total", "Kode BC"},
        {"No", "Location", "Date", "Supplier", "Contact", "Address", "Phone", "Terms", "Days", "Ppn", "Remark", "Currency", "Warehouse", "Purchase Order", "Revisi Number", "Status", "Include", "%", "Rate", "Include Print Purchase Price and Total", "Customs Code"}
    };

    /* this constant used to list text of listMaterialItem */
    public static final String textListOrderItem[][] = {
        {"No", "Sku", "Nama", "Qty @UnitStock", "Unit", "Hrg Beli Terakhir", "Hrg Beli", "Diskon Terakhir %",//8
            "Diskon1 %", "Diskon2 %", "Discount Nominal", "Netto Hrg Beli", "Total", "Qty Terima", "Hapus", "Tidak ada item order ...", "Barcode"},
        {"No", "Code", "Name", "Qty", "Unit", "Last Cost", "Cost", "last Discount %", "Discount1 %",
            "Discount2 %", "Disc. Nominal", "Netto Buying Price", "Total", "Qty Receive", "Delete", "No purchase data was Found", "Barcode"}
    };

    public static final String textDelete[][] = {
        {"Apakah Anda Yakin Akan Menghapus Data ?"},
        {"Are You Sure to Delete This Data? "}
    };

    public static final String textListGlobal[][] = {
        {"Pencarian Suplier"},
        {"Search Suplier"}
    };

    public String drawListOrderItem(int language, Vector objectClass, int start, boolean privManageData, long oidPurchaseOrder, double exchangeRate, String approot, String typeOfBusiness, int select_status) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0], "3%");//no
            ctrlist.addHeader(textListOrderItem[language][1], "10%");//sku
            if (typeOfBusiness.equals("0")) {
                //barcode
                ctrlist.addHeader(textListOrderItem[language][16], "10%");//sku
            }

            ctrlist.addHeader(textListOrderItem[language][2], "10%");//nama
            ctrlist.addHeader("Qty Beli", "3%");
            ctrlist.addHeader("Unit Beli", "3%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][13], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "3%");
            ctrlist.addHeader(textListOrderItem[language][5], "8%");
            ctrlist.addHeader(textListOrderItem[language][6], "8%");
            ctrlist.addHeader(textListOrderItem[language][7], "5%");
            ctrlist.addHeader(textListOrderItem[language][8], "5%");
            ctrlist.addHeader(textListOrderItem[language][9], "5%");
            ctrlist.addHeader(textListOrderItem[language][10], "8%");
            ctrlist.addHeader(textListOrderItem[language][11], "8%");
            ctrlist.addHeader(textListOrderItem[language][12], "8%");
            ctrlist.addHeader(textListOrderItem[language][14], "8%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                PurchaseOrderItem poItem = (PurchaseOrderItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                MatCurrency matCurrency = (MatCurrency) temp.get(3);
                Unit unitKonv = new Unit();
                try {
                    unitKonv = PstUnit.fetchExc(poItem.getUnitRequestId());
                } catch (Exception ex) {
                }
                rowx = new Vector();
                start = start + 1;

                rowx.add("" + start + "");
                if (privManageData) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(poItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }

                if (typeOfBusiness.equals("0")) {
                    rowx.add(mat.getBarCode());//barcode
                }

                rowx.add(mat.getName());
                rowx.add("" + poItem.getQtyRequest());
                rowx.add(unitKonv.getCode());

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getQuantity()) + "</div>");
                //adding qty rec in po by mirahu 20120427
                double qtyRec = 0.0;
                qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseOrder, poItem.getMaterialId());
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyRec) + "</div>");
                // unit
                rowx.add(unit.getCode());
                // harga beli
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getPrice()) + "</div>");
                // harga
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()) + "</div>");
                // discount
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount()) + "</div>");
                // discount 1
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount1()) + "</div>");
                // discount 2
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount2()) + "</div>");
                // disc nominal
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()) + "</div>");
                // curr harga beli
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()) + "</div>");
                // total
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getTotal()) + "</div>");
                // delete
                // add by fitra 17-05-2014
                if (privManageData && (select_status != 5 || select_status != 2 || select_status != 7 || select_status != 1 || select_status != 2)) {
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(poItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
                } else {
                    rowx.add("Delete");
                }
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListOrderItem[language][15] + "</div>";
        }
        return result;
    }

    public String drawListOrderBonusItem(int language, Vector objectClass, int start, boolean privManageData, long oidPurchaseOrder, double exchangeRate, String approot) {
        String result = "";
        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader(textListOrderItem[language][0], "3%");
            ctrlist.addHeader(textListOrderItem[language][1], "10%");
            ctrlist.addHeader(textListOrderItem[language][2], "15%");
            ctrlist.addHeader("Qty Beli", "5%");
            ctrlist.addHeader("Unit Beli", "5%");
            ctrlist.addHeader(textListOrderItem[language][3], "5%");
            ctrlist.addHeader(textListOrderItem[language][13], "5%");
            ctrlist.addHeader(textListOrderItem[language][4], "3%");
            ctrlist.addHeader(textListOrderItem[language][5], "8%");
            ctrlist.addHeader(textListOrderItem[language][6], "8%");
            ctrlist.addHeader(textListOrderItem[language][7], "5%");
            ctrlist.addHeader(textListOrderItem[language][8], "5%");
            ctrlist.addHeader(textListOrderItem[language][9], "5%");
            ctrlist.addHeader(textListOrderItem[language][10], "8%");
            ctrlist.addHeader(textListOrderItem[language][11], "8%");
            ctrlist.addHeader(textListOrderItem[language][12], "8%");
            ctrlist.addHeader(textListOrderItem[language][14], "8%");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1, 1);
            ctrlist.reset();
            int index = -1;
            if (start < 0) {
                start = 0;
            }

            for (int i = 0; i < objectClass.size(); i++) {
                Vector temp = (Vector) objectClass.get(i);
                PurchaseOrderItem poItem = (PurchaseOrderItem) temp.get(0);
                Material mat = (Material) temp.get(1);
                Unit unit = (Unit) temp.get(2);
                MatCurrency matCurrency = (MatCurrency) temp.get(3);
                Unit unitKonv = new Unit();
                try {
                    unitKonv = PstUnit.fetchExc(poItem.getUnitRequestId());
                } catch (Exception ex) {
                }
                rowx = new Vector();
                start = start + 1;

                rowx.add("" + start + "");
                if (privManageData) {
                    rowx.add("<a href=\"javascript:editItem('" + String.valueOf(poItem.getOID()) + "')\">" + mat.getSku() + "</a>");
                } else {
                    rowx.add(mat.getSku());
                }

                rowx.add(mat.getName());
                rowx.add("" + poItem.getQtyRequest());
                rowx.add(unitKonv.getCode());

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getQuantity()) + "</div>");
                //adding qty rec in po by mirahu 20120427
                double qtyRec = 0.0;
                qtyRec = PstMatReceiveItem.getQtyReceive(oidPurchaseOrder, poItem.getMaterialId());
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(qtyRec) + "</div>");
                // unit
                rowx.add(unit.getCode());
                // harga beli
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getPrice()) + "</div>");
                // harga
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getOrgBuyingPrice()) + "</div>");
                // discount
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount()) + "</div>");
                // discount 1
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount1()) + "</div>");
                // discount 2
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscount2()) + "</div>");
                // disc nominal
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getDiscNominal()) + "</div>");
                // curr harga beli
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getCurBuyingPrice()) + "</div>");
                // total
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(poItem.getTotal()) + "</div>");
                // delete
                // add by fitra 17-05-2014
                if (privManageData) {
                    rowx.add("<div align=\"center\"> <a href=\"javascript:cmdNewDelete('" + String.valueOf(poItem.getOID()) + "')\"><img src=" + approot + "/images/x3.png align=\"center\" ></a></div>");
                } else {
                    rowx.add("Delete");
                }
                lstData.add(rowx);
            }
            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListOrderItem[language][15] + "</div>";
        }
        return result;
    }


%>


<!-- Jsp Block -->
<%
    /**
     * get approval status for create document
     */
    long oidOrderLocation = Long.parseLong(PstSystemProperty.getValueByName("ORDER_RADITYA_LOCATION"));
    I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
    I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
    I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
    int docType = i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_POR);
%>


<%
    /**
     * get request data from current form
     */
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidPurchaseOrder = FRMQueryString.requestLong(request, "hidden_material_order_id");
    String textHistory = FRMQueryString.requestString(request, "text_history");
    int type = FRMQueryString.requestInt(request, "str_type");
    int poUseDirectPrinting = 0;
    int includePrintPrice = FRMQueryString.requestInt(request, "includePrinPrice");

// for printing document
    if (iCommand == Command.PRINT) {
        System.out.println(" JSP 0 ");
        String hostIpIdx = "";
        try {
            String command = request.getParameter("command");
            hostIpIdx = request.getParameter("printeridx");
            System.out.println("Print on " + hostIpIdx + " Command = " + iCommand);
            if (hostIpIdx != null) {
                PrinterHost prnHost = RemotePrintMan.getPrinterHost(hostIpIdx, ";");
                PrnConfig prn = RemotePrintMan.getPrinterConfig(hostIpIdx, ";");
                DSJ_PrintObj obj = InternalExternalPrinting.printForm(oidPurchaseOrder, type, companyAddress); //TestPrn.getTestObj();// get object to print !
                obj.setPrnIndex(prn.getPrnIndex());
                RemotePrintMan.printObj(prnHost, obj);
            }
        } catch (Exception exc3) {
            System.out.println("Print Exc " + exc3);
        }

        iCommand = Command.EDIT;
    }
    /**
     * initialization of some identifier
     */
    String errMsg = "";
    String getTmpHistory;
    int iErrCode = FRMMessage.ERR_NONE;

    /**
     * purchasing pr code and title
     */
    String poCode = ""; // i_pstDocType.getDocCode(docType);
    String poTitle = "PO"; //i_pstDocType.getDocTitle(docType);
    String poDocTitle = textListOrderHeader[SESS_LANGUAGE][13]; //i_pstDocType.getDocTitle(docType);
    String poItemTitle = poTitle + " Item";
    String poTitleBlank = "";

    /**
     * purchasing pr code and title
     */
    String prCode = i_pstDocType.getDocCode(i_pstDocType.composeDocumentType(I_DocType.SYSTEM_MATERIAL, I_DocType.MAT_DOC_TYPE_PRR));

    /**
     * action process
     */
    LogSysHistory logHistory = new LogSysHistory();

    ControlLine ctrLine = new ControlLine();
    CtrlPurchaseOrder ctrlPurchaseOrder = new CtrlPurchaseOrder(request);
    iErrCode = ctrlPurchaseOrder.action(iCommand, oidPurchaseOrder, userName, userId);
    FrmPurchaseOrder frmpo = ctrlPurchaseOrder.getForm();
    PurchaseOrder po = ctrlPurchaseOrder.getPurchaseOrder();
    errMsg = ctrlPurchaseOrder.getMessage();

    /**
     * if iCommand = Commmand.ADD ---> Set default rate which value taken from
     * PstCurrencyRate
     */
//double curr = PstCurrencyRate.getLastCurrency();
    if (iCommand == Command.ADD) {
        //po.setVendorExchangeRate(curr);
    }

    /**
     * generate code of current currency
     */
    String priceCode = "Rp.";
    /*if(po.getPriceCurrency()==PstCurrencyRate.RATE_CODE_USD)
{
        priceCode = "US$.";
}*/

    /**
     * check if document already closed or not
     */
    boolean documentClosed = false;
    if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
        documentClosed = true;
    }

    /**
     * check if document may modified or not
     */
    boolean privManageData = true;
    if (po.getPoStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
    }
    /**
     * list purchase order item
     */
//textHistory = logHistory.getHistory();
    oidPurchaseOrder = po.getOID();
    int recordToGetItem = 10;

    String whereClauseItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder
            + " AND " + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS] + "=0";
    String orderClauseItem = "";
    int vectSizeItem = PstPurchaseOrderItem.getCount(whereClauseItem);
    whereClauseItem = "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder
            + " AND " + "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS] + "=0";
    Vector listPurchaseOrderItem = PstPurchaseOrderItem.list(startItem, recordToGetItem, whereClauseItem);

//bonus item
    String whereClauseBonusItem = PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder
            + " AND " + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS] + "=1";
    int vectSizeBonusItem = PstPurchaseOrderItem.getCount(whereClauseBonusItem);
    whereClauseBonusItem = "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder
            + " AND " + "POI." + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_BONUS] + "=1";
    Vector listPurchaseOrderBonusItem = PstPurchaseOrderItem.list(startItem, recordToGetItem, whereClauseBonusItem);

    double defaultPpn = 0;
    defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));

    String enableInput = "";
    if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED || po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
        enableInput = "readonly";
    }

    long oidNewSupplier = Long.parseLong(PstSystemProperty.getValueByName("MAPPING_NEW_SUPPLIER_ID"));

    if (iCommand == Command.DELETE && iErrCode == 0) {
%>
<jsp:forward page="srcpomaterial.jsp">
    <jsp:param name="command" value="<%=Command.FIRST%>"/>
</jsp:forward>
<%
    }

//Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_CONTACT_CODE]);
    String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
            + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
            + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
            + " != " + PstContactList.DELETE;
    Vector vt_supp = PstContactList.listContactByClassType(0, 0, wh_supp, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
%>


<!-- FUNGSI UNTUK MENAMPILKAN HISTORY DATA PURCHASE ORDER -->
<%
    String msgString = "";
    long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
    Vector listDistributionPO = new Vector(1, 1);
//Vector listGetLastData = new Vector(1, 1);
    int start = FRMQueryString.requestInt(request, "start");
    int recordToGet = 0;
    String whereClausePurchaseOrder = " dpo." + PstDistributionPurchaseOrder.fieldNames[PstDistributionPurchaseOrder.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder;
    listDistributionPO = PstDistributionPurchaseOrder.getListWithLocationName(0, 0, whereClausePurchaseOrder, "");
%>
<%!    public String drawList(Vector objectClass, long oidLog) {

        ControlList ctrlist = new ControlList(); //membuat new class ControlList

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");

        ctrlist.addHeader("No", "10%");
        ctrlist.addHeader("Location", "10%");
        ctrlist.addHeader("Persentase Distribution(%)", "10%");
        ctrlist.setLinkRow(0);

        Vector lstData = ctrlist.getData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset();

        int index = -1;
        int noUrut = 0;
        for (int i = 0; i < objectClass.size(); i++) {
            DistributionPurchaseOrder distributionPurchaseOrder = (DistributionPurchaseOrder) objectClass.get(i);
            Vector rowx = new Vector();
            if (oidLog == distributionPurchaseOrder.getOID()) {
                index = i;
            }
            noUrut = noUrut + 1;
            rowx.add("" + noUrut);
            rowx.add("" + distributionPurchaseOrder.getLocationName());
            rowx.add("" + distributionPurchaseOrder.getQty());
            lstData.add(rowx);
        }
        return ctrlist.draw(index);
    }
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata - ProChain POS</title>
        <script language="JavaScript">
        //------------------------- START JAVASCRIPT FUNCTION FOR PO MAIN -----------------------

            function cmdAdd(){
            document.frm_purchaseorder.target = "";
            document.frm_purchaseorder.command.value = "<%=Command.VIEW%>";
            //document.frm_purchaseorder.hidden_material_order_id.value=0;
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }

            function cmdEdit(oid){
            document.frm_purchaseorder.command.value = "<%=Command.EDIT%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }






            function compare(){
            var dt = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_dy.value;
            var mn = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_mn.value;
            var yy = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE]%>_yr.value;
            var dt = new Date(yy, mn - 1, dt);
            var bool = new Boolean(compareDate(dt));
            return bool;
            }

            function cmdSave(){
            <%--
            <%if(privManageData){%>
            document.frm_purchaseorder.command.value="<%=Command.SAVE%>";
            <%}else{%>
            document.frm_purchaseorder.command.value="<%=Command.EDIT%>";
            <%}%>--%>
            document.frm_purchaseorder.command.value = "<%=Command.SAVE%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            if (compare() == true)
                    document.frm_purchaseorder.submit();
            }

            function cmdAsk(oid){
            document.frm_purchaseorder.command.value = "<%=Command.ASK%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }

            function cmdCancel(){
            document.frm_purchaseorder.command.value = "<%=Command.CANCEL%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }


            function cmdDelete(oid){
            if (confirm("Are you sure want to delete this?")){
            document.frm_purchaseorder.command.value = "<%=Command.DELETE%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.approval_command.value = "<%=Command.DELETE%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }}

            function cmdConfirmDelete(oid){
            document.frm_purchaseorder.command.value = "<%=Command.DELETE%>";
            document.frm_purchaseorder.hidden_mat_order_item_id.value = oid;
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.approval_command.value = "<%=Command.DELETE%>";
            document.frm_purchaseorder.action = "pomaterialitem.jsp";
            document.frm_purchaseorder.submit();
            }

        // add by fitra 17-05-2014
            function cmdNewDelete(oid){
            var msg;
            msg = "<%=textDelete[SESS_LANGUAGE][0]%>";
            var agree = confirm(msg);
            if (agree)
                    return cmdConfirmDelete(oid);
            else
                    return cmdEdit(oid);
            }


            function cmdAlert(){
            document.frm_purchaseorder.command.value = "<%=Command.FIRST%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "pomaterial_edit.jsp";
            document.frm_purchaseorder.submit();
            }



            function cmdBack(){
            document.frm_purchaseorder.command.value = "<%=Command.FIRST%>";
            document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
            document.frm_purchaseorder.action = "search_po_material.jsp";
            document.frm_purchaseorder.submit();
            }

            function printDirectForm()
            {        
            document.frm_purchaseorder.command.value = "<%=Command.PRINT%>";        
                    document.frm_purchaseorder.prev_command.value = "<%=prevCommand%>";
                    document.frm_purchaseorder.action = "pomaterial_edit.jsp";
                    document.frm_purchaseorder.submit();
                    }

            function changeCurrency(value){
            var oidCurrencyId = value;
            checkAjax(oidCurrencyId);
            }

            function checkAjax(oidCurrencyId){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE=" + oidCurrencyId + "&typeCheckCurrency=1",
                    type : "POST",
                    async : false,
                    success : function(data) {
                    document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>.value = data;
                    //document.frm_purchaseorder.exchangeRate.value=data;
                    }
            });
            }

            function changeTermOfService(value){
            var termOfService = value;
            var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
            if (termOfService == 1){
            //alert("ini adalah payment kredit")  
            checkAjaxDaysTermOfService(currId);
            } else{
            document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>.value = 0;
            }
            //checkAjax(oidCurrencyId);
            }

            function checkAjaxDaysTermOfService(currId){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.DaysTermOfService?oid_contact=" + currId,
                    type : "POST",
                    async : false,
                    success : function(data) {
                    document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>.value = data;
                    }
            });
            }

            function printForm(){
            window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderPrintPDF?hidden_material_order_id=<%=oidPurchaseOrder%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPoCode()%>");
                }

                function printOutForm(){
                var include = 0;
                var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
                if (includeprint){
                include = 1;
                }
                window.open("<%=approot%>/servlet/com.dimata.posbo.report.purchase.PurchaseOrderNoShippingPrintPDF?hidden_material_order_id=<%=oidPurchaseOrder%>&approot=<%=approot%>&sess_language=<%=SESS_LANGUAGE%>&/<%=po.getPoCode()%>&showprintprice=" + include);
                }

                function printFormHtml() {
                var include = 0;
                var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
                if (includeprint){
                include = 1;
                }
                window.open("po_material_print_form.jsp?hidden_material_order_id=<%=oidPurchaseOrder%>&command=<%=Command.EDIT%>&showprintprice=" + include, "receivereport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                }

                function printFormExcel() {
                var include = 0;
                var includeprint = document.frm_purchaseorder.includePrinPrice.checked;
                if (includeprint){
                include = 1;
                }
                window.open("po_material_print_excel.jsp?hidden_material_order_id=<%=oidPurchaseOrder%>&command=<%=Command.EDIT%>&showprintprice=" + include, "receivereport", "scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                }

                function klik(){
                var bool = new Boolean();
            <%
                if (listPurchaseOrderItem.size() > 0) {
            %>
                alert("Supplier tidak bisa di ubah\nKarena sudah ada data item");
                bool = true;
            <%} else {%>
                bool = false;
            <%}%>
                return bool;
                }

                function changeVendor(){
                //alert(document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>);
                //alert(document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value);
                if (!klik()){
                var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
                switch (currId){
            <%
                    if (vt_supp != null && vt_supp.size() > 0) {
                        for (int i = 0; i < vt_supp.size(); i++) {
                            ContactList contactList = (ContactList) vt_supp.get(i);
            %>
                case "<%=contactList.getOID()%>" :
                        document.frm_purchaseorder.hid_contact.value = "<%=contactList.getPersonName().length() > 0 ? contactList.getPersonName() : "-"%>";
                document.frm_purchaseorder.hid_addres.value = "<%=contactList.getBussAddress().length() > 0 ? contactList.getBussAddress() : "-"%>";
                document.frm_purchaseorder.hid_phone.value = "<%=contactList.getTelpNr().length() > 0 ? contactList.getTelpNr() : "-"%>";
                break;
            <%}
                    }%>
                default :
                        break;
                }
                } else{
                document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value = "<%=po.getSupplierId()%>";
                }
                }

                function changeVendorFisrt(){
                //alert(document.frm_purchaseorder.<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>);
                //alert(document.frm_purchaseorder.<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value);
                var currId = document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID]%>.value;
                switch (currId){
            <%
                if (vt_supp != null && vt_supp.size() > 0) {
                    for (int i = 0; i < vt_supp.size(); i++) {
                        ContactList contactList = (ContactList) vt_supp.get(i);
            %>
                case "<%=contactList.getOID()%>" :
                        document.frm_purchaseorder.hid_contact.value = "<%=contactList.getPersonName().length() > 0 ? contactList.getPersonName() : "-"%>";
                document.frm_purchaseorder.hid_addres.value = "<%=contactList.getBussAddress().length() > 0 ? contactList.getBussAddress() : "-"%>";
                document.frm_purchaseorder.hid_phone.value = "<%=contactList.getTelpNr().length() > 0 ? contactList.getTelpNr() : "-"%>";
                break;
            <%}
                }%>
                default :
                        break;
                }
                }

        //------------------------- END JAVASCRIPT FUNCTION FOR PO MAIN -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR PO ITEM -----------------------
                function addItem(){
                document.frm_purchaseorder.command.value = "<%=Command.ADD%>";
                document.frm_purchaseorder.action = "pomaterialitem.jsp";
                if (compareDateForAdd() == true)
                        document.frm_purchaseorder.submit();
                }

                function editItem(oid){
                document.frm_purchaseorder.command.value = "<%=Command.EDIT%>";
                document.frm_purchaseorder.hidden_mat_order_item_id.value = oid;
                document.frm_purchaseorder.action = "pomaterialitem.jsp";
                document.frm_purchaseorder.submit();
                }

                function itemList(comm){
                document.frm_purchaseorder.command.value = comm;
                document.frm_purchaseorder.prev_command.value = comm;
                document.frm_purchaseorder.action = "pomaterialitem.jsp";
                document.frm_purchaseorder.submit();
                }
        //------------------------- END JAVASCRIPT FUNCTION FOR PO ITEM -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------
                function addDelivery(){
                document.frm_purchaseorder.command.value = "<%=Command.ADD%>";
                document.frm_purchaseorder.action = "pomaterialdelivery.jsp";
                document.frm_purchaseorder.submit();
                }

                function editDelivery(oid){
                document.frm_purchaseorder.command.value = "<%=Command.EDIT%>";
                document.frm_purchaseorder.hidden_order_deliver_sch_id.value = oid;
                document.frm_purchaseorder.action = "pomaterialdelivery.jsp";
                document.frm_purchaseorder.submit();
                }

                function deliveryList(comm){
                document.frm_purchaseorder.command.value = comm;
                document.frm_purchaseorder.prev_command.value = comm;
                document.frm_purchaseorder.action = "pomaterialdelivery.jsp";
                document.frm_purchaseorder.submit();
                }
        //------------------------- END JAVASCRIPT FUNCTION FOR PO DELIVERY -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------
                function paymentList(comm){
                document.frm_purchaseorder.command.value = comm;
                document.frm_purchaseorder.action = "ordermaterialpayment.jsp";
                document.frm_purchaseorder.submit();
                }
        //------------------------- END JAVASCRIPT FUNCTION FOR PO PAYMENT -----------------------


        //------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
                function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr; for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
                }

                function MM_preloadImages() { //v3.0
                var d = document; if (d.images){ if (!d.MM_p) d.MM_p = new Array();
                var i, j = d.MM_p.length, a = MM_preloadImages.arguments; for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0){ d.MM_p[j] = new Image; d.MM_p[j++].src = a[i]; }}
                }

                function MM_findObj(n, d) { //v4.0
                var p, i, x; if (!d) d = document; if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                d = parent.frames[n.substring(p + 1)].document; n = n.substring(0, p); }
                if (!(x = d[n]) && d.all) x = d.all[n]; for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++) x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById) x = document.getElementById(n); return x;
                }

                function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments; document.MM_sr = new Array; for (i = 0; i < (a.length - 2); i += 3)
                        if ((x = MM_findObj(a[i])) != null){document.MM_sr[j++] = x; if (!x.oSrc) x.oSrc = x.src; x.src = a[i + 2]; }
                }

                function viewHistoryTable() {
                var strvalue = "../../../main/historypo.jsp?command=<%=Command.FIRST%>" +
                        "&oidDocHistory=<%=oidPurchaseOrder%>";
                window.open(strvalue, "material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                }

                function viewDistributionLocation() {
                var strvalue = "po_distribution_location.jsp?command=<%=Command.ADD%>" +
                        "&hidden_oidPurchaseOrder=<%=oidPurchaseOrder%>";
                window.open(strvalue, "material", "height=250,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                }
        //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <%if (menuUsed == MENU_ICON) {%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <link type="text/css" rel="stylesheet" href="../../../styles/bootstrap3.1/css/bootstrap.css">
        <link rel="stylesheet" href="../../../styles/main.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
        <!-- #EndEditable -->
        <script type="text/javascript" src="../../../styles/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap3.1/js/bootstrap.min.js"></script>
        <script type="text/javascript">
               $(document).ready(function(){
               var base = "<%=approot%>";
               var language = "<%= SESS_LANGUAGE%>";
               function ajaxReceive(url, data, type, append, title){
               $.ajax({
               url : "" + url + "",
                       data: "" + data + "",
                       type : "" + type + "",
                       async : false,
                       cache: false,
                       success : function(data) {
                       $('' + append + '').html(data);
                       onSuccess(data, title);
                       },
                       error : function(data){

                       }
               }).done(function(data){
               onDone(data, title, append);
               });
               }
               function onSuccess(data, title){

               }
               function onDone(data, title, append){
               if (title === "getListSuplier"){
               selectSuplier();
               }
               }
               function searchSuplierText(){
               $('#searchSuplierText').keyup(function(){
               getListSuplier();
               });
               }
               function getListSuplier(){
               var url = "" + base + "/AjaxReceive";
               var command = "<%=Command.LIST%>";
               var typeText = $('#searchSuplierText').val();
               var data = "command=" + command + "&typeText=" + typeText + "";
               ajaxReceive(url, data, "POST", "#dynamicPlaceSuplier", "getListSuplier");
               }
               function selectSuplier(){
               $('.selectSuplier').click(function(){
               var address = $(this).data('address');
               var contactname = $(this).data('contactname');
               var oidsuplier = $(this).data('oidsuplier');
               var namasuplier = $(this).data('namasuplier');
               var phone = $(this).data('phone');
               $('#suplierName').val(namasuplier);
               $('#hid_contact').val(contactname);
               $('#hid_addres').val(address);
               $('#hid_phone').val(phone);
               $('#FRM_FIELD_SUPPLIER_ID').val(oidsuplier);
               $('#modalSearchSuplier').modal('hide');
               });
               }
               $('#searchSuplier').click(function(){
               getListSuplier();
               $("#modalSearchSuplier").modal("show");
               });
               searchSuplierText();
               });
        </script>

    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if (menuUsed == MENU_PER_TRANS) {%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%} else {%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>

            <tr>
                <td valign="top" align="left">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
                                &nbsp;<%=textListOrderHeader[SESS_LANGUAGE][12]%> &gt; <%=textListOrderHeader[SESS_LANGUAGE][13]%><!-- #EndEditable --></td>
                        </tr>
                        <tr>
                            <td><!-- #BeginEditable "content" -->
                                <form name="frm_purchaseorder" method="post" action="">
                                    <input type="hidden" name="command" value="">
                                    <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                    <input type="hidden" name="start_item" value="<%=startItem%>">
                                    <input type="hidden" name="command_item" value="<%=cmdItem%>">
                                    <input type="hidden" name="approval_command" value="<%=appCommand%>">
                                    <input type="hidden" name="hidden_material_order_id" value="<%=oidPurchaseOrder%>">
                                    <input type="hidden" name="hidden_mat_order_item_id" value="">
                                    <input type="hidden" name="hidden_order_deliver_sch_id" value="">
                                    <input type="hidden" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_CODE]%>" value="<%=po.getPoCode()%>">
                                    <input type="hidden" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                                    <table width="100%" border="0">
                                        <tr valign="top">
                                            <td colspan="3">
                                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td width="30%" align="left" valign="top">&nbsp;</td>
                                                        <td align="center" valign="bottom" width="40%">&nbsp;</td>
                                                        <td width="30%" align="right"></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left" valign="top" width="30%">&nbsp;</td>
                                                        <td align="center" width="40%">&nbsp;</td>
                                                        <td align="right" class="comment" width="30%"></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="left" valign="top" width="37%">
                                                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td width="28%">
                                                                        <%
                                                                            // if(po.getPoCode()!="" && iErrCode==0)
                                                                            //{
                                                                            out.println(poCode + " " + textListOrderHeader[SESS_LANGUAGE][0]);
                                                                            //}
                                                                            //else
                                                                            //{
                                                                            // 	out.println("");
                                    // }
%>
                                                                    </td>
                                                                    <td width="3%">:</td>
                                                                    <td width="69%"><%=(po.getPoCode().length() == 0 ? "<b>- Otomatis -</b>" : "<b>" + po.getPoCode() + "</b>")%> </td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="28%"><%=textListOrderHeader[SESS_LANGUAGE][1]%></td>
                                                                    <td width="3%">:</td>
                                                                    <td width="69%">
                                                                        <%
                                                                            Vector val_locationid = new Vector(1, 1);
                                                                            Vector key_locationid = new Vector(1, 1);
                                                                            /*String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
                                                                          whereClause += " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
                                                                          Vector vt_loc = PstLocation.list(0, 0, whereClause, "");*/
                                                                            //add opie-eyek
                                                                            //algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
                                                                            String whereClause = " (" + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE
                                                                                    + " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE + ")";

                                                                            whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");

                                                                            Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                                                            for (int d = 0; d < vt_loc.size(); d++) {
                                                                                Location loc = (Location) vt_loc.get(d);
                                                                                val_locationid.add("" + loc.getOID() + "");
                                                                                key_locationid.add(loc.getName());
                                                                            }
                                                                            String select_locationid = "";
                                                                            if (useForRaditya.equals("1")) {
                                                                                select_locationid = "" + oidOrderLocation; //selected on combo box

                                                                            } else {
                                                                                select_locationid = "" + po.getLocationId(); //selected on combo box
                                                                            }
                                                                        %>
                                                                        <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="20" width="28%"><%=textListOrderHeader[SESS_LANGUAGE][2]%></td>
                                                                    <td width="3%">:</td>
                                                                    <td width="69%"><%=ControlDate.drawDateWithStyle(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PURCH_DATE], (po.getPurchDate() == null) ? new Date() : po.getPurchDate(), 0, -1, "formElemen", "")%></td>
                                                                </tr>
                                                                <tr>
                                                                    <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][11]%></td>
                                                                    <td>:</td>
                                                                    <td width="20%">
                                                                        <%
                                                                            long oidCurrency = 0;
                                                                            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                                            Vector vectCurrVal = new Vector(1, 1);
                                                                            Vector vectCurrKey = new Vector(1, 1);
                                                                            for (int i = 0; i < listCurr.size(); i++) {
                                                                                CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                                                                if (i == 0) {
                                                                                    oidCurrency = currencyType.getOID();
                                                                                }
                                                                                vectCurrKey.add(currencyType.getCode());
                                                                                vectCurrVal.add("" + currencyType.getOID());
                                                                            }
                                                                            //mencari rate yang berjalan
                                                                            if (oidPurchaseOrder != 0) {
                                                                                oidCurrency = po.getCurrencyId();
                                                                            }
                                                                            double resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrency);
                                                                        %>
                                                                        <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CURRENCY_ID], "formElemen", null, "" + po.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:changeCurrency(this.value)\"")%>
                                                                        &nbsp;&nbsp;
                                                                        <%=textListOrderHeader[SESS_LANGUAGE][18]%>&nbsp;&nbsp;
                                                                        <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_EXCHANGE_RATE]%>" type="text" class="formElemen" size="10" value="<%=po.getExchangeRate() != 0 ? po.getExchangeRate() : resultKonversi%>" <%=enableInput%>>
                                                                    </td>
                                                                </tr>
                                                                <!--<tr>
                                                                  <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][14]%></td>
                                                                  <td>:</td>
                                                                  <td><input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CODE_REVISI]%>" type="text" class="formElemen" size="10" value="<%=po.getCodeRevisi()%>" ></td>
                                                                </tr>-->
                                                                <% if (PstSystemProperty.getValueByName("ENABLE_DUTY_FREE").equals("1")) {%>
                                                                <!--								<tr>
                                                                                                                                        <td height="20"><%=textListOrderHeader[SESS_LANGUAGE][20]%></td>					
                                                                                                                                        <td> : </td>					
                                                                                                                                        <td>A01</td>					
                                                                                                                                </tr>-->
                                                                <%}%>
                                                            </table>
                                                        </td>
                                                        <td width="38%" align="center" valign="top">
                                                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][3]%></td>
                                                                    <td width="2%">:</td>
                                                                    <td width="79%">
                                                                        <%
                                                                            Vector val_supplier = new Vector(1, 1);
                                                                            Vector key_supplier = new Vector(1, 1);
                                                                            if (vt_supp != null && vt_supp.size() > 0) {
                                                                                for (int d = 0; d < vt_supp.size(); d++) {
                                                                                    ContactList cnt = (ContactList) vt_supp.get(d);
                                                                                    String cntName = cnt.getCompName();
                                                                                    if (cntName.length() == 0) {
                                                                                        cntName = cnt.getPersonName() + " " + cnt.getPersonLastname();
                                                                                    }

                                                                                    if (cntName.compareToIgnoreCase("'") >= 0) {
                                                                                        cntName = cntName.replace('\'', '`');
                                                                                    }

                                                                                    val_supplier.add(String.valueOf(cnt.getOID()));
                                                                                    key_supplier.add(cntName);
                                                                                }
                                                                            }
                                                                            String select_supplier = "" + po.getSupplierId();
                                                                        %>
                                                                        <div style="display:none">
                                                                            <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_SUPPLIER_ID], null, select_supplier, val_supplier, key_supplier, "onClick=\"javascript:klik()\" onChange=\"javascript:changeVendor()\"", "formElemen")%> 
                                                                        </div>                                
                                                                        <%
                                                                            String suplierReplaceName = "";
                                                                            if (oidNewSupplier == po.getSupplierId() && oidNewSupplier != 0) {
                                                                                if (po.getRemark().indexOf(";") == -1) {
                                                                                    String arrRemark[] = po.getRemark().split(":");
                                                                                    suplierReplaceName = arrRemark[1];
                                                                                } else {
                                                                                    String arrRemark[] = po.getRemark().split(";");
                                                                                    String arrSuplierName[] = arrRemark[0].split(":");
                                                                                    suplierReplaceName = arrSuplierName[1];
                                                                                }
                                                                            } else {
                                                                                ContactList objSuplier = new ContactList();
                                                                                if (po.getSupplierId() != 0) {
                                                                                    try {
                                                                                        objSuplier = PstContactList.fetchExc(po.getSupplierId());
                                                                                    } catch (Exception exc) {
                                                                                        System.out.println("HostLst:  " + exc);
                                                                                    }
                                                                                }
                                                                                suplierReplaceName = objSuplier.getCompName();
                                                                            }
                                                                        %>
                                                                        <div class="inputText" style="float:left">
                                                                            <input value='<%= suplierReplaceName%>'id="suplierName" type="text" size="28" name='suplierName' readonly>
                                                                        </div>
                                                                        <div class="spanImage" style="margin-top:5px;">
                                                                            <img style="cursor:pointer" src="../../../images/BtnSearch.jpg" id="searchSuplier">                    
                                                                        </div>

                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][4]%></td>
                                                                    <td width="2%">:</td>
                                                                    <td width="79%">
                                                                        <input name="hid_contact" id="hid_contact" disabled type="text" size="30" class="formElemen"></td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][5]%></td>
                                                                    <td width="2%">:</td>
                                                                    <td width="79%">
                                                                        <input name="hid_addres" id="hid_addres" disabled type="text" size="40" class="formElemen">                              </td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="19%"><%=textListOrderHeader[SESS_LANGUAGE][6]%></td>
                                                                    <td width="2%">:</td>
                                                                    <td width="79%">
                                                                        <input id="hid_phone" name="hid_phone"  disabled type="text" size="30" class="formElemen">                              </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                    <td>&nbsp;</td>
                                                                    <td><table>
                                                                            <tr>
                                                                                <td align="left"><%=textListOrderHeader[SESS_LANGUAGE][15]%></td>
                                                                                <td align="left">:
                                                                                    <%
                                                                                        Vector obj_status = new Vector(1, 1);
                                                                                        Vector val_status = new Vector(1, 1);
                                                                                        Vector key_status = new Vector(1, 1);

                                                                                        val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_DRAFT));
                                                                                        key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);

                                                                                        //add by fitra
                                                                                        if ((listPurchaseOrderItem != null) && (listPurchaseOrderItem.size() > 0)) {
                                                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED));
                                                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                                                        }

                                                                                        // update opie-eyek 19022013
                                                                                        // user bisa memfinalkan purchase request  jika  :
                                                                                        // 1. punya approve document pr = true
                                                                                        // 2. lokasi sumber (lokasi asal)  ada di lokasi-lokasi yg diassign ke user
                                                                                        boolean locationAssign = false;
                                                                                        locationAssign = PstDataCustom.checkDataCustom(userId, "user_location_map", po.getLocationId());
                                                                                        if ((listPurchaseOrderItem != null) && (listPurchaseOrderItem.size() > 0) && (locationAssign == true) && (privApproval == true)) {
                                                                                            if (!typeOfBusiness.equals("3")) {
                                                                                                val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_APPROVED));
                                                                                                key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]);
                                                                                            }
                                                                                            val_status.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                                                            key_status.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                                        }

                                                                                        String select_status = "" + po.getPoStatus();
                                                                                        if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED) {
                                                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                                                        } else if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_POSTED) {
                                                                                            out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                                                        } else if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) {
                                                                                            if ((listPurchaseOrderItem != null) && (listPurchaseOrderItem.size() > 0) && (locationAssign == true) && (privClosed == true)) {
                                                                                                Vector val_status2 = new Vector(1, 1);
                                                                                                Vector key_status2 = new Vector(1, 1);
                                                                                                val_status2.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_CLOSED));
                                                                                                key_status2.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                                                                val_status2.add(String.valueOf(I_DocStatus.DOCUMENT_STATUS_FINAL));
                                                                                                key_status2.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                                                out.println(ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_STATUS], null, select_status, val_status2, key_status2, "", "formElemen"));
                                                                                            } else {
                                                                                                out.println(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                                                            }

                                                                                        } else {
                                                                                    %>
                                                                                    <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PO_STATUS], null, select_status, val_status, key_status, "", "formElemen")%> </td>
                                                                                    <% }%>
                                                                                <td valign="bottom">&nbsp;</td>
                                                                                <td>&nbsp;</td>
                                                                                <td align="right"></td>
                                                                                <td valign="top"></td>
                                                                            </tr>
                                                                        </table></td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                        <td align="right" valign="top" class="hiddenTextMain" width="25%">
                                                            <table width="100%" border="0" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td width="27%"><%=textListOrderHeader[SESS_LANGUAGE][7]%></td>
                                                                    <td width="4%">:</td>
                                                                    <td width="69%">
                                                                        <%
                                                                            Vector val_terms = new Vector(1, 1);
                                                                            Vector key_terms = new Vector(1, 1);
                                                                            for (int d = 0; d < PstPurchaseOrder.fieldsPaymentType.length; d++) {
                                                                                val_terms.add(String.valueOf(d));
                                                                                key_terms.add(PstPurchaseOrder.fieldsPaymentType[d]);
                                                                            }
                                                                            String select_terms = "" + po.getTermOfPayment();
                                                                        %>
                                                                        <%=ControlCombo.draw(FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_TERM_OF_PAYMENT], null, select_terms, val_terms, key_terms, "onChange=\"javascript:changeTermOfService(this.value)\"", "formElemen")%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td width="27%"><%=textListOrderHeader[SESS_LANGUAGE][8]%></td>
                                                                    <td width="4%">:</td>
                                                                    <td width="69%">
                                                                        <input name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_CREDIT_TIME]%>" type="text" class="formElemen" style="text-align:right" size="5" value="<%=po.getCreditTime()%>">
                                                                    </td>
                                                                </tr>
                                                                <tr valign="top">
                                                                    <td width="27%"><%=textListOrderHeader[SESS_LANGUAGE][10]%></td>
                                                                    <td width="4%">:</td>
                                                                    <td width="69%">
                                                                        <%
                                                                            String remarkReplace = "";
                                                                            if (oidNewSupplier == po.getSupplierId()) {
                                                                                if (po.getRemark().indexOf(";") == -1) {
                                                                                    remarkReplace = "";
                                                                                } else {
                                                                                    String[] arrRemark = po.getRemark().split(";");
                                                                                    if (arrRemark.length > 1) {
                                                                                        for (int i = 1; i <= arrRemark.length - 1; i++) {
                                                                                            remarkReplace += arrRemark[i];
                                                                                        }

                                                                                    } else {
                                                                                        remarkReplace = "";
                                                                                    }
                                                                                }

                                                                            } else {
                                                                                remarkReplace = po.getRemark();
                                                                            }
                                                                        %>
                                                                        <textarea name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_REMARK]%>" cols="25" rows="3" wrap="VIRTUAL" class="formElemen"><%=remarkReplace%></textarea>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%=drawListOrderItem(SESS_LANGUAGE, listPurchaseOrderItem, startItem, privManageData, oidPurchaseOrder, po.getExchangeRate(), approot, typeOfBusiness, po.getPoStatus())%></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">Bonus Item</td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3"> <%=drawListOrderBonusItem(SESS_LANGUAGE, listPurchaseOrderBonusItem, startItem, privManageData, oidPurchaseOrder, po.getExchangeRate(), approot)%></td>
                                                    </tr>


                                                    <%if (oidPurchaseOrder != 0) {%>
                                                    <tr align="left" valign="top">
                                                        <td height="8" align="left" colspan="3" class="command">
                                                            <span class="command">
                                                                <%
                                                                    if (cmdItem != Command.FIRST && cmdItem != Command.PREV && cmdItem != Command.NEXT && cmdItem != Command.LAST) {
                                                                        cmdItem = Command.FIRST;
                                                                    }
                                                                    ctrLine.setLocationImg(approot + "/images");
                                                                    ctrLine.initDefault();
                                                                    ctrLine.setImageListName(approot + "/images", "item");

                                                                    ctrLine.setListFirstCommand("javascript:itemList('" + Command.FIRST + "')");
                                                                    ctrLine.setListNextCommand("javascript:itemList('" + Command.NEXT + "')");
                                                                    ctrLine.setListPrevCommand("javascript:itemList('" + Command.PREV + "')");
                                                                    ctrLine.setListLastCommand("javascript:itemList('" + Command.LAST + "')");
                                                                %>
                                                                <%=ctrLine.drawImageListLimit(cmdItem, vectSizeItem, startItem, recordToGetItem)%>
                                                            </span>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <td height="22" valign="middle" colspan="3">
                                                            <%
                                                                if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {
                                                            %>
                                                            <table width="50%" border="0" cellspacing="2" cellpadding="0">
                                                                <tr>
                                                                    <td width="6%"><a href="javascript:addItem()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image200', '', '<%=approot%>/images/BtnNewOn.jpg', 1)"><img name="Image200" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true)%>"></a></td>
                                                                    <td width="94%"><a href="javascript:addItem()"><%=ctrLine.getCommand(SESS_LANGUAGE, poCode + " Item", ctrLine.CMD_ADD, true)%></a></td>
                                                                </tr>
                                                            </table>
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                </table>
                                            </td>
                                        </tr>
                                        <%//if(oidPurchaseOrder!=0){%>
                                        <%if(listPurchaseOrderItem!=null && listPurchaseOrderItem.size()>0){%>
                                        <tr>
                                            <td colspan="2" valign="top"> </td>
                                            <td width="30%" valign="top">
                                                <table width="100%" border="0">
                                                    <tr>
                                                        <td align="right" nowrap><%="SUB TOTAL :"%></td>
                                                        <td>&nbsp;</td>
                                                        <td><div align="right">
                                                                <%
                                                                    String whereItem = "" + PstPurchaseOrderItem.fieldNames[PstPurchaseOrderItem.FLD_PURCHASE_ORDER_ID] + "=" + oidPurchaseOrder;
                                                                    double total = PstPurchaseOrderItem.getTotal(whereItem) / po.getExchangeRate();
                                                                    out.println(FRMHandler.userFormatStringDecimal(total));
                                                                    double lastPpn = po.getPpn() / po.getExchangeRate();
                                                                    if (lastPpn == 0) {
                                                                        lastPpn = defaultPpn / po.getExchangeRate();
                                                                    }
                                                                    double ppn = total * lastPpn / 100;
                                                                    ppn = total + ppn;

                                                                    //include or not include
                                                                    double valuePpn = 0.0;
                                                                    if (po.getIncludePpn() == 1) {
                                                                        valuePpn = total - (total / 1.1);
                                                                    } else if (po.getIncludePpn() == 0) {
                                                                        valuePpn = total * lastPpn / 100;
                                                                    }
                                                                %>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="40%" align="right" nowrap>
                                                            <div align="right"><input type="checkbox" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_INCLUDE_PPN]%>" value="1" <% if (po.getIncludePpn() == 1) {%>checked<%}%>><%=textListOrderHeader[SESS_LANGUAGE][16]%>&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][9]%>
                                                                <input type="text"  class="formElemen" name="<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PPN]%>" value="<%if (ppn != 0.0) {%><%=FRMHandler.userFormatStringDecimal(lastPpn)%><%} else {%><%=FRMHandler.userFormatStringDecimal(defaultPpn)%><%}%>"  size="5" style="text-align:right">&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][17]%>:</div></td>
                                                        <td width="4%"> <div align="right"></div></td>
                                                        <td width="56%"><div align="right">
                                                            <!--<input name="<%//=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_PPN]%>" type="text" class="formElemenR" value="<%//=FRMHandler.userFormatStringDecimal(po.getPpn())%>" size="15">-->
                                                                <%=FRMHandler.userFormatStringDecimal(valuePpn)%>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">TOTAL : </td>
                                                        <td>&nbsp;</td>
                                                        <% if (po.getIncludePpn() == 1) {%>
                                                        <td><div align="right"><b><%=FRMHandler.userFormatStringDecimal(total)%></b></div></td>
                                                                    <%} else {%>
                                                        <td width="44%"><div align="right"><b><%=FRMHandler.userFormatStringDecimal(ppn)%></b></div></td>
                                                                    <%}%>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <%}%>
                                        <!--tr>
                                          <td colspan="3">
                                            <table width="100%" border="0">
                                              <tr valign="top">
                                                <td colspan="2"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr align="center">
                                                      <td><p>Order By</p>
                                                        <p>
                                                          <input type="text" name="textfield3">
                                                          <br>
                                                          <strong>Purchasing</strong> </p>
                                                      </td>
                                                      <td><p>Approved By</p>
                                                        <p>Name :
                                                          <input type="text" name="textfield2"><br>
                                                          Department :
                                                          <input type="text" name="textfield2">
                                                        </p></td>
                                                      <td><p>Aknowledge By</p>
                                                        <p>
                                                          <input type="text" name="textfield"><br>
                                                          <strong>Inventorar </strong> </p>
                                                      </td>
                                                    </tr> </table>
                                                </td>
                                              </tr-->
                                        <tr>
                                            <td width="70%">
                                                <%

                                                    ctrLine.setLocationImg (approot

                                                    +"/images");

                                                // set image alternative caption
                                                    ctrLine.setSaveImageAlt (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
                                                    ctrLine.setBackImageAlt (SESS_LANGUAGE

                                                    ==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                                                    ctrLine.setDeleteImageAlt (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
                                                    ctrLine.setEditImageAlt (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

                       
                                                    ctrLine.setTableWidth (
                                                    "100%");
                                                String scomDel = "javascript:cmdAsk('" + oidPurchaseOrder + "')";
                                                    String sconDelCom = "javascript:cmdDelete('" + oidPurchaseOrder + "')";
                                                    String scancel = "javascript:cmdEdit('" + oidPurchaseOrder + "')";
                                                    ////ctrLine.setCommandStyle("command");

                                                    ctrLine.setColCommStyle (

                                                    "command");

                                                // set command caption
                                                    ctrLine.setSaveCaption (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_SAVE,true));
                                                    ctrLine.setBackCaption (SESS_LANGUAGE

                                                    ==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true) : ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK,true)+" List");
                                                    ctrLine.setDeleteCaption (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_ASK,true));
                                                    ctrLine.setConfirmDelCaption (ctrLine.getCommand

                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_DELETE,true));
                                                    ctrLine.setCancelCaption (ctrLine.getCommand
                                                    (SESS_LANGUAGE,poTitle,ctrLine.CMD_CANCEL,false));

                                                    if(privDelete && privManageData

                            
                                                        ){
                                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                                        ctrLine.setDeleteCommand(scomDel);
                                                        ctrLine.setEditCommand(scancel);
                                                    }

                            
                                                        else{
                                                    ctrLine.setConfirmDelCaption("");
                                                        ctrLine.setDeleteCaption("");
                                                        ctrLine.setEditCaption("");
                                                    }

                                                    // if (deleteDocument == false){ 
                                                    //iCommand = Command.NONE; 
                                                    //    }
                                                    if(privAdd
                                                    ==false && privUpdate

                            
                                                        ==false){
                                                    ctrLine.setSaveCaption("");
                                                    }

                                                    if(privAdd

                                                    ==false || po.getPoStatus () 
                                                        !=I_DocStatus.DOCUMENT_STATUS_DRAFT){
                                                    ctrLine.setAddCaption("");
                                                    }

                                                    if (iCommand == Command.NONE

                            
                            
                                                    ) {
                                                    //iCommand=Command.SAVE; 
                                                }

                                                    if(iCommand
                                                    ==Command.SAVE

                                                    && frmpo.errorSize () 
                            

                                                    ==0){
                                                    //iCommand=Command.EDIT;
                                                }

                                                if(po.getPoStatus () 
                                                        ==I_DocStatus.DOCUMENT_STATUS_FINAL ) {
                                                    ctrLine.setDeleteCaption("");
                                                        if ((listPurchaseOrderItem != null) && (listPurchaseOrderItem.size() > 0) && (locationAssign == true) && (privClosed == true)) {
                                                            ctrLine.setSaveCaption(ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_SAVE, true));
                                                        } else {
                                                            ctrLine.setSaveCaption("");
                                                        }

                                                        ctrLine.setAddCaption("");
                                                    }

                                                    if(documentClosed

                            
                                                        ){
                                                    ctrLine.setSaveCaption("");
                                                        ctrLine.setBackCaption(SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) : ctrLine.getCommand(SESS_LANGUAGE, poTitle, ctrLine.CMD_BACK, true) + " List");
                                                        ctrLine.setDeleteCaption("");
                                                        ctrLine.setConfirmDelCaption("");
                                                        ctrLine.setCancelCaption("");
                                                    }

                                                    ctrLine.initDefault ();
                                                %>
                                                <%=ctrLine.drawImage(iCommand, iErrCode, errMsg)%>
                                            </td>
                                            <td width="40%">
                                                <%if(poUseDirectPrinting

                             
                                 ==1){%>
                                                <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                    <tr>
                                                        <td colspan="2" valign="top">
                                                            <%
                                                                Vector valtype = new Vector(1, 1);
                                                                Vector keytype = new Vector(1, 1);
                                                                valtype.add(String.valueOf(0));
                                                                keytype.add("INTERNAL");
                                                                valtype.add(String.valueOf(1));
                                                                keytype.add("EXTERNAL");
                                                                String selecttype = "" + type;
                                                            %> <%=ControlCombo.draw("str_type", null, selecttype, valtype, keytype, "", "formElemen")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="5%">
                                                            <%
                                                                Vector hostLst = null;
                                                                try {
                                                                    hostLst = RemotePrintMan.getHostList();
                                                                    System.out.println(" JSP 1 1");
                                                                    /* if(hostLst!=null){
                                                                   for(int h=0;h<hostLst.size();h++){
                                                                      PrinterHost host = (PrinterHost )hostLst.get(h);
                                                                      System.out.println(" JSP 1 2"+h);
                                                                      if(host!=null){
                                                                              out.println(""+h+") "+host.getHostName()+"<br>");
                                                                      }
                                                                   }
                                                                }*/
                                                                } catch (Exception exc) {
                                                                    System.out.println("HostLst:  " + exc);
                                                                }
                                                            %>
                                                            <select name="printeridx">
                                                                <%
                                                                    Vector prnLst = null;
                                                                    PrinterHost host = null;
                                                                    if (hostLst != null) {
                                                                        for (int h = 0; h < hostLst.size(); h++) {
                                                                            try {
                                                                                host = (PrinterHost) hostLst.get(h);
                                                                                if (host != null) {
                                                                                    prnLst = host.getListOfPrinters(false);//getPrinterListWithStatus(host);
                                                                                }
                                                                                if (prnLst != null) {
                                                                                    for (int i = 0; i < prnLst.size(); i++) {
                                                                                        try {
                                                                                            PrnConfig prnConf = (PrnConfig) prnLst.get(i);
                                                                                            out.print(" <option value='" + host.getHostIP() + ";" + prnConf.getPrnIndex() + "'> ");
                                                                                            out.println(prnConf.getPrnName() + " on " + host.getHostName() + " (" + prnConf.getPrnPort() + ")");
                                                                                            out.print("f</option>");
                                                                                        } catch (Exception exc) {
                                                                                            out.println("ERROR " + exc);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } catch (Exception exc1) {
                                                                                out.println("ERROR" + exc1);
                                                                            }
                                                                        }
                                                                    }
                                                                %>
                                                            </select>
                                                        </td>
                                                        <td width="95%" nowrap>&nbsp; <a href="javascript:printDirectForm()" class="command" >
                                                                <%if (hostLst.size() > 0 && hostLst != null) {%> CETAK PO <%}%></a>
                                                        </td>
                                                    </tr>
                                                    <%}

                              
                                  else{%>
                                                    <tr>
                                                        <td width="5%" valign="top" align="right">

                                                        </td>
                                                        <td width="95%" nowrap align="left">&nbsp;
                                                            <%
                                                                includePrintPrice = 0;
                                                            %>
                                                            <input type="checkbox" name="includePrinPrice" value="1" checked >&nbsp;&nbsp;<%=textListOrderHeader[SESS_LANGUAGE][19]%>
                                                        </td>
                                                    </tr>
                                                    <tr> 
                                                        <td height="100%" valign="top" width="9%" align="left">&nbsp;</td>
                                                    </tr>
                                                    <tr>
                                                        <td width="5%" valign="top" align="right">

                                                        </td>

                                                        <% if (useForRaditya.equals("1")) {
                                      if (po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_CLOSED || po.getPoStatus() == I_DocStatus.DOCUMENT_STATUS_FINAL) { %>
                                                        <td width="95%" nowrap align="left">&nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printOutForm()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO</i></a>
                                                            <!--/td>-->
                                                            &nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printFormHtml()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO Excel Mode</i></a>
                                                            &nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printFormExcel()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO Excel</i></a> 
                                                        </td>
                                                        <%} else {
                                  }
                              } else {%>
                                                        <td width="95%" nowrap align="left">&nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printOutForm()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO</i></a>
                                                            <!--/td>-->
                                                            &nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printFormHtml()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO Excel Mode</i></a>
                                                            &nbsp;
                                                            <!--a href="javascript:printForm()" class="command" >Print With Shipping Information</a> | -->
                                                            <a class="btn-primary btn-lg" href="javascript:printFormExcel()" class="command" >&nbsp;&nbsp;<i class="fa fa-print">Print PO Excel</i></a> 
                                                        </td>
                                                        <%}%>
                                                    </tr>
                                                </table>
                                                <%}%>
                                            </td>
                                        </tr>
                                        <td>
                                        <centre> <a href="javascript:viewHistoryTable()">TABEL HISTORY</a></centre>
                                        <table width="100%" border="0" cellpadding="2" cellspacing="2">
                                            <tr>
                                                <%if((listDistributionPO

                                         !=null && listDistributionPO.size () 
                                             >0)||oidPurchaseOrder!=0){%>
                                                <td>
                                                    <%=drawList(listDistributionPO, oidPurchaseOrder)%>
                                                    <br>
                                                    <a href="javascript:viewDistributionLocation()">Set Distibution Location</a>
                                                </td>
                                                <%}%>
                                            </tr>
                                        </table>
                                        </td>
                                        </tr>
                                        <!--==================================================================================================== -->
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
        <%if(menuUsed == MENU_ICON

          
              ){%>
        <%@include file="../../../styletemplate/footer.jsp" %>
        <%}

            
                else{%>
        <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
        <script language="JavaScript">
            changeVendorFisrt();
              document.frm_purchaseorder.<%=FrmPurchaseOrder.fieldNames[FrmPurchaseOrder.FRM_FIELD_LOCATION_ID]%>.focus();
        </script>
        <script language="JavaScript">
            <%--
             <% 
             // add by fitra 10-5-2014
             if(po.getPoStatus()==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.ADD){%> 
                cmdSave();
             <% } %>      
            --%>
            <%
                // add by fitra 10-5-2014

                if(po.getPoStatus () 
                
                ==I_DocStatus.DOCUMENT_STATUS_DRAFT && iCommand==Command.SAVE){%>
              addItem();
            <% } %>
        </script>
        <!-- #EndEditable --> </td>
</tr>
</table>
</body>
<div id="modalSearchSuplier" class="modal fade" tabindex="-1">
    <div id=""  class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" data-dismiss="modal" class="close"  aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modal-title"><%=textListGlobal[SESS_LANGUAGE][0]%></h4>
            </div>
            <div class="modal-body" id="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <input type="text" class="form-control" id="searchSuplierText"/>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div id="dynamicPlaceSuplier"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" data-dismiss="modal" class="btn btn-danger">Close</button>
            </div>
        </div>
    </div>

</div>
</html>
