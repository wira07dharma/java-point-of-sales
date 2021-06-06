<%-- 
    Document   : nemenu_iii
    Created on : Aug 27, 2013, 10:45:06 AM
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String spliturlMenu[] = request.getServletPath().toString().trim().split("/");

    String urlMenu = null;
    if (spliturlMenu != null && spliturlMenu.length > 0) {
        // for(int i=0;i<spliturl.length;i++) {
        // if (spliturl[i].equalsIgnoreCase("home.jsp")) {
        int idxLnght = spliturlMenu.length - 1;
        try {
            urlMenu = spliturlMenu[idxLnght];
        } catch (Exception exc) {
        }
    }
    String homeActive = "";
    String employeeActive = "";
    String trainingActive = "";
    String reportsActive = "";
    String canteenActive = "";
    String clinicActive = "";
    String lockerActive = "";
    String masterDataActive = "";
    String systemActive = "";
    String payrollSetupActive = "";
    String overtimeActive = "";
    String payrollProcessActive = "";
    String helpActive = "";

    if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("home.jsp")) {
        homeActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("employee.jsp")) {
        employeeActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("training.jsp")) {
        trainingActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("reports.jsp")) {
        reportsActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("canteen.jsp")) {
        canteenActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("clinic.jsp")) {
        clinicActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("locker.jsp")) {
        lockerActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("master_data.jsp")) {
        masterDataActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("system.jsp")) {
        systemActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("payroll_setup.jsp")) {
        payrollSetupActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("overtime.jsp")) {
        overtimeActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("payroll_process.jsp")) {
        payrollProcessActive = "class=\"current\"";
    } else if (urlMenu != null && urlMenu.length() > 0 && urlMenu.equalsIgnoreCase("help.jsp")) {
        helpActive = "class=\"current\"";
    }
%>
<!DOCTYPE html>
<!--<link href="../menustylesheet/sample_menu_iii.css" rel="stylesheet" type="text/css">-->
<style type="text/css">
    /*#tabs30{position:relative;height:29px;font-size:12px; overflow: auto;font-size:12px; width: auto; text-transform:uppercase;background-color: <%//=bgMenu%>; font-family: Arial;}
    #tabs30 ul{list-style-type:none; padding: 0; margin: 0;width:auto;}
    #tabs30 ul li{display:inline;}
    #tabs30 ul li a span{display: block; float: left;color:<%//=fontMenu%>;text-decoration:none; padding: 5px 10px 5px 10px; height: 18px;}
    #tabs30 ul li a:hover span{display:block;cursor:pointer; font-weight: bold;color:<%//=bgMenu%>; background-color: <%//=hoverMenu%>}
    #tabs30 ul li a.current span{display:block; cursor:pointer; font-weight: bold;color:#fff;}*/


    #tabs30{position:relative;height:39px;font-size:12px; overflow: auto;font-size:12px; width: auto; text-transform:uppercase;background-color: <%=bgMenu%>; font-family: Arial;}
    #tabs30 ul{list-style-type:none; padding: 0; margin: 0;width:auto;}
    #tabs30 ul li{display:inline;}
    #tabs30 ul li a{display: block; float: left;color:<%=fontMenu%>;text-decoration:none; height: 39px;}
    #tabs30 ul li a span{display: block; float: left;color:<%=fontMenu%>;text-decoration:none; padding: 10px; height: 19px;}
    #tabs30 ul li a:hover, #tabs30 ul li a.current{display:block;cursor:pointer; font-weight: bold;color:<%=fontMenu%>; background-color: <%=hoverMenu%>;}
    #tabs30 ul li a:hover span, #tabs30 ul li a.current span{display:block;cursor:pointer; font-weight: bold;color:<%=fontMenu%>; height: 39px;}
</style>

<div id="tabs30">
    <ul>
        <li><a href="<%=approot%>/menuaplikasi/home.jsp" <%=homeActive%>><span>Home</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/employee.jsp" <%=employeeActive%>><span>Employee</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/training.jsp" <%=trainingActive%>><span>Training</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/reports.jsp" <%=reportsActive%>><span>Reports</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/canteen.jsp" <%=canteenActive%>><span>Canteen</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/clinic.jsp" <%=clinicActive%>><span>Clinic</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/locker.jsp" <%=lockerActive%>><span>Locker</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/master_data.jsp" <%=masterDataActive%>><span>Master Data</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/system.jsp" <%=systemActive%>><span>System</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/payroll_setup.jsp" <%=payrollSetupActive%>><span>Payroll Setup</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/overtime.jsp" <%=overtimeActive%>><span>Overtime</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/payroll_process.jsp" <%=payrollProcessActive%>><span>Payroll Process</span></a></li>
        <li><a href="<%=approot%>/menuaplikasi/help.jsp" <%=helpActive%>><span>Help</span></a></li>
    </ul>
</div>