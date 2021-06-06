<%-- 
    Document   : event_list
    Created on : Jun 11, 2019, 11:00:10 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstClsUserModule"%>
<%@page import="com.dimata.posbo.entity.masterdata.ClsUserModule"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.ajax.AjaxCashless"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%//
    int command = FRMQueryString.requestCommand(request);
    long eventId = FRMQueryString.requestLong(request, "event_id");
    int tabView = FRMQueryString.requestInt(request, "tab_view");
    
    Event event = new Event();
    String message = "";
    String msgStyle = "label-success";
    int error = 0;
    if (eventId != 0) {
        try {
            event = PstEvent.fetchExc(eventId);
            
            if (command == Command.SAVE) {
                //DELETE ALL DATA MAPPING FOR THIS EVENT
                Vector<ClsUserModule> listMapping = PstClsUserModule.list(0, 0, PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID] + " = '" + eventId + "'", "");
                for (ClsUserModule map : listMapping) {
                    PstClsUserModule.deleteExc(map.getOID());
                }
                String[] checkedMapping = FRMQueryString.requestStringValues(request, "FRM_MAPPING");
                if (checkedMapping != null) {
                    for (String s : checkedMapping) {
                        long mapUserId = FRMQueryString.requestLong(request, "FRM_USER_ID_" + s);
                        int mapModuleId = FRMQueryString.requestInt(request, "FRM_MODULE_ID_" + s);
                        
                        //CHECK IF DATA ALREADY EXIST
                        String whereCheck = PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID] + " = '" + eventId + "'";
                        whereCheck += " AND " + PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID] + " = '" + mapUserId + "'";
                        whereCheck += " AND " + PstClsUserModule.fieldNames[PstClsUserModule.FLD_ID_MODUL_ENABLE] + " = '" + mapModuleId + "'";
                        Vector listExist = PstClsUserModule.list(0, 0, whereCheck, "");
                        if (listExist.isEmpty()) {
                            ClsUserModule module = new ClsUserModule();
                            module.setEventOID(eventId);
                            module.setUserId(mapUserId);
                            module.setIdModulEnable(mapModuleId);
                            PstClsUserModule.insertExc(module);
                        }
                    }
                    message = "Mapping data is updated";
                }
            }
        } catch (Exception e) {
            error +=1;
            message = e.getMessage();
            System.out.println(e.getMessage());
        }
    }
    
    if (error > 0) {
        msgStyle = "label-danger";
    }
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>Event List</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .table thead tr th {white-space: nowrap}

            input[type="checkbox"], input[type="radio"]{
                position: absolute;
                right: 9000px;
            }

            /*Check box*/
            input[type="checkbox"] + .label-text:before{
                content: "\f096";
                font-family: "FontAwesome";
                speak: none;
                font-style: normal;
                font-weight: normal;
                font-variant: normal;
                text-transform: none;
                line-height: 1;
                -webkit-font-smoothing:antialiased;
                width: 1em;
                display: inline-block;
                margin-right: 5px;
            }

            input[type="checkbox"]:checked + .label-text:before{
                content: "\f14a";
                color: #2980b9;
                animation: effect 250ms ease-in;
            }

            input[type="checkbox"]:disabled + .label-text{
                color: #aaa;
            }

            input[type="checkbox"]:disabled + .label-text:before{
                content: "\f0c8";
                color: #ccc;
            }


        </style>
        <script>
            $(document).ready(function () {
                function settingSelect2(id, placeholder) {
                    $(id).select2({
                        placeholder: placeholder
                    });
                }
                settingSelect2('.select2event', 'Event');
                
                $('#event').change(function () {
                    window.location = "map_event_user.jsp?tab_view=<%=tabView%>&event_id=" + $(this).val();
                });
                
                $('#formMapping').submit(function () {
                    $('#btnSaveMapping').html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
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
                <li>Map User</li>
            </ol>
        </section>
                       
        <section class="content">
            <div class="box box-warning">
                <% if (tabView == 1) { %>
                <ul class="nav nav-tabs">
                    <li><a href="event.jsp?command=<%=Command.EDIT%>&tab_view=1&event_id=<%=eventId%>">Event Setup</a></li>
                    <li><a href="mapping_event_item.jsp?tab_view=1&event_id=<%=eventId%>">Map Item</a></li>
                    <li><a href="mapping_event_bar.jsp?tab_view=1&event_id=<%=eventId%>">Map Bar</a></li>
                    <li class="active text-bold"><a href="map_event_user.jsp?tab_view=1&event_id=<%=eventId%>">Map User</a></li>
                </ul>
                <% } else { %>
                <div class="box-header with-border">
                    <h4 class="box-title">Map User</h4>
                </div>
                <% } %>
                
                <div class="box-body">
                    <% if (tabView == 0) { %>
                    <div class="form-inline">
                        <span>Choose Event :</span>
                        <select class="form-control input-sm select2event" id="event">
                            <option value="0">- Select -</option>
                            <%//
                                Vector<Event> listEvent = PstEvent.list(0, 0, PstEvent.fieldNames[PstEvent.FLD_COMPANY_ID] + " = '" + userSession.getAppUser().getCompanyId() + "'", PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE] + " ASC ");
                                for (Event e : listEvent) {
                                    out.print("<option " + (eventId == e.getOID() ? "selected" : "") + " value='" + e.getOID() + "'>" + e.getEventTitle() + "</option>");
                                }
                            %>
                        </select>
                        
                        <% if (eventId != 0) { %>
                        <button type="button" id="btnAddMapping" class="btn btn-sm btn-primary hidden"><i class="fa fa-plus"></i> Add Mapping</button>
                        <% } %>
                    </div>
                    <br>
                    <% } else { %>
                    <h4>Event title : <%= event.getEventTitle() %></h4>
                    <br>
                    <% } %>
                    
                    <% if (eventId != 0) {%>
                    <%//
                        int totalMapping = PstClsUserModule.list(0, 0, PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID] + " = '" + eventId + "'", "").size();
                    %>
                    <p><b>Map user to module :</b> <%= totalMapping %> mapping</p>
                    <form id="formMapping">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="event_id" value="<%=eventId%>">
                        <input type="hidden" name="tab_view" value="<%=tabView%>">
                        <table class="table table-bordered">
                            <tr class="label-default">
                                <th colspan="3" class="text-center">USER</th>
                                <th colspan="<%=AjaxCashless.strModule.length %>" class="text-center">MODULE</th>
                            </tr>
                            <tr>
                                <th>No.</th>
                                <th>Name</th>
                                <th>Login ID</th>
                                <% for (String s : AjaxCashless.strModule) { %>
                                <th class="text-center"><%= s %></th>
                                <% } %>
                            </tr>
                            <%//
                                Vector<AppUser> listUser = PstAppUser.listFullObj(0, 0, PstAppUser.fieldNames[PstAppUser.FLD_COMPANY_ID] + " = '" + userSession.getAppUser().getCompanyId() + "'", PstAppUser.fieldNames[PstAppUser.FLD_LOGIN_ID]);
                                int no = 0;
                                for (AppUser user : listUser) {
                                    no++;
                            %>
                            <tr>
                                <td><%=no%>.</td>
                                <td><%=user.getFullName() %></td>
                                <td><%=user.getLoginId() %></td>
                                <% for (int i = 0; i < AjaxCashless.strModule.length; i++) { %>
                                <%//
                                    String whereModule = PstClsUserModule.fieldNames[PstClsUserModule.FLD_EVENT_OID] + " = '" + eventId + "'";
                                    whereModule += " AND " + PstClsUserModule.fieldNames[PstClsUserModule.FLD_USER_ID] + " = '" + user.getOID() + "'";
                                    whereModule += " AND " + PstClsUserModule.fieldNames[PstClsUserModule.FLD_ID_MODUL_ENABLE] + " = '" + i + "'";
                                    int exist = PstClsUserModule.list(0, 0, whereModule, "").size();
                                %>
                                <td class="text-center">
                                    <label><input type="checkbox" <%= (exist == 1) ? "checked" : ""%> name="FRM_MAPPING" value="<%=no%>_<%=i%>"><span class="label-text"></span></label>
                                    <input type="hidden" name="FRM_USER_ID_<%=no%>_<%=i%>" value="<%=user.getOID()%>">
                                    <input type="hidden" name="FRM_MODULE_ID_<%=no%>_<%=i%>" value="<%=i%>">
                                </td>
                                <% } %>
                            </tr>
                            <%
                                }
                                
                                if (listUser.isEmpty()) {
                                    out.print("<tr>");
                                    out.print("<td colspan='3' class='text-center'>No data</td>");
                                    out.print("<td colspan='" + AjaxCashless.strModule.length + "' class='text-center'>No data</td>");
                                    out.print("</tr>");
                                }
                            %>
                        </table>
                        <button id="btnSaveMapping" class="btn btn-sm btn-primary pull-right"><i class="fa fa-check"></i> Save Mapping</button>
                        <br>
                    </form>
                    <% }%>
                    <div class="<%=msgStyle %> text-center"><%=message%></div>
                </div>
            </div>
        </section>

        <%--
        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h4 class="box-title">List</h4>
                </div>
                <div class="box-body">
                    <div class="form-group row mb-4">
                        <label class="col-form-label text-md-right col-md-2">Select Event</label>
                        <div class="col-sm-6 col-md-3">
                            <select name="selectEvent" class="form-control" id="event">
                                <option value="">Select</option>
                                <%
                                    for (int i = 0; i < listEvent.size(); i++) {
                                        Event event = (Event) listEvent.get(i);
                                %>
                                <option value="<%=event.getOID()%>"><%=event.getEventTitle()%></option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <p></p>
                    <div class="table-responsive">

                    </div>
                </div>
            </div>
        </section>
        --%>
                            
        <div class="modal" tabindex="-1" role="dialog" id="modalMap">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Mapping</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form name="frmMap" id="formMap">
                            <input type="hidden" name="event_id" value="" id="event_id">
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" id="btnSave">Save</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>						

    </body>
    <script type="text/javascript">
        /*
            $(document).ready(function () {
                var base = "<%= approot%>";
                function ajaxLoadList(url, data, type, appendTo, another, optional, optional2) {
                    $.ajax({
                        url: "" + url + "",
                        data: "" + data + "",
                        type: "" + type + "",
                        dataType: "json",
                        async: false,
                        cache: false,
                        success: function (data) {

                        },
                        error: function (data) {
                            //alert('error');
                        }
                    }).done(function (data) {
                        $('' + appendTo + '').html(data.FRM_FIELD_HTML);
                        if (another === "loadData") {
                            $("#btnAdd").click(function () {
                                var url = "" + base + "/AjaxCashless";
                                var eventId = $("#event").val();
                                var data = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=formAdd&EVENT_ID=" + eventId;
                                ajaxLoadList(url, data, "POST", "#modalMap #formMap", "addData", "", "");
                                $('#modalMap').modal('show');
                            });
                        }
                        if (another === "addData") {
                            $('#user').select2({
                                placeholder: "Select User",
                                allowClear: true
                            });
                            $("#btnSave").click(function () {
                                var url = "" + base + "/AjaxCashless";
                                var str = $("#formMap").serialize();
                                var data = str + "&command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveMapModul";
                                ajaxLoadList(url, data, "POST", ".table-responsive", "saveMapModul", "", "");
                            });
                        }
                        if (another === "saveMapModul") {
                            $('#modalMap').modal('close');
                        }
                    });
                }

                $("#event").change(function () {
                    var url = "" + base + "/AjaxCashless";
                    var eventId = $(this).val();
                    var data = "command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=loadUserMap&EVENT_ID=" + eventId;
                    ajaxLoadList(url, data, "POST", ".table-responsive", "loadData", "", "");
                });
            });
        */    
    </script>
</html>
