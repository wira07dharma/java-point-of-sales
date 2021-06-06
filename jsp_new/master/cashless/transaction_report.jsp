<%-- 
    Document   : report_registration
    Created on : Jul 6, 2019, 9:38:26 AM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.dimata.posbo.db.DBHandler"%>
<%@page import="com.dimata.posbo.db.DBResultSet"%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstEvent"%>
<%@page import="com.dimata.posbo.entity.masterdata.Event"%>
<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@page import="com.dimata.common.entity.payment.PaymentSystem"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>


<%!    public double getQty(){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(item.QTY)"
									+ " FROM pos_cls_transaction_item item"
									+ " INNER JOIN pos_cls_transaction tr "
									+ " ON item.TRANSACTION_ID = tr.TRANSACTION_ID"
									+ " INNER JOIN pos_material mat"
									+ " ON item.MATERIAL_ID = mat.MATERIAL_ID"
									+ " INNER JOIN pos_sub_category sub"
									+ " ON mat.SUB_CATEGORY_ID = sub.SUB_CATEGORY_ID"
									+ " ORDER BY mat.SUB_CATEGORY_ID, mat.NAME";



            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                amount = rs.getDouble(1);

            }

            rs.close();

            return amount;



        } catch (Exception e) {

            System.out.println(e);

        } finally {

            DBResultSet.close(dbrs);

        }
		return amount;
	}	
%>

