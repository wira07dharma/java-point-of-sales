<%-- 
    Document   : rate_pasar_berlian
    Created on : Oct 24, 2017, 5:21:49 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.form.masterdata.CtrlRatePasarBerlian"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmRatePasarBerlian"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmColor"%>
<%@page import="com.dimata.marketing.form.marketingnewsinfo.FrmMarketingNewsInfo"%>
<%@page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page language = "java" %>
<%@ page import = "java.util.*" %>
<%@ page import = "com.dimata.util.*" %>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>

<%@ include file = "./../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MARKET_RATE); %>
<%@ include file = "./../main/checkuser.jsp" %>

<%!//
    public static final String textListHeader[][]
            = {
                {"Berita dan Informasi", "Judul", "Tgl Mulai", "Tgl Berakhir", "Keterangan", "Gambar", "Lokasi"},
                {"News and Info", "Title", "Valid Start", "Valid End", "Description", "Image", "Location"}
            };

    public static final String textListButton[][]
            = {
                {"Tambah Baru", "Tambah Rate Pasar Berlian", "Ubah Rate Pasar Berlian", "Unggah Gambar"},
                {"Add New", "Add News and Info", "Edit News and Info", "Upload Image"}
            };
%>

<%//    
    int iCommand = FRMQueryString.requestCommand(request);
    CtrlRatePasarBerlian ctrlRatePasarBerlian = new CtrlRatePasarBerlian(request);
    int iErrCode = ctrlRatePasarBerlian.action(iCommand, 0, userId, userName);
%>

<html>
    <head>
        <title>Dimata - ProChain POS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    </head> 

    <body class="" style="background-color:beige">
        <div class="nonprint">
            <link href="./../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
            <link href="./../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
            <link href="./../styles/plugin/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />

            <div class="row" style="margin-left:0px; margin-right:0px; margin-top:10px; ">
                <div class="col-md-12">
                    <div class="row">
                        <div class="col-md-12">
                            <div class='box box-primary'>
                                <div class='box-header'>
                                    <h3 class="box-title">Rate Pasar Berlian</h3>
                                </div>
                                <div class='box-footer'>
                                    <button class="btn btn-sm btn-primary btnaddgeneral" data-toggle="modal" data-target="#modalmodal" data-oid="0" data-for="showform">
                                        <i class="fa fa-plus"></i> Add Rate Pasar Berlian
                                    </button>
                                </div>
                                <div class="box-body">
                                    <div id="countryElement">
                                        <table id="datatable" style="font-size: 12px" class="table table-bordered table-striped" params='' data-action="ajax/datatable_rate_pasar_berlian.jsp">
                                            <thead>
                                                <tr>
                                                    <th style="width: 1%">No.</th>
                                                    <th>Code</th>
                                                    <th>Name</th>
                                                    <th>Rate</th>
                                                    <th>Update Date</th>
                                                    <th>Description</th>
                                                    <th>Status</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div><!-- ./col -->
                    </div><!-- /.row -->
                </div>
            </div>

        </div>
        <div id="modalmodal" class="modal fade nonprint in" tabindex="-1" aria-hidden="false">
            <div class="modal-dialog nonprint">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="addeditgeneral-title">Tambah Rate Pasar Berlian</h4>
                    </div>
                    <form method="post">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="<%=(FrmRatePasarBerlian.fieldNames[FrmRatePasarBerlian.FRM_FIELD_RATEPASARID])%>" value="0">
                        <div class="modal-body ">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="box-body addeditgeneral-body">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="Tanggal update rate pasar berlian di update"></i> Tanggal Update
                                                    </label>
                                                    <input required type="text" class="form-control" disabled value="<%=(Formater.formatDate(new Date(), "dd-MM-yyyy"))%>" data-required="required" data-number="false" data-alpha="true" data-special="" data-type="number" data-error-message="Harga beli error (harus berupa angka)">
                                                </div>
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="Isilah kolom ini dengan Code (format angka)"></i> Kode
                                                        <i style="color:red">*</i>
                                                    </label>
                                                    <input required type="text" class="form-control" name="<%=(FrmRatePasarBerlian.fieldNames[FrmRatePasarBerlian.FRM_FIELD_CODE])%>" value="" data-required="required" data-number="false" data-alpha="true" data-special="" data-type="number" data-error-message="Harga beli error (harus berupa angka)">
                                                </div>
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="Isilah kolom ini dengan nama"></i> Nama
                                                        <i style="color:red">*</i>
                                                    </label>
                                                    <input type="text" class="form-control" name="<%=(FrmRatePasarBerlian.fieldNames[FrmRatePasarBerlian.FRM_FIELD_NAME])%>" value="" data-required="required" data-number="true" data-alpha="true" data-special="true" data-type="number" data-error-message="Nama error" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="Isilah kolom ini dengan nama"></i> Rate
                                                        <i style="color:red">*</i>
                                                    </label>
                                                    <input type="text" class="form-control" name="<%=(FrmRatePasarBerlian.fieldNames[FrmRatePasarBerlian.FRM_FIELD_RATE])%>" value="" data-required="required" data-number="true" data-alpha="true" data-special="true" data-type="number" data-error-message="Nama error" required>
                                                </div>
                                                <div class="form-group">
                                                    <label>
                                                        <i class="fa fa-question-circle" data-toggle="tooltip" data-placement="right" title="Isilah kolom ini dengan deskripsi"></i> Deskripsi
                                                        <i style="color:red">*</i>
                                                    </label>
                                                    <textarea class="form-control date" name="<%=(FrmRatePasarBerlian.fieldNames[FrmRatePasarBerlian.FRM_FIELD_DESCRIPTION])%>" required></textarea>
                                                </div>
                                                <p>
                                                    <i style="color:red">* = (wajib diisi)</i>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-primary" id="btngeneralform"><i class="fa fa-check"></i> Save</button>
                            <button type="button" data-dismiss="modal" class="btn btn-danger"><i class="fa fa-ban"></i> Close</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="./../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
        <script src="./../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
        <script src="./../styles/dimata-app.js" type="text/javascript"></script>
        <script type="text/javascript" src="./../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script type="text/javascript" src="./../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
        <script src="./../styles/iCheck/icheck.min.js" type="text/javascript"></script>
        <script src="./../styles/plugin/datatables/jquery.dataTables.js" type="text/javascript"></script>
        <script src="./../styles/plugin/datatables/dataTables.bootstrap.js" type="text/javascript"></script>  
        <script>
          $(window).load(function () {
            $("#datatable").each(function () {
              var that = $(this);
              var servletName = $(this).data('action');
              var params = $(this).attr('params');
              var dataFor = $(this).data('for');
              var datafilter = "";
              var privUpdate = "";
              var command = 1;
              var dt = $(this).dataTable({
                "order": [[ 6, "asc" ], [ 1, "asc" ]],
                "responsive": true,
                "bDestroy": true,
                "iDisplayLength": 10,
                "bProcessing": true,
                "bServerSide": true,
                "sAjaxSource": baseUrl(servletName + "?command=" + command + "&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=" + baseUrl() + "&params=" + params),
                "oLanguage": {
                  "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
                },
                "initComplete": function (settings, json) {

                },
                "fnDrawCallback": function (oSettings) {
                  $(that).find(".money").each(function () {
                    jMoney(this);
                  });
                  (!$(that).data('invoke')) || dataTableInvoker[$(that).data('invoke')](that);
                },
                "fnPageChange": function (oSettings) {

                }
              });
              new $.fn.dataTable.FixedHeader(dt);
            });
          });
        </script>
    </body>

</html>
