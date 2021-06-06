<%-- 
    Document   : reportposisistock_list_1_html
    Created on : Aug 4, 2016, 10:21:45 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@ page import="com.dimata.posbo.entity.search.SrcReportPotitionStock,
                 com.dimata.posbo.form.search.FrmSrcReportPotitionStock,
                 com.dimata.posbo.session.warehouse.SessReportPotitionStock,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.MaterialStock,
                 com.dimata.posbo.entity.masterdata.Unit,
                 com.dimata.posbo.entity.masterdata.Merk,
                 com.dimata.posbo.entity.masterdata.PstMerk,
                 com.dimata.posbo.form.masterdata.CtrlMaterialStock"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Category"%>
<%@ page language = "java" %>
<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
    {"Tidak ada data", "LAPORAN POSISI STOK", "Dari", "s/d", "Periode", "Lokasi", "Merk", "Kategori", "Semua",
             "Laporan Posisi Stock", "Cetak Laporan Posisi Stock", "Berdasarkan", "Nilai Stok dari","Supplier"},
    {"No data available", "STOCK POSITION REPORT", "From", "to", "Periode", "Location", "Merk", "Category", "All",
             "Stock Potition Report", "Print Stock Potition Report", "By", "Stock Value from","Supplier"}
};

public static final String textListTitleTable[][] = {
    {"NO","SKU","NAMA BARANG","HARGA BELI","UNIT","JUMLAH STOK","AWAL","OPNAME","TERIMA","TRANSFER","RETUR SUPP.","JUAL","RETUR CUST.","SALDO","NILAI STOK","HARGA JUAL","BARCODE","BIAYA"},
    {"NO","SKU","GOODS NAME","COGS","UNIT","QUANTITY STOCK","BEGINNING","OPNAME","RECEIVE","DISPATCH","SUPP. RETURN","SALE","CUST. RETURN","SALDO","STOCK VALUE","SALE PRICE","BARCODE","COST"}
};

