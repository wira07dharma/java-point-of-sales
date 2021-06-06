<%@ page import="com.dimata.util.Command,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.entity.contact.*,
                 com.dimata.posbo.entity.masterdata.PstMemberReg,
                 com.dimata.posbo.entity.masterdata.MemberReg,
                 com.dimata.harisma.entity.masterdata.PstReligion,
                 com.dimata.harisma.entity.masterdata.Religion,
                 com.dimata.posbo.entity.masterdata.PstMemberGroup,
                 com.dimata.posbo.entity.masterdata.MemberGroup,
				 com.dimata.posbo.entity.masterdata.MemberRegistrationHistory,
				 com.dimata.posbo.entity.masterdata.PstMemberRegistrationHistory"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_MEMBER); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Upload Excel</title>
<script language="javascript">
function cmdBack(){
	document.frmmember.command.value="<%=Command.BACK%>";
	document.frmmember.action="<%=approot%>/home.jsp";
	document.frmmember.submit();
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Member &gt; Import Data Member<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <form name="frmmember" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectSupplierExist = new Vector();
					String[] arrBarcode = request.getParameterValues("barcode");
					String[] arrType = request.getParameterValues("type");
					String[] arrNama = request.getParameterValues("nama");
					String[] arrAlamat = request.getParameterValues("alamat");
					String[] arrTelpon = request.getParameterValues("telpon");
					String[] arrHp = request.getParameterValues("hp");
                    String[] arrNoid = request.getParameterValues("noid");
                    String[] arrTmpLahir = request.getParameterValues("tmptlahir");
                    String[] arrSex = request.getParameterValues("sex");
                    String[] arrAgama = request.getParameterValues("agama");
                    // in
					String[] arrTglLahir = request.getParameterValues("tgllahir");
                    String[] arrTglDaftar = request.getParameterValues("tgldaftar");
                    String[] arrTglStart = request.getParameterValues("tglstart");
                    String[] arrTglAkhir = request.getParameterValues("tglakhir");
					
					boolean transferSuccess = false;
                    long oidContactClass = PstContactClass.getOidClassType(PstContactClass.CONTACT_TYPE_MEMBER);
                    Hashtable hashGroup = PstMemberGroup.getMemberGroup();
					
					for (int i=0; i<arrBarcode.length; i++){
                        String strKode = String.valueOf(arrBarcode[i]);
                        MemberReg objMemberReg = new MemberReg();
						
                        MemberGroup memberGroup = (MemberGroup)hashGroup.get(arrType[i].toUpperCase());
						if(memberGroup==null){
                            memberGroup = (MemberGroup)hashGroup.get("RETAIL");
                        }
						
                        objMemberReg.setContactCode(arrBarcode[i]);
                        objMemberReg.setMemberBarcode(arrBarcode[i]);
                        objMemberReg.setMemberGroupId(memberGroup.getOID());
                        objMemberReg.setPersonName(arrNama[i]);
                        objMemberReg.setHomeAddr(arrAlamat[i]);
                        objMemberReg.setHomeTelp(arrTelpon[i]);
                        objMemberReg.setTelpMobile(arrHp[i]);
                        objMemberReg.setMemberIdCardNumber(arrNoid[i]);
						
                        Date dtTglLahir = new Date();
                        try{
                            dtTglLahir = Formater.formatDate(String.valueOf(arrTglLahir[i]), "yyyy-MM-dd");
                        }catch(Exception e){}
                        objMemberReg.setMemberBirthDate(dtTglLahir);
						
                        // sex
                        if(arrSex[i].toUpperCase()=="L"){
                            objMemberReg.setMemberSex(PstMemberReg.MALE);
                        }else{
                            objMemberReg.setMemberSex(PstMemberReg.FEMALE);
                        }
						
                        // agama
                        Hashtable has = PstReligion.getReligion();
                        Religion reg = (Religion)has.get(arrAgama[i].toUpperCase());
                        if(reg!=null){
                            objMemberReg.setMemberReligionId(reg.getOID());
                        }else{
                            objMemberReg.setMemberReligionId(0);
                        }
                        objMemberReg.setHomeTown(arrTmpLahir[i]);

                        try{
                            long oidMemberReg = PstMemberReg.getCekMemberBarcode(strKode);
                            if(oidMemberReg != 0){
                                objMemberReg.setOID(oidMemberReg);
                                vectSupplierExist.add(objMemberReg);
                            }else{
                                long oidMember = PstMemberReg.insertExc(objMemberReg);
                                System.out.println("==>>> **** Sukses contact");

                                MemberRegistrationHistory memberRegHistory = new MemberRegistrationHistory();
                                memberRegHistory.setMemberId(oidMember);
                                Date dtTglDaftar = new Date();
                                try{
                                    dtTglDaftar = Formater.formatDate(String.valueOf(arrTglDaftar[i]), "yyyy-MM-dd");
                                }catch(Exception e){
									System.out.println("Exc dtTglDaftar ->> "+e.toString());
								}
                                memberRegHistory.setRegistrationDate(dtTglDaftar);

                                Date dtTglStart = new Date();
                                try{
                                    dtTglStart = Formater.formatDate(String.valueOf(arrTglStart[i]), "yyyy-MM-dd");
                                }catch(Exception e){
									System.out.println("Exc dtTglStart ->> "+e.toString());
								}
                                memberRegHistory.setValidStartDate(dtTglStart);

                                Date dtTglAkhir = new Date();
                                try{
                                    dtTglAkhir = Formater.formatDate(String.valueOf(arrTglAkhir[i]), "yyyy-MM-dd");
                                }catch(Exception e){
									System.out.println("ExcdtTglAkhir ->> "+e.toString());
								}
                                memberRegHistory.setValidExpiredDate(dtTglAkhir);

                                try{
                                    PstMemberRegistrationHistory.insertExc(memberRegHistory);
                                    System.out.println("==>>> **** Sukses insert MemberRegistrationHistory class");
                                }catch(Exception e){
									System.out.println("Exc when create member reg history ->> "+e.toString());
								}
								
								ContactClassAssign contactClassAssign = new ContactClassAssign();
                                contactClassAssign.setContactClassId(oidContactClass);
                                contactClassAssign.setContactId(oidMember);
                                try{
                                    PstContactClassAssign.insertExc(contactClassAssign);
                                    System.out.println("==>>> **** Sukses insert member type class");
                                }catch(Exception e){
									System.out.println("Exc when insert contact_class_assign ->> "+e.toString());
								}
								
                                transferSuccess = true;
                            }
                        }catch (Exception e){
                            System.out.println("Insert error : " + e);
                        }
                    }

					if(transferSuccess){
						out.println("Upload data member sukses ...");
						out.println("<br><br><div class=\"command\"><a href=\"javascript:cmdBack()\">Kembali ke Menu Utama</a></div>");
					}else{
						out.println("<font color=\"#FF0000\">Upload data member gagal ...</font>");
					}
					%>
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vectSupplierExist.size()>0){%><b>Data member yang tidak bisa di upload : </b><br>
								<%
								out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" width=\"50%\">");
								out.println("<tr><td class=\"listgentitle\">No</td><td class=\"listgentitle\">Kode</td><td class=\"listgentitle\">Nama</td></tr>");
								for(int k=0;k < vectSupplierExist.size();k++){
									MemberReg objMemberReg = (MemberReg)vectSupplierExist.get(k);
									out.println("<tr><td class=\"listgensell\">"+(k+1)+"</td><td class=\"listgensell\">"+objMemberReg.getMemberBarcode()+"</td><td class=\"listgensell\">"+objMemberReg.getPersonName()+"</td></tr>");
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
