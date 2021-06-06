<%-- 
    Document   : reportsale_list_detail_spesial_request
    Created on : Aug 7, 2015, 2:53:07 PM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@ page import="com.dimata.pos.entity.billing.PstBillMain,
                 com.dimata.common.entity.location.PstLocation,
                 com.dimata.common.entity.location.Location,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.common.entity.contact.ContactList,
                 com.dimata.posbo.entity.search.SrcSaleReport,
                 com.dimata.posbo.form.search.FrmSrcSaleReport,
                 com.dimata.posbo.report.sale.SaleReportDocument,
                 com.dimata.posbo.report.sale.SaleReportItem,
                 com.dimata.common.entity.contact.PstContactList,
                 com.dimata.posbo.entity.masterdata.*,
                 com.dimata.gui.jsp.ControlList,
                 com.dimata.qdep.form.FRMHandler,
                 com.dimata.qdep.form.FRMMessage,
                 com.dimata.qdep.form.FRMQueryString,
                 com.dimata.gui.jsp.ControlLine,
                 com.dimata.util.Command"%>
<%@ page import="com.dimata.posbo.session.masterdata.SessUploadCatalogPhoto" %>
<%@ page language = "java" %>
<!-- package java -->

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_SALES, AppObjInfo.G2_REPORT, AppObjInfo.OBJ_SALES_DETAIL); %>
<%@ include file = "../../../main/checkuser.jsp" %>



<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] = {
    {"NO", "KODE", "NAMA BARANG", "HRG BELI", "HRG JUAL", "JUMLAH", "STN", "TOTAL BELI", "TOTAL JUAL", "LABA","FOTO","ACTION"},
    {"NO", "CODE", "GOODS NAME", "COST", "PRICE", "QTY", "STN", "TOTAL COST", "TOTAL SELL", "MARGIN","PHOTO","ACTION"}
};

public static final String textListTitleHeader[][] = {
    {"LAPORAN DETAIL TRANSAKSI ", "LAPORAN DETAIL TRANSAKSI ", "Tidak ada data transaksi", "LOKASI", "SHIFT", 
             "Laporan", "Cetak Laporan Transaksi", "TIPE", "Urut Turun", "Urut Naik", " s/d ","Periode","Lokasi","Semua","Kategori"},
    {"DETAIL TRANSACTION REPORT OF ", "DETAIL TRANSACTION REPORT OF ", "No transaction data available", "LOCATION", "SHIFT", 
             "Report", "Print Transaction Report ", "TYPE", "Descending", "Ascending", " to ","Period","Location","All","Category"}
};

