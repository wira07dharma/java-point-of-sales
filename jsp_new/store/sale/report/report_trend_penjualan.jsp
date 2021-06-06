<%-- 
    Document   : report_trend_penjualan
    Created on : Mar 31, 2018, 10:09:11 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../../main/checkuser.jsp" %>

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

<%//
    Vector<Company> company = PstCompany.list(0, 0, "", "");
    String compName = "";
    if (!company.isEmpty()) {
        compName = company.get(0).getCompanyName().toUpperCase();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../../styles/select2/css/select2.min.css" />
        <link rel="stylesheet" media="screen" href="../../../styles/datepicker/bootstrap-datetimepicker.min.css"/>
        <link rel="stylesheet" media="screen" href="../../../styles/Highcharts-6.0.7/highcharts.css"/>

        <style>

            .form-group label {margin-top: 5px; width: 120px}

            .table-condensed th {font-size: 14px}
            .table-condensed td {font-size: 14px}

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

        <script type="text/javascript" src="../../../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
        <script type="text/javascript" src="../../../styles/Highcharts-6.0.7/highcharts.js"></script>
        <script type="text/javascript" src="../../../styles/Highcharts-6.0.7/exporting.js"></script>

        <script>
            $(document).ready(function () {

                var getDataFunction = function (onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType) {
                    $(this).getData({
                        onDone: function (data) {
                            onDone(data);
                        },
                        onSuccess: function (data) {
                            onSuccess(data);
                        },
                        approot: approot,
                        dataSend: dataSend,
                        servletName: servletName,
                        dataAppendTo: dataAppendTo,
                        notification: notification,
                        ajaxDataType: dataType
                    });
                };

                $('.datepick').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd',
                    minView: 2
                });

                var loadChart = function (chartId, chartType, dataFor) {
                    $("#" + chartId).html("<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>").fadeIn("medium");

                    var command = "<%=Command.NONE%>";
                    var dataSend = $("#formSearch").serialize();
                    dataSend += "&FRM_FIELD_CHART_DATA_FOR=" + chartId
                            + "&FRM_FIELD_CHART_TYPE=" + chartType
                            + "&command=" + command
                            + "&FRM_FIELD_DATA_FOR=" + dataFor;

                    onSuccess = function (data) {

                    };

                    onDone = function (data) {
                        //alert(JSON.stringify(data));
                        $('#btnSearch').removeAttr('disabled').html("<i class='fa fa-search'></i>&nbsp; Cari");
                        var error = data.ERROR;                        
                        if (error == 0) {
                            $('#chartPlace').highcharts(data.CHART_DATA);
                            $('#chartPrint').highcharts(data.CHART_DATA);
                            $('#hidePrint').removeClass('hidden');
                            /*
                            if (chartId === "chartPlace") {
                                //$('#chartPlace').highcharts(data.CHART_DATA);
                                new Highcharts.Chart(data.CHART_DATA);                                
                                $('#hidePrint').removeClass('hidden');
                            } else if (chartId === "chartPrint") {
                                //$('#chartPrint').highcharts(data.CHART_DATA);
                                new Highcharts.Chart(data.CHART_DATA);
                                $('#tanggal').html("Tanggal : " + data.START + " s/d " + data.END);
                                setTimeout(function () {
                                    window.print();
                                    $('#btnPrintChart').removeAttr('disabled').html("<i class='fa fa-print'></i>&nbsp; Cetak");
                                    $('#chartPrint').html("");
                                }, 2000);
                            }
                            */
                        } else {
                            $("#" + chartId).html(data.MESSAGE);
                            $('#hidePrint').addClass('hidden');
                        }
                    };

                    getDataFunction(onDone, onSuccess, "<%=approot%>", command, dataSend, "AjaxSaleGraphic", null, false, "json");
                };

                $('#btnSearch').click(function () {
                    var awal = $('#tglAwal').val();
                    var akhir = $('#tglAkhir').val();
                    if (awal === "" || akhir === "") {
                        alert("Tanggal harus diisi !");
                    } else {
                        $('#btnSearch').attr({'disabled': true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                        loadChart("chartPlace", "column", "reportTrendSales");
                    }
                });

                $('#btnPrintChart').click(function () {
                    $('#btnPrintChart').attr({'disabled': true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    window.print();
                    $('#btnPrintChart').removeAttr('disabled').html("<i class='fa fa-print'></i>&nbsp; Cetak");
                    /*
                    var awal = $('#tglAwal').val();
                    var akhir = $('#tglAkhir').val();
                    if (awal === "" || akhir === "") {
                        alert("Tanggal harus diisi !");
                    } else {                        
                        $('#btnPrintChart').attr({'disabled': true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                        loadChart("chartPrint", "column", "reportTrendSales");
                    }
                    */
                });

            });
        </script>

    </head>
    <body style="background-color: #eeeeee">
        <div class="col-md-12 nonPrint">
            
            <div class="">
                <h4>Laporan Trend Penjualan</h4>
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
                    <div id="chartPlace">Grafik laporan penjualan</div>
                </div>

                <div class="box-footer hidden" id="hidePrint" style="border-color: lightgray">
                    <button type="button" id="btnPrintChart" class="btn btn-sm btn-primary pull-right"><i class="fa fa-print"></i>&nbsp; Cetak</button>
                </div>

            </div>

        </div>

    <print-area>
        <div>
            <img style="width: 100px" src="../../../images/litama.jpeg">
            <span style="font-size: 24px; font-family: TimesNewRomans"><b><%=compName%></b> <small><i>Gallery</i></small></span>
        </div>
        
        <div>
            <h4><b>Laporan Trend Penjualan</b></h4>
        </div>
        
        <div id="tanggal"></div>
        
        <hr style="border-width: medium; border-color: black; margin: 5px 0px">
        
        <div id="chartPrint"></div>
        
        <hr style="border-width: thin; border-color: grey; margin-top: 0px; margin-bottom: 10px">

                <!--//START TEMPLATE TANDA TANGAN//-->                
                <% int signType = 1; %>
                <%@ include file = "../../../templates/template_sign.jsp" %>
                <!--//END TEMPLATE TANDA TANGAN//-->
                      
    </print-area>

</body>
</html>
