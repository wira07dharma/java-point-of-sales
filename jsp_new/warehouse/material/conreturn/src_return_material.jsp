<% 
/* 
 * Page Name  		:  src_return_material.jsp
 * Created on 		:  Selasa, 2 Agustus 2007 3:04 PM 
 * 
 * @author  		:  gwawan
 * @version  		:  -
 */

/*******************************************************************
 * Page Description : page ini merupakan gabungan dari page :
 						- srcreturn_wh_supp_material.jsp
						- srcreturn_wh_material.jsp.jsp
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
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
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%//@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN, AppObjInfo.OBJ_SUPPLIER_RETURN); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Konsinyasi","Pengembalian Barang","Pencarian","Pengembalian","Pengembalian dengan Nota","Pengembalian tanpa Nota"},
	{"Consigment","Out Stock", "Search","Out Stock","Out Stock with Invoice","Out Stock without Invoice"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] =  {
	{"Nomor","Supplier","Tanggal","Status","Urut Berdasar"},
	{"Number","Supplier","Date","Status","Sort By"}	
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_ROMR);
boolean privManageData = true;%>


<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

/**
* declaration of some identifier
*/
String retCode = ""; //i_pstDocType.getDocCode(docType);
String retTitle = "Pengembalian Barang"; //i_pstDocType.getDocTitle(docType);
String retItemTitle = retTitle + " Item";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();

SrcMatConReturn srcMatConReturn = new SrcMatConReturn();
FrmSrcMatConReturn frmSrcMatConReturn = new FrmSrcMatConReturn();
try {
	srcMatConReturn = (SrcMatConReturn)session.getValue(SessMatConReturn.SESS_SRC_MATRETURN);
}
catch(Exception e) {
	srcMatConReturn = new SrcMatConReturn();
}


if(srcMatConReturn==null) {
	srcMatConReturn = new SrcMatConReturn();
}

try {
	session.removeValue(SessMatConReturn.SESS_SRC_MATRETURN);
}
catch(Exception e) {
}
%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAddRcv() {
	document.frmsrcmatreturn.command.value="<%=Command.ADD%>";
	document.frmsrcmatreturn.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcmatreturn.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcmatreturn.action="return_wh_material_edit.jsp";
	if(compareDateForAdd()==true)
		document.frmsrcmatreturn.submit();
}

function cmdAdd() {
	document.frmsrcmatreturn.command.value="<%=Command.ADD%>";
	document.frmsrcmatreturn.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcmatreturn.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcmatreturn.action="return_wh_supp_material_edit.jsp";
	if(compareDateForAdd()==true)
		document.frmsrcmatreturn.submit();
}

function cmdSearch() {
	document.frmsrcmatreturn.command.value="<%=Command.LIST%>";
	document.frmsrcmatreturn.action="return_material_list.jsp";
	document.frmsrcmatreturn.submit();
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
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%>
		  <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatreturn" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <input type="hidden" name="<%=frmSrcMatConReturn.fieldNames[frmSrcMatConReturn.FRM_FIELD_LOCATION_TYPE]%>" value="<%=PstLocation.TYPE_LOCATION_WAREHOUSE%>">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(0,SESS_LANGUAGE,retCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNNUMBER] %>"  value="<%= srcMatConReturn.getReturnnumber() %>" class="formElemen" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="12%"><%=getJspTitle(1,SESS_LANGUAGE,retCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="87%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_VENDORNAME] %>"  value="<%= srcMatConReturn.getVendorname() %>" class="formElemen" size="30">
                        </td>
                      </tr>
                      <tr>
                        <td valign="top" align="left"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td valign="top" align="left">:</td>
                        <td valign="top" align="left">&nbsp; 
                          <% 
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE,PstLocation.fieldNames[PstLocation.FLD_CODE]);
							Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_CODE]);
							val_locationid.add("0");
							key_locationid.add("All");
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_locationid = ""+srcMatConReturn.getLocationFrom(); //selected on combo box
						  %>
                          <%=ControlCombo.draw(FrmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_LOCATION_FROM], null, select_locationid, val_locationid, key_locationid, "", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="12%" align="left"><%=getJspTitle(2,SESS_LANGUAGE,retCode,true)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <input type="radio" name="<%=frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNDATESTATUS] %>" <%if(srcMatConReturn.getReturndatestatus()==0){%>checked<%}%> value="0">
                          Semua Tanggal</td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="87%" valign="top"> 
                          <input type="radio" name="<%=frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNDATESTATUS] %>" <%if(srcMatConReturn.getReturndatestatus()==1){%>checked<%}%> value="1">
                          Dari <%=ControlDate.drawDate(frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNFROMDATE], srcMatConReturn.getReturnfromdate(),"formElemen",1,-5)%>sampai <%=	ControlDate.drawDate(frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNTODATE], srcMatConReturn.getReturntodate(),"formElemen",1,-5) %> </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="12%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,retCode,true)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="87%" valign="top" align="left"> 
                          <%
						  Vector vectResult = i_status.getDocStatusFor(docType);						  
						  for(int i=0; i<vectResult.size(); i++) {
						  	if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL)) {
								Vector vetTemp = (Vector)vectResult.get(i);
								int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));							
								String strPrStatus = String.valueOf(vetTemp.get(1));
							  %>
							  <input type="checkbox" class="formElemen" name="<%=frmSrcMatConReturn.fieldNames[FrmSrcMatConReturn.FRM_FIELD_RETURNSTATUS]%>" value="<%=(indexPrStatus)%>" <%if(getTruedFalse(srcMatConReturn.getReturnstatus(),indexPrStatus)){%>checked<%}%>>
							  <%=strPrStatus%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
							  <%
							}
						  }
						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="12%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,retCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="87%" valign="top" align="left"> 
                          <% 
							Vector key_sort = new Vector(1,1); 						  
							Vector val_sort = new Vector(1,1); 
							for(int i=0; i<textListHeader[0].length-1; i++){ 
								key_sort.add(""+i);							
								val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,retCode,true)); 
							}
							String select_sort = ""+srcMatConReturn.getReturnsortby(); 
							out.println("&nbsp;"+ControlCombo.draw(frmSrcMatConReturn.fieldNames[frmSrcMatConReturn.FRM_FIELD_RETURNSORTBY], null, select_sort,key_sort,val_sort,"","formElemen"));
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
                              <td nowrap width="4%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][3],ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="26%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][3],ctrLine.CMD_SEARCH,true)%></a></td>
                              <% if(privAdd && privManageData){%>
                              <td nowrap width="4%"><a href="javascript:cmdAddRcv()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td class="command" nowrap width="31%"><a href="javascript:cmdAddRcv()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][4],ctrLine.CMD_ADD,true)%></a></td>
							  <td nowrap width="4%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%>"></a></td>
                              <td class="command" nowrap width="31%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_ADD,true)%></a></td>
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
