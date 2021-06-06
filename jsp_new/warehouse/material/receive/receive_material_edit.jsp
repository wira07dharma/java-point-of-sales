<%-- 
    Document   : receive_material_edit
    Created on : Jan 26, 2020, 12:31:33 AM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.common.entity.location.Location"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.qdep.form.FRMHandler"%>
<%@page import="com.dimata.posbo.entity.warehouse.ForwarderInfo"%>
<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.common.entity.payment.PstDailyRate"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactClass"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.session.masterdata.SessPosting"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmMatReceive"%>
<%@page import="com.dimata.qdep.entity.I_ApplicationType"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.session.warehouse.SessForwarderInfo"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>

<%
    int appObjCodeShowQtyAndPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_QTY_AND_PRICE);
    int  appObjCodeAdditionalPrice = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE_PAYMENT);
    //boolean privApproval = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalApprove = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_APPROVE));
    boolean privApprovalFinal = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_FINAL));
    boolean privShowQtyPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeShowQtyAndPrice, AppObjInfo.COMMAND_VIEW));
    boolean privAdditionalPrice = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeAdditionalPrice, AppObjInfo.COMMAND_VIEW));
%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int startItem = FRMQueryString.requestInt(request, "start_item");
    int cmdItem = FRMQueryString.requestInt(request, "command_item");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    long oidReceiveMaterial = FRMQueryString.requestLong(request, "hidden_receive_id");
    long oidForwarderInfo = FRMQueryString.requestLong(request, "hidden_forwarder_id");
    int iCommandPosting = FRMQueryString.requestInt(request, "iCommandPosting");
    int cekBeratStatus = FRMQueryString.requestInt(request, "cek_berat");
//
//   if(iCommand == Command.SAVE && oidReceiveMaterial != 0){
//        iCommand = Command.NONE;
//    }
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
    String syspropPenerimaanHpp = PstSystemProperty.getValueByName("SHOW_PENERIMAAN_HPP");
    int includePpn = FRMQueryString.requestInt(request, "include_ppn");
    double defaultPpn = Double.parseDouble(PstSystemProperty.getValueByName("POS_PPN_DEFAULT"));
    long oidCurrencyRec = FRMQueryString.requestLong(request, "hidden_currency_id");
    String receiveDate = FRMQueryString.requestString(request, "FRM_FIELD_RECEIVE_DATE");

    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    ControlLine ctrLine = new ControlLine();
    CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
    iErrCode = ctrlMatReceive.action(iCommand, oidReceiveMaterial, userName, userId, request);
    FrmMatReceive frmMatReceive = ctrlMatReceive.getForm();
    MatReceive rec = ctrlMatReceive.getMatReceive();
    errMsg = ctrlMatReceive.getMessage();
//posting
//proses posting stock
    int typeOfBussiness = Integer.parseInt(PstSystemProperty.getValueByName("TYPE_OF_BUSINESS"));
//typer reail
    if (typeOfBussiness == I_ApplicationType.APPLICATION_DISTRIBUTIONS) {
        SessPosting sessPosting = new SessPosting();
        switch (iCommandPosting) {
            case Command.POSTING:
                boolean isOKP = sessPosting.postedQtyReceiveOnlyDoc(oidReceiveMaterial);
                if (isOKP) {
                    rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_APPROVED);
                }
                break;
            case Command.REPOSTING:
                boolean isOK = sessPosting.postedValueReceiveOnlyDoc(oidReceiveMaterial);
                if (isOK) {
                    rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                }
                break;

            default:
            // break;
        }
