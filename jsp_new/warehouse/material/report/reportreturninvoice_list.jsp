<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package common -->
<%@ page import = "com.dimata.common.entity.location.*" %>
<%@ page import = "com.dimata.common.entity.contact.*" %>
<!--package material -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<!--package material -->
<%@ page import = "com.dimata.posbo.entity.warehouse.*" %>
<%@ page import = "com.dimata.posbo.entity.masterdata.*" %>
<%@ page import = "com.dimata.posbo.entity.admin.*" %>
<%@ page import = "com.dimata.posbo.entity.search.*" %>
<%@ page import = "com.dimata.posbo.form.warehouse.*" %>
<%@ page import = "com.dimata.posbo.form.search.*" %>
<%@ page import = "com.dimata.posbo.session.warehouse.*" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RETURN, AppObjInfo.G2_SUPPLIER_RETURN_REPORT , AppObjInfo.OBJ_SUPPLIER_RETURN_REPORT_BY_INVOICE); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"Tidak ada data", "LAPORAN RETUR BARANG KE SUPLIER", "Periode", "Dari", "s/d", "Lokasi", "Suplier", 
	 "Merk", "Kategori", "Semua", "Laporan Retur", "Cetak Laporan Retur","Tidak ada data"},
	{"No data available", "GOODS RETURN TO SUPPLIER REPORT", "Period", "From", "to", "Location", "Supplier",
	 "Merk", "Category", "All", "Return Report", "Print Return Report","No data available"}
};

