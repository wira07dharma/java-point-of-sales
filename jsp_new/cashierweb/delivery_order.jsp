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
<!--% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_CASHIER);%>
<!--%@ include file = "../main/checkuser.jsp" %>
<!-- JSP Block -->
<%!   /* this constant used to list text of listHeader */
    public static final String textMaterialHeader[][] = {
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Currency", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer", //11
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax"}, //11
        {"Nota Type", "Number", "Customer Type", "Payment Type", "Bookeeping Curreny", "Sales Currency", "Date", "Delivery Address", "Tel/Hp", "Zip", "Customer",
            "Person", "Credit Limit", "Outstanding", "Available", "Sales", "City", "Province", "Country", "Total", "Paid", "Change", "Fax", "Discount", "Tax"}
    };
    public static final String textListOrderItem[][] = {
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",//8
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"},//8
        {"No", "Sku/Barcode", "Item Name", "Quantity", "Price", "Disc", "1.Disc%", "2.Disc%",
            "Disc. Val", "Total Disc.", "Net Price", "Note", "Ser.Number", "Status", "Due Date", "Amount", "Disc. Global", "Unit"}
    };

     public String drawListBillDetail(int language, int iCommandDetail, FrmBillDetail frmObject,
            Billdetail objEntity, Vector objectClass, long billdetailId, int start, int idNotaType) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("60%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textListOrderItem[language][0], "3%");
        ctrlist.addHeader(textListOrderItem[language][1], "13%");
        ctrlist.addHeader(textListOrderItem[language][2], "26%");
        ctrlist.addHeader(textListOrderItem[language][3], "5%");
        ctrlist.addHeader(textListOrderItem[language][17], "5%");

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
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\"><a href=\"javascript:cmdCheckRetur()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                } else {
                    rowx.add("<input type=\"hidden\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_MATERIAL_ID] + "\" value=\"" + (billdetail.getMaterialId())
                            + "\">"
                            + "<input  type=\"hidden\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_UNIT_ID] + "\" value=\"" + billdetail.getUnitId() + "\">"
                            + "<input tabindex=\"31\" type=\"text\" size=\"13\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_SKU] + "\" value=\"" + billdetail.getSku() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\"><a href=\"javascript:cmdCheck()\">CHK</a> | <a href=\"javascript:billDetailDelete('" + String.valueOf(billdetail.getOID()) + "')\">DEL</a>"); //
                }
                rowx.add("<input tabindex=\"32\" type=\"text\" size=\"20\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_ITEM_NAME] + "\" value=\"" + billdetail.getItemName() + "\" onKeyPress=\"javascript:keybrdPress(this, event)\" class=\"formElemenR\" readOnly>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" onKeyUp=\"javascript:calculate()\" name=\"" + frmObject.fieldNames[frmObject.FRM_FIELD_QTY] + "\" value=\"" + (billdetail.getQty()) + "\" onKeyPress=\"javascript:keybrdPress(this, event)\"  class=\"formElemenR\" style=\"text-align:right\"></div>");
                rowx.add("<div align=\"right\"><input tabindex=\"33\" type=\"text\" size=\"3\" name=\"matUnit\" value=\"" + unit.getCode() + "\" class=\"formElemenR\" style=\"text-align:right\" readOnly></div>");
                
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
                    rowx.add("<div align=\"right\"><a  href=\"javascript:goStock('" + billdetail.getOID() + "')\">[SN]</a> " + billdetail.getQty() + "</div>");
                } else {
                    rowx.add("<div align=\"right\">" + billdetail.getQty() + "</div>");
                }
                rowx.add("<div align=\"right\">" + unit.getCode() + "</div>");
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

            double amountTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_AMOUNT]);
            double taxPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]);
            double taxValueTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_VALUE]);
            double discTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_GLOBAL]);
            double discPctTmp = FRMQueryString.requestDouble(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_PCT]);
            int DiscType = FRMQueryString.requestInt(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DISC_TYPE]);

            int vectSize = PstBillMain.getCount(whereClause);
            BillMain billMain = ctrlBillMain.getBillMain();

            msgString = ctrlBillMain.getMessage();            

            // iErrCode = ctrlBillMain.action(iCommand, oidbillmain, idCust);
            if (iCommand == Command.EDIT || iCommand == Command.DELETE) {
                iErrCode = ctrlBillMain.action(iCommand, oidbillmain, idCust);
                billMain = ctrlBillMain.getBillMain();
                oidbillmain = billMain.getOID();
                idCurrency = billMain.getCurrencyId();
                idCustomer = billMain.getCustomerId();
                idTransType = billMain.getTransctionType();
                iCommand = 0;
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

            //Mencari selling Rate
            Vector listSellingRate = PstStandartRate.list(0, 0, PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID] + " ='2'", "");
            if (listSellingRate.size()!=0) {
                StandartRate standartRate = (StandartRate) listSellingRate.get(0);
                sellingRate = standartRate.getSellingRate();
            }

            //Untuk Bill Detail
            CtrlBillDetail ctrlBillDetail = new CtrlBillDetail(request);
            ctrlBillDetail.setLanguage(SESS_LANGUAGE);
            FrmBillDetail frmBillDetail = ctrlBillDetail.getForm();

            iErrCode = ctrlBillDetail.action(iCommandDetail, oidBillDetail, oidbillmain);
            //amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
            //int DiscType = FRMQueryString.requestInt(request, frmBillDetail.fieldNames[frmBillDetail.FRM_FIELD_DISC_TYPE]);

            Billdetail billdetail = ctrlBillDetail.getBillDetail();
            msgString = ctrlBillDetail.getMessage();

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
            
                amounttotal = PstBillDetail.getTotalAmount(oidbillmain);
                whereClauseDetail = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + oidbillmain;           

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
            function cancelmain(){
                var transType = document.frmdlvrorder.sale_type.value;
                // alert(transType);
                var notaType = document.frmdlvrorder.nota_type.value;
                //document.frmdlvrorder.<!--%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;
                document.frmdlvrorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_DOC_TYPE]%>.value = 0;
                document.frmdlvrorder.commandTmp.value="<%=Command.RESET%>";
                //document.frmdlvrorder.action = "delivery_order.jsp";
                //document.frmdlvrorder.submit();
                //alert(transType);
                if(transType == <%=PstBillMain.TRANS_TYPE_CASH%>){
                    cmdCancelOpenBill();
                }else{
                    document.frmdlvrorder.commandTmp.value="<%=Command.RESET%>";
                    cmdCancelBill();
                }

            }

            function cmdApprovalSupervisor(){
                document.frmdlvrorder.commandTmp.value="<%=Command.SAVE%>";
                document.frmdlvrorder.action = "delivery_order.jsp";
                document.frmdlvrorder.submit();
            }

            function cekSupervisor(){
                var oidbillmain = document.frmdlvrorder.oidbillmaintmp.value;
                if(oidbillmain == 0){
                    cmdAddNew();
                }else{
                    document.frmdlvrorder.conf_supervisor.value="0";
                    document.frmdlvrorder.commandTmp.value="<%=Command.APPROVE%>";
                    document.frmdlvrorder.action = "delivery_order.jsp";
                    document.frmdlvrorder.submit();
                }

            }

            function printFormHtml() {
                var printCount = document.frmdlvrorder.print_history.value;
                var oidbillmain = document.frmdlvrorder.oidbillmaintmp.value;
                
                if(oidbillmain == 0){
                    alert("Data is empty !");
                }else{
                    if(printCount > 0){                        
                    document.frmdlvrorder.conf_supervisor.value="1";
                    document.frmdlvrorder.command.value="0";
                    document.frmdlvrorder.commandTmp.value="<%=Command.APPROVE%>";
                    document.frmdlvrorder.action = "delivery_order.jsp";
                    document.frmdlvrorder.submit();
                    }else{
                        window.open("sales_order_print.jsp?hidden_bill_main_id=<%=oidbillmain%>&nota_type=<%=idNotaType%>&command=<%=Command.EDIT%>","salesorderprint","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                        cmdAddNew();
                        
                    }
                    
                }
            }

            function disablefield(){
                var taxCode = document.frmdlvrorder.tax_code.value;
                var taxValue = cleanNumberFloat(document.frmdlvrorder.tax_value.value,guiDigitGroup,guiDecimalSymbol);

                if (taxCode == 0 || taxCode == 1) {
                    document.getElementById('codetax').disabled='disabled';
                    document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TAX_PCT]%>.value = taxValue;
                }else{
                    document.getElementById('codetax').disabled='';
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

                        document.frmdlvrorder.select.value = 2;
                    }

                    function goSubmit(){
                        var limitType = -1;
                        document.frmdlvrorder.notasalestypeTmp.value = limitType;
                        document.frmdlvrorder.submit();
                    }

                    function goStock(oid){
                        var strvalue  = "bill_detail_stockcode.jsp?command=<%=Command.FIRST%>"+
                            "&hidden_bill_detail_id="+oid;
                        window.open(strvalue,"stockcode", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                    }

                    function cmdAdd()
                    {
                        document.frmmatkasir.hidden_bill_detail_id.value="0";
                        document.frmdlvrorder.commandDetail.value="<%=Command.ADD%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdAsk(oidBillDetail)
                    {
                        document.frmdlvrorder.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmdlvrorder.commandDetail.value="<%=Command.ASK%>";
                        document.frmdlvrorder.prev_command.value="<%=prevCommand%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdConfirmDelete(oidBillDetail)
                    {
                        document.frmdlvrorder.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmdlvrorder.commandDetail.value="<%=Command.DELETE%>";
                        document.frmdlvrorder.prev_command.value="<%=prevCommand%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function billDetailEdit(oidBillDetail)
                    {
                        //alert(oidBillDetail);
                        document.frmdlvrorder.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmdlvrorder.commandDetail.value="<%=Command.EDIT%>";
                        document.frmdlvrorder.prev_command.value="<%=prevCommand%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                   
                    function cmdCancel(oidBillDetail)
                    {
                        document.frmdlvrorder.hidden_bill_detail_id.value=oidBillDetail;
                        document.frmdlvrorder.commandDetail.value="<%=Command.EDIT%>";
                        document.frmdlvrorder.prev_command.value="<%=prevCommand%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    //function cmdBack()
                    //{
                    //  document.frmdlvrorder.commandDetail.value="<!--%=Command.BACK%>";
                    // document.frmdlvrorder.action="delivery_order.jsp";
                    //document.frmdlvrorder.submit();
                    //}

                    function cmdListFirst()
                    {
                        document.frmdlvrorder.commandDetail.value="<%=Command.FIRST%>";
                        document.frmdlvrorder.prev_command.value="<%=Command.FIRST%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdListPrev()
                    {
                        document.frmdlvrorder.commandDetail.value="<%=Command.PREV%>";
                        document.frmdlvrorder.prev_command.value="<%=Command.PREV%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdListNext()
                    {
                        document.frmdlvrorder.commandDetail.value="<%=Command.NEXT%>";
                        document.frmdlvrorder.prev_command.value="<%=Command.NEXT%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdListLast()
                    {
                        document.frmdlvrorder.commandDetail.value="<%=Command.LAST%>";
                        document.frmdlvrorder.prev_command.value="<%=Command.LAST%>";
                        document.frmdlvrorder.action="delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdChangeMember(){
                        document.frmdlvrorder.submit();
                    }

                    function cmdAdd2(){
                        document.frmdlvrorder.commandDetail.value="<%=Command.ADD%>";
                        document.frmdlvrorder.action = "delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdAddNew(){
                        var resetvalue = 0;
                        var codecust = cleanNumberFloat(document.frmdlvrorder.code_cust.value,guiDigitGroup,guiDecimalSymbol);

                        if(codecust == 3){
                            document.frmdlvrorder.outstanding.value=resetvalue;
                            document.frmdlvrorder.available.value=resetvalue;
                            document.frmdlvrorder.creditlimit.value=resetvalue;
                            document.frmdlvrorder.availableTmp.value = resetvalue;
                        }

                        document.frmdlvrorder.hidden_bill_main_id.value = resetvalue;
                        document.frmdlvrorder.oidbillmaintmp.value = resetvalue;
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>.value = "";
                        document.frmdlvrorder.<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>.value = "";
                        document.frmdlvrorder.notasalestype.value = 0;
                        document.frmdlvrorder.code_cust.value = resetvalue;
                        document.frmdlvrorder.code_custTmp.value = resetvalue;
                        document.frmdlvrorder.available_credit.value=resetvalue;
                        document.frmdlvrorder.commandDetail.value=resetvalue;
                        document.frmdlvrorder.commandTmp.value=resetvalue;
                        document.frmdlvrorder.vectSize.value=resetvalue;
                        document.frmdlvrorder.start.value=resetvalue;
                        document.frmdlvrorder.prev_command.value=resetvalue;
                        document.frmdlvrorder.start_detail.value=resetvalue;
                        document.frmdlvrorder.hidden_bill_detail_id.value=resetvalue;
                        document.frmdlvrorder.sale_type.value=resetvalue;
                        document.frmdlvrorder.CurrId.value=resetvalue;
                        document.frmdlvrorder.nota_type.value=resetvalue;
                        document.frmdlvrorder.billdetailTmp.value=resetvalue;
                        document.frmdlvrorder.trans_type.value=resetvalue;
                        document.frmdlvrorder.notasalestypeTmp.value="-1";
                        document.frmdlvrorder.commandDetail.value=resetvalue;
                        document.frmdlvrorder.print_history.value=resetvalue;
                        document.frmdlvrorder.conf_supervisor.value=resetvalue;
                        document.frmdlvrorder.action = "delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function cmdBack(){
                        document.frmdlvrorder.command.value="<%=Command.BACK%>";
                        document.frmdlvrorder.action="deliveryOrder_list.jsp";
                        document.frmdlvrorder.submit();                       
                    }

                    function cmdCancelBill(){
                        var oidbillmain = document.frmdlvrorder.oidbillmaintmp.value;
                        document.frmdlvrorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_PARENT_ID]%>.value = oidbillmain ;
                        document.frmdlvrorder.<%=FrmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANSACTION_STATUS]%>.value = 2;

                        document.frmdlvrorder.command.value="<%=Command.CANCEL%>";
                        //document.frmdlvrorder.commandTmp.value="<!--%=Command.RESET%>";
                        document.frmdlvrorder.action =  "delivery_order.jsp";
                        document.frmdlvrorder.submit();
                    }

                    function closeshift(){
                        document.frmdlvrorder.action = "close_shift.jsp";
                        document.frmdlvrorder.submit();
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
        </style>
        <!-- #EndEditable -->
    </head>

    <body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="noBack();"
          onpageshow="if (event.persisted) noBack();" onunload="">

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
                    <form name="frmdlvrorder" method ="post" action="">
                        <input type="hidden" name="command" value="<%=iCommand%>">
                        <input type="hidden" name="commandDetail" value="<%=iCommandDetail%>">
                        <input type="hidden" name="commandTmp" value="<%=iCommandTmp%>">

                        <input type="hidden" name="vectSize" value="<%=vectSize%>">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">

                        <input type="hidden" name="start" value="<%=start%>">
                        <input type="hidden" name="start_detail" value="<%=startDetail%>">

                        <input type="hidden" name="hidden_bill_main_id" value="<%=oidBillMain%>">
                        <input type="hidden" name="hidden_bill_detail_id" value="<%=oidBillDetail%>">
                        <input type="hidden" name="oidbillmaintmp" value="<%=oidbillmain%>">
                        <input type="hidden" name="sale_type" value="<%=idSaleType%>">

                        <input type="hidden" name="CurrId" value="<%=idCurrency%>">
                        <input type="hidden" name="nota_type" value="<%=idNotaType%>">
                        <input type="hidden" name="billdetailTmp" value="<%=idbilldetailTmp%>">
                        <input type="hidden" name="trans_type" value="<%=idTransType%>">
                        <input type="hidden" name="available_credit" value="<%=available%>">
                        <input type="hidden" name="print_history" value="<%=printHistory%>">
                        <input type="hidden" name="conf_supervisor" value="<%=confirmSupervisor%>">

                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Sales Order by <%=userName%> <!-- #EndEditable --> </td>
                            </tr
                            <tr>
                                <td height="20" align="left">
                                    <table width="98%" border="0" cellspacing="0" cellpadding="0" align="left">
                                        <tr>
                                            <td align="right">

                                                <div align="right"><input type="button" name="Closing Shift" value="Closing Shift" onClick="javascript:closeshift()" class="styleButton"> <a href="../logout.jsp">  Logout </a></div>
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
                                                                            <%      Vector val_notatype = new Vector(1, 1);
                                                                                        Vector key_notatype = new Vector(1, 1);

                                                                                        val_notatype.add("" + PstBillMain.NONE);
                                                                                        key_notatype.add("Sales Order");
                                                                                        val_notatype.add("" + PstBillMain.RETUR);
                                                                                        key_notatype.add("Sales Retur");
                                                                                        //val_notatype.add("" + PstBillMain.GIFT);
                                                                                        //key_notatype.add("Gift");

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
                                                                                        var notaType = document.frmdlvrorder.notaSalesType.value;
                                                                                        document.frmdlvrorder.notasalestypeTmp.value = limitType;
                                                                                        if(notaType==<%=PstBillMain.RETUR%>){
                                                                                            strvalue  = "srcRetur.jsp?command=<%=Command.FIRST%>"+
                                                                                                "&trans_type=1";
                                                                                            window.open(strvalue,"customer2", "height=1000,width=1200,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
                                                                                        }
                                                                            </script>
                                                                            <% }
                                                                            %>
                                                                            <%=ControlCombo.draw("notasalestype", null, (NotaSales == 0) ? NotaSalesCode : "" + NotaSales, val_notatype, key_notatype, "onChange=\"javascript:goSubmit()\"", "formElemen")%>
                                                                            <%if (NotaSales == PstBillMain.RETUR && iCommandDetail != Command.ADD) {%>
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
                                                                                        if (billMain.getCustomerId() != 0) {
                                                                                            Vector listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='1'", "");
                                                                                            MemberGroup memberGroupCode = (MemberGroup) listCodeCustomer.get(0);

                                                                                            if (billMain.getCustomerId() == memberGroupCode.getOID()) {
                                                                                                select_Customer = "" + memberGroupCode.getOID();
                                                                                            } else {
                                                                                                listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_GROUP_TYPE] + " ='3'", "");
                                                                                                MemberGroup memberGroup = (MemberGroup) listCodeCustomer.get(0);
                                                                                                select_Customer = "" + memberGroup.getOID();
                                                                                                codeCustomer = 3;
                                                                                            }

                                                                                        } else {
                                                                                            if (selectedCustomerId != 0) {
                                                                                                select_Customer = "" + selectedCustomerId;
                                                                                                Vector listCodeCustomer = PstMemberGroup.list(0, 0, PstMemberGroup.fieldNames[PstMemberGroup.FLD_MEMBER_GROUP_ID] + " ='" + selectedCustomerId + "'", "");
                                                                                                MemberGroup memberGroupCode = (MemberGroup) listCodeCustomer.get(0);
                                                                                                codeCustomer = memberGroupCode.getGroupType();
                                                                                            }
                                                                                        }



                                                                            %>
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CUSTOMER_ID], "formElemen", null, select_Customer, vectCustomerVal, vectCustomerKey, "onChange=\"javascript:cmdChangeMember()\"")%>
                                                                            <input name="code_cust" type="hidden" size="12"  value="<%=codeCustomer%>"></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][3]%></td>
                                                                        <td><%      Vector val_saletype = new Vector(1, 1);
                                                                                    Vector key_saletype = new Vector(1, 1);

                                                                                    if (codeCustomer == 3) {
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
                                                                            <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_TRANS_TYPE], null, (billMain.getTransctionType() == 0) ? select_saletype : "" + billMain.getTransctionType(), val_saletype, key_saletype, "", "formElemen")%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][15]%></td>

                                                                        <td><%=userName%></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][4]%></td>
                                                                        <td>Rp. at <%=FRMHandler.userFormatStringDecimal(sellingRate)%>/USD</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][5]%></td>
                                                                        <td><label><%
                                                                                    long currencyId = FRMQueryString.requestLong(request, frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID]);
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
                                                                                <%=ControlCombo.draw(frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_CURRENCY_ID], "formElemen", null, ("" + billMain.getCurrencyId() == "") ? select_cur : "" + billMain.getCurrencyId(), vectCurrVal, vectCurrKey, "")%>

                                                                                at <%=FRMHandler.userFormatStringDecimal(sellingRate)%>/USD </label></td>
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
                                                                                <textarea name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ADDRESS]%>" cols="32" rows="2" id="shAddrs" tabindex="5"><%=(shippAddr == "") ? billMain.getShippingAddress() : shippAddr%></textarea>
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
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_CITY]%>" type="text" id="shcity" size="12" tabindex="6"  value="<%=(shippCity == "") ? billMain.getShippingCity() : shippCity%>">,</td>
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PROVINCE]%>" type="text" id="shprov" size="12" tabindex="7" value="<%=(shippProv == "") ? billMain.getShippingProvince() : shippProv%>">,</td>
                                                                                    <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_COUNTRY]%>" type="text" id="shcountry" size="12" tabindex="8" value="<%=(shippCountry == "") ? billMain.getShippingCountry() : shippCountry%>"></td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][8]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_PH_NUMBER]%>" type="text" id="shtlp" size="15" tabindex="9" value="<%=(shippTlp == "") ? billMain.getShippingPhoneNumber() : shippTlp%>">

                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][22]%></td>
                                                                        <td><label>
                                                                                <input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_FAX]%>" type="text" id="shfax" size="15" tabindex="10" value="<%=(shippFax == "") ? billMain.getShippingFax() : shippFax%>">

                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][9]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_SHIPPING_ZIP]%>" type="text" id="shzip" size="10" maxlength="10" tabindex="11" value="<%=(shippZip == "") ? billMain.getShippingZipCode() : shippZip%>"></td>
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
                                                                                <input name="cusId" type="hidden" value="<%=billMain.getCustomerId()%>" size="15" maxlength="64" >
                                                                                <%
                                                                                                                                                                /*  MemberReg memberReg = new MemberReg();
                                                                                                                                                                try {
                                                                                                                                                                memberReg = PstMemberReg.fetchExc(billMain.getCustomerId());
                                                                                                                                                                } catch (Exception e) {
                                                                                                                                                                System.out.println("Customer not found ...");
                                                                                                                                                                }*/
                                                                                                                                                                //list member
                                                                                                                                                                MemberReg memberReg = new MemberReg();
                                                                                                                                                                Vector listMemberReg = PstMemberReg.list(start, recordToGet, "CNT." + PstMemberReg.fieldNames[PstMemberReg.FLD_CONTACT_ID] + " = '" + billMain.getCustomerId() + "'", orderClause);
                                                                                                                                                                if (listMemberReg.size() > 0) {
                                                                                                                                                                    memberReg = (MemberReg) listMemberReg.get(0);
                                                                                                                                                                }
                                                                                                                                                                String address = "";
                                                                                                                                                                if(memberReg.getBussAddress()!= null && memberReg.getBussAddress().length() > 0){
                                                                                                                                                                    address = memberReg.getBussAddress();
                                                                                                                                                                }else{
                                                                                                                                                                    address = memberReg.getHomeAddr();
                                                                                                                                                                }
                                                                                                                                                                //mencari credit limit
                                                                                                                                                                double totalKredit = PstBillMain.getTotalKredit(billMain.getCustomerId());
                                                                                                                                                                double totalRetur = PstBillMain.getReturnKredit(billMain.getCustomerId());
                                                                                                                                                                outstanding = totalKredit - totalRetur;

                                                                                                                                                                creditLimit = memberReg.getMemberCreditLimit();
                                                                                                                                                                available = creditLimit - outstanding;
                                                                                %>
                                                                                <input name="compName" type="text" value="<%=memberReg.getCompName()%>" id="namecomp" size="15" maxlength="64" tabindex="12">
                                                                                <%if (iCommandDetail != Command.ADD) {%>
                                                                                <a href="javascript:cmdCheck2()">CHK</a>
                                                                                <%}%>
                                                                            </label></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                        <td><!--input name="compAddr" type="text" value="<--%= address == "" ? compAddress : address%>" size="15" maxlength="64" tabindex="13" class="hiddenLabel"-->
                                                                            <label >
                                                                                <textarea name="compAddr" cols="17" rows="1" tabindex="13" class="hiddenLabel"><%= address == "" ? compAddress : address%></textarea>
                                                                            </label>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td><%=textMaterialHeader[SESS_LANGUAGE][11]%></td>
                                                                        <td><input name="<%=frmBillMain.fieldNames[FrmBillMain.FRM_FIELD_GUEST_NAME]%>" type="text" value="<%=billMain.getGuestName()%>" id="nameperson" size="15" maxlength="64" tabindex="14"></td>
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
                                                                            <input name="availableTmp" type="hidden" value="<%=available%>" >
                                                                        </td>
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
                                                            <td>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </fieldset>
                                            </td>
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
                                        <tr valign="top">
                                            <td height="22" valign="middle" colspan="3">
                                                <table width="17%" align="left" border="0" cellspacing="2" cellpadding="3">
                                                    <tr>
                                                        <td ></td>
                                                        <td ><input type="button" name="Button" value="Back to List" onClick="javascript:cmdBack()" class="styleButton"></td>                                                            
                                                        <td ><input type="button" name="Button" value="Print" onClick="javascript:printFormHtml()" class="styleButton"></td>
                                                        <td ></td>
                                                    </tr>
                                                </table>                                                
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

                </td>
            </tr>
            <tr>
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
                    <%@ include file = "../main/footer.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
    </body>    
    <!-- #EndTemplate -->
</html>
