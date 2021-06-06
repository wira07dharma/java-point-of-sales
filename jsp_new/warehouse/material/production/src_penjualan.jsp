<%-- 
    Document   : src_penjualan
    Created on : Nov 8, 2019, 7:32:06 PM
    Author     : WiraDharma
--%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file ="../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION);%>
<%@ include file ="../../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!    public static final String textListGlobal[][] = {
        {"Produksi", "Daftar Penjualan", "Pencarian", "Cari", "Barang", "Data Pengiriman", "Jadwal Pengiriman", "Pengiriman", "Status Pengiriman"},
        {"Production", "Selling List", "Search", "View", "Item", "Selling Data", "Dispatch Schedule", "Dispatch", "Status Dispatch"}
    };

    public static final String textListHeader[][] = {
        {"Nama", "No. Kredit / Invoice", "Tanggal Pengajuan", "Tipe Penjualan", "Kategori Kredit", "Lokasi"},
        {"Name", "No. Credit / Invoice", "Date Pengajuan", "Type Penjualan", "Category Credit", "Location"}
    };

    public static final String textHeaderTable[][] = {
        {"No", "No. PK / Invoice", "Nama Konsumen", "Tanggal Pengajuan", "Tipe Penjualan", "Kategori Kredit", "Lokasi", "Jumlah Pengajuan", "Status", "Aksi"},
        {"No", "No. PK / Invoice", "Name Customer", "Date Pengajuan", "Type Penjualan", "Category Credit", "Location", "Total Pengajuan", "Status", "Action"}
    };

    public static final String dataTableTitle[][] = {
        {
            "Tampilkan _MENU_ data per halaman",
            "Data Tidak Ditemukan",
            "Menampilkan halaman _PAGE_ dari _PAGES_",
            "Belum Ada Data",
            "(Disaring dari _MAX_ data)",
            "Pencarian :",
            "Awal",
            "Akhir",
            "Berikutnya",
            "Sebelumnya"
        }, {
            "Display _MENU_ records per page",
            "Nothing found - sorry",
            "Showing page _PAGE_ of _PAGES_",
            "No records available",
            "(filtered from _MAX_ total records)",
            "Search :",
            "First",
            "Last",
            "Next",
            "Previous"}
    };

