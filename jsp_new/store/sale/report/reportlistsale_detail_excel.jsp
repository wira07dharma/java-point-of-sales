<%-- 
    Document   : reportlistsale_detail_excel
    Created on : Aug 18, 2016, 4:54:57 PM
    Author     : dimata005
--%>

<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
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
				 com.dimata.posbo.entity.masterdata.*,
				 com.dimata.pos.session.billing.SessBilling"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_DETAIL_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Laporan Penjualan", "Laporan Penjualan Invoice Detail","Semua Location",
            "Penjualan Cash","Penjualan Credit","Semua Penjualan","Penjualan Biasa", "Penjualan Konsinyasi"},
	{"Sale Report", "Invoice Detail Sale Report","All Location",
            "Cash Sales"," Credit Sales","All Sales", "Normal Sale", "Consignment Sale"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"No","Tgl","Nomor","Konsumen","Sales Bruto","Diskon","Pajak","Pelayanan","Netto Inv.","DP Deduction","HPP","Kasir","Detail Item","Catatan"},
	{"No","Date","Number","Customer","Sales Bruto","Discount","Tax","Service","Netto Inv.","DP Deduction","Cogs","Sales","Item Detail","Remark"}
};

public static final String textListMaterialDetailHeader[][] = {
	{"Kode","Nama Produk","Harga","Diskon","Qty","Unit","Harga Total","Barcode"},
	{"Code","Product Name","Price","Discount","Qty","Unit","Total Price","Barcode"}
};

public static final String textListTitleHeader[][] = {
	{"Laporan Rekap Penjualan Harian","LAPORAN REKAP PENJUALAN PER ","Tidak ada data transaksi ..","Lokasi","Shift","Laporan","Cetak Laporan Penjualan Invoice Detail","Tipe","Tanggal","s/d","Mata Uang"},
	{"Daily Sales Recapitulation Report","SALES RECAPITULATION REPORT PER SHIFT","No transaction data available ..","Location","Shift","Report","Print Invoice Detail Sale Report","Type","Date","to","Currency Type"}
};

