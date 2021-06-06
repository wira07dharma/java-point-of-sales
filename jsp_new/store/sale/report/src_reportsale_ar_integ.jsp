<%-- 
    Document   : src_reportsale_detail_ar
    Created on : Feb 5, 2018, 3:34:54 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlCombo,
		 com.dimata.pos.entity.billing.*,
                 com.dimata.posbo.entity.masterdata.PstShift,
                 com.dimata.posbo.entity.masterdata.Shift,
                 com.dimata.pos.entity.masterCashier.*,
		 com.dimata.common.entity.payment.CurrencyType,
		 com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<%!
public static final String textListHeader[][] = 
{
	{"Lokasi","Shift","Kasir","Supplier","Kategori","Merk","Tanggal","Urut Berdasar","Kode Sales","Sub Kategori","Tipe Transaksi","Dikelompokkan berdasar"},
	{"Location","Shift","Cashier","Supplier","Category","Mark","Sale Date","Sort By","Sales Code","Sub Category","Transaction Type","Grouped by"}	
};

public static final String textListTitleHeader[][] = 
{
	{"Laporan Transaksi","Laporan Penjualan Harian","Pencarian","Cari Laporan","print excel"},
	{"Transaction Report","Daily Sale Report","Searching","Report Search","print excel"}
};

public static final String txtGlobal[][]=
{
       {"Laporan Penjualan > Integrasi AR","Lokasi","Tipe","Kasir","Mata Uang","Semua Shift","Semua Kasir","Semua Lokasi"},
       {"Report Sales > Integrasi AR","Location","Type","Cashier","Currency","All Shift","All Cashier","All Location"}
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
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");

SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();

SaleReportDocument saleReportDocument = new SaleReportDocument();
try{
	srcSaleReport  = (SrcSaleReport )session.getValue(SaleReportDocument.SALE_REPORT_DOC);
        
}catch(Exception e){
	srcSaleReport  = new SrcSaleReport();
}

if(srcSaleReport ==null){
	srcSaleReport = new SrcSaleReport();
        
}
try{
	session.removeValue(SaleReportDocument.SALE_REPORT_DOC);
}catch(Exception e){}
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
	document.frmsrcreportsale.command.value="<%=Command.LIST%>";
	document.frmsrcreportsale.action="reportlistsale_ar_integ.jsp";
	document.frmsrcreportsale.submit();
}

function cmdPrint(){
        document.frmsrcreportsale.command.value="<%=Command.LIST%>";
        document.frmsrcreportsale.target = '_blank';
        document.frmsrcreportsale.action="buffer_excel_perinvoice_sales.jsp";
	document.frmsrcreportsale.submit();
}

