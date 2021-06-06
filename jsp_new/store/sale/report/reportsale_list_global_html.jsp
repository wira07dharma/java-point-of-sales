<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.hanoman.entity.masterdata.Contact"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstContact"%>
<%@ page import="com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.posbo.report.sale.SaleReportItem,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command"%>
<%@ page language = "java" %>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_SALES_GLOBAL); %>
<%@ include file = "../../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO","SKU","NAMA BARANG","HRG BELI","HRG JUAL","JUMLAH QTY","STN","TOTAL BELI","TOTAL JUAL","LABA","(tidak tercatat)","TOTAL INV","TOTAL TRANSAKSI"},
    {"NO","SKU","NAMA BARANG","COST","PRICE","TOTAL QTY","STN","TOTAL COST","TOTAL SELL","MARGIN","(unspecified)","TOTAL INV","TOTAL TRANSAKSI"}
};

public static final String textListTitleHeader[][] = {
    {"LAPORAN TRANSAKSI ","LAPORAN TRANSAKSI ","Tidak ada data transaksi","Lokasi","SHIFT","Laporan",
             "Cetak Laporan Transaksi ","TIPE","Semua "," s/d ","Urut Turun","Urut Naik", "Periode"},
    {"TRANSACTION REPORT OF ","TRANSATION TYPE OF ","No transaction data available","Location","SHIFT","Report",
             "Print Transaction Report Of","TYPE","All "," to ","Descending","Ascending","Period"}
};

