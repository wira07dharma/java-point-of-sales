<%@ page import="com.dimata.util.blob.TextLoader"%>
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import="com.dimata.uploadbarcode.BarcodeTextFile"%>
<%@ page import="com.dimata.uploadbarcode.ResultTextFile"%>
<%@ page import="com.dimata.uploadbarcode.BarcodeTextFile"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Material" %>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import="com.dimata.common.entity.location.Location" %>
<%@ page import="com.dimata.common.entity.location.PstLocation" %>
<%@ page import="com.dimata.common.entity.contact.ContactList" %>
<%@ page import="com.dimata.common.entity.contact.PstContactList" %>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType" %>
<%@ page import="com.dimata.common.entity.payment.PstStandartRate" %>
<%@ page import="com.dimata.common.entity.payment.StandartRate" %>
<%@ page import="com.dimata.common.entity.payment.CurrencyType" %>
<%@ page import="com.dimata.posbo.entity.warehouse.MatReceive" %>
<%@ page import="com.dimata.posbo.entity.warehouse.MatReceiveItem" %>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMatVendorPrice" %>
<%@ page import="com.dimata.posbo.entity.masterdata.MatVendorPrice" %>
<%@ page import="com.dimata.qdep.form.FRMQueryString" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessMatReceive" %>

<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>

<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_PESERTA, AppObjInfo.G2_PESERTA, AppObjInfo.OBJ_D_IMPORT_DATA_EXCEL); %>
<%//@ include file = "../main/checkuser.jsp" %>
<% 
    String path = FRMQueryString.requestString(request, "filename");
    int status = FRMQueryString.requestInt(request, "status_upload");
    int getlist = FRMQueryString.requestInt(request, "get_list");
    
    System.out.println("strPath : "+path+"tanggal : "); //+new Date("0707235612"));
    
    int useBrowse = 0;
    try{
        useBrowse = Integer.parseInt(PstSystemProperty.getValueByName("USE_BARCODE_PATH_STATIC"));
        }catch(Exception e){}
    if(useBrowse==1){
            String pathstatic = "";
            try{
                pathstatic = PstSystemProperty.getValueByName("BARCODE_PATH_STATIC");
                }catch(Exception x){}
            path = pathstatic ;
        }

    /*upload file di barcodefile */
    String pathstatic = "";
  if(useBrowse == 0 && !(getlist==1 || getlist==2)) {
    boolean bool = true;

    try {
    TextLoader uploader = new TextLoader();
    int numFiles = uploader.uploadText(config, request, response);
    String fieldFormName = "filename";
    Object obj = uploader.getTextFile(fieldFormName);
    byte[] byteOfObj = (byte[]) obj;
    int intByteOfObjLength = byteOfObj.length;
    if(intByteOfObjLength > 0){
            pathstatic = PstSystemProperty.getValueByName("BARCODE_PATH_STATIC");
            java.io.ByteArrayInputStream byteIns = new java.io.ByteArrayInputStream(byteOfObj);
            uploader.writeCache(byteIns, pathstatic, true);
        }
     path = pathstatic ;
  }
    catch(Exception e){
        bool = false;
        System.out.println("Exc when upload file : " + e.toString());
    }
  }
  else {
    pathstatic = PstSystemProperty.getValueByName("BARCODE_PATH_STATIC");
    path = pathstatic ;
  }
    //end of barcodefile
    
    // rename file
    if(getlist==1 || getlist==2){
	if(getlist==1){
	    System.out.println("getlist : >> "+getlist);
	    try {
		BarcodeTextFile brt = new BarcodeTextFile(config, request, response);
		brt.renameFile(path);
	    } catch (Exception e) {
		System.out.println("error : >> "+e.toString());
	    }
	}	
	response.sendRedirect("../warehouse/material/receive/receive_material_list.jsp");
    }
       
    // ngambil/upload file sesuai dengan yang dipilih oleh "browse"
    TextLoader uploader = new TextLoader();
    Hashtable has = new Hashtable();
    Vector listResult = new Vector();
    if (status == 1) {
        try {
            Vector list = (Vector) session.getValue("barcode_session");
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    String description = FRMQueryString.requestString(request, "description_"+i+"");
                    String suppNomor = FRMQueryString.requestString(request, "supplier_nomor_"+i+"");
                    long locOid = FRMQueryString.requestLong(request, "loc_oid_"+i+"");
                    Date dateRec = ControlDate.getDateTime("receive_date_"+i+"",request);

                    System.out.println("description >> : "+description);
                    System.out.println("suppNomor >> : "+suppNomor);
                    System.out.println("locOid >> : "+locOid);
                    System.out.println("dateRec >> : "+dateRec);
                    
                    Vector vectreceive = (Vector)list.get(i);
                    MatReceive matreceive = (MatReceive)vectreceive.get(0);
                    matreceive.setLocationId(locOid);
                    matreceive.setInvoiceSupplier(suppNomor);
                    matreceive.setRemark(description);
                    matreceive.setReceiveDate(dateRec);
                    
                    vectreceive.setElementAt(matreceive, 0);
                    list.setElementAt(vectreceive, i);
                }
            }
            listResult = SessMatReceive.approveReceiveProcess(list);

            if(listResult.size()>0){
		try{
		    session.putValue("BARCODE_RECEIVE", listResult);
		    }catch(Exception e){}
                //response.sendRedirect("../warehouse/material/receive/receive_material_list.jsp");
            }else{
		try{
		    session.removeValue("BARCODE_RECEIVE");
		    }catch(Exception e){}
		}
        } catch (Exception ev) {
        } 
    }else{
        try{
          session.removeValue("barcode_session");
        }catch(Exception ev){}
        try {
            BarcodeTextFile br = new BarcodeTextFile(config, request, response);
            //String path = "D:\\user\\gadnyana\\dimata project\\sto\\barcode\\PACK1.DAT";
            has = br.downloadTeFile(path, 0);
        } catch (Exception e) {
        }
    }
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<script type="text/javascript">
    function cmdback() {
        document.frmimportopname.action = "importtextfilereceivesrc.jsp";
        document.frmimportopname.submit();
    }

    function cmdapprove(status) {
        document.frmimportopname.action = "importtextfilereceiveprocess.jsp";
        document.frmimportopname.status_upload.value = status; 
        document.frmimportopname.submit();
    }

    function getlist(status) {
        document.frmimportopname.action = "importtextfilereceiveprocess.jsp";
        document.frmimportopname.get_list.value = status; 
        document.frmimportopname.submit();
    }
    
    function cmdprint(){
	window.open("<%=approot%>/servlet/com.dimata.posbo.session.transferdata.ItemFailedPDF?sess_language=<%=SESS_LANGUAGE%>");
    }
    

