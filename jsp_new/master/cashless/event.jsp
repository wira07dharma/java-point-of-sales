<%-- 
    Document   : event.jsp
    Created on : Jun 11, 2019, 9:21:05 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.posbo.entity.masterdata.Company"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlEvent"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    long eventId = FRMQueryString.requestLong(request, "event_id");
	long companyId = FRMQueryString.requestLong(request, "company_id");

    CtrlEvent ctrlEvent = new CtrlEvent(request);
    int error = ctrlEvent.action(command, eventId);
    String message = ctrlEvent.getMessage();
    Event event = ctrlEvent.getEvent();
    FrmEvent frmEvent = ctrlEvent.getForm();
    
    int backToList = 0;
    if (error == 0 && (command == Command.SAVE || command == Command.DELETE)) {
        backToList = 1;
    }

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>Event</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .table thead tr th {white-space: nowrap}
            .table-condensed {font-size: 14px}
        </style>
        <script>
            if ("<%=backToList%>" === "1") {
                alert("<%=message%>");
                window.location = "event_list.jsp";
            }
            
            $(document).ready(function () {
                $('.date_time_picker').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd hh:ii'
                    //minView: 2
                });
                
                $('#btnSaveEvent').click(function () {
                    var btn = $(this).html();
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
                    $('#formEvent').submit();
                    //$(this).html(btn).removeAttr('disabled');
                });
                
                $('#btnBackEvent').click(function () {
                    var btn = $(this).html();
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
                    window.location = "event_list.jsp";
                    //$(this).html(btn).removeAttr('disabled');
                });
                
                $('#btnDeleteEvent').click(function () {
                    var btn = $(this).html();
                    if (confirm("Delete this event ?")) {
                        $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled: true});
                        window.location = "event.jsp?command=<%=Command.DELETE%>&event_id=<%=eventId%>";
                    }
                    $(this).html(btn).removeAttr('disabled');
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
                <% if (eventId > 1) { %>
                <ul class="nav nav-tabs">
                    <li class="active text-bold"><a href="event.jsp?command=<%=Command.EDIT%>&tab_view=1&event_id=<%=eventId%>">Event Setup</a></li>
					<li><a href="ticket.jsp?tab_view=1&event_id=<%=eventId%>">Ticket</a></li>
                    <li><a href="mapping_event_item.jsp?tab_view=1&event_id=<%=eventId%>">Map Item</a></li>
                    <li><a href="mapping_event_bar.jsp?tab_view=1&event_id=<%=eventId%>">Map Bar</a></li>
                    <li><a href="map_event_user.jsp?tab_view=1&event_id=<%=eventId%>">Map User</a></li>
                </ul>
                <% } else { %>
                <div class="box-header with-border">
                    <h4 class="box-title">Event Setup</h4>
                </div>
                <% } %>
                <div class="box-body">
                    <form class="form-horizontal" id="formEvent">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="event_id" value="<%=event.getOID()%>">
                        
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Event Code</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_EVENT_CODE]%>" value="<%=event.getEventCode()%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_EVENT_CODE)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Event Title</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_EVENT_TITLE]%>" value="<%=event.getEventTitle()%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_EVENT_TITLE)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Description</label>
                            <div class="col-sm-4">
                                <textarea class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_DESCRIPTION]%>"><%=event.getDescription()%></textarea>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_DESCRIPTION)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Event Start Time</label>
                            <div class="col-sm-4">
                                <%
                                    String startTime = (event.getEventDatetime() == null) ? "" : Formater.formatDate(event.getEventDatetime(), "yyyy-MM-dd HH:mm:ss");
                                %>
                                <input type="text" class="form-control input-sm date_time_picker" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_EVENT_DATETIME]%>" value="<%=startTime%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_EVENT_DATETIME)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Event End Time</label>
                            <div class="col-sm-4">
                                <%
                                    String endTime = (event.getEventEndDatetime() == null) ? "" : Formater.formatDate(event.getEventEndDatetime(), "yyyy-MM-dd HH:mm:ss");
                                %>
                                <input type="text" class="form-control input-sm date_time_picker" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_EVENT_END_DATETIME]%>" value="<%=endTime%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_EVENT_END_DATETIME)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Price Type</label>
                            <div class="col-sm-4">
                                <select class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_PRICE_TYPE_ID]%>">
                                    <%
                                        Vector<PriceType> listPriceType = PstPriceType.list(0, 0, "", PstPriceType.fieldNames[PstPriceType.FLD_NAME]);
                                        for (PriceType pt : listPriceType) {
                                    %>
                                    <option <%=(event.getPriceTypeId()== pt.getOID() ? "selected":"")%> value="<%=pt.getOID() %>"><%=pt.getName() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_PRICE_TYPE_ID)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Tag Deposit</label>
                            <div class="col-sm-4">
                                <input type="text" class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_TAG_DEPOSIT]%>" value="<%=event.getTagDeposit()%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_TAG_DEPOSIT)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Currency</label>
                            <div class="col-sm-4">
                                <select class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_CURRENCY_TYPE_ID]%>">
                                    <%
                                        Vector<CurrencyType> listCurrency = PstCurrencyType.list(0, 0, "", "");
                                        for (CurrencyType ct : listCurrency) {
                                    %>
                                    <option <%=(event.getCurrencyTypeId()== ct.getOID() ? "selected":"")%> value="<%= ct.getOID() %>"><%=ct.getCode()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_CURRENCY_TYPE_ID)%>
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2 control-label">Enable Refund</label>
                            <div class="col-sm-4">
                                <input type="radio" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_REFUND_ENABLE]%>" value="0"> No 
								<input type="radio" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_REFUND_ENABLE]%>" value="1"> Yes
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2 control-label">Refund Limit Date</label>
                            <div class="col-sm-4">
                                <%
                                    String refundLimit = (event.getLimitRefund()== null) ? "" : Formater.formatDate(event.getLimitRefund(), "yyyy-MM-dd HH:mm:ss");
                                %>
                                <input type="text" class="form-control input-sm date_time_picker" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_LIMIT_REFUND_DATE]%>" value="<%=refundLimit%>">
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_LIMIT_REFUND_DATE)%>
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2 control-label">Refund Mode</label>
                            <div class="col-sm-4">
                                <select class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_REFUND_MODE]%>">
                                    <%
										int idx = 0;
                                        for (String c : PstEvent.strMode) {
                                    %>
                                    <option <%=(event.getRefundMode()== idx ? "selected":"")%> value="<%= idx %>"><%=c%></option>
                                    <%
										idx++;
                                        }
                                    %>
                                </select>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_COMPANY_ID)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">Company</label>
                            <div class="col-sm-4">
								<select class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_COMPANY_ID]%>" id="companyId">
									<option>Select</option>
                                    <%
                                        Vector<Company> listCompany = PstCompany.list(0, 0, "", PstCompany.fieldNames[PstCompany.FLD_COMPANY_NAME]);
                                        for (Company c : listCompany) {
                                    %>
                                    <option <%=(event.getCompanyId() == c.getOID() ? "selected":"")%> value="<%= c.getOID() %>"><%=c.getCompanyName() %></option>
                                    <%
                                        }
                                    %>
                                </select>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_COMPANY_ID)%>
                            </div>
                        </div>
						<div class="form-group">
                            <label class="col-sm-2 control-label">Location</label>
                            <div class="col-sm-4">
                                <select class="form-control input-sm" name="<%=FrmEvent.fieldNames[FrmEvent.FRM_FIELD_LOCATION_ID]%>">
									<option>Select</option>
                                    <%
                                        Vector<Location> listLocation = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                        for (Location l : listLocation) {
                                    %>
                                    <option <%=(event.getLocationId()== l.getOID() ? "selected":"")%> value="<%= l.getOID() %>"><%=l.getName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                                <%=frmEvent.getErrorMsg(FrmEvent.FRM_FIELD_LOCATION_ID)%>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-4">
                                <button type="button" id="btnSaveEvent" class="btn btn-sm btn-primary"><i class="fa fa-check"></i> Save Event</button>
                                <button type="button" id="btnBackEvent" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i> Back</button>
                                <% if(event.getOID() != 0) {%>
                                <button type="button" id="btnDeleteEvent" class="btn btn-sm btn-danger pull-right"><i class="fa fa-remove"></i> Delete Event</button>
                                <% } %>
                            </div>
                        </div>
                    </form>
                    <%
                        String msgStyle = (error == 0) ? "label-success" : "label-danger";
                    %>
                    <div class="text-center <%=msgStyle%>"><%=message%></div>
                </div>
            </div>
        </section>
    </body>
</html>