public static final String textListMaterialHeaderMain[][] = {
	{"NO","NOTA","TANGGAL","SUPLIER","ITEM","TOTAL RETUR"},
	{"NO","INVOICE","DATE","SUPPLIER","ITEM","TOTAL RETURN"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
	{"NO","SKU","NAMA BARANG","QTY","SATUAN","HRG BELI","TOTAL BELI"},
	{"NO","SKU","NAME","QTY","UNIT","COST","TOTAL COST"}
};

public Vector drawLineHorizontal() {
	Vector rowx = new Vector();
	//Add Under line
	rowx.add("-"); //0
	rowx.add("--------");//1
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("----------------------------");//3
	rowx.add("<div align=\"center\">"+"-"+"</div>");//4
	rowx.add("--------------------------------------------------------------------");//5
	rowx.add("<div align=\"center\">"+"-"+"</div>");//6
	rowx.add("------------");//7
	rowx.add("<div align=\"center\">"+"-"+"</div>");//8
	rowx.add("------------------------------");//9
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("--------------------------------------------");//12
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	return rowx;
}

public Vector drawHeader(int language)
{
	Vector rowx = new Vector();
	//Add Header
	rowx.add("|");// 0
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");// 1
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 2
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");// 3
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 4
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");// 5
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 6
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");// 7
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 8
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");// 9
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 10
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");// 11
	rowx.add("<div align=\"center\">"+"|"+"</div>");// 12
	return rowx;
}

public Vector drawLineSubTotal()
{
	Vector rowx = new Vector();
	rowx.add("|"); //0
	rowx.add("");//1
	rowx.add("");//10
	rowx.add("");//3
	rowx.add("");//4
	rowx.add("");//5
	rowx.add("<div align=\"center\">"+"-"+"</div>");//6
	rowx.add("------------");//7
	rowx.add("<div align=\"center\">"+"-"+"</div>");//8
	rowx.add("------------------------------");//9
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("-------------------------------------------");//12
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	return rowx;
}

public Vector drawLineTotal()
{
	Vector rowx = new Vector();
	rowx.add(""); //0
	rowx.add("");//1
	rowx.add("");//10
	rowx.add("");//3
	rowx.add("");//4
	rowx.add("");//5
	rowx.add("<div align=\"center\">"+"-"+"</div>");//6
	rowx.add("------------");//7
	rowx.add("<div align=\"center\">"+"-"+"</div>");//8
	rowx.add("------------------------------");//9
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	rowx.add("-------------------------------------------");//12
	rowx.add("<div align=\"center\">"+"-"+"</div>");//10
	return rowx;
}
	
public Vector drawLineSingleSpot()
{
	Vector rowx = new Vector();
	rowx.add("-");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	return rowx;
}
	
public Vector drawLineTotalSide()
{
	Vector rowx = new Vector();
	rowx.add("-");
	rowx.add("--------");
	rowx.add("-");
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-----------------------------");
	rowx.add("-");	
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-------------------");
	rowx.add("-");
	rowx.add("-----------------------------");
	rowx.add("-");
	return rowx;
}
	
public Vector drawList(int language,Vector objectClass, boolean isCategory,
						boolean isSubCategory,boolean isReason,boolean isSupplier)
{
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","3%","0","0","center","top"); // nomer
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","8%","0","0","center","top"); // sku
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","20%","0","0","center","top"); // nama barang
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","4%","0","0","right","bottom"); // qty
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","9%","0","0","right","bottom"); // harga beli barang default.
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
		ctrlist.dataFormat("","13%","0","0","right","bottom"); // total harga beli barang 
		ctrlist.dataFormat("","1%","0","0","left","bottom"); // |
	
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		double totalRec = 0.00;
		double totalPrice = 0.00;
		double subTotalRec = 0.00;
		double subTotalPrice = 0.00;
		double subTotalQty = 0;
		double totalQty = 0;
		long noBill = 0;
		boolean firstRow = true;
		int baris = 0;
		int countTrue = 0;
		
		int maxlines = 72;
		int maxlinespgdst = 75;
		boolean boolmaxlines = true;
		
		for(int i=0; i<objectClass.size(); i++)	{
			Vector rowx = new Vector();				
			Vector vt = (Vector)objectClass.get(i);
			MatReturn ret = (MatReturn)vt.get(0);
			MatReturnItem rti = (MatReturnItem)vt.get(1);
			Material mat = (Material)vt.get(2);
			Unit unt = (Unit)vt.get(3);
			ContactList cnt = (ContactList)vt.get(5);
			String nama_barang = mat.getName();
			if (nama_barang.length() >= 22)	{
				nama_barang = nama_barang.substring(0,22);
			}
			String unit_name = unt.getCode();
			if (unit_name.length() >= 5) {
				unit_name = unit_name.substring(0,5);
			}

			//Tambahkan header tanggal dan invoice
			if (noBill != ret.getOID())	{
				noBill = ret.getOID();
				if (firstRow == true) {
					firstRow = false;
					//Add header only
					lstData.add(drawLineHorizontal());
					baris += 1;
					lstData.add(drawHeader(language));
					baris += 1;
					lstData.add(drawLineHorizontal());
					baris += 1;
					//Add date
					rowx.add("*2");
					rowx.add("|");
					rowx.add("Tanggal");
					rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(ret.getReturnDate(), "dd-MM-yyyy")+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
					//Add invoice number
					rowx = new Vector();
					rowx.add("*2");
					rowx.add("|");
					rowx.add("Invoice");
					rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+ret.getRetCode()+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
					if (isSupplier == false) {
						//Add invoice number
						rowx = new Vector();
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Supplier");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
						rowx.add("<div align=\"center\">"+"|"+"</div>");
						lstData.add(rowx);
						baris += 1;
					}
					lstData.add(drawLineHorizontal());
					baris += 1;
					rowx = new Vector();
				} //end firstRow
				else {
					lstData.add(drawLineHorizontal());
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineSingleSpot());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					//Add sub total then header
					//Add sub total
					rowx.add("|"); // |
					rowx.add(" "); // nomer
					rowx.add(" "); // |
					rowx.add(" "); // kode
					rowx.add(" "); // |
					rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\"></div>");			
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");			
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineTotal());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					//lstData.add(drawLineTotal());
					lstData.add(drawLineSubTotal());
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineSingleSpot());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					//Add date
					rowx = new Vector();
					rowx.add("*2");
					rowx.add("|");
					rowx.add("Tanggal");
					rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(ret.getReturnDate(), "dd-MM-yyyy")+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineHorizontal());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					//Add invoice number
					rowx = new Vector();
					rowx.add("*2");
					rowx.add("|");
					rowx.add("Invoice");
					rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+ret.getRetCode()+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineHorizontal());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					if (isSupplier == false) {
						//Add invoice number
						rowx = new Vector();
						rowx.add("*2");
						rowx.add("|");
						rowx.add("Supplier");
						rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
						rowx.add("<div align=\"center\">"+"|"+"</div>");
						lstData.add(rowx);
						baris += 1;
						if (baris == maxlines) {
							if(boolmaxlines) {
								maxlines = maxlinespgdst;
							}
							//Add line
							lstData.add(drawLineHorizontal());
							//Add header for next page and restart counting baris
							lstData.add(drawLineHorizontal());
							baris = 1;
							lstData.add(drawHeader(language));
							baris += 1;
							lstData.add(drawLineHorizontal());
							baris += 1;
						}
					}
					lstData.add(drawLineHorizontal());
					baris += 1;
					if (baris == maxlines) {
						if(boolmaxlines) {
							maxlines = maxlinespgdst;
						}
						//Add line
						lstData.add(drawLineSingleSpot());
						//Add header for next page and restart counting baris
						lstData.add(drawLineHorizontal());
						baris = 1;
						lstData.add(drawHeader(language));
						baris += 1;
						lstData.add(drawLineHorizontal());
						baris += 1;
					}
					rowx = new Vector();
					subTotalPrice = 0.00;
					subTotalRec = 0.00;
					subTotalQty = 0;
				}
			}
			rowx.add("|");
			rowx.add(""+(i+1));
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(mat.getSku());
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(nama_barang);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rti.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rti.getCost() * ret.getTransRate())+"</div>");			
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rti.getTotal() * ret.getTransRate())+"</div>");			
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			lstData.add(rowx);
			baris += 1;
			if (baris == maxlines) {
				if(boolmaxlines) {
					maxlines = maxlinespgdst;
				}
				//Add line
				lstData.add(drawLineHorizontal());
				//Add header for next page and restart counting baris
				lstData.add(drawLineHorizontal());
				baris = 1;
				lstData.add(drawHeader(language));
				baris += 1;
				lstData.add(drawLineHorizontal());
				baris += 1;
			}
			totalRec += (rti.getCost() * ret.getTransRate());
			totalPrice += (rti.getCost() * rti.getQty() * ret.getTransRate());
			totalQty += rti.getQty();
			subTotalRec += (rti.getCost() * ret.getTransRate());
			subTotalPrice += (rti.getCost() * rti.getQty() * ret.getTransRate());
			subTotalQty += rti.getQty();

			lstLinkData.add("");
		}
		lstData.add(drawLineHorizontal());
		Vector rowx = new Vector();
		//Add sub total
		rowx.add(" "); // |
		rowx.add(" "); // nomer
		rowx.add(" "); // |
		rowx.add(" "); // kode
		rowx.add(" "); // |
		rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\"></div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		lstData.add(rowx);
		lstData.add(drawLineTotal());
		//Add TOTAL
		rowx = new Vector();
		rowx.add(" "); // |
		rowx.add(" "); // nomer
		rowx.add(" "); // |
		rowx.add(" "); // kode
		rowx.add(" "); // |
		rowx.add("<div align=\"right\">"+"TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\"></div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalPrice)+"</div>");			
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		lstData.add(rowx);
		lstData.add(drawLineTotal());
		result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
	}
	else {
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][12]+"</div>");
	}
	return result;
}

