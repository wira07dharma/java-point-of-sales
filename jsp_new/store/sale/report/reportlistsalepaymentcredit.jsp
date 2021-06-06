<%@page import="com.dimata.common.entity.payment.PstPaymentSystem"%>
<%@ page import="com.dimata.posbo.entity.search.SrcSaleReport,
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
				 com.dimata.common.entity.payment.PstCurrencyType,
				 com.dimata.common.entity.payment.CurrencyType,
                 com.dimata.pos.entity.billing.*,
                 com.dimata.pos.entity.payment.*,
				 com.dimata.posbo.entity.masterdata.*"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_AR, AppObjInfo.OBJ_PAYMENT); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
    {"NO","TANGGAL","NOMOR","MEMBER","TOTAL","INVOICE REFERENSI","Tanggal"},
    {"NO","DATE","NUMBER","MEMBER","DP","INVOICE REFERENCE","Date"}
};

public static final String textListTitleHeader[][] =
{
    {"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER SHIFT","Tidak ada data transaksi ..","Lokasi","SHIFT","Laporan","Cetak Transaksi Harian","TIPE"},
    {"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","LOCATION","SHIFT","Report","Print Daily Transaction ","TYPE"}
};

public static final String textGlobal[][] =
{
           {" Laporan Penjualan > Pelunasan Piutang","Semua Location","Tipe","Return Penjualan Cash","Return Penjualan Credit","Mata Uang","Tanggal","Lokasi","Shift","Semua Shift","Kategori","Supplier","Kasir","Dari"},
           {"Sales Report > Settlement of receivables","All Location","Type","Return Sales Cash","Return Credit Sales","Currency","Date","Location","Shift","All Shift","Category","Vendor","Cashier","From"}
};

public String drawList(int language,Vector objectClass,SrcSaleReport srcSaleReport, int start) {
    
    String result = "";
    if(objectClass!=null && objectClass.size()>0){
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("1");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.dataFormat(textListMaterialHeader[language][0],"5%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][2],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][1],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][5],"15%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeader[language][3],"30%","0","0","center","bottom");
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
        for(int i=0; i<objectClass.size(); i++){
            Vector vect = (Vector)objectClass.get(i);
            BillMain billMain = (BillMain)vect.get(1);
            CreditPaymentMain creditPaymentMain = (CreditPaymentMain)vect.get(0);
            
            rowx = new Vector();
            ContactList contactlist = new ContactList();
            try{
                contactlist = PstContactList.fetchExc(billMain.getCustomerId());
            }catch(Exception e){}
            
            rowx.add("<div align=\"center\">"+(start+i+1)+"</div>");
            rowx.add("<div align=\"center\">"+creditPaymentMain.getInvoiceNumber()+"</div>");
            rowx.add("<div align=\"center\">"+Formater.formatDate(creditPaymentMain.getBillDate(),"dd/MM/yyyy")+"</div>");
            rowx.add("<div align=\"center\">"+billMain.getInvoiceNumber()+"</div>");
            rowx.add("<div align=\"left\">"+contactlist.getPersonName()+"</div>");
            double tot = PstCashCreditPayment.getTotalCreditPayment(creditPaymentMain.getOID()); //PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID());
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(tot)+"</div>");
            
            total = total + tot;
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(creditPaymentMain.getOID()));
        }
        
        rowx = new Vector();
        rowx.add("<div align=\"center\"></div>");
        rowx.add("<div align=\"center\"></div>");
        rowx.add("<div align=\"center\"></div>");
        rowx.add("<div align=\"center\"></div>");
        rowx.add("<div align=\"left\"><b>TOTAL</b></div>");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(total)+"</b></div>");
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));
        
        result = ctrlist.draw();
    }
    return result;
}


