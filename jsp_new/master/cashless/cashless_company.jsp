<%-- 
    Document   : cashless_company
    Created on : Jun 7, 2019, 9:20:17 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.gui.jsp.ControlDate"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.form.masterdata.FrmPayGeneral"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@page import="com.dimata.posbo.entity.masterdata.PayGeneral"%>
<%@page import="com.dimata.posbo.form.masterdata.CtrlPayGeneral"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    int command = FRMQueryString.requestCommand(request);
    long companyOid = FRMQueryString.requestLong(request, "company_id");

    CtrlPayGeneral ctrlPayGeneral = new CtrlPayGeneral(request);
    int error = ctrlPayGeneral.action(command, companyOid);
    PayGeneral payGeneral = ctrlPayGeneral.getPayGeneral();
    FrmPayGeneral frmPayGeneral = ctrlPayGeneral.getForm();
    String message = ctrlPayGeneral.getMessage();

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>CASHLESS COMPANY</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .datetimepicker th, td {font-size: 14px}
            .errfont {font-size: 12px; color: #dd4b39}
            .input_date {width: 32%; display: inline}
        </style>
        <script>
            $(document).ready(function () {
                $('.date_picker').datetimepicker({
                    autoclose: true,
                    todayBtn: true,
                    format: 'yyyy-mm-dd',
                    minView: 2
                });

                $('.btn').click(function() {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled:true});
                });
                
                $('#btnSave').click(function () {
                    $('#formCompany').submit();
                });
            });
        </script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Company</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Master</li>
                <li>Company</li>
            </ol>
        </section>
        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h4 class="box-title">Company Setup</h4>
                </div>
                <div class="box-body">
                    <form class="form-horizontal row" id="formCompany">
                        <input type="hidden" name="command" value="<%=Command.SAVE%>">
                        <input type="hidden" name="company_id" value="<%=payGeneral.getOID()%>">

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Company Name</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_COMPANY_NAME]%>" value="<%=payGeneral.getCompanyName()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_COMPANY_NAME)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Address</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_COMP_ADDRESS]%>" value="<%=payGeneral.getCompAddress()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_COMP_ADDRESS)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">City / Postal Code</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_CITY]%>" value="<%=payGeneral.getCity()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_CITY)%>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" required="" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_ZIP_CODE]%>" value="<%=payGeneral.getZipCode()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_ZIP_CODE)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Business Type</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_BUSSINESS_TYPE]%>" value="<%=payGeneral.getBussinessType()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_BUSSINESS_TYPE)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Tax Office</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_TAX_OFFICE]%>" value="<%=payGeneral.getTaxOffice()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_TAX_OFFICE)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Work Days</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_WORK_DAYS]%>" value="<%=payGeneral.getWorkDays()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_WORK_DAYS)%>
                                </div>
                            </div>
                        </div>

                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-sm-4 control-label">NPWP / NPPKP</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_REG_TAX_NR]%>" value="<%=payGeneral.getRegTaxNr()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_REG_TAX_NR)%>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_REG_TAX_BUS_NR]%>" value="<%=payGeneral.getRegTaxBusNr()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_REG_TAX_BUS_NR)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Establishment Date</label>
                                <div class="col-sm-8">
                                    <%=ControlDate.drawDateWithStyle(frmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_REG_TAX_DATE], payGeneral.getRegTaxDate()==null?new Date():payGeneral.getRegTaxDate(), 1, -35,"form-control input-sm input_date")%>
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_REG_TAX_DATE)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Telephone / Fax</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_TEL]%>" value="<%=payGeneral.getTel()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_TEL)%>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_FAX]%>" value="<%=payGeneral.getFax()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_FAX)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Leader Name & Position</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_LEADER_NAME]%>" value="<%=payGeneral.getLeaderName()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_LEADER_NAME)%>
                                </div>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_LEADER_POSITION]%>" value="<%=payGeneral.getLeaderPosition()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_LEADER_POSITION)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Tax Report Location</label>
                                <div class="col-sm-8">
                                    <input type="text" class="form-control input-sm" name="<%=FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_TAX_REP_LOCATION]%>" value="<%=payGeneral.getTaxRepLocation()%>">
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_TAX_REP_LOCATION)%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-4 control-label">Local Currency</label>
                                <div class="col-sm-8">
                                    <%
                                        Vector curr_value = new Vector(1, 1);
                                        Vector curr_key = new Vector(1, 1);
                                        Vector listCurr = PstCurrencyType.list(0, 0, "", PstCurrencyType.fieldNames[PstCurrencyType.FLD_TAB_INDEX]);
                                        for (int i = 0; i < listCurr.size(); i++) {
                                            CurrencyType curr = (CurrencyType) listCurr.get(i);
                                            curr_key.add(curr.getName());
                                            curr_value.add(String.valueOf(curr.getCode()));
                                        }
                                    %>
                                    <%=ControlCombo.draw(FrmPayGeneral.fieldNames[FrmPayGeneral.FRM_FIELD_LOCAL_CUR_CODE], "form-control", null, "" + payGeneral.getLocalCurCode(), curr_value, curr_key)%>
                                    <%=frmPayGeneral.getErrorMsg(FrmPayGeneral.FRM_FIELD_LOCAL_CUR_CODE)%>
                                </div>
                            </div>
                        </div>
                    </form>
                    <%
                        String msgStyle = error == 0 ? "label-success":"label-danger";
                    %>
                    <div class="text-center <%=msgStyle%>"><%=message%></div>
                </div>
                <div class="box-footer">
                    <button type="button" id="btnSave" class="btn btn-sm btn-primary"><i class="fa fa-check"></i> Save</button>
                    <a href="company_list.jsp" class="btn btn-sm btn-primary"><i class="fa fa-undo"></i> Back To Company List</a>
                    <% if (payGeneral.getOID() != 0) { %>
                    <a href="cashless_company.jsp?command=<%=Command.DELETE%>&company_id=<%=payGeneral.getOID()%>" class="btn btn-sm btn-danger pull-right"><i class="fa fa-remove"></i> Delete Company</a>
                    <% } %>
                </div>
            </div>
        </section>
    </body>
</html>