//change kasir based on Location
function cmdChangeLocation(){ 
   document.frmsrcreportsale.submit();
}

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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<%=txtGlobal[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frmsrcreportsale" method="post" action="">  
              <input type="hidden" name="command" value="<%=iCommand%>">
              <table width="100%" border="0">
                <tr> 
                  <td colspan="2"> 
                    <table width="100%" border="0" cellspacing="3" cellpadding="1">
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left"><%=txtGlobal[SESS_LANGUAGE][1]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; 
                          <%
                                                        long selectedLocationId = FRMQueryString.requestLong(request, frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID]);
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							//Vector vt_loc = PstLocation.listLocationStore(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                                        
                                                        
                                                        boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                                        String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                                        Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                        if(checkDataAllLocReportView){
                                                            val_locationid.add("0");
                                                            key_locationid.add(txtGlobal[SESS_LANGUAGE][7]);
                                                        }
                                                        
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_loc = "0";
                                                        if (selectedLocationId!=0){
                                                            select_loc= ""+selectedLocationId;

                                                        }
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation()\"", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="21">Dari</td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReport.getDateFrom(),"formElemen",4,-8)%> &nbsp;s/d &nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TO_DATE],srcSaleReport.getDateTo(),"formElemen",4,-8)%></td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=txtGlobal[SESS_LANGUAGE][2]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
                        <%
						Vector val_saletype = new Vector(1,1);
						Vector key_saletype = new Vector(1,1);
                                                
						val_saletype.add(""+PstBillMain.TRANS_TYPE_CREDIT);
						key_saletype.add(SrcSaleReport.reportType[SESS_LANGUAGE][SrcSaleReport.TYPE_CREDIT]);

						String select_saletype = "" + iSaleReportType;
						%>
                        	<%=ControlCombo.draw("sale_type", null, select_saletype, val_saletype, key_saletype, "", "formElemen")%>
						</td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">Shift</td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
                          <%
							Vector val_shiftid = new Vector(1,1);
							Vector key_shiftid = new Vector(1,1);
							Vector vt_shift = PstShift.list(0,0,"", PstShift.fieldNames[PstShift.FLD_NAME]);
							val_shiftid.add("0");
							key_shiftid.add(txtGlobal[SESS_LANGUAGE][5]);
							for(int d=0;d<vt_shift.size();d++){
								Shift shift = (Shift)vt_shift.get(d);
								val_shiftid.add(""+shift.getOID()+"");
								key_shiftid.add(shift.getName());
							}
							String select_shift = "0";
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_SHIFT_ID], null, select_shift, val_shiftid, key_shiftid, "", "formElemen")%></td>
                      </tr>

                      <!-- Add choice per cashier -->
                      <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=txtGlobal[SESS_LANGUAGE][3]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
                          <%
                                                        
                                                        String where = "";
                                                        if (selectedLocationId!=0) {
                                                            where = PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]+"='" +selectedLocationId+"'";
                                                        }
							Vector val_cashierid = new Vector(1,1);
							Vector key_cashierid = new Vector(1,1);
                                                        Location location = new Location();
                                                        Vector vt_cashier = PstCashMaster.list(0,0,where, PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]);
							val_cashierid.add("0");
							key_cashierid.add(txtGlobal[SESS_LANGUAGE][6]);
							for(int d=0;d<vt_cashier.size();d++){
								CashMaster cashMaster = (CashMaster)vt_cashier.get(d);
								val_cashierid.add(""+cashMaster.getOID()+"");
                                                                location = PstLocation.fetchExc(cashMaster.getLocationId());
                                                                //key_cashierid.add(location.getCode());
								//key_cashierid.add(""+cashMaster.getCashierNumber());
                                                                key_cashierid.add(location.getCode()+" - "+""+ cashMaster.getCashierNumber());

							}
							//String select_cashier = "0";
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CASH_MASTER_ID], null,""+val_cashierid, val_cashierid, key_cashierid, "", "formElemen")%></td>
                      </tr>
			<tr>
                        <td height="21" valign="top" width="9%" align="left"><%=txtGlobal[SESS_LANGUAGE][4]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
							<%
							Vector listCurr = PstCurrencyType.list(0,0,PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE,"");
							Vector vectCurrVal = new Vector(1,1);
							Vector vectCurrKey = new Vector(1,1);
							vectCurrKey.add("All");
							vectCurrVal.add("0");
							for(int i=0; i<listCurr.size(); i++) {
								CurrencyType currencyType = (CurrencyType)listCurr.get(i);
								vectCurrKey.add(currencyType.getCode());
								vectCurrVal.add(""+currencyType.getOID());
							}
							%>
							<%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CURRENCY], null, ""+srcSaleReport.getCurrencyOid(), vectCurrVal, vectCurrKey, "formElemen")%>  
						</td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left">
                          <table width="" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td nowrap width="">&nbsp;</td>
                              <td nowrap width="">&nbsp;</td>
                              <td nowrap width="">&nbsp;</td>
                              <td nowrap width="">&nbsp;</td>
                            </tr> 
                            <tr>
                              <td nowrap width="">&nbsp;</td>
                              <td class="command" nowrap width=""><a class="btn btn-primary" href="javascript:cmdSearch()"><i class="fa fa-search"></i> <%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                              <td nowrap width="">&nbsp;</td>
                              <td class="command" nowrap width=""><a class="btn btn-primary" href="javascript:cmdPrint()"><i class="fa fa-print"></i> <%=textListTitleHeader[SESS_LANGUAGE][4]%></a></td>
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



</SCRIPT>
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

