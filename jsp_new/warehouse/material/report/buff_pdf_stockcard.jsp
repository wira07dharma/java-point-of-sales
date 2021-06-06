<%@ page language = "java" %>
<%@ page import="com.dimata.posbo.session.warehouse.SessStockCard,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.warehouse.StockCardReport,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.pos.entity.billing.PstBillMain,
                 com.dimata.posbo.form.search.FrmSrcStockCard,
                 com.dimata.posbo.entity.search.SrcStockCard,
                 com.dimata.posbo.entity.masterdata.Material,
                 com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page import="com.dimata.posbo.entity.warehouse.PstMaterialStockCode"%>
<%@ page import="com.dimata.posbo.entity.warehouse.MaterialStockCode"%>
<%@ page import="com.dimata.qdep.form.FRMHandler"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_CARD); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"Tidak ada data","Kartu Stok","Periode","Lokasi","Kode/Nama Barang","Periode"," s/d ","Cetak Kartu Stok","Stok awal"},
	{"No available data", "Stock Card","Period","Location","Code/Goods Name","Period"," to ","Print Stock Card","Beginning stock"}
};

public static final String textListMaterialHeader[][] = {
	{"Tanggal","Nomor Dokumen","Keterangan","Mutasi","Stok Masuk","Stok Keluar","Saldo"},
	{"Date","Number","Remark","Mutation","Stock In","Stock Out","Saldo"}
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
/**
 * Vector untuk keperluan PDF
 */
Vector headerPdf = new Vector(1,1);
Vector bodyPdf = new Vector(1,1);
Vector footerPdf = new Vector(1,1);
Vector listTableHeaderPdf = new Vector(1,1);
Vector pdfContent = new Vector(1,1);

/** start fetch data */
SrcStockCard srcStockCard = new SrcStockCard();
SessStockCard sessStockCard = new SessStockCard();
FrmSrcStockCard frmSrcStockCard = new FrmSrcStockCard(request, srcStockCard);

try { 
	srcStockCard = (SrcStockCard)session.getValue(SessStockCard.SESS_STOCK_CARD); 
	if (srcStockCard == null) srcStockCard = new SrcStockCard();
}
catch(Exception e) { 
	srcStockCard = new SrcStockCard();
}

session.putValue(SessStockCard.SESS_STOCK_CARD, srcStockCard);


Location objLocation = new Location();
if (srcStockCard.getLocationId() != 0) {
	try	{
		objLocation = PstLocation.fetchExc(srcStockCard.getLocationId());
	}
	catch(Exception e) {
	}
}

Material objMaterial = new Material();
if (srcStockCard.getMaterialId() != 0) {
	try{
		objMaterial = PstMaterial.fetchExc(srcStockCard.getMaterialId());
	}
	catch(Exception e){
	}
}

/** get data */
Vector records = SessStockCard.createHistoryStockCard(srcStockCard);

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);

String periode = Formater.formatDate(srcStockCard.getStardDate(), "dd-MM-yyyy");
periode += textListGlobal[SESS_LANGUAGE][6]+""+Formater.formatDate(srcStockCard.getEndDate(), "dd-MM-yyyy");

headerPdf.add(0, (String) companyAddress.get(0));
headerPdf.add(1, (String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, (textListGlobal[SESS_LANGUAGE][1]).toUpperCase()); //judul
headerPdf.add(4, (textListGlobal[SESS_LANGUAGE][3]).toUpperCase()); //lokasi
headerPdf.add(5, objLocation.getName().toUpperCase());
headerPdf.add(6, (textListGlobal[SESS_LANGUAGE][4]).toUpperCase()); //kode/nama barang
headerPdf.add(7, objMaterial.getSku()+" / "+objMaterial.getName());
headerPdf.add(8, (textListGlobal[SESS_LANGUAGE][5]).toUpperCase()); //periode
headerPdf.add(9, periode);

listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][5]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][6]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(records);
bodyPdf.add(srcStockCard);
bodyPdf.add(textListGlobal[SESS_LANGUAGE][8]); // keterangan untuk stok awal

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);

session.putValue("SESS_STOCK_CARD_REPORT", pdfContent);
%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.stock.StockCardPdf?approot=<%=approot%>";
</script>