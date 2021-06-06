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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN_REPORT , AppObjInfo.OBJ_SUMMARY_SUPPLIER_RETURN_REPORT_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"NO","TANGGAL","NO INVOICE","NAMA SUPPLIER","QTY","JUMLAH"},
	{"NO","DATE","INVOICE NUMBER","SUPPLIER NAME","QTY","TOTAL COST"}
};

public Vector drawLineHorizontal()
{ 
	Vector rowx = new Vector();
	//Add Under line
	rowx.add("-"); //0
	rowx.add("--------");//1
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("----------------------------");//3
	rowx.add("<div align=\"center\">"+"-"+"</div>");//4
	rowx.add("------------------------------");//9
	rowx.add("<div align=\"center\">"+"-"+"</div>");//6
	rowx.add("---------------------------------------------------------------------");//5
	rowx.add("<div align=\"center\">"+"-"+"</div>");//8
	rowx.add("------------");//7
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("--------------------------------------------");//12
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	return rowx;
}

public Vector drawHeader(int language)
{
	Vector rowx = new Vector();
	//Add Header
	rowx.add("|");// 0
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");// 1
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 2
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");// 3
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 4
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");// 5
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 6
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");// 7
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 8
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");// 9
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 10
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");// 11
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 12
	return rowx;
}

public Vector drawLineTotal()
{
	Vector rowx = new Vector();
	rowx.add(""); //0
	rowx.add("");//1
	rowx.add("");//10
	rowx.add("");//3
	rowx.add("");//4
	rowx.add("");//5
	rowx.add("");//6
	rowx.add("");//9
	rowx.add("<div align=\"center\">"+"-"+"</div>");//8
	rowx.add("------------");//7
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("--------------------------------------------");//12
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
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
	return rowx;
}
	
public Vector drawLineTotalSide()
{
	Vector rowx = new Vector();
	rowx.add("-");
	rowx.add("--------");
	rowx.add("-");
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-----------------------------");
	rowx.add("-");	
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-----------------------------");
	rowx.add("-");
	return rowx;
}
	
public Vector drawList(int language,Vector objectClass, boolean isCategory,
					boolean isSubCategory, boolean isReason, boolean isSupplier)
{
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","3%","0","0","center","top"); // nomer
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","8%","0","0","center","top"); // sku
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","9%","0","0","right","bottom"); // harga beli barang default.
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","20%","0","0","center","top"); // nama barang
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","4%","0","0","right","bottom"); // qty
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","13%","0","0","right","bottom"); // total harga beli barang 
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
	
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		double totalSale = 0.00;
		double totalCost = 0.00;
		double subTotalSale = 0.00;
		double subTotalCost = 0.00;
		double totalQty = 0;
		double subTotalQty = 0;
		String tglJual = "";
		boolean firstRow = true;
		int baris = 0;
		int countTrue = 0;

		int maxlines = 72;
		int maxlinespgdst = 75;
		boolean boolmaxlines = true;

		for(int i=0; i<objectClass.size(); i++) 
		{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			String tanggal = (String)vt.get(0);
			long oidBill = ((Long)vt.get(1)).longValue();
			String Invoice = (String)vt.get(2);
			double hpp = ((Double)vt.get(3)).doubleValue();
			double price = ((Double)vt.get(4)).doubleValue();
			String alasan = (String)vt.get(5);
			double qty = ((Double)vt.get(6)).doubleValue();
			String supplier = (String)vt.get(7);
			if (supplier.length() > 25) supplier = supplier.substring(0,25);
				
			if (firstRow == true){
				firstRow = false;
				//Add header only
				lstData.add(drawLineHorizontal());
				baris += 1;
				lstData.add(drawHeader(language));
				baris += 1;
				lstData.add(drawLineHorizontal());
				baris += 1;
			}
			rowx.add("|");
			rowx.add(""+(i+1));
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(tanggal);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(Invoice);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(supplier);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(qty)+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(hpp)+"</div>");			
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			lstData.add(rowx);
			baris += 1;
					if (baris == maxlines)
					{
						if(boolmaxlines){
							maxlines = maxlinespgdst;
						}
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
			totalSale += price;
			totalCost += hpp;
			totalQty += qty;
			subTotalSale += price;
			subTotalCost += hpp;
			subTotalQty += qty;

			lstLinkData.add("");
		}
		lstData.add(drawLineHorizontal());
		//Add TOTAL
		Vector rowx = new Vector();		
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("<div align=\"right\">"+"TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalCost)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		lstData.add(rowx);
		lstData.add(drawLineTotal());
		result = ctrlist.drawMePartVector(0, lstData.size(),rowx.size());
	}
	else
	{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;Tidak ada data retur barang ...</div>");
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
boolean isReason = false;

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcReportReturn srcReportReturn = new SrcReportReturn();
SessReportReturn sessReportReturn = new SessReportReturn();
FrmSrcReportReturn frmSrcReportReturn = new FrmSrcReportReturn(request, srcReportReturn);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 { 
		srcReportReturn = (SrcReportReturn)session.getValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE); 
		if (srcReportReturn == null) srcReportReturn = new SrcReportReturn();
	 }
	 catch(Exception e)
	 { 
		srcReportReturn = new SrcReportReturn();
	 }
}
else
{
	 frmSrcReportReturn.requestEntityObject(srcReportReturn);
	 // Karena tidak memakai parameter REASON, maka property ini di-set -1
	 srcReportReturn.setReturnReason(-1);
	 session.putValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE, srcReportReturn);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector records = sessReportReturn.getReportReturnRekapInvoice(srcReportReturn);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	//start = ctrlReportReturn.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
