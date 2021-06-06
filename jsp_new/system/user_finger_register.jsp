<%@ page import = "java.util.*,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command,
                   com.dimata.posbo.entity.admin.PstAppUser,
                   com.dimata.posbo.form.admin.CtrlAppUser,
                   com.dimata.posbo.entity.admin.AppUser" %>
<%@ include file = "../main/javainit.jsp" %>
<%
   String regTemp = FRMQueryString.requestString(request,"RegTemp"); 

   if (regTemp!=""){
        String [] data = regTemp.split(";");
        String vStamp 	= data[0];
        String sn 	= data[1];
        String user_id	= data[2];
        String regTemps = data[3];
        
        int result=0;
        CtrlAppUser ctrlAppUser = new CtrlAppUser(request);
        result = ctrlAppUser.action(Command.RECONFIRM, Long.parseLong(user_id), request);
        
        String destUrl = ""+baseURL+"system/finger.jsp";
        
        out.print(destUrl);
   }
%>
