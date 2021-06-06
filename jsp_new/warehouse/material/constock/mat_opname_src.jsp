<%@ page import="com.dimata.qdep.entity.I_PstDocType,
                 com.dimata.posbo.entity.search.SrcMatConStockOpname,
                 com.dimata.posbo.session.warehouse.SessMatConStockOpname,
                 com.dimata.posbo.form.search.FrmSrcMatConStockOpname,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlCombo,
                 com.dimata.posbo.entity.masterdata.PstCategory,
                 com.dimata.posbo.entity.masterdata.Category,
                 com.dimata.gui.jsp.ControlDate"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK, AppObjInfo.OBJ_STOCK_OPNAME); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final String textListHeader[][] = 
{
	{"Lokasi","Supplier","Kategori","Sub Kategori",
		"Tanggal Opname","Semua Tanggal","Dari tanggal"," s/d ",
		"Urut Berdasarkan","Status Document", "Semua Lokasi", "Semua Kategori"
	},
		
	{"Location","Supplier","Category","Sub Category",
		"Opname Date","All Date","From","To",
		"Sort by","Document Status", "All Location", "All Category"
	}	
};
%>

<!-- Jsp Block -->
<%
/**
* get approval status for create document 
*/
//I_PstDocType i_pstDocType = (I_PstDocType) Class.forName(docTypeClassName).newInstance();
int systemName = I_DocType.SYSTEM_MATERIAL;
//int docType = i_pstDocType.composeDocumentType(systemName,I_DocType.MAT_DOC_TYPE_OPN);
%>

<%
//String opnameCode = i_pstDocType.getDocCode(docType);
String opnameTitle = "Opname Barang";//i_pstDocType.getDocTitle(docType);

int iCommand = FRMQueryString.requestCommand(request);

ControlLine ctrLine = new ControlLine();
SrcMatConStockOpname srcMatConStockOpname = new SrcMatConStockOpname();
FrmSrcMatConStockOpname frmSrcMatConStockOpname = new FrmSrcMatConStockOpname();
try
{
	srcMatConStockOpname = (SrcMatConStockOpname)session.getValue(SessMatConStockOpname.SESS_SRC_MATSTOCKOPNAME);
}
catch(Exception e)
{
	srcMatConStockOpname = new SrcMatConStockOpname();
	srcMatConStockOpname.setStatus(-1);
}

if(srcMatConStockOpname==null)
{
	srcMatConStockOpname = new SrcMatConStockOpname();
	srcMatConStockOpname.setStatus(-1);
}

try
{
	session.removeValue(SessMatConStockOpname.SESS_SRC_MATSTOCKOPNAME);
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
<!--
function cmdAdd()
{
	document.frmsrcmatstockopname.command.value="<%=Command.ADD%>";
	document.frmsrcmatstockopname.action="mat_opname_edit.jsp";
	document.frmsrcmatstockopname.approval_command.value="<%=Command.SAVE%>";
	if(compareDateForAdd()==true)
		document.frmsrcmatstockopname.submit();
}

function cmdSearch()
{
	document.frmsrcmatstockopname.command.value="<%=Command.LIST%>";
	document.frmsrcmatstockopname.action="mat_opname_list.jsp";
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->Konsinyasi > Opname > Pencarian<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcmatstockopname" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="<%=frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_STATUS]%>" value="-1">
              <table width="100%" border="0" cellspacing="2" cellpadding="2">
                <tr> 
                  <td colspan="3" align="left" class="title">&nbsp; 
                  </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][0]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <%
						Vector locationid_value = new Vector(1,1);
						Vector locationid_key = new Vector(1,1);
						String whereClause = "";//PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_WAREHOUSE;
						Vector vectLocation = PstLocation.list(0,0,whereClause,PstLocation.fieldNames[PstLocation.FLD_CODE]);
						locationid_value.add("0");
						locationid_key.add(textListHeader[SESS_LANGUAGE][10]);
						if(vectLocation!=null && vectLocation.size()>0)
						{						
							for(int b=0; b < vectLocation.size(); b++)
							{
								Location location = (Location)vectLocation.get(b);
								locationid_value.add(""+location.getOID());
								locationid_key.add(location.getName());
							}
						}	
					 	String selectValue = ""+srcMatConStockOpname.getLocationId();
					  %>
                    <%= ControlCombo.draw(frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_LOCATION_ID], null, selectValue, locationid_value, locationid_key, "", "formElemen") %> </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][2]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <%
						Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_CODE]);
						Vector vectGroupVal = new Vector(1,1);
						Vector vectGroupKey = new Vector(1,1);
						vectGroupVal.add(textListHeader[SESS_LANGUAGE][11]);
						vectGroupKey.add("0");																	  	
						if(materGroup!=null && materGroup.size()>0)
						{
							for(int i=0; i<materGroup.size(); i++)
							{
								Category mGroup = (Category)materGroup.get(i);
								vectGroupVal.add(mGroup.getName());
								vectGroupKey.add(""+mGroup.getOID());																	  	
							}
						}
						out.println(ControlCombo.draw(frmSrcMatConStockOpname.fieldNames[frmSrcMatConStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcMatConStockOpname.getLocationId(), vectGroupKey, vectGroupVal, null));
					%>
                  </td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> <%=textListHeader[SESS_LANGUAGE][4]%> </td>
                  <td height="21" width="1%" valign="top" align="left">:</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <input type="radio" class="formElemen" name="<%=frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_STATUS_DATE] %>" <%if(srcMatConStockOpname.getStatusDate()==0){%>checked<%}%> value="0">
                    <%=textListHeader[SESS_LANGUAGE][5]%></td>
                </tr>
                <tr> 
                  <td height="21" width="11%"> 
                    <div align="right"></div>
                  </td>
                  <td height="21" width="1%" valign="top" align="left">&nbsp;</td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <input type="radio" class="formElemen" name="<%=frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_STATUS_DATE] %>" <%if(srcMatConStockOpname.getStatusDate()==1){%>checked<%}%> value="1">
                    <%=textListHeader[SESS_LANGUAGE][6]%> <%=ControlDate.drawDateWithStyle(frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_FROM_DATE],srcMatConStockOpname.getFromDate(),1,-5,"formElemen","") %> 
					<%=textListHeader[SESS_LANGUAGE][7]%> <%=ControlDate.drawDateWithStyle(frmSrcMatConStockOpname.fieldNames[FrmSrcMatConStockOpname.FRM_FIELD_TO_DATE],srcMatConStockOpname.getToDate(),1,-5,"formElemen","")%> </td>
                </tr>
                <tr> 
                  <td height="21" width="11%">&nbsp; </td>
                  <td height="21" width="1%" valign="top" align="left"></td>
                  <td height="21" width="88%" valign="top" align="left"> 
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td width="16%" nowrap> 
                        <td width="14%" nowrap> 
                        <td width="67%" nowrap> 
                        <td width="1%">&nbsp;</td>
                        <td width="2%">&nbsp;</td>
                      </tr>
                    </table>
                  </td>
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
                    <table width="50%" border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td nowrap width="9%"><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image101" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_SEARCH,true)%>"></a></td>
                        <td class="command" nowrap width="45%"><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_SEARCH,true)%></a></td>
                        <% 
						if(privAdd)
						{
						%>
                        <td nowrap width="3%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image100','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image100" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_ADD,true)%>"></a></td>
                        <td class="command" nowrap width="43%"><a href="javascript:cmdAdd()"><%=ctrLine.getCommand(SESS_LANGUAGE,opnameTitle,ctrLine.CMD_ADD,true)%></a></td>
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
<!-- #EndTemplate -->
</html>
