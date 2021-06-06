<%-- 
    Document   : reportreceiveinvoice_list_excel
    Created on : May 4, 2016, 9:17:35 AM
    Author     : dimata005
--%>
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
<%@ page import = "com.dimata.common.entity.payment.CurrencyType"%>
<%@ page import = "com.dimata.common.entity.payment.PstCurrencyType"%>
<%@ page import = "com.dimata.common.entity.payment.StandartRate"%>
<%@ page import = "com.dimata.common.entity.payment.PstStandartRate"%>

<%@ include file = "../../../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_RECEIVING, AppObjInfo.G2_PURCHASE_RECEIVE_REPORT, AppObjInfo.OBJ_PURCHASE_RECEIVE_REPORT_BY_RECEIPT); %>
<%@ include file = "../../../main/checkuser.jsp" %>


<!-- Jsp Block -->
<%!
public static final int ADD_TYPE_SEARCH = 0;
public static final int ADD_TYPE_LIST = 1;

public static final String textListGlobal[][] = {
    {"Tidak ada data penerimaan barang", "LAPORAN PENERIMAAN BARANG DARI SUPLIER PER INVOICE", "Periode", "s/d", "Lokasi",
             "Laporan Penerimaan","Cetak Laporan Penerimaan","Mata Uang","Semua Lokasi","Semua Mata Uang"},
     {"Receiving goods item not found", "GOODS RECEIVE FROM SUPPLIER REPORT BY INVOICE", "Period", "to", "Location",
             "Receive Report","Print Receive Report","Currency","All Location","All Currency"}
};

/* this constant used to list text of listHeader */
public static final String textListMaterialHeader[][] =
{
    {"NO","SKU","NAMA","QTY","HARGA BELI","TOTAL BELI","HPP","TOTAL HPP"},
    {"NO","CODE","NAME","QTY","COST","TOTAL COST","PRICE","TOTAL PRICE"}
    
};

public static final String textListMaterialDetail[][] =
{
    {"Tanggal","Invoice","Supplier","Keterangan"},
    {"Date","Invoice","Vendor","Remark"}
    
};

public Vector drawLineHorizontal() {
    Vector rowx = new Vector();
    //Add Under line
    rowx.add(""); //0
    rowx.add("");//1
    rowx.add("<div align=\"center\">"+"</div>");//10
    rowx.add("");//3
    rowx.add("<div align=\"center\">"+"</div>");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"</div>");//6
    rowx.add("");//7
    rowx.add("<div align=\"center\">"+"</div>");//8
    rowx.add("");//9
    rowx.add("<div align=\"center\">"+"</div>");//10
    rowx.add("");//11
    rowx.add("<div align=\"center\">"+"</div>");//12
    
    rowx.add("");//13
    rowx.add("<div align=\"center\">"+"</div>");//14
    rowx.add("");//15
    rowx.add("<div align=\"center\">"+"</div>");//16
    return rowx;
}

public Vector drawHeader(int language) {
    Vector rowx = new Vector();
    //Add Header
    rowx.add("");// 0
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");// 1
    rowx.add("<div align=\"center\">"+"</div>");// 2
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");// 3
    rowx.add("<div align=\"center\">"+"</div>");// 4
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");// 5
    rowx.add("<div align=\"center\">"+"</div>");// 6
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");// 7
    rowx.add("<div align=\"center\">"+"</div>");// 8
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");// 9
    rowx.add("<div align=\"center\">"+"</div>");// 10
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");// 11
    rowx.add("<div align=\"center\">"+"</div>");// 12
    
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][6]+"</div>");// 13
    rowx.add("<div align=\"center\">"+"</div>");// 14
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][7]+"</div>");// 15
    rowx.add("<div align=\"center\">"+"</div>");// 16
    return rowx;
}

public Vector drawLineSubTotal() {
    Vector rowx = new Vector();
    rowx.add(""); //0
    rowx.add("");//1
    rowx.add("");//10
    rowx.add("");//3
    rowx.add("");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"</div>");//6
    rowx.add("");//7
    rowx.add("<div align=\"center\">"+"</div>");//8
    rowx.add("");//9
    rowx.add("<div align=\"center\">"+"</div>");//10
    rowx.add("");//11
    rowx.add("<div align=\"center\">"+"</div>");//12
    
    rowx.add("");//13
    rowx.add("<div align=\"center\">"+"</div>");//14
    rowx.add("");//15
    rowx.add("<div align=\"center\">"+"</div>");//16
    
    return rowx;
}