public String drawList(int language, Vector objectClass, int start, SrcSaleReport srcSaleReport, long currencyId, int paymentDinamis, int itemType) {
    String result = "";
    String frmCurrency = "#,###";
        String frmCurrencyUsd = "#,###.##";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setBorder(1);
		ctrlist.dataFormat(textListMaterialHeader[language][0],"3%","0","0","center","bottom"); //no
                ctrlist.dataFormat(textListMaterialHeader[language][2],"5%","0","0","center","bottom"); // no inv
                ctrlist.dataFormat(textListMaterialHeader[language][1],"5%","0","0","center","bottom"); // tgl
                ctrlist.dataFormat(textListMaterialHeader[language][11],"5%","0","0","center","bottom"); // spg
                ctrlist.dataFormat(textListMaterialHeader[language][13],"5%","0","0","center","bottom"); // remark
                ctrlist.dataFormat(textListMaterialHeader[language][12],"40%","0","0","center","bottom"); // detail
                ctrlist.dataFormat(textListMaterialHeader[language][8],"9%","0","0","center","bottom"); // trans
                ctrlist.dataFormat(textListMaterialHeader[language][5],"7%","0","0","center","bottom"); // disc
                ctrlist.dataFormat(textListMaterialHeader[language][6],"7%","0","0","center","bottom"); // tax
                ctrlist.dataFormat(textListMaterialHeader[language][7],"7%","0","0","center","bottom"); // tax
                ctrlist.dataFormat(textListMaterialHeader[language][4],"9%","0","0","center","bottom"); // trans
		
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

                double bruto = 0;
		double diskon = 0;
		double pajak = 0;
		double service = 0;
		double netto = 0;
		
		double totalBruto = 0;
                double totalDisc = 0;
                double totalTax = 0;
                double totalService = 0;						
                double totalNetto = 0;
		
		double grandBruto = 0, grandDiskon = 0, grandPajak = 0, grandService = 0, grandNetto = 0;
		String designMat = String.valueOf(PstSystemProperty.getValueByName("DESIGN_MATERIAL_FOR"));
                int DESIGN_MATERIAL_FOR = Integer.parseInt(designMat);
                
                String where = "";
		double rate = 0;
		int no = 0;
                for(int i=0; i<objectClass.size(); i++) {
                    BillMain billMain = (BillMain)objectClass.get(i);

                    String where2 = " BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID] + "=" + billMain.getOID()
                            + " AND PM."+PstMaterial.fieldNames[PstMaterial.FLD_CONSIGMENT_TYPE]+" = " + itemType
                            + " AND BILL_DETAIL."+PstBillDetail.fieldNames[PstBillDetail.FLD_QUANTITY]+" > 0";
                            Vector list1 = SessBilling.listInvoiceDetail(0, 0, where2, "");
                        
                    if (list1.size() > 0) {
                    no = no +1;
                    ContactList contactlist = new ContactList();
                    try {
                        contactlist = PstContactList.fetchExc(billMain.getCustomerId());
                    }
                    catch(Exception e) {
                            System.out.println("Contact not found ...");
                    }
                    if(paymentDinamis!=2){
                        rate = billMain.getRate();
                    }else{
                        rate=1;
                    }
                    rowx = new Vector();			
                    rowx.add("<div align=\"center\">"+(no+1)+"</div>");
                    rowx.add("<div align=\"left\">"+billMain.getInvoiceNumber()+"</div>");
                    rowx.add("<div align=\"left\">"+Formater.formatDate(billMain.getBillDate(),"dd/MM/yyyy HH:mm:ss")+"</div>");
                    rowx.add("<div align=\"left\">"+PstAppUser.getNameCashier(billMain.getAppUserId())+"</div>");
                    rowx.add("<div align=\"left\">"+billMain.getNotes()+"</div>");
                    where = PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID();
                    Vector list = SessBilling.listInvoiceDetail(0, 0, where, "");
                    rowx.add(drawListDetail(language, list, currencyId, rate));

                    if(currencyId != 0) {
                            bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()); 
                            diskon = billMain.getDiscount();
                            pajak = billMain.getTaxValue();
                            service = billMain.getServiceValue();
                    }else {
                             bruto = PstBillDetail.getSumTotalItem(PstBillDetail.fieldNames[PstBillDetail.FLD_BILL_MAIN_ID]+"="+billMain.getOID()) * rate;
                            diskon = billMain.getDiscount() * rate;
                            pajak = billMain.getTaxValue() * rate;
                            service = billMain.getServiceValue() * rate;
                    }
                    //netto = bruto - diskon + pajak;
                    netto = bruto - diskon + pajak + service;

                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(bruto)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(diskon)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pajak)+"</div>");
                    //adding service value
                    //by Mirahu 23122011
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(service)+"</div>");
                    rowx.add("<div align=\"right\">"+Formater.formatNumber(netto, frmCurrency)+"</div>");

                    totalBruto += bruto;
                    totalDisc += diskon;
                    totalTax += pajak;
                    grandBruto += bruto;
                    //adding service value
                    //by Mirahu 23122011
                    totalService +=service;
                    totalNetto += netto;

                    lstData.add(rowx);
                    lstLinkData.add(String.valueOf(billMain.getOID()));
                    }
                }

                rowx = new Vector();
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                        rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"><b>Sub Total</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalBruto)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalDisc)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalTax)+"</b></div>");
                //adding service value
                //by Mirahu 23122011
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalService)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(totalNetto, frmCurrency)+"</b></div>");
                lstData.add(rowx);
                if(paymentDinamis==2){
                    srcSaleReport.setCurrencyOid(1);
                }
                Vector grandTotal = SessBilling.getGrandTotal(srcSaleReport);
                if(DESIGN_MATERIAL_FOR==1){
                }else{
                    grandBruto = Double.parseDouble((String)grandTotal.get(0));
                }

                grandDiskon = Double.parseDouble((String)grandTotal.get(1));
                grandPajak = Double.parseDouble((String)grandTotal.get(2));
                grandService = Double.parseDouble((String)grandTotal.get(3));
                grandNetto = grandBruto - grandDiskon + grandPajak + grandService;

                rowx = new Vector();
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"></div>");
                rowx.add("<div align=\"left\"><b>Grand Total</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandBruto)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandDiskon)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandPajak)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandService)+"</b></div>");
                rowx.add("<div align=\"right\"><b>"+Formater.formatNumber(grandNetto, frmCurrency)+"</b></div>");
                lstData.add(rowx);

                lstLinkData.add(String.valueOf(0));

                result = ctrlist.draw();
    }
    return result;
}


