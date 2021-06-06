<%@ page import="com.dimata.qdep.form.FRMHandler,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.posbo.entity.search.SrcReportStock,
                 com.dimata.posbo.session.warehouse.SessReportConStock,
                 com.dimata.posbo.form.search.FrmSrcReportStock,
                 com.dimata.util.Command,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.common.entity.contact.PstContactList"%>
<%@ page import="com.dimata.posbo.session.masterdata.SessMaterial"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = 1; //AppObjInfo.composeObjCode(AppObjInfo.G1_STOCK, AppObjInfo.G2_STOCK_REPORT, AppObjInfo.OBJ_STOCK_REPORT_BY_SUPPLIER); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final String textListGlobal[][] = {
	{"LAPORAN STOK BARANG", "Periode", "Lokasi", "Kategori", "Consignor", "Semua","Merk"},
	{"GOODS STOCK REPORT", "Period", "Location", "Category", "Consignor", "All","Merk"}
};

public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY", "SATUAN", "HRG BELI","TOTAL BELI","HPP","NILAI STOK"},
	{"NO","CODE","NAME","QTY", "UNIT", "COST","TOTAL COST","COGS","STOCK VALUE"}
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
SrcReportStock srcReportStock = new SrcReportStock();
SessReportConStock sessReportStock = new SessReportConStock();
FrmSrcReportStock frmSrcReportStock = new FrmSrcReportStock(request, srcReportStock);

try { 
	srcReportStock = (SrcReportStock)session.getValue(SessReportConStock.SESS_SRC_REPORT_STOCK); 
	if (srcReportStock == null) srcReportStock = new SrcReportStock();
}
catch(Exception e) { 
	srcReportStock = new SrcReportStock();
}

session.putValue(SessReportConStock.SESS_SRC_REPORT_STOCK, srcReportStock);


Location location = new Location();
if (srcReportStock.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportStock.getLocationId());
	}
	catch(Exception e) {
		System.out.println(e.toString());
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][2]);
}

Periode periode = new Periode();
try {
	periode = PstPeriode.fetchExc(srcReportStock.getPeriodeId());
}
catch(Exception e){
	System.out.println(e.toString());
}

ContactList contactList = new ContactList();
try{
	contactList = PstContactList.fetchExc(srcReportStock.getSupplierId());
}catch(Exception e){
	System.out.println(e.toString());
}

Category category = new Category();
try{
	category = PstCategory.fetchExc(srcReportStock.getCategoryId());
        if(category.getOID()==0)
            category.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][3]);
}catch(Exception e){
    category.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][3]);
    System.out.println("Exc when get kategory");
}

Merk merk = new Merk();
try{
	merk = PstMerk.fetchExc(srcReportStock.getMerkId());
        if(merk.getOID()==0)
            merk.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][6]);

}catch(Exception e){
    merk.setName(textListGlobal[SESS_LANGUAGE][5]+" "+textListGlobal[SESS_LANGUAGE][6]);
    System.out.println("Exc when get merk");
}

boolean calculateQtyNull = false;
//Vector listStockAll = sessReportStock.getReportStockAll(srcReportStock, calculateQtyNull); // getReportStock
//Vector listStockPerKategori = sessReportStock.getReportStockPerKategori(srcReportStock, calculateQtyNull); // getReportStock
Vector listStockPerSupplier = sessReportStock.getReportStock(srcReportStock); //, calculateQtyNull); // getReportStock

Vector listReportStock = new Vector(1,1);
double totalNilaiStock = 0;

for(int i=0; i<listStockPerSupplier.size(); i++) {
	Vector vt = (Vector)listStockPerSupplier.get(i);			
	MaterialConStock materialStock = (MaterialConStock)vt.get(0);
	Material material = (Material)vt.get(1);
	Unit unit = (Unit)vt.get(4);   
	
	Vector temp = new Vector(1,1);
	temp.add(String.valueOf(i+1)+".");
	temp.add(material.getSku());
	temp.add(material.getName());
	temp.add(FRMHandler.userFormatStringDecimal(materialStock.getQty()));
	temp.add(unit.getCode());
	temp.add(FRMHandler.userFormatStringDecimal(material.getAveragePrice()));
	temp.add(String.valueOf(material.getAveragePrice() * materialStock.getQty()));
	
	listReportStock.add(temp);
	
	totalNilaiStock += (material.getAveragePrice() * materialStock.getQty());
}

/** store data to vector pdf */
Vector compTelpFax = (Vector)companyAddress.get(2);

headerPdf.add(0, (String) companyAddress.get(0));
headerPdf.add(1, (String) companyAddress.get(1));
headerPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
headerPdf.add(3, textListGlobal[SESS_LANGUAGE][0]);
headerPdf.add(4, (textListGlobal[SESS_LANGUAGE][1]).toUpperCase()); //periode
headerPdf.add(5, " : "+periode.getPeriodeName().toUpperCase());
headerPdf.add(6, (textListGlobal[SESS_LANGUAGE][2]).toUpperCase()); //location
headerPdf.add(7, " : "+location.getName().toUpperCase());
headerPdf.add(8, (textListGlobal[SESS_LANGUAGE][4]).toUpperCase()); //supplier
headerPdf.add(9, " : "+contactList.getCompName().toUpperCase());
headerPdf.add(10, (textListGlobal[SESS_LANGUAGE][3]).toUpperCase()); //supplier
headerPdf.add(11, " : "+category.getName().toUpperCase());
headerPdf.add(12, (textListGlobal[SESS_LANGUAGE][6]).toUpperCase()); //supplier
headerPdf.add(13, " : "+merk.getName().toUpperCase());


listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][0]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][1]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][2]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][3]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][4]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][7]);
listTableHeaderPdf.add(textListMaterialHeader[SESS_LANGUAGE][8]);

bodyPdf.add(listTableHeaderPdf);
bodyPdf.add(listReportStock);

footerPdf.add(FRMHandler.userFormatStringDecimal(totalNilaiStock));

pdfContent.add(headerPdf);
pdfContent.add(bodyPdf);
pdfContent.add(footerPdf);

session.putValue("REPORT_STOCK", pdfContent);
%>
<script language="JavaScript">
    document.location="<%=approot%>/servlet/com.dimata.posbo.report.stock.ReportStockPdf?approot=<%=approot%>";
</script>