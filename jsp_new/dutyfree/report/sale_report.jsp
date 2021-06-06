<%-- 
    Document   : sale_report
    Created on : Oct 7, 2019, 3:11:58 PM
    Author     : IanRizky
--%>

<%@page import="org.json.JSONObject"%>
<%@page import="com.dimata.posbo.session.sales.SessSalesReport"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchBill"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchBill"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@page import="com.dimata.pos.entity.billing.PstBillMain"%>
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
    String kodeBarang = FRMQueryString.requestString(request, "KODE_BARANG");
    String seriBarang = FRMQueryString.requestString(request, "SERI_BARANG");
    String namaPembeli = FRMQueryString.requestString(request, "NAMA_PEMBELI");

    command = Command.LIST;
    if (dateFrom == "") {
        dateFrom = Formater.formatDate(new Date(), "yyyy-MM-01");
    }
    if (dateTo == "") {
        dateTo = Formater.formatDate(new Date(), "yyyy-MM-dd");
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
            .form-group label {margin-top: 5px; width: 130px}

            .tabel-data, .tabel-data thead tr th, .tabel-data tbody tr td {
                font-size: 12px;
                border-color: #DDD;
            }

            .tabel-data thead tr th{
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
                <h4>Laporan Penjualan Barang</h4>
            </div>

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <div class="box-body">
                    <form name="frm" id="formSearch" class="form-horizontal">
                        <input  type="hidden" name="command" value="<%=Command.LIST%>">
                        <p></p>
                        <div class="col-sm-6">
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
                                <label class="col-sm-1">Nama Pembeli</label>
                                <div class="col-sm-8">
                                    <input type="text" placeholder="Nama Pembeli" class="form-control input-sm" name="NAMA_PEMBELI" value="<%=namaPembeli%>">
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
                        <thead>
                            <tr class="bg-primary">
                                <td style="width: 1%; text-align: center; vertical-align: middle" rowspan="2" colspan="1">NO</td>
                                <td colspan="3" rowspan="1" style="text-align: center">DATA PENJUALAN</td>
                                <td colspan="4" rowspan="1" style="text-align: center">DATA PEMBELI</td>
                                <td colspan="5" rowspan="1" style="text-align: center">DATA BARANG</td>
                            </tr>
                            <tr class="bg-primary">
                                <td style="text-align: center">TGL</td>
                                <td style="text-align: center">JAM</td>
                                <td style="text-align: center">NOMOR INVOICE</td>
                                <td style="text-align: center">NAMA</td>
                                <td style="text-align: center">JENIS DOKUMEN</td>
                                <td style="text-align: center">NO DOKUMEN</td>
                                <td style="text-align: center">NO PASPOR</td>
                                <td style="text-align: center">KODE BARANG</td>
                                <td style="text-align: center">SERI BARANG</td>
                                <td style="text-align: center">NAMA BARANG</td>
                                <td style="text-align: center">JML</td>
                                <td style="text-align: center">SAT</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%//
                                SessSalesReport salesReport = new SessSalesReport();
                                salesReport.setDateStart(dateFrom);
                                salesReport.setDateEnd(dateTo);
                                salesReport.setItemCategoryCode(kodeBarang);
                                salesReport.setItemSku(seriBarang);
                                salesReport.setCustomerName(namaPembeli);
                                ArrayList<JSONObject> listBill = SessSalesReport.listSales(salesReport);
                                int no = 0;
                                for (JSONObject object : listBill) {
                                    no++;
                            %>

                            <tr>
                                <td><%=no%>.</td>
                                <td><%=object.getString("DATE")%></td>
                                <td><%=object.getString("TIME")%></td>
                                <td><%=object.getString("INVOICE")%></td>
                                <td><%=object.getString("NAME")%></td>
                                <td><%=object.getString("JENIS_DOKUMEN")%></td>
                                <td><%=object.getString("NOMOR_DOKUMEN")%></td>
                                <td><%=object.getString("NOMOR_PASSPORT")%></td>
                                <td><%=object.getString("KODE_BARANG")%></td>
                                <td><%=object.getString("SERI_BARANG")%></td>
                                <td><%=object.getString("NAMA_BARANG")%></td>
                                <td><%=object.getDouble("JUMLAH")%></td>
                                <td><%=object.getString("SATUAN")%></td>
                            </tr>

                            <%
                                }
                            %>  
                        </tbody>
                    </table>
                </div>
            </div>
            <% }%>
        </div>

    </body>
</html>