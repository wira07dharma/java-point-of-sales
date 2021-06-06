<%-- 
    Document   : user_list.jsp
    Created on : May 7, 2020, 3:45:31 PM
    Author     : WiraDharma
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ include file ="../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION);%>
<%@ include file ="../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
<%
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata - ProChain POS</title><!-- BOOTSTRAPS CSS -->
        <%@include file="../styles/plugin_component.jsp" %>
        <style>
            
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
            <h1>System<small>| User</small> </h1>
            <ol class="breadcrumb">
                <li>User Management</li>
            </ol>
        </section>
        <section class="content">
            <div id="data-user" class="box box-primary">
            <!--<form name="frmUserManagement" id="frmUserManagement" method="post" action="" class="form-horizontal">-->
                <div class="box-header with-border">
                    <div class="pull-left">
                    <div class="form-group">
                        <select class="form-control input-sm" name="USER_STATUS" id="USER_STATUS">
                                <option value="on">User Aktif</option>
                                <option value="off">User Non Aktif</option>
                                <option value="semua">Semua User</option>
                            </select>
                    </div>
                        <!--<input type="checkbox" checked class="flat-blue" id="STATUS_USER" name="STATUS_USER"><span class="p"> User Aktif</span>-->
                    </div>
                    <div class="pull-right">
                    <button button class="btn btn-success" id="search-user"><i class="fa fa-search"> Search</i></button>
                    <button class="btn btn-primary" id="add-user"><i class="fa fa-plus"> Add User</i></button>
                    </div>
                </div>
            <!--</form>-->
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="listUser" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th class="text-center">No</th>
                                    <th class="text-center">ID Pemakai</th>
                                    <th class="text-center">Nama Lengkap</th>
                                    <th class="text-center">Location</th>
                                    <th class="text-center">Status</th>
                                    <th class="text-center">Action</th>
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
                    var searchForm = $('#USER_STATUS').val();
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
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + "&SEND_USER_ID=<%=userId%>&STATUS_USER=" + searchForm,
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
                
                function runProduksiTable() {
                    var elementidparent = 'data-penjualan';
                    var servlet = 'AjaxUserManagement';
                    var dataFor = 'listUserAll';
                    var elementid = 'listUser';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }

                runProduksiTable();
                
                $('body').on('click', '#search-user', function () {
                    var searchForm = $('#USER_STATUS').val();
                    console.log(searchForm);
                    runProduksiTable();
                });
                
                $('#add-user').click(function(){
                    window.location.href = "<%=approot%>/system/user_edit.jsp";
                });
                
                $('body').on('click', '.detail-user-btn', function(){
                    var oid = $(this).data('oid');
                    window.location.href = "<%=approot%>/system/user_edit.jsp?user_oid="+oid+"&command=<%=Command.EDIT%>";
                });
            });
        </script>
    </body>
</html>
