<%-- 
    Document   : create_transmaterial_old
    Created on : Dec 7, 2015, 4:32:38 PM
    Author     : dimata005
--%>
<%@page contentType="text/html"%>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessMinimumStock"%>
<%@ page import="com.dimata.posbo.entity.search.SrcMinimumStock"%>
<%@ page import="com.dimata.posbo.entity.warehouse.PstMatDispatch"%>
<%@ page import="com.dimata.util.Command"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_MINIMUM_STOCK); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- JSP Block -->
<%
	int start = FRMQueryString.requestInt(request, "start");
	int iCommand = FRMQueryString.requestCommand(request);
    String txtqtypo[] = request.getParameterValues("txt_order");

    // ini digunakan untuk mengantisipasi jika 1 row, karena ngak memerlukan index
    String txtqtyporev = request.getParameter("txt_order");
    System.out.println("txtqtyporev : "+txtqtyporev);
    SrcMinimumStock srcMinimumStock = new SrcMinimumStock();
    try{
        srcMinimumStock = (SrcMinimumStock)session.getValue(SessMinimumStock.SESSION_MINIMUM_NAME);
        srcMinimumStock.resetOidMaterial();
    }catch(Exception e){}
    // ini proses pengambilan qty po automatic creation
    boolean status = false;
    Vector vectQty = new Vector(1,1);
    if(txtqtypo.length > 0){
        for(int k=0;k < txtqtypo.length;k++){
            long oidMaterial = Long.parseLong(txtqtypo[k]);
            double totalQtyRequest = 0;
            if(srcMinimumStock.getvLocation().size()>0){
                Vector vSemu = new Vector();
                for(int i=0;i<srcMinimumStock.getvLocation().size();i++){
                    Location location = (Location)srcMinimumStock.getvLocation().get(i);
                    double qtytrans = FRMQueryString.requestDouble(request, "txt_dispatch_"+location.getOID()+"_"+oidMaterial);
                    if(qtytrans!=0){
                        totalQtyRequest = totalQtyRequest + qtytrans;
                        Vector vt = new Vector();
                        vt.add(String.valueOf(oidMaterial));
                        vt.add(String.valueOf(qtytrans));
                        vt.add(String.valueOf(location.getOID()));
                        vSemu.add(vt);
                    }
                }

                int qtystock = FRMQueryString.requestInt(request, "txt_stock_"+oidMaterial);
                if(qtystock < totalQtyRequest){
                    srcMinimumStock.setOidMaterial(oidMaterial);
                }else{
                    for(int i=0;i<vSemu.size();i++){
                        vectQty.add((Vector)vSemu.get(i));
                    }
                }
            }
        }
        System.out.println("test transfer data : ");
        if(vectQty.size()>0){
            System.out.println("masuk ke sadfasdfasfsd");
            status = PstMatDispatch.AutoInsertTransferData(vectQty,tranUsedPriceHpp);
        }
        if(srcMinimumStock.getListOidMaterial().size()>0){
            try{
                session.removeValue(SessMinimumStock.SESSION_MINIMUM_NAME);
                session.putValue(SessMinimumStock.SESSION_MINIMUM_NAME,srcMinimumStock);
            }catch(Exception e){}
        }
    }
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

    function cmdback(){
        document.frm_newtransfer.command.value = "<%=Command.FIRST%>";
        document.frm_newtransfer.action = "../report/reportstockmin_list.jsp";
        document.frm_newtransfer.submit();
    }

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet"
/>
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
<!--
function hideObjectForMarketing(){
}

function hideObjectForWarehouse(){
}

function hideObjectForProduction(){
}

function hideObjectForPurchasing(){
}

function hideObjectForAccounting(){
}

function hideObjectForHRD(){
}

function hideObjectForGallery(){
}

function hideObjectForMasterData(){
}

function hideObjectForSystem(){
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
//-->
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <%if(menuUsed == MENU_PER_TRANS){%>
  <tr>
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" -->
      <%@ include file = "../../../main/header.jsp" %>
      <!-- #EndEditable --></td>
  </tr>
  <tr>
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" -->
      <%@ include file = "../../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
  <%}else{%>
   <tr bgcolor="#FFFFFF">
    <td height="10" ID="MAINMENU">
      <%@include file="../../../styletemplate/template_header.jsp" %>
    </td>
  </tr>
  <%}%>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><strong>
            Automatic Create Transfer Stock</strong><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <%
            try
            {
            %>
            <form name="frm_newtransfer" method="post" action="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="finish" value="">
              <table width="100%" border="0" cellspacing="1" cellpadding="1">
                <%
                    if(status){
                %>
                <tr>
                  <td align="center" bgcolor="#CCCCCC" span>Transfered Success ... </td>
                </tr>
                  <%
                    if(srcMinimumStock.getListOidMaterial().size()>0){
                  %>
                  <tr>
                    <td align="center" bgcolor="#CCCCCC" span>Somes item cannot process, <a href="javascript:cmdback()">Look Error Transfer</a></td>
                  </tr>
                  <%}%>
                 <%}else{%>
                  <%
                    if(srcMinimumStock.getListOidMaterial().size()>0){
                  %>
                  <tr>
                    <td align="center" bgcolor="#CCCCCC" span>Somes item cannot process, <a href="javascript:cmdback()">Look Error Transfer</a></td>
                  </tr>
                  <%}else{%>
                  <tr>
                    <td align="center" bgcolor="#CCCCCC" span>Proses failed, please try again ... </td>
                  </tr>
                  <%}}%>
              </table>
            </form>
            <%
                }
                catch(Exception eee)
                {
                    out.println("Form Exc : "+eee);
                }
            %>
            <script language="JavaScript">
	window.focus();
</script>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" -->
        <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../../styletemplate/footer.jsp" %>
        <%}else{%>
            <%@ include file = "../../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

