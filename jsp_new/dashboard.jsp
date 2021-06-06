<%-- 
    Document   : list_anggota_kelompok
    Created on : Aug 29, 2017, 9:04:36 AM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.common.entity.system.SystemProperty"%>
<%@page import="com.dimata.common.session.system.SessSystemProperty"%>
<%@page import="com.dimata.util.Command"%>
<%@ include file = "/main/javainit.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/plugins/font-awesome-4.7.0/css/font-awesome.min.css">        
    <!-- Datetime Picker -->
    <link href="<%=approot%>/styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css" rel="stylesheet">
    <!-- Select2 -->
    <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/plugins/select2/select2.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="<%=approot%>/styles/AdminLTE-2.3.11/dist/css/skins/_all-skins.min.css">
    <link href="<%=approot%>/styles/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="<%=approot%>/styles/bootstrap-notify/bootstrap-notify.css"/>
    <!-- jQuery 2.2.3 -->
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/jQuery/jquery-2.2.3.min.js"></script>
    <!-- Bootstrap 3.3.6 -->
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/bootstrap/js/bootstrap.min.js"></script>
    <!-- FastClick -->
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/fastclick/fastclick.js"></script>
    <!-- AdminLTE for demo purposes -->
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/dist/js/demo.js"></script>
    <!-- AdminLTE App -->
    <script type="text/javascript" src="<%=approot%>/styles/bootstrap-notify.js"></script>
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/dist/js/app.min.js"></script>    
    <script src="<%=approot%>/styles/dist/js/dimata-app.js" type="text/javascript"></script>
    <!-- Select2 -->
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/select2/select2.full.min.js"></script>

    <!-- Datetime Picker -->
    <script src="<%=approot%>/styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/datatables/jquery.dataTables.js" type="text/javascript"></script>
    <script src="<%=approot%>/styles/AdminLTE-2.3.11/plugins/datatables/dataTables.bootstrap.js" type="text/javascript"></script>
    <!--chart -->
    <script src="<%=approot%>/styles/chart/highcharts.js"></script>
    <style>
      th {text-align: center; font-weight: normal; vertical-align: middle !important}
      th {background-color: #00a65a; color: white; padding-right: 8px !important}
      th:after {display: none !important;}
    </style>
    <script language="javascript">
      $(document).ready(function () {

        var dataSend;
        var command;
        var dataFor;
        var approot = "<%= approot%>";

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

        var loadChart = function (chartName, line) {
          //dataSend = $("#frmsrcreportsale").serialize();
          dataSend = "";
          dataSend = dataSend + "&FRM_FIELD_CHART_DATA_FOR=" + chartName + "&FRM_FIELD_CHART_TYPE=" + line + "&command=<%= Command.NONE%>&FRM_FIELD_DATA_FOR=dataSaleChart";
          $("#" + chartName + "").html("<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>").fadeIn("medium");
          //$("#grafik").hide();
          onSuccess = function (data) {

          };

          onDone = function (data) {
            renderChart(chartName, data);
          };

          getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomeReport", null, false, "json");
        };

        //RENDER THE CHART
        function renderChart(chartId, data) {
          var chartOptions = data.CHART_DATA;
          var chart = new Highcharts.Chart(chartOptions);
        }


        loadChart("chartPlace", "line");
        loadChart("chartPlaceDua", "line");
        loadChart("chartPlaceItem", "bar");

      });
      
      function cmdEditSystemProperty(oid, name, note){
      var url = "";	
       url = "<%=approot%>/system/syspropnew.jsp?command=<%=Command.EDIT%>&oid="+oid+"&name="+name+"&note="+note;
       var newWind = window.open(url);
              newWind.window.focus();   
      }
      function cmdAddSystemProperty(oid, name, note){
      var url = "";	
       url = "<%=approot%>/system/syspropnew.jsp?command=<%=Command.ADD%>&oid="+oid+"&name="+name+"&note="+note;
       var newWind = window.open(url);
              newWind.window.focus();   
      }
    </script>
  </head>
  <body style="background-color: rgb(248, 248, 248)">
    <section class="content-header">
      <h1>
        Dashboard
        <small>Report</small>
      </h1>
      <ol class="breadcrumb">
        <li><a href=""><i class="fa fa-dashboard"></i> Home</a></li>
        <li class="active">Dasboard</li>
      </ol>
    </section>

    <section class="content">
      <div class="row">
        <div class="col-md-6">
          <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Rekap Penjualan & Cost Bulan Sebelumnya</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
              </div>
            </div>
            <div class="box-body">
              <div id="chartPlaceDua">
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Rekap Penjualan & Cost Bulan Ini</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
              </div>
            </div>
            <div class="box-body">
              <div id="chartPlace">
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-6">
          <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Item Terlaris Bulan Ini</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
              </div>
            </div>
            <div class="box-body">
              <div id="chartPlaceItem">
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="box box-info collapsed-box">
            <div class="box-header with-border">
              <h3 class="box-title">System Property</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-plus"></i>
                </button>
                <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
              </div>
            </div>
            <div class="box-body">
              <%
                boolean needSetting = false;
                String strProp = PstSystemProperty.getValueByName(SessSystemProperty.systemPropsProchain[0]);
                for (int ip = 0; ip < SessSystemProperty.systemPropsProchain.length; ip++) {
                }
                for (int ip = 0; ip < SessSystemProperty.systemPropsProchain.length; ip++) {
                  strProp = PstSystemProperty.getValueByName(SessSystemProperty.systemPropsProchain[ip]);
                  Vector listSysProp = PstSystemProperty.list(0, 1, PstSystemProperty.fieldNames[PstSystemProperty.FLD_NAME] + " = '" + SessSystemProperty.systemPropsProchain[ip] + "'", "");
                  if ((listSysProp != null || listSysProp.size() < 1)) {
                    listSysProp = PstSystemProperty.list(0, 1, PstSystemProperty.fieldNames[PstSystemProperty.FLD_NAME] + " = '" + SessSystemProperty.systemPropsProchain[ip] + "'", "");
                  }
                  SystemProperty sysProp = (listSysProp != null && listSysProp.size() > 0 ? (SystemProperty) listSysProp.get(0) : null);
                  if (strProp != null) {
                    strProp = strProp.trim();
                  }
                  if (strProp == null || strProp.length() < 1 || sysProp == null) {
                    needSetting = true;
                    long newOid = sysProp != null ? sysProp.getOID() : 0;
                    %>
                        <p align="center" style="font-weight: bolder" >Please set system property : <%= SessSystemProperty.systemPropsProchain[ip]%>
                          <%if(newOid != 0){ %>  
                          <a class="btn btn-primary btn-xs" href="javascript:cmdEditSystemProperty('<%=newOid %>','<%=SessSystemProperty.systemPropsProchain[ip]%>','<%=SessSystemProperty.systemPropsProchainNote[ip]%>')">Edit</a>
                          <%}else{%>
                          <a class="btn btn-primary btn-xs" href="javascript:cmdAddSystemProperty('<%=newOid %>','<%=SessSystemProperty.systemPropsProchain[ip]%>','<%=SessSystemProperty.systemPropsProchainNote[ip]%>')">Edit</a>
                        <%}%>
                        </p>
                    <%
                  }
                }
              %>
            </div>
          </div>
        </div>
      </div>
    </section>
  </body>
</html>
