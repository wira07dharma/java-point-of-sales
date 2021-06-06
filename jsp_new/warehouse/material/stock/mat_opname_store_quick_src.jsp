<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package garment -->
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.jsp.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit"},
	{"Stock","Opname","Search","List","Edit"}
};

public static final String textListHeader[][] = {
	{"Lokasi","Supplier","Kategori","Sub Kategori","Tanggal Opname","Semua Tanggal","Dari tanggal"," s/d ",
		"Urut Berdasarkan","Status Dokumen","Semua Lokasi","Semua Kategori"},
	{"Location","Supplier","Category","Sub Category","Opname Date","All Date","From"," to ",
		"Sort by","Document Status","All Location","All Category"}	
};


%>

<!-- Jsp Block -->
<%
/**
* get approval status for create document 
*/
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_OPN);
%>

<%
String opnameCode = i_pstDocType.getDocCode(docType);
String opnameTitle = "Opname Barang";//i_pstDocType.getDocTitle(docType);

int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();
SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname();
try{
	srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
}
catch(Exception e){
	srcMatStockOpname = new SrcMatStockOpname();
	srcMatStockOpname.setStatus(-1);
}

if(srcMatStockOpname==null){
	srcMatStockOpname = new SrcMatStockOpname();
	srcMatStockOpname.setStatus(-1);
}

try{
	session.removeValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME);
}
catch(Exception e){
}

/** get list location */
Vector locationid_value = new Vector(1,1);
Vector locationid_key = new Vector(1,1);						
//Vector vectLocation = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
Vector vectLocation = PstLocation.list(0, 0, "", PstLocation.fieldNames[PstLocation.FLD_CODE]);
locationid_value.add("0");
locationid_key.add(textListHeader[SESS_LANGUAGE][10]);
for(int b=0; b < vectLocation.size(); b++) {
	Location location = (Location)vectLocation.get(b);
	locationid_value.add(""+location.getOID());
	locationid_key.add(location.getName());
}
String selectValue = ""+srcMatStockOpname.getLocationId();

/** get list category */
Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
Vector vectGroupVal = new Vector(1,1);
Vector vectGroupKey = new Vector(1,1);
vectGroupKey.add("0");
vectGroupVal.add(textListHeader[SESS_LANGUAGE][11]);
if(materGroup!=null && materGroup.size()>0) {
	for(int i=0; i<materGroup.size(); i++) {
		Category mGroup = (Category)materGroup.get(i);
		vectGroupVal.add(mGroup.getName());
		vectGroupKey.add(""+mGroup.getOID());																	  	
	}
}

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
function cmdAdd(){
	document.frmsrcmatstockopname.command.value="<%=Command.ADD%>";
	document.frmsrcmatstockopname.action="mat_opname_store_quick_edit.jsp";
	document.frmsrcmatstockopname.approval_command.value="<%=Command.SAVE%>";
	if(compareDateForAdd()==true)
		document.frmsrcmatstockopname.submit();
}

function cmdSearch(){
	document.frmsrcmatstockopname.command.value="<%=Command.LIST%>";
	document.frmsrcmatstockopname.action="mat_opname_store_quick_list.jsp";
	document.frmsrcmatstockopname.submit();
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
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

</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
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
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatstockopname" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS]%>" value="-1">
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td colspan="3" align="left" class="title"> 
                    <hr size="1">
                  </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][0]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"><%= ControlCombo.draw(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_LOCATION_ID], null, selectValue, locationid_value, locationid_key, "", "formElemen") %> </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][2]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"><%=ControlCombo.draw(frmSrcMatStockOpname.fieldNames[frmSrcMatStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcMatStockOpname.getLocationId(), vectGroupKey, vectGroupVal, null)%>
                  </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][4]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <input type="radio" class="formElemen" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS_DATE] %>" <%if(srcMatStockOpname.getStatusDate()==0){%>checked<%}%> value="0">
                    <%=textListHeader[SESS_LANGUAGE][5]%></td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> 
                    <div align="right"></div>
                  </td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <input type="radio" class="formElemen" name="<%=frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_STATUS_DATE] %>" <%if(srcMatStockOpname.getStatusDate()==1){%>checked<%}%> value="1">
                    <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_FROM_DATE],srcMatStockOpname.getFromDate(),1,-5,"formElemen","") %> 
					<%=textListHeader[SESS_LANGUAGE][7]%> <%=ControlDate.drawDateWithStyle(frmSrcMatStockOpname.fieldNames[FrmSrcMatStockOpname.FRM_FIELD_TO_DATE],srcMatStockOpname.getToDate(),1,-5,"formElemen","")%> </td>
                </tr>
                <tr> 
                  <td height="21" valign="top" width="11%" align="left">&nbsp;</td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="88%" valign="top" align="left">&nbsp;</td>
                </tr>
                <tr> 
                  <td height="21" valign="top" width="11%" align="left">&nbsp;</td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <table width="56%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td nowrap width="6%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_SEARCH,true)%>"></a></td>
                        <td class="command" nowrap width="39%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_SEARCH,true)%></a></td>
                        <% 
						if(privAdd) {
						%>
                        <td nowrap width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%>"></a></td>
                        <td class="command" nowrap width="48%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%></a></td>
                        <%
						}
						%>
                      </tr>
                    </table>
                  </td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
