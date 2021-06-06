<%-- 
    Document   : homepage_mobile
    Created on : Aug 6, 2014, 10:48:15 AM
    Author     : dimata005
--%>

e<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>
<%@ include file = "main/javainit.jsp" %>
<%	
switch(userGroupNewStatus){
	case PstAppUser.GROUP_STORE :
		response.sendRedirect("homepage_menumobile.jsp?menu=home");
		break;
			
	case PstAppUser.GROUP_ADMINISTRATOR :
		response.sendRedirect("homepage_menumobile.jsp?menu=home");
		break;

	case PstAppUser.GROUP_SUPERVISOR :
		response.sendRedirect("homepage_menumobile.jsp?menu=home");
		break;
    
        case PstAppUser.GROUP_CASHIER:
		response.sendRedirect("homepage_menumobile.jsp?menu=home");
                break;

	case PstAppUser.GROUP_WAREHOUSE :
		response.sendRedirect("homepage_menumobile.jsp?menu=home");
        break;
        
	default : 
		response.sendRedirect("login_new.jsp");				
		break;
}
%>


