<%-- 
    Document   : CheckUserCashier
    Created on : 18 Jun 13, 13:34:38
    Author     : Wiweka
--%>


<%@ page import="com.dimata.posbo.entity.admin.AppObjInfo"%>
<%@ page import="com.dimata.qdep.system.I_SystemInfo"%>
<%
int appObjCodeKasirPaymentFrame = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_KASIR, AppObjInfo.OBJ_KASIR_PAYMENT) ;
boolean privApprovalKasirPaymentFrame = userSessionframe.checkPrivilege(AppObjInfo.composeCode(appObjCodeKasirPaymentFrame, AppObjInfo.COMMAND_VIEW));

//werehouse menu
boolean privPurchaseRequestWarehouseFrame = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_WAREHOUSE), AppObjInfo.COMMAND_VIEW));
boolean privPurchaseRequestOutletFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_REQUEST_OUTLET), AppObjInfo.COMMAND_VIEW));
boolean privApprovalPurchaseOrderFrame  =userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_PURCHASE_ORDER), AppObjInfo.COMMAND_VIEW));

boolean privApprovalReceiveFromPoFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_FROM_PO), AppObjInfo.COMMAND_VIEW));
boolean privApprovalReceiveWithoutPoFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

boolean privApprovalReturnWithPOFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITH_PO), AppObjInfo.COMMAND_VIEW));
boolean privApprovalReturnWithoutPOFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RETURN_WITHOUT_PO), AppObjInfo.COMMAND_VIEW));

boolean privApprovalRequestTransferFrame  =  userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_REQUEST_TRANSFER), AppObjInfo.COMMAND_VIEW));
boolean privApprovalTransferFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER ), AppObjInfo.COMMAND_VIEW));
boolean privApprovalTransferUnitFrame  = userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo. OBJ_MENU_HOME_TRANSFER_UNIT ), AppObjInfo.COMMAND_VIEW)); 
boolean privApprovalReceiveTransferFrame  =userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_RECEIVE_TRANSFER ), AppObjInfo.COMMAND_VIEW));

boolean privApprovalCostingFrame  =userSessionframe.checkPrivilege(AppObjInfo.composeCode(AppObjInfo.composeObjCode(AppObjInfo.G1_MENU, AppObjInfo.G2_MENU_SUBMENU_HOME, AppObjInfo.OBJ_MENU_HOME_COSTING ), AppObjInfo.COMMAND_VIEW));

%>

