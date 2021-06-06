<%@ page import="com.dimata.util.Command,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.entity.contact.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.common.entity.payment.StandartRate,
                 com.dimata.common.entity.payment.PstStandartRate"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_CATALOG); %>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Catalog &gt; Import Data Supplier Price
      <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <form name="frmsupplier" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectProductExist = new Vector();
					String[] arrKode = request.getParameterValues("kode");
					String[] arrSuppCode = request.getParameterValues("suppcode");
					String[] arrCurrBuy = request.getParameterValues("currbuy");
					String[] arrBuyingunit = request.getParameterValues("buyingunit");
					String[] arrBuyingPrice = request.getParameterValues("buyingprice");
					String[] arrDiscountPercen = request.getParameterValues("discountpercen");
                    String[] arrPpnPercen = request.getParameterValues("ppnpercen");

                    Hashtable lsHasCurr = PstCurrencyType.getListMerkHashtable();
                    Hashtable lsHasUnit = PstUnit.getListUnitHastable();

                    boolean transferSuccess = false;
					for (int i=0; i<arrKode.length; i++){
                        String strKode = String.valueOf(arrKode[i]);
                        Material objMaterial = PstMaterial.getMaterialByCode(arrKode[i]);
                        long oidContact = PstContactList.cekCodeContact(arrSuppCode[i]);
                        if((objMaterial.getOID()!=0) && oidContact!=0){
                            try{

                                CurrencyType currTypeBuy = (CurrencyType)lsHasCurr.get(arrCurrBuy[i].toUpperCase());
                                if(currTypeBuy==null){
                                    currTypeBuy = new CurrencyType();
                                }

                                Unit unitBuy = (Unit)lsHasUnit.get(arrBuyingunit[i].toUpperCase());
                                 if(unitBuy==null){
                                     unitBuy = new Unit();
                                 }

                                MatVendorPrice matVendorPrice = new MatVendorPrice();
                                matVendorPrice.setMaterialId(objMaterial.getOID());
                                matVendorPrice.setVendorId(oidContact);
                                matVendorPrice.setPriceCurrency(currTypeBuy.getOID());
                                matVendorPrice.setBuyingUnitId(unitBuy.getOID());
                                matVendorPrice.setOrgBuyingPrice(Double.parseDouble(arrBuyingPrice[i]));
                                matVendorPrice.setLastDiscount(Double.parseDouble(arrDiscountPercen[i]));
                                matVendorPrice.setLastVat(Double.parseDouble(arrPpnPercen[i]));
                                matVendorPrice.setCurrBuyingPrice(Double.parseDouble(arrBuyingPrice[i]));

                                try{
                                    PstMatVendorPrice.insertExc(matVendorPrice);
                                }catch(Exception e){
                                    System.out.println("==>>> Err : "+e.toString());
                                }

                                System.out.println("==>>> **** Sukses insert product");
                                transferSuccess = true;
                            }catch (Exception e){
                                System.out.println("Insert error : " + objMaterial.getName()+" = "+ e);
                            }
                        }
                    }

					if(transferSuccess){
						out.println("Upload data sukses ...");
						out.println("<br><br><div class=\"command\"><a href=\"javascript:cmdBack()\">Kembali ke Menu Utama</a></div>");
					}else{
						out.println("<font color=\"#FF0000\">Upload data gagal ...</font>");
					}
					%>
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vectProductExist.size()>0){%><b>Data yang tidak bisa di upload : </b><br>
								<%
								for(int k=0;k < vectProductExist.size();k++){
									Material objMaterial = (Material)vectProductExist.get(k);
									out.print("&nbsp;&nbsp;"+(k+1)+". &nbsp;&nbsp;Kode : "+objMaterial.getSku()+" &nbsp;&nbsp;Nama : "+objMaterial.getName()+"<br>");
								}
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
