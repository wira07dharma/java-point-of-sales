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
<%--!DOCTYPE html--%>
<%
if(menuUsed==3||menuUsed==4){%>
    <style type="text/css">
     table {
        background-color:white;
    }
    .listheader { COLOR: #FFFFFF; background-color:<%=tableHeader%>; FONT-SIZE: 10px; FONT-WEIGHT: bold; TEXT-ALIGN: center}
    .listgensell {  color: #000000; background-color:<%=tableCell%>}
    .listgensell {  color: #000000; background-color:<%=tableCell%>}
    .tabcontent {  background-color: <%=tableCell%>}
    .table_cell {  background-color: <%=tableCell%>}
    .listgentitle {  font-size: 11px; font-style: normal; font-weight: bold; color: #FFFFFF; background-color: <%=tableHeader%>; text-align: center}
    //.title_tbl { text-align: center; background-color:<%=tableHeader%>;}
    </style>
    <link rel="StyleSheet" href="<%=approot%>/styles/font-awesome/font-awesome.css" type="text/css" >
<%}else{%>
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
   <%if(false){%>
    <tr style="background-color: <%=header1%>">
        <td colspan="5"  style="color: <%=warnaFont%>; border-bottom: 1px solid <%=garis1%> "><!-- garis border bawah-->
            <table align="right" height="30px">
                <tr>
                    <td   align="left"><%=new Date()%> |  <a href="#" title="user"  style="color: <%=warnaFont%>">User</a> | <a href="javascript:changePicture()" title="change picture"  style="color: <%=warnaFont%>">Change Picture</a> |  <a href="<%=approot%>/styletemplate/chage_template.jsp" title="modif template"  style="color: <%=warnaFont%>">Modif Template</a> |  <a href="#" title="logout"  style="color: <%=warnaFont%>">Logout </a></td>
                </tr>
            </table>    
        </td>
    </tr>
   <%}%>
    <tr>
        <td valign="top" nowrap>
            <table style="background-color: <%=header2%>; margin-top:-2px; border-bottom: 1px solid #000;" width="100%">
                <tr >
                   <td rowspan="2" height="105px" width="170px" >
                        <%if(typeOfBusiness.equals("3")){%>
                            <img src="<%=approot%>/imgcompany/<%=pictureCompany == null || pictureCompany.getNamaPicture() == null ? "dimata_system_logo.png" : pictureCompany.getNamaPicture()%>" onclick="Javascript:window.open('<%=(approot+"/homepage_menuicon_rtc.jsp?menu=home") %>')" height="70px" width="160px" >
                        <%}else{%>
                            <img src="<%=approot%>/imgcompany/<%=pictureCompany == null || pictureCompany.getNamaPicture() == null ? "dimata_system_logo.png" : pictureCompany.getNamaPicture()%>" onclick="Javascript:window.open('<%=(approot+"/homepage_menuicon.jsp?menu=home") %>')" height="70px" width="160px" >
                        <%}%>
                    </td>
                </tr>
                <tr>
                    <td style="background-color: #f5820f;" nowrap valign="middle">
                        <%@include file="../menumain/menu_i.jsp"%>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<%}%>
