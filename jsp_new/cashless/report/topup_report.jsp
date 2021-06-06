<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DAILY_SUMMARY); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
    public static final int ADD_TYPE_SEARCH = 0;
    public static final int ADD_TYPE_LIST = 1;

    public static final String textListHeader[][] = 
    {
        {"Lokasi","Shift","Kasir","Supplier","Kategori","Tanggal","Urut Berdasar","Kode Sales","Semua Lokasi","Cari"},
        {"Location","Shift","Cashier","Supplier","Category","Sale Date","Sort By","Sales Code","All Location","Search"}
    };

    public static final String textButton[][] = 
    {
        {"Rekap Harian","Laporan Rekap Penjualan Per Tanggal","Grafik Rekap Harian"},
        {"Daily Report","Summary Report of Sales Per Day","Sales Per Day Chart"}
    };

    public String getJspTitle(int index, int language, String prefiks, boolean addBody){
        String result = "";
        if(addBody){
            if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
                result = textListHeader[language][index] + " " + prefiks;
            }else{
                result = prefiks + " " + textListHeader[language][index];		
            }
        }
        else{
            result = textListHeader[language][index];
        } 
        return result;
    }

%>
<%
    int iCommand = FRMQueryString.requestCommand(request);

    ControlLine ctrLine = new ControlLine();

    SrcReportSale srcReportSale = new SrcReportSale();
    FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale();
    try {
        srcReportSale = (SrcReportSale) session.getValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
    } catch (Exception e) {
        srcReportSale = new SrcReportSale();
    }

    if (srcReportSale == null) {
        srcReportSale = new SrcReportSale();
    }

    try {
        session.removeValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP);
    } catch (Exception e) {
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dimata ProChain POS</title>
        <%if(menuUsed == MENU_ICON){%>
        <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
        <%}%>      
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <style>
            @media print{
                .printable{
                        display: block;
                        visibility: visible;
                }
                .nonprint{
                        display: none;
                        visibility: hidden;
                }
             }
        </style>
    </head>
    <body class="" style="background-color:beige"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
        <div class="nonprint">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
            <%if(menuUsed == MENU_PER_TRANS){%>
            <tr>
                <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
                    <%@ include file = "../../main/header.jsp" %>
                </td>
            </tr>
            <tr>
                <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <%}else{%>
            <tr bgcolor="#FFFFFF">
                <td height="10" ID="MAINMENU">
                    <%@include file="../../styletemplate/template_header.jsp" %>
                </td>
            </tr>
            <%}%>
            <tr> 
                <link href="../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
                <link href="../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
                
             
                <td valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">  
                        <tr> 
                            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
                                <div   class="row" style="margin-left:0px; margin-right:0px; margin-top:10px; ">
                                    <div class="col-md-8">                                       
                                        <div class="box box-primary">
                                            <div class="box-header">
                                                <h3 class="box-title">Filter</h3>
                                                <div class="box-tools pull-right">
                                                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                </div>
                                            </div>
                                            <div class="box-body">
                                                <form name="frmsrcreportsale" id="frmsrcreportsale" class="form-horizontal" role="form">
                                                    <input type="hidden" name="language" id="language" value="<%= SESS_LANGUAGE%>">
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" for="email">
                                                            <%=getJspTitle(0,SESS_LANGUAGE,"",true)%>
                                                        </label>
                                                        <div class="col-sm-10">
                                                            <% 
                                                                Vector val_locationid = new Vector(1,1);
                                                                Vector key_locationid = new Vector(1,1); 

                                                                boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                                                String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                                                Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                                if(checkDataAllLocReportView){
                                                                    val_locationid.add("0");
                                                                    key_locationid.add(textListHeader[SESS_LANGUAGE][8]);
                                                                }
                                                                for(int d=0;d<vt_loc.size();d++){
                                                                    Location loc = (Location)vt_loc.get(d);
                                                                    val_locationid.add(""+loc.getOID()+"");
                                                                    key_locationid.add(loc.getName());
                                                                }
                                                            %> 
                                                            <%=ControlCombo.draw(frmSrcReportSale.fieldNames[frmSrcReportSale.FRM_FIELD_LOCATION_ID], null, "", val_locationid, key_locationid, "multiple='multiple'", "form-control selects2")%>                                                           
                                                        </div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" for="pwd">
                                                            <%=getJspTitle(5,SESS_LANGUAGE,"",true)%>
                                                        </label>
                                                        <div class="col-sm-5">
                                                            <div class="input-group">
                                                                <div class="input-group-addon">
                                                                    <i class="fa fa-calendar"></i>
                                                                </div>
                                                                <input  id="dateFrom" name="<%=frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_FROM]%>" class="form-control dates" type="text">
                                                            </div>
                                                        </div>
                                                        <div class="col-sm-5">
                                                            <div class="input-group">
                                                                <div class="input-group-addon">
                                                                    <i class="fa fa-calendar"></i>
                                                                </div>
                                                                <input id="dateTo" name="<%=frmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_DATE_TO]%>" class="form-control dates" type="text">
                                                            </div>
                                                        </div>                                                    
                                                    </div>                                                    
                                                    <div class="form-group">
                                                        <label class="control-label col-sm-2" for="pwd">
                                                            <%=getJspTitle(4,SESS_LANGUAGE,"",true)%>
                                                        </label>
                                                        <div class="col-sm-10">
                                                            <%
                                                                Vector val_categoryid = new Vector(1,1);
                                                                Vector key_categoryid = new Vector(1,1);

                                                                Vector listCategory = PstCategory.list(0, 0, "", "");
                                                                for (int i=0; i<listCategory.size();i++ ){
                                                                    Category entCategory = (Category) listCategory.get(i);
                                                                    val_categoryid.add(""+entCategory.getOID()+"");
                                                                    key_categoryid.add(""+entCategory.getName()+"");
                                                                }
                                                            %>
                                                            <%=ControlCombo.draw(FrmSrcReportSale.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID], null, "", val_categoryid, key_categoryid, " multiple='multiple' ", "form-control selects2")%>
                                                            
                                                        </div>
                                                    </div>                                                    
                                                    <div class="form-group">
                                                        <div class="col-sm-offset-2 col-sm-10">
                                                            <button id="btnSearch" type="button" class="btn btn-primary">
                                                                <i class="fa fa-filter"></i>
                                                                <%=getJspTitle(9,SESS_LANGUAGE,"",true)%>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </form>   
                                            </div>
                                            <div class="box-footer">
                                                
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div  class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    <div class="col-md-12">
                                        <div id="grafik" >
                                            <div  class="box box-primary">
                                                <div class="box-header">
                                                    <h3 class="box-title"><%=textButton[SESS_LANGUAGE][2]%></h3>
                                                    <div class="box-tools pull-right">
                                                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                                    </div>
                                                </div>
                                                <div class="box-body" >
                                                    <div id="chartPlace" class="col-md-12"></div>
                                                    
                                                </div>
                                                <div class="box-footer">
                                                       &nbsp; 
                                                </div>
                                            </div>
                                        </div>
                                        
                                    </div>
                                </div>
                                <div id="dynamicReportTable" class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    
                                </div>
                                                    
                                <div id="dynamicReportTablePerLocation" class="row" style="margin-left:0px; margin-right:0px; margin-top:10px;">
                                    
                                </div>
                            </td>
                        </tr>
                        
                    </table>
                </td>
            </tr>
            <tr> 
                <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
                    <%if(menuUsed == MENU_ICON){%>
                    <%@include file="../../styletemplate/footer.jsp" %>

                    <%}else{%>
                    <%@ include file = "../../main/footer.jsp" %>
                    <%}%>
                    <!-- #EndEditable --> </td>
            </tr>
        </table>
        </div>
    </body>
    <script src="../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script src="../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
    <script src="../../styles/dimata-app.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
    <script src="../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
    <script type="text/javascript" src="../../styles/chart/highcharts.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            var dataSend;
            var command;
            var dataFor;
            var approot = "<%= approot%>";
            
            var modalSetting = function(elementId, backdrop, keyboard, show){
                $(elementId).modal({
                    backdrop	: backdrop,
                    keyboard	: keyboard,
                    show	: show
                });
            };
                
            var getDataFunction = function(onDone, onSuccess, approot, command, dataSend, servletName, dataAppendTo, notification, dataType){		
                $(this).getData({
                   onDone	: function(data){
                       onDone(data);
                   },
                   onSuccess	: function(data){
                        onSuccess(data);
                   },
                   approot	: approot,
                   dataSend	: dataSend,
                   servletName	: servletName,
                   dataAppendTo	: dataAppendTo,
                   notification : notification,
                   ajaxDataType	: dataType
                });
            };
                
            function validateOptions (elementId, checkType, classError, minLength, matchValue){
                $(elementId).validate({
                    minLength   : minLength,
                    matchValue  : matchValue,
                    classError  : classError,
                    checkType   : checkType
                });
            }
            
            //RENDER THE CHART
            function renderChart(chartId, data){
                var chartOptions = data.CHART_DATA;
                var chart = new Highcharts.Chart(chartOptions);
            }
            
            var loadCategory = function(elementId){
                var oidLocation = $(""+elementId+"").val();
                dataSend = "command=<%= Command.NONE%>&FRM_FIELD_DATA_FOR=getComboCategory&FRM_FLD_LOCATION_ID="+oidLocation+"";
                //alert(dataSend);
                onSuccess = function(data){

                };
                onDone = function(data){
                    //$('.js-example-basic-multiple').select2();
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#dynamicCombo", true, "json");
            };
            
            var locationChange= function(elementId){
                $(elementId).change(function(){
                    loadCategory(elementId);
                });
                
            };
            
            var loadReportTabel= function(){
                dataSend = $("#frmsrcreportsale").serialize();
                dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTable";
                
                onSuccess = function(data){
                };
                onDone = function(data){
                    exportExcel("#btnExportExcel");
                    printButtonClick("#btnPrint");
                    
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#dynamicReportTable", true, "json");
            };
            
            var printFunction = function(elementId){
                
            };
            
            var loadReportTabe2= function(){
                //alert("test");
                dataSend = $("#frmsrcreportsale").serialize();
                dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTableLocation";
                
                onSuccess = function(data){
                };
                onDone = function(data){
                  
                    exportExcelPerLocation("#exportExcelPerLocation");
                    printButtonClickPerLocation("#btnprintperlocation");
                    
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#dynamicReportTablePerLocation", true, "json");
            };
            
            
            var loadReportTabelOld= function(){
                dataSend = $("#frmsrcreportsale").serialize();
                dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTableOld";
                onSuccess = function(data){

                };
                onDone = function(data){
                    window.open("reportsalerekaptanggal_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#printPlace", true, "json");
            };
            
            
            var loadReportTabelOldPerLocation= function(){
                
                dataSend = $("#frmsrcreportsale").serialize();
                dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTableLocation";
                window.open("reportsalerekaptanggal_form_print_location.jsp?"+dataSend+"","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                
                //getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#printPlace", true, "json");
            };
            
            var eventLoadReport = function(elementId){
                $(elementId).click(function(){
                    loadReportTabel();
                    loadReportTabe2();
                    loadChart();
                });
            };
            
            var loadChart = function(){
                
                dataSend = $("#frmsrcreportsale").serialize();
                dataSend = dataSend + "&FRM_FIELD_CHART_DATA_FOR=chartPlace&FRM_FIELD_CHART_TYPE=line&command=<%= Command.NONE%>&FRM_FIELD_DATA_FOR=dataSaleChart";
                $("#chartPlace").html("<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>").fadeIn("medium");
                //$("#grafik").hide();
                onSuccess = function(data){

                };

                onDone = function(data){      
                    //alert(JSON.stringify(data));
                    renderChart('chartPlace', data);
                    //$("#grafik").fadeIn();
                };
                
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSaleGraphic", null, false, "json");
            };
            
            
            
            var printButtonClick = function(elementId){
                $(elementId).click(function(){
                    //alert("test");
                    loadReportTabelOld();
                });
            };
            
            var printButtonClickPerLocation = function(elementId){
                $(elementId).click(function(){
                    //alert("test above");
                    loadReportTabelOldPerLocation();
                });
            };
            
            var exportExcel = function(elementId){
                $(elementId).click(function(){
                    dataSend = $("#frmsrcreportsale").serialize();
                    dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTableOld";
                    onSuccess = function(data){

                    };
                    onDone = function(data){
                        window.open("reportsalerekaptanggal_list_excel.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
                    };
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxSalesReport", "#printPlace", true, "json");
                    
                });
                
            };
            
            var exportExcelPerLocation = function(elementId){
                $(elementId).click(function(){
                    dataSend = $("#frmsrcreportsale").serialize();
                    dataSend = dataSend +"&command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=getListReportTableLocation";                  
                    window.open("reportsalerekaptanggal_list_location_excel.jsp?"+dataSend+"","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");  
                });
                
            };

            //----------------------------
            loadCategory("#FRM_FIELD_LOCATION_ID");
            locationChange("#FRM_FIELD_LOCATION_ID");
            eventLoadReport("#btnSearch");
            $('.selects2').select2();
            $('.selects3').select2();
        });
    </script>
    <script type="text/javascript">
        
        $(function () {
            $('.dates').datetimepicker({
                autoclose: true,
                todayBtn: true,
                format: 'mm/dd/yyyy',
                minView:2
            });
        });
    </script>
    <div style="visibility: hidden;" class="printable" id="printPlace"></div>
</html>