public String drawList(int infoShowed, int stockValueBy, int language, Vector objectClass, int start, Vector grandTotalStockValue) {
    String result = "";
    
    try{
        if(objectClass!=null && objectClass.size()>0) { 
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("99%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(textListTitleTable[language][0],"3%","2","0");
            ctrlist.addHeader(textListTitleTable[language][1],"10%","2","0");
            ctrlist.addHeader(textListTitleTable[language][2],"16%","2","0");

            if((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) ||
                    (infoShowed == SrcReportPotitionStock.SHOW_BOTH &&  stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
                ctrlist.addHeader(textListTitleTable[language][3],"7%","2","0"); // HPP/COGS
            }

            if(infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                ctrlist.addHeader(textListTitleTable[language][4],"5%","2","0"); // UNIT
                ctrlist.addHeader(textListTitleTable[language][5],"0%","0","8"); // TITLE: QUANTITY
            }
            else if(infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
                ctrlist.addHeader(textListTitleTable[language][14],"0%","0","8"); // TITLE: VALUE
            }

            ctrlist.addHeader(textListTitleTable[language][6],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][7],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][8],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][9],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][10],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][11],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][12],"7%","0","0");
            ctrlist.addHeader(textListTitleTable[language][13],"7%","0","0");

            if(infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                ctrlist.addHeader(textListTitleTable[language][14],"0%","2","0");
            }

            /*if(infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY) {
                ctrlist.addHeader(textListTitleTable[language][14],"11%","2","0"); // STOCK VALUE CLOSING
            }*/

            Vector lstData = ctrlist.getData();

            double stockValueBegin = 0;
            double stockValueOpname = 0;
            double stockValueReceive = 0;
            double stockValueDispatch = 0;
            double stockValueReturn = 0;
            double stockValueSale = 0;
            double stockValueReturnCust = 0;
            double stockValueClosing = 0;

            double subTotalStockValueBegin = 0;
            double subTotalStockValueOpname = 0;
            double subTotalStockValueReceive = 0;
            double subTotalStockValueDispatch = 0;
            double subTotalStockValueReturn = 0;
            double subTotalStockValueSale = 0;
            double subTotalStockValueReturnCust = 0;
            double subTotalStockValueClosing = 0;

            double grandTotalStockValueBegin = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_BEGIN));
            double grandTotalStockValueOpname = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_OPNAME));
            double grandTotalStockValueReceive = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_RECEIVE));
            double grandTotalStockValueDispatch = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_DISPATCH));
            double grandTotalStockValueReturn = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_RETURN));
            double grandTotalStockValueSale = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_SALE));
            double grandTotalStockValueReturnCust = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_RETURN_CUST));
            double grandTotalStockValueClosing = Double.parseDouble((String)grandTotalStockValue.get(SessReportPotitionStock.STOCK_VALUE_CLOSING));

            for(int i=0; i<objectClass.size(); i++) {
                Vector vt = (Vector)objectClass.get(i);
                Material material = (Material)vt.get(0);
                MaterialStock materialStock = (MaterialStock)vt.get(1);
                Unit unit = (Unit)vt.get(2);
                Vector stockValue = (Vector)vt.get(3);
                
                System.out.println("sadfsdfdsfsd");
                
                if(infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                    stockValueBegin = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_BEGIN));
                    stockValueOpname = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_OPNAME));
                    stockValueReceive = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_RECEIVE));
                    stockValueDispatch = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_DISPATCH));
                    stockValueReturn = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_RETURN));
                    stockValueSale = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_SALE));
                    stockValueReturnCust = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_RETURN_CUST));
                    stockValueClosing = Double.parseDouble((String)stockValue.get(SessReportPotitionStock.STOCK_VALUE_CLOSING));

                    subTotalStockValueBegin += stockValueBegin;
                    subTotalStockValueOpname += stockValueOpname;
                    subTotalStockValueReceive += stockValueReceive;
                    subTotalStockValueDispatch += stockValueDispatch;
                    subTotalStockValueReturn += stockValueReturn;
                    subTotalStockValueSale += stockValueSale;
                    subTotalStockValueReturnCust += stockValueReturnCust;
                    subTotalStockValueClosing += stockValueClosing;
                }

                Vector rowx = new Vector();
                rowx.add(String.valueOf(start+i+1)+".");
                rowx.add(material.getSku());
                rowx.add(material.getName());

                if((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) || 
                        (infoShowed == SrcReportPotitionStock.SHOW_BOTH && stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>"); // HPP/COGS
                }

                if(infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                    rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQty())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getOpnameQty())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyIn())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyOut())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyMin())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getSaleQty())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getQtyMax())+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(materialStock.getClosingQty())+"</div>");
                } else if(infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueBegin)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueOpname)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueReceive)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueDispatch)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueReturn)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueSale)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueReturnCust)+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueClosing)+"</div>");
                }

                if(infoShowed == SrcReportPotitionStock.SHOW_BOTH) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(stockValueClosing)+"</div>");
                }

                /*if(infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice() * materialStock.getClosingQty())+"</div>");
                }*/

                lstData.add(rowx);
            }

            if(infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY) {

                /** sub total */
                Vector rowy = new Vector();
                rowy.add("");
                rowy.add("<b>Sub Total</b>");
                rowy.add("");
                if(stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
                    rowy.add("");
                }
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueBegin)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueOpname)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueReceive)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueDispatch)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueReturn)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueSale)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueReturnCust)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueClosing)+"</div>");

                lstData.add(rowy);

                /** grand total */
                rowy = new Vector();
                rowy.add("");
                rowy.add("<b>Grand Total</b>");
                rowy.add("");
                if(stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
                    rowy.add("");
                }
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueBegin)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueOpname)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueReceive)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueDispatch)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueReturn)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueSale)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueReturnCust)+"</div>");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueClosing)+"</div>");

                lstData.add(rowy);
            }
            else if (infoShowed == SrcReportPotitionStock.SHOW_BOTH) {

                /** sub total */
                Vector rowy = new Vector();
                rowy.add("");
                rowy.add("<b>Sub Total</b>");
                rowy.add("");
                if(stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
                    rowy.add("");
                }
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalStockValueClosing)+"</div>");

                lstData.add(rowy);

                /** grand total */
                rowy = new Vector();
                rowy.add("");
                rowy.add("<b>Grand Total</b>");
                rowy.add("");
                if(stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
                    rowy.add("");
                }
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("");
                rowy.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalStockValueClosing)+"</div>");

                lstData.add(rowy);
            }

            result = ctrlist.drawList(); 
        } else{
            result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>";
        }
    }catch(Exception e){
        System.out.println("err. "+e.toString());
    }
    return result;
}
%>

