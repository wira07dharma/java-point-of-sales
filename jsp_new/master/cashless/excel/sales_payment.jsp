<%-- 
    Document   : sales_payment
    Created on : Jul 6, 2019, 10:04:48 PM
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

	public double getAmountTopUpRegist(){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOP_UP_AMOUNT) FROM  pos_cls_registration ";
			



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


	public double getAmountTopUp(){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up";



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

	public double getAmountTag(){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TAG_DEPOSIT) FROM  pos_cls_registration";



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

	public double getAmountRefund(){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(REFUND_AMOUNT) FROM  pos_cls_refund";



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

	public int countReg(){
		DBResultSet dbrs = null;
		int amount = 0;
        try {

            String sql = "SELECT COUNT(TAG_ID) FROM  pos_cls_registration";



            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                amount = rs.getInt(1);

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

	public double getPaymentAmount(int paymethodId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_top_up WHERE PAY_METHOD_ID = "+paymethodId;



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

	public double getPaymentAmountRegist(int paymethodId){
		DBResultSet dbrs = null;
		double amount = 0;
        try {

            String sql = "SELECT SUM(TOP_UP_AMOUNT) FROM  pos_cls_registration WHERE PAYMENT_SYSTEM_ID = "+paymethodId;



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
	response.setHeader("Content-Disposition", "attachment; filename=sales_and_payment.xls");
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
				<td colspan="7" style="text-align: center"><b><u>SALES SUMMARY REPORT</u></b></td>
			</tr>
			<tr>
				<td colspan="7">
					<table>
						<tr>
							<td colspan="3"><b>SALES:</b></td>
						</tr>
                        <%
							DBResultSet dbrs = null;
							double amount = 0;
							double sum=getQty();
							int count = countReg();
							double total = 0;
							double totalAll = 0;
							try {

								String sql = "SELECT cat.NAME, SUM(item.QTY), SUM(item.PRICE_PER_UNIT*item.QTY)"
									+ " FROM pos_cls_transaction_item item"
									+ " INNER JOIN pos_cls_transaction tr "
									+ " ON item.TRANSACTION_ID = tr.TRANSACTION_ID"
									+ " INNER JOIN pos_material mat"
									+ " ON item.MATERIAL_ID = mat.MATERIAL_ID"
									+ " INNER JOIN pos_category cat"
									+ " ON mat.CATEGORY_ID = cat.CATEGORY_ID"
									+ " GROUP BY cat.NAME"
									+ " ORDER BY cat.NAME";


								
								dbrs = DBHandler.execQueryResult(sql);

								ResultSet rs = dbrs.getResultSet();

								
								while (rs.next()) {
										total = total + rs.getDouble(3);
										%>
										<tr>
											<td><%=rs.getString(1)%></td>
											<td><%=rs.getInt(2)%></td>
											<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										</tr>
										<%
								}


								rs.close();

							} catch (Exception e) {

								System.out.println(e);

							} finally {

								DBResultSet.close(dbrs);

							}
							
							
							double totalUnconsumed = 0;
							if ((getAmountTopUp()+getAmountTopUpRegist()-getAmountRefund()-total-getAmountTag()) > 0){
								totalUnconsumed = getAmountTopUp()+getAmountTopUpRegist()-getAmountRefund()-total-getAmountTag();
							}
							totalAll = total + totalUnconsumed + getAmountTag();
                        %>
						<tr>
							<td>Unconsumed</td>
							<td><%=count%></td>
							<td><%=String.format("%,.0f", totalUnconsumed)%></td>
						</tr>
						<tr>
							<td>Tag Deposit</td>
							<td><%=count%></td>
							<td><%=String.format("%,.0f", getAmountTag())%></td>
						</tr>
						<tr>
							<td colspan="2">Total</td>
							<td><%=String.format("%,.0f", totalAll)%></td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="3"><b>Payment:</b></td>
						</tr>
						<tr>
							<td colspan="2">Cash</td>
							<td><%=String.format("%,.0f", getPaymentAmount(1)+getPaymentAmountRegist(1))%></td>
						</tr>
						<tr>
							<td colspan="2">C.Card</td>
							<td><%=String.format("%,.0f", getPaymentAmount(2)+getPaymentAmountRegist(3))%></td>
						</tr>
						<tr>
							<td colspan="2">City Ledger</td>
							<td><%=String.format("%,.0f", getPaymentAmount(3)+getPaymentAmountRegist(3))%></td>
						</tr>
						<tr>
							<td colspan="2">Refund</td>
							<td>-<%=String.format("%,.0f", getAmountRefund())%></td>
						</tr>
						<% 
							double totalPayment = getPaymentAmount(1)+getPaymentAmountRegist(1)+getPaymentAmount(2)+getPaymentAmountRegist(2)+getPaymentAmount(3)+getPaymentAmountRegist(3)-getAmountRefund();
						%>
						<tr>
							<td colspan="2">Total Payment</td>
							<td><%=String.format("%,.0f", totalPayment)%></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
    </body>
</html>
