<%-- 
    Document   : src_harga_pokok_item
    Created on : Jan 19, 2018, 4:15:47 PM
    Author     : Ed
--%>

<%@page import="com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@page import="com.dimata.posbo.entity.search.SrcMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.posbo.form.warehouse.CtrlMatDispatch"%>
<%@page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ page import= "com.dimata.common.entity.location.PstLocation,
         com.dimata.common.entity.location.Location,
         com.dimata.qdep.entity.I_PstDocType,
         com.dimata.qdep.form.FRMQueryString,
         com.dimata.gui.jsp.ControlLine,
         com.dimata.util.Command,
         com.dimata.gui.jsp.ControlCombo,
         com.dimata.gui.jsp.ControlDate,
         com.dimata.posbo.session.warehouse.SessMatDispatch,
         com.dimata.posbo.entity.search.SrcMatDispatch,
         com.dimata.posbo.form.warehouse.FrmMatDispatch,
         com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_HARGA_POKOK_ITEM);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%    SrcMatDispatch srcMatDispatch = new SrcMatDispatch();
    FrmSrcMatDispatch frmSrcMatDispatch = new FrmSrcMatDispatch(request, srcMatDispatch);
%>


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Harga Pokok</title>

        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/jquery-ui-1.12.1/jquery-ui.min.css"/>
        <!--link rel="stylesheet" type="text/css" href="../styles/jquery-ui-1.12.1/jquery-ui.theme.min.css"/-->
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/AdminLTE.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/dist/css/skins/_all-skins.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/font-awesome/font-awesome.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/flat/blue.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/iCheck/all.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/select2/css/select2.min.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" type="text/css" href="../../styles/plugin/datatables/dataTables.bootstrap.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/bootstrap-notify.css"/>
        <link rel="stylesheet" type="text/css" href="../../styles/datepicker/datepicker3.css"/>

        <script type="text/javascript" src="../../styles/jquery-3.1.1.min.js"></script>
        <script type="text/javascript" src="../../styles/jquery-ui-1.12.1/jquery-ui.min.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap/js/bootstrap.min.js"></script>  
        <script type="text/javascript" src="../../styles/dimata-app.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript" src="../../styles/JavaScript-autoComplete-master/auto-complete.min.js"></script>
        <script type="text/javascript" src="../../styles/iCheck/icheck.min.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/jquery.dataTables.js"></script>
        <script type="text/javascript" src="../../styles/plugin/datatables/dataTables.bootstrap.js"></script>
        <script type="text/javascript" src="../../styles/bootstrap-notify.js"></script>
        <script type="text/javascript" src="../../styles/select2/js/select2.min.js"></script>
        <script type="text/javascript" src="../../styles/datepicker/bootstrap-datepicker.js"/>></script>

    <style>

        body table {font-size: 14px}

    </style>