//-------------------------------------------------//

public Vector drawList2(int language, Vector objectClass, int start, int recordToGet) {
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
		
		ctrlist.dataFormat(textListMaterialHeaderMain[language][0],"3%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeaderMain[language][1],"13%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeaderMain[language][2],"7%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeaderMain[language][3],"12%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeaderMain[language][4],"55%","0","0","center","bottom");
        ctrlist.dataFormat(textListMaterialHeaderMain[language][5],"10%","0","0","center","bottom");
		
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		
		int nomor = 1;
		long currentMatReturnOid = 0;
		double totalRetur = 0;
		double grandTotalRetur = 0;
		
		Vector listMatReturnItem = new Vector(1,1);
		double totalQty = 0;
		for(int i=0; i<objectClass.size(); i++)	{
			Vector rowx = new Vector();				
			Vector objVector = (Vector)objectClass.get(i);
			MatReturn objMatReturn = (MatReturn)objVector.get(0);
			MatReturnItem objMatReturnItem = (MatReturnItem)objVector.get(1);
			Material objMaterial = (Material)objVector.get(2);
			Unit objUnit = (Unit)objVector.get(3);
			ContactList objContactList = (ContactList)objVector.get(5);
			
			currentMatReturnOid = objMatReturn.getOID();
			
			totalRetur += (objMatReturnItem.getQty() * objMatReturnItem.getCost() * objMatReturn.getTransRate());
			totalQty += objMatReturnItem.getQty();
			Vector tempMatReturItem = new Vector(1,1);
			tempMatReturItem.add(objMaterial.getSku());
			tempMatReturItem.add(objMaterial.getName());
			tempMatReturItem.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReturnItem.getQty())+"</div>");
			tempMatReturItem.add("<div align=\"center\">"+objUnit.getCode()+"</div>");
			tempMatReturItem.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReturnItem.getCost() * objMatReturn.getTransRate())+"</div>");
			tempMatReturItem.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(objMatReturnItem.getQty() * objMatReturnItem.getCost() * objMatReturn.getTransRate())+"</div>");
			listMatReturnItem.add(tempMatReturItem);
			
			if((i+1) < objectClass.size()) { //cek, apakah masih ada list selanjutnya?
				Vector vct = (Vector)objectClass.get(i+1);
				MatReturn matReturn = (MatReturn)vct.get(0);
				//cek apakah oid return sama dengan oid return selanjutnya? jika tidak simpan untuk selanjutnya ditampilkan
				if(matReturn.getOID() != currentMatReturnOid) { 
					rowx.add(""+nomor);
					rowx.add(objMatReturn.getRetCode());
					rowx.add(Formater.formatDate(objMatReturn.getReturnDate(), "dd-MM-yyyy"));
					rowx.add(objContactList.getCompName());
					rowx.add(drawListDetail(language, listMatReturnItem));
					rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalRetur)+"</div>");
					lstData.add(rowx);
					listMatReturnItem = new Vector(1,1);
					
					grandTotalRetur += totalRetur;
					nomor++;
					totalRetur = 0;
				}
			}
			else { //kondisi untuk record paling terakhir
				rowx.add(""+nomor);
				rowx.add(objMatReturn.getRetCode());
				rowx.add(Formater.formatDate(objMatReturn.getReturnDate(), "dd-MM-yyyy"));
				rowx.add(objContactList.getCompName());
				rowx.add(drawListDetail(language, listMatReturnItem));
				rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalRetur)+"</div>");
				lstData.add(rowx);
				listMatReturnItem = new Vector(1,1);
				
				grandTotalRetur += totalRetur;
				nomor++;
				totalRetur = 0;
			}
        }
        result.add(ctrlist.draw());
		result.add(FRMHandler.userFormatStringDecimal(grandTotalRetur));
		result.add(FRMHandler.userFormatStringDecimal(totalQty));
    }
	else {
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][12]+"</div>");
		result.add("");
		result.add("");
	}
    return result;
}


