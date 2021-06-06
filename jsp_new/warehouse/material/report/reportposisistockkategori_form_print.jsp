<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
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
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION_BY_CATEGORY); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
%>
<%
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
SrcReportStock srcReportStock = new SrcReportStock();

Vector report = new Vector(1,1);
Vector records = new Vector(1,1);
Vector hasil1 = new Vector();
try
{
	report = (Vector)session.getValue("SESS_MAT_STOCK_REPORT");
	
	srcReportStock = (SrcReportStock)report.get(0);
	hasil1 = (Vector)report.get(1);
}
catch(Exception e){}

%> 
<!-- End of Jsp Block -->
<html>
<!-- #BeginTemplate "/Templates/print.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdListFirst()
{
	document.frm_reportstock.command.value="<%=Command.FIRST%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListPrev()
{
	document.frm_reportstock.command.value="<%=Command.PREV%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListNext()
{
	document.frm_reportstock.command.value="<%=Command.NEXT%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdListLast()
{
	document.frm_reportstock.command.value="<%=Command.LAST%>";
	document.frm_reportstock.action="reportstock_list.jsp";
	document.frm_reportstock.submit();
}

function cmdBack()
{
	document.frm_reportstock.command.value="<%=Command.BACK%>";
	document.frm_reportstock.action="src_reportstock.jsp";
	document.frm_reportstock.submit();
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
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../../../styles/print.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
				
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b>LAPORAN POSISI 
                          STOCK DI GUDANG PER KATEGORI</b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                      <tr align="center"> 
                        <td width="25%">&nbsp;</td>
                        <td class="listgensell" width="50%"><b><%=Formater.formatDate(srcReportStock.getDateFrom(), "dd-MM-yyyy")%> <%= " s/d " + Formater.formatDate(srcReportStock.getDateTo(), "dd-MM-yyyy")%></b></td>
                        <td width="25%">&nbsp;</td>
                      </tr>
                    </table></td>
                </tr>
                <%
					Location lokasi = new Location();
					if (srcReportStock.getLocationId() != 0){
						try{
							lokasi = PstLocation.fetchExc(srcReportStock.getLocationId());
						}catch(Exception exx){}
					}
					
					Category categ = new Category();
					if (srcReportStock.getLocationId() != 0){
						try{
							categ = PstCategory.fetchExc(srcReportStock.getCategoryId());
						}catch(Exception exx){}
					}
					
					%>
                <tr align="left" valign="top">
                  <td class="listgensell" valign="middle" ><b>&nbsp;LOKASI : <%=lokasi.getName().toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td class="listgensell" valign="middle" > <b>&nbsp;KATEGORI 
                    : <%=categ.getName().toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="0"> <%
						  	for (int i=0; i<hasil1.size();i++)
						  	{
						  		out.println((String)hasil1.get(i));
							}
						%> </td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --> 
</html>

