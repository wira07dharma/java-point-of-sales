<%@page import="com.dimata.common.entity.system.AppValue"%>
<%@page import="com.dimata.pos.entity.billing.PstBillDetail"%>
<%@page import="com.dimata.pos.entity.billing.BillMain"%>
<%@ page import="java.util.Hashtable,
				 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
				 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.pos.entity.billing.PstPendingOrder,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.pos.entity.billing.PendingOrder,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
				 com.dimata.common.entity.payment.PstCurrencyType,
				 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_PENDING_ORDER); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Laporan Pending Order","Tidak ada data...","Lokasi","Periode","s/d","Mata Uang","Semua","Cetak Laporan Pending Order"},
	{"Pending Order Report","No data available...","Location","Period","to","Currency","All","Print Pending Order Report"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","TANGGAL","NOMOR","KONSUMEN","DOWN PAYMENT","JUMLAH","KURS","TOTAL","NOTE","SALES NETTO","DISC","TAX"},
	{"NO","DATE","NUMBER","CUSTOMER","DOWN PAYMENT","NOMINAL","RATE","TOTAL","CATATAN","SALES NETTO","DISC","TAX"}
};

public Vector drawList(int language, Vector objectClass, SrcSaleReport srcSaleReport, int start, Hashtable hashCurrType, long lSelectedCurrType) {
    Vector result = new Vector(1,1);
    if(objectClass != null && objectClass.size() > 0) {	
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("listgentitle");		

            ctrlist.addHeader(textListMaterialHeader[language][0],"3%","0","0");
            ctrlist.addHeader(textListMaterialHeader[language][1],"7%","0","0");
            ctrlist.addHeader(textListMaterialHeader[language][2],"15%","0","0");
            ctrlist.addHeader(textListMaterialHeader[language][3],"30%","0","0");
            //new
            ctrlist.addHeader(textListMaterialHeader[language][7],"30%","0","0");
            
            /*ctrlist.addHeader(textListMaterialHeader[language][4],"30%","0","3");
            
            ctrlist.addHeader(textListMaterialHeader[language][5]+" ("+hashCurrType.get(""+lSelectedCurrType)+")","13%","0","0");
            ctrlist.addHeader(textListMaterialHeader[language][6],"9%","0","0");
            ctrlist.addHeader(textListMaterialHeader[language][7]+" (Rp)","13%","0","0");*/
            String frmCurrency = AppValue.getValueByKey("FORMAT_LOCAL_CURRENCY");//"#,###";
            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector();

            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();		

            double total = 0;
            double bruto = 0;
            double diskon = 0;
            double pajak = 0;
            double servis = 0;
            double netto = 0;
            double dp = 0;
            double totalBruto = 0;
            double totalDisc = 0;
            double totalTax = 0;
            double totalService = 0;						
            double totalNetto = 0;
            double totalDp = 0;
            double totalCost = 0;
            
            for(int i=0; i<objectClass.size(); i++) {
                BillMain pendingOrder = (BillMain)objectClass.get(i);
                rowx = new Vector();		
                ContactList contactlist = new ContactList();
                try	{
                    contactlist = PstContactList.fetchExc(pendingOrder.getCustomerId());
                }
                catch(Exception e) {
                        //System.out.println("Exc when fetch member : " + e.toString());
                }

                rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
                rowx.add("<div align=\"left\">"+Formater.formatDate(pendingOrder.getBillDate(),"dd/MM/yyyy")+"</div>");
                rowx.add("<div align=\"left\"><a href=\"javascript:cmdEdit('"+pendingOrder.getOID()+"')\">"+pendingOrder.getInvoiceNumber()+"</a></div>");
                rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");	

                bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+pendingOrder.getOID());
                rowx.add("<div align=\"right\">"+Formater.formatNumber(bruto, frmCurrency)+"</div>");	
                //netto = bruto - diskon + pajak + servis;
                //dp = PstPendingOrder.getDpValue(pendingOrder.getOID());

                /*rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pendingOrder.getRate())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pendingOrder.getPaidAmount()* pendingOrder.getRate())+"</div>");*/

                total = bruto+bruto;//(pendingOrder.getPaidAmount() * pendingOrder.getRate());

                lstData.add(rowx);

                lstLinkData.add(String.valueOf(0));
            }

            result.add(ctrlist.drawList());
            result.add(FRMHandler.userFormatStringDecimal(total));
    }else {
            result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][1]+"</div>");
            result.add("");
    }
    return result;
}
%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
long lCurrTypeOid = FRMQueryString.requestLong(request, FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CURRENCY]);
int recordToGet = 100;

