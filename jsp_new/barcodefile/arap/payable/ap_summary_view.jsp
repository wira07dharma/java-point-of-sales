<% 
/* 
 * Page Name  		:  ap_summary_view.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Page Description : [project description ... ] 
 * Imput Parameters : [input parameter ...] 
 * Output 			: [output ...] 
 *******************************************************************/
%>
 
<%@ page language="java" %>
<!-- package java -->
<%@ page import="java.util.*"%>
<!-- package qdep -->
<%@ page import="com.dimata.util.*"%>
<%@ page import="com.dimata.gui.jsp.*"%>
<%@ page import="com.dimata.qdep.form.*"%>
<%@ page import="com.dimata.qdep.entity.*"%>
<!-- package project -->
<%@ page import="com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import="com.dimata.common.entity.payment.PaymentSystem"%>
<%@ page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@ page import="com.dimata.common.form.payment.FrmPaymentSystem"%>
<%@ page import="com.dimata.common.entity.payment.PaymentInfo"%>
<%@ page import="com.dimata.common.entity.payment.PstPaymentInfo"%>
<%@ page import="com.dimata.common.form.payment.FrmPaymentInfo"%>
<%@ page import="com.dimata.common.entity.payment.DailyRate"%>
<%@ page import="com.dimata.common.entity.payment.PstDailyRate"%>

<%@ page import="com.dimata.posbo.entity.search.SrcAccPayable"%>
<%@ page import="com.dimata.posbo.form.search.FrmSrcAccPayable"%>
<%@ page import="com.dimata.posbo.session.arap.SessAccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.AccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.PstAccPayable"%>
<%@ page import="com.dimata.posbo.entity.arap.AccPayableDetail"%>
<%@ page import="com.dimata.posbo.entity.arap.PstAccPayableDetail"%>
<%@ page import="com.dimata.posbo.form.arap.FrmAccPayable"%>
<%@ page import="com.dimata.posbo.form.arap.CtrlAccPayable"%>
<%@ page import="com.dimata.posbo.form.arap.FrmAccPayableDetail"%>
<%@ page import="com.dimata.posbo.form.arap.CtrlAccPayableDetail"%>
<%@ page import="com.dimata.posbo.entity.warehouse.PstMatReceiveItem"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MatReceiveItem"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Unit"%>
<%@ page import="com.dimata.posbo.session.warehouse.SessMatReturn"%>

<%@ include file = "../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PURCHASING, AppObjInfo.G2_AP, AppObjInfo.OBJ_AP_SUMMARY); %>
<%@ include file = "../../main/checkuser.jsp" %>

<%!
public static final String textGlobalTitle[][] = {
	{
	 "Gudang","Penerimaan Barang","Laporan","Rekap Hutang","Kembali ke Pencarian","Sub Total","Grand Total", //0-6
	 "Daftar hutang tidak ditemukan!","Cetak Laporan Rekap Hutang","LAPORAN REKAP HUTANG" //7-9
	},
	{
	 "Warehouse","Receive Goods","Report","AP Summary","Back To Search","Sub Total","Grand Total", //0-6
	 "Account Payable list not found!","Print AP Summary Report","AP SUMMARY REPORT" //7-9
	}
};

public static final String textListHeader[][] = {
	{"Nama Supplier","Nomor Invoice","Tgl. Invoice","Ket.","Rincian Item","Total","Diskon","Pajak","Retur","Pembayaran","Saldo Hutang"},
	{"Supplier Name", "Invoice Number","Invoice Date","Remark","Detail Item","Total","Discount","Tax","Return","Payment","AP Balance"}
};

public static final String textListItemHeader[][] = {
	{"Kode","Unit","Harga","Qty","Total"},
	{"Code","Unit","Price","Qty","Total"}
};

