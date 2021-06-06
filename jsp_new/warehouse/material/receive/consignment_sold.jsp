<%-- 
    Document   : consignment_sold
    Created on : Dec 14, 2017, 3:03:09 PM
    Author     : Ed
--%>

<%@page import="com.dimata.pos.entity.balance.CashCashier"%>
<%@page import="com.dimata.pos.form.balance.FrmCashCashier"%>
<%@page import="com.dimata.common.form.location.FrmLocation"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../../main/javainit.jsp" %>

<%@page import="com.dimata.util.Command"%>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.gui.jsp.ControlList,
                   com.dimata.qdep.form.FRMHandler,
                   com.dimata.qdep.form.FRMMessage,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.qdep.entity.I_PstDocType,
                   com.dimata.gui.jsp.ControlLine,
                   com.dimata.util.Command,
                   com.dimata.common.entity.location.PstLocation,
                   com.dimata.common.entity.contact.PstContactClass,
                   com.dimata.common.entity.contact.PstContactList,
                   com.dimata.common.entity.contact.ContactList,
                   com.dimata.gui.jsp.ControlCombo,
                   com.dimata.common.entity.location.Location,
                   com.dimata.gui.jsp.ControlDate,
                   com.dimata.posbo.entity.masterdata.*,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.form.warehouse.FrmMatReceive,
                   com.dimata.posbo.entity.purchasing.PurchaseOrder,
                   com.dimata.posbo.entity.purchasing.PstPurchaseOrder,
                   com.dimata.posbo.entity.warehouse.*,
                   com.dimata.posbo.session.warehouse.SessForwarderInfo,
                   com.dimata.posbo.form.warehouse.FrmForwarderInfo,
                   com.dimata.common.entity.payment.PstCurrencyType,
                   com.dimata.common.entity.payment.CurrencyType" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Consignment Sold</title>

        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/datepicker/datepicker3.css"/>

        <script type="text/javascript" src="../../../styles/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="../../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
        <script type="text/javascript" src="../../../styles/iCheck/icheck.min.js"></script>
        <script type="text/javascript" src="../../../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap-notify.js"></script>
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datepicker.js"/>></script>
 
    <style>
        body table {font-size: 14px}
        #nodata {background-color: lightgray}
    </style>

    </head>
    <body bgcolor="#FFFFFF">
        <input type="hidden" name="command" id="command" value="<%= Command.ADD%>">
        <section class="content-header">
            <h1>Consignment Sold
                <small> list</small></h1>
            <ol class="breadcrumb">
                <li>
                    <a href=""> Penerimaan
                        <i class="fa fa-download">

                        </i>
                        <li class="active">Consignment Sold</li>
                    </a>
                </li>
            </ol>
        </section>
        <p></p>
        <div>
            <div class="row"> 
                <div class="col-sm-12">
                    <div class="box box-default">
                        <div class="box-body">
                            
                            <form id="mainform" class="form-horizontal">
                                <input type="hidden" name="FRM_FIELD_DATA_FOR" id="generaldatafor">
                                <input type="hidden" name="FRM_FIELD_OID" id="oid">
                                <input type="hidden" name="command" value="<%= Command.LIST%>">
