<%-- 
    Document   : reportsale_list_global_margin_excel
    Created on : May 3, 2016, 3:36:25 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                com.dimata.posbo.form.search.FrmSrcSaleReport,
                com.dimata.posbo.report.sale.SaleReportDocument,
                com.dimata.posbo.report.sale.SaleReportItem,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command"%>
<%@ page import="com.dimata.posbo.entity.masterdata.Material"%>
<%@ page import="com.dimata.pos.entity.billing.Billdetail"%>
<%@ page import="com.dimata.common.entity.payment.StandartRate"%>
<%@ page import="com.dimata.common.entity.payment.PstStandartRate"%>
<%@ page import="com.dimata.posbo.entity.masterdata.PstMaterial"%>
<%@ page language = "java" %>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_GROSS_MARGIN); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = 
{
	{"NO","SKU","NAMA BARANG","HRG BELI","HRG JUAL","JUMLAH","STN","TOTAL HPP","TOTAL JUAL","LABA KOTOR","LABA KOTOR(%)","(tidak tercatat)","QTY STOK","SKU","BARCODE"},
	{"NO","SKU","NAMA BARANG","AVERAGE","PRICE","QTY","STN","TOTAL AVERAGE","TOTAL SELL","MARGIN","MARGIN(%)","(unspecified)","STOCK QTY","SKU","BARCODE"}
};

public static final String textListTitleHeader[][] = 
{
	{"LAPORAN LABA KOTOR TRANSAKSI ","LAPORAN LABA KOTOR TRANSAKSI ","Tidak ada data transaksi ..","LOKASI","SHIFT","Laporan Laba Kotor Transaksi","Cetak Laporan Laba Kotor Transaksi ","TIPE","s/d","Urut Turun","Urut Naik"},
	{"MARGIN REPORT OF ","MARGIN REPORT OF ","No transaction data available ..","LOCATION","SHIFT","Margin Report Of ","Print Margin Report Of ","TYPE","to","Descending","Ascending"}        
};

    // this for get data net supplier
    public double gettotalnettsupplier(double totalCost, long materialOid){
        double total = 0;
        Material material = new Material();
        try{
            material = PstMaterial.fetchExc(materialOid);
        }catch(Exception e){}
        StandartRate standartRate = PstStandartRate.getActiveStandardRate(material.getDefaultCostCurrencyId());
        total = (totalCost);// * standartRate.getSellingRate());
        return total;
    }