public Vector drawList(int language, Vector objectClass, SrcSaleReport srcSaleReport, int viewImage,String approot) {
    String result = "";
    Vector listAll = new Vector(1, 1);
    Vector listTableHeaderPdf = new Vector(1, 1);
    Vector listContentPdf = new Vector(1, 1);
    Vector listTableFooterPdf = new Vector(1, 1);
    Vector listBodyPdf = new Vector(1, 1);
    double grandTotalQty = 0;
    double grandTotalSold = 0;
    double grandTotalCost = 0;
    
    //String strHrgBeli = "";
    //String strHrgJual = "";
    //String strTotalBeli = "";
    //String strTotalJual = "";
    //String strMargin = "";
    
    if (objectClass != null && objectClass.size() > 0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("listgen");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("1");
        ctrlist.setHeaderStyle("listgentitle");
        
        ctrlist.dataFormat(textListMaterialHeader[language][0], "5%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][0]);
        String groupTitle = "";
        int groupMeth = srcSaleReport.getGroupBy();
        groupTitle = SrcSaleReport.groupMethod[language][groupMeth];
        
        if (viewImage == 1) {
            ctrlist.dataFormat(textListMaterialHeader[language][10], "20%", "0", "0", "center", "bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][10]);
        }
        
        ctrlist.dataFormat(textListMaterialHeader[language][1] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_ITEM+ "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_ITEM + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "14%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][1]);
        
        ctrlist.dataFormat(textListMaterialHeader[language][2] + "<a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">  </a>", "35%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][2]);
        ctrlist.dataFormat(textListMaterialHeader[language][5] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "13%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][5]);
        ctrlist.dataFormat(textListMaterialHeader[language][8] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "13%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][8]);
        
        // UNTUK TOTAL HPP
        ctrlist.dataFormat(textListMaterialHeader[language][7] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][7] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][7] + "\">   </a>", "13%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][7]);
        
        
        ctrlist.addHeader(textListMaterialHeader[language][11]);
        listTableHeaderPdf.add(textListMaterialHeader[language][11]);
        
        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        //ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        
        for (int i = 0; i < objectClass.size(); i++) {
            Vector rowPdf = new Vector(1, 1);
            Vector rowx = new Vector();
            SaleReportItem saleItem = (SaleReportItem) objectClass.get(i);
            String linkParameter = "";
            
            rowx.add("<div align=\"center\">" + (i + 1) + "</div>");
            rowPdf.add("" + (i + 1));
            
            // untuk menampilkan gambar
            if (viewImage == 1) {
                SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(saleItem.getItemCode());
                if (pictPath.length() > 0) {
                    rowx.add("<div align=\"center\"><img heigth = \"110\" width = \"110\" src=\"" + approot + "/" + pictPath + "\"></div>");
                } else {
                    rowx.add("");
                }
            }
            
            rowx.add("<div align=\"center\">" + saleItem.getItemCode() + "</div>");
            rowPdf.add("" + saleItem.getItemCode());
            
            switch (groupMeth) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    rowx.add("<div align=\"left\">" + saleItem.getCategoryName() + "</div>");
                    rowPdf.add(saleItem.getCategoryName());
                    linkParameter = String.valueOf(saleItem.getCategoryId());
                    break;
                case SrcSaleReport.GROUP_BY_LOCATION:
                    rowx.add("<div align=\"left\">" + saleItem.getLocationName() + "</div>");
                    rowPdf.add(saleItem.getLocationName());
                    linkParameter = String.valueOf(saleItem.getLocationId());
                    break;
                case SrcSaleReport.GROUP_BY_SALES:
                    rowx.add("<div align=\"left\">" + saleItem.getSalesPersonName() + "</div>");
                    rowPdf.add(saleItem.getSalesPersonName());
                    linkParameter = String.valueOf(saleItem.getSalesPersonId());
                    break;
                case SrcSaleReport.GROUP_BY_SHIFT:
                    rowx.add("<div align=\"left\">" + saleItem.getShiftName() + "</div>");
                    rowPdf.add(saleItem.getShiftName());
                    linkParameter = String.valueOf(saleItem.getShiftId());
                    break;
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    rowx.add("<div align=\"left\">" + saleItem.getSupplierName() + "</div>");
                    rowPdf.add(saleItem.getSupplierName());
                    linkParameter = String.valueOf(saleItem.getSupplierId());
                    break;
                default:
                    rowx.add("<div align=\"left\">" + saleItem.getItemName() + "</div>");
                    rowPdf.add(saleItem.getItemName());
                    linkParameter = String.valueOf(saleItem.getItemId());
                    break;
            }
            
            //grandTotalQty = grandTotalQty + saleItem.getTotalQtyByStock();
            grandTotalQty = grandTotalQty + saleItem.getTotalQty();
            grandTotalSold = grandTotalSold + saleItem.getTotalSold();
            grandTotalCost = grandTotalCost + saleItem.getTotalCost();
                    
            //rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock()) + "</div>");
            //rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock()));
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()) + "</div>");
            rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()));
            //rowx.add("<div align=\"right\">"+strHrgBeli+"</div>");
            //rowx.add("<div align=\"right\">"+strHrgJual+"</div>");
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()) + "</div>");
            rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));
            
            rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()) + "</div>");
            rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()));
            
            rowx.add("<a href=\"../../../master/material/material_main.jsp?hidden_material_id=0&command=2&namespesialrequest="+saleItem.getItemName()+"\"  target=\"_blank\" > Move to Masterdata </a>");
            rowPdf.add("");

            listContentPdf.add(rowPdf); 
            
            lstData.add(rowx);
            lstLinkData.add(linkParameter);
        }
        
        Vector rowx = new Vector();
        if (viewImage == 1) {
            rowx.add("");
            //listTableFooterPdf.add("");
        }
        
        rowx.add("");
        listTableFooterPdf.add("");
        
        rowx.add("<div align=\"right\"></div>");
        listTableFooterPdf.add("");
        
        rowx.add("<div align=\"right\"><b>GRAND TOTAL</b></div>");
        listTableFooterPdf.add("GRAND TOTAL");
        
        rowx.add("<div align=\"right\"><table witdh=\"100%\"><tr><td><div align=\"right\"></div></td><td><div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalQty) + "</b></div></td></tr></table></div>");
        listTableFooterPdf.add("" + FRMHandler.userFormatStringDecimal(grandTotalQty));
        
        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalSold) + "</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalSold));
        
        // total cost
        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalCost) + "</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalCost));
        
        rowx.add("");
        listTableFooterPdf.add("");
        //listPdf.add(vtPdf);
        
        lstData.add(rowx);
        lstLinkData.add(String.valueOf(0));
        
        result = ctrlist.draw(); 
    } else {
        result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListTitleHeader[language][2] + "</div>";
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
int viewImage = FRMQueryString.requestInt(request, "view_image");
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
int sortCommand = Command.FIRST;//9999;

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
//SrcSaleReport srcSaleReportDetail = new SrcSaleReport();
//SaleReportDocument saleReportDocument= new SaleReportDocument();
//FrmSrcSaleReport frmSrcSaleReportDetail = new FrmSrcSaleReport(request, srcSaleReportDetail);
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);

