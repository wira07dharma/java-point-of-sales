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
<!--pos versi 1-->
<!--%@ page import = "com.dimata.cashier.entity.billing.*" %-->
<!--%@ page import = "com.dimata.cashier.entity.payment.*" %-->

<!--pos versi 2-->
<%@ page import = "com.dimata.pos.entity.billing.*" %>
<%@ page import = "com.dimata.pos.entity.payment.*" %>

<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DAILY_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No","Tanggal","Total"},
	{"No","Date","Total"}
};


public static final String textListHeader[][] = 
{
	{"LAPORAN REKAP HARIAN"},
	{"SUMMARY DAILY REPORT"}
};

public String drawList(int language,Vector objectClass)
{
	String result = "";
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("600%");
		ctrlist.setListStyle("listgensell");
		ctrlist.setTitleStyle("listgensell");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgensell");
		ctrlist.addHeader(textListMaterialHeader[language][0],"3%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"15%");
	
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		double totalSale = 0.00;
		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			String tanggal = (String)vt.get(0);
			Double totalRekap = (Double)vt.get(1);
			
			rowx.add(""+(i+1));
			rowx.add("<div align=\"center\">"+tanggal+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</div>");			
			totalSale += totalRekap.doubleValue();
			lstData.add(rowx);

			lstLinkData.add("");
		}
		//Add TOTAL
		Vector rowx = new Vector();				
		rowx.add("");
		rowx.add("TOTAL");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalSale)+"</div>");			
		lstData.add(rowx);
		result = ctrlist.draw();
	}
	else
	{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;No data available ...</div>";		
	}
	return result;
}
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
SrcReportSale srcReportSale = new SrcReportSale();

Vector report = new Vector(1,1);
Vector hasil = new Vector(1,1);
try
{
	report = (Vector)session.getValue("SESS_MAT_SALE_REPORT_REKAP");
	srcReportSale = (SrcReportSale)report.get(0);
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
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
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
							
                        <td class="listgensell" width="50%"><b><%=textListHeader[SESS_LANGUAGE][0]%></b></td>
							<td width="25%">&nbsp;</td>
						  </tr>
						  <tr align="center">
							<td width="25%">&nbsp;</td>
							<td class="listgensell" width="50%"><b>
						<%
							if(!Validator.isEqualsDate(srcReportSale.getDateFrom(),srcReportSale.getDateTo())){
								out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy")+" s/d " + Formater.formatDate(srcReportSale.getDateTo(), "dd-MM-yyyy"));
							}else{
								out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy"));
							}
						%>
							</b></td>
							<td width="25%">&nbsp;</td>
						  </tr>
					  </table>
					</td>
				  </tr>
					<%
                                        String namaLokasi = "";
                                        if (srcReportSale.getLocationMultiple().length()>0){
                                            try{
                                                String[] parts = srcReportSale.getLocationMultiple().split(",");
                                                for (int i = 0; i<parts.length;i++){
                                                    Location entLocation = new Location();
                                                    long oidLocation = Long.parseLong(parts[i]);
                                                    entLocation = PstLocation.fetchExc(oidLocation);
                                                    
                                                    if (namaLokasi.length()>0){
                                                        namaLokasi += ","+entLocation.getName()+"";
                                                    }else{
                                                        namaLokasi = entLocation.getName();
                                                    }    
                                                }
                                            }catch(Exception ex){
                                                namaLokasi="";
                                            }
                                        }else if (srcReportSale.getLocationId() != 0){
                                            Location entLocation = new Location();
                                            try{
                                                entLocation = PstLocation.fetchExc(srcReportSale.getLocationId());
                                                namaLokasi = entLocation.getName();
                                            }catch(Exception ex){
                                                namaLokasi="";
                                            }
                                            
                                        }
                                        
                                        if (namaLokasi.length()>0){
                                            String html = "";
                                            
                                            html += ""
                                            + "<tr align='left' valign='top'>"
                                                + "<td class='listgensell' valign='middle'><b>&nbsp;Lokasi : "+namaLokasi+"</b></td>"
                                            + "</tr>";
                                            
                                            out.println(html);
                                        }
                                        
					
					%>
				  <tr>
					<td>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<%
							if (srcReportSale.getSupplierId() != 0)
							{
								try
								{
									ContactList cnt = PstContactList.fetchExc(srcReportSale.getSupplierId());
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
							if (srcReportSale.getCategoryId() != 0)
							{
								try
								{
									Category kategori = PstCategory.fetchExc(srcReportSale.getCategoryId());
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
							if (srcReportSale.getSubCategoryId() != 0)
							{
								try
								{
									SubCategory skat = PstSubCategory.fetchExc(srcReportSale.getSubCategoryId());
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
							<%
							if (srcReportSale.getShiftId() != 0)
							{
								try
								{
									Shift shf = PstShift.fetchExc(srcReportSale.getShiftId());
							%>
							  <tr>
							  <td width="15%" class="listgensell" valign="middle"> 
								<b>&nbsp;Shift</b>
							  </td>
							  <td width="3%" class="listgensell" valign="middle"> 
								<b>:</b>
							  </td>
							  <td width="82%" class="listgensell" valign="middle"> 
								<b><%=shf.getName()%> </b> </td>
							  </tr>
							<%
								}
								catch(Exception exx)
								{
								}
							}
							%>
							<%
							if (srcReportSale.getOperatorId() != 0)
							{
								try
								{
									AppUser usr = PstAppUser.fetch(srcReportSale.getOperatorId());
							%>
							  <tr>
							  <td width="15%" class="listgensell" valign="middle"> 
								<b>&nbsp;Operator</b>
							  </td>
							  <td width="3%" class="listgensell" valign="middle"> 
								<b>:</b>
							  </td>
							  <td width="82%" class="listgensell" valign="middle"> 
								<b><%=usr.getFullName()%> </b> </td>
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
