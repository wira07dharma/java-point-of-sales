<%  
    com.dimata.posbo.session.masterdata.SessMacAddress sessMacAddress = new com.dimata.posbo.session.masterdata.SessMacAddress();
    String whereClause="";
    String macAddress="";
    
    macAddress = sessMacAddress.getMacAddress();
   
    out.print(""+macAddress+"");
%>