<%
/** judul laporan dibuat dinamis */
String reportTitle1 = FRMQueryString.requestString(request, "report_title1");
String reportTitle2 = FRMQueryString.requestString(request, "report_title2");

int iViewType = FRMQueryString.requestInt(request, "view_type");

/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector bodyPdf = new Vector(1,1);
Vector footerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);
ControlLine ctrLine = new ControlLine();
/** start fetch data */
SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock(request, srcReportPotitionStock);

try {
    srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
    if (srcReportPotitionStock == null) {
        srcReportPotitionStock = new SrcReportPotitionStock();
    }
    
} catch(Exception e) {
    srcReportPotitionStock = new SrcReportPotitionStock();
}

//session.putValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT, srcReportPotitionStock);


Location location = new Location();
if (srcReportPotitionStock.getLocationId() != 0) {
    try	{
        location = PstLocation.fetchExc(srcReportPotitionStock.getLocationId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][7]+" "+textListGlobal[SESS_LANGUAGE][4]);
}

Merk merk = new Merk();
if(srcReportPotitionStock.getMerkId() != 0) {
    try {
        merk = PstMerk.fetchExc(srcReportPotitionStock.getMerkId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    merk.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][6]);
}

Category category = new Category();
if(srcReportPotitionStock.getCategoryId() != 0) {
    try {
        category = PstCategory.fetchExc(srcReportPotitionStock.getCategoryId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    category.setName(textListGlobal[SESS_LANGUAGE][7]+" "+textListGlobal[SESS_LANGUAGE][6]);
}

/**
 * START GET POTITION STOCK REPORT
 * Prosedur proses mendapatkan laporan posisi stock
 * 1. Rekap data seluruh transaksi
 * 2. Mengambil nilai stok dan jumlah list dari data hasil rekap
 * 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList
 */

Vector stockValue = new Vector(1,1);
Vector vctReportStock  = new Vector(1,1);

/** untuk menentukkan apakah proses melibatkan stok bernilai nol */
boolean isCalculateZeroQty = true;

/** backup informasi dateFrom dan dateTo */
Date dateFrom = new Date();
Date dateTo = new Date();
dateFrom = srcReportPotitionStock.getDateFrom();
dateTo = srcReportPotitionStock.getDateTo();


Date realDateFrom = new Date();
Date realDateTo = new Date();
realDateFrom = (Date)srcReportPotitionStock.getDateFrom().clone();
realDateTo = (Date)srcReportPotitionStock.getDateTo().clone();

/** 1. Rekap data seluruh transaksi */
boolean isSummarryTransactionData = false;
if(srcReportPotitionStock.getGenerateReport()) {
    //isSummarryTransactionData = objSessReportPotitionStock.summarryTransactionData(false, srcReportPotitionStock, false);
    srcReportPotitionStock.setGeneratereport(false);
}
else {
    isSummarryTransactionData = true;
}

/** proses dibawah ini, oleh karena ada pengurangan hari pada instance srcReportPotitionStock */
dateFrom.setDate(dateFrom.getDate() + 1);
srcReportPotitionStock.setDateFrom(dateFrom);
srcReportPotitionStock.setDateTo(dateTo);

if(isSummarryTransactionData) { // cek apakah proses rekap data OK
    /** 2. Mengambil nilai stok dan jumlah list dari data hasil rekap */
    //Vector vctStockValue = objSessReportPotitionStock.getGrandTotalReportPosisiStockAll(srcReportPotitionStock, isCalculateZeroQty);
    //stockValue = (Vector)vctStockValue.get(1);
    
    /** 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList */
    //vctReportStock = objSessReportPotitionStock.reportPosisiStockAll(srcReportPotitionStock, "", 0, 0, isCalculateZeroQty);
}
else {
    System.out.println("Process Failed...");
}



/** get report title */
String reportTilte = "";
if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_QTY_ONLY) {
    reportTilte = (textListGlobal[SESS_LANGUAGE][11]+" "+SrcReportPotitionStock.stringInfoShowed[SESS_LANGUAGE][SrcReportPotitionStock.SHOW_QTY_ONLY]);
} else if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_VALUE_ONLY) {
    reportTilte = (textListGlobal[SESS_LANGUAGE][11]+" "+textListGlobal[SESS_LANGUAGE][12]);
    if(srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
        reportTilte += (" "+SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER]);
    } else if(srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION) {
        reportTilte += (" "+SrcReportPotitionStock.stringStockValueBy[SESS_LANGUAGE][SrcReportPotitionStock.STOCK_VALUE_BY_COGS_TRANSACTION]);
    }
}


