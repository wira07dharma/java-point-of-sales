<%@ page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page import="com.dimata.qdep.system.I_SystemInfo"%>
<%@page import="com.dimata.posbo.session.admin.SessUserSession"%>

<%
if(userSession==null || isLoggedIn==false) {
	response.sendRedirect(approot + "/index.jsp");
}

try {
	if(!(userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW)))){
		%>
		<script language="javascript">
			document.location.href ="<%=approot%>/sys_info.jsp?ic=<%= I_SystemInfo.HAVE_NOPRIV%>";
		</script>
		<%
	}
}
catch(Exception exc) {
	System.out.println(exc.toString());
	%>
	<script language="javascript">
            window.top.location.href ="<%=approot%>/index.jsp%>";
	</script>
	<%
}

boolean privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
boolean privUpdate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
boolean privDelete = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
boolean privStart = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_START));
boolean privStop = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_STOP));

int appObjCodeKasirPayment = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT) ;
boolean privApprovalKasirPayment = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPayment, AppObjInfo.COMMAND_VIEW));

//werehouse menu
boolean privPurchaseRequestWarehouse = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_WAREHOUSE), AppObjInfo.COMMAND_VIEW));
boolean privPurchaseRequestOutlet = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_OUTLET), AppObjInfo.COMMAND_VIEW));
boolean privApprovalPurchaseOrder =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_ORDER), AppObjInfo.COMMAND_VIEW));

boolean privApprovalReceiveFromPo = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_FROM_PO), AppObjInfo.COMMAND_VIEW));
boolean privApprovalReceiveWithoutPo = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

boolean privApprovalReturnWithPO= userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITH_PO), AppObjInfo.COMMAND_VIEW));
boolean privApprovalReturnWithoutPO = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

boolean privApprovalRequestTransfer =  userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_REQUEST_TRANSFER), AppObjInfo.COMMAND_VIEW));
boolean privApprovalTransfer = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER ), AppObjInfo.COMMAND_VIEW));
boolean privApprovalTransferUnit = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER_UNIT ), AppObjInfo.COMMAND_VIEW)); 
boolean privApprovalReceiveTransfer =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_TRANSFER ), AppObjInfo.COMMAND_VIEW));

boolean privApprovalCosting =userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_COSTING ), AppObjInfo.COMMAND_VIEW));

//added by dewok 2018-04-24 for litama
boolean privViewProductionEmas = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER_PRODUCTION, AppObjInfo.OBJ_TRANSFER_PRODUCTION_FOR_GOLD ), AppObjInfo.COMMAND_VIEW));
boolean privViewReportIncentive = userSession.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_REPORT_INCENTIVE ), AppObjInfo.COMMAND_VIEW));

//out.println(privAdd+" "+privUpdate+" "+privDelete+" "+privStart+" "+privStop);
%>
