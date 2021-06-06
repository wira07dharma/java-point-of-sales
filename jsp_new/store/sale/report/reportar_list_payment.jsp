<%@ page import="com.dimata.posbo.entity.search.RptArInvoice,
				 com.dimata.posbo.entity.search.RptArPayment,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.util.Command,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.pos.form.billing.CtrlBillMain,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.pos.entity.billing.*,
                 com.dimata.pos.entity.payment.*,
				 com.dimata.posbo.entity.masterdata.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_SUMMARY); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"NO","TANGGAL","NOMOR","MEMBER","TOTAL","INVOICE REFERENSI"},
	{"NO","DATE","NUMBER","MEMBER","DP","INVOICE REFERENCE"}
};

public static final String textListTitleHeader[][] = 
{
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Piutang","Cetak Transaksi Harian","TIPE"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","AR","Print Daily Transaction ","TYPE"}
};

public String drawList(int language,Vector objectClass,int start, int recordToGet) {

    String result = "";
	if(objectClass!=null && objectClass.size()>0){
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("50%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.dataFormat(textListMaterialHeader[language][0],"5%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][2],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][1],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][4],"15%","0","0","center","bottom");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
        Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

        double total = 0;
		double totalAll = 0;
		for(int i=0; i<objectClass.size(); i++){
            RptArPayment arPay = (RptArPayment) objectClass.get(i);
			
			if(i>=start && i <(start+recordToGet)){
            rowx = new Vector();
            rowx.add("<div align=\"center\">"+(i+1)+"</div>");
            rowx.add("<div align=\"center\">"+arPay.getPayInvoiceNo()+"</div>");
			rowx.add("<div align=\"center\">"+Formater.formatDate(arPay.getPayDate(),"dd/MM/yyyy")+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(arPay.getTotalPay())+"</div>");
			total = total + arPay.getTotalPay();
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(arPay.getCreditPaymentMainId()));
			}
			
			totalAll = totalAll + arPay.getTotalPay();
            
        }
	
		if(total==totalAll){
			rowx = new Vector();
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
			rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total)+"</b></div>");
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(0));
		}
		else{
			rowx = new Vector();
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"left\"><b>TOTAL FOR THIS LIST</b></div>");
			rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total)+"</b></div>");
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(0));
			
			rowx = new Vector();
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"center\"></div>");
			rowx.add("<div align=\"left\"><b>GRANT TOTAL</b></div>");
			rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalAll)+"</b></div>");
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(0));
		}
        result = ctrlist.draw();
    }
    return result;
}


public String generateHeader()
{
	return "<tr>" + 
			  "<td width=\"4%\" class=\"listgentitle\">NO</td>" + 
			  "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
			  "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
			  "<td width=\"26%\" class=\"listgentitle\">TOTAL</td>" +
			"</tr>";
}

public String generateItemHeader(String strItemHeader)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"5\"><b>"+strItemHeader+"</b></td>" +
			"</tr>";
}

public String generateContent(String strNum, String strCurrency, String strAmount, String strRate, String strTotal)
{
	return "<tr valign=\"top\">" + 
			  "<td width=\"4%\" class=\"listgensell\" align=\"center\">"+strNum+"</td>" + 
			  "<td width=\"24%\" class=\"listgensell\">"+strCurrency+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strAmount+"</td>" +
			  "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strRate+"</td>" +
			  "<td width=\"26%\" class=\"listgensell\" align=\"right\">"+strTotal+"</td>" +
			"</tr>";			
}

public String generateItemFooter(String strItemFooter, String strValue)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\"><b>"+strItemFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateFooter(String strFooter, String strValue)
{
	return "<tr valign=\"top\">" + 
			  "<td class=\"listgensell\" colspan=\"4\" ><b>"+strFooter+"</b></td>" +
			  "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
			"</tr>";
}

public String generateBlankSpace()
{
	return "<tr valign=\"top\">"  +
			  "<td class=\"listgensell\" colspan=\"5\">&nbsp;</td>" +
			"</tr>";
}

%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prev_start =  FRMQueryString.requestInt(request, "prev_start");

int indeks = FRMQueryString.requestInt(request, "indeks");
int recordToGet = 20;

/**
* instantiate some object used in this page
*/

ControlLine ctrLine = new ControlLine();
RptArInvoice rptArInvoice = new RptArInvoice();

int vectSize = 0;

/**
* handle current search data session 
*/
    try
	{
        Vector vTemp = (Vector)session.getValue(SaleReportDocument.AR_PAYMENT_DOC);
		rptArInvoice = (RptArInvoice) vTemp.get(indeks);
    }
	catch(Exception e)
	{
		
	}

    vectSize = rptArInvoice.getVectPayment().size();
    if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
	{
	    try
		{
            start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
	    }
		catch(Exception e)
		{
	        rptArInvoice = new RptArInvoice();
        }
    }
	else{
		prev_start = start;
		start = 0;
	}
	
    Location location = new Location();
    try
	{
        location = PstLocation.fetchExc(rptArInvoice.getLocationId());
    }
	catch(Exception e)
	{
		location.setName("Semua toko");
	}
	ContactList contact = new ContactList();
	try
	{
        contact = PstContactList.fetchExc(rptArInvoice.getMemberId());
    }
	catch(Exception e)
	{
	}
    Vector records = rptArInvoice.getVectPayment();
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdEdit(oid)
{
    document.frm_reportsale.hidden_group_id.value=oid;
	document.frm_reportsale.start.value="<%=prev_start%>";
	document.frm_reportsale.prev_start.value="<%=start%>";
    document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="paymentaredit.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportar_list_payment.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportar_list_payment.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportar_list_payment.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportar_list_payment.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.start.value="<%=prev_start%>";
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="reportlistar.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.material.report.RekapPenjualanPerShiftPDFByDoc");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
            &nbsp;Laporan Penjualan > Rekap Piutang<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="hidden_group_id" value="">
			  <input type="hidden" name="indeks" value="<%=indeks%>">
              <input type="hidden" name="prev_start" value="<%=prev_start%>">
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
                    <b>Ref. Invoice: <%=rptArInvoice.getInvoiceNo()%> </b> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b>Member Name: 
                    <%=contact.getPersonName()%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,start,recordToGet)%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
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
                              <td nowrap width="5%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK,true)%>"></a></td>
                              <td class="command" nowrap width="95%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK,true)%></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="39%">&nbsp;</td>
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
