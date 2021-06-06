<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.entity.search.SrcReportSale,
                 com.dimata.posbo.form.search.FrmSrcReportSale,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.gui.jsp.ControlCombo,
		 com.dimata.pos.entity.billing.*,
                 com.dimata.posbo.entity.masterdata.PstShift,
                 com.dimata.posbo.entity.masterdata.Shift,
                 com.dimata.common.entity.contact.PstContactClass,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
		 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.posbo.entity.masterdata.PstCategory,
                 com.dimata.posbo.entity.masterdata.Category,
                 com.dimata.posbo.entity.masterdata.Category,
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
	{"Laporan Transaksi","Laporan Penjualan Harian","Pencarian","Cari Laporan"},
	{"Transaction Report","Daily Sale Report","Searching","Report Search"}	        
};

public static final String textGlobal[][] =
{
           {"Laporan Penjualan > Rekap Penjualan","Semua Location","Tipe","Return Penjualan Cash","Return Penjualan Credit","Mata Uang","Tanggal","Lokasi","Shift","Semua Shift","Kategori","Supplier"},
           {"Sales Report > Summary Report","All Location","Type","Return Sales Cash","Return Credit Sales","Currency","Date","Location","Shift","All Shift","Category","Vendor"}
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

SrcReportSale srcSaleReport = new SrcReportSale();
FrmSrcReportSale frmSrcSaleReport = new FrmSrcReportSale();

SaleReportDocument saleReportDocument = new SaleReportDocument();
try{
	srcSaleReport  = (SrcReportSale )session.getValue(SaleReportDocument.SALE_REPORT_DOC);
        
}catch(Exception e){
	srcSaleReport  = new SrcReportSale();
}

if(srcSaleReport ==null){
    srcSaleReport = new SrcReportSale();
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
	document.frmsrcreportsale.action="reportlistrekapkategori.jsp";
	document.frmsrcreportsale.submit();
}
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<%=textGlobal[SESS_LANGUAGE][0]%> <!-- #EndEditable --></td>
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
                        <td height="21" valign="top" width="9%" align="left"><%=textGlobal[SESS_LANGUAGE][7]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; 
                          <%
							Vector val_locationid = new Vector(1,1);
							Vector key_locationid = new Vector(1,1);
							//Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
							//Vector vt_loc = PstLocation.listLocationStore(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                        boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                                        String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location");
                                                        Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                        if(checkDataAllLocReportView){
                                                            val_locationid.add("0");
                                                            key_locationid.add(textGlobal[SESS_LANGUAGE][1]);
                                                        }
							for(int d=0;d<vt_loc.size();d++){
								Location loc = (Location)vt_loc.get(d);
								val_locationid.add(""+loc.getOID()+"");
								key_locationid.add(loc.getName());
							}
							String select_loc = "0";
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcReportSale.FRM_FIELD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, "", "formElemen")%></td>
                      </tr>
                      <tr> 
                        <td height="21">Dari</td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FIELD_DATE_FROM],srcSaleReport.getDateFrom(),"formElemen",4,-8)%> &nbsp;s/d &nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FIELD_DATE_TO],srcSaleReport.getDateTo(),"formElemen",4,-8)%></td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=textGlobal[SESS_LANGUAGE][8]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
                          <%
							Vector val_shiftid = new Vector(1,1);
							Vector key_shiftid = new Vector(1,1);
							Vector vt_shift = PstShift.list(0,0,"", PstShift.fieldNames[PstShift.FLD_NAME]);
							val_shiftid.add("0");
							key_shiftid.add(textGlobal[SESS_LANGUAGE][9]);
							for(int d=0;d<vt_shift.size();d++){
								Shift shift = (Shift)vt_shift.get(d);
								val_shiftid.add(""+shift.getOID()+"");
								key_shiftid.add(shift.getName());
							}
							String select_shift = "0";
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcReportSale.FRM_FIELD_SHIFT_ID], null, select_shift, val_shiftid, key_shiftid, "", "formElemen")%></td>
                      </tr>
					  <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=textGlobal[SESS_LANGUAGE][5]%></td>
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
							<%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcReportSale.FRM_FIELD_CURRENCY_ID],"formElemen", null, ""+srcSaleReport.getCurrencyId() , vectCurrVal, vectCurrKey,null)%></td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" align="left"><%=textGlobal[SESS_LANGUAGE][10]%></td>
                        <td height="21" valign="top" align="left">:</td>
                        <td height="21" valign="top" align="left">&nbsp;
                        <%
                    Vector materGroup = PstCategory.list(0,0,PstCategory.fieldNames[PstCategory.FLD_CATEGORY_ID]+"!=0",PstCategory.fieldNames[PstCategory.FLD_NAME]);
                    Vector vectGroupVal = new Vector(1,1);
                    Vector vectGroupKey = new Vector(1,1);
                    vectGroupVal.add("All");
                    vectGroupKey.add("0");
                    if(materGroup!=null && materGroup.size()>0) {
                        int maxGrp = materGroup.size();
                        for(int i=0; i<maxGrp; i++) {
                            Category mGroup = (Category)materGroup.get(i);       
                            vectGroupVal.add(mGroup.getName());
                            vectGroupKey.add(""+mGroup.getOID());
                        }
                    } else {
                        vectGroupVal.add("No Group Available");
                        vectGroupKey.add("-1");
                    }
                    out.println(ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcReportSale.FRM_FIELD_CATEGORY_ID],"formElemen", null, ""+srcSaleReport.getCategoryId(), vectGroupKey, vectGroupVal, " tabindex=\"4\" onkeydown=\"javascript:fnTrapKD(event)\""));                    %></td>
                      </tr>
                      <tr>
                        <td height="21" valign="top" align="left"><%=textGlobal[SESS_LANGUAGE][11]%></td>
                        <td height="21" valign="top" align="left">:</td>
                        <td height="21" valign="top" align="left">&nbsp;
                        <% 
						Vector obj_supplier = new Vector(1,1);
						Vector val_supplier = new Vector(1,1);
						Vector key_supplier = new Vector(1,1);
						val_supplier.add("0");
						key_supplier.add("All");
						String wh_supp = PstContactClass.fieldNames[PstContactClass.FLD_CLASS_TYPE]+
                                                        
								" = "+PstContactClass.CONTACT_TYPE_SUPPLIER+" AND "+PstContactList.fieldNames[PstContactList.FLD_PROCESS_STATUS]+
								" != "+PstContactList.DELETE;
                                                
						Vector vt_supp = PstContactList.listContactByClassType(0,0,wh_supp,PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]);
						if(vt_supp!=null && vt_supp.size()>0){
							int maxSupp = vt_supp.size();
							for(int d=0; d<maxSupp; d++){
								ContactList cnt = (ContactList)vt_supp.get(d);
								String cntName = cnt.getCompName();
								val_supplier.add(String.valueOf(cnt.getOID()));
                                                                
								key_supplier.add(cntName);
							}
						}
						String select_supplier = ""+srcSaleReport.getSupplierId();

						%>
						<%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcReportSale.FRM_FIELD_SUPPLIER_ID],"formElemen",null,select_supplier,val_supplier,key_supplier," tabindex=\"3\" onkeydown=\"javascript:fnTrapKD(event)\"")%> </td>
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
                              <td nowrap width="7%">&nbsp;<a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Cari Laporan"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdSearch()"><%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                            </tr>
                          </table>                        </td>
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
