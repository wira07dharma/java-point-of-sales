<%@page import="com.dimata.posbo.session.masterdata.SessMaterial"%>
<%@page import="com.dimata.common.entity.payment.PriceType"%>
<%@page import="com.dimata.common.entity.payment.PstPriceType"%>
<%@page import="com.dimata.posbo.entity.purchasing.PstPurchaseOrder"%>
<%@page import="com.dimata.posbo.entity.purchasing.PurchaseOrder"%>
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
    {"NO","SKU","NAMA","QTY","HARGA BELI","TOTAL BELI","HPP","TOTAL HPP","BARCODE","HARGA JUAL","TOTAL HARGA JUAL","MARGIN"},
    {"NO","CODE","NAME","QTY","COST","TOTAL COST","PRICE","TOTAL PRICE","BARCODE","SALE PRICE","TOTAL SALES PRICE","MARGIN"}
    
};

public static final String textListMaterialDetail[][] =
{
    {"Tanggal","Invoice","Supplier","Keterangan","No PO","Kode Penerimaan"},
    {"Date","Invoice","Vendor","Remark","No PO","Code Receive"}
    
};

public Vector drawLineHorizontal(Vector listCurrStandardX, Vector listTypeHrga) {
    Vector rowx = new Vector();
    //Add Under line
    rowx.add("-"); //0
    rowx.add("-----------");//no
    rowx.add("<div align=\"center\">"+"-"+"</div>");//10
    rowx.add("------------------");//sku
    
    rowx.add("<div align=\"center\">"+"-"+"</div>");//10
    rowx.add("------------------");//barcode
    
    rowx.add("<div align=\"center\">"+"-"+"</div>");//4
    rowx.add("---------------------------------------------------");//5
    rowx.add("<div align=\"center\">"+"-"+"</div>");//6
    rowx.add("----");//7
    rowx.add("<div align=\"center\">"+"-"+"</div>");//8
    rowx.add("-------------------------");//9

    rowx.add("<div align=\"center\">"+"-"+"</div>");//10
    rowx.add("-------------------------");//11

    rowx.add("<div align=\"center\">"+"-"+"</div>");//12
    rowx.add("-------------------------");//13

    rowx.add("<div align=\"center\">"+"-"+"</div>");//14
    rowx.add("-------------------------");//15

    //cek harga jual
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("-------------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("-------------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("-------------------------");//15
            }
        }
    }

    rowx.add("<div align=\"center\">"+"-"+"</div>");//16
    return rowx;
}

public Vector drawHeader(int language,Vector listCurrStandardX, Vector listTypeHrga) {
    Vector rowx = new Vector();
    //Add Header
    rowx.add("|");// no
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][0]+"</div>");// no
    rowx.add("<div align=\"center\">"+"|"+"</div>");// sku
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][1]+"</div>");// sku
    
    rowx.add("<div align=\"center\">"+"|"+"</div>");// barcode
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][8]+"</div>");// barcode

    rowx.add("<div align=\"center\">"+"|"+"</div>");// sku
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][2]+"</div>");// 5
    rowx.add("<div align=\"center\">"+"|"+"</div>");// 6
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][3]+"</div>");// 7
    rowx.add("<div align=\"center\">"+"|"+"</div>");// 8
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][4]+"</div>");// 9
    rowx.add("<div align=\"center\">"+"|"+"</div>");// 10
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][5]+"</div>");// 11
    rowx.add("<div align=\"center\">"+"|"+"</div>");// 12
    
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][6]+"</div>");// 13
    rowx.add("<div align=\"center\">"+"|"+"</div>");// 14
    rowx.add("<div align=\"center\">"+textListMaterialHeader[language][7]+"</div>");// 15
    
    //.Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                rowx.add("<div align=\"center\">"+"|"+"</div>");// 14
                rowx.add("<div align=\"center\">"+textListMaterialHeader[language][9]+"</div>");// 15
                rowx.add("<div align=\"center\">"+"|"+"</div>");// 14
                rowx.add("<div align=\"center\">"+textListMaterialHeader[language][10]+"</div>");// 15
                rowx.add("<div align=\"center\">"+"|"+"</div>");// 14
                rowx.add("<div align=\"center\">"+textListMaterialHeader[language][11]+"</div>");// 15
            }
        }
    }

    rowx.add("<div align=\"center\">"+"|"+"</div>");// 16
    return rowx;
}

