<%-- 
    Document   : getdata
    Created on : May 3, 2014, 11:31:05 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.ajax.DummyDB"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%
    DummyDB db = new DummyDB();
    String query = request.getParameter("q");
    List<String> countries = db.getData(query);
    Iterator<String> iterator = countries.iterator();
    while(iterator.hasNext()) {
        String country = (String)iterator.next();
        out.println(country);
    }
%>