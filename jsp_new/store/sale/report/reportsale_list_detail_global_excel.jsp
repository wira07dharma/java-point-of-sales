<%-- 
    Document   : reportsale_list_detail_global_html
    Created on : Aug 8, 2016, 10:50:31 AM
    Author     : dimata005
--%>
<%@page import="com.dimata.posbo.entity.admin.PstAppUser"%>
<%@page import="com.dimata.hanoman.entity.masterdata.PstMasterType"%>
<%@page import="com.dimata.hanoman.entity.masterdata.MasterType"%>
<%@page import="java.text.DateFormat"%>
<%@page import="com.dimata.posbo.session.warehouse.SessMatCostingStokFisik"%>
<%@page import="java.text.SimpleDateFormat"%>
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
    {"NO", "SKU", "NAMA BARANG", "HRG BELI", "HRG JUAL", "JUMLAH", "STN", "TOTAL BELI", "TOTAL JUAL", "LABA","FOTO","SALES","SHIFT","BARCODE","QTY STOK"},
    {"NO", "CODE", "GOODS NAME", "COST", "PRICE", "QTY", "STN", "TOTAL COST", "TOTAL SELL", "MARGIN","PHOTO","SALES","SHIFT","BARCODE","STOCK QTY"}
};

public static final String textListTitleHeader[][] = {
    {"LAPORAN DETAIL TRANSAKSI ", "LAPORAN DETAIL TRANSAKSI ", "Tidak ada data transaksi", "LOKASI", "SHIFT", 
             "Laporan", "Cetak Laporan Transaksi", "TIPE", "Urut Turun", "Urut Naik", " s/d ","Periode","Lokasi","Semua","Kategori"},
    {"DETAIL TRANSACTION REPORT OF ", "DETAIL TRANSACTION REPORT OF ", "No transaction data available", "LOCATION", "SHIFT", 
             "Report", "Print Transaction Report ", "TYPE", "Descending", "Ascending", " to ","Period","Location","All","Category"}
};

%>