public Vector drawList(int language,Vector objectClass,SrcSaleReport srcSaleReport) {
    String result = "";
    Vector listAll = new Vector(1,1);
    Vector listTableHeaderPdf = new Vector(1,1);
    Vector listContentPdf = new Vector(1,1);
    Vector listTableFooterPdf = new Vector(1,1);
    Vector listBodyPdf = new Vector(1,1);
    double grandTotalQty = 0;
    double grandTotalSold = 0;
    double totalBonus = 0;
    double totalSale = 0;
    double totalInv = 0;
    double totalCost = 0.0;
    double totalTransaksi = 0;

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

        ctrlist.dataFormat(textListMaterialHeader[language][0],"5%","0","0","center","bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][0]);
        
        String groupTitle = "";
        int groupMeth = srcSaleReport.getGroupBy();
        groupTitle = SrcSaleReport.groupMethod[language][groupMeth];
        
        ctrlist.dataFormat(groupTitle.toUpperCase()+"<a href=\"javascript:cmdSort('"+srcSaleReport.getGroupBy()+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\">  </a><a href=\"javascript:cmdSort('"+srcSaleReport.getGroupBy()+"','"+SrcSaleReport.SORT_DESC+"')\" > <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">  </a>","25%","0","0","center","bottom");
        
        listTableHeaderPdf.add(groupTitle.toUpperCase());
        
        if(groupMeth==SrcSaleReport.GROUP_BY_SHIFT){
                ctrlist.dataFormat(textListMaterialHeader[language][12]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\"> </a>","10%","0","0","center","bottom");
                listTableHeaderPdf.add(textListMaterialHeader[language][12]);
        }
        
        /*if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat(textListMaterialHeader[language][11]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\"> </a>","10%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][11]);
        }*/
        if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat(textListMaterialHeader[language][12]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\"> </a>","10%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][12]);
        }else{
            ctrlist.dataFormat(textListMaterialHeader[language][5]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\"> </a>","10%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][5]);
        }
        
        if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat(textListMaterialHeader[language][5]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_QTY+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\"> </a>","10%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][5]);
        }else{
            ctrlist.dataFormat(textListMaterialHeader[language][8]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">  </a>","15%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][8]);
        }

        //hapus
        if(groupMeth!=SrcSaleReport.GROUP_BY_SALES && groupMeth!=SrcSaleReport.GROUP_BY_SHIFT){
            ctrlist.dataFormat(textListMaterialHeader[language][7]+"<a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">  </a>","15%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][7]);
        }

        if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat(textListMaterialHeader[language][8]+" SALES <a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_ASC+"')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][11]+"\"> </a><a href=\"javascript:cmdSort('"+SrcSaleReport.SORT_BY_TOTAL_SALE+"','"+SrcSaleReport.SORT_DESC+"')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\"  width=\"10\" height=\"10\" alt=\""+textListTitleHeader[language][10]+"\">  </a>","15%","0","0","center","bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][8]+" SALES ");
        }

        //buatkan laba dan margin
        if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
            ctrlist.dataFormat("LABA KOTOR ","15%","0","0","center","bottom");
            listTableHeaderPdf.add("LABA KOTOR ");
        }
        if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
            ctrlist.dataFormat("MARGIN ","15%","0","0","center","bottom");
            listTableHeaderPdf.add(" MARGIN ");
        }

        
        if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat("Bonus","10%","0","0","center","bottom");
            listTableHeaderPdf.add("Bonus");
        }
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        //ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        double totalbruto=0;
        for(int i=0; i<objectClass.size(); i++){
            Vector rowPdf = new Vector(1,1);
            Vector rowx = new Vector();
            SaleReportItem saleItem = (SaleReportItem)objectClass.get(i);
            String linkParameter = "";
            
            try {
                rowx.add("<div align=\"center\">"+(i+1)+"</div>");
                rowPdf.add(""+(i+1));
                String nameValue = null;
                switch(groupMeth){
                    case SrcSaleReport.GROUP_BY_CATEGORY:
                        nameValue = saleItem.getCategoryName();
                        
                        linkParameter = String.valueOf(saleItem.getCategoryId());
                        break;
                    case SrcSaleReport.GROUP_BY_LOCATION:
                        nameValue = saleItem.getLocationName();
                        
                        linkParameter = String.valueOf(saleItem.getLocationId());
                        break;
                    case SrcSaleReport.GROUP_BY_SALES:
                        nameValue = saleItem.getSalesPersonName();
                        
                        linkParameter = String.valueOf(saleItem.getSalesPersonId());
                        break;
                    case SrcSaleReport.GROUP_BY_SHIFT:
                        nameValue = saleItem.getShiftName();
                        
                        linkParameter = String.valueOf(saleItem.getShiftId());
                        break;
                    case SrcSaleReport.GROUP_BY_SUPPLIER:
                        nameValue = saleItem.getSupplierName();
                        
                        linkParameter = String.valueOf(saleItem.getSupplierId());
                        break;
                    //add Group by merk, by Mirah
                     case SrcSaleReport.GROUP_BY_MARK:
                        nameValue = saleItem.getMarkName();

                        linkParameter = String.valueOf(saleItem.getMarkId());
                        break;
                    default:
                        nameValue = saleItem.getItemName();
                        linkParameter = String.valueOf(saleItem.getItemId());
                        break;
                }
                if(nameValue==null){
                    nameValue = textListMaterialHeader[language][10];
                }
                //textListMaterialHeader
                rowx.add("<div align=\"left\">"+nameValue+"</div>");
                rowPdf.add(nameValue);

                if(groupMeth==SrcSaleReport.GROUP_BY_SHIFT){
                        rowx.add("<div align=\"right\">"+saleItem.getTotTransaksi()+"</div>");
                        rowPdf.add(""+saleItem.getTotTransaksi());//userFormatStringDecimalWithoutPoint
                        totalTransaksi = totalTransaksi + saleItem.getTotTransaksi();
                }

                if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getDiscount())+"</div>");
                    rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getDiscount()));//userFormatStringDecimalWithoutPoint
                    totalInv = totalInv + saleItem.getDiscount();
                }
                //rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock())+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalQty())+"</div>");
                rowPdf.add(""+FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()));

                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalSold())+"</div>");
                rowPdf.add(""+FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));
                
                //hapus sales
                if(groupMeth!=SrcSaleReport.GROUP_BY_SALES && groupMeth!=SrcSaleReport.GROUP_BY_SHIFT){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalCost())+"</div>");
                    rowPdf.add(""+FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()));
                }