public String drawListCreditPayment(Vector vListPayment) {
    String sResult = "";
    if(vListPayment!=null && vListPayment.size()>0) {
        // get list currency type
        Vector vCurrType = PstCurrencyType.list(0,0,"","");
        Hashtable hashCurrType = new Hashtable();
        if(vCurrType!=null && vCurrType.size()>0) {
            int iCurrTypeCount = vCurrType.size();
            for(int i=0; i<iCurrTypeCount; i++) {
                CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
                hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
            }
        }
        
        
        String sHeader = generateHeader();
        String strContent = "";
        
        double dTotalPayment = 0;
        int iListPaymentCount = vListPayment.size();
        for(int i=0; i<iListPaymentCount; i++) {
            Vector vObj = (Vector) vListPayment.get(i);
            if(vObj!=null && vObj.size()>0) {
                double dPaymentPerType = 0;
                String strPaymentTypeName = "";
                for(int j=0; j<vObj.size(); j++) {
                    CashCreditPayments objCashCreditPayments = (CashCreditPayments) vObj.get(j);
                    
                    String strPaymentType = PstCashPayment.paymentType[objCashCreditPayments.getPaymentType()];
                    String strNumber = String.valueOf(j+1);
                    String strCurrency = String.valueOf(hashCurrType.get(""+objCashCreditPayments.getCurrencyId()));
                    String strAmount = FRMHandler.userFormatStringDecimal( (objCashCreditPayments.getAmount()/objCashCreditPayments.getRate() ));
                    String strRate = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getRate());
                    String strTotal = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getAmount());
                    dPaymentPerType = dPaymentPerType + objCashCreditPayments.getAmount();
                    
                    if(j==0) {
                        strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
                        strPaymentTypeName = strPaymentType.toUpperCase();
                    }
                    
                    strContent = strContent + generateContent(strNumber, strCurrency, strAmount, strRate, strTotal);
                }
                
                strContent = strContent + generateItemFooter("TOTAL "+strPaymentTypeName, FRMHandler.userFormatStringDecimal(dPaymentPerType));
                strContent = strContent + generateBlankSpace();
                dTotalPayment = dTotalPayment + dPaymentPerType;
            }
        }
        
        String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));
        sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
    }
    
    return sResult;
}

public String drawListCreditPaymentDinamis(Vector vListPayment) {
    String sResult = "";
    if(vListPayment!=null && vListPayment.size()>0) {
        // get list currency type
        Vector vCurrType = PstCurrencyType.list(0,0,"","");
        Hashtable hashCurrType = new Hashtable();
        if(vCurrType!=null && vCurrType.size()>0) {
            int iCurrTypeCount = vCurrType.size();
            for(int i=0; i<iCurrTypeCount; i++) {
                CurrencyType objCurrencyType = (CurrencyType) vCurrType.get(i);
                hashCurrType.put(""+objCurrencyType.getOID(), objCurrencyType.getName()+"("+objCurrencyType.getCode()+")");
            }
        }


        String sHeader = generateHeader();
        String strContent = "";

        double dTotalPayment = 0;
        int iListPaymentCount = vListPayment.size();
        for(int i=0; i<iListPaymentCount; i++) {
            Vector vObj = (Vector) vListPayment.get(i);
            if(vObj!=null && vObj.size()>0) {
                double dPaymentPerType = 0;
                String strPaymentTypeName = "";
                for(int j=0; j<vObj.size(); j++) {
                    CashCreditPayments objCashCreditPayments = (CashCreditPayments) vObj.get(j);

                    String strPaymentType = PstCashPayment.paymentType[objCashCreditPayments.getPaymentType()];
                    String strNumber = String.valueOf(j+1);
                    String strCurrency = String.valueOf(hashCurrType.get(""+objCashCreditPayments.getCurrencyId()));
                    String strAmount = FRMHandler.userFormatStringDecimal( (objCashCreditPayments.getAmount()/objCashCreditPayments.getRate() ));
                    String strRate = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getRate());
                    String strTotal = FRMHandler.userFormatStringDecimal(objCashCreditPayments.getAmount());
                    dPaymentPerType = dPaymentPerType + objCashCreditPayments.getAmount();

                    if(j==0) {
                        strContent = strContent + generateItemHeader(strPaymentType.toUpperCase());
                        strPaymentTypeName = strPaymentType.toUpperCase();
                    }

                    strContent = strContent + generateContent(strNumber, strCurrency, strAmount, strRate, strTotal);
                }

                strContent = strContent + generateItemFooter("TOTAL "+strPaymentTypeName, FRMHandler.userFormatStringDecimal(dPaymentPerType));
                strContent = strContent + generateBlankSpace();
                dTotalPayment = dTotalPayment + dPaymentPerType;
            }
        }

        String sFooter = generateFooter("TOTAL PAYMENT", FRMHandler.userFormatStringDecimal(dTotalPayment));
        sResult = "<table width=\"50%\" class=\"listgen\" cellspacing=\"1\" border=\"0\">" + sHeader + strContent + sFooter + "</table>";
    }

    return sResult;
}


