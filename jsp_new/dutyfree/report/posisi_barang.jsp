<%-- 
    Document   : posisi_barang
    Created on : Oct 7, 2019, 2:30:33 PM
    Author     : IanRizky
--%>

<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    String dateFrom = FRMQueryString.requestString(request, "TGL_AWAL");
    String dateTo = FRMQueryString.requestString(request, "TGL_AKHIR");
	String jenisDokumen = FRMQueryString.requestString(request, "JENIS_DOKUMEN");
	String kodeBarang = FRMQueryString.requestString(request, "KODE_BARANG");

    if (dateFrom == "") {
        dateFrom = Formater.formatDate(new Date(), "yyyy-MM-dd");
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
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <link rel="stylesheet" media="screen" href="../../styles/Highcharts-6.0.7/highcharts.css"/>

        <style>

			
            .form-group label {margin-top: 5px; width: 170px}

            .tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
                font-size: 12px;
                padding: 5px;
                border-color: #DDD;
            }

            .highcharts-credits {display: none}
            .highcharts-button {display: none}
            
            print-area { visibility: hidden; display: none; }
            print-area.preview { visibility: visible; display: block; }
            @media print
            {
                body .nonPrint * { visibility: hidden; display: none; } 
                body print-area * { visibility: visible; }
                body print-area   { visibility: visible; display: unset !important; position: static; top: 0; left: 0; }
            }

        </style>

        <script type="text/javascript" src="../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script type="text/javascript" src="../../styles/Highcharts-6.0.7/highcharts.js"></script>
        <script type="text/javascript" src="../../styles/Highcharts-6.0.7/exporting.js"></script>

    </head>
    <body style="background-color: #eeeeee">
        <div class="col-md-12 nonPrint">
            
            <div class="">
                <h4>Laporan posisi barang per dokumen pabean</h4>
            </div>
            
            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <div class="box-body">
                    <form id="formSearch" class="form-horizontal">
                        <p></p>
                        <div class="form-group">
                            <label class="col-sm-2">Tanggal Dokumen</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal awal" id="tglAwal" class="form-control input-sm datepick" name="TGL_MASUK_AWAL" value="<%=dateFrom%>">
                                </div>
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal akhir" id="tglAkhir" class="form-control input-sm datepick" name="TGL_MASUK_AKHIR" value="<%=dateTo%>">
                                </div>
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2">Jenis Dokumen</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <input type="text" size="100" placeholder="Jenis Dokumen" class="form-control input-sm" name="JENIS_DOKUMEN" value="<%=jenisDokumen%>">
                                </div>
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2">Kode Barang</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <input type="text" size="100" placeholder="Kode Barang" class="form-control input-sm" name="KODE_BARANG" value="<%=kodeBarang%>">
                                </div>
                            </div>
                        </div>

                    </form>                    
                </div>

                <div class="box-footer" style="border-color: lightgray">
                    <button type="button" id="btnSearch" class="btn btn-sm btn-primary"><i class="fa fa-search"></i>&nbsp; Cari</button>
                </div>

            </div>

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Hasil Pencarian</h3>
                </div>

				<div class="box-body">
					<table class="table table-bordered tabel-data">
						<tr class="bg-primary">
							<td style="width: 1%; text-align: center; vertical-align: middle" rowspan="2" colspan="1">NO</td>
							<td colspan="10" rowspan="1" style="text-align: center">DOKUMEN PEMASUKAN</td>
							<td colspan="8" rowspan="1" style="text-align: center">DOKUMEN PENGELUARAN</td>
							<td colspan="3" rowspan="1" style="text-align: center">SALDO BARANG</td>
						</tr>
						<tr class="bg-primary">
							<td style="text-align: center">JENIS</td>
							<td style="text-align: center">NO</td>
							<td style="text-align: center">TGL</td>
							<td style="text-align: center">TGL MASUK</td>
							<td style="text-align: center">KODE BARANG</td>
							<td style="text-align: center">SERI BARANG</td>
							<td style="text-align: center">NAMA BARANG</td>
							<td style="text-align: center">SAT</td>
							<td style="text-align: center">JMLH</td>
							<td style="text-align: center">NILAI PABEAN</td>
							<td style="text-align: center">JENIS</td>
							<td style="text-align: center">NO</td>
							<td style="text-align: center">TGL</td>
							<td style="text-align: center">TGL KELUAR</td>
							<td style="text-align: center">NAMA BARANG</td>
							<td style="text-align: center">SAT</td>
							<td style="text-align: center">JMLH</td>
							<td style="text-align: center">NILAI PAB</td>
							<td style="text-align: center">JMLH</td>
							<td style="text-align: center">SAT</td>
							<td style="text-align: center">NILAI PAB</td>
						</tr>
					</table>
				</div>
            </div>

        </div>

</body>
</html>