//                if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
//                    Sales sales = new Sales();
//                      AppUser ap = new AppUser();
//                    try{
//                        sales = PstSales.fetchExc(saleItem.getSalesPersonId());
//                      ap = PstAppUser.fetch(saleItem.getSalesPersonId());
//                    }catch(Exception e){}
//                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((saleItem.getTotalSold()*sales.getCommision())/100)+"</div>");
//                    rowPdf.add(""+FRMHandler.userFormatStringDecimal((saleItem.getTotalSold()*sales.getCommision())/100));
//                    totalBonus = totalBonus + (saleItem.getTotalSold()*sales.getCommision())/100;
//                }
                
                if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
                    totalbruto = totalbruto+(saleItem.getTotalSold()-saleItem.getTotalCost());
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()-saleItem.getTotalCost())+"</div>");
                    rowPdf.add("");
                }

                if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(((saleItem.getTotalSold()-saleItem.getTotalCost())/saleItem.getTotalCost())*100)+"%</div>");
                    rowPdf.add("");
                }


                //grandTotalQty = grandTotalQty + saleItem.getTotalQtyByStock();
                grandTotalQty = grandTotalQty + saleItem.getTotalQty();
                grandTotalSold = grandTotalSold + saleItem.getTotalSold();
                totalCost = totalCost + saleItem.getTotalCost();
                        
                // untuk report pdf
                //vtPdf.add(""+(i+1));
                //vtPdf.add(saleItem.getItemName());
                //vtPdf.add(""+saleItem.getTotalQty());
                //vtPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));
                
                //listPdf.add(vtPdf);
                listContentPdf.add(rowPdf);
                lstData.add(rowx);
                lstLinkData.add(linkParameter);
            } catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        try {
            Vector rowx = new Vector();
            
            listTableFooterPdf.add("");
            /* if(groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
               rowx.add("");
            }
            else {*/
            listTableFooterPdf.add("GRAND TOTAL ");
            //}
            
            rowx.add("");
            /*if(groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
               rowx.add("");
            }
            else {*/
               rowx.add("<div align=\"right\" colspan=\"2\"><b>GRAND TOTAL</b></div>");
                if(groupMeth==SrcSaleReport.GROUP_BY_SHIFT){
                     rowx.add("<div align=\"right\"><b>"+totalTransaksi+"</b></div>");
                     listTableFooterPdf.add(""+totalTransaksi);
                 }
            //}
            if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalInv)+"</b></div>");
                listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(totalInv));
            }
            /*if(groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
               rowx.add("");
            }
            else {*/
             rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalQty)+"</b></div>");
             listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(grandTotalQty));
           //}
            /*if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalSale)+"</b></div>");
                listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(totalSale));
            }*/
           /*if(groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
             rowx.add("");
            }
            else {*/
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(grandTotalSold)+"</b></div>");
            listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(grandTotalSold));
            
                if(groupMeth!=SrcSaleReport.GROUP_BY_SALES && groupMeth!=SrcSaleReport.GROUP_BY_SHIFT){
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalCost)+"</b></div>");
                    listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(totalCost));
                }

            if(groupMeth==SrcSaleReport.GROUP_BY_SALES){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalBonus)+"</b></div>");
                listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(totalBonus));
            }
            
            if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalbruto)+"</b></div>");
                    listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(totalbruto));
            }

            if(groupMeth==SrcSaleReport.GROUP_BY_CATEGORY || groupMeth==SrcSaleReport.GROUP_BY_SUPPLIER){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(((grandTotalSold-totalCost)/totalCost)*100)+"%</b></div>");
                listTableFooterPdf.add(""+FRMHandler.userFormatStringDecimal(((grandTotalSold-totalCost)/totalCost)*100));
            }

            lstData.add(rowx);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        
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
//if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST ){
try {
    srcSaleReport= (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC);
    if (srcSaleReport== null) {
        srcSaleReport= new SrcSaleReport();
    }
}catch(Exception e){
    srcSaleReport = new SrcSaleReport();
}

//System.out.println(srcSaleReport.showValues());
session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);

/*srcSaleReport.setQueryType(srcSaleReport.getGroupBy());
//srcSaleReport.setGroupBy(SrcSaleReport.GROUP_BY_ITEM);

//session.putValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL, srcSaleReportDetail);
srcSaleReport.setSortBy(sortMethod);
srcSaleReport.setDescSort(descSort);*/

Vector records = SaleReportDocument.getList(srcSaleReport,0,20000,SaleReportDocument.LOG_MODE_CONSOLE);
vectSize = records.size();
Vector listHeaderPdf = new Vector(1,1);
Vector listBodyPdf = new Vector(1,1);
Vector listPdf = new Vector(1,1);
Vector list = drawList(SESS_LANGUAGE,records,srcSaleReport);
String str = "";
Vector compTelpFax = (Vector)companyAddress.get(2);

// LIST FOR MAIN
listHeaderPdf.add(0, companyAddress.get(0));
listHeaderPdf.add(1, companyAddress.get(1));
listHeaderPdf.add(2, ((String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1)));

try {
    str = (String)list.get(0);
    listBodyPdf= (Vector)list.get(1);
    
}catch(Exception e) {
    
}

try {
    session.removeValue("hidden_group_id");
    session.removeValue("sort_method");
}catch(Exception e){
    
}

merkName = PstSystemProperty.getValueByName("NAME_OF_MERK");

