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
<%@ page import = "com.dimata.cashier.entity.billing.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
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
SrcReportStockOpname srcReportStockOpname = new SrcReportStockOpname();

Vector report = new Vector(1,1);
Vector hasil = new Vector(1,1);
try
{
	report = (Vector)session.getValue("SESS_MAT_STOCK_OPNAME_REPORT_INVOICE");
	
	srcReportStockOpname = (SrcReportStockOpname)report.get(0);
	hasil = (Vector)report.get(1);
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
<!--



function cmdEdit(oid)
{
}

function cmdListFirst()
{
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportsale_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev()
{
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportsale_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext()
{
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportsale_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast()
{
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportsale_list.jsp";
	document.frm_reportsale.submit();
}

function cmdBack()
{
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportsale.jsp";
	document.frm_reportsale.submit();
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------//-->
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<link rel="stylesheet" href="../../../styles/print.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr> 
    <td width="88%" valign="top" align="left" height="56"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
				  <tr>
					<td>
					  <table width="100%" border="0" cellspacing="0" cellpadding="0">
						  <tr align="center">
							<td width="25%">&nbsp;</td>
							<td class="listgensell" width="50%"><b>LAPORAN STOCK OPNAME PER INVOICE</b></td>
							<td width="25%">&nbsp;</td>
						  </tr>
						  <tr align="center">
							<td width="25%">&nbsp;</td>
							<td class="listgensell" width="50%"><b><%=Formater.formatDate(srcReportStockOpname.getDateFrom(), "dd-MM-yyyy")%> <%= " sd " + Formater.formatDate(srcReportStockOpname.getDateTo(), "dd-MM-yyyy")%></b></td>
							<td width="25%">&nbsp;</td>
						  </tr>
					  </table>
					</td>
				  </tr>
					<%
					if (srcReportStockOpname.getLocationId() != 0)
					{
						try
						{
							Location lokasi = PstLocation.fetchExc(srcReportStockOpname.getLocationId());
						%>
					<tr align="left" valign="top">
					  <td class="listgensell" valign="middle" >
						<b>&nbsp;<%=lokasi.getName()%> </b> </td>
					</tr>
					<%
							}
							catch(Exception exx)
							{
							}
						}
					%>
				  <tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<%
							if (srcReportStockOpname.getSupplierId() != 0)
							{
								try
								{
									ContactList cnt = PstContactList.fetchExc(srcReportStockOpname.getSupplierId());
								%>
							  <tr>
							  <td width="15%" class="listgensell" valign="middle" > 
								<b>&nbsp;Supplier</b>
							  </td>
							  <td width="3%" class="listgensell" valign="middle"> 
								<b>:</b>
							  </td>
							  <td width="82%" class="listgensell" valign="middle"> 
								<b><%=cnt.getCompName()%> </b> </td>
							  </tr>
							<%
								}
								catch(Exception exx)
								{
								}
							}
							%>
							<%
							if (srcReportStockOpname.getCategoryId() != 0)
							{
								try
								{
									Category kategori = PstCategory.fetchExc(srcReportStockOpname.getCategoryId());
								%>
							  <tr>
							  <td width="15%" class="listgensell" valign="middle"> 
								<b>&nbsp;Kategori</b>
							  </td>
							  <td width="3%" class="listgensell" valign="middle"> 
								<b>:</b>
							  </td>
							  <td width="82%" class="listgensell" valign="middle"> 
								<b><%=kategori.getName()%> </b> </td>
							  </tr>
							<%
								}
								catch(Exception exx)
								{
								}
							}
							%>
									<%
									if (srcReportStockOpname.getSubCategoryId() != 0)
									{
										try
										{
											SubCategory skat = PstSubCategory.fetchExc(srcReportStockOpname.getSubCategoryId());
									%>
									  <tr>
									  <td width="15%" class="listgensell" valign="middle"> 
										<b>&nbsp;Sub Kategori</b>
									  </td>
									  <td width="3%" class="listgensell" valign="middle"> 
										<b>:</b>
									  </td>
									  <td width="82%" class="listgensell" valign="middle"> 
										<b><%=skat.getName()%> </b> </td>
									  </tr>
									<%
										}
										catch(Exception exx)
										{
										}
									}
									%>
						</table>
					</td>
				  </tr>
					<tr align="left" valign="top"> 
					  <td valign="middle" colspan="0">
						<%
						  	for (int i=0; i<hasil.size();i++)
						  	{
						  		out.println((String)hasil.get(i));
							}
						%>
					  </td>
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
