<%-- 
    Document   : list_report_sales
    Created on : Mar 11, 2020, 11:15:30 AM
    Author     : Regen
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
<%@ include file = "../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE);%>
<%@ include file = "../../../main/checkuser.jsp" %>

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

                $(window).load(function () {
                    $('.date-harian').css("display", "none");
                    $('.date-bulanan').css("display", "block");
                });

                $('#form_jenis_laporan').change(function () {
                    var transaski = $('#form_type_transaksi').val();
                    var laporan = $('#form_jenis_laporan').val();
                    if (laporan == 1) {
                        $('#tampilan').css("display", "none");
                        $('.date-harian').css("display", "none");
                        $('.date-bulanan').css("display", "block");
                    } else if (laporan == 2) {
                        $('#tampilan').css("display", "block");
                        if (transaski == 0) {
                            $('#tampilan').css("display", "block");
                            $('#tampilan-invoice').css("display", "block");
                            $('#tampilan-kredit').css("display", "none");
                            ;
                            $("input[type='checkbox']#check-kredit").iCheck('uncheck');
                        } else if (transaski == 4) {
                            $('#tampilan').css("display", "block");
                            $('#tampilan-kredit').css("display", "block");
                            $('#tampilan-invoice').css("display", "none");
                            $("input[type='checkbox']#check-invoice").iCheck('uncheck');
                        } else if (transaski == 6) {
                            $('#tampilan').css("display", "none");
                            $('#tampilan-invoice').css("display", "none");
                            $('#tampilan-kredit').css("display", "none");
                            $("input[type='checkbox']#check-invoice").iCheck('uncheck');
                            $("input[type='checkbox']#check-kredit").iCheck('uncheck');
                        }
                        $('.date-bulanan').css("display", "none");
                        $('.date-harian').css("display", "block");
                    }
                });

                $('#form_type_transaksi').change(function () {
                    var laporan = $('#form_type_transaksi').val();
                    var jenislaporan = $('#form_jenis_laporan').val();
                    if (laporan == 0 && jenislaporan == 2) {
                        $('#tampilan').css("display", "block");
                        $('#tampilan-invoice').css("display", "block");
                        $('#tampilan-kredit').css("display", "none");
                        ;
                        $("input[type='checkbox']#check-kredit").iCheck('uncheck');
                    } else if (laporan == 4 && jenislaporan == 2) {
                        $('#tampilan').css("display", "block");
                        $('#tampilan-kredit').css("display", "block");
                        $('#tampilan-invoice').css("display", "none");
                        $("input[type='checkbox']#check-invoice").iCheck('uncheck');
                    } else if (laporan == 6) {
                        $('#tampilan').css("display", "none");
                        $('#tampilan-invoice').css("display", "none");
                        $('#tampilan-kredit').css("display", "none");
                        $("input[type='checkbox']#check-invoice").iCheck('uncheck');
                        $("input[type='checkbox']#check-kredit").iCheck('uncheck');
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
                    var transaksi = $('#form_type_transaksi').val();
                    var invoice = $('#check-invoice').val();
                    var kredit = $('#check-kredit').val();
                    var formData = $('#frmPenjualan');
                    console.log(formData.serialize());
                    var jenis = $('#jenis_report').val();
                    var url = "";
                    if (jenis==="0"){
                           url =  "<%=approot%>/store/sale/report/summary_report_sales.jsp?" + formData.serialize();
                       } else {
                           if (transaksi==="6"){
                               alert("Tidak dapat menampilkan detail untuk Tipe ini!");
                            } else {
                                url =  "<%=approot%>/store/sale/report/detail_report_sales.jsp?" + formData.serialize();
                            }
                       }

                    if (laporan == 1) {
                        if (startDate == "" || endDate == "") {
                            alert("Didalam pencarian tanggal sangat dibutuhkan \nDiharapkan menginput tanggal!");
                        } else {
                            window.location = url;
                        }

                    } else if (laporan == 2) {
                        if (startDate1 == "" || endDate1 == "") {
                            alert("Didalam pencarian tanggal sangat dibutuhkan \nDiharapkan menginput tanggal!");

                        } else {
                            window.location = url;
                        }

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
                                        <label class="col-sm-4">Jenis Laporan</label>
                                        <div class="col-sm-8">
                                            <select class="form-control input-sm select2" id="form_jenis_laporan" name="form_jenis_laporan">
                                                <option value="1">Bulanan</option>
                                                <option value="2">Harian</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="col-sm-4">Tanggal</label>
                                        <div class="input-group col-sm-8" style="padding-left: 15px;padding-right: 15px;">
                                            <input type="text" autocomplete="off" style="float: left;width: 45%;" class="date-bulanan form-control date-picker" aria-describedby="basic-addon2"  id="startDate" name="start_month" value="" placeholder="semua tanggal">
                                            <span class="input-group-addon date-bulanan" id="basic-addon2" style="float: left;height: 34px;text-align: center;width: 10%;padding: 8px 0px 0px 0px;display: inline-block;">s/d</span>
                                            <input type="text" autocomplete="off" style="float: left;width: 45%;" class="date-bulanan form-control date-picker" aria-describedby="basic-addon2" id="endDate" name="end_month" value="" placeholder="semua tanggal">

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
                                        <label class="col-sm-4">Tipe Transaksi</label>
                                        <div class="col-sm-8">
                                            <select class="form-control input-sm select2" id="form_type_transaksi" name="form_type_transaksi">
                                                <option value="<%=SrcSaleReport.TYPE_CREDIT%>"><%=SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CREDIT]%></option>
                                                <option value="<%=SrcSaleReport.TYPE_CASH%>"><%=SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CASH]%></option>
                                                <option value="<%=SrcSaleReport.TYPE_CASH_CREDIT%>"><%=SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CASH_CREDIT]%></option>
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
                                    <div class="form-group" style="display: none" id="tampilan">
                                        <label class="col-sm-4">Tampilan</label>
                                        <div class="col-sm-8" style="display: none" id="tampilan-invoice">
                                            <input type="checkbox" class="flat-blue" name="invoice_filter" id="check-invoice"><span class="p"> No Invoice</span>
                                        </div>
                                        <div class="col-sm-8" id="tampilan-kredit">
                                            <input type="checkbox" class="flat-blue" name="kredit_filter" id="check-kredit"><span class="p"> No Kredit</span>
                                        </div>
                                    </div>
                                    <!--                                    <div class="form-group" id="invoice-data" style="display: none">
                                                                            <label class="col-sm-4"></label>
                                                                            <div class="col-sm-8">
                                                                               <input type="text" autocomplete="off" class="form-control" name="invoice_filter" id="invoice_filter" value="" placeholder="No Invoice">
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group" id="kredit-data" style="display: none">
                                                                            <label class="col-sm-4"></label>
                                                                            <div class="col-sm-8">
                                                                               <input type="text" autocomplete="off" class="form-control" name="kredit_filter" id="kredit_filter" value="" placeholder="No Kredit">
                                                                            </div>
                                                                        </div>-->
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