public Vector drawList(int language,Vector objectClass,SrcSaleReport srcSaleReport,int marginPctMethod)
{
    String result = "";
    Vector listAll = new Vector(1,1);
    Vector listTableHeaderPdf = new Vector(1,1);
    Vector listContentPdf = new Vector(1,1);
    Vector listTableFooterPdf = new Vector(1,1); 
    Vector listBodyPdf = new Vector(1,1);
    double grandTotalQty = 0;
    double grandTotalSold = 0;
    double grandTotalMargin = 0;
    double grandTotalMarginPct = 0;
    double grandTotalCost = 0;
    double grandStock=0;

	if(objectClass!=null && objectClass.size()>0)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setCellSpacing("0");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.setBorder(1);
		ctrlist.dataFormat(textListMaterialHeader[language][0],"5%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][0]);
                String groupTitle = "";
                int groupMeth = srcSaleReport.getGroupBy();

                if(groupMeth==SrcSaleReport.GROUP_BY_ITEM){
                    ctrlist.dataFormat(textListMaterialHeader[language][13],"5%","0","0","left","bottom");
                    listTableHeaderPdf.add(textListMaterialHeader[language][13]);
                    ctrlist.dataFormat(textListMaterialHeader[language][14],"8%","0","0","left","bottom");
                    listTableHeaderPdf.add(textListMaterialHeader[language][14]);
                }   
                groupTitle = SrcSaleReport.groupMethod[language][groupMeth];
                ctrlist.dataFormat(""+groupTitle+"<a href=\"javascript:cmdSort('"+srcSaleReport.getGroupBy()+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">  </a><a href=\"javascript:cmdSort('"+srcSaleReport.getGroupBy()+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][9]+"\">  </a>","30%","0","0","center","bottom");
                listTableHeaderPdf.add(groupTitle);
                ctrlist.dataFormat(textListMaterialHeader[language][5]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">   </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][9]+"\">  </a>","8%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][5]);
                
                if(srcSaleReport.getViewStock()==1 && groupMeth==SrcSaleReport.GROUP_BY_ITEM){
                    ctrlist.dataFormat(textListMaterialHeader[language][12],"5%","0","0","right","bottom");
                    listTableHeaderPdf.add(textListMaterialHeader[language][12]);
                }

                ctrlist.dataFormat(textListMaterialHeader[language][8]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">   </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][9]+"\">  </a>","15%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][8]);
                ctrlist.dataFormat(textListMaterialHeader[language][7],"15%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][7]);
                ctrlist.dataFormat(textListMaterialHeader[language][9]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_MARGIN+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">   </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_MARGIN+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][9]+"\">  </a>","15%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][9]);
                ctrlist.dataFormat(textListMaterialHeader[language][10]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_MARGIN_PCT+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">   </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_MARGIN_PCT+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][9]+"\">  </a>","15%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][10]);
                
		ctrlist.setLinkRow(-1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
                //ctrlist.setLinkRow(1);
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();

		for(int i=0; i<objectClass.size(); i++) {
                        Vector rowPdf = new Vector(1,1);
			Vector rowx = new Vector();
			SaleReportItem saleItem = (SaleReportItem)objectClass.get(i);
                        String linkParameter = ""; 
                        rowx.add("<div align=\"center\">"+(i+1)+"</div>");
                        rowPdf.add(""+(i+1));
                        String nameValue = null; 
                        long materialId=0;
                        switch(groupMeth){
                        case SrcSaleReport.GROUP_BY_CATEGORY:
                            nameValue = saleItem.getCategoryName();
                            //rowx.add("<div align=\"left\">"+saleItem.getCategoryName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getCategoryId());
                            break;
                        case SrcSaleReport.GROUP_BY_LOCATION:
                            nameValue = saleItem.getLocationName();
                            //rowx.add("<div align=\"left\">"+saleItem.getLocationName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getLocationId());
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
                            nameValue = saleItem.getSalesPersonName();
                            //rowx.add("<div align=\"left\">"+saleItem.getSalesPersonName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getSalesPersonId());
                            break;
                        case SrcSaleReport.GROUP_BY_SHIFT:
                            nameValue = saleItem.getShiftName();
                            //rowx.add("<div align=\"left\">"+saleItem.getShiftName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getShiftId());
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            nameValue = saleItem.getSupplierName();
                            //rowx.add("<div align=\"left\">"+saleItem.getSupplierName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getSupplierId());
                            break;                                
                        default:
                            nameValue = saleItem.getItemName();
                            materialId = saleItem.getItemId();
                            //rowx.add("<div align=\"left\">"+saleItem.getItemName()+"</div>");
                            linkParameter = String.valueOf(saleItem.getItemId());
                           break;
                        }
						
                        if(nameValue==null) {
                            nameValue = textListMaterialHeader[language][11];
                        }
						
			double costValue = 0.0; //gettotalnettsupplier(saleItem.getTotalCost(), saleItem.getItemId());
                        
                        

                        if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
                            costValue = saleItem.getTotalMarginPct();
                        }else{
                            costValue = saleItem.getTotalCost();
                        }
						
                        double totalMargin = 0;
                        double totalMarginPct = 0;
                        if(saleItem.getTotalSold() > 0){
                            totalMargin = (saleItem.getTotalSold() - costValue); // saleItem.getTotalCost()
                            if(marginPctMethod==SrcSaleReport.MARGIN_BY_COST){
                                totalMarginPct = ((saleItem.getTotalSold() - costValue)/ saleItem.getTotalCost())*100;
                            }else if(marginPctMethod==SrcSaleReport.MARGIN_BY_SOLD){
                                totalMarginPct = ((saleItem.getTotalSold() - costValue) / saleItem.getTotalSold())*100;
                            }
                        }
						
			//textListMaterialHeader
                        if(groupMeth==SrcSaleReport.GROUP_BY_ITEM){
                            rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>"+saleItem.getItemCode()+"</div>");
                            rowPdf.add(""+saleItem.getItemCode());
                            rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"' >"+saleItem.getItemBarcode()+"</div>");
                            rowPdf.add(""+saleItem.getItemBarcode());
                        }                         

                        if(materialId!=0){                        
                            //rowx.add("<div align=\"left\">"+nameValue+"</div>");
                            rowx.add("<div align=\"left\">"+nameValue+"</div>");
                        }else{
                            rowx.add("<div align=\"left\">"+nameValue+"</div>");
                        }
                        rowPdf.add(nameValue);
                        //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock())+"</div>");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalQty())+"</div>");
                        rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()));

                        double sisaStock=0;
                        if(srcSaleReport.getViewStock()==1 && groupMeth==SrcSaleReport.GROUP_BY_ITEM){
                            if(srcSaleReport.getLocationId()!=0){
                                //String date = Formater.formsrcSaleReport.getDateTo();
                                //Date endDate = new Date(srcSaleReport.getDateTo().getTime());
                                String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd 23:59:59");
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                                Date date = new Date();
                                try{
                                    date = format.parse(endDate);
                                }catch(Exception ex){}
                                sisaStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(saleItem.getItemId(), srcSaleReport.getLocationId(), date, 0);
                                
                                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(sisaStock)+"</div>");
                                rowPdf.add(FRMHandler.userFormatStringDecimal(sisaStock));
                            }else{
                                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(sisaStock)+"</div>");
                                rowPdf.add(FRMHandler.userFormatStringDecimal(sisaStock));
                            }
                        }

                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalSold())+"</div>");
                        rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));
                        if(materialId!=0){
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(costValue)+"</div>");
                        }else{
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(costValue)+"</div>");
                        }
                        rowPdf.add(FRMHandler.userFormatStringDecimal(costValue));
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalMargin)+"</div>");
                        rowPdf.add(FRMHandler.userFormatStringDecimal(totalMargin)+"");
                        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalMarginPct)+"%</div>");
                        rowPdf.add(FRMHandler.userFormatStringDecimal(totalMarginPct)+"%");
						
                        //grandTotalQty = grandTotalQty + saleItem.getTotalQtyByStock();
                        grandTotalQty = grandTotalQty + saleItem.getTotalQty();
                        grandTotalSold = grandTotalSold + saleItem.getTotalSold();
                        grandTotalCost = grandTotalCost + costValue;
                        grandTotalMargin = grandTotalMargin + totalMargin;
                        grandTotalMarginPct = (grandTotalMarginPct + totalMarginPct);
			grandStock = grandStock + sisaStock;			
                        //vtPdf.add(""+(i+1));
                        //vtPdf.add(saleItem.getItemName());
                        //vtPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()));
                        //vtPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));
                        //vtPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()));
                        //vtPdf.add(FRMHandler.userFormatStringDecimal(totalMargin));
                        //vtPdf.add(FRMHandler.userFormatStringDecimal(totalMarginPct));
                        //vtPdf.add("");
                        //vtPdf.add("");
                        //vtPdf.add(strHrgJual);
                        //vtPdf.add(""+cbd.getQty());
                        //vtPdf.add(unt.getCode());
                        //vtPdf.add(strTotalBeli);
                        //vtPdf.add(strTotalJual);
                        //vtPdf.add(strMargin);
            listContentPdf.add(rowPdf);

	    lstData.add(rowx);
            lstLinkData.add(linkParameter);
	    }

        Vector rowx = new Vector();
        //strTotalBeli = FRMHandler.userFormatStringDecimal(totBeli);
        //strTotalJual = FRMHandler.userFormatStringDecimal(totJual);

        rowx.add("");
        listTableFooterPdf.add("");
        if(groupMeth==SrcSaleReport.GROUP_BY_ITEM){
            rowx.add("");
            listTableFooterPdf.add("");
            rowx.add("");
            listTableFooterPdf.add("");
        }
        //rowx.add("");
        rowx.add("<div align=\"right\"><b>GRAND TOTAL</b></div>");
        listTableFooterPdf.add("GRAND TOTAL ");
        //rowx.add("");
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalQty)+"</b></div>");
        listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(grandTotalQty));
        if(srcSaleReport.getViewStock()==1 && groupMeth==SrcSaleReport.GROUP_BY_ITEM){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandStock)+"</b></div>");
                listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(grandStock));
        }
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalSold)+"</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalSold));
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalCost)+"</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalCost));
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalMargin)+"</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalMargin));
        if(marginPctMethod==SrcSaleReport.MARGIN_BY_COST){
                            grandTotalMarginPct = ((grandTotalSold-grandTotalCost)/grandTotalCost)*100;
         }else if(marginPctMethod==SrcSaleReport.MARGIN_BY_SOLD){
                            grandTotalMarginPct = ((grandTotalSold-grandTotalCost)/grandTotalSold)*100;
        }
        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalMarginPct)+" %</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalMarginPct)+"%");
        
        
        

        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));

        result = ctrlist.draw();
    }else{
		result = "<div class=\"msginfo\">&nbsp;&nbsp;"+textListTitleHeader[language][2]+"</div>";
	}
    listAll.add(result);
    listBodyPdf.add(listTableHeaderPdf);
    listBodyPdf.add(listContentPdf);
    listBodyPdf.add(listTableFooterPdf);
    listAll.add(listBodyPdf);
	return listAll;
}
%>

