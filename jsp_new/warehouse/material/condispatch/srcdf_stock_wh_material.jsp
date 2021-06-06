<%@ page import="com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.posbo.session.warehouse.SessMatDispatch,
                 com.dimata.posbo.entity.search.SrcMatDispatch,
                 com.dimata.posbo.form.search.FrmSrcMatDispatch"%>
<%@ page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_TRANSFER, AppObjInfo.G2_TRANSFER, AppObjInfo.OBJ_TRANSFER); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Konsinyasi","Transfer Barang","Pencarian"},
	{"Consigment","Goods Transfer","Searching"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"Nomor","Lokasi Asal","Lokasi Tujuan","Tanggal","Status","Urut Berdasar"},
	{"Number","From Location","To Location","Date","Status","Sort By"}	
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		result = textListHeader[language][index];		
	}
	else
	{
		result = textListHeader[language][index];
	} 
	return result;
}

public boolean getTruedFalse(Vector vect, int index)
{
	for(int i=0;i<vect.size();i++)
	{
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
int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_DF);
boolean privManageData = true;%>

<%
if(userGroupNewStatus == PstAppUser.GROUP_SUPERVISOR){
	privAdd=false;
	privUpdate=false;
	privDelete=false;
}
%>

<%
/**
* get data from 'hidden form'
*/
int iCommand = FRMQueryString.requestCommand(request);

/**
* declaration of some identifier
*/
String dfCode = i_pstDocType.getDocCode(docType);
String dfTitle = "Transfer Barang"; //i_pstDocType.getDocTitle(docType);
String dfItemTitle = dfTitle + " Item";

/**
* ControlLine 
*/
ControlLine ctrLine = new ControlLine();

com.dimata.posbo.entity.search.SrcMatDispatch srcMatDispatch = new com.dimata.posbo.entity.search.SrcMatDispatch();
com.dimata.posbo.form.search.FrmSrcMatDispatch frmSrcMatDispatch = new com.dimata.posbo.form.search.FrmSrcMatDispatch();
try
{
	srcMatDispatch = (SrcMatDispatch)session.getValue(SessMatDispatch.SESS_SRC_MATDISPATCH);
}
catch(Exception e)
{
	srcMatDispatch = new SrcMatDispatch();
}


if(srcMatDispatch==null)
{
	srcMatDispatch = new SrcMatDispatch();
}

try
{
	session.removeValue(SessMatDispatch.SESS_SRC_MATDISPATCH);
}
catch(Exception e)
{
}
%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdAdd()
{
	document.frmsrcmatdispatch.command.value="<%=Command.ADD%>";
	document.frmsrcmatdispatch.approval_command.value="<%=Command.SAVE%>";	
	document.frmsrcmatdispatch.add_type.value="<%=ADD_TYPE_SEARCH%>";				
	document.frmsrcmatdispatch.action="df_stock_wh_material_edit.jsp";
	if(compareDateForAdd()==true)
	document.frmsrcmatdispatch.submit();
}

function cmdSearch()
{
	document.frmsrcmatdispatch.command.value="<%=Command.LIST%>";
	document.frmsrcmatdispatch.action="df_stock_wh_material_list.jsp";
	document.frmsrcmatdispatch.submit();
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
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%> &gt; <%=textListGlobal[SESS_LANGUAGE][2]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatdispatch" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">			
              <input type="hidden" name="approval_command">			    			  			  			  			  			  
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="1" cellpadding="1">
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(0,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <input type="text" name="<%=frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_CODE] %>"  value="<%= srcMatDispatch.getDispatchCode() %>" class="formElemen" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(1,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <% 
							Vector obj_locationid = new Vector(1,1); 
							Vector val_locationid = new Vector(1,1); 
							Vector key_locationid = new Vector(1,1); 
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							Vector vt_loc = PstLocation.list(0,0,"", PstLocation.fieldNames[PstLocation.FLD_CODE]);
							val_locationid.add("0");
							key_locationid.add("All");
							for(int d=0;d<vt_loc.size();d++) {
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_FROM], null, "", val_locationid, key_locationid, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="16%"><%=getJspTitle(2,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="83%">&nbsp; 
                          <% 
							Vector obj_locationid1 = new Vector(1,1); 
							Vector val_locationid1 = new Vector(1,1); 
							Vector key_locationid1 = new Vector(1,1); 
							String locWhClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE;
							String locOrderBy = String.valueOf(PstLocation.fieldNames[PstLocation.FLD_CODE]);														
							Vector vt_loc1 = PstLocation.list(0,0,locWhClause,locOrderBy);
                            val_locationid1.add("0");
                            key_locationid1.add("All");
							for(int d=0;d<vt_loc1.size();d++){
								Location loc1 = (Location)vt_loc1.get(d);
								val_locationid1.add(""+loc1.getOID()+"");
								key_locationid1.add(loc1.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_TO], null, "", val_locationid1, key_locationid1, "", "formElemen")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="16%" align="left"><%=getJspTitle(3,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="83%" valign="top" align="left"> 
                          <i> 
                          <input type="checkbox" name="<%=FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_IGNORE_DATE]%>" value="1" <%if(srcMatDispatch.getIgnoreDate()){%>checked<%}%>>
                          </i>Semua Tanggal </td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="83%" valign="top"> &nbsp;Dari <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_FROM], (srcMatDispatch.getDispatchDateFrom()==null) ? new Date() : srcMatDispatch.getDispatchDateFrom(), 1, -5, "formElemen", "")%> s/d <%=ControlDate.drawDateWithStyle(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_DISPATCH_DATE_TO], (srcMatDispatch.getDispatchDateTo()==null) ? new Date() : srcMatDispatch.getDispatchDateTo(), 1, -5, "formElemen", "")%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,dfCode,true)%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="83%" valign="top" align="left"> 
                          &nbsp; 
                          <%
							  Vector val_status = new Vector(1,1); //hidden values that will be deliver on request (oids) 
							  Vector key_status = new Vector(1,1); //texts that displayed on combo box
							  val_status.add("-1");
							  key_status.add("All");
								  
							  Vector vectResult = i_status.getDocStatusFor(docType);						  
							  for(int i=0; i<vectResult.size(); i++)
							  {
									if ((i == I_DocStatus.DOCUMENT_STATUS_DRAFT) || (i == I_DocStatus.DOCUMENT_STATUS_POSTED) || (i == I_DocStatus.DOCUMENT_STATUS_CLOSED) || (i == I_DocStatus.DOCUMENT_STATUS_FINAL))	
									{
										Vector vetTemp = (Vector)vectResult.get(i);
										int indexPrStatus = Integer.parseInt(String.valueOf(vetTemp.get(0)));							
										String strPrStatus = String.valueOf(vetTemp.get(1));
										
										val_status.add(""+indexPrStatus);
										key_status.add(strPrStatus);
									}	
							  }
							  String select_status = ""+srcMatDispatch.getStatus(); //selected on combo box*/
						  %>
                          <%=ControlCombo.draw(FrmSrcMatDispatch.fieldNames[FrmSrcMatDispatch.FRM_FIELD_STATUS], null, select_status, val_status, key_status, "", "formElemen")%><%=frmSrcMatDispatch.getErrorMsg(FrmSrcMatDispatch.FRM_FIELD_STATUS)%>
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="16%" align="left"><%=getJspTitle(5,SESS_LANGUAGE,dfCode,false)%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="83%" valign="top" align="left">&nbsp; 
                          <% 
							Vector key_sort = new Vector(1,1); 						  
							Vector val_sort = new Vector(1,1); 
							for(int i=0; i<textListHeader[0].length-1; i++)
							{ 
								key_sort.add(""+i);							
								val_sort.add(""+getJspTitle(i,SESS_LANGUAGE,dfCode,true)); 
							}
							String select_sort = ""+srcMatDispatch.getSortBy(); 
							out.println(ControlCombo.draw(frmSrcMatDispatch.fieldNames[frmSrcMatDispatch.FRM_FIELD_SORT_BY], null, select_sort,key_sort,val_sort,"","formElemen"));
						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="83%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="16%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="83%" valign="top" align="left"> 
                          <table width="57%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="6%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="44%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_SEARCH,true)%></a></td>
                              <% if(privAdd){%>
                              <td nowrap width="7%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                              <td class="command" nowrap width="43%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,dfTitle,ctrLine.CMD_ADD,true)%></a></td>
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
            <SCRIPT language=JavaScript>
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
</SCRIPT>
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
