<%-- 
    Document   : cashcashier
    Created on : 02 Jul 13, 10:50:09
    Author     : Wiweka
--%>

<%@ page language="java" %>
<%@ page import = "java.util.*,
         com.dimata.posbo.printing.purchasing.InternalExternalPrinting,
         com.dimata.printman.RemotePrintMan,
         com.dimata.printman.DSJ_PrintObj,
         com.dimata.printman.PrnConfig,
         com.dimata.printman.PrinterHost,
         com.dimata.common.entity.contact.PstContactList,
         com.dimata.common.entity.contact.PstContactClass,
         com.dimata.common.entity.contact.ContactList,
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
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CASHIER); %>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- JSP Block -->
<%!   /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Nota Type", "Number", "Quest Type", "Payment", "Bookeeping Currency", "Sales Currency", "Date", "Delivery Address", "Tel/Hp/Fax", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country"},
        {"Nota Type", "Number", "Quest Type", "Payment", "Bookeeping Curreny", "Sales Currency", "Date", "Delivery Address", "Tel/Hp/Fax", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",//8
            "Disc.", "Disc.Total", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount"},//8
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",
            "Disc.", "Disc.Total", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount"}
    };

    public String drawListBillDetailAdd(int language, int iCommandDetail, FrmBillDetail frmObject,
            Billdetail objEntity, Vector objectClass, long billdetailId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "3%");
        ctrlist.addHeader(textListOrderItem[language][1], "13%");
        ctrlist.addHeader(textListOrderItem[language][2], "20%");
        ctrlist.addHeader(textListOrderItem[language][3], "3%");
        ctrlist.addHeader(textListOrderItem[language][4], "8%");
        //ctrlist.addHeader(textListOrderItem[language][5], "3%");
        ctrlist.addHeader(textListOrderItem[language][6], "4%");
        ctrlist.addHeader(textListOrderItem[language][7], "4%");
        ctrlist.addHeader(textListOrderItem[language][8], "8%");
        ctrlist.addHeader(textListOrderItem[language][9], "8%");
        ctrlist.addHeader(textListOrderItem[language][10], "8%");
        ctrlist.addHeader(textListOrderItem[language][11], "8%");

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        //iCommandDetail = Command.ADD;
        rowx = new Vector();
        if (iCommandDetail == Command.ADD || (iCommandDetail == Command.SAVE && frmObject.errorSize() > 0)) {
            rowx.add(">");
            // code
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\""
                    + "\"><input tabindex=\"1\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" class=\"formElemen\" onFocus=\"tabber1.show(2); return false;\"><a tabindex=\"2\" href=\"javascript:cmdCheck()\">CHK</a>");
            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            // qty
            rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"3\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate()\"  style=\"text-align:right\"></div>");
            // harga
            rowx.add("<input type=\"text\" input tabindex=\"4\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"\" class=\"hiddenTextR\" onKeyDown=\"javascript:keyDownCheck(event)\" readOnly>");
            // diskon 1
            rowx.add("<input type=\"text\" input tabindex=\"5\" size=\"4\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\"  value=\"\" >");
            // diskon 2
            rowx.add("<input tabindex=\"6\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"\" class=\"formElemenR\">");
            // nominal diskon
            rowx.add("<input tabindex=\"7\" input type=\"text\" size=\"8\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"\" >");
            // disc total
            rowx.add("<input tabindex=\"8\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCON_GLOBAL] + "\" value=\"\" class=\"formElemenR\">");
            // net price
            rowx.add("<input tabindex=\"9\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"\" class=\"formElemenR\">");
            // nota
            rowx.add("<input tabindex=\"10\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"\" class=\"formElemenR\">");

            lstData.add(rowx);
        }

        return ctrlist.draw();
    }


    public String drawListBillDetail(int language, int iCommandDetail, FrmBillDetail frmObject,
            Billdetail objEntity, Vector objectClass, long billdetailId, int start) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("", "3%");
        ctrlist.addHeader("", "13%");
        ctrlist.addHeader("", "20%");
        ctrlist.addHeader("", "3%");
        ctrlist.addHeader("", "8%");
        ctrlist.addHeader("", "4%");
        ctrlist.addHeader("", "4%");
        ctrlist.addHeader("", "8%");
        ctrlist.addHeader("", "8%");
        ctrlist.addHeader("", "8%");
        ctrlist.addHeader("", "8%");

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
            Billdetail billdetail = (Billdetail) temp.get(0);

            rowx = new Vector();
            start = start + 1;

            if (billdetailId == billdetail.getOID()) {
                index = i;
            }
            if (index == i && (iCommandDetail == Command.EDIT || iCommandDetail == Command.ASK)) {
                rowx.add("" + start);
                // code
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + billdetail.getMaterialId()
                        + "\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" class=\"formElemen\"><a href=\"javascript:cmdCheck()\">CHK</a>"); //
                // name
                rowx.add("<input type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + billdetail.getItemName() + "\"  readOnly>");
                // qty
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"4\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getQty()) + "\" class=\"formElemen\" style=\"text-align:right\"></div>");
                // price
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getItemPrice()) + "\"  readOnly></div>");
                // discon 1
                rowx.add("<input type=\"text\" size=\"4\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getDisc1()) + "\" readOnly>");
                // discount 2
                rowx.add("<input type=\"text\" size=\"4\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getDisc2()) + "\" class=\"formElemenR\">");
                // disc nominal
                rowx.add("<input type=\"text\" size=\"10\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getDisc()) + "\" class=\"formElemenR\">");
                // disc total
                rowx.add("<input type=\"text\" size=\"10\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCON_GLOBAL] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getDiscGlobal()) + "\" class=\"formElemenR\">");
                // net total
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"" + FRMHandler.userFormatStringDecimal(billdetail.getTotalPrice()) + "\"  readOnly></div>");
                // note
                rowx.add("<div align=\"right\"><input type=\"text\" size=\"15\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"" + billdetail.getNote() + "\"  readOnly></div>");

            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:billDetailEdit('" + String.valueOf(billdetail.getOID()) + "')\">" + billdetail.getSku() + "</a>");
                rowx.add(billdetail.getItemName());

                if(mat.getRequiredSerialNumber()==PstMaterial.REQUIRED){
                String where = PstReceiveStockCode.fieldNames[PstReceiveStockCode.FLD_RECEIVE_MATERIAL_ITEM_ID]+"="+recItem.getOID();
                int cnt = PstReceiveStockCode.getCount(where);

                double recQtyPerBuyUnit = recItem.getQty();
                double qtyPerSellingUnit = PstUnit.getQtyPerBaseUnit(mat.getBuyUnitId(), mat.getDefaultStockUnitId());
                double recQty = recQtyPerBuyUnit * qtyPerSellingUnit;
                double max = recQty;
                if(cnt<max){
                    if(listError.size()==0){
                        listError.add("Silahkan cek :");
                    }
                        listError.add(""+listError.size()+". Jumlah serial kode stok "+mat.getName()+" tidak sama dengan qty terima");
                }
                    rowx.add("<div align=\"right\"><a href=\"javascript:gostock('"+String.valueOf(recItem.getOID())+"')\">[ST.CD]</a> "+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                }else{
                   rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(recItem.getQty())+"</div>");
                }
                rowx.add("<div align=\"right\">" + billdetail.getQty() + "</div>");
                // harga
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getItemPrice()) + "</div>");
                // discon 1
                rowx.add("<div align=\"right\">" + billdetail.getDisc1() + "</div>");
                // discon 2
                rowx.add("<div align=\"right\">" +billdetail.getDisc2() + "</div>");
                // disc nominal
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getDisc()) + "</div>");
                // disc total
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getDiscGlobal()) + "</div>");
                // net total price
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getTotalPrice()) + "</div>");
                // note
                rowx.add("<div align=\"right\">" + billdetail.getNote() + "</div>");
            }
            lstData.add(rowx);
        }

        //iCommandDetail = Command.ADD;

      /* rowx = new Vector();
        if (iCommandDetail == Command.ADD || (iCommandDetail == Command.SAVE && frmObject.errorSize() > 0)) {
            rowx.add("" + (start + 1));
            // code
            rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\""
                    + "\"><input tabindex=\"1\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" class=\"formElemen\" onFocus=\"tabber1.show(2); return false;\"><a tabindex=\"2\" href=\"javascript:cmdCheck()\">CHK</a>");
            // name
            rowx.add("<input type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyDown=\"javascript:keyDownCheck(event)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            // qty
            rowx.add("<div align=\"right\"><input tabindex=\"3\" type=\"text\" size=\"4\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate()\"  style=\"text-align:right\"></div>");
            // harga
            rowx.add("<input type=\"text\" input tabindex=\"4\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"\" class=\"hiddenTextR\" onKeyDown=\"javascript:keyDownCheck(event)\" readOnly>");
            // diskon 1
            rowx.add("<input type=\"text\" input tabindex=\"5\" size=\"4\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\"  value=\"\" >");
            // diskon 2
            rowx.add("<input tabindex=\"6\" type=\"text\" size=\"4\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"\" class=\"formElemenR\">");
            // nominal diskon
            rowx.add("<input tabindex=\"7\" input type=\"text\" size=\"10\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"\" >");
            // disc total
            rowx.add("<input tabindex=\"8\" type=\"text\" size=\"10\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISCON_GLOBAL] + "\" value=\"\" class=\"formElemenR\">");
            // net price
            rowx.add("<input tabindex=\"9\" type=\"text\" size=\"10\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"\" class=\"formElemenR\">");
            // nota
            rowx.add("<input tabindex=\"10\" type=\"text\" size=\"10\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"\" class=\"formElemenR\">");

            lstData.add(rowx);
        }*/

        return ctrlist.draw();
    }