<%!
public Vector drawList(int language, Vector objectClass, SrcSaleReport srcSaleReport, int viewImage,String approot,long oidSales) {
    String result = "";
    Vector listAll = new Vector(1, 1);
    Vector listTableHeaderPdf = new Vector(1, 1);
    Vector listContentPdf = new Vector(1, 1);
    Vector listTableFooterPdf = new Vector(1, 1);
    Vector listBodyPdf = new Vector(1, 1);
    double grandTotalQty = 0;
    double grandTotalSold = 0;
    double grandTotalCost = 0;
    double grandSisaStock=0;
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
        ctrlist.setCellSpacing("0");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setBorder(1);
        ctrlist.dataFormat(textListMaterialHeader[language][0], "5%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][0]);
        String groupTitle = "";
        int groupMeth = srcSaleReport.getGroupBy();
        groupTitle = SrcSaleReport.groupMethod[language][groupMeth];
        
        if (viewImage == 1) {
            ctrlist.dataFormat(textListMaterialHeader[language][10], "20%", "0", "0", "center", "bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][10]);
        }
        
        ctrlist.dataFormat(textListMaterialHeader[language][1] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_ITEM+ "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_ITEM + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "5%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][1]);

        //barcode
        ctrlist.dataFormat("BARCODE", "5%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add("BARCODE");
        
        //nama barang
        ctrlist.dataFormat(textListMaterialHeader[language][2] + "<a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">  </a>", "35%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][2]);

        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
            ctrlist.dataFormat(textListMaterialHeader[language][12] + "<a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + srcSaleReport.getGroupBy() + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">  </a>", "25%", "0", "0", "center", "bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][12]);
        }
        
        //jumlah qty
        ctrlist.dataFormat(textListMaterialHeader[language][5] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "8%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][5]);
        
        if(srcSaleReport.getViewStock()==1){
            ctrlist.dataFormat(textListMaterialHeader[language][14] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_QTY + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "8%", "0", "0", "center", "bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][14]);
        }

        ctrlist.dataFormat(textListMaterialHeader[language][8] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][9] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][8] + "\">   </a>", "13%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][8]);
        
        // UNTUK TOTAL HPP
        ctrlist.dataFormat(textListMaterialHeader[language][7] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][7] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_TOTAL_SALE + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][7] + "\">   </a>", "13%", "0", "0", "center", "bottom");
        listTableHeaderPdf.add(textListMaterialHeader[language][7]);
        
        //untuk SALES
        
        if (groupMeth==SrcSaleReport.GROUP_BY_SALES){
            ctrlist.dataFormat(textListMaterialHeader[language][11] + "<a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_SALES_PERSON + "','" + SrcSaleReport.SORT_ASC + "')\"> <img src=\"../../../images/arrow_up_white.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][11] + "\"> </a><a href=\"javascript:cmdSort('" + SrcSaleReport.SORT_BY_SALES_PERSON + "','" + SrcSaleReport.SORT_DESC + "')\"> <img src=\"../../../images/arrow_down_black.gif\" border=\"0\" width=\"10\" height=\"10\" alt=\"" + textListTitleHeader[language][11] + "\">   </a>", "13%", "0", "0", "center", "bottom");
            listTableHeaderPdf.add(textListMaterialHeader[language][11]);
        }
        
         //buatkan laba dan margin
        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
            ctrlist.dataFormat("LABA KOTOR ","15%","0","0","center","bottom");
            listTableHeaderPdf.add("LABA KOTOR ");
        }
        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
            ctrlist.dataFormat("MARGIN ","15%","0","0","center","bottom");
            listTableHeaderPdf.add(" MARGIN ");
        }

        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        //ctrlist.setLinkRow(1);
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        long oldheader=0;
        boolean header = false;
        String namaHeader="";
        int loop=0;
        double subjumlahqty=0;
        double subQtyStock=0;
        double subjumlahjual=0;
        double subjumlahbeli=0;
        double subtotalbruto=0;
        double totalbruto=0;
        for (int i = 0; i < objectClass.size(); i++) {
            loop=loop+1;
            Vector rowPdf = new Vector(1, 1);
            Vector rowx = new Vector();
            SaleReportItem saleItem = (SaleReportItem) objectClass.get(i);
            String linkParameter = "";
            
            //buatkan header nya
            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY){
                if(oldheader!=saleItem.getCategoryId()){
                    header=true;
                    namaHeader = saleItem.getCategoryName();
                    oldheader = saleItem.getCategoryId();
                }else{
                    header=false;
                }
            }

            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SALES){
                if(oldheader!=saleItem.getSalesPersonId()){
                    header=true;
                    namaHeader = saleItem.getSalesPersonName();
                    oldheader = saleItem.getSalesPersonId();
                }else{
                    header=false;
                }
            }

            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                if(oldheader!=saleItem.getSupplierId()){
                    header=true;
                    namaHeader = saleItem.getSupplierName();
                    oldheader = saleItem.getSupplierId();
                }else{
                    header=false;
                }
            }

            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
                if(oldheader!=saleItem.getShiftId()){
                    header=true;
                    namaHeader = saleItem.getShiftName();
                    oldheader = saleItem.getShiftId();
                }else{
                    header=false;
                }
            }
            double sisaStock=0;
            if(srcSaleReport.getViewStock()==1){
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
                }
            }

            if(header){
                //tambahkan subtotal
                if(loop!=1){
                    rowx = new Vector();
                    rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");
                    // untuk menampilkan gambar
                    if (viewImage == 1) {
                        SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                        String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(saleItem.getItemCode());
                        if (pictPath.length() > 0) {
                            rowx.add("<div align=\"center\"></div>");
                        } else {
                            rowx.add("");
                        }
                    }

                    rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");

                    //barcode
                    rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");
                    
                    switch (groupMeth) {
                        case SrcSaleReport.GROUP_BY_CATEGORY:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            linkParameter = String.valueOf(saleItem.getCategoryId());
                            break;
                        case SrcSaleReport.GROUP_BY_LOCATION:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                           // linkParameter = String.valueOf(saleItem.getLocationId());
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
                            if (oidSales!=0){
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getSalesPersonId());
                                break;
                            }else{
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getItemId());
                            }

                        case SrcSaleReport.GROUP_BY_SHIFT:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            //linkParameter = String.valueOf(saleItem.getShiftId());
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            //linkParameter = String.valueOf(saleItem.getSupplierId());
                            break;
                        default:
                            rowx.add("<div align=\"right\"><b>SUB TOTAL</b></div>");
                            rowPdf.add(""+namaHeader);

                            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getShiftId());
                            }

                            break;
                    }
                    rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahqty) + "</b></div>");
                    if(srcSaleReport.getViewStock()==1){
                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subQtyStock) + "</b></div>");
                    }
                    rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahjual) + "</b></div>");
                    rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahbeli) + "</b></div>");
                    if (groupMeth==SrcSaleReport.GROUP_BY_SALES){
                        if (oidSales==0){
                            rowx.add("<div align=\"right\"></div>");
                            rowPdf.add("");
                        }
                    }

                    if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                           rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subtotalbruto) + "</b></div>");
                            rowPdf.add("");
                    }

                    if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                        rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(((subjumlahjual-subjumlahbeli)/subjumlahbeli)*100)+"%</b></div>");
                            rowPdf.add("");
                    }
                    
                    listContentPdf.add(rowPdf); 
                    lstData.add(rowx);
                    lstLinkData.add(linkParameter);
                }
                
                subjumlahqty=0;
                subjumlahqty = subjumlahqty+saleItem.getTotalQty();

                subjumlahjual=0;
                subjumlahjual =subjumlahjual+saleItem.getTotalSold();

                subjumlahbeli=0;
                subjumlahbeli = subjumlahbeli+saleItem.getTotalCost();
                
                subtotalbruto=0;
                subtotalbruto = subtotalbruto + (saleItem.getTotalSold()-saleItem.getTotalCost());
                
                subQtyStock=0;
                subQtyStock=subQtyStock+sisaStock;

                rowx = new Vector();
                rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");
                    // untuk menampilkan gambar
                    if (viewImage == 1) {
                        SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                        String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(saleItem.getItemCode());
                        if (pictPath.length() > 0) {
                            rowx.add("<div align=\"center\"></div>");
                        } else {
                            rowx.add("");
                        }
                    }

                    rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");
                    
                    //barcode
                    rowx.add("<div align=\"center\"></div>");
                    rowPdf.add("");

                    switch (groupMeth) {
                        case SrcSaleReport.GROUP_BY_CATEGORY:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            linkParameter = String.valueOf(saleItem.getCategoryId());
                            break;
                        case SrcSaleReport.GROUP_BY_LOCATION:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                           // linkParameter = String.valueOf(saleItem.getLocationId());
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
                            if (oidSales!=0){
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getSalesPersonId());
                                break;
                            }else{
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getItemId());
                            }

                        case SrcSaleReport.GROUP_BY_SHIFT:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            //linkParameter = String.valueOf(saleItem.getShiftId());
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            rowx.add("<div align=\"left\"></div>");
                            rowPdf.add("");
                            //linkParameter = String.valueOf(saleItem.getSupplierId());
                            break;
                        default:
                            rowx.add("<div align=\"left\"><b>"+namaHeader+"</b></div>");
                            rowPdf.add(""+namaHeader);

                            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
                                rowx.add("<div align=\"left\"></div>");
                                rowPdf.add("");
                                //linkParameter = String.valueOf(saleItem.getShiftId());
                            }

                            break;
                    }
                    rowx.add("<div align=\"right\"></div>");
                    if(srcSaleReport.getViewStock()==1){
                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(0) + "</b></div>");
                    }
                    rowx.add("<div align=\"right\"></div>");
                    rowx.add("<div align=\"right\"></div>");
                    if (groupMeth==SrcSaleReport.GROUP_BY_SALES){
                        if (oidSales==0){
                            rowx.add("<div align=\"right\"></div>");
                            rowPdf.add("");
                        }
                    }

                    if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                           rowx.add("<div align=\"right\"></div>");
                           rowPdf.add("");
                    }

                    if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                        rowx.add("<div align=\"right\"></div>");
                            rowPdf.add("");
                    }

                    listContentPdf.add(rowPdf); 
                    lstData.add(rowx);
                    lstLinkData.add(linkParameter);

                }else{

                    subjumlahqty = subjumlahqty+saleItem.getTotalQty();
                    subjumlahjual =subjumlahjual+saleItem.getTotalSold();
                    subjumlahbeli = subjumlahbeli+saleItem.getTotalCost();
                    subtotalbruto = subtotalbruto + (saleItem.getTotalSold()-saleItem.getTotalCost());
                    subQtyStock=subQtyStock+sisaStock;
                }
            
                rowx = new Vector();
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

                rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"' >" + saleItem.getItemCode() + "</div>");
                rowPdf.add("" + saleItem.getItemCode());

                rowx.add("<div align=\"left\" style='mso-number-format:\"\\@\"'>" + saleItem.getItemBarcode() + "</div>");
                rowPdf.add("" + saleItem.getItemBarcode());

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
                        if (oidSales!=0){
                            rowx.add("<div align=\"left\">" + saleItem.getSalesPersonName() + "</div>");
                            rowPdf.add(saleItem.getSalesPersonName());
                            linkParameter = String.valueOf(saleItem.getSalesPersonId());
                            break;
                        }else{
                            rowx.add("<div align=\"left\">" + saleItem.getItemName() + "</div>");
                            rowPdf.add(saleItem.getItemName());
                            linkParameter = String.valueOf(saleItem.getItemId());
                        }

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

                        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
                            rowx.add("<div align=\"left\">" + saleItem.getShiftName() + "</div>");
                            rowPdf.add(saleItem.getShiftName());
                            linkParameter = String.valueOf(saleItem.getShiftId());
                        }

                        break;
                }

                //grandTotalQty = grandTotalQty + saleItem.getTotalQtyByStock();
                grandTotalQty = grandTotalQty + saleItem.getTotalQty();
                grandTotalSold = grandTotalSold + saleItem.getTotalSold();
                grandTotalCost = grandTotalCost + saleItem.getTotalCost();
                grandSisaStock=grandSisaStock+sisaStock;
                //rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock()) + "</div>");
                //rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQtyByStock()));
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()) + "</div>");
                rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()));
                
                if(srcSaleReport.getViewStock()==1){
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(sisaStock) + "</div>");
                    rowPdf.add(FRMHandler.userFormatStringDecimal(sisaStock));
                }
                
                //rowx.add("<div align=\"right\">"+strHrgBeli+"</div>");
                //rowx.add("<div align=\"right\">"+strHrgJual+"</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()) + "</div>");
                rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()));

                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()) + "</div>");
                rowPdf.add(FRMHandler.userFormatStringDecimal(saleItem.getTotalCost()));


                if (groupMeth==SrcSaleReport.GROUP_BY_SALES){
                    if (oidSales==0){
                        rowx.add("<div align=\"right\">" + saleItem.getSalesPersonName() + "</div>");
                        rowPdf.add(saleItem.getSalesPersonName());
                    }
                }

                if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                    totalbruto = totalbruto+(saleItem.getTotalSold()-saleItem.getTotalCost());
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(saleItem.getTotalSold()-saleItem.getTotalCost())+"</div>");
                    rowPdf.add("");
                }

                if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(((saleItem.getTotalSold()-saleItem.getTotalCost())/saleItem.getTotalCost())*100)+"%</b></div>");
                    rowPdf.add("");
                }

            listContentPdf.add(rowPdf); 
            
            lstData.add(rowx);
            lstLinkData.add(linkParameter);
        }
        
        Vector rowPdf = new Vector(1, 1);
        Vector rowx = new Vector();
        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SALES){
            rowPdf = new Vector(1, 1);
            rowx = new Vector();
            rowx.add("<div align=\"center\"></div>");
            rowPdf.add("");
            // untuk menampilkan gambar
            if (viewImage == 1) {
                rowx.add("");
            }

            rowx.add("<div align=\"center\"></div>");
            rowPdf.add("");
            //barcode
            rowx.add("<div align=\"center\"></div>");
            rowPdf.add("");

            switch (groupMeth) {
                case SrcSaleReport.GROUP_BY_CATEGORY:
                    rowx.add("<div align=\"left\"></div>");
                    rowPdf.add("");
                    break;
                case SrcSaleReport.GROUP_BY_LOCATION:
                    rowx.add("<div align=\"left\"></div>");
                    rowPdf.add("");
                    break;
                case SrcSaleReport.GROUP_BY_SALES:
                    if (oidSales!=0){
                        rowx.add("<div align=\"left\"></div>");
                        rowPdf.add("");
                        break;
                    }else{
                        rowx.add("<div align=\"left\"></div>");
                        rowPdf.add("");
                    }

                case SrcSaleReport.GROUP_BY_SHIFT:
                    rowx.add("<div align=\"left\"></div>");
                    rowPdf.add("");
                    break;
                case SrcSaleReport.GROUP_BY_SUPPLIER:
                    rowx.add("<div align=\"left\"></div>");
                    rowPdf.add("");
                    break;
                default:
                    rowx.add("<div align=\"right\"><b>SUB TOTAL</b></div>");
                    rowPdf.add(""+namaHeader);

                    if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
                        rowx.add("<div align=\"left\"></div>");
                        rowPdf.add("");
                    }

                    break;
            }
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahqty) + "</b></div>");
            if(srcSaleReport.getViewStock()==1){
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandSisaStock) + "</b></div>");
            }
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahjual) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahbeli) + "</b></div>");
            if (groupMeth==SrcSaleReport.GROUP_BY_SALES){
                if (oidSales==0){
                    rowx.add("<div align=\"right\"></div>");
                    rowPdf.add("");
                }
            }

            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                    rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalbruto)+"</b></div>");
                    rowPdf.add("");
            }

            if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(((subjumlahjual-subjumlahbeli)/subjumlahbeli)*100)+"%</b></div>");
                rowPdf.add("");
            }

            listContentPdf.add(rowPdf); 
            lstData.add(rowx);
            lstLinkData.add("");
        }

        rowx = new Vector();
        if (viewImage == 1) {
            rowx.add("");
        }
        
        rowx.add("");
        listTableFooterPdf.add("");
        
        rowx.add("<div align=\"right\"></div>");
        listTableFooterPdf.add("");

        //barcode
        rowx.add("<div align=\"right\"></div>");
        listTableFooterPdf.add("");
        
        rowx.add("<div align=\"right\"><b>GRAND TOTAL</b></div>");
        listTableFooterPdf.add("GRAND TOTAL");

        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SHIFT){
            rowx.add("<div align=\"right\"></div>");
            listTableFooterPdf.add("");
        }
        
        rowx.add("<div align=\"right\"><table witdh=\"100%\"><tr><td><div align=\"right\"></div></td><td><div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalQty) + "</b></div></td></tr></table></div>");
        listTableFooterPdf.add("" + FRMHandler.userFormatStringDecimal(grandTotalQty));
        
        if(srcSaleReport.getViewStock()==1){
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandSisaStock) + "</b></div>");
        }

        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalSold) + "</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalSold));
        
        // total cost
        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalCost) + "</b></div>");
        listTableFooterPdf.add(FRMHandler.userFormatStringDecimal(grandTotalCost));

        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
                rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(totalbruto)+"</b></div>");
                listTableFooterPdf.add("");
        }

        if(srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType()==SrcSaleReport.GROUP_BY_SUPPLIER){
            rowx.add("<div align=\"right\"><b>"+FRMHandler.userFormatStringDecimal(((grandTotalSold-grandTotalCost)/grandTotalCost)*100)+"%</b></div>");
            listTableFooterPdf.add("");
        }


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

