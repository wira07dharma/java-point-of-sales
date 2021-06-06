<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.posbo.session.warehouse.SessReportSale,
                 com.dimata.posbo.entity.search.SrcReportSale,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.search.FrmSrcReportSale,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.payment.PstCurrencyType,
                 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.pos.entity.billing.*,
                 com.dimata.pos.entity.payment.*,
                 com.dimata.posbo.entity.masterdata.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>
<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","KSG","QTY","DESCRIPTION"},
	{"NO","KSG","QTY","DESCRIPTION"}
};

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE","Mata Uang"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Laporan","Print Daily Transaction ","TYPE","Currency Type"}
};

public String drawList(int language,Vector objectClass,SrcReportSale srcSaleReport) {

    String result = "";
	if(objectClass!=null && objectClass.size()>0){   
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("40%");
		ctrlist.setListStyle("listgen"); 
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][1],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][2],"10%","0","0","center","bottom");
                ctrlist.dataFormat(textListMaterialHeader[language][3],"35%","0","0","center","bottom");

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
                
                double totalqty = 0.0 ;
		for(int i=0; i<objectClass.size(); i++)	{
                    rowx = new Vector();
                    Vector listitem = (Vector)objectClass.get(i);
                    System.out.println("listitem : "+listitem);
                    
                    rowx.add("<div align=\"right\">"+String.valueOf(listitem.get(0))+"</div>");
                    rowx.add("<div align=\"center\">"+String.valueOf(listitem.get(1))+"</div>");
                    rowx.add("<div align=\"right\">"+String.valueOf(listitem.get(2))+"</div>");
                    rowx.add("<div align=\"left\">"+String.valueOf(listitem.get(3))+"</div>");
                    
                    totalqty = totalqty + Double.parseDouble(""+listitem.get(2));
                    
                    lstData.add(rowx);
                    lstLinkData.add(String.valueOf(-1));
                }

        rowx = new Vector();
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>&nbsp;</b></div>");
        rowx.add("<div align=\"right\"><b>"+String.valueOf(totalqty)+"</b></div>");
        rowx.add("<div align=\"right\"><b>&nbsp;</b></div>");	
        						
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));

        result = ctrlist.draw();
    }
    return result;
}

%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 100;

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcReportSale srcSaleReport = new SrcReportSale();
FrmSrcReportSale frmSrcReportSale = new FrmSrcReportSale(request, srcSaleReport);
frmSrcReportSale.requestEntityObject(srcSaleReport); 

int vectSize = 0;

/**
* handle current search data session 
*/
try {
    if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK) {
        srcSaleReport = (SrcReportSale)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
    }
}
catch(Exception e) {
}

if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST){
	try{
		start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
		if (srcSaleReport== null){
                    srcSaleReport= new SrcReportSale();
            }
	}
	catch(Exception e){
		srcSaleReport = new SrcReportSale();
	}
}
else{
    session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
}

Location location = new Location();
try{
    location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e){
	location.setName("Semua toko");
}

Vector records = SessReportSale.ReportBrand(srcSaleReport);
try{
	session.putValue("report_rekap_ksg",records);
}catch(Exception e){}

String strCurrencyType = "All";
if(srcSaleReport.getCurrencyId() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyId();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdEdit(oid){
        document.frm_reportsale.hidden_billmain_id.value=oid;	
        document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="invoice_edit.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistsale.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportrekap.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){

    //window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
	window.open("reportlistrekap_print_form.jsp","reportlist_ksg","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
            &nbsp;Laporan Penjualan > Penjualan Per KSG <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center">&nbsp;</td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=textListTitleHeader[SESS_LANGUAGE][3]%> : <%=location.getName()%> </b> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b>Tanggal : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>				
                <%--<tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b>Tipe : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : "Penjualan Kredit"%></b></td>
                </tr>--%>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records,srcSaleReport)%></td>
                </tr>
				<tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>															
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</b></td>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>							
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					//out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> 
                    <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td class="command" nowrap width="95%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="39%"><a href="javascript:printForm()">Print Laporan</a></td>
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
