<%@ page import="com.dimata.posbo.entity.search.SrcReportDispatch,
                 com.dimata.posbo.session.warehouse.SessReportCosting,
                 com.dimata.posbo.form.search.FrmSrcReportDispatch,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.posbo.entity.warehouse.MatDispatchItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command,
                 com.dimata.posbo.entity.masterdata.Costing,
                 com.dimata.posbo.entity.masterdata.PstCosting,
                 com.dimata.posbo.entity.warehouse.*"%>
<%@ page language = "java" %>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_COSTING, AppObjInfo.G2_COSTING_REPORT, AppObjInfo.OBJ_COSTING_REPORT); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
	{"LAPORAN PEMBIAYAAN PER NOTA","Pembiayaan","Laporan Per Nota","Pencarian","Cetak Laporan Pembiayaan","Tidak ada data pembiayaan",
	 "Laporan Pembiayaan"},
	{"COSTING REPORT BY INVOICE","Costing","Report By Invoice","Search","Print Costing Report","No costing data available",
	 "Costing Report"}
};

/* this constant used to list text of listHeader */
public static final String textListHeader[][] = {
	{"Periode"," s/d ","Lokasi Asal","Lokasi Tujuan","Kategori","Urut Berdasar","Sub Kategori","Suplier","Semua","Tipe Biaya","Semua Lokasi"},
	{"Period"," to ","From Location","To Location","Category","Sort By","Sub Category","Supplier","All", "Type of Costing","All Location"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
	{"NO","SKU","NAMA BARANG","QTY","UNIT","HPP","TOTAL HPP"},
	{"NO","SKU","NAME","QTY","UNIT","HRG SELLING","TOTAL SELLING"}
};

public static final String textListMaterialDetail[][] =
{
	{"Tanggal","No. Nota"},
	{"Date","Invoice No"}
};

/*public Vector drawList2(int language, Vector objectClass) {
	Vector result = new Vector(1,1);
	if(objectClass!=null && objectClass.size()>0) {
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		
		ctrlist.addHeader(textListMaterialHeader[language][0],"5%");
		ctrlist.addHeader(textListMaterialHeader[language][1],"15%");
		ctrlist.addHeader(textListMaterialHeader[language][2],"30%");
		ctrlist.addHeader(textListMaterialHeader[language][3],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][4],"10%");
		ctrlist.addHeader(textListMaterialHeader[language][5],"12%");
                ctrlist.addHeader(textListMaterialHeader[language][6],"18%");
                
		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
                double grandTotalHpp = 0;
		
		for(int i=0; i<objectClass.size(); i++) {
			Vector vt = (Vector)objectClass.get(i);
			MatDispatchItem dfi = (MatDispatchItem)vt.get(0);
			Material material = (Material)vt.get(1);
			Unit unit = (Unit)vt.get(2);
			Category category = (Category)vt.get(3);
			
			Vector rowx = new Vector();
			Vector temp = new Vector(1,1);
			rowx.add(String.valueOf(i+1));
			rowx.add(material.getSku());
			rowx.add(material.getName());
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfi.getQty())+"</div>");
                        rowx.add("<div align=\"center\">"+unit.getCode()+"</div>");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(material.getAveragePrice())+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(dfi.getQty() * material.getAveragePrice())+"</div>");
                        
			lstData.add(rowx);
                        
                        grandTotalHpp += (dfi.getQty() * material.getAveragePrice());
		}
		result.add(ctrlist.draw());
                result.add(FRMHandler.userFormatStringDecimal(grandTotalHpp));
	}
	else{
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>");
                result.add("");
	}
	return result;
}*/

public Vector drawLineHorizontal()
{
	Vector rowx = new Vector();
	//Add Under line
	rowx.add("-");
	rowx.add("--------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------------------------------------------------------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	return rowx;
}

public Vector drawHeader(int language)
{
	Vector rowx = new Vector();
	//Add Header
	rowx.add("|");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	rowx.add("<div align=\"center\">"+textListMaterialHeader[language][6]+"</div>");
	rowx.add("<div align=\"center\">"+"|"+"</div>");
	return rowx;
}

public Vector drawLineSubTotal()
{
	Vector rowx = new Vector();
	rowx.add("|");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("---------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-----------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	return rowx;
}

public Vector drawLineTotal()
{
	Vector rowx = new Vector();
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("----------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
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
	rowx.add("");
	rowx.add("");
	return rowx;
}

public Vector drawLineTotalSide()
{
	Vector rowx = new Vector();
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("--------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	rowx.add("-------------------------------");
	rowx.add("<div align=\"center\">"+"-"+"</div>");
	return rowx;
}

public Vector drawList(int language,Vector objectClass)
{
	Vector result = new Vector();
	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");

		ctrlist.dataFormat("","1%","0","0","left","top");
		ctrlist.dataFormat("","3%","0","0","center","top");
		ctrlist.dataFormat("","1%","0","0","left","top");
		ctrlist.dataFormat("","10%","0","0","center","top");
		ctrlist.dataFormat("","1%","0","0","left","top");
		ctrlist.dataFormat("","30%","0","0","center","top");
		ctrlist.dataFormat("","1%","0","0","right","bottom");
		ctrlist.dataFormat("","4%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","right","bottom");
		ctrlist.dataFormat("","4%","0","0","center","bottom");
		ctrlist.dataFormat("","1%","0","0","right","bottom");
		ctrlist.dataFormat("","9%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","right","bottom");
		ctrlist.dataFormat("","10%","0","0","right","bottom");
		ctrlist.dataFormat("","1%","0","0","right","bottom");

		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		boolean firstRow = true;
		double totalPrice = 0.00;
		double totalCost = 0.00;
		double subTotalPrice = 0.00;
		double subTotalCost = 0.00;
		double totalQty = 0;
		double subTotalQty = 0;
		int internalCounter = 0;
		int baris = 0;
		int countTrue = 0;
		//Tentukan nilai baris untuk halaman pertama

		//int maxlines = 72;
                int maxlines = 100;
		int maxlinespgdst = 74;
		boolean boolmaxlines = true;
                String cat_name = "";
                long noBill = 0;
		for(int i=0; i<objectClass.size(); i++)
		{
			Vector rowx = new Vector();
			Vector vt = (Vector)objectClass.get(i);
                        MatCostingItem costItem = (MatCostingItem)vt.get(0);
                        MatCosting matCost = (MatCosting)vt.get(1);
			//MatDispatchItem dfi = (MatDispatchItem)vt.get(0);
			Material mat = (Material)vt.get(2);
			Unit unt = (Unit)vt.get(3);
			Category cat = (Category)vt.get(4);
			String material_name = mat.getName();
			String unit_name = unt.getCode();
			if (material_name.length() > 35) material_name = material_name.substring(0,22);
			if (unit_name.length() > 10) unit_name = unit_name.substring(0,4);

           // if (firstRow == true){

               // firstRow = false;
                //Add Under line
               // lstData.add(drawLineHorizontal());
               // baris += 1;
                //Add Header
               // lstData.add(drawHeader(language));
               // baris += 1;
                //Add Under line
               // lstData.add(drawLineHorizontal());
               // baris += 1;
            //}

			//Tambahkan header tanggal dan invoice
			if (noBill != matCost.getOID()){
				 //String whereClause = PstMatReceive.fieldNames[PstMatReceive.FLD_INVOICE_SUPPLIER]+"='"+matCost.getInvoiceSupplier()+"'";
                               // Vector vect = PstMatReceive.list(0,0,whereClause,"");

                                noBill = matCost.getOID();
				if (firstRow == true){

					firstRow = false;
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
					//Add Header
					lstData.add(drawHeader(language));
					baris += 1;
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;

					//Add sub kategori
					//rowx.add("*3");
					//rowx.add("|");
					//rowx.add("Kategori");
					//rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cat.getName()+"</div>");
					//rowx.add("|");
					//lstData.add(rowx);
					//rowx = new Vector();
					//baris += 1;


                                        //Add date
                                        rowx.add("*3");
                                        rowx.add("|");
                                        rowx.add(textListMaterialDetail[language][0]);
                                        rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(matCost.getCostingDate(), "dd-MM-yyyy")+"</div>");
                                        rowx.add("<div align=\"center\">"+"|"+"</div>");
                                        lstData.add(rowx);
                                        baris += 1;

                                        //Add code biaya
                                        rowx = new Vector();
                                        rowx.add("*3");
                                        rowx.add("|");
                                        rowx.add(textListMaterialDetail[language][1]);
                                        rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+matCost.getCostingCode()+"</div>");
                                        rowx.add("<div align=\"center\">"+"|"+"</div>");
                                        lstData.add(rowx);
                                        baris += 1;

					lstData.add(drawLineHorizontal());
					baris += 1;
                                        rowx = new Vector();
                                        firstRow = true;
				}else{
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
						if (baris == maxlines){
							if(boolmaxlines){
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
					//Add sub total then header
					//Add sub total
					rowx.add("|");
					rowx.add("");
					rowx.add("");
					rowx.add("");
					rowx.add("");
					rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+subTotalQty+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
					rowx.add("<div align=\"center\">"+"|"+"</div>");
					lstData.add(rowx);
					baris += 1;
						if (baris == maxlines)
						{
							if(boolmaxlines){
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
					//Add Under line Total
					lstData.add(drawLineSubTotal());
					baris += 1;
						if (baris == maxlines)
						{
							if(boolmaxlines){
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
					//Add Under line
					lstData.add(drawLineHorizontal());
					baris += 1;
						if (baris == maxlines){
							if(boolmaxlines){
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
					subTotalCost = 0.00;
					subTotalQty = 0;
					internalCounter = 0;
				}
			}
            

			internalCounter += 1;
			rowx.add("|");
			rowx.add(""+(i+1));
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(mat.getSku());
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(material_name);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(costItem.getQty())+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add(unit_name);
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getAveragePrice())+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(costItem.getQty() * mat.getAveragePrice())+"</div>");
			rowx.add("<div align=\"center\">"+"|"+"</div>");
			lstData.add(rowx);
			baris += 1;
				if (baris == maxlines){
					if(boolmaxlines){
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

			totalPrice += (costItem.getQty() * mat.getAveragePrice());
			totalQty += costItem.getQty();
			subTotalPrice += (costItem.getQty() * mat.getAveragePrice());
			subTotalQty += costItem.getQty();

			lstLinkData.add("");
		}

		//Add Underline
		lstData.add(drawLineHorizontal());
		//Add SUB TOTAL
		Vector rowx = new Vector();
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+subTotalQty+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		lstData.add(rowx);
		//Add Under line Total
		lstData.add(drawLineTotal());
		//Add TOTAL
		rowx = new Vector();
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("");
		rowx.add("<div align=\"right\">"+"TOTAL"+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalPrice)+"</div>");
		rowx.add("<div align=\"center\">"+"|"+"</div>");
		lstData.add(rowx);
		//Add Under line Total
		lstData.add(drawLineTotal());
		result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
	}
	else {
		result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][5]+"</div>");
	}
	return result;
}
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
boolean isCategory = false;
boolean isSubCategory = false;
boolean isDispatchTo = false;
boolean isSupplier = false;
boolean isCosting = false;

ControlLine ctrLine = new ControlLine();
SrcReportDispatch srcReportDispatch = new SrcReportDispatch();
SessReportCosting sessReportDispatch = new SessReportCosting();
FrmSrcReportDispatch frmSrcReportDispatch = new FrmSrcReportDispatch(request, srcReportDispatch);
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST)
{
	 try
	 {
		srcReportDispatch = (SrcReportDispatch)session.getValue(SessReportCosting.SESS_SRC_REPORT_DISPATCH);
		if (srcReportDispatch == null) srcReportDispatch = new SrcReportDispatch();
	 }
	 catch(Exception e)
	 {
		srcReportDispatch = new SrcReportDispatch();
	 }
}
else
{
	 frmSrcReportDispatch.requestEntityObject(srcReportDispatch);

          //vector status
      Vector vectCostId = new Vector(1,1);
       String[] strCostId = request.getParameterValues(FrmSrcReportDispatch.fieldNames[FrmSrcReportDispatch.FRM_FIELD_COSTING_ID]);
       if(strCostId!=null && strCostId.length>0) {
         for(int i=0; i<strCostId.length; i++) {
	   try {
                    vectCostId.add(strCostId[i]);
                }
            catch(Exception exc) {
                    System.out.println("err");
                    }
                 }
            }
       srcReportDispatch.setCostingId(vectCostId);
       //end of status
	 session.putValue(SessReportCosting.SESS_SRC_REPORT_DISPATCH, srcReportDispatch);
}
//Vector records = SessReportCosting.getReportCosting(srcReportDispatch);
Vector records = SessReportCosting.getReportCostingTypeCosting(srcReportDispatch);
int vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST)
{
	//start = ctrlReportDispatch.actionList(iCommand,start,vectSize,recordToGet);
}

Location location_from = new Location();
if (srcReportDispatch.getLocationId() != 0) {
	try	{
		location_from = PstLocation.fetchExc(srcReportDispatch.getLocationId());
	}
	catch(Exception e) {
	}
}
else
    location_from.setName(textListHeader[SESS_LANGUAGE][10]);

Location location_to = new Location();
if (srcReportDispatch.getDispatchTo() != 0) {
	try	{
		location_to = PstLocation.fetchExc(srcReportDispatch.getDispatchTo());
	}
	catch(Exception e) {
	}
}
else
    location_to.setName(textListHeader[SESS_LANGUAGE][10]);

/*Category category = new Category();
if (srcReportDispatch.getCategoryId() != 0){
	try{
		category = PstCategory.fetchExc(srcReportDispatch.getCategoryId());
	}
	catch(Exception e){
	}
}
else {
	category.setName(textListHeader[SESS_LANGUAGE][8]+" "+textListHeader[SESS_LANGUAGE][4]);
}*/

Costing costing = new Costing();
//if (srcReportDispatch.getCostingId() != 0){
	//try{
		//costing = PstCosting.fetchExc(srcReportDispatch.getCostingId());
	//}
	//catch(Exception e){
	////}
//}
//else {
	//costing.setName(textListHeader[SESS_LANGUAGE][8]+" "+textListHeader[SESS_LANGUAGE][9]);
//}

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
}

function cmdListFirst()
{
	document.frm_reportdispatch.command.value="<%=Command.FIRST%>";
	document.frm_reportdispatch.action="reportcostinginvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListPrev()
{
	document.frm_reportdispatch.command.value="<%=Command.PREV%>";
	document.frm_reportdispatch.action="reportcostinginvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListNext()
{
	document.frm_reportdispatch.command.value="<%=Command.NEXT%>";
	document.frm_reportdispatch.action="reportcostinginvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdListLast()
{
	document.frm_reportdispatch.command.value="<%=Command.LAST%>";
	document.frm_reportdispatch.action="reportcostinginvoice_list.jsp";
	document.frm_reportdispatch.submit();
}

function cmdBack()
{
	document.frm_reportdispatch.command.value="<%=Command.BACK%>";
	document.frm_reportdispatch.action="src_reportcostinginvoice.jsp";
	document.frm_reportdispatch.submit();
}

function printForm(){
	window.open("reportcostinginvoice_form_print.jsp","costingkategorireport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

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
            <form name="frm_reportdispatch" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
				<tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" valign="middle">
				  	<h4><strong><%=textListGlobal[SESS_LANGUAGE][0]%></strong></h4>
				  </td>
                </tr>
				<tr align="left" valign="top"> 
                  <td class="command" width="11%"><strong><%=(textListHeader[SESS_LANGUAGE][0]).toUpperCase()%></strong></td>
				  <td class="command" width="0%"><strong>:</strong></td>
				  <td class="command" width="89%"><%=Formater.formatDate(srcReportDispatch.getDateFrom(), "dd-MM-yyyy")%><%=textListHeader[SESS_LANGUAGE][1]%><%=Formater.formatDate(srcReportDispatch.getDateTo(), "dd-MM-yyyy")%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td class="command"><strong><%=(textListHeader[SESS_LANGUAGE][2]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%=location_from.getName().toUpperCase()%></td>
                </tr>
				<tr align="left" valign="top"> 
                  <td class="command"><strong><%=(textListHeader[SESS_LANGUAGE][3]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%=location_to.getName().toUpperCase()%></td>
                </tr>
				<!--<tr align="left" valign="top">
                  <td class="command"><strong><%//=(textListHeader[SESS_LANGUAGE][9]).toUpperCase()%></strong></td>
				  <td class="command"><strong>:</strong></td>
				  <td class="command"><%//=costing.getName().toUpperCase()%></td>
                </tr>-->
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%
                  //Vector vct = drawList2(SESS_LANGUAGE, records);
                  //out.println((String)vct.get(0));
					Vector hasil = drawList(SESS_LANGUAGE,records);
					Vector report = new Vector(1,1);
					report.add(srcReportDispatch);
					report.add(hasil);
					try
					{
						session.putValue("SESS_MAT_REPORT_COSTING",report);
					}
					catch(Exception e){}

					for(int k=0;k<hasil.size();k++){
						out.println(hasil.get(k));
					}

					%> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="90%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][6],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table></td>
                        <td width="20%"> 
						  <%if(records!=null && records.size()>0){%>
						  <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td width="95%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm()" class="command" ><i class="fa fa-print">&nbsp;<%=textListGlobal[SESS_LANGUAGE][4]%></i></a></td>
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
