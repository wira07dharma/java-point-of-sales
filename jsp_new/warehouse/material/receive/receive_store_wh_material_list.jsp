<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.warehouse.MatReceive,
                   com.dimata.posbo.entity.search.SrcMatReceive,
                   com.dimata.posbo.form.warehouse.CtrlMatReceive,
                   com.dimata.posbo.session.warehouse.SessMatReceive,
                   com.dimata.posbo.form.search.FrmSrcMatReceive,
                   com.dimata.posbo.entity.warehouse.PstMatReceive" %>
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

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_LOCATION_RECEIVE, AppObjInfo.OBJ_LOCATION_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Penerimaan","Dari Toko/Gudang","Pencarian","Daftar","Edit","Tidak ada data penerimaan"},
	{"Recive","From Store/Warehouse","Search","List","Edit","Receive","No receive data available"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =  {
	{"No","Nomor","Tanggal","Penerimaan Dari","Lokasi Penerimaan","Status","Keterangan"},
	{"No","Number","Date","Receive From","Receive Location","Status","Remark"}
};

public String drawList(int language,Vector objectClass,int start,int docType,I_DocStatus i_status) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][6],"27%");
	
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		if(start < 0) {
			start = 0;		
		}	
		
		Hashtable hashListLocation = PstLocation.getHashListLocation();
		String lokasiAsal = "";
		String lokasiTerima = "";
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatReceive rec = (MatReceive)vt.get(0);
			Location loc = (Location)vt.get(1);		
			start = start + 1;
			lokasiAsal = (String)hashListLocation.get(String.valueOf(rec.getReceiveFrom()));
			lokasiTerima = (String)hashListLocation.get(String.valueOf(rec.getLocationId()));
			
			Vector rowx = new Vector();				
			rowx.add(""+start);
			rowx.add(rec.getRecCode());
			
			String str_dt_RecDate = ""; 
			try {
				Date dt_RecDate = rec.getReceiveDate();
				if(dt_RecDate==null) {
					dt_RecDate = new Date();
				}	
				str_dt_RecDate = Formater.formatDate(dt_RecDate, "dd-MM-yyyy");
			}
			catch(Exception e) {
				str_dt_RecDate = "";
			}
	
			rowx.add(str_dt_RecDate);
			rowx.add(lokasiAsal);//loc.getName());
			rowx.add(lokasiTerima);			
			rowx.add(i_status.getDocStatusName(docType,rec.getReceiveStatus()));
			rowx.add(rec.getRemark());
	
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(rec.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>";		
	}
	return result;
}
%>

<%
// get approval status for create document 
I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
I_Approval i_approval = (I_Approval) Class.forName(approvalClassName).newInstance();
I_DocStatus i_status = (I_DocStatus) Class.forName(docStatusClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
boolean privManageData = true;
%>

<%
// get title for purchasing(pr) document
String recCode = "";//i_pstDocType.getDocCode(docType);
String recTitle = textListGlobal[SESS_LANGUAGE][0];//i_pstDocType.getDocTitle(docType);
String retItemTitle = recTitle + " Item";

// get request data from current form
long oidMatReceive = FRMQueryString.requestLong(request, "hidden_receive_id");

// initialitation some variable
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";

// instantiate some object used in this page
ControlLine ctrLine = new ControlLine();
CtrlMatReceive ctrlMatReceive = new CtrlMatReceive(request);
SrcMatReceive srcMatReceive = new SrcMatReceive();
SessMatReceive sessMatReceive = new SessMatReceive();
FrmSrcMatReceive frmSrcMatReceive = new FrmSrcMatReceive(request, srcMatReceive);

// handle current search data session 
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcMatReceive = (SrcMatReceive)session.getValue(SessMatReceive.SESS_SRC_MATRECEIVE);
        if (srcMatReceive == null) {
            srcMatReceive = new SrcMatReceive();
            srcMatReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
            srcMatReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
        }
    } catch(Exception e) {
        srcMatReceive = new SrcMatReceive();
        srcMatReceive.setLocationType(PstLocation.TYPE_LOCATION_STORE);
        srcMatReceive.setReceiveSource(PstMatReceive.SOURCE_FROM_DISPATCH);
    }
} else {
    frmSrcMatReceive.requestEntityObject(srcMatReceive);
    Vector vectSt = new Vector(1,1);
    String[] strStatus = request.getParameterValues(FrmSrcMatReceive.fieldNames[FrmSrcMatReceive.FRM_FIELD_RECEIVESTATUS]);
    if(strStatus!=null && strStatus.length>0) {
        for(int i=0; i<strStatus.length; i++) {
            try {
                vectSt.add(strStatus[i]);
            } catch(Exception exc) {
                System.out.println("err");
            }
        }
    }
    srcMatReceive.setReceivestatus(vectSt);
    session.putValue(SessMatReceive.SESS_SRC_MATRECEIVE, srcMatReceive);
}

// get vectSize, start and data to be display in this page
String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessMatReceive.getCountSearchNonSupplier(srcMatReceive,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    start = ctrlMatReceive.actionList(iCommand,start,vectSize,recordToGet);
}
Vector records = sessMatReceive.searchMatReceiveNonSupplier(srcMatReceive,start,recordToGet,whereClausex);
%>
<!-- End of Jsp Block -->
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	if(compareDateForAdd()==true){	
		document.frm_recmaterial.start.value=0;
		document.frm_recmaterial.approval_command.value="<%=Command.SAVE%>";			
		document.frm_recmaterial.command.value="<%=Command.ADD%>";
		document.frm_recmaterial.add_type.value="<%=ADD_TYPE_LIST%>";			
		document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
		document.frm_recmaterial.submit();
	}	
}

function cmdEdit(oid){
	document.frm_recmaterial.hidden_receive_id.value=oid;
	document.frm_recmaterial.start.value=0;
	document.frm_recmaterial.approval_command.value="<%=Command.APPROVE%>";					
	document.frm_recmaterial.command.value="<%=Command.EDIT%>";
	document.frm_recmaterial.action="receive_store_wh_material_edit.jsp";
	document.frm_recmaterial.submit();
}

function cmdListFirst(){
	document.frm_recmaterial.command.value="<%=Command.FIRST%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListPrev(){
	document.frm_recmaterial.command.value="<%=Command.PREV%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListNext(){
	document.frm_recmaterial.command.value="<%=Command.NEXT%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdListLast(){
	document.frm_recmaterial.command.value="<%=Command.LAST%>";
	document.frm_recmaterial.action="receive_store_wh_material_list.jsp";
	document.frm_recmaterial.submit();
}

function cmdBack(){
	document.frm_recmaterial.command.value="<%=Command.BACK%>";
	document.frm_recmaterial.action="srcreceive_store_wh_material.jsp";
	document.frm_recmaterial.submit();
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> > <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][3]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_recmaterial" method="retst" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">			  			  			  			  
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_receive_id" value="<%=oidMatReceive%>">
              <input type="hidden" name="approval_command">			   
			  <table width="100%" cellspacing="0" cellpadding="3">
			  <tr>
                  <td valign="top" colspan="3">
                    <hr size="1">
                  </td>
				</tr>
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
				  <table width="51%" border="0" cellspacing="0" cellpadding="0">						
					<tr>                               
					  <%if(privAdd){%>							
					  <td width="5%" nowrap><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD,true)%>"></a></td>
					  <td width="44%" nowrap class="command"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_ADD,true)%></a></td>
					  <%}%>
					  <td width="5%" nowrap><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
					  <td width="46%" nowrap class="command"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,recTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
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
