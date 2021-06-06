<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>
<%@ include file = "main/javainit.jsp" %>
<%	
switch(userGroupNewStatus){
	case PstAppUser.GROUP_STORE :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}
		else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}
		break;
			
	case PstAppUser.GROUP_ADMINISTRATOR :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}
		else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}
		break;

	case PstAppUser.GROUP_SUPERVISOR :
		//response.sendRedirect("homepage_supervisor.jsp");
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}
		else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}
		break;
    
    case PstAppUser.GROUP_CASHIER:
        //response.sendRedirect("homepage_supervisor.jsp");
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}
		else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}
        break;

	case PstAppUser.GROUP_WAREHOUSE :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}
		else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}
		break;		 

	default : 
		response.sendRedirect("login.jsp");				
		break;
}
%>

