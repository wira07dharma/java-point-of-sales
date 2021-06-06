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
<%//@ page import = "com.dimata.cashier.entity.billing.*" %>
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1;// AppObjInfo.composeObjCode(AppObjInfo.--, AppObjInfo.--, AppObjInfo.--); %>
<%//@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"No","Tanggal","SKU","Nama","Harga Beli","Qty Opn","Qty Jual","Qty Sys","Selisih","Total Lost"},
	{"No","Date","Code","Name","Cost","Opn Qty","Sld Qty","Sys Qty","Blnc","Total Lost"}
};

public Vector drawLineHorizontal()
{
	Vector rowx = new Vector();
	//Add Under line
	rowx.add("-");
	rowx.add("-------");
	rowx.add("--------------------------");
	rowx.add("------------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("------------------------");
	rowx.add("-");
	return rowx;
}

public Vector drawHeader(int language)
{
	Vector rowx = new Vector();
	//Add Header
	rowx.add("|");
	rowx.add(textListMaterialHeader[language][0]);
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][4]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][5]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][6]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][7]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][8]+"</div>");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"right\">"+textListMaterialHeader[language][9]+"</div>");
	rowx.add("|");
	return rowx;
}

public Vector drawLineTotal()
{
	Vector rowx = new Vector();
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
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("------------------------");
	rowx.add("-");
	return rowx;
}
	
public Vector drawLineSingleSpot()
{
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
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("------------------------");
	rowx.add("|");
	return rowx;
}
	
