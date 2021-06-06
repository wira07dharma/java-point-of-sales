<%@ page import="com.dimata.util.Command,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.entity.contact.*"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_SUPPLIER); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Upload Excel</title>
<script language="javascript">
function cmdBack(){
	document.frmsupplier.command.value="<%=Command.BACK%>";
	document.frmsupplier.action="<%=approot%>/home.jsp";
	document.frmsupplier.submit();
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css"> 
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../../main/mnmain.jsp" %>
            <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Supplier &gt; Import Data Supplier<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <form name="frmsupplier" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectSupplierExist = new Vector();
					String[] arrKode = request.getParameterValues("kode");
					String[] arrType = request.getParameterValues("type");
					String[] arrNama = request.getParameterValues("nama");
					String[] arrAlamat = request.getParameterValues("alamat");
					String[] arrFax = request.getParameterValues("fax");
					String[] arrNegara = request.getParameterValues("negara");
					String[] arrPropinsi = request.getParameterValues("propinsi");
					String[] arrKota = request.getParameterValues("kota");
					String[] arrKontak = request.getParameterValues("kontak");
                        String[] arrTelpon = request.getParameterValues("telpon");
					String strError = "";

					boolean transferSuccess = false;
					Vector verr = new Vector();
                    long oidContactClass = PstContactClass.getOidClassType(PstContactClass.CONTACT_TYPE_SUPPLIER);
					for (int i=0; i<arrKode.length; i++){
                        String strKode = String.valueOf(arrKode[i]);
                        ContactList objContactList = new ContactList();
                        objContactList.setContactCode(arrKode[i]);
                        objContactList.setContactType(0);
                        objContactList.setCompName(arrNama[i]);
                        objContactList.setBussAddress(arrAlamat[i]);
                        objContactList.setFax(arrFax[i]);
                        objContactList.setCountry(arrNegara[i]);
                        objContactList.setProvince(arrPropinsi[i]);
                        objContactList.setTown(arrKota[i]);
                        objContactList.setPersonName(arrKontak[i]);
                        objContactList.setTelpNr(arrTelpon[i]);
                        try{
                            long oidContactList = PstContactList.cekCodeContact(strKode);
                            if(oidContactList != 0){
                                objContactList.setOID(oidContactList);
                                vectSupplierExist.add(objContactList);
                            }else{
                                long oidContact = PstContactList.insertExc(objContactList);
                                System.out.println("==>>> **** Sukses contact");
                                ContactClassAssign contactClassAssign = new ContactClassAssign();
                                contactClassAssign.setContactClassId(oidContactClass);
                                contactClassAssign.setContactId(oidContact);
                                try{
                                    PstContactClassAssign.insertExc(contactClassAssign);
                                    System.out.println("==>>> **** Sukses insert contact type class");
                                }catch(Exception e){}
                                transferSuccess = true;
                            }
                        }catch (Exception e){
                            System.out.println("Insert error : " + e);
                        }
                    }

					if(transferSuccess){
						out.println("Upload data supplier sukses ...");
						out.println("<br><br><div class=\"command\"><a href=\"javascript:cmdBack()\">Kembali ke Menu Utama</a></div>");
					}else{
						out.println("<font color=\"#FF0000\">Upload data supplier gagal ...</font>");
					}
					%>
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vectSupplierExist.size()>0){%><b>Data supplier yang tidak bisa di upload : </b><br>
								<%
								out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" width=\"50%\">");
								out.println("<tr><td class=\"listgentitle\">No</td><td class=\"listgentitle\">Kode</td><td class=\"listgentitle\">Nama</td></tr>");
								for(int k=0;k < vectSupplierExist.size();k++){
									ContactList objContactList = (ContactList)vectSupplierExist.get(k);
									out.println("<tr><td class=\"listgensell\">"+(k+1)+"</td><td class=\"listgensell\">"+objContactList.getContactCode()+"</td><td class=\"listgensell\">"+objContactList.getCompName()+"</td></tr>");
								}
								out.println("</table>");
							%>
						<%}%></td>
                      </tr>
                    </table>
                  </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
            <%@ include file = "../../main/footer.jsp" %>
            <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
