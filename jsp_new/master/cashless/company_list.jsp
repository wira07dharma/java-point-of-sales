<%-- 
    Document   : company_list
    Created on : Jun 7, 2019, 9:56:21 AM
    Author     : Dimata 007
--%>

<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstPayGeneral"%>
<%@page import="com.dimata.posbo.entity.masterdata.PayGeneral"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%//
    Vector<PayGeneral> listCompany = PstPayGeneral.list(0, 0, "", PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME]);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>CASHLESS COMPANY LIST</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .table thead tr th {white-space: nowrap}
        </style>
        <script>
            $(document).ready(function() {
                $('.btn').click(function() {
                    $(this).html("<i class='fa fa-spinner fa-pulse'></i> Wait...").attr({disabled:true});
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
                    <h4 class="box-title">Company List</h4>
                </div>
                <div class="box-body">
                    <a href="cashless_company.jsp" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i> Add Company</a>
                    <p></p>
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover">
                            <thead>
                                <tr class="label-default">
                                    <th>No.</th>
                                    <th>Name</th>
                                    <th>Address</th>
                                    <th>City</th>
                                    <th>Postal Code</th>
                                    <th>Business Type</th>
                                    <th>Telephone</th>
                                    <th>Fax</th>
                                    <th>Leader Name</th>
                                    <th>Position</th>
                                    <th>Work Days</th>
                                    <th class="text-center"><i class="fa fa-gear"></i></th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    int no = 0;
                                    for (PayGeneral pg : listCompany) {
                                        no++;
                                %>
                                <tr>
                                    <td><%= no %>.</td>
                                    <td><%= pg.getCompanyName() %></td>
                                    <td><%= pg.getCompAddress() %></td>
                                    <td><%= pg.getCity() %></td>
                                    <td><%= pg.getZipCode() %></td>
                                    <td><%= pg.getBussinessType() %></td>
                                    <td><%= pg.getTel() %></td>
                                    <td><%= pg.getFax() %></td>
                                    <td><%= pg.getLeaderName() %></td>
                                    <td><%= pg.getLeaderPosition() %></td>
                                    <td><%= pg.getWorkDays() %></td>
                                    <td class="text-center"><a href="cashless_company.jsp?command=<%=Command.EDIT%>&company_id=<%=pg.getOID()%>" class="btn btn-xs btn-warning"><i class="fa fa-pencil"></i></a></td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>
    </body>
</html>
