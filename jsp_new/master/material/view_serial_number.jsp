<%-- 
    Document   : material_unit_buy
    Created on : Mar 12, 2014, 3:01:20 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%>
<%@page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MASTERDATA, AppObjInfo.OBJ_MASTERDATA_MERK); %>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"No","Kode","Value","Daftar","Kode","Status"},
	{"No","Code","Value","List of","Code","Status"}
};

/* this method used to list material merk */
public String drawList(int language,Vector listserial)
{
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListHeader[language][0],"5%");
	ctrlist.addHeader(textListHeader[language][1],"10%");
        ctrlist.addHeader(textListHeader[language][2],"35%");
	ctrlist.setLinkRow(1);
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
        int start=0;

	for(int i = 0; i < listserial.size(); i++) 
	{
		MaterialStockCode materialStockCode = (MaterialStockCode)listserial.get(i);
		rowx = new Vector();
		start = start + 1;	
		rowx.add("<div align=\"center\">" + start+"</div>");
                rowx.add("<div align=\"center\">" + materialStockCode.getStockCode()+"</div>");
                rowx.add("<div align=\"center\">" + materialStockCode.getStockValue()+"</div>");
		lstData.add(rowx);
	}
	
	return ctrlist.draw();
}
%>

<%

long oidLocation = FRMQueryString.requestLong(request, "hidden_location_id");
long oidMaterial = FRMQueryString.requestLong(request, "hidden_material_id");

Vector listserial = new Vector();
String wherexx = PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_LOCATION_ID]+"='"+oidLocation+"'"+
      " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_MATERIAL_ID]+"='"+oidMaterial+"'"+
      " AND "+PstMaterialStockCode.fieldNames[PstMaterialStockCode.FLD_STOCK_STATUS]+"="+PstMaterialStockCode.FLD_STOCK_STATUS_GOOD;
listserial = PstMaterialStockCode.list(0,0,wherexx,"");

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>

<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
<%@include file="../../styletemplate/template_header_empty.jsp" %>
  <tr>
    <td valign="top" align="left">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmmatmerk" method ="post" action="">
              <input type="hidden" name="oidMaterial" value="<%=oidMaterial%>">
              <input type="hidden" name="hidden_material_id" value="<%=oidMaterial%>">
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr align="left" valign="top"> 
                  <td height="8"  colspan="3"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="left" valign="top"> 
                        <td height="8" valign="middle" colspan="3"> 
                          <hr size="1">
                        </td>
                      </tr>
                      <tr align="left" valign="top"> 
                        <td height="14" valign="middle" colspan="3" class="comment">&nbsp;</td>
                      </tr>
                      <%try{%>
                      <tr align="left" valign="top"> 
                        <td height="22" valign="middle" colspan="3"> 
                            <%=drawList(SESS_LANGUAGE,listserial)%> 
                        </td>
                      </tr>
                      <% 
                      }catch(Exception exc){ 
                      }%>
                    </table>
                  </td>
                </tr>
              </table>
            </form>
           </td> 
        </tr> 
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20"> <!-- #BeginEditable "footer" --> 
      <%if(menuUsed == MENU_ICON){%>
            <%@include file="../../styletemplate/footer.jsp" %>

        <%}else{%>
            <%@ include file = "../../main/footer.jsp" %>
        <%}%>
      <!-- #EndEditable --> 
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>

