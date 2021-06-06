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
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.jsp.*" %>
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Stok","Opname","Pencarian","Daftar","Edit","Tidak ada data opname ...."},
	{"Stock","Opname","Search","List","Edit","No opname data available ...."}
};

public static final String textListHeader[][] = {
	{"No","Nomor","Tanggal","Lokasi","Status","Keterangan"},
	{"No","Number","Date","Location","Status","Description"}	
};

public String drawList(Vector objectClass, int start, int language, int docType, I_DocStatus i_status){
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{	
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListHeader[language][0],"3%");
		ctrlist.addHeader(textListHeader[language][1],"15%");
		ctrlist.addHeader(textListHeader[language][2],"10%");
		ctrlist.addHeader(textListHeader[language][3],"25%");
		ctrlist.addHeader(textListHeader[language][4],"7%");
		ctrlist.addHeader(textListHeader[language][5],"40%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start<0)
			start = 0;
	
		for (int i = 0; i < objectClass.size(); i++) 
		{
			Vector vt = (Vector)objectClass.get(i);
			Vector rowx = new Vector();
			start = start + 1;			
			MatStockOpname matStockOpname = (MatStockOpname)vt.get(0);
			Location location = (Location)vt.get(1);
			
			rowx.add(""+start);
			String str_dt_OpnameDate = ""; 
			try
			{
				Date dt_OpnameDate = matStockOpname.getStockOpnameDate();
				if(dt_OpnameDate==null)
				{
					dt_OpnameDate = new Date();
				}
				str_dt_OpnameDate = Formater.formatDate(dt_OpnameDate, "dd-MM-yyyy");
			}
			catch(Exception e)
			{ 
				str_dt_OpnameDate = ""; 
			}
			
			rowx.add(matStockOpname.getStockOpnameNumber());
			rowx.add(str_dt_OpnameDate);
			rowx.add(location.getName());
			rowx.add(i_status.getDocStatusName(docType,matStockOpname.getStockOpnameStatus()));
			rowx.add(matStockOpname.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(matStockOpname.getOID()));
		}
		result = ctrlist.draw();
	}else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";
	}
	return result;	
}
%>

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

ControlLine ctrLine = new ControlLine();
CtrlMatStockOpname ctrlMatStockOpname = new CtrlMatStockOpname(request);
long oidMatStockOpname = FRMQueryString.requestLong(request, "hidden_mat_stock_opname_id");

int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 10;
int vectSize = 0;
String whereClause = "";

SrcMatStockOpname srcMatStockOpname = new SrcMatStockOpname();
FrmSrcMatStockOpname frmSrcMatStockOpname = new FrmSrcMatStockOpname(request, srcMatStockOpname);
frmSrcMatStockOpname.requestEntityObject(srcMatStockOpname);
if((iCommand==Command.NEXT)||(iCommand==Command.FIRST)||(iCommand==Command.PREV)||(iCommand==Command.LAST)){
 try{ 
	if(session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME)!=null){
		srcMatStockOpname = (SrcMatStockOpname)session.getValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME); 
	}
 }catch(Exception e){ 
		srcMatStockOpname = new SrcMatStockOpname();
 }
}

SessMatStockOpname sessMatStockOpname = new SessMatStockOpname();
session.putValue(SessMatStockOpname.SESS_SRC_MATSTOCKOPNAME, srcMatStockOpname);
vectSize = sessMatStockOpname.getCountSearch(srcMatStockOpname);

ctrlMatStockOpname.action(iCommand , oidMatStockOpname);
if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)){
	start = ctrlMatStockOpname.actionList(iCommand, start, vectSize, recordToGet);
}

Vector records = sessMatStockOpname.searchMatStockOpname(srcMatStockOpname, start, recordToGet);
try{
	session.removeValue("SESSION_OPNAME_SELECTED_ITEM");
}catch(Exception e){}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title
><script language="JavaScript">
<!--
function cmdAdd(){
	document.frm_matstockopname.command.value="<%=Command.ADD%>";
	document.frm_matstockopname.approval_command.value="<%=Command.SAVE%>";
	document.frm_matstockopname.action="mat_opname_store_quick_edit.jsp";
	if(compareDateForAdd()==true)
		document.frm_matstockopname.submit();
}

function cmdEdit(oid){
	document.frm_matstockopname.command.value="<%=Command.EDIT%>";
	document.frm_matstockopname.hidden_opname_id.value = oid;
	document.frm_matstockopname.approval_command.value="<%=Command.APPROVE%>";
	document.frm_matstockopname.action="mat_opname_store_quick_edit.jsp";
	document.frm_matstockopname.submit();
}

function cmdListFirst(){
	document.frm_matstockopname.command.value="<%=Command.FIRST%>";
	document.frm_matstockopname.action="mat_opname_store_quick_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListPrev(){
	document.frm_matstockopname.command.value="<%=Command.PREV%>";
	document.frm_matstockopname.action="mat_opname_store_quick_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListNext(){
	document.frm_matstockopname.command.value="<%=Command.NEXT%>";
	document.frm_matstockopname.action="mat_opname_store_quick_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdListLast(){
	document.frm_matstockopname.command.value="<%=Command.LAST%>";
	document.frm_matstockopname.action="mat_opname_store_quick_list.jsp";
	document.frm_matstockopname.submit();
}

function cmdBack(){
	document.frm_matstockopname.command.value="<%=Command.BACK%>";
	document.frm_matstockopname.action="mat_opname_store_quick_src.jsp";
	document.frm_matstockopname.submit();
}

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
            <%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_matstockopname" method="post" action="">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_opname_id" value="<%=oidMatStockOpname%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr> 
                  <td> 
                    <hr size="1" noshade>
                  </td>
                </tr>
                <tr> 
                  <td><%=drawList(records,start,SESS_LANGUAGE,docType,i_status)%></td>
                </tr>
				<% 
				ctrLine.setLocationImg(approot+"/images");
				ctrLine.initDefault();
				String strList = ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet);
				if(strList.length()>0){
				%>				
                <tr> 
                  <td><%=strList%></td>
                </tr>
				<%}%>
                <tr> 				
                  <td> 
                    <table width="60%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <%//if(privAdd && privManageData){%>
                        <td width="5%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%>"></a></td>
                        <td width="34%" nowrap class="command"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_ADD,true)%></a></td>
                        <%//}%>
                        <td width="5%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                        <td width="56%" nowrap class="command"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][1],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
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