//maka statusnya final = posting value
    } else {
        //type kecuali retail, klo final langsung posting
        if (iCommandPosting == Command.POSTING) {
            SessPosting sessPosting = new SessPosting();
            boolean isOK = sessPosting.postedReceiveDoc(oidReceiveMaterial, userName, userId);
            if (isOK) {
                rec.setReceiveStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
            }
        }
    }

    String priceCode = "Rp.";
    int start = 0; 
    int record = 0; 
    String whereClause = ""; 
    String order = ""; 

    long oidCurrencyx = 0;
    double resultKonversi = 0.0; 
    oidCurrencyRec = rec.getCurrencyId();
    oidReceiveMaterial = rec.getOID();
    int recordToGetItem = 0;
    String whereClauseItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=0";

    String orderClauseItem = " RMI." + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ITEM_ID];
    if (typeOfBusinessDetail == 2) {
        orderClauseItem = " RIGHT(MAT." + PstMaterial.fieldNames[PstMaterial.FLD_SKU] + ",7) ASC";
    }
    int vectSizeItem = PstMatReceiveItem.getCount(whereClauseItem);

    Vector listMatReceiveItem = new Vector();

    if (rec != null && rec.getOID() != 0) {
        listMatReceiveItem = PstMatReceiveItem.listVectorRecItemComplete(startItem, recordToGetItem, whereClauseItem, orderClauseItem);
    }

    double totalBeratItem = 0;
    if (!listMatReceiveItem.isEmpty()) {
        for (int i = 0; i < listMatReceiveItem.size(); i++) {
            Vector temp = (Vector) listMatReceiveItem.get(i);
            MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
            totalBeratItem += recItem.getBerat();
        }
    }

    Vector vctForwarderInfo = new Vector(1, 1);
    try {
        if (oidReceiveMaterial != 0) {
            vctForwarderInfo = SessForwarderInfo.getObjForwarderInfo(oidReceiveMaterial);
        }
    } catch (Exception e) {
    }

    double totalForwarderCost = SessForwarderInfo.getTotalCost(oidReceiveMaterial);


    String enableInput = "";
    boolean privManageData = true;
    if (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT) {
        privManageData = false;
        enableInput = "readonly";
    }

    String whereClauseBonusItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial
            + " AND " + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_BONUS] + "=1";

    Vector listMatReceiveBonusItem = new Vector();
    if (rec != null && rec.getOID() != 0) {
        listMatReceiveBonusItem = PstMatReceiveItem.listVectorRecItemComplete(0, 0, whereClauseBonusItem, orderClauseItem);
    }
     String input = "";
          if ((rec.getCurrencyId() == 0)) {
          resultKonversi = PstDailyRate.getCurrentDailyRateSales(oidCurrencyx);
        } else {
          input = "disabled";
        }
    Vector <Location> listLocation = PstLocation.list(start, record, whereClause, order);
    String where  = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]
                    + " = " + PstContactClass.CONTACT_TYPE_SUPPLIER
                    + " AND " + PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]
                    + " != " + PstContactList.DELETE;
    Vector <ContactList> listSupplier = PstContactList.listContactByClassType(0, 0, where, PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);

    Vector <CurrencyType> listCurr = PstCurrencyType.list(0, 0, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS] + "=" + PstCurrencyType.INCLUDE, "");

