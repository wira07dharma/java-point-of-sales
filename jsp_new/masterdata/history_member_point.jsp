<%-- 
    Document   : history_member_point
    Created on : Mar 28, 2018, 10:02:31 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.common.entity.logger.PstLogSysHistory"%>
<%@page import="com.dimata.common.entity.logger.LogSysHistory"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../main/checkuser.jsp" %>

<%!    public String drawList(Vector objectClass, long oidLog) {

        ControlList ctrlist = new ControlList();

        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("table table-bordered table-hover");
        ctrlist.setTitleStyle("");
        ctrlist.setCellStyle("");
        ctrlist.setHeaderStyle("th");

        ctrlist.addHeader("No.", "1%");
        ctrlist.addHeader("Login Name", "10%");
        ctrlist.addHeader("User Action", "10%");
        ctrlist.addHeader("Update Date", "10%");
        ctrlist.addHeader("Application", "10%");
        ctrlist.addHeader("Detail", "50%");
        ctrlist.setLinkRow(0);

        Vector lstData = ctrlist.getData();

        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");

        ctrlist.reset();

        int index = -1;

        for (int i = 0; i < objectClass.size(); i++) {
            LogSysHistory logSysHistory = (LogSysHistory) objectClass.get(i);
            Vector rowx = new Vector();
            if (oidLog == logSysHistory.getOID()) {
                index = i;
            }
            rowx.add("" + (i+1));
            rowx.add("" + logSysHistory.getLogLoginName());
            rowx.add("" + logSysHistory.getLogUserAction());
            rowx.add("" + Formater.formatDate(logSysHistory.getLogUpdateDate(), "yyyy/MM/dd hh:mm:ss"));
            rowx.add("" + logSysHistory.getLogApplication());
            rowx.add("" + logSysHistory.getLogDetail());

            lstData.add(rowx);
        }
        if (objectClass.isEmpty()) {
            return "Tidak ada data history";
        }
        return ctrlist.draw(index);
    }
%>

<%//
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidMemberReg = FRMQueryString.requestLong(request, "hidden_contact_id");
    long oidLog = FRMQueryString.requestLong(request, "hidden_log_Id");
    long oidDocHistory = FRMQueryString.requestLong(request, "oidDocHistory");

    Vector listMemberPointHistory = new Vector(1, 1);
    int recordToGet = 0;
    String whereClauseHistory = PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_DOCUMENT_ID] + " = " + oidDocHistory;
    String orderClauseHistory = "" + PstLogSysHistory.fieldNames[PstLogSysHistory.FLD_LOG_UPDATE_DATE] + " DESC ";
    listMemberPointHistory = PstLogSysHistory.listPurchaseOrder(start, recordToGet, whereClauseHistory, orderClauseHistory);    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../styles/bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="../styles/dist/css/AdminLTE.css"/>
        <style>
            table {font-size: 14px}            
            .th {text-align: center; padding: 4px 8px !important; background-color: lightgray}            
        </style>
    </head>
    <body style="background-color: #eeeeee">
        <br>
        <div class="col-md-12">
            <div class="box box-primary">
                <div class="box-header with-border" style="border-color: lightgray">
                    <h3 class="box-title">History Penukaran Poin Member</h3>
                </div>

                <div class="box-body">
                    <%=drawList(listMemberPointHistory, oidLog)%>
                </div>
            </div>
        </div>
    </body>
</html>
