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
		 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.pos.entity.balance.*"%>
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
           {"Laporan Penjualan > Penjualan Per Cash Opening","Semua Location","Tipe","Return Penjualan Cash","Return Penjualan Credit","Mata Uang","Tanggal","Lokasi","Shift","Semua Shift","Kategori","Supplier","Kasir"},
           {"Sales Report > Sales Report Per Cash Opening","All Location","Type","Return Sales Cash","Return Credit Sales","Currency","Date","Location","Shift","All Shift","Category","Vendor","Cashier"}
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
    document.frmsrcreportsale.action="report_cashier_sales_sum.jsp";
    document.frmsrcreportsale.submit();
}


//change kasir based on Location
function cmdChangeLocation(){ 
   document.frmsrcreportsale.submit();
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">

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
            <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<%=textGlobal[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
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
                                                long selectedLocationId = FRMQueryString.requestLong(request, frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID]);
                                                Vector val_locationid = new Vector(1,1);
                                                Vector key_locationid = new Vector(1,1);                               
                                                boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                                String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location","");
                                                Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                                if(checkDataAllLocReportView){
                                                    val_locationid.add("0");
                                                    key_locationid.add("Semua Lokasi");
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
                                            <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, "onChange=\"javascript:cmdChangeLocation()\"", "formElemen")%>
                                        </td>
                                    </tr>
                                    <tr> 
                                        <td height="21">Tanggal</td>
                                        <td height="21">:</td>
                                        <td height="21">&nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReport.getDateFrom(),"formElemen",4,-8)%> 
                                        </td>
                                    </tr>                
                                    
                                    <tr>
                                        <td height="21" valign="top" width="9%" align="left"><%=textGlobal[SESS_LANGUAGE][12]%></td>
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
                                            //CashCashier cashCashier = new CashCashier();
                                            Vector vt_cashier = PstCashMaster.list(0,0,where, PstCashMaster.fieldNames[PstCashMaster.FLD_LOCATION_ID]);
                                            val_cashierid.add("0");
                                            key_cashierid.add("All Cashier");
                                            for(int d=0;d<vt_cashier.size();d++){
                                                    CashMaster cashMaster = (CashMaster)vt_cashier.get(d);
                                                    val_cashierid.add(""+cashMaster.getOID()+"");
                                                    location = PstLocation.fetchExc(cashMaster.getLocationId());
                                                    //cashCashier = PstCashCashier.fetchExc(cashMaster.getOID());
                                                    //key_cashierid.add(location.getCode());
                                                    //key_cashierid.add(""+cashMaster.getCashierNumber());
                                                    key_cashierid.add(location.getCode()+" - "+""+ cashMaster.getCashierNumber());
                                            }
                                            //String select_cashier = "0";
                                        %>
                                        <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CASH_MASTER_ID], null,""+val_cashierid, val_cashierid, key_cashierid, "", "formElemen")%></td>
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
                                        <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CURRENCY], null, ""+srcSaleReport.getCurrencyOid(), vectCurrVal, vectCurrKey, "formElemen")%>  
                                        </td>
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
                                        </table>
                                      </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </form>            
            </td> 
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