// get data currency type
String sWhereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_IN_PROCESS]+"="+PstCurrencyType.INCLUDE;
Vector vCurrencyType = PstCurrencyType.list(0,0,sWhereClause,"");
Hashtable hashCurrType = new Hashtable();
if(vCurrencyType!=null && vCurrencyType.size()>0) {
	int iCurrTypeCount = vCurrencyType.size();
	for(int i=0; i<iCurrTypeCount; i++)	{
		CurrencyType objCurrType = (CurrencyType) vCurrencyType.get(i);
		hashCurrType.put(""+objCurrType.getOID(), ""+objCurrType.getCode());
	}
}

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);
frmSrcSaleReport.requestEntity(srcSaleReport);
int vectSize = 0;

/**
* handle current search data session 
*/
try {
	if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST) {
		srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
	}
}
catch(Exception e) {
}

vectSize = PstPendingOrder.getCountPendingOrder(srcSaleReport);

if(iCommand==Command.FIRST || iCommand==Command.PREV ||	iCommand==Command.NEXT || iCommand==Command.LAST) {
	try {
		start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
		if (srcSaleReport== null) {
			srcSaleReport= new SrcSaleReport();
		}
	}
	catch(Exception e) {
		srcSaleReport = new SrcSaleReport();
	}
}
else {
	session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
}

Location location = new Location();
try {
	location = PstLocation.fetchExc(srcSaleReport.getLocationId());
}
catch(Exception e) {
	location.setName(textListGlobal[SESS_LANGUAGE][6]+" "+textListGlobal[SESS_LANGUAGE][2]);
}

//Vector records = PstPendingOrder.getDataPendingOrder(srcSaleReport, start, recordToGet);
Vector records = PstPendingOrder.getDataPendingOrder(srcSaleReport, 0, 0);

Vector vctDrawList = drawList(SESS_LANGUAGE, records, srcSaleReport, start, hashCurrType, lCurrTypeOid);

String strDrawList = (String)vctDrawList.get(0);
String strTotalDp = (String)vctDrawList.get(1);
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportlistpendingorder.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistpendingorder.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistpendingorder.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistpendingorder.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportpendingorder.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("buff_pdf_report_pending_order.jsp","report_pending_order")
	//window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function cmdEdit(oid){
        /*document.frm_reportsale.hidden_billmain_id.value=oid;	
        document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="invoice_edit_open_bill.jsp";
	document.frm_reportsale.submit();*/
        //window.open("buff_pdf_report_pending_order.jsp?hidden_billmain_id='"+oid+"'","report_pending_order")
        window.open("invoice_edit_open_bill.jsp?hidden_billmain_id="+oid+"","stockreport","scrollbars=yes,height=600,width=1200,status=no,toolbar=no,menubar=yes,location=no");
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="">
              <input type="hidden" name="<%=FrmSrcSaleReport.fieldNames[FrmSrcSaleReport.FRM_FLD_CURRENCY]%>" value="<%=lCurrTypeOid%>">			  			  
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center"><h4><strong><%=(textListGlobal[SESS_LANGUAGE][0]).toUpperCase()%></strong></h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="1" class="command" width="10%"> 
                    <strong><%=textListGlobal[SESS_LANGUAGE][3]%> </strong></td>
                  <td height="14" valign="middle" colspan="2" class="command" width="90%"> 
                    <strong>: <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][4]%> <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%> </strong></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="1" class="command" width="10%"> 
                    <strong><%=textListGlobal[SESS_LANGUAGE][2]%> </strong></td>
                  <td height="14" valign="middle" colspan="2" class="command" width="90%"> 
                    <strong>: <%=location.getName()%> </strong></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="1" class="command" width="10%"> 
                    <strong><%=textListGlobal[SESS_LANGUAGE][5]%> </strong></td>
                  <td height="14" valign="middle" colspan="2" class="command" width="90%"> 
                    <strong>: <%=hashCurrType.get(""+lCurrTypeOid)%> </strong></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%=strDrawList%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3" align="right">
				    <% if(strTotalDp != "") { %>
					<h4><b>GRAND <%=textListMaterialHeader[SESS_LANGUAGE][7]%> :&nbsp;&nbsp;&nbsp;<%=strTotalDp%></b></h3> 
					<% } %>
				  </td>
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
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td nowrap width="5%"> <a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"> 
                                <img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_BACK_SEARCH,true)%>"> 
                                </a> </td-->
                              <td class="command" nowrap width="95%"> <a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i>  
                                <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][0],ctrLine.CMD_BACK_SEARCH,true)%> </a> </td>
                            </tr>
                          </table></td>
                        <td width="20%">
						<% if(records.size() != 0) { %>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][7]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][7]%></a></td>
                            </tr>
                          </table>
						<% } %>
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