<%!
    public Vector drawListGreenbowl(int language, Vector objectClass, SrcSaleReport srcSaleReport, int viewImage, String approot, long oidSales) {
        String result = "";
        Vector listAll = new Vector(1, 1);
        double grandTotalQty = 0;
        double grandTotalSold = 0;
        double grandTotalCost = 0;
        double grandSisaStock = 0;

        if (objectClass != null && objectClass.size() > 0) {
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.setCellSpacing("1");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.dataFormat(textListMaterialHeader[language][0], "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("BILL", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("DATE", "", "0", "0", "center", "bottom");
            String groupTitle = "";
            int groupMeth = srcSaleReport.getGroupBy();
            groupTitle = SrcSaleReport.groupMethod[language][groupMeth];
            ctrlist.dataFormat(textListMaterialHeader[language][1], "", "0", "0", "center", "bottom");//sku
            ctrlist.dataFormat("BARCODE", "", "0", "0", "center", "bottom");//barcode
            ctrlist.dataFormat("NAME", "", "0", "0", "center", "bottom");//nama barang
            ctrlist.dataFormat("COLOR", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("SIZE", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("SEASON", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("SOH", "", "0", "0", "center", "bottom");
            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                ctrlist.dataFormat(textListMaterialHeader[language][12], "", "0", "0", "center", "bottom");
            }
            ctrlist.dataFormat("QTY SOLD", "", "0", "0", "center", "bottom");//jumlah qty
            ctrlist.dataFormat("RRP", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("%DISC", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("NET PRICE", "", "0", "0", "center", "bottom");
            if (srcSaleReport.getViewStock() == 1) {
                ctrlist.dataFormat(textListMaterialHeader[language][14], "", "0", "0", "center", "bottom");
            }
            ctrlist.dataFormat("%CONS", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("PRICE CONS", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("TOTAL CONS", "", "0", "0", "center", "bottom");
            ctrlist.dataFormat("END", "", "0", "0", "center", "bottom");

            ctrlist.setLinkRow(-1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            //ctrlist.setLinkRow(1);
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();
            long oldheader = 0;
            boolean header = false;
            String namaHeader = "";
            int loop = 0;
            long lastMasterTypeId = 0;
            
            double subjumlahqty = 0;
            double subQtyStock = 0;
            double subjumlahjual = 0;
            double subjumlahbeli = 0;
            double subTotalRRP = 0;
            double subTotalNetPrice = 0;
            double subTotalPriceCons = 0;
            double subTotalForTotalCons = 0;
            double grandTotalRRP = 0;
            double grandTotalNetPrice = 0;
            double grandTotalPriceCons = 0;
            double grandTotalForTotalCons = 0;
            
            for (int i = 0; i < objectClass.size(); i++) {
                loop = loop + 1;
                Vector rowx = new Vector();
                SaleReportItem saleItem = (SaleReportItem) objectClass.get(i);
                String linkParameter = "";

                Material material = new Material();
                Color color = new Color();
                MasterType masterTypeSize = new MasterType();
                MasterType masterTypeSeason = new MasterType();

                if (saleItem.getItemBarcode().equals("SB03")) {
                    System.out.println("");
                }
                
                //get data color, size, season
                try {
                    material = PstMaterial.fetchExc(saleItem.getItemId());
                    color = PstColor.fetchExc(material.getPosColor());
                    long oidMappingSize = PstMaterialMappingType.getSelectedTypeId(1, material.getOID());
                    long oidMappingSeason = PstMaterialMappingType.getSelectedTypeId(3, material.getOID());
                    masterTypeSize = PstMasterType.fetchExc(oidMappingSize);
                    masterTypeSeason = PstMasterType.fetchExc(oidMappingSeason);
                } catch (Exception e) {

                }

                double rrp = saleItem.getPrice();
                double discPct = saleItem.getDiscount() / (saleItem.getPrice() * saleItem.getTotalQty()) * 100;
                double netPrice = (rrp - saleItem.getDiscount()) * saleItem.getTotalQty();
                if (netPrice < 0) {
					netPrice = 0;
				}
                double cons = material.getProfit();
                double priceCons = netPrice - (netPrice * cons / 100);
                double totalCons = priceCons * saleItem.getTotalQty();
                
                if (saleItem.getSupplierId() > 0) {
                    grandTotalRRP += rrp;
                }
                if (saleItem.getSupplierId() > 0) {
				grandTotalNetPrice += netPrice;
                }
                if (saleItem.getSupplierId() > 0) {
				grandTotalPriceCons += priceCons;
                }
                if (saleItem.getSupplierId() > 0) {
				grandTotalForTotalCons += totalCons;
                }
                if (saleItem.getSupplierId() == -1) {
                    if (saleItem.getTotalQty() > 0) {
                        saleItem.setTotalQty(saleItem.getTotalQty() * -1);
                        rrp *= -1;
                        netPrice *= -1;
                        priceCons *= -1;
                        totalCons *= -1;
                    }
                }

                double sisaStock = 0;
                if (srcSaleReport.getViewStock() == 1) {
                    if (srcSaleReport.getLocationId() != 0) {
                        String endDate = Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd 23:59:59");
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                        Date date = new Date();
                        try {
                            date = format.parse(endDate);
                        } catch (Exception ex) {
                        }
                        sisaStock = SessMatCostingStokFisik.qtyMaterialBasedOnStockCard(saleItem.getItemId(), srcSaleReport.getLocationId(), date, 0);
                    }
                }

                //buatkan header nya
                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY) {
                    if (oldheader != saleItem.getCategoryId()) {
                        header = true;
                        namaHeader = saleItem.getCategoryName();
                        oldheader = saleItem.getCategoryId();
                    } else {
                        header = false;
                    }
                }

                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SALES) {
                    if (oldheader != saleItem.getSalesPersonId()) {
                        header = true;
                        namaHeader = saleItem.getSalesPersonName();
                        oldheader = saleItem.getSalesPersonId();
                    } else {
                        header = false;
                    }
                }

                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    if (oldheader != saleItem.getSupplierId()) {
                        header = true;
                        namaHeader = saleItem.getSupplierName();
                        oldheader = saleItem.getSupplierId();
                    } else {
                        header = false;
                    }
                }

                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                    if (oldheader != saleItem.getShiftId()) {
                        header = true;
                        namaHeader = saleItem.getShiftName();
                        oldheader = saleItem.getShiftId();
                    } else {
                        header = false;
                    }
                }
                
                if (header) {
                    //tambahkan subtotal
                    if (loop != 1) {
                        rowx = new Vector();
                        rowx.add("<div align=\"center\"></div>");
                        // untuk menampilkan gambar
                        if (viewImage == 1) {
                            SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                            String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(saleItem.getItemCode());
                            if (pictPath.length() > 0) {
                                rowx.add("<div align=\"center\"></div>");
                            } else {
                                rowx.add("");
                            }
                        }

                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");

                        switch (groupMeth) {
                            case SrcSaleReport.GROUP_BY_CATEGORY:
                                rowx.add("<div align=\"left\"></div>");
                                linkParameter = String.valueOf(saleItem.getCategoryId());
                                break;
                            case SrcSaleReport.GROUP_BY_LOCATION:
                                rowx.add("<div align=\"left\"></div>");
                                break;
                            case SrcSaleReport.GROUP_BY_SALES:
                                if (oidSales != 0) {
                                    rowx.add("<div align=\"left\"></div>");
                                    break;
                                } else {
                                    rowx.add("<div align=\"left\"></div>");
                                }

                            case SrcSaleReport.GROUP_BY_SHIFT:
                                rowx.add("<div align=\"left\"></div>");
                                break;
                            case SrcSaleReport.GROUP_BY_SUPPLIER:
                                rowx.add("<div align=\"left\"></div>");
                                break;
                            default:
                                rowx.add("<div align=\"right\"><b>SUB&nbsp;TOTAL</b></div>");
                                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                                    rowx.add("<div align=\"left\"></div>");
                                }
                                break;
                        }

                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");
                        rowx.add("<div align=\"center\"></div>");

                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahqty) + "</b></div>");
                        if (srcSaleReport.getViewStock() == 1) {
                            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subQtyStock) + "</b></div>");
                        }
                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalRRP) + "</b></div>");
                        rowx.add("<div align=\"right\"><b></b></div>");

                        if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalNetPrice) + "</b></div>");
                        }

                        if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(((subjumlahjual - subjumlahbeli) / subjumlahbeli) * 100) + "</b></div>");
                        }

                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalPriceCons) + "</b></div>");
                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalForTotalCons) + "</b></div>");
                        rowx.add("<div align=\"center\"></div>");

                        lstData.add(rowx);
                        lstLinkData.add(linkParameter);
                        
                        rowx = new Vector();
                        rowx.add("");
                        lstData.add(rowx);
                    }

                    subjumlahqty = 0;
                    subjumlahqty = saleItem.getTotalQty();

                    subjumlahjual = 0;
                    subjumlahjual = saleItem.getTotalSold();

                    subjumlahbeli = 0;
                    subjumlahbeli = saleItem.getTotalCost();

                    subTotalNetPrice = 0;
                    subTotalNetPrice = netPrice;

                    subQtyStock = 0;
                    subQtyStock = sisaStock;

                    subTotalRRP = 0;
                    subTotalRRP = rrp;

                    subTotalPriceCons = 0;
                    subTotalPriceCons = priceCons;

                    subTotalForTotalCons = 0;
                    subTotalForTotalCons = totalCons;

                    rowx = new Vector();
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    // untuk menampilkan gambar
                    if (viewImage == 1) {
                        SessUploadCatalogPhoto objSessUploadCatalogPhoto = new SessUploadCatalogPhoto();
                        String pictPath = objSessUploadCatalogPhoto.fetchImagePeserta(saleItem.getItemCode());
                        if (pictPath.length() > 0) {
                            rowx.add("<div align=\"center\"></div>");
                        } else {
                            rowx.add("");
                        }
                    }
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");

                    switch (groupMeth) {
                        case SrcSaleReport.GROUP_BY_CATEGORY:
                            rowx.add("<div align=\"left\"></div>");
                            linkParameter = String.valueOf(saleItem.getCategoryId());
                            break;
                        case SrcSaleReport.GROUP_BY_LOCATION:
                            rowx.add("<div align=\"left\"></div>");
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
                            if (oidSales != 0) {
                                rowx.add("<div align=\"left\"></div>");
                                break;
                            } else {
                                rowx.add("<div align=\"left\"></div>");
                            }

                        case SrcSaleReport.GROUP_BY_SHIFT:
                            rowx.add("<div align=\"left\"></div>");
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            rowx.add("<div align=\"left\"></div>");
                            break;
                        default:
                            rowx.add("<div align=\"left\" style='background-color: yellow'><b>" + namaHeader + "</b></div>");
                            
                            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                                rowx.add("<div align=\"left\"></div>");
                            }
                            break;
                    }
                    
                    rowx.add("<div align=\"center\"></div>");
                    if (srcSaleReport.getViewStock() == 1) {
                        rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(0) + "</b></div>");
                    }
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");
                    rowx.add("<div align=\"center\"></div>");

                    if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                        rowx.add("<div align=\"center\"></div>");
                    }

                    if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                        rowx.add("<div align=\"center\"></div>");
                    }

                    lstData.add(rowx);
                    lstLinkData.add(linkParameter);

                } else {
                    subjumlahqty += saleItem.getTotalQty();
                    subjumlahjual += saleItem.getTotalSold();
                    subjumlahbeli += saleItem.getTotalCost();
                    subTotalNetPrice += netPrice;
                    subQtyStock += sisaStock;
                    subTotalRRP += rrp;
                    subTotalPriceCons += priceCons;
                    subTotalForTotalCons += totalCons;
                }
                
                //header 2 untuk keterangan promosi
                long masterTypeId = saleItem.getMasterTypeId();
                String masterTypeName = saleItem.getMasterTypeName();
                if (masterTypeId == 0) {
                    masterTypeName = "-";
                }
                
                if (loop == 1 || lastMasterTypeId != masterTypeId) {
                    lastMasterTypeId = masterTypeId;
                    rowx = new Vector();
                    rowx.add("");
                    rowx.add("<div align=\"left\"><b>Promo : " + masterTypeName + "</b></div>");
                    lstData.add(rowx);
                }

                rowx = new Vector();
                rowx.add("<div align=\"center\">" + (i + 1) + "</div>");
                rowx.add("<div align=\"center\">" + saleItem.getBillNumber() + "</div>");
                rowx.add("<div align=\"center\">" + saleItem.getBillDate() + "</div>");

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

                rowx.add("<div align=\"left\">" + saleItem.getItemCode() + "</div>");
                rowx.add("<div align=\"left\">" + saleItem.getItemBarcode() + "</div>");

                switch (groupMeth) {
                    case SrcSaleReport.GROUP_BY_CATEGORY:
                        rowx.add("<div align=\"left\">" + saleItem.getCategoryName() + "</div>");
                        linkParameter = String.valueOf(saleItem.getCategoryId());
                        break;
                    case SrcSaleReport.GROUP_BY_LOCATION:
                        rowx.add("<div align=\"left\">" + saleItem.getLocationName() + "</div>");
                        linkParameter = String.valueOf(saleItem.getLocationId());
                        break;
                    case SrcSaleReport.GROUP_BY_SALES:
                        if (oidSales != 0) {
                            rowx.add("<div align=\"left\">" + saleItem.getSalesPersonName() + "</div>");
                            linkParameter = String.valueOf(saleItem.getSalesPersonId());
                            break;
                        } else {
                            rowx.add("<div align=\"left\">" + saleItem.getItemName() + "</div>");
                            linkParameter = String.valueOf(saleItem.getItemId());
                        }
                    case SrcSaleReport.GROUP_BY_SHIFT:
                        rowx.add("<div align=\"left\">" + saleItem.getShiftName() + "</div>");
                        linkParameter = String.valueOf(saleItem.getShiftId());
                        break;
                    case SrcSaleReport.GROUP_BY_SUPPLIER:
                        rowx.add("<div align=\"left\">" + saleItem.getSupplierName() + "</div>");
                        linkParameter = String.valueOf(saleItem.getSupplierId());
                        break;
                    default:
                        rowx.add("<div align=\"left\">" + saleItem.getItemName() + "</div>");
                        linkParameter = String.valueOf(saleItem.getItemId());

                        if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                            rowx.add("<div align=\"left\">" + saleItem.getShiftName() + "</div>");
                            linkParameter = String.valueOf(saleItem.getShiftId());
                        }

                        break;
                }

                if (saleItem.getSupplierId() > 0) {
                    grandTotalQty += saleItem.getTotalQty();
                    grandTotalSold += saleItem.getTotalSold();
                    grandTotalCost += saleItem.getTotalCost();
                    grandSisaStock += sisaStock;
                }
                
                rowx.add("<div align=\"left\">" + color.getColorName() + "</div>");
                rowx.add("<div align=\"center\">" + masterTypeSize.getMasterName() + "</div>");
                rowx.add("<div align=\"center\">" + masterTypeSeason.getMasterName() + "</div>");
                rowx.add("<div align=\"center\">soh</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(saleItem.getTotalQty()) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(rrp) + "</div>");
                rowx.add("<div align=\"center\">" + discPct + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(netPrice) + "</div>");
                rowx.add("<div align=\"center\">" + cons + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(priceCons) + "</div>");
                rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(totalCons) + "</div>");
                rowx.add("<div align=\"center\">end</div>");

                if (srcSaleReport.getViewStock() == 1) {
                    rowx.add("<div align=\"right\">" + FRMHandler.userFormatStringDecimal(sisaStock) + "</div>");
                }

                lstData.add(rowx);
                lstLinkData.add(linkParameter);
            }

            Vector rowx = new Vector();
            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SALES
				|| srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                rowx = new Vector();
                rowx.add("<div align=\"center\"></div>");
                // untuk menampilkan gambar
                if (viewImage == 1) {
                    rowx.add("");
                }

                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");
                //barcode
                rowx.add("<div align=\"center\"></div>");

                switch (groupMeth) {
                    case SrcSaleReport.GROUP_BY_CATEGORY:
                        rowx.add("<div align=\"left\"></div>");
                        break;
                    case SrcSaleReport.GROUP_BY_LOCATION:
                        rowx.add("<div align=\"left\"></div>");
                        break;
                    case SrcSaleReport.GROUP_BY_SALES:
                        if (oidSales != 0) {
                            rowx.add("<div align=\"left\"></div>");
                            break;
                        } else {
                            rowx.add("<div align=\"left\"></div>");
                        }

                    case SrcSaleReport.GROUP_BY_SHIFT:
                        rowx.add("<div align=\"left\"></div>");
                        break;
                    case SrcSaleReport.GROUP_BY_SUPPLIER:
                        rowx.add("<div align=\"left\"></div>");
                        break;
                    default:
                        rowx.add("<div align=\"right\"><b>SUB&nbsp;TOTAL</b></div>");

                        if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                            rowx.add("<div align=\"left\"></div>");
                        }

                        break;
                }

                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");
                rowx.add("<div align=\"center\"></div>");

                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subjumlahqty) + "</b></div>");
                if (srcSaleReport.getViewStock() == 1) {
                    rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandSisaStock) + "</b></div>");
                }
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalRRP) + "</b></div>");
                rowx.add("<div align=\"right\"><b></b></div>");

                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalNetPrice) + "</b></div>");
                }

                if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                    rowx.add("<div align=\"right\"><b></b></div>");
                }

                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalPriceCons) + "</b></div>");
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(subTotalForTotalCons) + "</b></div>");
                rowx.add("<div align=\"center\"></div>");

                lstData.add(rowx);
                lstLinkData.add("");
            }

            //>>>>> ADD ROW FOR GRAND TOTAL <<<<<
            rowx = new Vector();
            if (viewImage == 1) {
                rowx.add("");
            }
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("<div align=\"right\"><b>GRAND&nbsp;TOTAL</b></div>");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            rowx.add("");
            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SHIFT) {
                rowx.add("<div align=\"right\"></div>");
            }
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalQty) + "</b></div>");
            if (srcSaleReport.getViewStock() == 1) {
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandSisaStock) + "</b></div>");
            }
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalRRP) + "</b></div>");
            rowx.add("<div align=\"right\"><b></b></div>");
            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalNetPrice) + "</b></div>");
            }
            if (srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_CATEGORY || srcSaleReport.getQueryType() == SrcSaleReport.GROUP_BY_SUPPLIER) {
                rowx.add("<div align=\"right\"><b></b></div>");
            }

            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalPriceCons) + "</b></div>");
            rowx.add("<div align=\"right\"><b>" + FRMHandler.userFormatStringDecimal(grandTotalForTotalCons) + "</b></div>");
            rowx.add("<div align=\"right\"><b></b></div>");

            lstData.add(rowx);
            lstLinkData.add(String.valueOf(0));

            result = ctrlist.draw();
        } else {
            result = "<div class=\"msginfo\">&nbsp;&nbsp;" + textListTitleHeader[language][2] + "</div>";
        }
        listAll.add(result);
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
int fromglobal = FRMQueryString.requestInt(request, "fromglobal"); 
int recordToGet = 20;
int vectSize = 0;
String whereClause = "";
int sortCommand = Command.FIRST;//9999;

