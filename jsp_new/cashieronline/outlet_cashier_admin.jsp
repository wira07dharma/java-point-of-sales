<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.posbo.ajax.checkStockOutletOrder"%>
<%@ page language = "java"  %>
<%@ page import = " java.util.* ,
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
         com.dimata.pos.entity.payment.CashPayments1,
         com.dimata.pos.form.payment.*,
         com.dimata.pos.entity.payment.*,
         com.dimata.pos.form.masterCashier.*,
         com.dimata.pos.entity.masterCashier.*,
         com.dimata.common.entity.payment.*,
         com.dimata.posbo.entity.admin.*"%>
<%@ include file = "../main/javainit.jsp" %>
<%!    //final static int CMD_NONE =0;
    final static int CMD_APPROVAL = 1;

%>
<%
        int appObjCodeSalesOrder = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_ORDER);
        int appObjCodeSalesRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_RETUR);
        int appObjCodeSalesInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_SALES_ORDER, AppObjInfo.OBJ_SALES_INVOICE);        

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalSalesOrder = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesOrder, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesRetur = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesRetur, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalSalesInvoice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeSalesInvoice, AppObjInfo.COMMAND_VIEW));
%>
<!-- JSP Block -->
<%!   /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Currency", "Sales Currency", "Date", "Address", "Tel/Hp", "Zip", "Customer", //11
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax","Notes","Location Warehouse"}, //11
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Curreny", "Sales Currency", "Date", "Address", "Tel/Hp", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax","Notes","Location Warehouse"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",//8
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"},//8
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"}
    };

    public String drawListBillDetailAdd(int language, int iCommandDetail, FrmBillDetail frmObject,
            Billdetail objEntity, Vector objectClass, long billdetailId, int start, int idNotaType, long oidBillMain) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "3%");
        ctrlist.addHeader(textListOrderItem[language][1], "13%");
        ctrlist.addHeader(textListOrderItem[language][2], "26%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");
        ctrlist.addHeader(textListOrderItem[language][17], "5%");
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

        rowx = new Vector();
        if (iCommandDetail == Command.ADD || (iCommandDetail == Command.SAVE && frmObject.errorSize() > 0)) {
            rowx.add(">");
            if (idNotaType == PstBillMain.RETUR || idNotaType == PstBillMain.RETUR_ALL) {
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"\">"
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"\">"
                        + "<input tabindex=\"18\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event, this.value)\" class=\"formElemen\"><a tabindex=\"19\" href=\"javascript:cmdCheckRetur()\">CHK</a>");
                rowx.add("<input type=\"text\" tabindex=\"20\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyPress=\"keybrdPress(this, event, this.value)\"><a href=\"javascript:cmdCheckRetur()\">CHK</a>");
            } else {
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"\">"
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"\">"
                        + "<input tabindex=\"18\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemen\"><a tabindex=\"19\" href=\"javascript:cmdCheck()\">CHK</a>");
                rowx.add("<input type=\"text\" size=\"20\" tabindex=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyPress=\"keybrdPress(this, event, this.value)\"><a href=\"javascript:cmdCheck()\">CHK</a>");
            }
            rowx.add("<div align=\"right\"><input tabindex=\"21\" type=\"text\" size=\"3\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate()\" onKeyPress=\"keybrdPress(this, event, this.value)\"  style=\"text-align:right\"></div>");
            rowx.add("<input tabindex=\"\" type=\"text\" size=\"3\"  name=\"matUnit\" value=\"\" class=\"hiddenTextR\"  class=\"formElemenR\">");
            rowx.add("<input type=\"text\" tabindex=\"22\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"\" class=\"hiddenTextR\" onKeyPress=\"keybrdPress(this, event, this.value)\" readOnly>");
            rowx.add("<input type=\"text\" tabindex=\"23\" size=\"3\" id=\"disc1\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\"  value=\"\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"24\" type=\"text\" size=\"3\" id=\"disc2\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"25\" type=\"text\" size=\"8\" id=\"disc\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"26\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_DISC] + "\" value=\"\" class=\"hiddenTextR\"  class=\"formElemenR\">");
            rowx.add("<input tabindex=\"27\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"\" class=\"hiddenTextR\"  onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"28\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\">");

            lstData.add(rowx);
        }

        return ctrlist.draw();
    }

    public String drawListBillDetail(int language, int iCommandDetail, FrmBillDetail frmObject,
            Billdetail objEntity, Vector objectClass, long billdetailId, int start, int idNotaType) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader("", "3%");
        ctrlist.addHeader("", "13%");
        ctrlist.addHeader("", "26%");
        ctrlist.addHeader("", "5%");
        ctrlist.addHeader("", "5%");
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
            Unit unit = (Unit) temp.get(1);
            rowx = new Vector();
            start = start + 1;

            if (billdetailId == billdetail.getOID()) {
                index = i;
            }
            if (index == i && (iCommandDetail == Command.EDIT || iCommandDetail == Command.ASK)) {
                rowx.add("" + start);
                if (idNotaType == PstBillMain.RETUR || idNotaType == PstBillMain.RETUR_ALL) {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + (billdetail.getMaterialId())
                            + "\">"
                            + "<input  type=\"hidden\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + billdetail.getUnitId() + "\">"
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\"><a href=\"javascript:cmdCheckRetur()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                } else {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + (billdetail.getMaterialId())
                            + "\">"
                            + "<input  type=\"hidden\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + billdetail.getUnitId() + "\">"
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\"><a href=\"javascript:cmdCheck()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                }
                rowx.add("<input tabindex=\"32\" type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + billdetail.getItemName() + "\" onKeyPress=\"keybrdPress(this, event, this.value)\" class=\"formElemenR\" readOnly>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + (billdetail.getQty()) + "\" onKeyPress=\"keybrdPress(this, event, this.value)\"  class=\"formElemenR\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"formElemenR\" style=\"text-align:right\" readOnly></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"34\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"" + (billdetail.getItemPrice()) + "\" class=\"formElemenR\"  readOnly></div>");
                rowx.add("<input tabindex=\"35\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\" value=\"" + (billdetail.getDisc1()) + "\" onKeyPress=\"keybrdPress(this, event, this.value)\"  class=\"formElemenR\" >");
                rowx.add("<input tabindex=\"36\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"" + (billdetail.getDisc2()) + "\" onKeyPress=\"keybrdPress(this, event, this.value)\"  class=\"formElemenR\">");
                rowx.add("<input tabindex=\"37\" type=\"text\" size=\"8\" onKeyUp=\"javascript:calculate()\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"" + (billdetail.getDisc()) + "\" onKeyPress=\"keybrdPress(this, event, this.value)\"  class=\"formElemenR\">");
                rowx.add("<input tabindex=\"38\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_DISC] + "\" value=\"" + (billdetail.getTotalDisc()) + "\" class=\"formElemenR\" readOnly>");
                rowx.add("<div align=\"right\"><input tabindex=\"39\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"" + billdetail.getTotalPrice() + "\" class=\"formElemenR\"  readOnly></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"40\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"" + billdetail.getNote() + "\" onKeyPress=\"keybrdPress(this, event, this.value)\"   class=\"formElemenR\"  ></div>");

            } else {
                rowx.add("" + start + "");
                rowx.add(billdetail.getSku());
                rowx.add(billdetail.getItemName());
                //Mengecek serialcode
                int requiredSerialNumber = 0;
                Vector listSerialCode = PstMaterial.list(0, 0, PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " ='" + billdetail.getSku() + "'", "");
                for (int j = 0; j < listSerialCode.size(); j++) {
                    Material material = (Material) listSerialCode.get(j);
                    requiredSerialNumber = material.getRequiredSerialNumber();
                }
                if (requiredSerialNumber == PstMaterial.REQUIRED) {
                    rowx.add("<div align=\"right\">" + billdetail.getQty() + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + billdetail.getQty() + "</div>");
                }
                rowx.add("<div align=\"right\">" + unit.getCode() + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getItemPrice()) + "</div>");
                rowx.add("<div align=\"right\">" + billdetail.getDisc1() + "</div>");
                rowx.add("<div align=\"right\">" + (billdetail.getDisc2()) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getDisc()) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getTotalDisc()) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(billdetail.getTotalPrice()) + "</div>");
                rowx.add("<div align=\"right\">" + (billdetail.getNote() == null ? "" : billdetail.getNote()) + "</div>");
            }
            lstData.add(rowx);
        }
        return ctrlist.draw();
        
    }