*/

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack()
{
	document.frm_reportreturn.command.value="<%=Command.BACK%>";
	document.frm_reportreturn.action="src_reportreturninvoicerekap.jsp";
	document.frm_reportreturn.submit();
}

function printForm(){
	window.open("reportreturninvoicerekap_form_print.jsp","returnreportinvoicerekap","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            Rekap Retur Barang Ke Supplier Per Invoice<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportreturn" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="middle"> <hr size="1"> </td>
                </tr>
                <%
				if (srcReportReturn.getLocationId() != 0)
				{
					try
					{
						Location lokasi = PstLocation.fetchExc(srcReportReturn.getLocationId());
					%>
                <tr align="left" valign="top"> 
                  <td height="14" align="center" valign="middle"><strong>REKAP 
                    RETUR BARANG KE SUPPLIER PER INVOICE</strong></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" align="center" valign="middle"><%=Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")%> <%= " s/d " + Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" class="command"><strong>LOKASI 
                    : <%=lokasi.getName().toUpperCase()%></strong></td>
                </tr>
                <%
						}
						catch(Exception exx)
						{
						}
					}
				%>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle"> <%
					Vector hasil = drawList(SESS_LANGUAGE,records,isCategory,isSubCategory,isReason,isSupplier);
					Vector report = new Vector(1,1);
					report.add(srcReportReturn);
					report.add(hasil);
					try
					{
						session.putValue("SESS_MAT_RETURN_REPORT_INVOICE_REKAP",report);
					}
					catch(Exception e){}
					
					for(int k=0;k<hasil.size();k++){
						out.println(hasil.get(k));
					}
					%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top"> <table width="100%" border="0">
                      <tr> 
                        <td width="66%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="9%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,"Retur Barang",ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td nowrap width="4%">&nbsp;</td>
                              <td class="command" nowrap width="87%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,"Retur Barang",ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                        <td width="34%"> 
						  <%if(records!=null &&  records.size()>0){%>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" >Print 
                                Retur Barang</a></td>
                            </tr>
                          </table>
						  <%}else{%>
						  &nbsp;
						  <%}%>
						</td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
