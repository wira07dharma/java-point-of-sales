<%-- 
    Document   : custome_report
    Created on : Oct 12, 2017, 10:22:42 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.custom.CustomeReportMaster"%>
<%@page import="com.dimata.common.session.report.SessCustomeReport"%>
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
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
    public static final String textListHeader[][] = 
    {
        {"Custome Report","Judul","Tgl Mulai","Tgl Berakhir","Keterangan","Gambar","Lokasi"},
        {"Customer Report","Title","Valid Start", "Valid End","Description","Image","Location"}
    };
    
    public static final String textListButton[][]=
    {
        {"Tambah Baru","Tambah Berita dan Informasi","Ubah Berita dan Informasi","Unggah Gambar"},
        {"Add New","Add News and Info","Edit News and Info","Upload Image"}
    };
%>
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <link href="../../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
    <link href="../../../styles/plugin/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />
    <link href="../../../styles/plugin/datatables/buttons.bootstrap.min.css" rel="stylesheet" type="text/css" />
    <style>
        .table-nonfluid {
            font-size: 12px;
            overflow-x: scroll;
            display: block;
        }
        table { table-layout: fixed; }
        table th, table td { overflow: hidden; }
    </style>
</head> 
<body>
    <section class="content">
        <div class="row">
            <div class="box box-primary">
                <div class="box-header">
                    <h3 class="box-title"><%= textListHeader[SESS_LANGUAGE][0]%></h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    </div>
                </div>
                <div class="col-md-12">
                        <div class="form-inline">
                            <%
                                      Vector val_locationid = new Vector(1,1);
                                        Vector key_locationid = new Vector(1,1);
                                        boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                        String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                        Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                        if(checkDataAllLocReportView){
                                             val_locationid.add("0");
                                            key_locationid.add("Semua Lokasi");
                                        }
                                        for(int d=0;d<vt_loc.size();d++){
                                                Location loc = (Location)vt_loc.get(d);
                                                val_locationid.add(""+loc.getOID()+"");
                                                key_locationid.add(loc.getName());
                                        }
                                %>
                             <%=ControlCombo.drawBootsratap("FRM_FIELD_LOCATION_ID", null, null, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation()\"", "form-control input-sm")%>

                            <%
                                      Vector val_customeReportMaster = new Vector(1,1);
                                      Vector key_customeReportMaster = new Vector(1,1);
                                      Vector vt_custome_master = SessCustomeReport.listCustomeReport(0,0,"","");
                                      /*for(int d=0;d<vt_custome_master.size();d++){
                                              CustomeReportMaster customeReportMaster = (CustomeReportMaster)vt_custome_master.get(d);
                                              val_customeReportMaster.add(""+customeReportMaster.getOID()+"");
                                              key_customeReportMaster.add(customeReportMaster.getCustomeReportName());
                                      }*/
                                      //val_customeReportMaster.add("-1");
                                      //key_customeReportMaster.add("Summary Sales Report (backup)");
                                      val_customeReportMaster.add("1");
                                      key_customeReportMaster.add("Summary Sales Report (Full)");
                                      val_customeReportMaster.add("2");
                                      key_customeReportMaster.add("Sales F&B Report");
                                      val_customeReportMaster.add("3");
                                      key_customeReportMaster.add("Sales Report by Cash");
                                      val_customeReportMaster.add("4");
                                      key_customeReportMaster.add("Report by Credit Card");
                                      val_customeReportMaster.add("5");
                                      key_customeReportMaster.add("Sales Report by C/L");
                                      val_customeReportMaster.add("6");
                                      key_customeReportMaster.add("Sales Report by Delivery");
                                      val_customeReportMaster.add("7");
                                      key_customeReportMaster.add("Sales Report by Take A Way");
                                      val_customeReportMaster.add("8");
                                      key_customeReportMaster.add("Report Accumulative By Monthly");
                                      val_customeReportMaster.add("9");
                                      key_customeReportMaster.add("Report Accumulative By Yearly");
                                      val_customeReportMaster.add("10");
                                      key_customeReportMaster.add("Report By Tax");
                                      val_customeReportMaster.add("11");
                                      key_customeReportMaster.add("Report Report By Service");
                                      val_customeReportMaster.add("12");
                                      key_customeReportMaster.add("Report Report By Staff");
                                      val_customeReportMaster.add("13");
                                      key_customeReportMaster.add("Report Report By Item");
                                      val_customeReportMaster.add("14");
                                      key_customeReportMaster.add("Report Report By Void");
                                      val_customeReportMaster.add("15");
                                      key_customeReportMaster.add("Report Report By Error");
                                      val_customeReportMaster.add("17");
                                      key_customeReportMaster.add("Report Report By Compliment");
                                      val_customeReportMaster.add("18");
                                      key_customeReportMaster.add("Report Report By Discount");
                              %>
                           <%=ControlCombo.drawBootsratap("combo", null, null, val_customeReportMaster, key_customeReportMaster, "onChange=\"javascript:cmdChangeLocation()\"", "form-control input-sm")%>

                        </div>
                         <br>  
                        <%
                          Date startDate = new Date();
                          String dateStart = Formater.formatDate(startDate,"yyyy-MM-dd"); 
                          String dateEnd = Formater.formatDate(startDate,"yyyy-MM-dd"); 
                        %> 
                        <div class="form-inline">
                            <label>Date range</label>
                            &nbsp;
                            <input type="text" id="dateStart" class="form-control input-sm dates" value="<%=dateStart%>">
                            &nbsp;
                            <label>To</label>
                            &nbsp;
                            <input type="text" id="dateEnd" class="form-control input-sm dates" value="<%=dateStart%>">
                            &nbsp;
                            <button type="button" id="btn_filter" class="btn btn-sm btn-primary"><i class="fa fa-filter"></i> &nbsp; Filter</button>
                        </div><br>
                        <div class="form-inline">
                            <label>Filter Data</label>
                            &nbsp;
                            <input type="text" id="filter_txt" class="form-control input-sm" placeholder="paper bill / system bill">
                            &nbsp;
                            <button type="button" id="btn_filter_plus" class="btn btn-sm btn-default">&nbsp;<i class="fa fa-plus"></i>&nbsp;</button>
                            <button type="button" id="btn_filter_minus" class="btn btn-sm btn-default">&nbsp;<i class="fa fa-minus"></i>&nbsp;</button>
                            <button type="button" id="btn_filter_today" class="btn btn-sm btn-success"><i class="fa fa-filter"></i>&nbsp;&nbsp;Today</button>
                            <button type="button" id="btn_filter_yesterday" class="btn btn-sm btn-danger"><i class="fa fa-filter"></i>&nbsp;&nbsp;Yesterday</button>
                            <button type="button" id="btn_filter_this_week" class="btn btn-sm btn-info"><i class="fa fa-filter"></i>&nbsp;&nbsp;This Week</button>
                            <button type="button" id="btn_filter_this_month" class="btn btn-sm btn-warning"><i class="fa fa-filter"></i>&nbsp;&nbsp;This Month</button>
                            <button type="button" id="btn_filter_last_month" class="btn btn-sm btn-facebook "><i class="fa fa-filter"></i>&nbsp;&nbsp;Last Month</button>
                        </div><br>
                </div>
                <div class="row"> <br></div>
                <div class="box-body">
                    <div id="newsInfoElement"><br><br>
                        <table  class="table table-nonfluid table-responsive" class="display" width="100%" cellspacing="0">
                            <thead>
                                <tr id="tableHeaderColumn">
                                </tr>
                            </thead>    
                        </table>
                    </div>
                </div>           
            </div>
        </div>
    </section>