public Vector drawLineSubTotal(Vector listCurrStandardX, Vector listTypeHrga) {
    Vector rowx = new Vector();
    rowx.add("|"); //0
    rowx.add("");//1
    rowx.add("");//2
    rowx.add("");//barcode
    rowx.add("");//barcode
    rowx.add("");//3
    rowx.add("");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"-"+"</div>");//6
    rowx.add("---------");//7
    rowx.add("<div align=\"center\">"+"-"+"</div>");//8
    rowx.add("---------------");//9
    rowx.add("<div align=\"center\">"+"-"+"</div>");//10
    rowx.add("---------------");//11
    rowx.add("<div align=\"center\">"+"-"+"</div>");//12
    
    rowx.add("--------------------");//13
    rowx.add("<div align=\"center\">"+"-"+"</div>");//14
    rowx.add("--------------------");//15
    
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
            }
        }
    }
    
    rowx.add("<div align=\"center\">"+"-"+"</div>");//16
    
    return rowx;
}

public Vector drawLineTotal(Vector listCurrStandardX, Vector listTypeHrga) {
    Vector rowx = new Vector();
    rowx.add(""); //0
    rowx.add("");//1
    rowx.add("");//10
    rowx.add("");//10
    rowx.add("");//10
    rowx.add("");//3
    rowx.add("");//4
    rowx.add("");//5
    rowx.add("<div align=\"center\">"+"-"+"</div>");//6
    rowx.add("---------");//7
    rowx.add("<div align=\"center\">"+"-"+"</div>");//8
    rowx.add("--------------------");//9
    rowx.add("<div align=\"center\">"+"-"+"</div>");//10
    rowx.add("--------------------");//11
    rowx.add("<div align=\"center\">"+"-"+"</div>");//12
    rowx.add("--------------------");//13
    rowx.add("<div align=\"center\">"+"-"+"</div>");//14
    rowx.add("--------------------");//15
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
                rowx.add("<div align=\"center\">"+"-"+"</div>");//14
                rowx.add("--------------------");//15
            }
        }
    }
    rowx.add("<div align=\"center\">"+"-"+"</div>");//16
   
    return rowx;
}

public Vector drawLineSingleSpot(Vector listCurrStandardX, Vector listTypeHrga) {
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
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                rowx.add("");//14
                rowx.add("");//14
                rowx.add("");//14
                rowx.add("");//14
                rowx.add("");//14
                rowx.add("");//14
            }
        }
    }
    rowx.add("");
    return rowx;
}

public Vector drawLineTotalSide(Vector listCurrStandardX, Vector listTypeHrga) {
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
    if(listTypeHrga != null && listTypeHrga.size()>0){
        for(int i = 0; i<listTypeHrga.size();i++){
            for(int j=0;j<listCurrStandardX.size();j++){
                Vector temp = (Vector)listCurrStandardX.get(j);
                CurrencyType curr = (CurrencyType)temp.get(0);
                PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                //rowx.add("");//14
                rowx.add("--------------------");//12
                rowx.add("<div align=\"center\">"+"-"+"</div>");//10
            }
        }
    }
    return rowx;
}
	
