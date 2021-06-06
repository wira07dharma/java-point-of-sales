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
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"Lokasi","Supplier","Kategori","Sub Kategori","Tanggal","Urut Berdasar"},
	{"Location","Supplier","Category","Sub Category","Date","Sort By"}	
};

public String getJspTitle(int index, int language, String prefiks, boolean addBody)
{
	String result = "";
	if(addBody)
	{
		if(language==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT){	
			result = textListHeader[language][index] + " " + prefiks;
		}
		else
		{
			result = prefiks + " " + textListHeader[language][index];		
		}
	}
	else
	{
		result = textListHeader[language][index];
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

SrcReportStockOpname srcReportStockOpname = new SrcReportStockOpname();
FrmSrcReportStockOpname frmSrcReportStockOpname = new FrmSrcReportStockOpname();
try
{
	srcReportStockOpname = (SrcReportStockOpname)session.getValue(SessReportStockOpname.SESS_SRC_REPORT_STOCK_OPNAME);
}
catch(Exception e)
{
	srcReportStockOpname = new SrcReportStockOpname();
}


if(srcReportStockOpname==null)
{
	srcReportStockOpname = new SrcReportStockOpname();
}

try
{
	session.removeValue(SessReportStockOpname.SESS_SRC_REPORT_STOCK_OPNAME);
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

function cmdSearch()
{
	var reportType = document.frmsrcreportstockopname.open_report.value;
	var textSubKategori = document.frmsrcreportstockopname.txt_subcategory.value;
	if (textSubKategori == "")
	{
		document.frmsrcreportstockopname.<%=FrmSrcReportStockOpname.fieldNames[FrmSrcReportStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>.value = "0";
	}
	document.frmsrcreportstockopname.command.value="<%=Command.LIST%>";
	switch(reportType)
	{
		case "0":
			document.frmsrcreportstockopname.action="reportstockopnameinvoice_list.jsp";
			break;
		case "1":
			document.frmsrcreportstockopname.action="reportstockopnamepriceinvoice_list.jsp";
			break;
	}
	document.frmsrcreportstockopname.submit();
}

function cmdSelectSubCategory()
{
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmsrcreportstockopname.txt_subcategory.value+
			"&oidCategory="+document.frmsrcreportstockopname.<%=FrmSrcReportStockOpname.fieldNames[FrmSrcReportStockOpname.FRM_FIELD_CATEGORY_ID]%>.value +
			"&caller=2",
			"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
            Stock Opname Report by Invoice &gt; <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreportstockopname" method="post" action="">
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
                        <td height="21" width="9%"><%=getJspTitle(0,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp; 
                          <% 
							Vector obj_locationid = new Vector(1,1); 
							Vector val_locationid = new Vector(1,1); 
							Vector key_locationid = new Vector(1,1); 
							Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_CODE]);
							for(int d=0;d<vt_loc.size();d++)
							{
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
						  %>
                          <%=ControlCombo.draw(frmSrcReportStockOpname.fieldNames[frmSrcReportStockOpname.FRM_FIELD_LOCATION_ID], null, ""+srcReportStockOpname.getLocationId(), val_locationid, key_locationid, "", "formElemen")%> 
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" width="9%"><%=getJspTitle(1,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
                          <% 
							Vector obj_supplier = new Vector(1,1);  
							Vector val_supplier = new Vector(1,1); 
							Vector key_supplier = new Vector(1,1); 
							val_supplier.add("0");
							key_supplier.add("Semua Supplier");
							Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
							for(int d=0; d<vt_supp.size(); d++){
								ContactList cnt = (ContactList)vt_supp.get(d);
								String cntName = cnt.getCompName();
								val_supplier.add(String.valueOf(cnt.getOID()));
								key_supplier.add(cntName);  
							}
							String select_supplier = ""+srcReportStockOpname.getSupplierId(); 
						  %>
                          <%=ControlCombo.draw(frmSrcReportStockOpname.fieldNames[frmSrcReportStockOpname.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%> 
						</td>
                      </tr>
                      <tr> 
                        <td height="21" width="9%"><%=getJspTitle(2,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
						<%
							Vector materGroup = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
							Vector vectGroupVal = new Vector(1,1);
							Vector vectGroupKey = new Vector(1,1);
							vectGroupVal.add("Semua Kategori");
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
							out.println(ControlCombo.draw(frmSrcReportStockOpname.fieldNames[frmSrcReportStockOpname.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcReportStockOpname.getCategoryId(), vectGroupKey, vectGroupVal, null));
						%>
						</td>
                      </tr>
                      <tr> 
                        <td height="21" width="9%"><%=getJspTitle(3,SESS_LANGUAGE,"",true)%></td>
                        <td height="21" width="1%">:</td>
                        <td height="21" width="90%">&nbsp;
                          <input type="text" name="txt_subcategory"  value="" class="formElemen" size="30">
                          <a href="javascript:cmdSelectSubCategory()">chk</a> 
                          <input type="hidden" name="<%=frmSrcReportStockOpname.fieldNames[FrmSrcReportStockOpname.FRM_FIELD_SUB_CATEGORY_ID]%>"  value="">
                        </td>
                      </tr>
                      <tr> 
                        <td height="43" rowspan="2" valign="top" width="9%" align="left"><%=getJspTitle(4,SESS_LANGUAGE,"",true)%></td>
                        <td height="43" rowspan="2" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left"> &nbsp; 
                          From <%=ControlDate.drawDate(frmSrcReportStockOpname.fieldNames[FrmSrcReportStockOpname.FRM_FIELD_DATE_FROM], srcReportStockOpname.getDateFrom(),"formElemen",1,-5)%> 
                          to <%=	ControlDate.drawDate(frmSrcReportStockOpname.fieldNames[FrmSrcReportStockOpname.FRM_FIELD_DATE_TO], srcReportStockOpname.getDateTo(),"formElemen",1,-5) %> 
                        </td>
                      </tr>
                      <tr align="left"> 
                        <td height="21" width="90%" valign="top">  </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="9%" align="left"><%="Report to View"%></td>
                        <td height="19" valign="top" width="1%" align="left"></td>
                        <td height="19" width="90%" valign="top" align="left"> 
                          <% 
							Vector key_sort = new Vector(1,1); 						  
							Vector val_sort = new Vector(1,1); 
							key_sort.add("0");
							val_sort.add("Lost By Cost");
							key_sort.add("1");
							val_sort.add("Lost By Price");
							out.println("&nbsp;"+ControlCombo.draw("open_report", null, "0",key_sort,val_sort,"","formElemen"));
 						  %>
                        </td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Sale Report",ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td nowrap>&nbsp;</td>
                              <td class="command" nowrap><a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Opname Report",ctrLine.CMD_SEARCH,true)%></a></td>
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
