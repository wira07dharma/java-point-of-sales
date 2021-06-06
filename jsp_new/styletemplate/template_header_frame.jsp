<%-- 
    Document   : template_header_frame
    Created on : Jun 3, 2014, 10:16:18 AM
    Author     : sangtel6
--%>

<%-- 
    Document   : template_header
    Created on : Jul 29, 2013, 2:31:38 PM
    Author     : user
--%>

<%@page import="com.dimata.aplikasi.entity.mastertemplate.TempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.mastertemplate.PstTempDinamis"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PictureCompany"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.aplikasi.entity.picturecompany.PstPictureCompany"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../main/javainit.jsp" %>
<%--!DOCTYPE html--%>
<script>
    function changePicture() {
        window.open("<%=approot%>/styletemplate/picture_company.jsp?oidCompanyPic=" + "<%=pictureCompany != null && pictureCompany.getOID() != 0 ? pictureCompany.getOID() : 0%>",
                "upload_Image", "height=550,width=500, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }
</script>
<style type="text/css">
.listheader { COLOR: #FFFFFF; background-color:<%=tableHeader%>; FONT-SIZE: 10px; FONT-WEIGHT: bold; TEXT-ALIGN: center}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.listgensell {  color: #000000; background-color:<%=tableCell%>}
.tabcontent {  background-color: <%=tableCell%>}
.table_cell {  background-color: <%=tableCell%>}
.listgentitle {  font-size: 11px; font-style: normal; font-weight: bold; color: #FFFFFF; background-color: <%=tableHeader%>; text-align: center}
</style>
<table  border="0" cellpadding="0" width="100%" border="1">
    <tr>
        <td valign="top" nowrap>
            <table style="background-color: <%=header2%>; margin-top:-2px;" width="100%">
                <tr >
                   <td rowspan="2" height="105px" width="170px" >
                        <img src="<%=approot%>/imgcompany/<%=pictureCompany == null || pictureCompany.getNamaPicture() == null ? "dimata_system_logo.png" : pictureCompany.getNamaPicture()%>" onclick="Javascript:window.open('<%=(approot+"/prochain_home.jsp?menu=home") %>')" height="70px" width="160px" >
                   </td>
                </tr>
                <tr >
                    <td nowrap valign="middle">
                        <%
                            if (navigation != null && navigation.equalsIgnoreCase("menu_i")) {
                        %>
                        <%@include file="../menumain/menu_ii.jsp"%>
                        <%} else if (navigation != null && navigation.equalsIgnoreCase("menu_ii")) {%>
                        <%@include file="../menumain/menu_ii.jsp"%>
                        <% } else if (navigation != null && navigation.equalsIgnoreCase("menu_iii")) {%>
                        <%@include file="../menumain/menu_iii.jsp"%>
                        <%} else if (navigation != null && navigation.equalsIgnoreCase("menu_v")) {%>
                        <%@include file="../menumain/menu_v.jsp"%>
                        <% } else if (navigation != null && navigation.equalsIgnoreCase("menu_vi")) {%>
                        <%@include file="../menumain/menu_vi.jsp"%>
                        <% } else {%>
                        <%@include file="../menumain/menu_vii.jsp"%>
                        <% }%>
                    </td>

                </tr>
            </table>
        </td>
    </tr>
   
</table>
