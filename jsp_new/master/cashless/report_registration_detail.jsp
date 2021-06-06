<%-- 
    Document   : report_registration
    Created on : Jul 6, 2019, 9:38:26 AM
    Author     : Dimata 007
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<%int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER);%>
<%@ include file = "../../main/checkuser.jsp" %>

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
    int command = FRMQueryString.requestCommand(request);
	long userOid = FRMQueryString.requestLong(request, "user");
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
			function cmdExport()
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.action="excel/summary_detail.jsp";
					document.frmsrc.submit();
				}
		</script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Detail Cashier Transaction</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Report</li>
                <li>Detail Cashier Transaction</li>
            </ol>
        </section>

        <section class="content">
            <div class="box box-warning">
                <div class="box-header with-border">
                    <h4 class="box-title">Search</h4>
                </div>
                <div class="box-body">
					<form name='frmsrc'>
						<input type="hidden" name='user' value="<%=userOid%>">
						<input type="hidden" name='event' value="<%=eventOid%>">
						<input type="hidden" name='command'>
						<a href="javascript:cmdExport()" class="btn btn-sm btn-primary"><i class="fa fa-file-excel-o"></i> Excel</a>
					</form>
					
					<% if (command == Command.LIST){ %>
                    <table class="table">
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

									%>
									<tr>
										<td>1</td>
										<td><%=rs.getString(1)%></td>
										
										<% if (rs.getInt(2)==0 || (rs.getInt(2)==2 && rs.getInt(5)==1)){ %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<% if ((rs.getInt(2)==2 && rs.getInt(5)==0)){ %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<% if (rs.getInt(2)==1){ %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										
										<% if (rs.getInt(4)==1){ %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<% if (rs.getInt(4)==2){ %>
										<td><%=String.format("%,.0f", rs.getDouble(3))%></td>
										<% } else { %>
										<td>0</td>
										<% } %>
										
										<% if (rs.getInt(4)==3){ %>
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
                        
                    </table>
					<% } %>
                </div>
            </div>
        </section>
    </body>
</html>
