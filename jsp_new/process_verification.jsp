<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.posbo.entity.admin.AppUser" %>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser" %>

<%!
    String regTemp="";
    String whereClause ="";
%>

<%
    /*regTemp = request.getParameter("VerPas");
    if (!regTemp.equals("")){
        String [] tempData = regTemp.split("\\;");
        try{
            if(tempData.length>0){
                String user_id	= tempData[0];
                whereClause=" "+PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID]+"= "+user_id+"";
                Vector listAppUser = PstAppUser.listFullObj(0, 0, whereClause, "");
                AppUser appUser = (AppUser)listAppUser.get(0);
                
                //update status user ke 1, yang berarti user tersebut sudah login
                long result = PstAppUser.updateUserStatus(appUser.getOID(), 1);
                
                String url = request.getRequestURL().toString();
                String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
                
                out.println("" + baseURL + "close.jsp");
            }
            
        }catch(Exception es){
        }
        
    }*/
    
    long user_id = 0;
    int result = 0;
    
    user_id = FRMQueryString.requestLong(request, "oid");
    result = FRMQueryString.requestInt(request, "result");
    if (user_id!=0){
        if (result==1){
            try{
                whereClause=" "+PstAppUser.fieldNames[PstAppUser.FLD_EMPLOYEE_ID]+"= '"+user_id+"'";
                Vector listAppUser = PstAppUser.listFullObj(0, 0, whereClause, "");
                AppUser appUser = (AppUser)listAppUser.get(0);
                
                //update status user ke 1, yang berarti user tersebut sudah login
                long resultX = PstAppUser.updateUserStatus(appUser.getOID(), 1);
                
            }catch(Exception es){
            
            }
        }
    }
    
%>
