<%-- 
    Document   : ticket
    Created on : Jun 25, 2019, 1:57:43 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.posbo.entity.masterdata.ClsSpending"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstClsSpending"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%//
    int command = FRMQueryString.requestCommand(request);
    long eventId = FRMQueryString.requestLong(request, "event_id");
    long itemId = FRMQueryString.requestLong(request, "item_id");
    int tabView = FRMQueryString.requestInt(request, "tab_view");

    Event event = new Event();
    String message = "";
    String msgStyle = "label-success";
    int error = 0;
    if (eventId != 0) {
        try {
            event = PstEvent.fetchExc(eventId);
        } catch (Exception e) {
            error += 1;
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
        <title>Event Item</title>
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .dataTable > thead > tr > th {white-space: nowrap; padding-right: 8px !important; width: auto !important}
            .dataTable > thead > tr > th[class*="sort"]:after{content: "" !important;}
        </style>
        
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Event</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Event</li>
                <li>Map Item</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-warning">
                <% if (tabView == 1) {%>
                <ul class="nav nav-tabs">
                    <li><a href="event.jsp?command=<%=Command.EDIT%>&tab_view=1&event_id=<%=eventId%>">Event Setup</a></li>
					<li class="active text-bold"><a href="ticket.jsp?tab_view=1&event_id=<%=eventId%>">Ticket</a></li>
                    <li><a href="mapping_event_item.jsp?tab_view=1&event_id=<%=eventId%>">Map Item</a></li>
                    <li><a href="mapping_event_bar.jsp?tab_view=1&event_id=<%=eventId%>">Map Bar</a></li>
                    <li><a href="map_event_user.jsp?tab_view=1&event_id=<%=eventId%>">Map User</a></li>
                </ul>
                <% } else { %>
                <div class="box-header with-border">
                    <h4 class="box-title">Ticket</h4>
                </div>
                <% } %>
                <div class="box-body">
                    <div class="form-inline">
                        <% if (tabView == 0) { %>
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
                        <% } else {%>
                        <h4>Event title : <%= event.getEventTitle()%></h4>
                        <% } %>

                        <% if (eventId != 0) { %>
                        <button type="button" id="btnAddItem" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i> Add Ticket</button>
                        <% } %>
                    </div>
					<div class="table-responsive">

                    </div>
                </div>
            </div>
        </section>

        <div class="modal" tabindex="-1" role="dialog" id="modalMap">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Add Ticket</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <form name="frmMap" id="formMap">
                            
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
                        if (another === "addData") {
                            $("#btnSave").click(function () {
                                var url = "" + base + "/AjaxCashless";
                                var str = $("#formMap").serialize();
								$("btnSave").html("Please Wait");
                                var data = str + "&command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveTicket";
                                ajaxLoadList(url, data, "POST", ".table-responsive", "saveTicket", "", "");
								$("#modalMap .close").click();
                            });
                        }
                        if (another === "saveTicket" || another == "deleteTicket") {
							$("btnSave").html("Save");
							$("#modalMap .close").click();
							loadTicket();
                        }
						
						$(".btnEdit").click(function () {
							var url = "" + base + "/AjaxCashless";
							var eventId = "<%=event.getOID()%>";
							var oid = $(this).data('oid');
							var data = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=formAddTicket&EVENT_ID=" + eventId+"&CLS_SPENDING_ID="+oid;
							ajaxLoadList(url, data, "POST", "#modalMap #formMap", "addData", "", "");
							$('#modalMap').modal('show');
						});
						
						$(".btnDelete").click(function () {
							if (confirm("Are you sure?")) {
								var url = "" + base + "/AjaxCashless";
								var eventId = "<%=event.getOID()%>";
								var oid = $(this).data('oid');
								var data = "command=<%=Command.DELETE%>&FRM_FIELD_DATA_FOR=deleteTicket&TICKET_ID=" + oid;
								ajaxLoadList(url, data, "POST", "", "deleteTicket", "", "");
							}
						});
						
						
                    });
                }
				
				$("#btnAddItem").click(function () {
					var url = "" + base + "/AjaxCashless";
					var eventId = "<%=event.getOID()%>";
					var data = "command=<%=Command.NONE%>&FRM_FIELD_DATA_FOR=formAddTicket&EVENT_ID=" + eventId;
					ajaxLoadList(url, data, "POST", "#modalMap #formMap", "addData", "", "");
					$('#modalMap').modal('show');
				});


				loadTicket();

               function loadTicket() {
                    var url = "" + base + "/AjaxCashless";
                    var eventId = "<%=event.getOID()%>";
                    var data = "command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=loadTicket&EVENT_ID=" + eventId;
                    ajaxLoadList(url, data, "POST", ".table-responsive", "loadData", "", "");
                }
			
					$('#event').select2({
						placeholder: "Select Event",
						allowClear: true
					});

					function save(){
						$("#btnSave").click(function(){
							var url = ""+base+"/AjaxCashless";
							var str = $( "#formMap" ).serialize();
							var data = str + "&command=<%=Command.SAVE%>&FRM_FIELD_DATA_FOR=saveTicket";
							ajaxLoadList(url,data,"POST",".table-responsive","saveTicket","","");
							$("#modalMap .close").click();
						}); 
					}

					$("#event").change(function(){
						var url = ""+base+"/AjaxCashless";
						var eventId = $(this).val();
						var data = "command=<%=Command.LIST%>&FRM_FIELD_DATA_FOR=loadTicket&EVENT_ID="+eventId;
						ajaxLoadList(url,data,"POST",".table-responsive","loadData","","");
					  }); 
				});
    </script>
</html>