<%-- 
    Document   : src_pengiriman
    Created on : Nov 12, 2019, 11:02:56 AM
    Author     : Regen
--%>

<%@page import="java.text.NumberFormat"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="com.dimata.sedana.form.kredit.FrmPinjaman"%>
<%@page import="com.dimata.pos.form.billing.FrmBillMain"%>
<%@page import="com.dimata.harisma.entity.employee.PstEmployee"%>
<%@page import="com.dimata.aiso.entity.masterdata.mastertabungan.PstJenisKredit"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
<%@page import="com.dimata.sedana.entity.kredit.PstPinjaman"%>
<%@page import="com.dimata.harisma.entity.employee.Employee"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.aiso.entity.masterdata.mastertabungan.JenisKredit"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.sedana.entity.kredit.Pinjaman"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file ="../../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION);%>
<%@ include file ="../../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%!    public static final String textListGlobal[][] = {
        {"Produksi", "Daftar Penjualan", "Pencarian", "Cari", "Barang", "Data Pengiriman", "Jadwal Pengiriman", "Pengiriman", "Status Pengiriman"},
        {"Production", "Selling List", "Search", "View", "Item", "Selling Data", "Dispatch Schedule", "Dispatch", "Status Dispatch"}
    };

    public static final String textListHeader[][] = {
        {"Nama", "No PK/Invoice", "Tanggal Pengajuan", "Tipe Penjualan", "Kategori Kredit", "Lokasi"},
        {"Name", "No PK/Invoice No", "Date Pengajuan", "Type Penjualan", "Category Credit", "Location"}
    };

    public static final String textHeaderTable[][] = {
        {"No", "No PK/Invoice", "Nama Konsumen", "Tanggal Pengajuan", "Tipe Penjualan", "Kategori Kredit", "Lokasi", "Jumlah Pengajuan", "Status", "Aksi"},
        {"No", "No PK/Invoice No", "Name Customer", "Date Pengajuan", "Type Penjualan", "Category Credit", "Location", "Total Pengajuan", "Status", "Action"}
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

<%    int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    long oid = FRMQueryString.requestLong(request, "oid");
    long oidDispatch = FRMQueryString.requestLong(request, "BILL_MAIN_ID");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    String invNum = FRMQueryString.requestString(request, "invoiceNum");
    String nomorbc = FRMQueryString.requestString(request, "nomorbc");

    Pinjaman pinjaman = new Pinjaman();
    BillMain billMain = new BillMain();
    Location location = new Location();
    JenisKredit typeKredit = new JenisKredit();
    Contact anggota = new Contact();
    Employee employee = new Employee();


%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="../../../styles/plugin_component.jsp" %>
        <title>Dimata - ProChain POS</title>
        <style>

            body {background-color: #EEE}

            .box .box-header, .box .box-footer {border-color: lightgray}

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
            .pad-0{
                padding: 0;
            }
            p {
                margin: 0 0 5px;
                font-size: 12px;
            }
            .row{
                margin:  0px;
            }
            .pad-left-0{
                padding-left: 0;
            }

            .box {
                position: relative;
                border-radius: 3px;
                background: #ffffff;
                border-top: 3px solid #d2d6de;
                margin-left: 15px;
                margin-right: 15px;
                margin-bottom: 20px;
                width: 100%;
                box-shadow: 0 1px 1px rgba(0, 0, 0, 0.1);
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
            }
            .nav-buat {
                margin-left: 15px;
            }
            a.btn.btn-success.pull-right {
                margin-right: 15px;
            }
            .box-header {
                color: #444;
                display: block;
                padding: 10px 25px 10px 10px;
                position: relative;
            }
            .modal-content{
                width: 850px;
            }
            .modal-dialog{
                width: 850px;
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
                <li>
                    <a  id="penjualan" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][6]%></a>
                </li>
                <li class="active">
                    <a id="serachPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][7]%></a>
                </li>
                <li> <a id="statusPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][8]%></a>
                </li>
            </ul>
        </section>				  
        <form id="FRM_NAME_BILL_MAIN" name="FRM_NAME_BILL_MAIN" action="" method="POST">	
            <input type="hidden" id="start_" name="start" value="<%=start%>">
            <input type="hidden" id="command_" name="command" value="<%= iCommand%>">
            <input type="hidden" id="oid_" name="oid" value="<%= oid%>">
            <input type="hidden" id="BILL_MAIN_ID" name="BILL_MAIN_ID" value="<%= oidDispatch%>"> 				  
            <div id="data-penjualan" class="box box-primary">
                <div class="box-header with-border">
                    <label class="box-title pull-left"><%=textListGlobal[SESS_LANGUAGE][5]%></label>
                    <div class="pull-right">
                        <a id="detail-item" class="btn btn-xs btn-primary"><i class="fa fa-file"></i> Item Detail</a>
                        <a id="update-status" class="btn btn-xs btn-success"><i class="fa fa-send"> </i> Send</a>
                    </div>
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="listPengiriman" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][0]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][1]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][2]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][3]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][4]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][5]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][6]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][7]%></th>
                                    <th>Delivery Date</th>
                                    <th>Delivery Courier</th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][8]%></th>
                                    <th><%=textHeaderTable[SESS_LANGUAGE][9]%></th>
                                </tr>
                            </thead> 
                            
                        </table>
                    </div>   
                    <p></p>
                    <div class="box-footer">
                    </div>
                </div>
            </div>
        </form>
        <!-- Modal -->
        <div class="modal fade" id="modalItem" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">Detail Item</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <table id="listItem" class="table table-striped table-bordered" style="width:100%">
                            <thead class="headerList">
                                <tr>
                                    <th>No</th>
                                    <th>Nama Konsumen</th>
                                    <th>SKU</th>
                                    <th>Name</th>
                                    <th>Barcode</th>
                                    <th>Color</th>
                                    <th>Type</th>
                                    <th>Qty</th>
                                </tr>
                            </thead>

                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {

                $("#penjualan").click(function () {
                    window.location.href = "src_penjualan.jsp";
                });
                $("#statusPengiriman").click(function () {
                    window.location.href = "status_pengiriman.jsp";
                });

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

                function runPengirimaTable() {
                    var elementidparent = 'data-penjualan';
                    var servlet = 'AjaxProduksi';
                    var dataFor = 'listPengirimanAll';
                    var elementid = 'listPengiriman';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }
                
                runPengirimaTable();

                $('#update-status').click(function () {
                    var formData = $('#FRM_NAME_BILL_MAIN').serialize();
                    var dataFor = "checkCourier";
                    var command = <%=Command.NONE%>;
                    var dataSend = "" + formData + "&FRM_FIELD_DATA_FOR=" + dataFor + "&command=" + command;
                    onDone = function (data) {
                        if (data.RETURN_ERROR_CODE == 0) {
                            documentSend();
                        } else {
                            alert(data.RETURN_MESSAGE);
                        }
                    };
                    onSuccess = function (data) {};
                    getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);

                });

                function documentSend() {
                    var formData = $('#FRM_NAME_BILL_MAIN').serialize();
                    var dataFor = "sendProduction";
                    var command = <%=Command.NONE%>;
                    var oidBill = $('.detail-produksi-btn').val();
                    var dataSend = "" + formData + "&FRM_FIELD_DATA_FOR=" + dataFor + "&command=" + command;
                    onDone = function (data) {
                        if (data.RETURN_ERROR_CODE == 0) {
                            alert(data.RETURN_MESSAGE);
                            window.location.href = "status_pengiriman.jsp";
                        } else {
                            alert(data.RETURN_MESSAGE);
                        }
                    };
                    onSuccess = function (data) {};
                    console.log(dataSend);
                    window.open("<%= approot%>/PrintSuratPerintahPengirimanBarang?" + formData + "&approot=<%= approot%>&sess_language=<%= SESS_LANGUAGE%>&bill_main_oid=" + oidBill);
                    getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);

                };

                $('body').on('click', '.show-list-item', function () {
                    var oid = $(this).data('oid');
                    console.log("OID: " + oid);
                    $('#modalItem').modal('show');
                    $('#modalItem').one('shown.bs.modal', function () {
                        var elementParentId = 'modalItem';
                        var elementId = 'listItem';
                        var servlet = 'AjaxPengiriman';
                        var dataFor = 'listItemDetail';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null, oid);
                    });
                });
                $('body').on('click', '#detail-item', function () {
                    var data = $('#FRM_NAME_BILL_MAIN');
                    var oid = data.serialize();
                    var oidDispatch = $('#BILL_MAIN_ID').val();
                    console.log(data.serialize());
                    console.log("OID: " + oid);
                    $('#modalItem').modal('show');
                    $('#modalItem').one('shown.bs.modal', function () {
                        var elementParentId = 'modalItem';
                        var elementId = 'listItem';
                        var servlet = 'AjaxPengiriman';
                        var dataFor = 'itemBanyak';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null, oid);
                    });
                });
            });
        </script>
    </body>
</html>