public String drawListDetail(int language,Vector objectClass, long currencyId, double rate) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		ctrlist.setHeaderStyle("listgentitle");
                ctrlist.setBorder(1);
		ctrlist.dataFormat(textListMaterialDetailHeader[language][0],"17%","0","0","center","bottom"); //sku
                ctrlist.dataFormat(textListMaterialDetailHeader[language][7],"17%","0","0","center","bottom"); //barcode
                ctrlist.dataFormat(textListMaterialDetailHeader[language][1],"17%","0","0","center","bottom"); //nama
                
		ctrlist.dataFormat(textListMaterialDetailHeader[language][2],"9%","0","0","center","bottom"); //harga
		ctrlist.dataFormat(textListMaterialDetailHeader[language][3],"9%","0","0","center","bottom"); //diskon
		ctrlist.dataFormat(textListMaterialDetailHeader[language][4],"7%","0","0","center","bottom"); //qty
		ctrlist.dataFormat(textListMaterialDetailHeader[language][5],"5%","0","0","center","bottom"); //unit
		ctrlist.dataFormat(textListMaterialDetailHeader[language][6],"13%","0","0","center","bottom"); //harga total

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++) {
			Vector temp = (Vector)objectClass.get(i);
			Billdetail billDetail = (Billdetail)temp.get(0);
			Unit unit = (Unit)temp.get(1);
                        Material material = (Material) temp.get(2);

			rowx = new Vector();
			rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>"+billDetail.getSku()+"</div>");
                        rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"' >"+material.getBarCode()+"</div>");
                        rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>"+billDetail.getItemName()+"</div>");
			if(currencyId != 0) {
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getItemPrice())+"</div>");
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getDisc())+"</div>");
			}
			else {
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getItemPrice() * rate)+"</div>");
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getDisc() * rate)+"</div>");
			}
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getQty())+"</div>");
			rowx.add("<div align=\"left\">"+unit.getCode()+"</div>");
			if(currencyId != 0) {
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice())+"</div>");
			}
			else {
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(billDetail.getTotalPrice() * rate)+"</div>");
			}

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(0));
		}
		result = ctrlist.draw();
	}
	return result;
}
	
%>

<%
CtrlBillMain ctrlBillMain = new CtrlBillMain(request);
int iCommand = FRMQueryString.requestCommand(request);
int iSaleReportType = FRMQueryString.requestInt(request, "sale_type");
int itemType = FRMQueryString.requestInt(request, "item_type");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 0;

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
    srcSaleReport = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
}
catch(Exception e) {
}

srcSaleReport.setTransType(iSaleReportType);
String startDate = Formater.formatDate(srcSaleReport.getDateFrom(),"yyyy-MM-dd 00:00:00");
String endDate = Formater.formatDate(srcSaleReport.getDateTo(),"yyyy-MM-dd 23:59:59");

/** tanggal transaksi */
String where = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE]+" BETWEEN '"+startDate+"' AND '"+endDate+"'";


if(iSaleReportType == -1){
 where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE] + "=" + PstBillMain.TYPE_INVOICE +
                              " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CASH +
                              " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_TYPE_CREDIT + ")" +
                              " AND (" + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_CLOSE +
                              " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")" ;
}else if(iSaleReportType == PstCashPayment.CASH) {
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
	where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE;
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_STATUS_CLOSE;
} else {
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_DOC_TYPE]+"="+PstBillMain.TYPE_INVOICE;
	where = where  + " AND ("+PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS]+"="+PstBillMain.TRANS_STATUS_CLOSE+
                         " OR " + PstBillMain.fieldNames[PstBillMain.FLD_TRANSACTION_STATUS] + "=" + PstBillMain.TRANS_STATUS_OPEN + ")";
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_TRANSCATION_TYPE]+"="+PstBillMain.TRANS_STATUS_OPEN;
}

