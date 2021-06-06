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
	{"No","Tanggal","Total HPP", "Total Disc","Total Jual","Total Profit","Total Tax","Total Service"},
	{"No","Date","Total Cost","Total Disc","Total Sale","Total Profit","Total Tax","Total Service"}
};

public static final String textListHeader[][] = 
{
	{"LAPORAN REKAP HARIAN","Laporan Rekap Harian"},
	{"SUMMARY DAILY REPORT","Summary Daily Report"}
};

public static final String textButton[][] = 
{
	{"Rekap Harian"},
	{"Daily Report"}
};

public Vector drawLineHorizontal()
{
	Vector rowx = new Vector();
	//Add Under line
	rowx.add("-");
	rowx.add("-------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");

        rowx.add("---------------------------------------");
	rowx.add("<div align=\"right\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"right\">"+"-"+"</div>");
        return rowx;
}

public Vector drawHeader(int language)
{
	Vector rowx = new Vector();
	//Add Header
	rowx.add("|");
	rowx.add(textListMaterialHeader[language][0]);
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][4]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][2]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][5]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][6]+"</div>");
	rowx.add("<div align=\"right\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][7]+"</div>");
	rowx.add("<div align=\"right\">"+"|"+"</div>");
        return rowx;
}

public Vector drawLineTotal(){
	Vector rowx = new Vector();
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
        
        // tax service
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
        
	return rowx;
}
	
public Vector drawLineSingleSpot(){
	Vector rowx = new Vector();
	rowx.add("-");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
        
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
        return rowx;
}
	
public Vector drawLineTotalSide()
{
	Vector rowx = new Vector();
	rowx.add("|");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
        
        // tax service
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"right\">"+"|"+"</div>");
	rowx.add("--------------------------------------");
	rowx.add("<div align=\"right\">"+"|"+"</div>");

        return rowx;
}
	
