<%@ page import="com.dimata.util.Command,
                 com.dimata.common.entity.system.PstSystemProperty,
                 com.dimata.common.entity.contact.*,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.common.entity.payment.StandartRate,
                 com.dimata.common.entity.payment.PstStandartRate,
                 com.dimata.posbo.form.masterdata.CtrlMaterial"%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Catalog &gt; Import Data Catalog
      <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
                <form name="frmsupplier" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectProductExist = new Vector();
					String[] arrKode = request.getParameterValues("kode");
					String[] arrSellCurr = request.getParameterValues("sellcurr");
					String[] arrSellRate = request.getParameterValues("sellrate");
					String[] arrSellAgent = request.getParameterValues("sellagent");
					String[] arrSellgenMember = request.getParameterValues("sellgenmember");
					String[] arrSellPublic = request.getParameterValues("sellpublic");
					String[] arrDiscagentMemberPercen = request.getParameterValues("discagentmemberpercen");
					String[] arrDiscagentMemberValue = request.getParameterValues("discagentmembervalue");
					String[] arrDiscGenMemberPercen = request.getParameterValues("discgenmemberpercen");
                    String[] arrDiscGenMemberValue = request.getParameterValues("discgenmembervalue");
                    String[] srrDiscPublicMemberPercen = request.getParameterValues("discpublicmemberpercen");
                    String[] srrDiscPublicMemberValue = request.getParameterValues("discpublicmembervalue");

                    //System.out.println("==>>> arrDiscagentMemberPercen : "+arrDiscagentMemberPercen);
                    //System.out.println("==>>> arrDiscagentMemberValue : "+arrDiscagentMemberValue);
                    //System.out.println("==>>> arrDiscGenMemberPercen : "+arrDiscGenMemberPercen);
                    //System.out.println("==>>> arrDiscGenMemberValue : "+arrDiscGenMemberValue);
                    //System.out.println("==>>> srrDiscPublicMemberPercen : "+srrDiscPublicMemberPercen);
                    //System.out.println("==>>> srrDiscPublicMemberValue : "+srrDiscPublicMemberValue);

					String strError = "";

                    Hashtable lsHasCurr = PstCurrencyType.getListMerkHashtable();
                    boolean transferSuccess = false;
					Vector verr = new Vector();
                    long priceTypeMemberAgen = 0;
                    long priceTypePublic = 0;
                    long priceTypeMemberUmum = 0;
                    long discMemberAgen = 0;
                    long discPublic = 0;
                    long discMemberUmum = 0;

                        try{
                            priceTypeMemberUmum = Long.parseLong(PstSystemProperty.getValueByName("PRICE_TYPE_VIP"));
                            priceTypeMemberAgen = Long.parseLong(PstSystemProperty.getValueByName("PRICE_TYPE_WHOLESALE"));
                            priceTypePublic = Long.parseLong(PstSystemProperty.getValueByName("PRICE_TYPE_RETAIL"));
                            discMemberUmum = Long.parseLong(PstSystemProperty.getValueByName("DISC_VIP"));
                            discMemberAgen = Long.parseLong(PstSystemProperty.getValueByName("DISC_WHOLESALE"));
                            discPublic = Long.parseLong(PstSystemProperty.getValueByName("DISC_RETAIL"));
                        }catch(Exception e){
                            System.out.println("Error at get SYSTEM PROPERTY >>> "+e.toString());
                        }

                    // proses men set program yang untk hanoman / material sendiri.
                    int DESIGN_MATERIAL_FOR = 0;
                    try {
                        String designMat = String.valueOf(com.dimata.system.entity.PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
                        DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
                    } catch (Exception e) {}
                        // long oidContactClass = PstContactClass.getOidClassType(PstContactClass.CONTACT_TYPE_SUPPLIER);
					for (int i=0; i<arrKode.length; i++){
                        String strKode = String.valueOf(arrKode[i]);
                        Material objMaterial = PstMaterial.getMaterialByCode(arrKode[i]);
                        if(objMaterial.getOID()!=0){
                            try{
                                CurrencyType currTypeBuy = (CurrencyType)lsHasCurr.get(arrSellCurr[i].toUpperCase());
                                if(currTypeBuy==null){
                                    currTypeBuy = new CurrencyType();
                                }
                                //System.out.println("==>>> currTypeBuy : "+currTypeBuy.getName());
                                StandartRate standartRate = PstStandartRate.getActiveStandardRate(currTypeBuy.getOID());
                                if(standartRate.getOID()!=0){
                                    PriceTypeMapping priceTypeMapping = new PriceTypeMapping();
                                    // price member agen
                                    priceTypeMapping.setMaterialId(objMaterial.getOID());
                                    priceTypeMapping.setPriceTypeId(priceTypeMemberAgen);
                                    priceTypeMapping.setStandartRateId(standartRate.getOID());
                                    try{
                                        //System.out.println("==>>> arrSellAgent[i] : "+arrSellAgent[i]);
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellAgent[i]));
                                        if(Double.parseDouble(arrSellAgent[i])!=0){
                                            PriceTypeMapping priceTypeMapp = PstPriceTypeMapping.fetchExc(priceTypeMemberAgen,objMaterial.getOID(),standartRate.getOID());
                                            if(priceTypeMapp.getOID()!=0){
                                                priceTypeMapp.setPrice(priceTypeMapping.getPrice());
                                                PstPriceTypeMapping.updateExc(priceTypeMapp);
                                            }else
                                                PstPriceTypeMapping.insertExc(priceTypeMapping);
                                        }
                                    }catch(Exception e){
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellAgent[i]));
                                        PstPriceTypeMapping.insertExc(priceTypeMapping);
										System.out.println("error at price member agen : "+e.toString());
                                    }

                                    // price member public
                                    priceTypeMapping = new PriceTypeMapping();
                                    priceTypeMapping.setMaterialId(objMaterial.getOID());
                                    priceTypeMapping.setPriceTypeId(priceTypePublic);
                                    priceTypeMapping.setStandartRateId(standartRate.getOID());
                                    try{
                                        //System.out.println("==>>> arrSellPublic[i] : "+arrSellPublic[i]);
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellPublic[i]));
                                        if(Double.parseDouble(arrSellPublic[i])!=0){
                                            PriceTypeMapping priceTypeMapp = PstPriceTypeMapping.fetchExc(priceTypePublic,objMaterial.getOID(),standartRate.getOID());
                                            if(priceTypeMapp.getOID()!=0){
                                                priceTypeMapp.setPrice(priceTypeMapping.getPrice());
                                                PstPriceTypeMapping.updateExc(priceTypeMapp);
                                            }else
                                                PstPriceTypeMapping.insertExc(priceTypeMapping);

                                            //PstPriceTypeMapping.insertExc(priceTypeMapping);
                                            if (DESIGN_MATERIAL_FOR == CtrlMaterial.DESIGN_HANOMAN) {
                                                CtrlMaterial.updateCatalogPriceManual(objMaterial.getOID(),Double.parseDouble(arrSellPublic[i]));
                                            }
                                        }
                                    }catch(Exception e){
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellPublic[i]));
                                        PstPriceTypeMapping.insertExc(priceTypeMapping);
										System.out.println("error at price member pulic : "+e.toString());
                                    }
                                    // price member umum
                                    priceTypeMapping = new PriceTypeMapping();
                                    priceTypeMapping.setMaterialId(objMaterial.getOID());
                                    priceTypeMapping.setPriceTypeId(priceTypeMemberUmum);
                                    priceTypeMapping.setStandartRateId(standartRate.getOID());
                                    try{
                                        //System.out.println("==>>> arrSellgenMember[i] : "+arrSellgenMember[i]);
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellgenMember[i]));
                                        if(Double.parseDouble(arrSellgenMember[i])!=0){
                                            PriceTypeMapping priceTypeMapp = PstPriceTypeMapping.fetchExc(priceTypeMemberUmum,objMaterial.getOID(),standartRate.getOID());
                                            if(priceTypeMapp.getOID()!=0){
                                                priceTypeMapp.setPrice(priceTypeMapping.getPrice());
                                                PstPriceTypeMapping.updateExc(priceTypeMapp);
                                            }else
                                                PstPriceTypeMapping.insertExc(priceTypeMapping);
                                            //PstPriceTypeMapping.insertExc(priceTypeMapping);
                                        }
                                    }catch(Exception e){
                                        priceTypeMapping.setPrice(Double.parseDouble(arrSellgenMember[i]));
                                        PstPriceTypeMapping.insertExc(priceTypeMapping);
										System.out.println("error at price member umum : "+e.toString());
                                    }
                                    // Discount Agen
                                    DiscountMapping discountMapping = new DiscountMapping();
                                    discountMapping.setMaterialId(objMaterial.getOID());
                                    discountMapping.setDiscountTypeId(discMemberAgen);
                                    discountMapping.setCurrencyTypeId(currTypeBuy.getOID());
                                    //System.out.println("==>>> arrDiscagentMemberPercen[i] : "+arrDiscagentMemberPercen[i]);
                                    //System.out.println("==>>> arrDiscagentMemberValue[i] : "+arrDiscagentMemberValue[i]);
                                    discountMapping.setDiscountPct(Double.parseDouble(arrDiscagentMemberPercen[i]));
                                    discountMapping.setDiscountValue(Double.parseDouble(arrDiscagentMemberValue[i]));
                                    try{
                                        DiscountMapping pstDistMapp = new DiscountMapping();
										try{
											pstDistMapp = PstDiscountMapping.fetchExc(discMemberAgen,objMaterial.getOID(),currTypeBuy.getOID());
										}catch(Exception e){
											pstDistMapp = new DiscountMapping();
										}
										
                                        if(pstDistMapp.getOID()!=0){
                                            pstDistMapp.setDiscountValue(discountMapping.getDiscountValue());
                                            pstDistMapp.setDiscountPct(discountMapping.getDiscountPct());
											PstDiscountMapping.updateExc(pstDistMapp);
                                        }else
                                            PstDiscountMapping.insertExc(discountMapping);

                                    }catch(Exception e){
										System.out.println("error at discount agen : "+e.toString());
									}

                                    // Discount public
                                    discountMapping = new DiscountMapping();
                                    discountMapping.setMaterialId(objMaterial.getOID());
                                    discountMapping.setDiscountTypeId(discPublic);
                                    discountMapping.setCurrencyTypeId(currTypeBuy.getOID());
                                    //System.out.println("==>>> srrDiscPublicMemberPercen[i] : "+srrDiscPublicMemberPercen[i]);
                                    //System.out.println("==>>> srrDiscPublicMemberValue[i] : "+srrDiscPublicMemberValue[i]);
                                    discountMapping.setDiscountPct(Double.parseDouble(srrDiscPublicMemberPercen[i]));
                                    discountMapping.setDiscountValue(Double.parseDouble(srrDiscPublicMemberValue[i]));
                                    try{
                                        DiscountMapping pstDistMapp = new DiscountMapping();
										try{
											pstDistMapp = PstDiscountMapping.fetchExc(discPublic,objMaterial.getOID(),currTypeBuy.getOID());
										}catch(Exception e){
											pstDistMapp = new DiscountMapping();
										}
										
                                        if(pstDistMapp.getOID()!=0){
                                            pstDistMapp.setDiscountValue(discountMapping.getDiscountValue());
                                            pstDistMapp.setDiscountPct(discountMapping.getDiscountPct());
											PstDiscountMapping.updateExc(pstDistMapp);
                                        }else
                                            PstDiscountMapping.insertExc(discountMapping);
                                    }catch(Exception e){
										System.out.println("error at discount public : "+e.toString());
									}

                                    // discount member general
                                    discountMapping = new DiscountMapping();
                                    discountMapping.setMaterialId(objMaterial.getOID());
                                    discountMapping.setDiscountTypeId(discMemberUmum);
                                    discountMapping.setCurrencyTypeId(currTypeBuy.getOID());
                                    //System.out.println("==>>> arrDiscGenMemberPercen[i] : "+arrDiscGenMemberPercen[i]);
                                    //System.out.println("==>>> arrDiscGenMemberValue[i] : "+arrDiscGenMemberValue[i]);
                                    discountMapping.setDiscountPct(Double.parseDouble(arrDiscGenMemberPercen[i]));
                                    discountMapping.setDiscountValue(Double.parseDouble(arrDiscGenMemberValue[i]));
                                    try{
                                        DiscountMapping pstDistMapp = new DiscountMapping();
										try{
                                        	pstDistMapp = PstDiscountMapping.fetchExc(discMemberUmum,objMaterial.getOID(),currTypeBuy.getOID());
										}catch(Exception e){
											pstDistMapp = new DiscountMapping();
										}
										
                                        if(pstDistMapp.getOID()!=0){
                                            pstDistMapp.setDiscountValue(discountMapping.getDiscountValue());
                                            pstDistMapp.setDiscountPct(discountMapping.getDiscountPct());
                                        }else
                                            PstDiscountMapping.insertExc(discountMapping);

                                    }catch(Exception e){
										System.out.println("error at discount member general : "+e.toString());
									}
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
