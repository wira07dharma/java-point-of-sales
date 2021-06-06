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
<% int  appObjCode = 1;//AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = 
{
	{"Lokasi","Consignor","Kategori","Tanggal","Sku","Merk","Periode","Semua"},
	{"Location","Consignor","Category","Date","Code","Merk","Periode","All"}
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
int tp = FRMQueryString.requestInt(request,"type");
    int type = PstLocation.TYPE_LOCATION_WAREHOUSE;
    if(tp==1){
        type = PstLocation.TYPE_LOCATION_STORE;
    }


ControlLine ctrLine = new ControlLine();

SrcReportStock srcReportStock = new SrcReportStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock();
try
{
	srcReportStock = (SrcReportStock)session.getValue(SessReportStock.SESS_SRC_REPORT_STOCK);
}
catch(Exception e)
{
	srcReportStock = new SrcReportStock();
}


if(srcReportStock==null)
{
	srcReportStock = new SrcReportStock();
}

try
{
	session.removeValue(SessReportStock.SESS_SRC_REPORT_STOCK_LIST);
}
catch(Exception e)
{
}

Periode periode = new Periode();
try{
    periode = PstPeriode.getPeriodeRunning();
}catch(Exception e){}
%>
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
function cmdSearch() {
	document.frmsrcreportstock.command.value="<%=Command.LIST%>";
	document.frmsrcreportstock.action="reportstocksupplier_list.jsp";
	document.frmsrcreportstock.submit();
}

function cmdSelectSubCategory() {
	window.open("subcategorydosearch.jsp?command=<%=Command.FIRST%>&txt_subcategory="+document.frmsrcreportstock.txt_subcategory.value+
			"&oidCategory="+document.frmsrcreportstock.<%=FrmSrcReportStock.fieldNames[FrmSrcReportStock.FRM_FIELD_CATEGORY_ID]%>.value+
			"&caller=1",
			"material", "height=600,width=600,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes");
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
            Konsinyasi > Laporan Stok Barang<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreportstock" method="post" action="">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="type" value="<%=tp%>">
              <input type="hidden" name="<%=frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_PERIODE_ID]%>" value="<%=periode.getOID()%>">
              
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
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1); 
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + type,PstLocation.fieldNames[PstLocation.FLD_CODE]);
							Vector vt_loc = PstLocation.list(0,0,"",PstLocation.fieldNames[PstLocation.FLD_CODE]);
							val_locationid.add("0");
							key_locationid.add(textListHeader[SESS_LANGUAGE][7]+" "+textListHeader[SESS_LANGUAGE][0]);
							for(int d=0;d<vt_loc.size();d++) {
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
						 %> <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_LOCATION_ID], null, ""+srcReportStock.getLocationId(), val_locationid, key_locationid, "", "formElemen")%>
						</td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="90%" valign="top" align="left">&nbsp; 
                          <% 
							Vector val_supplier = new Vector(1,1);
							Vector key_supplier = new Vector(1,1);
							
                            String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                             " = "+PstContactClass.CONTACT_TYPE_SUPPLIER+
                                             " AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
                                             " != "+PstContactList.DELETE;
                            Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
                                                            val_supplier.add(""+0);
                                                            key_supplier.add("");  
							  
							//Vector vt_supp = PstContactList.list(0,0,"",PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
							if(vt_supp!=null && vt_supp.size()>0){
								for(int d=0; d<vt_supp.size(); d++){
									ContactList cnt = (ContactList)vt_supp.get(d);
									String cntName = cnt.getCompName();
									val_supplier.add(String.valueOf(cnt.getOID()));
									key_supplier.add(cntName);  
								}
							}
							String select_supplier = ""+srcReportStock.getSupplierId();
						  %>
                          <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_SUPPLIER_ID],null,select_supplier,val_supplier,key_supplier,"","formElemen")%> 
                        </td>
                      </tr>
                      <tr> 
                        <td height="19" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="90%" valign="top" align="left">&nbsp; 
                          <% 
                            Vector val_categ = new Vector(1,1);
                            Vector key_categ = new Vector(1,1);
                                val_categ.add(""+0);
                                key_categ.add("");  
							  
                                Vector vt_categ = PstCategory.list(0,0,"",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                                if(vt_categ!=null && vt_categ.size()>0){
                                        for(int d=0; d<vt_categ.size(); d++){
                                                Category category = (Category)vt_categ.get(d);
                                                val_categ.add(String.valueOf(category.getOID()));
                                                key_categ.add(category.getName());  
                                        }
                                }
                                String select_categ = ""+srcReportStock.getCategoryId();
                          %>
                          <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_CATEGORY_ID],null,select_categ,val_categ,key_categ,"","formElemen")%> 
                        </td>
                      </tr>                      
                      <tr> 
                        <td height="19" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][5]%></td>
                        <td height="19" valign="top" width="1%" align="left">:</td>
                        <td height="19" width="90%" valign="top" align="left">&nbsp; 
                          <% 
                            Vector val_merk = new Vector(1,1);
                            Vector key_merk = new Vector(1,1);
                                val_merk.add(""+0);
                                key_merk.add("");  
							  
                                Vector vt_merk = PstMerk.list(0,0,"",PstMerk.fieldNames[PstMerk.FLD_NAME]);
                                if(vt_merk!=null && vt_merk.size()>0){
                                        for(int d=0; d<vt_merk.size(); d++){
                                                Merk merk = (Merk)vt_merk.get(d);
                                                val_merk.add(String.valueOf(merk.getOID()));
                                                key_merk.add(merk.getName());  
                                        }
                                }
                                String select_merk = ""+srcReportStock.getMerkId();
                          %>
                          <%=ControlCombo.draw(frmSrcReportStock.fieldNames[frmSrcReportStock.FRM_FIELD_MERK_ID],null,select_merk,val_merk,key_merk,"","formElemen")%> 
                        </td>
                      </tr>                      
                       <tr>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                        <td height="21" valign="top" align="left">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td width="4%" nowrap><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Laporan Stok",ctrLine.CMD_SEARCH,true)%>"></a></td>
                              <td width="96%" nowrap class="command">&nbsp;&nbsp;<a href="javascript:cmdSearch()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Laporan Stok",ctrLine.CMD_SEARCH,true)%></a></td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate -->
</html>
