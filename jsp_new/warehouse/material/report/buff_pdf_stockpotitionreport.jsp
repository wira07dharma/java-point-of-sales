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
                 com.dimata.posbo.entity.masterdata.Category,
                 com.dimata.posbo.entity.masterdata.PstCategory"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_POTITION); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
    {"LAPORAN POSISI STOK Ku", "Dari", "s/d", "Periode", "Lokasi", "Merk", "Kategori", "Semua"},
    {"STOCK POSITION REPORT", "From", "to", "Periode", "Location", "Merk", "Category", "All"}
};

public static final String textListTitleTable[][] = {
    {"NO","SKU","NAMA BARANG","HPP","UNIT","JUMLAH STOK","AWAL","OPNAME","TERIMA","TRANSFER","RETUR SUPP.","JUAL","RETUR CUST.","SALDO","NILAI STOK"},
    {"NO","SKU","GOODS NAME","COGS","UNIT","QUANTITY STOCK","BEGINNING","OPNAME","RECEIVE","DISPATCH","SUPP. RETURN","SALE","CUST. RETURN","SALDO","STOCK VALUE"}
};
%>

<html>
<head>
    <title>Dimata - ProChain POS</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head> 

<body bgcolor="#FFFFFF" text="#000000">
    Loading ...
    <script language="JavaScript">
        window.focus();
    </script>
</body>
</html>

<!-- JSP Block -->
<%
/** judul laporan dibuat dinamis */
String reportTitle1 = FRMQueryString.requestString(request, "report_title1");
String reportTitle2 = FRMQueryString.requestString(request, "report_title2");

/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector bodyPdf = new Vector(1,1);
Vector footerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);

/** start fetch data */
SrcReportPotitionStock srcReportPotitionStock = new SrcReportPotitionStock();
SessReportPotitionStock objSessReportPotitionStock = new SessReportPotitionStock();
FrmSrcReportPotitionStock frmSrcReportPotitionStock = new FrmSrcReportPotitionStock(request, srcReportPotitionStock);

try {
    srcReportPotitionStock = (SrcReportPotitionStock)session.getValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT);
    if (srcReportPotitionStock == null) srcReportPotitionStock = new SrcReportPotitionStock();
} catch(Exception e) {
    srcReportPotitionStock = new SrcReportPotitionStock();
}

session.putValue(SessReportPotitionStock.SESS_SRC_STOCK_POTITION_REPORT, srcReportPotitionStock);


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
    merk.setName(textListGlobal[SESS_LANGUAGE][7]+" "+textListGlobal[SESS_LANGUAGE][5]);
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

/** 1. Rekap data seluruh transaksi */
boolean isSummarryTransactionData = false;
if(srcReportPotitionStock.getGenerateReport()) {
    isSummarryTransactionData = objSessReportPotitionStock.summarryTransactionData(false, srcReportPotitionStock, false);
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
    Vector vctStockValue = objSessReportPotitionStock.getGrandTotalReportPosisiStockAll(srcReportPotitionStock, isCalculateZeroQty);
    stockValue = (Vector)vctStockValue.get(1);
    
    /** 3. Mengambil list data hasil rekap sesuai dengan permintaan ControlList */
    vctReportStock = objSessReportPotitionStock.reportPosisiStockAll(srcReportPotitionStock, "", 0, 0, isCalculateZeroQty);
}
else {
    System.out.println("Process Failed...");
}
/**
 * END GET POTITION STOCK REPORT
 */

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);

headerPdf.add(0, (String) companyAddress.get(0));
headerPdf.add(1, (String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, reportTitle1);
if(srcReportPotitionStock.getInfoShowed() != SrcReportPotitionStock.SHOW_BOTH) {
    headerPdf.add(4, "("+reportTitle2+")");
} else {
    headerPdf.add(4, "");
}
headerPdf.add(5, (textListGlobal[SESS_LANGUAGE][3]).toUpperCase()); //periode
headerPdf.add(6, Formater.formatDate(srcReportPotitionStock.getDateFrom(), "dd-MM-yyyy")+" "+textListGlobal[SESS_LANGUAGE][2]+" "+Formater.formatDate(srcReportPotitionStock.getDateTo(), "dd-MM-yyyy"));
headerPdf.add(7, (textListGlobal[SESS_LANGUAGE][4]).toUpperCase()); //location
headerPdf.add(8, location.getName().toUpperCase());
headerPdf.add(9, (textListGlobal[SESS_LANGUAGE][6]).toUpperCase()); //category
headerPdf.add(10, category.getName().toUpperCase());
headerPdf.add(11, (textListGlobal[SESS_LANGUAGE][5]).toUpperCase()); //merk
headerPdf.add(12, merk.getName().toUpperCase());

listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][2]);

if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_QTY_ONLY) {
    listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][4]); //unit
}
else if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_VALUE_ONLY && srcReportPotitionStock.getStockValueBy() == SrcReportPotitionStock.STOCK_VALUE_BY_COGS_MASTER) {
    listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][3]); //hpp
}
else {
    listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][3]);
}

/** title */
if(srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_QTY_ONLY ||
        srcReportPotitionStock.getInfoShowed() == SrcReportPotitionStock.SHOW_BOTH) {
    listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][5]);
}
else {
    listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][14]);
}

listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][6]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][7]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][8]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][9]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][10]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][11]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][12]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][13]);

listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListTitleTable[SESS_LANGUAGE][14]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(vctReportStock);

//footerPdf.add(stockValue);

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(stockValue);
pdfContent.add(srcReportPotitionStock);

session.putValue("STOCK_POTITION_REPORT", pdfContent);
%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.stock.StockPotitionReportPdf?approot=<%=approot%>";
</script>