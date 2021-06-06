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
					document.frmsrc.action="sales_and_payment.jsp";
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
					document.frmsrc.action="excel/sales_payment.jsp";
					document.frmsrc.submit();
				}
		</script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Sales and Payment Report</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Report</li>
                <li>Sales and Payment Report</li>
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
											out.print("<option " + (eventOid == e.getOID() ? "selected" : "") + " value='" + e.getOID() + "'>" + e.getEventTitle() + "</option>");
										}
									%>
                                </select>
                            </div>
                        </div>
						<a href="javascript:cmdSearch()" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Search</a>
						&nbsp; &nbsp;
						<a href="javascript:cmdExport()" class="btn btn-sm btn-primary"><i class="fa fa-file-excel-o"></i> Excel</a>
                    
					 
					<% if (command == Command.LIST){ %>
                    <table class="table">
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
					<% } %>
					</form>
                </div>
            </div>
        </section>
    </body>
</html>
