<%-- 
    Document   : checkusercashieronline
    Created on : Aug 15, 2014, 2:52:29 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.pos.entity.balance.CashCashier"%>
<%@page import="com.dimata.pos.entity.balance.PstCashCashier"%>
<%@page import="java.util.Vector"%>
<%
//Cek jika user sudah pernah melakukan opening closing langsung masuk ke halaman cashier
Vector vctCashCashier=new Vector(1,1);
long oidCashCashierOnline=0;
long oidCashierShifId=0;
String whereClosingBalance=""+PstCashCashier.fieldNames[PstCashCashier.FLD_APPUSER_ID]+"="+userId+" AND "+PstCashCashier.fieldNames[PstCashCashier.FLD_SPVCLOSE_OID]+"=1";
vctCashCashier=PstCashCashier.list(0,0,whereClosingBalance,"");
if ((vctCashCashier!=null)&&(vctCashCashier.size()>0)){
        CashCashier cashCashier = (CashCashier)vctCashCashier.get(0);
        oidCashCashierOnline=cashCashier.getOID();
        oidCashierShifId=cashCashier.getShiftId();
}else{
    response.sendRedirect("m_open_shift.jsp?typeListCashier=1");
}
%>