public Vector drawList(int language,Vector objectClass, boolean isCategory,
						boolean isSubCategory, boolean isSupplier)
{
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("90%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","3%","2","0","center","top");
		ctrlist.dataFormat("","11%","2","0","center","top");
		ctrlist.dataFormat("","18%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","9%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","5%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","5%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","5%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","5%","2","0","center","top");
		ctrlist.dataFormat("","5%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
		ctrlist.dataFormat("","10%","2","0","center","top");
		ctrlist.dataFormat("","1%","2","0","center","top");
	
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		double totalLost = 0.00;
		double subTotalLost = 0.00;
		int totalQty = 0;
		int subTotalQty = 0;
		String subCategory = "";
		boolean firstRow = true;
		int baris = 0;
		int countTrue = 0;
		//Tentukan nilai baris untuk halaman pertama
		if (isSupplier == true)
		{
			countTrue += 1;
		}
		if (isCategory == true)
		{
			countTrue += 1;
		}
		if (isSubCategory == true)
		{
			countTrue += 1;
		}
		switch (countTrue)
		{
			case 0:
				baris = 3;
				break;
			case 1:
				baris = 4;
				break;
			case 2:
				baris = 5;
				break;
			case 3:
				baris = 6;
				break;
		}

		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			MatStockOpnameItem soi = (MatStockOpnameItem)vt.get(0);
			Material mat = (Material)vt.get(1);
			Category cat = (Category)vt.get(2);
			SubCategory scat = (SubCategory)vt.get(3);
			Unit unt = (Unit)vt.get(4);
			double qtyLost = soi.getQtySystem() - (soi.getQtyOpname() + soi.getQtySold());	
			String nama_barang = mat.getName();
			if (nama_barang.length() >= 22)
			{
				nama_barang = nama_barang.substring(0,22);
			}
			String unit_name = unt.getCode();
			if (unit_name.length() >= 5)
			{
				unit_name = unit_name.substring(0,5);
			}

			//Tambahkan header tanggal dan invoice
			if (subCategory.equals(scat.getName()) == false)
			{
				subCategory = scat.getName();
				if (firstRow == true)
				{
					firstRow = false;
					//Add header only
					//Add Under line
					if ((isCategory == true) && (isSubCategory == true))
					{
					}
					else
					{
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					//Add kategori
					if (isCategory == false)
					{
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Kategori");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cat.getName()+"</div>");
						rowx.add("|");
						lstData.add(rowx);
						baris += 1;
					}
					if (isSubCategory == false)
					{	
						//Add sub kategori
						rowx = new Vector();
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Sub Kategori");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+scat.getName()+"</div>");
						rowx.add("|");
						lstData.add(rowx);
						baris += 1;
					}
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
					//Add Header
					lstData.add(drawHeader(language));
					baris += 1;
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
					rowx = new Vector();
				}
				else
				{
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
						if (baris == 72)
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
					//Add sub total then header
					//Add sub total
					rowx.add("|");
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
					rowx.add("<div align=\"right\">"+"Total"+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+subTotalQty+"</div>");
					rowx.add("");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalLost)+"</div>");
					rowx.add("|");
					lstData.add(rowx);
					baris += 1;
						if (baris == 72)
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
					//Add Under line
					lstData.add(drawLineTotalSide());
					baris += 1;
						if (baris == 72)
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
					rowx = new Vector();
					if (isCategory == false)
					{
						//Add kategori
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Kategori");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cat.getName()+"</div>");
						rowx.add("|");
						baris += 1;
						lstData.add(rowx);
						if (baris == 72)
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
						}					}	
					if (isSubCategory == false)
					{
						//Add sub kategori
						rowx = new Vector();
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Sub Kategori");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+scat.getName()+"</div>");
						rowx.add("|");
						baris += 1;
						lstData.add(rowx);
						if (baris == 72)
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
						}					}	
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
						if (baris == 72)
						{
							//Add line
							lstData.add(drawLineSingleSpot());
							//Add header for next page and restart counting baris
							lstData.add(drawLineHorizontal());
							baris = 1;
							lstData.add(drawHeader(language));
							baris += 1;
							lstData.add(drawLineHorizontal());
							baris += 1;
						}
					rowx = new Vector();
					subTotalLost = 0.00;
					subTotalQty = 0;
				}
			}

			rowx.add("|");
			rowx.add(""+(i+1));
			rowx.add(mat.getSku());
			rowx.add(nama_barang);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(soi.getCost())+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+soi.getQtyOpname()+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+soi.getQtySold()+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+soi.getQtySystem()+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+qtyLost+"</div>");
			rowx.add(unit_name);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qtyLost * soi.getCost())+"</div>");
			rowx.add("|");
			lstData.add(rowx);
			baris += 1;
				if (baris == 72)
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
			totalLost += (soi.getCost() * qtyLost);
			totalQty += qtyLost;
			subTotalLost += (soi.getCost() * qtyLost);
			subTotalQty += qtyLost;

			lstLinkData.add("");
		}
		//Add Under line
		lstData.add(drawLineHorizontal());
		//Add SUB TOTAL
		Vector rowx = new Vector();				
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
		rowx.add("<div align=\"right\">"+"Total"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+subTotalQty+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalLost)+"</div>");
		rowx.add("|");
		lstData.add(rowx);
		//Add Under line
		lstData.add(drawLineTotal());
		//Add TOTAL
		rowx = new Vector();				
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
		rowx.add("<div align=\"right\">"+"Total"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+totalQty+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalLost)+"</div>");
		rowx.add("|");
		lstData.add(rowx);
		//Add Under line
		lstData.add(drawLineTotal());
		result = ctrlist.drawMePartVector(0, lstData.size(),rowx.size());
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
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcReportStockOpname srcReportStockOpname = new SrcReportStockOpname();
SessReportStockOpname sessReportStockOpname = new SessReportStockOpname();
FrmSrcReportStockOpname frmSrcReportStockOpname = new FrmSrcReportStockOpname(request, srcReportStockOpname);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 { 
		srcReportStockOpname = (SrcReportStockOpname)session.getValue(SessReportStockOpname.SESS_SRC_REPORT_STOCK_OPNAME); 
		if (srcReportStockOpname == null) srcReportStockOpname = new SrcReportStockOpname();
	 }
	 catch(Exception e)
	 { 
		srcReportStockOpname = new SrcReportStockOpname();
	 }
}
else
{
	 frmSrcReportStockOpname.requestEntityObject(srcReportStockOpname);
	 session.putValue(SessReportStockOpname.SESS_SRC_REPORT_STOCK_OPNAME, srcReportStockOpname);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector records = sessReportStockOpname.getReportStockOpnameTotal(srcReportStockOpname);
vectSize = records.size();
System.out.println("vectSize "+vectSize);
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	//start = ctrlReportStockOpname.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
*/

%>
<!-- End of Jsp Block -->

<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
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
	document.frm_reportstockopname.command.value="<%=Command.FIRST%>";
	document.frm_reportstockopname.action="reportstockopname_list.jsp";
	document.frm_reportstockopname.submit();
}