public String drawList(int start, int language , Vector objectClass, SrcAccPayable srcAccPayable, double dailyRate){
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("99%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader("No.","3%");
	ctrlist.addHeader(textListHeader[language][0],"12%");
	ctrlist.addHeader(textListHeader[language][1],"10%");
	ctrlist.addHeader(textListHeader[language][2],"7%");
	ctrlist.addHeader(textListHeader[language][3],"8%");
	ctrlist.addHeader(textListHeader[language][4],"10%");
	ctrlist.addHeader(textListHeader[language][5],"10%");
	ctrlist.addHeader(textListHeader[language][7],"10%");
	ctrlist.addHeader(textListHeader[language][8],"10%");
	ctrlist.addHeader(textListHeader[language][9],"10%");
	ctrlist.addHeader(textListHeader[language][10],"10%");

	ctrlist.setLinkRow(0);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;
	
	double subTotalAmount = 0;
	double subTotalTax = 0;
	double subTotalRetur = 0;
	double subTotalPayment = 0;
	double subTotalApSaldo = 0;
	double totalAmount = 0;
	double totalTax = 0;
	double totalRetur = 0;
	double totalPayment = 0;
	double totalApSaldo = 0;
	Vector listMatReceiveItem = new Vector(1,1);
	String whereClause = "";
	
	/** untuk mendapatkan nilai grand total */
	//Vector grandTotal = SessAccPayable.getListAP(srcAccPayable, 0, 0);
        Vector grandTotal = SessAccPayable.getListAP(srcAccPayable, 0, 0, 0);
	for(int m=0; m<grandTotal.size(); m++) {
		Vector temp3 = (Vector)grandTotal.get(m);
		totalAmount += Double.parseDouble((String)temp3.get(5));
		totalTax += Double.parseDouble((String)temp3.get(6));
	}
	//totalRetur = SessMatReturn.getTotalReturnByReceive(0);
        totalRetur = SessMatReturn.getTotalReturnByReceive(srcAccPayable,0);
	totalPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, 0))/dailyRate;
	totalApSaldo = (totalAmount + totalTax) - totalPayment;
	
	for (int i = 0; i < objectClass.size(); i++) {
		rowx = new Vector();
		Vector temp = (Vector)objectClass.get(i);
		long oidInvoice = Long.parseLong((String)temp.get(0));
		double total = Double.parseDouble((String)temp.get(5));
		double tax = Double.parseDouble((String)temp.get(6));
		//double retur = SessMatReturn.getTotalReturnByReceive(oidInvoice);
                double retur = SessMatReturn.getTotalReturnByReceive(srcAccPayable, oidInvoice);
		double apPayment = (SessAccPayable.getTotalAPPayment(srcAccPayable, oidInvoice))/dailyRate;
		double apSaldo = (total + tax) - apPayment - retur;
		
		subTotalAmount += total;
		subTotalTax += tax;
		subTotalRetur += retur;
		subTotalPayment += apPayment;
		subTotalApSaldo += apSaldo;
		
		rowx.add("<div align=\"center\">"+String.valueOf(start+i+1)+"<div>");
		rowx.add((String)temp.get(1));
		rowx.add((String)temp.get(2));
		rowx.add(Formater.formatDate((Date)temp.get(4), "dd MMM yyyy"));
		rowx.add((String)temp.get(3));
		
		/** detail item */
		listMatReceiveItem = new Vector(1,1);
		whereClause = PstMatReceiveItem.fieldNames[PstMatReceiveItem.FLD_RECEIVE_MATERIAL_ID]+"="+oidInvoice;
		listMatReceiveItem = PstMatReceiveItem.list(0 , 0, whereClause);
		rowx.add(drawListItem(language, listMatReceiveItem));
		
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(total)+"<div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(tax)+"<div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(retur)+"<div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(apPayment)+"<div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(apSaldo)+"<div>");
		lstData.add(rowx);
	}
	
	rowx = new Vector(1,1);
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"right\"><strong>Sub Total</strong></div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(subTotalAmount)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(subTotalTax)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(subTotalRetur)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(subTotalPayment)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(subTotalApSaldo)+"</strong><div>");
	lstData.add(rowx);
	
	rowx = new Vector(1,1);
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"right\"><strong>Grand Total</strong></div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(totalAmount)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(totalTax)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(totalRetur)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(totalPayment)+"</strong><div>");
	rowx.add("<div align=\"right\"><strong>"+FRMHandler.userFormatStringDecimal(totalApSaldo)+"</strong><div>");
	lstData.add(rowx);
	
	return ctrlist.draw();
}

