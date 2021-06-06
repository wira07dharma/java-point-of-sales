<%-- 
    Document   : summary_detail
    Created on : Jul 6, 2019, 7:55:58 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.posbo.entity.admin.AppUser"%>
<%@page import="com.dimata.util.Formater"%>
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
<!DOCTYPE html>
<%!
	
	public double getAmountTopUp(long userId, long eventId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up tp"
				+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
				+ " WHERE tp.user_id ="+userId+" AND reg.EVENT_OID="+eventId;



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

	public double getAmountRefund(long userId, long eventId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(REFUND_AMOUNT) FROM  pos_cls_refund tp"
				+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
				+ " WHERE tp.user_id ="+userId+" AND reg.EVENT_OID="+eventId;



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

	public double getPaymentAmount(long userId, long eventId, int paymethodId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up tp"
				+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
				+ " WHERE tp.user_id ="+userId+" AND reg.EVENT_OID="+eventId+" AND tp.PAY_METHOD_ID = "+paymethodId;



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

	public double getPaymentAmountRefund(long userId, long eventId, int paymethodId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(REFUND_AMOUNT) FROM  pos_cls_refund tp"
				+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
				+ " WHERE tp.user_id ="+userId+" AND reg.EVENT_OID="+eventId+" AND tp.PAYMENT_SYSTEM_ID = "+paymethodId;



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
	response.setHeader("Content-Disposition", "attachment; filename=summary_cashier_detail.xls");
    int command = FRMQueryString.requestCommand(request);
	long userOid = FRMQueryString.requestLong(request, "user");
	long eventOid = FRMQueryString.requestLong(request, "event");
	
	Event event = new Event();
	try {
		event = PstEvent.fetchExc(eventOid);
	} catch (Exception exc){}
	
	AppUser appUser = new AppUser();
	try {
		appUser = PstAppUser.fetch(userOid);
	} catch (Exception exc){
		
	}
    
%>
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>
        <table class="table">
			<tr>
				<td colspan="9" style="text-align: center"><b><u>DETAIL CASHIER TRANSACTION</u></b></td>
			</tr>
			<tr>
				<td>Username</td>
				<td colspan="8"><%= appUser.getLoginId()%></td>
			</tr>
			<tr>
				<td>Event</td>
				<td><%= event.getEventTitle() %></td>
				<td colspan="7"></td>
			</tr>
			<tr>
				<td>Date</td>
				<td><%=Formater.formatDate(event.getEventDatetime(), "dd-MM")%></td>
				<td colspan="7"></td>
			</tr>
			<tr>
				<td colspan="9">
					<table border='1'>
                        <tr>
                            <th>Pax</th>
                            <th>RFID No.</th>
                            <th>Top Up</th>
                            <th>Min Spend</th>
                            <th>Refund</th>
                            <th>Subtotal</th>
							<th>Cash</th>
							<th>Credit Card</th>
							<th>C/L</th>
                        </tr>
                        
                        <%
							DBResultSet dbrs = null;
							double amount = 0;
							double totalTopUp = 0;
							double minSpendTotal = 0;
							double refundTotal = 0;
							double subTotalTotal = 0;
							double cashTotal = 0;
							double ccTotal = 0;
							double clTotal = 0;
							try {

								String sql = "(SELECT reg.TAG_ID,0 AS TYPE, tp.TOPUP_AMOUNT, tp.PAY_METHOD_ID, 1 FROM pos_cls_top_up tp"
									+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
									+ " WHERE tp.user_id ="+userOid+" AND reg.EVENT_OID="+eventOid+") UNION "
									+ " (SELECT reg.TAG_ID, 1 AS TYPE, -tp.REFUND_AMOUNT, tp.PAYMENT_SYSTEM_ID, 1 FROM pos_cls_refund tp"
									+ " INNER JOIN pos_cls_registration reg ON tp.ID_CUSTOMER = reg.MEMBER_ID"
									+ " WHERE tp.user_id ="+userOid+" AND reg.EVENT_OID="+eventOid+") UNION "
									+ " (SELECT TAG_ID, 2 AS TYPE, TOP_UP_AMOUNT, PAYMENT_SYSTEM_ID, STATUS FROM pos_cls_registration"
									+ " WHERE user_id ="+userOid+" )";


								dbrs = DBHandler.execQueryResult(sql);

								ResultSet rs = dbrs.getResultSet();

								while (rs.next()) {
									subTotalTotal = subTotalTotal + rs.getDouble(3);
									%>
									<tr>
										<td>1</td>
										<td><%=rs.getString(1)%></td>
										
										<% if (rs.getInt(2)==0 || (rs.getInt(2)==2 && rs.getInt(5)==1)){
											totalTopUp = totalTopUp + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<% if ((rs.getInt(2)==2 && rs.getInt(5)==0)){ 
											minSpendTotal = minSpendTotal + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										
										<% if (rs.getInt(2)==1){ 
											refundTotal = refundTotal + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% if (rs.getInt(4)==1){ 
											cashTotal = cashTotal + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										<% if (rs.getInt(4)==2){ 
											ccTotal = ccTotal + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										<% if (rs.getInt(4)==3){ 
											clTotal = clTotal + rs.getDouble(3);
										%>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
									</tr>
									
									<%

								}

								rs.close();

							} catch (Exception e) {

								System.out.println(e);

							} finally {

								DBResultSet.close(dbrs);

							}
							
							

                        %>
                        <tr>
								<td></td>
								<td></td>
								<td><%=String.format("%,.0f", totalTopUp)%></td>
								<td><%=String.format("%,.0f", minSpendTotal)%></td>
								<td><%=String.format("%,.0f", refundTotal)%></td>
								<td><%=String.format("%,.0f", subTotalTotal)%></td>
								<td><%=String.format("%,.0f", cashTotal)%></td>
								<td><%=String.format("%,.0f", ccTotal)%></td>
								<td><%=String.format("%,.0f", clTotal)%></td>
							</tr>
                    </table>
				</td>
			</tr>
		</table>
    </body>
</html>
