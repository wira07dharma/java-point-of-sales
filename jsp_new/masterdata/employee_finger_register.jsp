<%@ page import = "java.util.*,
                   com.dimata.qdep.form.FRMQueryString,
                   com.dimata.util.Command
                   " %>

<%@page import="com.dimata.posbo.form.admin.CtrlFingerPatern"%>
<%@page import="com.dimata.posbo.form.admin.FrmFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.PstFingerPatern"%>
<%@page import="com.dimata.posbo.entity.admin.FingerPatern"%>

<%
   /*String regTemp = FRMQueryString.requestString(request,"RegTemp"); 
   
   if (!regTemp.equals("")){
        String [] data = regTemp.split(";");
       
        String user_id	= data[2];
        String regTemps = data[3];
        
        String Tamp[] = user_id.split("A");
        Long employeeOID = Long.parseLong(Tamp[0]);
        int fingerType = Integer.parseInt(Tamp[1]);
        
        FingerPatern fingerPatern = new FingerPatern();
        fingerPatern.setEmployeeId(employeeOID);
        fingerPatern.setFingerType(fingerType);
        fingerPatern.setFingerPatern(regTemps);
        
        CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);
        int errCode = ctrlFingerPatern.action(Command.SAVE, fingerPatern);
        if (errCode==0){
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
                
            out.println("" + baseURL + "close.jsp");
        }
        
        
   }*/
   
    long employeeOID = 0;
    int fingerType =0;
    String regTemp ="";
    
    regTemp = FRMQueryString.requestString(request,"patern"); 
    if (regTemp.length()>0){
        employeeOID = FRMQueryString.requestLong(request,"oid");
        fingerType = FRMQueryString.requestInt(request, "typefinger");
        
        FingerPatern fingerPatern = new FingerPatern();
        fingerPatern.setEmployeeId(employeeOID);
        fingerPatern.setFingerType(fingerType);
        fingerPatern.setFingerPatern(regTemp);
        
        CtrlFingerPatern ctrlFingerPatern = new CtrlFingerPatern(request);
        int errCode = ctrlFingerPatern.action(Command.SAVE, fingerPatern);
        if (errCode==0){
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
                
            //out.println("1");
        }else{
            //out.println("0");
        }
        
    }
   
%>