public String drawListItem(int language , Vector objectClass){
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textListItemHeader[language][0],"25%");
	ctrlist.addHeader(textListItemHeader[language][1],"10%");
	ctrlist.addHeader(textListItemHeader[language][2],"25%");
	ctrlist.addHeader(textListItemHeader[language][3],"15%");
	ctrlist.addHeader(textListItemHeader[language][4],"25%");
	
	ctrlist.setLinkRow(0);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	Vector rowx = new Vector(1,1);
	ctrlist.reset();
	int index = -1;

	for (int i = 0; i < objectClass.size(); i++) {
		Vector listMatReceiveItem = (Vector)objectClass.get(i);
		MatReceiveItem recItem = (MatReceiveItem)listMatReceiveItem.get(0);
		Material mat = (Material)listMatReceiveItem.get(1);
		Unit unit = (Unit)listMatReceiveItem.get(2);
		
		rowx = new Vector(1,1);
		rowx.add(mat.getSku());
		rowx.add(unit.getCode());
		rowx.add(FRMHandler.userFormatStringDecimal(recItem.getCost()));
		rowx.add(FRMHandler.userFormatStringDecimal(recItem.getQty()));
		rowx.add(FRMHandler.userFormatStringDecimal(recItem.getTotal()));
		
		lstData.add(rowx);
	}
	
	return ctrlist.draw();
}

%>

<!-- JSP Block -->
<%
int iErrCode = FRMMessage.ERR_NONE;
String errMsg = "";
int recordToGet = 10;
int vectSize = 0;
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");

Vector records = new Vector();
ControlLine ctrLine = new ControlLine();
Control control = new Control();

SrcAccPayable srcAccPayable = new SrcAccPayable();
FrmSrcAccPayable frmSrcAccPayable = new FrmSrcAccPayable(request, srcAccPayable);
/*frmSrcAccPayable.requestEntityObject(srcAccPayable);
//vector status
      Vector vectSt = new Vector(1,1);
       String[] strStatus = request.getParameterValues(FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_AP_STATUS]);
       if(strStatus!=null && strStatus.length>0) {
         for(int i=0; i<strStatus.length; i++) {
	   try {
                    vectSt.add(strStatus[i]);
                }
            catch(Exception exc) {
                    System.out.println("err");
                    }
                 }
            }*/


if(iCommand != Command.LIST) {
	try{ 
		srcAccPayable = (SrcAccPayable)session.getValue(SessAccPayable.SESS_ACC_PAYABLE); 
		if (srcAccPayable == null)
			srcAccPayable = new SrcAccPayable();			
	}catch(Exception e){ 
		srcAccPayable = new SrcAccPayable();
	}
}
else {
    frmSrcAccPayable.requestEntityObject(srcAccPayable);
 //vector status
      Vector vectSt = new Vector(1,1);
       String[] strStatus = request.getParameterValues(FrmSrcAccPayable.fieldNames[FrmSrcAccPayable.FRM_FIELD_AP_STATUS]);
       if(strStatus!=null && strStatus.length>0) {
         for(int i=0; i<strStatus.length; i++) {
	   try {
                    vectSt.add(strStatus[i]);
                }
            catch(Exception exc) {
                    System.out.println("err");
                    }
                 }
            }
       srcAccPayable.setApstatus(vectSt);
       session.putValue(SessAccPayable.SESS_ACC_PAYABLE, srcAccPayable);
}
//srcAccPayable.setApstatus(vectSt);
//session.putValue(SessAccPayable.SESS_ACC_PAYABLE, srcAccPayable);

//vectSize = 	SessAccPayable.countListAP(srcAccPayable);
vectSize = SessAccPayable.countListAP(srcAccPayable, 0);

if((iCommand==Command.FIRST)||(iCommand==Command.NEXT)||(iCommand==Command.PREV)||(iCommand==Command.LAST)||(iCommand==Command.LIST)) {
	start = control.actionList(iCommand, start, vectSize, recordToGet);
}

//records = SessAccPayable.getListAP(srcAccPayable, start, recordToGet);
records = SessAccPayable.getListAP(srcAccPayable, start, recordToGet,0);

/** Untuk mendapatkan besarnya daily rate dalam satuan Rp */
/**
 * Yang dibagi dengan variabel dailyRate yaitu: 
 * dr pada function javascript:calculate(), totalPayment, apPayment dan amount
 * Ini berfungsi untuk mengasilkan amount yang sesuai dengan search key yang diminta (Rp atau USD)
 */
String whereClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_CURRENCY_TYPE_ID]+" = "+srcAccPayable.getCurrencyId();
String orderClause2 = PstDailyRate.fieldNames[PstDailyRate.FLD_ROSTER_DATE]+" DESC";
Vector listDailyRate2 = PstDailyRate.list(0, 0, whereClause2, orderClause2);
DailyRate dr2 = (DailyRate)listDailyRate2.get(0);
double dailyRate = dr2	.getSellingRate();