<!--                                <input type="hidden" name="approot" id="approot" value="<%--= approot--%>">-->
                                
                                <%
                                    Vector listLocation = new Vector();
                                    listLocation = PstLocation.listAll();
                                %>
                                
                                <div class="form-group">
                                    <label class="control-label col-sm-1">Location:</label>
                                    <div class="col-sm-4">
                                        <%
                                            if (listLocation.size() > 0){
                                        %>
                                        <select class="form-control" id="selectLocation" name="<%=FrmLocation.fieldNames[FrmLocation.FRM_FIELD_NAME]%>">
                                            <%
                                                for (int i = 0; i < listLocation.size(); i++){
                                                    Location location = (Location) listLocation.get(i);
                                            %>
                                            
                                            <option value="<%= location.getOID()%>"><%= location.getName()%></option>
                                            
                                        <%   
                                                }
                                            }
                                            
                                        %>
                                            
                                            
                                        </select>
                                    </div>
                                </div>
                                <%
                                    CashCashier cashCashier = new CashCashier();
                                    
                                %>
                                
                                <div class="form-group">
                                    <label class="control-label col-sm-1">Start&nbsp;Date:</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="datepickerStart form-control" name="<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_OPEN_DATE]%>" value="<%=Formater.formatDate(cashCashier.getOpenDate(), "yyyy-MM-dd")%>">
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label class="control-label col-sm-1">End&nbsp;Date:</label>
                                    <div class="col-sm-4">
                                        <input type="text" class="datepickerEnd form-control" name="<%=FrmCashCashier.fieldNames[FrmCashCashier.FRM_FIELD_CLOSE_DATE]%>" value="<%=Formater.formatDate(cashCashier.getCloseDate(), "yyyy-MM-dd")%>">
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <div class="col-sm-1 col-sm-offset-1">
                                        <button type="submit" class="btn btn-primary" id="btnSubmit" data-oid="0" data-for="listResult"><i class="fa fa-cog"></i>&nbsp; Process</button>
                                    </div>
                                </div>
                            </form>     
                        </div>
                    </div>
                </div>
                                        
                <!--TABLE LIST-->
                
                <div class="col-sm-12">
                    <div class="box-body showList-body">

                    </div>
                </div>

            </div>
        </div>
        <script>
            $(document).ready(function() {
                //INITIALIZE
                var command = $("#command").val();
                var approot = "<%= approot%>";
                var oid = null;
                var dataFor = null;
                var dataForCreate = null;
                var dataSend = null;

                //FUNCTION VARIABLE
                var onDone = null;
                var onSuccess = null;
                
                //DATE PICKER FOR START & END DATE
                $(".datepickerEnd").datepicker({
                    format: "yyyy-mm-dd",
                    autoclose: true
                });
                $(".datepickerStart").datepicker({
                    format: "yyyy-mm-dd",
                    autoclose: true
                });
                
                var modalSetting = function (elementId, backdrop, keyboard, show) {
                        $(elementId).modal({
                            backdrop: backdrop,
                            keyboard: keyboard,
                            show: show
                        });
                    };
                
                var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
                        /*
                         * getDataFor	: # FOR PROCCESS FILTER
                         * onDone	: # ON DONE FUNCTION,
                         * onSuccess	: # ON ON SUCCESS FUNCTION,
                         * approot	: # APPLICATION ROOT,
                         * dataSend	: # DATA TO SEND TO THE SERVLET,
                         * servletName  : # SERVLET'S NAME,
                         * dataType	: # Data Type (JSON, HTML, TEXT)
                         */
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
                    }
                    
                    //CLICK BUTTON
                    $("form#mainform").submit(function(){
                        dataFor = $("#btnSubmit").data("for");
                        oid = $("#btnSubmit").data("oid");
                        
                        var curretnBtnHtml = $("#btnSubmit").html();
                        $("#btnSubmit").html("<i class='fa fa-cog fa-spin'></i> Processing...").attr({"disabled": "true"});
                        $("#generaldatafor").val(dataFor);
                        $("#oid").val(oid);                        
                        
                        dataSend = $(this).serialize(); 
                         
                        onSuccess = function (data) {
                            $("#btnSubmit").removeAttr("disabled").html(curretnBtnHtml);

                            };
                        onDone = function (data) {
                                //alert(data.FRM_FIELD_HTML);
                                 //BUTTON CREATE DOC
                    
                            };
                        
                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxConsignmentSold", ".showList-body", true, "json");
                        return false;
                    });
                    
                    //BUTTON CREATE
                    $("body").on("click","#btnCreate",function(){
                       
                       dataFor = $("#btnCreate").data("for");
                       command = 1;
                       
                       var currentBtnHtml = $("#btnCreate").html();
                       $("#btnCreate").html("<i class='fa fa-spinner fa-pulse'></i> Processing...").attr({"disabled": "true"});
                       $("#generaldatafor").val(dataFor);
                       
                       modalSetting("#cdoc", "static", false, false);
                       $("#cdoc").modal("show");
                       
                       $(".datepicker").datepicker({
                            format: "yyyy-mm-dd",
                            autoclose: true
                        });
                        
                        //click button save
                        $("#btngeneralform").unbind().click(function(){
                           dataSend = {
                                "FRM_FIELD_DATA_FOR": dataFor,
                                "FRM_FIELD_RECEIVE_DATE": $(".datepicker").val(),
                                "command": command
                            } 
                         
                        onSuccess = function (data) {
                            $("#btnCreate").removeAttr("disabled").html(currentBtnHtml);
                            $("#cdoc").modal("hide");

                            };
                        onDone = function (data) {
                                //alert(data.FRM_FIELD_MSG);
                                 //BUTTON CREATE DOC
                    
                            };
                        
                        getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxConsignmentSold", ".showSuccess", true, "json");
                        return false;
                           
                        });
                       
                       
                    });
                    
            });

        </script>
        
        <div id="cdoc" class="modal fade nonprint" tabindex="-1">
                <div class="modal-dialog nonprint modal-sm">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                            <h4 class="addeditgeneral-title"></h4>
                        </div>
                        <form id="generalform">
                            
                            <div class="modal-body ">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="box-body addeditgeneral-body">
                                            <div class='form-group'>
                                                <label>
                                                <i class='fa fa-question-circle' data-toggle='tooltip' data-placement='right'></i>
                                                Receive Date <i style='color:red'>(m*</i></label>
                                                <input type='text' class='form-control datepicker'>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" id="btngeneralform"><i class="fa fa-check"></i> Save</button>
                                <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Close</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
    </body>
    
</html>