%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdSort(method,desc) {
	document.frm_reportsale.sort_method.value=method;
	document.frm_reportsale.sort_desc.value=desc;
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.command.value="<%=sortCommand%>";
	document.frm_reportsale.action="reportsale_list_global.jsp";
	document.frm_reportsale.submit();
}

function cmdEdit(oid) {
	document.frm_reportsale.hidden_group_id.value=oid;
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.command.value="<%=Command.LIST%>";
	document.frm_reportsale.action="reportsale_list_detail.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst() {
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev() {
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.action="reportsale_list_global.jsp";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.submit();
}

function cmdListNext() {
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast() {
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_global.jsp";
	document.frm_reportsale.submit();
}

function cmdBack() {
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="src_reportsale_global.jsp";
	document.frm_reportsale.submit();
}

function printForm() {
    //window.open("<//%=approot%>/servlet/com.dimata.posbo.report.sale.SaleReportPdf","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
    window.open("reportsale_list_global_html.jsp","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no"); 
}
function printFormExcel(){
    window.open("reportsale_list_global_excel.jsp","sale","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no"); 
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

function MM_swapImage() { //v3.0
	var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

//-->
</script>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<SCRIPT language=JavaScript>
<!--
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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
//-->
</SCRIPT>
<!-- #EndEditable --> 
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#FCFDEC" >
  <tr> 
    <td valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">  
        <tr> 
          <td height="20" class="mainheader"><!-- #BeginEditable "contenttitle" --> &nbsp; <!-- #EndEditable --></td>
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
              <table width="100%" cellspacing="0" cellpadding="0" border="0">
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center"><h4>
                    LAPORAN PENJUALAN GLOBAL
                  </h4></td>  
                </tr>  
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="3" align="center"><h4>
                    <%=textListTitleHeader[SESS_LANGUAGE][0]%> <%=new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()%> <%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+ new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()%>
                    <% listHeaderPdf.add(3,textListTitleHeader[SESS_LANGUAGE][0]+" "+new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase() +" "+SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()); %>
                  </h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase()%></b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">:
                    <%=(Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy"))%> <%=textListTitleHeader[SESS_LANGUAGE][9]%>
                    <%=(Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"))%>
                    <%
                    listHeaderPdf.add(4, (textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase());
                    listHeaderPdf.add(5, Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textListTitleHeader[SESS_LANGUAGE][9]+" "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
                    %>
                  </td>
                </tr>
                <%
                Location location = new Location();
                if(srcSaleReport.getLocationId()==0){
                    location.setName(""+textListTitleHeader[SESS_LANGUAGE][8]+" "+textListTitleHeader[SESS_LANGUAGE][3]) ;
                } else {
                    location = PstLocation.fetchExc(srcSaleReport.getLocationId());
                }
                %>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][3]).toUpperCase()%></b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">:
                    <%=location.getName()%>                  
                    <%
                    listHeaderPdf.add(6, (textListTitleHeader[SESS_LANGUAGE][3]).toUpperCase());
                    listHeaderPdf.add(7, location.getName());
                    %>
                  </td>
                </tr>
                <%
                String nameCat = "Semua Category";    
                if(srcSaleReport.getCategoryId()!=0){
                    Category cat = PstCategory.fetchExc(srcSaleReport.getCategoryId());
                    nameCat = cat.getName();
                }
                %>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b>Category</b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">:
                    <%=nameCat%>                  
                    <%
                    listHeaderPdf.add(6, (textListTitleHeader[SESS_LANGUAGE][3]).toUpperCase());
                    listHeaderPdf.add(7, nameCat);
                    %>
                  </td>
                </tr>
                <%
                String supplier = "Semua Supplier";    
                if(srcSaleReport.getSupplierId()!=0){
                    Contact cat = PstContact.fetchExc(srcSaleReport.getSupplierId());
                    supplier = cat.getCompName() ;
                }
                %>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b>Supplier</b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">:
                    <%=supplier%>                  
                    <%
                    listHeaderPdf.add(6, (textListTitleHeader[SESS_LANGUAGE][3]).toUpperCase());
                    listHeaderPdf.add(7, supplier);
                    %>
                  </td>
                </tr>
                
                
                <%
                try{
                    listPdf.add(listHeaderPdf);
                    listPdf.add(listBodyPdf);
                    session.putValue(SaleReportDocument.SALE_REPORT_DOC_PDF,listPdf);
                }catch(Exception e){
                    e.printStackTrace();
                }
                %>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%=str%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="8" align="left" colspan="3" class="command"> <span class="command"> 
                    </span> </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                    
                    </table></td>
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
