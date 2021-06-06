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
             "Laporan Posisi Stock", "Cetak Laporan Posisi Stock", "Berdasarkan", "Nilai Stok dari"},
    {"No data available", "STOCK POSITION REPORT", "From", "to", "Periode", "Location", "Merk", "Category", "All",
             "Stock Potition Report", "Print Stock Potition Report", "By", "Stock Value from"}
};

public static final String textListTitleTable[][] = {
    {"NO","SKU","NAMA BARANG","HPP","UNIT","JUMLAH STOK","AWAL","OPNAME","TERIMA","TRANSFER","RETUR SUPP.","JUAL","RETUR CUST.","SALDO","NILAI STOK"},
    {"NO","SKU","GOODS NAME","COGS","UNIT","QUANTITY STOCK","BEGINNING","OPNAME","RECEIVE","DISPATCH","SUPP. RETURN","SALE","CUST. RETURN","SALDO","STOCK VALUE"}
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
/**
 * instantiate some object used in this page
 */
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int type = FRMQueryString.requestInt(request, "type");
CtrlMaterialStock ctrlMaterialStock = new CtrlMaterialStock(request);
ControlLine ctrLine = new ControlLine();
SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock(request, srcReportPotitionStock);

String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;

/**
 * handle current search data session
 */
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
        if (srcReportPotitionStock == null) srcReportPotitionStock = new SrcReportPotitionStock();
    } catch(Exception e) {
        srcReportPotitionStock = new SrcReportPotitionStock();
    }
} else {
    frmSrcReportPotitionStock.requestEntityObject(srcReportPotitionStock);
    session.putValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT, srcReportPotitionStock);
}

Location location = new Location();
if (srcReportPotitionStock.getLocationId() != 0) {
    try	{
        location = PstLocation.fetchExc(srcReportPotitionStock.getLocationId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][5]);
}

Category category = new Category();
if(srcReportPotitionStock.getCategoryId() != 0) {
    try {
        category = PstCategory.fetchExc(srcReportPotitionStock.getCategoryId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    category.setName(textListGlobal[SESS_LANGUAGE][8]+" "+textListGlobal[SESS_LANGUAGE][7]);
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

//proses pengambilan userid dilakukan di javainit
srcReportPotitionStock.setUserId(userId);

/**
 * START GET POTITION STOCK REPORT
 * Prosedur proses mendapatkan laporan posisi stock
 * 1. Rekap data seluruh transaksi
 * 2. Mengambil nilai stok dan jumlah list dari data hasil rekap
 * 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList
 */

if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_QTY_ONLY) srcReportPotitionStock.setStockValueBy(SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER);

int recordToGet = 10;
int vectSize = 0;
Vector stockValue = new Vector(1,1);
Vector vctReportStock  = new Vector(1,1);

/** untuk menentukkan apakah proses melibatkan stok bernilai nol */
boolean isCalculateZeroQty = true;

/** backup informasi dateFrom dan dateTo */
Date dateFrom = new Date();
Date dateTo = new Date();
dateFrom = srcReportPotitionStock.getDateFrom();
dateTo = srcReportPotitionStock.getDateTo();

/** 1. Rekap data seluruh transaksi */
boolean isSummarryTransactionData = false;
if(srcReportPotitionStock.getGenerateReport()) {
    isSummarryTransactionData = objSessReportPotitionStock.summarryTransactionData(false, srcReportPotitionStock, false);
    srcReportPotitionStock.setGeneratereport(false);
    
    /** proses dibawah ini, oleh karena ada pengurangan hari pada instance srcReportPotitionStock */
    dateFrom.setDate(dateFrom.getDate() + 1);
    srcReportPotitionStock.setDateFrom(dateFrom); 
    srcReportPotitionStock.setDateTo(dateTo);
}
else {
    isSummarryTransactionData = true;
}

if(isSummarryTransactionData) { // cek apakah proses rekap data OK
    /** 2. Mengambil nilai stok dan jumlah list dari data hasil rekap */
    Vector vctStockValue = objSessReportPotitionStock.getGrandTotalReportPosisiStockAll(srcReportPotitionStock, isCalculateZeroQty);
    vectSize = Integer.parseInt((String)vctStockValue.get(0));
    
    stockValue = (Vector)vctStockValue.get(1);
    
    if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
        start = ctrlMaterialStock.actionList(iCommand,start,vectSize,recordToGet);
    }
    
    /** 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList */
    vctReportStock = objSessReportPotitionStock.reportPosisiStockAll(srcReportPotitionStock, "", start, recordToGet, isCalculateZeroQty);
}
else {
    System.out.println("Process Failed...");
}

System.out.println("vctReportStock : "+vctReportStock);

/**
 * END GET POTITION STOCK REPORT
 */


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

    
    
%>
<!-- End of Jsp Block -->

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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
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
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="type" value="<%=type%>">
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
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][4]).toUpperCase()%></b></td>
                  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                  <td width="89%" height="14" valign="middle" class="command">
                    <%=Formater.formatDate(srcReportPotitionStock.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][3]%> <%=Formater.formatDate(srcReportPotitionStock.getDateTo(), "dd-MM-yyyy")%>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][5]).toUpperCase()%></b></td>
                  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                  <td width="89%" height="14" valign="middle" class="command"><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][7]).toUpperCase()%></b></td>
                  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                  <td width="89%" height="14" valign="middle" class="command"><%=category.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top">
                  <td width="10%" height="14" valign="middle" class="command"><b><%=(textListGlobal[SESS_LANGUAGE][6]).toUpperCase()%></b></td>
                  <td width="1%" height="14" valign="middle" class="command"><strong>:</strong></td>
                  <td width="89%" height="14" valign="middle" class="command"><%=merk.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td align="center" valign="middle" colspan="3"><%=drawList(srcReportPotitionStock.getInfoShowed(), srcReportPotitionStock.getStockValueBy(), SESS_LANGUAGE, vctReportStock, start, stockValue)%></td>
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
                        <td width="80%"> <table width="55%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td nowrap width="6%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td>
                              <td nowrap width="2%">&nbsp;</td>
                              <td class="command" nowrap width="92%"><a href="javascript:cmdBack()"><%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][9],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table></td>
                        <td width="20%">
                        <% if(vctReportStock.size() != 0) { %>
			  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][10]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][10]%></a></td>
                            </tr>
                          </table>
			<% } %></td>
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
      <%@ include file = "../../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
