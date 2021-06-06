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
                 com.dimata.gui.jsp.ControlCombo,
				 com.dimata.pos.entity.billing.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<%!
public static final String textListGlobal[][] = {
	{"Laporan Penjualan","Rekap Piutang","Cari Laporan","Semua Lokasi","Balance","Un Balance"},
	{"Sale Report","AR Summary","Search Report","All Location","Balance","Un Balance"}
};

public static final String textListHeader[][] = {
	{"Lokasi","Kode Member","Nama Member","Tanggal","Semua Tanggal","Dari","s/d","Status","Mata Uang"},
	{"Location","Member Code","Member Name","Date","All Date","From","to","Status","Currency"}	
};


%>

<!-- Jsp Block -->
<%
/**
* get data from 'hidden form object'
*/
int iCommand = FRMQueryString.requestCommand(request);

SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport();

SaleReportDocument saleReportDocument = new SaleReportDocument();
try {
	srcSaleReport  = (SrcSaleReport )session.getValue(SaleReportDocument.AR_REPORT_DOC);
        
}catch(Exception e) {
	srcSaleReport  = new SrcSaleReport();
	srcSaleReport.setTransStatus(PstBillMain.TRANS_STATUS_OPEN);
}

if(srcSaleReport ==null) {
	srcSaleReport = new SrcSaleReport();
	srcSaleReport.setTransStatus(PstBillMain.TRANS_STATUS_OPEN);    
}

try {
	session.removeValue(SaleReportDocument.AR_REPORT_DOC);
}catch(Exception e) {
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdSearch(){
	document.frmsrcreportsale.command.value="<%=Command.LIST%>";
	document.frmsrcreportsale.action="reportlistar.jsp";
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
		  	<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%>
		  <!-- #EndEditable --></td>
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
                        <td height="21" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][0]%></td>
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
                                                            key_locationid.add(textListGlobal[SESS_LANGUAGE][3]);
                                                        }
                                                        
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
                        <td height="21"><%=textListHeader[SESS_LANGUAGE][1]%></td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; 
                          <input type="text" name="<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CUST_CODE]%>2" value="<%=srcSaleReport.getCustCode()%>" width="20" size="20">
                        </td>
                      </tr>
                      <tr> 
                        <td height="21"><%=textListHeader[SESS_LANGUAGE][2]%></td>
                        <td height="21">:</td>
                        <td height="21">&nbsp; 
                          <input type="text" name="<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CUST_NAME]%>" value="<%=srcSaleReport.getCustName()%>" width="40" size="40">
                        </td>
                      </tr>
                      <tr> 
                        <td height="45" rowspan="2" width="9%" valign="top" align="left"><%=textListHeader[SESS_LANGUAGE][3]%></td>
                        <td height="45" rowspan="2" width="1%" valign="top" align="left">:</td>
                        <td height="21" width="90%">&nbsp;
						  <input type="radio" name="<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_USE_DATE] %>" <%if(srcSaleReport.getUseDate()==0){%>checked<%}%> value="0"><%=textListHeader[SESS_LANGUAGE][4]%>
						</td>
                      </tr>
                      <tr> 
                        <td height="21" width="90%" valign="top" align="left">&nbsp;
						  <input type="radio" name="<%=frmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_USE_DATE] %>" <%if(srcSaleReport.getUseDate()==1){%>checked<%}%> value="1">
                          <%=textListHeader[SESS_LANGUAGE][5]%>&nbsp;<%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_FROM_DATE],srcSaleReport.getDateFrom(),"formElemen",4,-8)%>&nbsp;<%=textListHeader[SESS_LANGUAGE][6]%>&nbsp;<%=ControlDate.drawDate(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TO_DATE],srcSaleReport.getDateTo(),"formElemen",4,-8)%>
						</td>
                      </tr>
                      <tr> 
                        <td height="21" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][7]%></td>
                        <td height="21" valign="top" width="1%" align="left">:</td>
                        <td height="21" width="90%" valign="top" align="left">&nbsp; 
                          <%
						Vector val_saletype = new Vector(1,1);
						Vector key_saletype = new Vector(1,1);

						//val_saletype.add("-1");
						//key_saletype.add("Semua tipe penjualan");
						
						val_saletype.add(""+PstBillMain.TRANS_STATUS_CLOSE);
						key_saletype.add(textListGlobal[SESS_LANGUAGE][4]);
						
						val_saletype.add(""+PstBillMain.TRANS_STATUS_OPEN);
						key_saletype.add(textListGlobal[SESS_LANGUAGE][5]);

						String select_saletype = "" + srcSaleReport.getTransStatus();
						%>
                          <%=ControlCombo.draw(frmSrcSaleReport.fieldNames[frmSrcSaleReport.FRM_FLD_TRANS_STATUS], null, select_saletype, val_saletype, key_saletype, "", "formElemen")%> </td>
                      </tr>
					  <tr>
                        <td height="21" valign="top" width="9%" align="left"><%=textListHeader[SESS_LANGUAGE][8]%></td>
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
                              <td class="command" nowrap width="90%"><a class="btn-primary btn-lg" href="javascript:cmdSearch()"><i class="fa fa-search"> &nbsp;<%=textListGlobal[SESS_LANGUAGE][2]%></i></a></td>
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
