<% 
/* 
 * Page Name  		:  src_receive_material.jsp
 * Created on 		:  Selasa, 2 Agustus 2007 10:40 AM 
 * 
 * @author  		:  gwawan, adnyana
 * @version  		:  -
 */

/*******************************************************************
 * Page Description : page ini merupakan gabungan dari page :
 						- srcreceive_wh_supp_material.jsp 
						- srcreceive_wh_supp_po_material.jsp
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*,
                   com.dimata.posbo.entity.search.SrcMatConReceive,
                   com.dimata.posbo.form.search.FrmSrcMatConReceive,
                   com.dimata.posbo.session.warehouse.SessMatConReceive,
                   com.dimata.posbo.entity.warehouse.PstMatConReceive" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package material -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE, AppObjInfo.OBJ_PURCHASE_RECEIVE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Nomor","Consignor","Nota Consignor","Consignee Terima","Tanggal","Status","Urut Berdasar", "Terima Barang", "Dengan PO", "Tanpa PO"},
	{"Number","Consignor","Consignot Invoice","Consignee Receive","Date","Status","Sort By","Goods Receive","With PO","Without PO"}
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody) {
	String result = "";
	if(addBody) {
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else {
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else {
		result = textListHeader[language][index];
	} 
	return result;
}

public boolean getTruedFalse(Vector vect, int index) {
	for(int i=0;i<vect.size();i++) {
		int iStatus = Integer.parseInt((String)vect.get(i));
		if(iStatus==index)
			return true;
	}
	return false;
}
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_LMRR);
boolean privManageData = true;
%>


<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

/**
* declaration of some identifier
*/
String recCode = i_pstDocType.getDocCode(docType);
String recTitle = "Terima Barang"; // i_pstDocType.getDocTitle(docType);
String recItemTitle = recTitle + " Item";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();

SrcMatConReceive srcMatConReceive = new SrcMatConReceive();
FrmSrcMatConReceive frmSrcMatConReceive = new FrmSrcMatConReceive();
try {
	srcMatConReceive = (SrcMatConReceive)session.getValue(SessMatConReceive.SESS_SRC_MATRECEIVE);
}
catch(Exception e) {
	srcMatConReceive = new SrcMatConReceive();
}


if(srcMatConReceive==null) {
	srcMatConReceive = new SrcMatConReceive();
}

try {
	session.removeValue(SessMatConReceive.SESS_SRC_MATRECEIVE);
}
catch(Exception e){
	System.out.println("Exc. when remove session SESS_SRC_MATRECEIVE");
}
%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdAddPO(){
	document.frmsrcmatreceive.command.value="<%=Command.ADD%>";
	document.frmsrcmatreceive.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcmatreceive.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcmatreceive.action="receive_wh_supp_po_material_edit.jsp";
	document.frmsrcmatreceive.submit();
}

function cmdAdd(){
	document.frmsrcmatreceive.command.value="<%=Command.ADD%>";
	document.frmsrcmatreceive.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcmatreceive.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcmatreceive.action="receive_wh_supp_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frmsrcmatreceive.submit();
}