/**
 * handle current search data session
 */
//if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST ){
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==sortCommand) {
    try {
        //srcSaleReportDetail = (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL);
        //if (srcSaleReportDetail == null) srcSaleReportDetail = new SrcSaleReport();
        //System.out.println("1");
        srcSaleReport= (SrcSaleReport)session.getValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL);
        if (srcSaleReport== null) {
            //System.out.println("2");
            srcSaleReport= new SrcSaleReport();
        }
    }catch(Exception e){
        srcSaleReport = new SrcSaleReport();
    }
    //session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
}else{
    frmSrcSaleReport.requestEntity(srcSaleReport);
    //frmSrcSaleReportDetail.requestEntity(srcSaleReportDetail);
    srcSaleReport.setQueryType(srcSaleReport.getGroupBy());
}

srcSaleReport.setGroupBy(SrcSaleReport.GROUP_BY_ITEM);
srcSaleReport.setSortBy(sortMethod);
srcSaleReport.setDescSort(descSort);
session.putValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL, srcSaleReport);
//srcSaleReport.setSortBy(SrcSaleReport.SORT_BY_ITEM);

Vector records = SaleReportDocument.getListSpesialRequest(srcSaleReport,0,100000,SaleReportDocument.LOG_MODE_CONSOLE);
vectSize = records.size();

// ini dipakai untuk
// pembuatan invoice internal
try{
    session.putValue("list_for_internal_invoice",records);
}catch(Exception e){}

Vector listHeaderPdf = new Vector(1,1);
Vector listBodyPdf = new Vector(1,1);
Vector listPdf = new Vector(1,1);
Vector list = drawList(SESS_LANGUAGE,records,srcSaleReport,srcSaleReport.getImageView(),approot);
String str = "";
Vector compTelpFax = (Vector)companyAddress.get(2);

// LIST FOR MAIN
listHeaderPdf.add(0, companyAddress.get(0));
listHeaderPdf.add(1, companyAddress.get(1));
listHeaderPdf.add(2, ((String)compTelpFax.get(0)+" "+(String)compTelpFax.get(1)));

//listPdf.add(Formater.formatDate(srcReportSale.getDateFrom(), "dd-MM-yyyy"));
//listPdf.add(lokasi.getName());
//listPdf.add(shf.getName());
//listPdf.add(PstBillMain.transType[SESS_LANGUAGE][srcReportSale.getDocType()]);

