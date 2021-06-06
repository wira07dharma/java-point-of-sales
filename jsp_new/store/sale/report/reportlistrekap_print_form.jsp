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
		ctrlist.setAreaWidth("100%");
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

//SrcSaleReport srcSaleReport = new SrcSaleReport();
try{
	srcSaleReport = (SrcReportSale)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
}catch(Exception e){}

Location location = new Location();
try{
    location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e){
	location.setName("Semua toko");
}

Vector records = new Vector(); 
try{
	records = (Vector)session.getValue("report_rekap_ksg");
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

<html>
<!-- DW6 -->
<head>

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
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------

//------------------------- END JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
//-->
</script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

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
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
   
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        
        <tr> 
          <td>            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="">
              <table width="100%" cellspacing="0" cellpadding="3">
                
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b><%=textListTitleHeader[SESS_LANGUAGE][3]%> : <%=location.getName()%> </b> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" class="command"> 
                    <b>Tanggal : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>
                <%--
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b>Tipe : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? "Penjualan Cash" : "Penjualan Kredit"%></b></td>
                </tr>--%>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%//=drawList(SESS_LANGUAGE, records,srcSaleReport)%></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><table width="100%" border="1" cellspacing="1" cellpadding="1">
                    <tr>
                      <th width="10%" scope="col">NO</th>
                      <th width="40%" scope="col">KSG</th>
                      <th width="25%" scope="col">QTY</th>
                      <th width="25%" scope="col">Description</th>
                    </tr>
					<%
					double totalqty = 0.0;
					for(int i=0; i<records.size(); i++)	{
								Vector listitem = (Vector)records.get(i);
					%>
                    <tr>
                      <td>&nbsp;<%=String.valueOf(listitem.get(0))%></td>
                      <td>&nbsp;<%=String.valueOf(listitem.get(1))%></td>
                      <td>&nbsp;<%=String.valueOf(listitem.get(2))%></td>
                      <td>&nbsp;<%=String.valueOf(listitem.get(3))%></td>
                    </tr>
					<%
						totalqty = totalqty + Double.parseDouble(""+listitem.get(2));
					}%>
                    <tr>
                      <td colspan="2" align="right">TOTAL </td>
                      <td>&nbsp;<%=totalqty%></td>
                      <td>&nbsp;</td>
                    </tr>
                  </table></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3">&nbsp;</td>
                </tr>
              </table>
            </form></td> 
        </tr> 
    </table>    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20">  
      </td>
  </tr>
</table>
</body>
</html>