/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);

Vector compTelpFax = (Vector)companyAddress.get(2);
headerPdf.add(0,(String) companyAddress.get(0));
headerPdf.add(1,(String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));

%>
<!-- End of JSP Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>

function printForm() {
	window.open("buffer_ap_summary_pdf.jsp","form_ap_detail");
}

//<!--
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
function hideObjectForSystem(){
}

function cmdBackToSearch(){
	document.frap.command.value="<%=Command.BACK%>";
	document.frap.action="ap_summary_search.jsp";
	document.frap.submit();
}

function cmdListFirst(){
	document.frap.command.value="<%=Command.FIRST%>";
	document.frap.action="ap_summary_view.jsp";
	document.frap.submit();
}

function cmdListPrev(){
	document.frap.command.value="<%=Command.PREV%>";
	document.frap.action="ap_summary_view.jsp";
	document.frap.submit();
}

function cmdListNext(){
	document.frap.command.value="<%=Command.NEXT%>";
	document.frap.action="ap_summary_view.jsp";
	document.frap.submit();
}

function cmdListLast(){
	document.frap.command.value="<%=Command.LAST%>";
	document.frap.action="ap_summary_view.jsp";
	document.frap.submit();
}

function cmdBack(){
	document.frap.command.value="<%=Command.BACK%>";
	document.frap.oid_invoice_selected.value="0";
	document.frap.oid_payment_selected.value="0";
	document.frap.action="ap_summary_view.jsp";
	document.frap.submit();
}

function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


function setstock(prodOID,wareHouse0ID){
  	window.open("set_stock.jsp","",'status=yes, scrollbars,width=650,height=550');
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td height="25" ID="TOPTITLE"> <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --></td> 
  </tr> 
  <tr> 
    <td height="20" ID="MAINMENU"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->
            <%=textGlobalTitle[SESS_LANGUAGE][0]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][1]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][2]%> &gt; <%=textGlobalTitle[SESS_LANGUAGE][3]%>
			<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
		  <form name="frap" method="post" action="">
		  	<input type="hidden" name="command" value="<%=iCommand%>">
			<input type="hidden" name="start" value="<%=start%>">
            <table width="100%" border="0" cellspacing="1" cellpadding="1">                           
              <tr> 
                  <td valign="middle" colspan="2"> 
                    <table border="0" width="100%" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td height="8" class="comment"><hr size="1"></td>
                      </tr>
					  <tr align="left" valign="top"> 
                 		<td height="14" valign="middle" colspan="3" align="center"><b><%=textGlobalTitle[SESS_LANGUAGE][9]%></b></td>
					  </tr>
					  <tr> 
                        <td>&nbsp;</td>
                      </tr>
					  <% if(records.size() > 0) { /*tampilan ini untuk kondisi list yang dicari not empty*/ %>					
                      <tr> 
                        <td width="100%" align="center"><%=drawList(start, SESS_LANGUAGE, records, srcAccPayable, dailyRate)%></td>
					  </tr>
					  <tr>
					  	<td>
						<span class="command"> 
						<%
						ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();
						out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
						%> 
						</span>
						</td>
					  </tr>
					  <% } else { /*tampilan ini untuk kondisi list yang dicari empty*/ %>
                      <tr> 
                        <td height="8" width="10">&nbsp;</td>
                      </tr>
                      <tr> 
                        <td height="2" width="100%" class="comment"><strong><%=textGlobalTitle[SESS_LANGUAGE][7]%></strong></td>
                      </tr>
					  <% } %>
					  <tr> 
                        <td height="8" width="10">&nbsp;</td>
                      </tr>
                    </table>
					  <table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr> 
						  <td nowrap align="left" class="command">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
                              <tr>
                                <td width="1"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                <td width="25"><a href="javascript:cmdBackToSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=textGlobalTitle[SESS_LANGUAGE][4]%>"></a></td>
                                <td width="5"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
								<td height="22" valign="middle" colspan="3"><a href="javascript:cmdBackToSearch()" class="command"><%=textGlobalTitle[SESS_LANGUAGE][4]%></a></td>
								<% if(records.size() > 0) { %>
								<td width="31" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" ></a></td>
								<td width="195" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textGlobalTitle[SESS_LANGUAGE][8]%></a></td>
								<% } %>
                              </tr>
                            </table></td>
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
      <%@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
