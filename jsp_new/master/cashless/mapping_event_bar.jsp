<%-- 
    Document   : mapping_event_bar.jsp
    Created on : Jun 12, 2019, 3:11:02 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.Room"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstRoom"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    long eventId = FRMQueryString.requestLong(request, "event_id");
    long roomId = FRMQueryString.requestLong(request, "room_id");
    long itemId = FRMQueryString.requestLong(request, "item_id");
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
                int deleted = PstEvent.deleteMappingBarItem(eventId);

                int saved = 0;
                String[] checkedMapping = FRMQueryString.requestStringValues(request, "FRM_MAPPING");
                if (checkedMapping != null) {
                    for (String s : checkedMapping) {
                        long mapItemId = FRMQueryString.requestLong(request, "FRM_ITEM_ID_" + s);
                        long mapRoomId = FRMQueryString.requestLong(request, "FRM_ROOM_ID_" + s);
                        
                        //CHECK IF DATA ALREADY EXIST
                        int exist = PstEvent.checkMappingBarItemExist(mapRoomId, eventId, mapItemId);
                        if (exist == 0) {
                            saved += PstEvent.saveMappingBarItem(mapRoomId, eventId, mapItemId);
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
        <title>JSP Page</title>
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .smallHeader {width: 1%!important}
            
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

                function settingSelect2(id, placeholder) {
                    $(id).select2({
                        placeholder: placeholder
                    });
                }

                settingSelect2('.select2event', 'Event');
                settingSelect2('.select2item', 'Item');

                $('#event').change(function () {
                    window.location = "mapping_event_bar.jsp?tab_view=<%=tabView%>&event_id=" + $(this).val();
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
                <li>Map Bar</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-warning">
                <% if (tabView == 1) { %>
                <ul class="nav nav-tabs">
                    <li><a href="event.jsp?command=<%=Command.EDIT%>&tab_view=1&event_id=<%=eventId%>">Event Setup</a></li>
					<li><a href="ticket.jsp?tab_view=1&event_id=<%=eventId%>">Ticket</a></li>
                    <li><a href="mapping_event_item.jsp?tab_view=1&event_id=<%=eventId%>">Map Item</a></li>
                    <li class="active text-bold"><a href="mapping_event_bar.jsp?tab_view=1&event_id=<%=eventId%>">Map Bar</a></li>
                    <li><a href="map_event_user.jsp?tab_view=1&event_id=<%=eventId%>">Map User</a></li>
                </ul>
                <% } else { %>
                <div class="box-header with-border">
                    <h4 class="box-title">Map Bar</h4>
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
                    </div>
                    <br>
                    <% } else { %>
                    <h4>Event title : <%= event.getEventTitle() %></h4>
                    <br>
                    <% } %>

                    <% if (eventId != 0) {%>
                    <%//
                        int totalMapping = PstEvent.listMappingBarItem(event.getOID()).size();
                    %>
                    <p><b>Map item to bar :</b></p>
                    <form id="formMapping" method="POST">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="event_id" value="<%=eventId%>">
                        <input type="hidden" name="tab_view" value="<%=tabView%>">
                        <table class="table table-bordered">
                            <%//
                                Vector<Room> listBar = PstRoom.list(0, 0, "", PstRoom.fieldNames[PstRoom.FLD_NAME]);
                            %>
                            <tr class="label-default">
                                <th colspan="4" class="text-center">ITEM</th>
                                <th colspan="<%=listBar.size()%>" class="text-center">BAR</th>
                            </tr>
                            <tr>
                                <th style="width: 1%; vertical-align: middle">No.</th>
                                <th>Name</th>
                                <th>SKU</th>
                                <th>Category</th>
                                    <%for (Room r : listBar) {%>
                                <th class="text-center"><%=r.getName()%></th>
                                    <% } %>
                                <%//
                                    if (listBar.isEmpty()) {
                                        out.print("<th class='text-center'>No data</th>");
                                    }
                                %>
                            </tr>
                            <%//
                                ArrayList<HashMap> listEventItem = PstEvent.listEventItem(event.getOID());
                                int no = 0;
                                for (HashMap<String, String> data : listEventItem) {
                                    no++;
                            %>
                            <tr>
                                <td><%=no%>.</td>
                                <td><%=data.get("MATERIAL_NAME")%></td>
                                <td><%=data.get("MATERIAL_SKU")%></td>
                                <td><%=data.get("CATEGORY_NAME")%></td>
                                <%for (Room r : listBar) {%>
                                <%
                                    int exist = PstEvent.checkMappingBarItemExist(r.getOID(), eventId, Long.valueOf(data.get("MATERIAL_ID")));
                                %>
                                <td class="text-center">
                                    <label><input type="checkbox" <%= (exist == 1) ? "checked" : ""%> name="FRM_MAPPING" value="<%=no%>_<%=r.getOID()%>"><span class="label-text"></span></label>
                                    <input type="hidden" name="FRM_ITEM_ID_<%=no%>_<%=r.getOID()%>" value="<%=data.get("MATERIAL_ID")%>">
                                    <input type="hidden" name="FRM_ROOM_ID_<%=no%>_<%=r.getOID()%>" value="<%=r.getOID()%>">
                                </td>
                                <% } %>
                                <%//
                                    if (listBar.isEmpty()) {
                                        out.print("<td class='text-center'>No data</td>");
                                    }
                                %>
                            </tr>
                            <%
                                }
                                
                                if (listEventItem.isEmpty()) {
                                    out.print("<tr>");
                                    out.print("<td colspan='4' class='text-center'>No data</td>");
                                    out.print("<td colspan='" + listBar.size() + "' class='text-center'>No data</td>");
                                    out.print("</tr>");
                                }
                            %>
                        </table>
                        <%= totalMapping %> data mapping
                        <button id="btnSaveMapping" class="btn btn-sm btn-primary pull-right"><i class="fa fa-check"></i> Save Mapping</button>
                        <br>
                    </form>
                    <% }%>
                    <div class="<%=msgStyle %> text-center"><%=message%></div>
                </div>
            </div>
        </section>
    </body>
</html>