function cmdSearch(){
	document.frmsrcmatreceive.command.value="<%=Command.LIST%>";
	document.frmsrcmatreceive.action="receive_material_list.jsp";
	document.frmsrcmatreceive.submit();
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

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg','<%=approot%>/images/BtnNewOn.jpg')">
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
            &nbsp;Konsinyasi &gt; Terima Barang &gt; Pencarian <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatreceive" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <input type="hidden" name="<%=frmSrcMatConReceive.fieldNames[frmSrcMatConReceive.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <input type="hidden" name="<%=frmSrcMatConReceive.fieldNames[frmSrcMatConReceive.FRM_FIELD_RECEIVE_SOURCE]%>" value="<%=PstMatConReceive.SOURCE_FROM_SUPPLIER%>">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td width="12%" valign="top"><%=getJspTitle(0,SESS_LANGUAGE,recCode,false)%></td>
                        <td width="1%" valign="top">:</td>
                        <td width="87%" valign="top">&nbsp;
                          <input type="text" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVENUMBER] %>"  value="<%= srcMatConReceive.getReceivenumber() %>" class="formElemen" size="20">
                      </td>
                      </tr>
                      <tr> 
                        <td width="12%" valign="top"><%=getJspTitle(1,SESS_LANGUAGE,recCode,false)%></td>
                        <td width="1%" valign="top">:</td>
                        <td width="87%" valign="top">&nbsp;
                          <input type="text" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatConReceive.getVendorname() %>" class="formElemen" size="30">
                        </td>
                      </tr>
                      <tr> 
                        <td width="12%" valign="top"><%=getJspTitle(2,SESS_LANGUAGE,recCode,false)%></td>
                        <td width="1%" valign="top">:</td>
                        <td width="87%">&nbsp;
                          <input type="text" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_INVOICE_SUPPLIER] %>"  value="<%= srcMatConReceive.getInvoiceSupplier() %>" class="formElemen" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(3,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <% 
							Vector obj_locationid = new Vector(1,1); 
							Vector val_locationid = new Vector(1,1); 
							Vector key_locationid = new Vector(1,1); 
							String whereClause = PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
							whereClause += " OR " + PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
							Vector vt_loc = PstLocation.list(0, 0, whereClause, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							val_locationid.add(String.valueOf(0));
							key_locationid.add("All");
							for(int d=0; d<vt_loc.size(); d++) {
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcMatConReceive.fieldNames[frmSrcMatConReceive.FRM_FIELD_LOCATION_ID], null, "", val_locationid, key_locationid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="12%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <input type="radio" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatConReceive.getReceivedatestatus()==0){%>checked<%}%> value="0">
                          Semua Tanggal</td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="87%" valign="top"> 
                          <input type="radio" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVEDATESTATUS] %>" <%if(srcMatConReceive.getReceivedatestatus()==1){%>checked<%}%> value="1">
                          Dari <%=ControlDate.drawDate(frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVEFROMDATE], srcMatConReceive.getReceivefromdate(),"formElemen",1,-5)%> sampai <%=	ControlDate.drawDate(frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVETODATE], srcMatConReceive.getReceivetodate(),"formElemen",1,-5) %> </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <%
						  Vector vectResult = i_status.getDocStatusFor(docType);						  
						  for(int i=0; i<vectResult.size(); i++)
						  {
						  	if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL))	
							{
								Vector vetTemp = (Vector)vectResult.get(i);
								int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));							
								String strPrStatus = String.valueOf(vetTemp.get(1));
							  %>
							  <input type="checkbox" class="formElemen" name="<%=frmSrcMatConReceive.fieldNames[FrmSrcMatConReceive.FRM_FIELD_RECEIVESTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatConReceive.getReceivestatus(),indexPrStatus)){%>checked<%}%>>
							  <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
							  <%
							}
						  }
						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="12%" align="left"><%=getJspTitle(6,SESS_LANGUAGE,recCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="87%" valign="top" align="left"> 
                          <% 
							Vector key_sort = new Vector(1,1); 						  
							Vector val_sort = new Vector(1,1); 
							int maxItem = textListHeader[0].length-1;
							for(int i=0; i<maxItem; i++){ 
								key_sort.add(""+i);							
								val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,recCode,false)); 
							}
							String select_sort = ""+srcMatConReceive.getReceivesortby(); 
							out.println("&nbsp;"+ControlCombo.draw(frmSrcMatConReceive.fieldNames[frmSrcMatConReceive.FRM_FIELD_RECEIVESORTBY], null, select_sort,key_sort,val_sort,"","formElemen"));
						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <table width="80%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7],ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="26%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7],ctrLine.CMD_SEARCH,true)%></a></td>
                              <% if(privAdd){%>
                              <td nowrap width="4%"><a href="javascript:cmdAddPO()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1001','','<%=approot%>/images/BtnNewOn.jpg',1)"><img src="<%=approot%>/images/BtnNew.jpg" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][8],ctrLine.CMD_ADD,true)%>" name="Image1001" width="24" height="24" border="0" id="Image1001"></a></td>
                              <td class="command" nowrap width="31%"><a href="javascript:cmdAddPO()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][8],ctrLine.CMD_ADD,true)%></a></td>
                              <td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][9],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td class="command" nowrap width="31%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][9],ctrLine.CMD_ADD,true)%></a></td>
                              <%}%>
                            </tr>
                          </table>
                        </td>
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
