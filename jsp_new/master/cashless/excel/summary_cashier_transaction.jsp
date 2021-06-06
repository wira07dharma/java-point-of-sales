<%-- 
    Document   : summary_cashier_transaction
    Created on : Jul 6, 2019, 7:20:53 PM
    Author     : IanRizky
--%>
<%@page import="com.dimata.util.Formater"%>
<%@page import="com.dimata.posbo.entity.admin.AppUser"%>
<%@page import="java.util.Vector"%>
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

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up WHERE user_id ="+userId;
			



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

	public double getAmountTopUpRegist(long userId, long eventId, int status){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOP_UP_AMOUNT) FROM  pos_cls_registration WHERE user_id ="+userId+" AND STATUS ="+status;
			



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

            String sql = "SELECT SUM(REFUND_AMOUNT) FROM  pos_cls_refund WHERE user_id ="+userId;



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

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up WHERE user_id ="+userId
				+" AND PAY_METHOD_ID = "+paymethodId;



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

	public double getPaymentAmountRegist(long userId, long eventId, int paymethodId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOP_UP_AMOUNT) FROM  pos_cls_registration WHERE user_id ="+userId
				+" AND PAYMENT_SYSTEM_ID = "+paymethodId;



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

            String sql = "SELECT SUM(REFUND_AMOUNT) FROM  pos_cls_refund WHERE user_id ="+userId
					+" AND PAYMENT_SYSTEM_ID = "+paymethodId;



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

<%!    public String getPaymentType(int paymentType) {
        switch (paymentType) {
            case 0:
                return "Credit Card";
            case 1:
                return "Cash";
            case 2:
                return "Debit Card";
            case 3:
                return "Return";
            case 4:
                return "Voucher";
            case 5:
                return "Voucher Complimentary";
            case 6:
                return "FOC";
            case 7:
                return "Cash Employee";
            default:
                return "Unknown";
        }
    }
%>
<%//
	response.setHeader("Content-Disposition", "attachment; filename=summary_cashier.xls");
    int command = FRMQueryString.requestCommand(request);
	long userOid = FRMQueryString.requestLong(request, "user");
	long eventOid = FRMQueryString.requestLong(request, "event");
	
	Event event = new Event();
	try {
		event = PstEvent.fetchExc(eventOid);
	} catch (Exception exc){}

%>
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>
        <% if (command == Command.LIST){ %>
			<table class="table">
				<tr>
					<td colspan="9" style="text-align: center"><b><u>SUMMARY CASHIER TRANSACTION</u></b></td>
				</tr>
				<tr>
					<td>Username</td>
					<td colspan="8">All</td>
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
								<th>No</th>
								<th>Cashier Name</th>
								<th>Top Up</th>
								<th>Min Spend</th>
								<th>Refund</th>
								<th>Subtotal</th>
								<th>Cash</th>
								<th>Credit Card</th>
								<th>C/L</th>
							</tr>

							<%
								String whereClause = "";
								if (userOid>0){
									whereClause = PstAppUser.fieldNames[PstAppUser.FLD_USER_ID]+"="+userOid;
								}
								Vector listSelUser = PstAppUser.listFullObj(0, 0, whereClause, "");
								double totalTopUp = 0;
								double minSpendTotal = 0;
								double refundTotal = 0;
								double subTotalTotal = 0;
								double cashTotal = 0;
								double ccTotal = 0;
								double clTotal = 0;
								for (int i=0; i < listSelUser.size();i++){ 
									AppUser appUser = (AppUser) listSelUser.get(i);

									double topUp = getAmountTopUp(appUser.getOID(), eventOid) + getAmountTopUpRegist(appUser.getOID(), eventOid, 1) ;
								double minSpend = getAmountTopUpRegist(appUser.getOID(), eventOid, 0);
								double refund = getAmountRefund(appUser.getOID(), eventOid);
								double subTotal = topUp + minSpend - refund;
								double cash = getPaymentAmount(appUser.getOID(), eventOid,1) + getPaymentAmountRegist(appUser.getOID(), eventOid,1);
								double cc = getPaymentAmount(appUser.getOID(), eventOid,2) + getPaymentAmountRegist(appUser.getOID(), eventOid,2);
								double cl = getPaymentAmount(appUser.getOID(), eventOid,3) + getPaymentAmountRegist(appUser.getOID(), eventOid,3);
								double cashRef = getPaymentAmountRefund(appUser.getOID(), eventOid,1);
								double ccRef = getPaymentAmountRefund(appUser.getOID(), eventOid,2);
								double clRef = getPaymentAmountRefund(appUser.getOID(), eventOid,3);

									totalTopUp = totalTopUp + topUp;
									minSpendTotal = minSpendTotal + minSpend;
									refundTotal = refundTotal + refund;
									subTotalTotal = subTotalTotal + subTotal;
									cashTotal = cashTotal + cash - cashRef;
									ccTotal = ccTotal + cc - ccRef;
									clTotal = clTotal + cl - clRef;						

									%>
									<tr>
										<td style="text-align: right"><%=(i+1)%></td>
										<td><%=appUser.getLoginId()%></td>
										<td><%=String.format("%,.0f", topUp)%></td>
										<td><%=String.format("%,.0f", minSpend)%></td>
										<td>-<%=String.format("%,.0f", refund)%></td>
										<td><%=String.format("%,.0f", subTotal)%></td>
										<td><%=String.format("%,.0f", cash-cashRef)%></td>
										<td><%=String.format("%,.0f", cc-ccRef)%></td>
										<td><%=String.format("%,.0f", cl-clRef)%></td>
									</tr>
									<%
								}


							%>
							<tr>
								<td></td>
								<td></td>
								<td><%=String.format("%,.0f", totalTopUp)%></td>
								<td><%=String.format("%,.0f", minSpendTotal)%></td>
								<td>-<%=String.format("%,.0f", refundTotal)%></td>
								<td><%=String.format("%,.0f", subTotalTotal)%></td>
								<td><%=String.format("%,.0f", cashTotal)%></td>
								<td><%=String.format("%,.0f", ccTotal)%></td>
								<td><%=String.format("%,.0f", clTotal)%></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<% } %>
    </body>
</html>