%>

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int iCommandDetail = FRMQueryString.requestInt(request, "commandDetail");
            int start = FRMQueryString.requestInt(request, "start");
            int startDetail = FRMQueryString.requestInt(request, "start_detail");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");
            int idSaleType = FRMQueryString.requestInt(request, "sale_type");
            long idCurrency = FRMQueryString.requestLong(request, "CurrId");
            long idCustomer = FRMQueryString.requestLong(request, "CustId");
            long idPriceType = FRMQueryString.requestLong(request, "PriceTypeId");
            long idStandartRate = FRMQueryString.requestLong(request, "StandartRateId");
            double amounttotal = FRMQueryString.requestDouble(request, "total_amount");
            int codeCustomer = 0;
            String itemName = textListOrderItem[SESS_LANGUAGE][2];

            long oidBillDetail = FRMQueryString.requestLong(request, "hidden_bill_detail_id");
            /*variable declaration*/
            boolean privManageData = true;
            int recordToGet = 10;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = "";
            String orderClause = "";

            CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
            ControlLine ctrLine = new ControlLine();
            Vector listBillMain = new Vector(1, 1);

            FrmBillMain frmBillMain = ctrlBillMain.getForm();

            int vectSize = PstBillMain.getCount(whereClause);
            BillMain billMain = ctrlBillMain.getBillMain();
            msgString = ctrlBillMain.getMessage();
            long oidbillmain = FRMQueryString.requestLong(request, "oidbillmaintmp");
            if (iCommand == Command.SAVE) {
                iErrCode = ctrlBillMain.action(iCommand, oidBillMain);
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                idCustomer = billMain.getCustomerId();
                iCommand = 0;
            }

            //Mencari Price Type Id
           Vector listPriceType = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + idCustomer + "'", "");
            for (int i = 0; i < listPriceType.size(); i++) {
                MemberGroup memberGroup = (MemberGroup) listPriceType.get(i);
                idPriceType = memberGroup.getPriceTypeId();
            }

           //Mencari Standard Rate Id
           Vector listStandardRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='" + idCurrency + "'", "");
            for (int i = 0; i < listStandardRate.size(); i++) {
                StandartRate standartRate = (StandartRate) listStandardRate.get(i);
                idStandartRate = standartRate.getOID();
            }

            //Untuk Bill Detail
            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            ctrlBillDetail.setLanguage(SESS_LANGUAGE);

             if (iCommandDetail == Command.SAVE) {
                iErrCode = ctrlBillDetail.action(iCommandDetail, oidBillDetail, oidbillmain);

            }

            amounttotal = PstBillDetail.getTotalAmount(oidbillmain);

            FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();
            Billdetail billdetail = ctrlBillDetail.getBillDetail();
            msgString = ctrlBillDetail.getMessage();

            //Mencari Shift Id, cascashier id, location id
            long shiftId = 0;
            long cashcashierId = 0;
            long cashmasterId = 0;
            long locationId = 0;
            Vector listCashCashier = PstCashCashier.list(0, 0, PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID] + " ='" + userId + "' AND " + PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID] + " = '1'", "");
            for (int i = 0; i < listCashCashier.size(); i++) {
                CashCashier cashCashier = (CashCashier) listCashCashier.get(i);
                cashcashierId = cashCashier.getOID();
                shiftId = cashCashier.getShiftId();
                cashmasterId = cashCashier.getCashMasterId();
            }

            Vector listLocation = PstCashMaster.list(0, 0, PstCashMaster.fieldNames[PstCashMaster.FLD_CASH_MASTER_ID] + " ='" + cashmasterId + "'", "");
            for (int i = 0; i < listLocation.size(); i++) {
                CashMaster cashMaster = (CashMaster) listLocation.get(i);
                locationId = cashMaster.getLocationId();
            }

            /*switch list BillMain*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/
            /* get record to display */
            listBillMain = PstBillMain.list(start, recordToGet, whereClause, orderClause);
            /*handle condition if size of record to display = 0 and start > 0 	after delete*/
            if (listBillMain.size() < 1 && start > 0) {
                if (vectSize - recordToGet > recordToGet) {
                    start = start - recordToGet;   //go to Command.PREV
                } else {
                    start = 0;
                    iCommand = Command.FIRST;
                    prevCommand = Command.FIRST; //go to Command.FIRST
                }
                listBillMain = PstBillMain.list(start, recordToGet, whereClause, orderClause);
            }

            /**Untuk Bill Detail*/
            String whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;
            String orderClauseDetail = "";
            int vectSizeDetail = PstBillDetail.getCount(whereClauseDetail);
            int recordToGetDetail = 10;

            if (iCommandDetail == Command.FIRST || iCommandDetail == Command.PREV || iCommandDetail == Command.NEXT || iCommandDetail == Command.LAST) {
                startDetail = ctrlBillDetail.actionList(iCommandDetail, startDetail, vectSizeDetail, recordToGetDetail);
            }

            whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;
            Vector listBillDetail = PstBillDetail.list(startDetail, recordToGetDetail, whereClauseDetail, orderClauseDetail);
            if (listBillDetail.size() < 1 && startDetail > 0) {
                if (vectSizeDetail - recordToGetDetail > recordToGetDetail) {
                    startDetail = startDetail - recordToGetDetail;
                } else {
                    startDetail = 0;
                    iCommandDetail = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listBillDetail = PstBillDetail.list(startDetail, recordToGetDetail, whereClauseDetail, orderClauseDetail);
            }

            /** kondisi ini untuk manampilakn form tambah item. posisi pada baris program paling bawah */
            if (iCommandDetail == Command.SAVE && iErrCode == 0) {
                iCommandDetail = Command.ADD;
            }
%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <style type="text/css">
            <!--
            @import 'cashier/custom.css';
            -->
        </style>

        <script type="text/javascript" src="cashier/yetii.js"></script>
        <script src="cashier/jquery-1.7.1.min.js" type="text/javascript"></script>
        <!-- #BeginEditable "doctitle" -->
        <title>Dimata ProChain - </title>
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

            function cmdKeyIssue(){
                var con = confirm("Please, Card on Keylock. Are You Sure To Continues ? ");
                var commandValue=commandKey;
                if (con ==true)
                {

                }
            }

            var newwindow = null;
            function gothere(oSelect) {
                if (newwindow && !newwindow.closed) newwindow.close();
                if(oSelect.selectedIndex > 0){
                    var url = oSelect[oSelect.selectedIndex].value;
                    newwindow=window.open(url,"newwindow","width=1200,height=1100");
                }
                if (newwindow && !newwindow.closed) newwindow.focus();
            }

            function calculate(){
                var qty = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
                var cost = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
                var lastDisc = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.value,guiDigitGroup,guiDecimalSymbol);
                var lastDisc2 = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>.value,guiDigitGroup,guiDecimalSymbol);
                var lastDiscNom = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.value,guiDigitGroup,guiDecimalSymbol);
                var sum = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                var totalAmountTmp = "";

                if(qty<0.0000){
                    document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value=0;
                    return;
                }

                if(isNaN(cost) || (cost==""))
                    cost = 0.0;
                if(isNaN(lastDisc) || (lastDisc==""))
                    lastDisc = 0.0;
                if(isNaN(lastDisc2) || (lastDisc2==""))
                    lastDisc2 = 0.0;
                if(isNaN(lastDiscNom) || (lastDiscNom==""))
                    lastDiscNom = 0.0;

                var totaldiscount = cost * lastDisc / 100;
                var totalMinus = cost - totaldiscount;
                var totaldiscount2 = totalMinus * lastDisc2 / 100;
                var totalDisc = totaldiscount2 + lastDiscNom;

                var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                var lastTotal = qty * totalCost;

                //untuk menghutung total penjualan
                if(sum == 0.0){
                    totalAmountTmp = lastTotal;
                }else{
                    totalAmountTmp = parseFloat(sum) + parseFloat(lastTotal);
                }
                //document.frmcashier.<!--%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISCON_GLOBAL]%>.value = totalMinus;//formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_PRICE]%>.value = lastTotal;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_AMOUNT]%>.value = parseFloat(totalAmountTmp);
            }

            function cmdAdd()
            {
                document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdAsk(oidBillDetail)
            {
                document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                document.frmcashier.commandDetail.value="<%=Command.ASK%>";
                document.frmcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdConfirmDelete(oidBillDetail)
            {
                document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                document.frmcashier.commandDetail.value="<%=Command.DELETE%>";
                document.frmcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdSave()
            {
                document.frmcashier.commandDetail.value="<%=Command.SAVE%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function billDetailEdit(oidBillDetail)
            {
                document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                document.frmcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdCancel(oidBillDetail)
            {
                document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                document.frmcashier.prev_command.value="<%=prevCommand%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdBack()
            {
                document.frmcashier.commandDetail.value="<%=Command.BACK%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdListFirst()
            {
                document.frmcashier.commandDetail.value="<%=Command.FIRST%>";
                document.frmcashier.prev_command.value="<%=Command.FIRST%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdListPrev()
            {
                document.frmcashier.commandDetail.value="<%=Command.PREV%>";
                document.frmcashier.prev_command.value="<%=Command.PREV%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdListNext()
            {
                document.frmcashier.commandDetail.value="<%=Command.NEXT%>";
                document.frmcashier.prev_command.value="<%=Command.NEXT%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdListLast()
            {
                document.frmcashier.commandDetail.value="<%=Command.LAST%>";
                document.frmcashier.prev_command.value="<%=Command.LAST%>";
                document.frmcashier.action="cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }


            function cmdChangeMember(){
                document.frmcashier.submit();
            }

            function cmdApproval(){
                document.frmcashier.command.value="<%=Command.SAVE%>";
                document.frmcashier.action = "cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdApproval2(){
                document.frmcashier.commandDetail.value="<%=Command.SAVE%>";
                document.frmcashier.action = "cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdAdd2(){
                document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                document.frmcashier.action = "cashier_lyt_1.jsp";
                document.frmcashier.submit();
            }

            function cmdCheck(){
                var strvalue  = "materialdosearchcashier.jsp?command=<%=Command.FIRST%>"+
                    "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                    "&price_type_id="+document.frmcashier.PriceTypeId.value;
                window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }

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

        <script type="text/javascript">

            function disablefield(){
                if (document.getElementById('quest_type').value==0) {
                    document.getElementById('payment').disabled='disabled';
                }else{
                    document.getElementById('payment').disabled='';
                }
            }

        </script>
        <!-- Js untuk PopUp -->
        <script type="text/javascript">

            // $(document).ready(function(){
            $(document).ready(function(){
                //open popup
                $("#pop").click(function(){
                    $("#overlay_form").fadeIn(1000);
                    positionPopup();
                });

                //close popup
                $("#close").click(function(){
                    $("#overlay_form").fadeOut(500);
                });
            });

            //position the popup at the center of the page
            function positionPopup(){
                if(!$("#overlay_form").is(':visible')){
                    return;
                }
                $("#overlay_form").css({
                    left: ($(window).width() - $('#overlay_form').width()) / 2,
                    top: ($(window).width() - $('#overlay_form').width()) / 7,
                    position:'absolute'
                });
            }

            //maintain the popup at center of the page when browser resized
            $(window).bind('resize',positionPopup);

        </script>

        <style>
            #overlay_form{
                position: absolute;
                border: 5px solid gray;
                padding: 10px;
                background: white;
                width: auto;
                height: auto;
            }
            #pop{

                border: 1px solid gray;
                width:auto;
                text-align: center;
                padding: 6px;
                border-radius: 5px;
                text-decoration: none;
                margin:auto;
            }
        </style>

        <style type="text/css">
            <!--
            .style1 {
                color: #009900;
                font-weight: bold;
            }
            .style3 {font-size: 24px}
            -->
        </style>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
                    <form name="frmcashier" method ="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="commandDetail" value="<%=iCommandDetail%>">
                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="start_detail" value="<%=startDetail%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                        <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">
                        <input type="hidden" name="oidbillmaintmp" value="<%=oidbillmain%>">
                        <input type="hidden" name="CurrId" value="<%=idCurrency%>">
                        <input type="hidden" name="CustId" value="<%=idCustomer%>">
                        <input type="hidden" name="PriceTypeId" value="<%=idPriceType%>">
                        <input type="hidden" name="StandartRateId" value="<%=idStandartRate%>">
                        <input type="hidden" name="total_amount" value="<%=amounttotal%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_INVOICE_NUMBER]%>" value="<%=billMain.getInvoiceNumber()%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_APP_USER_ID]%>" value="<%=userId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashcashierId%>">

                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Cashier by <%=userName%> <!-- #EndEditable --><div align="right"><a href="close_shift.jsp">Close Shift</a></div></td>
                            </tr>
                            <tr>
                                <td><!-- #BeginEditable "content" -->
                                    <table width="100%" cellspacing="1" cellpadding="1">
                                        <tr>
                                            <td>
                                                <div id="demo" class="demolayout">

                                                    <ul id="demo-nav" class="demolayout">
                                                        <li class="democlass"><a href="#tab2">Main</a></li>
                                                        <li class="hidden"><a href="#tab3" >asd</a></li>
                                                    </ul>
                                                    <div class="tabs-container">
                                                        <div class="tab" id="tab2">
                                                            <table align="center" width="98%" cellspacing="1" cellpadding="1">
                                                                <tr>
                                                                    <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                            <tr>
                                                                                <td width="37%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                                                <td width="63%"><label>
                                                                                        <select name="select" tabindex="1" onChange="gothere(this)">
                                                                                            <option selected>Sales Order</option>
                                                                                            <option value="customerdosearch.jsp">Sales Return</option>
                                                                                            <option value="customerdosearch_1.jsp">Gift ( Hadiah )</option>
                                                                                        </select>
                                                                                    </label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                                <td><%=(billMain.getInvoiceNumber() == "" ? "<b>- Otomatis -</b>" : "<b> - Otomatis -</b>")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td height="32"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                                                                                <td>
                                                                                    <%

                                                                                                long selectedCustomerId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
                                                                                                Vector listCustomer = PstMemberGroup.list(0, 0, "", "");
                                                                                                Vector vectCustomerVal = new Vector(1, 1);
                                                                                                Vector vectCustomerKey = new Vector(1, 1);
                                                                                                for (int i = 0; i <= listCustomer.size(); i++) {
                                                                                                    if (i == 0) {
                                                                                                        vectCustomerKey.add("-none-");
                                                                                                        vectCustomerVal.add("");
                                                                                                    } else {
                                                                                                        MemberGroup memberGroup = (MemberGroup) listCustomer.get(i - 1);
                                                                                                        vectCustomerKey.add(memberGroup.getName());
                                                                                                        vectCustomerVal.add("" + memberGroup.getOID());
                                                                                                    }
                                                                                                }
                                                                                                String select_Customer = "0";

                                                                                                if (selectedCustomerId != 0) {
                                                                                                    select_Customer = "" + selectedCustomerId;
                                                                                                    Vector listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + selectedCustomerId + "'", "");
                                                                                                    MemberGroup memberGroupCode = (MemberGroup) listCodeCustomer.get(0);
                                                                                                    codeCustomer = memberGroupCode.getGroupType();
                                                                                                }




                                                                                    %>
                                                                                    <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID], "formElemen", null, select_Customer, vectCustomerVal, vectCustomerKey, "onChange=\"javascript:cmdChangeMember()\"")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                                                <td><%Vector val_saletype = new Vector(1, 1);
                                                                                            Vector key_saletype = new Vector(1, 1);

                                                                                            val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
                                                                                            key_saletype.add("Cash");

                                                                                            val_saletype.add("" + PstBillMain.TRANS_TYPE_CREDIT);
                                                                                            key_saletype.add("Kredit");

                                                                                            String select_saletype = "" + idSaleType;
                                                                                    %>
                                                                                    <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE], null, select_saletype, val_saletype, key_saletype, "", "formElemen")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][15]%></td>

                                                                                <td><%=userName%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                                                <td>Rp. at 9.600/USD</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                                <td><label><%

                                                                                            long currencyId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID]);
                                                                                            Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                                                            Vector vectCurrVal = new Vector(1, 1);
                                                                                            Vector vectCurrKey = new Vector(1, 1);
                                                                                            for (int i = 0; i <= listCurr.size(); i++) {
                                                                                                if (i == 0) {
                                                                                                    vectCurrKey.add("-none-");
                                                                                                    vectCurrVal.add("");
                                                                                                } else {
                                                                                                    CurrencyType currencyType = (CurrencyType) listCurr.get(i - 1);
                                                                                                    vectCurrKey.add(currencyType.getCode());
                                                                                                    vectCurrVal.add("" + currencyType.getOID());
                                                                                                }
                                                                                            }
                                                                                            String select_cur = "";
                                                                                            select_cur = "" + currencyId;



                                                                                        %>
                                                                                        <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID], "formElemen", null, select_cur, vectCurrVal, vectCurrKey, "")%>

                                                                                        at 9650/USD </label></td>
                                                                            </tr>
                                                                        </table></td>
                                                                    <td></td>
                                                                    <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                            <tr>
                                                                                <td width="20%"><%=textMaterialHeader[SESS_LANGUAGE][6]%></td>
                                                                                <td width="80%"><%=ControlDate.drawDateWithStyle(FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_DATE], (billMain.getBillDate() == null) ? new Date() : billMain.getBillDate(), 0, -1, "formElemen", "")%></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][7]%></td>
                                                                                <td><label>
                                                                                        <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>" cols="32" rows="2" tabindex="5" value="<%=billMain.getShippingAddress()%>"></textarea>
                                                                                    </label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                                <td>
                                                                                    <table cellspacing="0" cellpadding="0">
                                                                                        <tr>
                                                                                            <td><%=textMaterialHeader[SESS_LANGUAGE][16]%></td>
                                                                                            <td><%=textMaterialHeader[SESS_LANGUAGE][17]%></td>
                                                                                            <td><%=textMaterialHeader[SESS_LANGUAGE][18]%></td>
                                                                                        </tr>
                                                                                        <tr>
                                                                                            <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>" type="text" size="12" tabindex="6" value="<%=billMain.getShippingCity()%>">,</td>
                                                                                            <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>" type="text" size="12" tabindex="7" value="<%=billMain.getShippingProvince()%>">,</td>
                                                                                            <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>" type="text" size="12" tabindex="8" value="<%=billMain.getShippingCountry()%>"></td>
                                                                                        </tr>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                                <td><label>
                                                                                        <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>" type="text" size="15" tabindex="9" value="<%=billMain.getShippingPhoneNumber()%>">
                                                                                        /<br>
                                                                                        <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_MOBILE_NUMBER]%>" type="text" size="15" tabindex="10" value="<%=billMain.getShippingMobilePhone()%>">
                                                                                        /                    </label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                                <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>" type="text" size="10" maxlength="10" tabindex="11" value="<%=billMain.getShippingZipCode()%>"></td>
                                                                            </tr>
                                                                        </table></td>
                                                                    <td></td>
                                                                    <td colspan="2" rowspan="5" valign="top">
                                                                        <%

                                                                                    if (codeCustomer == 3) {
                                                                        %>
                                                                        <table width="107%" border="0" cellspacing="2" cellpadding="2">
                                                                            <tr>
                                                                                <td width="26%"><%=textMaterialHeader[SESS_LANGUAGE][10]%></td>
                                                                                <td width="74%"><label>
                                                                                        <input name="compName" type="text" value="" size="15" maxlength="64" tabindex="13">
                                                                                        <a href="#" id="pop">CHK</a>
                                                                                    </label></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                                <td><input name="compAddr" type="text" value="" size="15" maxlength="64" tabindex="13" class="hiddenLabel" ></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                                <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" type="text" value="<%=billMain.getGuestName()%>" size="15" maxlength="64" tabindex="15"></td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][12]%> </td>
                                                                                <td>Rp. - </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][13]%></td>
                                                                                <td>Rp. - </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td><%=textMaterialHeader[SESS_LANGUAGE][14]%></td>
                                                                                <td>Rp. - </td>
                                                                            </tr>
                                                                        </table>
                                                                        <%}%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td></td>
                                                                </tr>
                                                                <tr>
                                                                    <td bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                    <td nowrap bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                    <td></td>
                                                                    <td bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                    <td bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                    <td></td>
                                                                    <td bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                    <td nowrap bgcolor="#FFFF66"><span class="style3 style3"></span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td></td>
                                                                    <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                            <tr>
                                                                                <td ></td>
                                                                                <td ><a href="javascript:cmdApproval()" onKeyDown="javascript:keybrdPress(this)"><span >SUBMIT</span></a></td>

                                                                            </tr>
                                                                        </table></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <div class="tab" id="tab3">

                                                        </div>
                                                    </div>
                                                </div>
                                                <script type="text/javascript">
                                                    function customfunction(tabnumber) {
                                                    }

                                                    var tabber1 = new Yetii({
                                                        id: 'demo',
                                                        callback: customfunction,
                                                        persist: true

                                                    });


                                                </script>
                                            </td>
                                        </tr>
                                        <tr>
                                            <%try {
                                            %>
                                            <td > <%= drawListBillDetailAdd(SESS_LANGUAGE, iCommandDetail, frmBillDetail, billdetail, listBillDetail, oidBillDetail, startDetail)%> </td>
                                            <%
                                                        } catch (Exception e) {
                                                            System.out.println(e);
                                                        }
                                            %>
                                        </tr>
                                        <tr>
                                            <%try {
                                            %>
                                            <td > <%= drawListBillDetail(SESS_LANGUAGE, iCommandDetail, frmBillDetail, billdetail, listBillDetail, oidBillDetail, startDetail)%> </td>
                                            <%
                                                        } catch (Exception e) {
                                                            System.out.println(e);
                                                        }
                                            %>
                                        </tr>
                                        <tr>
                                            <td >
                                                <% if(iCommand == Command.ADD){ %>
                                                <table>
                                                    <tr>
                                                        <td bgcolor="#ffaa17s"></td>
                                                    </tr>
                                                </table>
                                                <%}%>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <table width="50%" align="right">
                                                    <tr>
                                                        <td>
                                                            Total
                                                        </td>
                                                        <td align="right">
                                                            <input name="<%=frmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_AMOUNT]%>" align="right" type="text" value="<%=amounttotal%>" class="style3 style3" class="hiddenLabel" readonly>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="8" align="left" colspan="3" class="command">
                                                <span class="command">
                                                </span> </td>
                                        </tr>
                                        <tr align="left" valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <!--%if (iCommandDetail != Command.ADD && iCommandDetail != Command.EDIT && iCommandDetail != Command.ASK && iErrCode == FRMMessage.NONE) {%-->
                                                <table width="17%" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <!--%if (privAdd) {%-->
                                                        <td ></td>
                                                        <td ><a href="javascript:cmdApproval2()" onKeyDown="javascript:keybrdPress(this)"><span >SAVE</span></a></td>
                                                        <!--%}%-->                                                                      <td ><a href="javascript:cmdAdd2()" onKeyDown="javascript:keybrdPress(this)"><span >ADD</span></a></td>
                                                    </tr>
                                                </table>
                                                <!--%}%-->
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><!--label>
                                                    <input type="submit" name="Submit3" value="Save">
                                                    <input type="submit" name="Submit32" value="Delete">
                                                </label--></td>
                                        </tr>
                                    </table>

                                    <form name="form1" method="post" action="">
                                    </form>
                                    <!-- #EndEditable --></td>
                            </tr>
                        </table>
                    </form>

                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        <form id="overlay_form" style="display:none">
            <%@ include file = "customerdosearch.jsp" %>
            <a href="#" id="close" >Close</a>
        </form>
    </body>
    <!-- #EndTemplate --></html>