public Vector drawLineTotal() {
    Vector rowx = new Vector();
    rowx.add(""); //0
    rowx.add("");//1
    rowx.add("");//10
    rowx.add("");//3
    rowx.add("");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"</div>");//6
    rowx.add("");//7
    rowx.add("<div align=\"center\">"+"</div>");//8
    rowx.add("");//9
    rowx.add("<div align=\"center\">"+"</div>");//10
    rowx.add("");//11
    rowx.add("<div align=\"center\">"+"</div>");//12
    
    rowx.add("");//13
    rowx.add("<div align=\"center\">"+"</div>");//14
    rowx.add("");//15
    rowx.add("<div align=\"center\">"+"</div>");//16
    
    return rowx;
}

public Vector drawLineSingleSpot() {
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

public Vector drawLineTotalSide() {
    Vector rowx = new Vector();
    rowx.add(""); //0
    rowx.add("");//1
    rowx.add("");//10
    rowx.add("");//3
    rowx.add("");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"</div>");//6
    rowx.add("");//7
    rowx.add("<div align=\"center\">"+"</div>");//8
    rowx.add("");//9
    rowx.add("<div align=\"center\">"+"</div>");//10
    rowx.add("");//12
    rowx.add("<div align=\"center\">"+"</div>");//10
    return rowx;
}
	