</head>
<body bgcolor = "#ffffff">

    <section class="content-header">
        <h1>Harga Pokok
            <small> menu</small></h1>
        <ol class="breadcrumb">
            <li>
                <a href="http://localhost:8080/dProchain_20161124/menuapp/menu_page.jsp?menu=masterdata&titlemenu=Master%20Data"> Masterdata
                    <i class="fa fa-exchange">

                    </i>
                    <li class="active">Harga Pokok Item</li>
                </a>
            </li>
        </ol>
    </section>
    <p></p>
    <div>

        <div class="row">
            <div class="col-sm-12">
                <div class="box box-default">
                    <div class="container">
                        <div class="box-body">
                            <form id="mainform" class="form-horizontal">

                                <input type="hidden" name="command" value="<%= Command.LIST%>">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--NOMOR-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Nomor:</label>
                                                <div class="col-sm-8">
                                                    <input id="selectNomor" class="form-control" type="text" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_CODE]%>" value="<%= srcMatDispatch.getDispatchCode()%>" >
                                                </div>
                                            </div>
                                        </div>


                                        <!--Status-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Status:</label>
                                                <div class="col-sm-8">
                                                    <%
                                                        Vector statusDocVal = new Vector();
                                                        Vector statusDocKey = new Vector();

                                                        statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_DRAFT);
                                                        statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                                                        statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_POSTED);
                                                        statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_FINAL);
                                                        statusDocVal.add(I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED);

                                                        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_DRAFT]);
                                                        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_CLOSED]);
                                                        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_POSTED]);
                                                        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_FINAL]);
                                                        statusDocKey.add(I_DocStatus.fieldDocumentStatus[I_DocStatus.DOCUMENT_STATUS_TO_BE_APPROVED]);
                                                    %>
                                                    <select class="form-control" id="selectStatus" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS]%>" value="<%= srcMatDispatch.getStatus()%>" >
                                                        <option value="99">Semua Status</option>

                                                        <%
                                                            for (int i = 0; i < statusDocVal.size() - 1; i++) {
                                                        %>
                                                        <option value="<%= statusDocVal.get(i)%>"><%= statusDocKey.get(i)%></option>

                                                        <%
                                                            }
                                                        %>
                                                    </select>
                                                </div>

                                            </div> <!--formgroup--> 
                                        </div> 
                                    </div>
                                </div>
                                <%
                                    Vector listLocation = new Vector();
                                    listLocation = PstLocation.listAll();
                                %>


                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--Lokasi Asal-->

                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Lokasi&nbsp;Asal:</label>
                                                <div class="col-sm-8">
                                                    <%
                                                        if (listLocation.size() > 0) {
                                                    %>
                                                    <select class="form-control" id="selectLocationAsal" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_FROM]%>">
                                                        <option value="<%= srcMatDispatch.getDispatchFrom()%>">Semua Lokasi</option>

                                                        <%

                                                            for (int i = 0; i < listLocation.size(); i++) {
                                                                Location location = (Location) listLocation.get(i);

                                                        %>
                                                        <option value="<%= location.getOID()%>"><%= location.getName()%></option>

                                                        <%

                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>   

                                        <!--Lokasi Tujuan-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Lokasi&nbsp;Tujuan:</label>
                                                <div class="col-sm-8">
                                                    <%
                                                        if (listLocation.size() > 0) {
                                                    %>
                                                    <select class="form-control" id="selectLocationTujuan" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_TO]%>">
                                                        <option value="<%= srcMatDispatch.getDispatchTo()%>">Semua Lokasi</option>

                                                        <%
                                                            //if SEMUA LOKASI choosen

                                                            for (int i = 0; i < listLocation.size(); i++) {
                                                                Location location = (Location) listLocation.get(i);

                                                        %>
                                                        <option value="<%= location.getOID()%>"><%= location.getName()%></option>

                                                        <%

                                                                }
                                                            }
                                                        %>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--Tanggal-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Tanggal Awal:</label>
                                                <div class="col-sm-8">
                                                    <input id="dateAwal" name="<%= frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_FROM]%>" type="text" class="datepicker form-control">
                                                </div>  
                                            </div>
                                        </div>
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Tanggal Akhir:</label>
                                                <div class="col-sm-8">
                                                    <input id="dateAkhir" name="<%= frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_TO]%>" type="text" class="datepicker form-control">
                                                </div>  
                                            </div>
                                        </div>

                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <div class="col-sm-4 col-sm-offset-4">
                                                    <input id="allDate" type="checkbox" name="<%= frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_FROM]%>" value="0" ><label>&nbsp;Semua&nbsp;Tanggal</label>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--Urut-->
                                        <div class="col-sm-6">
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Urut&nbsp;Berdasarkan:</label>
                                                <div class="col-sm-8">
                                                    <select class="form-control" id="selectUrut" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_SORT_BY]%>" value="<%= srcMatDispatch.getSortBy()%>" >
                                                        <option value="<%=PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_CODE]%>">Nomor</option>
                                                        <option value="<%=PstMatDispatch.fieldNames[PstMatDispatch.FLD_LOCATION_ID]%>">Lokasi Asal</option>
                                                        <option value="<%=PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_TO]%>">Lokasi Tujuan</option>
                                                        <option value="<%=PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_DATE]%>">Tanggal</option>
                                                        <option value="<%=PstMatDispatch.fieldNames[PstMatDispatch.FLD_DISPATCH_STATUS]%>">Status</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-sm-12">
                                        <!--Button-->
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <div class="col-sm-10 col-sm-offset-2">
                                                    <button type="button" class="btn btn-primary" id="btnSearch" data-oid="0" data-for="searchProduksi"><i class="fa fa-search"></i>&nbsp;Cari&nbsp;Dokumen&nbsp;HPP</button>
                                                    &nbsp;
                                                    <a href="add_harga_pokok_group.jsp" class="btn btn-primary" id="btnAdd" data-oid="0" data-for="addProduksi"><i class="fa fa-plus"></i>&nbsp;Tambah&nbsp;Dokumen&nbsp;HPP</a>
                                                </div>
                                            </div>    
                                        </div>    
                                    </div>    
                                </div>    
                            </form>  
                        </div>
                    </div><!--box-->
                </div><!--COLSM12-->
            </div><!--COLSM12-->

            <!--TABLE LIST-->
            <div class="col-sm-12">
                <div class="box-body showList-body">

                    <div class='row'>
                        <div class='col-sm-12'>
                            <div id="tableList" class="box box-info">
                                <div class="box-header with-border">
                                    <h3 class="box-title">List Harga Pokok</h3>
                                </div>
                                <table class='table table-bordered table-striped table-info'>
                                    <thead class='thead-inverse'>
                                        <tr>
                                            <th>No.</th>
                                            <th>Kode</th>
                                            <th>Lokasi Asal</th>
                                            <th>Lokasi Tujuan</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>

                                </table>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div> <!--ROW-->
    </div>