<%

int iErrCode = FRMMessage.ERR_NONE;
String msgStr = "";
int iCommand = FRMQueryString.requestCommand(request);
long oidGroup = FRMQueryString.requestLong(request, "hidden_group_id");
int sortMethod = 0;
sortMethod = FRMQueryString.requestInt(request, "sort_method");
int global_group = 0;
global_group = FRMQueryString.requestInt(request, "global_group");
int descSort = 0;
descSort = FRMQueryString.requestInt(request, "sort_desc");
int start = FRMQueryString.requestInt(request, "start");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
int sortCommand = 9999; 

/**
* instantiate some object used in this page
*/
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
SrcSaleReport srcSaleReportDetail = new SrcSaleReport();
SaleReportDocument saleReportDocument= new SaleReportDocument();
FrmSrcSaleReport frmSrcSaleReportDetail = new FrmSrcSaleReport(request, srcSaleReportDetail);
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);

/**
* handle current search data session 
*/
    try{
        srcSaleReport= (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC); 
        if (srcSaleReport== null) {
            srcSaleReport= new SrcSaleReport();
            }
    }catch(Exception e){
        srcSaleReport = new SrcSaleReport();
    }

    //System.out.println(srcSaleReport.showValues());
   session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport); 
   session.putValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL, srcSaleReportDetail);
   srcSaleReport.setQueryType(srcSaleReport.getGroupBy());
   srcSaleReport.setSortBy(sortMethod);
   srcSaleReport.setDescSort(descSort); 
   
    Vector records = SaleReportDocument.getList(srcSaleReport,0,0,SaleReportDocument.LOG_MODE_CONSOLE);   
    vectSize = records.size();
    Vector listHeaderPdf = new Vector(1,1);    
    Vector listBodyPdf = new Vector(1,1); 
    Vector listPdf = new Vector(1,1);
    Vector list = drawList(SESS_LANGUAGE,records,srcSaleReport,SrcSaleReport.MARGIN_BY_COST);
    String str = "";

    // LIST FOR MAIN
	Vector compTelpFax = (Vector)companyAddress.get(2);
	listHeaderPdf.add(0,(String) companyAddress.get(0));
	listHeaderPdf.add(1,(String) companyAddress.get(1));
	listHeaderPdf.add(2, (String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1));
    
    try{
        str = (String)list.get(0);
        //list.set(0,listPdf);
        listBodyPdf= (Vector)list.get(1); 
        
    }catch(Exception e)
	{
	}
    
        try{
            //System.out.println("Removing");
	//  session.removeValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL);
            session.removeValue("hidden_group_id");
            session.removeValue("sort_method");
            //System.out.println("Removed");
        }catch(Exception e){
            //System.out.println("Failed");
        }

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "reportsale_list_global_margin.xls" ); 
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> 
            <%=textListTitleHeader[SESS_LANGUAGE][0]%> <%=new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase() %> <%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()%>
			<% listHeaderPdf.add(3,textListTitleHeader[SESS_LANGUAGE][0]+" "+new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()+" "+SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()); %> <!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td><!-- #BeginEditable "content" --> 
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="sort_method" value="<%=srcSaleReport.getSortBy()%>">
              <input type="hidden" name="sort_desc" value="<%=srcSaleReport.getDescSort()%>">
              <input type="hidden" name="global_group" value="<%=global_group%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="hidden_group_id" value="0">
              <input type="hidden" name="hidden_material_id">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td valign="middle" colspan="3"> <hr size="1"> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center"><b><%=textListTitleHeader[SESS_LANGUAGE][1]%> <%=new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()%> <%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()%></b></td>
                </tr>
                <%
            listHeaderPdf.add(4,textListTitleHeader[SESS_LANGUAGE][1]+" "+new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()+" "+SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase());
            %>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center">
				  <b>
                  <%
				  out.println(Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy"));
				  %> <%=" "+textListTitleHeader[SESS_LANGUAGE][8]+" "%>  <%
				  out.println(Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
				  %>
				  <%
                                    listHeaderPdf.add(5,"Periode "+Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textListTitleHeader[SESS_LANGUAGE][8]+" "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
                                    %>
				  </b> 
				  </td>
                </tr>
                <%
                            Location location = new Location();
                            if(srcSaleReport.getLocationId()==0){
                                location.setName(" All ") ;}
                            else{
                             location = PstLocation.fetchExc(srcSaleReport.getLocationId());
                            }
                            %>
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="3" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_LOCATION]%>: <%=location.getName()%> </b> </td>
                                <%
                                listHeaderPdf.add(6,SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_LOCATION]+":"+location.getName());
                                %>
                            </tr>
              
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%=str%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    </span> </td>
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

