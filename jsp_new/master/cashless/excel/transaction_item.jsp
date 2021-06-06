<%-- 
    Document   : transaction_item
    Created on : Jul 6, 2019, 9:59:18 PM
    Author     : IanRizky
--%>
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
<%@page contentType="application/x-msexcel" pageEncoding="UTF-8" %>
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
	response.setHeader("Content-Disposition", "attachment; filename=summary_transaction_item.xls");
    int command = FRMQueryString.requestCommand(request);
	long eventOid = FRMQueryString.requestLong(request, "event");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <table class="table">
			<tr>
				<td colspan="7" style="text-align: center"><b><u>MENU SOLD REPORT</u></b></td>
			</tr>
			<tr>
				<td colspan="7">
					<table border='1'>
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
											<td><%=mix+"%"%></td>
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
											<td><%=mix+"%"%></td>
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
									<td><%=mixTotal%>%</td>
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
				</td>
			</tr>
		</table>
    </body>
</html>