function cmdListPrev()
{
	document.frm_reportstockopname.command.value="<%=Command.PREV%>";
	document.frm_reportstockopname.action="reportstockopname_list.jsp";
	document.frm_reportstockopname.submit();
}

function cmdListNext()
{
	document.frm_reportstockopname.command.value="<%=Command.NEXT%>";
	document.frm_reportstockopname.action="reportstockopname_list.jsp";
	document.frm_reportstockopname.submit();
}

function cmdListLast()
{
	document.frm_reportstockopname.command.value="<%=Command.LAST%>";
	document.frm_reportstockopname.action="reportstockopname_list.jsp";
	document.frm_reportstockopname.submit();
}

function cmdBack()
{
	document.frm_reportstockopname.command.value="<%=Command.BACK%>";
	document.frm_reportstockopname.action="src_reportstockopname.jsp";
	document.frm_reportstockopname.submit();
}

function printForm(){
	window.open("reportstockopname_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">
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
            Stock Opname Report &gt; Item List <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportstockopname" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="3"> 
                    <hr size="1">
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
				if (srcReportStockOpname.getSupplierId() != 0)
				{
					try
					{
						ContactList cnt = PstContactList.fetchExc(srcReportStockOpname.getSupplierId());
						isSupplier = true;
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
				if (srcReportStockOpname.getCategoryId() != 0)
				{
					try
					{
						Category kategori = PstCategory.fetchExc(srcReportStockOpname.getCategoryId());
						isCategory = true;
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
				if (srcReportStockOpname.getSubCategoryId() != 0)
				{
					try
					{
						SubCategory skat = PstSubCategory.fetchExc(srcReportStockOpname.getSubCategoryId());
						isSubCategory = true;
					%>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b> &nbsp; <%= "Sub Category : " + skat.getName()%> </b> </td>
                </tr>
                <%
					}
					catch(Exception exx)
					{
					}
				}
			%>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command">&nbsp; 
                    <b> <%= "From : " + Formater.formatDate(srcReportStockOpname.getDateFrom(), "dd-MM-yyyy")%> <%= " To " + Formater.formatDate(srcReportStockOpname.getDateTo(), "dd-MM-yyyy")%> </b> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="comment">&nbsp;<u><%=SESS_LANGUAGE==com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT ? "Daftar Item" : "Item List"%></u></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">
				<%
					Vector hasil1 = drawList(SESS_LANGUAGE,records,isCategory,isSubCategory,isSupplier);
					Vector report = new Vector(1,1);
					report.add(srcReportStockOpname);
                                        out.println(hasil1);
                                        for(int k=0;k<hasil1.size();k++){
                                            out.println(hasil1.get(k));
                                        }
					report.add(hasil1);
					try
					{
						session.removeValue("SESS_MAT_STOCK_OPNAME_REPORT");
					}
					catch(Exception ex){}
					try
					{
						session.putValue("SESS_MAT_STOCK_OPNAME_REPORT",report);
					}
					catch(Exception e){}
					
				%>
				  </td>
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
                  <td height="18" valign="top" colspan="3"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="66%"> 
                          <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Sale Report",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Stock Opname Report",ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="34%"> 
                          <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" >Print 
                                Stock Opname Report</a></td>
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
