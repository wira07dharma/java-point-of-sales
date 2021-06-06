<%-- 
    Document   : in_out_item
    Created on : Oct 7, 2019, 3:04:14 PM
    Author     : IanRizky
--%>

<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstUnit"%>
<%@page import="com.dimata.posbo.entity.warehouse.StockCardReport"%>
<%@page import="com.dimata.posbo.session.warehouse.SessStockCard"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.posbo.entity.search.SrcStockCard"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatchBill"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatReceive"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceive"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
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
    String jenisDokumen = FRMQueryString.requestString(request, "JENIS_DOKUMEN");
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

    Vector records = new Vector();
    SrcStockCard srcStockCard = new SrcStockCard();
    Material material = new Material();
    Unit unit = new Unit();
    Category category = new Category();
    if (command == Command.LIST) {
        try {
            srcStockCard.setStardDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom));
            srcStockCard.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateTo));
            if (idLokasi > 0) {
                srcStockCard.setLocationId(idLokasi);
            }
            Vector status = new Vector();
            status.add("" + I_DocStatus.DOCUMENT_STATUS_FINAL);
            status.add("" + I_DocStatus.DOCUMENT_STATUS_CLOSED);
            status.add("" + I_DocStatus.DOCUMENT_STATUS_POSTED);
            srcStockCard.setDocStatus(status);

            //CARI ITEM BERDASARKAN PARAMETER PENCARIAN
            //CARI KATEGORI
            if (!kodeBarang.isEmpty()) {
                Vector<Category> listCat = PstCategory.list(0, 1, PstCategory.fieldNames[PstCategory.FLD_CODE] + " = '" + kodeBarang + "'", "");
                if (!listCat.isEmpty()) {
                    category = listCat.get(0);
                }
            }
            //CARI MATERIAL
            String whereMat = "";
            if (category.getOID() != 0) {
                whereMat += whereMat.isEmpty() ? "" : " AND ";
                whereMat += PstMaterial.fieldNames[PstMaterial.FLD_CATEGORY_ID] + " = " + category.getOID();
            }
            if (!seriBarang.isEmpty()) {
                whereMat += whereMat.isEmpty() ? "" : " AND ";
                whereMat = PstMaterial.fieldNames[PstMaterial.FLD_SKU] + " = '" + seriBarang + "'";
            }
            if (!namaBarang.isEmpty()) {
                whereMat += whereMat.isEmpty() ? "" : " AND ";
                whereMat += PstMaterial.fieldNames[PstMaterial.FLD_NAME] + " LIKE '%" + namaBarang + "%'";
            }
            Vector<Material> listMat = PstMaterial.list(0, 1, whereMat, "");
            if (!listMat.isEmpty()) {
                material = listMat.get(0);
                unit = PstUnit.fetchExc(material.getDefaultStockUnitId());
                category = PstCategory.fetchExc(material.getCategoryId());
                srcStockCard.setMaterialId(material.getOID());
            }
            records = SessStockCard.createHistoryStockCard(srcStockCard);
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
    <body style="background-color: #eeeeee;">
        <div class="col-md-12 nonPrint">
            <div class="">
                <h4>Laporan Pemasukan dan Pengeluaran Barang per Dokumen Pabean</h4>
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
                                <label class="col-sm-1">Tanggal Masuk</label>
                                <div class="col-sm-8">
                                    <div class="input-group">
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" placeholder="Tanggal masuk awal" id="tglAwal" class="form-control input-sm datepick" name="TGL_AWAL" value="<%=dateFrom%>">
                                        <span class="input-group-addon">s/d</span>
                                        <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                        <input type="text" placeholder="Tanggal masuk akhir" id="tglAkhir" class="form-control input-sm datepick" name="TGL_AKHIR" value="<%=dateTo%>">
                                    </div>
                                </div>
                            </div>
                            <!--        
                            <div class="form-group">
                                <label class="col-sm-1">Jenis Dokumen</label>
                                <div class="col-sm-8">
                                    <input type="text" placeholder="Jenis Dokumen" class="form-control input-sm" name="JENIS_DOKUMEN" value="<%=jenisDokumen%>">
                                </div>
                            </div>
                            -->
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
                    <div style="overflow-x: auto">
                        <table id="datareport" class="table table-bordered tabel-data">
                            <thead>
                                <tr class="bg-primary">
                                    <td style="width: 1%; text-align: center; vertical-align: middle" rowspan="2" colspan="1">NO</td>
                                    <td colspan="11" rowspan="1" style="text-align: center">DOKUMEN PEMASUKAN</td>
                                    <td colspan="4" rowspan="1" style="text-align: center">SALDO BARANG <br> (AWAL PERIODE)</td>
                                    <td colspan="9" rowspan="1" style="text-align: center">DOKUMEN PENGELUARAN</td>
                                    <td colspan="4" rowspan="1" style="text-align: center">SALDO BARANG <br> (AKHIR PERIODE)</td>
                                </tr>
                                <tr class="bg-primary">
                                    <td style="text-align: center">JENIS</td>
                                    <td style="text-align: center">NO</td>
                                    <td style="text-align: center">TGL BC</td>
                                    <td style="text-align: center">TGL MASUK</td>
                                    <td style="text-align: center">KODE BARANG</td>
                                    <td style="text-align: center">SERI BARANG</td>
                                    <td style="text-align: center">NAMA BARANG</td>
                                    <td style="text-align: center">SAT</td>
                                    <td style="text-align: center">JMLH</td>
                                    <td style="text-align: center">NILAI PAB (Rp)</td>
                                    <td style="text-align: center">NILAI PAB ($)</td>
                                    <td style="text-align: center">JMLH</td>
                                    <td style="text-align: center">SAT</td>
                                    <td style="text-align: center">NILAI PAB (Rp)</td>
                                    <td style="text-align: center">NILAI PAB ($)</td>
                                    <td style="text-align: center">JENIS</td>
                                    <td style="text-align: center">NO</td>
                                    <td style="text-align: center">TGL</td>
                                    <td style="text-align: center">TGL KELUAR</td>
                                    <td style="text-align: center">NAMA BARANG</td>
                                    <td style="text-align: center">SAT</td>
                                    <td style="text-align: center">JMLH</td>
                                    <td style="text-align: center">NILAI PAB (Rp)</td>
                                    <td style="text-align: center">NILAI PAB ($)</td>
                                    <td style="text-align: center">JMLH</td>
                                    <td style="text-align: center">SAT</td>
                                    <td style="text-align: center">NILAI PAB (Rp)</td>
                                    <td style="text-align: center">NILAI PAB ($)</td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    if (records.size() > 0) {
                                        int row = 0;
                                        Vector objectClass = (Vector) records.get(1);
                                        Vector listPemasukan = new Vector();
                                        Vector listPengeluaran = new Vector();
                                        if (objectClass != null && objectClass.size() > 0) {
                                            for (int i = 0; i < objectClass.size(); i++) {
                                                StockCardReport stockCardReport = (StockCardReport) objectClass.get(i);
                                                if (stockCardReport.getDocType() == I_DocType.MAT_DOC_TYPE_LMRR) {
                                                    listPemasukan.add(stockCardReport);
                                                } else if (stockCardReport.getDocType() == I_DocType.MAT_DOC_TYPE_DF) {
                                                    listPengeluaran.add(stockCardReport);
                                                }
                                            }
                                        }

                                        if (listPemasukan.size() > listPengeluaran.size()) {
                                            row = listPemasukan.size();
                                        } else {
                                            row = listPengeluaran.size();
                                        }
                                        double saldo = 0;
                                        for (int i = 0; i < row; i++) {
                                %>
                                <tr>
                                    <%
                                        if (listPemasukan.size() > i) {
                                            StockCardReport stockCardReport = (StockCardReport) listPemasukan.get(i);
                                            String whereMatReceive = PstMatReceive.fieldNames[PstMatReceive.FLD_REC_CODE] + "='" + stockCardReport.getDocCode() + "'";
                                            Vector listMatRec = PstMatReceive.list(0, 0, whereMatReceive, "");
                                            MatReceive matReceive = new MatReceive();
                                            if (listMatRec.size() > 0) {
                                                matReceive = (MatReceive) listMatRec.get(0);
                                            }

                                            MatReceiveItem matReceiveItem = new MatReceiveItem();
                                            if (listMatRec.size() > 0) {
                                                matReceiveItem = PstMatReceiveItem.getObjectReceiveItem("", matReceive.getOID(), material.getOID());
                                            }
                                            saldo = saldo + stockCardReport.getQty();

                                    %>
                                    <td><%=i + 1%></td>
                                    <td><%=matReceive.getJenisDokumen()%></td>
                                    <td><%=matReceive.getNomorBc()%></td>
                                    <td><%=Formater.formatDate(matReceive.getTglBc(), "yyyy-MM-dd")%></td>
                                    <td><%=Formater.formatDate(matReceive.getReceiveDate(), "yyyy-MM-dd")%></td>
                                    <td><%=category.getCode()%></td>
                                    <td><%=material.getSku()%></td>
                                    <td><%=material.getName()%></td>
                                    <td><%=unit.getCode()%></td>
                                    <td><%=matReceiveItem.getQty()%></td>
                                    <td><%=material.getDefaultCost()%></td>
                                    <td><%=0%></td>
                                    <td><%=saldo%></td>
                                    <td><%=unit.getCode()%></td>
                                    <td><%=material.getDefaultCost()%></td>
                                    <td><%=0%></td>
                                    <%
                                    } else {
                                    %>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <%
                                        }
                                        if (listPengeluaran.size() > i) {
                                            StockCardReport stockCardReport = (StockCardReport) listPengeluaran.get(i);
                                            String whereMatDispatch = PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE] + "='" + stockCardReport.getDocCode() + "'";
                                            Vector listMatDispatch = PstMatDispatch.list(0, 0, whereMatDispatch, "");
                                            MatDispatch matDispatch = new MatDispatch();
                                            if (listMatDispatch.size() > 0) {
                                                matDispatch = (MatDispatch) listMatDispatch.get(0);
                                            }

                                            MatDispatchItem matDispatchItem = new MatDispatchItem();
                                            if (listMatDispatch.size() > 0) {
                                                matDispatchItem = PstMatDispatchItem.getObjectDispatchItem(material.getOID(), matDispatch.getOID());
                                            }
                                            saldo = saldo - stockCardReport.getQty();
                                    %>
                                    <td><%=matDispatch.getJenisDokumen()%></td>
                                    <td><%=matDispatch.getNomorBeaCukai()%></td>
                                    <td><%=Formater.formatDate(matDispatch.getDispatchDate(), "yyyy-MM-dd")%></td>
                                    <td><%=Formater.formatDate(matDispatch.getDispatchDate(), "yyyy-MM-dd")%></td>
                                    <td><%=material.getName()%></td>
                                    <td><%=unit.getCode()%></td>
                                    <td><%=matDispatchItem.getQty()%></td>
                                    <td><%=material.getDefaultCost()%></td>
                                    <td><%=0%></td>
                                    <td><%=saldo%></td>
                                    <td><%=unit.getCode()%></td>
                                    <td><%=material.getDefaultCost()%></td>
                                    <td><%=0%></td>
                                    <%
                                    } else {
                                    %>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <%
                                        }
                                    %>
                                </tr>
                                <%
                                        }
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <% }%>
        </div>

    </body>
</html>

