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

            String sql = "SELECT SUM(TOPUP_AMOUNT) FROM  pos_cls_registration WHERE user_id ="+userId
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
    int command = FRMQueryString.requestCommand(request);
	long userOid = FRMQueryString.requestLong(request, "user");
	long eventOid = FRMQueryString.requestLong(request, "event");
    String dateFrom = FRMQueryString.requestString(request, "FRM_DATE_FROM");
    String dateTo = FRMQueryString.requestString(request, "FRM_DATE_TO");

    if (command == Command.NONE) {
        dateFrom = Formater.formatDate(new Date(), "yyyy-MM-dd");
        dateTo = Formater.formatDate(new Date(), "yyyy-MM-dd");
    }
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
					document.frmsrc.action="report_registration.jsp";
					document.frmsrc.submit();
				}
			function cmdExport()
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.action="excel/summary_cashier_transaction.jsp";
					document.frmsrc.submit();
				}
			function cmdDetail(oid)
				{
					document.frmsrc.command.value="<%=Command.LIST%>";
					document.frmsrc.user.value=oid;
					document.frmsrc.action="report_registration_detail.jsp";
					document.frmsrc.submit();
				}
		</script>
    </head>
    <body style="background-color: #eeeeee">
        <section class="content-header">
            <h1>Summary Cashier Transaction</h1>
            <ol class="breadcrumb">
                <li>Cashless</li>
                <li>Report</li>
                <li>Summary Cashier Transaction</li>
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
                            <label class="col-sm-1">User</label>
                            <div class="col-sm-3">
                                <select class="form-control input-sm" name="user">
                                    <option value="0">All User</option>
									<%//
										Vector<AppUser> listUser = PstAppUser.listFullObj(0, 0, "", "");
										for (AppUser e : listUser) {
											out.print("<option " + " value='" + e.getOID() + "'>" + e.getLoginId()+ "</option>");
										}
									%>
                                </select>
                            </div>
                        </div>
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
<!--                        <div class="form-group">
                            <label class="col-sm-1">Date</label>
                            <div class="col-sm-3">
                                <div class="input-group">
                                    <input type="text" placeholder="Date from" class="form-control input-sm" name="FRM_DATE_FROM" value="<%=dateFrom%>">
                                    <span class="input-group-addon">To</span>
                                    <input type="text" placeholder="Date to" class="form-control input-sm" name="FRM_DATE_TO" value="<%=dateTo%>">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-1 col-sm-3">
                                <button type="submit" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Find</button>
                            </div>
                        </div>-->
							<a href="javascript:cmdSearch()" class="btn btn-sm btn-primary"><i class="fa fa-search"></i> Search</a>&nbsp; &nbsp;
						<a href="javascript:cmdExport()" class="btn btn-sm btn-primary"><i class="fa fa-file-excel-o"></i> Excel</a>
                    
					 
					<% if (command == Command.LIST){ %>
                    <table class="table">
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

								%>
								<tr>
									<td><%=(i+1)%></td>
									<td><a href="javascript:cmdDetail('<%=appUser.getOID()%>')"><%=appUser.getLoginId()%></a></td>
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
                        
                    </table>
					<% } %>
					</form>
                </div>
            </div>
        </section>
    </body>
</html>