public String generateHeader() {
    return "<tr>" +
            "<td width=\"4%\" class=\"listgentitle\">NO</td>" +
            "<td width=\"24%\" class=\"listgentitle\">CURRENCY</td>" +
            "<td width=\"23%\" class=\"listgentitle\">AMOUNT</td>" +
            "<td width=\"23%\" class=\"listgentitle\">RATE</td>" +
            "<td width=\"26%\" class=\"listgentitle\">TOTAL</td>" +
            "</tr>";
}

public String generateItemHeader(String strItemHeader) {
    return "<tr valign=\"top\">" +
            "<td class=\"listgensell\" colspan=\"5\"><b>"+strItemHeader+"</b></td>" +
            "</tr>";
}

public String generateContent(String strNum, String strCurrency, String strAmount, String strRate, String strTotal) {
    return "<tr valign=\"top\">" +
            "<td width=\"4%\" class=\"listgensell\" align=\"center\">"+strNum+"</td>" +
            "<td width=\"24%\" class=\"listgensell\">"+strCurrency+"</td>" +
            "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strAmount+"</td>" +
            "<td width=\"23%\" class=\"listgensell\" align=\"right\">"+strRate+"</td>" +
            "<td width=\"26%\" class=\"listgensell\" align=\"right\">"+strTotal+"</td>" +
            "</tr>";
}

public String generateItemFooter(String strItemFooter, String strValue) {
    return "<tr valign=\"top\">" +
            "<td class=\"listgensell\" colspan=\"4\"><b>"+strItemFooter+"</b></td>" +
            "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
            "</tr>";
}

public String generateFooter(String strFooter, String strValue) {
    return "<tr valign=\"top\">" +
            "<td class=\"listgensell\" colspan=\"4\" ><b>"+strFooter+"</b></td>" +
            "<td class=\"listgensell\" width=\"26%\" align=\"right\"><b>"+strValue+"</b></td>" +
            "</tr>";
}

public String generateBlankSpace() {
    return "<tr valign=\"top\">"  +
            "<td class=\"listgensell\" colspan=\"5\">&nbsp;</td>" +
            "</tr>";
}

%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 100;

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
    if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK) {
        srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
    }
} catch(Exception e) {
    
}

//String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd");
//String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd");
//String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'"+
//       " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_TYPE_CREDIT;

//if(srcSaleReport.getLocationId()!=0){
//    where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
//}
int paymentDinamis = 0;
paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
    vectSize = PstCreditPaymentMain.getCountPaymentCreditDinamis(srcSaleReport);
}else{
    vectSize = PstCreditPaymentMain.getCountPaymentCredit(srcSaleReport);
}


if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        start = ctrlBillMain.actionList(iCommand,start,vectSize,recordToGet);
        if (srcSaleReport== null) {
            srcSaleReport= new SrcSaleReport();
        }
    } catch(Exception e) {
        srcSaleReport = new SrcSaleReport();
    }
} else {
    session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
}

Location location = new Location();
try {
    location = PstLocation.fetchExc(srcSaleReport.getLocationId());
} catch(Exception e) {
    location.setName("Semua toko");
}

Vector records = new Vector();
if(paymentDinamis == PstPaymentSystem.USING_PAYMENT_DINAMIS) {
    records=PstCreditPaymentMain.getDataPaymentCreditDinamis(srcSaleReport,start,recordToGet);
}else{
    records=PstCreditPaymentMain.getDataPaymentCredit(srcSaleReport,start,recordToGet);
}

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
    document.frm_reportsale.command.value="<%=Command.EDIT%>";
	document.frm_reportsale.action="paymentcreditedit.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.action="reportlistsalepaymentcredit.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistsalepaymentcredit.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistsalepaymentcredit.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistsalepaymentcredit.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportsalepaymentcredit.jsp";
	document.frm_reportsale.submit();
	//history.back();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.RekapPenjualanPerShiftPDFByDoc");
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> &nbsp;<%=textGlobal[SESS_LANGUAGE][0]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_group_id" value="">
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
                    <b><%=textListMaterialHeader[SESS_LANGUAGE][6]%> : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> s/d <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE,records,srcSaleReport, start)%></td>
                </tr>	
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> 				  
				  <span class="command">
				  <%
					ctrLine.setLocationImg(approot+"/images");
					ctrLine.initDefault();
					out.println(ctrLine.drawImageListLimit(iCommand,vectSize,start,recordToGet));
				  %>
				  </span>
				 </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="61%"> 
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="95%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
                        </td>
                        <td width="39%">&nbsp;</td>
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
