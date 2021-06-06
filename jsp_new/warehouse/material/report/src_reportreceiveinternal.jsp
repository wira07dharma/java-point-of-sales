<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE_REPORT, AppObjInfo.OBJ_LOCATION_RECEIVE_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String listTextGlobal[][] = {
	{"LAPORAN PENERIMAAN BARANG ANTAR LOKASI","Penerimaan","Dari Toko/Gudang","Laporan Global","Pencarian",
	 "Cetak Laporan Penerimaan"," Tidak ada data penerimaan barang...","Laporan Penerimaan"},
	{"GOODS RECEIVE BETWEEN LOCATION REPORT","Receive","From Store/Warehouse","Global Report","Search",
	 "Print Receive Report","There is no goods receive data ...","Receive Report"}
};

public static final String listTextTitle[][] = {
	{"Periode"," s/d ", "Lokasi","Semua"},
	{"Period"," to ","Location","All"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
	String result = "";
	if(addBody)	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT) {	
			result = listTextTitle[language][index] + " " + prefiks;
		}
		else {
			result = prefiks + " " + listTextTitle[language][index];		
		}
	}
	else {
		result = listTextTitle[language][index];
	} 
	return result;
}

%>


<!-- Jsp Block -->
<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();

SrcReportReceive srcReportReceive = new SrcReportReceive();
FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive();
try {
	srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE);
}
catch(Exception e) {
	srcReportReceive = new SrcReportReceive();
}


if(srcReportReceive==null) {
	srcReportReceive = new SrcReportReceive();
}

try {
	session.removeValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE);
}
catch(Exception e) {
}

Vector val_locationid = new Vector(1,1);
Vector key_locationid = new Vector(1,1); 
//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,PstLocation.fieldNames[PstLocation.FLD_CODE]);
//Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
//add opie-eyek
//algoritma : di check di sistem usernya dimana saja user tsb bisa melakukan create document
String whereClause = " ("+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE +
                   " OR "+PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE +")";

whereClause += " AND "+PstDataCustom.whereLocReportView(userId, "user_create_document_location");

Vector vt_loc = PstLocation.listLocationCreateDocument(0, 0, whereClause, "");
val_locationid.add("0");
key_locationid.add(listTextTitle[SESS_LANGUAGE][3]+" "+listTextTitle[SESS_LANGUAGE][2]);
for(int d=0;d<vt_loc.size();d++) {
	Location loc = (Location)vt_loc.get(d);
	val_locationid.add(""+loc.getOID()+"");
	key_locationid.add(loc.getName());
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdSearch() {
	document.frmsrcreceivereport.command.value="<%=Command.LIST%>";
	document.frmsrcreceivereport.action="reportreceiveinternal_list.jsp";
	document.frmsrcreceivereport.submit();
}

<!--
//-------------- script control line -------------------
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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            <%=listTextGlobal[SESS_LANGUAGE][1]%> &gt; <%=listTextGlobal[SESS_LANGUAGE][2]%> &gt; <%=listTextGlobal[SESS_LANGUAGE][3]%> &gt; <%=listTextGlobal[SESS_LANGUAGE][4]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreceivereport" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <hr size="1">
                  </td>
                </tr>
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td valign="top" width="11%" align="left"><strong><%=listTextTitle[SESS_LANGUAGE][0]%></strong><!--%=getJspTitle(3,SESS_LANGUAGE,"",true)%--></td>
                        <td valign="top" width="0%" align="left"><strong>:</strong></td>
                        <td width="89%" valign="top" align="left"><%=ControlDate.drawDate(frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_FROM], srcReportReceive.getDateFrom(),"formElemen",1,-5)%><%=listTextTitle[SESS_LANGUAGE][1]%><%=ControlDate.drawDate(frmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_DATE_TO], srcReportReceive.getDateTo(),"formElemen",1,-5) %> </td>
                      </tr>
                        <td valign="top" align="left"><strong><%=listTextTitle[SESS_LANGUAGE][2]%></strong><!--%=getJspTitle(0,SESS_LANGUAGE,"",true)%--></td>
                        <td valign="top" align="left"><strong>:</strong></td>
                        <td valign="top" align="left"><%=ControlCombo.draw(FrmSrcReportReceive.fieldNames[FrmSrcReportReceive.FRM_FIELD_LOCATION_ID], null, ""+srcReportReceive.getLocationId(), val_locationid, key_locationid, "", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="11%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="0%" align="left">&nbsp;</td>
                        <td height="21" width="89%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td width="90%" nowrap class="command"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,listTextGlobal[SESS_LANGUAGE][7],ctrLine.CMD_SEARCH,true)%></i></a></td>
                            </tr>
                          </table></td>
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