public String drawListDetail(int language, Vector objectClass) {
	String result = "";
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("1");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.dataFormat(textListMaterialHeader[language][1],"16%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][2],"34%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][3],"10%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][4],"10%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][5],"15%","0","0","center","bottom");
		ctrlist.dataFormat(textListMaterialHeader[language][6],"15%","0","0","center","bottom");

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector();

		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++) {
			Vector vctList = (Vector)objectClass.get(i);
			rowx = new Vector();
			rowx.add((String)vctList.get(0));
			rowx.add((String)vctList.get(1));
			rowx.add((String)vctList.get(2));
			rowx.add((String)vctList.get(3));
			rowx.add((String)vctList.get(4));
			rowx.add((String)vctList.get(5));

			lstData.add(rowx);
		}
		result = ctrlist.draw();
	}
	return result;
}

%>

<%
int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
boolean isCategory = false;
boolean isSubCategory = false;
boolean isSupplier = false;
boolean isReason = false;

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcReportReturn srcReportReturn = new SrcReportReturn();
SessReportReturn sessReportReturn = new SessReportReturn();
FrmSrcReportReturn frmSrcReportReturn = new FrmSrcReportReturn(request, srcReportReturn);

/**
* handle current search data session 
*/
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 { 
		srcReportReturn = (SrcReportReturn)session.getValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE); 
		if (srcReportReturn == null) srcReportReturn = new SrcReportReturn();
	 }
	 catch(Exception e)
	 { 
		srcReportReturn = new SrcReportReturn();
	 }
}
else
{
	 frmSrcReportReturn.requestEntityObject(srcReportReturn);
	 // Karena tidak memakai parameter REASON, maka property ini di-set -1
	 srcReportReturn.setReturnReason(-1);
	 session.putValue(SessReportReturn.SESS_SRC_REPORT_RETURN_INVOICE, srcReportReturn);
}