public Vector drawList(int language,Vector objectClass, SrcReportReceive objSrcReportReceive) {
    Vector result = new Vector();
    if(objectClass!=null && objectClass.size()>0) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("");
        ctrlist.setTitleStyle("listgentitle");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("listgentitle");
        ctrlist.setCellSpacing("0");
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","3%","0","0","center","top");    // nomer
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","8%","0","0","center","top");    // sku
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","22%","0","0","center","top");   // nama barang
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","4%","0","0","right","bottom");  // qty
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","10%","0","0","right","bottom");  // harga beli barang default.
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","10%","0","0","right","bottom"); // total harga beli barang
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        
        ctrlist.dataFormat("","10%","0","0","right","bottom");  // harga beli barang default.
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","10%","0","0","right","bottom"); // total harga beli barang
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.setBorder(1);

        ctrlist.setLinkRow(-1);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        long noBill = 0;
        boolean firstRow = true;
        double totalRec = 0.00;
        double totalPrice = 0.00;
        double totalQty = 0;
        double subTotalRec = 0.00;
        double subTotalPrice = 0.00;
        double subTotalQty = 0;
        int baris = 0;
        int countTrue = 0;
        
        double subTotalJual = 0.00;
        double totalJual = 0.00;
        double totalLastJual = 0.00;
        
        int maxlines = 73;
        int maxlinespgdst = 74;
        boolean boolmaxlines = true;
        
        StandartRate objStandartRate = PstStandartRate.getActiveStandardRate(objSrcReportReceive.getCurrencyId());
        
        //Tentukan nilai baris untuk halaman pertama
        for(int i=0; i<objectClass.size(); i++) {
            Vector rowx = new Vector();
            Vector vt = (Vector)objectClass.get(i);
            MatReceive rec = (MatReceive)vt.get(0);
            MatReceiveItem rmi = (MatReceiveItem)vt.get(1);
            Material mat = (Material)vt.get(2);
            Unit unt = (Unit)vt.get(3);
            Category cat = (Category)vt.get(5);
            SubCategory scat = (SubCategory)vt.get(6);
            ContactList cnt = (ContactList)vt.get(7);
            String material_name = mat.getName();
            String unit_name = unt.getCode();
            if (material_name.length() > 44) material_name = material_name.substring(0,44);
            if (unit_name.length() > 4) unit_name = unit_name.substring(0,4);
            
            //Tambahkan header tanggal dan invoice
            if (noBill != rec.getOID()) {
                noBill = rec.getOID();
                if (firstRow == true) {
                    firstRow = false;
                    
                    //Add header only
                    //lstData.add(drawLineHorizontal());
                    //baris += 1;
                    lstData.add(drawHeader(language));
                    baris += 1;
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    
                    //Add date
                    rowx.add("*2");
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][0]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][1]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getInvoiceSupplier()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][2]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice keterangan
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][3]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRemark()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    rowx = new Vector();
                } else {
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        //lstData.add(drawLineHorizontal());
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    //Add sub total then header
                    //Add sub total
                    rowx.add("");
                    rowx.add(""); // no
                    rowx.add(""); // |
                    rowx.add(""); // sku
                    rowx.add(""); // |
                    rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    rowx.add("<div align=\"right\"></div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    rowx.add("<div align=\"right\"></div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalJual)+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    lstData.add(drawLineSubTotal());
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
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
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][0]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
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
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][1]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getInvoiceSupplier()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
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
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][2]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
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
                    rowx.add("");
                    rowx.add(textListMaterialDetail[language][3]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRemark()+"</div>");
                    rowx.add("<div align=\"center\">"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal());
                        lstData.add(drawLineHorizontal());
                        baris = 1;
                        lstData.add(drawHeader(language));
                        baris += 1;
                        lstData.add(drawLineHorizontal());
                        baris += 1;
                    }
                    
                    lstData.add(drawLineHorizontal());
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
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
                    totalJual = 0.00;
                }
            }
            
            rowx.add("");
            rowx.add(""+(i+1));
            rowx.add("<div align=\"center\">"+"</div>");
            rowx.add(mat.getSku());
            rowx.add("<div align=\"center\">"+"</div>");
            rowx.add(material_name);
            rowx.add("<div align=\"center\">"+"</div>");
            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty())+"</div>");
            rowx.add("<div align=\"center\">"+"</div>");
            if(objSrcReportReceive.getCurrencyId() == 0) {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getCost() * rec.getTransRate())+"</div>");
            } else {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getCost())+"</div>");
            }
            rowx.add("<div align=\"center\">"+"</div>");
            if(objSrcReportReceive.getCurrencyId() == 0) {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * rmi.getCost() * rec.getTransRate())+"</div>");
            } else {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * rmi.getCost())+"</div>");
            }
            rowx.add("<div align=\"center\">"+"</div>");
            if(objSrcReportReceive.getCurrencyId() == 0) {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
            } else {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()/objStandartRate.getSellingRate())+"</div>");
            }
            rowx.add("<div align=\"center\">"+"</div>");
            if(objSrcReportReceive.getCurrencyId() == 0) {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * mat.getDefaultPrice())+"</div>");
            } else {
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((rmi.getQty() * mat.getDefaultPrice())/objStandartRate.getSellingRate())+"</div>");
            }
            rowx.add("<div align=\"center\">"+"</div>");
            
            lstData.add(rowx);
            baris += 1;
            
            if (baris == maxlines) {
                if(boolmaxlines){
                    maxlines = maxlinespgdst;
                }
                //Add line
                //lstData.add(drawLineHorizontal());
                //Add header for next page and restart counting baris
                lstData.add(drawLineHorizontal());
                lstData.add(drawLineHorizontal());
                baris = 1;
                lstData.add(drawHeader(language));
                baris += 1;
                lstData.add(drawLineHorizontal());
                baris += 1;
            }
            
            
            totalQty += rmi.getQty();
            subTotalQty += rmi.getQty();
            
            if(objSrcReportReceive.getCurrencyId() == 0) {
                totalRec += rmi.getCost() * rec.getTransRate();
                totalPrice += (rmi.getCost() * rmi.getQty() * rec.getTransRate());
                
                subTotalRec += rmi.getCost() * rec.getTransRate();
                subTotalPrice += (rmi.getCost() * rmi.getQty() * rec.getTransRate());
                
                subTotalJual += mat.getDefaultPrice();
                totalJual += (mat.getDefaultPrice() * rmi.getQty());
                totalLastJual += (mat.getDefaultPrice() * rmi.getQty());
            }
            else {
                totalRec += rmi.getCost();
                totalPrice += (rmi.getCost() * rmi.getQty());
                
                subTotalRec += rmi.getCost();
                subTotalPrice += (rmi.getCost() * rmi.getQty());
                
                subTotalJual += (mat.getDefaultPrice()/objStandartRate.getSellingRate());
                totalJual += ((mat.getDefaultPrice() * rmi.getQty())/objStandartRate.getSellingRate());
                totalLastJual += ((mat.getDefaultPrice() * rmi.getQty())/objStandartRate.getSellingRate());
            }
            
            lstLinkData.add("");
        }
        
        lstData.add(drawLineHorizontal());
        Vector rowx = new Vector();
        //Add sub total
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">SUB TOTAL</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalJual)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        
        lstData.add(rowx);
        lstData.add(drawLineTotal());
        
        //Add TOTAL
        rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">TOTAL</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalLastJual)+"</div>");
        rowx.add("<div align=\"center\">"+"</div>");
        
        lstData.add(rowx);
        lstData.add(drawLineTotal());
        result = ctrlist.drawMePartVector(0, lstData.size(), rowx.size());
    } else {
        result.add("<div class=\"msginfo\">&nbsp;&nbsp;"+textListGlobal[language][0]+"</div>");
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

/**
 * instantiate some object used in this page
 */
ControlLine ctrLine = new ControlLine();
SrcReportReceive srcReportReceive = new SrcReportReceive();
SessReportReceive sessReportReceive = new SessReportReceive();
FrmSrcReportReceive frmSrcReportReceive = new FrmSrcReportReceive(request, srcReportReceive);

/**
 * handle current search data session
 */
try {
    srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
    if (srcReportReceive == null) srcReportReceive = new SrcReportReceive();
} catch(Exception e) {
    srcReportReceive = new SrcReportReceive();
}

/**
 * get vectSize, start and data to be display in this page
 */
Vector records = sessReportReceive.getReportReceive(srcReportReceive);
vectSize = records.size();
if(iCommand==Command.FIRST || iCommand==Command.NEXT || iCommand==Command.PREV || iCommand==Command.LAST || iCommand==Command.LIST) {
    //start = ctrlReportReceive.actionList(iCommand,start,vectSize,recordToGet);
}

/**
 * set value vector for stock report print
 * biar tidak load data lagi
 */

Location location = new Location();
if (srcReportReceive.getLocationId() != 0) {
try {
    location = PstLocation.fetchExc(srcReportReceive.getLocationId());
} catch(Exception e) {
    System.out.println("Exc when fetch Location : " + e.toString());
}
} else {
    location.setName(textListGlobal[SESS_LANGUAGE][8]);
}



CurrencyType objCurrencyType = new CurrencyType();
if(srcReportReceive.getCurrencyId() != 0) {
    try {
        objCurrencyType = PstCurrencyType.fetchExc(srcReportReceive.getCurrencyId());
    } catch(Exception e) {
        System.out.println(e.toString());
    }
} else {
    //objCurrencyType.setCode("-");
    objCurrencyType.setCode(textListGlobal[SESS_LANGUAGE][9]);
}

%>
<!-- End of Jsp Block -->
<%@ page contentType="application/x-msexcel" %>
<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 

</head> 
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
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
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" ><h4><%=textListGlobal[SESS_LANGUAGE][1]%></h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%"><strong><%=textListGlobal[SESS_LANGUAGE][2]%>:<%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%>
                        <%=textListGlobal[SESS_LANGUAGE][3]%>
                        <%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%></strong></td>
                  <td width="90%" colspan="2">
                        
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td><strong><%=textListGlobal[SESS_LANGUAGE][4]%></strong><strong>:</strong> <%=location.getName().toUpperCase()%></td>
                  <td colspan="2"></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td><strong><%=textListGlobal[SESS_LANGUAGE][7]%></strong><strong>:</strong> <%=objCurrencyType.getCode()%></td>
                  <td colspan="2"></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">
                  <%
                    Vector hasil = drawList(SESS_LANGUAGE,records,srcReportReceive);
                    Vector report = new Vector(1,1);
                    report.add(srcReportReceive);
                    report.add(hasil);
                    try {
                            session.putValue("SESS_MAT_RECEIVE_REPORT_INVOICE",report);
                    }
                    catch(Exception e){}

                    for(int k=0;k<hasil.size();k++){
                            out.println(hasil.get(k));
                    }
                  %> </td>
                </tr>
                <tr>
				  <td align="right" colspan="3">&nbsp;</td>
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