/** shift */
if(srcSaleReport.getShiftId()!=0) {
        where = where  + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_SHIFT_ID]+"="+srcSaleReport.getShiftId();
}

/** lokasi */
if(srcSaleReport.getLocationId()!=0) {
	where = where + " AND "+PstBillMain.fieldNames[PstBillMain.FLD_LOCATION_ID]+"="+srcSaleReport.getLocationId();
}

/** currency */
if(srcSaleReport.getCurrencyOid()!=0) {
	where = where + " AND " + PstBillMain.fieldNames[PstBillMain.FLD_CURRENCY_ID]+"="+srcSaleReport.getCurrencyOid();
}

vectSize = PstBillMain.getCount(where);
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
	location.setName(textListGlobal[SESS_LANGUAGE][2]);
}

//add opie 23-06-2012
int paymentDinamis = 0;
paymentDinamis = Integer.parseInt(PstSystemProperty.getValueByName("USING_PAYMENT_DYNAMIC"));
String order = PstBillMain.fieldNames[PstBillMain.FLD_BILL_DATE];
Vector records = PstBillMain.list(start,recordToGet, where,order);
//Vector records = PstBillMain.list(0, 0, where, "");

String strCurrencyType = "All";
if(srcSaleReport.getCurrencyOid() != 0) {
	//Get currency code
	String whereClause = PstCurrencyType.fieldNames[PstCurrencyType.FLD_CURRENCY_TYPE_ID]+"="+srcSaleReport.getCurrencyOid();
	Vector listCurrencyType = PstCurrencyType.list(0, 0, whereClause, "");
	CurrencyType currencyType = (CurrencyType)listCurrencyType.get(0);
	strCurrencyType = currencyType.getCode();
}

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "reportlistsale_detail_excel.xls" ); 
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
	document.frm_reportsale.action="reportlistsale_detail.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportlistsale_detail.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.action="reportlistsale_detail.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.action="reportlistsale_detail.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportsale_detail.jsp";
	document.frm_reportsale.submit();
}

function printForm() {
	//window.open("buffer_inv_detail_pdf.jsp?sale_type="+<%=iSaleReportType%>+"","form_rpt_inv");
        window.open("reportsale_list_detail_global_excel.jsp?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	
}

function printExcel(){
    //window.open("<%=approot%>/SalesDetailExcel?sale_type=<%=iSaleReportType%>&command=<%=Command.LIST%>&language=<%=SESS_LANGUAGE%>","corectionwh","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("reportsale_list_detail_global_excel.jsp?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	
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
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            &nbsp;<%=textListGlobal[SESS_LANGUAGE][0]%> &gt; <%=textListGlobal[SESS_LANGUAGE][1]%><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_billmain_id" value="">
              <input type="hidden" name="sale_type" value="<%=iSaleReportType%>">			  
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
                    <b><%=textListTitleHeader[SESS_LANGUAGE][8]%> : <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> <%=textListTitleHeader[SESS_LANGUAGE][9]%> <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%></b> </td>
                </tr>				
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][7]%> : <%=(iSaleReportType==PstBillMain.TRANS_TYPE_CASH) ? textListGlobal[SESS_LANGUAGE][3] : iSaleReportType==PstBillMain.TRANS_TYPE_CREDIT?textListGlobal[SESS_LANGUAGE][4]:textListGlobal[SESS_LANGUAGE][5]%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><b><%=textListTitleHeader[SESS_LANGUAGE][10]%> : <%=strCurrencyType%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"><%=drawList(SESS_LANGUAGE, records, start, srcSaleReport, srcSaleReport.getCurrencyOid(),paymentDinamis, itemType)%></td>
                </tr>
              </table>
            </form>
            <!-- #EndEditable --></td> 
        </tr> 
      </table>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
