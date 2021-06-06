<%-- 
    Document   : nilai_tukar_emas
    Created on : Oct 24, 2017, 5:58:35 PM
    Author     : dimata005
--%>

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
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_NILAI_TUKAR_EMAS); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%!
    public static final String textListHeader[][] = 
    {
        {"Berita dan Informasi","Judul","Tgl Mulai","Tgl Berakhir","Keterangan","Gambar","Lokasi"},
        {"News and Info","Title","Valid Start", "Valid End","Description","Image","Location"}
    };
    
    public static final String textListButton[][]=
    {
        {"Tambah Baru","Tambah Nilai Tukar Emas","Ubah Nilai Tukar Emas","Unggah Gambar"},
        {"Add New","Add News and Info","Edit News and Info","Upload Image"}
    };
%>
<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
    <style>
        table {font-size: 12px}
        #tableNilaiTukarEmasElement th {
            //padding: 8px;
            text-align: center;
        }
        table th:after {display: none !important}
        .modal-header, .modal-footer {padding: 10px 15px !important}
    </style>
</head> 
<%
    
%>
<body class="" style="background-color:beige">
    <div class="nonprint">
        <link href="../../styles/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/dist/css/AdminLTE.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/dist/css/skins/_all-skins.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/font-awesome/font-awesome.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/iCheck/flat/blue.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/iCheck/all.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/select2/css/select2.min.css" rel="stylesheet" type="text/css" />
        <link href="../../styles/datepicker/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
        <link href="../../styles/plugin/datatables/dataTables.bootstrap.css" rel="stylesheet" type="text/css" />

        <div class="row" style="margin-left:0px; margin-right:0px; margin-top:10px; ">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-12">
                        <div class='box box-primary'>
                            <div class='box-header with-border'>
                                <h3 class="box-title">Nilai Tukar Emas</h3>
                            </div>
                            <%//
                                Vector vMasLantakan = PstEmasLantakan.list(0,1,""+PstEmasLantakan.fieldNames[PstEmasLantakan.FLD_STATUS_AKTIF]+"=1","");
                                EmasLantakan emasLantakan = new EmasLantakan();
                                try {
                                    if (vMasLantakan.size() > 0) {
                                        emasLantakan = (EmasLantakan) vMasLantakan.get(0);
                                    }
                                } catch (Exception ex) {
                                }
                            %>
                            <div class='box-header'>
                                Emas Lantakan Beli    = Rp. <%=String.format("%,.0f", emasLantakan.getHargaBeli())%>.00<br>
                                Emas Lantakan Jual    = Rp. <%=String.format("%,.0f", emasLantakan.getHargaJual())%>.00<br>
                                Emas Lantakan Tengah  = Rp. <%=String.format("%,.0f", emasLantakan.getHargaTengah())%>.00
                            </div>
                            <div class='box-footer'>
                                <button class="btn btn-sm btn-primary btnaddgeneral" data-oid="0" data-for="showform">
                                    <i class="fa fa-plus"></i> Add Nilai Tukar Emas
                                </button>
                            </div>
                            <div class="box-body">
                                <div id="countryElement">
                                    <table class="table table-bordered table-striped">
                                        <thead>
                                            <tr>
                                                <th>No.</th>
                                                <th>Kadar</th>
                                                <th>Warna</th>
                                                <th>Lokal</th>
                                                <th>Return Lokal</th>
                                                <th>Harga Jual</th>
                                                <th>Harga Beli</th>
                                                <th>Return Lokal Rusak</th>
                                                <th>Asing</th>
                                                <th>Return Asing</th>
                                                <th>Return Asing Rusak</th>
                                                <th>Pesanan</th>
                                                <th>Tarif Retur</th>
                                                <th>Tarif Retur > 1thn</th>
                                                <th style="width: 50px">Action</th>
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

    <div id="addeditgeneral" class="modal fade nonprint" tabindex="-1">
        <div class="modal-dialog nonprint">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="addeditgeneral-title"></h4>
                </div>
                <form id="generalform" class="form-horizontal">
                    <input type="hidden" name="FRM_FIELD_DATA_FOR" id="generaldatafor">
                    <input type="hidden" name="command" value="<%= Command.SAVE%>">
                    <input type="hidden" name="FRM_FIELD_OID" id="oid">
                    <input type="hidden" name="FRM_FIELD_USER_ID" value="<%= userId%>">
                    <input type="hidden" name="FRM_FIELD_USER_NAME" value="<%= userName%>">
                    <div class="modal-body ">
                        <div class="row">
                            <div class="col-md-12">
                                <div class="box-body addeditgeneral-body">

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

</body>
<script src="../../styles/bootstrap/js/jquery.min.js" type="text/javascript"></script>
<script src="../../styles/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>  
<script src="../../styles/dimata-app.js" type="text/javascript"></script>
<script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.js" charset="UTF-8"></script>
<script type="text/javascript" src="../../styles/datepicker/bootstrap-datetimepicker.id.js" charset="UTF-8"></script>
<script src="../../styles/iCheck/icheck.min.js" type="text/javascript"></script>
<script src="../../styles/plugin/datatables/jquery.dataTables.js" type="text/javascript"></script>
<script src="../../styles/plugin/datatables/dataTables.bootstrap.js" type="text/javascript"></script>

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

        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
            var datafilter = "";
            var privUpdate = "";
            $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
            $("#" + elementId).dataTable({"bDestroy": true,
                "iDisplayLength": 10,
                "bProcessing": true,
                "oLanguage": {
                    "sProcessing": "<div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                },
                "bServerSide": true,
                "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>",
                aoColumnDefs: [
                    {
                        bSortable: false,
                        aTargets: [-1]
                    }
                ],
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
            $(elementId).unbind().click(function () {
                modalSetting("#addeditgeneral",false,null,null);
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
                        $(".modal-footer").show();
                        $(".modal-dialog").removeClass("modal-lg");
                    } else if (dataFor  == 'showformLog'){
                        $(".addeditgeneral-title").html("Log History");
                        $(".modal-footer").hide();
                        $(".modal-dialog").addClass("modal-lg");
                    }

                } else {
                    if (dataFor == 'showform') {
                        $(".modal-dialog").removeClass("modal-lg");
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
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxNilaiTukarEmas", ".addeditgeneral-body", false, "json");
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
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxNilaiTukarEmas", null, true, "json");
                    $(this).removeAttr("disabled").html(currentHtml);
                }

            });
        };

        function runDataTables() {
            dataTablesOptions("#countryElement", "tableNilaiTukarEmasElement", "AjaxNilaiTukarEmas", "list", callBackDataTables);
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
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxNilaiTukarEmas", ".addeditgeneral-body", false, "json");
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
        runDataTables();
        addeditgeneral(".btnaddgeneral");
        deletegeneral("#tableNilaiTukarEmasElement",".button-delete");
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
                    runDataTables();
                };
            }else if (generaldatafor=="upload"){
                onDone = function (data) {
                    runDataTables();
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
                    getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxNilaiTukarEmas", null, true, "json");
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
                getDataFunction(onDone, onSuccess, approot, command, dataSend, "AjaxNilaiTukarEmas", null, true, "json");
            }
            return false;
        });

    });
</script>
</html>