%>

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int iCommandDetail = FRMQueryString.requestInt(request, "commandDetail");
            int iCommandTmp = FRMQueryString.requestInt(request, "commandTmp");

            int start = FRMQueryString.requestInt(request, "start");
            int startDetail = FRMQueryString.requestInt(request, "start_detail");
            int startPayment = FRMQueryString.requestInt(request, "start_payment");

            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");

            long idCurrency = FRMQueryString.requestLong(request, "CurrId");
            long idCustomer = FRMQueryString.requestLong(request, "CustId");
            long idPriceType = FRMQueryString.requestLong(request, "PriceTypeId");
            long idStandartRate = FRMQueryString.requestLong(request, "StandartRateId");
            double amounttotal = FRMQueryString.requestDouble(request, "total_amount");
            long idCust = FRMQueryString.requestLong(request, "cusId");
            int idNotaType = FRMQueryString.requestInt(request, "nota_type");
            long idbilldetailTmp = FRMQueryString.requestLong(request, "billdetailTmp");
            int idTransType = FRMQueryString.requestInt(request, "trans_type");
            double stockReturn = FRMQueryString.requestDouble(request, "stock_return");
            long oidbillmain = FRMQueryString.requestLong(request, "oidbillmaintmp");

            double creditLimit = FRMQueryString.requestDouble(request, "creditlimit");
            double outstanding = FRMQueryString.requestDouble(request, "outstanding");
            double available = FRMQueryString.requestDouble(request, "available");
            int NotaSales = FRMQueryString.requestInt(request, "notasalestype");
            int NotatypeTmp = FRMQueryString.requestInt(request, "notasalestypeTmp");
            int codeCustomer = FRMQueryString.requestInt(request, "code_cust");
            String compAddress = FRMQueryString.requestString(request, "compAddr");
            int tmpsalesorder = FRMQueryString.requestInt(request, "allTotal");

            int printHistory = FRMQueryString.requestInt(request, "print_history");
            int confirmSupervisor = FRMQueryString.requestInt(request, "conf_supervisor");

            

            int docType = 0;
            int transStatus = 1;
            double grandtotalTmp = 0;
            double sellingRate = 0;

            if (iCommand == Command.CANCEL) {
                oidBillMain = oidbillmain;
                oidbillmain = 0;
                iCommand = Command.SAVE;
            }

            if (iCommandTmp == Command.CANCEL) {
                iCommand = Command.SAVE;
                iCommandTmp = Command.RESET;
            }

            if (idNotaType == PstBillMain.RETUR || idNotaType == PstBillMain.RETUR_ALL) {
                docType = 1;
                transStatus = 0;
            }

            String itemName = textListOrderItem[SESS_LANGUAGE][2];
            
            long oidBillDetail = FRMQueryString.requestLong(request, "hidden_bill_detail_id");
            long oidCashPayment = FRMQueryString.requestLong(request, "hidden_cash_payment_id");

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
            String salesCode = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]);

            int idSaleType = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]);

            String shippAddr = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]);
            String shippCity = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]);
            String shippProv = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]);
            String shippCountry = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]);
            String shippTlp = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]);
            String shippFax = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]);
            String shippZip = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]);
            String notes = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]);

            long currencyId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID]);

            double rate = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]);
            double amountTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]);
            double taxPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]);
            double taxValueTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]);
            double discTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]);
            double discPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]);
            int DiscType = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]);
            int typeSalesOrder = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]);
            long custId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
            //FRMQueryString.requestLong(request, "cusId");
            String custName = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]);
            String compName = FRMQueryString.requestString(request, "compName");
            //String salesName = FRMQueryString.requestString(request, "sales_name");
            //String warehouseSales= FRMQueryString.requestString(request, "warehouseSales");
            
            int vectSize = PstBillMain.getCount(whereClause);
            BillMain billMain = ctrlBillMain.getBillMain();

            msgString = ctrlBillMain.getMessage();
            String whereData = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6 AND "+PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_USER_ID] + " = "+billMain.getAppUserId();
            Vector sale = PstMappingUserGroup.list(0, 0, whereData, "");
            
            for (int i = 0; i < sale.size(); i++) {
                AppUser sales = (AppUser) sale.get(i);
                salesName = sales.getFullName();
                DataCustom dc = new DataCustom();
                long oidLoc = 0;
                String whereLoc = PstDataCustom.fieldNames[PstDataCustom.FLD_OWNER_ID]+" = "+sales.getOID();
                Vector loc = PstDataCustom.list(0, 0, whereLoc, "");
                for (int ii = 0; ii < loc.size(); ii++) {
                dc = (DataCustom) loc.get(ii);
                oidLoc = Long.parseLong(dc.getDataValue());
                }
                Location xLocation=PstLocation.fetchExc(oidLoc);
                locationSales=oidLoc;
                warehouseSales= xLocation.getName();
            }

//            Vector listSalesOrder = PstSales.list(0, 0, PstSales.fieldNames[PstSales.FLD_CODE] + " ='" + salesCode + "'", "");
            //long locationSales=0;
            //long salesCurrencyId=0;
//            for (int i = 0; i < listSalesOrder.size(); i++) {
//                Sales sales = (Sales) listSalesOrder.get(i);
//                salesName = sales.getName();
//                Location xLocation=PstLocation.fetchExc(sales.getLocationId());
//                locationSales=sales.getLocationId();
//                warehouseSales= xLocation.getName();
//                salesCurrencyId = sales.getDefaultCurrencyId();
//            }
            
            //Mencari Rate
            Vector listRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='" + currencyId + "'", "");
            for (int i = 0; i < listRate.size(); i++) {
                StandartRate standartRate = (StandartRate) listRate.get(i);
                rate = standartRate.getSellingRate();
            }

            // iErrCode = ctrlBillMain.action(iCommand, oidbillmain, idCust);
            if (iCommand == Command.EDIT || iCommand == Command.DELETE) {
                iErrCode = ctrlBillMain.action(iCommand, oidbillmain, idCust);
                billMain = ctrlBillMain.getBillMain();
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                idCustomer = billMain.getCustomerId();
                idTransType = billMain.getTransctionType();
                int transStatusTmp = billMain.getTransactionStatus();
                iCommand = 0;
                if(transStatusTmp == 1 && idTransType==0){
                   // iCommandDetail = Command.ADD;
                }
            }

            if (iCommand == Command.SAVE) {
                iErrCode = ctrlBillMain.action(iCommand, oidbillmain, idCust);
                billMain = ctrlBillMain.getBillMain();
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                idCustomer = billMain.getCustomerId();
                idTransType = billMain.getTransctionType();
                iCommand = 0;
                iCommandDetail = Command.ADD;

            }
            //Mencari Price Type Id
            long idCustTmp = 0;
            Vector listCodeCustomerTmp = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='1'", "");
            MemberGroup memberGroupCodeTmp = (MemberGroup) listCodeCustomerTmp.get(0);
            idCust = memberGroupCodeTmp.getOID();
            if (idCustomer == memberGroupCodeTmp.getOID()) {
                idCustTmp = memberGroupCodeTmp.getOID();
            } else {
                listCodeCustomerTmp = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='3'", "");
                MemberGroup memberGroup = (MemberGroup) listCodeCustomerTmp.get(0);
                idCustTmp = memberGroup.getOID();
            }

            Vector listPriceType = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + idCustTmp + "'", "");
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
            

            //Mencari selling Rate 2020-01-02 peralihan