</script>
<head>
    <!-- #BeginEditable "doctitle" -->
    <title>Upload Excel</title>
    <!-- #EndEditable -->
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <!-- #BeginEditable "styles" -->
    <link rel="stylesheet" href="../styles/main.css" type="text/css">
    <!-- #EndEditable -->
    <!-- #BeginEditable "stylestab" -->
    <link rel="stylesheet" href="../styles/tab.css" type="text/css">
    <!-- #EndEditable -->
    <!-- #BeginEditable "headerscript" -->
    <!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC">
    <tr>
        <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
            <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
            <%@ include file = "../main/mnmain.jsp" %>
            <!-- #EndEditable --> </td>
  </tr>
  <tr>
    <td width="88%" valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Barcode Process<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
                <%
                    out.println("<form name=\"frmimportopname\" method=\"post\" action=\"importdispatchsave.jsp\">");
                %>
              <input type="hidden" name="status_upload" value="">
		<input type="hidden" name="get_list" value="">
                  <%
                      // ngambil/upload file sesuai dengan yang dipilih oleh "browse"
                      try {
			 Vector listFailed = new Vector();
                          if(listResult.size()==0){
			      if(has!=null) {
				  Enumeration enu = has.keys();
				  Vector listAll = new Vector(1,1);
				  int jindx = 0;
				  while (enu.hasMoreElements()) {
				      Vector v = (Vector) has.get(enu.nextElement());
				      out.println("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">");

				      MatReceive matReceive = new MatReceive();
				      MatReceiveItem matReceiveItem = null;
				      Vector itmList = new Vector(1,1);
				      Vector listOpname = new Vector(1,1);
				      for (int k = 0; k < v.size(); k++) {
					  matReceiveItem = new MatReceiveItem();
					  ResultTextFile result = (ResultTextFile) v.get(k);
					  Material material = new Material();
					  try {
					      material = PstMaterial.fetchBySkuBarcode(result.getProdBarcode());

                                             //If barcode = 0 and ada tanda 0 di dpn barcode scanner
                                              String barcode = result.getProdBarcode();
                                              String barcodePertama = "";
                                                barcodePertama = barcode.substring(0, 1);
                                             if(material.getOID() == 0 && barcodePertama.equals("0")){
                                                int jmlBarcode = barcode.length();
                                                String newBarcode = "";
                                                newBarcode = barcode.substring(1, jmlBarcode);
                                                material = PstMaterial.fetchBySkuBarcode(newBarcode);
                                            }

					      //if(material.getOID()==0){
						  //if(result.getProdBarcode()
						//}
					  } catch (Exception ex) {
					  }

                                          Location loc = new Location();
					  try {
					      //location = PstLocation.fetchByCode(result.getLocCode());
                                              loc = PstLocation.fetchByIdMachine(result.getLocCode());
					  } catch (Exception ex) {
					  }

					  // this for get contact list or vendor
					  ContactList contactList = new ContactList();
					  long oidvendor = 0;
					  try {
					      //oidvendor = PstContactList.cekCodeContact(result.getLocCode());
                                                oidvendor = PstContactList.cekCodeContact(loc.getCode());
					      contactList = PstContactList.fetchExc(oidvendor);
					  } catch (Exception ex) {
					  }

					  // this for get material
					  if (material.getOID() == 0) {
					      //material.setSku("&nbsp;");
					      //material.setBarCode("&nbsp;");
					      //material.setName("Item failed");					      
					      
					      material.setSku("<font color=\"#FF0000\" align=\"left\">"+result.getProdBarcode()+"</font>");
					      material.setBarCode("<font color=\"#FF0000\" align=\"left\">"+result.getProdBarcode()+"</font>");
					      material.setName("Item failed");
					      material.setCurrBuyPrice(result.getQty());

					      Material mat = new Material();
					      mat.setSku(result.getProdBarcode());
					      mat.setBarCode(result.getProdBarcode());
					      mat.setName("Item failed");

					      mat.setCurrBuyPrice(result.getQty());
					      listFailed.add(mat);
					      
					  }else{
					      matReceiveItem.setMaterialId(material.getOID());
					      matReceiveItem.setQty(result.getQty());


					      String where = PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_VENDOR_ID]+"="+oidvendor+
						      " AND "+PstMatVendorPrice.fieldNames[PstMatVendorPrice.FLD_MATERIAL_ID]+"="+oidvendor;

					      Vector vlist = PstMatVendorPrice.list(0, 0, where, "");                                          
					      double pricereal = 0.0;
					      double disc = 0.0;
					      long currencyId = 0;
					      long unitOid = 0;

					      if(vlist.size()>0){
						  MatVendorPrice matVendorPrice = (MatVendorPrice)vlist.get(0);
						  pricereal = matVendorPrice.getOrgBuyingPrice();
						  disc = matVendorPrice.getLastDiscount();
						  currencyId = matVendorPrice.getPriceCurrency();
						  unitOid = matVendorPrice.getBuyingUnitId();
					      }else{
						    unitOid = material.getBuyUnitId();
						    pricereal = material.getDefaultCost();
						    currencyId = material.getDefaultCostCurrencyId();
					      }

					      matReceiveItem.setCost(pricereal);
					      matReceiveItem.setCurrencyId(currencyId);
					      matReceiveItem.setUnitId(unitOid);
					      matReceiveItem.setExpiredDate(new Date());
					      matReceiveItem.setDiscNominal(0);
					      matReceiveItem.setDiscount(0);
					      matReceiveItem.setDiscount2(0);
					      matReceiveItem.setForwarderCost(0);
					      matReceiveItem.setTotal(pricereal * matReceiveItem.getQty());
					      matReceiveItem.setResidueQty(matReceiveItem.getQty());

					      itmList.add(matReceiveItem);
					  }

					  if (contactList.getOID() == 0) {
					      contactList.setCompName("Supplier is Failed");
					  }
					  if (k == 0) {
					      //out.println("<tr>");
					     // out.println("<td width=\"20%\" colspan=\"6\"><div align=\"left\"><b>Receive Data</b></div></td>");
					     // out.println("</tr>");

					      out.println("<tr>");
					      out.println("<td width=\"20%\" colspan=\"6\"><div align=\"left\">");
						out.println("<table width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"2\">");
						out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Supplier</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: "+contactList.getCompName()+"</div></td>");
						out.println("<td width=\"20%\"><div align=\"right\"><b>Description :</b></div></td>");
						out.println("<td width=\"20%\" rowspan=\"5\" valign=\"top\"><div align=\"left\" ><textarea cols=\"20\" name=\"description_"+jindx+"\"></textarea></div></td>");
						out.println("</tr>");

						out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Invoice Supplier Number</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: <input type=\"text\" class=\"formElemen\" name=\"supplier_nomor_"+jindx+"\" value=\"\"></div></td>");
						out.println("</tr>");

						/*out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Invoice Supplier Number</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: <input type=\"text\" class=\"formElemen\" name=\"supplier_nomor_"+jindx+"\" value=\"\"></div></td>");
						out.println("</tr>");*/

						out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Location</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: ");
						Vector vloc = PstLocation.list(0,0,"", "");
						    out.println("<select name=\"loc_oid_"+jindx+"\" class=\"formElemen\">");
						    for(int j=0;j<vloc.size();j++){
							Location location = (Location)vloc.get(j);
							out.println("<option value="+location.getOID()+">"+location.getName()+"</option>");
						    }
						    out.println("</select>");
						out.println("</div></td>");
						out.println("</tr>");

						out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Date</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: "+ControlDate.drawDateWithStyle("receive_date_"+jindx+"",new Date(), 1, -5, "formElemen", "")+"</div></td>");
						out.println("</tr>");

						out.println("<tr>");
						out.println("<td width=\"20%\"><div align=\"left\"><b>Time</b></div></td>");
						out.println("<td width=\"20%\"><div align=\"left\">: "+ControlDate.drawTimeSec("receive_date_"+jindx+"", new Date(), "formElemen")+"</div></td>");
						out.println("</tr>");

						out.println("</table>");
					      out.println("</div></td>");
					      out.println("</tr>");

					      out.println("<tr>");
					      out.println("<td width=\"5%\"><div align=\"center\"><b>No</b></div></td>");
					      out.println("<td width=\"10%\"><div align=\"center\"><b>Code</b></div></td>");
					      out.println("<td width=\"10%\"><div align=\"center\"><b>Barcode</b></div></td>");
					      out.println("<td width=\"35%\"><div align=\"center\"><b>Description</b></div></td>");
					      out.println("<td width=\"5%\"><div align=\"center\"><b>Qty</b></div></td>");
					      out.println("<td width=\"15%\"><div align=\"center\"><b>Datetime</b></div></td>");
					      out.println("</tr>");
					  }
					  out.println("<tr>");

					  if (k == 0) {                                          
						CurrencyType defaultCurrencyType = new CurrencyType();
						defaultCurrencyType = PstCurrencyType.getDefaultCurrencyType();
						matReceive.setCurrencyId(defaultCurrencyType.getOID());

					      if(matReceive.getCurrencyId() != 0) {
						    String whereClause = PstStandartRate.fieldNames[PstStandartRate.FLD_CURRENCY_TYPE_ID]+" = "+matReceive.getCurrencyId();
						    whereClause += " AND "+PstStandartRate.fieldNames[PstStandartRate.FLD_STATUS]+" = "+PstStandartRate.ACTIVE;
						    Vector listStandardRate = PstStandartRate.list(0, 1, whereClause, "");
						    StandartRate objStandartRate = (StandartRate)listStandardRate.get(0);                                                
						    matReceive.setTransRate(objStandartRate.getSellingRate());
						}
						else {
						    matReceive.setTransRate(0);
						}
					      matReceive.setLocationId(0);
					      matReceive.setReceiveDate(new Date());
					      matReceive.setSupplierId(contactList.getOID());
					      matReceive.setRemark(""+Formater.formatDate(result.getDatetime(), "dd/MM/yyyy HH:mm"));
					      //out.println("<td><input type=\"hidden\" name=\"vendor" + contactList.getOID() + "" + Formater.formatDate(result.getDatetime(), "yyyyMMdd") + "\" value=" + contactList.getOID() + "\">" + contactList.getCompName() + "</td>");
					  }
					  out.println("<td><div align=\"center\">"+(k+1)+"</div></td>");

					  out.println("<td>" + material.getSku() + "</td>");
					  out.println("<td>" + material.getBarCode() + "</td>");
					  out.println("<td><input type=\"hidden\" name=\"name" + material.getOID() + "" + contactList.getOID() + "" + Formater.formatDate(result.getDatetime(), "yyyyMMdd") + "\" value=\"" + material.getOID() + "\">" + material.getName() + "</td>");
					  out.println("<td><div align=\"center\">" + result.getQty() + "</div></td>");
					  out.println("<td>" + Formater.formatDate(result.getDatetime(), "yyyy-MM-dd HH:mm") + "</td>");
					  out.println("</tr>");
				      }
				      listOpname.add(matReceive);
				      listOpname.add(itmList);

				      out.println("</table>&nbsp;");
				      System.out.println(v);
				      listAll.add(listOpname);

				      jindx++;
				  }

				  out.println("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
				 if(listFailed.size()>0){				
				out.println("<tr>");
				out.println("<td width=\"15%\"><div align=\"center\"><a href=\"javascript:cmdprint()\">View Item Failed</a></div></td>");
				out.println("</tr>");
			      }
				out.println("<tr>");
				out.println("<td width=\"15%\"><div align=\"center\"><hr></div></td>");
				out.println("</tr>");

				  out.println("<tr>");
				  out.println("<td width=\"15%\"><div align=\"center\"><a href=\"javascript:cmdback()\">Back to </a> | click <b><a href=\"javascript:cmdapprove('1')\">Upload Data</a></b> to approve download from macine.</div></td>");
				  out.println("</tr>");
				out.println("<tr>");
				out.println("<td width=\"15%\"><div align=\"center\"><hr></div></td>");
				out.println("</tr>");
				  
				  out.println("</table>&nbsp;");

				  try{
				    session.putValue("barcode_session_failed",listFailed);   
				    session.putValue("barcode_session",listAll);
				  }catch(Exception ev){}
			    }else{
                                  out.println("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\">&nbsp;<div align=\"center\"><b>Generate failed !, File not found.&nbsp;</b></div></td>");
                                  out.println("</tr>");
				  out.println("</table>&nbsp;");
				  }
                          }else{
                              //for(int i=0;i<listResult.size();i++){
                              //    MatReceive matReceive = (MatReceive)listResult.get(i);
                                  out.println("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\">&nbsp;<div align=\"center\"><b>"+listResult.size()+" document is generate&nbsp;</b></div></td>");
                                  out.println("</tr>");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\"><hr></td>");
                                  out.println("</tr>");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\"><div align=\"center\"><input type=\"button\" onClick=\"javascript:getlist('1')\" name=\"asasa\" value=\"Rename file Barcode & Get Result\"></div></td>");
                                  out.println("</tr>");
				  out.println("<tr>");
                                  out.println("<td width=\"15%\"><hr></td>");
                                  out.println("</tr>");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\"><div align=\"center\"><input type=\"button\" onClick=\"javascript:getlist('2')\" value=\"Get Result\"></div></td>");
                                  out.println("</tr>");
				  			    out.println("</table>&nbsp;");
                              //}
                          }
                      } catch (Exception e) {
                          System.out.println("---===Error : " + e.toString());
                      }
                  %>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
            <%@ include file = "../main/footer.jsp" %>
            <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #BeginEditable "contentfooter" -->
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
