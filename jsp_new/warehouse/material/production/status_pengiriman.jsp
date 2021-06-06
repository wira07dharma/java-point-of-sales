<%-- 
    Document   : status_pengiriman
    Created on : Nov 12, 2019, 11:03:11 AM
    Author     : Regen
--%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.dimata.posbo.form.warehouse.FrmBillImageMapping"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlBillImageMapping"%>
<%@page import="com.dimata.sedana.session.SessEmpRelevantDoc"%>
<%@page import="com.dimata.util.blob.ImageLoader"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstBillImageMapping"%>
<%@page import="com.dimata.posbo.entity.warehouse.BillImageMapping"%>
<%@page import="org.json.JSONArray"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.common.entity.custom.DataCustom"%>
<%@page import="com.dimata.services.WebServices"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
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
<%!  public static final String textListGlobal[][] = {
        {"Produksi", "Daftar Penjualan", "Pencarian", "Cari", "Barang", "Data Status Pengiriman", "Jadwal Pengiriman", "Pengiriman", "Status Pengiriman"},
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
<%  int start = FRMQueryString.requestInt(request, "start");
    int iCommand = FRMQueryString.requestCommand(request);
    long oid = FRMQueryString.requestLong(request, "oid");
    long oidDispatch = FRMQueryString.requestLong(request, "BILL_MAIN_ID");
    long oidDokumen = FRMQueryString.requestLong(request, "DOKUMEN_ID");
    int appCommand = FRMQueryString.requestInt(request, "approval_command");
    String invNum = FRMQueryString.requestString(request, "invoiceNum");
    String nomorbc = FRMQueryString.requestString(request, "nomorbc");
    String file = "FILE_NAME";
    String dokumen = FRMQueryString.requestString(request, "DOKUMEN_NAME");
    String keterangan = FRMQueryString.requestString(request, "KETERANGAN");

    String errMsg = "";
    int iErrCode = FRMMessage.ERR_NONE;

    Pinjaman pinjaman = new Pinjaman();
    BillMain billMain = new BillMain();
    Location location = new Location();
    JenisKredit typeKredit = new JenisKredit();
    Contact anggota = new Contact();
    Employee employee = new Employee();
    Billdetail billdetail = new Billdetail();
    Material material = new Material();
    BillImageMapping bim = new BillImageMapping();
    String fieldFormName = "";

    ImageLoader uploader = new ImageLoader();
    int numFiles = uploader.uploadImage(config, request, response);
    String fileName = "" + String.valueOf(new Date() + "_" + uploader.getFileName());
    System.out.println("fileName real..." + fileName);
    try {
        fieldFormName = file;
        Object obj = uploader.getImage(fieldFormName);
        System.out.println("obj..." + obj);

        byte[] byteOfObj = (byte[]) obj;
        int intByteOfObjLength = byteOfObj.length;
        if (intByteOfObjLength > 0) {

            long oidBlobEmpPicture = 0;
            SessEmpRelevantDoc objSessEmpRelevantDoc = new SessEmpRelevantDoc();
            String pathFileName = objSessEmpRelevantDoc.getAbsoluteFileName(fileName);
            java.io.ByteArrayInputStream byteIns = new java.io.ByteArrayInputStream(byteOfObj);
            int result = uploader.writeCache(byteIns, pathFileName, true);

        }
    } catch (Exception e) {
        System.out.println("Exc1 when upload image : " + e.toString());
    }
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
            .ft-st {
                padding-top: 15%;
                text-align: center;
            }
            .modal-dialog {
                width: 90% !important;
                margin: 30px auto;
            }
            .tab-group {
                position: relative;
                margin-top: 0 !important;
                border-radius: 0 0 10px 10px;
            }
            .modal-backdrop.fade.in {
                z-index: 1;
            }
            .carousel-inner {
                position: relative;
                overflow: hidden;
                width: 100%;
                height: 465px;
            }
            #data-dokumen{
                /*        width: 500px;
                        height: 300px;*/
            }
            img{
                max-width: 100% !important;
                max-height: 100%;
                display: block; /* remove extra space below image */ 
                height: 365px !important;
            }
            .center {
                display: block;
                margin-left: auto;
                margin-top: auto;
                margin-bottom: auto;
                margin-right: auto;
                /*      width: 50%;*/
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
                <li  >
                    <a  id="penjualan" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][6]%></a>
                </li>
                <li>
                    <a id="serachPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][7]%></a>
                </li>
                <li class="active"> <a id="statusPengiriman" data-toggle="tab"><%=textListGlobal[SESS_LANGUAGE][8]%></a>
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
                </div>
                <div class="box-body">
                    <div class="table-responsive">
                        <table id="listStPengiriman" class="table table-striped table-bordered" style="width:100%">
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
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Status-->
        <div class="modal fade" id="modalStatus" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">Status</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="oidBillMain_" name="oidBillMain" value="">
                        <input type="hidden" id="employee_" name="employee" value="">

                        <div class="form-group">
                            <label for="note_">Catatan</label>
                            <textarea class="form-control" id="note_" rows="3" style="resize: none;"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <div class="text-center">
                            <button class="btn btn-warning pengiriman-btn" value="<%= PstBillMain.PETUGAS_DELIVERY_STATUS_ON_PRODUCTION%>"><i class="fa fa-calendar-times-o"></i> Reschedule </button>
                            <button class="btn btn-success pengiriman-btn" value="<%= PstBillMain.PETUGAS_DELIVERY_STATUS_DITERIMA%>"><i class="fa fa-calendar-check-o"></i> Diterima </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Document Image-->
        <div class="modal fade" id="modalDokumen" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true" style="z-index: 10;">
            <div class="modal-dialog" role="document">
                <div class="modal-content" style="min-height: 480px;">
                    <div class="modal-header">
                        <label class="modal-title" id="exampleModalLabel">Dokumen Item</label>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="billOid_" name="oidBillMain" value="">

                        <div class="tab-group">
                            <section id="tab1" title="View Dokumen">
                                <div class="box box-prochain" style="margin-left: 0 !important;">
                                    <div class="box-body" style="background-color: #607d8b;height: 400px;">
                                        <div class="row">
                                            <div class="col-md-12">

                                                <div id="myCarousel" class="carousel slide" data-ride="carousel" style="width: 60% !important;height: 400px !important;margin: auto;">

                                                    <div id="data-dokumen"></div>

                                                    <!-- Left and right controls -->
                                                    <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                                                        <span class="glyphicon glyphicon-chevron-left"></span>
                                                        <span class="sr-only">Previous</span>
                                                    </a>

                                                    <a class="right carousel-control" href="#myCarousel" data-slide="next">
                                                        <span class="glyphicon glyphicon-chevron-right"></span>
                                                        <span class="sr-only">Next</span>
                                                    </a>

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>

                            <section id="tab2" title="Detail Dokumen">
                                <div class="box box-prochain" style="margin-left: 0 !important;">
                                    <div class="box-body">            
                                        <div class="row">
                                            <div class="col-md-12"> 
                                                <div class="row">
                                                    <div class="table-responsive">
                                                        <table id="list-dokumen" class="dataTable table table-striped table-bordered" style="width:100%">
                                                            <thead class="headerList">
                                                                <tr>
                                                                    <th>No</th>
                                                                    <th>Nama Dokumen</th>
                                                                    <th>Nama File</th>
                                                                    <th>Deskripsi</th>
                                                                    <th>Aksi</th>
                                                                </tr>
                                                            </thead>
                                                        </table>
                                                    </div>   
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </section>
                            <section id="tab3" title="Tambah Dokumen">
                                <div class="box box-prochain" style="margin-left: 0 !important;">
                                    <div class="box-body" style="min-height: 115px !important;padding-top: 35px;">            
                                        <div class="row">
                                            <form class="form-horizontal" id="frmDokumen" name="frmDokumen">
                                                <div class="col-md-12"> 
                                                    <div class="row">
                                                        <div class="col-md-7">
                                                            <div class="form-group">
                                                                <label class="col-sm-4 font-small">Dokumen Name</label>
                                                                <div class="col-sm-8">
                                                                    <input type="text" class="form-control" id="DOKUMEN_NAME" name="DOKUMEN_NAME" placeholder="Dokumen Name" >
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-5">
                                                            <div class="form-group">
                                                                <label class="col-sm-4 font-small">Keterangan</label>
                                                                <div class="col-sm-8">
                                                                    <textarea class="form-control" id="KETERANGAN" name="KETERANGAN" placeholder="..." ></textarea>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="box-footer">
                                        <div class="pull-right">
                                            <button type="button" class="btn btn-default dokumen-btn" data-dismiss="modal" aria-label="Close"> Kembali </button>
                                            <button type="button" class="btn btn-primary dokumen-tambah"><i class="fa fa-save"></i> Simpan </button>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </div>
                    </div>

                </div>
            </div>
        </div>

        <script>
            $(document).ready(function () {
    //        $('#listItem').DataTable({
    //          "paging": false,
    //          "lengthChange": false,
    //          "searching": false,
    //          "ordering": false,
    //          "info": false,
    //          "autoWidth": true,
    //        });

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
                    var dataFor = 'listStatusPengiriman';
                    var elementid = 'listStPengiriman';
                    runDataTable(elementidparent, elementid, servlet, dataFor, null);
                }
                
                runPengirimaTable();
                
                $('body').on('click', '.btn-send-barang', function () {
                    var cbmOID = $(this).data('cbmoid');
                    var empOID = $(this).data('empoid');
                    var sendNote = $(this).data('sendnote');
                    var status = $(this).val();
                    var modalStatus = $('#modalStatus');
                    modalStatus.modal('show');
                    modalStatus.one('shown.bs.modal', function () {
                        modalStatus.find('.modal-body #oidBillMain_').val(cbmOID);
                        modalStatus.find('.modal-body #employee_').val(empOID);
                        modalStatus.find('.modal-body #note_').val(sendNote);
                    });

                });

//                $('#listStPengiriman').DataTable({
//                    "ordering": false
//                });


                $("#penjualan").click(function () {
                    window.location.href = "src_penjualan.jsp";
                });

                $("#serachPengiriman").click(function () {
                    window.location.href = "src_pengiriman.jsp";
                });

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

                $('body').on('click', '.btn-dokumen', function () {

                    var cbmOID = $(this).data('bmoid');
                    var oid = $(this).data('bmoid');
                    $('#modalDokumen').modal('show');
                    $('#modalDokumen').one('shown.bs.modal', function () {

                        //Get Dokumen View
                        var modalDokumen = $('#modalDokumen');
                        modalDokumen.find('.modal-body #billOid_').val(cbmOID);
                        console.log("OID: " + cbmOID);
                        var dataSend = {
                            "FRM_FIELD_DATA_FOR": "getDokumen",
                            "BILL_MAIN_OID": cbmOID,
                            "command": "<%= Command.NONE%>"
                        };
                        onDone = function (data) {
                            $('#data-dokumen').html(data.FRM_FIELD_HTML);
                        };
                        onSuccess = function (data) {};
                        getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);

                        //Get List Dokumen
                        var elementParentId = 'modalDokumen';
                        var elementId = 'list-dokumen';
                        var servlet = 'AjaxPengiriman';
                        var dataFor = 'listDokumenDetail';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null, oid);

                    });
                });

                $('body').on('click', '.btn-upload', function () {
                    var bimOID = $(this).data('oidbim');
                    var oid = $(this).data('bmain');
                    var url = "/warehouse/material/production/file_document.jsp";
                    window.open("<%=approot%>" + url + "?command=" +<%=Command.EDIT%> + "&dokumen_id=" + bimOID + "&bill_main_id=" + oid, null, "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");

                });


                $('body').on('click', '.delete-dokumen', function () {
                    var bimOID = $(this).data('oidbim');
                    var oid = $(this).data('bmain');
                    var dataSend = {
                        "FRM_FIELD_DATA_FOR": "deleteDokumen",
                        "BILL_MAIN_OID": oid,
                        "oid": bimOID,
                        "command": "<%= Command.DELETE%>"
                    };
                    onDone = function (data) {
                        alert(data.RETURN_MESSAGE);

                        //Get List Dokumen
                        var elementParentId = 'modalDokumen';
                        var elementId = 'list-dokumen';
                        var servlet = 'AjaxPengiriman';
                        var dataFor = 'listDokumenDetail';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null, oid);
                    };
                    onSuccess = function (data) {};
                    getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);

                });

                $('body').on('click', '.pengiriman-btn', function () {
                    var status = $(this).val();
                    var cbmoid = $('#oidBillMain_').val();
                    var empoid = $('#employee_').val();
                    var notes = $('#note_').val();
                    if (notes == "" || notes == "-" || notes == " ") {
                        alert("Catatan sangat diperlukan dalam transaksi barang");
                    } else {
                        var dataSend = {
                            "FRM_FIELD_DATA_FOR": "updatePengiriman",
                            "DELIVERY_STATUS": status,
                            "BILL_MAIN_OID": cbmoid,
                            "EMPLOYEE_OID": empoid,
                            "DELIVERY_NOTES": notes,
                            "command": "<%= Command.SAVE%>"
                        };
                        onDone = function (data) {
                            alert(data.RETURN_MESSAGE);
                            window.location.href = "src_penjualan.jsp";
                        };
                        onSuccess = function (data) {};

                        if (confirm("Update status pengiriman?")) {
                            getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);
                        }
                    }
                });

                $('.dokumen-tambah').click(function () {
                    var dokumen = $('#DOKUMEN_NAME').val();
                    var oid = $('#billOid_').val();
                    var keterangan = $('#KETERANGAN').val();
                    var modalDokumen = $('#modalDokumen');

                    var dataSend = {
                        "FRM_FIELD_DATA_FOR": "saveDokumen",
                        "DOKUMEN_NAME": dokumen,
                        "KETERANGAN": keterangan,
                        "BILL_MAIN_OID": oid,
                        "command": "<%= Command.SAVE%>"
                    };
                    onDone = function (data) {
                        alert(data.RETURN_MESSAGE);

                        //Get List Dokumen
                        var elementParentId = 'modalDokumen';
                        var elementId = 'list-dokumen';
                        var servlet = 'AjaxPengiriman';
                        var dataFor = 'listDokumenDetail';
                        runDataTable(elementParentId, elementId, servlet, dataFor, null, oid);
                        $('#DOKUMEN_NAME').val("");
                        $('#KETERANGAN').val("");

                    };
                    onSuccess = function (data) {};

                    getDataFunction(onDone, onSuccess, dataSend, "AjaxPengiriman", null, false);
                });


                $(function () {
                    console.log("Document is Ready broo!!");
                    $('#modalST').click(function () {
                        var data = $('#FRM_NAME_BILL_MAIN');
                        var oidDispatch = $('#BILL_MAIN_ID').val();
                        var oid = $('#oid').val();
                        var cmd = "<%= Command.EDIT%>"
                        console.log(data.serialize());
                        $.ajax({
                            type: 'GET',
                            url: "<%= approot%>/AjaxPengiriman?command=" +<%=Command.SAVE%> + "&oid=" + oid + "&type=sendST&BILL_MAIN_ID=" + oidDispatch,
                            data: data.serialize(),
                            cache: false,
                            dataType: 'JSON',
                            success: function (data) {
                            }
                        }).done(function () {
                            window.location.href = "status_pengiriman.jsp";
                        });
                    });
                });
            });

            ;
            (function (defaults, $, window, document, undefined) {

                'use strict';

                $.extend({
                    tabifySetup: function (options) {
                        return $.extend(defaults, options);
                    }
                }).fn.extend({
                    tabify: function (options) {
                        options = $.extend({}, defaults, options);
                        return $(this).each(function () {
                            var $element, tabHTML, $tabs, $sections;
                            $element = $(this);
                            $sections = $element.children();
                            // Build tabHTML
                            tabHTML = '<ul class="tab-nav">';
                            $sections.each(function () {
                                if ($(this).attr("title") && $(this).attr("id")) {
                                    tabHTML += '<li><a href="#' + $(this).attr("id") + '">' + $(this).attr("title") + '</a></li>';
                                }
                            });
                            tabHTML += '</ul>';
                            // Prepend navigation
                            $element.prepend(tabHTML);
                            // Load tabs
                            $tabs = $element.find('.tab-nav li');
                            // Functions
                            var activateTab = function (id) {
                                $tabs.filter('.active').removeClass('active');
                                $sections.filter('.active').removeClass('active');
                                $tabs.has('a[href="' + id + '"]').addClass('active');
                                $sections.filter(id).addClass('active');
                            }
                            // Setup events
                            $tabs.on('click', function (e) {
                                activateTab($(this).find('a').attr('href'));
                                e.preventDefault();
                            });
                            // Activate first tab
                            activateTab($tabs.first().find('a').attr('href'));
                        });
                    }
                });
            })({
                property: "value",
                otherProperty: "value"
            }, jQuery, window, document);

            // Calling the plugin
            $('.tab-group').tabify();

        </script>
    </body>
</html>