</div>
<script>
    $(document).ready(function () {
        //INITIAL
        var command = $("#command").val();
        var approot = "<%= approot%>";
        var oid = null;
        var dataFor = null;

        //FUNCTION VARIABLE
        var onDone = null;
        var onSuccess = null;
        var dataSend = null;


        //ENTER KEY
        function keyEnter(element1, element2) {
            $(element1).keypress(function (e) {
                if (e.which === 13) {
                    $(element2).focus();
                    return false;
                }
            });
        }
        keyEnter("#selectNomor", "#selectStatus");
        keyEnter("#selectStatus", "#selectLocationAsal");
        keyEnter("#selectLocationAsal", "#selectLocationTujuan");
        keyEnter("#selectLocationTujuan", "#dateAwal");
        keyEnter("#dateAwal", "#dateAkhir");
        keyEnter("#dateAkhir", "#selectUrut");
        keyEnter("#selectUrut", "#btnSearch");

        //DATEPICKER
        $(".datepicker").datepicker({
            format: "yyyy-mm-dd",
            autoclose: true
        });

        //ALL DATE
        $("#allDate").change(function () {
            if ($(this).is(':checked')) {
                $(".datepicker").attr('disabled', true);
                $(".datepicker").val("");

            } else {
                $(".datepicker").attr('disabled', false);
            }
        });

        $(".haha").click(function () {
            var oidDispatch = $(this).data("oid");
        });

        function cmdEdit(oid)
        {
            document.mainform.hidden_dispatch_id.value = oid;
            document.mainform.start.value = 0;
            document.mainform.approval_command.value = "<%=Command.APPROVE%>";
            document.mainform.command.value = "<%=Command.EDIT%>";
            document.mainform.action = "add_produksi_item.jsp";
            document.mainform.submit();
        }

        ///////////////////////////DATA TABLE////////////////////////////////

        function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
            var dataSend = $("#mainform").serialize();

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
                "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>&" + dataSend,
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
            dataTablesOptions("#tableList", "tabletableList", "AjaxSrcProduksi", "listeventSrc", null);
        }

        $('#btnSearch').click(function () {

            runDataTables();
        });

    });
</script>
</body>
</html>

