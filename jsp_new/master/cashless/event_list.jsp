<%-- 
    Document   : event_list
    Created on : Jun 11, 2019, 11:00:10 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>Event List</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .dataTable > thead > tr > th {white-space: nowrap; padding-right: 8px !important; width: auto !important}
            .dataTable > thead > tr > th[class*="sort"]:after{content: "" !important;}
        </style>
        <script>
            $(document).ready(function () {

                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";
                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({"bDestroy": true,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12 progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
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
                    $(elementIdParent).css("font-size", "12px");

                    $("#" + elementId).css("width", "100%");
                }

                function runDataTables() {
                    dataTablesOptions("#eventElement", "tableEventElement", "AjaxCashless", "listEvent", null);
                }
                
                runDataTables();

                $('body').on("click",".btn-xs",function() {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({disabled: true});
                });

                $('body').on("click",".btn-sm",function() {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
                });
            });
        </script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Event</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Event</li>
                <li>Event Setup</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h4 class="box-title">Event List</h4>
                </div>
                <div class="box-body">
                    <a href="event.jsp" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i> Add New Event</a>
                    <p></p>
                    <div id="eventElement">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th style="width: 1%">No.</th>
                                    <th>Event Code</th>
                                    <th>Event Title</th>
                                    <th>Description</th>
                                    <th>Event Start Time</th>
                                    <th>Event End Time</th>
                                    <th>Company</th>
                                    <th style="width: 1%; text-align: center"><i class="fa fa-gear"></i></th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
