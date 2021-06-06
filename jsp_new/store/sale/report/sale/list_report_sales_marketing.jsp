<%-- 
    Document   : list_report_sales_marketing
    Created on : 24 Jun 20, 17:10:53
    Author     : gndiw
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.search.SrcSaleReport"%>
<%@page import="com.dimata.qdep.entity.I_PstDocType"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file = "../../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../../main/checkuser.jsp" %>

<%    int iCommand = FRMQueryString.requestCommand(request);

    int start = 0;
    int record = 0;
    String whereClause = "";
    String order = "";

    Vector<Location> listLocation = PstLocation.list(start, record, whereClause, order);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title>
        <%@include file="../../../../styles/plugin_component.jsp" %>
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
            .ui-datepicker-calendar {
                display: none;
            }

        </style>
        <script language="javascript">
            $(document).ready(function () {

                var approot = "<%= approot%>";
                var getDataFunction = function (onDone, onSuccess, dataSend, servletName, dataAppendTo, notification) {
                    $(this).getData({
                        onDone: function (data) {
                            onDone(data);
                        },
                        onSuccess: function (data) {
                            onSuccess(data);
                        },
                        approot: "<%=approot%>",
                        dataSend: dataSend,
                        servletName: servletName,
                        dataAppendTo: dataAppendTo,
                        notification: notification
                    });
                };

                //MODAL SETTING
                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };


                //Select2
                $('.select2').select2({placeholder: "Semua"});
                $('.selectAll').select2({placeholder: "Semua"});

                //        Date Picker
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });

                $('.date-picker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    dateFormat: 'MM yy',

                    onClose: function () {
                        var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
                        var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
                        $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
                    },

                    beforeShow: function () {
                        if ((selDate = $(this).val()).length > 0)
                        {
                            iYear = selDate.substring(selDate.length - 4, selDate.length);
                            iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
                            $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
                            $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
                        }
                    }
                });


                $('#form_jenis_laporan').change(function () {
                    var laporan = $('#form_jenis_laporan').val();
                    if (laporan == 1) {
                        $('#tampilan').css("display", "none");
                        $('.date-harian').css("display", "none");
                        $('.date-bulanan').css("display", "block");
                    } else if (laporan == 2) {
                        $('#tampilan').css("display", "block");
                        $('.date-bulanan').css("display", "none");
                        $('.date-harian').css("display", "block");
                    }
                });


                $('#search-form-btn').click(function () {
                    var command = $('#command').val();
                    var laporan = $('#form_jenis_laporan').val();
                    var startDate = $('#startDate').val();
                    var endDate = $('#endDate').val();
                    var startDate1 = $('#startDate1').val();
                    var endDate1 = $('#endDate1').val();
                    var location = $('#form_location').val();
                    var invoice = $('#check-invoice').val();
                    var kredit = $('#check-kredit').val();
                    var formData = $('#frmPenjualan');
                    console.log(formData.serialize());
                    var jenis = $('#jenis_report').val();
                    var url = "<%=approot%>/store/sale/report/sale/report_sales_marketing.jsp?" + formData.serialize();
                    if (startDate1 == "" || endDate1 == "") {
                           alert("Didalam pencarian tanggal sangat dibutuhkan \nDiharapkan menginput tanggal!");

                       } else {
                           window.location = url;
                       }

                });

                $('input[type="checkbox"].flat-blue').iCheck({
                    checkboxClass: 'icheckbox_square-blue'
                });

            });
        </script>
    </head>
    <body>
        <section class="content-header">
            <h3>Laporan Menu<small> Pencarian</small> </h3>
            <ol class="breadcrumb">
                <li>Laporan</li>
                <li>Penjualan</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-prochain">
                <div class="box-header with-border">
                    <label class="box-title pull-left">Pencarian</label>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" name="frmPenjualan" id="frmPenjualan" method="post" action="">
                        <input type="hidden" name="command" id="command" value="<%=Command.LIST%>">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="col-md-6">

                                    <div class="form-group">
                                        <label class="col-sm-4">Tanggal</label>
                                        <div class="input-group col-sm-8" style="padding-left: 15px;padding-right: 15px;">
                                            <input type="text" autocomplete="off" style="float: left;width: 45%;" class="date-harian form-control datePicker" aria-describedby="basic-addon2" id="startDate1" name="start_date" value="" placeholder="semua tanggal">
                                            <span class="input-group-addon date-harian" id="basic-addon2" style="float: left;height: 34px;text-align: center;width: 10%;padding: 8px 0px 0px 0px;display: inline-block;">s/d</span>
                                            <input type="text" autocomplete="off" style="float: left;width: 45%;" class="date-harian form-control datePicker" aria-describedby="basic-addon2" id="endDate1" name="end_date" value="" placeholder="semua tanggal">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Lokasi Penjualan</label>
                                        <div class="col-sm-8">
                                            <select class="form-control input-sm select2" id="form_location" name="form_location" multiple="" placeholder="semua">
                                                
                                                <%
                                                    for (Location loc : listLocation) {
                                                %>
                                                <option value="<%=loc.getOID()%>"><%=loc.getName()%></option>
                                                <%}
                                                %>
                                            </select>
                                        </div>
                                    </div>


                                </div>

                                <div class="col-md-6">

                                    <div class="form-group">
                                        <label class="col-sm-4">Kategori</label>
                                        <div class="col-sm-8">
                                            <select class="form-control input-sm select2" id="form_category" name="form_category" multiple="" placeholder="semua">
                                                
                                                <%
                                                    Vector<Category> listCat = PstCategory.list(0, 0, "", "");
                                                    for (Category cat : listCat) {
                                                %>
                                                <option value="<%=cat.getOID()%>"><%=cat.getName()%></option>
                                                <%}
                                                %>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-4">Tampilkan Laporan</label>
                                        <div class="col-sm-8">
                                            <select class="form-control input-sm select2" id="jenis_report" name="jenis_report">
                                                <option value="0">Summary</option>
                                                <option value="1">Detail</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer">
                    <div class="pull-right">
                        <button class="btn btn-prochain" id="search-form-btn"><i class="fa fa-search"> </i> Cari Data</button>
                    </div>
                </div>
            </div>
        </section>

    </body>
</html>
