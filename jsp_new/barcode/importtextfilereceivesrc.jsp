<%@ page import="com.dimata.common.entity.location.PstLocation"%>
<%@ page import="com.dimata.gui.jsp.ControlCombo"%>
<%@ page import="com.dimata.common.entity.location.Location"%>
<%response.setHeader("Expires", "Mon, 06 Jan 1990 00:00:01 GMT");%>
<%response.setHeader("Pragma", "no-cache");%>
<%response.setHeader("Cache-Control", "nocache");%>
<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>
<% //int  appObjCode = 1; //com.dimata.posbo.entity.admin.AppObjInfo.composeObjCode(AppObjInfo.G1_PESERTA, AppObjInfo.G2_PESERTA, AppObjInfo.OBJ_D_IMPORT_DATA_EXCEL); %>
<%//@ include file = "../main/checkuser.jsp" %>
<% int  appObjCode = com.dimata.posbo.entity.admin.AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../main/checkuser.jsp" %>
 
<!-- JSP Block -->
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<script type="text/javascript">
    function cmdlist() {
        //document.frmdispatch.action = "../warehouse/material/stock/mat_stock_store_correction_list.jsp?status_upload=1";
        document.frmdispatch.action = "../warehouse/material/receive/receive_material_list.jsp";
        document.frmdispatch.submit();
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Receive process  from scanner result <!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
 				<form name="frmdispatch" method="post" action="importtextfilereceiveprocess.jsp" enctype="multipart/form-data">

              <table width="100%" border="0">
                <tr>
                  <td align="center">&nbsp;This button to process insert receive after download data from scanner.</td>
                </tr>
                <tr>
                  <td align="center">
                      <%
                        int useBrowse = 0;
                        try{
                            useBrowse = Integer.parseInt(PstSystemProperty.getValueByName("USE_BARCODE_PATH_STATIC"));
                            }catch(Exception e){
                                useBrowse = 0;
                            }
                        
                        if(useBrowse==0){                      
                      %>
                      <input type="file" name="filename" size="60" value=""><%}%><input type="submit" name="Submit" value="Continue Process >>">                  </td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                  
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td>&nbsp;</td>
                </tr>
                <tr>
                  <td align="center">&nbsp;This button for display list of receive </td>
                </tr>
                <tr>
                  <td align="center"><input type="button" name="button" value="List of Receive" onClick="javascript:cmdlist()"></td>
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
