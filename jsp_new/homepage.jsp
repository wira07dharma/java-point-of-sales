e<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>
<%@ include file = "main/javainit.jsp" %>
<%	
switch(userGroupNewStatus){
	case PstAppUser.GROUP_STORE :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
                }else if(menuUsed == MENU_ICON){
                    if(typeOfBusiness.equals("3")){//distribusi (RTC)
                        response.sendRedirect("homepage_menuicon_rtc.jsp?menu=home");
                    }else{
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                            response.sendRedirect("homepage_menuicon.jsp?menu=home");
                        }else{
                            response.sendRedirect("homepage_admin_trans.jsp");
                        }
                    }
                }

		break;
			
	case PstAppUser.GROUP_ADMINISTRATOR :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}else if(menuUsed == MENU_ICON){
                    if(typeOfBusiness.equals("3")){//distribusi (RTC)
                        response.sendRedirect("homepage_menuicon_rtc.jsp?menu=home");
                    }else{
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                            response.sendRedirect("homepage_menuicon.jsp?menu=home");
                        }else{
                            response.sendRedirect("homepage_admin_trans.jsp");
                        }
                    }
                }
		break;

	case PstAppUser.GROUP_SUPERVISOR :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}else if(menuUsed == MENU_ICON){
                       if(typeOfBusiness.equals("3")){//distribusi (RTC)
                        response.sendRedirect("homepage_menuicon_rtc.jsp?menu=home");
                    }else{
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                            response.sendRedirect("homepage_menuicon.jsp?menu=home");
                        }else{
                            response.sendRedirect("homepage_admin_trans.jsp");
                        }
                    }
                }
		break;
    
        case PstAppUser.GROUP_CASHIER:
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("cashierweb/open_shift.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("cashierweb/open_shift.jsp");
		}else if(menuUsed == MENU_ICON){
                        response.sendRedirect("cashierweb/open_shift.jsp");
                }
                break;

	case PstAppUser.GROUP_WAREHOUSE :
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("homepage_admin.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("homepage_admin_trans.jsp");
		}else if(menuUsed == MENU_ICON){
                    if(typeOfBusiness.equals("3")){//distribusi (RTC)
                        response.sendRedirect("homepage_menuicon_rtc.jsp?menu=home");
                    }else{
                        if(MODUS_TRANSFER == MODUS_TRANSFER_SERVER){
                            response.sendRedirect("homepage_menuicon.jsp?menu=home");
                        }else{
                            response.sendRedirect("homepage_admin_trans.jsp");
                        }
                    }
                }
        break;

        case PstAppUser.GROUP_SALES_ADMIN:
		if(menuUsed == MENU_DEFAULT) {
			response.sendRedirect("cashierweb/open_shift.jsp");
		}else if(menuUsed == MENU_PER_TRANS) {
			response.sendRedirect("cashierweb/open_shift.jsp");
		}else if(menuUsed == MENU_ICON){
                        response.sendRedirect("cashierweb/open_shift.jsp");
                }
        break;

	default : 
		response.sendRedirect("login_new.jsp");				
		break;
}
%>

