<%-- 
    Document   : list_component
    Created on : Sep 22, 2016, 11:24:53 AM
    Author     : dimata005
--%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMinimumStock"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%
        Hashtable searcName = SessMinimumStock.getMaterialComponentRequestSearchByHash();
	String query = (String)request.getParameter("q");
	response.setHeader("Content-Type", "text/html");
	int cnt=1;
	for(int i=0;i<searcName.size();i++)
	{
		if(searcName.get(""+i).toString().toUpperCase().startsWith(query.toUpperCase()))
		{
			out.print((String)searcName.get(""+i) +"\n");
			if(cnt>=10)
				break;
			cnt++;
		}
	}
%>