%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%@include file="../../../styles/plugin_component.jsp" %>
        <style>

            .select2-container--default .select2-selection--multiple {
                background-color: white;
                border: 1px solid #d2d6de !important;
                border-radius: 0px !important;
                cursor: text;
            }
            .select2-container--default .select2-selection--single{
                background-color: white;
                border: 1px solid #d2d6de !important;
                border-radius: 0px !important;
                cursor: text;
            }
            .select2-container--default .select2-selection--multiple .select2-selection__choice {
                background-color: #6b7ae6 !important;
                border: 1px solid #ffffff00 !important;
                border-radius: 2px !important;
                color: white !important;
                cursor: pointer !important;
                float: left;
                margin-right: 5px;
                margin-top: 5px;
                padding: 0 5px;
            }
            .select2-container--default .select2-selection--multiple .select2-selection__choice__remove {
                color: #fff !important;
                cursor: pointer;
                display: inline-block;
                font-weight: bold;
                margin-right: 2px;
            }
            .select2-container {
                box-sizing: border-box;
                display: inline-block;
                width: 100% !important;
                margin: 0;
                position: relative;
                vertical-align: middle;
            }
            .input-group {
                position: relative;
                display: inline-block !important;
                border-collapse: separate;
            }
            .font-small {
                font-size: 12px !important;
            }
            .min-form {
                min-width: auto;
                min-height: 70px !important;
            }
            .input-group-addon {
                padding: 6px 12px;
                font-size: 14px;
                font-weight: 400;
                line-height: 1;
                color: #555;
                text-align: center;
                background-color: #eee;
                border: 1px solid #ccc !important;
            }
            .font-12 {
                font-size: 12px;
            }
            select.form-control.input-sm {
                width: 33.33333%;
            }
            span.select2-selection.select2-selection--single {
                font-size: 11px;
            }
        </style>
        <script>
            $(document).ready(function () {

            //Timepicker
            $('.timepicker').timepicker({
              showInputs : true,
               showMeridian: false,
               format: 'hh:mm'
            });
    //        Date Picker
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });

                $('input[type="checkbox"].flat-blue').iCheck({
                    checkboxClass: 'icheckbox_square-blue'
                });

                $('.select2').select2({placeholder: "Semua"});
                $('.selectAll').select2({placeholder: "Semua"});

                //MODAL SETTING
                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };

            $('#simpan-receive').click(function (){
                cmdSave();
            });
            $('#tambah-item').click(function (){
                addItem();
            });
            $('#tambah-forward').click(function (){
                addForwarderInfo();
            });
            $('#hapus-receive').click(function (){
                cmdConfirmDelete('<%=oidReceiveMaterial%>');
            });
            $('#kembali-list').click(function (){
                cmdBack();
            });
            });
            
        var windowSupplier;
        function addSupplier(){
            windowSupplier=window.open("../../../master/contact/contact_company_edit.jsp?command=<%=Command.ADD%>&source_link=receive_material_edit.jsp","add_supplier", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            if (window.focus) { windowSupplier.focus();}
        }

        function  setSupplierOnLGR(textIn, supOID){
            var oOption = self.opener.document.createElement("OPTION");
            oOption.text=textIn;
            oOption.value="supOID";
            document.forms.frmReceive.FRM_FIELD_SUPPLIER_ID.add(oOption);
            document.forms.frmReceive.FRM_FIELD_SUPPLIER_ID.value = "504404432982825708";
            changeFocus(self.opener.document.forms.frmReceive.FRM_FIELD_SUPPLIER_ID);
        }

        function cmdEdit(oid){
    
            document.frmReceive.command.value="<%=Command.EDIT%>";
            document.frmReceive.prev_command.value="<%=prevCommand%>";
            document.frmReceive.action="receive_material_edit.jsp";
            document.frmReceive.submit();
        }

        function compare(){
            var dt = document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_dy.value;
            var mn = document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_mn.value;
            var yy = document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE]%>_yr.value;
            var dt = new Date(yy,mn-1,dt);
            var bool = new Boolean(compareDate(dt));
            return bool;
        }

        function cmdNewDelete(oid){
        var msg;
        msg= "Are You Sure to Delete This Data? ";
        var agree=confirm(msg);
        if (agree)
        return cmdConfirmDeleteItem(oid) ;
        else
        return cmdEdit(oid);
        }

        function cmdConfirmDeleteItem(oidReceiveMaterialItem) {
            document.frmReceive.hidden_receive_item_id.value=oidReceiveMaterialItem;
            document.frmReceive.command.value="<%=Command.DELETE%>";
            document.frmReceive.prev_command.value="<%=prevCommand%>";
            document.frmReceive.approval_command.value="<%=Command.DELETE%>";
            document.frmReceive.action="receive_material_edit.jsp";
            document.frmReceive.submit();
        }

        function cmdSave() {      
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                var invNo = document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>.value;
               if(invNo!=''){
                    var statusDoc = document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>.value;
            <%if (typeOfBusiness.equals("3")) {%>
                         if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" && <%=privApprovalFinal%>==false){
                             var conf = confirm("Are You Sure to Posting Stock ? ");
                             if(conf){
                                document.frmReceive.command.value="<%=Command.SAVE%>";
                                document.frmReceive.iCommandPosting.value="<%=Command.POSTING%>";
                                document.frmReceive.prev_command.value="<%=prevCommand%>";
                                document.frmReceive.action="receive_material_edit.jsp";
                                if(compare()==true)
                                            document.frmReceive.submit();
                             }
                        }else if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                             var conf = confirm("Are You Sure to Posting Cost Price?");
                             if(conf){
                                document.frmReceive.command.value="<%=Command.SAVE%>";
                                document.frmReceive.iCommandPosting.value="<%=Command.REPOSTING%>";
                                document.frmReceive.prev_command.value="<%=prevCommand%>";
                                document.frmReceive.action="receive_material_edit.jsp";
                                if(compare()==true)
                                            document.frmReceive.submit();
                             }
                        }else{
                                document.frmReceive.command.value="<%=Command.SAVE%>";
                                document.frmReceive.prev_command.value="<%=prevCommand%>";
                                document.frmReceive.action="receive_material_edit.jsp";
                                if(compare()==true)
                                document.frmReceive.submit();
                        }   
                    <% } else {%>
                        if(statusDoc=="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>"){
                             var conf = confirm("Are You Sure to Posting Stock ? ");
                             if(conf){
                                document.frmReceive.command.value="<%=Command.SAVE%>";
                                document.frmReceive.prev_command.value="<%=prevCommand%>";
                                document.frmReceive.iCommandPosting.value="<%=Command.POSTING%>";
                                document.frmReceive.action="receive_material_edit.jsp";
                                if(compare()==true)
                                            document.frmReceive.submit();
                             }
                        }else{
                            document.frmReceive.command.value="<%=Command.SAVE%>";
                            document.frmReceive.prev_command.value="<%=prevCommand%>";
                            document.frmReceive.action="receive_material_edit.jsp";
                            if(compare()==true)
                                        document.frmReceive.submit();
                        }
                            <%}%>
                }else{
                    alert("Nomor invoice supplier harus diisi");
                }
                            <%
                            } else {
                            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                            <%
                                }
                            %>
        }

        function cmdAsk(oid) {
            <% 
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frmReceive.command.value="<%=Command.ASK%>";
                document.frmReceive.prev_command.value="<%=prevCommand%>";
                document.frmReceive.action="receive_material_edit.jsp";
                document.frmReceive.submit();
                <%
                } else {
                %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
                <%
                    }
                %>
        }

        function gostock(oid) {
            document.frmReceive.command.value="<%=Command.EDIT%>";
            document.frmReceive.rec_type.value = 2;
            document.frmReceive.type_doc.value = 2;
            document.frmReceive.hidden_receive_item_id.value=oid;
            document.frmReceive.action="rec_wh_stockcode.jsp";
            document.frmReceive.submit();
        }

        function cmdCancel() {
            document.frmReceive.command.value="<%=Command.CANCEL%>";
            document.frmReceive.prev_command.value="<%=prevCommand%>";
            document.frmReceive.action="receive_material_edit.jsp";
            document.frmReceive.submit();
        }

        function cmdConfirmDelete(oid) {
            if(confirm('Are you sure want to delete this document?')){
            document.frmReceive.command.value="<%=Command.DELETE%>";
            document.frmReceive.prev_command.value="<%=prevCommand%>";
            document.frmReceive.approval_command.value="<%=Command.DELETE%>";
            document.frmReceive.action="receive_material_edit.jsp";
            document.frmReceive.submit();
        }
        }

        function cmdBack() {
            document.frmReceive.command.value="<%=Command.FIRST%>";
            document.frmReceive.prev_command.value="<%=prevCommand%>";
            document.frmReceive.action="search_receive_material.jsp";
            document.frmReceive.submit();
        }

        function printExcel() {
            var typePrint = document.frmReceive.type_print_tranfer.value;
            window.open("receive_wh_supp_material_print_form.jsp?printExcel=1&hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
        }

        function printForm() {
            var typePrint = document.frmReceive.type_print_tranfer.value;
            <%if (typeOfBusinessDetail == 2) {%>
                window.open("print_out_receive_wh_supp_material_item.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.LIST%>&type_print_tranfer="+typePrint+"","receivereport");
            <%} else {%>
                window.open("receive_wh_supp_material_print_form.jsp?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>&type_print_tranfer="+typePrint+"","receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            <%}%>    
        }

        function PostingStock() {
            var conf = confirm("Are You Sure to Posting Stock ? ");
            if(conf){
                document.frmReceive.command.value="<%=Command.POSTING%>";
                document.frmReceive.prev_command.value="<%=prevCommand%>";
                document.frmReceive.action="receive_material_edit.jsp";
                document.frmReceive.submit();
                }
        }

        function PostingCostPrice() {
            var conf = confirm("Are You Sure to Posting Cost Price?");
            if(conf){
                document.frmReceive.command.value="<%=Command.REPOSTING%>";
                document.frmReceive.prev_command.value="<%=prevCommand%>";
                document.frmReceive.action="receive_material_edit.jsp";
                document.frmReceive.submit();
                }
        }

        function addForwarderInfo() {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frmReceive.command.value="<%=Command.EDIT%>";
                document.frmReceive.action="forwarder_info.jsp";
                document.frmReceive.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }

        function editForwarderInfo(){
            document.frmReceive.command.value="<%=Command.EDIT%>";
            document.frmReceive.action="forwarder_info.jsp";
            document.frmReceive.submit();
        }

        function addReceivePayable(oid) {
    
                document.frmReceive.command.value="<%=Command.ADD%>";
                document.frmReceive.oid_invoice_selected.value=oid;
                document.frmReceive.action="../../../arap/payable/payable_view.jsp";
                document.frmReceive.submit();
        }

        function printBarcode(){
            var con = confirm("Print Barcode ? ");
            if (con ==true)
            {
                window.open("<%=approot%>/servlet/com.dimata.posbo.printing.warehouse.PrintBarcode?hidden_receive_id=<%=oidReceiveMaterial%>&command=<%=Command.EDIT%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
            }
        }

        function changeCurrency(value){
            //alert("asa");
            var oidCurrencyId=value;
            checkAjax(oidCurrencyId);
        }

        function checkAjax(oidCurrencyId){
            $.ajax({
            url : "<%=approot%>/servlet/com.dimata.posbo.ajax.CurrentDailyRateRunning?FRM_FIELD_RATE="+oidCurrencyId+"&typeCheckCurrency=1",
            type : "POST",
            async : false,
            success : function(data) {
                 document.frmReceive.<%=FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_TRANS_RATE]%>.value=data;
                 //document.frm_purchaseorder.exchangeRate.value=data;
            }
        });
        }
        function addItem() {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frmReceive.command.value="<%=Command.ADD%>";
                document.frmReceive.action="receive_wh_supp_materialitem.jsp";
                if(compareDateForAdd()==true)
                    document.frmReceive.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }
		function importItem(oid){
			window.open("import_item_excel.jsp?receive_material_id="+oid,"receivereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
		}

        function editItem(oid) {
            <%
                if ((rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_POSTED) && (rec.getReceiveStatus() != I_DocStatus.DOCUMENT_STATUS_CLOSED)) {
            %>
                document.frmReceive.command.value="<%=Command.EDIT%>";
                document.frmReceive.hidden_receive_item_id.value=oid;
                document.frmReceive.action="receive_wh_supp_materialitem.jsp";
                document.frmReceive.submit();
            <%
            } else {
            %>
                    alert("Document has been <%=I_DocStatus.fieldDocumentStatus[rec.getReceiveStatus()]%> !!!");
            <%
                }
            %>
        }
        function MM_swapImgRestore() { //v3.0
                var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
        }

        function MM_preloadImages() { //v3.0
                var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
                var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
                if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
        }

        function MM_swapImage() { //v3.0
                var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
                if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
        }
        //------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

        function MM_findObj(n, d) { //v4.01
          var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
            d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
          if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
          for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
          if(!x && d.getElementById) x=d.getElementById(n); return x;
        }

        function viewHistoryTable() {
            var strvalue ="../../../main/historypo.jsp?command=<%=Command.FIRST%>"+
                             "&oidDocHistory=<%=oidReceiveMaterial%>";
            window.open(strvalue,"material", "height=600,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
        }

        function formApproval(recMatId) {    
            var winApproval = window.open("receive_approval_form.jsp?rec_mat_id="+recMatId, "material", "height=300,width=400,left=500,top=100,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes,resizing=false");
            if (window.focus) {
                winApproval.focus();
            }
        }
        </script>
    </head>
    <body>
        <section class="content-header">
            <h1>Penerimaan<small> Edit</small> </h1>
            <ol class="breadcrumb">
                <li>Penerimaan</li>
                <li>Non PO</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-prochain">
                <div class="box-header with-border">
                    <label class="box-title pull-left">Input Penerimaan</label>
                </div>
                <div class="box-body">
                    <form name="frmReceive" id="frmReceive" method="post" action="">
                        <input type="hidden" name="command" value="">
                        <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                        <input type="hidden" name="start_item" value="<%=startItem%>">
                        <input type="hidden" name="rec_type" value="">
                        <input type="hidden" name="type_doc" value="">
                        <input type="hidden" name="command_item" value="<%=cmdItem%>">
                        <input type="hidden" name="approval_command" value="<%=appCommand%>">
                        <input type="hidden" name="hidden_receive_id" value="<%=oidReceiveMaterial%>">
                        <input type="hidden" name="hidden_forwarder_id" value="<%=oidForwarderInfo%>">
                        <input type="hidden" name="hidden_receive_item_id" value="">
                        <input type="hidden" name="hidden_currency_id" value="<%=rec.getCurrencyId()%>">
                        <input type="hidden" name="hidden_currency_id" value="<%=oidCurrencyRec%>">
                        <input type="hidden" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_REC_CODE]%>" value="<%=rec.getRecCode()%>">
                        <input type="hidden" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
                        <input type="hidden" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=(rec.getReceiveSource() == PstMatReceive.SOURCE_FROM_BUYBACK ? PstMatReceive.SOURCE_FROM_BUYBACK : PstMatReceive.SOURCE_FROM_SUPPLIER)%>">
                        <input type="hidden" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_DISCOUNT]%>" value="<%=rec.getDiscount()%>">
                        <input type="hidden" name="iCommandPosting" value="">                        
                        <div class="row">
                            <div class="col-md-12">

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Kode</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" class="form-control input-sm" readonly value="<%=rec.getRecCode()%>" placeholder="Automatic Code" >
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Lokasi</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_LOCATION_ID]%>">
                                                <%
                                                  for (Location loc : listLocation) {
                                                  long oidLocation = rec.getLocationId();
                                                %>
                                                <option value="<%=loc.getOID()%>" <%=loc.getOID()  == oidLocation ? "selected" : ""%>><%=loc.getName()%></option>
                                                <%}
                                                %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Tanggal</label>
                                        <div class="input-group col-sm-8">
                                            <div class="bootstrap-timepicker">                                              
                                                <%=ControlDate.drawDateWithStyleNew(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), 1, -5, "form-control input-sm", "")%>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Status</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_RECEIVE_STATUS]%>" id="doc_status">
                                                <%if(iCommand == Command.ADD){ %>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>" <%=I_DocStatus.DOCUMENT_STATUS_DRAFT  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                                                <%}else if(rec.getReceiveType() == 0){%>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>" <%=I_DocStatus.DOCUMENT_STATUS_DRAFT  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED%>" <%=I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]%></option>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_APPROVED%>" <%=I_DocStatus.DOCUMENT_STATUS_APPROVED  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_APPROVED]%></option>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>" <%=I_DocStatus.DOCUMENT_STATUS_FINAL  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]%></option>
                                                <%}else if(rec.getReceiveType() == 1){%>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_DRAFT%>" <%=I_DocStatus.DOCUMENT_STATUS_DRAFT  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]%></option>
                                                <option value="<%=I_DocStatus.DOCUMENT_STATUS_FINAL%>" <%=I_DocStatus.DOCUMENT_STATUS_FINAL  == rec.getReceiveStatus() ? "selected" : ""%>><%=I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]%></option>
                                                <%}%>
                                            </select>
                                            <% if (rec.getReceiveStatus() == I_DocStatus.DOCUMENT_STATUS_DRAFT) {%>
                                            <p class="font-12">Pastikan mengisi info forwarder sebelum status 'Final' (Jika Ada)</p>
                                            <%}%>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Terms</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_TERM_OF_PAYMENT]%>" >
                                            <%
                                                for (int i = 0; i < PstMatReceive.fieldsPaymentType.length; i++) {
                                                   String sortBy = ""+rec.getTermOfPayment();
                                                %>
                                            <option value="<%=i%>" <%=PstMatReceive.fieldsPaymentType[i]  == sortBy ? "selected" : ""%>><%=PstMatReceive.fieldsPaymentType[i] %></option>
                                            <%}
                                            %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Waktu</label>
                                        <div class="input-group col-sm-8">
                                          <div class="bootstrap-timepicker">                                              
                                              <%=ControlDate.drawTimeSecNew(FrmMatReceive.fieldNames[FrmMatReceive.FRM_FIELD_RECEIVE_DATE], (rec.getReceiveDate() == null) ? new Date() : rec.getReceiveDate(), "form-control input-sm")%>
                                          </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Supplier</label>
                                        <div class="input-group col-md-8">
                                            <div class="col-sm-10" style="padding: 0px !important;">
                                                <select class="form-control input-sm select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_SUPPLIER_ID]%>" id="supplier">
                                                <%
                                                  for (ContactList con : listSupplier) {
                                                  long oidSupplier = rec.getSupplierId();
                                                  String cntName = con.getContactCode() + " - " + con.getCompName();
                                                  if (cntName.length() == 0) {
                                                    cntName = con.getPersonName() + " " + con.getPersonLastname();
                                                  }
                                                %>
                                                <option value="<%=con.getOID()%>" <%=con.getOID()  == oidSupplier ? "selected" : ""%>><%=cntName%></option>
                                                <%}
                                                %>
                                                </select>
                                            </div>
                                            <div class="col-sm-2" style="padding: 0px !important;">
                                                <div class="input-group-addon btn btn-primary" id="btnOpenSearchSuplier"><i class="fa fa-plus"></i></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">No Invoice</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" class="form-control input-sm" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_INVOICE_SUPPLIER]%>"  value="<%=rec.getInvoiceSupplier()%>" placeholder="Invoice" >
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Tipe Receive</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_RECEIVE_TYPE]%>" id="tipe">
                                                <%
                                                    for (int i = 0; i < MatReceive.tipePenerimaanKey.length; i++) {
                                                       String oidTipe = ""+rec.getReceiveType();
                                                %>
                                                <option value="<%=MatReceive.tipePenerimaanValue[i] %>" <%=MatReceive.tipePenerimaanValue[i]  == oidTipe ? "selected" : ""%>><%=MatReceive.tipePenerimaanKey[i] %></option>
                                                <%}%>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Days</label>
                                        <div class="input-group col-sm-8">
                                          <%if (privShowQtyPrice) {%>
                                              <input type="text" class="form-control"  id="days" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_CREDIT_TIME]%>" value="<%=rec.getCreditTime()%>">
                                          <%} else {%>
                                              <input name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_CREDIT_TIME]%>" type="hidden" value="<%=rec.getCreditTime()%>">
                                          <%}%>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Mata Uang</label>
                                        <div class="input-group col-sm-8">
                                            <select class="form-control input-sm select2" onChange="javascript:changeCurrency(this.value)" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_CURRENCY_ID] %>" id="currency" <%=input%>>
                                                <%
                                                  for (CurrencyType cur : listCurr) {
                                                  oidCurrencyx = cur.getOID();
                                                 
                                                %>
                                                <option value="<%=cur.getOID()%>" <%=cur.getOID()  == rec.getCurrencyId() ? "selected" : ""%>><%=cur.getCode()%></option>
                                                <%}
                                                %>
                                            </select>
                                            <input type="hidden" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_CURRENCY_ID] %>"  value="<%=rec.getCurrencyId()%>">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Rate</label>
                                        <div class="input-group col-sm-8">
                                            <input type="text" class="form-control input-sm" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_TRANS_RATE] %>"  value="<%=rec.getTransRate() != 0 ? rec.getTransRate() : resultKonversi%>" <%=enableInput%>>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4 font-small">Keterangan</label>
                                        <div class="input-group col-sm-8">
                                            <textarea class="form-control input-sm min-form" name="<%=frmMatReceive.fieldNames[frmMatReceive.FRM_FIELD_REMARK] %>" placeholder="-" ><%=rec.getRemark()%></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <div class="pull-right">
                        <button type="submit" form="frmReceive"  class="btn btn-prochain" id="simpan-receive"><i class="fa fa-save"> </i> Simpan</button>
                        <button type="button" class="btn btn-danger" id="hapus-receive"><i class="fa fa-trash"> </i> Hapus</button>
                        <button type="button" class="btn btn-primary" id="kembali-list"><i class="fa fa-arrow-left"> </i> Kembali</button>
                    </div>
                </div>
            </div>
            <div class="box box-prochain">
                <div class="box-header with-border">
                    <label class="box-title pull-left">Data Penerimaan</label>
                    <div class="pull-right">
                    <button type="button" class="btn btn-primary" id="tambah-item"><i class="fa fa-plus"> </i> </button>
                    </div>
                </div>
                <div class="box-body">

                    <div class="box">
                        <div class="box-header with-border">
                            <label class="box-title pull-left">Item List</label>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div id="receiveTableElement">
                                        <table id="listReceive" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                                            <thead>
                                                <tr>
                                                    <th>No.</th>                                    
                                                    <th>SKU</th>
                                                    <th>Barcode</th>
                                                    <th>Nama Barang</th>
                                                    <th>Unit</th>
                                                    <th>Qty</th>
                                                    <th class="aksi">Aksi</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                for (int i = 0; i < listMatReceiveItem.size(); i++) {
                                                Vector temp = (Vector) listMatReceiveItem.get(i);
                                                MatReceiveItem recItem = (MatReceiveItem) temp.get(0);
                                                Material mat = (Material) temp.get(1);
                                                Unit unit = (Unit) temp.get(2);
                                                %>
                                                <tr>
                                                    <td class="text-center"><%=i + 1%></td>
                                                    <td class="text-center"><%=mat.getSku()%></td>
                                                    <td class="text-center"><%=mat.getBarCode()%></td>
                                                    <td class="text-center"><%=mat.getName()%></td>
                                                    <td class="text-center"><%=unit.getCode()%></td>
                                                    <td class="text-center"><a href="javascript:gostock('<%=String.valueOf(recItem.getOID())%>')">[ST.CD]</a><%=FRMHandler.userFormatStringDecimal(recItem.getQty())%></td>
                                                    <td class="text-center"><a class="btn btn-danger" href="javascript:cmdNewDelete('<%=recItem.getOID()%>')"><i class="fa fa-trash"></i></a></td>
                                                </tr>                                                
                                                <%}%>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                    <div class="col-md-12"><br>
                                    <div class="pull-right">
                                        <button class="btn btn-secondary"><strong>Total Qty :</strong> 3</button>
                                    </div>
                                    </div>
                            </div>
                        </div>
                    </div>
                    
                <%if(1==2){%>
                    <div class="box">
                        <div class="box-header with-border">
                            <label class="box-title pull-left">Rincian Pembayaran</label>
                            <div class="pull-right">
                            <button type="button" class="btn btn-primary" id="tambah-tanpa"><i class="fa fa-plus"> </i> </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-8">
                                    <div id="pembayaranTableElement">
                                        <table id="listPembayaran" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                                            <thead>
                                                <tr>
                                                    <th>Tanggal</th>                                    
                                                    <th>System Pembayaran</th>
                                                    <th>Mata Uang</th>
                                                    <th>Rate</th>
                                                    <th>Jumlah Dalam Mata Uang</th>
                                                    <th>Jumlah</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td class="text-center"></td>
                                                    <td class="text-center"></td>
                                                    <td class="text-center"></td>
                                                    <td class="text-center"></td>
                                                    <td class="text-center"></td>
                                                    <td class="text-center"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="5" class="text-center">Total Pembayaran</td>
                                                    <td class="text-center"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="5" class="text-center">Saldo Piutang</td>
                                                    <td class="text-center"></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <%
                                        String whereItem = "" + PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID] + "=" + oidReceiveMaterial;
                                        double totalBeli = PstMatReceiveItem.getTotal(whereItem);
                                        double ppn = rec.getTotalPpn();
                                        if (ppn == 0) {
                                            ppn = defaultPpn;
                                        }
                                        double totalBeliWithPPN = (totalBeli * (ppn / 100)) + totalBeli;
                                        double valuePpn = 0.0;
                                        if (rec.getIncludePpn() == 1) {
                                            valuePpn = totalBeli - (totalBeli / 1.1);
                                        } //else if(rec.getIncludePpn()== 0){
                                        else {
                                            valuePpn = totalBeli * (ppn / 100);
                                        }
                                    %>
                                    <div class="col-sm-6">
                                        <h6>Sub Total Harga Beli</h6>
                                        <h6>Include PPN</h6>
                                        <h6>Sub Total</h6>
                                        <h6>Sub Total Ongkos Kirim</h6>
                                        <h6>Grand Total</h6>
                                    </div>
                                    <div class="col-sm-1">
                                        <h6>:</h6>
                                        <h6>:</h6>
                                        <h6>:</h6>
                                        <h6>:</h6>
                                        <h6>:</h6>
                                    </div>
                                    <div class="col-sm-4">
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalBeli)%></h6>
                                        <h6><%=FRMHandler.userFormatStringDecimal(valuePpn)%></h6>
                                         <% if (rec.getIncludePpn()==1) { %>
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalBeli)%></h6>
                                         <%} else {%>
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN)%></h6>
                                         <%}%>
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></h6>
                                        <% if (rec.getIncludePpn()==1) { %>
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalBeli+totalForwarderCost)%></h6>
                                        <%}else {%>
                                        <h6><%=FRMHandler.userFormatStringDecimal(totalBeliWithPPN+totalForwarderCost)%></h6>
                                        <%}%>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="box">
                        <div class="box-header with-border">
                            <label class="box-title pull-left">Forwarder Info</label>
                            <div class="pull-right">
                            <button type="button" class="btn btn-primary" id="tambah-forward"><i class="fa fa-plus"> </i></button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div id="forwardTableElement">
                                        <table id="listForwardInfo" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                                            <thead>
                                                <tr>
                                                    <th>No.</th>                                    
                                                    <th>Nama Perusahaan</th>
                                                    <th>Tanggal</th>
                                                    <th>Total Biaya</th>
                                                    <th>Keterangan</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    if(vctForwarderInfo.size() != 0){
                                                    if(vctForwarderInfo.size() > 0) {
                                                        Vector temp = (Vector)vctForwarderInfo.get(0);
                                                        ForwarderInfo forwarderInfo = (ForwarderInfo)temp.get(0);
                                                        ContactList contactList = (ContactList)temp.get(1);
                                                        try {
                                                                contactList = PstContactList.fetchExc(forwarderInfo.getContactId());
                                                            } catch (Exception e) {
                                                            }
                                                %>
                                                <tr>
                                                    <td class="text-center">
                                                        <a href="javascript:editForwarderInfo()"><%=forwarderInfo.getDocNumber()%></a>
                                                    </td>
                                                    <td class="text-center"><%=contactList.getCompName() %></td>
                                                    <td class="text-center"><%=forwarderInfo.getDocDate()%></td>
                                                    <td class="text-center"><%=FRMHandler.userFormatStringDecimal(totalForwarderCost)%></td>
                                                    <td class="text-center"><%=forwarderInfo.getNotes()%></td>
                                                </tr>
                                                <%}}else{%>
                                                <tr>
                                                    <td colspan="5" class="text-center">
                                                        Tidak Ada Data
                                                    </td>
                                                </tr>
                                                <%}%>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <%}%>
                </div>
                <div class="box-footer">
                </div>
            </div>
        </section>
        <script>

            $(document).ready(function () {
                $('#listReceive').DataTable({
                    "paging": true,
                    "ordering": false,
                    "info": true
                });
            });
        </script>
    </body>
</html>
