<%-- 
    Document   : cek_mac
    Created on : Dec 7, 2015, 11:56:32 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%
    com.dimata.common.entity.finger.DeviceFinger deviceFinger = new com.dimata.common.entity.finger.DeviceFinger();
    com.dimata.posbo.session.masterdata.SessMacAddress sessMacAddress = new com.dimata.posbo.session.masterdata.SessMacAddress();
    com.dimata.common.entity.finger.PstDeviceFinger pstDeviceFinger = new com.dimata.common.entity.finger.PstDeviceFinger();
    String whereClause="";
    String macAddress="";
    
    macAddress = sessMacAddress.getMacAddress();
    
    int count = SessMaterial.getCountMaterial(whereClause, 0, 0);
%>
<%=count%>