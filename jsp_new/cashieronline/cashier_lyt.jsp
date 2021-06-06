<%@page import="com.dimata.harisma.entity.masterdata.PstPosition"%>
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
         com.dimata.posbo.entity.admin.*,
         com.dimata.posbo.entity.warehouse.*"%>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_LOGIN, AppObjInfo.G2_LOGIN, AppObjInfo.OBJ_LOGIN_LOGIN); %>
<%@include file = "../main/checkuser.jsp" %>
<!-- JSP Block -->
<%!
    //final static int CMD_NONE =0;
    final static int CMD_APPROVAL = 1;
%>
<%
        int appObjCodeKasirInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_INVOICE); 
        int appObjCodeKasirCreditInvoice = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_CREDIT_INVOICE);
        int appObjCodeKasirRetur = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_RETUR);
        //int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT) ;

%>
<!--%@ include file = "../main/checkuser.jsp" %-->
<%
        boolean privApprovalKasirInv = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirInvoice, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirKreditInv = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirCreditInvoice, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirRetur= userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirRetur, AppObjInfo.COMMAND_VIEW));
        boolean privApprovalKasirPay = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));
%>
<%!   /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Currency", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer", //10
            "Person", "Credit Limit", "Outstanding", "Available", "Sales Invoicing", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax","Notes","Sales Order","Tanggal Jatuh Tempo"}, //11
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Curreny", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales Invoicing", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax","Notes","Sales Order","Due Date Payment"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",//8
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit","Gudang"},//8
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit","Warehouse"}
    };

    public String drawListBillDetailAdd(int language, int iCommandDetail, FrmBillDetail frmObject,
        Billdetail objEntity, Vector objectClass, long billdetailId, int start, int idNotaType, long oidBillMain,Vector vectLoc, boolean privSalesInvoice) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.dataFormat(textListOrderItem[language][0], "3%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][1], "13%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][2], "26%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][3], "5%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][17], "5%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][4], "8%","0","0","center","center");
       
        ctrlist.dataFormat(textListOrderItem[language][6], "4%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][7], "4%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][8], "8%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][9], "8%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][10], "8%","0","0","center","center");
        ctrlist.dataFormat(textListOrderItem[language][11], "8%","0","0","center","center");

       

        Vector lstData = ctrlist.getData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        ctrlist.setLinkRow(1);
        int index = -1;
        if (start < 0) {
            start = 0;
        }
        int no = 0;
        rowx = new Vector();
        if (iCommandDetail == Command.ADD || (iCommandDetail == Command.SAVE && frmObject.errorSize() > 0)) {
            no = no + 1;
            rowx.add(String.valueOf(no));
            if (idNotaType == PstBillMain.RETUR || idNotaType == PstBillMain.RETUR_ALL || privSalesInvoice) {
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"\">"
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"\">"
                        + "<input tabindex=\"18\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemen\"><a tabindex=\"19\" href=\"javascript:cmdCheckRetur()\">&nbsp;</a>");
                rowx.add("<input type=\"text\" tabindex=\"20\" size=\"20\" id=\"inputItemName\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyPress=\"javascript:keybrdPress(this, event)\"><a href=\"javascript:cmdCheckRetur()\">&nbsp;</a><input type=\"button\" id=\"btn\" name=\"btnchk\" value=\"CHK\" onClick=\"javascript:cmdCHK()\">");
            } else {
                rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"\">"
                        + "<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"\">"
                        + "<input tabindex=\"18\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemen\"><a tabindex=\"19\" href=\"javascript:cmdCheck()\">&nbsp;</a>");
                rowx.add("b<input type=\"text\" size=\"20\" tabindex=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + "" + "\" class=\"formElemen\" onKeyPress=\"javascript:keybrdPress(this, event)\"><a href=\"javascript:cmdCheck()\">&nbsp;</a>");
            }
            rowx.add("<div align=\"right\"><input tabindex=\"21\" type=\"text\" size=\"3\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"\" class=\"formElemen\" onkeyup=\"javascript:calculate()\" onKeyPress=\"javascript:keybrdPress(this, event)\"  style=\"text-align:right\"></div>");
            rowx.add("<input tabindex=\"\" type=\"text\" size=\"3\"  name=\"matUnit\" value=\"\" class=\"hiddenTextR\"  class=\"formElemenR\">");
            rowx.add("<input type=\"text\" tabindex=\"22\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"\" class=\"hiddenTextR\" onKeyPress=\"javascript:keybrdPress(this, event)\" readOnly>");
            rowx.add("<input type=\"text\" tabindex=\"23\" size=\"3\" id=\"disc1\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\"  value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"24\" type=\"text\" size=\"3\" id=\"disc2\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"25\" type=\"text\" size=\"8\" id=\"disc\" onkeyup=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"26\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_DISC] + "\" value=\"\" class=\"hiddenTextR\"  class=\"formElemenR\">");
            rowx.add("<input tabindex=\"27\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"\" class=\"hiddenTextR\"  onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\">");
            rowx.add("<input tabindex=\"28\" type=\"text\" size=\"8\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\">");

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

            double qty = PstBillDetail.getCountStock(billdetail.getBillMainId(), billdetail.getMaterialId());
            double disc1 = (billdetail.getItemPrice()) * (billdetail.getDisc1()) / 100;
            double discPrice1 = (billdetail.getItemPrice()) - disc1;
            double disc2 = discPrice1 * (billdetail.getDisc2()) / 100;
            double discPrice2 = discPrice1 - disc2;
            double netPrice = discPrice2 - billdetail.getDisc();
            double totalDisc = (billdetail.getItemPrice()) - netPrice;
            double totalPrice = qty * netPrice;

            if(qty > 0){

            if (billdetailId == billdetail.getOID()) {
                index = i;
            }
            if (index == i && (iCommandDetail == Command.EDIT || iCommandDetail == Command.ASK)) {
                rowx.add("" + start);
                if (idNotaType == PstBillMain.RETUR || idNotaType == PstBillMain.RETUR_ALL) {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + (billdetail.getMaterialId())
                            + "\">"
                            + "<input  type=\"hidden\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + billdetail.getUnitId() + "\">"
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\"><a href=\"javascript:cmdCheckRetur()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                } else {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + (billdetail.getMaterialId())
                            + "\">"
                            + "<input  type=\"hidden\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + billdetail.getUnitId() + "\">"
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\"><a href=\"javascript:cmdCheck()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                }
                rowx.add("<input tabindex=\"32\" type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + billdetail.getItemName() + "\" class=\"formElemenR\" readOnly>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"test\" onKeyPress=\"javascript:keybrdPress(this, event)\"  class=\"formElemenR\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"formElemenR\" style=\"text-align:right\" readOnly></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"34\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_PRICE] + "\" value=\"" + (billdetail.getItemPrice()) + "\" class=\"formElemenR\"  readOnly></div>");
                rowx.add("<input tabindex=\"35\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_1] + "\" value=\"" + (billdetail.getDisc1()) + "\" onKeyPress=\"javascript:keybrdPress(this, event)\"  class=\"formElemenR\" >");
                rowx.add("<input tabindex=\"36\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC_2] + "\" value=\"" + (billdetail.getDisc2()) + "\" onKeyPress=\"javascript:keybrdPress(this, event)\"  class=\"formElemenR\">");
                rowx.add("<input tabindex=\"37\" type=\"text\" size=\"8\" onKeyUp=\"javascript:calculate()\"  name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_DISC] + "\" value=\"" + (billdetail.getDisc()) + "\" onKeyPress=\"javascript:keybrdPress(this, event)\"  class=\"formElemenR\">");
                rowx.add("<input tabindex=\"38\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_DISC] + "\" value=\"" + totalDisc + "\" class=\"formElemenR\" readOnly>");
                rowx.add("<div align=\"right\"><input tabindex=\"39\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_TOTAL_PRICE] + "\" value=\"" + totalPrice + "\" class=\"formElemenR\"  readOnly></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"40\" type=\"text\" size=\"8\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_NOTE] + "\" value=\"" + billdetail.getNote() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\"   class=\"formElemenR\"  ></div>");

            } else {
                rowx.add("" + start + "");
                rowx.add("<a href=\"javascript:billDetailEdit('" + String.valueOf(billdetail.getOID()) + "')\">" + billdetail.getSku() + "</a>");
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
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalDisc) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(netPrice) + "</div>");
                rowx.add("<div align=\"right\">" + (billdetail.getNote() == null ? "" : billdetail.getNote()) + "</div>");
            }
            lstData.add(rowx);
        }
            }
        return ctrlist.draw();
        
    }

%>

