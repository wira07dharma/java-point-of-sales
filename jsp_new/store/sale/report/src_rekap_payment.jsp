<%-- 
    Document   : src_rekap_payment
    Created on : Mar 10, 2014, 5:09:42 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.pos.entity.payment.CashCreditPayments"%>
<%@page import="com.dimata.pos.entity.payment.PstCashCreditPayment"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.common.entity.custom.PstDataCustom"%>
<%@ page import="com.dimata.qdep.form.FRMQueryString,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.gui.jsp.ControlDate,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
				 com.dimata.common.entity.payment.PstCurrencyType,
				 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.gui.jsp.ControlCombo"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_PAYMENT); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<%!

public static final String textListHeader[][] = 
{
	{"Lokasi","Shift","Kasir","Supplier","Kategori","Merk","Tanggal","Urut Berdasar","Kode Sales","Sub Kategori","Tipe Transaksi","Dikelompokkan berdasar","Mata Uang"},
	{"Location","Shift","Cashier","Supplier","Category","Mark","Sale Date","Sort By","Sales Code","Sub Category","Transaction Type","Grouped by","Currency"}	
};

public static final String textListTitleHeader[][] =   
{
	{"Laporan Transaksi","Laporan Pelunasan Piutang","Pencarian","Cari Laporan","Update Payment"},
	{"Transaction Report","Receivable Payment","Searching","Report Search","Update Payment"}   	
};

public static final String textHeaderPayment[][] =   
{
	{"NO","Date","Tanggal Jatuh Tempo","Tanggal Pencairan","Amount","Status","Tanggal Update","Approve"},
	{"NO","Date","Tanggal Jatuh Tempo","Tanggal Pencairan","Amount","Status","Tanggal Update","Approve"}
};

public String drawList(int language,Vector objectClass, int start) {
    
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.addHeader(textHeaderPayment[language][0], "5%");
        ctrlist.addHeader(textHeaderPayment[language][2], "20%");
        ctrlist.addHeader(textHeaderPayment[language][3], "10%");
        ctrlist.addHeader(textHeaderPayment[language][4], "20%");
        ctrlist.addHeader(textHeaderPayment[language][5], "10%");
        ctrlist.addHeader(textHeaderPayment[language][6], "20%");
        ctrlist.addHeader(textHeaderPayment[language][7], "20%");
        
        ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector(1, 1);
        ctrlist.reset();
        int index = -1;
        if (start < 0) {
            start = 0;
        }

        for (int i = 0; i < objectClass.size(); i++) {
            CashCreditPayments cashCreditPayments = (CashCreditPayments) objectClass.get(i);
            
            start = start + 1;
            rowx = new Vector();
            rowx.add("" + start);
            rowx.add("<div align=\"left\">"+Formater.formatDate(cashCreditPayments.getDueDateCheck(), "yyyy-MM-dd")+"</div>");
            rowx.add("<div align=\"left\">"+Formater.formatDate(cashCreditPayments.getTanggalCair(), "yyyy-MM-dd")+"</div>");
            rowx.add("<div align=\"left\">"+Formater.formatNumber(cashCreditPayments.getAmount(), "###,###")+"</div>");
            if(cashCreditPayments.getPaymentStatus()==5){
                rowx.add("<div align=\"left\">Done</div>");
                rowx.add("<div align=\"left\">"+Formater.formatDate(cashCreditPayments.getPayDateTime(), "yyyy-MM-dd")+"</div>");
                rowx.add("<div align=\"left\"></div>");
            }else{
                rowx.add("<div align=\"left\">On Proses</div>");
                rowx.add("<div align=\"left\">"+ControlDate.drawDate("date_update", new Date(),"formElemen", 0, 0)+"</div>");
                rowx.add("<div align=\"left\"><input type=\"checkbox\"  name=\"invoice_"+cashCreditPayments.getOID()+"\" value=\"1\">Update Pembayaran</div>");
            }
            
            lstData.add(rowx);
        }
        
        return ctrlist.draw();
}



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
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);

