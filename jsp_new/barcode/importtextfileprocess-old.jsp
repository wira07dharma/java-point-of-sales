<%@ page import="com.dimata.util.blob.TextLoader"%>
<%@ page import="com.dimata.uploadbarcode.BarcodeTextFile"%>
<%@ page import="com.dimata.uploadbarcode.ResultTextFile"%>
<%@ page import="com.dimata.uploadbarcode.BarcodeTextFile"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Material" %>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMaterial" %>
<%@ page import="com.dimata.common.entity.location.Location" %>
<%@ page import="com.dimata.common.entity.location.PstLocation" %>
<%@ page import="com.dimata.posbo.entity.warehouse.MatStockOpname" %>
<%@ page import="com.dimata.posbo.entity.warehouse.MatStockOpnameItem" %>
<%@ page import="com.dimata.qdep.form.FRMQueryString" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessMatStockOpname" %>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %> 
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_PESERTA, AppObjInfo.G2_PESERTA, AppObjInfo.OBJ_D_IMPORT_DATA_EXCEL); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
	String path = FRMQueryString.requestString(request, "filename");

    int status = FRMQueryString.requestInt(request, "status_upload");

    // ngambil/upload file sesuai dengan yang dipilih oleh "browse"
    TextLoader uploader = new TextLoader();
    Hashtable has = new Hashtable();
    Vector listResult = new Vector();
    if (status == 1) {
        try {
            Vector list = (Vector) session.getValue("barcode_session");
            listResult = SessMatStockOpname.approveOpnameProcess(list);

            if(listResult.size()>0){
                response.sendRedirect("../warehouse/material/stock/mat_stock_store_correction_list.jsp?status_upload=1");
            }
        } catch (Exception ev) {
        }
    }else{
        try{
          session.removeValue("barcode_session");
        }catch(Exception ev){}
        try {
            uploader.uploadText(config, request, response);
            //String path = "D:\\user\\gadnyana\\dimata project\\sto\\barcode\\PACK1.DAT";
            has = BarcodeTextFile.downloadTeFile(path, 1);
        } catch (Exception e) {
        }
    }
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<script type="text/javascript">
    function cmdback() {
        document.frmimportopname.action = "importtextfilesrc.jsp";
        document.frmimportopname.submit();
    }

    function cmdapprove(status) {
        document.frmimportopname.action = "importtextfileprocess.jsp";
        document.frmimportopname.status_upload.value = status; 
        document.frmimportopname.submit();
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
                  <%
                      // ngambil/upload file sesuai dengan yang dipilih oleh "browse"
                      try {
                          if(listResult.size()==0){
                              Enumeration enu = has.keys();
                              Vector listAll = new Vector(1,1);
                              while (enu.hasMoreElements()) {
                                  Vector v = (Vector) has.get(enu.nextElement());
                                  out.println("<table width=\"100%\" border=\"1\" cellspacing=\"1\" cellpadding=\"1\">");

                                  MatStockOpname matStockOpname = new MatStockOpname();
                                  MatStockOpnameItem matStockOpnameItem = null;
                                  Vector itmList = new Vector(1,1);
                                  Vector listOpname = new Vector(1,1);
                                  for (int k = 0; k < v.size(); k++) {
                                      matStockOpnameItem = new MatStockOpnameItem();
                                      ResultTextFile result = (ResultTextFile) v.get(k);
                                      Material material = new Material();
                                      try {
                                          material = PstMaterial.fetchBySkuBarcode(result.getProdBarcode());
                                      } catch (Exception ex) {
                                      }
                                      Location location = new Location();
                                      try {
                                          location = PstLocation.fetchByCode(result.getLocCode());
                                      } catch (Exception ex) {
                                      }
                                      if (material.getOID() == 0) {
                                          material.setSku("&nbsp;");
                                          material.setBarCode("&nbsp;");
                                          material.setName("Item failed");
                                      }else{
                                          matStockOpnameItem.setMaterialId(material.getOID());
                                          matStockOpnameItem.setQtyOpname(result.getQty());
                                          matStockOpnameItem.setUnitId(material.getDefaultSellUnitId());
                                          matStockOpnameItem.setCost(material.getAveragePrice());
                                          itmList.add(matStockOpnameItem);
                                      }

                                      if (location.getOID() == 0) {
                                          location.setName("Location Failed");
                                      }
                                      if (k == 0) {
                                          out.println("<tr>");
                                          out.println("<td width=\"20%\"><div align=\"center\"><b>Location</b></div></td>");
                                          out.println("<td width=\"10%\"><div align=\"center\"><b>Item Code</b></div></td>");
                                          out.println("<td width=\"10%\"><div align=\"center\"><b>Item Barcode</b></div></td>");
                                          out.println("<td width=\"35%\"><div align=\"center\"><b>Item Name</b></div></td>");
                                          out.println("<td width=\"5%\"><div align=\"center\"><b>Qty</b></div></td>");
                                          out.println("<td width=\"15%\"><div align=\"center\"><b>Datetime</b></div></td>");
                                          out.println("</tr>");
                                      }
                                      out.println("<tr>");
                                      if (k == 0) {
                                          matStockOpname.setLocationId(location.getOID());
                                          matStockOpname.setStockOpnameDate(result.getDatetime());
                                          matStockOpname.setStockOpnameTime(Formater.formatDate(result.getDatetime(), "HH:mm"));
                                          matStockOpname.setSubCategoryId(1);
                                          matStockOpname.setRemark("From Barcode macine");
                                          out.println("<td><input type=\"hidden\" name=\"loc" + location.getOID() + "" + Formater.formatDate(result.getDatetime(), "yyyyMMdd") + "\" value=" + location.getOID() + "\">" + location.getName() + "</td>");
                                      } else {
                                          out.println("<td>&nbsp;</td>");
                                      }
                                      out.println("<td>" + material.getSku() + "</td>");
                                      out.println("<td>" + material.getBarCode() + "</td>");
                                      out.println("<td><input type=\"hidden\" name=\"name" + material.getOID() + "" + location.getOID() + "" + Formater.formatDate(result.getDatetime(), "yyyyMMdd") + "\" value=\"" + material.getOID() + "\">" + material.getName() + "</td>");
                                      out.println("<td><div align=\"center\">" + result.getQty() + "</div></td>");
                                      out.println("<td>" + Formater.formatDate(result.getDatetime(), "yyyy-MM-dd HH:mm") + "</td>");
                                      out.println("</tr>");
                                  }
                                  listOpname.add(matStockOpname);
                                  listOpname.add(itmList);

                                  out.println("</table>&nbsp;");
                                  System.out.println(v);
                                  listAll.add(listOpname);
                              }

                              out.println("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
                              out.println("<tr>");
                              out.println("<td width=\"15%\"><div align=\"center\">click <b><a href=\"javascript:cmdapprove('1')\">Upload Data</a></b> to approve download from macine.</div></td>");
                              out.println("</tr>");
                              out.println("</table>&nbsp;");

                              try{
                                session.putValue("barcode_session",listAll);
                              }catch(Exception ev){}
                          }else{
                              for(int i=0;i<listResult.size();i++){
                                  MatStockOpname matOpname = (MatStockOpname)listResult.get(i);
                                  out.println("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">");
                                  out.println("<tr>");
                                  out.println("<td width=\"15%\">Success ...&nbsp;</td>");
                                  out.println("<td width=\"15%\"><div align=\"center\"><b><a href=\"javascript:cmdedit('"+matOpname.getOID()+"\"')\">"+matOpname.getStockOpnameNumber()+"</a></b></div></td>");
                                  out.println("<td width=\"15%\">&nbsp;</td>");
                                  out.println("</tr>");
                                  out.println("</table>&nbsp;");
                              }
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
