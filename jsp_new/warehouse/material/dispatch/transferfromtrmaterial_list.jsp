<%-- 
    Document   : transferfromtrmaterial_list
    Created on : Apr 23, 2014, 10:27:37 AM
    Author     : dimata005
--%>

<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.common.entity.payment.CurrencyType,
                   com.dimata.common.entity.payment.PstCurrencyType" %>
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
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.purchasing.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.purchasing.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.purchasing.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_PURCHASING, AppObjInfo.OBJ_PURCHASE_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textMainHeader[][] =
{
	{"Gudang","SR dari Store Request"},
	{"Warehouse","Store Request"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"No","Kode","Tanggal","Supplier","Status","Action","Mata Uang","Lokasi"},
	{"No","Code","Date","Supplier","Status","Action","Currency","Location"}
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
		ctrlist.addHeader(textListMaterialHeader[language][1],"14%");
                ctrlist.addHeader(textListMaterialHeader[language][7],"7%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"7%");
		//ctrlist.addHeader(textListMaterialHeader[language][3],"30%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"7%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"15%");

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

		for(int i=0; i<objectClass.size(); i++){
			Vector vt = (Vector)objectClass.get(i);
			PurchaseRequest po = (PurchaseRequest)vt.get(0);
			//ContactList contact = (ContactList)vt.get(1);
                        
			start = start + 1;

			Vector rowx = new Vector();
			rowx.add(""+start);
			rowx.add(po.getPrCode());
                        
                        rowx.add(po.getLocationName());
                        
			String str_dt_PurchDate = "";
			try{
				Date dt_PurchDate = po.getPurchRequestDate();
				if(dt_PurchDate==null){
					dt_PurchDate = new Date();
				}
				str_dt_PurchDate = Formater.formatDate(dt_PurchDate, "dd-MM-yyyy");
			}
			catch(Exception e){ str_dt_PurchDate = ""; }

			rowx.add(str_dt_PurchDate);
                        
			//rowx.add(cntName);
			rowx.add(i_status.getDocStatusName(docType,po.getPrStatus()));
			
                        //checkbox
                        rowx.add("<center>"
                                + "<input type=\"hidden\" name=\"pomaterial\" value=\""+po.getOID()+"\">"
                                + "<input type=\"radio\" name=\"pomaterial_"+po.getOID()+"\" value=\"1\">"
                                + "</center>");

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(po.getOID()));
		}
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data order pembelian ...</div>";
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_POR);
%>

<%
/**
* get title for purchasing(pr) document
*/
String poCode = "PO"; //i_pstDocType.getDocCode(docType);
String poTitle = textMainHeader[SESS_LANGUAGE][1]; //i_pstDocType.getDocTitle(docType);
String poItemTitle = poTitle + " Item";
String poTitleBlank = "";

/**
* get request data from current form
*/
long oidPurchaseRequest = FRMQueryString.requestLong(request, " hidden_material_request_id");

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
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
CtrlPurchaseRequest ctrlPurchaseRequest = new CtrlPurchaseRequest(request);
SrcPurchaseRequest srcPurchaseRequest = new SrcPurchaseRequest();
SessPurchaseRequest sessPurchaseRequest = new SessPurchaseRequest();
FrmSrcPurchaseRequest frmSrcPurchaseRequest = new FrmSrcPurchaseRequest(request, srcPurchaseRequest);

/**
* handle current search data session
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 {
		srcPurchaseRequest = (SrcPurchaseRequest)session.getValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL);
		if (srcPurchaseRequest == null) srcPurchaseRequest = new SrcPurchaseRequest();
	 }
	 catch(Exception e)
	 {
		System.out.println(" Session null : " +e);
		srcPurchaseRequest = new SrcPurchaseRequest();
	 }
}else{
	 frmSrcPurchaseRequest.requestEntityObject(srcPurchaseRequest);
	 Vector vectSt = new Vector(1,1);
	 String[] strStatus = request.getParameterValues(FrmSrcPurchaseRequest.fieldNames[FrmSrcPurchaseRequest.FRM_FIELD_PRMSTATUS]);
	 if(strStatus!=null && strStatus.length>0){
		 for(int i=0; i<strStatus.length; i++){
			try{
				vectSt.add(strStatus[i]);
			}catch(Exception exc){
				System.out.println("err");
			}
		 }
	 }
	 srcPurchaseRequest.setPrmstatus(vectSt);
	 session.putValue(SessPurchaseRequest.SESS_SRC_REQUESTMATERIAL, srcPurchaseRequest);
}

/**
* get vectSize, start and data to be display in this page
*/