<%//
    int command = FRMQueryString.requestCommand(request);
	long eventOid = FRMQueryString.requestLong(request, "event");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@ include file = "../../styles/plugin_component.jsp" %>
        <title>Registration Report</title>
        <style>
            .box .box-header, .box-footer {border-color: lightgray}
            .table {font-size: 12px}
            .table thead tr th {white-space: nowrap}
        </style>
		<script language="JavaScript">
			function cmdSearch()
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.action="transaction_report.jsp";
					document.frmsrc.submit();
				}
			function cmdDetail(oid)
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.user.value=oid;
					document.frmsrc.action="report_registration_detail.jsp";
					document.frmsrc.submit();
				}
			function cmdExport()
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.action="excel/transaction_item.jsp";
					document.frmsrc.submit();
				}
		</script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Summary Transaction Item</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Report</li>
                <li>Summary Transaction Item</li>
            </ol>
        </section>

        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h4 class="box-title">Search</h4>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" name="frmsrc" method="post">
						<input type="hidden" name="command">
                        <div class="form-group">
                            <label class="col-sm-1">Event</label>
                            <div class="col-sm-3">
                                <select class="form-control input-sm" name="event">
									<option value="0">- Select Event -</option>
									<%//
										Vector<Event> listEvent = PstEvent.list(0, 0, PstEvent.fieldNames[PstEvent.FLD_COMPANY_ID] + " = '" + userSession.getAppUser().getCompanyId() + "'", PstEvent.fieldNames[PstEvent.FLD_EVENT_TITLE] + " ASC ");
										for (Event e : listEvent) {
											out.print("<option " + (eventOid == e.getOID() ? "selected" : "") +  " value='" + e.getOID() + "'>" + e.getEventTitle() + "</option>");
										}
									%>
                                </select>
                            </div>
                        </div>
						<a href="javascript:cmdSearch()" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Search</a>&nbsp; &nbsp;
						<a href="javascript:cmdExport()" class="btn btn-sm btn-primary"><i class="fa fa-file-excel-o"></i> Excel</a>
                    
					 
					<% if (command == Command.LIST){ %>
                    <table class="table">
                        <tr>
                            <th>Menu Item</th>
                            <th>Number Sold</th>
                            <th>Menu Mix</th>
                            <th>Selling Price</th>
                            <th>Menu Revenue</th>
                            <th>Cost Percentage</th>
							<th>Menu Profit</th>
                        </tr>
                        
                        <%
							DBResultSet dbrs = null;
							double amount = 0;
							double sum=getQty();
							try {

								String sql = "SELECT sub.NAME, mat.NAME, SUM(item.QTY), item.PRICE_PER_UNIT, mat.MATERIAL_ID, mat.SKU"
									+ " FROM pos_cls_transaction_item item"
									+ " INNER JOIN pos_cls_transaction tr "
									+ " ON item.TRANSACTION_ID = tr.TRANSACTION_ID"
									+ " INNER JOIN pos_material mat"
									+ " ON item.MATERIAL_ID = mat.MATERIAL_ID"
									+ " INNER JOIN pos_sub_category sub"
									+ " ON mat.SUB_CATEGORY_ID = sub.SUB_CATEGORY_ID"
									+ " GROUP BY mat.SKU"
									+ " ORDER BY mat.SUB_CATEGORY_ID, mat.NAME";


								
								dbrs = DBHandler.execQueryResult(sql);

								ResultSet rs = dbrs.getResultSet();

								String name = "";
								double total = 0 ;
								double mixTotal = 0;
								double revTotal = 0;
								double profitTotal = 0;
								while (rs.next()) {
									
									mixTotal = mixTotal + ((rs.getDouble(3)/sum) * 100);
									total = total + rs.getInt(3);
									revTotal = revTotal + (rs.getInt(3) * rs.getDouble(4));
									profitTotal = profitTotal + (rs.getInt(3) * rs.getDouble(4)) - ((rs.getInt(3) * rs.getDouble(4)) * (25.0/100.0) );
									
									Material mat = new Material();
									try {
										mat = PstMaterial.fetchExc(rs.getLong(5));
									} catch (Exception exc){}
									
									double currBuyPrice = mat.getCurrBuyPrice();
									double margin = 0;
									if (currBuyPrice>0){
										margin = (rs.getDouble(4) - currBuyPrice) * 100 / currBuyPrice;
									}
									
									if (!name.equals(rs.getString(1))){
										name = rs.getString(1);
										double mix = (rs.getDouble(3)/sum) * 100;
										
										%>
										<tr>
											<td><b><u><%=name%></u></b></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
										<tr>
											<td><%=rs.getString(2)%></td>
											<td><%=rs.getInt(3)%></td>
											<td><%=String.format("%,.2f", mix)+"%"%></td>
											<td><%=String.format("%,.0f", rs.getDouble(4))%></td>
											<td><%=String.format("%,.0f", rs.getInt(3) * rs.getDouble(4))%></td>
											<td>25%</td>
											<td><%=String.format("%,.0f", (rs.getInt(3) * rs.getDouble(4)) - ((rs.getInt(3) * rs.getDouble(4)) * (25.0/100.0) ))%></td>
										</tr>
										<%
									} else {
										double mix = (rs.getInt(3)/sum) * 100;
										%>
										<tr>
											<td><%=rs.getString(2)%></td>
											<td><%=rs.getInt(3)%></td>
											<td><%=String.format("%,.2f", mix)+"%"%></td>
											<td><%=String.format("%,.0f", rs.getDouble(4))%></td>
											<td><%=String.format("%,.0f", rs.getInt(3) * rs.getDouble(4))%></td>
											<td>25%</td>
											<td><%=String.format("%,.0f", (rs.getInt(3) * rs.getDouble(4)) - ((rs.getInt(3) * rs.getDouble(4)) * (25.0/100.0) ))%></td>
										</tr>
										<%
									}

								}
								%>
								<tr>
									<td><b>Total</b></td>
									<td><%=total%></td>
									<td><%=String.format("%,.2f", mixTotal)%>%</td>
									<td></td>
									<td><%=String.format("%,.0f", revTotal)%></td>
									<td></td>
									<td><%=String.format("%,.0f", profitTotal)%></td>
								</tr>
								<%


								rs.close();

							} catch (Exception e) {

								System.out.println(e);

							} finally {

								DBResultSet.close(dbrs);

							}
							
							

                        %>
                        
                    </table>
					<% } %>
					</form>
                </div>
            </div>
        </section>
    </body>
</html>