String useForGreenbowl = PstSystemProperty.getValueByName("USE_FOR_GREENBOWL");

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcSaleReport srcSaleReport = new SrcSaleReport();
//SrcSaleReport srcSaleReportDetail = new SrcSaleReport();
//SaleReportDocument saleReportDocument= new SaleReportDocument();
//FrmSrcSaleReport frmSrcSaleReportDetail = new FrmSrcSaleReport(request, srcSaleReportDetail);
FrmSrcSaleReport frmSrcSaleReport = new FrmSrcSaleReport(request, srcSaleReport);

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

long salesId = 0;
try{
    salesId = FRMQueryString.requestLong(request, "FRM_FLD_SALES_PERSON_ID");
}catch(Exception ex){

}

Vector records = SaleReportDocument.getList(srcSaleReport,0,0,SaleReportDocument.LOG_MODE_CONSOLE);
if (useForGreenbowl.equals("1")) {
    records = SaleReportDocument.getListJoinWithMasterTypeForPromosi(srcSaleReport, 0, 0, SaleReportDocument.LOG_MODE_CONSOLE);
}
vectSize = records.size();

// ini dipakai untuk
// pembuatan invoice internal
try{
    session.putValue("list_for_internal_invoice",records);
}catch(Exception e){}

Vector listHeaderPdf = new Vector(1,1);
Vector listBodyPdf = new Vector(1,1);
Vector listPdf = new Vector(1,1);