public Vector drawList(int language,Vector objectClass, SrcReportReceive objSrcReportReceive, Vector listTypeHrga, int view) {
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
        ctrlist.dataFormat("","3%","0","0","center","top");    // sku

        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","7%","0","0","center","top");    // barcode
       
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","15%","0","0","center","top");   // nama barang

        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","1%","0","0","right","bottom");  // qty
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","10%","0","0","right","bottom");  // harga beli barang default.
        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","10%","0","0","right","bottom"); // total harga beli barang

        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","5%","0","0","right","bottom");  // harga beli barang default.

        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga beli barang
        
        //harga jual
        Vector listCurrStandardX = PstStandartRate.listCurrStandard(0);  
        if(listTypeHrga != null && listTypeHrga.size()>0){
            for(int i = 0; i<listTypeHrga.size();i++){
                for(int j=0;j<listCurrStandardX.size();j++){
                    Vector temp = (Vector)listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType)temp.get(0);
                    PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                    ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                    ctrlist.dataFormat("","5%","0","0","right","bottom"); // harga jual
                    ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                    ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                    ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                    ctrlist.dataFormat("","5%","0","0","right","bottom"); // profite
                }
            }
        }


        ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
        
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
        double subHargaJual=0;
        double subTotalHargaJual=0;

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
        double grandHargajual=0;
        double grandTotalHargajual=0;

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
                    lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                    baris += 1;
                    lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    
                    //Add date
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][0]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][1]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getInvoiceSupplier()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    

                    //Add invoice number
                    String nomorPo="Tanpa PO";
                    try{
                        if(rec.getPurchaseOrderId()!=0){
                                PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
                                nomorPo = purchaseOrder.getPoCode();
                        }
                    }catch(Exception ex){
                    }
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][4]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+nomorPo+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;

                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][5]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRecCode()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;

                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][2]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    //Add invoice keterangan
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][3]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRemark()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    
                    lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    rowx = new Vector();
                } else {
                    lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        //lstData.add(drawLineHorizontal());
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    //Add sub total then header
                    //Add sub total
                    rowx.add("|");
                    rowx.add(""); // no
                    rowx.add(""); // |
                    rowx.add(""); // sku
                    rowx.add(""); // |
                    rowx.add(""); // barcode
                    rowx.add(""); // |
                    rowx.add("<div align=\"right\">"+"SUB TOTAL"+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\"></div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\"></div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalJual)+"</div>");
                    //harga jual customer
                    if(listTypeHrga != null && listTypeHrga.size()>0){
                        for(int l = 0; l<listTypeHrga.size();l++){
                            for(int j=0;j<listCurrStandardX.size();j++){
                                rowx.add("");
                                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subHargaJual)+"</div>");
                                rowx.add("");
                                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalHargaJual)+"</div>");
                                rowx.add("");
                                rowx.add("");
                            }
                        }
                    }
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    
                    lstData.add(drawLineSubTotal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    //Add date
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][0]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+Formater.formatDate(rec.getReceiveDate(), "dd-MM-yyyy")+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][1]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getInvoiceSupplier()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }

                    String nomorPo="Tanpa PO";
                    try{
                        if(rec.getPurchaseOrderId()!=0){
                                PurchaseOrder purchaseOrder = PstPurchaseOrder.fetchExc(rec.getPurchaseOrderId());
                                nomorPo = purchaseOrder.getPoCode();
                        }
                    }catch(Exception ex){
                    }
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][4]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+nomorPo+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }

                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][4]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRecCode()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }

                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][2]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+cnt.getCompName()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    
                    //Add invoice number
                    rowx = new Vector();
                    rowx.add("*2");
                    rowx.add("|");
                    rowx.add(textListMaterialDetail[language][3]);
                    rowx.add("<div align=\"left\">&nbsp;:&nbsp;"+rec.getRemark()+"</div>");
                    rowx.add("<div align=\"center\">"+"|"+"</div>");
                    lstData.add(rowx);
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineHorizontal());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    
                    lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                    baris += 1;
                    if (baris == maxlines) {
                        if(boolmaxlines){
                            maxlines = maxlinespgdst;
                        }
                        //Add line
                        //lstData.add(drawLineSingleSpot());
                        //Add header for next page and restart counting baris
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris = 1;
                        lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                        baris += 1;
                        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                        baris += 1;
                    }
                    rowx = new Vector();
                    subTotalPrice = 0.00;
                    subTotalRec = 0.00;
                    subTotalQty = 0;
                    totalJual = 0.00;
                    subHargaJual=0;
                    subTotalHargaJual=0;
                    
                }
            }
            
            /*detail*/
            if(view==1){
                rowx.add("|");
                rowx.add(""+(i+1));
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                rowx.add(mat.getSku());

                rowx.add("<div align=\"center\">"+"|"+"</div>");
                rowx.add(""+mat.getBarCode()+" ");

                rowx.add("<div align=\"center\">"+"|"+"</div>");
                rowx.add(material_name);
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty())+"</div>");
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                if(objSrcReportReceive.getCurrencyId() == 0) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getCost() * rec.getTransRate())+"</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getCost())+"</div>");
                }
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                if(objSrcReportReceive.getCurrencyId() == 0) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * rmi.getCost() * rec.getTransRate())+"</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * rmi.getCost())+"</div>");
                }
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                if(objSrcReportReceive.getCurrencyId() == 0) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice())+"</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(mat.getDefaultPrice()/objStandartRate.getSellingRate())+"</div>");
                }
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                if(objSrcReportReceive.getCurrencyId() == 0) {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(rmi.getQty() * mat.getDefaultPrice())+"</div>");
                } else {
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal((rmi.getQty() * mat.getDefaultPrice())/objStandartRate.getSellingRate())+"</div>");
                }

                SrcMaterial srcMaterial = new SrcMaterial();
                //Material material = new Material();
                Hashtable memberPrice = SessMaterial.getPriceSaleInTypePriceMember(srcMaterial, mat, mat.getOID());
                if(listTypeHrga != null && listTypeHrga.size()>0){
                    for(int k = 0; k<listTypeHrga.size();k++){
                        PriceType pricetype = (PriceType)listTypeHrga.get(k); 
                        for(int j=0;j<listCurrStandardX.size();j++){
                            Vector temp = (Vector)listCurrStandardX.get(j);
                            Vector tempStand = (Vector)listCurrStandardX.get(j);
                            CurrencyType currx = (CurrencyType)tempStand.get(0);
                            StandartRate standart = (StandartRate)tempStand.get(1);

                            PriceTypeMapping pTypeMapping = null; 
                            if(memberPrice!=null && !memberPrice.isEmpty()){

                               pTypeMapping = (PriceTypeMapping) memberPrice.get(""+pricetype.getOID()+"_"+standart.getOID());
                            }

                           if(pTypeMapping==null) {
                                pTypeMapping= new PriceTypeMapping();
                                pTypeMapping.setMaterialId(mat.getOID());
                                pTypeMapping.setPriceTypeId(pricetype.getOID());
                                pTypeMapping.setStandartRateId(currx.getOID());
                            }    

                            if(j==0){
                                grandHargajual=grandHargajual+pTypeMapping.getPrice();
                                grandTotalHargajual=grandTotalHargajual+(pTypeMapping.getPrice()*rmi.getQty());

                                subHargaJual=subHargaJual+pTypeMapping.getPrice();
                                subTotalHargaJual=subTotalHargaJual+(pTypeMapping.getPrice()*rmi.getQty());
                            }

                            rowx.add("<div align=\"center\">"+"|"+"</div>");
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice())+"</div>");
                            rowx.add("<div align=\"center\">"+"|"+"</div>");
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(pTypeMapping.getPrice()*rmi.getQty())+"</div>");
                            rowx.add("<div align=\"center\">"+"|"+"</div>");
                            rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(((pTypeMapping.getPrice()-mat.getDefaultPrice())/mat.getDefaultPrice())*100)+"%</div>");
                        }
                    }
                }    
                rowx.add("<div align=\"center\">"+"|"+"</div>");
                lstData.add(rowx);
            }
            
            baris += 1;
            
            if (baris == maxlines) {
                if(boolmaxlines){
                    maxlines = maxlinespgdst;
                }
                //Add line
                //lstData.add(drawLineHorizontal());
                //Add header for next page and restart counting baris
                lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
                baris = 1;
                lstData.add(drawHeader(language,listCurrStandardX,listTypeHrga));
                baris += 1;
                lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
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
        
        lstData.add(drawLineHorizontal(listCurrStandardX,listTypeHrga));
        Vector rowx = new Vector();
        //Add sub total
        rowx.add("");
        rowx.add("");
        rowx.add("");//barcode
        rowx.add("");//barcode
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">SUB TOTAL</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalJual)+"</div>");
        
        //harga jual customer
        if(listTypeHrga != null && listTypeHrga.size()>0){
            for(int i = 0; i<listTypeHrga.size();i++){
                for(int j=0;j<listCurrStandardX.size();j++){
                    Vector temp = (Vector)listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType)temp.get(0);
                    PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                    //ctrlist.dataFormat("","1%","0","0","left","bottom");   // |
                    //ctrlist.dataFormat("","5%","0","0","right","bottom"); // total harga jual
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subHargaJual)+"</div>");
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(subTotalHargaJual)+"</div>");
                    rowx.add("");
                    rowx.add("");
                }
            }
        }

        rowx.add("<div align=\"center\">"+"|"+"</div>");
        
        lstData.add(rowx);
        lstData.add(drawLineTotal(listCurrStandardX,listTypeHrga));
        
        //Add TOTAL
        rowx = new Vector();
        rowx.add("");
        rowx.add("");
        rowx.add("");//barcode
        rowx.add("");//barcode
        rowx.add("");
        rowx.add("");
        rowx.add("");
        rowx.add("<div align=\"right\">TOTAL</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalQty)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalPrice)+"</div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        
        rowx.add("<div align=\"right\"></div>");
        rowx.add("<div align=\"center\">"+"|"+"</div>");
        rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(totalLastJual)+"</div>");
        
        if(listTypeHrga != null && listTypeHrga.size()>0){
            for(int i = 0; i<listTypeHrga.size();i++){
                for(int j=0;j<listCurrStandardX.size();j++){
                    Vector temp = (Vector)listCurrStandardX.get(j);
                    CurrencyType curr = (CurrencyType)temp.get(0);
                    PriceType pricetype = (PriceType)listTypeHrga.get(i); 
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandHargajual)+"</div>");
                    rowx.add("");
                    rowx.add("<div align=\"right\">"+FRMHandler.userFormatStringDecimal(grandTotalHargajual)+"</div>");
                    rowx.add("");
                    rowx.add("");
                }
            }
        }

        rowx.add("<div align=\"center\">"+"|"+"</div>");
        
        lstData.add(rowx);
        lstData.add(drawLineTotal(listCurrStandardX,listTypeHrga));
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
int view = FRMQueryString.requestInt(request, "view");
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
if(iCommand==Command.BACK || iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST) {
    try {
        srcReportReceive = (SrcReportReceive)session.getValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE);
        if (srcReportReceive == null) srcReportReceive = new SrcReportReceive();
    } catch(Exception e) {
        srcReportReceive = new SrcReportReceive();
    }
} else {
    frmSrcReportReceive.requestEntityObject(srcReportReceive);
    session.putValue(SessReportReceive.SESS_SRC_REPORT_RECEIVE_INVOICE, srcReportReceive);
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

<html><!-- #BeginTemplate "/Templates/main.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Dimata - ProChain POS</title>
<script language="JavaScript">
<!--

function cmdBack()
{
	document.frm_reportsale.command.value="<%=Command.BACK%>";
	document.frm_reportsale.action="src_reportreceiveinvoice.jsp";
	document.frm_reportsale.submit();
}

function printForm(){
	window.open("reportreceiveinvoice_form_print.jsp?view=<%=view%>","receivereportinvoice","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
}

function printFormExcel(){
    window.open("reportreceiveinvoice_list_excel.jsp?view=<%=view%>","receivereportinvoice","scrollbars=yes,height=600,width=800,status=no,toolbar=no,menubar=yes,location=no");
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
<link rel="stylesheet" href="../../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<%if(menuUsed == MENU_ICON){%>
    <link href="../../../stylesheets/general_home_style.css" type="text/css" rel="stylesheet" />
<%}%>
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
            <form name="frm_reportsale" method="post" action="">
              <input type="hidden" name="command" value="">
              <input type="hidden" name="add_type" value="">
              <input type="hidden" name="start" value="<%=start%>">
              <input type="hidden" name="approval_command">
              <input type="hidden" name="view" value="<%=view%>">
              <table width="100%" cellspacing="0" cellpadding="3">
                <tr align="left" valign="top"> 
                  <td height="14" colspan="3" align="center" ><h4><%=textListGlobal[SESS_LANGUAGE][1]%></h4></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td width="10%"><strong><%=textListGlobal[SESS_LANGUAGE][2]%></strong></td>
                  <td width="90%" colspan="2"><strong>:</strong>
                        <%=Formater.formatDate(srcReportReceive.getDateFrom(), "dd-MM-yyyy")%>
                        <%=textListGlobal[SESS_LANGUAGE][3]%>
                        <%=Formater.formatDate(srcReportReceive.getDateTo(), "dd-MM-yyyy")%>
                  </td>
                </tr>
                <tr align="left" valign="top"> 
                  <td><strong><%=textListGlobal[SESS_LANGUAGE][4]%></strong></td>
                  <td colspan="2"><strong>:</strong> <%=location.getName().toUpperCase()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td><strong><%=textListGlobal[SESS_LANGUAGE][7]%></strong></td>
                  <td colspan="2"><strong>:</strong> <%=objCurrencyType.getCode()%></td>
                </tr>
                <tr align="left" valign="top"> 
                  <td height="22" valign="middle" colspan="3">
                  <%
                      
                    Vector vectMember = new Vector(1,1);
                    String[] strMember = null;
                    Vector listTypeHrga =  new Vector ();
                    strMember = request.getParameterValues("FRM_FIELD_PRICE_TYPE_ID");
                    String sStrMember="";
                    if(strMember!=null && strMember.length>0) {
                           for(int i=0; i<strMember.length; i++) {
                                   try {
                                       if(strMember[i] != null && strMember[i].length()>0){ 
                                        vectMember.add(strMember[i]);
                                       sStrMember=sStrMember+strMember[i]+",";
                                       }
                                   }
                                   catch(Exception exc) {
                                           System.out.println("err");
                                   }
                           }
                           if(sStrMember != null && sStrMember.length()>0){
                               sStrMember=sStrMember.substring(0, sStrMember.length()-1);
                               String whereClauses = PstPriceType.fieldNames[PstPriceType.FLD_PRICE_TYPE_ID]+ " IN("+sStrMember+")";
                                listTypeHrga =  PstPriceType.list(0, 0, whereClauses, "");
                           }
                    }   
                      
                    Vector hasil = drawList(SESS_LANGUAGE,records,srcReportReceive,listTypeHrga, view);
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
                <tr align="left" valign="top"> 
                  <td height="18" valign="top" colspan="3"> <table width="100%" border="0">
                      <tr> 
                        <td width="80%"> <table width="52%" border="0" cellspacing="0" cellpadding="0">
                            <tr> 
                              <td class="command" nowrap width="90%"><a class="btn-primary btn-lg" href="javascript:cmdBack()"><i class="fa fa-backward"> &nbsp;<%=ctrLine.getCommand(SESS_LANGUAGE,textListGlobal[SESS_LANGUAGE][5],ctrLine.CMD_BACK_SEARCH,true)%></i></a></td>
                            </tr>
                          </table>
						</td>
                        <td width="20%">
						 <table width="100%" border="0" cellpadding="0" cellspacing="0">
                            <%
							if(records.size()>0){
							%>
							<tr> 
                              <td width="45%" nowrap>&nbsp; <a class="btn-primary btn-lg" href="javascript:printForm()" class="command"><i class="fa fa-print"> &nbsp;<%=textListGlobal[SESS_LANGUAGE][6]%></i></a></td>

                            </tr>
							<%}%>
                          </table>
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
