<%@page import="java.util.Hashtable"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMinimumStock"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%
        Hashtable searcName = SessMinimumStock.getMaterialRequestSearchByHash();
        
        String countries[] = { };
        
	String query = (String)request.getParameter("q");
	response.setHeader("Content-Type", "text/html");
	int cnt=1;
	for(int i=0;i<searcName.size();i++)
	{
                if(searcName.get(""+i).toString().toUpperCase().contains(query.toUpperCase()))
                {
			out.print((String)searcName.get(""+i) +"\n");
			if(cnt>=10)
				break;
			cnt++;
		}
	}
%>