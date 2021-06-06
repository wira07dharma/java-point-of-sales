<%-- 
    Document   : email_promotion
    Created on : Mar 23, 2018, 3:59:41 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.email.PstSettingTemplateInfo"%>
<%@page import="com.dimata.common.entity.email.SettingTemplateInfo"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.common.form.email.FrmSettingTemplateInfo"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../main/checkuser.jsp" %>

<%//
    int iCommand = FRMQueryString.requestCommand(request);
    long oidTemplate = FRMQueryString.requestLong(request, FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_ID]);

    SettingTemplateInfo sti = new SettingTemplateInfo();
    String tglStart = "";
    String tglEnd = "";
    String judul = "";
    String pesan = "";

    if (iCommand == Command.EDIT) {
        if (oidTemplate != 0) {
            try {
                sti = PstSettingTemplateInfo.fetchExc(oidTemplate);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        judul = "" + sti.getTemplateInfoSubject();
        pesan = "" + sti.getTemplateInfoContentHtml();
        tglStart = "" + Formater.formatDate(sti.getTemplateInfoDateStart(), "yyyy-MM-dd");
        tglEnd = "" + Formater.formatDate(sti.getTemplateInfoDateEnd(), "yyyy-MM-dd");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/plugin/datatables/dataTables.bootstrap.css"/>        
        <link rel="stylesheet" media="screen" href="../styles/datepicker/bootstrap-datetimepicker.min.css"/>       

        <style>

            #templateInfoElement table {font-size: 14px}
            th:after {display: none !important;}
            th {font-size: 14px; text-align: center; padding: 8px !important; background-color: lightgray}
            td {font-size: 14px}

        </style>

        <script type="text/javascript" src="../styles/bootstrap/js/jquery.min.js"></script>
        <script type="text/javascript" src="../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../styles/AdminLTE-2.3.11/plugins/ckeditor/ckeditor.js"></script>        
        <script type="text/javascript" src="../styles/datepicker/bootstrap-datetimepicker.js"></script>        

        <script>
            $(document).ready(function () {

                //==============================================================
                //SETTING

                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({"bDestroy": true,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12'><div class='progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div></div>"
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
                            if (callBackDataTables !== null) {
                                callBackDataTables();
                            }
                        },
                        "fnDrawCallback": function (oSettings) {
                            if (callBackDataTables !== null) {
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

                function runDataTables() {
                    dataTablesOptions("#templateInfoElement", "tableTemplateInfoElement", "AjaxSettingTemplateInfo", "listTemplate", callBackDataTables);
                }

                callBackDataTables = function () {

                };

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

                //==============================================================
                //ACTION

                runDataTables();
                
                $('#btnSave').click(function () {
                    var button = $(this).html();
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    
                    var dataFor = "saveTemplate";
                    var oid = $('#oidInfo').val();
                    var command = "<%=Command.SAVE%>";

                    var dataSend = "" +
                            "FRM_FIELD_DATA_FOR=" + dataFor + "&" +
                            "FRM_FIELD_OID=" + oid + "&" +
                            "command=" + command + "&" +
                            "<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_CONTENT_HTML]%>=" + CKEDITOR.instances.ckeditor.getData() +
                            "";

                    onDone = function (data) {                        
                        var error = data.RETURN_ERROR;
                        runDataTables();
                        if (error == 0) {
                            window.location.href = "email_promotion.jsp";
                        } else {
                            alert(data.RETURN_MESSAGE);
                        }
                    };
                    
                    onSuccess = function (data) {
                        $('#btnSave').removeAttr("disabled").html(button);
                    };
                    
                    var data = $('#formInfo').serialize();
                    dataSend += "&" + data;
                    getDataFunction(onDone, onSuccess, "<%=approot%>", command, dataSend, "AjaxSettingTemplateInfo", "", false, "json");
                    return false;
                });
                
                $('body').on("click", "#btnAdd", function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    window.location.href = "email_promotion.jsp?command=" + "<%=Command.ADD%>";
                });

                $('body').on("click", ".edit_template", function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>");
                    var oid = $(this).data('oid');
                    window.location.href = "email_promotion.jsp?command=" + "<%=Command.EDIT%>" + "&<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_ID]%>" + "=" + oid;
                });
                
                $('body').on("click", "#btnCancel", function () {
                    $(this).attr({"disabled":true}).html("<i class='fa fa-spinner fa-pulse'></i>&nbsp; Tunggu...");
                    window.location.href = "email_promotion.jsp";
                });

            });
        </script>
    </head>
    <body style="background-color: #EEEEEE">
        <br>
        <div class="col-md-12">

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">Daftar Template</h3>
                </div>

                <div class="box-body">                    
                    <div id="templateInfoElement" class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th style="width: 1%">No.</th>
                                    <th>Judul</th>
                                    <%--<th>Isi Pesan</th>--%>
                                    <th>Tanggal Mulai</th>
                                    <th>Tanggal Berakhir</th>
                                    <th style="width: 1%">Aksi</th>
                                </tr>
                            </thead>
                        </table>
                    </div>                    
                </div>

                <div class="box-footer" style="border-color: lightgray">
                    <button type="button" id="btnAdd" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i>&nbsp; Tambah Template</button>
                </div>

            </div>

            <%if (iCommand == Command.ADD || iCommand == Command.EDIT) {%>

            <div class="box box-primary">

                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title"><%if (iCommand == Command.ADD) {out.print("Template Baru");} else {out.print("Ubah Template");}%></h3>
                </div>

                <p></p>

                <form method="post" id="formInfo" name="<%=FrmSettingTemplateInfo.FRM_NAME_SETTING_TEMPLATE_INFO%>" class="form-horizontal">

                    <div class="box-body">
                        <input type="hidden" id="oidInfo" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_ID]%>" value="<%=oidTemplate%>">
                        <input type="hidden" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_APPLICATION]%>" value="Prochain">
                        <input type="hidden" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_TYPE_MENU]%>" value="Promosi">
                        <input type="hidden" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_TYPE_SEND]%>" value="1">
                        <input type="hidden" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_TYPE_SEND_BY]%>" value="1">

                        <div class="form-group">
                            <label class="control-label col-sm-1">Periode</label>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal mulai" class="form-control input-sm datepick" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_DATE_START]%>" value="<%=tglStart%>">
                                </div>                                
                            </div>                            
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-calendar"></i></span>
                                    <input type="text" placeholder="Tanggal berakhir" class="form-control input-sm datepick" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_DATE_END]%>" value="<%=tglEnd%>">
                                </div>                                
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-1">Judul</label>
                            <div class="col-sm-6">
                                <textarea style="resize: none" class="form-control input-sm" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_SUBJECT]%>"><%=judul%></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-1">Pesan</label>
                            <div class="col-sm-10">
                                <textarea type="text" id="ckeditor" class="ckeditor" name="<%=FrmSettingTemplateInfo.fieldNames[FrmSettingTemplateInfo.FRM_FIELD_TEMPLATE_INFO_CONTENT_HTML]%>"><%=pesan%></textarea>
                            </div>
                        </div>

                    </div>

                    <div class="box-footer" style="border-color: lightgray">
                        <div class="">
                            <button type="button" id="btnSave" class="btn btn-sm btn-primary"><i class="fa fa-save"></i>&nbsp; Simpan</button>
                            <button type="button" id="btnCancel" class="btn btn-sm btn-warning"><i class="fa fa-undo"></i>&nbsp; Batal</button>
                        </div>
                    </div>

                </form>

            </div>

            <%}%>

        </div>
    </body>
</html>
