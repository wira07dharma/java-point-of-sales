<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.MatConDispatch,
                   com.dimata.posbo.form.warehouse.CtrlMatConDispatch,
                   com.dimata.posbo.entity.search.SrcMatConDispatch,
                   com.dimata.posbo.session.warehouse.SessMatConDispatch,
                   com.dimata.posbo.form.search.FrmSrcMatConDispatch" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->


<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){
	privAdd=false;
	privUpdate=false;
	privDelete=false;
}
%>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Konsinyasi","Pengiriman Barang","Daftar Pengiriman"},
	{"Consigment","Goods Delivery","List of Delivery"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No","Nomor","Tanggal","Lokasi Asal","Lokasi Tujuan","Status"},
	{"No","Number","Date","Dispatch From","Dispatch To","Status"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"20%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		if(start<0)
		{
			start = 0;		
		}	
		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			MatConDispatch df = (MatConDispatch)vt.get(0);
			Location loc1 = (Location)vt.get(1);		
			Location loc2 = new Location();
			try
			{
				loc2 = PstLocation.fetchExc(df.getDispatchTo());		
			}
			catch(Exception e)
			{
			}
			start += 1;
			
			rowx.add(""+start);
			rowx.add(df.getDispatchCode());
			rowx.add(Formater.formatDate(df.getDispatchDate(), "dd-MM-yyyy"));
			rowx.add(loc1.getName());			
			rowx.add(loc2.getName());			
			rowx.add(i_status.getDocStatusName(docType,df.getDispatchStatus()));
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(df.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data pengiriman ...</div>";		
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_DF);
%>

<%
/**
* get title for purchasing(pr) document
*/
String dfCode = "";//i_pstDocType.getDocCode(docType);
String dfTitle = "Pengiriman"; //i_pstDocType.getDocTitle(docType);
String dfItemTitle = dfTitle + " Item";

/**
* get request data from current form
*/
long oidMatConDispatch = FRMQueryString.requestLong(request, "hidden_dispatch_id");

/**
* initialitation some variable
*/
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
CtrlMatConDispatch ctrlMatConDispatch = new CtrlMatConDispatch(request);
SrcMatConDispatch srcMatConDispatch = new SrcMatConDispatch();
SessMatConDispatch sessMatConDispatch = new SessMatConDispatch();
FrmSrcMatConDispatch frmSrcMatConDispatch = new FrmSrcMatConDispatch(request, srcMatConDispatch);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 { 
		srcMatConDispatch = (SrcMatConDispatch)session.getValue(SessMatConDispatch.SESS_SRC_MATDISPATCH); 
		if (srcMatConDispatch == null) srcMatConDispatch = new SrcMatConDispatch();
	 }
	 catch(Exception e)
	 { 
		srcMatConDispatch = new SrcMatConDispatch();
	 }
}
else
{
	 frmSrcMatConDispatch.requestEntityObject(srcMatConDispatch);
	 session.putValue(SessMatConDispatch.SESS_SRC_MATDISPATCH, srcMatConDispatch);
}

/**
* get vectSize, start and data to be display in this page
*/
vectSize = sessMatConDispatch.getCountMatConDispatch(srcMatConDispatch);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	start = ctrlMatConDispatch.actionList(iCommand,start,vectSize,recordToGet);
}	
Vector records = sessMatConDispatch.listMatConDispatch(srcMatConDispatch,start,recordToGet);
%>
<!-- End of Jsp Block -->

<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frm_matdispatch.start.value=0;
	document.frm_matdispatch.approval_command.value="<%=Command.SAVE%>";			
	document.frm_matdispatch.command.value="<%=Command.ADD%>";
	document.frm_matdispatch.add_type.value="<%=ADD_TYPE_LIST%>";			
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frm_matdispatch.submit();
}

function cmdEdit(oid)
{
	document.frm_matdispatch.hidden_dispatch_id.value=oid;
	document.frm_matdispatch.start.value=0;
	document.frm_matdispatch.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_matdispatch.command.value="<%=Command.EDIT%>";
	document.frm_matdispatch.action="df_stock_wh_material_edit.jsp";
	document.frm_matdispatch.submit();
}

function cmdListFirst()
{
	document.frm_matdispatch.command.value="<%=Command.FIRST%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
}

function cmdListPrev()
{
	document.frm_matdispatch.command.value="<%=Command.PREV%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
}

function cmdListNext()
{
	document.frm_matdispatch.command.value="<%=Command.NEXT%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
}

function cmdListLast()
{
	document.frm_matdispatch.command.value="<%=Command.LAST%>";
	document.frm_matdispatch.action="df_stock_wh_material_list.jsp";
	document.frm_matdispatch.submit();
}

function cmdBack()
{
	document.frm_matdispatch.command.value="<%=Command.BACK%>";
	document.frm_matdispatch.action="srcdf_stock_wh_material.jsp";
	document.frm_matdispatch.submit();
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
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
//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
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
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_matdispatch" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">			  			  			  			  
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_dispatch_id" value="<%=oidMatConDispatch%>">
              <input type="hidden" name="approval_command">			   
			  <table width="100%" cellspacing="0" cellpadding="3">
			  <tr align="left" valign="top"> 
				<td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,docType,i_status)%></td>
			  </tr>					  					
			  <tr align="left" valign="top"> 
				<td height="8" align="left" colspan="3" class="command"> 
				  <span class="command"> 
				  <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %> 
				  </span>
				</td>
			  </tr>
			  <tr align="left" valign="top"> 
				<td height="18" valign="top" colspan="3"> 					                             
				    <table width="52%" border="0" cellspacing="0" cellpadding="0">
                      <tr>                               
					  <%if(privAdd){%>							
					    <td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td>
					    <td nowrap width="1%">&nbsp;</td>
					    <td class="command" nowrap width="42%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%></a></td>
					    <td nowrap width="0%">&nbsp;</td>
					  <%}%>
					    <td nowrap width="4%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
					    <td nowrap width="1%">&nbsp;</td>
					    <td class="command" nowrap width="48%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
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
<!-- #EndTemplate -->
</html>