response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "reportposisistok.xls" ); 
%>
<%@ page contentType="application/x-msexcel" %>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">

function cmdBack() {
    document.frm_reportstock.command.value="<%=Command.BACK%>";
    document.frm_reportstock.action="src_reportposisistock.jsp";
    document.frm_reportstock.submit();
}

function printForm() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("buff_pdf_stockpotitionreport.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>","form_stockpotitionreport");
}

function printForm() {
    //window.open("reportstock_form_print.jsp","stockposisireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("printFormExcel.jsp?report_title1=<%=textListGlobal[SESS_LANGUAGE][1]%>&report_title2=<%=reportTilte%>","form_stockpotitionreport");
}


//------------------------- START JAVASCRIPT FUNCTION FOR CTRLLINE -----------------------
function cmdListFirst() {
    document.frm_reportstock.command.value="<%=Command.FIRST%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListPrev() {
    document.frm_reportstock.command.value="<%=Command.PREV%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListNext() {
    document.frm_reportstock.command.value="<%=Command.NEXT%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}

function cmdListLast() {
    document.frm_reportstock.command.value="<%=Command.LAST%>";
    document.frm_reportstock.action="reportposisistock_list_1.jsp";
    document.frm_reportstock.submit();
}
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

<body bgcolor="#FFFFFF"  leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" -->
            <form name="frm_reportstock" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="approval_command">
               <table width="100%" cellspacing="0" cellpadding="3">
		<tr align="left" valign="top">
                  <td height="14" colspan="3" align="center" valign="middle">
                    <h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%><br>
                    <% if(srcReportPotitionStock.getInfoShowed() != SrcReportPotitionStock.SHOW_BOTH) { out.println("("+reportTilte+")"); } %>
                    </strong></h4>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%" height="25" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%>: <%=Formater.formatDate(realDateFrom, "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][3]%> <%=Formater.formatDate(realDateTo, "dd-MM-yyyy")%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="25" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%>:<%=location.getName().toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="25" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%>: <%=category.getName().toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="25" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%>: <%=merk.getName().toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="25" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][13]).toUpperCase()%>:
                      <%
                          String name=" Semua Supplier ";
                          if(srcReportPotitionStock.getSupplierId()!=0){
                              try{
                                  Contact contact = PstContact.fetchExc(srcReportPotitionStock.getSupplierId());
                                  name=contact.getCompName();
                              }catch(Exception ex){
                              }
                          }
                      %>
                      <%=name.toUpperCase()%></b></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td align="center" valign="middle" colspan="3">
               <%if(isSummarryTransactionData) {%>
                     <!-- list of report position stok -->
                     <table width="99%" class="listarea">
                         <tr>
                          <td>
                            <table width="100%" class="listgen" cellspacing="1" border="1">
                              <%
                                Vector vectMember = new Vector(1,1);
                                String[] strMember = null;
                                Vector listTypeHrga =  new Vector ();
                                strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
                                String sStrMember="";
                                int colspan = 9;
                                if(strMember!=null && strMember.length>0) {
                                       for(int i=0; i<strMember.length; i++) {
                                               try {
                                                   if(strMember[i] != null && strMember[i].length()>0){ 
                                                    vectMember.add(strMember[i]);
                                                   sStrMember=sStrMember+strMember[i]+",";
                                                   }
                                               }
                                               catch(Exception exc) {
                                                       System.out.println("err");
                                               }
                                       }
                                       if(sStrMember != null && sStrMember.length()>0){
                                            sStrMember=sStrMember.substring(0, sStrMember.length()-1);
                                            String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+sStrMember+")";
                                            listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
                                       }
                                       srcReportPotitionStock.setvPriceTypeId(listTypeHrga);
                                       colspan=colspan;
                                }
                            %>  
                              <tr>
                                    <td width="3%"  class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][0]%></td>
                                    <td width="7%" class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][1]%></td>
                                    <%if(iViewType==0){%>
                                        <td width="7%" class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][16]%></td>
                                    <%}%>
                                    <td width="12%" class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][2]%></td>
                                    <%
                                        int infoShowed = srcReportPotitionStock.getInfoShowed();
                                        int stockValueBy = srcReportPotitionStock.getStockValueBy();
                                        String cellStyle = "listgensell";

                                    if((infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY && stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) ||
                                     (infoShowed == SrcReportPotitionStock.SHOW_BOTH &&  stockValueBy == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER)) { %>
                                        <td width="7%"  class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][3]%></td>
                                        
                                        <%
                                        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
                                        if(listTypeHrga != null && listTypeHrga.size()>0 && iViewType==0){
                                              for(int i = 0; i<listTypeHrga.size();i++){
                                                  for(int j=0;j<listCurrStandardX.size();j++){
                                                      %>
                                                           <td width="7%"  class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][15]%></td>
                                                      <%
                                                  }
                                              }
                                          }
                                        %>
                                        
                                    <% } %>
                                    <% if(infoShowed == SrcReportPotitionStock.SHOW_QTY_ONLY || infoShowed == SrcReportPotitionStock.SHOW_BOTH) { %>
                                        <td width="5%"  class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][4]%></td>
                                        <td width="0%"  class="listgentitle" rowspan="0" colspan="<%=colspan%>" align="center"><%=textListTitleTable[SESS_LANGUAGE][5]%></td>
                                    <% } %>
                                    
                                    
                                    <%if(infoShowed == SrcReportPotitionStock.SHOW_VALUE_ONLY){ %>

                                    <td width="0%"  class="listgentitle" rowspan="0" colspan="<%=colspan%>" ><%=textListTitleTable[SESS_LANGUAGE][14]%></td>
                                    <% } %>
                                    <% if(infoShowed == SrcReportPotitionStock.SHOW_BOTH) { %>
                                    <td width="0%" class="listgentitle" rowspan="2" colspan="0" ><%=textListTitleTable[SESS_LANGUAGE][14]%></td>
                                    <%} %>
                                    <tr>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][6]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][7]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][8]%></td>
                                      
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][9]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][10]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][11]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][12]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][17]%></td>
                                      <td width="7%" class="listgentitle" ><%=textListTitleTable[SESS_LANGUAGE][13]%></td>
                                    </tr>
                                   
                                    <%if(iViewType == 0) {%>
                                        <% objSessReportPotitionStock.getReportStockAll(out, infoShowed, stockValueBy, SESS_LANGUAGE, 0, srcReportPotitionStock,isCalculateZeroQty, 0, 0, cellStyle);%>
                                    <%}else {%>
                                        <% objSessReportPotitionStock.getGrandTotalReportStockAll(out, infoShowed, stockValueBy, SESS_LANGUAGE, 0, srcReportPotitionStock,isCalculateZeroQty, 0, 0, cellStyle);%>
                                    <%}%>
                           
                            </table>
                          </td>
                        </tr>
                    </table>
                     <!-- End of list report position stock -->
                  </td>
                </tr>
                <%}else {%>
                <%//}%>
                <!-- Command If summary failed -->
                <tr>
                    <td>
                      <table width="99%" class="listarea">
                        <tr>
                          <td width="99%" align ="center">Process Failed...</td>
                        <tr>
                      </table>
                    </td>
                </tr>
                <%}%>
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