Vector list = drawList(SESS_LANGUAGE,records,srcSaleReport,srcSaleReport.getImageView(),approot,salesId);
if (useForGreenbowl.equals("1")) {
    list = drawListGreenbowl(SESS_LANGUAGE, records, srcSaleReport, srcSaleReport.getImageView(), approot, salesId);
}
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

String dateExport = Formater.formatDate(new Date(), "yyyy-MM-dd");
if (srcSaleReport.getDateFrom().equals(srcSaleReport.getDateTo())) {
    dateExport = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd");
} else {
    dateExport = Formater.formatDate(srcSaleReport.getDateFrom(), "yyyy-MM-dd") + "_to_" + Formater.formatDate(srcSaleReport.getDateTo(), "yyyy-MM-dd");
}

response.setContentType("application/x-msexcel"); 
response.setHeader("Content-Disposition","attachment; filename=" + "Report_Sales_Global_"+location.getName().replaceAll(" ", "_") +"_"+dateExport+".xls" );

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
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdEdit(oid){
	document.frm_reportsale.hidden_group_id.value=oid;
	document.frm_reportsale.command.value="<%=Command.LIST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListFirst(){
	document.frm_reportsale.command.value="<%=Command.FIRST%>";
	document.frmsrcreportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListPrev(){
	document.frm_reportsale.command.value="<%=Command.PREV%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListNext(){
	document.frm_reportsale.command.value="<%=Command.NEXT%>";
	document.frmsrcreportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdListLast(){
	document.frm_reportsale.command.value="<%=Command.LAST%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
	document.frm_reportsale.action="reportsale_list_detail_global.jsp";
	document.frm_reportsale.submit();
}

function cmdBack(){
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.global_group.value=<%=global_group%>;
        if(<%=fromglobal%>==1){
            document.frm_reportsale.action="src_reportsale_global.jsp";
        }else{
            document.frm_reportsale.action="src_reportsale_detail_global.jsp";
        }
	
	document.frm_reportsale.submit();
}

function printForm(){
    window.open("<%=approot%>/servlet/com.dimata.posbo.report.sale.SaleReportPdfDetail?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	//window.open("reportsaleinvoice_form_print.jsp","stockreport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}
function printFormExcel(){
     window.open("reportsale_list_detail_global_excel.jsp?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	
}


function printFormHtml(){
     window.open("reportsale_list_detail_global_html.jsp?view_photo=<%=srcSaleReport.getImageView()%>&approot=<%=approot%>","salereport","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
	
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
              <input type="hidden" name="fromglobal" value="<%=fromglobal%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top">
                  <td height="20" valign="middle" colspan="3" align="center"><h4>
                    <%=textListTitleHeader[SESS_LANGUAGE][1]%> <%=new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()%> <%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()%></h4>
                    <% listHeaderPdf.add(3,textListTitleHeader[SESS_LANGUAGE][0]+" "+new String(SrcSaleReport.reportType[SESS_LANGUAGE][srcSaleReport.getTransType()]).toUpperCase()+" "+SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_GROUP_BY]+" "+new String(SrcSaleReport.groupMethod[SESS_LANGUAGE][global_group]).toUpperCase()); %>
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="20" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][11]).toUpperCase()%>:
                    <%=Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")%> <%=textListTitleHeader[SESS_LANGUAGE][10]%> 
                    <%=Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy")%>
                    <%
                    listHeaderPdf.add(4, (textListTitleHeader[SESS_LANGUAGE][11]).toUpperCase());
                    listHeaderPdf.add(5, Formater.formatDate(srcSaleReport.getDateFrom(), "dd-MM-yyyy")+" "+textListTitleHeader[SESS_LANGUAGE][10]+" "+Formater.formatDate(srcSaleReport.getDateTo(), "dd-MM-yyyy"));
                    %></b>
                  </td>
                  <td height="20" valign="middle" colspan="2" width="90%">
                  </td>
                </tr>
                <tr align="left" valign="top">
                  <td height="20" valign="middle" colspan="1" width="10%">
                    <b><%=(textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase()%>: <%=location.getName()%>
                    <%
                    listHeaderPdf.add(6, (textListTitleHeader[SESS_LANGUAGE][12]).toUpperCase());
                    listHeaderPdf.add(7, location.getName());
                    %></b>
                  </td>
                  <td height="20" valign="middle" colspan="2" width="90%">
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
                            <td height="20" valign="middle" colspan="1" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_CATEGORY]%>: <%=category.getName()%></b>
                            </td>
                            <td height="20" valign="middle" colspan="2" width="90%">
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_CATEGORY]);
                            listHeaderPdf.add(9, category.getName());
                            break;
                        case SrcSaleReport.GROUP_BY_SALES:
                            if (srcSaleReport.getSalesPersonId()!=0){
//                                Sales sales = PstSales.fetchExc(srcSaleReport.getSalesPersonId());
                            AppUser sales = PstAppUser.fetch(srcSaleReport.getSalesPersonId());
                                %>
                                <tr align="left" valign="top">
                                <td height="20" valign="middle" colspan="1" class="command">
                                    <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SALES]%>: <%=sales.getFullName()%></b>
                                </td>
                                <td height="20" valign="middle" colspan="2" width="90%">
                                </td>
                                </tr>
                                <%
                                listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SALES]);
                                listHeaderPdf.add(9, sales.getFullName());
                            }else{ %>
                                <tr align="left" valign="top">
                                <td height="20" valign="middle" colspan="1" class="command">
                                    <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SALES]%>: All Sales</b>
                                </td>
                                <td height="20" valign="middle" colspan="2" width="90%">
                                </td>
                                </tr>
                            <%
                            }
                            break;
                        case SrcSaleReport.GROUP_BY_SHIFT:
                            Shift shift = new Shift();
                            if(srcSaleReport.getShiftId()!=0){
                                shift = PstShift.fetchExc(srcSaleReport.getShiftId());
                            }
                            %>
                            <tr align="left" valign="top">
                            <td height="20" valign="middle" colspan="1" class="command">
                                <b><%=SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SHIFT]%>: <%=shift.getName()%></b>
                            </td>
                            <td height="20" valign="middle" colspan="2" width="90%">
                            </td>
                            </tr>
                            <%
                            listHeaderPdf.add(8, SrcSaleReport.fieldNames[SESS_LANGUAGE][SrcSaleReport.FLD_SHIFT]);
                            listHeaderPdf.add(9, shift.getName());
                            break;
                        case SrcSaleReport.GROUP_BY_SUPPLIER:
                            ContactList contact = new ContactList();
                            String supp = "Semua Supplier";
                            if(srcSaleReport.getSupplierId()!=0 ){
                                try{
                                    contact = PstContactList.fetchExc(srcSaleReport.getSupplierId());
                                    supp = contact.getCompName();
                                }catch(Exception ex){}
                            }
                            %>
                            <tr align="left" valign="top">
                            <td height="20" valign="middle" colspan="1" class="command"><b>SUPPLIER: <%=supp%></b>
                            </td>
                            <td height="20" valign="middle" colspan="2" width="90%">
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
                  <td height="22" valign="middle" colspan="3"></td>
                </tr>
                <tr align="left" valign="top">
                  <td height="22" valign="middle" colspan="3"> <%=str%></td><%-- tampil --%>
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