String whereClausex = PstDataCustom.whereLocReportView(userId, "user_create_document_location");

vectSize = sessPurchaseRequest.getCountTransferRequestMaterial(srcPurchaseRequest,docType,1,whereClausex);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST){
	start = ctrlPurchaseRequest.actionList(iCommand,start,vectSize,recordToGet);
}

Vector records = sessPurchaseRequest.searchPurchaseRequestForPOMaterial(srcPurchaseRequest,docType,start,recordToGet,1,whereClausex);

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd(){
    //var xxx = document.frm_requestmaterial.pomaterial.value;
    //alert("xxx"+xxx);
	document.frm_requestmaterial.start.value=0;
	document.frm_requestmaterial.approval_command.value="<%=Command.SAVE%>";
	document.frm_requestmaterial.command.value="<%=Command.ADD%>";
	document.frm_requestmaterial.add_type.value="<%=ADD_TYPE_LIST%>";
	document.frm_requestmaterial.action="create_transferfromtransferequest.jsp";
	if(compareDateForAdd()==true)
		document.frm_requestmaterial.submit();
}

function cmdEdit(oid){
        var strvalue  = "<%=approot%>/purchasing/material/pom/prtowarehousematerial_edit.jsp?hidden_material_request_id="+oid+"&command=3";
        winSrcMaterial = window.open(strvalue,"", "height=600,width=800,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
         if (window.focus) { winSrcMaterial.focus();}
}

function cmdListFirst(){
	document.frm_requestmaterial.command.value="<%=Command.FIRST%>";
	document.frm_requestmaterial.action="prmaterial_list.jsp";
	document.frm_requestmaterial.submit();
}

function cmdListPrev(){
	document.frm_requestmaterial.command.value="<%=Command.PREV%>";
	document.frm_requestmaterial.action="prmaterial_list.jsp";
	document.frm_requestmaterial.submit();
}

function cmdListNext(){
	document.frm_requestmaterial.command.value="<%=Command.NEXT%>";
	document.frm_requestmaterial.action="prmaterial_list.jsp";
	document.frm_requestmaterial.submit();
}

function cmdListLast(){
	document.frm_requestmaterial.command.value="<%=Command.LAST%>";
	document.frm_requestmaterial.action="prmaterial_list.jsp";
	document.frm_requestmaterial.submit();
}

function cmdBack(){
	document.frm_requestmaterial.command.value="<%=Command.BACK%>";
	document.frm_requestmaterial.action="srcprfortransferrequest.jsp";
	document.frm_requestmaterial.submit();
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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable -->
</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg','<%=approot%>/images/BtnBackOn.jpg')">
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
            &nbsp;<%=textMainHeader[SESS_LANGUAGE][0]%> &gt; <%=textMainHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr>
          <td><!-- #BeginEditable "content" -->
            <form name="frm_requestmaterial" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_material_request_id" value="<%=oidPurchaseRequest%>">
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
				    <table width="44%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <%if(privAdd){%>
                        <!--td nowrap width="6%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%>"></a></td-->
                        <td class="command" nowrap width="37%"><a class="btn btn-primary" href="javascript:cmdAdd()"><i class="fa fa-plus-circle"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_ADD,true)%></a></td>
                        <%}%>
                        <!--td nowrap width="2%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                        <td class="command" nowrap width="55%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,poTitle,ctrLine.CMD_BACK_SEARCH,true)%></a></td>
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