%>
<%
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title><!-- BOOTSTRAPS CSS -->
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <link rel="stylesheet" href="../../../styles/datatables/jquery.dataTables.css"  type="text/css" />
        <!--<link href="../../../styles/plugin/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />-->
        <link rel="stylesheet" media="screen" href="../../../styles/Highcharts-6.0.7/highcharts.css"/>
        <style>
            .modal-header, .modal-body, .modal-footer {padding-top: 10px; padding-bottom: 10px}

            span.input-group-addon {
                padding: 5px;
            }
            button.btn.btn-sm.btn-danger.pull-right {
                margin-left: 10px;
                width: 10%;
            }
            p {
                margin: 0 0 5px;
                font-size: 12px;
            }
            button.btn.btn-sm.btn-success.pull-right {
                margin-left: 10px;
                width: 10%;
            }
            input.form-control.input-sm.datePicker {
                padding: 5px;
            }
            body {background-color: #EEE}

            .box .box-header, .box .box-footer {border-color: lightgray}

            .datetimepicker th {font-size: 14px}
            .datetimepicker td {font-size: 12px}

            .tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
                font-size: 12px;
                padding: 5px;
                border-color: #DDD;
            }

            .tabel-data tbody tr th {
                text-align: center;
                white-space: nowrap;
                text-transform: uppercase;
            }

            .form-group label {padding-top: 5px}
            .table, form {margin-bottom: 0px}
            .middle-inline {text-align: center; white-space: nowrap}
            th {
                text-align: center;
                font-size: 12px;
            }
            thead.headerList {
                background-color: #3c8dbc;
                color: #fff;
                text-align: center;
            }
            td {
                font-size: 14px;
                text-align: center;
                border: 1px solid #428bca !important;
            }
            table.dataTable thead > tr > th {
                padding-right: 25px !important;
            }
            th.sorting {
                padding: 0px !important;
                padding-bottom: 7px !important;
                padding-left: 5px !important;
            }
            .pad-top-8{
                padding: 8px 0px 0px 0px;
            }
            .nav-tabs>li.active>a, .nav-tabs>li.active>a:hover, .nav-tabs>li.active>a:focus {
                color: #fff;
                background-color: #44a3da;
                border: 1px solid #fff;
                border-bottom-color: #ffffff00;
                cursor: default;
            }
            .content-header {
                position: relative;
                padding: 15px 15px 0 15px;
                margin-bottom: 20px;
            }
            .line {
                margin-left: 15px;
                border-bottom: 3px solid #cccccc;
            }.nav-buat {
                margin-left: 15px;
            }
            .content{
                padding-top: 0px !important;
            }
            .dataTables_wrapper .dataTables_paginate .paginate_button {
                box-sizing: border-box;
                display: inline-block;
                min-width: 1.5em;
                padding: 0 !important; 
                margin-left: 2px;
                text-align: center;
                text-decoration: none !important;
                cursor: pointer;
                cursor: hand;
                color: #333 !important;
                border: 1px solid transparent;
            }


        </style>
    </head>
    <body>
        <section class="content-header">
            <h1><%=textListGlobal[SESS_LANGUAGE][0]%><small> <%=textListGlobal[SESS_LANGUAGE][1]%></small> </h1>
            <ol class="breadcrumb">
                <li><%=textListGlobal[SESS_LANGUAGE][0]%></li>
            </ol>
        </section>
        <p class="line"></p>
        <section class="nav-buat">
            <ul  class="nav nav-tabs">
                <li  class="active">
                    <a  id="penjualan" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][6]%></a>
                </li>
                <li>
                    <a id="serachPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][7]%></a>
                </li>
                <li> <a id="statusPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][8]%></a>
                </li>
            </ul>
        </section>
        <section class="content">
            <div id="data-penjualan" class="box box-primary">
                <div class="box-header with-border">
                    <label class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][5]%></label>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="listStock" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][0]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][1]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][2]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][3]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][4]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][5]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][6]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][7]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][8]%></th>
                                    <th class="text-center"><%=textHeaderTable[SESS_LANGUAGE][9]%></th>
                                </tr>
                            </thead>

                        </table>
                    </div>   
                    <p></p>
                    <div class="box-footer">
                    </div>
                </div>
            </div>
        </section>
        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script src="../../../styles/plugin/datatables/jquery.dataTables.js" type="text/javascript"></script>
        <script src="../../../styles/datatables/dataTables.bootstrap.js" type="text/javascript"></script>
        <!--<script src="../../../styles/plugin/datatables/dataTables.bootstrap.js" type="text/javascript"></script>-->
        <script type="text/javascript" src="../../../styles/Highcharts-6.0.7/highcharts.js"></script>
        <script type="text/javascript" src="../../../styles/Highcharts-6.0.7/exporting.js"></script>
        <script>
            $(document).ready(function () {

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

                // DATABLE SETTING
                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({
                        "bDestroy": true,
                        "ordering": false,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        //  "info":     true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>",
                            "sLengthMenu": "<%=dataTableTitle[SESS_LANGUAGE][0]%>",
                            "sZeroRecords": "<%=dataTableTitle[SESS_LANGUAGE][1]%>",
                            "sInfo": "<%=dataTableTitle[SESS_LANGUAGE][2]%>",
                            "sInfoEmpty": "<%=dataTableTitle[SESS_LANGUAGE][3]%>",
                            "sInfoFiltered": "<%=dataTableTitle[SESS_LANGUAGE][4]%>",
                            "sSearch": "<%=dataTableTitle[SESS_LANGUAGE][5]%>",
                            "oPaginate": {
                                "sFirst ": "<%=dataTableTitle[SESS_LANGUAGE][6]%>",
                                "sLast ": "<%=dataTableTitle[SESS_LANGUAGE][7]%>",
                                "sNext ": "<%=dataTableTitle[SESS_LANGUAGE][8]%>",
                                "sPrevious ": "<%=dataTableTitle[SESS_LANGUAGE][9]%>"
                            }
                        },
                        "bServerSide": true,
                        "searchDelay": 2000,
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_USER_ID=<%=userId%>",
                        aoColumnDefs: [
                            {
                                bSortable: false,
                                aTargets: [-1]
                            }
                        ],
                        "initComplete": function (settings, json) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnPageChange": function (oSettings) {

                        }
                    });
                    $(elementIdParent).find("#" + elementId + "_filter").find("input").addClass("form-control");
                    $(elementIdParent).find("#" + elementId + "_length").find("select").addClass("form-control");
                    $("#" + elementId).css("width", "100%");
                }

                function runDataTable(elementidparent, elementid, servlet, dataFor, callBack) {
                    dataTablesOptions("#" + elementidparent, elementid, servlet, dataFor, callBack);
                }

                var oTables = $('#listStock').DataTable({
                    "ordering": false
                });
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });

                function runProduksiTable() {
                    var elementidparent = 'data-penjualan';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listProduksiAll';
                    var elementid = 'listStock';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }

//    $('#listStock').DataTables();

                $('body').on('click', '.detail-produksi-btn', function () {
                    var oid = $(this).data('oid');
                    window.location = "detail_penjualan.jsp?oid_cbm=" + oid;

                });
                $('body').on('click', '.dokumen-produksi-btn', function () {
                    var oid = $(this).data('oid');
                  window.open("<%= approot%>/PrintDocumentProduction?approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid="+oid);
                });

                runProduksiTable();

                $("#serachPengiriman").click(function () {
                    window.location.href = "src_pengiriman.jsp";
                });
                $("#statusPengiriman").click(function () {
                    window.location.href = "status_pengiriman.jsp";
                });
            });
        </script>
    </body>
</html>



