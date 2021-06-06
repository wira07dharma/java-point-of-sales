<%-- 
    Document   : index_jsp
    Created on : Nov 4, 2015, 11:20:02 AM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.license.LicenseProduct"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstCompany"%>
<%@page import="com.dimata.common.entity.system.PstSystemProperty"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    int pos_integ = 0;
    try {
        String designMat = String.valueOf(PstSystemProperty.getValueByName("PROCHAIN_LOGIN_TYPE"));
        pos_integ = Integer.parseInt(designMat);
    } catch (Exception e) {
        pos_integ = 0;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body bgcolor="#FFFFFF" text="#000000">
        <%
            boolean isValidKey = false;
            String freeLicense = String.valueOf(PstSystemProperty.getValueByName("FREE_LICENSE"));
            if (freeLicense.equals("1")) {
                isValidKey = true;
            } else {
                String txtChiperText = "";
                try {
                    txtChiperText = PstCompany.getLicenseKey();
                } catch (Exception e) {
                }
                isValidKey = LicenseProduct.btDekripActionPerformed(txtChiperText);
            }
        %>
        <script language="javascript">
            <%if (isValidKey) {%>
                <%if (pos_integ == 1) {%>
                    window.location = "login_finger.jsp";
                <%} else {%>
                    window.location = "login.jsp";
                <%}%>
            <%} else {%>
                window.location = "license.jsp";
            <%}%>
        </script>
        <iframe style="height:1px" frameborder=0 width=1></iframe>
    </body>
</html>