</body>
    <script src="../../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
    <script src="../../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
    <script src="../../../styles/dimata-app.js" type="text/javascript"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
    <script type="text/javascript" src="../../../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
    <script src="../../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/dataTables.bootstrap.min.js" type="text/javascript"></script>
    
    <script src="../../../styles/plugin/datatables/dataTables.buttons.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/buttons.bootstrap.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/jszip.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/pdfmake.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/dataTables.bootstrap.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/vfs_fonts.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/buttons.html5.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/buttons.print.min.js" type="text/javascript"></script>
    <script src="../../../styles/plugin/datatables/buttons.colVis.min.js" type="text/javascript"></script>
    
    <script type="text/javascript">
    $(document).ready(function(){   
        var dataSend;
        var command;
        var dataFor;
        var approot = "<%= approot%>";
        var command = $("#command").val();
        var oid = null;
        var onDone = null;
        var onSuccess = null;
        var callBackDataTables = null;
        var iCheckBox = null;
        var addeditgeneral = null;
        var areaTypeForm = null;
        var deletegeneral = null;
        var language = "<%= SESS_LANGUAGE%>";
        
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
        
        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables, typebuttonfilter, textinput) {            
            var datafilter = "";
            var privUpdate = "";      
            var start = $('#dateStart').val();
            var end = $('#dateEnd').val();
            var combo = $('#combo').val(); 
            var locationId=$('#FRM_FIELD_LOCATION_ID').val();
            
            var e = document.getElementById("FRM_FIELD_LOCATION_ID");
            var locationText = e.options[e.selectedIndex].text;
            var f = document.getElementById("combo");
            var reportText = f.options[f.selectedIndex].text;
            alert(""+reportText);
            $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
            $("#" + elementId).dataTable({"bDestroy": true,
                "iDisplayLength": 10,
                "bProcessing": true,
                "ordering":true,
                "bFilter": true,
                "dom": 'Bfrtip',
                "lengthChange": true,
                "autoWidth": false,
                "buttons": [
                    'copy',
                    {
                        "extend": 'excelHtml5',
                        "title": ''+reportText+' '+locationText,
                        "messageTop": ''+start+' s/d '+end
                    },
                    {
                        "extend": 'csvHtml5',
                        "title": ''+reportText+' '+locationText,
                        "messageTop": ''+start+' s/d '+end
                    },
                    {
                        "extend": 'pdfHtml5',
                        "title": ''+reportText+' '+locationText,
                        "orientation": 'landscape',
                        "pageSize": 'LEGAL'
                    },
                    'print',
                    'pageLength',
                    'colvis'
                ],
                "lengthMenu": [
                    [ 10, 25, 50, 100, -1 ],
                    [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
                ],
                "searching": false,
                "oLanguage": {
                    "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                },
                "bServerSide": true,
                "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>"
                + "&FRM_FIELD_DATE_START=" + start + "&FRM_FIELD_DATE_END=" + end + "&FRM_FIELD_COMBO=" + combo+"&FRM_FIELD_LOCATION_ID="+locationId+"&button_filter_range="+typebuttonfilter+"&textinput="+textinput,
                
                "initComplete": function (settings, json) {
                    if (callBackDataTables != null) {
                        callBackDataTables();
                    }
                },
                "fnDrawCallback": function (oSettings) {
                    if (callBackDataTables != null) {
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
        
        function validateOptions(elementId, checkType, classError, minLength, matchValue) {

            /* OPTIONS
             * minLength    : INT VALUE,
             * matchValue   : STRING OR INT VALUE,
             * classError   : STRING VALUE,
             * checkType    : STRING VALUE ('text' for default, 'email' for email check
             */

            $(elementId).validate({
                minLength: minLength,
                matchValue: matchValue,
                classError: classError,
                checkType: checkType
            });
        }
        
        var validateForm = function ()       {
            validateOptions("#<%=FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_TITLE]%>", 'text', 'has-error', 1, null);
            validateOptions("#<%=FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_START]%>", 'text', 'has-error', 1, null);
            validateOptions("#<%=FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_VALID_END]%>", 'text', 'has-error', 1, null);
            validateOptions("#<%=FrmMarketingNewsInfo.fieldNames[FrmMarketingNewsInfo.FRM_FIELD_DESCRIPTION]%>", 'text', 'has-error', 1, null);
        };
        
        addeditgeneral = function (elementId) {
            $(elementId).click(function () {
                $("#addeditgeneral").modal("show");
                command = $("#command").val();
                oid = $(this).data('oid');
                dataFor = $(this).data('for');
                $("#generaldatafor").val(dataFor);
                $("#oid").val(oid);
                //SET TITLE MODAL
                if (oid != 0) {
                    if (dataFor == 'showform') {
                        $(".addeditgeneral-title").html("<%= textListButton[SESS_LANGUAGE][2]%>");
                    }

                } else {
                    if (dataFor == 'showform') {
                        $(".addeditgeneral-title").html("<%= textListButton[SESS_LANGUAGE][1]%>");
                    }
                }
                dataSend = {
                    "FRM_FIELD_DATA_FOR": dataFor,
                    "FRM_FIELD_OID": oid,
                    "FRM_FIELD_LANGUAGE" : language,
                    "command": command
                };
                onDone = function (data) {
                    $('.dates').datetimepicker({
                        autoclose: true,
                        todayBtn: true,
                        format: 'dd/mm/yyyy',
                        minView:2
                    });
                };
                onSuccess = function (data) {

                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", ".addeditgeneral-body", false, "json");
            });
        };
        
        deletegeneral = function (tableId, elementId) {

            $(tableId).on("click", elementId, function () {
                dataFor = $(this).data("for");
                command = $(this).data("command");
                oid = $(this).data("oid");
                var confirmText = "Are you sure want to delete these data?";
                if (confirm(confirmText)) {
                    var currentHtml = $(this).html();
                    $(this).html("Deleting...").attr({"disabled": true});
                    dataSend = {
                        "FRM_FIELD_DATA_FOR": dataFor,
                        "FRM_FIELD_OID": oid,
                        "command": command
                    };
                    onSuccess = function (data) {

                    };
                    if (dataFor == "delete") {
                        onDone = function (data) {
                            runDataTables();
                        };
                    }
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", null, true, "json");
                    $(this).removeAttr("disabled").html(currentHtml);
                }

            });
        };
        
        function runDataTables(typebuttonfilter, inputtxtfilter) {
            dataTablesOptions("#newsInfoElement", "tableNewsInfoElement", "AjaxCustomReportTable", "list", callBackDataTables,typebuttonfilter,inputtxtfilter);
        }
        
        var validateNew = function(formId){
          var returnVal = false;
          $(""+formId+" .required").each(function(){
              $(this).removeClass(".has-error");
              var value = $(this).val();
              if (value.length<=0){
                  $(this).addClass(".has-error");
                  returnVal = true;
              }
          });  
          return returnVal;
        };
        
        callBackDataTables = function () {
            addeditgeneral(".btneditgeneral");
            showUploadForm (".button-upload");
            //iCheckBox();
        };
        
        var searchImage = function(elementId,fileId){
            $(elementId).click(function(){
                $(fileId).trigger("click");
            });
        };
        
        var showUploadForm = function(elementId){
            $(elementId).click(function(){
                $("#addeditgeneral").modal("show");
                command = $("#command").val();
                oid = $(this).data('oid');
                dataFor = $(this).data('for');
                $("#generaldatafor").val(dataFor);
                $("#oid").val(oid);
                
                $(".addeditgeneral-title").html("<%= textListButton[SESS_LANGUAGE][3]%>");
                
                dataSend = {
                    "FRM_FIELD_DATA_FOR": dataFor,
                    "FRM_FIELD_OID": oid,
                    "FRM_FIELD_LANGUAGE" : language,
                    "command": command
                };
                
                onDone = function(){
                    searchImage(".clickSearchImage","#filename");
                    filechange("#filename",".fakeinput");
                };
                
                onSuccess = function(){
                    
                };
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", ".addeditgeneral-body", false, "json");
            });
        };
        
        function encodeImageFileAsURL() {
            var filesSelected = document.getElementById("filename").files;
            if (filesSelected.length > 0) {
              var fileToLoad = filesSelected[0];
              var fileReader = new FileReader();
              fileReader.onload = function(fileLoadedEvent) {
                var srcData = fileLoadedEvent.target.result; // <--- data: base64
                var newImage = document.createElement('img');
                newImage.src = srcData;
                $("#basename").val(srcData);
                newImage.style.width = "100%";
                document.getElementById("imgPrev").innerHTML = newImage.outerHTML;
              };
              fileReader.readAsDataURL(fileToLoad);

            }
        }
        
        var filechange = function(elementId,append){
            $(elementId).change(function(){
                var fileName = $(this).val();
                $(append).val(fileName);
                encodeImageFileAsURL();
            });
        };

        //-------------------------------
        //runDataTables();
        addeditgeneral(".btnaddgeneral");
        deletegeneral("#tableNewsInfoElement",".button-delete");
        $("#generalform").submit(function () {
            var currentBtnHtml = $("#btngeneralform").html();
            $("#btngeneralform").html("Saving...").attr({"disabled": "true"});
            var generaldatafor = $("#generaldatafor").val();
            //alert(generaldatafor);
            var result = false;
            if (generaldatafor == "showform") {
                validateForm();
                result = validateNew("#generalform");
                onDone = function (data) {
                    runDataTables(0,"");
                };
            }else if (generaldatafor=="upload"){
                onDone = function (data) {
                    runDataTables(0,"");
                };
            }
            
             
            //alert(result);
            if (generaldatafor == "showform") {
               if (result == false) {
                    onSuccess = function (data) {
                        $("#btngeneralform").removeAttr("disabled").html(currentBtnHtml);
                        $("#addeditgeneral").modal("hide");
                    };

                    dataSend = $(this).serialize();
                    //alert(dataSend);
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", null, true, "json");
                } else {
                    $("#btngeneralform").removeAttr("disabled").html(currentBtnHtml);
                }
            }else if (generaldatafor=="upload"){
                //alert("hahaha");
                onSuccess = function (data) {
                    $("#btngeneralform").removeAttr("disabled").html(currentBtnHtml);
                    $("#addeditgeneral").modal("hide");   
                };
                dataSend = $(this).serialize();
                //alert(dataSend);
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", null, true, "json");
            }
            
            
            
            
            return false;
        });
        
        $('.dates').datetimepicker({
            autoclose: true,
            todayBtn: true,
            format: 'yyyy-mm-dd',
            minView:2
        });
        
        $('#btn_filter').click(function() {
            $('#tableNewsInfoElement').DataTable().clear();
            command = "<%=Command.NONE%>";
            dataSend = {
                "FRM_FIELD_DATA_FOR": "getColumn",
                "FRM_FIELD_OID": oid,
                "FRM_FIELD_LANGUAGE" : language,
                "command": command,
                "FRM_FIELD_COMBO": $('#combo').val(),
                "FRM_FIELD_LOCATION_ID": $('#FRM_FIELD_LOCATION_ID').val(),
                "FRM_FIELD_DATE_START":$('#dateStart').val(),
                "FRM_FIELD_DATE_END":$('#dateEnd').val()
            };
            onDone = function(data){
                $('#tableHeaderColumn').html(data.FRM_FIELD_HTML);
                runDataTables(0,"");
               
            };
                
            onSuccess = function(data){
                
            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", null, false, "json");            
        });
        
        $('#btn_filter_today').click(function() {
             btn_filter_range(1,"");       
        });
        $('#btn_filter_yesterday').click(function() {
             btn_filter_range(2,"");       
        });
        
        $('#btn_filter_this_week').click(function() {
             btn_filter_range(3,"");       
        });
        
        $('#btn_filter_this_month').click(function() {
             btn_filter_range(4,"");       
        });
        
        $('#btn_filter_last_month').click(function() {
             btn_filter_range(5,"");       
        });
        
        $('#btn_filter_plus').click(function() {
             btn_filter_range(7,"");       
        });
        
         $('#btn_filter_minus').click(function() {
             btn_filter_range(6,"");       
        });
        
        $('#filter_txt').keyup(function() {
             var textinput = $('#filter_txt').val();
             //alert (textinput);
             btn_filter_range(0,textinput);       
        });
        
        function btn_filter_range(type,textinput) {
            $('#tableNewsInfoElement').DataTable().clear();
            command = "<%=Command.NONE%>";
            dataSend = {
                "FRM_FIELD_DATA_FOR": "getColumn",
                "FRM_FIELD_OID": oid,
                "FRM_FIELD_LANGUAGE" : language,
                "command": command,
                "FRM_FIELD_COMBO": $('#combo').val(),
                "FRM_FIELD_LOCATION_ID": $('#FRM_FIELD_LOCATION_ID').val(),
                "FRM_FIELD_DATE_START":$('#dateStart').val(),
                "FRM_FIELD_DATE_END":$('#dateEnd').val(),
                "button_filter_range": type,
                "textinput": textinput,
            };
            
            onDone = function(data){
                $('#tableHeaderColumn').html(data.FRM_FIELD_HTML);
                runDataTables(type,textinput);
                $('#dateStart').val(data.FRM_FIELD_DATE_START);
                $('#dateEnd').val(data.FRM_FIELD_DATE_END);
            };
            
            onSuccess = function(data){
            };
            getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxCustomReportTable", null, false, "json");
        }    
        
        $('#btn_print_excel').click(function(e) {
            var reportSelected = $('#combo').val();
            var dateStart = $('#dateStart').val();
            var dateEnd = $('#dateEnd').val();
            var FRM_FIELD_LOCATION_ID = $('#FRM_FIELD_LOCATION_ID').val();
            alert("test export print "+reportSelected);
            window.open("custome_report_excel.jsp?combo="+reportSelected+"&dateStart="+dateStart+"&dateEnd="+dateEnd+"&FRM_FIELD_LOCATION_ID="+FRM_FIELD_LOCATION_ID);
            e.preventDefault();
        });
            
            
    });
    </script>
</html>
