<%-- 
    Document   : stock_report
    Created on : Oct 7, 2019, 3:12:49 PM
    Author     : IanRizky
--%>


<%@page import="com.dimata.posbo.session.warehouse.SessReportPotitionStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.search.SrcReportPotitionStock"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    String dateFrom = FRMQueryString.requestString(request, "TGL_AWAL");
    String dateTo = FRMQueryString.requestString(request, "TGL_AKHIR");
    String namaBarang = FRMQueryString.requestString(request, "NAMA_BARANG");
    String kodeBarang = FRMQueryString.requestString(request, "KODE_BARANG");
    String seriBarang = FRMQueryString.requestString(request, "SERI_BARANG");
    long idLokasi = FRMQueryString.requestLong(request, "FRM_LOKASI");

    command = Command.LIST;
    if (dateFrom == "") {
        dateFrom = Formater.formatDate(new Date(), "yyyy-MM-01");
    }
    if (dateTo == "") {
        dateTo = Formater.formatDate(new Date(), "yyyy-MM-dd");
    }

    SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
    SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
    if (command == Command.LIST) {
        try {
            if (idLokasi > 0) {
                srcReportPotitionStock.setLocationId(idLokasi);
            }
            Material material = PstMaterial.fetchBySku(seriBarang);
            if (material.getOID() != 0) {
                if (kodeBarang.length() > 0) {
                    String whereCategory = PstCategory.fieldNames[PstCategory.FLD_CODE] + "=" + kodeBarang;
                    Vector listCat = PstCategory.list(0, 0, whereCategory, "", "");
                    if (listCat.size() > 0) {
                        Category category = (Category) listCat.get(0);
                        srcReportPotitionStock.setCategoryId(category.getOID());
                    }
                } else {

                }
                srcReportPotitionStock.setSku(seriBarang);
                Vector status = new Vector();
                status.add("" + I_DocStatus.DOCUMENT_STATUS_FINAL);
                status.add("" + I_DocStatus.DOCUMENT_STATUS_CLOSED);
                status.add("" + I_DocStatus.DOCUMENT_STATUS_POSTED);
            }
        } catch (Exception exc) {
        }

    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" media="screen" href="../../styles/datepicker/bootstrap-datetimepicker.min.css"/>

        <link href="../../styles/plugin/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/plugin/datatables/buttons.bootstrap.min.css" rel="stylesheet" type="text/css" />

        <style>
            .form-group label {margin-top: 5px; width: 160px}

            .tabel-data, .tabel-data thead tr th, .tabel-data tbody tr td {
                font-size: 12px;
                border-color: #DDD;
            }

            .tabel-data thead tr th{
                text-align: center;
                vertical-align: middle;
            }

            .tabel-data tr th{
                text-align: center;
                vertical-align: middle;
            }

            .table-condensed {font-size: 14px;}
            
        </style>

        <script type="text/javascript" src="../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>

        <script src="../../styles/plugin/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/dataTables.buttons.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/buttons.bootstrap.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/jszip.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/pdfmake.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/vfs_fonts.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/buttons.html5.min.js" type="text/javascript"></script>
        <script src="../../styles/plugin/datatables/buttons.print.min.js" type="text/javascript"></script>

        <script>
            $(document).ready(function () {
                $('.datepick').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd',
                    minView: 2
                });

                /*DATATABLE SETTING*/
                function setDataTable(elementId) {
                    $(elementId).DataTable({
                        ordering: false,
                        dom: 'Blfrtip',
                        buttons: [
                            {
                                "extend": 'excelHtml5',
                                "title": 'Laporan Persediaan Barang'
                            },
                            {
                                "extend": 'pdfHtml5',
                                "title": 'Laporan Persediaan Barang'
                            },
                            {
                                "extend": 'print',
                                "title": ''
                            }
                        ]
                    });
                    $(elementId).css({'width': '100%'});
                    $(elementId + '_wrapper .buttons-excel').html('Export Excel').removeClass('btn-default').addClass('btn-sm btn-success text-bold');
                    $(elementId + '_wrapper .buttons-pdf').html('Export PDF').removeClass('btn-default').addClass('btn-sm btn-danger text-bold');
                    $(elementId + '_wrapper .buttons-print').html('Print').removeClass('btn-default').addClass('btn-sm btn-info text-bold');
                    $('.dt-buttons').removeClass('btn-group').css({'margin-bottom': '10px'});
                    $(elementId + '_length').css({'float': 'left'});
                    $(elementId + '_filter').css({'float': 'right'});
                    $(elementId + '_info').css({'float': 'left'});
                    $(elementId + '_paginate').css({'float': 'right'});
                    $('.dataTables_empty').css({'text-align': 'center', 'background-color': 'lightgray'});
                }

                setDataTable('#datareport');

                $('#formSearch').submit(function () {
                    $('#btnSearch').attr({'disabled': true}).html('<i class="fa fa-spinner fa-pulse"></i> Tunggu...');
                });

            });
        </script>
    </head>
    <body style="background-color: #eeeeee">
        <div class="col-md-12 nonPrint">

            <div class="">
                <h4>Laporan Persediaan Barang</h4>
            </div>

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <div class="box-body">
                    <form name="frm" id="formSearch" class="form-horizontal">
                        <input type="hidden" name="command" value="<%=Command.LIST%>">
                        <p></p>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-1">Nama Barang</label>
                                <div class="col-sm-8">
                                    <input type="text" placeholder="Nama Barang" class="form-control input-sm" name="NAMA_BARANG" value="<%=namaBarang%>">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1">Kode Barang</label>
                                <div class="col-sm-8">
                                    <input type="text" placeholder="Kode Barang" class="form-control input-sm" name="KODE_BARANG" value="<%=kodeBarang%>">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1">Seri Barang</label>
                                <div class="col-sm-8">
                                    <input type="text" placeholder="Seri Barang" class="form-control input-sm" name="SERI_BARANG" value="<%=seriBarang%>">
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-1">Tanggal</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" required="" placeholder="Tanggal awal" id="tglAwal" class="form-control input-sm datepick" name="TGL_AWAL" value="<%=dateFrom%>">
                                        <span class="input-group-addon">s/d</span>
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" required="" placeholder="Tanggal akhir" id="tglAkhir" class="form-control input-sm datepick" name="TGL_AKHIR" value="<%=dateTo%>">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-1">Lokasi</label>
                                <div class="col-sm-8">
                                    <%
                                        Vector val_locationid = new Vector(1, 1);
                                        Vector key_locationid = new Vector(1, 1);
                                        String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_DUTY_FREE;
                                        //whereClause += " AND " + PstDataCustom.whereLocReportView(userId, "user_create_document_location");
                                        Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
                                        val_locationid.add(String.valueOf(0));
                                        key_locationid.add("Semua lokasi");
                                        for (int d = 0; d < vt_loc.size(); d++) {
                                            Location loc = (Location) vt_loc.get(d);
                                            val_locationid.add("" + loc.getOID() + "");
                                            key_locationid.add(loc.getName());
                                        }
                                    %>                 
                                    <%=ControlCombo.draw("FRM_LOKASI", null, "" + idLokasi, val_locationid, key_locationid, "", "form-control input-sm")%>
                                </div>
                            </div>
                        </div>
                    </form>                    
                </div>

                <div class="box-footer" style="border-color: lightgray">
                    <button type="submit" form="formSearch" id="btnSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                </div>

            </div>

            <% if (command == Command.LIST) { %>
            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Hasil Pencarian</h3>
                </div>

                <div class="box-body">
                    <table id="datareport" class="table table-bordered tabel-data">
                        <thead style="text-align: center">
                            <tr class="bg-primary">
                                <td>NO</td>
                                <td>KODE BARANG</td>
                                <td>NAMA BARANG</td>
                                <td>SAT</td>
                                <td>SALDO AWAL</td>
                                <td>PEMASUKAN</td>
                                <td>PENGELUARAN / PENJUALAN</td>
                                <td>PENYESUAIAN <br> (ADJUSTMENT)</td>
                                <td>SALDO AKHIR</td>
                                <td>STOCK OPNAME</td>
                                <td>SELISIH</td>
                                <td>KET</td>
                            </tr>
                        </thead>
                        <tbody>
                            <% objSessReportPotitionStock.getReportStockAllDutyFree(out, 0, 0, SESS_LANGUAGE, 0, srcReportPotitionStock, true, 0, 0, "");%>
                        </tbody>
                    </table>
                </div>
            </div>
            <% }%>

        </div>

    </body>
</html>