/**
* get vectSize, start and data to be display in this page
*/
Vector records = sessReportReturn.getReportReturn(srcReportReturn);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	//start = ctrlReportReturn.actionList(iCommand,start,vectSize,recordToGet);
}	

/**
	set value vector for stock report print
	biar tidak load data lagi
*/


Location location = new Location();
if (srcReportReturn.getLocationId() != 0) {
	try	{
		location = PstLocation.fetchExc(srcReportReturn.getLocationId());
	}
	catch(Exception exx) {
	}
}
else {
	location.setName(textListGlobal[SESS_LANGUAGE][9]+" "+textListGlobal[SESS_LANGUAGE][5]);
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdBack()
{
	document.frm_reportreturn.command.value="<%=Command.BACK%>";
	document.frm_reportreturn.action="src_reportreturninvoice.jsp";
	document.frm_reportreturn.submit();
}

function printForm(){
	//window.open("reportreturninvoice_form_print.jsp","returnreportinvoice","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	window.open("buff_pdf_report_return.jsp","report_return_detail","");
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
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" -->&nbsp;<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportreturn" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" align="center" valign="middle" colspan="2"><h4><strong><%=textListGlobal[SESS_LANGUAGE][1]%></strong></h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%" valign="middle"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong></td>
                  <td width="90%" valign="middle"><strong>:</strong> <%=Formater.formatDate(srcReportReturn.getDateFrom(), "dd-MM-yyyy")%> <%=textListGlobal[SESS_LANGUAGE][4]%> <%=Formater.formatDate(srcReportReturn.getDateTo(), "dd-MM-yyyy")%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td valign="middle"><strong><%=textListGlobal[SESS_LANGUAGE][5]%></strong></td>
                  <td><strong>: </strong><%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3"> <%
					/*Vector hasil = drawList(SESS_LANGUAGE,records,isCategory,isSubCategory,isReason,isSupplier);
					Vector report = new Vector(1,1);
					report.add(srcReportReturn);
					report.add(hasil);
					try {
						session.putValue("SESS_MAT_RETURN_REPORT_INVOICE",report);
					}
					catch(Exception e){
					}
					
					for(int k=0;k<hasil.size();k++) {
						out.println(hasil.get(k));
					}
					*/
					%> 
					<%
						Vector temp = drawList2(SESS_LANGUAGE,records,0,0);
						String strList = (String)temp.get(0);
						String grandTotal = (String)temp.get(1);
						String totalQty = (String)temp.get(2);
						out.println(strList);
					%>
					</td>
                </tr>
				<tr align="left" valign="top"> 
                  <td align="right" valign="middle" colspan="3">&nbsp;</td>
                </tr>
				<% if(records.size() > 0) { %>
                <tr align="left" valign="top"> 
                  <td align="right" valign="middle" colspan="3">
				  	<h4>GRAND <%=textListMaterialHeaderMain[SESS_LANGUAGE][5]%> : &nbsp;&nbsp;&nbsp; <%=grandTotal%></b></h3>
				  </td>
                </tr>
				<tr align="left" valign="top"> 
                  <td align="right" valign="middle" colspan="3">
				  	<h4>TOTAL QTY : <%=totalQty%></b></h3>
				  </td>
                </tr>
				<% } %>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <!--td nowrap width="7%"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnBackOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][10],ctrLine.CMD_BACK_SEARCH,true)%>"></a></td-->
                              <td nowrap width="3%">&nbsp;</td>
                              <td class="command" nowrap width="90%"><a class="btn btn-primary" href="javascript:cmdBack()"><i class="fa fa-arrow-left"></i> <%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][10],ctrLine.CMD_BACK_SEARCH,true)%></a></td>
                            </tr>
                          </table>
						</td>
                        <td width="20%"> 
						  <%if(records!=null && records.size()>0){%>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="5%" valign="top"><a href="javascript:printForm()"><img src="<%=approot%>/images/BtnPrint.gif" width="31" height="27" border="0" alt="<%=textListGlobal[SESS_LANGUAGE][11]%>"></a></td>
                              <td width="95%" nowrap>&nbsp; <a href="javascript:printForm()" class="command" ><%=textListGlobal[SESS_LANGUAGE][11]%></a></td>
                            </tr>
                          </table>
						  <%}else{%>
						  &nbsp;
						  <%}%>
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
