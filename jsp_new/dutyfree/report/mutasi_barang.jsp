<%-- 
    Document   : mutasi_barang
    Created on : Oct 7, 2019, 2:51:58 PM
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
    int reportType = FRMQueryString.requestInt(request, "REPORT_TYPE");

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

			
            .form-group label {margin-top: 5px; width: 120px}

            .tabel-data, .tabel-data tbody tr th, .tabel-data tbody tr td {
                font-size: 12px;
                padding: 5px;
                border-color: #DDD;
            }
			
			.tabel-data  tbody tr th{
				text-align: center;
				vertical-align: middle;
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
                <h4>Laporan pertanggungjawaban mutasi barang.</h4>
            </div>
            
            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Form Pencarian</h3>
                </div>

                <div class="box-body">
                    <form id="formSearch" class="form-horizontal">
                        <p></p>
                        <div class="form-group">
                            <label class="col-sm-1">Tanggal</label>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" required="" placeholder="Tanggal awal" id="tglAwal" class="form-control input-sm datepick" name="TGL_AWAL" value="<%=dateFrom%>">
                                </div>
                            </div>
                            <div class="col-sm-2">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" required="" placeholder="Tanggal akhir" id="tglAkhir" class="form-control input-sm datepick" name="TGL_AKHIR" value="<%=dateTo%>">
                                </div>
                            </div>
                        
                            <label class="col-sm-1">Berdasarkan</label>
                            <div class="col-sm-2">
                                <select id="reportType" class="form-control input-sm" name="REPORT_TYPE">
                                    <option <%=reportType == 1 ? "selected" : "" %> value="1">Customer</option>
                                    <option <%=reportType == 2 ? "selected" : ""%> value="2">Supplier</option>
                                    <option <%=reportType == 3 ? "selected" : ""%> value="3">Item Type</option>
                                    <option <%=reportType == 4 ? "selected" : ""%> value="4">Sales Counter</option>
                                </select>
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
							<th>NO</th>
							<th>KODE BARANG</th>
							<th>NAMA BARANG</th>
							<th>SAT</th>
							<th>SALDO AWAL</th>
							<th>PEMASUKAN</th>
							<th>PENGELUARAN / PENJUALAN</th>
							<th>PENYESUAIAN <br> (ADJUSTMENT)</th>
							<th>SALDO AKHIR</th>
							<th>STOCK OPNAME</th>
							<th>SELISIH</th>
							<th>KET</th>
						</tr>
					</table>
				</div>
            </div>

        </div>

</body>
</html>