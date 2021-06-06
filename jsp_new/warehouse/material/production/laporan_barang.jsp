<%-- 
    Created on : Mar 23, 2020, 5:09:20 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.util.Command"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%!   
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
            /*      . {
                      position: relative;
                      display: inline-block !important;
                      border-collapse: separate;
                  }*/
            .form-group {
                margin-bottom: 0px;
            }
            .input-group-addon {
                padding: 6px 12px;
                font-size: 14px;
                font-weight: 400;
                line-height: 1;
                color: #555;
                text-align: center;
                background-color: #eee;
                border: 1px solid #e4e4e4 !important;
            }

            .input-group.col-sm-8 {
                padding-left: 0px !important;
            }
        </style>
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
                    var searchForm = $('#frmBarang');
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
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_USER_ID=<%=userId%>&" + searchForm.serialize(),
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
                
                function runItemProduksiTable() {
                    var elementidparent = 'requestTableElement';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listBarangProduksi';
                    var elementid = 'listBarang';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }
                runItemProduksiTable();
                
                //        Date Picker
                $('.datePicker').datetimepicker({
                    format: "yyyy-mm-dd",
                    todayBtn: true,
                    autoclose: true,
                    minView: 2
                });
                
                $('body').on('click', '#search-form-btn', function () {
                    runItemProduksiTable();
                });
                $('body').on('click', '#export-data', function () {
                    var searchForm = $('#frmBarang');
                    window.open("<%=approot%>/warehouse/material/report/production/export_excel_barang.jsp?"+searchForm.serialize(), "REPORT_ITEM");
                });
                $('body').on('click', '#print-data', function () {
                    var searchForm = $('#frmBarang');
                    window.open("<%=approot%>/DocumentLaporanBarang?"+searchForm.serialize(), "REPORT_ITEM");
                });

            });

        </script>
    </head>
    <body>
        <section class="content-header">
            <h1>Produksi<small> Laporan</small> </h1>
            <ol class="breadcrumb">
                <li>Laporan</li>
                <li>Barang</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-prochain">
                <div class="box-header with-border">
                    <form name="frmBarang" id="frmBarang" method="post" action="" class="form-horizontal">
                        <input type="hidden" name="userId" value="<%=userId %>">
                        <input type="hidden" name="approot" value="<%=approot %>">
                        <input type="hidden" name="sess_language" value="<%=SESS_LANGUAGE %>">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <div class="input-group col-sm-8" style="padding-left: 15px; padding-right: 15px">
                                            <div class="bootstrap-timepicker">                                              
                                                <input type="text" class="form-control input-sm datePicker" name="start_date" style="width: 40%;"  value="<%= Formater.formatDate(new Date(), "yyyy-MM-dd") %>">
                                            </div>
                                            <span class="input-group-addon" style="float: left;height: 30px;width: 20%;border-radius: 0px !important;"> s/d </span>
                                            <div class="bootstrap-timepicker">           
                                                <input type="text" class="form-control input-sm datePicker" name="end_date" style="width: 40%;" value="<%= Formater.formatDate(new Date(), "yyyy-MM-dd") %>">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="pull-right">
                                        <button type="button" class="btn btn-prochain" id="search-form-btn"><i class="fa fa-search"> </i> Cari Data</button>
                                        <button type="button" class="btn btn-default" id="print-data"><i class="fa fa-print"> </i> Print</button>
                                        <button type="button" class="btn btn-default" id="export-data"><i class="fa fa-file-archive-o"> </i> Export Excel</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-body">
                    <div class="row">
                        <div class="col-md-12">
                            <div id="requestTableElement">
                                <table id="listBarang" class="table table-striped table-bordered table-responsive" style="font-size: 14px">
                                    <thead>
                                        <tr>
                                            <th>No.</th>                                    
                                            <th>No Kredit/Invoice</th>
                                            <th>SKU</th>
                                            <th>Nama Barang</th>
                                            <th>Tipe</th>
                                            <th>Qty</th>
                                            <th>Petugas Delivery</th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="box-footer">

                </div>
            </div>
        </section>
        <script>

        </script>
    </body>
</html>