<%
            int iCommand = FRMQueryString.requestCommand(request);
            int iCommandDetail = FRMQueryString.requestInt(request, "commandDetail");
            int iCommandPayment = FRMQueryString.requestInt(request, "commandPayment");
            int iCommandTmp = FRMQueryString.requestInt(request, "commandTmp");

            int start = FRMQueryString.requestInt(request, "start"); 
            int startDetail = FRMQueryString.requestInt(request, "start_detail");
            int startPayment = FRMQueryString.requestInt(request, "start_payment");

            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidBillMain = FRMQueryString.requestLong(request, "hidden_bill_main_id");//for the first time, it have data

            long idCurrency = FRMQueryString.requestLong(request, "CurrId");
            long idPriceType = FRMQueryString.requestLong(request, "PriceTypeId"); // there is data
            long idStandartRate = FRMQueryString.requestLong(request, "StandartRateId");
            double amounttotal = FRMQueryString.requestDouble(request, "total_amount");
            
            int idNotaType = FRMQueryString.requestInt(request, "nota_type"); // 1
            long idbilldetailTmp = FRMQueryString.requestLong(request, "billdetailTmp");
            int idTransType = FRMQueryString.requestInt(request, "trans_type");
            double stockReturn = FRMQueryString.requestDouble(request, "stock_return");
            long oidbillmain = FRMQueryString.requestLong(request, "oidbillmaintmp");
            long oidPayment = FRMQueryString.requestLong(request, "oidPayment");

            double creditLimit = FRMQueryString.requestDouble(request, "creditlimit"); // 1.0
            double outstanding = FRMQueryString.requestDouble(request, "outstanding"); // there is data
            double available = FRMQueryString.requestDouble(request, "available"); // double data
            int NotaSales = FRMQueryString.requestInt(request, "notasalestype"); //1
            int NotatypeTmp = FRMQueryString.requestInt(request, "notasalestypeTmp");
            int codeCustomer = FRMQueryString.requestInt(request, "code_cust"); //3

            int typeCashier = FRMQueryString.requestInt(request, "typeCashier"); //1

            String compAddress = FRMQueryString.requestString(request, "compAddr"); // Rimo
            int tmpsalesorder = FRMQueryString.requestInt(request, "allTotal");
            //String salesName = FRMQueryString.requestString(request, "SalesName");
            String compName = FRMQueryString.requestString(request, "compName");//Elisya 2
            
            int ApprovalKasirPay = FRMQueryString.requestInt(request, "ApprovalKasirPay");//1
            
            if(ApprovalKasirPay==1){
                privApprovalKasirPay=false;
            }
            
            double sellingRate = 0;
            double rate = 0 ; 
            int transStatus = 1;
            int docType = 0;
            boolean privSalesInvoice = true;
            if(privApprovalKasirPay){
                privSalesInvoice = false;
                transStatus = 0;
            }

            if (iCommandPayment == Command.CANCEL) {
                docType = 0;
                transStatus = 2;
                iCommandPayment = Command.SAVE;
            }

            if (iCommandTmp == Command.CANCEL) {
                oidbillmain = oidBillMain;
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
            
            int idSaleType = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]);
            String shippAddr = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]);
            String shippCity = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]);
            String shippProv = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]);
            String shippCountry = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]);
            String shippTlp = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]);
            String shippFax = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]);
            String shippZip = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]);

            long currencyId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID]);

            double amountTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]);
            double taxPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]);
            double taxValueTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]);
            double discTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]);
            double discPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]);
            int DiscType = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]);

            String custName = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]);
            
            long custId = FRMQueryString.requestLong(request, "customerId");
            String notes = FRMQueryString.requestString(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]);
            int typeSalesOrder = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TYPE_SALES_ORDER]);
            
            int vectSize = PstBillMain.getCount(whereClause);
            BillMain billMain = ctrlBillMain.getBillMain();
            msgString = ctrlBillMain.getMessage();

            Vector vectLoc = PstLocation.list(0,0,"","");

            if (iCommand == Command.EDIT || iCommand == Command.DELETE) {
                iErrCode = ctrlBillMain.action(iCommand, oidbillmain, custId);
                billMain = ctrlBillMain.getBillMain();
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                custId = billMain.getCustomerId();
                idTransType = billMain.getTransctionType();
                iCommand = Command.ADD;
            }
            
            if (iCommand == Command.SAVE) {
                iErrCode = ctrlBillMain.action(iCommand, oidbillmain, custId);
                billMain = ctrlBillMain.getBillMain();
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                custId = billMain.getCustomerId();
                idTransType = billMain.getTransctionType();
                iCommand = 0;
                iCommandDetail = Command.ADD;
            }
            
            //Mencari Price Type Id
            long idCustTmp = 0;
            Vector listCodeCustomerTmp = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='1'", "");
            MemberGroup memberGroupCodeTmp = (MemberGroup) listCodeCustomerTmp.get(0);
            if (custId == memberGroupCodeTmp.getOID()) {
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

            //Mencari selling Rate
            if(billMain.getOID()!=0){
                Vector listSellingRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='2'", "");
                if (listSellingRate.size() != 0) {
                    StandartRate standartRate = (StandartRate) listSellingRate.get(0);
                    sellingRate = standartRate.getSellingRate();
                }
            }else{
                sellingRate = 1.0;//billMain.getRate();
            }
            

            //Untuk Bill Detail
            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            ctrlBillDetail.setLanguage(SESS_LANGUAGE);

            iErrCode = ctrlBillDetail.action(iCommandDetail, oidBillDetail, oidbillmain);
            //amounttotal = PstBillDetail.getTotalAmount(oidbillmain);

            FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();
            //int DiscType = FRMQueryString.requestInt(request, frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC_TYPE]);

            Billdetail billdetail = ctrlBillDetail.getBillDetail();
            msgString = ctrlBillDetail.getMessage();

            //Mencari Shift Id, cascashier id, location id
            long shiftId = 0;
            long cashcashierId = 0;
            long cashmasterId = 0;
            long locationId = 0;
            double taxValue = 0.00;
            double taxPct = 0.00;
            int taxCode = 0;
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

            /*switch list BillMain*/
            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                start = ctrlBillMain.actionList(iCommand, start, vectSize, recordToGet);
            }
            /* end switch list*/
            /* get record to display */
            if (oidbillmain != 0) {
                whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidbillmain + "'";
            } else {
                whereClause = PstBillMain.fieldNames[PstBillMain.FLD_BILL_MAIN_ID] + " ='" + oidBillMain + "'";
            }
            listBillMain = PstBillMain.list(start, recordToGet, whereClause, orderClause);

            String salesOrderCode="";
            long salesWarehouseLoc=0;
            if (listBillMain.size() > 0) {
                billMain = (BillMain) listBillMain.get(0);
                //cek sales person warehouse
                salesOrderCode=salesName;
                salesWarehouseLoc=PstLocation.getLocationByUserId(billMain.getAppUserSalesId());
//                salesWarehouseLoc=PstLocation.getLocationBySalesCode(billMain.getSalesCode());
                if(salesWarehouseLoc==0){
                    salesWarehouseLoc=billMain.getLocationId();
                }
                custId = billMain.getCustomerId();
            }

            /**Untuk Bill Detail*/
            String whereClauseDetail = "";
            String orderClauseDetail = "";
            int recordToGetDetail = 1000;
            Vector listBillDetail = new Vector();
            int disctype = 0;

            if ((idNotaType == PstBillMain.OPEN_BILL || idNotaType == PstBillMain.OPEN_CREDIT_INVOICE || idNotaType == PstBillMain.OPEN_ALL_INVOICE || idNotaType == PstBillMain.RETUR_ALL )&& privSalesInvoice == false) {
                disctype = billMain.getDiscType();
                amounttotal = PstBillDetail.getTotalAmount(oidBillMain);
                double disc = 0;
                if (disctype == PstBillMain.DISC_TYPE_PCT) {
                    disc = amounttotal * (billMain.getDiscPct()) / 100;
                } else {
                    disc = billMain.getDiscount();
                }
                double amountAftDisc = amounttotal - disc;
                double tax = amountAftDisc * (billMain.getTaxPercentage()) / 100;
                double amountAftTax = amountAftDisc + tax;
                amountTmp = amountAftTax;

                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidBillMain;

            } else if (idNotaType == PstBillMain.RETUR || privSalesInvoice == true) {
                disctype = billMain.getDiscType();
                amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
                double disc = 0;
                if (disctype == PstBillMain.DISC_TYPE_PCT) {
                    disc = amounttotal * (billMain.getDiscPct()) / 100;
                } else {
                    disc = billMain.getDiscount();
                }
                double amountAftDisc = amounttotal - disc;
                double tax = amountAftDisc * (billMain.getTaxPercentage()) / 100;
                double amountAftTax = amountAftDisc + tax;
                amountTmp = amountAftTax;
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;

           /*}  else if (idNotaType == PstBillMain.INVOICING) {
                DiscType = billMain.getDiscType();
                amounttotal = PstBillDetail.getTotalAmount(oidBillMain);
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;*/

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
                //idNotaType = 1;
            }

            //update grand total amount
            if (iCommand == Command.GOTO) {
                double grandtotal = PstBillMain.updateGrandTotal(oidbillmain, amountTmp, taxPctTmp, taxValueTmp, discTmp, discPctTmp, DiscType);
                iCommandPayment = Command.GOTO;

            }
           
            if (iCommand == Command.GOTO && idNotaType == PstBillMain.VOID) {
                int batal = PstBillMain.updateTransStatus(oidbillmain,2);//batal
            }

            //Untuk Cash Payment
            double change = 0;
            double payAmount = 0;
            CtrlCashPayment ctrlCashPayment = new CtrlCashPayment(request);
            ctrlCashPayment.setLanguage(SESS_LANGUAGE);

            FrmCashPayment frmCashPayment = ctrlCashPayment.getForm();
            
            if (idNotaType == PstBillMain.OPEN_BILL && privApprovalKasirPay) {
                payAmount = FRMQueryString.requestDouble(request, frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]);
                change = FRMQueryString.requestDouble(request, frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_AMOUNT_RETURN]);
            }

            if (iCommandPayment == Command.SAVE && idNotaType == PstBillMain.OPEN_BILL && privApprovalKasirPay) {
                rate = FRMQueryString.requestDouble(request, frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_RATE]);
                double grandtotal = PstBillMain.updateGrandTotal(oidBillMain, amountTmp, taxPctTmp, taxValueTmp, discTmp, discPctTmp, DiscType);
                iErrCode = ctrlCashPayment.action(iCommandPayment, oidCashPayment, oidBillMain, transStatus);
                CashPayments1 cashpayment = ctrlCashPayment.getCashPayment();
                oidPayment = cashpayment.getOID();
                iCommandPayment = Command.GOTO;
            }

            /** Untuk Credit Payment **/
            CtrlCashCreditPaymentMain ctrlCashCreditPaymentMain = new CtrlCashCreditPaymentMain(request);
            ctrlCashCreditPaymentMain.setLanguage(SESS_LANGUAGE);

            if (iCommandPayment == Command.SAVE && idNotaType == PstBillMain.OPEN_CREDIT_INVOICE && privApprovalKasirPay) {
                rate = FRMQueryString.requestDouble(request, FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_RATE]);
                double grandtotal = PstBillMain.updateGrandTotal(oidBillMain, amountTmp, taxPctTmp, taxValueTmp, discTmp, discPctTmp, DiscType);
                iErrCode = ctrlCashCreditPaymentMain.action(iCommandPayment, oidCashPayment, oidBillMain, transStatus);
                CreditPaymentMain creditpayment = ctrlCashCreditPaymentMain.getCreditPaymentMain();
                oidPayment = creditpayment.getOID();
                iCommandPayment = Command.GOTO;
            }

            FrmCashCreditPaymentMain frmCashCreditPaymentMain = ctrlCashCreditPaymentMain.getForm();
            if (idNotaType == PstBillMain.OPEN_CREDIT_INVOICE && privApprovalKasirPay) {
                payAmount = FRMQueryString.requestDouble(request, frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]);
                change = FRMQueryString.requestDouble(request, frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_AMOUNT_RETURN_CREDIT]);
            }

            //Mencari PayType
            long payType = 0;
            Vector listPayType = PstPaymentSystem.list(0, 0, PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_PAYMENT_TYPE] + " ='1' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CARD_INFO] + " ='0' AND " + PstPaymentSystem.fieldNames[PstPaymentSystem.FLD_CHECK_BG_INFO] + " ='0'", "");
            for (int i = 0; i < listPayType.size(); i++) {
                PaymentSystem paymentSystem = (PaymentSystem) listPayType.get(i);
                payType = paymentSystem.getOID();
            }


            /** kondisi ini untuk manampilakn form tambah item. posisi pada baris program paling bawah */
            if (iCommandDetail == Command.SAVE && iErrCode == 0 || iCommandDetail == Command.DELETE) {
                oidBillDetail = 0;
                iCommandDetail = Command.ADD;
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

            //Mencari Standard Rate Id
            Vector listStandardRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='" + idCurrency + "'", "");
            for (int i = 0; i < listStandardRate.size(); i++) {
                StandartRate standartRate = (StandartRate) listStandardRate.get(i);
                idStandartRate = standartRate.getOID();
            }

            //Mencari Rate
            Vector lisRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='" + currencyId + "'", "");
            for (int i = 0; i < lisRate.size(); i++) {
                StandartRate standartRate = (StandartRate) lisRate.get(i);
                rate = standartRate.getSellingRate();
            }

            //member list
            MemberReg memberReg = new MemberReg();
            //long oidCustomer=0;
            //oidCustomer=custId==0?custId:billMain.getCustomerId();
            Vector listMemberReg = PstMemberReg.list(start, recordToGet, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " = '" +custId + "'", orderClause);
            if (listMemberReg.size() > 0) {
                memberReg = (MemberReg) listMemberReg.get(0);
            }
            //mencari credit limit
            double totalKredit = PstBillMain.getTotalKredit(billMain.getCustomerId());
            double totalRetur = PstBillMain.getReturnKredit(billMain.getCustomerId());
            outstanding = totalKredit - totalRetur;

            creditLimit = memberReg.getMemberCreditLimit();
            available = creditLimit - outstanding;

            String address = "";
            if(memberReg.getBussAddress()!= null && memberReg.getBussAddress().length() > 0){
                address = memberReg.getBussAddress();
            }else{
                address = memberReg.getHomeAddr();
            }

            //Date dueDatePaymen = new Date();
            Date dueDatePayment = new Date();
            if(memberReg.getDayOfPayment()>0){
                 dueDatePayment.setDate(dueDatePayment.getDate()+memberReg.getDayOfPayment());
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
        <link rel="stylesheet" href="../styles/main_cashierweb.css" type="text/css">
        <!-- #EndEditable -->
        <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../styles/tab_cashierweb.css" type="text/css">
        <!-- #EndEditable -->
        <%if(menuUsed == MENU_ICON){%>
            <link href="../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>
        <script src="../styles/jquery.min.js"></script>
        <script type="text/javascript">
             function checkAjaxOutlet(value){
                    var stateCheck =  document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_MATERIAL_ID]%>.value;
                   // alert(stateCheck);
                    $.ajax({
                        url : "<%=approot%>/servlet/com.dimata.posbo.ajax.checkStockOutletOrder?<%=checkStockOutletOrder.CHECK_QTY_ORDER%>="+value+"&<%=checkStockOutletOrder.LOCATION_SALES%>=<%=salesWarehouseLoc%>&<%=checkStockOutletOrder.TYPE_CHECK_STOCK_SALES%>=<%=typeSalesOrder%>&<%=checkStockOutletOrder.CHECK_MATERIAL_ID%>="+stateCheck,
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
        <!-- #BeginEditable "headerscript" -->
        <SCRIPT language=JavaScript>
            
            function cancelmain(){
                var transType = document.frmcashier.sale_type.value;
                var notaType = document.frmcashier.nota_type.value;
                document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]%>.value = 0;
                document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                if(transType == <%=PstBillMain.TRANS_TYPE_CASH%>){
                    cmdCancelOpenBill();
                }else{
                    document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                    cmdCancelBill();
                }
            }

            function cmdApprovalSupervisor(){
                document.frmcashier.commandTmp.value="<%=Command.SAVE%>";
                document.frmcashier.action = "cashier_lyt.jsp";
                document.frmcashier.submit();
            }

            function approvSupervisor(){
                var oidBillMain = document.frmcashier.hidden_bill_main_id.value;
                if(oidBillMain == 0){
                    cmdAddNew();
                }else{
                    document.frmcashier.commandTmp.value="<%=Command.APPROVE%>";
                    document.frmcashier.action = "cashier_lyt.jsp";
                    document.frmcashier.submit();
                }

            }

            function printFormHtml() {
                var oidbillmain = document.frmcashier.hidden_bill_main_id.value;
                var oidPayment = document.frmcashier.oidPayment.value;
                if(oidbillmain == 0 || oidPayment ==0){
                    alert("Plese Click Save Invoice Button First To Prosses");
                }else{
                    window.open("cashier_invoice_print.jsp?hidden_bill_main_id=<%=oidBillMain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                    cmdAddNew();
                }
               
            }

            function printFormHtmlRetur() {
                var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                if(oidbillmain == 0){
                    alert("Data is empty !");
                }else{
                    if(<%=!privApprovalKasirPay%>){
                        var salesType = document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE]%>.value;
                        var idNotaTmp =  document.frmcashier.notasalestype.value;
                        /*if(salesType == <%//=PstBillMain.TRANS_TYPE_CASH%>){
                            idNotaTmp = <%//=PstBillMain.OPEN_BILL%>;
                        }else{
                            idNotaTmp = <%//=PstBillMain.OPEN_CREDIT_INVOICE%>;
                        }*/
                        window.open("sales_order_print.jsp?hidden_bill_main_id=<%=oidbillmain%>&nota_type="+idNotaTmp+"&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                        
                    }else{
                       window.open("sales_order_print.jsp?hidden_bill_main_id=<%=oidbillmain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no"); 
                    }                    
                }            
                cmdAddNew();
            }

            function printFormInvoice() {
                var oidbillmain = document.frmcashier.hidden_bill_main_id.value;
                var oidPayment = document.frmcashier.oidPayment.value;
                var grandtotal = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                var pay = cleanNumberFloat(document.frmcashier.pay.value,guiDigitGroup,guiDecimalSymbol);

                if(oidbillmain == 0){
                    alert("Payment data is empty !");
                }else if(parseFloat(pay) < parseFloat(grandtotal)){
                    alert("Payment less than total cost !");
                }else{
                    window.open("cashier_invoice_print.jsp?hidden_bill_main_id=<%=oidBillMain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprintinvoice","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                }
                cmdAddNew();
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

            function keybrdPress(frmObj, event) {
                var notaType = document.frmcashier.nota_type.value;
                if(event.keyCode == 13) {
                    switch(frmObj.name) {
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>':
                                if(notaType == <%=PstBillMain.RETUR%> || notaType == <%=PstBillMain.RETUR_ALL%>){
                                   // alert("notaType 1"+notaType);
                                    cmdCheckRetur();
                                }else{
                                    //update opie-eyek 20130916 agar tetep muncul
                                    //cmdCheck();
                                    //alert("notaType 2"+notaType);
                                    if(notaType == <%=PstBillMain.NONE%>){
                                        cmdCheck();
                                    }else{
                                        cmdCheckRetur();
                                    }
                                    
                                }
                                break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>':
                                
                                if(notaType == <%=PstBillMain.RETUR%>){
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>.focus();
                            }else{
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.focus();
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
                    }
                }
                
                function cmdCHK() {
                var notaType = document.frmcashier.nota_type.value;
                var frmObj = document.getElementById("inputItemName").getAttribute("name");
                    switch(frmObj) {
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>':
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                            break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>':
                                if(notaType == <%=PstBillMain.RETUR%> || notaType == <%=PstBillMain.RETUR_ALL%>){
                                   // alert("notaType 1"+notaType);
                                    cmdCheckRetur();
                                }else{
                                    //update opie-eyek 20130916 agar tetep muncul
                                    //cmdCheck();
                                    //alert("notaType 2"+notaType);
                                    if(notaType == <%=PstBillMain.NONE%>){
                                        cmdCheck();
                                    }else{
                                        cmdCheckRetur();
                                    }
                                    
                                }
                                break;
                        case '<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>':
                                
                                if(notaType == <%=PstBillMain.RETUR%>){
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_NOTE]%>.focus();
                            }else{
                                document.all.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISC_1]%>.focus();
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
                    
                }

                function keybrdPressTotal(frmObj, event) {
                    if(event.keyCode == 13) {
                        switch(frmObj.name) {
                            case 'subtotal':
                                document.all.disc_global.focus();
                                break;
                            case 'disc_global':
                                document.all.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.focus();
                                break;
                            case '<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>':
                                    if(<%=!privApprovalKasirPay%>){
                                        document.all.ButtonSavePayment.focus();
                                    }else{
                                        document.all.pay.focus();
                                    }
                                    
                                break;
                            case 'pay':
                                calculatePayment();
                                document.all.change.focus();
                                break;

                            case 'change':
                                document.all.ButtonSavePayment.focus();
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
                        document.frmcashier.hidden_bill_main_id.value = 0;
                        document.frmcashier.customerId.value = 0;
                        document.frmcashier.compAddr.value = "";
                        document.frmcashier.compName.value = "";
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>.value = "";
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
                       
                        if(isNaN(cost) || (cost==""))
                            cost = 0.0;
                        if(isNaN(lastDisc) || (lastDisc==""))
                            lastDisc = 0.0;
                        if(isNaN(lastDisc2) || (lastDisc2==""))
                            lastDisc2 = 0.0;
                        if(isNaN(lastDiscNom) || (lastDiscNom==""))
                            lastDiscNom = 0.0;
                        // if(isNaN(discGlobal) || (discGlobal==""))
                        // discGlobal = 0.0;

                        var totaldiscount = cost * lastDisc / 100;
                        var totalMinus = cost - totaldiscount;
                        var totaldiscount2 = totalMinus * lastDisc2 / 100;
                        //var totalDisc = totaldiscount2 + lastDiscNom;

                        var realCost = qty * cost;

                        var totalCost = (totalMinus - totaldiscount2) - lastDiscNom;
                        var lastTotal = qty * totalCost;
                        //var lastTotal = lastTotalTmp - discGlobal;

                        var totalDiscAll = realCost - lastTotal;

                        //untuk menghutung total penjualan
                        if(sum == 0.0){
                            totalAmountTmp = lastTotal;
                        }else{
                            totalAmountTmp = parseFloat(sum) + parseFloat(lastTotal);
                        }

                        document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_DISC]%>.value = totalDiscAll;
                            
                        //document.frmcashier.<!--%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_DISCON_GLOBAL]%>.value = totalMinus;//formatFloat(totalCost, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                        document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_PRICE]%>.value = lastTotal;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                        document.frmcashier.subtotal.value = parseFloat(totalAmountTmp);
                    }

                    function calculateTotal(){
                        var subtotal = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);
                        var disc_global = cleanNumberFloat(document.frmcashier.disc_global.value,guiDigitGroup,guiDecimalSymbol);
                        var disc_type = document.frmcashier.disc_type.value;
                        var taxCode = document.frmcashier.tax_code.value;
                        var taxVal = cleanNumberFloat(document.frmcashier.tax_value.value,guiDigitGroup,guiDecimalSymbol);
                        //var taxPct = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var tax_persen = cleanNumberFloat(document.frmcashier.tax_persen.value,guiDigitGroup,guiDecimalSymbol);
                        
                        //alert("taxCode "+taxCode+" tax_persen "+tax_persen);
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
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = tax_persen;
                        }


                        if(taxCode == 3){
                            var real_amount = parseFloat(total_amount) - parseFloat(taxVal);
                            var pct_value_tax = real_amount * tax_persen / 100;
                            total_amount = parseFloat(real_amount) + parseFloat(pct_value_tax);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = pct_value_tax;
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = tax_persen;
                        }else if(taxCode ==2){
                            total_amount =  parseFloat(total_amount) - parseFloat(taxVal);
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = taxVal;
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = tax_persen;
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
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = grand_total_amount;//formatFloat(lastTotal, '', guiDigitGroup, guiDecimalSymbol, decPlace);
                    }

                    function cmdIsi(element, evt){
                        var pay = cleanNumberFloat(document.frmcashier.pay.value,guiDigitGroup,guiDecimalSymbol);
                        var total = cleanNumberFloat(document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var notaType = document.frmcashier.nota_type.value;
                        if(notaType==<%=PstBillMain.OPEN_BILL%>){
                            //pay = cleanNumberFloat(document.frmcashier.<--%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                            var change = parseFloat(pay) - parseFloat(total);

                             if(change < 0){
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value = 0;
                                document.frmcashier.change.value=0;
                            }else{
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value = parseFloat(pay);
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_AMOUNT_RETURN]%>.value = parseFloat(change);
                                document.frmcashier.change.value = parseFloat(change);
                            }
                          

                        }else if(notaType==<%=PstBillMain.OPEN_CREDIT_INVOICE%>){
                            //pay = cleanNumberFloat(document.frmcashier.<--%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value,guiDigitGroup,guiDecimalSymbol);
                            var change = parseFloat(pay) - parseFloat(total);

                             if(change < 0){
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = 0;
                                document.frmcashier.change.value=0;
                            }else{
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = parseFloat(pay);
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_AMOUNT_RETURN_CREDIT]%>.value = parseFloat(change);
                                document.frmcashier.change.value = parseFloat(change);
                            }
                           
                        }
                    }

                    function calculatePayment(){
                        var pay = cleanNumberFloat(document.frmcashier.pay.value,guiDigitGroup,guiDecimalSymbol);
                        var total = cleanNumberFloat(document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var notaType = document.frmcashier.nota_type.value;
                        

                        if(notaType==<%=PstBillMain.OPEN_BILL%>){
                            
                            //pay = cleanNumberFloat(document.frmcashier.<--%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                            var change = parseFloat(pay) - parseFloat(total);

                            if(change < 0){
                                alert("Payment less than total cost !");
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value = 0;
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.focus();
                            }else{
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>.value = parseFloat(pay);
                                document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_AMOUNT_RETURN]%>.value = parseFloat(change);
                                document.frmcashier.change.value = parseFloat(change);
                            }

                        }else if(notaType==<%=PstBillMain.OPEN_CREDIT_INVOICE%>){
                            //pay = cleanNumberFloat(document.frmcashier.<--%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value,guiDigitGroup,guiDecimalSymbol);
                            var change = parseFloat(pay) - parseFloat(total);
                            
                            if(change < 0){
                                alert("Payment less than total cost !");
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = 0;
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.focus();
                            }else{
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = parseFloat(pay);
                                document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_AMOUNT_RETURN_CREDIT]%>.value = parseFloat(change);
                               
                                document.frmcashier.change.value = parseFloat(change);
                            }
                        }
                        // document.frmcashier.<!--%=FrmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>.value = parseFloat(pay);
                    }


                    function goStock(oid){
                        var strvalue  = "bill_detail_stockcode.jsp?command=<%=Command.FIRST%>"+
                            "&hidden_bill_detail_id="+oid;
                        window.open(strvalue,"stockcode", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdChkNota(code){
                        if(code == <%=PstBillMain.RETUR%>){
                            strvalue  = "srcRetur.jsp?command=<%=Command.FIRST%>"+
                                "&trans_type=2";
                            window.open(strvalue,"customer2", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }else if(code == <%=PstBillMain.OPEN_BILL%>){
                            strvalue  = "srcOpenBill.jsp";
                            window.open(strvalue,"srcOpenBill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }else{
                            strvalue  = "srcCustomerInvoice.jsp";
                            window.open(strvalue,"srcCreditInvoice", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }
                    }

                    function cmdAdd()
                    {
                        document.frmmatkasir.hidden_bill_detail_id.value="0";
                        document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdAsk(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.ASK%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdConfirmDelete(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.DELETE%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdSave()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.SAVE%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function billDetailEdit(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="cashier_lyt.jsp";
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
                            document.frmcashier.action="cashier_lyt.jsp";
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
                            document.frmcashier.action="cashier_lyt.jsp";
                            document.frmcashier.submit();
                        }
                    }

                    function cmdCancel(oidBillDetail)
                    {
                        document.frmcashier.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmcashier.commandDetail.value="<%=Command.EDIT%>";
                        document.frmcashier.prev_command.value="<%=prevCommand%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdBack()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.BACK%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListFirst()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.FIRST%>";
                        document.frmcashier.prev_command.value="<%=Command.FIRST%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListPrev()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.PREV%>";
                        document.frmcashier.prev_command.value="<%=Command.PREV%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListNext()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.NEXT%>";
                        document.frmcashier.prev_command.value="<%=Command.NEXT%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdListLast()
                    {
                        document.frmcashier.commandDetail.value="<%=Command.LAST%>";
                        document.frmcashier.prev_command.value="<%=Command.LAST%>";
                        document.frmcashier.action="cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdChangeMember(){
                        document.frmcashier.submit();
                    }

                     function cmdChangePaymentType(){
                        document.frmcashier.changePaymentType.value="-1";
                        document.frmcashier.submit();
                    }

                    function cmdApproval(code){
                        var custType = document.getElementsByClassName("customerType").value;
                        if(custType !=""){
                            if(code==0){
                                var creditlimmit = cleanNumberFloat(document.frmcashier.creditlimit.value,guiDigitGroup,guiDecimalSymbol);
                                var available = cleanNumberFloat(document.frmcashier.available.value,guiDigitGroup,guiDecimalSymbol);
                                var availableTmp = cleanNumberFloat(document.frmcashier.availableTmp.value,guiDigitGroup,guiDecimalSymbol);

                                if((creditlimmit<="") || isNaN(creditlimmit) || (creditlimmit<=0.0)||(available<="") || isNaN(available) || (available<=0.0)||(availableTmp<="")){
                                    alert("credit limit is empty or insufficient !");
                                }else{
                                    document.frmcashier.command.value="<%=Command.SAVE%>";
                                    document.frmcashier.action = "cashier_lyt.jsp";
                                    document.frmcashier.submit();
                                }
                            }else{
                                document.frmcashier.command.value="<%=Command.SAVE%>";
                                document.frmcashier.action = "cashier_lyt.jsp";
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
                            document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                            cmdCancelBill();
                        }
                    }

                    function cmdSaveAll()
                    {
                        document.frmcashier.allTotal.value = 1;
                        document.frmcashier.command.value="<%=Command.GOTO%>";
                        document.frmcashier.action = "cashier_lyt.jsp";
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
                        var codeCustomer = document.frmcashier.code_custTmp.value;
                        if(codeCustomer == 3){
                            document.getElementById('nameperson').disabled='';
                            document.getElementById('namecomp').disabled='';
                        }
                        document.frmcashier.oidbillmaintmp = oidbillmain;
                        document.frmcashier.command.value="<%=Command.EDIT%>";
                        document.frmcashier.commandDetail.value=0;
                        document.frmcashier.action = "cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdApproval2(){
                        var sku = document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_SKU]%>.value;
                        var qty = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_QTY]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var lastTotal = cleanNumberFloat(document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_TOTAL_PRICE]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var sum = cleanNumberFloat(document.frmcashier.total_amount.value,guiDigitGroup,guiDecimalSymbol);

                        var totalAmountTmp = "";

                        if(sum == 0.0){
                            totalAmountTmp = lastTotal;
                        }else{
                            totalAmountTmp = parseFloat(sum) + parseFloat(lastTotal);
                        }

                        if(sku != "" ){
                            if(qty != "" || qty !=0){
                                document.frmcashier.subtotal.value = parseFloat(totalAmountTmp);
                                document.frmcashier.grandtotal.value = parseFloat(totalAmountTmp);
                                document.frmcashier.commandDetail.value="<%=Command.SAVE%>";
                                document.frmcashier.action = "cashier_lyt.jsp";
                                document.frmcashier.submit();
                            }else{
                                alert("Quatity can not be empty !");
                            }
                        }else{
                            alert("Goods item can not be empty");
                        }
                    }

                    function cmdSaveRetur(){
                        document.frmcashier.commandDetail.value="<%=Command.GOTO%>";
                        document.frmcashier.action = "cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdAdd2(){
                        document.frmcashier.commandDetail.value="<%=Command.ADD%>";
                        document.frmcashier.action = "cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdCheck(){
                      /*  var strvalue  = "materialdosearchcashier.jsp?command=<%=Command.FIRST%>"+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value+
                            "&mat_code="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SKU]%>.value+
                            "&mat_name="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_NAME]%>.value+
                            "&trans_type="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value+
                            "&warehouseSales="+document.frmcashier.warehouseSales.value;*/
                         var strvalue  = "materialdosearchcashier.jsp?command=<%=Command.FIRST%>"+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value+
                            "&mat_code="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_SKU]%>.value+
                            "&mat_name="+document.frmcashier.<%=frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_ITEM_NAME]%>.value+
                            "&trans_type="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value+
                            "&type_sales_order=0"+
                            "&salesWarehouse=<%=locationId%>";
                        window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdCheckRetur(){
                        //if(<//%=privApprovalKasirKreditInv%> || <//%=privApprovalKasirInv%>){
                        <%if(NotaSales!=2){%>
                            var strvalue  = "itemsalesearch.jsp?command=<%=Command.FIRST%>"+
                            "&bill_main_id_old="+document.frmcashier.hidden_bill_main_id.value+
                            "&bill_main_id_new="+document.frmcashier.oidbillmaintmp.value+
                            "&location_id="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>.value+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value+
                            "&warehouseSales="+document.frmcashier.warehouseSales.value;
                            window.open(strvalue,"material2", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        <%}else{%>
                            var strvalue  = "itemmatdosearch.jsp?command=<%=Command.FIRST%>"+
                            "&bill_main_id="+document.frmcashier.hidden_bill_main_id.value+
                            "&standart_rate_id="+document.frmcashier.StandartRateId.value+
                            "&price_type_id="+document.frmcashier.PriceTypeId.value+
                            "&warehouseSales="+document.frmcashier.warehouseSales.value;
                            window.open(strvalue,"material2", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                            document.getElementById('disc1').disabled='disabled';
                            document.getElementById('disc2').disabled='disabled';
                            document.getElementById('disc').disabled='disabled';
                        <%}%>
                    }

                    function cmdCheck2(){
                        strvalue  = "customersearch.jsp?command=<%=Command.FIRST%>";
                        window.open(strvalue,"customer", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdSavePayment(){

                        var grandtotal = cleanNumberFloat(document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value,guiDigitGroup,guiDecimalSymbol);
                        var pay = cleanNumberFloat(document.frmcashier.pay.value,guiDigitGroup,guiDecimalSymbol);
                        
                        if(parseFloat(pay) < parseFloat(grandtotal)){
                            alert("Payment less than total cost !");
                            document.frmcashier.pay.value = 0;
                            document.frmcashier.pay.focus();
                        }else{
                            document.frmcashier.allTotal.value = 1;
                            document.frmcashier.commandPayment.value="<%=Command.SAVE%>";
                            document.frmcashier.action = "cashier_lyt.jsp";
                            document.frmcashier.submit();
                        }
                    }

                    function cmdAddNew(){
                        var resetvalue = 0;
                        var codecust = cleanNumberFloat(document.frmcashier.code_cust.value,guiDigitGroup,guiDecimalSymbol);
                        var idNotaType = document.frmcashier.nota_type.value;

                        if(codecust == 3){
                            document.frmcashier.outstanding.value=resetvalue;
                            document.frmcashier.available.value=resetvalue;
                            document.frmcashier.creditlimit.value=resetvalue;
                            document.frmcashier.availableTmp.value = resetvalue;
                        }
                        
                        if(idNotaType == <%=PstBillMain.NONE%>){
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = resetvalue;

                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>.value = resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>.value = "";
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>.value = "";
                            document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_SALES_ID]%>.value=resetvalue;
                            document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value = "";

                        }

                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value = resetvalue;
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]%>.value = resetvalue;
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]%>.value = resetvalue;
                        document.frmcashier.hidden_bill_main_id.value = resetvalue;
                        document.frmcashier.oidbillmaintmp.value = resetvalue;
                        document.frmcashier.disc_global.value = resetvalue;
                        
                        document.frmcashier.allTotal.value = resetvalue;
                        document.frmcashier.notasalestype.value = resetvalue;
                        document.frmcashier.code_cust.value = resetvalue;
                        
                        document.frmcashier.code_custTmp.value = resetvalue;
                        document.frmcashier.available_credit.value=resetvalue;
                        
                        document.frmcashier.commandDetail.value=resetvalue;
                        document.frmcashier.commandPayment.value=resetvalue;
                        document.frmcashier.vectSize.value=resetvalue;
                        document.frmcashier.start.value=resetvalue;
                        document.frmcashier.prev_command.value=resetvalue;
                        document.frmcashier.start_detail.value=resetvalue;
                        document.frmcashier.start_payment.value=resetvalue;
                        document.frmcashier.hidden_bill_detail_id.value=resetvalue;
                        document.frmcashier.sale_type.value=resetvalue;
                        document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>.value=resetvalue;
                        document.frmcashier.PriceTypeId.value=resetvalue;
                        document.frmcashier.StandartRateId.value=resetvalue;
                        document.frmcashier.total_amount.value=resetvalue;
                        document.frmcashier.billdetailTmp.value=resetvalue;
                        document.frmcashier.trans_type.value=resetvalue;
                        document.frmcashier.stock_return.value=resetvalue;
                        document.frmcashier.notaSalesType.value=resetvalue;
                        document.frmcashier.notasalestypeTmp.value=resetvalue;
                        document.frmcashier.customerId.value=resetvalue;
                        document.frmcashier.commandTmp.value=resetvalue;
                        document.frmcashier.hidden_bill_main_id.value = resetvalue;
                        document.frmcashier.oidbillmaintmp.value = resetvalue;
                        document.frmcashier.commandDetail.value=resetvalue;
                        document.frmcashier.nota_type.value=resetvalue;
                        document.frmcashier.grandtotal.value = resetvalue;
                        
                        <% if (typeCashier==0) {%>
                            document.frmcashier.action = "cashier_lyt.jsp";
                        <%}else{
                             if(idNotaType== PstBillMain.OPEN_BILL){%>
                                document.frmcashier.action = "src_list_open_bill.jsp";
                             <%}else if(idNotaType== PstBillMain.NONE){%>
                                document.frmcashier.action = "cashier_lyt.jsp";
                             <%}else if(idNotaType== PstBillMain.RETUR){%>
                                document.frmcashier.action = "src_list_retun_bill.jsp?trans_type=2";
                            <%}
                            }
                         %>
                        document.frmcashier.submit();
                    }

                    function cmdBack(){
                        var oidBillMain = document.frmcashier.hidden_bill_main_id.value;
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                        if(oidBillMain == 0){
                            cmdAddNew();
                            document.frmcashier.command.value="<%=Command.BACK%>";
                            document.frmcashier.action="salesorder_list.jsp";
                            document.frmcashier.submit();
                        }else{
                            var con = confirm("Invoice will be cancel.\nDo you want to go Back to List ?");
                            if (con ==true)
                            {
                                document.frmcashier.commandTmp.value="<%=Command.GOTO%>";
                                cmdCancelBill();

                            }
                        }
                    }

                    function cmdCancelBill(){
                        var oidbillmain = document.frmcashier.hidden_bill_main_id.value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_SALES_ID]%>.value = oidbillmain ;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;

                        document.frmcashier.command.value="<%=Command.CANCEL%>";
                        //document.frmcashier.commandTmp.value="<!--%=Command.RESET%>";
                        document.frmcashier.action =  "cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdCancelOpenBill(){
                        var oidbillmain = document.frmcashier.hidden_bill_main_id.value;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_SALES_ID]%>.value = oidbillmain ;
                        document.frmcashier.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;

                        document.frmcashier.commandTmp.value="<%=Command.CANCEL%>";
                        document.frmcashier.action =  "cashier_lyt.jsp";
                        document.frmcashier.submit();
                    }

                    function goNewPage(){
                        var oidbillmain = document.frmcashier.oidbillmaintmp.value;
                        if(oidbillmain != 0 ){
                            var con = confirm("Please submit Save Invoice before Add New Or Your Invoicing Invoice Process will be cancel.\nDo you want to reset this page ?");
                            if (con ==true)
                            {
                                document.frmcashier.commandTmp.value="<%=Command.RESET%>";
                                cmdCancelBill();
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

                    function logout(){
                        document.frmcashier.action = "../logout.jsp";
                        document.frmcashier.submit();
                    }

                    function cmdPaySystemCredit(){
                        var notaType = document.frmcashier.nota_type.value;
                        var strvalue = "";
                        if(notaType == <%=PstBillMain.OPEN_CREDIT_INVOICE%>){
                            strvalue  = "paymentTypeCredit.jsp?command=<%=Command.FIRST%>"+
                                "&hidden_bill_main_id="+document.frmcashier.hidden_bill_main_id.value+                                
                                "&app_user_id="+document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_APPUSER_ID]%>.value+
                                "&location_id="+document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_LOCATION_ID]%>.value+                                
                                "&shift_id="+document.frmcashier.<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SHIFT_ID]%>.value+
                                "&pay_type="+document.frmcashier.<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>.value+
                                "&amount="+document.frmcashier.total_amount.value+
                                "&FRM_FIELD_CURRENCY_ID=1";
                        }else if(notaType == <%=PstBillMain.OPEN_BILL%>){
                            strvalue  = "paymentTypeCash.jsp?command=<%=Command.FIRST%>"+
                                "&hidden_bill_main_id="+document.frmcashier.hidden_bill_main_id.value+
                                "&cash_cashier_id="+document.frmcashier.<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_CASHCASHIER_ID]%>.value+
                                "&amount="+document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>.value
                            "&FRM_FIELD_CURR_ID=1";
                        }

                        window.open(strvalue,"payCredit", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdHitung(){
                        var oidBillMain = document.frmcashier.hidden_bill_main.value;
                        var disc = document.frmcashier.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]%>.value;
                    }

                    function getListInvoice(){
                        strvalue  = "srcInvoice.jsp";
                        window.open(strvalue,"List_Invoice", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function modifKey(frmObj, event,value){
                       // alert(event.keyCode);
                        if(event.keyCode == 13) {

                        }else if (event.keyCode==112){ //F1=Open Sales Order
                            strvalue  = "srcOpenBill.jsp";
                            window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }else if (event.keyCode==120){ //F9=Save Transaction
                            var con = confirm("Yaking Untuk Menyimpan Data ?");
                            if (con ==true){
                                <% if (codeCustomer == 3 && NotaSales == PstBillMain.NONE) {%>
                                      cmdApproval(0);
                                <%} else {%>
                                      cmdApproval(1)
                                <%}%>
                           }
                        }else if (event.keyCode==113){ //F2=Open Cash Invoice
                            strvalue  = "srcOpenBill.jsp";
                            window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }else if (event.keyCode==115){ //F4=Open Credit Invoice
                            strvalue  = "srcCustomerInvoice.jsp";
                            window.open(strvalue,"creditinvoice", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                            document.getElementById('codetax').readOnly = true;
                            document.getElementById('<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>').readOnly = true;
                        }else if (event.keyCode==119){ //F8=Return
                            strvalue  = "srcRetur.jsp?command=<%=Command.FIRST%>"+
                            "&trans_type=2";
                            window.open(strvalue,"customer2","height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                        }
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
function goBack() {
    window.history.back();
}
        </SCRIPT>
        <SCRIPT type="text/javascript">
                    window.history.forward();
                    function noBack() { window.history.forward(); }
        </SCRIPT>
        <style type="text/css">
            <!--
            .style1 {
                color: #009900;
                font-weight: bold;
            }
            .style3 {font-size: 24px}
            .style4 {
                font-size: 26px;
                text-align: right;
                background-color: #fcfded;

            }
            .style5 {
                font-size: 14px;
                text-align: right;

            }
            .styleButton{
                color: #000099;
                font-weight: bold;
            }
            -->
            #btn {padding: 2px 3px; color: #fff; font-size: 11px; background-color: #333; border:1px solid #000; border-radius: 3px;}
            #btn:hover {background-color:#CC3300; border: 1px solid #800000; }
        </style>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="" onkeydown="modifKey(this, event, this.value)">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >

            <%if(menuUsed == MENU_PER_TRANS){%>
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
              <%}else{%>
               <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                  <%@include file="../styletemplate/template_header.jsp" %>
                </td>
              </tr>
              <%}%>
            <tr>
                <td valign="top" align="left">
                    <form name="frmcashier" method ="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="commandDetail" value="<%=iCommandDetail%>">
                        <input type="hidden" name="commandPayment" value="<%=iCommandPayment%>">
                        <input type="hidden" name="commandTmp" value="<%=iCommandTmp%>">

                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="start_detail" value="<%=startDetail%>">
                        <input type="hidden" name="start_payment" value="<%=startPayment%>">
                        <input type="hidden" name="cmd" value="<%=CMD_APPROVAL%>">

                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                        <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">
                        <input type="hidden" name="oidbillmaintmp" value="<%=oidbillmain%>">
                        <input type="hidden" name="hidden_cash_payment_id" value="<%=oidCashPayment%>">
                        <input type="hidden" name="sale_type" value="<%=idSaleType%>">
                        <input type="hidden" name="disc_type" value="<%=DiscType%>">

                        <input type="hidden" name="CurrId" value="<%=idCurrency%>">
                        <!--input type="hidden" name="customerId" value="<//%=idCustomer%>"-->
                        <input type="hidden" name="PriceTypeId" value="<%=idPriceType%>">
                        <input type="hidden" name="StandartRateId" value="<%=idStandartRate%>">
                        <input type="hidden" name="total_amount" value="<%=amounttotal%>">
                        <input type="hidden" name="nota_type" value="<%=idNotaType%>">
                        <input type="hidden" name="billdetailTmp" value="<%=idbilldetailTmp%>">
                        <input type="hidden" name="oidPayment" value="<%=oidPayment%>">
                        <input type="hidden" name="trans_type" value="<%=idTransType%>">
                        <input type="hidden" name="rec_type" value="">
                        <input type="hidden" name="stock_return" value="<%=stockReturn%>">
                        <input type="hidden" name="notaSalesType" value="<%=NotaSales%>">
                        <input type="hidden" name="notasalestypeTmp" value="<%=NotatypeTmp%>">
                        <input type="hidden" name="available_credit" value="<%=available%>">
                        <input type="hidden" name="code_custTmp" value="<%=codeCustomer%>">
                        <input type="hidden" name="tax_code" value="<%=taxCode%>">
                        <!--input type="hidden" name="tax_pct" value="<!--%=taxPct%>"-->
                        <input type="hidden" name="tax_value" value="<%=taxValue%>">
                        <input type="hidden" name="compAddrs" value="<%=compAddress%>">
                        <input type="hidden" name="allTotal" value="<%=tmpsalesorder%>">
                        <input type="hidden" name="warehouseSales" value="<%=salesWarehouseLoc%>">
                        <input type="hidden" name="SalesName" value="<%=salesName%>">
                        <input type="hidden" name="customerId" value="<%=custId%>">
                        <input type="hidden" name="typeCashier" value="<%=typeCashier%>">
                        
                        <input type="hidden" name="ApprovalKasirPay" value="<%=ApprovalKasirPay%>">
                         
                        <%if (idNotaType == PstBillMain.OPEN_CREDIT_INVOICE && privApprovalKasirPay) {%>
                        <!-- untuk payment -->
                        <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                        <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_APPUSER_ID]%>" value="<%=userId%>">
                        <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
                        <input type="hidden" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashcashierId%>">
                        <input type="hidden" name="<%=FrmCashCreditPayment.fieldNames[FrmCashCreditPayment.FRM_FIELD_PAY_TYPE]%>" value="<%=payType%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>" value="<%=rate%>">
                        <%} else if (idNotaType == PstBillMain.OPEN_BILL && privApprovalKasirPay) {%>
                        <!-- untuk cash payment -->
                        <input type="hidden" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_CURR_ID]%>" value="<%=(billMain.getCurrencyId())%>">
                        <input type="hidden" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_CASH_BILL_MAIN_ID]%>" value="<%=oidBillMain%>">
                        <input type="hidden" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_TYPE]%>" value="<%=payType%>">
                        <input type="hidden" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_RATE]%>" value="<%=rate%>">
                        <input type="hidden" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_CASHCASHIER_ID]%>" value="<%=cashcashierId%>">
                        <%--<input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>" value="<%=rate%>">--%>
                        <%}else{%>
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_INVOICE_NUMBER]%>" value="<%=billMain.getInvoiceNumber()%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIFT_ID]%>" value="<%=shiftId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_APP_USER_ID]%>" value="<%=userId%>">
                       <% if(!privApprovalKasirPay){%>
                            <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_INVOICING_ID]%>" value="<%=userId%>">
                        <%}%>
                        <% if(idNotaType == PstBillMain.RETUR){%>
                            <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>" value="<%=oidBillMain%>">
                        <%}%>
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_LOCATION_ID]%>" value="<%=locationId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CASH_CASHIER_ID]%>" value="<%=cashcashierId%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>" value="<%=transStatus%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_SALES_ID]%>" value="<%=oidBillMain%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]%>" value="<%=docType%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE]%>" value="<%=billMain.getSalesCode()%>">
                        <input type="hidden" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_APP_USER_SALES_ID]%>" value="<%=billMain.getAppUserSalesId()%>">
                        <%}%>

                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="20" class="mainheader"><center><h1><%=privApprovalKasirPay==true?"CASHIER BY ":"INVOICING ORDER BY "%><%=userName%></h1></center></td>
                            </tr
                            <tr>
                                <td height="20" align="left">
                                    <table width="98%" border="0" cellspacing="0" cellpadding="0" align="left">
                                        <tr>
                                            <td align="right">

                                                <div align="right"><input type="button" name="Closing Shift" value="Closing Shift" onClick="javascript:closeshift()" class="styleButton"></div>
                                            </td>

                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td><!-- #BeginEditable "content" -->
                                    <table width="100%" cellspacing="1" cellpadding="1">
                                        <tr>
                                            <td>
                                               
                                                <fieldset><legend >
                                                        <span class="style1">Main Data</span>
                                                    </legend>
                                                    <table id="table" align="center" width="98%" cellspacing="1" cellpadding="1">
                                                        <tr>
                                                            <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2">
                                                                    <tr>
                                                                        <td width="37%"><%=textMaterialHeader[SESS_LANGUAGE][0]%></td>
                                                                        <td width="63%">
                                                                            <%          Vector val_notatype = new Vector(1, 1);
                                                                                        Vector key_notatype = new Vector(1, 1);
                                                                                        if(typeCashier==0){
                                                                                            val_notatype.add("" + PstBillMain.NONE);
                                                                                            key_notatype.add("Sales Order");
                                                                                            if(!privApprovalKasirPay){
                                                                                            val_notatype.add("" + PstBillMain.OPEN_BILL);
                                                                                            key_notatype.add("Open List Sales Order");
                                                                                            }if(privApprovalKasirPay){
                                                                                            val_notatype.add("" + PstBillMain.OPEN_BILL);
                                                                                            key_notatype.add("Open List Cash Invoice");
                                                                                            }if(privApprovalKasirPay){
                                                                                            val_notatype.add("" + PstBillMain.OPEN_CREDIT_INVOICE);
                                                                                            key_notatype.add("Open List Credit Invoice");
                                                                                            }if(privApprovalKasirRetur){
                                                                                            val_notatype.add("" + PstBillMain.RETUR);
                                                                                            key_notatype.add("Retur");
                                                                                            }
                                                                                        }else{
                                                                                            if(idNotaType== PstBillMain.OPEN_BILL){
                                                                                                val_notatype.add("" + PstBillMain.OPEN_BILL);
                                                                                                key_notatype.add("Open List Sales Order");
                                                                                            }else if(idNotaType== PstBillMain.NONE){
                                                                                                val_notatype.add("" + PstBillMain.NONE);
                                                                                                key_notatype.add("Sales Order");
                                                                                            }else if(idNotaType== PstBillMain.RETUR){
                                                                                                val_notatype.add("" + PstBillMain.RETUR);
                                                                                                key_notatype.add("Retur");
                                                                                            }else if(idNotaType== PstBillMain.VOID){
                                                                                                val_notatype.add("" + PstBillMain.VOID);
                                                                                                key_notatype.add("Void");
                                                                                            }
                                                                                        }
                                                                                        

                                                                                        String NotaSalesCode = "" + NotaSales;
                                                                                        String link = "";
                                                                                        if (NotatypeTmp < 0) {
                                                                                            NotatypeTmp = NotaSales;
                                                                                        }
                                                                                        if (NotatypeTmp > 0 && NotatypeTmp < 10) {
                                                                            %>
                                                                            <script type="text/javascript">
                                                                                        var limitType = 11;
                                                                                        var strvalue = "";
                                                                                        var notaType = document.frmcashier.notaSalesType.value;
                                                                                        document.frmcashier.notasalestypeTmp.value = limitType;
                                                                                        //alert("notaType "+notaType);
                                                                                        if(notaType==1){
                                                                                            strvalue  = "srcOpenBill.jsp";
                                                                                            window.open(strvalue,"openbill", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                                                                                        }else if(notaType==5){
                                                                                            strvalue  = "srcCustomerInvoice.jsp";
                                                                                            window.open(strvalue,"creditinvoice", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                                                                                            document.getElementById('codetax').readOnly = true;
                                                                                            document.getElementById('<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]%>').readOnly = true;
                                                                                        }else if(notaType==2){
                                                                                            strvalue  = "srcRetur.jsp?command=<%=Command.FIRST%>"+
                                                                                                "&trans_type=2";
                                                                                            window.open(strvalue,"customer2", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                                                                                        }

                                                                            </script>

                                                                            <% }
                                                                            %>
                                                                            <%=ControlCombo.draw("notasalestype", null, (NotaSales == 0) ? NotaSalesCode : "" + NotaSales, val_notatype, key_notatype, "onChange=\"javascript:goSubmit()\"", "formElemen")%>
                                                                            <%if (NotaSales != PstBillMain.NONE) {%>
                                                                            <a href="javascript:cmdChkNota('<%=NotaSales%>')"> CHK </a>
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][1]%></td>
                                                                        <td><%=(billMain.getInvoiceNumber() == null ? "<b>- Otomatis -</b>" : billMain.getInvoiceNumber())%></td>
                                                                        <!--billMain.getInvoiceNumber()-->
                                                                    </tr>
                                                                    <tr>
                                                                        <td height="32"><%=textMaterialHeader[SESS_LANGUAGE][2]%></td>
                                                                        <td>
                                                                            <%

                                                                                        //long selectedCustomerId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]);
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
                                                                                        if (billMain.getCustomerId() != 0) {
                                                                                            //MemberReg memberRegx = PstMemberReg.fetchExc(billMain.getCustomerId());
                                                                                            Vector listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID]+"='"+memberReg.getMemberGroupId()+"'", "");
                                                                                            MemberGroup memberGroupCode = (MemberGroup) listCodeCustomer.get(0);
                                                                                            select_Customer = "" + memberGroupCode.getOID();
                                                                                            codeCustomer = 3;
                                                                                            /*if (billMain.getCustomerId() == memberGroupCode.getOID()) {
                                                                                                select_Customer = "" + memberGroupCode.getOID();
                                                                                            } else {
                                                                                                listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='3'", "");
                                                                                                MemberGroup memberGroup = (MemberGroup) listCodeCustomer.get(0);
                                                                                                select_Customer = "" + memberGroup.getOID();
                                                                                                codeCustomer = 3;
                                                                                            }*/

                                                                                        } else {
                                                                                            if (custId != 0) {
                                                                                                select_Customer = "" + memberReg.getMemberGroupId();
                                                                                                Vector listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + memberReg.getMemberGroupId() + "'", "");
                                                                                                MemberGroup memberGroupCode = (MemberGroup) listCodeCustomer.get(0);
                                                                                                codeCustomer = memberGroupCode.getGroupType();
                                                                                            }
                                                                                        }
                                                                            %>
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID], "customerType", null, select_Customer, vectCustomerVal, vectCustomerKey, "onChange=\"javascript:cmdChangeMember()\"")%>
                                                                            <input name="code_cust" type="hidden" size="12"  value="<%=codeCustomer%>"></td>

                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                                        <td><%
                                                                                    int changePaymentType = FRMQueryString.requestInt(request, "changePaymentType");
                                                                                    Vector val_saletype = new Vector(1, 1);
                                                                                    Vector key_saletype = new Vector(1, 1);
                                                                                    String select_saletype = "";
                                                                                    if (codeCustomer == 3) {
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
                                                                                        key_saletype.add("Cash");
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CREDIT);
                                                                                        key_saletype.add("Kredit");
                                                                                         if(available>=0 && changePaymentType!=-1){
                                                                                             select_saletype = "" + PstBillMain.TRANS_TYPE_CREDIT;
                                                                                         }else{
                                                                                              select_saletype = "" + idSaleType;//PstBillMain.TRANS_TYPE_CASH;
                                                                                         }
                                                                                    } else {
                                                                                        val_saletype.add("" + PstBillMain.TRANS_TYPE_CASH);
                                                                                        key_saletype.add("Cash");
                                                                                        select_saletype = "" + idSaleType;
                                                                                    }
                                                                            %>
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE], null, (billMain.getTransctionType() == 0) ? select_saletype : "" + billMain.getTransctionType(), val_saletype, key_saletype, "onChange =\"cmdChangePaymentType()\"", "formElemen")%></td>
                                                                             <input type="hidden" name="changePaymentType" value="">
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][26]%></td>
                                                                        <td>
                                                                            <%//=salesOrderCode%>
                                                                            <%
