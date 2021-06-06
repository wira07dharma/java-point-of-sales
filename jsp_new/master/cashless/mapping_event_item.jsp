<%-- 
    Document   : mapping_event_item
    Created on : Jun 11, 2019, 1:47:30 PM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.posbo.entity.masterdata.PstSubCategory"%>
<%@page import="com.dimata.posbo.entity.masterdata.SubCategory"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    long eventId = FRMQueryString.requestLong(request, "event_id");
    long itemId = FRMQueryString.requestLong(request, "item_id");
    int tabView = FRMQueryString.requestInt(request, "tab_view");
	double itemPrice = FRMQueryString.requestDouble(request, "price");
	
	long oidMaterial =FRMQueryString.requestLong(request, "item_oid");
	
	if (command == Command.EDIT){
		PstEvent.updateEventPrice(eventId, oidMaterial, itemPrice);
		oidMaterial = 0;
	}

    Event event = new Event();
    String message = "";
    String msgStyle = "label-success";
    int error = 0;
    if (eventId != 0) {
        try {
            event = PstEvent.fetchExc(eventId);

            if (command == Command.DELETE) {
                int existOnMappingBar = PstEvent.checkMappingBarItemExist(eventId, itemId);
                if (existOnMappingBar > 0) {
                    message = "Cannot delete this item! This item is used in bar mapping";
                    error += 1;
                } else {
                    int deleted = PstEvent.deleteEventItem(eventId, itemId);
                    if (deleted > 0) {
                        message = "Item is deleted";
                    } else {
                        error += 1;
                        message = "Failed to delete item";
                    }
                }
            }
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

                function dataTablesOptions(elementIdParent, elementId, servletName, dataFor, callBackDataTables) {
                    var datafilter = "";
                    var privUpdate = "";

                    var additionalParameter = "&FRM_FLD_EVENT_ID=<%=eventId%>";
                    additionalParameter += "&FRM_FLD_CATEGORY_ID=" + $('#category').val();
					additionalParameter += "&FRM_FLD_SUB_CATEGORY_ID=" + $('#subcategory').val();

                    $(elementIdParent).find('table').addClass('table-bordered table-striped table-hover').attr({'id': elementId});
                    $("#" + elementId).dataTable({"bDestroy": true,
                        "iDisplayLength": 10,
                        "bProcessing": true,
                        "oLanguage": {
                            "sProcessing": "<div class='col-sm-12 progress progress-striped active'><div class='progress-bar progress-bar-primary' style='width: 100%'><b>Please Wait...</b></div></div>"
                        },
                        "bServerSide": true,
                        "sAjaxSource": "<%= approot%>/" + servletName + "?command=<%= Command.LIST%>&FRM_FIELD_DATA_FILTER=" + datafilter + "&FRM_FIELD_DATA_FOR=" + dataFor + "&privUpdate=" + privUpdate + "&FRM_FLD_APP_ROOT=<%=approot%>" + additionalParameter,
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

                function runDataTablesItem() {
                    dataTablesOptions("#itemElement", "tableItemElement", "AjaxCashless", "listItem", null);
                }

                function settingSelect2(id, placeholder) {
                    $(id).select2({
                        placeholder: placeholder
                    });
                }

                settingSelect2('.select2event', 'Event');
                settingSelect2('.select2category', 'Category');
				settingSelect2('.select2subcategory', 'Sub Category');

                var modalSetting = function (elementId, backdrop, keyboard, show) {
                    $(elementId).modal({
                        backdrop: backdrop,
                        keyboard: keyboard,
                        show: show
                    });
                };

                $('#btnAddItem').click(function () {
                    var btn = $(this).html();
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
                    $("#modalAddItem").modal("show");
                    runDataTablesItem();
                    $(this).html(btn).removeAttr('disabled');
                });

                $('.btn_delete_event').click(function () {
                    if (confirm("Delete this item ?")) {
                        var btn = $(this).html();
                        var eventId = $(this).data('event');
                        var itemId = $(this).data('item');
                        $(this).html("<i class='fa fa-spinner fa-pulse'></i>").attr({disabled: true});
                        window.location = "mapping_event_item.jsp?tab_view=<%=tabView%>&command=<%=Command.DELETE%>&event_id=" + eventId + "&item_id=" + itemId;
                    }
                });

                $('#event').change(function () {
                    window.location = "mapping_event_item.jsp?tab_view=<%=tabView%>&event_id=" + $(this).val();
                });

                $('#category, #currency').change(function () {
                    runDataTablesItem();
                });
				
				$('#subcategory, #currency').change(function () {
                    runDataTablesItem();
                });

                $('#btnSaveMultiItem').click(function () {
                    var btn = $(this).html();
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});

                    var command = "<%=Command.SAVE%>";
                    var dataSend = $('#formMapEventItem').serialize() + "&command=" + command;
                    var onDone = function (data) {
                        alert(data.MESSAGE_RETURN);
                        if (data.ERROR_RETURN === 0 || data.ERROR_RETURN === "0") {
                            $("#modalAddItem").modal("hide");
                            window.location = "mapping_event_item.jsp?tab_view=<%=tabView%>&event_id=<%=eventId%>";
                        }
                        $('#btnSaveMultiItem').html(btn).removeAttr('disabled');
                    };
                    var onSuccess = function (data) {

                    };
                    getDataFunction(onDone, onSuccess, "<%=approot%>", command, dataSend, "AjaxCashless", null, false, "json");
                });

            });
        </script>
		<script>
			function cmdEditPrice(oid){
                document.frm.item_oid.value=oid;
				document.frm.command.value=<%=Command.LIST%>;
                document.frm.action="mapping_event_item.jsp";
                document.frm.submit();
            }
			function cmdSave(){
                document.frm.command.value=<%=Command.EDIT%>;
                document.frm.action="mapping_event_item.jsp";
                document.frm.submit();
            }
		</script>
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
			<form name="frm" id="frm">
				<input type="hidden" name="item_oid" value="<%=oidMaterial%>">
				<input type="hidden" name="event_id" value="<%=eventId%>">
				<input type="hidden" name="command" value="<%=command%>">
				<div class="box box-warning">
					<% if (tabView == 1) {%>
					<ul class="nav nav-tabs">
						<li><a href="event.jsp?command=<%=Command.EDIT%>&tab_view=1&event_id=<%=eventId%>">Event Setup</a></li>
						<li><a href="ticket.jsp?tab_view=1&event_id=<%=eventId%>">Ticket</a></li>
						<li class="active text-bold"><a href="mapping_event_item.jsp?tab_view=1&event_id=<%=eventId%>">Map Item</a></li>
						<li><a href="mapping_event_bar.jsp?tab_view=1&event_id=<%=eventId%>">Map Bar</a></li>
						<li><a href="map_event_user.jsp?tab_view=1&event_id=<%=eventId%>">Map User</a></li>
					</ul>
					<% } else { %>
					<div class="box-header with-border">
						<h4 class="box-title">Map Item</h4>
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
							<button type="button" id="btnAddItem" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i> Add Item</button>
							<% } %>
						</div>

						<% if (eventId != 0) {%>
						<br>
						<%//
							ArrayList<HashMap> listEventItem = PstEvent.listEventItem(event.getOID());
						%>
						<p><b>Item list :</b> <%=listEventItem.size() %> mapping</p>
						<table class="table table-striped">
							<tr>
								<th style="width: 1%">No.</th>
								<th>Item Name</th>
								<th>SKU</th>
								<th>Category</th>
								<th>Sub Category</th>
								<th>Price</th>
								<th style="width: 10%; text-align: center"><i class="fa fa-gear"></i></th>
							</tr>
							<%//
								int no = 0;
								for (HashMap<String, String> data : listEventItem) {
									no++;
									double price = Double.valueOf(data.get("ITEM_PRICE"));
							%>
							<tr>
								<td><%=no%>.</td>
								<td><%=data.get("MATERIAL_NAME")%></td>
								<td><%=data.get("MATERIAL_SKU")%></td>
								<td><%=data.get("CATEGORY_NAME")%></td>
								<td><%=data.get("SUB_CATEGORY_NAME")%></td>
								<% if (data.get("MATERIAL_ID").equals(""+oidMaterial)) {%>
									<td><input type="text" name="price" value="<%=price%>"></td>
								<% } else {%>
									<td><%=String.format("%,.2f", price)%></td>
								<% } %>
								<td>
									<% if (!data.get("MATERIAL_ID").equals(""+oidMaterial))  { %>
										<button type="button"  class="btn btn-xs btn-default btn_edit_event text-blue" onClick="javascript:cmdEditPrice('<%=data.get("MATERIAL_ID")%>')"><i class="fa fa-pencil"></i></button>
									<% } else {  %>
										<button type="button" class="btn btn-xs btn-default btn_save_event text-green" onClick="javascript:cmdSave()"><i class="fa fa-save"></i></button>
									<% } %>
									<button type="button" class="btn btn-xs btn-default btn_delete_event text-red" data-event="<%=eventId%>" data-item="<%=data.get("MATERIAL_ID")%>"><i class="fa fa-remove"></i></button>
								</td>
							</tr>
							<%
								}
								if (listEventItem.isEmpty()) {
									out.print("<tr><td colspan='6' class='text-center'>No data</td></tr>");
								}
							%>
						</table>
						<div class="<%=msgStyle%> text-center"><%=message%></div>
						<% }%>
					</div>
				</div>
			</form>
        </section>

        <% if (eventId != 0) { %>
        <div id="modalAddItem" class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>Add item</h4>
                    </div>
                    <div class="modal-body">
                        <div class="">
                            <select multiple="" class="form-control input-sm select2category" id="category" style="width: 100%">
                                <%//
                                    Vector<Category> listCategory = PstCategory.list(0, 0, "", PstCategory.fieldNames[PstCategory.FLD_NAME] + " ASC ");
                                    for (Category c : listCategory) {
                                        out.print("<option value='" + c.getOID() + "'>" + c.getName() + "</option>");
                                    }
                                %>
                            </select>
                        </div>
						<div id="subCategory">
                            <select multiple="" class="form-control input-sm select2subcategory" id="subcategory" style="width: 100%">
                                <%//
                                    Vector<SubCategory> listSubCategory = PstSubCategory.list(0, 0, "", PstSubCategory.fieldNames[PstSubCategory.FLD_NAME] + " ASC ");
                                    for (SubCategory s : listSubCategory) {
										Category c = new Category();
										try {
											c = PstCategory.fetchExc(s.getCategoryId());
										} catch (Exception exc){}
                                        out.print("<option value='" + s.getOID() + "'>" + c.getName() + " - "+ s.getName() + "</option>");
                                    }
                                %>
                            </select>
                        </div>
                        <br>	
                        <br>
                        <form class="" id="formMapEventItem">
                            <input type="hidden" name="FRM_FIELD_DATA_FOR" value="saveEventItem">
                            <input type="hidden" name="FRM_EVENT_ID" value="<%=event.getOID()%>">
                            <div id="itemElement">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                            <th>No.</th>
                                            <th>Name</th>
                                            <th>Category</th>
											<th>Sub Category</th>
                                            <th>Price</th>
                                            <th class="text-center"><i class="fa fa-check"></i></th>
                                        </tr>
                                    </thead>
                                </table>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer" style="margin: 0px">
                        <button type="button" id="btnSaveMultiItem" class="btn btn-sm btn-primary"><i class="fa fa-check"></i> Save</button>
                        <button type="button" data-dismiss="modal" class="btn btn-sm btn-default"><i class="fa fa-close"></i> Close</button>
                    </div>
                </div>
            </div>
        </div>
        <% }%>
    </body>
</html>