//            Vector listSellingRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='"+salesCurrencyId+"'", "");
//            if (listSellingRate.size()!=0) {
//                StandartRate standartRate = (StandartRate) listSellingRate.get(0);
//                sellingRate = standartRate.getSellingRate();
//            }

            //Untuk Bill Detail
            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            ctrlBillDetail.setLanguage(SESS_LANGUAGE);
            FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();

            if (iCommandTmp == Command.SUBMIT) {
                long MatId = FRMQueryString.requestLong(request, frmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]);
                int countExist = PstBillDetail.getCountExist(oidbillmain, MatId);
                if (countExist == 0 || iCommandDetail == Command.EDIT) {
                    iCommandDetail = Command.SAVE;
                    iCommandTmp = 0;
                } else {
                    iCommandTmp = Command.EDIT;
                }
            }
            iErrCode = ctrlBillDetail.action(iCommandDetail, oidBillDetail, oidbillmain);
            //amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
            //int DiscType = FRMQueryString.requestInt(request, frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC_TYPE]);

            Billdetail billdetail = ctrlBillDetail.getBillDetail();
            msgString = ctrlBillDetail.getMessage();

            //Mencari Shift Id, cascashier id, location id
            long shiftId = 0;
            long cashcashierId = 1;
            long cashmasterId = 0;
            long locationId = 1;
            double taxValue = 0.00;
            double taxPct = 0.00;
            int taxCode = 0;
            if(salesCode == ""){
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

            Vector listTax = PstLocation.list(0, 0, PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID] + " ='" + locationId + "'", "");
            for (int i = 0; i < listTax.size(); i++) {
                Location location = (Location) listTax.get(i);
                taxCode = location.getTaxSvcDefault();
                taxPct = location.getTaxPersen();
                taxValue = location.getTaxValue();
            }
            }

            /*switch list BillMain*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/
            /* get record to display */
            int transStatusTmp = 1;
            if (oidbillmain != 0) {
                whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidbillmain + "'";
            } else {
                whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
            }
            listBillMain = PstBillMain.list(start, recordToGet, whereClause, orderClause);
            if (listBillMain.size() > 0) {
                billMain = (BillMain) listBillMain.get(0);
                transStatusTmp = billMain.getTransactionStatus();
            }

            if (oidBillMain == 0 && oidbillmain != 0 && iCommandDetail == Command.LIST) {
                double amounttotalTmp = PstBillDetail.getTotalAmount(oidbillmain);
                double discValTmp = billMain.getDiscount();
                double taxValTmp = billMain.getTaxValue();
                grandtotalTmp =(amounttotalTmp - discValTmp) + taxValTmp;
                DiscType = billMain.getDiscType();
                iCommandDetail = 0;
            }

            /**Untuk Bill Detail*/
            String whereClauseDetail = "";
            String orderClauseDetail = "";
            int recordToGetDetail = 1000;
            Vector listBillDetail = new Vector();

            if (idNotaType == PstBillMain.RETUR) {
                amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
                double discValTmp = billMain.getDiscount();
                double taxValTmp = billMain.getTaxValue();
                grandtotalTmp =(amounttotal - discValTmp) + taxValTmp;
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;

            } else if (idNotaType == PstBillMain.RETUR_ALL) {
                amounttotal = PstBillDetail.getTotalAmount(oidBillMain);
                double discValTmp = billMain.getDiscount();
                double taxValTmp = billMain.getTaxValue();
                grandtotalTmp =(amounttotal - discValTmp) + taxValTmp;
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;

            } else {
                amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;
            }

            int vectSizeDetail = PstBillDetail.getCount(whereClauseDetail);

            if (iCommandDetail == Command.FIRST || iCommandDetail == Command.PREV || iCommandDetail == Command.NEXT || iCommandDetail == Command.LAST) {
                startDetail = ctrlBillDetail.actionList(iCommandDetail, startDetail, vectSizeDetail, recordToGetDetail);
            }

            listBillDetail = PstBillDetail.listMat(startDetail, recordToGetDetail, whereClauseDetail, orderClauseDetail);
            if (listBillDetail.size() < 1 && startDetail > 0) {
                if (vectSizeDetail - recordToGetDetail > recordToGetDetail) {
                    startDetail = startDetail - recordToGetDetail;
                } else {
                    startDetail = 0;
                    iCommandDetail = Command.FIRST;
                    prevCommand = Command.FIRST;
                }
                listBillDetail = PstBillDetail.listMat(startDetail, recordToGetDetail, whereClauseDetail, orderClauseDetail);
            }

            //Save All Retur
            if (iCommandDetail == Command.GOTO) {
                for (int i = 0; i < listBillDetail.size(); i++) {
                    Vector temp = (Vector) listBillDetail.get(i);
                    billdetail = (Billdetail) temp.get(0);

                    billdetail.setBillMainId(oidbillmain);
                    stockReturn = PstBillDetail.getCountStock(oidBillMain, billdetail.getMaterialId());
                    if (stockReturn > 0) {
                        billdetail.setQty(stockReturn);
                        long oid = PstBillDetail.insertExc(billdetail);
                    }
                }
                double amount = PstBillDetail.getTotalAmount(oidbillmain);
                double totalAmount = PstBillMain.updateTotalAmount(oidbillmain, amount);
                iCommandDetail = 0;
                idNotaType = PstBillMain.RETUR;
            }

            //update grand total amount
            if (iCommand == Command.GOTO) {
                double grandtotal = PstBillMain.updateGrandTotal(oidbillmain, amountTmp, taxPctTmp, taxValueTmp, discTmp, discPctTmp, DiscType);
                
                iCommandTmp = Command.CONFIRM;
                grandtotalTmp = amountTmp;
            }

            /** kondisi ini untuk manampilakn form tambah item. posisi pada baris program paling bawah */
            if (iCommandDetail == Command.SAVE && iErrCode == 0 || (iCommandDetail == Command.DELETE && idNotaType != PstBillMain.RETUR)) {
                oidBillDetail = 0;
                iCommandDetail = Command.ADD;
            }

            if (iCommandDetail == Command.DELETE && idNotaType == PstBillMain.RETUR) {
                oidBillDetail = 0;
            }

            int iCmd = Integer.parseInt((request.getParameter("cmd") == null) ? "0" : request.getParameter("cmd"));

            int appLanguage = 0;
            int approvalStatus = 0;

            if (iCommandTmp == Command.SAVE) {
                String loginID = FRMQueryString.requestString(request, "login_id");
                String passwd = FRMQueryString.requestString(request, "pass_wd");

                AppUser user = PstAppUser.getByLoginIDAndPassword(loginID, passwd);
                if (user == null) {
                    approvalStatus = 2;
                } else {
                    if (user.getUserGroupNew() == 1) {
                        approvalStatus = 1;
                    } else {
                        approvalStatus = 2;
                    }
                }
            }

            MemberReg memberReg = new MemberReg();
            //list member
            if(billMain.getCustomerId() != 0){
                custId = billMain.getCustomerId();
            }
            Vector listMemberReg = PstMemberReg.list(start, recordToGet, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " = '" + custId + "'", orderClause);
            if (listMemberReg.size() > 0) {
                memberReg = (MemberReg) listMemberReg.get(0);
            }
            String address = "";
            if(memberReg.getBussAddress()!= null && memberReg.getBussAddress().length() > 0){
                address = memberReg.getBussAddress();
            }else{
                address = memberReg.getHomeAddr();
            }
            

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
    <head>
        <title>Dimata ProChain - </title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
        <link rel="stylesheet" href="../styles/tab_cashierweb.css" type="text/css">
        <script src="../styles/jquery.min.js"></script>
        <script type="text/javascript">
             function checkAjaxOutlet(value){
                    var stateCheck =  document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>.value;
                   // alert(stateCheck);
                    $.ajax({
                        url : "<%=approot%>/servlet/com.dimata.posbo.ajax.checkStockOutletOrder?<%=checkStockOutletOrder.CHECK_QTY_ORDER%>="+value+"&<%=checkStockOutletOrder.LOCATION_SALES%>=<%=locationSales%>&<%=checkStockOutletOrder.TYPE_CHECK_STOCK_SALES%>=<%=typeSalesOrder%>&<%=checkStockOutletOrder.CHECK_MATERIAL_ID%>="+stateCheck,
                        type : "POST",
                        async : false,
                        success : function(data) {
                            //Do something with the data here untuk fungsi success
                            // var result=data;
                            if($.trim(data)!="false"){
                               //jika data yang dikirim lain dari kata succses maka munculkan resultnya
                               alert(data);
                               //dan ubah default value hidden_state menjadi 1
                               document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value="";
                               document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                            }else{
                                // jika enggak succsess maka ubah default value hidden_state menjadi 0
                                 document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.focus();
                            }
                        }
                    });
          }

            function showData(value){
               var oidCurrencyId=value;
               checkAjax(oidCurrencyId);
            }

            function checkAjax(oidCurrencyId){
                $.ajax({
                url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>="+oidCurrencyId+"",
                type : "POST",
                async : false,
                success : function(data) {
                     document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>.value=data;
                     document.frmcashier.rate.value=data;
                }
            });

            }
        </script>
        <script type="text/javascript">
            
            function cancelmain(){
                var transType = document.frmcashier.sale_type.value;
                // alert(transType);
                var notaType = document.frmcashier.nota_type.value;
                //document.frmcashier.<!--%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;
                document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]%>.value = 0;
                document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                //document.frmcashier.action = "outlet_cashier.jsp";
                //document.frmcashier.submit();
                //alert(transType);
                if(transType == <%=PstBillMain.TRANS_TYPE_CASH%>){
                    cmdCancelOpenBill();
                }else{
                    document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                    cmdCancelBill();
                }

            }

            function cmdApprovalSupervisor(){
                document.frmcashier.commandTmp.value="<%=Command.SAVE%>";
                document.frmcashier.action = "outlet_cashier.jsp";
                document.frmcashier.submit();
            }

            function cekSupervisor(){
                var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                if(oidbillmain == 0){
                    cmdAddNew();
                }else{
                    document.frmcashier.conf_supervisor.value="0";
                    document.frmcashier.commandTmp.value="<%=Command.APPROVE%>";
                    document.frmcashier.action = "outlet_cashier.jsp";
                    document.frmcashier.submit();
                }

            }

            function printFormHtml() {
                var printCount = document.frmcashier.print_history.value;
                var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                
                if(oidbillmain == 0){
                    alert("Data is empty !");
                }else{
                    if(printCount > 0){
                        
                    document.frmcashier.conf_supervisor.value="1";
                    document.frmcashier.command.value="0";
                     document.frmcashier.commandTmp.value="<%=Command.APPROVE%>";
                    document.frmcashier.action = "outlet_cashier.jsp";
                    document.frmcashier.submit();
                    }else{
                        window.open("sales_order_print.jsp?hidden_bill_main_id=<%=oidbillmain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                        
                        cmdAddNew();
                    }
                    
                }
            }

            function disablefield(){
                var taxCode = document.frmcashier.tax_code.value;
                var taxValue = cleanNumberFloat(document.frmcashier.tax_value.value,guiDigitGroup,guiDecimalSymbol);

                if (taxCode == 0 || taxCode == 1) {
                    document.getElementById('codetax').disabled='disabled';
                    document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = taxValue;
                }else{
                    document.getElementById('codetax').disabled='';
                }
            }

            function keybrdPress(frmObj, event, value) {
                var notaType = document.frmcashier.nota_type.value;
                if(event.keyCode == 13) {
                    switch(frmObj.name) {
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>':
                                if(notaType == <%=PstBillMain.RETUR%> || notaType == <%=PstBillMain.RETUR_ALL%>){
                                cmdCheckRetur();
                            }else{
                                cmdCheck();
                            }
                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>':
                                //jika kursor berada di field qty buatkan fungsi AJX
                                if(notaType == <%=PstBillMain.RETUR%>){
                                   document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>.focus();
                                }else{
                                  var qtyInput = document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value;
                                    if(qtyInput==0|| qtyInput==""){
                                        alert("Tidak Bisa di Proses, QTY tidak boleh 0");
                                        document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                                    }else{
                                        checkAjaxOutlet(value);
                                    }
                                }
                                
                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>.focus();

                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.focus();

                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>.focus();

                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>':
                                cmdApproval2();
                            document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.focus();
                            break;

                        default:
                            break;
                        }
                    }else if (event.keyCode==27){ //esc
                         switch(frmObj.name) {
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>':
                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>':
                                  document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value="";
                                  document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.focus();
                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>':
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.value="";
                                   document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>':
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value="";
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>':
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.value="";
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.focus();

                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>':
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.value="";
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.focus();
                                break;
                            case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>':
                                    cmdApproval2();
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.value="";
                                    document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.focus();
                                break;
                            default:
                                break;
                        }
                    } else if (event.keyCode==112 ){ //F1 = Add New
                            var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                                if(oidbillmain != 0 ){
                                    //ar con = confirm("Process will be cancel.\nDo you want to reset this page ?");
                                    var con = confirm("Yakin Untuk Membuat Invoice Baru ?");
                                    if (con ==true)
                                    {
                                        cmdAddNew();
                                    }
                                }else{
                                    cmdAddNew();
                                }
                     }
                }

                function keybrdPressHeader(frmObj, event,value){
                       // alert(frmObj);
                        if(event.keyCode == 13) {
                                 switch(frmObj.name) {
                                        case 'compName':
                                                if(value==""){
                                                    cmdCheck2();
                                                }else{
                                                    document.all.compAddr.focus();
                                                }
                                                break;
                                        case 'compAddr':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>.focus();
                                                break;
                                         case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>.focus();
                                                break;
                                         case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>.focus();
                                                break;
                                        default:
                                                break;
                                  }
                        }else if (event.keyCode==27){ //esc
                                switch(frmObj.name) {
                                         case 'compName':
                                                document.all.compAddr.focus();
                                                break;
                                        case 'compAddr':
                                                document.all.compName.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>':
                                                document.all.compAddr.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>.focus();
                                                break;
                                        case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>.focus();
                                                break;
                                         case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.focus();
                                                break;
                                         case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>.focus();
                                                break;
                                         case '<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>':
                                                document.all.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>.focus();
                                                break;
                                        default:
                                                break;
                                  }
                         }else if (event.keyCode==120){ //F9 = save
                            var con = confirm("Yaking Untuk Menyimpan Data ?");
                            if (con ==true){
                                <%if (codeCustomer == 3 && NotaSales == PstBillMain.NONE) {%>
                                    cmdApproval(0);
                                <%} else {%>
                                        cmdApproval(1);
                                <%}%>
                            }
                         }else if (event.keyCode==113 ){ //F2 = search Customer
                            <% if(oidBillMain!=0){%>
                                alert("Data Customer Sudah Tersimpan, Tekan F9 Untuk melanjutkan Transaksi");
                            <%}else{%>
                                 cmdCheck2();
                            <%}%>
                         }else if (event.keyCode==112 ){ //F1 = Add New
                            var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                                if(oidbillmain != 0 ){
                                    //ar con = confirm("Process will be cancel.\nDo you want to reset this page ?");
                                    var con = confirm("Yakin Untuk Membuat Invoice Baru ?");
                                    if (con ==true)
                                    {
                                        cmdAddNew();
                                    }
                                }else{
                                    cmdAddNew();
                                }
                         }
                }

                function keybrdPressTotal(frmObj, event) {
                    if(event.keyCode == 13) {
                        switch(frmObj.name) {
                            case 'disc_global':
                                document.all.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.focus();
                                break;
                            case '<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>':
                                    //changeTax();
                                document.all.ButtonSalesOrder.focus();
                                break;

                            default:
                                break;
                            }
                        }
                    }

                    var newwindow = null;
                    function gothere(oSelect) {

                        if (newwindow && !newwindow.closed) newwindow.close();
                        if(oSelect.selectedIndex > 0){
                            //alert(oSelect.selectedIndex);
                            var url = oSelect[oSelect.selectedIndex].value;
                            newwindow=window.open(url,"newwindow","width=1200,height=1100");
                        }
                        if (newwindow && !newwindow.closed) newwindow.focus();

                        document.frmcashier.select.value = 2;
                    }

                    function goSubmit(){
                        var limitType = -1;
                        document.frmcashier.notasalestypeTmp.value = limitType;
                        document.frmcashier.submit();
                    }

                    function calculate(){

                        var sisaStock = cleanNumberFloat(document.frmcashier.stock_return.value,guiDigitGroup,guiDecimalSymbol);
                        var qty = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var cost = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var lastDisc = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var lastDisc2 = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_2]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var lastDiscNom = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC]%>.value,guiDigitGroup,guiDecimalSymbol);

                        var sum = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                        var notaType = cleanNumberFloat(document.frmcashier.nota_type.value,guiDigitGroup,guiDecimalSymbol);
                        var totalAmountTmp = "";
                        var availableCredit = "";
                        var codecust = cleanNumberFloat(document.frmcashier.code_cust.value,guiDigitGroup,guiDecimalSymbol);
                        var typePatment = document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]%>.value;

                        var maxlenght = 5;
                        var qtyLenght = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value.length;
                        if(qtyLenght > maxlenght){
                            alert("Quantity can't be more than 5 characters");
                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.focus();
                        }

                        if(codecust == 3){
                            availableCredit = cleanNumberFloat(document.frmcashier.availableTmp.value,guiDigitGroup,guiDecimalSymbol);
                        }

                        if(isNaN(qty)){
                            alert("Must be a number !!");
                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value=0;
                            return;
                        }

                        if(qty<0.0000){
                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value=0;
                            return;
                        }

                        if(parseFloat(qty) > sisaStock && notaType==<%=PstBillMain.RETUR%>){
                            alert("The returned quantity is insufficient, the remaining quantity is: " +sisaStock);
                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value=0;
                        }else{

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

                            var realCost = qty * cost;

                            var totalCost = parseFloat(totalMinus) - parseFloat(totaldiscount2);
                            totalCost = totalCost - lastDiscNom;

                            var lastTotal = qty * totalCost;

                            var totalDiscAll = parseFloat(realCost) - parseFloat(lastTotal);

                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_DISC]%>.value = parseFloat(totalDiscAll);
                            document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_PRICE]%>.value = parseFloat(lastTotal);//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                        }
                    }

                    function calculateTotal(){
                        var subtotal = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                        var disc_global = cleanNumberFloat(document.frmcashier.disc_global.value,guiDigitGroup,guiDecimalSymbol);
                        var disc_type = document.frmcashier.disc_type.value;
                        var taxCode = document.frmcashier.tax_code.value;
                        var taxVal = cleanNumberFloat(document.frmcashier.tax_value.value,guiDigitGroup,guiDecimalSymbol);
                        var taxPct = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value,guiDigitGroup,guiDecimalSymbol);

                        if(isNaN(disc_global) || (disc_global==""))
                            disc_global = 0.0;
                        if(isNaN(taxVal) || (taxVal==""))
                            taxVal = 0.0;

                        var total_amount = "";
                        var pct_value ="";
                        if(disc_type == 0){
                            pct_value = disc_global;
                            total_amount = subtotal -  pct_value;
                        }else{
                            pct_value = subtotal * disc_global / 100;
                            total_amount = parseFloat(subtotal) - parseFloat(pct_value);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]%>.value = disc_global;
                        }

                        if(taxCode == 1 || taxCode == 3){
                            total_amount = total_amount + parseFloat(taxVal);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = taxVal;
                        }

                        if(taxCode == 3){
                            var real_amount = parseFloat(total_amount) - parseFloat(taxVal);
                            var pct_value_tax = real_amount * taxPct / 100;
                            total_amount = parseFloat(real_amount) + parseFloat(pct_value_tax);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = pct_value_tax;
                        }else if(taxCode ==2){
                            total_amount =  parseFloat(total_amount) - parseFloat(taxVal);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = taxVal;
                        }
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]%>.value = pct_value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = total_amount;
                        document.frmcashier.grandtotal.value = total_amount;
                    }

                    function changeTax(){
                        var totalAmount = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var taxPct = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var taxCode = document.frmcashier.tax_code.value;
                        var taxVal = cleanNumberFloat(document.frmcashier.tax_value.value,guiDigitGroup,guiDecimalSymbol);

                        var grand_total_amount = "";
                        if(isNaN(totalAmount) || (totalAmount==""))
                            totalAmount = 0.0;
                        if(isNaN(taxVal) || (taxVal==""))
                            taxVal = 0.0;

                        if(taxCode == 3){
                            var real_amount = parseFloat(totalAmount) - parseFloat(taxVal);
                            var pct_value = real_amount * taxPct / 100;
                            grand_total_amount = parseFloat(real_amount) + parseFloat(pct_value);
                        }else if(taxCode ==2){
                            grand_total_amount = real_amount;
                        }

                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = pct_value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = grand_total_amount;
                        document.frmcashier.grandtotal.value = grand_total_amount;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);

                    }

                    function calculatePayment(){
                        var pay = cleanNumberFloat(document.frmcashier.bayar.value,guiDigitGroup,guiDecimalSymbol);
                        var total = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                        var change = parseFloat(pay) - parseFloat(total);
                        document.frmcashier.kembalian.value = parseFloat(change);
                    }

                    function goStock(oid){
                        var strvalue  = "bill_detail_stockcode.jsp?command=<%=Command.FIRST%>"+
                            "&hidden_bill_detail_id="+oid;
                        window.open(strvalue,"stockcode", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdChkNota(code){
                        if(code == <%=PstBillMain.RETUR%>){
                            strvalue  = "srcRetur.jsp?command=<%=Command.FIRST%>"+
                                "&trans_type=1";
                            window.open(strvalue,"customer2", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }
                    }

                    function cmdAdd()
                    {
                        document.frmmatkasir.hidden_bill_detail_id.value="0";
                        document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdAsk(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.ASK%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdConfirmDelete(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.DELETE%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function billDetailEdit(oidBillDetail)
                    {
                        //alert(oidBillDetail);
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function billMainDelete(oidbillmain)
                    {
                        var con = confirm("Data will be canceled");
                        if (con ==true)
                        {
                            document.frmcashier.oidbillmaintmp.value=oidbillmain;
                            document.frmcashier.command.value="<%=Command.DELETE%>";
                            document.frmcashier.commandDetail.value="<%=Command.FIRST%>";
                            document.frmcashier.action="outlet_cashier.jsp";
                            document.frmcashier.submit();
                        }
                    }

                    function billDetailDelete(oidBillDetail)
                    {
                        var con = confirm("Data item will be deleted");
                        if (con ==true)
                        {
                            document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                            document.frmcashier.commandDetail.value="<%=Command.DELETE%>";
                            document.frmcashier.action="outlet_cashier.jsp";
                            document.frmcashier.submit();
                        }
                    }

                    function cmdCancel(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdBacktoList()
                    {
                     cmdAddNew();

                            document.frmcashier.command.value="<%=Command.BACK%>";
                            document.frmcashier.action="salesorder_list.jsp";
                            document.frmcashier.submit();
                    }

                    function cmdListFirst()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.FIRST%>";
                        document.frmcashier.prev_command.value="<%=Command.FIRST%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListPrev()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.PREV%>";
                        document.frmcashier.prev_command.value="<%=Command.PREV%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListNext()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.NEXT%>";
                        document.frmcashier.prev_command.value="<%=Command.NEXT%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListLast()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.LAST%>";
                        document.frmcashier.prev_command.value="<%=Command.LAST%>";
                        document.frmcashier.action="outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdChangeMember(){
                        document.frmcashier.submit();
                    }

                    function cmdApproval(code){
                        
                        var custType = document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value;
                        
                        if(custType !=""){
                            if(code==0){
                                var creditlimmit = cleanNumberFloat(document.frmcashier.creditlimit.value,guiDigitGroup,guiDecimalSymbol);
                                var available = cleanNumberFloat(document.frmcashier.available.value,guiDigitGroup,guiDecimalSymbol);
                                var availableTmp = cleanNumberFloat(document.frmcashier.availableTmp.value,guiDigitGroup,guiDecimalSymbol);
                                if((creditlimmit<="") || isNaN(creditlimmit) || (creditlimmit<=0.0)||(available<="")  || isNaN(available) || (available<=0.0) ||(availableTmp<="")){
                                   alert(creditlimmit+" ; "+available);
                                   alert("credit limit is empty or insufficient !");
                                }else{
                                    document.frmcashier.command.value="<%=Command.SAVE%>";
                                    document.frmcashier.action = "outlet_cashier.jsp";
                                    document.frmcashier.submit();
                                }
                            }else{
                                document.frmcashier.command.value="<%=Command.SAVE%>";
                                document.frmcashier.action = "outlet_cashier.jsp";
                                document.frmcashier.submit();
                            }
                        }else{
                            alert("Customer Type has not been selected");
                        }

                    }

                    function confrmSaveRetur(code){
                        var tmpcon= confirm("Are you sure to save this process ?");
                        if (tmpcon ==true)
                        {
                            cmdSaveAll();
                        }else{
                            //document.frmcashier.commandTmp.value="<!--%=Command.RESET%>";
                            //cmdCancelBill();
                            cmdCancelOpenBill();
                        }
                    }

                    function cmdSaveAll()
                    {
                        
                        document.frmcashier.allTotal.value = 1;
                        document.frmcashier.command.value="<%=Command.GOTO%>";
                        document.frmcashier.action = "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdEditMain(oidbillmain){
                        document.getElementById('shAddrs').disabled='';
                        document.getElementById('shcity').disabled='';
                        document.getElementById('shprov').disabled='';
                        document.getElementById('shcountry').disabled='';
                        document.getElementById('shtlp').disabled='';
                        document.getElementById('shfax').disabled='';
                        document.getElementById('shzip').disabled='';
                        

                        var codeCustomer = document.frmcashier.code_cust.value;
                        
                        if(codeCustomer == 3){
                            document.getElementById('nameperson').disabled='';
                            document.getElementById('namecomp').disabled='';
                        }

                        document.frmcashier.oidbillmaintmp = oidbillmain;
                        document.frmcashier.command.value="<%=Command.EDIT%>";
                        document.frmcashier.commandDetail.value=0;
                        document.frmcashier.action = "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdApproval2(){
                        var sku = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value;
                        var qty = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var lastTotal = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var sum = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                        var notaType = cleanNumberFloat(document.frmcashier.nota_type.value,guiDigitGroup,guiDecimalSymbol);
                        var codecust = cleanNumberFloat(document.frmcashier.code_cust.value,guiDigitGroup,guiDecimalSymbol);
                        
                        var typePatment = document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]%>.value;

                        var totalAmountTmp = "";
                        var availableCredit = "";

                        if(codecust == 3){
                            availableCredit = cleanNumberFloat(document.frmcashier.availableTmp.value,guiDigitGroup,guiDecimalSymbol);
                        }
                        //untuk menghutung total penjualan
                        if(sum == 0.0){
                            totalAmountTmp = lastTotal;
                        }else{
                            totalAmountTmp = parseFloat(sum) + parseFloat(lastTotal);
                        }

                        if(sku != "" ){
                            if(qty == "" || qty <=0 || isNaN(qty) || lastTotal == "" || lastTotal ==0 || lastTotal == 0.0 ) {
                                 alert("Quatity can not be empty !");
                                    //document.frmcashier.<--%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_AMOUNT]%>.value = parseFloat(totalAmountTmp);
                                
                            }else{
                            if(parseFloat(availableCredit) < lastTotal && codecust==3 && notaType!=<%=PstBillMain.RETUR%> && typePatment==1) {
                                    alert("Credit Limit rest is insufficient !");
                                    document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value=0;
                                }else{
                                    document.frmcashier.subtotal.value = parseFloat(totalAmountTmp);
                                    
                                    if(<%=!privApprovalSalesOrder && salesCode == ""%>){
                                    document.frmcashier.grandtotal.value = parseFloat(totalAmountTmp);
                                    }
                                    document.frmcashier.commandTmp.value="<%=Command.SUBMIT%>";
                                    document.frmcashier.action = "outlet_cashier.jsp";
                                    document.frmcashier.submit();
                               }
                            }
                        }else{
                            alert("Goods item can not be empty");
                        }
                    }


                    function cmdSaveRetur(){
                        document.frmcashier.allTotal.value = 1;
                        document.frmcashier.commandDetail.value="<%=Command.GOTO%>";
                        document.frmcashier.action = "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdAdd2(){
                        document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                        document.frmcashier.action = "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdCheck(){
                        var strvalue  = "materialdosearchcashier.jsp?command=<%=Command.FIRST%>"+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value+
                            "&mat_code="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SKU]%>.value+
                            "&mat_name="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_NAME]%>.value+
                            "&trans_type="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value+
                            "&type_sales_order="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>.value+
                            "&rate="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>.value+
                            "&salesWarehouse=<%=locationSales%>";
                        window.open(strvalue,"material", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdCheckRetur(){
                        var strvalue  = "itemmatdosearch.jsp?command=<%=Command.FIRST%>"+
                            "&bill_main_id="+document.frmcashier.hidden_bill_main_id.value+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value;
                        window.open(strvalue,"material2", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");

                        document.getElementById('disc1').disabled='disabled';
                        document.getElementById('disc2').disabled='disabled';
                        document.getElementById('disc').disabled='disabled';
                    }

                    function cmdCheck2(){
                        strvalue  = "customersearch.jsp?command=<%=Command.FIRST%>";
                        window.open(strvalue,"customer", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdAddNew(){
                        var resetvalue = 0;
                        var codecust = cleanNumberFloat(document.frmcashier.code_cust.value,guiDigitGroup,guiDecimalSymbol);

                        if(codecust == 3){
                            document.frmcashier.outstanding.value=resetvalue;
                            document.frmcashier.available.value=resetvalue;
                            document.frmcashier.creditlimit.value=resetvalue;
                            document.frmcashier.availableTmp.value = resetvalue;
                        }
                        
                        if(<%=!privApprovalSalesOrder && salesCode == ""%>){
                            document.frmcashier.disc_global.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = resetvalue;
                        }

                        document.frmcashier.hidden_bill_main_id.value = resetvalue;
                        document.frmcashier.oidbillmaintmp.value = resetvalue;
                        
                        document.frmcashier.compName.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value = "";
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value = "";
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>.value=resetvalue;
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>.value="";

                        document.frmcashier.allTotal.value = resetvalue;
                        document.frmcashier.notasalestype.value = 0;
                       // document.frmcashier.code_cust.value = resetvalue;
                        
                        document.frmcashier.code_cust.value = resetvalue;
                        document.frmcashier.available_credit.value=resetvalue;
                        
                        document.frmcashier.commandDetail.value=resetvalue;
                        document.frmcashier.commandTmp.value=resetvalue;
                        document.frmcashier.vectSize.value=resetvalue;
                        document.frmcashier.start.value=resetvalue;
                        document.frmcashier.prev_command.value=resetvalue;
                        document.frmcashier.start_detail.value=resetvalue;
                        document.frmcashier.start_payment.value=resetvalue;
                        document.frmcashier.hidden_bill_detail_id.value=resetvalue;
                        document.frmcashier.sale_type.value=resetvalue;
                        document.frmcashier.CurrId.value=resetvalue;
                        document.frmcashier.PriceTypeId.value=resetvalue;
                        document.frmcashier.StandartRateId.value=resetvalue;
                        document.frmcashier.total_amount.value=resetvalue;
                        document.frmcashier.nota_type.value=resetvalue;
                        document.frmcashier.billdetailTmp.value=resetvalue;
                        document.frmcashier.trans_type.value=resetvalue;
                        document.frmcashier.stock_return.value=resetvalue;
                        document.frmcashier.notasalestypeTmp.value="";
                        document.frmcashier.CustId.value=resetvalue;
                        document.frmcashier.compAddr.value="";
                        document.frmcashier.commandDetail.value=resetvalue;
                        document.frmcashier.print_history.value=resetvalue;
                        document.frmcashier.conf_supervisor.value=resetvalue;
                        document.frmcashier.action = "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdBack(){
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;

                        if(oidbillmain == 0){

                            cmdAddNew();

                            document.frmcashier.command.value="<%=Command.BACK%>";
                            document.frmcashier.action="salesorder_list.jsp";
                            document.frmcashier.submit();
                        }else{
                            //add opie-eyek 20130913
                             <% if(salesCode != ""){%>
                                cmdAddNew();
                                document.frmcashier.command.value="<%=Command.BACK%>";
                                document.frmcashier.action="salesorder_list.jsp";
                                document.frmcashier.submit();
                            <%}else{%>
                                var con = confirm("Invoice will be cancel.\nDo you want to go Back to List ?");
                                    if (con ==true)
                                    {
                                        document.frmcashier.commandTmp.value="<%=Command.GOTO%>";
                                        cmdCancelBill();
                                    }
                            <%}%>
                        }
                    }

                    function cmdCancelBill(){
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>.value = oidbillmain ;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;

                        document.frmcashier.command.value="<%=Command.CANCEL%>";
                        //document.frmcashier.commandTmp.value="<!--%=Command.RESET%>";
                        document.frmcashier.action =  "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdCancelOpenBill(){
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>.value = oidbillmain ;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;

                        document.frmcashier.commandTmp.value="<%=Command.CANCEL%>";
                        document.frmcashier.action =  "outlet_cashier.jsp";
                        document.frmcashier.submit();
                    }

                    function goNewPage(){
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                        if(oidbillmain != 0 ){
                            //ar con = confirm("Process will be cancel.\nDo you want to reset this page ?");
                            var con = confirm("Do You Want Add New Bill ?");
                            if (con ==true)
                            {
                                cmdAddNew();
                                //document.frmcashier.commandTmp.value="<--%=Command.RESET%>";
                               // cmdCancelBill();
                            }
                        }else{
                            cmdAddNew();
                        }
                    }

                    function closeshift(){
                        document.frmcashier.command.value = "<%=Command.SAVE%>";
                        document.frmcashier.action = "close_shift.jsp";
                        document.frmcashier.submit();
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

      </script>
        <SCRIPT type="text/javascript">
                    window.history.forward();
                    function noBack() { window.history.forward(); }
        </SCRIPT>
        <!-- #EndEditable -->
    </head>
    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../main/header.jsp" %>
                    <!-- #EndEditable --></td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <% if(privApprovalSalesOrder || salesCode != ""){%>
                        <%@ include file = "../main/mnmain_nodesc.jsp" %>
                    <%}else{%>
                        <%@ include file = "../main/mnmain.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td valign="top" align="left">
                    <form name="frmcashier" method ="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="commandDetail" value="<%=iCommandDetail%>">
                        <input type="hidden" name="commandTmp" value="<%=iCommandTmp%>">
                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="start_detail" value="<%=startDetail%>">
                        <input type="hidden" name="start_payment" value="<%=startPayment%>">
                        <input type="hidden" name="cmd" value="<%=CMD_APPROVAL%>">
                        <input type="hidden" name="disc_type" value="<%=DiscType%>">
                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                        <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">
                        <input type="hidden" name="oidbillmaintmp" value="<%=oidbillmain%>">
                        <input type="hidden" name="hidden_cash_payment_id" value="<%=oidCashPayment%>">
                        <input type="hidden" name="sale_type" value="<%=idSaleType%>">
                        <input type="hidden" name="CurrId" value="<%=idCurrency%>">
                        <input type="hidden" name="CustId" value="<%=idCustomer%>">
                        <input type="hidden" name="PriceTypeId" value="<%=idPriceType%>">
                        <input type="hidden" name="StandartRateId" value="<%=idStandartRate%>">
                        <input type="hidden" name="total_amount" value="<%=amounttotal%>">
                        <input type="hidden" name="nota_type" value="<%=idNotaType%>">
                        <input type="hidden" name="billdetailTmp" value="<%=idbilldetailTmp%>">
                        <input type="hidden" name="trans_type" value="<%=idTransType%>">
                        <input type="hidden" name="rec_type" value="">
                        <input type="hidden" name="stock_return" value="<%=stockReturn%>">
                        <input type="hidden" name="notaSalesType" value="<%=NotaSales%>">
                        <input type="hidden" name="notasalestypeTmp" value="<%=NotatypeTmp%>">
                        <input type="hidden" name="available_credit" value="<%=available%>">
                        <!--input type="hidden" name="code_custTmp" value="<--%=codeCustomer%>"-->
                        <input type="hidden" name="tax_code" value="<%=taxCode%>">
                        <input type="hidden" name="code_cust" value="<%=codeCustomer%>">
                        <input type="hidden" name="customerId" value="">
                        <!--input type="hidden" name="count_exist" value="<--%=countExist%>"-->
                        <input type="hidden" name="tax_value" value="<%=taxValue%>">
                        <input type="hidden" name="allTotal" value="<%=tmpsalesorder%>">
                        <input type="hidden" name="print_history" value="<%=printHistory%>">
                        <input type="hidden" name="conf_supervisor" value="<%=confirmSupervisor%>">
                        <input type="hidden" name="sales_name" value="<%=salesName%>">
                        <input type="hidden" name="warehouseSales" value="<%=warehouseSales%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_INVOICE_NUMBER]%>" value="<%=billMain.getInvoiceNumber()%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_APP_USER_ID]%>" value="<%=userId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashcashierId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>" value="<%=transStatus%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>" value="<%=oidBillMain%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]%>" value="<%=docType%>">
                        <!--input type="hidden" name="<//%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>" value="<//%=rate%>"-->
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=salesCode%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]%>" value="<%=typeSalesOrder%>">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td class="mainheader"><center><h1><%=typeSalesOrder==1?"Incoming Sales Order by":"Sales Order by"%> <%=salesCode==""? userName : salesName%></h1></center></td>
                            </tr>
                            <tr>
                                <td><!-- #BeginEditable "content" -->
                                    <table width="100%" cellspacing="1" cellpadding="1">
                                        <tr>
                                            <td>
                                                <fieldset>
                                                    <legend >
                                                        <span class="style1">Main Data</span>
                                                    </legend>
                                                    <table id="table" align="center" width="98%" cellspacing="1" cellpadding="1">
                                                        <tr>
                                                            <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                    <tr>
                                                                        <td width="37%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                                                                                                            </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                        <td><%=(billMain.getInvoiceNumber() == null ? "<b>- Otomatis -</b>" : billMain.getInvoiceNumber())%></td>
                                                                        <!--billMain.getInvoiceNumber()-->
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                                        <td><%      Vector val_saletype = new Vector(1, 1);
                                                                                    Vector key_saletype = new Vector(1, 1);

                                                                                    if (codeCustomer == 3 && salesCode =="") {
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
                                                                                        key_saletype.add("Cash");
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CREDIT);
                                                                                        key_saletype.add("Kredit");
                                                                                    } else {
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
                                                                                        key_saletype.add("Cash");
                                                                                    }
                                                                                    String select_saletype = "" + idSaleType;
                                                                            %>
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE], null, (billMain.getTransctionType() == 0) ? select_saletype : "" + billMain.getTransctionType(), val_saletype, key_saletype, "onChange=\"javascript:cmdChangeMember()\"", "formElemen")%></td>
                                                                    </tr>
                                                                   <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][6]%></td>
                                                                        <td><%=ControlDate.drawDateWithStyle(FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_DATE], (billMain.getBillDate() == null) ? new Date() : billMain.getBillDate(), 0, -1, "formElemen", "disabled")%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td></td>
                                                                        <td><%=ControlDate.drawTimeSec(FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_DATE], (billMain.getBillDate() == null) ? new Date() : billMain.getBillDate(),"formElemen","disabled")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][15]%></td>

                                                                        <td><%=salesCode==""? userName : salesName%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][4]%></td> <!--standart rate-->
                                                                        <td>Rp. <%=Formater.formatNumber(PstStandartRate.getStandardRate(), "#,###.##")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                        <td><%
                                                                                    Vector listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");
                                                                                    Vector vectCurrVal = new Vector(1, 1);
                                                                                    Vector vectCurrKey = new Vector(1, 1);
                                                                                    for (int i = 0; i < listCurr.size(); i++) {
                                                                                        CurrencyType currencyType = (CurrencyType) listCurr.get(i);
                                                                                        vectCurrKey.add(currencyType.getCode());
                                                                                        vectCurrVal.add("" + currencyType.getOID());
                                                                                    }
                                                                                    String select_cur = ""; 
                                                                                    select_cur = "" + currencyId;
                                                                                %>
                                                                                <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID], "formElemen", null, ("" + billMain.getCurrencyId() == "") ? select_cur : "" + billMain.getCurrencyId(), vectCurrVal, vectCurrKey, "onChange=\"javascript:showData(this.value)\"")%>

                                                                                at <%-- <%=FRMHandler.userFormatStringDecimal(sellingRate)%>/USD --%>Rp. 
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>" type="hidden" size="12" value="<%=(billMain.getOID()==0) ? sellingRate : billMain.getRate()%>">
                                                                                <input name="rate" type="text" disabled="true" size="12" value="<%=(billMain.getOID()==0) ? sellingRate : billMain.getRate()%>">
                                                                                </td>
                                                                    </tr>
                                                                     <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][26]%></td>
                                                                        <td><%=warehouseSales%></td>
                                                                    </tr>
                                                                </table></td>
                                                            <td></td>
                                                            <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2" >
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][7]%></td>
                                                                        <td><label>
                                                                                <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>" cols="32" rows="2" id="shAddrs" tabindex="5" onKeyDown="keybrdPressHeader(this, event, this.value)"><%=(billMain.getShippingAddress() == "") ?  memberReg.getBussAddress() : billMain.getShippingAddress()%></textarea>
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
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>" type="text" id="shcity" size="12" tabindex="6"  value="<%=(billMain.getShippingCity() == "") ? memberReg.getTown() : billMain.getShippingCity()%>" onKeyDown="keybrdPressHeader(this, event, this.value)">,</td>
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>" type="text" id="shprov" size="12" tabindex="7" value="<%=(billMain.getShippingProvince() == "") ? memberReg.getProvince() : billMain.getShippingProvince()%>" onKeyDown="keybrdPressHeader(this, event, this.value)">,</td>
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>" type="text" id="shcountry" size="12" tabindex="8" value="<%=(billMain.getShippingCountry() == "") ? memberReg.getCountry() : billMain.getShippingCountry()%>" onKeyDown="keybrdPressHeader(this, event, this.value)"></td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>" type="text" id="shtlp" size="15" tabindex="9" value="<%=(billMain.getShippingPhoneNumber() == "") ? memberReg.getTelpNr() : billMain.getShippingPhoneNumber()%>" onKeyDown="keybrdPressHeader(this, event, this.value)">

                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][22]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>" type="text" id="shfax" size="15" tabindex="10" value="<%=(billMain.getShippingFax() == "") ? memberReg.getFax() : billMain.getShippingFax()%>" onKeyDown="keybrdPressHeader(this, event, this.value)">

                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>" type="text" id="shzip" size="10" maxlength="10" tabindex="11" value="<%=(billMain.getShippingZipCode() == "") ? memberReg.getPostalCode() : billMain.getShippingZipCode()%>" onKeyDown="keybrdPressHeader(this, event, this.value)"></td>
                                                                    </tr>
                                                                </table></td>
                                                            <td></td>
                                                            <td colspan="2" rowspan="5" valign="top">
                                                           
                                                                <table width="107%" border="0" cellspacing="2" cellpadding="2">
                                                                    <tr>
                                                                        <td width="26%"><%=textMaterialHeader[SESS_LANGUAGE][10]%></td>
                                                                        <td width="74%"><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>" type="hidden" value="<%=custId==0?idCust:custId%>" size="15" maxlength="64" >
                                                                                <%
                                                                                    //mencari credit limit
                                                                                    double totalKredit = 0;
                                                                                    double totalRetur = 0;

                                                                                    totalKredit = PstBillMain.getTotalKredit(custId);
                                                                                    totalRetur = PstBillMain.getReturnKredit(custId);

                                                                                    outstanding = totalKredit - totalRetur;

                                                                                    creditLimit = memberReg.getMemberCreditLimit();
                                                                                    available = creditLimit - outstanding;
                                                                                %>
                                                                                <input name="compName" type="text" value="<%= compName ==""? memberReg.getCompName():compName %>" id="namecomp" size="15" maxlength="64" tabindex="12" onKeyDown="keybrdPressHeader(this, event, this.value)">
                                                                                
                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                        <td><!--input name="compAddr" type="text" value="<--%= address == "" ? compAddress : address%>" size="15" maxlength="64" tabindex="13" class="hiddenLabel"-->
                                                                            <label >
                                                                                <textarea name="compAddr" cols="17" rows="1" tabindex="13" class="hiddenLabel" onKeyDown="keybrdPressHeader(this, event, this.value)"><%= address == "" ? compAddress : address%></textarea>
                                                                            </label>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" type="text" value="<%=custName == ""?billMain.getGuestName():custName %>" id="nameperson" size="15" maxlength="64" tabindex="14" onKeyDown="keybrdPressHeader(this, event, this.value)"></td>
                                                                    </tr>
                                                                    <input name="creditlimit" type="hidden" value="<%=creditLimit == 0 ? 0 : (creditLimit)%>">
                                                                    <input name="outstanding" type="hidden" value="<%=outstanding == 0 ? 0 : (outstanding)%>">
                                                                    <input name="available" type="hidden" value="<%=available == 0 ? 0 : (available)%>">
                                                                    <input name="availableTmp" type="hidden" value="<%=available%>" >
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][25]%></td>
                                                                        <td><label>
                                                                                <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>" cols="32" rows="2" onKeyDown="keybrdPressHeader(this, event, this.value)"><%=(notes == "") ? billMain.getNotes() : notes%></textarea>
                                                                            </label></td>
                                                                    </tr>
                                                                </table>
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
                                                            <td><span class="style3 style3"></span></td>
                                                            <td><span class="style3 style3"></span></td>
                                                            <td></td>
                                                            <td><span class="style3 style3"></span></td>
                                                            <td><span class="style3 style3"></span></td>
                                                            <td></td>
                                                            <td><span class="style3 style3"></span></td>
                                                            <td><span class="style3 style3"></span></td>
                                                        </tr>
                                                        <tr>
                                                            <td></td>
                                                            <td><table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                    <tr>
                                                                        <td ></td>
                                                                        <td >

                                                                            <% /*
                                                                                        if (transStatusTmp != 1 && idNotaType != PstBillMain.RETUR_ALL && idNotaType != PstBillMain.RETUR) {
                                                                                        } else {
                                                                                            if (iCommandDetail == Command.ADD) { */%>
                                                                                                <!--<input type="button" name="Button" value="Edit" onClick="javascript:cmdEditMain('<%=oidbillmain%>')"  class="styleButton">-->
                                                                                                <%//} else {
                                                                                                //if (codeCustomer == 3 && NotaSales == PstBillMain.NONE) {%>
                                                                                                <!--<input type="button" name="Button" value="Save" onClick="javascript:cmdApproval(0)"  class="styleButton">-->
                                                                                                <%//} else {%>
                                                                                                <!--<input type="button" name="Button" value="Save" onClick="javascript:cmdApproval(1)"  class="styleButton">-->
                                                                                                <%//}
                                                                                           // }
                                                                                       // }%>

                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </fieldset>
                                            </td>
                                        </tr>
                                        <tr>
                                            <%try {
                                            %>
                                            <td > <%= drawListBillDetailAdd(SESS_LANGUAGE, iCommandDetail, frmBillDetail, billdetail, listBillDetail, oidBillDetail, startDetail, idNotaType, oidBillMain)%> </td>
                                            <%
                                                        } catch (Exception e) {
                                                            System.out.println(e);
                                                        }
                                            %>
                                        </tr>
                                        <tr>
                                            <%try {
                                            %>
                                            <td > <%= drawListBillDetail(SESS_LANGUAGE, iCommandDetail, frmBillDetail, billdetail, listBillDetail, oidBillDetail, startDetail, idNotaType)%> </td>
                                            <%
                                                        } catch (Exception e) {
                                                            System.out.println(e);
                                                        }
                                            %>
                                        </tr>
                                        <tr>
                                            <td >
                                                <% if (iCommand == Command.ADD) {%>
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
                                                
                                                <table width="30%" align="right">
                                                    <tr>
                                                        <td widht="15%">
                                                            <span class="style5">
                                                             <%if(privApprovalSalesOrder || salesCode != "") {%>
                                                                <%=textMaterialHeader[SESS_LANGUAGE][19]%>
                                                             <%}%>
                                                            </span>
                                                        </td>
                                                        <td align="right" colspan="2" widht="20%">
                                                            <input size="34" name="subtotal" align="right" type="text" value="<%=FRMHandler.userFormatStringDecimal(amounttotal)%>" class="style5"  readonly>
                                                        </td>
                                                    </tr>
                                                    <%if(!privApprovalSalesOrder && salesCode =="") {%>
                                                    <tr>
                                                        <td widht="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][23]%></span>
                                                        </td>
                                                        <td width="5%" align="left">
                                                            <%

                                                                        Vector val_disctype = new Vector(1, 1);
                                                                        Vector key_disctype = new Vector(1, 1);

                                                                        val_disctype.add("" + PstBillMain.DISC_TYPE_PCT);
                                                                        key_disctype.add(" % ");
                                                                        val_disctype.add("" + PstBillMain.DISC_TYPE_VALUE);
                                                                        key_disctype.add(" val ");

                                                                        String disc_type = "" + DiscType;

                                                            %>
                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE], null, (billMain.getDiscType() == 3) ? disc_type : "" + billMain.getDiscType(), val_disctype, key_disctype, "onChange =\"cmdChangeMember()\"", "formElemen")%>

                                                        </td>
                                                        <td align="left" width="15%">
                                                            <%if (DiscType == PstBillMain.DISC_TYPE_VALUE) {%>
                                                            <input name="disc_global" tabindex="29" align="right" type="text" onkeyup="javascript:calculateTotal()" onkeypress="javascript:keybrdPressTotal(this, event)" value="<%=(billMain.getDiscount() == 0) ? discTmp : billMain.getDiscount()%>" class="style5">
                                                            <%} else {%>
                                                            <input name="disc_global" tabindex="29" align="right" type="text" onkeyup="javascript:calculateTotal()" onkeypress="javascript:keybrdPressTotal(this, event)" value="<%=(billMain.getDiscPct() == 0) ? discPctTmp : billMain.getDiscPct()%>" class="style5">
                                                            <%}%>
                                                            <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]%>" tabindex="29" align="right" type="hidden" value="<%=(billMain.getDiscount() == 0) ? discTmp : billMain.getDiscount()%>" class="style5">
                                                            <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]%>" tabindex="29" align="right" type="hidden" value="<%=(billMain.getDiscPct() == 0) ? discPctTmp : billMain.getDiscPct()%>" class="style5">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td widht="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][24]%></span>
                                                        </td>
                                                        <td align="left" width="5%" colspan="2">
                                                            <input id="codetax" size="2" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>" onkeyup="javascript:calculateTotal()" onkeypress="javascript:keybrdPressTotal(this, event)" tabindex="30" align="right" type="text" value="<%=(billMain.getTaxPercentage() == 0) ? taxPctTmp : (billMain.getTaxPercentage())%>" class="style5"> %
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][19]%></span>
                                                        </td>
                                                        <td align="left" colspan="2" width="20%">
                                                            <input size="34" name="grandtotal" align="right" type="text" value="<%=(grandtotalTmp == 0) ? FRMHandler.userFormatStringDecimal(amounttotal) : FRMHandler.userFormatStringDecimal(grandtotalTmp)%>" class="style5"  readonly>
                                                            <input size="34" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>" align="right" type="hidden" value="<%=(grandtotalTmp == 0) ? amounttotal : grandtotalTmp%>" class="style5"  readonly>
                                                            <input size="34" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>" align="right" type="hidden" value="<%= billMain.getTaxValue()%>" class="style5"  readonly>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td width="15%" colspan="3" align="left">
                                                            <!--input type="button" name="Button" tabindex="31" value="Save Sales Order" onClick="javascript:cmdSaveAll()"  class="styleButton"-->
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <%if (iCommandTmp == Command.APPROVE) {%>
                                                <fieldset>
                                                    <legend>Approve by supervisor : </legend>
                                                    <br>
                                                    <p>Username
                                                        <input type="text" name="login_id" size="20" onKeyDown="keybrdPress(this, event, this.value)">
                                                        Password
                                                        <input type="password" name="pass_wd" size="15" onKeyPress="keybrdPress(this, event, this.value)">
                                                    </p>
                                                    <p>
                                                        <label>
                                                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                                                <tr>
                                                                    <td ></td>
                                                                    <td ><input id="aLogin" type="button" name="Button" value="Submit" onClick="javascript:cmdApprovalSupervisor()" class="formElemen">
                                                                    </td>
                                                                    <td >&nbsp;</td>
                                                                    <td ></td>
                                                                </tr>
                                                            </table>
                                                        </label>
                                                    </p>

                                                </fieldset>
                                                <%}%>
                                                <%if ((iCmd == CMD_APPROVAL) && (approvalStatus > 1)) {%>
                                                <font class="errfont" color="#FF0000"><%if (appLanguage == com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {%>nama atau password salah<%} else {%>
                                                    username or password wrong, try again...
                                                    <%}%></font>
                                                    <%}%>
                                            </td>
                                        </tr>
                                        <tr valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <!--
                                                <table width="17%" align="left" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <td ></td>
                                                        <td ><%//if (transStatusTmp != 1 && idNotaType != PstBillMain.RETUR_ALL && idNotaType != PstBillMain.RETUR) {%>
                                                            <input type="button" name="Button" value="Back to List" onClick="javascript:cmdBacktoList()" class="styleButton">
                                                                   <% //} else {
                                                                        //if (idNotaType != PstBillMain.RETUR_ALL) {%>
                                                            <input type="button" name="Button" value="Save Detail" onClick="javascript:cmdApproval2()"  class="styleButton">
                                                            <%//}%>
                                                        </td>
                                                        <td ><input type="button" name="Button" value="Add New" onClick="javascript:goNewPage()" class="styleButton"></td>
                                                        <td ><input type="button" name="Button Cancel" value="Cancel Bill" onClick="javascript:cancelmain()" class="styleButton"></td>
                                                        <td ><input type="button" name="Button" value="Back to List" onClick="javascript:cmdBack()" class="styleButton"></td>
                                                            <%//}%>
                                                        <td ><input type="button" name="Button" value="Print" onClick="javascript:printFormHtml()" class="styleButton"></td>
                                                        <td ></td>
                                                    </tr>
                                                </table>
                                                -->
                                                
                                            </td>
                                        </tr>
                                         <tr>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                             <td>
                                                 <fieldset>
                                                    <legend >
                                                        <h2>Kunci Bantuan</h2>
                                                    </legend>
                                                     <h3>F9 = Save&nbsp&nbsp;F2 = Pencarian Customer &nbsp;&nbsp; F1 = Tambah Baru</h3>
                                                  </fieldset>
                                             </td>
                                        </tr>
                                        <tr>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>


                                            </td>
                                        </tr>

                                    </table>
                                    <!-- #EndEditable --></td>
                            </tr>
                        </table>
                    </form>
                    <script language="JavaScript">
                                 document.frmcashier.compName.focus();
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>
    <% if (iCommandTmp == Command.EDIT) {%>
    <script type="text/javascript">
                alert("Item is already exist !");
                document.frmcashier.commandTmp.value =0;
    </script>
    <%}%>

    <% if ((iCmd == CMD_APPROVAL) && (approvalStatus == 1) && iErrCode == FRMMessage.NONE) {%>
    <script type="text/javascript">
                var codesupervisor = document.frmcashier.conf_supervisor.value;
                if(codesupervisor>0){
                    window.open("sales_order_print.jsp?hidden_bill_main_id=<%=oidbillmain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                    cmdAddNew();
                }else{
                    cancelmain();
                }
                

    </script>
    <%}%>

    <%if (iCommandDetail == Command.ADD) {%>
    <script type="text/javascript">
                document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.focus();

                document.getElementById('shAddrs').disabled='disabled';
                document.getElementById('shcity').disabled='disabled';
                document.getElementById('shprov').disabled='disabled';
                document.getElementById('shcountry').disabled='disabled';
                document.getElementById('shtlp').disabled='disabled';
                document.getElementById('shfax').disabled='disabled';
                document.getElementById('shzip').disabled='disabled';

                var codeCustomer = document.frmcashier.code_cust.value;

                if(codeCustomer == 3){
                    document.getElementById('nameperson').disabled='disabled';
                    document.getElementById('namecomp').disabled='disabled';
                }



    </script>
    <%}%>

    <%if (iCommandTmp == Command.GOTO) {%>
    <script type="text/javascript">
                cmdAddNew();
                document.frmcashier.command.value="<%=Command.BACK%>";
                document.frmcashier.action="salesorder_list.jsp";
                document.frmcashier.submit();
    </script>
    <%} else if (iCommandTmp == Command.RESET) {%>
    <script type="text/javascript">
                cmdAddNew();

    </script>
    <%}%>
    <%if (iCommandTmp == Command.CONFIRM) {%>
    <script type="text/javascript">
                var code = document.frmcashier.allTotal.value;
                if(code == 1){
                    var tmpcon = "";
                    var con = confirm("Print invoice ? ");
                    if (con ==true)
                    {
                        printFormHtml();
                        //document.frmcashier.print_history.value="1";
                        tmpcon= confirm("Open new sales order ?");
                        if (tmpcon ==true)
                        {
                            cmdAddNew();
                        }
                    }else{
                        tmpcon= confirm("Open new sales order ?");
                        if (tmpcon ==true)
                        {
                            cmdAddNew();
                        }
                    }
                }
    </script>
    <%}%>
    <!-- #EndTemplate -->
</html>
