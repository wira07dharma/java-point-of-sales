<%
    com.dimata.common.entity.finger.DeviceFinger deviceFinger = new com.dimata.common.entity.finger.DeviceFinger();
    com.dimata.posbo.session.masterdata.SessMacAddress sessMacAddress = new com.dimata.posbo.session.masterdata.SessMacAddress();
    com.dimata.common.entity.finger.PstDeviceFinger pstDeviceFinger = new com.dimata.common.entity.finger.PstDeviceFinger();
    String whereClause="";
    String macAddress="";
    
    //TAMBAHAN MENDADAK
    String verPas = request.getParameter("vc");    
    String snn = verPas;
    
    macAddress = sessMacAddress.getMacAddress();
    whereClause = " "+pstDeviceFinger.fieldNames[pstDeviceFinger.FLD_VC]+"='"+verPas+"'";
    java.util.Vector listDeviceFinger = pstDeviceFinger.list(0, 0, whereClause, "");
    deviceFinger = (com.dimata.common.entity.finger.DeviceFinger)listDeviceFinger.get(0);
    
    //String ac="CSY509AEBD4EC48ABD0EA71T";
    //String sn="EZ00J001632";
    
    String ac = deviceFinger.getAc();
    String sn = deviceFinger.getSn();
    
    out.print(""+ac+""+sn+"");
%>