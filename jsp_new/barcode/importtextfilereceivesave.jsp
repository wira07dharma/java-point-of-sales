<%@ page import="com.dimata.util.Command"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatDispatch"%>
<%@ page import="com.dimata.posbo.excel.upload.UploadDispatchExcel"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatDispatchItem"%>
<%@ page import="com.dimata.gui.jsp.ControlDate"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %> 
<% int  appObjCode = 1; // com.dimata.posbo.entity.admin.AppObjInfo.composeObjCode(com.dimata.posbo.entity.admin.AppObjInfo.G1_PESERTA, AppObjInfo.G2_PESERTA, AppObjInfo.OBJ_D_IMPORT_DATA_EXCEL); %>
<%@ include file = "../main/checkuser.jsp" %>
<%

    long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
    long oidLocationTo = FRMQueryString.requestLong(request, "hidden_location_id_to");
    String notes = FRMQueryString.requestString(request, "hidden_remaks");
    MatDispatch matDispatch = new MatDispatch();
    System.out.println("Upload From : "+oidLocation);
    System.out.println("Upload To : "+oidLocationTo);
    System.out.println("Upload notes : "+notes);
    try{
        matDispatch.setLocationId(oidLocation);
        Date dfDate = ControlDate.getDateTime("hidden_dispatch_date", request);
        System.out.println("Upload Date : "+dfDate);
        matDispatch.setDispatchDate(dfDate);
        matDispatch.setDispatchTo(oidLocationTo);
        matDispatch.setRemark(notes);
    }catch(Exception e){
        System.out.println("=>> Error create opname main");
    }


%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Upload
            Excel &gt; Upload Dispatch<!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
                <form name="frmsupplier" method="post" action="">
				  <input type="hidden" name="command" value="">
					<%
                    Vector vectDispatchItem = new Vector();
					String[] arrKode = request.getParameterValues("kode");
					String[] arrQty = request.getParameterValues("qty");
					String strError = "";

					boolean transferSuccess = false;
					Vector verr = new Vector();
					for (int i=0; i<arrKode.length; i++){
                        try{
                            Material mat = new Material();
                            try{
                                mat = PstMaterial.fetchBySku(arrKode[i]);
                            }catch(Exception e){
                            }
                            if(mat.getOID() != 0){
                                MatDispatchItem matStockDfItem = new MatDispatchItem();
                                matStockDfItem.setMaterialId(mat.getOID());
                                matStockDfItem.setQty(Integer.parseInt(String.valueOf(arrQty[i].substring(0,arrQty[i].lastIndexOf(".")))));
                                matStockDfItem.setResidueQty(matStockDfItem.getQty());
                                matStockDfItem.setHpp(mat.getAveragePrice());
                                matStockDfItem.setHppTotal(matStockDfItem.getQty() * mat.getAveragePrice());
                                matStockDfItem.setUnitId(mat.getDefaultStockUnitId());
                                vectDispatchItem.add(matStockDfItem);
                                transferSuccess = true;
                            }
                        }catch (Exception e){
                            System.out.println("Insert error : " + e);
                        }
                    }

                    Vector vt = new Vector();
                    if(vectDispatchItem!=null && vectDispatchItem.size()>0){
                        vt = UploadDispatchExcel.uploadDataDispatch(matDispatch,vectDispatchItem);
                        if(vt.size()>0)
                            transferSuccess = false;
                    }
                    if(transferSuccess){
						out.println("&nbsp;Data is Uploaded ...");
						out.println("<br><br><div class=\"command\"></div>");
					}else{
						out.println("<font color=\"#FF0000\">Upload is failed ...</font>");
					}
					%>
                    <table width="100%"  border="0" cellspacing="1" cellpadding="1">
                      <tr>
                        <td><%if(vt.size()>0){%>
                    <b>Dispatch item can't be uploaded : </b><br>
								<%
								for(int k=0;k < vectDispatchItem.size();k++){
									MatDispatchItem objMatDispatchItem = (MatDispatchItem)vectDispatchItem.get(k);
                                    Material material = new Material();
                                    try{
                                        material = PstMaterial.fetchExc(objMatDispatchItem.getMaterialId());
                                    }catch(Exception e){}
                                    out.print("&nbsp;&nbsp;"+(k+1)+". &nbsp;&nbsp;Kode : "+material.getSku()+" -  &nbsp;&nbsp;Nama : "+material.getName()+"<br>");
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
            <%@ include file = "../main/footer.jsp" %>
            <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #BeginEditable "contentfooter" -->
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