Vector records= new Vector();
String where=" CCPI.TANGGAL_PENCAIRAN BETWEEN '" + Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd") + "'" +
             " AND '" + Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd") + "'"+
             " AND CCPM.LOCATION_ID='"+srcSaleReport.getLocationId()+"'";

if(iCommand==Command.LIST){
    records = PstCashCreditPayment.listPayment(0, 0, where, "");
}else if(iCommand==Command.UPDATE){
        //update paymentstatus dan payment datetime
        int x = PstCashCreditPayment.requestPayment(request, srcSaleReport);
        records = PstCashCreditPayment.listPayment(0, 0, where, "");
}else{
    srcSaleReport = new SrcSaleReport();
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
	document.frmsrcreportsale.command.value="<%=Command.LIST%>";
	document.frmsrcreportsale.action="src_rekap_payment.jsp";
	document.frmsrcreportsale.submit();
}


function cmdUpdate(){
	document.frmsrcreportsale.command.value="<%=Command.UPDATE%>";
	document.frmsrcreportsale.action="src_rekap_payment.jsp";
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;Laporan 
            Penjualan > Pelunasan Piutang<!-- #EndEditable --></td>
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
                        <td height="21" valign="top" width="9%" align="left">Lokasi</td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
                        <%
                                Vector val_locationid = new Vector(1,1);
                                Vector key_locationid = new Vector(1,1);
                                //Vector vt_loc = PstLocation.list(0,0,PstLocation.fieldNames[PstLocation.FLD_TYPE] + " = " + PstLocation.TYPE_LOCATION_STORE, PstLocation.fieldNames[PstLocation.FLD_CODE]);
                                //Vector vt_loc = PstLocation.listLocationStore(0,0,"",PstLocation.fieldNames[PstLocation.FLD_NAME]);
                                //boolean checkDataAllLocReportView = PstDataCustom.checkDataAllLocReportView(userId, "user_view_sale_stock_report_location","0");
                                String whereLocViewReport = PstDataCustom.whereLocReportView(userId, "user_view_sale_stock_report_location");
                                Vector vt_loc = PstLocation.listLocationStore(0,0,whereLocViewReport,PstLocation.fieldNames[PstLocation.FLD_NAME]);

                                //val_locationid.add("0");
                                //key_locationid.add("Semua Toko");
                                for(int d=0;d<vt_loc.size();d++){
                                        Location loc = (Location)vt_loc.get(d);
                                        val_locationid.add(""+loc.getOID()+"");
                                        key_locationid.add(loc.getName());
                                }
                                String select_loc = "0";
                        %>
                        <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_LOCATION_ID], null, select_loc, val_locationid, key_locationid, "", "formElemen")%></td>
                      </tr>
                      <tr>
                        <td height="21">Tanggal Pencairan Dari</td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReport.getDateFrom(),"formElemen",4,-8)%> &nbsp;s/d &nbsp;
                        <%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TO_DATE],srcSaleReport.getDateTo(),"formElemen",4,-8)%></td>
                      </tr>
					  <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][12]%></td>
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
                              <td nowrap width="7%">&nbsp;<a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][3]%>"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdSearch()"><%=textListTitleHeader[SESS_LANGUAGE][3]%></a></td>
                            </tr>
                          </table></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <%if(iCommand==Command.LIST || iCommand==Command.UPDATE){%>              
                  <table width="100%" border="0">
                    <tr> 
                      <td> 
                          <%=drawList(SESS_LANGUAGE,records, 0)%>
                      </td>
                    </tr>
                  </table>
                  <table width="100%" border="0">        
                    <tr>
                        <td height="21" valign="top" width="9%" align="left">&nbsp;</td>
                        <td height="21" valign="top" width="1%" align="left">&nbsp;</td>
                        <td height="21" width="90%" valign="top" align="left"> 
                          <table width="40%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="7%">&nbsp;<a href="javascript:cmdUpdate()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="<%=textListTitleHeader[SESS_LANGUAGE][4]%>"></a></td>
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a href="javascript:cmdUpdate()"><%=textListTitleHeader[SESS_LANGUAGE][4]%></a></td>
                            </tr>
                          </table></td>
                    </tr>
                  </table>
              <%}%>
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