public Vector drawList(int language,Vector objectClass)
{
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("80%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","3%","0","0","center","middle");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","10%","0","0","center","middle");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","15%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","15%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","15%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","center","middle");
                
                // tax service
		ctrlist.dataFormat("","15%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","center","middle");
		ctrlist.dataFormat("","15%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","center","middle");
	
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		double totalJual = 0.00;
		double totalCost = 0.00;
		double totalPotongan = 0.00;
		double totalProfit = 0.00;
                
                double totalTaxVal = 0.00;
                double totalServiceVal = 0.00;
                
		boolean firstRow = true;
		int baris = 2;
		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			String tanggal = (String)vt.get(0);
			Double totalRekap = (Double)vt.get(1);
			Double totalHPP = (Double)vt.get(2);
			Double totalDisc = (Double)vt.get(3);
			
                        Double totalTax = (Double)vt.get(4);
                        Double totalService = (Double)vt.get(5);
                        
			if (firstRow == true)
			{
				firstRow = false;
				//Draw Header
				lstData.add(drawLineHorizontal());
				baris+=1;
				lstData.add(drawHeader(language));
				baris+=1;
				lstData.add(drawLineHorizontal());
				baris+=1;
			}
			rowx.add("|");
			rowx.add(""+(i+1));
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"center\">"+tanggal+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue())+"</div>"); // 
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalHPP.doubleValue())+"</div>");			
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalRekap.doubleValue() - totalHPP.doubleValue())+"</div>");			
			rowx.add("<div align=\"center\">"+"|"+"</div>");
                        
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalTax.doubleValue())+"</div>");			
			rowx.add("<div align=\"right\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalService.doubleValue())+"</div>");			
			rowx.add("<div align=\"right\">"+"|"+"</div>");

                        lstData.add(rowx);
			
			totalJual += totalRekap.doubleValue();
			totalCost += totalHPP.doubleValue();
			totalPotongan += totalDisc.doubleValue();
			totalProfit += (totalRekap.doubleValue() - totalHPP.doubleValue());
                        totalTaxVal += totalTax.doubleValue();
                        totalServiceVal += totalService.doubleValue();
                               
			baris+=1;
			
				if (baris == 78)
				{
					//Add line
					lstData.add(drawLineHorizontal());
					//Add header for next page and restart counting baris
					lstData.add(drawLineHorizontal());
					baris = 1;
					lstData.add(drawHeader(language));
					baris += 1;
					lstData.add(drawLineHorizontal());
					baris += 1;
				}

			lstLinkData.add("");
		}
		lstData.add(drawLineHorizontal());
		//Add TOTAL
		Vector rowx = new Vector();				
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("<div align=\"right\">"+"TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalJual)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalCost)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalProfit)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
                
                // total tax service
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalTaxVal)+"</div>");// 
		rowx.add("<div align=\"right\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalServiceVal)+"</div>");// 
		rowx.add("<div align=\"right\">"+"|"+"</div>");
                
		lstData.add(rowx);
		lstData.add(drawLineTotal());
		result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
	}
	else
	{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;No data available ...</div>");
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
SessReportSale sessReportSale = new SessReportSale();
FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcReportSale);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 { 
		srcReportSale = (SrcReportSale)session.getValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP); 
		if (srcReportSale == null) srcReportSale = new SrcReportSale();
	 }
	 catch(Exception e)
	 { 
		srcReportSale = new SrcReportSale();
	 }
}
else
{
	 frmSrcReportSale.requestEntityObject(srcReportSale);
	 session.putValue(SessReportSale.SESS_SRC_REPORT_SALE_REKAP, srcReportSale);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector records = sessReportSale.getReportSaleRekapTanggal(srcReportSale);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	//start = ctrlReportSale.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
**/
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
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
	document.frm_reportsale.action="reportsalerekaptanggal_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev()
{
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportsalerekaptanggal_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext()
{
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportsalerekaptanggal_list.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast()
{
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportsalerekaptanggal_list.jsp";
	document.frm_reportsale.submit();
}

function cmdBack()
{
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportsalerekaptanggal.jsp";
	document.frm_reportsale.submit();
}

function printForm(){
	window.open("reportsalerekaptanggal_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printFormExcel(){
	window.open("reportsalerekaptanggal_list_excel.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

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
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css"
rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
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

</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><%=textListHeader[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="3"> <hr size="1"> </td>
                </tr>
                <tr align="left" valign="top"> 
                    <td height="14" valign="middle" colspan="3" align="center"><b><%=textListHeader[SESS_LANGUAGE][0]%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center" ><b> 
                    <%
							if(!Validator.isEqualsDate(srcReportSale.getDateFrom(),srcReportSale.getDateTo())){
								out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy")+" s/d " + Formater.formatDate(srcReportSale.getDateTo(), "dd-MM-yyyy"));
							}else{
								out.println(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy"));
							}
						%>
                    </b></td>
                </tr>
                <%
				if (srcReportSale.getLocationId() != 0)
				{
					try
					{
						Location lokasi = PstLocation.fetchExc(srcReportSale.getLocationId());
					%>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Location : " + lokasi.getName()%> </b> </td>
                </tr>
                <%
						}
						catch(Exception exx)
						{
						}
					}
				%>
                <%
				if (srcReportSale.getSupplierId() != 0)
				{
					try
					{
						ContactList cnt = PstContactList.fetchExc(srcReportSale.getSupplierId());
					%>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Supplier : " + cnt.getCompName()%> </b> </td>
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
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Category : " + kategori.getName()%> </b> </td>
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
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Shift : " + shf.getName()%> </b> </td>
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
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Cashier : " + usr.getFullName()%> </b> </td>
                </tr>
                <%
				}
				catch(Exception exx)
				{
				}
			}
			%>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%
					Vector hasil = drawList(SESS_LANGUAGE,records);
					Vector report = new Vector(1,1);
					report.add(srcReportSale);
					report.add(hasil);
					
					for(int k=0;k<hasil.size();k++){
						out.println(hasil.get(k));
					}
					
					try
					{
						session.putValue("SESS_MAT_SALE_REPORT_REKAP",report);
					}
					catch(Exception e){}
					%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    <%
					//ctrLine.setLocationImg(approot+"/images");
					//ctrLine.initDefault();
					//out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="58%"> <table width="75%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="2%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Sale Report",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="98%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textButton[SESS_LANGUAGE][0],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                        <td width="42%"> <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="45%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" >Print <%=textButton[SESS_LANGUAGE][0]%></a></td>
                              <td width="5%" valign="top"><a href="javascript:printFormExcel()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="45%" nowrap>&nbsp; <a href="javascript:printFormExcel()" class="command" >Export Excel</a></td>
                           
                            </tr>
                          </table></td>
                      </tr>
                    </table></td>
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
