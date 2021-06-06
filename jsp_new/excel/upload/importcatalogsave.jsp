<%@ page import="com.dimata.util.Command,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.entity.contact.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.ObjLink.BOPos.CatalogLink,
                 com.dimata.posbo.form.masterdata.CtrlMaterial"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_IMPORT_DATA, AppObjInfo.OBJ_IMPORT_DATA_CATALOG); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static String[][] listTitleGlobal = {
	{"Proses upload data sudah selesai....", "Proses upload data gagal !",
	 "Data catalog yang tidak bisa di upload karena memiliki kode catalog yang sama",
	 "Data catalog yang tidak bisa di upload karena informasi kategori dan merk yang diberikan salah"},
	{"Upload data process has finished....", "Upload data process was failed !",
	 "Data catalog cannot be upload in duplicate code",
	 "Data catalog cannot be upload. The information of category and merk is incorrect"}
};

%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Upload Excel</title>
<script language="javascript">
function cmdBack(){
	document.frmsupplier.command.value="<%=Command.BACK%>";
	document.frmsupplier.action="<%=approot%>/homepage.jsp";
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Catalog &gt; Import Data Catalog<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <form name="frmsupplier" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectProductExist = new Vector();
					Vector vectProductFailed = new Vector();
					String[] arrKode = request.getParameterValues("kode");
					String[] arrBarcode = request.getParameterValues("barcode");
					String[] arrCategory = request.getParameterValues("category");
					String[] arrMerk = request.getParameterValues("merk");
					String[] arrName = request.getParameterValues("name");
					String[] arrSerialNumber = request.getParameterValues("serial_number");
					String[] arrLastBuyCurr = request.getParameterValues("last_buy_cur");
					String[] arrBuyUnit = request.getParameterValues("buy_unit");
					String[] arrLastBuyPrice = request.getParameterValues("last_buy_price");
                    String[] arrPoint = request.getParameterValues("point");
                    String[] srrSellUnit = request.getParameterValues("sell_unit");
                    String[] arrCogs = request.getParameterValues("cogs");
					String strError = "";

					Hashtable lsHasCateg = PstCategory.getListCategoryHastable();
                    Hashtable lsHasMerk = PstMerk.getListAccountMerk();
                    Hashtable lsHasCurr = PstCurrencyType.getListMerkHashtable();
                    Hashtable lsHasUnit = PstUnit.getListUnitHastable();
                    boolean transferSuccess = false;
					Vector verr = new Vector();
                    long oidContactClass = PstContactClass.getOidClassType(PstContactClass.CONTACT_TYPE_SUPPLIER);


                    // proses men set program yang untk hanoman / material sendiri.
                    int DESIGN_MATERIAL_FOR = 0;
                    try {
                        String designMat = String.valueOf(com.dimata.system.entity.PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
                        DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
                    } catch (Exception e) {}

					for (int i=0; i<arrKode.length; i++){
                        String strKode = String.valueOf(arrKode[i]);
                        Material objMaterial = new Material();
                        objMaterial.setSku(arrKode[i]);
                        objMaterial.setBarCode(arrBarcode[i]);
                        objMaterial.setName(arrName[i]);
						
						if(strKode.length() > 0) { // cek kode, jika kosong proses selanjutnya tidak dieksekusi
							
							// mencari oid merk
							Merk merk = (Merk)lsHasMerk.get(arrMerk[i].toUpperCase());
							if(merk==null){
								merk = new Merk();
							}
							// get oid category
							Category category = (Category)lsHasCateg.get(arrCategory[i].toUpperCase());
							if(category==null){
								category = new Category();
							}
							// get currency oid
							CurrencyType currTypeBuy = (CurrencyType)lsHasCurr.get(arrLastBuyCurr[i].toUpperCase());
							if(currTypeBuy==null){
								currTypeBuy = new CurrencyType();
							}
							// get currency oid
						   Unit unitSell = (Unit)lsHasUnit.get(srrSellUnit[i].toUpperCase());
							if(unitSell==null){
								unitSell = new Unit();
							}
							// get unit buy
							Unit unit = (Unit)lsHasUnit.get(arrBuyUnit[i].toUpperCase());
							if(unit==null){
								unit = new Unit();
							}
	
							int serialUse = 0;
							if(arrSerialNumber[i].toUpperCase().matches("YES")){
								serialUse = 1;
							}
	
							double lastBuying = 0.0;
							if(arrLastBuyPrice[i].length()>0){
								try{
									lastBuying = Double.parseDouble(arrLastBuyPrice[i]);
								}catch(Exception e){}
							}
	
							double avgPrice = 0.0;
							if(arrCogs[i].length()>0){
								try{
									avgPrice = Double.parseDouble(arrCogs[i]);
								}catch(Exception e){}
							}
							
							objMaterial.setMerkId(merk.getOID());
							objMaterial.setCategoryId(category.getOID());
							objMaterial.setDefaultStockUnitId(unitSell.getOID());
							objMaterial.setDefaultPriceCurrencyId(currTypeBuy.getOID());
							objMaterial.setDefaultCostCurrencyId(currTypeBuy.getOID());
							objMaterial.setBuyUnitId(unit.getOID());
							objMaterial.setProcessStatus(PstMaterial.INSERT);
							objMaterial.setDefaultCost(Double.parseDouble(arrLastBuyPrice[i]));
							objMaterial.setRequiredSerialNumber(serialUse);
							objMaterial.setCurrBuyPrice(lastBuying);
							objMaterial.setAveragePrice(avgPrice);
	
							objMaterial.setCurrSellPriceRecomentation(0);
							objMaterial.setProfit(0);
							objMaterial.setDefaultPrice(0);
							objMaterial.setLastDiscount(0);
							objMaterial.setLastVat(0);
							objMaterial.setSubCategoryId(0);
							objMaterial.setSupplierId(0);
							objMaterial.setPriceType01(0);
							objMaterial.setPriceType02(0);
							objMaterial.setPriceType03(0);
							objMaterial.setMaterialType(PstMaterial.MATERIAL_TYPE_REGULAR);
	
							// NEW
							objMaterial.setDefaultSupplierType(0);
							objMaterial.setExpiredDate(new Date());
							objMaterial.setMinimumPoint(0);
							objMaterial.setLastUpdate(new Date());
	
							try {
								long productOid = PstMaterial.checkMaterialCode(strKode);
								if(productOid != 0) { // cek apakah material sudah ada/belum
									objMaterial.setOID(productOid);
									vectProductExist.add(objMaterial);
								} else {
									if(category.getOID() != 0 && merk.getOID() != 0) {
										long oidMaterial = PstMaterial.insertExc(objMaterial);
										if (DESIGN_MATERIAL_FOR == CtrlMaterial.DESIGN_HANOMAN) {
											Vector vloc = new Vector();
											vloc.add(String.valueOf(12412412));
											CtrlMaterial.insertCatalogManual(oidMaterial,vloc);
										}
										System.out.println("==>>> **** Sukses insert product");
										transferSuccess = true;
									}
									else {
										/** kondisi untuk catalog yang tidak membawa informasi kategori dan merk */
										vectProductFailed.add(objMaterial);
									}
								}
							}catch (Exception e){
								System.out.println("Insert error : " + e.toString());
							}
						} // end check code
                    }
					
					if(transferSuccess){
						out.println("<br>&nbsp;&nbsp;"+listTitleGlobal[SESS_LANGUAGE][0]+" ("+(arrKode.length-vectProductExist.size()-vectProductFailed.size())+")<br>");
					}else{
						out.println("<br>&nbsp;&nbsp;<font color=\"#FF0000\">"+listTitleGlobal[SESS_LANGUAGE][1]+"</font><br>");
					}
					
					%>
					<!-- data yang tidak bisa di-upload karena memiliki kode catalog yang sama -->
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vectProductExist.size() > 0){%>
								<br><b><%=listTitleGlobal[SESS_LANGUAGE][2]%> (<%=vectProductExist.size()%>)</b><br>
								<%
								out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" width=\"70%\">");
								out.println("<tr><td class=\"listgentitle\">No</td><td class=\"listgentitle\">Kode</td><td class=\"listgentitle\">Nama</td></tr>");
								for(int k=0;k < vectProductExist.size();k++){
									Material objMaterial = (Material)vectProductExist.get(k);
									out.println("<tr><td class=\"listgensell\">"+(k+1)+"</td><td class=\"listgensell\">"+objMaterial.getSku()+"</td><td class=\"listgensell\">"+objMaterial.getName()+"</td></tr>");
								}
								out.println("</table>");
							%>
						<%}
						%></td>
                      </tr>
                    </table>
					<!-- data yang tidak bisa di-upload karena informasi kategori dan merk yang diberikan salah -->
					<table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vectProductFailed.size() > 0){%>
								<br><b><%=listTitleGlobal[SESS_LANGUAGE][3]%> (<%=vectProductFailed.size()%>)</b><br>
								<%
								out.println("<table class=\"listgen\" cellpadding=\"1\" cellspacing=\"1\" border=\"0\" width=\"70%\">");
								out.println("<tr><td class=\"listgentitle\">No</td><td class=\"listgentitle\">Kode</td><td class=\"listgentitle\">Nama</td></tr>");
								for(int k=0;k < vectProductFailed.size();k++){
									Material objMaterial = (Material)vectProductFailed.get(k);
									out.println("<tr><td class=\"listgensell\">"+(k+1)+"</td><td class=\"listgensell\">"+objMaterial.getSku()+"</td><td class=\"listgensell\">"+objMaterial.getName()+"</td></tr>");
								}
								out.println("</table>");
							%>
						<%}
						%></td>
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