//                                                                                 Vector listSales = PstSales.listAll();
                                                                                String whereData = PstMappingUserGroup.fieldNames[PstMappingUserGroup.FLD_GROUP_USER_ID] + " = 6";
                                                                                Vector sale = PstMappingUserGroup.list(0, 0, whereData, "");
                                                                                 Vector vectSalesVal = new Vector(1, 1);
                                                                                 Vector vectSalesKey = new Vector(1, 1);
                                                                                 String namaSales = "";
                                                                                 for (int i = 0; i < sale.size(); i++) {
                                                                                        AppUser matSales = (AppUser) sale.get(i);
                                                                                        //if(billMain.getPersonName())
                                                                                        //vectSalesKey.add(matSales.getName());
                                                                                        namaSales = matSales.getFullName();
                                                                                        vectSalesKey.add(matSales.getFullName());
                                                                                        vectSalesVal.add("" + matSales.getOID());
                                                                                        
                                                                                 }
                                                                            %>
                                                                            
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SALES_CODE], "formElemen", null,namaSales, vectSalesVal, vectSalesKey, "")%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                                        <td>Rp. <%=Formater.formatNumber(PstStandartRate.getStandardRate(), "#,###.##")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                        <td><label><%
                                                                                    
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

                                                                                at <%-- <%=FRMHandler.userFormatStringDecimal(sellingRate)%>/USD --%>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_RATE]%>" type="hidden" size="12" value="<%=(billMain.getOID()==0) ? sellingRate : billMain.getRate()%>">
                                                                                 <input name="rate" type="text" size="12" disabled="true" value="<%=(billMain.getOID()==0) ? sellingRate : billMain.getRate()%>">
                                                                            </label>
                                                                        </td>
                                                                    </tr>
                                                                </table></td>
                                                            <td></td>
                                                            <td colspan="2" rowspan="5" valign="top"><table width="100%" border="0" cellspacing="2" cellpadding="2" >
                                                                    <tr>
                                                                        <td width="20%"><%=textMaterialHeader[SESS_LANGUAGE][6]%></td>
                                                                        <td width="80%"><%=ControlDate.drawDateWithStyle(FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_BILL_DATE], (billMain.getBillDate() == null) ? new Date() : billMain.getBillDate(), 0, -1, "formElemen", "disabled")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][7]%></td>
                                                                        <td><label>
                                                                                <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>" cols="32" rows="2" id="shAddrs" tabindex="5" ><%=(billMain.getShippingAddress() == "") ?  memberReg.getBussAddress() : billMain.getShippingAddress()%></textarea>
                                                                                <!--input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>" type="hidden" value="<%=(billMain.getShippingAddress() == "") ?  memberReg.getBussAddress() : billMain.getShippingAddress()%>"-->
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
                                                                                    <td>
                                                                                        <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>" type="text" id="shcity" size="12" tabindex="6"  value="<%=(billMain.getShippingCity() == "") ? memberReg.getTown() : billMain.getShippingCity()%>" >
                                                                                        <!--input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>" type="hidden"  value="<%=(billMain.getShippingCity() == "") ? memberReg.getTown() : billMain.getShippingCity()%>"-->,
                                                                                    </td>
                                                                                    <td>
                                                                                        <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>" type="text" id="shprov" size="12" tabindex="7" value="<%=(billMain.getShippingProvince() == "") ? memberReg.getProvince() : billMain.getShippingProvince()%>">
                                                                                        <!--input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>" type="hidden"  value="<%=(billMain.getShippingProvince() == "") ? memberReg.getProvince() : billMain.getShippingProvince()%>"-->,
                                                                                    </td>
                                                                                    <td>
                                                                                        <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>" type="text" id="shcountry" size="12" tabindex="8" value="<%=(billMain.getShippingCountry() == "") ? memberReg.getCountry() : billMain.getShippingCountry()%>">
                                                                                        <!--input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>" type="hidden" value="<%=(billMain.getShippingCountry() == "") ? memberReg.getCountry() : billMain.getShippingCountry()%>"-->
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>" type="text" id="shtlp" size="15" tabindex="9" value="<%=(billMain.getShippingPhoneNumber() == "") ? memberReg.getTelpNr() : billMain.getShippingPhoneNumber()%>">
                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][22]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>" type="text" id="shfax" size="15" tabindex="10" value="<%=(billMain.getShippingFax() == "") ? memberReg.getFax() : billMain.getShippingFax()%>">
                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>" type="text" id="shzip" size="10" maxlength="10" tabindex="11" value="<%=(billMain.getShippingZipCode() == "" || billMain.getShippingZipCode() == null) ? memberReg.getPostalCode() : billMain.getShippingZipCode()%>"></td>
                                                                    </tr>
                                                                </table></td>
                                                            <td></td>
                                                            <td colspan="2" rowspan="5" valign="top">
                                                                <%
                                                                if (privApprovalKasirInv) {
                                                                %>
                                                                <table width="107%" border="0" cellspacing="2" cellpadding="2">
                                                                    <tr>
                                                                        <td width="26%"><%=textMaterialHeader[SESS_LANGUAGE][10]%></td>
                                                                        <td width="74%"><label>
                                                                                <!--input name="cusId" type="hidden" value="<%=billMain.getCustomerId()%>" size="15" maxlength="64" -->
                                                                                 <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID]%>" type="hidden" value="<%=custId%>" size="15" maxlength="64" >
                                                                                <input name="compName" type="text" value="<%=compName ==""? memberReg.getCompName():compName%>" id="namecomp" size="15" maxlength="64" tabindex="12">
                                                                                <!--a href="javascript:cmdCheck2()">CHK</a-->
                                                                                <%if (oidBillMain==0) {%>
                                                                                <a href="javascript:cmdCheck2()">CHK</a>
                                                                                <%}%>
                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                        <td><input name="compAddr" type="text" value="<%=address == "" ? compAddress : address %>" size="15" maxlength="64" tabindex="13" class="hiddenLabel" readonly ></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" type="text" value="<%=custName == ""?billMain.getGuestName():custName %>" id="nameperson" size="15" maxlength="64" tabindex="14" ></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][12]%> </td>
                                                                        <td>Rp. <input name="creditlimit" type="text" align="right" value="<%=creditLimit == 0 ? 0 : FRMHandler.userFormatStringDecimal(creditLimit)%>" size="15" tabindex="15" class="hiddenLabel" readonly></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][13]%></td>
                                                                        <td>Rp. <input name="outstanding" type="text" align="right" value="<%=outstanding == 0 ? 0 : FRMHandler.userFormatStringDecimal(outstanding)%>" size="15" maxlength="64" tabindex="16" class="hiddenLabel" readonly> </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][14]%></td>
                                                                        <td>Rp. <input name="available" type="text" value="<%=available == 0 ? 0 : FRMHandler.userFormatStringDecimal(available)%>" align="right" size="15" maxlength="64" tabindex="17" class="hiddenLabel" readonly>
                                                                            <input name="availableTmp" type="hidden" value="<%=available == 0 ? 0 : available%>" align="right" size="15" maxlength="64" tabindex="17" class="hiddenLabel" readonly> </td>
                                                                    </tr>
                                                                     <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][27]%></td>
                                                                        <%
                                                                            
                                                                        %>
                                                                        <td>
                                                                            <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DUE_DATE_PAYMENT]%>" type="hidden" value="<%=memberReg.getDayOfPayment()%>" align="right" size="15" maxlength="64" tabindex="17" class="hiddenLabel" readonly> 
                                                                            <%=memberReg.getDayOfPayment()%> days <%= Formater.formatDate(dueDatePayment, "dd MMM yyyy")%>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][25]%></td>
                                                                        <td><label>
                                                                                <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_NOTES]%>" cols="32" rows="2" ><%=(notes == "") ? billMain.getNotes() : notes%></textarea>
                                                                            </label></td>
                                                                    </tr>
                                                                </table>
                                                                <%}else{%>
                                                                    <input name="compAddr" type="hidden" value="" size="15" maxlength="64" tabindex="13" class="hiddenLabel" readonly >
                                                                    <input name="compName" type="hidden" value="" size="15" maxlength="64" tabindex="13" class="hiddenLabel" readonly >
                                                                    <input name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" type="hidden" value="" size="15" maxlength="64" tabindex="13" class="hiddenLabel" readonly >
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
                                                                            <%if(!privApprovalKasirPay){
                                                                            if (iCommandDetail == Command.ADD ) {%>
                                                                            <input type="button" name="Button" value="Edit" onClick="javascript:cmdEditMain('<%=oidbillmain%>')"  class="styleButton">
                                                                            <%}
                                                                            //if (iCommand == Command.ADD) {
                                                                                if (codeCustomer == 3 && NotaSales == PstBillMain.NONE) {%>
                                                                                    <input type="button" name="Button" value="Save" onClick="javascript:cmdApproval(0)"  class="styleButton">
                                                                                <%} else {%>
                                                                                    <input type="button" name="Button" value="Save" onClick="javascript:cmdApproval(1)"  class="styleButton">
                                                                                <%}
                                                                             //}
                                                                            }
                                                                            %>
                                                                            <input type="button" name="back" onclick="goBack()" value="Back" />
                                                                        </td>                                                   </tr>
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
                                            <td > <%= drawListBillDetailAdd(SESS_LANGUAGE, iCommandDetail, frmBillDetail, billdetail, listBillDetail, oidBillDetail, startDetail, idNotaType, oidBillMain,vectLoc,privSalesInvoice)%> </td>
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
                                                <table width="60%" align="left">
                                                    <tr>
                                                         <td>
                                                             <fieldset>
                                                                <legend >
                                                                     <span class="style1">Kunci Bantuan</span>
                                                                </legend>
                                                                 <h3>F1 = Open Sales Order&nbsp&nbsp;F2 = Open Cash Invoice&nbsp&nbsp;F4 = Open Credit Invoice&nbsp&nbsp;F9 = Save Transaction &nbsp&nbsp;F8 = Return</h3>
                                                              </fieldset>
                                                         </td>
                                                    </tr>
                                                </table>
                                                <table width="30%" align="right">
                                                    <tr>
                                                        <td widht="15%">
                                                            <span class="style5">Subtotal</span>
                                                        </td>
                                                        <td align="left" colspan="2" widht="20%">
                                                            <input size="34" name="subtotal" align="right" type="text" value="<%=FRMHandler.userFormatStringDecimal(amounttotal)%>"  class="style5"  readonly>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td widht="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][23]%></span>
                                                        </td>
                                                        <td width="5%" align="left">
                                                            <%          Vector val_disctype = new Vector(1, 1);
                                                                        Vector key_disctype = new Vector(1, 1);

                                                                        val_disctype.add("" + PstBillDetail.DISC_TYPE_PERCENT);
                                                                        key_disctype.add(" % ");
                                                                        val_disctype.add("" + PstBillDetail.DISC_TYPE_VALUE);
                                                                        key_disctype.add(" val ");

                                                                        String disc_type1 = "" + DiscType;
                                                                        String discTypeTmp = "";
                                                                        if (billMain.getDiscount() != 0|| billMain.getDiscPct() != 0) { 
                                                                            discTypeTmp = "" + billMain.getDiscType();
                                                                            DiscType = billMain.getDiscType();
                                                                        }

                                                            %>

                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE], null, (discTypeTmp == "") ? disc_type1 : discTypeTmp, val_disctype, key_disctype, "onChange =\"cmdChangeMember()\"", "formElemen")%>

                                                        </td>
                                                        <td align="left" width="15%">
                                                            <% if (DiscType == PstBillDetail.DISC_TYPE_VALUE) {%>
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
                                                             <input name="tax_persen" tabindex="29" align="right" type="text" onkeyup="javascript:calculateTotal()" onkeypress="javascript:keybrdPressTotal(this, event)" value="<%=(billMain.getTaxPercentage() == 0) ? taxPctTmp : (billMain.getTaxPercentage())%>" class="style5">
                                                            <input id="codetax" size="2" name="<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>" onkeyup="javascript:calculateTotal()" type="hidden" tabindex="30" align="right" type="text" value="<%=(billMain.getTaxPercentage() == 0) ? taxPctTmp : (billMain.getTaxPercentage())%>" class="style5"> %
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][19]%></span>
                                                        </td>
                                                        <td align="left" colspan="2" width="20%">
                                                            <input size="34" name="grandtotal" align="right" type="text" value="<%=FRMHandler.userFormatStringDecimal(amountTmp)%>"  class="style5" readonly>
                                                            <input size="34" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]%>" align="right" type="hidden" value="<%=amountTmp%>" class="style5"  readonly>
                                                            <input size="34" name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]%>" align="right" type="hidden" value="<%=billMain.getTaxValue()%>" class="style5"  readonly>
                                                        </td>
                                                    </tr>
                                                    <%
                                                                //if(idNotaType != PstBillMain.RETUR || idNotaType != PstBillMain.RETUR){
                                                                int transTypeInvoice = 0;
                                                                String whereClausePay = "";
                                                                if (idNotaType == PstBillMain.OPEN_ALL_INVOICE) {
                                                                    transTypeInvoice = billMain.getTransctionType();
                                                                    if (transTypeInvoice == PstBillMain.TRANS_TYPE_CASH) {
                                                                        whereClausePay = "CP." + PstCashPayment1.fieldNames[PstCashPayment1.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'";
                                                                        payAmount = PstCashPayment1.getSumPayment(whereClausePay);
                                                                    } else {
                                                                        whereClausePay = "CPM." + PstCreditPaymentMain.fieldNames[PstCreditPaymentMain.FLD_BILL_MAIN_ID] + " = '" + oidBillMain + "'";
                                                                        payAmount = PstCreditPaymentMain.getSumPayment(whereClausePay);
                                                                    }
                                                                    Vector listReturPayment = PstCashReturn.list(0, 0, PstCashReturn.fieldNames[PstCashReturn.FLD_BILLMAIN_ID] + " ='" + oidBillMain + "'", "");
                                                                    CashReturn cashReturn = new CashReturn();
                                                                    if(listReturPayment.size()>0) {
                                                                        cashReturn = (CashReturn) listReturPayment.get(0);
                                                                        change = cashReturn.getAmount();
                                                                    }
                                                                }


                                                    %>
                                                    <%
                                                    if(privApprovalKasirPay) {
                                                    %>
                                                    <tr>
                                                        <td width="15%">
                                                            <span class="style5"> <%=textMaterialHeader[SESS_LANGUAGE][20]%> </span>
                                                        </td>
                                                        <td align="left" colspan="2" width="20%">
                                                            <input size="34" name="pay" align="right" type="text" value="<%=payAmount%>" onkeyup="javascript:cmdIsi(this, event)" onkeypress="javascript:keybrdPressTotal(this, event)" class="style5" >
                                                            <!--%if (idNotaType == PstBillMain.OPEN_BILL || transTypeInvoice == PstBillMain.TRANS_TYPE_CASH) {%-->
                                                            <input size="34" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_PAY_AMOUNT]%>" align="right" type="hidden" value="<%=payAmount%>"  class="style5" >
                                                            <!--%} else if (idNotaType == PstBillMain.OPEN_CREDIT_INVOICE || transTypeInvoice == PstBillMain.TRANS_TYPE_CREDIT) {%-->
                                                            <input size="34" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_PAY_AMOUNT_CREDIT]%>" align="right" type="hidden" value="<%=payAmount%>"  class="style5" >

                                                            <!--%}%-->

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td width="15%">
                                                            <span class="style5"><%=textMaterialHeader[SESS_LANGUAGE][21]%></span>
                                                        </td>
                                                        <td align="left" colspan="2" width="20%">
                                                            <input size="34" name="change" align="right" type="text" value="<%=FRMHandler.userFormatStringDecimal(change)%>" class="style5"  onkeypress="javascript:keybrdPressTotal(this, event)" readonly>
                                                            <!--%if (idNotaType == PstBillMain.OPEN_BILL || (idNotaType == PstBillMain.OPEN_ALL_INVOICE && transTypeInvoice == PstBillMain.TRANS_TYPE_CASH)) {%-->
                                                            <input size="34" name="<%=frmCashPayment.fieldNames[FrmCashPayment.FRM_FIELD_AMOUNT_RETURN]%>" align="right" type="hidden" value="<%=FRMHandler.userFormatStringDecimal(change)%>" class="style5"  readonly>
                                                            <!--%} else if (idNotaType == PstBillMain.OPEN_CREDIT_INVOICE || (idNotaType == PstBillMain.OPEN_ALL_INVOICE && transTypeInvoice == PstBillMain.TRANS_TYPE_CREDIT)) {%-->
                                                            <input size="34" name="<%=frmCashCreditPaymentMain.fieldNames[FrmCashCreditPaymentMain.FRM_FIELD_AMOUNT_RETURN_CREDIT]%>" align="right" type="hidden" value="<%=FRMHandler.userFormatStringDecimal(change)%>" class="style5"readonly>
                                                            <!--%}%-->
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <td width="15%" colspan="3" align="left">
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
                                                        <input type="text" name="login_id" size="20" onKeyDown="javascript:keybrdPress(this, event)">
                                                        Password
                                                        <input type="password" name="pass_wd" size="15" onKeyPress="javascript:keybrdPress(this, event)">
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
                                        <tr  valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <table  width="17%" align="left" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <td ></td>
                                                        <td >
                                                        <%--
                                                            <%if (idNotaType != PstBillMain.RETUR_ALL) {%>
                                                                <input type="button" name="Button" value="Save Detail" onClick="javascript:cmdApproval2()"  class="styleButton">
                                                            <%}%>
                                                        --%>
                                                        </td>
                                                        <td ><input type="button" name="Button" value="Add New" onClick="javascript:goNewPage()" class="styleButton"></td>
                                                        <%if(NotaSales!=0 && NotaSales!=1){%>
                                                            <td ><input type="button" name="Button" value="Cancel" onClick="javascript:cancelmain()" class="styleButton"></td>
                                                        <%}%>
                                                        <%if(!privApprovalKasirInv){%>
                                                            <td ><input type="button" name="Button" value="Multiple Payment" onClick="javascript:cmdPaySystemCredit()" class="styleButton"></td>
                                                        <%}%>
                                                        <td >
                                                            <%if (idNotaType == PstBillMain.OPEN_ALL_INVOICE) {%>
                                                            <input type="button" name="Button" value="Print Invoice" onClick="javascript:printFormInvoice()" class="styleButton">
                                                            <%} else {%>
                                                            <input type="button" name="Button" value="Print" onClick="javascript:printFormHtml()" class="styleButton">
                                                            <%}%>
                                                        </td>
                                                        <%--
                                                        <td >
                                                            <input type="button" name="Button" value="List Invoice" onClick="javascript:getListInvoice()" class="styleButton">
                                                        </td>
                                                        --%>
                                                    </tr>
                                                </table>
                                                <table width="30%" align="right" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <td widht="15%">
                                                            <%if(privApprovalKasirPay){%>
                                                            <%if (idNotaType == PstBillMain.RETUR_ALL) {%>
                                                            <input type="button" name="ButtonSavePayment" value="Save Retur" onClick="javascript:confrmSaveRetur(0)" class="styleButton">
                                                            <%} else if (idNotaType == PstBillMain.RETUR) {%>
                                                            <input type="button" name="ButtonSavePayment" align="right" value="Save Retur" onClick="javascript:confrmSaveRetur(1)" class="styleButton">
                                                            <%} else {%>
                                                            <input type="button" name="ButtonSavePayment" value="Save Payment" onClick="javascript:cmdSavePayment()"  class="styleButton">
                                                            <%}%>
                                                            <%}else{%>
                                                            <input type="button" name="ButtonSavePayment" value="Save Invoice" onClick="javascript:cmdSaveAll()"  class="styleButton">
                                                            <%}%>
                                                        </td>
                                                    </tr>
                                                </table>
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
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td></td>
                                        </tr>
                                    </table>
                                   </td>
                            </tr>
                        </table>
                    </form>
                    <script type="text/javascript">
                        <%if (iCommandDetail == Command.ADD ) {%>
                             document.frmcashier.<%=FrmBillDetail.fieldNames[FrmBillDetail.FRM_FIELD_ITEM_NAME]%>.focus();
                        <%}%>
                    </script>
                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%if(menuUsed == MENU_ICON){%>
                        <%@include file="../styletemplate/footer.jsp" %>
                    <%}else{%>
                        <%@ include file = "../main/footer.jsp" %>
                    <%}%>
                </td>
            </tr>
        </table>
    </body>
    <% if ((iCmd == CMD_APPROVAL) && (approvalStatus == 1) && iErrCode == FRMMessage.NONE) {%>
        <script type="text/javascript">
                    cancelmain();
        </script>
    <%}if (iCommandTmp == Command.RESET) {%>
        <script type="text/javascript">
                    cmdAddNew();
        </script>
    <%}if (iCommandPayment == Command.GOTO) {%>
        <script type="text/javascript">
                    var code = document.frmcashier.allTotal.value;
                    var idNotaType = document.frmcashier.nota_type.value;

                    if(code == 1){
                        var tmpcon = "";
                        var con = confirm("Print invoice ? ");
                        if (con ==true)
                        {
                            if(idNotaType == <%=PstBillMain.RETUR%> || idNotaType == <%=PstBillMain.RETUR_ALL%> || <%=!privApprovalKasirPay%>){
                                printFormHtmlRetur();
                            }else{
                                printFormHtmlRetur();
                                //printFormHtml();
                            }
                             cmdAddNew();
                        }
                    }

        </script>
    <%}%>

    <!-- #EndTemplate -->
</html>