try{
    str = (String)list.get(0);
    listBodyPdf= (Vector)list.get(1);    
}catch(Exception e) {
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
//session.putValue(SaleReportDocument.SALE_REPORT_DOC, srcSaleReport);
//session.putValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL, srcSaleReportDetail);

/** get location list */
Location location = new Location();
if (srcSaleReport.getLocationId() != 0) {
    try	{
        location = PstLocation.fetchExc(srcSaleReport.getLocationId());
    } catch(Exception e) {
    }
} else {
    location.setName(textListTitleHeader[SESS_LANGUAGE][13]+" "+textListTitleHeader[SESS_LANGUAGE][12]);
}
%>
<!-- End of Jsp Block -->

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--
function cmdSort(method,desc){
	document.frm_reportsale.sort_method.value=method;
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.sort_desc.value=desc;
	document.frm_reportsale.command.value="<%=sortCommand%>";
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdEdit(oid){
	document.frm_reportsale.hidden_group_id.value=oid;
	document.frm_reportsale.command.value="<%=Command.LIST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frmsrcreportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frmsrcreportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="src_reportsale_spesial_request.jsp";
	document.frm_reportsale.submit();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.sale.SaleReportPdfDetail?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function cmdInternalInvoice(){
    document.frm_reportsale.command.value="<%=Command.LOAD%>";
    document.frm_reportsale.action="../salemain.jsp";
	document.frm_reportsale.submit();
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

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
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

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg')">    
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
            <form name="frm_reportsale" method="post" action="">
             <input type="hidden" name="sale_type" value="<%=PstBillMain.TYPE_IMVOICE_CLAIM%>">
             <input type="hidden" name="location_name" value="<%=srcSaleReport.getLocationId()%>">
              <input type="hidden" name="command" value="<%=iCommand%>">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="sort_method" value="<%=srcSaleReport.getSortBy()%>">
              <input type="hidden" name="sort_desc" value="<%=srcSaleReport.getDescSort()%>">
              <input type="hidden" name="global_group" value="<%=global_group%>">
              <input type="hidden" name="approval_command" value="">
              <input type="hidden" name="hidden_group_id" value="0">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="3" align="center"><h4>
                    <%=textListTitleHeader[SESS_LANGUAGE][1]%> <%=new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()%> <%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()%></h4>
                    <% listHeaderPdf.add(3,textListTitleHeader[SESS_LANGUAGE][0]+" "+new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()+" "+SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()); %>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][11]).toUpperCase()%></b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">:
                    <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> <%=textListTitleHeader[SESS_LANGUAGE][10]%> 
                    <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%>
                    <%
                    listHeaderPdf.add(4, (textListTitleHeader[SESS_LANGUAGE][11]).toUpperCase());
                    listHeaderPdf.add(5, Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textListTitleHeader[SESS_LANGUAGE][10]+" "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
                    %>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="14" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase()%></b>
                  </td>
                  <td height="14" valign="middle" colspan="2" width="90%">: <%=location.getName()%>
                    <%
                    listHeaderPdf.add(6, (textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase());
                    listHeaderPdf.add(7, location.getName());
                    %>
                  </td>
                </tr>
                <%
                    switch(global_group){
                        case SrcSaleReport.GROUP_BY_CATEGORY:
                            Category category = new Category();
                            if (srcSaleReport.getCategoryId() != 0) {
                                try	{
                                    category = PstCategory.fetchExc(srcSaleReport.getCategoryId());
                                } catch(Exception e) {
                                }
                            } else {
                                category.setName(textListTitleHeader[SESS_LANGUAGE][13]+" "+textListTitleHeader[SESS_LANGUAGE][14]);
                            }

                            //Category category = PstCategory.fetchExc(srcSaleReport.getCategoryId());
                            %>
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="1" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_CATEGORY]%></b>
                            </td>
                            <td height="14" valign="middle" colspan="2" width="90%">: <%=category.getName()%>
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_CATEGORY]);
                            listHeaderPdf.add(9, category.getName());
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
//                            Sales sales = PstSales.fetchExc(srcSaleReport.getSalesPersonId());
                            AppUser sales = PstAppUser.fetch(srcSaleReport.getSalesPersonId());

                            %>
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="1" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SALES]%></b>
                            </td>
                            <td height="14" valign="middle" colspan="2" width="90%">: <%=sales.getFullName()%>
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SALES]);
                            listHeaderPdf.add(9, sales.getFullName());
                            break;
                        case SrcSaleReport.GROUP_BY_SHIFT:
                            Shift shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                            %>
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="1" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SHIFT]%></b>
                            </td>
                            <td height="14" valign="middle" colspan="2" width="90%">: <%=shift.getName()%>
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SHIFT]);
                            listHeaderPdf.add(9, shift.getName());
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            ContactList contact = PstContactList.fetchExc(srcSaleReport.getSupplierId());
                            %>
                            <tr align="left" valign="top">
                            <td height="14" valign="middle" colspan="1" class="command"><b>SUPPLIER</b>
                            </td>
                            <td height="14" valign="middle" colspan="2" width="90%">: <%=contact.getCompName()%>
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, "SUPPLIER");
                            listHeaderPdf.add(9, contact.getCompName());
                            break;
                        default:
                           break;
                        }
                %>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%=str%></td><%-- tampil --%>
                </tr>
                <tr align="left" valign="top">
                  <td height="8" align="left" colspan="3" class="command"> <span class="command">
                    </span> </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr>
                        <td width="75%">
                          <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                              <td colspan="2">&nbsp;</td>
                            </tr>
                            <tr>
                              <td class="command" nowrap width="95%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListTitleHeader[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>  
                          </table>
                        </td>
                        <td width="25%" valign="bottom"> <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <%
                              if(vectSize>0) {
                            %>
                            <tr> 
                              <td width="95%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm()" class="command" >
                                <i class="fa fa-print">&nbsp;<%=textListTitleHeader[SESS_LANGUAGE][6]+" "+SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]%></i></a>
                              </td>
                            <%
                              }
                              try {
                                  listPdf.add(listHeaderPdf);
                                  listPdf.add(listBodyPdf);
                                  session.putValue(SaleReportDocument.SALE_REPORT_DOC_DETAIL_PDF,listPdf);
                              } catch(Exception e){
                                  e.printStackTrace();
                              }
                            %>
                            </tr>
                          </table></td>
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

