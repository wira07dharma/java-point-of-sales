<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="java.util.Vector"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
    public static final String textListHeader[][] = 
    {
        {"Periode","Lokasi","Mata Uang","Suplier","Tipe Harga","Semua Lokasi","Semua Mata Uang","Semua Suplier","Cari Laporan"},
        {"Period","Location","Currency","Supplier","Price Type","All Location","All Currency","All Supplier","Search Report"}
    };
%>
<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
        <%if(menuUsed == MENU_ICON){%>
        <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>

        <link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

        <style media="print">
            .nonprint{ display: none; }
            #printReportPlace{ display: block !important; }
        </style>
    </head> 
    <%

        int iCommand = FRMQueryString.requestCommand(request);
        ControlLine ctrLine = new ControlLine();
        SrcReportReceive srcReportReceive = new SrcReportReceive();
        FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive();
        try{
            srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
        } catch(Exception e){
            srcReportReceive = new SrcReportReceive();
        }
        if(srcReportReceive==null){
            srcReportReceive = new SrcReportReceive();
        }
        try{
            session.removeValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
        } catch(Exception e){
        }

        //location
        Vector val_locationid = new Vector(1,1);
        Vector key_locationid = new Vector(1,1);
        String whereClause = " "
            + "("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE 
            + " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

        whereClause += " "
            + " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");
   
        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
        //val_locationid.add("0");
        //key_locationid.add(textListHeader[SESS_LANGUAGE][5]);
        for(int d=0;d<vt_loc.size();d++){
            Location loc = (Location)vt_loc.get(d);
            val_locationid.add(""+loc.getOID()+"");
            key_locationid.add(loc.getName());
        }
        //currency
        Vector val_currency = new Vector(1,1);
        Vector key_currency = new Vector(1,1);
        Vector vCurrency = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
        val_currency.add("0");
        key_currency.add(textListHeader[SESS_LANGUAGE][6]);
        for(int e=0; e<vCurrency.size(); e++) {
            CurrencyType objCurrencyType = (CurrencyType)vCurrency.get(e);
            val_currency.add(""+objCurrencyType.getOID());
            key_currency.add(objCurrencyType.getCode());
        }
        //suplier
        Vector val_supplier = new Vector(1,1);
        Vector key_supplier = new Vector(1,1);
        String wh_supp = ""
            + PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE] + " = " +PstContactClass.CONTACT_TYPE_SUPPLIER
            + " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+ " != "+PstContactList.DELETE;
        Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
        val_supplier.add("0");
        key_supplier.add(textListHeader[SESS_LANGUAGE][7]);
        for(int d=0; d<vt_supp.size(); d++){
            ContactList cnt = (ContactList)vt_supp.get(d);
            String cntName = cnt.getCompName();
            val_supplier.add(String.valueOf(cnt.getOID()));
            key_supplier.add(cntName);  
        }
    
        //tipe harga
        String ordPrice = PstPriceType.fieldNames[PstPriceType.FLD_INDEX]; 
        Vector listPriceType = PstPriceType.list(0,0,"",ordPrice);
    %>
    <body class="" style="background-color: beige">
        <div class="nonprint">
            <%if(menuUsed == MENU_PER_TRANS){%>
            <div><%@ include file = "../../../../main/header.jsp" %></div>
            <div><%@ include file = "../../../../main/mnmain.jsp" %></div>
            <%}else{%>
            <div><%@include file="../../../../styletemplate/template_header.jsp" %></div>
            <%}%>

            <link href="../../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
            <link href="../../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">

            <div class="row" style="margin-left:0px; margin-right:0px; margin-top:20px;">
                <div class="col-md-12">
                    <div class="box box-primary">
                        <div class="box-header">
                            <h3 class="box-title">Filter</h3>
                        </div>
                        <div class="box-body">
                            <div class="col-md-8">
                            <form name="frmsrcreceivereport" id="frmsrcreceivereport" class="form-horizontal" role="form">
                                <input type='hidden' name ='command' value='<%= Command.NONE%>'>
                                <input type='hidden' id="datafor" name='FRM_FIELD_DATA_FOR' value='getSummaryReportPerInvoice'>
                                <input type='hidden' name='language' value='<%= SESS_LANGUAGE%>'>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">
                                        <%= textListHeader[SESS_LANGUAGE][0]%>
                                    </label>
                                    <div class="col-sm-5">
                                        <div class="input-group">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input id="dateFrom" name="<%= frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM]%>" class="form-control dates" type="text">
                                        </div>
                                    </div>
                                    <div class="col-sm-5">
                                        <div class="input-group">
                                            <div class="input-group-addon">
                                                <i class="fa fa-calendar"></i>
                                            </div>
                                            <input id="dateTo" name="<%= frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO]%>" class="form-control dates" type="text">
                                        </div>
                                    </div> 
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">
                                        <%= textListHeader[SESS_LANGUAGE][1]%>
                                    </label>
                                    <div class="col-sm-10">
                                        <%=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID], null, ""+srcReportReceive.getLocationId(), val_locationid, key_locationid, "multiple='multiple' style='width: 100%'", "form-control selects2")%> 
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">
                                        <%= textListHeader[SESS_LANGUAGE][2]%>
                                    </label>
                                    <div class="col-sm-10">
                                        <%=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_CURRENCY_ID], null, ""+srcReportReceive.getCurrencyId(), val_currency, key_currency, "", "form-control")%>
                                    </div>
                                </div>
                                <%if(typeOfBusinessDetail == 2) {%>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">Penerimaan</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="FRM_TIPE_PENERIMAAN">
                                            <option value="">Semua Tipe</option>
                                            <option value="<%=Material.MATERIAL_TYPE_EMAS%>"><%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_EMAS]%></option>
                                            <option value="<%=Material.MATERIAL_TYPE_BERLIAN%>"><%=Material.MATERIAL_TYPE_TITLE[Material.MATERIAL_TYPE_BERLIAN]%></option>
                                        </select>
                                    </div>
                                </div>
                                <%}else {%>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">
                                        <%= textListHeader[SESS_LANGUAGE][3]%>
                                    </label>
                                    <div class="col-sm-10">
                                        <%=ControlCombo.draw(frmSrcReportReceive.fieldNames[frmSrcReportReceive.FRM_FIELD_SUPPLIER_ID],null,"",val_supplier,key_supplier,"","form-control")%> 
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="">
                                        <%= textListHeader[SESS_LANGUAGE][4]%>
                                    </label>
                                    <div class="col-sm-10">
                                        <%
                                            for (int i = 0; i<listPriceType.size();i++){
                                                PriceType prType = (PriceType)listPriceType.get(i);
                                                out.println("<label class='checkbox-inline'><input type='checkbox' name='"+FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_PRICE_TYPE_ID]+"' value='"+prType.getOID()+"'>"+prType.getCode()+"</label>");
                                            }
                                        %>
                                    </div>
                                </div>
                                <%}%>
                                <div class="form-group">
                                    <div class='col-sm-2'></div>
                                    <div class="col-sm-10">
                                        <button id="btnSearch" type="button" class="btn btn-primary">
                                            <i class="fa fa-filter"></i>
                                            <%= textListHeader[SESS_LANGUAGE][8]%>
                                        </button>                                                
                                    </div>
                                </div>   
                            </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id='reportPlace'></div>            

            <%if(menuUsed == MENU_ICON){%>
            <div><%@include file="../../../../styletemplate/footer.jsp" %></div>                        
            <%}else{%>
            <div><%@ include file = "../../../../main/footer.jsp" %></div>                        
            <%}%>
            <!-- #EndEditable -->

        </div>
    </body>
    <script src="../../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script src="../../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
    <script src="../../../styles/dimata-app.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
    <script src="../../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
    <script type="text/javascript" src="../../../styles/chart/highcharts.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var dataSend;
            var command;
            var dataFor;
            var approot = "<%= approot%>";

            var modalSetting = function (elementId, backdrop, keyboard, show) {
                $(elementId).modal({
                    backdrop: backdrop,
                    keyboard: keyboard,
                    show: show
                });
            };

            var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
                $(this).getData({
                    onDone: function (data) {
                        onDone(data);
                    },
                    onSuccess: function (data) {
                        onSuccess(data);
                    },
                    approot: approot,
                    dataSend: dataSend,
                    servletName: servletName,
                    dataAppendTo: dataAppendTo,
                    notification: notification,
                    ajaxDataType: dataType
                });
            };

            var loadReport = function () {
                var dataSend = $("#frmsrcreceivereport").serialize();
                dataSend = dataSend + "&typeFor=0";
                onSuccess = function (data) {

                };
                onDone = function (data) {
                    btnPrint("#btnPrint");
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", "#reportPlace", true, "json");
            };

            var loadReport2 = function () {
                var dataSend = $("#frmsrcreceivereport").serialize();
                dataSend = dataSend + "&typeFor=1";
                onSuccess = function (data) {

                };
                onDone = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", "#printReportPlace", true, "json");
            };

            var loadReportBisnisDetail2 = function () {
                var dataSend = $("#frmsrcreceivereport").serialize();
                onSuccess = function (data) {

                };
                onDone = function (data) {
                    printPenerimaan('#btnprintjewelry');
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxPenerimaan", "#reportPlace", true, "json");
            };

            var btnPrint = function (elementID) {
                $(elementID).click(function () {
                    var dataSend = $("#frmsrcreceivereport").serialize();
                    dataSend = dataSend + "&typeFor=1";
                    window.open("printreportsummaryreceive.jsp?" + dataSend + "");
                });
            };

            //
            $("#btnSearch").click(function () {
                var tipeBisnisDetail = "<%=typeOfBusinessDetail%>";
                if (tipeBisnisDetail === "2") {
                    $('#datafor').val("getReportBisnisDetail2");
                    loadReportBisnisDetail2();
                } else {
                    loadReport();
                    //loadReport2();
                }
            });
            $('.selects2').select2();
            
            function printPenerimaan(elementId) {
                $(elementId).click(function() {
                    var dataSend = $("#frmsrcreceivereport").serialize();
                    window.open("print_report_summary_penerimaan_jewelry.jsp?" + dataSend + "");
                });
            };

        });
    </script>
    <script type="text/javascript">

        $(function () {
            $('.dates').datetimepicker({
                autoclose: true,
                todayBtn: true,
                format: 'mm/dd/yyyy',
                minView: 2
            });
        });
    </script>

    <div id="printReportPlace"></div>
    
    <div class="modal fade" id="modalPrintSummary" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Modal Header</h4>
                </div>
                <div class="modal-body">
                    <p>Some text in the modal.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>
</html>
